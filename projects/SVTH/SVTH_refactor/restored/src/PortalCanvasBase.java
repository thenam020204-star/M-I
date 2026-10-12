import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;

public abstract class PortalCanvasBase extends Canvas implements Runnable, CommandListener {
   public boolean isMenuOpen;
   private boolean isDialogOpen;
   protected boolean isLogoutPending;
   public boolean isEmbeddedMode;
   private String[] dialogLines;
   protected boolean isRunning;
   protected PortalClient client;
   public PortalManager manager;
   public static Vector pageImages;
   protected int mode;
   public static int screenWidth;
   public static int screenHeight;
   public static final Font font = Font.getFont(0, 0, 8);
   protected int keyCode;
   protected int gameAction;
   private boolean isPointerPressed;
   private int pointerX;
   private int pointerY;
   protected int headerHeight;
   protected int footerHeight;
   public static int lineHeight;
   private static String cancelLabel = "Hủy";
   public static String[] softKeyLabels = new String[]{
      "Có", "Không", "Đăng nhập", "Xác minh", "Trở về", "Menu", "OK", "Vào khu chuyên", "Đổi QQ", "Trở về", "", ""
   };
   protected byte leftSoftKey = -1;
   protected byte rightSoftKey = -1;
   public byte prevLeftSoftKey;
   public byte prevRightSoftKey;
   private int[] footerGradient;
   private int[] footerEdgeGradient;
   private int[] progressGradient;
   private int[] dialogGradient;
   protected int[] highlightGradient;
   private int[] bodyGradient;
   public TextBox textBox;
   public Command okCommand;
   public Command backCommand;
   public Thread repaintThread;
   protected boolean isInitialized;

   public PortalCanvasBase(PortalClient var1) {
      this.setFullScreenMode(true);
      this.isRunning = true;
      this.isDialogOpen = false;
      this.isLogoutPending = false;
      this.isMenuOpen = false;
      this.isInitialized = false;
      this.client = var1;
      this.client.hasPointer = this.hasPointerEvents();
      this.manager = new PortalManager(var1);
   }

   public final void initMetrics(boolean var1) {
      screenWidth = this.client.screenWidth;
      screenHeight = this.client.screenHeight;
      vh.stringWidth(font, "Quốc");
      vh.charWidth(font, '*');
      lineHeight = vh.getHeight(font) + 2;
      this.headerHeight = lineHeight;
      this.footerHeight = lineHeight + 10;
      var1 = vh.getHeight(font) + 4;
      this.footerGradient = makeGradient(12, 3164256, 1650754);
      this.footerEdgeGradient = makeGradient(screenWidth / 4, 2311520, 7709902);
      this.bodyGradient = makeGradient((screenHeight - this.footerHeight - this.headerHeight) / 4 + 1, 733778, 1395578);
      int var2 = var1 > 18 ? 16 : var1 - 2;
      this.progressGradient = makeGradient(var2, 2915263, 1264507);
      this.highlightGradient = makeGradient(var1, 3318527, 1133951);
      makeGradient(var1, 42492, 92087);
      this.dialogGradient = makeGradient(var1, 938626, 866139);
   }

   public abstract void initialize();

   public final void setListener(PortalListener var1) {
      this.manager.listener = var1;
   }

   protected void paint(Graphics var1) {
      this.processKey();
      if (this.isRunning) {
         if (this.client.hasPointer && this.isPointerPressed) {
            byte var4 = 0;
            if (isInSoftKeyArea(this.pointerX, this.pointerY, 0, screenHeight - this.footerHeight, 25, this.footerHeight)) {
               var4 = 1;
            } else if (isInSoftKeyArea(this.pointerX, this.pointerY, screenWidth - 25, screenHeight - this.footerHeight, 25, this.footerHeight)) {
               var4 = 2;
            }

            switch (var4) {
               case 1:
                  this.keyCode = -506;
                  this.processKey();
                  break;
               case 2:
                  this.keyCode = -507;
                  this.processKey();
            }

            this.clearPointer();
         }

         var1.setColor(1397123);
         var1.fillRect(0, 0, screenWidth, screenHeight);
         var1.setColor(0);
         var1.drawRect(0, 0, screenWidth - 1, screenHeight - 1);
         this.manager.tickProgress();
         Graphics var3 = var1;
         PortalCanvasBase var2 = this;
         int var8 = this.bodyGradient.length;
         int var5 = this.headerHeight;
         int var6 = screenHeight - this.footerHeight;

         for (int var7 = 0; var5 < var6 && var7 < var8; var7++) {
            var3.setColor(var2.bodyGradient[var7]);
            var3.fillRect(0, var5, screenWidth, 4);
            var5 += 4;
         }

         if (var5 < var6) {
            var3.setColor(var2.bodyGradient[var8 - 1]);
            var3.fillRect(0, var5, screenWidth, var6 - var5);
         }

         this.drawContent(var1);
         this.drawBar(var1, screenHeight - this.footerHeight, screenWidth, this.footerHeight);
         if (this.manager.loadState == 1) {
            var8 = screenWidth - 84 > 112 ? 112 : screenWidth - 84;
            var5 = screenWidth - var8 >> 1;
            var6 = screenHeight - this.footerHeight;
            var1.setColor(268068);
            var1.fillRect(var5, var6 + 2, var8, lineHeight);
            fillGradient(var1, var5 + 1, var6 + 2, this.manager.progressPercent * var8 / 100, lineHeight, this.progressGradient);
            var1.setColor(1782338);
            var1.drawRect(var5, var6 + 2, var8, lineHeight);
            var1.setColor(8829414);
            if (this.manager.progressPercent != 100) {
               vh.drawString(var1, "Đang kết nối..." + this.manager.progressPercent + "%", screenWidth >> 1, var6 + 2, 17);
            }

            drawOutlinedText(var1, cancelLabel, screenWidth - 5, screenHeight - this.footerHeight + 4, 6177536, 16770602, 24);
         } else if (this.rightSoftKey != -1) {
            drawOutlinedText(var1, softKeyLabels[this.rightSoftKey], screenWidth - 5, screenHeight - this.footerHeight + 4, 6177536, 16770602, 24);
         }

         if (this.leftSoftKey != -1) {
            drawOutlinedText(var1, softKeyLabels[this.leftSoftKey], 5, screenHeight - this.footerHeight + 4, 6177536, 16770602, 20);
         }

         if (this.isDialogOpen) {
            this.drawDialog(var1);
         }
      }
   }

