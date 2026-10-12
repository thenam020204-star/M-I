import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cp extends bg {
  private byte a_B = 0;
  private int l_I;
  private Image a_Image;
  boolean a_Z;

  public cp() {
    this.a_Vector = null;
    this.a_Vector = new Vector();
  }

  public cp(byte var1, int var2, Image var3) {
    this.a_Image = var3;
    this.l_I = 10;
    this.a_B = 1;
    this.a_Vector = null;
    this.a_Vector = new Vector();
  }

  public final void a(String var1) {
    this.a_Vector.addElement(var1);
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(Graphics var1) {
    int var2 = this.b_I + this.k_I;
    int var3 = this.a_I + this.f_I;
    int var4;
    if ((var4 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var4 = this.a_Vector.size();
    }

    for (int var5 = this.h_I; var5 < var4; var5++) {
      if (this.a_B == 0) {
        var1.setColor(16777215);
        if (this.a_Z) {
          vh.drawString(var1, "x  ", var3, var2 + 2, 24);
        }

        vh.drawString(var1, (String)this.a_Vector.elementAt(var5), var3, var2 + 2, 20);
      } else if (this.a_B == 1) {
        int var6 = Integer.parseInt((String)this.a_Vector.elementAt(var5));
        cg.a(var1, var6, var3, var2 - 2 + this.j_I / 2, this.a_Image, this.a_Image.getWidth() / this.l_I, this.a_Image.getHeight(), 3);
      }

      var2 += this.j_I;
    }
  }

  public final void a_V() {
  }
}
