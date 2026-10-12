import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;

public abstract class ai extends Canvas implements Runnable, CommandListener {
  public boolean a_Z;
  private boolean f_Z;
  protected boolean b_Z;
  public boolean c_Z;
  private String[] b_AString;
  protected boolean d_Z;
  protected u a_u;
  public an a_an;
  public static Vector b_Vector;
  protected int d_I;
  public static int e_I;
  public static int f_I;
  public static final Font a_Font = Font.getFont(0, 0, 8);
  protected int g_I;
  protected int h_I;
  private boolean g_Z;
  private int a_I;
  private int b_I;
  protected int i_I;
  protected int j_I;
  public static int k_I;
  private static String a_String = "Hủy";
  public static String[] a_AString = new String[]{"Có", "Không", "Đăng nhập", "Xác minh", "Trở về", "Menu", "OK", "Vào khu chuyên", "Đổi QQ", "Trở về", "", ""};
  protected byte a_B = -1;
  protected byte b_B = -1;
  public byte c_B;
  public byte d_B;
  private int[] b_AI;
  private int[] c_AI;
  private int[] d_AI;
  private int[] e_AI;
  protected int[] a_AI;
  private int[] f_AI;
  public TextBox a_TextBox;
  public Command a_Command;
  public Command b_Command;
  public Thread a_Thread;
  protected boolean e_Z;

  public ai(u var1) {
    this.setFullScreenMode(true);
    this.d_Z = true;
    this.f_Z = false;
    this.b_Z = false;
    this.a_Z = false;
    this.e_Z = false;
    this.a_u = var1;
    this.a_u.a_Z = this.hasPointerEvents();
    this.a_an = new an(var1);
  }

  public final void a(boolean var1) {
    e_I = this.a_u.a_I;
    f_I = this.a_u.b_I;
    vh.stringWidth(a_Font, "Quốc");
    vh.charWidth(a_Font, '*');
    k_I = vh.getHeight(a_Font) + 2;
    this.i_I = k_I;
    this.j_I = k_I + 10;
    var1 = vh.getHeight(a_Font) + 4;
    this.b_AI = a(12, 3164256, 1650754);
    this.c_AI = a(e_I / 4, 2311520, 7709902);
    this.f_AI = a((f_I - this.j_I - this.i_I) / 4 + 1, 733778, 1395578);
    int var2 = var1 > 18 ? 16 : var1 - 2;
    this.d_AI = a(var2, 2915263, 1264507);
    this.a_AI = a(var1, 3318527, 1133951);
    a(var1, 42492, 92087);
    this.e_AI = a(var1, 938626, 866139);
  }

  public abstract void f();

  public final void a(v var1) {
    this.a_an.a_v = var1;
  }

  protected void paint(Graphics var1) {
    this.a();
    if (this.d_Z) {
      if (this.a_u.a_Z && this.g_Z) {
        byte var4 = 0;
        if (a(this.a_I, this.b_I, 0, f_I - this.j_I, 25, this.j_I)) {
          var4 = 1;
        } else if (a(this.a_I, this.b_I, e_I - 25, f_I - this.j_I, 25, this.j_I)) {
          var4 = 2;
        }

        switch (var4) {
          case 1:
            this.g_I = -506;
            this.a();
            break;
          case 2:
            this.g_I = -507;
            this.a();
        }

        this.b();
      }

      var1.setColor(1397123);
      var1.fillRect(0, 0, e_I, f_I);
      var1.setColor(0);
      var1.drawRect(0, 0, e_I - 1, f_I - 1);
      this.a_an.d();
      Graphics var3 = var1;
      ai var2 = this;
      int var8 = this.f_AI.length;
      int var5 = this.i_I;
      int var6 = f_I - this.j_I;

      for (int var7 = 0; var5 < var6 && var7 < var8; var7++) {
        var3.setColor(var2.f_AI[var7]);
        var3.fillRect(0, var5, e_I, 4);
        var5 += 4;
      }

      if (var5 < var6) {
        var3.setColor(var2.f_AI[var8 - 1]);
        var3.fillRect(0, var5, e_I, var6 - var5);
      }

      this.a(var1);
      this.a(var1, f_I - this.j_I, e_I, this.j_I);
      if (this.a_an.a_I == 1) {
        var8 = e_I - 84 > 112 ? 112 : e_I - 84;
        var5 = e_I - var8 >> 1;
        var6 = f_I - this.j_I;
        var1.setColor(268068);
        var1.fillRect(var5, var6 + 2, var8, k_I);
        a(var1, var5 + 1, var6 + 2, this.a_an.b_I * var8 / 100, k_I, this.d_AI);
        var1.setColor(1782338);
        var1.drawRect(var5, var6 + 2, var8, k_I);
        var1.setColor(8829414);
        if (this.a_an.b_I != 100) {
          vh.drawString(var1, "Đang kết nối..." + this.a_an.b_I + "%", e_I >> 1, var6 + 2, 17);
        }

        a(var1, a_String, e_I - 5, f_I - this.j_I + 4, 6177536, 16770602, 24);
      } else if (this.b_B != -1) {
        a(var1, a_AString[this.b_B], e_I - 5, f_I - this.j_I + 4, 6177536, 16770602, 24);
      }

      if (this.a_B != -1) {
        a(var1, a_AString[this.a_B], 5, f_I - this.j_I + 4, 6177536, 16770602, 20);
      }

      if (this.f_Z) {
        this.b(var1);
      }
    }
  }

