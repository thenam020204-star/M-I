import java.io.UnsupportedEncodingException;
import javax.microedition.lcdui.Image;

public final class cc {
  private static co[] a_Aco;
  private static String[] a_AString;
  private static boolean[] a_AZ;
  private static boolean[] b_AZ;
  private static byte[] a_AB;
  private static Image[] a_AImage;
  private static String[] b_AString;
  private static byte[] b_AB;
  private static byte[][] a_AAB;
  private static String[] c_AString;
  private static byte[] c_AB;
  private static String[] d_AString;
  private static String[] e_AString;
  private static byte[] d_AB;
  private static int a_I;

  public static void a(int var0) {
    int var1 = 0;

    for (int var2 = 0; a_Aco != null && var2 < a_Aco.length; var2++) {
      if (a_AB[var2] == var0) {
        a_Aco[var2] = null;
        a_AString[var2] = null;
        a_AZ[var2] = false;
        b_AZ[var2] = false;
      }

      if (a_Aco[var2] != null) {
        var1++;
      }
    }

    if (a_Aco != null && var1 < a_Aco.length) {
      if (var1 == 0) {
        a_Aco = null;
        a_AString = null;
        a_AZ = null;
        b_AZ = null;
        a_AB = null;
      } else {
        co[] var15 = new co[var1];
        String[] var3 = new String[var1];
        byte[] var4 = new byte[var1];
        boolean[] var5 = new boolean[var1];
        boolean[] var8 = new boolean[var1];
        int var6 = 0;

        for (int var7 = 0; a_Aco != null && var7 < a_Aco.length; var7++) {
          if (a_Aco[var7] != null) {
            var15[var6] = a_Aco[var7];
            var3[var6] = a_AString[var7];
            var4[var6] = a_AB[var7];
            var5[var6] = a_AZ[var7];
            var8[var6] = b_AZ[var7];
            var6++;
          }
        }

        a_Aco = var15;
        a_AString = var3;
        a_AB = var4;
        a_AZ = var5;
        b_AZ = var8;
      }
    }

    var1 = 0;

    for (int var16 = 0; a_AImage != null && var16 < a_AImage.length; var16++) {
      if (b_AB[var16] == var0) {
        if (a_AImage[var16] != null) {
          a_I = a_I - a_AImage[var16].getWidth() * a_AImage[var16].getHeight();
          a_AImage[var16] = null;
        }

        b_AString[var16] = null;
      }

      if (a_AImage[var16] != null) {
        var1++;
      }
    }

    if (a_AImage != null && var1 < a_AImage.length) {
      if (var1 == 0) {
        a_AImage = null;
        b_AString = null;
        b_AB = null;
      } else {
        Image[] var17 = new Image[var1];
        String[] var22 = new String[var1];
        byte[] var10 = new byte[var1];
        int var25 = 0;

        for (int var28 = 0; a_AImage != null && var28 < a_AImage.length; var28++) {
          if (a_AImage[var28] != null) {
            var17[var25] = a_AImage[var28];
            var22[var25] = b_AString[var28];
            var10[var25] = b_AB[var28];
            var25++;
          }
        }

        a_AImage = var17;
        b_AString = var22;
        b_AB = var10;
      }
    }

    var1 = 0;

    for (int var18 = 0; a_AAB != null && var18 < a_AAB.length; var18++) {
      if (c_AB[var18] == var0) {
        a_AAB[var18] = null;
        c_AString[var18] = null;
      }

      if (a_AAB[var18] != null) {
        var1++;
      }
    }

    if (a_AAB != null && var1 < a_AAB.length) {
      if (var1 == 0) {
        a_AAB = null;
        c_AString = null;
        c_AB = null;
      } else {
        byte[][] var19 = new byte[var1][];
        String[] var23 = new String[var1];
        byte[] var26 = new byte[var1];
        int var29 = 0;

        for (int var12 = 0; a_AAB != null && var12 < a_AAB.length; var12++) {
          if (a_AAB[var12] != null) {
            var19[var29] = a_AAB[var12];
            var23[var29] = c_AString[var12];
            var26[var29] = c_AB[var12];
            var29++;
          }
        }

        a_AAB = var19;
        c_AString = var23;
        c_AB = var26;
      }
    }

    var1 = 0;

    for (int var20 = 0; d_AString != null && var20 < d_AString.length; var20++) {
      if (d_AB[var20] == var0) {
        d_AString[var20] = null;
        e_AString[var20] = null;
      }

      if (d_AString[var20] != null) {
        var1++;
      }
    }

    if (d_AString != null && var1 < d_AString.length) {
      if (var1 == 0) {
        d_AString = null;
        e_AString = null;
        d_AB = null;
      } else {
        String[] var21 = new String[var1];
        String[] var24 = new String[var1];
        byte[] var14 = new byte[var1];
        int var27 = 0;

        for (int var30 = 0; d_AString != null && var30 < d_AString.length; var30++) {
          if (d_AString[var30] != null) {
            var21[var27] = d_AString[var30];
            var24[var27] = e_AString[var30];
            var14[var27] = d_AB[var30];
            var27++;
          }
        }

        d_AString = var21;
        e_AString = var24;
        d_AB = var14;
      }
    }

    cg.a();
  }

