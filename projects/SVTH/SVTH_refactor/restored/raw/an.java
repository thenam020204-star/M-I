import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;

public final class an {
  v a_v;
  public int a_I;
  private ak a_ak;
  private u a_u;
  public int b_I;
  private int c_I;
  public boolean a_Z;
  private int d_I;
  private Vector a_Vector;
  private cb a_cb;
  private Vector b_Vector;

  public an(u var1) {
    this.a_u = var1;
    this.a_Vector = new Vector();
    this.b_Vector = new Vector();
    this.a_ak = new ak(var1, this);
  }

  public final void a_V() {
    this.a_ak.a();
  }

  public final void b() {
    this.a_ak.c();
  }

  public final void c() {
    this.a_ak.b();
  }

  public final void a(Hashtable[] var1) {
    if (u.b_Z) {
      if (this.a_u.a_ai().c_Z) {
        this.a_u.a_ac().c();
      }

      ai.b_Vector.removeAllElements();
    }

    for (int var4 = 0; var4 < var1.length; var4++) {
      int var3 = -1;
      String var2 = a(var1[var4], "type");

      for (int var5 = 0; var5 < ag.a_AString.length; var5++) {
        if (ag.a_AString[var5].equals(var2)) {
          var3 = var5;
          break;
        }
      }

      switch (var3) {
        case 0:
          Hashtable var25 = var1[var4];
          this.a_u.a_ac().d(var25);
          break;
        case 1:
          Hashtable var24 = var1[var4];
          this.a_u.a_ac().b(var24);
          break;
        case 2:
          Hashtable var23 = var1[var4];
          this.a_u.a_ac().a(var23);
          break;
        case 3:
          Hashtable var22 = var1[var4];
          if (a(var22, "name").endsWith("_server")) {
            af.a_String = a(var22, "value");
          }

          this.a_u.a_Hashtable.put(var22.get("name"), var22.get("value"));
          break;
        case 4:
          this.a_u.a_ac().a_V();
          break;
        case 5:
          Hashtable var21 = var1[var4];
          String var34;
          if ((var34 = a(var21, "value")).length() > 0) {
            var34 = var34;

            try {
              byte[] var36;
              Image var37 = Image.createImage(var36 = az.a(var34), 0, var36.length);
              ai.b_Vector.addElement(var37);
            } catch (Exception var13) {
              var13.printStackTrace();
            }
          }

          this.a_u.a_ac().c(var21);
        case 6:
        default:
          break;
        case 7:
          Hashtable var19 = var1[var4];
          int var38 = a(var19, "id", -1);

          by var41;
          try {
            String var45 = a(var19, "code");
            String var42 = a(var19, "subCode");
            String var9 = a(var19, "msg");
            Hashtable var28;
            if (var45.length() == 0 && var42.length() == 0 && var9.length() == 0) {
              byte[] var20 = a(var19, "value").getBytes("UTF-8");
              byte var29 = 0;
              short var46 = ak.a(var20, 0);
              int var30 = var29 + 2;
              Hashtable var10000;
              if (var46 == 0) {
                var10000 = null;
              } else {
                Hashtable var11 = new Hashtable(var46);

                for (int var12 = 0; var12 < var46; var12++) {
                  byte var47 = var20[var30];
                  String var43 = new String(var20, ++var30, var47, "UTF-8");
                  int var32 = var30 + var47;
                  short var48 = ak.a(var20, var32);
                  int var33 = var32 + 2;
                  String var10 = new String(var20, var33, var48, "UTF-8");
                  var30 = var33 + var48;
                  var11.put(var43, var10);
                }

                var10000 = var11;
              }

              var28 = var10000;
            } else {
              (var28 = new Hashtable(3)).put("code", var45);
              var28.put("subCode", var42);
              var28.put("msg", var9);
            }

            var41 = new by(a(var28, "code", -1), a(var28, "subCode", -1), a(var28, "msg"));
          } catch (Exception var14) {
            var14.printStackTrace();
            var41 = new by(-1, -1, "");
          }

          label107:
          switch (var38) {
            case 1:
              switch (var41.a_I()) {
                case 1:
                  if (this.a_u.a_ai().d_I != 1) {
                    this.a_u.a_ai().a_V(1);
                  }

                  this.a_u.a_ai().b(var41.a_String());
                  break label107;
                case 2:
                  this.a_u.a_I();
                  this.a_u.a_ai().b(var41.a_String());
                default:
                  break label107;
              }
            case 2:
              switch (var41.a_I()) {
                case 0:
                  int var49;
                  if ((var49 = Integer.parseInt((String)this.a_u.a_Hashtable.get("_vertifyValue")))
                    == Integer.parseInt((String)this.a_u.a_Hashtable.get("_vCInterval"))) {
                    var49 = 0;
                  } else {
                    var49++;
                  }

                  this.a_u.a_Hashtable.put("_vertifyValue", String.valueOf(var49));
                  this.a_u.a_ai().e();
                  break label107;
                case 1:
                  this.a_u.a_ai().b(var41.a_String());
                  this.a_u.a_ai().a_V(5);
                default:
                  break label107;
              }
            default:
              if (u.b_Z) {
                this.a_u.a_ai().e();
                if (this.a_u.a_Displayable != null) {
                  this.a_u.a_Display.setCurrent(this.a_u.a_Displayable);
                }
              } else {
                this.a_ak.c();
              }
          }

          this.a(var38, var41);
          this.a_v.a(var38, var41);
          break;
        case 8:
          Hashtable var18 = var1[var4];
          this.a_u.a_ac().e(var18);
          break;
        case 9:
          Hashtable var17 = var1[var4];
          ac var27 = this.a_u.a_ac();
          int var40 = a(var17, "pixel", 0);
          var27.c_I += var40;
          if (var27.c_I > var27.b_I) {
            var27.a_V();
          }
          break;
        case 10:
          Hashtable var16 = var1[var4];
          ac var26 = this.a_u.a_ac();
          al var39;
          (var39 = new al()).a_Z = true;
          var39.b_String = a(var16, "href");
          r[] var44 = var26.a_Ar(a(var16, "addField"));
          var39.a(a(var16, "cache"));
          var26.a_al = var39;
          var26.a_an.a(var39, var44);
          this.a_u.a_ac().a_V("Đang kết nối... Vui lòng đợi!");
          return;
        case 11:
          Hashtable var15 = var1[var4];
          switch (a(var15, "id", -1)) {
            case 7:
              cb var6 = new cb();
              a(var15, "uid");
              a(var15, "age", 0);
              a(var15, "dataKey");
              a(var15, "dataValue");
              a(var15, "honor", 0);
              var6.b_I = a(var15, "level", 0);
              a(var15, "lost", 0);
              var6.a_String = a(var15, "nickName");
              var6.a_I = a(var15, "score", 0);
              a(var15, "sex", 0);
              a(var15, "status", 0);
              a(var15, "win", 0);
              this.a_Vector.addElement(var6);
              break;
            case 8:
            case 10:
              h var7 = new h();
              a(var15, "uid");
              a(var15, "age", 0);
              a(var15, "dataKey");
              a(var15, "dataValue");
              a(var15, "level", 0);
              a(var15, "nickName");
              a(var15, "score", 0);
              a(var15, "sex", 0);
              this.a_Vector.addElement(var7);
              break;
            case 9:
              dg var8;
              (var8 = new dg()).a_String = a(var15, "uid");
              a(var15, "age", 0);
              a(var15, "dataKey");
              a(var15, "dataValue");
              var8.b_String = a(var15, "nickName");
              var8.a_I = a(var15, "score", 0);
              a(var15, "sex", 0);
              a(var15, "time", 0);
              a(var15, "iType", 0);
              this.a_Vector.addElement(var8);
          }
      }
    }

    if (u.b_Z) {
      if (this.a_u.a_ai().c_Z) {
        this.a_u.a_ac().b_V();
      }

      if (this.a_u.a_ac().a_Vector.size() == 0) {
        this.a_u.a_ac().a_V("Mạng đang bận‚ vui lòng thử lại sau!");
      }
    }
  }

