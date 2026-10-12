import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextBox;

public final class ac extends ai {
  public static int a_I;
  private int l_I;
  private int m_I;
  private int n_I;
  private int o_I;
  private int p_I;
  private int q_I;
  private int r_I;
  private int s_I;
  private int t_I;
  private int u_I;
  private int v_I;
  public int b_I;
  private int w_I;
  private int x_I;
  private int y_I;
  private int z_I;
  public int c_I;
  private int A_I;
  private boolean f_Z;
  private boolean g_Z;
  private int B_I;
  public Vector a_Vector;
  private Vector c_Vector;
  private Vector d_Vector;
  public al a_al;
  public static aq a_aq;
  public static Image a_Image;
  private boolean h_Z;
  private boolean i_Z;
  private al b_al;
  private al c_al;
  private String[] b_AString = new String[]{"1.Trang chủ", "2.Làm mới", "3.Tiến", "4.Trợ giúp", "5.Đăng xuất", "6.Về trò chơi"};
  private int C_I = 1991325;
  private int D_I = 731458;
  private int E_I = 8829158;
  private int F_I = 16777215;
  private boolean j_Z;

  public ac(u var1) {
    super(var1);
    super.c_Z = true;
  }

  public final void a(Graphics var1) {
    var1.setFont(ai.a_Font);
    this.a(var1, 0, ai.e_I, super.i_I);
    if (this.a_Vector.size() > 0) {
      var1.setClip(0, super.i_I, ai.e_I, this.w_I);

      for (int var3 = 0; var3 < this.a_Vector.size(); var3++) {
        ((ag)this.a_Vector.elementAt(var3)).a(var1);
      }

      r var10;
      if (this.b_Z() && (var10 = (r)this.a_Vector.elementAt(this.B_I)).a_Z && var10.c_I == 0) {
        var10.b(var1);
      }

      var1.setClip(0, 0, ai.e_I, ai.f_I);
    }

    if (var1.getFont() != ai.a_Font) {
      var1.setFont(ai.a_Font);
    }

    if (this.f_Z) {
      var1.setClip(this.p_I, this.q_I, this.n_I, this.o_I);
      int var4 = this.t_I * 10000 / (this.u_I - this.w_I) * (this.o_I - this.s_I) / 10000;
      var1.setColor(464686);
      var1.fillRect(this.p_I, this.q_I, this.n_I, this.o_I);
      var1.setColor(1657987);
      var1.drawRect(this.p_I, this.q_I, this.n_I, this.o_I);
      var1.setColor(1587307);
      var1.fillRect(this.p_I + 2, this.q_I + 1 + var4, this.r_I, this.s_I);
      var1.setClip(0, 0, ai.e_I, ai.f_I);
    }

    Graphics var11 = var1;
    ac var2 = this;
    if (super.a_Z) {
      int var8 = ai.k_I * this.b_AString.length + 6;
      int var12 = ai.f_I - super.j_I - var8;
      int var5;
      int var6 = (var5 = vh.stringWidth(ai.a_Font, this.b_AString[0])) + 20;
      var1.setColor(this.D_I);
      var1.fillRect(1, var12, var6, var8);
      var1.setColor(534079);
      var1.drawRect(1, var12, var6 - 1, var8 - 1);
      var1.setColor(this.C_I);
      var1.drawRect(2, var12 + 1, var6 - 3, var8 - 3);
      var1.setColor(1991325);
      var1.drawRect(2, var12 + 1, var6 - 3, var8 - 3);

      for (int var9 = 0; var9 < var2.b_AString.length; var9++) {
        if (var9 == var2.m_I) {
          ai.a(var11, 4, var12 + 2 + ai.k_I * var9, var6 - 7, ai.k_I, var2.a_AI);
          var11.setColor(var2.F_I);
        } else {
          var11.setColor(var2.E_I);
        }

        if (var9 == 2 && var2.A_I >= var2.d_Vector.size() - 1) {
          var11.setColor(9803157);
        }

        vh.drawString(var11, var2.b_AString[var9], 1 + (var6 - var5 >> 1), var12 + 3 + ai.k_I * var9, 20);
      }
    }
  }

