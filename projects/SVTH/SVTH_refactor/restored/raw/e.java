import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class e {
  int a_I;
  int b_I;
  int c_I;
  byte[] a_AB;
  public int d_I;
  public short a_S;
  public int[] a_AI = new int[21];
  public short b_S;
  public short c_S;
  public String a_String;
  public int e_I = 0;
  public m a_m = new m();
  public int f_I = 0;
  public boolean a_Z = false;
  byte[] b_AB;
  byte[] c_AB;
  public boolean b_Z = true;
  boolean c_Z;
  boolean d_Z;
  boolean e_Z = false;
  public boolean f_Z = false;
  boolean g_Z = false;
  boolean h_Z;
  boolean i_Z = false;
  public boolean j_Z = false;
  public boolean k_Z = false;
  public boolean l_Z = false;
  boolean m_Z = false;
  boolean n_Z = false;
  boolean o_Z;
  public boolean p_Z;
  private cj a_cj = new cj();
  int g_I;
  public int[] b_AI = new int[]{0, 0};
  public bx a_bx;
  public bx b_bx;
  bx[] a_Abx = null;
  bx c_bx;
  bx d_bx;
  bx e_bx;
  bx f_bx;
  bx g_bx;
  public short[] a_AS;
  public m b_m = new m();
  public m c_m = new m();
  public short[] b_AS;
  public d[] a_Ad;
  public short[] c_AS;
  int h_I;
  int i_I;
  public byte[] d_AB;
  int j_I;
  int k_I;
  int l_I;
  short d_S;
  public byte[] e_AB;
  public byte[] f_AB;
  public byte a_B;
  public byte b_B;
  public byte c_B;
  public byte d_B;
  public byte[] g_AB;
  public byte[] h_AB;
  public byte[] i_AB;
  public int m_I;
  public int n_I;
  public Image a_Image;
  int o_I;
  boolean q_Z;

  public e() {
    new m();
    this.i_I = 0;
    this.d_AB = new byte[4];
    this.d_S = 5;
    this.f_AB = new byte[]{0, 0};
    this.a_B = 0;
    this.b_B = 0;
    this.c_B = 0;
    this.d_B = 0;
    this.g_AB = new byte[]{0, 0};
    this.h_AB = new byte[]{0, 0};
    this.i_AB = new byte[]{0, 0};
    this.o_I = 0;
    this.q_Z = false;
    this.a_Image = cc.a("/pic/miss.gf");
  }

  public final void a(m var1) {
    this.b_m.a_I = var1.a_I;
    this.b_m.b_I = var1.b_I;
  }

  public final int a_I() {
    return this.e_I;
  }

  public final void a(Graphics var1) {
    if (this.b_Z) {
      if (this.f_I == 0 || this.f_I == 14 || this.f_I == 2 || this.f_I == 13 || this.f_I == 1 || this.f_I == 8 || this.f_I == 11) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
          return;
        }
      } else if (this.f_I == 9 || this.f_I == 17) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
          return;
        }
      } else if (this.f_I == 3) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
        }

        if (this.d_bx == null) {
          this.f_I = 0;
          return;
        }

        this.d_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.d_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.d_bx.a(true);
        }

        if (this.d_bx.c() >= this.d_bx.a_I(0) - 1) {
          this.d_bx.b(0);
          this.f_I = 0;
          return;
        }
      } else if (this.f_I == 15) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        if (this.a_I < 10) {
          if (this.a_I == 0) {
            if (at.a_String.equals("N7610")) {
              this.a_bx.a(var1);
            } else {
              this.a_bx.a(var1, true);
            }
          } else if (at.a_String.equals("N7610")) {
            this.a_bx.a(var1);
          } else {
            this.a_bx.a(var1, false);
          }
        } else if (at.a_String.equals("N7610")) {
          this.a_bx.a(var1);
        } else {
          this.a_bx.a(var1, true);
        }

        if (da.a_da.a_S % 3 == 0) {
          if (at.a_String.equals("N7610")) {
            this.a_bx.a(true);
            return;
          }

          this.a_bx.a(true);
          return;
        }
      } else if (this.f_I == 6 || this.f_I == 16) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
          return;
        }
      } else if (this.f_I == 5) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
          return;
        }
      } else if (this.f_I == 10) {
        this.a_bx.a(this.b_m.a_I, this.b_m.b_I);
        this.a_bx.a(var1);
        if (da.a_da.a_S % 3 == 0) {
          this.a_bx.a(true);
        }
      }
    }
  }

  public final void a_V() {
    this.g_I = cj.a(this.b_m.a_I, this.c_m.a_I);
    this.b_AI = cj.a_AI(this.b_m.a_I, this.b_m.b_I, this.c_m.a_I, this.c_m.b_I, this.a_bx.a_I(2));
    this.h_Z = false;
  }

  public final void a(int var1, int var2) {
    this.a_bx.a_V(var1);
    this.a_bx.b(0);
  }

  public static void a(bx var0, int var1, int var2) {
    var0.a_V(var1);
    var0.b(0);
  }
}
