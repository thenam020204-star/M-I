final class s extends Thread {
  volatile boolean a_Z;
  private ap a_ap;

  s(ap var1) {
    this.a_ap = var1;
    this.a_Z = false;
  }

  public final void run() {
    while (ap.a_Z(this.a_ap)) {
      af var1;
      synchronized (ap.a_AI(this.a_ap)) {
        while (ap.a_Vector(this.a_ap).size() == 0) {
          try {
            ap.a_AI(this.a_ap).wait();
          } catch (InterruptedException var5) {
          }

          if (!ap.a_Z(this.a_ap)) {
            return;
          }
        }

        var1 = (af)ap.a_Vector(this.a_ap).firstElement();
        if (this.getPriority() == 5 && var1.a_I() == 1) {
          try {
            Thread.sleep(400L);
          } catch (InterruptedException var3) {
          }
          continue;
        }

        ap.a_Vector(this.a_ap).removeElement(var1);
      }

      try {
        Thread.sleep(100L);
      } catch (InterruptedException var4) {
      }

      this.a_Z = false;
      ap.a(this.a_ap, var1, this);
    }
  }
}
