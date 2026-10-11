#!/usr/bin/env python3
"""Sinh font 14/13/12/11/10px tu f15.bin, giu nguyen net chu goc.

f15 (goc):  hang 0-4 vung dau (4 hang dau + 1 hang ho) | 5-12 chu hoa (8) | 13-14 duoi (2)
            x-height 5 (hang 8-12), chu rong 6, dau cach 4.
Bien the (chi nen theo chieu doc, KHONG doi do rong, KHONG ve lai net):
  14px: vung dau 4 | hoa 8 | duoi 2   -> chu giu y nguyen, dau chong cua chu hoa sat dinh chu
  13px: vung dau 4 | hoa 7 | duoi 2   -> bo 1 hang lap (net dung) o chu hoa / phan dau chu thuong
  12px: vung dau 4 | hoa 6 | duoi 2   -> bo 2 hang lap
  11px: vung dau 3 | hoa 6 | duoi 2   -> nhu 12px; chu hoa mang 2 dau chong (Ấ Ắ Ế Ố...) thap 1 hang
  10px: vung dau 3 | hoa 6 | duoi 1   -> nhu 11px; phan duoi (g j p q y , ;) bo 1 hang
  Chu thuong giu nguyen x-height 5 o moi co.
Chu co dau duoc ghep lai bang chinh bo dau cua f15 (tach tu f15, khong ve moi).
"""
import os, sys, struct, unicodedata, itertools, zlib

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)

TOP15, CAP15, X15, BASE15 = 5, 8, 8, 12      # f15: hang dau chu hoa=5, dinh x=8, chan=12
VARIANTS = {14: dict(top=4, cap=8, desc=2), 13: dict(top=4, cap=7, desc=2), 12: dict(top=4, cap=6, desc=2),
            11: dict(top=3, cap=6, desc=2), 10: dict(top=3, cap=6, desc=1)}

TONE = {'́': 'acute', '̀': 'grave', '̉': 'hook', '̃': 'tilde'}
HAT = {'̂': 'circ', '̆': 'breve'}
HORN, BELOW = '̛', '̣'
PLAIN = {'ơ': 'o', 'ư': 'u', 'Ơ': 'O', 'Ư': 'U'}

# Hang bi bo (toa do f15) chon tay cho tung co; glyph khong co ten o day dung auto_rows().
OVERRIDE = {
    13: {'E': (10,), 'S': (9,)},
    12: {'2': (7, 10), 'E': (7, 10), 'F': (7, 10), 'G': (8, 10), 'K': (7, 10),
         't': (6, 7), 'đ': (5, 7),
         # i/j: giu 1 hang ho giua cham va than chu -> cham len vung dau (bo hang 0,1 thay vi 0)
         'i': (0, 1, 7), 'j': (0, 1, 7)},
}
OVERRIDE[11] = dict(OVERRIDE[12], i=(0, 1, 2, 7), j=(0, 1, 2, 7))
OVERRIDE[10] = dict(OVERRIDE[11])
# Gop net (OR) hang nguon vao hang dich truoc khi bo hang nguon: {co: {ky_tu: [(nguon, dich)]}}
# t 12px: thanh ngang (hang 7) ha xuong dinh x-height (hang 8) de khong thanh chu T.
MERGE = {12: {'t': [(7, 8)]}}
MERGE[11] = MERGE[10] = MERGE[12]
# 10px: hang bo them o phan duoi (toa do f15, vung 8..14). Khong co ten -> tu chon.
DESC_OVERRIDE = {}
# Chu hoa thap (than 5 hang) dung khi vung dau 3 hang khong du cho 2 dau chong: hang bo (vung 5..12).
LOWER = {'A': (6, 7, 8), 'E': (7, 9, 10), 'O': (7, 8, 9)}


def load(path):
    d = open(path, 'rb').read()
    L = (d[0] << 8) | d[1]
    cs = d[2:2 + L].decode('utf-8'); p = 2 + L
    h = d[p]; p += 1
    w = list(d[p:p + len(cs)]); p += len(cs)
    W = sum(w); bits = d[p:]; g = {}; off = 0
    for i, c in enumerate(cs):
        g[c] = [[(bits[(y * W + off + x) >> 3] >> ((y * W + off + x) & 7)) & 1
                 for x in range(w[i])] for y in range(h)]
        off += w[i]
    return cs, h, w, g


