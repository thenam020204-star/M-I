import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;

public final class GameCanvas extends Canvas implements Runnable {
   public static GameCanvas instance;
   public short frameCounter;
   private boolean loopRunning;
   public boolean paused;
   private long frameStartMs;
   private long frameEndMs;
   private long frameAfterSleepMs;
   private long fps;
   private short frameIntervalMs;
   private boolean threadAlive;
   private Thread gameThread = null;
   public static byte screenState;
   LogoScreen logoScreen;
   TitleScreen titleScreen;
   public World world;
   static Random random;
   public int[] a_AI;
   short screenWidth;
   short screenHeight;
   public Display display;
   private String toastText;
   private boolean toastActive;
   private int toastFrames;

   public final void setDisplayable(Displayable var1) {
      this.display.setCurrent(var1);
   }

   protected GameCanvas(Display var1) {
      this.display = var1;
      this.setFullScreenMode(true);
      instance = this;
      this.frameIntervalMs = 16;
      this.loopRunning = false;
      this.paused = false;
      if (random == null) {
         (random = new Random()).setSeed(System.currentTimeMillis());
      }

      this.screenWidth = (short)super.getWidth();
      this.screenHeight = (short)super.getHeight();
      if (this.screenWidth == 128) {
         this.screenHeight = 160;
      } else {
         if (this.screenWidth == 176 && this.screenHeight < 220) {
            this.screenHeight = 208;
         }
      }
   }

   public final int getWidth() {
      return this.screenWidth;
   }

   public final int getHeight() {
      return this.screenHeight;
   }

   public final void startLoop() {
      this.loopRunning = true;
      if (this.gameThread == null && !this.threadAlive) {
         this.gameThread = new Thread(this);
         this.gameThread.start();
         this.threadAlive = true;
      }

      this.display.setCurrent(this);
   }

   public final void stopLoop() {
      this.loopRunning = false;
   }

   protected final void hideNotify() {
      System.out.println("hideNotify()");
      this.paused = true;
      if (Main.music != null) {
         Main.music.stop();
      }

      if (this.world != null) {
         World.stopMusic();
      }

      Main.self.stopMusic();
      screenState = 7;
   }

   protected final void showNotify() {
      this.paused = false;
   }

   protected final void keyPressed(int var1) {
      if (!this.toastActive) {
         if (!QQCommunityOverlay.handleKey(var1 = KeyMap.normalizeKey(var1))) {
            if (var1 == KeyMap.softRightKey) {
               if (DeviceConfig.modelName.equals("L6")
                  || DeviceConfig.modelName.equals("L7")
                  || DeviceConfig.modelName.equals("K1")
                  || DeviceConfig.modelName.equals("E398")
                  || DeviceConfig.modelName.equals("V8")
                  || DeviceConfig.modelName.equals("E2")) {
                  var1 = KeyMap.softLeftKey;
               }
            } else if (var1 == KeyMap.softLeftKey
               && (
                  DeviceConfig.modelName.equals("L6")
                     || DeviceConfig.modelName.equals("L7")
                     || DeviceConfig.modelName.equals("K1")
                     || DeviceConfig.modelName.equals("E398")
                     || DeviceConfig.modelName.equals("V8")
                     || DeviceConfig.modelName.equals("E2")
               )) {
               var1 = KeyMap.softRightKey;
            }

            synchronized (this) {
               switch (screenState) {
                  case 1:
                  case 5:
                  default:
                     break;
                  case 2:
                     if (this.logoScreen != null) {
                        LogoScreen var3 = this.logoScreen;
                        if (this.logoScreen.mode == 1) {
                           if (var1 == KeyMap.softLeftKey) {
                              DeviceConfig.playMusic = true;
                              var3.isFinished = true;
                              GameData.musicVolume = DeviceConfig.defaultVolume;
                              Main.self.showCover();
                           } else if (var1 == KeyMap.softRightKey) {
                              GameData.musicVolume = 0;
                              DeviceConfig.playMusic = false;
                              var3.isFinished = true;
                              Main.self.showCover();
                           }
                        }
                     }
                     break;
                  case 3:
                     if (this.titleScreen != null) {
                        this.titleScreen.handleKey(var1);
                     }
                     break;
                  case 4:
                     if (this.world != null) {
                        this.world.handleKey(var1);
                     }
                     break;
                  case 6:
                     try {
                        Main.self.destroyApp(true);
                     } catch (MIDletStateChangeException var5) {
                        var5.printStackTrace();
                     }
                     break;
                  case 7:
                     if (var1 == 8) {
                        if (this.logoScreen != null) {
                           screenState = 2;
                        } else if (this.titleScreen != null) {
                           if (Main.music != null) {
                              Main.self.playMusic(GameSettings.titleMusic, -1, GameData.musicVolume);
                              Main.music.setVolume(GameData.musicVolume);
                              Main.music.start();
                           }

                           screenState = 3;
                        } else if (this.world != null) {
                           screenState = 4;
                           if (this.world.battle != null) {
                              if (this.world.battle.phase == 24) {
                                 Battle.playBattleMusic(1);
                              } else if (this.world.battle.phase == 25) {
                                 Battle.playBattleMusic(2);
                              } else {
                                 Battle.playBattleMusic(0);
                              }
                           } else {
                              World.playSceneMusic();
                           }
                        }

                        this.a_AI = null;
                     }
               }
            }

            if (!Utils.contains(this.a_AI, var1)) {
               this.a_AI = Utils.append(this.a_AI, var1);
            }

            if (DeviceConfig.allowDebug && var1 == 42) {
               DeviceConfig.debugMode = !DeviceConfig.debugMode;
            }
         }
      }
   }

