public final class af {
  public static String a_String = "http://mbsvrt.3g.qq.com/";
  public static String b_String = "http://sngame.3g.qq.com/sngame/";
  public static String c_String = "http://sngame.3g.qq.com/sngame/";
  private int a_I = 5;
  private String d_String;
  private byte[] a_AB;
  private p a_p;
  private String e_String;
  private String f_String = "";
  public long a_J = 0L;

  public af(String var1, byte[] var2, p var3) {
    this.d_String = var1;
    this.a_AB = var2;
    this.a_p = var3;
    this.e_String = "GET";
  }

  public final void a(String var1) {
    this.d_String = var1;
  }

  public final String a_String() {
    return this.e_String;
  }

  public final void b(String var1) {
    this.e_String = var1;
  }

  public final String b() {
    return this.d_String;
  }

  public final p a_p() {
    return this.a_p;
  }

  public final byte[] a_AB() {
    byte[] var1 = null;
    if (this.a_AB != null) {
      var1 = new byte[this.a_AB.length];
      System.arraycopy(this.a_AB, 0, var1, 0, var1.length);
    }

    return var1;
  }

  public final void a(byte[] var1) {
    this.a_AB = null;
    if (var1 != null) {
      this.a_AB = new byte[var1.length];
      System.arraycopy(var1, 0, this.a_AB, 0, this.a_AB.length);
    }
  }

  public final String c() {
    return this.f_String;
  }

  public final void c(String var1) {
    this.f_String = var1;
  }

  public final int a_I() {
    return this.a_I;
  }
}
