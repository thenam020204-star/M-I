import java.io.UnsupportedEncodingException;
import javax.microedition.lcdui.Image;

public final class ResourceCache {
   private static AvAnimation[] animCache;
   private static String[] animCachePaths;
   private static boolean[] a_AZ;
   private static boolean[] b_AZ;
   private static byte[] animCacheGroups;
   private static Image[] imageCache;
   private static String[] imageCachePaths;
   private static byte[] imageCacheGroups;
   private static byte[][] bytesCache;
   private static String[] bytesCachePaths;
   private static byte[] bytesCacheGroups;
   private static String[] textCacheValues;
   private static String[] textCachePaths;
   private static byte[] d_AB;
   private static int imageCachePixels;

   public ResourceCache() {
   }

   public static void releaseGroup(int var0) {
      int var1 = 0;

      for (int var2 = 0; animCache != null && var2 < animCache.length; var2++) {
         if (animCacheGroups[var2] == var0) {
            animCache[var2] = null;
            animCachePaths[var2] = null;
            a_AZ[var2] = false;
            b_AZ[var2] = false;
         }

         if (animCache[var2] != null) {
            var1++;
         }
      }

      if (animCache != null && var1 < animCache.length) {
         if (var1 == 0) {
            animCache = null;
            animCachePaths = null;
            a_AZ = null;
            b_AZ = null;
            animCacheGroups = null;
         } else {
            AvAnimation[] var15 = new AvAnimation[var1];
            String[] var3 = new String[var1];
            byte[] var4 = new byte[var1];
            boolean[] var5 = new boolean[var1];
            boolean[] var8 = new boolean[var1];
            int var6 = 0;

            for (int var7 = 0; animCache != null && var7 < animCache.length; var7++) {
               if (animCache[var7] != null) {
                  var15[var6] = animCache[var7];
                  var3[var6] = animCachePaths[var7];
                  var4[var6] = animCacheGroups[var7];
                  var5[var6] = a_AZ[var7];
                  var8[var6] = b_AZ[var7];
                  var6++;
               }
            }

            animCache = var15;
            animCachePaths = var3;
            animCacheGroups = var4;
            a_AZ = var5;
            b_AZ = var8;
         }
      }

      var1 = 0;

      for (int var16 = 0; imageCache != null && var16 < imageCache.length; var16++) {
         if (imageCacheGroups[var16] == var0) {
            if (imageCache[var16] != null) {
               imageCachePixels = imageCachePixels - imageCache[var16].getWidth() * imageCache[var16].getHeight();
               imageCache[var16] = null;
            }

            imageCachePaths[var16] = null;
         }

         if (imageCache[var16] != null) {
            var1++;
         }
      }

      if (imageCache != null && var1 < imageCache.length) {
         if (var1 == 0) {
            imageCache = null;
            imageCachePaths = null;
            imageCacheGroups = null;
         } else {
            Image[] var17 = new Image[var1];
            String[] var22 = new String[var1];
            byte[] var10 = new byte[var1];
            int var25 = 0;

            for (int var28 = 0; imageCache != null && var28 < imageCache.length; var28++) {
               if (imageCache[var28] != null) {
                  var17[var25] = imageCache[var28];
                  var22[var25] = imageCachePaths[var28];
                  var10[var25] = imageCacheGroups[var28];
                  var25++;
               }
            }

            imageCache = var17;
            imageCachePaths = var22;
            imageCacheGroups = var10;
         }
      }

      var1 = 0;

      for (int var18 = 0; bytesCache != null && var18 < bytesCache.length; var18++) {
         if (bytesCacheGroups[var18] == var0) {
            bytesCache[var18] = null;
            bytesCachePaths[var18] = null;
         }

         if (bytesCache[var18] != null) {
            var1++;
         }
      }

      if (bytesCache != null && var1 < bytesCache.length) {
         if (var1 == 0) {
            bytesCache = null;
            bytesCachePaths = null;
            bytesCacheGroups = null;
         } else {
            byte[][] var19 = new byte[var1][];
            String[] var23 = new String[var1];
            byte[] var26 = new byte[var1];
            int var29 = 0;

            for (int var12 = 0; bytesCache != null && var12 < bytesCache.length; var12++) {
               if (bytesCache[var12] != null) {
                  var19[var29] = bytesCache[var12];
                  var23[var29] = bytesCachePaths[var12];
                  var26[var29] = bytesCacheGroups[var12];
                  var29++;
               }
            }

            bytesCache = var19;
            bytesCachePaths = var23;
            bytesCacheGroups = var26;
         }
      }

      var1 = 0;

      for (int var20 = 0; textCacheValues != null && var20 < textCacheValues.length; var20++) {
         if (d_AB[var20] == var0) {
            textCacheValues[var20] = null;
            textCachePaths[var20] = null;
         }

         if (textCacheValues[var20] != null) {
            var1++;
         }
      }

      if (textCacheValues != null && var1 < textCacheValues.length) {
         if (var1 == 0) {
            textCacheValues = null;
            textCachePaths = null;
            d_AB = null;
         } else {
            String[] var21 = new String[var1];
            String[] var24 = new String[var1];
            byte[] var14 = new byte[var1];
            int var27 = 0;

            for (int var30 = 0; textCacheValues != null && var30 < textCacheValues.length; var30++) {
               if (textCacheValues[var30] != null) {
                  var21[var27] = textCacheValues[var30];
                  var24[var27] = textCachePaths[var30];
                  var14[var27] = d_AB[var30];
                  var27++;
               }
            }

            textCacheValues = var21;
            textCachePaths = var24;
            d_AB = var14;
         }
      }

      Utils.clearImageSliceCache();
   }

