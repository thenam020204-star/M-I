import javax.microedition.lcdui.Graphics;

public final class r extends ag {
  private w a_w;
  public int a_I;
  public int b_I = 0;
  public int c_I;
  private int z_I;
  private String a_String;
  private String b_String;
  public boolean a_Z;
  private String c_String;
  private String d_String;
  private static int A_I;

  public r(int var1, int var2, int var3, int var4, int var5, int var6, u var7) {
    super((byte)1, var1, true, var3);
    this.c_I = var6;
    this.a_I = var5;
    this.a_Z = false;
    this.c_String = "";
    this.d_String = "";
    A_I = vh.charWidth(ai.a_Font, 'a');
    this.z_I = 0;
    this.b_I = var4;
    if (var6 == 0) {
      this.a_w = new w(var4, var5, var7);
    }
  }

  public final void a(String var1, String var2) {
    this.a_String = var1;
    this.b_String = var2;
  }

  public final void a(Graphics var1) {
    super.l_I = super.h_I;
    super.m_I = super.i_I + ag.k_I;
    if (super.d_I == ac.a_I) {
      if (this.a_Z) {
        var1.setColor(16777215);
        var1.fillRect(super.l_I, super.m_I, super.f_I, super.g_I);
        var1.setColor(ag.r_I);
        var1.drawRect(super.l_I, super.m_I, super.f_I - 1, super.g_I - 1);
        var1.setColor(167930);
        var1.drawRect(super.l_I + 1, super.m_I + 1, super.f_I - 3, super.g_I - 3);
        var1.setColor(ag.v_I);
      } else {
        var1.setColor(ag.t_I);
        var1.fillRect(super.l_I, super.m_I, super.f_I, super.g_I);
        var1.setColor(ag.r_I);
        var1.drawRect(super.l_I, super.m_I, super.f_I - 1, super.g_I - 1);
        var1.setColor(167930);
        var1.drawRect(super.l_I + 1, super.m_I + 1, super.f_I - 3, super.g_I - 3);
        var1.setColor(ag.v_I);
      }
    } else {
      var1.setColor(ag.s_I);
      var1.fillRect(super.l_I, super.m_I, super.f_I, super.g_I);
      var1.setColor(ag.q_I);
      var1.drawRect(super.l_I, super.m_I, super.f_I - 1, super.g_I - 1);
      var1.setColor(ag.u_I);
    }

    if (this.c_String.length() > 0) {
      if (this.b_I != 65536 && vh.stringWidth(ai.a_Font, this.c_String) >= super.f_I - 6 - A_I) {
        var1.setClip(super.l_I + 2, super.m_I, super.f_I - 4, super.g_I);
        vh.drawString(var1, this.c_String, super.l_I + super.f_I - 6 - A_I, super.m_I + 3, 24);
        var1.setClip(0, 0, ai.e_I, ai.f_I);
        return;
      }

      vh.drawString(var1, this.c_String, super.l_I + 3, super.m_I + 3, 20);
    }
  }

  public final void b(Graphics var1) {
    this.a_w.a_V();
    if (vh.stringWidth(ai.a_Font, this.c_String) < super.f_I - 6 - A_I) {
      this.a_w.a(var1, super.l_I + this.z_I + 3, super.m_I + 3, super.f_I);
    } else {
      this.a_w.a(var1, super.l_I + super.f_I - 6 - A_I, super.m_I + 3, super.f_I);
    }

    this.a_w.a(var1);
    if (this.a_w.a_I() == 1) {
      this.a(this.a_w.a_String());
    }
  }

  public final void a(int var1, int var2) {
    if (this.c_I == 0 && this.a_Z) {
      this.a_w.a_Z(var1, var2);
      this.a(this.a_w.a_String());
    }
  }

  public final void a(String var1) {
    this.d_String = var1;
    if (this.b_I != 65536) {
      this.c_String = var1;
      this.z_I = vh.stringWidth(ai.a_Font, this.c_String);
    } else {
      if (this.z_I < super.f_I - 6) {
        this.c_String = ad.a(var1.length());
        this.z_I = vh.stringWidth(ai.a_Font, this.c_String);
      }
    }
  }

  public final String a_String() {
    return this.b_I == 65536 ? n.a_String(this.d_String.getBytes()) : this.d_String;
  }

  public final boolean a_Z() {
    return this.c_I == 0 ? this.a_w.a_Z() : false;
  }

  public final String b() {
    return this.a_String;
  }

  public final String c() {
    return this.b_String;
  }
}
