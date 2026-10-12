import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class g extends bg {
  private boolean a_Z = false;

  public g(boolean var1) {
    this.a_Z = true;
    this.a_Vector = null;
    this.a_Vector = new Vector();
  }

  public final void a(Image var1) {
    this.a_Vector.addElement(var1);
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(Graphics var1) {
    var1.setClip(this.a_I, this.b_I, this.e_I, this.d_I);
    int var2 = this.b_I + this.j_I / 2;
    int var3 = this.a_I + this.f_I;
    int var4;
    if ((var4 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var4 = this.a_Vector.size();
    }

    for (int var5 = this.h_I; var5 < var4; var5++) {
      Image var6 = (Image)this.a_Vector.elementAt(var5);
      var1.drawImage(var6, var3 + (var6.getWidth() >> 1), var2, 3);
      if (var5 == this.g_I && this.a_Z) {
        var1.setColor(12842492);
        var1.drawRect(var3 - 1, var2 - (var6.getHeight() + 2) / 2, var6.getWidth() + 2, var6.getHeight() + 2);
        var1.setColor(541060);
        var1.drawRect(var3 - 2, var2 - 1 - (var6.getHeight() + 2) / 2, var6.getWidth() + 4, var6.getHeight() + 4);
      } else {
        var1.setColor(15589360);
        var1.drawRect(var3 - 1, var2 - (var6.getHeight() + 2) / 2, var6.getWidth() + 2, var6.getHeight() + 2);
        var1.setColor(6898011);
        var1.drawRect(var3 - 2, var2 - 1 - (var6.getHeight() + 2) / 2, var6.getWidth() + 4, var6.getHeight() + 4);
      }

      var2 += this.j_I;
    }

    var1.setClip(0, 0, da.a_da.getWidth(), da.a_da.getHeight());
  }

  public final void a_V() {
    if (this.a_Vector != null) {
      this.a_Vector.removeAllElements();
    }
  }
}
