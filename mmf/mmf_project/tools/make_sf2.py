"""Build a SoundFont (SF2 2.01) from the MA-3 FM voices of one or more SMAF (.mmf) files, using the V1 synth (ma3_synth.py).

Usage: python3 -I make_sf2.py out.sf2 a.mmf [b.mmf ...]   (also writes sf2_map.csv next to out.sf2)

Design (PLAN GD2):
 * voices are de-duplicated by byte content (FM: 36 payload bytes; drum: key + PCM payload) -> one INSTRUMENT / one set of samples per voice;
 * preset = (bank, program): the first voice seen for a program number goes to bank 0, different voices with the same program
   number go to bank 1, 2, ... (same program number); two identical voices under different program numbers = two presets, one shared instrument;
 * drums: bank 128, preset 0 = first kit; a song whose (key, voice) pairs conflict with kit 0 goes to kit 1, 2, ...
 * melody voices: multisample every 3 semitones (roots = multiples of 3), baked envelope, loop if the voice sustains;
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
LOOP_START_S = 2.0                   # loop searched after this point (voice should have settled)
LOOP_MIN_S, LOOP_MAX_S = 0.25, 0.70
NONLOOP_CAP_S = 4.0
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

def render_melody_sample(fm, root):
    """-> dict(x float 44.1k, loop (a,b)|None, release_tc, stats)."""
    x = to_44k(S.render_fm_note(fm, root, int(5 * S.SR)))
    sr = OUT_SR
    e = env_db(x, 2205)                               # 50 ms blocks
    pk = e.max()
    def seg(t0, t1):
        return e[int(t0 / 0.05):int(t1 / 0.05)]
    rc, t60 = release_tc(fm, root)
    st = dict(sustain=False)
    if len(x) > 3.2 * sr:
        m1 = np.mean(seg(2.0, 2.5) ** 2) ** .5; m2 = np.mean(seg(2.6, 3.1) ** 2) ** .5
        ratio = m2 / (m1 + 1e-30)
        st.update(m_db=20 * math.log10(m1 / pk + 1e-30), ratio=ratio)
        st['sustain'] = (m1 > pk * 10 ** (-50 / 20)) and 0.9 < ratio < 1.1
    if st['sustain']:
        a = int(LOOP_START_S * sr); W = 2048
        ref = x[a:a + W]
        lo, hi = int(LOOP_MIN_S * sr), int(LOOP_MAX_S * sr)
        tgt = x[a + lo:a + hi + W]
        c = correlate(tgt, ref, mode='valid', method='fft')
        en = np.sqrt(np.convolve(tgt ** 2, np.ones(W), mode='valid') * np.sum(ref ** 2) + 1e-30)
        ncc = c / en
        L = lo + int(np.argmax(ncc)); st['ncc'] = float(ncc.max())
        b = a + L
        X = min(L // 3, int(0.06 * sr))
        y = x[:b + 8].copy()
        w = (np.arange(1, X + 1) / X)
        y[b - X:b] = (1 - w) * x[b - X:b] + w * x[a - X:a]
        y[b:b + 8] = x[a:a + 8]
        st['xfade_ms'] = 1000 * X / sr
        return dict(x=y, loop=(a, b), rel=rc, st=st)
    # non-looping: cut at -60 dB (cap 4 s)
    cap = int(NONLOOP_CAP_S * sr)
    idx = np.nonzero(e > pk * 1e-3)[0]
    end = min(cap, (idx[-1] + 2) * 2205) if len(idx) else 2205
    end = min(end, len(x))
    y = x[:end].copy()
    f = min(int(0.04 * sr), end // 4); y[-f:] *= np.linspace(1, 0, f)
    st['end_s'] = end / sr
    return dict(x=y, loop=None, rel=rc, st=st)

def render_drum_sample(key):
    x = S.render_drum(key, 0) * S.DRUM_GAIN
    x = to_44k(x)
    e = env_db(x, 441); pk = e.max()
    idx = np.nonzero(e > pk * 1e-3)[0]
    end = min(len(x), (idx[-1] + 2) * 441)
    y = x[:end].copy(); f = min(int(0.02 * OUT_SR), end // 4); y[-f:] *= np.linspace(1, 0, f)
    return dict(x=y, loop=None, rel=7500, st=dict(end_s=end / OUT_SR))

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

GEN = dict(keyRange=43, releaseVolEnv=38, sampleModes=54, rootKey=58, sampleID=53, instrument=41)

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
        else: ls, le = start, start
        shdr += name20(s['name']) + struct.pack('<IIIIIBbHH', start, end, ls, le, OUT_SR, s['root'], 0, 0, 1)
    shdr += name20('EOS') + struct.pack('<IIIIIBbHH', 0, 0, 0, 0, 0, 0, 0, 0, 0)
    ibag = b''; igen = b''; inst = b''
    for it in instruments:
        inst += name20(it['name']) + struct.pack('<H', len(ibag) // 4)
        for z in it['zones']:
            ibag += struct.pack('<HH', len(igen) // 4, 0)
            igen += gen_bytes(GEN['keyRange'], (z['lo'], z['hi']))
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
    outp = sys.argv[1]; srcs = sys.argv[2:]
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
            samples.append(dict(name='%s_%03d' % (h[:6], r), x=res['x'], loop=res['loop'], root=r))
            zones.append(dict(lo=0 if r == roots[0] else r - 1, hi=127 if r == roots[-1] else r + 1,
                              sample=sid, rel=res['rel'], loop=res['loop'] is not None, root=r))
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
    csvp = os.path.join(os.path.dirname(os.path.abspath(outp)), 'sf2_map.csv')
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
    for h, v in fm_voice.items():
        rs = [r for hh, r, res in report if hh == h]
        lp = [res for hh, r, res in report if hh == h and res['loop']]
        print('  voice %s (%s) roots %d..%d: %s' % (h[:6], v['song'], min(rs), max(rs), 'LOOP %d/%d' % (len(lp), len(rs)) if lp else 'no loop'))
    print('presets:', [(p['bank'], p['preset'], p['name']) for p in presets])
    if LOG: print('warnings:', len(LOG))
    return report, samples, g

if __name__ == '__main__':
    main()
