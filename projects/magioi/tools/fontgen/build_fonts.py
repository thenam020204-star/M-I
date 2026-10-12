# -*- coding: utf-8 -*-
"""Sinh bo font 15px / 13px tu fonts/f18.bin (ban chuan).

    python3 fontgen/build_fonts.py            # ghi fonts/f15 f13 (co chan), f15s f13s, f15c f13c
    python3 fontgen/build_fonts.py --check18  # tu kiem: ghep lai f18 tu chu goc + dau, so voi f18.bin
    python3 fontgen/build_fonts.py --sheet    # in bang ASCII cua tung font vua sinh

Ba bien the:
  f<co>.bin   CO CHAN  - net cua f18 (rong co dinh 7), chi nen chieu cao.
  f<co>s.bin  KHONG CHAN - glyph sans (fontgen/sans18.py) cung luoi 18px, rong theo net -> gon hon.
  f<co>c.bin  CHAN CHON LOC - nhu khong chan, rieng I i j l J 1 4 giu chan (sans18.SELECTIVE).

Cach nen: chia 18 hang thanh vung  dau(0-4) | tren x(5-8) | x-height(9-14) | duoi(15-17)
va bo bot hang trong tung vung (15px: 5|3|5|2, 13px: 4|2|5|2). Moi glyph chon to hop hang bo
it mat thong tin nhat (hang trung lap truoc), khong lam dut/dinh net. Chu hoa, so, ky hieu duoc
bo tu do trong ca than chu (10 hang -> 8 / 7). Chu co dau duoc ghep lai: chu goc da nen + dau
(sac huyen hoi nga nang, mu, trang, moc) dat theo bo cuc cua tung co.
"""
import itertools, os, sys, unicodedata

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, os.path.join(ROOT, 'pixeltext_kit'))
sys.path.insert(0, HERE)
from pixeltext import container            # noqa: E402
import sans18                              # noqa: E402

SRC = os.path.join(ROOT, 'fonts', 'f18.bin')

# ------------------------------------------------------------------ bo cuc doc
ZONES = {'A': range(0, 5), 'T': range(5, 9), 'X': range(9, 15), 'D': range(15, 18)}
KEEP = {18: dict(A=5, T=4, X=6, D=3), 15: dict(A=5, T=3, X=5, D=2), 13: dict(A=4, T=2, X=5, D=2)}
LAYOUT = {  # hang dinh chu hoa, dinh x-height, chan chu
    18: dict(cap=5, x=9, base=14),
    15: dict(cap=5, x=8, base=12),
    13: dict(cap=4, x=6, base=10),
}
SERIF_W = 7          # do rong co dinh cua ban co chan (nhu f18)
SERIF_SPACE = 5

# ------------------------------------------------------------------ dau (tren -> duoi)
#  anchor = cot cua dau dat thang cot giua chu goc
MARKS = {
    '\u0301': ([".#", "#."], 0),                  # sac
    '\u0300': (["#.", ".#"], 0),                  # huyen
    '\u0309': (["##.", "..#", ".#."], 1),         # hoi
    '\u0303': ([".#.#", "#.#."], 1),              # nga
    '\u0302': ([".##.", "#..#"], 1),              # mu
    '\u0306': (["#..#", ".##."], 1),              # trang
    '\u0323': (["#"], 0),                         # nang (duoi)
}
ODD = {  # bien the cho chu goc rong le (ban khong chan)
    '\u0300': (["#.", ".#"], 1),
    '\u0302': ([".#.", "#.#"], 1),
    '\u0306': (["#...#", ".###."], 2),
}
HOOK2 = (["##.", ".#."], 1)                       # hoi 2 hang khi thieu cho
HORN, DOT_BELOW = '\u031b', '\u0323'

