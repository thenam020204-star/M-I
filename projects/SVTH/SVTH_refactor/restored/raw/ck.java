import javax.microedition.lcdui.Image;

public final class ck {
  public short a_S;
  public byte a_B;
  public short b_S;
  public short c_S;
  public short d_S;
  public short e_S;
  public short f_S;
  public short g_S;
  public short h_S;
  public short i_S;
  public short j_S;
  public short k_S;
  public short l_S;
  public short m_S;
  public short n_S;
  private static String[] d_AString;
  public byte b_B;
  public static short[] a_AS;
  public static String[] a_AString;
  public static short[] b_AS;
  private static String[] e_AString;
  public static String[] b_AString;
  public static String[] c_AString;
  public static short[] c_AS;
  private static short[] e_AS;
  public static short[] d_AS;
  public static Image[] a_AImage;

  public ck() {
  }

  public ck(int var1) {
    a_V();
    if (o.c_AAString == null) {
      o.c_AAString = cg.a_AAString(cg.a_String("/bin/equip.bin"), "equip", "equipend", null, "\t");
    }

    for (int var2 = 0; o.c_AAString != null && var2 < o.c_AAString.length; var2++) {
      if (cg.a_I(o.c_AAString[var2][0]) == var1) {
        this.a_S = (short)(var2 + 1);
        break;
      }
    }

    this.b_B = 1;
    this.c_V();
  }

  private void c_V() {
    if (d_AString == null) {
      if (o.c_AAString == null) {
        o.c_AAString = cg.a_AAString(cg.a_String("/bin/equip.bin"), "equip", "equipend", null, "\t");
      }

      String[] var1 = null;

      for (int var2 = 0; var2 < o.c_AAString.length; var2++) {
        var1 = cg.a(var1, o.c_AAString[var2][3]);
        d_AString = cg.a(d_AString, o.c_AAString[var2][3]);
      }

      for (int var5 = 0; var5 < o.c_AAString.length; var5++) {
        d_AS = cg.a_AS(d_AS, cg.a_S(o.c_AAString[var5][9]));
      }
    }

    short[][] var4 = k.a(cg.b_AString(d_AString[this.a_S - 1], "|"), null);

    for (int var6 = 0; var4 != null && var6 < var4.length; var6++) {
      int var3 = k.b(var4[var6][1], var4[var6][2]);
      if (var4[var6][0] == 1) {
        this.h_S += (short)var3;
      } else if (var4[var6][0] == 2) {
        this.i_S += (short)var3;
      } else if (var4[var6][0] == 3) {
        this.j_S += (short)var3;
      } else if (var4[var6][0] == 4) {
        this.k_S += (short)var3;
      } else if (var4[var6][0] == 5) {
        this.l_S += (short)var3;
      } else if (var4[var6][0] == 6) {
        this.m_S += (short)var3;
        if (this.m_S < 0) {
          this.m_S = 0;
        }
      } else if (var4[var6][0] == 7) {
        this.n_S += (short)var3;
      } else {
        System.out.println("createEquip specialField logic err");
      }
    }

    this.a_B = 0;
  }

  public static void a_V() {
    if (a_AString == null) {
      if (o.c_AAString == null) {
        o.c_AAString = cg.a_AAString(cg.a_String("/bin/equip.bin"), "equip", "equipend", null, "\t");
      }

      for (int var0 = 0; var0 < o.c_AAString.length; var0++) {
        a_AString = cg.a(a_AString, o.c_AAString[var0][1]);
        b_AS = cg.a_AS(b_AS, cg.a_I(o.c_AAString[var0][2]));
        b_AString = cg.a(b_AString, o.c_AAString[var0][5]);
        c_AString = cg.a(c_AString, o.c_AAString[var0][6]);
        e_AString = cg.a(e_AString, o.c_AAString[var0][4]);
        c_AS = cg.a_AS(c_AS, cg.a_I(o.c_AAString[var0][7]));
        e_AS = cg.a_AS(e_AS, cg.a_I(o.c_AAString[var0][8]));
        a_AS = cg.a_AS(a_AS, cg.a_I(o.c_AAString[var0][0]));
        d_AS = cg.a_AS(d_AS, cg.a_S(o.c_AAString[var0][9]));
      }
    }
  }

  public static void b_V() {
    if (e_AString == null) {
      a_V();
    }

    if (a_AImage == null) {
      byte var0;
      a_AImage = new Image[var0 = (byte)e_AString.length];

      for (byte var1 = 0; var1 < var0; var1++) {
        a_AImage[var1] = cc.a_Image("/icon/" + e_AString[var1] + ".gf", 2);
      }
    }
  }

  public final int a_I() {
    return this.f_S + this.l_S;
  }

  public final int b_I() {
    return this.d_S + this.j_S;
  }

  public final int c_I() {
    return this.e_S + this.k_S;
  }

  public final int d() {
    return this.h_S + this.b_S;
  }

  public final int e() {
    return this.i_S + this.c_S;
  }

  public static String a(ck var0) {
    String var1 = "";
    if (var0.h_S + var0.b_S > 0) {
      var1 = var1 + " HP+" + (var0.h_S + var0.b_S);
    }

    if (var0.i_S + var0.c_S > 0) {
      var1 = var1 + " MP +" + (var0.i_S + var0.c_S);
    }

    if (var0.j_S + var0.d_S > 0) {
      var1 = var1 + " ATK+" + (var0.j_S + var0.d_S);
    }

    if (var0.k_S + var0.e_S > 0) {
      var1 = var1 + " DEF+" + (var0.k_S + var0.e_S);
    }

    if (var0.l_S + var0.f_S > 0) {
      var1 = var1 + " SPD+" + (var0.l_S + var0.f_S);
    }

    if (var0.n_S + var0.g_S > 0) {
      var1 = var1 + " CRIT+" + (var0.n_S + var0.g_S);
    }

    if (var0.m_S > 0) {
      var1 = var1 + " Lỗ " + var0.a_B + "/" + var0.m_S;
    }

    return var1 + " Cấp yêu cầu " + d_AS[o.f_I(var0.a_S)];
  }
}
