import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bn {
  public e[] a_Ae = new e[20];
  public int a_I;
  public int b_I = 0;
  private int c_I;
  boolean a_Z = false;
  private int d_I;
  private boolean e_Z;
  private static byte[][] a_AAB;
  private int e_I = 0;
  private Image[] a_AImage = new Image[3];
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private Image d_Image;
  private Image e_Image;
  private Image f_Image;
  private Image g_Image;
  private Image h_Image;
  private Image i_Image;
  private Image j_Image;
  private bx a_bx;
  private int[][] a_AAI = new int[][]{{-17, -65}, {28, -40}, {31, -1}, {1, 27}, {-45, 24}, {-68, -10}, {-59, -49}};
  private int[][] b_AAI = new int[][]{{8, -57}, {32, -17}, {15, 21}, {-24, 31}, {-56, 12}, {-62, -25}, {-43, -60}};
  private int[][] c_AAI = new int[][]{{-17, -65}, {30, -15}, {-17, 32}, {-60, -17}};
  private int[][] d_AAI = new int[][]{{25, -30}, {30, 15}, {-45, 15}, {-45, -30}};
  private Image k_Image;
  private Image l_Image;
  private Image m_Image;
  private Image n_Image;
  private int f_I;
  private boolean f_Z;
  private boolean g_Z;
  private int g_I;
  private Image o_Image;
  private Image p_Image;
  private Image q_Image;
  private Image r_Image;
  private Image s_Image;
  private Image t_Image;
  private Image u_Image;
  private Image v_Image;
  private int[] a_AI;
  private int h_I;
  private ab a_ab;
  private ab b_ab;
  private ab c_ab;
  private ab d_ab;
  private dc a_dc;
  private int i_I;
  private int j_I;
  private int[][] e_AAI;
  private short[][] a_AAS;
  private int k_I;
  private Image w_Image;
  private bx b_bx;
  private Image x_Image;
  private Image y_Image;
  private Image z_Image;
  private bx c_bx;
  private int l_I;
  private int m_I;
  private short[] d_AS;
  private short[] e_AS;
  public bl[] a_Abl;
  public cv[] a_Acv;
  private short[] f_AS;
  private short[] g_AS;
  private byte a_B;
  private String[] a_AString;
  private short[][] b_AAS;
  private short[][] c_AAS;
  private short[][] d_AAS;
  private short[][] e_AAS;
  private short[] h_AS;
  public short[] a_AS;
  private String[][] a_AAString;
  private String[][] b_AAString;
  public short[] b_AS;
  public short[] c_AS;
  public boolean b_Z;
  private static byte b_B;
  private short[][] f_AAS;
  private short[] i_AS;
  private cf a_cf;
  private byte c_B;
  private String[] b_AString;
  private byte d_B;
  private int n_I;
  private bx d_bx;
  private bx e_bx;
  private int[] b_AI;
  private byte e_B;
  private Vector a_Vector;
  private int o_I;
  private String[] c_AString;
  private String[] d_AString;
  private static short a_S;
  private String a_String;
  private int p_I;
  private static int q_I = 0;
  private int r_I;
  boolean c_Z;
  private int s_I;
  private int t_I;
  private int u_I;
  private bx f_bx;
  private byte[] a_AB;
  private boolean h_Z;
  private short[][] g_AAS;
  private Image A_Image;
  private Image B_Image;
  private int v_I;
  private boolean i_Z;
  private Image C_Image;
  private int w_I;
  private int x_I;
  private int y_I;
  private int z_I;
  private int A_I;
  private int B_I;
  private String[] e_AString;
  private byte f_B;
  boolean d_Z;

  public bn() {
    String[] var10000 = new String[]{"Đánh", "Phòng thủ", "Kỹ năng", "Vật phẩm", "Sủng vật", "Hạ ngay", "Chạy", "Đánh", "Phòng thủ", "Kỹ năng", "Lui"};
    this.f_I = 0;
    this.f_Z = false;
    this.g_Z = false;
    this.h_I = 194;
    this.i_I = 0;
    this.j_I = 0;
    this.e_AAI = new int[20][5];
    this.l_I = 0;
    this.m_I = 0;
    this.f_AS = null;
    this.g_AS = null;
    this.a_B = 0;
    this.i_AS = new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38};
    this.n_I = 0;
    this.e_bx = null;
    this.a_Vector = new Vector();
    this.o_I = 0;
    this.p_I = 16777215;
    this.r_I = 556710;
    this.c_Z = true;
    this.s_I = 0;
    this.t_I = 0;
    this.u_I = 0;
    this.h_Z = false;
    this.g_AAS = null;
    int[] var1 = new int[]{15629412, 6868447};
    this.v_I = 0;
    this.i_Z = false;
    int[] var2 = new int[]{16660536, 12175614, 15964940, 9693914};
    this.w_I = 0;
    this.x_I = 0;
    this.y_I = 0;
    this.z_I = 0;
    this.A_I = 0;
    this.B_I = 0;
    this.d_Z = false;
  }

  public final boolean a_Z() {
    try {
      Thread.sleep(0L);
    } catch (InterruptedException var5) {
      var5.printStackTrace();
    }

    this.z_I = 191;
    this.a_AI = new int[]{(at.a_AS[0] - 193) / 2, (at.a_AS[1] - 255) / 2, 193, 28};
    this.h_I = 200;
    this.c_V(90, this.h_I - 90);
    this.c_I = 10;
    this.c_bx = new bx();
    this.c_bx.a("/av/long1.av");
    if (this.e_bx == null) {
      this.e_bx = new bx();
      this.e_bx.a("/ft/tanshe.av");
    }

    if (this.A_Image == null) {
      this.A_Image = cc.a("/pic/grain1.gf");
    }

    if (this.B_Image == null) {
      this.B_Image = cc.a("/pic/grain2.gf");
    }

    if (this.y_Image == null) {
      this.y_Image = cc.a_Image("/pic/star.gf", 2);
    }

    if (this.u_Image == null) {
      this.u_Image = cc.a("/icon/12.gf");
    }

    if (this.v_Image == null) {
      this.v_Image = cc.a("/icon/10.gf");
    }

    if (d.a_AString == null) {
      d.a();
    }

    if (o.b_AAString == null) {
      o.b_AAString = cg.a_AAString(cg.a_String("/bin/skill.bin"), "skill:", "skillend", null, "\t");
    }

    this.m_I = 10;
    this.w_Image = cc.a("/ftmap/" + o.b_B + ".jpg");
    this.z_Image = cc.a("/pic/E020q03.gf");
    this.n_Image = cc.a_Image("/pic/number01.gf", 2);
    this.b_bx = new bx();
    this.b_bx.a("/pic/D25.av");
    this.k_Image = cc.a("/pic/D26.gf");
    byte[] var1 = be.a(cg.b_AB("/pic/D26.gf"), 50);
    this.l_Image = Image.createImage(var1, 0, var1.length);
    this.m_Image = cc.a("/pic/D27.gf");
    this.a_AImage[0] = cc.a("/pic/D28.gf");
    this.a_AImage[1] = cc.a("/pic/D32.gf");
    this.a_AImage[2] = cc.a("/pic/D33.gf");
    String var10001 = "/pic/D09.gf";
    this.e_Image = null;
    this.m_I = 20;
    this.a_Image = cc.a_Image("/pic/number01.gf", 2);
    this.b_Image = cc.a_Image("/pic/number03.gf", 2);
    cc.a_Image("/pic/number04.gf", 2);
    this.j_Image = cc.a("/pic/battleNumber1.gf");
    if (this.f_Image == null) {
      this.f_Image = cc.a("/pic/tzjy.gf");
    }

    this.c_Image = cc.a_Image("/pic/D02.gf", 2);
    this.d_Image = cc.a("/pic/D05.gf");
    this.g_Image = cc.a("/pic/D13.gf");
    this.i_Image = cc.a("/pic/D29.gf");
    this.h_Image = cc.a("/pic/up_down.gf");
    this.x_Image = cc.a_Image("/pic/arrow.gf", 2);
    cc.a("/pic/battlelist.gf");
    cc.a("/pic/battlelist1.gf");
    this.m_I = 30;
    this.o();
    String var6;
    String var2 = cg.a(var6 = cg.a_String("/bin/battlepos.bin"), "team:", "teamEnd");
    String var7 = cg.a(var6, "enemy:", "enemyEnd");
    String var3 = cg.a_String("/bin/enemy.bin");
    if (this.b_Z) {
      if (da.a_da.a_cd.r_Z) {
        if ((var3 = cg.a(var3, "teachMode:", "teachModeEnd")) != null) {
          this.c_B = 1;
          var3 = cg.a(var3, "teach1:", "teach1End");
          this.b_AString = cg.b_AString(var3, "\r\n");
        }
      } else if ((var3 = cg.a(var3, "enemy" + this.b_AS[0] + ":", "enemy" + this.b_AS[0] + "End")) != null) {
        var3 = cg.a(var3, "start:", "startEnd");
        this.b_AString = cg.b_AString(var3, "\r\n");
      }
    }

    this.d_AS = new short[3];
    this.e_AS = new short[3];
    var3 = cg.a(var2, "pos:", "posEnd");
    this.d_AS = cg.a_AS(var3, "x");
    this.e_AS = cg.a_AS(var3, "y");
    this.m_I = 40;
    if (this.b_AS == null) {
      if (o.a_AB != null) {
        this.a_B = (byte)k.a(o.a_AB[0], o.a_AB[1] - o.a_AB[0] + 1);
      }
    } else {
      this.a_B = (byte)this.b_AS.length;
    }

    int[] var15 = cg.a_AI(var7, "x");
    int[] var9 = cg.a_AI(var7, "y");
    var1 = cg.a_AB(var7, "amount" + this.a_B);

    for (int var4 = 0; var1 != null && var4 < var1.length; var4++) {
      this.f_AS = cg.a_AS(this.f_AS, var15[var1[var4]]);
      this.g_AS = cg.a_AS(this.g_AS, var9[var1[var4]]);
    }

    this.m_I = 50;
    this.a_AString = new String[this.a_B];
    this.b_AAS = new short[this.a_B][];
    this.c_AAS = new short[this.a_B][];
    this.d_AAS = new short[this.a_B][];
    this.e_AAS = new short[this.a_B][];
    this.h_AS = new short[this.a_B];
    this.m_I = 60;
    if (a_AAB == null) {
      i();
    }

    this.m_I = 70;
    this.k();
    this.m_I = 80;
    this.l();
    this.m_I = 90;
    this.j();
    this.m();
    this.c_I = this.b_I(10, 20);
    this.m_I = 100;
    return true;
  }

  public static void a_V(int var0) {
    cd.j();
    String var1 = null;
    if (var0 == 0) {
      var1 = o.j_String;
    } else if (var0 == 1) {
      var1 = o.k_String;
    } else if (var0 == 2) {
      var1 = o.l_String;
    }

    if (var1 != null && !var1.equals("")) {
      if (Main.music == null) {
        Main.music = new bv();
      }

      Main.music.a("/music/" + var1 + ".mid");
      Main.music.b(o.g_B);
      Main.music.a();
    }
  }

  private void h() {
    this.n();
    o.c_I += 2;
    this.b_V();
  }

  public final void a_V() {
    for (int var1 = 10; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null && !this.a_Ae[var1].f_Z) {
        this.a_Ae[var1].m_I = 0;
        this.a_Ae[var1].f_Z = true;
        this.a_Ae[var1].a_bx.j_Z = false;
        this.a_Ae[var1].f_I = 128;
      }
    }
  }

  public final void b_V() {
    Vector var1 = new Vector();
    Vector var2 = new Vector();
    int var3 = 0;

    for (int var4 = 0; var4 < this.a_Ae.length; var4++) {
      if (this.a_Ae[var4] != null) {
        this.a_Ae[var4].a_AI[3] = this.a_Ae[var4].m_I;
        this.a_Ae[var4].a_AI[5] = this.a_Ae[var4].n_I;
      }
    }

    for (int var10 = 10; this.a_Ae != null && var10 < this.a_Ae.length; var10++) {
      if (this.a_Ae[var10] != null) {
        var3 += this.a_Ae[var10].a_AI[1];
      }
    }

    if (da.a_da.a_cd.a_ci.c_Z) {
      var3 = 0;
    }

    this.b_AI = cg.a_AI(this.b_AI, 0);
    if (var3 > 0 && df.c_B > 1) {
      var1.addElement(df.c_B + "x EXP");
      this.b_AI = cg.a_AI(this.b_AI, 0);
    }

    if (ck.a_AString == null) {
      ck.a_V();
    }

    o.d();
    this.t();
    this.u();
    if (var3 > 0) {
      for (int var11 = 0; var11 < this.a_Abl.length; var11++) {
        if (this.a_Abl[var11] != null) {
          var3 = 0;

          for (int var5 = 10; this.a_Ae != null && var5 < this.a_Ae.length; var5++) {
            if (this.a_Ae[var5] != null) {
              var3 += this.a_Ae[var5].a_AI[1];
            }
          }

          if (df.c_B > 1) {
            var3 *= df.c_B;
          }

          if (var11 == 0) {
            var2.addElement(this.a_Abl[var11].a_String + " EXP+" + var3);
            this.b_AI = cg.a_AI(this.b_AI, 0);
            bl var18;
            (var18 = new bl()).a_String = this.a_Abl[var11].a_String;
            var18.a_AI = new int[this.a_Abl[var11].a_AI.length];

            for (int var22 = 0; var22 < this.a_Abl[var11].a_AI.length; var22++) {
              var18.a_AI[var22] = this.a_Abl[var11].a_AI[var22];
            }

            for (this.a_Abl[var11].a_AI[1] = this.a_Abl[var11].a_AI[1] + var3;
              this.a_Abl[var11].a_AI[1] >= this.a_Abl[var11].a_AI[2];
              this.a_Abl[var11].a_AI[5] = this.a_Abl[var11].a_AI[11]
            ) {
              if (!this.a_Vector.contains(var18)) {
                this.a_Vector.addElement(var18);
              }

              this.a_Abl[var11].a_AI[0]++;
              this.a_Abl[var11].a_AI[15] = this.a_Abl[var11].a_AI[2];
              int var23 = this.a_Ae[var11].a_AI[0] + 1;
              this.a_Abl[var11].a_AI[2] = var23 * var23 * var23 / 2 + var23 * var23;
              this.a_Abl[var11].a_AI[1] = 0;
              this.a_Abl[var11].a_AI[16] = this.a_Abl[var11].a_AI[16] + 10;
              o.d(this.a_Abl[var11]);
              this.a_Abl[var11].a_AI[3] = this.a_Abl[var11].a_AI[10];
            }
          } else if (this.a_Abl[var11].a_AI[3] > 0) {
            var2.addElement(this.a_Abl[var11].a_String + " EXP +" + var3);

            for (this.a_Abl[var11].a_AI[1] = this.a_Abl[var11].a_AI[1] + var3;
              this.a_Abl[var11].a_AI[1] >= this.a_Abl[var11].a_AI[2];
              this.b_AI = cg.a_AI(this.b_AI, 0)
            ) {
              bl var17;
              (var17 = new bl()).a_String = this.a_Abl[var11].a_String;
              var17.a_AI = new int[this.a_Abl[var11].a_AI.length];

              for (int var6 = 0; var6 < this.a_Abl[var11].a_AI.length; var6++) {
                var17.a_AI[var6] = this.a_Abl[var11].a_AI[var6];
              }

              if (!this.a_Vector.contains(var17)) {
                this.a_Vector.addElement(var17);
              }

              this.a_Abl[var11].a_AI[0]++;
              this.a_Abl[var11].a_AI[15] = this.a_Abl[var11].a_AI[2];
              int var21 = this.a_Abl[var11].a_AI[0] + 1;
              this.a_Abl[var11].a_AI[2] = var21 * var21 * var21 / 2 + var21 * var21;
              this.a_Abl[var11].a_AI[1] = 0;
              o.e(this.a_Abl[var11]);
              this.a_Abl[var11].a_AI[3] = this.a_Abl[var11].a_AI[10];
              this.a_Abl[var11].a_AI[5] = this.a_Abl[var11].a_AI[11];
            }
          }
        }
      }
    }

    int var12;
    if ((var12 = k.a(0, 100)) < 15) {
      var1.addElement("Thịt ngon X 1");
      this.b_AI = cg.a_AI(this.b_AI, 0);
      o.b(17, 1);
    } else if (var12 < 30) {
      var1.addElement("Thịt thường X 1");
      this.b_AI = cg.a_AI(this.b_AI, 0);
      o.b(16, 1);
    } else if (var12 < 45) {
      var1.addElement("Thịt kém X 1");
      this.b_AI = cg.a_AI(this.b_AI, 0);
      o.b(15, 1);
    }

    for (int var8 = 0; this.b_AAS != null && var8 < this.b_AAS.length; var8++) {
      for (int var19 = 0; this.b_AAS[var8] != null && var19 < this.b_AAS[var8].length; var19++) {
        if (this.a_Ae[var8 + 10] != null) {
          boolean var24 = true;
          if (this.c_AAS[var8] != null && var19 < this.c_AAS[var8].length) {
            short var13 = this.c_AAS[var8][var19];
            if (df.b_Z) {
              var13 <<= 1;
            }

            if (k.b(0, 100) <= var13) {
              var24 = true;
            } else {
              var24 = false;
            }
          }

          if (var24 && this.b_AAS[var8][var19] <= o.f_AString.length) {
            boolean var14 = false;

            for (int var25 = 0; var25 < var1.size(); var25++) {
              if (((String)var1.elementAt(var25)).equals("" + o.f_AString[this.b_AAS[var8][var19] - 1])) {
                var14 = true;
              }
            }

            if (!var14) {
              var1.addElement("" + o.f_AString[this.b_AAS[var8][var19] - 1] + " X 1");
              this.b_AI = cg.a_AI(this.b_AI, 0);
            }

            o.b(this.b_AAS[var8][var19], 1);
          }
        }
      }
    }

    for (int var9 = 0; this.d_AAS != null && var9 < this.d_AAS.length; var9++) {
      for (int var20 = 0; this.d_AAS[var9] != null && var20 < this.d_AAS[var9].length; var20++) {
        boolean var26 = true;
        if (this.e_AAS[var9] != null && var20 < this.e_AAS[var9].length) {
          short var15 = this.e_AAS[var9][var20];
          if (df.b_Z) {
            var15 <<= 1;
          }

          if (k.b(1, 1000) <= var15) {
            var26 = true;
          } else {
            var26 = false;
          }
        }

        if (var26) {
          ck.a_V();
          if (this.d_AAS[var9][var20] <= ck.a_AString.length) {
            boolean var16 = false;

            for (int var27 = 0; var27 < var1.size(); var27++) {
              if (((String)var1.elementAt(var27)).equals("Nhận " + ck.a_AString[this.d_AAS[var9][var20] - 1])) {
                var16 = true;
              }
            }

            if (!var16) {
              var1.addElement("Nhận " + ck.a_AString[this.d_AAS[var9][var20] - 1]);
              this.b_AI = cg.a_AI(this.b_AI, 0);
            }

            o.d(this.d_AAS[var9][var20], -1);
          }
          break;
        }
      }
    }

    if (var1.size() > 0) {
      this.c_AString = new String[var1.size()];
      var1.copyInto(this.c_AString);
    }

    if (var2.size() > 0) {
      this.d_AString = new String[var2.size()];
      var2.copyInto(this.d_AString);
    }

    this.r();
    this.c_V(142, this.h_I - 142);
    this.a_I = 24;
    a_S = 0;
    if (this.c_AString == null) {
      this.e_B = 1;
    } else {
      this.e_B = 0;
    }

    this.p();
  }

  private void a(String[] var1) {
    if (this.a_cf == null) {
      this.a_cf = new cf();
    }

    Vector var2 = new Vector();
    String[] var3 = null;

    for (int var4 = 0; var4 < var1.length; var4++) {
      if (var1[var4].indexOf("&") != -1) {
        var1[var4] = k.a(var1[var4], "&", o.c_AString[0]);
      }

      var2.addElement(var1[var4]);
    }

    if (var2.size() > 0) {
      var3 = new String[var2.size()];
      var2.copyInto(var3);
    }

    int var6 = var3[0].indexOf(".");
    int var5 = var3[0].indexOf("$");
    cg.a_S(var3[0].substring(var5 + 1, var6));
    this.a_cf.a_AString = var3;
    this.a_cf.a_B = 0;
    this.a_cf.e();
    this.a_cf.a_Z = true;
    this.a_I = 4;
  }

  private static void i() {
    String var0 = cg.a_String("/bin/skill.bin");
    if (o.b_AAString == null) {
      o.b_AAString = cg.a_AAString(var0, "skill:", "skillend", null, "\t");
    }

    if (d.a_AString == null) {
      d.a();
    }

    a_AAB = cg.a_AAB(var0, "headNum:", "headNumEnd", "=");
  }

  private void j() {
    o.d();
    Vector var1 = new Vector();

    for (int var2 = 0; o.a_AAS != null && var2 < o.a_AAS.length; var2++) {
      int var3 = o.d_I(o.a_AAS[var2][0]);
      if (o.g_AS[var3] == 1) {
        var1.addElement(o.a_AAS[var2]);
      }
    }

    if (var1.size() > 0) {
      this.f_AAS = new short[var1.size()][];
      var1.copyInto(this.f_AAS);
    } else {
      this.f_AAS = null;
    }

    if (this.f_AAS != null) {
      b_B = 0;
      if (0 > this.f_AAS.length - 1) {
        b_B = (byte)(this.f_AAS.length - 1);
        return;
      }
    } else {
      b_B = 0;
    }
  }

  private void k() {
    if (a_AAB == null) {
      i();
    }

    if (o.d_AAString == null) {
      o.d_AAString = cg.a_AAString(cg.a_String("/bin/team.bin"), "role:", "roleEnd", null, "\t");
    }

    if (this.a_String == null) {
      this.a_String = cg.a_String("/bin/team.bin");
    }

    for (int var1 = 0; var1 < this.a_Abl.length; var1++) {
      this.a_Ae[var1] = w.a_e();
      this.a_Ae[var1].a_String = this.a_Abl[var1].a_String;
      this.a_Ae[var1].a_I = var1;
      this.a_Ae[var1].d_I = this.a_Abl[var1].h_S;
      this.a_Ae[var1].a_S = this.a_Abl[var1].c_S;
      if (var1 != 0) {
        this.a_Ae[var1].a_bx = new bx();
        this.a_Ae[var1].a_bx = k.a(this.a_Abl[var1].h_S, true);
        if (this.a_Ae[var1].a_bx.a_I(2) > 1) {
          e var30 = this.a_Ae[var1];
          byte var18 = 0;
          var30.e_I = var18;
        } else {
          e var31 = this.a_Ae[var1];
          byte var19 = 1;
          var31.e_I = var19;
        }

        this.a_Ae[var1].b_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[2];
        this.a_Ae[var1].c_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[3];
        this.a_Ae[var1].d_bx = new bx();
        this.a_Ae[var1].d_bx.a("/ft/defense.av", 1);
        cc.a("/pic/battleNumber1.gf");
        String var8;
        String var20 = cg.a_String(var8 = cg.a(this.a_String, "attackDefault:", "attackDefaultEnd"), "actEffect");
        String var24 = cg.a_String(var8, "frame");
        this.a_Ae[var1].c_AB = cg.b_AB(var24, ",");
        this.a_Ae[var1].c_bx = new bx();
        this.a_Ae[var1].c_bx.a(var20, 1);
      } else {
        byte var2 = 0;

        for (int var3 = 0; var3 < o.d_AAString.length; var3++) {
          if (cg.a_I(o.d_AAString[var3][1]) == this.a_Abl[var1].h_S) {
            var2 = (byte)(var3 + 1);
            break;
          }
        }

        this.a_Abl[var1].c_String = o.d_AAString[var2 - 1][2];
        if (this.a_Abl[var1].c_String.toLowerCase().endsWith(".av")) {
          cc.a("/pic/battleNumber1.gf");
          cc.a("/pic/battleNumber2.gf");
          this.a_Ae[var1].a_bx = new bx();
          this.a_Ae[var1].a_bx.a(this.a_Abl[var1].c_String, 1);
          if (this.a_Ae[var1].a_bx.a_I(2) > 1) {
            e var10000 = this.a_Ae[var1];
            byte var13 = 0;
            var10000.e_I = var13;
          } else {
            e var29 = this.a_Ae[var1];
            byte var14 = 1;
            var29.e_I = var14;
          }

          this.a_Ae[var1].b_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[2];
          this.a_Ae[var1].c_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[3];
          this.a_Ae[var1].d_bx = new bx();
          this.a_Ae[var1].d_bx.a("/ft/defense.av", 1);
          if (this.a_Ae[var1].d_I == 1) {
            this.a_bx = new bx();
            this.a_bx.a("/face/1f.av");
            this.a_Ae[var1].b_bx = new bx();
            this.a_Ae[var1].b_bx.a("/face/zdtx1.av", 1);
            String var15;
            String var4 = cg.a_String(var15 = cg.a(this.a_String, "attack1:", "attack1End"), "actEffect");
            String var5 = cg.a_String(var15, "frame");
            this.a_Ae[var1].c_AB = cg.b_AB(var5, ",");
            this.a_Ae[var1].c_bx = new bx();
            this.a_Ae[var1].c_bx.a(var4, 1);
          } else if (this.a_Ae[var1].d_I == 2) {
            this.a_bx = new bx();
            this.a_bx.a("/face/2f.av");
            this.a_Ae[var1].b_bx = new bx();
            this.a_Ae[var1].b_bx.a("/face/zdtx2.av", 1);
            this.a_Ae[var1].b_bx.b(0);
            String var16;
            String var22 = cg.a_String(var16 = cg.a(this.a_String, "attack2:", "attack2End"), "actEffect");
            String var6 = cg.a_String(var16, "frame");
            this.a_Ae[var1].c_AB = cg.b_AB(var6, ",");
            this.a_Ae[var1].c_bx = new bx();
            this.a_Ae[var1].c_bx.a(var22, 1);
          } else {
            String var17;
            String var23 = cg.a_String(var17 = cg.a(this.a_String, "attackDefault:", "attackDefaultEnd"), "actEffect");
            String var7 = cg.a_String(var17, "frame");
            this.a_Ae[var1].c_AB = cg.b_AB(var7, ",");
            this.a_Ae[var1].c_bx = new bx();
            this.a_Ae[var1].c_bx.a(var23, 1);
          }
        } else {
          this.a_Ae[var1].e_I = 0;
          cc.a(this.a_Abl[var1].c_String);
        }
      }

      this.a_Ae[var1].e_bx = new bx();
      this.a_Ae[var1].e_bx.a("/ft/zhongdu.av", 1);
      this.a_Ae[var1].f_bx = new bx();
      this.a_Ae[var1].f_bx.a("/ft/zhunluan.av", 1);
      this.a_Ae[var1].g_bx = new bx();
      this.a_Ae[var1].g_bx.a("/ft/zhongyun.av", 1);
      if (var1 == 0) {
        this.a_Ae[var1].c_m.a_I = this.d_AS[1];
        this.a_Ae[var1].c_m.b_I = this.e_AS[1];
      } else if (var1 == 1) {
        this.a_Ae[var1].c_m.a_I = this.d_AS[0];
        this.a_Ae[var1].c_m.b_I = this.e_AS[0];
      } else {
        this.a_Ae[var1].c_m.a_I = this.d_AS[var1];
        this.a_Ae[var1].c_m.b_I = this.e_AS[var1];
      }

      this.a_Ae[var1].b_m.a_I = this.a_Ae[var1].c_m.a_I;
      this.a_Ae[var1].b_m.b_I = this.a_Ae[var1].c_m.b_I;
      this.a_Ae[var1].b_Z = true;
      Vector var9 = new Vector();
      Vector var21 = new Vector();
      this.a_Ae[var1].a_Ad = null;
      this.a_Ae[var1].c_AS = null;
      if (this.a_Abl[var1].a_AS != null && this.a_Abl[var1].a_AS.length > 0) {
        this.a_Ae[var1].a_AS = new short[this.a_Abl[var1].a_AS.length];
      }

      for (int var25 = 0; this.a_Abl[var1].a_AS != null && var25 < this.a_Abl[var1].a_AS.length; var25++) {
        this.a_Ae[var1].a_AS[var25] = this.a_Abl[var1].a_AS[var25];
        if (!cg.a_Z(this.i_AS, this.a_Abl[var1].a_AS[var25])) {
          var9.addElement(new Short(this.a_Abl[var1].a_AS[var25]));
          var21.addElement(this.a_Abl[var1].a_Ad[var25]);
        }

        if (this.a_Abl[var1].a_AS[var25] == 7) {
          this.a_Ae[var1].j_Z = true;
        }

        if (this.a_Abl[var1].a_AS[var25] == 37) {
          this.a_Ae[var1].k_Z = true;
        }

        if (this.a_Abl[var1].a_AS[var25] == 36) {
          this.a_Ae[var1].n_Z = true;
        }
      }

      if (var9.size() > 0) {
        this.a_Ae[var1].c_AS = new short[var9.size()];
        this.a_Ae[var1].a_Ad = new d[var21.size()];
        Short[] var26 = new Short[var9.size()];
        var9.copyInto(var26);

        for (int var10 = 0; var10 < this.a_Ae[var1].c_AS.length; var10++) {
          this.a_Ae[var1].c_AS[var10] = var26[var10];
          this.a_Ae[var1].a_Ad[var10] = (d)var21.elementAt(var10);
        }
      }

      this.a_Ae[var1].d_AB = new byte[4];
      this.a_Ae[var1].e_AB = new byte[4];

      for (int var27 = 0; var27 < this.a_Abl[var1].a_AB.length; var27++) {
        this.a_Ae[var1].d_AB[var27] = this.a_Abl[var1].a_AB[var27];
        this.a_Ae[var1].e_AB[var27] = this.a_Abl[var1].a_AB[var27];
      }

      byte var28 = 0;
      if (this.a_Ae[0].a_AS != null && cg.a_Z(this.a_Ae[0].a_AS, 35)) {
        var28 = 5;
      }

      for (int var11 = 0; var11 < this.a_Abl[var1].a_AI.length; var11++) {
        this.a_Ae[var1].a_AI[var11] = this.a_Abl[var1].a_AI[var11];
      }

      this.a_Ae[var1].j_I = this.a_Abl[var1].a_AI[12] * (var28 + 100) / 100;
      this.a_Ae[var1].k_I = this.a_Abl[var1].a_AI[13] * (var28 + 100) / 100;
      this.a_Ae[var1].l_I = this.a_Abl[var1].a_AI[14] * (var28 + 100) / 100;
      if (o.b_I > 0) {
        this.a_Ae[var1].j_I = this.a_Ae[var1].j_I * o.b_I;
        this.a_Ae[var1].k_I = this.a_Ae[var1].k_I * o.b_I;
        this.a_Ae[var1].l_I = this.a_Ae[var1].l_I * o.b_I;
      }

      if (this.a_Ae[var1].c_AS != null && cg.a_Z(this.a_Ae[var1].c_AS, 38)) {
        this.a_Ae[var1].l_I = this.a_Ae[var1].l_I + this.a_Abl[var1].a_AI[14] * 20 / 100;
      }

      this.a_Ae[var1].j_I = this.a_Ae[var1].j_I + a_I(this.a_Ae[var1]);

      for (int var12 = 0; var12 < 6; var12++) {
        if (this.a_Abl[var1].a_Ack != null && this.a_Abl[var1].a_Ack[var12] != null) {
          this.a_Ae[var1].d_S = (short)(this.a_Ae[var1].d_S + (short)(this.a_Abl[var1].a_Ack[var12].n_S + this.a_Abl[var1].a_Ack[var12].g_S));
        }
      }

      this.a_Ae[var1].m_I = this.a_Ae[var1].a_AI[3];
      this.a_Ae[var1].n_I = this.a_Ae[var1].a_AI[5];
    }
  }

  private void l() {
    if (this.a_String == null) {
      this.a_String = cg.a_String("/bin/team.bin");
    }

    for (int var2 = 0; var2 < this.a_B; var2++) {
      short var7;
      if (this.b_AS == null) {
        var7 = k.b(0, o.a_AS.length - 1);
        var7 = o.a_AS[var7];
      } else {
        var7 = this.b_AS[var2];
      }

      String var3 = cg.a_String("/bin/petdata.bin");
      if (this.a_AAString == null) {
        this.a_AAString = cg.a_AAString(var3, "petdata", "end", null, "\t");
      }

      if (this.b_AAString == null) {
        this.b_AAString = cg.a_AAString(var3, "stardata", "end", null, "\t");
      }

      this.a_AString[var2] = this.a_AAString[var7 - 1][0];
      this.a_Ae[var2 + 10] = w.a_e();
      cc.a("/pic/battleNumber1.gf");
      cc.a("/pic/battleNumber2.gf");
      this.a_Ae[var2 + 10].a_bx = k.a(var7, false);
      if (this.a_Ae[var2 + 10].a_bx.a_I(2) > 1) {
        e var10000 = this.a_Ae[var2 + 10];
        byte var4 = 0;
        var10000.e_I = var4;
      } else {
        e var27 = this.a_Ae[var2 + 10];
        byte var12 = 1;
        var27.e_I = var12;
      }

      short[] var8 = this.a_Ae[var2 + 10].a_bx.a(0, 0, 0);
      this.a_Ae[var2 + 10].b_S = var8[2];
      this.a_Ae[var2 + 10].c_S = var8[3];
      this.a_Ae[var2 + 10].a_String = this.a_AString[var2];
      this.a_Ae[var2 + 10].d_I = var7;
      this.a_Ae[var2 + 10].d_bx = new bx();
      this.a_Ae[var2 + 10].d_bx.a("/ft/defense.av", 1);
      this.a_Ae[var2 + 10].e_bx = new bx();
      this.a_Ae[var2 + 10].e_bx.a("/ft/zhongdu.av", 1);
      this.a_Ae[var2 + 10].f_bx = new bx();
      this.a_Ae[var2 + 10].f_bx.a("/ft/zhunluan.av", 1);
      this.a_Ae[var2 + 10].g_bx = new bx();
      this.a_Ae[var2 + 10].g_bx.a("/ft/zhongyun.av", 1);
      String var13 = cg.a_String(var3 = cg.a(this.a_String, "attackDefault:", "attackDefaultEnd"), "actEffect");
      var3 = cg.a_String(var3, "frame");
      this.a_Ae[var2 + 10].c_AB = cg.b_AB(var3, ",");
      this.a_Ae[var2 + 10].c_bx = new bx();
      this.a_Ae[var2 + 10].c_bx.a(var13, 1);
      int var11 = var7 - 1;
      this.a_Ae[var2 + 10].a_AI = new int[bl.e_B];
      if (this.b_AS == null) {
        k.a(1, 1000);
        if (o.d_String == null) {
          this.a_Ae[var2 + 10].a_AI[0] = k.b(this.a_Ae[0].a_AI[0] - 3, this.a_Ae[0].a_AI[0] + 3);
        } else {
          int[] var5;
          int var14;
          if ((var14 = (var5 = cg.b_AI(o.d_String, ","))[1] - var5[0]) <= 0) {
            var14 = 1;
          }

          this.a_Ae[var2 + 10].a_AI[0] = k.a(var5[0], var14);
        }
      } else if (this.c_AS != null) {
        this.a_Ae[var2 + 10].a_AI[0] = this.c_AS[var2];
      } else {
        this.a_Ae[var2 + 10].a_AI[0] = 1;
      }

      int var15 = var7 - 1;
      int[] var20 = k.a(this.a_AAString, this.b_AAString, var15, 3, this.a_Ae[var2 + 10].a_AI[0]);
      this.a_Ae[var2 + 10].a_AI[3] = var20[1];
      var20 = k.a(this.a_AAString, this.b_AAString, var15, 5, this.a_Ae[var2 + 10].a_AI[0]);
      this.a_Ae[var2 + 10].a_AI[5] = var20[1];
      var20 = k.a(this.a_AAString, this.b_AAString, var15, 7, this.a_Ae[var2 + 10].a_AI[0]);
      this.a_Ae[var2 + 10].a_AI[12] = var20[1];
      var20 = k.a(this.a_AAString, this.b_AAString, var15, 9, this.a_Ae[var2 + 10].a_AI[0]);
      this.a_Ae[var2 + 10].a_AI[13] = var20[1];
      var20 = k.a(this.a_AAString, this.b_AAString, var15, 11, this.a_Ae[var2 + 10].a_AI[0]);
      this.a_Ae[var2 + 10].a_AI[14] = var20[1];
      this.a_Ae[var2 + 10].a_AI[11] = this.a_Ae[var2 + 10].a_AI[5];
      this.a_Ae[var2 + 10].a_AI[10] = this.a_Ae[var2 + 10].a_AI[3];
      this.a_Ae[var2 + 10].m_I = this.a_Ae[var2 + 10].a_AI[3];
      this.a_Ae[var2 + 10].n_I = this.a_Ae[var2 + 10].a_AI[5];
      int var16 = cg.a_I(this.a_AAString[var11][1]);
      this.a_Ae[var2 + 10].h_I = var16;
      this.a_Ae[var2 + 10].a_AI[1] = this.a_Ae[var2 + 10].h_I * this.a_Ae[var2 + 10].h_I * this.a_Ae[var2 + 10].a_AI[0];
      this.a_Ae[var2 + 10].i_I = 0;
      if (this.a_AAString[var11][14].equals("-1")) {
        this.b_AAS[var2] = null;
      } else {
        this.b_AAS[var2] = cg.b_AS(this.a_AAString[var11][14], ",");
      }

      if (this.a_AAString[var11][15].equals("-1")) {
        this.c_AAS[var2] = null;
      } else {
        this.c_AAS[var2] = cg.b_AS(this.a_AAString[var11][15], ",");
      }

      if (this.a_AAString[var11][16].equals("-1")) {
        this.d_AAS[var2] = null;
      } else {
        this.d_AAS[var2] = cg.b_AS(this.a_AAString[var11][16], ",");
      }

      if (this.a_AAString[var11][17].equals("-1")) {
        this.e_AAS[var2] = null;
      } else {
        this.e_AAS[var2] = cg.b_AS(this.a_AAString[var11][17], ",");
      }

      if (this.a_AAString[var11][13].equals("-1")) {
        this.a_Ae[var2 + 10].a_AS = null;
      } else {
        String[] var17 = cg.b_AString(this.a_AAString[var11][13], "|");

        for (int var25 = 0; var25 < var17.length; var25++) {
          short[] var6 = cg.b_AS(var17[var25], ",");
          this.a_Ae[var2 + 10].a_AS = cg.a_AS(this.a_Ae[var2 + 10].a_AS, var6[0]);
          if (!cg.a_Z(this.i_AS, var6[0])) {
            this.a_Ae[var2 + 10].c_AS = cg.a_AS(this.a_Ae[var2 + 10].c_AS, var6[0]);
            this.a_Ae[var2 + 10].b_AS = cg.a_AS(this.a_Ae[var2 + 10].b_AS, var6[1]);
          }

          if (var6[0] == 7) {
            this.a_Ae[var2 + 10].j_Z = true;
          }
        }
      }

      if (this.a_AAString[var11][2] != null) {
        for (int var18 = 0; var18 < 4; var18++) {
          String var26;
          if ((var26 = this.a_AAString[var11][2].substring(var18, var18 + 1)).equals("a")) {
            this.a_Ae[var2 + 10].e_AB = cg.a_AB(this.a_Ae[var2 + 10].e_AB, 10);
          } else {
            this.a_Ae[var2 + 10].e_AB = cg.a_AB(this.a_Ae[var2 + 10].e_AB, cg.a_I(var26));
          }
        }
      }

      this.a_Ae[var2 + 10].d_AB = new byte[4];

      for (int var19 = 0; var19 < this.a_Ae[var2 + 10].e_AB.length; var19++) {
        this.a_Ae[var2 + 10].d_AB[var19] = this.a_Ae[var2 + 10].e_AB[var19];
      }

      this.a_Ae[var2 + 10].j_I = this.a_Ae[var2 + 10].a_AI[12] + a_I(this.a_Ae[var2 + 10]);
      this.a_Ae[var2 + 10].k_I = this.a_Ae[var2 + 10].a_AI[13];
      this.a_Ae[var2 + 10].l_I = this.a_Ae[var2 + 10].a_AI[14];
      this.a_Ae[var2 + 10].c_m.a_I = this.f_AS[var2];
      this.a_Ae[var2 + 10].c_m.b_I = this.g_AS[var2];
      this.a_Ae[var2 + 10].b_m.a_I = this.f_AS[var2];
      this.a_Ae[var2 + 10].b_m.b_I = this.g_AS[var2];
      this.a_Ae[var2 + 10].d_I = var7;
      this.a_Ae[var2 + 10].a_I = var2 + 10;
      this.h_AS[var2] = (short)var7;
      if (this.b_Z && this.a_AS != null && this.a_AS[var2] == 0) {
        this.a_Ae[var2 + 10].b_Z = false;
      }
    }
  }

  private static boolean a(e var0, e var1, int var2) {
    boolean var3 = false;
    int var4 = k.a(1, 100);
    int var5 = 0 + var0.l_I;
    int var8 = 0 + var1.l_I;
    int var6 = var5 * 100 / (var5 + var8) + var5 + var2;
    int var7 = var6 + 20;
    if (var7 < 20) {
      var7 = 20;
    }

    if (var4 <= var7) {
      var3 = true;
    }

    return var3;
  }

  public final void a_V(int var1, int var2) {
    this.a(this.a_Ae[var1], this.a_Ae[var2], this.a_Ae[var1].j_I, this.a_Ae[var2].k_I, 0, false);
  }

  private void a(e var1, e var2, int var3, int var4, int var5, boolean var6) {
    if (!a(var1, var2, var5) && !var6) {
      var2.o_Z = true;
    } else {
      var5 = 200 + a(var1.d_AB, var2.d_AB);
      var6 = k.b(0, 5);
      int var7 = var3;
      if (var1.m_Z) {
        var7 = var3 * 2;
      }

      int var8 = var7 * var5 / 100 - var4 + var6;
      var3 = k.b(0, 100);
      if (var2.l_Z && var3 < 15) {
        var8 /= 2;
      }

      if (var2.p_Z) {
        var8 /= 5;
      }

      if (var2.n_Z) {
        var8 /= 2;
      }

      if (var8 < 10) {
        var8 = 10;
      }

      var2.m_I -= var8;
    }
  }

  private static int a(byte[] var0, byte[] var1) {
    int var2 = 0;

    for (int var3 = 0; var3 < 4; var3++) {
      if (var0[var3] > 0) {
        if (var3 == 0) {
          if (var1[var3 + 1] > 0) {
            var2 += var0[var3] * var1[var3 + 1];
          }

          if (var1[3] > 0) {
            var2 -= var0[var3] * var1[3];
          }
        } else if (var3 == 1) {
          if (var1[var3 + 1] > 0) {
            var2 += var0[var3] * var1[var3 + 1];
          }

          if (var1[var3 - 1] > 0) {
            var2 -= var0[var3] * var1[var3 - 1];
          }
        } else if (var3 == 2) {
          if (var1[var3 + 1] > 0) {
            var2 += var0[var3] * var1[var3 + 1];
          }

          if (var1[var3 - 1] > 0) {
            var2 -= var0[var3] * var1[var3 - 1];
          }
        } else if (var3 == 3) {
          if (var1[0] > 0) {
            var2 += var0[var3] * var1[0];
          }

          if (var1[var3 - 1] > 0) {
            var2 -= var0[var3] * var1[var3 - 1];
          }
        }
      }
    }

    return var2;
  }

  private static int a_I(e var0) {
    int var1 = 0;
    if (var0.a_AS != null) {
      for (int var2 = 0; var2 < var0.a_AS.length; var2++) {
        if (var0.a_AS[var2] == 29 && var0.d_AB[0] > 0) {
          var1 += var0.d_AB[0] * var0.a_AI[12] / 10;
        }

        if (var0.a_AS[var2] == 30 && var0.d_AB[3] > 0) {
          var1 += var0.d_AB[3] * var0.a_AI[12] / 10;
        }

        if (var0.a_AS[var2] == 31 && var0.d_AB[2] > 0) {
          var1 += var0.d_AB[2] * var0.a_AI[12] / 10;
        }

        if (var0.a_AS[var2] == 32 && var0.d_AB[1] > 0) {
          var1 += var0.d_AB[1] * var0.a_AI[12] / 10;
        }
      }
    }

    return var1;
  }

  private int a_I(int var1, int var2) {
    int var3 = 0;

    for (int var4 = var1; var4 < var2; var4++) {
      if (this.a_Ae[var4] != null && !this.a_Ae[var4].f_Z) {
        var3++;
      }
    }

    return var3;
  }

  private byte[] a_AB(int var1, int var2) {
    byte[] var3 = null;

    for (int var4 = var1; var4 < var2; var4++) {
      if (this.a_Ae[var4] != null && !this.a_Ae[var4].f_Z) {
        var3 = cg.a_AB(var3, var4);
      }
    }

    return var3;
  }

  private void m() {
    this.a_AAS = new short[this.a_I(0, this.a_Ae.length)][2];
    int var1 = 0;

    for (int var2 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null && !this.a_Ae[var1].f_Z) {
        this.a_AAS[var2][0] = (short)var1;
        this.a_AAS[var2][1] = (short)this.a_Ae[var1].l_I;
        var2++;
      }
    }

    k.a(this.a_AAS, 1, 0);
  }

  public final void a_V(e var1) {
    var1.f_AB = new byte[2];
    var1.f_AB[0] = 0;
    var1.f_AB[1] = 0;
    var1.a_B = 0;
    var1.b_B = 0;
    var1.c_B = 0;
    var1.i_AB = new byte[2];
    var1.g_AB = new byte[2];
    var1.h_AB = new byte[2];

    for (int var2 = 1; var2 < this.e_AAI[var1.a_I].length; var2++) {
      this.e_AAI[var1.a_I][var2] = -1;
    }
  }

  private void n() {
    for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null) {
        this.a_V(this.a_Ae[var1]);
      }
    }
  }

  private void o() {
    for (int var1 = 0; var1 < this.e_AAI.length; var1++) {
      this.e_AAI[var1][0] = var1;

      for (int var2 = 1; var2 < this.e_AAI[var1].length; var2++) {
        this.e_AAI[var1][var2] = -1;
      }
    }
  }

  public final void c_V() {
    this.a_Z();
    this.a_Z = true;
  }

  public final void a(Graphics var1) {
    if (this.c_Z) {
      if (this.w_Image != null) {
        var1.drawImage(this.w_Image, at.a_AS[0] >> 1, at.a_AS[1] >> 1, 3);
      }

      if (this.d_Z) {
        if (this.A_I < 10) {
          if (da.a_da.a_S % 3 == 0) {
            this.A_I++;
            var1.setColor(16711680);
            var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
          }
        } else {
          this.A_I = 0;
          this.d_Z = false;
        }
      }
    } else {
      var1.setColor(0);
      var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
    }

    if (this.a_I == 28) {
      Graphics var35 = var1;
      var1.setColor(0);
      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
      if (this.c_bx != null) {
        var1.setColor(16777113);
        if (o.l_AString != null) {
          String[] var45 = cg.a(o.l_AString[o.f_B], at.a_AS[0] - 50);

          for (int var48 = 0; var48 < var45.length; var48++) {
            vh.drawString(var35, var45[var48], at.a_AS[0] / 2, at.a_AS[1] / 3 + var48 * cg.b_S, 17);
          }
        }

        this.c_bx.d_S = (short)(15 + (at.a_AS[0] - 30) * this.l_I / 100);
        this.c_bx.e_S = (short)(at.a_AS[1] - 15);
        this.c_bx.a_V(3);
        this.c_bx.a(var35);
        this.c_bx.a(true);
      }

      if (this.l_I < this.m_I) {
        this.l_I = this.m_I + 0;
        if (this.l_I > this.m_I) {
          this.l_I = this.m_I;
        }
      }

      if (this.a_Z && this.l_I >= 100) {
        if (this.b_AString != null) {
          this.d_B = 1;
          this.a(this.b_AString);
        } else {
          this.a_I = 0;
          this.g_I = 0;
          a_V(0);
        }

        this.c_bx = null;
        if (!at.a_String.equals("N73")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("N5800")
          && !at.a_String.equals("E62")
          && !at.a_String.equals("W958C")) {
          k.a_V();
        }

        this.m_I = 0;
        this.l_I = 0;
      }

      try {
        Thread.sleep(0L);
      } catch (InterruptedException var7) {
        var7.printStackTrace();
      }
    } else if (this.a_I == 27 && this.z_Image != null) {
      if (q_I < this.z_Image.getHeight()) {
        q_I += 5;
        var1.setClip(at.a_AS[0] - this.z_Image.getWidth() >> 1, at.a_AS[1] - this.z_Image.getHeight() >> 1, this.z_Image.getWidth(), this.z_Image.getHeight());
        var1.drawImage(this.z_Image, at.a_AS[0] - this.z_Image.getWidth() >> 1, (at.a_AS[1] + this.z_Image.getHeight() >> 1) - q_I, 20);
      } else {
        try {
          Thread.sleep(500L);
        } catch (InterruptedException var8) {
          var8.printStackTrace();
        }

        q_I = 0;
        if (this.b_AString != null) {
          this.d_B = 1;
          this.a(this.b_AString);
        } else {
          this.a_I = 0;
          this.g_I = 0;
        }
      }
    } else {
      for (int var2 = 10; var2 < 20; var2++) {
        if (this.a_Ae[var2] != null && this.a_Ae[var2].a_bx != null) {
          this.a_Ae[var2].a(var1);
        }
      }

      for (int var9 = 9; var9 >= 0; var9--) {
        if (this.a_Ae[var9] != null && this.a_Ae[var9].a_bx != null) {
          if (var9 == 0) {
            if (this.a_Ae[1] != null) {
              this.a_Ae[1].a(var1);
            } else {
              this.a_Ae[0].a(var1);
            }
          } else if (var9 == 1) {
            this.a_Ae[0].a(var1);
          } else {
            this.a_Ae[var9].a(var1);
          }
        }
      }

      if (this.a_Ae[this.j_I].f_I == 5) {
        if (this.a_Ae[this.j_I].b_bx != null) {
          if (!this.a_Ae[this.j_I].e_Z) {
            this.a_Ae[this.j_I].b_bx.a(at.a_AS[0] >> 1, at.a_AS[1] >> 1);
            this.a_Ae[this.j_I].b_bx.a(var1);
            if (this.a_Ae[this.j_I].b_bx.c() >= 5 && this.a_Ae[this.j_I].b_bx.c() <= 15) {
              short var10 = this.a_Ae[this.j_I].b_bx.a(0, 0, 0)[2];
              short var3 = this.a_Ae[this.j_I].b_bx.a(0, 0, 0)[3];
              cg.a(var1, d.a_AString[this.a_Ae[this.j_I].c_I - 1], (at.a_AS[0] >> 1) - var10 / 2, (at.a_AS[1] >> 1) - var3, 13695223, 1196461, 17);
            }

            if (this.a_Ae[this.j_I].b_bx.c() >= this.a_Ae[this.j_I].b_bx.a_I(0) - 1) {
              this.a_Ae[this.j_I].e_Z = true;
              this.a_Ae[this.j_I].b_bx.b(0);
              this.a_Ae[this.j_I].a(4, 0);
            } else if (da.a_da.a_S % 3 == 0) {
              this.a_Ae[this.j_I].b_bx.a(true);
            }
          }
        } else if (this.a_Ae[this.j_I].c_I != 0) {
          f(var1);
          cg.a(var1, d.a_AString[this.a_Ae[this.j_I].c_I - 1], at.a_AS[0] >> 1, 6 + cg.b_S, 13695223, 1196461, 33);
        }
      }

      if (this.a_Ae[this.j_I] != null && this.a_Ae[this.j_I].f_I == 10) {
        if (this.a_Ae[this.j_I].q_Z && this.a_Ae[this.j_I].b_AB != null && !this.a_Ae[this.j_I].c_Z) {
          this.a(var1, this.a_Ae[this.j_I].b_AB);
        }

        if (this.a_Ae[this.j_I].c_Z && !this.a_Ae[this.j_I].d_Z) {
          byte var12 = a_AAB[this.s_I][1];
          byte var26 = a_AAB[this.s_I][1];

          for (int var36 = 0; var36 < this.a_Ae[this.j_I].b_AB.length; var36++) {
            if (this.a_e(this.a_Ae[this.j_I].b_AB[var36]) != null) {
              if (this.a_e(this.a_Ae[this.j_I].b_AB[var36]).m_I != this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_AI[3]) {
                k.a(
                  var1,
                  this.a_e(this.a_Ae[this.j_I].b_AB[var36]).m_I - this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_AI[3],
                  this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.a_I - var26,
                  this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.b_I - this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_S + var12,
                  this.j_Image,
                  13,
                  13,
                  33,
                  0
                );
                if (this.s_I % 2 == 0) {
                  this.a_e(this.a_Ae[this.j_I].b_AB[var36]).b_m.a_I = this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.a_I - 4;
                } else {
                  this.a_e(this.a_Ae[this.j_I].b_AB[var36]).b_m.a_I = this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.a_I + 4;
                }
              }

              if (this.a_e(this.a_Ae[this.j_I].b_AB[var36]).m_I <= 0 && da.a_da.a_S % 2 == 0) {
                this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_bx.j_Z = !this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_bx.j_Z;
              }

              if (this.a_e(this.a_Ae[this.j_I].b_AB[var36]).o_Z) {
                if (this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_Image != null) {
                  var1.drawRegion(
                    this.a_e(this.a_Ae[this.j_I].b_AB[var36]).a_Image,
                    0,
                    0,
                    43,
                    19,
                    0,
                    this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.a_I,
                    this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.b_I - this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_S,
                    33
                  );
                } else {
                  var1.setColor(2153971);
                  vh.drawString(
                    var1,
                    "Né",
                    this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.a_I,
                    this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_m.b_I - this.a_e(this.a_Ae[this.j_I].b_AB[var36]).c_S,
                    33
                  );
                }
              }
            }
          }

          if (this.a_Ae[this.j_I].m_I != this.a_Ae[this.j_I].a_AI[3]) {
            k.a(
              var1,
              this.a_Ae[this.j_I].m_I - this.a_Ae[this.j_I].a_AI[3],
              this.a_Ae[this.j_I].c_m.a_I - var26,
              this.a_Ae[this.j_I].c_m.b_I - this.a_Ae[this.j_I].c_S + var12,
              this.j_Image,
              13,
              13,
              33,
              0
            );
          }

          if (this.s_I < a_AAB.length - 1) {
            if (da.a_da.a_S % 3 == 0) {
              this.s_I++;
            }
          } else {
            this.s_I = 0;
            this.a_Ae[this.j_I].d_Z = true;
            da.a_da.a_cd.a_bn.v();
            if (this.a_Ae[this.j_I].b_AB != null) {
              for (int var37 = 0; var37 < this.a_Ae[this.j_I].b_AB.length; var37++) {
                if (this.a_Ae[this.a_Ae[this.j_I].b_AB[var37]] != null && a_Z(this.a_Ae[this.a_Ae[this.j_I].b_AB[var37]])) {
                  this.a_Ae[this.a_Ae[this.j_I].b_AB[var37]].a_Z = true;
                }
              }
            }
          }
        }
      } else if (this.a_Ae[this.j_I] != null && this.a_Ae[this.j_I].f_I == 17 && this.a_Ae[this.j_I].q_Z) {
        if (this.a_Ae[this.j_I].q_Z && this.a_Ae[this.j_I].b_AB != null && !this.a_Ae[this.j_I].c_Z) {
          this.a(var1, this.a_Ae[this.j_I].b_AB);
        }

        if (this.a_Ae[this.j_I].c_Z) {
          byte var11 = a_AAB[this.s_I][1];
          byte var25 = a_AAB[this.s_I][1];

          for (int var4 = 0; var4 < this.a_Ae[this.j_I].b_AB.length; var4++) {
            if (this.a_e(this.a_Ae[this.j_I].b_AB[var4]) != null
              && this.a_e(this.a_Ae[this.j_I].b_AB[var4]).m_I != this.a_e(this.a_Ae[this.j_I].b_AB[var4]).a_AI[3]) {
              k.a(
                var1,
                this.a_e(this.a_Ae[this.j_I].b_AB[var4]).m_I - this.a_e(this.a_Ae[this.j_I].b_AB[var4]).a_AI[3],
                this.a_e(this.a_Ae[this.j_I].b_AB[var4]).c_m.a_I - var25,
                this.a_e(this.a_Ae[this.j_I].b_AB[var4]).c_m.b_I - this.a_e(this.a_Ae[this.j_I].b_AB[var4]).c_S + var11,
                this.j_Image,
                13,
                13,
                33,
                0
              );
            }
          }

          if (this.s_I < a_AAB.length - 1) {
            if (da.a_da.a_S % 3 == 0) {
              this.s_I++;
            }
          } else {
            if (!this.c_Z) {
              this.c_Z = true;
            }

            this.s_I = 0;
            this.a_Ae[this.j_I].q_Z = false;
            this.a_Ae[this.j_I].c_Z = false;
            this.a_Ae[this.j_I].f_I = 0;
            this.a_Ae[this.j_I].a(0, 0);
            da.a_da.a_cd.a_bn.v();
          }
        }
      }

      Graphics var27 = var1;
      bn var13 = this;

      for (int var38 = 0; var38 < var13.a_Ae.length; var38++) {
        if (var13.a_Ae[var38] != null) {
          if (var13.a_Ae[var38].c_bx != null && var13.a_Ae[var38].g_Z) {
            var13.a_Ae[var38].c_bx.a(var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I, var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I);
            var13.a_Ae[var38].c_bx.a(var27);
            if (da.a_da.a_S % 3 == 0) {
              var13.a_Ae[var38].c_bx.a(true);
            }

            if (var13.a_Ae[var38].c_bx.c() >= var13.a_Ae[var38].c_bx.a_I(0) - 1) {
              var13.a_Ae[var38].c_bx.b(0);
              var13.a_Ae[var38].g_Z = false;
            }
          }

          if (var13.a_Ae[var38].f_I == 2) {
            byte var5 = a_AAB[var13.s_I][1];
            byte var6 = a_AAB[var13.s_I][1];
            if (var13.a_e(var13.a_Ae[var38].b_I) != null) {
              if (var13.a_e(var13.a_Ae[var38].b_I).m_I != var13.a_e(var13.a_Ae[var38].b_I).a_AI[3]) {
                if (var13.a_e(var13.a_Ae[var38].b_I).m_I <= 0 && da.a_da.a_S % 2 == 0) {
                  var13.a_e(var13.a_Ae[var38].b_I).a_bx.j_Z = !var13.a_e(var13.a_Ae[var38].b_I).a_bx.j_Z;
                }

                k.a(
                  var27,
                  var13.a_e(var13.a_Ae[var38].b_I).m_I - var13.a_e(var13.a_Ae[var38].b_I).a_AI[3],
                  var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I - var6,
                  var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I - var13.a_e(var13.a_Ae[var38].b_I).c_S + var5,
                  var13.j_Image,
                  13,
                  13,
                  33,
                  0
                );
                if (da.a_da.a_S % 2 == 0) {
                  var13.a_e(var13.a_Ae[var38].b_I).b_m.a_I = var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I - 4;
                } else {
                  var13.a_e(var13.a_Ae[var38].b_I).b_m.a_I = var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I + 4;
                }
              }

              if (var13.a_e(var13.a_Ae[var38].b_I).o_Z) {
                if (var13.a_e(var13.a_Ae[var38].b_I).a_Image != null) {
                  var27.drawRegion(
                    var13.a_e(var13.a_Ae[var38].b_I).a_Image,
                    0,
                    0,
                    43,
                    19,
                    0,
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I,
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I - var13.a_e(var13.a_Ae[var38].b_I).c_S,
                    33
                  );
                } else {
                  var27.setColor(2153971);
                  vh.drawString(
                    var27, "Né", var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I, var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I - var13.a_e(var13.a_Ae[var38].b_I).c_S, 33
                  );
                }
              }

              if (var13.a_Ae[var38].m_Z) {
                if (var13.a_e(var13.a_Ae[var38].b_I).a_Image != null) {
                  var27.drawRegion(
                    var13.a_e(var13.a_Ae[var38].b_I).a_Image,
                    0,
                    var13.a_e(var13.a_Ae[var38].b_I).a_Image.getHeight() >> 1,
                    43,
                    var13.a_e(var13.a_Ae[var38].b_I).a_Image.getHeight() >> 1,
                    0,
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I,
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I - var13.a_e(var13.a_Ae[var38].b_I).c_S,
                    33
                  );
                } else {
                  var27.setColor(2153971);
                  vh.drawString(
                    var27,
                    "CRIT",
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.a_I,
                    var13.a_e(var13.a_Ae[var38].b_I).c_m.b_I - var13.a_e(var13.a_Ae[var38].b_I).c_S,
                    33
                  );
                }
              }
            }

            if (var13.s_I < a_AAB.length - 1) {
              if (da.a_da.a_S % 3 == 0) {
                var13.s_I++;
              }
            } else {
              var13.s_I = 0;
              if (var13.a_Ae[var38].m_Z) {
                var13.a_Ae[var38].m_Z = false;
                var13.d_Z = false;
              }

              var13.a_Ae[var38].d_Z = true;
              var13.a_Ae[var38].f_I = 15;
              if (var13.a_Ae[var38].a_I() == 1) {
                var13.a_Ae[var38].a_bx.j_Z = false;
              }

              var13.a_Ae[var38].a(2, 0);
              if (var13.a_e(var13.a_Ae[var38].b_I) != null) {
                var13.a_e(var13.a_Ae[var38].b_I).a_AI[3] = var13.a_e(var13.a_Ae[var38].b_I).m_I;
                if (var13.a_e(var13.a_Ae[var38].b_I).a_AI[3] <= 0) {
                  var13.a_e(var13.a_Ae[var38].b_I).a_AI[3] = 0;
                  var13.a_e(var13.a_Ae[var38].b_I).m_I = 0;
                  var13.a_e(var13.a_Ae[var38].b_I).f_Z = true;
                  var13.a_e(var13.a_Ae[var38].b_I).f_I = 128;
                  var13.a_e(var13.a_Ae[var38].b_I).a_bx.j_Z = false;
                }
              }
            }
          }
        }
      }

      if (this.a_Ae[this.j_I] != null && this.a_Ae[this.j_I].i_Z) {
        f(var1);
        cg.a(var1, "Hợp kích", at.a_AS[0] >> 1, 6 + cg.b_S, 13695223, 1196461, 33);
      }

      for (int var14 = 10; var14 < 20; var14++) {
        if (this.a_Ae[var14] != null && this.a_Ae[var14].a_bx != null && this.a_Ae[var14].a_AI[3] > 0 && this.a_Ae[var14].a_Z) {
          a(var1, this.a_Ae[var14]);
        }
      }

      for (int var15 = 9; var15 >= 0; var15--) {
        if (this.a_Ae[var15] != null && this.a_Ae[var15].a_bx != null && this.a_Ae[var15].a_AI[3] > 0 && this.a_Ae[var15].a_Z) {
          a(var1, this.a_Ae[var15]);
        }
      }

      if (this.a_I != 0) {
        i.a(0);
      }

      if (this.a_I == 0) {
        var27 = var1;
        var13 = this;
        if (!this.h_Z && a_AAB != null) {
          byte var39 = a_AAB[this.s_I][1];
          byte var46 = a_AAB[this.s_I][1];

          for (int var49 = 0; var49 < var13.a_Ae.length; var49++) {
            if (var13.a_Ae[var49] != null && var13.a_Ae[var49].m_I < var13.a_Ae[var49].a_AI[3]) {
              k.a(
                var27,
                var13.a_Ae[var49].m_I - var13.a_Ae[var49].a_AI[3],
                var13.a_Ae[var49].c_m.a_I - var46,
                var13.a_Ae[var49].c_m.b_I - var13.a_Ae[var49].c_S + var39,
                var13.j_Image,
                13,
                13,
                33,
                0
              );
              var27.setColor(16711680);
            }
          }

          if (var13.s_I < a_AAB.length - 1) {
            if (da.a_da.a_S % 3 == 0) {
              var13.s_I++;
            }
          } else {
            var13.h_Z = true;
            var13.s_I = 0;
            int var50 = 0;

            for (int var29 = 10; var29 < var13.a_Ae.length; var29++) {
              if (var13.a_Ae[var29] != null) {
                var13.a_Ae[var29].a_AI[3] = var13.a_Ae[var29].m_I;
                if (var13.a_Ae[var29].a_AI[3] <= 0 && var13.a_Ae[var29].a_bx != null && var13.a_Ae[var29].a_bx.j_Z) {
                  var13.a_Ae[var29].a_AI[3] = 0;
                  var13.a_Ae[var29].m_I = 0;
                }

                if (var13.a_Ae[var29].b_Z) {
                  var50 += var13.a_Ae[var29].a_AI[3];
                }
              }
            }

            if (var50 <= 0) {
              var13.h();
            } else {
              for (int var30 = 0; var30 < 10; var30++) {
                if (var13.a_Ae[var30] != null) {
                  if (var13.a_Ae[var30].m_I <= 0) {
                    var13.a_Ae[var30].a_AI[3] = 0;
                    var13.a_Ae[var30].m_I = 0;
                    var13.a_Ae[var30].a_bx.j_Z = false;
                    var13.a_Ae[var30].f_Z = true;
                    var13.a_Ae[var30].f_I = 128;
                  }

                  if (var13.a_Ae[var30].n_I < 0) {
                    var13.a_Ae[var30].a_AI[5] = 0;
                    var13.a_Ae[var30].n_I = 0;
                  }
                }
              }

              if (var13.a_Ae[0].a_AI[3] <= 0) {
                var13.e_V();
              }
            }
          }
        }

        if (this.a_Ae[this.i_I] != null) {
          this.b(var1, this.a_Ae[this.i_I]);
        }

        this.d(var1);
        var13 = this;
        if (this.i_Z) {
          if (this.e_bx != null) {
            if (this.g_I == 66) {
              if (this.i_I == 1) {
                this.e_bx.d_S = this.d_AS[0];
                this.e_bx.e_S = this.e_AS[0];
              } else {
                this.e_bx.d_S = this.d_AS[this.i_I];
                this.e_bx.e_S = this.e_AS[this.i_I];
              }
            } else {
              this.e_bx.d_S = this.d_AS[this.v_I];
              this.e_bx.e_S = this.e_AS[this.v_I];
            }

            k.a(var1, this.e_bx, this.e_bx.d_S, this.e_bx.e_S, true, 0);
            if (this.e_bx.c() >= this.e_bx.d() - 1) {
              if (this.g_I == 66) {
                this.t();
                if (this.a_Abl[this.i_I] != null) {
                  for (int var40 = 0; var40 < o.a_Acv.length; var40++) {
                    if (o.a_Acv[var40].b_I == var13.a_Abl[var13.i_I].c_S) {
                      o.a_Acv[var40].a_Z = false;
                      if (var13.a_Abl[var13.i_I].a_AI[3] > 0) {
                        o.a_Acv[var40].b_Z = false;
                      } else {
                        o.a_Acv[var40].b_Z = true;
                      }
                      break;
                    }
                  }
                }

                o.b(var13.a_Abl);
                var13.a_Acv = o.a_Acv();
                var13.a_Abl[var13.i_I] = null;
                var13.a_Ae[var13.i_I] = null;
                var13.m();
                var13.s();
              } else {
                this.q();
              }

              var13.i_Z = false;
            }
          } else {
            this.i_Z = false;
          }
        }

        if ((
            at.a_String.equals("E6")
              || at.a_String.equals("E680")
              || at.a_String.equals("W958C")
              || at.a_String.equals("S1")
              || at.a_String.equals("N95")
              || at.a_String.equals("N5800")
          )
          && (this.g_I == 1 || this.g_I == 21 || this.g_I == 41 || this.g_I == 51)) {
          if (this.C_Image != null) {
            cg.a(var1, this.C_Image, this.C_Image.getWidth() / 2, this.C_Image.getHeight(), 1, da.a_da.b_S - 15, da.a_da.c_S - 15, 3, 0);
          } else {
            this.C_Image = cc.a_Image("/pic/D23.gf", 1);
          }
        }
      } else if (this.a_I == 5) {
        this.b(var1);
      } else if (this.a_I == 6) {
        for (int var18 = 0; var18 < 20; var18++) {
          if (this.a_Ae[var18] != null && this.a_Ae[var18].f_I == 9 && this.a_Ae[var18].q_Z) {
            byte var31 = a_AAB[this.s_I][1];
            byte var41 = a_AAB[this.s_I][1];
            if (this.a_e(this.a_Ae[var18].b_I) != null && this.a_e(this.a_Ae[var18].b_I).m_I != this.a_e(this.a_Ae[var18].b_I).a_AI[3]) {
              k.a(
                var1,
                this.a_e(this.a_Ae[var18].b_I).m_I - this.a_e(this.a_Ae[var18].b_I).a_AI[3],
                this.a_e(this.a_Ae[var18].b_I).c_m.a_I - var41,
                this.a_e(this.a_Ae[var18].b_I).c_m.b_I - this.a_e(this.a_Ae[var18].b_I).c_S + var31,
                this.j_Image,
                13,
                13,
                33,
                0
              );
            }

            if (this.a_e(this.a_Ae[var18].b_I) != null && this.a_e(this.a_Ae[var18].b_I).n_I != this.a_e(this.a_Ae[var18].b_I).a_AI[5]) {
              k.a(
                var1,
                this.a_e(this.a_Ae[var18].b_I).n_I - this.a_e(this.a_Ae[var18].b_I).a_AI[5],
                this.a_e(this.a_Ae[var18].b_I).c_m.a_I - var41,
                this.a_e(this.a_Ae[var18].b_I).c_m.b_I - this.a_e(this.a_Ae[var18].b_I).c_S + var31 - 15,
                this.j_Image,
                13,
                13,
                33,
                1
              );
            }

            if (this.s_I < a_AAB.length - 1) {
              if (da.a_da.a_S % 3 == 0) {
                this.s_I++;
              }
            } else {
              this.s_I = 0;
              da.a_da.a_cd.a_bn.v();
              this.a_Ae[var18].q_Z = false;
              this.a_Ae[var18].a(0, 0);
              this.a_Ae[var18].f_I = 0;
            }
          }
        }
      } else if (this.a_I == 4) {
        this.a_cf.a(var1);
      } else if (this.a_I == 24) {
        int var32 = (at.a_AS[1] - 9 * (cg.b_S + 1)) / 2;
        int var19 = at.a_AS[0] - 16;
        int var20 = (at.a_AS[0] - var19) / 2;
        k.a(var1, var20, var32, at.a_AS[0] - var20 * 2, 9 * (cg.b_S + 1), 1317669, 15788720, 80, true);
        var1.setColor(16709092);
        var1.fillRect(var20 + 10, var32 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 5, at.a_AS[0] - 2 * (var20 + 10), 1);
        var1.setColor(3027509);
        var1.fillRect(var20 + 10, var32 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 6, at.a_AS[0] - 2 * (var20 + 10), 1);
        cg.a(var1, "Thắng trận", at.a_AS[0] >> 1, var32 + 4, 16706636, 9193495, 17);
        int var33 = var32 + cg.b_S * 3 / 2;
        var1.setColor(16777215);
        if (this.e_B == 0) {
          if (this.c_AString != null) {
            cg.a(var1, "Nhận vật phẩm", at.a_AS[0] / 2, var33 + 15, 9240572, 2314880, 17);
            if (this.c_AString.length > 6) {
              k.a(var1, null, null, at.a_AS[0] - 25, cg.b_S + 1 + var33 + 5, 3 * (cg.b_S + 1), this.c_AString.length, 3, a_S);
            }

            for (short var42 = 0; var42 < 6; var42++) {
              if (a_S + var42 < this.c_AString.length) {
                var1.setColor(16777215);
                vh.drawString(var1, this.c_AString[var42 + a_S], at.a_AS[0] / 2, var33 + 12 + (cg.b_S + 1) * (var42 + 1), 17);
              }
            }
          }
        } else if (this.e_B == 2) {
          bl var43 = (bl)this.a_Vector.elementAt(this.o_I);
          int var47 = 0;

          for (int var21 = 0; var21 < this.a_Abl.length; var21++) {
            if (this.a_Abl[var21] != null && var43.a_String.equals(this.a_Abl[var21].a_String)) {
              var47 = var21;
              break;
            }
          }

          cg.a(var1, var43.a_String + " lên " + this.a_Abl[var47].a_AI[0] + " cấp", at.a_AS[0] / 2, var33 + 15, 9240572, 2314880, 17);
          if (var47 == 0) {
            cg.a(var1, "Thêm 10 điểm", at.a_AS[0] / 2, var33 + 20 + cg.b_S, 9240572, 2314880, 17);
            cg.a(var1, "Hãy phân bổ hợp lý", at.a_AS[0] / 2, var33 + 20 + (cg.b_S << 1), 9240572, 2314880, 17);
          } else {
            int var22 = var33 + 15 + cg.b_S;
            this.a(var1, 0, var43.a_AI[10], this.a_Abl[var47].a_AI[10], var22 - 10 + 8 + cg.b_S + 1);
            this.a(var1, 1, var43.a_AI[11], this.a_Abl[var47].a_AI[11], var22 - 10 + 8 + (cg.b_S + 1 << 1));
            this.a(var1, 3, var43.a_AI[12], this.a_Abl[var47].a_AI[12], var22 - 10 + 8 + (cg.b_S + 1) * 3);
            this.a(var1, 4, var43.a_AI[13], this.a_Abl[var47].a_AI[13], var22 - 10 + 8 + (cg.b_S + 1 << 2));
            this.a(var1, 5, var43.a_AI[14], this.a_Abl[var47].a_AI[14], var22 - 10 + 8 + (cg.b_S + 1) * 5);
          }
        } else if (this.e_B == 1) {
          cg.a(var1, "Nhận EXP", at.a_AS[0] / 2, var33 + 15, 9240572, 2314880, 17);

          for (short var44 = 0; var44 < 3; var44++) {
            if (this.d_AString != null && var44 < this.d_AString.length) {
              var1.setColor(16777215);
              vh.drawString(var1, this.d_AString[var44], at.a_AS[0] / 2, var33 + 12 + (cg.b_S + 1) * (var44 + 1), 17);
            }
          }
        }
      } else if (this.a_I == 25) {
        int var34 = (at.a_AS[1] - 9 * (cg.b_S + 1)) / 2;
        int var23 = (at.a_AS[0] - 190) / 2;
        k.a(var1, var23, var34, at.a_AS[0] - var23 * 2, 9 * (cg.b_S + 1), 1317669, 15788720, 80, true);
        var1.setColor(16709092);
        var1.fillRect(var23 + 10, var34 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 5, at.a_AS[0] - 2 * (var23 + 10), 1);
        var1.setColor(3027509);
        var1.fillRect(var23 + 10, var34 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 6, at.a_AS[0] - 2 * (var23 + 10), 1);
        cg.a(var1, "Thua trận", at.a_AS[0] >> 1, var34 + 4, 16706636, 9193495, 17);
        var1.setColor(this.p_I);
        if (this.e_AString != null && this.f_B < this.e_AString.length) {
          cg.a(var1, this.e_AString[this.f_B], at.a_AS[0] / 2, var34 + cg.b_S * 3, this.p_I, this.r_I, 33);
        }
      } else if (this.a_I == 8 && this.a_Ae[this.j_I].f_I == 16) {
        if (this.a(this.a_Ae[this.j_I].a_AB) && this.t_I >= this.a_Ae[this.j_I].a_AB.length) {
          this.a_Ae[this.j_I].q_Z = true;
          if (this.a_Ae[this.j_I].a_I() == 0) {
            this.a_Ae[this.j_I].a_V();
          } else {
            this.a_Ae[this.j_I].a_bx.j_Z = false;
          }

          this.a_Ae[this.j_I].a(2, 0);
          this.a_Ae[this.j_I].h_Z = false;
          this.a_Ae[this.j_I].f_I = 2;

          for (int var24 = 0; var24 < this.a_Ae[this.j_I].a_AB.length; var24++) {
            this.a_e(this.a_Ae[this.j_I].a_AB[var24]).q_Z = true;
            if (this.a_e(this.a_Ae[this.j_I].a_AB[var24]).a_I() == 0) {
              this.a_e(this.a_Ae[this.j_I].a_AB[var24]).a_V();
            } else {
              this.a_e(this.a_Ae[this.j_I].a_AB[var24]).a_bx.j_Z = false;
            }

            this.a_e(this.a_Ae[this.j_I].a_AB[var24]).h_Z = false;
            this.a_e(this.a_Ae[this.j_I].a_AB[var24]).i_Z = false;
            this.a_e(this.a_Ae[this.j_I].a_AB[var24]).f_I = 15;
            this.a_e(this.a_Ae[this.j_I].a_AB[var24]).a(2, 0);
          }

          this.a_Ae[this.j_I].i_Z = false;
          this.a_I = 3;
          this.t_I = 0;
          this.a_Ae[this.j_I].a_AB = null;
        } else if (this.a_e(this.a_Ae[this.j_I].a_AB[this.t_I]).f_I == 13) {
          if (this.a_e(this.a_Ae[this.j_I].a_AB[this.t_I]).a_bx.a() == 0) {
            this.a_e(this.a_Ae[this.j_I].a_AB[this.t_I]).a(3, 0);
          }
        } else if (this.a_e(this.a_Ae[this.j_I].a_AB[this.t_I]).f_I == 16 && this.t_I < this.a_Ae[this.j_I].a_AB.length) {
          this.t_I++;
        }
      }

      if (ct.b_B != 1 && this.c_Z) {
        this.c(var1);
      }
    }
  }

  private void b(Graphics var1) {
    int var5;
    int var3 = (var5 = (at.a_AS[1] - 9 * (cg.b_S + 1)) / 2) + 6 * (cg.b_S + 1) - 10;
    int var2;
    int var4 = (var2 = (at.a_AS[0] - 160) / 2) + 8;
    k.a(var1, var2, var5, 160, 9 * (cg.b_S + 2), 1317669, 15788720, 80, true);
    var1.setColor(16709092);
    var1.fillRect(var2 + 10, var5 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 5, at.a_AS[0] - 2 * (var2 + 10), 1);
    var1.setColor(3027509);
    var1.fillRect(var2 + 10, var5 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 6, at.a_AS[0] - 2 * (var2 + 10), 1);
    var1.setColor(16711680);
    cg.a(var1, "Thêm vào đồ giám thành công", at.a_AS[0] >> 1, var5 + 4, 16706636, 9193495, 17);
    k.a(var1, this.f_bx, var2 + 80, var5 - 10 + 5 * (cg.b_S + 1), true, 0);
    cg.a(var1, this.a_Ae[this.u_I].a_String, at.a_AS[0] / 2, var3, 9240572, 2314880, 33);
    byte[] var6 = cg.b_AB(this.a_AAString[this.a_Ae[this.u_I].d_I - 1][18], ",");
    cg.b(var1, "Gen đặc trưng", var4 - 6, var3, 16706636, 9128217, 20);
    if (o.f_AString == null) {
      o.d();
    }

    var2 = o.d_I(var6[0]);
    var5 = vh.stringWidth(cg.a_Font, "Gen đặc trưng");
    var1.setColor(16777215);
    vh.drawString(var1, o.f_AString[var2], var4 - 6 + var5, var3, 20);
    if (this.a_AB != null) {
      if (this.g_Image == null) {
        this.g_Image = cc.a("/pic/D13.gf");
      }

      var3 += cg.b_S + 1;
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, 0, var4, var3, 20, 0);
      var3 -= 3;
      a(var1, this.y_Image, var4 + 30, var3 + 8, this.a_AB[0], 3);
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, 3, var4 + 80, var3, 20, 0);
      a(var1, this.y_Image, var4 + 30 + 80, var3 + 8, this.a_AB[2], 3);
      var3 += 16;
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, 1, var4, var3, 20, 0);
      a(var1, this.y_Image, var4 + 30, var3 + 8, this.a_AB[1], 3);
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, 4, var4 + 80, var3, 20, 0);
      a(var1, this.y_Image, var4 + 30 + 80, var3 + 8, this.a_AB[3], 3);
      var3 += 16;
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, 5, var4, var3, 20, 0);
      a(var1, this.y_Image, var4 + 30, var3 + 8, this.a_AB[4], 3);
      this.a(var1, var4 + 80, var3 - 2 + 8, this.a_Ae[this.u_I].e_AB);
    }
  }

  private static void a(Graphics var0, Image var1, int var2, int var3, int var4, int var5) {
    var5 = var4 / 2;

    for (int var6 = 0; var5 > 0 && var6 < var5; var6++) {
      var0.drawRegion(var1, 0, 0, var1.getWidth() / 2, var1.getHeight(), 0, var2 + var6 * var1.getWidth() / 2, var3, 3);
    }

    if (var4 % 2 != 0) {
      var0.drawRegion(var1, var1.getWidth() / 2, 0, var1.getWidth() / 2, var1.getHeight(), 0, var2 + var5 * var1.getWidth() / 2, var3, 3);
    }
  }

  private void a(Graphics var1, int var2, int var3, int var4, int var5) {
    cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, var2, at.a_AS[0] / 2 - 50 - 15, var5, 24, 0);
    cg.a(var1, var3, at.a_AS[0] / 2 - 15, var5 + 5, this.b_Image, 6, 8, 24);
    var1.drawRegion(this.h_Image, 0, 0, 7, 7, 0, at.a_AS[0] / 2, var5 + 5, 17);
    cg.a(var1, var4, at.a_AS[0] / 2 + 15, var5 + 5, this.b_Image, 6, 8, 20);
  }

  private static void a(Graphics var0, e var1) {
    int var2 = var1.b_m.a_I;
    int var3 = var1.b_m.b_I - var1.c_S / 2;
    if (var1.f_AB[0] > 0) {
      k.a(var0, var1.e_bx, var2, var3, true, 0);
    } else if (var1.b_B > 0) {
      k.a(var0, var1.f_bx, var2, var3, true, 0);
    } else {
      if (var1.a_B > 0) {
        k.a(var0, var1.g_bx, var2, var3, true, 0);
      }
    }
  }

  private void a(Graphics var1, byte[] var2) {
    if (this.a_Ae[this.j_I].a_Abx == null) {
      this.a_Ae[this.j_I].c_Z = true;
    } else {
      if (!this.a_Ae[this.j_I].c_Z && var2.length != this.a_Ae[this.j_I].a_Abx.length) {
        System.out.println("Có lỗi khi khởi tạo new");
      }

      for (int var3 = 0; var3 < var2.length; var3++) {
        this.a_Ae[this.j_I].a_Abx[var3].a(var1);
        if (this.a_Ae[this.j_I].a_Abx[var3].c() >= this.a_Ae[this.j_I].a_Abx[var3].a_I(0) - 1) {
          this.a_Ae[this.j_I].c_Z = true;
          this.a_Ae[this.j_I].a_Abx[var3].b(0);
          byte var5 = var2[var3];
          this.a_Ae[var5].a_bx.j_Z = true;
        } else if (da.a_da.a_S % 3 == 0) {
          this.a_Ae[this.j_I].a_Abx[var3].a(true);
        }
      }
    }
  }

  private void c(Graphics var1) {
    byte var2 = 5;
    if (at.a_AS[0] > 240) {
      var2 = 10;
    }

    if (this.g_AAS == null) {
      this.g_AAS = new short[var2][3];

      for (int var3 = 0; var3 < this.g_AAS.length; var3++) {
        this.g_AAS[var3][0] = (short)k.b(5, at.a_AS[0] - 10);
        this.g_AAS[var3][1] = (short)k.b(at.a_AS[1] >> 2, at.a_AS[1] - 20);
        this.g_AAS[var3][2] = (short)k.b(0, 2);
      }
    }

    for (int var4 = 0; this.g_AAS != null && var4 < this.g_AAS.length; var4++) {
      if (this.g_AAS[var4][2] == 1) {
        var1.drawImage(this.A_Image, this.g_AAS[var4][0], this.g_AAS[var4][1], 3);
      } else if (this.g_AAS[var4][2] == 2) {
        var1.drawImage(this.B_Image, this.g_AAS[var4][0], this.g_AAS[var4][1], 3);
      }

      this.g_AAS[var4][1]--;
      if (this.g_AAS[var4][1] < 5) {
        this.g_AAS[var4][0] = (short)k.b(5, at.a_AS[0] - 10);
        this.g_AAS[var4][1] = (short)k.b(at.a_AS[1] / 4, (at.a_AS[1] << 1) / 3 - 10);
        this.g_AAS[var4][2] = (short)k.b(0, 2);
      }
    }
  }

  private void b(Graphics var1, e var2) {
    int[][] var3 = new int[][]{{var2.a_AI[3], var2.a_AI[10]}, {var2.a_AI[5], var2.a_AI[11]}, {var2.a_AI[1], var2.a_AI[2]}};
    var1.drawImage(this.a_AImage[0], 0, at.a_AS[1], 36);
    int var4 = 0;
    if (var2.a_I == 0) {
      if (this.a_bx != null) {
        this.a_bx.a(0, at.a_AS[1] - 5);
        this.a_bx.a(var1);
        if (this.e_Image != null) {
          cg.a(var1, this.e_Image, this.e_Image.getWidth(), this.e_Image.getHeight() >> 1, var2.d_I - 1, 0, at.a_AS[1] - 5, 36, 0);
        } else {
          cg.b(var1, var2.a_String, 0, at.a_AS[1] - 5, 9502459, 540331, 36);
        }

        short[] var6;
        if ((var6 = this.a_bx.a(0, 0, 0)) != null) {
          var4 = 0 + var6[2];
        } else {
          var4 += 80;
        }
      } else {
        var4 += 30;
      }
    } else {
      var1.drawImage(this.i_Image, 0, at.a_AS[1] - cg.b_S, 36);
      cg.b(var1, var2.a_String, 5, at.a_AS[1] - 5, 9502459, 540331, 36);
      var4 += 80;
    }

    int var5 = at.a_AS[1] - 6 - 39;

    for (int var11 = 0; var11 < 3; var11++) {
      cg.a(var1, this.a_AImage[1], this.a_AImage[1].getWidth(), this.a_AImage[1].getHeight() / 3, var11, var4 + (var11 << 1), var5 + var11 * 13, 20, 0);
      cg.a(
        var1,
        this.a_AImage[2],
        var3[var11][0] * this.a_AImage[2].getWidth() / var3[var11][1],
        this.a_AImage[2].getHeight() / 3,
        var11,
        var4 + this.a_AImage[1].getWidth() + 2 + (var11 << 1),
        var5 + 3 + var11 * 13,
        20,
        0
      );
      a(
        var1,
        var4 + this.a_AImage[1].getWidth() + 2 + (var11 << 1),
        var5 + 3 + var11 * 13,
        this.a_AImage[2].getWidth() - 2,
        16773874,
        6509908,
        this.a_AImage[2].getHeight() / 3
      );
      cg.a(
        var1,
        var3[var11][0],
        var4 + 10 + this.a_AImage[1].getWidth() + 2 + (var11 << 1),
        var5 + var11 * 13,
        this.a_Image,
        this.a_Image.getWidth() / 14,
        this.a_Image.getHeight(),
        20
      );
    }

    if (this.c_Image != null) {
      int var10002 = var4 + 18 + this.a_AImage[1].getWidth() + this.a_AImage[2].getWidth();
      int var10003 = var5 + 10;
      byte[] var10 = var2.e_AB;
      var4 = var10003;
      int var7 = var10002;
      if (var10 != null) {
        if (var10[0] > 0) {
          var1.drawRegion(
            this.c_Image,
            this.c_Image.getWidth() / 2,
            this.c_Image.getHeight() / 2,
            this.c_Image.getWidth() / 2,
            this.c_Image.getHeight() / 2,
            0,
            var7,
            var4,
            3
          );
          cg.a(var1, var10[0], var7 + this.c_Image.getWidth() / 4, var4 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
          var7 += 16;
          var4 += 16;
        }

        if (var10[1] > 0) {
          var1.drawRegion(this.c_Image, this.c_Image.getWidth() / 2, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var4, 3);
          cg.a(var1, var10[1], var7 + this.c_Image.getWidth() / 4, var4 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
          var7 += 16;
          var4 += 16;
        }

        if (var10[2] > 0) {
          var1.drawRegion(this.c_Image, 0, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var4, 3);
          cg.a(var1, var10[2], var7 + this.c_Image.getWidth() / 4, var4 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
          var7 += 16;
          var4 += 16;
        }

        if (var10[3] > 0) {
          var1.drawRegion(this.c_Image, 0, this.c_Image.getHeight() / 2, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var4, 3);
          cg.a(var1, var10[3], var7 + this.c_Image.getWidth() / 4, var4 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
        }
      }
    } else {
      System.out.println("Ảnh Địa Thủy Hỏa Phong trống");
    }
  }

  private static void a(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
    var0.setColor(var4);
    var0.drawRect(var1, var2, var3, var6 - 2);
    var0.setColor(var5);
    var0.drawRoundRect(var1 - 1, var2 - 1, var3 + 2, var6, 4, 4);
  }

  private void d(Graphics var1) {
    if (this.g_I == 0) {
      Graphics var2 = var1;
      bn var6 = this;
      f(var1);
      if (this.a_Ae[this.i_I] != null) {
        if (this.a_Ae[this.i_I].a_AI[3] > 0) {
          String var3 = this.a_Ae[this.i_I].a_String + " Bắt đầu tấn công";
          if (vh.stringWidth(cg.a_Font, var3) <= at.a_AS[0]) {
            cg.a(var1, var3, at.a_AS[0] / 2, 6 + cg.b_S, 15987162, 5971280, 33);
          } else {
            k.a(var1, var3, 2);
          }

          int var10 = cg.b_S + 11;
          int var4 = 0;
          if (this.a_Ae[this.i_I].c_B > 0) {
            var1.drawImage(this.u_Image, 2, var10, 20);
            cg.a(var1, this.a_Ae[this.i_I].c_B, 6, var10 + 4, this.a_Image, 6, 7, 20);
            var4 = 1;
          }

          if (this.a_Ae[this.i_I].g_AB[0] > 0) {
            var1.drawImage(this.v_Image, 2 + var4 * 20, var10, 20);
            cg.a(var1, this.a_Ae[this.i_I].g_AB[0], 6 + var4 * 21, var10 + 4, this.a_Image, 6, 7, 20);
            var4++;
          }

          if (this.a_Ae[this.i_I].h_AB[0] > 0) {
            var1.drawImage(this.v_Image, 2 + var4 * 20, var10, 20);
            cg.a(var1, this.a_Ae[this.i_I].h_AB[0], 6 + var4 * 21, var10 + 4, this.a_Image, 6, 7, 20);
          }
        } else {
          cg.a(var1, this.a_Ae[this.i_I].a_String + " tử trận", at.a_AS[0] / 2, 6 + cg.b_S, this.p_I, this.r_I, 33);
        }
      }

      this.b_bx.a(var1, at.a_AS[0] / 2, at.a_AS[1] / 2);
      if (this.i_I != 0) {
        for (int var12 = 0; var12 < 4; var12++) {
          if (!var6.f_Z && !var6.g_Z) {
            if (var12 != 0) {
              cg.a(
                var2,
                var6.l_Image,
                var6.l_Image.getWidth() / 11,
                var6.l_Image.getHeight(),
                (var12 + var6.f_I) % 11,
                var6.c_AAI[var12][0] + at.a_AS[0] / 2,
                var6.c_AAI[var12][1] + at.a_AS[1] / 2,
                20,
                0
              );
            } else {
              cg.a(
                var2,
                var6.k_Image,
                var6.k_Image.getWidth() / 11,
                var6.k_Image.getHeight(),
                (var12 + var6.f_I) % 11,
                var6.c_AAI[var12][0] + at.a_AS[0] / 2,
                var6.c_AAI[var12][1] + at.a_AS[1] / 2,
                20,
                0
              );
            }

            cg.a(var2, var6.m_Image, var6.m_Image.getWidth(), var6.m_Image.getHeight() / 11, var6.f_I, 14 + at.a_AS[0] / 2, -63 + at.a_AS[1] / 2, 20, 0);
          } else {
            if (var6.g_Z) {
              cg.a(
                var2,
                var6.l_Image,
                var6.l_Image.getWidth() / 11,
                var6.l_Image.getHeight(),
                (var12 + 1 + var6.f_I) % 11,
                var6.d_AAI[var12][0] + at.a_AS[0] / 2,
                var6.d_AAI[var12][1] + at.a_AS[1] / 2,
                20,
                0
              );
            }

            if (var6.f_Z) {
              cg.a(
                var2,
                var6.l_Image,
                var6.l_Image.getWidth() / 11,
                var6.l_Image.getHeight(),
                (var12 + var6.f_I) % 11,
                var6.d_AAI[var12][0] + at.a_AS[0] / 2,
                var6.d_AAI[var12][1] + at.a_AS[1] / 2,
                20,
                0
              );
            }
          }

          if (var6.g_Z) {
            var6.g_Z = false;
          }

          if (var6.f_Z) {
            var6.f_Z = false;
          }
        }

        return;
      }

      for (int var11 = 0; var11 < 7; var11++) {
        if (!var6.f_Z && !var6.g_Z) {
          if (var11 != 0) {
            cg.a(
              var2,
              var6.l_Image,
              var6.l_Image.getWidth() / 11,
              var6.l_Image.getHeight(),
              (var11 + var6.f_I) % 7,
              var6.a_AAI[var11][0] + at.a_AS[0] / 2,
              var6.a_AAI[var11][1] + at.a_AS[1] / 2,
              20,
              0
            );
          } else {
            cg.a(
              var2,
              var6.k_Image,
              var6.k_Image.getWidth() / 11,
              var6.k_Image.getHeight(),
              (var11 + var6.f_I) % 7,
              var6.a_AAI[var11][0] + at.a_AS[0] / 2,
              var6.a_AAI[var11][1] + at.a_AS[1] / 2,
              20,
              0
            );
          }

          cg.a(var2, var6.m_Image, var6.m_Image.getWidth(), var6.m_Image.getHeight() / 11, var6.f_I, 14 + at.a_AS[0] / 2, -63 + at.a_AS[1] / 2, 20, 0);
        } else {
          if (var6.g_Z) {
            cg.a(
              var2,
              var6.l_Image,
              var6.l_Image.getWidth() / 11,
              var6.l_Image.getHeight(),
              (var11 + 1 + var6.f_I) % 7,
              var6.b_AAI[var11][0] + at.a_AS[0] / 2,
              var6.b_AAI[var11][1] + at.a_AS[1] / 2,
              20,
              0
            );
          }

          if (var6.f_Z) {
            cg.a(
              var2,
              var6.l_Image,
              var6.l_Image.getWidth() / 11,
              var6.l_Image.getHeight(),
              (var11 + var6.f_I) % 7,
              var6.b_AAI[var11][0] + at.a_AS[0] / 2,
              var6.b_AAI[var11][1] + at.a_AS[1] / 2,
              20,
              0
            );
          }
        }
      }

      if (var6.g_Z) {
        var6.g_Z = false;
      }

      if (!var6.f_Z) {
        return;
      }

      var6.f_Z = false;
    } else {
      if (this.g_I == 1 || this.g_I == 21) {
        this.a(var1, this.c_I);
        return;
      }

      if (this.g_I == 51) {
        if (!this.i_Z) {
          var1.drawRegion(this.x_Image, 15, 0, 15, 15, 0, this.d_AS[this.v_I], this.e_AS[this.v_I] - 20 + this.k_I, 33);
          return;
        }
      } else if (this.g_I == 66) {
        f(var1);
        if (!this.i_Z) {
          var1.drawRegion(this.x_Image, 15, 0, 15, 15, 0, this.a_Ae[this.i_I].c_m.a_I, this.a_Ae[this.i_I].c_m.b_I - this.a_Ae[this.i_I].c_S + this.k_I, 33);
          return;
        }
      } else {
        if (this.g_I == 41) {
          byte[] var9 = this.a_AB(0, 10);
          this.a(var1, var9[this.n_I]);
          return;
        }

        if (this.g_I == 2) {
          this.e(var1);
          if (this.b_ab != null) {
            this.b_ab.a(var1);
            String[] var15 = cg.a(d.c_AString[this.b_ab.a_I(this.b_ab.b_I())], this.a_AI[2] - 10);
            k.a(var1, var15, this.a_AI[0] + 5, this.a_AI[1] + this.a_AI[3] + this.w_I, this.a_AI[2] - 14, 10, this.x_I / cg.b_S, 13629944, null, false);
          }

          return;
        }

        if (this.g_I == 4) {
          this.e(var1);
          if (this.a_ab != null) {
            this.a_ab.a(var1);
            if (this.a_dc != null) {
              this.a_dc.a();
            } else {
              this.a_dc = new dc();
            }

            this.a_dc.a(o.g_AString[this.a_ab.a_I(this.a_ab.b_I())], this.z_I - 10, null, 13629944);
            this.a_dc.a(this.a_AI[0] + 5, this.a_AI[1] + this.a_AI[3] + this.w_I, this.z_I - 10, this.x_I, this.x_I / cg.b_S);
            this.a_dc.a(var1, this.a_dc, 1, true, false);
          }

          return;
        }

        if (this.g_I == 3) {
          this.e(var1);
          if (this.c_ab != null) {
            this.c_ab.a(var1);
            if (this.a_dc != null) {
              this.a_dc.a();
            } else {
              this.a_dc = new dc();
            }

            var1.setColor(13629944);
            vh.drawString(
              var1,
              "Cấp này cần MP : " + d.a_AAS[this.b_ab.a_I(this.b_ab.b_I())][this.c_ab.b_I()],
              at.a_AS[0] >> 1,
              this.a_AI[1] + this.a_AI[3] + this.w_I + (this.x_I - cg.b_S >> 1),
              17
            );
            return;
          }
        } else if (this.g_I == 5 && this.d_ab != null) {
          this.e(var1);
          this.d_ab.a(var1);
          k.a(var1, this.d_bx, this.a_AI[0] + (this.a_AI[2] >> 2), this.a_AI[1] + this.a_AI[3] + this.w_I + this.x_I - 10, true, 0);
          int var13 = this.a_AI[1] + this.a_AI[3] + this.w_I;
          int var16 = this.x_I / 4;
          int var7 = this.a_AI[0] + this.a_AI[2] / 2;
          int var5 = this.a_AI[2] / 8;
          if (this.a_Acv[this.d_ab.b_I()].a_AB != null && this.c_Image != null) {
            if (this.a_Acv[this.d_ab.b_I()].a_AB[0] > 0) {
              var1.drawRegion(
                this.c_Image,
                this.c_Image.getWidth() / 2,
                this.c_Image.getHeight() / 2,
                this.c_Image.getWidth() / 2,
                this.c_Image.getHeight() / 2,
                0,
                var7,
                var13 + var16 / 2,
                6
              );
              cg.a(var1, this.a_Acv[this.d_ab.b_I()].a_AB[0], var7 + this.c_Image.getWidth() / 2, var13 + var16 / 2, this.n_Image, 6, 7, 6);
              var7 += var5;
            }

            if (this.a_Acv[this.d_ab.b_I()].a_AB[1] > 0) {
              var1.drawRegion(
                this.c_Image, this.c_Image.getWidth() / 2, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var13 + var16 / 2, 6
              );
              cg.a(var1, this.a_Acv[this.d_ab.b_I()].a_AB[1], var7 + this.c_Image.getWidth() / 2, var13 + var16 / 2, this.n_Image, 6, 7, 6);
              var7 += var5;
            }

            if (this.a_Acv[this.d_ab.b_I()].a_AB[2] > 0) {
              var1.drawRegion(this.c_Image, 0, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var13 + var16 / 2, 6);
              cg.a(var1, this.a_Acv[this.d_ab.b_I()].a_AB[2], var7 + this.c_Image.getWidth() / 2, var13 + var16 / 2, this.n_Image, 6, 7, 6);
              var7 += var5;
            }

            if (this.a_Acv[this.d_ab.b_I()].a_AB[3] > 0) {
              var1.drawRegion(
                this.c_Image, 0, this.c_Image.getHeight() / 2, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var7, var13 + var16 / 2, 6
              );
              cg.a(var1, this.a_Acv[this.d_ab.b_I()].a_AB[3], var7 + this.c_Image.getWidth() / 2, var13 + var16 / 2, this.n_Image, 6, 7, 6);
            }
          }

          var7 = this.a_AI[0] + this.a_AI[2] / 2 - 4;
          var13 = var13 + var16 + 10;
          int[][] var17;
          (var17 = new int[3][2])[0][0] = this.a_Acv[this.d_ab.b_I()].a_AI[1];
          var17[0][1] = this.a_Acv[this.d_ab.b_I()].a_AI[2];
          var17[1][0] = this.a_Acv[this.d_ab.b_I()].a_AI[4];
          var17[1][1] = this.a_Acv[this.d_ab.b_I()].a_AI[5];
          var17[2][1] = this.a_Acv[this.d_ab.b_I()].a_AI[13];
          var17[2][1] = this.a_Acv[this.d_ab.b_I()].a_AI[14];
          this.a(var1, var7, var13, var16, var17);
        }
      }
    }
  }

  private void a(Graphics var1, int var2, int var3, int var4, int[][] var5) {
    for (int var6 = 0; var6 < 3; var6++) {
      int var10 = var3 + var4 * var6;
      cg.a(var1, this.d_Image, this.d_Image.getWidth() * var5[var6][0] / var5[var6][1], this.d_Image.getHeight() >> 2, var6, var2, var10, 20, 0);
      a(var1, var2, var10, this.d_Image.getWidth(), 4664337, 15988459, this.d_Image.getHeight() >> 2);
      int var10002 = var5[var6][0];
      int var10003 = var5[var6][1];
      int var10004 = var2 + this.g_Image.getWidth() * 5 / 2;
      int var17 = var10 - 4;
      int var16 = var10004;
      int var15 = var10003;
      int var14 = var10002;
      var1.drawRegion(this.n_Image, 60, 0, 6, 7, 0, var16, var17, 17);
      cg.a(var1, var14, var16 - 6, var17, this.n_Image, 6, 7, 24);
      cg.a(var1, var15, var16 + 6, var17, this.n_Image, 6, 7, 20);
      cg.a(var1, this.g_Image, this.g_Image.getWidth(), this.g_Image.getHeight() / 10, var6, var2, var10 - 8, 20, 0);
    }
  }

  private void c_V(int var1, int var2) {
    this.r_Image = null;
    this.s_Image = null;
    this.w_I = 0;
    this.x_I = 0;
    if (var1 != 0) {
      this.w_I = var1 - 2;
      this.r_Image = k.a_Image(193, var1, 2959909, 85);
    }

    if (var2 > 0) {
      this.x_I = var2 - 2;
      this.s_Image = k.a_Image(193, var2, 657930, 85);
    }
  }

  private void e(Graphics var1) {
    var1.drawImage(this.o_Image, this.a_AI[0], this.a_AI[1], 20);
    if (this.p_Image != null) {
      var1.drawImage(this.p_Image, this.a_AI[0] + this.a_AI[2] / 2, this.a_AI[1] + this.o_Image.getHeight() / 2, 3);
    }

    int[] var4 = this.a_AI;
    int var5 = var4[0] + 1;
    int var6 = var4[1] + var4[3];
    if (this.r_Image != null) {
      var1.drawImage(this.r_Image, var5, var4[1] + var4[3], 20);
      var6 += this.w_I;
    }

    if (this.s_Image != null) {
      var1.drawImage(this.s_Image, var5, var6, 20);
      var6 += this.x_I;
    }

    var1.drawImage(this.t_Image, var5, var6, 20);
    int[] var2 = this.a_AI;
    int var3 = this.a_AI[0] + 3;
    int var8 = var2[1] + var2[3];
    if (this.r_Image != null) {
      var8 += this.w_I;
      var1.setColor(2303014);
      var1.fillRect(var2[0] + 2, var8 - 1, this.z_I - 2, 2);
      var1.setColor(14083300);
      var1.fillRect(var2[0] + 3, var8, this.z_I - 4, 1);
    }

    if (this.s_Image != null) {
      var8 += this.x_I;
      var1.setColor(2303014);
      var1.fillRect(var2[0] + 2, var8 - 1, this.z_I - 2, 2);
      var1.setColor(14083300);
      var1.fillRect(var2[0] + 3, var8, this.z_I - 4, 1);
    }

    int var9 = var8 + this.y_I;
    var1.setColor(3027509);
    var1.drawRoundRect(var2[0] + 1, var2[1], this.z_I, var9 - var2[1], 4, 4);
    var1.setColor(12371653);
    var1.drawRoundRect(var2[0] + 2, var2[1], this.z_I - 2, var9 - var2[1] - 1, 4, 4);
    int var10 = var9;

    for (int var7 = 0; var7 < 2; var7++) {
      if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        cg.a(
          var1,
          this.q_Image,
          this.q_Image.getWidth(),
          this.q_Image.getHeight() / 3,
          var7,
          var3 + 6 + (this.q_Image.getWidth() >> 1) + (this.z_I - this.q_Image.getWidth() - 14) * var7,
          var10 - (this.y_I >> 1),
          3,
          0
        );
      } else {
        cg.a(
          var1,
          this.q_Image,
          this.q_Image.getWidth(),
          this.q_Image.getHeight() / 3,
          1 - var7,
          var3 + 6 + (this.q_Image.getWidth() >> 1) + (this.z_I - this.q_Image.getWidth() - 14) * var7,
          var10 - (this.y_I >> 1),
          3,
          0
        );
      }
    }
  }

  private void a(Graphics var1, int var2, int var3, byte[] var4) {
    if (this.c_Image != null) {
      if (var4[0] > 0) {
        var1.drawRegion(
          this.c_Image, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var2, var3, 3
        );
        cg.a(var1, var4[0], var2 + this.c_Image.getWidth() / 4, var3 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
        var2 += 20;
      }

      if (var4[1] > 0) {
        var1.drawRegion(this.c_Image, this.c_Image.getWidth() / 2, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var2, var3, 3);
        cg.a(var1, var4[1], var2 + this.c_Image.getWidth() / 4, var3 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
        var2 += 20;
      }

      if (var4[2] > 0) {
        var1.drawRegion(this.c_Image, 0, 0, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var2, var3, 3);
        cg.a(var1, var4[2], var2 + this.c_Image.getWidth() / 4, var3 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
        var2 += 20;
      }

      if (var4[3] > 0) {
        var1.drawRegion(this.c_Image, 0, this.c_Image.getHeight() / 2, this.c_Image.getWidth() / 2, this.c_Image.getHeight() / 2, 0, var2, var3, 3);
        cg.a(var1, var4[3], var2 + this.c_Image.getWidth() / 4, var3 + this.c_Image.getHeight() / 4, this.b_Image, 6, 8, 3);
      }
    }
  }

  private void a(Graphics var1, int var2) {
    f(var1);
    if (this.a_Ae[var2] != null) {
      int var3 = cg.b_S - 2;
      if (this.c_Image != null) {
        byte[] var4 = this.a_Ae[var2].e_AB;
        this.a(var1, 8, var3, var4);
      }

      var3 += 8;
      int var7 = a(this.a_Ae[this.i_I].d_AB, this.a_Ae[var2].d_AB);
      String var5 = "";
      if (var7 >= 10) {
        var5 = "[Khắc]";
      } else if (var7 <= -10) {
        var5 = "[Bị khắc]";
      }

      cg.a(var1, var5 + this.a_Ae[var2].a_String + this.a_Ae[var2].a_AI[0] + " cấp", at.a_AS[0] >> 1, var3, 15987162, 5971280, 33);
      var1.setColor(16773874);
      var1.drawRect(at.a_AS[0] - 40, 9, 37, 5);
      var1.setColor(15626599);
      var1.fillRect(at.a_AS[0] - 39, 10, this.a_Ae[var2].a_AI[3] * 36 / this.a_Ae[var2].a_AI[10], 4);
      cg.a(var1, this.a_Ae[var2].a_AI[3], at.a_AS[0] - 30, 11, this.a_Image, this.a_Image.getWidth() / 14, this.a_Image.getHeight(), 36);
      var1.setColor(16773874);
      var1.drawRect(at.a_AS[0] - 40, 21, 37, 5);
      var1.setColor(6793964);
      var1.fillRect(at.a_AS[0] - 39, 22, this.a_Ae[var2].a_AI[5] * 36 / this.a_Ae[var2].a_AI[11], 4);
      cg.a(var1, this.a_Ae[var2].a_AI[5], at.a_AS[0] - 30, 23, this.a_Image, this.a_Image.getWidth() / 14, this.a_Image.getHeight(), 36);
      var1.drawRegion(this.x_Image, 15, 0, 15, 15, 0, this.a_Ae[var2].b_m.a_I, this.a_Ae[var2].b_m.b_I - 50 + this.k_I, 33);
    }
  }

  private static void f(Graphics var0) {
    k.a(var0, 0, 0, at.a_AS[0], cg.b_S + 10, 1317669, 15788720, 80, false);
  }

  public final boolean a_Z(int var1) {
    if (this.a_I == 25) {
      if ((var1 == 8 || var1 == b.a_S) && (var1 == 8 || var1 == b.a_S)) {
        synchronized (da.a_da) {
          da.a_da.a_cd.a_ci.a_B = 0;
          if (this.e_AString != null && this.f_B < this.e_AString.length - 1) {
            this.f_B++;
          } else {
            o.b(this.a_Abl);
            o.c(this.a_Abl);
            if (!this.b_Z) {
              da.a_da.a_cd.l();
              o.g();
              if (o.b_AS != null) {
                cd.c_S = o.b_AS[1];
                da.a_da.a_cd.a_V(o.b_AS[0]);
              } else {
                cd.c_S = (short)o.e_B;
                da.a_da.a_cd.a_V(o.d_S);
              }
            } else {
              da.a_da.a_cd.l();
              if (da.a_da.a_cd.p_Z) {
                da.a_da.a_cd.p_Z = false;
                da.a_da.a_cd.a_V(cg.a_I(o.m_AString[0]));
              } else if (at.f_Z) {
                da.a_da.a_cd.b();
              } else {
                cd.m();
              }
            }

            da.a_da.a_cd.a_ci.a(0);
          }
        }
      }
    } else if (this.a_I == 5) {
      if (var1 == 8 || var1 == b.a_S) {
        this.u_I = 0;
        this.f_bx = null;
        this.a_I = 3;
      }
    } else if (this.a_I == 24) {
      if (this.e_B == 0) {
        if (var1 == 8 || var1 == b.a_S) {
          da.a_da.a_cd.a_ci.a_B = 2;
          if (this.c_AString != null) {
            if (this.c_AString.length <= 7) {
              if (this.d_AString == null) {
                a(this.a_Abl);
              } else {
                this.e_B = 1;
              }
            } else if (++a_S > this.c_AString.length - 7) {
              if (this.d_AString == null) {
                a(this.a_Abl);
              } else {
                this.e_B = 1;
              }
            }
          } else if (this.d_AString == null) {
            a(this.a_Abl);
          } else {
            this.e_B = 1;
          }
        } else if (var1 == 1) {
          if (this.c_AString != null && a_S > 0) {
            a_S--;
          }
        } else if (var1 == 6 && this.c_AString != null && this.c_AString.length > 7 && a_S < this.c_AString.length - 7) {
          a_S++;
        }
      } else if (this.e_B == 2) {
        if (var1 == 8 || var1 == b.a_S) {
          if (this.o_I < this.a_Vector.size() - 1) {
            this.o_I++;
          } else {
            this.o_I = 0;
            this.a_Vector.removeAllElements();
            a(this.a_Abl);
          }
        }
      } else if (this.e_B == 1 && (var1 == 8 || var1 == b.a_S)) {
        if (this.a_Vector.size() > 0) {
          this.e_B = 2;
        } else {
          a(this.a_Abl);
        }
      }
    } else if (this.a_I == 0) {
      bn var9 = this;
      k.b();
      if (this.g_I == 0) {
        bn var10 = this;
        if (var1 == 5 && !this.g_Z) {
          if (this.i_I == 0) {
            this.f_I++;
            this.f_I %= 7;
          } else if (this.f_I < 10) {
            this.f_I++;
          } else {
            this.f_I = 7;
          }

          this.f_Z = true;
        } else if (var1 == 2 && !this.f_Z) {
          if (this.i_I == 0) {
            this.f_I--;
            this.f_I = (this.f_I + 7) % 7;
          } else if (this.f_I > 7) {
            this.f_I--;
          } else {
            this.f_I = 10;
          }

          this.g_Z = true;
        }

        if (var1 != 8 && var1 != b.a_S) {
          if (var1 == b.b_S) {
            if (!this.e_Z) {
              if (this.i_I > 0) {
                boolean var17 = false;

                for (int var27 = this.i_I - 1; var27 >= 0; var27--) {
                  if (var10.a_Ae[var27] != null && var10.a_Ae[var27].a_B == 0 && var10.a_Ae[var27].b_B == 0) {
                    var17 = true;
                    var10.i_I = var27;
                    break;
                  }
                }

                if (var17 && var10.i_I == 0) {
                  var10.f_I = 0;
                }
              }
            } else if (this.i_I > 0) {
              boolean var18 = false;

              for (int var28 = this.i_I - 1; var28 > 0; var28--) {
                if (var10.a_Ae[var28] != null && var10.a_Ae[var28].a_B == 0 && var10.a_Ae[var28].b_B == 0) {
                  var18 = true;
                  var10.i_I = var28;
                  break;
                }
              }

              if (var18 && var10.i_I == 0) {
                var10.f_I = 0;
              }
            }
          }
        } else {
          if (da.a_da.a_cd.r_Z && this.i_I == 0) {
            if (this.c_B == 1) {
              if (this.f_I != 2) {
                da.a_da.a("Hãy chọn kỹ năng trinh sát!", 8);
                return false;
              }
            } else if (this.c_B == 2) {
              if (this.f_I != 0) {
                da.a_da.a("Hãy chọn đòn đánh trước!", 8);
                return false;
              }
            } else if (this.c_B == 3 && this.f_I != 4) {
              da.a_da.a("Hãy chọn sủng vật trước‚chọn một sủng vật để tham chiến!", 8);
              return false;
            }
          }

          if (this.a_Ae[this.i_I].c_B > 0 && this.f_I == 2) {
            da.a_da.a("Trong Trung Thiên Địa Mạc Ngôn cấm dùng kỹ năng", 10);
          } else if (this.f_I == 0 || this.f_I == 7) {
            this.g_I = 1;
            this.e_AAI[this.i_I][1] = 1;
          } else if (this.f_I == 1 || this.f_I == 8) {
            this.e_AAI[this.i_I][1] = 2;
            this.s();
          } else if (this.f_I != 2 && this.f_I != 9) {
            if (this.f_I == 3) {
              if (this.f_AAS != null) {
                this.e_AAI[this.i_I][1] = 6;
                this.r();
                this.c_V(142, this.h_I - 142);
                this.g_I = 4;
                this.a_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.w_I);
                o.d();
                if (o.a_AImage == null) {
                  o.e();
                }

                int[][] var12 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
                bo var23 = new bo((byte)1, var12);
                g var36 = new g(true);
                cp var39 = new cp();
                this.a_ab.a_AI = new int[this.f_AAS.length];

                for (int var13 = 0; var13 < var10.f_AAS.length; var13++) {
                  int var42 = o.d_I(var10.f_AAS[var13][0]);
                  var10.a_ab.a_AI[var13] = var42;
                  var36.a(o.a_AImage[var42]);
                  var23.a(o.f_AString[var42]);
                  var39.a(Integer.toString(var10.f_AAS[var13][1]));
                }

                var36.c(cg.b_S + 10);
                var23.c(cg.b_S + 10);
                var39.c(cg.b_S + 10);
                var36.a(var10.a_AI[0] + 10, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 4, var10.w_I);
                var23.a(var10.a_AI[0] + var10.a_AI[2] / 4, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 2, var10.w_I);
                var39.a(var10.a_AI[0] + var10.a_AI[2] * 3 / 4, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 4, var10.w_I);
                boolean var43 = true;
                var39.a_Z = true;
                var10.a_ab.a(var36);
                var10.a_ab.a(var23);
                var10.a_ab.a(var39);
                var10.a_ab.a(var10.x_Image, var10.a_AI[0] + 3);
              } else {
                da.a_da.a("Không có vật phẩm dùng được", 10);
              }
            } else if (this.f_I == 4) {
              this.r();
              this.c_V(88, this.h_I - 88);
              if (this.a_Acv == null) {
                da.a_da.a("Không có sủng vật để chọn", 10);
              } else {
                this.d_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.w_I);
                int[][] var14 = new int[][]{{9764604, 343466}, {15794175, 15794175}};
                int[][] var24 = new int[][]{{15921628, 5971029}, {15921628, 5971029}};
                int[][] var37 = new int[][]{{16706636, 9193495}, {16706636, 9193495}};
                bo var40 = new bo((byte)1, var14);
                bo var15 = new bo((byte)1, var24);
                bo var44 = new bo((byte)1, var37);
                this.d_ab.a_AI = new int[this.a_Acv.length];

                for (int var25 = 0; var25 < var10.a_Acv.length; var25++) {
                  var10.d_ab.a_AI[var25] = var10.a_Acv[var25].a_I;
                  var40.a(var10.a_Acv[var25].a_String);
                  var15.a(String.valueOf(var10.a_Acv[var25].a_AI[0]));
                  var44.a(" cấp");
                }

                var40.c(cg.b_S + 10);
                var15.c(cg.b_S + 10);
                var44.c(cg.b_S + 10);
                var40.a(var10.a_AI[0] + 25, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 4, var10.w_I);
                var15.a(var10.a_AI[0] + 15 + var10.a_AI[2] / 4, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 2, var10.w_I);
                var44.a(var10.a_AI[0] - 10 + var10.a_AI[2] * 3 / 4, var10.a_AI[1] + var10.a_AI[3], var10.a_AI[2] / 4, var10.w_I);
                var10.d_ab.a(var40);
                var10.d_ab.a(var15);
                var10.d_ab.a(var44);
                var10.d_ab.a(var10.x_Image, var10.a_AI[0] + 3);
                var10.d_bx = null;
                var10.d_bx = k.a(var10.a_Acv[var10.d_ab.b_I()].a_I, true);
                if (var10.d_ab != null) {
                  var10.g_I = 5;
                }
              }
            } else if (this.f_I == 5) {
              if (da.a_da.a_cd.r_Z || da.a_da.a_cd.a_ci.c_Z) {
                da.a_da.a("Không dùng được trong trận này", 8);
              } else if (cq.c_Z) {
                df.a("/bin/smsmenu.bin");
                df.a_AI[da.a_da.a_cd.a_df.b_B] = 8;
                this.b_I = 3;
                df.a_B = 2;
              } else {
                df.a("/bin/smsmenu.bin");
                da.a_da.a_cd.a_df.b(8);
                this.b_I = 3;
              }
            } else if (this.f_I == 6) {
              boolean var16 = false;
              if (!this.b_Z) {
                Random var26;
                (var26 = new Random()).setSeed(System.currentTimeMillis());
                if (Math.abs(var26.nextInt()) / 21474836 <= 30) {
                  var16 = true;
                }
              } else {
                if (at.e_Z) {
                  System.out.println("Không thể bỏ chạy");
                }

                var16 = false;
                da.a_da.a("Không thể bỏ chạy", 10);
              }

              if (var16) {
                this.t();
                o.b(da.a_da.a_cd.a_bn.a_Abl);
                o.c(da.a_da.a_cd.a_bn.a_Abl);
                da.a_da.a_cd.a_ci.a_B = 1;
                da.a_da.a_cd.l();
                if (at.f_Z) {
                  da.a_da.a_cd.b();
                } else {
                  cd.m();
                }
              } else {
                da.a_da.a("Bỏ chạy thất bại", 10);
                this.a_I = 2;
                this.i_I = 0;
                this.f_I = 0;
              }
            } else if (this.f_I == 10) {
              this.g_I = 66;
              this.i_Z = true;
              if (this.e_bx != null) {
                this.e_bx.b(0);
              }
            }
          } else if (this.a_Ae[this.i_I].c_AS == null) {
            da.a_da.a("Không có kỹ năng khả dụng", 10);
          } else {
            bn var11 = this;
            this.r();
            this.c_V(142, this.h_I - 142);
            this.g_I = 2;
            if (d.a_AImage == null) {
              d.b();
            }

            this.b_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.w_I);
            int[][] var6 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
            bo var3 = new bo((byte)1, var6);
            g var4 = new g(true);
            this.b_ab.a_AI = new int[this.a_Ae[this.i_I].c_AS.length];
            if (this.a_Ae[this.i_I].c_AS != null) {
              for (int var5 = 0; var5 < var11.a_Ae[var11.i_I].c_AS.length; var5++) {
                int var41 = o.c_I(var11.a_Ae[var11.i_I].c_AS[var5]);
                var11.b_ab.a_AI[var5] = var41;
                var4.a(d.a_AImage[var41]);
                var3.a(d.a_AString[var41]);
              }
            }

            var4.c(cg.b_S + 10);
            var3.c(cg.b_S + 10);
            var4.a(var11.a_AI[0] + 10, var11.a_AI[1] + var11.a_AI[3], var11.a_AI[2] / 4, var11.w_I);
            var3.a(var11.a_AI[0] + var11.a_AI[2] / 4, var11.a_AI[1] + var11.a_AI[3], var11.a_AI[2] / 2, var11.w_I);
            var11.b_ab.a(var4);
            var11.b_ab.a(var3);
            var11.b_ab.a(var11.x_Image, var11.a_AI[0] + 3);
            this.e_AAI[this.i_I][1] = 4;
          }
        }
      } else if (this.g_I == 1) {
        if (var1 == 1) {
          this.c_I = this.c_I(this.c_I - 1, 9);
        } else if (var1 == 6) {
          byte[] var29 = this.a_AB(10, 20);
          if (this.c_I < var29[var29.length - 1]) {
            this.c_I = this.b_I(this.c_I + 1, 20);
          } else {
            this.c_I = this.b_I(10, 20);
          }
        } else if (var1 == 8 || var1 == b.a_S) {
          this.s();
        } else if (var1 == b.b_S) {
          this.g_I = 0;
        }
      } else if (this.g_I == 21) {
        int var30 = o.c_I(this.e_AAI[this.i_I][3]);
        if (var1 == 1) {
          if (d.b_AS[var30] == 1) {
            if (this.c_I > 0) {
              this.c_I--;
            } else {
              byte[] var19 = this.a_AB(0, 10);
              this.c_I = var19[var19.length - 1];
            }
          } else if (d.b_AS[var30] == 2) {
            this.c_I = this.c_I(this.c_I - 1, 9);
          }
        } else if (var1 == 6) {
          if (d.b_AS[var30] == 1) {
            int var20 = this.a_I(0, 10);
            if (this.c_I < var20 - 1) {
              this.c_I = this.b_I(this.c_I + 1, 10);
            } else {
              this.c_I = this.b_I(0, 10);
            }
          } else if (d.b_AS[var30] == 2) {
            byte[] var21 = this.a_AB(10, 20);
            if (this.c_I < var21[var21.length - 1]) {
              this.c_I = this.b_I(this.c_I + 1, 20);
            } else {
              this.c_I = this.b_I(10, 20);
            }
          }
        } else if (var1 == 8 || var1 == b.a_S) {
          this.s();
        } else if (var1 == b.b_S) {
          this.c_I = this.b_I(10, 20);
          this.g_I = 0;
        }
      } else if (this.g_I == 41) {
        if (var1 == 1) {
          if (this.n_I > 0) {
            this.n_I--;
          } else {
            byte[] var31 = this.a_AB(0, 10);
            this.n_I = var31[var31.length - 1];
          }
        } else if (var1 == 6) {
          if (this.n_I < this.a_I(0, 10) - 1) {
            this.n_I++;
          } else {
            this.n_I = 0;
          }
        } else if (var1 == 8 || var1 == b.a_S) {
          byte[] var32 = this.a_AB(0, 10);
          this.c_I = var32[this.n_I];
          this.s();
        } else if (var1 == b.b_S) {
          this.g_I = 0;
        }
      } else if (this.g_I == 2) {
        if (var1 == 1) {
          this.b_ab.b_V();
        } else if (var1 == 6) {
          this.b_ab.c_V();
        } else if (var1 != 8 && var1 != b.a_S) {
          if (var1 == b.b_S) {
            this.c_I = this.b_I(10, 20);
            this.g_I = 0;
          }
        } else if (da.a_da.a_cd.r_Z && this.c_B == 1 && this.a_Ae[this.i_I].a_Ad[this.b_ab.b_I()].a_S != 39) {
          da.a_da.a("Hãy chọn kỹ năng trinh sát!", 8);
        } else {
          this.c_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.w_I);
          int[][] var33 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
          bo var22 = new bo((byte)1, var33);

          for (int var34 = 0; var34 < var9.a_Ae[var9.i_I].a_Ad[var9.b_ab.b_I()].a_B; var34++) {
            int var38 = o.c_I(var9.a_Ae[var9.i_I].a_Ad[var9.b_ab.b_I()].a_S);
            var22.a(d.a_AString[var38] + "    " + (var34 + 1) + "   cấp");
          }

          var22.c(cg.b_S + 10);
          var22.a(var9.a_AI[0], var9.a_AI[1] + var9.a_AI[3], var9.a_AI[2], var9.w_I);
          var9.c_ab.a(var22);
          var9.c_ab.a(var9.x_Image, var9.a_AI[0] + 15);
          var9.g_I = 3;
          var9.e_AAI[var9.i_I][3] = var9.a_Ae[var9.i_I].a_Ad[var9.b_ab.b_I()].a_S;
        }
      } else if (this.g_I == 4) {
        if (var1 == 1) {
          this.a_ab.b_V();
        } else if (var1 == 6) {
          this.a_ab.c_V();
        } else if (var1 == 8 || var1 == b.a_S) {
          if (this.f_AAS[this.a_ab.b_I()][1] > 0) {
            if (da.a_da.a_cd.r_Z && this.i_I == 0 && this.c_B == 3 && this.f_AAS[this.a_ab.b_I()][0] != 29) {
              da.a_da.a("Hãy chọn thẻ phong ấn", 8);
              return false;
            }

            if ((da.a_da.a_cd.a_ci.c_Z || da.a_da.a_cd.p_Z || da.a_da.a_cd.q_Z) && cg.a_Z(new short[]{29, 30, 31}, this.f_AAS[this.a_ab.b_I()][0])) {
              da.a_da.a("Không dùng được trong trận này", 8);
              return false;
            }
          }

          this.n_I = 0;
          this.g_I = 41;
        } else if (var1 == b.b_S) {
          this.g_I = 0;
        }
      } else if (this.g_I == 5) {
        if (var1 == 1) {
          this.d_ab.b_V();
          this.d_bx = null;
          this.d_bx = k.a(this.a_Acv[this.d_ab.b_I()].a_I, true);
        } else if (var1 == 6) {
          this.d_ab.c_V();
          this.d_bx = null;
          this.d_bx = k.a(this.a_Acv[this.d_ab.b_I()].a_I, true);
        } else if (var1 == 8 || var1 == b.a_S) {
          this.v_I = 0;
          this.B_I = this.d_ab.b_I();
          this.g_I = 51;
        } else if (var1 == b.b_S) {
          this.g_I = 0;
        }
      } else if (this.g_I == 51) {
        if (var1 == 1 || var1 == 6) {
          if (this.a_I(0, 10) > 1) {
            if (this.v_I == 2) {
              this.v_I = 0;
            } else if (this.v_I == 0) {
              this.v_I = 2;
            }
          }
        } else if (var1 == b.b_S) {
          this.g_I = 5;
        } else if (var1 == 8 || var1 == b.a_S) {
          this.i_Z = true;
          if (this.e_bx != null) {
            this.e_bx.b(0);
          }
        }
      } else if (this.g_I == 3) {
        if (var1 == 1) {
          this.c_ab.b_V();
        } else if (var1 == 6) {
          this.c_ab.c_V();
        } else if (var1 != 8 && var1 != b.a_S) {
          if (var1 == b.b_S) {
            this.c_I = this.b_I(10, 20);
            this.g_I = 2;
          }
        } else {
          if (d.a_AString == null) {
            d.a();
          }

          int var35 = o.c_I(this.e_AAI[this.i_I][3]);
          if (d.a_AAS[var35][this.c_ab.b_I()] > this.a_Ae[this.i_I].a_AI[5]) {
            da.a_da.a("MP quá thấp, không dùng được kỹ năng", 8);
          } else {
            this.e_AAI[this.i_I][4] = this.c_ab.b_I() + 1;
            if (d.c_AS[var35] == 1) {
              if (d.b_AS[var35] != 0) {
                if (d.b_AS[var35] == 1) {
                  this.g_I = 21;
                  this.c_I = 0;
                } else if (d.b_AS[var35] == 2) {
                  this.c_I = this.b_I(10, 20);
                  this.g_I = 21;
                  return false;
                }

                return false;
              }

              this.c_I = this.e_AAI[this.i_I][0];
            }

            this.s();
          }
        }
      }
    } else if (this.a_I == 4) {
      if (this.a_cf != null) {
        if (this.a_cf.a_Z()) {
          if (this.a_cf.a_B >= this.a_cf.a_AString.length - 1) {
            if (this.d_B == 1) {
              this.a_I = 0;
              this.g_I = 0;
              this.b_AString = null;
            } else if (this.d_B != 2 && this.d_B != 3 && this.d_B == 4) {
              return true;
            }
          } else {
            this.a_cf.f();
          }
        } else {
          this.a_cf.c();
        }
      }
    } else if ((var1 == 8 || var1 == b.a_S) && this.a_I == 1) {
      this.a_I = 2;
    }

    return false;
  }

  private boolean a(byte[] var1) {
    if (var1 != null) {
      for (int var2 = 0; var2 < var1.length; var2++) {
        if (this.a_Ae[var1[var2]] != null && this.a_Ae[var1[var2]].f_I != 16) {
          return false;
        }
      }
    }

    return true;
  }

  public final void b_V(int var1, int var2) {
    int var3 = this.a_AI[0];
    int var4 = this.a_AI[1] + this.a_AI[3] + this.h_I;
    int var5 = this.a_AI[2];
    if (this.a_I == 0) {
      if (this.g_I != 2 && this.g_I != 3 && this.g_I != 4 && this.g_I != 5) {
        if ((this.g_I == 1 || this.g_I == 21 || this.g_I == 41 || this.g_I == 51)
          && ab.a(var1, var2, at.a_AS[0] - 30, at.a_AS[1] - 30, 30, 30)
          && (
            at.a_String.equals("E6")
              || at.a_String.equals("E680")
              || at.a_String.equals("W958C")
              || at.a_String.equals("S1")
              || at.a_String.equals("N95")
              || at.a_String.equals("N5800")
          )) {
          this.a_Z(b.b_S);
        }
      } else if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        if (ab.a(var1, var2, var3, var4, 30, 30)) {
          this.a_Z(b.a_S);
          return;
        }

        if (ab.a(var1, var2, var3 + var5 - 30, var4, 30, 30)) {
          this.a_Z(b.b_S);
          return;
        }
      } else {
        if (ab.a(var1, var2, var3, var4, 30, 30)) {
          this.a_Z(b.b_S);
          return;
        }

        if (ab.a(var1, var2, var3 + var5 - 30, var4, 30, 30)) {
          this.a_Z(b.a_S);
          return;
        }
      }
    }
  }

  private void p() {
    for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null) {
        this.a_Ae[var1].i_Z = false;
        this.a_Ae[var1].a_bx.a_V(0);
        this.a_Ae[var1].a(this.a_Ae[var1].c_m);
      }
    }
  }

  private void q() {
    this.t();
    if (this.v_I == 0) {
      if (this.a_Abl.length >= 2) {
        if (this.a_Acv[this.B_I].a_AI[1] > 0) {
          boolean var1 = false;
          if (this.a_Abl[1] != null) {
            for (int var2 = 0; var2 < o.a_Acv.length; var2++) {
              if (o.a_Acv[var2].b_I == this.a_Abl[1].c_S) {
                o.a_Acv[var2].a_Z = false;
                if (this.a_Abl[1].a_AI[3] <= 0) {
                  o.a_Acv[var2].b_Z = true;
                }

                var1 = true;
                break;
              }
            }
          } else {
            var1 = true;
          }

          if (var1) {
            o.b(this.a_Abl);
            this.a_Acv[this.B_I].a_Z = true;
            this.a_Abl[1] = o.a_bl(this.a_Acv[this.B_I]);
            this.b_V(1);
            this.m();
            this.a_Acv = null;
            this.a_Acv = o.a_Acv();
            this.e_Z = true;
          } else {
            System.out.println("không tìm thấy sủng vật của đội trong mảng sủng vật");
          }
        }
      } else {
        new bl();
        bl var3 = o.a_bl(this.a_Acv[this.B_I]);
        if (this.a_Abl == null) {
          this.a_Abl = new bl[1];
        } else {
          bl[] var6 = new bl[this.a_Abl.length + 1];
          System.arraycopy(this.a_Abl, 0, var6, 0, this.a_Abl.length);
          this.a_Abl = var6;
        }

        this.a_Abl[this.a_Abl.length - 1] = var3;
        this.b_V(1);
        this.m();
        this.a_Acv[this.B_I].a_Z = true;
        this.e_Z = true;
        this.a_Acv = null;
        this.a_Acv = o.a_Acv();
      }
    } else if (this.v_I == 2) {
      if (this.a_Abl.length >= 3) {
        if (this.a_Acv[this.B_I].a_AI[1] > 0) {
          boolean var4 = false;
          if (this.a_Abl[2] != null) {
            for (int var7 = 0; var7 < o.a_Acv.length; var7++) {
              if (o.a_Acv[var7].b_I == this.a_Abl[2].c_S) {
                o.a_Acv[var7].a_Z = false;
                if (this.a_Abl[2].a_AI[3] <= 0) {
                  o.a_Acv[var7].b_Z = true;
                }

                var4 = true;
                break;
              }
            }
          } else {
            var4 = true;
          }

          if (var4) {
            o.b(this.a_Abl);
            this.a_Acv[this.B_I].a_Z = true;
            this.a_Abl[2] = o.a_bl(this.a_Acv[this.B_I]);
            this.b_V(2);
            this.m();
            this.a_Acv = null;
            this.a_Acv = o.a_Acv();
            this.e_Z = true;
          }
        }
      } else {
        new bl();
        bl var5 = o.a_bl(this.a_Acv[this.B_I]);
        if (this.a_Abl == null) {
          this.a_Abl = new bl[1];
        } else {
          bl[] var8 = new bl[this.a_Abl.length + 1];
          System.arraycopy(this.a_Abl, 0, var8, 0, this.a_Abl.length);
          this.a_Abl = var8;
        }

        this.a_Abl[this.a_Abl.length - 1] = var5;
        this.b_V(2);
        this.m();
        this.a_Acv[this.B_I].a_Z = true;
        this.e_Z = true;
        this.a_Acv = null;
        this.a_Acv = o.a_Acv();
      }
    }

    this.B_I = 0;
    this.s();
  }

  private void b_V(int var1) {
    this.a_Ae[var1] = w.a_e();
    this.a_Ae[var1].a_String = this.a_Abl[var1].a_String;
    this.a_Ae[var1].a_I = var1;
    this.a_Ae[var1].d_I = this.a_Abl[var1].h_S;
    this.a_Ae[var1].a_bx = new bx();
    this.a_Ae[var1].a_bx = k.a(this.a_Abl[var1].h_S, true);
    if (this.a_Ae[var1].a_bx.a_I(2) > 1) {
      e var10000 = this.a_Ae[var1];
      byte var3 = 0;
      var10000.e_I = var3;
    } else {
      e var17 = this.a_Ae[var1];
      byte var11 = 1;
      var17.e_I = var11;
    }

    this.a_Ae[var1].b_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[2];
    this.a_Ae[var1].c_S = this.a_Ae[var1].a_bx.a(0, 0, 0)[3];
    String var2;
    String var12 = cg.a_String(var2 = cg.a(this.a_String, "attackDefault:", "attackDefaultEnd"), "actEffect");
    var2 = cg.a_String(var2, "frame");
    this.a_Ae[var1].c_AB = cg.b_AB(var2, ",");
    this.a_Ae[var1].c_bx = new bx();
    this.a_Ae[var1].c_bx.a(var12, 1);
    this.a_Ae[var1].d_bx = new bx();
    this.a_Ae[var1].d_bx.a("/ft/defense.av", 1);
    this.a_Ae[var1].e_bx = new bx();
    this.a_Ae[var1].e_bx.a("/ft/zhongdu.av", 1);
    this.a_Ae[var1].f_bx = new bx();
    this.a_Ae[var1].f_bx.a("/ft/zhunluan.av", 1);
    this.a_Ae[var1].g_bx = new bx();
    this.a_Ae[var1].g_bx.a("/ft/zhongyun.av", 1);

    for (int var7 = 0; var7 < 6; var7++) {
      if (this.a_Abl[var1].a_Ack != null && this.a_Abl[var1].a_Ack[var7] != null) {
        this.a_Ae[var1].d_S = (short)(this.a_Ae[var1].d_S + (short)(this.a_Abl[var1].a_Ack[var7].n_S + this.a_Abl[var1].a_Ack[var7].g_S));
      }
    }

    if (var1 == 1) {
      this.a_Ae[var1].c_m.a_I = this.d_AS[0];
      this.a_Ae[var1].c_m.b_I = this.e_AS[0];
    } else {
      this.a_Ae[var1].c_m.a_I = this.d_AS[var1];
      this.a_Ae[var1].c_m.b_I = this.e_AS[var1];
    }

    this.a_Ae[var1].b_m.a_I = this.a_Ae[var1].c_m.a_I;
    this.a_Ae[var1].b_m.b_I = this.a_Ae[var1].c_m.b_I;
    this.a_Ae[var1].b_Z = true;
    Vector var8 = new Vector();
    Vector var13 = new Vector();
    this.a_Ae[var1].a_Ad = null;
    this.a_Ae[var1].c_AS = null;

    for (int var4 = 0; this.a_Abl[var1].a_AS != null && var4 < this.a_Abl[var1].a_AS.length; var4++) {
      if (!cg.a_Z(this.i_AS, this.a_Abl[var1].a_AS[var4])) {
        var8.addElement(new Short(this.a_Abl[var1].a_AS[var4]));
        var13.addElement(this.a_Abl[var1].a_Ad[var4]);
      }

      if (this.a_Abl[var1].a_AS[var4] == 7) {
        this.a_Ae[var1].j_Z = true;
      }
    }

    if (var8.size() > 0) {
      this.a_Ae[var1].c_AS = new short[var8.size()];
      this.a_Ae[var1].a_Ad = new d[var13.size()];
      Short[] var14 = new Short[var8.size()];
      var8.copyInto(var14);

      for (int var9 = 0; var9 < this.a_Ae[var1].c_AS.length; var9++) {
        this.a_Ae[var1].c_AS[var9] = var14[var9];
        this.a_Ae[var1].a_Ad[var9] = (d)var13.elementAt(var9);
      }
    }

    this.a_Ae[var1].d_AB = new byte[4];
    this.a_Ae[var1].e_AB = new byte[4];

    for (int var15 = 0; var15 < this.a_Abl[var1].a_AB.length; var15++) {
      this.a_Ae[var1].d_AB[var15] = this.a_Abl[var1].a_AB[var15];
      this.a_Ae[var1].e_AB[var15] = this.a_Abl[var1].a_AB[var15];
    }

    byte var16 = 0;
    if (this.a_Ae[0].c_AS != null && cg.a_Z(this.a_Ae[0].c_AS, 46)) {
      var16 = 5;
    }

    for (int var10 = 0; var10 < this.a_Abl[var1].a_AI.length; var10++) {
      this.a_Ae[var1].a_AI[var10] = this.a_Abl[var1].a_AI[var10];
    }

    this.a_Ae[var1].j_I = this.a_Abl[var1].a_AI[12] * (var16 + 100) / 100;
    this.a_Ae[var1].k_I = this.a_Abl[var1].a_AI[13] * (var16 + 100) / 100;
    this.a_Ae[var1].l_I = this.a_Abl[var1].a_AI[14] * (var16 + 100) / 100;
    if (o.b_I > 0) {
      this.a_Ae[var1].j_I = this.a_Ae[var1].j_I * o.b_I;
      this.a_Ae[var1].k_I = this.a_Ae[var1].k_I * o.b_I;
      this.a_Ae[var1].l_I = this.a_Ae[var1].l_I * o.b_I;
    }

    this.a_Ae[var1].j_I = this.a_Ae[var1].j_I + a_I(this.a_Ae[var1]);
    this.a_Ae[var1].m_I = this.a_Ae[var1].a_AI[3] * (var16 + 100) / 100;
    this.a_Ae[var1].n_I = this.a_Ae[var1].a_AI[5] * (var16 + 100) / 100;
    if (var1 != 0) {
      e var18 = this.a_Ae[0];
      e var5 = this.a_Ae[var1];
    }
  }

  private void r() {
    if (this.t_Image == null) {
      this.t_Image = k.a_Image(193, 30, 2959909, 85);
    }

    this.y_I = 28;
    if (this.o_Image == null) {
      this.o_Image = cc.a("/pic/D03.gf");
    }

    if (this.p_Image == null) {
      this.p_Image = cc.a("/pic/D46.gf");
    }

    if (this.q_Image == null) {
      this.q_Image = cc.a("/pic/D06.gf");
    }
  }

  private int b_I(int var1, int var2) {
    int var3 = -1;

    for (int var4 = var1; var4 < var2; var4++) {
      if (this.a_Ae[var4] != null && !this.a_Ae[var4].f_Z) {
        var3 = var4;
        break;
      }
    }

    return var3 == -1 ? var1 - 1 : var3;
  }

  private int c_I(int var1, int var2) {
    var2 = -1;

    for (int var3 = var1; var3 > 9; var3--) {
      if (this.a_Ae[var3] != null && !this.a_Ae[var3].f_Z) {
        var2 = var3;
        break;
      }
    }

    return var2 == -1 ? var1 + 1 : var2;
  }

  private void s() {
    label91: {
      if (this.i_I == 0) {
        this.e_AAI[this.i_I][2] = this.c_I;
        if (this.a_I(0, 10) != 1) {
          if (this.a_I(0, 10) > 1) {
            byte[] var1;
            if ((var1 = cg.b(this.a_AB(0, 10), 0)).length == 1) {
              if (this.a_Ae[var1[0]] != null && this.a_Ae[var1[0]].a_B > 0) {
                this.a_I = 2;
                this.i_I = 0;
                this.f_I = 0;
              } else if (this.a_Ae[var1[0]] != null && this.a_Ae[var1[0]].b_B > 0) {
                byte[] var2 = this.a_AB(0, this.a_Ae.length);
                this.e_AAI[var1[0]][1] = 1;
                byte[] var3 = cg.b(var2, var1[0]);
                int var4 = k.a(0, var3.length);
                this.e_AAI[var1[0]][2] = var3[var4];
                this.a_I = 2;
                this.i_I = 0;
                this.f_I = 0;
              } else {
                this.i_I = var1[0];
                this.f_I = 7;
                this.c_I = this.b_I(10, 20);
              }
            } else if (var1.length == 2) {
              if (this.a_Ae[var1[0]] != null && this.a_Ae[var1[0]].a_B > 0) {
                if (this.a_Ae[var1[1]] != null && this.a_Ae[var1[1]].a_B > 0) {
                  this.a_I = 2;
                  this.i_I = 0;
                  this.f_I = 0;
                } else if (this.a_Ae[var1[1]] != null && this.a_Ae[var1[1]].b_B > 0) {
                  byte[] var7 = this.a_AB(0, this.a_Ae.length);
                  this.e_AAI[var1[1]][1] = 1;
                  byte[] var11 = cg.b(var7, var1[1]);
                  int var15 = k.a(0, var11.length);
                  this.e_AAI[var1[1]][2] = var11[var15];
                  this.i_I = 0;
                  this.f_I = 0;
                  this.a_I = 2;
                } else {
                  this.i_I = var1[1];
                  this.f_I = 7;
                  this.c_I = this.b_I(10, 20);
                }
              } else if (this.a_Ae[var1[0]] != null && this.a_Ae[var1[0]].b_B > 0) {
                byte[] var6 = this.a_AB(0, this.a_Ae.length);
                this.e_AAI[var1[0]][1] = 1;
                byte[] var9 = cg.b(var6, var1[0]);
                int var13 = k.a(0, var9.length);
                this.e_AAI[var1[0]][2] = var9[var13];
                if (this.a_Ae[var1[1]] != null && this.a_Ae[var1[1]].a_B > 0) {
                  this.a_I = 2;
                  this.i_I = 0;
                  this.f_I = 0;
                } else if (this.a_Ae[var1[1]] != null && this.a_Ae[var1[1]].b_B > 0) {
                  this.e_AAI[var1[1]][1] = 1;
                  var9 = cg.b(var6, var1[1]);
                  var13 = k.a(0, var9.length);
                  this.e_AAI[var1[0]][2] = var9[var13];
                  this.i_I = 0;
                  this.f_I = 0;
                  this.a_I = 2;
                } else {
                  this.i_I = var1[1];
                  this.f_I = 7;
                  this.c_I = this.b_I(10, 20);
                }
              } else {
                this.i_I = var1[0];
                this.f_I = 7;
                this.c_I = this.b_I(10, 20);
              }
            }
          }
          break label91;
        }
      } else {
        this.e_AAI[this.i_I][2] = this.c_I;
        if (this.i_I == 1 && this.a_Ae[2] != null && !this.a_Ae[2].f_Z) {
          if (this.a_Ae[2].a_B > 0) {
            this.a_I = 2;
            this.i_I = 0;
            break label91;
          }

          if (this.a_Ae[2].b_B <= 0) {
            this.i_I = 2;
            this.f_I = 7;
            this.c_I = this.b_I(10, 20);
            break label91;
          }

          byte[] var5 = this.a_AB(0, this.a_Ae.length);
          this.e_AAI[2][1] = 1;
          byte[] var8 = cg.b(var5, 2);
          int var12 = k.a(0, var8.length);
          this.e_AAI[2][2] = var8[var12];
        }
      }

      this.a_I = 2;
      this.i_I = 0;
      this.f_I = 0;
    }

    this.g_I = 0;
  }

  public final void d_V() {
    for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null) {
        this.a_Ae[var1].a_bx = null;
      }

      this.a_Ae[var1] = null;
    }

    if (this.d_ab != null) {
      this.d_ab.a_V();
      this.d_ab = null;
    }

    if (this.b_ab != null) {
      this.b_ab.a_V();
      this.b_ab = null;
    }

    if (this.a_ab != null) {
      this.a_ab.a_V();
      this.a_ab = null;
    }

    if (this.c_ab != null) {
      this.c_ab.a_V();
      this.c_ab = null;
    }

    this.d_bx = null;
  }

  public final void e_V() {
    a_V(2);
    if (this.b_Z) {
      this.e_AString = null;
    } else {
      this.e_AString = new String[]{"Mất một nửa vàng", "Tự về làng"};
    }

    this.f_B = 0;
    this.r();
    this.c_V(142, this.h_I - 142);
    this.t();
    this.u();
    o.a(new bl[]{this.a_Abl[0]});

    for (int var1 = 0; var1 < this.a_Abl.length; var1++) {
      if (var1 == 0) {
        o.d(this.a_Abl[var1]);
      } else if (this.a_Abl[var1] != null) {
        o.e(this.a_Abl[var1]);
      }
    }

    this.a_I = 25;
  }

  public final m a_m(int var1, int var2) {
    m var3 = new m();
    if (var1 >= 10) {
      var3.a_I = this.a_Ae[var1].c_m.a_I + this.a_Ae[var2].b_S;
    } else {
      var3.a_I = this.a_Ae[var1].c_m.a_I - this.a_Ae[var2].b_S;
    }

    var3.b_I = this.a_Ae[var1].c_m.b_I;
    return var3;
  }

  public final void f() {
    if (da.a_da.a_S % 3 == 0) {
      this.k_I = this.k_I == 0 ? 3 : 0;
    }

    if (this.a_Ae[0] != null) {
      for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
        e var2;
        if (this.a_Ae[var1] != null && (var2 = this.a_Ae[var1]).b_Z && var2.f_I != 0) {
          if (var2.f_I == 6) {
            if (var2.a_bx.c() >= var2.a_bx.a_I(1) - 1) {
              if (var2.e_I == 1) {
                var2.a_bx.j_Z = false;
                var2.h_Z = false;
              } else {
                m var30 = var2.a_m;
                m var6 = var2.c_m;
                var2.g_I = cj.a(var6.a_I, var30.a_I);
                var2.b_AI = cj.a_AI(var6.a_I, var6.b_I, var30.a_I, var30.b_I, var2.a_bx.a_I(2));
                var2.h_Z = false;
              }

              e.a(var2.a_bx, 2, 0);
              var2.o_I = 0;
              var2.f_I = 14;
            }
          } else if (var2.f_I == 14) {
            if (var2.e_I == 0) {
              if (da.a_da.a_S % 3 == 0) {
                var2.b_m = cj.a_m(var2.b_m.a_I, var2.b_m.b_I, var2.b_AI[0], var2.b_AI[1], var2.g_I);
              }

              if (var2.a_I < 10) {
                if (var2.b_m.a_I <= var2.a_m.a_I + var2.b_S) {
                  if (var2.i_Z) {
                    var2.f_I = 13;
                    if (var2.a_AB != null) {
                      da.a_da.a_cd.a_bn.a_I = 8;
                      e.a(var2.a_bx, 3, 0);
                    } else {
                      e.a(var2.a_bx, 0, 0);
                    }
                  } else {
                    if (var2.m_Z) {
                      da.a_da.a_cd.a_bn.d_Z = true;
                    }

                    var2.f_I = 1;
                    e.a(var2.a_bx, 3, 0);
                  }
                }
              } else if (var2.b_m.a_I >= var2.a_m.a_I - var2.b_S) {
                if (var2.i_Z) {
                  var2.f_I = 13;
                  if (var2.a_AB != null) {
                    da.a_da.a_cd.a_bn.a_I = 8;
                    e.a(var2.a_bx, 3, 0);
                  } else {
                    e.a(var2.a_bx, 0, 0);
                  }
                } else {
                  if (var2.m_Z) {
                    da.a_da.a_cd.a_bn.d_Z = true;
                  }

                  var2.f_I = 1;
                  e.a(var2.a_bx, 3, 0);
                }
              }
            } else if (var2.o_I > 6) {
              var2.a_bx.j_Z = true;
              if (var2.i_Z) {
                var2.f_I = 13;
                if (var2.a_AB != null) {
                  da.a_da.a_cd.a_bn.a_I = 8;
                  e.a(var2.a_bx, 3, 0);
                } else {
                  e.a(var2.a_bx, 0, 0);
                }
              } else {
                if (var2.m_Z) {
                  da.a_da.a_cd.a_bn.d_Z = true;
                }

                e.a(var2.a_bx, 3, 0);
                var2.f_I = 1;
              }

              var2.o_I = 0;
              var2.b_m.a_I = at.a_AS[0] >> 1;
              int var106 = var2.b_I;
              bn var69 = da.a_da.a_cd.a_bn;
              var2.b_m.b_I = da.a_da.a_cd.a_bn.a_Ae[var106].c_m.b_I;
            } else if (da.a_da.a_S % 3 == 0) {
              var2.o_I++;
            }
          } else if (var2.f_I == 1) {
            if (var2.a_bx.c() >= var2.a_bx.a_I(3) - 1) {
              if (!var2.l_Z) {
                int var103 = var2.b_I;
                bn var66 = da.a_da.a_cd.a_bn;
                if (da.a_da.a_cd.a_bn.a_Ae[var103].j_Z) {
                  var103 = var2.b_I;
                  var66 = da.a_da.a_cd.a_bn;
                  if (!da.a_da.a_cd.a_bn.a_Ae[var103].l_Z && var2.b_B == 0 && var2.a_B == 0) {
                    int var4 = k.b(1, 100);
                    if (50 > var4) {
                      var103 = var2.b_I;
                      var66 = da.a_da.a_cd.a_bn;
                      da.a_da.a_cd.a_bn.a_Ae[var103].l_Z = true;
                    }
                  }
                }
              }

              var2.q_Z = true;
              if (var2.e_I == 0) {
                var2.a_V();
              }

              e.a(var2.a_bx, 0, 0);
              var2.h_Z = false;
              if (var2.a_bx.c() <= var2.c_AB.length) {
                var2.g_Z = true;
              }

              var2.f_I = 2;
            } else if (var2.a_bx.c() > var2.c_AB.length) {
              for (int var29 = 0; var29 < var2.c_AB.length; var29++) {
                if (var2.a_bx.c() == var2.c_AB[var29]) {
                  var2.g_Z = true;
                }
              }
            }
          } else if (var2.f_I == 8) {
            int var101 = var2.b_I;
            bn var64 = da.a_da.a_cd.a_bn;
            if (da.a_da.a_cd.a_bn.a_Ae[var101].f_I == 0) {
              var101 = var2.b_I;
              var64 = da.a_da.a_cd.a_bn;
              da.a_da.a_cd.a_bn.a_Ae[var101].l_Z = false;
              e.a(var2.a_bx, 0, 0);
              var2.a_AI[3] = var2.m_I;
              if (var2.a_AI[3] <= 0) {
                var2.a_AI[3] = 0;
                var2.m_I = 0;
                var2.f_Z = true;
                var2.a_bx.j_Z = false;
                var2.f_I = 128;
              } else {
                var2.f_I = 0;
              }
            }
          } else if (var2.f_I == 15) {
            if (!var2.h_Z) {
              if (var2.e_I == 0) {
                if (da.a_da.a_S % 3 == 0) {
                  var2.b_m = cj.a_m(var2.b_m.a_I, var2.b_m.b_I, var2.b_AI[0], var2.b_AI[1], var2.g_I);
                }

                if (var2.a_I < 10) {
                  if (var2.b_m.a_I >= var2.c_m.a_I - var2.b_S / 2) {
                    var2.h_Z = true;
                    e.a(var2.a_bx, 0, 0);
                  }
                } else if (var2.b_m.a_I <= var2.c_m.a_I + var2.b_S / 2) {
                  var2.h_Z = true;
                  e.a(var2.a_bx, 0, 0);
                }
              } else if (var2.o_I > 6) {
                var2.b_m.a_I = var2.c_m.a_I;
                var2.b_m.b_I = var2.c_m.b_I;
                var2.a_bx.j_Z = true;
                var2.h_Z = true;
                e.a(var2.a_bx, 0, 0);
                var2.o_I = 0;
              } else if (da.a_da.a_S % 3 == 0) {
                var2.o_I++;
              }
            }

            if (var2.h_Z) {
              var2.b_m.a_I = var2.c_m.a_I;
              var2.b_m.b_I = var2.c_m.b_I;
              int var95 = var2.b_I;
              bn var58 = da.a_da.a_cd.a_bn;
              if (da.a_da.a_cd.a_bn.a_Ae[var95].l_Z) {
                var2.f_I = 8;
                var95 = var2.b_I;
                var58 = da.a_da.a_cd.a_bn;
                e.a(da.a_da.a_cd.a_bn.a_Ae[var95].a_bx, 3, 0);
                var95 = var2.b_I;
                var58 = da.a_da.a_cd.a_bn;
                da.a_da.a_cd.a_bn.a_Ae[var95].b_I = var2.a_I;
                da.a_da.a_cd.a_bn.a_V(var2.b_I, var2.a_I);
                var95 = var2.b_I;
                var58 = da.a_da.a_cd.a_bn;
                da.a_da.a_cd.a_bn.a_Ae[var95].a_m = da.a_da.a_cd.a_bn.a_m(var2.a_I, var2.b_I);
                var95 = var2.b_I;
                var58 = da.a_da.a_cd.a_bn;
                da.a_da.a_cd.a_bn.a_Ae[var95].f_I = 6;
                var95 = var2.b_I;
                var58 = da.a_da.a_cd.a_bn;
                da.a_da.a_cd.a_bn.a_Ae[var95].a_bx.a_V(1);
              } else {
                var2.b_m.a_I = var2.c_m.a_I;
                var2.b_m.b_I = var2.c_m.b_I;
                var2.f_I = 0;
                var2.d_Z = false;
              }
            }
          } else if (var2.f_I == 13) {
            if (var2.a_bx.a() == 3) {
              if (var2.a_bx.c() >= var2.a_bx.a_I(3) - 1) {
                if (var2.a_bx.c() <= var2.c_AB.length) {
                  var2.g_Z = true;
                }

                e.a(var2.a_bx, 0, 0);
                var2.f_I = 16;
              } else if (var2.a_bx.c() > var2.c_AB.length) {
                for (int var3 = 0; var3 < var2.c_AB.length; var3++) {
                  if (var2.a_bx.c() == var2.c_AB[var3]) {
                    if (da.a_da.a_S % 2 == 0) {
                      int var91 = var2.b_I;
                      bn var54 = da.a_da.a_cd.a_bn;
                      int var92 = var2.b_I;
                      var54 = da.a_da.a_cd.a_bn;
                      da.a_da.a_cd.a_bn.a_Ae[var91].b_m.a_I = da.a_da.a_cd.a_bn.a_Ae[var92].c_m.a_I - 4;
                    } else {
                      int var93 = var2.b_I;
                      bn var56 = da.a_da.a_cd.a_bn;
                      int var94 = var2.b_I;
                      var56 = da.a_da.a_cd.a_bn;
                      da.a_da.a_cd.a_bn.a_Ae[var93].b_m.a_I = da.a_da.a_cd.a_bn.a_Ae[var94].c_m.a_I + 4;
                    }

                    var2.g_Z = true;
                  }
                }
              }
            }
          } else if (var2.f_I == 5) {
            if (var2.e_Z && var2.a_bx.c() >= var2.a_bx.a_I(4) - 1) {
              e.a(var2.a_bx, 0, 0);
              var2.q_Z = true;
              if (var2.d_B > 0) {
                var2.d_B--;
              }

              var2.f_I = 10;
            }
          } else if (var2.f_I != 9 && var2.f_I != 17) {
            if (var2.f_I == 10 && var2.c_Z && var2.d_Z) {
              var2.d_Z = false;
              if (var2.c_Z && var2.c_I == 39) {
                int var7 = var2.b_I;
                bn var5 = da.a_da.a_cd.a_bn;
                if (!cg.a_Z(o.p_AS, da.a_da.a_cd.a_bn.a_Ae[var7].d_I)) {
                  var7 = var2.b_I;
                  var5 = da.a_da.a_cd.a_bn;
                  o.p_AS = cg.a_AS(o.p_AS, da.a_da.a_cd.a_bn.a_Ae[var7].d_I);
                  da.a_da.a_cd.a_bn.a_I = 5;
                } else {
                  da.a_da.a("Đã có sủng vật này, trinh sát vô hiệu", 8);
                }
              }

              var2.c_Z = false;
              if (!da.a_da.a_cd.a_bn.c_Z) {
                da.a_da.a_cd.a_bn.c_Z = true;
              }

              if (var2.d_B == 0) {
                var2.d_B = 0;
                var2.e_Z = false;
                var2.f_I = 0;
              } else {
                int var90 = var2.b_I;
                bn var53 = da.a_da.a_cd.a_bn;
                if (da.a_da.a_cd.a_bn.a_Ae[var90].f_Z) {
                  var2.d_B = 0;
                  var2.e_Z = false;
                  var2.f_I = 0;
                } else {
                  var2.e_Z = true;
                  var2.a(4, 0);
                  da.a_da.a_cd.a_bn.a_V(var2.a_I, var2.b_I);
                  var2.f_I = 5;
                }
              }
            }
          } else if (!var2.q_Z && var2.a_bx.c() >= var2.a_bx.a_I(5) - 1) {
            e.a(var2.a_bx, 0, 0);
            var2.q_Z = true;
          }
        }
      }

      if (this.a_I == 2) {
        bn var28 = this;
        bn var77 = this;

        for (int var86 = 10; var86 < var77.e_AAI.length; var86++) {
          if (var77.a_Ae[var86] != null && var77.a_Ae[var86].f_I != 128 && var77.a_Ae[var86].a_B == 0) {
            var77.e_AAI[var86][0] = var86;
            if (var77.a_Ae[var86].b_B != 0) {
              byte[] var43 = cg.b(var77.a_AB(0, 20), var86);
              var77.e_AAI[var86][1] = 1;
              if (var43.length > 1) {
                int var25 = k.a(0, var43.length);
                var77.e_AAI[var86][2] = var43[var25];
              } else {
                var77.e_AAI[var86][2] = var43[0];
              }
            } else if (var77.a_Ae[var86].c_B > 0) {
              byte[] var42 = var77.a_AB(0, 10);
              var77.e_AAI[var86][1] = 1;
              if (var42.length > 1) {
                int var24 = k.a(0, var42.length);
                var77.e_AAI[var86][2] = var42[var24];
              } else {
                var77.e_AAI[var86][2] = var42[0];
              }
            } else {
              byte var113 = 0;
              int var115 = k.b(1, 100);
              byte var18;
              byte var37;
              if (var77.a_Ae[var86].m_I > var77.a_Ae[var86].a_AI[3] / 2) {
                var37 = 50;
                var18 = 100;
              } else {
                var37 = 1;
                var18 = 70;
                var113 = 100;
              }

              if (var115 < var37) {
                byte[] var23 = var77.a_AB(0, 10);
                var77.e_AAI[var86][1] = 1;
                if (var23.length > 1) {
                  var37 = k.a(0, var23.length);
                  var77.e_AAI[var86][2] = var23[var37];
                } else {
                  var77.e_AAI[var86][2] = var23[0];
                }
              } else if (var115 >= var18) {
                if (var115 < var113) {
                  var77.e_AAI[var86][1] = 2;
                }
              } else if (var77.a_Ae[var86].c_AS == null) {
                byte[] var22 = var77.a_AB(0, 10);
                var77.e_AAI[var86][1] = 1;
                if (var22.length > 1) {
                  var37 = k.a(0, var22.length);
                  var77.e_AAI[var86][2] = var22[var37];
                } else {
                  var77.e_AAI[var86][2] = var22[0];
                }
              } else {
                var18 = k.a(0, var77.a_Ae[var86].c_AS.length);
                var77.e_AAI[var86][3] = var77.a_Ae[var86].c_AS[var18];
                short var38;
                int var117 = o.c_I(var38 = var77.a_Ae[var86].c_AS[var18]);
                e var132 = var77.a_Ae[var86];
                var18 = var18;
                int var124 = var117;
                int var129 = var77.a_Ae[var86].n_I;
                e var128 = var132;
                var38 = -1;

                for (int var50 = 0; var50 < var128.b_AS[var18]; var50++) {
                  if (d.a_AAS[var124][var50] <= var129) {
                    var38 = var50;
                  }
                }

                if (var38 != -1) {
                  var18 = var38 + 1;
                  var77.e_AAI[var86][1] = 4;
                  var77.e_AAI[var86][4] = var18;
                  if (d.b_AS[var117] == 0) {
                    var77.e_AAI[var86][2] = var86;
                  } else if (d.b_AS[var117] == 1) {
                    byte[] var119;
                    if (d.c_AS[var117] == 1 && (var119 = var77.a_AB(10, 20)) != null) {
                      if (var119.length > 1) {
                        var124 = k.a(0, var119.length);
                        var77.e_AAI[var86][2] = var119[var124];
                      } else {
                        var77.e_AAI[var86][2] = var119[0];
                      }
                    }
                  } else {
                    byte[] var120;
                    if (d.b_AS[var117] == 2 && d.c_AS[var117] == 1 && (var120 = var77.a_AB(0, 10)) != null) {
                      if (var120.length > 1) {
                        var124 = k.a(0, var120.length);
                        var77.e_AAI[var86][2] = var120[var124];
                      } else {
                        var77.e_AAI[var86][2] = var120[0];
                      }
                    }
                  }
                } else {
                  byte[] var121 = var77.a_AB(0, 10);
                  var77.e_AAI[var86][1] = 1;
                  if (var121.length > 1) {
                    var124 = k.a(0, var121.length);
                    var77.e_AAI[var86][2] = var121[var124];
                  } else {
                    var77.e_AAI[var86][2] = var121[0];
                  }
                }
              }
            }
          }
        }

        var77 = this;
        int var87 = 0;

        boolean var133;
        while (true) {
          if (var87 < var77.e_AAI.length) {
            int var44;
            if ((var44 = var77.e_AAI[var87][1]) != 2 && var44 != 6) {
              var87++;
              continue;
            }

            var133 = true;
            break;
          }

          var133 = false;
          break;
        }

        if (var133) {
          for (int var45 = 0; var45 < var28.e_AAI.length; var45++) {
            int var51;
            if ((var51 = var28.e_AAI[var45][1]) == 2) {
              var28.e_AAI[var45][1] = -1;
              var28.a_Ae[var28.e_AAI[var45][0]].p_Z = true;
              var28.a_Ae[var28.e_AAI[var45][0]].f_I = 3;
            } else if (var51 == 6) {
              var28.e_AAI[var45][1] = -1;
              if (var28.a_Ae[var28.e_AAI[var45][2]] == null) {
                if (var45 > 9) {
                  if (var28.a_Ae[var45].b_I > 9) {
                    var28.a_Ae[var45].b_I = var45;
                  }
                } else if (var45 < 9) {
                  var28.a_Ae[var45].b_I = var45;
                }
              } else {
                var28.a_Ae[var45].b_I = var28.e_AAI[var45][2];
              }

              e var134 = var28.a_Ae[var45];
              byte var88 = 9;
              var134.f_I = 9;
              var28.a_Ae[var45].a(5, 0);
              o.a(var28.a_Ae[var28.a_Ae[var45].b_I], var28.f_AAS[var28.a_ab.b_I()][0]);
            }
          }

          var28.a_I = 6;
        } else {
          this.a_I = 3;
        }

        var28.j_I = var28.a_AAS[0][0];
      } else {
        if (this.a_I == 6) {
          bn var26 = this;
          int var31 = 0;

          boolean var131;
          while (true) {
            if (var31 >= var26.a_Ae.length) {
              var131 = true;
              break;
            }

            if (var26.a_Ae[var31] != null && !var26.a_Ae[var31].f_Z && var26.a_Ae[var31].f_I != 0) {
              var131 = false;
              break;
            }

            var31++;
          }

          if (var131) {
            this.a_I = 3;
            return;
          }
        } else if (this.a_I == 3) {
          bn var27 = this;
          if (this.a_Ae[0].f_I == 128) {
            this.p();
            this.n();
            this.e_V();
            if (!da.a_da.a_cd.a_ci.c_Z) {
              if (!df.d_Z) {
                this.e_V();
                return;
              }

              df.a("/bin/smsmenu.bin");
              da.a_da.a_cd.a_df.b(7);
              this.b_I = 1;
            }
          } else {
            if (this.e_Z()) {
              this.p();
              this.h();
              return;
            }

            if (this.d_I >= this.a_AAS.length) {
              this.p();
              this.g();
              return;
            }

            for (int var32 = 0; var32 < var27.a_Ae.length; var32++) {
              if (var27.a_Ae[var32] != null && var27.a_Ae[var32].f_Z) {
                var27.a_Ae[var32].a_bx.j_Z = true;

                for (int var46 = 1; var46 < var27.e_AAI[var32].length; var46++) {
                  var27.e_AAI[var32][var46] = -1;
                }
              }
            }

            if (var27.a_Ae[var27.j_I] != null) {
              int var33;
              if ((var33 = var27.e_AAI[var27.j_I][1]) == 1 && var27.a_Ae[var27.j_I].a_B == 0) {
                if (var27.e_AAI[var27.j_I][2] != -1) {
                  if (var27.a_Ae[var27.j_I].b_B != 0) {
                    byte[] var73;
                    if ((var73 = cg.b(var27.a_AB(0, var27.a_Ae.length), var27.j_I)).length > 1) {
                      int var85 = k.a(0, var73.length);
                      var27.e_AAI[var27.j_I][2] = var73[var85];
                    } else {
                      var27.e_AAI[var27.j_I][2] = var73[0];
                    }
                  }

                  var27.a_Ae[var27.j_I].b_I = var27.b_I(var27.e_AAI[var27.j_I][2]);
                  var27.e_AAI[var27.j_I][2] = var27.a_Ae[var27.j_I].b_I;
                  bn var111 = var27;
                  byte[] var8 = null;
                  byte var15;
                  byte var35;
                  if (var27.j_I > 9) {
                    var15 = 10;
                    var35 = 20;
                  } else {
                    var15 = 0;
                    var35 = 10;
                  }

                  for (int var9 = var15; var9 < var35; var9++) {
                    if (var111.a_Ae[var111.j_I].b_B == 0
                      && var111.e_AAI[var111.j_I][0] != var111.e_AAI[var9][0]
                      && var111.a_Ae[var111.e_AAI[var9][0]] != null
                      && var111.a_Ae[var111.e_AAI[var9][0]].b_B == 0
                      && var111.a_Ae[var111.e_AAI[var9][0]].a_B == 0
                      && var111.e_AAI[var111.j_I][1] == 1
                      && var111.e_AAI[var9][1] == 1
                      && var111.e_AAI[var111.j_I][2] != -1
                      && var111.e_AAI[var111.j_I][2] == var111.e_AAI[var9][2]) {
                      int var10001 = var111.e_AAI[var111.j_I][0];
                      int var11 = var111.e_AAI[var9][0];
                      int var10 = var10001;
                      if (var111.a_Ae[var10] == null || var111.a_Ae[var10].f_Z || var111.a_Ae[var11] == null || var111.a_Ae[var11].f_Z
                        ? false
                        : var111.a_Ae[var10].l_I >= var111.a_Ae[var11].l_I - 150 && var111.a_Ae[var10].l_I <= var111.a_Ae[var11].l_I + 150) {
                        if (!cg.a_Z(var8, var111.e_AAI[var111.j_I][0])) {
                          var8 = cg.a_AB(var8, var111.e_AAI[var111.j_I][0]);
                        }

                        if (!cg.a_Z(var8, var111.e_AAI[var9][0])) {
                          var8 = cg.a_AB(var8, var111.e_AAI[var9][0]);
                        }
                      }
                    }
                  }

                  byte[] var48 = var8;
                  if (var8 != null && var27.a_Ae[var27.j_I].b_B == 0) {
                    var27.a_Ae[var27.j_I].a_AB = var8;
                    boolean var116 = false;
                    var35 = var27.a_Ae[var27.j_I].b_I;
                    var15 = var27.j_I;
                    var8 = var8;
                    var111 = var27;
                    if (a(var27.a_Ae[var15], var27.a_Ae[var35], 0)) {
                      var15 = 0;

                      for (int var118 = 0; var118 < var8.length; var118++) {
                        int var122 = 200 + a(var111.a_Ae[var8[var118]].d_AB, var111.a_Ae[var35].d_AB);
                        int var12 = k.b(0, 5);
                        int var13 = var111.a_Ae[var8[var118]].j_I;
                        var122 = var111.a_Ae[var8[var118]].j_I * var122 / 100 - var111.a_Ae[var35].k_I + var12;
                        var15 += var122;
                      }

                      if (var111.a_Ae[var35].p_Z) {
                        var15 /= 5;
                      }

                      if (var15 < 10) {
                        var15 = 10;
                      }

                      var111.a_Ae[var35].m_I -= var15 * 110 / 100;
                    } else {
                      var27.a_Ae[var35].o_Z = true;
                    }

                    for (int var76 = 0; var76 < var48.length; var76++) {
                      if (!var27.a_Ae[var27.e_AAI[var48[var76]][0]].f_Z) {
                        var27.a_Ae[var27.e_AAI[var48[var76]][0]].b_I = var27.e_AAI[var48[var76]][2];
                        var27.a_Ae[var27.e_AAI[var48[var76]][0]].a_m = var27.a_m(var27.e_AAI[var48[var76]][2], var27.e_AAI[var48[var76]][0]);
                        var27.a_Ae[var27.e_AAI[var48[var76]][0]].i_Z = true;
                        var27.a_Ae[var27.e_AAI[var48[var76]][0]].f_I = 6;
                        var27.a_Ae[var27.e_AAI[var48[var76]][0]].a_bx.a_V(1);
                        var27.e_AAI[var27.e_AAI[var48[var76]][0]][1] = -1;
                        var27.e_AAI[var27.e_AAI[var48[var76]][0]][2] = -1;
                      }
                    }

                    var27.a_Ae[var27.j_I].a_AB = cg.b(var27.a_Ae[var27.j_I].a_AB, var27.a_Ae[var27.j_I].a_I);
                  } else {
                    var27.a_Ae[var27.j_I].b_I = var27.e_AAI[var27.j_I][2];
                    var27.a_Ae[var27.j_I].a_m = var27.a_m(var27.a_Ae[var27.j_I].b_I, var27.j_I);
                    if (var27.j_I < 9) {
                      if (k.a(1, 100) <= var27.a_Ae[var27.j_I].d_S) {
                        var27.a_Ae[var27.j_I].m_Z = true;
                      }

                      if (var27.a_Ae[var27.j_I].k_Z) {
                        var27.a_Ae[var27.j_I].m_Z = true;
                      }
                    }

                    var27.a_V(var27.j_I, var27.a_Ae[var27.j_I].b_I);
                    if (var27.a_Ae[var27.j_I].b_B != 0) {
                      if (var27.j_I > 9) {
                        if (var27.a_Ae[var27.j_I].b_I > 9) {
                          var27.a_Ae[var27.j_I].e_Z = true;
                          byte[] var74;
                          (var74 = new byte[1])[0] = (byte)var27.a_Ae[var27.j_I].b_I;
                          var27.a_Ae[var27.j_I].a_Abx = new bx[1];
                          var27.a_Ae[var27.j_I].a_Abx[0] = new bx();
                          var27.a_Ae[var27.j_I].a_Abx[0].a("/ft/gongji1.av", 1);
                          var27.a_Ae[var27.j_I].b_AB = var74;
                          var27.a_Ae[var27.j_I].a(4, 0);
                          var27.a_Ae[var27.j_I].f_I = 5;
                        } else {
                          var27.a_Ae[var27.j_I].f_I = 6;
                          var27.a_Ae[var27.j_I].a_bx.a_V(1);
                        }
                      } else if (var27.a_Ae[var27.j_I].b_I < 10) {
                        byte[] var75;
                        (var75 = new byte[1])[0] = (byte)var27.a_Ae[var27.j_I].b_I;
                        var27.a_Ae[var27.j_I].a_Abx = new bx[1];
                        var27.a_Ae[var27.j_I].a_Abx[0] = new bx();
                        var27.a_Ae[var27.j_I].a_Abx[0].a("/ft/gongji1.av");
                        var27.a_Ae[var27.j_I].b_AB = var75;
                        var27.a_Ae[var27.j_I].e_Z = true;
                        var27.a_Ae[var27.j_I].a(4, 0);
                        var27.a_Ae[var27.j_I].f_I = 5;
                      } else {
                        var27.a_Ae[var27.j_I].f_I = 6;
                        var27.a_Ae[var27.j_I].a_bx.a_V(1);
                      }
                    } else {
                      var27.a_Ae[var27.j_I].f_I = 6;
                      var27.a_Ae[var27.j_I].a_bx.a_V(1);
                    }

                    var27.e_AAI[var27.j_I][1] = -1;
                    var27.e_AAI[var27.j_I][2] = -1;
                  }
                }
              } else if (var33 == 4 && var27.a_Ae[var27.j_I].a_B == 0 && var27.e_AAI[var27.j_I][3] != -1) {
                if (var27.a_Ae[var27.j_I].b_B != 0) {
                  byte[] var72;
                  if ((var72 = cg.b(var27.a_AB(0, var27.a_Ae.length), var27.j_I)).length > 1) {
                    int var82 = k.a(0, var72.length);
                    var27.e_AAI[var27.j_I][2] = var72[var82];
                  } else {
                    var27.e_AAI[var27.j_I][2] = var72[0];
                  }

                  var27.a_Ae[var27.j_I].b_I = var27.b_I(var27.e_AAI[var27.j_I][2]);
                  var27.a_V(var27.j_I, var27.a_Ae[var27.j_I].b_I);
                  if (var27.j_I > 9) {
                    if (var27.a_Ae[var27.j_I].b_I > 9) {
                      var27.a_Ae[var27.j_I].e_Z = true;
                      byte[] var83;
                      (var83 = new byte[1])[0] = (byte)var27.a_Ae[var27.j_I].b_I;
                      var27.a_Ae[var27.j_I].a_Abx = new bx[1];
                      var27.a_Ae[var27.j_I].a_Abx[0] = new bx();
                      var27.a_Ae[var27.j_I].a_Abx[0].a("/ft/gongji1.av", 1);
                      var27.a_Ae[var27.j_I].b_AB = var83;
                      var27.a_Ae[var27.j_I].a(4, 0);
                      var27.a_Ae[var27.j_I].f_I = 5;
                    } else {
                      var27.a_Ae[var27.j_I].f_I = 6;
                      var27.a_Ae[var27.j_I].a_bx.a_V(1);
                    }
                  } else if (var27.a_Ae[var27.j_I].b_I < 10) {
                    byte[] var84;
                    (var84 = new byte[1])[0] = (byte)var27.a_Ae[var27.j_I].b_I;
                    var27.a_Ae[var27.j_I].a_Abx = new bx[1];
                    var27.a_Ae[var27.j_I].a_Abx[0] = new bx();
                    var27.a_Ae[var27.j_I].a_Abx[0].a("/ft/gongji1.av", 1);
                    var27.a_Ae[var27.j_I].b_AB = var84;
                    var27.a_Ae[var27.j_I].e_Z = true;
                    var27.a_Ae[var27.j_I].a(4, 0);
                    var27.a_Ae[var27.j_I].f_I = 5;
                  } else {
                    var27.a_Ae[var27.j_I].f_I = 6;
                    var27.a_Ae[var27.j_I].a_bx.a_V(1);
                  }
                } else {
                  int var47 = o.c_I(var27.e_AAI[var27.j_I][3]);
                  if (d.b_AS[var47] == 1) {
                    if (var27.e_AAI[var27.j_I][2] != -1 && var27.a_Ae[var27.e_AAI[var27.j_I][2]] != null && !var27.a_Ae[var27.e_AAI[var27.j_I][2]].f_Z) {
                      var27.a_Ae[var27.j_I].b_I = var27.e_AAI[var27.j_I][2];
                    } else {
                      var27.a_Ae[var27.j_I].b_I = var27.j_I;
                    }
                  } else {
                    var27.a_Ae[var27.j_I].b_I = var27.e_AAI[var27.j_I][2];
                  }

                  var27.a_Ae[var27.j_I].c_I = var27.e_AAI[var27.j_I][3];
                  var27.a(var27.a_Ae[var27.j_I], var27.e_AAI[var27.j_I][3], var27.e_AAI[var27.j_I][4]);
                  if (var27.a_Ae[var27.j_I].c_I == 39) {
                    var27.a_Ae[var27.j_I].n_I -= 30;
                    int var107 = var27.a_Ae[var27.j_I].b_I;
                    da.a_da.a_cd.a_bn.u_I = var27.a_Ae[var107].a_I;
                    var107 = var27.a_Ae[var27.j_I].b_I;
                    da.a_da.a_cd.a_bn.f_bx = k.a(var27.a_Ae[var107].d_I, true);

                    for (int var79 = 0; var79 < 5; var79++) {
                      var107 = var27.a_Ae[var27.j_I].b_I;
                      byte[] var70 = cg.b_AB(var27.a_AAString[var27.a_Ae[var107].d_I - 1][4 + var79 * 2], "|");
                      var27.a_AB = cg.a_AB(var27.a_AB, var70[0]);
                    }
                  }

                  if (d.b_AS[var47] == 1) {
                    var27.a_Ae[var27.j_I].a(5, 0);
                    var27.a_Ae[var27.j_I].f_I = 17;
                  } else if (var27.a_Ae[var27.j_I].a_I == 0) {
                    var27.a_Ae[var27.j_I].f_I = 5;
                  } else {
                    var27.a_Ae[var27.j_I].e_Z = true;
                    var27.a_Ae[var27.j_I].a(4, 0);
                    var27.a_Ae[var27.j_I].f_I = 5;
                  }

                  if (var27.a_Ae[var27.j_I].c_I != 39 && var27.j_I < 10) {
                    int var71 = -1;

                    for (int var80 = 0; var80 < var27.a_Ae[var27.j_I].a_Ad.length; var80++) {
                      if (var27.a_Ae[var27.j_I].a_Ad[var80].a_S == var27.e_AAI[var27.j_I][3]) {
                        var71 = var80;
                        break;
                      }
                    }

                    if (var71 != -1 && var27.a_Ae[var27.j_I].a_Ad[var71].a_B <= var27.a_Ae[var27.j_I].a_Ad[var71].b_B) {
                      if (df.b_Z) {
                        var27.a_Ae[var27.j_I].a_Ad[var71].a_I = var27.a_Ae[var27.j_I].a_Ad[var71].a_I + 2 * var27.e_AAI[var27.j_I][4];
                      } else {
                        var27.a_Ae[var27.j_I].a_Ad[var71].a_I = var27.a_Ae[var27.j_I].a_Ad[var71].a_I + var27.e_AAI[var27.j_I][4];
                      }

                      boolean var81 = false;
                      if (var27.a_Ae[var27.j_I].a_Ad[var71].a_I >= var27.a_Ae[var27.j_I].a_Ad[var71].b_I) {
                        d var110;
                        if ((var110 = var27.a_Ae[var27.j_I].a_Ad[var71]).a_B < var110.b_B) {
                          var81 = true;
                        } else {
                          var27.a_Ae[var27.j_I].a_Ad[var71].a_I = var27.a_Ae[var27.j_I].a_Ad[var71].b_I;
                        }

                        if (var81 && var27.a_Ae[var27.j_I].a_Ad[var71].a_B < 10) {
                          var27.a_Ae[var27.j_I].a_Ad[var71].a_B++;
                          var27.a_Ae[var27.j_I].a_Ad[var71].a_I = 0;
                          var27.a_Ae[var27.j_I].a_Ad[var71].b_I = (var27.a_Ae[var27.j_I].a_Ad[var71].a_B + 1) * (var27.a_Ae[var27.j_I].a_Ad[var71].a_B + 1)
                            << 1;
                          int var14 = o.c_I(var27.a_Ae[var27.j_I].a_Ad[var71].a_S);
                          String var34 = d.a_AString[var14]
                            + "  "
                            + (var27.a_Ae[var27.j_I].a_Ad[var71].a_B - 1)
                            + " cấp->"
                            + var27.a_Ae[var27.j_I].a_Ad[var71].a_B
                            + " cấp";
                          da.a_da.a(var34, 10);
                        }
                      }
                    }
                  }

                  var27.e_AAI[var27.j_I][1] = -1;
                  var27.e_AAI[var27.j_I][2] = -1;
                }
              }

              var27.e_AAI[var27.j_I][1] = -1;
              var27.e_AAI[var27.j_I][2] = -1;
              if (var27.a_Ae[var27.j_I].f_I == 0 && var27.e_AAI[var27.j_I][1] == -1) {
                var27.d_I++;
                var27.s_I = 0;
                if (var27.d_I < var27.a_AAS.length) {
                  if (!var27.a_Ae[var27.a_AAS[var27.d_I][0]].f_Z) {
                    var27.j_I = var27.a_AAS[var27.d_I][0];
                    return;
                  }

                  if (var27.a_Ae[0].f_I == 128) {
                    var27.p();
                    var27.n();
                    if (da.a_da.a_cd.a_ci.c_Z) {
                      var27.e_V();
                      return;
                    }

                    if (!df.d_Z) {
                      var27.e_V();
                      return;
                    }

                    df.a("/bin/smsmenu.bin");
                    da.a_da.a_cd.a_df.b(7);
                    var27.b_I = 1;
                  } else if (var27.e_Z()) {
                    var27.p();
                    var27.h();
                    return;
                  }
                }
              } else if (var27.a_Ae[var27.j_I].f_I == 128 || var27.a_Ae[var27.j_I].f_Z) {
                if (var27.a_Ae[0].f_I != 128 && !var27.a_Ae[0].f_Z) {
                  if (var27.e_Z()) {
                    var27.p();
                    var27.h();
                    return;
                  }

                  var27.d_I++;

                  for (int var49 = 0; var49 < var27.a_Ae.length; var49++) {
                    if (var27.a_Ae[var49] != null && var27.a_Ae[var49].o_Z) {
                      var27.a_Ae[var49].o_Z = false;
                    }
                  }

                  if (var27.d_I < var27.a_AAS.length && !var27.a_Ae[var27.a_AAS[var27.d_I][0]].f_Z) {
                    var27.j_I = var27.a_AAS[var27.d_I][0];
                  }
                } else {
                  var27.p();
                  var27.n();
                  if (da.a_da.a_cd.a_ci.c_Z) {
                    var27.e_V();
                    return;
                  }

                  if (!df.d_Z) {
                    var27.e_V();
                    return;
                  }

                  df.a("/bin/smsmenu.bin");
                  da.a_da.a_cd.a_df.b(7);
                  var27.b_I = 1;
                }
              }
            }
          }
        }
      }
    }
  }

  private boolean c_Z() {
    for (int var1 = 0; var1 < 10; var1++) {
      if (this.a_Ae[var1] != null && this.a_Ae[var1].a_AI[3] > 0 && this.a_Ae[var1].a_B == 0) {
        return false;
      }
    }

    return true;
  }

  private boolean d_Z() {
    for (int var1 = 0; var1 < 10; var1++) {
      if (this.a_Ae[var1] != null && this.a_Ae[var1].a_AI[3] > 0 && this.a_Ae[var1].b_B == 0) {
        return false;
      }
    }

    return true;
  }

  private int a_I(int var1) {
    byte var2 = -1;
    if (this.a_Ae[var1].a_B != 0) {
      var2 = 0;
    } else if (this.a_Ae[var1].b_B > 0) {
      var2 = 1;
    }

    return var2;
  }

  private void t() {
    for (int var1 = 0; var1 < this.a_Ae.length / 2; var1++) {
      if (this.a_Ae[var1] != null) {
        for (int var2 = 0; var2 < this.a_Abl.length; var2++) {
          if (this.a_Abl[var2] != null && this.a_Abl[var2].h_S == this.a_Ae[var1].d_I && this.a_Abl[var2].c_S == this.a_Ae[var1].a_S) {
            for (int var3 = 0; var3 < this.a_Abl[var1].a_AI.length; var3++) {
              this.a_Abl[var2].a_AI[var3] = this.a_Ae[var1].a_AI[var3];
            }
          }
        }
      }
    }
  }

  public final boolean b_Z() {
    return this.b_AS != null || o.a_AS != null;
  }

  private static void a(bl[] var0) {
    if (da.a_da.a_cd.a_ci.c_Z) {
      for (int var1 = 0; var1 < var0.length; var1++) {
        if (var0[var1] != null) {
          var0[var1].a_AI[3] = var0[var1].a_AI[10];
          var0[var1].a_AI[5] = var0[var1].a_AI[11];
        }
      }
    }

    o.b(var0);
    o.c(var0);
    o.a(o.a_Abl);
    da.a_da.a_cd.l();
    if (da.a_da.a_cd.p_Z) {
      da.a_da.a_cd.p_Z = false;

      for (int var5 = 0; da.a_da.a_cd.a_bj.a_Acz != null && var5 < da.a_da.a_cd.a_bj.a_Acz.length; var5++) {
        if (da.a_da.a_cd.a_bj.a_Acz[var5] != null && da.a_da.a_cd.a_bj.a_Acz[var5].i_B == 1 && ((bl)da.a_da.a_cd.a_bj.a_Acz[var5]).h_S == o.c_AAS[0][0]) {
          ((bl)da.a_da.a_cd.a_bj.a_Acz[var5]).j_Z = false;
          break;
        }
      }

      int var6 = k.b(0, 100);

      for (int var2 = 0; o.n_AString != null && var2 < o.n_AString.length; var2++) {
        da.a_da.a_cd.a_AI = null;
        da.a_da.a_cd.a_AI = cg.b_AI(o.n_AString[var2], ",");
        if (da.a_da.a_cd.a_AI[3] >= var6) {
          if (da.a_da.a_cd.a_AI[0] == 4) {
            var6 = (short)da.a_da.a_cd.a_AI[1];
            String[][] var3 = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
            String var4 = var3[--var6][0];
            da.a_da.a_cd.c_String = var4;
          }
          break;
        }
      }

      da.a_da.a_cd.b_B = 8;
    }

    if (at.f_Z) {
      da.a_da.a_cd.b();
    } else {
      cd.m();
    }

    da.a_da.a_cd.a_ci.a(0);
  }

  private void u() {
    for (int var1 = 0; var1 < this.a_Ae.length / 2; var1++) {
      if (this.a_Ae[var1] != null) {
        for (int var2 = 0; var2 < this.a_Abl.length; var2++) {
          if (this.a_Abl[var2] != null && this.a_Ae[var1].d_I == this.a_Abl[var2].h_S) {
            for (int var3 = 0; this.a_Ae[var1].a_Ad != null && var3 < this.a_Ae[var1].a_Ad.length; var3++) {
              bl var10000 = this.a_Abl[var2];
              d var5 = this.a_Ae[var1].a_Ad[var3];
              bl var4 = var10000;

              for (int var6 = 0; var6 < var4.a_AS.length; var6++) {
                if (var4.a_AS[var6] == var5.a_S) {
                  var4.a_Ad[var6].a_B = var5.a_B;
                  var4.a_Ad[var6].a_I = var5.a_I;
                  var4.a_Ad[var6].b_I = var5.b_I;
                }
              }
            }
          }
        }
      }
    }
  }

  private void v() {
    for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null) {
        this.a_Ae[var1].a_AI[3] = this.a_Ae[var1].m_I;
        if (this.a_Ae[var1].a_AI[3] <= 0) {
          this.a_Ae[var1].a_AI[3] = 0;
          this.a_Ae[var1].m_I = 0;
          this.a_Ae[var1].f_Z = true;
          this.a_Ae[var1].f_I = 128;
          this.a_Ae[var1].a_bx.j_Z = false;
          this.a_Ae[var1].o_Z = false;
        }

        this.a_Ae[var1].a_AI[5] = this.a_Ae[var1].n_I;
        if (this.a_Ae[var1].a_AI[5] > this.a_Ae[var1].a_AI[11]) {
          this.a_Ae[var1].a_AI[5] = this.a_Ae[var1].a_AI[11];
        }

        if (this.a_Ae[var1].a_AI[3] > this.a_Ae[var1].a_AI[10]) {
          this.a_Ae[var1].a_AI[3] = this.a_Ae[var1].a_AI[10];
        }
      }
    }
  }

  public final void g() {
    this.h_Z = false;
    this.s_I = 0;
    this.j();

    for (int var1 = 0; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null) {
        this.a_Ae[var1].a_AI[3] = this.a_Ae[var1].m_I;
        this.a_Ae[var1].a_AI[5] = this.a_Ae[var1].n_I;
        this.a_Ae[var1].b_AB = null;
        this.a_Ae[var1].q_Z = false;
        this.a_Ae[var1].e_Z = false;
        this.a_Ae[var1].c_Z = false;
        this.a_Ae[var1].d_Z = false;
        this.a_Ae[var1].a_AB = null;
        this.a_Ae[var1].i_Z = false;
        this.a_Ae[var1].c_I = 0;
        this.a_Ae[var1].m_Z = false;
        this.a_Ae[var1].a_Abx = null;
        this.t_I = 0;
      }
    }

    if (this.e_Z()) {
      this.b_V();
      this.r();
      this.c_V(142, this.h_I - 142);
      this.p();
    } else if (!this.a_Ae[0].f_Z && this.a_Ae[0].f_I != 128) {
      bn var6 = this;

      for (int var2 = 0; var2 < var6.a_Ae.length; var2++) {
        if (var6.a_Ae[var2] != null) {
          if (var6.a_Ae[var2].o_Z) {
            var6.a_Ae[var2].o_Z = false;
          }

          if (!a_Z(var6.a_Ae[var2])) {
            var6.a_Ae[var2].a_Z = false;
          }

          if (var6.a_Ae[var2].f_I == 3) {
            var6.a_Ae[var2].a_bx.j_Z = true;
            var6.a_Ae[var2].f_I = 0;
          }

          if (var6.a_Ae[var2].p_Z) {
            var6.a_Ae[var2].p_Z = false;
          }

          if (var6.a_Ae[var2].a_AI[3] > 0) {
            if (var6.a_Ae[var2].g_AB[0] > 0) {
              var6.a_Ae[var2].g_AB[0]--;
              if (var6.a_Ae[var2].g_AB[0] == 0) {
                var6.a_Ae[var2].j_I = var6.a_Ae[var2].j_I - var6.a_Ae[var2].a_AI[12] * var6.a_Ae[var2].g_AB[1] / 100;
                var6.a_Ae[var2].g_AB = new byte[2];
              }

              if (var6.a_Ae[var2].j_I < 0) {
                var6.a_Ae[var2].j_I = 0;
              }
            }

            if (var6.a_Ae[var2].f_AB[0] > 0) {
              int var3;
              if ((var3 = var6.a_Ae[var2].a_AI[10] * var6.a_Ae[var2].f_AB[1] / 100) <= 0) {
                var3 = 1;
              }

              var6.a_Ae[var2].m_I -= var3;
              var6.a_Ae[var2].f_AB[0]--;
              if (var6.a_Ae[var2].f_AB[0] == 0) {
                var6.a_Ae[var2].f_AB = new byte[2];
              }
            }

            if (var6.a_Ae[var2].a_B > 0) {
              var6.a_Ae[var2].a_B--;
            }

            if (var6.a_Ae[var2].b_B > 0) {
              var6.a_Ae[var2].b_B--;
            }

            if (var6.a_Ae[var2].i_AB[0] > 0) {
              var6.a_Ae[var2].i_AB[0]--;
              if (var6.a_Ae[var2].i_AB[0] == 0) {
                var6.a_Ae[var2].k_I = var6.a_Ae[var2].k_I + var6.a_Ae[var2].a_AI[13] * var6.a_Ae[var2].i_AB[1] / 100;
                var6.a_Ae[var2].i_AB = new byte[2];
              }
            }

            if (var6.a_Ae[var2].h_AB[0] > 0) {
              var6.a_Ae[var2].h_AB[0]--;
              if (var6.a_Ae[var2].h_AB[0] == 0) {
                var6.a_Ae[var2].h_AB = new byte[2];
              } else {
                var6.a_Ae[var2].j_I = var6.a_Ae[var2].j_I + var6.a_Ae[var2].a_AI[12] * var6.a_Ae[var2].h_AB[1] / 100;
                var6.a_Ae[var2].k_I = var6.a_Ae[var2].k_I + var6.a_Ae[var2].a_AI[13] * var6.a_Ae[var2].h_AB[1] / 100;
              }
            }

            if (var6.a_Ae[var2].c_B > 0) {
              var6.a_Ae[var2].c_B--;
            }
          } else if (var6.a_Ae[var2].a_AI[3] <= 0) {
            var6.a_Ae[var2].f_I = 128;
            var6.a_Ae[var2].f_Z = true;
            var6.a_Ae[var2].a_bx.j_Z = false;
          }
        }
      }

      this.c_B++;
      if (da.a_da.a_cd.r_Z && this.c_B < 4) {
        this.b_AString = null;
        String var23 = cg.a(cg.a(cg.a_String("/bin/enemy.bin"), "teachMode:", "teachModeEnd"), "teach" + this.c_B + ":", "teach" + this.c_B + "End");
        this.b_AString = cg.b_AString(var23, "\r\n");
        if (this.b_AString != null) {
          this.d_B = 1;
          this.a(this.b_AString);
        }

        this.o();
        this.m();
        this.c_I = this.b_I(10, 20);
        this.d_I = 0;
      } else {
        this.o();
        this.m();
        if (this.e_Z) {
          this.e_Z = false;
        }

        this.c_I = this.b_I(10, 20);
        this.d_I = 0;
        if (!this.c_Z()) {
          if (this.d_Z()) {
            byte[] var8 = this.a_AB(0, 10);
            byte[] var14 = this.a_AB(0, this.a_Ae.length);

            for (int var22 = 0; var22 < var8.length; var22++) {
              this.e_AAI[var8[var22]][1] = 1;
              byte[] var30;
              if ((var30 = cg.b(var14, var8[var22])).length > 1) {
                int var31 = k.a(0, var30.length);
                this.e_AAI[var8[var22]][2] = var30[var31];
              } else {
                this.e_AAI[var8[var22]][2] = var30[0];
              }
            }

            this.a_I = 2;
            return;
          }

          if (this.a_Ae[0].a_B == 0 && this.a_Ae[0].b_B == 0) {
            this.i_I = 0;
            this.a_I = 0;
            this.g_I = 0;
            this.f_I = 0;
            return;
          }

          byte[] var7 = cg.b(this.a_AB(0, 10), 0);
          if (this.a_I(0) == 0) {
            if (var7.length != 1) {
              if (this.a_I(var7[0]) == -1) {
                this.i_I = var7[0];
                this.a_I = 0;
                this.g_I = 0;
                this.f_I = 7;
                return;
              }

              if (this.a_I(var7[0]) == 0 && this.a_I(var7[1]) == 1) {
                byte[] var12 = this.a_AB(0, this.a_Ae.length);
                this.e_AAI[var7[1]][1] = 1;
                byte[] var18 = cg.b(var12, var7[1]);
                int var26 = k.a(0, var18.length);
                this.e_AAI[var7[1]][2] = var18[var26];
                this.a_I = 2;
              } else if (this.a_I(var7[0]) == 1 && this.a_I(var7[1]) == 0) {
                byte[] var11 = this.a_AB(0, this.a_Ae.length);
                this.e_AAI[var7[0]][1] = 1;
                byte[] var17 = cg.b(var11, var7[0]);
                int var25 = k.a(0, var17.length);
                this.e_AAI[var7[0]][2] = var17[var25];
              } else if (this.a_I(var7[0]) == 1 && this.a_I(var7[1]) == 1) {
                byte[] var10 = this.a_AB(0, this.a_Ae.length);

                for (int var16 = 0; var16 < var7.length; var16++) {
                  this.e_AAI[var7[var16]][1] = 1;
                  byte[] var24;
                  if ((var24 = cg.b(var10, var7[var16])).length > 1) {
                    int var5 = k.a(0, var24.length);
                    this.e_AAI[var7[var16]][2] = var24[var5];
                  } else {
                    this.e_AAI[var7[var16]][2] = var24[0];
                  }
                }
              }

              this.a_I = 2;
              return;
            }

            if (this.a_I(var7[0]) == -1) {
              this.i_I = var7[0];
              this.a_I = 0;
              this.g_I = 0;
              this.f_I = 7;
              return;
            }

            if (this.a_I(var7[0]) != 1) {
              return;
            }

            byte[] var9 = this.a_AB(0, this.a_Ae.length);
            this.e_AAI[var7[0]][1] = 1;
            byte[] var15 = cg.b(var9, var7[0]);
            int var4 = k.a(0, var15.length);
            this.e_AAI[var7[0]][2] = var15[var4];
          } else {
            if (this.a_I(0) != 1) {
              return;
            }

            byte[] var13 = this.a_AB(0, this.a_Ae.length);
            this.e_AAI[0][1] = 1;
            byte[] var19 = cg.b(var13, 0);
            int var27 = k.a(0, var19.length);
            this.e_AAI[0][2] = var19[var27];
            if (var7.length == 1) {
              if (this.a_I(var7[0]) == -1) {
                this.i_I = var7[0];
                this.a_I = 0;
                this.g_I = 0;
                this.f_I = 7;
                return;
              }

              if (this.a_I(var7[0]) != 0) {
                return;
              }
            } else {
              if (this.a_I(var7[0]) == -1) {
                this.i_I = var7[0];
                this.a_I = 0;
                this.g_I = 0;
                this.f_I = 7;
                return;
              }

              if (this.a_I(var7[0]) == 0 && this.a_I(var7[1]) == 1) {
                this.e_AAI[var7[1]][1] = 1;
                var19 = cg.b(var13, var7[1]);
                var27 = k.a(0, var19.length);
                this.e_AAI[var7[1]][2] = var19[var27];
                this.a_I = 2;
              } else if (this.a_I(var7[0]) == 1 && this.a_I(var7[1]) == 0) {
                this.e_AAI[var7[0]][1] = 1;
                var19 = cg.b(var13, var7[0]);
                var27 = k.a(0, var19.length);
                this.e_AAI[var7[0]][2] = var19[var27];
              }
            }
          }
        }

        this.a_I = 2;
      }
    } else {
      this.p();
      this.n();
      this.e_V();
    }
  }

  private int b_I(int var1) {
    if (this.a_Ae[var1].f_Z) {
      if (var1 > 9) {
        int[] var4 = new int[this.a_I(10, this.a_Ae.length)];
        int var2 = 10;

        for (int var3 = 0; var2 < this.a_Ae.length; var2++) {
          if (this.a_Ae[var2] != null && !this.a_Ae[var2].f_Z) {
            var4[var3] = var2;
            var3++;
          }
        }

        if (var4.length > 1) {
          var2 = k.a(0, var4.length);
          var1 = var4[var2];
        } else {
          var1 = var4[0];
        }
      } else {
        int[] var5 = new int[this.a_I(0, 10)];
        int var7 = 0;

        for (int var9 = 0; var7 < 10; var7++) {
          if (this.a_Ae[var7] != null && !this.a_Ae[var7].f_Z) {
            var5[var9] = var7;
            var9++;
          }
        }

        if (var5.length > 1) {
          var7 = k.a(0, var5.length);
          var1 = var5[var7];
        } else {
          var1 = var5[0];
        }
      }
    }

    return var1;
  }

  private boolean e_Z() {
    for (int var1 = 10; var1 < this.a_Ae.length; var1++) {
      if (this.a_Ae[var1] != null && this.a_Ae[var1].f_I != 128) {
        return false;
      }
    }

    return true;
  }

  private static boolean a_Z(e var0) {
    if (var0.f_AB != null && var0.f_AB[0] > 0) {
      return true;
    } else {
      return var0.a_B > 0 ? true : var0.b_B > 0;
    }
  }

  private void a(e var1, int var2, int var3) {
    e[] var4 = null;
    if (var3 <= 0) {
      var3 = 1;
    }

    if (d.a_AString == null) {
      d.a();
    }

    int var5 = o.c_I(var2);
    if (d.c_AS[var5] == 1) {
      var4 = new e[1];
      var1.b_I = this.b_I(var1.b_I);
      var4[0] = this.a_Ae[var1.b_I];
    } else {
      if (this.c_Z) {
        this.c_Z = false;
      }

      if (d.b_AS[var5] == 1) {
        if (var1.a_I < 10) {
          var4 = new e[this.a_I(0, 10)];
          int var6 = 0;

          for (int var7 = 0; var7 < 10; var7++) {
            if (this.a_Ae[var7] != null && !this.a_Ae[var7].f_Z) {
              var4[var6] = this.a_Ae[var7];
              var6++;
            }
          }
        } else {
          var4 = new e[this.a_I(10, 20)];
          int var35 = 0;

          for (int var39 = 10; var39 < 20; var39++) {
            if (this.a_Ae[var39] != null && !this.a_Ae[var39].f_Z) {
              var4[var35] = this.a_Ae[var39];
              var35++;
            }
          }
        }
      } else if (d.b_AS[var5] == 2) {
        if (var1.a_I < 10) {
          var4 = new e[this.a_I(10, 20)];
          int var36 = 0;

          for (int var40 = 10; var40 < 20; var40++) {
            if (this.a_Ae[var40] != null && !this.a_Ae[var40].f_Z) {
              var4[var36] = this.a_Ae[var40];
              var36++;
            }
          }
        } else {
          var4 = new e[this.a_I(0, 10)];
          int var37 = 0;

          for (int var41 = 0; var41 < 10; var41++) {
            if (this.a_Ae[var41] != null && !this.a_Ae[var41].f_Z) {
              var4[var37] = this.a_Ae[var41];
              var37++;
            }
          }
        }
      }
    }

    byte[] var38 = null;

    for (int var42 = 0; var42 < var4.length; var42++) {
      var38 = cg.a_AB(var38, var4[var42].a_I);
    }

    var1.b_AB = var38;
    var1.a_Abx = null;
    String var43 = "/ft/" + o.b_AAString[var5][3];
    if (var38 != null && var38.length != 0) {
      var1.a_Abx = new bx[var38.length];

      for (int var8 = 0; var8 < var38.length; var8++) {
        var1.a_Abx[var8] = new bx();
        var1.a_Abx[var8].a(var43);
        byte var11 = var38[var8];
        int var9 = this.a_Ae[var11].c_m.a_I;
        var11 = var38[var8];
        int var10 = this.a_Ae[var11].c_m.b_I;
        var1.a_Abx[var8].d_S = (short)var9;
        var1.a_Abx[var8].e_S = (short)var10;
      }
    }

    short var44 = d.a_AAS[var5][var3 - 1];
    var1.n_I -= var44;
    if (var1.n_I < 0) {
      var1.n_I = 0;
    }

    if (var2 == 1) {
      this.a(var1, var4[0], var1.j_I, var4[0].k_I, 0, false);
      if (!var4[0].o_Z && !a_Z(var4[0])) {
        var4[0].a_B = (byte)k.b(2 + var3 / 4, 3 + var3 / 4);
      }
    } else if (var2 == 2) {
      var4[0].g_AB[0] = 3;
      var4[0].g_AB[1] = (byte)(25 + var3 * 5);
      var4[0].j_I = var4[0].a_AI[12] * (100 + var4[0].g_AB[1]) / 100;
    } else if (var2 == 3) {
      for (int var45 = 0; var45 < var4.length; var45++) {
        if (var4[var45].m_I > 0) {
          this.a(var1, var4[var45], var1.j_I * (62 + (var3 << 3)) / 100, var4[var45].k_I, 0, false);
        }
      }
    } else if (var2 == 4) {
      var1.d_B = (byte)(2 + (var3 - 1) / 4);
      if (at.d_Z) {
        System.out.println("mainRole.DUOCHONG " + var1.d_B);
      }

      this.a_V(var1.a_I, var4[0].a_I);
    } else if (var2 == 5) {
      this.a(var1, var4[0], var1.j_I * (110 + var3 * 10) / 100, var4[0].k_I, -20, false);
    } else if (var2 == 6) {
      this.a(var1, var4[0], var1.j_I, 0, 0, true);
      var1.i_AB[0] = 1;
      var1.i_AB[1] = (byte)(54 - var3 * 8 / 2);
      var1.k_I = var1.k_I - var1.k_I * var1.i_AB[1] / 100;
    } else if (var2 == 8) {
      this.a(var1, var4[0], var1.j_I * (140 + var3 * 10) / 100, var4[0].k_I, 0, false);
      if (!var4[0].o_Z && k.b(0, 100) < 15) {
        var4[0].m_I = 0;
      }
    } else if (var2 == 9) {
      for (int var46 = 0; var46 < var4.length; var46++) {
        if (var4[0].m_I > 0) {
          if (a(var1, var4[0], 0) && !a_Z(var4[var46])) {
            var4[var46].f_AB[0] = (byte)(2 + var3 / 2);
            var4[var46].f_AB[1] = (byte)(var3 + 4);
          } else {
            var4[var46].o_Z = true;
          }
        }
      }
    } else if (var2 == 34) {
      this.a(var1, var4[0], var1.j_I, var4[0].k_I, 0, false);
      if (!var4[0].o_Z && !a_Z(var4[0])) {
        var4[0].b_B = (byte)(2 + var3 / 2);
      }
    } else if (var2 == 10) {
      for (int var47 = 0; var47 < var4.length; var47++) {
        if (var4[var47].m_I > 0) {
          if (a(var1, var4[var47], 0) && !a_Z(var4[var47])) {
            var4[var47].b_B = (byte)(2 + var3 / 2);
          } else {
            var4[var47].o_Z = true;
          }
        }
      }
    } else if (var2 == 11) {
      if (var4[0].m_I > 0) {
        if (a(var1, var4[0], 0) && !a_Z(var4[0])) {
          var4[0].a_B = (byte)k.b(2 + var3 / 4, 3 + var3 / 4);
        } else {
          var4[0].o_Z = true;
        }
      }
    } else if (var2 == 12) {
      for (int var48 = 0; var48 < var4.length; var48++) {
        if (var4[var48].m_I > 0) {
          if (a(var1, var4[var48], 0) && !a_Z(var4[var48])) {
            var4[var48].a_B = (byte)k.b(2 + var3 / 4, 3 + var3 / 4);
          } else {
            var4[var48].o_Z = true;
          }
        }
      }
    } else if (var2 == 13) {
      var4[0].m_I = var4[0].m_I + var4[0].a_AI[10] * (10 + var3 * 5) / 100 + var1.j_I * (20 + var3 * 2) / 100;
      if (var4[0].m_I > var4[0].a_AI[10]) {
        var4[0].m_I = var4[0].a_AI[10];
      }
    } else if (var2 == 14) {
      for (int var49 = 0; var49 < var4.length; var49++) {
        if (var4[var49].m_I > 0) {
          var4[var49].m_I = var4[var49].m_I + var4[var49].a_AI[10] * (10 + var3 * 5) / 100 + var1.j_I * (20 + var3 * 2) / 100;
        }

        if (var4[var49].m_I > var4[var49].a_AI[10]) {
          var4[var49].m_I = var4[var49].a_AI[10];
        }
      }
    } else if (var2 == 15) {
      for (int var50 = 0; var50 < var4.length; var50++) {
        var4[var50].f_AB = new byte[2];
        var4[var50].a_B = 0;
        var4[var50].b_B = 0;
        var4[var50].c_B = 0;
      }
    } else if (var2 == 16) {
      if (var3 < 6) {
        int var51 = k.b(1, 5);

        for (int var67 = 0; var67 < var4.length; var67++) {
          if (var4[var67] != null && var4[var67].m_I > 0 && var4[var67].c_B == 0) {
            var4[var67].c_B = (byte)var51;
          }
        }

        byte[] var68;
        if (var1.a_I < 10) {
          var68 = this.a_AB(0, 10);
        } else {
          var68 = this.a_AB(10, 20);
        }

        for (int var12 = 0; var12 < var68.length; var12++) {
          if (this.a_Ae[var68[var12]] != null && this.a_Ae[var68[var12]].m_I > 0 && this.a_Ae[var68[var12]].c_B == 0) {
            this.a_Ae[var68[var12]].c_B = (byte)var51;
          }
        }
      } else {
        int var52 = k.b(6, 10);

        for (int var69 = 0; var69 < var4.length; var69++) {
          if (var4[var69].m_I > 0 && var4[var69].c_B == 0) {
            var4[var69].c_B = (byte)var52;
          }
        }
      }
    } else if (var2 == 17) {
      for (int var53 = 0; var53 < var4.length; var53++) {
        if (var4[var53].m_I > 0 && var4[var53].h_AB[0] == 0) {
          if (var3 < 6) {
            var4[var53].h_AB[0] = 3;
          } else {
            var4[var53].h_AB[0] = 5;
          }

          var4[var53].h_AB[1] = (byte)(12 + var3 * 3);
          var4[var53].j_I = var4[var53].j_I + var4[var53].a_AI[12] * var4[var53].h_AB[1] / 100;
          var4[var53].k_I = var4[var53].k_I + var4[var53].a_AI[13] * var4[var53].h_AB[1] / 100;
        }
      }
    } else if (var2 == 18) {
      if (var4[0].m_I > 0) {
        if (a(var1, var4[0], 0)) {
          int var54 = k.b(0, 5);
          if ((var2 = var1.j_I * (125 + var3 * 5) / 100 - var4[0].k_I + var54) < 10) {
            var2 = 10;
          }

          var4[0].m_I -= var2;
        } else {
          var4[0].o_Z = true;
        }
      }
    } else if (var2 == 19) {
      for (int var55 = 0; var55 < var4.length; var55++) {
        if (var4[var55].m_I > 0) {
          if (a(var1, var4[var55], 0)) {
            int var70 = k.b(0, 5);
            if ((var5 = var1.j_I * (145 + var3 * 5) / 100 - var4[var55].k_I + var70) < 10) {
              var5 = 10;
            }

            var4[var55].m_I -= var5;
          } else {
            var4[var55].o_Z = true;
          }
        }
      }
    } else if (var2 == 20) {
      int var56 = 200 + a(new byte[]{0, 0, 0, 10}, var4[0].d_AB);
      int var71 = var1.j_I * (115 + var3 * 5) / 100;
      if (a(var1, var4[0], 0)) {
        var2 = k.b(0, 5);
        if ((var5 = var71 * var56 / 100 - var4[0].k_I + var2) < 10) {
          var5 = 10;
        }

        var4[0].m_I -= var5;
      } else {
        var4[0].o_Z = true;
      }
    } else if (var2 == 21) {
      for (int var57 = 0; var57 < var4.length; var57++) {
        if (var4[var57].m_I > 0) {
          int var72 = 200 + a(new byte[]{0, 0, 0, 10}, var4[var57].d_AB);
          var2 = var1.j_I * (115 + var3 * 5) / 100;
          if (a(var1, var4[var57], 0)) {
            var5 = k.b(0, 5);
            if ((var2 = var2 * var72 / 100 - var4[var57].k_I + var5) < 10) {
              var2 = 10;
            }

            var4[var57].m_I -= var2;
          } else {
            var4[var57].o_Z = true;
          }
        }
      }
    } else if (var2 == 22) {
      int var58 = 200 + a(new byte[]{10, 0, 0, 0}, var4[0].d_AB);
      int var73 = var1.j_I * (115 + var3 * 5) / 100;
      if (a(var1, var4[0], 0)) {
        var2 = k.b(0, 5);
        if ((var5 = var73 * var58 / 100 - var4[0].k_I + var2) < 10) {
          var5 = 10;
        }

        var4[0].m_I -= var5;
      } else {
        var4[0].o_Z = true;
      }
    } else if (var2 == 23) {
      for (int var59 = 0; var59 < var4.length; var59++) {
        if (var4[var59].m_I > 0) {
          int var74 = 200 + a(new byte[]{10, 0, 0, 0}, var4[var59].d_AB);
          var2 = var1.j_I * (115 + var3 * 5) / 100;
          if (a(var1, var4[var59], 0)) {
            var5 = k.b(0, 5);
            if ((var2 = var2 * var74 / 100 - var4[var59].k_I + var5) < 10) {
              var2 = 10;
            }

            var4[var59].m_I -= var2;
          } else {
            var4[var59].o_Z = true;
          }
        }
      }
    } else if (var2 == 24) {
      int var60 = 200 + a(new byte[]{0, 0, 10, 0}, var4[0].d_AB);
      int var75 = var1.j_I * (145 + var3 * 5) / 100;
      if (a(var1, var4[0], 0)) {
        var2 = k.b(0, 5);
        if ((var5 = var75 * var60 / 100 - var4[0].k_I + var2) < 10) {
          var5 = 10;
        }

        var4[0].m_I -= var5;
      } else {
        var4[0].o_Z = true;
      }
    } else if (var2 == 25) {
      for (int var61 = 0; var61 < var4.length; var61++) {
        if (var4[var61].m_I > 0) {
          int var76 = 200 + a(new byte[]{0, 0, 10, 0}, var4[var61].d_AB);
          var2 = var1.j_I * (115 + var3 * 5) / 100;
          if (a(var1, var4[var61], 0)) {
            var5 = k.b(0, 5);
            if ((var2 = var2 * var76 / 100 - var4[var61].k_I + var5) < 10) {
              var2 = 10;
            }

            var4[var61].m_I -= var2;
          } else {
            var4[var61].o_Z = true;
          }
        }
      }
    } else if (var2 == 26) {
      int var62 = 200 + a(new byte[]{0, 10, 0, 0}, var4[0].d_AB);
      int var77 = var1.j_I * (115 + var3 * 5) / 100;
      if (a(var1, var4[0], 0)) {
        var2 = k.b(0, 5);
        if ((var5 = var77 * var62 / 100 - var4[0].k_I + var2) < 10) {
          var5 = 10;
        }

        var4[0].m_I -= var5;
      } else {
        var4[0].o_Z = true;
      }
    } else if (var2 == 27) {
      for (int var63 = 0; var63 < var4.length; var63++) {
        if (var4[var63].m_I > 0) {
          int var78 = 200 + a(new byte[]{0, 10, 0, 0}, var4[var63].d_AB);
          var2 = var1.j_I * (145 + var3 * 5) / 100;
          if (a(var1, var4[var63], 0)) {
            var5 = k.b(0, 5);
            if ((var2 = var2 * var78 / 100 - var4[var63].k_I + var5) < 10) {
              var2 = 10;
            }

            var4[var63].m_I -= var2;
          } else {
            var4[var63].o_Z = true;
          }
        }
      }
    } else if (var2 == 28) {
      int var64 = var4[0].m_I;
      this.a(var1, var4[0], var1.j_I, var4[0].k_I, 0, false);
      if (!var4[0].o_Z) {
        int var79;
        if ((var79 = (var64 - var4[0].m_I) * (48 + (var3 << 1)) / 100) < 10) {
          var79 = 10;
        }

        var1.m_I += var79;
        if (var1.m_I > var1.a_AI[10]) {
          var1.m_I = var1.a_AI[10];
        }
      }
    } else if (var2 == 33) {
      int[][] var65 = new int[][]{{2, 3, 5}, {2, 3, 7}, {3, 4, 8}, {4, 5, 10}, {4, 5, 12}};
      this.a(var1, var4[0], var1.j_I, var4[0].k_I, 0, false);
      if (!var4[0].o_Z && !a_Z(var4[0])) {
        var4[0].f_AB[0] = (byte)k.b(var65[var3 - 1][0], var65[var3 - 1][1]);
        var4[0].f_AB[1] = (byte)var65[var3 - 1][2];
      }
    }

    for (int var66 = 0; var66 < this.a_Ae.length; var66++) {
      int var80;
      if (this.a_Ae[var66] != null
        && this.a_Ae[var66].p_Z
        && this.a_Ae[var66].m_I < this.a_Ae[var66].a_AI[3]
        && (var80 = (this.a_Ae[var66].a_AI[3] - this.a_Ae[var66].m_I) / 5) != 0) {
        this.a_Ae[var66].m_I = this.a_Ae[var66].a_AI[3] - var80;
      }
    }
  }

  public final e a_e(int var1) {
    return this.a_Ae[var1];
  }
}
