import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class av {
  public byte a_B;
  private Image a_Image;
  private bx a_bx;
  private boolean[] a_AZ;
  private byte b_B = 0;
  private String[] a_AString;
  private int[] a_AI;
  private short[] a_AS;
  private long a_J;
  boolean a_Z = false;
  private Image b_Image;

  public av() {
    this.a_AString = cq.a_AString;
    this.a_AS = cq.a_AS;
    this.a_AI = cq.a_AI;
    this.a_AS = cg.a_AS(this.a_AS, 2000);
    if (this.a_AString != null) {
      this.a_AZ = new boolean[this.a_AString.length];
    }

    this.b_Image = cc.a("/pic/musicIcon.gf");
    this.a_J = System.currentTimeMillis();
    i.a(3);
  }

  public final void a(Graphics var1) {
    var1.setColor(0);
    var1.setClip(0, 0, da.a_da.b_S, da.a_da.c_S);
    var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
    var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
    if (this.a_B == 1) {
      if (this.b_Image != null) {
        var1.drawImage(this.b_Image, at.a_AS[0] / 2, at.a_AS[1] / 2 - cg.b_S - 4, 33);
      }

      String var2 = "(Tắt nhạc để trò chơi chạy mượt hơn)";
      String[] var5 = cg.a("(Tắt nhạc để trò chơi chạy mượt hơn)", "\r\n", at.a_AS[0] - 20);
      var1.setColor(16777113);
      vh.drawString(var1, "Bật âm nhạc?", at.a_AS[0] / 2, at.a_AS[1] / 2 - cg.b_S, 17);
      var1.setColor(16711680);

      for (int var3 = 0; var5 != null && var3 < var5.length; var3++) {
        vh.drawString(var1, var5[var3], at.a_AS[0] / 2, at.a_AS[1] / 2 + vh.stringWidth(cg.a_Font, "MM") * var3, 17);
      }

      var1.setColor(16777215);
      if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        vh.drawString(var1, "Có", 2, at.a_AS[1] - 2, 36);
        vh.drawString(var1, "Không", at.a_AS[0] - 2, at.a_AS[1] - 2, 40);
        return;
      }

      vh.drawString(var1, "Không", 2, at.a_AS[1] - 2, 36);
      vh.drawString(var1, "Có", at.a_AS[0] - 2, at.a_AS[1] - 2, 40);
    } else if (this.a_B == 2) {
      var1.setColor(16749056);
      var1.setClip(0, 0, da.a_da.b_S, da.a_da.c_S);
      var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
      var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
      if (!this.a_Z) {
        byte var6 = this.b_B;
        if (!this.a_AZ[var6]) {
          if (this.a_AString[var6].toLowerCase().endsWith(".av")) {
            this.a_bx = new bx();
            this.a_bx.a(this.a_AString[var6]);
            this.a_bx.a(at.a_AS[0] / 2, at.a_AS[1] / 2);
          } else if (this.a_AString[var6].toLowerCase().endsWith(".png") || this.a_AString[var6].toLowerCase().endsWith(".gf")) {
            this.a_Image = cc.a(this.a_AString[var6]);
          }

          this.a_AZ[var6] = true;
        }

        if (!this.a_AString[var6].toLowerCase().endsWith(".png") && !this.a_AString[var6].toLowerCase().endsWith(".gf")) {
          if (this.a_AString[var6].toLowerCase().endsWith(".av") && this.a_bx != null) {
            int var4 = 0;
            if (this.a_AI != null) {
              var4 = this.a_AI[var6];
            }

            var1.setColor(var4);
            var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
            this.a_bx.a(var1);
            if (this.a_bx.c() < this.a_bx.a_I(0) - 1) {
              if (da.a_da.a_S % 3 == 0) {
                this.a_bx.a(false);
                return;
              }
            } else {
              if (this.b_B < this.a_AString.length - 1) {
                this.b_B++;
                this.a_J = System.currentTimeMillis();
                return;
              }

              var1.setColor(0);
              var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
              this.a_AString = null;
              this.a_Z = true;
              if (at.a_Z) {
                this.a_B = 1;
                return;
              }

              Main.self.showCover();
            }
          }
        } else if (this.a_Image != null) {
          var1.drawImage(this.a_Image, at.a_AS[0] / 2, at.a_AS[1] / 2, 3);
          if (System.currentTimeMillis() - this.a_J > (long)this.a_AS[this.b_B]) {
            if (this.b_B < this.a_AString.length - 1) {
              this.b_B++;
              this.a_J = System.currentTimeMillis();
              return;
            }

            var1.setColor(0);
            var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
            this.a_AString = null;
            this.a_Z = true;
            if (at.a_Z) {
              this.a_B = 1;
              return;
            }

            Main.self.showCover();
            return;
          }
        }
      }
    }
  }
}
