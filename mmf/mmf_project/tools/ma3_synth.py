"""MA-3 style 4-op FM synth driven by the voice parameters decoded from Mtsu (V1 / V2).

Operator/envelope/LFO/KSL/waveform models follow the measured YMF825/MA-5 clone `but80/fmfm` (MIT; tables re-typed here,
read as documentation). MA-3 (YMU762) is parameter-compatible but NOT measured -> timing/level details are MEDIUM confidence.
Drum voices in this file are PCM (type 01) that reference chip-internal waves -> approximated, LOW confidence.

Usage: python3 -I ma3_synth.py in.mmf out_dir [v1|v2|both]
"""
import sys, os, math
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np
from numba import njit
from scipy.signal import lfilter, resample_poly
from smaf_parse import parse_mmf, two_pass_notes, channel_setup
from ma3_voice import decode_all
from finish import master, write_flac

SR = 48000
FNUMCOEF = (1 << 19) / SR * 0.5
MULT2 = [1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 20, 24, 24, 30, 30]       # x2 table (MULTI 0 = 1/2)
LFO_HZ = [1.8, 4.0, 5.9, 7.0]
VIB_CENT = [3.4, 6.7, 13.5, 26.8]
TREM_DB = [1.3, 2.8, 5.8, 11.8]
FB_TAB = [0, 1/32, 1/16, 1/8, 1/4, 1/2, 1.0, 2.0]
MODMUL = 4.0
EPS = 1.0 / 32768.0
_VOLTAB_DB = [1e30, 47.9, 42.6, 37.2, 33.1, 29.8, 27.0, 24.6, 22.4, 20.6, 18.9, 17.3, 15.9, 14.6, 13.4, 12.2,
              11.1, 10.1, 9.2, 8.3, 7.4, 6.6, 5.8, 5.1, 4.4, 3.6, 3.0, 2.3, 1.7, 1.1, 0.6, 0.0]
VOLTAB = [0.0] + [10 ** (-d / 20) for d in _VOLTAB_DB[1:]]
DTCOEF = np.array([
    [0]*16,
    [0, 0, .05, .05, .05, .05, .09, .09, .14, .14, .18, .23, .27, .32, .37, .37],
    [.05, .05, .09, .09, .14, .14, .18, .23, .27, .32, .41, .46, .59, .64, .73, .73],
    [.09, .09, .14, .14, .18, .23, .28, .32, .41, .46, .59, .64, .87, .91, 1.0, 1.0],
    [0]*16,
    [-0, -0, -.05, -.05, -.05, -.05, -.09, -.09, -.14, -.14, -.18, -.23, -.27, -.32, -.37, -.37],
    [-.05, -.05, -.09, -.09, -.14, -.14, -.18, -.23, -.27, -.32, -.41, -.46, -.59, -.64, -.73, -.73],
    [-.09, -.09, -.14, -.14, -.18, -.23, -.28, -.32, -.41, -.46, -.59, -.64, -.87, -.91, -1.0, -1.0]])
DECAY_DB4 = np.array([
    [17.9342]*5 + [22.4116]*4 + [26.9076]*4 + [31.3661]*3,
    [17.9465, 22.4376, 22.4376, 31.4026, 31.4026, 44.8696, 44.8696, 62.7959, 62.7959, 89.6707, 89.6707, 125.5546, 125.5546, 179.2684, 179.2684, 250.9128]])
ATTACK_S1 = np.array([[3.07068, 3.07068, 3.07068, 2.45670, 2.45670, 2.04699, 2.04699, 1.75471, 1.75471],
                      [3.07082, 2.45660, 1.75489, 1.22816, 0.87737, 0.61414, 0.43876, 0.30714, 0.21935]])
CARRIER = [[0, 1, 0, 0], [1, 1, 0, 0], [1, 1, 1, 1], [0, 0, 0, 1], [0, 0, 0, 1], [0, 1, 0, 1], [1, 0, 0, 1], [1, 0, 1, 1]]

