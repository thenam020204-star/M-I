public final class bd {
  private String a_String;
  private int a_I;

  public bd() {
  }

  public bd(int var1, int var2, String var3) {
    this.a_I = var2;
    this.a_String = var3;
  }

  public final String a_String() {
    return this.a_String;
  }

  public final int a_I() {
    return this.a_I;
  }
}
