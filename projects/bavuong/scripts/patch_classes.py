#!/usr/bin/env python3
"""Vá class game trong thư mục jar đã giải nén (sau inject.py):
  - mọi class: Graphics.drawString / Font.stringWidth/substringWidth/getHeight -> VH.ds/sw/ssw/fh (BitmapFont 15c)
  - o.a(String,II)I, o.a(String[],II)I, o.c(String,I)I (ngắt dòng theo ký tự) -> VH.wA/wD/wC (theo từ, px)
  - o.a(String,IIIII)V (chia dòng theo số ký tự) -> VH.dsn
  - o.aI(): vùng vẽ khung thoại (pc 122..367) -> VH.box(o); vùng chân dung+chữ+nhãn skip (797..1166) -> VH.dlg(o, màu)
    phần còn lại thành nop (không còn mã chết, không còn nhánh nhảy vào giữa vùng)
  - o.ag(int): phân trang thoại 2 dòng -> 3 dòng (iconst_2 -> iconst_3 ở `t.e > 2` và `F * 2`)
  - ForeverHero: Display.setCurrent -> VL.show (splash logo, chỉ lần đầu)
  patch_classes.py <jar_dir>"""
import os, sys, glob, struct
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from bcpatch import Klass, walk
from cpool import jutf_dec

G, F, S = 'javax/microedition/lcdui/Graphics', 'javax/microedition/lcdui/Font', 'Ljava/lang/String;'
MAP = {
    (G, 'drawString', '(%sIII)V' % S, 0xb6): (0xb8, 'VH', 'ds', '(L%s;%sIII)V' % (G, S)),
    (F, 'stringWidth', '(%s)I' % S, 0xb6): (0xb8, 'VH', 'sw', '(L%s;%s)I' % (F, S)),
    (F, 'substringWidth', '(%sII)I' % S, 0xb6): (0xb8, 'VH', 'ssw', '(L%s;%sII)I' % (F, S)),
    (F, 'getHeight', '()I', 0xb6): (0xb8, 'VH', 'fh', '(L%s;)I' % F),
}
SPLASH = {('javax/microedition/lcdui/Display', 'setCurrent', '(Ljavax/microedition/lcdui/Displayable;)V', 0xb6):
          (0xb8, 'VL', 'show', '(Ljavax/microedition/lcdui/Display;Ljavax/microedition/lcdui/Displayable;)V')}

def u2(i): return struct.pack('>H', i)

def code_of(k, name, desc):
    for m, a in k.codes():
        if k.mname(m) == (name, desc): return m, a
    raise KeyError((name, desc))

def drop_stackmap(k, a):
    """bỏ StackMap/StackMapTable con của Code (preverify.sh sinh lại)"""
    ms, ml, code, tail = k.split_code(a)
    ne = struct.unpack('>H', tail[:2])[0]; et = tail[:2 + 8 * ne]; p = 2 + 8 * ne
    na = struct.unpack('>H', tail[p:p+2])[0]; p += 2; keep = []
    for _ in range(na):
        an, al = struct.unpack('>HI', tail[p:p+6])
        if jutf_dec(k.cp[an][1]) not in ('StackMap', 'StackMapTable'): keep.append(tail[p:p+6+al])
        p += 6 + al
    a[1] = bytearray(struct.pack('>HHI', ms, ml, len(code)) + code + et + struct.pack('>H', len(keep)) + b''.join(keep))
    return et

def targets(code):
    """[(pc_lệnh, đích)] mọi lệnh nhảy"""
    out = []
    for pc, op in walk(code):
        if 0x99 <= op <= 0xa8 or op in (0xc6, 0xc7):
            out.append((pc, pc + struct.unpack('>h', code[pc+1:pc+3])[0]))
        elif op in (0xc8, 0xc9):
            out.append((pc, pc + struct.unpack('>i', code[pc+1:pc+5])[0]))
        elif op in (0xaa, 0xab):
            q = (pc + 4) & ~3
            out.append((pc, pc + struct.unpack('>i', code[q:q+4])[0]))
            if op == 0xaa:
                lo, hi = struct.unpack('>ii', code[q+4:q+12])
                for j in range(hi - lo + 1): out.append((pc, pc + struct.unpack('>i', code[q+12+4*j:q+16+4*j])[0]))
            else:
                n = struct.unpack('>i', code[q+4:q+8])[0]
                for j in range(n): out.append((pc, pc + struct.unpack('>i', code[q+12+8*j:q+16+8*j])[0]))
    return out

