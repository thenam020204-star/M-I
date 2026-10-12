#!/usr/bin/env python3
"""Định dạng file mapN.map (theo k.a(String,int) trong k.class).
Phần đầu (tileset, layer...) giữ nguyên byte. Sau đó: byte nBlock, mỗi block = short len + len byte.
Mỗi block (kịch bản 1 sự kiện): byte nStr, nStr x readUTF (lời thoại), rồi dữ liệu lệnh (giữ nguyên).
Vị trí bắt đầu khu block không ghi trong file (phụ thuộc kích thước ảnh tileset) nên dò bằng
chuỗi block khớp hoàn toàn tới cuối file / dữ liệu sau.
"""
import struct

def _parse_strs(c):
    n = c[0]
    if n > 127: return None
    p = 1; out = []
    for _ in range(n):
        if p + 2 > len(c): return None
        L = struct.unpack('>H', c[p:p+2])[0]
        if p + 2 + L > len(c): return None
        try: s = c[p+2:p+2+L].decode('utf-8')
        except UnicodeDecodeError: return None
        out.append(s); p += 2 + L
    return out, p

def _chain(b, p):
    nb = b[p]
    if nb == 0 or nb > 127: return None
    q = p + 1; blocks = []
    for _ in range(nb):
        if q + 2 > len(b): return None
        L = struct.unpack('>h', b[q:q+2])[0]
        if L <= 0 or q + 2 + L > len(b): return None
        c = b[q+2:q+2+L]
        r = _parse_strs(c)
        if r is None: return None
        blocks.append((r[0], c[r[1]:]))
        q += 2 + L
    return blocks, q

def parse(b):
    """-> (head, blocks, tail) | None. blocks = [(strings, rest_bytes)]"""
    best = None
    for p in range(len(b) - 3, 0, -1):
        r = _chain(b, p)
        if r is None: continue
        blocks, q = r
        # ưu tiên chuỗi kết thúc đúng cuối file
        if q == len(b):
            # chuỗi khớp tới cuối file, chứa nhiều byte lời thoại nhất (hoà thì lấy cái sớm nhất)
            score = sum(len(s.encode('utf-8')) + 2 for st, _ in blocks for s in st)
            if best is None or score >= best[0]:
                best = (score, (b[:p], blocks, b''))
    return best[1] if best else None

def utf(s):
    e = s.encode('utf-8')
    assert len(e) < 65536
    return struct.pack('>H', len(e)) + e

def build(head, blocks, tail):
    out = bytearray(head); out.append(len(blocks))
    for strs, rest in blocks:
        c = bytes([len(strs)]) + b''.join(utf(s) for s in strs) + rest
        assert len(c) < 32768, 'block quá dài'
        out += struct.pack('>h', len(c)) + c
    return bytes(out + tail)
