import javax.microedition.lcdui.Graphics;

public final class ah extends ag {
  private String a_String;
  private int a_I;
  private int b_I;
  private int c_I;

  public ah(int var1, int var2, boolean var3, int var4) {
    super((byte)8, var1, var3, var4);
  }

  public final void a(String var1, int var2, int var3, int var4) {
    if (var1 != null) {
      this.a_String = "";
      this.b_I = ai.k_I;
      int var5 = vh.stringWidth(ai.a_Font, var1);
      var5 = (ai.e_I - (var2 << 1)) / var5;

      for (int var6 = 0; var6 < var5; var6++) {
        this.a_String = this.a_String + var1;
      }
    } else {
      this.b_I = 2;
    }

    super.h_I = var2;
    this.a_I = var3;
    this.c_I = var4;
  }

  public final void a(Graphics var1) {
    super.l_I = super.h_I;
    super.m_I = super.i_I + ag.k_I;
    super.m_I = super.m_I + (this.a_I - this.b_I >> 1);
    if (this.a_String != null) {
      if (this.c_I != -16777216) {
        var1.setColor(this.c_I);
      } else {
        var1.setColor(ag.n_I);
      }

      vh.drawString(var1, this.a_String, super.l_I, super.m_I, 20);
    } else {
      var1.setColor(0);
      var1.drawLine(super.l_I, super.m_I, ai.e_I - super.l_I, super.m_I);
      var1.setColor(3568808);
      var1.drawLine(super.l_I, super.m_I + 1, ai.e_I - super.l_I, super.m_I + 1);
    }
  }
}
