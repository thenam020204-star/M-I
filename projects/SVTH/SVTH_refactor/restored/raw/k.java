import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class k {
  private static Image a_Image;
  private static Image b_Image;
  private static Image c_Image;
  private static Random a_Random;
  private static byte a_B = 0;
  private static int a_I = 0;
  private static byte b_B = 0;
  private static boolean a_Z = false;
  private static int b_I;

  public static void a_V() {
    cc.a(1);
    cc.a(0);
    System.gc();
  }

  public static String a(String var0, String var1, String var2) {
    int var3 = 0;

    int var4;
    while ((var4 = var0.indexOf(var1, var3)) != -1) {
      int var5 = var1.length();
      var0 = var0.substring(var3, var4) + var2 + var0.substring(var4 + var5, var0.length());
      var3 = var4 + var5 + 1;
    }

    return var0;
  }

  public static byte[] a(byte[] var0) {
    if (var0 == null) {
      return null;
    } else {
      for (int var2 = 0; var2 < var0.length; var2++) {
        for (int var3 = 0; var3 < var0.length - 1 - var2; var3++) {
          if (var0[var3] > var0[var3 + 1]) {
            byte var1 = var0[var3];
            var0[var3] = var0[var3 + 1];
            var0[var3 + 1] = var1;
          }
        }
      }

      return var0;
    }
  }

  public static boolean a(bl var0) {
    boolean var1 = false;

    for (int var2 = 0; var2 < o.a_Abl.length; var2++) {
      if (o.a_Abl[var2] == var0) {
        var1 = true;
      }
    }

    return var1;
  }

  public static short[][] a(String[] var0, String var1) {
    short[][] var2 = null;
    if (var0 != null) {
      var2 = new short[var0.length][];

      for (int var3 = 0; var0 != null && var3 < var0.length; var3++) {
        if (var1 != null) {
          var2[var3] = cg.b_AS(var0[var3], var1);
        } else {
          var1 = ",";
          var2[var3] = cg.b_AS(var0[var3], var1);
        }
      }
    }

    return var2;
  }

  public static int a(int var0, int var1) {
    if (a_Random == null) {
      a_Random = new Random();
    }

    int var2;
    return var2 = var0 + Math.abs(a_Random.nextInt()) % var1;
  }

  public static int b(int var0, int var1) {
    if (var1 <= var0) {
      var0 = var0;
    } else {
      var0 = a(var0, var1 - var0 + 1);
    }

    return var0;
  }

  public static void a(short[][] var0, int var1, int var2) {
    int var3;
    for (int var9 = (var3 = var0.length + 1) / 2; var9 > 0; var9 /= 2) {
      for (int var4 = var9; var4 < var3; var4++) {
        int var5 = var4 - var9;

        while (var5 >= var9) {
          short[] var10000 = var0[var5 - 1];
          short[] var10001 = var0[var5 + var9 - 1];
          boolean var8 = false;
          byte var7 = 1;
          short[] var6 = var10001;
          short[] var10 = var10000;
          byte var14;
          if (var10000[var7] < var6[var7]) {
            byte var11;
            if (!var8) {
              var11 = 1;
            } else {
              var11 = -1;
            }

            var14 = var11;
          } else if (var10[var7] > var6[var7]) {
            byte var12;
            if (!var8) {
              var12 = -1;
            } else {
              var12 = 1;
            }

            var14 = var12;
          } else {
            var14 = 0;
          }

          if (var14 > 0) {
            short[] var13 = var0[var5 - 1];
            var0[var5 - 1] = var0[var5 + var9 - 1];
            var0[var5 + var9 - 1] = var13;
            var5 -= var9;
          } else {
            var5 = 0;
          }
        }
      }
    }
  }

  public static int[] a(int var0, int var1, int var2) {
    int[] var4 = null;
    int[] var6 = null;
    if (var1 + 1 < 2) {
      return null;
    } else {
      for (int var3 = 0; var3 < var1 + 1; var3++) {
        var6 = cg.a_AI(var6, var3 + 0);
      }

      for (int var7 = 0; var7 < 2; var7++) {
        var1 = a(0, var6.length);
        var4 = cg.a_AI(var4, var6[var1]);
        var6 = cg.c(var6, var1);
      }

      return var4;
    }
  }

  public static int a(int[] var0, int var1) {
    if (cg.a_Z(var0, var1)) {
      var0 = cg.b(var0, var1);
    }

    var1 = b(0, var0.length - 1);
    int var2;
    return var2 = var0[var1];
  }

  public static void a(Graphics var0, String[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8, boolean var9) {
    var0.setClip(var2, var3, var4, var6 * cg.b_S);
    if (var6 < var1.length) {
      a_I++;
      if (!a_Z) {
        if (a_I >= 10) {
          a_Z = true;
        }
      } else {
        if (a_I % 3 == 0) {
          b_B = (byte)(b_B + 6);
        }

        if (b_B >= cg.b_S) {
          a_Z = false;
          a_B++;
          b_B = 0;
          a_I = 0;
          if (a_B == var1.length) {
            a_B = 0;
          }
        }
      }
    }

    for (int var10 = a_B; var10 < var1.length; var10++) {
      if (var8 != null) {
        var0.setColor(var8[var10]);
      } else {
        var0.setColor(var7);
      }

      if (var9) {
        vh.drawString(var0, var1[var10], var2 + var4 / 2, var3 + cg.b_S * (var10 - a_B) - b_B, 17);
      } else {
        vh.drawString(var0, var1[var10], var2, var3 + cg.b_S * (var10 - a_B) - b_B, 20);
      }
    }

    var0.setColor(16777215);
    var0.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
  }

  public static void b() {
    a_B = 0;
    a_I = 0;
    b_B = 0;
    a_Z = false;
  }

  private static int[][] a(short var0) {
    int[][] var1 = null;
    int[] var2 = null;
    int[] var3 = null;
    if (o.e_String == null) {
      o.e_String = cg.a_String("/bin/colorTable.bin");
    }

    int var4;
    if (var0 % 4 == 0) {
      var4 = var0 / 4;
    } else {
      var4 = var0 / 4 + 1;
    }

    String[][] var7 = cg.a_AAString(o.e_String, "enemy" + var4 + ":", "enemy" + var4 + "End", null, ",");
    if ((var0 = var0 % 4) == 0) {
      var0 = 4;
    }

    if (var0 != 1 && var7 != null) {
      var2 = new int[var7[0].length];
      var3 = new int[var7[0].length];

      for (int var5 = 0; var5 < var7[0].length; var5++) {
        var2[var5] = cg.a_I(var7[0][var5]);
        var3[var5] = cg.a_I(var7[var0 - 1][var5]);
      }
    }

    if (var2 != null) {
      (var1 = new int[2][])[0] = var2;
      var1[1] = var3;
    }

    return var1;
  }

  public static bx a(int var0, boolean var1) {
    String var3;
    if (var0 % 4 == 0) {
      int var2 = var0 / 4;
      var3 = "/ft/" + var2 + ".av";
    } else {
      int var5 = var0 / 4 + 1;
      var3 = "/ft/" + var5 + ".av";
    }

    bx var6;
    (var6 = new bx()).a_Z = var1;
    int[][] var4;
    if ((var4 = a((short)var0)) != null) {
      System.out.println("Đổi màu");
      if (!at.a_String.equals("N7610") && !at.a_String.equals("N73")) {
        var6.a(var3, null, null, null, var4[0], var4[1], 0);
        if (!at.a_String.equals("N73")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("N5800")
          && !at.a_String.equals("E62")
          && !at.a_String.equals("W958C")) {
          cc.a(0);
        }
      } else {
        var6.a(var3, null, null, null, var4[0], var4[1], 1);
      }
    } else if (!at.a_String.equals("N7610") && !at.a_String.equals("N73")) {
      var6.a(var3, 0);
    } else {
      var6.a(var3, 1);
    }

    return var6;
  }

  public static String[][] a(String[][] var0, String[] var1) {
    if (var0 == null) {
      (var0 = new String[1][])[0] = var1;
      return var0;
    } else {
      String[][] var2 = new String[var0.length + 1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static int a(int[] var0) {
    int var1 = 0;
    if (var0 == null) {
      var1 = -1;
    } else {
      for (int var2 = 0; var2 < var0.length; var2++) {
        if (var1 <= var0[var2]) {
          var1 = var0[var2];
        }
      }
    }

    return var1;
  }

  public static void a(Graphics var0, Image var1, Image var2, int var3, int var4, int var5, int var6, int var7, int var8) {
    a_Image = null;
    b_Image = null;
    if (a_Image == null) {
      a_Image = cc.a_Image("/pic/arrowhead.gf", 2);
    }

    if (b_Image == null) {
      b_Image = cc.a_Image("/pic/arrow1.gf", 2);
    }

    var0.setColor(3219973);
    var0.fillRect(var3, var4 + 4, a_Image.getWidth(), var5 - 8);
    var0.setColor(13410343);
    var0.fillRect(var3 + 1, var4 + 4, 1, var5 - 8);
    var0.fillRect(var3 + a_Image.getWidth() - 2, var4 + 4, 1, var5 - 8);
    if (a_Image != null) {
      var0.drawRegion(a_Image, 0, 0, a_Image.getWidth(), a_Image.getHeight() / 2, 0, var3, var4, 20);
      var0.drawRegion(a_Image, 0, a_Image.getHeight() / 2, a_Image.getWidth(), a_Image.getHeight() / 2, 0, var3, var4 + var5, 36);
    }

    if (var6 > var7) {
      int var9 = var5 - 12 - b_Image.getHeight();
      if (var8 < var6 - var7) {
        var0.drawImage(b_Image, var3, var4 + 6 + var8 * var9 / (var6 - var7), 20);
        return;
      }

      var0.drawImage(b_Image, var3, var4 + var5 - 6, 36);
    }
  }

  public static short[] a(String var0, String var1, String var2, String var3, String var4) {
    short[] var10000;
    if ((var0 = cg.a_String(cg.a(var0, var1, var2), var4)) != null && !var0.equals("")) {
      String[] var6 = cg.b_AString(var0, var3);
      short[] var7 = null;
      if (var6 != null) {
        var7 = new short[var6.length];

        for (int var8 = 0; var8 < var6.length; var8++) {
          var7[var8] = cg.a_S(var6[var8]);
        }
      }

      var10000 = var7;
    } else {
      var10000 = null;
    }

    return var10000;
  }

  public static void a(Graphics var0, String var1, int var2) {
    var0.setColor(0);
    vh.drawString(var0, var1, b_I, 2, 20);
    if ((b_I = b_I) < -vh.stringWidth(cg.a_Font, var1)) {
      b_I = at.a_AS[0];
    }
  }

  public static short[][][] a(short[][][] var0, short[][] var1) {
    if (var0 == null) {
      (var0 = new short[1][][])[0] = var1;
      return var0;
    } else {
      short[][][] var2 = new short[var0.length + 1][][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static int a_I() {
    int var0 = 0;

    for (int var1 = 0; o.a_Acv != null && var1 < o.a_Acv.length; var1++) {
      if (o.a_Acv[var1].a_B == o.e_S && o.a_Acv[var1].a_Z) {
        var0++;
      }
    }

    return var0;
  }

  public static void a(int var0) {
    for (int var1 = 0; var1 < da.a_da.a_cd.a_bj.a_Acz.length; var1++) {
      if (da.a_da.a_cd.a_bj.a_Acz[var1] != null && da.a_da.a_cd.a_bj.a_Acz[var1].i_B == 1 && da.a_da.a_cd.a_bj.a_Acz[var1].j_Z) {
        System.out.println("==============" + da.a_da.a_cd.a_bj.a_Acz[var1].h_S);
      }
    }

    if (o.a_Abl[0].h_S != var0) {
      o.e_S = (short)var0;
      o.a(o.a_Abl);
      o.a_Abl[0].a_bx = null;
      da.a_da.a_cd.a_bj.c(o.a_Abl[0]);
      da.a_da.a_cd.a_bj.d();
      da.a_da.a_cd.a_bj.b();
      short var5 = o.a_Abl[0].d_S;
      short var2 = o.a_Abl[0].e_S;
      byte var3 = o.a_Abl[0].d_B;
      new bl();
      bl var4 = o.a_bl(var0);
      o.a_Abl = null;
      o.b_AB = null;
      o.c(var4);
      if (o.e_S < 4) {
        o.d(o.a_Abl[0]);
      }

      o.a_Abl[0].a(var5, var2);
      o.a_Abl[0].a_V(var3);
      da.a_da.a_cd.a_bj.a(o.a_Abl[0]);
    }
  }

  public static void a(Graphics var0, String var1, int var2, int var3) {
    var0.translate(0, 0);
    if (var1 != null) {
      String[] var4 = cg.a(var1, var2 - 40);
      int var7 = vh.stringWidth(cg.a_Font, var1) + 20;
      int var5 = cg.b_S + 15;
      var3 /= 3;
      int var6 = (var2 - var7) / 2;
      if (var4.length > 1) {
        int var8 = var2 - 30;
        var5 = var4.length * cg.b_S + (var4.length + 1) * 5;
        a(var0, 16, var3, var8, var5);

        for (int var9 = 0; var9 < var4.length; var9++) {
          var0.setColor(16777215);
          vh.drawString(var0, var4[var9], var2 / 2, var3 + 8 + (cg.b_S + 1) * var9, 17);
        }

        return;
      }

      a(var0, var6, var3, var7, var5);
      var0.setColor(16777215);
      vh.drawString(var0, var4[0], var2 / 2, var3 + 8, 17);
    }
  }

  private static void a(Graphics var0, int var1, int var2, int var3, int var4) {
    var0.setColor(1381651);
    var0.fillRoundRect(var1 - 1, var2 - 1, var3 + 3, var4 + 3, 8, 8);
    var0.setColor(16053217);
    var0.drawRoundRect(var1, var2, var3, var4, 8, 8);
    var0.setColor(6768205);
    var0.fillRect(var1 + 2, var2 + 2, var3 - 3, var4 - 3);
  }

  public static void c() {
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      c_Image = null;
      a_Image = null;
      b_Image = null;
    }
  }

  public static void a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
    if (ct.b_B == 1) {
      var0.setColor(15788720);
      var0.fillRect(var1, var2, var3, var4);
      var0.setColor(1317669);
      var0.fillRect(var1 + 1, var2 + 1, var3 - 2, var4 - 2);
    } else {
      if (c_Image == null) {
        c_Image = Image.createRGBImage(a_AI(var3, var4, 1317669, 204), var3, var4, true);
      } else if (var4 != c_Image.getHeight() || var3 != c_Image.getWidth()) {
        c();
        c_Image = Image.createRGBImage(a_AI(var3, var4, 1317669, 204), var3, var4, true);
      }

      var0.drawImage(c_Image, var1, var2, 20);
    }

    if (var8) {
      var0.setColor(3027509);
      var0.drawRoundRect(var1 - 1, var2 - 1, var3 + 1, var4 + 1, 4, 4);
      var0.setColor(12371653);
      var0.drawRoundRect(var1, var2, var3 - 1, var4 - 1, 4, 4);
    }
  }

  public static Image a_Image(int var0, int var1, int var2, int var3) {
    return Image.createRGBImage(a_AI(var0 - 2, var1 - 2, var2, 216), var0 - 2, var1 - 2, true);
  }

  private static int[] a_AI(int var0, int var1, int var2, int var3) {
    if (var3 >= 0 && var3 <= 255) {
      int[] var6 = new int[var0 * var1];
      var1 = a(var2, "R");
      int var4 = a(var2, "G");
      var2 = a(var2, "B");

      for (int var5 = 0; var5 < var6.length; var5++) {
        var6[var5] = (var3 << 24) + (var1 << 16) + (var4 << 8) + var2;
      }

      return var6;
    } else {
      return null;
    }
  }

  private static int a(int var0, String var1) {
    if (var1.equals("R") || var1.equals("r")) {
      int var3;
      var0 = (var3 = var0 & 0xFF0000) >> 16;
    } else if (var1.equals("G") || var1.equals("g")) {
      int var2;
      var0 = (var2 = var0 & 0xFF00) >> 8;
    } else if (var1.equals("B") || var1.equals("b")) {
      var0 &= 255;
    }

    return var0;
  }

  public static int[] a(String[][] var0, String[][] var1, int var2, int var3, int var4) {
    int var6 = cg.a_I(var0[var2][var3]) - 1;
    int[] var5 = cg.b_AI(var1[var6][var3 - 3], "|");
    var6 = cg.a_I(var0[var2][var3 + 1]) - 1;
    int[] var7 = cg.b_AI(var1[var6][var3 - 2], "|");
    int[] var8;
    if (var4 == 1) {
      var8 = cg.a_AI(cg.a_AI(null, o.a(var7[0], var7[1], true)), b(var5[0], var5[1]));
    } else {
      int var9 = a(var7[0], var7[1] - (var7[1] + var7[0]) / 2 + 1);
      var8 = cg.a_AI(cg.a_AI(null, var9), b(var5[0], var5[1]) + var9 * (var4 - 1));
    }

    return var8;
  }

  public static void a(short var0, short var1, short var2) {
    bz var3;
    (var3 = new bz()).a_String = "Dịch chuyển phó bản";
    var3.a_AS = new short[]{(short)(var0 - 12), (short)(var1 - 12), 24, 24};
    var3.a_Z = true;
    var3.b_Z = true;
    var3.a_AAString = new String[][]{{"换场景", String.valueOf((int)var2), "1"}};
    if (da.a_da.a_cd.a_ci.a_Abz == null) {
      da.a_da.a_cd.a_ci.a_Abz = new bz[1];
    } else {
      bz[] var4 = new bz[da.a_da.a_cd.a_ci.a_Abz.length + 1];
      System.arraycopy(da.a_da.a_cd.a_ci.a_Abz, 0, var4, 0, da.a_da.a_cd.a_ci.a_Abz.length);
      da.a_da.a_cd.a_ci.a_Abz = var4;
    }

    da.a_da.a_cd.a_ci.a_Abz[da.a_da.a_cd.a_ci.a_Abz.length - 1] = var3;
    if (da.a_da.a_cd.a_bx == null) {
      da.a_da.a_cd.a_bx = new bx();
      da.a_da.a_cd.a_bx.a("/map/pics/fazheng.av");
      da.a_da.a_cd.e_S = var0;
      da.a_da.a_cd.f_S = var1;
    }
  }

  public static void a(Graphics var0, bx var1, int var2, int var3, boolean var4, int var5) {
    if (var1 != null) {
      var1.a_V(var5);
      var1.d_S = (short)var2;
      var1.e_S = (short)var3;
      var1.a(var0);
      if (da.a_da.a_S % 2 == 0) {
        var1.a(true);
      }
    }
  }

  public static void a(Graphics var0, int var1, int var2, int var3, Image var4, int var5, int var6, int var7, int var8) {
    if (var4 == null) {
      if (var8 == 1) {
        cg.a(var0, "" + var1, var2, var3, 2380419, 9306107, 33);
      } else {
        cg.a(var0, "" + var1, var2, var3, 16681646, 8791076, 33);
      }
    } else if (var1 <= 0) {
      cg.a(var0, var1, var2, var3, var4, 13, 13, 33);
    } else {
      var5 = cg.a(var1);
      cg.a(var0, var1, var2, var3, var4, 13, 13, 33);
      var0.drawRegion(var4, 143, 0, 13, 13, 0, var2 - var5 * 13 / 2 - 6, var3, 33);
    }
  }

  public static void a(Graphics var0, String var1, int var2, int var3, int var4, int var5) {
    var0.setColor(2093589);
    vh.drawString(var0, var1, var2, var3, 17);
    vh.drawString(var0, var1, var2 + 1, var3, 17);
  }

  public static cv[] a_Acv() {
    cv[] var0 = null;
    Vector var1 = new Vector();

    for (int var2 = 0; o.a_Acv != null && var2 < o.a_Acv.length; var2++) {
      if (o.a_Acv[var2].a_B == o.e_S) {
        var1.addElement(o.a_Acv[var2]);
      }
    }

    if (var1.size() > 0) {
      var0 = new cv[var1.size()];
      var1.copyInto(var0);
    }

    return var0;
  }
}
