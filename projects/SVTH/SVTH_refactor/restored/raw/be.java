public final class be {
  private static int[] a_AI = new int[256];

  static {
    for (int var0 = 0; var0 < 256; var0++) {
      int var1 = var0;

      for (int var2 = 0; var2 < 8; var2++) {
        if ((var1 & 1) == 1) {
          var1 = -306674912 ^ var1 >>> 1;
        } else {
          var1 >>>= 1;
        }

        a_AI[var0] = var1;
      }
    }
  }

  public static byte[] a(byte[] var0, int[] var1, int[] var2) {
    if (var0 == null || var2 == null) {
      return null;
    } else if (var0 == null) {
      return null;
    } else {
      byte[] var3 = new byte[var0.length];
      System.arraycopy(var0, 0, var3, 0, var0.length);
      int[] var15;
      if ((var15 = a(var3)) != null) {
        int var10001 = var15[0];
        int var10002 = var15[1];
        int[] var6 = var2;
        int[] var5 = var1;
        int var4 = var10002;
        int var18 = var10001;
        byte[] var16 = var3;

        for (int var13 = var18; var13 < var18 + var4; var13 += 3) {
          if (var5 != null) {
            for (int var14 = 0; var14 < var5.length; var14++) {
              byte var7 = (byte)(var5[var14] >> 16 & 0xFF);
              byte var8 = (byte)(var5[var14] >> 8 & 0xFF);
              byte var9 = (byte)var5[var14];
              byte var19;
              byte var20;
              byte var21;
              if (var14 < var6.length) {
                var19 = (byte)(var6[var14] >> 16 & 0xFF);
                var20 = (byte)(var6[var14] >> 8 & 0xFF);
                var21 = (byte)var6[var14];
              } else {
                var19 = (byte)(var6[var6.length - 1] >> 16 & 0xFF);
                var20 = (byte)(var6[var6.length - 1] >> 8 & 0xFF);
                var21 = (byte)var6[var6.length - 1];
              }

              if (var16[var13] == var7 && var16[var13 + 1] == var8 && var16[var13 + 2] == var9) {
                var16[var13] = var19;
                var16[var13 + 1] = var20;
                var16[var13 + 2] = var21;
                break;
              }
            }
          } else {
            byte var10 = (byte)(var6[0] >> 16);
            byte var11 = (byte)(var6[0] >> 8);
            byte var12 = (byte)var6[0];
            var16[var13] = var10;
            var16[var13 + 1] = var11;
            var16[var13 + 2] = var12;
          }
        }

        int var17 = a(var3, var15[0] - 4, var15[1] + 4);
        var3[var15[0] - 1 + var15[1] + 4] = (byte)var17;
        var3[var15[0] - 1 + var15[1] + 3] = (byte)(var17 >>> 8);
        var3[var15[0] - 1 + var15[1] + 2] = (byte)(var17 >>> 16);
        var3[var15[0] - 1 + var15[1] + 1] = (byte)(var17 >> 24);
      }

      return var3;
    }
  }

  public static byte[] a(byte[] var0, int var1) {
    if (var0 == null) {
      return null;
    } else {
      byte[] var2 = new byte[var0.length];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      int[] var7;
      if ((var7 = a(var2)) != null) {
        for (int var3 = var7[0]; var3 < var7[0] + var7[1]; var3 += 3) {
          short var4 = (short)(var2[var3] & 255);
          short var5 = (short)(var2[var3 + 1] & 255);
          short var6 = (short)(var2[var3 + 2] & 255);
          if (var1 <= 100) {
            var2[var3] = (byte)(var4 * var1 / 100);
            var2[var3 + 1] = (byte)(var5 * var1 / 100);
            var2[var3 + 2] = (byte)(var6 * var1 / 100);
          }
        }

        if (var1 <= 100) {
          int var8 = a(var2, var7[0] - 4, var7[1] + 4);
          var2[var7[0] - 1 + var7[1] + 4] = (byte)var8;
          var2[var7[0] - 1 + var7[1] + 3] = (byte)(var8 >>> 8);
          var2[var7[0] - 1 + var7[1] + 2] = (byte)(var8 >>> 16);
          var2[var7[0] - 1 + var7[1] + 1] = (byte)(var8 >> 24);
        }
      }

      return var2;
    }
  }

  private static int[] a(byte[] var0) {
    if (var0 == null) {
      return null;
    } else {
      String[] var1 = new String[]{"50", "4c", "54", "45"};
      String[] var2 = new String[]{"49", "44", "41", "54"};
      int var3 = 0;

      for (int var4 = 0; var4 < var0.length; var4++) {
        if (Integer.toHexString(var0[var4]).equals(var1[var3])) {
          if (++var3 >= 4) {
            int var6 = var4 + 1;
            int var5 = ((var0[var4 - 3 - 4] & 255) << 24) + ((var0[var4 - 3 - 3] & 255) << 16) + ((var0[var4 - 3 - 2] & 255) << 8) + (var0[var4 - 3 - 1] & 255);
            return new int[]{var6, var5};
          }
        } else if (Integer.toHexString(var0[var4]).equals(var2[var3])) {
          if (++var3 >= 4) {
            return null;
          }
        } else if (var3 != 0) {
          var3 = 0;
        }
      }

      return null;
    }
  }

  private static int a(byte[] var0, int var1, int var2) {
    int var3 = -1;

    for (int var4 = 0; var4 < var2; var4++) {
      var3 = a_AI[(var3 ^ var0[var1 + var4]) & 0xFF] ^ var3 >>> 8;
    }

    return ~var3;
  }
}
