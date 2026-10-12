import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class World {
   public byte worldState;
   public byte mode;
   byte questDialogPage;
   private byte loadTargetPercent;
   public static short mapWidth;
   public static short mapHeight;
   private short cameraX;
   private short cameraY;
   private static int mapOffsetX;
   private static int mapOffsetY;
   TileMap tileMap;
   public SceneLayer sceneLayer;
   DialogBox dialogBox;
   public Battle battle;
   TreasureChest nearbyChest;
   GameMenu menu;
   ScreenWipe screenWipe;
   RoleSprite dialogSpeaker;
   private RoleSprite scratchRole;
   private RoleSprite roleBeingRemoved;
   RoleSprite nearbyNpc;
   private CloseUpAnim closeUpAnim;
   StoryCrawl storyCrawl;
   public ScriptEngine script;
   TextBlock textLayout;
   RainDrop[] rainDrops;
   public PaidServiceMenu smsMenu;
   private String pendingMapName;
   boolean loadFinished = false;
   private byte loadShownPercent;
   public static short entryPointIndex;
   private boolean isSameMapReload;
   boolean nearChest = false;
   boolean nearNpc = false;
   public String[] messageLines;
   public byte messageScroll;
   byte endingId;
   boolean isShaking;
   private byte shakeOffsetY;
   private String endingStoryText;
   private String[] endingLines;
   private int endingTextY;
   private int creditsY;
   boolean isResting;
   public byte restFrames;
   public boolean battlePending;
   private short[] battleEnemyIds;
   private short[] battleEnemyLevels;
   private short[] battleEnemyCounts;
   public byte flashCounter;
   private boolean battleFlashing;
   private boolean isRemovingRole;
   private byte removeBlinkCount;
   public String currentMapName;
   private static boolean endScreenDone;
   public boolean playerControlEnabled;
   public boolean fieldHudEnabled;
   private int enterFrames;
   private boolean isEnteringMap;
   private boolean endingTextDone;
   private Image a_Image;
   private Image debugDigitsImage;
   private Image boxCornerImage;
   short messageVisibleLines;
   boolean isScreenFlashing;
   boolean letterboxOpening;
   boolean letterboxClosing;
   int crawlSpeed = 2;
   boolean storyCrawlActive;
   private byte m_B;
   boolean isRaining;
   boolean levelUpEffect;
   private AnimSprite levelUpAnim;
   private AnimSprite enterMapAnim;
   private Image mazeGateImage1;
   private Image mazeGateImage2;
   private AnimSprite smsIconAnim;
   private AnimSprite loadingAnim;
   boolean useSecondEntry;
   private int[][] paletteFromColors;
   private int[][] paletteToColors;
   public String chosenOption;
   AnimSprite portalAnim;
   private AnimSprite questAvailableMarker;
   private AnimSprite questActiveMarker;
   private AnimSprite questDoneMarker;
   short portalX;
   short portalY;
   public boolean isDungeonBattle;
   public boolean isNormalBattle;
   public int[] dungeonLoot;
   String[] lootNotices;
   private Image goldIcon;
   private Image softKeyImage;
   private Image expIcon;
   public boolean isTutorialBattle;
   boolean isTransforming;
   private AnimSprite transformAnim;
   byte transformKind;
   short shownQuestId;
   private Image orderImage;
   public String lootPetName;
   boolean cameraReturning;
   private static int panTravelled = 0;
   private static int panOffsetX;
   private static int panOffsetY;
   private int dragStartX;
   private int dragStartY;
   private int dragCurX;
   private int dragCurY;
   private boolean tickerPaused = false;
   private long tickerPauseStart;
   private byte tickerLine = 1;
   private boolean tickerActive;
   private String[] tickerLines;
   private short tickerScroll;
   short letterboxTarget;
   private short letterboxSize = 0;
   boolean blindsClosing = false;
   boolean blindsOpening = false;
   private int blindsWidth = 0;
   private boolean cameraPanning;
   private short panSteps;
   private byte panDirection;
   private boolean whiteFlashOn;

   public World() {
      this.screenWipe = new ScreenWipe();
   }

   public final void startGame() {
      GameData.initNewGame();
      this.changeScene(GameData.startSceneId);
   }

   public final void loadSaveSlot(String var1) {
      GameCanvas.instance.titleScreen = null;
      GameData.loadGame(var1);
      this.changeScene(GameData.currentSceneId);
   }

   public final void changeScene(int var1) {
      if (GameData.loadSceneInfo(var1)) {
         if (GameData.isInDungeon && GameData.isRestoringSave) {
            if (GameData.dungeonLinkScenes != null) {
               GameData.currentSceneId = Utils.parseShort(GameData.dungeonLinkScenes[0]);
            }

            GameData.loadSceneInfo(GameData.currentSceneId);
            GameData.isRestoringSave = false;
         } else {
            GameData.currentSceneId = (short)var1;
         }

         String var2 = GameData.sceneMapName;
         this.pendingMapName = var2;
         this.beginLoading();
      }
   }

   public final void beginLoading() {
      this.loadFinished = false;
      if (this.worldState != 1) {
         this.worldState = 1;
         new SceneLoader(this);
      }

      this.shakeOffsetY = 0;
   }

   public final void loadScene() {
      DeviceConfig.h_Z = false;
      this.playerControlEnabled = false;
      this.fieldHudEnabled = false;
      this.isEnteringMap = true;
      this.enterFrames = 0;
      panOffsetX = 0;
      panOffsetY = 0;
      this.portalAnim = null;
      this.letterboxSize = 0;
      this.letterboxClosing = false;
      this.blindsClosing = false;
      this.blindsOpening = false;
      this.blindsWidth = 0;
      this.releaseResources();
      if (this.loadingAnim == null) {
         this.loadingAnim = new AnimSprite();
         this.loadingAnim.load("/av/long2.av");
      }

      this.isSameMapReload = false;
      if (this.currentMapName != null && this.currentMapName.equals(this.pendingMapName) && entryPointIndex < 0) {
         this.isSameMapReload = true;
      }

      this.loadTargetPercent = 10;
      if (this.smsIconAnim == null) {
         this.smsIconAnim = new AnimSprite();
         this.smsIconAnim.load("/av/sms.av");
      }

      if (GameData.mazeGatePoints != null && GameData.mazeGatePoints.length == 2 && this.mazeGateImage1 == null) {
         this.mazeGateImage1 = ResourceCache.loadImage("/map/pics/sdt1.gf");
         this.mazeGateImage2 = ResourceCache.loadImage("/map/pics/sdt2.gf");
      }

      this.currentMapName = this.pendingMapName;
      if (this.questAvailableMarker == null) {
         this.questAvailableMarker = new AnimSprite();
         this.questActiveMarker = new AnimSprite();
         this.questDoneMarker = new AnimSprite();
         this.questAvailableMarker.load("/pic/task/task1.av");
         this.questActiveMarker.load("/pic/task/task2.av");
         this.questDoneMarker.load("/pic/task/task3.av");
      }

      this.loadTargetPercent = 20;
      if (this.tileMap == null) {
         this.tileMap = new TileMap();
      } else {
         this.tileMap.clear();

         try {
            Thread.sleep(20L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      }

      this.sceneLayer = null;
      this.sceneLayer = new SceneLayer();
      this.sceneLayer.setPhase(GameData.storyPhase);
      this.tileMap.loadMap("/map/" + GameData.sceneMapName + ".map");
      if (DeviceConfig.modelName.equals("N7610")) {
         this.tileMap.loadTilesetBytes(null, null, "/map/pics/", 1);
      } else {
         this.tileMap.loadTilesetBytes(null, null, "/map/pics/", 0);
      }

      if (GameData.recolorImageNames != null) {
         this.recolorTilesets();
      }

      this.tileMap.createImages();
      mapWidth = this.tileMap.getMapWidth();
      mapHeight = this.tileMap.getMapHeight();
      this.loadTargetPercent = 50;
      if (GameData.recolorImageNames != null) {
         String[] var1 = null;

         for (int var2 = 0; var2 < GameData.recolorImageNames.length; var2++) {
            var1 = Utils.append(var1, GameData.recolorImageNames[var2] + ".gf");
         }

         this.loadPaletteTables();
         this.sceneLayer
            .loadBuildingsRecolored(
               "/building/s" + GameData.buildingFileId + "sp.dat",
               "/map/pics/",
               "/map/pics/",
               null,
               null,
               var1,
               this.paletteFromColors,
               this.paletteToColors,
               -1,
               -1
            );
      } else {
         this.sceneLayer.loadBuildings("/building/s" + GameData.buildingFileId + "sp.dat", "/map/pics/", "/map/pics/");
      }

      TreasureChest[] var4 = loadChests();

      for (int var7 = 0; var4 != null && var7 < var4.length; var7++) {
         this.sceneLayer.addActor(var4[var7]);
      }

      RoleSprite[] var8 = this.buildSceneRoles();

      for (int var5 = 0; var8 != null && var5 < var8.length; var5++) {
         this.sceneLayer.addActor(var8[var5]);
      }

      this.loadTargetPercent = 90;
      if (!this.isSameMapReload) {
         this.script = new ScriptEngine();
         this.script.loadEvents("/bin/s" + GameData.currentSceneId + "e.txt", GameData.storyPhase);
      }

      this.sceneLayer.setOccurredEvents(GameData.eventFlags);
      this.resetSceneNpcs();
      if (GameData.party[0] != null) {
         this.cameraX = (short)(GameData.party[0].x - GameCanvas.instance.getWidth() / 2);
         this.cameraY = (short)(GameData.party[0].y - GameCanvas.instance.getHeight() / 2);
      }

      if (this.cameraX < 0) {
         this.cameraX = 0;
      } else if (this.cameraX > mapWidth - GameCanvas.instance.getWidth()) {
         this.cameraX = (short)(mapWidth - GameCanvas.instance.getWidth());
         if (this.cameraX < 0) {
            this.cameraX = 0;
         }
      }

      if (this.cameraY < 0) {
         this.cameraY = 0;
      } else if (this.cameraY > mapHeight - GameCanvas.instance.getHeight()) {
         this.cameraY = (short)(mapHeight - GameCanvas.instance.getHeight());
         if (this.cameraY < 0) {
            this.cameraY = 0;
         }
      }

      entryPointIndex = -1;
      boolean var6 = false;
      int var9 = Integer.MAX_VALUE / ScreenWipe.typeCount;
      int var10 = Math.abs(GameCanvas.random.nextInt()) / var9;
      this.screenWipe.setType(var10);
      this.screenWipe.begin();
      if (DeviceConfig.debugMode) {
         System.out.println("Tải xong");
      }

      this.loadTargetPercent = 100;
      if (GameData.party[0] != null) {
         GameData.party[0].isFlying = false;
         GameData.party[0].isRiding = false;
         GameData.party[0].noClip = false;
      }

      playSceneMusic();
      this.script.advance(0);
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         GameUtil.freeImageCaches();
      }

      this.battleFlashing = false;
      this.whiteFlashOn = false;
   }

   private RoleSprite[] buildSceneRoles() {
      GameData.sceneNpcs = null;
      RoleSprite[] var1 = null;
      short var2 = GameData.currentSceneId;
      byte var3 = GameData.storyPhase;
      Vector var4 = new Vector();
      if (GameData.sceneChangeText == null) {
         GameData.sceneChangeText = Utils.loadConfigText("/bin/sceneChange.bin");
      }

      if (GameData.currentSceneId != 1 || GameData.party[0].isFlying || GameData.party[0].isRiding) {
         GameData.party[0].renderMode = 1;
         GameData.party[0].animSprite = new AnimSprite();
         GameData.party[0].animSprite.load(GameData.roleAnimPaths[GameData.party[0].roleId - 1]);
         if (GameData.party[0].isFlying) {
            System.out.println("Đáp xuống");
            GameData.party[0].land();
         }
      }

      if (GameData.currentSceneId == 1) {
         GameCanvas.instance.world.fieldHudEnabled = false;
         GameData.party[0].isVisible = false;
         GameCanvas.instance.world.transformKind = 2;
         FieldInput.applyTransform(2);
      }

      if (!this.isSameMapReload) {
         GameData.party[0].mapWidth = mapWidth;
         GameData.party[0].mapHeight = mapHeight;
         RoleSaveRecord var5;
         if ((var5 = GameData.findRoleRecord(GameData.party[0].roleId)) != null && GameData.isRestoringSave) {
            if (GameData.isInDungeon) {
               if (GameData.mazeEntryPoints != null) {
                  int[] var19 = Utils.parseIntArray(GameData.mazeEntryPoints[0], ",");
                  GameData.party[0].setPosition(var19[0], var19[1]);
                  GameData.party[0].setDirection(var19[2]);
               }
            } else {
               GameData.party[0].setPosition(var5.x, var5.y);
               GameData.party[0].setDirection(var5.direction);
            }

            GameData.isRestoringSave = false;
         } else if (GameData.mazeEntryPoints != null) {
            if (GameData.mazeEntryPoints.length < 2) {
               System.out.println("Tọa độ bản đồ ngẫu nhiên không đủ‚chỉ có một điểm dịch chuyển");
            }

            int[] var6;
            if (this.useSecondEntry) {
               var6 = Utils.parseIntArray(GameData.mazeEntryPoints[1], ",");
               this.useSecondEntry = false;
            } else {
               var6 = Utils.parseIntArray(GameData.mazeEntryPoints[0], ",");
            }

            GameData.party[0].setPosition(var6[0], var6[1]);
            GameData.party[0].setDirection(var6[2]);
         } else {
            short[][] var18 = Utils.getShortTable(GameData.sceneChangeText, "S" + var2 + "P:", "S" + var2 + "PEnd", "=");
            byte var7 = Utils.getByte(GameData.sceneChangeText, "S" + var2 + "DP");
            if (var18 != null) {
               if (GameData.isInDungeon) {
                  GameData.party[0].setPosition(var18[var7 - 1][0], var18[var7 - 1][1]);
                  GameData.party[0].setDirection(var18[var7 - 1][2]);
               } else if (var18.length > 1) {
                  if (entryPointIndex > 0) {
                     if (entryPointIndex <= var18.length) {
                        GameData.party[0].setPosition(var18[entryPointIndex - 1][0], var18[entryPointIndex - 1][1]);
                        GameData.party[0].setDirection(var18[entryPointIndex - 1][2]);
                     } else {
                        GameData.party[0].setPosition(var18[var7 - 1][0], var18[var7 - 1][1]);
                        GameData.party[0].setDirection(var18[var7 - 1][2]);
                     }
                  } else {
                     GameData.party[0].setPosition(var18[var7 - 1][0], var18[var7 - 1][1]);
                     GameData.party[0].setDirection(var18[var7 - 1][2]);
                  }
               } else {
                  GameData.party[0].setPosition(var18[0][0], var18[0][1]);
                  GameData.party[0].setDirection(var18[0][2]);
               }
            } else {
               System.out.println("Không lấy được tọa độ trong cảnh của nhân vật");
            }
         }

         for (int var20 = 0; var20 < GameData.party.length; var20++) {
            var4.addElement(GameData.party[var20]);
         }

         if (GameData.party[0].isRiding) {
            GameData.party[0].moveSpeed = 5;
         } else {
            GameData.party[0].moveSpeed = 4;
         }

         for (int var21 = 0; var21 < GameData.party.length; var21++) {
            if (GameData.isInDungeon) {
               GameData.party[var21].isVisible = false;
            } else {
               GameData.party[var21].isVisible = true;
            }
         }

         GameData.party[0].setIdleAnim();
         GameData.party[0].initFollowTrail();
         GameData.placeFollowers();
      } else {
         for (int var15 = 0; var15 < GameData.party.length; var15++) {
            var4.addElement(GameData.party[var15]);
         }

         GameData.party[0].setDirection(RoleSprite.lastMoveDirection);
      }

      if (GameData.dungeonBosses != null && GameData.currentSceneId == 67) {
         RoleSprite var16 = new RoleSprite(GameUtil.loadBattleAnimation(GameData.dungeonBosses[0][0], false));
         short var22 = GameData.dungeonBosses[0][0];
         var16.animSprite.setAction((var22 - 1) * 3);
         var16.roleId = GameData.dungeonBosses[0][0];
         var16.kind = 1;
         var16.actorType = 2;
         var16.name = "???";
         var16.setPosition(GameData.dungeonBossPos[0], GameData.dungeonBossPos[1]);
         var16.mapWidth = mapWidth;
         var16.mapHeight = mapHeight;
         var16.moveSpeed = 1;
         var16.canWander = false;
         var16.isAnimating = true;
         var16.setIdleAnim();
         var4.addElement(var16);
      }

      String var17 = Utils.loadConfigText("/bin/s" + var2 + "r.bin");
      boolean var23 = false;
      if (var17 != null) {
         var23 = true;
      }

      if (var23) {
         short[] var27 = Utils.getShortArray(var17, "roles");

         for (int var14 = 0; var14 < var27.length; var14++) {
            String var24;
            if ((var24 = Utils.substringBetween(var17, "role" + var27[var14] + ":", "role" + var27[var14] + "End")) != null) {
               byte var8 = Utils.getByte(var24, "amount");
               short var9 = var27[var14];

               for (int var10 = 0; var10 < var8; var10++) {
                  String var11;
                  byte[] var12 = Utils.getByteArray(var11 = Utils.substringBetween(var24, "group" + (var10 + 1) + ":", "group" + (var10 + 1) + "End"), "phase");
                  short[] var13 = Utils.getShortArray(var11, "pos");
                  byte var28 = Utils.getByte(var11, "walk");
                  if (var12 != null && Utils.contains(var12, var3) || var12 == null) {
                     if (!Utils.contains(GameData.partyRoleIds, var9) && !Utils.contains(GameData.removedRoleIds, var9)) {
                        RoleSprite var25;
                        if (!GameData.roleAnimPaths[var9 - 1].toLowerCase().endsWith(".gf") && !GameData.roleAnimPaths[var9 - 1].toLowerCase().endsWith(".png")
                           )
                         {
                           AnimSprite var26;
                           (var26 = new AnimSprite()).load(GameData.roleAnimPaths[var9 - 1]);
                           var25 = new RoleSprite(var26);
                        } else {
                           (var25 = new RoleSprite(ResourceCache.loadImage(GameData.roleAnimPaths[var9 - 1]))).getCollisionBoxes(1);
                        }

                        var25.roleId = var9;
                        var25.kind = 1;
                        var25.actorType = 2;
                        var25.name = GameData.roleNames[var9 - 1];
                        if (Math.abs(GameData.party[0].x - var13[0]) < 10 && Math.abs(GameData.party[0].y - var13[1]) < 10) {
                           if (var13[0] <= GameData.party[0].x) {
                              var13[0] = (short)(var13[0] - 10);
                           } else {
                              var13[0] = (short)(var13[0] + 10);
                           }
                        }

                        var25.setPosition(var13[0], var13[1]);
                        var25.mapWidth = mapWidth;
                        var25.mapHeight = mapHeight;
                        var25.setDirection(var13[2]);
                        var25.moveSpeed = 1;
                        if (var28 == 1) {
                           var25.canWander = true;
                        } else {
                           var25.canWander = false;
                        }

                        var25.isAnimating = true;
                        var4.addElement(var25);
                        GameData.addSceneNpc(var25);
                        var25.setIdleAnim();
                     } else if (DeviceConfig.debugMode) {
                        System.out.println("Tinh linh " + var9 + " Đang ở trong đội‚không thêm vào cảnh");
                     }
                     break;
                  }
               }
            }
         }
      }

      if (var4.size() > 0) {
         var1 = new RoleSprite[var4.size()];
         var4.copyInto(var1);
      }

      if (var1 == null) {
         System.out.println("returnRoles tức nhóm đội ngũ đang trống");
      }

      return var1;
   }

   private static TreasureChest[] loadChests() {
      TreasureChest[] var0 = null;
      short var1 = GameData.currentSceneId;
      byte var2 = GameData.storyPhase;
      String[][] var3 = Utils.getStringTable(Utils.loadConfigText("/bin/box.bin"), "box:", "boxEnd", null, "\t");
      String[][] var4 = null;

      for (int var5 = 0; var3 != null && var5 < var3.length; var5++) {
         if (Utils.parseShort(var3[var5][0]) == var1 || Utils.parseByte(var3[var5][2]) == var2) {
            var4 = GameUtil.append(var4, var3[var5]);
         }
      }

      if (var4 == null) {
         return null;
      } else {
         Vector var23 = new Vector();

         for (int var19 = 0; var19 < var4.length; var19++) {
            short var20 = Utils.parseShort(var4[var19][1]);
            byte var6 = Utils.parseByte(var4[var19][3]);
            short[] var7 = Utils.parseShortArray(var4[var19][4], ",");
            boolean var8 = Utils.parseByte(var4[var19][5]) == 1;
            byte var9 = Utils.parseByte(var4[var19][6]);
            short var10 = Utils.parseShort(var4[var19][7]);
            String[] var15 = null;
            if (!var4[var19][8].equals("0")) {
               var15 = Utils.split(var4[var19][8], "|");
            }

            short[][] var11 = null;

            for (int var12 = 0; var15 != null && var12 < var15.length; var12++) {
               short[] var13 = Utils.parseShortArray(var15[var12], ",");
               var11 = Utils.append(var11, var13);
            }

            String[] var16 = null;
            if (!var4[var19][9].equals("0")) {
               var16 = Utils.split(var4[var19][9], "|");
            }

            short[][] var24 = null;

            for (int var25 = 0; var16 != null && var25 < var16.length; var25++) {
               short[] var14 = Utils.parseShortArray(var16[var25], ",");
               var24 = Utils.append(var24, var14);
            }

            TreasureChest var26 = null;
            if (var8) {
               if (var6 == 0) {
                  AnimSprite var17;
                  (var17 = new AnimSprite()).load("/av/box.av");
                  var26 = new TreasureChest(var17);
               } else if (var6 == 1) {
                  AnimSprite var18;
                  (var18 = new AnimSprite()).load("/av/box1.av");
                  var26 = new TreasureChest(var18);
               }
            } else {
               var26 = new TreasureChest();
            }

            var26.chestKind = var6;
            var26.isVisible = var8;
            var26.chestId = var20;
            var26.setColumn6(var9);
            if (Utils.contains(GameData.openedChestIds, var20)) {
               byte var21 = 1;
               var26.openFrame = (byte)var21;
            } else {
               byte var22 = 0;
               var26.openFrame = (byte)var22;
               var26.gold = var10;
               var26.items = var11;
               var26.equips = var24;
            }

            var26.setPosition(var7[0], var7[1]);
            var26.kind = 12;
            var23.addElement(var26);
         }

         if (var23.size() > 0) {
            var0 = new TreasureChest[var23.size()];
            var23.copyInto(var0);
         }

         return var0;
      }
   }

   public final void updateAndDraw(Graphics var1) {
      if (this.worldState == 2) {
         label691: {
            if (!DeviceConfig.h_Z) {
               if (GameData.party[0] == null) {
                  break label691;
               }

               short var3 = (short)(GameData.party[0].x - GameCanvas.instance.getWidth() / 2);
               short var4 = (short)(GameData.party[0].y - GameCanvas.instance.getHeight() / 2);
               if (var3 < 0) {
                  var3 = 0;
               } else if (var3 > mapWidth - GameCanvas.instance.getWidth() && (var3 = (short)(mapWidth - GameCanvas.instance.getWidth())) < 0) {
                  var3 = 0;
               }

               if (var4 < 0) {
                  var4 = 0;
               } else if (var4 > mapHeight - GameCanvas.instance.getHeight() && (var4 = (short)(mapHeight - GameCanvas.instance.getHeight())) < 0) {
                  var4 = 0;
               }

               if (this.cameraX < var3 && this.cameraX < mapWidth - GameCanvas.instance.getWidth()) {
                  this.cameraX = (short)(this.cameraX + 6);
                  if (this.cameraX > var3) {
                     this.cameraX = var3;
                  }
               } else if (this.cameraX > var3 && this.cameraX > 0) {
                  this.cameraX = (short)(this.cameraX - 6);
                  if (this.cameraX < var3) {
                     this.cameraX = var3;
                  }
               }

               if (this.cameraY < var4 && this.cameraY < mapHeight - GameCanvas.instance.getHeight()) {
                  this.cameraY = (short)(this.cameraY + 6);
                  if (this.cameraY > var4) {
                     this.cameraY = var4;
                  }
               } else if (this.cameraY > var4 && this.cameraY > 0) {
                  this.cameraY = (short)(this.cameraY - 6);
                  if (this.cameraY < var4) {
                     this.cameraY = var4;
                  }
               }
            }

            int var5 = GameCanvas.instance.getWidth() <= mapWidth ? GameCanvas.instance.getWidth() : mapWidth;
            int var6 = GameCanvas.instance.getHeight() <= mapHeight ? GameCanvas.instance.getHeight() : mapHeight;
            if (this.sceneLayer != null) {
               this.sceneLayer.setViewport(this.cameraX, this.cameraY, var5, var6);
            }

            if (this.tileMap != null) {
               this.tileMap.setViewport(this.cameraX, this.cameraY, var5, var6);
            }
         }

         if (this.mode != 1) {
            if ((this.mode == 2 || this.mode == 3) && GameCanvas.instance.frameCounter % 3 == 0) {
               this.sceneLayer.animateBuildings();
            }
         } else {
            if (!this.script.isExecuting) {
               if (!this.script.isRunning && this.mode == 1 && GameData.party[0] != null) {
                  this.nearbyNpc = null;
                  RoleSprite var10001;
                  if (GameData.party[0] != null && this.sceneLayer != null) {
                     if (this.scratchRole == null) {
                        this.scratchRole = new RoleSprite();
                     }

                     var10001 = RoleSprite.findTalkableNear(GameData.party[0]);
                  } else {
                     var10001 = null;
                  }

                  this.nearbyNpc = var10001;
                  if (this.nearbyNpc != null) {
                     this.nearNpc = true;
                  } else {
                     this.nearNpc = false;
                  }
               }

               if (!this.script.isRunning && this.mode == 1 && GameData.party[0] != null && !GameData.party[0].isFlying) {
                  TreasureChest var35;
                  if (GameData.party[0] != null && this.sceneLayer != null) {
                     if (this.scratchRole == null) {
                        this.scratchRole = new RoleSprite();
                     }

                     var35 = RoleSprite.findChestAhead(GameData.party[0]);
                  } else {
                     var35 = null;
                  }

                  this.nearbyChest = var35;
                  if (this.nearbyChest != null) {
                     this.nearChest = true;
                  } else {
                     this.nearChest = false;
                  }
               }

               this.script.checkAutoEvents(GameData.party[0].getCollisionBoxes(1));
               ScriptEngine var10000 = this.script;
               short[] var15 = GameData.party[0].getCollisionBoxes(1);
               ScriptEngine var2 = var10000;
               if (!var10000.isRunning && var15 != null) {
                  boolean var21 = false;

                  for (int var23 = 0; var2.events != null && var23 < var2.events.length; var23++) {
                     if (Utils.rectsOverlap(
                           var2.events[var23].area[0],
                           var2.events[var23].area[1],
                           var2.events[var23].area[2],
                           var2.events[var23].area[3],
                           var15[0],
                           var15[1],
                           var15[2],
                           var15[3]
                        )
                        && !var2.events[var23].isAuto
                        && !GameData.hasEventFlag(var2.events[var23].id)) {
                        var2.hasManualEvent = true;
                        var2.manualEvent = var2.events[var23];
                        var21 = true;
                        break;
                     }
                  }

                  if (!var21) {
                     var2.hasManualEvent = false;
                     var2.manualEvent = null;
                  }
               }
            }

            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sceneLayer.animateBuildings();
            }

            World var10 = this;

            for (int var16 = 0; var10.sceneLayer != null && var10.sceneLayer.actors != null && var16 < var10.sceneLayer.actors.length; var16++) {
               if (var10.sceneLayer.actors[var16] != null
                  && var10.sceneLayer.actors[var16].kind == 1
                  && ((RoleSprite)var10.sceneLayer.actors[var16]).isMovingToTarget) {
                  ((RoleSprite)var10.sceneLayer.actors[var16]).updateMoveTo();
               }
            }

            var10 = this;

            for (int var17 = 0; var10.sceneLayer != null && var10.sceneLayer.actors != null && var17 < var10.sceneLayer.actors.length; var17++) {
               if (var10.sceneLayer.actors[var17] != null
                  && var10.sceneLayer.actors[var17].kind == 1
                  && ((RoleSprite)var10.sceneLayer.actors[var17]).isPlayingAction) {
                  ((RoleSprite)var10.sceneLayer.actors[var17]).updateScriptAction();
               }
            }

            if (this.cameraPanning) {
               boolean var18 = false;
               if (this.panDirection == 0) {
                  if (mapOffsetY <= 0) {
                     var18 = true;
                  }

                  if (this.cameraY + DeviceConfig.screenSize[1] >= mapHeight) {
                     var18 = false;
                  }
               } else if (this.panDirection == 1) {
                  if (mapOffsetY <= 0) {
                     var18 = true;
                  }

                  if (this.cameraY <= 0) {
                     var18 = false;
                  }
               } else if (this.panDirection == 2) {
                  if (mapOffsetX <= 0) {
                     var18 = true;
                  }

                  if (this.cameraX <= 0) {
                     var18 = false;
                  }
               } else if (this.panDirection == 3) {
                  if (mapOffsetX <= 0) {
                     var18 = true;
                  }

                  if (this.cameraX + DeviceConfig.screenSize[0] >= mapWidth) {
                     var18 = false;
                  }
               }

               if (var18) {
                  DeviceConfig.h_Z = true;
                  if (this.panSteps * DeviceConfig.f_B > panTravelled) {
                     byte var24 = DeviceConfig.f_B;
                     switch (this.panDirection) {
                        case 0:
                           this.cameraY = (short)(this.cameraY + var24);
                           panTravelled += var24;
                           panOffsetY += var24;
                           break;
                        case 1:
                           this.cameraY = (short)(this.cameraY - var24);
                           panTravelled += var24;
                           panOffsetY -= var24;
                           break;
                        case 2:
                           this.cameraX = (short)(this.cameraX - var24);
                           panTravelled += var24;
                           panOffsetX -= var24;
                           break;
                        case 3:
                           this.cameraX = (short)(this.cameraX + var24);
                           panTravelled += var24;
                           panOffsetX += var24;
                     }

                     if (this.cameraY < 0) {
                        panOffsetY = panOffsetY - this.cameraY;
                        this.cameraY = 0;
                     } else if (mapHeight > DeviceConfig.screenSize[1] && this.cameraY + DeviceConfig.screenSize[1] > mapHeight) {
                        panOffsetY = panOffsetY - (this.cameraY + DeviceConfig.screenSize[1] - mapHeight);
                        this.cameraY = (short)(mapHeight - DeviceConfig.screenSize[1]);
                     }

                     if (this.cameraX < 0) {
                        panOffsetX = panOffsetX - this.cameraX;
                        this.cameraX = 0;
                     } else if (mapWidth > DeviceConfig.screenSize[0] && this.cameraX + DeviceConfig.screenSize[0] > mapWidth) {
                        panOffsetX = panOffsetX - (this.cameraX + DeviceConfig.screenSize[0] - mapWidth);
                        this.cameraX = (short)(mapWidth - DeviceConfig.screenSize[0]);
                     }
                  } else {
                     this.cameraPanning = false;
                     panTravelled = 0;
                     this.script.advance(5);
                  }
               } else {
                  this.cameraPanning = false;
                  panTravelled = 0;
                  this.script.advance(5);
               }
            }

            if (this.cameraReturning) {
               if (panOffsetX > 0) {
                  if (panOffsetX < DeviceConfig.f_B) {
                     panOffsetX = panOffsetX - panOffsetX;
                     this.cameraX = (short)(this.cameraX - panOffsetX);
                  } else {
                     panOffsetX = panOffsetX - DeviceConfig.f_B;
                     this.cameraX = (short)(this.cameraX - DeviceConfig.f_B);
                  }
               } else if (panOffsetX < 0) {
                  if (panOffsetX - DeviceConfig.f_B > 0) {
                     panOffsetX = panOffsetX - panOffsetX;
                     this.cameraX = (short)(this.cameraX - panOffsetX);
                  } else {
                     panOffsetX = panOffsetX + DeviceConfig.f_B;
                     this.cameraX = (short)(this.cameraX + DeviceConfig.f_B);
                  }
               }

               if (panOffsetY > 0) {
                  if (panOffsetY < DeviceConfig.f_B) {
                     panOffsetY = panOffsetY - panOffsetY;
                     this.cameraY = (short)(this.cameraY - panOffsetY);
                  } else {
                     panOffsetY = panOffsetY - DeviceConfig.f_B;
                     this.cameraY = (short)(this.cameraY - DeviceConfig.f_B);
                  }
               } else if (panOffsetY < 0) {
                  if (panOffsetY - DeviceConfig.f_B > 0) {
                     panOffsetY = panOffsetY - panOffsetY;
                     this.cameraY = (short)(this.cameraY - panOffsetY);
                  } else {
                     panOffsetY = panOffsetY + DeviceConfig.f_B;
                     this.cameraY = (short)(this.cameraY + DeviceConfig.f_B);
                  }
               }

               if (panOffsetX == 0 && panOffsetY == 0) {
                  this.cameraReturning = false;
                  DeviceConfig.h_Z = false;
                  this.script.advance(5);
               }
            }

            var10 = this;

            for (int var19 = 0; var10.sceneLayer != null && var10.sceneLayer.actors != null && var19 < var10.sceneLayer.actors.length; var19++) {
               if (var10.sceneLayer.actors[var19] != null
                  && var10.sceneLayer.actors[var19].kind == 1
                  && ((RoleSprite)var10.sceneLayer.actors[var19]).actorType == 2
                  && ((RoleSprite)var10.sceneLayer.actors[var19]).isWandering
                  && !((RoleSprite)var10.sceneLayer.actors[var19]).isMovingToTarget
                  && ((RoleSprite)var10.sceneLayer.actors[var19]).isVisible) {
                  ((RoleSprite)var10.sceneLayer.actors[var19]).updateWander();
               }
            }
         }

         if (GameCanvas.instance.frameCounter % 4 == 0) {
            if (this.battle == null && !this.script.isRunning && GameData.activeQuestIds != null) {
               for (int var20 = 0; var20 < GameData.activeQuestIds.length; var20++) {
                  if (!GameData.activeQuests[var20].isCompleted) {
                     Quest var22;
                     boolean var34;
                     label580: {
                        Quest var28 = var22 = GameData.activeQuests[var20];
                        boolean var13 = false;
                        if (var28.conditions != null) {
                           String[] var25 = Utils.split(var28.conditions, "|");

                           for (int var29 = 0; var29 < var25.length; var29++) {
                              String[] var7;
                              if ((var7 = Utils.split(var25[var29], ",")) != null) {
                                 if (var7[0].equals("有装备")) {
                                    Utils.parseInt(var7[1]);
                                    if (!GameData.hasEquip(Utils.parseInt(var7[2]))) {
                                       var34 = false;
                                       break label580;
                                    }

                                    var13 = true;
                                 } else if (var7[0].equals("道具数量")) {
                                    if (GameData.getItemCount(Utils.parseInt(var7[1])) < Utils.parseInt(var7[2])) {
                                       var34 = false;
                                       break label580;
                                    }

                                    var13 = true;
                                 } else if (var7[0].equals("有宠物")) {
                                    if (!GameData.hasPetSpecies(Utils.parseInt(var7[1]))) {
                                       var34 = false;
                                       break label580;
                                    }

                                    var13 = true;
                                 } else if (var7[0].equals("有技能")) {
                                    if (!GameData.hasSkill(GameData.party[0], Utils.parseInt(var7[1]))) {
                                       var34 = false;
                                       break label580;
                                    }

                                    var13 = true;
                                 } else if (var7[0].equals("已发生事件")) {
                                    if (!Utils.contains(GameData.eventFlags, Utils.parseInt(var7[1]))) {
                                       var34 = false;
                                       break label580;
                                    }

                                    var13 = true;
                                 }
                              }
                           }
                        }

                        var34 = var13;
                     }

                     if (var34) {
                        String var30 = var22.completionCost;
                        if (!var22.completionCost.equals("-1")) {
                           String[] var26 = Utils.split(var30, "|");

                           for (int var31 = 0; var31 < var26.length; var31++) {
                              String[] var32;
                              PortalProtocol.dumpArray(var32 = Utils.split(var26[var31], ","), "tmp");
                              if (var32 != null) {
                                 if (var32[0].equals("扣除道具")) {
                                    GameData.removeItem(Utils.parseInt(var32[1]), Utils.parseInt(var32[2]));
                                 } else if (var32[0].equals("扣除装备")) {
                                    GameData.removeEquip(Utils.parseInt(var32[1]));
                                 } else if (var32[0].equals("移除宠物")) {
                                    int var14 = Utils.parseInt(var32[1]);

                                    for (int var33 = 0; GameData.myPets != null && var33 < GameData.myPets.length; var33++) {
                                       if (GameData.myPets[var33].petId == var14) {
                                          GameData.removePet(GameData.myPets[var33].uid);
                                          break;
                                       }
                                    }
                                 }
                              }
                           }
                        }

                        var22.isCompleted = true;

                        for (int var27 = 0; GameData.activeQuestIds != null && var27 < GameData.activeQuestIds.length; var27++) {
                           if (GameData.activeQuestIds[var27] == var22.id) {
                              GameData.questDoneFlags[var27] = 1;
                              break;
                           }
                        }

                        GameCanvas.instance.showToast("“ " + var22.name + " \" hoàn thành!", 10);
                     }
                  }
               }
            }

            this.script.step();
         }

         if (GameCanvas.instance.frameCounter % 3 == 0) {
            if (this.letterboxOpening) {
               if (this.letterboxSize < this.letterboxTarget) {
                  this.letterboxSize = (short)(this.letterboxSize + 2);
               } else {
                  this.letterboxOpening = false;
                  this.script.advance(5);
               }
            }

            if (this.letterboxClosing) {
               if (this.letterboxSize > 0) {
                  this.letterboxSize--;
               } else {
                  this.letterboxClosing = false;
                  this.script.advance(5);
               }
            }

            if (this.blindsClosing) {
               if (this.blindsWidth < DeviceConfig.screenSize[0] / 6) {
                  this.blindsWidth += 8;
               } else {
                  this.blindsClosing = false;
                  this.script.advance(5);
               }
            }

            if (this.blindsOpening) {
               if (this.blindsWidth > 0) {
                  this.blindsWidth -= 4;
               } else {
                  this.blindsOpening = false;
                  this.script.advance(5);
               }
            }

            if (this.isScreenFlashing) {
               this.whiteFlashOn = !this.whiteFlashOn;
               this.flashCounter--;
               if (this.flashCounter <= 0) {
                  this.isScreenFlashing = false;
                  this.whiteFlashOn = false;
                  this.script.advance(5);
               }
            }

            if (this.m_B == 0) {
               this.m_B = 1;
            } else {
               this.m_B = 0;
            }
         }

         if (GameCanvas.instance.frameCounter % 2 == 0 && this.battleFlashing) {
            this.playerControlEnabled = false;
            this.whiteFlashOn = !this.whiteFlashOn;
            this.flashCounter--;
            if (this.flashCounter <= 0) {
               this.battleFlashing = false;
               this.whiteFlashOn = false;
               this.battle = null;
               if (this.script.isExecuting) {
                  this.playerControlEnabled = false;
               } else {
                  this.playerControlEnabled = true;
               }

               if (!DeviceConfig.modelName.equals("N73")
                  && !DeviceConfig.modelName.equals("N7610")
                  && !DeviceConfig.modelName.equals("N5800")
                  && !DeviceConfig.modelName.equals("E62")
                  && !DeviceConfig.modelName.equals("W958C")
                  && DeviceConfig.unloadBeforeBattle) {
                  this.mode = 0;
                  this.tileMap.releaseImages();
                  this.sceneLayer.clearBuildings();
                  GameUtil.freeImageCaches();
                  this.releaseResources();
               }

               if (GameData.party[0].isFlying) {
                  GameData.party[0].land();
               }

               try {
                  Thread.sleep(0L);
               } catch (InterruptedException var9) {
                  var9.printStackTrace();
               }

               this.battle = new Battle();
               this.battle.enemyIds = this.battleEnemyIds;
               this.battle.enemyLevels = this.battleEnemyLevels;
               this.battle.enemyVisible = this.battleEnemyCounts;
               this.battle.party = GameData.buildBattleParty();
               this.battle.petRoster = null;
               this.battle.petRoster = GameData.getReservePets();
               if (this.battleEnemyIds != null) {
                  this.battle.isStoryBattle = true;
               }

               this.battle.phase = 28;
               if (this.battle.hasEnemySource()) {
                  this.mode = 5;
                  this.battle.init();
               } else {
                  this.battle = null;

                  try {
                     Thread.sleep(200L);
                  } catch (InterruptedException var8) {
                     var8.printStackTrace();
                  }
               }

               this.battlePending = false;
            }
         }

         if (this.isShaking) {
            if (GameCanvas.instance.frameCounter % 4 == 0) {
               this.shakeOffsetY = 4;
            } else {
               this.shakeOffsetY = 0;
            }
         } else {
            this.shakeOffsetY = 0;
         }

         if (this.isRemovingRole && GameCanvas.instance.frameCounter % 2 == 0) {
            this.removeBlinkCount--;
            if (this.removeBlinkCount > 0) {
               this.roleBeingRemoved.isVisible = !this.roleBeingRemoved.isVisible;
            } else {
               this.roleBeingRemoved.isVisible = false;
               if (this.sceneLayer != null) {
                  this.sceneLayer.removeActor(this.roleBeingRemoved);
               }

               this.roleBeingRemoved = null;
               this.isRemovingRole = false;
               this.script.advance(0);
            }
         }
      }

      if (this.worldState == 2 && this.mode == 1 && this.playerControlEnabled) {
         if (Utils.contains(GameCanvas.instance.a_AI, 2)) {
            GameData.party[0].setDirection(2);
            GameData.party[0].stepForward();
         } else if (Utils.contains(GameCanvas.instance.a_AI, 5)) {
            GameData.party[0].setDirection(3);
            GameData.party[0].stepForward();
         } else if (Utils.contains(GameCanvas.instance.a_AI, 1)) {
            GameData.party[0].setDirection(1);
            GameData.party[0].stepForward();
         } else if (Utils.contains(GameCanvas.instance.a_AI, 6)) {
            GameData.party[0].setDirection(0);
            GameData.party[0].stepForward();
         } else if (!GameData.party[0].isFlying) {
            if (GameData.party[0].roleId == 1 || GameData.party[0].roleId == 2 || GameData.party[0].roleId == 3) {
               GameData.party[0].setIdleAnim();
            }
         } else {
            if (GameData.party[0].animSprite.getAction() == 4) {
               GameData.party[0].animSprite.setAction(0);
            } else if (GameData.party[0].animSprite.getAction() == 5) {
               GameData.party[0].animSprite.setAction(1);
            } else if (GameData.party[0].animSprite.getAction() == 6) {
               GameData.party[0].animSprite.setAction(2);
            } else if (GameData.party[0].animSprite.getAction() == 7) {
               GameData.party[0].animSprite.setAction(3);
            }

            if (GameCanvas.instance.frameCounter % 2 == 0 && GameData.party[0].isRiding) {
               GameData.party[0].animSprite.setFrame(0);
            }
         }
      }

      this.drawWorld(var1);
   }

   public final void reviveRole(int var1) {
      GameData.removedRoleIds = Utils.removeValue(GameData.removedRoleIds, var1);
      this.script.advance(0);
   }

   public final void triggerRandomEncounter(boolean var1) {
      this.beginBattle(null, null, null);
   }

   public final RoleSprite findRoleById(int var1) {
      if (GameData.party[0] != null && this.sceneLayer != null) {
         if (this.scratchRole == null) {
            this.scratchRole = new RoleSprite();
         }

         return RoleSprite.findRoleOnMap(var1);
      } else {
         return null;
      }
   }

   private void drawWorld(Graphics var1) {
      if (this.worldState == 1) {
         TouchSoftKeys.setSoftKeys(0);
         var1.setColor(0);
         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         var1.setColor(16777113);
         if (GameData.loadingTips != null) {
            String[] var18 = Utils.wrapText(GameData.loadingTips[GameData.tipIndex], DeviceConfig.screenSize[0] - 50);

            for (int var25 = 0; var25 < var18.length; var25++) {
               vh.drawString(var1, var18[var25], DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 3 + var25 * Utils.lineHeight, 17);
            }
         }

         if (this.loadingAnim != null) {
            this.loadingAnim.x = (short)(15 + (DeviceConfig.screenSize[0] - 30) * this.loadShownPercent / 100);
            this.loadingAnim.y = (short)(DeviceConfig.screenSize[1] - 15);
            this.loadingAnim.setAction(3);
            this.loadingAnim.draw(var1);
            if (GameCanvas.instance.frameCounter % 2 == 0) {
               this.loadingAnim.nextFrame(true);
            }
         }

         if (this.loadShownPercent < this.loadTargetPercent) {
            this.loadShownPercent++;
            if (this.loadShownPercent > this.loadTargetPercent) {
               this.loadShownPercent = this.loadTargetPercent;
            }
         }

         if (this.loadFinished && this.loadShownPercent >= 100) {
            if (GameData.loadingTips != null) {
               GameData.tipIndex = (byte)GameUtil.randomRange(0, GameData.loadingTips.length);
            }

            this.loadingAnim = null;
            if (!DeviceConfig.modelName.equals("N73")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("N5800")
               && !DeviceConfig.modelName.equals("E62")
               && !DeviceConfig.modelName.equals("W958C")) {
               GameUtil.freeImageCaches();
            }

            this.loadTargetPercent = 0;
            this.loadShownPercent = 0;
            this.worldState = 2;
            this.mode = 1;
            return;
         }
      } else if (this.worldState != 2) {
         if (this.worldState == 3) {
            TouchSoftKeys.setSoftKeys(0);
            var1.setColor(0);
            var1.setClip(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
            var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
            var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
            var1.setColor(16777215);
            vh.drawString(var1, "Ta... bị đánh bại rồi sao?", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 - 40, 17);
            vh.drawString(var1, "Hả? Cơ thể không cử động được nữa.", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 - 20, 17);
            vh.drawString(var1, "Kết thúc thế này sao?", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 17);
            endScreenDone = true;
         }
      } else {
         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         if (GameCanvas.instance.frameCounter % GameSettings.skipNeedle == 0) {
            var1.setColor(0);
            var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
            if (this.mode == 1) {
               TouchSoftKeys.setSoftKeys(3);
            } else {
               TouchSoftKeys.setSoftKeys(0);
            }

            if ((
                  DeviceConfig.modelName.equals("E6")
                     || DeviceConfig.modelName.equals("E680")
                     || DeviceConfig.modelName.equals("W958C")
                     || DeviceConfig.modelName.equals("S1")
                     || DeviceConfig.modelName.equals("N95")
                     || DeviceConfig.modelName.equals("N5800")
               )
               && this.mode == 1) {
               byte var3 = 2;
               if (!TouchSoftKeys.hasKey((byte)5)) {
                  TouchButton var4;
                  (var4 = new TouchButton()).buttonId = 5;
                  var4.rect = new short[]{(short)(DeviceConfig.screenSize[0] / 2 - 13), (short)(GameCanvas.instance.screenHeight - 26), 26, 26};
                  TouchSoftKeys.addKey(var4);
               }
            }

            if (this.mode == 9 || this.mode == 8 || this.mode == 1 || this.mode == 2 || this.mode == 3 || this.mode == 4 || this.mode == 6 || this.mode == 7) {
               if (mapWidth < DeviceConfig.screenSize[0]) {
                  mapOffsetX = (DeviceConfig.screenSize[0] - mapWidth) / 2;
               } else {
                  mapOffsetX = 0;
               }

               if (mapHeight < DeviceConfig.screenSize[1]) {
                  mapOffsetY = (DeviceConfig.screenSize[1] - mapHeight) / 2;
               } else {
                  mapOffsetY = 0;
               }

               if (this.tileMap != null) {
                  this.tileMap.drawBackground(var1, mapOffsetX, this.shakeOffsetY + mapOffsetY);
               }

               if (GameData.mazeGatePoints != null && GameData.mazeGatePoints.length == 2) {
                  for (int var2 = 0; var2 < GameData.mazeGatePoints.length; var2++) {
                     short[] var19 = Utils.parseShortArray(GameData.mazeGatePoints[var2], ",");
                     if (var2 == 0) {
                        if (this.mazeGateImage1 != null && GameData.dungeonGateMode == 2) {
                           var1.drawImage(
                              this.mazeGateImage1, var19[0] + mapOffsetX - this.cameraX, var19[1] + 10 + this.shakeOffsetY + mapOffsetY - this.cameraY, 33
                           );
                        }
                     } else if (var2 == 1 && this.mazeGateImage2 != null) {
                        var1.drawImage(
                           this.mazeGateImage2, var19[0] + mapOffsetX - this.cameraX, var19[1] + 10 + this.shakeOffsetY + mapOffsetY - this.cameraY, 33
                        );
                     }
                  }
               }

               if (this.portalAnim != null) {
                  GameUtil.drawAnimation(var1, this.portalAnim, this.portalX + mapOffsetX - this.cameraX, this.portalY + mapOffsetY - this.cameraY, true, 0);
               }

               if (this.sceneLayer != null) {
                  this.sceneLayer.drawMiddle(var1, mapOffsetX, this.shakeOffsetY + mapOffsetY);
               }

               if (this.tileMap != null) {
                  this.tileMap.drawForeground(var1, mapOffsetX, this.shakeOffsetY + mapOffsetY);
               }

               if (this.sceneLayer != null) {
                  this.sceneLayer.drawTop(var1, mapOffsetX, this.shakeOffsetY + mapOffsetY);
                  Graphics var20 = var1;
                  if (GameData.questTable == null) {
                     GameData.questTable = Utils.getStringTable(Utils.loadConfigText("/bin/task.bin"), "task", "taskend", null, "\t");
                  }

                  int[] var26 = null;

                  for (int var6 = 0; GameData.sceneNpcs != null && var6 < GameData.sceneNpcs.length; var6++) {
                     for (int var7 = 0; GameData.activeQuests != null && var7 < GameData.activeQuests.length; var7++) {
                        if (Utils.contains(GameData.activeQuestIds, GameData.activeQuests[var7].id)
                           && GameData.sceneNpcs[var6].roleId == GameData.activeQuests[var7].turnInRoleId) {
                           if (GameData.activeQuests[var7].isCompleted) {
                              GameUtil.drawAnimation(
                                 var20,
                                 GameCanvas.instance.world.questDoneMarker,
                                 GameData.sceneNpcs[var6].x + mapOffsetX - GameCanvas.instance.world.cameraX,
                                 GameData.sceneNpcs[var6].y + mapOffsetY - GameData.sceneNpcs[var6].headHeight - GameCanvas.instance.world.cameraY - 3,
                                 true,
                                 0
                              );
                           } else {
                              GameUtil.drawAnimation(
                                 var20,
                                 GameCanvas.instance.world.questActiveMarker,
                                 GameData.sceneNpcs[var6].x + mapOffsetX - GameCanvas.instance.world.cameraX,
                                 GameData.sceneNpcs[var6].y + mapOffsetY - GameData.sceneNpcs[var6].headHeight - GameCanvas.instance.world.cameraY - 3,
                                 true,
                                 0
                              );
                           }

                           var26 = Utils.append(var26, var6);
                        }
                     }

                     int var33 = -1;

                     for (int var11 = 0; var11 < GameData.questTable.length; var11++) {
                        if (GameData.sceneNpcs[var6].roleId == Utils.parseShort(GameData.questTable[var11][9])) {
                           var33 = var11;
                           break;
                        }
                     }

                     if (var33 >= 0
                        && !Utils.contains(GameData.activeQuestIds, Utils.parseShort(GameData.questTable[var33][0]))
                        && !Utils.contains(GameData.finishedQuestIds, Utils.parseShort(GameData.questTable[var33][0]))) {
                        if (GameData.questTable[var33][2].equals("-1")) {
                           if (!Utils.contains(var26, var6)) {
                              GameUtil.drawAnimation(
                                 var20,
                                 GameCanvas.instance.world.questAvailableMarker,
                                 GameData.sceneNpcs[var6].x + mapOffsetX - GameCanvas.instance.world.cameraX,
                                 GameData.sceneNpcs[var6].y + mapOffsetY - GameData.sceneNpcs[var6].headHeight - GameCanvas.instance.world.cameraY - 3,
                                 true,
                                 0
                              );
                           }
                        } else {
                           String[] var12;
                           if ((var12 = Utils.split(GameData.questTable[var33][2], ","))[0].equals("已发生事件")
                              && Utils.contains(GameData.eventFlags, Utils.parseInt(var12[1]))
                              && !Utils.contains(var26, var6)
                              && !Utils.contains(var26, var6)) {
                              GameUtil.drawAnimation(
                                 var20,
                                 GameCanvas.instance.world.questAvailableMarker,
                                 GameData.sceneNpcs[var6].x + mapOffsetX - GameCanvas.instance.world.cameraX,
                                 GameData.sceneNpcs[var6].y + mapOffsetY - GameData.sceneNpcs[var6].headHeight - GameCanvas.instance.world.cameraY - 3,
                                 true,
                                 0
                              );
                           }
                        }
                     }
                  }

                  this.sceneLayer.drawSkyActors(var1, mapOffsetX, mapOffsetY);
               }

               var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
            }

            if (this.screenWipe != null) {
               this.screenWipe.draw(var1);
            }

            if (!this.screenWipe.isActive()) {
               if (this.isEnteringMap) {
                  if (GameData.isInDungeon) {
                     if (this.enterMapAnim == null) {
                        this.enterMapAnim = new AnimSprite();
                        this.enterMapAnim.load("/ft/tanshe.av");
                     }

                     this.enterMapAnim.x = (short)(GameData.party[0].x + mapOffsetX - this.cameraX);
                     this.enterMapAnim.y = (short)(GameData.party[0].y + mapOffsetY - this.cameraY);
                     this.enterMapAnim.draw(var1);
                     this.enterMapAnim.nextFrame(false);
                     if (this.enterMapAnim.getFrame() >= this.enterMapAnim.getFrameCount() - 1) {
                        GameData.party[0].isVisible = true;
                     }
                  }

                  this.enterFrames++;
                  if (this.enterFrames >= 10) {
                     if (this.enterMapAnim != null) {
                        this.enterMapAnim.setFrame(0);
                     }

                     this.isEnteringMap = false;
                     this.fieldHudEnabled = true;
                  }
               }

               if (this.levelUpEffect && !this.script.isRunning) {
                  if (this.levelUpAnim == null) {
                     this.levelUpAnim = new AnimSprite();
                     this.levelUpAnim.load("/skill/douqi.av");
                  }

                  GameUtil.drawAnimation(
                     var1, this.levelUpAnim, GameData.party[0].x + mapOffsetX - this.cameraX, GameData.party[0].y + mapOffsetY - this.cameraY, true, 0
                  );
                  if (this.levelUpAnim.getFrame() >= this.levelUpAnim.getFrameCount() - 1) {
                     this.levelUpEffect = false;
                     this.levelUpAnim.setFrame(0);
                  }
               }

               if (this.isTransforming) {
                  if (this.transformAnim == null) {
                     this.transformAnim = new AnimSprite();
                     this.transformAnim.load("/av/bianxing.av");
                  }

                  GameUtil.drawAnimation(
                     var1, this.transformAnim, GameData.party[0].x + mapOffsetX - this.cameraX, GameData.party[0].y + mapOffsetY - this.cameraY, true, 0
                  );
                  if (this.transformAnim.getFrame() >= this.transformAnim.getFrameCount() - 1) {
                     this.isTransforming = false;
                     this.transformAnim.setFrame(0);
                     FieldInput.applyTransform(this.transformKind);
                  }
               }

               if (this.tickerActive && !this.script.isRunning && this.tickerLines != null) {
                  Graphics var27 = var1;
                  World var21 = this;
                  if (this.tickerScroll < this.tickerLines.length * (Utils.lineHeight + 1) + Utils.lineHeight) {
                     GameUtil.drawPanel(
                        var1,
                        40,
                        DeviceConfig.screenSize[1] - Utils.lineHeight - 6,
                        DeviceConfig.screenSize[0] - 80,
                        Utils.lineHeight + 6,
                        1317669,
                        15788720,
                        80,
                        false
                     );
                     var1.setClip(0, DeviceConfig.screenSize[1] - 3 - Utils.lineHeight, DeviceConfig.screenSize[0], Utils.lineHeight);
                     var1.setColor(16777215);

                     for (int var31 = 0; var21.tickerLines != null && var31 < var21.tickerLines.length; var31++) {
                        vh.drawString(
                           var27,
                           var21.tickerLines[var31],
                           DeviceConfig.screenSize[0] / 2,
                           DeviceConfig.screenSize[1] - 3 - var21.tickerScroll + (Utils.lineHeight + 1) * var31,
                           17
                        );
                     }

                     if (!var21.tickerPaused) {
                        var21.tickerScroll++;
                        if (var21.tickerScroll == var21.tickerLine * (Utils.lineHeight + 1)) {
                           var21.tickerLine++;
                           var21.tickerPaused = true;
                           var21.tickerPauseStart = System.currentTimeMillis();
                        }
                     } else if (System.currentTimeMillis() - var21.tickerPauseStart >= 1000L) {
                        var21.tickerPaused = false;
                     }
                  } else {
                     this.tickerLine = 1;
                     this.tickerActive = false;
                     this.tickerLines = null;
                  }

                  var27.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               }

               if (this.isRaining) {
                  for (int var13 = 0; var13 < this.rainDrops.length; var13++) {
                     RainDrop var22 = this.rainDrops[var13];
                     var1.setColor(16777215);
                     var1.drawLine(var22.x, var22.y, var22.x + var22.lineDx, var22.y + var22.lineDy);
                     if (var22.x < -10 || var22.y > DeviceConfig.screenSize[1]) {
                        var22.x = (short)GameUtil.randomRange(0, DeviceConfig.screenSize[0] + 150);
                        var22.y = 0;
                     }

                     var22.x = (short)(var22.x + var22.dx);
                     var22.y = (short)(var22.y + var22.dy);
                  }
               }

               if (this.mode != 4 && this.mode != 5) {
                  Utils.drawOutlinedText(var1, GameData.sceneName, 2, 2, 65370, 0, 20);
               }

               if (this.fieldHudEnabled
                  && !this.script.isRunning
                  && this.mode != 6
                  && !this.storyCrawlActive
                  && this.mode != 3
                  && this.mode != 4
                  && this.mode != 5
                  && this.mode != 7
                  && this.mode != 8
                  && this.smsIconAnim != null) {
                  if (!DeviceConfig.modelName.equals("E2")
                     && !DeviceConfig.modelName.equals("E398")
                     && !DeviceConfig.modelName.equals("L6")
                     && !DeviceConfig.modelName.equals("L7")
                     && !DeviceConfig.modelName.equals("K1")
                     && !DeviceConfig.modelName.equals("V8")) {
                     this.smsIconAnim.x = (short)(DeviceConfig.screenSize[0] - 20);
                     this.smsIconAnim.y = (short)(DeviceConfig.screenSize[1] - 8);
                  } else {
                     this.smsIconAnim.x = 20;
                     this.smsIconAnim.y = (short)(DeviceConfig.screenSize[1] - 8);
                  }

                  this.smsIconAnim.draw(var1);
                  if (GameCanvas.instance.frameCounter % 2 == 0) {
                     this.smsIconAnim.nextFrame(true);
                  }
               }

               if (this.letterboxSize > 0) {
                  var1.setColor(0);
                  var1.fillRect(0, 0, DeviceConfig.screenSize[0], this.letterboxSize << 2);
                  var1.fillRect(
                     0,
                     DeviceConfig.screenSize[1] - (this.letterboxSize << 2),
                     DeviceConfig.screenSize[0],
                     DeviceConfig.screenSize[1] - (this.letterboxSize << 2)
                  );
               }

               if (this.blindsWidth > 0) {
                  var1.setColor(0);

                  for (int var14 = 0; var14 < 6; var14++) {
                     var1.fillRect(var14 * DeviceConfig.screenSize[0] / 6, 0, this.blindsWidth, DeviceConfig.screenSize[1]);
                  }
               }

               if (this.fieldHudEnabled && this.mode != 6 && this.mode != 5 && this.mode != 3 && !GameCanvas.instance.world.script.isRunning) {
                  TouchSoftKeys.draw(var1);
               }

               if (this.mode == 10) {
                  if (this.orderImage == null) {
                     this.orderImage = ResourceCache.loadImage("/pic/order.jpg");
                  }

                  if (this.orderImage != null) {
                     var1.drawImage(this.orderImage, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 3);
                  }

                  var1.setColor(16777215);
                  vh.drawString(var1, "Nhấn OK để tiếp tục", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] - 4, 33);
               }

               if (this.mode == 9) {
                  Graphics var28 = var1;
                  World var24 = this;
                  if (GameData.questTable == null) {
                     GameData.questTable = Utils.getStringTable(Utils.loadConfigText("/bin/task.bin"), "task", "taskend", null, "\t");
                  }

                  int var34 = GameData.findQuestIndex(this.shownQuestId);
                  int var17 = DeviceConfig.screenSize[1] / 2 - 5 * (Utils.lineHeight + 1);
                  GameUtil.drawPanel(var1, 40, var17, DeviceConfig.screenSize[0] - 80, 9 * (Utils.lineHeight + 1), 1317669, 15788720, 80, true);
                  String[] var5 = new String[]{"Chính tuyến", "Phụ tuyến", "Công hội"};
                  Utils.drawOutlinedText(
                     var1,
                     GameData.questTable[var34][1] + " [" + var5[Utils.parseInt(GameData.questTable[var34][3]) - 1] + "]",
                     DeviceConfig.screenSize[0] / 2,
                     var17 + 2 + (30 - Utils.lineHeight) / 2,
                     9306111,
                     2248833,
                     17
                  );
                  var1.setColor(16709092);
                  var1.fillRect(50, var17 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 5, DeviceConfig.screenSize[0] - 100, 1);
                  var1.setColor(3027509);
                  var1.fillRect(50, var17 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 6, DeviceConfig.screenSize[0] - 100, 1);
                  if (this.questDialogPage == 1) {
                     GameUtil.drawBoldText(var1, "Mô tả nhiệm vụ", DeviceConfig.screenSize[0] / 2, var17 + 2 + 30 + (30 - Utils.lineHeight) / 2, 2093589, 17);
                     String var30 = GameData.questTable[var34][5];
                     if (this.textLayout != null) {
                        this.textLayout.clear();
                     } else {
                        this.textLayout = new TextBlock();
                     }

                     this.textLayout.addText(var30, DeviceConfig.screenSize[0] - 90, null, 16777215);
                     this.textLayout.setBounds(45, var17 + 2 + 60, DeviceConfig.screenSize[0] - 80, Utils.lineHeight + 1 << 2, 4);
                     this.textLayout.draw(var1, this.textLayout, this.messageScroll, true, false);
                     if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                        Utils.drawOutlinedText(var1, "Thưởng", 43, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
                        Utils.drawOutlinedText(var1, "Bỏ", DeviceConfig.screenSize[0] - 40 - 3, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40);
                     } else {
                        Utils.drawOutlinedText(var1, "Bỏ", 43, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
                        Utils.drawOutlinedText(
                           var1, "Thưởng", DeviceConfig.screenSize[0] - 40 - 3, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40
                        );
                     }
                  } else {
                     var5 = Utils.split(GameData.questTable[var34][8], "|");
                     GameUtil.drawBoldText(var1, "Phần thưởng", DeviceConfig.screenSize[0] / 2, var17 + 2 + 30 + (30 - Utils.lineHeight) / 2, 2093589, 17);
                     var1.setColor(16777215);
                     var34 = 0;

                     for (int var8 = 0; var8 < var5.length; var8++) {
                        int[] var32;
                        if ((var32 = Utils.parseIntArray(var5[var8], ","))[0] == 1) {
                           GameData.loadItemTable();
                           if (GameData.itemIcons == null) {
                              GameData.loadItemIcons();
                           }

                           if (GameData.itemIcons[GameData.findItemIndex(var32[1])] != null) {
                              var28.drawImage(GameData.itemIcons[GameData.findItemIndex(var32[1])], 60, var17 + 2 + 60 + 15 + (Utils.lineHeight + 1) * var34, 3);
                           }

                           vh.drawString(
                              var28,
                              GameData.itemNames[GameData.findItemIndex(var32[1])],
                              DeviceConfig.screenSize[0] / 2,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              17
                           );
                           vh.drawString(
                              var28,
                              "x " + var32[2],
                              DeviceConfig.screenSize[0] - 40 - 10,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              24
                           );
                           var34++;
                        }

                        if (var32[0] == 2) {
                           Equip.loadTable();
                           if (Equip.icons == null) {
                              Equip.loadIcons();
                           }

                           if (Equip.icons[GameData.findEquipIndex(var32[1])] != null) {
                              var28.drawImage(Equip.icons[GameData.findEquipIndex(var32[1])], 60, var17 + 2 + 60 + 15 + (Utils.lineHeight + 1) * var34, 3);
                           }

                           vh.drawString(
                              var28,
                              Equip.names[GameData.findEquipIndex(var32[1])],
                              DeviceConfig.screenSize[0] / 2,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              17
                           );
                           vh.drawString(
                              var28,
                              "x 1",
                              DeviceConfig.screenSize[0] - 40 - 10,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              24
                           );
                           var34++;
                        }

                        if (var32[0] == 3) {
                           if (var24.goldIcon == null) {
                              var24.goldIcon = ResourceCache.loadImage("/pic/D10.gf");
                           }

                           var28.drawImage(var24.goldIcon, 60, var17 + 2 + 60 + 15 + (Utils.lineHeight + 1) * var34, 3);
                           vh.drawString(
                              var28, "Vàng", DeviceConfig.screenSize[0] / 2, var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34, 17
                           );
                           vh.drawString(
                              var28,
                              "" + var32[1],
                              DeviceConfig.screenSize[0] - 40 - 10,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              24
                           );
                           var34++;
                        }

                        if (var32[0] == 4) {
                           if (var24.lootPetName != null) {
                              vh.drawString(
                                 var28,
                                 var24.lootPetName,
                                 DeviceConfig.screenSize[0] / 2,
                                 var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                                 17
                              );
                              vh.drawString(
                                 var28,
                                 "x 1",
                                 DeviceConfig.screenSize[0] - 40 - 10,
                                 var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                                 24
                              );
                           } else {
                              int var10 = (short)var32[1];
                              String[][] var9 = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
                              String var36 = var9[--var10][0];
                              var24.lootPetName = var36;
                           }

                           var34++;
                        }

                        if (var32[0] == 5) {
                           if (var24.expIcon == null) {
                              var24.expIcon = ResourceCache.loadImage("/icon/13.gf");
                           }

                           var28.drawImage(var24.expIcon, 60, var17 + 2 + 60 + 15 + (Utils.lineHeight + 1) * var34, 3);
                           vh.drawString(
                              var28, "EXP", DeviceConfig.screenSize[0] / 2, var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34, 17
                           );
                           vh.drawString(
                              var28,
                              "" + var32[1],
                              DeviceConfig.screenSize[0] - 40 - 10,
                              var17 + 2 + 60 + (30 - Utils.lineHeight) / 2 + (Utils.lineHeight + 1) * var34,
                              24
                           );
                           var34++;
                        }
                     }

                     if (var24.softKeyImage == null) {
                        System.out.println("Pooooooooollll");
                        var24.softKeyImage = ResourceCache.loadImage("/pic/D23.gf", 1);
                     }

                     if (var24.softKeyImage != null) {
                        if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                           Utils.drawFrame(
                              var28,
                              var24.softKeyImage,
                              var24.softKeyImage.getWidth() >> 1,
                              var24.softKeyImage.getHeight(),
                              0,
                              43,
                              var17 + 9 * (Utils.lineHeight + 1) - 3,
                              36,
                              0
                           );
                           Utils.drawFrame(
                              var28,
                              var24.softKeyImage,
                              var24.softKeyImage.getWidth() >> 1,
                              var24.softKeyImage.getHeight(),
                              1,
                              DeviceConfig.screenSize[0] - 40 - 3,
                              var17 + 9 * (Utils.lineHeight + 1) - 3,
                              40,
                              0
                           );
                        } else {
                           Utils.drawFrame(
                              var28,
                              var24.softKeyImage,
                              var24.softKeyImage.getWidth() >> 1,
                              var24.softKeyImage.getHeight(),
                              1,
                              43,
                              var17 + 9 * (Utils.lineHeight + 1) - 3,
                              36,
                              0
                           );
                           Utils.drawFrame(
                              var28,
                              var24.softKeyImage,
                              var24.softKeyImage.getWidth() >> 1,
                              var24.softKeyImage.getHeight(),
                              0,
                              DeviceConfig.screenSize[0] - 40 - 3,
                              var17 + 9 * (Utils.lineHeight + 1) - 3,
                              40,
                              0
                           );
                        }
                     } else if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                        Utils.drawOutlinedText(var28, "Thưởng", 43, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
                        Utils.drawOutlinedText(var28, "Bỏ", DeviceConfig.screenSize[0] - 40 - 3, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40);
                     } else {
                        Utils.drawOutlinedText(var28, "Bỏ", 43, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
                        Utils.drawOutlinedText(
                           var28, "Thưởng", DeviceConfig.screenSize[0] - 40 - 3, var17 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40
                        );
                     }
                  }
               } else if (this.mode == 8) {
                  this.drawDungeonLoot(var1);
               } else if (this.mode == 2) {
                  if (this.dialogBox != null && this.dialogBox.isVisible) {
                     var1.translate(0, 0);
                     this.dialogBox.draw(var1);
                  }
               } else if (this.mode == 4) {
                  if (this.menu != null) {
                     this.menu.draw(var1);
                  }
               } else if (this.mode != 3) {
                  if (this.mode == 7 && this.smsMenu != null) {
                     this.smsMenu.draw(var1);
                  }
               } else if (this.messageLines != null) {
                  if (this.textLayout != null) {
                     this.textLayout.clear();
                  } else {
                     this.textLayout = new TextBlock();
                  }

                  for (int var15 = 0; var15 < this.messageLines.length; var15++) {
                     this.textLayout.addText(this.messageLines[var15], DeviceConfig.screenSize[0] - 70, null, 16777215);
                  }

                  this.messageVisibleLines = 3;
                  int var16 = DeviceConfig.screenSize[1] / 2 - (this.messageVisibleLines * (Utils.lineHeight + 1) + 10) / 2;
                  int var23 = this.messageVisibleLines * (Utils.lineHeight + 1) + 10;
                  if (this.textLayout.lines.length + 1 < this.messageVisibleLines) {
                     var23 = (this.textLayout.lines.length + 1) * (Utils.lineHeight + 1) + 10;
                     var16 = DeviceConfig.screenSize[1] / 2 - ((this.textLayout.lines.length + 1) * (Utils.lineHeight + 1) + 10) / 2;
                  }

                  GameUtil.drawPanel(var1, 20, var16, DeviceConfig.screenSize[0] - 40, var23, 1317669, 15788720, 80, true);
                  if (this.boxCornerImage == null) {
                     this.boxCornerImage = ResourceCache.loadImage("/pic/jiao1.gf", 2);
                  }

                  if (this.boxCornerImage != null) {
                     var1.drawRegion(this.boxCornerImage, 0, 0, this.boxCornerImage.getWidth() >> 1, this.boxCornerImage.getHeight() >> 1, 0, 19, var16 - 1, 20);
                     var1.drawRegion(
                        this.boxCornerImage,
                        this.boxCornerImage.getWidth() >> 1,
                        0,
                        this.boxCornerImage.getWidth() >> 1,
                        this.boxCornerImage.getHeight() >> 1,
                        0,
                        21 + DeviceConfig.screenSize[0] - 40,
                        var16 - 1,
                        24
                     );
                     var1.drawRegion(
                        this.boxCornerImage,
                        0,
                        this.boxCornerImage.getWidth() >> 1,
                        this.boxCornerImage.getHeight() >> 1,
                        this.boxCornerImage.getHeight() >> 1,
                        0,
                        19,
                        var16 + 1 + var23,
                        36
                     );
                     var1.drawRegion(
                        this.boxCornerImage,
                        this.boxCornerImage.getWidth() >> 1,
                        this.boxCornerImage.getHeight() >> 1,
                        this.boxCornerImage.getWidth() >> 1,
                        this.boxCornerImage.getHeight() >> 1,
                        0,
                        21 + DeviceConfig.screenSize[0] - 40,
                        var16 + 1 + var23,
                        40
                     );
                  } else {
                     var1.setColor(2170141);
                     var1.drawRoundRect(19, var16 - 1, DeviceConfig.screenSize[0] - 40 + 1, var23 + 1, 10, 10);
                  }

                  this.textLayout.setBounds(20, var16 + 5, DeviceConfig.screenSize[0] - 40, var23 - 10, this.messageVisibleLines);
                  this.textLayout.addText("Nhấn OK để tiếp tục", DeviceConfig.screenSize[0] - 70, null, 2093589);
                  this.textLayout.draw(var1, this.textLayout, this.messageScroll, false, true);
                  var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               }

               if (this.storyCrawlActive) {
                  this.storyCrawl.draw(var1);
               }

               if (this.isResting) {
                  var1.setColor(0);
                  var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
                  var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
                  var1.setColor(16777215);
                  vh.drawString(var1, "Nghỉ ngơi một chút", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 17);
                  this.restFrames--;
                  if (this.restFrames <= 0) {
                     this.isResting = false;
                     this.script.advance(0);
                  }
               }

               if (GameData.isEndingPending) {
                  this.drawEnding(var1);
               }

               if (this.whiteFlashOn) {
                  var1.setColor(16777215);
                  var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
                  var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               }

               if (DeviceConfig.debugMode) {
                  vh.drawString(var1, "phase:", 0, 25, 20);
                  if (this.debugDigitsImage == null) {
                     this.debugDigitsImage = ResourceCache.loadImage("/pic/number01.gf", 2);
                  }

                  if (this.debugDigitsImage != null) {
                     Utils.drawNumber(var1, GameData.storyPhase, vh.stringWidth(Utils.defaultFont, "phase:") + 2, 30, this.debugDigitsImage, 6, 7, 20);
                  }
               }

               if (this.nearNpc && this.dialogBox == null && this.menu == null && this.smsMenu == null) {
                  var1.setColor(16711680);
                  if (this.nearbyNpc.isVisible) {
                     Utils.drawOutlinedText(var1, this.nearbyNpc.name, DeviceConfig.screenSize[0] - 4, 2, 16711680, 16773728, 24);
                  }
               }

               this.drawDebugOverlay(var1);
            }
         }

         if (this.mode == 5) {
            if (this.battle != null) {
               this.battle.tick();
               this.battle.draw(var1);
               return;
            }
         } else if (this.mode == 6) {
            if (!CloseUpAnim.isPlaying) {
               return;
            }

            if (CloseUpAnim.anim == null) {
               CloseUpAnim.isPlaying = false;
               GameCanvas.instance.world.script.advance(5);
               CloseUpAnim.anim = null;
               GameCanvas.instance.world.mode = 1;
               return;
            }

            if (CloseUpAnim.waitForKey == 1) {
               var1.setColor(0);
               var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
            }

            CloseUpAnim.anim.x = CloseUpAnim.x;
            CloseUpAnim.anim.y = CloseUpAnim.y;
            CloseUpAnim.anim.draw(var1);
            if (CloseUpAnim.anim.getFrame() < CloseUpAnim.anim.getActionFrameCount(0) - 1) {
               CloseUpAnim.anim.nextFrame(false);
            } else if (CloseUpAnim.waitForKey == 1) {
               var1.setColor(16777215);
               vh.drawString(var1, "Nhấn phím bất kỳ để trở về", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] - 4, 33);
            } else {
               CloseUpAnim.isPlaying = false;
               GameCanvas.instance.world.script.advance(5);
               CloseUpAnim.anim = null;
               GameCanvas.instance.world.mode = 1;
            }
         }
      }
   }

   private void drawDungeonLoot(Graphics var1) {
      int var3 = (DeviceConfig.screenSize[1] - 9 * (Utils.lineHeight + 1)) / 2;
      int var2 = (DeviceConfig.screenSize[0] - 160) / 2;
      GameUtil.drawPanel(var1, var2, var3, DeviceConfig.screenSize[0] - var2 * 2, 9 * (Utils.lineHeight + 1), 1317669, 15788720, 80, true);
      Utils.drawOutlinedText(
         var1, GameData.dungeonName + " [Phó bản]", DeviceConfig.screenSize[0] / 2, var3 + 2 + (30 - Utils.lineHeight) / 2, 9306111, 2248833, 17
      );
      var1.setColor(16709092);
      var1.fillRect(var2 + 10, var3 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 5, DeviceConfig.screenSize[0] - 2 * (var2 + 10), 1);
      var1.setColor(3027509);
      var1.fillRect(var2 + 10, var3 + 2 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 6, DeviceConfig.screenSize[0] - 2 * (var2 + 10), 1);
      GameUtil.drawBoldText(var1, "Chiến lợi phẩm", DeviceConfig.screenSize[0] / 2, var3 + 2 + 30 + (30 - Utils.lineHeight) / 2, 2093589, 17);
      var1.setColor(16777215);
      if (this.dungeonLoot != null) {
         if (this.dungeonLoot[0] == 1) {
            GameData.loadItemTable();
            if (GameData.itemIcons == null) {
               GameData.loadItemIcons();
            }

            if (GameData.itemIcons[GameData.findItemIndex(this.dungeonLoot[1])] != null) {
               var1.drawImage(GameData.itemIcons[GameData.findItemIndex(this.dungeonLoot[1])], var2 + 20, var3 + 2 + 60 + 15, 3);
            }

            vh.drawString(
               var1,
               GameData.itemNames[GameData.findItemIndex(this.dungeonLoot[1])],
               DeviceConfig.screenSize[0] / 2,
               var3 + 2 + 60 + (30 - Utils.lineHeight) / 2,
               17
            );
            vh.drawString(var1, "x " + this.dungeonLoot[2], DeviceConfig.screenSize[0] - var2 - 10, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 24);
            if (this.lootNotices == null) {
               this.lootNotices = Utils.append(
                  this.lootNotices, "Nhận " + GameData.itemNames[GameData.findItemIndex(this.dungeonLoot[1])] + "  x" + this.dungeonLoot[2]
               );
            }
         } else if (this.dungeonLoot[0] == 2) {
            Equip.loadTable();
            if (Equip.icons == null) {
               Equip.loadIcons();
            }

            if (Equip.icons[GameData.findEquipIndex(this.dungeonLoot[1])] != null) {
               var1.drawImage(Equip.icons[GameData.findEquipIndex(this.dungeonLoot[1])], var2 + 20, var3 + 2 + 60 + 15, 3);
            }

            vh.drawString(
               var1, Equip.names[GameData.findEquipIndex(this.dungeonLoot[1])], DeviceConfig.screenSize[0] / 2, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 17
            );
            vh.drawString(var1, "x 1", DeviceConfig.screenSize[0] - var2 - 10, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 24);
            if (this.lootNotices == null) {
               this.lootNotices = Utils.append(
                  this.lootNotices, "Nhận " + Equip.names[GameData.findEquipIndex(this.dungeonLoot[1])] + "  x" + this.dungeonLoot[2]
               );
            }
         } else if (this.dungeonLoot[0] == 3) {
            if (this.goldIcon == null) {
               this.goldIcon = ResourceCache.loadImage("/pic/D10.gf");
            }

            var1.drawImage(this.goldIcon, var2 + 20, var3 + 2 + 60 + 15, 3);
            vh.drawString(var1, "Vàng", DeviceConfig.screenSize[0] / 2, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 17);
            vh.drawString(var1, "" + this.dungeonLoot[1], DeviceConfig.screenSize[0] - var2 - 10, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 24);
            if (this.lootNotices == null) {
               this.lootNotices = Utils.append(this.lootNotices, "Nhận vàng+" + this.dungeonLoot[1]);
            }
         } else if (this.dungeonLoot[0] == 4) {
            if (this.lootPetName != null) {
               vh.drawString(var1, this.lootPetName, DeviceConfig.screenSize[0] / 2, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 17);
               vh.drawString(var1, "x 1", DeviceConfig.screenSize[0] - var2 - 10, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2, 24);
               if (this.lootNotices == null) {
                  this.lootNotices = Utils.append(this.lootNotices, "Nhận sủng vật [" + this.lootPetName + "]");
               }
            } else {
               short var6 = (short)this.dungeonLoot[1];
               String[][] var4 = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
               int var5 = var6 - 1;
               String var7 = var4[var5][0];
               this.lootPetName = var7;
            }
         }
      }

      Object var8 = null;
      int var9 = 0;

      while (
         GameData.dungeonRewards != null
            && var9 < GameData.dungeonRewards.length
            && ((Object[])(var8 = Utils.parseIntArray(GameData.dungeonRewards[var9], ",")))[0] != 5
      ) {
         var9++;
      }

      if (var8 != null) {
         if (this.expIcon == null) {
            this.expIcon = ResourceCache.loadImage("/icon/13.gf");
         }

         var1.drawImage(this.expIcon, var2 + 20, var3 + 2 + 60 + 15 + Utils.lineHeight, 3);
         vh.drawString(var1, "EXP", DeviceConfig.screenSize[0] / 2, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 1, 17);
         vh.drawString(
            var1, "" + ((Object[])var8)[1], DeviceConfig.screenSize[0] - var2 - 10, var3 + 2 + 60 + (30 - Utils.lineHeight) / 2 + Utils.lineHeight + 1, 24
         );
         if (this.lootNotices == null || this.lootNotices != null && this.lootNotices.length < 2) {
            this.lootNotices = Utils.append(this.lootNotices, "Nhận EXP+" + ((Object[])var8)[1]);
         }
      }

      if (this.softKeyImage == null) {
         this.softKeyImage = ResourceCache.loadImage("/pic/D23.gf", 1);
      }

      if (this.softKeyImage != null) {
         if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            Utils.drawFrame(
               var1,
               this.softKeyImage,
               this.softKeyImage.getWidth() >> 1,
               this.softKeyImage.getHeight(),
               0,
               var2 + 3,
               var3 + 9 * (Utils.lineHeight + 1) - 3,
               36,
               0
            );
            Utils.drawFrame(
               var1,
               this.softKeyImage,
               this.softKeyImage.getWidth() >> 1,
               this.softKeyImage.getHeight(),
               1,
               DeviceConfig.screenSize[0] - var2 - 3,
               var3 + 9 * (Utils.lineHeight + 1) - 3,
               40,
               0
            );
         } else {
            Utils.drawFrame(
               var1,
               this.softKeyImage,
               this.softKeyImage.getWidth() >> 1,
               this.softKeyImage.getHeight(),
               1,
               var2 + 3,
               var3 + 9 * (Utils.lineHeight + 1) - 3,
               36,
               0
            );
            Utils.drawFrame(
               var1,
               this.softKeyImage,
               this.softKeyImage.getWidth() >> 1,
               this.softKeyImage.getHeight(),
               0,
               DeviceConfig.screenSize[0] - var2 - 3,
               var3 + 9 * (Utils.lineHeight + 1) - 3,
               40,
               0
            );
         }
      } else if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         Utils.drawOutlinedText(var1, "Nhận", var2 + 3, var3 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
         Utils.drawOutlinedText(var1, "Bỏ", DeviceConfig.screenSize[0] - var2 - 3, var3 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40);
      } else {
         Utils.drawOutlinedText(var1, "Bỏ", var2 + 3, var3 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 36);
         Utils.drawOutlinedText(var1, "Nhận", DeviceConfig.screenSize[0] - var2 - 3, var3 + 9 * (Utils.lineHeight + 1) - 3, 16642337, 11208715, 40);
      }
   }

   private void drawDebugOverlay(Graphics var1) {
      if (DeviceConfig.debugMode) {
         var1.translate(mapOffsetX, mapOffsetY);
         var1.translate(-this.cameraX, -this.cameraY);
         this.tileMap.drawDebugBlocks(var1);
         var1.translate(this.cameraX, this.cameraY);
         var1.translate(-this.cameraX, -this.cameraY);
         Graphics var3 = var1;
         ScriptEngine var2 = this.script;

         for (int var4 = 0; var2.events != null && var4 < var2.events.length; var4++) {
            if (Utils.contains(GameData.eventFlags, var2.events[var4].id)) {
               var3.setColor(15597568);
            } else {
               var3.setColor(16777215);
            }

            var3.drawRect(var2.events[var4].area[0], var2.events[var4].area[1], var2.events[var4].area[2], var2.events[var4].area[3]);
            vh.drawString(var3, String.valueOf(var2.events[var4].name), var2.events[var4].area[0], var2.events[var4].area[1], 20);
         }

         var1.translate(this.cameraX, this.cameraY);
         var1.translate(-this.cameraX, -this.cameraY);
         if (this.sceneLayer != null) {
            this.sceneLayer.drawDebugRects(var1);
         }

         var1.translate(this.cameraX, this.cameraY);
         var1.translate(-this.cameraX, -this.cameraY);

         for (int var6 = 0; this.sceneLayer != null && this.sceneLayer.actors != null && var6 < this.sceneLayer.actors.length; var6++) {
            if (this.sceneLayer.actors[var6] != null && this.sceneLayer.actors[var6].kind == 12) {
               short[] var8 = this.sceneLayer.actors[var6].getCollisionBoxes(1);
               var1.setColor(65280);
               var1.drawRect(var8[0], var8[1], var8[2], var8[3]);
            }
         }

         var1.setColor(16777215);
         int var7 = this.dragStartX < this.dragCurX ? this.dragStartX : this.dragCurX;
         int var9 = this.dragStartY < this.dragCurY ? this.dragStartY : this.dragCurY;
         int var10 = this.dragCurX > this.dragStartX ? this.dragCurX : this.dragStartX;
         int var5 = this.dragCurY > this.dragStartY ? this.dragCurY : this.dragStartY;
         var1.drawRect(var7, var9, var10 - var7, var5 - var9);
         var1.setColor(0);
         vh.drawString(var1, "(" + var7 + "," + var9 + "," + (var10 - var7) + "," + (var5 - var9) + ")", var7 + 1, var5 + 1, 20);
         var1.setColor(16777215);
         vh.drawString(var1, "(" + var7 + "," + var9 + "," + (var10 - var7) + "," + (var5 - var9) + ")", var7, var5, 20);
         var1.translate(this.cameraX, this.cameraY);
         var1.translate(0, 0);
      }
   }

   protected final void handleKey(int var1) {
      if (GameData.isEndingPending) {
         if (!endScreenDone) {
            return;
         }

         GameData.isEndingPending = false;
         endScreenDone = false;
         GameCanvas.screenState = 0;
         GameCanvas.instance.world.worldState = 0;
         GameData.resetGameData();
         Main.self.showCover();
      }

      if (this.worldState == 2) {
         if (this.mode == 7) {
            this.smsMenu.handleKey(var1);
         } else if (this.mode == 4) {
            MenuList.handleMenuKey(var1);
         } else if (this.mode == 6) {
            if (CloseUpAnim.anim.getFrame() >= CloseUpAnim.anim.getActionFrameCount(0) - 1) {
               CloseUpAnim.isPlaying = false;
               GameCanvas.instance.world.script.advance(5);
               CloseUpAnim.anim = null;
               GameCanvas.instance.world.mode = 1;
            }
         } else if (this.mode == 5) {
            this.battle.handleKey(var1);
         } else {
            FieldInput.handleKey(var1);
         }
      } else {
         if (this.worldState == 3 && endScreenDone) {
            GameCanvas.screenState = 0;
            this.a_Image = null;
            this.worldState = 0;
            GameData.resetGameData();
            Main.self.showCover();
         }
      }
   }

   public final void onPointerPress(int var1, int var2) {
      if (this.worldState == 2 && GameCanvas.instance.world.fieldHudEnabled) {
         if (this.mode == 1) {
            TouchButton var3;
            if ((var3 = TouchSoftKeys.hitTest(var1, var2)) != null) {
               if (GameCanvas.instance.world.script.isRunning) {
                  if (var3.buttonId == 2 && GameCanvas.instance.world.storyCrawlActive) {
                     GameCanvas.instance.world.storyCrawlActive = false;
                     GameCanvas.instance.world.script.advance(5);
                     GameCanvas.instance.world.storyCrawl.lines = null;
                  }
               } else if (var3.buttonId == 1) {
                  FieldInput.openMainMenu();
                  GameMenu.menuPage = 1;
                  GameCanvas.instance.world.menu.initPages();
               } else if (var3.buttonId == 2) {
                  if (GameCanvas.instance.world.storyCrawlActive) {
                     GameCanvas.instance.world.storyCrawlActive = false;
                     GameCanvas.instance.world.script.advance(5);
                     GameCanvas.instance.world.storyCrawl.lines = null;
                  } else {
                     PaidServiceMenu.open("/bin/smsmenu.bin");
                     if (DeviceConfig.unloadBeforeBattle) {
                        GameCanvas.instance.world.releaseResources();
                     }
                  }
               } else if (var3.buttonId == 4) {
                  if (PaidServiceMenu.hasTransform) {
                     if (GameData.rideFlags == 0) {
                        GameCanvas.instance.showToast("Hiện không thể dùng thú cưỡi", 10);
                     } else if (GameData.rideFlags == 2) {
                        GameCanvas.instance.showToast("Hiện không thể dùng thú cưỡi", 10);
                     } else if (!GameCanvas.instance.world.tileMap.hitsBlock(GameData.party[0].getCollisionBoxes(1), 1)
                        && !GameCanvas.instance.world.sceneLayer.collides(GameData.party[0].getCollisionBoxes(1), GameData.party[0], 1)) {
                        GameCanvas.instance.world.fieldHudEnabled = false;
                        GameCanvas.instance.world.isTransforming = true;
                        GameData.party[0].isVisible = false;
                        GameCanvas.instance.world.transformKind = 1;
                     }
                  } else {
                     GameCanvas.instance.world.messageLines = new String[]{"Chưa mở chức năng cưỡi sủng vật!"};
                     GameCanvas.instance.world.messageScroll = 0;
                     GameCanvas.instance.world.mode = 3;
                  }
               } else if (var3.buttonId == 5) {
                  FieldInput.startFlyTransform();
               }
            }
         } else if (this.mode == 4) {
            MenuList.handleMenuPointer(var1, var2);
         } else if (this.mode == 5) {
            this.battle.handlePointer(var1, var2);
         } else if (this.mode == 7) {
            this.smsMenu.handlePointer(var1, var2);
         } else if (this.mode == 9) {
            int var5 = DeviceConfig.screenSize[1] / 2 - 5 * (Utils.lineHeight + 1) + 9 * (Utils.lineHeight + 1) - 30;
            int var4 = DeviceConfig.screenSize[0] - 80;
            if (MenuList.isInButton(var1, var2, 40, var5, 30, 30)) {
               FieldInput.handleKey(KeyMap.softLeftKey);
            } else if (MenuList.isInButton(var1, var2, var4 + 40 - 30, var5, 30, 30)) {
               FieldInput.handleKey(KeyMap.softRightKey);
            }
         } else if (this.mode == 8) {
            int var7 = (DeviceConfig.screenSize[1] - 9 * (Utils.lineHeight + 1)) / 2;
            int var6 = (DeviceConfig.screenSize[0] - 160) / 2;
            if (MenuList.isInButton(var1, var2, var6, var7 + 9 * (Utils.lineHeight + 1) - 30, 30, 30)) {
               FieldInput.handleKey(KeyMap.softLeftKey);
            } else if (MenuList.isInButton(var1, var2, var6 + 160 - 30, var7 + 9 * (Utils.lineHeight + 1) - 30, 30, 30)) {
               FieldInput.handleKey(KeyMap.softRightKey);
            }
         }
      }

      this.dragStartX = var1 + this.cameraX - mapOffsetX;
      this.dragStartY = var2 + this.cameraY - mapOffsetY;
      this.dragCurX = this.dragStartX;
      this.dragCurY = this.dragStartY;
   }

   public final void onPointerRelease(int var1, int var2) {
      this.dragCurX = var1 + this.cameraX - mapOffsetX;
      this.dragCurY = var2 + this.cameraY - mapOffsetY;
      var1 = this.dragStartX < this.dragCurX ? this.dragStartX : this.dragCurX;
      var2 = this.dragStartY < this.dragCurY ? this.dragStartY : this.dragCurY;
      int var3 = this.dragCurX > this.dragStartX ? this.dragCurX : this.dragStartX;
      int var4 = this.dragCurY > this.dragStartY ? this.dragCurY : this.dragStartY;
      if (DeviceConfig.debugMode) {
         System.out.println("(" + var1 + "," + var2 + "," + (var3 - var1) + "," + (var4 - var2) + ")");
      }
   }

   public final void onPointerDrag(int var1, int var2) {
      this.dragCurX = var1 + this.cameraX - mapOffsetX;
      this.dragCurY = var2 + this.cameraY - mapOffsetY;
   }

   public final void reloadOrPlayMusic() {
      if (DeviceConfig.unloadBeforeBattle) {
         this.beginLoading();
      } else {
         playSceneMusic();
      }
   }

   public final void showTicker(String[] var1) {
      this.tickerLines = var1;
      this.tickerActive = true;
      this.tickerScroll = 0;
   }

   public final void startStoryCrawl(int var1, int var2, int var3, int var4, int var5) {
      if (this.storyCrawl == null) {
         this.storyCrawl = new StoryCrawl();
      }

      this.storyCrawl.begin(var1, var2, var3, var4, var5);
      this.storyCrawlActive = true;
   }

   public final void releaseResources() {
      if (DeviceConfig.debugMode) {
         System.out.println("Xóa tài nguyên clearResource()");
      }

      if (GameData.party != null && GameData.party[0] != null) {
         GameData.party[0].encounterSteps = -1;
         GameData.party[0].isMovingToTarget = false;
      }

      if (this.sceneLayer != null) {
         if (this.sceneLayer.actors != null) {
            this.sceneLayer.actors = null;
         }

         this.sceneLayer = null;
      }

      this.tileMap = null;
      this.battle = null;
      this.dialogBox = null;
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         GameUtil.freeImageCaches();
      }

      try {
         Thread.sleep(500L);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      if (DeviceConfig.debugMode) {
         System.out.println("Giải phóng tài nguyên xong");
      }
   }

   public final void startDialog(String[] var1, int var2) {
      if (this.dialogBox == null) {
         this.dialogBox = new DialogBox();
      }

      Vector var3 = new Vector();
      String[] var4 = null;

      for (int var5 = var2; var5 < var1.length; var5++) {
         if (var1[var5].indexOf("&") != -1) {
            var1[var5] = GameUtil.replaceAll(var1[var5], "&", GameData.roleNames[0]);
         }

         var3.addElement(var1[var5]);
      }

      if (var3.size() > 0) {
         var4 = new String[var3.size()];
         var3.copyInto(var4);
      }

      this.dialogBox.speaker = this.script.currentEvent.speaker;
      this.dialogBox.lines = var4;
      this.dialogBox.lineIndex = 0;
      if (this.script.currentEvent != null && this.script.currentEvent.speaker != null) {
         this.script.currentEvent.speaker.stopWander();
         if (this.script.currentEvent.speaker.actorType <= 2
            && (
               this.script.currentEvent.speaker.renderMode != 1
                  || this.script.currentEvent.speaker.animSprite.getActionCount() == 4
                  || this.script.currentEvent.speaker.animSprite.getActionCount() == 8
            )) {
            if (DeviceConfig.debugMode) {
               System.out.println("NPC quay hướng");
            }

            this.script.currentEvent.speaker.savedDirection = this.script.currentEvent.speaker.direction;
            var2 = GameData.party[0].y - this.script.currentEvent.speaker.y;
            int var7 = GameData.party[0].x - this.script.currentEvent.speaker.x;
            if (Math.abs(var2) > Math.abs(var7)) {
               if (var2 < 0) {
                  this.script.currentEvent.speaker.setDirection(1);
               } else {
                  this.script.currentEvent.speaker.setDirection(0);
               }
            } else if (var7 < 0) {
               this.script.currentEvent.speaker.setDirection(2);
            } else {
               this.script.currentEvent.speaker.setDirection(3);
            }

            this.script.currentEvent.speaker.setIdleAnim();
         }
      }

      this.dialogBox.layoutLine();
      this.dialogBox.isVisible = true;
      this.mode = 2;
   }

   public final RoleSprite findSceneRole(int var1) {
      RoleSprite var2 = null;

      for (int var3 = 0; this.sceneLayer.actors != null && var3 < this.sceneLayer.actors.length; var3++) {
         if (this.sceneLayer.actors[var3] != null && this.sceneLayer.actors[var3].kind == 1 && ((RoleSprite)this.sceneLayer.actors[var3]).roleId == var1) {
            var2 = (RoleSprite)this.sceneLayer.actors[var3];
            break;
         }
      }

      return var2;
   }

   public static void moveRole(RoleSprite var0, int var1, int var2, int var3, int var4, int var5) {
      if (var0 != null) {
         if (var2 > 0) {
            var0.moveSpeed = (byte)var2;
         }

         switch (var1) {
            case 0:
               var0.moveTo(var0.x, var0.y + var3, var1);
               return;
            case 1:
               var0.moveTo(var0.x, var0.y - var3, var1);
               return;
            case 2:
               var0.moveTo(var0.x - var3, var0.y, var1);
               return;
            case 3:
               var0.moveTo(var0.x + var3, var0.y, var1);
               return;
            default:
               var0.moveTo(var3, var4, var5);
         }
      }
   }

   public final void playCloseUpAnim(String var1, int var2, int var3, int var4) {
      if (this.closeUpAnim == null) {
         this.closeUpAnim = new CloseUpAnim();
      }

      int var10002 = var2 + mapOffsetX - this.cameraX;
      int var9 = var3 + mapOffsetY - this.cameraY;
      var3 = var10002;
      CloseUpAnim var6 = this.closeUpAnim;
      CloseUpAnim.animPath = var1;
      CloseUpAnim.x = (short)var3;
      CloseUpAnim.y = (short)var9;
      CloseUpAnim.waitForKey = var4;
      CloseUpAnim.isPlaying = true;
      (CloseUpAnim.anim = new AnimSprite()).load(CloseUpAnim.animPath);
      CloseUpAnim.anim.setPosition(DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2);
      this.mode = 6;
   }

   public final void startCameraPan(int var1, int var2) {
      this.cameraPanning = true;
      this.panDirection = (byte)var1;
      this.panSteps = (short)var2;
   }

   public static boolean leaderHasSkill(int var0) {
      return Utils.contains(GameData.party[0].skillIds, var0);
   }

   public final void loadEndingStory() {
      String var1 = Utils.loadConfigText("/bin/story.bin");
      this.endingStoryText = Utils.substringBetween(var1, "end" + this.endingId + "Story:", "end" + this.endingId + "StoryEnd");
      this.endingLines = Utils.splitAndWrap(this.endingStoryText, "\r\n", DeviceConfig.screenSize[0] - 10);
      this.endingStoryText = null;
      this.endingTextY = DeviceConfig.screenSize[1] + 5;
      this.creditsY = DeviceConfig.screenSize[1];
   }

   public final void resetSceneNpcs() {
      for (int var1 = 0; this.sceneLayer != null && this.sceneLayer.actors != null && var1 < this.sceneLayer.actors.length; var1++) {
         if (this.sceneLayer.actors[var1] != null
            && this.sceneLayer.actors[var1].kind == 1
            && ((RoleSprite)this.sceneLayer.actors[var1]).actorType == 2
            && !Utils.contains(GameData.partyRoleIds, ((RoleSprite)this.sceneLayer.actors[var1]).roleId)) {
            ((RoleSprite)this.sceneLayer.actors[var1]).resumeWander();

            try {
               Thread.sleep(50L);
            } catch (InterruptedException var3) {
               var3.printStackTrace();
            }
         }
      }
   }

   public final void openPetDepot() {
      if (DeviceConfig.unloadBeforeBattle && this.mode != 5) {
         this.releaseResources();
      }

      try {
         Thread.sleep(100L);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

      this.menu = new GameMenu();
      this.menu.initPages();
      if (this.battle == null && this.smsMenu == null) {
         this.mode = 4;
      }
   }

   public final void openBestiary() {
      GameMenu.hero = GameData.party[0];
      GameMenu.menuPage = 8;
      GameMenu.dexPage = 1;
      this.menu.buildDexLists();
   }

   public final void openShop(String var1, int var2, int var3) {
      GameMenu.shopItemIds = null;
      if (DeviceConfig.unloadBeforeBattle && this.mode != 5) {
         this.releaseResources();
      }

      try {
         Thread.sleep(100L);
      } catch (InterruptedException var5) {
         var5.printStackTrace();
      }

      GameMenu.hero = GameData.party[0];
      this.menu = new GameMenu();
      GameCanvas.instance.world.mode = 4;
      if (var1.equals("技能学习")) {
         GameMenu.skillLearnerType = var3;
         GameMenu.shopType = 2;
         GameMenu.loadShopStock(var2);
         GameMenu.menuPage = 14;
         this.menu.initPages();
      } else if (var1.equals("买道具")) {
         GameMenu.menuPage = 15;
         GameMenu.shopType = 0;
         GameData.loadItemTable();
         GameMenu.loadShopStock(var2);
         this.menu.initPages();
      } else {
         if (var1.equals("买装备")) {
            GameMenu.menuPage = 15;
            GameMenu.shopType = 1;
            Equip.loadTable();
            GameMenu.loadShopStock(var2);
            this.menu.initPages();
         }
      }
   }

   public final void startScriptBattle(String[] var1, int var2) {
      short[] var3 = new short[var1.length - var2];
      short[] var4 = new short[var1.length - var2];
      short[] var5 = new short[var1.length - var2];

      for (int var7 = 0; var7 < var3.length; var7++) {
         short[] var6 = Utils.parseShortArray(var1[var7 + var2], "|");
         var3[var7] = var6[0];
         var4[var7] = var6[1];
         var5[var7] = var6[2];
      }

      this.beginBattle(var3, var4, var5);
   }

   private void beginBattle(short[] var1, short[] var2, short[] var3) {
      World var4 = this;
      if (this.sceneLayer != null) {
         for (int var5 = 0; var4.sceneLayer.actors != null && var5 < var4.sceneLayer.actors.length; var5++) {
            if (var4.sceneLayer.actors[var5] != null && var4.sceneLayer.actors[var5].kind == 1 && ((RoleSprite)var4.sceneLayer.actors[var5]).actorType == 2) {
               ((RoleSprite)var4.sceneLayer.actors[var5]).stopWander();
            }
         }
      }

      this.battlePending = true;
      this.battleEnemyIds = var1;
      this.battleEnemyLevels = var2;
      this.battleEnemyCounts = var3;
      this.flashCounter = 5;
      this.battleFlashing = true;
   }

   public static void stopMusic() {
      if (Main.music != null) {
         Main.music.stop();
         Main.music.release();
         Main.music = null;
      }
   }

   public final void removeRole(int var1) {
      GameData.removedRoleIds = Utils.append(GameData.removedRoleIds, var1);
      RoleSprite var2 = null;

      for (int var3 = 0; this.sceneLayer != null && this.sceneLayer.actors != null && var3 < this.sceneLayer.actors.length; var3++) {
         if (this.sceneLayer.actors[var3] != null && this.sceneLayer.actors[var3].roleId == var1) {
            var2 = (RoleSprite)this.sceneLayer.actors[var3];
         }
      }

      if (var2 != null) {
         this.roleBeingRemoved = var2;
         this.removeBlinkCount = 10;
         this.isRemovingRole = true;
         this.playerControlEnabled = false;
      } else {
         this.script.advance(0);
      }
   }

   public final void closeDialog() {
      if (this.dialogBox != null) {
         this.dialogBox.closeDialog();
         this.dialogBox = null;
      }

      this.nearbyNpc = null;
      FieldInput.talkTarget = null;
      this.nearNpc = false;
      this.dialogSpeaker = null;

      try {
         Thread.sleep(20L);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }
   }

   public final void returnFromBattle() {
      this.script.isMustLoseBattle = false;
      this.isNormalBattle = false;
      this.isTutorialBattle = false;

      for (int var1 = 0; var1 < GameData.party.length; var1++) {
         if (GameData.party[var1].stats[3] <= 0) {
            GameData.party[var1].stats[3] = GameData.party[var1].stats[10];
            GameData.party[var1].stats[5] = GameData.party[var1].stats[11];
         }
      }

      for (int var5 = 0; GameData.myPets != null && var5 < GameData.myPets.length; var5++) {
         GameData.myPets[var5].isFainted = false;
         if (GameData.myPets[var5].stats[1] <= 0) {
            GameData.myPets[var5].stats[1] = 1;
            if (GameData.myPets[var5].isDeployed) {
               GameData.myPets[var5].isDeployed = false;
            }
         }
      }

      if (DeviceConfig.modelName.equals("N73")
         || DeviceConfig.modelName.equals("N7610")
         || DeviceConfig.modelName.equals("N5800")
         || DeviceConfig.modelName.equals("E62")
         || DeviceConfig.modelName.equals("W958C")) {
         this.worldState = 0;
         stopMusic();
         this.battle.dispose();
         this.battle = null;
         this.smsMenu = null;

         try {
            Thread.sleep(400L);
         } catch (InterruptedException var4) {
            var4.printStackTrace();
         }

         this.worldState = 2;
         playSceneMusic();
         this.mode = 1;
         GameCanvas.instance.world.resetSceneNpcs();
      } else if (DeviceConfig.unloadBeforeBattle) {
         this.worldState = 0;
         stopMusic();
         this.battle.dispose();
         this.battle = null;
         this.smsMenu = null;
         GameUtil.freeImageCaches();

         try {
            Thread.sleep(400L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         this.reloadOrPlayMusic();
      } else {
         this.worldState = 0;
         this.battle.dispose();
         this.battle = null;
         stopMusic();
         this.smsMenu = null;
         GameUtil.freeImageCaches();

         try {
            Thread.sleep(400L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         this.worldState = 2;
         playSceneMusic();
         this.mode = 1;
      }
   }

   public static void levelUpPets(PetInfo[] var0) {
      if (var0 != null) {
         for (int var1 = 0; var1 < var0.length; var1++) {
            if (var0[var1].isDeployed && var0[var1].stats[13] >= var0[var1].stats[14]) {
               GameData.growPetStats(var0[var1]);
               var0[var1].stats[0]++;
               var0[var1].stats[15] = var0[var1].stats[14];
               int var2 = var0[var1].stats[0] + 1;
               var0[var1].stats[14] = var2 * var2 * var2 / 2 + var2 * var2;
               var0[var1].stats[13] = 0;
               var0[var1].stats[1] = var0[var1].stats[2];
               var0[var1].stats[4] = var0[var1].stats[5];
            }
         }
      }
   }

   public final void checkLevelUp(RoleSprite var1) {
      if (var1.stats != null && var1.stats[1] >= var1.stats[2]) {
         var1.stats[0]++;
         var1.stats[15] = var1.stats[2];
         int var2 = var1.stats[0] + 1;
         var1.stats[2] = var2 * var2 * var2 / 2 + var2 * var2;
         var1.stats[1] = 0;
         GameData.refreshRoleStats(var1);
         var1.stats[3] = var1.stats[10];
         var1.stats[5] = var1.stats[11];
         this.levelUpEffect = true;
      }
   }

   public static void playSceneMusic() {
      if (GameData.sceneMusic != null && !GameData.sceneMusic.equals("")) {
         if (Main.music != null) {
            if (!Main.music.fileName.equals("/music/" + GameData.sceneMusic + ".mid")) {
               Main.music.stop();
               Main.music.release();
               Main.music.load("/music/" + GameData.sceneMusic + ".mid");
               Main.music.setVolume(GameData.musicVolume);
               Main.music.start();
               return;
            }

            Main.music.setVolume(GameData.musicVolume);
            Main.music.start();
            return;
         }

         (Main.music = new MusicPlayer()).load("/music/" + GameData.sceneMusic + ".mid");
         Main.music.setVolume(GameData.musicVolume);
         Main.music.start();
      }
   }

   private void drawEnding(Graphics var1) {
      if (!this.endingTextDone && this.endingLines != null) {
         for (int var4 = 0; this.endingLines != null && var4 < this.endingLines.length; var4++) {
            Utils.drawOutlinedText(var1, this.endingLines[var4], DeviceConfig.screenSize[0] / 2, this.endingTextY + Utils.lineHeight * var4, 0, 16777215, 33);
         }

         if (GameCanvas.instance.frameCounter % 2 == 0) {
            if (this.endingTextY + Utils.lineHeight * this.endingLines.length <= -10) {
               this.endingTextDone = true;
               return;
            }

            this.endingTextY -= 3;
            if (DeviceConfig.modelName.equals("N73")) {
               this.endingTextY -= 6;
               return;
            }
         }
      } else {
         var1.setColor(0);
         var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         int var10001 = DeviceConfig.screenSize[0] / 2;
         int var3 = this.creditsY;
         int var2 = var10001;
         var1.setColor(16753731);
         vh.drawString(var1, "Đội ngũ phát triển", var2, var3, 33);
         var1.setColor(16776960);
         vh.drawString(var1, "Thiết kế", var2, var3 + (Utils.lineHeight << 1), 33);
         var1.setColor(16777215);
         vh.drawString(var1, "softsai", var2, var3 + Utils.lineHeight * 3, 33);
         var1.setColor(16776960);
         vh.drawString(var1, "Mỹ thuật", var2, var3 + Utils.lineHeight * 5, 33);
         var1.setColor(16777215);
         vh.drawString(var1, "deapho", var2, var3 + Utils.lineHeight * 6, 33);
         var1.setColor(16776960);
         vh.drawString(var1, "Lập trình", var2, var3 + (Utils.lineHeight << 3), 33);
         var1.setColor(16777215);
         vh.drawString(var1, "alter", var2, var3 + Utils.lineHeight * 9, 33);
         var1.setColor(16776960);
         vh.drawString(var1, "Kiểm thử", var2, var3 + Utils.lineHeight * 11, 33);
         var1.setColor(16777215);
         vh.drawString(var1, "softsai", var2, var3 + Utils.lineHeight * 12, 33);
         var1.setColor(16776960);
         vh.drawString(var1, "Công ty TNHH Công nghệ Thông tin Tỉnh Trung Nguyệt", var2, var3 + Utils.lineHeight * 19, 33);
         vh.drawString(var1, "2009.6", var2, var3 + Utils.lineHeight * 20, 33);
         if (GameCanvas.instance.frameCounter % 2 == 0) {
            if (this.creditsY + Utils.lineHeight * 20 > DeviceConfig.screenSize[1] / 2) {
               this.creditsY -= 3;
               if (DeviceConfig.modelName.equals("D608")) {
                  this.endingTextY -= 5;
                  return;
               }
            } else {
               endScreenDone = true;
            }
         }
      }
   }

   private void loadPaletteTables() {
      this.paletteFromColors = null;
      this.paletteToColors = null;
      String[] var3 = Utils.getStringBlock(Utils.loadConfigText("/bin/sn.bin"), "tablename", "tableend", null, "\t");

      for (int var4 = 0; GameData.recolorImageNames != null && var4 < GameData.recolorImageNames.length; var4++) {
         String[] var2 = Utils.split(GameData.recolorSpecs[var4], ",");
         String[][] var1;
         if ((var1 = Utils.getStringTable(GameData.colorTableText, var3[Utils.parseInt(var2[0]) - 1] + ":", "End", null, ",")) != null) {
            int[] var5 = null;
            int[] var6 = null;

            for (int var7 = 0; var1 != null && var7 < var1[0].length; var7++) {
               var5 = Utils.append(var5, Utils.parseInt(var1[0][var7]));
               var6 = Utils.append(var6, Utils.parseInt(var1[Utils.parseInt(var2[1]) - 1][var7]));
            }

            this.paletteFromColors = Utils.append(this.paletteFromColors, var5);
            this.paletteToColors = Utils.append(this.paletteToColors, var6);
         }
      }
   }

   private void recolorTilesets() {
      if (GameData.recolorImageNames != null) {
         String[] var1 = Utils.getStringBlock(Utils.loadConfigText("/bin/sn.bin"), "tablename", "tableend", null, "\t");

         for (int var2 = 0; var2 < GameData.recolorImageNames.length; var2++) {
            int[] var3 = null;
            int[] var4 = null;
            int[] var5 = Utils.parseIntArray(GameData.recolorSpecs[var2], ",");
            String[][] var6;
            if ((var6 = Utils.getStringTable(GameData.colorTableText, var1[var5[0] - 1] + ":", "End", null, ",")) != null) {
               for (int var7 = 0; var6 != null && var7 < var6[0].length; var7++) {
                  var3 = Utils.append(var3, Utils.parseInt(var6[0][var7]));
                  var4 = Utils.append(var4, Utils.parseInt(var6[var5[1] - 1][var7]));
               }
            }

            this.tileMap.recolorTileset(GameData.recolorImageNames[var2] + ".gf", var3, var4);
         }
      }
   }

   public final void startRain() {
      this.rainDrops = new RainDrop[100];

      for (int var1 = 0; var1 < 100; var1++) {
         this.rainDrops[var1] = new RainDrop();
      }

      this.isRaining = true;
      this.script.advance(0);
   }
}
