final class SceneLoader implements Runnable {
   private Thread thread;
   private final World world;

   public SceneLoader(World var1) {
      this.world = var1;
      this.thread = null;
      this.thread = new Thread(this);
      this.thread.start();
   }

   public final void run() {
      this.world.loadScene();
      this.world.loadFinished = true;
   }
}
