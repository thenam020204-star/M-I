"""Build a SoundFont (SF2 2.01) from the MA-3 FM voices of one or more SMAF (.mmf) files, using the V1 synth (ma3_synth.py).

Usage: python3 -I make_sf2.py out.sf2 a.mmf [b.mmf ...] [--map out/sf2_map_x.csv]
       (map csv defaults to sf2_map.csv next to out.sf2)

Design (PLAN GD2):
 * voices are de-duplicated by byte content (FM: 36 payload bytes; drum: key + PCM payload) -> one INSTRUMENT / one set of samples per voice;
 * preset = (bank, program): the first voice seen for a program number goes to bank 0, different voices with the same program
   number go to bank 1, 2, ... (same program number); two identical voices under different program numbers = two presets, one shared instrument;
 * drums: bank 128, preset 0 = first kit; a song whose (key, voice) pairs conflict with kit 0 goes to kit 1, 2, ...
 * melody voices: multisample every 3 semitones (roots = multiples of 3), baked envelope. Loop design (PLAN GD4 "Thiet ke sua T7"):
   the dB-envelope of the rendered note is fitted by a line from the earliest point `a` on; slope ~0 -> plain loop, slope<0 ->
   loop on a gain-flattened region + SF2 hold(a)/decay(96 dB/|slope|)/sustain(max) generators (SF2 decay is linear in dB, like the synth),
   otherwise one-shot cut at -60 dB (cap 4 s). Loop length = integer multiple of the f0 period (and ~ LFO period if EAM/EVB active);
 * the SF2 writer is hand-made (RIFF sfbk); sf2utils is only used by the verification step.
"""
import sys, os, struct, hashlib, math, csv
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np
from scipy.signal import resample_poly, correlate
import ma3_synth as S
from ma3_voice import parse_sysex_voice
from smaf_parse import parse_mmf, two_pass_notes, channel_setup

OUT_SR = 44100
MIN_ROOT, MAX_ROOT = 24, 96          # minimum multisample coverage (union with used range +-12)
ANALYSIS_GATE_S = 8.0                # note length used to analyse the envelope / find the loop
A_MIN_S, A_MAX_S = 0.25, 3.0         # earliest / latest loop start considered
FIT_MAX_S, FIT_MIN_S = 4.0, 0.6      # linear-dB fit window (from a)
FIT_FLOOR_DB = 60.0                  # the fit window stops where the smoothed level is 60 dB under its peak (inaudible beyond)
FAINT_DB = -40.0                     # loop start quieter than this (rel. peak) -> not worth a loop, one-shot
SMOOTH_S = 0.5                       # power-mean window for the dB envelope (kills LFO/beat ripple)
TOL_DB, TOL_FLAT_DB = 0.7, 3.5       # decaying voices: max |residual| of the line; flat voices: RMS residual (beat/LFO ripple allowed)
FLAT_SLOPE = 0.9                     # |slope| (dB/s) below this = plain loop (SF2 cannot decay slower than ~0.95 dB/s: 8000 tc)
XFADE_S = 0.05
STEEP_SLOPE = -30.0                  # steeper than this -> one-shot (dies in < 2.4 s anyway)
LOOP_MIN_S, LOOP_MAX_S = 0.25, 0.70
NONLOOP_CAP_S = 4.0
DECAY_FULL_DB = 96.0                 # FluidSynth: decayVolEnv = time for a 96 dB fall (calibrated in the T7 test, see PLAN)
SUSTAIN_CB = 1000
LOG = []

def warn(msg):
    LOG.append(msg); print('WARN:', msg, file=sys.stderr)

# ---------------------------------------------------------------- song / voice collection
def split_sysex(mtsu):
    p = 0
    while p < len(mtsu):
        if mtsu[p] != 0xf0:
            raise ValueError('bad sysex start at %d' % p)
        ln = mtsu[p + 1]; body = mtsu[p + 2:p + 2 + ln]; p += 2 + ln
        if not body or body[-1] != 0xf7:
            raise ValueError('sysex without F7')
        yield body

