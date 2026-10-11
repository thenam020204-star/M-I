# -*- coding: utf-8 -*-
"""Build font_10px_v2.bin: same charset / height / row-metrics as font_10px.bin,
with the whole Vietnamese diacritic zone redrawn.

Rules enforced here (see README-v2 for the reasoning):
  * base Latin/digit/punctuation glyphs are copied bit-for-bit from the old cut
  * lowercase bodies never move          -> x-height, baseline, descender kept
  * capitals that carry a mark ABOVE are shortened 7 -> 6 rows (rows 2..7)
  * marks live only in rows 0-1, row 2 stays empty (never touches the letter)
  * stacked lowercase (hat + tone) widens 5 -> 7 so hat and tone never merge
  * no mark ever puts ink on (row 1, advance-1)  -> cannot fuse with the next
    glyph's ascender / i-dot
"""
import os, sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from pixeltext import container
import spec

SRC = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'pixeltext', 'fonts', 'font_10px.bin')
OUT = os.environ.get('OUT', os.path.join(os.path.dirname(os.path.abspath(__file__)), 'pixeltext', 'fonts', 'font_10px_v2.bin'))
H = 10

old = container.decode(SRC)
CHARS = old['chars']

# ------------------------------------------------------------------ old glyphs
oldg, o = {}, 0
for ch, w in zip(old['chars'], old['widths']):
    oldg[ch] = (w, [[old['rows'][y][o + x] for x in range(w)] for y in range(H)])
    o += w


def blank(w):
    return [[0] * w for _ in range(H)]


def body_of(ch, rows):
    """copy rows `rows` of an old glyph into a fresh cell of the same width"""
    w, bm = oldg[ch]
    g = blank(w)
    for y in rows:
        g[y] = list(bm[y])
    return g


def widen(g, w):
    out = []
    for r in g:
        out.append(list(r) + [0] * (w - len(r)))
    return out


def ink_cols(g, rows=None):
    rows = range(H) if rows is None else rows
    cs = [x for x in range(len(g[0])) if any(g[y][x] for y in rows)]
    return (cs[0], cs[-1]) if cs else (0, -1)


def put(g, mark, row, col):
    cells, mw = mark
    for (dy, dx) in cells:
        g[row + dy][col + dx] = 1
    return mw


def contacts(g, cells, row, col):
    """how badly a mark collides with the letter: overlap counts double"""
    n = 0
    for (dy, dx) in cells:
        y, x = row + dy, col + dx
        if g[y][x]:
            n += 2                      # mark would sit inside the letter
        elif y + 1 < H and g[y + 1][x]:
            n += 1                      # mark would touch the letter's top
    return n


def place_hat(g, mark, body_rows, w):
    """lowest (closest to the letter) placement that never touches it"""
    cells, mw = mark
    x0, x1 = ink_cols(g, body_rows)
    c = (x0 + x1) / 2.0
    base = int(round(c - (mw - 1) / 2.0))
    best = None
    for row in (1, 0):
        for d in (0, 1, -1, 2, -2, 3, -3):
            L = base + d
            if L < 0 or L + mw > w:
                continue
            n = contacts(g, cells, row, L)
            if n == 0 and d == 0:
                return row, L
            score = n + 2 * abs(d)      # centred beats contact-free-but-skewed
            if best is None or score < best[0]:
                best = (score, row, L)
    if best:
        return best[1], best[2]
    return 0, max(0, min(base, w - mw))


def centred(g, mw, rows):
    x0, x1 = ink_cols(g, rows)
    c = (x0 + x1) / 2.0
    left = int(round(c - (mw - 1) / 2.0))
    return max(0, min(left, len(g[0]) - mw))


# -------------------------------------------------------------- 6-row capitals
NARROW_CAP_HAT = os.environ.get('NARROWCAP', '1') == '1'
CAPROW = int(os.environ.get('CAPROW', '2'))   # 2 -> 6-row caps, 3 -> 5-row caps