# ------------------------------------------------------------------ sua tay
#  ep bo cac hang nay (0..17 cua luoi 18px) khi nen chu goc: (bien_the, co, ky_tu) -> [hang]
BASE_OVERRIDES = {
    ('serif', 15, 'E'): [7, 11],      # giu ca hai gai tren/duoi
    ('serif', 15, '2'): [8, 13],      # giu moc dau so 2
    ('serif', 13, '2'): [7, 8, 13],
    ('sans', 13, '2'): [7, 8, 13],
    ('serif', 13, 'X'): [7, 10, 13],  # X can doi
    ('sans', 13, 'X'): [8, 10, 12],
    ('sans', 13, 'R'): [8, 12, 14],   # bung R giu 2 hang
    ('sans', 13, 'N'): [6, 10, 14],
    ('sans', 15, 'x'): [12],
    ('sans', 13, 'x'): [12],
    ('serif', 13, 'đ'): [5, 8],       # giu gach ngang cua đ
    ('serif', 15, '%'): [8, 11],      # duong cheo lien net
    ('serif', 13, '%'): [5, 8, 11],
    ('sans', 15, '%'): [8, 11],
    ('sans', 13, '%'): [5, 8, 11],
}
#  thay ca glyph sau cung: (bien_the, co, ky_tu) -> danh sach hang ('#'/'.')
OVERRIDES = {}


# ================================================================== tien ich bitmap
def blank(h, w):
    return [[0] * w for _ in range(h)]


def parse(rows):
    return [[1 if c == '#' else 0 for c in r] for r in rows]


def ink_cols(bm):
    cols = [x for x in range(len(bm[0])) if any(r[x] for r in bm)] if bm and bm[0] else []
    return (cols[0], cols[-1]) if cols else (0, -1)


def components(bm):
    h, w = len(bm), len(bm[0]) if bm else 0
    seen, n = set(), 0
    for y in range(h):
        for x in range(w):
            if bm[y][x] and (y, x) not in seen:
                n += 1
                st = [(y, x)]
                seen.add((y, x))
                while st:
                    cy, cx = st.pop()
                    for dy in (-1, 0, 1):
                        for dx in (-1, 0, 1):
                            ny, nx = cy + dy, cx + dx
                            if 0 <= ny < h and 0 <= nx < w and bm[ny][nx] and (ny, nx) not in seen:
                                seen.add((ny, nx))
                                st.append((ny, nx))
    return n


def ham(a, b):
    return sum(1 for p, q in zip(a, b) if p != q)


# ================================================================== nen chieu cao
def compress(bm, size, kind, force=()):
    """bm: 18 hang. kind='lower': chu thuong, giu dung tung vung (T,X rieng);
    'upper' (chu hoa, so): than chu (T+X) bo tu do, uu tien chia deu cac doan lap;
    'symbol': nhu upper nhung uu tien phan bo hang trong nhu chu thuong."""
    lower = kind == 'lower'
    keep = KEEP[size]
    if size == 18:
        return [r[:] for r in bm]
    ncomp = components(bm)
    drop = {z: len(ZONES[z]) - keep[z] for z in ZONES}

    def choices(zone_rows, k):
        return list(itertools.combinations(zone_rows, k))

    if lower:
        parts = [choices(ZONES[z], drop[z]) for z in 'ATXD']
    else:
        body = list(ZONES['T']) + list(ZONES['X'])
        parts = [choices(ZONES['A'], drop['A']), choices(body, drop['T'] + drop['X']),
                 choices(ZONES['D'], drop['D'])]
    best = None
    for combo in itertools.product(*parts):
        dele = set(itertools.chain(*combo))
        if not dele.issuperset(force):
            continue
        kept = [y for y in range(18) if y not in dele]
        out = [bm[y] for y in kept]
        cost = 1000 * abs(components(out) - ncomp)
        loss = 0
        for y in dele:
            above = max((k for k in kept if k < y), default=None)
            below = min((k for k in kept if k > y), default=None)
            c = [ham(bm[y], bm[k]) for k in (above, below) if k is not None]
            loss += min(c) if c else 0
        cost += 10 * loss
        if kind == 'symbol':
            cost += 3 * abs(sum(1 for y in dele if y in ZONES['T']) - drop['T'])
        if kind == 'upper':   # chia deu: B, H, 8 giu hai nua can nhau
            run, prev = 0, None
            for r in out + [None]:
                if r is not None and r == prev and any(r):
                    run += 1
                else:
                    cost += 0.05 * run * run
                    run = 1
                prev = r
        for y in dele:   # hoa: vung x bo hang thap truoc (giu net ngang cua e, a), vung khac bo giua
            z = next(z for z in ZONES if y in ZONES[z])
            zr = ZONES[z]
            cost += 0.01 * ((zr[-1] - y) if z == 'X' else abs(y - (zr[0] + zr[-1]) / 2.0))
        if best is None or cost < best[0]:
            best = (cost, out)
    return [r[:] for r in best[1]]


