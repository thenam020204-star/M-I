import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ca {
  private short a_S;
  private short b_S;
  private short c_S;
  private short d_S;
  private short e_S;
  private short f_S;
  private byte a_B;
  private byte b_B;
  private String[] a_AString;
  private Image[] a_AImage;
  private byte[] a_AB;
  private short[] a_AS;
  private short[] b_AS;
  private short[] c_AS;
  private short[] d_AS;
  private byte[] b_AB;
  private Image[] b_AImage;
  private byte[][] a_AAB;
  private byte[][] b_AAB;
  private short[][] a_AAS;
  private byte[] c_AB;
  private byte c_B;
  private short[] e_AS;
  private short[] f_AS;
  private short[] g_AS;
  private short[] h_AS;
  private byte[] d_AB;
  private String a_String;
  private String b_String;
  private byte[][] c_AAB;
  private int a_I;
  private Image a_Image;
  private Graphics a_Graphics;
  private boolean a_Z = true;
  private boolean b_Z;
  private short g_S;
  private short h_S;
  private short i_S;
  private short j_S;
  private short k_S;
  private short l_S;
  private short m_S;
  private boolean c_Z = true;
  private short[][][] a_AAAS;
  private short[][][] b_AAAS;
  private byte d_B;

  public final void a(String var1, int[] var2, int[] var3) {
    for (int var4 = 0; this.a_AString != null && var4 < this.a_AString.length; var4++) {
      if (this.a_AString[var4].equals(var1)) {
        this.c_AAB[var4] = be.a(this.c_AAB[var4], var2, var3);
        return;
      }
    }
  }

  public final boolean a(short[] var1, int var2) {
    for (byte var4 = 0; var1 != null && var4 < var1.length; var4 += 4) {
      for (int var3 = 0; this.e_AS != null && var3 < this.e_AS.length; var3++) {
        if (1 == this.d_AB[var3]
          && cg.a(var1[var4], var1[var4 + 1], var1[var4 + 2], var1[var4 + 3], this.e_AS[var3], this.f_AS[var3], this.g_AS[var3], this.h_AS[var3])) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean a(int var1) {
    for (int var2 = 0; this.a_AAS != null && this.a_AAS[var1] != null && var2 < this.a_AAS[var1].length; var2++) {
      if (this.c_AS[this.a_AAS[var1][var2]] != this.a_B) {
        return false;
      }

      if (this.d_AS[this.a_AAS[var1][var2]] != this.b_B) {
        return false;
      }
    }

    return true;
  }

  public final void a_V() {
    this.e_S = 0;
    this.f_S = 0;
    this.a_B = 0;
    this.b_B = 0;
    this.a_AImage = null;
    this.a_AB = null;
    this.a_AS = null;
    this.b_AS = null;
    this.c_AS = null;
    this.d_AS = null;
    this.b_AB = null;
    this.b_AImage = null;
    this.a_AAB = null;
    this.b_AAB = null;
    this.a_AAS = null;
    this.c_B = -1;
    this.e_AS = null;
    this.f_AS = null;
    this.g_AS = null;
    this.h_AS = null;
    this.d_AB = null;
    this.c_AAB = null;
    this.a_Image = null;
    this.a_Graphics = null;
    this.b_Z = false;
  }

  public final void b_V() {
    this.a_AImage = null;
    this.b_AImage = null;
    this.c_AAB = null;
    this.a_Image = null;
    this.a_Graphics = null;
    this.b_Z = false;
  }

  private void b(int var1, int var2, int var3, int var4) {
    for (int var5 = var1; var5 <= var2; var5++) {
      if (var3 > 0) {
        this.a(
          this.a_Graphics,
          var5 * this.a_B % this.i_S,
          var4 * this.b_B % this.j_S,
          new short[]{(short)(var5 * this.a_B), (short)(var4 * this.b_B), this.a_B, (short)(var3 * this.b_B)}
        );
      }

      if (var3 < this.l_S) {
        this.a(
          this.a_Graphics,
          var5 * this.a_B % this.i_S,
          0,
          new short[]{(short)(var5 * this.a_B), (short)((var4 + var3) * this.b_B), this.a_B, (short)((this.l_S - var3) * this.b_B)}
        );
      }
    }
  }

  private void c(int var1, int var2, int var3, int var4) {
    for (int var5 = var1; var5 <= var2; var5++) {
      if (var3 > 0) {
        this.a(
          this.a_Graphics,
          var4 * this.a_B % this.i_S,
          var5 * this.b_B % this.j_S,
          new short[]{(short)(var4 * this.a_B), (short)(var5 * this.b_B), (short)(var3 * this.a_B), this.b_B}
        );
      }

      if (var3 < this.k_S) {
        this.a(
          this.a_Graphics,
          (var4 + var3) * this.a_B % this.i_S,
          var5 * this.b_B % this.j_S,
          new short[]{(short)((var4 + var3) * this.a_B), (short)(var5 * this.b_B), (short)((this.k_S - var3) * this.a_B), this.b_B}
        );
      }
    }
  }

  public final void c() {
    if (this.a_AString != null) {
      this.a_AImage = new Image[this.a_AString.length];

      for (int var1 = 0; var1 < this.a_AImage.length; var1++) {
        this.a_AImage[var1] = Image.createImage(this.c_AAB[var1], 0, this.c_AAB[var1].length);
      }

      this.c_AAB = null;
      ca var3 = this;
      if (at.a_B == 1) {
        this.b_AImage = new Image[this.a_AS.length];

        for (int var4 = 0; var4 < var3.a_AS.length; var4++) {
          if (var3.a_AImage[var3.a_AB[var4]] != null && var3.b_AB[var4] > 0) {
            var3.b_AImage[var4] = Image.createImage(
              var3.a_AImage[var3.a_AB[var4]], var3.a_AS[var4], var3.b_AS[var4], var3.c_AS[var4], var3.d_AS[var4], var3.b_AB[var4]
            );
          }
        }
      } else {
        if (at.a_B == 2) {
          this.b_AImage = new Image[this.a_AS.length];

          for (int var2 = 0; var2 < var3.a_AS.length; var2++) {
            if (var3.a_AImage[var3.a_AB[var2]] != null) {
              var3.b_AImage[var2] = Image.createImage(
                var3.a_AImage[var3.a_AB[var2]], var3.a_AS[var2], var3.b_AS[var2], var3.c_AS[var2], var3.d_AS[var2], var3.b_AB[var2]
              );
            }
          }

          var3.a_AImage = null;
        }
      }
    }
  }

  private void a_V(String var1) {
    if (var1 == null) {
      if (this.a_String == null) {
        int var2 = this.b_String.lastIndexOf(47);
        this.a_String = this.b_String.substring(0, var2 + 1);
      }
    } else if (var1.startsWith("/")) {
      this.a_String = var1;
    } else {
      int var3 = this.b_String.lastIndexOf(47);
      this.a_String = this.b_String.substring(0, var3 + 1) + var1;
    }

    if (!this.a_String.endsWith("/")) {
      this.a_String = this.a_String + "/";
    }
  }

  private boolean a(byte[] var1) {
    if (var1 == null) {
      return false;
    } else {
      ByteArrayInputStream var13 = new ByteArrayInputStream(var1);
      DataInputStream var14 = new DataInputStream(var13);

      try {
        byte[] var2 = new byte[3];
        var14.read(var2);
        this.d_B = var14.readByte();
        this.e_S = var14.readShort();
        this.f_S = var14.readShort();
        this.a_B = var14.readByte();
        this.b_B = var14.readByte();
        short var15;
        if ((var15 = (short)(var14.readByte() & 255)) > 0) {
          this.a_AString = new String[var15];

          for (int var3 = 0; var3 < var15; var3++) {
            this.a_AString[var3] = var14.readUTF();
          }
        }

        short var20;
        if ((var20 = var14.readShort()) > 0) {
          this.a_AB = new byte[var20];
          this.a_AS = new short[var20];
          this.b_AS = new short[var20];
          this.c_AS = new short[var20];
          this.d_AS = new short[var20];
          this.b_AB = new byte[var20];
          var2 = new byte[4];
          var14.read(var2);

          for (int var4 = 0; var4 < var20; var4++) {
            this.a_AB[var4] = var14.readByte();
            if (var2[0] == 1) {
              this.a_AS[var4] = var14.readShort();
            } else {
              this.a_AS[var4] = (short)(var14.readByte() & 255);
            }

            if (var2[1] == 1) {
              this.b_AS[var4] = var14.readShort();
            } else {
              this.b_AS[var4] = (short)(var14.readByte() & 255);
            }

            if (var2[2] == 1) {
              this.c_AS[var4] = var14.readShort();
            } else {
              this.c_AS[var4] = (short)(var14.readByte() & 255);
            }

            if (var2[3] == 1) {
              this.d_AS[var4] = var14.readShort();
            } else {
              this.d_AS[var4] = (short)(var14.readByte() & 255);
            }

            this.b_AB[var4] = var14.readByte();
          }
        }

        if (this.d_B == 1) {
          byte var17;
          if ((var17 = var14.readByte()) > 0) {
            this.a_AAB = new byte[var17][];
            this.b_AAB = new byte[var17][];
            this.a_AAS = new short[var17][];

            for (int var21 = 0; var21 < var17; var21++) {
              short var5;
              if ((var5 = var14.readShort()) > 0) {
                this.a_AAB[var21] = new byte[var5];
                this.b_AAB[var21] = new byte[var5];
                this.a_AAS[var21] = new short[var5];

                for (int var6 = 0; var6 < var5; var6++) {
                  this.a_AAB[var21][var6] = var14.readByte();
                  this.b_AAB[var21][var6] = var14.readByte();
                  if (var20 > 255) {
                    this.a_AAS[var21][var6] = var14.readShort();
                  } else {
                    this.a_AAS[var21][var6] = (short)(var14.readByte() & 255);
                  }
                }
              }
            }
          }
        } else if (this.d_B == 2) {
          byte var18;
          if ((var18 = var14.readByte()) > 0 && var18 > 0) {
            this.a_AAB = new byte[var18][];
            this.b_AAB = new byte[var18][];
            this.a_AAS = new short[var18][];
            this.c_AB = new byte[var18];

            for (int var22 = 0; var22 < var18; var22++) {
              int var25 = var14.readByte();
              this.c_AB[var22] = (byte)var25;
              if (var25 == 0) {
                short var33;
                if ((var33 = var14.readShort()) > 0) {
                  this.a_AAB[var22] = new byte[var33];
                  this.b_AAB[var22] = new byte[var33];
                  this.a_AAS[var22] = new short[var33];

                  for (int var40 = 0; var40 < var33; var40++) {
                    this.a_AAB[var22][var40] = var14.readByte();
                    this.b_AAB[var22][var40] = var14.readByte();
                    if (var20 > 255) {
                      this.a_AAS[var22][var40] = var14.readShort();
                    } else {
                      this.a_AAS[var22][var40] = (short)(var14.readByte() & 255);
                    }
                  }
                }
              } else if (var25 == 1) {
                int var32 = this.e_S / this.a_B + (this.e_S % this.a_B != 0 ? 1 : 0);
                int var39 = this.f_S / this.b_B + (this.f_S % this.b_B != 0 ? 1 : 0);
                if (this.a_AB.length > 255) {
                  for (int var44 = 0; var44 < var39; var44++) {
                    for (int var48 = 0; var48 < var32; var48++) {
                      if ((var25 = var14.readShort()) > 0) {
                        this.a_AAB[var22] = cg.a_AB(this.a_AAB[var22], var48);
                        this.b_AAB[var22] = cg.a_AB(this.b_AAB[var22], var44);
                        this.a_AAS[var22] = cg.a_AS(this.a_AAS[var22], var25 - 1);
                      }
                    }
                  }
                } else {
                  for (int var43 = 0; var43 < var39; var43++) {
                    for (int var47 = 0; var47 < var32; var47++) {
                      if ((var25 = (short)(var14.readByte() & 255)) > 0) {
                        this.a_AAB[var22] = cg.a_AB(this.a_AAB[var22], var47);
                        this.b_AAB[var22] = cg.a_AB(this.b_AAB[var22], var43);
                        this.a_AAS[var22] = cg.a_AS(this.a_AAS[var22], var25 - 1);
                      }
                    }
                  }
                }
              } else if (var25 == 2) {
                int var31 = this.e_S / this.a_B + (this.e_S % this.a_B != 0 ? 1 : 0);
                int var7 = this.f_S / this.b_B + (this.f_S % this.b_B != 0 ? 1 : 0);
                short var8;
                if (this.a_AB.length > 255) {
                  var8 = var14.readShort();
                } else {
                  var8 = var14.readByte();
                }

                int var9 = this.c_AS[var8] / this.a_B;
                var25 = this.d_AS[var8] / this.b_B;

                for (int var10 = 0; var10 < var7; var10 += var25) {
                  for (int var11 = 0; var11 < var31; var11 += var9) {
                    this.a_AAB[var22] = cg.a_AB(this.a_AAB[var22], var11);
                    this.b_AAB[var22] = cg.a_AB(this.b_AAB[var22], var10);
                    this.a_AAS[var22] = cg.a_AS(this.a_AAS[var22], var8);
                  }
                }
              }
            }
          }

          this.a_I = var14.readInt();
        }

        this.c_B = var14.readByte();
        short var19;
        if ((var19 = var14.readShort()) > 0) {
          byte[] var23 = new byte[4];
          var14.read(var23);
          this.e_AS = new short[var19];
          this.f_AS = new short[var19];
          this.g_AS = new short[var19];
          this.h_AS = new short[var19];
          this.d_AB = new byte[var19];

          for (int var29 = 0; var29 < var19; var29++) {
            if (var23[0] == 1) {
              this.e_AS[var29] = var14.readShort();
            } else {
              this.e_AS[var29] = var14.readByte();
            }

            if (var23[1] == 1) {
              this.f_AS[var29] = var14.readShort();
            } else {
              this.f_AS[var29] = var14.readByte();
            }

            if (var23[2] == 1) {
              this.g_AS[var29] = var14.readShort();
            } else {
              this.g_AS[var29] = (short)(var14.readByte() & 255);
            }

            if (var23[3] == 1) {
              this.h_AS[var29] = var14.readShort();
            } else {
              this.h_AS[var29] = (short)(var14.readByte() & 255);
            }

            this.d_AB[var29] = var14.readByte();
          }
        }

        if (this.c_Z && this.a_AAB != null) {
          boolean var24 = true;
          boolean var30 = true;
          if (this.c_B > 0) {
            for (int var35 = 0; var35 < this.c_B; var35++) {
              if (!this.a(var35)) {
                var24 = false;
                System.out.println("Bản đồ " + var35 + " Ô lớp không chuẩn, hậu cảnh không dùng được tăng tốc ma trận");
                break;
              }
            }

            for (int var36 = this.c_B; var36 < this.a_AAB.length; var36++) {
              if (!this.a(var36)) {
                var30 = false;
                System.out.println("Bản đồ " + var36 + " Ô lớp không chuẩn, tiền cảnh không dùng được tăng tốc ma trận");
                break;
              }
            }
          } else if (this.c_B < 0) {
            for (int var34 = 0; var34 < this.a_AAB.length; var34++) {
              if (!this.a(var34)) {
                var24 = false;
                System.out.println("Bản đồ " + var34 + " Ô lớp không chuẩn, hậu cảnh không dùng được tăng tốc ma trận");
                break;
              }
            }

            var30 = false;
          }

          if (this.c_B > 0) {
            if (var24) {
              this.a_AAAS = new short[this.c_B][][];
            }

            if (var30) {
              this.b_AAAS = new short[this.a_AAB.length - this.c_B][][];
            }
          } else if (this.c_B == 0) {
            if (var30) {
              this.b_AAAS = new short[this.a_AAB.length - this.c_B][][];
            }
          } else if (var24) {
            this.a_AAAS = new short[this.a_AAB.length][][];
          }

          for (int var37 = 0; this.a_AAAS != null && var37 < this.a_AAAS.length; var37++) {
            int var41 = this.f_S / this.b_B;
            int var45 = this.e_S / this.a_B;
            this.a_AAAS[var37] = new short[var41][var45];

            for (int var49 = 0; this.a_AAB[var37] != null && var49 < this.a_AAB[var37].length; var49++) {
              this.a_AAAS[var37][this.b_AAB[var37][var49]][this.a_AAB[var37][var49]] = (short)(this.a_AAS[var37][var49] + 1);
            }

            this.a_AAB[var37] = null;
            this.b_AAB[var37] = null;
            this.a_AAS[var37] = null;
          }

          for (int var38 = 0; this.b_AAAS != null && var38 < this.b_AAAS.length; var38++) {
            int var42 = this.f_S / this.b_B;
            int var46 = this.e_S / this.a_B;
            this.b_AAAS[var38] = new short[var42][var46];

            for (int var50 = 0; this.a_AAB[var38 + this.c_B] != null && var50 < this.a_AAB[var38 + this.c_B].length; var50++) {
              this.b_AAAS[var38][this.b_AAB[var38 + this.c_B][var50]][this.a_AAB[var38 + this.c_B][var50]] = (short)(this.a_AAS[var38 + this.c_B][var50] + 1);
            }

            this.a_AAB[var38 + this.c_B] = null;
            this.b_AAB[var38 + this.c_B] = null;
            this.a_AAS[var38 + this.c_B] = null;
          }

          if (var24 && this.a_AAAS != null) {
            System.out.println("Hậu cảnh dùng tăng tốc ma trận");
          }

          if (var30 && this.b_AAAS != null) {
            System.out.println("Tiền cảnh dùng tăng tốc ma trận");
          }
        }

        return true;
      } catch (IOException var12) {
        var12.printStackTrace();
        return false;
      }
    }
  }

  public final short a_S() {
    return this.f_S;
  }

  public final short b_S() {
    return this.e_S;
  }

  public final void a(String[] var1, String[] var2, String var3, int var4) {
    if (this.a_AString != null) {
      this.a_V(var3);
      this.c_AAB = new byte[this.a_AString.length][];

      for (int var5 = 0; var5 < this.a_AString.length; var5++) {
        String var6 = this.a_AString[var5];
        this.c_AAB[var5] = cc.a_AB(this.a_String + var6, var4);
      }
    }
  }

  public final void a(Graphics var1, int var2, int var3) {
    if (!this.a_Z || !at.r_Z) {
      this.a(var1, var2, var3, new short[]{this.a_S, this.b_S, this.c_S, this.d_S});
    } else if (this.b_Z) {
      int var8 = this.a_S % this.i_S;
      int var9 = this.b_S % this.j_S;
      var1.drawRegion(this.a_Image, var8, var9, this.i_S - var8, this.j_S - var9, 0, var2, var3, 20);
      var1.drawRegion(this.a_Image, 0, var9, var8, this.j_S - var9, 0, var2 + this.i_S - var8, var3, 20);
      var1.drawRegion(this.a_Image, 0, 0, var8, var9, 0, var2 + this.i_S - var8, var3 + this.j_S - var9, 20);
      var1.drawRegion(this.a_Image, var8, 0, this.i_S - var8, var9, 0, var2, var3 + this.j_S - var9, 20);
    } else {
      this.m_S = (short)(this.a_B << 1);
      this.i_S = (short)(this.c_S / this.a_B * this.a_B + (this.c_S % this.a_B != 0 ? this.a_B : 0) + this.m_S);
      this.j_S = (short)(this.d_S / this.b_B * this.b_B + (this.d_S % this.b_B != 0 ? this.b_B : 0) + this.m_S);
      this.k_S = (short)(this.i_S / this.a_B);
      this.l_S = (short)(this.j_S / this.b_B);
      this.g_S = (short)(this.a_S / this.a_B * this.a_B);
      this.h_S = (short)(this.b_S / this.b_B * this.b_B);
      this.a_Image = Image.createImage(this.i_S, this.j_S);
      this.a_Graphics = this.a_Image.getGraphics();
      this.a_Graphics.setColor(this.a_I);
      this.a_Graphics.fillRect(0, 0, this.i_S, this.j_S);
      int var4 = this.a_S / this.a_B;
      int var5 = this.b_S / this.b_B;
      int var6 = this.k_S - var4 % this.k_S;
      var2 = this.l_S - var5 % this.l_S;
      this.a(
        this.a_Graphics,
        var4 % this.k_S * this.a_B,
        var5 % this.l_S * this.b_B,
        new short[]{(short)(var4 * this.a_B), (short)(var5 * this.b_B), (short)(var6 * this.a_B), (short)(var2 * this.b_B)}
      );
      if (this.k_S > var6) {
        this.a(
          this.a_Graphics,
          0,
          var5 % this.l_S * this.b_B,
          new short[]{(short)((var4 + var6) * this.a_B), (short)(var5 * this.b_B), (short)((this.k_S - var6) * this.a_B), (short)(var2 * this.b_B)}
        );
      }

      if (this.l_S > var2) {
        this.a(
          this.a_Graphics,
          var4 % this.k_S * this.a_B,
          0,
          new short[]{(short)(var4 * this.a_B), (short)((var5 + var2) * this.b_B), (short)(var6 * this.a_B), (short)((this.l_S - var2) * this.b_B)}
        );
      }

      if (this.k_S > var6 && this.l_S > var2) {
        this.a(
          this.a_Graphics,
          0,
          0,
          new short[]{
            (short)((var4 + var6) * this.a_B), (short)((var5 + var2) * this.b_B), (short)((this.k_S - var6) * this.a_B), (short)((this.l_S - var2) * this.b_B)
          }
        );
      }

      this.b_Z = true;
    }
  }

  private void a(Graphics var1, int var2, int var3, short[] var4) {
    if (this.a_AAAS != null) {
      short var21;
      short var23 = (short)((var21 = var4[0]) + var4[2]);
      short var7;
      short var18 = (short)((var7 = var4[1]) + var4[3]);
      short var8 = (short)(var21 / this.a_B);
      short var9 = (short)(var23 / this.a_B + (var23 % this.a_B != 0 ? 1 : 0));
      short var10 = (short)(var7 / this.b_B);
      short var11 = (short)(var18 / this.b_B + (var18 % this.b_B != 0 ? 1 : 0));
      if (var8 < 0) {
        var8 = 0;
      }

      if (var10 < 0) {
        var10 = 0;
      }

      var1.translate(var2 - var21, var3 - var7);
      if (this.a_Z && at.r_Z || at.s_Z) {
        var1.setColor(this.a_I);
        var1.fillRect(var21, var7, var23 - var21, var18 - var7);
      }

      short var19;
      var23 = var19 = (short)(var8 * this.a_B);
      short var12;
      short var13 = var12 = (short)(var10 * this.b_B);

      for (int var14 = 0; var14 < this.a_AAAS.length; var14++) {
        for (int var15 = var10; var15 < var11 && var15 < this.a_AAAS[var14].length; var15++) {
          for (int var16 = var8; var16 < var9 && var16 < this.a_AAAS[var14][var15].length; var16++) {
            short var17;
            if ((var17 = (short)(this.a_AAAS[var14][var15][var16] - 1)) >= 0) {
              if (this.b_AImage != null && this.b_AImage[var17] != null) {
                var1.drawImage(this.b_AImage[var17], var19, var12, 20);
              } else {
                var1.drawRegion(
                  this.a_AImage[this.a_AB[var17]], this.a_AS[var17], this.b_AS[var17], this.c_AS[var17], this.d_AS[var17], this.b_AB[var17], var19, var12, 20
                );
              }
            }

            var19 = (short)(var19 + this.a_B);
          }

          var12 = (short)(var12 + this.b_B);
          var19 = var23;
        }

        var12 = var13;
      }

      var1.translate(-var2 + var21, -var3 + var7);
    } else if (this.c_B > 0) {
      short[] var20 = new short[this.c_B];

      for (int var22 = 0; var22 < this.c_B; var22++) {
        var20[var22] = (short)var22;
      }

      if (this.a_Z && at.r_Z || at.s_Z) {
        var1.setColor(this.a_I);
        var1.translate(var2 - var4[0], var3 - var4[1]);
        var1.fillRect(var4[0], var4[1], var4[2], var4[3]);
        var1.translate(-var2 + var4[0], -var3 + var4[1]);
      }

      this.a(var1, var20, var2, var3, var4);
    } else {
      if (this.c_B < 0) {
        short[] var5 = new short[this.a_AAB.length];

        for (int var6 = 0; var6 < this.a_AAB.length; var6++) {
          var5[var6] = (short)var6;
        }

        if (this.a_Z && at.r_Z || at.s_Z) {
          var1.setColor(this.a_I);
          var1.translate(var2 - var4[0], var3 - var4[1]);
          var1.fillRect(var4[0], var4[1], var4[2], var4[3]);
          var1.translate(-var2 + var4[0], -var3 + var4[1]);
        }

        this.a(var1, var5, var2, var3, var4);
      }
    }
  }

  public final void a(Graphics var1) {
    for (int var2 = 0; this.e_AS != null && var2 < this.e_AS.length; var2++) {
      if (this.d_AB[var2] == 1) {
        var1.setColor(16711680);
      } else if (this.d_AB[var2] == 2) {
        var1.setColor(255);
      } else {
        var1.setColor(16711935);
      }

      var1.drawRect(this.e_AS[var2], this.f_AS[var2], this.g_AS[var2], this.h_AS[var2]);
    }
  }

  public final void b(Graphics var1, int var2, int var3) {
    if (this.b_AAAS != null) {
      short var18 = this.a_S;
      short var19 = (short)(this.a_S + this.c_S);
      short var6 = this.b_S;
      short var7 = (short)(this.b_S + this.d_S);
      short var8 = (short)(var18 / this.a_B);
      var19 = (short)(var19 / this.a_B);
      short var9 = (short)(var6 / this.b_B);
      var7 = (short)(var7 / this.b_B);
      if (var8 < 0) {
        var8 = 0;
      }

      if (var9 < 0) {
        var9 = 0;
      }

      if (var19 > this.e_S / this.a_B - 1) {
        var19 = (short)(this.e_S / this.a_B - 1);
      }

      if (var7 > this.f_S / this.b_B - 1) {
        var7 = (short)(this.f_S / this.b_B - 1);
      }

      var1.translate(var2 - var18, var3 - var6);
      short var10;
      short var11 = var10 = (short)(var8 * this.a_B);
      short var12;
      short var13 = var12 = (short)(var9 * this.b_B);

      for (int var14 = 0; var14 < this.b_AAAS.length; var14++) {
        for (int var15 = var9; var15 <= var7; var15++) {
          for (int var16 = var8; var16 <= var19; var16++) {
            short var17;
            if ((var17 = (short)((short)(this.b_AAAS[var14][var15][var16] & 255) - 1)) >= 0) {
              if (this.b_AImage != null && this.b_AImage[var17] != null) {
                var1.drawImage(this.b_AImage[var17], var10, var12, 20);
              } else {
                var1.drawRegion(
                  this.a_AImage[this.a_AB[var17]], this.a_AS[var17], this.b_AS[var17], this.c_AS[var17], this.d_AS[var17], this.b_AB[var17], var10, var12, 20
                );
              }
            }

            var10 = (short)(var10 + this.a_B);
          }

          var12 = (short)(var12 + this.b_B);
          var10 = var11;
        }

        var12 = var13;
      }

      var1.translate(-var2 + var18, -var3 + var6);
    } else {
      if (this.c_B >= 0) {
        short[] var4 = new short[this.a_AAB.length - this.c_B];

        for (int var5 = 0; var5 < var4.length; var5++) {
          var4[var5] = (short)(this.c_B + var5);
        }

        this.a(var1, var4, var2, var3, new short[]{this.a_S, this.b_S, this.c_S, this.d_S});
      }
    }
  }

  private void a(Graphics var1, short[] var2, int var3, int var4, short[] var5) {
    if (var2 != null) {
      short var6;
      int var7 = (var6 = var5[0]) + var5[2];
      short var8;
      int var9 = (var8 = var5[1]) + var5[3];
      int var10 = var1.getClipX();
      int var11 = var1.getClipY();
      int var12 = var1.getClipWidth();
      int var13 = var1.getClipHeight();
      var1.setClip(var3, var4, var5[2], var5[3]);
      var1.translate(var3 - var5[0], var4 - var5[1]);
      var6 = (short)(var6 / this.a_B);
      var8 = (short)(var8 / this.b_B);
      short var19 = (short)(var7 / this.a_B + (var7 % this.a_B != 0 ? 1 : 0));
      short var21 = (short)(var9 / this.b_B + (var9 % this.b_B != 0 ? 1 : 0));
      if (var6 < 0) {
        var6 = 0;
      }

      if (var8 < 0) {
        var8 = 0;
      }

      for (int var14 = 0; var14 < var2.length; var14++) {
        for (int var15 = 0; this.a_AAB[var2[var14]] != null && var15 < this.a_AAB[var2[var14]].length; var15++) {
          int var16 = this.a_AAB[var2[var14]][var15] * this.a_B;
          int var17 = this.b_AAB[var2[var14]][var15] * this.b_B;
          if (this.c_AS[this.a_AAS[var2[var14]][var15]] > this.a_B || this.d_AS[this.a_AAS[var2[var14]][var15]] > this.b_B) {
            if (cg.a(var5[0], var5[1], var5[2], var5[3], var16, var17, this.c_AS[this.a_AAS[var2[var14]][var15]], this.d_AS[this.a_AAS[var2[var14]][var15]])) {
              if (this.b_AImage != null && this.b_AImage[this.a_AAS[var2[var14]][var15]] != null) {
                var1.drawImage(this.b_AImage[this.a_AAS[var2[var14]][var15]], var16, var17, 20);
              } else if (this.a_AImage != null && this.a_AImage[this.a_AB[this.a_AAS[var2[var14]][var15]]] != null) {
                var1.drawRegion(
                  this.a_AImage[this.a_AB[this.a_AAS[var2[var14]][var15]]],
                  this.a_AS[this.a_AAS[var2[var14]][var15]],
                  this.b_AS[this.a_AAS[var2[var14]][var15]],
                  this.c_AS[this.a_AAS[var2[var14]][var15]],
                  this.d_AS[this.a_AAS[var2[var14]][var15]],
                  this.b_AB[this.a_AAS[var2[var14]][var15]],
                  var16,
                  var17,
                  20
                );
              }
            }
          } else if (this.a_AAB[var2[var14]][var15] >= var6
            && this.a_AAB[var2[var14]][var15] <= var19
            && this.b_AAB[var2[var14]][var15] >= var8
            && this.b_AAB[var2[var14]][var15] <= var21) {
            if (this.b_AImage != null && this.b_AImage[this.a_AAS[var2[var14]][var15]] != null) {
              var1.drawImage(this.b_AImage[this.a_AAS[var2[var14]][var15]], var16, var17, 20);
            } else if (this.a_AImage != null && this.a_AImage[this.a_AB[this.a_AAS[var2[var14]][var15]]] != null) {
              var1.drawRegion(
                this.a_AImage[this.a_AB[this.a_AAS[var2[var14]][var15]]],
                this.a_AS[this.a_AAS[var2[var14]][var15]],
                this.b_AS[this.a_AAS[var2[var14]][var15]],
                this.c_AS[this.a_AAS[var2[var14]][var15]],
                this.d_AS[this.a_AAS[var2[var14]][var15]],
                this.b_AB[this.a_AAS[var2[var14]][var15]],
                var16,
                var17,
                20
              );
            }
          }
        }
      }

      var1.translate(-(var3 - var5[0]), -(var4 - var5[1]));
      var1.setClip(var10, var11, var12, var13);
    }
  }

  public final boolean a_Z(String var1) {
    this.b_String = var1;
    byte[] var2;
    if (at.j_Z) {
      var2 = cc.a_AB(var1, at.h_B);
    } else {
      var2 = cg.a_AB(var1);
    }

    this.a_V(null);
    if (var2 != null) {
      return this.a(var2);
    } else {
      System.out.println(var1 + " Không tìm thấy tệp");
      return false;
    }
  }

  public final void a(int var1, int var2, int var3, int var4) {
    this.a_S = (short)var1;
    this.b_S = (short)var2;
    this.c_S = (short)var3;
    this.d_S = (short)var4;
    if (this.a_S > this.e_S - var3) {
      this.a_S = (short)(this.e_S - var3);
    }

    if (this.a_S < 0) {
      this.a_S = 0;
    }

    if (this.b_S > this.f_S - var4) {
      this.b_S = (short)(this.f_S - var4);
    }

    if (this.b_S < 0) {
      this.b_S = 0;
    }

    if (this.a_Z && this.a_Image != null) {
      if (this.a_S - this.g_S >= this.m_S) {
        var3 = (var2 = (this.g_S + this.i_S) / this.a_B) + (this.a_S - this.g_S) / this.a_B - 1;
        var4 = this.h_S / this.b_B;
        int var5 = this.l_S - var4 % this.l_S;
        this.b(var2, var3, var5, var4);
        this.g_S = (short)(this.a_S / this.a_B * this.a_B);
      }

      if (this.a_S < this.g_S) {
        var2 = this.g_S / this.a_B - 1;
        var3 = this.a_S / this.a_B;
        var4 = this.h_S / this.b_B;
        int var18 = this.l_S - var4 % this.l_S;
        this.b(var3, var2, var18, var4);
        this.g_S = (short)(this.a_S / this.a_B * this.a_B);
      }

      if (this.b_S - this.h_S >= this.m_S) {
        var3 = (var2 = (this.h_S + this.j_S) / this.b_B) + (this.b_S - this.h_S) / this.b_B - 1;
        var4 = this.g_S / this.a_B;
        int var19 = this.k_S - var4 % this.k_S;
        this.c(var2, var3, var19, var4);
        this.h_S = (short)(this.b_S / this.b_B * this.b_B);
      }

      if (this.b_S < this.h_S) {
        var2 = this.h_S / this.b_B - 1;
        var3 = this.b_S / this.b_B;
        var4 = this.g_S / this.a_B;
        int var20 = this.k_S - var4 % this.k_S;
        this.c(var3, var2, var20, var4);
        this.h_S = (short)(this.b_S / this.b_B * this.b_B);
      }
    }
  }
}
