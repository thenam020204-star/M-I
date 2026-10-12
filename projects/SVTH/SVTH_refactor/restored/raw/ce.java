final class ce implements Runnable {
  private Thread a_Thread;
  private final bs a_bs;

  public ce(bs var1) {
    this.a_bs = var1;
    this.a_Thread = null;
    this.a_Thread = new Thread(this);
    this.a_Thread.start();
  }

  public final void run() {
    this.a_bs.b();
    this.a_bs.a_Z = true;
  }
}