def splice(k, a, start, end, new, expect_start, expect_end):
    """ghi `new` tại [start,end), phần dư = nop. Kiểm byte biên và không có nhánh từ ngoài nhảy vào giữa vùng."""
    ms, ml, code, tail = k.split_code(a); code = bytearray(code)
    assert code[start] == expect_start and code[end] == expect_end, ('biên vùng lệch', start, end, code[start], code[end])
    pcs = {pc for pc, op in walk(bytes(code))}
    assert start in pcs and end in pcs
    for src, dst in targets(bytes(code)):
        if not (start <= src < end): assert not (start < dst < end), ('nhánh vào giữa vùng', src, dst)
    ne = struct.unpack('>H', tail[:2])[0]
    for i in range(ne):
        s, e, h, c = struct.unpack('>HHHH', tail[2+8*i:10+8*i])
        assert not (start < h < end), 'handler trong vùng'
    assert len(new) <= end - start
    code[start:end] = bytes(new) + b'\x00' * (end - start - len(new))
    a[1] = bytearray(struct.pack('>HHI', ms, ml, len(code))) + code + tail

def patch_o(k):
    n = 0
    for name, desc, vh, vdesc, body, ret, ms, ml in [
        ('a', '(%sII)I' % S, 'wA', '(Lo;%sII)I' % S, b'\x2a\x2b\x1c\x1d', b'\xac', 4, 4),
        ('a', '([%sII)I' % S, 'wD', '(Lo;[%sII)I' % S, b'\x2a\x2b\x1c\x1d', b'\xac', 4, 4),
        ('c', '(%sI)I' % S, 'wC', '(Lo;%sI)I' % S, b'\x2a\x2b\x1c', b'\xac', 3, 3),
        ('a', '(%sIIIII)V' % S, 'dsn', '(Lo;%sIIIII)V' % S, b'\x2a\x2b\x1c\x1d\x15\x04\x15\x05\x15\x06', b'\xb1', 7, 7),
    ]:
        r = k.find_or_add_ref(10, 'VH', vh, vdesc)
        k.replace_body(name, desc, body + b'\xb8' + u2(r) + ret, ms, ml); n += 1
    # khung thoại trong aI()
    m, a = code_of(k, 'aI', '()V')
    drop_stackmap(k, a)
    rb = k.find_or_add_ref(10, 'VH', 'box', '(Lo;)V')
    rd = k.find_or_add_ref(10, 'VH', 'dlg', '(Lo;I)V')
    splice(k, a, 122, 367, b'\x2a\xb8' + u2(rb), 0x2a, 0x05)
    splice(k, a, 797, 1166, b'\x2a\x15\x08\xb8' + u2(rd), 0x2a, 0xa7)
    n += 2
    # phân trang 3 dòng trong ag(int)
    m, a = code_of(k, 'ag', '(I)V')
    te = k.find_or_add_ref(9, 't', 'e', 'I'); fF = k.find_or_add_ref(9, 'o', 'F', 'I')
    ms_, ml_, code, tail = k.split_code(a); code = bytearray(code); c = 0
    for pc, op in walk(bytes(code)):
        if op == 0xb4 and code[pc+3] == 0x05:
            ref = struct.unpack('>H', code[pc+1:pc+3])[0]
            if (ref == te and code[pc+4] == 0xa4) or (ref == fF and code[pc+4] == 0x68):
                code[pc+3] = 0x06; c += 1
    assert c == 4, 'phân trang: %d chỗ' % c
    a[1] = bytearray(struct.pack('>HHI', ms_, ml_, len(code))) + code + tail
    return n + c

def main(jd):
    for f in sorted(glob.glob(os.path.join(jd, '*.class'))):
        name = os.path.basename(f)[:-6]
        if name in ('VH', 'VL', 'BitmapFont'): continue
        k = Klass(open(f, 'rb').read())
        m = dict(MAP)
        if name == 'ForeverHero': m.update(SPLASH)
        n = k.redirect(m)
        if name == 'o': n += patch_o(k)
        if n:
            open(f, 'wb').write(k.build()); print(name, n)

if __name__ == '__main__':
    main(sys.argv[1])
