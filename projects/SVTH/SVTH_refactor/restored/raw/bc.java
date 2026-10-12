import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bc extends bg {
  private int l_I = 1;
  private int m_I;
  private int n_I = 0;
  private Image a_Image;
  private Image b_Image;

  public bc() {
    this.a_Vector = new Vector();
  }

  public final void a(int var1, int var2, int var3, int var4) {
    this.a_I = var1;
    this.b_I = var2;
    this.c_I = var3;
    this.d_I = var4;
    this.i_I = var4 / this.j_I;
    this.e_I = 0;
    this.e_I = this.e_I + (var3 - 2 * this.f_I);
    this.g_I = 0;
  }

  public final void b() {
    if (this.a_Vector.size() > 0) {
      if (this.a_Vector.size() % this.i_I == 0) {
        this.m_I = this.a_Vector.size() / this.i_I;
        return;
      }

      this.m_I = this.a_Vector.size() / this.i_I + 1;
    }
  }

  private int d_I() {
    return this.g_I + (this.l_I - 1) * this.i_I;
  }

  public final int a_I() {
    return ((bu)this.a_Vector.elementAt(this.d_I())).a_I;
  }

  public final String a_String() {
    return ((bu)this.a_Vector.elementAt(this.d_I())).a_String;
  }

  public final void c() {
    if (this.l_I < this.m_I) {
      this.l_I++;
      this.g_I = 0;
    }
  }

  public final void d_V() {
    if (this.l_I > 1) {
      this.l_I--;
      this.g_I = 0;
    }
  }

  public final void e() {
    if (this.g_I < this.i_I - 1 && this.g_I + (this.l_I - 1) * this.i_I < this.a_Vector.size() - 1) {
      this.g_I++;
    }
  }

  public final void f() {
    if (this.g_I > 0) {
      this.g_I--;
    }
  }

  public final void a(int var1, String var2) {
    this.a_Vector.addElement(new bu(var1, var2));
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(Image var1, Image var2) {
    this.a_Image = var1;
    this.b_Image = var2;
  }

  public final void a(Graphics var1) {
    if (da.a_da.a_S % 12 == 0) {
      this.n_I = 3;
    } else {
      this.n_I = 0;
    }

    int var3 = this.b_I + this.k_I;
    int var4 = this.a_I;
    int var2;
    if ((var2 = this.l_I * this.i_I) > this.a_Vector.size()) {
      this.a_Vector.size();
    }

    if (this.a_Image != null) {
      var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var4 + 5 + this.n_I, var3 + this.g_I * this.j_I, 17);
    }

    for (int var5 = 0; var5 < this.i_I; var5++) {
      if ((this.l_I - 1) * this.i_I + var5 < this.a_Vector.size()) {
        bu var8 = (bu)this.a_Vector.elementAt((this.l_I - 1) * this.i_I + var5);
        String var6 = "#  ";
        String var7;
        if ((var7 = String.valueOf(var8.a_I)).length() == 1) {
          var7 = "00" + var7;
        } else if (var7.length() == 2) {
          var7 = "0" + var7;
        }

        if (var5 == this.g_I) {
          cg.a(var1, var6 + var7, var4 + cg.a_S, var3, 16711680, 16777215, 20);
          cg.a(var1, var8.a_String, var4 + this.c_I * 3 / 4, var3, 16711680, 16777215, 17);
        } else {
          cg.a(var1, var6 + var7, var4 + cg.a_S, var3, 15987162, 5971280, 20);
          cg.a(var1, var8.a_String, var4 + this.c_I * 3 / 4, var3, 15987162, 5971280, 17);
        }

        var3 += this.j_I;
      }
    }

    var1.drawRegion(this.b_Image, 60, 0, 6, 7, 0, var4 + this.c_I / 2, this.b_I + this.i_I * this.j_I, 33);
    cg.a(var1, this.l_I, var4 + this.c_I / 2 - 6, this.b_I + this.i_I * this.j_I, this.b_Image, 6, 7, 40);
    cg.a(var1, this.m_I, var4 + this.c_I / 2 + 6, this.b_I + this.i_I * this.j_I, this.b_Image, 6, 7, 36);
  }

  public final void a_V() {
    this.a_Vector.removeAllElements();
  }
}
