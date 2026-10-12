import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;

public final class TitleScreen {
   private static byte page;
   private byte menuIndex = 0;
   private String[] menuItems;
   private byte loadTargetPercent;
   private byte loadPercent;
   private AnimSprite startAnim;
   private AnimSprite logoAnim;
   private AnimSprite loadingAnim;
   private AnimSprite volumeAnim;
   private Image backgroundImage;
   private Image menuLabelImage;
   private Image menuArrowsImage;
   private String helpText;
   private String[] helpLines;
   private String[] aboutLines;
   private boolean hasSave;
   private byte[] saveSlots;
   private String[] saveSlotNames;
   private String[] shownMenuItems;
   private byte blinkFrame;
   private boolean isEnteringGame;
   boolean isLoaded = false;
   private byte slotIndex;
   private short helpScrollY;
   private String[] recommendedGames;
   private String[] recommendedUrls;
   private byte recommendedIndex;
   private Image topBarImage;
   private Image levelLabelImage;
   private Image softKeyIcons;
   private Image digitsImage;
   private Image heroNamesImage;
   private Image cursorImage;
   private Image musicIcon;
   private Image volumeArrowsImage;
   private AnimSprite[] slotHeroAnims;
   private TextBlock noticeBox;
   private int cursorBob = 0;
   private byte recommendedScroll;
   private int recommendedRows;

   public TitleScreen() {
      if (DeviceConfig.allowMusic) {
         String[] var10000 = new String[]{"Bật/tắt nhạc", "Trở về"};
         if (ChannelConfig.addMenuText != null && !ChannelConfig.addMenuText.equals("")) {
            this.menuItems = new String[]{
               "Chơi mới",
               "Chơi tiếp",
               "Cài đặt",
               "Cộng đồng QQ",
               "Bảng xếp hạng",
               ChannelConfig.addMenuText,
               "Game hay đề cử",
               "Hướng dẫn",
               "Giới thiệu",
               "Thoát game"
            };
         } else {
            this.menuItems = new String[]{
               "Chơi mới", "Chơi tiếp", "Cài đặt", "Cộng đồng QQ", "Bảng xếp hạng", "Trung tâm game QQ", "Hướng dẫn", "Giới thiệu", "Thoát game"
            };
         }
      } else {
         this.menuItems = new String[]{"Chơi mới", "Chơi tiếp", "Trung tâm game QQ", "Hướng dẫn", "Giới thiệu", "Thoát game"};
      }
   }

   public final void startLoading() {
      page = 0;
      new TitleLoader(this);
   }

   private void releaseResources() {
      this.loadingAnim = null;
      this.startAnim = null;
      this.backgroundImage = null;
      this.menuLabelImage = null;
      this.menuArrowsImage = null;
   }

   public final void loadResources() {
      if (this.loadingAnim == null) {
         this.loadingAnim = new AnimSprite();
         this.loadingAnim.load("/av/long1.av");
      }

      this.loadTargetPercent = 0;
      this.loadTargetPercent = 20;
      InputStream var1 = Main.self.getClass().getResourceAsStream("/pic/start/start.av");
      if (this.startAnim == null) {
         this.startAnim = new AnimSprite();
         this.startAnim.loadInGroup("/pic/start/start.av", 1);
      }

      if (var1 == null) {
         this.startAnim = null;
      }

      var1 = Main.self.getClass().getResourceAsStream("/pic/start/logo.av");
      if (this.logoAnim == null) {
         this.logoAnim = new AnimSprite();
         this.logoAnim.loadInGroup("/pic/start/logo.av", 1);
      }

      if (var1 == null) {
         this.logoAnim = null;
      }

      this.backgroundImage = ResourceCache.loadImage("/pic/menuBack.jpg");
      String var10001 = "/pic/hk2.gf";
      this.menuLabelImage = null;
      this.menuArrowsImage = ResourceCache.loadImage("/pic/D04.gf");
      if (this.topBarImage == null) {
         this.topBarImage = ResourceCache.loadImage("/pic/topbj.gf");
      }

      if (this.softKeyIcons == null) {
         this.softKeyIcons = ResourceCache.loadImage("/pic/D23.gf");
      }

      if (this.digitsImage == null) {
         this.digitsImage = ResourceCache.loadImage("/pic/number01.gf", 2);
      }

      if (this.heroNamesImage == null) {
         this.heroNamesImage = ResourceCache.loadImage("/pic/D09.gf");
      }

      if (this.cursorImage == null) {
         this.cursorImage = ResourceCache.loadImage("/pic/arrow.gf", 2);
      }

      if (this.musicIcon == null) {
         this.musicIcon = ResourceCache.loadImage("/pic/musicIcon.gf");
      }

      if (this.levelLabelImage == null) {
         this.levelLabelImage = ResourceCache.loadImage("/pic/D08.gf");
      }

      if (this.volumeArrowsImage == null) {
         this.volumeArrowsImage = ResourceCache.loadImage("/pic/D11.gf");
      }

      this.loadTargetPercent = 70;
      if (DeviceConfig.modelName != null) {
         Equip.loadTable();
         if (Equip.icons == null) {
            Equip.loadIcons();
         }

         Skill.loadTable();
         Skill.loadIcons();
         if (GameData.itemTypes == null) {
            GameData.loadItemTable();
            GameData.loadItemIcons();
         }
      }

      String var4 = Utils.loadConfigText("/bin/about.bin");
      this.helpText = Utils.substringBetween(var4, "help:", "helpEnd");
      this.helpLines = Utils.splitAndWrap(this.helpText, "\r\n", 170);
      this.helpText = null;
      this.aboutLines = Utils.splitAndWrap(ChannelConfig.aboutText, "\r\n", 170);
      this.loadTargetPercent = 90;
      this.hasSave = false;
      String[] var5;
      PortalProtocol.dumpArray(var5 = RecordStore.listRecordStores(), "Lưu game");

      for (int var2 = 0; var5 != null && var2 < var5.length; var2++) {
         if (var5[var2].equals(GameData.saveSlot1Store)) {
            this.hasSave = true;
            this.saveSlots = Utils.append(this.saveSlots, 1);
         } else if (var5[var2].equals(GameData.saveSlot2Store)) {
            this.hasSave = true;
            this.saveSlots = Utils.append(this.saveSlots, 2);
         } else if (var5[var2].equals(GameData.saveSlot3Store)) {
            this.hasSave = true;
            this.saveSlots = Utils.append(this.saveSlots, 3);
         }
      }

      if (this.saveSlots != null) {
         this.saveSlots = GameUtil.sortBytes(this.saveSlots);

         for (int var6 = 0; var6 < this.saveSlots.length; var6++) {
            if (this.saveSlots[var6] == 1) {
               this.saveSlotNames = Utils.append(this.saveSlotNames, "Tiến độ 1");
            } else if (this.saveSlots[var6] == 2) {
               this.saveSlotNames = Utils.append(this.saveSlotNames, "Tiến độ 2");
            } else if (this.saveSlots[var6] == 3) {
               this.saveSlotNames = Utils.append(this.saveSlotNames, "Tiến độ 3");
            }
         }
      }

      this.shownMenuItems = this.menuItems;
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         GameUtil.freeImageCaches();
      }

