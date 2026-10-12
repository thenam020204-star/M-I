#!/usr/bin/env python3
"""Parser for the game's .av animation container (class co.a(byte[]) in the jar).
Layout (all big-endian, DataInputStream):
  2 bytes magic, 1 byte version(==1)
  short nImg, nImg x UTF  (image names)
  short nTile, 4 flag bytes (x,y,w,h wide?), per tile: img idx (byte or short if nImg>255),x,y,w,h, byte flip
  short nFrame, 3 flag bytes, per frame: short n, n x (x,y,tile)
  short nBox,  4 flag bytes, per box-set: short n, n x (x,y,w,h, byte type)
  short nAnim, byte wide, per anim: short n, n x frameIdx(box-set/frame index)
"""
import struct, sys, os

class R:
    def __init__(s, d): s.d = d; s.p = 0
    def b(s): v = s.d[s.p]; s.p += 1; return v
    def sb(s): v = s.b(); return v - 256 if v > 127 else v
    def h(s): v = struct.unpack('>h', s.d[s.p:s.p+2])[0]; s.p += 2; return v
    def utf(s): n = struct.unpack('>H', s.d[s.p:s.p+2])[0]; s.p += 2; v = s.d[s.p:s.p+n].decode('utf8', 'replace'); s.p += n; return v
    def raw(s, n): v = s.d[s.p:s.p+n]; s.p += n; return v

def parse(path):
    r = R(open(path, 'rb').read())
    r.raw(2)
    ver = r.b()
    out = {'ver': ver}
    nimg = r.h(); out['imgs'] = [r.utf() for _ in range(nimg)] if nimg > 0 else []
    nt = r.h(); tiles = []
    if nt > 0:
        f = r.raw(4)
        for _ in range(nt):
            img = r.h() if nimg > 255 else r.b()
            vals = [r.h() if f[i] == 1 else r.b() & 255 for i in range(4)]
            tiles.append((img, *vals, r.sb()))
    out['tiles'] = tiles
    nf = r.h(); frames = []
    if nf > 0:
        f = r.raw(3)
        for _ in range(nf):
            n = r.h(); fr = []
            for _ in range(max(n, 0)):
                x = r.h() if f[0] == 1 else r.sb()
                y = r.h() if f[1] == 1 else r.sb()
                t = r.h() if f[2] == 1 else r.b() & 255
                fr.append((x, y, t))
            frames.append(fr)
    out['frames'] = frames
    nb = r.h(); boxes = []
    if nb > 0:
        f = r.raw(4)
        for _ in range(nb):
            n = r.h(); bs = []
            for _ in range(max(n, 0)):
                x = r.h() if f[0] == 1 else r.sb()
                y = r.h() if f[1] == 1 else r.sb()
                w = r.h() if f[2] == 1 else r.b() & 255
                hh = r.h() if f[3] == 1 else r.b() & 255
                bs.append((x, y, w, hh, r.sb()))
            boxes.append(bs)
    out['boxes'] = boxes
    na = r.h(); anims = []
    if na > 0:
        wide = r.b()
        for _ in range(na):
            n = r.h(); anims.append([(r.h() if wide == 1 else r.b() & 255) for _ in range(max(n, 0))])
    out['anims'] = anims
    out['tail'] = len(r.d) - r.p
    return out

if __name__ == '__main__':
    for p in sys.argv[1:]:
        o = parse(p)
        print(os.path.basename(p), 'ver', o['ver'], 'imgs', o['imgs'], 'tiles', len(o['tiles']), 'frames', len(o['frames']),
              'boxsets', len(o['boxes']), 'anims', len(o['anims']), 'lens', [len(a) for a in o['anims']], 'tail', o['tail'])
