import javax.microedition.lcdui.Graphics;

public final class df implements cx {
  public static byte a_B;
  public static int[] a_AI;
  private static String[] a_AString = new String[]{"06", "13", "15", "10", "10", "01", "02", "16"};
  private static String[] b_AString = new String[]{"01", "02", "03", "04", "05", "06", "07", "08"};
  private static int[] b_AI = new int[]{2, 4, 1, 4, 4, 3, 1, 1};
  private static String[] c_AString;
  private static String[] d_AString;
  private static String[] e_AString;
  public static boolean a_Z;
  byte b_B;
  private byte d_B;
  private byte e_B;
  private ch a_ch;
  private dc a_dc;
  private String[] f_AString = new String[]{"Vô Song Bí Kỹ", "Tiệm Bí Bảo", "Siêu trang bị", "Nhận thưởng"};
  private a a_a;
  private String c_String;
  private String d_String;
  private String[] g_AString;
  static boolean b_Z = false;
  static byte c_B;
  public static boolean c_Z = false;
  public static boolean d_Z = false;
  static int a_I = 0;
  private int b_I = 0;
  public static String a_String = "";
  public static String b_String = "";

  public df() {
    a_B = 0;
    this.b_B = 0;
    this.d_B = 0;
  }

  public static void a(String var0) {
    da.a_da.a_cd.a_df = new df();
    da.a_da.a_cd.b_B = 7;
    String var1;
    String[][] var2 = cg.a_AAString(var1 = cg.a_String(var0), "smsLib:", "smsLibEnd", null, "\t");
    c_AString = cg.a_AString(var1, "sureNote:", "sureNoteEnd", "\t");
    if (d_AString == null) {
      for (int var3 = 0; var2 != null && var3 < var2.length; var3++) {
        a_AI = cg.a_AI(a_AI, cg.a_I(var2[var3][0]));
        d_AString = cg.a(d_AString, var2[var3][1]);
        e_AString = cg.a(e_AString, var2[var3][2]);
      }
    }
  }

  public final void a(Graphics var1) {
    i.a(3);
    var1.setColor(4077616);
    var1.fillRect(0, 0, at.a_AS[0], at.a_AS[1]);
    var1.setColor(15919841);
    var1.fillRect(0, 0, at.a_AS[0], 30);
    var1.setColor(0);
    var1.setStrokeStyle(0);
    var1.drawLine(0, 29, at.a_AS[0], 29);
    var1.setColor(0);
    vh.drawString(var1, "Dịch vụ ưu đãi", at.a_AS[0] >> 1, 5, 17);
    var1.setColor(15919841);
    var1.fillRect(0, at.a_AS[1] - 30, at.a_AS[0], 30);
    var1.setColor(0);
    var1.setStrokeStyle(0);
    var1.drawLine(0, at.a_AS[1] - 30, at.a_AS[0], at.a_AS[1] - 30);
    var1.setColor(0);
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      vh.drawString(var1, "OK", 3, at.a_AS[1] - 2, 36);
      vh.drawString(var1, "Trở về", at.a_AS[0] - 3, at.a_AS[1] - 2, 40);
    } else {
      vh.drawString(var1, "Trở về", 3, at.a_AS[1] - 2, 36);
      vh.drawString(var1, "OK", at.a_AS[0] - 3, at.a_AS[1] - 2, 40);
    }

    if (this.a_dc != null) {
      this.a_dc.a();
    } else {
      this.a_dc = new dc();
    }

