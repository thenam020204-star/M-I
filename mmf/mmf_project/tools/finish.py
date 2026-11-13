"""Common mastering: fade-out, peak normalise, FLAC (ffmpeg level 8). Library only."""
import os, subprocess, numpy as np, soundfile as sf

def master(x, sr, fade_s=8.0, peak_db=-1.0):
    """x: float array (n,) or (n,ch). Returns processed float array."""
    x = np.asarray(x, dtype=np.float64)
    n = x.shape[0]
    f = int(round(fade_s * sr))
    ramp = np.ones(n)
    t = np.linspace(0, 1, f, endpoint=True)
    ramp[n - f:] = (1.0 - t)             # linear fade to exact zero at last sample
    x = x * (ramp[:, None] if x.ndim == 2 else ramp)
    pk = np.max(np.abs(x))
    if not np.isfinite(pk) or pk <= 0:
        raise ValueError('empty/NaN audio')
    return x * (10 ** (peak_db / 20) / pk)

def write_flac(x, sr, path, bits=16, seed=1):
    """TPDF-dithered quantise (16-bit) then ffmpeg flac -compression_level 8."""
    rng = np.random.default_rng(seed)
    tmp = path + '.tmp.wav'
    if bits == 16:
        q = 32767.0
        d = (rng.random(x.shape) - rng.random(x.shape))
        y = np.clip(np.round(x * q + d), -32768, 32767).astype(np.int16)
        sf.write(tmp, y, sr, subtype='PCM_16')
        samp = ['-sample_fmt', 's16']
    else:
        q = 8388607.0
        d = (rng.random(x.shape) - rng.random(x.shape))
        y = np.clip(np.round(x * q + d), -8388608, 8388607).astype(np.int32) << 8
        sf.write(tmp, y, sr, subtype='PCM_24')
        samp = ['-sample_fmt', 's32']
    subprocess.run(['ffmpeg', '-y', '-loglevel', 'error', '-i', tmp, '-c:a', 'flac', '-compression_level', '8'] + samp + [path], check=True)
    os.remove(tmp)
