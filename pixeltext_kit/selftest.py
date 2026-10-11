# -*- coding: utf-8 -*-
"""Self-test for the pixeltext toolkit."""
import sys, os, io, subprocess, json
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import pixeltext as pt
from pixeltext import container
from PIL import Image

ok = 0
def T(cond, what):
    global ok
    assert cond, "FAIL: " + what
    ok += 1

# ---------------------------------------------------------------- container
for s in pt.SIZES:
    f = pt.Font.load(s)
    T(f.height > 0 and len(f.glyphs) > 200, "%s loads" % s)
    raw = open(next(p for p in (os.path.join(d, pt.FONTS[s]) for d in pt.font.FONT_DIRS if d)
                    if os.path.exists(p)), 'rb').read()
    d = container.decode(raw)
    re = container.encode(d['chars'], d['height'], d['widths'], d['rows'])
    T(re == raw[:len(re)], "%s round-trips byte-exact" % s)

# ------------------------------------------------------------------ metrics
f13 = pt.Font.load(13)
T(f13.missing("Trò chơi mới") == [], "vietnamese covered at 13px")
T(f13.missing("★") == ['★'], "missing char detected")
for s in pt.SIZES:
    T(pt.Font.load(s).ambiguous_in("TRÒ ƯƠNG ẮẰẲẴ 0O Il1|") == [], "%spx: capitals with tone marks, 0/O, I/l/1 distinct" % s)
T(pt.Font.load(13).ambiguous_in(",‚") == [(',', '‚'), ('‚', ',')], ",/‚ ambiguity detected")
for s in (15, 13):
    T(pt.Font.load(s).height == s == pt.Font.load('%ds' % s).height == pt.Font.load('%dc' % s).height,
      "%dpx: all three variants" % s)
    T(set(pt.Font.load(s).glyphs[c].advance for c in pt.Font.load(s).chars if c != ' ') == {7},
      "%dpx co chan: fixed width 7 like f18" % s)
    T(pt.measure("Trò chơi mới", '%ds' % s)['w'] < pt.measure("Trò chơi mới", s)['w'] * 3 // 4,
      "%dpx khong chan is narrower" % s)
    T(pt.measure("Trò chơi mới", '%dc' % s)['w'] < pt.measure("Trò chơi mới", s)['w'] * 3 // 4,
      "%dpx chan chon loc is narrower" % s)
    sel, sans = pt.Font.load('%dc' % s), pt.Font.load('%ds' % s)
    diff = ''.join(c for c in sel.chars if sel.glyphs[c].bitmap != sans.glyphs[c].bitmap)
    T(diff == '14IJijlíìỉĩịÍÌỈĨỊ', "%dpx chan chon loc: only I i j l J 1 4 keep serifs (%s)" % (s, diff))

m = pt.measure("Trò chơi mới", 13)
T(m['w'] == 80 and m['h'] == 13 and m['lines'] == 1, "measure 13px")
EXPECT_W = {18: 80, 15: 80, 13: 80, '15s': 56, '13s': 54, '15c': 58, '13c': 56}   # "Trò chơi mới"
for s, w_ in EXPECT_W.items():
    T(pt.measure("Trò chơi mới", s)['w'] == w_, "measure %spx" % s)
T(pt.measure("a\nbb", 13)['lines'] == 2, "multi-line measure")
T(pt.measure("Trò", 13, scale=3)['w'] == pt.measure("Trò", 13)['w'] * 3, "scale multiplies")

w = pt.wrap("Đội Red Hawk đã sẵn sàng chờ lệnh xuất kích", 100, 13)
T('\n' in w, "wrap inserts newlines")
for line in w.split('\n'):
    T(pt.measure(line, 13)['w'] <= 100, "wrapped line fits: %r" % line)

# --------------------------------------------------------------------- fit
p = pt.fit("Trò chơi mới", (64, 16))
T(p and p['w'] <= 64 and p['h'] <= 16, "fit returns something that fits")
T(pt.fit("Trò chơi mới", (10, 6)) is None, "fit gives up when impossible")
p2 = pt.fit("Nhiệm vụ chính", (70, 17))
T(p2 and p2['w'] <= 70, "fit for a real UI tile")

