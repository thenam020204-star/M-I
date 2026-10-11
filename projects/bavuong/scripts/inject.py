#!/usr/bin/env python3
"""Nạp bản dịch vào .class trong thư mục jar đã giải nén (sửa tại chỗ).
  inject.py <tsv_dịch> <jar_dir>
Dòng chưa dịch giữ gốc. Utf8 chỉ dùng bởi đúng 1 CONSTANT_String thì sửa tại chỗ (không phình class);
dùng chung (tên field/method...) thì thêm Utf8 mới. Chỉ số cp của String giữ nguyên nên ldc không đổi."""
import os, sys, glob
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import cpool

def unesc(s):
    out = []; i = 0
    while i < len(s):
        if s[i] == '\\' and i + 1 < len(s):
            out.append({'n': '\n', 't': '\t', '\\': '\\'}.get(s[i+1], s[i:i+2])); i += 2
        else: out.append(s[i]); i += 1
    return ''.join(out)

def load(tsv):
    tr = {}
    for ln in open(tsv, encoding='utf-8').read().split('\n')[1:]:
        if not ln.strip(): continue
        c = ln.split('\t')
        if len(c) > 4 and c[4]: tr[c[0]] = unesc(c[4])
    return tr

def uses(c):
    n = {}
    for e in c.cp:
        if not e: continue
        if e[0] in (7, 8): n[e[1]] = n.get(e[1], 0) + 1
        elif e[0] == 12:
            for v in e[1]: n[v] = n.get(v, 0) + 1
    return n

def main(tsv, jd):
    tr = load(tsv); used = set()
    for f in sorted(glob.glob(os.path.join(jd, '*.class'))):
        fn = os.path.basename(f); c = cpool.ClassFile(open(f, 'rb').read()); ch = False
        # Utf8 còn được dùng bởi field/method/attribute name (tham chiếu từ phần rest) -> coi như dùng chung
        n = uses(c)
        for ci, ui, s in c.strings():
            k = 'C:%s:%d' % (fn, ci)
            if k not in tr: continue
            if n.get(ui, 0) == 1 and ui not in shared_rest(c):
                c.set_utf(ui, tr[k])
            else:
                c.cp[ci] = [8, c.add([1, cpool.jutf_enc(tr[k])])]
            used.add(k); ch = True
        if ch: open(f, 'wb').write(c.build())
    miss = set(tr) - used
    print('đã nạp', len(used), 'chuỗi', ('; KHÔNG khớp id: %d %s' % (len(miss), sorted(miss)[:5])) if miss else '')

_cache = {}
def shared_rest(c):
    """Utf8 index được tham chiếu từ ngoài constant pool (tên field/method/descriptor/attribute)."""
    if id(c) in _cache: return _cache[id(c)]
    from bcpatch import Klass
    k = Klass(c.build()); s = set()
    for grp in (k.fields, k.methods):
        for a, nm, ds, attrs in grp:
            s |= {nm, ds} | {an for an, ab in attrs}
            for an, ab in attrs:
                if cpool.jutf_dec(k.cp[an][1]) == 'Code':
                    import struct
                    ms, ml, cl = struct.unpack('>HHI', ab[:8]); p = 8 + cl
                    ne = struct.unpack('>H', ab[p:p+2])[0]; p += 2 + 8 * ne
                    na = struct.unpack('>H', ab[p:p+2])[0]; p += 2
                    for _ in range(na):
                        an2, al = struct.unpack('>HI', ab[p:p+6]); s.add(an2); p += 6 + al
    # SourceFile/khác ở cattrs: tên attribute + có thể giá trị
    r = k.cattrs; import struct
    na = struct.unpack('>H', r[:2])[0]; p = 2
    for _ in range(na):
        an, al = struct.unpack('>HI', r[p:p+6]); s.add(an)
        if al == 2: s.add(struct.unpack('>H', r[p+6:p+8])[0])
        p += 6 + al
    _cache[id(c)] = s
    return s

if __name__ == '__main__':
    main(*sys.argv[1:3])
