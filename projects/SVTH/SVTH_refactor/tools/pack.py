#!/usr/bin/env python3
"""pack.py <orig.jar> <patched_dir> <new_classes_dir> <res_dir> <out.jar> <version> [--drop name ...]
Gói JAR chưa preverify: manifest đầu tiên (giữ MỌI thuộc tính gốc, đổi MIDlet-Version), entry sắp xếp,
tài nguyên gốc chép NGUYÊN (byte-for-byte), class đã vá thay thế bản gốc, class/tài nguyên mới thêm vào."""
import sys, os, zipfile, re

orig, patched, newcls, res, out, ver = sys.argv[1:7]
extra_manifest = []
if '--manifest' in sys.argv:
    i = sys.argv.index('--manifest')
    extra_manifest = sys.argv[i + 1].split('|')
zo = zipfile.ZipFile(orig)
man = zo.read('META-INF/MANIFEST.MF').decode('utf-8')
lines = [l for l in man.replace('\r\n', '\n').split('\n') if l.strip()]
lines = [('MIDlet-Version: ' + ver) if l.startswith('MIDlet-Version:') else l for l in lines]
lines += extra_manifest
manifest = '\r\n'.join(lines) + '\r\n\r\n'

entries = {}
for zi in zo.infolist():
    if zi.filename.endswith('/') or zi.filename == 'META-INF/MANIFEST.MF':
        continue
    entries[zi.filename] = zo.read(zi.filename)
def addtree(root):
    if not root or not os.path.isdir(root): return
    for r, ds, fs in os.walk(root):
        for f in fs:
            p = os.path.join(r, f)
            entries[os.path.relpath(p, root).replace(os.sep, '/')] = open(p, 'rb').read()
for root in (patched, newcls, res):
    addtree(root)
with zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED) as z:
    z.writestr('META-INF/MANIFEST.MF', manifest)
    for n in sorted(entries):
        z.writestr(n, entries[n])
print('packed', out, len(entries) + 1, 'entries')
