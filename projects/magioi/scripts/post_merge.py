#!/usr/bin/env python3
"""Sửa sau `dp merge` những chỗ dp không làm được (dp tự cắt khoảng trắng đầu/cuối; dòng dp bỏ qua).
  post_merge.py <tsv>   (sửa tại chỗ)"""
import sys
OV = {  # gốc -> dịch
    '？？？': '???',                                   # tên người nói; font không có dấu ？ toàn khổ
    '（#￥%￥……&……*）': '(#$%$……&……*)',               # câu chửi bị che
    '孔)': ' lỗ)',                                    # "(3" + " lỗ)" + "Vũ khí..."
    '武器|攻击：': 'Vũ khí|ATK: ', '头盔|回避：': 'Mũ|Né: ', '防具|防御：': 'Giáp|DEF: ',
    '|卡片附加属性：': '|Chỉ số thẻ thêm: ', '|附加属性：': '|Chỉ số thêm: ',
    '金钱:': 'Tiền: ', '总价:': 'Tổng giá: ', '卖出价：': 'Giá bán: ', '效:': 'Hiệu ứng: ',
}
p = sys.argv[1]
L = open(p, encoding='utf-8').read().split('\n'); n = 0
for i, l in enumerate(L):
    c = l.split('\t')
    if len(c) > 4 and c[3] in OV:
        c[4] = OV[c[3]]; L[i] = '\t'.join(c); n += 1
open(p, 'w', encoding='utf-8').write('\n'.join(L))
print('post_merge: sửa', n, 'dòng')
