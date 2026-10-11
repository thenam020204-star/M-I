"""Ghi ban dich vao thu muc jar da giai nen.  python3 apply_text.py <jar_dir> [text_can_dich.tsv]
Map (.m) luon duoc chuyen sang dinh dang bang chuoi u2 (ScriptLoader)."""
import sys, os, glob, csv
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import mapfmt, textfmt
d = sys.argv[1]
tr = textfmt.load_tsv(sys.argv[2]) if len(sys.argv) > 2 and os.path.exists(sys.argv[2]) else {}
n = 0
for f in sorted(glob.glob(d + '/m/m*/*.m')):
    rel = os.path.relpath(f, d)
    b = open(f, 'rb').read(); i = mapfmt.parse(b)
    names = [tr.get((rel, 'name%d' % k), s) if s is not None else None for k, s in enumerate(i['names'])]
    strs = [tr.get((rel, str(k)), s) for k, s in enumerate(i['strings'])]
    open(f, 'wb').write(mapfmt.build_new(b, i, names, strs))
n += textfmt.apply_all(d, tr)
print('apply_text: %d muc dich' % n)
