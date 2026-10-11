#!/usr/bin/env python3
"""Sinh .jad từ manifest của jar, MIDlet-Jar-Size/URL khớp jar.  make_jad.py <jar> <jad>"""
import os, sys, zipfile
jar, jad = sys.argv[1:3]
mf = zipfile.ZipFile(jar).read('META-INF/MANIFEST.MF').decode('utf-8')
lines = [l for l in mf.replace('\r', '').split('\n') if l.strip() and not l.startswith(('Manifest-Version', 'Ant-Version', 'Created-By'))]
lines += ['MIDlet-Jar-URL: %s' % os.path.basename(jar), 'MIDlet-Jar-Size: %d' % os.path.getsize(jar)]
open(jad, 'w', encoding='utf-8').write('\n'.join(lines) + '\n')