  public final void a_V(String var1) {
    if (var1 == null) {
      var1 = "Không có dữ liệu";
    }

    int var3 = (ai.f_I - super.j_I) / ai.k_I >> 1;
    int var2 = vh.stringWidth(ai.a_Font, var1);
    int var4 = 8;
    if (var2 > ai.e_I) {
      int var5 = var2 / ai.e_I + 1;
      if ((var3 -= var5 / 2) < 0) {
        var3 = 0;
      }
    } else {
      var4 = ai.e_I - var2 - 10 >> 1;
    }

    if (var4 < 0) {
      var4 = 0;
    }

    Hashtable[] var6 = new Hashtable[var3 + 1];

    for (int var7 = 0; var7 < var3; var7++) {
      var6[var7] = new Hashtable();
      var6[var7].put("type", ag.a_AString[4]);
    }

    var6[var6.length - 1] = new Hashtable();
    var6[var6.length - 1].put("type", ag.a_AString[0]);
    var6[var6.length - 1].put("value", var1);
    var6[var6.length - 1].put("space", String.valueOf(var4));
    super.a_an.a(var6);
  }

  public final r[] a_Ar(String var1) {
    String[] var7;
    r[] var2 = new r[(var7 = ad.a(var1)).length];
    int var3 = 0;

    for (int var5 = 0; var5 < this.a_Vector.size(); var5++) {
      ag var4;
      if ((var4 = (ag)this.a_Vector.elementAt(var5)).a_B == 1) {
        r var8 = (r)var4;

        for (int var6 = 0; var6 < var7.length; var6++) {
          if (var8.c().equals(var7[var6])) {
            var2[var6] = var8;
            var3++;
            break;
          }
        }

        if (var3 == var2.length) {
          break;
        }
      }
    }

    return var2;
  }

  private void a(String var1, String var2, String var3) {
    al var4;
    (var4 = new al()).b_String = var1;
    if (var2.length() > 0) {
      var4.a(var2);
    }

    var4.a_String = var3;
    this.c_Vector.addElement(var4);
    if (a_I == -1) {
      a_I = 0;
    }
  }

  private void a(String var1, String var2, String var3, int var4) {
    al var5;
    (var5 = new al()).b_String = var1;
    if (var2.length() > 0) {
      var5.a(var2);
    }

    var5.a_String = var3;
    switch (var4) {
      case 1:
        this.b_al = var5;
        return;
      case 2:
        this.c_al = var5;
    }
  }

  private void a(ag var1) {
    if (var1.i_I + ag.k_I < ai.f_I - super.j_I) {
      this.z_I = var1.e_I;
    }

    this.a_Vector.addElement(var1);
  }

  public final void a(Hashtable var1) {
    String var2 = an.a(var1, "href");
    an.a(var1, "name");
    String var3 = an.a(var1, "value");
    int var4;
    switch (var4 = an.a(var1, "lv", 0)) {
      case 0:
        q var6 = new q(this.x_I, this.a_Vector.size(), this.c_Vector.size());
        int var5 = vh.stringWidth(ai.a_Font, var3);
        var6.a(var3);
        if (this.c_I + var5 > this.b_I) {
          this.a_V();
        }

        var6.a(this.c_I, this.u_I, var5, ai.k_I);
        this.c_I += var5;
        if (var6.g_I > this.v_I) {
          this.v_I = var6.g_I;
        }

        this.a(var6);
        this.a(var2, an.a(var1, "cache"), an.a(var1, "addField"));
      default:
        return;
      case 1:
        ai.a_AString[10] = var3;
        this.h_Z = true;
        this.a(var2, an.a(var1, "cache"), an.a(var1, "addField"), var4);
        return;
      case 2:
        this.i_Z = true;
        ai.a_AString[11] = var3;
        this.a(var2, an.a(var1, "cache"), an.a(var1, "addField"), var4);
    }
  }

