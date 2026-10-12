import javax.microedition.lcdui.Graphics;

public final class cl {
  private String a_String;
  String[] a_AString;
  private short b_S;
  private short c_S;
  private short d_S;
  private short e_S;
  private short f_S;
  short a_S;
  private byte a_B;
  private byte b_B;
  private boolean a_Z = false;
  private short g_S = 0;
  private short h_S = 4;

  public final void a(Graphics var1) {
    int var2 = vh.getHeight(cg.a_Font);
    if (this.a_S == 1) {
      var1.setColor(16711680);
      if (!at.a_String.equals("E2")
        && !at.a_String.equals("E398")
        && !at.a_String.equals("L6")
        && !at.a_String.equals("L7")
        && !at.a_String.equals("K1")
        && !at.a_String.equals("V8")) {
        vh.drawString(var1, "Bỏ qua", at.a_AS[0] - 2, at.a_AS[1] - 2, 40);
      } else {
        vh.drawString(var1, "Bỏ qua", 2, at.a_AS[1] - 2, 36);
      }

      var1.setClip(this.d_S, this.e_S, at.a_AS[0], this.b_S);
      if (da.a_da.a_S % 2 == 0) {
        this.f_S = (short)(this.f_S - da.a_da.a_cd.a_I);
      }

      if (this.f_S <= this.c_S) {
        da.a_da.a_cd.l_Z = false;
        da.a_da.a_cd.a_ci.a(5);
        this.a_AString = null;
      } else {
        for (int var4 = 0; this.a_AString != null && var4 < this.a_AString.length; var4++) {
          var1.setColor(16777215);
          vh.drawString(var1, this.a_AString[var4], at.a_AS[0] / 2, this.f_S + var2 * var4, 17);
        }
      }
    } else {
      var1.setColor(0);
      var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
      var1.setColor(16711680);
      if (!at.a_String.equals("E2")
        && !at.a_String.equals("E398")
        && !at.a_String.equals("L6")
        && !at.a_String.equals("L7")
        && !at.a_String.equals("K1")
        && !at.a_String.equals("V8")) {
        vh.drawString(var1, "Bỏ qua", at.a_AS[0] - 2, at.a_AS[1] - 2, 40);
      } else {
        vh.drawString(var1, "Bỏ qua", 2, at.a_AS[1] - 2, 36);
      }

      var1.setClip(this.d_S, at.a_AS[1] / 2 - this.h_S * var2 / 2, at.a_AS[0], var2 * this.h_S);
      if (this.a_B <= this.a_AString.length - 1) {
        if (this.a_B <= this.h_S - 1) {
          var1.setColor(16777215);
          vh.drawString(
            var1,
            this.a_AString[this.a_B].substring(0, this.b_B + 1),
            at.a_AS[0] / 2 - vh.stringWidth(cg.a_Font, this.a_AString[this.a_B]) / 2,
            at.a_AS[1] / 2 - this.h_S * var2 / 2 + var2 * this.a_B,
            20
          );
        } else {
          var1.setColor(16777215);
          vh.drawString(
            var1,
            this.a_AString[this.a_B].substring(0, this.b_B + 1),
            at.a_AS[0] / 2 - vh.stringWidth(cg.a_Font, this.a_AString[this.a_B]) / 2,
            at.a_AS[1] / 2 - this.h_S * var2 / 2 + var2 * (this.h_S - 1),
            20
          );
        }
      }

      if (this.a_Z) {
        for (int var3 = 0; this.a_AString != null && var3 < this.a_B; var3++) {
          var1.setColor(16777215);
          vh.drawString(var1, this.a_AString[var3], at.a_AS[0] / 2, this.g_S + at.a_AS[1] / 2 - this.h_S * var2 / 2 + var2 * var3, 17);
        }
      }

      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      if (this.b_B < this.a_AString[this.a_B].length() - 1) {
        if (da.a_da.a_S % da.a_da.a_cd.a_I == 0) {
          this.b_B++;
        }
      } else if (da.a_da.a_S % da.a_da.a_cd.a_I == 0) {
        this.a_Z = true;
        this.a_B++;
        if (this.a_B >= this.h_S) {
          this.g_S = (short)(this.g_S - var2);
        }

        this.b_B = 0;
      }

      if (this.a_B > this.a_AString.length - 1) {
        da.a_da.a_cd.l_Z = false;
        da.a_da.a_cd.a_ci.a(5);
        this.a_AString = null;
        this.a_Z = false;
        this.g_S = 0;
      }
    }
  }

  public final void a(int var1, int var2, int var3, int var4, int var5) {
    this.d_S = (short)var2;
    this.e_S = (short)var3;
    this.f_S = (short)(var4 + var3);
    this.a_S = (short)var5;
    String var6 = cg.a_String("/bin/story.bin");
    this.a_String = cg.a(var6, "Story" + var1 + ":", "Story" + var1 + "End");
    if (this.a_String.indexOf("&") != -1) {
      this.a_String = k.a(this.a_String, "&", o.c_AString[0]);
    }

    this.a_AString = cg.a(this.a_String, "\r\n", at.a_AS[0] - 10);
    this.a_String = null;
    this.b_S = (short)var4;
    this.c_S = (short)(-(this.a_AString.length * vh.getHeight(cg.a_Font)) + var3);
    this.a_B = 0;
    this.b_B = 0;
    if (this.a_S == 1) {
      da.a_da.a_cd.a_I = 2;
    } else {
      da.a_da.a_cd.a_I = 8;
    }
  }
}
