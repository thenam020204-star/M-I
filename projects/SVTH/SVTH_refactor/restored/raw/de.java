import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class de extends bg {
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private Image d_Image;
  private Image e_Image;
  boolean a_Z = false;
  boolean b_Z = false;
  private Image f_Image;
  private int l_I = 0;
  private String[] a_AString = new String[]{"Trạng thái", "Sủng vật", "Kỹ năng", "Trang bị", "Túi đồ", "Nhiệm vụ", "Đồ giám", "Hệ thống"};

  public de() {
    this.e_Image = cc.a("/pic/D04.gf");
    this.a_Vector = new Vector();
    this.a_Image = cc.a("/pic/D03.gf");
    String var10001 = "/pic/D46.gf";
    this.f_Image = null;
    this.b_Image = cc.a("/pic/xttb.gf");
    this.c_Image = cc.a("/pic/D12.gf");
    this.d_Image = cc.a("/pic/tbbj.gf");
  }

  public final void a(boolean var1) {
    this.a_Z = true;
  }

  public final void a(int var1, int var2, int var3, int var4, int var5) {
    this.a_I = var1;
    this.b_I = var2;
    this.c_I = 193;
    this.d_I = 28;
    this.j_I = 28;
    this.e_I = 27;
    this.i_I = 5;
  }

  public final void a(int var1, int var2) {
    this.a_Vector.addElement(new dd(var1, var2));
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(int var1) {
    if (var1 >= this.a_Vector.size()) {
      throw new IndexOutOfBoundsException();
    } else {
      this.g_I = var1;
    }
  }

  public final void e() {
    if (this.g_I < this.a_Vector.size() - 1) {
      this.g_I++;
      if (this.g_I >= this.h_I + this.i_I) {
        this.h_I++;
        return;
      }
    } else {
      this.h_I = 0;
      this.g_I = 0;
    }
  }

  public final void f() {
    if (this.g_I > 0) {
      this.g_I--;
      if (this.g_I < this.h_I) {
        this.h_I = this.g_I;
        return;
      }
    } else {
      this.g_I = this.a_Vector.size() - 1;
      this.h_I = this.a_Vector.size() - this.i_I;
    }
  }

  public final void a(Graphics var1) {
    if (da.a_da.a_S % 12 == 0) {
      this.l_I = 3;
    } else {
      this.l_I = 0;
    }

    int var4;
    if ((var4 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var4 = this.a_Vector.size();
    }

    int var3 = this.a_I;
    if (this.a_Image != null) {
      var1.drawImage(this.a_Image, var3, this.b_I, 20);
    } else {
      var1.setColor(5924969);
      var1.fillRect(this.a_I + 1, this.b_I + 1, this.c_I - 2, 15);
      var1.setColor(1911597);
      var1.fillRect(this.a_I + 1, this.b_I + 15, this.c_I - 2, 15);
      var1.setColor(3027509);
      var1.drawRoundRect(this.a_I, this.b_I + 1, this.c_I, 29, 4, 4);
      var1.setColor(12371653);
      var1.drawRoundRect(this.a_I + 1, this.b_I + 2, this.c_I - 2, 28, 4, 4);
    }

    if (!this.a_Z) {
      if (!this.b_Z) {
        cg.a(var1, this.e_Image, this.e_Image.getWidth() / 2, this.e_Image.getHeight(), 0, var3 + 5 - this.l_I, this.b_I + (this.d_I >> 1), 3, 0);
        cg.a(var1, this.e_Image, this.e_Image.getWidth() / 2, this.e_Image.getHeight(), 1, var3 - 5 + this.l_I + this.c_I, this.b_I + (this.d_I >> 1), 3, 0);
      }

      var3 += 54;

      for (int var5 = this.h_I; var5 < var4; var5++) {
        dd var2 = (dd)this.a_Vector.elementAt(var5);
        if (var5 == this.g_I) {
          if (this.c_Image != null) {
            cg.a(var1, this.c_Image, this.c_Image.getWidth(), this.c_Image.getHeight() / 8, var2.a_I, this.a_I + 5 + (this.f_I << 1), this.b_I + 5, 20, 0);
          } else {
            cg.a(var1, this.a_AString[var2.a_I], this.a_I + 15 + (this.f_I << 1), this.b_I + 5, 16308570, 203551, 20);
          }

          cg.a(var1, this.b_Image, this.b_Image.getWidth() / 8, this.b_Image.getHeight(), var2.b_I, var3, this.b_I - 4, 20, 0);
          var1.drawImage(this.d_Image, var3, this.b_I + 29, 36);
        } else {
          cg.a(var1, this.b_Image, this.b_Image.getWidth() / 8, this.b_Image.getHeight(), var2.b_I, var3, this.b_I, 20, 0);
        }

        var3 += this.e_I;
      }
    } else if (this.f_Image != null) {
      var1.drawImage(this.f_Image, this.a_I + this.c_I / 2, this.b_I + 15, 3);
    } else {
      cg.a(var1, "Chọn", this.a_I + this.c_I / 2, this.b_I + 15 - cg.b_S / 2, 16308570, 203551, 17);
    }
  }

  public final void a_V() {
  }
}
