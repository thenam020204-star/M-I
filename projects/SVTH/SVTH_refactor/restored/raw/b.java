public final class b {
  private static short[] c_AS = new short[]{-1};
  private static short[] d_AS = new short[]{-2};
  private static short[] e_AS = new short[]{-3};
  private static short[] f_AS = new short[]{-4};
  private static short[] g_AS = new short[]{-5};
  public static short[] a_AS = new short[]{-6};
  public static short[] b_AS = new short[]{-7};
  private static short[][] a_AAS = new short[][]{{48}, {49}, {50}, {51}, {52}, {53}, {54}, {55}, {56}, {57}};
  private static short[] h_AS = new short[]{42};
  private static short[] i_AS = new short[]{35};
  public static short a_S = 998;
  public static short b_S = 999;

  public static void a() {
    String var0;
    short[] var1;
    if ((var1 = cg.a_AS(var0 = cg.a_String("/bin/key.txt"), "u")) != null) {
      c_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "d")) != null) {
      d_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "l")) != null) {
      e_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "r")) != null) {
      f_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "ok")) != null) {
      g_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "sl")) != null) {
      a_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "sr")) != null) {
      b_AS = var1;
    }

    short[][] var9 = cg.a_AAS(var0, "number:", "end", "=");

    for (int var2 = 0; var9 != null && var2 < var9.length; var2++) {
      if (var9[var2] != null) {
        a_AAS[var2] = var9[var2];
      }
    }

    if ((var1 = cg.a_AS(var0, "star")) != null) {
      h_AS = var1;
    }

    if ((var1 = cg.a_AS(var0, "pound")) != null) {
      i_AS = var1;
    }
  }

  public static int a(int var0) {
    for (int var1 = 0; var1 < g_AS.length; var1++) {
      if (var0 == g_AS[var1]) {
        return 8;
      }
    }

    for (int var3 = 0; var3 < c_AS.length; var3++) {
      if (var0 == c_AS[var3]) {
        return 1;
      }
    }

    for (int var4 = 0; var4 < d_AS.length; var4++) {
      if (var0 == d_AS[var4]) {
        return 6;
      }
    }

    for (int var5 = 0; var5 < e_AS.length; var5++) {
      if (var0 == e_AS[var5]) {
        return 2;
      }
    }

    for (int var6 = 0; var6 < f_AS.length; var6++) {
      if (var0 == f_AS[var6]) {
        return 5;
      }
    }

    for (int var7 = 0; var7 < a_AS.length; var7++) {
      if (var0 == a_AS[var7]) {
        return a_S;
      }
    }

    for (int var8 = 0; var8 < b_AS.length; var8++) {
      if (var0 == b_AS[var8]) {
        return b_S;
      }
    }

    for (int var9 = 0; var9 < h_AS.length; var9++) {
      if (var0 == h_AS[var9]) {
        return 42;
      }
    }

    for (int var10 = 0; var10 < i_AS.length; var10++) {
      if (var0 == i_AS[var10]) {
        return 35;
      }
    }

    for (int var11 = 0; var11 < a_AAS.length; var11++) {
      for (int var2 = 0; var2 < a_AAS[var11].length; var2++) {
        if (a_AAS[var11][var2] == var0) {
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
