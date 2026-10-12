import java.util.Vector;

public final class ScriptEngine {
   public boolean isRunning;
   public ScriptEvent[] events;
   private ScriptEvent lastAreaEvent;
   private ScriptEvent areaEventHit;
   ScriptEvent currentEvent;
   private boolean allowRetrigger;
   public boolean hasManualEvent;
   ScriptEvent manualEvent;
   public byte battleResult;
   private short continuousLeft;
   private short continuousPending;
   private boolean lastCondition;
   private byte lineCondition;
   public boolean isMustLoseBattle;
   public boolean isExecuting;
   private short lineIndex;
   private short lastExecutedLine = -1;
   private boolean inComment;
   private short[] blockConditions;
   private short blockDepth;
   private String[] a_AString;
   public static int randomValue;
   private short randomQuestId;
   private short[][] randomQuestPools = new short[][]{{37, 38, 39, 40, 41, 42, 43, 44}, {40, 41, 42, 43, 44, 45, 46, 47}, {42, 43, 44, 45, 46, 47, 48, 49, 50}};

   public ScriptEngine() {
   }

   private void b() {
      if (this.a_AString != null) {
         short var1 = -1;

         for (int var2 = this.a_AString.length - 1; var2 > 0; var2 -= 2) {
            if (Utils.parseInt(this.a_AString[var2 - 1]) == this.blockDepth) {
               var1 = (short)(var2 - 1);
            }
         }

         if (var1 > 0) {
            String[] var3 = new String[var1];
            System.arraycopy(this.a_AString, 0, var3, 0, var3.length);
            this.a_AString = var3;
         } else {
            if (var1 == 0) {
               this.a_AString = null;
            }
         }
      }
   }

   private void pushBlockCondition(boolean var1) {
      if (this.blockConditions == null) {
         this.blockConditions = new short[2];
         this.blockConditions[0] = this.blockDepth;
         this.blockConditions[1] = (short)(var1 ? 1 : 0);
      } else {
         for (byte var2 = 0; var2 < this.blockConditions.length; var2 += 2) {
            if (this.blockConditions[var2] == this.blockDepth) {
               this.blockConditions[var2 + 1] = (short)(var1 ? 1 : 0);
               return;
            }
         }

         short[] var3 = new short[this.blockConditions.length + 2];
         System.arraycopy(this.blockConditions, 0, var3, 0, this.blockConditions.length);
         this.blockConditions = var3;
         var3[var3.length - 2] = this.blockDepth;
         var3[var3.length - 1] = (short)(var1 ? 1 : 0);
      }
   }

   private void popBlockCondition() {
      if (this.blockConditions != null) {
         short var1 = -1;

         for (int var2 = this.blockConditions.length - 1; var2 > 0; var2 -= 2) {
            if (this.blockConditions[var2 - 1] == this.blockDepth) {
               var1 = (short)(var2 - 1);
            }
         }

         if (var1 > 0) {
            short[] var3 = new short[var1];
            System.arraycopy(this.blockConditions, 0, var3, 0, var3.length);
            this.blockConditions = var3;
         } else {
            if (var1 == 0) {
               this.blockConditions = null;
            }
         }
      }
   }

   private boolean isBlockActive() {
      for (byte var1 = 0; this.blockConditions != null && var1 < this.blockConditions.length - 1; var1 += 2) {
         if (this.blockConditions[var1] <= this.blockDepth && this.blockConditions[var1 + 1] == 0) {
            return false;
         }
      }

      return true;
   }

   public final void loadEvents(String var1, int var2) {
      this.events = null;
      var1 = Utils.loadConfigText(var1);
      Vector var3 = new Vector();
      short var4 = Utils.getShort(var1, "eventAmount");

      for (int var5 = 0; var1 != null && var5 < var4; var5++) {
         String var6;
         if ((var6 = Utils.substringBetween(var1, "event" + (var5 + 1) + ":", "event" + (var5 + 1) + "End")) != null) {
            short var7 = Utils.getShort(var6, "id");
            Utils.getByte(var6, "type");
            String var8 = Utils.getValue(var6, "name");
            short[] var9 = Utils.getShortArray(var6, "area");
            boolean var10 = Utils.getBool(var6, "auto");
            boolean var11 = Utils.getBool(var6, "loop");
            String[][] var12 = Utils.getStringTable(var6, "script:", "scriptEnd", null);
            int[] var19 = Utils.getIntArray(var6, "phase");
            if (!GameData.hasEventFlag(var7) && (var19 == null || Utils.contains(var19, var2)) && var9 != null) {
               ScriptEvent var20;
               (var20 = new ScriptEvent()).id = var7;
               var20.name = var8;
               var20.area = var9;
               var20.isAuto = var10;
               var20.isLoop = var11;
               var20.scriptLines = var12;
               var3.addElement(var20);
            }
         }
      }

      if (GameData.dungeonBosses != null && GameData.currentSceneId == 67) {
         GameData.removeEventFlag(6701);
         ScriptEvent var15;
         (var15 = new ScriptEvent()).id = 6701;
         var15.name = "副本战";
         var15.area = new short[]{(short)(GameData.dungeonBossPos[0] - 80), (short)(GameData.dungeonBossPos[1] - 24), 160, 48};
         var15.isAuto = true;
         var15.isLoop = false;
         String[] var21 = new String[]{"副本战斗"};

         for (int var25 = 0; GameData.dungeonBosses != null && var25 < GameData.dungeonBosses.length; var25++) {
            if (GameData.dungeonBosses[var25][1] == -1) {
               var21 = Utils.append(var21, GameData.dungeonBosses[var25][0] + "|" + (GameData.party[0].stats[0] + 5) + "|1");
            } else {
               var21 = Utils.append(var21, GameData.dungeonBosses[var25][0] + "|" + GameData.dungeonBosses[var25][1] + "|1");
            }
         }

         var15.scriptLines = new String[][]{{"对话", "Đã vào đây rồi thì ở lại luôn đi!"}, var21};
         var3.addElement(var15);
      }

      if (GameData.mazeGatePoints != null) {
         if (GameData.dungeonGateMode == 1) {
            short[] var16 = Utils.parseShortArray(GameData.mazeGatePoints[1], ",");
            ScriptEvent var22;
            (var22 = new ScriptEvent()).name = "换场景2";
            var22.area = new short[]{(short)(var16[0] - 10), (short)(var16[1] - 10), 20, 20};
            var22.isAuto = true;
            var22.isLoop = false;
            if (GameData.dungeonFloor >= GameData.dungeonFloorCount) {
               var22.scriptLines = new String[][]{{"换场景", GameData.dungeonLinkScenes[1], "1"}};
            } else {
               var22.scriptLines = new String[][]{{"换场景", String.valueOf(GameData.currentSceneId), "1"}};
            }

            var3.addElement(var22);
         } else {
            for (int var23 = 0; var23 < GameData.mazeGatePoints.length; var23++) {
               short[] var17 = Utils.parseShortArray(GameData.mazeGatePoints[var23], ",");
               ScriptEvent var26;
               (var26 = new ScriptEvent()).name = "换场景" + (var23 + 1);
               var26.area = new short[]{(short)(var17[0] - 10), (short)(var17[1] - 10), 20, 20};
               var26.isAuto = true;
               var26.isLoop = true;
               if (GameData.dungeonFloor >= GameData.dungeonFloorCount && var23 == 1) {
                  var26.scriptLines = new String[][]{{"换场景", GameData.dungeonLinkScenes[1], "1"}};
               } else if (GameData.dungeonFloor == 1 && var23 == 0) {
                  var26.scriptLines = new String[][]{{"换场景", GameData.dungeonLinkScenes[0], "1"}};
               } else {
                  if (var23 == 0) {
                     var26.scriptLines = new String[][]{{"换场景2", String.valueOf(GameData.currentSceneId), "1"}};
                  }

                  if (var23 == 1) {
                     var26.scriptLines = new String[][]{{"换场景", String.valueOf(GameData.currentSceneId), "1"}};
                  }
               }

               var3.addElement(var26);
            }
         }
      }

      if (GameData.sceneChangeText == null) {
         GameData.sceneChangeText = Utils.loadConfigText("/bin/sceneChange.bin");
      }

      short[][] var18;
      if ((var18 = Utils.getShortTable(GameData.sceneChangeText, "S" + GameData.currentSceneId + "A:", "S" + GameData.currentSceneId + "AEnd", "=")) != null) {
         short[][] var24 = Utils.getShortTable(GameData.sceneChangeText, "Connect:", "ConnectEnd", "=");
         int var27 = -1;

         for (int var14 = 0; var14 < var18.length; var14++) {
            if (var27 < 0) {
               for (int var28 = 0; var28 < var24.length; var28++) {
                  if (var24[var28][0] == GameData.currentSceneId) {
                     var27 = var28;
                     break;
                  }
               }
            }

            ScriptEvent var29;
            (var29 = new ScriptEvent()).name = "换场景" + (var14 + 1);
            var29.area = var18[var14];
            var29.isAuto = true;
            var29.isLoop = true;
            var29.scriptLines = new String[][]{{"换场景", String.valueOf(var24[var27 + var14][2]), String.valueOf(var24[var27 + var14][3])}};
            var3.addElement(var29);
         }
      }

      if (var3.size() > 0) {
         this.events = new ScriptEvent[var3.size()];
         var3.copyInto(this.events);
      }
   }

