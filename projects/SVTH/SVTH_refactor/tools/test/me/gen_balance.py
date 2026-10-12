#!/usr/bin/env python3
"""gen_balance.py <out.txt> <level> <n_battles> <scene_enemy_ids> <count_range> <level_range> [solo]
Sinh kịch bản MeRun (phiên 8) đo cân bằng: vào game, (Julie vào đội — cần bản debug có World.dbg$join), đặt chỉ số hero theo mô hình
balance_sim.hero(L) (10 điểm/cấp chia đều + trang bị theo cấp), đặt bảng quái của cảnh, rồi N trận gặp ngẫu nhiên; trước mỗi trận hồi đầy HP/MP.
Mỗi trận in một dòng 'MERUN battle ... result=win|lose rounds= minHpPct0= minHpPct1= foes=role:lv ...'."""
import sys, os
sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'sim'))
import balance_sim as bs
out, L, n, ids, cnt, lvr = sys.argv[1], int(sys.argv[2]), int(sys.argv[3]), sys.argv[4], sys.argv[5], sys.argv[6]
solo = len(sys.argv) > 7 and sys.argv[7] == 'solo'
s = ['wait 3000', 'show', 'wait 1500', 'key -5', 'wait 3000', 'key -7', 'wait 5000', 'key -5', 'wait 14000', 'key -7', 'wait 3000']
heroes = [('Quinn', 0)] + ([] if solo else [('Julie', 1)])
s += ['sets GameData sceneEnemyIds = S:' + ids, 'sets GameData sceneEnemyCountRange = B:' + cnt, 'sets GameData sceneEnemyLevelRange = s:' + lvr]
for b in range(n):
    # the game keeps only the leader in GameData.party after a battle (keepLeaderOnly); the debug join is not persistent -> re-join
    if not solo: s += ['calls World dbg$join i:2', 'wait 300']
    for name, i in heroes:
        hp, atk, de, spd = (int(x) for x in bs.hero(name, L))
        p = 'GameData party[%d] stats' % i
        for k, v in ((0, L), (4, hp), (10, hp), (3, hp), (7, atk), (12, atk), (8, de), (13, de), (9, spd), (14, spd), (5, 5000), (6, 5000), (11, 5000)):
            s.append('sets %s[%d] = i:%d' % (p, k, v))
    s += ['callm GameCanvas instance:GameCanvas world:World -- triggerRandomEncounter z:false', 'wait 2500', 'shot b%d_start.png' % (b + 1),
          'battle 180000 L%d#%d' % (L, b + 1), 'shot b%d_end.png' % (b + 1)]
s += ['say DONE', 'quit']
open(out, 'w').write('\n'.join(s) + '\n')
