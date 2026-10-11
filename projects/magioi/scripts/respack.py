#!/usr/bin/env python3
"""Gói tài nguyên LEVEL-9: index 'level-9' + dữ liệu nối liền chia khúc res0..resN.
  index: int chunk, int n, n x (UTF tên, int size, byte resIdx, int offset)
  respack.py unpack <jar_dir> <out_dir>      -> out_dir/<tên> + out_dir/_order.txt
  respack.py pack   <in_dir> <jar_dir>       -> ghi lại level-9 + res* vào jar_dir (giữ thứ tự)
"""
import os, struct, sys, glob

def rutf(b, p):
    n = struct.unpack('>H', b[p:p+2])[0]; return b[p+2:p+2+n].decode('utf-8'), p+2+n

def read_index(jd):
    b = open(os.path.join(jd, 'level-9'), 'rb').read()
    chunk, n = struct.unpack('>ii', b[:8]); p = 8; ents = []
    for _ in range(n):
        name, p = rutf(b, p)
        size, ri, off = struct.unpack('>iBi', b[p:p+9]); p += 9
        ents.append((name, size, ri, off))
    assert p == len(b), (p, len(b))
    return chunk, ents

def unpack(jd, od):
    chunk, ents = read_index(jd)
    nres = len(glob.glob(os.path.join(jd, 'res*')))
    data = b''.join(open(os.path.join(jd, 'res%d' % i), 'rb').read() for i in range(nres))
    os.makedirs(od, exist_ok=True)
    with open(os.path.join(od, '_order.txt'), 'w') as fo:
        fo.write('%d\n' % chunk)
        for name, size, ri, off in ents:
            st = ri * chunk + off
            open(os.path.join(od, name), 'wb').write(data[st:st+size])
            fo.write(name + '\n')
    print('unpacked', len(ents), 'entries, chunk', chunk, 'res files', nres)

def pack(idir, jd):
    lines = open(os.path.join(idir, '_order.txt')).read().split('\n')
    chunk = int(lines[0]); names = [l for l in lines[1:] if l]
    blob = bytearray(); idx = bytearray(struct.pack('>ii', chunk, len(names)))
    for name in names:
        d = open(os.path.join(idir, name), 'rb').read()
        pos = len(blob); blob += d
        nb = name.encode('utf-8')
        idx += struct.pack('>H', len(nb)) + nb + struct.pack('>iBi', len(d), pos // chunk, pos % chunk)
    for f in glob.glob(os.path.join(jd, 'res*')):
        if os.path.basename(f)[3:].isdigit(): os.remove(f)
    for i in range(0, len(blob), chunk):
        open(os.path.join(jd, 'res%d' % (i // chunk)), 'wb').write(blob[i:i+chunk])
    open(os.path.join(jd, 'level-9'), 'wb').write(idx)
    print('packed', len(names), 'entries ->', (len(blob) + chunk - 1) // chunk, 'res files')

if __name__ == '__main__':
    {'unpack': unpack, 'pack': pack}[sys.argv[1]](sys.argv[2], sys.argv[3])