   public final void checkAutoEvents(short[] var1) {
      if (!this.isRunning) {
         if (GameCanvas.instance.world.mode == 1 && !GameCanvas.instance.world.screenWipe.isActive() && GameData.party[0] != null) {
            ScriptEvent var10001;
            short[] var2 = var1;
            ScriptEngine var4 = this;
            label64:
            if (!this.isRunning && var1 != null) {
               for (int var3 = 0; var4.events != null && var3 < var4.events.length; var3++) {
                  if (Utils.rectsOverlap(
                        var4.events[var3].area[0],
                        var4.events[var3].area[1],
                        var4.events[var3].area[2],
                        var4.events[var3].area[3],
                        var2[0],
                        var2[1],
                        var2[2],
                        var2[3]
                     )
                     && var4.events[var3].isAuto
                     && !GameData.hasEventFlag(var4.events[var3].id)) {
                     var10001 = var4.events[var3];
                     break label64;
                  }
               }

               var10001 = null;
            } else {
               var10001 = null;
            }

            this.areaEventHit = var10001;
            if (this.areaEventHit != null) {
               if (this.allowRetrigger) {
                  this.lastAreaEvent = this.areaEventHit;
               }

               if (this.lastAreaEvent == null) {
                  this.lastAreaEvent = this.areaEventHit;
                  if (this.lastAreaEvent.isAuto) {
                     if (DeviceConfig.debugMode) {
                        System.out.println("执行事件:" + this.lastAreaEvent.name);
                     }

                     this.triggerAutoEvent(this.lastAreaEvent);
                     return;
                  }
               } else if (this.lastAreaEvent != this.areaEventHit) {
                  this.lastAreaEvent = null;
                  this.lastAreaEvent = this.areaEventHit;
                  if (this.lastAreaEvent.isAuto) {
                     if (DeviceConfig.debugMode) {
                        System.out.println("执行事件:" + this.lastAreaEvent.name);
                     }

                     this.triggerAutoEvent(this.lastAreaEvent);
                     return;
                  }
               }
            } else {
               this.lastAreaEvent = null;
               if (this.allowRetrigger) {
                  this.allowRetrigger = false;
               }
            }
         }
      }
   }

   private void triggerAutoEvent(ScriptEvent var1) {
      if (!this.isRunning) {
         if (var1.isAuto) {
            this.startEvent(var1);
         }
      }
   }

   public final void startEvent(ScriptEvent var1) {
      if (!this.isRunning && GameCanvas.instance.world.battle == null) {
         this.isRunning = true;
         this.currentEvent = var1;
         this.lineIndex = 0;
         this.lastExecutedLine = -1;
         GameCanvas.instance.world.chosenOption = null;
         this.battleResult = -1;

         for (int var2 = 0; var2 < GameData.party.length; var2++) {
            GameData.party[var2].setIdleAnim();
         }

         this.lastCondition = true;
         this.lineCondition = 0;
         this.continuousLeft = 0;
         this.continuousPending = 0;
         this.isExecuting = true;
         GameCanvas.instance.world.playerControlEnabled = false;
      }
   }

   private void endEvent() {
      this.isExecuting = false;
      this.currentEvent = null;
      this.isRunning = false;
      if (GameCanvas.instance.world != null) {
         GameCanvas.instance.world.playerControlEnabled = true;
      }
   }

   public final void step() {
      if (this.isExecuting) {
         for (int var1 = 0; this.currentEvent != null && this.currentEvent.scriptLines != null && var1 < this.currentEvent.scriptLines.length; var1++) {
            if (var1 == this.lineIndex && this.lineIndex > this.lastExecutedLine) {
               this.lastExecutedLine = this.lineIndex;
               if (this.continuousLeft > 0) {
                  this.continuousLeft--;
               }

               if (!this.inComment) {
                  if (!this.currentEvent.scriptLines[var1][0].startsWith("/*")) {
                     this.executeLine(this.currentEvent.scriptLines[var1], 0);
                     continue;
                  }

                  this.inComment = true;
               } else if (this.currentEvent.scriptLines[var1][0].startsWith("*/")) {
                  this.inComment = false;
               }

               this.lineIndex++;
            }
         }

         if (this.currentEvent != null && this.currentEvent.scriptLines != null && this.lineIndex > this.currentEvent.scriptLines.length - 1) {
            if (this.currentEvent != null && !this.currentEvent.isLoop && this.currentEvent.id > 0) {
               GameData.addEventFlag(this.currentEvent.id);
            }

            this.endEvent();
         }
      }
   }

