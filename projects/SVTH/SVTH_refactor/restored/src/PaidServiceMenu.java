import javax.microedition.lcdui.Graphics;

public final class PaidServiceMenu implements ResultListener {
   public static byte page;
   public static int[] itemFeatureIds;
   private static String[] a_AString = new String[]{"06", "13", "15", "10", "10", "01", "02", "16"};
   private static String[] b_AString = new String[]{"01", "02", "03", "04", "05", "06", "07", "08"};
   private static int[] b_AI = new int[]{2, 4, 1, 4, 4, 3, 1, 1};
   private static String[] confirmNotes;
   private static String[] itemNames;
   private static String[] successMessages;
   public static boolean rewardClaimed;
   byte itemIndex;
   private byte mainMenuIndex;
   private byte payMethodIndex;
   private SmsPaymentFlow smsFlow;
   private TextBlock textBox;
   private String[] mainMenuItems = new String[]{"Vô Song Bí Kỹ", "Tiệm Bí Bảo", "Siêu trang bị", "Nhận thưởng"};
   private SmsFeeConfig smsFeeConfig;
   private String c_String;
   private String d_String;
   private String[] g_AString;
   static boolean hasLuckyPack = false;
   static byte expMultiplier;
   public static boolean hasTransform = false;
   public static boolean isFullVersion = false;
   static int totalSpent = 0;
   private int pendingFeatureId = 0;
   public static String a_String = "";
   public static String b_String = "";

   public PaidServiceMenu() {
      page = 0;
      this.itemIndex = 0;
      this.mainMenuIndex = 0;
   }

   public static void open(String var0) {
      GameCanvas.instance.world.smsMenu = new PaidServiceMenu();
      GameCanvas.instance.world.mode = 7;
      String var1;
      String[][] var2 = Utils.getStringTable(var1 = Utils.loadConfigText(var0), "smsLib:", "smsLibEnd", null, "\t");
      confirmNotes = Utils.getStringBlock(var1, "sureNote:", "sureNoteEnd", "\t");
      if (itemNames == null) {
         for (int var3 = 0; var2 != null && var3 < var2.length; var3++) {
            itemFeatureIds = Utils.append(itemFeatureIds, Utils.parseInt(var2[var3][0]));
            itemNames = Utils.append(itemNames, var2[var3][1]);
            successMessages = Utils.append(successMessages, var2[var3][2]);
         }
      }
   }

