import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Building {
   public byte kind;
   private Image image;
   public AnimSprite anim;
   private short[] hitRects;
   public short[] bounds;

   public Building() {
   }

   public static boolean isGenuineCopy() {
      if (!DeviceConfig.needCopyRight) {
         return true;
      } else {
         boolean var0 = true;
         if (!DeviceConfig.allowDebug) {
            if (Runtime.getRuntime().totalMemory() == 8000000L) {
               var0 = false;
            }

            if (ChannelConfig.isKemu && !noCrackClassFound()) {
               var0 = false;
            }
         }

         return var0;
      }
   }

   private static boolean noCrackClassFound() {
      boolean var0 = false;

      for (int var1 = 0; DeviceConfig.crackClassNames != null && var1 < DeviceConfig.crackClassNames.length; var1++) {
         try {
            Class.forName(DeviceConfig.crackClassNames[var1]);
            var0 = true;
            break;
         } catch (ClassNotFoundException var2) {
         }
      }

      return !var0;
   }

   public short[] getBounds() {
      return this.bounds;
   }

   public Building(Image var1, short[] var2) {
      this.kind = 0;
      this.image = var1;
      this.hitRects = var2;
      if (this.image != null) {
         this.bounds = new short[]{
            (short)(-this.image.getWidth() / 2), (short)(-this.image.getHeight()), (short)this.image.getWidth(), (short)this.image.getHeight()
         };
      }
   }

   public Building(AnimSprite var1, short[] var2) {
      this.kind = 1;
      this.anim = var1;
      if (this.anim != null) {
         this.bounds = this.anim.anim.getBounds();
      }

      this.hitRects = var2;
   }

   public void draw(Graphics var1, int var2, int var3) {
      switch (this.kind) {
         case 0:
            if (this.image != null) {
               var1.drawImage(this.image, var2, var3, 33);
               return;
            }
            break;
         case 1:
            if (this.anim != null) {
               this.anim.x = (short)var2;
               this.anim.y = (short)var3;
               this.anim.draw(var1);
            }
      }
   }

   public void drawDebugRects(Graphics var1, int var2, int var3) {
      var1.setColor(16711680);
      switch (this.kind) {
         case 0:
            for (byte var5 = 0; this.hitRects != null; var5 += 4) {
               if (var5 >= this.hitRects.length) {
                  return;
               }

               var1.drawRect(var2 + this.hitRects[var5], var3 + this.hitRects[var5 + 1], this.hitRects[var5 + 2], this.hitRects[var5 + 3]);
            }
            break;
         case 1:
            if (this.hitRects != null) {
               for (byte var4 = 0; this.hitRects != null; var4 += 4) {
                  if (var4 >= this.hitRects.length) {
                     return;
                  }

                  var1.drawRect(var2 + this.hitRects[var4], var3 + this.hitRects[var4 + 1], this.hitRects[var4 + 2], this.hitRects[var4 + 3]);
               }
            } else if (this.anim != null) {
               this.anim.x = (short)var2;
               this.anim.y = (short)var3;
               this.anim.drawDebugBoxes(var1);
            }
      }
   }

   public short[] getBoxesAt(int var1, int var2, int var3) {
      short[] var4 = null;
      switch (this.kind) {
         case 0:
            if (this.image != null && this.hitRects != null) {
               var4 = new short[this.hitRects.length];
               System.arraycopy(this.hitRects, 0, var4, 0, this.hitRects.length);

               for (byte var5 = 0; var5 < var4.length; var5 += 4) {
                  var4[var5] = (short)(var4[var5] + var1);
                  var4[var5 + 1] = (short)(var4[var5 + 1] + var2);
               }
            }
         case 1:
            if (this.hitRects != null && (var3 == 1 || var3 == 0)) {
               var4 = new short[this.hitRects.length];
               System.arraycopy(this.hitRects, 0, var4, 0, this.hitRects.length);

               for (byte var12 = 0; var12 < var4.length; var12 += 4) {
                  var4[var12] = (short)(var4[var12] + var1);
                  var4[var12 + 1] = (short)(var4[var12 + 1] + var2);
               }
            } else if (this.anim != null) {
               boolean var6 = false;
               boolean var7 = false;
               AvAnimation var8 = this.anim.anim;
               var4 = this.anim.anim.getBoxes(0, 0, var1, var2, var3, false);
            }
      }

      return var4;
   }

   public void nextFrame() {
      if (this.kind == 1 && this.anim != null) {
         this.anim.nextFrame(true);
      }
   }
}