# ================================================================== ghep dau
def place(canvas, rows, top, left):
    for dy, r in enumerate(rows):
        for dx, c in enumerate(r):
            if c == '#':
                y, x = top + dy, left + dx
                if not (0 <= y < len(canvas)):
                    raise ValueError('dau tran ra ngoai chieu cao')
                canvas[y][x] = 1


def compose(base, marks, size, upper, odd_ok, anchor_col=None):
    """base: bitmap da nen (h hang, co the rong them 2 cot le de chua dau/moc).
    Tra ve bitmap moi (cung chieu rong, cot co the am -> dung le PAD)."""
    lay = LAYOUT[size]
    top = lay['cap'] if upper else lay['x']
    PAD = 3
    h = len(base)
    W = len(base[0]) + 2 * PAD
    cv = [[0] * PAD + r[:] + [0] * PAD for r in base]
    x0, x1 = ink_cols(base)
    anchor = PAD + (x0 + x1) // 2
    odd = odd_ok and (x1 - x0 + 1) % 2 == 1
    if anchor_col is not None:      # dat dau thang mot cot (than chu), dung dau kieu le
        anchor, odd = PAD + anchor_col, True

    def mk(m):
        return ODD[m] if odd and m in ODD else MARKS[m]

    # moc (ơ ư): mot cham cheo tren phai + noi dinh
    if HORN in marks:
        cv[top - 1][PAD + x1 + 1] = 1
        cv[top][PAD + x1] = 1
    # nang: duoi chan chu, tranh cham net (dich phai)
    if DOT_BELOW in marks:
        y = lay['base'] + 2
        x = anchor
        while any(cv[yy][xx] for yy in (y - 1, y, y + 1) if 0 <= yy < h
                  for xx in (x - 1, x, x + 1)):
            x += 1
        cv[y][x] = 1
    above = [m for m in marks if m not in (HORN, DOT_BELOW)]
    if above:
        g0, g1 = 1, (0 if upper else 1)
        shapes = [mk(m) for m in above]
        while True:
            need = sum(len(s[0]) for s in shapes) + g0 + g1 * (len(shapes) - 1)
            if need <= top:
                break
            if g1:
                g1 = 0
            elif g0:
                g0 = 0
            elif any(s is MARKS['\u0309'] for s in shapes):
                shapes = [HOOK2 if s is MARKS['\u0309'] else s for s in shapes]
            else:
                raise ValueError('khong du cho cho dau')
        y = top - g0
        for i, (rows, anc) in enumerate(shapes):
            y -= len(rows)
            place(cv, rows, y, anchor - anc)
            y -= g1
    return cv, PAD


# ================================================================== nguon glyph
def load_f18():
    f = container.decode(SRC)
    assert f['height'] == 18
    g, o = {}, 0
    for ch, w in zip(f['chars'], f['widths']):
        g[ch] = [list(f['rows'][y][o:o + w]) for y in range(18)]
        o += w
    return f['chars'], g


def decomp(ch):
    d = unicodedata.normalize('NFD', ch)
    if len(d) > 1 and all(unicodedata.combining(c) for c in d[1:]):
        return d[0], list(d[1:])
    return ch, []


def sans_glyph(ch, f18, extra=None):
    if extra and ch in extra or ch in sans18.GLYPHS:
        top, spec = extra[ch] if extra and ch in extra else sans18.GLYPHS[ch]
        rows = spec.split()
        w = max(len(r) for r in rows)
        bm = blank(18, w)
        for dy, r in enumerate(rows):
            for x, c in enumerate(r):
                bm[top + dy][x] = 1 if c == '#' else 0
        return bm
    bm = f18[ch]                      # dau cau / ky hieu: lay tu f18, cat sat
    x0, x1 = ink_cols(bm)
    return [r[x0:x1 + 1] for r in bm] if x1 >= x0 else [[] for _ in bm]


