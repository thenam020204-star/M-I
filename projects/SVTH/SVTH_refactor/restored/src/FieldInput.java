import java.util.Vector;

public final class FieldInput {
   static RoleSprite talkTarget;

   public FieldInput() {
   }

   public static void handleKey(int var0) {
      if (GameCanvas.instance.world.worldState == 2 && GameCanvas.instance.world.fieldHudEnabled) {
         if (GameCanvas.instance.world.mode == 1) {
            if (GameCanvas.instance.world.script.isRunning && !GameCanvas.instance.world.storyCrawlActive) {
               return;
            }

            if (var0 == 8) {
               if (GameCanvas.instance.world.storyCrawlActive) {
                  if (GameCanvas.instance.world.storyCrawl.style == 1) {
                     GameCanvas.instance.world.crawlSpeed++;
                     return;
                  }

                  if (GameCanvas.instance.world.crawlSpeed >= 4) {
                     GameCanvas.instance.world.crawlSpeed -= 2;
                     return;
                  }

                  GameCanvas.instance.world.crawlSpeed = 2;
                  return;
               }

               if (GameCanvas.instance.world.script.hasManualEvent) {
                  GameCanvas.instance.world.script.startEvent(GameCanvas.instance.world.script.manualEvent);
                  return;
               }

               if (GameCanvas.instance.world.nearChest) {
                  TreasureChest var10 = GameCanvas.instance.world.nearbyChest;
                  boolean var15 = false;
                  if (var10.chestKind == 0) {
                     var15 = true;
                  } else if (var10.chestKind == 1) {
                     for (int var17 = 0; GameData.bagItems != null && var17 < GameData.bagItems.length; var17++) {
                        if (GameData.bagItems[var17][0] == 19 && GameData.bagItems[var17][1] > 0) {
                           GameData.removeItem(19, 1);
                           var15 = true;
                           break;
                        }
                     }
                  }

                  Vector var18 = new Vector();
                  if (var15) {
                     byte var19 = 1;
                     var10.openFrame = (byte)var19;
                     if (!Utils.contains(GameData.openedChestIds, var10.chestId)) {
                        GameData.openedChestIds = Utils.append(GameData.openedChestIds, var10.chestId);
                     } else {
                        var10.alreadyOpened = 1;
                     }

                     String[] var16 = null;
                     String[] var20 = null;
                     if (var10.alreadyOpened == 1) {
                        var18.addElement("Rương đã mở");
                     } else {
                        if (var10.gold > 0) {
                           var18.addElement("Nhận vàng " + var10.gold);
                           GameData.money = GameData.money + var10.gold;
                           var10.gold = 0;
                        }

                        if (var10.items != null) {
                           if (GameData.itemTable == null) {
                              GameData.itemTable = Utils.getStringTable(Utils.loadConfigText("/bin/item.bin"), "item", "itemend", null, "\t");
                           }

                           for (int var21 = 0; var21 < GameData.itemTable.length; var21++) {
                              var16 = Utils.append(var16, GameData.itemTable[var21][1]);
                           }
                        }

                        if (var10.equips != null) {
                           if (GameData.equipTable == null) {
                              GameData.equipTable = Utils.getStringTable(Utils.loadConfigText("/bin/equip.bin"), "equip", "equipend", null, "\t");
                           }

                           for (int var22 = 0; var22 < GameData.equipTable.length; var22++) {
                              var20 = Utils.append(var20, GameData.equipTable[var22][1]);
                           }
                        }

                        for (int var23 = 0; var10.items != null && var23 < var10.items.length; var23++) {
                           var18.addElement("Nhận " + var16[var10.items[var23][0] - 1]);
                           GameData.addItem(var10.items[var23][0], var10.items[var23][1]);
                        }

                        var10.items = null;

                        for (int var24 = 0; var10.equips != null && var24 < var10.equips.length; var24++) {
                           var18.addElement("Nhận " + var20[var10.equips[var24][0] - 1]);
                           GameData.addEquip(var10.equips[var24][0], -1);
                        }

                        var10.equips = null;
                     }

                     if (var18.size() <= 0) {
                        var18.addElement("Rương trống rỗng");
                     }
                  } else {
                     var18.addElement("Không có Chìa vạn năng");
                  }

                  GameCanvas.instance.world.messageLines = new String[var18.size()];
                  var18.copyInto(GameCanvas.instance.world.messageLines);
                  GameCanvas.instance.world.messageScroll = 0;
                  GameCanvas.instance.world.mode = 3;
                  return;
               }

               if (GameCanvas.instance.world.nearNpc) {
                  if (GameData.party[0].isFlying) {
                     GameCanvas.instance.showToast("Đang bay không thể nói chuyện", 10);
                     return;
                  }

                  talkTarget = GameCanvas.instance.world.nearbyNpc;
                  boolean var8 = false;

                  for (int var12 = 0; GameData.party != null && var12 < GameData.party.length; var12++) {
                     if (talkTarget == GameData.party[var12]) {
                        var8 = true;
                     }
                  }

                  if (var8) {
                     return;
                  }

                  if (talkTarget.roleId > 0) {
                     String var13;
                     if ((var13 = Utils.loadConfigText("/bin/s" + GameData.currentSceneId + "c.bin")) == null) {
                        return;
                     }

                     String var2;
                     if ((var2 = Utils.substringBetween(var13, "role" + talkTarget.roleId + ":", "role" + talkTarget.roleId + "End")) == null) {
                        return;
                     }

                     byte var14 = Utils.getByte(var2, "amount");
                     String[][] var9 = null;

                     for (int var5 = 0; var5 < var14; var5++) {
                        String var4 = Utils.substringBetween(var2, "group" + (var5 + 1) + ":", "group" + (var5 + 1) + "End");
                        byte var3 = Utils.getByte(var2, "phase");
                        if (var4 != null) {
                           if (var3 == 0 || var3 == GameData.storyPhase) {
                              var9 = Utils.getStringTable(var4, "script:", "scriptEnd", null);
                           }
                           break;
                        }
                     }

                     if (var9 == null) {
                        return;
                     }

                     ScriptEvent var25;
                     (var25 = new ScriptEvent()).scriptLines = var9;
                     var25.speaker = talkTarget;
                     GameCanvas.instance.world.script.startEvent(var25);
                     talkTarget = null;
                  }

                  return;
               }
            } else {
               if (var0 == KeyMap.softLeftKey) {
                  if (!GameCanvas.instance.world.storyCrawlActive) {
                     openMainMenu();
                     GameMenu.menuPage = 1;
                     GameCanvas.instance.world.menu.initPages();
                     return;
                  }

                  if (GameCanvas.instance.world.storyCrawl.style == 1) {
                     GameCanvas.instance.world.crawlSpeed++;
                     return;
                  }

                  if (GameCanvas.instance.world.crawlSpeed >= 4) {
                     GameCanvas.instance.world.crawlSpeed -= 2;
                     return;
                  }

                  GameCanvas.instance.world.crawlSpeed = 2;
                  return;
               }

               if (var0 == KeyMap.softRightKey) {
                  if (GameCanvas.instance.world.storyCrawlActive) {
                     GameCanvas.instance.world.storyCrawlActive = false;
                     GameCanvas.instance.world.script.advance(5);
                     GameCanvas.instance.world.storyCrawl.lines = null;
                     return;
                  }

                  PaidServiceMenu.open("/bin/smsmenu.bin");
                  if (DeviceConfig.unloadBeforeBattle) {
                     GameCanvas.instance.world.releaseResources();
                     return;
                  }
               } else {
                  if (var0 == 57) {
                     startFlyTransform();
                     return;
                  }

                  if (var0 == 35 && DeviceConfig.debugMode) {
                     GameData.party[0].noClip = !GameData.party[0].noClip;
                     return;
                  }
               }
            }
         } else if (GameCanvas.instance.world.mode == 3) {
            if (GameCanvas.instance.world.textLayout != null && GameCanvas.instance.world.textLayout.lines != null) {
               if (var0 == 8) {
                  if (GameCanvas.instance.world.messageScroll + GameCanvas.instance.world.messageVisibleLines
                     < GameCanvas.instance.world.textLayout.lines.length) {
                     GameCanvas.instance.world.messageScroll++;
                     return;
                  }

                  GameCanvas.instance.world.mode = 1;
                  GameCanvas.instance.world.messageLines = null;
                  GameCanvas.instance.world.messageScroll = 0;
                  GameCanvas.instance.world.script.advance(0);
                  return;
               }

               if (var0 == 6) {
                  if (GameCanvas.instance.world.messageScroll + GameCanvas.instance.world.messageVisibleLines
                     < GameCanvas.instance.world.textLayout.lines.length) {
                     GameCanvas.instance.world.messageScroll++;
                     return;
                  }
               } else if (var0 == 1 && GameCanvas.instance.world.messageScroll > 0) {
                  GameCanvas.instance.world.messageScroll--;
                  return;
               }
            }
         } else if (GameCanvas.instance.world.mode == 10) {
            if (var0 == KeyMap.softLeftKey || var0 == 8) {
               PaidServiceMenu.open("/bin/smsmenu.bin");
               GameCanvas.instance.world.smsMenu.purchase(6);
               return;
            }
         } else if (GameCanvas.instance.world.mode == 2) {
            if (var0 == 8) {
               if (GameCanvas.instance.world.dialogBox.boxMode == 0) {
                  if (GameCanvas.instance.world.dialogBox != null) {
                     if (GameCanvas.instance.world.dialogBox.isTextComplete()) {
                        if (GameCanvas.instance.world.dialogBox.lineIndex >= GameCanvas.instance.world.dialogBox.lines.length - 1) {
                           if (GameCanvas.instance.world.dialogBox.speaker != null) {
                              boolean var7 = false;
                              if (GameCanvas.instance.world.dialogBox.speaker.renderMode == 1
                                 && GameCanvas.instance.world.dialogBox.speaker.animSprite.getFrameCount() != 8
                                 && GameCanvas.instance.world.dialogBox.speaker.animSprite.getFrameCount() != 4) {
                                 var7 = true;
                              }

                              if (!var7) {
                                 GameCanvas.instance.world.dialogBox.speaker.setDirection(GameCanvas.instance.world.dialogBox.speaker.savedDirection);
                                 GameCanvas.instance.world.dialogBox.speaker.setIdleAnim();
                              }

                              GameCanvas.instance.world.dialogBox.speaker.resumeWander();
                           }

                           GameCanvas.instance.world.closeDialog();
                           GameCanvas.instance.world.mode = 1;
                           return;
                        }

                        GameCanvas.instance.world.dialogBox.nextLine();
                        return;
                     }

                     GameCanvas.instance.world.dialogBox.scrollDown();
                     return;
                  }
               } else if (GameCanvas.instance.world.dialogBox.boxMode == DialogBox.modeChoice) {
                  GameCanvas.instance.world.chosenOption = GameCanvas.instance.world.dialogBox.choices[GameCanvas.instance.world.dialogBox.selectedChoice];
                  GameCanvas.instance.world.dialogBox.selectedChoice = 0;
                  GameCanvas.instance.world.dialogBox.choiceScroll = 0;
                  GameCanvas.instance.world.closeDialog();
                  GameCanvas.instance.world.mode = 1;
                  return;
               }
            } else if (var0 == 6) {
               if (GameCanvas.instance.world.dialogBox.boxMode == 0) {
                  if (GameCanvas.instance.world.dialogBox != null) {
                     GameCanvas.instance.world.dialogBox.scrollDown();
                     return;
                  }
               } else if (GameCanvas.instance.world.dialogBox.boxMode == DialogBox.modeChoice) {
                  if (GameCanvas.instance.world.dialogBox.selectedChoice < GameCanvas.instance.world.dialogBox.choices.length - 1) {
                     GameCanvas.instance.world.dialogBox.selectedChoice++;
                  }

                  if (GameCanvas.instance.world.dialogBox.selectedChoice - GameCanvas.instance.world.dialogBox.choiceScroll == DialogBox.choiceRows) {
                     GameCanvas.instance.world.dialogBox.choiceScroll++;
                     return;
                  }
               }
            } else if (var0 == 1) {
               if (GameCanvas.instance.world.dialogBox.boxMode == 0) {
                  if (GameCanvas.instance.world.dialogBox != null) {
                     GameCanvas.instance.world.dialogBox.scrollUp();
                     return;
                  }
               } else if (GameCanvas.instance.world.dialogBox.boxMode == DialogBox.modeChoice) {
                  if (GameCanvas.instance.world.dialogBox.choiceScroll > 0
                     && GameCanvas.instance.world.dialogBox.choiceScroll == GameCanvas.instance.world.dialogBox.selectedChoice) {
                     GameCanvas.instance.world.dialogBox.choiceScroll--;
                  }

                  if (GameCanvas.instance.world.dialogBox.selectedChoice > 0) {
                     GameCanvas.instance.world.dialogBox.selectedChoice--;
                     return;
                  }
               }
            }
         } else if (GameCanvas.instance.world.mode == 8) {
            if (var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey) {
               if (var0 == KeyMap.softLeftKey) {
                  GameData.score += 5;
                  if (GameCanvas.instance.world.dungeonLoot[0] == 1) {
                     GameData.addItem(GameCanvas.instance.world.dungeonLoot[1], GameCanvas.instance.world.dungeonLoot[2]);
                  } else if (GameCanvas.instance.world.dungeonLoot[0] == 2) {
                     GameData.addEquip(GameCanvas.instance.world.dungeonLoot[1], -1);
                  } else if (GameCanvas.instance.world.dungeonLoot[0] == 3) {
                     GameData.money = GameData.money + GameCanvas.instance.world.dungeonLoot[1];
                  } else if (GameCanvas.instance.world.dungeonLoot[0] == 4) {
                     GameData.addPet(GameCanvas.instance.world.dungeonLoot[1], -1);
                  }

                  Object var6 = null;
                  int var1 = 0;

                  while (
                     GameData.dungeonRewards != null
                        && var1 < GameData.dungeonRewards.length
                        && (var26 = Utils.parseIntArray(GameData.dungeonRewards[var1], ","))[0] != 5
                  ) {
                     var1++;
                  }

                  if (var26 != null) {
                     GameData.party[0].stats[1] = GameData.party[0].stats[1] + var26[1];

                     for (int var11 = 0; GameData.myPets != null && var11 < GameData.myPets.length; var11++) {
                        if (GameData.myPets[var11].isDeployed) {
                           GameData.myPets[var11].stats[13] = GameData.myPets[var11].stats[13] + var26[1];
                        }
                     }
                  }

                  GameCanvas.instance.world.checkLevelUp(GameData.party[0]);
                  if (GameCanvas.instance.world.levelUpEffect) {
                     GameCanvas.instance.world.lootNotices = Utils.append(GameCanvas.instance.world.lootNotices, GameData.party[0].name + " đã lên cấp");
                  }

                  World.levelUpPets(GameData.myPets);
                  GameCanvas.instance.world.showTicker(GameCanvas.instance.world.lootNotices);
                  GameCanvas.instance.world.lootNotices = null;
               }

               GameCanvas.instance.world.lootPetName = null;
               GameCanvas.instance.world.mode = 1;
               GameCanvas.instance.world.script.advance(0);
               GameUtil.addDungeonPortal(GameData.dungeonExitPos[0], GameData.dungeonExitPos[1], Utils.parseShort(GameData.dungeonLinkScenes[0]));
               return;
            }
         } else if (GameCanvas.instance.world.mode == 9) {
            if (GameCanvas.instance.world.questDialogPage == 1) {
               GameUtil.resetScroll();
               if (var0 == KeyMap.softLeftKey) {
                  GameCanvas.instance.world.questDialogPage = 2;
                  return;
               }

               if (var0 != KeyMap.softRightKey) {
                  return;
               }
            } else {
               if (var0 != KeyMap.softLeftKey && var0 != KeyMap.softRightKey) {
                  return;
               }

               if (var0 == KeyMap.softLeftKey) {
                  GameData.acceptQuest(GameCanvas.instance.world.shownQuestId);
               }
            }

            GameCanvas.instance.world.lootPetName = null;
            GameCanvas.instance.world.script.advance(3);
            GameCanvas.instance.world.mode = 1;
         }
      }
   }

