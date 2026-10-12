import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class bo extends bg {
  private byte a_B = 0;
  private int[] a_AI = new int[2];
  private int[][] a_AAI = new int[2][2];

  public bo() {
    this.a_Vector = null;
    this.a_Vector = new Vector();
    this.a_AI[0] = 16711680;
    this.a_AI[1] = 0;
  }

  public bo(byte var1, int[][] var2) {
    this.a_AAI = var2;
    this.a_Vector = null;
    this.a_Vector = new Vector();
    this.a_B = 1;
  }

  public final void a(String var1) {
    this.a_Vector.addElement(var1);
    if (this.g_I < 0) {
      this.g_I = 0;
    }
  }

  public final void a(Graphics var1) {
    int var2 = this.b_I + this.k_I + 2;
    int var3 = this.a_I + this.f_I;
    int var4;
    if ((var4 = this.h_I + this.i_I) > this.a_Vector.size()) {
      var4 = this.a_Vector.size();
    }

    for (int var5 = this.h_I; var5 < var4; var5++) {
      if (this.a_B == 0) {
        if (var5 == this.g_I) {
          var1.setColor(this.a_AI[0]);
        } else {
          var1.setColor(this.a_AI[1]);
        }

        vh.drawString(var1, (String)this.a_Vector.elementAt(var5), var3 + this.e_I / 2, var2, 17);
      } else if (this.a_B == 1) {
        if (var5 == this.g_I) {
          cg.b(var1, (String)this.a_Vector.elementAt(var5), var3 + this.e_I / 2, var2, this.a_AAI[0][0], this.a_AAI[0][1], 17);
        } else if (this.a_AAI[1][0] == this.a_AAI[1][1]) {
          var1.setColor(this.a_AAI[1][0]);
          vh.drawString(var1, (String)this.a_Vector.elementAt(var5), var3 + this.e_I / 2, var2, 17);
        } else {
          cg.b(var1, (String)this.a_Vector.elementAt(var5), var3 + this.e_I / 2, var2, this.a_AAI[1][0], this.a_AAI[1][1], 17);
        }
      }

      var2 += this.j_I;
    }
  }

  public final void a_V() {
    this.a_Vector.removeAllElements();
    this.a_Vector = null;
  }
}
