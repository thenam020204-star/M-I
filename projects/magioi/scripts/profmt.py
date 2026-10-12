#!/usr/bin/env python3
"""l9.pro (f.a(String)): short n; 2n UTF (az: tên,mô tả vật phẩm); 3n short;
short n; n UTF (aB: trang bị); 9n short; n int; short n; n UTF (ay); 10n short; [đuôi]"""
import struct

def _u(b, p):
    L = struct.unpack('>H', b[p:p+2])[0]; return b[p+2:p+2+L].decode('utf-8'), p + 2 + L

def parse(b):
    p = 0; st = {}; raw = []
    for sec, mul_s, nums in (('az', 2, [(3, 2)]), ('aB', 1, [(9, 2), (1, 4)]), ('ay', 1, [(10, 2)])):
        n = struct.unpack('>h', b[p:p+2])[0]; p += 2
        arr = []
        for _ in range(n * mul_s):
            s, p = _u(b, p); arr.append(s)
        st[sec] = arr
        q = p
        for m, w in nums: p += n * m * w
        raw.append(b[q:p])
    return {'strings': st, 'raw': raw, 'tail': b[p:]}

def utf(s):
    e = s.encode('utf-8'); return struct.pack('>H', len(e)) + e

def build(d):
    out = bytearray()
    for (sec, mul), r in zip((('az', 2), ('aB', 1), ('ay', 1)), d['raw']):
        arr = d['strings'][sec]
        out += struct.pack('>h', len(arr) // mul) + b''.join(utf(s) for s in arr) + r
    return bytes(out + d['tail'])
