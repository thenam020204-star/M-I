import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class t extends ag {
  private int a_I;
  private int b_I;
  private int c_I;

  public t(int var1, int var2, boolean var3, int var4) {
    super((byte)5, var1, var3, var4);
  }

  public final void a(int var1, int var2, int var3) {
    this.a_I = var1;
    this.b_I = var2;
    this.c_I = var3;
  }

  public final void a(Graphics var1) {
    Image var2;
    if (this.a_I > 0 && this.a_I <= ai.b_Vector.size()) {
      var2 = (Image)ai.b_Vector.elementAt(this.a_I - 1);
    } else {
      var2 = ac.a_Image;
    }

    super.l_I = super.h_I;
    super.m_I = super.i_I;
    switch (this.b_I) {
      case 2:
        super.l_I = ai.e_I - super.f_I >> 1;
        break;
      case 3:
        super.l_I = ai.e_I - super.f_I - 5;
    }

    int var3 = super.g_I;
    if (super.e_I < ac.a_aq.a_I() - 1) {
      var3 = ac.a_aq.a_I(super.e_I + 1) - ac.a_aq.a_I(super.e_I);
    }

    switch (this.c_I) {
      case 2:
        super.m_I = super.m_I + (var3 - super.g_I >> 1);
        break;
      case 3:
        super.m_I = super.m_I + (var3 - super.g_I);
    }

    super.l_I = super.l_I;
    super.m_I = super.m_I + ag.k_I;
    if (super.b_Z) {
      var1.setColor(ag.y_I);
      if (super.d_I == ac.a_I) {
        var1.fillRect(super.l_I - 1, super.m_I - 1, super.f_I + 2, super.g_I + 2);
      } else {
        var1.drawLine(super.l_I, super.m_I + super.g_I + 1, super.l_I + super.f_I, super.m_I + super.g_I + 1);
      }
    }

    if (var2 != null) {
      var1.drawImage(var2, super.l_I, super.m_I, 20);
    } else {
      var1.drawRect(super.l_I, super.m_I, super.f_I, super.g_I);
    }
  }
}