      this.loadTargetPercent = 100;
   }

   public final void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 3 == 0) {
         if (this.blinkFrame == 0) {
            this.blinkFrame = 2;
         } else {
            this.blinkFrame = 0;
         }
      }

      Graphics var2 = var1;
      TitleScreen var8 = this;
      var1.setColor(0);
      var1.setClip(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
      var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
      var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      if (page == 0) {
         if (!DeviceConfig.modelName.equals("D608") && DeviceConfig.screenSize[0] >= 176) {
            if (this.loadingAnim != null) {
               this.loadingAnim.x = (short)(15 + (DeviceConfig.screenSize[0] - 30) * this.loadPercent / 100);
               this.loadingAnim.y = (short)(DeviceConfig.screenSize[1] - 15);
               this.loadingAnim.setAction(3);
               this.loadingAnim.draw(var1);
               if (GameCanvas.instance.frameCounter % 2 == 0) {
                  this.loadingAnim.nextFrame(true);
               }
            }

            var1.setColor(16765232);
            if (this.loadPercent < 20) {
               vh.drawString(var1, "Đang tải dữ liệu bản đồ...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 40) {
               vh.drawString(var1, "Đang tải dữ liệu tài nguyên...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 70) {
               vh.drawString(var1, "Đang tải dữ liệu nhân vật...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 100) {
               vh.drawString(var1, "Đang tải dữ liệu giao diện...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            }
         } else {
            var1.setColor(16765232);
            if (this.loadPercent < 20) {
               vh.drawString(var1, "Đang tải dữ liệu bản đồ...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 40) {
               vh.drawString(var1, "Đang tải dữ liệu tài nguyên...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 70) {
               vh.drawString(var1, "Đang tải dữ liệu nhân vật...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            } else if (this.loadPercent < 100) {
               vh.drawString(var1, "Đang tải dữ liệu giao diện...", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
            }

            var1.setColor(16711680);
            var1.fillRect(20, DeviceConfig.screenSize[1] - 10, DeviceConfig.screenSize[0] - 40, 1);
            var1.setColor(16249886);
            var1.fillRect(20, DeviceConfig.screenSize[1] - 10, (DeviceConfig.screenSize[0] - 40) * this.loadPercent / 100, 1);
         }

         if (this.loadPercent < this.loadTargetPercent) {
            this.loadPercent++;
         } else {
            this.loadPercent = this.loadTargetPercent;
         }

         if (this.isLoaded && this.loadPercent >= 100) {
            if (GameData.loadingTips != null) {
               GameData.tipIndex = (byte)GameUtil.randomRange(0, GameData.loadingTips.length);
            }

            this.loadTargetPercent = 0;
            this.loadPercent = 0;
            this.loadingAnim = null;
            if (!DeviceConfig.modelName.equals("N73")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("N5800")
               && !DeviceConfig.modelName.equals("E62")
               && !DeviceConfig.modelName.equals("W958C")) {
               GameUtil.freeImageCaches();
            }

            if (this.startAnim != null) {
               page = 6;
            } else {
               page = 1;
            }

            if (DeviceConfig.playMusic && GameSettings.titleMusic != null) {
               Main.self.playMusic(GameSettings.titleMusic, -1, GameData.musicVolume);
               return;
            }
         }
      } else if (page == 6) {
         if (this.startAnim != null) {
            GameUtil.drawAnimation(var1, this.startAnim, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, true, 0);
            if (this.startAnim.getFrame() >= this.startAnim.getActionFrameCount(0) - 1) {
               page = 1;
               return;
            }
         }
      } else {
         if (page == 1 || page == 8) {
            if (this.startAnim != null) {
               GameUtil.drawAnimation(var1, this.startAnim, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, true, 1);
            } else if (this.backgroundImage != null) {
               var1.drawImage(this.backgroundImage, DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1, 3);
            }

            if (this.logoAnim != null) {
               GameUtil.drawAnimation(var1, this.logoAnim, DeviceConfig.screenSize[0], 0, true, 1);
            }

            vh.getHeight(Utils.defaultFont);
            short var14;
            if (this.menuLabelImage == null) {
               var14 = (short)(DeviceConfig.screenSize[1] - 22);
               Utils.drawOutlinedText(
                  var1, this.shownMenuItems[this.menuIndex], DeviceConfig.screenSize[0] / 2, var14 + Utils.lineHeight - 4, 16250611, 5667444, 33
               );
               var14 = (short)(var14 + 7);
            } else {
               if (DeviceConfig.screenSize[1] > 320) {
                  var14 = (short)(DeviceConfig.screenSize[1] - 30);
               } else {
                  var14 = (short)(DeviceConfig.screenSize[1] - 25);
               }

               if (DeviceConfig.modelName.equals("Dopod_S1")) {
                  var14 = (short)(DeviceConfig.screenSize[1] + 5);
               }

               if (this.menuIndex == 3 || this.menuIndex == 4 || this.menuIndex == 5 || this.menuIndex == 6) {
                  Utils.drawOutlinedText(var1, this.shownMenuItems[this.menuIndex], DeviceConfig.screenSize[0] / 2, var14 - 10, 16250611, 5667444, 17);
               } else if (this.menuIndex < 3) {
                  Utils.drawFrame(var1, this.menuLabelImage, this.menuLabelImage.getWidth(), 20, this.menuIndex, DeviceConfig.screenSize[0] / 2, var14, 3, 0);
               } else {
                  Utils.drawFrame(
                     var1, this.menuLabelImage, this.menuLabelImage.getWidth(), 20, this.menuIndex - 4, DeviceConfig.screenSize[0] / 2, var14, 3, 0
                  );
               }
            }

            if (this.menuArrowsImage != null && page != 8) {
               Utils.drawFrame(
                  var1,
                  this.menuArrowsImage,
                  this.menuArrowsImage.getWidth() / 2,
                  this.menuArrowsImage.getHeight(),
                  0,
                  (DeviceConfig.screenSize[0] - 90) / 2 - 20 - this.cursorBob,
                  var14,
                  3,
                  0
               );
               Utils.drawFrame(
                  var1,
                  this.menuArrowsImage,
                  this.menuArrowsImage.getWidth() / 2,
                  this.menuArrowsImage.getHeight(),
                  1,
                  (DeviceConfig.screenSize[0] + 90) / 2 + 20 + this.cursorBob,
                  var14,
                  3,
                  0
               );
            }

            if (page == 8) {
               TouchSoftKeys.setSoftKeys(3);
               String var23 = "Vào trung tâm QQ Game sẽ đóng trò chơi hiện tại, chắc chắn vào?";
               Utils.wrapText("Vào trung tâm QQ Game sẽ đóng trò chơi hiện tại, chắc chắn vào?", DeviceConfig.screenSize[0] - 30);
               GameUtil.drawMessageBox(var1, var23, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               if (DeviceConfig.modelName.equals("E6")
                  || DeviceConfig.modelName.equals("E680")
                  || DeviceConfig.modelName.equals("W958C")
                  || DeviceConfig.modelName.equals("S1")
                  || DeviceConfig.modelName.equals("N95")
                  || DeviceConfig.modelName.equals("N5800")) {
                  TouchSoftKeys.draw(var1);
               }
            }

            return;
         }

         if (page == 7) {
            this.drawPanelBackground(var1);
            int var27 = (DeviceConfig.screenSize[0] - 176) / 2;
            int var31 = (DeviceConfig.screenSize[1] - 208) / 2;
            String var22 = "Lưu ý: tải miễn phí (không gồm phí mạng), trong game sẽ thu một phần phí chức năng, mức phí cụ thể sẽ được hỏi xác nhận trong game!";
            this.recommendedRows = 188 / Utils.lineHeight;
            int var12 = this.recommendedRows;
            if (this.recommendedGames != null) {
               if (this.recommendedGames.length <= this.recommendedRows) {
                  var12 = this.recommendedGames.length;
               } else {
                  GameUtil.drawScrollBar(
                     var1,
                     null,
                     null,
                     var27 + 176 - 5,
                     var31 + 10,
                     (Utils.lineHeight + 1) * this.recommendedRows,
                     this.recommendedGames.length,
                     this.recommendedRows,
                     this.recommendedScroll
                  );
               }
            }

            var31 += 10;
            if (this.recommendedGames != null) {
               for (int var34 = 0; var34 < var12; var34++) {
                  if (var34 + var8.recommendedScroll == var8.recommendedIndex) {
                     Utils.drawOutlinedText(
                        var2,
                        var8.recommendedGames[var34 + var8.recommendedScroll],
                        DeviceConfig.screenSize[0] / 2,
                        var31 + (Utils.lineHeight + 1) * var34,
                        16711680,
                        16777215,
                        17
                     );
                  } else {
                     var2.setColor(16777215);
                     vh.drawString(
                        var2, var8.recommendedGames[var34 + var8.recommendedScroll], DeviceConfig.screenSize[0] / 2, var31 + (Utils.lineHeight + 1) * var34, 17
                     );
                  }
               }
            }

            var31 += var12 * Utils.lineHeight;
            if (var8.noticeBox == null) {
               var8.noticeBox = new TextBlock();
               var8.noticeBox.addText(var22, 166, null, 13695736);
               var8.noticeBox
                  .setBounds(var27 + 4, var31 + 15, 166, 208 - var12 * Utils.lineHeight - 25, (208 - var12 * Utils.lineHeight - 25) / Utils.lineHeight);
            }

            var8.noticeBox.draw(var2, var8.noticeBox, 1, true, false);
            return;
         }

         if (page == 2) {
            this.drawPanelBackground(var1);
            int var26 = DeviceConfig.screenSize[0] - 176;
            int var30 = DeviceConfig.screenSize[1] - 208;
            TouchSoftKeys.setSoftKeys(2);
            var1.setColor(16777215);
            int var11 = vh.getHeight(Utils.defaultFont);

            for (int var21 = 0; var8.helpLines != null && var21 < var8.helpLines.length; var21++) {
               vh.drawString(var2, var8.helpLines[var21], var26 / 2 + 3, var30 / 2 + 5 + var11 * var21 + var8.helpScrollY, 20);
            }

            return;
         }

         if (page == 4) {
            this.drawPanelBackground(var1);
            int var25 = DeviceConfig.screenSize[0] - 176;
            int var29 = DeviceConfig.screenSize[1] - 208;
            TouchSoftKeys.setSoftKeys(2);
            var1.setColor(16777215);
            int var10 = vh.getHeight(Utils.defaultFont);

            for (int var20 = 0; var8.aboutLines != null && var20 < var8.aboutLines.length; var20++) {
               vh.drawString(var2, var8.aboutLines[var20], var25 / 2 + 3, var29 / 2 + 5 + var10 * var20, 20);
            }

            return;
         }

         if (page == 3) {
            this.drawPanelBackground(var1);
            TouchSoftKeys.setSoftKeys(2);
            var1.setColor(12961221);
            int var9 = (DeviceConfig.screenSize[0] - 90 - 27) / 2;
            int var19 = DeviceConfig.screenSize[1] / 2;
            if (this.musicIcon != null) {
               var1.drawImage(this.musicIcon, 50, var19 - 20, 3);
            }

            int var24 = GameData.musicVolume / 10;
            if (this.volumeAnim != null) {
               this.volumeAnim.setPosition(DeviceConfig.screenSize[0] >> 1, (DeviceConfig.screenSize[1] >> 1) - 20);
               this.volumeAnim.draw(var1);
               if (GameCanvas.instance.frameCounter % 3 == 0) {
                  this.volumeAnim.nextFrame(true);
               }

               var19 += 20;

               for (int var6 = 0; var6 < 10; var6++) {
                  if (var6 < var24) {
                     var2.setColor(16776412);
                     var2.fillRect(var9 + var6 * 12, var19, 9, 5);
                     var2.setColor(16773198);
                     var2.fillRect(var9 + 1 + var6 * 12, var19 + 1, 8, 4);
                  } else {
                     var2.setColor(15132390);
                     var2.fillRect(var9 + var6 * 12, var19, 9, 5);
                     var2.setColor(12961221);
                     var2.fillRect(var9 + 1 + var6 * 12, var19 + 1, 8, 4);
                  }
               }
            } else {
               for (int var28 = 0; var28 < 10; var28++) {
                  if (var28 < var24) {
                     var2.setColor(16776412);
                     var2.fillRect(var9 + var28 * 12, var19 - (var28 << 2), 9, 5 + (var28 << 2));
                     var2.setColor(16773198);
                     var2.fillRect(var9 + 1 + var28 * 12, var19 + 1 - (var28 << 2), 8, 4 + (var28 << 2));
                  } else {
                     var2.setColor(15132390);
                     var2.fillRect(var9 + var28 * 12, var19 - (var28 << 2), 9, 5 + (var28 << 2));
                     var2.setColor(12961221);
                     var2.fillRect(var9 + 1 + var28 * 12, var19 + 1 - (var28 << 2), 8, 4 + (var28 << 2));
                  }
               }
            }

            if (DeviceConfig.playMusic && var24 != 0) {
               Utils.drawNumber(var2, GameData.musicVolume, DeviceConfig.screenSize[0] / 2, var19 + 5 + 4 + Utils.lineHeight / 2, this.digitsImage, 6, 7, 3);
            } else {
               var2.setColor(16777215);
               vh.drawString(var2, "Tắt", DeviceConfig.screenSize[0] / 2, var19 + 5 + 4, 17);
            }

            if (this.volumeArrowsImage != null) {
               Utils.drawFrame(
                  var2,
                  this.volumeArrowsImage,
                  this.volumeArrowsImage.getWidth() / 2,
                  this.volumeArrowsImage.getHeight(),
                  1,
                  DeviceConfig.screenSize[0] / 2 - this.cursorBob + vh.stringWidth(Utils.defaultFont, "Thuộc tính"),
                  var19 + 5 + 4 + Utils.lineHeight / 2,
                  3,
                  0
               );
               Utils.drawFrame(
                  var2,
                  this.volumeArrowsImage,
                  this.volumeArrowsImage.getWidth() / 2,
                  this.volumeArrowsImage.getHeight(),
                  0,
                  DeviceConfig.screenSize[0] / 2 + this.cursorBob - vh.stringWidth(Utils.defaultFont, "Thuộc tính"),
                  var19 + 5 + 4 + Utils.lineHeight / 2,
                  3,
                  0
               );
            }

            return;
         }

         if (page == 5) {
            this.drawPanelBackground(var1);
            int var3 = DeviceConfig.screenSize[0] - 176 >> 1;
            TouchSoftKeys.setSoftKeys(3);
            if (this.saveSlotNames != null) {
               for (int var7 = 0; var7 < DeviceConfig.rmsCount; var7++) {
                  int var4 = DeviceConfig.screenSize[1] - 208 >> 1;
                  var2.setColor(16755968);
                  if (GameData.hasSaveIndex) {
                     var2.setColor(0);
                     if (GameData.slotUsed[var7] == 1) {
                        if (var8.slotHeroAnims != null && var8.slotHeroAnims[var7] != null) {
                           var8.slotHeroAnims[var7].x = (short)(var3 + 44);
                           var8.slotHeroAnims[var7].y = (short)(var4 + 60 + var7 * 104);
                           if (var7 == var8.slotIndex) {
                              var8.slotHeroAnims[var7].setAction(0);
                           } else {
                              var8.slotHeroAnims[var7].setAction(4);
                           }

                           var8.slotHeroAnims[var7].draw(var2);
                           var8.slotHeroAnims[var7].nextFrame(true);
                        }

                        var4 += 40;
                        Utils.drawOutlinedText(var2, GameData.slotSceneNames[var7], var3 + 176 - 10, var4 + 5 - 40 + var7 * 104, 3532779, 146244, 24);
                        var4 += 20;
                        if (var8.heroNamesImage != null) {
                           var2.drawRegion(
                              var8.heroNamesImage,
                              0,
                              var8.heroNamesImage.getHeight() / 2 * (GameData.slotHeroIds[var7] - 1),
                              var8.heroNamesImage.getWidth(),
                              var8.heroNamesImage.getHeight() / 2,
                              0,
                              var3 + 88,
                              var4 + var7 * 104,
                              3
                           );
                        } else {
                           String var5 = "";
                           if (GameData.slotHeroIds[var7] == 1) {
                              var5 = "Quinn";
                           } else if (GameData.slotHeroIds[var7] == 2) {
                              var5 = "Julie";
                           }

                           Utils.drawOutlinedText(var2, var5, var3 + 88, var4 - Utils.lineHeight / 2 + var7 * 104, 16776937, 257041742, 24);
                        }

                        Utils.drawNumber(var2, GameData.slotHeroLevels[var7], var3 + 113, var4 + var7 * 104, var8.digitsImage, 6, 7, 3);
                        var2.drawImage(var8.levelLabelImage, var3 + 132, var4 + var7 * 104, 3);
                        var4 += 20;
                        Utils.drawNumber(var2, GameData.slotYears[var7], var3 + 44, var4 + var7 * 104, var8.digitsImage, 6, 7, 20);
                        var2.drawRegion(var8.digitsImage, 60, 0, 6, 7, 0, var3 + 44 + 24 + 1, var4 + var7 * 104, 20);
                        var2.drawRegion(var8.digitsImage, 60, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1, var4 + var7 * 104, 17);
                        Utils.drawNumber(var2, GameData.slotMonths[var7], var3 + 44 + 24 + 6 + 2 + 12, var4 + var7 * 104, var8.digitsImage, 6, 7, 24);
                        Utils.drawNumber(
                           var2, GameData.slotDays[var7], var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12, var4 + var7 * 104, var8.digitsImage, 6, 7, 24
                        );
                        if (GameData.slotMonths[var7] < 10) {
                           var2.drawRegion(var8.digitsImage, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2, var4 + var7 * 104, 20);
                        }

                        if (GameData.slotDays[var7] < 10) {
                           var2.drawRegion(var8.digitsImage, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1, var4 + var7 * 104, 20);
                        }

                        Utils.drawNumber(
                           var2,
                           GameData.slotHours[var7],
                           var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12,
                           var4 + var7 * 104,
                           var8.digitsImage,
                           6,
                           7,
                           24
                        );
                        if (GameData.slotHours[var7] < 10) {
                           var2.drawRegion(var8.digitsImage, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10, var4 + var7 * 104, 20);
                        }

                        var2.drawRegion(var8.digitsImage, 78, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12, var4 + var7 * 104, 20);
                        Utils.drawNumber(
                           var2,
                           GameData.slotMinutes[var7],
                           var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12 + 6 + 12,
                           var4 + var7 * 104,
                           var8.digitsImage,
                           6,
                           7,
                           24
                        );
                        if (GameData.slotMinutes[var7] < 10) {
                           var2.drawRegion(
                              var8.digitsImage, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12 + 6, var4 + var7 * 104, 20
                           );
                        }
                     } else {
                        var2.setColor(16777215);
                        vh.drawString(
                           var2, "Tiến độ " + (var7 + 1) + ": Trống", DeviceConfig.screenSize[0] / 2, var4 + 52 + var7 * 104 - Utils.lineHeight / 2, 17
                        );
                     }
                  } else {
                     var2.setColor(16777215);
                     vh.drawString(var2, "Tiến độ " + (var7 + 1) + ": Trống", DeviceConfig.screenSize[0] / 2, var4 + 52 + var7 * 104 - Utils.lineHeight / 2, 17);
                  }
               }

               int var18 = DeviceConfig.screenSize[1] - 208 >> 1;
               if (var8.cursorImage != null) {
                  var2.drawRegion(var8.cursorImage, 0, 0, 15, 15, 0, var3 + 10 + var8.cursorBob, var18 + 52 + var8.slotIndex * 104, 3);
                  return;
               }
            } else {
               var1.setColor(16777215);
               vh.drawString(var1, "Chưa có bản lưu", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 - Utils.lineHeight / 2, 17);
            }
         }
      }
   }

   protected final void handleKey(int var1) {
      if (!this.isEnteringGame) {
         if (page == 6) {
            if (var1 == 8) {
               page = 1;
               return;
            }
         } else if (page == 1) {
            if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == 2) {
                  if (this.menuIndex > 0) {
                     this.menuIndex--;
                     return;
                  }

                  this.menuIndex = (byte)(this.shownMenuItems.length - 1);
                  return;
               }

               if (var1 == 5) {
                  if (this.menuIndex < this.shownMenuItems.length - 1) {
                     this.menuIndex++;
                     return;
                  }

                  this.menuIndex = 0;
                  return;
               }
            } else {
               if (this.menuItems[this.menuIndex].equals("Chơi mới")) {
                  GameData.storyPhase = 1;
                  if (Building.isGenuineCopy()) {
                     Main.self.newGame();
                     this.isEnteringGame = false;
                     this.releaseResources();
                     return;
                  }

                  GameCanvas.screenState = 6;
                  return;
               }

               if (this.menuItems[this.menuIndex].equals("Chơi tiếp")) {
                  if (!Building.isGenuineCopy()) {
                     GameCanvas.screenState = 6;
                     return;
                  }

                  page = 5;
                  String[] var11 = RecordStore.listRecordStores();

                  for (int var12 = 0; var11 != null && var12 < var11.length; var12++) {
                     if (var11[var12].equals(GameData.saveIndexStore)) {
                        GameData.hasSaveIndex = true;
                        GameData.loadSlotSummaries();
                     }
                  }

                  if (GameData.hasSaveIndex) {
                     this.slotHeroAnims = new AnimSprite[GameData.slotHeroIds.length];

                     for (int var13 = 0; var13 < this.slotHeroAnims.length; var13++) {
                        this.slotHeroAnims[var13] = new AnimSprite();
                        this.slotHeroAnims[var13].load("/av/chr" + GameData.slotHeroIds[var13] + ".av");
                     }
                  }

                  return;
               }

               if (this.menuItems[this.menuIndex].equals("Cài đặt")) {
                  page = 3;
                  if (this.volumeAnim == null) {
                     this.volumeAnim = new AnimSprite();
                     this.volumeAnim.load("/pic/D41.av");
                     if (GameData.musicVolume > 0) {
                        this.volumeAnim.setAction(1);
                     } else {
                        this.volumeAnim.setAction(0);
                     }
                  }

                  if (Main.self.getClass().getResourceAsStream("/pic/D41.av") == null) {
                     this.volumeAnim = null;
                  }

                  return;
               }

               if (this.menuItems[this.menuIndex].equals("Hướng dẫn")) {
                  page = 2;
                  return;
               }

               if (this.menuItems[this.menuIndex].equals("Giới thiệu")) {
                  page = 4;
                  return;
               }

               if (this.menuItems[this.menuIndex].equals("Thoát game")) {
                  try {
                     Main.self.destroyApp(true);
                  } catch (MIDletStateChangeException var8) {
                     var8.printStackTrace();
                     return;
                  }
               } else {
                  if (this.menuItems[this.menuIndex].equals(ChannelConfig.addMenuText)) {
                     page = 8;
                     return;
                  }

                  if (this.menuItems[this.menuIndex].equals("Cộng đồng QQ")) {
                     QQCommunityOverlay.init(
                        Main.self,
                        GameCanvas.instance.display,
                        GameCanvas.instance,
                        GameCanvas.instance.screenWidth,
                        GameCanvas.instance.screenHeight,
                        false,
                        Utils.parseInt(ChannelConfig.gameId),
                        120
                     );
                     QQCommunityOverlay.openHomePage();
                     return;
                  }

                  if (this.menuItems[this.menuIndex].equals("Bảng xếp hạng")) {
                     QQCommunityOverlay.init(
                        Main.self,
                        GameCanvas.instance.display,
                        GameCanvas.instance,
                        GameCanvas.instance.screenWidth,
                        GameCanvas.instance.screenHeight,
                        false,
                        Utils.parseInt(ChannelConfig.gameId),
                        120
                     );
                     QQCommunityOverlay.showRanking();
                     return;
                  }

                  if (this.shownMenuItems[this.menuIndex].equals("Game hay đề cử")) {
                     if (this.recommendedGames == null) {
                        this.recommendedGames = null;

                        for (int var2 = 0; var2 < 5; var2++) {
                           String var10;
                           if ((var10 = Main.self.getAppProperty("Tj" + (var2 + 1) + "Url")) != null) {
                              int var3 = 0;

                              for (int var4 = 0; var2 < var10.length(); var4++) {
                                 if (var10.substring(var4, var4 + 1).equals("&")) {
                                    if (++var3 == 3) {
                                       var3 = var4;
                                       break;
                                    }
                                 }
                              }

                              if (var10.substring(var3 - 2, var3).equals("43")) {
                                 this.recommendedGames = Utils.append(this.recommendedGames, "Tải {Linh Thú Chi Vương}");
                              } else if (var10.substring(var3 - 2, var3).equals("48")) {
                                 this.recommendedGames = Utils.append(this.recommendedGames, "Tải {Hiên Viên Thương Khung Lục}");
                              } else if (var10.substring(var3 - 2, var3).equals("05")) {
                                 this.recommendedGames = Utils.append(this.recommendedGames, "Tải {Kiếm Phá Thương Khung}");
                              } else if (var10.substring(var3 - 2, var3).equals("03")) {
                                 this.recommendedGames = Utils.append(this.recommendedGames, "Tải {Thần Mã Giang Hồ}");
                              } else if (var10.substring(var3 - 2, var3).equals("31")) {
                                 this.recommendedGames = Utils.append(this.recommendedGames, "Tải {Sủng Vật Thiên Hạ 2}");
                              }

                              this.recommendedUrls = Utils.append(this.recommendedUrls, var10);
                           }
                        }
                     }

                     page = 7;
                     return;
                  }
               }
            }
         } else if (page == 2) {
            if (var1 == KeyMap.softRightKey) {
               page = 1;
               return;
            }

            if (var1 == 1) {
               if (this.helpScrollY < 0) {
                  this.helpScrollY = (short)(this.helpScrollY + Utils.lineHeight);
                  return;
               }
            } else if (var1 == 6 && Utils.lineHeight + 5 + Utils.lineHeight * this.helpLines.length + this.helpScrollY > GameCanvas.instance.getHeight() - 3) {
               this.helpScrollY = (short)(this.helpScrollY - Utils.lineHeight);
               return;
            }
         } else if (page == 4) {
            if (var1 == 8 || var1 == KeyMap.softRightKey || var1 == KeyMap.softLeftKey) {
               page = 1;
               return;
            }
         } else if (page == 3) {
            if (var1 == 8 || var1 == KeyMap.softRightKey || var1 == KeyMap.softLeftKey) {
               page = 1;
            }

            if (var1 == 5) {
               if (!DeviceConfig.playMusic) {
                  DeviceConfig.playMusic = true;
               }

               if (GameData.musicVolume < 100) {
                  GameData.musicVolume = (byte)(GameData.musicVolume + 10);
               }

               if (GameData.musicVolume >= 100) {
                  GameData.musicVolume = 100;
               }

               if (this.volumeAnim != null) {
                  this.volumeAnim.setAction(1);
               }

               if (Main.music != null) {
                  Main.music.setVolume(GameData.musicVolume);
               } else {
                  Main.self.playMusic(GameSettings.titleMusic, -1, GameData.musicVolume);
               }
            }

            if (var1 == 2) {
               if (GameData.musicVolume > 0) {
                  GameData.musicVolume = (byte)(GameData.musicVolume - 10);
               }

               if (GameData.musicVolume <= 0) {
                  if (this.volumeAnim != null) {
                     this.volumeAnim.setAction(0);
                  }

                  GameData.musicVolume = 0;
               }

               Main.music.setVolume(GameData.musicVolume);
               return;
            }
         } else if (page == 5) {
            if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == KeyMap.softRightKey) {
                  page = 1;
                  return;
               }

               if (var1 == 1) {
                  if (this.slotIndex > 0) {
                     this.slotIndex--;
                     return;
                  }
               } else if (var1 == 6 && this.saveSlotNames != null && this.slotIndex < this.saveSlotNames.length - 1) {
                  this.slotIndex++;
                  return;
               }
            } else if (this.hasSave) {
               Main.self.stopMusic();
               this.isEnteringGame = true;
               GameCanvas.screenState = 4;
               if (GameCanvas.instance.world == null) {
                  Runtime.getRuntime().freeMemory();
                  GameCanvas.instance.world = new World();
                  Runtime.getRuntime().freeMemory();
               }

               GameCanvas.instance.world.loadSaveSlot(this.saveSlotNames[this.slotIndex]);
               if (!DeviceConfig.modelName.equals("N73")
                  && !DeviceConfig.modelName.equals("N7610")
                  && !DeviceConfig.modelName.equals("N5800")
                  && !DeviceConfig.modelName.equals("E62")
                  && !DeviceConfig.modelName.equals("W958C")) {
                  this.releaseResources();
                  return;
               }
            }
         } else if (page == 7) {
            if (var1 == KeyMap.softRightKey) {
               this.recommendedScroll = 0;
               this.recommendedIndex = 0;
               page = 1;
               return;
            }

            if (var1 == KeyMap.softLeftKey || var1 == 8) {
               Main.self.stopThread();
               Main.self.a_GameCanvas.world = null;

               try {
                  Main.self.platformRequest(this.recommendedUrls[this.recommendedIndex]);
               } catch (Exception var6) {
                  var6.printStackTrace();
               }

               try {
                  Main.self.destroyApp(true);
                  return;
               } catch (MIDletStateChangeException var5) {
                  var5.printStackTrace();
                  return;
               }
            }

            if (var1 == 1) {
               if (this.recommendedScroll == this.recommendedIndex && this.recommendedScroll > 0) {
                  this.recommendedScroll--;
               }

               if (this.recommendedIndex > 0) {
                  this.recommendedIndex--;
                  return;
               }
            } else if (var1 == 6) {
               if (this.recommendedIndex < this.recommendedGames.length - 1) {
                  this.recommendedIndex++;
               }

               if (this.recommendedScroll + this.recommendedRows == this.recommendedIndex) {
                  this.recommendedScroll++;
                  return;
               }
            }
         } else if (page == 8) {
            if (var1 == KeyMap.softRightKey) {
               page = 1;
               return;
            }

            if (var1 == KeyMap.softLeftKey) {
               Main.self.stopThread();
               Main.self.a_GameCanvas.world = null;

               try {
                  Main.self.platformRequest(ChannelConfig.addMenuLink);
               } catch (Exception var7) {
                  var7.printStackTrace();
               }

               try {
                  Main.self.destroyApp(true);
                  return;
               } catch (MIDletStateChangeException var9) {
                  var9.printStackTrace();
               }
            }
         }
      }
   }

   public final void handlePointer(int var1, int var2) {
      int var3 = DeviceConfig.screenSize[0] - 176 >> 1;
      int var4 = DeviceConfig.screenSize[1] - 208 >> 1;
      if (page == 2 || page == 4 || page == 3 || page == 5 || page == 7) {
         if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            if (MenuList.isInButton(var1, var2, var3, var4 + 208 - 30, 30, 30)) {
               this.handleKey(KeyMap.softLeftKey);
            } else if (MenuList.isInButton(var1, var2, var3 + 176 - 30, var4 + 208 - 30, 30, 30)) {
               this.handleKey(KeyMap.softRightKey);
            }
         } else if (MenuList.isInButton(var1, var2, var3, var4 + 208 - 30, 30, 30)) {
            this.handleKey(KeyMap.softRightKey);
         } else if (MenuList.isInButton(var1, var2, var3 + 176 - 30, var4 + 208 - 30, 30, 30)) {
            this.handleKey(KeyMap.softLeftKey);
         }
      }

      TouchButton var7;
      if ((var7 = TouchSoftKeys.hitTest(var1, var2)) != null) {
         if (var7.buttonId == 2) {
            if (page == 8) {
               page = 1;
               return;
            }
         } else if (var7.buttonId == 1 && page == 8) {
            Main.self.stopThread();
            Main.self.a_GameCanvas.world = null;

            try {
               Main.self.platformRequest(ChannelConfig.addMenuLink);
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            try {
               Main.self.destroyApp(true);
               return;
            } catch (MIDletStateChangeException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   private void drawPanelBackground(Graphics var1) {
      if (this.startAnim != null) {
         GameUtil.drawAnimation(var1, this.startAnim, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, true, 1);
      } else if (this.backgroundImage != null) {
         var1.drawImage(this.backgroundImage, DeviceConfig.screenSize[0] >> 1, DeviceConfig.screenSize[1] >> 1, 3);
      }

      if (this.logoAnim != null) {
         GameUtil.drawAnimation(var1, this.logoAnim, DeviceConfig.screenSize[0], 0, true, 1);
      }

      if (this.menuLabelImage == null) {
         short var2 = (short)(DeviceConfig.screenSize[1] - 22);
         Utils.drawOutlinedText(var1, this.shownMenuItems[this.menuIndex], DeviceConfig.screenSize[0] / 2, var2 + Utils.lineHeight - 4, 16250611, 5667444, 17);
      } else {
         short var4;
         if (DeviceConfig.screenSize[1] > 320) {
            var4 = (short)(DeviceConfig.screenSize[1] - 30);
         } else {
            var4 = (short)(DeviceConfig.screenSize[1] - 25);
         }

         if (this.menuIndex == 3 || this.menuIndex == 4 || this.menuIndex == 5 || this.menuIndex == 6) {
            Utils.drawOutlinedText(var1, this.shownMenuItems[this.menuIndex], DeviceConfig.screenSize[0] / 2, var4 - 10, 16250611, 5667444, 17);
         } else if (this.menuIndex < 3) {
            Utils.drawFrame(var1, this.menuLabelImage, this.menuLabelImage.getWidth(), 20, this.menuIndex, DeviceConfig.screenSize[0] / 2, var4, 3, 0);
         } else {
            Utils.drawFrame(var1, this.menuLabelImage, this.menuLabelImage.getWidth(), 20, this.menuIndex - 4, DeviceConfig.screenSize[0] / 2, var4, 3, 0);
         }
      }

      int var5 = DeviceConfig.screenSize[0] - 176;
      int var3 = DeviceConfig.screenSize[1] - 208;
      var1.setColor(2237991);
      var1.fillRoundRect(var5 / 2 - 1, var3 / 2 - 1, 179, 211, 8, 8);
      var1.setColor(16053217);
      var1.drawRoundRect(var5 / 2, var3 / 2, 176, 208, 8, 8);
      var1.setColor(6768205);
      var1.fillRect(var5 / 2 + 2, var3 / 2 + 2, 173, 205);
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         Utils.drawFrame(var1, this.softKeyIcons, this.softKeyIcons.getWidth() / 2, this.softKeyIcons.getHeight(), 0, var5 / 2 + 4, var3 / 2 + 208 - 4, 36, 0);
         Utils.drawFrame(
            var1, this.softKeyIcons, this.softKeyIcons.getWidth() / 2, this.softKeyIcons.getHeight(), 1, var5 / 2 + 176 - 4, var3 / 2 + 208 - 4, 40, 0
         );
      } else {
         Utils.drawFrame(var1, this.softKeyIcons, this.softKeyIcons.getWidth() / 2, this.softKeyIcons.getHeight(), 1, var5 / 2 + 4, var3 / 2 + 208 - 4, 36, 0);
         Utils.drawFrame(
            var1, this.softKeyIcons, this.softKeyIcons.getWidth() / 2, this.softKeyIcons.getHeight(), 0, var5 / 2 + 176 - 4, var3 / 2 + 208 - 4, 40, 0
         );
      }
   }
}
