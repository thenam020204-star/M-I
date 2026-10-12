import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStore;

public final class bs {
  private static byte a_B;
  private byte b_B = 0;
  private String[] a_AString;
  private byte c_B;
  private byte d_B;
  private bx a_bx;
  private bx b_bx;
  private bx c_bx;
  private bx d_bx;
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private String a_String;
  private String[] b_AString;
  private String[] c_AString;
  private boolean b_Z;
  private byte[] a_AB;
  private String[] d_AString;
  private String[] e_AString;
  private byte e_B;
  private boolean c_Z;
  boolean a_Z = false;
  private byte f_B;
  private short a_S;
  private String[] f_AString;
  private String[] g_AString;
  private byte g_B;
  private Image d_Image;
  private Image e_Image;
  private Image f_Image;
  private Image g_Image;
  private Image h_Image;
  private Image i_Image;
  private Image j_Image;
  private Image k_Image;
  private bx[] a_Abx;
  private dc a_dc;
  private int a_I = 0;
  private byte h_B;
  private int b_I;

  public bs() {
    if (at.a_Z) {
      String[] var10000 = new String[]{"Bật/tắt nhạc", "Trở về"};
      if (cq.f_String != null && !cq.f_String.equals("")) {
        this.a_AString = new String[]{
          "Chơi mới", "Chơi tiếp", "Cài đặt", "Cộng đồng QQ", "Bảng xếp hạng", cq.f_String, "Game hay đề cử", "Hướng dẫn", "Giới thiệu", "Thoát game"
        };
      } else {
        this.a_AString = new String[]{
          "Chơi mới", "Chơi tiếp", "Cài đặt", "Cộng đồng QQ", "Bảng xếp hạng", "Trung tâm game QQ", "Hướng dẫn", "Giới thiệu", "Thoát game"
        };
      }
    } else {
      this.a_AString = new String[]{"Chơi mới", "Chơi tiếp", "Trung tâm game QQ", "Hướng dẫn", "Giới thiệu", "Thoát game"};
    }
  }

  public final void a() {
    a_B = 0;
    new ce(this);
  }

  private void c() {
    this.c_bx = null;
    this.a_bx = null;
    this.a_Image = null;
    this.b_Image = null;
    this.c_Image = null;
  }

  public final void b() {
    if (this.c_bx == null) {
      this.c_bx = new bx();
      this.c_bx.a("/av/long1.av");
    }

    this.c_B = 0;
    this.c_B = 20;
    InputStream var1 = Main.self.getClass().getResourceAsStream("/pic/start/start.av");
    if (this.a_bx == null) {
      this.a_bx = new bx();
      this.a_bx.a("/pic/start/start.av", 1);
    }

    if (var1 == null) {
      this.a_bx = null;
    }

    var1 = Main.self.getClass().getResourceAsStream("/pic/start/logo.av");
    if (this.b_bx == null) {
      this.b_bx = new bx();
      this.b_bx.a("/pic/start/logo.av", 1);
    }

    if (var1 == null) {
      this.b_bx = null;
    }

    this.a_Image = cc.a("/pic/menuBack.jpg");
    String var10001 = "/pic/hk2.gf";
    this.b_Image = null;
    this.c_Image = cc.a("/pic/D04.gf");
    if (this.d_Image == null) {
      this.d_Image = cc.a("/pic/topbj.gf");
    }

    if (this.f_Image == null) {
      this.f_Image = cc.a("/pic/D23.gf");
    }

    if (this.g_Image == null) {
      this.g_Image = cc.a_Image("/pic/number01.gf", 2);
    }

    if (this.h_Image == null) {
      this.h_Image = cc.a("/pic/D09.gf");
    }

    if (this.i_Image == null) {
      this.i_Image = cc.a_Image("/pic/arrow.gf", 2);
    }

    if (this.j_Image == null) {
      this.j_Image = cc.a("/pic/musicIcon.gf");
    }

    if (this.e_Image == null) {
      this.e_Image = cc.a("/pic/D08.gf");
    }

    if (this.k_Image == null) {
      this.k_Image = cc.a("/pic/D11.gf");
    }

    this.c_B = 70;
    if (at.a_String != null) {
      ck.a_V();
      if (ck.a_AImage == null) {
        ck.b_V();
      }

      d.a();
      d.b();
      if (o.f_AS == null) {
        o.d();
        o.e();
      }
    }

    String var4 = cg.a_String("/bin/about.bin");
    this.a_String = cg.a(var4, "help:", "helpEnd");
    this.b_AString = cg.a(this.a_String, "\r\n", 170);
    this.a_String = null;
    this.c_AString = cg.a(cq.a_String, "\r\n", 170);
    this.c_B = 90;
    this.b_Z = false;
    String[] var5;
    ak.a(var5 = RecordStore.listRecordStores(), "Lưu game");

    for (int var2 = 0; var5 != null && var2 < var5.length; var2++) {
      if (var5[var2].equals(o.n_String)) {
        this.b_Z = true;
        this.a_AB = cg.a_AB(this.a_AB, 1);
      } else if (var5[var2].equals(o.o_String)) {
        this.b_Z = true;
        this.a_AB = cg.a_AB(this.a_AB, 2);
      } else if (var5[var2].equals(o.p_String)) {
        this.b_Z = true;
        this.a_AB = cg.a_AB(this.a_AB, 3);
      }
    }

    if (this.a_AB != null) {
      this.a_AB = k.a(this.a_AB);

      for (int var6 = 0; var6 < this.a_AB.length; var6++) {
        if (this.a_AB[var6] == 1) {
          this.d_AString = cg.a(this.d_AString, "Tiến độ 1");
        } else if (this.a_AB[var6] == 2) {
          this.d_AString = cg.a(this.d_AString, "Tiến độ 2");
        } else if (this.a_AB[var6] == 3) {
          this.d_AString = cg.a(this.d_AString, "Tiến độ 3");
        }
      }
    }

    this.e_AString = this.a_AString;
    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      k.a_V();
    }

