public final class bh implements Runnable {
  private String a_String = null;
  private String b_String = null;
  private cx a_cx;

  private bh() {
  }

  public static final void a(String var0, String var1, cx var2) {
    bh var3;
    (var3 = new bh()).a_String = var0;
    var3.b_String = var1;
    var3.a_cx = var2;
    new Thread(var3).start();
  }

  public final void run() {
    this.a_cx.a(0, null);
  }
}
