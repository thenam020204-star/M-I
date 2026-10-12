public final class aq {
  private int[] a_AI = new int[30];
  private int a_I = 0;

  protected final void a_V(int var1) {
    if (this.a_I > this.a_AI.length - 1) {
      int[] var2 = new int[this.a_AI.length];
      System.arraycopy(this.a_AI, 0, var2, 0, var2.length);
      this.a_AI = null;
      this.a_AI = new int[var2.length + 30];
      System.arraycopy(var2, 0, this.a_AI, 0, var2.length);
    }

    this.a_AI[this.a_I] = var1;
    this.a_I++;
  }

  public final int a_I(int var1) {
    return this.a_AI[var1];
  }

  public final int a_I() {
    return this.a_I;
  }

  protected final void a_V() {
    this.a_I = 0;
    this.a_AI = null;
    this.a_AI = new int[30];
  }
}
