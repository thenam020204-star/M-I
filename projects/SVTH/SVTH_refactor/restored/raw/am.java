import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class am extends ag {
  private int a_I;
  private int b_I;
  private int c_I;
  private String a_String;

  public am(String var1, int var2, int var3, boolean var4, int var5) {
    super((byte)0, var2, var4, var5);
    this.a_String = var1;
  }

  public final void a(int var1, int var2, int var3) {
    this.c_I = var1;
    this.a_I = var2;
    this.b_I = var3;
    Font var4 = Font.getFont(0, this.a_I, this.b_I);
    super.f_I = vh.stringWidth(var4, this.a_String);
    super.g_I = vh.getHeight(var4);
  }

  private void b(Graphics var1) {
    if (this.c_I != -16777216) {
      var1.setColor(this.c_I);
    }

    if (this.b_I != 8 || this.a_I != 0) {
      var1.setFont(Font.getFont(0, this.a_I, this.b_I));
    }
  }

  public final void a(Graphics var1) {
    if (this.a_String != null && this.a_String.length() != 0) {
      super.l_I = super.h_I;
      super.m_I = super.i_I + ag.k_I;
      if (!super.b_Z) {
        var1.setColor(ag.n_I);
        this.b(var1);
        vh.drawString(var1, this.a_String, super.l_I, super.m_I, 20);
      } else if (super.d_I == ac.a_I) {
        var1.setColor(ag.o_I);
        var1.drawLine(super.l_I, super.m_I + super.g_I, super.l_I + super.f_I, super.m_I + super.g_I);
        var1.setColor(6177536);
        vh.drawString(var1, this.a_String, super.l_I - 1, super.m_I - 1, 20);
        vh.drawString(var1, this.a_String, super.l_I + 1, super.m_I + 1, 20);
        var1.setColor(ag.o_I);
        this.b(var1);
        vh.drawString(var1, this.a_String, super.l_I, super.m_I, 20);
      } else {
        var1.setColor(ag.p_I);
        var1.drawLine(super.l_I, super.m_I + super.g_I, super.l_I + super.f_I, super.m_I + super.g_I);
        var1.setColor(ag.p_I);
        this.b(var1);
        vh.drawString(var1, this.a_String, super.l_I, super.m_I, 20);
      }
    }
  }
}