def build_tables():
    n = 1024; W = np.zeros((32, n))
    i = np.arange(n); sine = np.sin(2 * np.pi * i / n)
    def half(s): d = s.copy(); d[512:] = 0; return d
    def absw(s): d = s.copy(); d[512:] = s[:512]; return d
    def absq(s): d = np.zeros(n); d[:256] = s[:256]; d[512:768] = s[:256]; return d
    def octw(s): d = np.zeros(n); d[:512] = s[::2][:512]; return d
    def absoct(s): d = np.zeros(n); d[:256] = s[::2][:256]; d[256:512] = s[::2][:256]; return d
    W[0] = sine; W[1] = half(sine); W[2] = absw(sine); W[3] = absq(sine); W[4] = octw(sine); W[5] = absoct(sine)
    sq = np.where(i < 512, 1.0, -1.0); W[6] = sq; W[14] = half(sq); W[22] = absq(sq); W[30] = octw(W[14])
    for k in range(512):
        x = k * 16.0 / 256.0
        W[7][k] = 2 ** -x; W[7][1023 - k] = -2 ** -(x + 1 / 16)
    cs = np.clip(sine * math.sqrt(2), -1, 1); W[8] = cs; W[9] = half(cs); W[10] = absw(cs); W[11] = absq(cs)
    W[12] = octw(cs); W[13] = absoct(cs)
    tri = np.zeros(n)
    for k in range(256):
        tri[k] = k / 256; tri[256 + k] = (256 - k) / 256; tri[512 + k] = -k / 256; tri[768 + k] = -(256 - k) / 256
    W[16] = tri; W[17] = half(tri); W[18] = absw(tri); W[19] = absq(tri); W[20] = octw(tri); W[21] = absoct(tri)
    saw = np.zeros(n); saw[:512] = np.arange(512) / 512; saw[512:] = np.arange(512) / 512 - 1
    W[24] = saw; W[25] = half(saw); W[26] = absw(saw); W[27] = absq(saw); W[28] = octw(saw); W[29] = absoct(saw)
    # invalid WS (15,23,31) -> sine fallback
    for k in (15, 23, 31): W[k] = sine
    m = 8192; ph = np.arange(m) / m
    def tri_sin(p):
        p = p * 4
        return np.where(p < 1, p, np.where(p < 3, 2 - p, p - 4))
    def tri_cos(p):
        p = p * 4
        return np.where(p < 2, 1 - p, p - 3)
    VIB = np.array([2 ** (tri_sin(ph) * c / 1200) for c in VIB_CENT])
    TREM = np.array([10 ** (((tri_cos(ph) - 1) * .5 * d) / 20) for d in TREM_DB])
    KSL = np.zeros((4, 8, 32))
    bases = [0, .08, 1 / 15, 1 / 15]; bc = [0, 3.0, 1.5, 6.01]; fc = [0, .38, .185, .75]
    for k in range(4):
        for b in range(8):
            for f in range(32):
                f5 = min(f, 15)
                v = bases[k] - bc[k] * (b - 2) - fc[k] * (f5 - 7)
                if b < 2 or v >= 0: v = 0.0
                KSL[k, b, f] = 10 ** (v / 20)
    return W, VIB, TREM, KSL

WF, VIB, TREM, KSL = build_tables()

