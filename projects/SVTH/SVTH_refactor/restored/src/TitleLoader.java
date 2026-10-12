final class TitleLoader implements Runnable {
   private Thread thread;
   private final TitleScreen titleScreen;

   public TitleLoader(TitleScreen var1) {
      this.titleScreen = var1;
      this.thread = null;
      this.thread = new Thread(this);
      this.thread.start();
   }

   public final void run() {
      this.titleScreen.loadResources();
      this.titleScreen.isLoaded = true;
   }
}
