import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class bj {
  static bj a_bj;
  private short a_S;
  private short b_S;
  private short c_S;
  private short d_S;
  private c[] a_Ac;
  public cz[] a_Acz;
  private short[][] a_AAS;
  private short[][] b_AAS;
  private short[][] c_AAS;
  public cz[] b_Acz;
  private cz[] c_Acz;
  private String a_String;
  private String b_String;
  private String[] a_AString;
  private short[][] d_AAS;
  private int a_I;
  private int[] a_AI;
  private int[][] a_AAI;
  private int[][] b_AAI;
  private int[][] c_AAI;
  private short[][] e_AAS;
  private short[][] f_AAS;
  private short[][] g_AAS;
  private boolean[] a_AZ;
  private boolean[] b_AZ;
  private boolean[] c_AZ;

  public bj() {
    a_bj = this;
  }

  public final void a(cz var1) {
    if (this.a_Acz == null) {
      this.a_Acz = new cz[1];
    } else {
      cz[] var2 = new cz[this.a_Acz.length + 1];
      System.arraycopy(this.a_Acz, 0, var2, 0, this.a_Acz.length);
      this.a_Acz = var2;
    }

    this.a_Acz[this.a_Acz.length - 1] = var1;
  }

  public final boolean a(short[] var1, cz var2, int var3) {
    return this.a(var1, new cz[]{var2}, 1);
  }

  public final boolean a(short[] var1, cz[] var2, int var3) {
    if (var1 == null) {
      return false;
    } else {
      for (int var4 = 0; this.a_Ac != null && this.a_AAS != null && var4 < this.a_AAS.length; var4++) {
        short[] var6;
        if (this.a_AAS[var4] != null
          && this.a_Z(var4)
          && this.a_Ac[this.a_AAS[var4][0]].a_AS() != null
          && (var6 = this.a_Ac[this.a_AAS[var4][0]].a(this.a_AAS[var4][1], this.a_AAS[var4][2], var3)) != null) {
          for (byte var5 = 0; var5 < var1.length; var5 += 4) {
            for (byte var7 = 0; var7 < var6.length; var7 += 4) {
              if (cg.a(var1[var5], var1[var5 + 1], var1[var5 + 2], var1[var5 + 3], var6[var7], var6[var7 + 1], var6[var7 + 2], var6[var7 + 3])) {
                return true;
              }
            }
          }
        }
      }

      for (int var8 = 0; this.a_Acz != null && var8 < this.a_Acz.length; var8++) {
        if (this.a_Acz[var8] != null && this.a_Acz[var8].j_Z && this.a_Acz[var8].k_Z) {
          boolean var9 = true;

          for (int var11 = 0; var2 != null && var11 < var2.length; var11++) {
            if (var2[var11] == this.a_Acz[var8]) {
              var9 = false;
              break;
            }
          }

          short[] var10;
          if (var9 && this.a_Acz[var8].a_AS() != null && (var10 = this.a_Acz[var8].a_AS(var3)) != null) {
            for (byte var13 = 0; var13 < var1.length - 3; var13 += 4) {
              for (byte var12 = 0; var12 < var10.length - 3; var12 += 4) {
                if (cg.a(var1[var13], var1[var13 + 1], var1[var13 + 2], var1[var13 + 3], var10[var12], var10[var12 + 1], var10[var12 + 2], var10[var12 + 3])) {
                  return true;
                }
              }
            }
          }
        }
      }

      return false;
    }
  }

  public final void a() {
    this.a_AAS = null;
    this.b_AAS = null;
    this.c_AAS = null;
    this.a_Ac = null;
  }

  public final void b() {
    if (this.c_Acz != null) {
      this.a(this.c_Acz);

      for (int var1 = this.c_Acz.length - 1; var1 >= 0; var1--) {
        if (this.c_Acz[var1] != null) {
          if (var1 < this.c_Acz.length - 1) {
            cz[] var2 = new cz[var1 + 1];
            System.arraycopy(this.c_Acz, 0, var2, 0, var2.length);
            this.c_Acz = var2;
            return;
          }

          return;
        }
      }
    }

    if (this.a_Acz != null) {
      this.a(this.a_Acz);

      for (int var3 = this.a_Acz.length - 1; var3 >= 0; var3--) {
        if (this.a_Acz[var3] != null) {
          if (var3 < this.a_Acz.length - 1) {
            cz[] var4 = new cz[var3 + 1];
            System.arraycopy(this.a_Acz, 0, var4, 0, var4.length);
            this.a_Acz = var4;
            return;
          }

          return;
        }
      }
    }

    if (this.b_Acz != null) {
      this.a(this.b_Acz);

      for (int var5 = this.b_Acz.length - 1; var5 >= 0; var5--) {
        if (this.b_Acz[var5] != null) {
          if (var5 < this.b_Acz.length - 1) {
            cz[] var6 = new cz[var5 + 1];
            System.arraycopy(this.b_Acz, 0, var6, 0, var6.length);
            this.b_Acz = var6;
            return;
          }

          return;
        }
      }
    }
  }

  public final void a_V(String var1, String var2, String var3) {
    boolean var12 = false;
    byte var13 = -1;
    bj var14 = null;
    var14 = null;
    var14 = null;
    var14 = null;
    if (this.a_Z(var1, var2, var3)) {
      byte var7 = 0;
      byte var6 = -1;
      Object var5 = null;
      Object var23 = null;
      Object var22 = null;
      Object var20 = null;
      var14 = this;
      if (this.a_AString != null && this.a_AString.length > 0) {
        this.a_Ac = new c[this.a_AString.length];

        for (int var8 = 0; var14.a_AString != null && var8 < var14.a_AString.length; var8++) {
          if (var14.a_AString[var8] != null && !var14.a_AString[var8].equals("")) {
            Object var9 = var14.a_AString[var8];
            if (var14.a_AString[var8].toLowerCase().endsWith(".gf") || var14.a_AString[var8].toLowerCase().endsWith(".png")) {
              Image var24 = null;
              if (var20 != null) {
                for (int var11 = 0; var20 != null && var11 < ((Object[])var20).length; var11++) {
                  if (((Object[])var20)[var11].equals(var14.a_AString[var8])) {
                    var9 = ((Object[])var22)[var11];
                    break;
                  }
                }
              } else if (var22 != null && var8 < ((Object[])var22).length && ((Object[])var22)[var8] != null) {
                var9 = ((Object[])var22)[var8];
              }

              if (var5 == null && var6 < 0) {
                var24 = cc.a_Image(var14.a_String + var14.a_AString[var8], var7);
              } else {
                byte[] var25;
                if ((var25 = cc.a_AB(var14.a_String + var9, var7)) != null) {
                  if (var5 != null) {
                    var25 = be.a(var25, (int[])var23, (int[])var5);
                  }

                  if (var6 >= 0) {
                    var25 = be.a(var25, var6);
                  }

                  var24 = cg.a(var25);
                }
              }

              var14.a_Ac[var8] = new c(var24, var14.d_AAS[var8]);
            } else if (var14.a_AString[var8].toLowerCase().endsWith(".av")) {
              bx var10;
              (var10 = new bx()).a(var14.a_String + var14.a_AString[var8], var14.b_String, (String[])var20, (String[])var22, (int[])var23, (int[])var5, var7);
              var14.a_Ac[var8] = new c(var10, var14.d_AAS[var8]);
            }
          }
        }
      }

      var14.a_AString = null;
      var14.d_AAS = null;
    }
  }

  public final void a(String var1, String var2, String var3, String[] var4, String[] var5, String[] var6, int[][] var7, int[][] var8, int var9, int var10) {
    if (this.a_Z(var1, var2, var3)) {
      byte var24 = -1;
      byte var23 = -1;
      int[][] var22 = var8;
      int[][] var21 = var7;
      var4 = var6;
      Object var19 = null;
      Object var18 = null;
      bj var17 = this;
      if (this.a_AString != null && this.a_AString.length > 0) {
        this.a_Ac = new c[this.a_AString.length];

        for (int var25 = 0; var17.a_AString != null && var25 < var17.a_AString.length; var25++) {
          if (var17.a_AString[var25] != null && !var17.a_AString[var25].equals("")) {
            Object var26 = var17.a_AString[var25];
            if (!var17.a_AString[var25].toLowerCase().endsWith(".gf") && !var17.a_AString[var25].toLowerCase().endsWith(".png")) {
              if (var17.a_AString[var25].toLowerCase().endsWith(".av")) {
                bx var27 = new bx();
                if (var4 != null && (var22 != null || var23 >= 0)) {
                  co var30;
                  (var30 = new co()).a_Z(var17.a_String + var17.a_AString[var25]);
                  var30.a_String = var17.b_String;
                  var30.a((String[])var18, (String[])var19, var24);

                  for (int var32 = 0; var32 < var4.length; var32++) {
                    if (var32 < var21.length && var32 < var22.length) {
                      var30.a(var4[var32], var21[var32], var22[var32]);
                    }
                  }

                  var30.a_V();
                  var27.a_co = var30;
                } else {
                  String var10001 = var17.a_String + var17.a_AString[var25];
                  String var31 = var17.b_String;
                  String var29 = var10001;
                  var27.a(var29, var31, (String[])var18, (String[])var19, null, null, var24);
                }

                var17.a_Ac[var25] = new c(var27, var17.d_AAS[var25]);
              }
            } else {
              Image var11 = null;
              if (var18 != null) {
                for (int var12 = 0; var18 != null && var12 < ((Object[])var18).length; var12++) {
                  if (((Object[])var18)[var12].equals(var17.a_AString[var25])) {
                    var26 = ((Object[])var19)[var12];
                    break;
                  }
                }
              } else if (var19 != null && var25 < ((Object[])var19).length && ((Object[])var19)[var25] != null) {
                var26 = ((Object[])var19)[var25];
              }

              if (var4 != null && (var22 != null || var23 >= 0)) {
                boolean var28 = false;

                for (int var13 = 0; var4 != null && var13 < var4.length; var13++) {
                  if (var26.equals(var4[var13]) && var21 != null && var22 != null && var13 < var21.length && var13 < var22.length) {
                    var28 = true;
                    byte[] var14;
                    if ((var14 = cc.a_AB(var17.a_String + var26, var24)) != null) {
                      if (var22 != null) {
                        var14 = be.a(var14, var21[var13], var22[var13]);
                      }

                      if (var23 >= 0) {
                        var14 = be.a(var14, var23);
                      }

                      var11 = cg.a(var14);
                    }
                  }
                }

                if (!var28) {
                  var11 = cc.a_Image(var17.a_String + var26, var24);
                }
              } else {
                var11 = cc.a_Image(var17.a_String + var26, var24);
              }

              var17.a_Ac[var25] = new c(var11, var17.d_AAS[var25]);
            }
          }
        }
      }

      var17.a_AString = null;
      var17.d_AAS = null;
    }
  }

  private boolean a_Z(String var1, String var2, String var3) {
    this.a_String = var2;
    if (var3 == null) {
      if (this.b_String == null) {
        int var4 = this.a_String.lastIndexOf(47);
        this.b_String = this.a_String.substring(0, var4 + 1);
      }
    } else if (var3.startsWith("/")) {
      this.b_String = var3;
    } else {
      int var30 = this.a_String.lastIndexOf(47);
      this.b_String = this.a_String.substring(0, var30 + 1) + var3;
    }

    if (!this.b_String.endsWith("/")) {
      this.b_String = this.b_String + "/";
    }

    this.a_AString = null;
    this.d_AAS = null;
    this.a_AAS = null;
    this.b_AAS = null;
    this.c_AAS = null;
    this.a_AZ = null;
    this.b_AZ = null;
    this.c_AZ = null;
    byte[] var7;
    if (at.j_Z) {
      var7 = cc.a_AB(var1, at.h_B);
    } else {
      var7 = cg.a_AB(var1);
    }

    if (var7 == null) {
      return false;
    } else {
      ByteArrayInputStream var8 = new ByteArrayInputStream(var7);
      DataInputStream var9 = new DataInputStream(var8);

      try {
        short var10 = var9.readShort();
        this.a_AString = new String[var10];

        for (int var21 = 0; var21 < var10; var21++) {
          this.a_AString[var21] = var9.readUTF();
        }

        short var22 = var9.readShort();
        this.d_AAS = new short[var22][];

        for (int var11 = 0; var11 < var22; var11++) {
          byte var31;
          if ((var31 = var9.readByte()) > 0) {
            this.d_AAS[var11] = new short[var31];

            for (int var5 = 0; var5 < var31; var5++) {
              this.d_AAS[var11][var5] = var9.readShort();
            }
          }
        }

        short var12;
        if ((var12 = var9.readShort()) > 0) {
          this.a_AAS = new short[var12][];

          for (int var32 = 0; var32 < var12; var32++) {
            this.a_AAS[var32] = new short[3];

            for (int var40 = 0; var40 < 3; var40++) {
              this.a_AAS[var32][var40] = var9.readShort();
            }
          }
        }

        short var33;
        if ((var33 = var9.readShort()) > 0) {
          this.b_AAS = new short[var33][];

          for (int var41 = 0; var41 < var33; var41++) {
            this.b_AAS[var41] = new short[3];

            for (int var13 = 0; var13 < 3; var13++) {
              this.b_AAS[var41][var13] = var9.readShort();
            }
          }
        }

        short var42;
        if ((var42 = var9.readShort()) > 0) {
          this.c_AAS = new short[var42][];

          for (int var14 = 0; var14 < var42; var14++) {
            this.c_AAS[var14] = new short[3];

            for (int var23 = 0; var23 < 3; var23++) {
              this.c_AAS[var14][var23] = var9.readShort();
            }
          }
        }

        short var15;
        if ((var15 = var9.readShort()) > 0) {
          this.a_AAI = new int[var15][];

          for (int var24 = 0; var24 < var15; var24++) {
            var33 = var9.readShort();

            for (int var43 = 0; var43 < var33; var43++) {
              this.a_AAI[var24] = cg.a_AI(this.a_AAI[var24], var9.readInt());
            }
          }
        }

        short var25;
        if ((var25 = var9.readShort()) > 0) {
          this.b_AAI = new int[var25][];

          for (int var35 = 0; var35 < var25; var35++) {
            var42 = var9.readShort();

            for (int var16 = 0; var16 < var42; var16++) {
              this.b_AAI[var35] = cg.a_AI(this.b_AAI[var35], var9.readInt());
            }
          }
        }

        if ((var33 = var9.readShort()) > 0) {
          this.c_AAI = new int[var33][];

          for (int var45 = 0; var45 < var33; var45++) {
            short var17 = var9.readShort();

            for (int var26 = 0; var26 < var17; var26++) {
              this.c_AAI[var45] = cg.a_AI(this.c_AAI[var45], var9.readInt());
            }
          }
        }

        if ((var42 = var9.readShort()) > 0) {
          this.e_AAS = new short[var42][];

          for (int var18 = 0; var18 < var42; var18++) {
            short var27 = var9.readShort();

            for (int var37 = 0; var37 < var27; var37++) {
              this.e_AAS[var18] = cg.a_AS(this.e_AAS[var18], var9.readShort());
            }
          }
        }

        short var19;
        if ((var19 = var9.readShort()) > 0) {
          this.f_AAS = new short[var19][];

          for (int var28 = 0; var28 < var19; var28++) {
            var33 = var9.readShort();

            for (int var47 = 0; var47 < var33; var47++) {
              this.f_AAS[var28] = cg.a_AS(this.f_AAS[var28], var9.readShort());
            }
          }
        }

        short var29;
        if ((var29 = var9.readShort()) > 0) {
          this.g_AAS = new short[var29][];

          for (int var39 = 0; var39 < var29; var39++) {
            var42 = var9.readShort();

            for (int var20 = 0; var20 < var42; var20++) {
              this.g_AAS[var39] = cg.a_AS(this.g_AAS[var39], var9.readShort());
            }
          }
        }

        return true;
      } catch (Exception var6) {
        var6.printStackTrace();
        return false;
      }
    }
  }

  private boolean a_Z(int var1) {
    this.e();
    return this.a_AZ != null ? this.a_AZ[var1] : true;
  }

  public final void a(Graphics var1, int var2, int var3) {
    Graphics var10 = var1;
    bj var9 = this;
    this.e();
    this.a(this.a_Acz);
    this.a(this.c_Acz);
    var1.translate(-this.a_S + var2, -this.b_S + var3);

    for (int var5 = 0; var9.b_AAS != null && var5 < var9.b_AAS.length; var5++) {
      boolean var6;
      short[] var7;
      if ((var6 = var9.b_AZ[var5])
        && var9.a_Ac[var9.b_AAS[var5][0]] != null
        && (var7 = var9.a_Ac[var9.b_AAS[var5][0]].a_AS()) != null
        && cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.b_AAS[var5][1] + var7[0], var9.b_AAS[var5][2] + var7[1], var7[2], var7[3])) {
        var9.a_Ac[var9.b_AAS[var5][0]].a(var10, var9.b_AAS[var5][1], var9.b_AAS[var5][2]);
      }
    }

    for (int var12 = 0; var9.c_Acz != null && var12 < var9.c_Acz.length; var12++) {
      short[] var14;
      if (var9.c_Acz[var12] != null
        && var9.c_Acz[var12].j_Z
        && (var14 = var9.c_Acz[var12].a_AS()) != null
        && cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.c_Acz[var12].d_S + var14[0], var9.c_Acz[var12].e_S + var14[1], var14[2], var14[3])) {
        var9.c_Acz[var12].a(var10);
      }
    }

    int var13 = 0;
    if (var9.a_AAS == null) {
      for (int var15 = 0; var9.a_Acz != null && var15 < var9.a_Acz.length && var9.a_Acz[var15] != null; var15++) {
        short[] var19;
        if (var9.a_Acz[var15].j_Z
          && (var19 = var9.a_Acz[var15].a_AS()) != null
          && cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.a_Acz[var15].d_S + var19[0], var9.a_Acz[var15].e_S + var19[1], var19[2], var19[3])) {
          var9.a_Acz[var15].a(var10);
          var13 = var9.a_Acz.length;
        }
      }
    } else {
      short var16 = var9.a_AAS[0][2];

      for (int var20 = 0; var9.a_Acz != null && var20 < var9.a_Acz.length && var9.a_Acz[var20] != null; var20++) {
        if (var9.a_Acz[var20].j_Z) {
          short[] var8;
          if ((var8 = var9.a_Acz[var20].a_AS()) != null) {
            if (var9.a_Acz[var20].e_S > var16) {
              break;
            }

            if (cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.a_Acz[var20].d_S + var8[0], var9.a_Acz[var20].e_S + var8[1], var8[2], var8[3])) {
              var9.a_Acz[var20].a(var10);
            }

            var13 = var20 + 1;
          }
        } else {
          var13 = var20 + 1;
        }
      }
    }

    for (int var17 = 0; var9.a_AAS != null && var17 < var9.a_AAS.length; var17++) {
      if (var17 > 0 && var17 < var9.a_AAS.length) {
        for (int var21 = var13; var9.a_Acz != null && var21 < var9.a_Acz.length && var9.a_Acz[var21] != null; var21++) {
          if (var9.a_Acz[var21].j_Z) {
            short[] var24;
            if ((var24 = var9.a_Acz[var21].a_AS()) != null && var9.a_Acz[var21].e_S > var9.a_AAS[var17 - 1][2]) {
              if (var9.a_Acz[var21].e_S > var9.a_AAS[var17][2]) {
                break;
              }

              if (cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.a_Acz[var21].d_S + var24[0], var9.a_Acz[var21].e_S + var24[1], var24[2], var24[3])) {
                var9.a_Acz[var21].a(var10);
              }

              var13 = var21 + 1;
            }
          } else {
            var13 = var21 + 1;
          }
        }
      }

      short[] var22;
      if (var9.a_Z(var17)
        && var9.a_Ac[var9.a_AAS[var17][0]] != null
        && (var22 = var9.a_Ac[var9.a_AAS[var17][0]].a_AS()) != null
        && cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.a_AAS[var17][1] + var22[0], var9.a_AAS[var17][2] + var22[1], var22[2], var22[3])) {
        var9.a_Ac[var9.a_AAS[var17][0]].a(var10, var9.a_AAS[var17][1], var9.a_AAS[var17][2]);
      }
    }

    if (var9.a_AAS != null) {
      short var18 = var9.a_AAS[var9.a_AAS.length - 1][2];

      for (int var23 = var13; var9.a_Acz != null && var23 < var9.a_Acz.length && var9.a_Acz[var23] != null; var23++) {
        short[] var25;
        if (var9.a_Acz[var23].j_Z
          && (var25 = var9.a_Acz[var23].a_AS()) != null
          && var9.a_Acz[var23].e_S > var18
          && cg.a(var9.a_S, var9.b_S, var9.c_S, var9.d_S, var9.a_Acz[var23].d_S + var25[0], var9.a_Acz[var23].e_S + var25[1], var25[2], var25[3])) {
          var9.a_Acz[var23].a(var10);
        }
      }
    }

    var10.translate(var9.a_S - var2, var9.b_S - var3);
  }

  public final void a(Graphics var1) {
    for (int var2 = 0; this.a_AAS != null && var2 < this.a_AAS.length; var2++) {
      short[] var3;
      if (this.a_AAS[var2] != null
        && this.a_Z(var2)
        && this.a_Ac[this.a_AAS[var2][0]] != null
        && (var3 = this.a_Ac[this.a_AAS[var2][0]].a_AS()) != null
        && cg.a(this.a_S, this.b_S, this.c_S, this.d_S, this.a_AAS[var2][1] + var3[0], this.a_AAS[var2][2] + var3[1], var3[2], var3[3])) {
        this.a_Ac[this.a_AAS[var2][0]].b(var1, this.a_AAS[var2][1], this.a_AAS[var2][2]);
      }
    }

    for (int var4 = 0; this.a_Acz != null && var4 < this.a_Acz.length; var4++) {
      if (this.a_Acz[var4] != null && this.a_Acz[var4].j_Z) {
        this.a_Acz[var4].b(var1);
      }
    }

    for (int var5 = 0; this.b_Acz != null && var5 < this.b_Acz.length; var5++) {
      if (this.b_Acz[var5] != null && this.b_Acz[var5].j_Z) {
        this.b_Acz[var5].b(var1);
      }
    }
  }

  public final void b(Graphics var1, int var2, int var3) {
    this.a(this.b_Acz);
    var1.translate(-this.a_S + var2, -this.b_S + var3);

    for (int var4 = 0; this.b_Acz != null && var4 < this.b_Acz.length && this.b_Acz[var4] != null; var4++) {
      short[] var5;
      if (this.b_Acz[var4].j_Z
        && (var5 = this.b_Acz[var4].a_AS()) != null
        && cg.a(this.a_S, this.b_S, this.c_S, this.d_S, this.b_Acz[var4].d_S + var5[0], this.b_Acz[var4].e_S + var5[1], var5[2], var5[3])) {
        this.b_Acz[var4].a(var1);
      }
    }

    var1.translate(this.a_S - var2, this.b_S - var3);
  }

  public final void c(Graphics var1, int var2, int var3) {
    var1.translate(-this.a_S + var2, -this.b_S + var3);

    for (int var4 = 0; this.c_AAS != null && var4 < this.c_AAS.length; var4++) {
      boolean var5;
      short[] var6;
      if ((var5 = this.c_AZ[var4])
        && this.a_Ac[this.c_AAS[var4][0]] != null
        && (var6 = this.a_Ac[this.c_AAS[var4][0]].a_AS()) != null
        && cg.a(this.a_S, this.b_S, this.c_S, this.d_S, this.c_AAS[var4][1] + var6[0], this.c_AAS[var4][2] + var6[1], var6[2], var6[3])) {
        this.a_Ac[this.c_AAS[var4][0]].a(var1, this.c_AAS[var4][1], this.c_AAS[var4][2]);
      }
    }

    var1.translate(this.a_S - var2, this.b_S - var3);
  }

  public final void c() {
    for (int var1 = 0; this.a_Ac != null && var1 < this.a_Ac.length; var1++) {
      if (this.a_Ac[var1] != null) {
        this.a_Ac[var1].a_V();
      }
    }
  }

  public final void b(cz var1) {
    int var2 = -1;

    for (int var3 = 0; this.b_Acz != null && var3 < this.b_Acz.length; var3++) {
      if (this.b_Acz[var3] == var1) {
        var2 = var3;
        break;
      }
    }

    if (var2 >= 0) {
      this.b_Acz[var2] = null;
    }

    this.b();
  }

  public final void c(cz var1) {
    int var2 = -1;

    for (int var3 = 0; this.a_Acz != null && var3 < this.a_Acz.length; var3++) {
      if (this.a_Acz[var3] == var1) {
        var2 = var3;
        break;
      }
    }

    if (var2 >= 0) {
      this.a_Acz[var2] = null;
    }

    this.b();
  }

  public final void a_V(int var1) {
    this.a_I = var1;
  }

  public final void a(int[] var1) {
    this.a_AI = var1;
  }

  public final void a(int var1, int var2, int var3, int var4) {
    this.a_S = (short)var1;
    this.b_S = (short)var2;
    this.c_S = (short)var3;
    this.d_S = (short)var4;
  }

  public final void d() {
    this.a(this.a_Acz);
    this.a(this.b_Acz);
    this.a(this.c_Acz);
  }

  private void a(cz[] var1) {
    boolean var2 = false;
    var1 = var1;
    if (var1 != null) {
      int var3;
      for (int var5 = (var3 = var1.length) / 2; var5 != 0; var5 /= 2) {
        for (int var6 = var5; var6 < var3; var6++) {
          cz var9 = null;
          short var7;
          if (var1[var6] == null) {
            var7 = 32767;
          } else {
            var7 = (var9 = var1[var6]).e_S;
          }

          int var4 = var6 - var5;
          short var8;
          if (var1[var4] == null) {
            var8 = 32767;
          } else {
            var8 = var1[var4].e_S;
          }

          if (!var2) {
            while (var7 < var8 && var4 >= 0 && var4 < var3) {
              var1[var4 + var5] = var1[var4];
              if ((var4 -= var5) < 0 || var4 >= var3) {
                break;
              }

              if (var1[var4] == null) {
                var8 = 32767;
              } else {
                var8 = var1[var4].e_S;
              }
            }
          } else {
            while (var7 > var8 && var4 >= 0 && var4 < var3) {
              var1[var4 + var5] = var1[var4];
              if ((var4 -= var5) < 0 || var4 >= var3) {
                break;
              }

              if (var1[var4] == null) {
                var8 = 32767;
              } else {
                var8 = var1[var4].e_S;
              }
            }
          }

          var1[var4 + var5] = var9;
        }
      }
    }
  }

  private void e() {
    if (this.a_AZ == null && this.a_AAS != null) {
      this.a_AZ = new boolean[this.a_AAS.length];

      for (int var1 = 0; var1 < this.a_AZ.length; var1++) {
        this.a_AZ[var1] = true;
      }

      for (int var4 = 0; this.a_AAI != null && var4 < this.a_AAI.length; var4++) {
        for (int var2 = 1; var2 < this.a_AAI[var4].length; var2++) {
          if (this.a_AAI[var4][var2] > 0 && !cg.a_Z(this.a_AI, this.a_AAI[var4][var2])
            || this.a_AAI[var4][var2] < 0 && cg.a_Z(this.a_AI, this.a_AAI[var4][var2])) {
            this.a_AZ[this.a_AAI[var4][0]] = false;
            break;
          }
        }
      }

      for (int var5 = 0; this.e_AAS != null && var5 < this.e_AAS.length; var5++) {
        boolean var12 = false;

        for (int var3 = 1; var3 < this.e_AAS[var5].length; var3++) {
          if (this.e_AAS[var5][var3] == this.a_I) {
            var12 = true;
            break;
          }
        }

        if (!var12) {
          this.a_AZ[this.e_AAS[var5][0]] = false;
        }
      }

      this.a_AAI = null;
      this.e_AAS = null;
    }

    if (this.b_AZ == null && this.b_AAS != null) {
      this.b_AZ = new boolean[this.b_AAS.length];

      for (int var6 = 0; var6 < this.b_AZ.length; var6++) {
        this.b_AZ[var6] = true;
      }

      for (int var7 = 0; this.b_AAI != null && var7 < this.b_AAI.length; var7++) {
        for (int var13 = 1; var13 < this.b_AAI[var7].length; var13++) {
          if (this.b_AAI[var7][var13] > 0 && !cg.a_Z(this.a_AI, this.b_AAI[var7][var13])
            || this.b_AAI[var7][var13] < 0 && cg.a_Z(this.a_AI, this.b_AAI[var7][var13])) {
            this.b_AZ[this.b_AAI[var7][0]] = false;
            break;
          }
        }
      }

      for (int var8 = 0; this.f_AAS != null && var8 < this.f_AAS.length; var8++) {
        boolean var14 = false;

        for (int var17 = 1; var17 < this.f_AAS[var8].length; var17++) {
          if (this.f_AAS[var8][var17] == this.a_I) {
            var14 = true;
            break;
          }
        }

        if (!var14) {
          this.b_AZ[this.f_AAS[var8][0]] = false;
        }
      }

      this.b_AAI = null;
      this.f_AAS = null;
    }

    if (this.c_AZ == null && this.c_AAS != null) {
      this.c_AZ = new boolean[this.c_AAS.length];

      for (int var9 = 0; var9 < this.c_AZ.length; var9++) {
        this.c_AZ[var9] = true;
      }

      for (int var10 = 0; this.c_AAI != null && var10 < this.c_AAI.length; var10++) {
        for (int var15 = 1; var15 < this.c_AAI[var10].length; var15++) {
          if (this.c_AAI[var10][var15] > 0 && !cg.a_Z(this.a_AI, this.c_AAI[var10][var15])
            || this.c_AAI[var10][var15] < 0 && cg.a_Z(this.a_AI, this.c_AAI[var10][var15])) {
            this.c_AZ[this.c_AAI[var10][0]] = false;
            break;
          }
        }
      }

      for (int var11 = 0; this.g_AAS != null && var11 < this.g_AAS.length; var11++) {
        boolean var16 = false;

        for (int var18 = 1; var18 < this.g_AAS[var11].length; var18++) {
          if (this.g_AAS[var11][var18] == this.a_I) {
            var16 = true;
            break;
          }
        }

        if (!var16) {
          this.c_AZ[this.g_AAS[var11][0]] = false;
        }
      }

      this.c_AAI = null;
      this.g_AAS = null;
    }
  }
}
