#!/usr/bin/env python3
"""Sinh 27 file WAV im lặng giữ chỗ cho SFX: res/sfx/<TÊN>_<v>.wav
TÊN ∈ HIT MISS CRIT SKILL FIRE WATER EARTH WIND HEAL (SFXType.name), v ∈ 0 quái/sủng vật, 1 Quinn (thương), 2 Julie (súng).
WAV PCM 8-bit mono 8 kHz, 50 ms (400 mẫu 0x80). Ghi đè file cùng tên bằng tiếng thật rồi chạy build_jar.sh.
Dùng: python3 -I tools/gen_silent_wavs.py [thư mục ra, mặc định res/sfx] [--force: ghi đè cả file đã có]"""
import os, struct, sys

NAMES = ["HIT", "MISS", "CRIT", "SKILL", "FIRE", "WATER", "EARTH", "WIND", "HEAL"]
RATE, MS = 8000, 50

def silent_wav():
    n = RATE * MS // 1000
    return (b"RIFF" + struct.pack("<I", 36 + n) + b"WAVEfmt " + struct.pack("<IHHIIHH", 16, 1, 1, RATE, RATE, 1, 8)
            + b"data" + struct.pack("<I", n) + b"\x80" * n)

def main():
    args = [a for a in sys.argv[1:] if not a.startswith("--")]
    out = args[0] if args else os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "res", "sfx")
    force = "--force" in sys.argv
    os.makedirs(out, exist_ok=True)
    w = silent_wav(); made = kept = 0
    for nm in NAMES:
        for v in range(3):
            p = os.path.join(out, "%s_%d.wav" % (nm, v))
            if os.path.exists(p) and not force:
                kept += 1; continue
            open(p, "wb").write(w); made += 1
    print("wav im lang: tao %d, giu nguyen %d (%s)" % (made, kept, os.path.normpath(out)))

main()
