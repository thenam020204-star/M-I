public final class HttpRequest {
   public static String portalServerUrl = "http://mbsvrt.3g.qq.com/";
   public static String sngameServerUrl = "http://sngame.3g.qq.com/sngame/";
   public static String sngameServerUrl2 = "http://sngame.3g.qq.com/sngame/";
   private int priority = 5;
   private String url;
   private byte[] data;
   private HttpRequestListener listener;
   private String method;
   private String errorMessage = "";
   public long bytesReceived = 0L;

   public HttpRequest(String var1, byte[] var2, HttpRequestListener var3) {
      this.url = var1;
      this.data = var2;
      this.listener = var3;
      this.method = "GET";
   }

   public final void setUrl(String var1) {
      this.url = var1;
   }

   public final String getMethod() {
      return this.method;
   }

   public final void setMethod(String var1) {
      this.method = var1;
   }

   public final String getUrl() {
      return this.url;
   }

   public final HttpRequestListener getListener() {
      return this.listener;
   }

   public final byte[] getData() {
      byte[] var1 = null;
      if (this.data != null) {
         var1 = new byte[this.data.length];
         System.arraycopy(this.data, 0, var1, 0, var1.length);
      }

      return var1;
   }

   public final void setData(byte[] var1) {
      this.data = null;
      if (var1 != null) {
         this.data = new byte[var1.length];
         System.arraycopy(var1, 0, this.data, 0, this.data.length);
      }
   }

   public final String getErrorMessage() {
      return this.errorMessage;
   }

   public final void setErrorMessage(String var1) {
      this.errorMessage = var1;
   }

   public final int getPriority() {
      return this.priority;
   }
}
