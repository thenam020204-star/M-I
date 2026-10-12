import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

final class y implements Runnable {
  private HttpConnection a_HttpConnection;
  public int a_I;
  public aw a_aw;
  byte[] a_AB;
  private aw b_aw;

  public y(aw var1) {
    this.b_aw = var1;
  }

  private HttpConnection a(boolean var1, String var2, String var3, String var4) {
    this.a_HttpConnection = (HttpConnection)Connector.open(var1 ? var3 : var2, 3, true);
    this.a_HttpConnection.setRequestMethod("POST");
    this.a_HttpConnection.setRequestProperty("Connection", "close");
    this.a_HttpConnection.setRequestProperty("Pragma", "no-cache");
    this.a_HttpConnection.setRequestProperty("User-Agent", "Profile/MIDP-2.0 Configuration/CLDC-1.0");
    this.a_HttpConnection.setRequestProperty("Content-Language", "zh-cn");
    this.a_HttpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    this.a_HttpConnection.setRequestProperty("Accept", "text/plain");
    if (var1) {
      this.a_HttpConnection.setRequestProperty("X-Online-Host", var4);
    }

    return this.a_HttpConnection;
  }

  public final void run() {
    try {
      Thread.sleep(500L);
      OutputStream var1 = null;

      try {
        this.a_HttpConnection = this.a(false, aw.a(), aw.b(), aw.c());
        (var1 = this.a_HttpConnection.openOutputStream()).write(this.a_AB);
        var1.close();
        var24 = true;
      } catch (Exception var18) {
        var24 = false;
      }

      if (!var24) {
        try {
          this.a_HttpConnection = this.a(true, aw.a(), aw.b(), aw.c());
          (var1 = this.a_HttpConnection.openOutputStream()).write(this.a_AB);
          var1.close();
        } catch (Exception var19) {
          this.b_aw.a_Z = false;
          this.a_aw.a(this.a_I, 2, "Kết nối thất bại");
          return;
        }
      }

      int var27 = this.a_HttpConnection.getResponseCode();
      this.b_aw.a_Z = false;
      if (var27 != 200) {
        this.a_aw.a(this.a_I, 2, "Kết nối thất bại responseCode:" + var27);
        return;
      }

      InputStream var28 = this.a_HttpConnection.openInputStream();
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
      this.a_aw.a(var32, var33 == 1 ? 0 : 3, var30);
      return;
    } catch (IOException var20) {
      this.a_aw.a(this.a_I, 2, "Lỗi IO mạng");
      return;
    } catch (SecurityException var21) {
      this.a_aw.a(this.a_I, 1, "Người dùng từ chối kết nối");
      return;
    } catch (Exception var22) {
      this.a_aw.a(this.a_I, 3, "Lỗi khác");
    } finally {
      this.b_aw.a_Z = false;

      try {
        if (this.a_HttpConnection != null) {
          this.a_HttpConnection.close();
        }
      } catch (Exception var17) {
        var17.printStackTrace();
      }
    }
  }
}