   private void executeLine(String[] var1, int var2) {
      if (!var1[var2].equals("换场景2") && !var1[var2].equals("换场景") && GameData.party[0].isFlying) {
         GameData.party[0].land();
      }

      if (var1[var2].equals("}")) {
         this.popBlockCondition();
         this.b();
         if (this.blockDepth > 0) {
            this.blockDepth--;
         }

         this.advance(0);
      } else if (var1[var2].equals("{")) {
         this.blockDepth++;
         this.pushBlockCondition(this.lastCondition);
         this.advance(0);
      } else if (!this.isBlockActive()) {
         this.advance(0);
      } else {
         if (DeviceConfig.debugMode) {
            System.out.println("执行事件:" + var1[var2]);
         }

         if (var1[var2].equals("对话")) {
            GameCanvas.instance.world.startDialog(var1, var2 + 1);
            this.advance(1);
         } else if (var1[var2].equals("必败战斗")) {
            this.isMustLoseBattle = true;
            GameCanvas.instance.world.startScriptBattle(var1, var2 + 1);
         } else if (var1[var2].equals("教学战斗")) {
            GameCanvas.instance.world.isTutorialBattle = true;
            GameCanvas.instance.world.startScriptBattle(var1, var2 + 1);
         } else if (var1[var2].equals("战斗")) {
            GameCanvas.instance.world.isNormalBattle = true;
            GameCanvas.instance.world.startScriptBattle(var1, var2 + 1);
         } else if (var1[var2].equals("副本战斗")) {
            GameCanvas.instance.world.isDungeonBattle = true;
            GameCanvas.instance.world.startScriptBattle(var1, var2 + 1);
         } else if (var1[var2].equals("清除BOSS") || var1[var2].equals("清除敌人")) {
            GameCanvas.instance.world.removeRole(Utils.parseInt(var1[var2 + 1]));
         } else if (var1[var2].equals("复活角色")) {
            GameCanvas.instance.world.reviveRole(Utils.parseInt(var1[var2 + 1]));
         } else {
            label1768: {
               label1887: {
                  if (var1[var2].equals("增加阶段")) {
                     GameData.storyPhase++;
                  } else if (var1[var2].equals("更新阶段")) {
                     GameData.storyPhase = Utils.parseByte(var1[var2 + 1]);
                  } else if (var1[var2].equals("去除事件")) {
                     GameData.addEventFlag(Utils.parseInt(var1[var2 + 1]));
                  } else if (var1[var2].equals("恢复事件")) {
                     GameData.removeEventFlag(Utils.parseInt(var1[var2 + 1]));
                  } else if (var1[var2].equals("获取道具")) {
                     GameData.addItem(Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                  } else if (var1[var2].equals("获取装备")) {
                     GameData.addEquip(Utils.parseInt(var1[var2 + 1]), -1);
                  } else if (var1[var2].equals("获取技能")) {
                     GameData.learnRoleSkill(GameData.party[0], Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                  } else {
                     if (var1[var2].equals("提示信息")) {
                        GameCanvas.instance.world.messageLines = new String[]{var1[var2 + 1]};
                        GameCanvas.instance.world.messageScroll = 0;
                        GameCanvas.instance.world.mode = 3;
                        return;
                     }

                     if (var1[var2].equals("短信购买")) {
                        if (Utils.parseInt(var1[var2 + 1]) == 99) {
                           if (ResourceCache.loadImage("/pic/order.jpg") != null) {
                              GameCanvas.instance.world.mode = 10;
                              return;
                           }

                           PaidServiceMenu.open("/bin/smsmenu.bin");
                           GameCanvas.instance.world.smsMenu.purchase(6);
                        }

                        return;
                     }

                     if (var1[var2].equals("失败处理")) {
                        GameData.applyDefeatPenalty();
                     } else {
                        if (var1[var2].equals("换场景2") || var1[var2].equals("换场景")) {
                           if (GameData.isInDungeon) {
                              if (var1[var2].equals("换场景2")) {
                                 GameCanvas.instance.world.useSecondEntry = true;
                                 GameData.dungeonFloor--;
                              } else {
                                 GameData.dungeonFloor++;
                              }
                           }

                           GameCanvas.instance.world.playerControlEnabled = true;
                           World.entryPointIndex = (short)Utils.parseByte(var1[var2 + 2]);
                           GameCanvas.instance.world.changeScene(Utils.parseShort(var1[var2 + 1]));
                           return;
                        }

                        if (var1[var2].equals("瞬移")) {
                           short var48 = Utils.parseShort(var1[var2 + 1]);
                           short var75 = Utils.parseShort(var1[var2 + 2]);
                           this.allowRetrigger = true;

                           for (int var92 = 0; GameData.party != null && var92 < GameData.party.length; var92++) {
                              GameData.party[var92].x = var48;
                              if (var92 == 0) {
                                 GameData.party[var92].y = var75;
                              } else {
                                 GameData.party[var92].y = (short)(GameData.party[var92 - 1].y - GameData.party[0].moveSpeed);
                              }

                              GameData.party[var92].setDirection(0);
                              GameData.party[0].initFollowTrail();
                           }

                           this.advance(0);
                           return;
                        }

                        if (var1[var2].equals("转向")) {
                           GameData.party[0].setDirection(Utils.parseInt(var1[var2 + 1]));
                           GameData.party[0].setIdleAnim();
                        } else {
                           if (var1[var2].equals("NPC转向")) {
                              byte var47 = Utils.parseByte(var1[var2 + 1]);
                              byte var74 = Utils.parseByte(var1[var2 + 2]);

                              for (int var91 = 0;
                                 GameCanvas.instance.world.sceneLayer.actors != null && var91 < GameCanvas.instance.world.sceneLayer.actors.length;
                                 var91++
                              ) {
                                 if (GameCanvas.instance.world.sceneLayer.actors[var91] != null
                                    && GameCanvas.instance.world.sceneLayer.actors[var91].kind == 1
                                    && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var91]).roleId == var47) {
                                    ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var91]).setDirection(var74);
                                    ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var91]).setIdleAnim();
                                 }
                              }

                              this.advance(0);
                              return;
                           }

                           if (var1[var2].equals("绘画章节")) {
                              GameCanvas.instance
                                 .world
                                 .startStoryCrawl(
                                    Utils.parseInt(var1[var2 + 1]),
                                    Utils.parseInt(var1[var2 + 2]),
                                    Utils.parseInt(var1[var2 + 3]),
                                    Utils.parseInt(var1[var2 + 4]),
                                    Utils.parseInt(var1[var2 + 5])
                                 );
                              this.advance(1);
                              return;
                           }

                           if (var1[var2].equals("特写动画")) {
                              GameCanvas.instance
                                 .world
                                 .playCloseUpAnim(
                                    String.valueOf(var1[var2 + 1]),
                                    Utils.parseInt(var1[var2 + 2]),
                                    Utils.parseInt(var1[var2 + 3]),
                                    Utils.parseInt(var1[var2 + 4])
                                 );
                              this.advance(1);
                              return;
                           }

                           if (var1[var2].equals("切换焦点")) {
                              GameCanvas.instance.world.startCameraPan(Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                              this.advance(1);
                              return;
                           }

                           if (var1[var2].equals("还原焦点")) {
                              GameCanvas.instance.world.cameraReturning = true;
                              return;
                           }

                           if (var1[var2].equals("走动")) {
                              short var46 = GameData.heroId;
                              int var73 = Utils.parseInt(var1[var2 + 1]);
                              RoleSprite var90;
                              if ((var90 = GameCanvas.instance.world.findSceneRole(var46)) != null) {
                                 if (var73 < 4) {
                                    if (GameData.currentSceneId == 1) {
                                       World.moveRole(var90, var73, 1, Utils.parseInt(var1[var2 + 2]), 0, 0);
                                    } else {
                                       World.moveRole(var90, var73, 3, Utils.parseInt(var1[var2 + 2]), 0, 0);
                                    }
                                 } else if (GameData.currentSceneId == 1) {
                                    World.moveRole(
                                       var90, var73, 1, Utils.parseInt(var1[var2 + 2]), Utils.parseInt(var1[var2 + 3]), Utils.parseInt(var1[var2 + 4])
                                    );
                                 } else {
                                    World.moveRole(
                                       var90, var73, 3, Utils.parseInt(var1[var2 + 2]), Utils.parseInt(var1[var2 + 3]), Utils.parseInt(var1[var2 + 4])
                                    );
                                 }
                              }

                              this.advance(1);
                              return;
                           }

                           if (var1[var2].equals("人物走动")) {
                              int var45 = Utils.parseInt(var1[var2 + 1]);
                              int var72 = Utils.parseInt(var1[var2 + 2]);
                              RoleSprite var89;
                              if ((var89 = GameCanvas.instance.world.findSceneRole(var45)) == null) {
                                 this.advance(2);
                                 return;
                              }

                              if (var72 < 4) {
                                 World.moveRole(var89, var72, 3, Utils.parseInt(var1[var2 + 3]), 0, 0);
                              } else {
                                 World.moveRole(var89, var72, 3, Utils.parseInt(var1[var2 + 3]), Utils.parseInt(var1[var2 + 4]), Utils.parseInt(var1[var2 + 5]));
                              }

                              this.advance(1);
                              return;
                           }

                           if (!var1[var2].equals("战斗胜利") && !var1[var2].equals("胜利动作")) {
                              if (!var1[var2].equals("战斗失败") && !var1[var2].equals("失败动作")) {
                                 if (!var1[var2].equals("战斗逃跑") && !var1[var2].equals("逃跑动作")) {
                                    if (var1[var2].equals("对话中选择")) {
                                       if (GameCanvas.instance.world.dialogBox == null) {
                                          GameCanvas.instance.world.dialogBox = new DialogBox();
                                          GameCanvas.instance.world.dialogBox.computeBoxWidth();
                                       }

                                       GameCanvas.instance.world.dialogBox.choices = new String[var1.length - (var2 + 1)];

                                       for (int var44 = var2 + 1; var44 < var1.length; var44++) {
                                          GameCanvas.instance.world.dialogBox.choices[var44 - (var2 + 1)] = var1[var44];
                                       }

                                       GameCanvas.instance.world.dialogBox.boxMode = DialogBox.modeChoice;
                                       GameCanvas.instance.world.mode = 2;
                                       GameCanvas.instance.world.dialogBox.isVisible = true;
                                       return;
                                    }

                                    if (var1[var2].equals("选择了")) {
                                       if (GameCanvas.instance.world.chosenOption != null && GameCanvas.instance.world.chosenOption.equals(var1[var2 + 1])) {
                                          if (this.lineCondition == 0) {
                                             this.lineCondition = 1;
                                          }

                                          if (var2 + 2 < var1.length) {
                                             this.executeLine(var1, var2 + 2);
                                             return;
                                          }

                                          this.advance(0);
                                          return;
                                       }

                                       this.lineCondition = 2;
                                    } else if (var1[var2].equals("加入任务")) {
                                       GameData.acceptQuest(Utils.parseShort(var1[var2 + 1]));
                                    } else if (var1[var2].equals("移除任务")) {
                                       GameData.finishQuest(Utils.parseShort(var1[var2 + 1]), true);
                                    } else {
                                       if (var1[var2].equals("已发生事件")) {
                                          int var43 = Utils.parseInt(var1[var2 + 1]);
                                          if (!Utils.contains(GameData.eventFlags, var43)) {
                                             this.lineCondition = 2;
                                             this.advance(0);
                                             return;
                                          }

                                          if (this.lineCondition == 0) {
                                             this.lineCondition = 1;
                                          }

                                          if (var2 + 2 < var1.length) {
                                             this.executeLine(var1, var2 + 2);
                                          } else {
                                             this.advance(0);
                                          }

                                          return;
                                       }

                                       if (var1[var2].equals("未发生事件")) {
                                          int var42 = Utils.parseInt(var1[var2 + 1]);
                                          if (Utils.contains(GameData.eventFlags, var42)) {
                                             this.lineCondition = 2;
                                             this.advance(0);
                                             return;
                                          }

                                          if (this.lineCondition == 0) {
                                             this.lineCondition = 1;
                                          }

                                          if (var2 + 2 < var1.length) {
                                             this.executeLine(var1, var2 + 2);
                                          } else {
                                             this.advance(0);
                                          }

                                          return;
                                       }

                                       if (var1[var2].equals("有标志")) {
                                          if (PaidServiceMenu.isFullVersion) {
                                             if (this.lineCondition == 0) {
                                                this.lineCondition = 1;
                                             }

                                             if (var2 + 1 < var1.length) {
                                                this.executeLine(var1, var2 + 1);
                                                return;
                                             }

                                             this.advance(0);
                                             return;
                                          }

                                          this.lineCondition = 2;
                                       } else if (var1[var2].equals("无标志")) {
                                          if (!PaidServiceMenu.isFullVersion) {
                                             if (this.lineCondition == 0) {
                                                this.lineCondition = 1;
                                             }

                                             if (var2 + 1 < var1.length) {
                                                this.executeLine(var1, var2 + 1);
                                                return;
                                             }

                                             this.advance(0);
                                             return;
                                          }

                                          this.lineCondition = 2;
                                       } else if (var1[var2].equals("人物洗点")) {
                                          int var3 = Utils.parseInt(var1[var2 + 1]) - 1;
                                          GameData.party[var3].stats[9] = 0;
                                          GameData.refreshRoleStats(GameData.party[var3]);
                                       } else {
                                          if (var1[var2].equals("宠物数量")) {
                                             String var41 = var1[var2 + 1];
                                             int var71 = 0;

                                             for (int var88 = 0; GameData.myPets != null && var88 < GameData.myPets.length; var88++) {
                                                if (GameData.myPets[var88].ownerHeroId == GameData.heroId) {
                                                   var71++;
                                                }
                                             }

                                             if (var41.equals("大于等于")) {
                                                if (var71 >= GameSettings.maxPetCount) {
                                                   if (this.lineCondition == 0) {
                                                      this.lineCondition = 1;
                                                   }

                                                   if (var2 + 3 < var1.length) {
                                                      this.executeLine(var1, var2 + 3);
                                                   } else {
                                                      this.advance(0);
                                                   }

                                                   return;
                                                } else {
                                                   this.lineCondition = 2;
                                                   this.advance(0);
                                                   return;
                                                }
                                             } else {
                                                if (!var41.equals("小于")) {
                                                   return;
                                                }

                                                if (var71 >= GameSettings.maxPetCount) {
                                                   this.lineCondition = 2;
                                                   this.advance(0);
                                                   return;
                                                }

                                                if (this.lineCondition == 0) {
                                                   this.lineCondition = 1;
                                                }

                                                if (var2 + 3 < var1.length) {
                                                   this.executeLine(var1, var2 + 3);
                                                } else {
                                                   this.advance(0);
                                                }

                                                return;
                                             }
                                          }

                                          if (var1[var2].equals("随机结果")) {
                                             String var40 = var1[var2 + 1];
                                             int var70 = Utils.parseInt(var1[var2 + 2]);
                                             if (var40.equals("大于")) {
                                                if (randomValue > var70) {
                                                   if (this.lineCondition == 0) {
                                                      this.lineCondition = 1;
                                                   }

                                                   if (var2 + 3 < var1.length) {
                                                      this.executeLine(var1, var2 + 3);
                                                   } else {
                                                      this.advance(0);
                                                   }

                                                   return;
                                                } else {
                                                   this.lineCondition = 2;
                                                   this.advance(0);
                                                   return;
                                                }
                                             } else if (var40.equals("小于")) {
                                                if (randomValue < var70) {
                                                   if (this.lineCondition == 0) {
                                                      this.lineCondition = 1;
                                                   }

                                                   if (var2 + 3 < var1.length) {
                                                      this.executeLine(var1, var2 + 3);
                                                   } else {
                                                      this.advance(0);
                                                   }

                                                   return;
                                                } else {
                                                   this.lineCondition = 2;
                                                   this.advance(0);
                                                   return;
                                                }
                                             } else {
                                                if (!var40.equals("等于")) {
                                                   return;
                                                }

                                                if (randomValue != var70) {
                                                   this.lineCondition = 2;
                                                   this.advance(0);
                                                   return;
                                                }

                                                if (this.lineCondition == 0) {
                                                   this.lineCondition = 1;
                                                }

                                                if (var2 + 3 < var1.length) {
                                                   this.executeLine(var1, var2 + 3);
                                                } else {
                                                   this.advance(0);
                                                }

                                                return;
                                             }
                                          }

                                          if (var1[var2].equals("随机数")) {
                                             randomValue = GameUtil.randomRange(Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                                             this.advance(5);
                                             return;
                                          }

                                          if (var1[var2].equals("有道具")) {
                                             if (GameData.getItemCount(Utils.parseInt(var1[var2 + 1])) > 0) {
                                                if (this.lineCondition == 0) {
                                                   this.lineCondition = 1;
                                                }

                                                if (var2 + 2 < var1.length) {
                                                   this.executeLine(var1, var2 + 2);
                                                   return;
                                                }

                                                this.advance(0);
                                                return;
                                             }

                                             this.lineCondition = 2;
                                          } else if (var1[var2].equals("无道具")) {
                                             if (GameData.getItemCount(Utils.parseInt(var1[var2 + 1])) <= 0) {
                                                if (this.lineCondition == 0) {
                                                   this.lineCondition = 1;
                                                }

                                                if (var2 + 2 < var1.length) {
                                                   this.executeLine(var1, var2 + 2);
                                                   return;
                                                }

                                                this.advance(0);
                                                return;
                                             }

                                             this.lineCondition = 2;
                                          } else if (var1[var2].equals("无技能")) {
                                             if (!World.leaderHasSkill(Utils.parseInt(var1[var2 + 1]))) {
                                                if (this.lineCondition == 0) {
                                                   this.lineCondition = 1;
                                                }

                                                if (var2 + 2 < var1.length) {
                                                   this.executeLine(var1, var2 + 2);
                                                   return;
                                                }

                                                this.advance(0);
                                                return;
                                             }

                                             this.lineCondition = 2;
                                          } else if (var1[var2].equals("扣除技能")) {
                                             GameData.forgetRoleSkill(GameData.party[0], Utils.parseShort(var1[var2 + 2]));
                                          } else if (var1[var2].equals("扣除道具")) {
                                             GameData.removeItem(Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                                          } else if (var1[var2].equals("扣除装备")) {
                                             GameData.removeEquip(Utils.parseInt(var1[var2 + 1]));
                                          } else {
                                             if (var1[var2].equals("结束任务") || var1[var2].equals("结束脚本")) {
                                                this.endEvent();
                                                return;
                                             }

                                             if (var1[var2].equals("游戏存档")) {
                                                GameData.saveSlotSummary((byte)0);
                                                GameData.saveGame((byte)0);
                                                this.advance(5);
                                                return;
                                             }

                                             if (var1[var2].equals("游戏结局")) {
                                                if (Main.music != null) {
                                                   Main.music.stop();
                                                }

                                                GameCanvas.instance.world.endingId = Utils.parseByte(var1[var2 + 1]);
                                                GameCanvas.instance.world.worldState = 3;
                                                GameCanvas.instance.world.loadEndingStory();
                                                return;
                                             }

                                             if (var1[var2].equals("游戏结局1")) {
                                                if (Main.music != null) {
                                                   Main.music.stop();
                                                }

                                                GameCanvas.instance.world.endingId = Utils.parseByte(var1[var2 + 1]);
                                                GameData.isEndingPending = true;
                                                GameCanvas.instance.world.loadEndingStory();
                                             } else {
                                                if (var1[var2].equals("屏幕闪烁")) {
                                                   GameCanvas.instance.world.isScreenFlashing = true;
                                                   GameCanvas.instance.world.flashCounter = 5;
                                                   this.advance(1);
                                                   return;
                                                }

                                                if (var1[var2].equals("场景抖动")) {
                                                   GameCanvas.instance.world.isShaking = true;
                                                } else if (var1[var2].equals("场景抖动停")) {
                                                   GameCanvas.instance.world.isShaking = false;
                                                } else {
                                                   if (var1[var2].equals("加入队伍")) {
                                                      int var39 = Utils.parseInt(var1[var2 + 1]);
                                                      boolean var69 = false;

                                                      for (int var86 = 0; var86 < GameCanvas.instance.world.sceneLayer.actors.length; var86++) {
                                                         if (GameCanvas.instance.world.sceneLayer.actors[var86] != null
                                                            && GameCanvas.instance.world.sceneLayer.actors[var86].roleId == var39
                                                            && GameCanvas.instance.world.sceneLayer.actors[var86].kind == 1) {
                                                            var69 = true;
                                                            RoleSprite var98;
                                                            GameData.loadRoleData(var98 = (RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var86]);
                                                            GameData.addPartyMember(var98);
                                                            var98.stopWander();
                                                            if (var98.roleId == 2 || var98.roleId == 3 || var98.roleId == 4 || var98.roleId == 5) {
                                                               GameData.refreshRoleStats(var98);
                                                               var98.stats[3] = var98.stats[10];
                                                               var98.stats[5] = var98.stats[11];
                                                            }
                                                            break;
                                                         }
                                                      }

                                                      if (!var69) {
                                                         RoleSprite var87;
                                                         GameData.addPartyMember(var87 = GameData.createRoleSprite(var39));
                                                         GameCanvas.instance.world.sceneLayer.addActor(var87);
                                                         GameData.refreshRoleStats(var87);
                                                         var87.stats[3] = var87.stats[10];
                                                         var87.stats[5] = var87.stats[11];
                                                      }

                                                      this.advance(0);
                                                      return;
                                                   }

                                                   if (var1[var2].equals("离开队伍")) {
                                                      GameData.removePartyMember(Utils.parseInt(var1[var2 + 1]));
                                                      GameCanvas.instance.world.resetSceneNpcs();
                                                   } else {
                                                      if (var1[var2].equals("属性加点")) {
                                                         FieldInput.openMainMenu();
                                                         GameMenu.menuPage = 2;
                                                         GameCanvas.instance.world.menu.initPages();
                                                         GameCanvas.instance.world.menu.isStatsFromScript = true;
                                                         return;
                                                      }

                                                      if (var1[var2].equals("进入图鉴")) {
                                                         int var38 = Utils.parseInt(var1[var2 + 1]);
                                                         GameData.discoveredPetIds = Utils.append(GameData.discoveredPetIds, var38);
                                                         GameCanvas.instance.world.menu = null;
                                                         FieldInput.openMainMenu();
                                                         GameCanvas.instance.world.menu.isDexFromScript = true;
                                                         GameCanvas.instance.world.openBestiary();
                                                         GameCanvas.instance.world.menu.initPages();
                                                         GameCanvas.instance.world.menu.loadDexPet(var38);
                                                         GameCanvas.instance.world.menu.setSoftKeyFrame(2);
                                                         GameCanvas.instance.world.menu.dexPetId = var38;
                                                         GameCanvas.instance.world.menu.tabBar.setSelectMode(true);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("元素加点")) {
                                                         FieldInput.openMainMenu();
                                                         GameCanvas.instance.world.menu.isStatsFromScript = true;
                                                         GameMenu.menuPage = 2;
                                                         return;
                                                      }

                                                      if (var1[var2].equals("技能学习")) {
                                                         GameCanvas.instance
                                                            .world
                                                            .openShop("技能学习", Utils.parseInt(var1[var2 + 1]), Utils.parseInt(var1[var2 + 2]));
                                                         return;
                                                      }

                                                      if (var1[var2].equals("寄宠物")) {
                                                         GameCanvas.instance.world.openPetDepot();
                                                         return;
                                                      }

                                                      if (var1[var2].equals("取宠物")) {
                                                         GameCanvas.instance.world.openPetDepot();
                                                         return;
                                                      }

                                                      if (var1[var2].equals("买道具")) {
                                                         GameCanvas.instance.world.openShop("买道具", Utils.parseInt(var1[var2 + 1]), 1);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("买装备")) {
                                                         GameCanvas.instance.world.openShop("买装备", Utils.parseInt(var1[var2 + 1]), 1);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("装备合成")) {
                                                         GameCanvas.instance.world.openShop("装备合成", Utils.parseInt(var1[var2 + 1]), 1);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("镶嵌")) {
                                                         GameCanvas.instance.world.openShop("镶嵌", 11, 1);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("改变属性")) {
                                                         int var37 = Utils.parseInt(var1[var2 + 1]);
                                                         int var68 = Utils.parseInt(var1[var2 + 2]);
                                                         int var85 = Utils.parseInt(var1[var2 + 3]);

                                                         for (int var97 = 0;
                                                            GameCanvas.instance.world.sceneLayer.actors != null
                                                               && var97 < GameCanvas.instance.world.sceneLayer.actors.length;
                                                            var97++
                                                         ) {
                                                            if (GameCanvas.instance.world.sceneLayer.actors[var97] != null
                                                               && GameCanvas.instance.world.sceneLayer.actors[var97].kind == 1
                                                               && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var97]).roleId == var37) {
                                                               if (var68 < ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var97]).stats.length) {
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var97]).stats[var68] = ((RoleSprite)GameCanvas.instance
                                                                           .world
                                                                           .sceneLayer
                                                                           .actors[var97])
                                                                        .stats[var68]
                                                                     + var85;
                                                                  GameData.storeRoleRecords(
                                                                     new RoleSprite[]{(RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var97]}
                                                                  );
                                                                  GameData.refreshRoleStats((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var97]);
                                                                  break;
                                                               }

                                                               System.out.println("改变属性越界,越界属性index" + var68);
                                                            }
                                                         }

                                                         this.advance(0);
                                                         return;
                                                      }

                                                      if (var1[var2].equals("移除NPC")) {
                                                         short var9 = Utils.parseShort(var1[var2 + 1]);

                                                         for (int var4 = 0;
                                                            GameCanvas.instance.world.sceneLayer.actors != null
                                                               && var4 < GameCanvas.instance.world.sceneLayer.actors.length;
                                                            var4++
                                                         ) {
                                                            if (GameCanvas.instance.world.sceneLayer.actors[var4] != null
                                                               && GameCanvas.instance.world.sceneLayer.actors[var4].kind == 1
                                                               && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var4]).roleId == var9) {
                                                               GameCanvas.instance
                                                                  .world
                                                                  .sceneLayer
                                                                  .removeActor(GameCanvas.instance.world.sceneLayer.actors[var4]);
                                                            }
                                                         }

                                                         GameCanvas.instance.world.sceneLayer.sortAllActors();
                                                         GameCanvas.instance.world.sceneLayer.compactActors();
                                                      } else if (var1[var2].equals("改复活点")) {
                                                         GameData.reviveSceneId = Utils.parseShort(var1[var2 + 1]);
                                                         GameData.revivePoint = Utils.parseByte(var1[var2 + 2]);
                                                      } else {
                                                         if (var1[var2].equals("道具数量")) {
                                                            short var36 = Utils.parseShort(var1[var2 + 1]);
                                                            String var67 = var1[var2 + 2];
                                                            int var84 = Utils.parseInt(var1[var2 + 3]);
                                                            if (var67.equals("大于")) {
                                                               if (GameData.getItemCount(var36) <= var84) {
                                                                  this.lineCondition = 2;
                                                                  break label1768;
                                                               }

                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 4 < var1.length) {
                                                                  this.executeLine(var1, var2 + 4);
                                                               } else {
                                                                  this.advance(0);
                                                               }

                                                               return;
                                                            } else {
                                                               if (!var67.equals("小于")) {
                                                                  break label1768;
                                                               }

                                                               if (GameData.getItemCount(var36) >= var84) {
                                                                  this.lineCondition = 2;
                                                                  break label1768;
                                                               }

                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 4 < var1.length) {
                                                                  this.executeLine(var1, var2 + 4);
                                                               } else {
                                                                  this.advance(0);
                                                               }

                                                               return;
                                                            }
                                                         }

                                                         if (var1[var2].equals("判断属性")) {
                                                            int var35 = Utils.parseInt(var1[var2 + 1]);
                                                            int var66 = Utils.parseInt(var1[var2 + 2]);
                                                            String var83 = var1[var2 + 3];
                                                            int var96 = Utils.parseInt(var1[var2 + 4]);
                                                            if (var83.equals("大于")) {
                                                               boolean var99 = false;

                                                               for (int var102 = 0;
                                                                  GameData.roleRecords != null && var102 < GameData.roleRecords.length;
                                                                  var102++
                                                               ) {
                                                                  if (GameData.roleRecords[var102].roleId == var35) {
                                                                     if (GameData.roleRecords[var102].stats[var66] > var96) {
                                                                        if (this.lineCondition == 0) {
                                                                           this.lineCondition = 1;
                                                                        }

                                                                        if (var2 + 5 < var1.length) {
                                                                           this.executeLine(var1, var2 + 5);
                                                                        } else {
                                                                           this.advance(0);
                                                                        }

                                                                        var99 = true;
                                                                        break;
                                                                     }

                                                                     this.lineCondition = 2;
                                                                  }
                                                               }

                                                               if (!var99) {
                                                                  this.advance(0);
                                                                  return;
                                                               }
                                                            } else {
                                                               if (!var83.equals("小于")) {
                                                                  this.advance(0);
                                                                  return;
                                                               }

                                                               boolean var100 = false;

                                                               for (int var103 = 0;
                                                                  GameData.roleRecords != null && var103 < GameData.roleRecords.length;
                                                                  var103++
                                                               ) {
                                                                  if (GameData.roleRecords[var103].roleId == var35) {
                                                                     if (GameData.roleRecords[var103].stats[var66] < var96) {
                                                                        if (this.lineCondition == 0) {
                                                                           this.lineCondition = 1;
                                                                        }

                                                                        if (var2 + 5 < var1.length) {
                                                                           this.executeLine(var1, var2 + 5);
                                                                        } else {
                                                                           this.advance(0);
                                                                        }

                                                                        var100 = true;
                                                                        break;
                                                                     }

                                                                     this.lineCondition = 2;
                                                                  }
                                                               }

                                                               if (!var100) {
                                                                  this.advance(0);
                                                                  return;
                                                               }
                                                            }

                                                            return;
                                                         }

                                                         if (var1[var2].equals("播放动作")) {
                                                            int var34 = Utils.parseInt(var1[var2 + 1]);
                                                            int var65 = Utils.parseInt(var1[var2 + 2]);
                                                            int var82;
                                                            if (var2 + 3 < var1.length) {
                                                               var82 = Utils.parseInt(var1[var2 + 3]);
                                                            } else {
                                                               var82 = 1;
                                                            }

                                                            for (int var95 = 0;
                                                               GameCanvas.instance.world.sceneLayer.actors != null
                                                                  && var95 < GameCanvas.instance.world.sceneLayer.actors.length;
                                                               var95++
                                                            ) {
                                                               if (GameCanvas.instance.world.sceneLayer.actors[var95] != null
                                                                  && GameCanvas.instance.world.sceneLayer.actors[var95].kind == 1
                                                                  && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).roleId == var34) {
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).animSprite.setFrame(0);
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).animSprite.setAction(var65);
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).actionId = var65;
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).actionRepeat = var82;
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).returnAction = Utils.parseInt(
                                                                     var1[var2 + 4]
                                                                  );
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var95]).isPlayingAction = true;
                                                                  return;
                                                               }
                                                            }

