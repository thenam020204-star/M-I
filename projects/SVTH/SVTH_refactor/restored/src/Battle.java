import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Battle {
   public BattleActor[] actors = new BattleActor[20];
   public int phase;
   public int smsContext = 0;
   private int targetCursor;
   boolean isLoaded = false;
   private int turnIndex;
   private boolean petSummoned;
   private static byte[][] popupOffsets;
   private int e_I = 0;
   private Image[] statusPanelImages = new Image[3];
   private Image digits;
   private Image digitsBig;
   private Image elementIcons;
   private Image barImage;
   private Image roleNameImage;
   private Image f_Image;
   private Image statIcons;
   private Image gainArrow;
   private Image namePlate;
   private Image damageDigits;
   private AnimSprite heroFace;
   private int[][] heroRing = new int[][]{{-17, -65}, {28, -40}, {31, -1}, {1, 27}, {-45, 24}, {-68, -10}, {-59, -49}};
   private int[][] heroRingTurning = new int[][]{{8, -57}, {32, -17}, {15, 21}, {-24, 31}, {-56, 12}, {-62, -25}, {-43, -60}};
   private int[][] petRing = new int[][]{{-17, -65}, {30, -15}, {-17, 32}, {-60, -17}};
   private int[][] petRingTurning = new int[][]{{25, -30}, {30, 15}, {-45, 15}, {-45, -30}};
   private Image commandIcons;
   private Image commandIconsDim;
   private Image commandLabels;
   private Image digitsSmall;
   private int commandIndex;
   private boolean isRingTurningRight;
   private boolean isRingTurningLeft;
   private int menuMode;
   private Image menuHeader;
   private Image menuTitle;
   private Image softKeyLabels;
   private Image listPanel;
   private Image infoPanel;
   private Image footerPanel;
   private Image silenceIcon;
   private Image buffIcon;
   private int[] menuRect;
   private int menuHeight;
   private MenuList itemList;
   private MenuList skillList;
   private MenuList skillLevelList;
   private MenuList petList;
   private TextBlock infoText;
   private int commandSlot;
   private int actingSlot;
   private int[][] actionPlans;
   private short[][] turnOrder;
   private int cursorBob;
   private Image background;
   private AnimSprite ringCenter;
   private Image arrowImage;
   private Image starImage;
   private Image introImage;
   private AnimSprite loadingSprite;
   private int shownLoadProgress;
   private int loadProgress;
   private short[] allyX;
   private short[] allyY;
   public RoleSprite[] party;
   public PetInfo[] petRoster;
   private short[] enemyX;
   private short[] enemyY;
   private byte enemyCount;
   private String[] enemyNames;
   private short[][] dropItemIds;
   private short[][] dropItemRates;
   private short[][] dropEquipIds;
   private short[][] dropEquipRates;
   private short[] enemyRoleIds;
   public short[] enemyVisible;
   private String[][] petTable;
   private String[][] starTable;
   public short[] enemyIds;
   public short[] enemyLevels;
   public boolean isStoryBattle;
   private static byte b_B;
   private short[][] usableItems;
   private short[] passiveSkillIds;
   private DialogBox dialog;
   private byte tutorialStep;
   private String[] dialogLines;
   private byte dialogMode;
   private int allyTargetIndex;
   private AnimSprite petPreview;
   private AnimSprite summonEffect;
   private int[] b_AI;
   private byte resultPage;
   private Vector levelUps;
   private int levelUpIndex;
   private String[] rewardItemLines;
   private String[] rewardExpLines;
   private static short rewardScroll;
   private String teamData;
   private int textColor;
   private static int introSlide = 0;
   private int textOutlineColor;
   boolean showBackground;
   private int popupFrame;
   private int comboIndex;
   private int scoutedSlot;
   private AnimSprite scoutedSprite;
   private byte[] scoutedStars;
   private boolean roundEffectsShown;
   private short[][] particles;
   private Image grainImage1;
   private Image grainImage2;
   private int summonSlot;
   private boolean isSummoning;
   private Image touchBackImage;
   private int listHeight;
   private int infoHeight;
   private int footerHeight;
   private int menuWidth;
   private int critFlashTicks;
   private int chosenPetIndex;
   private String[] defeatLines;
   private byte defeatLineIndex;
   boolean critFlash;

   public Battle() {
      String[] var10000 = new String[]{"Đánh", "Phòng thủ", "Kỹ năng", "Vật phẩm", "Sủng vật", "Hạ ngay", "Chạy", "Đánh", "Phòng thủ", "Kỹ năng", "Lui"};
      this.commandIndex = 0;
      this.isRingTurningRight = false;
      this.isRingTurningLeft = false;
      this.menuHeight = 194;
      this.commandSlot = 0;
      this.actingSlot = 0;
      this.actionPlans = new int[20][5];
      this.shownLoadProgress = 0;
      this.loadProgress = 0;
      this.enemyX = null;
      this.enemyY = null;
      this.enemyCount = 0;
      this.passiveSkillIds = new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38};
      this.allyTargetIndex = 0;
      this.summonEffect = null;
      this.levelUps = new Vector();
      this.levelUpIndex = 0;
      this.textColor = 16777215;
      this.textOutlineColor = 556710;
      this.showBackground = true;
      this.popupFrame = 0;
      this.comboIndex = 0;
      this.scoutedSlot = 0;
      this.roundEffectsShown = false;
      this.particles = null;
      int[] var1 = new int[]{15629412, 6868447};
      this.summonSlot = 0;
      this.isSummoning = false;
      int[] var2 = new int[]{16660536, 12175614, 15964940, 9693914};
      this.listHeight = 0;
      this.infoHeight = 0;
      this.footerHeight = 0;
      this.menuWidth = 0;
      this.critFlashTicks = 0;
      this.chosenPetIndex = 0;
      this.critFlash = false;
   }

   public final boolean loadResources() {
      try {
         Thread.sleep(0L);
      } catch (InterruptedException var5) {
         var5.printStackTrace();
      }

      this.menuWidth = 191;
      this.menuRect = new int[]{(DeviceConfig.screenSize[0] - 193) / 2, (DeviceConfig.screenSize[1] - 255) / 2, 193, 28};
      this.menuHeight = 200;
      this.createMenuPanels(90, this.menuHeight - 90);
      this.targetCursor = 10;
      this.loadingSprite = new AnimSprite();
      this.loadingSprite.load("/av/long1.av");
      if (this.summonEffect == null) {
         this.summonEffect = new AnimSprite();
         this.summonEffect.load("/ft/tanshe.av");
      }

      if (this.grainImage1 == null) {
         this.grainImage1 = ResourceCache.loadImage("/pic/grain1.gf");
      }

      if (this.grainImage2 == null) {
         this.grainImage2 = ResourceCache.loadImage("/pic/grain2.gf");
      }

      if (this.starImage == null) {
         this.starImage = ResourceCache.loadImage("/pic/star.gf", 2);
      }

      if (this.silenceIcon == null) {
         this.silenceIcon = ResourceCache.loadImage("/icon/12.gf");
      }

      if (this.buffIcon == null) {
         this.buffIcon = ResourceCache.loadImage("/icon/10.gf");
      }

      if (Skill.names == null) {
         Skill.loadTable();
      }

      if (GameData.skillTable == null) {
         GameData.skillTable = Utils.getStringTable(Utils.loadConfigText("/bin/skill.bin"), "skill:", "skillend", null, "\t");
      }

      this.loadProgress = 10;
      this.background = ResourceCache.loadImage("/ftmap/" + GameData.battleBackground + ".jpg");
      this.introImage = ResourceCache.loadImage("/pic/E020q03.gf");
      this.digitsSmall = ResourceCache.loadImage("/pic/number01.gf", 2);
      this.ringCenter = new AnimSprite();
      this.ringCenter.load("/pic/D25.av");
      this.commandIcons = ResourceCache.loadImage("/pic/D26.gf");
      byte[] var1 = PngRecolor.scaleBrightness(Utils.loadImageBytes("/pic/D26.gf"), 50);
      this.commandIconsDim = Image.createImage(var1, 0, var1.length);
      this.commandLabels = ResourceCache.loadImage("/pic/D27.gf");
      this.statusPanelImages[0] = ResourceCache.loadImage("/pic/D28.gf");
      this.statusPanelImages[1] = ResourceCache.loadImage("/pic/D32.gf");
      this.statusPanelImages[2] = ResourceCache.loadImage("/pic/D33.gf");
      String var10001 = "/pic/D09.gf";
      this.roleNameImage = null;
      this.loadProgress = 20;
      this.digits = ResourceCache.loadImage("/pic/number01.gf", 2);
      this.digitsBig = ResourceCache.loadImage("/pic/number03.gf", 2);
      ResourceCache.loadImage("/pic/number04.gf", 2);
      this.damageDigits = ResourceCache.loadImage("/pic/battleNumber1.gf");
      if (this.f_Image == null) {
         this.f_Image = ResourceCache.loadImage("/pic/tzjy.gf");
      }

      this.elementIcons = ResourceCache.loadImage("/pic/D02.gf", 2);
      this.barImage = ResourceCache.loadImage("/pic/D05.gf");
      this.statIcons = ResourceCache.loadImage("/pic/D13.gf");
      this.namePlate = ResourceCache.loadImage("/pic/D29.gf");
      this.gainArrow = ResourceCache.loadImage("/pic/up_down.gf");
      this.arrowImage = ResourceCache.loadImage("/pic/arrow.gf", 2);
      ResourceCache.loadImage("/pic/battlelist.gf");
      ResourceCache.loadImage("/pic/battlelist1.gf");
      this.loadProgress = 30;
      this.resetActionPlans();
      String var6;
      String var2 = Utils.substringBetween(var6 = Utils.loadConfigText("/bin/battlepos.bin"), "team:", "teamEnd");
      String var7 = Utils.substringBetween(var6, "enemy:", "enemyEnd");
      String var3 = Utils.loadConfigText("/bin/enemy.bin");
      if (this.isStoryBattle) {
         if (GameCanvas.instance.world.isTutorialBattle) {
            if ((var3 = Utils.substringBetween(var3, "teachMode:", "teachModeEnd")) != null) {
               this.tutorialStep = 1;
               var3 = Utils.substringBetween(var3, "teach1:", "teach1End");
               this.dialogLines = Utils.split(var3, "\r\n");
            }
         } else if ((var3 = Utils.substringBetween(var3, "enemy" + this.enemyIds[0] + ":", "enemy" + this.enemyIds[0] + "End")) != null) {
            var3 = Utils.substringBetween(var3, "start:", "startEnd");
            this.dialogLines = Utils.split(var3, "\r\n");
         }
      }

      this.allyX = new short[3];
      this.allyY = new short[3];
      var3 = Utils.substringBetween(var2, "pos:", "posEnd");
      this.allyX = Utils.getShortArray(var3, "x");
      this.allyY = Utils.getShortArray(var3, "y");
      this.loadProgress = 40;
      if (this.enemyIds == null) {
         if (GameData.sceneEnemyCountRange != null) {
            this.enemyCount = (byte)GameUtil.randomRange(
               GameData.sceneEnemyCountRange[0], GameData.sceneEnemyCountRange[1] - GameData.sceneEnemyCountRange[0] + 1
            );
         }
      } else {
         this.enemyCount = (byte)this.enemyIds.length;
      }

      int[] var15 = Utils.getIntArray(var7, "x");
      int[] var9 = Utils.getIntArray(var7, "y");
      var1 = Utils.getByteArray(var7, "amount" + this.enemyCount);

      for (int var4 = 0; var1 != null && var4 < var1.length; var4++) {
         this.enemyX = Utils.append(this.enemyX, var15[var1[var4]]);
         this.enemyY = Utils.append(this.enemyY, var9[var1[var4]]);
      }

      this.loadProgress = 50;
      this.enemyNames = new String[this.enemyCount];
      this.dropItemIds = new short[this.enemyCount][];
      this.dropItemRates = new short[this.enemyCount][];
      this.dropEquipIds = new short[this.enemyCount][];
      this.dropEquipRates = new short[this.enemyCount][];
      this.enemyRoleIds = new short[this.enemyCount];
      this.loadProgress = 60;
      if (popupOffsets == null) {
         loadSkillTables();
      }

      this.loadProgress = 70;
      this.createPartyActors();
      this.loadProgress = 80;
      this.createEnemyActors();
      this.loadProgress = 90;
      this.buildUsableItemList();
      this.buildTurnOrder();
      this.targetCursor = this.firstAliveSlot(10, 20);
      this.loadProgress = 100;
      return true;
   }

   public static void playBattleMusic(int var0) {
      World.stopMusic();
      String var1 = null;
      if (var0 == 0) {
         var1 = GameData.battleMusic;
      } else if (var0 == 1) {
         var1 = GameData.victoryMusic;
      } else if (var0 == 2) {
         var1 = GameData.defeatMusic;
      }

      if (var1 != null && !var1.equals("")) {
         if (Main.music == null) {
            Main.music = new MusicPlayer();
         }

         Main.music.load("/music/" + var1 + ".mid");
         Main.music.setVolume(GameData.musicVolume);
         Main.music.start();
      }
   }

   private void onVictory() {
      this.clearAllStatus();
      GameData.score += 2;
      this.buildVictoryRewards();
   }

   public final void killAllEnemies() {
      for (int var1 = 10; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null && !this.actors[var1].isDead) {
            this.actors[var1].hp = 0;
            this.actors[var1].isDead = true;
            this.actors[var1].sprite.isVisible = false;
            this.actors[var1].state = 128;
         }
      }
   }

   public final void buildVictoryRewards() {
      Vector var1 = new Vector();
      Vector var2 = new Vector();
      int var3 = 0;

      for (int var4 = 0; var4 < this.actors.length; var4++) {
         if (this.actors[var4] != null) {
            this.actors[var4].stats[3] = this.actors[var4].hp;
            this.actors[var4].stats[5] = this.actors[var4].mp;
         }
      }

      for (int var10 = 10; this.actors != null && var10 < this.actors.length; var10++) {
         if (this.actors[var10] != null) {
            var3 += this.actors[var10].stats[1];
         }
      }

      if (GameCanvas.instance.world.script.isMustLoseBattle) {
         var3 = 0;
      }

      this.b_AI = Utils.append(this.b_AI, 0);
      if (var3 > 0 && PaidServiceMenu.expMultiplier > 1) {
         var1.addElement(PaidServiceMenu.expMultiplier + "x EXP");
         this.b_AI = Utils.append(this.b_AI, 0);
      }

      if (Equip.names == null) {
         Equip.loadTable();
      }

      GameData.loadItemTable();
      this.saveStatsToParty();
      this.saveSkillProgressToParty();
      if (var3 > 0) {
         for (int var11 = 0; var11 < this.party.length; var11++) {
            if (this.party[var11] != null) {
               var3 = 0;

               for (int var5 = 10; this.actors != null && var5 < this.actors.length; var5++) {
                  if (this.actors[var5] != null) {
                     var3 += this.actors[var5].stats[1];
                  }
               }

               if (PaidServiceMenu.expMultiplier > 1) {
                  var3 *= PaidServiceMenu.expMultiplier;
               }

               if (var11 == 0) {
                  var2.addElement(this.party[var11].name + " EXP+" + var3);
                  this.b_AI = Utils.append(this.b_AI, 0);
                  RoleSprite var18;
                  (var18 = new RoleSprite()).name = this.party[var11].name;
                  var18.stats = new int[this.party[var11].stats.length];

                  for (int var22 = 0; var22 < this.party[var11].stats.length; var22++) {
                     var18.stats[var22] = this.party[var11].stats[var22];
                  }

                  for (this.party[var11].stats[1] = this.party[var11].stats[1] + var3;
                     this.party[var11].stats[1] >= this.party[var11].stats[2];
                     this.party[var11].stats[5] = this.party[var11].stats[11]
                  ) {
                     if (!this.levelUps.contains(var18)) {
                        this.levelUps.addElement(var18);
                     }

                     this.party[var11].stats[0]++;
                     this.party[var11].stats[15] = this.party[var11].stats[2];
                     int var23 = this.actors[var11].stats[0] + 1;
                     this.party[var11].stats[2] = var23 * var23 * var23 / 2 + var23 * var23;
                     this.party[var11].stats[1] = 0;
                     this.party[var11].stats[16] = this.party[var11].stats[16] + 10;
                     GameData.refreshRoleStats(this.party[var11]);
                     this.party[var11].stats[3] = this.party[var11].stats[10];
                  }
               } else if (this.party[var11].stats[3] > 0) {
                  var2.addElement(this.party[var11].name + " EXP +" + var3);

                  for (this.party[var11].stats[1] = this.party[var11].stats[1] + var3;
                     this.party[var11].stats[1] >= this.party[var11].stats[2];
                     this.b_AI = Utils.append(this.b_AI, 0)
                  ) {
                     RoleSprite var17;
                     (var17 = new RoleSprite()).name = this.party[var11].name;
                     var17.stats = new int[this.party[var11].stats.length];

                     for (int var6 = 0; var6 < this.party[var11].stats.length; var6++) {
                        var17.stats[var6] = this.party[var11].stats[var6];
                     }

                     if (!this.levelUps.contains(var17)) {
                        this.levelUps.addElement(var17);
                     }

                     this.party[var11].stats[0]++;
                     this.party[var11].stats[15] = this.party[var11].stats[2];
                     int var21 = this.party[var11].stats[0] + 1;
                     this.party[var11].stats[2] = var21 * var21 * var21 / 2 + var21 * var21;
                     this.party[var11].stats[1] = 0;
                     GameData.growPetSpriteStats(this.party[var11]);
                     this.party[var11].stats[3] = this.party[var11].stats[10];
                     this.party[var11].stats[5] = this.party[var11].stats[11];
                  }
               }
            }
         }
      }

      int var12;
      if ((var12 = GameUtil.randomRange(0, 100)) < 15) {
         var1.addElement("Thịt ngon X 1");
         this.b_AI = Utils.append(this.b_AI, 0);
         GameData.addItem(17, 1);
      } else if (var12 < 30) {
         var1.addElement("Thịt thường X 1");
         this.b_AI = Utils.append(this.b_AI, 0);
         GameData.addItem(16, 1);
      } else if (var12 < 45) {
         var1.addElement("Thịt kém X 1");
         this.b_AI = Utils.append(this.b_AI, 0);
         GameData.addItem(15, 1);
      }

      for (int var8 = 0; this.dropItemIds != null && var8 < this.dropItemIds.length; var8++) {
         for (int var19 = 0; this.dropItemIds[var8] != null && var19 < this.dropItemIds[var8].length; var19++) {
            if (this.actors[var8 + 10] != null) {
               boolean var24 = true;
               if (this.dropItemRates[var8] != null && var19 < this.dropItemRates[var8].length) {
                  short var13 = this.dropItemRates[var8][var19];
                  if (PaidServiceMenu.hasLuckyPack) {
                     var13 <<= 1;
                  }

                  if (GameUtil.randomBetween(0, 100) <= var13) {
                     var24 = true;
                  } else {
                     var24 = false;
                  }
               }

               if (var24 && this.dropItemIds[var8][var19] <= GameData.itemNames.length) {
                  boolean var14 = false;

                  for (int var25 = 0; var25 < var1.size(); var25++) {
                     if (((String)var1.elementAt(var25)).equals("" + GameData.itemNames[this.dropItemIds[var8][var19] - 1])) {
                        var14 = true;
                     }
                  }

                  if (!var14) {
                     var1.addElement("" + GameData.itemNames[this.dropItemIds[var8][var19] - 1] + " X 1");
                     this.b_AI = Utils.append(this.b_AI, 0);
                  }

                  GameData.addItem(this.dropItemIds[var8][var19], 1);
               }
            }
         }
      }

      for (int var9 = 0; this.dropEquipIds != null && var9 < this.dropEquipIds.length; var9++) {
         for (int var20 = 0; this.dropEquipIds[var9] != null && var20 < this.dropEquipIds[var9].length; var20++) {
            boolean var26 = true;
            if (this.dropEquipRates[var9] != null && var20 < this.dropEquipRates[var9].length) {
               short var15 = this.dropEquipRates[var9][var20];
               if (PaidServiceMenu.hasLuckyPack) {
                  var15 <<= 1;
               }

               if (GameUtil.randomBetween(1, 1000) <= var15) {
                  var26 = true;
               } else {
                  var26 = false;
               }
            }

            if (var26) {
               Equip.loadTable();
               if (this.dropEquipIds[var9][var20] <= Equip.names.length) {
                  boolean var16 = false;

                  for (int var27 = 0; var27 < var1.size(); var27++) {
                     if (((String)var1.elementAt(var27)).equals("Nhận " + Equip.names[this.dropEquipIds[var9][var20] - 1])) {
                        var16 = true;
                     }
                  }

                  if (!var16) {
                     var1.addElement("Nhận " + Equip.names[this.dropEquipIds[var9][var20] - 1]);
                     this.b_AI = Utils.append(this.b_AI, 0);
                  }

                  GameData.addEquip(this.dropEquipIds[var9][var20], -1);
               }
               break;
            }
         }
      }

      if (var1.size() > 0) {
         this.rewardItemLines = new String[var1.size()];
         var1.copyInto(this.rewardItemLines);
      }

      if (var2.size() > 0) {
         this.rewardExpLines = new String[var2.size()];
         var2.copyInto(this.rewardExpLines);
      }

      this.loadMenuImages();
      this.createMenuPanels(142, this.menuHeight - 142);
      this.phase = 24;
      rewardScroll = 0;
      if (this.rewardItemLines == null) {
         this.resultPage = 1;
      } else {
         this.resultPage = 0;
      }

      this.resetActorPoses();
   }

   private void startDialog(String[] var1) {
      if (this.dialog == null) {
         this.dialog = new DialogBox();
      }

      Vector var2 = new Vector();
      String[] var3 = null;

      for (int var4 = 0; var4 < var1.length; var4++) {
         if (var1[var4].indexOf("&") != -1) {
            var1[var4] = GameUtil.replaceAll(var1[var4], "&", GameData.roleNames[0]);
         }

         var2.addElement(var1[var4]);
      }

      if (var2.size() > 0) {
         var3 = new String[var2.size()];
         var2.copyInto(var3);
      }

      int var6 = var3[0].indexOf(".");
      int var5 = var3[0].indexOf("$");
      Utils.parseShort(var3[0].substring(var5 + 1, var6));
      this.dialog.lines = var3;
      this.dialog.lineIndex = 0;
      this.dialog.layoutLine();
      this.dialog.isVisible = true;
      this.phase = 4;
   }

   private static void loadSkillTables() {
      String var0 = Utils.loadConfigText("/bin/skill.bin");
      if (GameData.skillTable == null) {
         GameData.skillTable = Utils.getStringTable(var0, "skill:", "skillend", null, "\t");
      }

      if (Skill.names == null) {
         Skill.loadTable();
      }

      popupOffsets = Utils.getByteTable(var0, "headNum:", "headNumEnd", "=");
   }

   private void buildUsableItemList() {
      GameData.loadItemTable();
      Vector var1 = new Vector();

      for (int var2 = 0; GameData.bagItems != null && var2 < GameData.bagItems.length; var2++) {
         int var3 = GameData.findItemIndex(GameData.bagItems[var2][0]);
         if (GameData.itemBattleUsable[var3] == 1) {
            var1.addElement(GameData.bagItems[var2]);
         }
      }

      if (var1.size() > 0) {
         this.usableItems = new short[var1.size()][];
         var1.copyInto(this.usableItems);
      } else {
         this.usableItems = null;
      }

      if (this.usableItems != null) {
         b_B = 0;
         if (0 > this.usableItems.length - 1) {
            b_B = (byte)(this.usableItems.length - 1);
            return;
         }
      } else {
         b_B = 0;
      }
   }

   private void createPartyActors() {
      if (popupOffsets == null) {
         loadSkillTables();
      }

      if (GameData.partyRoleTable == null) {
         GameData.partyRoleTable = Utils.getStringTable(Utils.loadConfigText("/bin/team.bin"), "role:", "roleEnd", null, "\t");
      }

      if (this.teamData == null) {
         this.teamData = Utils.loadConfigText("/bin/team.bin");
      }

      for (int var1 = 0; var1 < this.party.length; var1++) {
         this.actors[var1] = MultiTapInput.newBattleActor();
         this.actors[var1].name = this.party[var1].name;
         this.actors[var1].slot = var1;
         this.actors[var1].roleId = this.party[var1].roleId;
         this.actors[var1].partyUid = this.party[var1].petUid;
         if (var1 != 0) {
            this.actors[var1].sprite = new AnimSprite();
            this.actors[var1].sprite = GameUtil.loadBattleAnimation(this.party[var1].roleId, true);
            if (this.actors[var1].sprite.getActionFrameCount(2) > 1) {
               BattleActor var30 = this.actors[var1];
               byte var18 = 0;
               var30.moveType = var18;
            } else {
               BattleActor var31 = this.actors[var1];
               byte var19 = 1;
               var31.moveType = var19;
            }

            this.actors[var1].spriteW = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[2];
            this.actors[var1].spriteH = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[3];
            this.actors[var1].defendEffect = new AnimSprite();
            this.actors[var1].defendEffect.loadInGroup("/ft/defense.av", 1);
            ResourceCache.loadImage("/pic/battleNumber1.gf");
            String var8;
            String var20 = Utils.getValue(var8 = Utils.substringBetween(this.teamData, "attackDefault:", "attackDefaultEnd"), "actEffect");
            String var24 = Utils.getValue(var8, "frame");
            this.actors[var1].hitFrames = Utils.parseByteArray(var24, ",");
            this.actors[var1].hitEffect = new AnimSprite();
            this.actors[var1].hitEffect.loadInGroup(var20, 1);
         } else {
            byte var2 = 0;

            for (int var3 = 0; var3 < GameData.partyRoleTable.length; var3++) {
               if (Utils.parseInt(GameData.partyRoleTable[var3][1]) == this.party[var1].roleId) {
                  var2 = (byte)(var3 + 1);
                  break;
               }
            }

            this.party[var1].battleSpritePath = GameData.partyRoleTable[var2 - 1][2];
            if (this.party[var1].battleSpritePath.toLowerCase().endsWith(".av")) {
               ResourceCache.loadImage("/pic/battleNumber1.gf");
               ResourceCache.loadImage("/pic/battleNumber2.gf");
               this.actors[var1].sprite = new AnimSprite();
               this.actors[var1].sprite.loadInGroup(this.party[var1].battleSpritePath, 1);
               if (this.actors[var1].sprite.getActionFrameCount(2) > 1) {
                  BattleActor var10000 = this.actors[var1];
                  byte var13 = 0;
                  var10000.moveType = var13;
               } else {
                  BattleActor var29 = this.actors[var1];
                  byte var14 = 1;
                  var29.moveType = var14;
               }

               this.actors[var1].spriteW = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[2];
               this.actors[var1].spriteH = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[3];
               this.actors[var1].defendEffect = new AnimSprite();
               this.actors[var1].defendEffect.loadInGroup("/ft/defense.av", 1);
               if (this.actors[var1].roleId == 1) {
                  this.heroFace = new AnimSprite();
                  this.heroFace.load("/face/1f.av");
                  this.actors[var1].castPortrait = new AnimSprite();
                  this.actors[var1].castPortrait.loadInGroup("/face/zdtx1.av", 1);
                  String var15;
                  String var4 = Utils.getValue(var15 = Utils.substringBetween(this.teamData, "attack1:", "attack1End"), "actEffect");
                  String var5 = Utils.getValue(var15, "frame");
                  this.actors[var1].hitFrames = Utils.parseByteArray(var5, ",");
                  this.actors[var1].hitEffect = new AnimSprite();
                  this.actors[var1].hitEffect.loadInGroup(var4, 1);
               } else if (this.actors[var1].roleId == 2) {
                  this.heroFace = new AnimSprite();
                  this.heroFace.load("/face/2f.av");
                  this.actors[var1].castPortrait = new AnimSprite();
                  this.actors[var1].castPortrait.loadInGroup("/face/zdtx2.av", 1);
                  this.actors[var1].castPortrait.setFrame(0);
                  String var16;
                  String var22 = Utils.getValue(var16 = Utils.substringBetween(this.teamData, "attack2:", "attack2End"), "actEffect");
                  String var6 = Utils.getValue(var16, "frame");
                  this.actors[var1].hitFrames = Utils.parseByteArray(var6, ",");
                  this.actors[var1].hitEffect = new AnimSprite();
                  this.actors[var1].hitEffect.loadInGroup(var22, 1);
               } else {
                  String var17;
                  String var23 = Utils.getValue(var17 = Utils.substringBetween(this.teamData, "attackDefault:", "attackDefaultEnd"), "actEffect");
                  String var7 = Utils.getValue(var17, "frame");
                  this.actors[var1].hitFrames = Utils.parseByteArray(var7, ",");
                  this.actors[var1].hitEffect = new AnimSprite();
                  this.actors[var1].hitEffect.loadInGroup(var23, 1);
               }
            } else {
               this.actors[var1].moveType = 0;
               ResourceCache.loadImage(this.party[var1].battleSpritePath);
            }
         }

         this.actors[var1].poisonEffect = new AnimSprite();
         this.actors[var1].poisonEffect.loadInGroup("/ft/zhongdu.av", 1);
         this.actors[var1].confuseEffect = new AnimSprite();
         this.actors[var1].confuseEffect.loadInGroup("/ft/zhunluan.av", 1);
         this.actors[var1].stunEffect = new AnimSprite();
         this.actors[var1].stunEffect.loadInGroup("/ft/zhongyun.av", 1);
         if (var1 == 0) {
            this.actors[var1].homePos.x = this.allyX[1];
            this.actors[var1].homePos.y = this.allyY[1];
         } else if (var1 == 1) {
            this.actors[var1].homePos.x = this.allyX[0];
            this.actors[var1].homePos.y = this.allyY[0];
         } else {
            this.actors[var1].homePos.x = this.allyX[var1];
            this.actors[var1].homePos.y = this.allyY[var1];
         }

         this.actors[var1].pos.x = this.actors[var1].homePos.x;
         this.actors[var1].pos.y = this.actors[var1].homePos.y;
         this.actors[var1].isPresent = true;
         Vector var9 = new Vector();
         Vector var21 = new Vector();
         this.actors[var1].skills = null;
         this.actors[var1].activeSkillIds = null;
         if (this.party[var1].skillIds != null && this.party[var1].skillIds.length > 0) {
            this.actors[var1].skillIds = new short[this.party[var1].skillIds.length];
         }

         for (int var25 = 0; this.party[var1].skillIds != null && var25 < this.party[var1].skillIds.length; var25++) {
            this.actors[var1].skillIds[var25] = this.party[var1].skillIds[var25];
            if (!Utils.contains(this.passiveSkillIds, this.party[var1].skillIds[var25])) {
               var9.addElement(new Short(this.party[var1].skillIds[var25]));
               var21.addElement(this.party[var1].skills[var25]);
            }

            if (this.party[var1].skillIds[var25] == 7) {
               this.actors[var1].hasCounter = true;
            }

            if (this.party[var1].skillIds[var25] == 37) {
               this.actors[var1].alwaysCrit = true;
            }

            if (this.party[var1].skillIds[var25] == 36) {
               this.actors[var1].halfDamage = true;
            }
         }

         if (var9.size() > 0) {
            this.actors[var1].activeSkillIds = new short[var9.size()];
            this.actors[var1].skills = new Skill[var21.size()];
            Short[] var26 = new Short[var9.size()];
            var9.copyInto(var26);

            for (int var10 = 0; var10 < this.actors[var1].activeSkillIds.length; var10++) {
               this.actors[var1].activeSkillIds[var10] = var26[var10];
               this.actors[var1].skills[var10] = (Skill)var21.elementAt(var10);
            }
         }

         this.actors[var1].elements = new byte[4];
         this.actors[var1].baseElements = new byte[4];

         for (int var27 = 0; var27 < this.party[var1].elementValues.length; var27++) {
            this.actors[var1].elements[var27] = this.party[var1].elementValues[var27];
            this.actors[var1].baseElements[var27] = this.party[var1].elementValues[var27];
         }

         byte var28 = 0;
         if (this.actors[0].skillIds != null && Utils.contains(this.actors[0].skillIds, 35)) {
            var28 = 5;
         }

         for (int var11 = 0; var11 < this.party[var1].stats.length; var11++) {
            this.actors[var1].stats[var11] = this.party[var1].stats[var11];
         }

         this.actors[var1].attack = this.party[var1].stats[12] * (var28 + 100) / 100;
         this.actors[var1].defense = this.party[var1].stats[13] * (var28 + 100) / 100;
         this.actors[var1].speed = this.party[var1].stats[14] * (var28 + 100) / 100;
         if (GameData.statMultiplier > 0) {
            this.actors[var1].attack = this.actors[var1].attack * GameData.statMultiplier;
            this.actors[var1].defense = this.actors[var1].defense * GameData.statMultiplier;
            this.actors[var1].speed = this.actors[var1].speed * GameData.statMultiplier;
         }

         if (this.actors[var1].activeSkillIds != null && Utils.contains(this.actors[var1].activeSkillIds, 38)) {
            this.actors[var1].speed = this.actors[var1].speed + this.party[var1].stats[14] * 20 / 100;
         }

         this.actors[var1].attack = this.actors[var1].attack + elementPassiveBonus(this.actors[var1]);

         for (int var12 = 0; var12 < 6; var12++) {
            if (this.party[var1].equips != null && this.party[var1].equips[var12] != null) {
               this.actors[var1].critRate = (short)(
                  this.actors[var1].critRate + (short)(this.party[var1].equips[var12].crit + this.party[var1].equips[var12].gemCrit)
               );
            }
         }

         this.actors[var1].hp = this.actors[var1].stats[3];
         this.actors[var1].mp = this.actors[var1].stats[5];
      }
   }

   private void createEnemyActors() {
      if (this.teamData == null) {
         this.teamData = Utils.loadConfigText("/bin/team.bin");
      }

      for (int var2 = 0; var2 < this.enemyCount; var2++) {
         short var7;
         if (this.enemyIds == null) {
            var7 = GameUtil.randomBetween(0, GameData.sceneEnemyIds.length - 1);
            var7 = GameData.sceneEnemyIds[var7];
         } else {
            var7 = this.enemyIds[var2];
         }

         String var3 = Utils.loadConfigText("/bin/petdata.bin");
         if (this.petTable == null) {
            this.petTable = Utils.getStringTable(var3, "petdata", "end", null, "\t");
         }

         if (this.starTable == null) {
            this.starTable = Utils.getStringTable(var3, "stardata", "end", null, "\t");
         }

         this.enemyNames[var2] = this.petTable[var7 - 1][0];
         this.actors[var2 + 10] = MultiTapInput.newBattleActor();
         ResourceCache.loadImage("/pic/battleNumber1.gf");
         ResourceCache.loadImage("/pic/battleNumber2.gf");
         this.actors[var2 + 10].sprite = GameUtil.loadBattleAnimation(var7, false);
         if (this.actors[var2 + 10].sprite.getActionFrameCount(2) > 1) {
            BattleActor var10000 = this.actors[var2 + 10];
            byte var4 = 0;
            var10000.moveType = var4;
         } else {
            BattleActor var27 = this.actors[var2 + 10];
            byte var12 = 1;
            var27.moveType = var12;
         }

         short[] var8 = this.actors[var2 + 10].sprite.getBoxesAt(0, 0, 0);
         this.actors[var2 + 10].spriteW = var8[2];
         this.actors[var2 + 10].spriteH = var8[3];
         this.actors[var2 + 10].name = this.enemyNames[var2];
         this.actors[var2 + 10].roleId = var7;
         this.actors[var2 + 10].defendEffect = new AnimSprite();
         this.actors[var2 + 10].defendEffect.loadInGroup("/ft/defense.av", 1);
         this.actors[var2 + 10].poisonEffect = new AnimSprite();
         this.actors[var2 + 10].poisonEffect.loadInGroup("/ft/zhongdu.av", 1);
         this.actors[var2 + 10].confuseEffect = new AnimSprite();
         this.actors[var2 + 10].confuseEffect.loadInGroup("/ft/zhunluan.av", 1);
         this.actors[var2 + 10].stunEffect = new AnimSprite();
         this.actors[var2 + 10].stunEffect.loadInGroup("/ft/zhongyun.av", 1);
         String var13 = Utils.getValue(var3 = Utils.substringBetween(this.teamData, "attackDefault:", "attackDefaultEnd"), "actEffect");
         var3 = Utils.getValue(var3, "frame");
         this.actors[var2 + 10].hitFrames = Utils.parseByteArray(var3, ",");
         this.actors[var2 + 10].hitEffect = new AnimSprite();
         this.actors[var2 + 10].hitEffect.loadInGroup(var13, 1);
         int var11 = var7 - 1;
         this.actors[var2 + 10].stats = new int[RoleSprite.statCount];
         if (this.enemyIds == null) {
            GameUtil.randomRange(1, 1000);
            if (GameData.sceneEnemyLevelRange == null) {
               this.actors[var2 + 10].stats[0] = GameUtil.randomBetween(this.actors[0].stats[0] - 3, this.actors[0].stats[0] + 3);
            } else {
               int[] var5;
               int var14;
               if ((var14 = (var5 = Utils.parseIntArray(GameData.sceneEnemyLevelRange, ","))[1] - var5[0]) <= 0) {
                  var14 = 1;
               }

               this.actors[var2 + 10].stats[0] = GameUtil.randomRange(var5[0], var14);
            }
         } else if (this.enemyLevels != null) {
            this.actors[var2 + 10].stats[0] = this.enemyLevels[var2];
         } else {
            this.actors[var2 + 10].stats[0] = 1;
         }

         int var15 = var7 - 1;
         int[] var20 = GameUtil.a(this.petTable, this.starTable, var15, 3, this.actors[var2 + 10].stats[0]);
         this.actors[var2 + 10].stats[3] = var20[1];
         var20 = GameUtil.a(this.petTable, this.starTable, var15, 5, this.actors[var2 + 10].stats[0]);
         this.actors[var2 + 10].stats[5] = var20[1];
         var20 = GameUtil.a(this.petTable, this.starTable, var15, 7, this.actors[var2 + 10].stats[0]);
         this.actors[var2 + 10].stats[12] = var20[1];
         var20 = GameUtil.a(this.petTable, this.starTable, var15, 9, this.actors[var2 + 10].stats[0]);
         this.actors[var2 + 10].stats[13] = var20[1];
         var20 = GameUtil.a(this.petTable, this.starTable, var15, 11, this.actors[var2 + 10].stats[0]);
         this.actors[var2 + 10].stats[14] = var20[1];
         this.actors[var2 + 10].stats[11] = this.actors[var2 + 10].stats[5];
         this.actors[var2 + 10].stats[10] = this.actors[var2 + 10].stats[3];
         this.actors[var2 + 10].hp = this.actors[var2 + 10].stats[3];
         this.actors[var2 + 10].mp = this.actors[var2 + 10].stats[5];
         int var16 = Utils.parseInt(this.petTable[var11][1]);
         this.actors[var2 + 10].expFactor = var16;
         this.actors[var2 + 10].stats[1] = this.actors[var2 + 10].expFactor * this.actors[var2 + 10].expFactor * this.actors[var2 + 10].stats[0];
         this.actors[var2 + 10].i_I = 0;
         if (this.petTable[var11][14].equals("-1")) {
            this.dropItemIds[var2] = null;
         } else {
            this.dropItemIds[var2] = Utils.parseShortArray(this.petTable[var11][14], ",");
         }

         if (this.petTable[var11][15].equals("-1")) {
            this.dropItemRates[var2] = null;
         } else {
            this.dropItemRates[var2] = Utils.parseShortArray(this.petTable[var11][15], ",");
         }

         if (this.petTable[var11][16].equals("-1")) {
            this.dropEquipIds[var2] = null;
         } else {
            this.dropEquipIds[var2] = Utils.parseShortArray(this.petTable[var11][16], ",");
         }

         if (this.petTable[var11][17].equals("-1")) {
            this.dropEquipRates[var2] = null;
         } else {
            this.dropEquipRates[var2] = Utils.parseShortArray(this.petTable[var11][17], ",");
         }

         if (this.petTable[var11][13].equals("-1")) {
            this.actors[var2 + 10].skillIds = null;
         } else {
            String[] var17 = Utils.split(this.petTable[var11][13], "|");

            for (int var25 = 0; var25 < var17.length; var25++) {
               short[] var6 = Utils.parseShortArray(var17[var25], ",");
               this.actors[var2 + 10].skillIds = Utils.append(this.actors[var2 + 10].skillIds, var6[0]);
               if (!Utils.contains(this.passiveSkillIds, var6[0])) {
                  this.actors[var2 + 10].activeSkillIds = Utils.append(this.actors[var2 + 10].activeSkillIds, var6[0]);
                  this.actors[var2 + 10].skillLevels = Utils.append(this.actors[var2 + 10].skillLevels, var6[1]);
               }

               if (var6[0] == 7) {
                  this.actors[var2 + 10].hasCounter = true;
               }
            }
         }

         if (this.petTable[var11][2] != null) {
            for (int var18 = 0; var18 < 4; var18++) {
               String var26;
               if ((var26 = this.petTable[var11][2].substring(var18, var18 + 1)).equals("a")) {
                  this.actors[var2 + 10].baseElements = Utils.append(this.actors[var2 + 10].baseElements, 10);
               } else {
                  this.actors[var2 + 10].baseElements = Utils.append(this.actors[var2 + 10].baseElements, Utils.parseInt(var26));
               }
            }
         }

         this.actors[var2 + 10].elements = new byte[4];

         for (int var19 = 0; var19 < this.actors[var2 + 10].baseElements.length; var19++) {
            this.actors[var2 + 10].elements[var19] = this.actors[var2 + 10].baseElements[var19];
         }

         this.actors[var2 + 10].attack = this.actors[var2 + 10].stats[12] + elementPassiveBonus(this.actors[var2 + 10]);
         this.actors[var2 + 10].defense = this.actors[var2 + 10].stats[13];
         this.actors[var2 + 10].speed = this.actors[var2 + 10].stats[14];
         this.actors[var2 + 10].homePos.x = this.enemyX[var2];
         this.actors[var2 + 10].homePos.y = this.enemyY[var2];
         this.actors[var2 + 10].pos.x = this.enemyX[var2];
         this.actors[var2 + 10].pos.y = this.enemyY[var2];
         this.actors[var2 + 10].roleId = var7;
         this.actors[var2 + 10].slot = var2 + 10;
         this.enemyRoleIds[var2] = (short)var7;
         if (this.isStoryBattle && this.enemyVisible != null && this.enemyVisible[var2] == 0) {
            this.actors[var2 + 10].isPresent = false;
         }
      }
   }

   private static boolean rollHit(BattleActor var0, BattleActor var1, int var2) {
      boolean var3 = false;
      int var4 = GameUtil.randomRange(1, 100);
      int var5 = 0 + var0.speed;
      int var8 = 0 + var1.speed;
      int var6 = var5 * 100 / (var5 + var8) + var5 + var2;
      int var7 = var6 + 20;
      if (var7 < 20) {
         var7 = 20;
      }

      if (var4 <= var7) {
         var3 = true;
      }

      return var3;
   }

   public final void normalAttack(int var1, int var2) {
      this.dealDamage(this.actors[var1], this.actors[var2], this.actors[var1].attack, this.actors[var2].defense, 0, false);
   }

   private void dealDamage(BattleActor var1, BattleActor var2, int var3, int var4, int var5, boolean var6) {
      if (!rollHit(var1, var2, var5) && !var6) {
         var2.dodged = true;
      } else {
         var5 = 200 + elementAdvantage(var1.elements, var2.elements);
         var6 = GameUtil.randomBetween(0, 5);
         int var7 = var3;
         if (var1.isCrit) {
            var7 = var3 * 2;
         }

         int var8 = var7 * var5 / 100 - var4 + var6;
         var3 = GameUtil.randomBetween(0, 100);
         if (var2.isCountering && var3 < 15) {
            var8 /= 2;
         }

         if (var2.isDefending) {
            var8 /= 5;
         }

         if (var2.halfDamage) {
            var8 /= 2;
         }

         if (var8 < 10) {
            var8 = 10;
         }

         var2.hp -= var8;
      }
   }

   private static int elementAdvantage(byte[] var0, byte[] var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 4; var3++) {
         if (var0[var3] > 0) {
            if (var3 == 0) {
               if (var1[var3 + 1] > 0) {
                  var2 += var0[var3] * var1[var3 + 1];
               }

               if (var1[3] > 0) {
                  var2 -= var0[var3] * var1[3];
               }
            } else if (var3 == 1) {
               if (var1[var3 + 1] > 0) {
                  var2 += var0[var3] * var1[var3 + 1];
               }

               if (var1[var3 - 1] > 0) {
                  var2 -= var0[var3] * var1[var3 - 1];
               }
            } else if (var3 == 2) {
               if (var1[var3 + 1] > 0) {
                  var2 += var0[var3] * var1[var3 + 1];
               }

               if (var1[var3 - 1] > 0) {
                  var2 -= var0[var3] * var1[var3 - 1];
               }
            } else if (var3 == 3) {
               if (var1[0] > 0) {
                  var2 += var0[var3] * var1[0];
               }

               if (var1[var3 - 1] > 0) {
                  var2 -= var0[var3] * var1[var3 - 1];
               }
            }
         }
      }

      return var2;
   }

   private static int elementPassiveBonus(BattleActor var0) {
      int var1 = 0;
      if (var0.skillIds != null) {
         for (int var2 = 0; var2 < var0.skillIds.length; var2++) {
            if (var0.skillIds[var2] == 29 && var0.elements[0] > 0) {
               var1 += var0.elements[0] * var0.stats[12] / 10;
            }

            if (var0.skillIds[var2] == 30 && var0.elements[3] > 0) {
               var1 += var0.elements[3] * var0.stats[12] / 10;
            }

            if (var0.skillIds[var2] == 31 && var0.elements[2] > 0) {
               var1 += var0.elements[2] * var0.stats[12] / 10;
            }

            if (var0.skillIds[var2] == 32 && var0.elements[1] > 0) {
               var1 += var0.elements[1] * var0.stats[12] / 10;
            }
         }
      }

      return var1;
   }

   private int countAlive(int var1, int var2) {
      int var3 = 0;

      for (int var4 = var1; var4 < var2; var4++) {
         if (this.actors[var4] != null && !this.actors[var4].isDead) {
            var3++;
         }
      }

      return var3;
   }

   private byte[] aliveSlots(int var1, int var2) {
      byte[] var3 = null;

      for (int var4 = var1; var4 < var2; var4++) {
         if (this.actors[var4] != null && !this.actors[var4].isDead) {
            var3 = Utils.append(var3, var4);
         }
      }

      return var3;
   }

   private void buildTurnOrder() {
      this.turnOrder = new short[this.countAlive(0, this.actors.length)][2];
      int var1 = 0;

      for (int var2 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null && !this.actors[var1].isDead) {
            this.turnOrder[var2][0] = (short)var1;
            this.turnOrder[var2][1] = (short)this.actors[var1].speed;
            var2++;
         }
      }

      GameUtil.a(this.turnOrder, 1, 0);
   }

   public final void clearStatus(BattleActor var1) {
      var1.poison = new byte[2];
      var1.poison[0] = 0;
      var1.poison[1] = 0;
      var1.stunTurns = 0;
      var1.confuseTurns = 0;
      var1.silenceTurns = 0;
      var1.defenseDebuff = new byte[2];
      var1.attackBuff = new byte[2];
      var1.rallyBuff = new byte[2];

      for (int var2 = 1; var2 < this.actionPlans[var1.slot].length; var2++) {
         this.actionPlans[var1.slot][var2] = -1;
      }
   }

   private void clearAllStatus() {
      for (int var1 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null) {
            this.clearStatus(this.actors[var1]);
         }
      }
   }

   private void resetActionPlans() {
      for (int var1 = 0; var1 < this.actionPlans.length; var1++) {
         this.actionPlans[var1][0] = var1;

         for (int var2 = 1; var2 < this.actionPlans[var1].length; var2++) {
            this.actionPlans[var1][var2] = -1;
         }
      }
   }

   public final void init() {
      this.loadResources();
      this.isLoaded = true;
   }

   public final void draw(Graphics var1) {
      if (this.showBackground) {
         if (this.background != null) {
            var1.drawImage(this.background, DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1, 3);
         }

         if (this.critFlash) {
            if (this.critFlashTicks < 10) {
               if (GameCanvas.instance.frameCounter % 3 == 0) {
                  this.critFlashTicks++;
                  var1.setColor(16711680);
                  var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               }
            } else {
               this.critFlashTicks = 0;
               this.critFlash = false;
            }
         }
      } else {
         var1.setColor(0);
         var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
      }

      if (this.phase == 28) {
         Graphics var35 = var1;
         var1.setColor(0);
         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         if (this.loadingSprite != null) {
            var1.setColor(16777113);
            if (GameData.loadingTips != null) {
               String[] var45 = Utils.wrapText(GameData.loadingTips[GameData.tipIndex], DeviceConfig.screenSize[0] - 50);

               for (int var48 = 0; var48 < var45.length; var48++) {
                  vh.drawString(var35, var45[var48], DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 3 + var48 * Utils.lineHeight, 17);
               }
            }

            this.loadingSprite.x = (short)(15 + (DeviceConfig.screenSize[0] - 30) * this.shownLoadProgress / 100);
            this.loadingSprite.y = (short)(DeviceConfig.screenSize[1] - 15);
            this.loadingSprite.setAction(3);
            this.loadingSprite.draw(var35);
            this.loadingSprite.nextFrame(true);
         }

         if (this.shownLoadProgress < this.loadProgress) {
            this.shownLoadProgress = this.loadProgress + 0;
            if (this.shownLoadProgress > this.loadProgress) {
               this.shownLoadProgress = this.loadProgress;
            }
         }

         if (this.isLoaded && this.shownLoadProgress >= 100) {
            if (this.dialogLines != null) {
               this.dialogMode = 1;
               this.startDialog(this.dialogLines);
            } else {
               this.phase = 0;
               this.menuMode = 0;
               playBattleMusic(0);
            }

            this.loadingSprite = null;
            if (!DeviceConfig.modelName.equals("N73")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("N5800")
               && !DeviceConfig.modelName.equals("E62")
               && !DeviceConfig.modelName.equals("W958C")) {
               GameUtil.freeImageCaches();
            }

            this.loadProgress = 0;
            this.shownLoadProgress = 0;
         }

         try {
            Thread.sleep(0L);
         } catch (InterruptedException var7) {
            var7.printStackTrace();
         }
      } else if (this.phase == 27 && this.introImage != null) {
         if (introSlide < this.introImage.getHeight()) {
            introSlide += 5;
            var1.setClip(
               DeviceConfig.screenSize[0] - this.introImage.getWidth() >> 1,
               DeviceConfig.screenSize[1] - this.introImage.getHeight() >> 1,
               this.introImage.getWidth(),
               this.introImage.getHeight()
            );
            var1.drawImage(
               this.introImage,
               DeviceConfig.screenSize[0] - this.introImage.getWidth() >> 1,
               (DeviceConfig.screenSize[1] + this.introImage.getHeight() >> 1) - introSlide,
               20
            );
         } else {
            try {
               Thread.sleep(500L);
            } catch (InterruptedException var8) {
               var8.printStackTrace();
            }

            introSlide = 0;
            if (this.dialogLines != null) {
               this.dialogMode = 1;
               this.startDialog(this.dialogLines);
            } else {
               this.phase = 0;
               this.menuMode = 0;
            }
         }
      } else {
         for (int var2 = 10; var2 < 20; var2++) {
            if (this.actors[var2] != null && this.actors[var2].sprite != null) {
               this.actors[var2].draw(var1);
            }
         }

         for (int var9 = 9; var9 >= 0; var9--) {
            if (this.actors[var9] != null && this.actors[var9].sprite != null) {
               if (var9 == 0) {
                  if (this.actors[1] != null) {
                     this.actors[1].draw(var1);
                  } else {
                     this.actors[0].draw(var1);
                  }
               } else if (var9 == 1) {
                  this.actors[0].draw(var1);
               } else {
                  this.actors[var9].draw(var1);
               }
            }
         }

         if (this.actors[this.actingSlot].state == 5) {
            if (this.actors[this.actingSlot].castPortrait != null) {
               if (!this.actors[this.actingSlot].castIntroDone) {
                  this.actors[this.actingSlot].castPortrait.setPosition(DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1);
                  this.actors[this.actingSlot].castPortrait.draw(var1);
                  if (this.actors[this.actingSlot].castPortrait.getFrame() >= 5 && this.actors[this.actingSlot].castPortrait.getFrame() <= 15) {
                     short var10 = this.actors[this.actingSlot].castPortrait.getBoxesAt(0, 0, 0)[2];
                     short var3 = this.actors[this.actingSlot].castPortrait.getBoxesAt(0, 0, 0)[3];
                     Utils.drawOutlinedText(
                        var1,
                        Skill.names[this.actors[this.actingSlot].skillId - 1],
                        (DeviceConfig.screenSize[0] >> 1) - var10 / 2,
                        (DeviceConfig.screenSize[1] >> 1) - var3,
                        13695223,
                        1196461,
                        17
                     );
                  }

                  if (this.actors[this.actingSlot].castPortrait.getFrame() >= this.actors[this.actingSlot].castPortrait.getActionFrameCount(0) - 1) {
                     this.actors[this.actingSlot].castIntroDone = true;
                     this.actors[this.actingSlot].castPortrait.setFrame(0);
                     this.actors[this.actingSlot].setAction(4, 0);
                  } else if (GameCanvas.instance.frameCounter % 3 == 0) {
                     this.actors[this.actingSlot].castPortrait.nextFrame(true);
                  }
               }
            } else if (this.actors[this.actingSlot].skillId != 0) {
               drawTopBar(var1);
               Utils.drawOutlinedText(
                  var1, Skill.names[this.actors[this.actingSlot].skillId - 1], DeviceConfig.screenSize[0] >> 1, 6 + Utils.lineHeight, 13695223, 1196461, 33
               );
            }
         }

         if (this.actors[this.actingSlot] != null && this.actors[this.actingSlot].state == 10) {
            if (this.actors[this.actingSlot].actionAnimDone && this.actors[this.actingSlot].targets != null && !this.actors[this.actingSlot].effectDone) {
               this.drawSkillEffects(var1, this.actors[this.actingSlot].targets);
            }

            if (this.actors[this.actingSlot].effectDone && !this.actors[this.actingSlot].resultShown) {
               byte var12 = popupOffsets[this.popupFrame][1];
               byte var26 = popupOffsets[this.popupFrame][1];

               for (int var36 = 0; var36 < this.actors[this.actingSlot].targets.length; var36++) {
                  if (this.getActor(this.actors[this.actingSlot].targets[var36]) != null) {
                     if (this.getActor(this.actors[this.actingSlot].targets[var36]).hp != this.getActor(this.actors[this.actingSlot].targets[var36]).stats[3]) {
                        GameUtil.a(
                           var1,
                           this.getActor(this.actors[this.actingSlot].targets[var36]).hp - this.getActor(this.actors[this.actingSlot].targets[var36]).stats[3],
                           this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.x - var26,
                           this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.y
                              - this.getActor(this.actors[this.actingSlot].targets[var36]).spriteH
                              + var12,
                           this.damageDigits,
                           13,
                           13,
                           33,
                           0
                        );
                        if (this.popupFrame % 2 == 0) {
                           this.getActor(this.actors[this.actingSlot].targets[var36]).pos.x = this.getActor(this.actors[this.actingSlot].targets[var36])
                                 .homePos
                                 .x
                              - 4;
                        } else {
                           this.getActor(this.actors[this.actingSlot].targets[var36]).pos.x = this.getActor(this.actors[this.actingSlot].targets[var36])
                                 .homePos
                                 .x
                              + 4;
                        }
                     }

                     if (this.getActor(this.actors[this.actingSlot].targets[var36]).hp <= 0 && GameCanvas.instance.frameCounter % 2 == 0) {
                        this.getActor(this.actors[this.actingSlot].targets[var36]).sprite.isVisible = !this.getActor(
                              this.actors[this.actingSlot].targets[var36]
                           )
                           .sprite
                           .isVisible;
                     }

                     if (this.getActor(this.actors[this.actingSlot].targets[var36]).dodged) {
                        if (this.getActor(this.actors[this.actingSlot].targets[var36]).missImage != null) {
                           var1.drawRegion(
                              this.getActor(this.actors[this.actingSlot].targets[var36]).missImage,
                              0,
                              0,
                              43,
                              19,
                              0,
                              this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.x,
                              this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.y
                                 - this.getActor(this.actors[this.actingSlot].targets[var36]).spriteH,
                              33
                           );
                        } else {
                           var1.setColor(2153971);
                           vh.drawString(
                              var1,
                              "Né",
                              this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.x,
                              this.getActor(this.actors[this.actingSlot].targets[var36]).homePos.y
                                 - this.getActor(this.actors[this.actingSlot].targets[var36]).spriteH,
                              33
                           );
                        }
                     }
                  }
               }

               if (this.actors[this.actingSlot].hp != this.actors[this.actingSlot].stats[3]) {
                  GameUtil.a(
                     var1,
                     this.actors[this.actingSlot].hp - this.actors[this.actingSlot].stats[3],
                     this.actors[this.actingSlot].homePos.x - var26,
                     this.actors[this.actingSlot].homePos.y - this.actors[this.actingSlot].spriteH + var12,
                     this.damageDigits,
                     13,
                     13,
                     33,
                     0
                  );
               }

               if (this.popupFrame < popupOffsets.length - 1) {
                  if (GameCanvas.instance.frameCounter % 3 == 0) {
                     this.popupFrame++;
                  }
               } else {
                  this.popupFrame = 0;
                  this.actors[this.actingSlot].resultShown = true;
                  GameCanvas.instance.world.battle.commitHpMp();
                  if (this.actors[this.actingSlot].targets != null) {
                     for (int var37 = 0; var37 < this.actors[this.actingSlot].targets.length; var37++) {
                        if (this.actors[this.actors[this.actingSlot].targets[var37]] != null
                           && hasNegativeStatus(this.actors[this.actors[this.actingSlot].targets[var37]])) {
                           this.actors[this.actors[this.actingSlot].targets[var37]].showStatusIcon = true;
                        }
                     }
                  }
               }
            }
         } else if (this.actors[this.actingSlot] != null && this.actors[this.actingSlot].state == 17 && this.actors[this.actingSlot].actionAnimDone) {
            if (this.actors[this.actingSlot].actionAnimDone && this.actors[this.actingSlot].targets != null && !this.actors[this.actingSlot].effectDone) {
               this.drawSkillEffects(var1, this.actors[this.actingSlot].targets);
            }

            if (this.actors[this.actingSlot].effectDone) {
               byte var11 = popupOffsets[this.popupFrame][1];
               byte var25 = popupOffsets[this.popupFrame][1];

               for (int var4 = 0; var4 < this.actors[this.actingSlot].targets.length; var4++) {
                  if (this.getActor(this.actors[this.actingSlot].targets[var4]) != null
                     && this.getActor(this.actors[this.actingSlot].targets[var4]).hp != this.getActor(this.actors[this.actingSlot].targets[var4]).stats[3]) {
                     GameUtil.a(
                        var1,
                        this.getActor(this.actors[this.actingSlot].targets[var4]).hp - this.getActor(this.actors[this.actingSlot].targets[var4]).stats[3],
                        this.getActor(this.actors[this.actingSlot].targets[var4]).homePos.x - var25,
                        this.getActor(this.actors[this.actingSlot].targets[var4]).homePos.y
                           - this.getActor(this.actors[this.actingSlot].targets[var4]).spriteH
                           + var11,
                        this.damageDigits,
                        13,
                        13,
                        33,
                        0
                     );
                  }
               }

               if (this.popupFrame < popupOffsets.length - 1) {
                  if (GameCanvas.instance.frameCounter % 3 == 0) {
                     this.popupFrame++;
                  }
               } else {
                  if (!this.showBackground) {
                     this.showBackground = true;
                  }

                  this.popupFrame = 0;
                  this.actors[this.actingSlot].actionAnimDone = false;
                  this.actors[this.actingSlot].effectDone = false;
                  this.actors[this.actingSlot].state = 0;
                  this.actors[this.actingSlot].setAction(0, 0);
                  GameCanvas.instance.world.battle.commitHpMp();
               }
            }
         }

         Graphics var27 = var1;
         Battle var13 = this;

         for (int var38 = 0; var38 < var13.actors.length; var38++) {
            if (var13.actors[var38] != null) {
               if (var13.actors[var38].hitEffect != null && var13.actors[var38].blowFlag) {
                  var13.actors[var38]
                     .hitEffect
                     .setPosition(var13.getActor(var13.actors[var38].targetSlot).homePos.x, var13.getActor(var13.actors[var38].targetSlot).homePos.y);
                  var13.actors[var38].hitEffect.draw(var27);
                  if (GameCanvas.instance.frameCounter % 3 == 0) {
                     var13.actors[var38].hitEffect.nextFrame(true);
                  }

                  if (var13.actors[var38].hitEffect.getFrame() >= var13.actors[var38].hitEffect.getActionFrameCount(0) - 1) {
                     var13.actors[var38].hitEffect.setFrame(0);
                     var13.actors[var38].blowFlag = false;
                  }
               }

               if (var13.actors[var38].state == 2) {
                  byte var5 = popupOffsets[var13.popupFrame][1];
                  byte var6 = popupOffsets[var13.popupFrame][1];
                  if (var13.getActor(var13.actors[var38].targetSlot) != null) {
                     if (var13.getActor(var13.actors[var38].targetSlot).hp != var13.getActor(var13.actors[var38].targetSlot).stats[3]) {
                        if (var13.getActor(var13.actors[var38].targetSlot).hp <= 0 && GameCanvas.instance.frameCounter % 2 == 0) {
                           var13.getActor(var13.actors[var38].targetSlot).sprite.isVisible = !var13.getActor(var13.actors[var38].targetSlot).sprite.isVisible;
                        }

                        GameUtil.a(
                           var27,
                           var13.getActor(var13.actors[var38].targetSlot).hp - var13.getActor(var13.actors[var38].targetSlot).stats[3],
                           var13.getActor(var13.actors[var38].targetSlot).homePos.x - var6,
                           var13.getActor(var13.actors[var38].targetSlot).homePos.y - var13.getActor(var13.actors[var38].targetSlot).spriteH + var5,
                           var13.damageDigits,
                           13,
                           13,
                           33,
                           0
                        );
                        if (GameCanvas.instance.frameCounter % 2 == 0) {
                           var13.getActor(var13.actors[var38].targetSlot).pos.x = var13.getActor(var13.actors[var38].targetSlot).homePos.x - 4;
                        } else {
                           var13.getActor(var13.actors[var38].targetSlot).pos.x = var13.getActor(var13.actors[var38].targetSlot).homePos.x + 4;
                        }
                     }

                     if (var13.getActor(var13.actors[var38].targetSlot).dodged) {
                        if (var13.getActor(var13.actors[var38].targetSlot).missImage != null) {
                           var27.drawRegion(
                              var13.getActor(var13.actors[var38].targetSlot).missImage,
                              0,
                              0,
                              43,
                              19,
                              0,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.x,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.y - var13.getActor(var13.actors[var38].targetSlot).spriteH,
                              33
                           );
                        } else {
                           var27.setColor(2153971);
                           vh.drawString(
                              var27,
                              "Né",
                              var13.getActor(var13.actors[var38].targetSlot).homePos.x,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.y - var13.getActor(var13.actors[var38].targetSlot).spriteH,
                              33
                           );
                        }
                     }

                     if (var13.actors[var38].isCrit) {
                        if (var13.getActor(var13.actors[var38].targetSlot).missImage != null) {
                           var27.drawRegion(
                              var13.getActor(var13.actors[var38].targetSlot).missImage,
                              0,
                              var13.getActor(var13.actors[var38].targetSlot).missImage.getHeight() >> 1,
                              43,
                              var13.getActor(var13.actors[var38].targetSlot).missImage.getHeight() >> 1,
                              0,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.x,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.y - var13.getActor(var13.actors[var38].targetSlot).spriteH,
                              33
                           );
                        } else {
                           var27.setColor(2153971);
                           vh.drawString(
                              var27,
                              "CRIT",
                              var13.getActor(var13.actors[var38].targetSlot).homePos.x,
                              var13.getActor(var13.actors[var38].targetSlot).homePos.y - var13.getActor(var13.actors[var38].targetSlot).spriteH,
                              33
                           );
                        }
                     }
                  }

                  if (var13.popupFrame < popupOffsets.length - 1) {
                     if (GameCanvas.instance.frameCounter % 3 == 0) {
                        var13.popupFrame++;
                     }
                  } else {
                     var13.popupFrame = 0;
                     if (var13.actors[var38].isCrit) {
                        var13.actors[var38].isCrit = false;
                        var13.critFlash = false;
                     }

                     var13.actors[var38].resultShown = true;
                     var13.actors[var38].state = 15;
                     if (var13.actors[var38].getMoveType() == 1) {
                        var13.actors[var38].sprite.isVisible = false;
                     }

                     var13.actors[var38].setAction(2, 0);
                     if (var13.getActor(var13.actors[var38].targetSlot) != null) {
                        var13.getActor(var13.actors[var38].targetSlot).stats[3] = var13.getActor(var13.actors[var38].targetSlot).hp;
                        if (var13.getActor(var13.actors[var38].targetSlot).stats[3] <= 0) {
                           var13.getActor(var13.actors[var38].targetSlot).stats[3] = 0;
                           var13.getActor(var13.actors[var38].targetSlot).hp = 0;
                           var13.getActor(var13.actors[var38].targetSlot).isDead = true;
                           var13.getActor(var13.actors[var38].targetSlot).state = 128;
                           var13.getActor(var13.actors[var38].targetSlot).sprite.isVisible = false;
                        }
                     }
                  }
               }
            }
         }

         if (this.actors[this.actingSlot] != null && this.actors[this.actingSlot].inCombo) {
            drawTopBar(var1);
            Utils.drawOutlinedText(var1, "Hợp kích", DeviceConfig.screenSize[0] >> 1, 6 + Utils.lineHeight, 13695223, 1196461, 33);
         }

         for (int var14 = 10; var14 < 20; var14++) {
            if (this.actors[var14] != null && this.actors[var14].sprite != null && this.actors[var14].stats[3] > 0 && this.actors[var14].showStatusIcon) {
               drawStatusIcon(var1, this.actors[var14]);
            }
         }

         for (int var15 = 9; var15 >= 0; var15--) {
            if (this.actors[var15] != null && this.actors[var15].sprite != null && this.actors[var15].stats[3] > 0 && this.actors[var15].showStatusIcon) {
               drawStatusIcon(var1, this.actors[var15]);
            }
         }

         if (this.phase != 0) {
            TouchSoftKeys.setSoftKeys(0);
         }

         if (this.phase == 0) {
            var27 = var1;
            var13 = this;
            if (!this.roundEffectsShown && popupOffsets != null) {
               byte var39 = popupOffsets[this.popupFrame][1];
               byte var46 = popupOffsets[this.popupFrame][1];

               for (int var49 = 0; var49 < var13.actors.length; var49++) {
                  if (var13.actors[var49] != null && var13.actors[var49].hp < var13.actors[var49].stats[3]) {
                     GameUtil.a(
                        var27,
                        var13.actors[var49].hp - var13.actors[var49].stats[3],
                        var13.actors[var49].homePos.x - var46,
                        var13.actors[var49].homePos.y - var13.actors[var49].spriteH + var39,
                        var13.damageDigits,
                        13,
                        13,
                        33,
                        0
                     );
                     var27.setColor(16711680);
                  }
               }

               if (var13.popupFrame < popupOffsets.length - 1) {
                  if (GameCanvas.instance.frameCounter % 3 == 0) {
                     var13.popupFrame++;
                  }
               } else {
                  var13.roundEffectsShown = true;
                  var13.popupFrame = 0;
                  int var50 = 0;

                  for (int var29 = 10; var29 < var13.actors.length; var29++) {
                     if (var13.actors[var29] != null) {
                        var13.actors[var29].stats[3] = var13.actors[var29].hp;
                        if (var13.actors[var29].stats[3] <= 0 && var13.actors[var29].sprite != null && var13.actors[var29].sprite.isVisible) {
                           var13.actors[var29].stats[3] = 0;
                           var13.actors[var29].hp = 0;
                        }

                        if (var13.actors[var29].isPresent) {
                           var50 += var13.actors[var29].stats[3];
                        }
                     }
                  }

                  if (var50 <= 0) {
                     var13.onVictory();
                  } else {
                     for (int var30 = 0; var30 < 10; var30++) {
                        if (var13.actors[var30] != null) {
                           if (var13.actors[var30].hp <= 0) {
                              var13.actors[var30].stats[3] = 0;
                              var13.actors[var30].hp = 0;
                              var13.actors[var30].sprite.isVisible = false;
                              var13.actors[var30].isDead = true;
                              var13.actors[var30].state = 128;
                           }

                           if (var13.actors[var30].mp < 0) {
                              var13.actors[var30].stats[5] = 0;
                              var13.actors[var30].mp = 0;
                           }
                        }
                     }

                     if (var13.actors[0].stats[3] <= 0) {
                        var13.onDefeat();
                     }
                  }
               }
            }

            if (this.actors[this.commandSlot] != null) {
               this.drawStatusPanel(var1, this.actors[this.commandSlot]);
            }

            this.drawCommandMenu(var1);
            var13 = this;
            if (this.isSummoning) {
               if (this.summonEffect != null) {
                  if (this.menuMode == 66) {
                     if (this.commandSlot == 1) {
                        this.summonEffect.x = this.allyX[0];
                        this.summonEffect.y = this.allyY[0];
                     } else {
                        this.summonEffect.x = this.allyX[this.commandSlot];
                        this.summonEffect.y = this.allyY[this.commandSlot];
                     }
                  } else {
                     this.summonEffect.x = this.allyX[this.summonSlot];
                     this.summonEffect.y = this.allyY[this.summonSlot];
                  }

                  GameUtil.drawAnimation(var1, this.summonEffect, this.summonEffect.x, this.summonEffect.y, true, 0);
                  if (this.summonEffect.getFrame() >= this.summonEffect.getFrameCount() - 1) {
                     if (this.menuMode == 66) {
                        this.saveStatsToParty();
                        if (this.party[this.commandSlot] != null) {
                           for (int var40 = 0; var40 < GameData.myPets.length; var40++) {
                              if (GameData.myPets[var40].uid == var13.party[var13.commandSlot].petUid) {
                                 GameData.myPets[var40].isDeployed = false;
                                 if (var13.party[var13.commandSlot].stats[3] > 0) {
                                    GameData.myPets[var40].isFainted = false;
                                 } else {
                                    GameData.myPets[var40].isFainted = true;
                                 }
                                 break;
                              }
                           }
                        }

                        GameData.syncPetsFromBattle(var13.party);
                        var13.petRoster = GameData.getReservePets();
                        var13.party[var13.commandSlot] = null;
                        var13.actors[var13.commandSlot] = null;
                        var13.buildTurnOrder();
                        var13.confirmCommand();
                     } else {
                        this.summonPet();
                     }

                     var13.isSummoning = false;
                  }
               } else {
                  this.isSummoning = false;
               }
            }

            if ((
                  DeviceConfig.modelName.equals("E6")
                     || DeviceConfig.modelName.equals("E680")
                     || DeviceConfig.modelName.equals("W958C")
                     || DeviceConfig.modelName.equals("S1")
                     || DeviceConfig.modelName.equals("N95")
                     || DeviceConfig.modelName.equals("N5800")
               )
               && (this.menuMode == 1 || this.menuMode == 21 || this.menuMode == 41 || this.menuMode == 51)) {
               if (this.touchBackImage != null) {
                  Utils.drawFrame(
                     var1,
                     this.touchBackImage,
                     this.touchBackImage.getWidth() / 2,
                     this.touchBackImage.getHeight(),
                     1,
                     GameCanvas.instance.screenWidth - 15,
                     GameCanvas.instance.screenHeight - 15,
                     3,
                     0
                  );
               } else {
                  this.touchBackImage = ResourceCache.loadImage("/pic/D23.gf", 1);
               }
            }
         } else if (this.phase == 5) {
            this.drawScoutResult(var1);
         } else if (this.phase == 6) {
            for (int var18 = 0; var18 < 20; var18++) {
               if (this.actors[var18] != null && this.actors[var18].state == 9 && this.actors[var18].actionAnimDone) {
                  byte var31 = popupOffsets[this.popupFrame][1];
                  byte var41 = popupOffsets[this.popupFrame][1];
                  if (this.getActor(this.actors[var18].targetSlot) != null
                     && this.getActor(this.actors[var18].targetSlot).hp != this.getActor(this.actors[var18].targetSlot).stats[3]) {
                     GameUtil.a(
                        var1,
                        this.getActor(this.actors[var18].targetSlot).hp - this.getActor(this.actors[var18].targetSlot).stats[3],
                        this.getActor(this.actors[var18].targetSlot).homePos.x - var41,
                        this.getActor(this.actors[var18].targetSlot).homePos.y - this.getActor(this.actors[var18].targetSlot).spriteH + var31,
                        this.damageDigits,
                        13,
                        13,
                        33,
                        0
                     );
                  }

                  if (this.getActor(this.actors[var18].targetSlot) != null
                     && this.getActor(this.actors[var18].targetSlot).mp != this.getActor(this.actors[var18].targetSlot).stats[5]) {
                     GameUtil.a(
                        var1,
                        this.getActor(this.actors[var18].targetSlot).mp - this.getActor(this.actors[var18].targetSlot).stats[5],
                        this.getActor(this.actors[var18].targetSlot).homePos.x - var41,
                        this.getActor(this.actors[var18].targetSlot).homePos.y - this.getActor(this.actors[var18].targetSlot).spriteH + var31 - 15,
                        this.damageDigits,
                        13,
                        13,
                        33,
                        1
                     );
                  }

                  if (this.popupFrame < popupOffsets.length - 1) {
                     if (GameCanvas.instance.frameCounter % 3 == 0) {
                        this.popupFrame++;
                     }
                  } else {
                     this.popupFrame = 0;
                     GameCanvas.instance.world.battle.commitHpMp();
                     this.actors[var18].actionAnimDone = false;
                     this.actors[var18].setAction(0, 0);
                     this.actors[var18].state = 0;
                  }
               }
            }
         } else if (this.phase == 4) {
            this.dialog.draw(var1);
         } else if (this.phase == 24) {
            int var32 = (DeviceConfig.screenSize[1] - 9 * (Utils.lineHeight + 1)) / 2;
            int var19 = DeviceConfig.screenSize[0] - 16;
            int var20 = (DeviceConfig.screenSize[0] - var19) / 2;
            GameUtil.drawPanel(var1, var20, var32, DeviceConfig.screenSize[0] - var20 * 2, 9 * (Utils.lineHeight + 1), 1317669, 15788720, 80, true);
            var1.setColor(16709092);
            var1.fillRect(var20 + 10, var32 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 5, DeviceConfig.screenSize[0] - 2 * (var20 + 10), 1);
            var1.setColor(3027509);
            var1.fillRect(var20 + 10, var32 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 6, DeviceConfig.screenSize[0] - 2 * (var20 + 10), 1);
            Utils.drawOutlinedText(var1, "Thắng trận", DeviceConfig.screenSize[0] >> 1, var32 + 4, 16706636, 9193495, 17);
            int var33 = var32 + Utils.lineHeight * 3 / 2;
            var1.setColor(16777215);
            if (this.resultPage == 0) {
               if (this.rewardItemLines != null) {
                  Utils.drawOutlinedText(var1, "Nhận vật phẩm", DeviceConfig.screenSize[0] / 2, var33 + 15, 9240572, 2314880, 17);
                  if (this.rewardItemLines.length > 6) {
                     GameUtil.drawScrollBar(
                        var1,
                        null,
                        null,
                        DeviceConfig.screenSize[0] - 25,
                        Utils.lineHeight + 1 + var33 + 5,
                        3 * (Utils.lineHeight + 1),
                        this.rewardItemLines.length,
                        3,
                        rewardScroll
                     );
                  }

                  for (short var42 = 0; var42 < 6; var42++) {
                     if (rewardScroll + var42 < this.rewardItemLines.length) {
                        var1.setColor(16777215);
                        vh.drawString(
                           var1,
                           this.rewardItemLines[var42 + rewardScroll],
                           DeviceConfig.screenSize[0] / 2,
                           var33 + 12 + (Utils.lineHeight + 1) * (var42 + 1),
                           17
                        );
                     }
                  }
               }
            } else if (this.resultPage == 2) {
               RoleSprite var43 = (RoleSprite)this.levelUps.elementAt(this.levelUpIndex);
               int var47 = 0;

               for (int var21 = 0; var21 < this.party.length; var21++) {
                  if (this.party[var21] != null && var43.name.equals(this.party[var21].name)) {
                     var47 = var21;
                     break;
                  }
               }

               Utils.drawOutlinedText(
                  var1, var43.name + " lên " + this.party[var47].stats[0] + " cấp", DeviceConfig.screenSize[0] / 2, var33 + 15, 9240572, 2314880, 17
               );
               if (var47 == 0) {
                  Utils.drawOutlinedText(var1, "Thêm 10 điểm", DeviceConfig.screenSize[0] / 2, var33 + 20 + Utils.lineHeight, 9240572, 2314880, 17);
                  Utils.drawOutlinedText(var1, "Hãy phân bổ hợp lý", DeviceConfig.screenSize[0] / 2, var33 + 20 + (Utils.lineHeight << 1), 9240572, 2314880, 17);
               } else {
                  int var22 = var33 + 15 + Utils.lineHeight;
                  this.drawStatGain(var1, 0, var43.stats[10], this.party[var47].stats[10], var22 - 10 + 8 + Utils.lineHeight + 1);
                  this.drawStatGain(var1, 1, var43.stats[11], this.party[var47].stats[11], var22 - 10 + 8 + (Utils.lineHeight + 1 << 1));
                  this.drawStatGain(var1, 3, var43.stats[12], this.party[var47].stats[12], var22 - 10 + 8 + (Utils.lineHeight + 1) * 3);
                  this.drawStatGain(var1, 4, var43.stats[13], this.party[var47].stats[13], var22 - 10 + 8 + (Utils.lineHeight + 1 << 2));
                  this.drawStatGain(var1, 5, var43.stats[14], this.party[var47].stats[14], var22 - 10 + 8 + (Utils.lineHeight + 1) * 5);
               }
            } else if (this.resultPage == 1) {
               Utils.drawOutlinedText(var1, "Nhận EXP", DeviceConfig.screenSize[0] / 2, var33 + 15, 9240572, 2314880, 17);

               for (short var44 = 0; var44 < 3; var44++) {
                  if (this.rewardExpLines != null && var44 < this.rewardExpLines.length) {
                     var1.setColor(16777215);
                     vh.drawString(var1, this.rewardExpLines[var44], DeviceConfig.screenSize[0] / 2, var33 + 12 + (Utils.lineHeight + 1) * (var44 + 1), 17);
                  }
               }
            }
         } else if (this.phase == 25) {
            int var34 = (DeviceConfig.screenSize[1] - 9 * (Utils.lineHeight + 1)) / 2;
            int var23 = (DeviceConfig.screenSize[0] - 190) / 2;
            GameUtil.drawPanel(var1, var23, var34, DeviceConfig.screenSize[0] - var23 * 2, 9 * (Utils.lineHeight + 1), 1317669, 15788720, 80, true);
            var1.setColor(16709092);
            var1.fillRect(var23 + 10, var34 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 5, DeviceConfig.screenSize[0] - 2 * (var23 + 10), 1);
            var1.setColor(3027509);
            var1.fillRect(var23 + 10, var34 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 6, DeviceConfig.screenSize[0] - 2 * (var23 + 10), 1);
            Utils.drawOutlinedText(var1, "Thua trận", DeviceConfig.screenSize[0] >> 1, var34 + 4, 16706636, 9193495, 17);
            var1.setColor(this.textColor);
            if (this.defeatLines != null && this.defeatLineIndex < this.defeatLines.length) {
               Utils.drawOutlinedText(
                  var1,
                  this.defeatLines[this.defeatLineIndex],
                  DeviceConfig.screenSize[0] / 2,
                  var34 + Utils.lineHeight * 3,
                  this.textColor,
                  this.textOutlineColor,
                  33
               );
            }
         } else if (this.phase == 8 && this.actors[this.actingSlot].state == 16) {
            if (this.allInComboWait(this.actors[this.actingSlot].comboPartners) && this.comboIndex >= this.actors[this.actingSlot].comboPartners.length) {
               this.actors[this.actingSlot].actionAnimDone = true;
               if (this.actors[this.actingSlot].getMoveType() == 0) {
                  this.actors[this.actingSlot].startReturn();
               } else {
                  this.actors[this.actingSlot].sprite.isVisible = false;
               }

               this.actors[this.actingSlot].setAction(2, 0);
               this.actors[this.actingSlot].hasArrived = false;
               this.actors[this.actingSlot].state = 2;

               for (int var24 = 0; var24 < this.actors[this.actingSlot].comboPartners.length; var24++) {
                  this.getActor(this.actors[this.actingSlot].comboPartners[var24]).actionAnimDone = true;
                  if (this.getActor(this.actors[this.actingSlot].comboPartners[var24]).getMoveType() == 0) {
                     this.getActor(this.actors[this.actingSlot].comboPartners[var24]).startReturn();
                  } else {
                     this.getActor(this.actors[this.actingSlot].comboPartners[var24]).sprite.isVisible = false;
                  }

                  this.getActor(this.actors[this.actingSlot].comboPartners[var24]).hasArrived = false;
                  this.getActor(this.actors[this.actingSlot].comboPartners[var24]).inCombo = false;
                  this.getActor(this.actors[this.actingSlot].comboPartners[var24]).state = 15;
                  this.getActor(this.actors[this.actingSlot].comboPartners[var24]).setAction(2, 0);
               }

               this.actors[this.actingSlot].inCombo = false;
               this.phase = 3;
               this.comboIndex = 0;
               this.actors[this.actingSlot].comboPartners = null;
            } else if (this.getActor(this.actors[this.actingSlot].comboPartners[this.comboIndex]).state == 13) {
               if (this.getActor(this.actors[this.actingSlot].comboPartners[this.comboIndex]).sprite.getAction() == 0) {
                  this.getActor(this.actors[this.actingSlot].comboPartners[this.comboIndex]).setAction(3, 0);
               }
            } else if (this.getActor(this.actors[this.actingSlot].comboPartners[this.comboIndex]).state == 16
               && this.comboIndex < this.actors[this.actingSlot].comboPartners.length) {
               this.comboIndex++;
            }
         }

         if (GameSettings.goodOrBad != 1 && this.showBackground) {
            this.drawParticles(var1);
         }
      }
   }

   private void drawScoutResult(Graphics var1) {
      int var5;
      int var3 = (var5 = (DeviceConfig.screenSize[1] - 9 * (Utils.lineHeight + 1)) / 2) + 6 * (Utils.lineHeight + 1) - 10;
      int var2;
      int var4 = (var2 = (DeviceConfig.screenSize[0] - 160) / 2) + 8;
      GameUtil.drawPanel(var1, var2, var5, 160, 9 * (Utils.lineHeight + 2), 1317669, 15788720, 80, true);
      var1.setColor(16709092);
      var1.fillRect(var2 + 10, var5 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 5, DeviceConfig.screenSize[0] - 2 * (var2 + 10), 1);
      var1.setColor(3027509);
      var1.fillRect(var2 + 10, var5 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 6, DeviceConfig.screenSize[0] - 2 * (var2 + 10), 1);
      var1.setColor(16711680);
      Utils.drawOutlinedText(var1, "Thêm vào đồ giám thành công", DeviceConfig.screenSize[0] >> 1, var5 + 4, 16706636, 9193495, 17);
      GameUtil.drawAnimation(var1, this.scoutedSprite, var2 + 80, var5 - 10 + 5 * (Utils.lineHeight + 1), true, 0);
      Utils.drawOutlinedText(var1, this.actors[this.scoutedSlot].name, DeviceConfig.screenSize[0] / 2, var3, 9240572, 2314880, 33);
      byte[] var6 = Utils.parseByteArray(this.petTable[this.actors[this.scoutedSlot].roleId - 1][18], ",");
      Utils.drawOutlinedText8(var1, "Gen đặc trưng", var4 - 6, var3, 16706636, 9128217, 20);
      if (GameData.itemNames == null) {
         GameData.loadItemTable();
      }

      var2 = GameData.findItemIndex(var6[0]);
      var5 = vh.stringWidth(Utils.defaultFont, "Gen đặc trưng");
      var1.setColor(16777215);
      vh.drawString(var1, GameData.itemNames[var2], var4 - 6 + var5, var3, 20);
      if (this.scoutedStars != null) {
         if (this.statIcons == null) {
            this.statIcons = ResourceCache.loadImage("/pic/D13.gf");
         }

         var3 += Utils.lineHeight + 1;
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, 0, var4, var3, 20, 0);
         var3 -= 3;
         drawStars(var1, this.starImage, var4 + 30, var3 + 8, this.scoutedStars[0], 3);
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, 3, var4 + 80, var3, 20, 0);
         drawStars(var1, this.starImage, var4 + 30 + 80, var3 + 8, this.scoutedStars[2], 3);
         var3 += 16;
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, 1, var4, var3, 20, 0);
         drawStars(var1, this.starImage, var4 + 30, var3 + 8, this.scoutedStars[1], 3);
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, 4, var4 + 80, var3, 20, 0);
         drawStars(var1, this.starImage, var4 + 30 + 80, var3 + 8, this.scoutedStars[3], 3);
         var3 += 16;
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, 5, var4, var3, 20, 0);
         drawStars(var1, this.starImage, var4 + 30, var3 + 8, this.scoutedStars[4], 3);
         this.drawElements(var1, var4 + 80, var3 - 2 + 8, this.actors[this.scoutedSlot].baseElements);
      }
   }

   private static void drawStars(Graphics var0, Image var1, int var2, int var3, int var4, int var5) {
      var5 = var4 / 2;

      for (int var6 = 0; var5 > 0 && var6 < var5; var6++) {
         var0.drawRegion(var1, 0, 0, var1.getWidth() / 2, var1.getHeight(), 0, var2 + var6 * var1.getWidth() / 2, var3, 3);
      }

      if (var4 % 2 != 0) {
         var0.drawRegion(var1, var1.getWidth() / 2, 0, var1.getWidth() / 2, var1.getHeight(), 0, var2 + var5 * var1.getWidth() / 2, var3, 3);
      }
   }

   private void drawStatGain(Graphics var1, int var2, int var3, int var4, int var5) {
      Utils.drawFrame(
         var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, var2, DeviceConfig.screenSize[0] / 2 - 50 - 15, var5, 24, 0
      );
      Utils.drawNumber(var1, var3, DeviceConfig.screenSize[0] / 2 - 15, var5 + 5, this.digitsBig, 6, 8, 24);
      var1.drawRegion(this.gainArrow, 0, 0, 7, 7, 0, DeviceConfig.screenSize[0] / 2, var5 + 5, 17);
      Utils.drawNumber(var1, var4, DeviceConfig.screenSize[0] / 2 + 15, var5 + 5, this.digitsBig, 6, 8, 20);
   }

   private static void drawStatusIcon(Graphics var0, BattleActor var1) {
      int var2 = var1.pos.x;
      int var3 = var1.pos.y - var1.spriteH / 2;
      if (var1.poison[0] > 0) {
         GameUtil.drawAnimation(var0, var1.poisonEffect, var2, var3, true, 0);
      } else if (var1.confuseTurns > 0) {
         GameUtil.drawAnimation(var0, var1.confuseEffect, var2, var3, true, 0);
      } else {
         if (var1.stunTurns > 0) {
            GameUtil.drawAnimation(var0, var1.stunEffect, var2, var3, true, 0);
         }
      }
   }

   private void drawSkillEffects(Graphics var1, byte[] var2) {
      if (this.actors[this.actingSlot].effectSprites == null) {
         this.actors[this.actingSlot].effectDone = true;
      } else {
         if (!this.actors[this.actingSlot].effectDone && var2.length != this.actors[this.actingSlot].effectSprites.length) {
            System.out.println("Có lỗi khi khởi tạo new");
         }

         for (int var3 = 0; var3 < var2.length; var3++) {
            this.actors[this.actingSlot].effectSprites[var3].draw(var1);
            if (this.actors[this.actingSlot].effectSprites[var3].getFrame() >= this.actors[this.actingSlot].effectSprites[var3].getActionFrameCount(0) - 1) {
               this.actors[this.actingSlot].effectDone = true;
               this.actors[this.actingSlot].effectSprites[var3].setFrame(0);
               byte var5 = var2[var3];
               this.actors[var5].sprite.isVisible = true;
            } else if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.actors[this.actingSlot].effectSprites[var3].nextFrame(true);
            }
         }
      }
   }

   private void drawParticles(Graphics var1) {
      byte var2 = 5;
      if (DeviceConfig.screenSize[0] > 240) {
         var2 = 10;
      }

      if (this.particles == null) {
         this.particles = new short[var2][3];

         for (int var3 = 0; var3 < this.particles.length; var3++) {
            this.particles[var3][0] = (short)GameUtil.randomBetween(5, DeviceConfig.screenSize[0] - 10);
            this.particles[var3][1] = (short)GameUtil.randomBetween(DeviceConfig.screenSize[1] >> 2, DeviceConfig.screenSize[1] - 20);
            this.particles[var3][2] = (short)GameUtil.randomBetween(0, 2);
         }
      }

      for (int var4 = 0; this.particles != null && var4 < this.particles.length; var4++) {
         if (this.particles[var4][2] == 1) {
            var1.drawImage(this.grainImage1, this.particles[var4][0], this.particles[var4][1], 3);
         } else if (this.particles[var4][2] == 2) {
            var1.drawImage(this.grainImage2, this.particles[var4][0], this.particles[var4][1], 3);
         }

         this.particles[var4][1]--;
         if (this.particles[var4][1] < 5) {
            this.particles[var4][0] = (short)GameUtil.randomBetween(5, DeviceConfig.screenSize[0] - 10);
            this.particles[var4][1] = (short)GameUtil.randomBetween(DeviceConfig.screenSize[1] / 4, (DeviceConfig.screenSize[1] << 1) / 3 - 10);
            this.particles[var4][2] = (short)GameUtil.randomBetween(0, 2);
         }
      }
   }

   private void drawStatusPanel(Graphics var1, BattleActor var2) {
      int[][] var3 = new int[][]{{var2.stats[3], var2.stats[10]}, {var2.stats[5], var2.stats[11]}, {var2.stats[1], var2.stats[2]}};
      var1.drawImage(this.statusPanelImages[0], 0, DeviceConfig.screenSize[1], 36);
      int var4 = 0;
      if (var2.slot == 0) {
         if (this.heroFace != null) {
            this.heroFace.setPosition(0, DeviceConfig.screenSize[1] - 5);
            this.heroFace.draw(var1);
            if (this.roleNameImage != null) {
               Utils.drawFrame(
                  var1,
                  this.roleNameImage,
                  this.roleNameImage.getWidth(),
                  this.roleNameImage.getHeight() >> 1,
                  var2.roleId - 1,
                  0,
                  DeviceConfig.screenSize[1] - 5,
                  36,
                  0
               );
            } else {
               Utils.drawOutlinedText8(var1, var2.name, 0, DeviceConfig.screenSize[1] - 5, 9502459, 540331, 36);
            }

            short[] var6;
            if ((var6 = this.heroFace.getBoxesAt(0, 0, 0)) != null) {
               var4 = 0 + var6[2];
            } else {
               var4 += 80;
            }
         } else {
            var4 += 30;
         }
      } else {
         var1.drawImage(this.namePlate, 0, DeviceConfig.screenSize[1] - Utils.lineHeight, 36);
         Utils.drawOutlinedText8(var1, var2.name, 5, DeviceConfig.screenSize[1] - 5, 9502459, 540331, 36);
         var4 += 80;
      }

      int var5 = DeviceConfig.screenSize[1] - 6 - 39;

      for (int var11 = 0; var11 < 3; var11++) {
         Utils.drawFrame(
            var1,
            this.statusPanelImages[1],
            this.statusPanelImages[1].getWidth(),
            this.statusPanelImages[1].getHeight() / 3,
            var11,
            var4 + (var11 << 1),
            var5 + var11 * 13,
            20,
            0
         );
         Utils.drawFrame(
            var1,
            this.statusPanelImages[2],
            var3[var11][0] * this.statusPanelImages[2].getWidth() / var3[var11][1],
            this.statusPanelImages[2].getHeight() / 3,
            var11,
            var4 + this.statusPanelImages[1].getWidth() + 2 + (var11 << 1),
            var5 + 3 + var11 * 13,
            20,
            0
         );
         drawBarFrame(
            var1,
            var4 + this.statusPanelImages[1].getWidth() + 2 + (var11 << 1),
            var5 + 3 + var11 * 13,
            this.statusPanelImages[2].getWidth() - 2,
            16773874,
            6509908,
            this.statusPanelImages[2].getHeight() / 3
         );
         Utils.drawNumber(
            var1,
            var3[var11][0],
            var4 + 10 + this.statusPanelImages[1].getWidth() + 2 + (var11 << 1),
            var5 + var11 * 13,
            this.digits,
            this.digits.getWidth() / 14,
            this.digits.getHeight(),
            20
         );
      }

      if (this.elementIcons != null) {
         int var10002 = var4 + 18 + this.statusPanelImages[1].getWidth() + this.statusPanelImages[2].getWidth();
         int var10003 = var5 + 10;
         byte[] var10 = var2.baseElements;
         var4 = var10003;
         int var7 = var10002;
         if (var10 != null) {
            if (var10[0] > 0) {
               var1.drawRegion(
                  this.elementIcons,
                  this.elementIcons.getWidth() / 2,
                  this.elementIcons.getHeight() / 2,
                  this.elementIcons.getWidth() / 2,
                  this.elementIcons.getHeight() / 2,
                  0,
                  var7,
                  var4,
                  3
               );
               Utils.drawNumber(var1, var10[0], var7 + this.elementIcons.getWidth() / 4, var4 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
               var7 += 16;
               var4 += 16;
            }

            if (var10[1] > 0) {
               var1.drawRegion(
                  this.elementIcons, this.elementIcons.getWidth() / 2, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var7, var4, 3
               );
               Utils.drawNumber(var1, var10[1], var7 + this.elementIcons.getWidth() / 4, var4 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
               var7 += 16;
               var4 += 16;
            }

            if (var10[2] > 0) {
               var1.drawRegion(this.elementIcons, 0, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var7, var4, 3);
               Utils.drawNumber(var1, var10[2], var7 + this.elementIcons.getWidth() / 4, var4 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
               var7 += 16;
               var4 += 16;
            }

            if (var10[3] > 0) {
               var1.drawRegion(
                  this.elementIcons,
                  0,
                  this.elementIcons.getHeight() / 2,
                  this.elementIcons.getWidth() / 2,
                  this.elementIcons.getHeight() / 2,
                  0,
                  var7,
                  var4,
                  3
               );
               Utils.drawNumber(var1, var10[3], var7 + this.elementIcons.getWidth() / 4, var4 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
            }
         }
      } else {
         System.out.println("Ảnh Địa Thủy Hỏa Phong trống");
      }
   }

   private static void drawBarFrame(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      var0.setColor(var4);
      var0.drawRect(var1, var2, var3, var6 - 2);
      var0.setColor(var5);
      var0.drawRoundRect(var1 - 1, var2 - 1, var3 + 2, var6, 4, 4);
   }

   private void drawCommandMenu(Graphics var1) {
      if (this.menuMode == 0) {
         Graphics var2 = var1;
         Battle var6 = this;
         drawTopBar(var1);
         if (this.actors[this.commandSlot] != null) {
            if (this.actors[this.commandSlot].stats[3] > 0) {
               String var3 = this.actors[this.commandSlot].name + " Bắt đầu tấn công";
               if (vh.stringWidth(Utils.defaultFont, var3) <= DeviceConfig.screenSize[0]) {
                  Utils.drawOutlinedText(var1, var3, DeviceConfig.screenSize[0] / 2, 6 + Utils.lineHeight, 15987162, 5971280, 33);
               } else {
                  GameUtil.drawMarquee(var1, var3, 2);
               }

               int var10 = Utils.lineHeight + 11;
               int var4 = 0;
               if (this.actors[this.commandSlot].silenceTurns > 0) {
                  var1.drawImage(this.silenceIcon, 2, var10, 20);
                  Utils.drawNumber(var1, this.actors[this.commandSlot].silenceTurns, 6, var10 + 4, this.digits, 6, 7, 20);
                  var4 = 1;
               }

               if (this.actors[this.commandSlot].attackBuff[0] > 0) {
                  var1.drawImage(this.buffIcon, 2 + var4 * 20, var10, 20);
                  Utils.drawNumber(var1, this.actors[this.commandSlot].attackBuff[0], 6 + var4 * 21, var10 + 4, this.digits, 6, 7, 20);
                  var4++;
               }

               if (this.actors[this.commandSlot].rallyBuff[0] > 0) {
                  var1.drawImage(this.buffIcon, 2 + var4 * 20, var10, 20);
                  Utils.drawNumber(var1, this.actors[this.commandSlot].rallyBuff[0], 6 + var4 * 21, var10 + 4, this.digits, 6, 7, 20);
               }
            } else {
               Utils.drawOutlinedText(
                  var1,
                  this.actors[this.commandSlot].name + " tử trận",
                  DeviceConfig.screenSize[0] / 2,
                  6 + Utils.lineHeight,
                  this.textColor,
                  this.textOutlineColor,
                  33
               );
            }
         }

         this.ringCenter.drawAt(var1, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2);
         if (this.commandSlot != 0) {
            for (int var12 = 0; var12 < 4; var12++) {
               if (!var6.isRingTurningRight && !var6.isRingTurningLeft) {
                  if (var12 != 0) {
                     Utils.drawFrame(
                        var2,
                        var6.commandIconsDim,
                        var6.commandIconsDim.getWidth() / 11,
                        var6.commandIconsDim.getHeight(),
                        (var12 + var6.commandIndex) % 11,
                        var6.petRing[var12][0] + DeviceConfig.screenSize[0] / 2,
                        var6.petRing[var12][1] + DeviceConfig.screenSize[1] / 2,
                        20,
                        0
                     );
                  } else {
                     Utils.drawFrame(
                        var2,
                        var6.commandIcons,
                        var6.commandIcons.getWidth() / 11,
                        var6.commandIcons.getHeight(),
                        (var12 + var6.commandIndex) % 11,
                        var6.petRing[var12][0] + DeviceConfig.screenSize[0] / 2,
                        var6.petRing[var12][1] + DeviceConfig.screenSize[1] / 2,
                        20,
                        0
                     );
                  }

                  Utils.drawFrame(
                     var2,
                     var6.commandLabels,
                     var6.commandLabels.getWidth(),
                     var6.commandLabels.getHeight() / 11,
                     var6.commandIndex,
                     14 + DeviceConfig.screenSize[0] / 2,
                     -63 + DeviceConfig.screenSize[1] / 2,
                     20,
                     0
                  );
               } else {
                  if (var6.isRingTurningLeft) {
                     Utils.drawFrame(
                        var2,
                        var6.commandIconsDim,
                        var6.commandIconsDim.getWidth() / 11,
                        var6.commandIconsDim.getHeight(),
                        (var12 + 1 + var6.commandIndex) % 11,
                        var6.petRingTurning[var12][0] + DeviceConfig.screenSize[0] / 2,
                        var6.petRingTurning[var12][1] + DeviceConfig.screenSize[1] / 2,
                        20,
                        0
                     );
                  }

                  if (var6.isRingTurningRight) {
                     Utils.drawFrame(
                        var2,
                        var6.commandIconsDim,
                        var6.commandIconsDim.getWidth() / 11,
                        var6.commandIconsDim.getHeight(),
                        (var12 + var6.commandIndex) % 11,
                        var6.petRingTurning[var12][0] + DeviceConfig.screenSize[0] / 2,
                        var6.petRingTurning[var12][1] + DeviceConfig.screenSize[1] / 2,
                        20,
                        0
                     );
                  }
               }

               if (var6.isRingTurningLeft) {
                  var6.isRingTurningLeft = false;
               }

               if (var6.isRingTurningRight) {
                  var6.isRingTurningRight = false;
               }
            }

            return;
         }

         for (int var11 = 0; var11 < 7; var11++) {
            if (!var6.isRingTurningRight && !var6.isRingTurningLeft) {
               if (var11 != 0) {
                  Utils.drawFrame(
                     var2,
                     var6.commandIconsDim,
                     var6.commandIconsDim.getWidth() / 11,
                     var6.commandIconsDim.getHeight(),
                     (var11 + var6.commandIndex) % 7,
                     var6.heroRing[var11][0] + DeviceConfig.screenSize[0] / 2,
                     var6.heroRing[var11][1] + DeviceConfig.screenSize[1] / 2,
                     20,
                     0
                  );
               } else {
                  Utils.drawFrame(
                     var2,
                     var6.commandIcons,
                     var6.commandIcons.getWidth() / 11,
                     var6.commandIcons.getHeight(),
                     (var11 + var6.commandIndex) % 7,
                     var6.heroRing[var11][0] + DeviceConfig.screenSize[0] / 2,
                     var6.heroRing[var11][1] + DeviceConfig.screenSize[1] / 2,
                     20,
                     0
                  );
               }

               Utils.drawFrame(
                  var2,
                  var6.commandLabels,
                  var6.commandLabels.getWidth(),
                  var6.commandLabels.getHeight() / 11,
                  var6.commandIndex,
                  14 + DeviceConfig.screenSize[0] / 2,
                  -63 + DeviceConfig.screenSize[1] / 2,
                  20,
                  0
               );
            } else {
               if (var6.isRingTurningLeft) {
                  Utils.drawFrame(
                     var2,
                     var6.commandIconsDim,
                     var6.commandIconsDim.getWidth() / 11,
                     var6.commandIconsDim.getHeight(),
                     (var11 + 1 + var6.commandIndex) % 7,
                     var6.heroRingTurning[var11][0] + DeviceConfig.screenSize[0] / 2,
                     var6.heroRingTurning[var11][1] + DeviceConfig.screenSize[1] / 2,
                     20,
                     0
                  );
               }

               if (var6.isRingTurningRight) {
                  Utils.drawFrame(
                     var2,
                     var6.commandIconsDim,
                     var6.commandIconsDim.getWidth() / 11,
                     var6.commandIconsDim.getHeight(),
                     (var11 + var6.commandIndex) % 7,
                     var6.heroRingTurning[var11][0] + DeviceConfig.screenSize[0] / 2,
                     var6.heroRingTurning[var11][1] + DeviceConfig.screenSize[1] / 2,
                     20,
                     0
                  );
               }
            }
         }

         if (var6.isRingTurningLeft) {
            var6.isRingTurningLeft = false;
         }

         if (!var6.isRingTurningRight) {
            return;
         }

         var6.isRingTurningRight = false;
      } else {
         if (this.menuMode == 1 || this.menuMode == 21) {
            this.drawTargetInfo(var1, this.targetCursor);
            return;
         }

         if (this.menuMode == 51) {
            if (!this.isSummoning) {
               var1.drawRegion(this.arrowImage, 15, 0, 15, 15, 0, this.allyX[this.summonSlot], this.allyY[this.summonSlot] - 20 + this.cursorBob, 33);
               return;
            }
         } else if (this.menuMode == 66) {
            drawTopBar(var1);
            if (!this.isSummoning) {
               var1.drawRegion(
                  this.arrowImage,
                  15,
                  0,
                  15,
                  15,
                  0,
                  this.actors[this.commandSlot].homePos.x,
                  this.actors[this.commandSlot].homePos.y - this.actors[this.commandSlot].spriteH + this.cursorBob,
                  33
               );
               return;
            }
         } else {
            if (this.menuMode == 41) {
               byte[] var9 = this.aliveSlots(0, 10);
               this.drawTargetInfo(var1, var9[this.allyTargetIndex]);
               return;
            }

            if (this.menuMode == 2) {
               this.drawMenuPanel(var1);
               if (this.skillList != null) {
                  this.skillList.draw(var1);
                  String[] var15 = Utils.wrapText(Skill.descriptions[this.skillList.getRowId(this.skillList.getSelectedRow())], this.menuRect[2] - 10);
                  GameUtil.drawScrollingText(
                     var1,
                     var15,
                     this.menuRect[0] + 5,
                     this.menuRect[1] + this.menuRect[3] + this.listHeight,
                     this.menuRect[2] - 14,
                     10,
                     this.infoHeight / Utils.lineHeight,
                     13629944,
                     null,
                     false
                  );
               }

               return;
            }

            if (this.menuMode == 4) {
               this.drawMenuPanel(var1);
               if (this.itemList != null) {
                  this.itemList.draw(var1);
                  if (this.infoText != null) {
                     this.infoText.clear();
                  } else {
                     this.infoText = new TextBlock();
                  }

                  this.infoText.addText(GameData.itemDescriptions[this.itemList.getRowId(this.itemList.getSelectedRow())], this.menuWidth - 10, null, 13629944);
                  this.infoText
                     .setBounds(
                        this.menuRect[0] + 5,
                        this.menuRect[1] + this.menuRect[3] + this.listHeight,
                        this.menuWidth - 10,
                        this.infoHeight,
                        this.infoHeight / Utils.lineHeight
                     );
                  this.infoText.draw(var1, this.infoText, 1, true, false);
               }

               return;
            }

            if (this.menuMode == 3) {
               this.drawMenuPanel(var1);
               if (this.skillLevelList != null) {
                  this.skillLevelList.draw(var1);
                  if (this.infoText != null) {
                     this.infoText.clear();
                  } else {
                     this.infoText = new TextBlock();
                  }

                  var1.setColor(13629944);
                  vh.drawString(
                     var1,
                     "Cấp này cần MP : "
                        + Skill.mpCostsPerLevel[this.skillList.getRowId(this.skillList.getSelectedRow())][this.skillLevelList.getSelectedRow()],
                     DeviceConfig.screenSize[0] >> 1,
                     this.menuRect[1] + this.menuRect[3] + this.listHeight + (this.infoHeight - Utils.lineHeight >> 1),
                     17
                  );
                  return;
               }
            } else if (this.menuMode == 5 && this.petList != null) {
               this.drawMenuPanel(var1);
               this.petList.draw(var1);
               GameUtil.drawAnimation(
                  var1,
                  this.petPreview,
                  this.menuRect[0] + (this.menuRect[2] >> 2),
                  this.menuRect[1] + this.menuRect[3] + this.listHeight + this.infoHeight - 10,
                  true,
                  0
               );
               int var13 = this.menuRect[1] + this.menuRect[3] + this.listHeight;
               int var16 = this.infoHeight / 4;
               int var7 = this.menuRect[0] + this.menuRect[2] / 2;
               int var5 = this.menuRect[2] / 8;
               if (this.petRoster[this.petList.getSelectedRow()].elementValues != null && this.elementIcons != null) {
                  if (this.petRoster[this.petList.getSelectedRow()].elementValues[0] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var7,
                        var13 + var16 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        this.petRoster[this.petList.getSelectedRow()].elementValues[0],
                        var7 + this.elementIcons.getWidth() / 2,
                        var13 + var16 / 2,
                        this.digitsSmall,
                        6,
                        7,
                        6
                     );
                     var7 += var5;
                  }

                  if (this.petRoster[this.petList.getSelectedRow()].elementValues[1] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        this.elementIcons.getWidth() / 2,
                        0,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var7,
                        var13 + var16 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        this.petRoster[this.petList.getSelectedRow()].elementValues[1],
                        var7 + this.elementIcons.getWidth() / 2,
                        var13 + var16 / 2,
                        this.digitsSmall,
                        6,
                        7,
                        6
                     );
                     var7 += var5;
                  }

                  if (this.petRoster[this.petList.getSelectedRow()].elementValues[2] > 0) {
                     var1.drawRegion(
                        this.elementIcons, 0, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var7, var13 + var16 / 2, 6
                     );
                     Utils.drawNumber(
                        var1,
                        this.petRoster[this.petList.getSelectedRow()].elementValues[2],
                        var7 + this.elementIcons.getWidth() / 2,
                        var13 + var16 / 2,
                        this.digitsSmall,
                        6,
                        7,
                        6
                     );
                     var7 += var5;
                  }

                  if (this.petRoster[this.petList.getSelectedRow()].elementValues[3] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        0,
                        this.elementIcons.getHeight() / 2,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var7,
                        var13 + var16 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        this.petRoster[this.petList.getSelectedRow()].elementValues[3],
                        var7 + this.elementIcons.getWidth() / 2,
                        var13 + var16 / 2,
                        this.digitsSmall,
                        6,
                        7,
                        6
                     );
                  }
               }

               var7 = this.menuRect[0] + this.menuRect[2] / 2 - 4;
               var13 = var13 + var16 + 10;
               int[][] var17;
               (var17 = new int[3][2])[0][0] = this.petRoster[this.petList.getSelectedRow()].stats[1];
               var17[0][1] = this.petRoster[this.petList.getSelectedRow()].stats[2];
               var17[1][0] = this.petRoster[this.petList.getSelectedRow()].stats[4];
               var17[1][1] = this.petRoster[this.petList.getSelectedRow()].stats[5];
               var17[2][1] = this.petRoster[this.petList.getSelectedRow()].stats[13];
               var17[2][1] = this.petRoster[this.petList.getSelectedRow()].stats[14];
               this.drawPetBars(var1, var7, var13, var16, var17);
            }
         }
      }
   }

   private void drawPetBars(Graphics var1, int var2, int var3, int var4, int[][] var5) {
      for (int var6 = 0; var6 < 3; var6++) {
         int var10 = var3 + var4 * var6;
         Utils.drawFrame(
            var1, this.barImage, this.barImage.getWidth() * var5[var6][0] / var5[var6][1], this.barImage.getHeight() >> 2, var6, var2, var10, 20, 0
         );
         drawBarFrame(var1, var2, var10, this.barImage.getWidth(), 4664337, 15988459, this.barImage.getHeight() >> 2);
         int var10002 = var5[var6][0];
         int var10003 = var5[var6][1];
         int var10004 = var2 + this.statIcons.getWidth() * 5 / 2;
         int var17 = var10 - 4;
         int var16 = var10004;
         int var15 = var10003;
         int var14 = var10002;
         var1.drawRegion(this.digitsSmall, 60, 0, 6, 7, 0, var16, var17, 17);
         Utils.drawNumber(var1, var14, var16 - 6, var17, this.digitsSmall, 6, 7, 24);
         Utils.drawNumber(var1, var15, var16 + 6, var17, this.digitsSmall, 6, 7, 20);
         Utils.drawFrame(var1, this.statIcons, this.statIcons.getWidth(), this.statIcons.getHeight() / 10, var6, var2, var10 - 8, 20, 0);
      }
   }

   private void createMenuPanels(int var1, int var2) {
      this.listPanel = null;
      this.infoPanel = null;
      this.listHeight = 0;
      this.infoHeight = 0;
      if (var1 != 0) {
         this.listHeight = var1 - 2;
         this.listPanel = GameUtil.createTranslucentImage(193, var1, 2959909, 85);
      }

      if (var2 > 0) {
         this.infoHeight = var2 - 2;
         this.infoPanel = GameUtil.createTranslucentImage(193, var2, 657930, 85);
      }
   }

   private void drawMenuPanel(Graphics var1) {
      var1.drawImage(this.menuHeader, this.menuRect[0], this.menuRect[1], 20);
      if (this.menuTitle != null) {
         var1.drawImage(this.menuTitle, this.menuRect[0] + this.menuRect[2] / 2, this.menuRect[1] + this.menuHeader.getHeight() / 2, 3);
      }

      int[] var4 = this.menuRect;
      int var5 = var4[0] + 1;
      int var6 = var4[1] + var4[3];
      if (this.listPanel != null) {
         var1.drawImage(this.listPanel, var5, var4[1] + var4[3], 20);
         var6 += this.listHeight;
      }

      if (this.infoPanel != null) {
         var1.drawImage(this.infoPanel, var5, var6, 20);
         var6 += this.infoHeight;
      }

      var1.drawImage(this.footerPanel, var5, var6, 20);
      int[] var2 = this.menuRect;
      int var3 = this.menuRect[0] + 3;
      int var8 = var2[1] + var2[3];
      if (this.listPanel != null) {
         var8 += this.listHeight;
         var1.setColor(2303014);
         var1.fillRect(var2[0] + 2, var8 - 1, this.menuWidth - 2, 2);
         var1.setColor(14083300);
         var1.fillRect(var2[0] + 3, var8, this.menuWidth - 4, 1);
      }

      if (this.infoPanel != null) {
         var8 += this.infoHeight;
         var1.setColor(2303014);
         var1.fillRect(var2[0] + 2, var8 - 1, this.menuWidth - 2, 2);
         var1.setColor(14083300);
         var1.fillRect(var2[0] + 3, var8, this.menuWidth - 4, 1);
      }

      int var9 = var8 + this.footerHeight;
      var1.setColor(3027509);
      var1.drawRoundRect(var2[0] + 1, var2[1], this.menuWidth, var9 - var2[1], 4, 4);
      var1.setColor(12371653);
      var1.drawRoundRect(var2[0] + 2, var2[1], this.menuWidth - 2, var9 - var2[1] - 1, 4, 4);
      int var10 = var9;

      for (int var7 = 0; var7 < 2; var7++) {
         if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            Utils.drawFrame(
               var1,
               this.softKeyLabels,
               this.softKeyLabels.getWidth(),
               this.softKeyLabels.getHeight() / 3,
               var7,
               var3 + 6 + (this.softKeyLabels.getWidth() >> 1) + (this.menuWidth - this.softKeyLabels.getWidth() - 14) * var7,
               var10 - (this.footerHeight >> 1),
               3,
               0
            );
         } else {
            Utils.drawFrame(
               var1,
               this.softKeyLabels,
               this.softKeyLabels.getWidth(),
               this.softKeyLabels.getHeight() / 3,
               1 - var7,
               var3 + 6 + (this.softKeyLabels.getWidth() >> 1) + (this.menuWidth - this.softKeyLabels.getWidth() - 14) * var7,
               var10 - (this.footerHeight >> 1),
               3,
               0
            );
         }
      }
   }

   private void drawElements(Graphics var1, int var2, int var3, byte[] var4) {
      if (this.elementIcons != null) {
         if (var4[0] > 0) {
            var1.drawRegion(
               this.elementIcons,
               this.elementIcons.getWidth() / 2,
               this.elementIcons.getHeight() / 2,
               this.elementIcons.getWidth() / 2,
               this.elementIcons.getHeight() / 2,
               0,
               var2,
               var3,
               3
            );
            Utils.drawNumber(var1, var4[0], var2 + this.elementIcons.getWidth() / 4, var3 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
            var2 += 20;
         }

         if (var4[1] > 0) {
            var1.drawRegion(
               this.elementIcons, this.elementIcons.getWidth() / 2, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var2, var3, 3
            );
            Utils.drawNumber(var1, var4[1], var2 + this.elementIcons.getWidth() / 4, var3 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
            var2 += 20;
         }

         if (var4[2] > 0) {
            var1.drawRegion(this.elementIcons, 0, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var2, var3, 3);
            Utils.drawNumber(var1, var4[2], var2 + this.elementIcons.getWidth() / 4, var3 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
            var2 += 20;
         }

         if (var4[3] > 0) {
            var1.drawRegion(
               this.elementIcons, 0, this.elementIcons.getHeight() / 2, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var2, var3, 3
            );
            Utils.drawNumber(var1, var4[3], var2 + this.elementIcons.getWidth() / 4, var3 + this.elementIcons.getHeight() / 4, this.digitsBig, 6, 8, 3);
         }
      }
   }

   private void drawTargetInfo(Graphics var1, int var2) {
      drawTopBar(var1);
      if (this.actors[var2] != null) {
         int var3 = Utils.lineHeight - 2;
         if (this.elementIcons != null) {
            byte[] var4 = this.actors[var2].baseElements;
            this.drawElements(var1, 8, var3, var4);
         }

         var3 += 8;
         int var7 = elementAdvantage(this.actors[this.commandSlot].elements, this.actors[var2].elements);
         String var5 = "";
         if (var7 >= 10) {
            var5 = "[Khắc]";
         } else if (var7 <= -10) {
            var5 = "[Bị khắc]";
         }

         Utils.drawOutlinedText(
            var1, var5 + this.actors[var2].name + this.actors[var2].stats[0] + " cấp", DeviceConfig.screenSize[0] >> 1, var3, 15987162, 5971280, 33
         );
         var1.setColor(16773874);
         var1.drawRect(DeviceConfig.screenSize[0] - 40, 9, 37, 5);
         var1.setColor(15626599);
         var1.fillRect(DeviceConfig.screenSize[0] - 39, 10, this.actors[var2].stats[3] * 36 / this.actors[var2].stats[10], 4);
         Utils.drawNumber(
            var1, this.actors[var2].stats[3], DeviceConfig.screenSize[0] - 30, 11, this.digits, this.digits.getWidth() / 14, this.digits.getHeight(), 36
         );
         var1.setColor(16773874);
         var1.drawRect(DeviceConfig.screenSize[0] - 40, 21, 37, 5);
         var1.setColor(6793964);
         var1.fillRect(DeviceConfig.screenSize[0] - 39, 22, this.actors[var2].stats[5] * 36 / this.actors[var2].stats[11], 4);
         Utils.drawNumber(
            var1, this.actors[var2].stats[5], DeviceConfig.screenSize[0] - 30, 23, this.digits, this.digits.getWidth() / 14, this.digits.getHeight(), 36
         );
         var1.drawRegion(this.arrowImage, 15, 0, 15, 15, 0, this.actors[var2].pos.x, this.actors[var2].pos.y - 50 + this.cursorBob, 33);
      }
   }

   private static void drawTopBar(Graphics var0) {
      GameUtil.drawPanel(var0, 0, 0, DeviceConfig.screenSize[0], Utils.lineHeight + 10, 1317669, 15788720, 80, false);
   }

   public final boolean handleKey(int var1) {
      if (this.phase == 25) {
         if ((var1 == 8 || var1 == KeyMap.softLeftKey) && (var1 == 8 || var1 == KeyMap.softLeftKey)) {
            synchronized (GameCanvas.instance) {
               GameCanvas.instance.world.script.battleResult = 0;
               if (this.defeatLines != null && this.defeatLineIndex < this.defeatLines.length - 1) {
                  this.defeatLineIndex++;
               } else {
                  GameData.syncPetsFromBattle(this.party);
                  GameData.keepLeaderOnly(this.party);
                  if (!this.isStoryBattle) {
                     GameCanvas.instance.world.returnFromBattle();
                     GameData.applyDefeatPenalty();
                     if (GameData.townReturnPoint != null) {
                        World.entryPointIndex = GameData.townReturnPoint[1];
                        GameCanvas.instance.world.changeScene(GameData.townReturnPoint[0]);
                     } else {
                        World.entryPointIndex = (short)GameData.revivePoint;
                        GameCanvas.instance.world.changeScene(GameData.reviveSceneId);
                     }
                  } else {
                     GameCanvas.instance.world.returnFromBattle();
                     if (GameCanvas.instance.world.isDungeonBattle) {
                        GameCanvas.instance.world.isDungeonBattle = false;
                        GameCanvas.instance.world.changeScene(Utils.parseInt(GameData.dungeonLinkScenes[0]));
                     } else if (DeviceConfig.unloadBeforeBattle) {
                        GameCanvas.instance.world.beginLoading();
                     } else {
                        World.playSceneMusic();
                     }
                  }

                  GameCanvas.instance.world.script.advance(0);
               }
            }
         }
      } else if (this.phase == 5) {
         if (var1 == 8 || var1 == KeyMap.softLeftKey) {
            this.scoutedSlot = 0;
            this.scoutedSprite = null;
            this.phase = 3;
         }
      } else if (this.phase == 24) {
         if (this.resultPage == 0) {
            if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               GameCanvas.instance.world.script.battleResult = 2;
               if (this.rewardItemLines != null) {
                  if (this.rewardItemLines.length <= 7) {
                     if (this.rewardExpLines == null) {
                        returnToMap(this.party);
                     } else {
                        this.resultPage = 1;
                     }
                  } else if (++rewardScroll > this.rewardItemLines.length - 7) {
                     if (this.rewardExpLines == null) {
                        returnToMap(this.party);
                     } else {
                        this.resultPage = 1;
                     }
                  }
               } else if (this.rewardExpLines == null) {
                  returnToMap(this.party);
               } else {
                  this.resultPage = 1;
               }
            } else if (var1 == 1) {
               if (this.rewardItemLines != null && rewardScroll > 0) {
                  rewardScroll--;
               }
            } else if (var1 == 6 && this.rewardItemLines != null && this.rewardItemLines.length > 7 && rewardScroll < this.rewardItemLines.length - 7) {
               rewardScroll++;
            }
         } else if (this.resultPage == 2) {
            if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               if (this.levelUpIndex < this.levelUps.size() - 1) {
                  this.levelUpIndex++;
               } else {
                  this.levelUpIndex = 0;
                  this.levelUps.removeAllElements();
                  returnToMap(this.party);
               }
            }
         } else if (this.resultPage == 1 && (var1 == 8 || var1 == KeyMap.softLeftKey)) {
            if (this.levelUps.size() > 0) {
               this.resultPage = 2;
            } else {
               returnToMap(this.party);
            }
         }
      } else if (this.phase == 0) {
         Battle var9 = this;
         GameUtil.resetScroll();
         if (this.menuMode == 0) {
            Battle var10 = this;
            if (var1 == 5 && !this.isRingTurningLeft) {
               if (this.commandSlot == 0) {
                  this.commandIndex++;
                  this.commandIndex %= 7;
               } else if (this.commandIndex < 10) {
                  this.commandIndex++;
               } else {
                  this.commandIndex = 7;
               }

               this.isRingTurningRight = true;
            } else if (var1 == 2 && !this.isRingTurningRight) {
               if (this.commandSlot == 0) {
                  this.commandIndex--;
                  this.commandIndex = (this.commandIndex + 7) % 7;
               } else if (this.commandIndex > 7) {
                  this.commandIndex--;
               } else {
                  this.commandIndex = 10;
               }

               this.isRingTurningLeft = true;
            }

            if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == KeyMap.softRightKey) {
                  if (!this.petSummoned) {
                     if (this.commandSlot > 0) {
                        boolean var17 = false;

                        for (int var27 = this.commandSlot - 1; var27 >= 0; var27--) {
                           if (var10.actors[var27] != null && var10.actors[var27].stunTurns == 0 && var10.actors[var27].confuseTurns == 0) {
                              var17 = true;
                              var10.commandSlot = var27;
                              break;
                           }
                        }

                        if (var17 && var10.commandSlot == 0) {
                           var10.commandIndex = 0;
                        }
                     }
                  } else if (this.commandSlot > 0) {
                     boolean var18 = false;

                     for (int var28 = this.commandSlot - 1; var28 > 0; var28--) {
                        if (var10.actors[var28] != null && var10.actors[var28].stunTurns == 0 && var10.actors[var28].confuseTurns == 0) {
                           var18 = true;
                           var10.commandSlot = var28;
                           break;
                        }
                     }

                     if (var18 && var10.commandSlot == 0) {
                        var10.commandIndex = 0;
                     }
                  }
               }
            } else {
               if (GameCanvas.instance.world.isTutorialBattle && this.commandSlot == 0) {
                  if (this.tutorialStep == 1) {
                     if (this.commandIndex != 2) {
                        GameCanvas.instance.showToast("Hãy chọn kỹ năng trinh sát!", 8);
                        return false;
                     }
                  } else if (this.tutorialStep == 2) {
                     if (this.commandIndex != 0) {
                        GameCanvas.instance.showToast("Hãy chọn đòn đánh trước!", 8);
                        return false;
                     }
                  } else if (this.tutorialStep == 3 && this.commandIndex != 4) {
                     GameCanvas.instance.showToast("Hãy chọn sủng vật trước‚chọn một sủng vật để tham chiến!", 8);
                     return false;
                  }
               }

               if (this.actors[this.commandSlot].silenceTurns > 0 && this.commandIndex == 2) {
                  GameCanvas.instance.showToast("Trong Trung Thiên Địa Mạc Ngôn cấm dùng kỹ năng", 10);
               } else if (this.commandIndex == 0 || this.commandIndex == 7) {
                  this.menuMode = 1;
                  this.actionPlans[this.commandSlot][1] = 1;
               } else if (this.commandIndex == 1 || this.commandIndex == 8) {
                  this.actionPlans[this.commandSlot][1] = 2;
                  this.confirmCommand();
               } else if (this.commandIndex != 2 && this.commandIndex != 9) {
                  if (this.commandIndex == 3) {
                     if (this.usableItems != null) {
                        this.actionPlans[this.commandSlot][1] = 6;
                        this.loadMenuImages();
                        this.createMenuPanels(142, this.menuHeight - 142);
                        this.menuMode = 4;
                        this.itemList = new MenuList(this.menuRect[0], this.menuRect[1] + this.menuRect[3], this.menuRect[2], this.listHeight);
                        GameData.loadItemTable();
                        if (GameData.itemIcons == null) {
                           GameData.loadItemIcons();
                        }

                        int[][] var12 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
                        TextListPanel var23 = new TextListPanel((byte)1, var12);
                        IconListPanel var36 = new IconListPanel(true);
                        ValueListPanel var39 = new ValueListPanel();
                        this.itemList.rowIds = new int[this.usableItems.length];

                        for (int var13 = 0; var13 < var10.usableItems.length; var13++) {
                           int var42 = GameData.findItemIndex(var10.usableItems[var13][0]);
                           var10.itemList.rowIds[var13] = var42;
                           var36.addIcon(GameData.itemIcons[var42]);
                           var23.addText(GameData.itemNames[var42]);
                           var39.addText(Integer.toString(var10.usableItems[var13][1]));
                        }

                        var36.setRowHeight(Utils.lineHeight + 10);
                        var23.setRowHeight(Utils.lineHeight + 10);
                        var39.setRowHeight(Utils.lineHeight + 10);
                        var36.setBounds(var10.menuRect[0] + 10, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 4, var10.listHeight);
                        var23.setBounds(
                           var10.menuRect[0] + var10.menuRect[2] / 4, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 2, var10.listHeight
                        );
                        var39.setBounds(
                           var10.menuRect[0] + var10.menuRect[2] * 3 / 4, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 4, var10.listHeight
                        );
                        boolean var43 = true;
                        var39.showTimesPrefix = true;
                        var10.itemList.addColumn(var36);
                        var10.itemList.addColumn(var23);
                        var10.itemList.addColumn(var39);
                        var10.itemList.setCursor(var10.arrowImage, var10.menuRect[0] + 3);
                     } else {
                        GameCanvas.instance.showToast("Không có vật phẩm dùng được", 10);
                     }
                  } else if (this.commandIndex == 4) {
                     this.loadMenuImages();
                     this.createMenuPanels(88, this.menuHeight - 88);
                     if (this.petRoster == null) {
                        GameCanvas.instance.showToast("Không có sủng vật để chọn", 10);
                     } else {
                        this.petList = new MenuList(this.menuRect[0], this.menuRect[1] + this.menuRect[3], this.menuRect[2], this.listHeight);
                        int[][] var14 = new int[][]{{9764604, 343466}, {15794175, 15794175}};
                        int[][] var24 = new int[][]{{15921628, 5971029}, {15921628, 5971029}};
                        int[][] var37 = new int[][]{{16706636, 9193495}, {16706636, 9193495}};
                        TextListPanel var40 = new TextListPanel((byte)1, var14);
                        TextListPanel var15 = new TextListPanel((byte)1, var24);
                        TextListPanel var44 = new TextListPanel((byte)1, var37);
                        this.petList.rowIds = new int[this.petRoster.length];

                        for (int var25 = 0; var25 < var10.petRoster.length; var25++) {
                           var10.petList.rowIds[var25] = var10.petRoster[var25].petId;
                           var40.addText(var10.petRoster[var25].name);
                           var15.addText(String.valueOf(var10.petRoster[var25].stats[0]));
                           var44.addText(" cấp");
                        }

                        var40.setRowHeight(Utils.lineHeight + 10);
                        var15.setRowHeight(Utils.lineHeight + 10);
                        var44.setRowHeight(Utils.lineHeight + 10);
                        var40.setBounds(var10.menuRect[0] + 25, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 4, var10.listHeight);
                        var15.setBounds(
                           var10.menuRect[0] + 15 + var10.menuRect[2] / 4, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 2, var10.listHeight
                        );
                        var44.setBounds(
                           var10.menuRect[0] - 10 + var10.menuRect[2] * 3 / 4, var10.menuRect[1] + var10.menuRect[3], var10.menuRect[2] / 4, var10.listHeight
                        );
                        var10.petList.addColumn(var40);
                        var10.petList.addColumn(var15);
                        var10.petList.addColumn(var44);
                        var10.petList.setCursor(var10.arrowImage, var10.menuRect[0] + 3);
                        var10.petPreview = null;
                        var10.petPreview = GameUtil.loadBattleAnimation(var10.petRoster[var10.petList.getSelectedRow()].petId, true);
                        if (var10.petList != null) {
                           var10.menuMode = 5;
                        }
                     }
                  } else if (this.commandIndex == 5) {
                     if (GameCanvas.instance.world.isTutorialBattle || GameCanvas.instance.world.script.isMustLoseBattle) {
                        GameCanvas.instance.showToast("Không dùng được trong trận này", 8);
                     } else if (ChannelConfig.isBbx) {
                        PaidServiceMenu.open("/bin/smsmenu.bin");
                        PaidServiceMenu.itemFeatureIds[GameCanvas.instance.world.smsMenu.itemIndex] = 8;
                        this.smsContext = 3;
                        PaidServiceMenu.page = 2;
                     } else {
                        PaidServiceMenu.open("/bin/smsmenu.bin");
                        GameCanvas.instance.world.smsMenu.purchase(8);
                        this.smsContext = 3;
                     }
                  } else if (this.commandIndex == 6) {
                     boolean var16 = false;
                     if (!this.isStoryBattle) {
                        Random var26;
                        (var26 = new Random()).setSeed(System.currentTimeMillis());
                        if (Math.abs(var26.nextInt()) / 21474836 <= 30) {
                           var16 = true;
                        }
                     } else {
                        if (DeviceConfig.debugMode) {
                           System.out.println("Không thể bỏ chạy");
                        }

                        var16 = false;
                        GameCanvas.instance.showToast("Không thể bỏ chạy", 10);
                     }

                     if (var16) {
                        this.saveStatsToParty();
                        GameData.syncPetsFromBattle(GameCanvas.instance.world.battle.party);
                        GameData.keepLeaderOnly(GameCanvas.instance.world.battle.party);
                        GameCanvas.instance.world.script.battleResult = 1;
                        GameCanvas.instance.world.returnFromBattle();
                        if (DeviceConfig.unloadBeforeBattle) {
                           GameCanvas.instance.world.beginLoading();
                        } else {
                           World.playSceneMusic();
                        }
                     } else {
                        GameCanvas.instance.showToast("Bỏ chạy thất bại", 10);
                        this.phase = 2;
                        this.commandSlot = 0;
                        this.commandIndex = 0;
                     }
                  } else if (this.commandIndex == 10) {
                     this.menuMode = 66;
                     this.isSummoning = true;
                     if (this.summonEffect != null) {
                        this.summonEffect.setFrame(0);
                     }
                  }
               } else if (this.actors[this.commandSlot].activeSkillIds == null) {
                  GameCanvas.instance.showToast("Không có kỹ năng khả dụng", 10);
               } else {
                  Battle var11 = this;
                  this.loadMenuImages();
                  this.createMenuPanels(142, this.menuHeight - 142);
                  this.menuMode = 2;
                  if (Skill.icons == null) {
                     Skill.loadIcons();
                  }

                  this.skillList = new MenuList(this.menuRect[0], this.menuRect[1] + this.menuRect[3], this.menuRect[2], this.listHeight);
                  int[][] var6 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
                  TextListPanel var3 = new TextListPanel((byte)1, var6);
                  IconListPanel var4 = new IconListPanel(true);
                  this.skillList.rowIds = new int[this.actors[this.commandSlot].activeSkillIds.length];
                  if (this.actors[this.commandSlot].activeSkillIds != null) {
                     for (int var5 = 0; var5 < var11.actors[var11.commandSlot].activeSkillIds.length; var5++) {
                        int var41 = GameData.findSkillIndex(var11.actors[var11.commandSlot].activeSkillIds[var5]);
                        var11.skillList.rowIds[var5] = var41;
                        var4.addIcon(Skill.icons[var41]);
                        var3.addText(Skill.names[var41]);
                     }
                  }

                  var4.setRowHeight(Utils.lineHeight + 10);
                  var3.setRowHeight(Utils.lineHeight + 10);
                  var4.setBounds(var11.menuRect[0] + 10, var11.menuRect[1] + var11.menuRect[3], var11.menuRect[2] / 4, var11.listHeight);
                  var3.setBounds(var11.menuRect[0] + var11.menuRect[2] / 4, var11.menuRect[1] + var11.menuRect[3], var11.menuRect[2] / 2, var11.listHeight);
                  var11.skillList.addColumn(var4);
                  var11.skillList.addColumn(var3);
                  var11.skillList.setCursor(var11.arrowImage, var11.menuRect[0] + 3);
                  this.actionPlans[this.commandSlot][1] = 4;
               }
            }
         } else if (this.menuMode == 1) {
            if (var1 == 1) {
               this.targetCursor = this.prevAliveEnemy(this.targetCursor - 1, 9);
            } else if (var1 == 6) {
               byte[] var29 = this.aliveSlots(10, 20);
               if (this.targetCursor < var29[var29.length - 1]) {
                  this.targetCursor = this.firstAliveSlot(this.targetCursor + 1, 20);
               } else {
                  this.targetCursor = this.firstAliveSlot(10, 20);
               }
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               this.confirmCommand();
            } else if (var1 == KeyMap.softRightKey) {
               this.menuMode = 0;
            }
         } else if (this.menuMode == 21) {
            int var30 = GameData.findSkillIndex(this.actionPlans[this.commandSlot][3]);
            if (var1 == 1) {
               if (Skill.targetSides[var30] == 1) {
                  if (this.targetCursor > 0) {
                     this.targetCursor--;
                  } else {
                     byte[] var19 = this.aliveSlots(0, 10);
                     this.targetCursor = var19[var19.length - 1];
                  }
               } else if (Skill.targetSides[var30] == 2) {
                  this.targetCursor = this.prevAliveEnemy(this.targetCursor - 1, 9);
               }
            } else if (var1 == 6) {
               if (Skill.targetSides[var30] == 1) {
                  int var20 = this.countAlive(0, 10);
                  if (this.targetCursor < var20 - 1) {
                     this.targetCursor = this.firstAliveSlot(this.targetCursor + 1, 10);
                  } else {
                     this.targetCursor = this.firstAliveSlot(0, 10);
                  }
               } else if (Skill.targetSides[var30] == 2) {
                  byte[] var21 = this.aliveSlots(10, 20);
                  if (this.targetCursor < var21[var21.length - 1]) {
                     this.targetCursor = this.firstAliveSlot(this.targetCursor + 1, 20);
                  } else {
                     this.targetCursor = this.firstAliveSlot(10, 20);
                  }
               }
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               this.confirmCommand();
            } else if (var1 == KeyMap.softRightKey) {
               this.targetCursor = this.firstAliveSlot(10, 20);
               this.menuMode = 0;
            }
         } else if (this.menuMode == 41) {
            if (var1 == 1) {
               if (this.allyTargetIndex > 0) {
                  this.allyTargetIndex--;
               } else {
                  byte[] var31 = this.aliveSlots(0, 10);
                  this.allyTargetIndex = var31[var31.length - 1];
               }
            } else if (var1 == 6) {
               if (this.allyTargetIndex < this.countAlive(0, 10) - 1) {
                  this.allyTargetIndex++;
               } else {
                  this.allyTargetIndex = 0;
               }
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               byte[] var32 = this.aliveSlots(0, 10);
               this.targetCursor = var32[this.allyTargetIndex];
               this.confirmCommand();
            } else if (var1 == KeyMap.softRightKey) {
               this.menuMode = 0;
            }
         } else if (this.menuMode == 2) {
            if (var1 == 1) {
               this.skillList.selectPrev();
            } else if (var1 == 6) {
               this.skillList.selectNext();
            } else if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == KeyMap.softRightKey) {
                  this.targetCursor = this.firstAliveSlot(10, 20);
                  this.menuMode = 0;
               }
            } else if (GameCanvas.instance.world.isTutorialBattle
               && this.tutorialStep == 1
               && this.actors[this.commandSlot].skills[this.skillList.getSelectedRow()].skillId != 39) {
               GameCanvas.instance.showToast("Hãy chọn kỹ năng trinh sát!", 8);
            } else {
               this.skillLevelList = new MenuList(this.menuRect[0], this.menuRect[1] + this.menuRect[3], this.menuRect[2], this.listHeight);
               int[][] var33 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
               TextListPanel var22 = new TextListPanel((byte)1, var33);

               for (int var34 = 0; var34 < var9.actors[var9.commandSlot].skills[var9.skillList.getSelectedRow()].level; var34++) {
                  int var38 = GameData.findSkillIndex(var9.actors[var9.commandSlot].skills[var9.skillList.getSelectedRow()].skillId);
                  var22.addText(Skill.names[var38] + "    " + (var34 + 1) + "   cấp");
               }

               var22.setRowHeight(Utils.lineHeight + 10);
               var22.setBounds(var9.menuRect[0], var9.menuRect[1] + var9.menuRect[3], var9.menuRect[2], var9.listHeight);
               var9.skillLevelList.addColumn(var22);
               var9.skillLevelList.setCursor(var9.arrowImage, var9.menuRect[0] + 15);
               var9.menuMode = 3;
               var9.actionPlans[var9.commandSlot][3] = var9.actors[var9.commandSlot].skills[var9.skillList.getSelectedRow()].skillId;
            }
         } else if (this.menuMode == 4) {
            if (var1 == 1) {
               this.itemList.selectPrev();
            } else if (var1 == 6) {
               this.itemList.selectNext();
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               if (this.usableItems[this.itemList.getSelectedRow()][1] > 0) {
                  if (GameCanvas.instance.world.isTutorialBattle
                     && this.commandSlot == 0
                     && this.tutorialStep == 3
                     && this.usableItems[this.itemList.getSelectedRow()][0] != 29) {
                     GameCanvas.instance.showToast("Hãy chọn thẻ phong ấn", 8);
                     return false;
                  }

                  if ((
                        GameCanvas.instance.world.script.isMustLoseBattle
                           || GameCanvas.instance.world.isDungeonBattle
                           || GameCanvas.instance.world.isNormalBattle
                     )
                     && Utils.contains(new short[]{29, 30, 31}, this.usableItems[this.itemList.getSelectedRow()][0])) {
                     GameCanvas.instance.showToast("Không dùng được trong trận này", 8);
                     return false;
                  }
               }

               this.allyTargetIndex = 0;
               this.menuMode = 41;
            } else if (var1 == KeyMap.softRightKey) {
               this.menuMode = 0;
            }
         } else if (this.menuMode == 5) {
            if (var1 == 1) {
               this.petList.selectPrev();
               this.petPreview = null;
               this.petPreview = GameUtil.loadBattleAnimation(this.petRoster[this.petList.getSelectedRow()].petId, true);
            } else if (var1 == 6) {
               this.petList.selectNext();
               this.petPreview = null;
               this.petPreview = GameUtil.loadBattleAnimation(this.petRoster[this.petList.getSelectedRow()].petId, true);
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               this.summonSlot = 0;
               this.chosenPetIndex = this.petList.getSelectedRow();
               this.menuMode = 51;
            } else if (var1 == KeyMap.softRightKey) {
               this.menuMode = 0;
            }
         } else if (this.menuMode == 51) {
            if (var1 == 1 || var1 == 6) {
               if (this.countAlive(0, 10) > 1) {
                  if (this.summonSlot == 2) {
                     this.summonSlot = 0;
                  } else if (this.summonSlot == 0) {
                     this.summonSlot = 2;
                  }
               }
            } else if (var1 == KeyMap.softRightKey) {
               this.menuMode = 5;
            } else if (var1 == 8 || var1 == KeyMap.softLeftKey) {
               this.isSummoning = true;
               if (this.summonEffect != null) {
                  this.summonEffect.setFrame(0);
               }
            }
         } else if (this.menuMode == 3) {
            if (var1 == 1) {
               this.skillLevelList.selectPrev();
            } else if (var1 == 6) {
               this.skillLevelList.selectNext();
            } else if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == KeyMap.softRightKey) {
                  this.targetCursor = this.firstAliveSlot(10, 20);
                  this.menuMode = 2;
               }
            } else {
               if (Skill.names == null) {
                  Skill.loadTable();
               }

               int var35 = GameData.findSkillIndex(this.actionPlans[this.commandSlot][3]);
               if (Skill.mpCostsPerLevel[var35][this.skillLevelList.getSelectedRow()] > this.actors[this.commandSlot].stats[5]) {
                  GameCanvas.instance.showToast("MP quá thấp, không dùng được kỹ năng", 8);
               } else {
                  this.actionPlans[this.commandSlot][4] = this.skillLevelList.getSelectedRow() + 1;
                  if (Skill.targetScopes[var35] == 1) {
                     if (Skill.targetSides[var35] != 0) {
                        if (Skill.targetSides[var35] == 1) {
                           this.menuMode = 21;
                           this.targetCursor = 0;
                        } else if (Skill.targetSides[var35] == 2) {
                           this.targetCursor = this.firstAliveSlot(10, 20);
                           this.menuMode = 21;
                           return false;
                        }

                        return false;
                     }

                     this.targetCursor = this.actionPlans[this.commandSlot][0];
                  }

                  this.confirmCommand();
               }
            }
         }
      } else if (this.phase == 4) {
         if (this.dialog != null) {
            if (this.dialog.isTextComplete()) {
               if (this.dialog.lineIndex >= this.dialog.lines.length - 1) {
                  if (this.dialogMode == 1) {
                     this.phase = 0;
                     this.menuMode = 0;
                     this.dialogLines = null;
                  } else if (this.dialogMode != 2 && this.dialogMode != 3 && this.dialogMode == 4) {
                     return true;
                  }
               } else {
                  this.dialog.nextLine();
               }
            } else {
               this.dialog.scrollDown();
            }
         }
      } else if ((var1 == 8 || var1 == KeyMap.softLeftKey) && this.phase == 1) {
         this.phase = 2;
      }

      return false;
   }

   private boolean allInComboWait(byte[] var1) {
      if (var1 != null) {
         for (int var2 = 0; var2 < var1.length; var2++) {
            if (this.actors[var1[var2]] != null && this.actors[var1[var2]].state != 16) {
               return false;
            }
         }
      }

      return true;
   }

   public final void handlePointer(int var1, int var2) {
      int var3 = this.menuRect[0];
      int var4 = this.menuRect[1] + this.menuRect[3] + this.menuHeight;
      int var5 = this.menuRect[2];
      if (this.phase == 0) {
         if (this.menuMode != 2 && this.menuMode != 3 && this.menuMode != 4 && this.menuMode != 5) {
            if ((this.menuMode == 1 || this.menuMode == 21 || this.menuMode == 41 || this.menuMode == 51)
               && MenuList.isInButton(var1, var2, DeviceConfig.screenSize[0] - 30, DeviceConfig.screenSize[1] - 30, 30, 30)
               && (
                  DeviceConfig.modelName.equals("E6")
                     || DeviceConfig.modelName.equals("E680")
                     || DeviceConfig.modelName.equals("W958C")
                     || DeviceConfig.modelName.equals("S1")
                     || DeviceConfig.modelName.equals("N95")
                     || DeviceConfig.modelName.equals("N5800")
               )) {
               this.handleKey(KeyMap.softRightKey);
            }
         } else if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            if (MenuList.isInButton(var1, var2, var3, var4, 30, 30)) {
               this.handleKey(KeyMap.softLeftKey);
               return;
            }

            if (MenuList.isInButton(var1, var2, var3 + var5 - 30, var4, 30, 30)) {
               this.handleKey(KeyMap.softRightKey);
               return;
            }
         } else {
            if (MenuList.isInButton(var1, var2, var3, var4, 30, 30)) {
               this.handleKey(KeyMap.softRightKey);
               return;
            }

            if (MenuList.isInButton(var1, var2, var3 + var5 - 30, var4, 30, 30)) {
               this.handleKey(KeyMap.softLeftKey);
               return;
            }
         }
      }
   }

   private void resetActorPoses() {
      for (int var1 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null) {
            this.actors[var1].inCombo = false;
            this.actors[var1].sprite.setAction(0);
            this.actors[var1].setPos(this.actors[var1].homePos);
         }
      }
   }

   private void summonPet() {
      this.saveStatsToParty();
      if (this.summonSlot == 0) {
         if (this.party.length >= 2) {
            if (this.petRoster[this.chosenPetIndex].stats[1] > 0) {
               boolean var1 = false;
               if (this.party[1] != null) {
                  for (int var2 = 0; var2 < GameData.myPets.length; var2++) {
                     if (GameData.myPets[var2].uid == this.party[1].petUid) {
                        GameData.myPets[var2].isDeployed = false;
                        if (this.party[1].stats[3] <= 0) {
                           GameData.myPets[var2].isFainted = true;
                        }

                        var1 = true;
                        break;
                     }
                  }
               } else {
                  var1 = true;
               }

               if (var1) {
                  GameData.syncPetsFromBattle(this.party);
                  this.petRoster[this.chosenPetIndex].isDeployed = true;
                  this.party[1] = GameData.createPetSprite(this.petRoster[this.chosenPetIndex]);
                  this.createPetActor(1);
                  this.buildTurnOrder();
                  this.petRoster = null;
                  this.petRoster = GameData.getReservePets();
                  this.petSummoned = true;
               } else {
                  System.out.println("không tìm thấy sủng vật của đội trong mảng sủng vật");
               }
            }
         } else {
            new RoleSprite();
            RoleSprite var3 = GameData.createPetSprite(this.petRoster[this.chosenPetIndex]);
            if (this.party == null) {
               this.party = new RoleSprite[1];
            } else {
               RoleSprite[] var6 = new RoleSprite[this.party.length + 1];
               System.arraycopy(this.party, 0, var6, 0, this.party.length);
               this.party = var6;
            }

            this.party[this.party.length - 1] = var3;
            this.createPetActor(1);
            this.buildTurnOrder();
            this.petRoster[this.chosenPetIndex].isDeployed = true;
            this.petSummoned = true;
            this.petRoster = null;
            this.petRoster = GameData.getReservePets();
         }
      } else if (this.summonSlot == 2) {
         if (this.party.length >= 3) {
            if (this.petRoster[this.chosenPetIndex].stats[1] > 0) {
               boolean var4 = false;
               if (this.party[2] != null) {
                  for (int var7 = 0; var7 < GameData.myPets.length; var7++) {
                     if (GameData.myPets[var7].uid == this.party[2].petUid) {
                        GameData.myPets[var7].isDeployed = false;
                        if (this.party[2].stats[3] <= 0) {
                           GameData.myPets[var7].isFainted = true;
                        }

                        var4 = true;
                        break;
                     }
                  }
               } else {
                  var4 = true;
               }

               if (var4) {
                  GameData.syncPetsFromBattle(this.party);
                  this.petRoster[this.chosenPetIndex].isDeployed = true;
                  this.party[2] = GameData.createPetSprite(this.petRoster[this.chosenPetIndex]);
                  this.createPetActor(2);
                  this.buildTurnOrder();
                  this.petRoster = null;
                  this.petRoster = GameData.getReservePets();
                  this.petSummoned = true;
               }
            }
         } else {
            new RoleSprite();
            RoleSprite var5 = GameData.createPetSprite(this.petRoster[this.chosenPetIndex]);
            if (this.party == null) {
               this.party = new RoleSprite[1];
            } else {
               RoleSprite[] var8 = new RoleSprite[this.party.length + 1];
               System.arraycopy(this.party, 0, var8, 0, this.party.length);
               this.party = var8;
            }

            this.party[this.party.length - 1] = var5;
            this.createPetActor(2);
            this.buildTurnOrder();
            this.petRoster[this.chosenPetIndex].isDeployed = true;
            this.petSummoned = true;
            this.petRoster = null;
            this.petRoster = GameData.getReservePets();
         }
      }

      this.chosenPetIndex = 0;
      this.confirmCommand();
   }

   private void createPetActor(int var1) {
      this.actors[var1] = MultiTapInput.newBattleActor();
      this.actors[var1].name = this.party[var1].name;
      this.actors[var1].slot = var1;
      this.actors[var1].roleId = this.party[var1].roleId;
      this.actors[var1].sprite = new AnimSprite();
      this.actors[var1].sprite = GameUtil.loadBattleAnimation(this.party[var1].roleId, true);
      if (this.actors[var1].sprite.getActionFrameCount(2) > 1) {
         BattleActor var10000 = this.actors[var1];
         byte var3 = 0;
         var10000.moveType = var3;
      } else {
         BattleActor var17 = this.actors[var1];
         byte var11 = 1;
         var17.moveType = var11;
      }

      this.actors[var1].spriteW = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[2];
      this.actors[var1].spriteH = this.actors[var1].sprite.getBoxesAt(0, 0, 0)[3];
      String var2;
      String var12 = Utils.getValue(var2 = Utils.substringBetween(this.teamData, "attackDefault:", "attackDefaultEnd"), "actEffect");
      var2 = Utils.getValue(var2, "frame");
      this.actors[var1].hitFrames = Utils.parseByteArray(var2, ",");
      this.actors[var1].hitEffect = new AnimSprite();
      this.actors[var1].hitEffect.loadInGroup(var12, 1);
      this.actors[var1].defendEffect = new AnimSprite();
      this.actors[var1].defendEffect.loadInGroup("/ft/defense.av", 1);
      this.actors[var1].poisonEffect = new AnimSprite();
      this.actors[var1].poisonEffect.loadInGroup("/ft/zhongdu.av", 1);
      this.actors[var1].confuseEffect = new AnimSprite();
      this.actors[var1].confuseEffect.loadInGroup("/ft/zhunluan.av", 1);
      this.actors[var1].stunEffect = new AnimSprite();
      this.actors[var1].stunEffect.loadInGroup("/ft/zhongyun.av", 1);

      for (int var7 = 0; var7 < 6; var7++) {
         if (this.party[var1].equips != null && this.party[var1].equips[var7] != null) {
            this.actors[var1].critRate = (short)(
               this.actors[var1].critRate + (short)(this.party[var1].equips[var7].crit + this.party[var1].equips[var7].gemCrit)
            );
         }
      }

      if (var1 == 1) {
         this.actors[var1].homePos.x = this.allyX[0];
         this.actors[var1].homePos.y = this.allyY[0];
      } else {
         this.actors[var1].homePos.x = this.allyX[var1];
         this.actors[var1].homePos.y = this.allyY[var1];
      }

      this.actors[var1].pos.x = this.actors[var1].homePos.x;
      this.actors[var1].pos.y = this.actors[var1].homePos.y;
      this.actors[var1].isPresent = true;
      Vector var8 = new Vector();
      Vector var13 = new Vector();
      this.actors[var1].skills = null;
      this.actors[var1].activeSkillIds = null;

      for (int var4 = 0; this.party[var1].skillIds != null && var4 < this.party[var1].skillIds.length; var4++) {
         if (!Utils.contains(this.passiveSkillIds, this.party[var1].skillIds[var4])) {
            var8.addElement(new Short(this.party[var1].skillIds[var4]));
            var13.addElement(this.party[var1].skills[var4]);
         }

         if (this.party[var1].skillIds[var4] == 7) {
            this.actors[var1].hasCounter = true;
         }
      }

      if (var8.size() > 0) {
         this.actors[var1].activeSkillIds = new short[var8.size()];
         this.actors[var1].skills = new Skill[var13.size()];
         Short[] var14 = new Short[var8.size()];
         var8.copyInto(var14);

         for (int var9 = 0; var9 < this.actors[var1].activeSkillIds.length; var9++) {
            this.actors[var1].activeSkillIds[var9] = var14[var9];
            this.actors[var1].skills[var9] = (Skill)var13.elementAt(var9);
         }
      }

      this.actors[var1].elements = new byte[4];
      this.actors[var1].baseElements = new byte[4];

      for (int var15 = 0; var15 < this.party[var1].elementValues.length; var15++) {
         this.actors[var1].elements[var15] = this.party[var1].elementValues[var15];
         this.actors[var1].baseElements[var15] = this.party[var1].elementValues[var15];
      }

      byte var16 = 0;
      if (this.actors[0].activeSkillIds != null && Utils.contains(this.actors[0].activeSkillIds, 46)) {
         var16 = 5;
      }

      for (int var10 = 0; var10 < this.party[var1].stats.length; var10++) {
         this.actors[var1].stats[var10] = this.party[var1].stats[var10];
      }

      this.actors[var1].attack = this.party[var1].stats[12] * (var16 + 100) / 100;
      this.actors[var1].defense = this.party[var1].stats[13] * (var16 + 100) / 100;
      this.actors[var1].speed = this.party[var1].stats[14] * (var16 + 100) / 100;
      if (GameData.statMultiplier > 0) {
         this.actors[var1].attack = this.actors[var1].attack * GameData.statMultiplier;
         this.actors[var1].defense = this.actors[var1].defense * GameData.statMultiplier;
         this.actors[var1].speed = this.actors[var1].speed * GameData.statMultiplier;
      }

      this.actors[var1].attack = this.actors[var1].attack + elementPassiveBonus(this.actors[var1]);
      this.actors[var1].hp = this.actors[var1].stats[3] * (var16 + 100) / 100;
      this.actors[var1].mp = this.actors[var1].stats[5] * (var16 + 100) / 100;
      if (var1 != 0) {
         BattleActor var18 = this.actors[0];
         BattleActor var5 = this.actors[var1];
      }
   }

   private void loadMenuImages() {
      if (this.footerPanel == null) {
         this.footerPanel = GameUtil.createTranslucentImage(193, 30, 2959909, 85);
      }

      this.footerHeight = 28;
      if (this.menuHeader == null) {
         this.menuHeader = ResourceCache.loadImage("/pic/D03.gf");
      }

      if (this.menuTitle == null) {
         this.menuTitle = ResourceCache.loadImage("/pic/D46.gf");
      }

      if (this.softKeyLabels == null) {
         this.softKeyLabels = ResourceCache.loadImage("/pic/D06.gf");
      }
   }

   private int firstAliveSlot(int var1, int var2) {
      int var3 = -1;

      for (int var4 = var1; var4 < var2; var4++) {
         if (this.actors[var4] != null && !this.actors[var4].isDead) {
            var3 = var4;
            break;
         }
      }

      return var3 == -1 ? var1 - 1 : var3;
   }

   private int prevAliveEnemy(int var1, int var2) {
      var2 = -1;

      for (int var3 = var1; var3 > 9; var3--) {
         if (this.actors[var3] != null && !this.actors[var3].isDead) {
            var2 = var3;
            break;
         }
      }

      return var2 == -1 ? var1 + 1 : var2;
   }

   private void confirmCommand() {
      label91: {
         if (this.commandSlot == 0) {
            this.actionPlans[this.commandSlot][2] = this.targetCursor;
            if (this.countAlive(0, 10) != 1) {
               if (this.countAlive(0, 10) > 1) {
                  byte[] var1;
                  if ((var1 = Utils.removeValue(this.aliveSlots(0, 10), 0)).length == 1) {
                     if (this.actors[var1[0]] != null && this.actors[var1[0]].stunTurns > 0) {
                        this.phase = 2;
                        this.commandSlot = 0;
                        this.commandIndex = 0;
                     } else if (this.actors[var1[0]] != null && this.actors[var1[0]].confuseTurns > 0) {
                        byte[] var2 = this.aliveSlots(0, this.actors.length);
                        this.actionPlans[var1[0]][1] = 1;
                        byte[] var3 = Utils.removeValue(var2, var1[0]);
                        int var4 = GameUtil.randomRange(0, var3.length);
                        this.actionPlans[var1[0]][2] = var3[var4];
                        this.phase = 2;
                        this.commandSlot = 0;
                        this.commandIndex = 0;
                     } else {
                        this.commandSlot = var1[0];
                        this.commandIndex = 7;
                        this.targetCursor = this.firstAliveSlot(10, 20);
                     }
                  } else if (var1.length == 2) {
                     if (this.actors[var1[0]] != null && this.actors[var1[0]].stunTurns > 0) {
                        if (this.actors[var1[1]] != null && this.actors[var1[1]].stunTurns > 0) {
                           this.phase = 2;
                           this.commandSlot = 0;
                           this.commandIndex = 0;
                        } else if (this.actors[var1[1]] != null && this.actors[var1[1]].confuseTurns > 0) {
                           byte[] var7 = this.aliveSlots(0, this.actors.length);
                           this.actionPlans[var1[1]][1] = 1;
                           byte[] var11 = Utils.removeValue(var7, var1[1]);
                           int var15 = GameUtil.randomRange(0, var11.length);
                           this.actionPlans[var1[1]][2] = var11[var15];
                           this.commandSlot = 0;
                           this.commandIndex = 0;
                           this.phase = 2;
                        } else {
                           this.commandSlot = var1[1];
                           this.commandIndex = 7;
                           this.targetCursor = this.firstAliveSlot(10, 20);
                        }
                     } else if (this.actors[var1[0]] != null && this.actors[var1[0]].confuseTurns > 0) {
                        byte[] var6 = this.aliveSlots(0, this.actors.length);
                        this.actionPlans[var1[0]][1] = 1;
                        byte[] var9 = Utils.removeValue(var6, var1[0]);
                        int var13 = GameUtil.randomRange(0, var9.length);
                        this.actionPlans[var1[0]][2] = var9[var13];
                        if (this.actors[var1[1]] != null && this.actors[var1[1]].stunTurns > 0) {
                           this.phase = 2;
                           this.commandSlot = 0;
                           this.commandIndex = 0;
                        } else if (this.actors[var1[1]] != null && this.actors[var1[1]].confuseTurns > 0) {
                           this.actionPlans[var1[1]][1] = 1;
                           var9 = Utils.removeValue(var6, var1[1]);
                           var13 = GameUtil.randomRange(0, var9.length);
                           this.actionPlans[var1[0]][2] = var9[var13];
                           this.commandSlot = 0;
                           this.commandIndex = 0;
                           this.phase = 2;
                        } else {
                           this.commandSlot = var1[1];
                           this.commandIndex = 7;
                           this.targetCursor = this.firstAliveSlot(10, 20);
                        }
                     } else {
                        this.commandSlot = var1[0];
                        this.commandIndex = 7;
                        this.targetCursor = this.firstAliveSlot(10, 20);
                     }
                  }
               }
               break label91;
            }
         } else {
            this.actionPlans[this.commandSlot][2] = this.targetCursor;
            if (this.commandSlot == 1 && this.actors[2] != null && !this.actors[2].isDead) {
               if (this.actors[2].stunTurns > 0) {
                  this.phase = 2;
                  this.commandSlot = 0;
                  break label91;
               }

               if (this.actors[2].confuseTurns <= 0) {
                  this.commandSlot = 2;
                  this.commandIndex = 7;
                  this.targetCursor = this.firstAliveSlot(10, 20);
                  break label91;
               }

               byte[] var5 = this.aliveSlots(0, this.actors.length);
               this.actionPlans[2][1] = 1;
               byte[] var8 = Utils.removeValue(var5, 2);
               int var12 = GameUtil.randomRange(0, var8.length);
               this.actionPlans[2][2] = var8[var12];
            }
         }

         this.phase = 2;
         this.commandSlot = 0;
         this.commandIndex = 0;
      }

      this.menuMode = 0;
   }

   public final void dispose() {
      for (int var1 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null) {
            this.actors[var1].sprite = null;
         }

         this.actors[var1] = null;
      }

      if (this.petList != null) {
         this.petList.clearColumns();
         this.petList = null;
      }

      if (this.skillList != null) {
         this.skillList.clearColumns();
         this.skillList = null;
      }

      if (this.itemList != null) {
         this.itemList.clearColumns();
         this.itemList = null;
      }

      if (this.skillLevelList != null) {
         this.skillLevelList.clearColumns();
         this.skillLevelList = null;
      }

      this.petPreview = null;
   }

   public final void onDefeat() {
      playBattleMusic(2);
      if (this.isStoryBattle) {
         this.defeatLines = null;
      } else {
         this.defeatLines = new String[]{"Mất một nửa vàng", "Tự về làng"};
      }

      this.defeatLineIndex = 0;
      this.loadMenuImages();
      this.createMenuPanels(142, this.menuHeight - 142);
      this.saveStatsToParty();
      this.saveSkillProgressToParty();
      GameData.storeRoleRecords(new RoleSprite[]{this.party[0]});

      for (int var1 = 0; var1 < this.party.length; var1++) {
         if (var1 == 0) {
            GameData.refreshRoleStats(this.party[var1]);
         } else if (this.party[var1] != null) {
            GameData.growPetSpriteStats(this.party[var1]);
         }
      }

      this.phase = 25;
   }

   public final Point attackPosition(int var1, int var2) {
      Point var3 = new Point();
      if (var1 >= 10) {
         var3.x = this.actors[var1].homePos.x + this.actors[var2].spriteW;
      } else {
         var3.x = this.actors[var1].homePos.x - this.actors[var2].spriteW;
      }

      var3.y = this.actors[var1].homePos.y;
      return var3;
   }

   public final void tick() {
      if (GameCanvas.instance.frameCounter % 3 == 0) {
         this.cursorBob = this.cursorBob == 0 ? 3 : 0;
      }

      if (this.actors[0] != null) {
         for (int var1 = 0; var1 < this.actors.length; var1++) {
            BattleActor var2;
            if (this.actors[var1] != null && (var2 = this.actors[var1]).isPresent && var2.state != 0) {
               if (var2.state == 6) {
                  if (var2.sprite.getFrame() >= var2.sprite.getActionFrameCount(1) - 1) {
                     if (var2.moveType == 1) {
                        var2.sprite.isVisible = false;
                        var2.hasArrived = false;
                     } else {
                        Point var30 = var2.attackPos;
                        Point var6 = var2.homePos;
                        var2.moveDir = BattleMotion.directionTo(var6.x, var30.x);
                        var2.moveVelocity = BattleMotion.velocity(var6.x, var6.y, var30.x, var30.y, var2.sprite.getActionFrameCount(2));
                        var2.hasArrived = false;
                     }

                     BattleActor.setSpriteAction(var2.sprite, 2, 0);
                     var2.hiddenTicks = 0;
                     var2.state = 14;
                  }
               } else if (var2.state == 14) {
                  if (var2.moveType == 0) {
                     if (GameCanvas.instance.frameCounter % 3 == 0) {
                        var2.pos = BattleMotion.step(var2.pos.x, var2.pos.y, var2.moveVelocity[0], var2.moveVelocity[1], var2.moveDir);
                     }

                     if (var2.slot < 10) {
                        if (var2.pos.x <= var2.attackPos.x + var2.spriteW) {
                           if (var2.inCombo) {
                              var2.state = 13;
                              if (var2.comboPartners != null) {
                                 GameCanvas.instance.world.battle.phase = 8;
                                 BattleActor.setSpriteAction(var2.sprite, 3, 0);
                              } else {
                                 BattleActor.setSpriteAction(var2.sprite, 0, 0);
                              }
                           } else {
                              if (var2.isCrit) {
                                 GameCanvas.instance.world.battle.critFlash = true;
                              }

                              var2.state = 1;
                              BattleActor.setSpriteAction(var2.sprite, 3, 0);
                           }
                        }
                     } else if (var2.pos.x >= var2.attackPos.x - var2.spriteW) {
                        if (var2.inCombo) {
                           var2.state = 13;
                           if (var2.comboPartners != null) {
                              GameCanvas.instance.world.battle.phase = 8;
                              BattleActor.setSpriteAction(var2.sprite, 3, 0);
                           } else {
                              BattleActor.setSpriteAction(var2.sprite, 0, 0);
                           }
                        } else {
                           if (var2.isCrit) {
                              GameCanvas.instance.world.battle.critFlash = true;
                           }

                           var2.state = 1;
                           BattleActor.setSpriteAction(var2.sprite, 3, 0);
                        }
                     }
                  } else if (var2.hiddenTicks > 6) {
                     var2.sprite.isVisible = true;
                     if (var2.inCombo) {
                        var2.state = 13;
                        if (var2.comboPartners != null) {
                           GameCanvas.instance.world.battle.phase = 8;
                           BattleActor.setSpriteAction(var2.sprite, 3, 0);
                        } else {
                           BattleActor.setSpriteAction(var2.sprite, 0, 0);
                        }
                     } else {
                        if (var2.isCrit) {
                           GameCanvas.instance.world.battle.critFlash = true;
                        }

                        BattleActor.setSpriteAction(var2.sprite, 3, 0);
                        var2.state = 1;
                     }

                     var2.hiddenTicks = 0;
                     var2.pos.x = DeviceConfig.screenSize[0] >> 1;
                     int var106 = var2.targetSlot;
                     Battle var69 = GameCanvas.instance.world.battle;
                     var2.pos.y = GameCanvas.instance.world.battle.actors[var106].homePos.y;
                  } else if (GameCanvas.instance.frameCounter % 3 == 0) {
                     var2.hiddenTicks++;
                  }
               } else if (var2.state == 1) {
                  if (var2.sprite.getFrame() >= var2.sprite.getActionFrameCount(3) - 1) {
                     if (!var2.isCountering) {
                        int var103 = var2.targetSlot;
                        Battle var66 = GameCanvas.instance.world.battle;
                        if (GameCanvas.instance.world.battle.actors[var103].hasCounter) {
                           var103 = var2.targetSlot;
                           var66 = GameCanvas.instance.world.battle;
                           if (!GameCanvas.instance.world.battle.actors[var103].isCountering && var2.confuseTurns == 0 && var2.stunTurns == 0) {
                              int var4 = GameUtil.randomBetween(1, 100);
                              if (50 > var4) {
                                 var103 = var2.targetSlot;
                                 var66 = GameCanvas.instance.world.battle;
                                 GameCanvas.instance.world.battle.actors[var103].isCountering = true;
                              }
                           }
                        }
                     }

                     var2.actionAnimDone = true;
                     if (var2.moveType == 0) {
                        var2.startReturn();
                     }

                     BattleActor.setSpriteAction(var2.sprite, 0, 0);
                     var2.hasArrived = false;
                     if (var2.sprite.getFrame() <= var2.hitFrames.length) {
                        var2.blowFlag = true;
                     }

                     var2.state = 2;
                  } else if (var2.sprite.getFrame() > var2.hitFrames.length) {
                     for (int var29 = 0; var29 < var2.hitFrames.length; var29++) {
                        if (var2.sprite.getFrame() == var2.hitFrames[var29]) {
                           var2.blowFlag = true;
                        }
                     }
                  }
               } else if (var2.state == 8) {
                  int var101 = var2.targetSlot;
                  Battle var64 = GameCanvas.instance.world.battle;
                  if (GameCanvas.instance.world.battle.actors[var101].state == 0) {
                     var101 = var2.targetSlot;
                     var64 = GameCanvas.instance.world.battle;
                     GameCanvas.instance.world.battle.actors[var101].isCountering = false;
                     BattleActor.setSpriteAction(var2.sprite, 0, 0);
                     var2.stats[3] = var2.hp;
                     if (var2.stats[3] <= 0) {
                        var2.stats[3] = 0;
                        var2.hp = 0;
                        var2.isDead = true;
                        var2.sprite.isVisible = false;
                        var2.state = 128;
                     } else {
                        var2.state = 0;
                     }
                  }
               } else if (var2.state == 15) {
                  if (!var2.hasArrived) {
                     if (var2.moveType == 0) {
                        if (GameCanvas.instance.frameCounter % 3 == 0) {
                           var2.pos = BattleMotion.step(var2.pos.x, var2.pos.y, var2.moveVelocity[0], var2.moveVelocity[1], var2.moveDir);
                        }

                        if (var2.slot < 10) {
                           if (var2.pos.x >= var2.homePos.x - var2.spriteW / 2) {
                              var2.hasArrived = true;
                              BattleActor.setSpriteAction(var2.sprite, 0, 0);
                           }
                        } else if (var2.pos.x <= var2.homePos.x + var2.spriteW / 2) {
                           var2.hasArrived = true;
                           BattleActor.setSpriteAction(var2.sprite, 0, 0);
                        }
                     } else if (var2.hiddenTicks > 6) {
                        var2.pos.x = var2.homePos.x;
                        var2.pos.y = var2.homePos.y;
                        var2.sprite.isVisible = true;
                        var2.hasArrived = true;
                        BattleActor.setSpriteAction(var2.sprite, 0, 0);
                        var2.hiddenTicks = 0;
                     } else if (GameCanvas.instance.frameCounter % 3 == 0) {
                        var2.hiddenTicks++;
                     }
                  }

                  if (var2.hasArrived) {
                     var2.pos.x = var2.homePos.x;
                     var2.pos.y = var2.homePos.y;
                     int var95 = var2.targetSlot;
                     Battle var58 = GameCanvas.instance.world.battle;
                     if (GameCanvas.instance.world.battle.actors[var95].isCountering) {
                        var2.state = 8;
                        var95 = var2.targetSlot;
                        var58 = GameCanvas.instance.world.battle;
                        BattleActor.setSpriteAction(GameCanvas.instance.world.battle.actors[var95].sprite, 3, 0);
                        var95 = var2.targetSlot;
                        var58 = GameCanvas.instance.world.battle;
                        GameCanvas.instance.world.battle.actors[var95].targetSlot = var2.slot;
                        GameCanvas.instance.world.battle.normalAttack(var2.targetSlot, var2.slot);
                        var95 = var2.targetSlot;
                        var58 = GameCanvas.instance.world.battle;
                        GameCanvas.instance.world.battle.actors[var95].attackPos = GameCanvas.instance.world.battle.attackPosition(var2.slot, var2.targetSlot);
                        var95 = var2.targetSlot;
                        var58 = GameCanvas.instance.world.battle;
                        GameCanvas.instance.world.battle.actors[var95].state = 6;
                        var95 = var2.targetSlot;
                        var58 = GameCanvas.instance.world.battle;
                        GameCanvas.instance.world.battle.actors[var95].sprite.setAction(1);
                     } else {
                        var2.pos.x = var2.homePos.x;
                        var2.pos.y = var2.homePos.y;
                        var2.state = 0;
                        var2.resultShown = false;
                     }
                  }
               } else if (var2.state == 13) {
                  if (var2.sprite.getAction() == 3) {
                     if (var2.sprite.getFrame() >= var2.sprite.getActionFrameCount(3) - 1) {
                        if (var2.sprite.getFrame() <= var2.hitFrames.length) {
                           var2.blowFlag = true;
                        }

                        BattleActor.setSpriteAction(var2.sprite, 0, 0);
                        var2.state = 16;
                     } else if (var2.sprite.getFrame() > var2.hitFrames.length) {
                        for (int var3 = 0; var3 < var2.hitFrames.length; var3++) {
                           if (var2.sprite.getFrame() == var2.hitFrames[var3]) {
                              if (GameCanvas.instance.frameCounter % 2 == 0) {
                                 int var91 = var2.targetSlot;
                                 Battle var54 = GameCanvas.instance.world.battle;
                                 int var92 = var2.targetSlot;
                                 var54 = GameCanvas.instance.world.battle;
                                 GameCanvas.instance.world.battle.actors[var91].pos.x = GameCanvas.instance.world.battle.actors[var92].homePos.x - 4;
                              } else {
                                 int var93 = var2.targetSlot;
                                 Battle var56 = GameCanvas.instance.world.battle;
                                 int var94 = var2.targetSlot;
                                 var56 = GameCanvas.instance.world.battle;
                                 GameCanvas.instance.world.battle.actors[var93].pos.x = GameCanvas.instance.world.battle.actors[var94].homePos.x + 4;
                              }

                              var2.blowFlag = true;
                           }
                        }
                     }
                  }
               } else if (var2.state == 5) {
                  if (var2.castIntroDone && var2.sprite.getFrame() >= var2.sprite.getActionFrameCount(4) - 1) {
                     BattleActor.setSpriteAction(var2.sprite, 0, 0);
                     var2.actionAnimDone = true;
                     if (var2.hitsLeft > 0) {
                        var2.hitsLeft--;
                     }

                     var2.state = 10;
                  }
               } else if (var2.state != 9 && var2.state != 17) {
                  if (var2.state == 10 && var2.effectDone && var2.resultShown) {
                     var2.resultShown = false;
                     if (var2.effectDone && var2.skillId == 39) {
                        int var7 = var2.targetSlot;
                        Battle var5 = GameCanvas.instance.world.battle;
                        if (!Utils.contains(GameData.discoveredPetIds, GameCanvas.instance.world.battle.actors[var7].roleId)) {
                           var7 = var2.targetSlot;
                           var5 = GameCanvas.instance.world.battle;
                           GameData.discoveredPetIds = Utils.append(GameData.discoveredPetIds, GameCanvas.instance.world.battle.actors[var7].roleId);
                           GameCanvas.instance.world.battle.phase = 5;
                        } else {
                           GameCanvas.instance.showToast("Đã có sủng vật này, trinh sát vô hiệu", 8);
                        }
                     }

                     var2.effectDone = false;
                     if (!GameCanvas.instance.world.battle.showBackground) {
                        GameCanvas.instance.world.battle.showBackground = true;
                     }

                     if (var2.hitsLeft == 0) {
                        var2.hitsLeft = 0;
                        var2.castIntroDone = false;
                        var2.state = 0;
                     } else {
                        int var90 = var2.targetSlot;
                        Battle var53 = GameCanvas.instance.world.battle;
                        if (GameCanvas.instance.world.battle.actors[var90].isDead) {
                           var2.hitsLeft = 0;
                           var2.castIntroDone = false;
                           var2.state = 0;
                        } else {
                           var2.castIntroDone = true;
                           var2.setAction(4, 0);
                           GameCanvas.instance.world.battle.normalAttack(var2.slot, var2.targetSlot);
                           var2.state = 5;
                        }
                     }
                  }
               } else if (!var2.actionAnimDone && var2.sprite.getFrame() >= var2.sprite.getActionFrameCount(5) - 1) {
                  BattleActor.setSpriteAction(var2.sprite, 0, 0);
                  var2.actionAnimDone = true;
               }
            }
         }

         if (this.phase == 2) {
            Battle var28 = this;
            Battle var77 = this;

            for (int var86 = 10; var86 < var77.actionPlans.length; var86++) {
               if (var77.actors[var86] != null && var77.actors[var86].state != 128 && var77.actors[var86].stunTurns == 0) {
                  var77.actionPlans[var86][0] = var86;
                  if (var77.actors[var86].confuseTurns != 0) {
                     byte[] var43 = Utils.removeValue(var77.aliveSlots(0, 20), var86);
                     var77.actionPlans[var86][1] = 1;
                     if (var43.length > 1) {
                        int var25 = GameUtil.randomRange(0, var43.length);
                        var77.actionPlans[var86][2] = var43[var25];
                     } else {
                        var77.actionPlans[var86][2] = var43[0];
                     }
                  } else if (var77.actors[var86].silenceTurns > 0) {
                     byte[] var42 = var77.aliveSlots(0, 10);
                     var77.actionPlans[var86][1] = 1;
                     if (var42.length > 1) {
                        int var24 = GameUtil.randomRange(0, var42.length);
                        var77.actionPlans[var86][2] = var42[var24];
                     } else {
                        var77.actionPlans[var86][2] = var42[0];
                     }
                  } else {
                     byte var113 = 0;
                     int var115 = GameUtil.randomBetween(1, 100);
                     byte var18;
                     byte var37;
                     if (var77.actors[var86].hp > var77.actors[var86].stats[3] / 2) {
                        var37 = 50;
                        var18 = 100;
                     } else {
                        var37 = 1;
                        var18 = 70;
                        var113 = 100;
                     }

                     if (var115 < var37) {
                        byte[] var23 = var77.aliveSlots(0, 10);
                        var77.actionPlans[var86][1] = 1;
                        if (var23.length > 1) {
                           var37 = GameUtil.randomRange(0, var23.length);
                           var77.actionPlans[var86][2] = var23[var37];
                        } else {
                           var77.actionPlans[var86][2] = var23[0];
                        }
                     } else if (var115 >= var18) {
                        if (var115 < var113) {
                           var77.actionPlans[var86][1] = 2;
                        }
                     } else if (var77.actors[var86].activeSkillIds == null) {
                        byte[] var22 = var77.aliveSlots(0, 10);
                        var77.actionPlans[var86][1] = 1;
                        if (var22.length > 1) {
                           var37 = GameUtil.randomRange(0, var22.length);
                           var77.actionPlans[var86][2] = var22[var37];
                        } else {
                           var77.actionPlans[var86][2] = var22[0];
                        }
                     } else {
                        var18 = GameUtil.randomRange(0, var77.actors[var86].activeSkillIds.length);
                        var77.actionPlans[var86][3] = var77.actors[var86].activeSkillIds[var18];
                        short var38;
                        int var117 = GameData.findSkillIndex(var38 = var77.actors[var86].activeSkillIds[var18]);
                        BattleActor var132 = var77.actors[var86];
                        var18 = var18;
                        int var124 = var117;
                        int var129 = var77.actors[var86].mp;
                        BattleActor var128 = var132;
                        var38 = -1;

                        for (int var50 = 0; var50 < var128.skillLevels[var18]; var50++) {
                           if (Skill.mpCostsPerLevel[var124][var50] <= var129) {
                              var38 = var50;
                           }
                        }

                        if (var38 != -1) {
                           var18 = var38 + 1;
                           var77.actionPlans[var86][1] = 4;
                           var77.actionPlans[var86][4] = var18;
                           if (Skill.targetSides[var117] == 0) {
                              var77.actionPlans[var86][2] = var86;
                           } else if (Skill.targetSides[var117] == 1) {
                              byte[] var119;
                              if (Skill.targetScopes[var117] == 1 && (var119 = var77.aliveSlots(10, 20)) != null) {
                                 if (var119.length > 1) {
                                    var124 = GameUtil.randomRange(0, var119.length);
                                    var77.actionPlans[var86][2] = var119[var124];
                                 } else {
                                    var77.actionPlans[var86][2] = var119[0];
                                 }
                              }
                           } else {
                              byte[] var120;
                              if (Skill.targetSides[var117] == 2 && Skill.targetScopes[var117] == 1 && (var120 = var77.aliveSlots(0, 10)) != null) {
                                 if (var120.length > 1) {
                                    var124 = GameUtil.randomRange(0, var120.length);
                                    var77.actionPlans[var86][2] = var120[var124];
                                 } else {
                                    var77.actionPlans[var86][2] = var120[0];
                                 }
                              }
                           }
                        } else {
                           byte[] var121 = var77.aliveSlots(0, 10);
                           var77.actionPlans[var86][1] = 1;
                           if (var121.length > 1) {
                              var124 = GameUtil.randomRange(0, var121.length);
                              var77.actionPlans[var86][2] = var121[var124];
                           } else {
                              var77.actionPlans[var86][2] = var121[0];
                           }
                        }
                     }
                  }
               }
            }

            var77 = this;
            int var87 = 0;

            boolean var133;
            while (true) {
               if (var87 < var77.actionPlans.length) {
                  int var44;
                  if ((var44 = var77.actionPlans[var87][1]) != 2 && var44 != 6) {
                     var87++;
                     continue;
                  }

                  var133 = true;
                  break;
               }

               var133 = false;
               break;
            }

            if (var133) {
               for (int var45 = 0; var45 < var28.actionPlans.length; var45++) {
                  int var51;
                  if ((var51 = var28.actionPlans[var45][1]) == 2) {
                     var28.actionPlans[var45][1] = -1;
                     var28.actors[var28.actionPlans[var45][0]].isDefending = true;
                     var28.actors[var28.actionPlans[var45][0]].state = 3;
                  } else if (var51 == 6) {
                     var28.actionPlans[var45][1] = -1;
                     if (var28.actors[var28.actionPlans[var45][2]] == null) {
                        if (var45 > 9) {
                           if (var28.actors[var45].targetSlot > 9) {
                              var28.actors[var45].targetSlot = var45;
                           }
                        } else if (var45 < 9) {
                           var28.actors[var45].targetSlot = var45;
                        }
                     } else {
                        var28.actors[var45].targetSlot = var28.actionPlans[var45][2];
                     }

                     BattleActor var134 = var28.actors[var45];
                     byte var88 = 9;
                     var134.state = 9;
                     var28.actors[var45].setAction(5, 0);
                     GameData.useItemInBattle(var28.actors[var28.actors[var45].targetSlot], var28.usableItems[var28.itemList.getSelectedRow()][0]);
                  }
               }

               var28.phase = 6;
            } else {
               this.phase = 3;
            }

            var28.actingSlot = var28.turnOrder[0][0];
         } else {
            if (this.phase == 6) {
               Battle var26 = this;
               int var31 = 0;

               boolean var131;
               while (true) {
                  if (var31 >= var26.actors.length) {
                     var131 = true;
                     break;
                  }

                  if (var26.actors[var31] != null && !var26.actors[var31].isDead && var26.actors[var31].state != 0) {
                     var131 = false;
                     break;
                  }

                  var31++;
               }

               if (var131) {
                  this.phase = 3;
                  return;
               }
            } else if (this.phase == 3) {
               Battle var27 = this;
               if (this.actors[0].state == 128) {
                  this.resetActorPoses();
                  this.clearAllStatus();
                  this.onDefeat();
                  if (!GameCanvas.instance.world.script.isMustLoseBattle) {
                     if (!PaidServiceMenu.isFullVersion) {
                        this.onDefeat();
                        return;
                     }

                     PaidServiceMenu.open("/bin/smsmenu.bin");
                     GameCanvas.instance.world.smsMenu.purchase(7);
                     this.smsContext = 1;
                  }
               } else {
                  if (this.allEnemiesDead()) {
                     this.resetActorPoses();
                     this.onVictory();
                     return;
                  }

                  if (this.turnIndex >= this.turnOrder.length) {
                     this.resetActorPoses();
                     this.endRound();
                     return;
                  }

                  for (int var32 = 0; var32 < var27.actors.length; var32++) {
                     if (var27.actors[var32] != null && var27.actors[var32].isDead) {
                        var27.actors[var32].sprite.isVisible = true;

                        for (int var46 = 1; var46 < var27.actionPlans[var32].length; var46++) {
                           var27.actionPlans[var32][var46] = -1;
                        }
                     }
                  }

                  if (var27.actors[var27.actingSlot] != null) {
                     int var33;
                     if ((var33 = var27.actionPlans[var27.actingSlot][1]) == 1 && var27.actors[var27.actingSlot].stunTurns == 0) {
                        if (var27.actionPlans[var27.actingSlot][2] != -1) {
                           if (var27.actors[var27.actingSlot].confuseTurns != 0) {
                              byte[] var73;
                              if ((var73 = Utils.removeValue(var27.aliveSlots(0, var27.actors.length), var27.actingSlot)).length > 1) {
                                 int var85 = GameUtil.randomRange(0, var73.length);
                                 var27.actionPlans[var27.actingSlot][2] = var73[var85];
                              } else {
                                 var27.actionPlans[var27.actingSlot][2] = var73[0];
                              }
                           }

                           var27.actors[var27.actingSlot].targetSlot = var27.retargetIfDead(var27.actionPlans[var27.actingSlot][2]);
                           var27.actionPlans[var27.actingSlot][2] = var27.actors[var27.actingSlot].targetSlot;
                           Battle var111 = var27;
                           byte[] var8 = null;
                           byte var15;
                           byte var35;
                           if (var27.actingSlot > 9) {
                              var15 = 10;
                              var35 = 20;
                           } else {
                              var15 = 0;
                              var35 = 10;
                           }

                           for (int var9 = var15; var9 < var35; var9++) {
                              if (var111.actors[var111.actingSlot].confuseTurns == 0
                                 && var111.actionPlans[var111.actingSlot][0] != var111.actionPlans[var9][0]
                                 && var111.actors[var111.actionPlans[var9][0]] != null
                                 && var111.actors[var111.actionPlans[var9][0]].confuseTurns == 0
                                 && var111.actors[var111.actionPlans[var9][0]].stunTurns == 0
                                 && var111.actionPlans[var111.actingSlot][1] == 1
                                 && var111.actionPlans[var9][1] == 1
                                 && var111.actionPlans[var111.actingSlot][2] != -1
                                 && var111.actionPlans[var111.actingSlot][2] == var111.actionPlans[var9][2]) {
                                 int var10001 = var111.actionPlans[var111.actingSlot][0];
                                 int var11 = var111.actionPlans[var9][0];
                                 int var10 = var10001;
                                 if (var111.actors[var10] == null || var111.actors[var10].isDead || var111.actors[var11] == null || var111.actors[var11].isDead
                                    ? false
                                    : var111.actors[var10].speed >= var111.actors[var11].speed - 150
                                       && var111.actors[var10].speed <= var111.actors[var11].speed + 150) {
                                    if (!Utils.contains(var8, var111.actionPlans[var111.actingSlot][0])) {
                                       var8 = Utils.append(var8, var111.actionPlans[var111.actingSlot][0]);
                                    }

                                    if (!Utils.contains(var8, var111.actionPlans[var9][0])) {
                                       var8 = Utils.append(var8, var111.actionPlans[var9][0]);
                                    }
                                 }
                              }
                           }

                           byte[] var48 = var8;
                           if (var8 != null && var27.actors[var27.actingSlot].confuseTurns == 0) {
                              var27.actors[var27.actingSlot].comboPartners = var8;
                              boolean var116 = false;
                              var35 = var27.actors[var27.actingSlot].targetSlot;
                              var15 = var27.actingSlot;
                              var8 = var8;
                              var111 = var27;
                              if (rollHit(var27.actors[var15], var27.actors[var35], 0)) {
                                 var15 = 0;

                                 for (int var118 = 0; var118 < var8.length; var118++) {
                                    int var122 = 200 + elementAdvantage(var111.actors[var8[var118]].elements, var111.actors[var35].elements);
                                    int var12 = GameUtil.randomBetween(0, 5);
                                    int var13 = var111.actors[var8[var118]].attack;
                                    var122 = var111.actors[var8[var118]].attack * var122 / 100 - var111.actors[var35].defense + var12;
                                    var15 += var122;
                                 }

                                 if (var111.actors[var35].isDefending) {
                                    var15 /= 5;
                                 }

                                 if (var15 < 10) {
                                    var15 = 10;
                                 }

                                 var111.actors[var35].hp -= var15 * 110 / 100;
                              } else {
                                 var27.actors[var35].dodged = true;
                              }

                              for (int var76 = 0; var76 < var48.length; var76++) {
                                 if (!var27.actors[var27.actionPlans[var48[var76]][0]].isDead) {
                                    var27.actors[var27.actionPlans[var48[var76]][0]].targetSlot = var27.actionPlans[var48[var76]][2];
                                    var27.actors[var27.actionPlans[var48[var76]][0]].attackPos = var27.attackPosition(
                                       var27.actionPlans[var48[var76]][2], var27.actionPlans[var48[var76]][0]
                                    );
                                    var27.actors[var27.actionPlans[var48[var76]][0]].inCombo = true;
                                    var27.actors[var27.actionPlans[var48[var76]][0]].state = 6;
                                    var27.actors[var27.actionPlans[var48[var76]][0]].sprite.setAction(1);
                                    var27.actionPlans[var27.actionPlans[var48[var76]][0]][1] = -1;
                                    var27.actionPlans[var27.actionPlans[var48[var76]][0]][2] = -1;
                                 }
                              }

                              var27.actors[var27.actingSlot].comboPartners = Utils.removeValue(
                                 var27.actors[var27.actingSlot].comboPartners, var27.actors[var27.actingSlot].slot
                              );
                           } else {
                              var27.actors[var27.actingSlot].targetSlot = var27.actionPlans[var27.actingSlot][2];
                              var27.actors[var27.actingSlot].attackPos = var27.attackPosition(var27.actors[var27.actingSlot].targetSlot, var27.actingSlot);
                              if (var27.actingSlot < 9) {
                                 if (GameUtil.randomRange(1, 100) <= var27.actors[var27.actingSlot].critRate) {
                                    var27.actors[var27.actingSlot].isCrit = true;
                                 }

                                 if (var27.actors[var27.actingSlot].alwaysCrit) {
                                    var27.actors[var27.actingSlot].isCrit = true;
                                 }
                              }

                              var27.normalAttack(var27.actingSlot, var27.actors[var27.actingSlot].targetSlot);
                              if (var27.actors[var27.actingSlot].confuseTurns != 0) {
                                 if (var27.actingSlot > 9) {
                                    if (var27.actors[var27.actingSlot].targetSlot > 9) {
                                       var27.actors[var27.actingSlot].castIntroDone = true;
                                       byte[] var74;
                                       (var74 = new byte[1])[0] = (byte)var27.actors[var27.actingSlot].targetSlot;
                                       var27.actors[var27.actingSlot].effectSprites = new AnimSprite[1];
                                       var27.actors[var27.actingSlot].effectSprites[0] = new AnimSprite();
                                       var27.actors[var27.actingSlot].effectSprites[0].loadInGroup("/ft/gongji1.av", 1);
                                       var27.actors[var27.actingSlot].targets = var74;
                                       var27.actors[var27.actingSlot].setAction(4, 0);
                                       var27.actors[var27.actingSlot].state = 5;
                                    } else {
                                       var27.actors[var27.actingSlot].state = 6;
                                       var27.actors[var27.actingSlot].sprite.setAction(1);
                                    }
                                 } else if (var27.actors[var27.actingSlot].targetSlot < 10) {
                                    byte[] var75;
                                    (var75 = new byte[1])[0] = (byte)var27.actors[var27.actingSlot].targetSlot;
                                    var27.actors[var27.actingSlot].effectSprites = new AnimSprite[1];
                                    var27.actors[var27.actingSlot].effectSprites[0] = new AnimSprite();
                                    var27.actors[var27.actingSlot].effectSprites[0].load("/ft/gongji1.av");
                                    var27.actors[var27.actingSlot].targets = var75;
                                    var27.actors[var27.actingSlot].castIntroDone = true;
                                    var27.actors[var27.actingSlot].setAction(4, 0);
                                    var27.actors[var27.actingSlot].state = 5;
                                 } else {
                                    var27.actors[var27.actingSlot].state = 6;
                                    var27.actors[var27.actingSlot].sprite.setAction(1);
                                 }
                              } else {
                                 var27.actors[var27.actingSlot].state = 6;
                                 var27.actors[var27.actingSlot].sprite.setAction(1);
                              }

                              var27.actionPlans[var27.actingSlot][1] = -1;
                              var27.actionPlans[var27.actingSlot][2] = -1;
                           }
                        }
                     } else if (var33 == 4 && var27.actors[var27.actingSlot].stunTurns == 0 && var27.actionPlans[var27.actingSlot][3] != -1) {
                        if (var27.actors[var27.actingSlot].confuseTurns != 0) {
                           byte[] var72;
                           if ((var72 = Utils.removeValue(var27.aliveSlots(0, var27.actors.length), var27.actingSlot)).length > 1) {
                              int var82 = GameUtil.randomRange(0, var72.length);
                              var27.actionPlans[var27.actingSlot][2] = var72[var82];
                           } else {
                              var27.actionPlans[var27.actingSlot][2] = var72[0];
                           }

                           var27.actors[var27.actingSlot].targetSlot = var27.retargetIfDead(var27.actionPlans[var27.actingSlot][2]);
                           var27.normalAttack(var27.actingSlot, var27.actors[var27.actingSlot].targetSlot);
                           if (var27.actingSlot > 9) {
                              if (var27.actors[var27.actingSlot].targetSlot > 9) {
                                 var27.actors[var27.actingSlot].castIntroDone = true;
                                 byte[] var83;
                                 (var83 = new byte[1])[0] = (byte)var27.actors[var27.actingSlot].targetSlot;
                                 var27.actors[var27.actingSlot].effectSprites = new AnimSprite[1];
                                 var27.actors[var27.actingSlot].effectSprites[0] = new AnimSprite();
                                 var27.actors[var27.actingSlot].effectSprites[0].loadInGroup("/ft/gongji1.av", 1);
                                 var27.actors[var27.actingSlot].targets = var83;
                                 var27.actors[var27.actingSlot].setAction(4, 0);
                                 var27.actors[var27.actingSlot].state = 5;
                              } else {
                                 var27.actors[var27.actingSlot].state = 6;
                                 var27.actors[var27.actingSlot].sprite.setAction(1);
                              }
                           } else if (var27.actors[var27.actingSlot].targetSlot < 10) {
                              byte[] var84;
                              (var84 = new byte[1])[0] = (byte)var27.actors[var27.actingSlot].targetSlot;
                              var27.actors[var27.actingSlot].effectSprites = new AnimSprite[1];
                              var27.actors[var27.actingSlot].effectSprites[0] = new AnimSprite();
                              var27.actors[var27.actingSlot].effectSprites[0].loadInGroup("/ft/gongji1.av", 1);
                              var27.actors[var27.actingSlot].targets = var84;
                              var27.actors[var27.actingSlot].castIntroDone = true;
                              var27.actors[var27.actingSlot].setAction(4, 0);
                              var27.actors[var27.actingSlot].state = 5;
                           } else {
                              var27.actors[var27.actingSlot].state = 6;
                              var27.actors[var27.actingSlot].sprite.setAction(1);
                           }
                        } else {
                           int var47 = GameData.findSkillIndex(var27.actionPlans[var27.actingSlot][3]);
                           if (Skill.targetSides[var47] == 1) {
                              if (var27.actionPlans[var27.actingSlot][2] != -1
                                 && var27.actors[var27.actionPlans[var27.actingSlot][2]] != null
                                 && !var27.actors[var27.actionPlans[var27.actingSlot][2]].isDead) {
                                 var27.actors[var27.actingSlot].targetSlot = var27.actionPlans[var27.actingSlot][2];
                              } else {
                                 var27.actors[var27.actingSlot].targetSlot = var27.actingSlot;
                              }
                           } else {
                              var27.actors[var27.actingSlot].targetSlot = var27.actionPlans[var27.actingSlot][2];
                           }

                           var27.actors[var27.actingSlot].skillId = var27.actionPlans[var27.actingSlot][3];
                           var27.applySkill(var27.actors[var27.actingSlot], var27.actionPlans[var27.actingSlot][3], var27.actionPlans[var27.actingSlot][4]);
                           if (var27.actors[var27.actingSlot].skillId == 39) {
                              var27.actors[var27.actingSlot].mp -= 30;
                              int var107 = var27.actors[var27.actingSlot].targetSlot;
                              GameCanvas.instance.world.battle.scoutedSlot = var27.actors[var107].slot;
                              var107 = var27.actors[var27.actingSlot].targetSlot;
                              GameCanvas.instance.world.battle.scoutedSprite = GameUtil.loadBattleAnimation(var27.actors[var107].roleId, true);

                              for (int var79 = 0; var79 < 5; var79++) {
                                 var107 = var27.actors[var27.actingSlot].targetSlot;
                                 byte[] var70 = Utils.parseByteArray(var27.petTable[var27.actors[var107].roleId - 1][4 + var79 * 2], "|");
                                 var27.scoutedStars = Utils.append(var27.scoutedStars, var70[0]);
                              }
                           }

                           if (Skill.targetSides[var47] == 1) {
                              var27.actors[var27.actingSlot].setAction(5, 0);
                              var27.actors[var27.actingSlot].state = 17;
                           } else if (var27.actors[var27.actingSlot].slot == 0) {
                              var27.actors[var27.actingSlot].state = 5;
                           } else {
                              var27.actors[var27.actingSlot].castIntroDone = true;
                              var27.actors[var27.actingSlot].setAction(4, 0);
                              var27.actors[var27.actingSlot].state = 5;
                           }

                           if (var27.actors[var27.actingSlot].skillId != 39 && var27.actingSlot < 10) {
                              int var71 = -1;

                              for (int var80 = 0; var80 < var27.actors[var27.actingSlot].skills.length; var80++) {
                                 if (var27.actors[var27.actingSlot].skills[var80].skillId == var27.actionPlans[var27.actingSlot][3]) {
                                    var71 = var80;
                                    break;
                                 }
                              }

                              if (var71 != -1 && var27.actors[var27.actingSlot].skills[var71].level <= var27.actors[var27.actingSlot].skills[var71].maxLevel) {
                                 if (PaidServiceMenu.hasLuckyPack) {
                                    var27.actors[var27.actingSlot].skills[var71].exp = var27.actors[var27.actingSlot].skills[var71].exp
                                       + 2 * var27.actionPlans[var27.actingSlot][4];
                                 } else {
                                    var27.actors[var27.actingSlot].skills[var71].exp = var27.actors[var27.actingSlot].skills[var71].exp
                                       + var27.actionPlans[var27.actingSlot][4];
                                 }

                                 boolean var81 = false;
                                 if (var27.actors[var27.actingSlot].skills[var71].exp >= var27.actors[var27.actingSlot].skills[var71].expToNext) {
                                    Skill var110;
                                    if ((var110 = var27.actors[var27.actingSlot].skills[var71]).level < var110.maxLevel) {
                                       var81 = true;
                                    } else {
                                       var27.actors[var27.actingSlot].skills[var71].exp = var27.actors[var27.actingSlot].skills[var71].expToNext;
                                    }

                                    if (var81 && var27.actors[var27.actingSlot].skills[var71].level < 10) {
                                       var27.actors[var27.actingSlot].skills[var71].level++;
                                       var27.actors[var27.actingSlot].skills[var71].exp = 0;
                                       var27.actors[var27.actingSlot].skills[var71].expToNext = (var27.actors[var27.actingSlot].skills[var71].level + 1)
                                             * (var27.actors[var27.actingSlot].skills[var71].level + 1)
                                          << 1;
                                       int var14 = GameData.findSkillIndex(var27.actors[var27.actingSlot].skills[var71].skillId);
                                       String var34 = Skill.names[var14]
                                          + "  "
                                          + (var27.actors[var27.actingSlot].skills[var71].level - 1)
                                          + " cấp->"
                                          + var27.actors[var27.actingSlot].skills[var71].level
                                          + " cấp";
                                       GameCanvas.instance.showToast(var34, 10);
                                    }
                                 }
                              }
                           }

                           var27.actionPlans[var27.actingSlot][1] = -1;
                           var27.actionPlans[var27.actingSlot][2] = -1;
                        }
                     }

                     var27.actionPlans[var27.actingSlot][1] = -1;
                     var27.actionPlans[var27.actingSlot][2] = -1;
                     if (var27.actors[var27.actingSlot].state == 0 && var27.actionPlans[var27.actingSlot][1] == -1) {
                        var27.turnIndex++;
                        var27.popupFrame = 0;
                        if (var27.turnIndex < var27.turnOrder.length) {
                           if (!var27.actors[var27.turnOrder[var27.turnIndex][0]].isDead) {
                              var27.actingSlot = var27.turnOrder[var27.turnIndex][0];
                              return;
                           }

                           if (var27.actors[0].state == 128) {
                              var27.resetActorPoses();
                              var27.clearAllStatus();
                              if (GameCanvas.instance.world.script.isMustLoseBattle) {
                                 var27.onDefeat();
                                 return;
                              }

                              if (!PaidServiceMenu.isFullVersion) {
                                 var27.onDefeat();
                                 return;
                              }

                              PaidServiceMenu.open("/bin/smsmenu.bin");
                              GameCanvas.instance.world.smsMenu.purchase(7);
                              var27.smsContext = 1;
                           } else if (var27.allEnemiesDead()) {
                              var27.resetActorPoses();
                              var27.onVictory();
                              return;
                           }
                        }
                     } else if (var27.actors[var27.actingSlot].state == 128 || var27.actors[var27.actingSlot].isDead) {
                        if (var27.actors[0].state != 128 && !var27.actors[0].isDead) {
                           if (var27.allEnemiesDead()) {
                              var27.resetActorPoses();
                              var27.onVictory();
                              return;
                           }

                           var27.turnIndex++;

                           for (int var49 = 0; var49 < var27.actors.length; var49++) {
                              if (var27.actors[var49] != null && var27.actors[var49].dodged) {
                                 var27.actors[var49].dodged = false;
                              }
                           }

                           if (var27.turnIndex < var27.turnOrder.length && !var27.actors[var27.turnOrder[var27.turnIndex][0]].isDead) {
                              var27.actingSlot = var27.turnOrder[var27.turnIndex][0];
                           }
                        } else {
                           var27.resetActorPoses();
                           var27.clearAllStatus();
                           if (GameCanvas.instance.world.script.isMustLoseBattle) {
                              var27.onDefeat();
                              return;
                           }

                           if (!PaidServiceMenu.isFullVersion) {
                              var27.onDefeat();
                              return;
                           }

                           PaidServiceMenu.open("/bin/smsmenu.bin");
                           GameCanvas.instance.world.smsMenu.purchase(7);
                           var27.smsContext = 1;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean allAlliesStunned() {
      for (int var1 = 0; var1 < 10; var1++) {
         if (this.actors[var1] != null && this.actors[var1].stats[3] > 0 && this.actors[var1].stunTurns == 0) {
            return false;
         }
      }

      return true;
   }

   private boolean allAlliesConfused() {
      for (int var1 = 0; var1 < 10; var1++) {
         if (this.actors[var1] != null && this.actors[var1].stats[3] > 0 && this.actors[var1].confuseTurns == 0) {
            return false;
         }
      }

      return true;
   }

   private int controlStatus(int var1) {
      byte var2 = -1;
      if (this.actors[var1].stunTurns != 0) {
         var2 = 0;
      } else if (this.actors[var1].confuseTurns > 0) {
         var2 = 1;
      }

      return var2;
   }

   private void saveStatsToParty() {
      for (int var1 = 0; var1 < this.actors.length / 2; var1++) {
         if (this.actors[var1] != null) {
            for (int var2 = 0; var2 < this.party.length; var2++) {
               if (this.party[var2] != null && this.party[var2].roleId == this.actors[var1].roleId && this.party[var2].petUid == this.actors[var1].partyUid) {
                  for (int var3 = 0; var3 < this.party[var1].stats.length; var3++) {
                     this.party[var2].stats[var3] = this.actors[var1].stats[var3];
                  }
               }
            }
         }
      }
   }

   public final boolean hasEnemySource() {
      return this.enemyIds != null || GameData.sceneEnemyIds != null;
   }

   private static void returnToMap(RoleSprite[] var0) {
      if (GameCanvas.instance.world.script.isMustLoseBattle) {
         for (int var1 = 0; var1 < var0.length; var1++) {
            if (var0[var1] != null) {
               var0[var1].stats[3] = var0[var1].stats[10];
               var0[var1].stats[5] = var0[var1].stats[11];
            }
         }
      }

      GameData.syncPetsFromBattle(var0);
      GameData.keepLeaderOnly(var0);
      GameData.storeRoleRecords(GameData.party);
      GameCanvas.instance.world.returnFromBattle();
      if (GameCanvas.instance.world.isDungeonBattle) {
         GameCanvas.instance.world.isDungeonBattle = false;

         for (int var5 = 0; GameCanvas.instance.world.sceneLayer.actors != null && var5 < GameCanvas.instance.world.sceneLayer.actors.length; var5++) {
            if (GameCanvas.instance.world.sceneLayer.actors[var5] != null
               && GameCanvas.instance.world.sceneLayer.actors[var5].kind == 1
               && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var5]).roleId == GameData.dungeonBosses[0][0]) {
               ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var5]).isVisible = false;
               break;
            }
         }

         int var6 = GameUtil.randomBetween(0, 100);

         for (int var2 = 0; GameData.dungeonRewards != null && var2 < GameData.dungeonRewards.length; var2++) {
            GameCanvas.instance.world.dungeonLoot = null;
            GameCanvas.instance.world.dungeonLoot = Utils.parseIntArray(GameData.dungeonRewards[var2], ",");
            if (GameCanvas.instance.world.dungeonLoot[3] >= var6) {
               if (GameCanvas.instance.world.dungeonLoot[0] == 4) {
                  var6 = (short)GameCanvas.instance.world.dungeonLoot[1];
                  String[][] var3 = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
                  String var4 = var3[--var6][0];
                  GameCanvas.instance.world.lootPetName = var4;
               }
               break;
            }
         }

         GameCanvas.instance.world.mode = 8;
      }

      if (DeviceConfig.unloadBeforeBattle) {
         GameCanvas.instance.world.beginLoading();
      } else {
         World.playSceneMusic();
      }

      GameCanvas.instance.world.script.advance(0);
   }

   private void saveSkillProgressToParty() {
      for (int var1 = 0; var1 < this.actors.length / 2; var1++) {
         if (this.actors[var1] != null) {
            for (int var2 = 0; var2 < this.party.length; var2++) {
               if (this.party[var2] != null && this.actors[var1].roleId == this.party[var2].roleId) {
                  for (int var3 = 0; this.actors[var1].skills != null && var3 < this.actors[var1].skills.length; var3++) {
                     RoleSprite var10000 = this.party[var2];
                     Skill var5 = this.actors[var1].skills[var3];
                     RoleSprite var4 = var10000;

                     for (int var6 = 0; var6 < var4.skillIds.length; var6++) {
                        if (var4.skillIds[var6] == var5.skillId) {
                           var4.skills[var6].level = var5.level;
                           var4.skills[var6].exp = var5.exp;
                           var4.skills[var6].expToNext = var5.expToNext;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void commitHpMp() {
      for (int var1 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null) {
            this.actors[var1].stats[3] = this.actors[var1].hp;
            if (this.actors[var1].stats[3] <= 0) {
               this.actors[var1].stats[3] = 0;
               this.actors[var1].hp = 0;
               this.actors[var1].isDead = true;
               this.actors[var1].state = 128;
               this.actors[var1].sprite.isVisible = false;
               this.actors[var1].dodged = false;
            }

            this.actors[var1].stats[5] = this.actors[var1].mp;
            if (this.actors[var1].stats[5] > this.actors[var1].stats[11]) {
               this.actors[var1].stats[5] = this.actors[var1].stats[11];
            }

            if (this.actors[var1].stats[3] > this.actors[var1].stats[10]) {
               this.actors[var1].stats[3] = this.actors[var1].stats[10];
            }
         }
      }
   }

   public final void endRound() {
      this.roundEffectsShown = false;
      this.popupFrame = 0;
      this.buildUsableItemList();

      for (int var1 = 0; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null) {
            this.actors[var1].stats[3] = this.actors[var1].hp;
            this.actors[var1].stats[5] = this.actors[var1].mp;
            this.actors[var1].targets = null;
            this.actors[var1].actionAnimDone = false;
            this.actors[var1].castIntroDone = false;
            this.actors[var1].effectDone = false;
            this.actors[var1].resultShown = false;
            this.actors[var1].comboPartners = null;
            this.actors[var1].inCombo = false;
            this.actors[var1].skillId = 0;
            this.actors[var1].isCrit = false;
            this.actors[var1].effectSprites = null;
            this.comboIndex = 0;
         }
      }

      if (this.allEnemiesDead()) {
         this.buildVictoryRewards();
         this.loadMenuImages();
         this.createMenuPanels(142, this.menuHeight - 142);
         this.resetActorPoses();
      } else if (!this.actors[0].isDead && this.actors[0].state != 128) {
         Battle var6 = this;

         for (int var2 = 0; var2 < var6.actors.length; var2++) {
            if (var6.actors[var2] != null) {
               if (var6.actors[var2].dodged) {
                  var6.actors[var2].dodged = false;
               }

               if (!hasNegativeStatus(var6.actors[var2])) {
                  var6.actors[var2].showStatusIcon = false;
               }

               if (var6.actors[var2].state == 3) {
                  var6.actors[var2].sprite.isVisible = true;
                  var6.actors[var2].state = 0;
               }

               if (var6.actors[var2].isDefending) {
                  var6.actors[var2].isDefending = false;
               }

               if (var6.actors[var2].stats[3] > 0) {
                  if (var6.actors[var2].attackBuff[0] > 0) {
                     var6.actors[var2].attackBuff[0]--;
                     if (var6.actors[var2].attackBuff[0] == 0) {
                        var6.actors[var2].attack = var6.actors[var2].attack - var6.actors[var2].stats[12] * var6.actors[var2].attackBuff[1] / 100;
                        var6.actors[var2].attackBuff = new byte[2];
                     }

                     if (var6.actors[var2].attack < 0) {
                        var6.actors[var2].attack = 0;
                     }
                  }

                  if (var6.actors[var2].poison[0] > 0) {
                     int var3;
                     if ((var3 = var6.actors[var2].stats[10] * var6.actors[var2].poison[1] / 100) <= 0) {
                        var3 = 1;
                     }

                     var6.actors[var2].hp -= var3;
                     var6.actors[var2].poison[0]--;
                     if (var6.actors[var2].poison[0] == 0) {
                        var6.actors[var2].poison = new byte[2];
                     }
                  }

                  if (var6.actors[var2].stunTurns > 0) {
                     var6.actors[var2].stunTurns--;
                  }

                  if (var6.actors[var2].confuseTurns > 0) {
                     var6.actors[var2].confuseTurns--;
                  }

                  if (var6.actors[var2].defenseDebuff[0] > 0) {
                     var6.actors[var2].defenseDebuff[0]--;
                     if (var6.actors[var2].defenseDebuff[0] == 0) {
                        var6.actors[var2].defense = var6.actors[var2].defense + var6.actors[var2].stats[13] * var6.actors[var2].defenseDebuff[1] / 100;
                        var6.actors[var2].defenseDebuff = new byte[2];
                     }
                  }

                  if (var6.actors[var2].rallyBuff[0] > 0) {
                     var6.actors[var2].rallyBuff[0]--;
                     if (var6.actors[var2].rallyBuff[0] == 0) {
                        var6.actors[var2].rallyBuff = new byte[2];
                     } else {
                        var6.actors[var2].attack = var6.actors[var2].attack + var6.actors[var2].stats[12] * var6.actors[var2].rallyBuff[1] / 100;
                        var6.actors[var2].defense = var6.actors[var2].defense + var6.actors[var2].stats[13] * var6.actors[var2].rallyBuff[1] / 100;
                     }
                  }

                  if (var6.actors[var2].silenceTurns > 0) {
                     var6.actors[var2].silenceTurns--;
                  }
               } else if (var6.actors[var2].stats[3] <= 0) {
                  var6.actors[var2].state = 128;
                  var6.actors[var2].isDead = true;
                  var6.actors[var2].sprite.isVisible = false;
               }
            }
         }

         this.tutorialStep++;
         if (GameCanvas.instance.world.isTutorialBattle && this.tutorialStep < 4) {
            this.dialogLines = null;
            String var23 = Utils.substringBetween(
               Utils.substringBetween(Utils.loadConfigText("/bin/enemy.bin"), "teachMode:", "teachModeEnd"),
               "teach" + this.tutorialStep + ":",
               "teach" + this.tutorialStep + "End"
            );
            this.dialogLines = Utils.split(var23, "\r\n");
            if (this.dialogLines != null) {
               this.dialogMode = 1;
               this.startDialog(this.dialogLines);
            }

            this.resetActionPlans();
            this.buildTurnOrder();
            this.targetCursor = this.firstAliveSlot(10, 20);
            this.turnIndex = 0;
         } else {
            this.resetActionPlans();
            this.buildTurnOrder();
            if (this.petSummoned) {
               this.petSummoned = false;
            }

            this.targetCursor = this.firstAliveSlot(10, 20);
            this.turnIndex = 0;
            if (!this.allAlliesStunned()) {
               if (this.allAlliesConfused()) {
                  byte[] var8 = this.aliveSlots(0, 10);
                  byte[] var14 = this.aliveSlots(0, this.actors.length);

                  for (int var22 = 0; var22 < var8.length; var22++) {
                     this.actionPlans[var8[var22]][1] = 1;
                     byte[] var30;
                     if ((var30 = Utils.removeValue(var14, var8[var22])).length > 1) {
                        int var31 = GameUtil.randomRange(0, var30.length);
                        this.actionPlans[var8[var22]][2] = var30[var31];
                     } else {
                        this.actionPlans[var8[var22]][2] = var30[0];
                     }
                  }

                  this.phase = 2;
                  return;
               }

               if (this.actors[0].stunTurns == 0 && this.actors[0].confuseTurns == 0) {
                  this.commandSlot = 0;
                  this.phase = 0;
                  this.menuMode = 0;
                  this.commandIndex = 0;
                  return;
               }

               byte[] var7 = Utils.removeValue(this.aliveSlots(0, 10), 0);
               if (this.controlStatus(0) == 0) {
                  if (var7.length != 1) {
                     if (this.controlStatus(var7[0]) == -1) {
                        this.commandSlot = var7[0];
                        this.phase = 0;
                        this.menuMode = 0;
                        this.commandIndex = 7;
                        return;
                     }

                     if (this.controlStatus(var7[0]) == 0 && this.controlStatus(var7[1]) == 1) {
                        byte[] var12 = this.aliveSlots(0, this.actors.length);
                        this.actionPlans[var7[1]][1] = 1;
                        byte[] var18 = Utils.removeValue(var12, var7[1]);
                        int var26 = GameUtil.randomRange(0, var18.length);
                        this.actionPlans[var7[1]][2] = var18[var26];
                        this.phase = 2;
                     } else if (this.controlStatus(var7[0]) == 1 && this.controlStatus(var7[1]) == 0) {
                        byte[] var11 = this.aliveSlots(0, this.actors.length);
                        this.actionPlans[var7[0]][1] = 1;
                        byte[] var17 = Utils.removeValue(var11, var7[0]);
                        int var25 = GameUtil.randomRange(0, var17.length);
                        this.actionPlans[var7[0]][2] = var17[var25];
                     } else if (this.controlStatus(var7[0]) == 1 && this.controlStatus(var7[1]) == 1) {
                        byte[] var10 = this.aliveSlots(0, this.actors.length);

                        for (int var16 = 0; var16 < var7.length; var16++) {
                           this.actionPlans[var7[var16]][1] = 1;
                           byte[] var24;
                           if ((var24 = Utils.removeValue(var10, var7[var16])).length > 1) {
                              int var5 = GameUtil.randomRange(0, var24.length);
                              this.actionPlans[var7[var16]][2] = var24[var5];
                           } else {
                              this.actionPlans[var7[var16]][2] = var24[0];
                           }
                        }
                     }

                     this.phase = 2;
                     return;
                  }

                  if (this.controlStatus(var7[0]) == -1) {
                     this.commandSlot = var7[0];
                     this.phase = 0;
                     this.menuMode = 0;
                     this.commandIndex = 7;
                     return;
                  }

                  if (this.controlStatus(var7[0]) != 1) {
                     return;
                  }

                  byte[] var9 = this.aliveSlots(0, this.actors.length);
                  this.actionPlans[var7[0]][1] = 1;
                  byte[] var15 = Utils.removeValue(var9, var7[0]);
                  int var4 = GameUtil.randomRange(0, var15.length);
                  this.actionPlans[var7[0]][2] = var15[var4];
               } else {
                  if (this.controlStatus(0) != 1) {
                     return;
                  }

                  byte[] var13 = this.aliveSlots(0, this.actors.length);
                  this.actionPlans[0][1] = 1;
                  byte[] var19 = Utils.removeValue(var13, 0);
                  int var27 = GameUtil.randomRange(0, var19.length);
                  this.actionPlans[0][2] = var19[var27];
                  if (var7.length == 1) {
                     if (this.controlStatus(var7[0]) == -1) {
                        this.commandSlot = var7[0];
                        this.phase = 0;
                        this.menuMode = 0;
                        this.commandIndex = 7;
                        return;
                     }

                     if (this.controlStatus(var7[0]) != 0) {
                        return;
                     }
                  } else {
                     if (this.controlStatus(var7[0]) == -1) {
                        this.commandSlot = var7[0];
                        this.phase = 0;
                        this.menuMode = 0;
                        this.commandIndex = 7;
                        return;
                     }

                     if (this.controlStatus(var7[0]) == 0 && this.controlStatus(var7[1]) == 1) {
                        this.actionPlans[var7[1]][1] = 1;
                        var19 = Utils.removeValue(var13, var7[1]);
                        var27 = GameUtil.randomRange(0, var19.length);
                        this.actionPlans[var7[1]][2] = var19[var27];
                        this.phase = 2;
                     } else if (this.controlStatus(var7[0]) == 1 && this.controlStatus(var7[1]) == 0) {
                        this.actionPlans[var7[0]][1] = 1;
                        var19 = Utils.removeValue(var13, var7[0]);
                        var27 = GameUtil.randomRange(0, var19.length);
                        this.actionPlans[var7[0]][2] = var19[var27];
                     }
                  }
               }
            }

            this.phase = 2;
         }
      } else {
         this.resetActorPoses();
         this.clearAllStatus();
         this.onDefeat();
      }
   }

   private int retargetIfDead(int var1) {
      if (this.actors[var1].isDead) {
         if (var1 > 9) {
            int[] var4 = new int[this.countAlive(10, this.actors.length)];
            int var2 = 10;

            for (int var3 = 0; var2 < this.actors.length; var2++) {
               if (this.actors[var2] != null && !this.actors[var2].isDead) {
                  var4[var3] = var2;
                  var3++;
               }
            }

            if (var4.length > 1) {
               var2 = GameUtil.randomRange(0, var4.length);
               var1 = var4[var2];
            } else {
               var1 = var4[0];
            }
         } else {
            int[] var5 = new int[this.countAlive(0, 10)];
            int var7 = 0;

            for (int var9 = 0; var7 < 10; var7++) {
               if (this.actors[var7] != null && !this.actors[var7].isDead) {
                  var5[var9] = var7;
                  var9++;
               }
            }

            if (var5.length > 1) {
               var7 = GameUtil.randomRange(0, var5.length);
               var1 = var5[var7];
            } else {
               var1 = var5[0];
            }
         }
      }

      return var1;
   }

   private boolean allEnemiesDead() {
      for (int var1 = 10; var1 < this.actors.length; var1++) {
         if (this.actors[var1] != null && this.actors[var1].state != 128) {
            return false;
         }
      }

      return true;
   }

   private static boolean hasNegativeStatus(BattleActor var0) {
      if (var0.poison != null && var0.poison[0] > 0) {
         return true;
      } else {
         return var0.stunTurns > 0 ? true : var0.confuseTurns > 0;
      }
   }

   private void applySkill(BattleActor var1, int var2, int var3) {
      BattleActor[] var4 = null;
      if (var3 <= 0) {
         var3 = 1;
      }

      if (Skill.names == null) {
         Skill.loadTable();
      }

      int var5 = GameData.findSkillIndex(var2);
      if (Skill.targetScopes[var5] == 1) {
         var4 = new BattleActor[1];
         var1.targetSlot = this.retargetIfDead(var1.targetSlot);
         var4[0] = this.actors[var1.targetSlot];
      } else {
         if (this.showBackground) {
            this.showBackground = false;
         }

         if (Skill.targetSides[var5] == 1) {
            if (var1.slot < 10) {
               var4 = new BattleActor[this.countAlive(0, 10)];
               int var6 = 0;

               for (int var7 = 0; var7 < 10; var7++) {
                  if (this.actors[var7] != null && !this.actors[var7].isDead) {
                     var4[var6] = this.actors[var7];
                     var6++;
                  }
               }
            } else {
               var4 = new BattleActor[this.countAlive(10, 20)];
               int var35 = 0;

               for (int var39 = 10; var39 < 20; var39++) {
                  if (this.actors[var39] != null && !this.actors[var39].isDead) {
                     var4[var35] = this.actors[var39];
                     var35++;
                  }
               }
            }
         } else if (Skill.targetSides[var5] == 2) {
            if (var1.slot < 10) {
               var4 = new BattleActor[this.countAlive(10, 20)];
               int var36 = 0;

               for (int var40 = 10; var40 < 20; var40++) {
                  if (this.actors[var40] != null && !this.actors[var40].isDead) {
                     var4[var36] = this.actors[var40];
                     var36++;
                  }
               }
            } else {
               var4 = new BattleActor[this.countAlive(0, 10)];
               int var37 = 0;

               for (int var41 = 0; var41 < 10; var41++) {
                  if (this.actors[var41] != null && !this.actors[var41].isDead) {
                     var4[var37] = this.actors[var41];
                     var37++;
                  }
               }
            }
         }
      }

      byte[] var38 = null;

      for (int var42 = 0; var42 < var4.length; var42++) {
         var38 = Utils.append(var38, var4[var42].slot);
      }

      var1.targets = var38;
      var1.effectSprites = null;
      String var43 = "/ft/" + GameData.skillTable[var5][3];
      if (var38 != null && var38.length != 0) {
         var1.effectSprites = new AnimSprite[var38.length];

         for (int var8 = 0; var8 < var38.length; var8++) {
            var1.effectSprites[var8] = new AnimSprite();
            var1.effectSprites[var8].load(var43);
            byte var11 = var38[var8];
            int var9 = this.actors[var11].homePos.x;
            var11 = var38[var8];
            int var10 = this.actors[var11].homePos.y;
            var1.effectSprites[var8].x = (short)var9;
            var1.effectSprites[var8].y = (short)var10;
         }
      }

      short var44 = Skill.mpCostsPerLevel[var5][var3 - 1];
      var1.mp -= var44;
      if (var1.mp < 0) {
         var1.mp = 0;
      }

      if (var2 == 1) {
         this.dealDamage(var1, var4[0], var1.attack, var4[0].defense, 0, false);
         if (!var4[0].dodged && !hasNegativeStatus(var4[0])) {
            var4[0].stunTurns = (byte)GameUtil.randomBetween(2 + var3 / 4, 3 + var3 / 4);
         }
      } else if (var2 == 2) {
         var4[0].attackBuff[0] = 3;
         var4[0].attackBuff[1] = (byte)(25 + var3 * 5);
         var4[0].attack = var4[0].stats[12] * (100 + var4[0].attackBuff[1]) / 100;
      } else if (var2 == 3) {
         for (int var45 = 0; var45 < var4.length; var45++) {
            if (var4[var45].hp > 0) {
               this.dealDamage(var1, var4[var45], var1.attack * (62 + (var3 << 3)) / 100, var4[var45].defense, 0, false);
            }
         }
      } else if (var2 == 4) {
         var1.hitsLeft = (byte)(2 + (var3 - 1) / 4);
         if (DeviceConfig.allowDebug) {
            System.out.println("mainRole.DUOCHONG " + var1.hitsLeft);
         }

         this.normalAttack(var1.slot, var4[0].slot);
      } else if (var2 == 5) {
         this.dealDamage(var1, var4[0], var1.attack * (110 + var3 * 10) / 100, var4[0].defense, -20, false);
      } else if (var2 == 6) {
         this.dealDamage(var1, var4[0], var1.attack, 0, 0, true);
         var1.defenseDebuff[0] = 1;
         var1.defenseDebuff[1] = (byte)(54 - var3 * 8 / 2);
         var1.defense = var1.defense - var1.defense * var1.defenseDebuff[1] / 100;
      } else if (var2 == 8) {
         this.dealDamage(var1, var4[0], var1.attack * (140 + var3 * 10) / 100, var4[0].defense, 0, false);
         if (!var4[0].dodged && GameUtil.randomBetween(0, 100) < 15) {
            var4[0].hp = 0;
         }
      } else if (var2 == 9) {
         for (int var46 = 0; var46 < var4.length; var46++) {
            if (var4[0].hp > 0) {
               if (rollHit(var1, var4[0], 0) && !hasNegativeStatus(var4[var46])) {
                  var4[var46].poison[0] = (byte)(2 + var3 / 2);
                  var4[var46].poison[1] = (byte)(var3 + 4);
               } else {
                  var4[var46].dodged = true;
               }
            }
         }
      } else if (var2 == 34) {
         this.dealDamage(var1, var4[0], var1.attack, var4[0].defense, 0, false);
         if (!var4[0].dodged && !hasNegativeStatus(var4[0])) {
            var4[0].confuseTurns = (byte)(2 + var3 / 2);
         }
      } else if (var2 == 10) {
         for (int var47 = 0; var47 < var4.length; var47++) {
            if (var4[var47].hp > 0) {
               if (rollHit(var1, var4[var47], 0) && !hasNegativeStatus(var4[var47])) {
                  var4[var47].confuseTurns = (byte)(2 + var3 / 2);
               } else {
                  var4[var47].dodged = true;
               }
            }
         }
      } else if (var2 == 11) {
         if (var4[0].hp > 0) {
            if (rollHit(var1, var4[0], 0) && !hasNegativeStatus(var4[0])) {
               var4[0].stunTurns = (byte)GameUtil.randomBetween(2 + var3 / 4, 3 + var3 / 4);
            } else {
               var4[0].dodged = true;
            }
         }
      } else if (var2 == 12) {
         for (int var48 = 0; var48 < var4.length; var48++) {
            if (var4[var48].hp > 0) {
               if (rollHit(var1, var4[var48], 0) && !hasNegativeStatus(var4[var48])) {
                  var4[var48].stunTurns = (byte)GameUtil.randomBetween(2 + var3 / 4, 3 + var3 / 4);
               } else {
                  var4[var48].dodged = true;
               }
            }
         }
      } else if (var2 == 13) {
         var4[0].hp = var4[0].hp + var4[0].stats[10] * (10 + var3 * 5) / 100 + var1.attack * (20 + var3 * 2) / 100;
         if (var4[0].hp > var4[0].stats[10]) {
            var4[0].hp = var4[0].stats[10];
         }
      } else if (var2 == 14) {
         for (int var49 = 0; var49 < var4.length; var49++) {
            if (var4[var49].hp > 0) {
               var4[var49].hp = var4[var49].hp + var4[var49].stats[10] * (10 + var3 * 5) / 100 + var1.attack * (20 + var3 * 2) / 100;
            }

            if (var4[var49].hp > var4[var49].stats[10]) {
               var4[var49].hp = var4[var49].stats[10];
            }
         }
      } else if (var2 == 15) {
         for (int var50 = 0; var50 < var4.length; var50++) {
            var4[var50].poison = new byte[2];
            var4[var50].stunTurns = 0;
            var4[var50].confuseTurns = 0;
            var4[var50].silenceTurns = 0;
         }
      } else if (var2 == 16) {
         if (var3 < 6) {
            int var51 = GameUtil.randomBetween(1, 5);

            for (int var67 = 0; var67 < var4.length; var67++) {
               if (var4[var67] != null && var4[var67].hp > 0 && var4[var67].silenceTurns == 0) {
                  var4[var67].silenceTurns = (byte)var51;
               }
            }

            byte[] var68;
            if (var1.slot < 10) {
               var68 = this.aliveSlots(0, 10);
            } else {
               var68 = this.aliveSlots(10, 20);
            }

            for (int var12 = 0; var12 < var68.length; var12++) {
               if (this.actors[var68[var12]] != null && this.actors[var68[var12]].hp > 0 && this.actors[var68[var12]].silenceTurns == 0) {
                  this.actors[var68[var12]].silenceTurns = (byte)var51;
               }
            }
         } else {
            int var52 = GameUtil.randomBetween(6, 10);

            for (int var69 = 0; var69 < var4.length; var69++) {
               if (var4[var69].hp > 0 && var4[var69].silenceTurns == 0) {
                  var4[var69].silenceTurns = (byte)var52;
               }
            }
         }
      } else if (var2 == 17) {
         for (int var53 = 0; var53 < var4.length; var53++) {
            if (var4[var53].hp > 0 && var4[var53].rallyBuff[0] == 0) {
               if (var3 < 6) {
                  var4[var53].rallyBuff[0] = 3;
               } else {
                  var4[var53].rallyBuff[0] = 5;
               }

               var4[var53].rallyBuff[1] = (byte)(12 + var3 * 3);
               var4[var53].attack = var4[var53].attack + var4[var53].stats[12] * var4[var53].rallyBuff[1] / 100;
               var4[var53].defense = var4[var53].defense + var4[var53].stats[13] * var4[var53].rallyBuff[1] / 100;
            }
         }
      } else if (var2 == 18) {
         if (var4[0].hp > 0) {
            if (rollHit(var1, var4[0], 0)) {
               int var54 = GameUtil.randomBetween(0, 5);
               if ((var2 = var1.attack * (125 + var3 * 5) / 100 - var4[0].defense + var54) < 10) {
                  var2 = 10;
               }

               var4[0].hp -= var2;
            } else {
               var4[0].dodged = true;
            }
         }
      } else if (var2 == 19) {
         for (int var55 = 0; var55 < var4.length; var55++) {
            if (var4[var55].hp > 0) {
               if (rollHit(var1, var4[var55], 0)) {
                  int var70 = GameUtil.randomBetween(0, 5);
                  if ((var5 = var1.attack * (145 + var3 * 5) / 100 - var4[var55].defense + var70) < 10) {
                     var5 = 10;
                  }

                  var4[var55].hp -= var5;
               } else {
                  var4[var55].dodged = true;
               }
            }
         }
      } else if (var2 == 20) {
         int var56 = 200 + elementAdvantage(new byte[]{0, 0, 0, 10}, var4[0].elements);
         int var71 = var1.attack * (115 + var3 * 5) / 100;
         if (rollHit(var1, var4[0], 0)) {
            var2 = GameUtil.randomBetween(0, 5);
            if ((var5 = var71 * var56 / 100 - var4[0].defense + var2) < 10) {
               var5 = 10;
            }

            var4[0].hp -= var5;
         } else {
            var4[0].dodged = true;
         }
      } else if (var2 == 21) {
         for (int var57 = 0; var57 < var4.length; var57++) {
            if (var4[var57].hp > 0) {
               int var72 = 200 + elementAdvantage(new byte[]{0, 0, 0, 10}, var4[var57].elements);
               var2 = var1.attack * (115 + var3 * 5) / 100;
               if (rollHit(var1, var4[var57], 0)) {
                  var5 = GameUtil.randomBetween(0, 5);
                  if ((var2 = var2 * var72 / 100 - var4[var57].defense + var5) < 10) {
                     var2 = 10;
                  }

                  var4[var57].hp -= var2;
               } else {
                  var4[var57].dodged = true;
               }
            }
         }
      } else if (var2 == 22) {
         int var58 = 200 + elementAdvantage(new byte[]{10, 0, 0, 0}, var4[0].elements);
         int var73 = var1.attack * (115 + var3 * 5) / 100;
         if (rollHit(var1, var4[0], 0)) {
            var2 = GameUtil.randomBetween(0, 5);
            if ((var5 = var73 * var58 / 100 - var4[0].defense + var2) < 10) {
               var5 = 10;
            }

            var4[0].hp -= var5;
         } else {
            var4[0].dodged = true;
         }
      } else if (var2 == 23) {
         for (int var59 = 0; var59 < var4.length; var59++) {
            if (var4[var59].hp > 0) {
               int var74 = 200 + elementAdvantage(new byte[]{10, 0, 0, 0}, var4[var59].elements);
               var2 = var1.attack * (115 + var3 * 5) / 100;
               if (rollHit(var1, var4[var59], 0)) {
                  var5 = GameUtil.randomBetween(0, 5);
                  if ((var2 = var2 * var74 / 100 - var4[var59].defense + var5) < 10) {
                     var2 = 10;
                  }

                  var4[var59].hp -= var2;
               } else {
                  var4[var59].dodged = true;
               }
            }
         }
      } else if (var2 == 24) {
         int var60 = 200 + elementAdvantage(new byte[]{0, 0, 10, 0}, var4[0].elements);
         int var75 = var1.attack * (145 + var3 * 5) / 100;
         if (rollHit(var1, var4[0], 0)) {
            var2 = GameUtil.randomBetween(0, 5);
            if ((var5 = var75 * var60 / 100 - var4[0].defense + var2) < 10) {
               var5 = 10;
            }

            var4[0].hp -= var5;
         } else {
            var4[0].dodged = true;
         }
      } else if (var2 == 25) {
         for (int var61 = 0; var61 < var4.length; var61++) {
            if (var4[var61].hp > 0) {
               int var76 = 200 + elementAdvantage(new byte[]{0, 0, 10, 0}, var4[var61].elements);
               var2 = var1.attack * (115 + var3 * 5) / 100;
               if (rollHit(var1, var4[var61], 0)) {
                  var5 = GameUtil.randomBetween(0, 5);
                  if ((var2 = var2 * var76 / 100 - var4[var61].defense + var5) < 10) {
                     var2 = 10;
                  }

                  var4[var61].hp -= var2;
               } else {
                  var4[var61].dodged = true;
               }
            }
         }
      } else if (var2 == 26) {
         int var62 = 200 + elementAdvantage(new byte[]{0, 10, 0, 0}, var4[0].elements);
         int var77 = var1.attack * (115 + var3 * 5) / 100;
         if (rollHit(var1, var4[0], 0)) {
            var2 = GameUtil.randomBetween(0, 5);
            if ((var5 = var77 * var62 / 100 - var4[0].defense + var2) < 10) {
               var5 = 10;
            }

            var4[0].hp -= var5;
         } else {
            var4[0].dodged = true;
         }
      } else if (var2 == 27) {
         for (int var63 = 0; var63 < var4.length; var63++) {
            if (var4[var63].hp > 0) {
               int var78 = 200 + elementAdvantage(new byte[]{0, 10, 0, 0}, var4[var63].elements);
               var2 = var1.attack * (145 + var3 * 5) / 100;
               if (rollHit(var1, var4[var63], 0)) {
                  var5 = GameUtil.randomBetween(0, 5);
                  if ((var2 = var2 * var78 / 100 - var4[var63].defense + var5) < 10) {
                     var2 = 10;
                  }

                  var4[var63].hp -= var2;
               } else {
                  var4[var63].dodged = true;
               }
            }
         }
      } else if (var2 == 28) {
         int var64 = var4[0].hp;
         this.dealDamage(var1, var4[0], var1.attack, var4[0].defense, 0, false);
         if (!var4[0].dodged) {
            int var79;
            if ((var79 = (var64 - var4[0].hp) * (48 + (var3 << 1)) / 100) < 10) {
               var79 = 10;
            }

            var1.hp += var79;
            if (var1.hp > var1.stats[10]) {
               var1.hp = var1.stats[10];
            }
         }
      } else if (var2 == 33) {
         int[][] var65 = new int[][]{{2, 3, 5}, {2, 3, 7}, {3, 4, 8}, {4, 5, 10}, {4, 5, 12}};
         this.dealDamage(var1, var4[0], var1.attack, var4[0].defense, 0, false);
         if (!var4[0].dodged && !hasNegativeStatus(var4[0])) {
            var4[0].poison[0] = (byte)GameUtil.randomBetween(var65[var3 - 1][0], var65[var3 - 1][1]);
            var4[0].poison[1] = (byte)var65[var3 - 1][2];
         }
      }

      for (int var66 = 0; var66 < this.actors.length; var66++) {
         int var80;
         if (this.actors[var66] != null
            && this.actors[var66].isDefending
            && this.actors[var66].hp < this.actors[var66].stats[3]
            && (var80 = (this.actors[var66].stats[3] - this.actors[var66].hp) / 5) != 0) {
            this.actors[var66].hp = this.actors[var66].stats[3] - var80;
         }
      }
   }

   public final BattleActor getActor(int var1) {
      return this.actors[var1];
   }
}
