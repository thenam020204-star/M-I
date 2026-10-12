import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cd {
  public byte a_B;
  public byte b_B;
  byte c_B;
  private byte i_B;
  public static short a_S;
  public static short b_S;
  private short i_S;
  private short j_S;
  private static int b_I;
  private static int c_I;
  ca a_ca;
  public bj a_bj;
  cf a_cf;
  public bn a_bn;
  ax a_ax;
  as a_as;
  bb a_bb;
  bl a_bl;
  private bl c_bl;
  private bl d_bl;
  bl b_bl;
  private cr a_cr;
  cl a_cl;
  public ci a_ci;
  dc a_dc;
  bi[] a_Abi;
  public df a_df;
  private String d_String;
  boolean a_Z = false;
  private byte j_B;
  public static short c_S;
  private boolean w_Z;
  boolean b_Z = false;
  boolean c_Z = false;
  public String[] a_AString;
  public byte d_B;
  byte e_B;
  boolean d_Z;
  private byte k_B;
  private String e_String;
  private String[] c_AString;
  private int d_I;
  private int e_I;
  boolean e_Z;
  public byte f_B;
  public boolean f_Z;
  private short[] a_AS;
  private short[] b_AS;
  private short[] c_AS;
  public byte g_B;
  private boolean x_Z;
  private boolean y_Z;
  private byte l_B;
  public String a_String;
  private static boolean z_Z;
  public boolean g_Z;
  public boolean h_Z;
  private int f_I;
  private boolean A_Z;
  private boolean B_Z;
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  short d_S;
  boolean i_Z;
  boolean j_Z;
  boolean k_Z;
  int a_I = 2;
  boolean l_Z;
  private byte m_B;
  boolean m_Z;
  boolean n_Z;
  private bx b_bx;
  private bx c_bx;
  private Image d_Image;
  private Image e_Image;
  private bx d_bx;
  private bx e_bx;
  boolean o_Z;
  private int[][] a_AAI;
  private int[][] b_AAI;
  public String b_String;
  bx a_bx;
  private bx f_bx;
  private bx g_bx;
  private bx h_bx;
  short e_S;
  short f_S;
  public boolean p_Z;
  public boolean q_Z;
  public int[] a_AI;
  String[] b_AString;
  private Image f_Image;
  private Image g_Image;
  private Image h_Image;
  public boolean r_Z;
  boolean s_Z;
  private bx i_bx;
  byte h_B;
  short g_S;
  private Image i_Image;
  public String c_String;
  boolean t_Z;
  private static int g_I = 0;
  private static int h_I;
  private static int i_I;
  private int j_I;
  private int k_I;
  private int l_I;
  private int m_I;
  private boolean C_Z = false;
  private long a_J;
  private byte n_B = 1;
  private boolean D_Z;
  private String[] d_AString;
  private short k_S;
  short h_S;
  private short l_S = 0;
  boolean u_Z = false;
  boolean v_Z = false;
  private int n_I = 0;
  private boolean E_Z;
  private short m_S;
  private byte o_B;
  private boolean F_Z;

  public cd() {
    this.a_bb = new bb();
  }

  public final void a_V() {
    o.a_V();
    this.a_V(o.a_S);
  }

  public final void a(String var1) {
    da.a_da.a_bs = null;
    o.a_V(var1);
    this.a_V(o.b_S);
  }

  public final void a_V(int var1) {
    if (o.a_Z(var1)) {
      if (o.d_Z && o.a_Z) {
        if (o.m_AString != null) {
          o.b_S = cg.a_S(o.m_AString[0]);
        }

        o.a_Z(o.b_S);
        o.a_Z = false;
      } else {
        o.b_S = (short)var1;
      }

      String var2 = o.a_String;
      this.d_String = var2;
      this.b();
    }
  }

  public final void b() {
    this.a_Z = false;
    if (this.a_B != 1) {
      this.a_B = 1;
      new j(this);
    }

    this.k_B = 0;
  }

  public final void c() {
    at.h_Z = false;
    this.g_Z = false;
    this.h_Z = false;
    this.A_Z = true;
    this.f_I = 0;
    h_I = 0;
    i_I = 0;
    this.a_bx = null;
    this.l_S = 0;
    this.k_Z = false;
    this.u_Z = false;
    this.v_Z = false;
    this.n_I = 0;
    this.e();
    if (this.e_bx == null) {
      this.e_bx = new bx();
      this.e_bx.a("/av/long2.av");
    }

    this.w_Z = false;
    if (this.a_String != null && this.a_String.equals(this.d_String) && c_S < 0) {
      this.w_Z = true;
    }

    this.i_B = 10;
    if (this.d_bx == null) {
      this.d_bx = new bx();
      this.d_bx.a("/av/sms.av");
    }

    if (o.k_AString != null && o.k_AString.length == 2 && this.d_Image == null) {
      this.d_Image = cc.a("/map/pics/sdt1.gf");
      this.e_Image = cc.a("/map/pics/sdt2.gf");
    }

    this.a_String = this.d_String;
    if (this.f_bx == null) {
      this.f_bx = new bx();
      this.g_bx = new bx();
      this.h_bx = new bx();
      this.f_bx.a("/pic/task/task1.av");
      this.g_bx.a("/pic/task/task2.av");
      this.h_bx.a("/pic/task/task3.av");
    }

    this.i_B = 20;
    if (this.a_ca == null) {
      this.a_ca = new ca();
    } else {
      this.a_ca.a_V();

      try {
        Thread.sleep(20L);
      } catch (InterruptedException var3) {
        var3.printStackTrace();
      }
    }

    this.a_bj = null;
    this.a_bj = new bj();
    this.a_bj.a_V(o.a_B);
    this.a_ca.a_Z("/map/" + o.a_String + ".map");
    if (at.a_String.equals("N7610")) {
      this.a_ca.a(null, null, "/map/pics/", 1);
    } else {
      this.a_ca.a(null, null, "/map/pics/", 0);
    }

    if (o.a_AString != null) {
      this.p();
    }

    this.a_ca.c();
    a_S = this.a_ca.b_S();
    b_S = this.a_ca.a_S();
    this.i_B = 50;
    if (o.a_AString != null) {
      String[] var1 = null;

      for (int var2 = 0; var2 < o.a_AString.length; var2++) {
        var1 = cg.a(var1, o.a_AString[var2] + ".gf");
      }

      this.o();
      this.a_bj.a("/building/s" + o.f_String + "sp.dat", "/map/pics/", "/map/pics/", null, null, var1, this.a_AAI, this.b_AAI, -1, -1);
    } else {
      this.a_bj.a_V("/building/s" + o.f_String + "sp.dat", "/map/pics/", "/map/pics/");
    }

    ax[] var4 = a_Aax();

    for (int var7 = 0; var4 != null && var7 < var4.length; var7++) {
      this.a_bj.a(var4[var7]);
    }

    bl[] var8 = this.a_Abl();

    for (int var5 = 0; var8 != null && var5 < var8.length; var5++) {
      this.a_bj.a(var8[var5]);
    }

    this.i_B = 90;
    if (!this.w_Z) {
      this.a_ci = new ci();
      this.a_ci.a("/bin/s" + o.b_S + "e.txt", o.a_B);
    }

    this.a_bj.a(o.a_AI);
    this.g();
    if (o.a_Abl[0] != null) {
      this.i_S = (short)(o.a_Abl[0].d_S - da.a_da.getWidth() / 2);
      this.j_S = (short)(o.a_Abl[0].e_S - da.a_da.getHeight() / 2);
    }

    if (this.i_S < 0) {
      this.i_S = 0;
    } else if (this.i_S > a_S - da.a_da.getWidth()) {
      this.i_S = (short)(a_S - da.a_da.getWidth());
      if (this.i_S < 0) {
        this.i_S = 0;
      }
    }

    if (this.j_S < 0) {
      this.j_S = 0;
    } else if (this.j_S > b_S - da.a_da.getHeight()) {
      this.j_S = (short)(b_S - da.a_da.getHeight());
      if (this.j_S < 0) {
        this.j_S = 0;
      }
    }

    c_S = -1;
    boolean var6 = false;
    int var9 = Integer.MAX_VALUE / bb.a_B;
    int var10 = Math.abs(da.a_Random.nextInt()) / var9;
    this.a_bb.a(var10);
    this.a_bb.a_V();
    if (at.e_Z) {
      System.out.println("Tải xong");
    }

    this.i_B = 100;
    if (o.a_Abl[0] != null) {
      o.a_Abl[0].b_Z = false;
      o.a_Abl[0].c_Z = false;
      o.a_Abl[0].f_Z = false;
    }

    m();
    this.a_ci.a(0);
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      k.a_V();
    }

    this.x_Z = false;
    this.F_Z = false;
  }

  private bl[] a_Abl() {
    o.b_Abl = null;
    bl[] var1 = null;
    short var2 = o.b_S;
    byte var3 = o.a_B;
    Vector var4 = new Vector();
    if (o.q_String == null) {
      o.q_String = cg.a_String("/bin/sceneChange.bin");
    }

    if (o.b_S != 1 || o.a_Abl[0].b_Z || o.a_Abl[0].c_Z) {
      o.a_Abl[0].a_B = 1;
      o.a_Abl[0].a_bx = new bx();
      o.a_Abl[0].a_bx.a(o.e_AString[o.a_Abl[0].h_S - 1]);
      if (o.a_Abl[0].b_Z) {
        System.out.println("Đáp xuống");
        o.a_Abl[0].j_V();
      }
    }

    if (o.b_S == 1) {
      da.a_da.a_cd.h_Z = false;
      o.a_Abl[0].j_Z = false;
      da.a_da.a_cd.h_B = 2;
      ae.b(2);
    }

    if (!this.w_Z) {
      o.a_Abl[0].a_S = a_S;
      o.a_Abl[0].b_S = b_S;
      ao var5;
      if ((var5 = o.a_ao(o.a_Abl[0].h_S)) != null && o.a_Z) {
        if (o.d_Z) {
          if (o.j_AString != null) {
            int[] var19 = cg.b_AI(o.j_AString[0], ",");
            o.a_Abl[0].a(var19[0], var19[1]);
            o.a_Abl[0].a_V(var19[2]);
          }
        } else {
          o.a_Abl[0].a(var5.a_S, var5.b_S);
          o.a_Abl[0].a_V(var5.b_B);
        }

        o.a_Z = false;
      } else if (o.j_AString != null) {
        if (o.j_AString.length < 2) {
          System.out.println("Tọa độ bản đồ ngẫu nhiên không đủ‚chỉ có một điểm dịch chuyển");
        }

        int[] var6;
        if (this.o_Z) {
          var6 = cg.b_AI(o.j_AString[1], ",");
          this.o_Z = false;
        } else {
          var6 = cg.b_AI(o.j_AString[0], ",");
        }

        o.a_Abl[0].a(var6[0], var6[1]);
        o.a_Abl[0].a_V(var6[2]);
      } else {
        short[][] var18 = cg.a_AAS(o.q_String, "S" + var2 + "P:", "S" + var2 + "PEnd", "=");
        byte var7 = cg.a_B(o.q_String, "S" + var2 + "DP");
        if (var18 != null) {
          if (o.d_Z) {
            o.a_Abl[0].a(var18[var7 - 1][0], var18[var7 - 1][1]);
            o.a_Abl[0].a_V(var18[var7 - 1][2]);
          } else if (var18.length > 1) {
            if (c_S > 0) {
              if (c_S <= var18.length) {
                o.a_Abl[0].a(var18[c_S - 1][0], var18[c_S - 1][1]);
                o.a_Abl[0].a_V(var18[c_S - 1][2]);
              } else {
                o.a_Abl[0].a(var18[var7 - 1][0], var18[var7 - 1][1]);
                o.a_Abl[0].a_V(var18[var7 - 1][2]);
              }
            } else {
              o.a_Abl[0].a(var18[var7 - 1][0], var18[var7 - 1][1]);
              o.a_Abl[0].a_V(var18[var7 - 1][2]);
            }
          } else {
            o.a_Abl[0].a(var18[0][0], var18[0][1]);
            o.a_Abl[0].a_V(var18[0][2]);
          }
        } else {
          System.out.println("Không lấy được tọa độ trong cảnh của nhân vật");
        }
      }

      for (int var20 = 0; var20 < o.a_Abl.length; var20++) {
        var4.addElement(o.a_Abl[var20]);
      }

      if (o.a_Abl[0].c_Z) {
        o.a_Abl[0].c_B = 5;
      } else {
        o.a_Abl[0].c_B = 4;
      }

      for (int var21 = 0; var21 < o.a_Abl.length; var21++) {
        if (o.d_Z) {
          o.a_Abl[var21].j_Z = false;
        } else {
          o.a_Abl[var21].j_Z = true;
        }
      }

      o.a_Abl[0].c_V();
      o.a_Abl[0].d_V();
      o.c();
    } else {
      for (int var15 = 0; var15 < o.a_Abl.length; var15++) {
        var4.addElement(o.a_Abl[var15]);
      }

      o.a_Abl[0].a_V(bl.b_B);
    }

    if (o.c_AAS != null && o.b_S == 67) {
      bl var16 = new bl(k.a(o.c_AAS[0][0], false));
      short var22 = o.c_AAS[0][0];
      var16.a_bx.a_V((var22 - 1) * 3);
      var16.h_S = o.c_AAS[0][0];
      var16.i_B = 1;
      var16.g_B = 2;
      var16.a_String = "???";
      var16.a(o.q_AS[0], o.q_AS[1]);
      var16.a_S = a_S;
      var16.b_S = b_S;
      var16.c_B = 1;
      var16.a_Z = false;
      var16.g_Z = true;
      var16.c_V();
      var4.addElement(var16);
    }

    String var17 = cg.a_String("/bin/s" + var2 + "r.bin");
    boolean var23 = false;
    if (var17 != null) {
      var23 = true;
    }

    if (var23) {
      short[] var27 = cg.a_AS(var17, "roles");

      for (int var14 = 0; var14 < var27.length; var14++) {
        String var24;
        if ((var24 = cg.a(var17, "role" + var27[var14] + ":", "role" + var27[var14] + "End")) != null) {
          byte var8 = cg.a_B(var24, "amount");
          short var9 = var27[var14];

          for (int var10 = 0; var10 < var8; var10++) {
            String var11;
            byte[] var12 = cg.a_AB(var11 = cg.a(var24, "group" + (var10 + 1) + ":", "group" + (var10 + 1) + "End"), "phase");
            short[] var13 = cg.a_AS(var11, "pos");
            byte var28 = cg.a_B(var11, "walk");
            if (var12 != null && cg.a_Z(var12, var3) || var12 == null) {
              if (!cg.a_Z(o.b_AB, var9) && !cg.a_Z(o.c_AS, var9)) {
                bl var25;
                if (!o.e_AString[var9 - 1].toLowerCase().endsWith(".gf") && !o.e_AString[var9 - 1].toLowerCase().endsWith(".png")) {
                  bx var26;
                  (var26 = new bx()).a(o.e_AString[var9 - 1]);
                  var25 = new bl(var26);
                } else {
                  (var25 = new bl(cc.a(o.e_AString[var9 - 1]))).a_AS(1);
                }

                var25.h_S = var9;
                var25.i_B = 1;
                var25.g_B = 2;
                var25.a_String = o.c_AString[var9 - 1];
                if (Math.abs(o.a_Abl[0].d_S - var13[0]) < 10 && Math.abs(o.a_Abl[0].e_S - var13[1]) < 10) {
                  if (var13[0] <= o.a_Abl[0].d_S) {
                    var13[0] = (short)(var13[0] - 10);
                  } else {
                    var13[0] = (short)(var13[0] + 10);
                  }
                }

                var25.a(var13[0], var13[1]);
                var25.a_S = a_S;
                var25.b_S = b_S;
                var25.a_V(var13[2]);
                var25.c_B = 1;
                if (var28 == 1) {
                  var25.a_Z = true;
                } else {
                  var25.a_Z = false;
                }

                var25.g_Z = true;
                var4.addElement(var25);
                o.b(var25);
                var25.c_V();
              } else if (at.e_Z) {
                System.out.println("Tinh linh " + var9 + " Đang ở trong đội‚không thêm vào cảnh");
              }
              break;
            }
          }
        }
      }
    }

    if (var4.size() > 0) {
      var1 = new bl[var4.size()];
      var4.copyInto(var1);
    }

    if (var1 == null) {
      System.out.println("returnRoles tức nhóm đội ngũ đang trống");
    }

    return var1;
  }

  private static ax[] a_Aax() {
    ax[] var0 = null;
    short var1 = o.b_S;
    byte var2 = o.a_B;
    String[][] var3 = cg.a_AAString(cg.a_String("/bin/box.bin"), "box:", "boxEnd", null, "\t");
    String[][] var4 = null;

    for (int var5 = 0; var3 != null && var5 < var3.length; var5++) {
      if (cg.a_S(var3[var5][0]) == var1 || cg.a_B(var3[var5][2]) == var2) {
        var4 = k.a(var4, var3[var5]);
      }
    }

    if (var4 == null) {
      return null;
    } else {
      Vector var23 = new Vector();

      for (int var19 = 0; var19 < var4.length; var19++) {
        short var20 = cg.a_S(var4[var19][1]);
        byte var6 = cg.a_B(var4[var19][3]);
        short[] var7 = cg.b_AS(var4[var19][4], ",");
        boolean var8 = cg.a_B(var4[var19][5]) == 1;
        byte var9 = cg.a_B(var4[var19][6]);
        short var10 = cg.a_S(var4[var19][7]);
        String[] var15 = null;
        if (!var4[var19][8].equals("0")) {
          var15 = cg.b_AString(var4[var19][8], "|");
        }

        short[][] var11 = null;

        for (int var12 = 0; var15 != null && var12 < var15.length; var12++) {
          short[] var13 = cg.b_AS(var15[var12], ",");
          var11 = cg.a(var11, var13);
        }

        String[] var16 = null;
        if (!var4[var19][9].equals("0")) {
          var16 = cg.b_AString(var4[var19][9], "|");
        }

        short[][] var24 = null;

        for (int var25 = 0; var16 != null && var25 < var16.length; var25++) {
          short[] var14 = cg.b_AS(var16[var25], ",");
          var24 = cg.a(var24, var14);
        }

        ax var26 = null;
        if (var8) {
          if (var6 == 0) {
            bx var17;
            (var17 = new bx()).a("/av/box.av");
            var26 = new ax(var17);
          } else if (var6 == 1) {
            bx var18;
            (var18 = new bx()).a("/av/box1.av");
            var26 = new ax(var18);
          }
        } else {
          var26 = new ax();
        }

        var26.b_B = var6;
        var26.j_Z = var8;
        var26.a_S = var20;
        var26.a_V(var9);
        if (cg.a_Z(o.d_AS, var20)) {
          byte var21 = 1;
          var26.a_B = (byte)var21;
        } else {
          byte var22 = 0;
          var26.a_B = (byte)var22;
          var26.b_S = var10;
          var26.a_AAS = var11;
          var26.b_AAS = var24;
        }

        var26.a(var7[0], var7[1]);
        var26.i_B = 12;
        var23.addElement(var26);
      }

      if (var23.size() > 0) {
        var0 = new ax[var23.size()];
        var23.copyInto(var0);
      }

      return var0;
    }
  }

  public final void a(Graphics var1) {
    if (this.a_B == 2) {
      label691: {
        if (!at.h_Z) {
          if (o.a_Abl[0] == null) {
            break label691;
          }

          short var3 = (short)(o.a_Abl[0].d_S - da.a_da.getWidth() / 2);
          short var4 = (short)(o.a_Abl[0].e_S - da.a_da.getHeight() / 2);
          if (var3 < 0) {
            var3 = 0;
          } else if (var3 > a_S - da.a_da.getWidth() && (var3 = (short)(a_S - da.a_da.getWidth())) < 0) {
            var3 = 0;
          }

          if (var4 < 0) {
            var4 = 0;
          } else if (var4 > b_S - da.a_da.getHeight() && (var4 = (short)(b_S - da.a_da.getHeight())) < 0) {
            var4 = 0;
          }

          if (this.i_S < var3 && this.i_S < a_S - da.a_da.getWidth()) {
            this.i_S = (short)(this.i_S + 6);
            if (this.i_S > var3) {
              this.i_S = var3;
            }
          } else if (this.i_S > var3 && this.i_S > 0) {
            this.i_S = (short)(this.i_S - 6);
            if (this.i_S < var3) {
              this.i_S = var3;
            }
          }

          if (this.j_S < var4 && this.j_S < b_S - da.a_da.getHeight()) {
            this.j_S = (short)(this.j_S + 6);
            if (this.j_S > var4) {
              this.j_S = var4;
            }
          } else if (this.j_S > var4 && this.j_S > 0) {
            this.j_S = (short)(this.j_S - 6);
            if (this.j_S < var4) {
              this.j_S = var4;
            }
          }
        }

        int var5 = da.a_da.getWidth() <= a_S ? da.a_da.getWidth() : a_S;
        int var6 = da.a_da.getHeight() <= b_S ? da.a_da.getHeight() : b_S;
        if (this.a_bj != null) {
          this.a_bj.a(this.i_S, this.j_S, var5, var6);
        }

        if (this.a_ca != null) {
          this.a_ca.a(this.i_S, this.j_S, var5, var6);
        }
      }

      if (this.b_B != 1) {
        if ((this.b_B == 2 || this.b_B == 3) && da.a_da.a_S % 3 == 0) {
          this.a_bj.c();
        }
      } else {
        if (!this.a_ci.d_Z) {
          if (!this.a_ci.a_Z && this.b_B == 1 && o.a_Abl[0] != null) {
            this.b_bl = null;
            bl var10001;
            if (o.a_Abl[0] != null && this.a_bj != null) {
              if (this.c_bl == null) {
                this.c_bl = new bl();
              }

              var10001 = bl.a_bl(o.a_Abl[0]);
            } else {
              var10001 = null;
            }

            this.b_bl = var10001;
            if (this.b_bl != null) {
              this.c_Z = true;
            } else {
              this.c_Z = false;
            }
          }

          if (!this.a_ci.a_Z && this.b_B == 1 && o.a_Abl[0] != null && !o.a_Abl[0].b_Z) {
            ax var35;
            if (o.a_Abl[0] != null && this.a_bj != null) {
              if (this.c_bl == null) {
                this.c_bl = new bl();
              }

              var35 = bl.a_ax(o.a_Abl[0]);
            } else {
              var35 = null;
            }

            this.a_ax = var35;
            if (this.a_ax != null) {
              this.b_Z = true;
            } else {
              this.b_Z = false;
            }
          }

          this.a_ci.a(o.a_Abl[0].a_AS(1));
          ci var10000 = this.a_ci;
          short[] var15 = o.a_Abl[0].a_AS(1);
          ci var2 = var10000;
          if (!var10000.a_Z && var15 != null) {
            boolean var21 = false;

            for (int var23 = 0; var2.a_Abz != null && var23 < var2.a_Abz.length; var23++) {
              if (cg.a(
                  var2.a_Abz[var23].a_AS[0],
                  var2.a_Abz[var23].a_AS[1],
                  var2.a_Abz[var23].a_AS[2],
                  var2.a_Abz[var23].a_AS[3],
                  var15[0],
                  var15[1],
                  var15[2],
                  var15[3]
                )
                && !var2.a_Abz[var23].a_Z
                && !o.b_Z(var2.a_Abz[var23].a_S)) {
                var2.b_Z = true;
                var2.b_bz = var2.a_Abz[var23];
                var21 = true;
                break;
              }
            }

            if (!var21) {
              var2.b_Z = false;
              var2.b_bz = null;
            }
          }
        }

        if (da.a_da.a_S % 3 == 0) {
          this.a_bj.c();
        }

        cd var10 = this;

        for (int var16 = 0; var10.a_bj != null && var10.a_bj.a_Acz != null && var16 < var10.a_bj.a_Acz.length; var16++) {
          if (var10.a_bj.a_Acz[var16] != null && var10.a_bj.a_Acz[var16].i_B == 1 && ((bl)var10.a_bj.a_Acz[var16]).h_Z) {
            ((bl)var10.a_bj.a_Acz[var16]).a_V();
          }
        }

        var10 = this;

        for (int var17 = 0; var10.a_bj != null && var10.a_bj.a_Acz != null && var17 < var10.a_bj.a_Acz.length; var17++) {
          if (var10.a_bj.a_Acz[var17] != null && var10.a_bj.a_Acz[var17].i_B == 1 && ((bl)var10.a_bj.a_Acz[var17]).i_Z) {
            ((bl)var10.a_bj.a_Acz[var17]).k();
          }
        }

        if (this.E_Z) {
          boolean var18 = false;
          if (this.o_B == 0) {
            if (c_I <= 0) {
              var18 = true;
            }

            if (this.j_S + at.a_AS[1] >= b_S) {
              var18 = false;
            }
          } else if (this.o_B == 1) {
            if (c_I <= 0) {
              var18 = true;
            }

            if (this.j_S <= 0) {
              var18 = false;
            }
          } else if (this.o_B == 2) {
            if (b_I <= 0) {
              var18 = true;
            }

            if (this.i_S <= 0) {
              var18 = false;
            }
          } else if (this.o_B == 3) {
            if (b_I <= 0) {
              var18 = true;
            }

            if (this.i_S + at.a_AS[0] >= a_S) {
              var18 = false;
            }
          }

          if (var18) {
            at.h_Z = true;
            if (this.m_S * at.f_B > g_I) {
              byte var24 = at.f_B;
              switch (this.o_B) {
                case 0:
                  this.j_S = (short)(this.j_S + var24);
                  g_I += var24;
                  i_I += var24;
                  break;
                case 1:
                  this.j_S = (short)(this.j_S - var24);
                  g_I += var24;
                  i_I -= var24;
                  break;
                case 2:
                  this.i_S = (short)(this.i_S - var24);
                  g_I += var24;
                  h_I -= var24;
                  break;
                case 3:
                  this.i_S = (short)(this.i_S + var24);
                  g_I += var24;
                  h_I += var24;
              }

              if (this.j_S < 0) {
                i_I = i_I - this.j_S;
                this.j_S = 0;
              } else if (b_S > at.a_AS[1] && this.j_S + at.a_AS[1] > b_S) {
                i_I = i_I - (this.j_S + at.a_AS[1] - b_S);
                this.j_S = (short)(b_S - at.a_AS[1]);
              }

              if (this.i_S < 0) {
                h_I = h_I - this.i_S;
                this.i_S = 0;
              } else if (a_S > at.a_AS[0] && this.i_S + at.a_AS[0] > a_S) {
                h_I = h_I - (this.i_S + at.a_AS[0] - a_S);
                this.i_S = (short)(a_S - at.a_AS[0]);
              }
            } else {
              this.E_Z = false;
              g_I = 0;
              this.a_ci.a(5);
            }
          } else {
            this.E_Z = false;
            g_I = 0;
            this.a_ci.a(5);
          }
        }

        if (this.t_Z) {
          if (h_I > 0) {
            if (h_I < at.f_B) {
              h_I = h_I - h_I;
              this.i_S = (short)(this.i_S - h_I);
            } else {
              h_I = h_I - at.f_B;
              this.i_S = (short)(this.i_S - at.f_B);
            }
          } else if (h_I < 0) {
            if (h_I - at.f_B > 0) {
              h_I = h_I - h_I;
              this.i_S = (short)(this.i_S - h_I);
            } else {
              h_I = h_I + at.f_B;
              this.i_S = (short)(this.i_S + at.f_B);
            }
          }

          if (i_I > 0) {
            if (i_I < at.f_B) {
              i_I = i_I - i_I;
              this.j_S = (short)(this.j_S - i_I);
            } else {
              i_I = i_I - at.f_B;
              this.j_S = (short)(this.j_S - at.f_B);
            }
          } else if (i_I < 0) {
            if (i_I - at.f_B > 0) {
              i_I = i_I - i_I;
              this.j_S = (short)(this.j_S - i_I);
            } else {
              i_I = i_I + at.f_B;
              this.j_S = (short)(this.j_S + at.f_B);
            }
          }

          if (h_I == 0 && i_I == 0) {
            this.t_Z = false;
            at.h_Z = false;
            this.a_ci.a(5);
          }
        }

        var10 = this;

        for (int var19 = 0; var10.a_bj != null && var10.a_bj.a_Acz != null && var19 < var10.a_bj.a_Acz.length; var19++) {
          if (var10.a_bj.a_Acz[var19] != null
            && var10.a_bj.a_Acz[var19].i_B == 1
            && ((bl)var10.a_bj.a_Acz[var19]).g_B == 2
            && ((bl)var10.a_bj.a_Acz[var19]).e_Z
            && !((bl)var10.a_bj.a_Acz[var19]).h_Z
            && ((bl)var10.a_bj.a_Acz[var19]).j_Z) {
            ((bl)var10.a_bj.a_Acz[var19]).h_V();
          }
        }
      }

      if (da.a_da.a_S % 4 == 0) {
        if (this.a_bn == null && !this.a_ci.a_Z && o.n_AS != null) {
          for (int var20 = 0; var20 < o.n_AS.length; var20++) {
            if (!o.a_Abp[var20].a_Z) {
              bp var22;
              boolean var34;
              label580: {
                bp var28 = var22 = o.a_Abp[var20];
                boolean var13 = false;
                if (var28.c_String != null) {
                  String[] var25 = cg.b_AString(var28.c_String, "|");

                  for (int var29 = 0; var29 < var25.length; var29++) {
                    String[] var7;
                    if ((var7 = cg.b_AString(var25[var29], ",")) != null) {
                      if (var7[0].equals("有装备")) {
                        cg.a_I(var7[1]);
                        if (!o.c_Z(cg.a_I(var7[2]))) {
                          var34 = false;
                          break label580;
                        }

                        var13 = true;
                      } else if (var7[0].equals("道具数量")) {
                        if (o.b_I(cg.a_I(var7[1])) < cg.a_I(var7[2])) {
                          var34 = false;
                          break label580;
                        }

                        var13 = true;
                      } else if (var7[0].equals("有宠物")) {
                        if (!o.d_Z(cg.a_I(var7[1]))) {
                          var34 = false;
                          break label580;
                        }

                        var13 = true;
                      } else if (var7[0].equals("有技能")) {
                        if (!o.a_Z(o.a_Abl[0], cg.a_I(var7[1]))) {
                          var34 = false;
                          break label580;
                        }

                        var13 = true;
                      } else if (var7[0].equals("已发生事件")) {
                        if (!cg.a_Z(o.a_AI, cg.a_I(var7[1]))) {
                          var34 = false;
                          break label580;
                        }

                        var13 = true;
                      }
                    }
                  }
                }

                var34 = var13;
              }

              if (var34) {
                String var30 = var22.d_String;
                if (!var22.d_String.equals("-1")) {
                  String[] var26 = cg.b_AString(var30, "|");

                  for (int var31 = 0; var31 < var26.length; var31++) {
                    String[] var32;
                    ak.a(var32 = cg.b_AString(var26[var31], ","), "tmp");
                    if (var32 != null) {
                      if (var32[0].equals("扣除道具")) {
                        o.c(cg.a_I(var32[1]), cg.a_I(var32[2]));
                      } else if (var32[0].equals("扣除装备")) {
                        o.c_V(cg.a_I(var32[1]));
                      } else if (var32[0].equals("移除宠物")) {
                        int var14 = cg.a_I(var32[1]);

                        for (int var33 = 0; o.a_Acv != null && var33 < o.a_Acv.length; var33++) {
                          if (o.a_Acv[var33].a_I == var14) {
                            o.e_V(o.a_Acv[var33].b_I);
                            break;
                          }
                        }
                      }
                    }
                  }
                }

                var22.a_Z = true;

                for (int var27 = 0; o.n_AS != null && var27 < o.n_AS.length; var27++) {
                  if (o.n_AS[var27] == var22.a_S) {
                    o.m_AS[var27] = 1;
                    break;
                  }
                }

                da.a_da.a("“ " + var22.a_String + " \" hoàn thành!", 10);
              }
            }
          }
        }

        this.a_ci.a_V();
      }

      if (da.a_da.a_S % 3 == 0) {
        if (this.j_Z) {
          if (this.l_S < this.h_S) {
            this.l_S = (short)(this.l_S + 2);
          } else {
            this.j_Z = false;
            this.a_ci.a(5);
          }
        }

        if (this.k_Z) {
          if (this.l_S > 0) {
            this.l_S--;
          } else {
            this.k_Z = false;
            this.a_ci.a(5);
          }
        }

        if (this.u_Z) {
          if (this.n_I < at.a_AS[0] / 6) {
            this.n_I += 8;
          } else {
            this.u_Z = false;
            this.a_ci.a(5);
          }
        }

        if (this.v_Z) {
          if (this.n_I > 0) {
            this.n_I -= 4;
          } else {
            this.v_Z = false;
            this.a_ci.a(5);
          }
        }

        if (this.i_Z) {
          this.F_Z = !this.F_Z;
          this.g_B--;
          if (this.g_B <= 0) {
            this.i_Z = false;
            this.F_Z = false;
            this.a_ci.a(5);
          }
        }

        if (this.m_B == 0) {
          this.m_B = 1;
        } else {
          this.m_B = 0;
        }
      }

      if (da.a_da.a_S % 2 == 0 && this.x_Z) {
        this.g_Z = false;
        this.F_Z = !this.F_Z;
        this.g_B--;
        if (this.g_B <= 0) {
          this.x_Z = false;
          this.F_Z = false;
          this.a_bn = null;
          if (this.a_ci.d_Z) {
            this.g_Z = false;
          } else {
            this.g_Z = true;
          }

          if (!at.a_String.equals("N73")
            && !at.a_String.equals("N7610")
            && !at.a_String.equals("N5800")
            && !at.a_String.equals("E62")
            && !at.a_String.equals("W958C")
            && at.f_Z) {
            this.b_B = 0;
            this.a_ca.b_V();
            this.a_bj.a();
            k.a_V();
            this.e();
          }

          if (o.a_Abl[0].b_Z) {
            o.a_Abl[0].j_V();
          }

          try {
            Thread.sleep(0L);
          } catch (InterruptedException var9) {
            var9.printStackTrace();
          }

          this.a_bn = new bn();
          this.a_bn.b_AS = this.a_AS;
          this.a_bn.c_AS = this.b_AS;
          this.a_bn.a_AS = this.c_AS;
          this.a_bn.a_Abl = o.a_Abl();
          this.a_bn.a_Acv = null;
          this.a_bn.a_Acv = o.a_Acv();
          if (this.a_AS != null) {
            this.a_bn.b_Z = true;
          }

          this.a_bn.a_I = 28;
          if (this.a_bn.b_Z()) {
            this.b_B = 5;
            this.a_bn.c_V();
          } else {
            this.a_bn = null;

            try {
              Thread.sleep(200L);
            } catch (InterruptedException var8) {
              var8.printStackTrace();
            }
          }

          this.f_Z = false;
        }
      }

      if (this.d_Z) {
        if (da.a_da.a_S % 4 == 0) {
          this.k_B = 4;
        } else {
          this.k_B = 0;
        }
      } else {
        this.k_B = 0;
      }

      if (this.y_Z && da.a_da.a_S % 2 == 0) {
        this.l_B--;
        if (this.l_B > 0) {
          this.d_bl.j_Z = !this.d_bl.j_Z;
        } else {
          this.d_bl.j_Z = false;
          if (this.a_bj != null) {
            this.a_bj.c(this.d_bl);
          }

          this.d_bl = null;
          this.y_Z = false;
          this.a_ci.a(0);
        }
      }
    }

    if (this.a_B == 2 && this.b_B == 1 && this.g_Z) {
      if (cg.a_Z(da.a_da.a_AI, 2)) {
        o.a_Abl[0].a_V(2);
        o.a_Abl[0].b_V();
      } else if (cg.a_Z(da.a_da.a_AI, 5)) {
        o.a_Abl[0].a_V(3);
        o.a_Abl[0].b_V();
      } else if (cg.a_Z(da.a_da.a_AI, 1)) {
        o.a_Abl[0].a_V(1);
        o.a_Abl[0].b_V();
      } else if (cg.a_Z(da.a_da.a_AI, 6)) {
        o.a_Abl[0].a_V(0);
        o.a_Abl[0].b_V();
      } else if (!o.a_Abl[0].b_Z) {
        if (o.a_Abl[0].h_S == 1 || o.a_Abl[0].h_S == 2 || o.a_Abl[0].h_S == 3) {
          o.a_Abl[0].c_V();
        }
      } else {
        if (o.a_Abl[0].a_bx.a() == 4) {
          o.a_Abl[0].a_bx.a_V(0);
        } else if (o.a_Abl[0].a_bx.a() == 5) {
          o.a_Abl[0].a_bx.a_V(1);
        } else if (o.a_Abl[0].a_bx.a() == 6) {
          o.a_Abl[0].a_bx.a_V(2);
        } else if (o.a_Abl[0].a_bx.a() == 7) {
          o.a_Abl[0].a_bx.a_V(3);
        }

        if (da.a_da.a_S % 2 == 0 && o.a_Abl[0].c_Z) {
          o.a_Abl[0].a_bx.b(0);
        }
      }
    }

    this.b(var1);
  }

  public final void b_V(int var1) {
    o.c_AS = cg.b(o.c_AS, var1);
    this.a_ci.a(0);
  }

  public final void a(boolean var1) {
    this.a(null, null, null);
  }

  public final bl a_bl(int var1) {
    if (o.a_Abl[0] != null && this.a_bj != null) {
      if (this.c_bl == null) {
        this.c_bl = new bl();
      }

      return bl.a_bl(var1);
    } else {
      return null;
    }
  }

  private void b(Graphics var1) {
    if (this.a_B == 1) {
      i.a(0);
      var1.setColor(0);
      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
      var1.setColor(16777113);
      if (o.l_AString != null) {
        String[] var18 = cg.a(o.l_AString[o.f_B], at.a_AS[0] - 50);

        for (int var25 = 0; var25 < var18.length; var25++) {
          vh.drawString(var1, var18[var25], at.a_AS[0] / 2, at.a_AS[1] / 3 + var25 * cg.b_S, 17);
        }
      }

      if (this.e_bx != null) {
        this.e_bx.d_S = (short)(15 + (at.a_AS[0] - 30) * this.j_B / 100);
        this.e_bx.e_S = (short)(at.a_AS[1] - 15);
        this.e_bx.a_V(3);
        this.e_bx.a(var1);
        if (da.a_da.a_S % 2 == 0) {
          this.e_bx.a(true);
        }
      }

      if (this.j_B < this.i_B) {
        this.j_B++;
        if (this.j_B > this.i_B) {
          this.j_B = this.i_B;
        }
      }

      if (this.a_Z && this.j_B >= 100) {
        if (o.l_AString != null) {
          o.f_B = (byte)k.a(0, o.l_AString.length);
        }

        this.e_bx = null;
        if (!at.a_String.equals("N73")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("N5800")
          && !at.a_String.equals("E62")
          && !at.a_String.equals("W958C")) {
          k.a_V();
        }

        this.i_B = 0;
        this.j_B = 0;
        this.a_B = 2;
        this.b_B = 1;
        return;
      }
    } else if (this.a_B != 2) {
      if (this.a_B == 3) {
        i.a(0);
        var1.setColor(0);
        var1.setClip(0, 0, da.a_da.b_S, da.a_da.c_S);
        var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
        var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
        var1.setColor(16777215);
        vh.drawString(var1, "Ta... bị đánh bại rồi sao?", at.a_AS[0] / 2, at.a_AS[1] / 2 - 40, 17);
        vh.drawString(var1, "Hả? Cơ thể không cử động được nữa.", at.a_AS[0] / 2, at.a_AS[1] / 2 - 20, 17);
        vh.drawString(var1, "Kết thúc thế này sao?", at.a_AS[0] / 2, at.a_AS[1] / 2, 17);
        z_Z = true;
      }
    } else {
      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      if (da.a_da.a_S % ct.a_B == 0) {
        var1.setColor(0);
        var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
        if (this.b_B == 1) {
          i.a(3);
        } else {
          i.a(0);
        }

        if ((
            at.a_String.equals("E6")
              || at.a_String.equals("E680")
              || at.a_String.equals("W958C")
              || at.a_String.equals("S1")
              || at.a_String.equals("N95")
              || at.a_String.equals("N5800")
          )
          && this.b_B == 1) {
          byte var3 = 2;
          if (!i.a_Z((byte)5)) {
            cm var4;
            (var4 = new cm()).a_B = 5;
            var4.a_AS = new short[]{(short)(at.a_AS[0] / 2 - 13), (short)(da.a_da.c_S - 26), 26, 26};
            i.a(var4);
          }
        }

        if (this.b_B == 9 || this.b_B == 8 || this.b_B == 1 || this.b_B == 2 || this.b_B == 3 || this.b_B == 4 || this.b_B == 6 || this.b_B == 7) {
          if (a_S < at.a_AS[0]) {
            b_I = (at.a_AS[0] - a_S) / 2;
          } else {
            b_I = 0;
          }

          if (b_S < at.a_AS[1]) {
            c_I = (at.a_AS[1] - b_S) / 2;
          } else {
            c_I = 0;
          }

          if (this.a_ca != null) {
            this.a_ca.a(var1, b_I, this.k_B + c_I);
          }

          if (o.k_AString != null && o.k_AString.length == 2) {
            for (int var2 = 0; var2 < o.k_AString.length; var2++) {
              short[] var19 = cg.b_AS(o.k_AString[var2], ",");
              if (var2 == 0) {
                if (this.d_Image != null && o.h_B == 2) {
                  var1.drawImage(this.d_Image, var19[0] + b_I - this.i_S, var19[1] + 10 + this.k_B + c_I - this.j_S, 33);
                }
              } else if (var2 == 1 && this.e_Image != null) {
                var1.drawImage(this.e_Image, var19[0] + b_I - this.i_S, var19[1] + 10 + this.k_B + c_I - this.j_S, 33);
              }
            }
          }

          if (this.a_bx != null) {
            k.a(var1, this.a_bx, this.e_S + b_I - this.i_S, this.f_S + c_I - this.j_S, true, 0);
          }

          if (this.a_bj != null) {
            this.a_bj.a(var1, b_I, this.k_B + c_I);
          }

          if (this.a_ca != null) {
            this.a_ca.b(var1, b_I, this.k_B + c_I);
          }

          if (this.a_bj != null) {
            this.a_bj.c(var1, b_I, this.k_B + c_I);
            Graphics var20 = var1;
            if (o.e_AAString == null) {
              o.e_AAString = cg.a_AAString(cg.a_String("/bin/task.bin"), "task", "taskend", null, "\t");
            }

            int[] var26 = null;

            for (int var6 = 0; o.b_Abl != null && var6 < o.b_Abl.length; var6++) {
              for (int var7 = 0; o.a_Abp != null && var7 < o.a_Abp.length; var7++) {
                if (cg.a_Z(o.n_AS, o.a_Abp[var7].a_S) && o.b_Abl[var6].h_S == o.a_Abp[var7].b_S) {
                  if (o.a_Abp[var7].a_Z) {
                    k.a(
                      var20,
                      da.a_da.a_cd.h_bx,
                      o.b_Abl[var6].d_S + b_I - da.a_da.a_cd.i_S,
                      o.b_Abl[var6].e_S + c_I - o.b_Abl[var6].i_S - da.a_da.a_cd.j_S - 3,
                      true,
                      0
                    );
                  } else {
                    k.a(
                      var20,
                      da.a_da.a_cd.g_bx,
                      o.b_Abl[var6].d_S + b_I - da.a_da.a_cd.i_S,
                      o.b_Abl[var6].e_S + c_I - o.b_Abl[var6].i_S - da.a_da.a_cd.j_S - 3,
                      true,
                      0
                    );
                  }

                  var26 = cg.a_AI(var26, var6);
                }
              }

              int var33 = -1;

              for (int var11 = 0; var11 < o.e_AAString.length; var11++) {
                if (o.b_Abl[var6].h_S == cg.a_S(o.e_AAString[var11][9])) {
                  var33 = var11;
                  break;
                }
              }

              if (var33 >= 0 && !cg.a_Z(o.n_AS, cg.a_S(o.e_AAString[var33][0])) && !cg.a_Z(o.o_AS, cg.a_S(o.e_AAString[var33][0]))) {
                if (o.e_AAString[var33][2].equals("-1")) {
                  if (!cg.a_Z(var26, var6)) {
                    k.a(
                      var20,
                      da.a_da.a_cd.f_bx,
                      o.b_Abl[var6].d_S + b_I - da.a_da.a_cd.i_S,
                      o.b_Abl[var6].e_S + c_I - o.b_Abl[var6].i_S - da.a_da.a_cd.j_S - 3,
                      true,
                      0
                    );
                  }
                } else {
                  String[] var12;
                  if ((var12 = cg.b_AString(o.e_AAString[var33][2], ","))[0].equals("已发生事件")
                    && cg.a_Z(o.a_AI, cg.a_I(var12[1]))
                    && !cg.a_Z(var26, var6)
                    && !cg.a_Z(var26, var6)) {
                    k.a(
                      var20,
                      da.a_da.a_cd.f_bx,
                      o.b_Abl[var6].d_S + b_I - da.a_da.a_cd.i_S,
                      o.b_Abl[var6].e_S + c_I - o.b_Abl[var6].i_S - da.a_da.a_cd.j_S - 3,
                      true,
                      0
                    );
                  }
                }
              }
            }

            this.a_bj.b(var1, b_I, c_I);
          }

          var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
        }

        if (this.a_bb != null) {
          this.a_bb.a(var1);
        }

        if (!this.a_bb.a_Z()) {
          if (this.A_Z) {
            if (o.d_Z) {
              if (this.c_bx == null) {
                this.c_bx = new bx();
                this.c_bx.a("/ft/tanshe.av");
              }

              this.c_bx.d_S = (short)(o.a_Abl[0].d_S + b_I - this.i_S);
              this.c_bx.e_S = (short)(o.a_Abl[0].e_S + c_I - this.j_S);
              this.c_bx.a(var1);
              this.c_bx.a(false);
              if (this.c_bx.c() >= this.c_bx.d() - 1) {
                o.a_Abl[0].j_Z = true;
              }
            }

            this.f_I++;
            if (this.f_I >= 10) {
              if (this.c_bx != null) {
                this.c_bx.b(0);
              }

              this.A_Z = false;
              this.h_Z = true;
            }
          }

          if (this.n_Z && !this.a_ci.a_Z) {
            if (this.b_bx == null) {
              this.b_bx = new bx();
              this.b_bx.a("/skill/douqi.av");
            }

            k.a(var1, this.b_bx, o.a_Abl[0].d_S + b_I - this.i_S, o.a_Abl[0].e_S + c_I - this.j_S, true, 0);
            if (this.b_bx.c() >= this.b_bx.d() - 1) {
              this.n_Z = false;
              this.b_bx.b(0);
            }
          }

          if (this.s_Z) {
            if (this.i_bx == null) {
              this.i_bx = new bx();
              this.i_bx.a("/av/bianxing.av");
            }

            k.a(var1, this.i_bx, o.a_Abl[0].d_S + b_I - this.i_S, o.a_Abl[0].e_S + c_I - this.j_S, true, 0);
            if (this.i_bx.c() >= this.i_bx.d() - 1) {
              this.s_Z = false;
              this.i_bx.b(0);
              ae.b(this.h_B);
            }
          }

          if (this.D_Z && !this.a_ci.a_Z && this.d_AString != null) {
            Graphics var27 = var1;
            cd var21 = this;
            if (this.k_S < this.d_AString.length * (cg.b_S + 1) + cg.b_S) {
              k.a(var1, 40, at.a_AS[1] - cg.b_S - 6, at.a_AS[0] - 80, cg.b_S + 6, 1317669, 15788720, 80, false);
              var1.setClip(0, at.a_AS[1] - 3 - cg.b_S, at.a_AS[0], cg.b_S);
              var1.setColor(16777215);

              for (int var31 = 0; var21.d_AString != null && var31 < var21.d_AString.length; var31++) {
                vh.drawString(var27, var21.d_AString[var31], at.a_AS[0] / 2, at.a_AS[1] - 3 - var21.k_S + (cg.b_S + 1) * var31, 17);
              }

              if (!var21.C_Z) {
                var21.k_S++;
                if (var21.k_S == var21.n_B * (cg.b_S + 1)) {
                  var21.n_B++;
                  var21.C_Z = true;
                  var21.a_J = System.currentTimeMillis();
                }
              } else if (System.currentTimeMillis() - var21.a_J >= 1000L) {
                var21.C_Z = false;
              }
            } else {
              this.n_B = 1;
              this.D_Z = false;
              this.d_AString = null;
            }

            var27.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
          }

          if (this.m_Z) {
            for (int var13 = 0; var13 < this.a_Abi.length; var13++) {
              bi var22 = this.a_Abi[var13];
              var1.setColor(16777215);
              var1.drawLine(var22.a_S, var22.b_S, var22.a_S + var22.c_B, var22.b_S + var22.d_B);
              if (var22.a_S < -10 || var22.b_S > at.a_AS[1]) {
                var22.a_S = (short)k.a(0, at.a_AS[0] + 150);
                var22.b_S = 0;
              }

              var22.a_S = (short)(var22.a_S + var22.a_B);
              var22.b_S = (short)(var22.b_S + var22.b_B);
            }
          }

          if (this.b_B != 4 && this.b_B != 5) {
            cg.a(var1, o.b_String, 2, 2, 65370, 0, 20);
          }

          if (this.h_Z
            && !this.a_ci.a_Z
            && this.b_B != 6
            && !this.l_Z
            && this.b_B != 3
            && this.b_B != 4
            && this.b_B != 5
            && this.b_B != 7
            && this.b_B != 8
            && this.d_bx != null) {
            if (!at.a_String.equals("E2")
              && !at.a_String.equals("E398")
              && !at.a_String.equals("L6")
              && !at.a_String.equals("L7")
              && !at.a_String.equals("K1")
              && !at.a_String.equals("V8")) {
              this.d_bx.d_S = (short)(at.a_AS[0] - 20);
              this.d_bx.e_S = (short)(at.a_AS[1] - 8);
            } else {
              this.d_bx.d_S = 20;
              this.d_bx.e_S = (short)(at.a_AS[1] - 8);
            }

            this.d_bx.a(var1);
            if (da.a_da.a_S % 2 == 0) {
              this.d_bx.a(true);
            }
          }

          if (this.l_S > 0) {
            var1.setColor(0);
            var1.fillRect(0, 0, at.a_AS[0], this.l_S << 2);
            var1.fillRect(0, at.a_AS[1] - (this.l_S << 2), at.a_AS[0], at.a_AS[1] - (this.l_S << 2));
          }

          if (this.n_I > 0) {
            var1.setColor(0);

            for (int var14 = 0; var14 < 6; var14++) {
              var1.fillRect(var14 * at.a_AS[0] / 6, 0, this.n_I, at.a_AS[1]);
            }
          }

          if (this.h_Z && this.b_B != 6 && this.b_B != 5 && this.b_B != 3 && !da.a_da.a_cd.a_ci.a_Z) {
            i.a(var1);
          }

          if (this.b_B == 10) {
            if (this.i_Image == null) {
              this.i_Image = cc.a("/pic/order.jpg");
            }

            if (this.i_Image != null) {
              var1.drawImage(this.i_Image, at.a_AS[0] / 2, at.a_AS[1] / 2, 3);
            }

            var1.setColor(16777215);
            vh.drawString(var1, "Nhấn OK để tiếp tục", at.a_AS[0] / 2, at.a_AS[1] - 4, 33);
          }

          if (this.b_B == 9) {
            Graphics var28 = var1;
            cd var24 = this;
            if (o.e_AAString == null) {
              o.e_AAString = cg.a_AAString(cg.a_String("/bin/task.bin"), "task", "taskend", null, "\t");
            }

            int var34 = o.e_I(this.g_S);
            int var17 = at.a_AS[1] / 2 - 5 * (cg.b_S + 1);
            k.a(var1, 40, var17, at.a_AS[0] - 80, 9 * (cg.b_S + 1), 1317669, 15788720, 80, true);
            String[] var5 = new String[]{"Chính tuyến", "Phụ tuyến", "Công hội"};
            cg.a(
              var1,
              o.e_AAString[var34][1] + " [" + var5[cg.a_I(o.e_AAString[var34][3]) - 1] + "]",
              at.a_AS[0] / 2,
              var17 + 2 + (30 - cg.b_S) / 2,
              9306111,
              2248833,
              17
            );
            var1.setColor(16709092);
            var1.fillRect(50, var17 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 5, at.a_AS[0] - 100, 1);
            var1.setColor(3027509);
            var1.fillRect(50, var17 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 6, at.a_AS[0] - 100, 1);
            if (this.c_B == 1) {
              k.a(var1, "Mô tả nhiệm vụ", at.a_AS[0] / 2, var17 + 2 + 30 + (30 - cg.b_S) / 2, 2093589, 17);
              String var30 = o.e_AAString[var34][5];
              if (this.a_dc != null) {
                this.a_dc.a();
              } else {
                this.a_dc = new dc();
              }

              this.a_dc.a(var30, at.a_AS[0] - 90, null, 16777215);
              this.a_dc.a(45, var17 + 2 + 60, at.a_AS[0] - 80, cg.b_S + 1 << 2, 4);
              this.a_dc.a(var1, this.a_dc, this.d_B, true, false);
              if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
                cg.a(var1, "Thưởng", 43, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
                cg.a(var1, "Bỏ", at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
              } else {
                cg.a(var1, "Bỏ", 43, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
                cg.a(var1, "Thưởng", at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
              }
            } else {
              var5 = cg.b_AString(o.e_AAString[var34][8], "|");
              k.a(var1, "Phần thưởng", at.a_AS[0] / 2, var17 + 2 + 30 + (30 - cg.b_S) / 2, 2093589, 17);
              var1.setColor(16777215);
              var34 = 0;

              for (int var8 = 0; var8 < var5.length; var8++) {
                int[] var32;
                if ((var32 = cg.b_AI(var5[var8], ","))[0] == 1) {
                  o.d();
                  if (o.a_AImage == null) {
                    o.e();
                  }

                  if (o.a_AImage[o.d_I(var32[1])] != null) {
                    var28.drawImage(o.a_AImage[o.d_I(var32[1])], 60, var17 + 2 + 60 + 15 + (cg.b_S + 1) * var34, 3);
                  }

                  vh.drawString(var28, o.f_AString[o.d_I(var32[1])], at.a_AS[0] / 2, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 17);
                  vh.drawString(var28, "x " + var32[2], at.a_AS[0] - 40 - 10, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 24);
                  var34++;
                }

                if (var32[0] == 2) {
                  ck.a_V();
                  if (ck.a_AImage == null) {
                    ck.b_V();
                  }

                  if (ck.a_AImage[o.f_I(var32[1])] != null) {
                    var28.drawImage(ck.a_AImage[o.f_I(var32[1])], 60, var17 + 2 + 60 + 15 + (cg.b_S + 1) * var34, 3);
                  }

                  vh.drawString(var28, ck.a_AString[o.f_I(var32[1])], at.a_AS[0] / 2, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 17);
                  vh.drawString(var28, "x 1", at.a_AS[0] - 40 - 10, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 24);
                  var34++;
                }

                if (var32[0] == 3) {
                  if (var24.f_Image == null) {
                    var24.f_Image = cc.a("/pic/D10.gf");
                  }

                  var28.drawImage(var24.f_Image, 60, var17 + 2 + 60 + 15 + (cg.b_S + 1) * var34, 3);
                  vh.drawString(var28, "Vàng", at.a_AS[0] / 2, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 17);
                  vh.drawString(var28, "" + var32[1], at.a_AS[0] - 40 - 10, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 24);
                  var34++;
                }

                if (var32[0] == 4) {
                  if (var24.c_String != null) {
                    vh.drawString(var28, var24.c_String, at.a_AS[0] / 2, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 17);
                    vh.drawString(var28, "x 1", at.a_AS[0] - 40 - 10, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 24);
                  } else {
                    int var10 = (short)var32[1];
                    String[][] var9 = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
                    String var36 = var9[--var10][0];
                    var24.c_String = var36;
                  }

                  var34++;
                }

                if (var32[0] == 5) {
                  if (var24.h_Image == null) {
                    var24.h_Image = cc.a("/icon/13.gf");
                  }

                  var28.drawImage(var24.h_Image, 60, var17 + 2 + 60 + 15 + (cg.b_S + 1) * var34, 3);
                  vh.drawString(var28, "EXP", at.a_AS[0] / 2, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 17);
                  vh.drawString(var28, "" + var32[1], at.a_AS[0] - 40 - 10, var17 + 2 + 60 + (30 - cg.b_S) / 2 + (cg.b_S + 1) * var34, 24);
                  var34++;
                }
              }

              if (var24.g_Image == null) {
                System.out.println("Pooooooooollll");
                var24.g_Image = cc.a_Image("/pic/D23.gf", 1);
              }

              if (var24.g_Image != null) {
                if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
                  cg.a(var28, var24.g_Image, var24.g_Image.getWidth() >> 1, var24.g_Image.getHeight(), 0, 43, var17 + 9 * (cg.b_S + 1) - 3, 36, 0);
                  cg.a(
                    var28, var24.g_Image, var24.g_Image.getWidth() >> 1, var24.g_Image.getHeight(), 1, at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 40, 0
                  );
                } else {
                  cg.a(var28, var24.g_Image, var24.g_Image.getWidth() >> 1, var24.g_Image.getHeight(), 1, 43, var17 + 9 * (cg.b_S + 1) - 3, 36, 0);
                  cg.a(
                    var28, var24.g_Image, var24.g_Image.getWidth() >> 1, var24.g_Image.getHeight(), 0, at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 40, 0
                  );
                }
              } else if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
                cg.a(var28, "Thưởng", 43, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
                cg.a(var28, "Bỏ", at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
              } else {
                cg.a(var28, "Bỏ", 43, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
                cg.a(var28, "Thưởng", at.a_AS[0] - 40 - 3, var17 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
              }
            }
          } else if (this.b_B == 8) {
            this.c(var1);
          } else if (this.b_B == 2) {
            if (this.a_cf != null && this.a_cf.a_Z) {
              var1.translate(0, 0);
              this.a_cf.a(var1);
            }
          } else if (this.b_B == 4) {
            if (this.a_as != null) {
              this.a_as.a(var1);
            }
          } else if (this.b_B != 3) {
            if (this.b_B == 7 && this.a_df != null) {
              this.a_df.a(var1);
            }
          } else if (this.a_AString != null) {
            if (this.a_dc != null) {
              this.a_dc.a();
            } else {
              this.a_dc = new dc();
            }

            for (int var15 = 0; var15 < this.a_AString.length; var15++) {
              this.a_dc.a(this.a_AString[var15], at.a_AS[0] - 70, null, 16777215);
            }

            this.d_S = 3;
            int var16 = at.a_AS[1] / 2 - (this.d_S * (cg.b_S + 1) + 10) / 2;
            int var23 = this.d_S * (cg.b_S + 1) + 10;
            if (this.a_dc.a_AString.length + 1 < this.d_S) {
              var23 = (this.a_dc.a_AString.length + 1) * (cg.b_S + 1) + 10;
              var16 = at.a_AS[1] / 2 - ((this.a_dc.a_AString.length + 1) * (cg.b_S + 1) + 10) / 2;
            }

            k.a(var1, 20, var16, at.a_AS[0] - 40, var23, 1317669, 15788720, 80, true);
            if (this.c_Image == null) {
              this.c_Image = cc.a_Image("/pic/jiao1.gf", 2);
            }

            if (this.c_Image != null) {
              var1.drawRegion(this.c_Image, 0, 0, this.c_Image.getWidth() >> 1, this.c_Image.getHeight() >> 1, 0, 19, var16 - 1, 20);
              var1.drawRegion(
                this.c_Image,
                this.c_Image.getWidth() >> 1,
                0,
                this.c_Image.getWidth() >> 1,
                this.c_Image.getHeight() >> 1,
                0,
                21 + at.a_AS[0] - 40,
                var16 - 1,
                24
              );
              var1.drawRegion(
                this.c_Image, 0, this.c_Image.getWidth() >> 1, this.c_Image.getHeight() >> 1, this.c_Image.getHeight() >> 1, 0, 19, var16 + 1 + var23, 36
              );
              var1.drawRegion(
                this.c_Image,
                this.c_Image.getWidth() >> 1,
                this.c_Image.getHeight() >> 1,
                this.c_Image.getWidth() >> 1,
                this.c_Image.getHeight() >> 1,
                0,
                21 + at.a_AS[0] - 40,
                var16 + 1 + var23,
                40
              );
            } else {
              var1.setColor(2170141);
              var1.drawRoundRect(19, var16 - 1, at.a_AS[0] - 40 + 1, var23 + 1, 10, 10);
            }

            this.a_dc.a(20, var16 + 5, at.a_AS[0] - 40, var23 - 10, this.d_S);
            this.a_dc.a("Nhấn OK để tiếp tục", at.a_AS[0] - 70, null, 2093589);
            this.a_dc.a(var1, this.a_dc, this.d_B, false, true);
            var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
          }

          if (this.l_Z) {
            this.a_cl.a(var1);
          }

          if (this.e_Z) {
            var1.setColor(0);
            var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
            var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
            var1.setColor(16777215);
            vh.drawString(var1, "Nghỉ ngơi một chút", at.a_AS[0] / 2, at.a_AS[1] / 2, 17);
            this.f_B--;
            if (this.f_B <= 0) {
              this.e_Z = false;
              this.a_ci.a(0);
            }
          }

          if (o.b_Z) {
            this.e(var1);
          }

          if (this.F_Z) {
            var1.setColor(16777215);
            var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
            var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
          }

          if (at.e_Z) {
            vh.drawString(var1, "phase:", 0, 25, 20);
            if (this.b_Image == null) {
              this.b_Image = cc.a_Image("/pic/number01.gf", 2);
            }

            if (this.b_Image != null) {
              cg.a(var1, o.a_B, vh.stringWidth(cg.a_Font, "phase:") + 2, 30, this.b_Image, 6, 7, 20);
            }
          }

          if (this.c_Z && this.a_cf == null && this.a_as == null && this.a_df == null) {
            var1.setColor(16711680);
            if (this.b_bl.j_Z) {
              cg.a(var1, this.b_bl.a_String, at.a_AS[0] - 4, 2, 16711680, 16773728, 24);
            }
          }

          this.d(var1);
        }
      }

      if (this.b_B == 5) {
        if (this.a_bn != null) {
          this.a_bn.f();
          this.a_bn.a(var1);
          return;
        }
      } else if (this.b_B == 6) {
        if (!cr.a_Z) {
          return;
        }

        if (cr.a_bx == null) {
          cr.a_Z = false;
          da.a_da.a_cd.a_ci.a(5);
          cr.a_bx = null;
          da.a_da.a_cd.b_B = 1;
          return;
        }

        if (cr.a_I == 1) {
          var1.setColor(0);
          var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
        }

        cr.a_bx.d_S = cr.a_S;
        cr.a_bx.e_S = cr.b_S;
        cr.a_bx.a(var1);
        if (cr.a_bx.c() < cr.a_bx.a_I(0) - 1) {
          cr.a_bx.a(false);
        } else if (cr.a_I == 1) {
          var1.setColor(16777215);
          vh.drawString(var1, "Nhấn phím bất kỳ để trở về", at.a_AS[0] / 2, at.a_AS[1] - 4, 33);
        } else {
          cr.a_Z = false;
          da.a_da.a_cd.a_ci.a(5);
          cr.a_bx = null;
          da.a_da.a_cd.b_B = 1;
        }
      }
    }
  }

  private void c(Graphics var1) {
    int var3 = (at.a_AS[1] - 9 * (cg.b_S + 1)) / 2;
    int var2 = (at.a_AS[0] - 160) / 2;
    k.a(var1, var2, var3, at.a_AS[0] - var2 * 2, 9 * (cg.b_S + 1), 1317669, 15788720, 80, true);
    cg.a(var1, o.s_String + " [Phó bản]", at.a_AS[0] / 2, var3 + 2 + (30 - cg.b_S) / 2, 9306111, 2248833, 17);
    var1.setColor(16709092);
    var1.fillRect(var2 + 10, var3 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 5, at.a_AS[0] - 2 * (var2 + 10), 1);
    var1.setColor(3027509);
    var1.fillRect(var2 + 10, var3 + 2 + (30 - cg.b_S) / 2 + cg.b_S + 6, at.a_AS[0] - 2 * (var2 + 10), 1);
    k.a(var1, "Chiến lợi phẩm", at.a_AS[0] / 2, var3 + 2 + 30 + (30 - cg.b_S) / 2, 2093589, 17);
    var1.setColor(16777215);
    if (this.a_AI != null) {
      if (this.a_AI[0] == 1) {
        o.d();
        if (o.a_AImage == null) {
          o.e();
        }

        if (o.a_AImage[o.d_I(this.a_AI[1])] != null) {
          var1.drawImage(o.a_AImage[o.d_I(this.a_AI[1])], var2 + 20, var3 + 2 + 60 + 15, 3);
        }

        vh.drawString(var1, o.f_AString[o.d_I(this.a_AI[1])], at.a_AS[0] / 2, var3 + 2 + 60 + (30 - cg.b_S) / 2, 17);
        vh.drawString(var1, "x " + this.a_AI[2], at.a_AS[0] - var2 - 10, var3 + 2 + 60 + (30 - cg.b_S) / 2, 24);
        if (this.b_AString == null) {
          this.b_AString = cg.a(this.b_AString, "Nhận " + o.f_AString[o.d_I(this.a_AI[1])] + "  x" + this.a_AI[2]);
        }
      } else if (this.a_AI[0] == 2) {
        ck.a_V();
        if (ck.a_AImage == null) {
          ck.b_V();
        }

        if (ck.a_AImage[o.f_I(this.a_AI[1])] != null) {
          var1.drawImage(ck.a_AImage[o.f_I(this.a_AI[1])], var2 + 20, var3 + 2 + 60 + 15, 3);
        }

        vh.drawString(var1, ck.a_AString[o.f_I(this.a_AI[1])], at.a_AS[0] / 2, var3 + 2 + 60 + (30 - cg.b_S) / 2, 17);
        vh.drawString(var1, "x 1", at.a_AS[0] - var2 - 10, var3 + 2 + 60 + (30 - cg.b_S) / 2, 24);
        if (this.b_AString == null) {
          this.b_AString = cg.a(this.b_AString, "Nhận " + ck.a_AString[o.f_I(this.a_AI[1])] + "  x" + this.a_AI[2]);
        }
      } else if (this.a_AI[0] == 3) {
        if (this.f_Image == null) {
          this.f_Image = cc.a("/pic/D10.gf");
        }

        var1.drawImage(this.f_Image, var2 + 20, var3 + 2 + 60 + 15, 3);
        vh.drawString(var1, "Vàng", at.a_AS[0] / 2, var3 + 2 + 60 + (30 - cg.b_S) / 2, 17);
        vh.drawString(var1, "" + this.a_AI[1], at.a_AS[0] - var2 - 10, var3 + 2 + 60 + (30 - cg.b_S) / 2, 24);
        if (this.b_AString == null) {
          this.b_AString = cg.a(this.b_AString, "Nhận vàng+" + this.a_AI[1]);
        }
      } else if (this.a_AI[0] == 4) {
        if (this.c_String != null) {
          vh.drawString(var1, this.c_String, at.a_AS[0] / 2, var3 + 2 + 60 + (30 - cg.b_S) / 2, 17);
          vh.drawString(var1, "x 1", at.a_AS[0] - var2 - 10, var3 + 2 + 60 + (30 - cg.b_S) / 2, 24);
          if (this.b_AString == null) {
            this.b_AString = cg.a(this.b_AString, "Nhận sủng vật [" + this.c_String + "]");
          }
        } else {
          short var6 = (short)this.a_AI[1];
          String[][] var4 = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
          int var5 = var6 - 1;
          String var7 = var4[var5][0];
          this.c_String = var7;
        }
      }
    }

    Object var8 = null;
    int var9 = 0;

    while (o.n_AString != null && var9 < o.n_AString.length && ((Object[])(var8 = cg.b_AI(o.n_AString[var9], ",")))[0] != 5) {
      var9++;
    }

    if (var8 != null) {
      if (this.h_Image == null) {
        this.h_Image = cc.a("/icon/13.gf");
      }

      var1.drawImage(this.h_Image, var2 + 20, var3 + 2 + 60 + 15 + cg.b_S, 3);
      vh.drawString(var1, "EXP", at.a_AS[0] / 2, var3 + 2 + 60 + (30 - cg.b_S) / 2 + cg.b_S + 1, 17);
      vh.drawString(var1, "" + ((Object[])var8)[1], at.a_AS[0] - var2 - 10, var3 + 2 + 60 + (30 - cg.b_S) / 2 + cg.b_S + 1, 24);
      if (this.b_AString == null || this.b_AString != null && this.b_AString.length < 2) {
        this.b_AString = cg.a(this.b_AString, "Nhận EXP+" + ((Object[])var8)[1]);
      }
    }

    if (this.g_Image == null) {
      this.g_Image = cc.a_Image("/pic/D23.gf", 1);
    }

    if (this.g_Image != null) {
      if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        cg.a(var1, this.g_Image, this.g_Image.getWidth() >> 1, this.g_Image.getHeight(), 0, var2 + 3, var3 + 9 * (cg.b_S + 1) - 3, 36, 0);
        cg.a(var1, this.g_Image, this.g_Image.getWidth() >> 1, this.g_Image.getHeight(), 1, at.a_AS[0] - var2 - 3, var3 + 9 * (cg.b_S + 1) - 3, 40, 0);
      } else {
        cg.a(var1, this.g_Image, this.g_Image.getWidth() >> 1, this.g_Image.getHeight(), 1, var2 + 3, var3 + 9 * (cg.b_S + 1) - 3, 36, 0);
        cg.a(var1, this.g_Image, this.g_Image.getWidth() >> 1, this.g_Image.getHeight(), 0, at.a_AS[0] - var2 - 3, var3 + 9 * (cg.b_S + 1) - 3, 40, 0);
      }
    } else if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      cg.a(var1, "Nhận", var2 + 3, var3 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
      cg.a(var1, "Bỏ", at.a_AS[0] - var2 - 3, var3 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
    } else {
      cg.a(var1, "Bỏ", var2 + 3, var3 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 36);
      cg.a(var1, "Nhận", at.a_AS[0] - var2 - 3, var3 + 9 * (cg.b_S + 1) - 3, 16642337, 11208715, 40);
    }
  }

  private void d(Graphics var1) {
    if (at.e_Z) {
      var1.translate(b_I, c_I);
      var1.translate(-this.i_S, -this.j_S);
      this.a_ca.a(var1);
      var1.translate(this.i_S, this.j_S);
      var1.translate(-this.i_S, -this.j_S);
      Graphics var3 = var1;
      ci var2 = this.a_ci;

      for (int var4 = 0; var2.a_Abz != null && var4 < var2.a_Abz.length; var4++) {
        if (cg.a_Z(o.a_AI, var2.a_Abz[var4].a_S)) {
          var3.setColor(15597568);
        } else {
          var3.setColor(16777215);
        }

        var3.drawRect(var2.a_Abz[var4].a_AS[0], var2.a_Abz[var4].a_AS[1], var2.a_Abz[var4].a_AS[2], var2.a_Abz[var4].a_AS[3]);
        vh.drawString(var3, String.valueOf(var2.a_Abz[var4].a_String), var2.a_Abz[var4].a_AS[0], var2.a_Abz[var4].a_AS[1], 20);
      }

      var1.translate(this.i_S, this.j_S);
      var1.translate(-this.i_S, -this.j_S);
      if (this.a_bj != null) {
        this.a_bj.a(var1);
      }

      var1.translate(this.i_S, this.j_S);
      var1.translate(-this.i_S, -this.j_S);

      for (int var6 = 0; this.a_bj != null && this.a_bj.a_Acz != null && var6 < this.a_bj.a_Acz.length; var6++) {
        if (this.a_bj.a_Acz[var6] != null && this.a_bj.a_Acz[var6].i_B == 12) {
          short[] var8 = this.a_bj.a_Acz[var6].a_AS(1);
          var1.setColor(65280);
          var1.drawRect(var8[0], var8[1], var8[2], var8[3]);
        }
      }

      var1.setColor(16777215);
      int var7 = this.j_I < this.l_I ? this.j_I : this.l_I;
      int var9 = this.k_I < this.m_I ? this.k_I : this.m_I;
      int var10 = this.l_I > this.j_I ? this.l_I : this.j_I;
      int var5 = this.m_I > this.k_I ? this.m_I : this.k_I;
      var1.drawRect(var7, var9, var10 - var7, var5 - var9);
      var1.setColor(0);
      vh.drawString(var1, "(" + var7 + "," + var9 + "," + (var10 - var7) + "," + (var5 - var9) + ")", var7 + 1, var5 + 1, 20);
      var1.setColor(16777215);
      vh.drawString(var1, "(" + var7 + "," + var9 + "," + (var10 - var7) + "," + (var5 - var9) + ")", var7, var5, 20);
      var1.translate(this.i_S, this.j_S);
      var1.translate(0, 0);
    }
  }

  protected final void c(int var1) {
    if (o.b_Z) {
      if (!z_Z) {
        return;
      }

      o.b_Z = false;
      z_Z = false;
      da.a_B = 0;
      da.a_da.a_cd.a_B = 0;
      o.f();
      Main.self.showCover();
    }

    if (this.a_B == 2) {
      if (this.b_B == 7) {
        this.a_df.a(var1);
      } else if (this.b_B == 4) {
        ab.a_V(var1);
      } else if (this.b_B == 6) {
        if (cr.a_bx.c() >= cr.a_bx.a_I(0) - 1) {
          cr.a_Z = false;
          da.a_da.a_cd.a_ci.a(5);
          cr.a_bx = null;
          da.a_da.a_cd.b_B = 1;
        }
      } else if (this.b_B == 5) {
        this.a_bn.a_Z(var1);
      } else {
        ae.a(var1);
      }
    } else {
      if (this.a_B == 3 && z_Z) {
        da.a_B = 0;
        this.a_Image = null;
        this.a_B = 0;
        o.f();
        Main.self.showCover();
      }
    }
  }

  public final void a(int var1, int var2) {
    if (this.a_B == 2 && da.a_da.a_cd.h_Z) {
      if (this.b_B == 1) {
        cm var3;
        if ((var3 = i.a(var1, var2)) != null) {
          if (da.a_da.a_cd.a_ci.a_Z) {
            if (var3.a_B == 2 && da.a_da.a_cd.l_Z) {
              da.a_da.a_cd.l_Z = false;
              da.a_da.a_cd.a_ci.a(5);
              da.a_da.a_cd.a_cl.a_AString = null;
            }
          } else if (var3.a_B == 1) {
            ae.a();
            as.o_B = 1;
            da.a_da.a_cd.a_as.i();
          } else if (var3.a_B == 2) {
            if (da.a_da.a_cd.l_Z) {
              da.a_da.a_cd.l_Z = false;
              da.a_da.a_cd.a_ci.a(5);
              da.a_da.a_cd.a_cl.a_AString = null;
            } else {
              df.a("/bin/smsmenu.bin");
              if (at.f_Z) {
                da.a_da.a_cd.e();
              }
            }
          } else if (var3.a_B == 4) {
            if (df.c_Z) {
              if (o.c_B == 0) {
                da.a_da.a("Hiện không thể dùng thú cưỡi", 10);
              } else if (o.c_B == 2) {
                da.a_da.a("Hiện không thể dùng thú cưỡi", 10);
              } else if (!da.a_da.a_cd.a_ca.a(o.a_Abl[0].a_AS(1), 1) && !da.a_da.a_cd.a_bj.a(o.a_Abl[0].a_AS(1), o.a_Abl[0], 1)) {
                da.a_da.a_cd.h_Z = false;
                da.a_da.a_cd.s_Z = true;
                o.a_Abl[0].j_Z = false;
                da.a_da.a_cd.h_B = 1;
              }
            } else {
              da.a_da.a_cd.a_AString = new String[]{"Chưa mở chức năng cưỡi sủng vật!"};
              da.a_da.a_cd.d_B = 0;
              da.a_da.a_cd.b_B = 3;
            }
          } else if (var3.a_B == 5) {
            ae.b();
          }
        }
      } else if (this.b_B == 4) {
        ab.a_V(var1, var2);
      } else if (this.b_B == 5) {
        this.a_bn.b_V(var1, var2);
      } else if (this.b_B == 7) {
        this.a_df.a(var1, var2);
      } else if (this.b_B == 9) {
        int var5 = at.a_AS[1] / 2 - 5 * (cg.b_S + 1) + 9 * (cg.b_S + 1) - 30;
        int var4 = at.a_AS[0] - 80;
        if (ab.a(var1, var2, 40, var5, 30, 30)) {
          ae.a(b.a_S);
        } else if (ab.a(var1, var2, var4 + 40 - 30, var5, 30, 30)) {
          ae.a(b.b_S);
        }
      } else if (this.b_B == 8) {
        int var7 = (at.a_AS[1] - 9 * (cg.b_S + 1)) / 2;
        int var6 = (at.a_AS[0] - 160) / 2;
        if (ab.a(var1, var2, var6, var7 + 9 * (cg.b_S + 1) - 30, 30, 30)) {
          ae.a(b.a_S);
        } else if (ab.a(var1, var2, var6 + 160 - 30, var7 + 9 * (cg.b_S + 1) - 30, 30, 30)) {
          ae.a(b.b_S);
        }
      }
    }

    this.j_I = var1 + this.i_S - b_I;
    this.k_I = var2 + this.j_S - c_I;
    this.l_I = this.j_I;
    this.m_I = this.k_I;
  }

  public final void b(int var1, int var2) {
    this.l_I = var1 + this.i_S - b_I;
    this.m_I = var2 + this.j_S - c_I;
    var1 = this.j_I < this.l_I ? this.j_I : this.l_I;
    var2 = this.k_I < this.m_I ? this.k_I : this.m_I;
    int var3 = this.l_I > this.j_I ? this.l_I : this.j_I;
    int var4 = this.m_I > this.k_I ? this.m_I : this.k_I;
    if (at.e_Z) {
      System.out.println("(" + var1 + "," + var2 + "," + (var3 - var1) + "," + (var4 - var2) + ")");
    }
  }

  public final void c(int var1, int var2) {
    this.l_I = var1 + this.i_S - b_I;
    this.m_I = var2 + this.j_S - c_I;
  }

  public final void d() {
    if (at.f_Z) {
      this.b();
    } else {
      m();
    }
  }

  public final void a(String[] var1) {
    this.d_AString = var1;
    this.D_Z = true;
    this.k_S = 0;
  }

  public final void a(int var1, int var2, int var3, int var4, int var5) {
    if (this.a_cl == null) {
      this.a_cl = new cl();
    }

    this.a_cl.a(var1, var2, var3, var4, var5);
    this.l_Z = true;
  }

  public final void e() {
    if (at.e_Z) {
      System.out.println("Xóa tài nguyên clearResource()");
    }

    if (o.a_Abl != null && o.a_Abl[0] != null) {
      o.a_Abl[0].a_I = -1;
      o.a_Abl[0].h_Z = false;
    }

    if (this.a_bj != null) {
      if (this.a_bj.a_Acz != null) {
        this.a_bj.a_Acz = null;
      }

      this.a_bj = null;
    }

    this.a_ca = null;
    this.a_bn = null;
    this.a_cf = null;
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      k.a_V();
    }

    try {
      Thread.sleep(500L);
    } catch (InterruptedException var2) {
      var2.printStackTrace();
    }

    if (at.e_Z) {
      System.out.println("Giải phóng tài nguyên xong");
    }
  }

  public final void a(String[] var1, int var2) {
    if (this.a_cf == null) {
      this.a_cf = new cf();
    }

    Vector var3 = new Vector();
    String[] var4 = null;

    for (int var5 = var2; var5 < var1.length; var5++) {
      if (var1[var5].indexOf("&") != -1) {
        var1[var5] = k.a(var1[var5], "&", o.c_AString[0]);
      }

      var3.addElement(var1[var5]);
    }

    if (var3.size() > 0) {
      var4 = new String[var3.size()];
      var3.copyInto(var4);
    }

    this.a_cf.a_bl = this.a_ci.a_bz.a_bl;
    this.a_cf.a_AString = var4;
    this.a_cf.a_B = 0;
    if (this.a_ci.a_bz != null && this.a_ci.a_bz.a_bl != null) {
      this.a_ci.a_bz.a_bl.f_V();
      if (this.a_ci.a_bz.a_bl.g_B <= 2 && (this.a_ci.a_bz.a_bl.a_B != 1 || this.a_ci.a_bz.a_bl.a_bx.b() == 4 || this.a_ci.a_bz.a_bl.a_bx.b() == 8)) {
        if (at.e_Z) {
          System.out.println("NPC quay hướng");
        }

        this.a_ci.a_bz.a_bl.h_B = this.a_ci.a_bz.a_bl.d_B;
        var2 = o.a_Abl[0].e_S - this.a_ci.a_bz.a_bl.e_S;
        int var7 = o.a_Abl[0].d_S - this.a_ci.a_bz.a_bl.d_S;
        if (Math.abs(var2) > Math.abs(var7)) {
          if (var2 < 0) {
            this.a_ci.a_bz.a_bl.a_V(1);
          } else {
            this.a_ci.a_bz.a_bl.a_V(0);
          }
        } else if (var7 < 0) {
          this.a_ci.a_bz.a_bl.a_V(2);
        } else {
          this.a_ci.a_bz.a_bl.a_V(3);
        }

        this.a_ci.a_bz.a_bl.c_V();
      }
    }

    this.a_cf.e();
    this.a_cf.a_Z = true;
    this.b_B = 2;
  }

  public final bl b_bl(int var1) {
    bl var2 = null;

    for (int var3 = 0; this.a_bj.a_Acz != null && var3 < this.a_bj.a_Acz.length; var3++) {
      if (this.a_bj.a_Acz[var3] != null && this.a_bj.a_Acz[var3].i_B == 1 && ((bl)this.a_bj.a_Acz[var3]).h_S == var1) {
        var2 = (bl)this.a_bj.a_Acz[var3];
        break;
      }
    }

    return var2;
  }

  public static void a(bl var0, int var1, int var2, int var3, int var4, int var5) {
    if (var0 != null) {
      if (var2 > 0) {
        var0.c_B = (byte)var2;
      }

      switch (var1) {
        case 0:
          var0.a(var0.d_S, var0.e_S + var3, var1);
          return;
        case 1:
          var0.a(var0.d_S, var0.e_S - var3, var1);
          return;
        case 2:
          var0.a(var0.d_S - var3, var0.e_S, var1);
          return;
        case 3:
          var0.a(var0.d_S + var3, var0.e_S, var1);
          return;
        default:
          var0.a(var3, var4, var5);
      }
    }
  }

  public final void a(String var1, int var2, int var3, int var4) {
    if (this.a_cr == null) {
      this.a_cr = new cr();
    }

    int var10002 = var2 + b_I - this.i_S;
    int var9 = var3 + c_I - this.j_S;
    var3 = var10002;
    cr var6 = this.a_cr;
    cr.a_String = var1;
    cr.a_S = (short)var3;
    cr.b_S = (short)var9;
    cr.a_I = var4;
    cr.a_Z = true;
    (cr.a_bx = new bx()).a(cr.a_String);
    cr.a_bx.a(at.a_AS[0] / 2, at.a_AS[1] / 2);
    this.b_B = 6;
  }

  public final void d(int var1, int var2) {
    this.E_Z = true;
    this.o_B = (byte)var1;
    this.m_S = (short)var2;
  }

  public static boolean a_Z(int var0) {
    return cg.a_Z(o.a_Abl[0].a_AS, var0);
  }

  public final void f() {
    String var1 = cg.a_String("/bin/story.bin");
    this.e_String = cg.a(var1, "end" + this.e_B + "Story:", "end" + this.e_B + "StoryEnd");
    this.c_AString = cg.a(this.e_String, "\r\n", at.a_AS[0] - 10);
    this.e_String = null;
    this.d_I = at.a_AS[1] + 5;
    this.e_I = at.a_AS[1];
  }

  public final void g() {
    for (int var1 = 0; this.a_bj != null && this.a_bj.a_Acz != null && var1 < this.a_bj.a_Acz.length; var1++) {
      if (this.a_bj.a_Acz[var1] != null
        && this.a_bj.a_Acz[var1].i_B == 1
        && ((bl)this.a_bj.a_Acz[var1]).g_B == 2
        && !cg.a_Z(o.b_AB, ((bl)this.a_bj.a_Acz[var1]).h_S)) {
        ((bl)this.a_bj.a_Acz[var1]).g_V();

        try {
          Thread.sleep(50L);
        } catch (InterruptedException var3) {
          var3.printStackTrace();
        }
      }
    }
  }

  public final void h() {
    if (at.f_Z && this.b_B != 5) {
      this.e();
    }

    try {
      Thread.sleep(100L);
    } catch (InterruptedException var2) {
      var2.printStackTrace();
    }

    this.a_as = new as();
    this.a_as.i();
    if (this.a_bn == null && this.a_df == null) {
      this.b_B = 4;
    }
  }

  public final void i() {
    as.a_bl = o.a_Abl[0];
    as.o_B = 8;
    as.z_B = 1;
    this.a_as.d();
  }

  public final void a(String var1, int var2, int var3) {
    as.a_AS = null;
    if (at.f_Z && this.b_B != 5) {
      this.e();
    }

    try {
      Thread.sleep(100L);
    } catch (InterruptedException var5) {
      var5.printStackTrace();
    }

    as.a_bl = o.a_Abl[0];
    this.a_as = new as();
    da.a_da.a_cd.b_B = 4;
    if (var1.equals("技能学习")) {
      as.c_I = var3;
      as.y_B = 2;
      as.b(var2);
      as.o_B = 14;
      this.a_as.i();
    } else if (var1.equals("买道具")) {
      as.o_B = 15;
      as.y_B = 0;
      o.d();
      as.b(var2);
      this.a_as.i();
    } else {
      if (var1.equals("买装备")) {
        as.o_B = 15;
        as.y_B = 1;
        ck.a_V();
        as.b(var2);
        this.a_as.i();
      }
    }
  }

  public final void b(String[] var1, int var2) {
    short[] var3 = new short[var1.length - var2];
    short[] var4 = new short[var1.length - var2];
    short[] var5 = new short[var1.length - var2];

    for (int var7 = 0; var7 < var3.length; var7++) {
      short[] var6 = cg.b_AS(var1[var7 + var2], "|");
      var3[var7] = var6[0];
      var4[var7] = var6[1];
      var5[var7] = var6[2];
    }

    this.a(var3, var4, var5);
  }

  private void a(short[] var1, short[] var2, short[] var3) {
    cd var4 = this;
    if (this.a_bj != null) {
      for (int var5 = 0; var4.a_bj.a_Acz != null && var5 < var4.a_bj.a_Acz.length; var5++) {
        if (var4.a_bj.a_Acz[var5] != null && var4.a_bj.a_Acz[var5].i_B == 1 && ((bl)var4.a_bj.a_Acz[var5]).g_B == 2) {
          ((bl)var4.a_bj.a_Acz[var5]).f_V();
        }
      }
    }

    this.f_Z = true;
    this.a_AS = var1;
    this.b_AS = var2;
    this.c_AS = var3;
    this.g_B = 5;
    this.x_Z = true;
  }

  public static void j() {
    if (Main.music != null) {
      Main.music.b();
      Main.music.c();
      Main.music = null;
    }
  }

  public final void d(int var1) {
    o.c_AS = cg.a_AS(o.c_AS, var1);
    bl var2 = null;

    for (int var3 = 0; this.a_bj != null && this.a_bj.a_Acz != null && var3 < this.a_bj.a_Acz.length; var3++) {
      if (this.a_bj.a_Acz[var3] != null && this.a_bj.a_Acz[var3].h_S == var1) {
        var2 = (bl)this.a_bj.a_Acz[var3];
      }
    }

    if (var2 != null) {
      this.d_bl = var2;
      this.l_B = 10;
      this.y_Z = true;
      this.g_Z = false;
    } else {
      this.a_ci.a(0);
    }
  }

  public final void k() {
    if (this.a_cf != null) {
      this.a_cf.a_V();
      this.a_cf = null;
    }

    this.b_bl = null;
    ae.a_bl = null;
    this.c_Z = false;
    this.a_bl = null;

    try {
      Thread.sleep(20L);
    } catch (InterruptedException var2) {
      var2.printStackTrace();
    }
  }

  public final void l() {
    this.a_ci.c_Z = false;
    this.q_Z = false;
    this.r_Z = false;

    for (int var1 = 0; var1 < o.a_Abl.length; var1++) {
      if (o.a_Abl[var1].a_AI[3] <= 0) {
        o.a_Abl[var1].a_AI[3] = o.a_Abl[var1].a_AI[10];
        o.a_Abl[var1].a_AI[5] = o.a_Abl[var1].a_AI[11];
      }
    }

    for (int var5 = 0; o.a_Acv != null && var5 < o.a_Acv.length; var5++) {
      o.a_Acv[var5].b_Z = false;
      if (o.a_Acv[var5].a_AI[1] <= 0) {
        o.a_Acv[var5].a_AI[1] = 1;
        if (o.a_Acv[var5].a_Z) {
          o.a_Acv[var5].a_Z = false;
        }
      }
    }

    if (at.a_String.equals("N73") || at.a_String.equals("N7610") || at.a_String.equals("N5800") || at.a_String.equals("E62") || at.a_String.equals("W958C")) {
      this.a_B = 0;
      j();
      this.a_bn.d_V();
      this.a_bn = null;
      this.a_df = null;

      try {
        Thread.sleep(400L);
      } catch (InterruptedException var4) {
        var4.printStackTrace();
      }

      this.a_B = 2;
      m();
      this.b_B = 1;
      da.a_da.a_cd.g();
    } else if (at.f_Z) {
      this.a_B = 0;
      j();
      this.a_bn.d_V();
      this.a_bn = null;
      this.a_df = null;
      k.a_V();

      try {
        Thread.sleep(400L);
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }

      this.d();
    } else {
      this.a_B = 0;
      this.a_bn.d_V();
      this.a_bn = null;
      j();
      this.a_df = null;
      k.a_V();

      try {
        Thread.sleep(400L);
      } catch (InterruptedException var3) {
        var3.printStackTrace();
      }

      this.a_B = 2;
      m();
      this.b_B = 1;
    }
  }

  public static void a(cv[] var0) {
    if (var0 != null) {
      for (int var1 = 0; var1 < var0.length; var1++) {
        if (var0[var1].a_Z && var0[var1].a_AI[13] >= var0[var1].a_AI[14]) {
          o.a_V(var0[var1]);
          var0[var1].a_AI[0]++;
          var0[var1].a_AI[15] = var0[var1].a_AI[14];
          int var2 = var0[var1].a_AI[0] + 1;
          var0[var1].a_AI[14] = var2 * var2 * var2 / 2 + var2 * var2;
          var0[var1].a_AI[13] = 0;
          var0[var1].a_AI[1] = var0[var1].a_AI[2];
          var0[var1].a_AI[4] = var0[var1].a_AI[5];
        }
      }
    }
  }

  public final void a(bl var1) {
    if (var1.a_AI != null && var1.a_AI[1] >= var1.a_AI[2]) {
      var1.a_AI[0]++;
      var1.a_AI[15] = var1.a_AI[2];
      int var2 = var1.a_AI[0] + 1;
      var1.a_AI[2] = var2 * var2 * var2 / 2 + var2 * var2;
      var1.a_AI[1] = 0;
      o.d(var1);
      var1.a_AI[3] = var1.a_AI[10];
      var1.a_AI[5] = var1.a_AI[11];
      this.n_Z = true;
    }
  }

  public static void m() {
    if (o.c_String != null && !o.c_String.equals("")) {
      if (Main.music != null) {
        if (!Main.music.a_String.equals("/music/" + o.c_String + ".mid")) {
          Main.music.b();
          Main.music.c();
          Main.music.a("/music/" + o.c_String + ".mid");
          Main.music.b(o.g_B);
          Main.music.a();
          return;
        }

        Main.music.b(o.g_B);
        Main.music.a();
        return;
      }

      (Main.music = new bv()).a("/music/" + o.c_String + ".mid");
      Main.music.b(o.g_B);
      Main.music.a();
    }
  }

  private void e(Graphics var1) {
    if (!this.B_Z && this.c_AString != null) {
      for (int var4 = 0; this.c_AString != null && var4 < this.c_AString.length; var4++) {
        cg.a(var1, this.c_AString[var4], at.a_AS[0] / 2, this.d_I + cg.b_S * var4, 0, 16777215, 33);
      }

      if (da.a_da.a_S % 2 == 0) {
        if (this.d_I + cg.b_S * this.c_AString.length <= -10) {
          this.B_Z = true;
          return;
        }

        this.d_I -= 3;
        if (at.a_String.equals("N73")) {
          this.d_I -= 6;
          return;
        }
      }
    } else {
      var1.setColor(0);
      var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
      int var10001 = at.a_AS[0] / 2;
      int var3 = this.e_I;
      int var2 = var10001;
      var1.setColor(16753731);
      vh.drawString(var1, "Đội ngũ phát triển", var2, var3, 33);
      var1.setColor(16776960);
      vh.drawString(var1, "Thiết kế", var2, var3 + (cg.b_S << 1), 33);
      var1.setColor(16777215);
      vh.drawString(var1, "softsai", var2, var3 + cg.b_S * 3, 33);
      var1.setColor(16776960);
      vh.drawString(var1, "Mỹ thuật", var2, var3 + cg.b_S * 5, 33);
      var1.setColor(16777215);
      vh.drawString(var1, "deapho", var2, var3 + cg.b_S * 6, 33);
      var1.setColor(16776960);
      vh.drawString(var1, "Lập trình", var2, var3 + (cg.b_S << 3), 33);
      var1.setColor(16777215);
      vh.drawString(var1, "alter", var2, var3 + cg.b_S * 9, 33);
      var1.setColor(16776960);
      vh.drawString(var1, "Kiểm thử", var2, var3 + cg.b_S * 11, 33);
      var1.setColor(16777215);
      vh.drawString(var1, "softsai", var2, var3 + cg.b_S * 12, 33);
      var1.setColor(16776960);
      vh.drawString(var1, "Công ty TNHH Công nghệ Thông tin Tỉnh Trung Nguyệt", var2, var3 + cg.b_S * 19, 33);
      vh.drawString(var1, "2009.6", var2, var3 + cg.b_S * 20, 33);
      if (da.a_da.a_S % 2 == 0) {
        if (this.e_I + cg.b_S * 20 > at.a_AS[1] / 2) {
          this.e_I -= 3;
          if (at.a_String.equals("D608")) {
            this.d_I -= 5;
            return;
          }
        } else {
          z_Z = true;
        }
      }
    }
  }

  private void o() {
    this.a_AAI = null;
    this.b_AAI = null;
    String[] var3 = cg.a_AString(cg.a_String("/bin/sn.bin"), "tablename", "tableend", null, "\t");

    for (int var4 = 0; o.a_AString != null && var4 < o.a_AString.length; var4++) {
      String[] var2 = cg.b_AString(o.b_AString[var4], ",");
      String[][] var1;
      if ((var1 = cg.a_AAString(o.e_String, var3[cg.a_I(var2[0]) - 1] + ":", "End", null, ",")) != null) {
        int[] var5 = null;
        int[] var6 = null;

        for (int var7 = 0; var1 != null && var7 < var1[0].length; var7++) {
          var5 = cg.a_AI(var5, cg.a_I(var1[0][var7]));
          var6 = cg.a_AI(var6, cg.a_I(var1[cg.a_I(var2[1]) - 1][var7]));
        }

        this.a_AAI = cg.a(this.a_AAI, var5);
        this.b_AAI = cg.a(this.b_AAI, var6);
      }
    }
  }

  private void p() {
    if (o.a_AString != null) {
      String[] var1 = cg.a_AString(cg.a_String("/bin/sn.bin"), "tablename", "tableend", null, "\t");

      for (int var2 = 0; var2 < o.a_AString.length; var2++) {
        int[] var3 = null;
        int[] var4 = null;
        int[] var5 = cg.b_AI(o.b_AString[var2], ",");
        String[][] var6;
        if ((var6 = cg.a_AAString(o.e_String, var1[var5[0] - 1] + ":", "End", null, ",")) != null) {
          for (int var7 = 0; var6 != null && var7 < var6[0].length; var7++) {
            var3 = cg.a_AI(var3, cg.a_I(var6[0][var7]));
            var4 = cg.a_AI(var4, cg.a_I(var6[var5[1] - 1][var7]));
          }
        }

        this.a_ca.a(o.a_AString[var2] + ".gf", var3, var4);
      }
    }
  }

  public final void n() {
    this.a_Abi = new bi[100];

    for (int var1 = 0; var1 < 100; var1++) {
      this.a_Abi[var1] = new bi();
    }

    this.m_Z = true;
    this.a_ci.a(0);
  }
}
