public final class SmsSendTask implements Runnable {
   private String smsText = null;
   private String phoneNumber = null;
   private ResultListener listener;

   private SmsSendTask() {
   }

   public static final void send(String var0, String var1, ResultListener var2) {
      SmsSendTask var3;
      (var3 = new SmsSendTask()).smsText = var0;
      var3.phoneNumber = var1;
      var3.listener = var2;
      new Thread(var3).start();
   }

   public final void run() {
      this.listener.onResult(0, null);
   }
}
