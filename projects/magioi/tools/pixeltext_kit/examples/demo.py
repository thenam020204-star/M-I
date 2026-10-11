# -*- coding: utf-8 -*-
"""Vẽ thử chuỗi "Trò chơi mới" ở mọi font (18/15/13 có chân, 15s/13s không chân, 15c/13c chân chọn lọc)."""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
import pixeltext as pt
from PIL import Image, ImageDraw

TEXT = "Trò chơi mới"
OUT = os.path.dirname(os.path.abspath(__file__))

# --- 1. kiểm tra trước khi vẽ ------------------------------------------------
for s in pt.SIZES:
    f = pt.Font.load(s)
    print("%3spx  rộng=%2dpx  cao=%2d  thiếu=%s  trùng glyph=%s"
          % (s, pt.measure(TEXT, s)['w'], f.height,
             ''.join(f.missing(TEXT)) or '-',
             ''.join(c for c, _ in f.ambiguous_in(TEXT)) or '-'))

# --- 2. ASCII, để kiểm tra mà không cần mở ảnh -------------------------------
for s in pt.SIZES:
    print("\n--- %spx ---" % s)
    print(pt.ascii_art(TEXT, s))

# --- 3. ba cỡ, ba kiểu tô ----------------------------------------------------
STYLES = [
    dict(name="dac",      fill="#e8f4ff", outline="#0d2233"),
    dict(name="gradient", fill=["#bff3ff", "#1f6fd0"], outline="#04121f",
         shadow="#000000b0", shadow_offset=(1, 2)),
    dict(name="vang",     fill=["#fff3b0", "#ff8a2b"], outline="#3a1600",
         outline_diag=True),
]
SCALE = 4
rows = []
for s in pt.SIZES:
    for st in STYLES:
        kw = {k: v for k, v in st.items() if k != 'name'}
        rows.append(("%spx · %s" % (s, st['name']),
                     pt.render(TEXT, s, scale=SCALE, pad=3, **kw)))

LW = 150
W = LW + max(i.width for _, i in rows) + 24
H = sum(i.height + 14 for _, i in rows) + 40
sheet = Image.new('RGBA', (W, H), (14, 16, 22, 255))
d = ImageDraw.Draw(sheet)
d.text((12, 12), 'pixeltext - "Tro choi moi" x%d' % SCALE, fill=(255, 214, 110, 255))
y = 36
for label, img in rows:
    d.text((12, y + img.height // 2 - 4), label, fill=(150, 190, 230, 255))
    sheet.alpha_composite(img, (LW, y))
    y += img.height + 14
sheet.convert('RGB').save(os.path.join(OUT, 'demo_trochoimoi.png'))
print("\n->", os.path.join(OUT, 'demo_trochoimoi.png'), sheet.size)
