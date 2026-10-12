final class HttpWorker extends Thread {
   volatile boolean isCancelled;
   private HttpRequestQueue queue;

   HttpWorker(HttpRequestQueue var1) {
      this.queue = var1;
      this.isCancelled = false;
   }

   public final void run() {
      while (HttpRequestQueue.isRunningOf(this.queue)) {
         HttpRequest var1;
         synchronized (HttpRequestQueue.lockOf(this.queue)) {
            while (HttpRequestQueue.queueOf(this.queue).size() == 0) {
               try {
                  HttpRequestQueue.lockOf(this.queue).wait();
               } catch (InterruptedException var5) {
               }

               if (!HttpRequestQueue.isRunningOf(this.queue)) {
                  return;
               }
            }

            var1 = (HttpRequest)HttpRequestQueue.queueOf(this.queue).firstElement();
            if (this.getPriority() == 5 && var1.getPriority() == 1) {
               try {
                  Thread.sleep(400L);
               } catch (InterruptedException var3) {
               }
               continue;
            }

            HttpRequestQueue.queueOf(this.queue).removeElement(var1);
         }

         try {
            Thread.sleep(100L);
         } catch (InterruptedException var4) {
         }

         this.isCancelled = false;
         HttpRequestQueue.execute(this.queue, var1, this);
      }
   }
}
