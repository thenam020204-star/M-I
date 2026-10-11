#!/usr/bin/env python3
"""Vẽ lại các ảnh có chữ Hán bằng font pixeltext (cùng bộ font với BitmapFont trong game).
  vh_images.py <thư_mục_jar_gốc_giải_nén> <thư_mục_ra>   -> ghi ảnh đã vá theo đúng đường dẫn trong jar
Mỗi ô: (x, y, w, h) = vùng code cắt-vẽ (setClip) lấy từ o.java; chữ cũ trong ô bị xoá, chữ mới canh giữa
theo chiều dọc. Danh sách chữ là các phương án theo thứ tự ưu tiên: lấy phương án đầu tiên vừa ô
(tính cả viền 1px). Màu = màu thân chữ gốc (một màu) + màu viền gốc nếu ảnh gốc có viền.
Ảnh được nới rộng (new_size) chỉ khi code clip rộng hơn ảnh gốc."""
import os, sys
from PIL import Image
HERE = os.path.dirname(os.path.abspath(__file__))
for p in (os.path.join(HERE, '..', 'tools', 'pixeltext-kit'), os.path.join(HERE, '..', 'work', 'tools', 'pixeltext-kit')):
    if os.path.isdir(p): sys.path.insert(0, p)
from pixeltext.metrics import layout
from pixeltext import paint

DARK = '#3b0900'      # viền nâu đỏ của nhãn UI gốc
LIGHT = '#f6e8bd'     # thân chữ sáng của nhãn UI gốc

def C(x, y, w, h, texts, fill, outline=None, sizes=('13c', '13s'), align='center'):
    return dict(box=(x, y, w, h), texts=texts if isinstance(texts, list) else [texts],
                fill=fill, outline=outline, sizes=sizes, align=align)

def rows(ys, w, h, texts, fill, outline=None, **kw):
    return [C(0, y, w, h, t, fill, outline, **kw) for y, t in zip(ys, texts)]

