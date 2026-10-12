import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cf {
  private dc a_dc;
  public boolean a_Z;
  public String[] a_AString;
  public byte a_B;
  private String a_String;
  public String[] b_AString;
  public byte b_B;
  short a_S;
  public byte c_B;
  public static byte d_B = 0;
  public static byte e_B = 1;
  private short c_S;
  private byte f_B;
  private short d_S;
  private byte g_B;
  private byte h_B;
  private byte i_B;
  private byte j_B;
  private byte k_B;
  public bl a_bl;
  private byte l_B = 16;
  private short e_S;
  private static byte m_B;
  static short b_S = 3;
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private Image d_Image;
  private Image e_Image;
  private int a_I;
  private boolean b_Z;
  private bx a_bx;
  private int b_I;
  private int c_I;
  private int d_I;
  private int e_I;
  private boolean c_Z;
  private int f_I = 20;
  private boolean d_Z;

  public cf() {
    byte var1 = 2;
    this.l_B = (byte)vh.getHeight(cg.a_Font);
    if (this.l_B < 16) {
      this.l_B = 16;
    }

    this.f_B = 3;
    this.d_S = 4;
    this.b_Image = cc.a_Image("/pic/arrow.gf", 2);
    if (this.a_Image == null) {
      cc.a("/pic/btpc.gf");
    }

    if (this.d_Image == null) {
      cc.a_Image("/pic/jiao1.gf", 2);
    }

    if (this.e_Image == null) {
      cc.a_Image("/pic/jiao2.gf", 2);
    }
  }

  public final void a_V() {
    k.c();
    this.a_bx = null;
    this.a_Image = null;
    this.a_Z = false;
    this.a_B = 0;
    this.b_B = 0;
    this.c_B = 0;
    m_B = 0;
    this.b_Image = null;
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      k.a_V();
    }

    da.a_da.a_cd.a_ci.a(3);
  }

  public final void b() {
    this.i_B = (byte)(this.i_B - this.f_B);
    if (this.i_B < 0) {
      this.i_B = 0;
    }
  }

  public final void c() {
    if ((this.j_B >= this.i_B + this.h_B - 1 || this.j_B >= this.a_dc.b_AString.length - 1) && this.k_B >= this.a_dc.b_AString[this.j_B].length() - 1) {
      if (this.i_B + this.h_B - 1 < this.g_B) {
        this.i_B = (byte)(this.i_B + this.f_B);
      }

      if (this.i_B > this.g_B) {
        this.i_B = this.g_B;
      }
    } else {
      this.j_B = (byte)(this.i_B + this.h_B - 1);
      if (this.j_B > this.a_dc.b_AString.length - 1) {
        this.j_B = (byte)(this.a_dc.b_AString.length - 1);
      }

      this.k_B = (byte)(this.a_dc.b_AString[this.j_B].length() - 1);
    }
  }

  public final boolean a_Z() {
    return this.i_B + this.h_B - 1 >= this.g_B && this.k_B >= this.a_dc.b_AString[this.j_B].length() - 1;
  }

  public final void d() {
    this.c_S = (short)(at.a_AS[0] - this.d_S);
  }

  public final void e() {
    if (this.a_dc != null) {
      this.a_dc = null;
    }

    this.a_dc = new dc();
    String var1 = null;
    if (this.a_B >= 0 && this.a_AString != null && this.a_B < this.a_AString.length) {
      var1 = this.a_AString[this.a_B];
    }

    this.a_String = null;
    this.c_Image = null;
    this.a_bx = null;
    int var2 = var1.indexOf(".");
    int var3 = var1.indexOf("$");
    if (var3 < 0) {
      var2 = -1;
    }

    if (var1.indexOf("@") != -1) {
      var1 = k.a(var1, "@", String.valueOf(ci.a_I));
    }

    if (var2 != -1) {
      this.e_S = cg.a_S(var1.substring(var3 + 1, var2));
      if (var3 == 0) {
        m_B = 0;
      } else {
        m_B = cg.a_B(var1.substring(0, var3));
      }

      if (m_B > 6) {
        m_B = 0;
      }

      da.a_da.a_cd.a_bl = da.a_da.a_cd.a_bl(this.e_S);
      var1 = var1.substring(var2 + 1);
      String var7 = o.d_AString[this.e_S - 1];
      this.a_String = o.c_AString[this.e_S - 1];
      if (var7 != null && !var7.equals("")) {
        if (var7.endsWith(".av")) {
          this.a_bx = new bx();
          this.a_bx.a(var7);
          this.a_bx.a_V(m_B);
        } else {
          this.c_Image = cc.a(var7);
        }
      }

      var1 = "<0x7ff5e8>" + this.a_String + "</>%" + var1;
    }

    this.d();
    if (var1 != null) {
      if (this.c_Image == null && this.a_bx == null) {
        this.a_dc.a(var1, "%", this.c_S - 16);
      } else {
        this.a_dc.a(var1, "%", this.c_S - 16);
      }

      this.g_B = (byte)(this.a_dc.b_AString.length - 1);
    }

    this.h_B = this.f_B;
    if (da.a_da.a_cd.a_bl != null) {
      if (da.a_da.a_cd.a_bn == null || !da.a_da.a_cd.a_bn.b_Z) {
        this.a_I = da.a_da.a_cd.a_bl.i_S;
      } else if (this.e_S != 1 && this.e_S != 2 && this.e_S != 3) {
        this.b_Z = true;
      } else {
        this.a_I = da.a_da.a_cd.a_bn.a_Ae[0].c_S;
      }
    }

    this.b_Z = true;
    boolean var5 = false;
    var2 = this.f_I - (this.d_S + (at.a_AS[0] - this.d_S) / 2 - at.a_AS[0] / 2);
    int var4;
    if (this.b_Z) {
      var3 = this.d_S / 2;
      var4 = at.a_AS[1] - (this.h_B * (cg.b_S + 1) + 10) - 5;
    } else if (0 <= at.a_AS[0] / 2) {
      var3 = 0 - this.c_S / 2 + this.d_S / 2;
      var4 = 0 - this.a_I - this.h_B * (cg.b_S + 1) - 13;
      if (var3 < this.d_S / 4) {
        var2 += var3 - this.d_S / 4;
        var3 = this.d_S / 4;
      }

      if (var4 < 10) {
        var5 = true;
        var4 = 11;
      }

      this.d_I = 2;
    } else {
      var2 = -var2;
      var3 = 0 - this.c_S / 2 - this.d_S / 2;
      var4 = 0 - this.a_I - this.h_B * (cg.b_S + 1) - 10;
      if (var3 + this.c_S > at.a_AS[0] - this.d_S / 4) {
        var2 += var3 + this.c_S - (at.a_AS[0] - this.d_S / 4);
        var3 = at.a_AS[0] - this.d_S / 4 - this.c_S;
      }

      if (var4 < 10) {
        var5 = true;
        var4 = 11;
      }

      this.d_I = 1;
    }

    this.e_I = var2;
    this.c_Z = var5;
    this.b_I = var3;
    this.c_I = var4;
    this.c_B = 0;
    this.i_B = 0;
    this.j_B = this.i_B;
    this.k_B = 0;
    this.d_Z = true;
  }

  public final void a(Graphics var1) {
    if (this.a_Z) {
      if (da.a_da.a_S % 3 == 0 && this.d_Z) {
        if (this.k_B < this.a_dc.b_AString[this.j_B].length() - 1) {
          this.k_B++;
        } else if (this.j_B < this.i_B + this.h_B - 1 && this.j_B < this.a_dc.b_AString.length - 1) {
          this.j_B++;
          this.k_B = 0;
        }

        if (this.j_B >= this.a_dc.b_AString.length - 1 && this.k_B >= this.a_dc.b_AString[this.a_dc.b_AString.length - 1].length() - 1) {
          this.d_Z = false;
        }
      }

      if (this.c_B == 0) {
        if (this.c_Image != null) {
          var1.drawImage(this.c_Image, at.a_AS[0], this.c_I + this.h_B * (cg.b_S + 1) + 10 - 2, 40);
        } else if (this.a_bx != null) {
          this.a_bx.a(var1, at.a_AS[0] - 30, at.a_AS[1]);
        }

        int var10003 = this.c_I + 5;
        int var10005 = this.h_B * (cg.b_S + 1) + 10;
        int var11 = this.c_Z;
        int var10 = this.e_I;
        int var9 = this.d_I;
        int var8 = var10005;
        int var7 = this.c_S;
        int var6 = var10003;
        int var5 = this.b_I;
        if (!this.b_Z) {
          if (!var11) {
            var1.setClip(0, 0, at.a_AS[0], var6 + var8 - 1);
          } else {
            var1.setClip(0, var6 + 1, at.a_AS[0], at.a_AS[1]);
          }
        }

        vh.boxAt(var1, var5, var6, var7, var8, 1317669, 15788720, 80, true);
        var1.setColor(2170141);
        vh.nop6(var1, var5 - 1, var6 - 1, var7 + 1, var8 + 1, 10, 10);
        var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
        if (this.d_Image != null) {
          var1.drawRegion(this.d_Image, 0, 0, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, 0, var5 - 1, var6 - 1, 20);
          var1.drawRegion(
            this.d_Image, this.d_Image.getWidth() >> 1, 0, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, 0, var5 + 1 + var7, var6 - 1, 24
          );
        }

        if (this.e_Image != null) {
          var1.drawRegion(this.e_Image, 0, 0, this.e_Image.getWidth() >> 1, this.e_Image.getHeight(), 0, var5 - 1, var6 + 1 + var8, 36);
          var1.drawRegion(
            this.e_Image, this.e_Image.getWidth() >> 1, 0, this.e_Image.getWidth() >> 1, this.e_Image.getHeight(), 0, var5 + var7 + 1, var6 + 1 + var8, 40
          );
        } else if (this.d_Image != null) {
          var1.drawRegion(
            this.d_Image, 0, this.d_Image.getHeight() >> 1, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, 0, var5 - 1, var6 + 1 + var8, 36
          );
          var1.drawRegion(
            this.d_Image,
            this.d_Image.getWidth() >> 1,
            this.d_Image.getHeight() >> 1,
            this.d_Image.getWidth() >> 1,
            this.d_Image.getHeight() >> 1,
            0,
            var5 + var7 + 1,
            var6 + 1 + var8,
            40
          );
        }

        if (!this.b_Z) {
          if (!var11) {
            var1.setColor(15788720);
            vh.nop4(var1, var5 + 2, var6 + var8 - 1, (var7 - 20) / 2 - 2 + var10, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 + var8 - 1, (var7 - 20) / 2 - 1 - var10, 1);
            var1.setColor(2170141);
            vh.nop4(var1, var5 + 2, var6 + var8, (var7 - 20) / 2 - 2 + var10, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 + var8, (var7 - 20) / 2 - 1 - var10, 1);
            vh.nop4(var1, var5 + 1, var6 + var8 - 1, 1, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + (var7 - 20) / 2 - 1, var6 + var8 - 1, 1, 1);
          } else {
            var1.setColor(15788720);
            vh.nop4(var1, var5 + 2, var6, (var7 - 20) / 2 - 2 + var10, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6, (var7 - 20) / 2 - 1 - var10, 1);
            var1.setColor(2170141);
            vh.nop4(var1, var5 + 2, var6 - 1, (var7 - 20) / 2 - 2 + var10, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 - 1, (var7 - 20) / 2 - 1 - var10, 1);
            vh.nop4(var1, var5 + 1, var6, 1, 1);
            vh.nop4(var1, var5 + var7 / 2 + 9 + (var7 - 20) / 2 - 1, var6, 1, 1);
          }

          if (this.a_Image != null) {
            if (!var11) {
              if (var9 == 1) {
                var1.drawRegion(this.a_Image, 0, 0, 28, 11, 0, var5 + var7 / 2 - 10 + var10, var6 + var8 - 1, 20);
              }

              if (var9 == 2) {
                var1.drawRegion(this.a_Image, 28, 0, 28, 11, 0, var5 + var7 / 2 - 10 - 9 + var10, var6 + var8 - 1, 20);
              }
            } else {
              if (var9 == 1) {
                var1.drawRegion(this.a_Image, 0, 11, 28, 11, 0, var5 + var7 / 2 - 10 + var10, var6 + 1, 36);
              }

              if (var9 == 2) {
                var1.drawRegion(this.a_Image, 28, 11, 28, 11, 0, var5 + var7 / 2 - 10 - 9 + var10, var6 + 1, 36);
              }
            }
          }
        }

        var1.setColor(8401696);

        for (int var15 = this.i_B; var15 < this.i_B + this.h_B; var15++) {
          if (this.a_dc.b_AString != null && var15 < this.a_dc.b_AString.length) {
            if (var15 < this.j_B) {
              if (this.a_dc.b_AString != null) {
                this.a_dc.a(var1, this.a_dc.b_AString[var15], var15, this.b_I + 3 + 8, this.c_I + 10 + this.l_B * (var15 - this.i_B), 16777215);
              }
            } else if (var15 == this.j_B && this.a_dc.b_AString != null) {
              dc var10000 = this.a_dc;
              String var10002 = this.a_dc.b_AString[var15].substring(0, this.k_B + 1);
              int var10004 = this.k_B + 1;
              var10005 = this.b_I + 3 + 8;
              int var10006 = this.c_I + 10 + this.l_B * (var15 - this.i_B);
              var10 = 16777215;
              var9 = var10006;
              var8 = var10005;
              var7 = var10004;
              var6 = var15;
              String var18 = var10002;
              Graphics var17 = var1;
              dc var14 = var10000;
              var11 = (boolean)0;
              String[][] var25 = null;

              for (int var12 = 0; var14.a_AAString != null && var12 < var14.a_AAString.length; var12++) {
                int var13 = cg.a_I(var14.a_AAString[var12][0]);
                if (var6 == var13) {
                  var11 = (boolean)1;
                  var25 = k.a(var25, var14.a_AAString[var12]);
                }
              }

              if (!var11) {
                var1.setColor(16777215);
                vh.drawString(var1, var18, var8, var9, 20);
              } else if (var7 >= var14.b_AString[var6].length()) {
                var14.a(var1, var14.b_AString[var6].substring(0, var7), var6, var8, var9, 16777215);
              } else {
                for (int var28 = 0; var25 != null && var28 < var25.length; var28++) {
                  if (cg.a_I(var25[var28][0]) == var6) {
                    var5 = cg.a_I(var25[var28][1]);
                    var11 = cg.a_I(var25[var28][2]);
                    if (var7 >= var5 && var7 <= var11) {
                      var14.a(var17, var14.b_AString[var6].substring(0, var5), var6, var8, var9, 16777215);
                      if (!var25[var28][3].startsWith("0x") && !var25[var28][3].startsWith("0X")) {
                        var17.setColor(var14.a_AI[cg.a_I(var25[var28][3]) - 1]);
                      } else {
                        var17.setColor(cg.a_I(var25[var28][3]));
                      }

                      vh.drawString(
                        var17,
                        var14.b_AString[var6].substring(var5, var7),
                        var8 + vh.stringWidth(cg.a_Font, var14.b_AString[var6].substring(0, var5)),
                        var9,
                        20
                      );
                      break;
                    }

                    var14.a(var17, var14.b_AString[var6].substring(0, var7), var6, var8, var9, 16777215);
                  }
                }
              }
            }
          }
        }
      } else {
        if (this.c_B == e_B) {
          byte var2;
          if (da.a_da.a_S % 12 == 0) {
            var2 = 3;
          } else {
            var2 = 0;
          }

          b_S = 3;
          int var3 = at.a_AS[1] / 2 - (b_S * (cg.b_S + 1) + 10) / 2;
          int var4 = b_S * (cg.b_S + 1) + 10;
          if (this.b_AString.length < b_S) {
            var4 = this.b_AString.length * (cg.b_S + 1) + 10;
            var3 = at.a_AS[1] / 2 - (this.b_AString.length * (cg.b_S + 1) + 10) / 2;
          }

          vh.boxAt(var1, 20, var3, at.a_AS[0] - 40, var4, 1317669, 15788720, 80, true);
          var1.setColor(2170141);
          vh.nop6(var1, 19, var3 - 1, at.a_AS[0] - 40 + 1, var4 + 1, 10, 10);
          if (this.d_Image != null) {
            var1.drawRegion(this.d_Image, 0, 0, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, 0, 19, var3 - 1, 20);
            var1.drawRegion(
              this.d_Image, this.d_Image.getWidth() >> 1, 0, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, 0, 21 + at.a_AS[0] - 40, var3 - 1, 24
            );
            var1.drawRegion(
              this.d_Image, 0, this.d_Image.getWidth() >> 1, this.d_Image.getHeight() >> 1, this.d_Image.getHeight() >> 1, 0, 19, var3 + 1 + var4, 36
            );
            var1.drawRegion(
              this.d_Image,
              this.d_Image.getWidth() >> 1,
              this.d_Image.getHeight() >> 1,
              this.d_Image.getWidth() >> 1,
              this.d_Image.getHeight() >> 1,
              0,
              21 + at.a_AS[0] - 40,
              var3 + 1 + var4,
              40
            );
          }

          if (this.b_AString.length > b_S) {
            k.a(var1, null, null, at.a_AS[0] - 35, var3 + 5, b_S * (cg.b_S + 1), this.b_AString.length, b_S, this.a_S);
          }

          for (int var16 = 0; var16 < b_S; var16++) {
            if (var16 + this.a_S < this.b_AString.length) {
              if (var16 + this.a_S == this.b_B) {
                k.a(var1, this.b_AString[var16 + this.a_S], at.a_AS[0] / 2, var3 + 5 + var16 * (cg.b_S + 1), 2093589, 17);
                if (this.b_AString.length > 1 && this.b_Image != null) {
                  var1.drawRegion(
                    this.b_Image,
                    0,
                    0,
                    15,
                    15,
                    0,
                    at.a_AS[0] / 2 - vh.stringWidth(cg.a_Font, this.b_AString[var16 + this.a_S]) / 2 - 10 + var2,
                    var3 + 5 + var16 * (cg.b_S + 1) + cg.b_S / 2,
                    3
                  );
                }
              } else {
                var1.setColor(16777215);
                vh.drawString(var1, this.b_AString[var16 + this.a_S], at.a_AS[0] / 2, var3 + 5 + var16 * (cg.b_S + 1), 17);
              }
            }
          }
        }
      }
    }
  }

  public final void f() {
    if ((this.j_B >= this.i_B + this.h_B - 1 || this.j_B >= this.a_dc.b_AString.length - 1) && this.k_B >= this.a_dc.b_AString[this.j_B].length() - 1) {
      if (this.a_B < this.a_AString.length - 1) {
        this.a_B++;
        if (this.a_B > this.a_AString.length - 1) {
          this.a_B = (byte)(this.a_AString.length - 1);
        }

        this.i_B = 0;
        this.e();
      }
    } else {
      this.j_B = (byte)(this.i_B + this.h_B - 1);
      if (this.j_B > this.a_dc.b_AString.length - 1) {
        this.j_B = (byte)(this.a_dc.b_AString.length - 1);
      }

      this.k_B = (byte)(this.a_dc.b_AString[this.j_B].length() - 1);
      this.d_Z = false;
    }
  }
}