def load_song(path):
    """-> dict(name, melody{pc:(hash,fm,lo,hi)}, drums{key:hash}, drum_voice{hash:key}, chans{ch:pc}, drum_keys_used) or None."""
    name = os.path.splitext(os.path.basename(path))[0]
    try:
        info = parse_mmf(path)
    except Exception as e:
        warn('%s: cannot read (%s) -> skipped' % (path, e)); return None
    if info.get('format') != 2:
        warn('%s: MTR format 0x%02x is not Mobile Standard (MA-3 layout assumed) -> skipped' % (name, info.get('format', -1))); return None
    if 'mtsp' in info:
        warn('%s: has Mtsp (PCM waves) -> PCM voices unsupported, FM voices kept' % name)
    try:
        defs = {}; drums = {}
        for body in split_sysex(info.get('mtsu', b'')):
            if body[:5] != b'\x43\x79\x06\x7f\x01':
                continue
            v = parse_sysex_voice(body)
            if v is None:
                continue
            pay = bytes(body[10:-1])
            if v['bank_msb'] == 0x7c and v['vtype'] == 0:
                defs[v['pc']] = (hashlib.sha1(pay).hexdigest()[:10], v['fm'])
            elif v['bank_msb'] == 0x7d and v['vtype'] == 1:
                drums[v['drumnote']] = hashlib.sha1(bytes([v['drumnote']]) + pay).hexdigest()[:10]
            else:
                warn('%s: unsupported voice bank %02x type %d pc=%d -> ignored' % (name, v['bank_msb'], v['vtype'], v['pc']))
        setup = channel_setup(info)
        lay, notes = two_pass_notes(info)
    except Exception as e:
        warn('%s: parse failure (%s) -> skipped' % (name, e)); return None
    melody = {}; chans = {}; dkeys = set()
    for n in notes:
        if n['ch'] == 9:
            if n['key'] in drums: dkeys.add(n['key'])
            else: warn('%s: drum key %d has no supported voice' % (name, n['key']))
            continue
        pc = setup[n['ch']]['prog']
        if pc not in defs:
            warn('%s: ch%d prog %d has no supported FM voice' % (name, n['ch'], pc)); continue
        h, fm = defs[pc]
        lo, hi = melody.get(pc, (h, fm, 127, 0))[2:]
        melody[pc] = (h, fm, min(lo, n['key']), max(hi, n['key']))
        chans[n['ch']] = pc
    return dict(name=name, melody=melody, drums={k: drums[k] for k in dkeys}, chans=chans)

# ---------------------------------------------------------------- sample rendering
def env_db(x, blk):
    n = len(x) // blk
    e = np.sqrt(np.mean(x[:n * blk].reshape(n, blk) ** 2, axis=1) + 1e-30)
    return e

def to_44k(x48):
    return resample_poly(S.dc_block(x48), 147, 160)

def release_tc(fm, note, forced=None):
    P = S.note_params(fm, note)
    nops = 2 if fm['alg'] < 2 else 4
    ts = []; ws = []
    for i in range(nops):
        if S.CARRIER[fm['alg']][i] != 1: continue
        o = fm['ops'][i]; q = P[i, 4]
        t = S.MAX_TAIL_S if (q >= 1.0 or o['xof']) else min(S.MAX_TAIL_S, math.log(1e-3) / math.log(q) / S.SR)
        ts.append(math.log(max(t, 1e-3))); ws.append(P[i, 6] + 1e-9)
    t60 = math.exp(np.average(ts, weights=ws))
    t_sf2 = t60 * 96.0 / 60.0                       # SF2 release = time to fall 96 dB (linear in dB), synth: 60 dB time t60
    return int(min(max(round(1200 * math.log2(max(t_sf2, 1e-3))), -12000), 8000)), t60

_BASE_FM = None
def base_hz(note, bo):
    """Frequency the synth really gives to a MULTI=1, DT=0 operator (fnum quantisation included)."""
    global _BASE_FM
    if _BASE_FM is None:
        op = dict(sr=0, xof=0, sus=1, ksr=0, rr=5, dr=0, ar=15, sl=0, tl=20, ksl=0, dam=0, eam=0, dvb=0, evb=0, multi=1, dt=0, ws=0, fb=0)
        _BASE_FM = dict(bo=1, lfo=0, alg=1, ops=[op] * 4)
    fm = dict(_BASE_FM); fm['bo'] = bo
    return S.note_params(fm, note)[0, 0] * S.SR

