"""V6: NES-style chiptune render (pulse 12.5/25/50 %, triangle, LFSR-ish noise), 4-bit amplitude steps, mono 44.1k/16.
Usage: python3 -I chiptune.py in.mmf out.flac"""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np
from smaf_parse import parse_mmf, two_pass_notes, channel_setup
from finish import master, write_flac

SR = 44100
# channel -> (wave, duty, style, level). style: 'pluck' (exp decay) | 'hold' | 'pad'; vib: vibrato depth cents
ROLE = {
    0: ('pulse', .25, 'pluck', 1.0, 0), 1: ('pulse', .125, 'pluck', 0.9, 0), 2: ('tri', 0, 'hold', 1.0, 0),
    3: ('pulse', .5, 'hold', 0.7, 0), 4: ('pulse', .25, 'hold', 0.8, 12), 5: ('pulse', .5, 'hold', 1.0, 10),
    6: ('tri', 0, 'hold', 1.0, 0), 7: ('pulse', .125, 'pad', 0.6, 0), 8: ('pulse', .25, 'pluck', 0.9, 0),
    10: ('pulse', .5, 'pad', 0.5, 0), 11: ('pulse', .125, 'pluck', 0.9, 0), 12: ('pulse', .25, 'pad', 0.6, 0),
    13: ('pulse', .5, 'pad', 0.5, 0), 14: ('pulse', .5, 'hold', 0.7, 10), 15: ('pulse', .25, 'pluck', 0.6, 0),
}

def hz(k): return 440.0 * 2 ** ((k - 69) / 12)

def render_pitched(ch, key, dur_s, att):
    wave, duty, style, lvl, vib = ROLE[ch]
    rel = 0.04
    n = int((dur_s + rel) * SR)
    t = np.arange(n) / SR
    f = hz(key) * np.ones(n)
    if vib:
        f = f * 2 ** ((vib / 1200) * np.sin(2 * np.pi * 5.5 * np.maximum(t - 0.15, 0)) * np.minimum((t - 0.15) / 0.2, 1).clip(0))
    ph = np.cumsum(f) / SR
    fr = ph % 1.0
    if wave == 'pulse':
        x = np.where(fr < duty, 1.0, -1.0)
    else:
        x = 4 * np.abs(fr - 0.5) - 1.0
        x = np.round((x + 1) * 7.5) / 7.5 - 1.0           # 4-bit triangle steps
    if style == 'pluck':
        env = np.exp(-t / 0.22)
    elif style == 'pad':
        env = np.minimum(t / 0.01, 1.0) * (0.75 + 0.25 * np.exp(-t / 0.3))
    else:
        env = np.minimum(t / 0.004, 1.0) * (0.85 + 0.15 * np.exp(-t / 0.12))
    env = env.copy()
    ro = int(dur_s * SR)
    env[ro:] *= np.linspace(1, 0, n - ro) if n > ro else 1
    if wave != 'tri':
        env = np.round(env * 15) / 15                      # 4-bit volume steps
    return x * env * lvl * att

def render_drum(key, rng):
    if key in (35, 36):
        n = int(0.16 * SR); t = np.arange(n) / SR
        f = 55 + 120 * np.exp(-t * 30); ph = np.cumsum(f) / SR
        x = 4 * np.abs(ph % 1 - .5) - 1; return x * np.exp(-t * 14) * 1.1
    if key in (41, 45):
        n = int(0.28 * SR); t = np.arange(n) / SR
        f0 = 95.0 if key == 41 else 130.0
        f = f0 + 70 * np.exp(-t * 25); ph = np.cumsum(f) / SR
        x = 4 * np.abs(ph % 1 - .5) - 1; return x * np.exp(-t * 9)
    def noise(n, hold):
        v = rng.integers(0, 2, n // hold + 2) * 2.0 - 1.0
        return np.repeat(v, hold)[:n]
    if key == 40:
        n = int(0.20 * SR); t = np.arange(n) / SR
        x = noise(n, 2) * np.exp(-t * 20); return np.round(x * 15) / 15 * 0.8
    n = int((0.7 if key == 49 else 0.45) * SR); t = np.arange(n) / SR
    x = noise(n, 1) * np.exp(-t * (6 if key == 49 else 9)); return np.round(x * 15) / 15 * 0.6

def main():
    src, dst = sys.argv[1], sys.argv[2]
    info = parse_mmf(src)
    lay, notes = two_pass_notes(info)
    setup = channel_setup(info)
    total = int(round(lay['total_ms'] / 1000 * SR))
    mix = np.zeros(total + 5 * SR)
    rng = np.random.default_rng(7)
    cache = {}
    for n in notes:
        s = setup[n['ch']]
        att = (s['vol'] / 127.0) * (n['vel'] / 127.0) ** 0.5
        pos = int(round(n['t_ms'] / 1000 * SR))
        if n['ch'] == 9:
            k = ('d', n['key'])
            if k not in cache: cache[k] = render_drum(n['key'], rng)
            w = cache[k] * att * 1.2
        else:
            w = render_pitched(n['ch'], n['key'], n['dur_ms'] / 1000, att)
        end = min(pos + len(w), len(mix))
        mix[pos:end] += w[:end - pos]
    x = mix[:total]
    from scipy.signal import lfilter
    x = lfilter([1.0, -1.0], [1.0, -0.9975], x)   # DC blocker
    x = master(x, SR)
    nz = np.nonzero(np.abs(x) > 1e-3)[0]
    x = x[:min(len(x), nz[-1] + int(0.1 * SR))]          # trim inaudible tail
    write_flac(x, SR, dst, 16)
    print('wrote', dst, 'samples', len(x))

main()
