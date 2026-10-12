public final class bq {
  private byte[] a_AB;

  public final byte[] a_AB(String var1) {
    int var2;
    if ((var2 = var1.length()) >= 16) {
      var1 = var1.substring(0, 16);
    } else {
      for (int var3 = 0; var3 < 16 - var2; var3++) {
        var1 = var1.concat("0");
      }
    }

    this.a_AB = var1.getBytes();
    return this.a_AB;
  }

  // $VF: Could not inline inconsistent finally blocks
  // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
  public final String a_String(String var1) {
    String var2 = "";

    try {
      try {
        String var8;
        int var3 = (var8 = new String(this.a(this.a_AB, a_AB(var1.getBytes()), false))).length();
        return var8.substring(0, var3 - 6);
      } catch (Exception var6) {
        var6.printStackTrace();
        return var2;
      }
    } catch (Throwable var7) {
      throw var7;
    }
  }

  private static int a_I(byte[] var0, int var1) {
    return (var0[var1] << 8 & 0xFF00) + (var0[var1 + 1] & 0xFF);
  }

  private static void a(int var0, byte[] var1, int var2) {
    var1[var2] = (byte)(var0 >>> 8);
    var1[var2 + 1] = (byte)var0;
  }

  private static int a(int var0, int var1) {
    if (var0 == 0) {
      var0 = 65537 - var1;
    } else if (var1 == 0) {
      var0 = 65537 - var0;
    } else {
      int var3;
      var1 = (var3 = var0 * var1) & 65535;
      var0 = var3 >>> 16;
      var0 = var1 - var0 + (var1 < var0 ? 1 : 0);
    }

    return var0 & 65535;
  }

  private int[] a_AI(byte[] var1) {
    int[] var2 = new int[52];
    if (var1.length < 16) {
      byte[] var3 = new byte[16];
      System.arraycopy(var1, 0, var3, var3.length - var1.length, var1.length);
      var1 = var3;
    }

    for (int var4 = 0; var4 < 8; var4++) {
      var2[var4] = a_I(var1, var4 << 1);
    }

    for (int var5 = 8; var5 < 52; var5++) {
      if ((var5 & 7) < 6) {
        var2[var5] = ((var2[var5 - 7] & 127) << 9 | var2[var5 - 6] >> 7) & 65535;
      } else if ((var5 & 7) == 6) {
        var2[var5] = ((var2[var5 - 7] & 127) << 9 | var2[var5 - 14] >> 7) & 65535;
      } else {
        var2[var5] = ((var2[var5 - 15] & 127) << 9 | var2[var5 - 14] >> 7) & 65535;
      }
    }

    return var2;
  }

  private static int a(int var0) {
    if (var0 < 2) {
      return var0;
    } else {
      int var1 = 1;
      int var2 = 65537 / var0;
      int var3 = 65537 % var0;

      while (var3 != 1) {
        int var4 = var0 / var3;
        var0 %= var3;
        var1 = var1 + var2 * var4 & 65535;
        if (var0 == 1) {
          return var1;
        }

        var4 = var3 / var0;
        var3 %= var0;
        var2 = var2 + var1 * var4 & 65535;
      }

      return 1 - var2 & 65535;
    }
  }

  private static byte[] a_AB(byte[] var0, int var1) {
    int var2 = var0.length;
    var1 -= var2 % var1;
    int var3;
    byte[] var4 = new byte[var3 = var2 + var1];
    System.arraycopy(var0, 0, var4, 0, var2);

    for (int var5 = var2; var5 < var3; var5++) {
      var4[var5] = (byte)var1;
    }

    return var4;
  }

  private byte[] a(byte[] var1, byte[] var2, boolean var3) {
    var1 = a_AB(var1, 16);
    byte[] var20;
    int var4 = (var3 = (var20 = a_AB(var2, 8)).length) / 8;
    byte[] var22 = new byte[var3];

    for (int var5 = 0; var5 < var4; var5++) {
      byte[] var6 = new byte[16];
      byte[] var7 = new byte[8];
      System.arraycopy(var1, 0, var6, 0, 16);
      System.arraycopy(var20, var5 << 3, var7, 0, 8);
      boolean var9 = false;
      byte[] var10 = new byte[8];
      int[] var10000;
      if (var9) {
        var10000 = this.a_AI(var6);
      } else {
        int[] var23 = this.a_AI(var6);
        int var13 = 52;
        int var14 = 0;
        int[] var15 = new int[52];
        var14++;
        int var11 = a(var23[0]);
        var14++;
        int var12 = var23[1];
        int var16 = 0 - var12 & 65535;
        var14++;
        var12 = var23[2];
        int var17 = 0 - var12 & 65535;
        var14++;
        int var18 = a(var23[3]);
        var13--;
        var15[51] = var18;
        var13--;
        var15[50] = var17;
        var13--;
        var15[49] = var16;
        var13--;
        var15[48] = var11;

        for (int var34 = 1; var34 < 8; var34++) {
          var11 = var23[var14++];
          var16 = var23[var14++];
          var13--;
          var15[var13] = var16;
          var13--;
          var15[var13] = var11;
          var11 = a(var23[var14++]);
          var12 = var23[var14++];
          var16 = 0 - var12 & 65535;
          var12 = var23[var14++];
          var17 = 0 - var12 & 65535;
          var18 = a(var23[var14++]);
          var13--;
          var15[var13] = var18;
          var13--;
          var15[var13] = var16;
          var13--;
          var15[var13] = var17;
          var13--;
          var15[var13] = var11;
        }

        var11 = var23[var14++];
        var16 = var23[var14++];
        var13--;
        var15[var13] = var16;
        var13--;
        var15[var13] = var11;
        var11 = a(var23[var14++]);
        var12 = var23[var14++];
        var16 = 0 - var12 & 65535;
        var12 = var23[var14++];
        var17 = 0 - var12 & 65535;
        var18 = a(var23[var14]);
        var13--;
        var15[var13] = var18;
        var13--;
        var15[var13] = var17;
        var13--;
        var15[var13] = var16;
        var13--;
        var15[var13] = var11;
        var10000 = var15;
      }

      int[] var24 = var10000;
      int[] var35 = var24;
      int var25 = 0;
      int var62 = a_I(var7, 0);
      int var78 = a_I(var7, 2);
      int var84 = a_I(var7, 4);
      int var40 = a_I(var7, 6);

      for (int var95 = 0; var95 < 8; var95++) {
        var62 = a(var62, var35[var25++]);
        int var79;
        var78 = (var79 = var78 + var35[var25++]) & 65535;
        int var85;
        var84 = (var85 = var84 + var35[var25++]) & 65535;
        var40 = a(var40, var35[var25++]);
        int var87 = var84 ^ var62;
        int var81 = var78 ^ var40;
        int var88 = a(var87, var35[var25++]);
        int var82;
        int var83 = a((var82 = var81 + var88) & 65535, var35[var25++]);
        int var89;
        int var90 = (var89 = var88 + var83) & 65535;
        var62 ^= var83;
        var40 ^= var90;
        var78 = var83 ^ var84;
        var84 = var90 ^ var78;
      }

      a(a(var62, var35[var25++]), var10, 0);
      a(var84 + var35[var25++], var10, 2);
      a(var78 + var35[var25++], var10, 4);
      a(a(var40, var35[var25]), var10, 6);
      System.arraycopy(var10, 0, var22, var5 << 3, 8);
    }

    return var22;
  }

  private static byte[] a_AB(byte[] var0) {
    if (var0.length % 2 != 0) {
      throw new IllegalArgumentException("Length Not Double Num");
    } else {
      byte[] var1 = new byte[var0.length / 2];

      for (byte var2 = 0; var2 < var0.length; var2 += 2) {
        String var3 = new String(var0, var2, 2);
        var1[var2 / 2] = (byte)Integer.parseInt(var3, 16);
      }

      return var1;
    }
  }
}