  public abstract void a(Graphics var1);

  public final void a(Graphics var1, int var2, int var3, int var4) {
    int var5 = 0;
    var1.setColor(202277);
    var1.drawLine(0, var2, var3, var2);
    var5++;

    for (int var6 = 0; var6 < this.c_AI.length; var6++) {
      var1.setColor(this.c_AI[var6]);
      var1.drawLine(var6 << 1, var2 + 1, (var6 << 1) + 1, var2 + 1);
      var1.drawLine(var3 - (var6 << 1) - 1, var2 + 1, var3 - (var6 << 1) - 2, var2 + 1);
    }

    var5++;

    for (int var9 = 1; var9 < this.b_AI.length; var9++) {
      var1.setColor(this.b_AI[var9]);
      var1.drawLine(0, var2 + var5, var3, var2 + var5);
      var5++;
    }

    if (var4 > var5) {
      var1.setColor(70433);
      var1.fillRect(0, var2 + var5, var3, var4 - var5);
    }
  }

  public static void a(Graphics var0, int var1, int var2, int var3, int var4, int[] var5) {
    int var7;
    if ((var7 = var5.length) > var4) {
      var7 = var4;
    }

    int var6;
    for (var6 = 0; var6 < var7; var6++) {
      var0.setColor(var5[var6]);
      var0.drawLine(var1, var2 + var6, var1 + var3, var2 + var6);
    }

    if (var4 > var7) {
      while (var6 < var4) {
        var0.setColor(var5[var7 - 1]);
        var0.drawLine(var1, var2 + var6, var1 + var3, var2 + var6);
        var6++;
      }
    }
  }

  public void run() {
    while (this.d_Z) {
      try {
        Thread.sleep(100L);
        this.repaint();
      } catch (Exception var1) {
        var1.printStackTrace();
      }
    }
  }

  protected final void a(byte var1, byte var2) {
    this.c_B = this.a_B;
    this.d_B = this.b_B;
    this.a_B = var1;
    this.b_B = var2;
  }

  public abstract void a_V(int var1);

  private static void a(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6) {
    var0.setColor(6177536);
    vh.drawString(var0, var1, var2 - 1, var3 - 1, var6);
    vh.drawString(var0, var1, var2 + 1, var3 + 1, var6);
    var0.setColor(16770602);
    vh.drawString(var0, var1, var2, var3, var6);
  }

  private static int[] a(int var0, int var1, int var2) {
    int var3 = var1 >> 16;
    int var4 = var1 >> 8 & 0xFF;
    int var5 = var1 & 0xFF;
    int var6 = var2 >> 16;
    int var7 = var2 >> 8 & 0xFF;
    int var8 = var2 & 0xFF;
    var6 = (var6 - var3) * 100 / (var0 - 2);
    var7 = (var7 - var4) * 100 / (var0 - 2);
    var8 = (var8 - var5) * 100 / (var0 - 2);
    int[] var9;
    (var9 = new int[var0])[0] = var1;
    var9[var0 - 1] = var2;

    for (int var10 = 1; var10 < var0 - 1; var10++) {
      var9[var10] = var3 + var10 * var6 / 100 << 16 | var4 + var10 * var7 / 100 << 8 | var5 + var10 * var8 / 100;
    }

    return var9;
  }