   protected final void keyReleased(int var1) {
      var1 = KeyMap.normalizeKey(var1);
      if (Utils.contains(this.a_AI, var1)) {
         this.a_AI = Utils.removeValue(this.a_AI, var1);
      }
   }

   public final void pointerPressed(int var1, int var2) {
      QQCommunityOverlay.handlePointer(var1, var2);
      if (screenState == 1) {
         if (DeviceConfig.modelName != null
            && (DeviceConfig.modelName.equals("L6") || DeviceConfig.modelName.equals("L7") || DeviceConfig.modelName.equals("E2"))) {
            if (var1 > instance.getWidth() - 32 && var2 > instance.getHeight() - 32) {
               DeviceConfig.playMusic = true;
               Main.self.showLogo();
               return;
            }

            if (var1 < 32 && var2 > instance.getHeight() - 32) {
               DeviceConfig.playMusic = false;
               Main.self.showLogo();
               return;
            }
         } else {
            if (var1 > instance.getWidth() - 32 && var2 > instance.getHeight() - 32) {
               DeviceConfig.playMusic = false;
               Main.self.showLogo();
               return;
            }

            if (var1 < 32 && var2 > instance.getHeight() - 32) {
               DeviceConfig.playMusic = true;
               Main.self.showLogo();
               return;
            }
         }
      } else if (screenState == 4) {
         if (this.world != null) {
            this.world.onPointerPress(var1, var2);
            return;
         }
      } else if (screenState == 3) {
         if (this.titleScreen != null) {
            this.titleScreen.handlePointer(var1, var2);
            return;
         }
      } else {
         if (screenState == 6) {
            try {
               Main.self.destroyApp(true);
               return;
            } catch (MIDletStateChangeException var4) {
               var4.printStackTrace();
               return;
            }
         }

         if (screenState == 2 && this.logoScreen != null) {
            LogoScreen var5 = this.logoScreen;
            TouchButton var7;
            if ((var7 = TouchSoftKeys.hitTest(var1, var2)) != null) {
               if (var7.buttonId == 2) {
                  if (var5.mode == 1) {
                     DeviceConfig.playMusic = false;
                     GameData.musicVolume = 0;
                     var5.isFinished = true;
                     Main.self.showCover();
                     return;
                  }
               } else if (var7.buttonId == 1 && var5.mode == 1) {
                  DeviceConfig.playMusic = true;
                  var5.isFinished = true;
                  GameData.musicVolume = DeviceConfig.defaultVolume;
                  Main.self.showCover();
               }
            }
         }
      }
   }

   public final void pointerDragged(int var1, int var2) {
      if (screenState == 4 && this.world != null) {
         this.world.onPointerDrag(var1, var2);
      }
   }

   public final void pointerReleased(int var1, int var2) {
      if (screenState == 4 && this.world != null) {
         this.world.onPointerRelease(var1, var2);
      }
   }

   public final void run() {
      while (this.loopRunning) {
         if (!this.paused) {
            this.frameStartMs = System.currentTimeMillis();
            this.repaint();
            this.serviceRepaints();
            this.frameCounter++;
            if (this.frameCounter >= 127) {
               this.frameCounter = 0;
            }

            this.frameEndMs = System.currentTimeMillis();
            if (DeviceConfig.modelName != null && (DeviceConfig.modelName.equals("L6") || DeviceConfig.modelName.equals("L7"))) {
               try {
                  Thread.sleep(10L);
               } catch (InterruptedException var3) {
                  var3.printStackTrace();
               }
            } else if (this.frameEndMs - this.frameStartMs < (long)this.frameIntervalMs) {
               try {
                  Thread.sleep((long)this.frameIntervalMs - (this.frameEndMs - this.frameStartMs));
               } catch (InterruptedException var2) {
                  var2.printStackTrace();
               }
            }

            this.frameAfterSleepMs = System.currentTimeMillis();
            if (this.frameAfterSleepMs > this.frameStartMs) {
               this.fps = 1000L / (this.frameAfterSleepMs - this.frameStartMs);
            }
         } else {
            try {
               Thread.sleep(2000L);
            } catch (InterruptedException var4) {
               var4.printStackTrace();
            }
         }
      }

      this.threadAlive = false;
   }

