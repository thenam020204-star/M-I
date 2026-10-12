#!/usr/bin/env python3
"""Chép ảnh đã Việt hoá (assets/res/*) đè lên thư mục gói res đã giải nén.  vh_res.py <res_dir>"""
import os, sys, shutil
src = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'assets', 'res')
n = 0
if os.path.isdir(src):
    for f in os.listdir(src):
        assert os.path.exists(os.path.join(sys.argv[1], f)), 'không có trong gói: ' + f
        shutil.copy(os.path.join(src, f), os.path.join(sys.argv[1], f)); n += 1
print('ảnh res đã vá:', n)
