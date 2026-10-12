import javax.microedition.lcdui.Image;

public final class d {
  public short a_S;
  public byte a_B;
  public byte b_B;
  public int a_I;
  public int b_I;
  public static Image[] a_AImage;
  public static short[] a_AS;
  public static String[] a_AString;
  public static String[] b_AString;
  public static short[] b_AS;
  public static short[] c_AS;
  public static short[][] a_AAS;
  public static String[] c_AString;
  private static short[] e_AS;
  public static short[][][] a_AAAS;
  public static short[] d_AS;

  public d() {
  }

  public d(int var1, int var2, int var3) {
    a();
    this.a_S = (short)var1;
    this.a_B = (byte)var2;
    this.a_I = 0;
    this.b_I = (this.a_B + 1) * (this.a_B + 1) << 1;
    this.b_B = 10;
    if (a_AAAS != null) {
      var1 = o.c_I(var1);

      for (int var5 = 0; var5 < a_AAAS[var1].length; var5++) {
        if (a_AAAS[var1][var5][0] == var3) {
          this.b_B = (byte)a_AAAS[var1][var5][2];
          return;
        }
      }
    }
  }

  public static void a() {
    if (a_AString == null) {
      if (o.b_AAString == null) {
        o.b_AAString = cg.a_AAString(cg.a_String("/bin/skill.bin"), "skill:", "skillend", null, "\t");
      }

      for (int var0 = 0; var0 < o.b_AAString.length; var0++) {
        a_AS = cg.a_AS(a_AS, cg.a_I(o.b_AAString[var0][0]));
        a_AString = cg.a(a_AString, o.b_AAString[var0][1]);
        b_AString = cg.a(b_AString, o.b_AAString[var0][2]);
        b_AS = cg.a_AS(b_AS, cg.a_B(o.b_AAString[var0][4]));
        c_AS = cg.a_AS(c_AS, cg.a_B(o.b_AAString[var0][5]));
        short[] var1 = cg.b_AS(o.b_AAString[var0][6], ",");
        a_AAS = cg.a(a_AAS, var1);
        c_AString = cg.a(c_AString, o.b_AAString[var0][7]);
        e_AS = cg.a_AS(e_AS, cg.a_I(o.b_AAString[var0][8]));
        String[] var5 = cg.b_AString(o.b_AAString[var0][9], "|");
        short[][] var2 = null;

        for (int var4 = 0; var5 != null && var4 < var5.length; var4++) {
          short[] var3 = cg.b_AS(var5[var4], ",");
          var2 = cg.a(var2, var3);
        }

        a_AAAS = k.a(a_AAAS, var2);
        d_AS = cg.a_AS(d_AS, cg.a_I(o.b_AAString[var0][10]));
      }
    }
  }

  public static void b() {
    if (b_AString == null) {
      a();
    }

    int var0 = b_AString.length;
    if (a_AImage == null) {
      a_AImage = new Image[var0];

      for (int var1 = 0; var1 < var0; var1++) {
        a_AImage[var1] = cc.a_Image("/icon/" + b_AString[var1] + ".gf", 2);
      }
    }
  }
}
