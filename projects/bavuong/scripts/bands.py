#!/usr/bin/env python3
"""In bố cục chữ của ảnh: các dải hàng (không trong suốt / khác nền) và các cụm cột trong mỗi dải.
  bands.py ảnh.png [gap_cột=4]"""
import sys
from PIL import Image
im = Image.open(sys.argv[1]).convert('RGBA'); gap = int(sys.argv[2]) if len(sys.argv) > 2 else 4
W, H = im.size; px = im.load()
bg = px[0, 0]
def on(x, y):
    p = px[x, y]
    return p[3] > 40 and (bg[3] < 40 or abs(p[0]-bg[0]) + abs(p[1]-bg[1]) + abs(p[2]-bg[2]) > 60)
rows = [any(on(x, y) for x in range(W)) for y in range(H)]
print(sys.argv[1], W, 'x', H, 'mode', Image.open(sys.argv[1]).mode, 'bg', bg)
y = 0
while y < H:
    if not rows[y]: y += 1; continue
    y0 = y
    while y < H and rows[y]: y += 1
    cols = [any(on(x, yy) for yy in range(y0, y)) for x in range(W)]
    segs = []; x = 0
    while x < W:
        if not cols[x]: x += 1; continue
        x0 = x; last = x
        while x < W and (cols[x] or any(cols[x:x+gap])):
            if cols[x]: last = x
            x += 1
        segs.append((x0, last - x0 + 1))
    print('  y=%d h=%d  cols=%s' % (y0, y - y0, segs))