def build(variant, size):
    charset, f18 = load_f18()
    serif = variant == 'serif'
    extra = sans18.SELECTIVE if variant == 'mixed' else None
    anchors = sans18.SELECTIVE_ANCHOR if variant == 'mixed' else {}
    src = dict(f18)
    # i khong cham, dung lam goc cho í ì ỉ ĩ ị
    src['ı'] = [r[:] if y >= 9 else [0] * len(r) for y, r in enumerate(f18['i'])]
    cache = {}

    def base_of(ch):
        if ch not in cache:
            bm = src[ch] if serif else sans_glyph(ch, src, extra)
            kind = 'lower' if (ch.islower() or ch in 'ıđ') else 'upper' if ch.isalnum() else 'symbol'
            ov = variant if variant != 'mixed' or ch in extra else 'sans'   # chu chung -> dung chon tay cua sans
            force = BASE_OVERRIDES.get((ov, size, ch), ())
            cache[ch] = compress(bm, size, kind, force) if bm and bm[0] else blank(KEEP_H(size), 0)
        return cache[ch]

    widths, glyphs = [], []
    for ch in charset:
        key = (variant, size, ch)
        if ch == ' ':
            w = SERIF_SPACE if serif else sans18.SPACE[size]
            bm = blank(KEEP_H(size), w)
        elif key in OVERRIDES:
            bm = parse(OVERRIDES[key])
            w = len(bm[0]) if serif else ink_cols(bm)[1] + 2
        else:
            b, marks = decomp(ch)
            if b == 'i' and marks:
                b = 'ı'
            base = base_of(b)
            if marks:
                cv, pad = compose(base, marks, size, b.isupper(), not serif, anchors.get(b))
            else:
                cv, pad = [r[:] for r in base], 0
            if serif:
                bm = [r[pad:pad + SERIF_W] for r in cv]
                lost = sum(sum(r) for r in cv) - sum(sum(r) for r in bm)
                assert lost == 0, (ch, 'dau tran khoi o 7px')
                w = SERIF_W
            else:
                x0, x1 = ink_cols(cv)
                if ch in sans18.TABULAR:
                    x0, x1 = pad, pad + 4
                bm = [r[x0:x1 + 1] + [0] for r in cv]
                w = x1 - x0 + 2
        assert len(bm) == KEEP_H(size) and all(len(r) == w for r in bm), ch
        widths.append(w)
        glyphs.append(bm)
    h = KEEP_H(size)
    rows = [sum((g[y] for g in glyphs), []) for y in range(h)]
    return charset, h, widths, rows, glyphs


def KEEP_H(size):
    return sum(KEEP[size].values())


SUFFIX = {'serif': '', 'sans': 's', 'mixed': 'c'}


def out_name(variant, size):
    return 'f%d%s.bin' % (size, SUFFIX[variant])


def sheet(charset, glyphs, per=16):
    out = []
    items = list(zip(charset, glyphs))
    for i in range(0, len(items), per):
        grp = items[i:i + per]
        out.append(' '.join(c.ljust(max(1, len(g[0]))) for c, g in grp))
        for y in range(len(grp[0][1])):
            out.append(' '.join(''.join('#' if v else '.' for v in g[y]) or '.' for c, g in grp))
        out.append('')
    return '\n'.join(out)


def main(argv):
    if '--check18' in argv:
        charset, f18 = load_f18()
        _, h, widths, rows, glyphs = build('serif', 18)
        bad = [ch for ch, g in zip(charset, glyphs) if g != f18[ch]]
        print('ghep lai f18: %d/%d glyph trung khop; khac: %s'
              % (len(charset) - len(bad), len(charset), ''.join(bad)))
        return 0
    targets = [(v, s) for s in (15, 13) for v in ('serif', 'sans', 'mixed')]
    if '--with-18s' in argv:
        targets.append(('sans', 18))
    for v, s in targets:
        charset, h, widths, rows, glyphs = build(v, s)
        data = container.encode(charset, h, widths, rows)
        path = os.path.join(ROOT, 'fonts', out_name(v, s))
        with open(path, 'wb') as fh:
            fh.write(data)
        print('%-9s %2dpx -> %s (%d byte)' % (v, s, os.path.relpath(path, ROOT), len(data)))
        if '--sheet' in argv:
            print(sheet(charset, glyphs))
    return 0


if __name__ == '__main__':
    sys.exit(main(sys.argv[1:]))