   public static void clearAll() {
      animCache = null;
      a_AZ = null;
      b_AZ = null;
      animCacheGroups = null;
      animCachePaths = null;
      imageCache = null;
      imageCachePaths = null;
      imageCacheGroups = null;
      imageCachePixels = 0;
      bytesCache = null;
      bytesCachePaths = null;
      bytesCacheGroups = null;
      textCacheValues = null;
      textCachePaths = null;
      d_AB = null;
      Utils.clearImageSliceCache();
   }

   public static AvAnimation getAnimation(
      String var0, String var1, String[] var2, String[] var3, int[] var4, int[] var5, int var6, boolean var7, boolean var8, int var9
   ) {
      AvAnimation var10 = null;
      boolean var11 = false;
      if (!DeviceConfig.useImgColorTable || !DeviceConfig.modelName.equals("N7610")) {
         for (int var12 = 0; animCache != null && var12 < animCache.length; var12++) {
            if (animCache[var12] != null
               && animCache[var12].path.equals(var0)
               && (DeviceConfig.cutAvImg > 0 && var7 == a_AZ[var12] && !b_AZ[var12] || DeviceConfig.cutAvImg == 0)) {
               if ((var10 = animCache[var12]).cutMode != DeviceConfig.cutAvImg) {
                  var10.loadImages(var1, null, null, null, null, -1, var7, false, var9);
               }

               var11 = true;
               break;
            }
         }
      }

      if (!var11 && (var10 = new AvAnimation()).load(var0)) {
         var10.loadImages(var1, null, null, null, null, -1, var7, false, var9);
         if (var9 >= 0 && (!DeviceConfig.useImgColorTable || !DeviceConfig.modelName.equals("N7610"))) {
            if (animCache == null) {
               animCache = new AvAnimation[1];
            } else {
               AvAnimation[] var13 = new AvAnimation[animCache.length + 1];
               System.arraycopy(animCache, 0, var13, 0, animCache.length);
               animCache = var13;
            }

            animCache[animCache.length - 1] = var10;
            animCachePaths = Utils.append(animCachePaths, var0);
            a_AZ = Utils.append(a_AZ, var7);
            b_AZ = Utils.append(b_AZ, false);
            animCacheGroups = Utils.append(animCacheGroups, var9);
         }
      }

      return var10;
   }

   public static Image loadImage(String var0) {
      return loadImage(var0, 0);
   }

   public static Image loadImage(String var0, int var1) {
      Image var2 = null;
      boolean var3 = false;
      String var4 = var0.substring(var0.lastIndexOf(47) + 1);
      var4 = "/pub/" + var4;
      if (!Utils.imageExists(var0)) {
         var0 = var4;
      }

      for (int var6 = 0; imageCachePaths != null && var6 < imageCachePaths.length; var6++) {
         if (imageCachePaths[var6] != null && Utils.isSameImagePath(imageCachePaths[var6], var0)) {
            var2 = imageCache[var6];
            var3 = true;
            break;
         }
      }

      if (!var3) {
         if ((var2 = Utils.loadImage(var0)) == null) {
            return null;
         }

         if (DeviceConfig.debugMode) {
            System.out.println("Tải ảnh mới:" + var0);
         }

         if (var1 >= 0) {
            if (imageCache == null) {
               imageCache = new Image[1];
            } else {
               Image[] var7 = new Image[imageCache.length + 1];
               System.arraycopy(imageCache, 0, var7, 0, imageCache.length);
               imageCache = var7;
            }

            imageCache[imageCache.length - 1] = var2;
            imageCachePaths = Utils.append(imageCachePaths, var0);
            imageCacheGroups = Utils.append(imageCacheGroups, var1);
            imageCachePixels = imageCachePixels + var2.getWidth() * var2.getHeight();
         }
      }

      return var2;
   }