  public final void b(Hashtable var1) {
    String var2 = an.a(var1, "name");
    String var3 = an.a(var1, "prompt");
    int var4 = an.a(var1, "len", 20);
    int var5 = an.a(var1, "size", 0);
    int var6 = an.a(var1, "imp", 0);
    int var7 = 0;
    switch (var6) {
      case 1:
        var7 = 2;
        break;
      case 2:
        var7 = 65536;
        break;
      case 3:
        var7 = 5;
        break;
      case 4:
        var7 = 3;
        break;
      case 5:
        var7 = 1;
        break;
      case 6:
        var7 = 4;
        break;
      case 7:
        var7 = 131072;
    }

    int var8 = an.a(var1, "lv", 1);
    r var9 = new r(this.x_I, this.a_Vector.size(), this.c_Vector.size(), var7, var4, var8, super.a_u);
    if (this.c_I + var5 > this.b_I) {
      this.a_V();
    }

    var9.a(this.c_I, this.u_I, var5, ai.k_I);
    var9.a(var3, var2);
    this.c_I += var5;
    if (var9.g_I > this.v_I) {
      this.v_I = var9.g_I;
    }

    this.a(var9);
    this.a("", "", "");
    if (this.c_Vector.size() == 1) {
      this.l_I = this.b_I(a_I);
      this.B_I = this.l_I;
    }
  }

  public final void c(Hashtable var1) {
    String var2 = an.a(var1, "href");
    boolean var3 = false;
    if (var2.length() > 0) {
      var3 = true;
    }

    int var6 = an.a(var1, "id", -1);
    Image var4;
    if (ai.b_Vector.size() > 0 && var6 > 0 && var6 <= ai.b_Vector.size()) {
      var4 = (Image)ai.b_Vector.elementAt(var6 - 1);
    } else {
      var4 = a_Image;
      var6 = -1;
    }

    int var5 = var4.getWidth();
    int var8 = var4.getHeight();
    t var7 = new t(this.x_I, this.a_Vector.size(), var3, this.c_Vector.size());
    if (this.c_I + var5 > this.b_I) {
      this.a_V();
    }

    if (var8 > this.v_I) {
      this.v_I = var8;
    }

    var7.a(this.c_I, this.u_I, var5, var8);
    var7.a(var6, an.a(var1, "align", 1), an.a(var1, "valign", 2));
    this.c_I += var5;
    this.a(var7);
    if (var3) {
      this.a(var2, an.a(var1, "cache"), an.a(var1, "addField"));
    }
  }

  public final void d(Hashtable var1) {
    String var2 = an.a(var1, "value");
    String var3 = an.a(var1, "href");
    boolean var4 = false;
    if (var3.length() > 0) {
      var4 = true;
    }

    int var5 = this.b_I - this.c_I;
    int var6 = an.a(var1, "size", -1);
    int var7 = an.a(var1, "style", -1);
    Font var14;
    if (var6 < 0 && var7 < 0) {
      var14 = ai.a_Font;
    } else {
      byte var13;
      switch (var7) {
        case 1:
          var13 = 1;
          break;
        case 2:
          var13 = 2;
          break;
        case 3:
          var13 = 3;
          break;
        case 4:
          var13 = 4;
          break;
        case 5:
          var13 = 5;
          break;
        case 6:
          var13 = 6;
          break;
        case 7:
          var13 = 7;
          break;
        default:
          var13 = 0;
      }

      byte var16;
      switch (var6) {
        case 1:
          var16 = 16;
          break;
        case 2:
          var16 = 0;
          break;
        default:
          var16 = 8;
      }

      var14 = Font.getFont(0, var13, var16);
    }

    var7 = an.a(var1, "color", -16777216);
    int var8 = vh.stringWidth(var14, var2);
    int var9 = an.a(var1, "space", 0);
    if (var8 <= var5) {
      this.a(var2, var4, var7, var14.getStyle(), var14.getSize(), var9);
    } else {
      if (this.c_I != 5) {
        String var11 = ad.a(var2, var5, var14);
        this.a(var11, var4, var7, var14.getStyle(), var14.getSize(), var9);
        var2 = var2.substring(var11.length(), var2.length());
      }

      String[] var12 = ad.a(var2, var14, this.b_I - 5);

      for (int var10 = 0; var10 < var12.length; var10++) {
        this.a_V();
        this.a(var12[var10], var4, var7, var14.getStyle(), var14.getSize(), var9);
      }
    }

    if (var4) {
      this.a(var3, an.a(var1, "cache"), an.a(var1, "addField"));
    }
  }

