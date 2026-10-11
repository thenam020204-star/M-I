"""Doc/ghi cac dinh dang chu (tru map). Moi muc dich co khoa (file, key) -> bytes UTF-8."""
import csv, glob, os
def load_tsv(p):
    out = {}
    with open(p, encoding='utf-8', newline='') as f:
        for r in csv.DictReader(f, delimiter='\t'):
            if r.get('dich'):
                out[(r['file'], r['khoa'])] = unesc(r['dich']).replace('~', '!').encode('utf-8')
    return out
def esc(s): return s.replace('\\', '\\\\').replace('\t', '\\t').replace('\n', '\\n')
def unesc(s):
    o = []; i = 0
    while i < len(s):
        if s[i] == '\\' and i + 1 < len(s):
            o.append({'\\': '\\', 't': '\t', 'n': '\n'}.get(s[i+1], s[i+1])); i += 2
        else: o.append(s[i]); i += 1
    return ''.join(o)
# ---- /t/*.tdf: u1 so chuoi, moi chuoi u2 BE do dai + byte
def tdf_parse(b):
    n = b[0]; p = 1; out = []
    for _ in range(n):
        L = b[p] << 8 | b[p+1]; p += 2; out.append(b[p:p+L]); p += L
    assert p == len(b); return out
def tdf_build(strs):
    o = bytearray([len(strs)])
    for s in strs: o += len(s).to_bytes(2, 'big') + s
    return bytes(o)
def apply_all(d, tr):
    n = 0
    for f in sorted(glob.glob(d + '/t/*.tdf')):
        rel = os.path.relpath(f, d); b = open(f, 'rb').read(); s = tdf_parse(b)
        s2 = [tr.get((rel, str(k)), x) for k, x in enumerate(s)]
        n += sum(1 for a, c in zip(s, s2) if a != c)
        open(f, 'wb').write(tdf_build(s2))
    for f in sorted(glob.glob(d + '/itm/*')):
        rel = os.path.relpath(f, d); recs = itm_parse(open(f, 'rb').read()); out = []
        for k, (nm, pr, de, rest) in enumerate(recs):
            nm2 = tr.get((rel, '%d.ten' % k), nm); de2 = tr.get((rel, '%d.mota' % k), de)
            n += (nm2 != nm) + (de2 != de); out.append((nm2, pr, de2, rest))
        open(f, 'wb').write(itm_build(out))
    f = d + '/en/edf'; rel = 'en/edf'; recs = rec_parse(open(f, 'rb').read()); out = []
    for k, (nm, rest) in enumerate(recs):
        nm2 = tr.get((rel, '%d.ten' % k), nm); n += nm2 != nm; out.append((nm2, rest))
    open(f, 'wb').write(rec_build(out))
    return n
# ---- /itm/NN: chuoi ban ghi [u2 do dai][ban ghi]; ban ghi: [u1 dai ten][ten][i32 gia][u1 dai mo ta][mo ta][phan con lai]
def itm_parse(b):
    p = 0; recs = []
    while p < len(b):
        L = b[p] << 8 | b[p+1]; r = b[p+2:p+2+L]; p += 2 + L
        n = r[0]; name = r[1:1+n]; q = 1 + n + 4; m = r[q]; desc = r[q+1:q+1+m]
        recs.append((name, r[1+n:q], desc, r[q+1+m:]))
    assert p == len(b); return recs
def itm_build(recs):
    o = bytearray()
    for name, price, desc, rest in recs:
        assert len(name) < 256 and len(desc) < 256
        r = bytes([len(name)]) + name + price + bytes([len(desc)]) + desc + rest
        r = r[:-1] + bytes([sum(r[:-1]) & 0xFF])   # byte cuoi = tong kiem (rpg.Resources.boolean_a)
        o += len(r).to_bytes(2, 'big') + r
    return bytes(o)
# ---- /en/edf: [u2 do dai][ban ghi], ban ghi bat dau [u1 dai ten][ten]
def rec_parse(b):
    p = 0; recs = []
    while p < len(b):
        L = b[p] << 8 | b[p+1]; r = b[p+2:p+2+L]; p += 2 + L
        recs.append((r[1:1+r[0]], r[1+r[0]:]))
    assert p == len(b); return recs
def rec_build(recs):
    o = bytearray()
    for name, rest in recs:
        assert len(name) < 128
        r = bytes([len(name)]) + name + rest
        r = r[:-1] + bytes([sum(r[:-1]) & 0xFF])   # byte cuoi = tong kiem
        o += len(r).to_bytes(2, 'big') + r
    return bytes(o)