  protected void keyPressed(int var1) {
    this.g_I = var1;
    ai var3 = this;
    if (this.g_I == this.a_u.c_I) {
      this.g_I = -506;
    } else if (this.g_I == this.a_u.d_I) {
      this.g_I = -507;
    } else {
      try {
        var3.h_I = var3.getGameAction(var3.g_I);
      } catch (Exception var2) {
        var2.printStackTrace();
      }

      switch (this.h_I) {
        case 1:
          if (this.g_I != 50) {
            this.g_I = -501;
            return;
          }
          break;
        case 2:
          if (this.g_I != 52) {
            this.g_I = -503;
            return;
          }
        case 3:
        case 4:
        case 7:
        default:
          break;
        case 5:
          if (this.g_I != 54) {
            this.g_I = -504;
            return;
          }
          break;
        case 6:
          if (this.g_I != 56) {
            this.g_I = -502;
            return;
          }
          break;
        case 8:
          if (this.g_I != 53) {
            this.g_I = -505;
          }
      }
    }
  }

  protected void pointerPressed(int var1, int var2) {
    this.g_Z = true;
    this.a_I = var1;
    this.b_I = var2;
  }

  private void a() {
    if (this.g_I != -9999) {
      if (this.f_Z) {
        label27:
        switch (this.g_I) {
          case -507:
            switch (this.b_B) {
              case 1:
                this.f_Z = false;
                this.b_AString = null;
                this.a(this.c_B, this.d_B);
              default:
                break label27;
            }
          case -506:
            switch (this.a_B) {
              case 0:
                if (this.b_Z) {
                  this.b_Z = false;
                  this.f_Z = false;
                  this.b_AString = null;
                  this.a(this.c_B, this.d_B);
                  this.a_an.f();
                  this.a_an.a(102);
                } else if (this.c_Z) {
                  this.e();
                  this.a_u.a_Display.setCurrent(this.a_u.a_Displayable);
                } else {
                  u.a(ak.a(this.a_u.a_Hashtable));
                  this.a_u.a_MIDlet.notifyDestroyed();
                }
                break;
              case 6:
                this.f_Z = false;
                this.b_AString = null;
                this.a(this.c_B, this.d_B);
            }
        }
      } else {
        this.d();
      }

      this.g_I = -9999;
      this.h_I = -9999;
    }
  }

  protected abstract void d();

  private void b() {
    this.g_Z = false;
    this.a_I = -1;
    this.b_I = -1;
  }

  public abstract void e();

  public abstract void b(String var1);

  public final void a(String var1, int var2) {
    this.a((byte)0, (byte)1);
    this.b_AString = ad.a(var1, a_Font, e_I - 20);
    this.f_Z = true;
  }

  private void b(Graphics var1) {
    int var2 = (k_I + 2) * (this.b_AString.length + 1) + 2;
    int var3 = f_I - var2 - this.j_I;
    var1.setColor(666180);
    var1.drawLine(0, var3, e_I, var3);
    var3++;
    var1.setColor(2448780);
    var1.drawLine(0, var3, e_I, var3);
    a(var1, 0, ++var3, e_I, var2 - 2, this.e_AI);
    var3 += (k_I + 2) / 2;

    for (int var4 = 0; var4 < this.b_AString.length; var4++) {
      if (var4 == 0) {
        a(var1, "Gợi ý : " + this.b_AString[var4], 12, var3, 6177536, 16770602, 20);
      } else {
        a(var1, this.b_AString[var4], 24, var3, 6177536, 16770602, 20);
      }

      var3 += k_I + 2;
    }
  }

  private static boolean a(int var0, int var1, int var2, int var3, int var4, int var5) {
    return var0 >= var2 && var0 <= var2 + 25 && var1 >= var3 && var1 <= var3 + var5;
  }

  protected final void g() {
    this.d_Z = false;
    this.e_Z = false;
    this.f_Z = false;
    u.b_Z = false;
    this.b_AString = null;
    b_Vector.removeAllElements();
    b_Vector = null;
    this.b_AI = null;
    this.c_AI = null;
    this.d_AI = null;
    this.e_AI = null;
    this.a_AI = null;
    this.f_AI = null;
  }
}