  public final void b(Hashtable[] var1) {
    this.a_Z = true;
    this.a_Vector.removeAllElements();
    if (u.b_Z && this.a_u.a_ai().c_Z) {
      this.a_u.a_ac().a(var1);
    }
  }

  public static String a(Hashtable var0, String var1) {
    String var2;
    if ((var2 = (String)var0.get(var1)) == null) {
      var2 = "";
    }

    return var2;
  }

  public static int a(Hashtable var0, String var1, int var2) {
    String var3;
    return (var3 = (String)var0.get(var1)) != null && var3.length() != 0 ? Integer.parseInt(var3) : var2;
  }

  public final void d() {
    if (this.a_I == 1) {
      if (this.a_Z) {
        this.b_I += 15;
        if (this.b_I > 100) {
          this.b_I = 100;
        }

        if (this.b_I == 100) {
          this.a_I = 0;
          return;
        }
      } else {
        if (this.b_I < 40 && this.c_I++ % 8 == 0) {
          this.b_I++;
        }

        this.d_I++;
        if (this.d_I > 300) {
          this.a_ak.b();
          this.a("Hết thời gian kết nối‚ vui lòng thử lại!");
          this.a_I = 0;
        }
      }
    }
  }

  public final void e() {
    this.a_I = 1;
    this.b_I = 0;
    this.c_I = 0;
    this.a_Z = false;
    this.d_I = 0;
  }

  public final void a(String var1) {
    if (u.b_Z) {
      this.a_u.a_ai().b(var1);
    } else {
      this.a_ak.c();
    }

    this.a_Z = true;
  }

