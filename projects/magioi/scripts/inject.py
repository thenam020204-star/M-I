#!/usr/bin/env python3
"""Nạp bản dịch vào thư mục jar đã giải nén (sửa tại chỗ).
  inject.py <tsv_dịch> <jar_dir> <res_dir_gốc>
Dòng chưa có bản dịch giữ nguyên gốc. Đọc lại từ file GỐC (jar_dir phải là bản giải nén sạch)."""
import os, re, sys, glob, shutil, tempfile
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import mapfmt, cpool, profmt, respack

def load(tsv):
    tr = {}
    for ln in open(tsv, encoding='utf-8').read().split('\n')[1:]:
        if not ln.strip(): continue
        c = ln.split('\t')
        dst = c[4] if len(c) > 4 else ''
        if dst:
            tr[c[0]] = dst.replace('\\n', '\n').replace('\\t', '\t')
    return tr

def main(tsv, jd, rd):
    tr = load(tsv); used = set()
    for f in glob.glob(os.path.join(jd, 'map*.map')):
        fn = os.path.basename(f); b = open(f, 'rb').read()
        head, blocks, tail = mapfmt.parse(b)
        ch = False
        for bi, (strs, rest) in enumerate(blocks):
            for si in range(len(strs)):
                k = 'M:%s:%d:%d' % (fn, bi, si)
                if k in tr: strs[si] = tr[k]; used.add(k); ch = True
        if ch: open(f, 'wb').write(mapfmt.build(head, blocks, tail))
    # l9.pro trong gói res
    tmp = tempfile.mkdtemp()
    for x in os.listdir(rd): shutil.copy(os.path.join(rd, x), tmp)
    d = profmt.parse(open(os.path.join(tmp, 'l9.pro'), 'rb').read())
    for sec, arr in d['strings'].items():
        for i in range(len(arr)):
            k = 'P:%s:%d' % (sec, i)
            if k in tr: arr[i] = tr[k]; used.add(k)
    open(os.path.join(tmp, 'l9.pro'), 'wb').write(profmt.build(d))
    respack.pack(tmp, jd); shutil.rmtree(tmp)
    for f in glob.glob(os.path.join(jd, '*.class')):
        fn = os.path.basename(f); c = cpool.ClassFile(open(f, 'rb').read()); ch = False
        for ci, ui, s in c.strings():
            k = 'C:%s:%d' % (fn, ci)
            if k in tr:
                # Utf8 có thể dùng chung bởi nhiều String -> tạo Utf8 mới cho riêng String này
                nu = c.add([1, cpool.jutf_enc(tr[k])]); c.cp[ci] = [8, nu]; used.add(k); ch = True
        if ch: open(f, 'wb').write(c.build())
    miss = set(tr) - used
    print('đã nạp', len(used), 'chuỗi', ('; KHÔNG khớp id: %d %s' % (len(miss), sorted(miss)[:5])) if miss else '')

if __name__ == '__main__':
    main(*sys.argv[1:4])
