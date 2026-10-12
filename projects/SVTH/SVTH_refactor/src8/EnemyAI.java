/*
 * session 8: stronger + smarter enemies; enemy level always tracks the heroes' average level.
 *
 * Hooks (inserted by tools/src/Patch8.java into the renamed game classes):
 *   Battle.createEnemyActors : before the first GameUtil.a(...) of each enemy -> adjustLevel(b, i)
 *                              (the game then derives HP/MP/ATK/DEF/SPD/EXP from the new level itself)
 *                              before every return                            -> onEnemiesCreated(b)
 *   Battle.tick              : right after the enemy planning loop of phase 2 -> planRound(b)
 *                              after every "putfield actingSlot"              -> onTurn(b)
 *
 * Everything is integer math (CLDC 1.0: no float/double); every entry point catches Throwable so a bug here
 * can never break a battle (the plan made by the original code is kept).
 * Tunables: /ai/config.txt inside the JAR (key=value, '#' comments).
 */
public final class EnemyAI {
    // ---- config (/ai/config.txt) ----
    static boolean enabled = true;   // master switch
    static boolean aiOn = true;      // smart action choice
    static boolean scaleOn = true;   // level tracking + stat floors
    static boolean jit = true;       // re-decide at the enemy's own turn with the current battle state
    static boolean pets = false;     // average level over heroes only (false) or heroes + deployed pets (true)
    static int lvMin = 0, lvMax = 2; // random encounters: level = avg + rand(lvMin..lvMax)
    static int bossLv = 3;           // scripted battles: level = avg + bossLv
    static boolean bossFloor = true; // scripted battles never go below the level the story designed
    static int lvCap = 200;
    static int hpMul = 115, atkMul = 100, defMul = 100, spdMul = 105; // % on top of the level-based stats
    static int skillTyp = 130;       // typical skill multiplier the ATK floor is sized for (skills scale 2*ATK before DEF is subtracted)
    // random skill sets (session 8, 2nd request): besides its natural skills every enemy gets a different random set each battle,
    // 1 + level/skillStep skills, at most skillMax random skills per enemy
    static boolean randSkill = true;
    static boolean randBoss = true;  // also story/dungeon enemies (false: only random encounters)
    static int skillStep = 10, skillMax = 7, skillLvStep = 20;
    static int rounds = 22, bossRounds = 35; // x10: party needs about this many rounds of normal attacks to clear the enemies
    static int loss = 12, bossLoss = 16;     // % of the party's total max HP the enemies can take per round (before AI bonuses)
    static int spdFloor = 70;        // % of the heroes' average speed
    static int defCap = 45;          // enemy DEF <= this % of 2x hero ATK (never immune to normal hits)
    static int noise = 10;           // % chance to pick the 2nd best action (less predictable)
    static int statCap = 999999;
    static boolean log = false;      // AILOG lines on stdout (also -Dai.log=1)

    static boolean loaded;

    // ---- per-round planning state (no allocation in the battle loop) ----
    private static final int[] projDmg = new int[10];   // damage already planned on hero slot 0..9 this round
    private static final boolean[] ccPlanned = new boolean[10];
    private static Battle lastScaled;

    private EnemyAI() { }  // instance only for getClass() (no class literals on CLDC 1.0)

    // =====================================================================================================
    // Level tracking + stats
    // =====================================================================================================

    /** Called for enemy i (slot 10+i) after the game set stats[0] (level) and before it derives the stats from it. */
    public static void adjustLevel(Battle b, int i) {
        try {
            if (!loaded) load();
            if (!enabled || !scaleOn || excluded()) return;
            BattleActor e = b.actors[10 + i];
            if (e == null || e.stats == null) return;
            int avg = partyLevel(b);
            if (avg <= 0) return;
            int orig = e.stats[0];
            int lv;
            if (b.enemyIds != null) {           // scripted: story / dungeon / boss
                lv = avg + bossLv;
                if (bossFloor && orig > lv) lv = orig;
            } else {
                lv = avg + GameUtil.randomBetween(lvMin, lvMax);
            }
            if (lv < 1) lv = 1;
            if (lv > lvCap) lv = lvCap;
            e.stats[0] = lv;
            if (log) System.out.println("AILOG level slot=" + (10 + i) + " role=" + e.roleId + " orig=" + orig + " avg=" + avg + " -> " + lv);
        } catch (Throwable t) {
            if (log) System.out.println("AILOG error adjustLevel " + t);
        }
    }

