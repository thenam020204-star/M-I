import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

public final class PortalClient {
   private static RecordStore store = null;
   private String qua;
   private String screenSize;
   public int screenWidth;
   public int screenHeight;
   private String bigFontSize;
   private String midFontSize;
   private String smallFontSize;
   public boolean hasPointer;
   public Hashtable settings = this.loadSettings();
   public Display display;
   public Displayable gameScreen;
   public MIDlet midlet;
   public final int leftSoftKeyCode;
   public final int rightSoftKeyCode;
   public int gameId;
   public int f_I;
   public int platformId;
   public String a_String;
   private PortalCanvasBase canvas;
   private PortalListener listener;
   public int pendingCommand;
   public static boolean isPortalShown = false;

   public PortalClient(
      MIDlet var1, Display var2, Displayable var3, PortalListener var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, String var12
   ) {
      if (this.settings == null) {
         this.settings = new Hashtable();
      }

      int var13;
      String var18;
      if ((var13 = (var18 = System.getProperty("microedition.platform")).indexOf(47)) > 0) {
         var18 = var18.substring(0, var13 - 1);
      }

      this.qua = "SN2010-" + var18 + "/" + 4;
      this.display = var2;
      this.gameScreen = var3;
      this.midlet = var1;
      this.screenWidth = var5;
      this.screenHeight = var6;
      this.leftSoftKeyCode = var7;
      this.rightSoftKeyCode = var8;
      this.gameId = var9;
      this.f_I = var10;
      this.platformId = 888888;
      this.a_String = var12;
      this.screenSize = this.screenWidth + "X" + this.screenHeight;
      String var14 = "Quốc";
      Font var15 = Font.getFont(0, 0, 16);
      this.bigFontSize = vh.stringWidth(var15, var14) + "X" + vh.getHeight(var15);
      Font var16 = Font.getFont(0, 0, 0);
      this.midFontSize = vh.stringWidth(var16, var14) + "X" + vh.getHeight(var16);
      Font var17 = Font.getFont(0, 0, 8);
      this.smallFontSize = vh.stringWidth(var17, var14) + "X" + vh.getHeight(var17);
      this.listener = var4;
      this.canvas = null;
      this.canvas = new PortalBrowser(this);
      this.canvas.setListener(this.listener);
   }

   public final String getQua() {
      return this.qua;
   }

   public final String getScreenSize() {
      return this.screenSize;
   }

   public final String getBigFontSize() {
      return this.bigFontSize;
   }

   public final String getMidFontSize() {
      return this.midFontSize;
   }

   public final String getSmallFontSize() {
      return this.smallFontSize;
   }

   public final boolean isLoggedIn() {
      return this.settings.get("_uid") != null && this.settings.get("_uid").toString().length() > 0;
   }

   public final int a() {
      this.canvas.setMode(2);
      return 1;
   }

   private void showPortal() {
      if (this.display.getCurrent() != this.canvas) {
         isPortalShown = true;
         if (this.canvas.isEmbeddedMode) {
            this.canvas.initialize();
         }

         this.display.setCurrent(this.canvas);
         this.canvas.repaintThread = new Thread(this.canvas);
         this.canvas.isRunning = true;
         this.canvas.repaintThread.start();
         this.canvas.manager.startNetwork();
      }
   }

   public static void saveSettings(byte[] var0) {
      try {
         openStore().setRecord(1, var0, 0, var0.length);
      } catch (Exception var1) {
         var1.printStackTrace();
      }
   }

   private static RecordStore openStore() {
      if (store == null) {
         store = RecordStore.openRecordStore("mboxclient", true);
         byte[] var0 = new byte[0];
         store.addRecord(var0, 0, 0);
      }

      return store;
   }

   private Hashtable loadSettings() {
      Hashtable var1 = new Hashtable();
      byte[] var2;
      if ((var2 = readSettingsRecord()) == null) {
         var1.put("_vCInterval", "5");
         var1.put("_vTInterval", "3");
         var1.put("_vertifyValue", "0");
         var1.put("_lastvTime", "0");
         return var1;
      } else {
         try {
            ByteArrayInputStream var8 = new ByteArrayInputStream(var2);
            DataInputStream var9;
            short var3 = (var9 = new DataInputStream(var8)).readShort();

            for (int var4 = 0; var4 < var3; var4++) {
               byte var5;
               String var11 = decodeUtf(readBytes(var5 = var9.readByte(), var9), var5);
               short var6;
               String var12 = decodeUtf(readBytes(var6 = var9.readShort(), var9), var6);
               var1.put(var11, var12);
            }
         } catch (Exception var7) {
         }

         String var10;
         if ((var10 = (String)var1.get("_server")) != null && var10.length() > 0) {
            HttpRequest.portalServerUrl = var10;
         }

         return var1;
      }
   }

