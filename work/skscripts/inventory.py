#!/usr/bin/env python3
"""Kiểm kê nhanh một game J2ME để biết phải Việt hoá ở đâu.

  python3 inventory.py game.jar [--json out.json]

In ra:
  - manifest (MIDP/CLDC khai báo) + API MIDP2/hãng THỰC SỰ được gọi
  - version class, đã preverify chưa (StackMap)
  - class nào vẽ chữ (drawString/drawChar/Font...), class nào đọc resource nào
  - nghi vấn font bitmap riêng (resource tên *font* + class đọc nó)
  - resource chứa chữ Hán/CJK (đếm ký tự) và ảnh PNG (để soát chữ trong ảnh)
"""
import json, re, sys, zipfile, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from classfile import ClassInfo

TEXT_API = ('drawString', 'drawSubstring', 'drawChar', 'drawChars', 'stringWidth',
            'substringWidth', 'charWidth', 'charsWidth', 'setFont', 'getFont')
UI_API = ('javax/microedition/lcdui/Form', 'javax/microedition/lcdui/Alert',
          'javax/microedition/lcdui/List', 'javax/microedition/lcdui/TextBox',
          'javax/microedition/lcdui/StringItem', 'javax/microedition/lcdui/Command')
MIDP2 = {('javax/microedition/lcdui/Graphics', 'drawRGB'), ('javax/microedition/lcdui/Graphics', 'drawRegion'),
         ('javax/microedition/lcdui/Image', 'createRGBImage'), ('javax/microedition/lcdui/Image', 'getRGB')}
CJK = re.compile(r'[㐀-鿿＀-￯　-〿]')


def _common(ch):
    # chữ Hán thông dụng = GB2312 cấp 1 (byte đầu 0xB0-0xD7): lọc nhiễu từ dữ liệu nhị phân
    try:
        g = ch.encode('gb2312')
        return len(g) == 2 and 0xB0 <= g[0] <= 0xD7
    except UnicodeEncodeError:
        return False


def cjk_count(b):
    """(số chữ Hán, encoding) — chỉ đếm cụm >=3 chữ mà >=80% là chữ thông dụng."""
    best = (0, '')
    for enc in ('utf-8', 'gbk', 'utf-16-be', 'utf-16-le', 'big5'):
        k = 0
        for m in re.findall(CJK.pattern + '{3,}', b.decode(enc, 'ignore')):
            if sum(map(_common, m)) >= 0.8 * len(m):
                k += len(m)
        if k > best[0]:
            best = (k, enc)
    return best


def main():
    jar = sys.argv[1]
    z = zipfile.ZipFile(jar)
    rep = {'manifest': {}, 'classes': {}, 'text_classes': {}, 'resource_loaders': {},
           'midp2_or_vendor_calls': {}, 'cjk_resources': {}, 'images': [], 'cjk_in_class_strings': {}}
    try:
        for l in z.read('META-INF/MANIFEST.MF').decode('utf-8', 'replace').splitlines():
            if ':' in l:
                k, v = l.split(':', 1); rep['manifest'][k.strip()] = v.strip()
    except KeyError:
        pass
    versions, unverified = {}, []
    for n in z.namelist():
        b = z.read(n)
        if n.endswith('.class'):
            try:
                c = ClassInfo(b)
            except Exception as e:
                rep['classes'][n] = 'PARSE ERROR %s' % e; continue
            versions['%d.%d' % (c.major, c.minor)] = versions.get('%d.%d' % (c.major, c.minor), 0) + 1
            if 'StackMap' not in c.attrs and c.branches:
                unverified.append(n)
            t = sorted({m for (o, m, _) in c.refs if m in TEXT_API and o.startswith('javax/microedition/lcdui/')}
                       | {o.split('/')[-1] for (o, _, _) in c.refs if o in UI_API})
            if t:
                rep['text_classes'][c.name] = t
            res = [s for s in c.strings if re.search(r'\.(bin|dat|txt|png|res|fnt|lang|str)$', s, re.I)]
            if res:
                rep['resource_loaders'][c.name] = res
            v = sorted({'%s.%s' % (o.split('/')[-1], m) for (o, m, _) in c.refs
                        if (o, m) in MIDP2 or o.startswith('javax/microedition/media')
                        or o.startswith('com/')})
            if v:
                rep['midp2_or_vendor_calls'][c.name] = v
            hz = [s for s in c.strings if CJK.search(s)]
            if hz:
                rep['cjk_in_class_strings'][c.name] = len(hz)
        elif n.lower().endswith('.png'):
            rep['images'].append(n)
        elif not n.endswith('/') and not n.startswith('META-INF'):
            k, enc = cjk_count(b)
            if k >= 4:
                rep['cjk_resources'][n] = '%d (%s)' % (k, enc)
    rep['classes'] = {'versions': versions, 'not_preverified': unverified}
    fonts = {cl: r for cl, r in rep['resource_loaders'].items() if any('font' in x.lower() for x in r)}
    rep['bitmap_font_suspects'] = fonts

    if '--json' in sys.argv:
        json.dump(rep, open(sys.argv[sys.argv.index('--json') + 1], 'w'), ensure_ascii=False, indent=1)
    m = rep['manifest']
    print('MIDlet: %s | %s | %s' % (m.get('MIDlet-Name'), m.get('MicroEdition-Profile'), m.get('MicroEdition-Configuration')))
    print('Class versions: %s | chưa preverify: %s' % (versions, unverified or 'không'))
    print('API MIDP2/MMAPI/hãng đang dùng:', sorted({x for v in rep['midp2_or_vendor_calls'].values() for x in v})[:25])
    print('Class vẽ chữ:', json.dumps(rep['text_classes'], ensure_ascii=False))
    print('Nghi font bitmap riêng:', json.dumps(fonts, ensure_ascii=False) or 'không')
    print('Resource có CJK (số ký tự):', json.dumps(dict(sorted(rep['cjk_resources'].items(), key=lambda x: -int(x[1].split()[0]))), ensure_ascii=False))
    print('Chuỗi CJK trong .class:', json.dumps(rep['cjk_in_class_strings']))
    print('Ảnh PNG: %d (soát bằng mắt ảnh nào có chữ)' % len(rep['images']))


if __name__ == '__main__':
    main()
