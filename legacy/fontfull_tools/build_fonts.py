#!/usr/bin/env python3
"""Sinh font bitmap 18px, 11px tu font goc 15px (f15.bin).
(14/13/12px: xem tools/derive_from15.py - nen tu f15, giu nguyen net.)

Dinh dang .bin (doc bang DataInputStream):
  writeUTF(charset) | byte height | byte width[n] | bit atlas (hang-truoc, LSB-first)

- f15.bin giu nguyen (goc). Cac co khac dung CUNG charset (227 ky tu).
- Chu goc (so, chu cai, dau cau, đ ơ ư ...) ve tay cho tung co (glyphs_*.py).
- Chu co dau tieng Viet duoc ghep tu: chu goc + dau mu/trang + dau thanh + cham duoi,
  voi bo dau ve tay rieng cho tung co (MARKS).

Chay: python3 tools/build_fonts.py   (tao f18.bin f11.bin + anh xem thu)
"""
import os, struct, sys, unicodedata, zlib

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, HERE)

from glyphs_18 import GLYPHS as G18
from glyphs_11 import GLYPHS as G11

# ----------------------------------------------------------------- thong so co chu
# cap: hang dinh chu hoa, x: hang dinh chu thuong, base: hang chan chu, dot: hang cham duoi
# gap_caps / gap_lower: co chen khoang trong giua dau mu va dau thanh (neu du cho)
METRICS = {
    18: dict(h=18, w=7, space=5, cap=5, x=9, base=14, dot=16, glyphs=G18),
    11: dict(h=11, w=6, space=4, cap=4, x=6, base=9, dot=10, glyphs=G11),
}

# ----------------------------------------------------------------- bo dau
# Moi dau: (bitmap, tam_x). Tam_x la cot (co the le .5) trung voi tam chu.
MARKS_ODD = {       # co 11: chu rong 5, tam cot nguyen (giong ban 15px)
    'acute':  (['.#', '#.'], 0),
    'grave':  (['#.', '.#'], 1),
    'hook':   (['##.', '..#', '.#.'], 1),
    'hook2':  (['##', '.#'], 1),
    'tilde':  (['.#.#', '#.#.'], 2),
    'circ':   (['.#.', '#.#'], 1),
    'breve':  (['#..#', '.##.'], 1),
}
MARKS_EVEN = {      # co 18: chu rong 6, tam o giua 2 cot
    'acute':  (['.#', '#.'], 0.5),
    'grave':  (['#.', '.#'], 0.5),
    'hook':   (['##.', '..#', '.#.'], 1.5),
    'hook2':  (['##', '.#'], 1.0),
    'tilde':  (['.#.#', '#.#.'], 1.5),
    'circ':   (['.##.', '#..#'], 1.5),
    'breve':  (['#..#', '.##.'], 1.5),
}
MARKS = {18: MARKS_EVEN, 11: MARKS_ODD}

TONE = {'́': 'acute', '̀': 'grave', '̉': 'hook', '̃': 'tilde'}
HAT = {'̂': 'circ', '̆': 'breve'}
HORN = '̛'
BELOW = '̣'
PLAIN = {'ơ': 'o', 'ư': 'u', 'Ơ': 'O', 'Ư': 'U'}

# Cham duoi cua y dat lech sang phai cho khoi dinh vao duoi chu (cot, hang).
DOT_OVERRIDE = {18: {'y': (4, 16)}, 11: {'y': (5, 10)}}


def load15(path):
    d = open(path, 'rb').read()
    L = (d[0] << 8) | d[1]
    cs = d[2:2 + L].decode('utf-8')
    p = 2 + L
    h = d[p]; p += 1
    w = list(d[p:p + len(cs)]); p += len(cs)
    W = sum(w)
    bits = d[p:]
    g, off = {}, 0
    for i, c in enumerate(cs):
        g[c] = [[(bits[(y * W + off + x) >> 3] >> ((y * W + off + x) & 7)) & 1
                 for x in range(w[i])] for y in range(h)]
        off += w[i]
    return cs, h, w, g


