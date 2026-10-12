import javax.microedition.lcdui.Graphics;

public final class bb {
  private byte b_B;
  private static byte c_B = 0;
  private static byte d_B = 1;
  private static byte e_B = 2;
  private static byte f_B = 3;
  public static byte a_B = 2;
  private boolean a_Z;
  private boolean b_Z;
  private int a_I;
  private int b_I = 30;

  public final void a(int var1) {
    this.b_B = (byte)var1;
  }

  public final void a_V() {
    this.a_Z = true;
    this.b_Z = false;
    if (this.b_B == 0) {
      this.a_I = 0;
    } else {
      if (this.b_B == d_B) {
        this.a_I = this.b_I;
      }
    }
  }

  public final boolean a_Z() {
    return this.a_Z || this.b_Z;
  }

  public final void a(Graphics var1) {
    if (this.b_B == 0) {
      if (this.a_Z) {
        da.a_da.a_cd.g_Z = false;
        var1.setColor(0);
        var1.fillRect(this.a_I, 0, 4, da.a_da.getHeight());
        var1.fillRect(this.a_I + 20, 0, 8, da.a_da.getHeight());
        var1.fillRect(this.a_I + 40, 0, 12, da.a_da.getHeight());
        var1.fillRect(this.a_I + 60, 0, 16, da.a_da.getHeight());
        var1.fillRect(this.a_I + 80, 0, da.a_da.getWidth(), da.a_da.getHeight());
        if (!at.a_String.equals("D608")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("E398")
          && !at.a_String.equals("L6")
          && !at.a_String.equals("L7")
          && !at.a_String.equals("K1")
          && !at.a_String.equals("N6230i")) {
          this.a_I += 20;
        } else {
          this.a_I += 30;
        }

        if (this.a_I > da.a_da.getWidth()) {
          this.a_Z = false;
          da.a_da.a_cd.g_Z = true;
        }

        return;
      }

      if (this.b_Z) {
        if (this.a_I >= 0) {
          this.b_Z = false;
          da.a_da.a_cd.a_V();
        }

        var1.setColor(0);
        var1.fillRect(this.a_I, 0, 4, da.a_da.getHeight());
        var1.fillRect(this.a_I + 20, 0, 8, da.a_da.getHeight());
        var1.fillRect(this.a_I + 40, 0, 12, da.a_da.getHeight());
        var1.fillRect(this.a_I + 60, 0, 16, da.a_da.getHeight());
        var1.fillRect(this.a_I, 0, da.a_da.getWidth(), da.a_da.getHeight());
        var1.fillRect(this.a_I + da.a_da.getWidth() + 20 - 16, 0, 16, da.a_da.getHeight());
        var1.fillRect(this.a_I + da.a_da.getWidth() + 40 - 12, 0, 12, da.a_da.getHeight());
        var1.fillRect(this.a_I + da.a_da.getWidth() + 60 - 8, 0, 8, da.a_da.getHeight());
        var1.fillRect(this.a_I + da.a_da.getWidth() + 80 - 4, 0, 4, da.a_da.getHeight());
        if (!at.a_String.equals("D608")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("E398")
          && !at.a_String.equals("L6")
          && !at.a_String.equals("L7")
          && !at.a_String.equals("K1")
          && !at.a_String.equals("N6230i")) {
          if (this.a_I + 20 > 0) {
            this.a_I = 0;
            return;
          }

          this.a_I += 20;
        } else if (this.a_I + 30 <= 0) {
          this.a_I += 30;
        } else {
          this.a_I = 0;
        }
      }
    } else if (this.b_B == d_B) {
      byte var2 = (byte)(da.a_da.b_S / this.b_I + 1);
      byte var3 = (byte)(da.a_da.c_S / this.b_I + 1);
      var1.setColor(0);
      if (this.a_Z) {
        da.a_da.a_cd.g_Z = false;

        for (int var4 = 0; var4 < var2; var4++) {
          for (int var5 = 0; var5 < var3; var5++) {
            var1.fillRect(var4 * this.b_I, var5 * this.b_I, this.a_I, this.a_I);
          }
        }

        if (!at.a_String.equals("D608")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("E398")
          && !at.a_String.equals("L6")
          && !at.a_String.equals("L7")
          && !at.a_String.equals("K1")
          && !at.a_String.equals("N6230i")) {
          this.a_I -= 3;
        } else {
          this.a_I -= 5;
        }

        if (this.a_I < 0) {
          this.a_Z = false;
          da.a_da.a_cd.g_Z = true;
          return;
        }
      } else if (this.b_Z) {
        for (int var6 = 0; var6 < var2; var6++) {
          for (int var7 = 0; var7 < var3; var7++) {
            var1.fillRect(var6 * this.b_I, var7 * this.b_I, this.a_I, this.a_I);
          }
        }

        if (!at.a_String.equals("D608")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("E398")
          && !at.a_String.equals("L6")
          && !at.a_String.equals("L7")
          && !at.a_String.equals("K1")
          && !at.a_String.equals("N6230i")) {
          this.a_I += 3;
        } else {
          this.a_I += 5;
        }

        if (this.a_I > this.b_I) {
          this.b_Z = false;
          this.a_I = 0;
          da.a_da.a_cd.a_V();
        }
      }
    }
  }
}
