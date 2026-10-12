import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;

public final class a {
  private static Hashtable a_Hashtable = new Hashtable();
  private static String[][] a_AAString = new String[2][];
  private static String[][] b_AAString = new String[2][];
  private static String[][] c_AAString = new String[1][];
  private static String[][] d_AAString = new String[1][];
  private static String a_String = "Fr";
  private static String b_String = "Sec";
  private static String c_String = "N7610";
  private static String d_String = "299";
  private static String e_String = "10661700299";
  private static String f_String = "Idx";
  private static String g_String = "0";
  private static int[] a_AI = new int[3];
  private static int a_I = 0;
  private static int[][] a_AAI = new int[2][];

  public a(MIDlet var1) {
    this.a(var1);
  }

  private static final String[] a(String var0, String var1) {
    if (var0 == null) {
      System.out.println(" split. aStr == null");
      return null;
    } else if (var0.length() > 0 && var1 != null) {
      if (var1.length() <= 0) {
        return new String[]{var0};
      } else {
        String[] var2 = null;
        String var3 = var0;
        var1 = var1;
        int[] var4 = new int[var0.length()];
        int[] var5 = new int[var0.length()];
        int var6 = 0;
        boolean var7 = false;
        int var8 = 0;

        do {
          boolean var10 = false;
          int var9;
          if ((var9 = var8 + var1.length()) <= var3.length() && var3.substring(var8, var9).equals(var1)) {
            var10 = true;
          }

          if (var10) {
            if (var7) {
              var7 = false;
              var5[var6++] = var8;
            }

            var8 = var9;
          } else {
            if (!var7) {
              var7 = true;
              var4[var6] = var8;
            }

            var8++;
          }
        } while (var8 < var3.length());

        if (var7) {
          var5[var6++] = var3.length();
        }

        if (var6 > 0) {
          var2 = new String[var6];

          for (int var12 = 0; var12 < var6; var12++) {
            var2[var12] = var0.substring(var4[var12], var5[var12]);
          }
        }

        return var2;
      }
    } else {
      return new String[]{var0};
    }
  }

  private static final String a_String(String var0) {
    if (var0 != null && var0.length() >= 32) {
      try {
        bq var1;
        (var1 = new bq()).a_AB("a4s3d5f1");
        String var5 = var1.a_String(var0.substring(0, var0.length() - 32));
        int var2 = 0;

        for (int var3 = 0; var3 < var5.length(); var3++) {
          if ((var5.charAt(var3) < 'a' || var5.charAt(var3) > 'z')
            && (var5.charAt(var3) < 'A' || var5.charAt(var3) > 'Z')
            && (var5.charAt(var3) < '0' || var5.charAt(var3) > '9')
            && var5.charAt(var3) != ';'
            && var5.charAt(var3) != ',') {
            var2 = var3;
            break;
          }
        }

        String var6 = var5.substring(0, var2);
        return new br().a(var6).equals(var0.substring(var0.length() - 32)) ? var6 : null;
      } catch (Exception var4) {
        var4.printStackTrace();
        return null;
      }
    } else {
      System.out.println("des. aStr ==null or aStr length < 32");
      return null;
    }
  }

  private static int a_I(String var0) {
    int var1 = 0;

    try {
      var1 = Integer.parseInt(var0);
    } catch (Exception var2) {
      var2.printStackTrace();
    }

    return var1;
  }

  private static String a(byte[] var0) {
    String var1;
    try {
      var1 = new String(var0, "utf-8");
    } catch (Exception var3) {
      try {
        var1 = new String(var0, "UTF-8");
      } catch (Exception var2) {
        var1 = new String(var0);
      }
    }

    return var1;
  }

  private static String[] a(int var0, String var1) {
    try {
      if (a_Hashtable.isEmpty()) {
        System.out.println("is empty");
      }

      if (a_Hashtable.containsKey(var1)) {
        Vector var3 = (Vector)a_Hashtable.get(var1);
        return var0 >= 0 && var0 < var3.size() ? (String[])var3.elementAt(var0) : null;
      } else {
        return null;
      }
    } catch (Exception var2) {
      var2.printStackTrace();
      return null;
    }
  }