   public abstract void drawContent(Graphics var1);

   public final void drawBar(Graphics var1, int var2, int var3, int var4) {
      int var5 = 0;
      var1.setColor(202277);
      var1.drawLine(0, var2, var3, var2);
      var5++;

      for (int var6 = 0; var6 < this.footerEdgeGradient.length; var6++) {
         var1.setColor(this.footerEdgeGradient[var6]);
         var1.drawLine(var6 << 1, var2 + 1, (var6 << 1) + 1, var2 + 1);
         var1.drawLine(var3 - (var6 << 1) - 1, var2 + 1, var3 - (var6 << 1) - 2, var2 + 1);
      }

      var5++;

      for (int var9 = 1; var9 < this.footerGradient.length; var9++) {
         var1.setColor(this.footerGradient[var9]);
         var1.drawLine(0, var2 + var5, var3, var2 + var5);
         var5++;
      }

      if (var4 > var5) {
         var1.setColor(70433);
         var1.fillRect(0, var2 + var5, var3, var4 - var5);
      }
   }

   public static void fillGradient(Graphics var0, int var1, int var2, int var3, int var4, int[] var5) {
      int var7;
      if ((var7 = var5.length) > var4) {
         var7 = var4;
      }

      int var6;
      for (var6 = 0; var6 < var7; var6++) {
         var0.setColor(var5[var6]);
         var0.drawLine(var1, var2 + var6, var1 + var3, var2 + var6);
      }

      if (var4 > var7) {
         while (var6 < var4) {
            var0.setColor(var5[var7 - 1]);
            var0.drawLine(var1, var2 + var6, var1 + var3, var2 + var6);
            var6++;
         }
      }
   }

   public void run() {
      while (this.isRunning) {
         try {
            Thread.sleep(100L);
            this.repaint();
         } catch (Exception var1) {
            var1.printStackTrace();
         }
      }
   }

   protected final void setSoftKeys(byte var1, byte var2) {
      this.prevLeftSoftKey = this.leftSoftKey;
      this.prevRightSoftKey = this.rightSoftKey;
      this.leftSoftKey = var1;
      this.rightSoftKey = var2;
   }

   public abstract void setMode(int var1);

   private static void drawOutlinedText(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6) {
      var0.setColor(6177536);
      vh.drawString(var0, var1, var2 - 1, var3 - 1, var6);
      vh.drawString(var0, var1, var2 + 1, var3 + 1, var6);
      var0.setColor(16770602);
      vh.drawString(var0, var1, var2, var3, var6);
   }

   private static int[] makeGradient(int var0, int var1, int var2) {
      int var3 = var1 >> 16;
      int var4 = var1 >> 8 & 0xFF;
      int var5 = var1 & 0xFF;
      int var6 = var2 >> 16;
      int var7 = var2 >> 8 & 0xFF;
      int var8 = var2 & 0xFF;
      var6 = (var6 - var3) * 100 / (var0 - 2);
      var7 = (var7 - var4) * 100 / (var0 - 2);
      var8 = (var8 - var5) * 100 / (var0 - 2);
      int[] var9;
      (var9 = new int[var0])[0] = var1;
      var9[var0 - 1] = var2;

      for (int var10 = 1; var10 < var0 - 1; var10++) {
         var9[var10] = var3 + var10 * var6 / 100 << 16 | var4 + var10 * var7 / 100 << 8 | var5 + var10 * var8 / 100;
      }

      return var9;
   }

