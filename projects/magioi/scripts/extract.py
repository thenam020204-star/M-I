#!/usr/bin/env python3
"""Trích mọi chuỗi hiển thị ra TSV: id, file, khoá, gốc, dịch, ghi chú, giới hạn.
  extract.py <jar_dir> <res_dir> <out.tsv>
Nguồn: map*.map (lời thoại, mapfmt), l9.pro trong gói res (vật phẩm/trang bị/kỹ năng),
chuỗi CJK trong .class (cpool). Chuỗi trùng được dp gộp."""
import os, re, sys, struct, glob
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import mapfmt, cpool, profmt

CJK = re.compile('[\u3000-\u303f\u4e00-\u9fff\uff00-\uffef\u2014\u2026]')

def names(jd):
    c = cpool.ClassFile(open(os.path.join(jd, 'l.class'), 'rb').read())
    return c

def speakers_guess(rest, nstr):
    """Đoán lệnh 0 [nhân vật, câu, phía] trong dữ liệu kịch bản sau chuỗi."""
    out = {}; pos = 0
    for s in range(nstr):
        for p in range(pos, len(rest) - 3):
            if rest[p] == 0 and rest[p+2] == s and rest[p+1] < 40 and rest[p+3] in (0, 1):
                out[s] = rest[p+1]; pos = p + 4; break
    return out

def main(jd, rd, outp):
    rows = []
    # l.H (tên người nói theo id, đọc từ l.java)
    H = ['主角', '道具商', '武器商', '萨琳', '导演', '阿塔雷', '母亲', '父亲', '塔塔', '花子', '丁克', '对话提示',
         '面具男', '面具女', '魔王宇春', '？？？', '曾哥', '雪丸', '团长', '艾蕾娜', '蘑菇老大', '众蘑菇', '众人',
         '宝爷', '丁次', '雷米', '卡兹', '卡玛', '莎尔娜', '息崔', '强尼', '尼格罗', '辛', '洛克', '达德利']
    def mk(i, f, k, s, note, lim=''):
        rows.append([i, f, k, s.replace('\t', '\\t').replace('\n', '\\n'), '', note, str(lim) if lim else ''])
    for f in sorted(glob.glob(os.path.join(jd, 'map*.map')), key=lambda x: int(re.findall(r'\d+', os.path.basename(x))[0])):
        fn = os.path.basename(f)
        head, blocks, tail = mapfmt.parse(open(f, 'rb').read())
        for bi, (strs, rest) in enumerate(blocks):
            sp = speakers_guess(rest, len(strs))
            for si, s in enumerate(strs):
                if not CJK.search(s): continue
                who = sp.get(si)
                note = 'thoại %s' % fn
                if who is not None:
                    note += ', người nói~%s' % (H[who] if who < len(H) else '#%d' % who)
                mk('M:%s:%d:%d' % (fn, bi, si), fn, '%d/%d' % (bi, si), s, note)
    pro = profmt.parse(open(os.path.join(rd, 'l9.pro'), 'rb').read())
    for sec, arr in pro['strings'].items():
        for i, s in enumerate(arr):
            if not CJK.search(s): continue
            kind = {'az': 'vật phẩm: tên' if i % 2 == 0 else 'vật phẩm: mô tả',
                    'aB': 'trang bị: tên', 'ay': 'kỹ năng/thẻ: tên'}[sec]
            lim = 10 if (sec != 'az' or i % 2 == 0) else ''
            mk('P:%s:%d' % (sec, i), 'l9.pro', '%s[%d]' % (sec, i), s, kind, lim)
    for f in sorted(glob.glob(os.path.join(jd, '*.class'))):
        fn = os.path.basename(f)
        c = cpool.ClassFile(open(f, 'rb').read())
        for ci, ui, s in c.strings():
            if not CJK.search(s): continue
            if fn == 'd.class' and s == '测': continue   # đơn vị đo bề rộng, xử lý riêng trong hook
            lim = len(s) * 2 + 2 if len(s) <= 6 and '|' not in s else ''
            mk('C:%s:%d' % (fn, ci), fn, 'cp%d' % ci, s, 'chuỗi UI trong %s' % fn, lim)
    with open(outp, 'w', encoding='utf-8') as fo:
        fo.write('id\tfile\tkhoa\tgoc\tdich\tghi chu\tgioi han\n')
        for r in rows: fo.write('\t'.join(r) + '\n')
    print(len(rows), 'dòng ->', outp)

if __name__ == '__main__':
    main(*sys.argv[1:4])
