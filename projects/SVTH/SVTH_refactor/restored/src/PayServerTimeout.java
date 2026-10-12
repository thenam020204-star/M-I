final class PayServerTimeout implements Runnable {
   long startTime;
   private long timeoutMs;
   private PayServerRequest request;

   PayServerTimeout(PayServerRequest var1) {
      this.request = var1;
      this.timeoutMs = 20000L;
   }

   public final void run() {
      while (this.request.isWaiting) {
         if (System.currentTimeMillis() - this.startTime > this.timeoutMs) {
            this.request.connection.callback.onPayResponse(this.request.connection.commandId, 2, "Hết thời gian kết nối");
            this.request.isWaiting = false;
         }

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }
}