  private void a(String var1, boolean var2, int var3, int var4, int var5, int var6) {
    am var7;
    (var7 = new am(var1, this.x_I, this.a_Vector.size(), var2, this.c_Vector.size())).b(this.c_I + var6, this.u_I);
    var7.a(var3, var4, var5);
    this.c_I = this.c_I + var7.f_I;
    if (this.v_I < var7.g_I) {
      this.v_I = var7.g_I;
    }

    this.a(var7);
  }

  public final void a_V() {
    this.c_I = 5;
    a_aq.a_V(this.u_I);
    this.u_I = this.u_I + this.v_I + 1;
    this.v_I = ai.k_I - 2;
    this.x_I++;
  }

  public final void b_V() {
    this.u_I = this.u_I + this.v_I + 1;
    if (this.u_I > this.w_I) {
      this.f_Z = true;
      this.s_I = this.w_I * 10000 / this.u_I * (this.o_I - 2) / 10000;
      if (this.s_I < ai.k_I) {
        this.s_I = ai.k_I;
      }
    }
  }

  public final void e(Hashtable var1) {
    String var2 = an.a(var1, "value");
    String var3 = an.a(var1, "href");
    int var4 = an.a(var1, "space", 0);
    int var5 = an.a(var1, "height", 0);
    int var6 = an.a(var1, "color", -16777216);
    boolean var7 = false;
    if (var3.length() > 0) {
      var7 = true;
    }

    if (var2.length() <= 0) {
      var2 = null;
    }

    ah var8;
    (var8 = new ah(this.x_I, this.a_Vector.size(), var7, var7 ? this.c_Vector.size() : -1)).a(var2, var4, var5, var6);
    var8.i_I = this.u_I;
    this.c_I = this.b_I - var4;
    if (var5 > this.v_I) {
      this.v_I = var5;
    }

    this.a(var8);
    if (var7) {
      this.a(var3, an.a(var1, "cache"), an.a(var1, "addField"));
    }
  }

  public final void a(Hashtable[] var1) {
    if (super.d_I == 1) {
      if (var1 != null && var1.length > 0 && !this.a_al.a_Z) {
        aa var3 = new aa(var1);
        int var2 = this.d_Vector.size();
        if (this.A_I != var2 - 1) {
          var2--;

          while (var2 > this.A_I) {
            this.d_Vector.removeElementAt(var2);
            var2--;
          }
        }

        if (this.d_Vector.size() < 5) {
          this.A_I++;
          this.d_Vector.addElement(var3);
        } else {
          this.d_Vector.removeElementAt(0);
          this.d_Vector.addElement(var3);
        }
      }

      this.a((byte)(this.h_Z ? 10 : 5), (byte)(this.i_Z ? 11 : (this.a_Z() ? 9 : 4)));
    }
  }

  private boolean a_Z() {
    return this.A_I > 0;
  }

  public final void c() {
    this.c_I = 5;
    this.u_I = 0;
    this.t_I = 0;
    ag.k_I = ag.j_I - this.t_I;
    this.x_I = 0;
    this.B_I = -1;
    this.l_I = -1;
    a_I = -1;
    this.y_I = 0;
    this.z_I = 0;
    this.f_Z = false;
    this.h_Z = false;
    this.i_Z = false;
    this.v_I = 0;
    this.a_Vector.removeAllElements();
    this.c_Vector.removeAllElements();
    a_aq.a_V();
  }

  public final void a_V(int var1) {
    super.d_I = var1;
    super.c_Z = true;
    switch (var1) {
      case 0:
        this.a_V("Game thuê bao tháng khi chạy phải đăng nhập‚ hãy đăng nhập trước!");
        this.a((byte)6, (byte)4);
        return;
      case 1:
        this.a((byte)5, (byte)4);
        this.A_I = -1;
        this.g_Z = false;
        this.f_Z = false;
        this.B_I = -1;
        this.l_I = -1;
    }
  }

  public final void b_V(int var1) {
    this.a_V("Đang kết nối... Vui lòng đợi!");
    super.a_an.a(var1);
  }

