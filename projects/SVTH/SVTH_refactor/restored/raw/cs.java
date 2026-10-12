import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cs {
  private int d_I;
  private int e_I;
  private int f_I;
  private int g_I;
  private String a_String;
  private String[] a_AString;
  private String b_String;
  private String[] b_AString;
  int[] a_AI;
  private int h_I = cg.b_S + 4;
  int a_I;
  int b_I;
  int c_I = 0;
  private Image a_Image;
  private Image b_Image;

  public cs(int var1, int var2, int var3, int var4) {
    this.d_I = var1;
    this.e_I = var2;
    this.f_I = var3;
    this.g_I = var4;
    this.a_I = this.g_I / this.h_I;
  }

  public final void a(String var1, String var2, String var3, String var4) {
    this.a_String = var1;
    this.a_AString = cg.a(var2, this.f_I - 20);
    this.b_I = this.b_I + this.a_AString.length + 2;
    this.b_String = var3;
    if (var4 != null && !var4.equals("-1")) {
      this.b_AString = cg.b_AString(var4, "|");
      this.b_I = this.b_I + this.b_AString.length;
    } else {
      this.b_AString = null;
    }
  }

  public final void a(Graphics var1) {
    var1.setClip(this.d_I, this.e_I, this.f_I, this.g_I);
    int var2 = this.d_I;
    int var3 = this.e_I + 2 - this.c_I * this.h_I;
    if (this.b_I > this.a_I) {
      k.a(var1, null, null, this.d_I + this.f_I - 15, this.e_I + 5, this.g_I - 10, this.b_I, this.a_I, this.c_I);
    }

    if (this.a_AI[0] == this.a_AI[1]) {
      var1.setColor(this.a_AI[0]);
      vh.drawString(var1, this.a_String, var2 + 4, var3 + 2, 20);
    } else {
      cg.b(var1, this.a_String, var2 + 4, var3, this.a_AI[0], this.a_AI[1], 20);
    }

    var3 += this.h_I;
    var1.setColor(16777215);

    for (int var4 = 0; var4 < this.a_AString.length; var4++) {
      vh.drawString(var1, this.a_AString[var4], var2 + 4, var3, 20);
      var3 += this.h_I;
    }

    if (this.b_AString != null) {
      if (this.a_AI[2] == this.a_AI[3]) {
        var1.setColor(this.a_AI[2]);
        vh.drawString(var1, this.b_String, var2 + 4, var3, 20);
      } else {
        cg.b(var1, this.b_String, var2 + 4, var3, this.a_AI[2], this.a_AI[3], 20);
      }
    }

    var3 += this.h_I;
    var1.setColor(16777215);
    if (this.b_AString != null) {
      System.out.println("sDetailStr" + this.b_AString);

      for (int var14 = 0; var14 < this.b_AString.length; var14++) {
        short[] var5;
        if ((var5 = cg.b_AS(this.b_AString[var14], ","))[0] == 1) {
          var2 = this.d_I + 4;
          o.d();
          if (o.a_AImage == null) {
            o.e();
          }

          if (o.a_AImage[o.d_I(var5[1])] != null) {
            var1.drawImage(o.a_AImage[o.d_I(var5[1])], var2 + o.a_AImage[o.d_I(var5[1])].getWidth() / 2, var3 - 2 + this.h_I / 2, 3);
            var2 += o.a_AImage[o.d_I(var5[1])].getWidth();
          }

          vh.drawString(var1, o.f_AString[o.d_I(var5[1])], var2, var3, 20);
          var2 = var2 + vh.stringWidth(cg.a_Font, o.f_AString[o.d_I(var5[1])]) + 5;
          vh.drawString(var1, "X" + var5[2], var2, var3, 20);
        } else if (var5[0] == 2) {
          var2 = this.d_I + 4;
          ck.a_V();
          if (ck.a_AImage == null) {
            ck.b_V();
          }

          if (ck.a_AImage[o.f_I(var5[1])] != null) {
            var1.drawImage(ck.a_AImage[o.f_I(var5[1])], var2 + o.a_AImage[o.d_I(var5[1])].getWidth() / 2, var3 - 2 + this.h_I / 2, 3);
            var2 += ck.a_AImage[o.f_I(var5[1])].getWidth();
          }

          vh.drawString(var1, ck.a_AString[o.f_I(var5[1])], var2, var3, 20);
          var2 = var2 + vh.stringWidth(cg.a_Font, ck.a_AString[o.d_I(var5[1])]) + 5;
          vh.drawString(var1, "X 1", var2, var3, 20);
        } else if (var5[0] == 3) {
          var2 = this.d_I + 4;
          if (this.b_Image == null) {
            this.b_Image = cc.a("/pic/D10.gf");
          }

          var1.drawImage(this.b_Image, var2 + this.b_Image.getWidth() / 2, var3 - 2 + this.h_I / 2, 3);
          vh.drawString(var1, "" + var5[1], var2 + this.b_Image.getWidth() + 5, var3, 20);
        } else if (var5[0] == 5) {
          var2 = this.d_I + 4;
          if (this.a_Image == null) {
            this.a_Image = cc.a("/icon/13.gf");
          }

          var1.drawImage(this.a_Image, var2 + this.a_Image.getWidth() / 2, var3 - 2 + this.h_I / 2, 3);
          vh.drawString(var1, "EXP", var2 + this.a_Image.getWidth() + 5, var3, 20);
          vh.drawString(var1, "" + var5[1], var2 + 10 + this.a_Image.getWidth() + vh.stringWidth(cg.a_Font, "Thuộc tính"), var3, 20);
        }

        var3 += this.h_I;
      }
    }

    var1.setClip(0, 0, da.a_da.getWidth(), da.a_da.getHeight());
  }
}
