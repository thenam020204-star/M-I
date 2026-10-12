import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

public final class bk implements v {
  private byte a_B;
  private static byte b_B = 0;
  private static byte c_B = 1;
  private static byte d_B = 2;
  private static byte e_B = 3;
  private byte f_B;
  private static byte g_B = 0;
  private static byte h_B = 1;
  private static byte i_B = 2;
  private static byte j_B = 3;
  private static byte k_B = 4;
  private static byte l_B = 5;
  private short a_S;
  private short b_S;
  private boolean a_Z;
  private int a_I;
  private short c_S;
  private int b_I;
  private int c_I;
  private int d_I;
  private int e_I;
  private int f_I;
  private int g_I;
  private int h_I;
  private int i_I;
  private cb a_cb;
  private dg[] a_Adg;
  private long a_J;
  private String a_String;
  private static bk a_bk;
  private static cw a_cw;
  private byte[] a_AB;
  private byte[] b_AB;
  private StringBuffer a_StringBuffer = new StringBuffer(".");

  private bk(short var1, short var2, boolean var3) {
    this.a_S = var1;
    this.b_S = var2;
    this.a_Z = var3;
    this.c_S = (short)((var1 << 2) / 5);
  }

  private boolean b_Z(int var1) {
    boolean var2 = true;
    if (this.f_B == j_B) {
      if (var1 == b.a_S) {
        this.c();
        b_V(12);
      } else if (var1 == b.b_S) {
        this.c();
      }
    } else if (this.f_B == i_B) {
      if (var1 == b.a_S) {
        this.c();
        b_V(16);
      } else if (var1 == b.b_S) {
        this.c();
      }
    } else if (this.f_B == k_B) {
      this.c();
    } else if (this.f_B == h_B) {
      this.c();
    } else if (this.f_B == l_B) {
      if (var1 == b.a_S) {
        a_V(this.a_I);
      } else if (var1 == b.b_S) {
        this.c();
      }
    } else if (this.f_B == 0) {
      if (this.a_B == e_B) {
        this.c();
      } else {
        var2 = false;
      }
    }

    return var2;
  }

  public final void a(int var1, by var2) {
    int var3 = var2.a_I();
    String var4 = var2.a_String();
    System.out.println("Loại dữ liệu:" + var1);
    System.out.println("Kết quả dữ liệu:" + var3);
    System.out.println("Thông tin dữ liệu:" + var4);
    if (var3 == 0) {
      if (var1 == 7) {
        this.a_cb = a_cw.a();
      } else if (var1 == 12) {
        this.a(var4);
      } else if (var1 == 3) {
        this.a_String = var4;
      }

      this.f_B = j_B;
    } else {
      if (var3 == -1) {
        this.f_B = i_B;
      }
    }
  }

  private static void b_V(int var0) {
    a_cw.a(1, var0);
  }

  private void c() {
    this.a_B = 0;
    this.f_B = 0;
    this.a_cb = null;
    this.a_Adg = null;
    this.a_J = 0L;
  }

  private void a(Graphics var1, String var2, int var3, int var4) {
    String[] var6 = cg.a(var2, this.c_S);
    var1.setColor(16777215);

    for (byte var5 = 0; var5 < var6.length; var5++) {
      vh.drawString(var1, var6[var5], var3, var4 - (cg.b_S + 2) * (var6.length - 1 - var5), 33);
    }
  }

  private void a(String var1) {
    String[] var4;
    if ((var4 = cg.b_AString(var1, "|#|")) != null) {
      this.a_Adg = new dg[var4.length];

      for (byte var2 = 0; var4 != null && var2 < var4.length; var2++) {
        String[] var3 = cg.b_AString(var4[var2], "|");
        this.a_Adg[var2] = new dg();
        this.a_Adg[var2].a_I = Integer.parseInt(var3[0]);
        this.a_Adg[var2].b_String = var3[1];
      }
    }
  }

