"""Report on audio files: python3 -I check_audio.py file1 [file2 ...]  (prints one line each + markdown table row)"""
import sys, os, subprocess, json, numpy as np, soundfile as sf

def info(path):
    pr = json.loads(subprocess.run(['ffprobe', '-v', 'error', '-show_streams', '-show_format', '-of', 'json', path],
                                   capture_output=True, text=True, check=True).stdout)
    s = pr['streams'][0]
    # decode fully with ffmpeg to float32 for peak/silence
    raw = subprocess.run(['ffmpeg', '-v', 'error', '-i', path, '-f', 'f32le', '-acodec', 'pcm_f32le', '-'],
                         capture_output=True, check=True).stdout
    ch = int(s['channels']); sr = int(s['sample_rate'])
    x = np.frombuffer(raw, dtype=np.float32).reshape(-1, ch)
    bits = int(s.get('bits_per_raw_sample') or 0) or int(s.get('bits_per_sample') or 0)
    a = np.max(np.abs(x), axis=1)
    pk = 20 * np.log10(max(float(a.max()), 1e-12))
    nz = np.nonzero(a > 10 ** (-60 / 20))[0]
    lead = nz[0] / sr if len(nz) else float('inf'); tail = (len(a) - 1 - nz[-1]) / sr if len(nz) else float('inf')
    return dict(name=os.path.basename(path), codec=s['codec_name'], dur=len(x) / sr, sr=sr, bits=bits, ch=ch,
                peak=pk, lead=lead, tail=tail, size=os.path.getsize(path), nan=bool(np.isnan(x).any()))

if __name__ == '__main__':
    for p in sys.argv[1:]:
        i = info(p)
        print('| %(name)s | %(dur).3f s | %(sr)d Hz | %(bits)d-bit | %(ch)dch | peak %(peak).2f dBFS | lead %(lead).2fs tail %(tail).2fs (<-60dB) | %(size)d B | nan=%(nan)s |' % i)
