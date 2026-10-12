import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;

public class MenuList {
   public Vector columns;
   public int[] rowIds;
   int x;
   int y;
   int w;
   int h;
   int cursorX;
   Image cursorImage;
   int cursorBob;

   public MenuList() {
   }

   public static void handleMenuKey(int var0) {
      label707: {
         if (GameCanvas.instance.world.worldState == 2) {
            if (GameCanvas.instance.world.menu == null) {
               GameCanvas.instance.world.mode = 1;
               return;
            }

            GameUtil.resetScroll();
            if (GameMenu.menuPage == 1) {
               if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                  if (var0 == KeyMap.softRightKey) {
                     closeMenu();
                     return;
                  }

                  if (var0 == 2) {
                     GameCanvas.instance.world.menu.tabBar.selectPrev();
                     byte var16 = (byte)((byte)GameCanvas.instance.world.menu.tabBar.getSelectedIndex() + 2);
                     GameCanvas.instance.world.menu.showPreview(var16);
                     return;
                  }

                  if (var0 == 5) {
                     GameCanvas.instance.world.menu.tabBar.selectNext();
                     byte var15 = (byte)((byte)GameCanvas.instance.world.menu.tabBar.getSelectedIndex() + 2);
                     GameCanvas.instance.world.menu.showPreview(var15);
                     return;
                  }
               } else if ((var5 = (byte)((byte)GameCanvas.instance.world.menu.tabBar.getSelectedIndex() + 2)) != 2 && var5 != 4) {
                  if (var5 == 3) {
                     if (GameMenu.pets != null) {
                        break label707;
                     }

                     GameCanvas.instance.world.menu.showToast("Không có sủng vật", 10);
                     GameMenu.menuPage = 1;
                  } else {
                     if (var5 != 7 || GameData.activeQuests != null) {
                        break label707;
                     }

                     GameCanvas.instance.world.menu.showToast("Không có nhiệm vụ", 10);
                     GameMenu.menuPage = 1;
                  }
               } else {
                  if (var5 != 4 || GameMenu.hero.skills != null) {
                     if (GameMenu.hero.facePath.endsWith(".av")) {
                        GameMenu.previewAnim = new AnimSprite();
                        int var13 = GameMenu.hero.facePath.indexOf(".");
                        String var14 = GameMenu.hero.facePath.substring(0, var13) + "s.av";
                        GameMenu.previewAnim.load(var14);
                     }
                     break label707;
                  }

                  GameCanvas.instance.showToast("Chưa học kỹ năng nào", 8);
                  GameMenu.menuPage = 1;
               }
            } else if (GameMenu.menuPage == 2) {
               if (var0 == KeyMap.softRightKey) {
                  if (GameCanvas.instance.world.menu.pendingStatPoints != null) {
                     for (int var11 = 0; var11 < GameCanvas.instance.world.menu.pendingStatPoints.length; var11++) {
                        GameMenu.hero.stats[16] = GameMenu.hero.stats[16] + GameCanvas.instance.world.menu.pendingStatPoints[var11];
                     }

                     int[] var12 = new int[]{0, 0, 0, 0};
                     int[] var4 = new int[]{0, 0, 0, 0, 0};
                     GameCanvas.instance.world.menu.pendingStatPoints = var12;
                     GameCanvas.instance.world.menu.pendingStatBonus = var4;
                  }

                  if (GameCanvas.instance.world.menu.isStatsFromScript) {
                     GameCanvas.instance.world.menu.isStatsFromScript = false;
                     closeMenu();
                     GameCanvas.instance.world.script.advance(5);
                     return;
                  }

                  GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
                  GameCanvas.instance.world.menu.resetListCursors();
                  return;
               }

               if (var0 == 6) {
                  GameCanvas.instance.world.menu.statRowIndex++;
                  GameCanvas.instance.world.menu.statRowIndex %= 4;
                  return;
               }

               if (var0 == 1) {
                  GameCanvas.instance.world.menu.statRowIndex--;
                  GameCanvas.instance.world.menu.statRowIndex = (GameCanvas.instance.world.menu.statRowIndex + 4) % 4;
                  return;
               }

               if (var0 == 2) {
                  if (GameCanvas.instance.world.menu.pendingStatPoints[GameCanvas.instance.world.menu.statRowIndex] > 0) {
                     GameCanvas.instance.world.menu.pendingStatPoints[GameCanvas.instance.world.menu.statRowIndex]--;
                     GameMenu.hero.stats[16]++;

                     for (int var10 = 0; var10 < 5; var10++) {
                        GameCanvas.instance.world.menu.pendingStatBonus[var10] = GameCanvas.instance.world.menu.pendingStatBonus[var10]
                           - GameMenu.hero.attrGrowth[GameCanvas.instance.world.menu.statRowIndex][var10];
                     }

                     return;
                  }
               } else if (var0 == 5) {
                  if (GameMenu.hero.stats[16] > 0) {
                     GameCanvas.instance.world.menu.pendingStatPoints[GameCanvas.instance.world.menu.statRowIndex]++;
                     GameMenu.hero.stats[16]--;

                     for (int var9 = 0; var9 < 5; var9++) {
                        GameCanvas.instance.world.menu.pendingStatBonus[var9] = GameCanvas.instance.world.menu.pendingStatBonus[var9]
                           + GameMenu.hero.attrGrowth[GameCanvas.instance.world.menu.statRowIndex][var9];
                     }

                     return;
                  }
               } else if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                  for (int var7 = 0; var7 < 4; var7++) {
                     GameMenu.hero.stats[var7 + 17] = GameMenu.hero.stats[var7 + 17] + GameCanvas.instance.world.menu.pendingStatPoints[var7];

                     for (int var2 = 0; var2 < 5; var2++) {
                        if (var2 < 2) {
                           GameMenu.hero.stats[4 + (var2 << 1)] = GameMenu.hero.stats[4 + (var2 << 1)]
                              + GameCanvas.instance.world.menu.pendingStatPoints[var7] * GameMenu.hero.attrGrowth[var7][var2];
                        } else {
                           GameMenu.hero.stats[var2 + 5] = GameMenu.hero.stats[var2 + 5]
                              + GameCanvas.instance.world.menu.pendingStatPoints[var7] * GameMenu.hero.attrGrowth[var7][var2];
                        }

                        GameMenu.hero.stats[var2 + 10] = GameMenu.hero.stats[var2 + 10]
                           + GameCanvas.instance.world.menu.pendingStatPoints[var7] * GameMenu.hero.attrGrowth[var7][var2];
                     }
                  }

                  int[] var8 = new int[]{0, 0, 0, 0};
                  int[] var3 = new int[]{0, 0, 0, 0, 0};
                  GameCanvas.instance.world.menu.pendingStatPoints = var8;
                  GameCanvas.instance.world.menu.pendingStatBonus = var3;
                  return;
               }
            } else {
               if (GameMenu.menuPage == 3) {
                  handlePetPageKey(var0);
                  return;
               }

               if (GameMenu.menuPage == 14) {
                  switch (GameMenu.skillShopPage) {
                     case 0:
                        if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                           if (var0 == KeyMap.softRightKey) {
                              closeMenu();
                              GameCanvas.instance.world.script.advance(5);
                              return;
                           }

                           if (var0 == 1) {
                              GameMenu.skillShopList.selectPrev();
                              return;
                           }

                           if (var0 == 6) {
                              GameMenu.skillShopList.selectNext();
                           }
                           break;
                        }

                        if (canLearnerLearnSkill()) {
                           if (GameMenu.skillLearnerType == 4 && GameMenu.pets == null) {
                              GameCanvas.instance.world.menu.showToast("Không có sủng vật", 10);
                           } else {
                              GameMenu.skillShopPage = 1;
                              GameMenu.learnerScroll = 0;
                              GameCanvas.instance.world.menu.buildLearnerList();
                              GameCanvas.instance.world.menu.setPanelHeights(0, GameCanvas.instance.world.menu.bodyH - 2);
                           }

                           return;
                        } else {
                           GameCanvas.instance.world.menu.showToast("Nghề này không thể học kỹ năng này", 12);
                           return;
                        }
                     case 1:
                        if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                           if (var0 == KeyMap.softRightKey) {
                              GameMenu.skillShopPage = 0;
                              GameMenu.learnerIndex = 0;
                              GameCanvas.instance.world.menu.setPanelHeights(160, GameCanvas.instance.world.menu.bodyH - 160);
                              return;
                           }

                           if (var0 == 1 && GameMenu.skillLearnerType == 4) {
                              if (GameMenu.learnerScroll > 0 && GameMenu.learnerScroll == GameMenu.learnerIndex) {
                                 GameMenu.learnerScroll--;
                              }

                              if (GameMenu.learnerIndex > 0) {
                                 GameMenu.learnerIndex--;
                                 return;
                              }
                           } else if (var0 == 6 && GameMenu.skillLearnerType == 4) {
                              int var6 = 1;
                              if (GameMenu.skillLearnerType == 4) {
                                 var6 = GameMenu.pets.length;
                              }

                              if (GameMenu.learnerIndex < var6 - 1) {
                                 GameMenu.learnerIndex++;
                              }

                              if (GameMenu.learnerScroll + GameMenu.learnerRows == GameMenu.learnerIndex) {
                                 GameMenu.learnerScroll++;
                              }
                           }
                           break;
                        }

                        if (hasLearnedSkill(GameMenu.skillLearnerType, GameMenu.learnerIndex, GameMenu.skillShopList.getSelectedRow())) {
                           GameCanvas.instance.world.menu.showToast("Đã học kỹ năng này", 10);
                           return;
                        } else if (!meetsSkillLevel(GameMenu.skillLearnerType, GameMenu.learnerIndex)) {
                           if (GameMenu.skillLearnerType != 4) {
                              GameCanvas.instance.world.menu.showToast(" không đủ cấp yêu cầu", 10);
                           } else {
                              GameCanvas.instance.world.menu.showToast("Cấp sủng vật không đạt yêu cầu hoặc số kỹ năng đã tối đa", 10);
                           }

                           return;
                        } else {
                           if (GameData.money
                              < Skill.learnPrices[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))]) {
                              GameCanvas.instance.world.menu.showToast("Không đủ vàng‚không thể học kỹ năng", 10);
                           } else {
                              GameMenu.skillShopPage = 2;
                           }

                           return;
                        }
                     case 2:
                        if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                           if (GameMenu.skillLearnerType != 4) {
                              GameData.learnRoleSkill(
                                 GameData.party[0],
                                 Skill.ids[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))],
                                 1
                              );
                           } else {
                              GameData.learnPetSkill(
                                 GameMenu.pets[GameMenu.learnerIndex],
                                 Skill.ids[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))],
                                 1
                              );
                           }

                           GameData.money = GameData.money
                              - Skill.learnPrices[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))];
                           GameCanvas.instance.world.menu.showToast("Học thành công", 10);
                           GameMenu.learnerIndex = 0;
                           GameMenu.skillShopPage = 1;
                           GameMenu.learnerScroll = 0;
                           break;
                        } else {
                           if (var0 == KeyMap.softRightKey) {
                              GameMenu.learnerIndex = 0;
                              GameMenu.learnerScroll = 0;
                              GameMenu.skillShopPage = 1;
                           }

                           return;
                        }
                     default:
                        return;
                  }
               } else {
                  if (GameMenu.menuPage == 9) {
                     handleSystemPageKey(var0);
                     return;
                  }

                  if (GameMenu.menuPage == 5) {
                     handleEquipPageKey(var0);
                     return;
                  }

                  if (GameMenu.menuPage == 6) {
                     if (GameMenu.bagPage == 0) {
                        if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                           GameMenu.bagItemIndex = 0;
                           GameMenu.bagScrollRow = 0;
                           GameMenu.bagPage = 1;
                        } else if (var0 == KeyMap.softRightKey) {
                           GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
                           GameCanvas.instance.world.menu.resetListCursors();
                        } else if (var0 == 2) {
                           if (--GameMenu.bagTabIndex >= 0) {
                              return;
                           }

                           GameMenu.bagTabIndex = 2;
                        } else {
                           if (var0 != 5 || ++GameMenu.bagTabIndex <= 2) {
                              return;
                           }

                           GameMenu.bagTabIndex = 0;
                        }
                     } else if (GameMenu.bagPage == 1) {
                        if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                           if (var0 == KeyMap.softRightKey) {
                              GameMenu.bagItemIndex = 0;
                              GameMenu.bagScrollRow = 0;
                              GameCanvas.instance.world.menu.showPage((byte)6, (byte)0);
                              GameCanvas.instance.world.menu.resetListCursors();
                           } else if (var0 == 5) {
                              if (GameMenu.bagItemIndex < GameSettings.maxItemTypes - 1) {
                                 GameMenu.bagItemIndex++;
                              }

                              if (GameMenu.bagItemIndex - GameMenu.bagScrollRow * GameMenu.bagCols < GameMenu.bagCols * GameMenu.bagRows) {
                                 return;
                              }

                              GameMenu.bagScrollRow++;
                           } else if (var0 == 2) {
                              if (GameMenu.bagScrollRow > 0 && GameMenu.bagItemIndex == GameMenu.bagScrollRow * GameMenu.bagCols) {
                                 GameMenu.bagScrollRow--;
                              }

                              if (GameMenu.bagItemIndex <= 0) {
                                 return;
                              }

                              GameMenu.bagItemIndex--;
                           } else if (var0 == 6) {
                              if (GameMenu.bagItemIndex + GameMenu.bagCols <= GameSettings.maxItemTypes - 1) {
                                 GameMenu.bagItemIndex = (byte)(GameMenu.bagItemIndex + GameMenu.bagCols);
                              } else if (GameMenu.bagItemIndex < GameSettings.maxItemTypes - 1) {
                                 GameMenu.bagItemIndex++;
                              }

                              if (GameMenu.bagItemIndex - GameMenu.bagScrollRow * GameMenu.bagCols < GameMenu.bagCols * GameMenu.bagRows) {
                                 return;
                              }

                              GameMenu.bagScrollRow++;
                           } else {
                              if (var0 != 1) {
                                 return;
                              }

                              if (GameMenu.bagItemIndex - GameMenu.bagCols >= 0) {
                                 GameMenu.bagItemIndex = (byte)(GameMenu.bagItemIndex - GameMenu.bagCols);
                              } else if (GameMenu.bagItemIndex > 0) {
                                 GameMenu.bagItemIndex--;
                              }

                              if (GameMenu.bagScrollRow <= 0 || GameMenu.bagItemIndex >= GameMenu.bagScrollRow * GameMenu.bagCols) {
                                 return;
                              }

                              GameMenu.bagScrollRow--;
                           }
                        } else {
                           if (GameMenu.bagItems == null
                              || GameMenu.bagItems[GameMenu.bagTabIndex] == null
                              || GameMenu.bagItemIndex >= GameMenu.bagItems[GameMenu.bagTabIndex].length
                              || GameData.itemTypes[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])] == 3) {
                              return;
                           }

                           GameMenu.bagActionIndex = 0;
                           GameMenu.bagActionNames = new String[]{"Dùng", "Bán", "Bỏ"};
                           GameMenu.bagActionIds = new int[]{13, 14, 12};
                           if (GameMenu.bagTabIndex == 1) {
                              GameMenu.bagActionNames = new String[]{"Bán", "Bỏ"};
                              GameMenu.bagActionIds = new int[]{14, 12};
                           }

                           GameMenu.bagPage = 2;
                        }
                     } else if (GameMenu.bagPage == 2) {
                        if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                           if (GameMenu.bagActionNames[GameMenu.bagActionIndex].equals("Dùng")) {
                              if (GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0]) == 5) {
                                 GameCanvas.instance.world.menu.showToast("Vật phẩm này chỉ dùng được trong trận", 10);
                                 System.out.println("Cola vạn năng");
                              } else if (GameData.itemTypes[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])] == 3) {
                                 GameCanvas.instance.world.menu.showToast("Không thể dùng vật phẩm này", 10);
                              } else if (GameData.itemTypes[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])] == 4) {
                                 if (GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0] == 14
                                    && GameMenu.pets != null
                                    && GameMenu.pets.length >= GameSettings.maxPetCount) {
                                    GameCanvas.instance.world.menu.showToast("Túi sủng vật đã đầy", 8);
                                 } else {
                                    GameData.useItemOnRole(GameData.party[0], GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0]);
                                    GameMenu.bagItems = null;
                                    if (GameCanvas.instance.world.menu != null) {
                                       GameMenu.buildBagItems();
                                       GameMenu.bagPage = 1;
                                       GameMenu.menuPage = 6;
                                    }
                                 }
                              } else {
                                 GameCanvas.instance.world.menu.showPage((byte)13, (byte)0);
                                 GameMenu.useTargetIndex = 0;
                                 GameMenu.useTargetScroll = 0;
                              }
                           } else if (GameMenu.bagActionNames[GameMenu.bagActionIndex].equals("Bán")) {
                              GameMenu.bagQuantity = 1;
                              GameMenu.bagPage = 3;
                           } else if (GameMenu.bagActionNames[GameMenu.bagActionIndex].equals("Bỏ")) {
                              GameMenu.bagQuantity = GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][1];
                              GameMenu.bagPage = 4;
                           } else {
                              if (!GameMenu.bagActionNames[GameMenu.bagActionIndex].equals("Hủy")) {
                                 return;
                              }

                              GameMenu.bagPage = 1;
                           }
                        } else if (var0 == KeyMap.softRightKey) {
                           GameCanvas.instance.world.menu.showPage((byte)6, (byte)1);
                           GameCanvas.instance.world.menu.resetListCursors();
                        } else if (var0 == 6) {
                           if (GameMenu.bagActionIndex >= GameMenu.bagActionNames.length - 1) {
                              return;
                           }

                           GameMenu.bagActionIndex++;
                        } else {
                           if (var0 != 1 || GameMenu.bagActionIndex <= 0) {
                              return;
                           }

                           GameMenu.bagActionIndex--;
                        }
                     } else if (GameMenu.bagPage == 4) {
                        if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                           if (GameData.itemTypes[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])] != 3) {
                              GameMenu.bagItems = removeBagItems(GameMenu.bagItems, GameMenu.bagTabIndex, GameMenu.bagItemIndex, GameMenu.bagQuantity);
                           } else {
                              GameCanvas.instance.world.menu.showToast("Không thể bỏ vật phẩm này!", 8);
                           }

                           GameMenu.bagQuantity = 1;
                           GameMenu.bagPage = 1;
                        } else if (var0 == KeyMap.softRightKey) {
                           GameCanvas.instance.world.menu.showPage((byte)6, (byte)2);
                        } else if (var0 == 5) {
                           if (++GameMenu.bagQuantity <= GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][1]) {
                              return;
                           }

                           GameMenu.bagQuantity = 1;
                        } else {
                           if (var0 != 2 || --GameMenu.bagQuantity > 0) {
                              return;
                           }

                           GameMenu.bagQuantity = GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][1];
                        }
                     } else {
                        if (GameMenu.bagPage != 3) {
                           return;
                        }

                        if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                           if (GameData.itemTypes[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])] != 3) {
                              GameData.money = GameData.money
                                 + GameData.itemSellPrices[GameData.findItemIndex(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0])]
                                    * GameMenu.bagQuantity;
                              GameMenu.bagItems = removeBagItems(GameMenu.bagItems, GameMenu.bagTabIndex, GameMenu.bagItemIndex, GameMenu.bagQuantity);
                           } else {
                              GameCanvas.instance.world.menu.showToast("Không thể bán vật phẩm này!", 8);
                           }

                           GameMenu.bagQuantity = 1;
                           GameMenu.bagPage = 1;
                        } else if (var0 == KeyMap.softRightKey) {
                           GameCanvas.instance.world.menu.showPage((byte)6, (byte)2);
                        } else {
                           if (var0 != 5) {
                              if (var0 == 2 && --GameMenu.bagQuantity <= 0) {
                                 GameMenu.bagQuantity = GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][1];
                              }

                              return;
                           }

                           if (++GameMenu.bagQuantity <= GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][1]) {
                              return;
                           }

                           GameMenu.bagQuantity = 1;
                        }
                     }
                  } else {
                     if (GameMenu.menuPage == 7) {
                        handleQuestPageKey(var0);
                        return;
                     }

                     if (GameMenu.menuPage == 4) {
                        if (GameMenu.heroSkillCount == 0) {
                           GameMenu.menuPage = 1;
                        } else {
                           if (GameMenu.skillPage != 0) {
                              if (GameMenu.skillPage == 1 && (var0 == 8 || var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey)) {
                                 GameCanvas.instance.world.menu.showPage((byte)4, (byte)0);
                              }

                              return;
                           }

                           if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                              GameMenu.skillPage = 1;
                           } else if (var0 == KeyMap.softRightKey) {
                              GameCanvas.instance.world.menu.heroSkillList.resetSelection(0);
                              GameCanvas.instance.world.menu.heroSkillList.scrollToTop();
                              GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
                              GameCanvas.instance.world.menu.resetListCursors();
                           } else if (var0 == 1) {
                              GameCanvas.instance.world.menu.heroSkillList.selectPrev();
                           } else {
                              if (var0 != 6) {
                                 return;
                              }

                              GameCanvas.instance.world.menu.heroSkillList.selectNext();
                           }
                        }
                     } else {
                        if (GameMenu.menuPage == 8) {
                           handlePetBookKey(var0);
                           return;
                        }

                        if (GameMenu.menuPage == 13) {
                           int var1 = 1;
                           if (GameMenu.pets != null) {
                              var1 = 1 + GameMenu.pets.length;
                           }

                           if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                              if (var0 == KeyMap.softRightKey) {
                                 GameMenu.bagTabIndex = 0;
                                 GameMenu.bagScrollRow = 0;
                                 GameMenu.bagItemIndex = 0;
                                 GameCanvas.instance.world.menu.showPage((byte)6, (byte)0);
                              } else {
                                 if (var0 != 1) {
                                    if (var0 == 6) {
                                       if (GameMenu.useTargetIndex < var1 - 1) {
                                          GameMenu.useTargetIndex++;
                                       }

                                       if (GameMenu.useTargetIndex - GameMenu.useTargetScroll == GameMenu.useTargetRows) {
                                          GameMenu.useTargetScroll++;
                                          return;
                                       }
                                    }

                                    return;
                                 }

                                 if (GameMenu.useTargetScroll > 0 && GameMenu.useTargetScroll == GameMenu.useTargetIndex) {
                                    GameMenu.useTargetScroll--;
                                 }

                                 if (GameMenu.useTargetIndex <= 0) {
                                    return;
                                 }

                                 GameMenu.useTargetIndex--;
                              }
                           } else {
                              if (GameMenu.useTargetIndex > 0) {
                                 if (GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0] == 52) {
                                    if (GameData.discoveredPetIds == null) {
                                       GameCanvas.instance.world.menu.showToast("Đồ giám không có hình dạng để chọn", 10);
                                       return;
                                    }

                                    GameCanvas.instance.world.menu.showPage((byte)12, (byte)0);
                                    return;
                                 }

                                 GameData.useItemOnPet(
                                    GameMenu.pets[GameMenu.useTargetIndex - 1], GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0]
                                 );
                              } else {
                                 if (GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0] == 52) {
                                    GameCanvas.instance.world.menu.showToast("Không dùng cho nhân vật chính", 10);
                                    return;
                                 }

                                 GameData.useItemOnRole(GameData.party[0], GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0]);
                              }

                              GameMenu.bagItems = null;
                              GameMenu.buildBagItems();
                              GameMenu.useTargetScroll = 0;
                              GameMenu.useTargetIndex = 0;
                              GameCanvas.instance.world.menu.showPage((byte)6, (byte)1);
                           }
                        } else if (GameMenu.menuPage == 15) {
                           switch (GameMenu.shopPage) {
                              case 0:
                                 if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                                    GameMenu.shopPage = 1;
                                    return;
                                 }

                                 if (var0 == KeyMap.softRightKey) {
                                    closeMenu();
                                    GameMenu.buyCount = 1;
                                    GameCanvas.instance.world.script.advance(5);
                                    return;
                                 }

                                 if (var0 == 1) {
                                    GameMenu.shopList.selectPrev();
                                    return;
                                 }

                                 if (var0 == 6) {
                                    GameMenu.shopList.selectNext();
                                 }
                                 break;
                              case 1:
                                 handleShopBuyKey(var0);
                                 return;
                              default:
                                 return;
                           }
                        } else if (GameMenu.menuPage == 12) {
                           if (var0 == 8 || var0 == KeyMap.softLeftKey) {
                              GameData.removeItem(GameMenu.bagItems[GameMenu.bagTabIndex][GameMenu.bagItemIndex][0], 1);
                              GameMenu.pets[GameMenu.useTargetIndex - 1].petId = GameCanvas.instance.world.menu.formList.getSelectedPetId();
                              GameCanvas.instance.world.menu.showToast(GameMenu.pets[GameMenu.useTargetIndex - 1].name + " Đã đổi hình dạng", 10);
                              GameMenu.bagItems = null;
                              GameCanvas.instance.world.menu.buildPetList();
                              GameMenu.buildBagItems();
                              GameCanvas.instance.world.menu.showPage((byte)6, (byte)1);
                              return;
                           }

                           if (var0 == KeyMap.softRightKey) {
                              GameMenu.bagTabIndex = 0;
                              GameMenu.bagScrollRow = 0;
                              GameMenu.bagItemIndex = 0;
                              GameCanvas.instance.world.menu.showPage((byte)6, (byte)0);
                              return;
                           }

                           if (var0 == 1) {
                              GameCanvas.instance.world.menu.formList.selectPrev();
                              GameMenu.previewAnim = new AnimSprite();
                              GameMenu.previewAnim = GameUtil.loadBattleAnimation(GameCanvas.instance.world.menu.formList.getSelectedPetId(), true);
                              return;
                           }

                           if (var0 == 6) {
                              GameCanvas.instance.world.menu.formList.selectNext();
                              GameMenu.previewAnim = new AnimSprite();
                              GameMenu.previewAnim = GameUtil.loadBattleAnimation(GameCanvas.instance.world.menu.formList.getSelectedPetId(), true);
                              return;
                           }

                           if (var0 == 2) {
                              GameCanvas.instance.world.menu.formList.prevPage();
                              GameMenu.previewAnim = new AnimSprite();
                              GameMenu.previewAnim = GameUtil.loadBattleAnimation(GameCanvas.instance.world.menu.formList.getSelectedPetId(), true);
                              return;
                           }

                           if (var0 == 5) {
                              GameCanvas.instance.world.menu.formList.nextPage();
                              GameMenu.previewAnim = new AnimSprite();
                              GameMenu.previewAnim = GameUtil.loadBattleAnimation(GameCanvas.instance.world.menu.formList.getSelectedPetId(), true);
                           }
                        }
                     }
                  }
               }
            }
         }

         return;
      }

      GameCanvas.instance.world.menu.showPage(var5, (byte)0);
   }

   private static void handleShopBuyKey(int var0) {
      if (var0 != 8 && var0 != KeyMap.softLeftKey) {
         if (var0 == KeyMap.softRightKey) {
            GameMenu.shopPage = 0;
         } else {
            if (var0 == 2) {
               if (GameMenu.buyCount > 1) {
                  GameMenu.buyCount--;
                  return;
               }

               GameMenu.buyCount = 99;
               return;
            }

            if (var0 != 5) {
               return;
            }

            if (GameMenu.buyCount < 99) {
               GameMenu.buyCount++;
               return;
            }
         }

         GameMenu.buyCount = 1;
      } else if (GameMenu.shopType == 0) {
         if (GameData.money
            < GameData.itemBuyPrices[GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] * GameMenu.buyCount) {
            GameCanvas.instance.world.menu.showToast("Không đủ vàng", 15);
            return;
         }

         if (GameData.bagItems != null) {
            var0 = GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()));
            if (GameData.countBagSlotsInGroup(GameData.itemIds[var0]) > GameSettings.maxItemTypes) {
               GameCanvas.instance.world.menu.showToast("Túi đồ đã đầy", 10);
               return;
            }

            if (GameData.getItemCount(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow())) + GameMenu.buyCount > 99) {
               GameMenu.buyCount = 99 - GameData.getItemCount(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()));
            }

            if (GameMenu.buyCount <= 0) {
               GameCanvas.instance.showToast("Số vật phẩm đã đạt giới hạn 99", 10);
               GameMenu.buyCount = 1;
               return;
            }

            GameData.addItem(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()), GameMenu.buyCount);
            GameData.money = GameData.money
               - GameData.itemBuyPrices[GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] * GameMenu.buyCount;
            GameCanvas.instance
               .world
               .menu
               .showToast(
                  "Mua " + GameData.itemNames[GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] + GameMenu.buyCount, 12
               );
            GameMenu.buyCount = 1;
            GameMenu.shopPage = 0;
            return;
         }

         if (GameData.bagItems == null) {
            if (GameData.getItemCount(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow())) + GameMenu.buyCount > 99) {
               GameMenu.buyCount = 99 - GameData.getItemCount(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()));
            }

            if (GameMenu.buyCount <= 0) {
               GameCanvas.instance.showToast("Số vật phẩm đã đạt giới hạn 99", 10);
               GameMenu.buyCount = 1;
               return;
            }

            GameData.addItem(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()), GameMenu.buyCount);
            GameData.money = GameData.money
               - GameData.itemBuyPrices[GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] * GameMenu.buyCount;
            GameCanvas.instance
               .world
               .menu
               .showToast(
                  "Mua " + GameData.itemNames[GameData.findItemIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] + GameMenu.buyCount, 12
               );
            GameMenu.buyCount = 1;
            GameMenu.shopPage = 0;
            return;
         }
      } else if (GameMenu.shopType == 1) {
         if (GameData.money < Equip.prices[GameData.findEquipIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] * GameMenu.buyCount) {
            GameCanvas.instance.world.menu.showToast("Không đủ vàng", 15);
            return;
         }

         if (GameData.equipBagIds != null) {
            for (int var2 = 0; var2 < GameMenu.buyCount; var2++) {
               GameData.addEquip(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()), 1);
            }

            GameData.money = GameData.money
               - Equip.prices[GameData.findEquipIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))] * GameMenu.buyCount;
            GameCanvas.instance
               .world
               .menu
               .showToast("Mua " + Equip.names[GameData.findEquipIndex(GameMenu.shopList.getRowId(GameMenu.shopList.getSelectedRow()))], 12);
            GameMenu.buyCount = 1;
            GameMenu.shopPage = 0;
            return;
         }
      }
   }

   private static boolean canLearnerLearnSkill() {
      short[][] var0 = Skill.learnRules[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))];
      int var1 = 0;

      for (int var2 = var0.length; var1 < var2; var1++) {
         if (var0[var1][0] == GameMenu.skillLearnerType) {
            return true;
         }
      }

      return false;
   }

   private static boolean meetsSkillLevel(int var0, int var1) {
      short[][] var2 = Skill.learnRules[GameData.findSkillIndex(GameMenu.skillShopList.getRowId(GameMenu.skillShopList.getSelectedRow()))];
      int var3 = -1;

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4][0] == var0) {
            var3 = var4;
            break;
         }
      }

      if (var3 != -1) {
         if (var0 != 4) {
            if (var2[var3][1] <= GameData.party[0].stats[0]) {
               return true;
            }
         } else if (var2[var3][1] <= GameMenu.pets[var1].stats[0] && GameMenu.pets[var1].skillIds.length <= 5) {
            return true;
         }
      }

      return false;
   }

   private static boolean hasLearnedSkill(int var0, int var1, int var2) {
      if (var0 != 4) {
         for (int var3 = 0; GameData.party[0].skills != null && var3 < GameData.party[0].skills.length; var3++) {
            if (GameData.party[0].skills[var3].skillId == Skill.ids[GameData.findSkillIndex(GameMenu.shopItemIds[var2])]) {
               return true;
            }
         }
      } else {
         for (int var4 = 0; GameMenu.pets[var1].skills != null && var4 < GameMenu.pets[var1].skills.length; var4++) {
            if (GameMenu.pets[var1].skills[var4].skillId == Skill.ids[GameData.findSkillIndex(GameMenu.shopItemIds[var2])]) {
               return true;
            }
         }
      }

      return false;
   }

   private static void handlePetBookKey(int var0) {
      if (GameMenu.dexPage == 0) {
         if (var0 == KeyMap.softRightKey) {
            GameMenu.dexElements = null;
            GameMenu.dexStatStars = null;
            GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
            GameCanvas.instance.world.menu.resetListCursors();
            return;
         }

         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == 6) {
               if (GameCanvas.instance.world.menu.b_I == 2) {
                  GameCanvas.instance.world.menu.dexAllList.selectNext();
                  return;
               }

               if (GameCanvas.instance.world.menu.b_I == 0) {
                  if (GameCanvas.instance.world.menu.dexFoundList != null) {
                     GameCanvas.instance.world.menu.dexFoundList.selectNext();
                     return;
                  }
               } else if (GameCanvas.instance.world.menu.b_I == 1 && GameCanvas.instance.world.menu.dexElementList != null) {
                  GameCanvas.instance.world.menu.dexElementList.selectNext();
                  return;
               }
            } else if (var0 == 1) {
               if (GameCanvas.instance.world.menu.b_I == 2) {
                  GameCanvas.instance.world.menu.dexAllList.selectPrev();
                  return;
               }

               if (GameCanvas.instance.world.menu.b_I == 0) {
                  if (GameCanvas.instance.world.menu.dexFoundList != null) {
                     GameCanvas.instance.world.menu.dexFoundList.selectPrev();
                     return;
                  }
               } else if (GameCanvas.instance.world.menu.b_I == 1 && GameCanvas.instance.world.menu.dexElementList != null) {
                  GameCanvas.instance.world.menu.dexElementList.selectPrev();
                  return;
               }
            } else if (var0 == 5) {
               if (GameCanvas.instance.world.menu.b_I == 2) {
                  GameCanvas.instance.world.menu.dexAllList.nextPage();
                  return;
               }

               if (GameCanvas.instance.world.menu.b_I == 0) {
                  if (GameCanvas.instance.world.menu.dexFoundList != null) {
                     GameCanvas.instance.world.menu.dexFoundList.nextPage();
                     return;
                  }
               } else if (GameCanvas.instance.world.menu.b_I == 1 && GameCanvas.instance.world.menu.dexElementList != null) {
                  GameCanvas.instance.world.menu.dexElementList.nextPage();
                  return;
               }
            } else if (var0 == 2) {
               if (GameCanvas.instance.world.menu.b_I == 2) {
                  GameCanvas.instance.world.menu.dexAllList.prevPage();
                  return;
               }

               if (GameCanvas.instance.world.menu.b_I == 0) {
                  if (GameCanvas.instance.world.menu.dexFoundList != null) {
                     GameCanvas.instance.world.menu.dexFoundList.prevPage();
                     return;
                  }
               } else if (GameCanvas.instance.world.menu.b_I == 1 && GameCanvas.instance.world.menu.dexElementList != null) {
                  GameCanvas.instance.world.menu.dexElementList.prevPage();
                  return;
               }
            } else {
               if (var0 == 55) {
                  GameCanvas.instance.world.menu.b_I--;
                  GameCanvas.instance.world.menu.b_I = (GameCanvas.instance.world.menu.b_I + 3) % 3;
                  return;
               }

               if (var0 == 57) {
                  GameCanvas.instance.world.menu.b_I++;
                  GameCanvas.instance.world.menu.b_I %= 3;
                  return;
               }
            }
         } else {
            if (GameCanvas.instance.world.menu.b_I == 2) {
               String var10 = GameCanvas.instance.world.menu.dexAllList.getSelectedName();
               int var13 = GameCanvas.instance.world.menu.dexAllList.getSelectedPetId();
               if (!var10.equals("") && !var10.equals("???")) {
                  GameCanvas.instance.world.menu.loadDexPet(var13);
                  GameCanvas.instance.world.menu.dexPetId = var13;
                  GameCanvas.instance.world.menu.showPage((byte)8, (byte)1);
               }

               return;
            }

            if (GameCanvas.instance.world.menu.b_I == 1) {
               String var6 = GameCanvas.instance.world.menu.dexElementList.getSelectedName();
               int var12 = GameCanvas.instance.world.menu.dexElementList.getSelectedPetId();
               if (!var6.equals("") && !var6.equals("???")) {
                  GameMenu.dexElements = null;
                  GameMenu.dexStatStars = null;
                  GameMenu.previewAnim = new AnimSprite();
                  GameMenu.previewAnim = GameUtil.loadBattleAnimation(var12, false);
                  String[] var15;
                  short[][] var20 = new short[(var15 = Utils.split(GameMenu.petData[var12 - 1][19], "|")).length][2];

                  for (int var23 = 0; var23 < var15.length; var23++) {
                     short[] var7 = Utils.parseShortArray(var15[var23], ",");
                     var20[var23][0] = var7[0];
                     var20[var23][1] = var7[1];
                  }

                  GameCanvas.instance.world.menu.fusionRecipe = var20;
                  if (GameMenu.petData[var12 - 1][2] != null) {
                     for (int var24 = 0; var24 < 4; var24++) {
                        String var8;
                        if ((var8 = GameMenu.petData[var12 - 1][2].substring(var24, var24 + 1)).equals("a")) {
                           GameMenu.dexElements = Utils.append(GameMenu.dexElements, 10);
                        } else {
                           GameMenu.dexElements = Utils.append(GameMenu.dexElements, Utils.parseInt(var8));
                        }
                     }
                  }

                  for (int var9 = 0; var9 < 5; var9++) {
                     byte[] var25 = Utils.parseByteArray(GameMenu.petData[var12 - 1][4 + var9 * 2], "|");
                     GameMenu.dexStatStars = Utils.append(GameMenu.dexStatStars, var25[0]);
                  }

                  if (GameData.newPetFlags[var12 - 1]) {
                     GameData.newPetFlags[var12 - 1] = false;
                  }

                  GameMenu.dexPetIndex = var12 - 1;
                  GameCanvas.instance.world.menu.dexPetId = var12;
                  GameCanvas.instance.world.menu.showPage((byte)8, (byte)1);
               }

               return;
            }

            if (GameCanvas.instance.world.menu.b_I == 0 && GameCanvas.instance.world.menu.dexFoundList != null) {
               var0 = GameCanvas.instance.world.menu.dexFoundList.getSelectedPetId();
               GameMenu.dexElements = null;
               GameMenu.dexStatStars = null;
               GameMenu.previewAnim = new AnimSprite();
               GameMenu.previewAnim = GameUtil.loadBattleAnimation(var0, false);
               if (GameMenu.petData == null) {
                  GameMenu.petData = Utils.getStringTable(Utils.loadConfigText("/bin/petdata.bin"), "petdata", "end", null, "\t");
               }

               String[] var1;
               short[][] var2 = new short[(var1 = Utils.split(GameMenu.petData[var0 - 1][19], "|")).length][2];

               for (int var3 = 0; var3 < var1.length; var3++) {
                  short[] var4 = Utils.parseShortArray(var1[var3], ",");
                  var2[var3][0] = var4[0];
                  var2[var3][1] = var4[1];
               }

               GameCanvas.instance.world.menu.fusionRecipe = var2;
               if (GameMenu.petData[var0 - 1][2] != null) {
                  for (int var18 = 0; var18 < 4; var18++) {
                     String var21;
                     if ((var21 = GameMenu.petData[var0 - 1][2].substring(var18, var18 + 1)).equals("a")) {
                        GameMenu.dexElements = Utils.append(GameMenu.dexElements, 10);
                     } else {
                        GameMenu.dexElements = Utils.append(GameMenu.dexElements, Utils.parseInt(var21));
                     }
                  }
               }

               for (int var22 = 0; var22 < 5; var22++) {
                  byte[] var19 = Utils.parseByteArray(GameMenu.petData[var0 - 1][4 + var22 * 2], "|");
                  GameMenu.dexStatStars = Utils.append(GameMenu.dexStatStars, var19[0]);
               }

               if (GameData.newPetFlags[var0 - 1]) {
                  GameData.newPetFlags[var0 - 1] = false;
               }

               System.out.println("petID" + var0);
               GameMenu.dexPetIndex = var0 - 1;
               GameCanvas.instance.world.menu.dexPetId = var0;
               GameCanvas.instance.world.menu.showPage((byte)8, (byte)1);
               return;
            }
         }
      } else if (GameMenu.dexPage == 1) {
         if (var0 == KeyMap.softRightKey) {
            if (!GameCanvas.instance.world.menu.isDexFromScript) {
               GameCanvas.instance.world.menu.showPage((byte)8, (byte)0);
               return;
            }

            GameCanvas.instance.world.menu.isDexFromScript = false;
            closeMenu();
            GameCanvas.instance.world.script.advance(5);
            return;
         }

         if (var0 == KeyMap.softLeftKey) {
            GameCanvas.instance.world.menu.showPage((byte)8, (byte)2);
            return;
         }
      } else if (GameMenu.dexPage == 2 && !GameCanvas.instance.world.menu.isFusing) {
         if (var0 == KeyMap.softRightKey) {
            if (!GameCanvas.instance.world.menu.isDexFromScript) {
               GameCanvas.instance.world.menu.showPage((byte)8, (byte)1);
               return;
            }

            GameCanvas.instance.world.menu.isDexFromScript = false;
            closeMenu();
            GameCanvas.instance.world.script.advance(5);
            return;
         }

         if (var0 == KeyMap.softLeftKey) {
            short[][] var11 = GameCanvas.instance.world.menu.fusionRecipe;
            boolean var14 = true;
            if (var11 != null) {
               for (int var16 = 0; var16 < var11.length; var16++) {
                  if (GameData.getItemCount(var11[var16][0]) < var11[var16][1]) {
                     var14 = false;
                  }
               }
            }

            if (!var14) {
               GameCanvas.instance.world.menu.showToast("Không đủ nguyên liệu", 10);
               return;
            }

            if (GameMenu.pets != null && GameMenu.pets.length >= GameSettings.maxPetCount) {
               GameCanvas.instance.world.menu.showToast("Ô sủng vật đã đầy", 8);
               return;
            }

            for (int var17 = 0; var17 < var11.length; var17++) {
               GameData.removeItem(var11[var17][0], var11[var17][1]);
            }

            GameCanvas.instance.world.menu.isFusing = true;
            GameCanvas.instance.world.menu.isFusionDone = false;
            GameCanvas.instance.world.menu.fusionProgress = 0;
         } else {
            if (var0 == 1) {
               GameCanvas.instance.world.menu.fusionMaterialList.selectPrev();
               return;
            }

            if (var0 == 6) {
               GameCanvas.instance.world.menu.fusionMaterialList.selectNext();
            }
         }
      }
   }

   private static void handleQuestPageKey(int var0) {
      if (GameMenu.questPage == 0) {
         if (var0 == 8 || var0 == KeyMap.softLeftKey) {
            if (GameMenu.questTabIndex == 1) {
               if (GameCanvas.instance.world.menu.sideQuestList == null || GameCanvas.instance.world.menu.sideQuestList.itemCount() == 0) {
                  GameCanvas.instance.world.menu.showToast("Hiện chưa nhận nhiệm vụ phụ tuyến", 10);
                  return;
               }
            } else if (GameMenu.questTabIndex == 0
               && (GameCanvas.instance.world.menu.mainQuestList == null || GameCanvas.instance.world.menu.mainQuestList.itemCount() == 0)) {
               GameCanvas.instance.world.menu.showToast("Hiện chưa nhận nhiệm vụ chính tuyến", 10);
               return;
            }

            GameCanvas.instance.world.menu.showPage((byte)7, (byte)(GameMenu.questTabIndex + 1));
            return;
         }

         if (var0 == KeyMap.softRightKey) {
            GameMenu.questNames = null;
            GameMenu.questTabIndex = 0;
            GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
            GameCanvas.instance.world.menu.resetListCursors();
            return;
         }

         if (var0 == 5) {
            GameMenu.questTabIndex = (byte)(GameMenu.questTabIndex == 1 ? 0 : 1);
            return;
         }

         if (var0 == 2) {
            GameMenu.questTabIndex = (byte)(GameMenu.questTabIndex == 0 ? 1 : 0);
            return;
         }
      } else if (GameMenu.questPage != 1 && GameMenu.questPage != 2) {
         if (GameMenu.questPage == 3) {
            if (var0 == 8 || var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey) {
               GameMenu.questPage = (byte)(GameMenu.questTabIndex + 1);
               return;
            }

            if (var0 == 6) {
               QuestDetailBox var8 = GameCanvas.instance.world.menu.questDetail;
               if (GameCanvas.instance.world.menu.questDetail.scrollLine < var8.totalLines - var8.visibleLines) {
                  var8.scrollLine++;
               }

               return;
            }

            if (var0 == 1) {
               QuestDetailBox var7 = GameCanvas.instance.world.menu.questDetail;
               if (GameCanvas.instance.world.menu.questDetail.scrollLine > 0) {
                  var7.scrollLine--;
               }

               return;
            }
         } else if (GameMenu.questPage == 4) {
            if (var0 == 8 || var0 == KeyMap.softLeftKey) {
               if (GameMenu.questActionIndex == 0) {
                  GameCanvas.instance.world.menu.questIndex = GameCanvas.instance.world.menu.sideQuestList.getSelectedQuestIndex();
                  GameCanvas.instance.world.menu.showPage((byte)7, (byte)3);
                  return;
               }

               GameCanvas.instance.world.menu.questIndex = GameCanvas.instance.world.menu.sideQuestList.getSelectedQuestIndex();
               GameMenu.questPage = 5;
               Object var11 = null;
               SideQuestListPanel var6 = GameCanvas.instance.world.menu.sideQuestList;
               GameCanvas.instance.world.menu.sideQuestList.cursorImg = (Image)var11;
               return;
            }

            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)7, (byte)2);
               return;
            }

            if (var0 == 6) {
               if (GameMenu.questActionIndex <= 0) {
                  GameMenu.questActionIndex++;
                  return;
               }
            } else if (var0 == 1 && GameMenu.questActionIndex > 0) {
               GameMenu.questActionIndex--;
               return;
            }
         } else if (GameMenu.questPage == 5) {
            if (var0 == 8 || var0 == KeyMap.softLeftKey) {
               short var4 = GameData.activeQuests[GameCanvas.instance.world.menu.questIndex].id;
               GameData.removeQuest(GameData.activeQuests[GameCanvas.instance.world.menu.questIndex]);
               GameData.activeQuestIds = Utils.removeValue(GameData.activeQuestIds, var4);
               GameCanvas.instance.world.menu.buildQuestLists();
               GameMenu.questNames = Utils.removeAt(GameMenu.questNames, 0);
               if (GameCanvas.instance.world.menu.sideQuestList != null && GameCanvas.instance.world.menu.sideQuestList.itemCount() != 0) {
                  Image var10 = GameCanvas.instance.world.menu.cursorArrow;
                  SideQuestListPanel var5 = GameCanvas.instance.world.menu.sideQuestList;
                  GameCanvas.instance.world.menu.sideQuestList.cursorImg = var10;
                  GameMenu.questPage = 2;
                  return;
               }

               GameCanvas.instance.world.menu.showPage((byte)7, (byte)0);
            } else if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)7, (byte)2);
            }
         }
      } else if (var0 != 8 && var0 != KeyMap.softLeftKey) {
         if (var0 == KeyMap.softRightKey) {
            GameMenu.j_B = 0;
            GameCanvas.instance.world.menu.showPage((byte)7, (byte)0);
            GameCanvas.instance.world.menu.resetListCursors();
            return;
         }

         if (var0 == 1) {
            if (GameMenu.questPage == 1) {
               GameCanvas.instance.world.menu.mainQuestList.selectPrev();
               return;
            }

            GameCanvas.instance.world.menu.sideQuestList.selectPrev();
            return;
         }

         if (var0 == 6) {
            if (GameMenu.questPage == 1) {
               GameCanvas.instance.world.menu.mainQuestList.selectNext();
               return;
            }

            GameCanvas.instance.world.menu.sideQuestList.selectNext();
            return;
         }
      } else {
         if (GameMenu.questPage != 2) {
            if (GameCanvas.instance.world.menu.mainQuestList.getSelectedStatus() == 1) {
               GameCanvas.instance.world.menu.showToast("Đã nộp nhiệm vụ hoàn thành!", 10);
               return;
            }

            boolean var3 = false;

            for (int var9 = 0; GameData.activeQuests != null && var9 < GameData.activeQuests.length; var9++) {
               if (GameData.activeQuests[var9].name.equals(GameCanvas.instance.world.menu.mainQuestList.getSelectedName())
                  && GameCanvas.instance.world.menu.mainQuestList.getSelectedStatus() != 1) {
                  GameCanvas.instance.world.menu.questIndex = var9;
                  var3 = true;
                  break;
               }
            }

            if (var3) {
               GameCanvas.instance.world.menu.showPage((byte)7, (byte)3);
            }

            return;
         }

         boolean var2 = false;

         for (int var1 = 0; var1 < GameData.activeQuests.length; var1++) {
            if (GameData.activeQuests[var1].name.equals(GameCanvas.instance.world.menu.sideQuestList.getSelectedName())) {
               if (GameData.activeQuests[var1].repeatable == 1) {
                  var2 = true;
               }
               break;
            }
         }

         if (!var2) {
            GameCanvas.instance.world.menu.questIndex = GameCanvas.instance.world.menu.sideQuestList.getSelectedQuestIndex();
            GameCanvas.instance.world.menu.showPage((byte)7, (byte)3);
            return;
         }

         GameMenu.questPage = 4;
         GameCanvas.instance.world.menu.questIndex = GameCanvas.instance.world.menu.sideQuestList.getSelectedQuestIndex();
      }
   }

   private static short[][][] removeBagItems(short[][][] var0, int var1, int var2, int var3) {
      if (var3 <= 0) {
         return var0;
      } else {
         GameData.removeItem(var0[var1][var2][0], var3);
         if (var0[var1][var2][1] > var3) {
            var0[var1][var2][1] = (short)(var0[var1][var2][1] - var3);
         } else {
            var0[var1] = Utils.removeAt(var0[var1], var2);
         }

         return var0;
      }
   }

   private static void handleEquipPageKey(int var0) {
      if (GameMenu.equipPage == 0) {
         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == KeyMap.softRightKey) {
               GameMenu.equipSlotIndex = 0;
               GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
               GameCanvas.instance.world.menu.resetListCursors();
               return;
            }

            if (var0 == 6) {
               if (GameMenu.equipSlotIndex < 11) {
                  GameMenu.equipSlotIndex++;
                  return;
               }
            } else if (var0 == 1) {
               if (GameMenu.equipSlotIndex > 0) {
                  GameMenu.equipSlotIndex--;
                  return;
               }
            } else if (var0 == 5) {
               if (GameMenu.equipSlotIndex >= 5 && GameMenu.equipSlotIndex < 11) {
                  GameMenu.equipSlotIndex++;
               }

               if (GameMenu.equipSlotIndex < 3) {
                  GameMenu.equipSlotIndex = (byte)(GameMenu.equipSlotIndex + 3);
                  return;
               }

               if (GameMenu.equipSlotIndex == 3) {
                  GameMenu.equipSlotIndex = 1;
                  return;
               }

               if (GameMenu.equipSlotIndex == 4) {
                  GameMenu.equipSlotIndex = 2;
                  return;
               }
            } else if (var0 == 2) {
               if (GameMenu.equipSlotIndex > 5) {
                  GameMenu.equipSlotIndex--;
                  return;
               }

               if (GameMenu.equipSlotIndex >= 3 && GameMenu.equipSlotIndex <= 5) {
                  GameMenu.equipSlotIndex = (byte)(GameMenu.equipSlotIndex - 3);
                  return;
               }

               if (GameMenu.equipSlotIndex == 2) {
                  GameMenu.equipSlotIndex = 4;
                  return;
               }

               if (GameMenu.equipSlotIndex == 1) {
                  GameMenu.equipSlotIndex = 3;
                  return;
               }
            }
         } else if (GameMenu.equipSlotIndex < 6) {
            if (GameMenu.hero.equips[GameMenu.equipSlotIndex] == null && GameData.equipBag[GameMenu.equipSlotIndex] == null) {
               String[] var7 = new String[]{"Vũ khí", "Mũ", "Áo", "Giày", "Dây chuyền", "Nhẫn"};
               GameCanvas.instance.world.menu.showToast("Không có " + var7[GameMenu.equipSlotIndex], 10);
               return;
            }

            GameMenu.equipBagIndex = 0;
            GameMenu.equipBagScrollRow = 0;
            GameMenu.equipPage = 1;
            return;
         }
      } else if (GameMenu.equipPage == 1) {
         if (var0 == 8 || var0 == KeyMap.softLeftKey) {
            GameMenu.equipActionIndex = 0;
            GameMenu.equipActionNames = new String[]{"Thay", "Tháo", "Bỏ", "Khảm"};
            GameMenu.equipActionIds = new int[]{10, 9, 12, 11};
            if (GameData.equipBag[GameMenu.equipSlotIndex] == null || GameMenu.equipBagIndex >= GameData.equipBag[GameMenu.equipSlotIndex].length) {
               GameMenu.equipActionNames = new String[]{"Tháo"};
               GameMenu.equipActionIds = new int[]{9};
            } else if (GameMenu.hero.equips[GameMenu.equipSlotIndex] == null) {
               GameMenu.equipActionNames = new String[]{"Trang bị ", "Khảm", "Bỏ"};
               GameMenu.equipActionIds = new int[]{8, 11, 12};
            }

            GameMenu.equipPage = 2;
            return;
         }

         if (var0 == KeyMap.softRightKey) {
            GameCanvas.instance.world.menu.showPage((byte)5, (byte)0);
            GameCanvas.instance.world.menu.resetListCursors();
            return;
         }

         if (var0 == 5 || var0 == 2 || var0 == 6 || var0 == 1) {
            if (var0 == 5) {
               if (GameMenu.equipBagIndex < GameSettings.maxItemTypes - 1) {
                  GameMenu.equipBagIndex++;
               }

               if (GameMenu.equipBagIndex - GameMenu.equipBagScrollRow * GameMenu.gridCols >= GameMenu.gridCols * GameMenu.gridRows) {
                  GameMenu.equipBagScrollRow++;
                  return;
               }
            } else if (var0 == 2) {
               if (GameMenu.equipBagScrollRow > 0 && GameMenu.equipBagIndex == GameMenu.equipBagScrollRow * GameMenu.gridCols) {
                  GameMenu.equipBagScrollRow--;
               }

               if (GameMenu.equipBagIndex > 0) {
                  GameMenu.equipBagIndex--;
                  return;
               }
            } else if (var0 == 6) {
               if (GameMenu.equipBagIndex + GameMenu.gridCols <= GameSettings.maxItemTypes - 1) {
                  GameMenu.equipBagIndex = (short)(GameMenu.equipBagIndex + GameMenu.gridCols);
               } else if (GameMenu.equipBagIndex < GameSettings.maxItemTypes - 1) {
                  GameMenu.equipBagIndex++;
               }

               if (GameMenu.equipBagIndex - GameMenu.equipBagScrollRow * GameMenu.gridCols >= GameMenu.gridCols * GameMenu.gridRows) {
                  GameMenu.equipBagScrollRow++;
                  return;
               }
            } else if (var0 == 1) {
               if (GameMenu.equipBagIndex - GameMenu.gridCols >= 0) {
                  GameMenu.equipBagIndex = (short)(GameMenu.equipBagIndex - GameMenu.gridCols);
               } else if (GameMenu.equipBagIndex > 0) {
                  GameMenu.equipBagIndex--;
               }

               if (GameMenu.equipBagScrollRow > 0 && GameMenu.equipBagIndex < GameMenu.equipBagScrollRow * GameMenu.gridCols) {
                  GameMenu.equipBagScrollRow--;
                  return;
               }
            }
         }
      } else if (GameMenu.equipPage == 2) {
         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)5, (byte)1);
               GameCanvas.instance.world.menu.resetListCursors();
               return;
            }

            if (var0 == 1) {
               if (GameMenu.equipActionIndex > 0) {
                  GameMenu.equipActionIndex--;
                  return;
               }
            } else if (var0 == 6 && GameMenu.equipActionIndex < GameMenu.equipActionNames.length - 1) {
               GameMenu.equipActionIndex++;
               return;
            }
         } else {
            if (GameMenu.equipActionNames[GameMenu.equipActionIndex].equals("Thay")) {
               equipFromBag(GameMenu.hero, GameMenu.equipSlotIndex, GameMenu.equipBagIndex);
               GameMenu.equipPage = 1;
               return;
            }

            if (GameMenu.equipActionNames[GameMenu.equipActionIndex].equals("Tháo")) {
               byte var8 = GameMenu.equipSlotIndex;
               RoleSprite var5 = GameMenu.hero;
               if (GameMenu.hero.equipIds[var8] > 0) {
                  GameData.equipBagIds[var8] = Utils.append(GameData.equipBagIds[var8], var5.equipIds[var8]);
                  GameData.equipBag[var8] = GameData.appendEquip(GameData.equipBag[var8], var5.equips[var8]);
                  short var2 = var5.equipIds[var8];
                  var5.equipIds[var8] = 0;
                  var5.equips[var8] = null;
                  GameData.refreshRoleStats(var5);
                  if (Utils.contains(GameMenu.setItemIds, var2)) {
                     GameMenu.setPiecesWorn--;
                  }

                  applySetBonus(var5, GameMenu.setPiecesWorn);
               }

               GameMenu.equipPage = 1;
               return;
            }

            if (GameMenu.equipActionNames[GameMenu.equipActionIndex].equals("Trang bị ")) {
               equipFromBag(GameMenu.hero, GameMenu.equipSlotIndex, GameMenu.equipBagIndex);
               GameMenu.equipPage = 1;
               return;
            }

            if (GameMenu.equipActionNames[GameMenu.equipActionIndex].equals("Bỏ")) {
               GameData.equipBagIds[GameMenu.equipSlotIndex] = Utils.removeAt(GameData.equipBagIds[GameMenu.equipSlotIndex], GameMenu.equipBagIndex);
               GameData.equipBag[GameMenu.equipSlotIndex] = GameData.removeEquipAt(GameData.equipBag[GameMenu.equipSlotIndex], GameMenu.equipBagIndex);
               GameMenu.equipPage = 1;
               return;
            }

            if (GameMenu.equipActionNames[GameMenu.equipActionIndex].equals("Khảm")) {
               if (GameData.itemTypes == null) {
                  GameData.loadItemTable();
                  GameData.loadItemIcons();
               }

               if (GameData.equipBag[GameMenu.equipSlotIndex][GameMenu.equipBagIndex].socketCount == 0) {
                  GameCanvas.instance.world.menu.showToast("Trang bị này không thể khảm", 8);
                  return;
               }

               if (GameData.equipBag[GameMenu.equipSlotIndex][GameMenu.equipBagIndex].gemCount
                  >= GameData.equipBag[GameMenu.equipSlotIndex][GameMenu.equipBagIndex].socketCount) {
                  GameCanvas.instance.world.menu.showToast("Hết lượt khảm", 8);
                  return;
               }

               boolean var4 = false;
               GameMenu.gems = null;

               for (int var1 = 0; GameData.bagItems != null && var1 < GameData.bagItems.length; var1++) {
                  if (GameData.itemTypes[GameData.findItemIndex(GameData.bagItems[var1][0])] == 5
                     && GameData.itemStatBonus[GameData.findItemIndex(GameData.bagItems[var1][0])] != ""
                     && !GameData.itemStatBonus[GameData.findItemIndex(GameData.bagItems[var1][0])].equals("-1")) {
                     GameMenu.gems = Utils.append(GameMenu.gems, new short[]{GameData.bagItems[var1][0], GameData.bagItems[var1][1]});
                     var4 = true;
                  }
               }

               if (!var4) {
                  GameCanvas.instance.world.menu.showToast("Không có đá quý", 8);
                  return;
               }

               GameMenu.gemScroll = 0;
               GameMenu.gemIndex = 0;
               GameMenu.selectedGemId = 0;
               GameCanvas.instance.world.menu.showPage((byte)5, (byte)3);
               GameMenu.socketResult = 0;
               return;
            }
         }
      } else if (GameMenu.equipPage == 3) {
         if (GameMenu.socketResult == 0) {
            if (var0 == 8) {
               if (GameMenu.gems == null) {
                  return;
               }

               var0 = GameMenu.gems.length;
               if (GameMenu.selectedGemId > 0) {
                  short var9 = GameMenu.gems[GameMenu.gemIndex][0];
                  boolean var12 = false;

                  for (int var3 = 0; var3 < GameMenu.gems.length; var3++) {
                     if (GameMenu.gems[var3][0] == GameMenu.selectedGemId) {
                        GameMenu.gems[var3][1]++;
                        var12 = true;
                        break;
                     }
                  }

                  if (!var12) {
                     short[][] var13 = new short[GameMenu.gems.length + 1][2];
                     System.arraycopy(GameMenu.gems, 0, var13, 0, GameMenu.gems.length);
                     var13[var13.length - 1][0] = (short)GameMenu.selectedGemId;
                     var13[var13.length - 1][1] = 1;
                     GameMenu.gems = var13;
                  }

                  GameMenu.selectedGemId = var9;
               } else {
                  GameMenu.selectedGemId = GameMenu.gems[GameMenu.gemIndex][0];
               }

               for (int var10 = 0; var10 < GameMenu.gems.length; var10++) {
                  if (GameMenu.gems[var10][0] == GameMenu.selectedGemId) {
                     GameMenu.gems[var10][1]--;
                     if (GameMenu.gems[var10][1] <= 0) {
                        GameMenu.gems = Utils.removeAt(GameMenu.gems, var10);
                     }
                     break;
                  }
               }

               int var11 = 0;
               if (GameMenu.gems != null) {
                  var11 = GameMenu.gems.length;
               }

               if (var11 < var0 && GameMenu.gems != null) {
                  if (GameMenu.gemScroll > 0) {
                     GameMenu.gemScroll--;
                  }

                  if (GameMenu.gemIndex >= GameMenu.gems.length - 1) {
                     GameMenu.gemIndex--;
                  }
               }

               return;
            }

            if (var0 == KeyMap.softLeftKey) {
               if (GameMenu.selectedGemId > 0) {
                  GameMenu.socketPending = true;
                  GameMenu.socketStartTime = System.currentTimeMillis();
                  return;
               }

               GameCanvas.instance.world.menu.showToast("Chưa chọn nguyên liệu", 8);
               return;
            }

            if (var0 == KeyMap.softRightKey) {
               GameMenu.selectedGemId = 0;
               GameMenu.gems = null;
               GameCanvas.instance.world.menu.showPage((byte)5, (byte)1);
               GameCanvas.instance.world.menu.resetListCursors();
               return;
            }

            if (var0 == 1) {
               if (GameMenu.gems == null) {
                  return;
               }

               if (GameMenu.gemIndex == GameMenu.gemScroll && GameMenu.gemScroll > 0) {
                  GameMenu.gemScroll--;
               }

               if (GameMenu.gemIndex > 0) {
                  GameMenu.gemIndex--;
                  return;
               }
            } else if (var0 == 6) {
               if (GameMenu.gems == null) {
                  return;
               }

               if (GameMenu.gemIndex < GameMenu.gems.length - 1) {
                  GameMenu.gemIndex++;
               }

               if (GameMenu.gemIndex == GameMenu.gemScroll + GameMenu.gemRows) {
                  GameMenu.gemScroll++;
                  return;
               }
            }
         } else if (GameMenu.socketResult == 1 && (var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey)) {
            GameMenu.selectedGemId = 0;
            GameMenu.gemIndex = 0;
            GameMenu.gemScroll = 0;
            GameCanvas.instance.world.menu.showPage((byte)5, (byte)1);
            GameMenu.socketResult = 0;
         }
      }
   }

   private static void equipFromBag(RoleSprite var0, int var1, int var2) {
      String[] var3 = new String[]{"Chiến binh", "Chú thuật sư", "Pháp sư"};
      Equip var5 = GameData.equipBag[var1][var2];
      RoleSprite var4 = var0;
      boolean var6 = false;
      if (Equip.allowedJobs != null) {
         Equip.loadTable();
      }

      short[] var12 = Utils.parseShortArray(Equip.allowedJobs[GameData.findEquipIndex(var5.equipId)], ",");

      for (int var7 = 0; var7 < var12.length; var7++) {
         if (var12[var7] == 0 || var12[var7] == var4.roleId) {
            var6 = true;
            break;
         }
      }

      if (!var6) {
         GameCanvas.instance.world.menu.showToast("Nhân vật không phù hợp", 8);
      } else {
         var5 = GameData.equipBag[var1][var2];
         var6 = false;
         if (Equip.requiredLevels == null) {
            Equip.loadTable();
         }

         short[] var14 = Equip.requiredLevels;
         boolean var8 = false;
         short var10 = var14[GameData.findEquipIndex(var5.equipId)];
         if (PaidServiceMenu.hasLuckyPack || var0.stats[0] >= var10) {
            var8 = true;
         }

         if (var8) {
            var6 = true;
         }

         if (var6) {
            short var11 = var0.equipIds[var1];
            var5 = var0.equips[var1];
            var0.equipIds[var1] = GameData.equipBagIds[var1][var2];
            var0.equips[var1] = new Equip();
            var0.equips[var1] = GameData.equipBag[var1][var2];
            GameData.equipBagIds[var1] = Utils.removeAt(GameData.equipBagIds[var1], var2);
            GameData.equipBag[var1] = GameData.removeEquipAt(GameData.equipBag[var1], var2);
            if (var11 > 0) {
               GameData.equipBagIds[var1] = Utils.append(GameData.equipBagIds[var1], var11);
               GameData.equipBag[var1] = GameData.appendEquip(GameData.equipBag[var1], var5);
            }

            GameData.refreshRoleStats(var0);
            if (Utils.contains(GameMenu.setItemIds, var0.equipIds[var1])) {
               GameCanvas.instance
                  .world
                  .menu
                  .showToast("Trang bị " + var3[var0.roleId - 1] + " bộ [ " + Equip.names[GameData.findEquipIndex(var0.equipIds[var1])] + " ]", 8);
               if ((var11 <= 0 || Utils.contains(GameMenu.setItemIds, var11)) && var11 > 0) {
                  return;
               }

               GameMenu.setPiecesWorn++;
            } else if (var11 > 0) {
               if (Utils.contains(GameMenu.setItemIds, var11)) {
                  GameMenu.setPiecesWorn--;
               }

               applySetBonus(var0, GameMenu.setPiecesWorn);
               return;
            }

            applySetBonus(var0, GameMenu.setPiecesWorn);
         } else {
            GameCanvas.instance.world.menu.showToast("Cấp không đủ!", 8);
         }
      }
   }

   private static void applySetBonus(RoleSprite var0, int var1) {
      if (var1 >= 2) {
         if (var0.roleId == 1) {
            var0.stats[13] = var0.stats[13] + 300;
         } else if (var0.roleId == 2) {
            var0.stats[10] = var0.stats[10] + 500;
         }
      }

      if (var1 >= 4) {
         if (var0.roleId == 1) {
            var0.stats[11] = var0.stats[11] + 400;
         } else if (var0.roleId == 2) {
            var0.stats[14] = var0.stats[14] + 300;
         }
      }

      if (var1 >= 6) {
         if (var0.roleId == 1) {
            var0.stats[10] = var0.stats[10] + 600;
            return;
         }

         if (var0.roleId == 2) {
            var0.stats[12] = var0.stats[12] + 500;
         }
      }
   }

   private static void handlePetPageKey(int var0) {
      if (GameMenu.pets == null) {
         GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
         GameCanvas.instance.world.menu.resetListCursors();
      } else {
         if (GameMenu.petPage == 0) {
            if (var0 == 8 || var0 == KeyMap.softLeftKey) {
               GameMenu.petActionNames = new String[]{"Thuộc tính", "Kỹ năng", "Xuất chiến", "Thả", "Đổi tên", "Cấy", "Tinh luyện"};
               GameMenu.petActionIds[2] = 2;
               if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].isDeployed) {
                  GameMenu.petActionNames = new String[]{"Thuộc tính", "Kỹ năng", "Nghỉ", "Thả", "Đổi tên", "Cấy", "Tinh luyện"};
                  GameMenu.petActionIds[2] = 3;
               }

               GameMenu.petActionIndex = 0;
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)1);
               return;
            }

            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
               GameCanvas.instance.world.menu.resetListCursors();
               return;
            }

            if (var0 == 1) {
               GameCanvas.instance.world.menu.petList.selectPrev();
               GameCanvas.instance.world.menu.petAnim = new AnimSprite();
               GameCanvas.instance.world.menu.petAnim = GameUtil.loadBattleAnimation(
                  GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].petId, true
               );
               return;
            }

            if (var0 == 6) {
               GameCanvas.instance.world.menu.petList.selectNext();
               GameCanvas.instance.world.menu.petAnim = new AnimSprite();
               GameCanvas.instance.world.menu.petAnim = GameUtil.loadBattleAnimation(
                  GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].petId, true
               );
               return;
            }
         } else if (GameMenu.petPage == 1) {
            if (var0 != 8 && var0 != KeyMap.softLeftKey) {
               if (var0 == KeyMap.softRightKey) {
                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
                  return;
               }

               if (var0 == 1) {
                  if (GameMenu.petActionIndex > 0) {
                     GameMenu.petActionIndex--;
                     return;
                  }
               } else if (var0 == 6 && GameMenu.petActionIndex < GameMenu.petActionNames.length - 1) {
                  GameMenu.petActionIndex++;
                  return;
               }
            } else if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Xuất chiến")) {
               if (GameUtil.a_I() >= 2) {
                  GameMenu.petMessage = "Số sủng vật xuất chiến đã đủ! Thao tác thất bại!";
                  GameCanvas.instance.world.menu.showToast("Số sủng vật xuất chiến đã đủ! Thao tác thất bại!", 8);
                  return;
               }

               GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].isDeployed = true;
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
            } else {
               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Thuộc tính")) {
                  var0 = 1;
                  byte[][] var31 = GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots;
                  String var26 = "Cấy";
                  byte[][] var23 = var31;
                  if (var31 == null) {
                     System.out.println(var26 + " is null!");
                  } else {
                     System.out.println(var26 + "={");

                     for (int var29 = 0; var29 < var23.length; var29++) {
                        var26 = "  {";

                        for (int var30 = 0; var23[var29] != null && var30 < var23[var29].length; var30++) {
                           if (var30 < var23[var29].length - 1) {
                              var26 = var26 + "" + var23[var29][var30] + ",";
                           } else {
                              var26 = var26 + "" + var23[var29][var30];
                           }
                        }

                        var26 = var26 + "}";
                        if (var29 < var23.length - 1) {
                           System.out.println(var26 + ",");
                        } else {
                           System.out.println(var26);
                        }
                     }

                     System.out.println("}");
                     System.out.println("length=" + var23.length);
                  }

                  for (int var24 = 0; var24 < 2; var24++) {
                     if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var24][0] != 0) {
                        var0++;
                     }
                  }

                  GameCanvas.instance.world.menu.geneSlotCount = var0;
                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)2);
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Nghỉ")) {
                  GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].isDeployed = false;
                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Kỹ năng")) {
                  if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].skills == null) {
                     GameCanvas.instance.world.menu.showToast("Chưa học kỹ năng nào", 8);
                     return;
                  }

                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)5);
                  Skill.loadTable();
                  Skill.loadIcons();
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Thả")) {
                  GameMenu.petPage = 3;
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Đổi tên")) {
                  TextInputDialog var10000 = new TextInputDialog();
                  GameMenu var13 = GameCanvas.instance.world.menu;
                  byte var4 = 4;
                  boolean var2 = false;
                  String var3 = "1-4 ký tự";
                  String var25 = "Đổi tên sủng vật";
                  TextInputDialog var22 = var10000;
                  var10000.listener = var13;
                  var22.form = null;
                  var22.textField = null;
                  var22.form = new Form(var25);
                  var22.textField = new TextField(var3, "", 4, 0);
                  var22.form.append(var22.textField);
                  var22.form.addCommand(new Command("OK", 4, 1));
                  var22.form.addCommand(new Command("Trở về", 2, 1));
                  var22.form.setCommandListener(var22);
                  GameCanvas.instance.setDisplayable(var22.form);
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Cấy")) {
                  if (GameData.itemIds == null) {
                     GameData.loadItemTable();
                     GameData.loadItemIcons();
                  }

                  GameCanvas.instance.world.menu.implantMaterials = GameData.getGeneItems();
                  if (GameCanvas.instance.world.menu.implantMaterials == null) {
                     GameCanvas.instance.world.menu.showToast("Tạm không có nguyên liệu để cấy", 8);
                     return;
                  }

                  boolean var12 = true;

                  for (int var21 = 0; var21 < 2; var21++) {
                     if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var21][0] == 0) {
                        var12 = false;
                     }
                  }

                  if (var12) {
                     GameCanvas.instance.world.menu.showToast("Số lần cấy sủng vật đã đạt giới hạn", 8);
                     return;
                  }

                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)7);
                  return;
               }

               if (GameMenu.petActionNames[GameMenu.petActionIndex].equals("Tinh luyện")) {
                  GameMenu.petPage = 8;
                  return;
               }
            }
         } else if (GameMenu.petPage == 2) {
            if (var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)1);
               return;
            }

            if (var0 == 5) {
               if (GameCanvas.instance.world.menu.geneIndex < GameCanvas.instance.world.menu.geneSlotCount - 1) {
                  GameCanvas.instance.world.menu.geneIndex++;
                  return;
               }
            } else if (var0 == 2 && GameCanvas.instance.world.menu.geneIndex > 0) {
               GameCanvas.instance.world.menu.geneIndex--;
               return;
            }
         } else if (GameMenu.petPage == 3) {
            if (var0 == 8 || var0 == KeyMap.softLeftKey) {
               GameData.removePetRecord(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].uid);
               GameData.removePet(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].uid);
               GameMenu.pets = null;
               if ((GameMenu.pets = GameUtil.a_APetInfo()) == null) {
                  GameCanvas.instance.world.menu.showToast("Không có sủng vật", 10);
                  GameMenu.menuPage = 1;
                  return;
               }

               System.out.println("Thả");
               GameCanvas.instance.world.menu.showToast("Đã thả thành công!", 10);
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
               return;
            }

            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)1);
               return;
            }
         } else if (GameMenu.petPage == 8) {
            if (var0 != 8 && var0 != KeyMap.softLeftKey) {
               if (var0 == KeyMap.softRightKey) {
                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)1);
                  return;
               }
            } else {
               if (!GameMenu.petData[GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].petId - 1][18].equals("-1")) {
                  GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].refineItemIds = Utils.parseByteArray(
                     GameMenu.petData[GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].petId - 1][18], ","
                  );
               }

               byte var10;
               GameData.addItem(var10 = GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].refineItemIds[0], 1);
               GameData.removePetRecord(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].uid);
               GameData.removePet(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].uid);
               GameMenu.bagItems = null;
               GameCanvas.instance.world.menu.reloadBag();
               GameMenu.pets = null;
               if ((GameMenu.pets = GameUtil.a_APetInfo()) != null) {
                  GameCanvas.instance.world.menu.showToast("Nhận vật phẩm " + GameData.itemNames[GameData.findItemIndex(var10)], 10);
                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
                  GameCanvas.instance.world.menu.buildPetList();
                  Image var20 = GameCanvas.instance.world.menu.cursorArrow;
                  PetListPanel var11 = GameCanvas.instance.world.menu.petList;
                  GameCanvas.instance.world.menu.petList.cursorImg = var20;
                  return;
               }

               GameCanvas.instance.world.menu.showToast("Nhận vật phẩm " + GameData.itemNames[GameData.findItemIndex(var10)], 10);
               GameMenu.menuPage = 1;
               GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
               GameCanvas.instance.world.menu.buildPetList();
            }
         } else if (GameMenu.petPage == 4) {
            if (var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
               return;
            }
         } else if (GameMenu.petPage == 5) {
            if (GameMenu.petSkillPage == 0) {
               if (var0 == KeyMap.softRightKey) {
                  GameMenu.petPage = 0;
                  if (GameCanvas.instance.world.menu.petList != null) {
                     Image var19 = GameCanvas.instance.world.menu.cursorArrow;
                     PetListPanel var9 = GameCanvas.instance.world.menu.petList;
                     GameCanvas.instance.world.menu.petList.cursorImg = var19;
                     return;
                  }
               } else {
                  if (var0 == KeyMap.softLeftKey || var0 == 8) {
                     GameMenu.petSkillPage = 1;
                     return;
                  }

                  if (var0 == 1) {
                     GameCanvas.instance.world.menu.petSkillList.selectPrev();
                     return;
                  }

                  if (var0 == 6) {
                     GameCanvas.instance.world.menu.petSkillList.selectNext();
                     return;
                  }
               }
            } else if (GameMenu.petSkillPage == 1 && (var0 == 8 || var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey)) {
               GameMenu.petSkillPage = 0;
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
               return;
            }
         } else if (GameMenu.petPage == 7) {
            if (GameCanvas.instance.world.menu.implantStep == 0) {
               if (var0 == 8) {
                  if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][0] != 0) {
                     GameCanvas.instance.world.menu.showToast("Đã cấy ở đây ", 8);
                     return;
                  }

                  GameCanvas.instance.world.menu.implantStep = 1;
                  return;
               }

               if (var0 == 2 || var0 == 5) {
                  if (GameCanvas.instance.world.menu.implantSlot == 1) {
                     GameCanvas.instance.world.menu.implantSlot = 0;
                     return;
                  }

                  GameCanvas.instance.world.menu.implantSlot = 1;
                  return;
               }

               if (var0 == KeyMap.softLeftKey) {
                  for (int var8 = 0; var8 < 2; var8++) {
                     if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var8][0] != 1
                        && GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var8][1] != 0) {
                        GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var8][0] = 1;
                        int var18 = GameData.findItemIndex(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var8][1]);
                        GameData.applyPetStatBonus(GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()], GameData.itemStatBonus[var18]);
                        GameData.removeItem(var18 + 1, 1);
                        if (var8 == 0) {
                           GameCanvas.instance.world.menu.showImplantFxLeft = true;
                        } else {
                           GameCanvas.instance.world.menu.showImplantFxRight = true;
                        }
                     }
                  }

                  GameCanvas.instance.world.menu.implantMaterials = null;
                  GameCanvas.instance.world.menu.implantMaterials = GameData.getGeneItems();
                  return;
               }

               if (var0 == KeyMap.softRightKey) {
                  for (int var7 = 0; var7 < 2; var7++) {
                     if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var7][0] == 0) {
                        GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[var7][1] = 0;
                     }
                  }

                  GameCanvas.instance.world.menu.showPage((byte)3, (byte)0);
                  return;
               }
            } else if (GameCanvas.instance.world.menu.implantStep == 1) {
               if (var0 != 8 && var0 != KeyMap.softLeftKey) {
                  if (var0 == KeyMap.softRightKey) {
                     GameCanvas.instance.world.menu.implantStep = 0;
                     return;
                  }

                  if (var0 == 5) {
                     if (GameCanvas.instance.world.menu.implantMaterialIndex < 17) {
                        GameCanvas.instance.world.menu.implantMaterialIndex++;
                     }

                     if (GameCanvas.instance.world.menu.implantMaterialIndex - GameCanvas.instance.world.menu.implantScrollRow * GameMenu.gridCols
                        >= GameMenu.gridCols * GameMenu.gridRows) {
                        GameCanvas.instance.world.menu.implantScrollRow++;
                        return;
                     }
                  } else if (var0 == 2) {
                     if (GameCanvas.instance.world.menu.implantScrollRow > 0
                        && GameCanvas.instance.world.menu.implantMaterialIndex == GameCanvas.instance.world.menu.implantScrollRow * GameMenu.gridCols) {
                        GameCanvas.instance.world.menu.implantScrollRow--;
                     }

                     if (GameCanvas.instance.world.menu.implantMaterialIndex > 0) {
                        GameCanvas.instance.world.menu.implantMaterialIndex--;
                        return;
                     }
                  } else if (var0 == 6) {
                     if (GameCanvas.instance.world.menu.implantMaterialIndex + GameMenu.gridCols <= 17) {
                        GameCanvas.instance.world.menu.implantMaterialIndex = GameCanvas.instance.world.menu.implantMaterialIndex + GameMenu.gridCols;
                     } else if (GameCanvas.instance.world.menu.implantMaterialIndex < 17) {
                        GameCanvas.instance.world.menu.implantMaterialIndex++;
                     }

                     if (GameCanvas.instance.world.menu.implantMaterialIndex - GameCanvas.instance.world.menu.implantScrollRow * GameMenu.gridCols
                        >= GameMenu.gridCols * GameMenu.gridRows) {
                        GameCanvas.instance.world.menu.implantScrollRow++;
                        return;
                     }
                  } else if (var0 == 1) {
                     if (GameCanvas.instance.world.menu.implantMaterialIndex - GameMenu.gridCols >= 0) {
                        GameCanvas.instance.world.menu.implantMaterialIndex = GameCanvas.instance.world.menu.implantMaterialIndex - GameMenu.gridCols;
                     } else if (GameCanvas.instance.world.menu.implantMaterialIndex > 0) {
                        GameCanvas.instance.world.menu.implantMaterialIndex--;
                     }

                     if (GameCanvas.instance.world.menu.implantScrollRow > 0
                        && GameCanvas.instance.world.menu.implantMaterialIndex < GameCanvas.instance.world.menu.implantScrollRow * GameMenu.gridCols) {
                        GameCanvas.instance.world.menu.implantScrollRow--;
                        return;
                     }
                  }
               } else {
                  if (GameCanvas.instance.world.menu.implantMaterials == null
                     || GameCanvas.instance.world.menu.implantMaterialIndex >= GameCanvas.instance.world.menu.implantMaterials.length) {
                     if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                        != 0) {
                        var0 = -1;
                        if (GameCanvas.instance.world.menu.implantMaterials != null) {
                           for (int var16 = 0; var16 < GameCanvas.instance.world.menu.implantMaterials.length; var16++) {
                              if (GameCanvas.instance.world.menu.implantMaterials[var16][0]
                                 == GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                                 )
                               {
                                 var0 = var16;
                                 break;
                              }
                           }
                        }

                        if (var0 != -1) {
                           GameCanvas.instance.world.menu.implantMaterials[var0][1]++;
                        } else {
                           byte[] var17 = Utils.append(
                              Utils.append(
                                 null,
                                 GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                              ),
                              1
                           );
                           GameCanvas.instance.world.menu.implantMaterials = Utils.append(GameCanvas.instance.world.menu.implantMaterials, var17);
                        }
                     }

                     GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1] = 0;
                     return;
                  }

                  if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1] == 0) {
                     GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1] = GameCanvas.instance
                        .world
                        .menu
                        .implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][0];
                     if (GameCanvas.instance.world.menu.implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][1] > 1) {
                        GameCanvas.instance.world.menu.implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][1]--;
                        return;
                     }

                     GameCanvas.instance.world.menu.implantMaterials = Utils.removeAt(
                        GameCanvas.instance.world.menu.implantMaterials, GameCanvas.instance.world.menu.implantMaterialIndex
                     );
                     return;
                  }

                  if (GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                     != GameCanvas.instance.world.menu.implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][0]) {
                     var0 = -1;

                     for (int var1 = 0; var1 < GameCanvas.instance.world.menu.implantMaterials.length; var1++) {
                        if (GameCanvas.instance.world.menu.implantMaterials[var1][0]
                           == GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                           )
                         {
                           var0 = var1;
                           break;
                        }
                     }

                     if (var0 != -1) {
                        GameCanvas.instance.world.menu.implantMaterials[var0][1]++;
                     } else {
                        byte[] var15 = Utils.append(
                           Utils.append(
                              null,
                              GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1]
                           ),
                           1
                        );
                        GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1] = GameCanvas.instance
                           .world
                           .menu
                           .implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][1];
                        GameCanvas.instance.world.menu.implantMaterials = Utils.append(GameCanvas.instance.world.menu.implantMaterials, var15);
                     }

                     GameMenu.pets[GameCanvas.instance.world.menu.petList.getSelectedSlot()].implantSlots[GameCanvas.instance.world.menu.implantSlot][1] = GameCanvas.instance
                        .world
                        .menu
                        .implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][0];
                     if (GameCanvas.instance.world.menu.implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][1] <= 1) {
                        GameCanvas.instance.world.menu.implantMaterials = Utils.removeAt(
                           GameCanvas.instance.world.menu.implantMaterials, GameCanvas.instance.world.menu.implantMaterialIndex
                        );
                        return;
                     }

                     GameCanvas.instance.world.menu.implantMaterials[GameCanvas.instance.world.menu.implantMaterialIndex][1]--;
                  }
               }
            } else if (GameCanvas.instance.world.menu.implantStep == 2 && (var0 == 8 || var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey)) {
               GameCanvas.instance.world.menu.resetImplant();
               GameCanvas.instance.world.menu.showPage((byte)3, (byte)1);
            }
         }
      }
   }

   private static void loadSaveSlotInfo() {
      GameMenu.saveSlotIndex = 0;
      String[] var0 = RecordStore.listRecordStores();

      for (int var1 = 0; var0 != null && var1 < var0.length; var1++) {
         if (var0[var1].equals(GameData.saveIndexStore)) {
            GameData.hasSaveIndex = true;
            GameData.loadSlotSummaries();
         }
      }

      if (GameData.hasSaveIndex) {
         GameMenu.saveSlotAnims = new AnimSprite[GameData.slotHeroIds.length];

         for (int var2 = 0; var2 < GameMenu.saveSlotAnims.length; var2++) {
            GameMenu.saveSlotAnims[var2] = new AnimSprite();
            GameMenu.saveSlotAnims[var2].load("/av/chr" + GameData.slotHeroIds[var2] + ".av");
         }
      }
   }

   private static void handleSystemPageKey(int var0) {
      if (GameMenu.systemPage == 6) {
         if (var0 == KeyMap.softRightKey) {
            GameCanvas.instance.world.menu.showPage((byte)9, (byte)0);
            return;
         }

         if (var0 == 8 || var0 == KeyMap.softLeftKey) {
            GameMenu.systemPage = 0;
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
            QQCommunityOverlay.uploadScore(GameData.score);
            GameCanvas.instance.world.menu.systemMenuList.setCursor(GameCanvas.instance.world.menu.cursorArrow, (DeviceConfig.screenSize[0] - 120) / 2);
            return;
         }
      } else if (GameMenu.systemPage == 0) {
         if (var0 == KeyMap.softRightKey) {
            GameCanvas.instance.world.menu.showPage((byte)1, (byte)0);
            GameCanvas.instance.world.menu.resetListCursors();
            return;
         }

         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == 1) {
               GameCanvas.instance.world.menu.systemMenuList.selectPrev();
               return;
            }

            if (var0 == 6) {
               GameCanvas.instance.world.menu.systemMenuList.selectNext();
               return;
            }
         } else {
            var0 = GameCanvas.instance.world.menu.systemMenuList.getSelectedRow();
            if (GameMenu.systemMenuRows[var0].equals("Dịch vụ ưu đãi")) {
               GameCanvas.instance.world.menu.releaseData();
               if (!DeviceConfig.modelName.equals("N73")
                  && !DeviceConfig.modelName.equals("N7610")
                  && !DeviceConfig.modelName.equals("N5800")
                  && !DeviceConfig.modelName.equals("E62")
                  && !DeviceConfig.modelName.equals("W958C")
                  && GameCanvas.instance.world.menu != null) {
                  GameCanvas.instance.world.menu.releaseImages();
               }

               GameCanvas.instance.world.menu = null;
               GameMenu.hero = null;
               GameUtil.clearPanelImages();
               PaidServiceMenu.open("/bin/smsmenu.bin");
            } else if (GameMenu.systemMenuRows[var0].equals("Lưu game")) {
               GameCanvas.instance.world.menu.showPage((byte)9, (byte)1);
               loadSaveSlotInfo();
            } else if (GameMenu.systemMenuRows[var0].equals("Tải game")) {
               GameCanvas.instance.world.menu.showPage((byte)9, (byte)2);
               loadSaveSlotInfo();
            } else if (GameMenu.systemMenuRows[var0].equals("Cài đặt nhạc")) {
               GameCanvas.instance.world.menu.showPage((byte)9, (byte)3);
            } else {
               if (!GameMenu.systemMenuRows[var0].equals("Thoát game")) {
                  if (GameMenu.systemMenuRows[var0].equals("Tải điểm QQ")) {
                     GameCanvas.instance.world.menu.resetListCursors();
                     GameMenu.systemPage = 6;
                  }

                  return;
               }

               GameMenu.systemPage = 4;
            }
         }
      } else if (GameMenu.systemPage == 1) {
         if (var0 == 8 || var0 == KeyMap.softLeftKey) {
            if (GameData.currentSceneId != 67) {
               if (GameData.slotUsed[GameMenu.saveSlotIndex] == 1) {
                  GameMenu.saveConfirmMode = 1;
                  GameMenu.confirmText = "Xác nhận ghi đè bản lưu hiện tại?";
                  GameMenu.systemPage = 5;
                  return;
               }

               GameData.hasSaveIndex = true;
               GameData.saveSlotSummary(GameMenu.saveSlotIndex);
               GameData.saveGame(GameMenu.saveSlotIndex);
               if (GameData.hasSaveIndex) {
                  GameMenu.saveSlotAnims = new AnimSprite[GameData.slotHeroIds.length];

                  for (int var3 = 0; var3 < GameMenu.saveSlotAnims.length; var3++) {
                     GameMenu.saveSlotAnims[var3] = new AnimSprite();
                     GameMenu.saveSlotAnims[var3].load("/av/chr" + GameData.slotHeroIds[var3] + ".av");
                  }
               }

               GameMenu.systemPage = 1;
               return;
            }

            GameCanvas.instance.world.menu.showToast("Không thể lưu ở cảnh này", 10);
            return;
         }

         if (var0 == KeyMap.softRightKey) {
            GameCanvas.instance.world.menu.showPage((byte)9, (byte)0);
            return;
         }

         if (var0 == 6) {
            if (GameMenu.saveSlotIndex < DeviceConfig.rmsCount - 1) {
               GameMenu.saveSlotIndex++;
               return;
            }
         } else if (var0 == 1 && GameMenu.saveSlotIndex > 0) {
            GameMenu.saveSlotIndex--;
            return;
         }
      } else if (GameMenu.systemPage == 2) {
         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)9, (byte)0);
               return;
            }

            if (var0 == 6) {
               if (GameMenu.saveSlotIndex < DeviceConfig.rmsCount - 1) {
                  GameMenu.saveSlotIndex++;
                  return;
               }
            } else if (var0 == 1 && GameMenu.saveSlotIndex > 0) {
               GameMenu.saveSlotIndex--;
               return;
            }
         } else if (GameData.slotUsed[GameMenu.saveSlotIndex] == 1) {
            GameMenu.saveConfirmMode = 2;
            GameMenu.systemPage = 5;
            GameMenu.confirmText = "Xác nhận tải bản lưu hiện tại?";
            return;
         }
      } else if (GameMenu.systemPage == 4) {
         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == KeyMap.softRightKey) {
               GameCanvas.instance.world.menu.showPage((byte)9, (byte)0);
               return;
            }
         } else {
            GameCanvas.instance.world.mode = 0;
            GameCanvas.instance.world.menu = null;

            try {
               Thread.sleep(300L);
            } catch (InterruptedException var1) {
               var1.printStackTrace();
            }

            if (GameCanvas.instance.world != null) {
               World.stopMusic();
            }

            Main.self.showCover();
            GameData.resetGameData();
            if (DeviceConfig.playMusic) {
               Main.self.playMusic(GameSettings.titleMusic, -1, GameData.musicVolume);
               return;
            }
         }
      } else if (GameMenu.systemPage == 3) {
         if (var0 == 8 || var0 == KeyMap.softLeftKey || var0 == KeyMap.softRightKey) {
            GameCanvas.instance.world.menu.showPage((byte)9, (byte)0);
         }

         if (var0 == 5) {
            if (!DeviceConfig.playMusic) {
               DeviceConfig.playMusic = true;
            }

            if (GameData.musicVolume < 100) {
               GameData.musicVolume = (byte)(GameData.musicVolume + 10);
            }

            if (GameData.musicVolume >= 100) {
               GameData.musicVolume = 100;
            }

            if (GameCanvas.instance.world.menu.speakerAnim != null) {
               GameCanvas.instance.world.menu.speakerAnim.setAction(1);
            }

            if (Main.music != null) {
               Main.music.setVolume(GameData.musicVolume);
            } else {
               Main.self.playMusic(GameSettings.titleMusic, -1, GameData.musicVolume);
            }
         }

         if (var0 == 2) {
            if (GameData.musicVolume > 0) {
               GameData.musicVolume = (byte)(GameData.musicVolume - 10);
            }

            if (GameData.musicVolume <= 0) {
               GameData.musicVolume = 0;
               if (GameCanvas.instance.world.menu.speakerAnim != null) {
                  GameCanvas.instance.world.menu.speakerAnim.setAction(0);
               }
            }

            Main.music.setVolume(GameData.musicVolume);
            return;
         }
      } else if (GameMenu.systemPage == 5) {
         if (var0 != 8 && var0 != KeyMap.softLeftKey) {
            if (var0 == KeyMap.softRightKey) {
               GameMenu.systemPage = GameMenu.saveConfirmMode;
               GameMenu.saveConfirmMode = 0;
            }
         } else {
            if (GameMenu.saveConfirmMode != 2) {
               saveToSelectedSlot();
               GameMenu.saveConfirmMode = 0;
               GameMenu.systemPage = 1;
               GameCanvas.instance.world.menu.showToast("Lưu thành công", 8);
               return;
            }

            GameMenu.saveConfirmMode = 0;
            if (GameData.slotUsed[GameMenu.saveSlotIndex] == 1) {
               String[] var2 = new String[]{"Tiến độ 1", "Tiến độ 2"};
               GameCanvas.instance.world.mode = 0;
               closeMenu();
               GameCanvas.instance.world.menu = null;
               GameCanvas.instance.world.currentMapName = null;
               GameData.resetGameData();
               GameCanvas.instance.world.loadSaveSlot(var2[GameMenu.saveSlotIndex]);
               return;
            }
         }
      }
   }

   private static boolean saveToSelectedSlot() {
      GameData.hasSaveIndex = true;
      GameData.saveSlotSummary(GameMenu.saveSlotIndex);
      GameData.saveGame(GameMenu.saveSlotIndex);
      if (GameData.hasSaveIndex) {
         GameMenu.saveSlotAnims = new AnimSprite[GameData.slotHeroIds.length];

         for (int var0 = 0; var0 < GameMenu.saveSlotAnims.length; var0++) {
            GameMenu.saveSlotAnims[var0] = new AnimSprite();
            GameMenu.saveSlotAnims[var0].load("/av/chr" + GameData.slotHeroIds[var0] + ".av");
         }
      }

      return true;
   }

   public static boolean closeMenu() {
      GameCanvas.instance.world.menu.releaseData();
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")
         && GameCanvas.instance.world.menu != null) {
         GameCanvas.instance.world.menu.releaseImages();
      }

      GameCanvas.instance.world.menu = null;
      GameMenu.hero = null;
      GameUtil.clearPanelImages();
      if (DeviceConfig.unloadBeforeBattle) {
         GameCanvas.instance.world.reloadOrPlayMusic();
      }

      try {
         Thread.sleep(30L);
      } catch (InterruptedException var1) {
         var1.printStackTrace();
      }

      GameCanvas.instance.world.mode = 1;
      return true;
   }

   public static boolean isInButton(int var0, int var1, int var2, int var3, int var4, int var5) {
      boolean var6 = false;
      if (var2 < var0 && var0 < var2 + 30 && var3 < var1 && var1 < var3 + 30) {
         var6 = true;
      }

      return var6;
   }

   public static void handleMenuPointer(int var0, int var1) {
      int var2 = GameCanvas.instance.world.menu.tabBarBounds[0];
      int var3 = GameCanvas.instance.world.menu.tabBarBounds[1] + GameCanvas.instance.world.menu.tabBarBounds[3] + GameCanvas.instance.world.menu.bodyH;
      int var4 = GameCanvas.instance.world.menu.tabBarBounds[2];
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         if (isInButton(var0, var1, var2, var3, 30, 30)) {
            handleMenuKey(KeyMap.softLeftKey);
         } else if (isInButton(var0, var1, var2 + var4 - 30, var3, 30, 30)) {
            handleMenuKey(KeyMap.softRightKey);
         }
      } else if (isInButton(var0, var1, var2, var3, 30, 30)) {
         handleMenuKey(KeyMap.softRightKey);
      } else if (isInButton(var0, var1, var2 + var4 - 30, var3, 30, 30)) {
         handleMenuKey(KeyMap.softLeftKey);
      }

      if (GameMenu.menuPage == 8 && GameMenu.dexPage == 0) {
         if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            if (isInButton(var0, var1, var2, var3 - GameCanvas.instance.world.menu.footerH - Utils.lineHeight, 30, 30)) {
               GameCanvas.instance.world.menu.b_I--;
               GameCanvas.instance.world.menu.b_I = (GameCanvas.instance.world.menu.b_I + 3) % 3;
               return;
            }

            if (isInButton(var0, var1, var2 + var4 - 30, var3 - GameCanvas.instance.world.menu.footerH - Utils.lineHeight, 30, 30)) {
               GameCanvas.instance.world.menu.b_I++;
               GameCanvas.instance.world.menu.b_I %= 3;
            }
         } else {
            if (isInButton(var0, var1, var2, var3 - GameCanvas.instance.world.menu.footerH - Utils.lineHeight, 30, 30)) {
               GameCanvas.instance.world.menu.b_I++;
               GameCanvas.instance.world.menu.b_I %= 3;
               return;
            }

            if (isInButton(var0, var1, var2 + var4 - 30, var3 - GameCanvas.instance.world.menu.footerH - Utils.lineHeight, 30, 30)) {
               GameCanvas.instance.world.menu.b_I--;
               GameCanvas.instance.world.menu.b_I = (GameCanvas.instance.world.menu.b_I + 3) % 3;
               return;
            }
         }
      }
   }

   public MenuList(int var1, int var2, int var3, int var4) {
      this.cursorBob = 0;
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
      this.columns = new Vector();
   }

   public void setCursor(Image var1, int var2) {
      this.cursorImage = var1;
      this.cursorX = var2;
   }

   public int getRowId(int var1) {
      return this.rowIds[var1];
   }

   public int getColumnCount() {
      return this.columns != null ? this.columns.size() : 0;
   }

   public int getSelectedRow() {
      return ((ListPanel)this.columns.elementAt(0)).getSelectedIndex();
   }

   public void addColumn(ListPanel var1) {
      this.columns.addElement(var1);
   }

   public void clearColumns() {
      for (int var1 = 0; var1 < this.columns.size(); var1++) {
         ((ListPanel)this.columns.elementAt(var1)).clear();
      }

      this.columns.removeAllElements();
   }

   public void selectPrev() {
      for (int var1 = 0; var1 < this.columns.size(); var1++) {
         ((ListPanel)this.columns.elementAt(var1)).selectPrev();
      }
   }

   public void selectNext() {
      for (int var1 = 0; var1 < this.columns.size(); var1++) {
         ((ListPanel)this.columns.elementAt(var1)).selectNext();
      }
   }

   public void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      if (this.columns != null && this.columns.size() > 0) {
         if (((ListPanel)this.columns.elementAt(0)).items.size() > ((ListPanel)this.columns.elementAt(0)).visibleRows) {
            GameUtil.drawScrollBar(
               var1,
               null,
               null,
               this.x + this.w - 15,
               this.y + 5,
               this.h - 10,
               ((ListPanel)this.columns.elementAt(0)).items.size(),
               ((ListPanel)this.columns.elementAt(0)).visibleRows,
               ((ListPanel)this.columns.elementAt(0)).scrollTop
            );
         }

         for (int var2 = 0; var2 < this.columns.size(); var2++) {
            ((ListPanel)this.columns.elementAt(var2)).draw(var1);
         }

         int var5 = this.y + (((ListPanel)this.columns.elementAt(0)).rowHeight >> 1);
         int var3;
         if ((var3 = ((ListPanel)this.columns.elementAt(0)).scrollTop + ((ListPanel)this.columns.elementAt(0)).visibleRows)
            > ((ListPanel)this.columns.elementAt(0)).items.size()) {
            var3 = ((ListPanel)this.columns.elementAt(0)).items.size();
         }

         for (int var4 = ((ListPanel)this.columns.elementAt(0)).scrollTop; var4 < var3; var4++) {
            if (var4 == this.getSelectedRow() && this.cursorImage != null) {
               var1.drawRegion(this.cursorImage, 0, 0, 15, 15, 0, this.cursorX + 8 + this.cursorBob, var5, 3);
            }

            var5 += ((ListPanel)this.columns.elementAt(0)).rowHeight;
         }
      }
   }

   void resetSelection(int var1) {
      for (int var2 = 0; var2 < this.columns.size(); var2++) {
         ((ListPanel)this.columns.elementAt(var2)).setSelectedIndex(0);
      }
   }

   void scrollToTop() {
      for (int var1 = 0; var1 < this.columns.size(); var1++) {
         ((ListPanel)this.columns.elementAt(var1)).resetScroll(0);
      }
   }
}
