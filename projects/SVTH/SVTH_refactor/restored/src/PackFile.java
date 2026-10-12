import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class PackFile {
   private static String partNamePrefix;
   private static String packDir;
   private static int entryCount;
   private static String[] entryNames;
   private static int[] entrySizes;
   private static short[] entryFirstPart;
   private static int[] entryOffsets;
   private static Object classAnchor = new Object();

   public PackFile() {
   }

   public static boolean loadIndex(String var0) {
      InputStream var1;
      if ((var1 = classAnchor.getClass().getResourceAsStream(var0)) == null) {
         return false;
      } else {
         DataInputStream var4 = new DataInputStream(var1);
         byte[] var2 = new byte[3];

         try {
            var4.read(var2);
            if ("jzy".equals(new String(var2)) && 1 == var4.readByte()) {
               partNamePrefix = var4.readUTF();
               entryNames = new String[entryCount = var4.readInt()];
               entrySizes = new int[entryCount];
               entryFirstPart = new short[entryCount];
               entryOffsets = new int[entryCount];

               for (int var5 = 0; var5 < entryCount; var5++) {
                  entryNames[var5] = var4.readUTF();
                  entrySizes[var5] = var4.readInt();
                  entryFirstPart[var5] = (short)(var4.readByte() & 255);
                  entryOffsets[var5] = var4.readInt();
               }

               packDir = var0.substring(0, var0.lastIndexOf(47));
            }

            var4.close();
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         return true;
      }
   }

   public static synchronized byte[] readEntry(String var0) {
      if (var0.startsWith("/")) {
         var0 = var0.substring(1);
      }

      if (entryNames != null) {
         for (int var1 = 0; var1 < entryNames.length; var1++) {
            if (var0.equals(entryNames[var1])) {
               short var7 = entryFirstPart[var1];
               int var2 = entrySizes[var1];
               int var3 = 0;

               ByteArrayOutputStream var4;
               for (var4 = new ByteArrayOutputStream(); var2 > 0; var3++) {
                  String var5 = packDir + "/" + partNamePrefix + (var7 + var3);
                  Object var6 = null;
                  if ((var6 = Utils.readResource(var5, false)) == null) {
                     System.out.println("Đọc tệp gói " + var5 + " Thất bại");
                     return null;
                  }

                  if (var3 == 0) {
                     int var8 = var2 <= ((Object[])var6).length - entryOffsets[var1] ? var2 : ((Object[])var6).length - entryOffsets[var1];
                     var4.write((byte[])var6, entryOffsets[var1], var8);
                     var2 -= var8;
                  } else {
                     int var9 = var2 <= ((Object[])var6).length ? var2 : ((Object[])var6).length;
                     var4.write((byte[])var6, 0, var9);
                     var2 -= var9;
                  }
               }

               return var4.toByteArray();
            }
         }
      }

      return null;
   }

   public static boolean hasEntry(String var0) {
      if (var0 == null) {
         return false;
      } else {
         if (var0.startsWith("/")) {
            var0 = var0.substring(1);
         }

         if (entryNames != null) {
            for (int var1 = 0; var1 < entryNames.length; var1++) {
               if (var0.equals(entryNames[var1])) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
