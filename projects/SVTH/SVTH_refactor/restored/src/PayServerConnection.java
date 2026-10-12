import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

final class PayServerConnection implements Runnable {
   private HttpConnection http;
   public int commandId;
   public PayServerRequest callback;
   byte[] body;
   private PayServerRequest owner;

   public PayServerConnection(PayServerRequest var1) {
      this.owner = var1;
   }

   private HttpConnection openConnection(boolean var1, String var2, String var3, String var4) {
      this.http = (HttpConnection)Connector.open(var1 ? var3 : var2, 3, true);
      this.http.setRequestMethod("POST");
      this.http.setRequestProperty("Connection", "close");
      this.http.setRequestProperty("Pragma", "no-cache");
      this.http.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
      this.http.setRequestProperty("Content-Language", "zh-cn");
      this.http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      this.http.setRequestProperty("Accept", "text/plain");
      if (var1) {
         this.http.setRequestProperty("X-Online-Host", var4);
      }

      return this.http;
   }

   public final void run() {
      try {
         Thread.sleep(500L);
         OutputStream var1 = null;

         try {
            this.http = this.openConnection(false, PayServerRequest.getDirectUrl(), PayServerRequest.getProxyUrl(), PayServerRequest.getHost());
            (var1 = this.http.openOutputStream()).write(this.body);
            var1.close();
            var24 = true;
         } catch (Exception var18) {
            var24 = false;
         }

         if (!var24) {
            try {
               this.http = this.openConnection(true, PayServerRequest.getDirectUrl(), PayServerRequest.getProxyUrl(), PayServerRequest.getHost());
               (var1 = this.http.openOutputStream()).write(this.body);
               var1.close();
            } catch (Exception var19) {
               this.owner.isWaiting = false;
               this.callback.onPayResponse(this.commandId, 2, "Kết nối thất bại");
               return;
            }
         }

         int var27 = this.http.getResponseCode();
         this.owner.isWaiting = false;
         if (var27 != 200) {
            this.callback.onPayResponse(this.commandId, 2, "Kết nối thất bại responseCode:" + var27);
            return;
         }

         InputStream var28 = this.http.openInputStream();
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         byte[] var3 = new byte[256];

         int var4;
         while ((var4 = var28.read(var3)) != -1) {
            var2.write(var3, 0, var4);
         }

         var3 = var2.toByteArray();
         DataInputStream var29;
         short var32 = (var29 = new DataInputStream(new ByteArrayInputStream(var3))).readShort();
         byte var33 = var29.readByte();
         String var30 = var29.readUTF();
         var28.close();
         this.callback.onPayResponse(var32, var33 == 1 ? 0 : 3, var30);
         return;
      } catch (IOException var20) {
         this.callback.onPayResponse(this.commandId, 2, "Lỗi IO mạng");
         return;
      } catch (SecurityException var21) {
         this.callback.onPayResponse(this.commandId, 1, "Người dùng từ chối kết nối");
         return;
      } catch (Exception var22) {
         this.callback.onPayResponse(this.commandId, 3, "Lỗi khác");
      } finally {
         this.owner.isWaiting = false;

         try {
            if (this.http != null) {
               this.http.close();
            }
         } catch (Exception var17) {
            var17.printStackTrace();
         }
      }
   }
}