   public final void showToast(String var1, int var2) {
      this.toastText = var1;
      this.toastFrames = var2;
      this.toastActive = true;
   }

   protected final void paint(Graphics var1) {
      synchronized (this) {
         var1.setFont(Utils.defaultFont);
         switch (screenState) {
            case 0:
               var1.setColor(0);
               var1.fillRect(0, 0, instance.getWidth(), instance.getHeight());
               break;
            case 1:
               var1.setColor(0);
               var1.fillRect(0, 0, instance.getWidth(), instance.getHeight());
               Utils.drawOutlinedText(var1, "Bật âm nhạc?", instance.getWidth() / 2, instance.getHeight() / 2, 13200659, 16777215, 33);
               var1.setColor(16777215);
               if (DeviceConfig.modelName == null
                  || !DeviceConfig.modelName.equals("L6") && !DeviceConfig.modelName.equals("L7") && !DeviceConfig.modelName.equals("E2")) {
                  vh.drawString(var1, "Không", instance.getWidth() - 1, instance.getHeight() - 1, 40);
                  vh.drawString(var1, "Có", 1, instance.getHeight() - 1, 36);
               } else {
                  vh.drawString(var1, "Có", instance.getWidth() - 1, instance.getHeight() - 1, 40);
                  vh.drawString(var1, "Không", 1, instance.getHeight() - 1, 36);
               }
               break;
            case 2:
               if (this.logoScreen != null) {
                  this.logoScreen.draw(var1);
               }
               break;
            case 3:
               if (this.titleScreen != null) {
                  this.titleScreen.draw(var1);
               }
               break;
            case 4:
               if (this.world != null) {
                  try {
                     this.world.updateAndDraw(var1);
                  } catch (Exception var9) {
                     var9.printStackTrace();
                  }
               }
            case 5:
            default:
               break;
            case 6:
               var1.setColor(16711680);
               vh.drawString(var1, "Hãy chạy trên điện thoại", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 33);
               break;
            case 7:
               var1.setColor(0);
               var1.fillRect(0, 0, instance.getWidth(), instance.getHeight());
               var1.setColor(16777215);
               vh.drawString(var1, "Nhấn OK để tiếp tục", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 17);
         }

         Graphics var4 = var1;
         var1.translate(0, 0);
         if (this.toastActive) {
            if (this.toastFrames <= 0) {
               this.toastText = null;
               this.toastActive = false;
            } else {
               if (this.toastText != null && !this.toastText.equals("")) {
                  String[] var8 = Utils.wrapText(this.toastText, DeviceConfig.screenSize[0] - 32);
                  int var5 = DeviceConfig.screenSize[0] / 2 - var8.length * Utils.lineHeight / 2 - 5;
                  int var6 = DeviceConfig.screenSize[0] - 24;
                  int var7 = var8.length * Utils.lineHeight + 10;
                  var1.setColor(1381651);
                  var1.fillRoundRect(11, var5 - 1, var6 + 3, var7 + 3, 8, 8);
                  var1.setColor(16053217);
                  var1.drawRoundRect(12, var5, var6, var7, 8, 8);
                  var1.setColor(6768205);
                  var1.fillRect(14, var5 + 2, var6 - 3, var7 - 3);

                  for (int var11 = 0; var11 < var8.length; var11++) {
                     var4.setColor(16777215);
                     vh.drawString(
                        var4,
                        var8[var11],
                        DeviceConfig.screenSize[0] / 2,
                        DeviceConfig.screenSize[0] / 2 - var8.length * Utils.lineHeight / 2 + var11 * Utils.lineHeight,
                        17
                     );
                  }
               }

               this.toastFrames--;
            }
         }

         var1.setClip(0, 0, instance.getWidth(), instance.getHeight());
         if (DeviceConfig.debugMode) {
            var1.setColor(16711680);
            vh.drawString(
               var1, "Bộ nhớ:" + (Runtime.getRuntime().freeMemory() >> 10) + "/" + (Runtime.getRuntime().totalMemory() >> 10), 2, instance.getHeight() - 2, 36
            );
            vh.drawString(var1, "FPS:" + instance.fps, 2, instance.getHeight() - Utils.lineHeight - 2, 36);
         }
      }

      QQCommunityOverlay.drawOverlay(var1);
   }
}