def save(path, cs, h, g):
    widths = [len(g[c][0]) for c in cs]
    W = sum(widths); out = bytearray((W * h + 7) // 8); off = 0
    for i, c in enumerate(cs):
        for y in range(h):
            for x in range(widths[i]):
                if g[c][y][x]:
                    k = y * W + off + x; out[k >> 3] |= 1 << (k & 7)
        off += widths[i]
    u = cs.encode('utf-8')
    with open(path, 'wb') as f:
        f.write(struct.pack('>H', len(u)) + u + bytes([h]) + bytes(widths) + bytes(out))


def decompose(ch):
    d = unicodedata.normalize('NFD', ch)
    if len(d) == 1:
        return None
    base, marks = d[0], d[1:]
    if HORN in marks:
        base = unicodedata.normalize('NFC', base + HORN); marks = marks.replace(HORN, '')
    if not marks:
        return None
    hat = next((HAT[m] for m in marks if m in HAT), None)
    tone = next((TONE[m] for m in marks if m in TONE), None)
    return base, hat, tone, BELOW in marks


def is_lower(c):
    return c.islower() or c in 'ơư'


# ------------------------------------------------------------ chon hang de bo
def auto_rows(bm, k, lower, cand=None):
    """Chon k hang (toa do f15) de bo. Chi phi = so pixel khac voi hang ke ben (hang lap = 0),
    giu nguyen hang dau/cuoi cua net, uu tien hang trong phia tren va hang gan giua.
    Chu thuong chi bo trong vung tren x-height (hang 5..7)."""
    lo, hi = TOP15, (X15 - 1 if lower else BASE15)
    rows = [tuple(r) for r in bm]
    ink = [y for y in range(15) if any(rows[y])]
    first, last = ink[0], ink[-1]
    best = None
    for combo in itertools.combinations(cand or range(lo, hi + 1), k):
        cost = 0.0
        for r in combo:
            if not any(rows[r]):
                above = any(any(rows[y]) for y in range(r))
                below = any(any(rows[y]) for y in range(r + 1, 15))
                if not above or not below:
                    c = 0                        # le trong ngoai net
                elif any(rows[r - 1]) and any(rows[r + 1]):
                    c = 200                      # khoang ho duy nhat (vd ? ! ;) -> giu
                else:
                    c = 1
            elif r in (first, last):
                c = 100
            else:
                c = 10 * min(sum(a != b for a, b in zip(rows[r], rows[n])) for n in (r - 1, r + 1))
            cost += c + abs(r - 8.5) * 0.1
        if k == 2 and combo[1] == combo[0] + 1 and any(rows[combo[0]]) and any(rows[combo[1]]):
            cost += 15                           # khong bo 2 hang net lien nhau
        if best is None or cost < best[0]:
            best = (cost, combo)
    return best


# ------------------------------------------------------------ dau (tach tu f15)
def split_marks(c, cs, g15):
    """Tach glyph co dau cua f15 thanh: chu goc + lop dau tren + lop dau duoi (theo tung pixel)."""
    base = decompose(c)[0]
    bm = g15[c]; b = [list(r) for r in g15[base]]
    upper = not is_lower(base)
    top = TOP15 if upper else X15
    if base == 'i':
        for y in range(top):
            b[y] = [0] * len(b[0])
    above, below = [], []
    for y in range(15):
        for x in range(len(bm[0])):
            if b[y][x] and not bm[y][x]:
                raise SystemExit('chu goc khong nam tron trong %r' % c)
            if bm[y][x] and not b[y][x]:
                (above if y < top else below).append((y, x))
    return base, upper, above, below


def blocks(pix):
    """Chia lop dau tren thanh cac khoi theo hang lien nhau (dau thanh / dau mu ...)."""
    rows = sorted(set(y for y, _ in pix)); out = []
    for y in rows:
        if out and out[-1][-1] == y - 1:
            out[-1].append(y)
        else:
            out.append([y])
    return [[(y, x) for (y, x) in pix if y in rs] for rs in out]


HOOK3 = ((0, 0), (0, 1), (1, 2), (2, 1))


def to_hook2(blk):
    """Dau hoi 3 hang -> dau hoi 2 hang dung cach dat cua f15 (Ẩ/Ẳ): lui sang phai 2 cot."""
    y0 = min(y for y, _ in blk); x0 = min(x for _, x in blk)
    if sorted((y - y0, x - x0) for y, x in blk) != sorted(HOOK3):
        return None
    return [(y0 + 1, x0 + 2), (y0 + 1, x0 + 3), (y0 + 2, x0 + 3)]


def place_above(above, shift, room):
    """Dich lop dau theo dinh chu moi; neu khong vua 'room' hang thi nen dan:
    1) bo khoang giua 2 dau, 2) doi dau hoi 3 hang -> 2 hang, 3) bo khoang giua dau va chu."""
    if not above:
        return []
    bl = blocks(above)
    bottom = max(y for y, _ in above)            # hang cuoi cua dau trong f15
    gap = (room + shift) - 1 - bottom         # so hang ho duoi dau (theo f15)

    def stack(bl, inter, gap):
        out, y = [], room - gap                  # y: hang ngay duoi khoi duoi cung
        for b in reversed(bl):
            ys = sorted(set(r for r, _ in b)); h = ys[-1] - ys[0] + 1
            for (r, x) in b:
                out.append((r - ys[0] + y - h, x))
            y -= h + inter
        return out

    inters = [min(set(r for r, _ in bl[i + 1])) - max(set(r for r, _ in bl[i])) - 1 for i in range(len(bl) - 1)]
    inter = inters[0] if inters else 0
    tries = [(bl, inter, gap), (bl, 0, gap)]
    h2 = [to_hook2(b) or b for b in bl]
    tries += [(h2, 0, gap), (h2, 0, 0)]
    for b, i, gp in tries:
        pix = stack(b, i, gp)
        if min(y for y, _ in pix) >= 0:
            return pix
    return None


def metrics(size):
    v = VARIANTS[size]
    cap = v['top']; base = cap + v['cap'] - 1
    return dict(h=size, cap=cap, x=base - 4, base=base)


def desc_drop(bm, size, taken=()):
    """Phan duoi 2 -> 1 hang: bo hang trong duoi chan chu, neu chu co duoi thi chon hang lap."""
    if VARIANTS[size]['desc'] == 2:
        return ()
    if not any(any(bm[y]) for y in (13, 14)):
        return (13,)
    return auto_rows(bm, 1, False, cand=[y for y in range(X15, 15) if y not in taken])[1]


def shrink(g15, c, drop, size):
    for r in drop:                               # ngoai vung chu chi duoc bo hang trong
        if not (TOP15 <= r <= 14) and any(g15[c][r]):
            raise SystemExit('bo mat net %r hang %d' % (c, r))
    src = [list(r) for r in g15[c]]
    for a, b in MERGE.get(size, {}).get(c, ()):
        assert a in drop
        src[b] = [p | q for p, q in zip(src[a], src[b])]
    out = [r for i, r in enumerate(src) if i not in drop]
    assert len(out) == size, (c, drop)
    return out


def build(cs, g15, size):
    m = metrics(size)
    k = CAP15 - VARIANTS[size]['cap']
    cut = tuple(range(TOP15 - VARIANTS[size]['top']))
    bases, chosen = {}, {}
    for c in cs:
        if decompose(c) is not None:
            continue
        ndesc = 2 - VARIANTS[size]['desc']
        if c in OVERRIDE.get(size, {}):
            drop = OVERRIDE[size][c]
            if len(drop) + ndesc != 15 - size:
                drop = cut + drop
        elif k == 0 or c == ' ':
            drop = cut + tuple(range(TOP15, TOP15 + k))
        else:
            drop = cut + auto_rows(g15[c], k, is_lower(c))[1]
        dd = DESC_OVERRIDE.get(c) or desc_drop(g15[c], size, drop)
        drop = tuple(sorted(drop + dd))
        chosen[c] = drop
        bases[c] = shrink(g15, c, drop, size)
    g = dict(bases)
    lowered = []
    for c in cs:
        if c in g:
            continue
        base, upper, above, below = split_marks(c, cs, g15)
        bm = [list(r) for r in bases[base]]
        top = m['cap'] if upper else m['x']
        if base == 'i':
            for y in range(top):
                bm[y] = [0] * len(bm[0])
        pix = place_above(above, (TOP15 if upper else X15) - top, top)
        if pix is None and upper and base in LOWER:
            # chu hoa thap 1 hang: bo it 1 hang o vung tren, bo them 1 hang trong than chu
            drop = cut[:-1] + LOWER[base]
            drop += desc_drop(g15[base], size, drop)
            bm = shrink(g15, base, drop, size)
            top += 1
            pix = place_above(above, TOP15 - top, top)
            lowered.append(c)
        if pix is None:
            raise SystemExit('khong du cho dat dau %r (%dpx)' % (c, size))
        for y, x in pix:
            bm[y][x] = 1
        for y, x in below:                       # cham duoi: giu khoang cach toi day
            bm[y - (15 - size)][x] = 1
        g[c] = bm
    chosen['lowered'] = ''.join(lowered)
    return g, chosen


def verify_identity(cs, g15):
    """Dung lai f15 bang chinh thuat toan -> phai trung 100% voi file goc."""
    VARIANTS[15] = dict(top=5, cap=8, desc=2)
    g, _ = build(cs, g15, 15)
    del VARIANTS[15]
    return [c for c in cs if g[c] != g15[c]]


def dump(g, cs, h, out=sys.stdout):
    for k in range(0, len(cs), 16):
        part = cs[k:k + 16]
        print('  '.join(c.ljust(len(g[c][0])) for c in part), file=out)
        for y in range(h):
            print('  '.join(''.join('#' if v else '.' for v in g[c][y]) for c in part) + '  %2d' % y, file=out)
        print(file=out)


def write_png(path, g, cs, sizes, scale=3):
    per = 38
    lines = [cs[i:i + per] for i in range(0, len(cs), per)]
    W = 8 + per * 6 + 8
    H = 6 + sum((s + 2) * len(lines) + 8 for s in sizes)
    pix = [[(255, 255, 255)] * W for _ in range(H)]
    y = 6
    for s in sizes:
        for ln in lines:
            x = 8
            for c in ln:
                bm = g[s][c]
                for yy in range(s):
                    for xx in range(len(bm[0])):
                        if bm[yy][xx]:
                            pix[y + yy][x + xx] = (0, 0, 0)
                x += len(g[s][c][0])
            y += s + 2
        for xx in range(W):
            pix[y + 3][xx] = (200, 200, 200)
        y += 8
    raw = bytearray()
    for yy in range(H * scale):
        raw.append(0)
        for xx in range(W * scale):
            raw += bytes(pix[yy // scale][xx // scale])

    def chunk(t, d):
        return struct.pack('>I', len(d)) + t + d + struct.pack('>I', zlib.crc32(t + d) & 0xffffffff)
    png = b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', W * scale, H * scale, 8, 2, 0, 0, 0))
    png += chunk(b'IDAT', zlib.compress(bytes(raw), 9)) + chunk(b'IEND', b'')
    open(path, 'wb').write(png)


def main():
    src = os.path.join(ROOT, 'f15.bin')
    outdir = os.path.join(ROOT, 'dist', 'font')
    cs, h, w, g15 = load(src)
    bad = verify_identity(cs, g15)
    if bad:
        raise SystemExit('thuat toan khong dung lai duoc f15: %s' % ''.join(bad))
    print('kiem tra: dung lai f15 tu chu goc + dau = trung 100%')
    allg = {15: g15}
    for size in (14, 13, 12, 11, 10):
        g, chosen = build(cs, g15, size)
        for c in cs:                             # do rong giu nguyen nhu f15
            assert len(g[c]) == size and all(len(r) == len(g15[c][0]) for r in g[c]), c
        save(os.path.join(outdir, 'f%d.bin' % size), cs, size, g)
        allg[size] = g
        print('f%d.bin  %d ky tu' % (size, len(cs)) + ('  chu hoa thap: ' + chosen['lowered'] if chosen['lowered'] else ''))
        if 'dump' in sys.argv[1:]:
            print('=== %dpx' % size)
            dump(g, cs, size)
    write_png(os.path.join(ROOT, 'preview_14_13_12.png'), allg, cs, (15, 14, 13, 12))
    write_png(os.path.join(ROOT, 'preview_12_11_10.png'), allg, cs, (12, 11, 10))


if __name__ == '__main__':
    main()
