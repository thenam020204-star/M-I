import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bf extends bg {
  Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private int l_I;

  public bf() {
    new Vector();
    this.a_Vector = null;
    this.a_Vector = new Vector();
    if (this.b_Image == null) {
      this.b_Image = cc.a_Image("/pic/number01.gf", 2);
    }

    if (this.c_Image == null) {
      this.c_Image = cc.a("/pic/D15.gf");
    }
  }

  public final String a_String() {
    return ((cn)this.a_Vector.elementAt(this.g_I)).a_String;
  }

  public final int a_I() {
    return ((cn)this.a_Vector.elementAt(this.g_I)).a_I;
  }

  public final void a(int var1, int var2, String var3, boolean var4) {
    this.a_Vector.addElement(new cn(var1, var2, var3, var4));
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
      k.a(var1, null, null, this.a_I + this.e_I - 10, this.b_I + (this.k_I << 1), this.d_I - (this.k_I << 2), this.a_Vector.size(), this.i_I, this.h_I);
    }

    for (int var5 = this.h_I; var5 < var4; var5++) {
      int var6 = this.a_I + this.f_I;
      cn var2 = (cn)this.a_Vector.elementAt(var5);
      if (this.b_Image != null) {
        var6 += a(var1, var2.b_I, var6, var3);
      }

      if (var5 == this.g_I) {
        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_I + this.l_I, var3 + (this.k_I << 2) + 4, 3);
        }

        var1.setColor(9240568);
      } else {
        var1.setColor(16777215);
      }

      vh.drawString(var1, var2.a_String, var6, var3, 20);
      if (var2.a_Z) {
        cg.a(var1, this.c_Image, this.c_Image.getWidth() / 2, this.c_Image.getHeight(), 0, this.a_I + this.c_I - 18, var3, 17, 0);
      } else {
        cg.a(var1, this.c_Image, this.c_Image.getWidth() / 2, this.c_Image.getHeight(), 1, this.a_I + this.c_I - 18, var3, 17, 0);
      }

      var3 += this.j_I;
    }
  }

  public static int a(Graphics var0, int var1, int var2, int var3) {
    cg.b(var0, "[", var2, var3, 15987164, 7546142, 17);
    cg.b(var0, "]", var2 + (cg.a_S + 4 << 1), var3, 15987164, 7546142, 17);
    if (var1 == 3) {
      cg.b(var0, "Công hội", var2 + 4 + cg.a_S, var3, 9633535, 409514, 17);
    } else {
      cg.b(var0, "Thường", var2 + 4 + cg.a_S, var3, 5439068, 608022, 17);
    }

    return 20 + 2 * cg.a_S;
  }

  public final void a_V() {
    this.a_Vector.removeAllElements();
    this.a_Vector = null;
  }
}
