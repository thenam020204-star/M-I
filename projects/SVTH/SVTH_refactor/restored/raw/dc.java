import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class dc {
  private short[] a_AS;
  public String[] a_AString;
  private int[] b_AI;
  private short a_S = 15;
  private short b_S = 30;
  private short c_S = 0;
  private short d_S = 0;
  private short e_S = 3;
  int[] a_AI = new int[]{16711680, 65280, 255};
  String[] b_AString;
  String[][] a_AAString;

  public final void a(String var1, int var2, String var3, int var4) {
    if (var1 == null) {
      this.a_AS = cg.a_AS(this.a_AS, -1);
    } else {
      String[] var5 = cg.a(var1, var2);
      var2 = 0;
      if (this.a_AString != null) {
        var2 = this.a_AString.length;
      }

      for (int var7 = 0; var7 < var5.length; var7++) {
        this.a_AS = cg.a_AS(this.a_AS, var2 + var7);
        this.a_AString = cg.a(this.a_AString, var5[var7]);
        this.b_AI = cg.a_AI(this.b_AI, var4);
      }
    }
  }

  public final void a() {
    this.a_AS = null;
    this.a_AString = null;
    this.b_AI = null;
  }

  public final void a(int var1, int var2, int var3, int var4, int var5) {
    this.a_S = (short)var1;
    this.b_S = (short)var2;
    this.c_S = (short)var3;
    this.d_S = (short)var4;
    this.e_S = (short)var5;
  }

  public final void a(Graphics var1, dc var2, int var3, boolean var4, boolean var5) {
    if (var2 != null && var2.a_AString != null) {
      if (var4) {
        k.a(var1, var2.a_AString, this.a_S, this.b_S + 3, this.c_S, 10, this.e_S, 0, var2.b_AI, var5);
        return;
      }

      var1.setColor(0);

      for (int var6 = var3; var6 < var2.a_AString.length && var6 < var3 + this.e_S; var6++) {
        var1.setColor(var2.b_AI[var6]);
        if (!var5) {
          vh.drawString(var1, var2.a_AString[var6], this.a_S + 4, this.b_S + cg.b_S * (var6 - var3), 20);
        } else {
          vh.drawString(var1, var2.a_AString[var6], this.a_S + this.c_S / 2, this.b_S + cg.b_S * (var6 - var3), 17);
        }
      }

      if (var2.a_AString.length > this.e_S) {
        k.a(var1, null, null, this.a_S + this.c_S - 15, this.b_S, this.d_S, this.a_AString.length, this.e_S, var3);
      }
    }
  }

  public final void a(String var1, String var2, int var3) {
    String[] var4 = vh.wrapParts(var1, var2, var3);
    if (var4 != null) {
      for (int var5 = 0; var5 < var4.length; var5++) {
        this.a(var4[var5], var5);
      }
    }
  }

  private void a(String var1, int var2) {
    try {
      int var3 = 0;
      Vector var4 = new Vector();
      StringBuffer var5 = new StringBuffer();
      int var6 = 0;
      String var7 = null;
      int var8 = 0;

      while (var3 < var1.length()) {
        int var9 = var1.indexOf("<", var3);
        int var10 = var1.indexOf(">", var3);
        if (var9 == -1 && var10 == -1) {
          var5.append(var1.substring(var3));
          break;
        }

        var5.append(var1.substring(var3, var9));
        if (!var1.substring(var9 + 1, var10).equals("/")) {
          var6 = var9 - var8;
          var7 = var1.substring(var9 + 1, var10);
          var8 += ("<" + var7 + ">").length();
        } else {
          if (var6 != var9 - var8) {
            var4.addElement(new String[]{String.valueOf(var2), String.valueOf(var6), String.valueOf(var9 - var8), var7});
          }

          var8 += "</>".length();
        }

        var3 = var10 + 1;
      }

      String[][] var12 = null;
      if (var4.size() > 0) {
        var12 = new String[var4.size()][];
        var4.copyInto(var12);
      }

      for (int var13 = 0; var12 != null && var13 < var12.length; var13++) {
        this.a_AAString = cg.a(this.a_AAString, var12[var13]);
      }

      this.b_AString = cg.a(this.b_AString, var5.toString());
    } catch (Exception var11) {
      var11.printStackTrace();
    }
  }

  public final void a(Graphics var1, String var2, int var3, int var4, int var5, int var6) {
    var1.setColor(var6);
    vh.drawString(var1, var2, var4, var5, 20);

    for (int var9 = 0; this.a_AAString != null && var9 < this.a_AAString.length; var9++) {
      int var7 = cg.a_I(this.a_AAString[var9][0]);
      if (var3 == var7) {
        int var8 = cg.a_I(this.a_AAString[var9][2]);
        if (var3 == var7 && var8 <= var2.length()) {
          if (!this.a_AAString[var9][3].startsWith("0x") && !this.a_AAString[var9][3].startsWith("0X")) {
            var1.setColor(this.a_AI[cg.a_I(this.a_AAString[var9][3]) - 1]);
          } else {
            var1.setColor(cg.a_I(this.a_AAString[var9][3]));
          }

          var7 = cg.a_I(this.a_AAString[var9][1]);
          vh.drawString(var1, var2.substring(var7, var8), var4 + vh.stringWidth(cg.a_Font, this.b_AString[var3].substring(0, var7)), var5, 20);
        }
      }
    }
  }
}
