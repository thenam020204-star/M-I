import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cy extends bg {
  Image a_Image;
  private int l_I;

  public cy() {
    new Vector();
    this.a_Vector = null;
    this.a_Vector = new Vector();
  }

  public final String a_String() {
    return ((bd)this.a_Vector.elementAt(this.g_I)).a_String();
  }

  public final int a_I() {
    return ((bd)this.a_Vector.elementAt(this.g_I)).a_I();
  }

  public final void a(int var1, int var2, String var3) {
    this.a_Vector.addElement(new bd(var1, var2, var3));
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
      bd var2 = (bd)this.a_Vector.elementAt(var5);
      if (var5 == this.g_I) {
        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_I + 15 + this.l_I, var3 + (this.k_I << 2) + 4, 3);
        }

        var1.setColor(9240568);
      } else {
        var1.setColor(16777215);
      }

      if (var2.a_I() == 1) {
        var1.setColor(11184810);
      }

      vh.drawString(var1, var2.a_String(), this.a_I + this.c_I / 2, var3, 17);
      var3 += this.j_I;
    }
  }

  public final void a_V() {
    if (this.a_Vector != null) {
      this.a_Vector.removeAllElements();
      this.a_Vector = null;
    }
  }
}
