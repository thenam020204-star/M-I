import javax.microedition.lcdui.Image;

public final class Skill {
   public short skillId;
   public byte level;
   public byte maxLevel;
   public int exp;
   public int expToNext;
   public static Image[] icons;
   public static short[] ids;
   public static String[] names;
   public static String[] iconNames;
   public static short[] targetSides;
   public static short[] targetScopes;
   public static short[][] mpCostsPerLevel;
   public static String[] descriptions;
   private static short[] e_AS;
   public static short[][][] learnRules;
   public static short[] learnPrices;

   public Skill() {
   }

   public Skill(int var1, int var2, int var3) {
      loadTable();
      this.skillId = (short)var1;
      this.level = (byte)var2;
      this.exp = 0;
      this.expToNext = (this.level + 1) * (this.level + 1) << 1;
      this.maxLevel = 10;
      if (learnRules != null) {
         var1 = GameData.findSkillIndex(var1);

         for (int var5 = 0; var5 < learnRules[var1].length; var5++) {
            if (learnRules[var1][var5][0] == var3) {
               this.maxLevel = (byte)learnRules[var1][var5][2];
               return;
            }
         }
      }
   }

   public static void loadTable() {
      if (names == null) {
         if (GameData.skillTable == null) {
            GameData.skillTable = Utils.getStringTable(Utils.loadConfigText("/bin/skill.bin"), "skill:", "skillend", null, "\t");
         }

         for (int var0 = 0; var0 < GameData.skillTable.length; var0++) {
            ids = Utils.append(ids, Utils.parseInt(GameData.skillTable[var0][0]));
            names = Utils.append(names, GameData.skillTable[var0][1]);
            iconNames = Utils.append(iconNames, GameData.skillTable[var0][2]);
            targetSides = Utils.append(targetSides, Utils.parseByte(GameData.skillTable[var0][4]));
            targetScopes = Utils.append(targetScopes, Utils.parseByte(GameData.skillTable[var0][5]));
            short[] var1 = Utils.parseShortArray(GameData.skillTable[var0][6], ",");
            mpCostsPerLevel = Utils.append(mpCostsPerLevel, var1);
            descriptions = Utils.append(descriptions, GameData.skillTable[var0][7]);
            e_AS = Utils.append(e_AS, Utils.parseInt(GameData.skillTable[var0][8]));
            String[] var5 = Utils.split(GameData.skillTable[var0][9], "|");
            short[][] var2 = null;

            for (int var4 = 0; var5 != null && var4 < var5.length; var4++) {
               short[] var3 = Utils.parseShortArray(var5[var4], ",");
               var2 = Utils.append(var2, var3);
            }

            learnRules = GameUtil.append(learnRules, var2);
            learnPrices = Utils.append(learnPrices, Utils.parseInt(GameData.skillTable[var0][10]));
         }
      }
   }

   public static void loadIcons() {
      if (iconNames == null) {
         loadTable();
      }

      int var0 = iconNames.length;
      if (icons == null) {
         icons = new Image[var0];

         for (int var1 = 0; var1 < var0; var1++) {
            icons[var1] = ResourceCache.loadImage("/icon/" + iconNames[var1] + ".gf", 2);
         }
      }
   }
}
