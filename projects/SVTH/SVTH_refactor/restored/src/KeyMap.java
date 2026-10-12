public final class KeyMap {
   private static short[] upCodes = new short[]{-1};
   private static short[] downCodes = new short[]{-2};
   private static short[] leftCodes = new short[]{-3};
   private static short[] rightCodes = new short[]{-4};
   private static short[] okCodes = new short[]{-5};
   public static short[] softLeftCodes = new short[]{-6};
   public static short[] softRightCodes = new short[]{-7};
   private static short[][] digitCodes = new short[][]{{48}, {49}, {50}, {51}, {52}, {53}, {54}, {55}, {56}, {57}};
   private static short[] starCodes = new short[]{42};
   private static short[] poundCodes = new short[]{35};
   public static short softLeftKey = 998;
   public static short softRightKey = 999;

   public KeyMap() {
   }

   public static void loadKeyConfig() {
      String var0;
      short[] var1;
      if ((var1 = Utils.getShortArray(var0 = Utils.loadConfigText("/bin/key.txt"), "u")) != null) {
         upCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "d")) != null) {
         downCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "l")) != null) {
         leftCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "r")) != null) {
         rightCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "ok")) != null) {
         okCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "sl")) != null) {
         softLeftCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "sr")) != null) {
         softRightCodes = var1;
      }

      short[][] var9 = Utils.getShortTable(var0, "number:", "end", "=");

      for (int var2 = 0; var9 != null && var2 < var9.length; var2++) {
         if (var9[var2] != null) {
            digitCodes[var2] = var9[var2];
         }
      }

      if ((var1 = Utils.getShortArray(var0, "star")) != null) {
         starCodes = var1;
      }

      if ((var1 = Utils.getShortArray(var0, "pound")) != null) {
         poundCodes = var1;
      }
   }

   public static int normalizeKey(int var0) {
      for (int var1 = 0; var1 < okCodes.length; var1++) {
         if (var0 == okCodes[var1]) {
            return 8;
         }
      }

      for (int var3 = 0; var3 < upCodes.length; var3++) {
         if (var0 == upCodes[var3]) {
            return 1;
         }
      }

      for (int var4 = 0; var4 < downCodes.length; var4++) {
         if (var0 == downCodes[var4]) {
            return 6;
         }
      }

      for (int var5 = 0; var5 < leftCodes.length; var5++) {
         if (var0 == leftCodes[var5]) {
            return 2;
         }
      }

      for (int var6 = 0; var6 < rightCodes.length; var6++) {
         if (var0 == rightCodes[var6]) {
            return 5;
         }
      }

      for (int var7 = 0; var7 < softLeftCodes.length; var7++) {
         if (var0 == softLeftCodes[var7]) {
            return softLeftKey;
         }
      }

      for (int var8 = 0; var8 < softRightCodes.length; var8++) {
         if (var0 == softRightCodes[var8]) {
            return softRightKey;
         }
      }

      for (int var9 = 0; var9 < starCodes.length; var9++) {
         if (var0 == starCodes[var9]) {
            return 42;
         }
      }

      for (int var10 = 0; var10 < poundCodes.length; var10++) {
         if (var0 == poundCodes[var10]) {
            return 35;
         }
      }

      for (int var11 = 0; var11 < digitCodes.length; var11++) {
         for (int var2 = 0; var2 < digitCodes[var11].length; var2++) {
            if (digitCodes[var11][var2] == var0) {
               if (var11 == 0) {
                  return 48;
               }

               if (var11 == 1) {
                  return 49;
               }

               if (var11 == 2) {
                  return 1;
               }

               if (var11 == 3) {
                  return 51;
               }

               if (var11 == 4) {
                  return 2;
               }

               if (var11 == 5) {
                  return 8;
               }

               if (var11 == 6) {
                  return 5;
               }

               if (var11 == 7) {
                  return 55;
               }

               if (var11 == 8) {
                  return 6;
               }

               if (var11 == 9) {
                  return 57;
               }
            }
         }
      }

      return 0;
   }
}