    if (a_B == 8) {
      this.a_dc.a("Chi tiêu đủ 12 tệ‚có thể nhận phần thưởng sau:", at.a_AS[0] - 20, null, 16777215);
      this.a_dc.a("Vàng 88888 + toàn đội x2 năng lực khi chiến đấu (trừ HP, MP)", at.a_AS[0] - 20, null, 65280);
      if (a_I >= 12) {
        this.a_dc.a("Đã tiêu " + a_I + " tệ (có thể nhận)", at.a_AS[0] - 20, null, 16777215);
      } else {
        this.a_dc.a("Đã tiêu " + a_I + " tệ (chi tiêu chưa đủ 12 tệ)", at.a_AS[0] - 20, null, 16777215);
      }

      this.a_dc.a(10, 30, at.a_AS[0] - 10, at.a_AS[1] - 10 - 20, (at.a_AS[1] - 10 - 20) / cg.b_S);
      this.a_dc.a(var1, this.a_dc, 1, true, false);
    } else if (a_B == 0) {
      for (int var5 = 0; var5 < this.f_AString.length; var5++) {
        if (var5 == this.d_B) {
          cg.a(var1, this.f_AString[this.d_B], at.a_AS[0] / 2, at.a_AS[1] / 2 - 30 - 2 * cg.b_S + var5 * (cg.b_S + 1), 16773120, 15867651, 17);
        } else {
          var1.setColor(16777215);
          vh.drawString(var1, this.f_AString[var5], at.a_AS[0] / 2, at.a_AS[1] / 2 - 30 - 2 * cg.b_S + var5 * (cg.b_S + 1), 17);
        }
      }
    } else {
      if (a_B == 1 || a_B == 5) {
        for (int var2 = 0; var2 < d_AString.length; var2++) {
          if (var2 == this.b_B) {
            cg.b(var1, d_AString[var2], at.a_AS[0] / 2, at.a_AS[1] / 2 - 30 - 2 * cg.b_S + var2 * (cg.b_S + 1), 16773120, 15867651, 17);
          } else {
            var1.setColor(16777215);
            vh.drawString(var1, d_AString[var2], at.a_AS[0] / 2, at.a_AS[1] / 2 - 30 - 2 * cg.b_S + var2 * (cg.b_S + 1), 17);
          }
        }

        if (a_B == 5) {
          String[] var4 = new String[]{"Thanh toán qua ĐT", "Trả bằng Xu Tiêu Dao"};

          for (int var3 = 0; var3 < var4.length; var3++) {
            if (var3 == this.e_B) {
              cg.a(var1, var4[var3], at.a_AS[0] / 2, at.a_AS[1] / 2 - cg.b_S - 4 + var3 * (cg.b_S + 3), 16773120, 15867651, 17);
            } else {
              var1.setColor(16777215);
              vh.drawString(var1, var4[var3], at.a_AS[0] / 2, at.a_AS[1] / 2 - cg.b_S - 4 + var3 * (cg.b_S + 3), 17);
            }
          }

          return;
        }
      } else if (a_B != 6) {
        if (a_B == 2) {
          this.a_dc.a(c_AString[a_AI[this.b_B] - 1], at.a_AS[0] - 20, null, 16777215);
          this.a_dc.a(cq.c_String, at.a_AS[0] - 20, null, 65280);
          this.a_dc.a("Đã gửi thành công 0 tin‚cần gửi thêm 1 tin. Gửi không?", at.a_AS[0] - 20, null, 16777215);
          this.a_dc.a(10, 30, at.a_AS[0] - 10, at.a_AS[1] - 10 - 20, (at.a_AS[1] - 10 - 20) / cg.b_S);
          this.a_dc.a(var1, this.a_dc, 1, true, false);
          return;
        }

        if (a_B == 7) {
          this.a_ch.a(var1);
          return;
        }

        if (a_B == 3) {
          this.a_dc.a(e_AString[this.b_I - 1], at.a_AS[0] - 20, null, 16777215);
          this.a_dc.a("Mua thành công‚nhớ lưu game kịp thời!", at.a_AS[0] - 20, null, 16777215);
          this.a_dc.a(10, 30, at.a_AS[0] - 10, at.a_AS[1] - 10 - 20, (at.a_AS[1] - 10 - 20) / cg.b_S);
          this.a_dc.a(var1, this.a_dc, 1, true, false);
        }
      }
    }
  }

  protected final void a(int var1) {
    k.b();
    if (a_B == 1) {
      if (var1 != 8 && var1 != b.a_S) {
        if (var1 == b.b_S) {
          this.d_B = 0;
          a_B = 0;
          return;
        }

        if (var1 == 1) {
          if (this.b_B > 0) {
            this.b_B--;
            return;
          }
        } else if (var1 == 6 && this.b_B < d_AString.length - 1) {
          this.b_B++;
          return;
        }
      } else {
        boolean var2 = false;
        if (a_AI[this.b_B] == 2) {
          if (c_B >= 4) {
            var2 = true;
          }
        } else if (a_AI[this.b_B] == 4) {
          if (c_Z) {
            var2 = true;
          }
        } else if (a_AI[this.b_B] == 5 && b_Z) {
          var2 = true;
        }

        if (var2) {
          da.a_da.a("Đã mua chức năng", 8);
          return;
        }

        if (at.q_Z) {
          this.e_B = 0;
          a_B = 5;
          return;
        }

        this.b(a_AI[this.b_B]);
      }
    } else if (a_B != 6) {
      if (a_B == 5) {
        if (var1 != 8 && var1 != b.a_S) {
          if (var1 == b.b_S) {
            a_B = 1;
            return;
          }

          if (var1 == 1) {
            if (this.e_B > 0) {
              this.e_B--;
              return;
            }
          } else if (var1 == 6 && this.e_B <= 0) {
            this.e_B++;
            return;
          }
        } else if (this.e_B == 0) {
          this.b(a_AI[this.b_B]);
          return;
        }
      } else if (a_B == 2) {
        if (var1 == b.a_S) {
          this.b(a_AI[this.b_B]);
          return;
        }

        if (var1 == b.b_S) {
          a_B = 1;
          return;
        }
      } else {
        if (a_B == 7) {
          this.a_ch.a_V(var1);
          return;
        }

        if (a_B == 3) {
          if (var1 == b.a_S || var1 == b.b_S) {
            a_B = 1;
            return;
          }
        } else if (a_B == 0) {
          if (var1 != 8 && var1 != b.a_S) {
            if (var1 == b.b_S) {
              b();
              return;
            }

            if (var1 == 6) {
              if (this.d_B < this.f_AString.length - 1) {
                this.d_B++;
                return;
              }
            } else if (var1 == 1 && this.d_B > 0) {
              this.d_B--;
              return;
            }
          } else {
            if (this.d_B == 0) {
              this.b_B = 0;
              a_B = 1;
              return;
            }

            if (this.d_B == 3) {
              if (!a_Z) {
                a_B = 8;
                return;
              }

              da.a_da.a("Đã nhận", 8);
              return;
            }

            if (this.d_B == 1) {
              b();
              da.a_da.a_cd.a("买道具", 8, 1);
              return;
            }

            if (this.d_B == 2) {
              b();
              da.a_da.a_cd.a("买装备", 8, 1);
              return;
            }
          }
        } else if (a_B == 8) {
          if (var1 == b.a_S) {
            if (a_I >= 12) {
              o.a_I += 88888;
              o.b_I = 2;
              a_Z = true;
              a_B = 0;
              da.a_da.a("Nhận thành công", 8);
              return;
            }

            da.a_da.a("Chi tiêu chưa đạt 12 tệ", 8);
            return;
          }

          if (var1 == b.b_S) {
            a_B = 0;
          }
        }
      }
    }
  }

  public final void b(int var1) {
    this.b_I = var1;
    this.a(0, null);
  }

  private void a() {
    int var1 = this.b_I;
    if (this.b_I == 1) {
      o.a_I += 8888;
      a_B = 3;
    } else if (var1 == 5) {
      b_Z = true;
      a_B = 3;
    } else if (var1 == 2) {
      c_B = 4;
      a_B = 3;
    } else if (var1 == 3) {
      cg.a_AAString(cg.a_String("/bin/team.bin"), "upgrade:", "upgradeEnd", null, "\t");
      bl var5 = o.a_Abl[0];

      for (int var2 = 0; var2 < 5; var2++) {
        var5.a_AI[0]++;
        var5.a_AI[15] = var5.a_AI[2];
        int var3 = var5.a_AI[0] + 1;
        var5.a_AI[2] = var3 * var3 * var3 / 2 + var3 * var3;
        var5.a_AI[1] = 0;
        var5.a_AI[16] = var5.a_AI[16] + 10;
        o.d(var5);
        var5.a_AI[3] = var5.a_AI[10];
        var5.a_AI[5] = var5.a_AI[11];

        for (int var4 = 0; o.a_Acv != null && var4 < o.a_Acv.length; var4++) {
          if (o.a_Acv[var4].a_B == o.e_S) {
            o.a_V(o.a_Acv[var4]);
            o.a_Acv[var4].a_AI[0]++;
            o.a_Acv[var4].a_AI[15] = o.a_Acv[var4].a_AI[14];
            var3 = o.a_Acv[var4].a_AI[0] + 1;
            o.a_Acv[var4].a_AI[14] = var3 * var3 * var3 / 2 + var3 * var3;
            o.a_Acv[var4].a_AI[13] = 0;
            o.a_Acv[var4].a_AI[1] = o.a_Acv[var4].a_AI[2];
            o.a_Acv[var4].a_AI[4] = o.a_Acv[var4].a_AI[5];
          }
        }
      }

      a_B = 3;
    } else if (var1 == 4) {
      c_Z = true;
      a_B = 3;
    } else if (var1 == 6) {
      d_Z = true;
      b();
      da.a_da.a_cd.a_ci.a(0);
    } else if (var1 == 8) {
      if (da.a_da.a_cd.a_bn != null) {
        b();
        da.a_da.a_cd.a_bn.a_V();
        da.a_da.a_cd.a_bn.b_V();
      }
    } else if (var1 == 7 && da.a_da.a_cd.a_bn != null) {
      b();

      for (int var6 = 0; var6 < da.a_da.a_cd.a_bn.a_Ae.length / 2; var6++) {
        if (da.a_da.a_cd.a_bn.a_Ae[var6] != null) {
          da.a_da.a_cd.a_bn.a_Ae[var6].f_I = 0;
          da.a_da.a_cd.a_bn.a_Ae[var6].a_bx.a_V(0);
          da.a_da.a_cd.a_bn.a_Ae[var6].a(da.a_da.a_cd.a_bn.a_Ae[var6].c_m);
          da.a_da.a_cd.a_bn.a_Ae[var6].f_Z = false;
          da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[3] = da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[10];
          da.a_da.a_cd.a_bn.a_Ae[var6].m_I = da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[10];
          da.a_da.a_cd.a_bn.a_Ae[var6].n_I = da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[11];
          da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[5] = da.a_da.a_cd.a_bn.a_Ae[var6].a_AI[11];
          da.a_da.a_cd.a_bn.a_Ae[var6].a_bx.j_Z = true;
        }
      }

      da.a_da.a_cd.a_bn.g();
      da.a_da.a_cd.a_bn.a_I = 0;
    }

    o.b(o.r_String);
  }

  private static void b() {
    da.a_da.a_cd.b_B = 0;
    d_AString = null;
    a_AI = null;
    c_AString = null;
    da.a_da.a_cd.a_df = null;
    if (da.a_da.a_cd.a_bn == null) {
      if (at.f_Z) {
        da.a_da.a_cd.g();
        da.a_da.a_cd.b();
      }

      da.a_da.a_cd.b_B = 1;
    } else {
      da.a_da.a_cd.b_B = 5;
    }
  }

  public final void a(int var1, int var2) {
    cm var3;
    if (da.a_da.a_cd.b_B == 7 && (var3 = i.a(var1, var2)) != null) {
      if (var3.a_B == 2) {
        this.a(b.b_S);
        return;
      }

      if (var3.a_B == 1) {
        this.a(b.a_S);
      }
    }
  }

  public final void a(int var1, String var2) {
    if (var1 == 0) {
      if (this.b_I == 6) {
        a_I += 4;
      } else {
        a_I += 2;
      }

      o.a_I += 1000;
      this.a();
    } else {
      System.out.println("Mua thất bại::");
      if (da.a_da.a_cd.a_bn != null) {
        b();
        if (da.a_da.a_cd.a_bn.b_I == 1) {
          da.a_da.a_cd.a_bn.e_V();
          return;
        }

        if (da.a_da.a_cd.a_bn.b_I == 2) {
          da.a_da.a_cd.a_bn.a_V();
          da.a_da.a_cd.a_bn.b_V();
          da.a_da.a_cd.a_bn.a_I = 24;
          return;
        }

        if (da.a_da.a_cd.a_bn.b_I == 3) {
          da.a_da.a_cd.a_bn.g();
          return;
        }
      } else {
        if (this.b_I == 6) {
          b();
          da.a_da.a_cd.a_AString = null;
          da.a_da.a_cd.d_B = -1;
          da.a_da.a_cd.a_ci.a(0);
          return;
        }

        a_B = 1;
      }
    }
  }
}