    this.c_B = 100;
  }

  public final void a(Graphics var1) {
    if (da.a_da.a_S % 3 == 0) {
      if (this.e_B == 0) {
        this.e_B = 2;
      } else {
        this.e_B = 0;
      }
    }

    Graphics var2 = var1;
    bs var8 = this;
    var1.setColor(0);
    var1.setClip(0, 0, da.a_da.b_S, da.a_da.c_S);
    var1.fillRect(0, 0, da.a_da.b_S, da.a_da.c_S);
    var1.setClip(0, 0, at.a_AS[0], at.a_AS[1]);
    if (da.a_da.a_S % 12 == 0) {
      this.a_I = 3;
    } else {
      this.a_I = 0;
    }

    if (a_B == 0) {
      if (!at.a_String.equals("D608") && at.a_AS[0] >= 176) {
        if (this.c_bx != null) {
          this.c_bx.d_S = (short)(15 + (at.a_AS[0] - 30) * this.d_B / 100);
          this.c_bx.e_S = (short)(at.a_AS[1] - 15);
          this.c_bx.a_V(3);
          this.c_bx.a(var1);
          if (da.a_da.a_S % 2 == 0) {
            this.c_bx.a(true);
          }
        }

        var1.setColor(16765232);
        if (this.d_B < 20) {
          vh.drawString(var1, "Đang tải dữ liệu bản đồ...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 40) {
          vh.drawString(var1, "Đang tải dữ liệu tài nguyên...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 70) {
          vh.drawString(var1, "Đang tải dữ liệu nhân vật...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 100) {
          vh.drawString(var1, "Đang tải dữ liệu giao diện...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        }
      } else {
        var1.setColor(16765232);
        if (this.d_B < 20) {
          vh.drawString(var1, "Đang tải dữ liệu bản đồ...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 40) {
          vh.drawString(var1, "Đang tải dữ liệu tài nguyên...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 70) {
          vh.drawString(var1, "Đang tải dữ liệu nhân vật...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        } else if (this.d_B < 100) {
          vh.drawString(var1, "Đang tải dữ liệu giao diện...", at.a_AS[0] / 2, at.a_AS[1] / 2, 33);
        }

        var1.setColor(16711680);
        var1.fillRect(20, at.a_AS[1] - 10, at.a_AS[0] - 40, 1);
        var1.setColor(16249886);
        var1.fillRect(20, at.a_AS[1] - 10, (at.a_AS[0] - 40) * this.d_B / 100, 1);
      }

      if (this.d_B < this.c_B) {
        this.d_B++;
      } else {
        this.d_B = this.c_B;
      }

      if (this.a_Z && this.d_B >= 100) {
        if (o.l_AString != null) {
          o.f_B = (byte)k.a(0, o.l_AString.length);
        }

        this.c_B = 0;
        this.d_B = 0;
        this.c_bx = null;
        if (!at.a_String.equals("N73")
          && !at.a_String.equals("N7610")
          && !at.a_String.equals("N5800")
          && !at.a_String.equals("E62")
          && !at.a_String.equals("W958C")) {
          k.a_V();
        }

        if (this.a_bx != null) {
          a_B = 6;
        } else {
          a_B = 1;
        }

        if (at.b_Z && ct.a_String != null) {
          Main.self.playMusic(ct.a_String, -1, o.g_B);
          return;
        }
      }
    } else if (a_B == 6) {
      if (this.a_bx != null) {
        k.a(var1, this.a_bx, at.a_AS[0] / 2, at.a_AS[1] / 2, true, 0);
        if (this.a_bx.c() >= this.a_bx.a_I(0) - 1) {
          a_B = 1;
          return;
        }
      }
    } else {
      if (a_B == 1 || a_B == 8) {
        if (this.a_bx != null) {
          k.a(var1, this.a_bx, at.a_AS[0] / 2, at.a_AS[1] / 2, true, 1);
        } else if (this.a_Image != null) {
          var1.drawImage(this.a_Image, at.a_AS[0] >> 1, at.a_AS[1] >> 1, 3);
        }

        if (this.b_bx != null) {
          k.a(var1, this.b_bx, at.a_AS[0], 0, true, 1);
        }

        vh.getHeight(cg.a_Font);
        short var14;
        if (this.b_Image == null) {
          var14 = (short)(at.a_AS[1] - 22);
          cg.a(var1, this.e_AString[this.b_B], at.a_AS[0] / 2, var14 + cg.b_S - 4, 16250611, 5667444, 33);
          var14 = (short)(var14 + 7);
        } else {
          if (at.a_AS[1] > 320) {
            var14 = (short)(at.a_AS[1] - 30);
          } else {
            var14 = (short)(at.a_AS[1] - 25);
          }

          if (at.a_String.equals("Dopod_S1")) {
            var14 = (short)(at.a_AS[1] + 5);
          }

          if (this.b_B == 3 || this.b_B == 4 || this.b_B == 5 || this.b_B == 6) {
            cg.a(var1, this.e_AString[this.b_B], at.a_AS[0] / 2, var14 - 10, 16250611, 5667444, 17);
          } else if (this.b_B < 3) {
            cg.a(var1, this.b_Image, this.b_Image.getWidth(), 20, this.b_B, at.a_AS[0] / 2, var14, 3, 0);
          } else {
            cg.a(var1, this.b_Image, this.b_Image.getWidth(), 20, this.b_B - 4, at.a_AS[0] / 2, var14, 3, 0);
          }
        }

        if (this.c_Image != null && a_B != 8) {
          cg.a(var1, this.c_Image, this.c_Image.getWidth() / 2, this.c_Image.getHeight(), 0, (at.a_AS[0] - 90) / 2 - 20 - this.a_I, var14, 3, 0);
          cg.a(var1, this.c_Image, this.c_Image.getWidth() / 2, this.c_Image.getHeight(), 1, (at.a_AS[0] + 90) / 2 + 20 + this.a_I, var14, 3, 0);
        }

        if (a_B == 8) {
          i.a(3);
          String var23 = "Vào trung tâm QQ Game sẽ đóng trò chơi hiện tại, chắc chắn vào?";
          cg.a("Vào trung tâm QQ Game sẽ đóng trò chơi hiện tại, chắc chắn vào?", at.a_AS[0] - 30);
          k.a(var1, var23, at.a_AS[0], at.a_AS[1]);
          if (at.a_String.equals("E6")
            || at.a_String.equals("E680")
            || at.a_String.equals("W958C")
            || at.a_String.equals("S1")
            || at.a_String.equals("N95")
            || at.a_String.equals("N5800")) {
            i.a(var1);
          }
        }

        return;
      }

      if (a_B == 7) {
        this.b(var1);
        int var27 = (at.a_AS[0] - 176) / 2;
        int var31 = (at.a_AS[1] - 208) / 2;
        String var22 = "Lưu ý: tải miễn phí (không gồm phí mạng), trong game sẽ thu một phần phí chức năng, mức phí cụ thể sẽ được hỏi xác nhận trong game!";
        this.b_I = 188 / cg.b_S;
        int var12 = this.b_I;
        if (this.f_AString != null) {
          if (this.f_AString.length <= this.b_I) {
            var12 = this.f_AString.length;
          } else {
            k.a(var1, null, null, var27 + 176 - 5, var31 + 10, (cg.b_S + 1) * this.b_I, this.f_AString.length, this.b_I, this.h_B);
          }
        }

        var31 += 10;
        if (this.f_AString != null) {
          for (int var34 = 0; var34 < var12; var34++) {
            if (var34 + var8.h_B == var8.g_B) {
              cg.a(var2, var8.f_AString[var34 + var8.h_B], at.a_AS[0] / 2, var31 + (cg.b_S + 1) * var34, 16711680, 16777215, 17);
            } else {
              var2.setColor(16777215);
              vh.drawString(var2, var8.f_AString[var34 + var8.h_B], at.a_AS[0] / 2, var31 + (cg.b_S + 1) * var34, 17);
            }
          }
        }

        var31 += var12 * cg.b_S;
        if (var8.a_dc == null) {
          var8.a_dc = new dc();
          var8.a_dc.a(var22, 166, null, 13695736);
          var8.a_dc.a(var27 + 4, var31 + 15, 166, 208 - var12 * cg.b_S - 25, (208 - var12 * cg.b_S - 25) / cg.b_S);
        }

        var8.a_dc.a(var2, var8.a_dc, 1, true, false);
        return;
      }

      if (a_B == 2) {
        this.b(var1);
        int var26 = at.a_AS[0] - 176;
        int var30 = at.a_AS[1] - 208;
        i.a(2);
        var1.setColor(16777215);
        int var11 = vh.getHeight(cg.a_Font);

        for (int var21 = 0; var8.b_AString != null && var21 < var8.b_AString.length; var21++) {
          vh.drawString(var2, var8.b_AString[var21], var26 / 2 + 3, var30 / 2 + 5 + var11 * var21 + var8.a_S, 20);
        }

        return;
      }

      if (a_B == 4) {
        this.b(var1);
        int var25 = at.a_AS[0] - 176;
        int var29 = at.a_AS[1] - 208;
        i.a(2);
        var1.setColor(16777215);
        int var10 = vh.getHeight(cg.a_Font);

        for (int var20 = 0; var8.c_AString != null && var20 < var8.c_AString.length; var20++) {
          vh.drawString(var2, var8.c_AString[var20], var25 / 2 + 3, var29 / 2 + 5 + var10 * var20, 20);
        }

        return;
      }

      if (a_B == 3) {
        this.b(var1);
        i.a(2);
        var1.setColor(12961221);
        int var9 = (at.a_AS[0] - 90 - 27) / 2;
        int var19 = at.a_AS[1] / 2;
        if (this.j_Image != null) {
          var1.drawImage(this.j_Image, 50, var19 - 20, 3);
        }

        int var24 = o.g_B / 10;
        if (this.d_bx != null) {
          this.d_bx.a(at.a_AS[0] >> 1, (at.a_AS[1] >> 1) - 20);
          this.d_bx.a(var1);
          if (da.a_da.a_S % 3 == 0) {
            this.d_bx.a(true);
          }

          var19 += 20;

          for (int var6 = 0; var6 < 10; var6++) {
            if (var6 < var24) {
              var2.setColor(16776412);
              var2.fillRect(var9 + var6 * 12, var19, 9, 5);
              var2.setColor(16773198);
              var2.fillRect(var9 + 1 + var6 * 12, var19 + 1, 8, 4);
            } else {
              var2.setColor(15132390);
              var2.fillRect(var9 + var6 * 12, var19, 9, 5);
              var2.setColor(12961221);
              var2.fillRect(var9 + 1 + var6 * 12, var19 + 1, 8, 4);
            }
          }
        } else {
          for (int var28 = 0; var28 < 10; var28++) {
            if (var28 < var24) {
              var2.setColor(16776412);
              var2.fillRect(var9 + var28 * 12, var19 - (var28 << 2), 9, 5 + (var28 << 2));
              var2.setColor(16773198);
              var2.fillRect(var9 + 1 + var28 * 12, var19 + 1 - (var28 << 2), 8, 4 + (var28 << 2));
            } else {
              var2.setColor(15132390);
              var2.fillRect(var9 + var28 * 12, var19 - (var28 << 2), 9, 5 + (var28 << 2));
              var2.setColor(12961221);
              var2.fillRect(var9 + 1 + var28 * 12, var19 + 1 - (var28 << 2), 8, 4 + (var28 << 2));
            }
          }
        }

        if (at.b_Z && var24 != 0) {
          cg.a(var2, o.g_B, at.a_AS[0] / 2, var19 + 5 + 4 + cg.b_S / 2, this.g_Image, 6, 7, 3);
        } else {
          var2.setColor(16777215);
          vh.drawString(var2, "Tắt", at.a_AS[0] / 2, var19 + 5 + 4, 17);
        }

        if (this.k_Image != null) {
          cg.a(
            var2,
            this.k_Image,
            this.k_Image.getWidth() / 2,
            this.k_Image.getHeight(),
            1,
            at.a_AS[0] / 2 - this.a_I + vh.stringWidth(cg.a_Font, "Thuộc tính"),
            var19 + 5 + 4 + cg.b_S / 2,
            3,
            0
          );
          cg.a(
            var2,
            this.k_Image,
            this.k_Image.getWidth() / 2,
            this.k_Image.getHeight(),
            0,
            at.a_AS[0] / 2 + this.a_I - vh.stringWidth(cg.a_Font, "Thuộc tính"),
            var19 + 5 + 4 + cg.b_S / 2,
            3,
            0
          );
        }

        return;
      }

      if (a_B == 5) {
        this.b(var1);
        int var3 = at.a_AS[0] - 176 >> 1;
        i.a(3);
        if (this.d_AString != null) {
          for (int var7 = 0; var7 < at.e_B; var7++) {
            int var4 = at.a_AS[1] - 208 >> 1;
            var2.setColor(16755968);
            if (o.c_Z) {
              var2.setColor(0);
              if (o.c_AB[var7] == 1) {
                if (var8.a_Abx != null && var8.a_Abx[var7] != null) {
                  var8.a_Abx[var7].d_S = (short)(var3 + 44);
                  var8.a_Abx[var7].e_S = (short)(var4 + 60 + var7 * 104);
                  if (var7 == var8.f_B) {
                    var8.a_Abx[var7].a_V(0);
                  } else {
                    var8.a_Abx[var7].a_V(4);
                  }

                  var8.a_Abx[var7].a(var2);
                  var8.a_Abx[var7].a(true);
                }

                var4 += 40;
                cg.a(var2, o.i_AString[var7], var3 + 176 - 10, var4 + 5 - 40 + var7 * 104, 3532779, 146244, 24);
                var4 += 20;
                if (var8.h_Image != null) {
                  var2.drawRegion(
                    var8.h_Image,
                    0,
                    var8.h_Image.getHeight() / 2 * (o.j_AS[var7] - 1),
                    var8.h_Image.getWidth(),
                    var8.h_Image.getHeight() / 2,
                    0,
                    var3 + 88,
                    var4 + var7 * 104,
                    3
                  );
                } else {
                  String var5 = "";
                  if (o.j_AS[var7] == 1) {
                    var5 = "Quinn";
                  } else if (o.j_AS[var7] == 2) {
                    var5 = "Julie";
                  }

                  cg.a(var2, var5, var3 + 88, var4 - cg.b_S / 2 + var7 * 104, 16776937, 257041742, 24);
                }

                cg.a(var2, o.k_AS[var7], var3 + 113, var4 + var7 * 104, var8.g_Image, 6, 7, 3);
                var2.drawImage(var8.e_Image, var3 + 132, var4 + var7 * 104, 3);
                var4 += 20;
                cg.a(var2, o.l_AS[var7], var3 + 44, var4 + var7 * 104, var8.g_Image, 6, 7, 20);
                var2.drawRegion(var8.g_Image, 60, 0, 6, 7, 0, var3 + 44 + 24 + 1, var4 + var7 * 104, 20);
                var2.drawRegion(var8.g_Image, 60, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1, var4 + var7 * 104, 17);
                cg.a(var2, o.d_AB[var7], var3 + 44 + 24 + 6 + 2 + 12, var4 + var7 * 104, var8.g_Image, 6, 7, 24);
                cg.a(var2, o.e_AB[var7], var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12, var4 + var7 * 104, var8.g_Image, 6, 7, 24);
                if (o.d_AB[var7] < 10) {
                  var2.drawRegion(var8.g_Image, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2, var4 + var7 * 104, 20);
                }

                if (o.e_AB[var7] < 10) {
                  var2.drawRegion(var8.g_Image, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1, var4 + var7 * 104, 20);
                }

                cg.a(var2, o.f_AB[var7], var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12, var4 + var7 * 104, var8.g_Image, 6, 7, 24);
                if (o.f_AB[var7] < 10) {
                  var2.drawRegion(var8.g_Image, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10, var4 + var7 * 104, 20);
                }

                var2.drawRegion(var8.g_Image, 78, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12, var4 + var7 * 104, 20);
                cg.a(var2, o.g_AB[var7], var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12 + 6 + 12, var4 + var7 * 104, var8.g_Image, 6, 7, 24);
                if (o.g_AB[var7] < 10) {
                  var2.drawRegion(var8.g_Image, 0, 0, 6, 7, 0, var3 + 44 + 24 + 6 + 2 + 12 + 3 + 1 + 3 + 1 + 12 + 10 + 12 + 6, var4 + var7 * 104, 20);
                }
              } else {
                var2.setColor(16777215);
                vh.drawString(var2, "Tiến độ " + (var7 + 1) + ": Trống", at.a_AS[0] / 2, var4 + 52 + var7 * 104 - cg.b_S / 2, 17);
              }
            } else {
              var2.setColor(16777215);
              vh.drawString(var2, "Tiến độ " + (var7 + 1) + ": Trống", at.a_AS[0] / 2, var4 + 52 + var7 * 104 - cg.b_S / 2, 17);
            }
          }

          int var18 = at.a_AS[1] - 208 >> 1;
          if (var8.i_Image != null) {
            var2.drawRegion(var8.i_Image, 0, 0, 15, 15, 0, var3 + 10 + var8.a_I, var18 + 52 + var8.f_B * 104, 3);
            return;
          }
        } else {
          var1.setColor(16777215);
          vh.drawString(var1, "Chưa có bản lưu", at.a_AS[0] / 2, at.a_AS[1] / 2 - cg.b_S / 2, 17);
        }
      }
    }
  }

  protected final void a(int var1) {
    if (!this.c_Z) {
      if (a_B == 6) {
        if (var1 == 8) {
          a_B = 1;
          return;
        }
      } else if (a_B == 1) {
        if (var1 != 8 && var1 != b.a_S) {
          if (var1 == 2) {
            if (this.b_B > 0) {
              this.b_B--;
              return;
            }

            this.b_B = (byte)(this.e_AString.length - 1);
            return;
          }

          if (var1 == 5) {
            if (this.b_B < this.e_AString.length - 1) {
              this.b_B++;
              return;
            }

            this.b_B = 0;
            return;
          }
        } else {
          if (this.a_AString[this.b_B].equals("Chơi mới")) {
            o.a_B = 1;
            if (c.a_Z()) {
              Main.self.newGame();
              this.c_Z = false;
              this.c();
              return;
            }

            da.a_B = 6;
            return;
          }

          if (this.a_AString[this.b_B].equals("Chơi tiếp")) {
            if (!c.a_Z()) {
              da.a_B = 6;
              return;
            }

            a_B = 5;
            String[] var11 = RecordStore.listRecordStores();

            for (int var12 = 0; var11 != null && var12 < var11.length; var12++) {
              if (var11[var12].equals(o.m_String)) {
                o.c_Z = true;
                o.a_Z();
              }
            }

            if (o.c_Z) {
              this.a_Abx = new bx[o.j_AS.length];

              for (int var13 = 0; var13 < this.a_Abx.length; var13++) {
                this.a_Abx[var13] = new bx();
                this.a_Abx[var13].a("/av/chr" + o.j_AS[var13] + ".av");
              }
            }

            return;
          }

          if (this.a_AString[this.b_B].equals("Cài đặt")) {
            a_B = 3;
            if (this.d_bx == null) {
              this.d_bx = new bx();
              this.d_bx.a("/pic/D41.av");
              if (o.g_B > 0) {
                this.d_bx.a_V(1);
              } else {
                this.d_bx.a_V(0);
              }
            }

            if (Main.self.getClass().getResourceAsStream("/pic/D41.av") == null) {
              this.d_bx = null;
            }

            return;
          }

          if (this.a_AString[this.b_B].equals("Hướng dẫn")) {
            a_B = 2;
            return;
          }

          if (this.a_AString[this.b_B].equals("Giới thiệu")) {
            a_B = 4;
            return;
          }

          if (this.a_AString[this.b_B].equals("Thoát game")) {
            try {
              Main.self.destroyApp(true);
            } catch (MIDletStateChangeException var8) {
              var8.printStackTrace();
              return;
            }
          } else {
            if (this.a_AString[this.b_B].equals(cq.f_String)) {
              a_B = 8;
              return;
            }

            if (this.a_AString[this.b_B].equals("Cộng đồng QQ")) {
              bk.a(Main.self, da.a_da.a_Display, da.a_da, da.a_da.b_S, da.a_da.c_S, false, cg.a_I(cq.e_String), 120);
              bk.a();
              return;
            }

            if (this.a_AString[this.b_B].equals("Bảng xếp hạng")) {
              bk.a(Main.self, da.a_da.a_Display, da.a_da, da.a_da.b_S, da.a_da.c_S, false, cg.a_I(cq.e_String), 120);
              bk.b();
              return;
            }

            if (this.e_AString[this.b_B].equals("Game hay đề cử")) {
              if (this.f_AString == null) {
                this.f_AString = null;

                for (int var2 = 0; var2 < 5; var2++) {
                  String var10;
                  if ((var10 = Main.self.getAppProperty("Tj" + (var2 + 1) + "Url")) != null) {
                    int var3 = 0;

                    for (int var4 = 0; var2 < var10.length(); var4++) {
                      if (var10.substring(var4, var4 + 1).equals("&")) {
                        if (++var3 == 3) {
                          var3 = var4;
                          break;
                        }
                      }
                    }

                    if (var10.substring(var3 - 2, var3).equals("43")) {
                      this.f_AString = cg.a(this.f_AString, "Tải {Linh Thú Chi Vương}");
                    } else if (var10.substring(var3 - 2, var3).equals("48")) {
                      this.f_AString = cg.a(this.f_AString, "Tải {Hiên Viên Thương Khung Lục}");
                    } else if (var10.substring(var3 - 2, var3).equals("05")) {
                      this.f_AString = cg.a(this.f_AString, "Tải {Kiếm Phá Thương Khung}");
                    } else if (var10.substring(var3 - 2, var3).equals("03")) {
                      this.f_AString = cg.a(this.f_AString, "Tải {Thần Mã Giang Hồ}");
                    } else if (var10.substring(var3 - 2, var3).equals("31")) {
                      this.f_AString = cg.a(this.f_AString, "Tải {Sủng Vật Thiên Hạ 2}");
                    }

                    this.g_AString = cg.a(this.g_AString, var10);
                  }
                }
              }

              a_B = 7;
              return;
            }
          }
        }
      } else if (a_B == 2) {
        if (var1 == b.b_S) {
          a_B = 1;
          return;
        }

        if (var1 == 1) {
          if (this.a_S < 0) {
            this.a_S = (short)(this.a_S + cg.b_S);
            return;
          }
        } else if (var1 == 6 && cg.b_S + 5 + cg.b_S * this.b_AString.length + this.a_S > da.a_da.getHeight() - 3) {
          this.a_S = (short)(this.a_S - cg.b_S);
          return;
        }
      } else if (a_B == 4) {
        if (var1 == 8 || var1 == b.b_S || var1 == b.a_S) {
          a_B = 1;
          return;
        }
      } else if (a_B == 3) {
        if (var1 == 8 || var1 == b.b_S || var1 == b.a_S) {
          a_B = 1;
        }

        if (var1 == 5) {
          if (!at.b_Z) {
            at.b_Z = true;
          }

          if (o.g_B < 100) {
            o.g_B = (byte)(o.g_B + 10);
          }

          if (o.g_B >= 100) {
            o.g_B = 100;
          }

          if (this.d_bx != null) {
            this.d_bx.a_V(1);
          }

          if (Main.music != null) {
            Main.music.b(o.g_B);
          } else {
            Main.self.playMusic(ct.a_String, -1, o.g_B);
          }
        }

        if (var1 == 2) {
          if (o.g_B > 0) {
            o.g_B = (byte)(o.g_B - 10);
          }

          if (o.g_B <= 0) {
            if (this.d_bx != null) {
              this.d_bx.a_V(0);
            }

            o.g_B = 0;
          }

          Main.music.b(o.g_B);
          return;
        }
      } else if (a_B == 5) {
        if (var1 != 8 && var1 != b.a_S) {
          if (var1 == b.b_S) {
            a_B = 1;
            return;
          }

          if (var1 == 1) {
            if (this.f_B > 0) {
              this.f_B--;
              return;
            }
          } else if (var1 == 6 && this.d_AString != null && this.f_B < this.d_AString.length - 1) {
            this.f_B++;
            return;
          }
        } else if (this.b_Z) {
          Main.self.stopMusic();
          this.c_Z = true;
          da.a_B = 4;
          if (da.a_da.a_cd == null) {
            Runtime.getRuntime().freeMemory();
            da.a_da.a_cd = new cd();
            Runtime.getRuntime().freeMemory();
          }

          da.a_da.a_cd.a(this.d_AString[this.f_B]);
          if (!at.a_String.equals("N73")
            && !at.a_String.equals("N7610")
            && !at.a_String.equals("N5800")
            && !at.a_String.equals("E62")
            && !at.a_String.equals("W958C")) {
            this.c();
            return;
          }
        }
      } else if (a_B == 7) {
        if (var1 == b.b_S) {
          this.h_B = 0;
          this.g_B = 0;
          a_B = 1;
          return;
        }

        if (var1 == b.a_S || var1 == 8) {
          Main.self.stopThread();
          Main.self.a_da.a_cd = null;

          try {
            Main.self.platformRequest(this.g_AString[this.g_B]);
          } catch (Exception var6) {
            var6.printStackTrace();
          }

          try {
            Main.self.destroyApp(true);
            return;
          } catch (MIDletStateChangeException var5) {
            var5.printStackTrace();
            return;
          }
        }

        if (var1 == 1) {
          if (this.h_B == this.g_B && this.h_B > 0) {
            this.h_B--;
          }

          if (this.g_B > 0) {
            this.g_B--;
            return;
          }
        } else if (var1 == 6) {
          if (this.g_B < this.f_AString.length - 1) {
            this.g_B++;
          }

          if (this.h_B + this.b_I == this.g_B) {
            this.h_B++;
            return;
          }
        }
      } else if (a_B == 8) {
        if (var1 == b.b_S) {
          a_B = 1;
          return;
        }

        if (var1 == b.a_S) {
          Main.self.stopThread();
          Main.self.a_da.a_cd = null;

          try {
            Main.self.platformRequest(cq.g_String);
          } catch (Exception var7) {
            var7.printStackTrace();
          }

          try {
            Main.self.destroyApp(true);
            return;
          } catch (MIDletStateChangeException var9) {
            var9.printStackTrace();
          }
        }
      }
    }
  }

  public final void a(int var1, int var2) {
    int var3 = at.a_AS[0] - 176 >> 1;
    int var4 = at.a_AS[1] - 208 >> 1;
    if (a_B == 2 || a_B == 4 || a_B == 3 || a_B == 5 || a_B == 7) {
      if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
        if (ab.a(var1, var2, var3, var4 + 208 - 30, 30, 30)) {
          this.a(b.a_S);
        } else if (ab.a(var1, var2, var3 + 176 - 30, var4 + 208 - 30, 30, 30)) {
          this.a(b.b_S);
        }
      } else if (ab.a(var1, var2, var3, var4 + 208 - 30, 30, 30)) {
        this.a(b.b_S);
      } else if (ab.a(var1, var2, var3 + 176 - 30, var4 + 208 - 30, 30, 30)) {
        this.a(b.a_S);
      }
    }

    cm var7;
    if ((var7 = i.a(var1, var2)) != null) {
      if (var7.a_B == 2) {
        if (a_B == 8) {
          a_B = 1;
          return;
        }
      } else if (var7.a_B == 1 && a_B == 8) {
        Main.self.stopThread();
        Main.self.a_da.a_cd = null;

        try {
          Main.self.platformRequest(cq.g_String);
        } catch (Exception var5) {
          var5.printStackTrace();
        }

        try {
          Main.self.destroyApp(true);
          return;
        } catch (MIDletStateChangeException var6) {
          var6.printStackTrace();
        }
      }
    }
  }

  private void b(Graphics var1) {
    if (this.a_bx != null) {
      k.a(var1, this.a_bx, at.a_AS[0] / 2, at.a_AS[1] / 2, true, 1);
    } else if (this.a_Image != null) {
      var1.drawImage(this.a_Image, at.a_AS[0] >> 1, at.a_AS[1] >> 1, 3);
    }

    if (this.b_bx != null) {
      k.a(var1, this.b_bx, at.a_AS[0], 0, true, 1);
    }

    if (this.b_Image == null) {
      short var2 = (short)(at.a_AS[1] - 22);
      cg.a(var1, this.e_AString[this.b_B], at.a_AS[0] / 2, var2 + cg.b_S - 4, 16250611, 5667444, 17);
    } else {
      short var4;
      if (at.a_AS[1] > 320) {
        var4 = (short)(at.a_AS[1] - 30);
      } else {
        var4 = (short)(at.a_AS[1] - 25);
      }

      if (this.b_B == 3 || this.b_B == 4 || this.b_B == 5 || this.b_B == 6) {
        cg.a(var1, this.e_AString[this.b_B], at.a_AS[0] / 2, var4 - 10, 16250611, 5667444, 17);
      } else if (this.b_B < 3) {
        cg.a(var1, this.b_Image, this.b_Image.getWidth(), 20, this.b_B, at.a_AS[0] / 2, var4, 3, 0);
      } else {
        cg.a(var1, this.b_Image, this.b_Image.getWidth(), 20, this.b_B - 4, at.a_AS[0] / 2, var4, 3, 0);
      }
    }

    int var5 = at.a_AS[0] - 176;
    int var3 = at.a_AS[1] - 208;
    var1.setColor(2237991);
    var1.fillRoundRect(var5 / 2 - 1, var3 / 2 - 1, 179, 211, 8, 8);
    var1.setColor(16053217);
    var1.drawRoundRect(var5 / 2, var3 / 2, 176, 208, 8, 8);
    var1.setColor(6768205);
    var1.fillRect(var5 / 2 + 2, var3 / 2 + 2, 173, 205);
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      cg.a(var1, this.f_Image, this.f_Image.getWidth() / 2, this.f_Image.getHeight(), 0, var5 / 2 + 4, var3 / 2 + 208 - 4, 36, 0);
      cg.a(var1, this.f_Image, this.f_Image.getWidth() / 2, this.f_Image.getHeight(), 1, var5 / 2 + 176 - 4, var3 / 2 + 208 - 4, 40, 0);
    } else {
      cg.a(var1, this.f_Image, this.f_Image.getWidth() / 2, this.f_Image.getHeight(), 1, var5 / 2 + 4, var3 / 2 + 208 - 4, 36, 0);
      cg.a(var1, this.f_Image, this.f_Image.getWidth() / 2, this.f_Image.getHeight(), 0, var5 / 2 + 176 - 4, var3 / 2 + 208 - 4, 40, 0);
    }
  }
}