   private static byte[] readSettingsRecord() {
      byte[] var0 = null;

      try {
         var0 = openStore().getRecord(1);
      } catch (Exception var1) {
         var1.printStackTrace();
      }

      return var0 != null && var0.length != 0 ? var0 : null;
   }

   // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private static String decodeUtf(byte[] var0, int var1) {
      String var2 = "";
      ByteArrayInputStream var3 = null;
      DataInputStream var4 = null;
      boolean var15 = false /* VF: Semaphore variable */;

      label115: {
         try {
            var15 = true;
            byte[] var5;
            (var5 = new byte[var1 + 2])[0] = (byte)(var1 >> 8);
            var5[1] = (byte)var1;
            System.arraycopy(var0, 0, var5, 2, var1);
            var3 = new ByteArrayInputStream(var5);
            var2 = (var4 = new DataInputStream(var3)).readUTF();
            var4.close();
            var4 = null;
            var3.close();
            var3 = null;
            var15 = false;
            return var2;
         } catch (IOException var22) {
            var15 = false;
         } catch (Exception var23) {
            var15 = false;
            break label115;
         } finally {
            if (var15) {
               if (var4 != null) {
                  try {
                     var4.close();
                  } catch (IOException var17) {
                  }
               }

               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (IOException var16) {
                  }
               }
            }
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var21) {
            }
         }

         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var20) {
            }

            return var2;
         }

         return var2;
      }

      if (var4 != null) {
         try {
            var4.close();
         } catch (IOException var19) {
         }
      }

      if (var3 != null) {
         try {
            var3.close();
         } catch (IOException var18) {
         }
      }

      return var2;
   }

   private static byte[] readBytes(int var0, DataInputStream var1) {
      if (var0 > 0 && var1 != null) {
         byte[] var2 = null;

         try {
            var2 = new byte[var0];

            for (int var3 = 0; var3 < var0; var3++) {
               var2[var3] = var1.readByte();
            }
         } catch (IOException var4) {
         }

         return var2;
      } else {
         return null;
      }
   }

   protected final void openPortal(int var1, int var2) {
      switch (var2) {
         case 17:
            if ((var2 = this.checkPrecondition(var2)) == -1) {
               this.listener.onPortalResult(1, new PortalResult(0, 0, null));
               return;
            }
            break;
         case 18:
            if ((var2 = this.checkPrecondition(var2)) == -1) {
               this.canvas.closePortal();
               this.listener.onPortalResult(2, new PortalResult(0, 0, null));
               return;
            }
      }

      this.showPortal();
      this.pendingCommand = var2;
      if (var1 == 2) {
         this.canvas.setMode(0);
      } else {
         ((PortalBrowser)this.canvas).requestCommand(var2);
         this.canvas.setMode(1);
      }
   }

   private int checkPrecondition(int var1) {
      int var2 = -1;
      switch (var1) {
         case 17:
            String var12 = (String)this.settings.get("_uid");
            String var13 = (String)this.settings.get("_sid");
            if (var12 == null || var12.length() == 0 || var13 == null || var13.length() == 0) {
               var2 = var1;
            }

            return var2;
         case 18:
            String var7 = (String)this.settings.get("_vertifyUser");
            String var3 = (String)this.settings.get("_vertifyValue");
            String var4 = (String)this.settings.get("_lastvTime");
            String var5 = (String)this.settings.get("_vCInterval");
            String var6 = (String)this.settings.get("_vTInterval");
            if (var7 == null || Integer.parseInt(var7) == 0) {
               return var1;
            } else if ((var2 = Integer.parseInt(var3)) >= Integer.parseInt(var5)) {
               return var1;
            } else {
               Calendar var10;
               int var11;
               if ((var11 = (var10 = Calendar.getInstance()).get(1) * 10000 + (var10.get(2) + 1) * 100 + var10.get(5) - Integer.parseInt(var4))
                     >= Integer.parseInt(var6)
                  || var11 < 0) {
                  return var1;
               } else {
                  this.settings.put("_vertifyValue", String.valueOf(++var2));
               }
            }
         default:
            return -1;
      }
   }

   protected final void sendCommand(int var1) {
      this.canvas.manager.startNetwork();
      this.canvas.manager.sendCommand(var1);
   }

   protected final void uploadScore(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
      this.showPortal();
      ((PortalBrowser)this.canvas).showMessagePage("Đang kết nối... Vui lòng đợi!");
      this.canvas.setMode(1);
      this.canvas.manager.uploadScore(var1, var2, var3, var4, var5, var6, var7);
   }

   public final PortalBrowser getBrowser() {
      return (PortalBrowser)this.canvas;
   }

   public final PortalCanvasBase getCanvas() {
      return this.canvas;
   }

   public final QQUserProfile getUserProfile() {
      return this.canvas.manager.getUserProfile();
   }
}
