import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class db extends bg {
  Image a_Image;
  private Image b_Image;
  private int l_I;

  public db() {
    new Vector();
    this.a_Vector = null;
    this.a_Vector = new Vector();
    if (this.b_Image == null) {
      this.b_Image = cc.a_Image("/pic/number01.gf", 2);
    }
  }

  public final void a(Image var1, String var2, int var3, int var4) {
    this.a_Vector.addElement(new bm(var1, var2, var3, var4));
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(Graphics var1) {
    if (da.a_da.a_S % 12 == 0) {
      this.l_I = 3;
    } else {
      this.l_I = 0;
    }

    int var3 = this.b_I + this.k_I;
    int var4;
    if ((var4 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var4 = this.a_Vector.size();
    }

    if (this.a_Vector.size() > this.i_I) {
      k.a(var1, null, null, this.a_I + this.e_I - 5, this.b_I, this.d_I - 10 - (this.k_I << 2), this.a_Vector.size(), this.i_I, this.h_I);
    }

    for (int var5 = this.h_I; var5 < var4; var5++) {
      int var6 = this.a_I + this.f_I;
      bm var2 = (bm)this.a_Vector.elementAt(var5);
      if (this.b_Image != null) {
        int var11 = var6 + 15 + this.c_I * 3 / 4;
        int var10 = var2.b_I;
        int var9 = var2.a_I;
        var1.drawRegion(this.b_Image, 60, 0, 6, 7, 0, var11, var3, 17);
        cg.a(var1, var9, var11 - 3, var3, this.b_Image, 6, 7, 24);
        cg.a(var1, var10, var11 + 3, var3, this.b_Image, 6, 7, 20);
      }

      Image var7 = var2.a_Image;
      if (var2.a_Image != null) {
        var1.drawImage(var7, var6 + 5 + (var7.getWidth() >> 1), var3, 3);
        if (var5 == this.g_I) {
          var1.setColor(12842492);
          var1.drawRect(var6 + 5 - 1, var3 - (var7.getHeight() + 2) / 2, var7.getWidth() + 2, var7.getHeight() + 2);
          var1.setColor(541060);
          var1.drawRect(var6 + 5 - 2, var3 - 1 - (var7.getHeight() + 2) / 2, var7.getWidth() + 4, var7.getHeight() + 4);
        } else {
          var1.setColor(15589360);
          var1.drawRect(var6 + 5 - 1, var3 - (var7.getHeight() + 2) / 2, var7.getWidth() + 2, var7.getHeight() + 2);
          var1.setColor(6898011);
          var1.drawRect(var6 + 5 - 2, var3 - 1 - (var7.getHeight() + 2) / 2, var7.getWidth() + 4, var7.getHeight() + 4);
        }
      }

      if (var5 == this.g_I) {
        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_I + this.l_I, var3 + 4, 3);
        }

        cg.a(var1, var2.a_String, var6 + (this.c_I >> 1), var3 - 8, 16711680, 16777215, 17);
      } else {
        cg.a(var1, var2.a_String, var6 + (this.c_I >> 1), var3 - 8, 15987162, 5971280, 17);
      }

      var3 += this.j_I;
    }
  }

  public final void a_V() {
  }
}