  private int a_I(int var1) {
    int var3 = 0;
    if (var1 % 2 == 1) {
      for (int var4 = 0; var4 < this.a_Vector.size(); var4++) {
        ag var2;
        if ((var2 = (ag)this.a_Vector.elementAt(var4)).d_I == a_I) {
          var3 = var2.e_I;
          if (var1 == 5) {
            return var3;
          }
        }

        if (var2.d_I > a_I && var2.b_Z) {
          if (var1 == 1 && var2.e_I > var3) {
            return var2.e_I;
          }

          if (var1 == 3) {
            return var2.e_I;
          }
        }
      }
    } else {
      for (int var6 = this.a_Vector.size() - 1; var6 >= 0; var6--) {
        ag var5;
        if ((var5 = (ag)this.a_Vector.elementAt(var6)).d_I == a_I) {
          var3 = var5.e_I;
        }

        if (this.c_I(var3 + 1) == a_I) {
          var3++;
        }

        if (var5.d_I < a_I && var5.b_Z) {
          if (var1 == 0 && var5.e_I < var3) {
            return var5.e_I;
          }

          if (var1 == 2) {
            return var5.e_I;
          }
        }
      }
    }

    return -1;
  }

  private int b_I(int var1) {
    for (int var2 = 0; var2 < this.a_Vector.size(); var2++) {
      if (((ag)this.a_Vector.elementAt(var2)).d_I == var1) {
        return var2;
      }
    }

    return -1;
  }

  private int a(int var1, int var2) {
    while (var1 >= 0 && var1 < a_aq.a_I()) {
      if (var2 == 0) {
        return a_aq.a_I(var1);
      }

      int var10000 = var1 + 1;
      var2 = 0;
      var1 = var10000;
    }

    return 0;
  }

  private int c_I(int var1) {
    int var2 = -1;

    for (int var3 = 0; var3 < this.a_Vector.size(); var3++) {
      ag var4;
      if ((var4 = (ag)this.a_Vector.elementAt(var3)).b_Z && var4.e_I == var1) {
        var2 = var4.d_I;
        break;
      }
    }

    return var2;
  }

  private void c_V(int var1) {
    if (this.b_Z()) {
      ((r)this.a_Vector.elementAt(this.B_I)).a_Z = false;
    }

    int var2 = this.a_I(var1);
    int var3 = this.a_I(5);
    if (var2 != -1) {
      if (var1 == 0) {
        a_I = this.c_I(var2);
        this.l_I = this.b_I(a_I);
      } else if (var1 == 2 && var2 >= this.y_I && a_I > 0) {
        a_I--;
        this.l_I = this.b_I(a_I);
      }
    }

    int var4 = this.y_I + (this.z_I - this.y_I) / 2 + 1;
    if (this.y_I > 0) {
      boolean var5 = false;
      if (var2 == -1) {
        var5 = true;
      } else if (var1 == 0 && var3 <= var4) {
        var5 = true;
      } else if (var3 > var2 & var3 <= var4) {
        var5 = true;
      } else if (var1 == 3 && this.a(var2, 1) < this.t_I) {
        var5 = true;
      }

      if (var5) {
        this.y_I--;
        this.z_I--;
        this.h();
      }
    }

    this.i();
  }

  private void d(int var1) {
    if (this.b_Z()) {
      ((r)this.a_Vector.elementAt(this.B_I)).a_Z = false;
    }

    int var2 = this.a_I(var1);
    int var3 = this.a_I(5);
    if (var2 != -1) {
      if (var1 == 1 && var2 <= this.z_I) {
        a_I = this.c_I(var2);
        this.l_I = this.b_I(a_I);
      } else if (var1 == 3 && var2 <= this.z_I && a_I < this.c_Vector.size()) {
        a_I++;
        this.l_I = this.b_I(a_I);
      }
    }

    int var4 = this.y_I + (this.z_I - this.y_I) / 2 + 1;
    if (this.u_I - this.t_I >= this.w_I) {
      boolean var5 = false;
      if (var2 == -1) {
        var5 = true;
      } else if (var1 == 1 && var3 >= var4) {
        var5 = true;
      } else if (var1 == 3 && var3 < var2 && var3 >= var4) {
        var5 = true;
      } else if (this.a(var2, 0) > this.t_I + this.w_I) {
        var5 = true;
      }

      if (var5) {
        this.y_I++;
        if (this.z_I < this.x_I) {
          this.z_I++;
        }

        this.h();
      }
    }

    this.i();
  }

