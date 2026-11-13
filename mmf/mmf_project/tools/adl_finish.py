"""raw s16le stereo (libADLMIDI) -> trimmed/faded/normalised FLAC.  usage: adl_finish.py in.raw out.flac total_seconds"""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np
from finish import master, write_flac
raw, out, tot = sys.argv[1], sys.argv[2], float(sys.argv[3])
x = np.fromfile(raw, dtype='<i2').reshape(-1, 2).astype(np.float64) / 32768.0
x = x[:int(round(tot * 44100))]
write_flac(master(x, 44100), 44100, out, 16)
