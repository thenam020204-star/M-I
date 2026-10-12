import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class l extends bg {
  private Image b_Image;
  private int l_I = 0;
  Image a_Image;

  public l() {
    this.a_Vector = new Vector();
    cc.a_Image("/pic/number01.gf", 2);
    String var10001 = "/pic/D19.gf";
    this.b_Image = null;
  }

  public final void a(int var1, int var2, boolean var3, String var4, int var5) {
    this.a_Vector.addElement(new bw(var1, var2, var3, var4, var5));
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final int a() {
    int var2 = this.g_I;
    if (var2 < this.a_Vector.size()) {
      return ((bw)this.a_Vector.elementAt(var2)).a_I();
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public final void a(Graphics var1) {
    int var3 = this.b_I + this.k_I + 2;
    int var4 = this.a_I + this.f_I;
    int var5;
    if ((var5 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var5 = this.a_Vector.size();
    }

    if (this.a_Vector.size() > this.i_I) {
      k.a(var1, null, null, this.a_I + this.e_I - 5, this.b_I + 5, this.d_I - 10, this.a_Vector.size(), this.i_I, this.h_I);
    }

    if (da.a_da.a_S % 12 == 0) {
      this.l_I = 3;
    } else {
      this.l_I = 0;
    }

    for (int var6 = this.h_I; var6 < var5; var6++) {
      bw var2 = (bw)this.a_Vector.elementAt(var6);
      if (var6 == this.g_I) {
        cg.b(var1, var2.a_String, var4 + 5, var3, 9502459, 540331, 20);
        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var4 + this.l_I, var3 + this.j_I / 2, 3);
        }
      } else {
        var1.setColor(15793919);
        vh.drawString(var1, var2.a_String, var4 + 5, var3, 20);
      }

      cg.b(var1, "" + var2.b(), var4 + (this.e_I - 40), var3, 15921628, 5971029, 20);
      int var7 = vh.stringWidth(cg.a_Font, "" + var2.b());
      cg.b(var1, " cấp", var4 + var7 + (this.e_I - 40), var3, 16706636, 9193495, 20);
      if (var2.a_Z()) {
        if (this.b_Image != null) {
          var1.drawImage(this.b_Image, var4 + this.e_I - 10, var3 - 2 + this.j_I / 2, 10);
        } else {
          cg.a(var1, "Xuất chiến", var4 + this.e_I - 10, var3 - 2 + this.j_I / 2 - cg.b_S / 2, 16308570, 203551, 24);
        }
      }

      var3 += this.j_I;
    }
  }

  public final void a_V() {
  }
}
