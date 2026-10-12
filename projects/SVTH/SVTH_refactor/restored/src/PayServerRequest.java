import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class PayServerRequest {
   private static String host;
   private static String proxyUrl;
   private static String directUrl;
   PayServerConnection connection;
   boolean isWaiting;

   public PayServerRequest() {
      host = "wap.moonic.cn";
      proxyUrl = "http://10.0.0.172:80/payServer.do";
      directUrl = "http://" + host + "/payServer.do";
   }

   public abstract void onPayResponse(int var1, int var2, String var3);

   public final void sendRequest(String var1, String var2, int var3) {
      this.connection = new PayServerConnection(this);
      this.connection.callback = this;
      this.connection.commandId = 12;
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      DataOutputStream var5 = new DataOutputStream(var4);

      try {
         var5.writeShort(this.connection.commandId);
         var5.writeUTF(var1);
         var5.writeUTF(var2);
         var5.writeShort(var3);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      this.connection.body = var4.toByteArray();
      PayServerConnection var7 = this.connection;
      if (this.connection.body != null) {
         new Thread(var7).start();
      }

      PayServerTimeout var8;
      (var8 = new PayServerTimeout(this)).startTime = System.currentTimeMillis();
      this.isWaiting = true;
      new Thread(var8).start();
   }

   static String getDirectUrl() {
      return directUrl;
   }

   static String getProxyUrl() {
      return proxyUrl;
   }

   static String getHost() {
      return host;
   }
}