   public final void draw(Graphics var1) {
      TouchSoftKeys.setSoftKeys(3);
      var1.setColor(4077616);
      var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
      var1.setColor(15919841);
      var1.fillRect(0, 0, DeviceConfig.screenSize[0], 30);
      var1.setColor(0);
      var1.setStrokeStyle(0);
      var1.drawLine(0, 29, DeviceConfig.screenSize[0], 29);
      var1.setColor(0);
      vh.drawString(var1, "Dịch vụ ưu đãi", DeviceConfig.screenSize[0] >> 1, 5, 17);
      var1.setColor(15919841);
      var1.fillRect(0, DeviceConfig.screenSize[1] - 30, DeviceConfig.screenSize[0], 30);
      var1.setColor(0);
      var1.setStrokeStyle(0);
      var1.drawLine(0, DeviceConfig.screenSize[1] - 30, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1] - 30);
      var1.setColor(0);
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         vh.drawString(var1, "OK", 3, DeviceConfig.screenSize[1] - 2, 36);
         vh.drawString(var1, "Trở về", DeviceConfig.screenSize[0] - 3, DeviceConfig.screenSize[1] - 2, 40);
      } else {
         vh.drawString(var1, "Trở về", 3, DeviceConfig.screenSize[1] - 2, 36);
         vh.drawString(var1, "OK", DeviceConfig.screenSize[0] - 3, DeviceConfig.screenSize[1] - 2, 40);
      }

      if (this.textBox != null) {
         this.textBox.clear();
      } else {
         this.textBox = new TextBlock();
      }

      if (page == 8) {
         this.textBox.addText("Chi tiêu đủ 12 tệ‚có thể nhận phần thưởng sau:", DeviceConfig.screenSize[0] - 20, null, 16777215);
         this.textBox.addText("Vàng 88888 + toàn đội x2 năng lực khi chiến đấu (trừ HP, MP)", DeviceConfig.screenSize[0] - 20, null, 65280);
         if (totalSpent >= 12) {
            this.textBox.addText("Đã tiêu " + totalSpent + " tệ (có thể nhận)", DeviceConfig.screenSize[0] - 20, null, 16777215);
         } else {
            this.textBox.addText("Đã tiêu " + totalSpent + " tệ (chi tiêu chưa đủ 12 tệ)", DeviceConfig.screenSize[0] - 20, null, 16777215);
         }

         this.textBox
            .setBounds(10, 30, DeviceConfig.screenSize[0] - 10, DeviceConfig.screenSize[1] - 10 - 20, (DeviceConfig.screenSize[1] - 10 - 20) / Utils.lineHeight);
         this.textBox.draw(var1, this.textBox, 1, true, false);
      } else if (page == 0) {
         for (int var5 = 0; var5 < this.mainMenuItems.length; var5++) {
            if (var5 == this.mainMenuIndex) {
               Utils.drawOutlinedText(
                  var1,
                  this.mainMenuItems[this.mainMenuIndex],
                  DeviceConfig.screenSize[0] / 2,
                  DeviceConfig.screenSize[1] / 2 - 30 - 2 * Utils.lineHeight + var5 * (Utils.lineHeight + 1),
                  16773120,
                  15867651,
                  17
               );
            } else {
               var1.setColor(16777215);
               vh.drawString(
                  var1,
                  this.mainMenuItems[var5],
                  DeviceConfig.screenSize[0] / 2,
                  DeviceConfig.screenSize[1] / 2 - 30 - 2 * Utils.lineHeight + var5 * (Utils.lineHeight + 1),
                  17
               );
            }
         }
      } else {
         if (page == 1 || page == 5) {
            for (int var2 = 0; var2 < itemNames.length; var2++) {
               if (var2 == this.itemIndex) {
                  Utils.drawOutlinedText8(
                     var1,
                     itemNames[var2],
                     DeviceConfig.screenSize[0] / 2,
                     DeviceConfig.screenSize[1] / 2 - 30 - 2 * Utils.lineHeight + var2 * (Utils.lineHeight + 1),
                     16773120,
                     15867651,
                     17
                  );
               } else {
                  var1.setColor(16777215);
                  vh.drawString(
                     var1,
                     itemNames[var2],
                     DeviceConfig.screenSize[0] / 2,
                     DeviceConfig.screenSize[1] / 2 - 30 - 2 * Utils.lineHeight + var2 * (Utils.lineHeight + 1),
                     17
                  );
               }
            }

            if (page == 5) {
               String[] var4 = new String[]{"Thanh toán qua ĐT", "Trả bằng Xu Tiêu Dao"};

               for (int var3 = 0; var3 < var4.length; var3++) {
                  if (var3 == this.payMethodIndex) {
                     Utils.drawOutlinedText(
                        var1,
                        var4[var3],
                        DeviceConfig.screenSize[0] / 2,
                        DeviceConfig.screenSize[1] / 2 - Utils.lineHeight - 4 + var3 * (Utils.lineHeight + 3),
                        16773120,
                        15867651,
                        17
                     );
                  } else {
                     var1.setColor(16777215);
                     vh.drawString(
                        var1,
                        var4[var3],
                        DeviceConfig.screenSize[0] / 2,
                        DeviceConfig.screenSize[1] / 2 - Utils.lineHeight - 4 + var3 * (Utils.lineHeight + 3),
                        17
                     );
                  }
               }

               return;
            }
         } else if (page != 6) {
            if (page == 2) {
               this.textBox.addText(confirmNotes[itemFeatureIds[this.itemIndex] - 1], DeviceConfig.screenSize[0] - 20, null, 16777215);
               this.textBox.addText(ChannelConfig.smsMessage, DeviceConfig.screenSize[0] - 20, null, 65280);
               this.textBox.addText("Đã gửi thành công 0 tin‚cần gửi thêm 1 tin. Gửi không?", DeviceConfig.screenSize[0] - 20, null, 16777215);
               this.textBox
                  .setBounds(
                     10, 30, DeviceConfig.screenSize[0] - 10, DeviceConfig.screenSize[1] - 10 - 20, (DeviceConfig.screenSize[1] - 10 - 20) / Utils.lineHeight
                  );
               this.textBox.draw(var1, this.textBox, 1, true, false);
               return;
            }

            if (page == 7) {
               this.smsFlow.draw(var1);
               return;
            }

            if (page == 3) {
               this.textBox.addText(successMessages[this.pendingFeatureId - 1], DeviceConfig.screenSize[0] - 20, null, 16777215);
               this.textBox.addText("Mua thành công‚nhớ lưu game kịp thời!", DeviceConfig.screenSize[0] - 20, null, 16777215);
               this.textBox
                  .setBounds(
                     10, 30, DeviceConfig.screenSize[0] - 10, DeviceConfig.screenSize[1] - 10 - 20, (DeviceConfig.screenSize[1] - 10 - 20) / Utils.lineHeight
                  );
               this.textBox.draw(var1, this.textBox, 1, true, false);
            }
         }
      }
   }

   protected final void handleKey(int var1) {
      GameUtil.resetScroll();
      if (page == 1) {
         if (var1 != 8 && var1 != KeyMap.softLeftKey) {
            if (var1 == KeyMap.softRightKey) {
               this.mainMenuIndex = 0;
               page = 0;
               return;
            }

            if (var1 == 1) {
               if (this.itemIndex > 0) {
                  this.itemIndex--;
                  return;
               }
            } else if (var1 == 6 && this.itemIndex < itemNames.length - 1) {
               this.itemIndex++;
               return;
            }
         } else {
            boolean var2 = false;
            if (itemFeatureIds[this.itemIndex] == 2) {
               if (expMultiplier >= 4) {
                  var2 = true;
               }
            } else if (itemFeatureIds[this.itemIndex] == 4) {
               if (hasTransform) {
                  var2 = true;
               }
            } else if (itemFeatureIds[this.itemIndex] == 5 && hasLuckyPack) {
               var2 = true;
            }

            if (var2) {
               GameCanvas.instance.showToast("Đã mua chức năng", 8);
               return;
            }

            if (DeviceConfig.useMoonicNetPay) {
               this.payMethodIndex = 0;
               page = 5;
               return;
            }

            this.purchase(itemFeatureIds[this.itemIndex]);
         }
      } else if (page != 6) {
         if (page == 5) {
            if (var1 != 8 && var1 != KeyMap.softLeftKey) {
               if (var1 == KeyMap.softRightKey) {
                  page = 1;
                  return;
               }

               if (var1 == 1) {
                  if (this.payMethodIndex > 0) {
                     this.payMethodIndex--;
                     return;
                  }
               } else if (var1 == 6 && this.payMethodIndex <= 0) {
                  this.payMethodIndex++;
                  return;
               }
            } else if (this.payMethodIndex == 0) {
               this.purchase(itemFeatureIds[this.itemIndex]);
               return;
            }
         } else if (page == 2) {
            if (var1 == KeyMap.softLeftKey) {
               this.purchase(itemFeatureIds[this.itemIndex]);
               return;
            }

            if (var1 == KeyMap.softRightKey) {
               page = 1;
               return;
            }
         } else {
            if (page == 7) {
               this.smsFlow.handleKey(var1);
               return;
            }

            if (page == 3) {
               if (var1 == KeyMap.softLeftKey || var1 == KeyMap.softRightKey) {
                  page = 1;
                  return;
               }
            } else if (page == 0) {
               if (var1 != 8 && var1 != KeyMap.softLeftKey) {
                  if (var1 == KeyMap.softRightKey) {
                     close();
                     return;
                  }

                  if (var1 == 6) {
                     if (this.mainMenuIndex < this.mainMenuItems.length - 1) {
                        this.mainMenuIndex++;
                        return;
                     }
                  } else if (var1 == 1 && this.mainMenuIndex > 0) {
                     this.mainMenuIndex--;
                     return;
                  }
               } else {
                  if (this.mainMenuIndex == 0) {
                     this.itemIndex = 0;
                     page = 1;
                     return;
                  }

                  if (this.mainMenuIndex == 3) {
                     if (!rewardClaimed) {
                        page = 8;
                        return;
                     }

                     GameCanvas.instance.showToast("Đã nhận", 8);
                     return;
                  }

                  if (this.mainMenuIndex == 1) {
                     close();
                     GameCanvas.instance.world.openShop("买道具", 8, 1);
                     return;
                  }

                  if (this.mainMenuIndex == 2) {
                     close();
                     GameCanvas.instance.world.openShop("买装备", 8, 1);
                     return;
                  }
               }
            } else if (page == 8) {
               if (var1 == KeyMap.softLeftKey) {
                  if (totalSpent >= 12) {
                     GameData.money += 88888;
                     GameData.statMultiplier = 2;
                     rewardClaimed = true;
                     page = 0;
                     GameCanvas.instance.showToast("Nhận thành công", 8);
                     return;
                  }

                  GameCanvas.instance.showToast("Chi tiêu chưa đạt 12 tệ", 8);
                  return;
               }

               if (var1 == KeyMap.softRightKey) {
                  page = 0;
               }
            }
         }
      }
   }

   public final void purchase(int var1) {
      this.pendingFeatureId = var1;
      this.onResult(0, null);
   }

   private void applyFeature() {
      int var1 = this.pendingFeatureId;
      if (this.pendingFeatureId == 1) {
         GameData.money += 8888;
         page = 3;
      } else if (var1 == 5) {
         hasLuckyPack = true;
         page = 3;
      } else if (var1 == 2) {
         expMultiplier = 4;
         page = 3;
      } else if (var1 == 3) {
         Utils.getStringTable(Utils.loadConfigText("/bin/team.bin"), "upgrade:", "upgradeEnd", null, "\t");
         RoleSprite var5 = GameData.party[0];

         for (int var2 = 0; var2 < 5; var2++) {
            var5.stats[0]++;
            var5.stats[15] = var5.stats[2];
            int var3 = var5.stats[0] + 1;
            var5.stats[2] = var3 * var3 * var3 / 2 + var3 * var3;
            var5.stats[1] = 0;
            var5.stats[16] = var5.stats[16] + 10;
            GameData.refreshRoleStats(var5);
            var5.stats[3] = var5.stats[10];
            var5.stats[5] = var5.stats[11];

            for (int var4 = 0; GameData.myPets != null && var4 < GameData.myPets.length; var4++) {
               if (GameData.myPets[var4].ownerHeroId == GameData.heroId) {
                  GameData.growPetStats(GameData.myPets[var4]);
                  GameData.myPets[var4].stats[0]++;
                  GameData.myPets[var4].stats[15] = GameData.myPets[var4].stats[14];
                  var3 = GameData.myPets[var4].stats[0] + 1;
                  GameData.myPets[var4].stats[14] = var3 * var3 * var3 / 2 + var3 * var3;
                  GameData.myPets[var4].stats[13] = 0;
                  GameData.myPets[var4].stats[1] = GameData.myPets[var4].stats[2];
                  GameData.myPets[var4].stats[4] = GameData.myPets[var4].stats[5];
               }
            }
         }

         page = 3;
      } else if (var1 == 4) {
         hasTransform = true;
         page = 3;
      } else if (var1 == 6) {
         isFullVersion = true;
         close();
         GameCanvas.instance.world.script.advance(0);
      } else if (var1 == 8) {
         if (GameCanvas.instance.world.battle != null) {
            close();
            GameCanvas.instance.world.battle.killAllEnemies();
            GameCanvas.instance.world.battle.buildVictoryRewards();
         }
      } else if (var1 == 7 && GameCanvas.instance.world.battle != null) {
         close();

         for (int var6 = 0; var6 < GameCanvas.instance.world.battle.actors.length / 2; var6++) {
            if (GameCanvas.instance.world.battle.actors[var6] != null) {
               GameCanvas.instance.world.battle.actors[var6].state = 0;
               GameCanvas.instance.world.battle.actors[var6].sprite.setAction(0);
               GameCanvas.instance.world.battle.actors[var6].setPos(GameCanvas.instance.world.battle.actors[var6].homePos);
               GameCanvas.instance.world.battle.actors[var6].isDead = false;
               GameCanvas.instance.world.battle.actors[var6].stats[3] = GameCanvas.instance.world.battle.actors[var6].stats[10];
               GameCanvas.instance.world.battle.actors[var6].hp = GameCanvas.instance.world.battle.actors[var6].stats[10];
               GameCanvas.instance.world.battle.actors[var6].mp = GameCanvas.instance.world.battle.actors[var6].stats[11];
               GameCanvas.instance.world.battle.actors[var6].stats[5] = GameCanvas.instance.world.battle.actors[var6].stats[11];
               GameCanvas.instance.world.battle.actors[var6].sprite.isVisible = true;
            }
         }

         GameCanvas.instance.world.battle.endRound();
         GameCanvas.instance.world.battle.phase = 0;
      }

      GameData.savePurchaseState(GameData.purchaseStore);
   }

   private static void close() {
      GameCanvas.instance.world.mode = 0;
      itemNames = null;
      itemFeatureIds = null;
      confirmNotes = null;
      GameCanvas.instance.world.smsMenu = null;
      if (GameCanvas.instance.world.battle == null) {
         if (DeviceConfig.unloadBeforeBattle) {
            GameCanvas.instance.world.resetSceneNpcs();
            GameCanvas.instance.world.beginLoading();
         }

         GameCanvas.instance.world.mode = 1;
      } else {
         GameCanvas.instance.world.mode = 5;
      }
   }

   public final void handlePointer(int var1, int var2) {
      TouchButton var3;
      if (GameCanvas.instance.world.mode == 7 && (var3 = TouchSoftKeys.hitTest(var1, var2)) != null) {
         if (var3.buttonId == 2) {
            this.handleKey(KeyMap.softRightKey);
            return;
         }

         if (var3.buttonId == 1) {
            this.handleKey(KeyMap.softLeftKey);
         }
      }
   }

   public final void onResult(int var1, String var2) {
      if (var1 == 0) {
         if (this.pendingFeatureId == 6) {
            totalSpent += 4;
         } else {
            totalSpent += 2;
         }

         GameData.money += 1000;
         this.applyFeature();
      } else {
         System.out.println("Mua thất bại::");
         if (GameCanvas.instance.world.battle != null) {
            close();
            if (GameCanvas.instance.world.battle.smsContext == 1) {
               GameCanvas.instance.world.battle.onDefeat();
               return;
            }

            if (GameCanvas.instance.world.battle.smsContext == 2) {
               GameCanvas.instance.world.battle.killAllEnemies();
               GameCanvas.instance.world.battle.buildVictoryRewards();
               GameCanvas.instance.world.battle.phase = 24;
               return;
            }

            if (GameCanvas.instance.world.battle.smsContext == 3) {
               GameCanvas.instance.world.battle.endRound();
               return;
            }
         } else {
            if (this.pendingFeatureId == 6) {
               close();
               GameCanvas.instance.world.messageLines = null;
               GameCanvas.instance.world.messageScroll = -1;
               GameCanvas.instance.world.script.advance(0);
               return;
            }

            page = 1;
         }
      }
   }
}
