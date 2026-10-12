final class x implements Runnable {
  private Thread a_Thread;
  private final bn a_bn;

  public x(bn var1) {
    this.a_bn = var1;
    this.a_Thread = null;
    this.a_Thread = new Thread(this);
    this.a_Thread.start();
  }

  public final void run() {
    this.a_bn.a_Z();
    if (at.g_Z) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }
    }

    this.a_bn.a_Z = true;
  }
}
