import javax.microedition.lcdui.Image;

public final class Equip {
   public short equipId;
   public byte gemCount;
   public short gemHp;
   public short gemMp;
   public short gemAttack;
   public short gemDefense;
   public short gemSpeed;
   public short gemCrit;
   public short hp;
   public short mp;
   public short attack;
   public short defense;
   public short speed;
   public short socketCount;
   public short crit;
   private static String[] statRollSpecs;
   public byte b_B;
   public static short[] ids;
   public static String[] names;
   public static short[] slotTypes;
   private static String[] iconNames;
   public static String[] allowedJobs;
   public static String[] descriptions;
   public static short[] prices;
   private static short[] e_AS;
   public static short[] requiredLevels;
   public static Image[] icons;

   public Equip() {
   }

   public Equip(int var1) {
      loadTable();
      if (GameData.equipTable == null) {
         GameData.equipTable = Utils.getStringTable(Utils.loadConfigText("/bin/equip.bin"), "equip", "equipend", null, "\t");
      }

      for (int var2 = 0; GameData.equipTable != null && var2 < GameData.equipTable.length; var2++) {
         if (Utils.parseInt(GameData.equipTable[var2][0]) == var1) {
            this.equipId = (short)(var2 + 1);
            break;
         }
      }

      this.b_B = 1;
      this.rollStats();
   }

   private void rollStats() {
      if (statRollSpecs == null) {
         if (GameData.equipTable == null) {
            GameData.equipTable = Utils.getStringTable(Utils.loadConfigText("/bin/equip.bin"), "equip", "equipend", null, "\t");
         }

         String[] var1 = null;

         for (int var2 = 0; var2 < GameData.equipTable.length; var2++) {
            var1 = Utils.append(var1, GameData.equipTable[var2][3]);
            statRollSpecs = Utils.append(statRollSpecs, GameData.equipTable[var2][3]);
         }

         for (int var5 = 0; var5 < GameData.equipTable.length; var5++) {
            requiredLevels = Utils.append(requiredLevels, Utils.parseShort(GameData.equipTable[var5][9]));
         }
      }

      short[][] var4 = GameUtil.parseShortRows(Utils.split(statRollSpecs[this.equipId - 1], "|"), null);

      for (int var6 = 0; var4 != null && var6 < var4.length; var6++) {
         int var3 = GameUtil.randomBetween(var4[var6][1], var4[var6][2]);
         if (var4[var6][0] == 1) {
            this.hp += (short)var3;
         } else if (var4[var6][0] == 2) {
            this.mp += (short)var3;
         } else if (var4[var6][0] == 3) {
            this.attack += (short)var3;
         } else if (var4[var6][0] == 4) {
            this.defense += (short)var3;
         } else if (var4[var6][0] == 5) {
            this.speed += (short)var3;
         } else if (var4[var6][0] == 6) {
            this.socketCount += (short)var3;
            if (this.socketCount < 0) {
               this.socketCount = 0;
            }
         } else if (var4[var6][0] == 7) {
            this.crit += (short)var3;
         } else {
            System.out.println("createEquip specialField logic err");
         }
      }

      this.gemCount = 0;
   }

   public static void loadTable() {
      if (names == null) {
         if (GameData.equipTable == null) {
            GameData.equipTable = Utils.getStringTable(Utils.loadConfigText("/bin/equip.bin"), "equip", "equipend", null, "\t");
         }

         for (int var0 = 0; var0 < GameData.equipTable.length; var0++) {
            names = Utils.append(names, GameData.equipTable[var0][1]);
            slotTypes = Utils.append(slotTypes, Utils.parseInt(GameData.equipTable[var0][2]));
            allowedJobs = Utils.append(allowedJobs, GameData.equipTable[var0][5]);
            descriptions = Utils.append(descriptions, GameData.equipTable[var0][6]);
            iconNames = Utils.append(iconNames, GameData.equipTable[var0][4]);
            prices = Utils.append(prices, Utils.parseInt(GameData.equipTable[var0][7]));
            e_AS = Utils.append(e_AS, Utils.parseInt(GameData.equipTable[var0][8]));
            ids = Utils.append(ids, Utils.parseInt(GameData.equipTable[var0][0]));
            requiredLevels = Utils.append(requiredLevels, Utils.parseShort(GameData.equipTable[var0][9]));
         }
      }
   }

   public static void loadIcons() {
      if (iconNames == null) {
         loadTable();
      }

      if (icons == null) {
         byte var0;
         icons = new Image[var0 = (byte)iconNames.length];

         for (byte var1 = 0; var1 < var0; var1++) {
            icons[var1] = ResourceCache.loadImage("/icon/" + iconNames[var1] + ".gf", 2);
         }
      }
   }

   public final int getTotalSpeed() {
      return this.gemSpeed + this.speed;
   }

   public final int getTotalAttack() {
      return this.gemAttack + this.attack;
   }

   public final int getTotalDefense() {
      return this.gemDefense + this.defense;
   }

   public final int getTotalHp() {
      return this.hp + this.gemHp;
   }

   public final int getTotalMp() {
      return this.mp + this.gemMp;
   }

   public static String describeStats(Equip var0) {
      String var1 = "";
      if (var0.hp + var0.gemHp > 0) {
         var1 = var1 + " HP+" + (var0.hp + var0.gemHp);
      }

      if (var0.mp + var0.gemMp > 0) {
         var1 = var1 + " MP +" + (var0.mp + var0.gemMp);
      }

      if (var0.attack + var0.gemAttack > 0) {
         var1 = var1 + " ATK+" + (var0.attack + var0.gemAttack);
      }

      if (var0.defense + var0.gemDefense > 0) {
         var1 = var1 + " DEF+" + (var0.defense + var0.gemDefense);
      }

      if (var0.speed + var0.gemSpeed > 0) {
         var1 = var1 + " SPD+" + (var0.speed + var0.gemSpeed);
      }

      if (var0.crit + var0.gemCrit > 0) {
         var1 = var1 + " CRIT+" + (var0.crit + var0.gemCrit);
      }

      if (var0.socketCount > 0) {
         var1 = var1 + " Lỗ " + var0.gemCount + "/" + var0.socketCount;
      }

      return var1 + " Cấp yêu cầu " + requiredLevels[GameData.findEquipIndex(var0.equipId)];
   }
}
