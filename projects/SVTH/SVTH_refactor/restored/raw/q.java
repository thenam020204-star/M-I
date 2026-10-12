import javax.microedition.lcdui.Graphics;

public final class q extends ag {
  private String a_String;

  public q(int var1, int var2, int var3) {
    super((byte)2, var1, true, var3);
  }

  public final void a(String var1) {
    this.a_String = var1;
  }

  public final void a(Graphics var1) {
    super.l_I = super.h_I;
    super.m_I = super.i_I + ag.k_I;
    if (super.d_I == ac.a_I) {
      var1.setColor(80737);
      var1.drawRoundRect(super.l_I, super.m_I, super.f_I, super.g_I, 3, 3);
      var1.setColor(ag.x_I);
    } else {
      var1.setColor(80737);
      var1.drawRoundRect(super.l_I, super.m_I, super.f_I, super.g_I, 3, 3);
      var1.setColor(ag.w_I);
    }

    if (this.a_String != null) {
      vh.drawString(var1, this.a_String, super.l_I + (super.f_I >> 1), super.m_I + 1, 17);
    }
  }
}