  public static void a() {
    a_Aco = null;
    a_AZ = null;
    b_AZ = null;
    a_AB = null;
    a_AString = null;
    a_AImage = null;
    b_AString = null;
    b_AB = null;
    a_I = 0;
    a_AAB = null;
    c_AString = null;
    c_AB = null;
    d_AString = null;
    e_AString = null;
    d_AB = null;
    cg.a();
  }

  public static co a(String var0, String var1, String[] var2, String[] var3, int[] var4, int[] var5, int var6, boolean var7, boolean var8, int var9) {
    co var10 = null;
    boolean var11 = false;
    if (!at.n_Z || !at.a_String.equals("N7610")) {
      for (int var12 = 0; a_Aco != null && var12 < a_Aco.length; var12++) {
        if (a_Aco[var12] != null && a_Aco[var12].b_String.equals(var0) && (at.b_B > 0 && var7 == a_AZ[var12] && !b_AZ[var12] || at.b_B == 0)) {
          if ((var10 = a_Aco[var12]).a_B != at.b_B) {
            var10.a(var1, null, null, null, null, -1, var7, false, var9);
          }

          var11 = true;
          break;
        }
      }
    }

    if (!var11 && (var10 = new co()).a_Z(var0)) {
      var10.a(var1, null, null, null, null, -1, var7, false, var9);
      if (var9 >= 0 && (!at.n_Z || !at.a_String.equals("N7610"))) {
        if (a_Aco == null) {
          a_Aco = new co[1];
        } else {
          co[] var13 = new co[a_Aco.length + 1];
          System.arraycopy(a_Aco, 0, var13, 0, a_Aco.length);
          a_Aco = var13;
        }

        a_Aco[a_Aco.length - 1] = var10;
        a_AString = cg.a(a_AString, var0);
        a_AZ = cg.a(a_AZ, var7);
        b_AZ = cg.a(b_AZ, false);
        a_AB = cg.a_AB(a_AB, var9);
      }
    }

    return var10;
  }

  public static Image a(String var0) {
    return a_Image(var0, 0);
  }