  private static void a(al var0, int var1) {
    switch (var1) {
      case 1:
        var0.b_String = "invoke/pay.m?_type=1";
        return;
      case 2:
        var0.b_String = "invoke/pay.m?_type=2";
        return;
      case 10:
        var0.b_String = "index.m";
        return;
      case 12:
        var0.b_String = "scoreTop.m";
        return;
      case 16:
        var0.b_String = "profile.m";
        return;
      case 17:
        var0.b_String = "loginInput.m";
        return;
      case 18:
        var0.b_String = "verify.m";
        return;
      case 20:
        var0.b_String = "onlineList.m";
        return;
      case 100:
        var0.b_String = "help.m";
        return;
      case 101:
        var0.b_String = "login.m";
        return;
      case 102:
        var0.b_String = "logout.m";
      default:
        return;
      case 1001:
        var0.b_String = "invoke/getUserInfo.m";
        return;
      case 1002:
        var0.b_String = "invoke/getOppUserInfo.m";
        return;
      case 1003:
        var0.b_String = "invoke/getServTime.m";
        return;
      case 1504:
        var0.b_String = "invoke/getFeedInfo.m?_type=0";
        return;
      case 1505:
        var0.b_String = "invoke/getFeedInfo.m?_type=1";
        return;
      case 1506:
        var0.b_String = "invoke/getFeedInfo.m?_type=2";
        return;
      case 1507:
        var0.b_String = "invoke/getFeedInfo.m?_type=3";
        return;
      case 1508:
        var0.b_String = "invoke/getFeedInfo.m?_type=4";
        return;
      case 1509:
        var0.b_String = "invoke/joinChallenge.m";
        return;
      case 1510:
        var0.b_String = "invoke/giveUpChallenge.m";
        return;
      case 2002:
        var0.b_String = "invoke/getRankData.m?_type=2";
        return;
      case 2003:
        var0.b_String = "invoke/getRankData.m?_type=1";
        return;
      case 2005:
        var0.b_String = "invoke/getRankData.m?_type=3";
        return;
      case 2100:
        var0.b_String = "invoke/uploadData.m";
    }
  }

  public final void a(int var1) {
    al var2;
    a(var2 = new al(), var1);
    var2.a_Hashtable = new Hashtable();
    String var3 = (String)this.a_u.a_Hashtable.get("_uid");
    String var4;
    if (var1 == 18 && var3 != null && (var4 = (String)this.a_u.a_Hashtable.get(var3)) != null) {
      var2.a_Hashtable.put(var3, var4);
    }

    this.a(var2);
    this.a_ak.a(var2, null);
  }

  public final void a(al var1, r[] var2) {
    if (var1.b_String.length() != 0) {
      this.a_ak.a(var1, var2);
    }
  }

  private void a(al var1) {
    String var2 = (String)this.a_u.a_Hashtable.get("_uid");
    String var3 = (String)this.a_u.a_Hashtable.get("_sid");
    String var4 = (String)this.a_u.a_Hashtable.get("_a8");
    if (var2 == null) {
      var1.a_Hashtable.put("_uid", "");
    } else {
      var1.a_Hashtable.put("_uid", var2);
    }

    if (var3 == null) {
      var1.a_Hashtable.put("_sid", "");
    } else {
      var1.a_Hashtable.put("_sid", var3);
    }

    if (var4 == null) {
      var1.a_Hashtable.put("_a8", "");
    } else {
      var1.a_Hashtable.put("_a8", var4);
    }
  }

  protected final void a(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
    al var8;
    a(var8 = new al(), 2100);
    var8.a_Hashtable = new Hashtable();
    this.a(var8);
    var8.a_Hashtable.put("_score", String.valueOf(var1));
    var8.a_Hashtable.put("_oppUin", var2);
    var8.a_Hashtable.put("_oppScore", String.valueOf(var3));
    var8.a_Hashtable.put("_winner", String.valueOf(var4));
    var8.a_Hashtable.put("_dataKey", var5);
    var8.a_Hashtable.put("_dataValue", var6);
    var8.a_Hashtable.put("_extInfo", var7);
    this.a_ak.a(var8, null);
  }

  private void a(int var1, by var2) {
    switch (var1) {
      case 6:
        return;
      case 7:
        this.a_cb = null;
        Object var6;
        if (this.a_Vector.size() > 0 && (var6 = this.a_Vector.elementAt(this.a_Vector.size() - 1)) instanceof cb) {
          this.a_cb = (cb)var6;
          return;
        }
        break;
      case 8:
      case 10:
        if (this.a_Vector.size() > 0 && this.a_Vector.elementAt(this.a_Vector.size() - 1) instanceof h) {
          return;
        }
        break;
      case 9:
        this.b_Vector.removeAllElements();

        for (int var5 = 0; this.a_Vector != null && var5 < this.a_Vector.size(); var5++) {
          if (this.a_Vector.elementAt(var5) instanceof dg) {
            dg var7 = (dg)this.a_Vector.elementAt(var5);

            int var3;
            for (var3 = 0; var3 < this.b_Vector.size(); var3++) {
              dg var4 = (dg)this.b_Vector.elementAt(var3);
              if (var7.a_String.equals(var4.a_String)) {
                break;
              }
            }

            if (var3 == this.b_Vector.size()) {
              if (this.b_Vector.size() == 50) {
                this.b_Vector.removeElementAt(0);
              }

              this.b_Vector.addElement(var7);
            }
          }
        }
    }
  }

  public final cb a_cb() {
    return this.a_cb;
  }

  public final void f() {
    this.a_cb = null;
    this.b_Vector.removeAllElements();
  }
}