                                                            return;
                                                         }

                                                         if (var1[var2].equals("飞行")) {
                                                            if (GameData.party[0].roleId == 1) {
                                                               GameData.flyAnimPath = "/av/long1.av";
                                                            } else {
                                                               GameData.flyAnimPath = "/av/long2.av";
                                                            }

                                                            GameData.party[0].animSprite = null;
                                                            GameData.party[0].animSprite = new AnimSprite();
                                                            GameData.party[0].animSprite.loadInGroup(GameData.flyAnimPath, 1);

                                                            for (int var33 = 0; var33 < GameData.party.length; var33++) {
                                                               if (var33 != 0) {
                                                                  GameData.party[var33].isVisible = false;
                                                               }
                                                            }

                                                            this.advance(5);
                                                            return;
                                                         }

                                                         if (var1[var2].equals("降落")) {
                                                            GameData.party[0].animSprite.loadInGroup(GameData.roleAnimPaths[0], 1);

                                                            for (int var32 = 0; var32 < GameData.party.length; var32++) {
                                                               if (var32 != 0) {
                                                                  GameData.party[var32].setPosition(GameData.party[0].x, GameData.party[0].y);
                                                                  GameData.party[var32].isVisible = true;
                                                               }
                                                            }

                                                            this.advance(5);
                                                            return;
                                                         }

                                                         if (var1[var2].equals("高级休息")) {
                                                            for (int var30 = 0; var30 < GameData.party.length; var30++) {
                                                               GameData.party[var30].stats[3] = GameData.party[var30].stats[10];
                                                               GameData.party[var30].stats[5] = GameData.party[var30].stats[11];
                                                            }

                                                            GameData.storeRoleRecords(GameData.party);

                                                            for (int var31 = 0; GameData.myPets != null && var31 < GameData.myPets.length; var31++) {
                                                               GameData.myPets[var31].stats[1] = GameData.myPets[var31].stats[2];
                                                               GameData.myPets[var31].stats[4] = GameData.myPets[var31].stats[5];
                                                            }

                                                            GameData.storePetRecords(GameData.myPets);
                                                            GameCanvas.instance.world.restFrames = 30;
                                                            GameCanvas.instance.world.isResting = true;
                                                            return;
                                                         }

                                                         if (var1[var2].equals("休息")) {
                                                            for (int var28 = 0; var28 < GameData.party.length; var28++) {
                                                               GameData.party[var28].stats[3] = GameData.party[var28].stats[10];
                                                            }

                                                            GameData.storeRoleRecords(GameData.party);

                                                            for (int var29 = 0; GameData.myPets != null && var29 < GameData.myPets.length; var29++) {
                                                               GameData.myPets[var29].stats[1] = GameData.myPets[var29].stats[2];
                                                            }

                                                            GameData.storePetRecords(GameData.myPets);
                                                            GameCanvas.instance.world.restFrames = 30;
                                                            GameCanvas.instance.world.isResting = true;
                                                            return;
                                                         }

                                                         if (var1[var2].equals("载入人物")) {
                                                            int var27 = Utils.parseInt(var1[var2 + 1]);
                                                            int var64 = Utils.parseInt(var1[var2 + 2]);
                                                            int var81 = Utils.parseInt(var1[var2 + 3]);
                                                            int var94 = Utils.parseInt(var1[var2 + 4]);
                                                            boolean var7 = false;

                                                            for (int var8 = 0; var8 < GameCanvas.instance.world.sceneLayer.actors.length; var8++) {
                                                               if (GameCanvas.instance.world.sceneLayer.actors[var8] != null
                                                                  && GameCanvas.instance.world.sceneLayer.actors[var8].roleId == var27
                                                                  && GameCanvas.instance.world.sceneLayer.actors[var8].kind == 1) {
                                                                  var7 = true;
                                                                  GameData.loadRoleData((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var8]);
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var8]).x = (short)var64;
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var8]).y = (short)var81;
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var8]).setDirection(var94);
                                                                  ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var8]).stopWander();
                                                                  break;
                                                               }
                                                            }

                                                            if (!var7) {
                                                               RoleSprite var101;
                                                               GameData.loadRoleData(var101 = GameData.createRoleSprite(var27));
                                                               var101.x = (short)var64;
                                                               var101.y = (short)var81;
                                                               var101.setDirection(var94);
                                                               var101.stopWander();
                                                               GameCanvas.instance.world.sceneLayer.addActor(var101);
                                                            }

                                                            this.advance(0);
                                                            return;
                                                         }

                                                         if (var1[var2].equals("有装备")) {
                                                            Utils.parseInt(var1[var2 + 1]);
                                                            if (GameData.hasEquip(Utils.parseInt(var1[var2 + 2]))) {
                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 3 < var1.length) {
                                                                  this.executeLine(var1, var2 + 3);
                                                                  return;
                                                               }

                                                               this.advance(0);
                                                               return;
                                                            }

                                                            this.lineCondition = 2;
                                                         } else if (var1[var2].equals("无装备")) {
                                                            Utils.parseInt(var1[var2 + 1]);
                                                            if (!GameData.hasEquip(Utils.parseInt(var1[var2 + 2]))) {
                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 3 < var1.length) {
                                                                  this.executeLine(var1, var2 + 3);
                                                                  return;
                                                               }

                                                               this.advance(0);
                                                               return;
                                                            }

                                                            this.lineCondition = 2;
                                                         } else if (var1[var2].equals("有技能")) {
                                                            if (GameData.hasSkill(GameData.party[0], Utils.parseInt(var1[var2 + 1]))) {
                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 3 < var1.length) {
                                                                  this.executeLine(var1, var2 + 3);
                                                                  return;
                                                               }

                                                               this.advance(0);
                                                               return;
                                                            }

                                                            this.lineCondition = 2;
                                                         } else if (var1[var2].equals("技能等级")) {
                                                            if (GameData.hasSkill(GameData.party[0], Utils.parseInt(var1[var2 + 1]))) {
                                                               int var10 = -1;

                                                               for (int var49 = 0; var49 < GameData.party[0].skills.length; var49++) {
                                                                  if (GameData.party[0].skills[var49].skillId == Utils.parseInt(var1[var2 + 1])) {
                                                                     var10 = var49;
                                                                     break;
                                                                  }
                                                               }

                                                               if (var1[var2 + 2].equals("小于")) {
                                                                  if (GameData.party[0].skills[var10].level < Utils.parseInt(var1[var2 + 3])) {
                                                                     if (this.lineCondition == 0) {
                                                                        this.lineCondition = 1;
                                                                     }

                                                                     if (var2 + 4 < var1.length) {
                                                                        this.executeLine(var1, var2 + 4);
                                                                     } else {
                                                                        this.advance(0);
                                                                     }

                                                                     return;
                                                                  } else {
                                                                     this.lineCondition = 2;
                                                                     this.advance(0);
                                                                     return;
                                                                  }
                                                               } else {
                                                                  if (!var1[var2 + 2].equals("大于")) {
                                                                     return;
                                                                  }

                                                                  if (GameData.party[0].skills[var10].level <= Utils.parseInt(var1[var2 + 3])) {
                                                                     this.lineCondition = 2;
                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (this.lineCondition == 0) {
                                                                     this.lineCondition = 1;
                                                                  }

                                                                  if (var2 + 4 < var1.length) {
                                                                     this.executeLine(var1, var2 + 4);
                                                                  } else {
                                                                     this.advance(0);
                                                                  }

                                                                  return;
                                                               }
                                                            }

                                                            this.lineCondition = 2;
                                                         } else if (var1[var2].equals("有宠物")) {
                                                            if (GameData.hasPetSpecies(Utils.parseInt(var1[var2 + 1]))) {
                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 2 < var1.length) {
                                                                  this.executeLine(var1, var2 + 2);
                                                                  return;
                                                               }

                                                               this.advance(0);
                                                               return;
                                                            }

                                                            this.lineCondition = 2;
                                                         } else {
                                                            if (var1[var2].equals("有战宠")) {
                                                               if (GameUtil.a_I() <= 0) {
                                                                  this.lineCondition = 2;
                                                                  this.advance(0);
                                                                  return;
                                                               }

                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 1 < var1.length) {
                                                                  this.executeLine(var1, var2 + 1);
                                                               } else {
                                                                  this.advance(0);
                                                               }

                                                               return;
                                                            }

                                                            if (var1[var2].equals("无战宠")) {
                                                               if (GameUtil.a_I() != 0) {
                                                                  this.lineCondition = 2;
                                                                  this.advance(0);
                                                                  return;
                                                               }

                                                               if (this.lineCondition == 0) {
                                                                  this.lineCondition = 1;
                                                               }

                                                               if (var2 + 1 < var1.length) {
                                                                  this.executeLine(var1, var2 + 1);
                                                               } else {
                                                                  this.advance(0);
                                                               }

                                                               return;
                                                            }

                                                            if (var1[var2].equals("无任务")) {
                                                               if (!Utils.contains(GameData.activeQuestIds, Utils.parseInt(var1[var2 + 1]))) {
                                                                  if (this.lineCondition == 0) {
                                                                     this.lineCondition = 1;
                                                                  }

                                                                  if (var2 + 2 < var1.length) {
                                                                     this.executeLine(var1, var2 + 2);
                                                                     return;
                                                                  }

                                                                  this.advance(0);
                                                                  return;
                                                               }

                                                               this.lineCondition = 2;
                                                            } else {
                                                               if (var1[var2].equals("有任务")) {
                                                                  short var26 = Utils.parseShort(var1[var2 + 1]);
                                                                  boolean var63 = false;

                                                                  for (int var80 = 0;
                                                                     GameData.activeQuests != null && var80 < GameData.activeQuests.length;
                                                                     var80++
                                                                  ) {
                                                                     if (GameData.activeQuests[var80].id == var26 && !GameData.activeQuests[var80].isCompleted) {
                                                                        var63 = true;
                                                                        break;
                                                                     }
                                                                  }

                                                                  if (!Utils.contains(GameData.activeQuestIds, Utils.parseInt(var1[var2 + 1])) || !var63) {
                                                                     this.lineCondition = 2;
                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (this.lineCondition == 0) {
                                                                     this.lineCondition = 1;
                                                                  }

                                                                  if (var2 + 2 < var1.length) {
                                                                     this.executeLine(var1, var2 + 2);
                                                                  } else {
                                                                     this.advance(0);
                                                                  }

                                                                  return;
                                                               }

                                                               if (var1[var2].equals("任务完成")) {
                                                                  short var25 = Utils.parseShort(var1[var2 + 1]);
                                                                  boolean var62 = false;

                                                                  for (int var79 = 0;
                                                                     GameData.activeQuests != null && var79 < GameData.activeQuests.length;
                                                                     var79++
                                                                  ) {
                                                                     if (GameData.activeQuests[var79].id == var25 && GameData.activeQuests[var79].isCompleted) {
                                                                        var62 = true;
                                                                        break;
                                                                     }
                                                                  }

                                                                  if (!var62) {
                                                                     this.lineCondition = 2;
                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (this.lineCondition == 0) {
                                                                     this.lineCondition = 1;
                                                                  }

                                                                  if (var2 + 2 < var1.length) {
                                                                     this.executeLine(var1, var2 + 2);
                                                                  } else {
                                                                     this.advance(0);
                                                                  }

                                                                  return;
                                                               }

                                                               if (var1[var2].equals("是否装备")) {
                                                                  if (GameData.party[0].equips[2] != null && GameData.party[0].equips[0] != null) {
                                                                     if (this.lineCondition == 0) {
                                                                        this.lineCondition = 1;
                                                                     }

                                                                     if (var2 + 1 < var1.length) {
                                                                        this.executeLine(var1, var2 + 1);
                                                                        return;
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  this.lineCondition = 2;
                                                               } else {
                                                                  if (var1[var2].equals("金钱数量")) {
                                                                     String var24 = var1[var2 + 1];
                                                                     int var61 = Utils.parseInt(var1[var2 + 2]);
                                                                     if (var24.equals("大于")) {
                                                                        if (GameData.money <= var61) {
                                                                           this.lineCondition = 2;
                                                                           break label1887;
                                                                        }

                                                                        if (this.lineCondition == 0) {
                                                                           this.lineCondition = 1;
                                                                        }

                                                                        if (var2 + 3 < var1.length) {
                                                                           this.executeLine(var1, var2 + 3);
                                                                        } else {
                                                                           this.advance(0);
                                                                        }

                                                                        return;
                                                                     } else {
                                                                        if (!var24.equals("小于")) {
                                                                           break label1887;
                                                                        }

                                                                        if (GameData.money >= var61) {
                                                                           this.lineCondition = 2;
                                                                           break label1887;
                                                                        }

                                                                        if (this.lineCondition == 0) {
                                                                           this.lineCondition = 1;
                                                                        }

                                                                        if (var2 + 3 < var1.length) {
                                                                           this.executeLine(var1, var2 + 3);
                                                                        } else {
                                                                           this.advance(0);
                                                                        }

                                                                        return;
                                                                     }
                                                                  }

                                                                  if (var1[var2].equals("平移")) {
                                                                     short var23 = Utils.parseShort(var1[var2 + 1]);
                                                                     byte var60 = Utils.parseByte(var1[var2 + 2]);
                                                                     RoleSprite var78 = null;

                                                                     for (int var93 = 0;
                                                                        GameCanvas.instance.world.sceneLayer.actors != null
                                                                           && var93 < GameCanvas.instance.world.sceneLayer.actors.length;
                                                                        var93++
                                                                     ) {
                                                                        if (GameCanvas.instance.world.sceneLayer.actors[var93] != null
                                                                           && GameCanvas.instance.world.sceneLayer.actors[var93].kind == 1
                                                                           && ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var93]).roleId == var23) {
                                                                           var78 = (RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var93];
                                                                           break;
                                                                        }
                                                                     }

                                                                     if (var78 != null) {
                                                                        switch (var60) {
                                                                           case 0:
                                                                              var78.y = (short)(var78.y + Utils.parseInt(var1[var2 + 3]));
                                                                              break;
                                                                           case 1:
                                                                              var78.y = (short)(var78.y - Utils.parseInt(var1[var2 + 3]));
                                                                              break;
                                                                           case 2:
                                                                              var78.x = (short)(var78.x - Utils.parseInt(var1[var2 + 3]));
                                                                              break;
                                                                           case 3:
                                                                              var78.x = (short)(var78.x + Utils.parseInt(var1[var2 + 3]));
                                                                        }
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("连续执行")) {
                                                                     this.advance(0);
                                                                     this.continuousLeft = Utils.parseShort(var1[var2 + 1]);
                                                                     this.continuousPending = this.continuousLeft;
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("变速走动")) {
                                                                     int var22 = Utils.parseInt(var1[var2 + 1]);
                                                                     int var59 = Utils.parseInt(var1[var2 + 2]);
                                                                     int var77 = Utils.parseInt(var1[var2 + 3]);
                                                                     RoleSprite var6;
                                                                     if ((var6 = GameCanvas.instance.world.findSceneRole(var22)) != null) {
                                                                        if (var77 < 4) {
                                                                           World.moveRole(var6, var77, var59, Utils.parseInt(var1[var2 + 4]), 0, 0);
                                                                        } else {
                                                                           World.moveRole(
                                                                              var6,
                                                                              var77,
                                                                              var59,
                                                                              Utils.parseInt(var1[var2 + 4]),
                                                                              Utils.parseInt(var1[var2 + 5]),
                                                                              Utils.parseInt(var1[var2 + 6])
                                                                           );
                                                                        }
                                                                     }

                                                                     this.advance(1);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("改变金钱")) {
                                                                     int var21 = Utils.parseInt(var1[var2 + 1]);
                                                                     if ((GameData.money += var21) < 0) {
                                                                        GameData.money = 0;
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("继承条件")) {
                                                                     if (this.lastCondition) {
                                                                        this.executeLine(var1, var2 + 1);
                                                                        return;
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("黑屏")) {
                                                                     GameCanvas.instance.world.blindsClosing = true;
                                                                     this.advance(1);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("恢复黑屏")) {
                                                                     GameCanvas.instance.world.blindsOpening = true;
                                                                     this.advance(1);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("上下拉屏")) {
                                                                     GameCanvas.instance.world.letterboxOpening = true;
                                                                     GameCanvas.instance.world.letterboxTarget = Utils.parseShort(var1[var2 + 1]);
                                                                     this.advance(1);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("恢复拉屏")) {
                                                                     GameCanvas.instance.world.letterboxClosing = true;
                                                                     this.advance(1);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("改变主角")) {
                                                                     if (GameData.heroId != Utils.parseShort(var1[var2 + 1])) {
                                                                        GameUtil.a(GameData.heroId = Utils.parseShort(var1[var2 + 1]));
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("控制抖动")) {
                                                                     int var20 = Utils.parseInt(var1[var2 + 1]);

                                                                     for (int var58 = 0; var58 < GameCanvas.instance.world.sceneLayer.actors.length; var58++) {
                                                                        if (GameCanvas.instance.world.sceneLayer.actors[var58] != null
                                                                           && GameCanvas.instance.world.sceneLayer.actors[var58].roleId == var20
                                                                           && GameCanvas.instance.world.sceneLayer.actors[var58].kind == 1) {
                                                                           ((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var58]).isAnimating = !((RoleSprite)GameCanvas.instance
                                                                                 .world
                                                                                 .sceneLayer
                                                                                 .actors[var58])
                                                                              .isAnimating;
                                                                        }
                                                                     }

                                                                     this.advance(0);
                                                                     return;
                                                                  }

                                                                  if (var1[var2].equals("载入宠物")) {
                                                                     GameData.addPet(Utils.parseInt(var1[var2 + 1]), -1);
                                                                  } else {
                                                                     if (var1[var2].equals("移除宠物")) {
                                                                        int var19 = Utils.parseInt(var1[var2 + 1]);

                                                                        for (int var57 = 0; GameData.myPets != null && var57 < GameData.myPets.length; var57++) {
                                                                           if (GameData.myPets[var57].petId == var19) {
                                                                              GameData.removePet(GameData.myPets[var57].uid);
                                                                              break;
                                                                           }
                                                                        }

                                                                        this.advance(0);
                                                                        return;
                                                                     }

                                                                     if (var1[var2].equals("开始下雨")) {
                                                                        GameCanvas.instance.world.startRain();
                                                                        this.advance(1);
                                                                        return;
                                                                     }

                                                                     if (var1[var2].equals("结束下雨")) {
                                                                        GameCanvas.instance.world.isRaining = false;
                                                                        GameCanvas.instance.world.rainDrops = null;
                                                                     } else {
                                                                        if (var1[var2].equals("有同伴")) {
                                                                           boolean var18 = false;
                                                                           int var56 = Utils.parseInt(var1[var2 + 1]);

                                                                           for (int var76 = 0;
                                                                              var76 < GameCanvas.instance.world.sceneLayer.actors.length;
                                                                              var76++
                                                                           ) {
                                                                              if (GameCanvas.instance.world.sceneLayer.actors[var76] != null
                                                                                 && GameCanvas.instance.world.sceneLayer.actors[var76].roleId == var56
                                                                                 && GameCanvas.instance.world.sceneLayer.actors[var76].kind == 1) {
                                                                                 var18 = true;
                                                                                 break;
                                                                              }
                                                                           }

                                                                           if (!var18) {
                                                                              this.lineCondition = 2;
                                                                              this.advance(0);
                                                                              return;
                                                                           }

                                                                           if (this.lineCondition == 0) {
                                                                              this.lineCondition = 1;
                                                                           }

                                                                           if (var2 + 2 < var1.length) {
                                                                              this.executeLine(var1, var2 + 2);
                                                                           } else {
                                                                              this.advance(0);
                                                                           }

                                                                           return;
                                                                        }

                                                                        if (var1[var2].equals("开启副本")) {
                                                                           short var11 = Utils.parseShort(var1[var2 + 1]);
                                                                           short var50 = Utils.parseShort(var1[var2 + 2]);
                                                                           short var5 = Utils.parseShort(var1[var2 + 3]);
                                                                           GameUtil.addDungeonPortal(var11, var50, var5);
                                                                        } else if (var1[var2].equals("是否开启")) {
                                                                           if (GameCanvas.instance.world.portalAnim != null) {
                                                                              if (this.lineCondition == 0) {
                                                                                 this.lineCondition = 1;
                                                                              }

                                                                              if (var2 + 1 < var1.length) {
                                                                                 this.executeLine(var1, var2 + 1);
                                                                                 return;
                                                                              }

                                                                              this.advance(0);
                                                                              return;
                                                                           }

                                                                           this.lineCondition = 2;
                                                                        } else {
                                                                           if (var1[var2].equals("主角ID")) {
                                                                              short var17 = Utils.parseShort(var1[var2 + 1]);
                                                                              if (GameData.heroId != var17) {
                                                                                 this.lineCondition = 2;
                                                                                 this.advance(0);
                                                                                 return;
                                                                              }

                                                                              if (this.lineCondition == 0) {
                                                                                 this.lineCondition = 1;
                                                                              }

                                                                              if (var2 + 2 < var1.length) {
                                                                                 this.executeLine(var1, var2 + 2);
                                                                              } else {
                                                                                 this.advance(0);
                                                                              }

                                                                              return;
                                                                           }

                                                                           if (var1[var2].equals("有随机任务")) {
                                                                              boolean var16 = false;

                                                                              for (int var55 = 0;
                                                                                 GameData.activeQuests != null && var55 < GameData.activeQuests.length;
                                                                                 var55++
                                                                              ) {
                                                                                 if (GameData.activeQuests[var55].type == 3
                                                                                    && !GameData.activeQuests[var55].isCompleted) {
                                                                                    var16 = true;
                                                                                    break;
                                                                                 }
                                                                              }

                                                                              if (!var16) {
                                                                                 this.lineCondition = 2;
                                                                                 this.advance(0);
                                                                                 return;
                                                                              }

                                                                              if (this.lineCondition == 0) {
                                                                                 this.lineCondition = 1;
                                                                              }

                                                                              if (var2 + 1 < var1.length) {
                                                                                 this.executeLine(var1, var2 + 1);
                                                                              } else {
                                                                                 this.advance(0);
                                                                              }

                                                                              return;
                                                                           }

                                                                           if (var1[var2].equals("无随机任务")) {
                                                                              boolean var15 = false;

                                                                              for (int var54 = 0;
                                                                                 GameData.activeQuests != null && var54 < GameData.activeQuests.length;
                                                                                 var54++
                                                                              ) {
                                                                                 if (GameData.activeQuests[var54].type == 3) {
                                                                                    var15 = true;
                                                                                    break;
                                                                                 }
                                                                              }

                                                                              if (var15) {
                                                                                 this.lineCondition = 2;
                                                                                 this.advance(0);
                                                                                 return;
                                                                              }

                                                                              if (this.lineCondition == 0) {
                                                                                 this.lineCondition = 1;
                                                                              }

                                                                              if (var2 + 1 < var1.length) {
                                                                                 this.executeLine(var1, var2 + 1);
                                                                              } else {
                                                                                 this.advance(0);
                                                                              }

                                                                              return;
                                                                           }

                                                                           if (var1[var2].equals("随机任务")) {
                                                                              int var12 = Utils.parseInt(var1[var2 + 1]);
                                                                              int var51 = GameUtil.randomBetween(0, this.randomQuestPools[var12 - 1].length - 1);
                                                                              this.randomQuestId = this.randomQuestPools[var12 - 1][var51];
                                                                           } else {
                                                                              if (var1[var2].equals("显示任务")) {
                                                                                 GameCanvas.instance.world.lootPetName = null;
                                                                                 if (var1.length - var2 > 1) {
                                                                                    GameCanvas.instance.world.shownQuestId = Utils.parseShort(var1[var2 + 1]);
                                                                                 } else {
                                                                                    GameCanvas.instance.world.shownQuestId = this.randomQuestId;
                                                                                 }

                                                                                 GameCanvas.instance.world.mode = 9;
                                                                                 GameCanvas.instance.world.questDialogPage = 1;
                                                                                 return;
                                                                              }

                                                                              if (var1[var2].equals("完成随机任务")) {
                                                                                 boolean var14 = false;

                                                                                 for (int var53 = 0;
                                                                                    GameData.activeQuests != null && var53 < GameData.activeQuests.length;
                                                                                    var53++
                                                                                 ) {
                                                                                    if (GameData.activeQuests[var53].id >= 37
                                                                                       && GameData.activeQuests[var53].id <= 50
                                                                                       && GameData.activeQuests[var53].isCompleted) {
                                                                                       var14 = true;
                                                                                       break;
                                                                                    }
                                                                                 }

                                                                                 if (!var14) {
                                                                                    this.lineCondition = 2;
                                                                                    this.advance(0);
                                                                                    return;
                                                                                 }

                                                                                 if (this.lineCondition == 0) {
                                                                                    this.lineCondition = 1;
                                                                                 }

                                                                                 if (var2 + 1 < var1.length) {
                                                                                    this.executeLine(var1, var2 + 1);
                                                                                 } else {
                                                                                    this.advance(0);
                                                                                 }

                                                                                 return;
                                                                              }

                                                                              if (var1[var2].equals("移除随机任务")) {
                                                                                 short var13 = 0;

                                                                                 for (int var52 = 0;
                                                                                    GameData.activeQuests != null && var52 < GameData.activeQuests.length;
                                                                                    var52++
                                                                                 ) {
                                                                                    if (GameData.activeQuests[var52].id >= 37
                                                                                       && GameData.activeQuests[var52].id <= 50
                                                                                       && GameData.activeQuests[var52].isCompleted) {
                                                                                       var13 = GameData.activeQuests[var52].id;
                                                                                       break;
                                                                                    }
                                                                                 }

                                                                                 if (var13 > 0) {
                                                                                    GameData.finishQuest(var13, true);
                                                                                    this.advance(0);
                                                                                 }

                                                                                 return;
                                                                              }

                                                                              if (var1[var2].equals("职业等级")) {
                                                                                 if (GameData.party[0].jobLevel == Utils.parseByte(var1[var2 + 1])) {
                                                                                    if (this.lineCondition == 0) {
                                                                                       this.lineCondition = 1;
                                                                                    }

                                                                                    if (var2 + 2 < var1.length) {
                                                                                       this.executeLine(var1, var2 + 2);
                                                                                       return;
                                                                                    }

                                                                                    this.advance(0);
                                                                                    return;
                                                                                 }

                                                                                 this.lineCondition = 2;
                                                                              } else if (var1[var2].equals("改变职业等级")) {
                                                                                 GameData.party[0].jobLevel++;
                                                                              }
                                                                           }
                                                                        }
                                                                     }
                                                                  }
                                                               }
                                                            }
                                                         }
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 } else {
                                    if (this.battleResult == 1) {
                                       if (this.lineCondition == 0) {
                                          this.lineCondition = 1;
                                       }

                                       if (var2 + 1 < var1.length) {
                                          this.executeLine(var1, var2 + 1);
                                          return;
                                       }

                                       this.advance(0);
                                       return;
                                    }

                                    this.lineCondition = 2;
                                 }
                              } else {
                                 if (this.battleResult == 0) {
                                    if (this.lineCondition == 0) {
                                       this.lineCondition = 1;
                                    }

                                    if (var2 + 1 < var1.length) {
                                       this.executeLine(var1, var2 + 1);
                                       return;
                                    }

                                    this.advance(0);
                                    return;
                                 }

                                 this.lineCondition = 2;
                              }
                           } else {
                              if (this.battleResult == 2) {
                                 if (this.lineCondition == 0) {
                                    this.lineCondition = 1;
                                 }

                                 if (var2 + 1 < var1.length) {
                                    this.executeLine(var1, var2 + 1);
                                    return;
                                 }

                                 this.advance(0);
                                 return;
                              }

                              this.lineCondition = 2;
                           }
                        }
                     }
                  }

                  this.advance(0);
                  return;
               }

               this.advance(0);
               return;
            }

            this.advance(0);
         }
      }
   }

   public final void advance(int var1) {
      if (var1 == 0) {
         this.lineIndex++;
         if (this.continuousPending > 0) {
            this.continuousPending--;
         }
      } else if (var1 == 1) {
         if (this.continuousLeft > 0) {
            this.lineIndex++;
            if (DeviceConfig.debugMode) {
               System.out.println("脚本继续执行");
            }
         } else if (DeviceConfig.debugMode) {
            System.out.println("脚本暂停");
         }
      } else if (var1 >= 2) {
         if (this.continuousPending > 0) {
            this.continuousPending--;
         }

         if (this.continuousPending == 0) {
            this.lineIndex++;
         }
      }

      if (this.lineCondition == 1) {
         this.lastCondition = true;
      } else if (this.lineCondition == 2) {
         this.lastCondition = false;
      }

      this.lineCondition = 0;
   }
}
