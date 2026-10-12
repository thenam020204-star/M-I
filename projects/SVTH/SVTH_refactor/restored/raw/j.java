final class j implements Runnable {
  private Thread a_Thread;
  private final cd a_cd;

  public j(cd var1) {
    this.a_cd = var1;
    this.a_Thread = null;
    this.a_Thread = new Thread(this);
    this.a_Thread.start();
  }

  public final void run() {
    this.a_cd.c();
    this.a_cd.a_Z = true;
  }
}
