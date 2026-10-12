import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

public final class u {
  private static RecordStore a_RecordStore = null;
  private String b_String;
  private String c_String;
  public int a_I;
  public int b_I;
  private String d_String;
  private String e_String;
  private String f_String;
  public boolean a_Z;
  public Hashtable a_Hashtable = this.a_Hashtable();
  public Display a_Display;
  public Displayable a_Displayable;
  public MIDlet a_MIDlet;
  public final int c_I;
  public final int d_I;
  public int e_I;
  public int f_I;
  public int g_I;
  public String a_String;
  private ai a_ai;
  private v a_v;
  public int h_I;
  public static boolean b_Z = false;

  public u(MIDlet var1, Display var2, Displayable var3, v var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, String var12) {
    if (this.a_Hashtable == null) {
      this.a_Hashtable = new Hashtable();
    }

    int var13;
    String var18;
    if ((var13 = (var18 = System.getProperty("microedition.platform")).indexOf(47)) > 0) {
      var18 = var18.substring(0, var13 - 1);
    }

    this.b_String = "SN2010-" + var18 + "/" + 4;
    this.a_Display = var2;
    this.a_Displayable = var3;
    this.a_MIDlet = var1;
    this.a_I = var5;
    this.b_I = var6;
    this.c_I = var7;
    this.d_I = var8;
    this.e_I = var9;
    this.f_I = var10;
    this.g_I = 888888;
    this.a_String = var12;
    this.c_String = this.a_I + "X" + this.b_I;
    String var14 = "Quốc";
    Font var15 = Font.getFont(0, 0, 16);
    this.d_String = vh.stringWidth(var15, var14) + "X" + vh.getHeight(var15);
    Font var16 = Font.getFont(0, 0, 0);
    this.e_String = vh.stringWidth(var16, var14) + "X" + vh.getHeight(var16);
    Font var17 = Font.getFont(0, 0, 8);
    this.f_String = vh.stringWidth(var17, var14) + "X" + vh.getHeight(var17);
    this.a_v = var4;
    this.a_ai = null;
    this.a_ai = new ac(this);
    this.a_ai.a(this.a_v);
  }

  public final String a_String() {
    return this.b_String;
  }

  public final String b() {
    return this.c_String;
  }

  public final String c() {
    return this.d_String;
  }

  public final String d() {
    return this.e_String;
  }

  public final String e() {
    return this.f_String;
  }

  public final boolean a_Z() {
    return this.a_Hashtable.get("_uid") != null && this.a_Hashtable.get("_uid").toString().length() > 0;
  }

  public final int a_I() {
    this.a_ai.a_V(2);
    return 1;
  }

  private void a_V() {
    if (this.a_Display.getCurrent() != this.a_ai) {
      b_Z = true;
      if (this.a_ai.c_Z) {
        this.a_ai.f();
      }

      this.a_Display.setCurrent(this.a_ai);
      this.a_ai.a_Thread = new Thread(this.a_ai);
      this.a_ai.d_Z = true;
      this.a_ai.a_Thread.start();
      this.a_ai.a_an.a_V();
    }
  }

  public static void a(byte[] var0) {
    try {
      a_RecordStore().setRecord(1, var0, 0, var0.length);
    } catch (Exception var1) {
      var1.printStackTrace();
    }
  }

  private static RecordStore a_RecordStore() {
    if (a_RecordStore == null) {
      a_RecordStore = RecordStore.openRecordStore("mboxclient", true);
      byte[] var0 = new byte[0];
      a_RecordStore.addRecord(var0, 0, 0);
    }

    return a_RecordStore;
  }

  private Hashtable a_Hashtable() {
    Hashtable var1 = new Hashtable();
    byte[] var2;
    if ((var2 = a_AB()) == null) {
      var1.put("_vCInterval", "5");
      var1.put("_vTInterval", "3");
      var1.put("_vertifyValue", "0");
      var1.put("_lastvTime", "0");
      return var1;
    } else {
      try {
        ByteArrayInputStream var8 = new ByteArrayInputStream(var2);
        DataInputStream var9;
        short var3 = (var9 = new DataInputStream(var8)).readShort();

        for (int var4 = 0; var4 < var3; var4++) {
          byte var5;
          String var11 = a(a(var5 = var9.readByte(), var9), var5);
          short var6;
          String var12 = a(a(var6 = var9.readShort(), var9), var6);
          var1.put(var11, var12);
        }
      } catch (Exception var7) {
      }

      String var10;
      if ((var10 = (String)var1.get("_server")) != null && var10.length() > 0) {
        af.a_String = var10;
      }

      return var1;
    }
  }

  private static byte[] a_AB() {
    byte[] var0 = null;

    try {
      var0 = a_RecordStore().getRecord(1);
    } catch (Exception var1) {
      var1.printStackTrace();
    }

    return var0 != null && var0.length != 0 ? var0 : null;
  }

  // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
  // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
  private static String a(byte[] var0, int var1) {
    String var2 = "";
    ByteArrayInputStream var3 = null;
    DataInputStream var4 = null;
    boolean var15 = false /* VF: Semaphore variable */;

    label115: {
      try {
        var15 = true;
        byte[] var5;
        (var5 = new byte[var1 + 2])[0] = (byte)(var1 >> 8);
        var5[1] = (byte)var1;
        System.arraycopy(var0, 0, var5, 2, var1);
        var3 = new ByteArrayInputStream(var5);
        var2 = (var4 = new DataInputStream(var3)).readUTF();
        var4.close();
        var4 = null;
        var3.close();
        var3 = null;
        var15 = false;
        return var2;
      } catch (IOException var22) {
        var15 = false;
      } catch (Exception var23) {
        var15 = false;
        break label115;
      } finally {
        if (var15) {
          if (var4 != null) {
            try {
              var4.close();
            } catch (IOException var17) {
            }
          }

          if (var3 != null) {
            try {
              var3.close();
            } catch (IOException var16) {
            }
          }
        }
      }

      if (var4 != null) {
        try {
          var4.close();
        } catch (IOException var21) {
        }
      }

      if (var3 != null) {
        try {
          var3.close();
        } catch (IOException var20) {
        }

        return var2;
      }

      return var2;
    }

    if (var4 != null) {
      try {
        var4.close();
      } catch (IOException var19) {
      }
    }

    if (var3 != null) {
      try {
        var3.close();
      } catch (IOException var18) {
      }
    }

    return var2;
  }

  private static byte[] a(int var0, DataInputStream var1) {
    if (var0 > 0 && var1 != null) {
      byte[] var2 = null;

      try {
        var2 = new byte[var0];

        for (int var3 = 0; var3 < var0; var3++) {
          var2[var3] = var1.readByte();
        }
      } catch (IOException var4) {
      }

      return var2;
    } else {
      return null;
    }
  }

  protected final void a(int var1, int var2) {
    switch (var2) {
      case 17:
        if ((var2 = this.a_I(var2)) == -1) {
          this.a_v.a(1, new by(0, 0, null));
          return;
        }
        break;
      case 18:
        if ((var2 = this.a_I(var2)) == -1) {
          this.a_ai.e();
          this.a_v.a(2, new by(0, 0, null));
          return;
        }
    }

    this.a_V();
    this.h_I = var2;
    if (var1 == 2) {
      this.a_ai.a_V(0);
    } else {
      ((ac)this.a_ai).b_V(var2);
      this.a_ai.a_V(1);
    }
  }

  private int a_I(int var1) {
    int var2 = -1;
    switch (var1) {
      case 17:
        String var12 = (String)this.a_Hashtable.get("_uid");
        String var13 = (String)this.a_Hashtable.get("_sid");
        if (var12 == null || var12.length() == 0 || var13 == null || var13.length() == 0) {
          var2 = var1;
        }

        return var2;
      case 18:
        String var7 = (String)this.a_Hashtable.get("_vertifyUser");
        String var3 = (String)this.a_Hashtable.get("_vertifyValue");
        String var4 = (String)this.a_Hashtable.get("_lastvTime");
        String var5 = (String)this.a_Hashtable.get("_vCInterval");
        String var6 = (String)this.a_Hashtable.get("_vTInterval");
        if (var7 == null || Integer.parseInt(var7) == 0) {
          return var1;
        } else if ((var2 = Integer.parseInt(var3)) >= Integer.parseInt(var5)) {
          return var1;
        } else {
          Calendar var10;
          int var11;
          if ((var11 = (var10 = Calendar.getInstance()).get(1) * 10000 + (var10.get(2) + 1) * 100 + var10.get(5) - Integer.parseInt(var4))
              >= Integer.parseInt(var6)
            || var11 < 0) {
            return var1;
          } else {
            this.a_Hashtable.put("_vertifyValue", String.valueOf(++var2));
          }
        }
      default:
        return -1;
    }
  }

  protected final void a_V(int var1) {
    this.a_ai.a_an.a_V();
    this.a_ai.a_an.a(var1);
  }

  protected final void a(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
    this.a_V();
    ((ac)this.a_ai).a_V("Đang kết nối... Vui lòng đợi!");
    this.a_ai.a_V(1);
    this.a_ai.a_an.a(var1, var2, var3, var4, var5, var6, var7);
  }

  public final ac a_ac() {
    return (ac)this.a_ai;
  }

  public final ai a_ai() {
    return this.a_ai;
  }

  public final cb a_cb() {
    return this.a_ai.a_an.a_cb();
  }
}