  public static Image a_Image(String var0, int var1) {
    Image var2 = null;
    boolean var3 = false;
    String var4 = var0.substring(var0.lastIndexOf(47) + 1);
    var4 = "/pub/" + var4;
    if (!cg.a_Z(var0)) {
      var0 = var4;
    }

    for (int var6 = 0; b_AString != null && var6 < b_AString.length; var6++) {
      if (b_AString[var6] != null && cg.a_Z(b_AString[var6], var0)) {
        var2 = a_AImage[var6];
        var3 = true;
        break;
      }
    }

    if (!var3) {
      if ((var2 = cg.a_Image(var0)) == null) {
        return null;
      }

      if (at.e_Z) {
        System.out.println("Tải ảnh mới:" + var0);
      }

      if (var1 >= 0) {
        if (a_AImage == null) {
          a_AImage = new Image[1];
        } else {
          Image[] var7 = new Image[a_AImage.length + 1];
          System.arraycopy(a_AImage, 0, var7, 0, a_AImage.length);
          a_AImage = var7;
        }

        a_AImage[a_AImage.length - 1] = var2;
        b_AString = cg.a(b_AString, var0);
        b_AB = cg.a_AB(b_AB, var1);
        a_I = a_I + var2.getWidth() * var2.getHeight();
      }
    }

    return var2;
  }

  public static Image b(String var0, int var1) {
    byte[] var2;
    return Image.createImage(var2 = a_AB(var0, 2), 0, var2.length);
  }

  public static byte[] a_AB(String var0, int var1) {
    byte[] var2 = null;
    boolean var3 = false;

    for (int var4 = 0; c_AString != null && var4 < c_AString.length; var4++) {
      if (c_AString[var4] != null && cg.a_Z(c_AString[var4], var0)) {
        var2 = new byte[a_AAB[var4].length];
        System.arraycopy(a_AAB[var4], 0, var2, 0, a_AAB[var4].length);
        var2 = var2;
        var3 = true;
        break;
      }
    }

    if (!var3) {
      if (!var0.toLowerCase().endsWith(".gf") && !var0.toLowerCase().endsWith(".png") && !var0.toLowerCase().endsWith(".jpg")) {
        var2 = cg.a_AB(var0);
      } else {
        var2 = cg.b_AB(var0);
      }

      if (var2 == null) {
        return null;
      }

      if (var1 >= 0) {
        if (a_AAB == null) {
          a_AAB = new byte[1][];
        } else {
          byte[][] var6 = new byte[a_AAB.length + 1][];
          System.arraycopy(a_AAB, 0, var6, 0, a_AAB.length);
          a_AAB = var6;
        }

        a_AAB[a_AAB.length - 1] = var2;
        c_AString = cg.a(c_AString, var0);
        c_AB = cg.a_AB(c_AB, var1);
      }
    }

    return var2;
  }

  public static String a(String var0, boolean var1, int var2) {
    var0 = vh.rn(var0);
    String var3 = null;
    String var4 = var0;
    boolean var5 = false;

    for (int var6 = 0; e_AString != null && var6 < e_AString.length; var6++) {
      if (e_AString[var6] != null && e_AString[var6].equals(var0)) {
        var3 = d_AString[var6];
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
      if ((var16 = cg.a_AB(var0)) == null) {
        if (!var0.toLowerCase().endsWith(".bin")) {
          if ((var1 = var0.lastIndexOf(46)) > 0) {
            var0 = var0.substring(0, var1) + ".bin";
          }

          var16 = cg.a_AB(var0);
        } else {
          if (!var0.toLowerCase().endsWith(".bin")) {
            return null;
          }

          if ((var1 = var0.lastIndexOf(46)) > 0) {
            var0 = var0.substring(0, var1) + ".txt";
          }

          var16 = cg.a_AB(var0);
        }
      }

      if (var16 == null) {
        return null;
      }

      if (!var0.toLowerCase().endsWith(".txt")) {
        if (cg.a_AB != null) {
          short var14 = 0;

          for (int var10 = 0; var10 < var16.length; var10++) {
            var16[var10] -= cg.a_AB[var14];
            if (var14 < cg.a_AB.length - 1) {
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
        if (d_AString == null) {
          d_AString = new String[1];
        } else {
          String[] var15 = new String[d_AString.length + 1];
          System.arraycopy(d_AString, 0, var15, 0, d_AString.length);
          d_AString = var15;
        }

        d_AString[d_AString.length - 1] = var3;
        e_AString = cg.a(e_AString, var4);
        d_AB = cg.a_AB(d_AB, var2);
      }
    }

    return var3;
  }
}
