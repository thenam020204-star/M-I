import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class i {
  private static Vector a_Vector;
  private static byte a_B;
  private static Image a_Image;
  private static Image b_Image;

  public static void a(int var0) {
    if (var0 == 0) {
      a_Vector = null;
      a_Image = null;
    } else {
      if (var0 == 1) {
        a_V((byte)2);
        if (!a_Z((byte)1)) {
          a();
          return;
        }
      } else if (var0 == 2) {
        a_V((byte)1);
        if (!a_Z((byte)2)) {
          b();
          return;
        }
      } else if (var0 == 3) {
        if (!a_Z((byte)1)) {
          a();
        }

        if (!a_Z((byte)2)) {
          b();
        }
      }
    }
  }

  public static void a(Graphics var0) {
    for (int var1 = 0; a_Vector != null && var1 < a_Vector.size(); var1++) {
      if ((a_B = ((cm)a_Vector.elementAt(var1)).a_B) == 1) {
        if (a_Image != null) {
          if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
            cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 0, 1, da.a_da.c_S - 1, 36, 0);
          } else {
            cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 0, da.a_da.b_S - 1, da.a_da.c_S - 1, 40, 0);
          }
        } else {
          a_Image = cc.a_Image("/pic/D23.gf", 1);
        }
      }

      if (a_B == 2) {
        if (da.a_B == 4) {
          if (da.a_da.a_cd.b_B != 1 && da.a_da.a_cd.b_B != 2 && da.a_da.a_cd.b_B != 3) {
            if (a_Image != null) {
              if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
                cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 1, da.a_da.b_S - 1, da.a_da.c_S - 1, 40, 0);
              } else {
                cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 1, 1, da.a_da.c_S - 1, 36, 0);
              }
            } else {
              a_Image = cc.a_Image("/pic/D23.gf", 1);
            }
          }
        } else if (a_Image != null) {
          if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
            cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 1, da.a_da.b_S - 1, da.a_da.c_S - 1, 40, 0);
          } else {
            cg.a(var0, a_Image, a_Image.getWidth() / 2, a_Image.getHeight(), 1, 1, da.a_da.c_S - 1, 36, 0);
          }
        } else {
          a_Image = cc.a_Image("/pic/D23.gf", 1);
        }
      }

      if (a_B == 5 && da.a_B == 4 && da.a_da.a_cd.b_B == 1) {
        if (b_Image != null) {
          var0.drawImage(b_Image, at.a_AS[0] / 2, at.a_AS[1] - 20, 17);
        } else {
          b_Image = cc.a("/pic/9.gf");
        }
      }
    }
  }

  private static void a() {
    cm var0;
    (var0 = new cm()).a_B = 1;
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      var0.a_AS = new short[]{0, (short)(da.a_da.c_S - 26), 26, 26};
    } else {
      var0.a_AS = new short[]{(short)(da.a_da.b_S - 26), (short)(da.a_da.c_S - 26), 26, 26};
    }

    a(var0);
  }

  private static void b() {
    cm var0;
    (var0 = new cm()).a_B = 2;
    if (!at.a_String.equals("E2") && !at.a_String.equals("V8")) {
      var0.a_AS = new short[]{(short)(da.a_da.b_S - 26), (short)(da.a_da.c_S - 26), 26, 26};
    } else {
      var0.a_AS = new short[]{0, (short)(da.a_da.c_S - 26), 26, 26};
    }

    a(var0);
  }

  public static boolean a_Z(byte var0) {
    for (int var1 = 0; a_Vector != null && var1 < a_Vector.size(); var1++) {
      if (((cm)a_Vector.elementAt(var1)).a_B == var0) {
        return true;
      }
    }

    return false;
  }

  private static void a_V(byte var0) {
    int var1 = -1;

    for (int var2 = 0; a_Vector != null && var2 < a_Vector.size(); var2++) {
      if (((cm)a_Vector.elementAt(var2)).a_B == var0) {
        var1 = var2;
      }
    }

    if (var1 >= 0) {
      a_Vector.removeElementAt(var1);
    }
  }

  public static void a(cm var0) {
    if (var0 != null) {
      if (a_Vector == null) {
        a_Vector = new Vector(1, 1);
      }

      a_Vector.addElement(var0);
    }
  }

  public static cm a(int var0, int var1) {
    cm var2 = null;

    for (int var3 = 0; a_Vector != null && var3 < a_Vector.size(); var3++) {
      short[] var4;
      if (cg.a((var4 = ((cm)a_Vector.elementAt(var3)).a_AS)[0], var4[1], var4[2], var4[3], var0, var1, 0, 0)) {
        var2 = (cm)a_Vector.elementAt(var3);
      }
    }

    return var2;
  }
}