# ảnh -> (kích thước mới hoặc None, [ô])
JOBS = {
    # T[2]/T[3]: chữ hiệu ứng khi né / chí mạng (vẽ cả ảnh, canh giữa theo getWidth)
    'image/game/game_008.png': (None, [C(0, 0, 55, 21, 'Né!', '#f2f3ff', '#1f31ff', sizes=(15, '15c'))]),
    'image/game/game_009.png': ((60, 21), [C(0, 0, 60, 21, ['Bạo kích!', 'Chí mạng!'], '#ffff00', '#9f1b00', sizes=('15c', '13c'))]),
    # al[0]: lên cấp (clip 55x13)
    'image/game/game_012.png': (None, [C(0, 0, 55, 13, ['Lên cấp!', 'Lên cấp'], '#ffff00', '#900500')]),
    # al[2]: "#跳过" (khung thoại cũ - nay VH tự vẽ "#Bỏ qua") + "继续"
    'image/game/game_014.png': (None, [C(0, 0, 33, 13, ['#Bỏ qua', 'Bỏ qua'], '#ffd800', DARK),
                                       C(33, 0, 25, 13, 'Tiếp', '#ffd800', DARK)]),
    # al[4]: "#敌人信息" (clip 60x15)
    'image/game/game_016.png': (None, [C(0, 0, 60, 15, ['#Tin địch', '#Xem địch'], 'auto')]),
    # ai[3]: menu nhân vật, hàng 12px, clip 48x12
    'image/game/game_032.png': (None, rows([0, 12, 24, 36, 48], 48, 12,
                                           [['Nhân vật'], ['Trang bị', 'T.bị'], ['Võ công'], ['Nhiệm vụ', 'N.vụ'], ['Menu']],
                                           'auto')),
    # ai[5]: nhãn chỉ số cột 26px (hàng 14px rồi 12px)
    'image/game/game_034.png': (None, rows([0, 14, 28, 42, 56, 70, 84], 26, 13,
                                           ['Lv', 'EXP', 'ATK', 'DEF', 'HP', 'MP', 'DEX'], 'auto')
                                      + rows([98, 110, 122, 134], 26, 11,
                                             [['C.hóa', '+Lv'], ['Nặng', 'KL'], ['Đồ', 'Item'], ['Hệ', 'Elem']], 'auto')),
    # ai[6]: STR AGI HP MP (clip 23) + "Điểm" (clip 47 -> nới ảnh)
    'image/game/game_035.png': ((47, 59), rows([0, 12, 24, 36], 23, 11, ['STR', 'AGI', 'HP', 'MP'], 'auto')
                                + [C(0, 48, 47, 11, ['Điểm', 'Pts'], 'auto', align='left')]),
    # ai[9]: nghề, ô 25x13 tại x = 0 / 26
    'image/game/game_038.png': (None, [C(0, 0, 25, 13, 'Kiếm', 'auto'), C(26, 0, 25, 13, ['Quyền', 'Võ'], 'auto')]),
    # ai[10]: gợi ý phím trong túi đồ, clip 53x11 (một chỗ 47)
    'image/game/game_039.png': (None, rows([0, 12, 24, 36], 47, 11, ['#Đồ', '*Chi tiết', '*Chi tiết', '#Vứt'], 'auto',
                                           align='left')),
    # ai[12]: tab túi đồ / nhiệm vụ, clip 47x11, chữ sáng không viền
    'image/game/game_041.png': (None, rows([0, 12, 24, 36, 48], 47, 11,
                                           [['Trang bị', 'T.bị'], ['Phím tắt', 'Phím'], ['NV chính'], ['NV phụ'], ['Tài sản']],
                                           'auto')),
    # ai[18]: menu tạm dừng, clip 61x14
    'image/game/game_047.png': ((61, 70), rows([0, 14, 28, 42, 56], 61, 14,
                                               ['Tiếp tục', 'Lưu game', ['Cách chơi'], 'Cài đặt', ['Menu chính']],
                                               'auto')),
    # ai[19]: tên cửa hàng, clip 53x11
    'image/game/game_048.png': ((53, 35), rows([0, 12, 24], 53, 11, [['Vật dụng'], ['Ch.luyện'], ['Rèn đúc']],
                                               'auto')),
    # al[7] / aa[30]: "右键:返回"
    'image/game/game_053.png': (None, [C(0, 0, 52, 13, [[('Phải', '#ffd800'), (':Về', '#4cffed')]], None, DARK)]),
    'image/menu/menu_030.png': (None, [C(0, 0, 52, 13, [[('Phải', '#ffd800'), (':Về', '#4cffed')]], None, DARK)]),
    # ai[20]: thuộc tính, ô 12x11: không / Hỏa / Băng / Lôi
    'image/game/game_059.png': (None, [C(0, 0, 12, 11, '-', LIGHT), C(12, 0, 12, 11, 'H', '#ffbd2e'),
                                       C(24, 0, 12, 11, 'B', '#93c8fe'), C(36, 0, 12, 11, 'L', '#ffff00')]),
    # K: nút bản đồ góc trên phải (neo phải): sao 0..15 giữ nguyên, chữ nới ảnh sang phải
    'image/game/game_map.png': ((50, 15), [C(16, 0, 34, 15, ['Bản đồ'], '#ffd800', DARK)]),
    # ae[1]: "按确定键继续" (vẽ theo getWidth/getHeight)
    'image/intro/intro_001.png': ((93, 15), [C(0, 0, 93, 15, ['Nhấn OK để tiếp tục'], '#f3cf2b', '#5d2413')]),
    # aa[2..4]: menu chính / chơi mới / trợ giúp: hàng 20px, clip 100 từ cx-35; chữ canh giữa thanh chọn (x tâm = 33)
    'image/menu/menu_002.png': ((100, 120), [C(0, 20 * i, 66, 20, t, '#4b462c', '#ffffff', sizes=('15c',))
                                             for i, t in enumerate(['Chơi mới', 'Tiếp tục', 'Trợ giúp', 'Cài đặt', 'Thông tin', 'Thoát'])]),
    'image/menu/menu_003.png': ((100, 40), [C(0, 20 * i, 66, 20, t, '#4b462c', '#ffffff', sizes=('15c',))
                                            for i, t in enumerate([['Hành trình mới', 'Chơi mới'], 'Tiếp tục'])]),
    'image/menu/menu_004.png': ((100, 60), [C(0, 20 * i, 66, 20, t, '#4b462c', '#ffffff', sizes=('15c',))
                                            for i, t in enumerate(['Cách chơi', 'Cốt truyện', 'Trợ giúp'])]),
    # aa[14]/aa[15]: cài đặt (nhãn clip 25..28, giá trị clip 25), hàng 14px
    'image/menu/menu_014.png': (None, rows([0, 14, 28, 42], 25, 14, [['Âm'], ['Tốc', 'T.độ'], ['Đi'], ['Rung']], 'auto')),
    'image/menu/menu_015.png': (None, rows([0, 14, 28, 42], 25, 14, ['Tắt', 'Bật', 'Auto', 'Tay'], 'auto')),
    # aa[18] "继续" / aa[26] "#跳过" (clip 34) ở màn intro
    'image/menu/menu_018.png': (None, [C(0, 0, 25, 13, 'Tiếp', '#ffd800', DARK)]),
    'image/menu/menu_026.png': (None, [C(0, 0, 34, 13, ['#Bỏ qua', 'Bỏ qua'], '#ffd800', DARK, sizes=('13c', '13s')),
                                       C(34, 0, 24, 13, 'Tiếp', '#ffd800', DARK)]),
    # I / J: nút "菜单" / "属性" góc dưới (nền hoa văn lặp 4px: dựng lại nền bằng 4 cột mép trái)
    'image/game/game_menu.png': (None, [dict(C(4, 3, 29, 16, 'Menu', '#f8d18a', '#040404'), tile4=True)]),
    'image/game/game_status.png': (None, [dict(C(3, 3, 31, 16, ['Chỉ số', 'C.số'], '#f8d18a', '#040404'), tile4=True)]),
    # v: "金钱"
    'image/money.png': (None, [C(0, 0, 23, 11, 'Tiền', 'auto')]),
}

