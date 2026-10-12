import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class TouchSoftKeys {
   private static Vector keys;
   private static byte drawingKeyId;
   private static Image softKeyImage;
   private static Image centerKeyImage;

   public TouchSoftKeys() {
   }

   public static void setSoftKeys(int var0) {
      if (var0 == 0) {
         keys = null;
         softKeyImage = null;
      } else {
         if (var0 == 1) {
            removeKey((byte)2);
            if (!hasKey((byte)1)) {
               addLeftKey();
               return;
            }
         } else if (var0 == 2) {
            removeKey((byte)1);
            if (!hasKey((byte)2)) {
               addRightKey();
               return;
            }
         } else if (var0 == 3) {
            if (!hasKey((byte)1)) {
               addLeftKey();
            }

            if (!hasKey((byte)2)) {
               addRightKey();
            }
         }
      }
   }

   public static void draw(Graphics var0) {
      for (int var1 = 0; keys != null && var1 < keys.size(); var1++) {
         if ((drawingKeyId = ((TouchButton)keys.elementAt(var1)).buttonId) == 1) {
            if (softKeyImage != null) {
               if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                  Utils.drawFrame(var0, softKeyImage, softKeyImage.getWidth() / 2, softKeyImage.getHeight(), 0, 1, GameCanvas.instance.screenHeight - 1, 36, 0);
               } else {
                  Utils.drawFrame(
                     var0,
                     softKeyImage,
                     softKeyImage.getWidth() / 2,
                     softKeyImage.getHeight(),
                     0,
                     GameCanvas.instance.screenWidth - 1,
                     GameCanvas.instance.screenHeight - 1,
                     40,
                     0
                  );
               }
            } else {
               softKeyImage = ResourceCache.loadImage("/pic/D23.gf", 1);
            }
         }

         if (drawingKeyId == 2) {
            if (GameCanvas.screenState == 4) {
               if (GameCanvas.instance.world.mode != 1 && GameCanvas.instance.world.mode != 2 && GameCanvas.instance.world.mode != 3) {
                  if (softKeyImage != null) {
                     if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                        Utils.drawFrame(
                           var0,
                           softKeyImage,
                           softKeyImage.getWidth() / 2,
                           softKeyImage.getHeight(),
                           1,
                           GameCanvas.instance.screenWidth - 1,
                           GameCanvas.instance.screenHeight - 1,
                           40,
                           0
                        );
                     } else {
                        Utils.drawFrame(
                           var0, softKeyImage, softKeyImage.getWidth() / 2, softKeyImage.getHeight(), 1, 1, GameCanvas.instance.screenHeight - 1, 36, 0
                        );
                     }
                  } else {
                     softKeyImage = ResourceCache.loadImage("/pic/D23.gf", 1);
                  }
               }
            } else if (softKeyImage != null) {
               if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
                  Utils.drawFrame(
                     var0,
                     softKeyImage,
                     softKeyImage.getWidth() / 2,
                     softKeyImage.getHeight(),
                     1,
                     GameCanvas.instance.screenWidth - 1,
                     GameCanvas.instance.screenHeight - 1,
                     40,
                     0
                  );
               } else {
                  Utils.drawFrame(var0, softKeyImage, softKeyImage.getWidth() / 2, softKeyImage.getHeight(), 1, 1, GameCanvas.instance.screenHeight - 1, 36, 0);
               }
            } else {
               softKeyImage = ResourceCache.loadImage("/pic/D23.gf", 1);
            }
         }

         if (drawingKeyId == 5 && GameCanvas.screenState == 4 && GameCanvas.instance.world.mode == 1) {
            if (centerKeyImage != null) {
               var0.drawImage(centerKeyImage, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] - 20, 17);
            } else {
               centerKeyImage = ResourceCache.loadImage("/pic/9.gf");
            }
         }
      }
   }

   private static void addLeftKey() {
      TouchButton var0;
      (var0 = new TouchButton()).buttonId = 1;
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         var0.rect = new short[]{0, (short)(GameCanvas.instance.screenHeight - 26), 26, 26};
      } else {
         var0.rect = new short[]{(short)(GameCanvas.instance.screenWidth - 26), (short)(GameCanvas.instance.screenHeight - 26), 26, 26};
      }

      addKey(var0);
   }

   private static void addRightKey() {
      TouchButton var0;
      (var0 = new TouchButton()).buttonId = 2;
      if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
         var0.rect = new short[]{(short)(GameCanvas.instance.screenWidth - 26), (short)(GameCanvas.instance.screenHeight - 26), 26, 26};
      } else {
         var0.rect = new short[]{0, (short)(GameCanvas.instance.screenHeight - 26), 26, 26};
      }

      addKey(var0);
   }

   public static boolean hasKey(byte var0) {
      for (int var1 = 0; keys != null && var1 < keys.size(); var1++) {
         if (((TouchButton)keys.elementAt(var1)).buttonId == var0) {
            return true;
         }
      }

      return false;
   }

   private static void removeKey(byte var0) {
      int var1 = -1;

      for (int var2 = 0; keys != null && var2 < keys.size(); var2++) {
         if (((TouchButton)keys.elementAt(var2)).buttonId == var0) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         keys.removeElementAt(var1);
      }
   }

   public static void addKey(TouchButton var0) {
      if (var0 != null) {
         if (keys == null) {
            keys = new Vector(1, 1);
         }

         keys.addElement(var0);
      }
   }

   public static TouchButton hitTest(int var0, int var1) {
      TouchButton var2 = null;

      for (int var3 = 0; keys != null && var3 < keys.size(); var3++) {
         short[] var4;
         if (Utils.rectsOverlap((var4 = ((TouchButton)keys.elementAt(var3)).rect)[0], var4[1], var4[2], var4[3], var0, var1, 0, 0)) {
            var2 = (TouchButton)keys.elementAt(var3);
         }
      }

      return var2;
   }
}
