import javax.microedition.lcdui.Graphics;

public abstract class ag {
  public static final String[] a_AString = new String[]{
    "a", "input", "button", "cache", "br", "img", "tab", "return", "partline", "blank", "redirect", "struct"
  };
  public boolean b_Z;
  public int d_I;
  public int e_I;
  public byte a_B;
  public int f_I;
  public int g_I;
  public int h_I;
  public int i_I;
  public static int j_I;
  public static int k_I;
  public int l_I;
  public int m_I;
  public static int n_I = 12574712;
  public static int o_I = 16631433;
  public static int p_I = 8829414;
  public static int q_I = 865624;
  public static int r_I = 16213002;
  public static int s_I = 11719149;
  public static int t_I = 7918079;
  public static int u_I = 1397123;
  public static int v_I = 1397123;
  public static int w_I = 9295103;
  public static int x_I = 16777215;
  public static int y_I = 255;

  public ag(byte var1, int var2, boolean var3, int var4) {
    this.a_B = var1;
    this.e_I = var2;
    this.b_Z = var3;
    if (this.b_Z) {
      this.d_I = var4;
    } else {
      this.d_I = -1;
    }
  }

  public final void a(int var1, int var2, int var3, int var4) {
    this.h_I = var1;
    this.i_I = var2;
    this.f_I = var3;
    this.g_I = var4;
  }

  public final void b(int var1, int var2) {
    this.h_I = var1;
    this.i_I = var2;
  }

  public abstract void a(Graphics var1);
}