  private void h() {
    this.t_I = this.a(this.y_I, 0);
    ag.k_I = ag.j_I - this.t_I;
  }

  private void i() {
    if (this.l_I != -1) {
      ag var1;
      if ((var1 = (ag)this.a_Vector.elementAt(this.l_I)).a_B == 1) {
        this.B_I = this.l_I;
        if (this.g_Z) {
          ((r)var1).a_Z = true;
          return;
        }
      } else {
        this.g_Z = false;
      }
    }
  }

  private boolean b_Z() {
    return this.B_I >= 0 && this.B_I == this.l_I;
  }

  protected final void d() {
    switch (super.d_I) {
      case 0:
        switch (super.g_I) {
          case -507:
            if (super.b_B == 4) {
              this.a("Bạn chắc chắn muốn thoát?", 1);
            }
            break;
          case -506:
            if (super.a_B == 6) {
              this.b_V(super.a_u.h_I);
              this.a_V(1);
              return;
            }
        }

        return;
      case 1:
        if (super.a_Z) {
          switch (super.g_I) {
            case -507:
              super.a_Z = false;
              this.a(super.c_B, super.d_B);
              return;
            case -506:
            case -505:
              super.a_Z = false;
              switch (this.m_I) {
                case 0:
                  this.a(super.c_B, super.d_B);
                  super.a_an.a(10);
                  return;
                case 1:
                  this.a(super.c_B, super.d_B);
                  super.a_an.a(this.a_al, null);
                  return;
                case 2:
                  if (this.A_I < this.d_Vector.size() - 1) {
                    this.A_I++;
                    aa var7 = (aa)this.d_Vector.elementAt(this.A_I);
                    super.a_an.a(var7.a_AHashtable);
                    this.a((byte)(this.h_Z ? 10 : 5), (byte)(this.i_Z ? 11 : (this.a_Z() ? 9 : 4)));
                    return;
                  }
                  break;
                case 3:
                  this.a(super.c_B, super.d_B);
                  super.a_an.a(100);
                  return;
                case 4:
                  this.a(super.c_B, super.d_B);
                  if (this.j_Z) {
                    this.a("Bạn chắc chắn muốn đăng xuất số hiện tại?", 1);
                    super.b_Z = true;
                    return;
                  }

                  super.a_an.a(101);
                  return;
                case 5:
                  this.e();
                  super.a_u.a_Display.setCurrent(super.a_u.a_Displayable);
                default:
                  return;
              }
            case -504:
            case -503:
            default:
              break;
            case -502:
              this.m_I = (this.m_I + 1) % this.b_AString.length;
              break;
            case -501:
              this.m_I = (this.m_I + this.b_AString.length - 1) % this.b_AString.length;
              return;
          }

          return;
        } else {
          switch (super.g_I) {
            case -507:
              if (super.a_an.a_I == 1) {
                if (!super.a_an.a_Z) {
                  super.a_an.c();
                  super.a_an.a_I = 0;
                  return;
                }
              } else {
                switch (super.b_B) {
                  case 4:
                    this.a("Bạn chắc chắn muốn thoát?", 1);
                    return;
                  case 9:
                    if (this.A_I > 0 && this.A_I < this.d_Vector.size()) {
                      this.A_I--;
                      aa var6 = (aa)this.d_Vector.elementAt(this.A_I);
                      super.a_an.a(var6.a_AHashtable);
                      this.a((byte)(this.h_Z ? 10 : 5), (byte)(this.i_Z ? 11 : (this.a_Z() ? 9 : 4)));
                      return;
                    }
                    break;
                  case 11:
                    super.a_an.a(this.c_al, this.a_Ar(this.c_al.a_String));
                    this.a(super.c_B, super.d_B);
                }
              }

              return;
            case -506:
              if (super.a_an.a_I != 1) {
                switch (super.a_B) {
                  case 5:
                    super.a_Z = true;
                    if (super.a_u.a_Hashtable != null && super.a_u.a_Z()) {
                      this.j_Z = true;
                      this.b_AString[4] = "5.Đăng xuất";
                    } else {
                      this.j_Z = false;
                      this.b_AString[4] = "5.Đăng nhập QQ";
                    }

                    this.a((byte)6, (byte)9);
                    return;
                  case 10:
                    super.a_an.a(this.b_al, this.a_Ar(this.b_al.a_String));
                    this.a(super.c_B, super.d_B);
                }
              }

              return;
            default:
              if (this.b_Z()) {
                r var2;
                (var2 = (r)this.a_Vector.elementAt(this.B_I)).a(super.g_I, super.h_I);
                switch (super.g_I) {
                  case -505:
                    if (var2.c_I == 0) {
                      var2.a_Z = !var2.a_Z;
                      this.g_Z = var2.a_Z;
                      return;
                    } else {
                      if (super.a_TextBox == null) {
                        super.a_TextBox = new TextBox("Hãy nhập", "", 16, 0);
                        super.a_Command = new Command("OK", 4, 1);
                        super.b_Command = new Command("Trở về", 2, 1);
                      }

                      var2 = (r)this.a_Vector.elementAt(this.B_I);
                      super.a_TextBox.setMaxSize(var2.a_I);
                      super.a_TextBox.setConstraints(var2.b_I);
                      super.a_TextBox.setTitle(var2.b());
                      super.a_TextBox.setString("");
                      super.a_TextBox.addCommand(super.a_Command);
                      super.a_TextBox.addCommand(super.b_Command);
                      super.a_TextBox.setCommandListener(this);
                      super.a_u.a_Display.setCurrent(super.a_TextBox);
                    }
                  default:
                    return;
                  case -504:
                    if (!var2.a_Z() && !this.g_Z) {
                      this.d(3);
                      return;
                    }
                    break;
                  case -503:
                    if (!var2.a_Z() && !this.g_Z) {
                      this.c_V(2);
                      return;
                    }
                    break;
                  case -502:
                    if (!var2.a_Z()) {
                      this.d(1);
                      return;
                    }
                    break;
                  case -501:
                    if (!var2.a_Z()) {
                      this.c_V(0);
                      return;
                    }
                }
              } else {
                switch (super.g_I) {
                  case -505:
                    if (this.c_Vector.size() > 0) {
                      al var5 = (al)this.c_Vector.elementAt(a_I);
                      r[] var3 = this.a_Ar(var5.a_String);
                      super.a_an.a(var5, var3);
                    }
                    break;
                  case -504:
                    this.d(3);
                    return;
                  case -503:
                    this.c_V(2);
                    return;
                  case -502:
                    this.d(1);
                    return;
                  case -501:
                    this.c_V(0);
                    return;
                }
              }
          }
        }
    }
  }

