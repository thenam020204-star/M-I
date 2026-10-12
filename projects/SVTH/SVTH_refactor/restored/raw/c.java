import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class c {
  public byte a_B;
  private Image a_Image;
  public bx a_bx;
  private short[] b_AS;
  public short[] a_AS;

  public c() {
  }

  public static boolean a_Z() {
    if (!at.m_Z) {
      return true;
    } else {
      boolean var0 = true;
      if (!at.d_Z) {
        if (Runtime.getRuntime().totalMemory() == 8000000L) {
          var0 = false;
        }

        if (cq.a_Z && !b()) {
          var0 = false;
        }
      }

      return var0;
    }
  }

  private static boolean b() {
    boolean var0 = false;

    for (int var1 = 0; at.a_AString != null && var1 < at.a_AString.length; var1++) {
      try {
        Class.forName(at.a_AString[var1]);
        var0 = true;
        break;
      } catch (ClassNotFoundException var2) {
      }
    }

    return !var0;
  }

  public short[] a_AS() {
    return this.a_AS;
  }

  public c(Image var1, short[] var2) {
    this.a_B = 0;
    this.a_Image = var1;
    this.b_AS = var2;
    if (this.a_Image != null) {
      this.a_AS = new short[]{
        (short)(-this.a_Image.getWidth() / 2), (short)(-this.a_Image.getHeight()), (short)this.a_Image.getWidth(), (short)this.a_Image.getHeight()
      };
    }
  }

  public c(bx var1, short[] var2) {
    this.a_B = 1;
    this.a_bx = var1;
    if (this.a_bx != null) {
      this.a_AS = this.a_bx.a_co.a_AS();
    }

    this.b_AS = var2;
  }

  public void a(Graphics var1, int var2, int var3) {
    switch (this.a_B) {
      case 0:
        if (this.a_Image != null) {
          var1.drawImage(this.a_Image, var2, var3, 33);
          return;
        }
        break;
      case 1:
        if (this.a_bx != null) {
          this.a_bx.d_S = (short)var2;
          this.a_bx.e_S = (short)var3;
          this.a_bx.a(var1);
        }
    }
  }

  public void b(Graphics var1, int var2, int var3) {
    var1.setColor(16711680);
    switch (this.a_B) {
      case 0:
        for (byte var5 = 0; this.b_AS != null; var5 += 4) {
          if (var5 >= this.b_AS.length) {
            return;
          }

          var1.drawRect(var2 + this.b_AS[var5], var3 + this.b_AS[var5 + 1], this.b_AS[var5 + 2], this.b_AS[var5 + 3]);
        }
        break;
      case 1:
        if (this.b_AS != null) {
          for (byte var4 = 0; this.b_AS != null; var4 += 4) {
            if (var4 >= this.b_AS.length) {
              return;
            }

            var1.drawRect(var2 + this.b_AS[var4], var3 + this.b_AS[var4 + 1], this.b_AS[var4 + 2], this.b_AS[var4 + 3]);
          }
        } else if (this.a_bx != null) {
          this.a_bx.d_S = (short)var2;
          this.a_bx.e_S = (short)var3;
          this.a_bx.b(var1);
        }
    }
  }

  public short[] a(int var1, int var2, int var3) {
    short[] var4 = null;
    switch (this.a_B) {
      case 0:
        if (this.a_Image != null && this.b_AS != null) {
          var4 = new short[this.b_AS.length];
          System.arraycopy(this.b_AS, 0, var4, 0, this.b_AS.length);

          for (byte var5 = 0; var5 < var4.length; var5 += 4) {
            var4[var5] = (short)(var4[var5] + var1);
            var4[var5 + 1] = (short)(var4[var5 + 1] + var2);
          }
        }
      case 1:
        if (this.b_AS != null && (var3 == 1 || var3 == 0)) {
          var4 = new short[this.b_AS.length];
          System.arraycopy(this.b_AS, 0, var4, 0, this.b_AS.length);

          for (byte var12 = 0; var12 < var4.length; var12 += 4) {
            var4[var12] = (short)(var4[var12] + var1);
            var4[var12 + 1] = (short)(var4[var12 + 1] + var2);
          }
        } else if (this.a_bx != null) {
          boolean var6 = false;
          boolean var7 = false;
          co var8 = this.a_bx.a_co;
          var4 = this.a_bx.a_co.a(0, 0, var1, var2, var3, false);
        }
    }

    return var4;
  }

  public void a_V() {
    if (this.a_B == 1 && this.a_bx != null) {
      this.a_bx.a(true);
    }
  }
}
