import javax.microedition.lcdui.Font;

public final class PortalTextUtil {
   public PortalTextUtil() {
   }

   public static String maskString(int var0) {
      StringBuffer var1 = new StringBuffer();

      for (int var2 = 0; var2 < var0; var2++) {
         var1.append('*');
      }

      return var1.toString();
   }

   public static String clipToWidth(String var0, int var1, Font var2) {
      char[] var6 = var0.toCharArray();
      StringBuffer var3 = new StringBuffer();
      int var4 = 0;

      for (int var5 = 0; var5 < var6.length && (var4 += vh.charWidth(var2, var6[var5])) <= var1; var5++) {
         var3.append(var6[var5]);
      }

      return var3.toString();
   }

   public static String[] splitAmpersand(String var0) {
      String[] var1 = new String[6];
      char[] var5 = var0.toCharArray();
      int var2 = 0;
      StringBuffer var3 = new StringBuffer();

      for (int var4 = 0; var4 < var5.length; var4++) {
         if (var5[var4] == '&') {
            if (var3.length() > 0) {
               if (var2 == var1.length) {
                  var1 = resizeArray(var1, var2 + 3);
               }

               var1[var2] = var3.toString();
               var2++;
               var3.delete(0, var3.length());
            }
         } else {
            var3.append(var5[var4]);
         }
      }

      if (var3.length() > 0) {
         if (var2 == var1.length) {
            var1 = resizeArray(var1, var2 + 1);
         }

         var1[var2] = var3.toString();
         var2++;
      }

      if (var2 < var1.length) {
         var1 = resizeArray(var1, var2);
      }

      return var1;
   }

   public static String[] wrapText(String var0, Font var1, int var2) {
      String[] var3 = new String[3];
      char[] var8 = var0.toCharArray();
      int var4 = 0;
      int var5 = 0;
      StringBuffer var6 = new StringBuffer();

      for (int var7 = 0; var7 < var8.length; var7++) {
         if ((var5 += vh.charWidth(var1, var8[var7])) < var2) {
            var6.append(var8[var7]);
         } else {
            if ((var5 = vh.charWidth(var1, var8[var7])) > var2) {
               return null;
            }

            if (var4 == var3.length) {
               var3 = resizeArray(var3, var4 + 3);
            }

            var3[var4] = var6.toString();
            var4++;
            var6.delete(0, var6.length());
            var6.append(var8[var7]);
         }
      }

      if (var6.length() > 0) {
         if (var4 == var3.length) {
            var3 = resizeArray(var3, var4 + 1);
         }

         var3[var4] = var6.toString();
         var4++;
      }

      if (var4 < var3.length) {
         var3 = resizeArray(var3, var4);
      }

      return var3;
   }

   private static String[] resizeArray(String[] var0, int var1) {
      if (var1 < var0.length) {
         String[] var2 = new String[var1];

         for (int var3 = 0; var3 < var1; var3++) {
            var2[var3] = var0[var3];
         }

         var0 = new String[var2.length];

         for (int var5 = 0; var5 < var1; var5++) {
            var0[var5] = var2[var5];
         }
      } else if (var1 > var0.length) {
         String[] var4 = new String[var0.length];

         for (int var6 = 0; var6 < var0.length; var6++) {
            var4[var6] = var0[var6];
         }

         var0 = new String[var1];

         for (int var7 = 0; var7 < var4.length; var7++) {
            var0[var7] = var4[var7];
         }
      }

      return var0;
   }
}
