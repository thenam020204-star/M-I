#!/usr/bin/env python3
"""Kiểm tra jar đã patch so với jar gốc — chạy sau MỖI lần build. Exit 1 = REJECT.

  python3 jar_check.py goc.jar patched.jar

Kiểm:
  1. Binary diff: file thêm / xoá / đổi (để biết chính xác patch đã đụng gì)
  2. Mỗi class: version <= 48, KHÔNG có StackMapTable (định dạng Java 6, KVM từ chối),
     có StackMap (đã preverify CLDC) nếu bản gốc có
  3. Tham chiếu ra ngoài jar chỉ tới CLDC/MIDP/API hãng (classfile.ALLOWED_PREFIXES)
     và không gọi API mà bản gốc chưa từng gọi (in cảnh báo để tự xác nhận)
  4. Class đổi: public/protected member của bản gốc phải còn nguyên descriptor
"""
import hashlib, os, sys, zipfile
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from classfile import ClassInfo, ALLOWED_PREFIXES


def load(p):
    z = zipfile.ZipFile(p)
    return {n: z.read(n) for n in z.namelist() if not n.endswith('/')}


def main():
    a, b = load(sys.argv[1]), load(sys.argv[2])
    errs, warns = [], []
    added = sorted(set(b) - set(a)); removed = sorted(set(a) - set(b))
    changed = sorted(n for n in set(a) & set(b) if hashlib.md5(a[n]).digest() != hashlib.md5(b[n]).digest())
    print('Thêm: %s\nXoá: %s\nĐổi: %s' % (added, removed, changed))

    internal = {n[:-6] for n in b if n.endswith('.class')}
    orig_ext = set()
    orig_cls = {}
    for n, d in a.items():
        if n.endswith('.class'):
            c = ClassInfo(d); orig_cls[n] = c
            orig_ext |= {r for r in c.refs if r[0] not in internal and not r[0].startswith('[')}
    for n in added + changed:
        if not n.endswith('.class'):
            continue
        try:
            c = ClassInfo(b[n])
        except Exception as e:
            errs.append('%s: không đọc được (%s)' % (n, e)); continue
        if c.major > 48:
            errs.append('%s: class version %d > 48 — biên dịch lại kiểu CLDC (ecj -target 1.3) rồi preverify' % (n, c.major))
        if 'StackMapTable' in c.attrs:
            errs.append('%s: còn StackMapTable — KVM thật sẽ VerifyError; gỡ rồi preverify lại' % n)
        o = orig_cls.get(n)
        if c.branches and 'StackMap' not in c.attrs:
            errs.append('%s: có rẽ nhánh nhưng thiếu StackMap -> chưa preverify' % n)
        for r in c.refs:
            if r[0] in internal or r[0].startswith('['):
                continue
            if r in orig_ext:
                continue
            if not r[0].startswith(ALLOWED_PREFIXES):
                errs.append('%s: gọi API ngoài CLDC/MIDP: %s.%s%s' % (n, r[0], r[1], r[2]))
            else:
                warns.append('%s: API mới so với bản gốc: %s.%s%s (xác nhận máy đích có)' % (n, r[0], r[1], r[2]))
        if o:
            lost = o.members - c.members
            if lost:
                errs.append('%s: mất/đổi descriptor member public: %s' % (n, sorted(lost)))
    for w in sorted(set(warns)):
        print('WARN', w)
    for e in errs:
        print('REJECT', e)
    print('RESULT:', 'FAIL' if errs else 'PASS')
    sys.exit(1 if errs else 0)


if __name__ == '__main__':
    main()