def save(path, cs, h, widths, g):
    W = sum(widths)
    out = bytearray((W * h + 7) // 8)
    off = 0
    for i, c in enumerate(cs):
        for y in range(h):
            for x in range(widths[i]):
                if g[c][y][x]:
                    k = y * W + off + x
                    out[k >> 3] |= 1 << (k & 7)
        off += widths[i]
    u = cs.encode('utf-8')
    with open(path, 'wb') as f:
        f.write(struct.pack('>H', len(u)) + u + bytes([h]) + bytes(widths) + bytes(out))


def to_bitmap(rows, h, w, name):
    """rows: dict {hang: chuoi} hoac (hang_bat_dau, [chuoi...])"""
    top, lines = rows
    bm = [[0] * w for _ in range(h)]
    for i, line in enumerate(lines):
        y = top + i
        if y >= h:
            raise SystemExit('glyph %r vuot chieu cao' % name)
        for x, ch in enumerate(line):
            if ch == '#':
                if x >= w:
                    raise SystemExit('glyph %r vuot chieu rong' % name)
                bm[y][x] = 1
    return bm


def decompose(ch):
    d = unicodedata.normalize('NFD', ch)
    if len(d) == 1:
        return None
    base, marks = d[0], d[1:]
    if HORN in marks:
        base = unicodedata.normalize('NFC', base + HORN)
        marks = marks.replace(HORN, '')
    if not marks:
        return None
    hat = next((HAT[m] for m in marks if m in HAT), None)
    tone = next((TONE[m] for m in marks if m in TONE), None)
    return base, hat, tone, BELOW in marks


def anchor(bm, top, base):
    xs = [x for y in range(top, base + 1) for x in range(len(bm[0])) if bm[y][x]]
    return (min(xs) + max(xs)) / 2.0


def stamp(bm, mark, cx, y0):
    shape, mc = mark
    x0 = int(cx - mc + 0.5 + 1e-9)
    for dy, line in enumerate(shape):
        for dx, ch in enumerate(line):
            if ch == '#':
                x = x0 + dx
                if not (0 <= x < len(bm[0])):
                    raise SystemExit('dau tran ngang')
                bm[y0 + dy][x] = 1


def compose(ch, bases, size):
    m = METRICS[size]
    base, hat, tone, below = decompose(ch)
    upper = base.isupper()
    bm = [list(r) for r in bases[base]]
    top = m['cap'] if upper else m['x']
    if base == 'i':          # i mat cham khi co dau
        for y in range(top):
            bm[y] = [0] * len(bm[0])
    plain = PLAIN.get(base, base)
    cx = anchor(bases[plain], top, m['base'])
    marks = MARKS[size]
    comps = [c for c in (tone, hat) if c]
    if comps:
        # thu cac cach xep tu thoang den chat
        tries = []
        for hk in ('hook', 'hook2'):
            parts = [marks[hk if c == 'hook' else c] for c in comps]
            hs = [len(p[0]) for p in parts]
            inter_opts = (0,) if upper else (1, 0)
            for bgap in (1, 0):
                for inter in inter_opts:
                    tries.append((parts, hs, bgap, inter))
        for parts, hs, bgap, inter in tries:
            need = sum(hs) + bgap + inter * (len(parts) - 1)
            if need <= top:
                y = top - bgap - sum(hs) - inter * (len(parts) - 1)
                for p, hh in zip(parts, hs):
                    stamp(bm, p, cx, y)
                    y += hh + inter
                break
        else:
            raise SystemExit('khong du cho dat dau cho %r (%dpx)' % (ch, size))
    if below:
        if base in DOT_OVERRIDE[size]:
            x, y = DOT_OVERRIDE[size][base]
        else:
            x, y = int(cx), m['dot']
        bm[y][x] = 1
    return bm


def build(size, cs):
    m = METRICS[size]
    h, w = m['h'], m['w']
    bases = {}
    for c in cs:
        if c == ' ':
            bases[c] = [[0] * m['space'] for _ in range(h)]
        elif decompose(c) is None:
            if c not in m['glyphs']:
                raise SystemExit('thieu glyph %r cho co %d' % (c, size))
            bases[c] = to_bitmap(m['glyphs'][c], h, w, c)
    g = dict(bases)
    for c in cs:
        if c not in g:
            g[c] = compose(c, bases, size)
    widths = [len(g[c][0]) for c in cs]
    return g, widths


# ----------------------------------------------------------------- xem thu
def write_png(path, pix, scale=2):
    H, W = len(pix), len(pix[0])
    raw = bytearray()
    for y in range(H * scale):
        raw.append(0)
        for x in range(W * scale):
            raw += bytes(pix[y // scale][x // scale])

    def chunk(t, d):
        return struct.pack('>I', len(d)) + t + d + struct.pack('>I', zlib.crc32(t + d) & 0xffffffff)
    png = b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', W * scale, H * scale, 8, 2, 0, 0, 0))
    png += chunk(b'IDAT', zlib.compress(bytes(raw), 9)) + chunk(b'IEND', b'')
    open(path, 'wb').write(png)


def preview(fonts, cs, path):
    per = 38
    lines = [cs[i:i + per] for i in range(0, len(cs), per)]
    W = 8 + per * 7 + 8
    H = 6 + sum((h + 3) * len(lines) + 8 for h, _, _ in fonts)
    pix = [[(255, 255, 255)] * W for _ in range(H)]
    y = 6
    for h, g, widths in fonts:
        wi = dict(zip(cs, widths))
        for ln in lines:
            x = 8
            for c in ln:
                for yy in range(h):
                    for xx in range(wi[c]):
                        if g[c][yy][xx]:
                            pix[y + yy][x + xx] = (0, 0, 0)
                x += wi[c]
            y += h + 3
        for xx in range(W):
            pix[y + 3][xx] = (200, 200, 200)
        y += 8
    write_png(path, pix)


def main():
    cs, h15, w15, g15 = load15(os.path.join(ROOT, 'f15.bin'))
    fonts = [(15, g15, w15)]
    for size in (18, 11):
        g, widths = build(size, cs)
        save(os.path.join(ROOT, 'f%d.bin' % size), cs, METRICS[size]['h'], widths, g)
        fonts.append((METRICS[size]['h'], g, widths))
    fonts.sort(key=lambda f: -f[0])
    preview(fonts, cs, os.path.join(ROOT, 'preview_charset.png'))
    if len(sys.argv) > 1 and sys.argv[1] == 'dump':
        for h, g, widths in fonts:
            if h == 15:
                continue
            print('=== %dpx' % h)
            for k in range(0, len(cs), 14):
                part = cs[k:k + 14]
                print('  '.join(c.ljust(len(g[c][0])) for c in part))
                for y in range(h):
                    print('  '.join(''.join('#' if v else '.' for v in g[c][y]) for c in part))
                print()


if __name__ == '__main__':
    main()
