import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class w {
  private String a_String;
  private int a_I;
  private int b_I;
  private int c_I;
  private int d_I;
  private int e_I;
  private boolean a_Z;
  private int f_I;
  private int g_I;
  private int h_I;
  private char[][] a_AAC;
  private char[] a_AC;
  private String b_String;
  private String c_String;
  private int i_I;
  private u a_u;

  public w(int var1, int var2, u var3) {
    this.a_String = "";
    this.a_Z = false;
    this.f_I = 0;
    this.a_AAC = new char[][]{
      {'0', '0', '+', '-', '=', '_', '*', '/', '\\', '|', ',', '.', '?', ':', ';', '\'', '"', '<', '>', '[', ']', '{', '}', '`'},
      {'1', '1', ' ', '#', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'},
      {'2', '2', 'a', 'b', 'c'},
      {'3', '3', 'd', 'e', 'f'},
      {'4', '4', 'g', 'h', 'i'},
      {'5', '5', 'j', 'k', 'l'},
      {'6', '6', 'm', 'n', 'o'},
      {'7', '7', 'p', 'q', 'r', 's'},
      {'8', '8', 't', 'u', 'v'},
      {'9', '9', 'w', 'x', 'y', 'z'}
    };
    this.a_AC = new char[]{
      ' ',
      '#',
      '~',
      '!',
      '@',
      '#',
      '$',
      '%',
      '^',
      '&',
      '*',
      '(',
      ')',
      '+',
      '-',
      '=',
      '_',
      '*',
      '/',
      '\\',
      '|',
      ',',
      '.',
      '?',
      ':',
      ';',
      '\'',
      '"',
      '<',
      '>',
      '[',
      ']',
      '{',
      '}',
      '`'
    };
    this.b_String = "Chọn";
    this.c_String = "Hủy";
    this.i_I = 12;
    this.a_u = var3;
    this.a_I = var1;
    this.i_I = var2;
    if (var1 == 65536) {
      this.a_I = 0;
    }

    if (this.a_I == 0) {
      this.e_I = 1;
    }
  }

  public final String a_String() {
    return this.a_String;
  }

  public final boolean a_Z() {
    return this.a_Z;
  }

  public final boolean a_Z(int var1, int var2) {
    char var3 = this.a_I == 2;
    boolean var4 = this.a_I == 0;
    if (var3) {
      if (this.b(var1, var2)) {
        return true;
      }
    } else if (var4) {
      if (var1 == 42) {
        if (!this.a_Z) {
          this.b();
          this.d_I = 0;
          if (this.a_String.length() >= this.i_I) {
            return true;
          }
        }

        this.a_Z = !this.a_Z;
        return true;
      }

      if (this.a_Z) {
        switch (var2) {
          case 1:
            this.f_I = this.f_I - this.g_I;
            if (this.f_I < 0) {
              var1 = (this.f_I + this.g_I) % this.g_I;
              var2 = this.a_AC.length % this.g_I;
              if (var1 < var2) {
                this.f_I = this.a_AC.length / this.g_I * this.g_I + var1;
              } else {
                this.f_I = (this.a_AC.length / this.g_I - 1) * this.g_I + var1;
              }
            }

            return true;
          case 2:
            this.f_I--;
            if (this.f_I < 0) {
              this.f_I = this.a_AC.length - 1;
            }

            return true;
          case 3:
          case 4:
          case 7:
          default:
            return false;
          case 5:
            this.f_I++;
            if (this.f_I >= this.a_AC.length) {
              this.f_I = 0;
            }

            return true;
          case 6:
            this.f_I = this.f_I + this.g_I;
            if (this.f_I >= this.a_AC.length) {
              this.f_I = this.f_I % this.g_I;
            }

            return true;
          case 8:
            String var6 = this.b_String;
            if (this.a_Z) {
              if (var6 == this.b_String) {
                this.a_Z = false;
                String var7 = this.a_String;
                var3 = this.a_AC[this.f_I];
                String var8 = var7 + var3;
                this.a_String = var8;
              } else if (var6 == this.c_String) {
                this.a_Z = false;
              }
            }

            return true;
        }
      }

      if (this.b(var1, var2)) {
        return true;
      }
    }

    return false;
  }

  private boolean b(int var1, int var2) {
    if (var1 == 35 && this.a_I == 0) {
      this.e_I++;
      if (this.e_I > 2) {
        this.e_I = 0;
      }

      return true;
    } else if ((var2 != 2 || var1 == 52) && var1 != -8 && var1 != -12 && var1 != -12345) {
      var2 = var1;
      w var6 = this;
      int var3 = 0;

      int var10000;
      while (true) {
        if (var3 >= var6.a_AAC.length) {
          var10000 = -1;
          break;
        }

        if (var6.a_AAC[var3][0] == var2) {
          var10000 = var3;
          break;
        }

        var3++;
      }

      var1 = var10000;
      if (var10000 < 0) {
        this.b();
        this.d_I = 0;
        return false;
      } else if (this.a_String.length() >= this.i_I) {
        return true;
      } else {
        switch (this.e_I) {
          case 0:
            char var8;
            if ((var8 = this.a_C(var1, 0)) >= '0' && var8 <= '9') {
              String var10 = this.a_String;
              String var11 = var10 + var8;
              this.a_String = var11;
            }
            break;
          case 1:
          case 2:
            if (this.d_I > 0) {
              if (this.c_I == var1) {
                this.b_I = (this.b_I + 1) % (this.a_AAC[this.c_I].length - 1);
                this.d_I = 14;
                return true;
              }

              this.b();
              this.d_I = 0;
              if (this.a_String.length() >= this.i_I) {
                return true;
              }
            }

            this.d_I = 16;
            this.c_I = var1;
            this.b_I = 1;
        }

        return true;
      }
    } else {
      String var4 = this.a_String;
      if (this.a_String.length() > 0) {
        String var5 = var4.substring(0, var4.length() - 1);
        this.a_String = var5;
      }

      return true;
    }
  }

  public final void a_V() {
    if (this.d_I > 0) {
      this.d_I--;
      if (this.d_I == 1) {
        this.b();
      }
    }
  }

  private void b() {
    if (this.d_I > 0) {
      String var1 = this.a_String;
      char var2 = this.a_C();
      var1 = var1 + var2;
      this.a_String = var1;
    }
  }

  private char a_C() {
    char var1 = this.a_C(this.c_I, this.b_I);
    if (this.e_I == 2 && var1 >= 'a' && var1 <= 'z') {
      var1 = (char)(var1 - 'a' + 65);
    }

    return var1;
  }

  private char a_C(int var1, int var2) {
    return this.a_AAC[var1][var2 + 1];
  }

  public final void a(Graphics var1, int var2, int var3, int var4) {
    if (System.currentTimeMillis() % 1500L < 1000L) {
      var1.setColor(1397123);
      var1.drawLine(var2, var3, var2, var3 + vh.getHeight(var1.getFont()) - 3);
    }

    if (this.d_I > 0) {
      char var5 = this.a_C();
      Font var6;
      int var7 = vh.charWidth(var6 = var1.getFont(), var5);
      int var8 = vh.getHeight(var6) + 1;
      var1.setColor(255);
      var1.fillRect(var2, var3, var7, var8);
      var1.setColor(16777215);
      var1.drawChar(var5, var2, var3 + var8 - 2, 36);
    }

    if (this.a_I == 0) {
      String var12 = "abc";
      if (this.e_I == 0) {
        var12 = "123";
      } else if (this.e_I == 1) {
        var12 = "abc";
      } else if (this.e_I == 2) {
        var12 = "ABC";
      }

      var1.setColor(16777215);
      int var13 = vh.stringWidth(var1.getFont(), var12);
      int var14 = var4 - var13 - 2;
      var2 = var3 + vh.getHeight(var1.getFont()) - 1;
      var3 = var13 + 4;
      var4 = vh.getHeight(var1.getFont()) + 2;
      var1.fillRoundRect(var14, var2, var3, var4, 4, 4);
      var1.setColor(0);
      var1.drawRoundRect(var14, var2, var3, var4, 4, 4);
      vh.drawString(var1, var12, var14 + 2, var2 + 1, 20);
    }
  }

  public final void a(Graphics var1) {
    if (this.a_Z) {
      int var2 = vh.getHeight(var1.getFont()) + 2;
      int var3 = vh.charWidth(var1.getFont(), '国') + 2;
      var2 = Math.max(var2, var3);
      var3 = this.a_u.a_I;
      this.g_I = var3 / var2 - 1;
      this.h_I = (this.a_AC.length + this.g_I - 1) / this.g_I;
      int var4 = (this.h_I + 1) * var2;
      var3 = (var3 - this.g_I * var2) / 2;
      int var5 = this.a_u.b_I - var4 - (var2 << 1);
      var1.setColor(4473924);
      var1.fillRect(var3, var5, this.g_I * var2, var4);
      var1.setColor(8947848);
      var1.drawRect(var3, var5, this.g_I * var2, var4);
      var1.setColor(11184810);
      var1.fillRect(var3 + this.f_I % this.g_I * var2, var5 + var2 + this.f_I / this.g_I * var2, var2, var2);
      if (var5 < 0) {
        var5 = 0;
      }

      var1.setColor(16777215);
      vh.drawString(var1, "Chọn ký hiệu:", var3 + 5, var5 + 1, 20);
      var5 += var2;
      var1.setColor(8947848);

      for (int var11 = 0; var11 <= this.h_I; var11++) {
        var1.drawLine(var3, var5 + var11 * var2, var3 + var2 * this.g_I, var5 + var11 * var2);
      }

      for (int var12 = 0; var12 <= this.g_I; var12++) {
        var1.drawLine(var3 + var12 * var2, var5, var3 + var12 * var2, var5 + var2 * this.h_I);
      }

      var4 = (var2 - vh.getHeight(var1.getFont()) + 1) / 2;
      var1.setColor(16777215);

      for (int var6 = 0; var6 < this.h_I; var6++) {
        for (int var7 = 0; var7 < this.g_I; var7++) {
          if (var6 * this.g_I + var7 < this.a_AC.length) {
            var1.drawChar(this.a_AC[var6 * this.g_I + var7], var3 + var7 * var2 + var2 / 2, var5 + var6 * var2 + var4, 17);
          }
        }
      }
    }
  }

  public final int a_I() {
    return this.d_I;
  }

  public w() {
  }

  public static e a_e() {
    return new e();
  }
}