    /** End of Battle.createEnemyActors: multipliers + floors relative to the actual party. */
    public static void onEnemiesCreated(Battle b) {
        try {
            if (!loaded) load();
            if (!enabled || excluded() || b == lastScaled) return;
            lastScaled = b;
            BattleActor[] a = b.actors;
            if (randSkill && (randBoss || b.enemyIds == null)) {
                for (int k = 0; k < b.enemyCount && 10 + k < a.length; k++) {
                    BattleActor e = a[10 + k];
                    if (e == null || e.stats == null) continue;
                    assignSkills(e, e.stats[0] < 1 ? 1 : e.stats[0]);
                    e.attack = e.stats[12] + elementPassiveBonus(e);   // new 29-32 passives
                }
            }
            if (!scaleOn) return;
            int n = 0;
            long sumHp = 0, sumAtk = 0, sumDef = 0, sumSpd = 0;
            for (int s = 0; s < 10; s++) {
                BattleActor h = a[s];
                if (h == null || h.isDead || h.stats == null) continue;
                n++;
                sumHp += maxHp(h);
                sumAtk += h.stats[12];
                sumDef += h.stats[13];
                sumSpd += h.stats[14];
            }
            if (n == 0) return;
            int cnt = b.enemyCount;
            if (cnt < 1) cnt = 1;
            boolean boss = b.enemyIds != null;
            int avgAtk = (int) (sumAtk / n), avgDef = (int) (sumDef / n), avgSpd = (int) (sumSpd / n);
            for (int k = 0; k < b.enemyCount && 10 + k < a.length; k++) {
                BattleActor e = a[10 + k];
                if (e == null || e.stats == null) continue;
                int lv = e.stats[0] < 1 ? 1 : e.stats[0];
                long hp0 = e.stats[3], atk0 = e.stats[12], def0 = e.stats[13], spd0 = e.stats[14];
                long hp = hp0 * hpMul / 100, atk = atk0 * atkMul / 100, def = def0 * defMul / 100, spd = spd0 * spdMul / 100;
                // species personality: compare with a mid-tier (stardata row 5) enemy of the same level
                long fHp = clamp(hp0 * 100 / (295 + 106L * (lv - 1)), 75, 135);
                long fAtk = clamp(atk0 * 100 / (110 + 20L * (lv - 1)), 75, 135);
                long fSpd = clamp(spd0 * 100 / (85 + 15L * (lv - 1)), 75, 135);
                long defMax = (long) avgAtk * 2 * defCap / 100;
                if (def > defMax) def = defMax;
                // ATK floor: one hit takes about (loss% of the party's total HP) / enemyCount from an average hero
                long perHit = sumHp * (boss ? bossLoss : loss) / 100 / cnt;
                long atkFloor = (avgDef + perHit) * 100 / (2L * skillTyp) * fAtk / 100;
                if (atk < atkFloor) atk = atkFloor;
                // HP floor: the party needs about `rounds` rounds of normal attacks to clear all enemies
                long partyDmg = 0;
                for (int s = 0; s < 10; s++) {
                    BattleActor h = a[s];
                    if (h == null || h.isDead || h.stats == null) continue;
                    long d = (long) h.stats[12] * 2 - def;
                    partyDmg += d < 10 ? 10 : d;
                }
                long hpFloor = partyDmg * (boss ? bossRounds : rounds) / 10 / cnt * fHp / 100;
                if (hp < hpFloor) hp = hpFloor;
                long spdMin = (long) avgSpd * spdFloor / 100 * fSpd / 100;
                if (spd < spdMin) spd = spdMin;
                // never weaker than the game made it
                if (hp < hp0) hp = hp0;
                if (atk < atk0) atk = atk0;
                if (spd < spd0) spd = spd0;
                int ihp = (int) clamp(hp, 1, statCap), iatk = (int) clamp(atk, 1, statCap);
                int idef = (int) clamp(def, 0, statCap), ispd = (int) clamp(spd, 1, statCap);
                e.stats[3] = ihp;
                e.stats[10] = ihp;
                e.hp = ihp;
                e.stats[12] = iatk;
                e.stats[13] = idef;
                e.stats[14] = ispd;
                e.attack = iatk + elementPassiveBonus(e);
                e.defense = idef;
                e.speed = ispd;
                if (log) System.out.println("AILOG stats slot=" + (10 + k) + " lv=" + lv + (boss ? " boss" : "") + " hp " + hp0 + "->" + ihp
                        + " atk " + atk0 + "->" + iatk + " def " + def0 + "->" + idef + " spd " + spd0 + "->" + ispd
                        + " party n=" + n + " hp=" + sumHp + " atk=" + avgAtk + " def=" + avgDef + " spd=" + avgSpd);
            }
        } catch (Throwable t) {
            if (log) System.out.println("AILOG error onEnemiesCreated " + t);
        }
    }

