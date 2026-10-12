final class BattleLoader implements Runnable {
   private Thread thread;
   private final Battle battle;

   public BattleLoader(Battle var1) {
      this.battle = var1;
      this.thread = null;
      this.thread = new Thread(this);
      this.thread.start();
   }

   public final void run() {
      this.battle.loadResources();
      if (DeviceConfig.moreSleep) {
         try {
            Thread.sleep(200L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      this.battle.isLoaded = true;
   }
}
