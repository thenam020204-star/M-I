# -*- coding: utf-8 -*-
"""Dựng thử một màn hình game 240x320 bằng pixeltext, ở mọi font (18/15/13 có chân, 15s/13s không chân, 15c/13c chân chọn lọc).

Mọi chữ trên màn hình đều do pixeltext vẽ - không dùng font hệ thống.
"""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
import pixeltext as pt
from PIL import Image, ImageDraw

W, H = 240, 320
OUT = os.path.dirname(os.path.abspath(__file__))

BG      = (10, 14, 24, 255)
PANEL   = (22, 34, 54, 255)
PANEL2  = (16, 24, 40, 255)
LINE    = (60, 120, 180, 255)
ACCENT  = (255, 196, 60, 255)
TEXT    = (206, 228, 250, 255)
DIM     = (110, 140, 172, 255)
SEL_BG  = (30, 78, 130, 255)
GREEN   = (110, 240, 140, 255)

ITEMS = ["Trò chơi mới", "Tải game", "Cài đặt", "Trợ giúp", "Thoát"]
SEL = 0
BLURB = ("Đội Red Hawk đã sẵn sàng. "
         "Chọn “Trò chơi mới” để bắt đầu chiến dịch Lorraine Canyon.")


def rounded(d, box, fill, outline=None, r=3):
    x0, y0, x1, y1 = box
    d.rectangle([x0 + r, y0, x1 - r, y1], fill=fill)
    d.rectangle([x0, y0 + r, x1, y1 - r], fill=fill)
    for cx, cy in ((x0 + r, y0 + r), (x1 - r, y0 + r), (x0 + r, y1 - r), (x1 - r, y1 - r)):
        d.ellipse([cx - r, cy - r, cx + r, cy + r], fill=fill)
    if outline:
        d.rectangle([x0, y0, x1, y1], outline=outline)


def screen(size):
    """Vẽ toàn bộ màn hình với font cỡ `size`."""
    f = pt.Font.load(size)
    img = Image.new('RGBA', (W, H), BG)
    d = ImageDraw.Draw(img)

    # nền: lưới sao mờ
    for y in range(0, H, 8):
        d.line([(0, y), (W, y)], fill=(14, 20, 34, 255))

    # ---- thanh tiêu đề ------------------------------------------------------
    d.rectangle([0, 0, W - 1, 34], fill=PANEL)
    d.line([(0, 35), (W - 1, 35)], fill=LINE)
    pt.draw(img, (0, 3, W, 16), "STAR FLEET", size, align='center',
            fill=["#fff6c9", "#ffae2b"], outline="#331a00", scale=1)
    pt.draw(img, (0, 18, W, 14), "ZERO - bản tiếng Việt", size,
            align='center', fill=DIM)

    # ---- danh sách menu -----------------------------------------------------
    top = 52
    row_h = f.height + 9
    for i, it in enumerate(ITEMS):
        y = top + i * row_h
        box = (18, y, W - 18, row_h - 3)
        if i == SEL:
            rounded(d, (box[0], box[1], box[0] + box[2] - 1, box[1] + box[3] - 1),
                    SEL_BG, LINE)
            pt.draw(img, (box[0] + 8, box[1], box[2] - 16, box[3]), ">", size,
                    align='left', valign='center', fill=ACCENT)
            pt.draw(img, box, it, size, align='center', valign='center',
                    fill="#ffffff", outline="#08243f")
        else:
            pt.draw(img, box, it, size, align='center', valign='center', fill=TEXT)

    # ---- bảng thông tin (tự xuống dòng theo bề rộng) -----------------------
    py = top + len(ITEMS) * row_h + 10
    ph = H - 26 - py
    d.rectangle([14, py, W - 15, py + ph], fill=PANEL2, outline=LINE)
    inner_w = W - 30 - 12
    body = pt.wrap(BLURB.replace('“', '"').replace('”', '"'), inner_w, size)
    pt.draw(img, (20, py + 5, inner_w, ph - 10), body, size,
            align='left', valign='top', fill=DIM, line_spacing=2, trim_v=False)

    # ---- thanh phím mềm -----------------------------------------------------
    d.rectangle([0, H - 20, W - 1, H - 1], fill=PANEL)
    d.line([(0, H - 21), (W - 1, H - 21)], fill=LINE)
    pt.draw(img, (6, H - 19, 90, 18), "Chọn", size, align='left', valign='center',
            fill=GREEN)
    pt.draw(img, (W - 96, H - 19, 90, 18), "Thoát", size, align='right',
            valign='center', fill=GREEN)

    # ---- nhãn cỡ font -------------------------------------------------------
    pt.draw(img, (W - 60, 40, 56, 12), "%spx" % size, size, align='right',
            valign='center', fill=(90, 120, 150, 255))
    return img


def main():
    shots = [(s, screen(s)) for s in pt.SIZES]
    for s, im in shots:
        im.convert('RGB').save(os.path.join(OUT, 'menu_%spx.png' % s))
    SC = 2
    pad = 12
    sheet = Image.new('RGBA', (len(shots) * (W * SC + pad) + pad,
                               H * SC + pad * 2 + 18), (26, 28, 34, 255))
    d = ImageDraw.Draw(sheet)
    for i, (s, im) in enumerate(shots):
        x = pad + i * (W * SC + pad)
        d.text((x, 4), "font %spx" % s, fill=(255, 214, 110, 255))
        sheet.alpha_composite(im.resize((W * SC, H * SC), Image.NEAREST), (x, 18 + pad // 2))
    sheet.convert('RGB').save(os.path.join(OUT, 'menu_all.png'))
    print("->", os.path.join(OUT, 'menu_all.png'), sheet.size)


if __name__ == '__main__':
    main()