    /** Usable by enemies: every active skill except scout (39) and silence (16: low levels also silence the caster's side),
     *  plus the passives that work on enemies (7 counter, 29-32 element ATK bonus). */
    private static final short[] POOL = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            28, 33, 34, 7, 29, 30, 31, 32};

    /** Adds 1 + lv/skillStep (<= skillMax) random skills the enemy doesn't have yet; level 1 + lv/skillLvStep (<= the skill's max). */
    static void assignSkills(BattleActor e, int lv) {
        int want = 1 + lv / skillStep;
        if (want > skillMax) want = skillMax;
        if (Skill.ids == null) Skill.loadTable();
        int added = 0, tries = 0;
        StringBuffer sb = log ? new StringBuffer() : null;
        while (added < want && tries++ < 200) {
            int id = POOL[GameUtil.randomRange(0, POOL.length)];
            if (has(e.skillIds, id)) continue;
            int idx = GameData.findSkillIndex(id);
            if (idx < 0) continue;
            short[] costs = Skill.mpCostsPerLevel[idx];
            boolean passive = Skill.targetSides[idx] == 3;
            if (passive) {
                e.skillIds = append(e.skillIds, id);
                if (id == 7) e.hasCounter = true;
            } else {
                int maxLv = 0;                       // levels with a real MP cost (skills 9-12 list 0 after level 5)
                while (maxLv < costs.length && costs[maxLv] > 0) maxLv++;
                if (id == 33 && maxLv > 5) maxLv = 5; // applySkill's poison table has 5 rows
                if (maxLv == 0) continue;
                int sl = 1 + lv / skillLvStep;
                if (sl > maxLv) sl = maxLv;
                e.skillIds = append(e.skillIds, id);
                e.activeSkillIds = append(e.activeSkillIds, id);
                e.skillLevels = append(e.skillLevels, sl);
                if (sb != null) sb.append(' ').append(id).append(':').append(sl);
                added++;
                continue;
            }
            if (sb != null) sb.append(' ').append(id).append(":p");
            added++;
        }
        if (sb != null) System.out.println("AILOG skills slot=" + e.slot + " role=" + e.roleId + " lv=" + lv + " +" + added + sb.toString());
    }

    private static boolean has(short[] a, int v) {
        if (a == null) return false;
        for (int i = 0; i < a.length; i++) if (a[i] == v) return true;
        return false;
    }

    private static short[] append(short[] a, int v) {
        int n = a == null ? 0 : a.length;
        short[] r = new short[n + 1];
        if (n > 0) System.arraycopy(a, 0, r, 0, n);
        r[n] = (short) v;
        return r;
    }

    /** Average level of the heroes in the battle (slots 0..9; pets have partyUid != 0). */
    static int partyLevel(Battle b) {
        int n = 0, sum = 0, all = 0, sumAll = 0;
        for (int s = 0; s < 10; s++) {
            BattleActor h = b.actors[s];
            if (h == null || h.stats == null) continue;
            all++;
            sumAll += h.stats[0];
            if (h.partyUid == 0) { n++; sum += h.stats[0]; }
        }
        if (pets || n == 0) { n = all; sum = sumAll; }
        return n == 0 ? 0 : (sum + n / 2) / n;
    }

    /** Same formula as Battle.elementPassiveBonus (private there). */
    static int elementPassiveBonus(BattleActor e) {
        int r = 0;
        if (e.skillIds == null || e.elements == null) return 0;
        for (int i = 0; i < e.skillIds.length; i++) {
            int id = e.skillIds[i];
            if (id == 29 && e.elements[0] > 0) r += e.elements[0] * e.stats[12] / 10;
            if (id == 30 && e.elements[3] > 0) r += e.elements[3] * e.stats[12] / 10;
            if (id == 31 && e.elements[2] > 0) r += e.elements[2] * e.stats[12] / 10;
            if (id == 32 && e.elements[1] > 0) r += e.elements[1] * e.stats[12] / 10;
        }
        return r;
    }

    static boolean excluded() {
        GameCanvas gc = GameCanvas.instance;
        if (gc == null || gc.world == null) return false;
        if (gc.world.isTutorialBattle) return true;              // 教学战斗: scripted tutorial, keep it as designed
        return gc.world.script != null && gc.world.script.isMustLoseBattle; // 必败战斗: the story expects a loss
    }

    // =====================================================================================================
    // Action choice
    // =====================================================================================================

    /** Phase 2, after the original enemy planning loop: replace each enemy's plan by the best action. */
    public static void planRound(Battle b) {
        try {
            if (!loaded) load();
            if (!enabled || !aiOn || excluded()) return;
            for (int i = 0; i < 10; i++) { projDmg[i] = 0; ccPlanned[i] = false; }
            int end = b.actionPlans.length < 20 ? b.actionPlans.length : 20;
            for (int s = 10; s < end; s++) decide(b, s, true);
        } catch (Throwable t) {
            if (log) System.out.println("AILOG error planRound " + t);
        }
    }

    /** After actingSlot changes: an enemy about to act re-decides with the current HP/status of everyone. */
    public static void onTurn(Battle b) {
        try {
            if (!enabled || !aiOn || !jit || !loaded || excluded()) return;
            int s = b.actingSlot;
            if (s < 10 || s >= 20 || s >= b.actionPlans.length) return;
            int cmd = b.actionPlans[s][1];
            if (cmd != 1 && cmd != 4) return;
            for (int i = 0; i < 10; i++) { projDmg[i] = 0; ccPlanned[i] = false; }
            decide(b, s, false);
        } catch (Throwable t) {
            if (log) System.out.println("AILOG error onTurn " + t);
        }
    }

    // candidate encoding: cmd (1 attack, 2 defend, 4 skill), target, skillId, level, value
    private static int bCmd, bTgt, bSkill, bLv, bVal, bDmg, sCmd, sTgt, sSkill, sLv, sVal, sDmg;
    private static int lastD;   // expected single-target damage of the last singleSkillValue() call

    private static void offer(int cmd, int tgt, int skill, int lv, int val) { offer(cmd, tgt, skill, lv, val, 0); }

    private static void offer(int cmd, int tgt, int skill, int lv, int val, int dmg) {
        if (val > bVal) {
            sCmd = bCmd; sTgt = bTgt; sSkill = bSkill; sLv = bLv; sVal = bVal; sDmg = bDmg;
            bCmd = cmd; bTgt = tgt; bSkill = skill; bLv = lv; bVal = val; bDmg = dmg;
        } else if (val > sVal) {
            sCmd = cmd; sTgt = tgt; sSkill = skill; sLv = lv; sVal = val; sDmg = dmg;
        }
    }

    static void decide(Battle b, int s, boolean roundStart) {
        BattleActor[] a = b.actors;
        BattleActor me = a[s];
        if (me == null || me.isDead || me.state == 128 || me.stats == null) return;
        if (me.stunTurns > 0 || me.confuseTurns > 0) return;     // the game handles these (skip / random target)
        int[] plan = b.actionPlans[s];
        bCmd = sCmd = 0; bVal = sVal = Integer.MIN_VALUE; bDmg = sDmg = 0;
        int maxFoeAtk = 1;
        for (int t = 0; t < 10; t++) if (alive(a[t]) && a[t].attack > maxFoeAtk) maxFoeAtk = a[t].attack;

        // 1. normal attack on each hero
        for (int t = 0; t < 10; t++) {
            BattleActor f = a[t];
            if (!alive(f)) continue;
            int d = normalDamage(b, me, f, roundStart) * hitPct(me, f, 0) / 100;
            int v = damageValue(b, f, d, maxFoeAtk);
            if (comboPartner(b, s, t)) v += 150;            // same target + similar speed -> the game's combo attack (x1.1, joined hits)
            if (f.hasCounter) v -= 40;
            offer(1, t, -1, -1, v, d);
        }

        // 2. skills
        if (me.silenceTurns == 0 && me.activeSkillIds != null && me.skillLevels != null) {
            if (Skill.ids == null) Skill.loadTable();
            for (int i = 0; i < me.activeSkillIds.length && i < me.skillLevels.length; i++) {
                int id = me.activeSkillIds[i];
                int idx = GameData.findSkillIndex(id);
                if (idx < 0) continue;
                int lv = affordable(me, idx, me.skillLevels[i], id);
                if (lv <= 0) continue;
                int cost = Skill.mpCostsPerLevel[idx][lv - 1];
                int mpPenalty = cost * 25 / (me.stats[5] > 0 ? me.stats[5] : 1);
                evalSkill(b, s, me, id, idx, lv, maxFoeAtk, roundStart, mpPenalty);
            }
        }

        // 3. defend (decided only at round start: the game applies it in phase 2)
        if (roundStart) {
            int pct = me.hp * 100 / maxHp(me);
            if (pct < 30 && GameUtil.randomRange(0, 100) < 50) offer(2, s, -1, -1, 220 + (30 - pct) * 6);
        }

        if (bCmd == 0) return;
        boolean second = sCmd != 0 && sVal > bVal / 2 && GameUtil.randomRange(0, 100) < noise;
        int cmd = second ? sCmd : bCmd, tgt = second ? sTgt : bTgt, skill = second ? sSkill : bSkill, lv = second ? sLv : bLv;
        int dmg = second ? sDmg : bDmg;
        if (!roundStart && cmd == 2) return;
        plan[0] = s;
        plan[1] = cmd;
        if (cmd == 1) {
            plan[2] = tgt;
            if (roundStart) projDmg[tgt] += dmg;      // later enemies see this hero as already hurt (focus / no overkill)
        } else if (cmd == 4) {
            plan[2] = tgt;
            plan[3] = skill;
            plan[4] = lv;
            if (roundStart) {
                if (tgt >= 0 && tgt < 10) projDmg[tgt] += dmg;
                if (skill == 1 || skill == 11 || skill == 34) ccPlanned[tgt] = true;   // don't stack crowd control on one hero
                else if (skill == 10 || skill == 12) for (int t = 0; t < 10; t++) ccPlanned[t] = true;
            }
        }
        if (log) System.out.println("AILOG plan slot=" + s + (roundStart ? " round" : " turn") + " cmd=" + cmd + " tgt=" + tgt + " skill=" + skill
                + " lv=" + lv + " val=" + (second ? sVal : bVal) + (second ? " (2nd)" : "") + " hp=" + me.hp + "/" + maxHp(me) + " mp=" + me.mp);
    }

    private static void evalSkill(Battle b, int s, BattleActor me, int id, int idx, int lv, int maxFoeAtk, boolean roundStart, int mpPenalty) {
        BattleActor[] a = b.actors;
        int side = Skill.targetSides[idx], scope = Skill.targetScopes[idx];
        int firstFoe = -1;
        for (int t = 0; t < 10; t++) if (alive(a[t])) { firstFoe = t; break; }
        if (side == 2) {
            if (firstFoe < 0) return;
            if (scope == 1) {
                for (int t = 0; t < 10; t++) {
                    BattleActor f = a[t];
                    if (!alive(f)) continue;
                    lastD = 0;
                    int v = singleSkillValue(b, me, f, id, lv, maxFoeAtk, roundStart);
                    if (v > Integer.MIN_VALUE / 2) offer(4, t, id, lv, v - mpPenalty, lastD);
                }
            } else {
                int v = 0;
                for (int t = 0; t < 10; t++) {
                    BattleActor f = a[t];
                    if (!alive(f)) continue;
                    v += aoeSkillValue(b, me, f, id, lv, maxFoeAtk, roundStart);
                }
                if (id == 16 && lv < 6) {       // low-level silence also silences our own side
                    for (int t = 10; t < 20 && t < a.length; t++) if (t != s && alive(a[t]) && a[t].activeSkillIds != null) v -= 150;
                }
                offer(4, firstFoe, id, lv, v - mpPenalty);
            }
        } else {
            // own side: heal / cleanse / buffs
            if (id == 13) {
                for (int t = 10; t < 20 && t < a.length; t++) {
                    BattleActor x = a[t];
                    if (!alive(x)) continue;
                    offer(4, t, id, lv, healValue(x, healAmount(me, x, lv)) - mpPenalty);
                }
            } else if (id == 14) {
                int v = 0;
                for (int t = 10; t < 20 && t < a.length; t++) if (alive(a[t])) v += healValue(a[t], healAmount(me, a[t], lv));
                offer(4, s, id, lv, v - mpPenalty);
            } else if (id == 15) {
                int v = 0;
                for (int t = 10; t < 20 && t < a.length; t++) if (alive(a[t]) && negative(a[t])) v += 250;
                if (v > 0) offer(4, s, id, lv, v - mpPenalty);
            } else if (id == 2) {
                if (roundStart && me.attackBuff != null && me.attackBuff[0] == 0) offer(4, s, id, lv, 90 + lv * 10 - mpPenalty);
            } else if (id == 17) {
                int v = 0;
                for (int t = 10; t < 20 && t < a.length; t++) if (alive(a[t]) && a[t].rallyBuff != null && a[t].rallyBuff[0] == 0) v += 60 + lv * 4;
                if (v > 0 && roundStart) offer(4, s, id, lv, v - mpPenalty);
            }
        }
    }

    /** Value of a single-target offensive skill on hero f; MIN_VALUE/2 = not usable/unknown. */
    private static int singleSkillValue(Battle b, BattleActor me, BattleActor f, int id, int lv, int maxFoeAtk, boolean rs) {
        int atk = me.attack, adv = elementAdvantage(me.elements, f.elements);
        int d;
        int extra = 0;
        switch (id) {
            case 1:  d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100; extra = ccValue(f, 260, lv); break;      // hit + stun
            case 34: d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100; extra = ccValue(f, 200, lv); break;     // hit + confuse
            case 33: d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100; extra = poisonValue(f, 3, 6 + lv); break; // hit + poison
            case 28: {                                                                                                // drain
                d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100;
                int heal = d * (48 + 2 * lv) / 100;
                extra = healValue(me, heal) / 2;
                break;
            }
            case 4: {                                                                                                 // multi-hit
                int hits = 2 + (lv - 1) / 4;
                d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100 * hits;
                break;
            }
            case 5:  d = mitigate(b, f, (atk * (110 + lv * 10) / 100) * (200 + adv) / 100 - f.defense, rs) * hitPct(me, f, -20) / 100; break;
            case 6:  d = mitigate(b, f, atk * (200 + adv) / 100, rs); extra = -60; break;                              // ignores DEF, lowers own DEF
            case 8:  d = mitigate(b, f, (atk * (140 + lv * 10) / 100) * (200 + adv) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100;
                     extra = 90; break;                                                                               // + 15% instant kill
            case 18: d = mitigate(b, f, atk * (125 + lv * 5) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100; break;
            case 20: case 22: case 24: case 26: {
                int e = elemAdv(id, f.elements);
                d = mitigate(b, f, (atk * (115 + lv * 5) / 100) * (200 + e) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100;
                break;
            }
            case 11: return negative(f) || ccPlanned[f.slot] ? Integer.MIN_VALUE / 2 : ccValue(f, 300, lv) * hitPct(me, f, 0) / 100 + threatBonus(f, maxFoeAtk);
            case 39: return Integer.MIN_VALUE / 2;
            default: d = normalDamage(b, me, f, rs) * hitPct(me, f, 0) / 100; break;
        }
        lastD = d;
        return damageValue(b, f, d, maxFoeAtk) + extra;
    }

    private static int aoeSkillValue(Battle b, BattleActor me, BattleActor f, int id, int lv, int maxFoeAtk, boolean rs) {
        int atk = me.attack, adv = elementAdvantage(me.elements, f.elements);
        switch (id) {
            case 3:  return damageValue(b, f, mitigate(b, f, (atk * (62 + (lv << 3)) / 100) * (200 + adv) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100, maxFoeAtk);
            case 19: return damageValue(b, f, mitigate(b, f, atk * (145 + lv * 5) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100, maxFoeAtk);
            case 21: case 23: case 25: case 27: {
                int e = elemAdv(id, f.elements);
                int pct = id == 27 ? 145 : 115;
                return damageValue(b, f, mitigate(b, f, (atk * (pct + lv * 5) / 100) * (200 + e) / 100 - f.defense, rs) * hitPct(me, f, 0) / 100, maxFoeAtk);
            }
            case 9:  return poisonValue(f, 2 + lv / 2, lv + 4) * hitPct(me, f, 0) / 100;
            case 10: return negative(f) ? 0 : ccValue(f, 200, lv) * hitPct(me, f, 0) / 100;
            case 12: return negative(f) ? 0 : ccValue(f, 300, lv) * hitPct(me, f, 0) / 100;
            case 16: return f.silenceTurns == 0 && f.activeSkillIds != null ? 150 : 0;
            default: return 0;
        }
    }

    // ---- value helpers (units: 1000 = a whole max-HP bar) ----

    private static int damageValue(Battle b, BattleActor f, int d, int maxFoeAtk) {
        int mhp = maxHp(f);
        int left = f.hp - projDmg[f.slot];
        if (left < 1) left = 1;
        int eff = d < left ? d : left;
        int v = (int) ((long) eff * 1000 / mhp);
        if (d >= left) v += f.slot == 0 ? 1100 : 600;      // kill; the leader (slot 0) dying ends the battle
        v = v * (80 + 40 * f.attack / maxFoeAtk) / 100;      // hit the hardest hitters first
        if (f.slot == 0) v = v * 115 / 100;
        if (isHealer(f)) v = v * 120 / 100;
        return v;
    }

    private static int threatBonus(BattleActor f, int maxFoeAtk) {
        return 40 * f.attack / maxFoeAtk + (isHealer(f) ? 60 : 0);
    }

    private static int ccValue(BattleActor f, int base, int lv) {
        if (negative(f) || ccPlanned[f.slot]) return 0;
        return base + lv * 10;
    }

    private static int poisonValue(BattleActor f, int turns, int pct) {
        if (negative(f)) return 0;
        return turns * pct * 10;
    }

    private static int healAmount(BattleActor caster, BattleActor x, int lv) {
        return maxHp(x) * (10 + lv * 5) / 100 + caster.attack * (20 + lv * 2) / 100;
    }

    private static int healValue(BattleActor x, int heal) {
        int mhp = maxHp(x);
        int missing = mhp - x.hp;
        if (missing <= 0) return 0;
        int eff = heal < missing ? heal : missing;
        int v = (int) ((long) eff * 1000 / mhp);
        int pct = x.hp * 100 / mhp;
        if (pct < 50) v = v * 160 / 100; else v = v * 70 / 100;
        if (pct < 25) v += 250;
        return v;
    }

    // ---- damage model (mirrors Battle.dealDamage / rollHit / applySkill) ----

    private static int normalDamage(Battle b, BattleActor me, BattleActor f, boolean rs) {
        return mitigate(b, f, me.attack * (200 + elementAdvantage(me.elements, f.elements)) / 100 - f.defense + 2, rs);
    }

    private static int mitigate(Battle b, BattleActor f, int d, boolean rs) {
        if (f.isDefending || (rs && b.actionPlans[f.slot][1] == 2)) d /= 5;
        if (f.halfDamage) d /= 2;
        return d < 10 ? 10 : d;
    }

    private static int hitPct(BattleActor me, BattleActor f, int bonus) {
        int sa = me.speed, sd = f.speed;
        int p = (sa + sd > 0 ? sa * 100 / (sa + sd) : 50) + sa + bonus + 20;
        if (p < 20) p = 20;
        return p > 100 ? 100 : p;
    }

    private static int elemAdv(int skillId, byte[] target) {
        int k;
        switch (skillId) {
            case 22: case 23: k = 0; break;   // Địa
            case 26: case 27: k = 1; break;   // Thủy
            case 24: case 25: k = 2; break;   // Hỏa
            default: k = 3; break;            // Phong (20, 21)
        }
        int next = (k + 1) & 3, prev = (k + 3) & 3;
        int r = 0;
        if (target[next] > 0) r += 10 * target[next];
        if (target[prev] > 0) r -= 10 * target[prev];
        return r;
    }

    /** Battle.elementAdvantage: element k beats k+1, loses to k-1 (mod 4). */
    static int elementAdvantage(byte[] x, byte[] y) {
        if (x == null || y == null) return 0;
        int r = 0;
        for (int k = 0; k < 4; k++) {
            if (x[k] <= 0) continue;
            int next = (k + 1) & 3, prev = (k + 3) & 3;
            if (y[next] > 0) r += x[k] * y[next];
            if (y[prev] > 0) r -= x[k] * y[prev];
        }
        return r;
    }

    private static boolean comboPartner(Battle b, int s, int t) {
        BattleActor me = b.actors[s];
        for (int o = 10; o < 20 && o < b.actors.length; o++) {
            if (o == s) continue;
            BattleActor x = b.actors[o];
            if (!alive(x) || x.stunTurns > 0 || x.confuseTurns > 0) continue;
            int[] p = b.actionPlans[o];
            if (p[1] == 1 && p[2] == t && x.speed >= me.speed - 150 && x.speed <= me.speed + 150) return true;
        }
        return false;
    }

    private static int affordable(BattleActor me, int idx, int maxLv, int id) {
        short[] costs = Skill.mpCostsPerLevel[idx];
        int lv = 0;
        for (int l = 0; l < maxLv && l < costs.length; l++) if (costs[l] <= me.mp) lv = l + 1;
        if (id == 33 && lv > 5) lv = 5;   // applySkill's poison table has 5 rows
        return lv;
    }

    private static boolean isHealer(BattleActor f) {
        short[] k = f.activeSkillIds;
        if (k == null) return false;
        for (int i = 0; i < k.length; i++) if (k[i] == 13 || k[i] == 14) return true;
        return false;
    }

    private static boolean negative(BattleActor f) {
        return (f.poison != null && f.poison[0] > 0) || f.stunTurns > 0 || f.confuseTurns > 0;
    }

    private static boolean alive(BattleActor x) {
        return x != null && !x.isDead && x.state != 128 && x.hp > 0 && x.isPresent;
    }

    static int maxHp(BattleActor x) {
        int m = x.stats[10] > 0 ? x.stats[10] : x.stats[3];
        if (m < x.hp) m = x.hp;
        return m > 0 ? m : 1;
    }

    private static long clamp(long v, long lo, long hi) {
        return v < lo ? lo : (v > hi ? hi : v);
    }

    // =====================================================================================================
    // Config
    // =====================================================================================================

    static void load() {
        loaded = true;
        readConfig();
        try {   // harness switch (-Dai.log=1); applied after the file so it wins over log=0. On phones getProperty returns null.
            String p = System.getProperty("ai.log");
            if (p != null && p.equals("1")) log = true;
        } catch (Throwable t) { }
    }

    private static void readConfig() {
        try {
            java.io.InputStream in = new EnemyAI().getClass().getResourceAsStream("/ai/config.txt");
            if (in == null) return;
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) >= 0) sb.append((char) c);
            in.close();
            String s = sb.toString();
            int p = 0;
            while (p < s.length()) {
                int e = s.indexOf('\n', p);
                if (e < 0) e = s.length();
                String line = s.substring(p, e).trim();
                p = e + 1;
                int eq = line.indexOf('=');
                if (eq <= 0 || line.charAt(0) == '#') continue;
                String k = line.substring(0, eq).trim(), v = line.substring(eq + 1).trim();
                int n;
                try { n = Integer.parseInt(v); } catch (Throwable t) { continue; }
                set(k, n);
            }
        } catch (Throwable t) { }
    }

    private static void set(String k, int n) {
        if (k.equals("enabled")) enabled = n != 0;
        else if (k.equals("ai")) aiOn = n != 0;
        else if (k.equals("scale")) scaleOn = n != 0;
        else if (k.equals("jit")) jit = n != 0;
        else if (k.equals("pets")) pets = n != 0;
        else if (k.equals("lvmin")) lvMin = (int) clamp(n, -20, 20);
        else if (k.equals("lvmax")) lvMax = (int) clamp(n, -20, 20);
        else if (k.equals("bosslv")) bossLv = (int) clamp(n, -20, 50);
        else if (k.equals("bossfloor")) bossFloor = n != 0;
        else if (k.equals("lvcap")) lvCap = (int) clamp(n, 1, 999);
        else if (k.equals("hpmul")) hpMul = (int) clamp(n, 10, 1000);
        else if (k.equals("atkmul")) atkMul = (int) clamp(n, 10, 1000);
        else if (k.equals("defmul")) defMul = (int) clamp(n, 10, 1000);
        else if (k.equals("spdmul")) spdMul = (int) clamp(n, 10, 1000);
        else if (k.equals("rounds")) rounds = (int) clamp(n, 0, 200);
        else if (k.equals("bossrounds")) bossRounds = (int) clamp(n, 0, 200);
        else if (k.equals("loss")) loss = (int) clamp(n, 0, 100);
        else if (k.equals("bossloss")) bossLoss = (int) clamp(n, 0, 100);
        else if (k.equals("spdfloor")) spdFloor = (int) clamp(n, 0, 300);
        else if (k.equals("defcap")) defCap = (int) clamp(n, 1, 1000);
        else if (k.equals("noise")) noise = (int) clamp(n, 0, 100);
        else if (k.equals("skilltyp")) skillTyp = (int) clamp(n, 50, 500);
        else if (k.equals("randskill")) randSkill = n != 0;
        else if (k.equals("randboss")) randBoss = n != 0;
        else if (k.equals("skillstep")) skillStep = (int) clamp(n, 1, 100);
        else if (k.equals("skillmax")) skillMax = (int) clamp(n, 0, 20);
        else if (k.equals("skilllvstep")) skillLvStep = (int) clamp(n, 1, 100);
        else if (k.equals("log")) log = n != 0;
        if (lvMax < lvMin) lvMax = lvMin;
    }
}
