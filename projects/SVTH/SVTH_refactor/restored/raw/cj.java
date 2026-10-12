public final class cj {
  public static int a(int var0, int var1) {
    byte var2;
    if (var0 - var1 > 0) {
      var2 = 2;
    } else {
      var2 = 3;
    }

    return var2;
  }

  public static m a_m(int var0, int var1, int var2, int var3, int var4) {
    m var5 = new m();
    if (var4 == 2) {
      var0 -= var2 / 100000;
    } else if (var4 == 3) {
      var0 += var2 / 100000;
    }

    var1 += var3 / 100000;
    var5.a_I = var0;
    var5.b_I = var1;
    return var5;
  }

  public static int[] a_AI(int var0, int var1, int var2, int var3, int var4) {
    int[] var5 = new int[]{0, 0};
    var0 = Math.abs(var2 - var0);
    var5[0] = var0 * 100000 / var4;
    var5[0] = var5[0] > 0 ? var5[0] : -var5[0];
    var5[1] = (var3 - var1) * 100000 / var4;
    return var5;
  }

  static {
    new cj();
  }
}
