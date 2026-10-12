import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public final class GameData {
   public static short startSceneId;
   public static byte storyPhase = 1;
   public static String sceneMapName;
   public static short currentSceneId;
   public static String sceneName;
   private static byte sceneKind;
   public static byte battleBackground = 1;
   public static String sceneMusic;
   public static short[] sceneEnemyIds;
   public static byte[] sceneEnemyCountRange;
   public static String sceneEnemyLevelRange;
   public static String[] recolorImageNames;
   public static String[] recolorSpecs;
   public static short[] townReturnPoint;
   public static byte rideFlags;
   static String colorTableText;
   public static String buildingFileId;
   public static RoleSprite[] party;
   public static byte[] partyRoleIds;
   public static short[][] bagItems;
   public static short[][] equipBagIds;
   public static Equip[][] equipBag;
   public static short c_S;
   private static byte followDistance = 15;
   public static byte followStepGap = 0;
   public static int money;
   public static short[] removedRoleIds;
   public static short[] openedChestIds;
   public static int[] eventFlags;
   public static String[] roleNames;
   public static String[] rolePortraits;
   public static String[] roleAnimPaths;
   public static String mountAnimPath;
   public static String flyAnimPath;
   public static String boatAnimPath = "/av/boat.av";
   public static RoleSaveRecord[] roleRecords;
   public static boolean isRestoringSave;
   public static String battleMusic = null;
   public static String victoryMusic = null;
   public static String defeatMusic = null;
   public static String[] itemNames;
   public static short[] itemIds;
   public static short[] itemTypes;
   private static short[] s_AS;
   private static short[] t_AS;
   private static short[] itemConsumable;
   public static short[] itemBattleUsable;
   public static String[] itemDescriptions;
   public static short[] itemBuyPrices;
   public static short[] itemSellPrices;
   private static String[] itemIconNames;
   public static Image[] itemIcons;
   public static String[] itemStatBonus;
   public static short reviveSceneId;
   public static byte revivePoint;
   public static boolean isEndingPending = false;
   public static String saveIndexStore = "cwzw3save";
   public static String saveSlot1Store = "cwzw3_save1";
   public static String saveSlot2Store = "cwzw3_save2";
   public static String saveSlot3Store = "cwzw3_save3";
   public static boolean hasSaveIndex = false;
   public static byte[] slotUsed = new byte[]{0, 0, 0};
   public static String[] slotSceneNames = new String[]{null, null, null};
   public static short[] slotHeroIds = new short[]{0, 0, 0};
   public static short[] slotHeroLevels = new short[]{1, 1, 1};
   public static short[] slotYears = new short[]{1, 1, 1};
   public static byte[] slotMonths = new byte[]{1, 1, 1};
   public static byte[] slotDays = new byte[]{1, 1, 1};
   public static byte[] slotHours = new byte[]{1, 1, 1};
   public static byte[] slotMinutes = new byte[]{1, 1, 1};
   private static byte[] slotSeconds = new byte[]{1, 1, 1};
   public static RoleSprite[] sceneNpcs;
   public static int statMultiplier;
   public static String[][] itemTable;
   private static String[][] sceneTable;
   private static String[][] roleTable;
   public static String[][] skillTable;
   public static String[][] equipTable;
   public static String[][] partyRoleTable;
   public static String[][] questTable;
   public static Quest[] activeQuests;
   public static short[] questDoneFlags;
   public static short[] activeQuestIds;
   public static short[] finishedQuestIds;
   private static String[][] mazeTable;
   public static String[] mazeEntryPoints;
   public static String[] mazeGatePoints;
   public static boolean isInDungeon;
   public static PetInfo[] myPets;
   private static short[] myPetSpeciesIds;
   private static int[] myPetUids;
   private static PetSaveRecord[] petRecords;
   public static String sceneChangeText;
   public static short heroId = 1;
   public static String[] loadingTips;
   public static byte tipIndex;
   public static String purchaseStore = "cwzw3SmsSave";
   public static short[] discoveredPetIds;
   public static boolean[] newPetFlags;
   public static byte musicVolume;
   public static int score;
   static byte dungeonGateMode;
   static byte dungeonFloorCount;
   public static String[] dungeonLinkScenes;
   static byte dungeonFloor;
   public static short[][] dungeonBosses;
   static short[] dungeonBossPos;
   static short[] dungeonExitPos;
   public static String[] dungeonRewards;
   static String dungeonName;

   public GameData() {
   }

   public static void initNewGame() {
      newPetFlags = new boolean[140];

      for (int var0 = 0; var0 < newPetFlags.length; var0++) {
         newPetFlags[var0] = false;
      }

      short[] var7 = new short[]{heroId};
      c_S = 0;

      for (int var1 = 0; var1 < var7.length; var1++) {
         short var2;
         addPartyMember(createRoleSprite(var2 = var7[var1]));
      }

      short[] var10 = new short[0];

      for (int var8 = 0; var8 < var10.length; var8++) {
         addPet(var10[var8], -1);
      }

      String var9;
      bagItems = Utils.getShortTable(var9 = Utils.loadConfigText("/bin/team.bin"), "item:", "end", "=");
      equipBagIds = Utils.getShortTable(var9, "bag:", "end", "=");

      for (int var11 = 0; var11 < party.length; var11++) {
         RoleSprite var13;
         if ((var13 = party[var11]).isCombatant) {
            refreshRoleStats(var13);
            var13.stats[3] = var13.stats[10];
            var13.stats[5] = var13.stats[11];
         }
      }

      if (equipBagIds != null) {
         short[][] var12 = equipBagIds;
         Equip[][] var10000;
         if (equipBagIds == null) {
            var10000 = null;
         } else {
            byte var3;
            Equip[][] var14 = new Equip[var3 = (byte)var12.length][];

            for (int var4 = 0; var4 < var3; var4++) {
               if (var12[var4] != null) {
                  short var5 = (short)var12[var4].length;
                  var14[var4] = new Equip[var5];

                  for (int var6 = 0; var6 < var5; var6++) {
                     var14[var4][var6] = new Equip(var12[var4][var6]);
                  }
               }
            }

            var10000 = var14;
         }

         equipBag = var10000;
      } else {
         equipBagIds = new short[6][];
         equipBag = new Equip[6][];
      }

      money = Utils.getInt(var9, "money");
      if (DeviceConfig.allowMusic && DeviceConfig.playMusic) {
         if (DeviceConfig.playMusic) {
            System.out.println("true");
            World.playSceneMusic();
            return;
         }

         System.out.println("false");
         World.stopMusic();
      }
   }

   public static void removeEventFlag(int var0) {
      if (var0 > 0 && eventFlags != null) {
         eventFlags = Utils.removeValue(eventFlags, var0);
         if (DeviceConfig.debugMode) {
            PortalProtocol.dumpArray(eventFlags, "Sự kiện đã xảy ra:");
         }
      }
   }

   public static void addEventFlag(int var0) {
      if (var0 > 0) {
         if (DeviceConfig.debugMode) {
            PortalProtocol.dumpArray(eventFlags, "ID sự kiện đã xảy ra trước khi thêm:");
         }

         if (eventFlags != null) {
            if (!Utils.contains(eventFlags, var0)) {
               if (DeviceConfig.debugMode) {
                  System.out.println("Thêm id:" + var0 + " sự kiện kết thúc");
               }

               eventFlags = Utils.append(eventFlags, var0);
               return;
            }

            if (DeviceConfig.debugMode) {
               System.out.println("Sự kiện cần xóa đã tồn tại trong mảng.");
               return;
            }
         } else {
            eventFlags = Utils.append(eventFlags, var0);
            if (DeviceConfig.debugMode) {
               PortalProtocol.dumpArray(eventFlags, "ID sự kiện đã xảy ra sau khi thêm:");
               return;
            }
         }
      } else if (DeviceConfig.debugMode) {
         System.out.println("Không thể thêm id:0 làm sự kiện");
      }
   }

   public static int rollBiasedHalf(int var0, int var1, boolean var2) {
      int var3 = (var0 + var1) / 2;
      int var4 = GameUtil.randomRange(1, 3);
      if (var2) {
         if (var4 < 2) {
            return GameUtil.randomRange(var0, var3 - var0 + 1);
         }
      } else if (var4 >= 2) {
         return GameUtil.randomRange(var0, var3 - var0 + 1);
      }

      return GameUtil.randomRange(var3, var1 - var3 + 1);
   }

   private static void loadPetData(PetInfo var0) {
      int var1 = var0.uid;
      int var2 = 0;

      PetSaveRecord var10000;
      while (true) {
         if (petRecords == null || var2 >= petRecords.length) {
            var10000 = null;
            break;
         }

         if (petRecords[var2].uid == var1) {
            var10000 = petRecords[var2];
            break;
         }

         var2++;
      }

      PetSaveRecord var8 = var10000;
      System.out.println("Sủng vật " + var0.petId + " lấy từ dữ liệu bộ nhớ");
      if (var8 != null) {
         if (DeviceConfig.debugMode) {
            System.out.println("Sủng vật " + var0.petId + " lấy từ dữ liệu bộ nhớ");
         }

         var0.stats = var8.stats;
         var0.isDeployed = var8.isDeployed;
         var0.skillIds = var8.skillIds;
         var0.skills = var8.skills;
         var0.elementValues = var8.elementValues;
         var0.petId = var8.petId;
         var0.name = var8.name;
         var0.dropSpec = var8.dropSpec;
         var0.ownerHeroId = var8.ownerHeroId;
         var0.implantSlots = var8.implantSlots;
      } else {
         if (DeviceConfig.debugMode) {
            System.out.println("Sủng vật " + var0.petId + " lấy dữ liệu từ văn bản");
         }

         String var10;
         String[][] var9 = Utils.getStringTable(var10 = Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
         String[][] var11 = Utils.getStringTable(var10, "stardata", "end", null, "\t");
         if (var9 != null) {
            int var3 = var0.petId - 1;
            var0.name = var9[var3][0];
            var0.stats = Utils.append(var0.stats, 1);

            for (int var7 = 0; var7 < 2; var7++) {
               int var5 = Utils.parseInt(var9[var3][3 + var7 * 2]) - 1;
               int[] var4;
               int var6 = GameUtil.randomRange((var4 = Utils.parseIntArray(var11[var5][var7 * 2], "|"))[0], var4[1] - var4[0] + 1);
               var0.stats = Utils.append(var0.stats, var6);
               var0.stats = Utils.append(var0.stats, var6);
               var5 = Utils.parseInt(var9[var3][4 + var7 * 2]) - 1;
               int[] var17 = Utils.parseIntArray(var11[var5][1 + var7 * 2], "|");
               if (var6 > (var4[0] + var4[1]) / 2) {
                  var0.stats = Utils.append(var0.stats, rollBiasedHalf(var17[0], var17[1], true));
               } else {
                  var0.stats = Utils.append(var0.stats, rollBiasedHalf(var17[0], var17[1], false));
               }
            }

            for (int var24 = 0; var24 < 3; var24++) {
               int var18 = Utils.parseInt(var9[var3][7 + var24 * 2]) - 1;
               int[] var14;
               int var21 = GameUtil.randomRange((var14 = Utils.parseIntArray(var11[var18][4 + var24 * 2], "|"))[0], var14[1] - var14[0] + 1);
               var0.stats = Utils.append(var0.stats, var21);
               var18 = Utils.parseInt(var9[var3][8 + var24 * 2]) - 1;
               int[] var20 = Utils.parseIntArray(var11[var18][5 + var24 * 2], "|");
               if (var21 > (var14[0] + var14[1]) / 2) {
                  var0.stats = Utils.append(var0.stats, rollBiasedHalf(var20[0], var20[1], true));
               } else {
                  var0.stats = Utils.append(var0.stats, rollBiasedHalf(var20[0], var20[1], false));
               }
            }

            int var22 = (var0.stats[0] * var0.stats[0] * var0.stats[0] * var0.stats[0] + 5) / 10;
            var0.stats = Utils.append(var0.stats, var22);
            var22 = (var0.stats[0] + 1) * (var0.stats[0] + 1) * (var0.stats[0] + 1) + (var0.stats[0] + 1) * (var0.stats[0] + 1);
            var0.stats = Utils.append(var0.stats, var22);
            var0.stats = Utils.append(var0.stats, 0);
            var0.stats = Utils.append(var0.stats, 60);
            Utils.parseByte(var9[var3][1]);
            if (!var9[var3][19].equals("-1")) {
               var0.dropSpec = var9[var3][19];
            } else {
               var0.dropSpec = "";
            }

            if (!var9[var3][18].equals("-1")) {
               var0.refineItemIds = Utils.parseByteArray(var9[var3][18], ",");
            }

            var0.isDeployed = false;
            if (var9[var3][13].equals("-1")) {
               var0.skillIds = null;
            } else {
               String[] var25 = Utils.split(var9[var3][13], "|");

               for (int var12 = 0; var12 < var25.length; var12++) {
                  short[] var15 = Utils.parseShortArray(var25[var12], ",");
                  var0.skillIds = Utils.append(var0.skillIds, var15[0]);
               }
            }

            var0.skills = buildPetSkills(var0, 1);
            if (var9[var3][2] != null) {
               for (int var26 = 0; var26 < 4; var26++) {
                  String var13;
                  if ((var13 = var9[var3][2].substring(var26, var26 + 1)).equals("a")) {
                     var0.elementValues = Utils.append(var0.elementValues, 10);
                  } else {
                     var0.elementValues = Utils.append(var0.elementValues, Utils.parseInt(var13));
                  }
               }
            }

            var0.ownerHeroId = (byte)heroId;
         }
      }

      storePetRecords(new PetInfo[]{var0});
   }

   public static void loadRoleData(RoleSprite var0) {
      if (partyRoleTable == null) {
         partyRoleTable = Utils.getStringTable(Utils.loadConfigText("/bin/team.bin"), "role:", "roleEnd", null, "\t");
      }

      byte var1 = -1;

      for (int var2 = 0; var2 < partyRoleTable.length; var2++) {
         if (Utils.parseInt(partyRoleTable[var2][1]) == var0.roleId) {
            var1 = (byte)var2;
            break;
         }
      }

      if (var1 < 0) {
         var0.facePath = rolePortraits[var0.roleId - 1];
         var0.isCombatant = false;
      } else {
         var0.isCombatant = true;
         RoleSaveRecord var4;
         if ((var4 = findRoleRecord(var0.roleId)) != null) {
            if (DeviceConfig.debugMode) {
               System.out.println("Nhân vật " + var0.roleId + " lấy từ dữ liệu bộ nhớ");
            }

            var0.stats = var4.stats;
            var0.skillIds = var4.skillIds;
            var0.skills = var4.skills;
            var0.equipIds = var4.equipIds;
            var0.equips = var4.equips;
            if (var0.roleId == 1) {
               var0.actorType = 1;
            } else {
               var0.actorType = 2;
            }

            var0.elementValues = var4.elementValues;
            var0.attrGrowth = var4.attrGrowth;
            var0.jobLevel = var4.jobLevel;
         } else {
            if (DeviceConfig.debugMode) {
               System.out.println("Nhân vật " + var0.roleId + " lấy từ dữ liệu văn bản");
            }

            int[] var5 = null;

            for (int var3 = 3; var3 < 13; var3++) {
               var5 = Utils.append(var5, Utils.parseInt(partyRoleTable[var1][var3]));
            }

            var0.stats = new int[RoleSprite.statCount];
            System.arraycopy(var5, 0, var0.stats, 0, var5.length);
            var0.stats[16] = 10;
            if (partyRoleTable[var1][13].equals("-1")) {
               var0.skillIds = null;
            } else {
               var0.skillIds = Utils.parseShortArray(partyRoleTable[var1][13], ",");
            }

            var0.skills = buildRoleSkills(var0, 1);
            var0.equipIds = Utils.parseShortArray(partyRoleTable[var1][14], ",");
            var0.equips = createEquips(var0.equipIds, -1);
            if (var0.roleId != 1 && var0.roleId != 2) {
               var0.actorType = 2;
            } else {
               var0.actorType = 1;
            }

            var0.elementValues = Utils.parseByteArray(partyRoleTable[var1][15], ",");
            var0.attrGrowth[0] = Utils.parseByteArray(partyRoleTable[var1][16], ",");
            var0.attrGrowth[1] = Utils.parseByteArray(partyRoleTable[var1][17], ",");
            var0.attrGrowth[2] = Utils.parseByteArray(partyRoleTable[var1][18], ",");
            var0.attrGrowth[3] = Utils.parseByteArray(partyRoleTable[var1][19], ",");
            var0.jobLevel = 1;
         }

         if (var0.equips == null) {
            var0.equips = new Equip[6];
         } else if (var0.equips.length < 6) {
            Equip[] var6 = new Equip[6];
            System.arraycopy(var0.equips, 0, var6, 0, var0.equips.length);
            var0.equips = null;
            var0.equips = var6;
         }

         var0.facePath = rolePortraits[var0.roleId - 1];
         storeRoleRecords(new RoleSprite[]{var0});
      }
   }

   public static void loadGame(String var0) {
      c_S = 0;
      String var1 = saveSlot1Store;
      if (var0.equals("Tiến độ 1")) {
         var1 = saveSlot1Store;
      } else if (var0.equals("Tiến độ 2")) {
         var1 = saveSlot2Store;
      } else if (var0.equals("Tiến độ 3")) {
         var1 = saveSlot3Store;
      }

      readSaveRecord(var1);

      for (int var2 = 0; var2 < partyRoleIds.length; var2++) {
         byte var5;
         addPartyMember(createRoleSprite(var5 = partyRoleIds[var2]));
      }

      for (int var3 = 0; activeQuestIds != null && var3 < activeQuestIds.length; var3++) {
         appendQuest(createQuest(activeQuestIds[var3], var3));
      }

      for (int var4 = 0; myPetSpeciesIds != null && var4 < myPetSpeciesIds.length; var4++) {
         addPet(myPetSpeciesIds[var4], myPetUids[var4]);
      }
   }

   public static void addPet(int var0, int var1) {
      PetInfo var2;
      (var2 = new PetInfo()).petId = var0;
      if (var1 == -1) {
         if (myPetUids == null) {
            var2.uid = 1;
         } else {
            var2.uid = GameUtil.maxValue(myPetUids) + 1;
         }
      } else {
         var2.uid = var1;
      }

      if (!Utils.contains(discoveredPetIds, var0)) {
         discoveredPetIds = Utils.append(discoveredPetIds, var0);
         newPetFlags[var0 - 1] = true;
      }

      loadPetData(var2);
      if (myPets == null) {
         myPets = new PetInfo[1];
      } else {
         PetInfo[] var3 = new PetInfo[myPets.length + 1];
         System.arraycopy(myPets, 0, var3, 0, myPets.length);
         myPets = var3;
      }

      myPets[myPets.length - 1] = var2;
      if (var2.stats == null) {
         loadPetData(var2);
      }

      if (!Utils.contains(myPetUids, var2.uid)) {
         myPetUids = Utils.append(myPetUids, var2.uid);
         myPetSpeciesIds = Utils.append(myPetSpeciesIds, var2.petId);
      }
   }

   public static Quest createQuest(int var0, int var1) {
      Quest var2 = new Quest();
      if (questTable == null) {
         questTable = Utils.getStringTable(Utils.loadConfigText("/bin/task.bin"), "task", "taskend", null, "\t");
      }

      short var3 = -1;

      for (int var4 = 0; var4 < questTable.length; var4++) {
         if (Utils.parseShort(questTable[var4][0]) == var0) {
            var3 = (short)var4;
            break;
         }
      }

      label28: {
         Quest var10000;
         boolean var10001;
         label27: {
            var2.id = (short)var0;
            var2.name = questTable[var3][1];
            if (var1 == -1) {
               var10000 = var2;
            } else {
               if (questDoneFlags == null) {
                  break label28;
               }

               var10000 = var2;
               if (questDoneFlags[var1] == 1) {
                  var10001 = true;
                  break label27;
               }
            }

            var10001 = false;
         }

         var10000.isCompleted = var10001;
      }

      var2.type = Utils.parseByte(questTable[var3][3]);
      var2.repeatable = Utils.parseByte(questTable[var3][4]);
      var2.description = questTable[var3][5];
      var2.conditions = questTable[var3][6];
      var2.completionCost = questTable[var3][7];
      var2.reward = questTable[var3][8];
      Utils.parseShort(questTable[var3][9]);
      var2.turnInRoleId = Utils.parseShort(questTable[var3][10]);
      return var2;
   }

   private static void appendQuest(Quest var0) {
      if (activeQuests == null) {
         activeQuests = new Quest[1];
      } else {
         Quest[] var1 = new Quest[activeQuests.length + 1];
         System.arraycopy(activeQuests, 0, var1, 0, activeQuests.length);
         activeQuests = var1;
      }

      activeQuests[activeQuests.length - 1] = var0;
   }

   public static void addSceneNpc(RoleSprite var0) {
      if (sceneNpcs == null) {
         sceneNpcs = new RoleSprite[1];
      } else {
         RoleSprite[] var1 = new RoleSprite[sceneNpcs.length + 1];
         System.arraycopy(sceneNpcs, 0, var1, 0, sceneNpcs.length);
         sceneNpcs = var1;
      }

      sceneNpcs[sceneNpcs.length - 1] = var0;
   }

   public static void addPartyMember(RoleSprite var0) {
      if (party == null) {
         party = new RoleSprite[1];
      } else {
         RoleSprite[] var1 = new RoleSprite[party.length + 1];
         System.arraycopy(party, 0, var1, 0, party.length);
         party = var1;
      }

      party[party.length - 1] = var0;
      if (var0.stats == null) {
         loadRoleData(var0);
      }

      var0.canWander = false;
      if (!Utils.contains(partyRoleIds, var0.roleId)) {
         partyRoleIds = Utils.append(partyRoleIds, var0.roleId);
      }
   }

   public static RoleSprite createRoleSprite(int var0) {
      RoleSprite var1 = new RoleSprite();
      if (roleTable == null) {
         roleTable = Utils.getStringTable(Utils.loadConfigText("/bin/role.bin"), "role:", "roleEnd", null, "\t");
      }

      String[] var2 = null;
      short[] var3 = null;

      for (int var4 = 0; var4 < roleTable.length; var4++) {
         var2 = Utils.append(var2, roleTable[var4][2]);
         var3 = Utils.append(var3, Utils.parseShort(roleTable[var4][0]));
      }

      int var6 = 0;

      for (int var5 = 0; var5 < var3.length; var5++) {
         if (var0 == var3[var5]) {
            var6 = var5;
            break;
         }
      }

      if (var2[var6].toLowerCase().endsWith(".png") || var2[var6].toLowerCase().endsWith(".gf")) {
         (var1 = new RoleSprite(ResourceCache.loadImage(var2[var6]))).getCollisionBoxes(1);
      } else if (var2[var6].toLowerCase().endsWith(".av")) {
         var1.renderMode = 1;
         AnimSprite var7;
         (var7 = new AnimSprite()).load(var2[var6]);
         var1 = new RoleSprite(var7);
      } else if (DeviceConfig.debugMode) {
         System.out.println("Định dạng ảnh tinh linh không xác định:" + var2[var6]);
      }

      var1.setDirection(0);
      var1.roleId = (short)var0;
      var1.name = roleNames[var1.roleId - 1];
      var1.facePath = rolePortraits[var1.roleId - 1];
      var1.kind = 1;
      loadRoleData(var1);
      return var1;
   }

   public static void loadStaticTables() {
      String var0 = Utils.loadConfigText("/bin/sn.bin");
      if (sceneTable == null) {
         sceneTable = Utils.getStringTable(var0, "data:", "dataEnd", null, "\t");
      }

      String var1;
      battleMusic = Utils.getValue(var1 = Utils.substringBetween(var0, "battleMusic:", "end"), "inbattle");
      victoryMusic = Utils.getValue(var1, "win");
      defeatMusic = Utils.getValue(var1, "lose");
      startSceneId = Utils.getShort(var0, "default");
      heroId = Utils.getShort(var0, "heroId");
      if (reviveSceneId == 0) {
         reviveSceneId = Utils.getShort(var0, "liveBackScene");
      }

      if (revivePoint == 0) {
         revivePoint = Utils.getByte(var0, "liveBackPos");
      }

      var0 = Utils.loadConfigText("/bin/role.bin");
      if (roleTable == null) {
         roleTable = Utils.getStringTable(var0, "role:", "roleEnd", null, "\t");
      }

      flyAnimPath = Utils.getValue(var0, "roleFlyBody");
      mountAnimPath = Utils.getValue(var0, "roleRunBodys");

      for (int var3 = 0; var3 < roleTable.length; var3++) {
         roleNames = Utils.append(roleNames, roleTable[var3][1]);
         if (roleTable[var3][3].equals("-1")) {
            roleTable[var3][3] = "";
         }

         rolePortraits = Utils.append(rolePortraits, roleTable[var3][3]);
         roleAnimPaths = Utils.append(roleAnimPaths, roleTable[var3][2]);
      }

      if (loadingTips == null) {
         if ((var0 = Utils.substringBetween(Utils.loadConfigText("/bin/about.bin"), "tips:", "tipsEnd")) != null) {
            loadingTips = Utils.split(var0, "\r\n");
         }

         tipIndex = (byte)GameUtil.randomRange(0, loadingTips.length);
      }
   }

   public static boolean loadSceneInfo(int var0) {
      sceneMapName = null;
      sceneEnemyIds = null;
      mazeTable = null;
      mazeEntryPoints = null;
      mazeGatePoints = null;
      recolorImageNames = null;
      recolorSpecs = null;
      String var1 = Utils.loadConfigText("/bin/sn.bin");
      if (sceneTable == null) {
         sceneTable = Utils.getStringTable(var1, "data:", "dataEnd", null, "\t");
      }

      boolean var9 = false;
      int var2 = 0;

      for (int var3 = 0; var3 < sceneTable.length; var3++) {
         if (Utils.parseShort(sceneTable[var3][0]) == var0) {
            var2 = var3;
            var9 = true;
            break;
         }
      }

      if (var9) {
         sceneKind = Utils.parseByte(sceneTable[var2][3]);
         if (sceneTable[var2].length > 12) {
            battleBackground = Utils.parseByte(sceneTable[var2][12]);
         } else {
            System.out.println("File Sn thiếu loại cảnh chiến đấu");
         }

         if (sceneKind == 1) {
            sceneMapName = sceneTable[var2][2];
            buildingFileId = String.valueOf(var0);
            dungeonFloor = 0;
            isInDungeon = false;
         } else {
            dungeonName = null;
            dungeonRewards = null;
            dungeonBosses = null;
            dungeonExitPos = null;
            dungeonLinkScenes = null;
            dungeonBossPos = null;
            String var13;
            String[][] var7 = Utils.getStringTable(var13 = Utils.loadConfigText("/bin/mazefile.bin"), "fuben:", "fubenEnd", null, "\t");
            int var10 = 0;

            for (int var4 = 0; var7 != null && var4 < var7.length; var4++) {
               if (var7[var4][0].equals(sceneTable[var2][2])) {
                  var10 = var4;
                  break;
               }
            }

            dungeonName = var7[var10][1];
            dungeonGateMode = Utils.parseByte(var7[var10][2]);
            dungeonFloorCount = Utils.parseByte(var7[var10][4]);
            dungeonLinkScenes = Utils.split(var7[var10][5], ",");
            String[] var17 = Utils.split(var7[var10][6], "|");

            for (int var6 = 0; var17 != null && var6 < var17.length; var6++) {
               short[] var5 = Utils.parseShortArray(var17[var6], ",");
               dungeonBosses = Utils.append(dungeonBosses, var5);
            }

            dungeonBossPos = Utils.parseShortArray(var7[var10][7], ",");
            dungeonExitPos = Utils.parseShortArray(var7[var10][8], ",");
            if (!var7[var10][9].equals("-1")) {
               dungeonRewards = Utils.split(var7[var10][9], "|");
            }

            if (!isInDungeon) {
               if (World.entryPointIndex == 2) {
                  dungeonFloor = dungeonFloorCount;
               } else {
                  dungeonFloor = 1;
               }
            }

            int var22 = 0;
            mazeTable = Utils.getStringTable(var13, "maze:", "mazeEnd", null, "\t");

            for (int var18 = 0; var18 < mazeTable.length; var18++) {
               if (mazeTable[var18][0].equals(var7[var10][3])) {
                  var22 = var18;
                  break;
               }
            }

            var17 = Utils.split(mazeTable[var22][1], ",");
            var0 = GameUtil.randomRange(0, var17.length);
            sceneMapName = var17[var0];
            buildingFileId = Utils.split(mazeTable[var22][2], ",")[var0];
            String[] var11 = Utils.getStringBlock(var13, "mazepos:", "mazeposEnd", "=");
            int var14 = Utils.parseInt(mazeTable[var22][3]);
            var17 = Utils.split(var11[var14], "#");
            String[] var12 = Utils.split(var11[var14 + 1], "#");
            String[] var15 = Utils.split(var17[var0], "|");
            int[] var21 = GameUtil.pickTwoDistinct(0, var15.length - 1, 2);
            mazeEntryPoints = Utils.append(mazeEntryPoints = Utils.append(mazeEntryPoints, var15[var21[0]]), var15[var21[1]]);
            String[] var16 = Utils.split(var12[var0], "|");
            mazeGatePoints = Utils.append(mazeGatePoints = Utils.append(mazeGatePoints, var16[var21[0]]), var16[var21[1]]);
            isInDungeon = true;
         }

         sceneName = sceneTable[var2][1];
         if (dungeonFloor > 0) {
            sceneName = sceneName + dungeonFloor;
         }

         sceneMusic = sceneTable[var2][4];
         if (sceneTable[var2][5].equals("-1")) {
            sceneEnemyIds = null;
         } else {
            sceneEnemyIds = Utils.parseShortArray(sceneTable[var2][5], ",");
         }

         if (sceneTable[var2][6].equals("-1")) {
            sceneEnemyCountRange = null;
         } else {
            sceneEnemyCountRange = Utils.parseByteArray(sceneTable[var2][6], ",");
         }

         if (sceneTable[var2][7].equals("-1")) {
            sceneEnemyLevelRange = null;
         } else {
            sceneEnemyLevelRange = sceneTable[var2][7];
         }

         if (sceneTable[var2][8].equals("-1")) {
            townReturnPoint = null;
         } else {
            townReturnPoint = Utils.parseShortArray(sceneTable[var2][8], ",");
         }

         if (sceneTable[var2][9].equals("-1")) {
            recolorImageNames = null;
         } else {
            recolorImageNames = Utils.split(sceneTable[var2][9], ",");
         }

         if (sceneTable[var2][10].equals("-1")) {
            recolorSpecs = null;
         } else {
            recolorSpecs = Utils.split(sceneTable[var2][10], "|");
         }

         if (sceneTable[var2][11].equals("-1")) {
            rideFlags = 0;
         } else {
            rideFlags = Utils.parseByte(sceneTable[var2][11]);
         }

         if (colorTableText == null) {
            colorTableText = Utils.loadConfigText("/bin/colorTable.bin");
         }

         return true;
      } else {
         System.out.println("Dữ liệu cảnh không tồn tại");
         return false;
      }
   }

   public static RoleSaveRecord findRoleRecord(int var0) {
      for (int var1 = 0; roleRecords != null && var1 < roleRecords.length; var1++) {
         if (roleRecords[var1].roleId == var0) {
            return roleRecords[var1];
         }
      }

      return null;
   }

   public static void applyPetStatBonus(PetInfo var0, String var1) {
      String[] var4 = Utils.split(var1, "|");

      for (int var2 = 0; var2 < var4.length; var2++) {
         short[] var3 = Utils.parseShortArray(var4[var2], ",");
         var0.stats[var3[0]] = var0.stats[var3[0]] + var3[1];
      }
   }

   public static void updateFollowSpacing(byte var0) {
      byte var1 = (byte)(followDistance / var0);
      if ((var0 = (byte)(followDistance % var0)) > 0) {
         var0 = (byte)(var1 + 1);
      } else {
         var0 = var1;
      }

      followStepGap = var0;
   }

   public static void placeFollowers() {
      int var0 = 0;
      short[][] var1 = party[0].followTrail;

      for (int var2 = 0; party != null && var2 < party.length; var2++) {
         if (var2 != 0) {
            party[var2].x = var1[(var0 + 1) * followStepGap - 1][0];
            party[var2].y = var1[(var0 + 1) * followStepGap - 1][1];
            party[var2].setDirection(var1[(var0 + 1) * followStepGap - 1][2]);
            party[var2].animSprite.setFrame(0);
            var0++;
         }
      }
   }

   public static int countBagSlotsInGroup(int var0) {
      int var1 = 0;
      if (bagItems != null) {
         var0 = findItemIndex(var0);
         short var2 = itemTypes[var0];

         for (int var3 = 0; var3 < bagItems.length; var3++) {
            var0 = findItemIndex(bagItems[var3][0]);
            short var6 = itemTypes[var0];
            if (var2 == 1) {
               if (var6 != 1) {
                  continue;
               }
            } else {
               if (var2 == 2 || var2 == 5) {
                  if (var6 == 2 || var6 == 5) {
                     var1++;
                  }
                  continue;
               }

               if (var2 != 3 && var2 != 4 || var6 != 3 && var6 != 4) {
                  continue;
               }
            }

            var1++;
         }
      }

      return var1;
   }

   public static void addItem(int var0, int var1) {
      int var2 = countBagSlotsInGroup(var0);
      if (bagItems == null) {
         if (var2 < GameSettings.maxItemTypes) {
            (bagItems = new short[1][2])[0][0] = (short)var0;
            bagItems[0][1] = (short)var1;
         } else {
            GameCanvas.instance.showToast("Túi đồ đã đầy", 10);
         }
      } else {
         boolean var3 = false;

         for (int var4 = 0; bagItems != null && var4 < bagItems.length; var4++) {
            if (bagItems[var4][0] == var0) {
               if (bagItems[var4][1] + var1 > 99) {
                  bagItems[var4][1] = 99;
                  GameCanvas.instance.showToast("Số vật phẩm đã tối đa", 10);
               } else {
                  bagItems[var4][1] = (short)(bagItems[var4][1] + var1);
               }

               var3 = true;
               break;
            }
         }

         if (!var3) {
            if (var2 < GameSettings.maxItemTypes) {
               short[][] var5 = new short[bagItems.length + 1][2];
               System.arraycopy(bagItems, 0, var5, 0, bagItems.length);
               var5[var5.length - 1][0] = (short)var0;
               var5[var5.length - 1][1] = (short)var1;
               bagItems = var5;
               return;
            }

            GameCanvas.instance.showToast("Túi đồ đã đầy", 10);
         }
      }
   }

   public static void removeItem(int var0, int var1) {
      if (bagItems != null) {
         for (int var2 = 0; var1 > 0 && var2 < bagItems.length; var2++) {
            if (bagItems[var2][0] == var0) {
               if (bagItems[var2][1] >= var1) {
                  bagItems[var2][1] = (short)(bagItems[var2][1] - var1);
                  var1 = 0;
               } else {
                  var1 -= bagItems[var2][1];
                  bagItems[var2][1] = 0;
               }
            }
         }

         for (int var3 = bagItems.length - 1; var3 >= 0; var3--) {
            if (bagItems[var3][1] <= 0) {
               bagItems = Utils.removeAt(bagItems, var3);
            }
         }
      }
   }

   public static void loadItemTable() {
      if (itemNames == null) {
         if (itemTable == null) {
            String var0;
            itemTable = Utils.getStringTable(var0 = Utils.loadConfigText("/bin/item.bin"), "item", "itemend", null, "\t");
            Utils.substringBetween(var0, "itemDefaultAni:", "itemDefaultAniEnd");
         }

         for (int var1 = 0; var1 < itemTable.length; var1++) {
            itemIds = Utils.append(itemIds, Utils.parseInt(itemTable[var1][0]));
            itemNames = Utils.append(itemNames, itemTable[var1][1]);
            itemTypes = Utils.append(itemTypes, Utils.parseInt(itemTable[var1][3]));
            s_AS = Utils.append(s_AS, Utils.parseInt(itemTable[var1][4]));
            itemBattleUsable = Utils.append(itemBattleUsable, Utils.parseInt(itemTable[var1][5]));
            t_AS = Utils.append(t_AS, Utils.parseInt(itemTable[var1][6]));
            itemConsumable = Utils.append(itemConsumable, Utils.parseInt(itemTable[var1][7]));
            itemDescriptions = Utils.append(itemDescriptions, itemTable[var1][8]);
            itemBuyPrices = Utils.append(itemBuyPrices, Utils.parseInt(itemTable[var1][9]));
            itemSellPrices = Utils.append(itemSellPrices, Utils.parseInt(itemTable[var1][10]));
            itemIconNames = Utils.append(itemIconNames, itemTable[var1][2]);
            itemStatBonus = Utils.append(itemStatBonus, itemTable[var1][11]);
         }
      }
   }

   public static void loadItemIcons() {
      if (itemIconNames == null) {
         loadItemTable();
      }

      if (itemIcons == null) {
         short var0;
         itemIcons = new Image[var0 = (short)itemIconNames.length];

         for (int var1 = 0; var1 < var0; var1++) {
            itemIcons[var1] = ResourceCache.loadImage("/icon/" + itemIconNames[var1] + ".gf", 2);
         }
      }
   }

   public static void refreshRoleStats(RoleSprite var0) {
      var0.stats[14] = var0.getSpeed();
      var0.stats[12] = var0.getAttack();
      var0.stats[13] = var0.getDefense();
      var0.stats[10] = var0.getMaxHp();
      var0.stats[11] = var0.getMaxMp();
      var0.stats[16] = var0.getFreePoints();
      var0.stats[17] = var0.getAttrPoints1();
      var0.stats[18] = var0.getAttrPoints2();
      var0.stats[19] = var0.getAttrPoints3();
      var0.stats[20] = var0.getAttrPoints4();
      if (var0.stats[3] > var0.stats[10]) {
         var0.stats[3] = var0.stats[10];
      }

      if (var0.stats[5] > var0.stats[11]) {
         var0.stats[5] = var0.stats[11];
      }
   }

   public static PetInfo[] getReservePets() {
      Vector var0 = new Vector();
      PetInfo[] var1 = null;

      for (byte var2 = 0; myPets != null && var2 < myPets.length; var2++) {
         if (myPets[var2].ownerHeroId == heroId && !myPets[var2].isDeployed && myPets[var2].stats[1] > 0 && !myPets[var2].isFainted) {
            var0.addElement(myPets[var2]);
         }
      }

      if (var0.size() > 0) {
         var1 = new PetInfo[var0.size()];
         var0.copyInto(var1);
      }

      return var1;
   }

   public static RoleSprite[] buildBattleParty() {
      Vector var0 = new Vector();
      RoleSprite[] var1 = null;

      for (byte var2 = 0; myPets != null && var2 < myPets.length; var2++) {
         if (myPets[var2].ownerHeroId == heroId && myPets[var2].isDeployed && myPets[var2].stats[1] > 0) {
            PetInfo var3;
            RoleSprite var6 = createPetSprite(var3 = myPets[var2]);
            if (party == null) {
               party = new RoleSprite[1];
            } else {
               RoleSprite[] var4 = new RoleSprite[party.length + 1];
               System.arraycopy(party, 0, var4, 0, party.length);
               party = var4;
            }

            party[party.length - 1] = var6;
         }
      }

      for (int var5 = 0; var5 < 4 && var5 < party.length; var5++) {
         if (party[var5].isCombatant) {
            var0.addElement(party[var5]);
         }
      }

      if (var0.size() > 0) {
         var1 = new RoleSprite[var0.size()];
         var0.copyInto(var1);
      }

      return var1;
   }

   public static RoleSprite createPetSprite(PetInfo var0) {
      RoleSprite var1;
      (var1 = new RoleSprite(GameUtil.loadBattleAnimation(var0.petId, true))).isCombatant = true;
      var1.name = var0.name;
      var1.elementValues = var0.elementValues;
      var1.stats = new int[RoleSprite.statCount];
      var1.stats[0] = var0.stats[0];
      var1.stats[3] = var0.stats[1];
      var1.stats[10] = var0.stats[2];
      var1.stats[5] = var0.stats[4];
      var1.stats[11] = var0.stats[5];
      var1.stats[12] = var0.stats[7];
      var1.stats[13] = var0.stats[9];
      var1.stats[14] = var0.stats[11];
      var1.stats[1] = var0.stats[13];
      var1.stats[2] = var0.stats[14];
      var1.stats[15] = var0.stats[15];
      var1.skillIds = var0.skillIds;
      var1.skills = var0.skills;
      var1.roleId = (short)var0.petId;
      var1.petUid = (short)var0.uid;
      var1.equips = null;
      var1.equips = new Equip[6];
      return var1;
   }

   private static Skill[] buildPetSkills(PetInfo var0, int var1) {
      if (var0.skillIds == null) {
         return null;
      } else {
         Skill[] var3 = new Skill[var0.skillIds.length];

         for (int var2 = 0; var2 < var0.skillIds.length; var2++) {
            var3[var2] = new Skill(var0.skillIds[var2], 1, 4);
         }

         return var3;
      }
   }

   private static Skill[] buildRoleSkills(RoleSprite var0, int var1) {
      if (var0.skillIds == null) {
         return null;
      } else {
         Skill[] var3 = new Skill[var0.skillIds.length];

         for (int var2 = 0; var2 < var0.skillIds.length; var2++) {
            var3[var2] = new Skill(var0.skillIds[var2], 1, var0.roleId);
         }

         return var3;
      }
   }

   private static Equip[] createEquips(short[] var0, int var1) {
      if (var0 == null) {
         System.out.println("Trang bị của tinh linh này là null");
         return null;
      } else if (var0 != null) {
         Equip[] var3 = new Equip[var0.length];

         for (int var2 = 0; var2 < var0.length; var2++) {
            if (var0[var2] > 0) {
               var3[var2] = new Equip(var0[var2]);
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public static void storePetRecords(PetInfo[] var0) {
      boolean var1 = false;

      for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
         for (int var3 = 0; petRecords != null && var3 < petRecords.length; var3++) {
            if (petRecords[var3].uid == var0[var2].uid) {
               var1 = true;
               petRecords[var3].stats = var0[var2].stats;
               petRecords[var3].skillIds = var0[var2].skillIds;
               petRecords[var3].skills = var0[var2].skills;
               petRecords[var3].elementValues = var0[var2].elementValues;
               petRecords[var3].name = var0[var2].name;
               petRecords[var3].petId = var0[var2].petId;
               petRecords[var3].isDeployed = var0[var2].isDeployed;
               petRecords[var3].dropSpec = var0[var2].dropSpec;
               petRecords[var3].implantSlots = var0[var2].implantSlots;
            }
         }

         if (!var1 && var0[var2].petId > 0) {
            if (petRecords == null) {
               petRecords = new PetSaveRecord[1];
            } else {
               PetSaveRecord[] var4 = new PetSaveRecord[petRecords.length + 1];
               System.arraycopy(petRecords, 0, var4, 0, petRecords.length);
               petRecords = var4;
            }

            petRecords[petRecords.length - 1] = new PetSaveRecord();
            petRecords[petRecords.length - 1].stats = var0[var2].stats;
            petRecords[petRecords.length - 1].petId = var0[var2].petId;
            petRecords[petRecords.length - 1].skillIds = var0[var2].skillIds;
            petRecords[petRecords.length - 1].skills = var0[var2].skills;
            petRecords[petRecords.length - 1].name = var0[var2].name;
            petRecords[petRecords.length - 1].elementValues = var0[var2].elementValues;
            petRecords[petRecords.length - 1].uid = var0[var2].uid;
            petRecords[petRecords.length - 1].isDeployed = var0[var2].isDeployed;
            petRecords[petRecords.length - 1].dropSpec = var0[var2].dropSpec;
            petRecords[petRecords.length - 1].ownerHeroId = var0[var2].ownerHeroId;
            petRecords[petRecords.length - 1].implantSlots = var0[var2].implantSlots;
         }
      }
   }

   public static void storeRoleRecords(RoleSprite[] var0) {
      if (partyRoleTable == null) {
         partyRoleTable = Utils.getStringTable(Utils.loadConfigText("/bin/team.bin"), "role:", "roleEnd", null, "\t");
      }

      boolean var1 = false;

      for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
         byte var3 = -1;

         for (int var4 = 0; var4 < partyRoleTable.length; var4++) {
            if (Utils.parseInt(partyRoleTable[var4][1]) == var0[var2].roleId) {
               var3 = (byte)var4;
               break;
            }
         }

         if (var0[var2] != null && var3 >= 0) {
            for (int var5 = 0; roleRecords != null && var5 < roleRecords.length; var5++) {
               if (roleRecords[var5].roleId == var0[var2].roleId) {
                  var1 = true;
                  roleRecords[var5].stats = var0[var2].stats;
                  roleRecords[var5].skillIds = var0[var2].skillIds;
                  roleRecords[var5].skills = var0[var2].skills;
                  roleRecords[var5].equipIds = var0[var2].equipIds;
                  roleRecords[var5].equips = var0[var2].equips;
                  roleRecords[var5].attrGrowth = var0[var2].attrGrowth;
                  roleRecords[var5].elementValues = var0[var2].elementValues;
                  roleRecords[var5].jobLevel = var0[var2].jobLevel;
                  break;
               }
            }

            if (!var1 && var0[var2].roleId > 0) {
               if (roleRecords == null) {
                  roleRecords = new RoleSaveRecord[1];
               } else {
                  RoleSaveRecord[] var6 = new RoleSaveRecord[roleRecords.length + 1];
                  System.arraycopy(roleRecords, 0, var6, 0, roleRecords.length);
                  roleRecords = var6;
               }

               roleRecords[roleRecords.length - 1] = new RoleSaveRecord();
               roleRecords[roleRecords.length - 1].roleId = (byte)var0[var2].roleId;
               roleRecords[roleRecords.length - 1].stats = var0[var2].stats;
               roleRecords[roleRecords.length - 1].attrGrowth = var0[var2].attrGrowth;
               roleRecords[roleRecords.length - 1].skillIds = var0[var2].skillIds;
               roleRecords[roleRecords.length - 1].skills = var0[var2].skills;
               roleRecords[roleRecords.length - 1].equipIds = var0[var2].equipIds;
               roleRecords[roleRecords.length - 1].equips = var0[var2].equips;
               roleRecords[roleRecords.length - 1].x = var0[var2].x;
               roleRecords[roleRecords.length - 1].y = var0[var2].y;
               roleRecords[roleRecords.length - 1].direction = var0[var2].direction;
               roleRecords[roleRecords.length - 1].elementValues = var0[var2].elementValues;
               roleRecords[roleRecords.length - 1].jobLevel = var0[var2].jobLevel;
            }
         }
      }
   }

   public static void syncPetsFromBattle(RoleSprite[] var0) {
      PetInfo var1 = new PetInfo();

      for (int var2 = 1; var0 != null && var2 < var0.length; var2++) {
         if (var0[var2] != null) {
            System.out.println("Cập nhật dữ liệu chiến đấu " + var0[var2].petUid);
            boolean var3 = false;

            for (int var4 = 0; myPets != null && var4 < myPets.length; var4++) {
               System.out.println("GameData.myPet[j].onlyId" + myPets[var4].uid);
               if (var0[var2].petUid == myPets[var4].uid) {
                  var3 = true;
                  var1 = myPets[var4];
                  System.out.println("Tìm thấy sủng vật " + var1.name);
                  break;
               }
            }

            if (var3) {
               var1.petId = var0[var2].roleId;
               var1.stats[0] = var0[var2].stats[0];
               var1.stats[1] = var0[var2].stats[3];
               var1.stats[2] = var0[var2].stats[10];
               var1.stats[4] = var0[var2].stats[5];
               var1.stats[5] = var0[var2].stats[11];
               var1.stats[7] = var0[var2].stats[12];
               var1.stats[9] = var0[var2].stats[13];
               var1.stats[11] = var0[var2].stats[14];
               var1.stats[13] = var0[var2].stats[1];
               var1.stats[14] = var0[var2].stats[2];
               var1.stats[15] = var0[var2].stats[15];
               var1.skills = var0[var2].skills;
               storePetRecords(new PetInfo[]{var1});
            }
         }
      }
   }

   public static void growPetStats(PetInfo var0) {
      int var1 = var0.stats[3];
      var0.stats[2] = var0.stats[2] + var1;
      var1 = var0.stats[6];
      var0.stats[5] = var0.stats[5] + var1;
      var1 = var0.stats[8];
      var0.stats[7] = var0.stats[7] + var1;
      var1 = var0.stats[10];
      var0.stats[9] = var0.stats[9] + var1;
      var1 = var0.stats[12];
      var0.stats[11] = var0.stats[11] + var1;
      storePetRecords(new PetInfo[]{var0});
   }

   public static void growPetSpriteStats(RoleSprite var0) {
      for (int var1 = 0; myPets != null && var1 < myPets.length; var1++) {
         if (var0.petUid == myPets[var1].uid) {
            int var2 = myPets[var1].stats[3];
            var0.stats[10] = var0.stats[10] + var2;
            var2 = myPets[var1].stats[6];
            var0.stats[11] = var0.stats[11] + var2;
            var2 = myPets[var1].stats[8];
            var0.stats[12] = var0.stats[12] + var2;
            var2 = myPets[var1].stats[10];
            var0.stats[13] = var0.stats[13] + var2;
            var2 = myPets[var1].stats[12];
            var0.stats[14] = var0.stats[14] + var2;
            return;
         }
      }
   }

   public static boolean saveSlotSummary(byte var0) {
      slotUsed[var0] = 1;
      slotSceneNames[var0] = sceneName;
      slotHeroIds[var0] = heroId;
      slotHeroLevels[var0] = (short)party[0].stats[0];
      Calendar var1;
      int var2 = (var1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))).get(1);
      int var3 = var1.get(2);
      int var4 = var1.get(5);
      int var5 = var1.get(11);
      int var6 = var1.get(12);
      int var15 = var1.get(13);
      slotYears[var0] = (short)var2;
      slotMonths[var0] = (byte)(var3 + 1);
      slotDays[var0] = (byte)var4;
      slotHours[var0] = (byte)var5;
      slotMinutes[var0] = (byte)var6;
      slotSeconds[var0] = (byte)var15;
      RecordStore var14 = null;

      try {
         (var14 = RecordStore.openRecordStore(saveIndexStore, true)).closeRecordStore();
         RecordStore.deleteRecordStore(saveIndexStore);
         var14 = RecordStore.openRecordStore(saveIndexStore, true);
      } catch (RecordStoreFullException var11) {
         var11.printStackTrace();
      } catch (RecordStoreNotFoundException var12) {
         var12.printStackTrace();
      } catch (RecordStoreException var13) {
         var13.printStackTrace();
      }

      ByteArrayOutputStream var16 = new ByteArrayOutputStream();
      DataOutputStream var17 = new DataOutputStream(var16);

      try {
         for (int var18 = 0; var18 < 3; var18++) {
            var17.writeByte(slotUsed[var18]);
            if (slotUsed[var18] == 1) {
               var17.writeUTF(slotSceneNames[var18]);
               var17.writeShort(slotHeroIds[var18]);
               var17.writeShort(slotHeroLevels[var18]);
               var17.writeShort(slotYears[var18]);
               var17.writeByte(slotMonths[var18]);
               var17.writeByte(slotDays[var18]);
               var17.writeByte(slotHours[var18]);
               var17.writeByte(slotMinutes[var18]);
               var17.writeByte(slotSeconds[var18]);
            }
         }

         byte[] var19 = var16.toByteArray();
         var14.addRecord(var19, 0, var19.length);
         var14.closeRecordStore();
         return true;
      } catch (IOException var7) {
         var7.printStackTrace();
         return false;
      } catch (RecordStoreNotOpenException var8) {
         var8.printStackTrace();
         return false;
      } catch (RecordStoreFullException var9) {
         var9.printStackTrace();
         return false;
      } catch (RecordStoreException var10) {
         var10.printStackTrace();
         return false;
      }
   }

   public static boolean loadPurchaseState(String var0) {
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         RecordEnumeration var10;
         if ((var10 = (var1 = RecordStore.openRecordStore(var0, true)).enumerateRecords(null, null, false)).hasNextElement()) {
            int var11 = var10.nextRecordId();
            var2 = var1.getRecord(var11);
         }
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
         var8.printStackTrace();
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      }

      if (var2 != null) {
         ByteArrayInputStream var12 = new ByteArrayInputStream(var2);
         DataInputStream var13 = new DataInputStream(var12);

         try {
            PaidServiceMenu.a_String = var13.readUTF();
            PaidServiceMenu.b_String = var13.readUTF();
            PaidServiceMenu.isFullVersion = var13.readByte() == 1;
            PaidServiceMenu.hasTransform = var13.readByte() == 1;
            PaidServiceMenu.hasLuckyPack = var13.readByte() == 1;
            PaidServiceMenu.expMultiplier = var13.readByte();
            PaidServiceMenu.totalSpent = var13.readInt();
            musicVolume = var13.readByte();
            var1.closeRecordStore();
            return true;
         } catch (IOException var3) {
            var3.printStackTrace();
            return false;
         } catch (RecordStoreNotOpenException var4) {
            var4.printStackTrace();
            return false;
         } catch (RecordStoreFullException var5) {
            var5.printStackTrace();
            return false;
         } catch (RecordStoreException var6) {
            var6.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean readSaveRecord(String var0) {
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         RecordEnumeration var3;
         if ((var3 = (var1 = RecordStore.openRecordStore(var0, false)).enumerateRecords(null, null, false)).hasNextElement()) {
            int var4 = var3.nextRecordId();
            var2 = var1.getRecord(var4);
         }
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
         var8.printStackTrace();
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      }

      if (var2 != null) {
         ByteArrayInputStream var14 = new ByteArrayInputStream(var2);
         DataInputStream var15 = new DataInputStream(var14);

         try {
            storyPhase = var15.readByte();
            currentSceneId = var15.readShort();
            if (DeviceConfig.playMusic = var15.readByte() == 1) {
               System.out.println("true");
               World.playSceneMusic();
            } else {
               System.out.println("false");
               World.stopMusic();
            }

            short var38;
            if ((var38 = var15.readShort()) > 0) {
               removedRoleIds = new short[var38];

               for (int var46 = 0; var46 < var38; var46++) {
                  removedRoleIds[var46] = var15.readShort();
               }
            } else {
               removedRoleIds = null;
            }

            short var47;
            if ((var47 = var15.readShort()) > 0) {
               openedChestIds = new short[var47];

               for (int var16 = 0; var16 < var47; var16++) {
                  openedChestIds[var16] = var15.readShort();
               }
            } else {
               openedChestIds = null;
            }

            short var17;
            if ((var17 = var15.readShort()) > 0) {
               eventFlags = new int[var17];

               for (int var39 = 0; var39 < var17; var39++) {
                  eventFlags[var39] = var15.readShort();
               }
            } else {
               eventFlags = null;
            }

            if ((var38 = var15.readShort()) > 0) {
               questDoneFlags = new short[var38];

               for (int var18 = 0; var18 < var38; var18++) {
                  questDoneFlags[var18] = var15.readShort();
               }
            } else {
               questDoneFlags = null;
            }

            short var19;
            if ((var19 = var15.readShort()) > 0) {
               activeQuestIds = new short[var19];

               for (int var41 = 0; var41 < var19; var41++) {
                  activeQuestIds[var41] = var15.readShort();
               }
            }

            if ((var38 = var15.readShort()) > 0) {
               finishedQuestIds = new short[var38];

               for (int var20 = 0; var20 < var38; var20++) {
                  finishedQuestIds[var20] = var15.readShort();
               }
            }

            byte var21;
            if ((var21 = var15.readByte()) > 0) {
               partyRoleIds = new byte[var21];

               for (int var22 = 0; var22 < partyRoleIds.length; var22++) {
                  partyRoleIds[var22] = var15.readByte();
               }
            }

            byte var23;
            if ((var23 = var15.readByte()) > 0) {
               myPetSpeciesIds = new short[var23];

               for (int var24 = 0; var24 < myPetSpeciesIds.length; var24++) {
                  myPetSpeciesIds[var24] = var15.readShort();
               }
            }

            byte var25;
            if ((var25 = var15.readByte()) > 0) {
               myPetUids = new int[var25];

               for (int var26 = 0; var26 < myPetUids.length; var26++) {
                  myPetUids[var26] = var15.readInt();
               }
            }

            short var27;
            if ((var27 = var15.readShort()) > 0) {
               bagItems = new short[var27][2];

               for (int var28 = 0; var28 < bagItems.length; var28++) {
                  bagItems[var28][0] = var15.readShort();
                  bagItems[var28][1] = var15.readShort();
               }
            }

            equipBagIds = new short[6][];
            equipBag = new Equip[6][];

            for (int var29 = 0; var29 < 6; var29++) {
               if ((var38 = var15.readShort()) > 0) {
                  equipBagIds[var29] = new short[var38];
                  equipBag[var29] = new Equip[var38];

                  for (int var48 = 0; var48 < var38; var48++) {
                     byte var5 = var15.readByte();
                     equipBagIds[var29][var48] = (short)var5;
                     if (var5 > 0) {
                        var15.readByte();
                        equipBag[var29][var48] = new Equip(var5);
                        equipBag[var29][var48].gemCount = var15.readByte();
                        equipBag[var29][var48].gemAttack = var15.readShort();
                        equipBag[var29][var48].gemDefense = var15.readShort();
                        equipBag[var29][var48].gemSpeed = var15.readShort();
                        equipBag[var29][var48].gemCrit = var15.readShort();
                        equipBag[var29][var48].gemMp = var15.readShort();
                        equipBag[var29][var48].hp = var15.readShort();
                        equipBag[var29][var48].mp = var15.readShort();
                        equipBag[var29][var48].attack = var15.readShort();
                        equipBag[var29][var48].defense = var15.readShort();
                        equipBag[var29][var48].speed = var15.readShort();
                        equipBag[var29][var48].socketCount = var15.readShort();
                        equipBag[var29][var48].crit = var15.readShort();
                     }
                  }
               }
            }

            if ((money = var15.readInt()) < 0) {
               money = 99999;
            }

            byte var30;
            if ((var30 = var15.readByte()) > 0) {
               roleRecords = new RoleSaveRecord[var30];

               for (int var44 = 0; roleRecords != null && var44 < roleRecords.length; var44++) {
                  roleRecords[var44] = new RoleSaveRecord();
                  roleRecords[var44].roleId = var15.readByte();
                  byte var49;
                  if ((var49 = var15.readByte()) > 0) {
                     roleRecords[var44].stats = new int[var49];

                     for (int var52 = 0; var52 < var49; var52++) {
                        roleRecords[var44].stats[var52] = var15.readInt();
                     }
                  }

                  roleRecords[var44].elementValues = new byte[4];

                  for (int var53 = 0; var53 < 4; var53++) {
                     roleRecords[var44].elementValues[var53] = var15.readByte();
                  }

                  roleRecords[var44].attrGrowth = new byte[4][5];

                  for (int var54 = 0; var54 < 4; var54++) {
                     for (int var31 = 0; var31 < 5; var31++) {
                        roleRecords[var44].attrGrowth[var54][var31] = var15.readByte();
                     }
                  }

                  byte var55;
                  if ((var55 = var15.readByte()) > 0) {
                     roleRecords[var44].skillIds = new short[var55];
                     roleRecords[var44].skills = new Skill[var55];

                     for (int var32 = 0; var32 < roleRecords[var44].skillIds.length; var32++) {
                        var55 = var15.readShort();
                        roleRecords[var44].skillIds[var32] = var55;
                        roleRecords[var44].skills[var32] = new Skill();
                        roleRecords[var44].skills[var32].skillId = var55;
                        roleRecords[var44].skills[var32].level = var15.readByte();
                        roleRecords[var44].skills[var32].exp = var15.readInt();
                        roleRecords[var44].skills[var32].expToNext = var15.readInt();
                        roleRecords[var44].skills[var32].maxLevel = var15.readByte();
                     }
                  }

                  short var33;
                  if ((var33 = var15.readShort()) > 0) {
                     roleRecords[var44].equipIds = new short[var33];
                     roleRecords[var44].equips = new Equip[var33];

                     for (int var57 = 0; var57 < roleRecords[var44].equipIds.length; var57++) {
                        byte var6 = var15.readByte();
                        roleRecords[var44].equipIds[var57] = (short)var6;
                        if (var6 > 0) {
                           var15.readByte();
                           roleRecords[var44].equips[var57] = new Equip(var6);
                           roleRecords[var44].equips[var57].gemCount = var15.readByte();
                           roleRecords[var44].equips[var57].gemAttack = var15.readShort();
                           roleRecords[var44].equips[var57].gemDefense = var15.readShort();
                           roleRecords[var44].equips[var57].gemSpeed = var15.readShort();
                           roleRecords[var44].equips[var57].gemCrit = var15.readShort();
                           roleRecords[var44].equips[var57].gemMp = var15.readShort();
                           roleRecords[var44].equips[var57].hp = var15.readShort();
                           roleRecords[var44].equips[var57].mp = var15.readShort();
                           roleRecords[var44].equips[var57].attack = var15.readShort();
                           roleRecords[var44].equips[var57].defense = var15.readShort();
                           roleRecords[var44].equips[var57].speed = var15.readShort();
                           roleRecords[var44].equips[var57].socketCount = var15.readShort();
                           roleRecords[var44].equips[var57].crit = var15.readShort();
                        }
                     }
                  }

                  roleRecords[var44].x = var15.readShort();
                  roleRecords[var44].y = var15.readShort();
                  roleRecords[var44].direction = var15.readByte();
                  roleRecords[var44].jobLevel = var15.readByte();
               }
            }

            byte var45;
            if ((var45 = var15.readByte()) > 0) {
               petRecords = new PetSaveRecord[var45];

               for (int var50 = 0; petRecords != null && var50 < petRecords.length; var50++) {
                  petRecords[var50] = new PetSaveRecord();
                  petRecords[var50].petId = var15.readInt();
                  petRecords[var50].name = var15.readUTF();
                  byte var58;
                  if ((var58 = var15.readByte()) > 0) {
                     petRecords[var50].stats = new int[var58];

                     for (int var34 = 0; var34 < var58; var34++) {
                        petRecords[var50].stats[var34] = var15.readInt();
                     }
                  }

                  petRecords[var50].isDeployed = var15.readBoolean();
                  petRecords[var50].elementValues = new byte[4];

                  for (int var35 = 0; var35 < 4; var35++) {
                     petRecords[var50].elementValues[var35] = var15.readByte();
                  }

                  byte var36;
                  if ((var36 = var15.readByte()) > 0) {
                     petRecords[var50].skillIds = new short[var36];
                     petRecords[var50].skills = new Skill[var36];

                     for (int var59 = 0; var59 < var36; var59++) {
                        short var63 = var15.readShort();
                        petRecords[var50].skillIds[var59] = var63;
                        petRecords[var50].skills[var59] = new Skill();
                        petRecords[var50].skills[var59].skillId = var63;
                        petRecords[var50].skills[var59].level = var15.readByte();
                        petRecords[var50].skills[var59].exp = var15.readInt();
                        petRecords[var50].skills[var59].expToNext = var15.readInt();
                        petRecords[var50].skills[var59].maxLevel = var15.readByte();
                     }
                  }

                  petRecords[var50].uid = var15.readInt();
                  petRecords[var50].dropSpec = var15.readUTF();
                  petRecords[var50].ownerHeroId = var15.readByte();
                  petRecords[var50].implantSlots = new byte[2][2];

                  for (int var60 = 0; var60 < 2; var60++) {
                     petRecords[var50].implantSlots[var60][0] = var15.readByte();
                     petRecords[var50].implantSlots[var60][1] = var15.readByte();
                  }
               }
            }

            heroId = var15.readShort();
            if ((var47 = var15.readShort()) > 0) {
               discoveredPetIds = new short[var47];

               for (int var61 = 0; var61 < discoveredPetIds.length; var61++) {
                  discoveredPetIds[var61] = var15.readShort();
               }
            } else {
               discoveredPetIds = null;
            }

            short var62;
            if ((var62 = var15.readShort()) > 0) {
               newPetFlags = new boolean[var62];

               for (int var37 = 0; var37 < var62; var37++) {
                  newPetFlags[var37] = var15.readByte() == 1;
               }
            } else {
               newPetFlags = null;
            }

            reviveSceneId = var15.readShort();
            revivePoint = var15.readByte();
            PaidServiceMenu.rewardClaimed = var15.readBoolean();
            statMultiplier = var15.readInt();
            roleNames[0] = var15.readUTF();
            score = var15.readInt();
            var1.closeRecordStore();
         } catch (IOException var10) {
            var10.printStackTrace();
            return false;
         } catch (RecordStoreNotOpenException var11) {
            var11.printStackTrace();
            return false;
         } catch (RecordStoreFullException var12) {
            var12.printStackTrace();
            return false;
         } catch (RecordStoreException var13) {
            var13.printStackTrace();
            return false;
         }

         isRestoringSave = true;
         return true;
      } else {
         return false;
      }
   }

   public static boolean savePurchaseState(String var0) {
      RecordStore var1 = null;

      try {
         (var1 = RecordStore.openRecordStore(var0, true)).closeRecordStore();
         RecordStore.deleteRecordStore(var0);
         var1 = RecordStore.openRecordStore(var0, true);
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
         var8.printStackTrace();
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      }

      ByteArrayOutputStream var10 = new ByteArrayOutputStream();
      DataOutputStream var2 = new DataOutputStream(var10);

      try {
         var2.writeUTF(PaidServiceMenu.a_String);
         var2.writeUTF(PaidServiceMenu.b_String);
         var2.writeByte(PaidServiceMenu.isFullVersion ? 1 : 0);
         var2.writeByte(PaidServiceMenu.hasTransform ? 1 : 0);
         var2.writeByte(PaidServiceMenu.hasLuckyPack ? 1 : 0);
         var2.writeByte(PaidServiceMenu.expMultiplier);
         var2.writeInt(PaidServiceMenu.totalSpent);
         var2.writeByte(musicVolume);
         byte[] var11 = var10.toByteArray();
         var1.addRecord(var11, 0, var11.length);
         var1.closeRecordStore();
         return true;
      } catch (IOException var3) {
         var3.printStackTrace();
         return false;
      } catch (RecordStoreNotOpenException var4) {
         var4.printStackTrace();
         return false;
      } catch (RecordStoreFullException var5) {
         var5.printStackTrace();
         return false;
      } catch (RecordStoreException var6) {
         var6.printStackTrace();
         return false;
      }
   }

   public static boolean saveGame(byte var0) {
      savePurchaseState(purchaseStore);
      storeRoleRecords(party);
      storePetRecords(myPets);
      RecordStore var1 = null;
      String var2 = saveSlot1Store;
      if (var0 == 0) {
         var2 = saveSlot1Store;
      } else if (var0 == 1) {
         var2 = saveSlot2Store;
      } else if (var0 == 2) {
         var2 = saveSlot3Store;
      }

      try {
         (var1 = RecordStore.openRecordStore(var2, true)).closeRecordStore();
         RecordStore.deleteRecordStore(var2);
         var1 = RecordStore.openRecordStore(var2, true);
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
         var8.printStackTrace();
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      }

      ByteArrayOutputStream var14 = new ByteArrayOutputStream();
      DataOutputStream var15 = new DataOutputStream(var14);

      try {
         var15.writeByte(storyPhase);
         var15.writeShort(currentSceneId);
         var15.writeByte(DeviceConfig.playMusic ? 1 : 0);
         int var3 = (short)(removedRoleIds != null ? removedRoleIds.length : 0);
         var15.writeShort(var3);
         if (var3 > 0) {
            for (int var4 = 0; var4 < var3; var4++) {
               var15.writeShort(removedRoleIds[var4]);
            }
         }

         int var30 = openedChestIds != null ? openedChestIds.length : 0;
         var15.writeShort(var30);
         if (var30 > 0) {
            for (int var16 = 0; var16 < var30; var16++) {
               var15.writeShort(openedChestIds[var16]);
            }
         }

         var3 = eventFlags != null ? eventFlags.length : 0;
         var15.writeShort(var3);
         if (var3 > 0) {
            for (int var31 = 0; var31 < var3; var31++) {
               var15.writeShort(eventFlags[var31]);
            }
         }

         int var32 = (short)(questDoneFlags != null ? questDoneFlags.length : 0);
         var15.writeShort(var32);
         if (var32 > 0) {
            for (int var18 = 0; var18 < var32; var18++) {
               var15.writeShort(questDoneFlags[var18]);
            }
         }

         short var19 = (short)(activeQuestIds != null ? activeQuestIds.length : 0);
         var15.writeShort(var19);
         if (var19 > 0) {
            for (int var33 = 0; var33 < var19; var33++) {
               var15.writeShort(activeQuestIds[var33]);
            }
         }

         var32 = finishedQuestIds != null ? finishedQuestIds.length : 0;
         var15.writeShort(var32);
         if (var32 > 0) {
            for (int var20 = 0; var20 < var32; var20++) {
               var15.writeShort(finishedQuestIds[var20]);
            }
         }

         var15.writeByte(partyRoleIds.length);
         var15.write(partyRoleIds);
         if (myPetSpeciesIds != null) {
            var15.writeByte(myPetSpeciesIds.length);

            for (int var21 = 0; var21 < myPetSpeciesIds.length; var21++) {
               var15.writeShort(myPetSpeciesIds[var21]);
            }
         } else {
            var15.writeByte(0);
         }

         if (myPetUids != null) {
            var15.writeByte(myPetUids.length);

            for (int var22 = 0; var22 < myPetUids.length; var22++) {
               var15.writeInt(myPetUids[var22]);
            }
         } else {
            var15.writeByte(0);
         }

         if (bagItems != null) {
            var15.writeShort(bagItems.length);
            System.out.println("Lưu");

            for (int var23 = 0; var23 < bagItems.length; var23++) {
               var15.writeShort(bagItems[var23][0]);
               var15.writeShort(bagItems[var23][1]);
            }
         } else {
            var15.writeShort(0);
         }

         for (int var24 = 0; var24 < 6; var24++) {
            var32 = equipBagIds[var24] != null ? equipBagIds[var24].length : 0;
            var15.writeShort(var32);
            if (var32 > 0) {
               for (int var5 = 0; var5 < var32; var5++) {
                  short var6 = equipBagIds[var24][var5];
                  var15.writeByte(var6);
                  if (var6 > 0) {
                     var15.writeByte(equipBag[var24][var5].b_B);
                     var15.writeByte(equipBag[var24][var5].gemCount);
                     var15.writeShort(equipBag[var24][var5].gemAttack);
                     var15.writeShort(equipBag[var24][var5].gemDefense);
                     var15.writeShort(equipBag[var24][var5].gemSpeed);
                     var15.writeShort(equipBag[var24][var5].gemCrit);
                     var15.writeShort(equipBag[var24][var5].gemMp);
                     var15.writeShort(equipBag[var24][var5].hp);
                     var15.writeShort(equipBag[var24][var5].mp);
                     var15.writeShort(equipBag[var24][var5].attack);
                     var15.writeShort(equipBag[var24][var5].defense);
                     var15.writeShort(equipBag[var24][var5].speed);
                     var15.writeShort(equipBag[var24][var5].socketCount);
                     var15.writeShort(equipBag[var24][var5].crit);
                  }
               }
            }
         }

         if (money < 0) {
            money = 99999;
         }

         var15.writeInt(money);
         var15.writeByte(roleRecords != null ? roleRecords.length : 0);

         for (int var25 = 0; roleRecords != null && var25 < roleRecords.length; var25++) {
            var15.writeByte(roleRecords[var25].roleId);
            var15.writeByte(roleRecords[var25].stats.length);

            for (int var36 = 0; var36 < roleRecords[var25].stats.length; var36++) {
               var15.writeInt(roleRecords[var25].stats[var36]);
            }

            for (int var37 = 0; var37 < roleRecords[var25].elementValues.length; var37++) {
               var15.writeByte(roleRecords[var25].elementValues[var37]);
            }

            for (int var38 = 0; var38 < roleRecords[var25].attrGrowth.length; var38++) {
               for (int var45 = 0; var45 < roleRecords[var25].attrGrowth[var38].length; var45++) {
                  var15.writeByte(roleRecords[var25].attrGrowth[var38][var45]);
               }
            }

            if (roleRecords[var25].skillIds != null) {
               var15.writeByte(roleRecords[var25].skillIds.length);

               for (int var39 = 0; var39 < roleRecords[var25].skillIds.length; var39++) {
                  var15.writeShort(roleRecords[var25].skillIds[var39]);
                  var15.writeByte(roleRecords[var25].skills[var39].level);
                  var15.writeInt(roleRecords[var25].skills[var39].exp);
                  var15.writeInt(roleRecords[var25].skills[var39].expToNext);
                  var15.writeByte(roleRecords[var25].skills[var39].maxLevel);
               }
            } else {
               var15.writeByte(0);
            }

            if (roleRecords[var25].equipIds != null) {
               var15.writeShort(roleRecords[var25].equipIds.length);

               for (int var40 = 0; var40 < roleRecords[var25].equipIds.length; var40++) {
                  short var46 = roleRecords[var25].equipIds[var40];
                  var15.writeByte(var46);
                  if (var46 > 0) {
                     if (roleRecords[var25].equips == null) {
                        System.out.println("roleDatas[i].equip==null");
                     }

                     var15.writeByte(roleRecords[var25].equips[var40].b_B);
                     var15.writeByte(roleRecords[var25].equips[var40].gemCount);
                     var15.writeShort(roleRecords[var25].equips[var40].gemAttack);
                     var15.writeShort(roleRecords[var25].equips[var40].gemDefense);
                     var15.writeShort(roleRecords[var25].equips[var40].gemSpeed);
                     var15.writeShort(roleRecords[var25].equips[var40].gemCrit);
                     var15.writeShort(roleRecords[var25].equips[var40].gemMp);
                     var15.writeShort(roleRecords[var25].equips[var40].hp);
                     var15.writeShort(roleRecords[var25].equips[var40].mp);
                     var15.writeShort(roleRecords[var25].equips[var40].attack);
                     var15.writeShort(roleRecords[var25].equips[var40].defense);
                     var15.writeShort(roleRecords[var25].equips[var40].speed);
                     var15.writeShort(roleRecords[var25].equips[var40].socketCount);
                     var15.writeShort(roleRecords[var25].equips[var40].crit);
                  }
               }
            } else {
               var15.writeShort(0);
            }

            if (roleRecords[var25].roleId == party[0].roleId) {
               roleRecords[var25].x = party[0].x;
               roleRecords[var25].y = party[0].y;
               roleRecords[var25].direction = party[0].direction;
            }

            var15.writeShort(roleRecords[var25].x);
            var15.writeShort(roleRecords[var25].y);
            var15.writeByte(roleRecords[var25].direction);
            var15.writeByte(roleRecords[var25].jobLevel);
         }

         var15.writeByte(petRecords != null ? petRecords.length : 0);

         for (int var26 = 0; petRecords != null && var26 < petRecords.length; var26++) {
            var15.writeInt(petRecords[var26].petId);
            var15.writeUTF(petRecords[var26].name);
            System.out.println("=========== mypetdatas[i].id" + petRecords[var26].petId + "mypetdatas[i].petName" + petRecords[var26].name);
            var15.writeByte(petRecords[var26].stats.length);

            for (int var41 = 0; var41 < petRecords[var26].stats.length; var41++) {
               var15.writeInt(petRecords[var26].stats[var41]);
            }

            var15.writeBoolean(petRecords[var26].isDeployed);

            for (int var42 = 0; var42 < petRecords[var26].elementValues.length; var42++) {
               var15.writeByte(petRecords[var26].elementValues[var42]);
            }

            if (petRecords[var26].skillIds != null) {
               var15.writeByte(petRecords[var26].skillIds.length);

               for (int var43 = 0; var43 < petRecords[var26].skillIds.length; var43++) {
                  var15.writeShort(petRecords[var26].skillIds[var43]);
                  var15.writeByte(petRecords[var26].skills[var43].level);
                  var15.writeInt(petRecords[var26].skills[var43].exp);
                  var15.writeInt(petRecords[var26].skills[var43].expToNext);
                  var15.writeByte(petRecords[var26].skills[var43].maxLevel);
               }
            } else {
               var15.writeByte(0);
            }

            var15.writeInt(petRecords[var26].uid);
            var15.writeUTF(petRecords[var26].dropSpec);
            var15.writeByte(petRecords[var26].ownerHeroId);

            for (int var44 = 0; var44 < 2; var44++) {
               var15.writeByte(petRecords[var26].implantSlots[var44][0]);
               var15.writeByte(petRecords[var26].implantSlots[var44][1]);
            }
         }

         var15.writeShort(heroId);
         if (discoveredPetIds != null) {
            var15.writeShort(discoveredPetIds.length);

            for (int var27 = 0; var27 < discoveredPetIds.length; var27++) {
               var15.writeShort(discoveredPetIds[var27]);
            }
         } else {
            var15.writeShort(0);
         }

         if (newPetFlags != null) {
            var15.writeShort(newPetFlags.length);

            for (int var28 = 0; var28 < newPetFlags.length; var28++) {
               var15.writeByte(newPetFlags[var28] ? 1 : 0);
            }
         } else {
            var15.writeShort(0);
         }

         var15.writeShort(reviveSceneId);
         var15.writeByte(revivePoint);
         var15.writeBoolean(PaidServiceMenu.rewardClaimed);
         var15.writeInt(statMultiplier);
         var15.writeUTF(roleNames[0]);
         var15.writeInt(score);
         byte[] var29 = var14.toByteArray();
         var1.addRecord(var29, 0, var29.length);
         var1.closeRecordStore();
         return true;
      } catch (IOException var10) {
         var10.printStackTrace();
         return false;
      } catch (RecordStoreNotOpenException var11) {
         var11.printStackTrace();
         return false;
      } catch (RecordStoreFullException var12) {
         var12.printStackTrace();
         return false;
      } catch (RecordStoreException var13) {
         var13.printStackTrace();
         return false;
      }
   }

   public static boolean loadSlotSummaries() {
      RecordStore var0 = null;
      byte[] var1 = null;

      try {
         RecordEnumeration var2;
         if ((var2 = (var0 = RecordStore.openRecordStore(saveIndexStore, false)).enumerateRecords(null, null, false)).hasNextElement()) {
            int var12 = var2.nextRecordId();
            var1 = var0.getRecord(var12);
         }
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
         var8.printStackTrace();
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      }

      if (var1 != null) {
         ByteArrayInputStream var10 = new ByteArrayInputStream(var1);
         DataInputStream var11 = new DataInputStream(var10);

         try {
            for (int var13 = 0; var13 < 3; var13++) {
               slotUsed[var13] = var11.readByte();
               if (slotUsed[var13] == 1) {
                  slotSceneNames[var13] = var11.readUTF();
                  slotHeroIds[var13] = var11.readShort();
                  slotHeroLevels[var13] = var11.readShort();
                  slotYears[var13] = var11.readShort();
                  slotMonths[var13] = var11.readByte();
                  slotDays[var13] = var11.readByte();
                  slotHours[var13] = var11.readByte();
                  slotMinutes[var13] = var11.readByte();
                  slotSeconds[var13] = var11.readByte();
               }
            }

            var0.closeRecordStore();
            return true;
         } catch (IOException var3) {
            var3.printStackTrace();
            return false;
         } catch (RecordStoreNotOpenException var4) {
            var4.printStackTrace();
            return false;
         } catch (RecordStoreFullException var5) {
            var5.printStackTrace();
            return false;
         } catch (RecordStoreException var6) {
            var6.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   public static void resetGameData() {
      heroId = Utils.getShort(Utils.loadConfigText("/bin/sn.bin"), "heroId");
      removedRoleIds = null;
      openedChestIds = null;
      eventFlags = null;
      partyRoleIds = null;
      party = null;
      bagItems = null;
      equipBagIds = null;
      money = 0;
      roleRecords = null;
      equipBag = null;
      myPets = null;
      myPetSpeciesIds = null;
      petRecords = null;
      myPetUids = null;
      itemTable = null;
      sceneTable = null;
      roleTable = null;
      mazeTable = null;
      mazeEntryPoints = null;
      mazeGatePoints = null;
      skillTable = null;
      equipTable = null;
      partyRoleTable = null;
      activeQuests = null;
      questTable = null;
      questDoneFlags = null;
      activeQuestIds = null;
      finishedQuestIds = null;
      colorTableText = null;
   }

   public static void removeEquip(int var0) {
      Equip.loadTable();
      int var1 = Equip.slotTypes[var0 - 1] - 1;
      if (equipBagIds[var1] != null && equipBagIds[var1].length > 0) {
         int var2 = 0;

         for (int var3 = 0; var3 < equipBagIds[var1].length; var3++) {
            if (equipBagIds[var1][var3] == var0) {
               var2 = var3;
            }
         }

         equipBagIds[var1] = Utils.removeAt(equipBagIds[var1], var2);
         equipBag[var1] = removeEquipAt(equipBag[var1], var2);
      }
   }

   public static int getItemCount(int var0) {
      short var1 = 0;

      for (int var2 = 0; bagItems != null && var2 < bagItems.length; var2++) {
         if (bagItems[var2] != null && bagItems[var2][0] == var0) {
            var1 += bagItems[var2][1];
         }
      }

      return var1;
   }

   private static void healActorHp(BattleActor var0, int var1) {
      var0.hp += var1;
      if (var0.hp > var0.stats[10]) {
         var0.hp = var0.stats[10];
      }
   }

   private static void healActorMp(BattleActor var0, int var1) {
      var0.mp += var1;
      if (var0.mp > var0.stats[11]) {
         var0.mp = var0.stats[11];
      }
   }

   private static void healRoleHp(RoleSprite var0, int var1) {
      var0.stats[3] = var0.stats[3] + var1;
      if (var0.stats[3] > var0.stats[10]) {
         var0.stats[3] = var0.stats[10];
      }
   }

   private static void healRoleMp(RoleSprite var0, int var1) {
      var0.stats[5] = var0.stats[5] + var1;
      if (var0.stats[5] > var0.stats[11]) {
         var0.stats[5] = var0.stats[11];
      }
   }

   public static void learnPetSkill(PetInfo var0, int var1, int var2) {
      if (!Utils.contains(var0.skillIds, var1)) {
         var0.skillIds = Utils.append(var0.skillIds, var1);
         Skill var3 = new Skill(var1, 1, 4);
         var0.skills = appendSkill(var0.skills, var3);
      }
   }

   public static void learnRoleSkill(RoleSprite var0, int var1, int var2) {
      if (!Utils.contains(var0.skillIds, var1)) {
         var0.skillIds = Utils.append(var0.skillIds, var1);
         Skill var3 = new Skill(var1, var2, var0.roleId);
         var0.skills = appendSkill(var0.skills, var3);
      }
   }

   public static void forgetRoleSkill(RoleSprite var0, int var1) {
      if (Utils.contains(var0.skillIds, var1)) {
         for (int var2 = 0; var2 < var0.skillIds.length; var2++) {
            if (var0.skillIds[var2] == var1) {
               Skill[] var4 = var0.skills;
               Skill[] var10001;
               if (var0.skills != null && var4.length != 1) {
                  for (int var5 = var2; var5 < var4.length - 1; var5++) {
                     var4[var5] = var4[var5 + 1];
                  }

                  Skill[] var6 = new Skill[var4.length - 1];
                  System.arraycopy(var4, 0, var6, 0, var4.length - 1);
                  var10001 = var6;
               } else {
                  var10001 = null;
               }

               var0.skills = var10001;
               break;
            }
         }

         var0.skillIds = Utils.removeValue(var0.skillIds, var1);
      }
   }

   private static void healPetHp(PetInfo var0, int var1) {
      var0.stats[1] = var0.stats[1] + var1;
      if (var0.stats[1] > var0.stats[2]) {
         var0.stats[1] = var0.stats[2];
      }
   }

   private static void healPetMp(PetInfo var0, int var1) {
      var0.stats[4] = var0.stats[4] + var1;
      if (var0.stats[4] > var0.stats[5]) {
         var0.stats[4] = var0.stats[5];
      }
   }

   public static void useItemOnPet(PetInfo var0, int var1) {
      if (getItemCount(var1) > 0) {
         loadItemTable();
         if (itemNames[var1 - 1].equals("Thuốc hồi HP")) {
            if (var0.stats[1] > 0) {
               healPetHp(var0, 1000);
            }

            GameCanvas.instance.world.menu.showToast("HP +1000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi HP lớn")) {
            if (var0.stats[1] > 0) {
               healPetHp(var0, 3000);
            }

            GameCanvas.instance.world.menu.showToast("HP +3000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi MP")) {
            if (var0.stats[1] > 0) {
               healPetMp(var0, 500);
            }

            GameCanvas.instance.world.menu.showToast("MP +500", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi MP lớn")) {
            if (var0.stats[1] > 0) {
               healPetMp(var0, 1000);
            }

            GameCanvas.instance.world.menu.showToast("MP +1000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi toàn phần")) {
            if (var0.stats[1] > 0) {
               var0.stats[1] = var0.stats[2];
               var0.stats[4] = var0.stats[5];
            }

            GameCanvas.instance.world.menu.showToast("Hồi đầy HP và MP", 10);
         } else if (itemNames[var1 - 1].equals("Thịt kém")) {
            if (var0.stats[1] > 0) {
               healPetHp(var0, 150);
            }

            GameCanvas.instance.world.menu.showToast("HP +150", 10);
         } else if (itemNames[var1 - 1].equals("Thịt thường")) {
            if (var0.stats[1] > 0) {
               healPetHp(var0, 300);
            }

            GameCanvas.instance.world.menu.showToast("HP +300", 10);
         } else if (itemNames[var1 - 1].equals("Thịt ngon")) {
            if (var0.stats[1] > 0) {
               healPetHp(var0, 600);
            }

            GameCanvas.instance.world.menu.showToast("HP +600", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc Lạc Thiên")) {
            GameCanvas.instance.world.menu.showToast("Trung thành +5", 10);
         } else if (itemNames[var1 - 1].equals("Sách Công Kích")) {
            var0.stats[7] = var0.stats[7] + 2;
            GameCanvas.instance.world.menu.showToast("Tăng 2 ATK", 10);
         } else if (itemNames[var1 - 1].equals("Sách Phòng Thủ")) {
            var0.stats[9] = var0.stats[9] + 2;
            GameCanvas.instance.world.menu.showToast("Tăng 2 DEF", 10);
         } else if (itemNames[var1 - 1].equals("Sách Tấn Tật")) {
            var0.stats[11] = var0.stats[11] + 2;
            GameCanvas.instance.world.menu.showToast("Tăng 2 điểm nhanh nhẹn", 10);
         } else if (itemNames[var1 - 1].equals("Sách Sinh Mệnh")) {
            var0.stats[2] = var0.stats[2] + 5;
            GameCanvas.instance.world.menu.showToast("Tăng 5 HP tối đa", 10);
         } else if (itemNames[var1 - 1].equals("Sách Tụ Khí")) {
            var0.stats[5] = var0.stats[5] + 5;
            GameCanvas.instance.world.menu.showToast("Tăng 5 MP tối đa", 10);
         }

         if (itemConsumable[findItemIndex(var1)] == 1) {
            removeItem(var1, 1);
         }

         storePetRecords(new PetInfo[]{var0});
      }
   }

   public static void useItemInBattle(BattleActor var0, int var1) {
      if (getItemCount(var1) > 0) {
         loadItemTable();
         if (!itemNames[var1 - 1].equals("Thuốc Lạc Thiên")) {
            if (itemNames[var1 - 1].equals("Thuốc hồi HP")) {
               if (var0.stats[3] > 0) {
                  healActorHp(var0, 1000);
               }
            } else if (itemNames[var1 - 1].equals("Thuốc hồi HP lớn")) {
               if (var0.stats[3] > 0) {
                  healActorHp(var0, 3000);
               }
            } else if (itemNames[var1 - 1].equals("Thuốc hồi MP")) {
               if (var0.stats[5] > 0) {
                  healActorMp(var0, 500);
               }
            } else if (itemNames[var1 - 1].equals("Thuốc hồi MP lớn")) {
               if (var0.stats[5] > 0) {
                  healActorMp(var0, 1000);
               }
            } else if (itemNames[var1 - 1].equals("Thuốc hồi toàn phần")) {
               if (var0.hp > 0) {
                  var0.hp = var0.stats[10];
                  var0.mp = var0.stats[11];
               }
            } else if (itemNames[var1 - 1].equals("Thịt kém")) {
               if (var0.stats[3] > 0) {
                  healActorHp(var0, 150);
               }
            } else if (itemNames[var1 - 1].equals("Thịt thường")) {
               if (var0.stats[3] > 0) {
                  healActorHp(var0, 300);
               }
            } else if (itemNames[var1 - 1].equals("Thịt ngon")) {
               if (var0.stats[3] > 0) {
                  healActorHp(var0, 600);
               }
            } else if (itemNames[var1 - 1].equals("Cola vạn năng") && GameCanvas.instance.world.battle != null) {
               GameCanvas.instance.world.battle.clearStatus(var0);
            }
         }

         if (itemConsumable[findItemIndex(var1)] == 1) {
            removeItem(var1, 1);
         }
      }
   }

   public static void useItemOnRole(RoleSprite var0, int var1) {
      boolean var2 = false;
      if (getItemCount(var1) > 0) {
         loadItemTable();
         if (itemNames[var1 - 1].equals("Thuốc hồi HP")) {
            if (var0.stats[3] > 0) {
               healRoleHp(var0, 1000);
            }

            GameCanvas.instance.world.menu.showToast("HP +1000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi HP lớn")) {
            if (var0.stats[3] > 0) {
               healRoleHp(var0, 3000);
            }

            GameCanvas.instance.world.menu.showToast("HP +3000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi MP")) {
            if (var0.stats[5] > 0) {
               healRoleMp(var0, 500);
            }

            GameCanvas.instance.world.menu.showToast("MP +500", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi MP lớn")) {
            if (var0.stats[5] > 0) {
               healRoleMp(var0, 1000);
            }

            GameCanvas.instance.world.menu.showToast("MP +1000", 10);
         } else if (itemNames[var1 - 1].equals("Thuốc hồi toàn phần")) {
            if (var0.stats[3] > 0) {
               var0.stats[3] = var0.stats[10];
               var0.stats[5] = var0.stats[11];
            }

            GameCanvas.instance.world.menu.showToast("Hồi đầy HP và MP", 10);
         } else if (itemNames[var1 - 1].equals("Thịt kém")) {
            if (var0.stats[3] > 0) {
               healRoleHp(var0, 150);
            }

            GameCanvas.instance.world.menu.showToast("HP +150", 10);
         } else if (itemNames[var1 - 1].equals("Thịt thường")) {
            if (var0.stats[3] > 0) {
               healRoleHp(var0, 300);
            }

            GameCanvas.instance.world.menu.showToast("HP +300", 10);
         } else if (itemNames[var1 - 1].equals("Thịt ngon")) {
            if (var0.stats[3] > 0) {
               healRoleHp(var0, 600);
            }

            GameCanvas.instance.world.menu.showToast("HP +600", 10);
         } else if (itemNames[var1 - 1].equals("Vé số sủng vật")) {
            if (GameUtil.randomRange(0, 100) <= 20) {
               GameCanvas.instance.world.menu.showToast("Không nhận được gì", 10);
            } else {
               addPet(GameUtil.randomRange(1, 120), -1);
               GameMenu.pets = null;
               GameMenu.pets = GameUtil.a_APetInfo();
               GameCanvas.instance.world.menu.showToast("Nhận sủng vật: " + myPets[myPets.length - 1].name, 10);
            }
         } else if (itemNames[var1 - 1].equals("Cuộn hồi thành")) {
            if (townReturnPoint != null) {
               World.entryPointIndex = townReturnPoint[1];
               GameCanvas.instance.world.changeScene(townReturnPoint[0]);
               MenuList.closeMenu();
               GameCanvas.instance.world.script.advance(0);
            } else {
               var2 = true;
               GameCanvas.instance.world.menu.showToast("Không dùng được ở cảnh này", 10);
            }
         } else if (itemNames[var1 - 1].equals("Sách Công Kích")) {
            var0.stats[7] = var0.stats[7] + 2;
            GameCanvas.instance.world.menu.showToast("Tăng 2 ATK", 10);
         } else if (itemNames[var1 - 1].equals("Sách Phòng Thủ")) {
            var0.stats[8] = var0.stats[8] + 2;
            GameCanvas.instance.world.menu.showToast("Tăng 2 DEF", 10);
         } else if (!itemNames[var1 - 1].equals("Sách Ma Công") && !itemNames[var1 - 1].equals("Sách Ma Kháng")) {
            if (itemNames[var1 - 1].equals("Sách Tấn Tật")) {
               var0.stats[9] = var0.stats[9] + 2;
               GameCanvas.instance.world.menu.showToast("Tăng 2 điểm nhanh nhẹn", 10);
            } else if (itemNames[var1 - 1].equals("Sách Sinh Mệnh")) {
               var0.stats[4] = var0.stats[4] + 5;
               GameCanvas.instance.world.menu.showToast("Tăng 5 HP tối đa", 10);
            } else if (itemNames[var1 - 1].equals("Sách Tụ Khí")) {
               var0.stats[6] = var0.stats[6] + 5;
               GameCanvas.instance.world.menu.showToast("Tăng 5 MP tối đa", 10);
            }
         }

         if (itemConsumable[findItemIndex(var1)] == 1 && !var2) {
            removeItem(var1, 1);
         }

         refreshRoleStats(var0);
      }
   }

   public static void addEquip(int var0, int var1) {
      short var3 = 0;
      if (equipBagIds != null) {
         for (int var2 = 0; var2 < equipBagIds.length; var2++) {
            if (equipBagIds[var2] != null) {
               var3 = (short)(var3 + equipBagIds[var2].length);
            }
         }
      }

      if (var3 < GameSettings.maxItemTypes) {
         if (equipTable == null) {
            equipTable = Utils.getStringTable(Utils.loadConfigText("/bin/equip.bin"), "equip", "equipend", null, "\t");
         }

         if (equipBagIds == null) {
            equipBagIds = new short[6][];
         }

         equipBagIds[Utils.parseInt(equipTable[var0 - 1][2]) - 1] = Utils.append(equipBagIds[Utils.parseInt(equipTable[var0 - 1][2]) - 1], (short)var0);
         if (equipBag == null) {
            equipBag = new Equip[6][];
         }

         Equip var4 = new Equip(var0);
         equipBag[Utils.parseInt(equipTable[var0 - 1][2]) - 1] = appendEquip(equipBag[Utils.parseInt(equipTable[var0 - 1][2]) - 1], var4);
      } else {
         GameCanvas.instance.showToast("Túi đồ đã đầy", 10);
      }
   }

   public static boolean hasEventFlag(int var0) {
      return Utils.contains(eventFlags, var0);
   }

   public static void applyDefeatPenalty() {
      if (!PaidServiceMenu.hasLuckyPack) {
         money /= 2;
      }

      for (int var0 = 0; party != null && var0 < party.length; var0++) {
         if (party[var0].stats != null) {
            party[var0].stats[3] = party[var0].stats[10];
            party[var0].stats[5] = party[var0].stats[11];
         }
      }
   }

   public static void removePetRecord(int var0) {
      for (int var1 = 0; petRecords != null && var1 < petRecords.length; var1++) {
         if (petRecords[var1].uid == var0) {
            PetSaveRecord var3 = petRecords[var1];
            boolean var4 = false;

            for (int var2 = 0; petRecords != null && var2 < petRecords.length; var2++) {
               if (var4) {
                  if (var2 < petRecords.length - 1) {
                     petRecords[var2] = petRecords[var2 + 1];
                  } else {
                     petRecords[var2] = null;
                  }
               } else if (petRecords[var2] == var3 && var2 < petRecords.length - 1) {
                  petRecords[var2] = petRecords[var2 + 1];
                  var4 = true;
               }
            }

            PetSaveRecord[] var5 = new PetSaveRecord[petRecords.length - 1];
            System.arraycopy(petRecords, 0, var5, 0, petRecords.length - 1);
            petRecords = var5;
            if (var5.length == 0) {
               petRecords = null;
            }

            return;
         }
      }
   }

   public static void removePet(int var0) {
      for (int var1 = 0; myPets != null && var1 < myPets.length; var1++) {
         if (myPets[var1].uid == var0) {
            PetInfo var3 = myPets[var1];
            boolean var4 = false;

            for (int var2 = 0; myPets != null && var2 < myPets.length; var2++) {
               if (var4) {
                  if (var2 < myPets.length - 1) {
                     myPets[var2] = myPets[var2 + 1];
                  } else {
                     myPets[var2] = null;
                  }
               } else if (myPets[var2] == var3 && var2 < myPets.length - 1) {
                  myPets[var2] = myPets[var2 + 1];
                  var4 = true;
               }
            }

            PetInfo[] var6 = new PetInfo[myPets.length - 1];
            System.arraycopy(myPets, 0, var6, 0, myPets.length - 1);
            myPets = var6;
            if (var6.length == 0) {
               myPets = null;
            }

            for (int var5 = 0; var5 < myPetUids.length; var5++) {
               if (myPetUids[var5] == var3.uid) {
                  myPetSpeciesIds = Utils.removeAt(myPetSpeciesIds, var5);
                  myPetUids = Utils.removeAt(myPetUids, var5);
                  return;
               }
            }

            return;
         }
      }
   }

   public static void keepLeaderOnly(RoleSprite[] var0) {
      RoleSprite[] var1 = new RoleSprite[1];
      System.arraycopy(var0, 0, var1, 0, 1);
      party = var1;
   }

   public static void removePartyMember(int var0) {
      for (int var1 = 0; var1 < party.length; var1++) {
         if (party[var1].roleId == var0) {
            RoleSprite var3 = party[var1];
            boolean var4 = false;

            for (int var2 = 0; party != null && var2 < party.length; var2++) {
               if (var4) {
                  if (var2 < party.length - 1) {
                     party[var2] = party[var2 + 1];
                  } else {
                     party[var2] = null;
                  }
               } else if (party[var2] == var3 && var2 < party.length - 1) {
                  party[var2] = party[var2 + 1];
                  var4 = true;
               }
            }

            RoleSprite[] var6 = new RoleSprite[party.length - 1];
            System.arraycopy(party, 0, var6, 0, party.length - 1);
            party = var6;
            var6[0].resetFollowTrail();
            var3.setDirection(0);
            var3.mapWidth = World.mapWidth;
            var3.mapHeight = World.mapHeight;

            for (int var5 = 0; var5 < partyRoleIds.length; var5++) {
               if (partyRoleIds[var5] == var3.roleId) {
                  partyRoleIds = Utils.removeAt(partyRoleIds, var5);
                  return;
               }
            }

            return;
         }
      }
   }

   private static void removeQuestById(int var0) {
      for (int var1 = 0; var1 < activeQuests.length; var1++) {
         if (activeQuests[var1].id == var0) {
            removeQuest(activeQuests[var1]);
            return;
         }
      }
   }

   public static void removeQuest(Quest var0) {
      boolean var1 = false;

      for (int var2 = 0; activeQuests != null && var2 < activeQuests.length; var2++) {
         if (var1) {
            if (var2 < activeQuests.length - 1) {
               activeQuests[var2] = activeQuests[var2 + 1];
            } else {
               activeQuests[var2] = null;
            }
         } else if (activeQuests[var2] == var0 && var2 < activeQuests.length - 1) {
            activeQuests[var2] = activeQuests[var2 + 1];
            var1 = true;
         }
      }

      Quest[] var3 = new Quest[activeQuests.length - 1];
      System.arraycopy(activeQuests, 0, var3, 0, activeQuests.length - 1);
      activeQuests = var3;
   }

   public static boolean hasEquip(int var0) {
      for (int var1 = 0; equipBagIds != null && var1 < equipBagIds.length; var1++) {
         for (int var2 = 0; equipBagIds[var1] != null && var2 < equipBagIds[var1].length; var2++) {
            if (equipBagIds[var1][var2] == var0) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean hasSkill(RoleSprite var0, int var1) {
      for (int var2 = 0; var0.skills != null && var2 < var0.skills.length; var2++) {
         if (var0.skills[var2].skillId == var1) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasPetSpecies(int var0) {
      for (int var1 = 0; myPets != null && var1 < myPets.length; var1++) {
         if (myPets[var1].ownerHeroId == heroId && myPets[var1].petId == var0) {
            return true;
         }
      }

      return false;
   }

   public static Equip[] removeEquipAt(Equip[] var0, int var1) {
      if (var0 != null && var0.length != 1) {
         for (int var2 = var1; var2 < var0.length - 1; var2++) {
            var0[var2] = var0[var2 + 1];
         }

         Equip[] var3 = new Equip[var0.length - 1];
         System.arraycopy(var0, 0, var3, 0, var0.length - 1);
         return var3;
      } else {
         return null;
      }
   }

   private static Skill[] appendSkill(Skill[] var0, Skill var1) {
      if (var0 == null) {
         (var0 = new Skill[1])[0] = var1;
         return var0;
      } else {
         Skill[] var2 = new Skill[var0.length + 1];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         var2[var2.length - 1] = var1;
         return var2;
      }
   }

   public static Equip[] appendEquip(Equip[] var0, Equip var1) {
      if (var0 == null) {
         (var0 = new Equip[1])[0] = var1;
         return var0;
      } else {
         Equip[] var2 = new Equip[var0.length + 1];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         var2[var2.length - 1] = var1;
         return var2;
      }
   }

   public static int findSkillIndex(int var0) {
      if (Skill.ids == null) {
         Skill.loadTable();
      }

      int var1 = -1;

      for (int var2 = 0; var2 < Skill.ids.length; var2++) {
         if (Skill.ids[var2] == var0) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public static byte[][] getGeneItems() {
      byte[][] var0 = null;

      for (int var1 = 0; bagItems != null && var1 < bagItems.length; var1++) {
         int var2;
         if ((var2 = findItemIndex(bagItems[var1][0])) != -1 && itemTypes[var2] == 6) {
            byte[] var3 = Utils.append(Utils.append(null, bagItems[var1][0]), bagItems[var1][1]);
            var0 = Utils.append(var0, var3);
         }
      }

      return var0;
   }

   public static int findItemIndex(int var0) {
      if (itemIds == null) {
         loadItemTable();
      }

      int var1 = -1;

      for (int var2 = 0; var2 < itemIds.length; var2++) {
         if (itemIds[var2] == var0) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public static int findQuestIndex(int var0) {
      if (questTable == null) {
         questTable = Utils.getStringTable(Utils.loadConfigText("/bin/task.bin"), "task", "taskend", null, "\t");
      }

      int var1 = -1;

      for (int var2 = 0; var2 < questTable.length; var2++) {
         if (Utils.parseInt(questTable[var2][0]) == var0) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public static int findEquipIndex(int var0) {
      if (Equip.ids == null) {
         Equip.loadTable();
      }

      int var1 = -1;

      for (int var2 = 0; var2 < Equip.ids.length; var2++) {
         if (Equip.ids[var2] == var0) {
            var1 = var2;
            break;
         }
      }

      return var1;
   }

   public static boolean acceptQuest(short var0) {
      if (!Utils.contains(activeQuestIds, var0)) {
         activeQuestIds = Utils.append(activeQuestIds, var0);
         questDoneFlags = Utils.append(questDoneFlags, 0);
         appendQuest(createQuest(var0, -1));
      }

      return true;
   }

   public static boolean finishQuest(short var0, boolean var1) {
      var1 = -1;

      for (int var2 = 0; activeQuestIds != null && var2 < activeQuestIds.length; var2++) {
         if (activeQuestIds[var2] == var0) {
            var1 = var2;
            break;
         }
      }

      if (var1 >= 0) {
         String[] var9 = null;
         if (!activeQuests[var1].reward.equals("-1")) {
            String[] var3 = Utils.split(activeQuests[var1].reward, "|");

            for (int var4 = 0; var4 < var3.length; var4++) {
               short[] var5;
               if ((var5 = Utils.parseShortArray(var3[var4], ","))[0] == 1) {
                  addItem(var5[1], var5[2]);
                  loadItemTable();
                  var9 = Utils.append(var9, "Nhận " + itemNames[findItemIndex(var5[1])] + "  x" + var5[2]);
               } else if (var5[0] == 2) {
                  addEquip(var5[1], -1);
                  Equip.loadTable();
                  var9 = Utils.append(var9, "Nhận " + Equip.names[findEquipIndex(var5[1])] + "  x1");
               } else if (var5[0] == 3) {
                  money = money + var5[1];
                  var9 = Utils.append(var9, "Nhận vàng+" + var5[1]);
               } else if (var5[0] == 4) {
                  addPet(var5[1], -1);
                  short var7 = var5[1];
                  String[][] var12 = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
                  int var10 = var7 - 1;
                  String var11 = var12[var10][0];
                  var9 = Utils.append(var9, "Nhận sủng vật [" + var11 + "]");
               } else if (var5[0] == 5) {
                  party[0].stats[1] = party[0].stats[1] + var5[1];

                  for (int var6 = 0; myPets != null && var6 < myPets.length; var6++) {
                     if (myPets[var6].ownerHeroId == heroId && myPets[var6].isDeployed) {
                        myPets[var6].stats[13] = myPets[var6].stats[13] + var5[1];
                        World.levelUpPets(new PetInfo[]{myPets[var6]});
                     }
                  }

                  var9 = Utils.append(var9, "Nhận EXP  +" + var5[1]);
                  GameCanvas.instance.world.checkLevelUp(party[0]);
                  if (GameCanvas.instance.world.levelUpEffect) {
                     var9 = Utils.append(Utils.append(var9, party[0].name + " đã lên cấp"), "Nhận 10 điểm");
                     party[0].stats[16] = party[0].stats[16] + 10;
                  }
               }
            }
         }

         if (activeQuests[var1].repeatable == 0 && !Utils.contains(finishedQuestIds, var0)) {
            finishedQuestIds = Utils.append(finishedQuestIds, var0);
         }

         activeQuestIds = Utils.removeAt(activeQuestIds, var1);
         questDoneFlags = Utils.removeAt(questDoneFlags, var1);
         removeQuestById(var0);
         GameCanvas.instance.world.showTicker(var9);
         score += 5;
      } else {
         System.out.println("Nhiệm vụ cần xóa không tồn tại trong danh sách nhiệm vụ!");
      }

      return true;
   }
}
