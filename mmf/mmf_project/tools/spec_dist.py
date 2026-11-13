"""Mean log-mel spectral distance between audio files (time-aligned, same sample rate, mono, loudness-normalised).
Usage: python3 -I spec_dist.py reference.flac test1.flac [test2.flac ...]
 * both files -> mono (channel mean) -> resampled to the reference rate -> RMS-normalised over the common length
 * time alignment: lag (|lag| <= 1 s) maximising the cross-correlation of the 10 ms log-energy envelopes; common part is kept
 * log-mel: Hann 2048-sample frames, hop 512, 64 mel bands 50 Hz - 16 kHz (HTK mel), 10*log10(power + 1e-10)
 * distance = mean over (frames, bands) of |dB_a - dB_b| ('L1 dB'); frames used: those where either file is within 60 dB of its loudest frame
   (so the digital silence of the fade tail does not dilute / dominate the result). Also prints RMS (L2) in dB and the 4-band split."""
import sys, numpy as np, soundfile as sf
from scipy.signal import resample_poly, fftconvolve
from math import gcd

NFFT, HOP, NMEL, FMIN, FMAX = 2048, 512, 64, 50.0, 16000.0

def load_mono(path, sr_out=None):
    x, sr = sf.read(path, always_2d=True, dtype='float64')
    x = x.mean(axis=1)
    if sr_out and sr != sr_out:
        g = gcd(int(sr), int(sr_out)); x = resample_poly(x, sr_out // g, sr // g); sr = sr_out
    return x, sr

def mel_fb(sr):
    hz2m = lambda f: 2595 * np.log10(1 + f / 700.0); m2hz = lambda m: 700 * (10 ** (m / 2595) - 1)
    pts = m2hz(np.linspace(hz2m(FMIN), hz2m(min(FMAX, sr / 2)), NMEL + 2))
    fr = np.fft.rfftfreq(NFFT, 1 / sr); fb = np.zeros((NMEL, len(fr)))
    for i in range(NMEL):
        l, c, r = pts[i:i + 3]
        fb[i] = np.maximum(0, np.minimum((fr - l) / (c - l), (r - fr) / (r - c)))
    return fb

def logmel(x, sr):
    n = 1 + (len(x) - NFFT) // HOP
    idx = np.arange(NFFT)[None, :] + HOP * np.arange(n)[:, None]
    S = np.abs(np.fft.rfft(x[idx] * np.hanning(NFFT), axis=1)) ** 2 / NFFT
    return 10 * np.log10(S @ mel_fb(sr).T + 1e-10)

def env10ms(x, sr):
    b = int(sr * 0.01); n = len(x) // b
    return 10 * np.log10((x[:n * b].reshape(n, b) ** 2).mean(axis=1) + 1e-12)

def align(a, b, sr, max_lag_s=1.0):
    ea, eb = env10ms(a, sr), env10ms(b, sr)
    n = min(len(ea), len(eb)); ea, eb = ea[:n] - ea[:n].mean(), eb[:n] - eb[:n].mean()
    c = fftconvolve(ea, eb[::-1], mode='full')            # c[k]: lag = k-(n-1) of a relative to b
    lags = np.arange(-(n - 1), n); m = np.abs(lags) <= int(max_lag_s / 0.01)
    lag = int(lags[m][np.argmax(c[m])]) * int(sr * 0.01)   # samples: a[t] ~ b[t - lag]
    if lag > 0: a = a[lag:]
    elif lag < 0: b = b[-lag:]
    n = min(len(a), len(b))
    return a[:n], b[:n], lag / sr

def distance(ref_path, test_path):
    a, sr = load_mono(ref_path); b, _ = load_mono(test_path, sr)
    a, b, lag = align(a, b, sr)
    a = a / (np.sqrt(np.mean(a ** 2)) + 1e-12); b = b / (np.sqrt(np.mean(b ** 2)) + 1e-12)
    A, B = logmel(a, sr), logmel(b, sr)
    ea, eb = A.max(axis=1), B.max(axis=1)
    keep = (ea > ea.max() - 60) | (eb > eb.max() - 60)
    d = (A - B)[keep]
    bands = np.array_split(np.arange(NMEL), 4)
    return dict(l1=float(np.abs(d).mean()), l2=float(np.sqrt((d ** 2).mean())), lag=lag, frames=int(keep.sum()),
                band_l1=[float(np.abs(d[:, bd]).mean()) for bd in bands], bias=float(d.mean()))

if __name__ == '__main__':
    ref = sys.argv[1]
    for t in sys.argv[2:]:
        r = distance(ref, t)
        print('%s vs %s: L1 %.2f dB  L2 %.2f dB  bias(ref-test) %+.2f dB  lag %+.3f s  frames %d  bands(low->high) L1 %s' % (
            ref.split('/')[-1], t.split('/')[-1], r['l1'], r['l2'], r['bias'], r['lag'], r['frames'], ' '.join('%.2f' % v for v in r['band_l1'])))