   public static void openMainMenu() {
      if (GameCanvas.instance.world.battle == null) {
         if (DeviceConfig.moreSleep) {
            try {
               Thread.sleep(200L);
            } catch (InterruptedException var1) {
               var1.printStackTrace();
            }
         }

         GameMenu.hero = GameData.party[0];
         GameMenu.pets = null;
         GameMenu.pets = GameUtil.a_APetInfo();
         GameCanvas.instance.world.menu = new GameMenu();
         GameCanvas.instance.world.mode = 4;
         if (DeviceConfig.unloadBeforeBattle) {
            GameCanvas.instance.world.releaseResources();
         }
      }
   }

   public static void startFlyTransform() {
      if (PaidServiceMenu.hasTransform) {
         if (GameData.rideFlags == 0) {
            GameCanvas.instance.showToast("Hiện không thể dùng biến thân", 10);
         } else if (GameData.rideFlags == 1) {
            GameCanvas.instance.showToast("Hiện không thể dùng biến thân", 10);
         } else {
            GameCanvas.instance.world.fieldHudEnabled = false;
            GameCanvas.instance.world.isTransforming = true;
            GameData.party[0].isVisible = false;
            GameCanvas.instance.world.transformKind = 0;
         }
      } else {
         GameCanvas.instance.world.messageLines = new String[]{"Chưa mở biến thân bay!"};
         GameCanvas.instance.world.messageScroll = 0;
         GameCanvas.instance.world.mode = 3;
      }
   }

