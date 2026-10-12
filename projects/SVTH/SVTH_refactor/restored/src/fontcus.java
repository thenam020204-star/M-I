import java.io.DataInputStream;
import java.util.Hashtable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class fontcus {
   public static byte fontHeight;
   public static int unusedNhungW;
   public static Hashtable charIndex;
   public static int[] glyphW;
   public static byte[][] glyphBits;
   public static int[] glyphOff;
   private static final int MAPLEN = 8448;
   public static short[] charMap;
   private static final int CACHE = 6;
   private static final int[] cacheCol = new int[6];
   private static final Image[] cacheImg = new Image[6];
   public static int cacheN;
   public static int atlasW;
   public static boolean atlasOk = true;

   public fontcus() {
   }

   private static int idxOf(int var0) {
      return var0 >= 0 && var0 < 8448 ? charMap[var0] : -1;
   }

   private static Image atlas(int var0) {
      for (int var1 = 0; var1 < cacheN; var1++) {
         if (cacheCol[var1] == var0) {
            Image var2 = cacheImg[var1];
            if (var1 != 0) {
               System.arraycopy(cacheCol, 0, cacheCol, 1, var1);
               System.arraycopy(cacheImg, 0, cacheImg, 1, var1);
               cacheCol[0] = var0;
               cacheImg[0] = var2;
            }

            return var2;
         }
      }

      if (atlasOk && glyphBits != null) {
         Image var12;
         try {
            byte var3 = fontHeight;
            int var4 = atlasW;
            int[] var5 = new int[var4 * var3];
            int var6 = 0xFF000000 | var0;

            for (int var7 = 0; var7 < var3; var7++) {
               byte[] var8 = glyphBits[var7];
               int var9 = var7 * var4;

               for (int var10 = 0; var10 < var4; var10++) {
                  if (var8[var10] != 0) {
                     var5[var9 + var10] = var6;
                  }
               }
            }

            var12 = Image.createRGBImage(var5, var4, var3, true);
         } catch (Throwable var11) {
            atlasOk = false;
            return null;
         }

         if (var12 == null) {
            atlasOk = false;
            return null;
         } else {
            int var13 = cacheN < 6 ? cacheN : 5;
            System.arraycopy(cacheCol, 0, cacheCol, 1, var13);
            System.arraycopy(cacheImg, 0, cacheImg, 1, var13);
            cacheCol[0] = var0;
            cacheImg[0] = var12;
            if (cacheN < 6) {
               cacheN++;
            }

            return var12;
         }
      } else {
         return null;
      }
   }

   private static int drawCharRGB(Graphics var0, int var1, int var2, int var3) {
      int var4 = glyphW[var1];
      int var5 = glyphOff[var1];
      byte var6 = fontHeight;
      int[] var7 = new int[var4 * var6];
      int var8 = 0xFF000000 | var0.getColor();

      for (int var9 = 0; var9 < var6; var9++) {
         byte[] var10 = glyphBits[var9];
         int var11 = var9 * var4;

         for (int var12 = 0; var12 < var4; var12++) {
            if (var10[var12 + var5] != 0) {
               var7[var11 + var12] = var8;
            }
         }
      }

      var0.drawRGB(var7, 0, var4, var2, var3, var4, var6, true);
      return var4;
   }

   public static boolean XuongDong(int var0, int var1, int var2, int var3) {
      if (var1 > var2) {
         return true;
      } else {
         return var0 != 32 ? false : var2 - var3 < 35;
      }
   }

   public static boolean XuongDong2(String var0, int var1, int var2, int var3, int var4) {
      if (var0 == null) {
         return false;
      } else {
         int var5 = var0.length();
         if (var1 >= 0 && var1 < var5) {
            char var6 = var0.charAt(var1);
            if (doChu(var2, var6) > var4) {
               return true;
            }

            if (var2 > var3 && var1 > 0 && var6 != ' ' && var0.charAt(var1 - 1) == ' ') {
               while (var1 < var5) {
                  char var7 = var0.charAt(var1);
                  if (var7 == ' ') {
                     break;
                  }

                  var2 = doChu(var2, var7);
                  if (var2 > var4) {
                     return true;
                  }

                  var1++;
               }
            }
         }

         return false;
      }
   }

   public static final int drawChar(Graphics var0, char var1, int var2, int var3) {
      var3 += 0;
      int var4 = idxOf(var1);
      if (var4 < 0) {
         return 0;
      } else {
         Image var5 = atlas(var0.getColor() & 16777215);
         if (var5 == null) {
            return drawCharRGB(var0, var4, var2, var3);
         } else {
            int var6 = glyphW[var4];
            var0.drawRegion(var5, glyphOff[var4], 0, var6, fontHeight, 0, var2, var3, 20);
            return var6;
         }
      }
   }

   public static short strWidth(String var0) {
      if (var0 == null) {
         return 0;
      } else {
         short[] var1 = charMap;
         int[] var2 = glyphW;
         if (var1 != null && var2 != null) {
            int var3 = var0.length();
            int var4 = 0;

            for (int var5 = 0; var5 < var3; var5++) {
               char var6 = var0.charAt(var5);
               if (var6 < 8448) {
                  short var7 = var1[var6];
                  if (var7 >= 0) {
                     var4 += var2[var7];
                  }
               }
            }

            return (short)var4;
         } else {
            return 0;
         }
      }
   }

   public static final void drawString(Graphics var0, String var1, int var2, int var3) {
      var3 += 0;
      if (var1 != null) {
         int var4 = var1.length();
         if (var4 != 0) {
            short[] var5 = charMap;
            int[] var6 = glyphW;
            int[] var7 = glyphOff;
            if (var5 != null) {
               Image var8 = atlas(var0.getColor() & 16777215);
               byte var9 = fontHeight;

               for (int var10 = 0; var10 < var4; var10++) {
                  char var11 = var1.charAt(var10);
                  if (var11 < 8448) {
                     short var12 = var5[var11];
                     if (var12 >= 0) {
                        if (var8 != null) {
                           int var13 = var6[var12];
                           var0.drawRegion(var8, var7[var12], 0, var13, var9, 0, var2, var3, 20);
                           var2 += var13;
                        } else {
                           var2 += drawCharRGB(var0, var12, var2, var3);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static short charWidth(char var0) {
      int var1 = idxOf(var0);
      return var1 >= 0 ? (short)glyphW[var1] : 0;
   }

   public static final int doChu(int var0, int var1) {
      int var2 = idxOf(var1);
      return var2 >= 0 ? var0 + glyphW[var2] : var0 + 12;
   }

   public static void drawUniversal(Graphics var0, char var1, int var2, int var3, int var4) {
      drawUniversal(var0, String.valueOf(var1), var2, var3, var4);
   }

   public static void drawUniversal(Graphics var0, String var1, int var2, int var3, int var4) {
      if (var1 != null) {
         if ((var4 & 9) != 0) {
            short var5 = strWidth(var1);
            if ((var4 & 1) != 0) {
               var2 -= var5 >> 1;
            }

            if ((var4 & 8) != 0) {
               var2 -= var5;
            }
         }

         if ((var4 & 2) != 0) {
            var3 -= 8;
         }

         if ((var4 & 32) != 0) {
            var3 -= 16;
         }

         drawString(var0, var1, var2, var3);
      }
   }

   static {
      try {
         charIndex = new Hashtable();
         DataInputStream var0 = new DataInputStream("".getClass().getResourceAsStream("/font.bin"));
         String var1 = var0.readUTF();
         byte var2 = var0.readByte();
         fontHeight = var2;
         int var3 = var1.length();
         glyphW = new int[var3];
         glyphOff = new int[var3];
         charMap = new short[8448];

         for (int var4 = 0; var4 < 8448; var4++) {
            charMap[var4] = -1;
         }

         byte var12 = 0;

         for (int var5 = 0; var5 < var3; var5++) {
            byte var6 = var0.readByte();
            glyphW[var5] = var6;
            glyphOff[var5] = var12;
            char var7 = var1.charAt(var5);
            if (var7 < 8448) {
               charMap[var7] = (short)var5;
            }

            charIndex.put(new Integer(var7), new Integer(var5));
            var12 += var6;
         }

         atlasW = var12;
         byte[][] var13 = new byte[var2][];

         for (int var14 = 0; var14 < var2; var14++) {
            var13[var14] = new byte[var12];
         }

         glyphBits = var13;
         byte var15 = 0;
         int var16 = 7;

         for (int var8 = 0; var8 < var2; var8++) {
            byte[] var9 = var13[var8];

            for (int var10 = 0; var10 < var12; var10++) {
               if (++var16 >= 8) {
                  var15 = var0.readByte();
                  var16 = 0;
               }

               if ((var15 & 1) != 0) {
                  var9[var10] = 1;
               }

               var15 >>= 1;
            }
         }

         var0.close();
         unusedNhungW = strWidth("nhung");
      } catch (Exception var11) {
         var11.printStackTrace();
      }
   }
}
