import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class cu {
  private static String a_String;
  private static String b_String;
  private static int a_I;
  private static String[] a_AString;
  private static int[] a_AI;
  private static short[] a_AS;
  private static int[] b_AI;
  private static Object a_Object = new Object();

  public static boolean a_Z(String var0) {
    InputStream var1;
    if ((var1 = a_Object.getClass().getResourceAsStream(var0)) == null) {
      return false;
    } else {
      DataInputStream var4 = new DataInputStream(var1);
      byte[] var2 = new byte[3];

      try {
        var4.read(var2);
        if ("jzy".equals(new String(var2)) && 1 == var4.readByte()) {
          a_String = var4.readUTF();
          a_AString = new String[a_I = var4.readInt()];
          a_AI = new int[a_I];
          a_AS = new short[a_I];
          b_AI = new int[a_I];

          for (int var5 = 0; var5 < a_I; var5++) {
            a_AString[var5] = var4.readUTF();
            a_AI[var5] = var4.readInt();
            a_AS[var5] = (short)(var4.readByte() & 255);
            b_AI[var5] = var4.readInt();
          }

          b_String = var0.substring(0, var0.lastIndexOf(47));
        }

        var4.close();
      } catch (IOException var3) {
        var3.printStackTrace();
      }

      return true;
    }
  }

  public static synchronized byte[] a_AB(String var0) {
    if (var0.startsWith("/")) {
      var0 = var0.substring(1);
    }

    if (a_AString != null) {
      for (int var1 = 0; var1 < a_AString.length; var1++) {
        if (var0.equals(a_AString[var1])) {
          short var7 = a_AS[var1];
          int var2 = a_AI[var1];
          int var3 = 0;

          ByteArrayOutputStream var4;
          for (var4 = new ByteArrayOutputStream(); var2 > 0; var3++) {
            String var5 = b_String + "/" + a_String + (var7 + var3);
            Object var6 = null;
            if ((var6 = cg.a_AB(var5, false)) == null) {
              System.out.println("Đọc tệp gói " + var5 + " Thất bại");
              return null;
            }

            if (var3 == 0) {
              int var8 = var2 <= ((Object[])var6).length - b_AI[var1] ? var2 : ((Object[])var6).length - b_AI[var1];
              var4.write((byte[])var6, b_AI[var1], var8);
              var2 -= var8;
            } else {
              int var9 = var2 <= ((Object[])var6).length ? var2 : ((Object[])var6).length;
              var4.write((byte[])var6, 0, var9);
              var2 -= var9;
            }
          }

          return var4.toByteArray();
        }
      }
    }

    return null;
  }

  public static boolean b(String var0) {
    if (var0 == null) {
      return false;
    } else {
      if (var0.startsWith("/")) {
        var0 = var0.substring(1);
      }

      if (a_AString != null) {
        for (int var1 = 0; var1 < a_AString.length; var1++) {
          if (var0.equals(a_AString[var1])) {
            return true;
          }
        }
      }

      return false;
    }
  }
}
