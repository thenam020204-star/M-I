import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public final class o {
  public static short a_S;
  public static byte a_B = 1;
  public static String a_String;
  public static short b_S;
  public static String b_String;
  private static byte k_B;
  public static byte b_B = 1;
  public static String c_String;
  public static short[] a_AS;
  public static byte[] a_AB;
  public static String d_String;
  public static String[] a_AString;
  public static String[] b_AString;
  public static short[] b_AS;
  public static byte c_B;
  static String e_String;
  public static String f_String;
  public static bl[] a_Abl;
  public static byte[] b_AB;
  public static short[][] a_AAS;
  public static short[][] b_AAS;
  public static ck[][] a_AAck;
  public static short c_S;
  private static byte l_B = 15;
  public static byte d_B = 0;
  public static int a_I;
  public static short[] c_AS;
  public static short[] d_AS;
  public static int[] a_AI;
  public static String[] c_AString;
  public static String[] d_AString;
  public static String[] e_AString;
  public static String g_String;
  public static String h_String;
  public static String i_String = "/av/boat.av";
  public static ao[] a_Aao;
  public static boolean a_Z;
  public static String j_String = null;
  public static String k_String = null;
  public static String l_String = null;
  public static String[] f_AString;
  public static short[] e_AS;
  public static short[] f_AS;
  private static short[] s_AS;
  private static short[] t_AS;
  private static short[] u_AS;
  public static short[] g_AS;
  public static String[] g_AString;
  public static short[] h_AS;
  public static short[] i_AS;
  private static String[] o_AString;
  public static Image[] a_AImage;
  public static String[] h_AString;
  public static short d_S;
  public static byte e_B;
  public static boolean b_Z = false;
  public static String m_String = "cwzw3save";
  public static String n_String = "cwzw3_save1";
  public static String o_String = "cwzw3_save2";
  public static String p_String = "cwzw3_save3";
  public static boolean c_Z = false;
  public static byte[] c_AB = new byte[]{0, 0, 0};
  public static String[] i_AString = new String[]{null, null, null};
  public static short[] j_AS = new short[]{0, 0, 0};
  public static short[] k_AS = new short[]{1, 1, 1};
  public static short[] l_AS = new short[]{1, 1, 1};
  public static byte[] d_AB = new byte[]{1, 1, 1};
  public static byte[] e_AB = new byte[]{1, 1, 1};
  public static byte[] f_AB = new byte[]{1, 1, 1};
  public static byte[] g_AB = new byte[]{1, 1, 1};
  private static byte[] h_AB = new byte[]{1, 1, 1};
  public static bl[] b_Abl;
  public static int b_I;
  public static String[][] a_AAString;
  private static String[][] f_AAString;
  private static String[][] g_AAString;
  public static String[][] b_AAString;
  public static String[][] c_AAString;
  public static String[][] d_AAString;
  public static String[][] e_AAString;
  public static bp[] a_Abp;
  public static short[] m_AS;
  public static short[] n_AS;
  public static short[] o_AS;
  private static String[][] h_AAString;
  public static String[] j_AString;
  public static String[] k_AString;
  public static boolean d_Z;
  public static cv[] a_Acv;
  private static short[] v_AS;
  private static int[] b_AI;
  private static au[] a_Aau;
  public static String q_String;
  public static short e_S = 1;
  public static String[] l_AString;
  public static byte f_B;
  public static String r_String = "cwzw3SmsSave";
  public static short[] p_AS;
  public static boolean[] a_AZ;
  public static byte g_B;
  public static int c_I;
  static byte h_B;
  static byte i_B;
  public static String[] m_AString;
  static byte j_B;
  public static short[][] c_AAS;
  static short[] q_AS;
  static short[] r_AS;
  public static String[] n_AString;
  static String s_String;

  public static void a_V() {
    a_AZ = new boolean[140];

    for (int var0 = 0; var0 < a_AZ.length; var0++) {
      a_AZ[var0] = false;
    }

    short[] var7 = new short[]{e_S};
    c_S = 0;

    for (int var1 = 0; var1 < var7.length; var1++) {
      short var2;
      c(a_bl(var2 = var7[var1]));
    }

    short[] var10 = new short[0];

    for (int var8 = 0; var8 < var10.length; var8++) {
      a_V(var10[var8], -1);
    }

    String var9;
    a_AAS = cg.a_AAS(var9 = cg.a_String("/bin/team.bin"), "item:", "end", "=");
    b_AAS = cg.a_AAS(var9, "bag:", "end", "=");

    for (int var11 = 0; var11 < a_Abl.length; var11++) {
      bl var13;
      if ((var13 = a_Abl[var11]).d_Z) {
        d(var13);
        var13.a_AI[3] = var13.a_AI[10];
        var13.a_AI[5] = var13.a_AI[11];
      }
    }

    if (b_AAS != null) {
      short[][] var12 = b_AAS;
      ck[][] var10000;
      if (b_AAS == null) {
        var10000 = null;
      } else {
        byte var3;
        ck[][] var14 = new ck[var3 = (byte)var12.length][];

        for (int var4 = 0; var4 < var3; var4++) {
          if (var12[var4] != null) {
            short var5 = (short)var12[var4].length;
            var14[var4] = new ck[var5];

            for (int var6 = 0; var6 < var5; var6++) {
              var14[var4][var6] = new ck(var12[var4][var6]);
            }
          }
        }

        var10000 = var14;
      }

      a_AAck = var10000;
    } else {
      b_AAS = new short[6][];
      a_AAck = new ck[6][];
    }

    a_I = cg.a_I(var9, "money");
    if (at.a_Z && at.b_Z) {
      if (at.b_Z) {
        System.out.println("true");
        cd.m();
        return;
      }

      System.out.println("false");
      cd.j();
    }
  }

  public static void a_V(int var0) {
    if (var0 > 0 && a_AI != null) {
      a_AI = cg.b(a_AI, var0);
      if (at.e_Z) {
        ak.a(a_AI, "Sự kiện đã xảy ra:");
      }
    }
  }

  public static void b_V(int var0) {
    if (var0 > 0) {
      if (at.e_Z) {
        ak.a(a_AI, "ID sự kiện đã xảy ra trước khi thêm:");
      }

      if (a_AI != null) {
        if (!cg.a_Z(a_AI, var0)) {
          if (at.e_Z) {
            System.out.println("Thêm id:" + var0 + " sự kiện kết thúc");
          }

          a_AI = cg.a_AI(a_AI, var0);
          return;
        }

        if (at.e_Z) {
          System.out.println("Sự kiện cần xóa đã tồn tại trong mảng.");
          return;
        }
      } else {
        a_AI = cg.a_AI(a_AI, var0);
        if (at.e_Z) {
          ak.a(a_AI, "ID sự kiện đã xảy ra sau khi thêm:");
          return;
        }
      }
    } else if (at.e_Z) {
      System.out.println("Không thể thêm id:0 làm sự kiện");
    }
  }

  public static int a(int var0, int var1, boolean var2) {
    int var3 = (var0 + var1) / 2;
    int var4 = k.a(1, 3);
    if (var2) {
      if (var4 < 2) {
        return k.a(var0, var3 - var0 + 1);
      }
    } else if (var4 >= 2) {
      return k.a(var0, var3 - var0 + 1);
    }

    return k.a(var3, var1 - var3 + 1);
  }

  private static void b(cv var0) {
    int var1 = var0.b_I;
    int var2 = 0;

    au var10000;
    while (true) {
      if (a_Aau == null || var2 >= a_Aau.length) {
        var10000 = null;
        break;
      }

      if (a_Aau[var2].b_I == var1) {
        var10000 = a_Aau[var2];
        break;
      }

      var2++;
    }

    au var8 = var10000;
    System.out.println("Sủng vật " + var0.a_I + " lấy từ dữ liệu bộ nhớ");
    if (var8 != null) {
      if (at.e_Z) {
        System.out.println("Sủng vật " + var0.a_I + " lấy từ dữ liệu bộ nhớ");
      }

      var0.a_AI = var8.a_AI;
      var0.a_Z = var8.a_Z;
      var0.a_AS = var8.a_AS;
      var0.a_Ad = var8.a_Ad;
      var0.a_AB = var8.a_AB;
      var0.a_I = var8.a_I;
      var0.a_String = var8.a_String;
      var0.b_String = var8.b_String;
      var0.a_B = var8.a_B;
      var0.a_AAB = var8.a_AAB;
    } else {
      if (at.e_Z) {
        System.out.println("Sủng vật " + var0.a_I + " lấy dữ liệu từ văn bản");
      }

      String var10;
      String[][] var9 = cg.a_AAString(var10 = cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
      String[][] var11 = cg.a_AAString(var10, "stardata", "end", null, "\t");
      if (var9 != null) {
        int var3 = var0.a_I - 1;
        var0.a_String = var9[var3][0];
        var0.a_AI = cg.a_AI(var0.a_AI, 1);

        for (int var7 = 0; var7 < 2; var7++) {
          int var5 = cg.a_I(var9[var3][3 + var7 * 2]) - 1;
          int[] var4;
          int var6 = k.a((var4 = cg.b_AI(var11[var5][var7 * 2], "|"))[0], var4[1] - var4[0] + 1);
          var0.a_AI = cg.a_AI(var0.a_AI, var6);
          var0.a_AI = cg.a_AI(var0.a_AI, var6);
          var5 = cg.a_I(var9[var3][4 + var7 * 2]) - 1;
          int[] var17 = cg.b_AI(var11[var5][1 + var7 * 2], "|");
          if (var6 > (var4[0] + var4[1]) / 2) {
            var0.a_AI = cg.a_AI(var0.a_AI, a(var17[0], var17[1], true));
          } else {
            var0.a_AI = cg.a_AI(var0.a_AI, a(var17[0], var17[1], false));
          }
        }

        for (int var24 = 0; var24 < 3; var24++) {
          int var18 = cg.a_I(var9[var3][7 + var24 * 2]) - 1;
          int[] var14;
          int var21 = k.a((var14 = cg.b_AI(var11[var18][4 + var24 * 2], "|"))[0], var14[1] - var14[0] + 1);
          var0.a_AI = cg.a_AI(var0.a_AI, var21);
          var18 = cg.a_I(var9[var3][8 + var24 * 2]) - 1;
          int[] var20 = cg.b_AI(var11[var18][5 + var24 * 2], "|");
          if (var21 > (var14[0] + var14[1]) / 2) {
            var0.a_AI = cg.a_AI(var0.a_AI, a(var20[0], var20[1], true));
          } else {
            var0.a_AI = cg.a_AI(var0.a_AI, a(var20[0], var20[1], false));
          }
        }

        int var22 = (var0.a_AI[0] * var0.a_AI[0] * var0.a_AI[0] * var0.a_AI[0] + 5) / 10;
        var0.a_AI = cg.a_AI(var0.a_AI, var22);
        var22 = (var0.a_AI[0] + 1) * (var0.a_AI[0] + 1) * (var0.a_AI[0] + 1) + (var0.a_AI[0] + 1) * (var0.a_AI[0] + 1);
        var0.a_AI = cg.a_AI(var0.a_AI, var22);
        var0.a_AI = cg.a_AI(var0.a_AI, 0);
        var0.a_AI = cg.a_AI(var0.a_AI, 60);
        cg.a_B(var9[var3][1]);
        if (!var9[var3][19].equals("-1")) {
          var0.b_String = var9[var3][19];
        } else {
          var0.b_String = "";
        }

        if (!var9[var3][18].equals("-1")) {
          var0.b_AB = cg.b_AB(var9[var3][18], ",");
        }

        var0.a_Z = false;
        if (var9[var3][13].equals("-1")) {
          var0.a_AS = null;
        } else {
          String[] var25 = cg.b_AString(var9[var3][13], "|");

          for (int var12 = 0; var12 < var25.length; var12++) {
            short[] var15 = cg.b_AS(var25[var12], ",");
            var0.a_AS = cg.a_AS(var0.a_AS, var15[0]);
          }
        }

        var0.a_Ad = a_Ad(var0, 1);
        if (var9[var3][2] != null) {
          for (int var26 = 0; var26 < 4; var26++) {
            String var13;
            if ((var13 = var9[var3][2].substring(var26, var26 + 1)).equals("a")) {
              var0.a_AB = cg.a_AB(var0.a_AB, 10);
            } else {
              var0.a_AB = cg.a_AB(var0.a_AB, cg.a_I(var13));
            }
          }
        }

        var0.a_B = (byte)e_S;
      }
    }

    a(new cv[]{var0});
  }

  public static void a(bl var0) {
    if (d_AAString == null) {
      d_AAString = cg.a_AAString(cg.a_String("/bin/team.bin"), "role:", "roleEnd", null, "\t");
    }

    byte var1 = -1;

    for (int var2 = 0; var2 < d_AAString.length; var2++) {
      if (cg.a_I(d_AAString[var2][1]) == var0.h_S) {
        var1 = (byte)var2;
        break;
      }
    }

    if (var1 < 0) {
      var0.b_String = d_AString[var0.h_S - 1];
      var0.d_Z = false;
    } else {
      var0.d_Z = true;
      ao var4;
      if ((var4 = a_ao(var0.h_S)) != null) {
        if (at.e_Z) {
          System.out.println("Nhân vật " + var0.h_S + " lấy từ dữ liệu bộ nhớ");
        }

        var0.a_AI = var4.a_AI;
        var0.a_AS = var4.a_AS;
        var0.a_Ad = var4.a_Ad;
        var0.b_AS = var4.b_AS;
        var0.a_Ack = var4.a_Ack;
        if (var0.h_S == 1) {
          var0.g_B = 1;
        } else {
          var0.g_B = 2;
        }

        var0.a_AB = var4.a_AB;
        var0.a_AAB = var4.a_AAB;
        var0.f_B = var4.c_B;
      } else {
        if (at.e_Z) {
          System.out.println("Nhân vật " + var0.h_S + " lấy từ dữ liệu văn bản");
        }

        int[] var5 = null;

        for (int var3 = 3; var3 < 13; var3++) {
          var5 = cg.a_AI(var5, cg.a_I(d_AAString[var1][var3]));
        }

        var0.a_AI = new int[bl.e_B];
        System.arraycopy(var5, 0, var0.a_AI, 0, var5.length);
        var0.a_AI[16] = 10;
        if (d_AAString[var1][13].equals("-1")) {
          var0.a_AS = null;
        } else {
          var0.a_AS = cg.b_AS(d_AAString[var1][13], ",");
        }

        var0.a_Ad = a_Ad(var0, 1);
        var0.b_AS = cg.b_AS(d_AAString[var1][14], ",");
        var0.a_Ack = a(var0.b_AS, -1);
        if (var0.h_S != 1 && var0.h_S != 2) {
          var0.g_B = 2;
        } else {
          var0.g_B = 1;
        }

        var0.a_AB = cg.b_AB(d_AAString[var1][15], ",");
        var0.a_AAB[0] = cg.b_AB(d_AAString[var1][16], ",");
        var0.a_AAB[1] = cg.b_AB(d_AAString[var1][17], ",");
        var0.a_AAB[2] = cg.b_AB(d_AAString[var1][18], ",");
        var0.a_AAB[3] = cg.b_AB(d_AAString[var1][19], ",");
        var0.f_B = 1;
      }

      if (var0.a_Ack == null) {
        var0.a_Ack = new ck[6];
      } else if (var0.a_Ack.length < 6) {
        ck[] var6 = new ck[6];
        System.arraycopy(var0.a_Ack, 0, var6, 0, var0.a_Ack.length);
        var0.a_Ack = null;
        var0.a_Ack = var6;
      }

      var0.b_String = d_AString[var0.h_S - 1];
      a(new bl[]{var0});
    }
  }

  public static void a_V(String var0) {
    c_S = 0;
    String var1 = n_String;
    if (var0.equals("Tiến độ 1")) {
      var1 = n_String;
    } else if (var0.equals("Tiến độ 2")) {
      var1 = o_String;
    } else if (var0.equals("Tiến độ 3")) {
      var1 = p_String;
    }

    c(var1);

    for (int var2 = 0; var2 < b_AB.length; var2++) {
      byte var5;
      c(a_bl(var5 = b_AB[var2]));
    }

    for (int var3 = 0; n_AS != null && var3 < n_AS.length; var3++) {
      b(a_bp(n_AS[var3], var3));
    }

    for (int var4 = 0; v_AS != null && var4 < v_AS.length; var4++) {
      a_V(v_AS[var4], b_AI[var4]);
    }
  }

  public static void a_V(int var0, int var1) {
    cv var2;
    (var2 = new cv()).a_I = var0;
    if (var1 == -1) {
      if (b_AI == null) {
        var2.b_I = 1;
      } else {
        var2.b_I = k.a(b_AI) + 1;
      }
    } else {
      var2.b_I = var1;
    }

    if (!cg.a_Z(p_AS, var0)) {
      p_AS = cg.a_AS(p_AS, var0);
      a_AZ[var0 - 1] = true;
    }

    b(var2);
    if (a_Acv == null) {
      a_Acv = new cv[1];
    } else {
      cv[] var3 = new cv[a_Acv.length + 1];
      System.arraycopy(a_Acv, 0, var3, 0, a_Acv.length);
      a_Acv = var3;
    }

    a_Acv[a_Acv.length - 1] = var2;
    if (var2.a_AI == null) {
      b(var2);
    }

    if (!cg.a_Z(b_AI, var2.b_I)) {
      b_AI = cg.a_AI(b_AI, var2.b_I);
      v_AS = cg.a_AS(v_AS, var2.a_I);
    }
  }

  public static bp a_bp(int var0, int var1) {
    bp var2 = new bp();
    if (e_AAString == null) {
      e_AAString = cg.a_AAString(cg.a_String("/bin/task.bin"), "task", "taskend", null, "\t");
    }

    short var3 = -1;

    for (int var4 = 0; var4 < e_AAString.length; var4++) {
      if (cg.a_S(e_AAString[var4][0]) == var0) {
        var3 = (short)var4;
        break;
      }
    }

    label28: {
      bp var10000;
      boolean var10001;
      label27: {
        var2.a_S = (short)var0;
        var2.a_String = e_AAString[var3][1];
        if (var1 == -1) {
          var10000 = var2;
        } else {
          if (m_AS == null) {
            break label28;
          }

          var10000 = var2;
          if (m_AS[var1] == 1) {
            var10001 = true;
            break label27;
          }
        }

        var10001 = false;
      }

      var10000.a_Z = var10001;
    }

    var2.a_B = cg.a_B(e_AAString[var3][3]);
    var2.b_B = cg.a_B(e_AAString[var3][4]);
    var2.b_String = e_AAString[var3][5];
    var2.c_String = e_AAString[var3][6];
    var2.d_String = e_AAString[var3][7];
    var2.e_String = e_AAString[var3][8];
    cg.a_S(e_AAString[var3][9]);
    var2.b_S = cg.a_S(e_AAString[var3][10]);
    return var2;
  }

  private static void b(bp var0) {
    if (a_Abp == null) {
      a_Abp = new bp[1];
    } else {
      bp[] var1 = new bp[a_Abp.length + 1];
      System.arraycopy(a_Abp, 0, var1, 0, a_Abp.length);
      a_Abp = var1;
    }

    a_Abp[a_Abp.length - 1] = var0;
  }

  public static void b(bl var0) {
    if (b_Abl == null) {
      b_Abl = new bl[1];
    } else {
      bl[] var1 = new bl[b_Abl.length + 1];
      System.arraycopy(b_Abl, 0, var1, 0, b_Abl.length);
      b_Abl = var1;
    }

    b_Abl[b_Abl.length - 1] = var0;
  }

  public static void c(bl var0) {
    if (a_Abl == null) {
      a_Abl = new bl[1];
    } else {
      bl[] var1 = new bl[a_Abl.length + 1];
      System.arraycopy(a_Abl, 0, var1, 0, a_Abl.length);
      a_Abl = var1;
    }

    a_Abl[a_Abl.length - 1] = var0;
    if (var0.a_AI == null) {
      a(var0);
    }

    var0.a_Z = false;
    if (!cg.a_Z(b_AB, var0.h_S)) {
      b_AB = cg.a_AB(b_AB, var0.h_S);
    }
  }

  public static bl a_bl(int var0) {
    bl var1 = new bl();
    if (g_AAString == null) {
      g_AAString = cg.a_AAString(cg.a_String("/bin/role.bin"), "role:", "roleEnd", null, "\t");
    }

    String[] var2 = null;
    short[] var3 = null;

    for (int var4 = 0; var4 < g_AAString.length; var4++) {
      var2 = cg.a(var2, g_AAString[var4][2]);
      var3 = cg.a_AS(var3, cg.a_S(g_AAString[var4][0]));
    }

    int var6 = 0;

    for (int var5 = 0; var5 < var3.length; var5++) {
      if (var0 == var3[var5]) {
        var6 = var5;
        break;
      }
    }

    if (var2[var6].toLowerCase().endsWith(".png") || var2[var6].toLowerCase().endsWith(".gf")) {
      (var1 = new bl(cc.a(var2[var6]))).a_AS(1);
    } else if (var2[var6].toLowerCase().endsWith(".av")) {
      var1.a_B = 1;
      bx var7;
      (var7 = new bx()).a(var2[var6]);
      var1 = new bl(var7);
    } else if (at.e_Z) {
      System.out.println("Định dạng ảnh tinh linh không xác định:" + var2[var6]);
    }

    var1.a_V(0);
    var1.h_S = (short)var0;
    var1.a_String = c_AString[var1.h_S - 1];
    var1.b_String = d_AString[var1.h_S - 1];
    var1.i_B = 1;
    a(var1);
    return var1;
  }

  public static void b() {
    String var0 = cg.a_String("/bin/sn.bin");
    if (f_AAString == null) {
      f_AAString = cg.a_AAString(var0, "data:", "dataEnd", null, "\t");
    }

    String var1;
    j_String = cg.a_String(var1 = cg.a(var0, "battleMusic:", "end"), "inbattle");
    k_String = cg.a_String(var1, "win");
    l_String = cg.a_String(var1, "lose");
    a_S = cg.a_S(var0, "default");
    e_S = cg.a_S(var0, "heroId");
    if (d_S == 0) {
      d_S = cg.a_S(var0, "liveBackScene");
    }

    if (e_B == 0) {
      e_B = cg.a_B(var0, "liveBackPos");
    }

    var0 = cg.a_String("/bin/role.bin");
    if (g_AAString == null) {
      g_AAString = cg.a_AAString(var0, "role:", "roleEnd", null, "\t");
    }

    h_String = cg.a_String(var0, "roleFlyBody");
    g_String = cg.a_String(var0, "roleRunBodys");

    for (int var3 = 0; var3 < g_AAString.length; var3++) {
      c_AString = cg.a(c_AString, g_AAString[var3][1]);
      if (g_AAString[var3][3].equals("-1")) {
        g_AAString[var3][3] = "";
      }

      d_AString = cg.a(d_AString, g_AAString[var3][3]);
      e_AString = cg.a(e_AString, g_AAString[var3][2]);
    }

    if (l_AString == null) {
      if ((var0 = cg.a(cg.a_String("/bin/about.bin"), "tips:", "tipsEnd")) != null) {
        l_AString = cg.b_AString(var0, "\r\n");
      }

      f_B = (byte)k.a(0, l_AString.length);
    }
  }

  public static boolean a_Z(int var0) {
    a_String = null;
    a_AS = null;
    h_AAString = null;
    j_AString = null;
    k_AString = null;
    a_AString = null;
    b_AString = null;
    String var1 = cg.a_String("/bin/sn.bin");
    if (f_AAString == null) {
      f_AAString = cg.a_AAString(var1, "data:", "dataEnd", null, "\t");
    }

    boolean var9 = false;
    int var2 = 0;

    for (int var3 = 0; var3 < f_AAString.length; var3++) {
      if (cg.a_S(f_AAString[var3][0]) == var0) {
        var2 = var3;
        var9 = true;
        break;
      }
    }

    if (var9) {
      k_B = cg.a_B(f_AAString[var2][3]);
      if (f_AAString[var2].length > 12) {
        b_B = cg.a_B(f_AAString[var2][12]);
      } else {
        System.out.println("File Sn thiếu loại cảnh chiến đấu");
      }

      if (k_B == 1) {
        a_String = f_AAString[var2][2];
        f_String = String.valueOf(var0);
        j_B = 0;
        d_Z = false;
      } else {
        s_String = null;
        n_AString = null;
        c_AAS = null;
        r_AS = null;
        m_AString = null;
        q_AS = null;
        String var13;
        String[][] var7 = cg.a_AAString(var13 = cg.a_String("/bin/mazefile.bin"), "fuben:", "fubenEnd", null, "\t");
        int var10 = 0;

        for (int var4 = 0; var7 != null && var4 < var7.length; var4++) {
          if (var7[var4][0].equals(f_AAString[var2][2])) {
            var10 = var4;
            break;
          }
        }

        s_String = var7[var10][1];
        h_B = cg.a_B(var7[var10][2]);
        i_B = cg.a_B(var7[var10][4]);
        m_AString = cg.b_AString(var7[var10][5], ",");
        String[] var17 = cg.b_AString(var7[var10][6], "|");

        for (int var6 = 0; var17 != null && var6 < var17.length; var6++) {
          short[] var5 = cg.b_AS(var17[var6], ",");
          c_AAS = cg.a(c_AAS, var5);
        }

        q_AS = cg.b_AS(var7[var10][7], ",");
        r_AS = cg.b_AS(var7[var10][8], ",");
        if (!var7[var10][9].equals("-1")) {
          n_AString = cg.b_AString(var7[var10][9], "|");
        }

        if (!d_Z) {
          if (cd.c_S == 2) {
            j_B = i_B;
          } else {
            j_B = 1;
          }
        }

        int var22 = 0;
        h_AAString = cg.a_AAString(var13, "maze:", "mazeEnd", null, "\t");

        for (int var18 = 0; var18 < h_AAString.length; var18++) {
          if (h_AAString[var18][0].equals(var7[var10][3])) {
            var22 = var18;
            break;
          }
        }

        var17 = cg.b_AString(h_AAString[var22][1], ",");
        var0 = k.a(0, var17.length);
        a_String = var17[var0];
        f_String = cg.b_AString(h_AAString[var22][2], ",")[var0];
        String[] var11 = cg.a_AString(var13, "mazepos:", "mazeposEnd", "=");
        int var14 = cg.a_I(h_AAString[var22][3]);
        var17 = cg.b_AString(var11[var14], "#");
        String[] var12 = cg.b_AString(var11[var14 + 1], "#");
        String[] var15 = cg.b_AString(var17[var0], "|");
        int[] var21 = k.a(0, var15.length - 1, 2);
        j_AString = cg.a(j_AString = cg.a(j_AString, var15[var21[0]]), var15[var21[1]]);
        String[] var16 = cg.b_AString(var12[var0], "|");
        k_AString = cg.a(k_AString = cg.a(k_AString, var16[var21[0]]), var16[var21[1]]);
        d_Z = true;
      }

      b_String = f_AAString[var2][1];
      if (j_B > 0) {
        b_String = b_String + j_B;
      }

      c_String = f_AAString[var2][4];
      if (f_AAString[var2][5].equals("-1")) {
        a_AS = null;
      } else {
        a_AS = cg.b_AS(f_AAString[var2][5], ",");
      }

      if (f_AAString[var2][6].equals("-1")) {
        a_AB = null;
      } else {
        a_AB = cg.b_AB(f_AAString[var2][6], ",");
      }

      if (f_AAString[var2][7].equals("-1")) {
        d_String = null;
      } else {
        d_String = f_AAString[var2][7];
      }

      if (f_AAString[var2][8].equals("-1")) {
        b_AS = null;
      } else {
        b_AS = cg.b_AS(f_AAString[var2][8], ",");
      }

      if (f_AAString[var2][9].equals("-1")) {
        a_AString = null;
      } else {
        a_AString = cg.b_AString(f_AAString[var2][9], ",");
      }

      if (f_AAString[var2][10].equals("-1")) {
        b_AString = null;
      } else {
        b_AString = cg.b_AString(f_AAString[var2][10], "|");
      }

      if (f_AAString[var2][11].equals("-1")) {
        c_B = 0;
      } else {
        c_B = cg.a_B(f_AAString[var2][11]);
      }

      if (e_String == null) {
        e_String = cg.a_String("/bin/colorTable.bin");
      }

      return true;
    } else {
      System.out.println("Dữ liệu cảnh không tồn tại");
      return false;
    }
  }

  public static ao a_ao(int var0) {
    for (int var1 = 0; a_Aao != null && var1 < a_Aao.length; var1++) {
      if (a_Aao[var1].a_B == var0) {
        return a_Aao[var1];
      }
    }

    return null;
  }

  public static void a(cv var0, String var1) {
    String[] var4 = cg.b_AString(var1, "|");

    for (int var2 = 0; var2 < var4.length; var2++) {
      short[] var3 = cg.b_AS(var4[var2], ",");
      var0.a_AI[var3[0]] = var0.a_AI[var3[0]] + var3[1];
    }
  }

  public static void a_V(byte var0) {
    byte var1 = (byte)(l_B / var0);
    if ((var0 = (byte)(l_B % var0)) > 0) {
      var0 = (byte)(var1 + 1);
    } else {
      var0 = var1;
    }

    d_B = var0;
  }

  public static void c() {
    int var0 = 0;
    short[][] var1 = a_Abl[0].a_AAS;

    for (int var2 = 0; a_Abl != null && var2 < a_Abl.length; var2++) {
      if (var2 != 0) {
        a_Abl[var2].d_S = var1[(var0 + 1) * d_B - 1][0];
        a_Abl[var2].e_S = var1[(var0 + 1) * d_B - 1][1];
        a_Abl[var2].a_V(var1[(var0 + 1) * d_B - 1][2]);
        a_Abl[var2].a_bx.b(0);
        var0++;
      }
    }
  }

  public static int a_I(int var0) {
    int var1 = 0;
    if (a_AAS != null) {
      var0 = d_I(var0);
      short var2 = f_AS[var0];

      for (int var3 = 0; var3 < a_AAS.length; var3++) {
        var0 = d_I(a_AAS[var3][0]);
        short var6 = f_AS[var0];
        if (var2 == 1) {
          if (var6 != 1) {
            continue;
          }
        } else {
          if (var2 == 2 || var2 == 5) {
            if (var6 == 2 || var6 == 5) {
              var1++;
            }
            continue;
          }

          if (var2 != 3 && var2 != 4 || var6 != 3 && var6 != 4) {
            continue;
          }
        }

        var1++;
      }
    }

    return var1;
  }

  public static void b(int var0, int var1) {
    int var2 = a_I(var0);
    if (a_AAS == null) {
      if (var2 < ct.a_S) {
        (a_AAS = new short[1][2])[0][0] = (short)var0;
        a_AAS[0][1] = (short)var1;
      } else {
        da.a_da.a("Túi đồ đã đầy", 10);
      }
    } else {
      boolean var3 = false;

      for (int var4 = 0; a_AAS != null && var4 < a_AAS.length; var4++) {
        if (a_AAS[var4][0] == var0) {
          if (a_AAS[var4][1] + var1 > 99) {
            a_AAS[var4][1] = 99;
            da.a_da.a("Số vật phẩm đã tối đa", 10);
          } else {
            a_AAS[var4][1] = (short)(a_AAS[var4][1] + var1);
          }

          var3 = true;
          break;
        }
      }

      if (!var3) {
        if (var2 < ct.a_S) {
          short[][] var5 = new short[a_AAS.length + 1][2];
          System.arraycopy(a_AAS, 0, var5, 0, a_AAS.length);
          var5[var5.length - 1][0] = (short)var0;
          var5[var5.length - 1][1] = (short)var1;
          a_AAS = var5;
          return;
        }

        da.a_da.a("Túi đồ đã đầy", 10);
      }
    }
  }

  public static void c(int var0, int var1) {
    if (a_AAS != null) {
      for (int var2 = 0; var1 > 0 && var2 < a_AAS.length; var2++) {
        if (a_AAS[var2][0] == var0) {
          if (a_AAS[var2][1] >= var1) {
            a_AAS[var2][1] = (short)(a_AAS[var2][1] - var1);
            var1 = 0;
          } else {
            var1 -= a_AAS[var2][1];
            a_AAS[var2][1] = 0;
          }
        }
      }

      for (int var3 = a_AAS.length - 1; var3 >= 0; var3--) {
        if (a_AAS[var3][1] <= 0) {
          a_AAS = cg.a(a_AAS, var3);
        }
      }
    }
  }

  public static void d() {
    if (f_AString == null) {
      if (a_AAString == null) {
        String var0;
        a_AAString = cg.a_AAString(var0 = cg.a_String("/bin/item.bin"), "item", "itemend", null, "\t");
        cg.a(var0, "itemDefaultAni:", "itemDefaultAniEnd");
      }

      for (int var1 = 0; var1 < a_AAString.length; var1++) {
        e_AS = cg.a_AS(e_AS, cg.a_I(a_AAString[var1][0]));
        f_AString = cg.a(f_AString, a_AAString[var1][1]);
        f_AS = cg.a_AS(f_AS, cg.a_I(a_AAString[var1][3]));
        s_AS = cg.a_AS(s_AS, cg.a_I(a_AAString[var1][4]));
        g_AS = cg.a_AS(g_AS, cg.a_I(a_AAString[var1][5]));
        t_AS = cg.a_AS(t_AS, cg.a_I(a_AAString[var1][6]));
        u_AS = cg.a_AS(u_AS, cg.a_I(a_AAString[var1][7]));
        g_AString = cg.a(g_AString, a_AAString[var1][8]);
        h_AS = cg.a_AS(h_AS, cg.a_I(a_AAString[var1][9]));
        i_AS = cg.a_AS(i_AS, cg.a_I(a_AAString[var1][10]));
        o_AString = cg.a(o_AString, a_AAString[var1][2]);
        h_AString = cg.a(h_AString, a_AAString[var1][11]);
      }
    }
  }

  public static void e() {
    if (o_AString == null) {
      d();
    }

    if (a_AImage == null) {
      short var0;
      a_AImage = new Image[var0 = (short)o_AString.length];

      for (int var1 = 0; var1 < var0; var1++) {
        a_AImage[var1] = cc.a_Image("/icon/" + o_AString[var1] + ".gf", 2);
      }
    }
  }

  public static void d(bl var0) {
    var0.a_AI[14] = var0.a_I();
    var0.a_AI[12] = var0.b_I();
    var0.a_AI[13] = var0.c_I();
    var0.a_AI[10] = var0.d_I();
    var0.a_AI[11] = var0.j_I();
    var0.a_AI[16] = var0.e_I();
    var0.a_AI[17] = var0.f_I();
    var0.a_AI[18] = var0.g_I();
    var0.a_AI[19] = var0.h_I();
    var0.a_AI[20] = var0.i_I();
    if (var0.a_AI[3] > var0.a_AI[10]) {
      var0.a_AI[3] = var0.a_AI[10];
    }

    if (var0.a_AI[5] > var0.a_AI[11]) {
      var0.a_AI[5] = var0.a_AI[11];
    }
  }

  public static cv[] a_Acv() {
    Vector var0 = new Vector();
    cv[] var1 = null;

    for (byte var2 = 0; a_Acv != null && var2 < a_Acv.length; var2++) {
      if (a_Acv[var2].a_B == e_S && !a_Acv[var2].a_Z && a_Acv[var2].a_AI[1] > 0 && !a_Acv[var2].b_Z) {
        var0.addElement(a_Acv[var2]);
      }
    }

    if (var0.size() > 0) {
      var1 = new cv[var0.size()];
      var0.copyInto(var1);
    }

    return var1;
  }

  public static bl[] a_Abl() {
    Vector var0 = new Vector();
    bl[] var1 = null;

    for (byte var2 = 0; a_Acv != null && var2 < a_Acv.length; var2++) {
      if (a_Acv[var2].a_B == e_S && a_Acv[var2].a_Z && a_Acv[var2].a_AI[1] > 0) {
        cv var3;
        bl var6 = a_bl(var3 = a_Acv[var2]);
        if (a_Abl == null) {
          a_Abl = new bl[1];
        } else {
          bl[] var4 = new bl[a_Abl.length + 1];
          System.arraycopy(a_Abl, 0, var4, 0, a_Abl.length);
          a_Abl = var4;
        }

        a_Abl[a_Abl.length - 1] = var6;
      }
    }

    for (int var5 = 0; var5 < 4 && var5 < a_Abl.length; var5++) {
      if (a_Abl[var5].d_Z) {
        var0.addElement(a_Abl[var5]);
      }
    }

    if (var0.size() > 0) {
      var1 = new bl[var0.size()];
      var0.copyInto(var1);
    }

    return var1;
  }

  public static bl a_bl(cv var0) {
    bl var1;
    (var1 = new bl(k.a(var0.a_I, true))).d_Z = true;
    var1.a_String = var0.a_String;
    var1.a_AB = var0.a_AB;
    var1.a_AI = new int[bl.e_B];
    var1.a_AI[0] = var0.a_AI[0];
    var1.a_AI[3] = var0.a_AI[1];
    var1.a_AI[10] = var0.a_AI[2];
    var1.a_AI[5] = var0.a_AI[4];
    var1.a_AI[11] = var0.a_AI[5];
    var1.a_AI[12] = var0.a_AI[7];
    var1.a_AI[13] = var0.a_AI[9];
    var1.a_AI[14] = var0.a_AI[11];
    var1.a_AI[1] = var0.a_AI[13];
    var1.a_AI[2] = var0.a_AI[14];
    var1.a_AI[15] = var0.a_AI[15];
    var1.a_AS = var0.a_AS;
    var1.a_Ad = var0.a_Ad;
    var1.h_S = (short)var0.a_I;
    var1.c_S = (short)var0.b_I;
    var1.a_Ack = null;
    var1.a_Ack = new ck[6];
    return var1;
  }

  private static d[] a_Ad(cv var0, int var1) {
    if (var0.a_AS == null) {
      return null;
    } else {
      d[] var3 = new d[var0.a_AS.length];

      for (int var2 = 0; var2 < var0.a_AS.length; var2++) {
        var3[var2] = new d(var0.a_AS[var2], 1, 4);
      }

      return var3;
    }
  }

  private static d[] a_Ad(bl var0, int var1) {
    if (var0.a_AS == null) {
      return null;
    } else {
      d[] var3 = new d[var0.a_AS.length];

      for (int var2 = 0; var2 < var0.a_AS.length; var2++) {
        var3[var2] = new d(var0.a_AS[var2], 1, var0.h_S);
      }

      return var3;
    }
  }

  private static ck[] a(short[] var0, int var1) {
    if (var0 == null) {
      System.out.println("Trang bị của tinh linh này là null");
      return null;
    } else if (var0 != null) {
      ck[] var3 = new ck[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
        if (var0[var2] > 0) {
          var3[var2] = new ck(var0[var2]);
        }
      }

      return var3;
    } else {
      return null;
    }
  }

  public static void a(cv[] var0) {
    boolean var1 = false;

    for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
      for (int var3 = 0; a_Aau != null && var3 < a_Aau.length; var3++) {
        if (a_Aau[var3].b_I == var0[var2].b_I) {
          var1 = true;
          a_Aau[var3].a_AI = var0[var2].a_AI;
          a_Aau[var3].a_AS = var0[var2].a_AS;
          a_Aau[var3].a_Ad = var0[var2].a_Ad;
          a_Aau[var3].a_AB = var0[var2].a_AB;
          a_Aau[var3].a_String = var0[var2].a_String;
          a_Aau[var3].a_I = var0[var2].a_I;
          a_Aau[var3].a_Z = var0[var2].a_Z;
          a_Aau[var3].b_String = var0[var2].b_String;
          a_Aau[var3].a_AAB = var0[var2].a_AAB;
        }
      }

      if (!var1 && var0[var2].a_I > 0) {
        if (a_Aau == null) {
          a_Aau = new au[1];
        } else {
          au[] var4 = new au[a_Aau.length + 1];
          System.arraycopy(a_Aau, 0, var4, 0, a_Aau.length);
          a_Aau = var4;
        }

        a_Aau[a_Aau.length - 1] = new au();
        a_Aau[a_Aau.length - 1].a_AI = var0[var2].a_AI;
        a_Aau[a_Aau.length - 1].a_I = var0[var2].a_I;
        a_Aau[a_Aau.length - 1].a_AS = var0[var2].a_AS;
        a_Aau[a_Aau.length - 1].a_Ad = var0[var2].a_Ad;
        a_Aau[a_Aau.length - 1].a_String = var0[var2].a_String;
        a_Aau[a_Aau.length - 1].a_AB = var0[var2].a_AB;
        a_Aau[a_Aau.length - 1].b_I = var0[var2].b_I;
        a_Aau[a_Aau.length - 1].a_Z = var0[var2].a_Z;
        a_Aau[a_Aau.length - 1].b_String = var0[var2].b_String;
        a_Aau[a_Aau.length - 1].a_B = var0[var2].a_B;
        a_Aau[a_Aau.length - 1].a_AAB = var0[var2].a_AAB;
      }
    }
  }

  public static void a(bl[] var0) {
    if (d_AAString == null) {
      d_AAString = cg.a_AAString(cg.a_String("/bin/team.bin"), "role:", "roleEnd", null, "\t");
    }

    boolean var1 = false;

    for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
      byte var3 = -1;

      for (int var4 = 0; var4 < d_AAString.length; var4++) {
        if (cg.a_I(d_AAString[var4][1]) == var0[var2].h_S) {
          var3 = (byte)var4;
          break;
        }
      }

      if (var0[var2] != null && var3 >= 0) {
        for (int var5 = 0; a_Aao != null && var5 < a_Aao.length; var5++) {
          if (a_Aao[var5].a_B == var0[var2].h_S) {
            var1 = true;
            a_Aao[var5].a_AI = var0[var2].a_AI;
            a_Aao[var5].a_AS = var0[var2].a_AS;
            a_Aao[var5].a_Ad = var0[var2].a_Ad;
            a_Aao[var5].b_AS = var0[var2].b_AS;
            a_Aao[var5].a_Ack = var0[var2].a_Ack;
            a_Aao[var5].a_AAB = var0[var2].a_AAB;
            a_Aao[var5].a_AB = var0[var2].a_AB;
            a_Aao[var5].c_B = var0[var2].f_B;
            break;
          }
        }

        if (!var1 && var0[var2].h_S > 0) {
          if (a_Aao == null) {
            a_Aao = new ao[1];
          } else {
            ao[] var6 = new ao[a_Aao.length + 1];
            System.arraycopy(a_Aao, 0, var6, 0, a_Aao.length);
            a_Aao = var6;
          }

          a_Aao[a_Aao.length - 1] = new ao();
          a_Aao[a_Aao.length - 1].a_B = (byte)var0[var2].h_S;
          a_Aao[a_Aao.length - 1].a_AI = var0[var2].a_AI;
          a_Aao[a_Aao.length - 1].a_AAB = var0[var2].a_AAB;
          a_Aao[a_Aao.length - 1].a_AS = var0[var2].a_AS;
          a_Aao[a_Aao.length - 1].a_Ad = var0[var2].a_Ad;
          a_Aao[a_Aao.length - 1].b_AS = var0[var2].b_AS;
          a_Aao[a_Aao.length - 1].a_Ack = var0[var2].a_Ack;
          a_Aao[a_Aao.length - 1].a_S = var0[var2].d_S;
          a_Aao[a_Aao.length - 1].b_S = var0[var2].e_S;
          a_Aao[a_Aao.length - 1].b_B = var0[var2].d_B;
          a_Aao[a_Aao.length - 1].a_AB = var0[var2].a_AB;
          a_Aao[a_Aao.length - 1].c_B = var0[var2].f_B;
        }
      }
    }
  }

  public static void b(bl[] var0) {
    cv var1 = new cv();

    for (int var2 = 1; var0 != null && var2 < var0.length; var2++) {
      if (var0[var2] != null) {
        System.out.println("Cập nhật dữ liệu chiến đấu " + var0[var2].c_S);
        boolean var3 = false;

        for (int var4 = 0; a_Acv != null && var4 < a_Acv.length; var4++) {
          System.out.println("GameData.myPet[j].onlyId" + a_Acv[var4].b_I);
          if (var0[var2].c_S == a_Acv[var4].b_I) {
            var3 = true;
            var1 = a_Acv[var4];
            System.out.println("Tìm thấy sủng vật " + var1.a_String);
            break;
          }
        }

        if (var3) {
          var1.a_I = var0[var2].h_S;
          var1.a_AI[0] = var0[var2].a_AI[0];
          var1.a_AI[1] = var0[var2].a_AI[3];
          var1.a_AI[2] = var0[var2].a_AI[10];
          var1.a_AI[4] = var0[var2].a_AI[5];
          var1.a_AI[5] = var0[var2].a_AI[11];
          var1.a_AI[7] = var0[var2].a_AI[12];
          var1.a_AI[9] = var0[var2].a_AI[13];
          var1.a_AI[11] = var0[var2].a_AI[14];
          var1.a_AI[13] = var0[var2].a_AI[1];
          var1.a_AI[14] = var0[var2].a_AI[2];
          var1.a_AI[15] = var0[var2].a_AI[15];
          var1.a_Ad = var0[var2].a_Ad;
          a(new cv[]{var1});
        }
      }
    }
  }

  public static void a_V(cv var0) {
    int var1 = var0.a_AI[3];
    var0.a_AI[2] = var0.a_AI[2] + var1;
    var1 = var0.a_AI[6];
    var0.a_AI[5] = var0.a_AI[5] + var1;
    var1 = var0.a_AI[8];
    var0.a_AI[7] = var0.a_AI[7] + var1;
    var1 = var0.a_AI[10];
    var0.a_AI[9] = var0.a_AI[9] + var1;
    var1 = var0.a_AI[12];
    var0.a_AI[11] = var0.a_AI[11] + var1;
    a(new cv[]{var0});
  }

  public static void e(bl var0) {
    for (int var1 = 0; a_Acv != null && var1 < a_Acv.length; var1++) {
      if (var0.c_S == a_Acv[var1].b_I) {
        int var2 = a_Acv[var1].a_AI[3];
        var0.a_AI[10] = var0.a_AI[10] + var2;
        var2 = a_Acv[var1].a_AI[6];
        var0.a_AI[11] = var0.a_AI[11] + var2;
        var2 = a_Acv[var1].a_AI[8];
        var0.a_AI[12] = var0.a_AI[12] + var2;
        var2 = a_Acv[var1].a_AI[10];
        var0.a_AI[13] = var0.a_AI[13] + var2;
        var2 = a_Acv[var1].a_AI[12];
        var0.a_AI[14] = var0.a_AI[14] + var2;
        return;
      }
    }
  }

  public static boolean a_Z(byte var0) {
    c_AB[var0] = 1;
    i_AString[var0] = b_String;
    j_AS[var0] = e_S;
    k_AS[var0] = (short)a_Abl[0].a_AI[0];
    Calendar var1;
    int var2 = (var1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))).get(1);
    int var3 = var1.get(2);
    int var4 = var1.get(5);
    int var5 = var1.get(11);
    int var6 = var1.get(12);
    int var15 = var1.get(13);
    l_AS[var0] = (short)var2;
    d_AB[var0] = (byte)(var3 + 1);
    e_AB[var0] = (byte)var4;
    f_AB[var0] = (byte)var5;
    g_AB[var0] = (byte)var6;
    h_AB[var0] = (byte)var15;
    RecordStore var14 = null;

    try {
      (var14 = RecordStore.openRecordStore(m_String, true)).closeRecordStore();
      RecordStore.deleteRecordStore(m_String);
      var14 = RecordStore.openRecordStore(m_String, true);
    } catch (RecordStoreFullException var11) {
      var11.printStackTrace();
    } catch (RecordStoreNotFoundException var12) {
      var12.printStackTrace();
    } catch (RecordStoreException var13) {
      var13.printStackTrace();
    }

    ByteArrayOutputStream var16 = new ByteArrayOutputStream();
    DataOutputStream var17 = new DataOutputStream(var16);

    try {
      for (int var18 = 0; var18 < 3; var18++) {
        var17.writeByte(c_AB[var18]);
        if (c_AB[var18] == 1) {
          var17.writeUTF(i_AString[var18]);
          var17.writeShort(j_AS[var18]);
          var17.writeShort(k_AS[var18]);
          var17.writeShort(l_AS[var18]);
          var17.writeByte(d_AB[var18]);
          var17.writeByte(e_AB[var18]);
          var17.writeByte(f_AB[var18]);
          var17.writeByte(g_AB[var18]);
          var17.writeByte(h_AB[var18]);
        }
      }

      byte[] var19 = var16.toByteArray();
      var14.addRecord(var19, 0, var19.length);
      var14.closeRecordStore();
      return true;
    } catch (IOException var7) {
      var7.printStackTrace();
      return false;
    } catch (RecordStoreNotOpenException var8) {
      var8.printStackTrace();
      return false;
    } catch (RecordStoreFullException var9) {
      var9.printStackTrace();
      return false;
    } catch (RecordStoreException var10) {
      var10.printStackTrace();
      return false;
    }
  }

  public static boolean a_Z(String var0) {
    RecordStore var1 = null;
    byte[] var2 = null;

    try {
      RecordEnumeration var10;
      if ((var10 = (var1 = RecordStore.openRecordStore(var0, true)).enumerateRecords(null, null, false)).hasNextElement()) {
        int var11 = var10.nextRecordId();
        var2 = var1.getRecord(var11);
      }
    } catch (RecordStoreFullException var7) {
      var7.printStackTrace();
    } catch (RecordStoreNotFoundException var8) {
      var8.printStackTrace();
    } catch (RecordStoreException var9) {
      var9.printStackTrace();
    }

    if (var2 != null) {
      ByteArrayInputStream var12 = new ByteArrayInputStream(var2);
      DataInputStream var13 = new DataInputStream(var12);

      try {
        df.a_String = var13.readUTF();
        df.b_String = var13.readUTF();
        df.d_Z = var13.readByte() == 1;
        df.c_Z = var13.readByte() == 1;
        df.b_Z = var13.readByte() == 1;
        df.c_B = var13.readByte();
        df.a_I = var13.readInt();
        g_B = var13.readByte();
        var1.closeRecordStore();
        return true;
      } catch (IOException var3) {
        var3.printStackTrace();
        return false;
      } catch (RecordStoreNotOpenException var4) {
        var4.printStackTrace();
        return false;
      } catch (RecordStoreFullException var5) {
        var5.printStackTrace();
        return false;
      } catch (RecordStoreException var6) {
        var6.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  private static boolean c(String var0) {
    RecordStore var1 = null;
    byte[] var2 = null;

    try {
      RecordEnumeration var3;
      if ((var3 = (var1 = RecordStore.openRecordStore(var0, false)).enumerateRecords(null, null, false)).hasNextElement()) {
        int var4 = var3.nextRecordId();
        var2 = var1.getRecord(var4);
      }
    } catch (RecordStoreFullException var7) {
      var7.printStackTrace();
    } catch (RecordStoreNotFoundException var8) {
      var8.printStackTrace();
    } catch (RecordStoreException var9) {
      var9.printStackTrace();
    }

    if (var2 != null) {
      ByteArrayInputStream var14 = new ByteArrayInputStream(var2);
      DataInputStream var15 = new DataInputStream(var14);

      try {
        a_B = var15.readByte();
        b_S = var15.readShort();
        if (at.b_Z = var15.readByte() == 1) {
          System.out.println("true");
          cd.m();
        } else {
          System.out.println("false");
          cd.j();
        }

        short var38;
        if ((var38 = var15.readShort()) > 0) {
          c_AS = new short[var38];

          for (int var46 = 0; var46 < var38; var46++) {
            c_AS[var46] = var15.readShort();
          }
        } else {
          c_AS = null;
        }

        short var47;
        if ((var47 = var15.readShort()) > 0) {
          d_AS = new short[var47];

          for (int var16 = 0; var16 < var47; var16++) {
            d_AS[var16] = var15.readShort();
          }
        } else {
          d_AS = null;
        }

        short var17;
        if ((var17 = var15.readShort()) > 0) {
          a_AI = new int[var17];

          for (int var39 = 0; var39 < var17; var39++) {
            a_AI[var39] = var15.readShort();
          }
        } else {
          a_AI = null;
        }

        if ((var38 = var15.readShort()) > 0) {
          m_AS = new short[var38];

          for (int var18 = 0; var18 < var38; var18++) {
            m_AS[var18] = var15.readShort();
          }
        } else {
          m_AS = null;
        }

        short var19;
        if ((var19 = var15.readShort()) > 0) {
          n_AS = new short[var19];

          for (int var41 = 0; var41 < var19; var41++) {
            n_AS[var41] = var15.readShort();
          }
        }

        if ((var38 = var15.readShort()) > 0) {
          o_AS = new short[var38];

          for (int var20 = 0; var20 < var38; var20++) {
            o_AS[var20] = var15.readShort();
          }
        }

        byte var21;
        if ((var21 = var15.readByte()) > 0) {
          b_AB = new byte[var21];

          for (int var22 = 0; var22 < b_AB.length; var22++) {
            b_AB[var22] = var15.readByte();
          }
        }

        byte var23;
        if ((var23 = var15.readByte()) > 0) {
          v_AS = new short[var23];

          for (int var24 = 0; var24 < v_AS.length; var24++) {
            v_AS[var24] = var15.readShort();
          }
        }

        byte var25;
        if ((var25 = var15.readByte()) > 0) {
          b_AI = new int[var25];

          for (int var26 = 0; var26 < b_AI.length; var26++) {
            b_AI[var26] = var15.readInt();
          }
        }

        short var27;
        if ((var27 = var15.readShort()) > 0) {
          a_AAS = new short[var27][2];

          for (int var28 = 0; var28 < a_AAS.length; var28++) {
            a_AAS[var28][0] = var15.readShort();
            a_AAS[var28][1] = var15.readShort();
          }
        }

        b_AAS = new short[6][];
        a_AAck = new ck[6][];

        for (int var29 = 0; var29 < 6; var29++) {
          if ((var38 = var15.readShort()) > 0) {
            b_AAS[var29] = new short[var38];
            a_AAck[var29] = new ck[var38];

            for (int var48 = 0; var48 < var38; var48++) {
              byte var5 = var15.readByte();
              b_AAS[var29][var48] = (short)var5;
              if (var5 > 0) {
                var15.readByte();
                a_AAck[var29][var48] = new ck(var5);
                a_AAck[var29][var48].a_B = var15.readByte();
                a_AAck[var29][var48].d_S = var15.readShort();
                a_AAck[var29][var48].e_S = var15.readShort();
                a_AAck[var29][var48].f_S = var15.readShort();
                a_AAck[var29][var48].g_S = var15.readShort();
                a_AAck[var29][var48].c_S = var15.readShort();
                a_AAck[var29][var48].h_S = var15.readShort();
                a_AAck[var29][var48].i_S = var15.readShort();
                a_AAck[var29][var48].j_S = var15.readShort();
                a_AAck[var29][var48].k_S = var15.readShort();
                a_AAck[var29][var48].l_S = var15.readShort();
                a_AAck[var29][var48].m_S = var15.readShort();
                a_AAck[var29][var48].n_S = var15.readShort();
              }
            }
          }
        }

        if ((a_I = var15.readInt()) < 0) {
          a_I = 99999;
        }

        byte var30;
        if ((var30 = var15.readByte()) > 0) {
          a_Aao = new ao[var30];

          for (int var44 = 0; a_Aao != null && var44 < a_Aao.length; var44++) {
            a_Aao[var44] = new ao();
            a_Aao[var44].a_B = var15.readByte();
            byte var49;
            if ((var49 = var15.readByte()) > 0) {
              a_Aao[var44].a_AI = new int[var49];

              for (int var52 = 0; var52 < var49; var52++) {
                a_Aao[var44].a_AI[var52] = var15.readInt();
              }
            }

            a_Aao[var44].a_AB = new byte[4];

            for (int var53 = 0; var53 < 4; var53++) {
              a_Aao[var44].a_AB[var53] = var15.readByte();
            }

            a_Aao[var44].a_AAB = new byte[4][5];

            for (int var54 = 0; var54 < 4; var54++) {
              for (int var31 = 0; var31 < 5; var31++) {
                a_Aao[var44].a_AAB[var54][var31] = var15.readByte();
              }
            }

            byte var55;
            if ((var55 = var15.readByte()) > 0) {
              a_Aao[var44].a_AS = new short[var55];
              a_Aao[var44].a_Ad = new d[var55];

              for (int var32 = 0; var32 < a_Aao[var44].a_AS.length; var32++) {
                var55 = var15.readShort();
                a_Aao[var44].a_AS[var32] = var55;
                a_Aao[var44].a_Ad[var32] = new d();
                a_Aao[var44].a_Ad[var32].a_S = var55;
                a_Aao[var44].a_Ad[var32].a_B = var15.readByte();
                a_Aao[var44].a_Ad[var32].a_I = var15.readInt();
                a_Aao[var44].a_Ad[var32].b_I = var15.readInt();
                a_Aao[var44].a_Ad[var32].b_B = var15.readByte();
              }
            }

            short var33;
            if ((var33 = var15.readShort()) > 0) {
              a_Aao[var44].b_AS = new short[var33];
              a_Aao[var44].a_Ack = new ck[var33];

              for (int var57 = 0; var57 < a_Aao[var44].b_AS.length; var57++) {
                byte var6 = var15.readByte();
                a_Aao[var44].b_AS[var57] = (short)var6;
                if (var6 > 0) {
                  var15.readByte();
                  a_Aao[var44].a_Ack[var57] = new ck(var6);
                  a_Aao[var44].a_Ack[var57].a_B = var15.readByte();
                  a_Aao[var44].a_Ack[var57].d_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].e_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].f_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].g_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].c_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].h_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].i_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].j_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].k_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].l_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].m_S = var15.readShort();
                  a_Aao[var44].a_Ack[var57].n_S = var15.readShort();
                }
              }
            }

            a_Aao[var44].a_S = var15.readShort();
            a_Aao[var44].b_S = var15.readShort();
            a_Aao[var44].b_B = var15.readByte();
            a_Aao[var44].c_B = var15.readByte();
          }
        }

        byte var45;
        if ((var45 = var15.readByte()) > 0) {
          a_Aau = new au[var45];

          for (int var50 = 0; a_Aau != null && var50 < a_Aau.length; var50++) {
            a_Aau[var50] = new au();
            a_Aau[var50].a_I = var15.readInt();
            a_Aau[var50].a_String = var15.readUTF();
            byte var58;
            if ((var58 = var15.readByte()) > 0) {
              a_Aau[var50].a_AI = new int[var58];

              for (int var34 = 0; var34 < var58; var34++) {
                a_Aau[var50].a_AI[var34] = var15.readInt();
              }
            }

            a_Aau[var50].a_Z = var15.readBoolean();
            a_Aau[var50].a_AB = new byte[4];

            for (int var35 = 0; var35 < 4; var35++) {
              a_Aau[var50].a_AB[var35] = var15.readByte();
            }

            byte var36;
            if ((var36 = var15.readByte()) > 0) {
              a_Aau[var50].a_AS = new short[var36];
              a_Aau[var50].a_Ad = new d[var36];

              for (int var59 = 0; var59 < var36; var59++) {
                short var63 = var15.readShort();
                a_Aau[var50].a_AS[var59] = var63;
                a_Aau[var50].a_Ad[var59] = new d();
                a_Aau[var50].a_Ad[var59].a_S = var63;
                a_Aau[var50].a_Ad[var59].a_B = var15.readByte();
                a_Aau[var50].a_Ad[var59].a_I = var15.readInt();
                a_Aau[var50].a_Ad[var59].b_I = var15.readInt();
                a_Aau[var50].a_Ad[var59].b_B = var15.readByte();
              }
            }

            a_Aau[var50].b_I = var15.readInt();
            a_Aau[var50].b_String = var15.readUTF();
            a_Aau[var50].a_B = var15.readByte();
            a_Aau[var50].a_AAB = new byte[2][2];

            for (int var60 = 0; var60 < 2; var60++) {
              a_Aau[var50].a_AAB[var60][0] = var15.readByte();
              a_Aau[var50].a_AAB[var60][1] = var15.readByte();
            }
          }
        }

        e_S = var15.readShort();
        if ((var47 = var15.readShort()) > 0) {
          p_AS = new short[var47];

          for (int var61 = 0; var61 < p_AS.length; var61++) {
            p_AS[var61] = var15.readShort();
          }
        } else {
          p_AS = null;
        }

        short var62;
        if ((var62 = var15.readShort()) > 0) {
          a_AZ = new boolean[var62];

          for (int var37 = 0; var37 < var62; var37++) {
            a_AZ[var37] = var15.readByte() == 1;
          }
        } else {
          a_AZ = null;
        }

        d_S = var15.readShort();
        e_B = var15.readByte();
        df.a_Z = var15.readBoolean();
        b_I = var15.readInt();
        c_AString[0] = var15.readUTF();
        c_I = var15.readInt();
        var1.closeRecordStore();
      } catch (IOException var10) {
        var10.printStackTrace();
        return false;
      } catch (RecordStoreNotOpenException var11) {
        var11.printStackTrace();
        return false;
      } catch (RecordStoreFullException var12) {
        var12.printStackTrace();
        return false;
      } catch (RecordStoreException var13) {
        var13.printStackTrace();
        return false;
      }

      a_Z = true;
      return true;
    } else {
      return false;
    }
  }

  public static boolean b(String var0) {
    RecordStore var1 = null;

    try {
      (var1 = RecordStore.openRecordStore(var0, true)).closeRecordStore();
      RecordStore.deleteRecordStore(var0);
      var1 = RecordStore.openRecordStore(var0, true);
    } catch (RecordStoreFullException var7) {
      var7.printStackTrace();
    } catch (RecordStoreNotFoundException var8) {
      var8.printStackTrace();
    } catch (RecordStoreException var9) {
      var9.printStackTrace();
    }

    ByteArrayOutputStream var10 = new ByteArrayOutputStream();
    DataOutputStream var2 = new DataOutputStream(var10);

    try {
      var2.writeUTF(df.a_String);
      var2.writeUTF(df.b_String);
      var2.writeByte(df.d_Z ? 1 : 0);
      var2.writeByte(df.c_Z ? 1 : 0);
      var2.writeByte(df.b_Z ? 1 : 0);
      var2.writeByte(df.c_B);
      var2.writeInt(df.a_I);
      var2.writeByte(g_B);
      byte[] var11 = var10.toByteArray();
      var1.addRecord(var11, 0, var11.length);
      var1.closeRecordStore();
      return true;
    } catch (IOException var3) {
      var3.printStackTrace();
      return false;
    } catch (RecordStoreNotOpenException var4) {
      var4.printStackTrace();
      return false;
    } catch (RecordStoreFullException var5) {
      var5.printStackTrace();
      return false;
    } catch (RecordStoreException var6) {
      var6.printStackTrace();
      return false;
    }
  }

  public static boolean b(byte var0) {
    b(r_String);
    a(a_Abl);
    a(a_Acv);
    RecordStore var1 = null;
    String var2 = n_String;
    if (var0 == 0) {
      var2 = n_String;
    } else if (var0 == 1) {
      var2 = o_String;
    } else if (var0 == 2) {
      var2 = p_String;
    }

    try {
      (var1 = RecordStore.openRecordStore(var2, true)).closeRecordStore();
      RecordStore.deleteRecordStore(var2);
      var1 = RecordStore.openRecordStore(var2, true);
    } catch (RecordStoreFullException var7) {
      var7.printStackTrace();
    } catch (RecordStoreNotFoundException var8) {
      var8.printStackTrace();
    } catch (RecordStoreException var9) {
      var9.printStackTrace();
    }

    ByteArrayOutputStream var14 = new ByteArrayOutputStream();
    DataOutputStream var15 = new DataOutputStream(var14);

    try {
      var15.writeByte(a_B);
      var15.writeShort(b_S);
      var15.writeByte(at.b_Z ? 1 : 0);
      int var3 = (short)(c_AS != null ? c_AS.length : 0);
      var15.writeShort(var3);
      if (var3 > 0) {
        for (int var4 = 0; var4 < var3; var4++) {
          var15.writeShort(c_AS[var4]);
        }
      }

      int var30 = d_AS != null ? d_AS.length : 0;
      var15.writeShort(var30);
      if (var30 > 0) {
        for (int var16 = 0; var16 < var30; var16++) {
          var15.writeShort(d_AS[var16]);
        }
      }

      var3 = a_AI != null ? a_AI.length : 0;
      var15.writeShort(var3);
      if (var3 > 0) {
        for (int var31 = 0; var31 < var3; var31++) {
          var15.writeShort(a_AI[var31]);
        }
      }

      int var32 = (short)(m_AS != null ? m_AS.length : 0);
      var15.writeShort(var32);
      if (var32 > 0) {
        for (int var18 = 0; var18 < var32; var18++) {
          var15.writeShort(m_AS[var18]);
        }
      }

      short var19 = (short)(n_AS != null ? n_AS.length : 0);
      var15.writeShort(var19);
      if (var19 > 0) {
        for (int var33 = 0; var33 < var19; var33++) {
          var15.writeShort(n_AS[var33]);
        }
      }

      var32 = o_AS != null ? o_AS.length : 0;
      var15.writeShort(var32);
      if (var32 > 0) {
        for (int var20 = 0; var20 < var32; var20++) {
          var15.writeShort(o_AS[var20]);
        }
      }

      var15.writeByte(b_AB.length);
      var15.write(b_AB);
      if (v_AS != null) {
        var15.writeByte(v_AS.length);

        for (int var21 = 0; var21 < v_AS.length; var21++) {
          var15.writeShort(v_AS[var21]);
        }
      } else {
        var15.writeByte(0);
      }

      if (b_AI != null) {
        var15.writeByte(b_AI.length);

        for (int var22 = 0; var22 < b_AI.length; var22++) {
          var15.writeInt(b_AI[var22]);
        }
      } else {
        var15.writeByte(0);
      }

      if (a_AAS != null) {
        var15.writeShort(a_AAS.length);
        System.out.println("Lưu");

        for (int var23 = 0; var23 < a_AAS.length; var23++) {
          var15.writeShort(a_AAS[var23][0]);
          var15.writeShort(a_AAS[var23][1]);
        }
      } else {
        var15.writeShort(0);
      }

      for (int var24 = 0; var24 < 6; var24++) {
        var32 = b_AAS[var24] != null ? b_AAS[var24].length : 0;
        var15.writeShort(var32);
        if (var32 > 0) {
          for (int var5 = 0; var5 < var32; var5++) {
            short var6 = b_AAS[var24][var5];
            var15.writeByte(var6);
            if (var6 > 0) {
              var15.writeByte(a_AAck[var24][var5].b_B);
              var15.writeByte(a_AAck[var24][var5].a_B);
              var15.writeShort(a_AAck[var24][var5].d_S);
              var15.writeShort(a_AAck[var24][var5].e_S);
              var15.writeShort(a_AAck[var24][var5].f_S);
              var15.writeShort(a_AAck[var24][var5].g_S);
              var15.writeShort(a_AAck[var24][var5].c_S);
              var15.writeShort(a_AAck[var24][var5].h_S);
              var15.writeShort(a_AAck[var24][var5].i_S);
              var15.writeShort(a_AAck[var24][var5].j_S);
              var15.writeShort(a_AAck[var24][var5].k_S);
              var15.writeShort(a_AAck[var24][var5].l_S);
              var15.writeShort(a_AAck[var24][var5].m_S);
              var15.writeShort(a_AAck[var24][var5].n_S);
            }
          }
        }
      }

      if (a_I < 0) {
        a_I = 99999;
      }

      var15.writeInt(a_I);
      var15.writeByte(a_Aao != null ? a_Aao.length : 0);

      for (int var25 = 0; a_Aao != null && var25 < a_Aao.length; var25++) {
        var15.writeByte(a_Aao[var25].a_B);
        var15.writeByte(a_Aao[var25].a_AI.length);

        for (int var36 = 0; var36 < a_Aao[var25].a_AI.length; var36++) {
          var15.writeInt(a_Aao[var25].a_AI[var36]);
        }

        for (int var37 = 0; var37 < a_Aao[var25].a_AB.length; var37++) {
          var15.writeByte(a_Aao[var25].a_AB[var37]);
        }

        for (int var38 = 0; var38 < a_Aao[var25].a_AAB.length; var38++) {
          for (int var45 = 0; var45 < a_Aao[var25].a_AAB[var38].length; var45++) {
            var15.writeByte(a_Aao[var25].a_AAB[var38][var45]);
          }
        }

        if (a_Aao[var25].a_AS != null) {
          var15.writeByte(a_Aao[var25].a_AS.length);

          for (int var39 = 0; var39 < a_Aao[var25].a_AS.length; var39++) {
            var15.writeShort(a_Aao[var25].a_AS[var39]);
            var15.writeByte(a_Aao[var25].a_Ad[var39].a_B);
            var15.writeInt(a_Aao[var25].a_Ad[var39].a_I);
            var15.writeInt(a_Aao[var25].a_Ad[var39].b_I);
            var15.writeByte(a_Aao[var25].a_Ad[var39].b_B);
          }
        } else {
          var15.writeByte(0);
        }

        if (a_Aao[var25].b_AS != null) {
          var15.writeShort(a_Aao[var25].b_AS.length);

          for (int var40 = 0; var40 < a_Aao[var25].b_AS.length; var40++) {
            short var46 = a_Aao[var25].b_AS[var40];
            var15.writeByte(var46);
            if (var46 > 0) {
              if (a_Aao[var25].a_Ack == null) {
                System.out.println("roleDatas[i].equip==null");
              }

              var15.writeByte(a_Aao[var25].a_Ack[var40].b_B);
              var15.writeByte(a_Aao[var25].a_Ack[var40].a_B);
              var15.writeShort(a_Aao[var25].a_Ack[var40].d_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].e_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].f_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].g_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].c_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].h_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].i_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].j_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].k_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].l_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].m_S);
              var15.writeShort(a_Aao[var25].a_Ack[var40].n_S);
            }
          }
        } else {
          var15.writeShort(0);
        }

        if (a_Aao[var25].a_B == a_Abl[0].h_S) {
          a_Aao[var25].a_S = a_Abl[0].d_S;
          a_Aao[var25].b_S = a_Abl[0].e_S;
          a_Aao[var25].b_B = a_Abl[0].d_B;
        }

        var15.writeShort(a_Aao[var25].a_S);
        var15.writeShort(a_Aao[var25].b_S);
        var15.writeByte(a_Aao[var25].b_B);
        var15.writeByte(a_Aao[var25].c_B);
      }

      var15.writeByte(a_Aau != null ? a_Aau.length : 0);

      for (int var26 = 0; a_Aau != null && var26 < a_Aau.length; var26++) {
        var15.writeInt(a_Aau[var26].a_I);
        var15.writeUTF(a_Aau[var26].a_String);
        System.out.println("=========== mypetdatas[i].id" + a_Aau[var26].a_I + "mypetdatas[i].petName" + a_Aau[var26].a_String);
        var15.writeByte(a_Aau[var26].a_AI.length);

        for (int var41 = 0; var41 < a_Aau[var26].a_AI.length; var41++) {
          var15.writeInt(a_Aau[var26].a_AI[var41]);
        }

        var15.writeBoolean(a_Aau[var26].a_Z);

        for (int var42 = 0; var42 < a_Aau[var26].a_AB.length; var42++) {
          var15.writeByte(a_Aau[var26].a_AB[var42]);
        }

        if (a_Aau[var26].a_AS != null) {
          var15.writeByte(a_Aau[var26].a_AS.length);

          for (int var43 = 0; var43 < a_Aau[var26].a_AS.length; var43++) {
            var15.writeShort(a_Aau[var26].a_AS[var43]);
            var15.writeByte(a_Aau[var26].a_Ad[var43].a_B);
            var15.writeInt(a_Aau[var26].a_Ad[var43].a_I);
            var15.writeInt(a_Aau[var26].a_Ad[var43].b_I);
            var15.writeByte(a_Aau[var26].a_Ad[var43].b_B);
          }
        } else {
          var15.writeByte(0);
        }

        var15.writeInt(a_Aau[var26].b_I);
        var15.writeUTF(a_Aau[var26].b_String);
        var15.writeByte(a_Aau[var26].a_B);

        for (int var44 = 0; var44 < 2; var44++) {
          var15.writeByte(a_Aau[var26].a_AAB[var44][0]);
          var15.writeByte(a_Aau[var26].a_AAB[var44][1]);
        }
      }

      var15.writeShort(e_S);
      if (p_AS != null) {
        var15.writeShort(p_AS.length);

        for (int var27 = 0; var27 < p_AS.length; var27++) {
          var15.writeShort(p_AS[var27]);
        }
      } else {
        var15.writeShort(0);
      }

      if (a_AZ != null) {
        var15.writeShort(a_AZ.length);

        for (int var28 = 0; var28 < a_AZ.length; var28++) {
          var15.writeByte(a_AZ[var28] ? 1 : 0);
        }
      } else {
        var15.writeShort(0);
      }

      var15.writeShort(d_S);
      var15.writeByte(e_B);
      var15.writeBoolean(df.a_Z);
      var15.writeInt(b_I);
      var15.writeUTF(c_AString[0]);
      var15.writeInt(c_I);
      byte[] var29 = var14.toByteArray();
      var1.addRecord(var29, 0, var29.length);
      var1.closeRecordStore();
      return true;
    } catch (IOException var10) {
      var10.printStackTrace();
      return false;
    } catch (RecordStoreNotOpenException var11) {
      var11.printStackTrace();
      return false;
    } catch (RecordStoreFullException var12) {
      var12.printStackTrace();
      return false;
    } catch (RecordStoreException var13) {
      var13.printStackTrace();
      return false;
    }
  }

  public static boolean a_Z() {
    RecordStore var0 = null;
    byte[] var1 = null;

    try {
      RecordEnumeration var2;
      if ((var2 = (var0 = RecordStore.openRecordStore(m_String, false)).enumerateRecords(null, null, false)).hasNextElement()) {
        int var12 = var2.nextRecordId();
        var1 = var0.getRecord(var12);
      }
    } catch (RecordStoreFullException var7) {
      var7.printStackTrace();
    } catch (RecordStoreNotFoundException var8) {
      var8.printStackTrace();
    } catch (RecordStoreException var9) {
      var9.printStackTrace();
    }

    if (var1 != null) {
      ByteArrayInputStream var10 = new ByteArrayInputStream(var1);
      DataInputStream var11 = new DataInputStream(var10);

      try {
        for (int var13 = 0; var13 < 3; var13++) {
          c_AB[var13] = var11.readByte();
          if (c_AB[var13] == 1) {
            i_AString[var13] = var11.readUTF();
            j_AS[var13] = var11.readShort();
            k_AS[var13] = var11.readShort();
            l_AS[var13] = var11.readShort();
            d_AB[var13] = var11.readByte();
            e_AB[var13] = var11.readByte();
            f_AB[var13] = var11.readByte();
            g_AB[var13] = var11.readByte();
            h_AB[var13] = var11.readByte();
          }
        }

        var0.closeRecordStore();
        return true;
      } catch (IOException var3) {
        var3.printStackTrace();
        return false;
      } catch (RecordStoreNotOpenException var4) {
        var4.printStackTrace();
        return false;
      } catch (RecordStoreFullException var5) {
        var5.printStackTrace();
        return false;
      } catch (RecordStoreException var6) {
        var6.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  public static void f() {
    e_S = cg.a_S(cg.a_String("/bin/sn.bin"), "heroId");
    c_AS = null;
    d_AS = null;
    a_AI = null;
    b_AB = null;
    a_Abl = null;
    a_AAS = null;
    b_AAS = null;
    a_I = 0;
    a_Aao = null;
    a_AAck = null;
    a_Acv = null;
    v_AS = null;
    a_Aau = null;
    b_AI = null;
    a_AAString = null;
    f_AAString = null;
    g_AAString = null;
    h_AAString = null;
    j_AString = null;
    k_AString = null;
    b_AAString = null;
    c_AAString = null;
    d_AAString = null;
    a_Abp = null;
    e_AAString = null;
    m_AS = null;
    n_AS = null;
    o_AS = null;
    e_String = null;
  }

  public static void c_V(int var0) {
    ck.a_V();
    int var1 = ck.b_AS[var0 - 1] - 1;
    if (b_AAS[var1] != null && b_AAS[var1].length > 0) {
      int var2 = 0;

      for (int var3 = 0; var3 < b_AAS[var1].length; var3++) {
        if (b_AAS[var1][var3] == var0) {
          var2 = var3;
        }
      }

      b_AAS[var1] = cg.c(b_AAS[var1], var2);
      a_AAck[var1] = a(a_AAck[var1], var2);
    }
  }

  public static int b_I(int var0) {
    short var1 = 0;

    for (int var2 = 0; a_AAS != null && var2 < a_AAS.length; var2++) {
      if (a_AAS[var2] != null && a_AAS[var2][0] == var0) {
        var1 += a_AAS[var2][1];
      }
    }

    return var1;
  }

  private static void b(e var0, int var1) {
    var0.m_I += var1;
    if (var0.m_I > var0.a_AI[10]) {
      var0.m_I = var0.a_AI[10];
    }
  }

  private static void c(e var0, int var1) {
    var0.n_I += var1;
    if (var0.n_I > var0.a_AI[11]) {
      var0.n_I = var0.a_AI[11];
    }
  }

  private static void c(bl var0, int var1) {
    var0.a_AI[3] = var0.a_AI[3] + var1;
    if (var0.a_AI[3] > var0.a_AI[10]) {
      var0.a_AI[3] = var0.a_AI[10];
    }
  }

  private static void d(bl var0, int var1) {
    var0.a_AI[5] = var0.a_AI[5] + var1;
    if (var0.a_AI[5] > var0.a_AI[11]) {
      var0.a_AI[5] = var0.a_AI[11];
    }
  }

  public static void a(cv var0, int var1, int var2) {
    if (!cg.a_Z(var0.a_AS, var1)) {
      var0.a_AS = cg.a_AS(var0.a_AS, var1);
      d var3 = new d(var1, 1, 4);
      var0.a_Ad = a(var0.a_Ad, var3);
    }
  }

  public static void a(bl var0, int var1, int var2) {
    if (!cg.a_Z(var0.a_AS, var1)) {
      var0.a_AS = cg.a_AS(var0.a_AS, var1);
      d var3 = new d(var1, var2, var0.h_S);
      var0.a_Ad = a(var0.a_Ad, var3);
    }
  }

  public static void a_V(bl var0, int var1) {
    if (cg.a_Z(var0.a_AS, var1)) {
      for (int var2 = 0; var2 < var0.a_AS.length; var2++) {
        if (var0.a_AS[var2] == var1) {
          d[] var4 = var0.a_Ad;
          d[] var10001;
          if (var0.a_Ad != null && var4.length != 1) {
            for (int var5 = var2; var5 < var4.length - 1; var5++) {
              var4[var5] = var4[var5 + 1];
            }

            d[] var6 = new d[var4.length - 1];
            System.arraycopy(var4, 0, var6, 0, var4.length - 1);
            var10001 = var6;
          } else {
            var10001 = null;
          }

          var0.a_Ad = var10001;
          break;
        }
      }

      var0.a_AS = cg.b(var0.a_AS, var1);
    }
  }

  private static void b(cv var0, int var1) {
    var0.a_AI[1] = var0.a_AI[1] + var1;
    if (var0.a_AI[1] > var0.a_AI[2]) {
      var0.a_AI[1] = var0.a_AI[2];
    }
  }

  private static void c(cv var0, int var1) {
    var0.a_AI[4] = var0.a_AI[4] + var1;
    if (var0.a_AI[4] > var0.a_AI[5]) {
      var0.a_AI[4] = var0.a_AI[5];
    }
  }

  public static void a_V(cv var0, int var1) {
    if (b_I(var1) > 0) {
      d();
      if (f_AString[var1 - 1].equals("Thuốc hồi HP")) {
        if (var0.a_AI[1] > 0) {
          b(var0, 1000);
        }

        da.a_da.a_cd.a_as.a("HP +1000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi HP lớn")) {
        if (var0.a_AI[1] > 0) {
          b(var0, 3000);
        }

        da.a_da.a_cd.a_as.a("HP +3000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi MP")) {
        if (var0.a_AI[1] > 0) {
          c(var0, 500);
        }

        da.a_da.a_cd.a_as.a("MP +500", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi MP lớn")) {
        if (var0.a_AI[1] > 0) {
          c(var0, 1000);
        }

        da.a_da.a_cd.a_as.a("MP +1000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi toàn phần")) {
        if (var0.a_AI[1] > 0) {
          var0.a_AI[1] = var0.a_AI[2];
          var0.a_AI[4] = var0.a_AI[5];
        }

        da.a_da.a_cd.a_as.a("Hồi đầy HP và MP", 10);
      } else if (f_AString[var1 - 1].equals("Thịt kém")) {
        if (var0.a_AI[1] > 0) {
          b(var0, 150);
        }

        da.a_da.a_cd.a_as.a("HP +150", 10);
      } else if (f_AString[var1 - 1].equals("Thịt thường")) {
        if (var0.a_AI[1] > 0) {
          b(var0, 300);
        }

        da.a_da.a_cd.a_as.a("HP +300", 10);
      } else if (f_AString[var1 - 1].equals("Thịt ngon")) {
        if (var0.a_AI[1] > 0) {
          b(var0, 600);
        }

        da.a_da.a_cd.a_as.a("HP +600", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc Lạc Thiên")) {
        da.a_da.a_cd.a_as.a("Trung thành +5", 10);
      } else if (f_AString[var1 - 1].equals("Sách Công Kích")) {
        var0.a_AI[7] = var0.a_AI[7] + 2;
        da.a_da.a_cd.a_as.a("Tăng 2 ATK", 10);
      } else if (f_AString[var1 - 1].equals("Sách Phòng Thủ")) {
        var0.a_AI[9] = var0.a_AI[9] + 2;
        da.a_da.a_cd.a_as.a("Tăng 2 DEF", 10);
      } else if (f_AString[var1 - 1].equals("Sách Tấn Tật")) {
        var0.a_AI[11] = var0.a_AI[11] + 2;
        da.a_da.a_cd.a_as.a("Tăng 2 điểm nhanh nhẹn", 10);
      } else if (f_AString[var1 - 1].equals("Sách Sinh Mệnh")) {
        var0.a_AI[2] = var0.a_AI[2] + 5;
        da.a_da.a_cd.a_as.a("Tăng 5 HP tối đa", 10);
      } else if (f_AString[var1 - 1].equals("Sách Tụ Khí")) {
        var0.a_AI[5] = var0.a_AI[5] + 5;
        da.a_da.a_cd.a_as.a("Tăng 5 MP tối đa", 10);
      }

      if (u_AS[d_I(var1)] == 1) {
        c(var1, 1);
      }

      a(new cv[]{var0});
    }
  }

  public static void a(e var0, int var1) {
    if (b_I(var1) > 0) {
      d();
      if (!f_AString[var1 - 1].equals("Thuốc Lạc Thiên")) {
        if (f_AString[var1 - 1].equals("Thuốc hồi HP")) {
          if (var0.a_AI[3] > 0) {
            b(var0, 1000);
          }
        } else if (f_AString[var1 - 1].equals("Thuốc hồi HP lớn")) {
          if (var0.a_AI[3] > 0) {
            b(var0, 3000);
          }
        } else if (f_AString[var1 - 1].equals("Thuốc hồi MP")) {
          if (var0.a_AI[5] > 0) {
            c(var0, 500);
          }
        } else if (f_AString[var1 - 1].equals("Thuốc hồi MP lớn")) {
          if (var0.a_AI[5] > 0) {
            c(var0, 1000);
          }
        } else if (f_AString[var1 - 1].equals("Thuốc hồi toàn phần")) {
          if (var0.m_I > 0) {
            var0.m_I = var0.a_AI[10];
            var0.n_I = var0.a_AI[11];
          }
        } else if (f_AString[var1 - 1].equals("Thịt kém")) {
          if (var0.a_AI[3] > 0) {
            b(var0, 150);
          }
        } else if (f_AString[var1 - 1].equals("Thịt thường")) {
          if (var0.a_AI[3] > 0) {
            b(var0, 300);
          }
        } else if (f_AString[var1 - 1].equals("Thịt ngon")) {
          if (var0.a_AI[3] > 0) {
            b(var0, 600);
          }
        } else if (f_AString[var1 - 1].equals("Cola vạn năng") && da.a_da.a_cd.a_bn != null) {
          da.a_da.a_cd.a_bn.a_V(var0);
        }
      }

      if (u_AS[d_I(var1)] == 1) {
        c(var1, 1);
      }
    }
  }

  public static void b(bl var0, int var1) {
    boolean var2 = false;
    if (b_I(var1) > 0) {
      d();
      if (f_AString[var1 - 1].equals("Thuốc hồi HP")) {
        if (var0.a_AI[3] > 0) {
          c(var0, 1000);
        }

        da.a_da.a_cd.a_as.a("HP +1000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi HP lớn")) {
        if (var0.a_AI[3] > 0) {
          c(var0, 3000);
        }

        da.a_da.a_cd.a_as.a("HP +3000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi MP")) {
        if (var0.a_AI[5] > 0) {
          d(var0, 500);
        }

        da.a_da.a_cd.a_as.a("MP +500", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi MP lớn")) {
        if (var0.a_AI[5] > 0) {
          d(var0, 1000);
        }

        da.a_da.a_cd.a_as.a("MP +1000", 10);
      } else if (f_AString[var1 - 1].equals("Thuốc hồi toàn phần")) {
        if (var0.a_AI[3] > 0) {
          var0.a_AI[3] = var0.a_AI[10];
          var0.a_AI[5] = var0.a_AI[11];
        }

        da.a_da.a_cd.a_as.a("Hồi đầy HP và MP", 10);
      } else if (f_AString[var1 - 1].equals("Thịt kém")) {
        if (var0.a_AI[3] > 0) {
          c(var0, 150);
        }

        da.a_da.a_cd.a_as.a("HP +150", 10);
      } else if (f_AString[var1 - 1].equals("Thịt thường")) {
        if (var0.a_AI[3] > 0) {
          c(var0, 300);
        }

        da.a_da.a_cd.a_as.a("HP +300", 10);
      } else if (f_AString[var1 - 1].equals("Thịt ngon")) {
        if (var0.a_AI[3] > 0) {
          c(var0, 600);
        }

        da.a_da.a_cd.a_as.a("HP +600", 10);
      } else if (f_AString[var1 - 1].equals("Vé số sủng vật")) {
        if (k.a(0, 100) <= 20) {
          da.a_da.a_cd.a_as.a("Không nhận được gì", 10);
        } else {
          a_V(k.a(1, 120), -1);
          as.a_Acv = null;
          as.a_Acv = k.a_Acv();
          da.a_da.a_cd.a_as.a("Nhận sủng vật: " + a_Acv[a_Acv.length - 1].a_String, 10);
        }
      } else if (f_AString[var1 - 1].equals("Cuộn hồi thành")) {
        if (b_AS != null) {
          cd.c_S = b_AS[1];
          da.a_da.a_cd.a_V(b_AS[0]);
          ab.a_Z();
          da.a_da.a_cd.a_ci.a(0);
        } else {
          var2 = true;
          da.a_da.a_cd.a_as.a("Không dùng được ở cảnh này", 10);
        }
      } else if (f_AString[var1 - 1].equals("Sách Công Kích")) {
        var0.a_AI[7] = var0.a_AI[7] + 2;
        da.a_da.a_cd.a_as.a("Tăng 2 ATK", 10);
      } else if (f_AString[var1 - 1].equals("Sách Phòng Thủ")) {
        var0.a_AI[8] = var0.a_AI[8] + 2;
        da.a_da.a_cd.a_as.a("Tăng 2 DEF", 10);
      } else if (!f_AString[var1 - 1].equals("Sách Ma Công") && !f_AString[var1 - 1].equals("Sách Ma Kháng")) {
        if (f_AString[var1 - 1].equals("Sách Tấn Tật")) {
          var0.a_AI[9] = var0.a_AI[9] + 2;
          da.a_da.a_cd.a_as.a("Tăng 2 điểm nhanh nhẹn", 10);
        } else if (f_AString[var1 - 1].equals("Sách Sinh Mệnh")) {
          var0.a_AI[4] = var0.a_AI[4] + 5;
          da.a_da.a_cd.a_as.a("Tăng 5 HP tối đa", 10);
        } else if (f_AString[var1 - 1].equals("Sách Tụ Khí")) {
          var0.a_AI[6] = var0.a_AI[6] + 5;
          da.a_da.a_cd.a_as.a("Tăng 5 MP tối đa", 10);
        }
      }

      if (u_AS[d_I(var1)] == 1 && !var2) {
        c(var1, 1);
      }

      d(var0);
    }
  }

  public static void d(int var0, int var1) {
    short var3 = 0;
    if (b_AAS != null) {
      for (int var2 = 0; var2 < b_AAS.length; var2++) {
        if (b_AAS[var2] != null) {
          var3 = (short)(var3 + b_AAS[var2].length);
        }
      }
    }

    if (var3 < ct.a_S) {
      if (c_AAString == null) {
        c_AAString = cg.a_AAString(cg.a_String("/bin/equip.bin"), "equip", "equipend", null, "\t");
      }

      if (b_AAS == null) {
        b_AAS = new short[6][];
      }

      b_AAS[cg.a_I(c_AAString[var0 - 1][2]) - 1] = cg.a_AS(b_AAS[cg.a_I(c_AAString[var0 - 1][2]) - 1], (short)var0);
      if (a_AAck == null) {
        a_AAck = new ck[6][];
      }

      ck var4 = new ck(var0);
      a_AAck[cg.a_I(c_AAString[var0 - 1][2]) - 1] = a(a_AAck[cg.a_I(c_AAString[var0 - 1][2]) - 1], var4);
    } else {
      da.a_da.a("Túi đồ đã đầy", 10);
    }
  }

  public static boolean b_Z(int var0) {
    return cg.a_Z(a_AI, var0);
  }

  public static void g() {
    if (!df.b_Z) {
      a_I /= 2;
    }

    for (int var0 = 0; a_Abl != null && var0 < a_Abl.length; var0++) {
      if (a_Abl[var0].a_AI != null) {
        a_Abl[var0].a_AI[3] = a_Abl[var0].a_AI[10];
        a_Abl[var0].a_AI[5] = a_Abl[var0].a_AI[11];
      }
    }
  }

  public static void d_V(int var0) {
    for (int var1 = 0; a_Aau != null && var1 < a_Aau.length; var1++) {
      if (a_Aau[var1].b_I == var0) {
        au var3 = a_Aau[var1];
        boolean var4 = false;

        for (int var2 = 0; a_Aau != null && var2 < a_Aau.length; var2++) {
          if (var4) {
            if (var2 < a_Aau.length - 1) {
              a_Aau[var2] = a_Aau[var2 + 1];
            } else {
              a_Aau[var2] = null;
            }
          } else if (a_Aau[var2] == var3 && var2 < a_Aau.length - 1) {
            a_Aau[var2] = a_Aau[var2 + 1];
            var4 = true;
          }
        }

        au[] var5 = new au[a_Aau.length - 1];
        System.arraycopy(a_Aau, 0, var5, 0, a_Aau.length - 1);
        a_Aau = var5;
        if (var5.length == 0) {
          a_Aau = null;
        }

        return;
      }
    }
  }

  public static void e_V(int var0) {
    for (int var1 = 0; a_Acv != null && var1 < a_Acv.length; var1++) {
      if (a_Acv[var1].b_I == var0) {
        cv var3 = a_Acv[var1];
        boolean var4 = false;

        for (int var2 = 0; a_Acv != null && var2 < a_Acv.length; var2++) {
          if (var4) {
            if (var2 < a_Acv.length - 1) {
              a_Acv[var2] = a_Acv[var2 + 1];
            } else {
              a_Acv[var2] = null;
            }
          } else if (a_Acv[var2] == var3 && var2 < a_Acv.length - 1) {
            a_Acv[var2] = a_Acv[var2 + 1];
            var4 = true;
          }
        }

        cv[] var6 = new cv[a_Acv.length - 1];
        System.arraycopy(a_Acv, 0, var6, 0, a_Acv.length - 1);
        a_Acv = var6;
        if (var6.length == 0) {
          a_Acv = null;
        }

        for (int var5 = 0; var5 < b_AI.length; var5++) {
          if (b_AI[var5] == var3.b_I) {
            v_AS = cg.c(v_AS, var5);
            b_AI = cg.c(b_AI, var5);
            return;
          }
        }

        return;
      }
    }
  }

  public static void c(bl[] var0) {
    bl[] var1 = new bl[1];
    System.arraycopy(var0, 0, var1, 0, 1);
    a_Abl = var1;
  }

  public static void f_V(int var0) {
    for (int var1 = 0; var1 < a_Abl.length; var1++) {
      if (a_Abl[var1].h_S == var0) {
        bl var3 = a_Abl[var1];
        boolean var4 = false;

        for (int var2 = 0; a_Abl != null && var2 < a_Abl.length; var2++) {
          if (var4) {
            if (var2 < a_Abl.length - 1) {
              a_Abl[var2] = a_Abl[var2 + 1];
            } else {
              a_Abl[var2] = null;
            }
          } else if (a_Abl[var2] == var3 && var2 < a_Abl.length - 1) {
            a_Abl[var2] = a_Abl[var2 + 1];
            var4 = true;
          }
        }

        bl[] var6 = new bl[a_Abl.length - 1];
        System.arraycopy(a_Abl, 0, var6, 0, a_Abl.length - 1);
        a_Abl = var6;
        var6[0].e_V();
        var3.a_V(0);
        var3.a_S = cd.a_S;
        var3.b_S = cd.b_S;

        for (int var5 = 0; var5 < b_AB.length; var5++) {
          if (b_AB[var5] == var3.h_S) {
            b_AB = cg.c(b_AB, var5);
            return;
          }
        }

        return;
      }
    }
  }

  private static void g(int var0) {
    for (int var1 = 0; var1 < a_Abp.length; var1++) {
      if (a_Abp[var1].a_S == var0) {
        a(a_Abp[var1]);
        return;
      }
    }
  }

  public static void a(bp var0) {
    boolean var1 = false;

    for (int var2 = 0; a_Abp != null && var2 < a_Abp.length; var2++) {
      if (var1) {
        if (var2 < a_Abp.length - 1) {
          a_Abp[var2] = a_Abp[var2 + 1];
        } else {
          a_Abp[var2] = null;
        }
      } else if (a_Abp[var2] == var0 && var2 < a_Abp.length - 1) {
        a_Abp[var2] = a_Abp[var2 + 1];
        var1 = true;
      }
    }

    bp[] var3 = new bp[a_Abp.length - 1];
    System.arraycopy(a_Abp, 0, var3, 0, a_Abp.length - 1);
    a_Abp = var3;
  }

  public static boolean c_Z(int var0) {
    for (int var1 = 0; b_AAS != null && var1 < b_AAS.length; var1++) {
      for (int var2 = 0; b_AAS[var1] != null && var2 < b_AAS[var1].length; var2++) {
        if (b_AAS[var1][var2] == var0) {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean a_Z(bl var0, int var1) {
    for (int var2 = 0; var0.a_Ad != null && var2 < var0.a_Ad.length; var2++) {
      if (var0.a_Ad[var2].a_S == var1) {
        return true;
      }
    }

    return false;
  }

  public static boolean d_Z(int var0) {
    for (int var1 = 0; a_Acv != null && var1 < a_Acv.length; var1++) {
      if (a_Acv[var1].a_B == e_S && a_Acv[var1].a_I == var0) {
        return true;
      }
    }

    return false;
  }

  public static ck[] a(ck[] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      ck[] var3 = new ck[var0.length - 1];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  private static d[] a(d[] var0, d var1) {
    if (var0 == null) {
      (var0 = new d[1])[0] = var1;
      return var0;
    } else {
      d[] var2 = new d[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static ck[] a(ck[] var0, ck var1) {
    if (var0 == null) {
      (var0 = new ck[1])[0] = var1;
      return var0;
    } else {
      ck[] var2 = new ck[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static int c_I(int var0) {
    if (d.a_AS == null) {
      d.a();
    }

    int var1 = -1;

    for (int var2 = 0; var2 < d.a_AS.length; var2++) {
      if (d.a_AS[var2] == var0) {
        var1 = var2;
        break;
      }
    }

    return var1;
  }

  public static byte[][] a_AAB() {
    byte[][] var0 = null;

    for (int var1 = 0; a_AAS != null && var1 < a_AAS.length; var1++) {
      int var2;
      if ((var2 = d_I(a_AAS[var1][0])) != -1 && f_AS[var2] == 6) {
        byte[] var3 = cg.a_AB(cg.a_AB(null, a_AAS[var1][0]), a_AAS[var1][1]);
        var0 = cg.a(var0, var3);
      }
    }

    return var0;
  }

  public static int d_I(int var0) {
    if (e_AS == null) {
      d();
    }

    int var1 = -1;

    for (int var2 = 0; var2 < e_AS.length; var2++) {
      if (e_AS[var2] == var0) {
        var1 = var2;
        break;
      }
    }

    return var1;
  }

  public static int e_I(int var0) {
    if (e_AAString == null) {
      e_AAString = cg.a_AAString(cg.a_String("/bin/task.bin"), "task", "taskend", null, "\t");
    }

    int var1 = -1;

    for (int var2 = 0; var2 < e_AAString.length; var2++) {
      if (cg.a_I(e_AAString[var2][0]) == var0) {
        var1 = var2;
        break;
      }
    }

    return var1;
  }

  public static int f_I(int var0) {
    if (ck.a_AS == null) {
      ck.a_V();
    }

    int var1 = -1;

    for (int var2 = 0; var2 < ck.a_AS.length; var2++) {
      if (ck.a_AS[var2] == var0) {
        var1 = var2;
        break;
      }
    }

    return var1;
  }

  public static boolean a(short var0) {
    if (!cg.a_Z(n_AS, var0)) {
      n_AS = cg.a_AS(n_AS, var0);
      m_AS = cg.a_AS(m_AS, 0);
      b(a_bp(var0, -1));
    }

    return true;
  }

  public static boolean a(short var0, boolean var1) {
    var1 = -1;

    for (int var2 = 0; n_AS != null && var2 < n_AS.length; var2++) {
      if (n_AS[var2] == var0) {
        var1 = var2;
        break;
      }
    }

    if (var1 >= 0) {
      String[] var9 = null;
      if (!a_Abp[var1].e_String.equals("-1")) {
        String[] var3 = cg.b_AString(a_Abp[var1].e_String, "|");

        for (int var4 = 0; var4 < var3.length; var4++) {
          short[] var5;
          if ((var5 = cg.b_AS(var3[var4], ","))[0] == 1) {
            b(var5[1], var5[2]);
            d();
            var9 = cg.a(var9, "Nhận " + f_AString[d_I(var5[1])] + "  x" + var5[2]);
          } else if (var5[0] == 2) {
            d(var5[1], -1);
            ck.a_V();
            var9 = cg.a(var9, "Nhận " + ck.a_AString[f_I(var5[1])] + "  x1");
          } else if (var5[0] == 3) {
            a_I = a_I + var5[1];
            var9 = cg.a(var9, "Nhận vàng+" + var5[1]);
          } else if (var5[0] == 4) {
            a_V(var5[1], -1);
            short var7 = var5[1];
            String[][] var12 = cg.a_AAString(cg.a_String("/bin/petdata.bin"), "petdata", "end", null, "\t");
            int var10 = var7 - 1;
            String var11 = var12[var10][0];
            var9 = cg.a(var9, "Nhận sủng vật [" + var11 + "]");
          } else if (var5[0] == 5) {
            a_Abl[0].a_AI[1] = a_Abl[0].a_AI[1] + var5[1];

            for (int var6 = 0; a_Acv != null && var6 < a_Acv.length; var6++) {
              if (a_Acv[var6].a_B == e_S && a_Acv[var6].a_Z) {
                a_Acv[var6].a_AI[13] = a_Acv[var6].a_AI[13] + var5[1];
                cd.a(new cv[]{a_Acv[var6]});
              }
            }

            var9 = cg.a(var9, "Nhận EXP  +" + var5[1]);
            da.a_da.a_cd.a(a_Abl[0]);
            if (da.a_da.a_cd.n_Z) {
              var9 = cg.a(cg.a(var9, a_Abl[0].a_String + " đã lên cấp"), "Nhận 10 điểm");
              a_Abl[0].a_AI[16] = a_Abl[0].a_AI[16] + 10;
            }
          }
        }
      }

      if (a_Abp[var1].b_B == 0 && !cg.a_Z(o_AS, var0)) {
        o_AS = cg.a_AS(o_AS, var0);
      }

      n_AS = cg.c(n_AS, var1);
      m_AS = cg.c(m_AS, var1);
      g(var0);
      da.a_da.a_cd.a(var9);
      c_I += 5;
    } else {
      System.out.println("Nhiệm vụ cần xóa không tồn tại trong danh sách nhiệm vụ!");
    }

    return true;
  }
}
