import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class co {
  private String[] a_AString;
  private Image[] b_AImage;
  private Image[] c_AImage;
  private short[] a_AS;
  private short[] b_AS;
  private short[] c_AS;
  private short[] d_AS;
  private short[] e_AS;
  private byte[] a_AB;
  private short[][] a_AAS;
  private short[][] b_AAS;
  private short[][] c_AAS;
  private short[][] d_AAS;
  private short[][] e_AAS;
  private short[][] f_AAS;
  private short[][] g_AAS;
  private byte[][] a_AAB;
  private short[][] h_AAS;
  private byte c_B = 1;
  String a_String;
  public String b_String;
  byte a_B;
  private short[] f_AS;
  private boolean a_Z;
  private boolean b_Z;
  private byte[][] b_AAB;
  private Image[] d_AImage;
  public Image[] a_AImage;
  public byte b_B;
  private String c_String;

  public final void a(String var1, int[] var2, int[] var3) {
    for (int var4 = 0; var4 < this.a_AString.length; var4++) {
      if (this.a_AString[var4].equals(var1)) {
        this.b_AAB[var4] = be.a(this.b_AAB[var4], var2, var3);
        return;
      }
    }
  }

  public final void a_V() {
    this.b_AImage = new Image[this.a_AString.length];

    for (int var1 = 0; var1 < this.b_AImage.length; var1++) {
      this.b_AImage[var1] = Image.createImage(this.b_AAB[var1], 0, this.b_AAB[var1].length);
    }

    this.b_AAB = null;
    this.b();
  }

  private void b() {
    if (at.b_B > 0) {
      this.a_B = at.b_B;
      this.c_AImage = new Image[this.b_AS.length];

      for (int var1 = 0; var1 < this.b_AS.length; var1++) {
        if (this.b_AImage[this.a_AS[var1]] != null) {
          int var2 = 0;
          if (this.a_Z) {
            var2 += 2;
          }

          if (this.b_Z) {
            var2++;
          }

          if (at.b_B == 1) {
            if ((this.a_AB[var1] ^ var2) > 0) {
              this.c_AImage[var1] = Image.createImage(
                this.b_AImage[this.a_AS[var1]], this.b_AS[var1], this.c_AS[var1], this.d_AS[var1], this.e_AS[var1], this.a_AB[var1] ^ var2
              );
            }
          } else if (at.b_B == 2) {
            this.c_AImage[var1] = Image.createImage(
              this.b_AImage[this.a_AS[var1]], this.b_AS[var1], this.c_AS[var1], this.d_AS[var1], this.e_AS[var1], this.a_AB[var1] ^ var2
            );
          }
        }
      }

      if (at.b_B == 2) {
        this.b_AImage = null;
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

  public final int a_I() {
    return this.h_AAS != null ? this.h_AAS.length : 0;
  }

  private boolean a(byte[] var1) {
    ByteArrayInputStream var8 = new ByteArrayInputStream(var1);
    DataInputStream var9 = new DataInputStream(var8);

    try {
      byte[] var2 = new byte[2];
      var9.read(var2);
      if (var9.readByte() != this.c_B) {
        return false;
      } else {
        short var10;
        if ((var10 = var9.readShort()) > 0) {
          this.a_AString = new String[var10];

          for (int var3 = 0; var3 < var10; var3++) {
            this.a_AString[var3] = var9.readUTF();
          }
        }

        short var14;
        if ((var14 = var9.readShort()) > 0) {
          this.a_AS = new short[var14];
          this.b_AS = new short[var14];
          this.c_AS = new short[var14];
          this.d_AS = new short[var14];
          this.e_AS = new short[var14];
          this.a_AB = new byte[var14];
          byte[] var4 = new byte[4];
          var9.read(var4);

          for (int var5 = 0; var5 < var14; var5++) {
            if (var10 > 255) {
              this.a_AS[var5] = var9.readShort();
            } else {
              this.a_AS[var5] = (short)(var9.readByte() & 255);
            }

            if (var4[0] == 1) {
              this.b_AS[var5] = var9.readShort();
            } else {
              this.b_AS[var5] = (short)(var9.readByte() & 255);
            }

            if (var4[1] == 1) {
              this.c_AS[var5] = var9.readShort();
            } else {
              this.c_AS[var5] = (short)(var9.readByte() & 255);
            }

            if (var4[2] == 1) {
              this.d_AS[var5] = var9.readShort();
            } else {
              this.d_AS[var5] = (short)(var9.readByte() & 255);
            }

            if (var4[3] == 1) {
              this.e_AS[var5] = var9.readShort();
            } else {
              this.e_AS[var5] = (short)(var9.readByte() & 255);
            }

            this.a_AB[var5] = var9.readByte();
          }
        }

        short var18;
        if ((var18 = var9.readShort()) > 0) {
          byte[] var21 = new byte[3];
          var9.read(var21);
          this.a_AAS = new short[var18][];
          this.b_AAS = new short[var18][];
          this.c_AAS = new short[var18][];

          for (int var11 = 0; var11 < var18; var11++) {
            if ((var14 = var9.readShort()) > 0) {
              this.a_AAS[var11] = new short[var14];
              this.b_AAS[var11] = new short[var14];
              this.c_AAS[var11] = new short[var14];

              for (int var6 = 0; var6 < var14; var6++) {
                if (var21[0] == 1) {
                  this.a_AAS[var11][var6] = var9.readShort();
                } else {
                  this.a_AAS[var11][var6] = var9.readByte();
                }

                if (var21[1] == 1) {
                  this.b_AAS[var11][var6] = var9.readShort();
                } else {
                  this.b_AAS[var11][var6] = var9.readByte();
                }

                if (var21[2] == 1) {
                  this.c_AAS[var11][var6] = var9.readShort();
                } else {
                  this.c_AAS[var11][var6] = (short)(var9.readByte() & 255);
                }
              }
            }
          }
        }

        short var22;
        if ((var22 = var9.readShort()) > 0) {
          this.d_AAS = new short[var22][];
          this.e_AAS = new short[var22][];
          this.f_AAS = new short[var22][];
          this.g_AAS = new short[var22][];
          this.a_AAB = new byte[var22][];
          var2 = new byte[4];
          var9.read(var2);

          for (int var16 = 0; var16 < this.d_AAS.length; var16++) {
            short var24;
            if ((var24 = var9.readShort()) > 0) {
              this.d_AAS[var16] = new short[var24];
              this.e_AAS[var16] = new short[var24];
              this.f_AAS[var16] = new short[var24];
              this.g_AAS[var16] = new short[var24];
              this.a_AAB[var16] = new byte[var24];

              for (int var19 = 0; var19 < var24; var19++) {
                if (var2[0] == 1) {
                  this.d_AAS[var16][var19] = var9.readShort();
                } else {
                  this.d_AAS[var16][var19] = var9.readByte();
                }

                if (var2[1] == 1) {
                  this.e_AAS[var16][var19] = var9.readShort();
                } else {
                  this.e_AAS[var16][var19] = var9.readByte();
                }

                if (var2[2] == 1) {
                  this.f_AAS[var16][var19] = var9.readShort();
                } else {
                  this.f_AAS[var16][var19] = (short)(var9.readByte() & 255);
                }

                if (var2[3] == 1) {
                  this.g_AAS[var16][var19] = var9.readShort();
                } else {
                  this.g_AAS[var16][var19] = (short)(var9.readByte() & 255);
                }

                this.a_AAB[var16][var19] = var9.readByte();
              }
            }
          }
        }

        short var13;
        if ((var13 = var9.readShort()) > 0) {
          this.h_AAS = new short[var13][];
          byte var17 = var9.readByte();

          for (int var25 = 0; var25 < var13; var25++) {
            if ((var18 = var9.readShort()) > 0) {
              this.h_AAS[var25] = new short[var18];

              for (int var23 = 0; var23 < var18; var23++) {
                if (var17 == 1) {
                  this.h_AAS[var25][var23] = var9.readShort();
                } else {
                  this.h_AAS[var25][var23] = (short)(var9.readByte() & 255);
                }
              }
            }
          }
        }

        return true;
      }
    } catch (IOException var7) {
      var7.printStackTrace();
      return false;
    }
  }

  public final int a(int var1) {
    if (this.h_AAS == null || var1 >= this.h_AAS.length) {
      return 0;
    } else {
      return this.h_AAS[var1] != null ? this.h_AAS[var1].length : 0;
    }
  }

  public final short[] a_AS() {
    if (this.f_AS == null) {
      short var1 = 0;
      short var2 = 0;
      short var3 = 0;
      short var4 = 0;

      for (int var5 = 0; this.a_AAS != null && var5 < this.a_AAS.length; var5++) {
        for (int var6 = 0; this.a_AAS[var5] != null && var6 < this.a_AAS[var5].length; var6++) {
          if (var1 > this.a_AAS[var5][var6]) {
            var1 = this.a_AAS[var5][var6];
          }

          if (var3 < this.a_AAS[var5][var6] + this.d_AS[this.c_AAS[var5][var6]]) {
            var3 = (short)(this.a_AAS[var5][var6] + this.d_AS[this.c_AAS[var5][var6]]);
          }
        }

        for (int var7 = 0; this.b_AAS[var5] != null && var7 < this.b_AAS[var5].length; var7++) {
          if (var2 > this.b_AAS[var5][var7]) {
            var2 = this.b_AAS[var5][var7];
          }

          if (var4 < this.b_AAS[var5][var7] + this.e_AS[this.c_AAS[var5][var7]]) {
            var4 = (short)(this.b_AAS[var5][var7] + this.e_AS[this.c_AAS[var5][var7]]);
          }
        }
      }

      this.f_AS = new short[]{var1, var2, (short)(var3 - var1), (short)(var4 - var2)};
    }

    return this.f_AS;
  }

  public final short[] a(int var1, int var2, int var3, int var4, int var5, boolean var6) {
    short[] var7 = null;
    if (this.h_AAS != null && var1 < this.h_AAS.length && var1 >= 0 && this.h_AAS[var1] != null && var2 < this.h_AAS[var1].length && var2 >= 0) {
      short var9 = this.h_AAS[var1][var2];
      if (this.d_AAS != null && var9 < this.d_AAS.length && this.d_AAS[var9] != null) {
        for (int var10 = 0; var10 < this.d_AAS[var9].length; var10++) {
          short var8 = 0;
          if (var5 == 0) {
            var8 = 1;
          } else if (var5 == this.a_AAB[var9][var10]) {
            var8 = 1;
          }

          if (var8) {
            if (var7 == null) {
              var7 = new short[4];
            } else {
              short[] var11 = new short[var7.length + 4];
              System.arraycopy(var7, 0, var11, 0, var7.length);
              var7 = var11;
            }

            if (var6) {
              var7[var7.length - 4] = (short)(-(this.d_AAS[var9][var10] + this.f_AAS[var9][var10]) + var3);
            } else {
              var7[var7.length - 4] = (short)(this.d_AAS[var9][var10] + var3);
            }

            var7[var7.length - 3] = (short)(this.e_AAS[var9][var10] + var4);
            var7[var7.length - 2] = this.f_AAS[var9][var10];
            var8 = this.g_AAS[var9][var10];
            var7[var7.length - 1] = var8;
          }
        }
      }
    }

    return var7;
  }

  public final void a(String[] var1, String[] var2, int var3) {
    this.b_AAB = new byte[this.a_AString.length][];

    for (int var4 = 0; var4 < this.a_AString.length; var4++) {
      String var5 = this.a_AString[var4];
      if (var1 != null && var2 != null) {
        for (int var6 = 0; var1 != null && var6 < var1.length; var6++) {
          if (var1[var6].equals(this.a_AString[var4])) {
            var5 = var2[var6];
            break;
          }
        }
      }

      this.b_AAB[var4] = cc.a_AB(this.a_String + var5, var3);
      if (this.b_AAB[var4] == null) {
        System.out.println(this.a_String + var5 + " Không tìm thấy ");
      }
    }
  }

  public final void a(String var1, String[] var2, String[] var3, int[] var4, int[] var5, int var6, boolean var7, boolean var8, int var9) {
    this.c_AImage = null;
    this.a_B = 0;
    if (this.a_AString != null) {
      this.a_V(var1);
      this.a_Z = var7;
      this.b_Z = var8;
      this.b_AImage = new Image[this.a_AString.length];

      for (int var10 = 0; var10 < this.a_AString.length; var10++) {
        String var11 = this.a_AString[var10];
        if (var2 != null) {
          for (int var12 = 0; var2 != null && var12 < var2.length; var12++) {
            if (var2[var12].equals(this.a_AString[var10])) {
              var11 = var3[var12];
              break;
            }
          }
        } else if (var3 != null && var10 < var3.length && var3[var10] != null) {
          var11 = var3[var10];
        }

        if (var5 == null && var6 < 0) {
          this.b_AImage[var10] = cc.a_Image(this.a_String + var11, var9);
        } else {
          byte[] var13;
          if ((var13 = cc.a_AB(this.a_String + var11, var9)) != null) {
            if (var5 != null) {
              var13 = be.a(var13, var4, var5);
            }

            if (var6 >= 0) {
              var13 = be.a(var13, var6);
            }

            this.b_AImage[var10] = cg.a(var13);
          }
        }

        if (this.b_AImage[var10] == null) {
          System.out.println("Av loadImages không tìm thấy ảnh:" + this.a_String + var11);
        }
      }

      this.b();
    }
  }

  public final void a(Graphics var1, int var2, int var3, int var4, int var5, boolean var6) {
    this.a(var1, var2, var3, var4, var5, var6, false);
  }

  public final void a(Graphics var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7) {
    if (this.h_AAS != null && this.a_AAS != null && var2 < this.h_AAS.length && this.h_AAS[var2] != null && var3 < this.h_AAS[var2].length) {
      short var16 = this.h_AAS[var2][var3];
      short var10 = 0;

      try {
        for (int var11 = 0; this.a_AAS[var16] != null; var11++) {
          if (var11 >= this.a_AAS[var16].length) {
            return;
          }

          short var8;
          if (var6) {
            var8 = (short)(-this.a_AAS[var16][var11] + var4);
          } else {
            var8 = (short)(this.a_AAS[var16][var11] + var4);
          }

          short var9;
          if (var7) {
            var9 = (short)(-this.b_AAS[var16][var11] + var5);
          } else {
            var9 = (short)(this.b_AAS[var16][var11] + var5);
          }

          var10 = this.c_AAS[var16][var11];
          int var14 = 0;
          byte var12;
          if (var6) {
            var12 = 8;
            var14 += 2;
          } else {
            var12 = 4;
          }

          byte var13;
          if (var7) {
            var13 = 32;
            var14++;
          } else {
            var13 = 16;
          }

          this.c_String = this.a_AString[this.a_AS[var10]];
          if (at.b_B == 0) {
            if (this.b_AImage != null) {
              if (this.b_B == 0) {
                if (this.b_AImage[this.a_AS[var10]] != null) {
                  var1.drawRegion(
                    this.b_AImage[this.a_AS[var10]],
                    this.b_AS[var10],
                    this.c_AS[var10],
                    this.d_AS[var10],
                    this.e_AS[var10],
                    this.a_AB[var10] ^ var14,
                    var8,
                    var9,
                    var12 | var13
                  );
                }
              } else if (this.b_B == 1 && this.d_AImage != null) {
                var1.drawRegion(
                  this.d_AImage[this.a_AS[var10]],
                  this.b_AS[var10],
                  this.c_AS[var10],
                  this.d_AS[var10],
                  this.e_AS[var10],
                  this.a_AB[var10] ^ var14,
                  var8,
                  var9,
                  var12 | var13
                );
              } else if (this.b_B == 2 && this.a_AImage != null) {
                var1.drawRegion(
                  this.a_AImage[this.a_AS[var10]],
                  this.b_AS[var10],
                  this.c_AS[var10],
                  this.d_AS[var10],
                  this.e_AS[var10],
                  this.a_AB[var10] ^ var14,
                  var8,
                  var9,
                  var12 | var13
                );
              }
            }
          } else if (at.b_B == 1) {
            if (this.c_AImage != null && this.c_AImage[var10] != null) {
              var1.drawImage(this.c_AImage[var10], var8, var9, var12 | var13);
            } else if (this.b_AImage[this.a_AS[var10]] != null) {
              var1.drawRegion(
                this.b_AImage[this.a_AS[var10]],
                this.b_AS[var10],
                this.c_AS[var10],
                this.d_AS[var10],
                this.e_AS[var10],
                this.a_AB[var10] ^ var14,
                var8,
                var9,
                var12 | var13
              );
            }
          } else if (at.b_B == 2 && this.c_AImage != null && this.c_AImage[var10] != null) {
            var1.drawImage(this.c_AImage[var10], var8, var9, var12 | var13);
          }
        }
      } catch (Exception var15) {
        if (at.d_Z) {
          var15.printStackTrace();
          System.out.println(this.b_String + " của " + var3 + " lỗi khung hình");
          System.out.println(this.b_String + " của " + var3 + " Lỗi cắt khung‚tên ảnh=" + this.c_String);
          System.out
            .println(
              this.b_String
                + " của "
                + var3
                + " Lỗi khung hình‚phạm vi cắt x="
                + this.b_AS[var10]
                + ",y="
                + this.c_AS[var10]
                + ",w="
                + this.d_AS[var10]
                + ",h="
                + this.e_AS[var10]
            );
        }
      }
    }
  }

  public final boolean a_Z(String var1) {
    Object var4 = null;
    this.b_String = var1;
    byte[] var3;
    if (at.k_Z) {
      var3 = cc.a_AB(var1, at.i_B);
    } else {
      var3 = cg.a_AB(var1);
    }

    if (var3 == null) {
      System.out.println(var1 + " không tồn tại");
      return false;
    } else {
      this.a_V(null);
      if (var3 != null) {
        return this.a(var3);
      } else {
        System.out.println(var1 + "Không tìm thấy tệp");
        return false;
      }
    }
  }
}
