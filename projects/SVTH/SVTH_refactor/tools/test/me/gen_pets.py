#!/usr/bin/env python3
"""gen_pets.py <out.txt> — kịch bản MeRun kiểm AI sủng vật (phiên 8): Quinn cấp 10 + 2 sủng vật xuất chiến
(uid 1 = Mộc Tinh Phong petId 9, có kỹ năng hồi máu 13; uid 2 = Sói Phong petId 1, không có hồi máu). Không cần bản debug.
Trận 1 đủ máu; trận 2 Quinn 25 % HP (pet hồi máu phải cứu); trận 3-4 Sói 15 % HP (thủ tới chết hoặc rút lui); trận 5 cả hai pet 15 %."""
import sys, os
sys.path.insert(0, os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', '..', 'sim'))
import balance_sim as bs
out = sys.argv[1]
L = 10
s = ['wait 3000', 'show', 'wait 1500', 'key -5', 'wait 3000', 'key -7', 'wait 5000', 'key -5', 'wait 14000', 'key -7', 'wait 3000',
     'calls GameData addPet i:9 i:-1', 'calls GameData addPet i:1 i:-1']
hp, atk, de, spd = (int(x) for x in bs.hero('Quinn', L))
php, patk, pde, pspd = int(hp * 0.7), int(atk * 0.7), int(de * 0.7), int(spd * 0.9)
for i in (0, 1):
    s += ['sets GameData myPets[%d] isDeployed = z:true' % i, 'sets GameData myPets[%d] isFainted = z:false' % i]
    for k, v in ((0, L), (2, php), (5, 3000), (4, 3000), (7, patk), (9, pde), (11, pspd)):
        s.append('sets GameData myPets[%d] stats[%d] = i:%d' % (i, k, v))
s += ['sets GameData sceneEnemyIds = S:5,6,9,10,41,42', 'sets GameData sceneEnemyCountRange = B:2,3', 'sets GameData sceneEnemyLevelRange = s:8,11']
cases = [(100, 100, 100), (25, 100, 100), (100, 100, 15), (100, 100, 15), (100, 15, 15)]
for n, (qp, p0, p1) in enumerate(cases):
    for k, v in ((0, L), (4, hp), (10, hp), (3, hp * qp // 100), (7, atk), (12, atk), (8, de), (13, de), (9, spd), (14, spd), (5, 5000), (6, 5000), (11, 5000)):
        s.append('sets GameData party[0] stats[%d] = i:%d' % (k, v))
    s += ['sets GameData myPets[0] stats[1] = i:%d' % (php * p0 // 100), 'sets GameData myPets[1] stats[1] = i:%d' % (php * p1 // 100),
          'sets GameData myPets[0] isFainted = z:false', 'sets GameData myPets[1] isFainted = z:false',
          'sets GameData myPets[0] isDeployed = z:true', 'sets GameData myPets[1] isDeployed = z:true',
          'say CASE %d quinn=%d%% healer=%d%% wolf=%d%%' % (n + 1, qp, p0, p1),
          'callm GameCanvas instance:GameCanvas world:World -- triggerRandomEncounter z:false', 'wait 4000', 'shot p%d_a.png' % (n + 1),
          'key -5', 'wait 700', 'key -5', 'wait 1500', 'shot p%d_b.png' % (n + 1),
          'battle 180000 P%d' % (n + 1), 'shot p%d_end.png' % (n + 1),
          'probe GameData myPets[0] stats[1]', 'probe GameData myPets[1] stats[1]', 'probe GameData myPets[1] isFainted']
s += ['say DONE', 'quit']
open(out, 'w').write('\n'.join(s) + '\n')