  private void a_V() {
    InputStream var1;
    if ((var1 = new Object().getClass().getResourceAsStream("/cpid.txt")) != null) {
      try {
        byte[] var2 = new byte[var1.available()];
        var1.read(var2);
        String var16 = a(var2);
        System.out.println("cpidListStr=" + var16);
        String[] var17 = a(var16, "\n");
        System.out.println("splitArray.length=" + (var17 != null ? var17.length : 0));

        for (int var7 = 0; var7 < (var17 != null ? var17.length : 0); var7++) {
          String[] var3 = a(var17[var7], ",");
          System.out.println("lineArray=" + var3.length);
          System.out.println("lineArray[0]=" + var3[0]);
          System.out.println("lineArray[1]=" + var3[1]);
          System.out.println("lineArray[2]=" + var3[2].length() + "," + var3[2].trim().length());
          String var4 = var3[0].trim();
          String var5 = a_String(var3[1].trim());
          String var6 = a_String(var3[2].trim());
          var3 = new String[]{var5, var6};
          System.out.println("cpid=" + var4 + ",code=" + var5 + ",dest=" + var6);
          if (!a_Hashtable.containsKey(var4)) {
            Vector var19;
            (var19 = new Vector()).addElement(var3);
            a_Hashtable.put(var4, var19);
            System.out.println("put.cpid=" + var4 + ",CodeDestV.size()=" + var19.size());
          } else {
            Vector var20 = (Vector)a_Hashtable.get(var4);
            System.out.println("old.size()=" + var20.size());
            var20.addElement(var3);
            System.out.println("old.size.2()=" + var20.size());
            a_Hashtable.remove(var4);
            a_Hashtable.put(var4, var20);
            System.out.println("put.cpid=" + var4 + ",old.size()=" + var20.size());
            System.out.println("cpid=" + var4 + ",cpidHashtable=" + ((Vector)a_Hashtable.get(var4)).size());
          }
        }

        return;
      } catch (Exception var14) {
        var14.printStackTrace();
      } finally {
        try {
          if (var1 != null) {
            var1.close();
          }
        } catch (Exception var13) {
        }
      }
    }
  }

  private void a(MIDlet var1) {
    try {
      String var14;
      if ((var14 = var1.getAppProperty("ChannelFirst")) != null) {
        a_String = var14;
      }

      if ((var14 = var1.getAppProperty("ChannelSecond")) != null) {
        b_String = var14;
      }

      if ((var14 = var1.getAppProperty("InterChannel")) != null) {
        f_String = var14;
      }

      if ((var14 = var1.getAppProperty("Term")) != null) {
        c_String = var14;
      }

      if ((var14 = var1.getAppProperty("QQ")) != null) {
        g_String = var14;
      }

      var1.getAppProperty("GameCenterName");
      var1.getAppProperty("GameCenterUrl");
      if ((var14 = a_String(var1.getAppProperty("FreeCode"))) != null) {
        d_String = var14;
      }

      if ((var14 = a_String(var1.getAppProperty("FreeDest"))) != null) {
        e_String = var14;
      }

      var14 = a_String(var1.getAppProperty("SmsCode1"));
      String var24 = a_String(var1.getAppProperty("SmsCode2"));
      String var4 = a_String(var1.getAppProperty("SmsDest1"));
      String var5 = a_String(var1.getAppProperty("SmsDest2"));
      a_AAString[0] = a(var14, ";");
      a_AAString[1] = a(var24, ";");
      b_AAString[0] = a(var4, ";");
      b_AAString[1] = a(var5, ";");
      String[] var22 = a(var1.getAppProperty("LogoLinkReco"), ";");

      for (int var25 = 0; var25 < (var22 != null ? (var22.length > 3 ? 3 : var22.length) : 0); var25++) {
        a_AI[var25] = a_I(var22[var25]);
      }

      if ((var24 = var1.getAppProperty("FeeMode")) != null) {
        a_I = a_I(var24);
      }

      var14 = a_String(var1.getAppProperty("DoCode"));
      String var10 = a_String(var1.getAppProperty("DoDest"));
      c_AAString[0] = a(var14, ";");
      d_AAString[0] = a(var10, ";");
      return;
    } catch (NullPointerException var8) {
      var8.printStackTrace();
    } finally {
      int var2 = 1;
      if (a_AAString[0] != null && b_AAString[0] != null) {
        if (a_AAString[0].length != b_AAString[0].length) {
          var2 = 0;
        }
      } else {
        var2 = 0;
      }

      if (!var2) {
        a_AAString[0] = null;
        b_AAString[0] = null;
        a_AAString[0] = new String[2];
        b_AAString[0] = new String[2];
        a_AAString[0][0] = "62";
        a_AAString[0][1] = "63";
        b_AAString[0][0] = "106662267";
        b_AAString[0][1] = "106662269";
      }

      var2 = (boolean)1;
      if (a_AAString[1] != null && b_AAString[1] != null) {
        if (a_AAString[1].length != b_AAString[1].length) {
          var2 = (boolean)0;
        }
      } else {
        var2 = (boolean)0;
      }

      if (!var2) {
        a_AAString[1] = null;
        b_AAString[1] = null;
        a_AAString[1] = new String[1];
        b_AAString[1] = new String[1];
        a_AAString[1][0] = "JG";
        b_AAString[1][0] = "1066170073";
      }

      var2 = (boolean)1;
      if (c_AAString[0] != null && d_AAString[0] != null) {
        if (c_AAString[0].length != d_AAString[0].length) {
          var2 = (boolean)0;
        }
      } else {
        var2 = (boolean)0;
      }

      if (!var2) {
        c_AAString[0] = null;
        d_AAString[0] = null;
        this.a_V();
      }

      var2 = 0;

      while (true) {
        if (var2 >= 2) {
          ;
        } else {
          a_AAI[var2] = new int[a_AAString[var2].length];

          for (int var3 = 0; var3 < a_AAString[var2].length; var3++) {
            a_AAI[var2][var3] = 0;
          }

          var2++;
        }
      }
    }
  }

