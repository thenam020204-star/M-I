public final class bw {
  private int a_I;
  private boolean a_Z;
  String a_String;
  private int b_I;

  public bw() {
    this.a_Z = false;
  }

  public bw(int var1, int var2, boolean var3, String var4, int var5) {
    this.a_I = var2;
    this.a_Z = var3;
    this.a_String = var4;
    this.b_I = var5;
  }

  public final int a_I() {
    return this.a_I;
  }

  public final int b() {
    return this.b_I;
  }

  public final boolean a_Z() {
    return this.a_Z;
  }
}
