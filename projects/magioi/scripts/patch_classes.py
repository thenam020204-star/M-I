#!/usr/bin/env python3
"""Vá class game trong thư mục jar đã giải nén (sau inject.py):
  - Graphics.drawString / Font.stringWidth/charWidth/getHeight -> VH.ds/sw/cw/fh (BitmapFont 13c)
  - d.a(String,Font,int) (ngắt dòng theo ký tự) -> VH.wrap (theo từ)
  - k.a(BBB)/k.w()/k.d(Graphics) (khung thoại 2 dòng) -> trampoline gọi VH.open/tick/paint
  - XMessage: Display.setCurrent(game) -> VL.show (splash logo)
  - d.class: hằng "测" (đơn vị bề rộng d.b) -> "MM" (=12px ở 13c)
  patch_classes.py <jar_dir>"""
import os, sys, glob, struct
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from bcpatch import Klass, walk

G, F, S = 'javax/microedition/lcdui/Graphics', 'javax/microedition/lcdui/Font', 'Ljava/lang/String;'
MAP = {
    (G, 'drawString', '(%sIII)V' % S, 0xb6): (0xb8, 'VH', 'ds', '(L%s;%sIII)V' % (G, S)),
    (F, 'stringWidth', '(%s)I' % S, 0xb6): (0xb8, 'VH', 'sw', '(L%s;%s)I' % (F, S)),
    (F, 'charWidth', '(C)I', 0xb6): (0xb8, 'VH', 'cw', '(L%s;C)I' % F),
    (F, 'getHeight', '()I', 0xb6): (0xb8, 'VH', 'fh', '(L%s;)I' % F),
    ('d', 'a', '(%sL%s;I)[%s' % (S, F, S), 0xb8): (0xb8, 'VH', 'wrap', '(%sL%s;I)[%s' % (S, F, S)),
}
SPLASH = {('javax/microedition/lcdui/Display', 'setCurrent', '(Ljavax/microedition/lcdui/Displayable;)V', 0xb6):
          (0xb8, 'VL', 'show', '(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V')}

def u2(i): return struct.pack('>H', i)

def patch_k(k):
    aL = k.find_or_add_ref(9, 'k', 'aL', '[' + S)
    aR = k.find_or_add_ref(9, 'k', 'aR', 'B')
    aQ = k.find_or_add_ref(9, 'k', 'aQ', 'Z')
    y = k.find_or_add_ref(9, 'k', 'y', 'Lj;')
    op = k.find_or_add_ref(10, 'VH', 'open', '(%sII)V' % S)
    tk = k.find_or_add_ref(10, 'VH', 'tick', '()I')
    pt = k.find_or_add_ref(10, 'VH', 'paint', '(L%s;Ljava/lang/Object;)V' % G)
    # k.a(byte nv, byte câu, byte phía): VH.open(aL[câu], nv, phía); aR = 1; aQ = false
    k.replace_body('a', '(BBB)V', b'\xb2' + u2(aL) + b'\x1b\x32\x1a\x1c\xb8' + u2(op)
                   + b'\x04\xb3' + u2(aR) + b'\x03\xb3' + u2(aQ) + b'\xb1', 3, 3)
    # k.w(): aR = (byte)VH.tick(); if (aR == 0) aQ = true
    k.replace_body('w', '()V', b'\xb8' + u2(tk) + b'\x59\x91\xb3' + u2(aR) + b'\x9a\x00\x07'
                   + b'\x04\xb3' + u2(aQ) + b'\xb1', 2, 0)
    # k.d(Graphics): VH.paint(g, y)
    k.replace_body('d', '(L%s;)V' % G, b'\x2a\xb2' + u2(y) + b'\xb8' + u2(pt) + b'\xb1', 2, 1)

def patch_namebox(k):
    """h: this.a(l.d[3], j.aS, 3, 0) -> TextBox tên tối đa 3 ký tự (tên Hán). Đổi iconst_3 -> iconst_5
    (cùng 1 byte) để tên Latin như "Aruru" không ném IllegalArgumentException."""
    tgt = None
    for i, e in enumerate(k.cp):
        if e and e[0] == 10 and k.ref(i) == ('h', 'a', '(%s%sII)V' % (S, S)): tgt = i
    n = 0
    for m, a in k.codes():
        ms, ml, code, tail = k.split_code(a); code = bytearray(code)
        for pc, op in walk(bytes(code)):
            if op in (0xb6, 0xb7) and struct.unpack('>H', code[pc+1:pc+3])[0] == tgt \
                    and code[pc-2] == 0x06 and code[pc-1] == 0x03:
                code[pc-2] = 0x08; n += 1
        a[1] = bytearray(struct.pack('>HHI', ms, ml, len(code))) + code + tail
    assert n == 1, 'không thấy lời gọi TextBox tên (%d)' % n
    return n

def main(jd):
    for f in sorted(glob.glob(os.path.join(jd, '*.class'))):
        name = os.path.basename(f)[:-6]
        if name in ('VH', 'VL', 'BitmapFont'): continue
        k = Klass(open(f, 'rb').read())
        m = dict(MAP)
        if name == 'd': del m[('d', 'a', '(%sL%s;I)[%s' % (S, F, S), 0xb8)]
        if name == 'XMessage': m.update(SPLASH)
        n = k.redirect(m)
        if name == 'k': patch_k(k); n += 3
        if name == 'h': n += patch_namebox(k)
        if name == 'd':
            for ci, ui, s in k.strings():
                if s == '测': k.cp[ci] = [8, k.find_or_add_utf('MM')]; n += 1
        if n:
            open(f, 'wb').write(k.build()); print(name, n)

if __name__ == '__main__':
    main(sys.argv[1])
