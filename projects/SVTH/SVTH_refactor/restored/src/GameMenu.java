import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class GameMenu implements ResultListener {
   private TextBlock textBox;
   public static RoleSprite hero;
   public static PetInfo[] pets;
   static byte petActionIndex;
   static byte equipSlotIndex;
   static short equipBagIndex;
   static byte equipBagScrollRow;
   static byte equipActionIndex;
   static byte bagTabIndex;
   static byte bagScrollRow;
   static byte bagItemIndex;
   static byte bagActionIndex;
   static byte questTabIndex;
   static byte j_B;
   static byte questActionIndex;
   static byte saveSlotIndex;
   static int dexPetIndex;
   static byte useTargetIndex;
   static byte useTargetScroll;
   static String[] systemMenuRows;
   private byte cursorBob;
   public static byte menuPage;
   public boolean isDexFromScript = false;
   public boolean isStatsFromScript = false;
   public static byte systemPage;
   public static byte petPage;
   public byte implantStep = 0;
   public boolean showImplantFxLeft = false;
   public boolean showImplantFxRight = false;
   public static byte petSkillPage = 0;
   public static byte equipPage;
   public static byte socketResult = 0;
   public static byte bagPage;
   public static byte questPage;
   public static byte skillPage;
   public static byte shopType;
   public static byte dexPage;
   public int b_I = 0;
   public static byte skillShopPage = 0;
   public static int skillLearnerType;
   public static byte shopPage = 0;
   static String confirmText;
   public static short[][] gems;
   public static int gemIndex = 0;
   public static int gemScroll = 0;
   public static int gemRows = 0;
   public static int selectedGemId = 0;
   private int screenW;
   private int screenH;
   private int previewPage = 2;
   SideQuestListPanel sideQuestList;
   MainQuestListPanel mainQuestList;
   QuestDetailBox questDetail;
   MenuTabBar tabBar;
   private int contentW = 193;
   private static AnimSprite elementWheelAnim;
   private AnimSprite dexFilterCursorAnim;
   AnimSprite petAnim;
   private AnimSprite heroFigureAnim;
   private AnimSprite implantFxLeft;
   private AnimSprite implantFxRight;
   AnimSprite speakerAnim;
   private static AnimSprite[] setGlowAnims;
   static AnimSprite previewAnim;
   static AnimSprite[] saveSlotAnims;
   private Image b_Image;
   private Image coinIcon;
   private Image digits;
   Image cursorArrow;
   private Image equipSlotLabels;
   private Image starImg;
   private Image questTabsImg;
   private Image geneLabelImg;
   private Image questTabsSelImg;
   private Image questDoneImg;
   private Image bagTabsImg;
   private Image bagTabsSelImg;
   private Image setLabelImg;
   private Image[] actionMenuImgs = new Image[2];
   private Image implantBoxBg;
   private Image dexHeaderImg;
   private Image topPanelBg;
   private Image bottomPanelBg;
   private Image footerBg;
   private Image elementIcons;
   private Image leftRightArrows;
   private Image confirmCancelImg;
   private Image statBarsImg;
   private Image systemTitleImg;
   private Image softKeyImg;
   private Image attrIcons;
   private Image proficiencyImg;
   private Image freePointsImg;
   private Image levelImg;
   private Image classNameImg;
   private Image footerCoinIcon;
   private Image dexArrowsImg;
   private Image dexFilterImg;
   private Image statSelectImg;
   private Image goldDigits;
   private Image plusDigits;
   private Image minusDigits;
   private String[] actionLabels = new String[]{
      "Thuộc tính", "Kỹ năng", "Xuất chiến", "Nghỉ", "Thả", "Đổi tên", "Cấy", "Tinh luyện", "Mặc", "Tháo", "Thay", "Khảm", "Bỏ", "Dùng", "Bán", "Xem", "Bỏ"
   };
   private int topPanelH = 0;
   private int bottomPanelH = 0;
   int footerH = 0;
   private int frameW = 0;
   MaterialListPanel fusionMaterialList;
   public DexListPanel formList;
   public DexListPanel dexAllList;
   public DexListPanel dexElementList;
   public DexListPanel dexFoundList;
   public short[][] fusionRecipe;
   public int dexPetId = 0;
   public static short[] shopItemIds;
   public int bodyH = 0;
   int[] tabBarBounds;
   public static MenuList skillShopList;
   public static MenuList shopList;
   MenuList systemMenuList;
   public MenuList heroSkillList;
   public MenuList petSkillList;
   public PetListPanel petList;
   private PetListPanel partyList;
   public static int buyCount = 1;
   private Equip shopEquip;
   public static int learnerRows;
   public static int learnerIndex;
   public static int learnerScroll;
   static byte useTargetRows;
   static String[][] petData;
   private static String[] dexNames;
   static byte[] dexElements;
   static byte[] dexStatStars;
   private String[] dexFilterNames;
   public boolean isFusionDone;
   public boolean isFusing;
   int fusionProgress;
   static boolean socketPending;
   static long socketStartTime;
   static short heroSkillCount;
   static byte saveConfirmMode;
   static String[] questNames;
   int questIndex;
   private String[] questTabNames;
   public static short bagCols = 7;
   public static short bagRows = 5;
   static short[][][] bagItems = null;
   static String[] bagActionNames;
   static int[] bagActionIds = new int[]{13, 14, 12};
   static short bagQuantity;
   private String[] bagTabNames;
   static short gridCols = 6;
   static short gridRows = 2;
   static String[] equipActionNames;
   static short[] setItemIds;
   static int setPiecesWorn;
   static int[] equipActionIds = new int[]{10, 9, 12, 11};
   public static String[] petActionNames;
   public static int[] petActionIds = new int[]{0, 1, 2, 4, 5, 6, 7};
   public static String petMessage;
   int implantScrollRow;
   int implantMaterialIndex;
   int implantSlot;
   byte[][] implantMaterials;
   int geneSlotCount;
   int geneIndex;
   int statRowIndex;
   int[] pendingStatPoints;
   int[] pendingStatBonus;
   private int softKeyFrame;
   private String toastText;
   private boolean isToastVisible;
   private int toastTimer;

   public GameMenu() {
      String[] var10000 = new String[]{"Nam chính", "Nữ chính", "Pháp sư", "Sủng vật"};
      this.dexFilterNames = new String[]{"Mới nhất", "Nguyên tố", "Tất cả"};
      this.isFusionDone = false;
      this.isFusing = false;
      this.fusionProgress = 0;
      this.questIndex = -1;
      this.questTabNames = new String[]{"NV chính tuyến", "NV phụ tuyến"};
      this.bagTabNames = new String[]{"Vật phẩm", "Nguyên liệu", "Khác"};
      int[] var2 = new int[]{16660536, 12175614, 15964940, 9693914};
      this.implantScrollRow = 0;
      this.implantMaterialIndex = 0;
      this.implantSlot = 0;
      this.implantMaterials = null;
      this.geneSlotCount = 1;
      this.geneIndex = 0;
      this.statRowIndex = 0;
      this.pendingStatPoints = new int[]{0, 0, 0, 0};
      this.pendingStatBonus = new int[]{0, 0, 0, 0, 0};
      this.softKeyFrame = 0;
      gridCols = 6;
      gridRows = 2;
      bagCols = 7;
      bagRows = 5;
      this.frameW = 191;
      if (this.tabBar == null) {
         this.tabBar = new MenuTabBar();

         for (int var1 = 0; var1 < 8; var1++) {
            this.tabBar.addTab(var1, var1);
         }

         this.tabBar.setLayout((DeviceConfig.screenSize[0] - 193) / 2, (DeviceConfig.screenSize[1] - 255) / 2, 193, 28, 27);
         this.tabBarBounds = this.tabBar.getBounds();
      }

      this.bodyH = 200;
      this.setPanelHeights(90, this.bodyH - 90);
      if (this.dexArrowsImg == null) {
         this.dexArrowsImg = ResourceCache.loadImage("/pic/D20.gf");
      }

      if (this.dexFilterImg == null) {
         this.dexFilterImg = ResourceCache.loadImage("/pic/D21.gf");
      }

      if (this.implantBoxBg == null) {
         this.implantBoxBg = GameUtil.createTranslucentImage(190, 90, 657930, 85);
      }

      if (this.confirmCancelImg == null) {
         this.confirmCancelImg = ResourceCache.loadImage("/pic/D23.gf");
      }

      if (this.elementIcons == null) {
         this.elementIcons = ResourceCache.loadImage("/pic/D02.gf", 2);
      }

      if (this.dexHeaderImg == null) {
         this.dexHeaderImg = ResourceCache.loadImage("/pic/tjitem.gf");
      }

      if (this.leftRightArrows == null) {
         this.leftRightArrows = ResourceCache.loadImage("/pic/D11.gf");
      }

      if (this.equipSlotLabels == null) {
         this.equipSlotLabels = ResourceCache.loadImage("/pic/partInfo.gf");
      }

      if (this.bagTabsImg == null) {
         this.bagTabsImg = ResourceCache.loadImage("/pic/goodsPc.gf");
      }

      if (this.bagTabsSelImg == null) {
         this.bagTabsSelImg = ResourceCache.loadImage("/pic/goodsPc1.gf");
      }

      if (this.starImg == null) {
         this.starImg = ResourceCache.loadImage("/pic/star.gf", 2);
      }

      if (this.questDoneImg == null) {
         this.questDoneImg = ResourceCache.loadImage("/pic/D15.gf");
      }

      if (this.questTabsSelImg == null) {
         this.questTabsSelImg = ResourceCache.loadImage("/pic/taskNameArr.gf");
      }

      if (this.statBarsImg == null) {
         this.statBarsImg = ResourceCache.loadImage("/pic/D05.gf");
      }

      if (this.footerCoinIcon == null) {
         this.footerCoinIcon = ResourceCache.loadImage("/pic/D10.gf");
      }

      if (this.softKeyImg == null) {
         this.softKeyImg = ResourceCache.loadImage("/pic/D06.gf");
      }

      if (this.actionMenuImgs[1] == null) {
         String var10002 = "/pic/imgmenu2.gf";
         this.actionMenuImgs[1] = null;
      }

      if (this.actionMenuImgs[0] == null) {
         String var4 = "/pic/imgmenu1.gf";
         this.actionMenuImgs[0] = null;
      }

      if (this.questTabsImg == null) {
         this.questTabsImg = ResourceCache.loadImage("/pic/taskNameArr1.gf");
      }

      if (this.plusDigits == null) {
         this.plusDigits = ResourceCache.loadImage("/pic/number04.gf", 2);
      }

      if (this.goldDigits == null) {
         this.goldDigits = ResourceCache.loadImage("/pic/number03.gf", 2);
      }

      if (this.statSelectImg == null) {
         this.statSelectImg = ResourceCache.loadImage("/pic/number02.gf", 2);
      }

      if (this.levelImg == null) {
         this.levelImg = ResourceCache.loadImage("/pic/D08.gf");
      }

      if (this.classNameImg == null) {
         this.classNameImg = ResourceCache.loadImage("/pic/D09.gf");
      }

      if (this.leftRightArrows == null) {
         this.leftRightArrows = ResourceCache.loadImage("/pic/D11.gf");
      }

      if (this.proficiencyImg == null) {
         this.proficiencyImg = ResourceCache.loadImage("/pic/shulian.gf");
      }

      if (this.freePointsImg == null) {
         this.freePointsImg = ResourceCache.loadImage("/pic/D07.gf");
      }

      if (this.attrIcons == null) {
         this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
      }

      if (this.setLabelImg == null) {
         this.setLabelImg = ResourceCache.loadImage("/pic/taoz.gf");
      }

      if (this.cursorArrow == null) {
         this.cursorArrow = ResourceCache.loadImage("/pic/arrow.gf", 2);
      }

      if (this.geneLabelImg == null) {
         String var10001 = "/pic/tzjy.gf";
         this.geneLabelImg = null;
      }

      if (this.coinIcon == null) {
         this.coinIcon = ResourceCache.loadImage("/pic/D10.gf");
      }

      if (this.b_Image == null) {
         this.b_Image = ResourceCache.loadImage("/pic/D23.gf");
      }

      if (this.digits == null) {
         this.digits = ResourceCache.loadImage("/pic/number01.gf", 2);
      }

      if (this.footerBg == null) {
         this.footerBg = GameUtil.createTranslucentImage(193, 30, 2959909, 85);
      }

      this.footerH = 28;
      if (this.systemTitleImg == null) {
         String var3 = "/pic/setup.gf";
         this.systemTitleImg = null;
      }

      if (this.minusDigits == null) {
         this.minusDigits = ResourceCache.loadImage("/pic/number05.gf", 2);
      }

      this.loadAnims();
      this.screenW = DeviceConfig.screenSize[0];
      this.screenH = DeviceConfig.screenSize[1];
   }

   public final void setPanelHeights(int var1, int var2) {
      this.topPanelBg = null;
      this.bottomPanelBg = null;
      this.topPanelH = 0;
      this.bottomPanelH = 0;
      if (var1 != 0) {
         this.topPanelH = var1 - 2;
         this.topPanelBg = GameUtil.createTranslucentImage(193, var1, 2959909, 85);
      }

      if (var2 > 0) {
         this.bottomPanelH = var2 - 2;
         this.bottomPanelBg = GameUtil.createTranslucentImage(193, var2, 657930, 85);
      }
   }

   private void loadAnims() {
      if (elementWheelAnim == null) {
         (elementWheelAnim = new AnimSprite()).load("/pic/D01.av");
      }

      if (setGlowAnims == null) {
         setGlowAnims = new AnimSprite[6];

         for (int var1 = 0; var1 < 6; var1++) {
            setGlowAnims[var1] = new AnimSprite();
            setGlowAnims[var1].load("/pic/equipstar/tbss.av");
         }
      }

      InputStream var2 = Main.self.getClass().getResourceAsStream("/pic/D41.av");
      if (this.speakerAnim == null) {
         this.speakerAnim = new AnimSprite();
         this.speakerAnim.load("/pic/D41.av");
      }

      if (var2 == null) {
         this.speakerAnim = null;
      }

      previewAnim = new AnimSprite();
      int var3 = hero.facePath.indexOf(".");
      String var4 = hero.facePath.substring(0, var3) + "s.av";
      previewAnim.load(var4);
      if (this.dexFilterCursorAnim == null) {
         this.dexFilterCursorAnim = new AnimSprite();
         this.dexFilterCursorAnim.load("/pic/D24.av");
      }

      if (this.implantFxLeft == null) {
         this.implantFxLeft = new AnimSprite();
         this.implantFxLeft.load("/pic/uiav/D28.av");
         this.implantFxRight = new AnimSprite();
         this.implantFxRight.isMirrored = true;
         this.implantFxRight.load("/pic/uiav/D28.av");
      }
   }

   public final void showPage(byte var1, byte var2) {
      menuPage = var1;
      if (this.softKeyFrame != 0) {
         byte var3 = 0;
         this.softKeyFrame = var3;
      }

      if (menuPage == 1) {
         boolean var20 = false;
         MenuTabBar var5 = this.tabBar;
         this.tabBar.hideArrows = var20;
      } else {
         boolean var21 = true;
         MenuTabBar var6 = this.tabBar;
         this.tabBar.hideArrows = var21;
      }

      if (menuPage != 1) {
         if (menuPage == 3) {
            petPage = (byte)var2;
            this.setPanelHeights(90, this.bodyH - 90);
            if (petPage == 0) {
               this.buildPetList();
               if (this.petList != null) {
                  Image var22 = this.cursorArrow;
                  PetListPanel var7 = this.petList;
                  this.petList.cursorImg = var22;
                  return;
               }
            } else if (petPage == 7) {
               if (this.implantStep == 0) {
                  this.setPanelHeights(this.bodyH, 0);
                  return;
               }
            } else {
               if (petPage == 1) {
                  Object var24 = null;
                  PetListPanel var10 = this.petList;
                  this.petList.cursorImg = (Image)var24;
                  return;
               }

               if (petPage == 5) {
                  this.setPanelHeights(90, this.bodyH - 90);
                  this.petSkillList = new MenuList(
                     this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2, this.tabBarBounds[2], this.bottomPanelH - 14
                  );
                  int[][] var8 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
                  TextListPanel var17 = new TextListPanel((byte)1, var8);
                  IconListPanel var23 = new IconListPanel(true);
                  if (Skill.names == null) {
                     Skill.loadTable();
                  }

                  if (pets[this.petList.getSelectedSlot()].skills != null) {
                     this.petSkillList.rowIds = new int[pets[this.petList.getSelectedSlot()].skills.length];

                     for (int var9 = 0; var9 < pets[this.petList.getSelectedSlot()].skills.length; var9++) {
                        int var4 = GameData.findSkillIndex(pets[this.petList.getSelectedSlot()].skills[var9].skillId);
                        var17.addText(Skill.names[var4] + "  " + pets[this.petList.getSelectedSlot()].skills[var9].level + " cấp");
                        Image var31 = ResourceCache.loadImage("/icon/" + Skill.iconNames[var4] + ".gf");
                        this.petSkillList.rowIds[var9] = pets[this.petList.getSelectedSlot()].skills[var9].skillId;
                        var23.addIcon(var31);
                     }

                     var23.setRowHeight(Utils.lineHeight + 3);
                     var17.setRowHeight(Utils.lineHeight + 3);
                     var23.setBounds(this.tabBarBounds[0] + 10, this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2, 40, this.bottomPanelH - 22);
                     var17.setBounds(
                        this.tabBarBounds[0] + 20,
                        this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2,
                        this.tabBarBounds[2],
                        this.bottomPanelH - 22
                     );
                     this.petSkillList.addColumn(var23);
                     this.petSkillList.addColumn(var17);
                     if (this.cursorArrow != null) {
                        this.petSkillList.setCursor(this.cursorArrow, this.tabBarBounds[0]);
                     }
                  }

                  return;
               }
            }
         } else {
            if (menuPage == 2) {
               this.setPanelHeights(90, this.bodyH - 90);
               return;
            }

            if (menuPage == 5) {
               gemRows = 3;
               Equip.loadTable();
               Equip.loadIcons();
               equipPage = 0;
               if (hero.roleId == 1) {
                  setItemIds = new short[]{14, 26, 38, 44, 51, 58};
               } else if (hero.roleId == 2) {
                  setItemIds = new short[]{7, 20, 32, 45, 52, 59};
               } else if (hero.roleId == 3) {
                  setItemIds = new short[]{7, 20, 32, 45, 52, 59};
               }

               setPiecesWorn = 0;

               for (int var16 = 0; var16 < hero.equipIds.length; var16++) {
                  if (Utils.contains(setItemIds, hero.equipIds[var16])) {
                     setPiecesWorn++;
                  }
               }

               equipPage = (byte)var2;
               if (var2 == 3) {
                  this.setPanelHeights(60, this.bodyH - 60);
                  return;
               }

               this.setPanelHeights(90, this.bodyH - 90);
               return;
            }

            if (menuPage == 4) {
               this.setPanelHeights(90, this.bodyH - 90);
               skillPage = 0;
               this.heroSkillList.setCursor(this.cursorArrow, this.tabBarBounds[0]);
               return;
            }

            if (menuPage == 7) {
               questPage = (byte)var2;
               this.setPanelHeights(30, this.bodyH - 30);
               if (questPage == 0) {
                  this.buildQuestLists();
                  return;
               }

               if (questPage == 2) {
                  Image var26 = this.cursorArrow;
                  SideQuestListPanel var14 = this.sideQuestList;
                  this.sideQuestList.cursorImg = var26;
                  return;
               }

               if (questPage == 1) {
                  Image var25 = this.cursorArrow;
                  MainQuestListPanel var13 = this.mainQuestList;
                  this.mainQuestList.cursorImg = var25;
                  return;
               }

               if (questPage == 3) {
                  this.questDetail = new QuestDetailBox(
                     this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH, this.tabBarBounds[2], this.bottomPanelH
                  );
                  int[] var11 = new int[]{16514974, 10571302, 16705870, 9194010};
                  QuestDetailBox var12 = this.questDetail;
                  this.questDetail.colors = var11;
                  this.questDetail
                     .setContent(
                        "Mô tả nhiệm vụ", GameData.activeQuests[this.questIndex].description, "Thưởng nhiệm vụ", GameData.activeQuests[this.questIndex].reward
                     );
                  return;
               }
            } else if (menuPage == 6) {
               GameData.loadItemTable();
               GameData.loadItemIcons();
               buildBagItems();
               bagTabIndex = 0;
               bagScrollRow = 0;
               bagItemIndex = 0;
               bagQuantity = 1;
               bagPage = 0;
               this.setPanelHeights(30, this.bodyH - 30);
               if (this.bagTabsImg == null) {
                  this.bagTabsImg = ResourceCache.loadImage("/pic/goodsPc.gf");
               }

               if (this.bagTabsSelImg == null) {
                  this.bagTabsSelImg = ResourceCache.loadImage("/pic/goodsPc1.gf");
                  return;
               }
            } else if (menuPage == 9) {
               this.setPanelHeights(0, this.bodyH - 2);
               this.leftRightArrows = ResourceCache.loadImage("/pic/D11.gf");
               this.buildSystemMenu();
               this.systemMenuList.setCursor(this.cursorArrow, (DeviceConfig.screenSize[0] - 120) / 2);
               systemPage = 0;
               if (this.leftRightArrows == null) {
                  this.leftRightArrows = ResourceCache.loadImage("/pic/D11.gf");
               }

               systemPage = (byte)var2;
               if (var2 == 0) {
                  this.setPanelHeights(0, this.bodyH - 2);
                  return;
               }

               if (systemPage == 1) {
                  this.setPanelHeights(30, this.bodyH - 30);
                  return;
               }

               if (systemPage == 2) {
                  this.setPanelHeights(30, this.bodyH - 30);
                  return;
               }

               if (systemPage == 3) {
                  if (!DeviceConfig.playMusic && GameData.musicVolume <= 0) {
                     if (this.speakerAnim != null) {
                        this.speakerAnim.setAction(0);
                     }
                  } else if (this.speakerAnim != null) {
                     this.speakerAnim.setAction(1);
                  }

                  this.setPanelHeights(30, this.bodyH - 30);
                  return;
               }

               if (systemPage != 4 && systemPage == 5) {
                  return;
               }
            } else if (menuPage == 8) {
               dexPage = (byte)var2;
               if (var2 == 0) {
                  this.setPanelHeights(0, this.bodyH - 2);
                  byte var27 = 2;
                  this.softKeyFrame = var27;
                  if (this.dexAllList != null) {
                     this.dexAllList.setImages(this.cursorArrow, this.digits);
                  }

                  if (this.dexFoundList != null) {
                     this.dexFoundList.setImages(this.cursorArrow, this.digits);
                  }

                  if (this.dexElementList != null) {
                     this.dexElementList.setImages(this.cursorArrow, this.digits);
                     return;
                  }
               } else {
                  if (dexPage == 1) {
                     byte var29 = 2;
                     this.softKeyFrame = var29;
                     this.setPanelHeights(90, this.bodyH - 90);
                     return;
                  }

                  if (dexPage == 2) {
                     byte var28 = 0;
                     this.softKeyFrame = var28;
                     GameData.loadItemTable();
                     GameData.loadItemIcons();
                     this.buildFusionMaterialList();
                     return;
                  }
               }
            } else {
               if (menuPage == 13) {
                  this.setPanelHeights(0, this.bodyH - 2);
                  return;
               }

               if (menuPage == 12) {
                  this.setPanelHeights(90, this.bodyH - 90);
                  this.formList = new DexListPanel();
                  if (petData == null) {
                     petData = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
                  }

                  for (int var15 = 0; var15 < 140; var15++) {
                     var2 = var15 + 1;
                     String var30 = petData[var2 - 1][0];
                     if (Utils.contains(GameData.discoveredPetIds, var15 + 1)) {
                        this.formList.addEntry(var15 + 1, var30);
                     }
                  }

                  this.formList.setBounds(this.tabBarBounds[0] + 8, this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2], this.topPanelH);
                  this.formList.setImages(this.cursorArrow, this.digits);
                  this.formList.updatePageCount();
                  previewAnim = new AnimSprite();
                  previewAnim = GameUtil.loadBattleAnimation(this.formList.getSelectedPetId(), true);
               }
            }
         }
      }
   }

   private void buildFusionMaterialList() {
      this.fusionMaterialList = null;
      this.fusionMaterialList = new MaterialListPanel();
      if (this.fusionRecipe != null) {
         for (int var1 = 0; var1 < this.fusionRecipe.length; var1++) {
            this.fusionMaterialList
               .addMaterial(
                  GameData.itemIcons[this.fusionRecipe[var1][0] - 1],
                  GameData.itemNames[this.fusionRecipe[var1][0] - 1],
                  GameData.getItemCount(this.fusionRecipe[var1][0]),
                  this.fusionRecipe[var1][1]
               );
         }
      }

      this.fusionMaterialList
         .setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 10, this.tabBarBounds[2], this.bottomPanelH);
      Image var2 = this.cursorArrow;
      MaterialListPanel var3 = this.fusionMaterialList;
      this.fusionMaterialList.cursorImg = var2;
   }

   public final void buildPetList() {
      if (this.petList != null) {
         this.petList = null;
      }

      this.petList = new PetListPanel();
      Object var2 = null;
      PetListPanel var1 = this.petList;
      this.petList.cursorImg = (Image)var2;
      this.petList.setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2], this.topPanelH);
      if (pets != null) {
         for (int var3 = 0; var3 < pets.length; var3++) {
            this.petList.addEntry(pets[var3].petId, var3, pets[var3].isDeployed, pets[var3].name, pets[var3].stats[0]);
         }

         this.petAnim = new AnimSprite();
         this.petAnim = GameUtil.loadBattleAnimation(pets[this.petList.getSelectedSlot()].petId, true);
      }
   }

   private void initEquipTab() {
      gemRows = 3;
      Equip.loadTable();
      Equip.loadIcons();
      this.heroFigureAnim = new AnimSprite();
      this.heroFigureAnim.load("/ft/chr" + hero.roleId + ".av");
      equipSlotIndex = 0;
      equipPage = 0;
      if (hero.roleId == 1) {
         setItemIds = new short[]{14, 26, 38, 44, 51, 58};
      } else if (hero.roleId == 2) {
         setItemIds = new short[]{7, 20, 32, 45, 52, 59};
      }

      setPiecesWorn = 0;

      for (int var1 = 0; var1 < hero.equipIds.length; var1++) {
         if (Utils.contains(setItemIds, hero.equipIds[var1])) {
            setPiecesWorn++;
         }
      }
   }

   private void buildHeroSkillList() {
      Skill.loadTable();
      Skill.loadIcons();
      if (this.heroSkillList != null) {
         this.heroSkillList = null;
      }

      this.heroSkillList = new MenuList(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + 94, this.tabBarBounds[2], this.bottomPanelH - 22);
      int[][] var1 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
      TextListPanel var5 = new TextListPanel((byte)1, var1);
      IconListPanel var2 = new IconListPanel(true);
      if (hero.skills != null) {
         this.heroSkillList.rowIds = new int[hero.skills.length];

         for (int var3 = 0; var3 < hero.skills.length; var3++) {
            int var4 = GameData.findSkillIndex(hero.skills[var3].skillId);
            this.heroSkillList.rowIds[var3] = hero.skills[var3].skillId;
            var5.addText(Skill.names[var4] + " " + hero.skills[var3].level + " cấp");
            var2.addIcon(Skill.icons[var4]);
         }

         var2.setRowHeight(Utils.lineHeight + 3);
         var5.setRowHeight(Utils.lineHeight + 3);
         var2.setBounds(this.tabBarBounds[0] + 10, this.tabBarBounds[1] + this.tabBarBounds[3] + 90 + 2, 40, this.bottomPanelH - 22);
         var5.setBounds(this.tabBarBounds[0] + 20, this.tabBarBounds[1] + this.tabBarBounds[3] + 90 + 2, this.tabBarBounds[2], this.bottomPanelH - 22);
         this.heroSkillList.addColumn(var2);
         this.heroSkillList.addColumn(var5);
         if (this.cursorArrow != null) {
            this.heroSkillList.setCursor(null, this.tabBarBounds[0]);
         }

         heroSkillCount = (short)hero.skills.length;
      }
   }

   public final void buildQuestLists() {
      if (this.sideQuestList != null) {
         this.sideQuestList.clear();
         this.sideQuestList = null;
      }

      if (this.mainQuestList != null) {
         this.mainQuestList.clear();
         this.mainQuestList = null;
      }

      this.mainQuestList = new MainQuestListPanel();
      Object var2 = null;
      MainQuestListPanel var1 = this.mainQuestList;
      this.mainQuestList.cursorImg = (Image)var2;
      this.sideQuestList = new SideQuestListPanel();
      var2 = null;
      SideQuestListPanel var3 = this.sideQuestList;
      this.sideQuestList.cursorImg = (Image)var2;

      for (int var4 = 0; GameData.activeQuests != null && var4 < GameData.activeQuests.length; var4++) {
         questNames = Utils.append(questNames, GameData.activeQuests[var4].name);
         if (GameData.activeQuests[var4].type == 1) {
            this.mainQuestList.addQuest(GameData.activeQuests[var4].id, 0, GameData.activeQuests[var4].name);
         } else if (GameData.activeQuests[var4].type == 2 || GameData.activeQuests[var4].type == 3) {
            this.sideQuestList.addQuest(var4, GameData.activeQuests[var4].type, GameData.activeQuests[var4].name, GameData.activeQuests[var4].isCompleted);
         }
      }

      if (GameData.finishedQuestIds != null && GameData.finishedQuestIds.length > 0) {
         for (int var5 = 0; var5 < GameData.finishedQuestIds.length; var5++) {
            if (((Quest)(var2 = GameData.createQuest(GameData.finishedQuestIds[var5], -1))).type == 1) {
               this.mainQuestList.addQuest(((Quest)var2).id, 1, ((Quest)var2).name);
            }
         }
      }

      this.sideQuestList.setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + 30, this.tabBarBounds[2], this.bodyH - 30);
      this.mainQuestList.setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + 50, this.tabBarBounds[2], this.bodyH - 60);
   }

   public final void reloadBag() {
      GameData.loadItemTable();
      GameData.loadItemIcons();
      buildBagItems();
   }

   private void buildSystemMenu() {
      if (this.systemMenuList == null) {
         if (DeviceConfig.allowMusic) {
            if (systemMenuRows == null) {
               systemMenuRows = new String[]{"Dịch vụ ưu đãi", "Lưu game", "Tải game", "Cài đặt nhạc", "Tải điểm QQ", "Thoát game"};
            }
         } else if (systemMenuRows == null) {
            systemMenuRows = new String[]{"Dịch vụ ưu đãi", "Lưu game", "Tải game", "Thoát game"};
         }

         int var1 = systemMenuRows.length * (Utils.lineHeight + 5);
         this.systemMenuList = new MenuList(
            (DeviceConfig.screenSize[0] - 120) / 2, this.tabBarBounds[1] + this.tabBarBounds[3] + (this.topPanelH + this.bottomPanelH - var1) / 2, 120, var1
         );
         int[][] var2 = new int[][]{{13695224, 1393321}, {13629944, 13629944}};
         TextListPanel var4 = new TextListPanel((byte)1, var2);

         for (int var3 = 0; var3 < systemMenuRows.length; var3++) {
            var4.addText(systemMenuRows[var3]);
         }

         var4.setBounds(
            (DeviceConfig.screenSize[0] - 120) / 2, this.tabBarBounds[1] + this.tabBarBounds[3] + (this.topPanelH + this.bottomPanelH - var1) / 2, 120, var1
         );
         var4.setRowHeight(Utils.lineHeight + 5);
         this.systemMenuList.setCursor(null, (DeviceConfig.screenSize[0] - 120) / 2 - 10);
         this.systemMenuList.addColumn(var4);
      }
   }

   public final void showPreview(byte var1) {
      this.previewPage = var1;
      if (this.softKeyFrame != 0) {
         byte var3 = 0;
         this.softKeyFrame = var3;
      }

      if (var1 == 1) {
         this.setPanelHeights(90, this.bodyH - 90);
         previewAnim = null;
         previewAnim = new AnimSprite();
         var1 = hero.facePath.indexOf(".");
         String var9 = hero.facePath.substring(0, var1) + "s.av";
         previewAnim.load(var9);
      } else if (var1 == 3) {
         petPage = 0;
         this.setPanelHeights(90, this.bodyH - 90);
         this.buildPetList();
      } else if (var1 == 2) {
         this.setPanelHeights(90, this.bodyH - 90);
         previewAnim = null;
         previewAnim = new AnimSprite();
         var1 = hero.facePath.indexOf(".");
         String var7 = hero.facePath.substring(0, var1) + "s.av";
         previewAnim.load(var7);
      } else if (var1 == 5) {
         (previewAnim = new AnimSprite()).load("/ft/chr" + hero.roleId + ".av");
         equipPage = 0;
         if (0 == 3) {
            this.setPanelHeights(60, this.bodyH - 60);
         } else {
            this.setPanelHeights(90, this.bodyH - 90);
         }
      } else if (var1 == 4) {
         this.setPanelHeights(90, this.bodyH - 90);
         previewAnim = null;
         previewAnim = new AnimSprite();
         var1 = hero.facePath.indexOf(".");
         String var5 = hero.facePath.substring(0, var1) + "s.av";
         previewAnim.load(var5);
      } else if (var1 == 7) {
         this.setPanelHeights(30, this.bodyH - 30);
         questPage = 0;
      } else if (var1 == 6) {
         this.setPanelHeights(30, this.bodyH - 30);
         bagTabIndex = 0;
         bagScrollRow = 0;
         bagItemIndex = 0;
         bagQuantity = 1;
         bagPage = 0;
      } else {
         if (var1 == 9) {
            this.setPanelHeights(0, this.bodyH - 2);
            systemPage = 0;
            systemPage = 0;
            if (true) {
               this.setPanelHeights(0, this.bodyH - 2);
               return;
            }
         } else {
            if (var1 == 8) {
               dexPage = 0;
               this.setPanelHeights(0, this.bodyH - 2);
               return;
            }

            if (var1 == 13) {
               this.setPanelHeights(0, this.bodyH - 2);
            }
         }
      }
   }

   public final void loadDexPet(int var1) {
      dexElements = null;
      dexStatStars = null;
      previewAnim = new AnimSprite();
      previewAnim = GameUtil.loadBattleAnimation(var1, false);
      String[] var2;
      short[][] var3 = new short[(var2 = Utils.split(petData[var1 - 1][19], "|")).length][2];

      for (int var4 = 0; var4 < var2.length; var4++) {
         short[] var5 = Utils.parseShortArray(var2[var4], ",");
         var3[var4][0] = var5[0];
         var3[var4][1] = var5[1];
      }

      this.fusionRecipe = var3;
      PortalProtocol.dumpArray(this.fusionRecipe, "Hợp thành " + var1);
      if (petData[var1 - 1][2] != null) {
         for (int var6 = 0; var6 < 4; var6++) {
            String var8;
            if ((var8 = petData[var1 - 1][2].substring(var6, var6 + 1)).equals("a")) {
               dexElements = Utils.append(dexElements, 10);
            } else {
               dexElements = Utils.append(dexElements, Utils.parseInt(var8));
            }
         }
      }

      for (int var9 = 0; var9 < 5; var9++) {
         byte[] var7 = Utils.parseByteArray(petData[var1 - 1][4 + var9 * 2], "|");
         dexStatStars = Utils.append(dexStatStars, var7[0]);
      }

      if (GameData.newPetFlags[var1 - 1]) {
         GameData.newPetFlags[var1 - 1] = false;
      }

      dexPetIndex = var1 - 1;
   }

   public final void buildDexLists() {
      dexPetIndex = 0;
      if (petData == null) {
         petData = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
      }

      this.dexAllList = new DexListPanel();
      short[] var1 = null;
      short[] var2 = null;
      short[] var3 = null;
      short[] var4 = null;
      dexNames = null;
      this.dexFoundList = null;
      this.dexElementList = null;

      for (int var6 = 0; var6 < petData.length; var6++) {
         int var7 = var6;
         String var8 = petData[var6][0];
         short[] var5 = null;

         for (int var9 = 0; var9 < 4; var9++) {
            String var10;
            if ((var10 = petData[var7][2].substring(var9, var9 + 1)).equals("a")) {
               var5 = Utils.append(var5, 10);
            } else {
               var5 = Utils.append(var5, Utils.parseInt(var10));
            }
         }

         if (var5[0] > 5) {
            var1 = Utils.append(var1, var6);
         }

         if (var5[1] > 5) {
            var2 = Utils.append(var2, var6);
         }

         if (var5[2] > 5) {
            var3 = Utils.append(var3, var6);
         }

         if (var5[3] > 5) {
            var4 = Utils.append(var4, var6);
         }

         if (Utils.contains(GameData.discoveredPetIds, var6 + 1)) {
            if (this.dexFoundList == null) {
               this.dexFoundList = new DexListPanel();
            }

            this.dexFoundList.addEntry(var6 + 1, var8);
            dexNames = Utils.append(dexNames, var8);
            this.dexAllList.addEntry(var6 + 1, var8);
         } else {
            dexNames = Utils.append(dexNames, "???");
            this.dexAllList.addEntry(var6 + 1, "???");
         }
      }

      if (this.dexElementList == null) {
         this.dexElementList = new DexListPanel();
      }

      short[] var11 = Utils.appendAll(Utils.appendAll(Utils.appendAll(Utils.appendAll(null, var1), var2), var3), var4);

      for (int var12 = 0; var12 < var11.length; var12++) {
         short var13 = var11[var12];
         String var14 = petData[var13][0];
         if (Utils.contains(GameData.discoveredPetIds, var11[var12] + 1)) {
            this.dexElementList.addEntry(var11[var12] + 1, var14);
         } else {
            this.dexElementList.addEntry(var11[var12] + 1, "???");
         }
      }

      if (this.dexFoundList != null) {
         this.dexFoundList
            .setBounds(
               this.tabBarBounds[0],
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 8,
               this.tabBarBounds[2],
               this.bodyH - 2 * (Utils.lineHeight + 8)
            );
         this.dexFoundList.setImages(null, this.digits);
         this.dexFoundList.updatePageCount();
      }

      this.dexAllList
         .setBounds(
            this.tabBarBounds[0],
            this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 8,
            this.tabBarBounds[2],
            this.bodyH - 2 * (Utils.lineHeight + 8)
         );
      this.dexAllList.setImages(null, this.digits);
      this.dexAllList.updatePageCount();
      this.dexElementList
         .setBounds(
            this.tabBarBounds[0],
            this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 8,
            this.tabBarBounds[2],
            this.bodyH - 2 * (Utils.lineHeight + 8)
         );
      this.dexElementList.setImages(null, this.digits);
      this.dexElementList.updatePageCount();
   }

   public static void loadShopStock(int var0) {
      String var1 = Utils.loadConfigText("/bin/shop.bin");
      hero = GameData.party[0];
      pets = null;
      pets = GameUtil.a_APetInfo();
      short[] var2 = null;
      if (shopType == 0) {
         var2 = GameUtil.getShortList(var1, "itemShop" + var0 + ":", "end", ",", "lib");
      } else if (shopType == 1) {
         var2 = GameUtil.getShortList(var1, "equipShop" + var0 + ":", "end", ",", "lib");
      } else if (shopType == 2) {
         var2 = GameUtil.getShortList(var1, "skillShop" + var0 + ":", "end", ",", "lib");
      }

      if (var2 != null) {
         for (int var3 = 0; var3 < var2.length; var3++) {
            shopItemIds = Utils.append(shopItemIds, var2[var3]);
         }
      }
   }

   public final void releaseData() {
      elementWheelAnim = null;
      setGlowAnims = null;
      petData = null;
      setItemIds = null;
      saveSlotAnims = null;
      previewAnim = null;
      bagItems = null;
      shopItemIds = null;
      systemMenuRows = null;
      equipActionNames = null;
      petActionNames = null;
      questNames = null;
      if (dexNames != null) {
         dexNames = null;
      }

      if (bagActionNames != null) {
         bagActionNames = null;
      }

      if (this.heroSkillList != null) {
         this.heroSkillList = null;
      }

      if (this.petSkillList != null) {
         this.petSkillList = null;
      }

      if (this.dexFoundList != null) {
         this.dexFoundList = null;
      }

      if (this.dexElementList != null) {
         this.dexElementList = null;
      }

      if (this.formList != null) {
         this.formList = null;
      }

      if (this.petList != null) {
         this.petList = null;
      }

      if (this.partyList != null) {
         this.partyList = null;
      }
   }

   public final void resetListCursors() {
      if (this.partyList != null) {
         Object var2 = null;
         PetListPanel var1 = this.partyList;
         this.partyList.cursorImg = (Image)var2;
         this.partyList.setSelectedIndex(0);
      }

      if (this.petList != null) {
         Object var4 = null;
         PetListPanel var3 = this.petList;
         this.petList.cursorImg = (Image)var4;
         if (this.petList.itemCount() > 0) {
            this.petList.setSelectedIndex(0);
         }
      }

      if (this.heroSkillList != null) {
         this.heroSkillList.setCursor(null, this.tabBarBounds[0]);
         this.heroSkillList.resetSelection(0);
      }

      if (this.systemMenuList != null) {
         this.systemMenuList.setCursor(null, this.systemMenuList.x);
      }

      if (this.dexAllList != null) {
         this.dexAllList.setImages(null, this.digits);
         this.dexAllList.setSelectedIndex(0);
      }

      if (this.dexFoundList != null) {
         this.dexFoundList.setImages(null, this.digits);
         this.dexFoundList.setSelectedIndex(0);
      }

      if (this.dexElementList != null) {
         this.dexElementList.setImages(null, this.digits);
         this.dexElementList.setSelectedIndex(0);
      }
   }

   public final void buildLearnerList() {
      if (skillLearnerType == 4) {
         if (this.petList != null) {
            this.petList = null;
         }

         this.petList = new PetListPanel();
         Object var2 = null;
         PetListPanel var1 = this.petList;
         this.petList.cursorImg = (Image)var2;
         this.petList.setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2], this.topPanelH);
         if (pets != null) {
            for (int var3 = 0; var3 < pets.length; var3++) {
               this.petList.addEntry(pets[var3].petId, var3, pets[var3].isDeployed, pets[var3].name, pets[var3].stats[0]);
            }

            return;
         }
      } else {
         if (this.partyList != null) {
            this.partyList = null;
         }

         this.partyList = new PetListPanel();
         Object var6 = null;
         PetListPanel var4 = this.partyList;
         this.partyList.cursorImg = (Image)var6;
         this.partyList.setBounds(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2], this.topPanelH);
         if (GameData.party != null) {
            for (int var5 = 0; var5 < GameData.party.length; var5++) {
               this.partyList.addEntry(GameData.party[var5].roleId, var5, false, GameData.party[var5].name, GameData.party[var5].stats[0]);
            }
         }
      }
   }

   public final void releaseImages() {
      this.topPanelBg = null;
      this.bottomPanelBg = null;
      this.footerBg = null;
      petData = null;
      elementWheelAnim = null;
      bagItems = null;
   }

   public final void initPages() {
      if (petData == null) {
         petData = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
      }

      if (menuPage == 14) {
         boolean var1 = true;
         MenuTabBar var6 = this.tabBar;
         this.tabBar.isSelectMode = true;
         this.setPanelHeights(160, this.bodyH - 160);
         if (Skill.names == null) {
            Skill.loadTable();
         }

         if (Skill.icons == null) {
            Skill.loadIcons();
         }

         pets = null;
         pets = GameUtil.a_APetInfo();
         skillShopPage = 0;
         skillShopList = new MenuList(
            this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 6, this.tabBarBounds[2], this.topPanelH - Utils.lineHeight
         );
         int[][] var7 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
         TextListPanel var8 = new TextListPanel((byte)1, var7);
         IconListPanel var2 = new IconListPanel(true);
         ValueListPanel var3 = new ValueListPanel((byte)1, 10, this.goldDigits);
         skillShopList.rowIds = new int[shopItemIds.length];
         String var5 = "Tiệm kỹ năng";
         short[] var4 = shopItemIds;
         if (shopItemIds == null) {
            System.out.println(var5 + " is null!");
         } else {
            System.out.println(var5 + "={");

            for (int var22 = 0; var22 < var4.length; var22++) {
               if (var22 < var4.length - 1) {
                  System.out.println("" + var4[var22] + ",");
               } else {
                  System.out.println("" + var4[var22]);
               }
            }

            System.out.println(" }");
            System.out.println("length=" + var4.length);
         }

         for (int var19 = 0; var19 < shopItemIds.length; var19++) {
            var2.addIcon(Skill.icons[GameData.findSkillIndex(shopItemIds[var19])]);
            var8.addText(Skill.names[GameData.findSkillIndex(shopItemIds[var19])]);
            var3.addText(String.valueOf(Skill.learnPrices[GameData.findSkillIndex(shopItemIds[var19])]));
            skillShopList.rowIds[var19] = shopItemIds[var19];
         }

         var2.setRowHeight(Utils.lineHeight + 8);
         var8.setRowHeight(Utils.lineHeight + 8);
         var3.setRowHeight(Utils.lineHeight + 8);
         var2.setBounds(
            this.tabBarBounds[0] + 10,
            this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
            this.tabBarBounds[2],
            this.topPanelH - Utils.lineHeight
         );
         var8.setBounds(
            this.tabBarBounds[0] + 35,
            this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
            this.tabBarBounds[2] / 2,
            this.topPanelH - Utils.lineHeight
         );
         var3.setBounds(
            this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4,
            this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
            this.tabBarBounds[2] / 4,
            this.topPanelH - Utils.lineHeight
         );
         skillShopList.addColumn(var2);
         skillShopList.addColumn(var8);
         skillShopList.addColumn(var3);
         skillShopList.setCursor(this.cursorArrow, this.tabBarBounds[0]);
      } else if (menuPage == 15) {
         boolean var9 = true;
         MenuTabBar var10 = this.tabBar;
         this.tabBar.isSelectMode = true;
         this.setPanelHeights(160, this.bodyH - 160);
         if (shopType == 0) {
            if (GameData.itemNames == null) {
               GameData.loadItemTable();
            }

            if (GameData.itemIcons == null) {
               GameData.loadItemIcons();
            }

            shopList = new MenuList(
               this.tabBarBounds[0],
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2],
               this.topPanelH - Utils.lineHeight
            );
            int[][] var11 = new int[][]{{16580583, 6036810}, {16580583, 5247562}};
            TextListPanel var12 = new TextListPanel((byte)1, var11);
            IconListPanel var15 = new IconListPanel(true);
            ValueListPanel var17 = new ValueListPanel((byte)1, 10, this.goldDigits);
            shopList.rowIds = new int[shopItemIds.length];

            for (int var20 = 0; var20 < shopItemIds.length; var20++) {
               var15.addIcon(GameData.itemIcons[GameData.findItemIndex(shopItemIds[var20])]);
               var12.addText(GameData.itemNames[GameData.findItemIndex(shopItemIds[var20])]);
               var17.addText(String.valueOf(GameData.itemBuyPrices[GameData.findItemIndex(shopItemIds[var20])]));
               shopList.rowIds[var20] = shopItemIds[var20];
            }

            var15.setRowHeight(Utils.lineHeight + 8);
            var12.setRowHeight(Utils.lineHeight + 8);
            var17.setRowHeight(Utils.lineHeight + 8);
            var15.setBounds(
               this.tabBarBounds[0] + 10,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2],
               this.topPanelH - Utils.lineHeight
            );
            var12.setBounds(
               this.tabBarBounds[0] + 35,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2] / 2,
               this.topPanelH - Utils.lineHeight
            );
            var17.setBounds(
               this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2] / 4,
               this.topPanelH - Utils.lineHeight
            );
            shopList.addColumn(var15);
            shopList.addColumn(var12);
            shopList.addColumn(var17);
            shopList.setCursor(this.cursorArrow, this.tabBarBounds[0]);
         } else if (shopType == 1) {
            if (Equip.names == null) {
               Equip.loadTable();
            }

            if (Equip.icons == null) {
               Equip.loadIcons();
            }

            shopList = new MenuList(
               this.tabBarBounds[0],
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 2,
               this.tabBarBounds[2],
               this.topPanelH - Utils.lineHeight
            );
            int[][] var13 = new int[][]{{16580583, 6036810}, {16580583, 5247562}};
            TextListPanel var14 = new TextListPanel((byte)1, var13);
            IconListPanel var16 = new IconListPanel(true);
            ValueListPanel var18 = new ValueListPanel((byte)1, 10, this.goldDigits);
            shopList.rowIds = new int[shopItemIds.length];

            for (int var21 = 0; var21 < shopItemIds.length; var21++) {
               var16.addIcon(Equip.icons[GameData.findEquipIndex(shopItemIds[var21])]);
               var14.addText(Equip.names[GameData.findEquipIndex(shopItemIds[var21])]);
               var18.addText(String.valueOf(Equip.prices[GameData.findEquipIndex(shopItemIds[var21])]));
               shopList.rowIds[var21] = shopItemIds[var21];
            }

            var16.setRowHeight(Utils.lineHeight + 8);
            var14.setRowHeight(Utils.lineHeight + 8);
            var18.setRowHeight(Utils.lineHeight + 8);
            var16.setBounds(
               this.tabBarBounds[0] + 10,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2],
               this.topPanelH - Utils.lineHeight
            );
            var14.setBounds(
               this.tabBarBounds[0] + 35,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2] / 2,
               this.topPanelH - Utils.lineHeight
            );
            var18.setBounds(
               this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4,
               this.tabBarBounds[1] + this.tabBarBounds[3] + Utils.lineHeight + 4,
               this.tabBarBounds[2] / 4,
               this.topPanelH - Utils.lineHeight
            );
            shopList.addColumn(var16);
            shopList.addColumn(var14);
            shopList.addColumn(var18);
            shopList.setCursor(this.cursorArrow, this.tabBarBounds[0]);
         }
      }

      this.buildPetList();
      this.initEquipTab();
      this.buildHeroSkillList();
      this.buildQuestLists();
      this.reloadBag();
      this.buildSystemMenu();
      this.buildDexLists();
   }

   public final void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      int[] var4 = this.tabBar.getBounds();
      int var7 = var4[0] + 1;
      int var9 = var4[1] + var4[3];
      if (this.topPanelBg != null) {
         var1.drawImage(this.topPanelBg, var7, var4[1] + var4[3], 20);
         var9 += this.topPanelH;
      }

      if (this.bottomPanelBg != null) {
         var1.drawImage(this.bottomPanelBg, var7, var9, 20);
         var9 += this.bottomPanelH;
      }

      var1.drawImage(this.footerBg, var7, var9, 20);
      int var5 = var4[0] + 3;
      int var6 = var4[1] + var4[3];
      if (this.topPanelBg != null) {
         var6 += this.topPanelH;
         var1.setColor(2303014);
         var1.fillRect(var4[0] + 2, var6 - 1, this.frameW - 2, 2);
         var1.setColor(14083300);
         var1.fillRect(var4[0] + 3, var6, this.frameW - 4, 1);
      }

      if (this.bottomPanelBg != null) {
         var6 += this.bottomPanelH;
         var1.setColor(2303014);
         var1.fillRect(var4[0] + 2, var6 - 1, this.frameW - 2, 2);
         var1.setColor(14083300);
         var1.fillRect(var4[0] + 3, var6, this.frameW - 4, 1);
      }

      var7 = var6 + this.footerH;
      var1.setColor(3027509);
      var1.drawRoundRect(var4[0] + 1, var4[1], this.frameW, var7 - var4[1], 4, 4);
      var1.setColor(12371653);
      var1.drawRoundRect(var4[0] + 2, var4[1], this.frameW - 2, var7 - var4[1] - 1, 4, 4);
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         Utils.drawFrame(
            var1,
            this.softKeyImg,
            this.softKeyImg.getWidth(),
            this.softKeyImg.getHeight() / 3,
            this.softKeyFrame,
            var5 + 6 + (this.softKeyImg.getWidth() >> 1),
            var7 - (this.footerH >> 1),
            3,
            0
         );
         Utils.drawFrame(
            var1,
            this.softKeyImg,
            this.softKeyImg.getWidth(),
            this.softKeyImg.getHeight() / 3,
            1,
            var5 + 6 + (this.softKeyImg.getWidth() >> 1) + (this.frameW - this.softKeyImg.getWidth() - 14),
            var7 - (this.footerH >> 1),
            3,
            0
         );
      } else {
         Utils.drawFrame(
            var1,
            this.softKeyImg,
            this.softKeyImg.getWidth(),
            this.softKeyImg.getHeight() / 3,
            1,
            var5 + 6 + (this.softKeyImg.getWidth() >> 1),
            var7 - (this.footerH >> 1),
            3,
            0
         );
         Utils.drawFrame(
            var1,
            this.softKeyImg,
            this.softKeyImg.getWidth(),
            this.softKeyImg.getHeight() / 3,
            this.softKeyFrame,
            var5 + 6 + (this.softKeyImg.getWidth() >> 1) + (this.frameW - this.softKeyImg.getWidth() - 14),
            var7 - (this.footerH >> 1),
            3,
            0
         );
      }

      var1.drawImage(this.footerCoinIcon, var5 + (this.softKeyImg.getWidth() << 1), var7 - (this.footerH >> 1), 3);
      Utils.drawNumber(var1, GameData.money, var5 + this.softKeyImg.getWidth() * 3, var7 - (this.footerH >> 1), this.goldDigits, 6, 8, 3);
      this.tabBar.draw(var1);
      if (menuPage == 1) {
         if (this.previewPage == 2) {
            this.drawStatusTab(var1);
         } else if (this.previewPage == 3) {
            this.drawPetTab(var1);
         } else if (this.previewPage == 9) {
            this.drawSystemTab(var1);
         } else if (this.previewPage == 5) {
            this.drawEquipTab(var1);
         } else if (this.previewPage == 6) {
            this.drawBagTab(var1);
         } else if (this.previewPage == 7) {
            this.drawQuestTab(var1);
         } else if (this.previewPage == 4) {
            this.drawSkillTab(var1);
         } else if (this.previewPage == 8) {
            this.drawDexTab(var1);
         } else if (this.previewPage == 13) {
            this.drawUseTargetList(var1);
         } else if (this.previewPage == 14) {
            this.drawSkillShop(var1);
         } else if (this.previewPage == 15) {
            this.drawShop(var1);
         } else if (this.previewPage == 12) {
            this.formList.draw(var1);
            int var2 = this.tabBarBounds[0];
            Utils.drawOutlinedText(
               var1,
               "Sẽ đổi thành hình dạng sau",
               this.tabBarBounds[0] + this.tabBarBounds[2] / 2 + 10,
               this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH,
               9240572,
               2315140,
               17
            );
            int var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + this.bottomPanelH - 20;
            int var14 = this.tabBarBounds[2];
            GameUtil.drawAnimation(var1, previewAnim, var2 + (var14 >> 1), var3, true, 0);
         }
      } else if (menuPage == 2) {
         this.drawStatusTab(var1);
      } else if (menuPage == 3) {
         this.drawPetTab(var1);
      } else if (menuPage == 9) {
         this.drawSystemTab(var1);
      } else if (menuPage == 5) {
         this.drawEquipTab(var1);
      } else if (menuPage == 6) {
         this.drawBagTab(var1);
      } else if (menuPage == 7) {
         this.drawQuestTab(var1);
      } else if (menuPage == 4) {
         this.drawSkillTab(var1);
      } else if (menuPage == 8) {
         this.drawDexTab(var1);
      } else if (menuPage == 13) {
         this.drawUseTargetList(var1);
      } else if (menuPage == 14) {
         this.drawSkillShop(var1);
      } else if (menuPage == 15) {
         this.drawShop(var1);
      } else if (menuPage == 12) {
         this.formList.draw(var1);
         int var10 = this.tabBarBounds[0];
         int var12 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + this.bottomPanelH - 20;
         int var15 = this.tabBarBounds[2];
         GameUtil.drawAnimation(var1, previewAnim, var10 + (var15 >> 1), var12, true, 0);
         Utils.drawOutlinedText(
            var1,
            "Sẽ đổi thành hình dạng sau",
            this.tabBarBounds[0] + this.tabBarBounds[2] / 2 + 10,
            this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH,
            9240572,
            2315140,
            17
         );
      }

      Graphics var13 = var1;
      GameMenu var11 = this;
      var1.translate(0, 0);
      if (this.isToastVisible) {
         if (this.toastTimer > 0) {
            if (this.toastText != null && !this.toastText.equals("")) {
               String[] var8 = Utils.wrapText(this.toastText, this.screenW - 32);
               var5 = this.screenH / 2 - var8.length * Utils.lineHeight / 2 - 5;
               var6 = this.screenW - 24;
               var7 = var8.length * Utils.lineHeight + 10;
               var1.setColor(1381651);
               var1.fillRoundRect(11, var5 - 1, var6 + 3, var7 + 3, 8, 8);
               var1.setColor(16053217);
               var1.drawRoundRect(12, var5, var6, var7, 8, 8);
               var1.setColor(6768205);
               var1.fillRect(14, var5 + 2, var6 - 3, var7 - 3);

               for (int var17 = 0; var17 < var8.length; var17++) {
                  var13.setColor(16777215);
                  vh.drawString(var13, var8[var17], var11.screenW / 2, var11.screenH / 2 - var8.length * Utils.lineHeight / 2 + var17 * Utils.lineHeight, 17);
               }
            }

            var11.toastTimer--;
            return;
         }

         this.toastText = null;
         this.isToastVisible = false;
      }
   }

   private void drawShop(Graphics var1) {
      if (shopPage == 0 || shopPage == 1) {
         if (this.textBox != null) {
            this.textBox.clear();
         } else {
            this.textBox = new TextBlock();
         }

         shopList.draw(var1);
         int var2 = this.tabBarBounds[0];
         int var4 = Utils.lineHeight + 5;
         int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
         Utils.drawOutlinedText(var1, "Tên", var2 + 10, var3 + 2, 9240572, 2315140, 20);
         Utils.drawOutlinedText(var1, "Giá", var2 + 10 + this.tabBarBounds[2] * 3 / 4, var3 + 2, 16771918, 9063443, 24);
         var1.setColor(2237991);
         var1.fillRect(var2 + 4, var3 + var4 - 1, this.tabBarBounds[2] - 8, 3);
         var1.setColor(14214117);
         var1.fillRect(var2 + 4, var3 + var4, this.tabBarBounds[2] - 8, 1);
         var3 += this.topPanelH;
         if (shopType == 0) {
            var4 = GameData.findItemIndex(shopList.getRowId(shopList.getSelectedRow()));
            this.textBox.addText(GameData.itemDescriptions[var4], this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.setBounds(var2 + 4, var3 - 2, this.tabBarBounds[2] - 10, this.bottomPanelH, this.bottomPanelH / Utils.lineHeight);
            this.textBox.draw(var1, this.textBox, 1, true, false);
         } else if (shopType == 1) {
            var4 = GameData.findEquipIndex(shopList.getRowId(shopList.getSelectedRow()));
            if (this.shopEquip == null || this.shopEquip.equipId != shopList.getRowId(shopList.getSelectedRow())) {
               this.shopEquip = new Equip(shopList.getRowId(shopList.getSelectedRow()));
            }

            this.textBox.addText(Equip.names[var4], this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.addText(Equip.descriptions[var4], this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.addText(Equip.describeStats(this.shopEquip), this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.setBounds(var2 + 4, var3 - 2, this.tabBarBounds[2] - 10, this.bottomPanelH, this.bottomPanelH / Utils.lineHeight);
            this.textBox.draw(var1, this.textBox, 1, true, false);
         }

         if (shopPage == 1) {
            short var5;
            if (shopType == 1) {
               var4 = GameData.findEquipIndex(shopList.getRowId(shopList.getSelectedRow()));
               var5 = Equip.prices[var4];
            } else {
               var4 = GameData.findItemIndex(shopList.getRowId(shopList.getSelectedRow()));
               var5 = GameData.itemBuyPrices[var4];
            }

            this.drawQuantityDialog(var1, true, 99, var5, buyCount);
         }
      }
   }

   private void drawSkillShop(Graphics var1) {
      if (skillShopPage == 0) {
         if (this.textBox != null) {
            this.textBox.clear();
         } else {
            this.textBox = new TextBlock();
         }

         int var10 = this.tabBarBounds[0];
         int var13 = Utils.lineHeight + 5;
         int var11 = this.tabBarBounds[1] + this.tabBarBounds[3];
         Utils.drawOutlinedText(var1, "Tên", var10 + 10, var11 + 2, 9240572, 2315140, 20);
         Utils.drawOutlinedText(var1, "Giá", this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4 + 10, var11 + 2, 16771918, 9063443, 24);
         var1.setColor(2237991);
         var1.fillRect(var10 + 4, var11 + var13 - 1, this.tabBarBounds[2] - 8, 3);
         var1.setColor(14214117);
         var1.fillRect(var10 + 4, var11 + var13, this.tabBarBounds[2] - 8, 1);
         if (skillShopList != null && skillShopList.getColumnCount() > 0) {
            skillShopList.draw(var1);
            var11 += this.topPanelH;
            String var14 = "";
            int var6 = GameData.findSkillIndex(skillShopList.getRowId(skillShopList.getSelectedRow()));
            short[][] var7 = Skill.learnRules[var6];
            int var8 = 0;

            for (int var9 = var7.length; var8 < var9; var8++) {
               if (var7[var8][0] == skillLearnerType) {
                  var14 = var14 + "‚ Cấp:" + var7[var8][1];
                  break;
               }
            }

            String var15 = var14 + "; Cần vàng:" + Skill.learnPrices[var6] + ".";
            this.textBox.addText(Skill.descriptions[var6] + var15, this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.setBounds(var10 + 4, var11, this.tabBarBounds[2] - 10, this.bottomPanelH, this.bottomPanelH / Utils.lineHeight);
            this.textBox.draw(var1, this.textBox, 1, true, false);
         }
      } else {
         if (skillShopPage == 1 || skillShopPage == 2) {
            this.drawLearnerList(var1);
            if (skillShopPage == 2) {
               var1.setColor(1381651);
               int var2 = this.tabBarBounds[0] + this.tabBarBounds[2] / 4;
               int var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.screenH / 4;
               int var4 = this.tabBarBounds[2] / 2 + 10;
               var1.fillRoundRect(var2 - 2, var3 - 2, var4 + 3, 53, 8, 8);
               var1.setColor(16053217);
               var1.drawRoundRect(var2, var3, var4, 50, 8, 8);
               var1.setColor(6702925);
               var1.fillRect(var2 + 2, var3 + 2, var4 - 3, 47);
               var1.setColor(14017767);
               vh.drawString(var1, "Xác nhận học kỹ năng", var2 + var4 / 2, var3, 17);
               int var5 = GameData.findSkillIndex(skillShopList.getRowId(skillShopList.getSelectedRow()));
               Utils.drawOutlinedText(var1, Skill.names[var5], var2 + 4, var3 + 15 + 2, 16510027, 8866072, 20);
               var1.setColor(14017767);
               vh.drawString(var1, " ?", var2 + 8 + vh.stringWidth(Utils.defaultFont, Skill.names[var5]), var3 + 15 + 2, 17);
               if (DeviceConfig.modelName.equals("E2") || DeviceConfig.modelName.equals("V8")) {
                  Utils.drawFrame(
                     var1, this.confirmCancelImg, this.confirmCancelImg.getWidth() / 2, this.confirmCancelImg.getHeight(), 1, var2 + 4, var3 + 50 - 4, 36, 0
                  );
                  Utils.drawFrame(
                     var1,
                     this.confirmCancelImg,
                     this.confirmCancelImg.getWidth() / 2,
                     this.confirmCancelImg.getHeight(),
                     0,
                     var2 + var4 - 4,
                     var3 + 50 - 4,
                     40,
                     0
                  );
                  return;
               }

               Utils.drawFrame(
                  var1, this.confirmCancelImg, this.confirmCancelImg.getWidth() / 2, this.confirmCancelImg.getHeight(), 0, var2 + 4, var3 + 50 - 4, 36, 0
               );
               Utils.drawFrame(
                  var1,
                  this.confirmCancelImg,
                  this.confirmCancelImg.getWidth() / 2,
                  this.confirmCancelImg.getHeight(),
                  1,
                  var2 + var4 - 4,
                  var3 + 50 - 4,
                  40,
                  0
               );
            }
         }
      }
   }

   private void drawLearnerList(Graphics var1) {
      int var2 = this.bottomPanelH;
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
      int var4 = 1;
      int var5 = this.tabBarBounds[0] + 8;
      if (skillLearnerType == 1 || skillLearnerType == 2) {
         var4 = 1;
      } else if (skillLearnerType == 4 && pets != null) {
         var4 = pets.length;
      }

      short var6 = (short)(Utils.lineHeight << 1);
      learnerRows = (byte)(var2 / var6);
      short var7;
      if (var2 % var6 != 0) {
         var7 = (short)(0 + var2 % var6 / 2 + var3);
      } else {
         var7 = (short)(0 + var3 + 0);
      }

      if (var4 > learnerRows) {
         GameUtil.drawScrollBar(var1, null, null, this.tabBarBounds[0] + this.tabBarBounds[2] - 10, var7, var6 * learnerRows, var4, learnerRows, learnerScroll);
      }

      if (skillLearnerType == 1 || skillLearnerType == 2) {
         Utils.drawOutlinedText8(var1, hero.name, this.tabBarBounds[0] + 5, var7, 9502459, 540331, 20);
         Utils.drawNumber(var1, hero.stats[0], this.screenW / 2 + 26 + 30, var7 + Utils.lineHeight / 2, this.digits, 6, 7, 3);
         if (this.levelImg == null) {
            this.levelImg = ResourceCache.loadImage("/pic/D08.gf");
         }

         if (this.levelImg != null) {
            var1.drawImage(this.levelImg, this.screenW / 2 + 26 + 50, var7 + Utils.lineHeight / 2, 3);
         }

         if (this.attrIcons == null) {
            this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
         }

         Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 0, var5 + 10, var7 + 6 + Utils.lineHeight + 4, 3, 0);
         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4, 20);
         Utils.drawNumber(var1, hero.stats[3], var5 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4, this.digits, 6, 7, 24);
         Utils.drawNumber(var1, hero.stats[10], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + Utils.lineHeight + 4, this.digits, 6, 7, 20);
         Utils.drawFrame(
            var1,
            this.attrIcons,
            this.attrIcons.getWidth(),
            this.attrIcons.getHeight() / 10,
            1,
            var5 + 6 + this.tabBarBounds[2] / 2,
            var7 + 6 + Utils.lineHeight + 4,
            3,
            0
         );
         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, this.screenW / 2 + 26 + 6 + 24 + 2, var7 + 3 + Utils.lineHeight + 4, 20);
         Utils.drawNumber(var1, hero.stats[5], this.screenW / 2 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4, this.digits, 6, 7, 24);
         Utils.drawNumber(var1, hero.stats[11], this.screenW / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + Utils.lineHeight + 4, this.digits, 6, 7, 20);
      } else if (skillLearnerType == 4) {
         for (int var8 = 0; var8 < learnerRows; var8++) {
            if (learnerScroll + var8 < pets.length) {
               Utils.drawOutlinedText8(var1, pets[learnerScroll + var8].name, this.tabBarBounds[0] + 5, var7 + var6 * var8, 9502459, 540331, 20);
               if (this.attrIcons == null) {
                  this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
               }

               if (this.levelImg == null) {
                  this.levelImg = ResourceCache.loadImage("/pic/D08.gf");
               }

               if (this.levelImg != null) {
                  var1.drawImage(this.levelImg, this.screenW / 2 + 26 + 50, var7 + var6 * var8 + Utils.lineHeight / 2, 3);
               }

               Utils.drawNumber(
                  var1, pets[learnerScroll + var8].stats[0], this.screenW / 2 + 26 + 30, var7 + var6 * var8 + Utils.lineHeight / 2, this.digits, 6, 7, 3
               );
               Utils.drawFrame(
                  var1,
                  this.attrIcons,
                  this.attrIcons.getWidth(),
                  this.attrIcons.getHeight() / 10,
                  0,
                  var5 + 10,
                  var7 + 6 + Utils.lineHeight + 4 + var6 * var8,
                  3,
                  0
               );
               var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var8, 20);
               Utils.drawNumber(
                  var1, pets[learnerScroll + var8].stats[1], var5 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4 + var6 * var8, this.digits, 6, 7, 24
               );
               Utils.drawNumber(
                  var1,
                  pets[learnerScroll + var8].stats[2],
                  var5 + 26 + 2 + 24 + 2 + 6 + 2,
                  var7 + 3 + Utils.lineHeight + 4 + var6 * var8,
                  this.digits,
                  6,
                  7,
                  20
               );
               Utils.drawFrame(
                  var1,
                  this.attrIcons,
                  this.attrIcons.getWidth(),
                  this.attrIcons.getHeight() / 10,
                  1,
                  var5 + 6 + this.tabBarBounds[2] / 2,
                  var7 + 6 + Utils.lineHeight + 4 + var6 * var8,
                  3,
                  0
               );
               var1.drawRegion(this.digits, 60, 0, 6, 7, 0, this.screenW / 2 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var8, 20);
               Utils.drawNumber(
                  var1,
                  pets[learnerScroll + var8].stats[4],
                  this.screenW / 2 + 26 + 2 + 24,
                  var7 + 3 + Utils.lineHeight + 4 + var6 * var8,
                  this.digits,
                  6,
                  7,
                  24
               );
               Utils.drawNumber(
                  var1,
                  pets[learnerScroll + var8].stats[5],
                  this.screenW / 2 + 26 + 2 + 24 + 2 + 6 + 2,
                  var7 + 3 + Utils.lineHeight + 4 + var6 * var8,
                  this.digits,
                  6,
                  7,
                  20
               );
            }
         }
      }

      if (this.cursorArrow != null) {
         var1.drawRegion(
            this.cursorArrow, 0, 0, 15, 15, 0, this.tabBarBounds[0] + 8 + this.cursorBob, var7 + var6 / 2 + var6 * (learnerIndex - learnerScroll), 3
         );
      }
   }

   private void drawUseTargetList(Graphics var1) {
      int var2 = this.bottomPanelH;
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
      int var4 = 1;
      int var5 = this.tabBarBounds[0] + 8;
      if (pets != null) {
         var4 = 1 + pets.length;
      }

      short var6 = (short)(Utils.lineHeight << 1);
      useTargetRows = (byte)(var2 / var6);
      short var7;
      if (var2 % var6 != 0) {
         var7 = (short)(0 + var2 % var6 / 2 + var3);
      } else {
         var7 = (short)(0 + var3 + 0);
      }

      var3 = useTargetRows;
      if (var4 < var3) {
         var3 = var4;
      }

      if (var4 > useTargetRows) {
         GameUtil.drawScrollBar(
            var1, null, null, this.tabBarBounds[0] + this.tabBarBounds[2] - 10, var7, var6 * useTargetRows, var4, useTargetRows, useTargetScroll
         );
      }

      for (int var9 = 0; var9 < var3; var9++) {
         if (var9 + useTargetScroll == 0) {
            Utils.drawOutlinedText8(var1, hero.name, this.tabBarBounds[0] + 5, var7 + var6 * var9, 9502459, 540331, 20);
            Utils.drawNumber(var1, hero.stats[0], this.screenW / 2 + 26 + 30, var7 + var6 * var9 + Utils.lineHeight / 2, this.digits, 6, 7, 3);
            if (this.levelImg == null) {
               this.levelImg = ResourceCache.loadImage("/pic/D08.gf");
            }

            if (this.levelImg != null) {
               var1.drawImage(this.levelImg, this.screenW / 2 + 26 + 50, var7 + var6 * var9 + Utils.lineHeight / 2, 3);
            }

            if (this.attrIcons == null) {
               this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
            }

            Utils.drawFrame(
               var1,
               this.attrIcons,
               this.attrIcons.getWidth(),
               this.attrIcons.getHeight() / 10,
               0,
               var5 + 10,
               var7 + 6 + Utils.lineHeight + 4 + var6 * var9,
               3,
               0
            );
            var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, 20);
            Utils.drawNumber(var1, hero.stats[3], var5 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, this.digits, 6, 7, 24);
            Utils.drawNumber(var1, hero.stats[10], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, this.digits, 6, 7, 20);
            Utils.drawFrame(
               var1,
               this.attrIcons,
               this.attrIcons.getWidth(),
               this.attrIcons.getHeight() / 10,
               1,
               var5 + 6 + this.tabBarBounds[2] / 2,
               var7 + 6 + Utils.lineHeight + 4 + var6 * var9,
               3,
               0
            );
            var1.drawRegion(this.digits, 60, 0, 6, 7, 0, this.screenW / 2 + 26 + 6 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, 20);
            Utils.drawNumber(var1, hero.stats[5], this.screenW / 2 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, this.digits, 6, 7, 24);
            Utils.drawNumber(
               var1, hero.stats[11], this.screenW / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, this.digits, 6, 7, 20
            );
         } else {
            Utils.drawOutlinedText8(var1, pets[useTargetScroll + var9 - 1].name, this.tabBarBounds[0] + 5, var7 + var6 * var9, 9502459, 540331, 20);
            if (this.attrIcons == null) {
               this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
            }

            if (this.levelImg == null) {
               this.levelImg = ResourceCache.loadImage("/pic/D08.gf");
            }

            Utils.drawNumber(
               var1, pets[useTargetScroll + var9 - 1].stats[0], this.screenW / 2 + 26 + 30, var7 + var6 * var9 + Utils.lineHeight / 2, this.digits, 6, 7, 3
            );
            if (this.levelImg != null) {
               var1.drawImage(this.levelImg, this.screenW / 2 + 26 + 50, var7 + var6 * var9 + Utils.lineHeight / 2, 3);
            }

            Utils.drawFrame(
               var1,
               this.attrIcons,
               this.attrIcons.getWidth(),
               this.attrIcons.getHeight() / 10,
               0,
               var5 + 10,
               var7 + 6 + Utils.lineHeight + 4 + var6 * var9,
               3,
               0
            );
            var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, 20);
            Utils.drawNumber(
               var1, pets[useTargetScroll + var9 - 1].stats[1], var5 + 26 + 2 + 24, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, this.digits, 6, 7, 24
            );
            Utils.drawNumber(
               var1,
               pets[useTargetScroll + var9 - 1].stats[2],
               var5 + 26 + 2 + 24 + 2 + 6 + 2,
               var7 + 3 + Utils.lineHeight + 4 + var6 * var9,
               this.digits,
               6,
               7,
               20
            );
            Utils.drawFrame(
               var1,
               this.attrIcons,
               this.attrIcons.getWidth(),
               this.attrIcons.getHeight() / 10,
               1,
               var5 + 6 + this.tabBarBounds[2] / 2,
               var7 + 6 + Utils.lineHeight + 4 + var6 * var9,
               3,
               0
            );
            var1.drawRegion(this.digits, 60, 0, 6, 7, 0, this.screenW / 2 + 26 + 2 + 24 + 2, var7 + 3 + Utils.lineHeight + 4 + var6 * var9, 20);
            Utils.drawNumber(
               var1,
               pets[useTargetScroll + var9 - 1].stats[4],
               this.screenW / 2 + 26 + 2 + 24,
               var7 + 3 + Utils.lineHeight + 4 + var6 * var9,
               this.digits,
               6,
               7,
               24
            );
            Utils.drawNumber(
               var1,
               pets[useTargetScroll + var9 - 1].stats[5],
               this.screenW / 2 + 26 + 2 + 24 + 2 + 6 + 2,
               var7 + 3 + Utils.lineHeight + 4 + var6 * var9,
               this.digits,
               6,
               7,
               20
            );
         }
      }

      if (this.cursorArrow != null) {
         var1.drawRegion(
            this.cursorArrow, 0, 0, 15, 15, 0, this.tabBarBounds[0] + 8 + this.cursorBob, var7 + var6 / 2 + var6 * (useTargetIndex - useTargetScroll), 3
         );
      }
   }

   private void drawDexTab(Graphics var1) {
      int var2 = this.tabBarBounds[0];
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
      int var4 = Utils.lineHeight + 8;
      var1.setColor(2237991);
      if (dexPage != 0) {
         int var22 = this.tabBarBounds[2];
         this.drawElements(var1, dexElements);
         var1.setClip(var2, var3, this.tabBarBounds[2] >> 1, this.topPanelH);
         var3 += this.topPanelH;
         if (previewAnim != null) {
            GameUtil.drawAnimation(var1, previewAnim, var2 + (this.tabBarBounds[2] >> 2), var3, true, 0);
         }

         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         Utils.drawOutlinedText8(var1, dexNames[dexPetIndex], var2 + 8, var3, 9502459, 540331, 36);
         if (dexPage == 1) {
            var3 += 4;
            var2 += 8;
            byte[] var27 = Utils.parseByteArray(petData[dexPetIndex][18], ",");
            if (GameData.itemNames == null) {
               GameData.loadItemTable();
            }

            var4 = GameData.findItemIndex(var27[0]);
            int var28 = vh.stringWidth(Utils.defaultFont, "Gen đặc trưng") + 3;
            Utils.drawOutlinedText8(var1, "Gen đặc trưng", var2, var3, 16706636, 9128217, 20);
            var1.setColor(16777215);
            vh.drawString(var1, GameData.itemNames[var4], var2 + var28, var3, 20);
            if (this.textBox != null) {
               this.textBox.clear();
            } else {
               this.textBox = new TextBlock();
            }

            this.textBox.addText(GameData.itemDescriptions[var4], this.tabBarBounds[2] - 10, null, 13695736);
            this.textBox.setBounds(var2 + 4, var3 + Utils.lineHeight, this.tabBarBounds[2] - 10, Utils.lineHeight, 1);
            this.textBox.draw(var1, this.textBox, 1, true, false);
            if (dexStatStars != null) {
               var3 = 2 + this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + this.bottomPanelH - 48;
               if (this.attrIcons == null) {
                  this.attrIcons = ResourceCache.loadImage("/pic/D13.gf");
               }

               Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 0, var2, var3, 20, 0);
               var3 -= 3;
               drawStars(var1, this.starImg, var2 + 30, var3 + 8, dexStatStars[0], 3);
               Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 3, var2 + var22 / 2, var3, 20, 0);
               drawStars(var1, this.starImg, var2 + 30 + var22 / 2, var3 + 8, dexStatStars[2], 3);
               var3 += 16;
               Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 1, var2, var3, 20, 0);
               drawStars(var1, this.starImg, var2 + 30, var3 + 8, dexStatStars[1], 3);
               Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 4, var2 + var22 / 2, var3, 20, 0);
               drawStars(var1, this.starImg, var2 + 30 + var22 / 2, var3 + 8, dexStatStars[3], 3);
               var3 += 16;
               Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, 5, var2, var3, 20, 0);
               drawStars(var1, this.starImg, var2 + 30, var3 + 8, dexStatStars[4], 3);
            }

            return;
         }

         if (dexPage == 2) {
            if (this.fusionMaterialList != null) {
               this.fusionMaterialList.draw(var1);
            }

            var22 = this.screenW * 3 / 4;
            var2 = (this.screenW >> 1) - (var22 >> 1);
            var3 = (var3 = this.screenH >> 1) - 2 * Utils.lineHeight;
            var4 = 2 * Utils.lineHeight + 10;
            if (this.isFusing && !this.isFusionDone && this.fusionProgress < 100) {
               var1.setColor(1381651);
               var1.fillRoundRect(var2 - 1, var3 - 1, var22 + 3, var4 + 3, 8, 8);
               var1.setColor(16053217);
               var1.drawRoundRect(var2, var3, var22, var4, 8, 8);
               var1.setColor(6768205);
               var1.fillRect(var2 + 2, var3 + 2, var22 - 3, var4 - 3);
               var2 += 2;
               var22 -= 8;
               this.fusionProgress += 5;
               var1.setColor(9262239);
               var1.drawRect(var2 + 1, var3 + var4 / 2, var22 - 2, 5);
               var1.setColor(16106751);
               var1.fillRect(var2 + 2, var3 + 1 + var4 / 2, (var22 - 4) * this.fusionProgress / 100, 4);
               String var26 = "Đang hợp thành";
               var1.setColor(16777215);
               vh.drawString(var1, var26, this.screenW >> 1, var3 + var4, 33);
            } else if (this.fusionProgress >= 100) {
               this.isFusionDone = true;
            }

            if (this.isFusionDone) {
               if (this.dexPetId > 0) {
                  this.buildFusionMaterialList();
                  byte var14 = 2;
                  this.softKeyFrame = var14;
                  GameData.addPet(this.dexPetId, -1);
                  pets = GameUtil.a_APetInfo();
                  this.showToast("Chúc mừng bạn có thêm một sủng vật mới", 10);
               }

               dexPage = 1;
               this.isFusionDone = false;
               this.isFusing = false;
               this.fusionProgress = 0;
            }
         }
      } else {
         var1.fillRect(var2 + 4, var3 + var4 - 1, this.tabBarBounds[2] - 8, 3);
         var1.fillRect(var2 + 4, var3 + this.bottomPanelH - var4 - 1, this.tabBarBounds[2] - 8, 3);
         var1.setColor(14214117);
         var1.fillRect(var2 + 4, var3 + Utils.lineHeight + 8, this.tabBarBounds[2] - 8, 1);
         var1.fillRect(var2 + 4, var3 + this.bottomPanelH - var4, this.tabBarBounds[2] - 8, 1);
         int var5 = 0;
         if (this.dexFoundList != null) {
            var5 = this.dexFoundList.itemCount();
         }

         if (this.dexHeaderImg != null) {
            Utils.drawFrame(var1, this.dexHeaderImg, this.dexHeaderImg.getWidth(), this.dexHeaderImg.getHeight() >> 1, 0, var2 + 10, var3 + (var4 >> 1), 6, 0);
            Utils.drawFrame(
               var1,
               this.dexHeaderImg,
               this.dexHeaderImg.getWidth(),
               this.dexHeaderImg.getHeight() >> 1,
               1,
               var2 + (this.tabBarBounds[2] >> 1),
               var3 + (var4 >> 1),
               3,
               0
            );
            Utils.drawNumber(var1, var5, var2 - 6 + 14 + this.tabBarBounds[2] * 3 / 4, var3 + (var4 >> 1), this.digits, 6, 7, 10);
            var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var2 + 14 + this.tabBarBounds[2] * 3 / 4, var3 + (var4 >> 1), 6);
            Utils.drawNumber(var1, 140, var2 + 6 + 14 + this.tabBarBounds[2] * 3 / 4, var3 + (var4 >> 1), this.digits, 6, 7, 6);
         } else {
            Utils.drawOutlinedText(var1, "Đã phát hiện:", var2 + (this.tabBarBounds[2] >> 2), var3 + 4, 16771916, 8931863, 17);
            Utils.drawOutlinedText(var1, " " + var5 + "/140", var2 + (this.tabBarBounds[2] >> 1), var3 + 4, 16646121, 7545885, 17);
         }

         if (this.dexArrowsImg != null) {
            Utils.drawFrame(
               var1, this.dexArrowsImg, this.dexArrowsImg.getWidth() / 2, this.dexArrowsImg.getHeight(), 0, var2 + 4, var3 + this.bottomPanelH - var4 / 2, 6, 0
            );
            Utils.drawFrame(
               var1,
               this.dexArrowsImg,
               this.dexArrowsImg.getWidth() / 2,
               this.dexArrowsImg.getHeight(),
               1,
               var2 - 4 + this.tabBarBounds[2],
               var3 + this.bottomPanelH - var4 / 2,
               10,
               0
            );
         }

         if (this.dexFilterImg != null) {
            for (int var6 = 0; var6 < 3; var6++) {
               Utils.drawFrame(
                  var1,
                  this.dexFilterImg,
                  this.dexFilterImg.getWidth(),
                  this.dexFilterImg.getHeight() / 3,
                  var6,
                  var2 + this.tabBarBounds[2] / 4 + var6 * 50,
                  var3 + this.bottomPanelH - var4 / 2,
                  3,
                  0
               );
            }

            if (this.dexFilterCursorAnim != null) {
               GameUtil.drawAnimation(
                  var1, this.dexFilterCursorAnim, var2 + this.tabBarBounds[2] / 4 + this.b_I * 50, var3 + this.bottomPanelH - var4 / 2, true, 0
               );
            }
         } else {
            for (int var25 = 0; var25 < 3; var25++) {
               Utils.drawOutlinedText(
                  var1,
                  this.dexFilterNames[var25],
                  var2 - 4 + this.tabBarBounds[2] / 4 + var25 * 48,
                  var3 + this.bottomPanelH - (var4 + Utils.lineHeight) / 2,
                  16771916,
                  8931863,
                  17
               );
            }

            var1.setColor(65280);
            var1.drawRect(
               var2 + this.tabBarBounds[2] / 4 - Utils.fontWidthMM - 6 + this.b_I * 48,
               var3 + this.bottomPanelH - (var4 + Utils.lineHeight) / 2,
               (Utils.fontWidthMM << 1) + 4,
               Utils.lineHeight + 2
            );
         }

         var3 += var4;
         if (this.b_I == 0) {
            if (this.dexFoundList != null) {
               this.dexFoundList.draw(var1);
            } else {
               var1.setColor(16777215);
               vh.drawString(var1, "Bạn chưa mở đồ giám", var2 + this.tabBarBounds[2] / 2, var3, 17);
            }
         } else {
            if (this.b_I != 1) {
               if (this.b_I == 2 && this.dexAllList != null) {
                  this.dexAllList.draw(var1);
               }

               return;
            }

            if (this.dexElementList == null) {
               return;
            }

            this.dexElementList.draw(var1);
         }
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

   private void drawSkillTab(Graphics var1) {
      int var2 = this.tabBarBounds[0] + (this.contentW >> 1) - 2;
      int var4 = this.topPanelH / 3;
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + (var4 >> 1);
      int[][] var5;
      (var5 = new int[3][2])[0][0] = hero.stats[3];
      var5[0][1] = hero.stats[10];
      var5[1][0] = hero.stats[5];
      var5[1][1] = hero.stats[11];
      var5[2][0] = hero.stats[1];
      var5[2][1] = hero.stats[2];
      this.drawStatBars(var1, var2 - 10, var3, var4, var5);
      var2 = this.tabBarBounds[0] + 4;
      var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + 4;
      var4 = (this.contentW >> 1) - 22;
      int var20 = this.topPanelH - this.classNameImg.getHeight() / 2 - 12;
      this.drawHeroPortrait(var1, var2, var3, var4, var20);
      if (skillPage == 0) {
         var3 += this.topPanelH;
         if (this.heroSkillList != null && this.heroSkillList.getColumnCount() > 0) {
            this.heroSkillList.draw(var1);
         }

         var2 += this.contentW / 2;
         var3 += this.bottomPanelH;
         var3 -= 5;
         if (this.proficiencyImg != null) {
            var1.drawImage(this.proficiencyImg, var2, var3, 33);
            var2 += this.proficiencyImg.getWidth() + 10;
         }

         Utils.drawNumber(var1, hero.skills[this.heroSkillList.getSelectedRow()].exp, var2, var3, this.digits, 6, 7, 40);
         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var2, var3, 36);
         var4 = 0;
         if (!Utils.contains(new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38}, this.heroSkillList.getRowId(this.heroSkillList.getSelectedRow()))) {
            var4 = hero.skills[this.heroSkillList.getSelectedRow()].expToNext;
         }

         if (this.heroSkillList.getRowId(this.heroSkillList.getSelectedRow()) == 39) {
            var4 = 0;
         }

         Utils.drawNumber(var1, var4, var2 + 6, var3, this.digits, 6, 7, 36);
      } else {
         if (skillPage == 1) {
            if (this.textBox != null) {
               this.textBox.clear();
            } else {
               this.textBox = new TextBlock();
            }

            var4 = GameData.findSkillIndex(this.heroSkillList.getRowId(this.heroSkillList.getSelectedRow()));
            if (Skill.icons == null) {
               Skill.loadIcons();
            }

            var2 = this.tabBarBounds[0] + 6;
            var1.drawImage(Skill.icons[var4], var2, this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2, 20);
            var2 += Skill.icons[var4].getWidth();
            Utils.drawOutlinedText(var1, Skill.names[var4], var2, this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 4, 16580583, 6036810, 20);
            if (this.minusDigits == null) {
               this.minusDigits = ResourceCache.createImageUncached("/pic/number05.gf", 2);
            }

            var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH;
            var2 += vh.stringWidth(Utils.defaultFont, Skill.names[var4]);
            Utils.drawOutlinedText8(var1, "" + hero.skills[this.heroSkillList.getSelectedRow()].level, var2 + 3, var3 + 4, 16550572, 8856870, 20);
            var2 += vh.stringWidth(Utils.defaultFont, "00");
            Utils.drawOutlinedText8(var1, "  cấp", var2, var3 + 4, 16776937, 6036812, 20);
            this.textBox.addText(Skill.descriptions[var4], this.contentW - 20, null, 16777215);
            this.textBox
               .setBounds(
                  this.tabBarBounds[0] + 5,
                  this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + vh.getHeight(Utils.defaultFont),
                  this.contentW - 20,
                  this.bottomPanelH - vh.getHeight(Utils.defaultFont) - 6,
                  (this.bottomPanelH - vh.getHeight(Utils.defaultFont) - 6) / Utils.lineHeight
               );
            this.textBox.draw(var1, this.textBox, 1, true, false);
         }
      }
   }

   private void drawSystemTab(Graphics var1) {
      int[] var2;
      int var3 = (var2 = this.tabBar.getBounds())[0];
      int var4 = var2[1] + var2[3];
      if (systemPage == 0) {
         if (this.systemMenuList != null) {
            this.systemMenuList.draw(var1);
            return;
         }
      } else {
         if (systemPage == 1 || systemPage == 2 || systemPage == 5) {
            if (systemPage != 1 && saveConfirmMode != 1) {
               if (systemPage == 2 || saveConfirmMode == 2) {
                  if (this.systemTitleImg != null) {
                     Utils.drawFrame(
                        var1,
                        this.systemTitleImg,
                        this.systemTitleImg.getWidth(),
                        this.systemTitleImg.getHeight() / 2,
                        0,
                        var3 + var2[2] / 2,
                        var4 + (this.topPanelH >> 1),
                        3,
                        0
                     );
                  } else {
                     Utils.drawOutlinedText(
                        var1, "Chọn bản ghi", var3 + var2[2] / 2, var4 + (this.topPanelH >> 1) - Utils.lineHeight / 2, 14286332, 2979009, 17
                     );
                  }
               }
            } else if (this.systemTitleImg != null) {
               Utils.drawFrame(
                  var1,
                  this.systemTitleImg,
                  this.systemTitleImg.getWidth(),
                  this.systemTitleImg.getHeight() / 2,
                  0,
                  var3 + var2[2] / 2,
                  var4 + (this.topPanelH >> 1),
                  3,
                  0
               );
            } else {
               Utils.drawOutlinedText(var1, "Chọn bản ghi", var3 + var2[2] / 2, var4 + (this.topPanelH >> 1) - Utils.lineHeight / 2, 14286332, 2979009, 17);
            }

            var4 += this.topPanelH;
            int var21 = this.bottomPanelH / 2;

            for (int var22 = 0; var22 < DeviceConfig.rmsCount; var22++) {
               var3 = var2[0];
               new StringBuffer().append("Ghi ").append(var22 + 1).toString();
               if (saveConfirmMode == 0 && this.cursorArrow != null && menuPage == 9) {
                  var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var3 + 5 + this.cursorBob, var4 + var21 / 4 + saveSlotIndex * 81, 3);
               }

               if (GameData.hasSaveIndex) {
                  if (GameData.slotUsed[var22] == 1 && saveSlotAnims != null && saveSlotAnims[var22] != null) {
                     saveSlotAnims[var22].x = (short)(var3 + var2[2] / 4);
                     saveSlotAnims[var22].y = (short)(var4 - Utils.lineHeight + var21 + var21 * var22);
                     if (var22 == saveSlotIndex) {
                        saveSlotAnims[var22].setAction(0);
                     } else {
                        saveSlotAnims[var22].setAction(4);
                     }

                     saveSlotAnims[var22].draw(var1);
                     saveSlotAnims[var22].nextFrame(true);
                     var3 = var2[0] - 15 + var2[2] / 2;
                     Utils.drawOutlinedText(var1, GameData.slotSceneNames[var22], var2[0] - 10 + var2[2], var4 + var21 * var22, 3532779, 146244, 24);
                     if (this.classNameImg != null) {
                        Utils.drawFrame(
                           var1,
                           this.classNameImg,
                           this.classNameImg.getWidth(),
                           this.classNameImg.getHeight() >> 1,
                           GameData.slotHeroIds[var22] - 1,
                           var3 + (this.classNameImg.getWidth() >> 1),
                           var4 + (Utils.lineHeight << 1) + var21 * var22,
                           3,
                           0
                        );
                     }

                     Utils.drawNumber(
                        var1, GameData.slotHeroLevels[var22], var3 + var2[2] / 2 - 40, var4 + (Utils.lineHeight << 1) + var21 * var22, this.digits, 6, 7, 3
                     );
                     var1.drawImage(this.levelImg, var3 + var2[2] / 2 - 15, var4 + (Utils.lineHeight << 1) + var21 * var22, 3);
                     int var10003 = var4 + var21 * var22 + Utils.lineHeight * 3;
                     short var10004 = GameData.slotYears[var22];
                     byte var10005 = GameData.slotMonths[var22];
                     byte var10006 = GameData.slotDays[var22];
                     byte var10007 = GameData.slotHours[var22];
                     byte var14 = GameData.slotMinutes[var22];
                     byte var13 = var10007;
                     byte var12 = var10006;
                     byte var11 = var10005;
                     short var10 = var10004;
                     int var9 = var10003;
                     if (this.digits != null) {
                        Utils.drawNumber(var1, var10, var3, var9, this.digits, 6, 7, 20);
                        var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var3 + 26, var9, 20);
                        if (var11 < 10) {
                           var1.drawRegion(this.digits, 0, 0, 6, 7, 0, var3 + 30, var9, 20);
                           Utils.drawNumber(var1, var11, var3 + 36, var9, this.digits, 6, 7, 20);
                        } else {
                           Utils.drawNumber(var1, var11, var3 + 30, var9, this.digits, 6, 7, 20);
                        }

                        var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var3 + 42, var9, 20);
                        if (var12 < 10) {
                           var1.drawRegion(this.digits, 0, 0, 6, 7, 0, var3 + 48, var9, 20);
                           Utils.drawNumber(var1, var12, var3 + 53, var9, this.digits, 6, 7, 20);
                        } else {
                           Utils.drawNumber(var1, var12, var3 + 48, var9, this.digits, 6, 7, 20);
                        }

                        Utils.drawNumber(var1, var13, var3 + 72, var9, this.digits, 6, 7, 24);
                        if (var13 < 10) {
                           var1.drawRegion(this.digits, 0, 0, 6, 7, 0, var3 + 60, var9, 20);
                        }

                        var1.drawRegion(this.digits, 78, 0, 6, 7, 0, var3 + 72, var9, 20);
                        Utils.drawNumber(var1, var14, var3 + 90, var9, this.digits, 6, 7, 24);
                        if (var14 < 10) {
                           var1.drawRegion(this.digits, 0, 0, 6, 7, 0, var3 + 78, var9, 20);
                        }
                     }
                  }
               } else {
                  var1.setColor(16777215);
                  vh.drawString(var1, " Trống ", this.screenW / 2, var4 + var21 * var22, 17);
               }
            }

            if (systemPage == 5) {
               GameUtil.drawMessageBox(var1, confirmText, this.screenW, this.screenH);
            }

            return;
         }

         if (systemPage == 3) {
            if (this.systemTitleImg != null) {
               Utils.drawFrame(
                  var1,
                  this.systemTitleImg,
                  this.systemTitleImg.getWidth(),
                  this.systemTitleImg.getHeight() / 2,
                  1,
                  this.screenW / 2,
                  var2[1] + var2[3] + (this.topPanelH >> 1),
                  3,
                  0
               );
            } else {
               Utils.drawOutlinedText8(
                  var1, "Âm lượng", this.screenW / 2, var2[1] + var2[3] - Utils.lineHeight / 2 + (this.topPanelH >> 1), 14286332, 2979009, 17
               );
            }

            var1.setColor(12961221);
            int var5 = (this.screenW - 90 - 27) / 2;
            int var6 = this.screenH / 2 + 20;
            int var15 = GameData.musicVolume / 10;
            if (this.speakerAnim != null) {
               this.speakerAnim.setPosition(this.screenW >> 1, (this.screenH >> 1) - 20);
               this.speakerAnim.draw(var1);
               if (GameCanvas.instance.frameCounter % 3 == 0) {
                  this.speakerAnim.nextFrame(true);
               }

               for (int var16 = 0; var16 < 10; var16++) {
                  if (var16 < var15) {
                     var1.setColor(16776412);
                     var1.fillRect(var5 + var16 * 12, var6, 9, 5);
                     var1.setColor(16773198);
                     var1.fillRect(var5 + 1 + var16 * 12, var6 + 1, 8, 4);
                  } else {
                     var1.setColor(15132390);
                     var1.fillRect(var5 + var16 * 12, var6, 9, 5);
                     var1.setColor(12961221);
                     var1.fillRect(var5 + 1 + var16 * 12, var6 + 1, 8, 4);
                  }
               }
            } else {
               for (int var17 = 0; var17 < 10; var17++) {
                  if (var17 < var15) {
                     var1.setColor(16776412);
                     var1.fillRect(var5 + var17 * 12, var6 - (var17 << 2), 9, 5 + (var17 << 2));
                     var1.setColor(16773198);
                     var1.fillRect(var5 + 1 + var17 * 12, var6 + 1 - (var17 << 2), 8, 4 + (var17 << 2));
                  } else {
                     var1.setColor(15132390);
                     var1.fillRect(var5 + var17 * 12, var6 - (var17 << 2), 9, 5 + (var17 << 2));
                     var1.setColor(12961221);
                     var1.fillRect(var5 + 1 + var17 * 12, var6 + 1 - (var17 << 2), 8, 4 + (var17 << 2));
                  }
               }
            }

            if (DeviceConfig.playMusic && var15 != 0) {
               Utils.drawNumber(var1, GameData.musicVolume, this.screenW / 2, var6 + 5 + 4 + Utils.lineHeight / 2, this.digits, 6, 7, 3);
            } else {
               var1.setColor(16777215);
               vh.drawString(var1, "Tắt", this.screenW / 2, var6 + 5 + 4, 17);
            }

            Utils.drawFrame(
               var1,
               this.leftRightArrows,
               this.leftRightArrows.getWidth() / 2,
               this.leftRightArrows.getHeight(),
               0,
               this.screenW / 2 - Utils.lineHeight - this.cursorBob,
               var6 + 5 + 2 + Utils.lineHeight / 2,
               20,
               0
            );
            Utils.drawFrame(
               var1,
               this.leftRightArrows,
               this.leftRightArrows.getWidth() / 2,
               this.leftRightArrows.getHeight(),
               1,
               this.screenW / 2 + Utils.lineHeight + this.cursorBob,
               var6 + 5 + 2 + Utils.lineHeight / 2,
               24,
               0
            );
            return;
         }

         if (systemPage == 4) {
            GameUtil.drawMessageBox(var1, "Trở về menu chính?", this.screenW, this.screenH);
            return;
         }

         if (systemPage == 6) {
            if (this.systemMenuList != null) {
               this.systemMenuList.draw(var1);
            }

            GameUtil.drawMessageBox(var1, "Điểm hiện tại: " + GameData.score + ", tải lên không?", this.screenW, this.screenH);
         }
      }
   }

   private void drawQuestTab(Graphics var1) {
      int var2 = this.tabBarBounds[0];
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
      if (questPage == 0 || questPage == 1 || questPage == 2 || questPage == 4 || questPage == 5) {
         for (int var4 = 0; var4 < 2; var4++) {
            if (this.questTabsImg != null) {
               if (questTabIndex == var4) {
                  if (this.cursorArrow != null && menuPage == 7 && questPage == 0) {
                     var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 + 10 + this.cursorBob + var4 * this.contentW / 2, var3 + (this.topPanelH >> 1), 3);
                  }

                  Utils.drawFrame(
                     var1,
                     this.questTabsSelImg,
                     this.questTabsSelImg.getWidth(),
                     this.questTabsSelImg.getHeight() >> 1,
                     var4,
                     var2 + this.contentW / 4 + var4 * this.contentW / 2,
                     var3 + (this.topPanelH >> 1),
                     3,
                     0
                  );
               } else {
                  Utils.drawFrame(
                     var1,
                     this.questTabsImg,
                     this.questTabsImg.getWidth(),
                     this.questTabsImg.getHeight() >> 1,
                     var4,
                     var2 + this.contentW / 4 + var4 * this.contentW / 2,
                     var3 + (this.topPanelH >> 1),
                     3,
                     0
                  );
               }
            } else if (questTabIndex == var4) {
               if (this.cursorArrow != null && menuPage == 7 && questPage == 0) {
                  var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 + 10 + this.cursorBob + var4 * this.contentW / 2, var3 + (this.topPanelH >> 1), 3);
               }

               Utils.drawOutlinedText(
                  var1,
                  this.questTabNames[var4],
                  var2 - 4 + this.contentW / 4 + var4 * this.contentW / 2,
                  var3 + (this.topPanelH - Utils.lineHeight >> 1),
                  15987164,
                  6036307,
                  17
               );
            } else {
               Utils.drawOutlinedText(
                  var1,
                  this.questTabNames[var4],
                  var2 - 4 + this.contentW / 4 + var4 * this.contentW / 2,
                  var3 + (this.topPanelH - Utils.lineHeight >> 1),
                  9306107,
                  2380419,
                  17
               );
            }
         }

         var3 += this.topPanelH;
         if (questTabIndex == 0) {
            Utils.drawOutlinedText8(var1, "Tên nhiệm vụ", DeviceConfig.screenSize[0] >> 1, var3 + 2, 16711581, 10833443, 17);
            if (this.mainQuestList != null && this.mainQuestList.itemCount() > 0) {
               this.mainQuestList.draw(var1);
            } else {
               var1.setColor(16777215);
               vh.drawString(var1, "Danh sách nhiệm vụ trống", DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1, 17);
            }
         } else if (this.sideQuestList != null && this.sideQuestList.itemCount() > 0) {
            this.sideQuestList.draw(var1);
         } else {
            var1.setColor(16777215);
            vh.drawString(var1, "Danh sách nhiệm vụ trống", DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1, 17);
         }

         if (questPage == 4) {
            short var5 = (short)(10 + vh.stringWidth(Utils.defaultFont, "Thuộc tính"));
            short var7 = (short)((this.screenH - 34) / 2 - 2 * Utils.lineHeight);
            this.drawActionMenu(var1, new int[]{15, 16}, questActionIndex, this.screenW / 2, var7, var5, 44, 6768205);
            return;
         }

         if (questPage == 5) {
            GameUtil.drawMessageBox(var1, "Xóa nhiệm vụ đang chọn?", this.screenW, this.screenH);
            return;
         }
      } else if (questPage == 3) {
         if (!DeviceConfig.modelName.equals("N5800")) {
            var2 += SideQuestListPanel.drawQuestTypeTag(var1, GameData.activeQuests[this.questIndex].type, var2 + 15, var3 + 6);
         }

         var1.setColor(9306109);
         vh.drawString(var1, GameData.activeQuests[this.questIndex].name, var2 + 15, var3 + 6, 20);
         if (GameData.activeQuests[this.questIndex].isCompleted) {
            Utils.drawFrame(
               var1,
               this.questDoneImg,
               this.questDoneImg.getWidth() / 2,
               this.questDoneImg.getHeight(),
               0,
               this.tabBarBounds[0] + this.tabBarBounds[2] - 15,
               var3 + this.topPanelH / 2,
               3,
               0
            );
         } else {
            Utils.drawFrame(
               var1,
               this.questDoneImg,
               this.questDoneImg.getWidth() / 2,
               this.questDoneImg.getHeight(),
               1,
               this.tabBarBounds[0] + this.tabBarBounds[2] - 15,
               var3 + this.topPanelH / 2,
               3,
               0
            );
         }

         if (this.questDetail != null) {
            this.questDetail.draw(var1);
         }
      }
   }

   private void drawBagTab(Graphics var1) {
      int var2 = this.tabBarBounds[0] + 5 + this.tabBarBounds[2] / 6;
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + (this.topPanelH >> 1);
      int var4 = this.tabBarBounds[0] + 8;
      int var5;
      int var6 = (var5 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 4) + 25 * bagRows;

      for (int var7 = 0; var7 < 3; var7++) {
         if (this.bagTabsSelImg != null) {
            if (var7 == bagTabIndex) {
               var1.drawRegion(
                  this.bagTabsSelImg,
                  0,
                  this.bagTabsSelImg.getHeight() / 3 * var7,
                  this.bagTabsSelImg.getWidth(),
                  this.bagTabsSelImg.getHeight() / 3,
                  0,
                  var2 + this.bagTabsImg.getWidth() / 2 + this.tabBarBounds[2] / 4 * var7,
                  var3,
                  3
               );
               if (this.cursorArrow != null && menuPage == 6 && bagPage == 0) {
                  var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 - 5 + this.cursorBob + this.tabBarBounds[2] / 4 * var7, var3, 3);
               }
            } else {
               var1.drawRegion(
                  this.bagTabsImg,
                  0,
                  this.bagTabsImg.getHeight() / 3 * var7,
                  this.bagTabsImg.getWidth(),
                  this.bagTabsImg.getHeight() / 3,
                  0,
                  var2 + this.bagTabsImg.getWidth() / 2 + this.tabBarBounds[2] / 4 * var7,
                  var3,
                  3
               );
            }
         } else if (var7 == bagTabIndex) {
            Utils.drawOutlinedText(
               var1, this.bagTabNames[var7], var2 + 12 + this.tabBarBounds[2] / 4 * var7, var3 - Utils.lineHeight / 2, 15987164, 6036307, 17
            );
            if (this.cursorArrow != null && menuPage == 6 && bagPage == 0) {
               var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 - 5 + this.cursorBob + this.tabBarBounds[2] / 4 * var7, var3, 3);
            }
         } else {
            Utils.drawOutlinedText(var1, this.bagTabNames[var7], var2 + 12 + this.tabBarBounds[2] / 4 * var7, var3 - Utils.lineHeight / 2, 9306107, 2380419, 17);
         }
      }

      for (int var15 = 0; var15 < bagRows; var15++) {
         for (int var8 = 0; var8 < bagCols; var8++) {
            drawSlotFrame(var1, var4 + var8 * 25, var5 + var15 * 25, 15589356, 6963805);
         }
      }

      GameUtil.drawScrollBar(var1, null, null, var4 + 25 * bagCols, var5, bagRows * 25 - 5, GameSettings.maxItemTypes / bagCols, bagRows, bagScrollRow);
      if (bagItems != null && bagItems[bagTabIndex] != null) {
         for (int var16 = 0; var16 < bagRows; var16++) {
            for (int var9 = 0; var9 < bagCols; var9++) {
               if (bagScrollRow * bagCols + var9 + var16 * bagCols < bagItems[bagTabIndex].length) {
                  var3 = GameData.findItemIndex(bagItems[bagTabIndex][bagScrollRow * bagCols + var9 + var16 * bagCols][0]);
                  if (GameData.itemIcons[var3] != null) {
                     var1.drawImage(GameData.itemIcons[var3], var4 + 11 + var9 * 25, var5 + 11 + var16 * 25, 3);
                  }

                  Utils.drawNumber(
                     var1,
                     bagItems[bagTabIndex][bagScrollRow * bagCols + var9 + var16 * bagCols][1],
                     var4 + 11 + var9 * 25,
                     var5 + 20 + var16 * 25,
                     this.digits,
                     6,
                     7,
                     33
                  );
               }
            }
         }
      } else {
         Utils.drawOutlinedText(
            var1, "Không có vật phẩm liên quan", DeviceConfig.screenSize[0] >> 1, var6 - 4 + vh.getHeight(Utils.defaultFont) / 2, 13695223, 1196461, 17
         );
      }

      if (bagPage != 0 && (bagPage == 1 || bagPage == 2 || bagPage == 4 || bagPage == 3)) {
         if (bagItems != null && bagItems[bagTabIndex] != null && bagItemIndex < bagItems[bagTabIndex].length) {
            if (this.textBox != null) {
               this.textBox.clear();
            } else {
               this.textBox = new TextBlock();
            }

            int var17 = GameData.findItemIndex(bagItems[bagTabIndex][bagItemIndex][0]);
            Utils.drawOutlinedText(var1, GameData.itemNames[var17], var4, var6 - 4, 13695223, 1196461, 20);
            this.textBox
               .addText(
                  GameData.itemDescriptions[var17], this.tabBarBounds[2] - 14 - vh.stringWidth(Utils.defaultFont, GameData.itemNames[var17]), null, 13629944
               );
            this.textBox
               .setBounds(
                  var4 + vh.stringWidth(Utils.defaultFont, GameData.itemNames[var17]),
                  var6 - 7,
                  this.tabBarBounds[2] - 14 - vh.stringWidth(Utils.defaultFont, GameData.itemNames[var17]),
                  2 * vh.getHeight(Utils.defaultFont),
                  2
               );
            this.textBox.draw(var1, this.textBox, 1, true, false);
         }

         if (bagPage == 1) {
            short var18 = (short)(bagItemIndex % bagCols);
            short var11 = (short)(bagItemIndex / bagCols - bagScrollRow);
            if (this.cursorArrow != null) {
               var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var4 + var18 * 25 + this.cursorBob, var5 + 11 + var11 * 25, 3);
            }

            return;
         }

         if (bagPage == 2) {
            short var10 = (short)(10 + vh.stringWidth(Utils.defaultFont, "Thuộc tính"));
            short var13 = (short)((bagActionIds.length << 4) + (bagActionNames.length - 1 << 2));
            short var14 = (short)((this.screenH - var13) / 2 - 2 * Utils.lineHeight);
            this.drawActionMenu(var1, bagActionIds, bagActionIndex, this.screenW / 2, var14, var10, var13, 6768205);
            return;
         }

         if (bagPage == 4) {
            this.drawQuantityDialog(var1, false, bagItems[bagTabIndex][bagItemIndex][1], 0, bagQuantity);
            return;
         }

         if (bagPage == 3) {
            this.drawQuantityDialog(
               var1,
               true,
               bagItems[bagTabIndex][bagItemIndex][1],
               GameData.itemSellPrices[GameData.findItemIndex(bagItems[bagTabIndex][bagItemIndex][0])],
               bagQuantity
            );
         }
      }
   }

   public static void buildBagItems() {
      short[][] var0 = null;
      short[][] var1 = null;
      short[][] var2 = null;
      if (GameData.bagItems != null) {
         for (int var3 = 0; var3 < GameData.bagItems.length; var3++) {
            int var4 = GameData.findItemIndex(GameData.bagItems[var3][0]);
            short[] var5 = Utils.append(Utils.append(null, GameData.bagItems[var3][0]), GameData.bagItems[var3][1]);
            if (GameData.itemTypes[var4] == 1) {
               var0 = Utils.append(var0, var5);
            } else if (GameData.itemTypes[var4] == 2) {
               var1 = Utils.append(var1, var5);
            } else if (GameData.itemTypes[var4] == 3) {
               var2 = Utils.append(var2, var5);
            } else if (GameData.itemTypes[var4] == 4) {
               var2 = Utils.append(var2, var5);
            } else if (GameData.itemTypes[var4] == 5) {
               var1 = Utils.append(var1, var5);
            } else if (GameData.itemTypes[var4] == 6) {
               var1 = Utils.append(var1, var5);
            }
         }
      }

      bagItems = GameUtil.append(bagItems = GameUtil.append(bagItems = GameUtil.append(bagItems, var0), var1), var2);
   }

   private static void drawSlotFrame(Graphics var0, int var1, int var2, int var3, int var4) {
      var0.setColor(var4);
      var0.drawRect(var1, var2, 20, 20);
      var0.setColor(var3);
      var0.drawRect(var1 + 1, var2 + 1, 18, 18);
      var0.setColor(2697251);
      var0.fillRect(var1 + 2, var2 + 2, 17, 17);
   }

   private void drawEquipTab(Graphics var1) {
      if (this.textBox != null) {
         this.textBox.clear();
      } else {
         this.textBox = new TextBlock();
      }

      int var2 = this.tabBarBounds[0];
      int var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
      int var4 = this.topPanelH / 5;
      if (equipPage != 3) {
         int var10002 = var2 + 6;
         int var10003 = var3 + 4;
         int var10004 = this.topPanelH - 10;
         int var10 = this.contentW / 2 - 10;
         int var9 = var10004;
         int var8 = var10003;
         int var7 = var10002;
         Graphics var6 = var1;
         if (this.heroFigureAnim != null) {
            GameUtil.drawAnimation(var1, this.heroFigureAnim, var7 - 4 + var10 / 2, var8 + var9 - 10, true, 0);
         }

         int var11 = var9 / 3;
         int var5 = var10 / 3;

         for (int var55 = 0; var55 < 3; var55++) {
            for (int var60 = 0; var60 < 2; var60++) {
               drawSlotFrame(var6, var7 + (var60 << 1) * var5, var8 + var11 * var55, 15589356, 6963805);
               if (hero.equips[var55 + var60 * 3] != null) {
                  int var12 = GameData.findEquipIndex(hero.equips[var55 + var60 * 3].equipId);
                  if (Equip.icons[var12] != null) {
                     var6.drawImage(Equip.icons[var12], var7 - 2 + var5 / 2 + (var60 << 1) * var5, var8 - 2 + var11 / 2 + var11 * var55, 3);
                  }
               }
            }
         }

         var10003 = var3 + this.topPanelH;
         int var56 = 26;
         var8 = var10003;
         var6 = var1;
         if (this.setLabelImg != null) {
            var1.drawRegion(
               this.setLabelImg,
               0,
               (this.setLabelImg.getHeight() >> 1) * (hero.roleId - 1),
               this.setLabelImg.getWidth(),
               this.setLabelImg.getHeight() >> 1,
               0,
               var2 + 4,
               var8 + 5,
               20
            );
         }

         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var2 + 22, var8 + this.setLabelImg.getHeight() / 2 + 5, 17);
         Utils.drawNumber(var1, setPiecesWorn, var2 + 15, var8 + this.setLabelImg.getHeight() / 2 + 5, this.digits, 6, 7, 20);
         var1.drawRegion(this.digits, 66, 0, 6, 7, 0, var2 + 22 - 6, var8 + this.setLabelImg.getHeight() / 2 + 5, 24);
         Utils.drawNumber(var1, 6, var2 + 22 + 6, var8 + this.setLabelImg.getHeight() / 2 + 5, this.digits, 6, 7, 20);
         var1.drawRegion(this.digits, 72, 0, 6, 7, 0, var2 + 22 + 6 + 6 + 1, var8 + this.setLabelImg.getHeight() / 2 + 5, 20);
         var7 = var2 + this.setLabelImg.getWidth() + 8;

         for (int var61 = 0; var61 < 6; var61++) {
            if (setItemIds != null) {
               if (Utils.contains(setItemIds, hero.equipIds[var61])) {
                  GameUtil.drawAnimation(var6, setGlowAnims[var61], var7 - 1 + var61 * 22, var8 + 2, true, 0);
               }

               drawSlotFrame(var6, var7 + var61 * 22, var8 + 3, 16381892, 9524792);
               if (Equip.icons[GameData.findEquipIndex(setItemIds[var61])] != null) {
                  var6.drawImage(Equip.icons[GameData.findEquipIndex(setItemIds[var61])], var7 + 10 + var61 * 22, var8 + 13, 3);
               }
            }
         }

         var7 = var7 - this.setLabelImg.getWidth() - 8;
         var6.setColor(2303014);
         var6.fillRect(var7 + 1, var8 + 26 + 2 - 1, this.frameW - 2, 2);
         var6.setColor(14083300);
         var6.fillRect(var7 + 2, var8 + 26 + 2, this.frameW - 4, 1);
         var2 += this.contentW / 2;
         var10002 = var2 + 15;
         var56 = var4;
         var8 = var3;
         var7 = var10002;
         var6 = var1;
         GameMenu var36 = this;
         int[] var62 = new int[]{hero.stats[10], hero.stats[11], hero.stats[12], hero.stats[13], hero.stats[14]};

         for (int var67 = 0; var67 < 5; var67++) {
            if (var36.attrIcons != null) {
               if (var67 < 2) {
                  Utils.drawFrame(
                     var6, var36.attrIcons, var36.attrIcons.getWidth(), var36.attrIcons.getHeight() / 10, var67, var7, var8 + var67 * var56 + var56 / 2, 3, 0
                  );
                  Utils.drawNumber(var6, var62[var67], var7 + 5 + var36.attrIcons.getWidth(), var8 + var56 / 2 + var67 * var56, var36.digits, 6, 7, 3);
               } else {
                  Utils.drawFrame(
                     var6,
                     var36.attrIcons,
                     var36.attrIcons.getWidth(),
                     var36.attrIcons.getHeight() / 10,
                     var67 + 1,
                     var7,
                     var8 + var56 / 2 + var67 * var56,
                     3,
                     0
                  );
                  Utils.drawNumber(var6, var62[var67], var7 + 5 + var36.attrIcons.getWidth(), var8 + var56 / 2 + var67 * var56, var36.digits, 6, 7, 3);
               }
            }
         }

         var5 = 0;
         int var42 = 0;
         var7 = 0;
         var8 = 0;
         var56 = 0;
         if (equipPage == 0) {
            if (equipSlotIndex < 6 && hero.equips[equipSlotIndex] != null) {
               var5 = hero.equips[equipSlotIndex].getTotalAttack();
               var56 = hero.equips[equipSlotIndex].getTotalMp();
               var42 = hero.equips[equipSlotIndex].getTotalDefense();
               var7 = hero.equips[equipSlotIndex].getTotalSpeed();
               var8 = hero.equips[equipSlotIndex].getTotalHp();
            }

            var2 = this.tabBarBounds[0];
            var3 = this.tabBarBounds[1] + this.tabBarBounds[3];
            var2 += 6;
            var3 += 2;
            var4 = this.topPanelH - 6;
            var10 = this.contentW / 2 - 10;
            var4 /= 3;
            var10 /= 3;
            if (menuPage == 5) {
               if (equipSlotIndex < 3) {
                  if (this.cursorArrow != null) {
                     var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 + this.cursorBob, var3 + var4 / 2 + var4 * equipSlotIndex, 3);
                  }
               } else if (equipSlotIndex < 6) {
                  if (this.cursorArrow != null) {
                     var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var2 + 2 * var10 + this.cursorBob, var3 + var4 / 2 + var4 * (equipSlotIndex - 3), 3);
                  }
               } else if (this.cursorArrow != null) {
                  var1.drawRegion(
                     this.cursorArrow,
                     0,
                     0,
                     15,
                     15,
                     0,
                     var2 + this.setLabelImg.getWidth() + 22 * (equipSlotIndex - 6) + this.cursorBob,
                     var3 - 6 + this.topPanelH + 20,
                     3
                  );
               }
            }

            if (equipSlotIndex < 6) {
               if (hero.equips[equipSlotIndex] != null) {
                  var11 = GameData.findEquipIndex(hero.equips[equipSlotIndex].equipId);
                  Utils.drawOutlinedText8(var1, Equip.names[var11], var2, var3 + this.topPanelH + 20 + 6, 13695224, 1196460, 20);
                  this.textBox.addText(Equip.describeStats(hero.equips[equipSlotIndex]), this.tabBarBounds[2] - 10, null, 16777215);
                  this.textBox.addText(Equip.descriptions[var11], this.contentW - 10, null, 16777215);
               } else {
                  String[] var72 = new String[]{"Vũ khí", "Mũ", "Áo", "Giày", "Dây chuyền", "Nhẫn"};
                  this.textBox.addText(var72[equipSlotIndex] + " Vị trí", this.contentW - 10, null, 16777215);
               }
            } else {
               if (setItemIds == null) {
                  return;
               }

               Utils.drawOutlinedText8(
                  var1, Equip.names[GameData.findEquipIndex(setItemIds[equipSlotIndex - 6])], var2, var3 + this.topPanelH + 20 + 6, 13695224, 1196460, 20
               );
               var11 = 16777215;
               String var80 = null;
               if (setPiecesWorn >= 2) {
                  var11 = 15925024;
               }

               if (hero.roleId == 1) {
                  var80 = "DEF+300 (2 món)";
               } else if (hero.roleId == 2) {
                  var80 = "HP+500 (2 món)";
               }

               this.textBox.addText(var80, this.contentW - 10, null, var11);
               if (setPiecesWorn >= 4) {
                  var11 = 15925024;
               } else {
                  var11 = 16777215;
               }

               if (hero.roleId == 1) {
                  var80 = "MP+400 (4 món)";
               } else if (hero.roleId == 2) {
                  var80 = "SPD+300 (4 món)";
               }

               this.textBox.addText(var80, this.contentW - 10, null, var11);
               if (setPiecesWorn >= 6) {
                  var11 = 15925024;
               } else {
                  var11 = 16777215;
               }

               if (hero.roleId == 1) {
                  var80 = "HP+600 (6 món)";
               } else if (hero.roleId == 2) {
                  var80 = "ATK+500 (6 món)";
               }

               this.textBox.addText(var80, this.contentW - 10, null, var11);
            }

            var3 = var3 + this.topPanelH + 40 + 4;
            this.textBox.setBounds(var2, var3, this.contentW - 10, this.bottomPanelH - 40 - 4, (this.bottomPanelH - 40 - 4) / Utils.lineHeight);
            this.textBox.draw(var1, this.textBox, 1, true, false);
         } else if (equipPage == 1 || equipPage == 2) {
            if (GameData.equipBagIds[equipSlotIndex] != null && equipBagIndex < GameData.equipBagIds[equipSlotIndex].length) {
               if (hero.equips[equipSlotIndex] != null) {
                  var5 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalAttack() - hero.equips[equipSlotIndex].getTotalAttack();
                  var42 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalDefense() - hero.equips[equipSlotIndex].getTotalDefense();
                  var7 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalSpeed() - hero.equips[equipSlotIndex].getTotalSpeed();
                  var8 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalHp() - hero.equips[equipSlotIndex].getTotalHp();
                  var56 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalMp() - hero.equips[equipSlotIndex].getTotalMp();
               } else {
                  var5 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalAttack();
                  var42 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalDefense();
                  var7 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalSpeed();
                  var8 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalHp();
                  var56 = GameData.equipBag[equipSlotIndex][equipBagIndex].getTotalMp();
               }
            } else if (hero.equips[equipSlotIndex] != null) {
               var5 = -hero.equips[equipSlotIndex].getTotalAttack();
               var42 = -hero.equips[equipSlotIndex].getTotalDefense();
               var7 = -hero.equips[equipSlotIndex].getTotalSpeed();
               var8 = -hero.equips[equipSlotIndex].getTotalHp();
               var56 = -hero.equips[equipSlotIndex].getTotalMp();
            }

            var4 = this.tabBarBounds[0] + 26;
            var3 += this.topPanelH + 30;
            var10 = this.bottomPanelH - 30 - 26 * gridRows;
            if (this.equipSlotLabels != null) {
               Utils.drawFrame(
                  var1,
                  this.equipSlotLabels,
                  this.equipSlotLabels.getWidth() / 6,
                  this.equipSlotLabels.getHeight(),
                  equipSlotIndex,
                  this.tabBarBounds[0] + 0,
                  var3 + 4,
                  20,
                  0
               );
            }

            for (int var68 = 0; var68 < gridRows; var68++) {
               for (int var77 = 0; var77 < gridCols; var77++) {
                  drawSlotFrame(var1, var4 + var77 * 26, var3 + var68 * 26, 15589356, 6963805);
                  if (GameData.equipBagIds[equipSlotIndex] != null
                     && var77 + var68 * gridCols + gridCols * equipBagScrollRow < GameData.equipBagIds[equipSlotIndex].length) {
                     int var15 = GameData.findEquipIndex(GameData.equipBagIds[equipSlotIndex][var77 + var68 * gridCols + gridCols * equipBagScrollRow]);
                     if (Equip.icons[var15] != null) {
                        var1.drawImage(Equip.icons[var15], var4 + 11 + var77 * 26, var3 + 11 + var68 * 26, 3);
                     }
                  }
               }
            }

            if (GameData.equipBag[equipSlotIndex] != null && equipBagIndex < GameData.equipBag[equipSlotIndex].length) {
               var11 = GameData.findEquipIndex(GameData.equipBag[equipSlotIndex][equipBagIndex].equipId);
               Utils.drawOutlinedText(var1, Equip.names[var11], this.tabBarBounds[0] + 4, var3 + 26 * gridRows, 13695224, 1130666, 20);
               this.textBox
                  .addText(
                     Equip.describeStats(GameData.equipBag[equipSlotIndex][equipBagIndex]),
                     this.tabBarBounds[2] - 12 - vh.stringWidth(Utils.defaultFont, Equip.names[var11]),
                     null,
                     13761017
                  );
               this.textBox
                  .addText(Equip.descriptions[var11], this.tabBarBounds[2] - 12 - vh.stringWidth(Utils.defaultFont, Equip.names[var11]), null, 13761017);
               this.textBox
                  .setBounds(
                     this.tabBarBounds[0] + 4 + vh.stringWidth(Utils.defaultFont, Equip.names[var11]),
                     var3 + 26 * gridRows,
                     this.tabBarBounds[2] - 12 - vh.stringWidth(Utils.defaultFont, Equip.names[var11]),
                     var10,
                     var10 / Utils.lineHeight
                  );
               this.textBox.draw(var1, this.textBox, 1, true, false);
            }

            GameUtil.drawScrollBar(
               var1,
               null,
               null,
               var4 + 26 * (gridCols - 1) + 22 + 5,
               var3,
               26 * (gridRows - 1) + 22,
               GameSettings.maxItemTypes / gridCols,
               gridRows,
               equipBagScrollRow
            );
            if (equipPage == 1) {
               short var70 = (short)(equipBagIndex % gridCols);
               short var78 = (short)(equipBagIndex / gridCols - equipBagScrollRow);
               if (this.cursorArrow != null) {
                  var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var4 + var70 * 26 + this.cursorBob, var3 + 11 + var78 * 26, 3);
               }
            } else if (equipPage == 2) {
               short var79 = (short)(10 + vh.stringWidth(Utils.defaultFont, "Thuộc tính"));
               short var82 = (short)(equipActionIds.length * 18 + (equipActionNames.length - 1 << 1));
               short var16 = (short)((this.screenH - var82) / 2 + 2 * Utils.lineHeight);
               this.drawActionMenu(var1, equipActionIds, equipActionIndex, this.screenW / 2, var16, var79, var82, 6768205);
            }
         }

         var2 = this.tabBarBounds[0] + this.contentW * 3 / 4 + 15;
         var4 = this.topPanelH / 5;
         var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + var4 / 2;
         if (var8 > 0) {
            if (this.plusDigits != null) {
               Utils.drawFrame(var1, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, var8, var2 + 14, var3, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 3);
         } else if (var8 < 0) {
            if (this.minusDigits != null) {
               Utils.drawFrame(var1, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, Math.abs(var8), var2 + 14, var3, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 3);
         }

         var3 += var4;
         if (var56 > 0) {
            if (this.plusDigits != null) {
               Utils.drawFrame(var1, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, var56, var2 + 14, var3, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 3);
         } else if (var56 < 0) {
            if (this.minusDigits != null) {
               Utils.drawFrame(var1, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, Math.abs(var56), var2 + 14, var3, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 3);
         }

         var3 += var4;
         if (var5 > 0) {
            if (this.plusDigits != null) {
               Utils.drawFrame(var1, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, var5, var2 + 14, var3, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 3);
         } else if (var5 < 0) {
            if (this.minusDigits != null) {
               Utils.drawFrame(var1, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, Math.abs(var5), var2 + 14, var3, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 3);
         }

         var3 += var4;
         if (var42 > 0) {
            if (this.plusDigits != null) {
               Utils.drawFrame(var1, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, var42, var2 + 14, var3, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 3);
         } else if (var42 < 0) {
            if (this.minusDigits != null) {
               Utils.drawFrame(var1, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 10, var2, var3, 3, 0);
            }

            Utils.drawNumber(var1, Math.abs(var42), var2 + 14, var3, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 3);
         }

         var3 += var4;
         if (var7 <= 0) {
            if (var7 < 0) {
               if (this.minusDigits != null) {
                  Utils.drawFrame(var1, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 10, var2, var3, 3, 0);
               }

               Utils.drawNumber(var1, Math.abs(var7), var2 + 14, var3, this.minusDigits, this.minusDigits.getWidth() / 11, this.minusDigits.getHeight(), 3);
            }

            return;
         }

         if (this.plusDigits != null) {
            Utils.drawFrame(var1, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 10, var2, var3, 3, 0);
         }

         Utils.drawNumber(var1, var7, var2 + 14, var3, this.plusDigits, this.plusDigits.getWidth() / 11, this.plusDigits.getHeight(), 3);
      } else {
         int var38 = this.tabBarBounds[0] + 10;
         int var43 = this.tabBarBounds[1] + this.tabBarBounds[3] + 5;
         int var49 = this.tabBarBounds[0];
         int var54 = var43 + this.topPanelH + Utils.lineHeight;
         int var59 = Utils.lineHeight + 5;
         short var35 = Utils.lineHeight;
         Utils.drawOutlinedText(var1, "Trang bị ", var38, var43, 9240572, 2315140, 20);
         Utils.drawOutlinedText(var1, "Đá quý ", var38, var43 + 5 + 20, 16711585, 10570787, 20);
         drawSlotFrame(var1, this.tabBarBounds[0] + this.tabBarBounds[2] / 4, var43, 15589356, 6963805);
         drawSlotFrame(var1, this.tabBarBounds[0] + this.tabBarBounds[2] / 4, var43 + 5 + 20, 15589356, 6963805);
         int var66 = GameData.findEquipIndex(GameData.equipBagIds[equipSlotIndex][equipBagIndex]);
         if (Equip.icons[var66] != null) {
            var1.drawImage(Equip.icons[var66], this.tabBarBounds[0] + this.tabBarBounds[2] / 4 + 10, var43 + 10, 3);
            Utils.drawOutlinedText(var1, Equip.names[var66], this.tabBarBounds[0] + this.tabBarBounds[2] / 2, var43, 16311264, 5899082, 20);
         }

         if (selectedGemId > 0) {
            var1.drawImage(GameData.itemIcons[GameData.findItemIndex(selectedGemId)], this.tabBarBounds[0] + this.tabBarBounds[2] / 4 + 10, var43 + 5 + 30, 3);
            Utils.drawOutlinedText(
               var1,
               GameData.itemNames[GameData.findItemIndex(selectedGemId)],
               this.tabBarBounds[0] + this.tabBarBounds[2] / 2,
               var43 + 5 + 20,
               16311264,
               5899082,
               20
            );
         }

         int var76 = gemRows;
         if (gems == null) {
            var1.setColor(16777215);
            vh.drawString(var1, "Không có đá quý để chọn", DeviceConfig.screenSize[0] >> 1, var54 + (var59 >> 1), 17);
         } else {
            if (gems.length < gemRows) {
               var76 = gems.length;
            }

            Utils.drawOutlinedText(var1, "Nguyên liệu đá quý", var49 + 40, var54 - Utils.lineHeight, 16640843, 8997145, 17);
            Utils.drawOutlinedText(var1, "Số lượng", this.tabBarBounds[2] - 20, var54 - Utils.lineHeight, 16640843, 8997145, 17);

            for (int var83 = 0; var83 < var76; var83++) {
               drawSlotFrame(var1, var49 + 20, var54 + var59 * var83, 15589356, 6963805);
               if (var83 + gemScroll < gems.length) {
                  var2 = GameData.findItemIndex(gems[gemScroll + var83][0]);
                  var1.drawImage(GameData.itemIcons[var2], var49 + 30, var54 + 10 + var59 * var83, 3);
                  if (var83 + gemScroll == gemIndex) {
                     if (this.cursorArrow != null) {
                        var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var49 + 10 + this.cursorBob, var54 + 10 + var59 * var83, 3);
                     }

                     Utils.drawOutlinedText(var1, GameData.itemNames[var2], this.screenW >> 1, var54 + var59 * var83, 16773120, 15867651, 17);
                     this.textBox.addText(GameData.itemNames[var2], this.tabBarBounds[2] - 8, null, 14291704);
                     this.textBox.addText(GameData.itemDescriptions[var2], this.tabBarBounds[2] - 8, null, 16777215);
                  } else {
                     Utils.drawOutlinedText(var1, GameData.itemNames[var2], this.screenW >> 1, var54 + var59 * var83, 16311264, 5899082, 17);
                  }

                  Utils.drawNumber(var1, gems[gemScroll + var83][1], this.tabBarBounds[2] - 20, var54 + 10 + var59 * var83, this.digits, 6, 7, 3);
               }
            }

            if (gems.length > gemRows) {
               GameUtil.drawScrollBar(
                  var1,
                  null,
                  null,
                  this.tabBarBounds[0] + this.tabBarBounds[2] - 20,
                  var54 - 10 + (var59 >> 1),
                  gemRows * Utils.lineHeight,
                  gems.length,
                  gemRows,
                  gemScroll
               );
            }
         }

         String var81 = "Phím giữa chọn đá quý‚lên xuống để đổi đá quý‚OK để khảm‚Trở về để hủy";
         this.textBox.addText(var81, this.tabBarBounds[2], null, 16777215);
         this.textBox
            .setBounds(
               this.tabBarBounds[0] + 4,
               this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + this.bottomPanelH - 20,
               this.tabBarBounds[2] - 8,
               var35 - 4,
               1
            );
         this.textBox.draw(var1, this.textBox, 1, true, false);
         if (socketPending && socketResult == 0 && System.currentTimeMillis() - socketStartTime >= 1000L) {
            socketPending = false;
            GameData.removeItem(selectedGemId, 1);
            Equip var10000 = GameData.equipBag[equipSlotIndex][equipBagIndex];
            String var44 = GameData.itemStatBonus[GameData.findItemIndex(selectedGemId)];
            Equip var39 = var10000;
            short[] var50;
            if ((var50 = Utils.parseShortArray(var44, ","))[0] == 1) {
               var39.gemHp = (short)(var39.gemHp + var50[1]);
            } else if (var50[0] == 2) {
               var39.gemMp = (short)(var39.gemMp + var50[1]);
            } else if (var50[0] == 3) {
               var39.gemAttack = (short)(var39.gemAttack + var50[1]);
            } else if (var50[0] == 4) {
               var39.gemDefense = (short)(var39.gemDefense + var50[1]);
            } else if (var50[0] == 5) {
               var39.gemSpeed = (short)(var39.gemSpeed + var50[1]);
            } else if (var50[0] == 6) {
               var39.gemCrit = (short)(var39.gemCrit + var50[1]);
            }

            var39.gemCount++;
            selectedGemId = 0;
            socketResult = 1;
         }

         if (socketResult == 1) {
            GameUtil.drawMessageBox(var1, "Chúc mừng‚khảm trang bị thành công", this.screenW, this.screenH);
         }
      }
   }

   private void drawStatBars(Graphics var1, int var2, int var3, int var4, int[][] var5) {
      for (int var6 = 0; var6 < 3; var6++) {
         int var10 = var3 + var4 * var6;
         Utils.drawFrame(
            var1, this.statBarsImg, this.statBarsImg.getWidth() * var5[var6][0] / var5[var6][1], this.statBarsImg.getHeight() >> 2, var6, var2, var10, 20, 0
         );
         int var10003 = this.statBarsImg.getWidth();
         int var19 = this.statBarsImg.getHeight() >> 2;
         int var18 = 15988459;
         int var17 = 4664337;
         int var16 = var10003;
         var1.setColor(4664337);
         var1.drawRect(var2 - 1, var10 - 1, var16 + 2, var19 + 2);
         var1.setColor(15988459);
         var1.drawRoundRect(var2 - 2, var10 - 2, var16 + 3, var19 + 3, 4, 4);
         int var10002 = var5[var6][0];
         var10003 = var5[var6][1];
         int var10004 = var2 + this.attrIcons.getWidth() * 5 / 2;
         var18 = var10 - 4;
         var17 = var10004;
         var16 = var10003;
         int var15 = var10002;
         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var17, var18, 17);
         Utils.drawNumber(var1, var15, var17 - 3, var18, this.digits, 6, 7, 24);
         Utils.drawNumber(var1, var16, var17 + 3, var18, this.digits, 6, 7, 20);
         Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, var6, var2, var10 - 8, 20, 0);
      }
   }

   private void drawStatValues(Graphics var1, int var2, int var3, int var4, int var5, int[] var6) {
      for (int var7 = 0; var7 < 3; var7++) {
         int var11 = var3 + var7 * var4;
         var1.drawRegion(this.statBarsImg, 0, (this.statBarsImg.getHeight() >> 2) * 3, var5, this.statBarsImg.getHeight() >> 2, 0, var2, var11 + 3, 20);
         Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, var7 + 3, var2, var11 - 8, 20, 0);
         Utils.drawNumber(var1, var6[var7], var2 + this.attrIcons.getWidth(), var11 - 4, this.digits, 6, 7, 20);
      }
   }

   public final void resetImplant() {
      this.implantScrollRow = 0;
      this.implantMaterialIndex = 0;
      this.implantSlot = 0;
      this.implantMaterials = null;
      this.implantStep = 0;
      this.implantFxLeft.setFrame(0);
      this.implantFxRight.setFrame(0);
   }

   private void drawPetTab(Graphics var1) {
      if (pets == null) {
         var1.setColor(16777215);
         vh.drawString(var1, "Không có sủng vật", this.screenW / 2, this.screenH / 2 + this.bottomPanelH / 2, 17);
      } else {
         if (petPage != 0 && petPage != 1 && petPage != 3 && petPage != 8) {
            if (petPage == 2) {
               this.drawPetAttributes(var1, pets[this.petList.getSelectedSlot()]);
               return;
            }

            if (petPage == 5) {
               PetInfo var16 = pets[this.petList.getSelectedSlot()];
               if (this.petAnim != null) {
                  var1.setClip(this.tabBarBounds[0], this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2] / 2, this.topPanelH);
                  GameUtil.drawAnimation(
                     var1,
                     this.petAnim,
                     this.tabBarBounds[0] - 8 + (this.tabBarBounds[2] >> 2),
                     this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH,
                     true,
                     0
                  );
                  var1.setClip(0, 0, this.screenW, this.screenH);
               }

               int var23 = this.tabBarBounds[0] + this.tabBarBounds[2] / 2;
               int var37 = this.topPanelH / 4;
               int var30 = this.tabBarBounds[1] + this.tabBarBounds[3];
               Utils.drawOutlinedText8(var1, var16.name, var23 - 10, var30 + 4, 9502459, 540331, 20);
               var30 += var37;
               int[][] var7;
               (var7 = new int[3][2])[0][0] = var16.stats[1];
               var7[0][1] = var16.stats[2];
               var7[1][0] = var16.stats[4];
               var7[1][1] = var16.stats[5];
               var7[2][0] = var16.stats[13];
               var7[2][1] = var16.stats[14];
               this.drawStatBars(var1, var23 - 4, var30 + 10, var37, var7);
               if (petSkillPage != 0) {
                  if (petSkillPage == 1) {
                     if (this.textBox != null) {
                        this.textBox.clear();
                     } else {
                        this.textBox = new TextBlock();
                     }

                     int var18 = GameData.findSkillIndex(this.petSkillList.getRowId(this.petSkillList.getSelectedRow()));
                     if (Skill.icons == null) {
                        Skill.loadIcons();
                     }

                     var1.drawImage(Skill.icons[var18], this.tabBarBounds[0] + 10, this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2, 20);
                     Utils.drawOutlinedText(
                        var1,
                        Skill.names[var18],
                        this.tabBarBounds[0] + 15 + Skill.icons[var18].getWidth(),
                        this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2,
                        16580583,
                        6036810,
                        20
                     );
                     if (this.minusDigits == null) {
                        this.minusDigits = ResourceCache.createImageUncached("/pic/number05.gf", 2);
                     }

                     Utils.drawOutlinedText(
                        var1,
                        "" + pets[this.petList.getSelectedSlot()].skills[this.petSkillList.getSelectedRow()].level,
                        this.tabBarBounds[0] + 58 + this.tabBarBounds[2] / 2,
                        this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2,
                        16681647,
                        8791075,
                        24
                     );
                     Utils.drawOutlinedText(
                        var1,
                        " cấp",
                        this.tabBarBounds[0] + 61 + this.tabBarBounds[2] / 2,
                        this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + 2,
                        16580583,
                        6036810,
                        20
                     );
                     this.textBox.addText(Skill.descriptions[var18], this.frameW - 10, null, 16777215);
                     this.textBox
                        .setBounds(
                           this.tabBarBounds[0] + 4,
                           this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH + Utils.lineHeight,
                           this.frameW - 10,
                           this.bottomPanelH - Utils.lineHeight - 3,
                           (this.bottomPanelH - Utils.lineHeight - 3) / Utils.lineHeight
                        );
                     this.textBox.draw(var1, this.textBox, 1, true, false);
                  }

                  return;
               }

               var23 = this.tabBarBounds[0];
               var30 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH;
               if (this.petSkillList != null) {
                  this.petSkillList.draw(var1);
               }

               var23 += this.contentW / 2;
               var30 += this.bottomPanelH;
               if (this.proficiencyImg != null) {
                  var1.drawImage(this.proficiencyImg, var23, var30, 33);
                  var23 += this.proficiencyImg.getWidth() + 15;
               }

               Utils.drawNumber(var1, pets[this.petList.getSelectedSlot()].skills[this.petSkillList.getSelectedRow()].exp, var23, var30, this.digits, 6, 7, 40);
               var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var23, var30, 36);
               int var17 = 0;
               if (!Utils.contains(new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38}, this.petSkillList.getRowId(this.petSkillList.getSelectedRow()))) {
                  var17 = pets[this.petList.getSelectedSlot()].skills[this.petSkillList.getSelectedRow()].expToNext;
               }

               Utils.drawNumber(var1, var17, var23 + 6, var30, this.digits, 6, 7, 36);
            } else if (petPage == 7) {
               int var9 = this.tabBarBounds[0] + 7;
               int var19 = this.tabBarBounds[1] + this.tabBarBounds[3] + 6;
               Utils.drawOutlinedText8(var1, pets[this.petList.getSelectedSlot()].name, this.screenW >> 1, var19, 9502459, 540331, 17);
               if (this.petAnim != null) {
                  GameUtil.drawAnimation(var1, this.petAnim, this.screenW >> 1, this.screenH >> 1, true, 0);
               }

               this.implantFxLeft.setPosition(var9 + 23, this.tabBarBounds[1] + this.tabBarBounds[3] + this.bodyH / 2);
               this.implantFxLeft.draw(var1);
               if (this.showImplantFxLeft) {
                  this.implantFxLeft.nextFrame(true);
               }

               this.implantFxRight.setPosition(var9 + (this.tabBarBounds[2] - 34), this.tabBarBounds[1] + this.tabBarBounds[3] + this.bodyH / 2);
               this.implantFxRight.draw(var1);
               if (this.showImplantFxRight) {
                  this.implantFxRight.nextFrame(true);
               }

               if (this.implantFxLeft.getFrame() >= this.implantFxLeft.getFrameCount() - 1) {
                  this.showImplantFxLeft = false;
               }

               if (this.implantFxRight.getFrame() >= this.implantFxRight.getFrameCount() - 1) {
                  this.showImplantFxRight = false;
               }

               if (this.implantFxLeft.getFrame() >= this.implantFxLeft.getFrameCount() - 1
                  || this.implantFxRight.getFrame() >= this.implantFxRight.getFrameCount() - 1) {
                  this.implantStep = 2;
               }

               for (int var26 = 0; var26 < 2; var26++) {
                  drawSlotFrame(var1, var9 + (this.tabBarBounds[2] - 34) * var26, var19, 15589356, 6963805);
                  int var34;
                  if (pets[this.petList.getSelectedSlot()].implantSlots[var26][1] != 0
                     && (var34 = GameData.findItemIndex(pets[this.petList.getSelectedSlot()].implantSlots[var26][1])) != -1
                     && GameData.itemIcons[var34] != null) {
                     var1.drawImage(GameData.itemIcons[var34], var9 + 11 + (this.tabBarBounds[2] - 34) * var26, var19 + 11, 3);
                  }
               }

               if (this.implantStep == 0 && this.cursorArrow != null) {
                  var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var9 + this.implantSlot * (this.tabBarBounds[2] - 34) + this.cursorBob, var19 + 11, 3);
                  int var28 = this.tabBarBounds[1] + this.tabBarBounds[3] + (this.topPanelH << 1) / 3;
                  int var36 = this.topPanelH / 3;
                  if (this.textBox != null) {
                     this.textBox.clear();
                  } else {
                     this.textBox = new TextBlock();
                  }

                  if (pets[this.petList.getSelectedSlot()].implantSlots[this.implantSlot][1] != 0) {
                     var9 = GameData.findItemIndex(pets[this.petList.getSelectedSlot()].implantSlots[this.implantSlot][1]);
                     if (this.implantMaterials != null && this.implantMaterialIndex < this.implantMaterials.length) {
                        String var40 = GameData.itemNames[var9];
                        if (pets[this.petList.getSelectedSlot()].implantSlots[this.implantSlot][0] != 0) {
                           var40 = var40 + "[Đã cấy]";
                        }

                        this.textBox.addText(var40, this.tabBarBounds[2] - 12, null, 9502459);
                        this.textBox.addText(GameData.itemDescriptions[var9], this.tabBarBounds[2] - 12, null, 9502459);
                     }
                  }

                  this.textBox.addText("Phím giữa chọn vật phẩm‚ chọn nguyên liệu cấy xong thì phím mềm trái để cấy", this.tabBarBounds[2] - 12, null, 9502459);
                  this.textBox.setBounds(this.tabBarBounds[0] + 4, var28, this.tabBarBounds[2] - 12, var36, var36 / Utils.lineHeight);
                  this.textBox.draw(var1, this.textBox, 1, true, false);
                  return;
               }

               if (this.implantStep == 1) {
                  var1.setColor(2303014);
                  var1.fillRect(this.tabBarBounds[0] + 3, this.tabBarBounds[1] + this.tabBarBounds[3] + 105, this.frameW - 3, 2);
                  var1.setColor(14083300);
                  var1.fillRect(this.tabBarBounds[0] + 4, this.tabBarBounds[1] + this.tabBarBounds[3] + 106, this.frameW - 5, 1);
                  var1.drawImage(this.implantBoxBg, this.tabBarBounds[0] + 3, this.tabBarBounds[1] + this.tabBarBounds[3] + this.bodyH / 2, 20);
                  int var27 = this.tabBarBounds[0] + 15;
                  int var35 = this.tabBarBounds[1] + this.tabBarBounds[3] + 115;

                  for (int var38 = 0; var38 < gridRows; var38++) {
                     for (int var10 = 0; var10 < gridCols; var10++) {
                        drawSlotFrame(var1, var27 + var10 * 26, var35 + var38 * 26, 15589356, 6963805);
                        if (this.implantMaterials != null && this.implantScrollRow * gridCols + var10 + var38 * gridCols < this.implantMaterials.length) {
                           if ((var19 = GameData.findItemIndex(this.implantMaterials[this.implantScrollRow * gridCols + var10 + var38 * gridCols][0])) != -1
                              && GameData.itemIcons[var19] != null) {
                              var1.drawImage(GameData.itemIcons[var19], var27 + 11 + var10 * 26, var35 + 11 + var38 * 26, 3);
                           }

                           Utils.drawNumber(
                              var1,
                              this.implantMaterials[this.implantScrollRow * gridCols + var10 + var38 * gridCols][1],
                              var27 + 11 + var10 * 26,
                              var35 + 20 + var38 * 26,
                              this.digits,
                              6,
                              7,
                              33
                           );
                        }
                     }
                  }

                  if (this.textBox != null) {
                     this.textBox.clear();
                  } else {
                     this.textBox = new TextBlock();
                  }

                  int var39 = this.implantBoxBg.getHeight() - 26 * gridRows;
                  if (this.implantMaterials != null && this.implantMaterialIndex < this.implantMaterials.length) {
                     var9 = GameData.findItemIndex(this.implantMaterials[this.implantMaterialIndex][0]);
                     this.textBox.addText(GameData.itemNames[var9], this.tabBarBounds[2] - 12, null, 13761017);
                     this.textBox.addText(GameData.itemDescriptions[var9], this.tabBarBounds[2] - 12, null, 13761017);
                     this.textBox.setBounds(this.tabBarBounds[0] + 4, var35 + 22 * gridRows, this.tabBarBounds[2] - 12, var39, var39 / Utils.lineHeight);
                     this.textBox.draw(var1, this.textBox, 1, true, false);
                  }

                  short var12 = (short)(this.implantMaterialIndex % gridCols);
                  short var21 = (short)(this.implantMaterialIndex / gridCols - this.implantScrollRow);
                  if (this.cursorArrow != null) {
                     var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, var27 + var12 * 26 + this.cursorBob, var35 + 11 + var21 * 26, 3);
                  }

                  GameUtil.drawScrollBar(
                     var1, null, null, this.tabBarBounds[0] + this.tabBarBounds[2] - 20, var35, 26 * gridRows, 18 / gridCols, gridRows, this.implantScrollRow
                  );
                  var1.setClip(0, 0, this.screenW, this.screenH);
                  return;
               }

               if (this.implantStep == 2) {
                  GameUtil.drawMessageBox(
                     var1, "Chúc mừng bạn," + pets[this.petList.getSelectedSlot()].name + " Cấy ghép thành công!! ", this.screenW, this.screenH
                  );
               }
            }
         } else {
            if (this.petList != null) {
               this.petList.draw(var1);
               int var3 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH;
               int var4 = this.bottomPanelH / 4;
               if (this.petAnim != null) {
                  var1.setClip(this.tabBarBounds[0], var3, this.contentW / 2, this.bottomPanelH);
                  GameUtil.drawAnimation(var1, this.petAnim, this.tabBarBounds[0] - 10 + this.tabBarBounds[2] / 4, var3 + this.bottomPanelH, true, 0);
                  var1.setClip(0, 0, this.screenW, this.screenH);
               }

               int var5 = this.tabBarBounds[0] + this.tabBarBounds[2] / 2;
               int var2 = this.tabBarBounds[2] / 8;
               if (pets[this.petList.getSelectedSlot()].elementValues != null && this.elementIcons != null) {
                  if (pets[this.petList.getSelectedSlot()].elementValues[0] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var5,
                        var3 + var4 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        pets[this.petList.getSelectedSlot()].elementValues[0],
                        var5 + this.elementIcons.getWidth() / 2,
                        var3 + var4 / 2,
                        this.digits,
                        6,
                        7,
                        6
                     );
                     var5 += var2;
                  }

                  if (pets[this.petList.getSelectedSlot()].elementValues[1] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        this.elementIcons.getWidth() / 2,
                        0,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var5,
                        var3 + var4 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        pets[this.petList.getSelectedSlot()].elementValues[1],
                        var5 + this.elementIcons.getWidth() / 2,
                        var3 + var4 / 2,
                        this.digits,
                        6,
                        7,
                        6
                     );
                     var5 += var2;
                  }

                  if (pets[this.petList.getSelectedSlot()].elementValues[2] > 0) {
                     var1.drawRegion(this.elementIcons, 0, 0, this.elementIcons.getWidth() / 2, this.elementIcons.getHeight() / 2, 0, var5, var3 + var4 / 2, 6);
                     Utils.drawNumber(
                        var1,
                        pets[this.petList.getSelectedSlot()].elementValues[2],
                        var5 + this.elementIcons.getWidth() / 2,
                        var3 + var4 / 2,
                        this.digits,
                        6,
                        7,
                        6
                     );
                     var5 += var2;
                  }

                  if (pets[this.petList.getSelectedSlot()].elementValues[3] > 0) {
                     var1.drawRegion(
                        this.elementIcons,
                        0,
                        this.elementIcons.getHeight() / 2,
                        this.elementIcons.getWidth() / 2,
                        this.elementIcons.getHeight() / 2,
                        0,
                        var5,
                        var3 + var4 / 2,
                        6
                     );
                     Utils.drawNumber(
                        var1,
                        pets[this.petList.getSelectedSlot()].elementValues[3],
                        var5 + this.elementIcons.getWidth() / 2,
                        var3 + var4 / 2,
                        this.digits,
                        6,
                        7,
                        6
                     );
                  }
               }

               var2 = this.tabBarBounds[0] + this.tabBarBounds[2] / 2 - 4;
               var3 = var3 + var4 + 10;
               int[][] var6;
               (var6 = new int[3][2])[0][0] = pets[this.petList.getSelectedSlot()].stats[1];
               var6[0][1] = pets[this.petList.getSelectedSlot()].stats[2];
               var6[1][0] = pets[this.petList.getSelectedSlot()].stats[4];
               var6[1][1] = pets[this.petList.getSelectedSlot()].stats[5];
               var6[2][0] = pets[this.petList.getSelectedSlot()].stats[13];
               var6[2][1] = pets[this.petList.getSelectedSlot()].stats[14];
               this.drawStatBars(var1, var2, var3, var4, var6);
            }

            if (petPage != 0) {
               if (petPage == 1) {
                  short var15 = (short)(10 + vh.stringWidth(Utils.defaultFont, "Thuộc tính"));
                  short var22 = (short)((petActionIds.length << 4) + (petActionNames.length - 1 << 1));
                  short var29 = (short)((this.screenH - var22) / 2 - 2 * Utils.lineHeight);
                  this.drawActionMenu(var1, petActionIds, petActionIndex, this.screenW / 2, var29 + 6, var15, var22, 6768205);
                  return;
               }

               if (petPage == 3) {
                  GameUtil.drawMessageBox(var1, "Chắc chắn thả sủng vật: " + pets[this.petList.getSelectedSlot()].name + "?", this.screenW, this.screenH);
                  return;
               }

               if (petPage == 8) {
                  GameUtil.drawMessageBox(
                     var1, "Chiết xuất gen năng lực của sủng vật này? Sau khi chiết xuất sủng vật sẽ biến mất.", this.screenW, this.screenH
                  );
                  return;
               }

               if (petPage == 4) {
                  GameUtil.drawMessageBox(var1, petMessage, this.screenW, this.screenH);
                  return;
               }
            }
         }
      }
   }

   private void drawPetAttributes(Graphics var1, PetInfo var2) {
      this.drawElements(var1, var2.elementValues);
      int var3 = this.tabBarBounds[0] + 4;
      int var5 = this.bottomPanelH / 5;
      int var4 = this.tabBarBounds[1] + this.tabBarBounds[3] + this.topPanelH;
      if (this.petAnim != null) {
         var1.setClip(var3, this.tabBarBounds[1] + this.tabBarBounds[3], this.tabBarBounds[2] / 2, this.topPanelH);
         GameUtil.drawAnimation(var1, this.petAnim, var3 - 8 + (this.tabBarBounds[2] >> 2), var4, true, 0);
         var1.setClip(0, 0, this.screenW, this.screenH);
      }

      Utils.drawOutlinedText8(var1, var2.name, var3 + 4, var4, 9502459, 540331, 36);
      Utils.drawOutlinedText8(var1, var2.stats[0] + "  cấp", var3 + 12 + vh.stringWidth(Utils.defaultFont, var2.name), var4, 16646122, 6102605, 36);
      if (this.geneLabelImg != null) {
         var1.drawImage(this.geneLabelImg, var3 + 4, var4 + var5 * 3, 20);
      } else {
         Utils.drawOutlinedText8(var1, "Gen đặc trưng:", var3 + 4, var4 + var5 * 3, 16706636, 9128217, 20);
      }

      if (GameData.itemNames == null) {
         GameData.loadItemTable();
      }

      if (this.textBox != null) {
         this.textBox.clear();
      } else {
         this.textBox = new TextBlock();
      }

      if (var2.refineItemIds == null) {
         if (petData == null) {
            petData = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
         }

         var2.refineItemIds = Utils.parseByteArray(petData[var2.petId - 1][18], ",");
      }

      int var6 = GameData.itemIcons[0].getWidth() + 10;

      for (int var7 = 0; var7 < 3; var7++) {
         drawSlotFrame(var1, this.tabBarBounds[0] + this.tabBarBounds[2] / 2 - 2 + var6 * var7, var4 - 2 + var5 * 3, 15589356, 6963805);
      }

      for (int var10 = 0; var10 < this.geneSlotCount; var10++) {
         if (var10 == 0) {
            int var8 = GameData.findItemIndex(var2.refineItemIds[0]);
            var1.setColor(16777215);
            var1.drawImage(GameData.itemIcons[var8], this.tabBarBounds[0] + this.tabBarBounds[2] / 2 + var6 * var10, var4 + 2 + var5 * 3, 20);
         } else {
            int var15;
            if ((var15 = GameData.findItemIndex(var2.implantSlots[var10 - 1][1])) != -1) {
               var1.drawImage(GameData.itemIcons[var15], this.tabBarBounds[0] + this.tabBarBounds[2] / 2 + var6 * var10, var4 + 2 + var5 * 3, 20);
            }
         }
      }

      if (this.cursorArrow != null) {
         var1.drawRegion(
            this.cursorArrow, 0, 0, 15, 15, 0, this.tabBarBounds[0] + this.tabBarBounds[2] / 2 + this.cursorBob + var6 * this.geneIndex, var4 + 8 + var5 * 3, 3
         );
      }

      if (this.geneIndex == 0) {
         int var11 = GameData.findItemIndex(var2.refineItemIds[0]);
         var1.setColor(16777215);
         this.textBox.addText(GameData.itemNames[var11] + GameData.itemDescriptions[var11], this.tabBarBounds[2] - 10, null, 9502459);
      } else if (this.geneIndex == 1) {
         if (var2.implantSlots[0][0] != 0) {
            int var12 = GameData.findItemIndex(var2.implantSlots[0][1]);
            this.textBox.addText(GameData.itemDescriptions[var12], this.tabBarBounds[2] - 10, null, 9502459);
         }
      } else if (this.geneIndex == 2 && var2.implantSlots[1][0] != 0) {
         int var13 = GameData.findItemIndex(var2.implantSlots[1][1]);
         this.textBox.addText(GameData.itemDescriptions[var13], this.tabBarBounds[2] - 10, null, 9502459);
      }

      this.textBox.setBounds(this.tabBarBounds[0] + 4, var4 + var5 * 4, this.tabBarBounds[2] - 10, var5 * 2, 1);
      this.textBox.draw(var1, this.textBox, 1, true, false);
      var4 += 10;
      int[][] var14;
      (var14 = new int[3][2])[0][0] = var2.stats[1];
      var14[0][1] = var2.stats[2];
      var14[1][0] = var2.stats[4];
      var14[1][1] = var2.stats[5];
      var14[2][0] = var2.stats[13];
      var14[2][1] = var2.stats[14];
      int[] var16;
      (var16 = new int[3])[0] = var2.stats[7];
      var16[1] = var2.stats[9];
      var16[2] = var2.stats[11];
      this.drawStatBars(var1, var3, var4, var5, var14);
      this.drawStatValues(var1, var3 + 2 + this.tabBarBounds[2] / 2, var4, var5, this.frameW / 2 - 8, var16);
   }

   private void drawStatusTab(Graphics var1) {
      this.drawElements(var1, hero.elementValues);
      int[][] var2;
      (var2 = new int[3][2])[0][0] = hero.stats[3];
      var2[0][1] = hero.stats[10];
      var2[1][0] = hero.stats[5];
      var2[1][1] = hero.stats[11];
      var2[2][0] = hero.stats[1];
      var2[2][1] = hero.stats[2];
      int[] var3;
      (var3 = new int[3])[0] = hero.stats[12];
      var3[1] = hero.stats[13];
      var3[2] = hero.stats[14];
      int[] var4 = new int[]{hero.stats[16], hero.stats[17], hero.stats[18], hero.stats[19], hero.stats[20]};
      int var5 = this.tabBarBounds[0] + 4;
      int var6 = this.tabBarBounds[1] + this.tabBarBounds[3] + 4;
      int var7 = (this.contentW >> 1) - 12;
      int var8 = this.topPanelH - 26;
      this.drawHeroPortrait(var1, var5 + 5, var6, var7, var8);
      var5 = this.tabBarBounds[0] + 8;
      var7 = this.bottomPanelH / 6;
      var6 = this.tabBarBounds[1] + this.tabBarBounds[3] + 90 + (var7 >> 1) + 3;
      this.drawStatBars(var1, var5, var6, var7, var2);
      this.drawStatValues(var1, var5, var6 - 5 + var7 * 3, var7, this.statBarsImg.getWidth(), var3);
      var5 += (this.contentW >> 1) + 8;
      int var9 = this.bottomPanelH / 5;

      for (int var11 = 0; var11 < 5; var11++) {
         if (var11 == 0) {
            var1.drawImage(this.freePointsImg, var5, var6 - 8 + var9 * var11, 20);
            Utils.drawNumber(var1, var4[var11], var5 + this.freePointsImg.getWidth() * 3 / 2, var6 - 5 + var9 * var11, this.goldDigits, 6, 8, 20);
         } else {
            if (this.pendingStatPoints[var11 - 1] > 0) {
               Utils.drawNumber(
                  var1,
                  this.pendingStatPoints[var11 - 1],
                  this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4,
                  var6 + 4 + var9 * var11,
                  this.plusDigits,
                  7,
                  7,
                  24
               );
            }

            Utils.drawFrame(var1, this.attrIcons, this.attrIcons.getWidth(), this.attrIcons.getHeight() / 10, var11 + 5, var5, var6 - 10 + var9 * var11, 20, 0);
            Utils.drawNumber(var1, var4[var11], var5, var6 + 4 + var9 * var11, this.digits, 6, 7, 20);
         }
      }

      if (menuPage == 2) {
         int var12 = this.tabBarBounds[0] + this.tabBarBounds[2] * 3 / 4;
         var1.drawImage(this.statSelectImg, var12, var6 + 4 + var9 * (this.statRowIndex + 1), 20);
         Utils.drawFrame(
            var1,
            this.leftRightArrows,
            this.leftRightArrows.getWidth() / 2,
            this.leftRightArrows.getHeight(),
            0,
            var12 + 15 + this.cursorBob,
            var6 + 4 + var9 * (this.statRowIndex + 1),
            24,
            0
         );
         Utils.drawNumber(
            var1, this.pendingStatPoints[this.statRowIndex], var12 + 15 + 10, var6 + 4 + var9 * (this.statRowIndex + 1), this.plusDigits, 7, 7, 17
         );
         Utils.drawFrame(
            var1,
            this.leftRightArrows,
            this.leftRightArrows.getWidth() / 2,
            this.leftRightArrows.getHeight(),
            1,
            var12 + 15 + 20 - this.cursorBob,
            var6 + 4 + var9 * (this.statRowIndex + 1),
            20,
            0
         );
         var5 -= this.tabBarBounds[2] / 4;

         for (int var10 = 0; var10 < 6; var10++) {
            if (var10 < 3) {
               if (var10 < 2 && this.pendingStatBonus[var10] > 0) {
                  Utils.drawFrame(var1, this.plusDigits, 7, 7, 10, var5 + 18, var6 - 8 + var10 * var7, 20, 0);
                  Utils.drawNumber(var1, this.pendingStatBonus[var10], var5 + 30, var6 - 8 + var10 * var7, this.plusDigits, 7, 7, 20);
               }
            } else if (this.pendingStatBonus[var10 - 1] > 0) {
               Utils.drawFrame(var1, this.plusDigits, 7, 7, 10, var5 + 10, var6 - 8 + var10 * var7, 20, 0);
               Utils.drawNumber(var1, this.pendingStatBonus[var10 - 1], var5 + 20, var6 - 8 + var10 * var7, this.plusDigits, 7, 7, 20);
            }
         }
      }
   }

   private void drawHeroPortrait(Graphics var1, int var2, int var3, int var4, int var5) {
      var1.setClip(var2, var3, var4, var5);
      GameUtil.drawAnimation(var1, previewAnim, var2 + 3 + (var4 >> 1), var3 + var5, true, 0);
      var1.setClip(0, 0, this.screenW, this.screenH);

      for (int var6 = 0; var6 < 2; var6++) {
         var1.setColor(4338001);
         var1.fillRect(var2, var3 + var5, var4, 16);
      }

      var1.setColor(10326691);
      var1.drawRoundRect(var2, var3, var4, var5 + 16, 4, 4);
      var1.setColor(14208729);
      var1.drawRoundRect(var2, var3, var4, var5, 4, 4);
      var1.setColor(2303014);
      var1.drawRoundRect(var2 - 1, var3 - 1, var4 + 2, var5 + 16 + 2, 4, 4);
      if (this.classNameImg != null) {
         Utils.drawFrame(
            var1, this.classNameImg, this.classNameImg.getWidth(), 14, hero.roleId - 1, var2 + 3, var3 + var5 + this.classNameImg.getHeight() / 2 + 2, 36, 0
         );
      } else {
         Utils.drawOutlinedText(var1, hero.name, var2 + 3, var3 + var5, 16776937, 257041742, 20);
      }

      Utils.drawNumber(var1, hero.stats[0], var2 + (var4 >> 1), var3 + var5 + 14, this.digits, 6, 7, 36);
      var1.drawImage(this.levelImg, var2 + var4, var3 + var5 + 14, 40);
   }

   private void drawElements(Graphics var1, byte[] var2) {
      GameUtil.drawAnimation(
         var1, elementWheelAnim, this.tabBarBounds[0] + this.contentW - 3 - (this.contentW >> 2), this.tabBarBounds[1] - 3 + this.tabBarBounds[3] + 45, true, 0
      );
      if (var2[0] > 0) {
         var1.drawRegion(
            this.digits, 66, 0, 6, 7, 0, this.tabBarBounds[0] + (this.contentW << 1) / 3 - 13, this.tabBarBounds[1] + this.tabBarBounds[3] + 60 - 2, 3
         );
         Utils.drawNumber(
            var1, var2[0], this.tabBarBounds[0] + (this.contentW << 1) / 3 - 4, this.tabBarBounds[1] + this.tabBarBounds[3] + 60 - 2, this.digits, 6, 7, 3
         );
         var1.drawRegion(
            this.digits, 72, 0, 6, 7, 0, this.tabBarBounds[0] + (this.contentW << 1) / 3 + 3, this.tabBarBounds[1] + this.tabBarBounds[3] + 60 - 2, 3
         );
      }

      if (var2[1] > 0) {
         var1.drawRegion(this.digits, 66, 0, 6, 7, 0, this.tabBarBounds[0] + this.contentW * 3 / 4 - 6, this.tabBarBounds[1] + this.tabBarBounds[3] + 30 - 2, 3);
         Utils.drawNumber(
            var1, var2[1], this.tabBarBounds[0] + this.contentW * 3 / 4 + 2, this.tabBarBounds[1] + this.tabBarBounds[3] + 30 - 3, this.digits, 6, 7, 3
         );
         var1.drawRegion(
            this.digits, 72, 0, 6, 7, 0, this.tabBarBounds[0] + this.contentW * 3 / 4 + 10, this.tabBarBounds[1] + this.tabBarBounds[3] + 30 - 2, 3
         );
      }

      if (var2[3] > 0) {
         var1.drawRegion(
            this.digits, 66, 0, 6, 7, 0, this.tabBarBounds[0] + this.contentW * 3 / 4 - 6, this.tabBarBounds[1] + this.tabBarBounds[3] + 90 - 3 - 2, 3
         );
         Utils.drawNumber(
            var1, var2[3], this.tabBarBounds[0] + this.contentW * 3 / 4 + 2, this.tabBarBounds[1] + this.tabBarBounds[3] + 90 - 5, this.digits, 6, 7, 3
         );
         var1.drawRegion(
            this.digits, 72, 0, 6, 7, 0, this.tabBarBounds[0] + this.contentW * 3 / 4 + 10, this.tabBarBounds[1] + this.tabBarBounds[3] + 90 - 3 - 2, 3
         );
      }

      if (var2[2] > 0) {
         var1.drawRegion(
            this.digits,
            66,
            0,
            6,
            7,
            0,
            this.tabBarBounds[0] + this.contentW - (this.contentW >> 4) - 8,
            this.tabBarBounds[1] + this.tabBarBounds[3] + 60 - 2,
            3
         );
         Utils.drawNumber(
            var1,
            var2[2],
            this.tabBarBounds[0] + this.contentW - (this.contentW >> 4),
            this.tabBar.getBounds()[1] + this.tabBarBounds[3] + 60 - 2,
            this.digits,
            6,
            7,
            3
         );
         var1.drawRegion(
            this.digits,
            72,
            0,
            6,
            7,
            0,
            this.tabBarBounds[0] + this.contentW - (this.contentW >> 4) + 8,
            this.tabBarBounds[1] + this.tabBarBounds[3] + 60 - 2,
            3
         );
      }
   }

   public final void setSoftKeyFrame(int var1) {
      this.softKeyFrame = var1;
   }

   private void drawQuantityDialog(Graphics var1, boolean var2, int var3, int var4, int var5) {
      int var6 = this.screenH / 3;
      int var7 = (this.screenW - 140) / 2;
      var1.setColor(1381651);
      var1.fillRoundRect(var7 - 2, var6 - 2, 143, 59, 8, 8);
      var1.setColor(16053217);
      var1.drawRoundRect(var7, var6, 140, 56, 8, 8);
      var1.setColor(6702925);
      var1.fillRect(var7 + 2, var6 + 2, 137, 53);
      Utils.drawOutlinedText(var1, "Số lượng:", var7 + 10, var6 + 8, 2547412, 411780, 20);
      if (this.leftRightArrows == null) {
         this.leftRightArrows = ResourceCache.loadImage("/pic/D11.gf");
      }

      if (this.leftRightArrows != null) {
         var1.drawRegion(
            this.leftRightArrows,
            0,
            0,
            this.leftRightArrows.getWidth() / 2,
            this.leftRightArrows.getHeight(),
            0,
            var7 + vh.stringWidth(Utils.defaultFont, "Số lượng:") + 20 - this.cursorBob,
            var6 + 8 + Utils.lineHeight / 2,
            3
         );
         var1.drawRegion(this.digits, 60, 0, 6, 7, 0, var7 + vh.stringWidth(Utils.defaultFont, "Số lượng:") + 20 + 25, var6 + 8 + Utils.lineHeight / 2, 3);
         Utils.drawNumber(var1, var5, var7 + vh.stringWidth(Utils.defaultFont, "Số lượng:") + 20 + 12, var6 + 8 + Utils.lineHeight / 2, this.digits, 6, 7, 3);
         Utils.drawNumber(
            var1, var3, var7 + vh.stringWidth(Utils.defaultFont, "Số lượng:") + 20 + 25 + 12, var6 + 8 + Utils.lineHeight / 2, this.digits, 6, 7, 3
         );
         var1.drawRegion(
            this.leftRightArrows,
            this.leftRightArrows.getWidth() / 2,
            0,
            this.leftRightArrows.getWidth() / 2,
            this.leftRightArrows.getHeight(),
            0,
            var7 + vh.stringWidth(Utils.defaultFont, "Số lượng:") + 70 + this.cursorBob,
            var6 + 8 + Utils.lineHeight / 2,
            3
         );
      }

      if (var2) {
         if (this.coinIcon != null) {
            var1.drawImage(this.coinIcon, var7 + 16, var6 + 8 + Utils.lineHeight + 6, 20);
         }

         Utils.drawNumber(var1, var4 * var5, this.screenW / 2, var6 + 8 + Utils.lineHeight + 6 + this.coinIcon.getHeight() / 2, this.digits, 6, 7, 3);
      }
   }

   private void drawActionMenu(Graphics var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      var1.setColor(1381651);
      var1.fillRoundRect((this.screenW - var6) / 2 - 1, var5 - 1, var6 + 3, var7 + 3, 8, 8);
      var1.setColor(16053217);
      var1.drawRoundRect((this.screenW - var6) / 2, var5, var6, var7, 8, 8);
      var1.setColor(6768205);
      var1.fillRect((this.screenW - var6) / 2 + 2, var5 + 2, var6 - 3, var7 - 3);

      for (int var9 = 0; var9 < var2.length; var9++) {
         if (var9 == var3) {
            if (this.actionMenuImgs[1] != null) {
               Utils.drawFrame(
                  var1,
                  this.actionMenuImgs[1],
                  this.actionMenuImgs[1].getWidth(),
                  this.actionMenuImgs[1].getHeight() / 17,
                  var2[var9],
                  var4,
                  var5 + 5 + var9 * 17,
                  17,
                  0
               );
            } else {
               Utils.drawOutlinedText(var1, this.actionLabels[var2[var9]], var4 + 3, var5 + 1 + var9 * 17, 12093825, 16777215, 17);
            }

            if (this.cursorArrow != null) {
               var1.drawRegion(this.cursorArrow, 0, 0, 15, 15, 0, (this.screenW - var6) / 2 + this.cursorBob, var5 + 5 + 6 + var9 * 17, 3);
            }
         } else if (this.actionMenuImgs[0] != null) {
            Utils.drawFrame(
               var1,
               this.actionMenuImgs[0],
               this.actionMenuImgs[0].getWidth(),
               this.actionMenuImgs[0].getHeight() / 17,
               var2[var9],
               var4,
               var5 + 5 + var9 * 17,
               17,
               0
            );
         } else {
            Utils.drawOutlinedText(var1, this.actionLabels[var2[var9]], var4 + 3, var5 + 1 + var9 * 17, 9306107, 2380419, 17);
         }
      }
   }

   public final void onResult(int var1, String var2) {
      if (var1 == 0) {
         pets[this.petList.getSelectedIndex()].name = var2;
         this.buildPetList();
         petPage = 0;
      }
   }

   public final void showToast(String var1, int var2) {
      this.toastText = var1;
      this.toastTimer = var2;
      this.isToastVisible = true;
   }
}