   public static Image createImageUncached(String var0, int var1) {
      byte[] var2;
      return Image.createImage(var2 = loadBytes(var0, 2), 0, var2.length);
   }

   public static byte[] loadBytes(String var0, int var1) {
      byte[] var2 = null;
      boolean var3 = false;

      for (int var4 = 0; bytesCachePaths != null && var4 < bytesCachePaths.length; var4++) {
         if (bytesCachePaths[var4] != null && Utils.isSameImagePath(bytesCachePaths[var4], var0)) {
            var2 = new byte[bytesCache[var4].length];
            System.arraycopy(bytesCache[var4], 0, var2, 0, bytesCache[var4].length);
            var2 = var2;
            var3 = true;
            break;
         }
      }

      if (!var3) {
         if (!var0.toLowerCase().endsWith(".gf") && !var0.toLowerCase().endsWith(".png") && !var0.toLowerCase().endsWith(".jpg")) {
            var2 = Utils.readResource(var0);
         } else {
            var2 = Utils.loadImageBytes(var0);
         }

         if (var2 == null) {
            return null;
         }

         if (var1 >= 0) {
            if (bytesCache == null) {
               bytesCache = new byte[1][];
            } else {
               byte[][] var6 = new byte[bytesCache.length + 1][];
               System.arraycopy(bytesCache, 0, var6, 0, bytesCache.length);
               bytesCache = var6;
            }

            bytesCache[bytesCache.length - 1] = var2;
            bytesCachePaths = Utils.append(bytesCachePaths, var0);
            bytesCacheGroups = Utils.append(bytesCacheGroups, var1);
         }
      }

      return var2;
   }

   public static String loadText(String var0, boolean var1, int var2) {
      var0 = vh.rn(var0);
      String var3 = null;
      String var4 = var0;
      boolean var5 = false;

      for (int var6 = 0; textCachePaths != null && var6 < textCachePaths.length; var6++) {
         if (textCachePaths[var6] != null && textCachePaths[var6].equals(var0)) {
            var3 = textCacheValues[var6];
            var5 = true;
            break;
         }
      }

      if (!var5) {
         if (var0 == null || var0.equals("")) {
            return null;
         }

         if (var1 && var0.endsWith(".txt")) {
            var0 = var0.substring(0, var0.length() - 4) + ".bin";
         }

         byte[] var16;
         if ((var16 = Utils.readResource(var0)) == null) {
            if (!var0.toLowerCase().endsWith(".bin")) {
               if ((var1 = var0.lastIndexOf(46)) > 0) {
                  var0 = var0.substring(0, var1) + ".bin";
               }

               var16 = Utils.readResource(var0);
            } else {
               if (!var0.toLowerCase().endsWith(".bin")) {
                  return null;
               }

               if ((var1 = var0.lastIndexOf(46)) > 0) {
                  var0 = var0.substring(0, var1) + ".txt";
               }

               var16 = Utils.readResource(var0);
            }
         }

         if (var16 == null) {
            return null;
         }

         if (!var0.toLowerCase().endsWith(".txt")) {
            if (Utils.configKey != null) {
               short var14 = 0;

               for (int var10 = 0; var10 < var16.length; var10++) {
                  var16[var10] -= Utils.configKey[var14];
                  if (var14 < Utils.configKey.length - 1) {
                     var14++;
                  } else {
                     var14 = 0;
                  }
               }
            } else {
               for (int var13 = 0; var13 < var16.length; var13++) {
                  var16[var13] = (byte)(var16[var13] + 10);
               }
            }
         }

         try {
            var3 = new String(var16, "UTF-8");
         } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         if (var2 >= 0) {
            if (textCacheValues == null) {
               textCacheValues = new String[1];
            } else {
               String[] var15 = new String[textCacheValues.length + 1];
               System.arraycopy(textCacheValues, 0, var15, 0, textCacheValues.length);
               textCacheValues = var15;
            }

            textCacheValues[textCacheValues.length - 1] = var3;
            textCachePaths = Utils.append(textCachePaths, var4);
            d_AB = Utils.append(d_AB, var2);
         }
      }

      return var3;
   }
}