  private void b(Graphics var1) {
    this.b_I = cg.a_S * 3;
    this.c_I = cg.a_S * 7;
    this.d_I = cg.a_S << 1;
    this.e_I = this.b_I + this.c_I + this.d_I;
    this.f_I = this.e_I < this.c_S ? this.e_I : this.c_S;
    this.c_I = this.f_I - this.b_I - this.d_I;
    this.g_I = (this.a_S - this.f_I) / 2;
    this.h_I = (byte)((this.b_S / cg.b_S << 2) / 5);
    this.i_I = (this.b_S - this.h_I * cg.b_S) / 2;
    this.h_I = this.h_I < this.a_Adg.length ? this.h_I : this.a_Adg.length;
    var1.setColor(16777215);
    vh.drawString(var1, "Bảng xếp hạng", this.a_S / 2, (this.i_I - cg.b_S) / 2, 17);
    vh.drawString(var1, "Hạng", this.g_I, this.i_I, 20);
    vh.drawString(var1, "Tên", this.g_I + this.b_I, this.i_I, 20);
    vh.drawString(var1, "Điểm", this.g_I + this.b_I + this.c_I, this.i_I, 20);

    for (int var2 = 0; this.a_Adg != null && var2 < this.h_I; var2++) {
      vh.drawString(var1, "" + (var2 + 1), this.g_I + cg.a_S, this.i_I + cg.b_S * (var2 + 1), 17);
      vh.drawString(var1, "" + this.a_Adg[var2].a_I, this.g_I + this.b_I + this.c_I, this.i_I + cg.b_S * (var2 + 1), 20);
    }

    for (int var7 = 0; this.a_Adg != null && var7 < this.h_I; var7++) {
      if (this.a_AB == null) {
        this.a_AB = new byte[this.h_I];
        this.b_AB = new byte[this.h_I];
      }

      int var6 = this.c_I - cg.a_S / 2;
      var1.setClip(this.g_I + this.b_I, this.i_I + cg.b_S, var6, this.b_S);
      vh.drawString(var1, this.a_Adg[var7].b_String, this.g_I + this.b_I + this.a_AB[var7], this.i_I + cg.b_S * (var7 + 1), 20);
      if (vh.stringWidth(cg.a_Font, this.a_Adg[var7].b_String) > var6) {
        if (Math.abs(this.a_AB[var7]) < vh.stringWidth(cg.a_Font, this.a_Adg[var7].b_String) - var6) {
          if (this.b_AB[var7] > 0) {
            this.b_AB[var7]--;
          } else {
            this.a_AB[var7]--;
          }
        } else if (this.b_AB[var7] < 30) {
          this.b_AB[var7]++;
        } else {
          this.a_AB[var7] = 0;
        }
      }

      var1.setClip(0, 0, this.a_S, this.b_S);
    }
  }

  private void c(Graphics var1) {
    this.a(var1, "Có", "Không");
  }

  private void a(Graphics var1, String var2, String var3) {
    var1.setColor(16777215);
    if (this.a_Z) {
      vh.drawString(var1, var3, 5, this.b_S - 5, 36);
      if (var2 != null) {
        vh.drawString(var1, var2, this.a_S - 5, this.b_S - 5, 40);
        return;
      }
    } else {
      if (var2 != null) {
        vh.drawString(var1, var2, 5, this.b_S - 5, 36);
      }

      vh.drawString(var1, var3, this.a_S - 5, this.b_S - 5, 40);
    }
  }

  public static void a(MIDlet var0, Display var1, Displayable var2, short var3, short var4, boolean var5, int var6, int var7) {
    a_bk = new bk(var3, var4, false);
    a_cw = new cw(var0, var1, var2, a_bk, var3, var4, b.a_AS[0], b.b_AS[0], var6, 120, "");
  }

