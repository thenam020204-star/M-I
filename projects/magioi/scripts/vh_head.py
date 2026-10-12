#!/usr/bin/env python3
"""Chân dung mới (vẽ chìm sau khung thoại, đứng trên cạnh đáy khung):
assets/src/head_<id>_src.png (PNG nền trong suốt) -> assets/jar/head_<id>.png, cao H px, bảng màu 256 có alpha.
  vh_head.py [H=150]"""
import os, sys, glob
from PIL import Image
P = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..')
H = int(sys.argv[1]) if len(sys.argv) > 1 else 150
for src in glob.glob(os.path.join(P, 'assets', 'src', 'head_*_src.png')):
    im = Image.open(src).convert('RGBA')
    im = im.crop(im.getbbox())
    w = round(im.width * H / im.height)
    im = im.resize((w, H), Image.LANCZOS)
    # alpha gần 0 -> 0 (bỏ viền mờ li ti), giữ alpha mềm ở mép
    a = im.split()[3].point(lambda v: 0 if v < 8 else v)
    im.putalpha(a)
    q = im.quantize(256, method=Image.FASTOCTREE, dither=Image.NONE)
    out = os.path.join(P, 'assets', 'jar', os.path.basename(src).replace('_src', ''))
    q.save(out, optimize=True)
    print(out, q.size, os.path.getsize(out), 'byte')
