#!/usr/bin/env python3
"""Render frames of an .av into a contact sheet. usage: avrender.py file.av out.png anim [res_dir]"""
import sys, os
sys.path.insert(0, os.path.dirname(__file__))
import avparse
from PIL import Image, ImageDraw

def render_frame(o, fidx, imgs, size=(160, 160), origin=(80, 110)):
    im = Image.new('RGBA', size, (60, 60, 80, 255))
    for (x, y, t) in o['frames'][fidx]:
        if t >= len(o['tiles']): continue
        ii, tx, ty, tw, th, flip = o['tiles'][t]
        src = imgs.get(ii)
        if src is None: continue
        crop = src.crop((tx, ty, tx + tw, ty + th))
        if flip & 1: crop = crop.transpose(Image.FLIP_LEFT_RIGHT)
        if flip & 2: crop = crop.transpose(Image.FLIP_TOP_BOTTOM)
        im.alpha_composite(crop.convert('RGBA'), (origin[0] + x, origin[1] + y)) if 0 <= origin[0]+x and 0 <= origin[1]+y and origin[0]+x+tw <= size[0] and origin[1]+y+th <= size[1] else im.paste(crop.convert('RGBA'), (origin[0] + x, origin[1] + y), crop.convert('RGBA'))
    return im

def main(av, out, anim, resdirs):
    o = avparse.parse(av)
    imgs = {}
    for i, n in enumerate(o['imgs']):
        base = n.rsplit('.', 1)[0]
        for d in resdirs:
            for ext in ('.png', '.jpg'):
                p = os.path.join(d, base + ext)
                if os.path.exists(p): imgs[i] = Image.open(p).convert('RGBA'); break
            if i in imgs: break
    seq = o['anims'][anim]
    cols = 8
    rows = (len(seq) + cols - 1) // cols
    sheet = Image.new('RGBA', (cols * 160, rows * 160), (0, 0, 0, 255))
    d = ImageDraw.Draw(sheet)
    for k, f in enumerate(seq):
        fr = render_frame(o, f, imgs)
        sheet.paste(fr, ((k % cols) * 160, (k // cols) * 160))
        d.text(((k % cols) * 160 + 3, (k // cols) * 160 + 3), f"k{k} f{f}", fill=(255, 255, 0, 255))
    sheet.save(out)

if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2], int(sys.argv[3]), sys.argv[4:] or ['.'])