KEEP_ART = {'image/game/game_map.png', 'image/game/game_menu.png', 'image/game/game_status.png'}   # giữ hình (ngôi sao), chỉ xoá ô chữ

def segs_of(t, fill):
    return t if isinstance(t, list) else [(t, fill)]

def fits(segs, size, w, h, pad, relaxed=False):
    """relaxed: thân chữ lọt ô nhưng viền trên/dưới có thể bị cắt 1px (chữ có dấu chồng như ế, ặ)"""
    full = ''.join(s for s, _ in segs)
    m = layout(full, size, 'metric', 1, None, 'left', 0, 1)
    b = m.ink_bbox()
    ih = (b[3] - b[1] + 1) if b[3] >= b[1] else 0
    return m.w + 2 * pad <= w and ih + (0 if relaxed else 2 * pad) <= h, m.w, ih

def auto_style(orig, box):
    """màu thân (phổ biến nhất) + màu viền (màu tối nhất, nếu ô có cả màu sáng) của ô trong ảnh gốc"""
    import collections
    x, y, w, h = box
    cnt = collections.Counter()
    for yy in range(y, min(y + h, orig.height)):
        for xx in range(x, min(x + w, orig.width)):
            p = orig.getpixel((xx, yy))
            if p[3] > 128: cnt[p[:3]] += 1
    if not cnt: return None, None
    lum = lambda c: 0.299 * c[0] + 0.587 * c[1] + 0.114 * c[2]
    dark = min(cnt, key=lum)
    light = [c for c in cnt if lum(c) - lum(dark) > 90]
    hexc = lambda c: '#%02x%02x%02x' % c
    if lum(dark) < 90 and light:
        body = max(light, key=lambda c: cnt[c])
        return hexc(body), hexc(dark)
    return hexc(cnt.most_common(1)[0][0]), None

def draw_cell(img, c, log, orig=None):
    if c['fill'] == 'auto':
        c = dict(c); c['fill'], c['outline'] = auto_style(orig, c['box'])
    x, y, w, h = c['box']
    pad = 1 if c['outline'] else 0
    pick = None
    for t in c['texts']:
        for relaxed in (False, True):
            for s in c['sizes']:
                ok, tw, ih = fits(segs_of(t, c['fill']), s, w, h, pad, relaxed)
                if ok: pick = (t, s); break
            if pick: break
        if pick: break
    if not pick:
        pick = (c['texts'][-1], c['sizes'][-1])
        log.append('  ! KHÔNG VỪA %s %r' % (c['box'], pick))
    t, s = pick
    segs = segs_of(t, c['fill'])
    px = img.load()
    for yy in range(y, min(y + h, img.height)):
        for xx in range(x, min(x + w, img.width)):
            px[xx, yy] = orig.getpixel((xx % 4, yy)) if c.get('tile4') else (0, 0, 0, 0)
    full = ''.join(a for a, _ in segs)
    m = layout(full, s, 'metric', 1, None, 'left', 0, 1)
    b = m.ink_bbox(); ih = b[3] - b[1] + 1
    ax = {'left': pad, 'center': (w - m.w) // 2, 'right': w - m.w - pad}[c['align']]
    ay = (h - ih) // 2 - b[1]
    # viền cho cả chuỗi trước, rồi thân chữ từng đoạn màu
    if c['outline']:
        paint._blit_clipped(img, m, (x + ax, y + ay), (0, 0, 0, 0), c['outline'], False, None, (1, 1), (x, y, w, h))
    cx = x + ax
    for txt, col in segs:
        mm = layout(txt, s, 'metric', 1, None, 'left', 0, 1)
        paint._blit_clipped(img, mm, (cx, y + ay), col, None, False, None, (1, 1), (x, y, w, h))
        cx += mm.w + 1
    log.append('  %s %s/%s -> %r [%s]' % (c['box'], c['fill'], c['outline'], full, s))

def main(src, dst):
    for rel, (newsize, cells) in JOBS.items():
        im = Image.open(os.path.join(src, rel)).convert('RGBA'); orig = im.copy()
        if rel not in KEEP_ART:
            im = Image.new('RGBA', im.size, (0, 0, 0, 0))   # ảnh toàn chữ: xoá sạch chữ gốc
        if newsize:
            big = Image.new('RGBA', newsize, (0, 0, 0, 0)); big.paste(im, (0, 0)); im = big
        log = []
        for c in cells: draw_cell(im, c, log, orig)
        out = os.path.join(dst, rel); os.makedirs(os.path.dirname(out), exist_ok=True)
        im.save(out, optimize=True)
        print(rel, im.size); print('\n'.join(log))

if __name__ == '__main__':
    main(*sys.argv[1:3])