def short_cap(name):
    art = spec.SHORT_CAPS[name]
    if CAPROW == 3:
        art = spec.SHORT_CAPS5[name]
    w = len(art[0])
    g = blank(w)
    for i, line in enumerate(art):
        g[CAPROW + i] = [1 if c == '#' else 0 for c in line]
    return g


BASE_UP = {k: short_cap(k) for k in spec.SHORT_CAPS}

# base bodies, lowercase (rows never move)
BASE_LO = {
    'a': body_of('a', range(3, 8)),
    'e': body_of('e', range(3, 8)),
    'i': body_of('i', range(3, 8)),          # dotless stem, w2
    'o': body_of('o', range(3, 8)),
    'u': body_of('u', range(3, 8)),
    'y': body_of('y', range(3, 10)),
    'o_horn': body_of('ơ', range(2, 8)),
    'u_horn': body_of('ư', range(2, 8)),
}
# dotless i for accented forms is 3 wide (stem centred) like the old cut
_i3 = blank(3)
for y in range(3, 8):
    _i3[y][1] = 1
BASE_LO['i'] = _i3

STACK_W = {'a': 7, 'e': 7, 'o': 7}                 # lowercase hat+tone widths
STACK_W_UP = {'A': 8, 'E': 8, 'O': 9}              # capitals already wide enough


def build(ch, base, hat, tone, dot):
    upper = base[0].isupper()
    src = (BASE_UP if upper else BASE_LO)[base]
    orig_w = len(src[0])
    scratch = orig_w + 6
    g = widen([list(r) for r in src], scratch)
    body_rows = range(CAPROW, H) if upper else range(2, H)

    if hat:
        hm = (spec.HATS_N if (upper and NARROW_CAP_HAT) else spec.HATS)[hat]
        hrow, L = place_hat(g, hm, body_rows, orig_w)
        put(g, hm, hrow, L)
        if tone:                       # tone sits up-right of the hat cluster
            for (dy, dx) in spec.STACK_TONES[tone]:
                g[dy][L + hm[1] + dx] = 1
    elif tone:
        mk = spec.TONES[tone]
        if mk[1] > orig_w:
            mk = spec.TONES_S[tone]
        put(g, mk, 0, centred(g, mk[1], body_rows))

    # final advance: keep the old one unless the mark needs more room; always
    # keep (row 1, last column) clear so the mark cannot fuse with the next glyph
    last = max([x for x in range(scratch) if any(g[y][x] for y in range(H))] or [0])
    need = last + 1 + (1 if g[1][last] else 0)
    w = max(orig_w, need)
    g = [r[:w] for r in g]

    if dot:
        x0, x1 = ink_cols(g, body_rows)
        c = int((x0 + x1) / 2.0 + 0.5)
        if any(g[9]) or any(g[8]):            # descender letter (y): find a free slot
            free = [x for x in range(w) if not g[8][x] and not g[9][x]
                    and (x == 0 or not g[9][x - 1]) and (x == w - 1 or not g[9][x + 1])]
            c = min(free, key=lambda x: abs(x - c)) if free else c
        g[9][c] = 1
    return w, g


new = {}
for ch, base, hat, tone, dot in spec.VOWELS:
    new[ch] = build(ch, base, hat, tone, dot)

# đ / Đ keep their old drawing (bar, no mark above)
# ------------------------------------------------------------------- assemble
widths, rows = [], [[] for _ in range(H)]
for ch in CHARS:
    if ch in new:
        w, g = new[ch]
    else:
        w, g = oldg[ch]
    widths.append(w)
    for y in range(H):
        rows[y].extend(g[y])

data = container.encode(CHARS, H, widths, rows)
open(OUT, 'wb').write(data)
print('wrote %s  %d bytes  %d chars  atlas %d' % (OUT, len(data), len(CHARS), sum(widths)))