def voice_traits(fm):
    nops = 2 if fm['alg'] < 2 else 4
    ops = fm['ops'][:nops]
    lfo = any(o['eam'] or (o['evb'] and o['dvb'] >= 1) for o in ops)
    half = any(o['multi'] == 0 for o in ops)
    return lfo, half

def power_db(x, win, hop):
    """Centered sliding mean-power in dB -> (centre sample indexes, dB)."""
    c = np.concatenate([[0.0], np.cumsum(x * x)])
    st = np.arange(0, len(x) - win + 1, hop)
    m = (c[st + win] - c[st]) / win
    return st + win // 2, 10 * np.log10(m + 1e-20)

def analyse_envelope(x, sr):
    """Earliest point a (s) from which the smoothed dB envelope is a straight line over >= FIT_MIN_S
    (window extended up to FIT_MAX_S or to -60 dB). -> dict(ok, a, slope, resid, why)."""
    win = int(SMOOTH_S * sr); hop = int(0.02 * sr)
    tc, es = power_db(x[:int(ANALYSIS_GATE_S * sr)], win, hop)
    t = tc / sr; pk = es.max()
    a = A_MIN_S; why = 'no linear dB segment'
    while a <= A_MAX_S + 1e-9:
        i0 = int(np.searchsorted(t, a))
        if i0 >= len(t): break
        low = np.nonzero(es[i0:] < pk - FIT_FLOOR_DB)[0]
        i1 = i0 + (low[0] if len(low) else len(es) - i0)       # exclusive
        i1 = min(i1, int(np.searchsorted(t, a + FIT_MAX_S)))
        if i1 - i0 < 3 or t[i1 - 1] - a < FIT_MIN_S:
            why = 'dies (-60 dB) before 0.6 s of line'; break
        tt, yy = t[i0:i1], es[i0:i1]
        c = np.polyfit(tt, yy, 1); r = np.abs(yy - np.polyval(c, tt)).max()
        s = c[0]
        rms = float(np.sqrt(np.mean((yy - np.polyval(c, tt)) ** 2)))
        flat = abs(s) < FLAT_SLOPE
        if ((flat and rms <= TOL_FLAT_DB) or (not flat and r <= TOL_DB)) and s <= FLAT_SLOPE:
            return dict(ok=True, a=float(tt[0]), slope=float(s), resid=float(rms if flat else r), mean_db=float(10 * np.log10(np.mean(10 ** (yy / 10)))), span=float(tt[-1] - tt[0]),
                        lvl_db=float(np.polyval(c, tt[0]) - pk))
        a += 0.02
    return dict(ok=False, why=why)

def loop_candidates(T, sr, lfo_on, tl):
    out = set()
    if lfo_on:
        m = 1
        while m * tl <= 1.0 * sr:
            Lt = m * tl
            if Lt >= LOOP_MIN_S * sr:
                tol = max(T, 0.01 * sr)
                k0, k1 = int(math.ceil((Lt - tol) / T)), int(math.floor((Lt + tol) / T))
                ks = [k for k in range(max(k0, 1), k1 + 1)]
                good = [k for k in ks if abs(k * T - round(k * T)) <= 0.1]
                if len(good) < 3: good = sorted(ks, key=lambda k: abs(k * T - round(k * T)))[:6]
                out.update(good)
            m += 1
        if out: return sorted(out)
    k0, k1 = int(math.ceil(LOOP_MIN_S * sr / T)), int(math.floor(LOOP_MAX_S * sr / T))
    ks = list(range(max(k0, 1), max(k1, k0) + 1))
    good = [k for k in ks if abs(k * T - round(k * T)) <= 0.1]
    if len(good) < 6: good = sorted(ks, key=lambda k: abs(k * T - round(k * T)))[:10]
    return sorted(good)