   public static void applyTransform(int var0) {
      if (var0 == 0) {
         if (GameData.party[0].isFlying) {
            GameData.party[0].land();
         } else {
            GameData.party[0].isRiding = false;
            GameData.party[0].startFlying();
         }
      } else if (var0 == 1) {
         if (!GameData.party[0].isRiding) {
            GameData.party[0].moveSpeed = 5;
            GameData.party[0].animSprite.loadInGroup(GameData.mountAnimPath, 1);
            if (GameData.party[0].direction == 0) {
               GameData.party[0].animSprite.setAction(0);
               GameData.party[0].animSprite.setFrame(0);
            }

            GameData.party[0].isRiding = true;
            if (GameData.party[0].isFlying) {
               GameCanvas.instance.world.sceneLayer.removeSkyActor(GameData.party[0]);
               GameData.party[0].isFlying = false;
               GameData.party[0].noClip = false;
            }
         } else {
            GameData.party[0].moveSpeed = 4;
            GameData.party[0].animSprite.loadInGroup(GameData.roleAnimPaths[GameData.party[0].roleId - 1], 1);

            for (int var1 = 0; var1 < GameData.party.length; var1++) {
               if (var1 != 0) {
                  GameData.party[var1].setPosition(GameData.party[0].x, GameData.party[0].y);
                  GameData.party[var1].isVisible = true;
               }
            }

            GameData.party[0].isRiding = false;
         }
      } else if (var0 == 2) {
         GameData.party[0].moveSpeed = 2;
         GameData.party[0].animSprite.loadInGroup(GameData.boatAnimPath, 1);
         if (GameData.party[0].direction == 0) {
            GameData.party[0].animSprite.setAction(0);
            GameData.party[0].animSprite.setFrame(0);
         }

         GameData.party[0].isRiding = true;
      }

      GameData.party[0].isVisible = true;
      GameCanvas.instance.world.fieldHudEnabled = true;
   }
}
