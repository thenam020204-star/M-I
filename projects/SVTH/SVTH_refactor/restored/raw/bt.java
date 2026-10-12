final class bt implements Runnable {
  long a_J;
  private long b_J;
  private aw a_aw;

  bt(aw var1) {
    this.a_aw = var1;
    this.b_J = 20000L;
  }

  public final void run() {
    while (this.a_aw.a_Z) {
      if (System.currentTimeMillis() - this.a_J > this.b_J) {
        this.a_aw.a_y.a_aw.a(this.a_aw.a_y.a_I, 2, "Hết thời gian kết nối");
        this.a_aw.a_Z = false;
      }

      try {
        Thread.sleep(500L);
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }
    }
  }
}