  public final void commandAction(Command var1, Displayable var2) {
    if (var1 == super.a_Command) {
      ((r)this.a_Vector.elementAt(this.B_I)).a(super.a_TextBox.getString());
    }

    super.a_u.a_Display.setCurrent(this);
  }

  public final void e() {
    super.g();
    u.a(ak.a(super.a_u.a_Hashtable));
    a_Image = null;
    this.a_Vector.removeAllElements();
    this.c_Vector.removeAllElements();
    a_aq.a_V();
    this.a_Vector = null;
    this.c_Vector = null;
    a_aq = null;
    this.d_Vector.removeAllElements();
    this.d_Vector = null;
    this.a_al = null;
    this.b_al = null;
    this.c_al = null;
    super.a_an.b();
  }

  public final void f() {
    if (!super.e_Z) {
      super.a(true);
      this.B_I = -1;
      this.l_I = -1;
      this.a_Vector = new Vector();
      this.c_Vector = new Vector();
      this.d_Vector = new Vector();
      ai.b_Vector = new Vector();
      a_aq = new aq();
      this.n_I = 5;
      this.p_I = ai.e_I - this.n_I - 1;
      this.q_I = super.i_I + 1;
      this.o_I = ai.f_I - super.i_I - super.j_I - 2;
      this.r_I = this.n_I - 2;
      this.s_I = 0;
      this.b_I = ai.e_I - this.n_I - 2;
      this.w_I = ai.f_I - super.i_I - super.j_I;
      ag.j_I = super.i_I + 2;

      try {
        a_Image = Image.createImage("/com/tencent/mbox/error.png");
      } catch (Exception var1) {
        var1.printStackTrace();
      }

      super.e_Z = true;
      this.j_Z = false;
    }
  }

  public final void b(String var1) {
    this.a_V(var1);
  }
}
