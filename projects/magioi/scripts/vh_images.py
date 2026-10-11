#!/usr/bin/env python3
"""Vẽ lại ảnh có chữ Hán bằng pixeltext (font 13c, một màu = màu thân chữ gốc, không viền).
  vh_images.py <res_dir_gốc> <out_dir>   (out_dir = assets/res, được vh_res.py chép đè vào gói)"""
import os, sys
sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'tools', 'pixeltext_kit'))
from PIL import Image
import pixeltext as pt

SRC, OUT = sys.argv[1], sys.argv[2]

def job(name, cells, keep_outline=False):
    im = Image.open(os.path.join(SRC, name)).convert('RGBA')
    for box, text in cells:
        kw = {'outline_diag': False} if keep_outline else {'outline': None}   # chỉ giữ viền (4 hướng) khi ảnh gốc có viền
        pt.patch(im, box, text, fill_rule='flat', size='13c', align='center', valign='center', **kw,
                 bg=(255, 255, 255, 0))   # nền trong suốt của ảnh gốc (góc ô có thể dính nét chữ)
    im.save(os.path.join(OUT, name))
    print(name, len(cells))

# menu chính: 7 dòng 64x16 (h.g[4], cắt theo r*16)
job('menu_string.png', [((0, i * 16, 64, 16), t) for i, t in enumerate(
    ['Chơi mới', 'Chơi tiếp', 'Cài đặt', 'Trợ giúp', 'Giới thiệu', 'Thoát game', 'Thêm game'])])
# menu trong game: 17 ô 30x16 (i.c[3]): 0-4 chỉ số gốc, 5-9 chỉ số chiến đấu (nằm trên nền vàng nhạt,
# gốc có viền đen -> giữ viền, chọn chữ ngắn không dấu chồng để viền không dính), 10-16 tab menu (nền nâu, không viền)
job('menu_ingamestring.png', [((i * 30, 0, 30, 16), t) for i, t in enumerate(
    ['STR', 'VIT', 'AGI', 'DEX', 'Còn', 'ATK', 'DEF', 'Né', 'CRIT', 'Bội'])], keep_outline=True)
SRC = OUT   # ô tab vẽ tiếp trên ảnh vừa ghi
job('menu_ingamestring.png', [((300 + i * 30, 0, 30, 16), t) for i, t in enumerate(
    ['Chỉ số', 'Túi đồ', 'Vũ khí', 'K.năng', 'Bản đồ', 'N.vụ', 'Hệ th.'])])
