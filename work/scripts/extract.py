"""Trich toan bo chu cua game ra TSV.  python3 extract.py <jar_dir> <out.tsv>"""
import sys, os, glob, csv
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import mapfmt, textfmt as T
d, outp = sys.argv[1], sys.argv[2]
rows = []
def add(f, k, b, note='', lim=''):
    s = b.decode('latin-1')
    if not s.strip(): return
    rows.append([len(rows) + 1, f, k, T.esc(s), '', note, lim])
for f in sorted(glob.glob(d + '/t/*.tdf')):
    rel = os.path.relpath(f, d)
    for k, s in enumerate(T.tdf_parse(open(f, 'rb').read())): add(rel, str(k), s, 'UI ' + rel)
for f in sorted(glob.glob(d + '/m/m*/*.m')):
    rel = os.path.relpath(f, d); i = mapfmt.parse(open(f, 'rb').read())
    mn = (i['names'][0] or b'').decode('latin-1')
    for k, s in enumerate(i['names']):
        if s: add(rel, 'name%d' % k, s, 'ten map', '127B')
    for k, s in enumerate(i['strings']): add(rel, str(k), s, 'map ' + mn)
for f in sorted(glob.glob(d + '/itm/*')):
    rel = os.path.relpath(f, d)
    for k, (nm, pr, de, rest) in enumerate(T.itm_parse(open(f, 'rb').read())):
        add(rel, '%d.ten' % k, nm, 'ten vat pham', '127B')
        if de != b'a': add(rel, '%d.mota' % k, de, 'mo ta vat pham', '255B')
for k, (nm, rest) in enumerate(T.rec_parse(open(d + '/en/edf', 'rb').read())):
    add('en/edf', '%d.ten' % k, nm, 'ten quai', '127B')
with open(outp, 'w', encoding='utf-8', newline='') as fo:
    w = csv.writer(fo, delimiter='\t', lineterminator='\n', quoting=csv.QUOTE_NONE, escapechar=None, quotechar='\x00')
    w.writerow(['id', 'file', 'khoa', 'goc', 'dich', 'ghi_chu', 'gioi_han'])
    w.writerows(rows)
print(len(rows), 'dong')