  public static void a(Graphics var0) {
    if (a_bk != null) {
      bk var10 = a_bk;
      if (a_bk.f_B != 0 || var10.a_B == e_B) {
        short var5 = var10.b_S;
        short var4 = var10.a_S;
        byte var2 = 0;
        var2 = (boolean)0;
        Graphics var3 = var0;
        if ((var2 = (byte)(var5 / 44)) * 22 << 1 < var5) {
          var2++;
        }

        byte var14;
        if ((var14 = (byte)(var4 / 44)) * 22 << 1 < var4) {
          var14++;
        }

        for (int var13 = 0; var13 < var14; var13++) {
          for (int var6 = 0; var6 < var2; var6++) {
            int var10001 = 0 + (var13 * 22 << 1);
            int var9 = 0 + (var6 * 22 << 1);
            int var8 = var10001;
            var3.setColor(1259628);
            var3.fillRect(var8, var9, 22, 22);
            var3.fillRect(var8 + 22, var9 + 22, 22, 22);
            var3.setColor(996456);
            var3.fillRect(var8 + 22, var9, 22, 22);
            var3.fillRect(var8, var9 + 22, 22, 22);
          }
        }

        if (var10.f_B == 0) {
          if (var10.a_B == e_B) {
            var10.a(var0, "Nhấn phím bất kỳ để về trò chơi", var10.a_S / 2, var10.b_S / 2);
            return;
          }
        } else {
          if (var10.f_B == h_B) {
            var0.setColor(16777215);
            var10.a_StringBuffer.append(".");
            if (var10.a_StringBuffer.toString().length() > 5) {
              var10.a_StringBuffer = new StringBuffer(".");
            }

            vh.drawString(
              var0,
              "Đang đọc dữ liệu " + var10.a_StringBuffer.toString(),
              (var10.a_S - vh.stringWidth(cg.a_Font, "Đang đọc dữ liệu.....")) / 2,
              var10.b_S / 2,
              36
            );
            vh.drawString(var0, "(Nhấn phím bất kỳ để thoát)", var10.a_S / 2, var10.b_S / 2 + cg.b_S, 17);
            if (System.currentTimeMillis() - var10.a_J > 10000L) {
              System.out.println("Bắt đầu:" + var10.a_J);
              System.out.println("Kết thúc:" + System.currentTimeMillis());
              var10.f_B = k_B;
            }

            return;
          }

          if (var10.f_B == j_B) {
            if (var10.a_B == c_B) {
              var0.setColor(16777215);
              vh.drawString(var0, "Tên QQ:" + var10.a_cb.a_String, 5, var10.b_S / 2 - cg.b_S, 36);
              vh.drawString(var0, "Cấp:" + var10.a_cb.b_I, 5, var10.b_S / 2, 36);
              vh.drawString(var0, "Điểm cao nhất:" + var10.a_cb.a_I, 5, var10.b_S / 2 + cg.b_S, 36);
              vh.drawString(var0, "Điểm hiện tại:" + 0, 5, var10.b_S / 2 + (cg.b_S << 1), 36);
            } else if (var10.a_B == d_B) {
              if (var10.a_Adg != null) {
                var10.b(var0);
              } else {
                var10.a(var0, "Chưa ai lên bảng, mau giành hạng nhất nào!", var10.a_S / 2, (var10.a_S - cg.b_S) / 2);
              }
            } else if (var10.a_B == e_B) {
              var10.a(var0, var10.a_String, var10.a_S / 2, var10.b_S / 2);
            }

            var10.a(var0, "Cộng đồng QQ", "Trở về");
            return;
          }

          if (var10.f_B == i_B) {
            var10.a(var0, "Chưa đăng nhập, đăng nhập ngay?", var10.a_S / 2, var10.b_S / 2);
            var10.c(var0);
            return;
          }

          if (var10.f_B == k_B) {
            var10.a(var0, "Hết thời gian lấy dữ liệu, hãy kiểm tra cài đặt mạng!", var10.a_S / 2, var10.b_S / 2 - cg.b_S);
            vh.drawString(var0, "Nhấn phím bất kỳ để tiếp tục", var10.a_S / 2, var10.b_S / 2, 33);
            return;
          }

          if (var10.f_B == l_B) {
            var10.a(var0, "Tải lên thất bại, thử lại?", var10.a_S / 2, var10.b_S / 2);
            var10.c(var0);
          }
        }
      }
    }
  }

  public static boolean a_Z(int var0) {
    boolean var1 = false;
    if (a_bk != null) {
      var1 = a_bk.b_Z(var0);
    }

    return var1;
  }

  public static boolean a(int var0, int var1) {
    if (a_bk != null) {
      bk var4 = a_bk;
      boolean var3 = false;
      if (cg.a(0, var4.b_S - 22, 22, 22, var0, var1, 1, 1)) {
        if (var4.a_Z) {
          var4.b_Z(b.b_S);
        } else {
          var4.b_Z(b.a_S);
        }

        var3 = true;
      } else if (cg.a(var4.a_S - 22, var4.b_S - 22, 22, 22, var0, var1, 1, 1)) {
        if (var4.a_Z) {
          var4.b_Z(b.a_S);
        } else {
          var4.b_Z(b.b_S);
        }

        var3 = true;
      }
    }

    return false;
  }

  public static void a() {
    b_V(10);
  }

  public static void b() {
    short var0 = 2005;
    bk var1 = a_bk;
    a_bk.a_J = System.currentTimeMillis();
    a_cw.a(2005);
    a_bk.a_B = d_B;
    a_bk.f_B = h_B;
  }

  public static void a_V(int var0) {
    String var1 = "";
    var0 = var0;
    a_bk.a_B = e_B;
    if (var0 <= 0) {
      a_bk.a_String = "Điểm hiện tại của bạn là 0, tải lên không hợp lệ.";
      a_bk.f_B = j_B;
    } else {
      a_bk.a_J = System.currentTimeMillis();

      try {
        a_bk.a_I = var0;
        if (var1 != null && !var1.equals("")) {
          var1 = "1:" + var1;
        }

        a_cw.a(var0, "", 0, 0, "", "", var1);
        a_bk.f_B = 0;
      } catch (Exception var2) {
        var2.printStackTrace();
        a_bk.f_B = l_B;
      }
    }
  }
}