def note_params(fm, note):
    """Per-op constant array (4 x 16) for a note. Mirrors fmfm controller.writeFrequency + operator setters."""
    bo = fm['bo']
    freq = 440.0 * 2 ** ((note - 57) / 12)       # fmfm convention (A3Note=57); BO=1 puts it at standard pitch
    block = min(max((note + 3 - 12) // 12, 0), 7)
    fnum = int(freq * FNUMCOEF * 2.0 + ((1 << block) >> 1)) >> block
    while fnum > 1024:
        block += 1; fnum >>= 1
    block = min(max(block, 0), 7)
    base = (fnum << (block + 3 - bo)) / (16.0 * FNUMCOEF)
    ksn16 = min(max((block + 1 - bo) * 2 + (fnum >> 9), 0), 15)
    blkbo = min(max(block + 1 - bo, 0), 7)
    P = np.zeros((4, 16))
    for i, o in enumerate(fm['ops']):
        ksr = 1 if o['ksr'] else 0
        ksn_dt = (block << 1) | (fnum >> 9)
        f = base + DTCOEF[o['dt']][ksn_dt]
        P[i, 0] = f / SR * MULT2[o['multi']] / 2.0
        if o['ar'] > 0:
            ksn9 = (ksn16 >> 1) + (ksn16 & 1)
            sec = ATTACK_S1[ksr][ksn9] / (1 << (o['ar'] - 1))
            P[i, 1] = 1.0 / (sec * SR)
        def rate(r):
            if r == 0: return 1.0
            dbs = DECAY_DB4[ksr][ksn16] / 2.0
            dbp = dbs * (1 << r) / 16.0 / SR
            return 10 ** (-dbp / 10)
        P[i, 2] = rate(o['dr']); P[i, 3] = rate(o['sr']); P[i, 4] = rate(o['rr'])
        P[i, 5] = 0.0 if o['sl'] == 15 else 10 ** (-3.0 * o['sl'] / 20)
        tlc = 0.0 if o['tl'] >= 63 else 10 ** (o['tl'] * -0.75 / 20)
        P[i, 6] = KSL[o['ksl']][blkbo][fnum >> 5] * tlc
        P[i, 7] = o['ws']; P[i, 8] = FB_TAB[o['fb']]; P[i, 9] = 1 if o['eam'] else 0; P[i, 10] = o['dam']
        P[i, 11] = 1 if o['evb'] else 0; P[i, 12] = o['dvb']; P[i, 13] = 1 if o['xof'] else 0
        P[i, 14] = 1 if o['ar'] > 0 else 0
    return P

@njit(cache=True)
def _op(i, P, mod, mi, phase, level, stage, WF, VIB, TREM):
    inc = P[i, 0]
    if P[i, 11] > 0.5:
        inc = inc * VIB[int(P[i, 12]), mi]
    phase[i] = (phase[i] + inc) % 1.0
    st = stage[i]
    if st == 0:
        return 0.0
    if st == 1:
        level[i] += P[i, 1]
        if level[i] >= 1.0:
            level[i] = 1.0; stage[i] = 2; st = 2
    if st == 2:
        if P[i, 5] < level[i]:
            level[i] *= P[i, 2]
        else:
            stage[i] = 3; st = 3
    if st == 3:
        if EPS < level[i]:
            level[i] *= P[i, 3]
        else:
            stage[i] = 0
    elif st == 4:
        if EPS < level[i]:
            level[i] *= P[i, 4]
        else:
            level[i] = 0.0; stage[i] = 0
    env = level[i]
    if P[i, 9] > 0.5:
        env *= TREM[int(P[i, 10]), mi]
    idx = (int(phase[i] * 1024.0) + int(math.floor(mod * 1024.0))) & 1023
    return WF[int(P[i, 7]), idx] * env * P[i, 6]

@njit(cache=True)
def _render(P, alg, lfo_inc, nsamp, gate, WF, VIB, TREM, carrier):
    out = np.zeros(nsamp)
    phase = np.zeros(4); level = np.zeros(4); stage = np.zeros(4, np.int64)
    for i in range(4):
        if P[i, 14] > 0.5:
            stage[i] = 1
    nops = 2 if alg < 2 else 4
    fb1p = 0.0; fb1c = 0.0; fbo1 = 0.0; fb3p = 0.0; fb3c = 0.0; fbo3 = 0.0
    lp = 0.0
    n_end = nsamp
    for n in range(nsamp):
        if n == gate:
            for i in range(nops):
                if P[i, 13] < 0.5 and stage[i] != 0:
                    stage[i] = 4
        mi = int(lp * 8192.0) & 8191
        lp = (lp + lfo_inc) % 1.0
        o1 = 0.0; o2 = 0.0; o3 = 0.0; o4 = 0.0; r = 0.0
        if alg == 0:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            r = _op(1, P, o1 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
        elif alg == 1:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            r = o1 + o2
        elif alg == 2:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, fbo3, mi, phase, level, stage, WF, VIB, TREM)
            o4 = _op(3, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            r = o1 + o2 + o3 + o4
        elif alg == 3:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, o2 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            r = _op(3, P, (o1 + o3) * 4.0, mi, phase, level, stage, WF, VIB, TREM)
        elif alg == 4:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, o1 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, o2 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            r = _op(3, P, o3 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
        elif alg == 5:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, o1 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, fbo3, mi, phase, level, stage, WF, VIB, TREM)
            o4 = _op(3, P, o3 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            r = o2 + o4
        elif alg == 6:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, o2 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            o4 = _op(3, P, o3 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            r = o1 + o4
        else:
            o1 = _op(0, P, fbo1, mi, phase, level, stage, WF, VIB, TREM)
            o2 = _op(1, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            o3 = _op(2, P, o2 * 4.0, mi, phase, level, stage, WF, VIB, TREM)
            o4 = _op(3, P, 0.0, mi, phase, level, stage, WF, VIB, TREM)
            r = o1 + o3 + o4
        if P[0, 8] != 0.0:
            fb1p = fb1c; fb1c = o1 * P[0, 8]; fbo1 = fb1p * 0.5 + fb1c * 0.5
        if P[2, 8] != 0.0:
            fb3p = fb3c; fb3c = o3 * P[2, 8]; fbo3 = fb3p * 0.5 + fb3c * 0.5
        out[n] = r
        if n > gate:       # stop when every carrier is off after key-off
            alive = False
            for i in range(nops):
                if carrier[i] == 1 and stage[i] != 0:
                    alive = True
            if not alive:
                n_end = n + 1
                break
    return out[:n_end]

MAX_TAIL_S = 4.0
DRUM_GAIN = 2.5   # approximated drums are scaled so that their RMS sits ~9 dB under the FM parts (level of PCM drums is unknown)

def render_fm_note(fm, note, gate_samples):
    P = note_params(fm, note)
    nsamp = gate_samples + int(MAX_TAIL_S * SR)
    x = _render(P, fm['alg'], LFO_HZ[fm['lfo']] / SR, nsamp, gate_samples, WF, VIB, TREM,
                np.array(CARRIER[fm['alg']], dtype=np.int64))
    if len(x) == nsamp:                       # hit the tail cap: short fade to avoid a click
        f = int(0.3 * SR); x[-f:] *= np.linspace(1, 0, f)
    return x

# ---- drums: PCM voices (type 01) referencing chip-internal waves -> approximation (LOW confidence)
def render_drum(key, gate_samples):
    rng = np.random.default_rng(key)
    t = lambda sec: np.arange(int(sec * SR)) / SR
    if key in (35, 36):                         # kick (wave id 0x03)
        f0, f1 = (160.0, 48.0) if key == 36 else (130.0, 40.0)
        tt = t(0.35); f = f1 + (f0 - f1) * np.exp(-tt * 28)
        x = np.sin(2 * np.pi * np.cumsum(f) / SR) * np.exp(-tt * 11)
        x += 0.25 * rng.standard_normal(len(tt)) * np.exp(-tt * 200)
        return 0.9 * x
    if key == 40:                               # snare (wave id 0x0b)
        tt = t(0.30)
        n = rng.standard_normal(len(tt)); n = n - lfilter([1, -0.9], [1], n) * 0  # white
        n = lfilter([1, -1], [1, -0.55], n)     # crude high-pass
        x = 0.7 * n * np.exp(-tt * 16) + 0.5 * np.sin(2 * np.pi * 190 * tt) * np.exp(-tt * 28)
        return 0.6 * x
    if key in (41, 45):                         # toms (wave id 0x0d)
        f1 = 85.0 if key == 41 else 120.0
        tt = t(0.55); f = f1 + 0.5 * f1 * np.exp(-tt * 18)
        x = np.sin(2 * np.pi * np.cumsum(f) / SR) * np.exp(-tt * 7)
        return 0.9 * x
    if key in (49, 52):                         # crash / china (wave id 0x15)
        dur, k = (1.6, 3.2) if key == 49 else (1.1, 4.5)
        tt = t(dur); n = rng.standard_normal(len(tt))
        n = lfilter([1, -1], [1, -0.35], n)
        x = n * np.exp(-tt * k) * (0.5 if key == 49 else 0.45)
        if key == 52: x *= 1 + 0.5 * np.sin(2 * np.pi * 70 * tt)
        return x
    raise KeyError(key)

def pan_gains(pan):
    a = math.pi * 0.5 * min(max(pan, 0), 127) / 127.0
    return math.cos(a), math.sin(a)

def reverb_stereo(x, wet=0.16):
    """Small Schroeder-style stereo reverb (V2 only)."""
    outs = []
    for ch, (combs, aps) in enumerate((([1557, 1617, 1491, 1422], [225, 556]), ([1580, 1640, 1514, 1445], [248, 579]))):
        acc = np.zeros(len(x))
        for d in combs:
            b = np.zeros(d + 1); b[d] = 1.0
            a = np.zeros(d + 1); a[0] = 1.0; a[d] = -0.80
            acc += lfilter([1.0], a, x[:, ch] if x.ndim == 2 else x)
        y = acc / len(combs)
        for d in aps:
            a = np.zeros(d + 1); a[0] = 1.0; a[d] = 0.5
            b = np.zeros(d + 1); b[0] = -0.5; b[d] = 1.0
            y = lfilter(b, a, y)
        outs.append(y)
    return np.stack(outs, axis=1) * wet

def synth(info):
    voices = decode_all(info['mtsu'])
    fm_by_pc = {v['pc']: v['fm'] for v in voices if 'fm' in v and v['bank_msb'] == 0x7c}
    drum_keys = {v['drumnote'] for v in voices if 'pcm_raw' in v}
    setup = channel_setup(info)
    lay, notes = two_pass_notes(info)
    n_total = int(round(lay['total_ms'] / 1000 * SR))
    mono = np.zeros(n_total + 8 * SR)
    stereo = np.zeros((n_total + 8 * SR, 2))
    cache = {}
    stats = dict(fm=0, drum=0, skipped=0)
    for n in notes:
        s = setup[n['ch']]
        gate = int(round(n['dur_ms'] / 1000 * SR))
        if gate <= 0:
            continue
        if n['ch'] == 9:
            if n['key'] not in drum_keys:
                stats['skipped'] += 1; continue
            ck = ('d', n['key']); stats['drum'] += 1
            if ck not in cache: cache[ck] = render_drum(n['key'], gate)
        else:
            fm = fm_by_pc.get(s['prog'])
            if fm is None:
                stats['skipped'] += 1; continue
            ck = ('f', s['prog'], n['key'], gate); stats['fm'] += 1
            if ck not in cache: cache[ck] = render_fm_note(fm, n['key'], gate)
        w = cache[ck]
        if n['ch'] == 9: w = w * DRUM_GAIN
        att = VOLTAB[s['vol'] >> 2] * VOLTAB[127 >> 2] * VOLTAB[n['vel'] >> 2]
        pos = int(round(n['t_ms'] / 1000 * SR))
        end = min(pos + len(w), len(mono))
        seg = w[:end - pos] * att
        mono[pos:end] += seg
        gl, gr = pan_gains(s['pan'])
        stereo[pos:end, 0] += seg * gl; stereo[pos:end, 1] += seg * gr
    stats['unique_rendered'] = len(cache)
    return lay, n_total, mono[:n_total], stereo[:n_total], stats

def dc_block(x, r=0.9975):
    """~20 Hz one-pole DC blocker (several WS waveforms are unipolar)."""
    return lfilter([1.0, -1.0], [1.0, -r], x, axis=0)

def main():
    src, outdir = sys.argv[1], sys.argv[2]
    which = sys.argv[3] if len(sys.argv) > 3 else 'both'
    info = parse_mmf(src)
    lay, n_total, mono, stereo, stats = synth(info)
    print('synth stats', stats, 'samples', n_total, 'raw peak mono %.3f stereo %.3f' % (np.abs(mono).max(), np.abs(stereo).max()))
    assert np.isfinite(mono).all() and np.isfinite(stereo).all()
    if which in ('v1', 'both'):
        x = resample_poly(dc_block(mono), 147, 160)          # 48k -> 44.1k
        x = master(x, 44100)
        write_flac(x, 44100, os.path.join(outdir, 'bgm_20_v1_ma3_fm_goc.flac'), 16)
        print('wrote v1')
    if which in ('v2', 'both'):
        wetp = reverb_stereo(stereo)
        y = dc_block(stereo + wetp)
        y = master(y, SR)
        write_flac(y, SR, os.path.join(outdir, 'bgm_20_v2_ma3_fm_hifi.flac'), 24)
        print('wrote v2')

if __name__ == '__main__':
    main()