   protected void keyPressed(int var1) {
      this.keyCode = var1;
      PortalCanvasBase var3 = this;
      if (this.keyCode == this.client.leftSoftKeyCode) {
         this.keyCode = -506;
      } else if (this.keyCode == this.client.rightSoftKeyCode) {
         this.keyCode = -507;
      } else {
         try {
            var3.gameAction = var3.getGameAction(var3.keyCode);
         } catch (Exception var2) {
            var2.printStackTrace();
         }

         switch (this.gameAction) {
            case 1:
               if (this.keyCode != 50) {
                  this.keyCode = -501;
                  return;
               }
               break;
            case 2:
               if (this.keyCode != 52) {
                  this.keyCode = -503;
                  return;
               }
            case 3:
            case 4:
            case 7:
            default:
               break;
            case 5:
               if (this.keyCode != 54) {
                  this.keyCode = -504;
                  return;
               }
               break;
            case 6:
               if (this.keyCode != 56) {
                  this.keyCode = -502;
                  return;
               }
               break;
            case 8:
               if (this.keyCode != 53) {
                  this.keyCode = -505;
               }
         }
      }
   }

   protected void pointerPressed(int var1, int var2) {
      this.isPointerPressed = true;
      this.pointerX = var1;
      this.pointerY = var2;
   }

   private void processKey() {
      if (this.keyCode != -9999) {
         if (this.isDialogOpen) {
            label27:
            switch (this.keyCode) {
               case -507:
                  switch (this.rightSoftKey) {
                     case 1:
                        this.isDialogOpen = false;
                        this.dialogLines = null;
                        this.setSoftKeys(this.prevLeftSoftKey, this.prevRightSoftKey);
                     default:
                        break label27;
                  }
               case -506:
                  switch (this.leftSoftKey) {
                     case 0:
                        if (this.isLogoutPending) {
                           this.isLogoutPending = false;
                           this.isDialogOpen = false;
                           this.dialogLines = null;
                           this.setSoftKeys(this.prevLeftSoftKey, this.prevRightSoftKey);
                           this.manager.clearUserData();
                           this.manager.sendCommand(102);
                        } else if (this.isEmbeddedMode) {
                           this.closePortal();
                           this.client.display.setCurrent(this.client.gameScreen);
                        } else {
                           PortalClient.saveSettings(PortalProtocol.serializeMap(this.client.settings));
                           this.client.midlet.notifyDestroyed();
                        }
                        break;
                     case 6:
                        this.isDialogOpen = false;
                        this.dialogLines = null;
                        this.setSoftKeys(this.prevLeftSoftKey, this.prevRightSoftKey);
                  }
            }
         } else {
            this.handleKey();
         }

         this.keyCode = -9999;
         this.gameAction = -9999;
      }
   }

   protected abstract void handleKey();

   private void clearPointer() {
      this.isPointerPressed = false;
      this.pointerX = -1;
      this.pointerY = -1;
   }

   public abstract void closePortal();

   public abstract void showMessage(String var1);

   public final void showConfirm(String var1, int var2) {
      this.setSoftKeys((byte)0, (byte)1);
      this.dialogLines = PortalTextUtil.wrapText(var1, font, screenWidth - 20);
      this.isDialogOpen = true;
   }

   private void drawDialog(Graphics var1) {
      int var2 = (lineHeight + 2) * (this.dialogLines.length + 1) + 2;
      int var3 = screenHeight - var2 - this.footerHeight;
      var1.setColor(666180);
      var1.drawLine(0, var3, screenWidth, var3);
      var3++;
      var1.setColor(2448780);
      var1.drawLine(0, var3, screenWidth, var3);
      fillGradient(var1, 0, ++var3, screenWidth, var2 - 2, this.dialogGradient);
      var3 += (lineHeight + 2) / 2;

      for (int var4 = 0; var4 < this.dialogLines.length; var4++) {
         if (var4 == 0) {
            drawOutlinedText(var1, "Gợi ý : " + this.dialogLines[var4], 12, var3, 6177536, 16770602, 20);
         } else {
            drawOutlinedText(var1, this.dialogLines[var4], 24, var3, 6177536, 16770602, 20);
         }

         var3 += lineHeight + 2;
      }
   }

   private static boolean isInSoftKeyArea(int var0, int var1, int var2, int var3, int var4, int var5) {
      return var0 >= var2 && var0 <= var2 + 25 && var1 >= var3 && var1 <= var3 + var5;
   }

   protected final void releaseResources() {
      this.isRunning = false;
      this.isInitialized = false;
      this.isDialogOpen = false;
      PortalClient.isPortalShown = false;
      this.dialogLines = null;
      pageImages.removeAllElements();
      pageImages = null;
      this.footerGradient = null;
      this.footerEdgeGradient = null;
      this.progressGradient = null;
      this.dialogGradient = null;
      this.highlightGradient = null;
      this.bodyGradient = null;
   }
}
