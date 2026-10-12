import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.ConnectionNotFoundException;

public class ak implements p {
  private ap a_ap;
  private an a_an;
  private u a_u;

  public ak(u var1, an var2) {
    this.a_an = var2;
    this.a_u = var1;
    this.a_ap = new ap(var1);
  }

  public final void a() {
    this.a_ap.a();
  }

  public final void a(af var1) {
    byte[] var2;
    if ((var2 = var1.a_AB()) != null) {
      Hashtable[] var3 = a(var2);
      this.a_an.a(var3);
      this.a_an.b(var3);
    }
  }

  public final void b(af var1) {
    this.a_an.a(var1.c());
  }

  private static Hashtable[] a(byte[] var0) {
    Hashtable[] var1 = null;

    try {
      String var8 = new String(var0, "UTF-8");
      z var2;
      (var2 = new z()).a(var8);
      var1 = new Hashtable[var2.a()];

      for (int var4 = 0; var4 < var1.length; var4++) {
        var1[var4] = new Hashtable(10);
        ay var9 = var2.a(var4);
        var1[var4].put("type", var9.a_String);

        for (int var5 = 0; var5 < var9.a_Vector.size(); var5++) {
          ar var3 = var9.a_Vector.size() == 0 ? null : (ar)var9.a_Vector.elementAt(var5);
          var1[var4].put(var3.a(), var3.b());
        }
      }
    } catch (Exception var7) {
      var7.printStackTrace();
    }

    return var1;
  }

  public final void a(al var1, r[] var2) {
    this.a_u.a_ac().a_al = var1;
    if (this.a_an.a_I == 1) {
      this.a_ap.c();
    }

    this.a_an.e();
    if (!var1.b_String.equals("") && var1.b_String.substring(0, 4).equals("http")) {
      try {
        this.a_u.a_MIDlet.platformRequest(var1.b_String);
      } catch (ConnectionNotFoundException var6) {
        var6.printStackTrace();
      }
    } else {
      r[] var5 = var2;
      u var4 = this.a_u;
      al var3 = var1;
      if (var1.a_Hashtable == null) {
        var1.a_Hashtable = new Hashtable();
      }

      if (var1.a_AString != null) {
        for (int var8 = 0; var8 < var3.a_AString.length; var8++) {
          var3.a_Hashtable.put(var3.a_AString[var8], an.a(var4.a_Hashtable, var3.a_AString[var8]));
        }
      }

      if (var2 != null) {
        for (int var9 = 0; var9 < var5.length; var9++) {
          var3.a_Hashtable.put(var5[var9].c(), var5[var9].a_String());
        }
      }

      byte[] var10 = null;

      try {
        var10 = this.a(var1);
      } catch (UnsupportedEncodingException var7) {
        var7.printStackTrace();
      }

      af var10000 = new af;
      String var12 = var1.b_String;
      if (var1.b_String == null || var12.trim().length() == 0) {
        var12 = "main";
      }

      var10000./* $VF: Unable to resugar constructor */<init>(this.a_u.g_I == 888888 ? af.b_String + var12 : af.a_String + var12, var10, this);
      af var11 = var10000;
      if (var10 == null) {
        var11.b("GET");
      } else {
        var11.b("POST");
      }

      this.a_ap.a(var11);
    }
  }

  public final void b() {
    this.a_ap.c();
  }

  public final void c() {
    this.a_ap.b();
  }

  private byte[] a(al var1) {
    if (var1 == null) {
      return null;
    } else {
      byte[] var2;
      if ((var2 = a(var1.a_Hashtable)) == null) {
        return null;
      } else {
        String var3 = this.a_u.a_String;
        byte[] var10000;
        if (this.a_u.a_String == null) {
          var10000 = null;
        } else {
          int var4;
          byte[] var5 = new byte[(var4 = var3.length()) << 2];
          int var6 = 0;

          for (int var7 = 0; var7 < var4; var7++) {
            char var8;
            if ((var8 = var3.charAt(var7)) < 128) {
              var5[var6++] = (byte)var8;
            } else if (var8 < 2048) {
              var5[var6++] = (byte)(var8 >> 6 & 31 | 192);
              var5[var6++] = (byte)(var8 & '?' | 128);
            } else if (var8 < 65536) {
              var5[var6++] = (byte)(var8 >> '\f' & 15 | 224);
              var5[var6++] = (byte)(var8 >> 6 & 63 | 128);
              var5[var6++] = (byte)(var8 & '?' | 128);
            } else if (var8 < 2097152) {
              var5[var6++] = (byte)(var8 >> 18 & 7 | 240);
              var5[var6++] = (byte)(var8 >> '\f' & 63 | 128);
              var5[var6++] = (byte)(var8 >> 6 & 63 | 128);
              var5[var6++] = (byte)(var8 & '?' | 128);
            }
          }

          byte[] var32 = new byte[var6];

          for (int var40 = 0; var40 < var6; var40++) {
            var32[var40] = var5[var40];
          }

          var10000 = var32;
        }

        byte[] var12 = var10000;
        int var20;
        var20 = (var20 = var10000.length) > 32 ? 32 : var20;
        String var23 = "";
        if (this.a_u.a_Hashtable.containsKey("_uuid")) {
          var23 = (String)this.a_u.a_Hashtable.get("_uuid");
        }

        byte[] var31 = new byte[0];

        try {
          var31 = var23.getBytes("UTF-8");
        } catch (Exception var10) {
          try {
            var31 = var23.getBytes("UTF-8");
          } catch (Exception var9) {
            var9.printStackTrace();
          }

          var10.printStackTrace();
        }

        int var33 = var20 + 25;
        var33 += 2;
        var33 += var31.length;
        var33 += 4;
        var33 += 2;
        var33 += var2.length;
        var33++;
        short var11 = var1.a_S;
        byte[] var24;
        (var24 = new byte[var33])[0] = 2;
        a(var24, 1, (long)var33);
        a(var24, 5, (short)100);
        a(var24, 7, 4L);
        a(var24, 11, (long)this.a_u.g_I);
        a(var24, 15, (long)this.a_u.f_I);
        a(var24, 19, (long)this.a_u.e_I);
        a(var24, 23, (short)var20);
        System.arraycopy(var12, 0, var24, 25, var20);
        int var13 = var20 + 25;
        a(var24, var13, (short)var31.length);
        int var14 = var13 + 2;
        System.arraycopy(var31, 0, var24, var14, var31.length);
        int var15 = var14 + var31.length;
        a(var24, var15, 0L);
        int var16 = var15 + 4;
        a(var24, var16, var11);
        int var17 = var16 + 2;
        System.arraycopy(var2, 0, var24, var17, var2.length);
        int var18 = var17 + var2.length;
        var24[var18] = 3;
        if (var33 != ++var18) {
          return null;
        } else {
          String var22 = "TT@Game911&007**";
          return new aj().a(var24, var22.getBytes());
        }
      }
    }
  }

