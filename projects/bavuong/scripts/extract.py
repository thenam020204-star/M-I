#!/usr/bin/env python3
"""Trích mọi chuỗi CJK hiển thị trong .class ra TSV: id, file, khoá, gốc, dịch, ghi chú, giới hạn.
  extract.py <jar_dir> <out.tsv>
Text của game này chỉ nằm trong constant pool (thoại = String[][] {portrait, sprite, emo, "[Tên]:câu"})."""
import os, re, sys, glob
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import cpool, struct
from bcpatch import Klass, walk

def ldc_order(data):
    """chỉ số CONSTANT_String theo thứ tự xuất hiện lần đầu trong bytecode (<clinit> trước)"""
    k = Klass(data); seen = []; ms = list(k.codes())
    ms.sort(key=lambda ma: k.mname(ma[0])[0] != '<clinit>')
    for m, a in ms:
        _, _, code, _ = k.split_code(a)
        for pc, op in walk(code):
            if op == 0x12: i = code[pc+1]
            elif op == 0x13: i = struct.unpack('>H', code[pc+1:pc+3])[0]
            else: continue
            if k.cp[i] and k.cp[i][0] == 8 and i not in seen: seen.append(i)
    return seen

CJK = re.compile('[\u3000-\u303f\u4e00-\u9fff\uff00-\uffef\u2014\u2026]')
SKIP = {('o.class', '\u5bbd')}          # "宽": đơn vị đo bề rộng chữ Hán, xử lý trong hook
DLG = set('abcefghiu')                   # class chỉ chứa bảng thoại / bảng chuỗi

def esc(s): return s.replace('\\', '\\\\').replace('\t', '\\t').replace('\n', '\\n')

def main(jd, outp):
    rows = []
    order = 'ouihgfecba'                      # UI trước, rồi thoại theo cốt truyện (u = sự kiện 0-34, i, h ...)
    files = sorted(glob.glob(os.path.join(jd, '*.class')),
                   key=lambda f: (order.find(os.path.basename(f)[0]) if os.path.basename(f)[0] in order else 99, f))
    for f in files:
        fn = os.path.basename(f)
        data = open(f, 'rb').read(); c = cpool.ClassFile(data)
        pos = {ci: n for n, ci in enumerate(ldc_order(data))}
        for ci, ui, s in sorted(c.strings(), key=lambda x: (pos.get(x[0], 1 << 30), x[0])):
            if not CJK.search(s) or (fn, s) in SKIP: continue
            if fn[0] in DLG and s.startswith('['):
                note, lim = 'thoại (%s)' % fn, ''
            elif fn[0] in DLG:
                note, lim = 'chuỗi/thoại trong %s' % fn, ''
            else:
                note = 'chuỗi UI trong %s' % fn
                lim = '' if len(s) > 8 else str(max(8, len(s) * 3))
            rows.append(['C:%s:%d' % (fn, ci), fn, 'cp%d' % ci, esc(s), '', note, lim])
    with open(outp, 'w', encoding='utf-8') as fo:
        fo.write('id\tfile\tkhoa\tgoc\tdich\tghi chu\tgioi han\n')
        for r in rows: fo.write('\t'.join(r) + '\n')
    print(len(rows), 'dòng ->', outp)

if __name__ == '__main__':
    main(*sys.argv[1:3])
