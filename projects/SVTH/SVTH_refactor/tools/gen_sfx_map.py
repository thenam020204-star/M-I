#!/usr/bin/env python3
"""Sinh res/sfx/map.txt từ bin/skill.txt + /ft/*.av của JAR gốc.
Dùng: gen_sfx_map.py <thư mục JAR đã giải nén> <out map.txt>
Marker va chạm của hiệu ứng = frame có tổng diện tích tile lớn nhất (đỉnh hiệu ứng); đây là HEURISTIC, sửa tay được trong map.txt.
"""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import avparse

ELEM = {20: 'A', 21: 'A', 22: 'E', 23: 'E', 24: 'F', 25: 'F', 26: 'W', 27: 'W'}  # A=Phong(gió) E=Địa(đất) F=Hỏa W=Thủy
PHYS = {1, 3, 4, 5, 6, 8, 33, 34}
HEAL = {13, 14}
DRAIN = {28}
LIGHT = {18, 19}
BUFF = {2, 9, 10, 11, 12, 15, 16, 17, 39}
PASSIVE = {7}  # Phản Kích: bị động (phản đòn đi qua đường đánh thường, không tung được)

def area(o, fi):
    return sum(o['tiles'][t][3] * o['tiles'][t][4] for (_, _, t) in o['frames'][fi])

def main(jar, out):
    rows = []
    for ln in open(os.path.join(jar, 'bin/skill.txt'), encoding='utf-8-sig').read().splitlines():
        p = ln.split('\t')
        if len(p) > 5 and p[0].isdigit():
            rows.append((int(p[0]), p[1].strip(), p[3].strip(), p[2].strip()))
    L = ['# map.txt — sinh bởi tools/gen_sfx_map.py (sửa tay được; chỉ NỐI THÊM, không đổi nghĩa cột)',
         '# role <roleId> <biến thể>   0=quái/sủng vật 1=Quinn(thương) 2=Julie(súng)',
         'role 1 1', 'role 2 2', 'role 3 2', 'role 39 2', 'role 40 2', '',
         '# skill <id> <lớp> <nguyên tố> <số nhịp>   lớp: P=vật lý E=nguyên tố/ánh sáng H=hồi máu B=trạng thái/buff D=hút máu X=không có hiệu ứng (bị động)',
         '#   nguyên tố: F=Hỏa W=Thủy E=Địa A=Phong -=không; số nhịp: 1 hoặc M (nhiều nhịp: 2+(cấp-1)/4, theo bn.a(e,int,int) skill 4)']
    effects = {}
    for sid, name, eff, _ in rows:
        if eff == '-1' or sid in PASSIVE:
            cls = 'X'
        elif sid in HEAL: cls = 'H'
        elif sid in DRAIN: cls = 'D'
        elif sid in PHYS: cls = 'P'
        elif sid in ELEM or sid in LIGHT: cls = 'E'
        elif sid in BUFF: cls = 'B'
        else: cls = 'B'
        el = ELEM.get(sid, '-')
        beats = 'M' if sid == 4 else '1'
        L.append('skill %d %s %s %s   # %s  [%s]' % (sid, cls, el, beats, name, eff))
        if cls != 'X': effects[eff] = None
    L += ['', '# effect <file.av> <frame va chạm,...>   (heuristic: frame có diện tích tile lớn nhất của anim 0)']
    for eff in sorted(effects):
        p = os.path.join(jar, 'ft', eff)
        if not os.path.exists(p):
            L.append('# effect %s  THIẾU FILE' % eff); continue
        o = avparse.parse(p)
        a = o['anims'][0]
        ar = [area(o, fi) for fi in a]
        k = max(range(len(ar)), key=lambda i: (ar[i], -i))
        if k == 0:
            k = 1 if len(a) > 1 else 0  # frame 0 hiển thị trước khi có chuyển frame; dùng frame 1 trở đi
        L.append('effect %s %d   # %d frame, đỉnh diện tích' % (eff, k, len(a)))
    open(out, 'w', encoding='utf-8').write('\n'.join(L) + '\n')
    print('wrote', out, len(rows), 'skills', len(effects), 'effects')

if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2])