def joint_metrics(y, a, b):
    """Click metrics on the looped signal: value step and slope step at the join relative to local medians."""
    n0 = min(4000, b - a)
    z = np.concatenate([y[b - n0:b], y[a:a + 4000]]); j = n0
    d = np.diff(z)                          # d[i] = z[i+1]-z[i]; join step is d[j-1]
    step = abs(d[j - 1]); med = np.median(np.abs(d)) + 1e-12
    dd = np.diff(d); sl = abs(dd[j - 2]); med2 = np.median(np.abs(dd)) + 1e-12
    return float(step / med), float(sl / med2)

def measure_release_tc(x, sr, gate_s, fallback_tc):
    """Release time (SF2 timecents) from the synth's own note-off tail: fit a straight line (dB vs time) to the 20 ms RMS of the
    first 40 dB after the key-off; SF2 release is linear in dB (96 dB per release time in FluidSynth). Falls back to the RR-based
    estimate when the note has already died before the gate or the tail is too short."""
    g = int(gate_s * sr)
    if len(x) < g + int(0.1 * sr):
        return fallback_tc, None
    blk = int(0.02 * sr)
    e = 20 * np.log10(env_db(x[g - 5 * blk:], blk) + 1e-12)
    ref = e[:5].max(); tail = e[5:]
    lim = np.nonzero(tail < ref - 40)[0]
    n = lim[0] if len(lim) else len(tail)
    if n < 3:
        return fallback_tc, None
    t = (np.arange(n) + 0.5) * 0.02
    c = np.polyfit(np.concatenate([[0.0], t]), np.concatenate([[ref], tail[:n]]), 1)
    slope = -c[0]
    if slope < 0.5:
        return fallback_tc, None
    return int(min(max(round(1200 * math.log2(DECAY_FULL_DB / slope)), -12000), 8000)), float(slope)