  private static void a(byte[] var0, int var1, long var2) {
    var0[var1] = (byte)((int)(var2 >> 24 & 255L));
    var0[var1 + 1] = (byte)((int)(var2 >> 16 & 255L));
    var0[var1 + 2] = (byte)((int)(var2 >> 8 & 255L));
    var0[var1 + 3] = (byte)((int)(var2 & 255L));
  }

  private static void a(byte[] var0, int var1, short var2) {
    var0[var1] = (byte)(var2 >> 8);
    var0[var1 + 1] = (byte)var2;
  }

  public static short a(byte[] var0, int var1) {
    return (short)(var0[var1] << 8 | var0[var1 + 1]);
  }

  public static byte[] a(Hashtable var0) {
    if (var0 == null) {
      return null;
    } else {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      int var2 = var0.size();

      try {
        Enumeration var3 = var0.keys();
        byte[] var4;
        a(var4 = new byte[2], 0, (short)var2);
        var1.write(var4);

        while (var3.hasMoreElements()) {
          String var10;
          byte[] var5 = (var10 = (String)var3.nextElement()).getBytes("UTF-8");
          var2 = 1 + var5.length;
          var4 = ((String)var0.get(var10)).getBytes("UTF-8");
          var2 += 2;
          byte[] var9;
          (var9 = new byte[var2 + var4.length])[0] = (byte)var5.length;
          System.arraycopy(var5, 0, var9, 1, var5.length);
          int var12 = 1 + var5.length;
          a(var9, var12, (short)var4.length);
          int var13 = var12 + 2;
          System.arraycopy(var4, 0, var9, var13, var4.length);
          var1.write(var9);
        }

        return var1.toByteArray();
      } catch (Exception var6) {
        var6.printStackTrace();
        return null;
      }
    }
  }

  public ak() {
  }

  public static void a(int[] var0, String var1) {
    if (var0 == null) {
      System.out.println(var1 + " is null!");
    } else {
      System.out.println(var1 + "={");

      for (int var2 = 0; var2 < var0.length; var2++) {
        if (var2 < var0.length - 1) {
          System.out.println("" + var0[var2] + ",");
        } else {
          System.out.println("" + var0[var2]);
        }
      }

      System.out.println(" }");
      System.out.println("length=" + var0.length);
    }
  }

  public static void a(short[][] var0, String var1) {
    if (var0 == null) {
      System.out.println(var1 + " is null!");
    } else {
      System.out.println(var1 + "={");

      for (int var4 = 0; var4 < var0.length; var4++) {
        String var2 = "  {";

        for (int var3 = 0; var0[var4] != null && var3 < var0[var4].length; var3++) {
          if (var3 < var0[var4].length - 1) {
            var2 = var2 + "" + var0[var4][var3] + ",";
          } else {
            var2 = var2 + "" + var0[var4][var3];
          }
        }

        var2 = var2 + "}";
        if (var4 < var0.length - 1) {
          System.out.println(var2 + ",");
        } else {
          System.out.println(var2);
        }
      }

      System.out.println("}");
      System.out.println("length=" + var0.length);
    }
  }

  public static void a(String[] var0, String var1) {
    if (var0 == null) {
      System.out.println(var1 + " is null!");
    } else {
      System.out.println(var1 + "={");

      for (int var2 = 0; var2 < var0.length; var2++) {
        if (var2 < var0.length - 1) {
          System.out.println("" + var0[var2] + ",");
        } else {
          System.out.println("" + var0[var2]);
        }
      }

      System.out.println(" }");
      System.out.println("length=" + var0.length);
    }
  }
}
