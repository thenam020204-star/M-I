import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class aw {
  private static String a_String;
  private static String b_String;
  private static String c_String;
  y a_y;
  boolean a_Z;

  public aw() {
    a_String = "wap.moonic.cn";
    b_String = "http://10.0.0.172:80/payServer.do";
    c_String = "http://" + a_String + "/payServer.do";
  }

  public abstract void a(int var1, int var2, String var3);

  public final void a(String var1, String var2, int var3) {
    this.a_y = new y(this);
    this.a_y.a_aw = this;
    this.a_y.a_I = 12;
    ByteArrayOutputStream var4 = new ByteArrayOutputStream();
    DataOutputStream var5 = new DataOutputStream(var4);

    try {
      var5.writeShort(this.a_y.a_I);
      var5.writeUTF(var1);
      var5.writeUTF(var2);
      var5.writeShort(var3);
    } catch (IOException var6) {
      var6.printStackTrace();
    }

    this.a_y.a_AB = var4.toByteArray();
    y var7 = this.a_y;
    if (this.a_y.a_AB != null) {
      new Thread(var7).start();
    }

    bt var8;
    (var8 = new bt(this)).a_J = System.currentTimeMillis();
    this.a_Z = true;
    new Thread(var8).start();
  }

  static String a() {
    return c_String;
  }

  static String b() {
    return b_String;
  }

  static String c() {
    return a_String;
  }
}