def render_melody_sample(fm, root):
    """-> dict(x float 44.1k, loop (a,b)|None, rel, hold_tc, decay_tc, sust_cb, pcorr, st)."""
    sr = OUT_SR
    x = to_44k(S.render_fm_note(fm, root, int(ANALYSIS_GATE_S * S.SR)))
    e = env_db(x, 2205)                               # 50 ms blocks
    pk = e.max()
    rc0, t60 = release_tc(fm, root)
    rc, rel_slope = measure_release_tc(x, sr, ANALYSIS_GATE_S, rc0)
    pcorr = -int(round(1200 * math.log2(base_hz(root, fm['bo']) / (440.0 * 2 ** ((root - 69) / 12)))))   # SF2: correction = cents to ADD to play in tune
    st = dict(kind='oneshot', rel_dbps=rel_slope)
    lfo_on, half = voice_traits(fm)
    env = analyse_envelope(x, sr) if len(x) > (A_MIN_S + 1.5) * sr else dict(ok=False, why='note too short')
    if env['ok'] and env['slope'] < STEEP_SLOPE:
        env = dict(ok=False, why='steep decay %.1f dB/s' % env['slope'])
    if env['ok'] and env['lvl_db'] < FAINT_DB:
        env = dict(ok=False, why='faint at loop start (%.0f dB)' % env['lvl_db'])
    if env['ok']:
        s = env['slope']; plain = abs(s) < FLAT_SLOPE
        a0 = int(env['a'] * sr)
        T = sr / (base_hz(root, fm['bo']) / (2.0 if half else 1.0))
        tl = sr / S.LFO_HZ[fm['lfo']]
        cand = loop_candidates(T, sr, lfo_on, tl)
        W = 4096
        tt = np.arange(len(x)) / sr
        flat_s = 0.0 if plain else s
        xf = x * 10 ** (-flat_s * (tt - env['a']) / 20)
        # a_s: position within 50 ms of a0 whose natural sample step / slope step (the loop join, after the crossfade) is smallest
        # relative to the local median |dx|, |ddx|
        lo_i = a0; hi_i = a0 + int(0.05 * sr)
        seg = xf[lo_i - 2:hi_i + 1]
        d1 = np.abs(np.diff(seg)); d2 = np.abs(np.diff(seg, 2))
        md1 = np.median(np.abs(np.diff(xf[a0:a0 + int(3 * T) + 64]))) + 1e-9
        md2 = np.median(np.abs(np.diff(xf[a0:a0 + int(3 * T) + 64], 2))) + 1e-9
        sc = d1[:len(d2)] / md1 + d2 / md2                                 # index j <-> position lo_i - 1 + j
        a_s = lo_i - 1 + int(np.argmin(sc))
        ref = xf[a_s:a_s + W]; refp = xf[a_s - W:a_s]
        den = np.sum(ref ** 2) + np.sum(refp ** 2) + 1e-30
        best = None
        for k in cand:
            L = int(round(k * T))
            if a_s + L + W + 8 > len(xf): continue
            fr = abs(k * T - L)
            cost = (np.sum((ref - xf[a_s + L:a_s + L + W]) ** 2) + np.sum((refp - xf[a_s + L - W:a_s + L]) ** 2)) / den + 0.1 * fr * fr
            cost += 0.15 * L / sr                                                # prefer short loops (file size)
            if plain:                                                            # loop level should equal the long-term mean level
                dm = 10 * math.log10(np.mean(xf[a_s:a_s + L] ** 2) + 1e-20) - env['mean_db']
                cost += 0.05 * dm * dm
            if best is None or cost < best[0]: best = (cost, L, k, fr)
        if best is None:
            env = dict(ok=False, why='no loop candidate fits the analysed note')
        else:
            cost, L, k, fr = best; b = a_s + L
            nm = float((np.sum((ref - xf[b:b + W]) ** 2) + np.sum((refp - xf[b - W:b]) ** 2)) / den)
            y0 = np.concatenate([x[:a_s], xf[a_s:b + 8]]); y0[b:b + 8] = y0[a_s:a_s + 8]
            X = min(L // 3, int(XFADE_S * sr))
            A, B = xf[b - X:b], xf[a_s - X:a_s]
            rho = float(np.clip(np.dot(A, B) / (np.sqrt(np.dot(A, A) * np.dot(B, B)) + 1e-30), -0.5, 1.0))
            w = np.arange(1, X + 1) / X
            y1 = y0.copy()
            y1[b - X:b] = ((1 - w) * A + w * B) / np.sqrt((1 - w) ** 2 + w ** 2 + 2 * w * (1 - w) * rho)   # power-preserving crossfade
            y1[b:b + 8] = y1[a_s:a_s + 8]
            m1 = joint_metrics(y1, a_s, b)
            y = y1
            st.update(kind='loop' if plain else 'loop+decay', a=a_s / sr, L=L / sr, k=k, frac=fr, nmse=nm, rho=rho, slope=s,
                      resid=env['resid'], xfade_ms=1000 * X / sr, joint=m1, lvl_db=env['lvl_db'])
            hold_tc = decay_tc = sust = None
            if not plain:
                hold_tc = int(round(1200 * math.log2(max(a_s / sr - 0.001, 0.001))))
                decay_tc = int(min(round(1200 * math.log2(DECAY_FULL_DB / abs(s))), 8000))
                sust = SUSTAIN_CB
            return dict(x=y, loop=(a_s, b), rel=rc, hold_tc=hold_tc, decay_tc=decay_tc, sust_cb=sust, pcorr=pcorr, st=st)
    st['why'] = env['why']
    # one-shot: cut at -60 dB (cap 4 s)
    cap = int(NONLOOP_CAP_S * sr)
    idx = np.nonzero(e > pk * 1e-3)[0]
    end = min(cap, (idx[-1] + 2) * 2205) if len(idx) else 2205
    end = min(end, len(x))
    y = x[:end].copy()
    f = min(int(0.04 * sr), end // 4); y[-f:] *= np.linspace(1, 0, f)
    st['end_s'] = end / sr; st['capped'] = end >= cap
    return dict(x=y, loop=None, rel=rc, hold_tc=None, decay_tc=None, sust_cb=None, pcorr=pcorr, st=st)

def render_drum_sample(key):
    x = S.render_drum(key, 0) * S.DRUM_GAIN
    x = to_44k(x)
    e = env_db(x, 441); pk = e.max()
    idx = np.nonzero(e > pk * 1e-3)[0]
    end = min(len(x), (idx[-1] + 2) * 441)
    y = x[:end].copy(); f = min(int(0.02 * OUT_SR), end // 4); y[-f:] *= np.linspace(1, 0, f)
    return dict(x=y, loop=None, rel=7500, hold_tc=None, decay_tc=None, sust_cb=None, pcorr=0, st=dict(kind='oneshot', end_s=end / OUT_SR))

# ---------------------------------------------------------------- SF2 writer
def zstr(s, n=None):
    b = s.encode('ascii', 'replace') + b'\0'
    if n: return b[:n - 1].ljust(n, b'\0') if len(b) > n else b.ljust(n, b'\0')
    return b + (b'\0' if len(b) % 2 else b'')

def chunk(cid, data):
    return cid + struct.pack('<I', len(data)) + data + (b'\0' if len(data) % 2 else b'')

def lst(kind, parts):
    d = kind + b''.join(parts)
    return b'LIST' + struct.pack('<I', len(d)) + d

def name20(s):
    return s.encode('ascii', 'replace')[:19].ljust(20, b'\0')

GEN = dict(holdVolEnv=35, decayVolEnv=36, sustainVolEnv=37, releaseVolEnv=38, instrument=41, keyRange=43, sampleID=53, sampleModes=54, rootKey=58)   # SF2 2.01 sec. 8.1.2

def gen_bytes(oper, val):
    if oper == GEN['keyRange']: return struct.pack('<HBB', oper, val[0], val[1])
    if oper in (GEN['sampleID'], GEN['instrument'], GEN['sampleModes']): return struct.pack('<HH', oper, val)
    return struct.pack('<Hh', oper, val)

def write_sf2(path, samples, instruments, presets, title):
    """samples: list of dict(name, pcm int16, loop(a,b)|None, root). instruments: list of dict(name, zones=[dict(lo,hi,sample,rel,loop,root)]).
    presets: list of dict(name, bank, preset, inst)."""
    smpl = bytearray(); shdr = b''
    for s in samples:
        start = len(smpl) // 2
        smpl += s['pcm'].astype('<i2').tobytes()
        end = len(smpl) // 2
        smpl += bytes(46 * 2)
        if s['loop']: ls, le = start + s['loop'][0], start + s['loop'][1]
        else:                                   # not looping: harmless but valid-looking points (some tools warn on ls==le)
            m = min(64, len(s['pcm']) // 4); ls, le = start + m, end - m
        shdr += name20(s['name']) + struct.pack('<IIIIIBbHH', start, end, ls, le, OUT_SR, s['root'], s.get('pcorr', 0), 0, 1)
    shdr += name20('EOS') + struct.pack('<IIIIIBbHH', 0, 0, 0, 0, 0, 0, 0, 0, 0)
    ibag = b''; igen = b''; inst = b''
    for it in instruments:
        inst += name20(it['name']) + struct.pack('<H', len(ibag) // 4)
        for z in it['zones']:
            ibag += struct.pack('<HH', len(igen) // 4, 0)
            igen += gen_bytes(GEN['keyRange'], (z['lo'], z['hi']))
            if z.get('hold_tc') is not None: igen += gen_bytes(GEN['holdVolEnv'], z['hold_tc'])
            if z.get('decay_tc') is not None: igen += gen_bytes(GEN['decayVolEnv'], z['decay_tc'])
            if z.get('sust_cb') is not None: igen += gen_bytes(GEN['sustainVolEnv'], z['sust_cb'])
            igen += gen_bytes(GEN['releaseVolEnv'], z['rel'])
            if z['loop']: igen += gen_bytes(GEN['sampleModes'], 1)
            igen += gen_bytes(GEN['rootKey'], z['root'])
            igen += gen_bytes(GEN['sampleID'], z['sample'])
    inst += name20('EOI') + struct.pack('<H', len(ibag) // 4)
    ibag += struct.pack('<HH', len(igen) // 4, 0)
    igen += struct.pack('<HH', 0, 0)
    imod = bytes(10)
    pbag = b''; pgen = b''; phdr = b''
    for p in presets:
        phdr += name20(p['name']) + struct.pack('<HHHIII', p['preset'], p['bank'], len(pbag) // 4, 0, 0, 0)
        pbag += struct.pack('<HH', len(pgen) // 4, 0)
        pgen += gen_bytes(GEN['instrument'], p['inst'])
    phdr += name20('EOP') + struct.pack('<HHHIII', 0, 0, len(pbag) // 4, 0, 0, 0)
    pbag += struct.pack('<HH', len(pgen) // 4, 0)
    pgen += struct.pack('<HH', 0, 0)
    pmod = bytes(10)
    info = lst(b'INFO', [chunk(b'ifil', struct.pack('<HH', 2, 1)), chunk(b'isng', zstr('EMU8000')),
                         chunk(b'INAM', zstr(title)), chunk(b'ISFT', zstr('make_sf2.py (MA-3 FM)'))])
    sdta = lst(b'sdta', [chunk(b'smpl', bytes(smpl))])
    pdta = lst(b'pdta', [chunk(b'phdr', phdr), chunk(b'pbag', pbag), chunk(b'pmod', pmod), chunk(b'pgen', pgen),
                         chunk(b'inst', inst), chunk(b'ibag', ibag), chunk(b'imod', imod), chunk(b'igen', igen), chunk(b'shdr', shdr)])
    body = b'sfbk' + info + sdta + pdta
    with open(path, 'wb') as f:
        f.write(b'RIFF' + struct.pack('<I', len(body)) + body)

# ---------------------------------------------------------------- assembly
def main():
    args = [a for a in sys.argv[1:] if a != '--verbose']; mapp = None
    if '--map' in args:
        i = args.index('--map'); mapp = args[i + 1]; del args[i:i + 2]
    outp = args[0]; srcs = args[1:]
    songs = [s for s in (load_song(p) for p in srcs) if s]
    if not songs:
        print('no usable songs'); sys.exit(1)
    # --- registry of unique voices
    fm_voice = {}      # hash -> dict(fm, lo, hi, song)
    drum_voice = {}    # hash -> dict(key, song)
    prog_variants = {} # pc -> [hash,...] (index = bank)
    kits = []          # list of {key: hash}
    rows = []
    for s in songs:
        for pc, (h, fm, lo, hi) in s['melody'].items():
            v = fm_voice.setdefault(h, dict(fm=fm, lo=lo, hi=hi, song=s['name']))
            v['lo'] = min(v['lo'], lo); v['hi'] = max(v['hi'], hi)
            var = prog_variants.setdefault(pc, [])
            if h not in var: var.append(h)
        for k, h in s['drums'].items():
            drum_voice.setdefault(h, dict(key=k, song=s['name']))
        kit_i = None
        for j, kit in enumerate(kits):
            if all(kit.get(k, h) == h for k, h in s['drums'].items()): kit_i = j; break
        if kit_i is None and s['drums']:
            kits.append({}); kit_i = len(kits) - 1
        if s['drums']: kits[kit_i].update(s['drums'])
        s['kit'] = kit_i
    # --- render samples
    samples = []; instruments = []; inst_of = {}
    report = []
    for h, v in fm_voice.items():
        lo = (min(MIN_ROOT, max(v['lo'] - 12, 0)) // 3) * 3
        hi = -(-max(MAX_ROOT, min(v['hi'] + 12, 120)) // 3) * 3
        roots = list(range(lo, hi + 1, 3))
        zones = []
        for r in roots:
            res = render_melody_sample(v['fm'], r)
            sid = len(samples)
            samples.append(dict(name='%s_%03d' % (h[:6], r), x=res['x'], loop=res['loop'], root=r, pcorr=res['pcorr']))
            zones.append(dict(lo=0 if r == roots[0] else r - 1, hi=127 if r == roots[-1] else r + 1,
                              sample=sid, rel=res['rel'], loop=res['loop'] is not None, root=r,
                              hold_tc=res['hold_tc'], decay_tc=res['decay_tc'], sust_cb=res['sust_cb']))
            report.append((h, r, res))
        inst_of[('f', h)] = len(instruments)
        instruments.append(dict(name=('%s %s' % (h[:6], v['song']))[:19], zones=zones))
    for h, v in drum_voice.items():
        res = render_drum_sample(v['key'])
        sid = len(samples)
        samples.append(dict(name='D%03d_%s' % (v['key'], h[:6]), x=res['x'], loop=None, root=v['key']))
        inst_of[('d', h)] = len(instruments)
        instruments.append(dict(name=('D%03d %s' % (v['key'], v['song']))[:19],
                                zones=[dict(lo=v['key'], hi=v['key'], sample=sid, rel=res['rel'], loop=False, root=v['key'])]))
        report.append((h, v['key'], res))
    # --- quantise with one global gain (relative levels as in V1)
    peak = max(np.abs(s['x']).max() for s in samples)
    g = 0.97 / peak
    for s in samples:
        s['pcm'] = np.clip(np.round(s['x'] * g * 32767), -32768, 32767).astype(np.int16)
    # --- presets
    presets = []
    first_song = {}
    for s in songs:
        for pc, (h, fm, lo, hi) in s['melody'].items():
            first_song.setdefault(h, s['name'])
    for pc, var in prog_variants.items():
        for bank, h in enumerate(var):
            presets.append(dict(name=('P%03d %s' % (pc, first_song[h]))[:19], bank=bank, preset=pc, inst=inst_of[('f', h)]))
    for j, kit in enumerate(kits):
        # one instrument per kit: all zones of the kit's voices
        zs = []
        for k, h in sorted(kit.items()):
            zs += instruments[inst_of[('d', h)]]['zones']
        instruments.append(dict(name='MA3 Drums' if j == 0 else 'MA3 Drums %d' % (j + 1), zones=zs))
        presets.append(dict(name='MA3 Drums' if j == 0 else 'MA3 Drums %d' % (j + 1), bank=128, preset=j, inst=len(instruments) - 1))
    presets.sort(key=lambda p: (p['bank'], p['preset']))
    write_sf2(outp, samples, instruments, presets, 'MA3 FM bgm')
    # --- map csv
    csvp = mapp or os.path.join(os.path.dirname(os.path.abspath(outp)), 'sf2_map.csv')
    with open(csvp, 'w', newline='') as f:
        w = csv.writer(f); w.writerow(['song', 'channel', 'smaf_prog', 'drum_key', 'sf2_bank', 'sf2_preset', 'voice_hash'])
        for s in songs:
            for ch, pc in sorted(s['chans'].items()):
                h = s['melody'][pc][0]
                w.writerow([s['name'], ch, pc, '', prog_variants[pc].index(h), pc, h])
            for k, h in sorted(s['drums'].items()):
                w.writerow([s['name'], 9, '', k, 128, s['kit'], h])
    # --- report
    nloop = sum(1 for s in samples if s['loop'])
    print('samples %d (loop %d), instruments %d, presets %d, file %d B, gain %.4f' % (len(samples), nloop, len(instruments), len(presets), os.path.getsize(outp), g))
    from collections import Counter
    for h, v in fm_voice.items():
        rr = [(r, res) for hh, r, res in report if hh == h]
        kinds = Counter(res['st']['kind'] for r, res in rr)
        nsm = sum(len(res['x']) for r, res in rr)
        print('  voice %s (%s) roots %d..%d: %s, %d smp (%.1f MB)' % (h[:6], v['song'], rr[0][0], rr[-1][0],
              ' '.join('%s=%d' % kv for kv in sorted(kinds.items())), nsm, nsm * 2 / 1e6))
        if '--verbose' in sys.argv:
            for r, res in rr:
                print('     root %3d %s' % (r, {k: (round(x, 3) if isinstance(x, float) else x) for k, x in res['st'].items()}))
    print('presets:', [(p['bank'], p['preset'], p['name']) for p in presets])
    if LOG: print('warnings:', len(LOG))
    return report, samples, g

if __name__ == '__main__':
    main()