  public static String[] a(int var0) {
    if (var0 != 1 && var0 != 2) {
      var0 = 2;
    }

    int var2 = 0;
    int var1 = 100000;
    boolean var3 = true;

    for (int var4 = 0; var4 < a_AAI[var0 - 1].length; var4++) {
      if (a_AAI[var0 - 1][var4] <= var1) {
        var2 = var4;
        var1 = a_AAI[var0 - 1][var4];
      }

      if (a_AAI[var0 - 1][var4] != 0) {
        var3 = false;
      }
    }

    if (var3) {
      var2 = Math.abs(new Random().nextInt()) % 100 % a_AAString[var0 - 1].length;
      System.out.println("Random minId :" + var2);
    }

    if (var2 < 0 || var2 >= a_AAString[var0 - 1].length) {
      var2 = 0;
    }

    String[] var5;
    (var5 = new String[2])[0] = a_AAString[var0 - 1][var2];
    var5[1] = b_AAString[var0 - 1][var2];
    a_AAI[var0 - 1][var2]++;
    return var5;
  }

  public static String a_String() {
    return a_String;
  }

  public static String b() {
    return b_String;
  }

  public static String c() {
    return c_String;
  }

  public static String[] a_AString() {
    String[] var0;
    (var0 = new String[2])[0] = d_String;
    var0[1] = e_String;
    return var0;
  }

  public static String d() {
    return f_String;
  }

  public static String e() {
    return g_String;
  }

  public static int a_I() {
    return a_I;
  }

  public final String[] a(int var1, int var2) {
    if (var1 == 0) {
      return null;
    } else {
      try {
        var1--;
        if (c_AAString[0] != null && var1 >= 0 && var1 < c_AAString[0].length) {
          if (c_AAString[0][var1] != null && d_AAString[0][var1] != null && c_AAString[0][var1].length() > 0 && d_AAString[0][var1].length() > 0) {
            String[] var5;
            (var5 = new String[2])[0] = c_AAString[0][var1];
            var5[1] = d_AAString[0][var1];
            return var5;
          } else {
            return this.b(var1, var2);
          }
        } else {
          return this.b(var1, var2);
        }
      } catch (Exception var3) {
        return null;
      }
    }
  }

  private String[] b(int var1, int var2) {
    String var3 = Integer.toString(var2);
    return a(var1, var3);
  }
}