# ------------------------------------------------------------------- layout
lay = pt.layout("Trò", 13)
T(lay.w == pt.measure("Trò", 13)['w'], "layout width == measure")
T(len(lay.scaled(2).pixels) == len(lay.pixels) * 4, "scaling quadruples pixels")
T(lay.trimmed().h <= lay.h, "trim removes blank rows")
T(len(lay.outline()) > 0, "outline produced")

# ------------------------------------------------------------------- render
img = pt.render("Trò chơi mới", 13, scale=2, fill="#ffffff", outline="#000000")
T(img.mode == 'RGBA' and img.width >= 60 * 2, "render size")
px = img.load()
T(any(px[x, y][3] for x in range(img.width) for y in range(img.height)), "render has ink")

g = pt.render("Ab", 15, fill=["#ff0000", "#0000ff"])
cols = {g.load()[x, y] for x in range(g.width) for y in range(g.height)
        if g.load()[x, y][3]}
T(len(cols) > 2, "gradient produces several colours")

art = pt.ascii_art("Trò", 13)
T(art.count("\n") == 13 + 1 and '#' in art, "ascii art shape")

# --------------------------------------------------------------------- draw
canvas = Image.new('RGBA', (120, 30), (20, 20, 30, 255))
info = pt.draw(canvas, (0, 0, 120, 30), "Bắt đầu", 13, align='center',
               valign='center', fill="#7CFF6B", outline="#0a3d14")
T(info['overflow_x'] == 0 and info['overflow_y'] == 0, "draw fits")
q = canvas.load()
T(any(q[x, y] != (20, 20, 30, 255) for x in range(120) for y in range(30)), "draw painted")

small = Image.new('RGBA', (20, 12), (0, 0, 0, 255))
info = pt.draw(small, (0, 0, 20, 12), "Nhiệm vụ chính", 15)
T(info['overflow_x'] > 0, "overflow reported")

# -------------------------------------------------------------------- patch
tile = Image.new('RGBA', (40, 16), (60, 90, 110, 255))
tq = tile.load()
for x in range(4, 30):
    for y in range(4, 12): tq[x, y] = (40, 240, 80, 255)
for x in range(4, 30): tq[x, 12] = (10, 90, 30, 255)
before = tile.copy()
pt.patch(tile, (0, 0, 40, 16), "Chỉ số", size='13s', mode='tight', gap=0)
T(tile.tobytes() != before.tobytes(), "patch changed the tile")
bg, rows, out = pt.sample_style(before, (0, 0, 40, 16))
T(bg == (60, 90, 110, 255), "sample_style found the background")
T(out == (10, 90, 30, 255), "sample_style found the outline")
T(rows and max(rows.values(), key=lambda c: c[1]) == (40, 240, 80, 255),
  "sample_style found the fill")

# ---------------------------------------------------------------------- cli
def run(*a):
    r = subprocess.run([sys.executable, '-m', 'pixeltext'] + list(a),
                       capture_output=True, text=True,
                       cwd=os.path.dirname(os.path.abspath(__file__)))
    return r.returncode, r.stdout.strip()
rc, out = run('measure', '--text', 'Trò chơi mới', '--size', 'all')
T(rc == 0 and json.loads(out)['sizes']['13px']['w'] == 80 and json.loads(out)['sizes']['13spx']['w'] == 54, "cli measure")
rc, out = run('check', '--text', 'Trò chơi mới')
T(rc == 0, "cli check clean")
rc, out = run('check', '--text', 'TRÒ')
T(rc == 0, "cli check: uppercase tones are fine in every size")
rc, out = run('check', '--text', ',‚')
T(rc == 1, "cli check flags identical glyphs ,/‚")
rc, out = run('fit', '--text', 'Trò chơi mới', '--box', '64x16')
T(rc == 0 and json.loads(out)['w'] <= 64, "cli fit")
rc, out = run('fit', '--text', 'Trò chơi mới', '--box', '8x8')
T(rc == 1, "cli fit fails loudly")

print("selftest: %d checks passed" % ok)
