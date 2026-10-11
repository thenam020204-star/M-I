#!/usr/bin/env python3
"""Chép class game sang thư mục khác, bỏ attribute InnerClasses (để ecj chịu tham chiếu class t/p bị
obfuscate thành "inner" của o). Chỉ dùng làm classpath khi biên dịch hook, KHÔNG đóng vào jar.
  strip_inner.py <jar_dir> <out_dir>"""
import os, sys, glob, struct
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from bcpatch import Klass
from cpool import jutf_dec
src, dst = sys.argv[1:3]; os.makedirs(dst, exist_ok=True)
for f in glob.glob(os.path.join(src, '*.class')):
    k = Klass(open(f, 'rb').read()); r = k.cattrs
    na = struct.unpack('>H', r[:2])[0]; p = 2; keep = []
    for _ in range(na):
        an, al = struct.unpack('>HI', r[p:p+6])
        if jutf_dec(k.cp[an][1]) != 'InnerClasses': keep.append(r[p:p+6+al])
        p += 6 + al
    k.cattrs = struct.pack('>H', len(keep)) + b''.join(keep)
    open(os.path.join(dst, os.path.basename(f)), 'wb').write(k.build())
