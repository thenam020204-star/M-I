import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class as implements cx {
  private dc a_dc;
  public static bl a_bl;
  public static cv[] a_Acv;
  static byte a_B;
  static byte b_B;
  static short a_S;
  static byte c_B;
  static byte d_B;
  static byte e_B;
  static byte f_B;
  static byte g_B;
  static byte h_B;
  static byte i_B;
  static byte j_B;
  static byte k_B;
  static byte l_B;
  static int a_I;
  static byte m_B;
  static byte n_B;
  static String[] a_AString;
  private byte E_B;
  public static byte o_B;
  public boolean a_Z = false;
  public boolean b_Z = false;
  public static byte p_B;
  public static byte q_B;
  public byte r_B = 0;
  public boolean c_Z = false;
  public boolean d_Z = false;
  public static byte s_B = 0;
  public static byte t_B;
  public static byte u_B = 0;
  public static byte v_B;
  public static byte w_B;
  public static byte x_B;
  public static byte y_B;
  public static byte z_B;
  public int b_I = 0;
  public static byte A_B = 0;
  public static int c_I;
  public static byte B_B = 0;
  static String a_String;
  public static short[][] a_AAS;
  public static int d_I = 0;
  public static int e_I = 0;
  public static int f_I = 0;
  public static int g_I = 0;
  private int x_I;
  private int y_I;
  private int z_I = 2;
  bf a_bf;
  cy a_cy;
  cs a_cs;
  de a_de;
  private int A_I = 193;
  private static bx d_bx;
  private bx e_bx;
  bx a_bx;
  private bx f_bx;
  private bx g_bx;
  private bx h_bx;
  bx b_bx;
  private static bx[] b_Abx;
  static bx c_bx;
  static bx[] a_Abx;
  private Image b_Image;
  private Image c_Image;
  private Image d_Image;
  Image a_Image;
  private Image e_Image;
  private Image f_Image;
  private Image g_Image;
  private Image h_Image;
  private Image i_Image;
  private Image j_Image;
  private Image k_Image;
  private Image l_Image;
  private Image m_Image;
  private Image[] a_AImage = new Image[2];
  private Image n_Image;
  private Image o_Image;
  private Image p_Image;
  private Image q_Image;
  private Image r_Image;
  private Image s_Image;
  private Image t_Image;
  private Image u_Image;
  private Image v_Image;
  private Image w_Image;
  private Image x_Image;
  private Image y_Image;
  private Image z_Image;
  private Image A_Image;
  private Image B_Image;
  private Image C_Image;
  private Image D_Image;
  private Image E_Image;
  private Image F_Image;
  private Image G_Image;
  private Image H_Image;
  private Image I_Image;
  private Image J_Image;
  private String[] f_AString = new String[]{
    "Thuộc tính", "Kỹ năng", "Xuất chiến", "Nghỉ", "Thả", "Đổi tên", "Cấy", "Tinh luyện", "Mặc", "Tháo", "Thay", "Khảm", "Bỏ", "Dùng", "Bán", "Xem", "Bỏ"
  };
  private int B_I = 0;
  private int C_I = 0;
  int h_I = 0;
  private int D_I = 0;
  db a_db;
  public bc a_bc;
  public bc b_bc;
  public bc c_bc;
  public bc d_bc;
  public short[][] b_AAS;
  public int i_I = 0;
  public static short[] a_AS;
  public int j_I = 0;
  int[] a_AI;
  public static ab a_ab;
  public static ab b_ab;
  ab c_ab;
  public ab d_ab;
  public ab e_ab;
  public l a_l;
  private l b_l;
  public static int k_I = 1;
  private ck a_ck;
  public static int l_I;
  public static int m_I;
  public static int n_I;
  static byte C_B;
  static String[][] a_AAString;
  private static String[] g_AString;
  static byte[] a_AB;
  static byte[] b_AB;
  private String[] h_AString;
  public boolean e_Z;
  public boolean f_Z;
  int o_I;
  static boolean g_Z;
  static long a_J;
  static short b_S;
  static byte D_B;
  static String[] b_AString;
  int p_I;
  private String[] i_AString;
  public static short c_S = 7;
  public static short d_S = 5;
  static short[][][] a_AAAS = null;
  static String[] c_AString;
  static int[] b_AI = new int[]{13, 14, 12};
  static short e_S;
  private String[] j_AString;
  static short f_S = 6;
  static short g_S = 2;
  static String[] d_AString;
  static short[] b_AS;
  static int q_I;
  static int[] c_AI = new int[]{10, 9, 12, 11};
  public static String[] e_AString;
  public static int[] d_AI = new int[]{0, 1, 2, 4, 5, 6, 7};
  public static String b_String;
  int r_I;
  int s_I;
  int t_I;
  byte[][] a_AAB;
  int u_I;
  int v_I;
  int w_I;
  int[] e_AI;
  int[] f_AI;
  private int E_I;
  private String c_String;
  private boolean h_Z;
  private int F_I;

  public as() {
    String[] var10000 = new String[]{"Nam chính", "Nữ chính", "Pháp sư", "Sủng vật"};
    this.h_AString = new String[]{"Mới nhất", "Nguyên tố", "Tất cả"};
    this.e_Z = false;
    this.f_Z = false;
    this.o_I = 0;
    this.p_I = -1;
    this.i_AString = new String[]{"NV chính tuyến", "NV phụ tuyến"};
    this.j_AString = new String[]{"Vật phẩm", "Nguyên liệu", "Khác"};
    int[] var2 = new int[]{16660536, 12175614, 15964940, 9693914};
    this.r_I = 0;
    this.s_I = 0;
    this.t_I = 0;
    this.a_AAB = null;
    this.u_I = 1;
    this.v_I = 0;
    this.w_I = 0;
    this.e_AI = new int[]{0, 0, 0, 0};
    this.f_AI = new int[]{0, 0, 0, 0, 0};
    this.E_I = 0;
    f_S = 6;
    g_S = 2;
    c_S = 7;
    d_S = 5;
    this.D_I = 191;
    if (this.a_de == null) {
      this.a_de = new de();

      for (int var1 = 0; var1 < 8; var1++) {
        this.a_de.a(var1, var1);
      }

      this.a_de.a((at.a_AS[0] - 193) / 2, (at.a_AS[1] - 255) / 2, 193, 28, 27);
      this.a_AI = this.a_de.a();
    }

    this.j_I = 200;
    this.a(90, this.j_I - 90);
    if (this.E_Image == null) {
      this.E_Image = cc.a("/pic/D20.gf");
    }

    if (this.F_Image == null) {
      this.F_Image = cc.a("/pic/D21.gf");
    }

    if (this.n_Image == null) {
      this.n_Image = k.a_Image(190, 90, 657930, 85);
    }

    if (this.u_Image == null) {
      this.u_Image = cc.a("/pic/D23.gf");
    }

    if (this.s_Image == null) {
      this.s_Image = cc.a_Image("/pic/D02.gf", 2);
    }

    if (this.o_Image == null) {
      this.o_Image = cc.a("/pic/tjitem.gf");
    }

    if (this.t_Image == null) {
      this.t_Image = cc.a("/pic/D11.gf");
    }

    if (this.e_Image == null) {
      this.e_Image = cc.a("/pic/partInfo.gf");
    }

    if (this.k_Image == null) {
      this.k_Image = cc.a("/pic/goodsPc.gf");
    }

    if (this.l_Image == null) {
      this.l_Image = cc.a("/pic/goodsPc1.gf");
    }

    if (this.f_Image == null) {
      this.f_Image = cc.a_Image("/pic/star.gf", 2);
    }

    if (this.j_Image == null) {
      this.j_Image = cc.a("/pic/D15.gf");
    }

    if (this.i_Image == null) {
      this.i_Image = cc.a("/pic/taskNameArr.gf");
    }

    if (this.v_Image == null) {
      this.v_Image = cc.a("/pic/D05.gf");
    }

    if (this.D_Image == null) {
      this.D_Image = cc.a("/pic/D10.gf");
    }

    if (this.x_Image == null) {
      this.x_Image = cc.a("/pic/D06.gf");
    }

    if (this.a_AImage[1] == null) {
      String var10002 = "/pic/imgmenu2.gf";
      this.a_AImage[1] = null;
    }

    if (this.a_AImage[0] == null) {
      String var4 = "/pic/imgmenu1.gf";
      this.a_AImage[0] = null;
    }

    if (this.g_Image == null) {
      this.g_Image = cc.a("/pic/taskNameArr1.gf");
    }

    if (this.I_Image == null) {
      this.I_Image = cc.a_Image("/pic/number04.gf", 2);
    }

    if (this.H_Image == null) {
      this.H_Image = cc.a_Image("/pic/number03.gf", 2);
    }

    if (this.G_Image == null) {
      this.G_Image = cc.a_Image("/pic/number02.gf", 2);
    }

    if (this.B_Image == null) {
      this.B_Image = cc.a("/pic/D08.gf");
    }

    if (this.C_Image == null) {
      this.C_Image = cc.a("/pic/D09.gf");
    }

    if (this.t_Image == null) {
      this.t_Image = cc.a("/pic/D11.gf");
    }

    if (this.z_Image == null) {
      this.z_Image = cc.a("/pic/shulian.gf");
    }

    if (this.A_Image == null) {
      this.A_Image = cc.a("/pic/D07.gf");
    }

    if (this.y_Image == null) {
      this.y_Image = cc.a("/pic/D13.gf");
    }

    if (this.m_Image == null) {
      this.m_Image = cc.a("/pic/taoz.gf");
    }

    if (this.a_Image == null) {
      this.a_Image = cc.a_Image("/pic/arrow.gf", 2);
    }

    if (this.h_Image == null) {
      String var10001 = "/pic/tzjy.gf";
      this.h_Image = null;
    }

    if (this.c_Image == null) {
      this.c_Image = cc.a("/pic/D10.gf");
    }

    if (this.b_Image == null) {
      this.b_Image = cc.a("/pic/D23.gf");
    }

    if (this.d_Image == null) {
      this.d_Image = cc.a_Image("/pic/number01.gf", 2);
    }

    if (this.r_Image == null) {
      this.r_Image = k.a_Image(193, 30, 2959909, 85);
    }

    this.h_I = 28;
    if (this.w_Image == null) {
      String var3 = "/pic/setup.gf";
      this.w_Image = null;
    }

    if (this.J_Image == null) {
      this.J_Image = cc.a_Image("/pic/number05.gf", 2);
    }

    this.l();
    this.x_I = at.a_AS[0];
    this.y_I = at.a_AS[1];
  }

  public final void a(int var1, int var2) {
    this.p_Image = null;
    this.q_Image = null;
    this.B_I = 0;
    this.C_I = 0;
    if (var1 != 0) {
      this.B_I = var1 - 2;
      this.p_Image = k.a_Image(193, var1, 2959909, 85);
    }

    if (var2 > 0) {
      this.C_I = var2 - 2;
      this.q_Image = k.a_Image(193, var2, 657930, 85);
    }
  }

  private void l() {
    if (d_bx == null) {
      (d_bx = new bx()).a("/pic/D01.av");
    }

    if (b_Abx == null) {
      b_Abx = new bx[6];

      for (int var1 = 0; var1 < 6; var1++) {
        b_Abx[var1] = new bx();
        b_Abx[var1].a("/pic/equipstar/tbss.av");
      }
    }

    InputStream var2 = Main.self.getClass().getResourceAsStream("/pic/D41.av");
    if (this.b_bx == null) {
      this.b_bx = new bx();
      this.b_bx.a("/pic/D41.av");
    }

    if (var2 == null) {
      this.b_bx = null;
    }

    c_bx = new bx();
    int var3 = a_bl.b_String.indexOf(".");
    String var4 = a_bl.b_String.substring(0, var3) + "s.av";
    c_bx.a(var4);
    if (this.e_bx == null) {
      this.e_bx = new bx();
      this.e_bx.a("/pic/D24.av");
    }

    if (this.g_bx == null) {
      this.g_bx = new bx();
      this.g_bx.a("/pic/uiav/D28.av");
      this.h_bx = new bx();
      this.h_bx.a_Z = true;
      this.h_bx.a("/pic/uiav/D28.av");
    }
  }

  public final void a(byte var1, byte var2) {
    o_B = var1;
    if (this.E_I != 0) {
      byte var3 = 0;
      this.E_I = var3;
    }

    if (o_B == 1) {
      boolean var20 = false;
      de var5 = this.a_de;
      this.a_de.b_Z = var20;
    } else {
      boolean var21 = true;
      de var6 = this.a_de;
      this.a_de.b_Z = var21;
    }

    if (o_B != 1) {
      if (o_B == 3) {
        q_B = (byte)var2;
        this.a(90, this.j_I - 90);
        if (q_B == 0) {
          this.a();
          if (this.a_l != null) {
            Image var22 = this.a_Image;
            l var7 = this.a_l;
            this.a_l.a_Image = var22;
            return;
          }
        } else if (q_B == 7) {
          if (this.r_B == 0) {
            this.a(this.j_I, 0);
            return;
          }
        } else {
          if (q_B == 1) {
            Object var24 = null;
            l var10 = this.a_l;
            this.a_l.a_Image = (Image)var24;
            return;
          }

          if (q_B == 5) {
            this.a(90, this.j_I - 90);
            this.e_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + this.B_I + 2, this.a_AI[2], this.C_I - 14);
            int[][] var8 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
            bo var17 = new bo((byte)1, var8);
            g var23 = new g(true);
            if (d.a_AString == null) {
              d.a();
            }

            if (a_Acv[this.a_l.a()].a_Ad != null) {
              this.e_ab.a_AI = new int[a_Acv[this.a_l.a()].a_Ad.length];

              for (int var9 = 0; var9 < a_Acv[this.a_l.a()].a_Ad.length; var9++) {
                int var4 = o.c_I(a_Acv[this.a_l.a()].a_Ad[var9].a_S);
                var17.a(d.a_AString[var4] + "  " + a_Acv[this.a_l.a()].a_Ad[var9].a_B + " cấp");
                Image var31 = cc.a("/icon/" + d.b_AString[var4] + ".gf");
                this.e_ab.a_AI[var9] = a_Acv[this.a_l.a()].a_Ad[var9].a_S;
                var23.a(var31);
              }

              var23.c(cg.b_S + 3);
              var17.c(cg.b_S + 3);
              var23.a(this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + this.B_I + 2, 40, this.C_I - 22);
              var17.a(this.a_AI[0] + 20, this.a_AI[1] + this.a_AI[3] + this.B_I + 2, this.a_AI[2], this.C_I - 22);
              this.e_ab.a(var23);
              this.e_ab.a(var17);
              if (this.a_Image != null) {
                this.e_ab.a(this.a_Image, this.a_AI[0]);
              }
            }

            return;
          }
        }
      } else {
        if (o_B == 2) {
          this.a(90, this.j_I - 90);
          return;
        }

        if (o_B == 5) {
          f_I = 3;
          ck.a_V();
          ck.b_V();
          t_B = 0;
          if (a_bl.h_S == 1) {
            b_AS = new short[]{14, 26, 38, 44, 51, 58};
          } else if (a_bl.h_S == 2) {
            b_AS = new short[]{7, 20, 32, 45, 52, 59};
          } else if (a_bl.h_S == 3) {
            b_AS = new short[]{7, 20, 32, 45, 52, 59};
          }

          q_I = 0;

          for (int var16 = 0; var16 < a_bl.b_AS.length; var16++) {
            if (cg.a_Z(b_AS, a_bl.b_AS[var16])) {
              q_I++;
            }
          }

          t_B = (byte)var2;
          if (var2 == 3) {
            this.a(60, this.j_I - 60);
            return;
          }

          this.a(90, this.j_I - 90);
          return;
        }

        if (o_B == 4) {
          this.a(90, this.j_I - 90);
          x_B = 0;
          this.d_ab.a(this.a_Image, this.a_AI[0]);
          return;
        }

        if (o_B == 7) {
          w_B = (byte)var2;
          this.a(30, this.j_I - 30);
          if (w_B == 0) {
            this.b();
            return;
          }

          if (w_B == 2) {
            Image var26 = this.a_Image;
            bf var14 = this.a_bf;
            this.a_bf.a_Image = var26;
            return;
          }

          if (w_B == 1) {
            Image var25 = this.a_Image;
            cy var13 = this.a_cy;
            this.a_cy.a_Image = var25;
            return;
          }

          if (w_B == 3) {
            this.a_cs = new cs(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + this.B_I, this.a_AI[2], this.C_I);
            int[] var11 = new int[]{16514974, 10571302, 16705870, 9194010};
            cs var12 = this.a_cs;
            this.a_cs.a_AI = var11;
            this.a_cs.a("Mô tả nhiệm vụ", o.a_Abp[this.p_I].b_String, "Thưởng nhiệm vụ", o.a_Abp[this.p_I].e_String);
            return;
          }
        } else if (o_B == 6) {
          o.d();
          o.e();
          j();
          e_B = 0;
          f_B = 0;
          g_B = 0;
          e_S = 1;
          v_B = 0;
          this.a(30, this.j_I - 30);
          if (this.k_Image == null) {
            this.k_Image = cc.a("/pic/goodsPc.gf");
          }

          if (this.l_Image == null) {
            this.l_Image = cc.a("/pic/goodsPc1.gf");
            return;
          }
        } else if (o_B == 9) {
          this.a(0, this.j_I - 2);
          this.t_Image = cc.a("/pic/D11.gf");
          this.p();
          this.c_ab.a(this.a_Image, (at.a_AS[0] - 120) / 2);
          p_B = 0;
          if (this.t_Image == null) {
            this.t_Image = cc.a("/pic/D11.gf");
          }

          p_B = (byte)var2;
          if (var2 == 0) {
            this.a(0, this.j_I - 2);
            return;
          }

          if (p_B == 1) {
            this.a(30, this.j_I - 30);
            return;
          }

          if (p_B == 2) {
            this.a(30, this.j_I - 30);
            return;
          }

          if (p_B == 3) {
            if (!at.b_Z && o.g_B <= 0) {
              if (this.b_bx != null) {
                this.b_bx.a_V(0);
              }
            } else if (this.b_bx != null) {
              this.b_bx.a_V(1);
            }

            this.a(30, this.j_I - 30);
            return;
          }

          if (p_B != 4 && p_B == 5) {
            return;
          }
        } else if (o_B == 8) {
          z_B = (byte)var2;
          if (var2 == 0) {
            this.a(0, this.j_I - 2);
            byte var27 = 2;
            this.E_I = var27;
            if (this.b_bc != null) {
              this.b_bc.a(this.a_Image, this.d_Image);
            }

            if (this.d_bc != null) {
              this.d_bc.a(this.a_Image, this.d_Image);
            }

            if (this.c_bc != null) {
              this.c_bc.a(this.a_Image, this.d_Image);
              return;
            }
          } else {
            if (z_B == 1) {
              byte var29 = 2;
              this.E_I = var29;
              this.a(90, this.j_I - 90);
              return;
            }

            if (z_B == 2) {
              byte var28 = 0;
              this.E_I = var28;
              o.d();
              o.e();
              this.m();
              return;
            }
          }
        } else {
          if (o_B == 13) {
            this.a(0, this.j_I - 2);
            return;
          }

          if (o_B == 12) {
            this.a(90, this.j_I - 90);
            this.a_bc = new bc();
            if (a_AAString == null) {
              a_AAString = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
            }

            for (int var15 = 0; var15 < 140; var15++) {
              var2 = var15 + 1;
              String var30 = a_AAString[var2 - 1][0];
              if (cg.a_Z(o.p_AS, var15 + 1)) {
                this.a_bc.a(var15 + 1, var30);
              }
            }

            this.a_bc.a(this.a_AI[0] + 8, this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.B_I);
            this.a_bc.a(this.a_Image, this.d_Image);
            this.a_bc.b();
            c_bx = new bx();
            c_bx = k.a(this.a_bc.a_I(), true);
          }
        }
      }
    }
  }

  private void m() {
    this.a_db = null;
    this.a_db = new db();
    if (this.b_AAS != null) {
      for (int var1 = 0; var1 < this.b_AAS.length; var1++) {
        this.a_db.a(o.a_AImage[this.b_AAS[var1][0] - 1], o.f_AString[this.b_AAS[var1][0] - 1], o.b_I(this.b_AAS[var1][0]), this.b_AAS[var1][1]);
      }
    }

    this.a_db.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + this.B_I + 10, this.a_AI[2], this.C_I);
    Image var2 = this.a_Image;
    db var3 = this.a_db;
    this.a_db.a_Image = var2;
  }

  public final void a() {
    if (this.a_l != null) {
      this.a_l = null;
    }

    this.a_l = new l();
    Object var2 = null;
    l var1 = this.a_l;
    this.a_l.a_Image = (Image)var2;
    this.a_l.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.B_I);
    if (a_Acv != null) {
      for (int var3 = 0; var3 < a_Acv.length; var3++) {
        this.a_l.a(a_Acv[var3].a_I, var3, a_Acv[var3].a_Z, a_Acv[var3].a_String, a_Acv[var3].a_AI[0]);
      }

      this.a_bx = new bx();
      this.a_bx = k.a(a_Acv[this.a_l.a()].a_I, true);
    }
  }

  private void n() {
    f_I = 3;
    ck.a_V();
    ck.b_V();
    this.f_bx = new bx();
    this.f_bx.a("/ft/chr" + a_bl.h_S + ".av");
    b_B = 0;
    t_B = 0;
    if (a_bl.h_S == 1) {
      b_AS = new short[]{14, 26, 38, 44, 51, 58};
    } else if (a_bl.h_S == 2) {
      b_AS = new short[]{7, 20, 32, 45, 52, 59};
    }

    q_I = 0;

    for (int var1 = 0; var1 < a_bl.b_AS.length; var1++) {
      if (cg.a_Z(b_AS, a_bl.b_AS[var1])) {
        q_I++;
      }
    }
  }

  private void o() {
    d.a();
    d.b();
    if (this.d_ab != null) {
      this.d_ab = null;
    }

    this.d_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + 94, this.a_AI[2], this.C_I - 22);
    int[][] var1 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
    bo var5 = new bo((byte)1, var1);
    g var2 = new g(true);
    if (a_bl.a_Ad != null) {
      this.d_ab.a_AI = new int[a_bl.a_Ad.length];

      for (int var3 = 0; var3 < a_bl.a_Ad.length; var3++) {
        int var4 = o.c_I(a_bl.a_Ad[var3].a_S);
        this.d_ab.a_AI[var3] = a_bl.a_Ad[var3].a_S;
        var5.a(d.a_AString[var4] + " " + a_bl.a_Ad[var3].a_B + " cấp");
        var2.a(d.a_AImage[var4]);
      }

      var2.c(cg.b_S + 3);
      var5.c(cg.b_S + 3);
      var2.a(this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + 90 + 2, 40, this.C_I - 22);
      var5.a(this.a_AI[0] + 20, this.a_AI[1] + this.a_AI[3] + 90 + 2, this.a_AI[2], this.C_I - 22);
      this.d_ab.a(var2);
      this.d_ab.a(var5);
      if (this.a_Image != null) {
        this.d_ab.a(null, this.a_AI[0]);
      }

      b_S = (short)a_bl.a_Ad.length;
    }
  }

  public final void b() {
    if (this.a_bf != null) {
      this.a_bf.a_V();
      this.a_bf = null;
    }

    if (this.a_cy != null) {
      this.a_cy.a_V();
      this.a_cy = null;
    }

    this.a_cy = new cy();
    Object var2 = null;
    cy var1 = this.a_cy;
    this.a_cy.a_Image = (Image)var2;
    this.a_bf = new bf();
    var2 = null;
    bf var3 = this.a_bf;
    this.a_bf.a_Image = (Image)var2;

    for (int var4 = 0; o.a_Abp != null && var4 < o.a_Abp.length; var4++) {
      b_AString = cg.a(b_AString, o.a_Abp[var4].a_String);
      if (o.a_Abp[var4].a_B == 1) {
        this.a_cy.a(o.a_Abp[var4].a_S, 0, o.a_Abp[var4].a_String);
      } else if (o.a_Abp[var4].a_B == 2 || o.a_Abp[var4].a_B == 3) {
        this.a_bf.a(var4, o.a_Abp[var4].a_B, o.a_Abp[var4].a_String, o.a_Abp[var4].a_Z);
      }
    }

    if (o.o_AS != null && o.o_AS.length > 0) {
      for (int var5 = 0; var5 < o.o_AS.length; var5++) {
        if (((bp)(var2 = o.a_bp(o.o_AS[var5], -1))).a_B == 1) {
          this.a_cy.a(((bp)var2).a_S, 1, ((bp)var2).a_String);
        }
      }
    }

    this.a_bf.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + 30, this.a_AI[2], this.j_I - 30);
    this.a_cy.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + 50, this.a_AI[2], this.j_I - 60);
  }

  public final void c() {
    o.d();
    o.e();
    j();
  }

  private void p() {
    if (this.c_ab == null) {
      if (at.a_Z) {
        if (a_AString == null) {
          a_AString = new String[]{"Dịch vụ ưu đãi", "Lưu game", "Tải game", "Cài đặt nhạc", "Tải điểm QQ", "Thoát game"};
        }
      } else if (a_AString == null) {
        a_AString = new String[]{"Dịch vụ ưu đãi", "Lưu game", "Tải game", "Thoát game"};
      }

      int var1 = a_AString.length * (cg.b_S + 5);
      this.c_ab = new ab((at.a_AS[0] - 120) / 2, this.a_AI[1] + this.a_AI[3] + (this.B_I + this.C_I - var1) / 2, 120, var1);
      int[][] var2 = new int[][]{{13695224, 1393321}, {13629944, 13629944}};
      bo var4 = new bo((byte)1, var2);

      for (int var3 = 0; var3 < a_AString.length; var3++) {
        var4.a(a_AString[var3]);
      }

      var4.a((at.a_AS[0] - 120) / 2, this.a_AI[1] + this.a_AI[3] + (this.B_I + this.C_I - var1) / 2, 120, var1);
      var4.c(cg.b_S + 5);
      this.c_ab.a(null, (at.a_AS[0] - 120) / 2 - 10);
      this.c_ab.a(var4);
    }
  }

  public final void a(byte var1) {
    this.z_I = var1;
    if (this.E_I != 0) {
      byte var3 = 0;
      this.E_I = var3;
    }

    if (var1 == 1) {
      this.a(90, this.j_I - 90);
      c_bx = null;
      c_bx = new bx();
      var1 = a_bl.b_String.indexOf(".");
      String var9 = a_bl.b_String.substring(0, var1) + "s.av";
      c_bx.a(var9);
    } else if (var1 == 3) {
      q_B = 0;
      this.a(90, this.j_I - 90);
      this.a();
    } else if (var1 == 2) {
      this.a(90, this.j_I - 90);
      c_bx = null;
      c_bx = new bx();
      var1 = a_bl.b_String.indexOf(".");
      String var7 = a_bl.b_String.substring(0, var1) + "s.av";
      c_bx.a(var7);
    } else if (var1 == 5) {
      (c_bx = new bx()).a("/ft/chr" + a_bl.h_S + ".av");
      t_B = 0;
      if (0 == 3) {
        this.a(60, this.j_I - 60);
      } else {
        this.a(90, this.j_I - 90);
      }
    } else if (var1 == 4) {
      this.a(90, this.j_I - 90);
      c_bx = null;
      c_bx = new bx();
      var1 = a_bl.b_String.indexOf(".");
      String var5 = a_bl.b_String.substring(0, var1) + "s.av";
      c_bx.a(var5);
    } else if (var1 == 7) {
      this.a(30, this.j_I - 30);
      w_B = 0;
    } else if (var1 == 6) {
      this.a(30, this.j_I - 30);
      e_B = 0;
      f_B = 0;
      g_B = 0;
      e_S = 1;
      v_B = 0;
    } else {
      if (var1 == 9) {
        this.a(0, this.j_I - 2);
        p_B = 0;
        p_B = 0;
        if (true) {
          this.a(0, this.j_I - 2);
          return;
        }
      } else {
        if (var1 == 8) {
          z_B = 0;
          this.a(0, this.j_I - 2);
          return;
        }

        if (var1 == 13) {
          this.a(0, this.j_I - 2);
        }
      }
    }
  }

  public final void a(int var1) {
    a_AB = null;
    b_AB = null;
    c_bx = new bx();
    c_bx = k.a(var1, false);
    String[] var2;
    short[][] var3 = new short[(var2 = cg.b_AString(a_AAString[var1 - 1][19], "|")).length][2];

    for (int var4 = 0; var4 < var2.length; var4++) {
      short[] var5 = cg.b_AS(var2[var4], ",");
      var3[var4][0] = var5[0];
      var3[var4][1] = var5[1];
    }

    this.b_AAS = var3;
    ak.a(this.b_AAS, "Hợp thành " + var1);
    if (a_AAString[var1 - 1][2] != null) {
      for (int var6 = 0; var6 < 4; var6++) {
        String var8;
        if ((var8 = a_AAString[var1 - 1][2].substring(var6, var6 + 1)).equals("a")) {
          a_AB = cg.a_AB(a_AB, 10);
        } else {
          a_AB = cg.a_AB(a_AB, cg.a_I(var8));
        }
      }
    }

    for (int var9 = 0; var9 < 5; var9++) {
      byte[] var7 = cg.b_AB(a_AAString[var1 - 1][4 + var9 * 2], "|");
      b_AB = cg.a_AB(b_AB, var7[0]);
    }

    if (o.a_AZ[var1 - 1]) {
      o.a_AZ[var1 - 1] = false;
    }

    a_I = var1 - 1;
  }

  public final void d() {
    a_I = 0;
    if (a_AAString == null) {
      a_AAString = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
    }

    this.b_bc = new bc();
    short[] var1 = null;
    short[] var2 = null;
    short[] var3 = null;
    short[] var4 = null;
    g_AString = null;
    this.d_bc = null;
    this.c_bc = null;

    for (int var6 = 0; var6 < a_AAString.length; var6++) {
      int var7 = var6;
      String var8 = a_AAString[var6][0];
      short[] var5 = null;

      for (int var9 = 0; var9 < 4; var9++) {
        String var10;
        if ((var10 = a_AAString[var7][2].substring(var9, var9 + 1)).equals("a")) {
          var5 = cg.a_AS(var5, 10);
        } else {
          var5 = cg.a_AS(var5, cg.a_I(var10));
        }
      }

      if (var5[0] > 5) {
        var1 = cg.a_AS(var1, var6);
      }

      if (var5[1] > 5) {
        var2 = cg.a_AS(var2, var6);
      }

      if (var5[2] > 5) {
        var3 = cg.a_AS(var3, var6);
      }

      if (var5[3] > 5) {
        var4 = cg.a_AS(var4, var6);
      }

      if (cg.a_Z(o.p_AS, var6 + 1)) {
        if (this.d_bc == null) {
          this.d_bc = new bc();
        }

        this.d_bc.a(var6 + 1, var8);
        g_AString = cg.a(g_AString, var8);
        this.b_bc.a(var6 + 1, var8);
      } else {
        g_AString = cg.a(g_AString, "???");
        this.b_bc.a(var6 + 1, "???");
      }
    }

    if (this.c_bc == null) {
      this.c_bc = new bc();
    }

    short[] var11 = cg.a_AS(cg.a_AS(cg.a_AS(cg.a_AS(null, var1), var2), var3), var4);

    for (int var12 = 0; var12 < var11.length; var12++) {
      short var13 = var11[var12];
      String var14 = a_AAString[var13][0];
      if (cg.a_Z(o.p_AS, var11[var12] + 1)) {
        this.c_bc.a(var11[var12] + 1, var14);
      } else {
        this.c_bc.a(var11[var12] + 1, "???");
      }
    }

    if (this.d_bc != null) {
      this.d_bc.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 8, this.a_AI[2], this.j_I - 2 * (cg.b_S + 8));
      this.d_bc.a(null, this.d_Image);
      this.d_bc.b();
    }

    this.b_bc.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 8, this.a_AI[2], this.j_I - 2 * (cg.b_S + 8));
    this.b_bc.a(null, this.d_Image);
    this.b_bc.b();
    this.c_bc.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 8, this.a_AI[2], this.j_I - 2 * (cg.b_S + 8));
    this.c_bc.a(null, this.d_Image);
    this.c_bc.b();
  }

  public static void b(int var0) {
    String var1 = cg.a_String("/bin/shop.bin");
    a_bl = o.a_Abl[0];
    a_Acv = null;
    a_Acv = k.a_Acv();
    short[] var2 = null;
    if (y_B == 0) {
      var2 = k.a(var1, "itemShop" + var0 + ":", "end", ",", "lib");
    } else if (y_B == 1) {
      var2 = k.a(var1, "equipShop" + var0 + ":", "end", ",", "lib");
    } else if (y_B == 2) {
      var2 = k.a(var1, "skillShop" + var0 + ":", "end", ",", "lib");
    }

    if (var2 != null) {
      for (int var3 = 0; var3 < var2.length; var3++) {
        a_AS = cg.a_AS(a_AS, var2[var3]);
      }
    }
  }

  public final void e() {
    d_bx = null;
    b_Abx = null;
    a_AAString = null;
    b_AS = null;
    a_Abx = null;
    c_bx = null;
    a_AAAS = null;
    a_AS = null;
    a_AString = null;
    d_AString = null;
    e_AString = null;
    b_AString = null;
    if (g_AString != null) {
      g_AString = null;
    }

    if (c_AString != null) {
      c_AString = null;
    }

    if (this.d_ab != null) {
      this.d_ab = null;
    }

    if (this.e_ab != null) {
      this.e_ab = null;
    }

    if (this.d_bc != null) {
      this.d_bc = null;
    }

    if (this.c_bc != null) {
      this.c_bc = null;
    }

    if (this.a_bc != null) {
      this.a_bc = null;
    }

    if (this.a_l != null) {
      this.a_l = null;
    }

    if (this.b_l != null) {
      this.b_l = null;
    }
  }

  public final void f() {
    if (this.b_l != null) {
      Object var2 = null;
      l var1 = this.b_l;
      this.b_l.a_Image = (Image)var2;
      this.b_l.a(0);
    }

    if (this.a_l != null) {
      Object var4 = null;
      l var3 = this.a_l;
      this.a_l.a_Image = (Image)var4;
      if (this.a_l.b() > 0) {
        this.a_l.a(0);
      }
    }

    if (this.d_ab != null) {
      this.d_ab.a(null, this.a_AI[0]);
      this.d_ab.b(0);
    }

    if (this.c_ab != null) {
      this.c_ab.a(null, this.c_ab.a_I);
    }

    if (this.b_bc != null) {
      this.b_bc.a(null, this.d_Image);
      this.b_bc.a(0);
    }

    if (this.d_bc != null) {
      this.d_bc.a(null, this.d_Image);
      this.d_bc.a(0);
    }

    if (this.c_bc != null) {
      this.c_bc.a(null, this.d_Image);
      this.c_bc.a(0);
    }
  }

  public final void g() {
    if (c_I == 4) {
      if (this.a_l != null) {
        this.a_l = null;
      }

      this.a_l = new l();
      Object var2 = null;
      l var1 = this.a_l;
      this.a_l.a_Image = (Image)var2;
      this.a_l.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.B_I);
      if (a_Acv != null) {
        for (int var3 = 0; var3 < a_Acv.length; var3++) {
          this.a_l.a(a_Acv[var3].a_I, var3, a_Acv[var3].a_Z, a_Acv[var3].a_String, a_Acv[var3].a_AI[0]);
        }

        return;
      }
    } else {
      if (this.b_l != null) {
        this.b_l = null;
      }

      this.b_l = new l();
      Object var6 = null;
      l var4 = this.b_l;
      this.b_l.a_Image = (Image)var6;
      this.b_l.a(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2], this.B_I);
      if (o.a_Abl != null) {
        for (int var5 = 0; var5 < o.a_Abl.length; var5++) {
          this.b_l.a(o.a_Abl[var5].h_S, var5, false, o.a_Abl[var5].a_String, o.a_Abl[var5].a_AI[0]);
        }
      }
    }
  }

  public final void h() {
    this.p_Image = null;
    this.q_Image = null;
    this.r_Image = null;
    a_AAString = null;
    d_bx = null;
    a_AAAS = null;
  }

  public final void i() {
    if (a_AAString == null) {
      a_AAString = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
    }

    if (o_B == 14) {
      boolean var1 = true;
      de var6 = this.a_de;
      this.a_de.a_Z = true;
      this.a(160, this.j_I - 160);
      if (d.a_AString == null) {
        d.a();
      }

      if (d.a_AImage == null) {
        d.b();
      }

      a_Acv = null;
      a_Acv = k.a_Acv();
      A_B = 0;
      a_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 6, this.a_AI[2], this.B_I - cg.b_S);
      int[][] var7 = new int[][]{{16711680, 16777215}, {16580583, 6036810}};
      bo var8 = new bo((byte)1, var7);
      g var2 = new g(true);
      cp var3 = new cp((byte)1, 10, this.H_Image);
      a_ab.a_AI = new int[a_AS.length];
      String var5 = "Tiệm kỹ năng";
      short[] var4 = a_AS;
      if (a_AS == null) {
        System.out.println(var5 + " is null!");
      } else {
        System.out.println(var5 + "={");

        for (int var22 = 0; var22 < var4.length; var22++) {
          if (var22 < var4.length - 1) {
            System.out.println("" + var4[var22] + ",");
          } else {
            System.out.println("" + var4[var22]);
          }
        }

        System.out.println(" }");
        System.out.println("length=" + var4.length);
      }

      for (int var19 = 0; var19 < a_AS.length; var19++) {
        var2.a(d.a_AImage[o.c_I(a_AS[var19])]);
        var8.a(d.a_AString[o.c_I(a_AS[var19])]);
        var3.a(String.valueOf(d.d_AS[o.c_I(a_AS[var19])]));
        a_ab.a_AI[var19] = a_AS[var19];
      }

      var2.c(cg.b_S + 8);
      var8.c(cg.b_S + 8);
      var3.c(cg.b_S + 8);
      var2.a(this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2], this.B_I - cg.b_S);
      var8.a(this.a_AI[0] + 35, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 2, this.B_I - cg.b_S);
      var3.a(this.a_AI[0] + this.a_AI[2] * 3 / 4, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 4, this.B_I - cg.b_S);
      a_ab.a(var2);
      a_ab.a(var8);
      a_ab.a(var3);
      a_ab.a(this.a_Image, this.a_AI[0]);
    } else if (o_B == 15) {
      boolean var9 = true;
      de var10 = this.a_de;
      this.a_de.a_Z = true;
      this.a(160, this.j_I - 160);
      if (y_B == 0) {
        if (o.f_AString == null) {
          o.d();
        }

        if (o.a_AImage == null) {
          o.e();
        }

        b_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2], this.B_I - cg.b_S);
        int[][] var11 = new int[][]{{16580583, 6036810}, {16580583, 5247562}};
        bo var12 = new bo((byte)1, var11);
        g var15 = new g(true);
        cp var17 = new cp((byte)1, 10, this.H_Image);
        b_ab.a_AI = new int[a_AS.length];

        for (int var20 = 0; var20 < a_AS.length; var20++) {
          var15.a(o.a_AImage[o.d_I(a_AS[var20])]);
          var12.a(o.f_AString[o.d_I(a_AS[var20])]);
          var17.a(String.valueOf(o.h_AS[o.d_I(a_AS[var20])]));
          b_ab.a_AI[var20] = a_AS[var20];
        }

        var15.c(cg.b_S + 8);
        var12.c(cg.b_S + 8);
        var17.c(cg.b_S + 8);
        var15.a(this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2], this.B_I - cg.b_S);
        var12.a(this.a_AI[0] + 35, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 2, this.B_I - cg.b_S);
        var17.a(this.a_AI[0] + this.a_AI[2] * 3 / 4, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 4, this.B_I - cg.b_S);
        b_ab.a(var15);
        b_ab.a(var12);
        b_ab.a(var17);
        b_ab.a(this.a_Image, this.a_AI[0]);
      } else if (y_B == 1) {
        if (ck.a_AString == null) {
          ck.a_V();
        }

        if (ck.a_AImage == null) {
          ck.b_V();
        }

        b_ab = new ab(this.a_AI[0], this.a_AI[1] + this.a_AI[3] + cg.b_S + 2, this.a_AI[2], this.B_I - cg.b_S);
        int[][] var13 = new int[][]{{16580583, 6036810}, {16580583, 5247562}};
        bo var14 = new bo((byte)1, var13);
        g var16 = new g(true);
        cp var18 = new cp((byte)1, 10, this.H_Image);
        b_ab.a_AI = new int[a_AS.length];

        for (int var21 = 0; var21 < a_AS.length; var21++) {
          var16.a(ck.a_AImage[o.f_I(a_AS[var21])]);
          var14.a(ck.a_AString[o.f_I(a_AS[var21])]);
          var18.a(String.valueOf(ck.c_AS[o.f_I(a_AS[var21])]));
          b_ab.a_AI[var21] = a_AS[var21];
        }

        var16.c(cg.b_S + 8);
        var14.c(cg.b_S + 8);
        var18.c(cg.b_S + 8);
        var16.a(this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2], this.B_I - cg.b_S);
        var14.a(this.a_AI[0] + 35, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 2, this.B_I - cg.b_S);
        var18.a(this.a_AI[0] + this.a_AI[2] * 3 / 4, this.a_AI[1] + this.a_AI[3] + cg.b_S + 4, this.a_AI[2] / 4, this.B_I - cg.b_S);
        b_ab.a(var16);
        b_ab.a(var14);
        b_ab.a(var18);
        b_ab.a(this.a_Image, this.a_AI[0]);
      }
    }

    this.a();
    this.n();
    this.o();
    this.b();
    this.c();
    this.p();
    this.d();
  }

  public final void a(Graphics var1) {
    if (da.a_da.a_S % 12 == 0) {
      this.E_B = 3;
    } else {
      this.E_B = 0;
    }

    int[] var4 = this.a_de.a();
    int var7 = var4[0] + 1;
    int var9 = var4[1] + var4[3];
    if (this.p_Image != null) {
      var1.drawImage(this.p_Image, var7, var4[1] + var4[3], 20);
      var9 += this.B_I;
    }

    if (this.q_Image != null) {
      var1.drawImage(this.q_Image, var7, var9, 20);
      var9 += this.C_I;
    }

    var1.drawImage(this.r_Image, var7, var9, 20);
    int var5 = var4[0] + 3;
    int var6 = var4[1] + var4[3];
    if (this.p_Image != null) {
      var6 += this.B_I;
      var1.setColor(2303014);
      var1.fillRect(var4[0] + 2, var6 - 1, this.D_I - 2, 2);
      var1.setColor(14083300);
      var1.fillRect(var4[0] + 3, var6, this.D_I - 4, 1);
    }

    if (this.q_Image != null) {
      var6 += this.C_I;
      var1.setColor(2303014);
      var1.fillRect(var4[0] + 2, var6 - 1, this.D_I - 2, 2);
      var1.setColor(14083300);
      var1.fillRect(var4[0] + 3, var6, this.D_I - 4, 1);
    }

    var7 = var6 + this.h_I;
    var1.setColor(3027509);
    var1.drawRoundRect(var4[0] + 1, var4[1], this.D_I, var7 - var4[1], 4, 4);
    var1.setColor(12371653);
    var1.drawRoundRect(var4[0] + 2, var4[1], this.D_I - 2, var7 - var4[1] - 1, 4, 4);
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      cg.a(
        var1,
        this.x_Image,
        this.x_Image.getWidth(),
        this.x_Image.getHeight() / 3,
        this.E_I,
        var5 + 6 + (this.x_Image.getWidth() >> 1),
        var7 - (this.h_I >> 1),
        3,
        0
      );
      cg.a(
        var1,
        this.x_Image,
        this.x_Image.getWidth(),
        this.x_Image.getHeight() / 3,
        1,
        var5 + 6 + (this.x_Image.getWidth() >> 1) + (this.D_I - this.x_Image.getWidth() - 14),
        var7 - (this.h_I >> 1),
        3,
        0
      );
    } else {
      cg.a(
        var1, this.x_Image, this.x_Image.getWidth(), this.x_Image.getHeight() / 3, 1, var5 + 6 + (this.x_Image.getWidth() >> 1), var7 - (this.h_I >> 1), 3, 0
      );
      cg.a(
        var1,
        this.x_Image,
        this.x_Image.getWidth(),
        this.x_Image.getHeight() / 3,
        this.E_I,
        var5 + 6 + (this.x_Image.getWidth() >> 1) + (this.D_I - this.x_Image.getWidth() - 14),
        var7 - (this.h_I >> 1),
        3,
        0
      );
    }

    var1.drawImage(this.D_Image, var5 + (this.x_Image.getWidth() << 1), var7 - (this.h_I >> 1), 3);
    cg.a(var1, o.a_I, var5 + this.x_Image.getWidth() * 3, var7 - (this.h_I >> 1), this.H_Image, 6, 8, 3);
    this.a_de.a(var1);
    if (o_B == 1) {
      if (this.z_I == 2) {
        this.m(var1);
      } else if (this.z_I == 3) {
        this.l(var1);
      } else if (this.z_I == 9) {
        this.h(var1);
      } else if (this.z_I == 5) {
        this.k(var1);
      } else if (this.z_I == 6) {
        this.j(var1);
      } else if (this.z_I == 7) {
        this.i(var1);
      } else if (this.z_I == 4) {
        this.g(var1);
      } else if (this.z_I == 8) {
        this.f(var1);
      } else if (this.z_I == 13) {
        this.e(var1);
      } else if (this.z_I == 14) {
        this.c(var1);
      } else if (this.z_I == 15) {
        this.b(var1);
      } else if (this.z_I == 12) {
        this.a_bc.a(var1);
        int var2 = this.a_AI[0];
        cg.a(var1, "Sẽ đổi thành hình dạng sau", this.a_AI[0] + this.a_AI[2] / 2 + 10, this.a_AI[1] + this.a_AI[3] + this.B_I, 9240572, 2315140, 17);
        int var3 = this.a_AI[1] + this.a_AI[3] + this.B_I + this.C_I - 20;
        int var14 = this.a_AI[2];
        k.a(var1, c_bx, var2 + (var14 >> 1), var3, true, 0);
      }
    } else if (o_B == 2) {
      this.m(var1);
    } else if (o_B == 3) {
      this.l(var1);
    } else if (o_B == 9) {
      this.h(var1);
    } else if (o_B == 5) {
      this.k(var1);
    } else if (o_B == 6) {
      this.j(var1);
    } else if (o_B == 7) {
      this.i(var1);
    } else if (o_B == 4) {
      this.g(var1);
    } else if (o_B == 8) {
      this.f(var1);
    } else if (o_B == 13) {
      this.e(var1);
    } else if (o_B == 14) {
      this.c(var1);
    } else if (o_B == 15) {
      this.b(var1);
    } else if (o_B == 12) {
      this.a_bc.a(var1);
      int var10 = this.a_AI[0];
      int var12 = this.a_AI[1] + this.a_AI[3] + this.B_I + this.C_I - 20;
      int var15 = this.a_AI[2];
      k.a(var1, c_bx, var10 + (var15 >> 1), var12, true, 0);
      cg.a(var1, "Sẽ đổi thành hình dạng sau", this.a_AI[0] + this.a_AI[2] / 2 + 10, this.a_AI[1] + this.a_AI[3] + this.B_I, 9240572, 2315140, 17);
    }

    Graphics var13 = var1;
    as var11 = this;
    var1.translate(0, 0);
    if (this.h_Z) {
      if (this.F_I > 0) {
        if (this.c_String != null && !this.c_String.equals("")) {
          String[] var8 = cg.a(this.c_String, this.x_I - 32);
          var5 = this.y_I / 2 - var8.length * cg.b_S / 2 - 5;
          var6 = this.x_I - 24;
          var7 = var8.length * cg.b_S + 10;
          var1.setColor(1381651);
          var1.fillRoundRect(11, var5 - 1, var6 + 3, var7 + 3, 8, 8);
          var1.setColor(16053217);
          var1.drawRoundRect(12, var5, var6, var7, 8, 8);
          var1.setColor(6768205);
          var1.fillRect(14, var5 + 2, var6 - 3, var7 - 3);

          for (int var17 = 0; var17 < var8.length; var17++) {
            var13.setColor(16777215);
            vh.drawString(var13, var8[var17], var11.x_I / 2, var11.y_I / 2 - var8.length * cg.b_S / 2 + var17 * cg.b_S, 17);
          }
        }

        var11.F_I--;
        return;
      }

      this.c_String = null;
      this.h_Z = false;
    }
  }

  private void b(Graphics var1) {
    if (B_B == 0 || B_B == 1) {
      if (this.a_dc != null) {
        this.a_dc.a();
      } else {
        this.a_dc = new dc();
      }

      b_ab.a(var1);
      int var2 = this.a_AI[0];
      int var4 = cg.b_S + 5;
      int var3 = this.a_AI[1] + this.a_AI[3];
      cg.a(var1, "Tên", var2 + 10, var3 + 2, 9240572, 2315140, 20);
      cg.a(var1, "Giá", var2 + 10 + this.a_AI[2] * 3 / 4, var3 + 2, 16771918, 9063443, 24);
      var1.setColor(2237991);
      var1.fillRect(var2 + 4, var3 + var4 - 1, this.a_AI[2] - 8, 3);
      var1.setColor(14214117);
      var1.fillRect(var2 + 4, var3 + var4, this.a_AI[2] - 8, 1);
      var3 += this.B_I;
      if (y_B == 0) {
        var4 = o.d_I(b_ab.a_I(b_ab.b_I()));
        this.a_dc.a(o.g_AString[var4], this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(var2 + 4, var3 - 2, this.a_AI[2] - 10, this.C_I, this.C_I / cg.b_S);
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      } else if (y_B == 1) {
        var4 = o.f_I(b_ab.a_I(b_ab.b_I()));
        if (this.a_ck == null || this.a_ck.a_S != b_ab.a_I(b_ab.b_I())) {
          this.a_ck = new ck(b_ab.a_I(b_ab.b_I()));
        }

        this.a_dc.a(ck.a_AString[var4], this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(ck.c_AString[var4], this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(ck.a(this.a_ck), this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(var2 + 4, var3 - 2, this.a_AI[2] - 10, this.C_I, this.C_I / cg.b_S);
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      }

      if (B_B == 1) {
        short var5;
        if (y_B == 1) {
          var4 = o.f_I(b_ab.a_I(b_ab.b_I()));
          var5 = ck.c_AS[var4];
        } else {
          var4 = o.d_I(b_ab.a_I(b_ab.b_I()));
          var5 = o.h_AS[var4];
        }

        this.a(var1, true, 99, var5, k_I);
      }
    }
  }

  private void c(Graphics var1) {
    if (A_B == 0) {
      if (this.a_dc != null) {
        this.a_dc.a();
      } else {
        this.a_dc = new dc();
      }

      int var10 = this.a_AI[0];
      int var13 = cg.b_S + 5;
      int var11 = this.a_AI[1] + this.a_AI[3];
      cg.a(var1, "Tên", var10 + 10, var11 + 2, 9240572, 2315140, 20);
      cg.a(var1, "Giá", this.a_AI[0] + this.a_AI[2] * 3 / 4 + 10, var11 + 2, 16771918, 9063443, 24);
      var1.setColor(2237991);
      var1.fillRect(var10 + 4, var11 + var13 - 1, this.a_AI[2] - 8, 3);
      var1.setColor(14214117);
      var1.fillRect(var10 + 4, var11 + var13, this.a_AI[2] - 8, 1);
      if (a_ab != null && a_ab.a_I() > 0) {
        a_ab.a(var1);
        var11 += this.B_I;
        String var14 = "";
        int var6 = o.c_I(a_ab.a_I(a_ab.b_I()));
        short[][] var7 = d.a_AAAS[var6];
        int var8 = 0;

        for (int var9 = var7.length; var8 < var9; var8++) {
          if (var7[var8][0] == c_I) {
            var14 = var14 + "‚ Cấp:" + var7[var8][1];
            break;
          }
        }

        String var15 = var14 + "; Cần vàng:" + d.d_AS[var6] + ".";
        this.a_dc.a(d.c_AString[var6] + var15, this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(var10 + 4, var11, this.a_AI[2] - 10, this.C_I, this.C_I / cg.b_S);
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      }
    } else {
      if (A_B == 1 || A_B == 2) {
        this.d(var1);
        if (A_B == 2) {
          var1.setColor(1381651);
          int var2 = this.a_AI[0] + this.a_AI[2] / 4;
          int var3 = this.a_AI[1] + this.a_AI[3] + this.y_I / 4;
          int var4 = this.a_AI[2] / 2 + 10;
          var1.fillRoundRect(var2 - 2, var3 - 2, var4 + 3, 53, 8, 8);
          var1.setColor(16053217);
          var1.drawRoundRect(var2, var3, var4, 50, 8, 8);
          var1.setColor(6702925);
          var1.fillRect(var2 + 2, var3 + 2, var4 - 3, 47);
          var1.setColor(14017767);
          vh.drawString(var1, "Xác nhận học kỹ năng", var2 + var4 / 2, var3, 17);
          int var5 = o.c_I(a_ab.a_I(a_ab.b_I()));
          cg.a(var1, d.a_AString[var5], var2 + 4, var3 + 15 + 2, 16510027, 8866072, 20);
          var1.setColor(14017767);
          vh.drawString(var1, " ?", var2 + 8 + vh.stringWidth(cg.a_Font, d.a_AString[var5]), var3 + 15 + 2, 17);
          if (at.a_String.equals("E2") || at.a_String.equals("V8")) {
            cg.a(var1, this.u_Image, this.u_Image.getWidth() / 2, this.u_Image.getHeight(), 1, var2 + 4, var3 + 50 - 4, 36, 0);
            cg.a(var1, this.u_Image, this.u_Image.getWidth() / 2, this.u_Image.getHeight(), 0, var2 + var4 - 4, var3 + 50 - 4, 40, 0);
            return;
          }

          cg.a(var1, this.u_Image, this.u_Image.getWidth() / 2, this.u_Image.getHeight(), 0, var2 + 4, var3 + 50 - 4, 36, 0);
          cg.a(var1, this.u_Image, this.u_Image.getWidth() / 2, this.u_Image.getHeight(), 1, var2 + var4 - 4, var3 + 50 - 4, 40, 0);
        }
      }
    }
  }

  private void d(Graphics var1) {
    int var2 = this.C_I;
    int var3 = this.a_AI[1] + this.a_AI[3];
    int var4 = 1;
    int var5 = this.a_AI[0] + 8;
    if (c_I == 1 || c_I == 2) {
      var4 = 1;
    } else if (c_I == 4 && a_Acv != null) {
      var4 = a_Acv.length;
    }

    short var6 = (short)(cg.b_S << 1);
    l_I = (byte)(var2 / var6);
    short var7;
    if (var2 % var6 != 0) {
      var7 = (short)(0 + var2 % var6 / 2 + var3);
    } else {
      var7 = (short)(0 + var3 + 0);
    }

    if (var4 > l_I) {
      k.a(var1, null, null, this.a_AI[0] + this.a_AI[2] - 10, var7, var6 * l_I, var4, l_I, n_I);
    }

    if (c_I == 1 || c_I == 2) {
      cg.b(var1, a_bl.a_String, this.a_AI[0] + 5, var7, 9502459, 540331, 20);
      cg.a(var1, a_bl.a_AI[0], this.x_I / 2 + 26 + 30, var7 + cg.b_S / 2, this.d_Image, 6, 7, 3);
      if (this.B_Image == null) {
        this.B_Image = cc.a("/pic/D08.gf");
      }

      if (this.B_Image != null) {
        var1.drawImage(this.B_Image, this.x_I / 2 + 26 + 50, var7 + cg.b_S / 2, 3);
      }

      if (this.y_Image == null) {
        this.y_Image = cc.a("/pic/D13.gf");
      }

      cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 0, var5 + 10, var7 + 6 + cg.b_S + 4, 3, 0);
      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4, 20);
      cg.a(var1, a_bl.a_AI[3], var5 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4, this.d_Image, 6, 7, 24);
      cg.a(var1, a_bl.a_AI[10], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4, this.d_Image, 6, 7, 20);
      cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 1, var5 + 6 + this.a_AI[2] / 2, var7 + 6 + cg.b_S + 4, 3, 0);
      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, this.x_I / 2 + 26 + 6 + 24 + 2, var7 + 3 + cg.b_S + 4, 20);
      cg.a(var1, a_bl.a_AI[5], this.x_I / 2 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4, this.d_Image, 6, 7, 24);
      cg.a(var1, a_bl.a_AI[11], this.x_I / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4, this.d_Image, 6, 7, 20);
    } else if (c_I == 4) {
      for (int var8 = 0; var8 < l_I; var8++) {
        if (n_I + var8 < a_Acv.length) {
          cg.b(var1, a_Acv[n_I + var8].a_String, this.a_AI[0] + 5, var7 + var6 * var8, 9502459, 540331, 20);
          if (this.y_Image == null) {
            this.y_Image = cc.a("/pic/D13.gf");
          }

          if (this.B_Image == null) {
            this.B_Image = cc.a("/pic/D08.gf");
          }

          if (this.B_Image != null) {
            var1.drawImage(this.B_Image, this.x_I / 2 + 26 + 50, var7 + var6 * var8 + cg.b_S / 2, 3);
          }

          cg.a(var1, a_Acv[n_I + var8].a_AI[0], this.x_I / 2 + 26 + 30, var7 + var6 * var8 + cg.b_S / 2, this.d_Image, 6, 7, 3);
          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 0, var5 + 10, var7 + 6 + cg.b_S + 4 + var6 * var8, 3, 0);
          var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var8, 20);
          cg.a(var1, a_Acv[n_I + var8].a_AI[1], var5 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var8, this.d_Image, 6, 7, 24);
          cg.a(var1, a_Acv[n_I + var8].a_AI[2], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var8, this.d_Image, 6, 7, 20);
          cg.a(
            var1,
            this.y_Image,
            this.y_Image.getWidth(),
            this.y_Image.getHeight() / 10,
            1,
            var5 + 6 + this.a_AI[2] / 2,
            var7 + 6 + cg.b_S + 4 + var6 * var8,
            3,
            0
          );
          var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, this.x_I / 2 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var8, 20);
          cg.a(var1, a_Acv[n_I + var8].a_AI[4], this.x_I / 2 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var8, this.d_Image, 6, 7, 24);
          cg.a(var1, a_Acv[n_I + var8].a_AI[5], this.x_I / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var8, this.d_Image, 6, 7, 20);
        }
      }
    }

    if (this.a_Image != null) {
      var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_AI[0] + 8 + this.E_B, var7 + var6 / 2 + var6 * (m_I - n_I), 3);
    }
  }

  private void e(Graphics var1) {
    int var2 = this.C_I;
    int var3 = this.a_AI[1] + this.a_AI[3];
    int var4 = 1;
    int var5 = this.a_AI[0] + 8;
    if (a_Acv != null) {
      var4 = 1 + a_Acv.length;
    }

    short var6 = (short)(cg.b_S << 1);
    C_B = (byte)(var2 / var6);
    short var7;
    if (var2 % var6 != 0) {
      var7 = (short)(0 + var2 % var6 / 2 + var3);
    } else {
      var7 = (short)(0 + var3 + 0);
    }

    var3 = C_B;
    if (var4 < var3) {
      var3 = var4;
    }

    if (var4 > C_B) {
      k.a(var1, null, null, this.a_AI[0] + this.a_AI[2] - 10, var7, var6 * C_B, var4, C_B, n_B);
    }

    for (int var9 = 0; var9 < var3; var9++) {
      if (var9 + n_B == 0) {
        cg.b(var1, a_bl.a_String, this.a_AI[0] + 5, var7 + var6 * var9, 9502459, 540331, 20);
        cg.a(var1, a_bl.a_AI[0], this.x_I / 2 + 26 + 30, var7 + var6 * var9 + cg.b_S / 2, this.d_Image, 6, 7, 3);
        if (this.B_Image == null) {
          this.B_Image = cc.a("/pic/D08.gf");
        }

        if (this.B_Image != null) {
          var1.drawImage(this.B_Image, this.x_I / 2 + 26 + 50, var7 + var6 * var9 + cg.b_S / 2, 3);
        }

        if (this.y_Image == null) {
          this.y_Image = cc.a("/pic/D13.gf");
        }

        cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 0, var5 + 10, var7 + 6 + cg.b_S + 4 + var6 * var9, 3, 0);
        var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, 20);
        cg.a(var1, a_bl.a_AI[3], var5 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 24);
        cg.a(var1, a_bl.a_AI[10], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 20);
        cg.a(
          var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 1, var5 + 6 + this.a_AI[2] / 2, var7 + 6 + cg.b_S + 4 + var6 * var9, 3, 0
        );
        var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, this.x_I / 2 + 26 + 6 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, 20);
        cg.a(var1, a_bl.a_AI[5], this.x_I / 2 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 24);
        cg.a(var1, a_bl.a_AI[11], this.x_I / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 20);
      } else {
        cg.b(var1, a_Acv[n_B + var9 - 1].a_String, this.a_AI[0] + 5, var7 + var6 * var9, 9502459, 540331, 20);
        if (this.y_Image == null) {
          this.y_Image = cc.a("/pic/D13.gf");
        }

        if (this.B_Image == null) {
          this.B_Image = cc.a("/pic/D08.gf");
        }

        cg.a(var1, a_Acv[n_B + var9 - 1].a_AI[0], this.x_I / 2 + 26 + 30, var7 + var6 * var9 + cg.b_S / 2, this.d_Image, 6, 7, 3);
        if (this.B_Image != null) {
          var1.drawImage(this.B_Image, this.x_I / 2 + 26 + 50, var7 + var6 * var9 + cg.b_S / 2, 3);
        }

        cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 0, var5 + 10, var7 + 6 + cg.b_S + 4 + var6 * var9, 3, 0);
        var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var5 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, 20);
        cg.a(var1, a_Acv[n_B + var9 - 1].a_AI[1], var5 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 24);
        cg.a(var1, a_Acv[n_B + var9 - 1].a_AI[2], var5 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 20);
        cg.a(
          var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 1, var5 + 6 + this.a_AI[2] / 2, var7 + 6 + cg.b_S + 4 + var6 * var9, 3, 0
        );
        var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, this.x_I / 2 + 26 + 2 + 24 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, 20);
        cg.a(var1, a_Acv[n_B + var9 - 1].a_AI[4], this.x_I / 2 + 26 + 2 + 24, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 24);
        cg.a(var1, a_Acv[n_B + var9 - 1].a_AI[5], this.x_I / 2 + 26 + 2 + 24 + 2 + 6 + 2, var7 + 3 + cg.b_S + 4 + var6 * var9, this.d_Image, 6, 7, 20);
      }
    }

    if (this.a_Image != null) {
      var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_AI[0] + 8 + this.E_B, var7 + var6 / 2 + var6 * (m_B - n_B), 3);
    }
  }

  private void f(Graphics var1) {
    int var2 = this.a_AI[0];
    int var3 = this.a_AI[1] + this.a_AI[3];
    int var4 = cg.b_S + 8;
    var1.setColor(2237991);
    if (z_B != 0) {
      int var22 = this.a_AI[2];
      this.a(var1, a_AB);
      var1.setClip(var2, var3, this.a_AI[2] >> 1, this.B_I);
      var3 += this.B_I;
      if (c_bx != null) {
        k.a(var1, c_bx, var2 + (this.a_AI[2] >> 2), var3, true, 0);
      }

      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      cg.b(var1, g_AString[a_I], var2 + 8, var3, 9502459, 540331, 36);
      if (z_B == 1) {
        var3 += 4;
        var2 += 8;
        byte[] var27 = cg.b_AB(a_AAString[a_I][18], ",");
        if (o.f_AString == null) {
          o.d();
        }

        var4 = o.d_I(var27[0]);
        int var28 = vh.stringWidth(cg.a_Font, "Gen đặc trưng") + 3;
        cg.b(var1, "Gen đặc trưng", var2, var3, 16706636, 9128217, 20);
        var1.setColor(16777215);
        vh.drawString(var1, o.f_AString[var4], var2 + var28, var3, 20);
        if (this.a_dc != null) {
          this.a_dc.a();
        } else {
          this.a_dc = new dc();
        }

        this.a_dc.a(o.g_AString[var4], this.a_AI[2] - 10, null, 13695736);
        this.a_dc.a(var2 + 4, var3 + cg.b_S, this.a_AI[2] - 10, cg.b_S, 1);
        this.a_dc.a(var1, this.a_dc, 1, true, false);
        if (b_AB != null) {
          var3 = 2 + this.a_AI[1] + this.a_AI[3] + this.B_I + this.C_I - 48;
          if (this.y_Image == null) {
            this.y_Image = cc.a("/pic/D13.gf");
          }

          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 0, var2, var3, 20, 0);
          var3 -= 3;
          a(var1, this.f_Image, var2 + 30, var3 + 8, b_AB[0], 3);
          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 3, var2 + var22 / 2, var3, 20, 0);
          a(var1, this.f_Image, var2 + 30 + var22 / 2, var3 + 8, b_AB[2], 3);
          var3 += 16;
          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 1, var2, var3, 20, 0);
          a(var1, this.f_Image, var2 + 30, var3 + 8, b_AB[1], 3);
          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 4, var2 + var22 / 2, var3, 20, 0);
          a(var1, this.f_Image, var2 + 30 + var22 / 2, var3 + 8, b_AB[3], 3);
          var3 += 16;
          cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, 5, var2, var3, 20, 0);
          a(var1, this.f_Image, var2 + 30, var3 + 8, b_AB[4], 3);
        }

        return;
      }

      if (z_B == 2) {
        if (this.a_db != null) {
          this.a_db.a(var1);
        }

        var22 = this.x_I * 3 / 4;
        var2 = (this.x_I >> 1) - (var22 >> 1);
        var3 = (var3 = this.y_I >> 1) - 2 * cg.b_S;
        var4 = 2 * cg.b_S + 10;
        if (this.f_Z && !this.e_Z && this.o_I < 100) {
          var1.setColor(1381651);
          var1.fillRoundRect(var2 - 1, var3 - 1, var22 + 3, var4 + 3, 8, 8);
          var1.setColor(16053217);
          var1.drawRoundRect(var2, var3, var22, var4, 8, 8);
          var1.setColor(6768205);
          var1.fillRect(var2 + 2, var3 + 2, var22 - 3, var4 - 3);
          var2 += 2;
          var22 -= 8;
          this.o_I += 5;
          var1.setColor(9262239);
          var1.drawRect(var2 + 1, var3 + var4 / 2, var22 - 2, 5);
          var1.setColor(16106751);
          var1.fillRect(var2 + 2, var3 + 1 + var4 / 2, (var22 - 4) * this.o_I / 100, 4);
          String var26 = "Đang hợp thành";
          var1.setColor(16777215);
          vh.drawString(var1, var26, this.x_I >> 1, var3 + var4, 33);
        } else if (this.o_I >= 100) {
          this.e_Z = true;
        }

        if (this.e_Z) {
          if (this.i_I > 0) {
            this.m();
            byte var14 = 2;
            this.E_I = var14;
            o.a_V(this.i_I, -1);
            a_Acv = k.a_Acv();
            this.a("Chúc mừng bạn có thêm một sủng vật mới", 10);
          }

          z_B = 1;
          this.e_Z = false;
          this.f_Z = false;
          this.o_I = 0;
        }
      }
    } else {
      var1.fillRect(var2 + 4, var3 + var4 - 1, this.a_AI[2] - 8, 3);
      var1.fillRect(var2 + 4, var3 + this.C_I - var4 - 1, this.a_AI[2] - 8, 3);
      var1.setColor(14214117);
      var1.fillRect(var2 + 4, var3 + cg.b_S + 8, this.a_AI[2] - 8, 1);
      var1.fillRect(var2 + 4, var3 + this.C_I - var4, this.a_AI[2] - 8, 1);
      int var5 = 0;
      if (this.d_bc != null) {
        var5 = this.d_bc.b();
      }

      if (this.o_Image != null) {
        cg.a(var1, this.o_Image, this.o_Image.getWidth(), this.o_Image.getHeight() >> 1, 0, var2 + 10, var3 + (var4 >> 1), 6, 0);
        cg.a(var1, this.o_Image, this.o_Image.getWidth(), this.o_Image.getHeight() >> 1, 1, var2 + (this.a_AI[2] >> 1), var3 + (var4 >> 1), 3, 0);
        cg.a(var1, var5, var2 - 6 + 14 + this.a_AI[2] * 3 / 4, var3 + (var4 >> 1), this.d_Image, 6, 7, 10);
        var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var2 + 14 + this.a_AI[2] * 3 / 4, var3 + (var4 >> 1), 6);
        cg.a(var1, 140, var2 + 6 + 14 + this.a_AI[2] * 3 / 4, var3 + (var4 >> 1), this.d_Image, 6, 7, 6);
      } else {
        cg.a(var1, "Đã phát hiện:", var2 + (this.a_AI[2] >> 2), var3 + 4, 16771916, 8931863, 17);
        cg.a(var1, " " + var5 + "/140", var2 + (this.a_AI[2] >> 1), var3 + 4, 16646121, 7545885, 17);
      }

      if (this.E_Image != null) {
        cg.a(var1, this.E_Image, this.E_Image.getWidth() / 2, this.E_Image.getHeight(), 0, var2 + 4, var3 + this.C_I - var4 / 2, 6, 0);
        cg.a(var1, this.E_Image, this.E_Image.getWidth() / 2, this.E_Image.getHeight(), 1, var2 - 4 + this.a_AI[2], var3 + this.C_I - var4 / 2, 10, 0);
      }

      if (this.F_Image != null) {
        for (int var6 = 0; var6 < 3; var6++) {
          cg.a(
            var1,
            this.F_Image,
            this.F_Image.getWidth(),
            this.F_Image.getHeight() / 3,
            var6,
            var2 + this.a_AI[2] / 4 + var6 * 50,
            var3 + this.C_I - var4 / 2,
            3,
            0
          );
        }

        if (this.e_bx != null) {
          k.a(var1, this.e_bx, var2 + this.a_AI[2] / 4 + this.b_I * 50, var3 + this.C_I - var4 / 2, true, 0);
        }
      } else {
        for (int var25 = 0; var25 < 3; var25++) {
          cg.a(var1, this.h_AString[var25], var2 - 4 + this.a_AI[2] / 4 + var25 * 48, var3 + this.C_I - (var4 + cg.b_S) / 2, 16771916, 8931863, 17);
        }

        var1.setColor(65280);
        var1.drawRect(var2 + this.a_AI[2] / 4 - cg.a_S - 6 + this.b_I * 48, var3 + this.C_I - (var4 + cg.b_S) / 2, (cg.a_S << 1) + 4, cg.b_S + 2);
      }

      var3 += var4;
      if (this.b_I == 0) {
        if (this.d_bc != null) {
          this.d_bc.a(var1);
        } else {
          var1.setColor(16777215);
          vh.drawString(var1, "Bạn chưa mở đồ giám", var2 + this.a_AI[2] / 2, var3, 17);
        }
      } else {
        if (this.b_I != 1) {
          if (this.b_I == 2 && this.b_bc != null) {
            this.b_bc.a(var1);
          }

          return;
        }

        if (this.c_bc == null) {
          return;
        }

        this.c_bc.a(var1);
      }
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

  private void g(Graphics var1) {
    int var2 = this.a_AI[0] + (this.A_I >> 1) - 2;
    int var4 = this.B_I / 3;
    int var3 = this.a_AI[1] + this.a_AI[3] + (var4 >> 1);
    int[][] var5;
    (var5 = new int[3][2])[0][0] = a_bl.a_AI[3];
    var5[0][1] = a_bl.a_AI[10];
    var5[1][0] = a_bl.a_AI[5];
    var5[1][1] = a_bl.a_AI[11];
    var5[2][0] = a_bl.a_AI[1];
    var5[2][1] = a_bl.a_AI[2];
    this.a(var1, var2 - 10, var3, var4, var5);
    var2 = this.a_AI[0] + 4;
    var3 = this.a_AI[1] + this.a_AI[3] + 4;
    var4 = (this.A_I >> 1) - 22;
    int var20 = this.B_I - this.C_Image.getHeight() / 2 - 12;
    this.b(var1, var2, var3, var4, var20);
    if (x_B == 0) {
      var3 += this.B_I;
      if (this.d_ab != null && this.d_ab.a_I() > 0) {
        this.d_ab.a(var1);
      }

      var2 += this.A_I / 2;
      var3 += this.C_I;
      var3 -= 5;
      if (this.z_Image != null) {
        var1.drawImage(this.z_Image, var2, var3, 33);
        var2 += this.z_Image.getWidth() + 10;
      }

      cg.a(var1, a_bl.a_Ad[this.d_ab.b_I()].a_I, var2, var3, this.d_Image, 6, 7, 40);
      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var2, var3, 36);
      var4 = 0;
      if (!cg.a_Z(new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38}, this.d_ab.a_I(this.d_ab.b_I()))) {
        var4 = a_bl.a_Ad[this.d_ab.b_I()].b_I;
      }

      if (this.d_ab.a_I(this.d_ab.b_I()) == 39) {
        var4 = 0;
      }

      cg.a(var1, var4, var2 + 6, var3, this.d_Image, 6, 7, 36);
    } else {
      if (x_B == 1) {
        if (this.a_dc != null) {
          this.a_dc.a();
        } else {
          this.a_dc = new dc();
        }

        var4 = o.c_I(this.d_ab.a_I(this.d_ab.b_I()));
        if (d.a_AImage == null) {
          d.b();
        }

        var2 = this.a_AI[0] + 6;
        var1.drawImage(d.a_AImage[var4], var2, this.a_AI[1] + this.a_AI[3] + this.B_I + 2, 20);
        var2 += d.a_AImage[var4].getWidth();
        cg.a(var1, d.a_AString[var4], var2, this.a_AI[1] + this.a_AI[3] + this.B_I + 4, 16580583, 6036810, 20);
        if (this.J_Image == null) {
          this.J_Image = cc.b("/pic/number05.gf", 2);
        }

        var3 = this.a_AI[1] + this.a_AI[3] + this.B_I;
        var2 += vh.stringWidth(cg.a_Font, d.a_AString[var4]);
        cg.b(var1, "" + a_bl.a_Ad[this.d_ab.b_I()].a_B, var2 + 3, var3 + 4, 16550572, 8856870, 20);
        var2 += vh.stringWidth(cg.a_Font, "00");
        cg.b(var1, "  cấp", var2, var3 + 4, 16776937, 6036812, 20);
        this.a_dc.a(d.c_AString[var4], this.A_I - 20, null, 16777215);
        this.a_dc
          .a(
            this.a_AI[0] + 5,
            this.a_AI[1] + this.a_AI[3] + this.B_I + vh.getHeight(cg.a_Font),
            this.A_I - 20,
            this.C_I - vh.getHeight(cg.a_Font) - 6,
            (this.C_I - vh.getHeight(cg.a_Font) - 6) / cg.b_S
          );
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      }
    }
  }

  private void h(Graphics var1) {
    int[] var2;
    int var3 = (var2 = this.a_de.a())[0];
    int var4 = var2[1] + var2[3];
    if (p_B == 0) {
      if (this.c_ab != null) {
        this.c_ab.a(var1);
        return;
      }
    } else {
      if (p_B == 1 || p_B == 2 || p_B == 5) {
        if (p_B != 1 && D_B != 1) {
          if (p_B == 2 || D_B == 2) {
            if (this.w_Image != null) {
              cg.a(var1, this.w_Image, this.w_Image.getWidth(), this.w_Image.getHeight() / 2, 0, var3 + var2[2] / 2, var4 + (this.B_I >> 1), 3, 0);
            } else {
              cg.a(var1, "Chọn bản ghi", var3 + var2[2] / 2, var4 + (this.B_I >> 1) - cg.b_S / 2, 14286332, 2979009, 17);
            }
          }
        } else if (this.w_Image != null) {
          cg.a(var1, this.w_Image, this.w_Image.getWidth(), this.w_Image.getHeight() / 2, 0, var3 + var2[2] / 2, var4 + (this.B_I >> 1), 3, 0);
        } else {
          cg.a(var1, "Chọn bản ghi", var3 + var2[2] / 2, var4 + (this.B_I >> 1) - cg.b_S / 2, 14286332, 2979009, 17);
        }

        var4 += this.B_I;
        int var21 = this.C_I / 2;

        for (int var22 = 0; var22 < at.e_B; var22++) {
          var3 = var2[0];
          new StringBuffer().append("Ghi ").append(var22 + 1).toString();
          if (D_B == 0 && this.a_Image != null && o_B == 9) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var3 + 5 + this.E_B, var4 + var21 / 4 + l_B * 81, 3);
          }

          if (o.c_Z) {
            if (o.c_AB[var22] == 1 && a_Abx != null && a_Abx[var22] != null) {
              a_Abx[var22].d_S = (short)(var3 + var2[2] / 4);
              a_Abx[var22].e_S = (short)(var4 - cg.b_S + var21 + var21 * var22);
              if (var22 == l_B) {
                a_Abx[var22].a_V(0);
              } else {
                a_Abx[var22].a_V(4);
              }

              a_Abx[var22].a(var1);
              a_Abx[var22].a(true);
              var3 = var2[0] - 15 + var2[2] / 2;
              cg.a(var1, o.i_AString[var22], var2[0] - 10 + var2[2], var4 + var21 * var22, 3532779, 146244, 24);
              if (this.C_Image != null) {
                cg.a(
                  var1,
                  this.C_Image,
                  this.C_Image.getWidth(),
                  this.C_Image.getHeight() >> 1,
                  o.j_AS[var22] - 1,
                  var3 + (this.C_Image.getWidth() >> 1),
                  var4 + (cg.b_S << 1) + var21 * var22,
                  3,
                  0
                );
              }

              cg.a(var1, o.k_AS[var22], var3 + var2[2] / 2 - 40, var4 + (cg.b_S << 1) + var21 * var22, this.d_Image, 6, 7, 3);
              var1.drawImage(this.B_Image, var3 + var2[2] / 2 - 15, var4 + (cg.b_S << 1) + var21 * var22, 3);
              int var10003 = var4 + var21 * var22 + cg.b_S * 3;
              short var10004 = o.l_AS[var22];
              byte var10005 = o.d_AB[var22];
              byte var10006 = o.e_AB[var22];
              byte var10007 = o.f_AB[var22];
              byte var14 = o.g_AB[var22];
              byte var13 = var10007;
              byte var12 = var10006;
              byte var11 = var10005;
              short var10 = var10004;
              int var9 = var10003;
              if (this.d_Image != null) {
                cg.a(var1, var10, var3, var9, this.d_Image, 6, 7, 20);
                var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var3 + 26, var9, 20);
                if (var11 < 10) {
                  var1.drawRegion(this.d_Image, 0, 0, 6, 7, 0, var3 + 30, var9, 20);
                  cg.a(var1, var11, var3 + 36, var9, this.d_Image, 6, 7, 20);
                } else {
                  cg.a(var1, var11, var3 + 30, var9, this.d_Image, 6, 7, 20);
                }

                var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var3 + 42, var9, 20);
                if (var12 < 10) {
                  var1.drawRegion(this.d_Image, 0, 0, 6, 7, 0, var3 + 48, var9, 20);
                  cg.a(var1, var12, var3 + 53, var9, this.d_Image, 6, 7, 20);
                } else {
                  cg.a(var1, var12, var3 + 48, var9, this.d_Image, 6, 7, 20);
                }

                cg.a(var1, var13, var3 + 72, var9, this.d_Image, 6, 7, 24);
                if (var13 < 10) {
                  var1.drawRegion(this.d_Image, 0, 0, 6, 7, 0, var3 + 60, var9, 20);
                }

                var1.drawRegion(this.d_Image, 78, 0, 6, 7, 0, var3 + 72, var9, 20);
                cg.a(var1, var14, var3 + 90, var9, this.d_Image, 6, 7, 24);
                if (var14 < 10) {
                  var1.drawRegion(this.d_Image, 0, 0, 6, 7, 0, var3 + 78, var9, 20);
                }
              }
            }
          } else {
            var1.setColor(16777215);
            vh.drawString(var1, " Trống ", this.x_I / 2, var4 + var21 * var22, 17);
          }
        }

        if (p_B == 5) {
          k.a(var1, a_String, this.x_I, this.y_I);
        }

        return;
      }

      if (p_B == 3) {
        if (this.w_Image != null) {
          cg.a(var1, this.w_Image, this.w_Image.getWidth(), this.w_Image.getHeight() / 2, 1, this.x_I / 2, var2[1] + var2[3] + (this.B_I >> 1), 3, 0);
        } else {
          cg.b(var1, "Âm lượng", this.x_I / 2, var2[1] + var2[3] - cg.b_S / 2 + (this.B_I >> 1), 14286332, 2979009, 17);
        }

        var1.setColor(12961221);
        int var5 = (this.x_I - 90 - 27) / 2;
        int var6 = this.y_I / 2 + 20;
        int var15 = o.g_B / 10;
        if (this.b_bx != null) {
          this.b_bx.a(this.x_I >> 1, (this.y_I >> 1) - 20);
          this.b_bx.a(var1);
          if (da.a_da.a_S % 3 == 0) {
            this.b_bx.a(true);
          }

          for (int var16 = 0; var16 < 10; var16++) {
            if (var16 < var15) {
              var1.setColor(16776412);
              var1.fillRect(var5 + var16 * 12, var6, 9, 5);
              var1.setColor(16773198);
              var1.fillRect(var5 + 1 + var16 * 12, var6 + 1, 8, 4);
            } else {
              var1.setColor(15132390);
              var1.fillRect(var5 + var16 * 12, var6, 9, 5);
              var1.setColor(12961221);
              var1.fillRect(var5 + 1 + var16 * 12, var6 + 1, 8, 4);
            }
          }
        } else {
          for (int var17 = 0; var17 < 10; var17++) {
            if (var17 < var15) {
              var1.setColor(16776412);
              var1.fillRect(var5 + var17 * 12, var6 - (var17 << 2), 9, 5 + (var17 << 2));
              var1.setColor(16773198);
              var1.fillRect(var5 + 1 + var17 * 12, var6 + 1 - (var17 << 2), 8, 4 + (var17 << 2));
            } else {
              var1.setColor(15132390);
              var1.fillRect(var5 + var17 * 12, var6 - (var17 << 2), 9, 5 + (var17 << 2));
              var1.setColor(12961221);
              var1.fillRect(var5 + 1 + var17 * 12, var6 + 1 - (var17 << 2), 8, 4 + (var17 << 2));
            }
          }
        }

        if (at.b_Z && var15 != 0) {
          cg.a(var1, o.g_B, this.x_I / 2, var6 + 5 + 4 + cg.b_S / 2, this.d_Image, 6, 7, 3);
        } else {
          var1.setColor(16777215);
          vh.drawString(var1, "Tắt", this.x_I / 2, var6 + 5 + 4, 17);
        }

        cg.a(var1, this.t_Image, this.t_Image.getWidth() / 2, this.t_Image.getHeight(), 0, this.x_I / 2 - cg.b_S - this.E_B, var6 + 5 + 2 + cg.b_S / 2, 20, 0);
        cg.a(var1, this.t_Image, this.t_Image.getWidth() / 2, this.t_Image.getHeight(), 1, this.x_I / 2 + cg.b_S + this.E_B, var6 + 5 + 2 + cg.b_S / 2, 24, 0);
        return;
      }

      if (p_B == 4) {
        k.a(var1, "Trở về menu chính?", this.x_I, this.y_I);
        return;
      }

      if (p_B == 6) {
        if (this.c_ab != null) {
          this.c_ab.a(var1);
        }

        k.a(var1, "Điểm hiện tại: " + o.c_I + ", tải lên không?", this.x_I, this.y_I);
      }
    }
  }

  private void i(Graphics var1) {
    int var2 = this.a_AI[0];
    int var3 = this.a_AI[1] + this.a_AI[3];
    if (w_B == 0 || w_B == 1 || w_B == 2 || w_B == 4 || w_B == 5) {
      for (int var4 = 0; var4 < 2; var4++) {
        if (this.g_Image != null) {
          if (i_B == var4) {
            if (this.a_Image != null && o_B == 7 && w_B == 0) {
              var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 + 10 + this.E_B + var4 * this.A_I / 2, var3 + (this.B_I >> 1), 3);
            }

            cg.a(
              var1,
              this.i_Image,
              this.i_Image.getWidth(),
              this.i_Image.getHeight() >> 1,
              var4,
              var2 + this.A_I / 4 + var4 * this.A_I / 2,
              var3 + (this.B_I >> 1),
              3,
              0
            );
          } else {
            cg.a(
              var1,
              this.g_Image,
              this.g_Image.getWidth(),
              this.g_Image.getHeight() >> 1,
              var4,
              var2 + this.A_I / 4 + var4 * this.A_I / 2,
              var3 + (this.B_I >> 1),
              3,
              0
            );
          }
        } else if (i_B == var4) {
          if (this.a_Image != null && o_B == 7 && w_B == 0) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 + 10 + this.E_B + var4 * this.A_I / 2, var3 + (this.B_I >> 1), 3);
          }

          cg.a(var1, this.i_AString[var4], var2 - 4 + this.A_I / 4 + var4 * this.A_I / 2, var3 + (this.B_I - cg.b_S >> 1), 15987164, 6036307, 17);
        } else {
          cg.a(var1, this.i_AString[var4], var2 - 4 + this.A_I / 4 + var4 * this.A_I / 2, var3 + (this.B_I - cg.b_S >> 1), 9306107, 2380419, 17);
        }
      }

      var3 += this.B_I;
      if (i_B == 0) {
        cg.b(var1, "Tên nhiệm vụ", at.a_AS[0] >> 1, var3 + 2, 16711581, 10833443, 17);
        if (this.a_cy != null && this.a_cy.b() > 0) {
          this.a_cy.a(var1);
        } else {
          var1.setColor(16777215);
          vh.drawString(var1, "Danh sách nhiệm vụ trống", at.a_AS[0] >> 1, at.a_AS[1] >> 1, 17);
        }
      } else if (this.a_bf != null && this.a_bf.b() > 0) {
        this.a_bf.a(var1);
      } else {
        var1.setColor(16777215);
        vh.drawString(var1, "Danh sách nhiệm vụ trống", at.a_AS[0] >> 1, at.a_AS[1] >> 1, 17);
      }

      if (w_B == 4) {
        short var5 = (short)(10 + vh.stringWidth(cg.a_Font, "Thuộc tính"));
        short var7 = (short)((this.y_I - 34) / 2 - 2 * cg.b_S);
        this.a(var1, new int[]{15, 16}, k_B, this.x_I / 2, var7, var5, 44, 6768205);
        return;
      }

      if (w_B == 5) {
        k.a(var1, "Xóa nhiệm vụ đang chọn?", this.x_I, this.y_I);
        return;
      }
    } else if (w_B == 3) {
      if (!at.a_String.equals("N5800")) {
        var2 += bf.a(var1, o.a_Abp[this.p_I].a_B, var2 + 15, var3 + 6);
      }

      var1.setColor(9306109);
      vh.drawString(var1, o.a_Abp[this.p_I].a_String, var2 + 15, var3 + 6, 20);
      if (o.a_Abp[this.p_I].a_Z) {
        cg.a(var1, this.j_Image, this.j_Image.getWidth() / 2, this.j_Image.getHeight(), 0, this.a_AI[0] + this.a_AI[2] - 15, var3 + this.B_I / 2, 3, 0);
      } else {
        cg.a(var1, this.j_Image, this.j_Image.getWidth() / 2, this.j_Image.getHeight(), 1, this.a_AI[0] + this.a_AI[2] - 15, var3 + this.B_I / 2, 3, 0);
      }

      if (this.a_cs != null) {
        this.a_cs.a(var1);
      }
    }
  }

  private void j(Graphics var1) {
    int var2 = this.a_AI[0] + 5 + this.a_AI[2] / 6;
    int var3 = this.a_AI[1] + this.a_AI[3] + (this.B_I >> 1);
    int var4 = this.a_AI[0] + 8;
    int var5;
    int var6 = (var5 = this.a_AI[1] + this.a_AI[3] + this.B_I + 4) + 25 * d_S;

    for (int var7 = 0; var7 < 3; var7++) {
      if (this.l_Image != null) {
        if (var7 == e_B) {
          var1.drawRegion(
            this.l_Image,
            0,
            this.l_Image.getHeight() / 3 * var7,
            this.l_Image.getWidth(),
            this.l_Image.getHeight() / 3,
            0,
            var2 + this.k_Image.getWidth() / 2 + this.a_AI[2] / 4 * var7,
            var3,
            3
          );
          if (this.a_Image != null && o_B == 6 && v_B == 0) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 - 5 + this.E_B + this.a_AI[2] / 4 * var7, var3, 3);
          }
        } else {
          var1.drawRegion(
            this.k_Image,
            0,
            this.k_Image.getHeight() / 3 * var7,
            this.k_Image.getWidth(),
            this.k_Image.getHeight() / 3,
            0,
            var2 + this.k_Image.getWidth() / 2 + this.a_AI[2] / 4 * var7,
            var3,
            3
          );
        }
      } else if (var7 == e_B) {
        cg.a(var1, this.j_AString[var7], var2 + 12 + this.a_AI[2] / 4 * var7, var3 - cg.b_S / 2, 15987164, 6036307, 17);
        if (this.a_Image != null && o_B == 6 && v_B == 0) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 - 5 + this.E_B + this.a_AI[2] / 4 * var7, var3, 3);
        }
      } else {
        cg.a(var1, this.j_AString[var7], var2 + 12 + this.a_AI[2] / 4 * var7, var3 - cg.b_S / 2, 9306107, 2380419, 17);
      }
    }

    for (int var15 = 0; var15 < d_S; var15++) {
      for (int var8 = 0; var8 < c_S; var8++) {
        a(var1, var4 + var8 * 25, var5 + var15 * 25, 15589356, 6963805);
      }
    }

    k.a(var1, null, null, var4 + 25 * c_S, var5, d_S * 25 - 5, ct.a_S / c_S, d_S, f_B);
    if (a_AAAS != null && a_AAAS[e_B] != null) {
      for (int var16 = 0; var16 < d_S; var16++) {
        for (int var9 = 0; var9 < c_S; var9++) {
          if (f_B * c_S + var9 + var16 * c_S < a_AAAS[e_B].length) {
            var3 = o.d_I(a_AAAS[e_B][f_B * c_S + var9 + var16 * c_S][0]);
            if (o.a_AImage[var3] != null) {
              var1.drawImage(o.a_AImage[var3], var4 + 11 + var9 * 25, var5 + 11 + var16 * 25, 3);
            }

            cg.a(var1, a_AAAS[e_B][f_B * c_S + var9 + var16 * c_S][1], var4 + 11 + var9 * 25, var5 + 20 + var16 * 25, this.d_Image, 6, 7, 33);
          }
        }
      }
    } else {
      cg.a(var1, "Không có vật phẩm liên quan", at.a_AS[0] >> 1, var6 - 4 + vh.getHeight(cg.a_Font) / 2, 13695223, 1196461, 17);
    }

    if (v_B != 0 && (v_B == 1 || v_B == 2 || v_B == 4 || v_B == 3)) {
      if (a_AAAS != null && a_AAAS[e_B] != null && g_B < a_AAAS[e_B].length) {
        if (this.a_dc != null) {
          this.a_dc.a();
        } else {
          this.a_dc = new dc();
        }

        int var17 = o.d_I(a_AAAS[e_B][g_B][0]);
        cg.a(var1, o.f_AString[var17], var4, var6 - 4, 13695223, 1196461, 20);
        this.a_dc.a(o.g_AString[var17], this.a_AI[2] - 14 - vh.stringWidth(cg.a_Font, o.f_AString[var17]), null, 13629944);
        this.a_dc
          .a(
            var4 + vh.stringWidth(cg.a_Font, o.f_AString[var17]),
            var6 - 7,
            this.a_AI[2] - 14 - vh.stringWidth(cg.a_Font, o.f_AString[var17]),
            2 * vh.getHeight(cg.a_Font),
            2
          );
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      }

      if (v_B == 1) {
        short var18 = (short)(g_B % c_S);
        short var11 = (short)(g_B / c_S - f_B);
        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var4 + var18 * 25 + this.E_B, var5 + 11 + var11 * 25, 3);
        }

        return;
      }

      if (v_B == 2) {
        short var10 = (short)(10 + vh.stringWidth(cg.a_Font, "Thuộc tính"));
        short var13 = (short)((b_AI.length << 4) + (c_AString.length - 1 << 2));
        short var14 = (short)((this.y_I - var13) / 2 - 2 * cg.b_S);
        this.a(var1, b_AI, h_B, this.x_I / 2, var14, var10, var13, 6768205);
        return;
      }

      if (v_B == 4) {
        this.a(var1, false, a_AAAS[e_B][g_B][1], 0, e_S);
        return;
      }

      if (v_B == 3) {
        this.a(var1, true, a_AAAS[e_B][g_B][1], o.i_AS[o.d_I(a_AAAS[e_B][g_B][0])], e_S);
      }
    }
  }

  public static void j() {
    short[][] var0 = null;
    short[][] var1 = null;
    short[][] var2 = null;
    if (o.a_AAS != null) {
      for (int var3 = 0; var3 < o.a_AAS.length; var3++) {
        int var4 = o.d_I(o.a_AAS[var3][0]);
        short[] var5 = cg.a_AS(cg.a_AS(null, o.a_AAS[var3][0]), o.a_AAS[var3][1]);
        if (o.f_AS[var4] == 1) {
          var0 = cg.a(var0, var5);
        } else if (o.f_AS[var4] == 2) {
          var1 = cg.a(var1, var5);
        } else if (o.f_AS[var4] == 3) {
          var2 = cg.a(var2, var5);
        } else if (o.f_AS[var4] == 4) {
          var2 = cg.a(var2, var5);
        } else if (o.f_AS[var4] == 5) {
          var1 = cg.a(var1, var5);
        } else if (o.f_AS[var4] == 6) {
          var1 = cg.a(var1, var5);
        }
      }
    }

    a_AAAS = k.a(a_AAAS = k.a(a_AAAS = k.a(a_AAAS, var0), var1), var2);
  }

  private static void a(Graphics var0, int var1, int var2, int var3, int var4) {
    var0.setColor(var4);
    var0.drawRect(var1, var2, 20, 20);
    var0.setColor(var3);
    var0.drawRect(var1 + 1, var2 + 1, 18, 18);
    var0.setColor(2697251);
    var0.fillRect(var1 + 2, var2 + 2, 17, 17);
  }

  private void k(Graphics var1) {
    if (this.a_dc != null) {
      this.a_dc.a();
    } else {
      this.a_dc = new dc();
    }

    int var2 = this.a_AI[0];
    int var3 = this.a_AI[1] + this.a_AI[3];
    int var4 = this.B_I / 5;
    if (t_B != 3) {
      int var10002 = var2 + 6;
      int var10003 = var3 + 4;
      int var10004 = this.B_I - 10;
      int var10 = this.A_I / 2 - 10;
      int var9 = var10004;
      int var8 = var10003;
      int var7 = var10002;
      Graphics var6 = var1;
      if (this.f_bx != null) {
        k.a(var1, this.f_bx, var7 - 4 + var10 / 2, var8 + var9 - 10, true, 0);
      }

      int var11 = var9 / 3;
      int var5 = var10 / 3;

      for (int var55 = 0; var55 < 3; var55++) {
        for (int var60 = 0; var60 < 2; var60++) {
          a(var6, var7 + (var60 << 1) * var5, var8 + var11 * var55, 15589356, 6963805);
          if (a_bl.a_Ack[var55 + var60 * 3] != null) {
            int var12 = o.f_I(a_bl.a_Ack[var55 + var60 * 3].a_S);
            if (ck.a_AImage[var12] != null) {
              var6.drawImage(ck.a_AImage[var12], var7 - 2 + var5 / 2 + (var60 << 1) * var5, var8 - 2 + var11 / 2 + var11 * var55, 3);
            }
          }
        }
      }

      var10003 = var3 + this.B_I;
      int var56 = 26;
      var8 = var10003;
      var6 = var1;
      if (this.m_Image != null) {
        var1.drawRegion(
          this.m_Image, 0, (this.m_Image.getHeight() >> 1) * (a_bl.h_S - 1), this.m_Image.getWidth(), this.m_Image.getHeight() >> 1, 0, var2 + 4, var8 + 5, 20
        );
      }

      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var2 + 22, var8 + this.m_Image.getHeight() / 2 + 5, 17);
      cg.a(var1, q_I, var2 + 15, var8 + this.m_Image.getHeight() / 2 + 5, this.d_Image, 6, 7, 20);
      var1.drawRegion(this.d_Image, 66, 0, 6, 7, 0, var2 + 22 - 6, var8 + this.m_Image.getHeight() / 2 + 5, 24);
      cg.a(var1, 6, var2 + 22 + 6, var8 + this.m_Image.getHeight() / 2 + 5, this.d_Image, 6, 7, 20);
      var1.drawRegion(this.d_Image, 72, 0, 6, 7, 0, var2 + 22 + 6 + 6 + 1, var8 + this.m_Image.getHeight() / 2 + 5, 20);
      var7 = var2 + this.m_Image.getWidth() + 8;

      for (int var61 = 0; var61 < 6; var61++) {
        if (b_AS != null) {
          if (cg.a_Z(b_AS, a_bl.b_AS[var61])) {
            k.a(var6, b_Abx[var61], var7 - 1 + var61 * 22, var8 + 2, true, 0);
          }

          a(var6, var7 + var61 * 22, var8 + 3, 16381892, 9524792);
          if (ck.a_AImage[o.f_I(b_AS[var61])] != null) {
            var6.drawImage(ck.a_AImage[o.f_I(b_AS[var61])], var7 + 10 + var61 * 22, var8 + 13, 3);
          }
        }
      }

      var7 = var7 - this.m_Image.getWidth() - 8;
      var6.setColor(2303014);
      var6.fillRect(var7 + 1, var8 + 26 + 2 - 1, this.D_I - 2, 2);
      var6.setColor(14083300);
      var6.fillRect(var7 + 2, var8 + 26 + 2, this.D_I - 4, 1);
      var2 += this.A_I / 2;
      var10002 = var2 + 15;
      var56 = var4;
      var8 = var3;
      var7 = var10002;
      var6 = var1;
      as var36 = this;
      int[] var62 = new int[]{a_bl.a_AI[10], a_bl.a_AI[11], a_bl.a_AI[12], a_bl.a_AI[13], a_bl.a_AI[14]};

      for (int var67 = 0; var67 < 5; var67++) {
        if (var36.y_Image != null) {
          if (var67 < 2) {
            cg.a(var6, var36.y_Image, var36.y_Image.getWidth(), var36.y_Image.getHeight() / 10, var67, var7, var8 + var67 * var56 + var56 / 2, 3, 0);
            cg.a(var6, var62[var67], var7 + 5 + var36.y_Image.getWidth(), var8 + var56 / 2 + var67 * var56, var36.d_Image, 6, 7, 3);
          } else {
            cg.a(var6, var36.y_Image, var36.y_Image.getWidth(), var36.y_Image.getHeight() / 10, var67 + 1, var7, var8 + var56 / 2 + var67 * var56, 3, 0);
            cg.a(var6, var62[var67], var7 + 5 + var36.y_Image.getWidth(), var8 + var56 / 2 + var67 * var56, var36.d_Image, 6, 7, 3);
          }
        }
      }

      var5 = 0;
      int var42 = 0;
      var7 = 0;
      var8 = 0;
      var56 = 0;
      if (t_B == 0) {
        if (b_B < 6 && a_bl.a_Ack[b_B] != null) {
          var5 = a_bl.a_Ack[b_B].b_I();
          var56 = a_bl.a_Ack[b_B].e();
          var42 = a_bl.a_Ack[b_B].c_I();
          var7 = a_bl.a_Ack[b_B].a_I();
          var8 = a_bl.a_Ack[b_B].d();
        }

        var2 = this.a_AI[0];
        var3 = this.a_AI[1] + this.a_AI[3];
        var2 += 6;
        var3 += 2;
        var4 = this.B_I - 6;
        var10 = this.A_I / 2 - 10;
        var4 /= 3;
        var10 /= 3;
        if (o_B == 5) {
          if (b_B < 3) {
            if (this.a_Image != null) {
              var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 + this.E_B, var3 + var4 / 2 + var4 * b_B, 3);
            }
          } else if (b_B < 6) {
            if (this.a_Image != null) {
              var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 + 2 * var10 + this.E_B, var3 + var4 / 2 + var4 * (b_B - 3), 3);
            }
          } else if (this.a_Image != null) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var2 + this.m_Image.getWidth() + 22 * (b_B - 6) + this.E_B, var3 - 6 + this.B_I + 20, 3);
          }
        }

        if (b_B < 6) {
          if (a_bl.a_Ack[b_B] != null) {
            var11 = o.f_I(a_bl.a_Ack[b_B].a_S);
            cg.b(var1, ck.a_AString[var11], var2, var3 + this.B_I + 20 + 6, 13695224, 1196460, 20);
            this.a_dc.a(ck.a(a_bl.a_Ack[b_B]), this.a_AI[2] - 10, null, 16777215);
            this.a_dc.a(ck.c_AString[var11], this.A_I - 10, null, 16777215);
          } else {
            String[] var72 = new String[]{"Vũ khí", "Mũ", "Áo", "Giày", "Dây chuyền", "Nhẫn"};
            this.a_dc.a(var72[b_B] + " Vị trí", this.A_I - 10, null, 16777215);
          }
        } else {
          if (b_AS == null) {
            return;
          }

          cg.b(var1, ck.a_AString[o.f_I(b_AS[b_B - 6])], var2, var3 + this.B_I + 20 + 6, 13695224, 1196460, 20);
          var11 = 16777215;
          String var80 = null;
          if (q_I >= 2) {
            var11 = 15925024;
          }

          if (a_bl.h_S == 1) {
            var80 = "DEF+300 (2 món)";
          } else if (a_bl.h_S == 2) {
            var80 = "HP+500 (2 món)";
          }

          this.a_dc.a(var80, this.A_I - 10, null, var11);
          if (q_I >= 4) {
            var11 = 15925024;
          } else {
            var11 = 16777215;
          }

          if (a_bl.h_S == 1) {
            var80 = "MP+400 (4 món)";
          } else if (a_bl.h_S == 2) {
            var80 = "SPD+300 (4 món)";
          }

          this.a_dc.a(var80, this.A_I - 10, null, var11);
          if (q_I >= 6) {
            var11 = 15925024;
          } else {
            var11 = 16777215;
          }

          if (a_bl.h_S == 1) {
            var80 = "HP+600 (6 món)";
          } else if (a_bl.h_S == 2) {
            var80 = "ATK+500 (6 món)";
          }

          this.a_dc.a(var80, this.A_I - 10, null, var11);
        }

        var3 = var3 + this.B_I + 40 + 4;
        this.a_dc.a(var2, var3, this.A_I - 10, this.C_I - 40 - 4, (this.C_I - 40 - 4) / cg.b_S);
        this.a_dc.a(var1, this.a_dc, 1, true, false);
      } else if (t_B == 1 || t_B == 2) {
        if (o.b_AAS[b_B] != null && a_S < o.b_AAS[b_B].length) {
          if (a_bl.a_Ack[b_B] != null) {
            var5 = o.a_AAck[b_B][a_S].b_I() - a_bl.a_Ack[b_B].b_I();
            var42 = o.a_AAck[b_B][a_S].c_I() - a_bl.a_Ack[b_B].c_I();
            var7 = o.a_AAck[b_B][a_S].a_I() - a_bl.a_Ack[b_B].a_I();
            var8 = o.a_AAck[b_B][a_S].d() - a_bl.a_Ack[b_B].d();
            var56 = o.a_AAck[b_B][a_S].e() - a_bl.a_Ack[b_B].e();
          } else {
            var5 = o.a_AAck[b_B][a_S].b_I();
            var42 = o.a_AAck[b_B][a_S].c_I();
            var7 = o.a_AAck[b_B][a_S].a_I();
            var8 = o.a_AAck[b_B][a_S].d();
            var56 = o.a_AAck[b_B][a_S].e();
          }
        } else if (a_bl.a_Ack[b_B] != null) {
          var5 = -a_bl.a_Ack[b_B].b_I();
          var42 = -a_bl.a_Ack[b_B].c_I();
          var7 = -a_bl.a_Ack[b_B].a_I();
          var8 = -a_bl.a_Ack[b_B].d();
          var56 = -a_bl.a_Ack[b_B].e();
        }

        var4 = this.a_AI[0] + 26;
        var3 += this.B_I + 30;
        var10 = this.C_I - 30 - 26 * g_S;
        if (this.e_Image != null) {
          cg.a(var1, this.e_Image, this.e_Image.getWidth() / 6, this.e_Image.getHeight(), b_B, this.a_AI[0] + 0, var3 + 4, 20, 0);
        }

        for (int var68 = 0; var68 < g_S; var68++) {
          for (int var77 = 0; var77 < f_S; var77++) {
            a(var1, var4 + var77 * 26, var3 + var68 * 26, 15589356, 6963805);
            if (o.b_AAS[b_B] != null && var77 + var68 * f_S + f_S * c_B < o.b_AAS[b_B].length) {
              int var15 = o.f_I(o.b_AAS[b_B][var77 + var68 * f_S + f_S * c_B]);
              if (ck.a_AImage[var15] != null) {
                var1.drawImage(ck.a_AImage[var15], var4 + 11 + var77 * 26, var3 + 11 + var68 * 26, 3);
              }
            }
          }
        }

        if (o.a_AAck[b_B] != null && a_S < o.a_AAck[b_B].length) {
          var11 = o.f_I(o.a_AAck[b_B][a_S].a_S);
          cg.a(var1, ck.a_AString[var11], this.a_AI[0] + 4, var3 + 26 * g_S, 13695224, 1130666, 20);
          this.a_dc.a(ck.a(o.a_AAck[b_B][a_S]), this.a_AI[2] - 12 - vh.stringWidth(cg.a_Font, ck.a_AString[var11]), null, 13761017);
          this.a_dc.a(ck.c_AString[var11], this.a_AI[2] - 12 - vh.stringWidth(cg.a_Font, ck.a_AString[var11]), null, 13761017);
          this.a_dc
            .a(
              this.a_AI[0] + 4 + vh.stringWidth(cg.a_Font, ck.a_AString[var11]),
              var3 + 26 * g_S,
              this.a_AI[2] - 12 - vh.stringWidth(cg.a_Font, ck.a_AString[var11]),
              var10,
              var10 / cg.b_S
            );
          this.a_dc.a(var1, this.a_dc, 1, true, false);
        }

        k.a(var1, null, null, var4 + 26 * (f_S - 1) + 22 + 5, var3, 26 * (g_S - 1) + 22, ct.a_S / f_S, g_S, c_B);
        if (t_B == 1) {
          short var70 = (short)(a_S % f_S);
          short var78 = (short)(a_S / f_S - c_B);
          if (this.a_Image != null) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var4 + var70 * 26 + this.E_B, var3 + 11 + var78 * 26, 3);
          }
        } else if (t_B == 2) {
          short var79 = (short)(10 + vh.stringWidth(cg.a_Font, "Thuộc tính"));
          short var82 = (short)(c_AI.length * 18 + (d_AString.length - 1 << 1));
          short var16 = (short)((this.y_I - var82) / 2 + 2 * cg.b_S);
          this.a(var1, c_AI, d_B, this.x_I / 2, var16, var79, var82, 6768205);
        }
      }

      var2 = this.a_AI[0] + this.A_I * 3 / 4 + 15;
      var4 = this.B_I / 5;
      var3 = this.a_AI[1] + this.a_AI[3] + var4 / 2;
      if (var8 > 0) {
        if (this.I_Image != null) {
          cg.a(var1, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, var8, var2 + 14, var3, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 3);
      } else if (var8 < 0) {
        if (this.J_Image != null) {
          cg.a(var1, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, Math.abs(var8), var2 + 14, var3, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 3);
      }

      var3 += var4;
      if (var56 > 0) {
        if (this.I_Image != null) {
          cg.a(var1, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, var56, var2 + 14, var3, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 3);
      } else if (var56 < 0) {
        if (this.J_Image != null) {
          cg.a(var1, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, Math.abs(var56), var2 + 14, var3, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 3);
      }

      var3 += var4;
      if (var5 > 0) {
        if (this.I_Image != null) {
          cg.a(var1, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, var5, var2 + 14, var3, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 3);
      } else if (var5 < 0) {
        if (this.J_Image != null) {
          cg.a(var1, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, Math.abs(var5), var2 + 14, var3, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 3);
      }

      var3 += var4;
      if (var42 > 0) {
        if (this.I_Image != null) {
          cg.a(var1, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, var42, var2 + 14, var3, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 3);
      } else if (var42 < 0) {
        if (this.J_Image != null) {
          cg.a(var1, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 10, var2, var3, 3, 0);
        }

        cg.a(var1, Math.abs(var42), var2 + 14, var3, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 3);
      }

      var3 += var4;
      if (var7 <= 0) {
        if (var7 < 0) {
          if (this.J_Image != null) {
            cg.a(var1, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 10, var2, var3, 3, 0);
          }

          cg.a(var1, Math.abs(var7), var2 + 14, var3, this.J_Image, this.J_Image.getWidth() / 11, this.J_Image.getHeight(), 3);
        }

        return;
      }

      if (this.I_Image != null) {
        cg.a(var1, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 10, var2, var3, 3, 0);
      }

      cg.a(var1, var7, var2 + 14, var3, this.I_Image, this.I_Image.getWidth() / 11, this.I_Image.getHeight(), 3);
    } else {
      int var38 = this.a_AI[0] + 10;
      int var43 = this.a_AI[1] + this.a_AI[3] + 5;
      int var49 = this.a_AI[0];
      int var54 = var43 + this.B_I + cg.b_S;
      int var59 = cg.b_S + 5;
      short var35 = cg.b_S;
      cg.a(var1, "Trang bị ", var38, var43, 9240572, 2315140, 20);
      cg.a(var1, "Đá quý ", var38, var43 + 5 + 20, 16711585, 10570787, 20);
      a(var1, this.a_AI[0] + this.a_AI[2] / 4, var43, 15589356, 6963805);
      a(var1, this.a_AI[0] + this.a_AI[2] / 4, var43 + 5 + 20, 15589356, 6963805);
      int var66 = o.f_I(o.b_AAS[b_B][a_S]);
      if (ck.a_AImage[var66] != null) {
        var1.drawImage(ck.a_AImage[var66], this.a_AI[0] + this.a_AI[2] / 4 + 10, var43 + 10, 3);
        cg.a(var1, ck.a_AString[var66], this.a_AI[0] + this.a_AI[2] / 2, var43, 16311264, 5899082, 20);
      }

      if (g_I > 0) {
        var1.drawImage(o.a_AImage[o.d_I(g_I)], this.a_AI[0] + this.a_AI[2] / 4 + 10, var43 + 5 + 30, 3);
        cg.a(var1, o.f_AString[o.d_I(g_I)], this.a_AI[0] + this.a_AI[2] / 2, var43 + 5 + 20, 16311264, 5899082, 20);
      }

      int var76 = f_I;
      if (a_AAS == null) {
        var1.setColor(16777215);
        vh.drawString(var1, "Không có đá quý để chọn", at.a_AS[0] >> 1, var54 + (var59 >> 1), 17);
      } else {
        if (a_AAS.length < f_I) {
          var76 = a_AAS.length;
        }

        cg.a(var1, "Nguyên liệu đá quý", var49 + 40, var54 - cg.b_S, 16640843, 8997145, 17);
        cg.a(var1, "Số lượng", this.a_AI[2] - 20, var54 - cg.b_S, 16640843, 8997145, 17);

        for (int var83 = 0; var83 < var76; var83++) {
          a(var1, var49 + 20, var54 + var59 * var83, 15589356, 6963805);
          if (var83 + e_I < a_AAS.length) {
            var2 = o.d_I(a_AAS[e_I + var83][0]);
            var1.drawImage(o.a_AImage[var2], var49 + 30, var54 + 10 + var59 * var83, 3);
            if (var83 + e_I == d_I) {
              if (this.a_Image != null) {
                var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var49 + 10 + this.E_B, var54 + 10 + var59 * var83, 3);
              }

              cg.a(var1, o.f_AString[var2], this.x_I >> 1, var54 + var59 * var83, 16773120, 15867651, 17);
              this.a_dc.a(o.f_AString[var2], this.a_AI[2] - 8, null, 14291704);
              this.a_dc.a(o.g_AString[var2], this.a_AI[2] - 8, null, 16777215);
            } else {
              cg.a(var1, o.f_AString[var2], this.x_I >> 1, var54 + var59 * var83, 16311264, 5899082, 17);
            }

            cg.a(var1, a_AAS[e_I + var83][1], this.a_AI[2] - 20, var54 + 10 + var59 * var83, this.d_Image, 6, 7, 3);
          }
        }

        if (a_AAS.length > f_I) {
          k.a(var1, null, null, this.a_AI[0] + this.a_AI[2] - 20, var54 - 10 + (var59 >> 1), f_I * cg.b_S, a_AAS.length, f_I, e_I);
        }
      }

      String var81 = "Phím giữa chọn đá quý‚lên xuống để đổi đá quý‚OK để khảm‚Trở về để hủy";
      this.a_dc.a(var81, this.a_AI[2], null, 16777215);
      this.a_dc.a(this.a_AI[0] + 4, this.a_AI[1] + this.a_AI[3] + this.B_I + this.C_I - 20, this.a_AI[2] - 8, var35 - 4, 1);
      this.a_dc.a(var1, this.a_dc, 1, true, false);
      if (g_Z && u_B == 0 && System.currentTimeMillis() - a_J >= 1000L) {
        g_Z = false;
        o.c(g_I, 1);
        ck var10000 = o.a_AAck[b_B][a_S];
        String var44 = o.h_AString[o.d_I(g_I)];
        ck var39 = var10000;
        short[] var50;
        if ((var50 = cg.b_AS(var44, ","))[0] == 1) {
          var39.b_S = (short)(var39.b_S + var50[1]);
        } else if (var50[0] == 2) {
          var39.c_S = (short)(var39.c_S + var50[1]);
        } else if (var50[0] == 3) {
          var39.d_S = (short)(var39.d_S + var50[1]);
        } else if (var50[0] == 4) {
          var39.e_S = (short)(var39.e_S + var50[1]);
        } else if (var50[0] == 5) {
          var39.f_S = (short)(var39.f_S + var50[1]);
        } else if (var50[0] == 6) {
          var39.g_S = (short)(var39.g_S + var50[1]);
        }

        var39.a_B++;
        g_I = 0;
        u_B = 1;
      }

      if (u_B == 1) {
        k.a(var1, "Chúc mừng‚khảm trang bị thành công", this.x_I, this.y_I);
      }
    }
  }

  private void a(Graphics var1, int var2, int var3, int var4, int[][] var5) {
    for (int var6 = 0; var6 < 3; var6++) {
      int var10 = var3 + var4 * var6;
      cg.a(var1, this.v_Image, this.v_Image.getWidth() * var5[var6][0] / var5[var6][1], this.v_Image.getHeight() >> 2, var6, var2, var10, 20, 0);
      int var10003 = this.v_Image.getWidth();
      int var19 = this.v_Image.getHeight() >> 2;
      int var18 = 15988459;
      int var17 = 4664337;
      int var16 = var10003;
      var1.setColor(4664337);
      var1.drawRect(var2 - 1, var10 - 1, var16 + 2, var19 + 2);
      var1.setColor(15988459);
      var1.drawRoundRect(var2 - 2, var10 - 2, var16 + 3, var19 + 3, 4, 4);
      int var10002 = var5[var6][0];
      var10003 = var5[var6][1];
      int var10004 = var2 + this.y_Image.getWidth() * 5 / 2;
      var18 = var10 - 4;
      var17 = var10004;
      var16 = var10003;
      int var15 = var10002;
      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var17, var18, 17);
      cg.a(var1, var15, var17 - 3, var18, this.d_Image, 6, 7, 24);
      cg.a(var1, var16, var17 + 3, var18, this.d_Image, 6, 7, 20);
      cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, var6, var2, var10 - 8, 20, 0);
    }
  }

  private void a(Graphics var1, int var2, int var3, int var4, int var5, int[] var6) {
    for (int var7 = 0; var7 < 3; var7++) {
      int var11 = var3 + var7 * var4;
      var1.drawRegion(this.v_Image, 0, (this.v_Image.getHeight() >> 2) * 3, var5, this.v_Image.getHeight() >> 2, 0, var2, var11 + 3, 20);
      cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, var7 + 3, var2, var11 - 8, 20, 0);
      cg.a(var1, var6[var7], var2 + this.y_Image.getWidth(), var11 - 4, this.d_Image, 6, 7, 20);
    }
  }

  public final void k() {
    this.r_I = 0;
    this.s_I = 0;
    this.t_I = 0;
    this.a_AAB = null;
    this.r_B = 0;
    this.g_bx.b(0);
    this.h_bx.b(0);
  }

  private void l(Graphics var1) {
    if (a_Acv == null) {
      var1.setColor(16777215);
      vh.drawString(var1, "Không có sủng vật", this.x_I / 2, this.y_I / 2 + this.C_I / 2, 17);
    } else {
      if (q_B != 0 && q_B != 1 && q_B != 3 && q_B != 8) {
        if (q_B == 2) {
          this.a(var1, a_Acv[this.a_l.a()]);
          return;
        }

        if (q_B == 5) {
          cv var16 = a_Acv[this.a_l.a()];
          if (this.a_bx != null) {
            var1.setClip(this.a_AI[0], this.a_AI[1] + this.a_AI[3], this.a_AI[2] / 2, this.B_I);
            k.a(var1, this.a_bx, this.a_AI[0] - 8 + (this.a_AI[2] >> 2), this.a_AI[1] + this.a_AI[3] + this.B_I, true, 0);
            var1.setClip(0, 0, this.x_I, this.y_I);
          }

          int var23 = this.a_AI[0] + this.a_AI[2] / 2;
          int var37 = this.B_I / 4;
          int var30 = this.a_AI[1] + this.a_AI[3];
          cg.b(var1, var16.a_String, var23 - 10, var30 + 4, 9502459, 540331, 20);
          var30 += var37;
          int[][] var7;
          (var7 = new int[3][2])[0][0] = var16.a_AI[1];
          var7[0][1] = var16.a_AI[2];
          var7[1][0] = var16.a_AI[4];
          var7[1][1] = var16.a_AI[5];
          var7[2][0] = var16.a_AI[13];
          var7[2][1] = var16.a_AI[14];
          this.a(var1, var23 - 4, var30 + 10, var37, var7);
          if (s_B != 0) {
            if (s_B == 1) {
              if (this.a_dc != null) {
                this.a_dc.a();
              } else {
                this.a_dc = new dc();
              }

              int var18 = o.c_I(this.e_ab.a_I(this.e_ab.b_I()));
              if (d.a_AImage == null) {
                d.b();
              }

              var1.drawImage(d.a_AImage[var18], this.a_AI[0] + 10, this.a_AI[1] + this.a_AI[3] + this.B_I + 2, 20);
              cg.a(
                var1, d.a_AString[var18], this.a_AI[0] + 15 + d.a_AImage[var18].getWidth(), this.a_AI[1] + this.a_AI[3] + this.B_I + 2, 16580583, 6036810, 20
              );
              if (this.J_Image == null) {
                this.J_Image = cc.b("/pic/number05.gf", 2);
              }

              cg.a(
                var1,
                "" + a_Acv[this.a_l.a()].a_Ad[this.e_ab.b_I()].a_B,
                this.a_AI[0] + 58 + this.a_AI[2] / 2,
                this.a_AI[1] + this.a_AI[3] + this.B_I + 2,
                16681647,
                8791075,
                24
              );
              cg.a(var1, " cấp", this.a_AI[0] + 61 + this.a_AI[2] / 2, this.a_AI[1] + this.a_AI[3] + this.B_I + 2, 16580583, 6036810, 20);
              this.a_dc.a(d.c_AString[var18], this.D_I - 10, null, 16777215);
              this.a_dc
                .a(this.a_AI[0] + 4, this.a_AI[1] + this.a_AI[3] + this.B_I + cg.b_S, this.D_I - 10, this.C_I - cg.b_S - 3, (this.C_I - cg.b_S - 3) / cg.b_S);
              this.a_dc.a(var1, this.a_dc, 1, true, false);
            }

            return;
          }

          var23 = this.a_AI[0];
          var30 = this.a_AI[1] + this.a_AI[3] + this.B_I;
          if (this.e_ab != null) {
            this.e_ab.a(var1);
          }

          var23 += this.A_I / 2;
          var30 += this.C_I;
          if (this.z_Image != null) {
            var1.drawImage(this.z_Image, var23, var30, 33);
            var23 += this.z_Image.getWidth() + 15;
          }

          cg.a(var1, a_Acv[this.a_l.a()].a_Ad[this.e_ab.b_I()].a_I, var23, var30, this.d_Image, 6, 7, 40);
          var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var23, var30, 36);
          int var17 = 0;
          if (!cg.a_Z(new short[]{7, 29, 30, 31, 32, 35, 36, 37, 38}, this.e_ab.a_I(this.e_ab.b_I()))) {
            var17 = a_Acv[this.a_l.a()].a_Ad[this.e_ab.b_I()].b_I;
          }

          cg.a(var1, var17, var23 + 6, var30, this.d_Image, 6, 7, 36);
        } else if (q_B == 7) {
          int var9 = this.a_AI[0] + 7;
          int var19 = this.a_AI[1] + this.a_AI[3] + 6;
          cg.b(var1, a_Acv[this.a_l.a()].a_String, this.x_I >> 1, var19, 9502459, 540331, 17);
          if (this.a_bx != null) {
            k.a(var1, this.a_bx, this.x_I >> 1, this.y_I >> 1, true, 0);
          }

          this.g_bx.a(var9 + 23, this.a_AI[1] + this.a_AI[3] + this.j_I / 2);
          this.g_bx.a(var1);
          if (this.c_Z) {
            this.g_bx.a(true);
          }

          this.h_bx.a(var9 + (this.a_AI[2] - 34), this.a_AI[1] + this.a_AI[3] + this.j_I / 2);
          this.h_bx.a(var1);
          if (this.d_Z) {
            this.h_bx.a(true);
          }

          if (this.g_bx.c() >= this.g_bx.d() - 1) {
            this.c_Z = false;
          }

          if (this.h_bx.c() >= this.h_bx.d() - 1) {
            this.d_Z = false;
          }

          if (this.g_bx.c() >= this.g_bx.d() - 1 || this.h_bx.c() >= this.h_bx.d() - 1) {
            this.r_B = 2;
          }

          for (int var26 = 0; var26 < 2; var26++) {
            a(var1, var9 + (this.a_AI[2] - 34) * var26, var19, 15589356, 6963805);
            int var34;
            if (a_Acv[this.a_l.a()].a_AAB[var26][1] != 0 && (var34 = o.d_I(a_Acv[this.a_l.a()].a_AAB[var26][1])) != -1 && o.a_AImage[var34] != null) {
              var1.drawImage(o.a_AImage[var34], var9 + 11 + (this.a_AI[2] - 34) * var26, var19 + 11, 3);
            }
          }

          if (this.r_B == 0 && this.a_Image != null) {
            var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var9 + this.t_I * (this.a_AI[2] - 34) + this.E_B, var19 + 11, 3);
            int var28 = this.a_AI[1] + this.a_AI[3] + (this.B_I << 1) / 3;
            int var36 = this.B_I / 3;
            if (this.a_dc != null) {
              this.a_dc.a();
            } else {
              this.a_dc = new dc();
            }

            if (a_Acv[this.a_l.a()].a_AAB[this.t_I][1] != 0) {
              var9 = o.d_I(a_Acv[this.a_l.a()].a_AAB[this.t_I][1]);
              if (this.a_AAB != null && this.s_I < this.a_AAB.length) {
                String var40 = o.f_AString[var9];
                if (a_Acv[this.a_l.a()].a_AAB[this.t_I][0] != 0) {
                  var40 = var40 + "[Đã cấy]";
                }

                this.a_dc.a(var40, this.a_AI[2] - 12, null, 9502459);
                this.a_dc.a(o.g_AString[var9], this.a_AI[2] - 12, null, 9502459);
              }
            }

            this.a_dc.a("Phím giữa chọn vật phẩm‚ chọn nguyên liệu cấy xong thì phím mềm trái để cấy", this.a_AI[2] - 12, null, 9502459);
            this.a_dc.a(this.a_AI[0] + 4, var28, this.a_AI[2] - 12, var36, var36 / cg.b_S);
            this.a_dc.a(var1, this.a_dc, 1, true, false);
            return;
          }

          if (this.r_B == 1) {
            var1.setColor(2303014);
            var1.fillRect(this.a_AI[0] + 3, this.a_AI[1] + this.a_AI[3] + 105, this.D_I - 3, 2);
            var1.setColor(14083300);
            var1.fillRect(this.a_AI[0] + 4, this.a_AI[1] + this.a_AI[3] + 106, this.D_I - 5, 1);
            var1.drawImage(this.n_Image, this.a_AI[0] + 3, this.a_AI[1] + this.a_AI[3] + this.j_I / 2, 20);
            int var27 = this.a_AI[0] + 15;
            int var35 = this.a_AI[1] + this.a_AI[3] + 115;

            for (int var38 = 0; var38 < g_S; var38++) {
              for (int var10 = 0; var10 < f_S; var10++) {
                a(var1, var27 + var10 * 26, var35 + var38 * 26, 15589356, 6963805);
                if (this.a_AAB != null && this.r_I * f_S + var10 + var38 * f_S < this.a_AAB.length) {
                  if ((var19 = o.d_I(this.a_AAB[this.r_I * f_S + var10 + var38 * f_S][0])) != -1 && o.a_AImage[var19] != null) {
                    var1.drawImage(o.a_AImage[var19], var27 + 11 + var10 * 26, var35 + 11 + var38 * 26, 3);
                  }

                  cg.a(var1, this.a_AAB[this.r_I * f_S + var10 + var38 * f_S][1], var27 + 11 + var10 * 26, var35 + 20 + var38 * 26, this.d_Image, 6, 7, 33);
                }
              }
            }

            if (this.a_dc != null) {
              this.a_dc.a();
            } else {
              this.a_dc = new dc();
            }

            int var39 = this.n_Image.getHeight() - 26 * g_S;
            if (this.a_AAB != null && this.s_I < this.a_AAB.length) {
              var9 = o.d_I(this.a_AAB[this.s_I][0]);
              this.a_dc.a(o.f_AString[var9], this.a_AI[2] - 12, null, 13761017);
              this.a_dc.a(o.g_AString[var9], this.a_AI[2] - 12, null, 13761017);
              this.a_dc.a(this.a_AI[0] + 4, var35 + 22 * g_S, this.a_AI[2] - 12, var39, var39 / cg.b_S);
              this.a_dc.a(var1, this.a_dc, 1, true, false);
            }

            short var12 = (short)(this.s_I % f_S);
            short var21 = (short)(this.s_I / f_S - this.r_I);
            if (this.a_Image != null) {
              var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, var27 + var12 * 26 + this.E_B, var35 + 11 + var21 * 26, 3);
            }

            k.a(var1, null, null, this.a_AI[0] + this.a_AI[2] - 20, var35, 26 * g_S, 18 / f_S, g_S, this.r_I);
            var1.setClip(0, 0, this.x_I, this.y_I);
            return;
          }

          if (this.r_B == 2) {
            k.a(var1, "Chúc mừng bạn," + a_Acv[this.a_l.a()].a_String + " Cấy ghép thành công!! ", this.x_I, this.y_I);
          }
        }
      } else {
        if (this.a_l != null) {
          this.a_l.a(var1);
          int var3 = this.a_AI[1] + this.a_AI[3] + this.B_I;
          int var4 = this.C_I / 4;
          if (this.a_bx != null) {
            var1.setClip(this.a_AI[0], var3, this.A_I / 2, this.C_I);
            k.a(var1, this.a_bx, this.a_AI[0] - 10 + this.a_AI[2] / 4, var3 + this.C_I, true, 0);
            var1.setClip(0, 0, this.x_I, this.y_I);
          }

          int var5 = this.a_AI[0] + this.a_AI[2] / 2;
          int var2 = this.a_AI[2] / 8;
          if (a_Acv[this.a_l.a()].a_AB != null && this.s_Image != null) {
            if (a_Acv[this.a_l.a()].a_AB[0] > 0) {
              var1.drawRegion(
                this.s_Image,
                this.s_Image.getWidth() / 2,
                this.s_Image.getHeight() / 2,
                this.s_Image.getWidth() / 2,
                this.s_Image.getHeight() / 2,
                0,
                var5,
                var3 + var4 / 2,
                6
              );
              cg.a(var1, a_Acv[this.a_l.a()].a_AB[0], var5 + this.s_Image.getWidth() / 2, var3 + var4 / 2, this.d_Image, 6, 7, 6);
              var5 += var2;
            }

            if (a_Acv[this.a_l.a()].a_AB[1] > 0) {
              var1.drawRegion(
                this.s_Image, this.s_Image.getWidth() / 2, 0, this.s_Image.getWidth() / 2, this.s_Image.getHeight() / 2, 0, var5, var3 + var4 / 2, 6
              );
              cg.a(var1, a_Acv[this.a_l.a()].a_AB[1], var5 + this.s_Image.getWidth() / 2, var3 + var4 / 2, this.d_Image, 6, 7, 6);
              var5 += var2;
            }

            if (a_Acv[this.a_l.a()].a_AB[2] > 0) {
              var1.drawRegion(this.s_Image, 0, 0, this.s_Image.getWidth() / 2, this.s_Image.getHeight() / 2, 0, var5, var3 + var4 / 2, 6);
              cg.a(var1, a_Acv[this.a_l.a()].a_AB[2], var5 + this.s_Image.getWidth() / 2, var3 + var4 / 2, this.d_Image, 6, 7, 6);
              var5 += var2;
            }

            if (a_Acv[this.a_l.a()].a_AB[3] > 0) {
              var1.drawRegion(
                this.s_Image, 0, this.s_Image.getHeight() / 2, this.s_Image.getWidth() / 2, this.s_Image.getHeight() / 2, 0, var5, var3 + var4 / 2, 6
              );
              cg.a(var1, a_Acv[this.a_l.a()].a_AB[3], var5 + this.s_Image.getWidth() / 2, var3 + var4 / 2, this.d_Image, 6, 7, 6);
            }
          }

          var2 = this.a_AI[0] + this.a_AI[2] / 2 - 4;
          var3 = var3 + var4 + 10;
          int[][] var6;
          (var6 = new int[3][2])[0][0] = a_Acv[this.a_l.a()].a_AI[1];
          var6[0][1] = a_Acv[this.a_l.a()].a_AI[2];
          var6[1][0] = a_Acv[this.a_l.a()].a_AI[4];
          var6[1][1] = a_Acv[this.a_l.a()].a_AI[5];
          var6[2][0] = a_Acv[this.a_l.a()].a_AI[13];
          var6[2][1] = a_Acv[this.a_l.a()].a_AI[14];
          this.a(var1, var2, var3, var4, var6);
        }

        if (q_B != 0) {
          if (q_B == 1) {
            short var15 = (short)(10 + vh.stringWidth(cg.a_Font, "Thuộc tính"));
            short var22 = (short)((d_AI.length << 4) + (e_AString.length - 1 << 1));
            short var29 = (short)((this.y_I - var22) / 2 - 2 * cg.b_S);
            this.a(var1, d_AI, a_B, this.x_I / 2, var29 + 6, var15, var22, 6768205);
            return;
          }

          if (q_B == 3) {
            k.a(var1, "Chắc chắn thả sủng vật: " + a_Acv[this.a_l.a()].a_String + "?", this.x_I, this.y_I);
            return;
          }

          if (q_B == 8) {
            k.a(var1, "Chiết xuất gen năng lực của sủng vật này? Sau khi chiết xuất sủng vật sẽ biến mất.", this.x_I, this.y_I);
            return;
          }

          if (q_B == 4) {
            k.a(var1, b_String, this.x_I, this.y_I);
            return;
          }
        }
      }
    }
  }

  private void a(Graphics var1, cv var2) {
    this.a(var1, var2.a_AB);
    int var3 = this.a_AI[0] + 4;
    int var5 = this.C_I / 5;
    int var4 = this.a_AI[1] + this.a_AI[3] + this.B_I;
    if (this.a_bx != null) {
      var1.setClip(var3, this.a_AI[1] + this.a_AI[3], this.a_AI[2] / 2, this.B_I);
      k.a(var1, this.a_bx, var3 - 8 + (this.a_AI[2] >> 2), var4, true, 0);
      var1.setClip(0, 0, this.x_I, this.y_I);
    }

    cg.b(var1, var2.a_String, var3 + 4, var4, 9502459, 540331, 36);
    cg.b(var1, var2.a_AI[0] + "  cấp", var3 + 12 + vh.stringWidth(cg.a_Font, var2.a_String), var4, 16646122, 6102605, 36);
    if (this.h_Image != null) {
      var1.drawImage(this.h_Image, var3 + 4, var4 + var5 * 3, 20);
    } else {
      cg.b(var1, "Gen đặc trưng:", var3 + 4, var4 + var5 * 3, 16706636, 9128217, 20);
    }

    if (o.f_AString == null) {
      o.d();
    }

    if (this.a_dc != null) {
      this.a_dc.a();
    } else {
      this.a_dc = new dc();
    }

    if (var2.b_AB == null) {
      if (a_AAString == null) {
        a_AAString = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
      }

      var2.b_AB = cg.b_AB(a_AAString[var2.a_I - 1][18], ",");
    }

    int var6 = o.a_AImage[0].getWidth() + 10;

    for (int var7 = 0; var7 < 3; var7++) {
      a(var1, this.a_AI[0] + this.a_AI[2] / 2 - 2 + var6 * var7, var4 - 2 + var5 * 3, 15589356, 6963805);
    }

    for (int var10 = 0; var10 < this.u_I; var10++) {
      if (var10 == 0) {
        int var8 = o.d_I(var2.b_AB[0]);
        var1.setColor(16777215);
        var1.drawImage(o.a_AImage[var8], this.a_AI[0] + this.a_AI[2] / 2 + var6 * var10, var4 + 2 + var5 * 3, 20);
      } else {
        int var15;
        if ((var15 = o.d_I(var2.a_AAB[var10 - 1][1])) != -1) {
          var1.drawImage(o.a_AImage[var15], this.a_AI[0] + this.a_AI[2] / 2 + var6 * var10, var4 + 2 + var5 * 3, 20);
        }
      }
    }

    if (this.a_Image != null) {
      var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, this.a_AI[0] + this.a_AI[2] / 2 + this.E_B + var6 * this.v_I, var4 + 8 + var5 * 3, 3);
    }

    if (this.v_I == 0) {
      int var11 = o.d_I(var2.b_AB[0]);
      var1.setColor(16777215);
      this.a_dc.a(o.f_AString[var11] + o.g_AString[var11], this.a_AI[2] - 10, null, 9502459);
    } else if (this.v_I == 1) {
      if (var2.a_AAB[0][0] != 0) {
        int var12 = o.d_I(var2.a_AAB[0][1]);
        this.a_dc.a(o.g_AString[var12], this.a_AI[2] - 10, null, 9502459);
      }
    } else if (this.v_I == 2 && var2.a_AAB[1][0] != 0) {
      int var13 = o.d_I(var2.a_AAB[1][1]);
      this.a_dc.a(o.g_AString[var13], this.a_AI[2] - 10, null, 9502459);
    }

    this.a_dc.a(this.a_AI[0] + 4, var4 + var5 * 4, this.a_AI[2] - 10, var5 * 2, 1);
    this.a_dc.a(var1, this.a_dc, 1, true, false);
    var4 += 10;
    int[][] var14;
    (var14 = new int[3][2])[0][0] = var2.a_AI[1];
    var14[0][1] = var2.a_AI[2];
    var14[1][0] = var2.a_AI[4];
    var14[1][1] = var2.a_AI[5];
    var14[2][0] = var2.a_AI[13];
    var14[2][1] = var2.a_AI[14];
    int[] var16;
    (var16 = new int[3])[0] = var2.a_AI[7];
    var16[1] = var2.a_AI[9];
    var16[2] = var2.a_AI[11];
    this.a(var1, var3, var4, var5, var14);
    this.a(var1, var3 + 2 + this.a_AI[2] / 2, var4, var5, this.D_I / 2 - 8, var16);
  }

  private void m(Graphics var1) {
    this.a(var1, a_bl.a_AB);
    int[][] var2;
    (var2 = new int[3][2])[0][0] = a_bl.a_AI[3];
    var2[0][1] = a_bl.a_AI[10];
    var2[1][0] = a_bl.a_AI[5];
    var2[1][1] = a_bl.a_AI[11];
    var2[2][0] = a_bl.a_AI[1];
    var2[2][1] = a_bl.a_AI[2];
    int[] var3;
    (var3 = new int[3])[0] = a_bl.a_AI[12];
    var3[1] = a_bl.a_AI[13];
    var3[2] = a_bl.a_AI[14];
    int[] var4 = new int[]{a_bl.a_AI[16], a_bl.a_AI[17], a_bl.a_AI[18], a_bl.a_AI[19], a_bl.a_AI[20]};
    int var5 = this.a_AI[0] + 4;
    int var6 = this.a_AI[1] + this.a_AI[3] + 4;
    int var7 = (this.A_I >> 1) - 12;
    int var8 = this.B_I - 26;
    this.b(var1, var5 + 5, var6, var7, var8);
    var5 = this.a_AI[0] + 8;
    var7 = this.C_I / 6;
    var6 = this.a_AI[1] + this.a_AI[3] + 90 + (var7 >> 1) + 3;
    this.a(var1, var5, var6, var7, var2);
    this.a(var1, var5, var6 - 5 + var7 * 3, var7, this.v_Image.getWidth(), var3);
    var5 += (this.A_I >> 1) + 8;
    int var9 = this.C_I / 5;

    for (int var11 = 0; var11 < 5; var11++) {
      if (var11 == 0) {
        var1.drawImage(this.A_Image, var5, var6 - 8 + var9 * var11, 20);
        cg.a(var1, var4[var11], var5 + this.A_Image.getWidth() * 3 / 2, var6 - 5 + var9 * var11, this.H_Image, 6, 8, 20);
      } else {
        if (this.e_AI[var11 - 1] > 0) {
          cg.a(var1, this.e_AI[var11 - 1], this.a_AI[0] + this.a_AI[2] * 3 / 4, var6 + 4 + var9 * var11, this.I_Image, 7, 7, 24);
        }

        cg.a(var1, this.y_Image, this.y_Image.getWidth(), this.y_Image.getHeight() / 10, var11 + 5, var5, var6 - 10 + var9 * var11, 20, 0);
        cg.a(var1, var4[var11], var5, var6 + 4 + var9 * var11, this.d_Image, 6, 7, 20);
      }
    }

    if (o_B == 2) {
      int var12 = this.a_AI[0] + this.a_AI[2] * 3 / 4;
      var1.drawImage(this.G_Image, var12, var6 + 4 + var9 * (this.w_I + 1), 20);
      cg.a(var1, this.t_Image, this.t_Image.getWidth() / 2, this.t_Image.getHeight(), 0, var12 + 15 + this.E_B, var6 + 4 + var9 * (this.w_I + 1), 24, 0);
      cg.a(var1, this.e_AI[this.w_I], var12 + 15 + 10, var6 + 4 + var9 * (this.w_I + 1), this.I_Image, 7, 7, 17);
      cg.a(var1, this.t_Image, this.t_Image.getWidth() / 2, this.t_Image.getHeight(), 1, var12 + 15 + 20 - this.E_B, var6 + 4 + var9 * (this.w_I + 1), 20, 0);
      var5 -= this.a_AI[2] / 4;

      for (int var10 = 0; var10 < 6; var10++) {
        if (var10 < 3) {
          if (var10 < 2 && this.f_AI[var10] > 0) {
            cg.a(var1, this.I_Image, 7, 7, 10, var5 + 18, var6 - 8 + var10 * var7, 20, 0);
            cg.a(var1, this.f_AI[var10], var5 + 30, var6 - 8 + var10 * var7, this.I_Image, 7, 7, 20);
          }
        } else if (this.f_AI[var10 - 1] > 0) {
          cg.a(var1, this.I_Image, 7, 7, 10, var5 + 10, var6 - 8 + var10 * var7, 20, 0);
          cg.a(var1, this.f_AI[var10 - 1], var5 + 20, var6 - 8 + var10 * var7, this.I_Image, 7, 7, 20);
        }
      }
    }
  }

  private void b(Graphics var1, int var2, int var3, int var4, int var5) {
    var1.setClip(var2, var3, var4, var5);
    k.a(var1, c_bx, var2 + 3 + (var4 >> 1), var3 + var5, true, 0);
    var1.setClip(0, 0, this.x_I, this.y_I);

    for (int var6 = 0; var6 < 2; var6++) {
      var1.setColor(4338001);
      var1.fillRect(var2, var3 + var5, var4, 16);
    }

    var1.setColor(10326691);
    var1.drawRoundRect(var2, var3, var4, var5 + 16, 4, 4);
    var1.setColor(14208729);
    var1.drawRoundRect(var2, var3, var4, var5, 4, 4);
    var1.setColor(2303014);
    var1.drawRoundRect(var2 - 1, var3 - 1, var4 + 2, var5 + 16 + 2, 4, 4);
    if (this.C_Image != null) {
      cg.a(var1, this.C_Image, this.C_Image.getWidth(), 14, a_bl.h_S - 1, var2 + 3, var3 + var5 + this.C_Image.getHeight() / 2 + 2, 36, 0);
    } else {
      cg.a(var1, a_bl.a_String, var2 + 3, var3 + var5, 16776937, 257041742, 20);
    }

    cg.a(var1, a_bl.a_AI[0], var2 + (var4 >> 1), var3 + var5 + 14, this.d_Image, 6, 7, 36);
    var1.drawImage(this.B_Image, var2 + var4, var3 + var5 + 14, 40);
  }

  private void a(Graphics var1, byte[] var2) {
    k.a(var1, d_bx, this.a_AI[0] + this.A_I - 3 - (this.A_I >> 2), this.a_AI[1] - 3 + this.a_AI[3] + 45, true, 0);
    if (var2[0] > 0) {
      var1.drawRegion(this.d_Image, 66, 0, 6, 7, 0, this.a_AI[0] + (this.A_I << 1) / 3 - 13, this.a_AI[1] + this.a_AI[3] + 60 - 2, 3);
      cg.a(var1, var2[0], this.a_AI[0] + (this.A_I << 1) / 3 - 4, this.a_AI[1] + this.a_AI[3] + 60 - 2, this.d_Image, 6, 7, 3);
      var1.drawRegion(this.d_Image, 72, 0, 6, 7, 0, this.a_AI[0] + (this.A_I << 1) / 3 + 3, this.a_AI[1] + this.a_AI[3] + 60 - 2, 3);
    }

    if (var2[1] > 0) {
      var1.drawRegion(this.d_Image, 66, 0, 6, 7, 0, this.a_AI[0] + this.A_I * 3 / 4 - 6, this.a_AI[1] + this.a_AI[3] + 30 - 2, 3);
      cg.a(var1, var2[1], this.a_AI[0] + this.A_I * 3 / 4 + 2, this.a_AI[1] + this.a_AI[3] + 30 - 3, this.d_Image, 6, 7, 3);
      var1.drawRegion(this.d_Image, 72, 0, 6, 7, 0, this.a_AI[0] + this.A_I * 3 / 4 + 10, this.a_AI[1] + this.a_AI[3] + 30 - 2, 3);
    }

    if (var2[3] > 0) {
      var1.drawRegion(this.d_Image, 66, 0, 6, 7, 0, this.a_AI[0] + this.A_I * 3 / 4 - 6, this.a_AI[1] + this.a_AI[3] + 90 - 3 - 2, 3);
      cg.a(var1, var2[3], this.a_AI[0] + this.A_I * 3 / 4 + 2, this.a_AI[1] + this.a_AI[3] + 90 - 5, this.d_Image, 6, 7, 3);
      var1.drawRegion(this.d_Image, 72, 0, 6, 7, 0, this.a_AI[0] + this.A_I * 3 / 4 + 10, this.a_AI[1] + this.a_AI[3] + 90 - 3 - 2, 3);
    }

    if (var2[2] > 0) {
      var1.drawRegion(this.d_Image, 66, 0, 6, 7, 0, this.a_AI[0] + this.A_I - (this.A_I >> 4) - 8, this.a_AI[1] + this.a_AI[3] + 60 - 2, 3);
      cg.a(var1, var2[2], this.a_AI[0] + this.A_I - (this.A_I >> 4), this.a_de.a()[1] + this.a_AI[3] + 60 - 2, this.d_Image, 6, 7, 3);
      var1.drawRegion(this.d_Image, 72, 0, 6, 7, 0, this.a_AI[0] + this.A_I - (this.A_I >> 4) + 8, this.a_AI[1] + this.a_AI[3] + 60 - 2, 3);
    }
  }

  public final void c(int var1) {
    this.E_I = var1;
  }

  private void a(Graphics var1, boolean var2, int var3, int var4, int var5) {
    int var6 = this.y_I / 3;
    int var7 = (this.x_I - 140) / 2;
    var1.setColor(1381651);
    var1.fillRoundRect(var7 - 2, var6 - 2, 143, 59, 8, 8);
    var1.setColor(16053217);
    var1.drawRoundRect(var7, var6, 140, 56, 8, 8);
    var1.setColor(6702925);
    var1.fillRect(var7 + 2, var6 + 2, 137, 53);
    cg.a(var1, "Số lượng:", var7 + 10, var6 + 8, 2547412, 411780, 20);
    if (this.t_Image == null) {
      this.t_Image = cc.a("/pic/D11.gf");
    }

    if (this.t_Image != null) {
      var1.drawRegion(
        this.t_Image,
        0,
        0,
        this.t_Image.getWidth() / 2,
        this.t_Image.getHeight(),
        0,
        var7 + vh.stringWidth(cg.a_Font, "Số lượng:") + 20 - this.E_B,
        var6 + 8 + cg.b_S / 2,
        3
      );
      var1.drawRegion(this.d_Image, 60, 0, 6, 7, 0, var7 + vh.stringWidth(cg.a_Font, "Số lượng:") + 20 + 25, var6 + 8 + cg.b_S / 2, 3);
      cg.a(var1, var5, var7 + vh.stringWidth(cg.a_Font, "Số lượng:") + 20 + 12, var6 + 8 + cg.b_S / 2, this.d_Image, 6, 7, 3);
      cg.a(var1, var3, var7 + vh.stringWidth(cg.a_Font, "Số lượng:") + 20 + 25 + 12, var6 + 8 + cg.b_S / 2, this.d_Image, 6, 7, 3);
      var1.drawRegion(
        this.t_Image,
        this.t_Image.getWidth() / 2,
        0,
        this.t_Image.getWidth() / 2,
        this.t_Image.getHeight(),
        0,
        var7 + vh.stringWidth(cg.a_Font, "Số lượng:") + 70 + this.E_B,
        var6 + 8 + cg.b_S / 2,
        3
      );
    }

    if (var2) {
      if (this.c_Image != null) {
        var1.drawImage(this.c_Image, var7 + 16, var6 + 8 + cg.b_S + 6, 20);
      }

      cg.a(var1, var4 * var5, this.x_I / 2, var6 + 8 + cg.b_S + 6 + this.c_Image.getHeight() / 2, this.d_Image, 6, 7, 3);
    }
  }

  private void a(Graphics var1, int[] var2, int var3, int var4, int var5, int var6, int var7, int var8) {
    var1.setColor(1381651);
    var1.fillRoundRect((this.x_I - var6) / 2 - 1, var5 - 1, var6 + 3, var7 + 3, 8, 8);
    var1.setColor(16053217);
    var1.drawRoundRect((this.x_I - var6) / 2, var5, var6, var7, 8, 8);
    var1.setColor(6768205);
    var1.fillRect((this.x_I - var6) / 2 + 2, var5 + 2, var6 - 3, var7 - 3);

    for (int var9 = 0; var9 < var2.length; var9++) {
      if (var9 == var3) {
        if (this.a_AImage[1] != null) {
          cg.a(var1, this.a_AImage[1], this.a_AImage[1].getWidth(), this.a_AImage[1].getHeight() / 17, var2[var9], var4, var5 + 5 + var9 * 17, 17, 0);
        } else {
          cg.a(var1, this.f_AString[var2[var9]], var4 + 3, var5 + 1 + var9 * 17, 12093825, 16777215, 17);
        }

        if (this.a_Image != null) {
          var1.drawRegion(this.a_Image, 0, 0, 15, 15, 0, (this.x_I - var6) / 2 + this.E_B, var5 + 5 + 6 + var9 * 17, 3);
        }
      } else if (this.a_AImage[0] != null) {
        cg.a(var1, this.a_AImage[0], this.a_AImage[0].getWidth(), this.a_AImage[0].getHeight() / 17, var2[var9], var4, var5 + 5 + var9 * 17, 17, 0);
      } else {
        cg.a(var1, this.f_AString[var2[var9]], var4 + 3, var5 + 1 + var9 * 17, 9306107, 2380419, 17);
      }
    }
  }

  public final void a(int var1, String var2) {
    if (var1 == 0) {
      a_Acv[this.a_l.c()].a_String = var2;
      this.a();
      q_B = 0;
    }
  }

  public final void a(String var1, int var2) {
    this.c_String = var1;
    this.F_I = var2;
    this.h_Z = true;
  }
}
