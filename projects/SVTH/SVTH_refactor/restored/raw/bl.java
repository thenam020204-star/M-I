import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bl extends cz {
  public byte a_B;
  private Image a_Image;
  public bx a_bx;
  public static byte b_B;
  public byte c_B = 4;
  public String a_String;
  public String b_String;
  public byte d_B;
  public static byte e_B = 21;
  public int[] a_AI;
  public byte f_B;
  public byte[] a_AB;
  public byte[][] a_AAB = new byte[4][5];
  public byte g_B;
  protected short a_S;
  protected short b_S;
  public short[][] a_AAS;
  protected boolean a_Z;
  public boolean b_Z;
  public boolean c_Z;
  public boolean d_Z;
  public d[] a_Ad;
  public short[] a_AS;
  public short[] b_AS;
  public ck[] a_Ack;
  int a_I = -1;
  protected boolean e_Z;
  public byte h_B;
  public String c_String;
  public boolean f_Z;
  private short[] c_AS;
  private short[] d_AS;
  public short c_S;
  public boolean g_Z = true;
  private short j_S;
  private short k_S;
  private byte j_B;
  boolean h_Z;
  boolean i_Z;
  public int b_I;
  public int c_I;
  int d_I;
  private short[] e_AS;
  private long a_J;
  private long b_J = 2000L;
  private boolean l_Z = false;

  public bl(Image var1) {
    this.a_B = 0;
    this.a_Image = var1;
    this.i_S = (short)this.a_Image.getHeight();
    this.d_AS = new short[]{
      (short)(-this.a_Image.getWidth() / 2), (short)(-this.a_Image.getHeight()), (short)this.a_Image.getWidth(), (short)this.a_Image.getHeight()
    };
    this.f_S = this.d_AS[2];
    this.g_S = this.d_AS[3];
    this.c_AS = new short[]{(short)(-this.a_Image.getWidth() / 2), -8, (short)this.a_Image.getWidth(), 8};
  }

  public bl(bx var1) {
    this.a_B = 1;
    this.a_bx = var1;
    if (this.a_bx != null) {
      this.d_AS = this.a_bx.a_AS();
    }

    short[] var2;
    if ((var2 = this.a_bx.a(0, 0, 2)) != null) {
      this.i_S = var2[3];
    } else {
      this.i_S = 40;
    }

    this.f_S = this.d_AS[2];
    this.g_S = this.d_AS[3];
  }

  public final void a(Graphics var1) {
    if (this.j_Z) {
      switch (this.a_B) {
        case 0:
          if (this.a_Image != null) {
            var1.drawImage(this.a_Image, this.d_S, this.e_S, 33);
            return;
          }
          break;
        case 1:
          if (this.a_bx != null) {
            this.a_bx.d_S = (short)this.d_S;
            this.a_bx.e_S = (short)this.e_S;
            this.a_bx.a(var1);
            if (this.g_Z && da.a_da.a_S % 2 == 0) {
              this.a_bx.a(true);
            }
          }
      }
    }
  }

  public bl() {
    this.d_B = 2;
  }

  public final short[] a_AS(int var1) {
    switch (this.a_B) {
      case 0:
        if (this.a_Image != null) {
          short[] var2;
          (var2 = new short[4])[0] = (short)(this.d_S - this.a_Image.getWidth() / 2);
          var2[1] = (short)(this.e_S - this.a_Image.getHeight() / 2);
          var2[2] = (short)this.a_Image.getWidth();
          var2[3] = (short)(this.a_Image.getHeight() / 2);
          this.g_S = (short)this.a_Image.getHeight();
          return var2;
        }

        return null;
      case 1:
        if (this.a_bx != null) {
          this.a_bx.d_S = this.d_S;
          this.a_bx.e_S = this.e_S;
          return this.a_bx.a(0, 0, var1);
        }

        return null;
      default:
        return null;
    }
  }

  public final void a_V(int var1) {
    if (this.a_B == 1) {
      this.d_B = (byte)var1;
      if (this.a_bx != null) {
        this.a_bx.a_V(var1);
        return;
      }
    } else if (this.a_B == 0) {
      this.d_B = (byte)var1;
    }
  }

  public final void a_V() {
    if (this.h_Z) {
      if (this.j_S - this.d_S < -this.c_B) {
        this.a_V(2);
        this.b_V();
        return;
      }

      if (this.j_S - this.d_S > this.c_B) {
        this.a_V(3);
        this.b_V();
        return;
      }

      if (this.k_S - this.e_S < -this.c_B) {
        this.a_V(1);
        this.b_V();
        return;
      }

      if (this.k_S - this.e_S > this.c_B) {
        this.a_V(0);
        this.b_V();
        return;
      }

      this.a_bx.b(0);
      this.h_Z = false;
      this.c_B = 4;
      if (this.j_B >= 0 && this.j_B <= 3) {
        this.a_V(this.j_B);
        this.c_V();
        if (at.d_Z) {
          System.out.println(this.h_S + " quay hướng " + this.j_B);
        }
      }

      da.a_da.a_cd.a_ci.a(5);
    }
  }

  public final void b_V() {
    if (this.a_B == 1) {
      if (this.a_bx != null) {
        this.a_bx.d_S = this.d_S;
        this.a_bx.e_S = this.e_S;
        this.e_AS = this.a_AS(1);
        if (this.e_AS != null) {
          for (byte var1 = 0; var1 < this.e_AS.length; var1 += 4) {
            switch (this.d_B) {
              case 0:
                this.e_AS[var1 + 1] = (short)(this.e_AS[var1 + 1] + this.c_B);
                break;
              case 1:
                this.e_AS[var1 + 1] = (short)(this.e_AS[var1 + 1] - this.c_B);
                break;
              case 2:
                this.e_AS[var1] = (short)(this.e_AS[var1] - this.c_B);
                break;
              case 3:
                this.e_AS[var1] = (short)(this.e_AS[var1] + this.c_B);
            }
          }
        }

        boolean var4;
        if (!this.h_Z) {
          label120:
          if (this.a(this.e_AS)) {
            if (this.f_Z) {
              short[] var3 = this.e_AS;
              byte var2 = 0;

              boolean var10000;
              while (true) {
                if (var2 >= var3.length) {
                  var10000 = false;
                  break;
                }

                if (var3[var2] < 0 || var3[var2] + var3[var2 + 2] > cd.a_S || var3[var2 + 1] < 0 || var3[var2 + 1] + var3[var2 + 3] > cd.b_S) {
                  var10000 = true;
                  break;
                }

                var2 += 4;
              }

              if (!var10000) {
                var4 = 1;
                break label120;
              }
            }

            var4 = 0;
          } else {
            var4 = 1;
          }
        } else {
          var4 = 1;
        }

        if (!var4) {
          if (this.h_S != 1 && this.h_S != 2 && this.h_S != 3) {
            this.c_V();
          }

          return;
        }

        switch (this.d_B) {
          case 0:
            this.e_S = (short)(this.e_S + this.c_B);
            break;
          case 1:
            this.e_S = (short)(this.e_S - this.c_B);
            break;
          case 2:
            this.d_S = (short)(this.d_S - this.c_B);
            break;
          case 3:
            this.d_S = (short)(this.d_S + this.c_B);
        }

        b_B = this.d_B;
        if (this != o.a_Abl[0] || o.a_AB == null || da.a_da.a_cd.a_ci.a_Z || this.b_Z || this.c_Z) {
          return;
        }

        if (this.a_I == -1) {
          Random var5;
          (var5 = new Random()).setSeed(System.currentTimeMillis());
          var4 = Math.abs(var5.nextInt()) / 42949672;
          this.a_I = var4 + 100;
        } else if (this.a_I > 0) {
          this.a_I--;
        } else {
          this.a_I = -1;
          if (da.a_da.a_cd.a_B != 2 || da.a_da.a_cd.a_ci.a_Z || this.h_Z) {
            return;
          }

          da.a_da.a_cd.a(false);
        }
      }
    } else if (this.a_Image != null) {
      switch (this.d_B) {
        case 0:
          this.e_S = (short)(this.e_S + this.c_B);
          return;
        case 1:
          this.e_S = (short)(this.e_S - this.c_B);
          return;
        case 2:
          this.d_S = (short)(this.d_S - this.c_B);
          return;
        case 3:
          this.d_S = (short)(this.d_S + this.c_B);
      }
    }
  }

  public final void c_V() {
    if (this.a_B == 1 && this.a_bx.b() == 8) {
      if (this.a_bx.a() == 0) {
        this.a_bx.a_V(4);
        return;
      }

      if (this.a_bx.a() == 1) {
        this.a_bx.a_V(5);
        return;
      }

      if (this.a_bx.a() == 2) {
        this.a_bx.a_V(6);
        return;
      }

      if (this.a_bx.a() == 3) {
        this.a_bx.a_V(7);
      }
    }
  }

  public final void a(int var1, int var2, int var3) {
    this.j_S = (short)var1;
    this.k_S = (short)var2;
    this.j_B = (byte)var3;
    this.h_Z = true;
  }

  public final void d_V() {
    this.a_AAS = null;
    o.a_V(this.c_B);
    this.a_AAS = new short[(o.a_Abl.length - 1) * o.d_B + 1][3];
    this.a_AAS[0][0] = this.d_S;
    this.a_AAS[0][1] = this.e_S;
    this.a_AAS[0][2] = (short)this.d_B;

    for (int var1 = 1; var1 < this.a_AAS.length; var1++) {
      this.a_AAS[var1][0] = this.a_AAS[var1 - 1][0];
      this.a_AAS[var1][1] = this.a_AAS[var1 - 1][1];
      this.a_AAS[var1][2] = this.a_AAS[var1 - 1][2];
      switch (this.d_B) {
        case 0:
          this.a_AAS[var1][1] = (short)(this.a_AAS[var1 - 1][1] - this.c_B);
          break;
        case 1:
          this.a_AAS[var1][1] = (short)(this.a_AAS[var1 - 1][1] + this.c_B);
          break;
        case 2:
          this.a_AAS[var1][0] = (short)(this.a_AAS[var1 - 1][0] + this.c_B);
          break;
        case 3:
          this.a_AAS[var1][0] = (short)(this.a_AAS[var1 - 1][0] - this.c_B);
      }
    }
  }

  public final void e_V() {
    o.a_V(this.c_B);
    short[][] var1;
    (var1 = new short[(o.a_Abl.length - 1) * o.d_B + 1][3])[0][0] = this.a_AAS[0][0];
    var1[0][1] = this.a_AAS[0][1];
    var1[0][2] = this.a_AAS[0][2];

    for (int var2 = 1; var2 < var1.length; var2++) {
      var1[var2][0] = this.d_S;
      var1[var2][1] = this.e_S;
      var1[var2][2] = (short)this.d_B;
      switch (this.d_B) {
        case 0:
          var1[var2][1] = (short)(var1[var2 - 1][1] - this.c_B);
          break;
        case 1:
          var1[var2][1] = (short)(var1[var2 - 1][1] + this.c_B);
          break;
        case 2:
          var1[var2][0] = (short)(var1[var2 - 1][0] + this.c_B);
          break;
        case 3:
          var1[var2][0] = (short)(var1[var2 - 1][0] - this.c_B);
      }
    }

    this.a_AAS = null;
    this.a_AAS = var1;
  }

  public final int a_I() {
    int var1 = 0 + this.a_AI[9];
    int var2 = 0;

    for (int var3 = 0; var3 < 6; var3++) {
      if (this.a_Ack[var3] != null) {
        var2 += this.a_Ack[var3].a_I();
      }
    }

    int var4;
    return var4 = var1 + var2;
  }

  public final int b_I() {
    int var1 = 0 + this.a_AI[7];
    int var2 = 0;

    for (int var3 = 0; var3 < 6; var3++) {
      if (this.a_Ack[var3] != null) {
        var2 += this.a_Ack[var3].b_I();
      }
    }

    int var4;
    return var4 = var1 + var2;
  }

  public final int c_I() {
    int var1 = 0 + this.a_AI[8];
    int var2 = 0;

    for (int var3 = 0; var3 < 6; var3++) {
      if (this.a_Ack[var3] != null) {
        var2 += this.a_Ack[var3].c_I();
      }
    }

    int var4;
    return var4 = var1 + var2;
  }

  public final int d_I() {
    int var1 = 0 + this.a_AI[4];
    int var2 = 0;

    for (int var3 = 0; var3 < 6; var3++) {
      if (this.a_Ack[var3] != null) {
        var2 += this.a_Ack[var3].d();
      }
    }

    int var4;
    return var4 = var1 + var2;
  }

  public final int e_I() {
    return this.a_AI[16];
  }

  public final int f_I() {
    int var1;
    return var1 = 0 + this.a_AI[17];
  }

  public final int g_I() {
    int var1;
    return var1 = 0 + this.a_AI[18];
  }

  public final int h_I() {
    int var1;
    return var1 = 0 + this.a_AI[19];
  }

  public final int i_I() {
    int var1;
    return var1 = 0 + this.a_AI[20];
  }

  public final int j_I() {
    int var1 = 0 + this.a_AI[6];
    int var2 = 0;

    for (int var3 = 0; var3 < 6; var3++) {
      if (this.a_Ack[var3] != null) {
        var2 += this.a_Ack[var3].e();
      }
    }

    int var4;
    return var4 = var1 + var2;
  }

  private boolean a(short[] var1) {
    short[] var3 = this.e_AS;
    ca var2 = da.a_da.a_cd.a_ca;
    if (da.a_da.a_cd.a_ca.a(var3, 1)) {
      return true;
    } else {
      for (byte var4 = 0; var4 < var1.length; var4 += 4) {
        if (var1[var4] < 0 || var1[var4] + var1[var4 + 2] > cd.a_S || var1[var4 + 1] < 0 || var1[var4 + 1] + var1[var4 + 3] > cd.b_S) {
          return true;
        }
      }

      if (da.a_da.a_cd.a_bj != null) {
        if (this.e_Z) {
          if (!o.a_Abl[0].b_Z) {
            if (da.a_da.a_cd.a_bj.a(var1, this, 1)) {
              return true;
            }
          } else if (da.a_da.a_cd.a_bj.a(var1, new cz[]{this}, 1)) {
            return true;
          }
        } else if (!o.a_Abl[0].b_Z && da.a_da.a_cd.a_bj.a(var1, o.a_Abl, 1)) {
          return true;
        }
      }

      return false;
    }
  }

  public final void f_V() {
    this.e_Z = false;
  }

  public final void g_V() {
    if (this.a_Z && !this.e_Z) {
      this.e_Z = true;
    }
  }

  public final void h_V() {
    if (this.a_Z && this.e_Z) {
      if (this.a_J == 0L) {
        this.a_J = System.currentTimeMillis();
      }

      if (System.currentTimeMillis() - this.a_J > this.b_J) {
        this.a_J = System.currentTimeMillis();
        this.b_J = (long)k.b(1000, 4000);
        if (this.l_Z) {
          this.l_Z = false;
          this.c_V();
        } else {
          int var1;
          if ((var1 = k.a(new int[]{0, 1, 2, 3, 4}, this.d_B)) < 4) {
            this.l_Z = true;
            this.a_V(var1);
          } else {
            this.l_Z = false;
          }
        }
      }

      if (this.l_Z) {
        this.b_V();
      }
    }
  }

  public final void i_V() {
    if (!da.a_da.a_cd.f_Z && da.a_da.a_cd.a_Z) {
      if (o.a_Abl[0].h_S == 1) {
        o.h_String = "/av/long1.av";
      } else {
        o.h_String = "/av/long2.av";
      }

      this.f_Z = true;
      o.a_Abl[0].c_B = 4;
      if (!at.a_String.equals("N73") && !at.a_String.equals("N7610")) {
        o.a_Abl[0].a_bx = null;
        o.a_Abl[0].a_bx = new bx();
        o.a_Abl[0].a_bx.a(o.h_String, -1);
        o.a_Abl[0].a_bx.a_V(this.d_B);
      } else {
        o.a_Abl[0].a_bx = null;
        o.a_Abl[0].a_bx = new bx();
        o.a_Abl[0].a_bx.a(o.h_String, 1);
        o.a_Abl[0].a_bx.a_V(this.d_B);
      }

      if (da.a_da.a_cd.a_bj != null) {
        bl var2 = o.a_Abl[0];
        bj var1 = da.a_da.a_cd.a_bj;
        da.a_da.a_cd.a_bj.c(var2);
        if (var1.b_Acz == null) {
          var1.b_Acz = new cz[1];
        } else {
          cz[] var3 = new cz[var1.b_Acz.length + 1];
          System.arraycopy(var1.b_Acz, 0, var3, 0, var1.b_Acz.length);
          var1.b_Acz = var3;
        }

        var1.b_Acz[var1.b_Acz.length - 1] = var2;
      }

      this.b_Z = true;
      da.a_da.a_cd.b_Z = false;
    }
  }

  public final void j_V() {
    if (!da.a_da.a_cd.f_Z && da.a_da.a_cd.a_Z) {
      if ((this.a(this.a_AS(1)) || da.a_da.a_cd.a_bj.a(this.a_AS(1), this, 1)) && !da.a_da.a_cd.a_ci.a_Z) {
        da.a_da.a("Không thể hạ cánh tại đây", 8);
      } else {
        this.f_Z = false;
        System.out.println("Kết thúc bay");
        this.b_Z = false;
        o.a_Abl[0].c_B = 4;
        if (!at.a_String.equals("N73") && !at.a_String.equals("N7610")) {
          o.a_Abl[0].a_bx.a(o.e_AString[this.h_S - 1], -1);
        } else {
          o.a_Abl[0].a_bx.a(o.e_AString[this.h_S - 1], 1);
        }

        bl var2 = o.a_Abl[0];
        bj var1 = da.a_da.a_cd.a_bj;
        da.a_da.a_cd.a_bj.b(var2);
        var1.a(var2);
        o.a_Abl[0].c_V();
      }
    }
  }

  public final short[] a_AS() {
    return this.d_AS;
  }

  public final void b(Graphics var1) {
    var1.setColor(16711680);
    if (this.a_B == 1) {
      if (this.a_bx != null) {
        this.a_bx.a(this.d_S, this.e_S);
        this.a_bx.b(var1);
        return;
      }
    } else if (this.a_B == 0) {
      for (byte var2 = 0; this.c_AS != null && var2 < this.c_AS.length; var2 += 4) {
        var1.drawRect(this.d_S + this.c_AS[var2], this.e_S + this.c_AS[var2 + 1], this.c_AS[var2 + 2], this.c_AS[var2 + 3]);
      }
    }
  }

  public final void k() {
    if (this.a_bx.c() < this.a_bx.a_I(this.c_I) - 1) {
      this.a_bx.a(true);
    } else if (this.b_I <= 1) {
      this.i_Z = false;
      this.a_bx.a_V(this.d_I);
      da.a_da.a_cd.a_ci.a(2);
    } else {
      this.b_I--;
      this.a_bx.b(0);
    }
  }

  public static bl a_bl(bl var0) {
    if (da.a_da.a_cd.a_bj != null) {
      for (int var1 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var1 < da.a_da.a_cd.a_bj.a_Acz.length; var1++) {
        if (da.a_da.a_cd.a_bj.a_Acz[var1] != null && da.a_da.a_cd.a_bj.a_Acz[var1].i_B == 1 && !k.a((bl)da.a_da.a_cd.a_bj.a_Acz[var1])) {
          short[] var2 = var0.a_AS(1);
          short[] var3;
          if ((var3 = da.a_da.a_cd.a_bj.a_Acz[var1].a_AS(1)) == null) {
            System.out.println("Lỗi!!!" + da.a_da.a_cd.a_bj.a_Acz[var1].h_S + " nhân vật chưa có khung va chạm");
          }

          if (cg.a(var2[0], var2[1], var2[2], var2[3], var3[0] - 5, var3[1] - 5, var3[2] + 10, var3[3] + 10)) {
            return (bl)da.a_da.a_cd.a_bj.a_Acz[var1];
          }
        }
      }
    }

    return null;
  }

  public static ax a_ax(bl var0) {
    if (da.a_da.a_cd.a_bj != null) {
      for (int var1 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var1 < da.a_da.a_cd.a_bj.a_Acz.length; var1++) {
        if (da.a_da.a_cd.a_bj.a_Acz[var1] != null && var0 != da.a_da.a_cd.a_bj.a_Acz[var1] && da.a_da.a_cd.a_bj.a_Acz[var1].i_B == 12) {
          short[] var3 = null;
          short[] var4 = var0.a_AS(1);
          switch (var0.d_B) {
            case 0:
              var3 = new short[]{var4[0], (short)(var4[1] + var4[3]), var4[2], (short)var4[3]};
              break;
            case 1:
              var3 = new short[]{var4[0], (short)(var4[1] - var4[3]), var4[2], (short)var4[3]};
              break;
            case 2:
              var3 = new short[]{(short)(var4[0] - var4[2]), var4[1], (short)var4[2], var4[3]};
              break;
            case 3:
              var3 = new short[]{(short)(var4[0] + var4[2]), var4[1], (short)var4[2], var4[3]};
          }

          if (cg.a_Z(var3, da.a_da.a_cd.a_bj.a_Acz[var1].a_AS(1))) {
            return (ax)da.a_da.a_cd.a_bj.a_Acz[var1];
          }
        }
      }
    }

    return null;
  }

  public static bl a_bl(int var0) {
    for (int var1 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var1 < da.a_da.a_cd.a_bj.a_Acz.length; var1++) {
      if (da.a_da.a_cd.a_bj.a_Acz[var1] != null && da.a_da.a_cd.a_bj.a_Acz[var1].h_S == var0 && da.a_da.a_cd.a_bj.a_Acz[var1].i_B == 1) {
        return (bl)da.a_da.a_cd.a_bj.a_Acz[var1];
      }
    }

    return null;
  }
}
