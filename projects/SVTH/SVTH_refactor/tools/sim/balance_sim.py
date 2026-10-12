#!/usr/bin/env python3
"""balance_sim.py — mô phỏng cân bằng quái (phiên 8). Chạy: python3 -I tools/sim/balance_sim.py [--new]
Mô hình hero: chỉ số gốc team.bin + 10 điểm/cấp chia đều 4 thuộc tính (attrGrowth của Quinn/Julie) + trang bị trung bình theo cấp
(equip.bin: vũ khí/mũ/áo/giày/dây chuyền/nhẫn cấp yêu cầu <= L, lấy giữa khoảng). Quái: stardata/petdata như GameUtil.a (tăng trưởng
lấy nửa dưới khoảng). Sát thương thường: atk*2 - def (+2.5 ngẫu nhiên), tối thiểu 10."""
import sys
STAR = [l.split('\t') for l in """140|190	55|75	120|130	55|75	30|50	12|14	30|50	11|13	20|40	10|12
170|230	65|82	150|180	65|82	40|70	13|16	40|70	12|14	30|50	11|13
210|260	73|94	190|220	73|94	60|90	15|18	60|90	13|16	40|70	12|14
230|290	83|111	230|250	83|111	80|110	17|20	80|110	14|18	50|90	13|16
270|320	97|133	250|280	97|133	90|130	19|22	90|130	16|19	60|110	14|18
300|350	111|154	290|310	111|154	100|140	21|24	100|140	18|20	70|130	16|19
330|390	130|167	320|340	130|167	110|150	23|26	110|150	20|22	80|140	18|20
370|430	142|175	350|380	142|175	120|160	25|28	120|160	22|25	90|150	20|22
450|490	151|188	390|420	151|188	130|170	27|30	130|170	24|28	100|160	22|25
1510|1530	220|250	1000|1200	175|200	140|190	29|33	140|190	26|30	110|170	24|28""".split('\n')]
def rng(s): a, b = map(int, s.split('|')); return a, b
def mon(tier, L):
    """tier 1..10 cho mọi chỉ số; trả (hp, atk, def, spd) kỳ vọng."""
    r = STAR[tier - 1]; out = []
    for c in (0, 4, 6, 8):
        b0, b1 = rng(r[c]); g0, g1 = rng(r[c + 1])
        g = (g0 + (g0 + (g1 - (g1 + g0) // 2 + 1) - 1)) / 2.0
        out.append((b0 + b1) / 2.0 + g * (L - 1))
    return out
HERO = {'Quinn': (700, 280, 290, 150, [(7,0,6,2,0),(0,50,2,1,2),(50,0,0,6,0),(0,0,0,2,8)]),
        'Julie': (1200, 430, 380, 350, [(0,40,3,0,3),(40,0,0,5,0),(0,0,5,4,10),(0,0,0,0,0)])}
WEAP = [(1,70),(10,152),(25,225),(40,307),(55,400),(65,495),(75,657)]
ARM = {'hat': [(1,40),(10,95),(25,180),(40,270),(55,342),(70,425)], 'body': [(1,62),(10,122),(25,217),(40,295),(55,397),(70,480)],
       'boot': [(1,25),(15,60),(30,115),(50,190),(60,270),(70,375)]}
NECK = [(1,100),(15,170),(30,255),(50,345),(60,455),(70,625)]
RING = [(1,25),(15,60),(30,100),(50,150),(60,225),(70,277)]
def best(t, L): return max([v for (lv, v) in t if lv <= L] or [0])
def hero(name, L, gear=True):
    hp, atk, de, spd, g = HERO[name]
    pts = 10 * (L - 1) / 4.0
    for k in range(4):
        if g[k] == (0,0,0,0,0): continue
        hp += pts * g[k][0]; atk += pts * g[k][2]; de += pts * g[k][3]; spd += pts * g[k][4]
    if gear:
        atk += best(WEAP, L) + best(RING, L); de += sum(best(ARM[k], L) for k in ARM); hp += best(NECK, L)
    return hp, atk, de, spd
def dmg(atk, de): return max(10, atk * 2 - de + 2.5)

# --- session 8 scaling, mirror of src8/EnemyAI.java (keep in sync) ---
CFG = dict(hpmul=115, atkmul=110, defmul=100, spdmul=105, rounds=22, bossrounds=35, loss=12, bossloss=16, spdfloor=70, defcap=45)
def scaled(m, h, boss=False, factor=(1, 1, 1, 1)):
    hp, atk, de, spd = m; hhp, hatk, hde, hspd = h
    hp = hp * CFG['hpmul'] / 100; atk = atk * CFG['atkmul'] / 100; de = de * CFG['defmul'] / 100; spd = spd * CFG['spdmul'] / 100
    de = min(de, hatk * 2 * CFG['defcap'] / 100.0)          # không để quái miễn nhiễm
    pct = CFG['bosshurt'] if boss else CFG['hurt']
    atk = max(atk, (hde + hhp * pct * factor[1] / 100.0) / 2)
    n = CFG['bosshits'] if boss else CFG['hits']
    hp = max(hp, n * factor[0] * dmg(hatk, de))
    spd = max(spd, hspd * CFG['spdfloor'] * factor[3] / 100.0)
    return hp, atk, de, spd

if __name__ == '__main__':
    new = '--new' in sys.argv
    print('L  | hero Q (hp/atk/def/spd)      | tier | quái hp/atk/def/spd           | đòn hero để hạ | %HP hero/đòn quái')
    for L in (1, 5, 10, 20, 30, 45, 60, 75, 90):
        h = hero('Quinn', L); j = hero('Julie', L)
        avg = tuple((a + b) / 2 for a, b in zip(h, j))
        for tier in (1, 3, 5, 8, 10):
            m = mon(tier, L)
            if new: m = scaled(m, avg)
            hits = m[0] / dmg(avg[1], m[2]); hurt = 100.0 * dmg(m[1], avg[2]) / avg[0]
            print('%2d | %5d/%4d/%4d/%3d | %2d | %6d/%5d/%5d/%4d | %5.1f | %5.1f%%' % (L, avg[0], avg[1], avg[2], avg[3], tier, m[0], m[1], m[2], m[3], hits, hurt))

# ---------------------------------------------------------------------------------------------------------------
# Monte-Carlo fight (--fight): Quinn + Julie (đánh thường vào quái máu thấp nhất, 5% chí mạng — người chơi "thận trọng"
# không dùng kỹ năng/vật phẩm = ca khó nhất) vs N quái đã nâng (AI: tập trung một mục tiêu, dùng kỹ năng đơn ×SKILLX
# khi còn MP, đòn hợp kích ×1.1). Đo: tỉ lệ thắng, số vòng, % HP đội mất, tỉ lệ có hero gục.
import random
def fight(L, n_enemy, boss, tier=5, skillx=1.35, mp_casts=4, trials=400, cfg=None, seed=1):
    if cfg: CFG.update(cfg)
    rnd = random.Random(seed)
    heroes0 = [hero('Quinn', L), hero('Julie', L)]
    sumhp = sum(h[0] for h in heroes0); avg = tuple(sum(h[i] for h in heroes0) / 2 for i in range(4))
    wins = rounds_t = lost_t = downs = 0
    for _ in range(trials):
        m = mon(tier, L)
        # floors from EnemyAI (party-relative)
        hp, atk, de, spd = m[0] * CFG['hpmul'] / 100, m[1] * CFG['atkmul'] / 100, m[2] * CFG['defmul'] / 100, m[3] * CFG['spdmul'] / 100
        de = min(de, avg[1] * 2 * CFG['defcap'] / 100)
        perhit = sumhp * (CFG['bossloss'] if boss else CFG['loss']) / 100 / n_enemy
        atk = max(atk, (avg[2] + perhit) * 100 / (2 * CFG.get('skilltyp', 130)))
        pdmg = sum(max(10, h[1] * 2 - de) for h in heroes0)
        hp = max(hp, pdmg * (CFG['bossrounds'] if boss else CFG['rounds']) / 10 / n_enemy)
        spd = max(spd, avg[3] * CFG['spdfloor'] / 100)
        H = [[h[0], h[0], h[1], h[2], h[3]] for h in heroes0]          # hp, max, atk, def, spd
        E = [[hp, atk, de, spd, mp_casts] for _ in range(n_enemy)]
        r = 0
        while r < 40:
            r += 1
            order = [('h', i) for i in range(2) if H[i][0] > 0] + [('e', i) for i in range(n_enemy) if E[i][0] > 0]
            order.sort(key=lambda x: -(H[x[1]][4] if x[0] == 'h' else E[x[1]][3]))
            # monsters focus the hero they can drop fastest (leader slot 0 bonus)
            for side, i in order:
                if side == 'h':
                    if H[i][0] <= 0: continue
                    al = [k for k in range(n_enemy) if E[k][0] > 0]
                    if not al: break
                    k = min(al, key=lambda k: E[k][0])
                    a = H[i][2] * (2 if rnd.random() < 0.05 else 1)
                    E[k][0] -= max(10, a * 2 - E[k][2] + rnd.randint(0, 5))
                else:
                    if E[i][0] <= 0: continue
                    al = [k for k in range(2) if H[k][0] > 0]
                    if not al: break
                    t = min(al, key=lambda k: H[k][0] - (200 if k == 0 else 0))
                    mult = 1.0
                    if E[i][4] > 0: mult = skillx; E[i][4] -= 1
                    elif sum(1 for x in E if x[0] > 0) > 1: mult = 1.1      # combo
                    H[t][0] -= max(10, E[i][1] * 2 * mult - H[t][3] + rnd.randint(0, 5))
            if H[0][0] <= 0 or all(h[0] <= 0 for h in H): break
            if all(e[0] <= 0 for e in E): wins += 1; break
        rounds_t += r
        lost_t += sum(min(h[1], h[1] - max(0, h[0])) for h in H) / sumhp
        downs += any(h[0] <= 0 for h in H)
    return wins / trials, rounds_t / trials, 100 * lost_t / trials, downs / trials

if __name__ == '__main__' and '--fight' in sys.argv:
    print('\nL  n boss | thắng | vòng | %HP đội mất | có hero gục')
    for L in (1, 10, 30, 60, 90):
        for n, boss in ((2, False), (3, False), (3, True)):
            w, r, lost, d = fight(L, n, boss)
            print('%2d %d %s | %4.0f%% | %4.1f | %5.1f%% | %4.0f%%' % (L, n, 'B' if boss else '-', w * 100, r, lost, d * 100))
