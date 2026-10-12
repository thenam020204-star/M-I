import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class vh {
  private static final int MISS = 12;
  public static int H_I = 15;
  private static Image boxImg;
  private static int boxW;
  private static int boxH;

  public static String rn(String var0) {
    if (var0 == null) {
      return null;
    } else if (var0.endsWith(".gf")) {
      return var0.substring(0, var0.length() - 3).concat(".png");
    } else {
      return var0.endsWith(".bin") ? var0.substring(0, var0.length() - 4).concat(".txt") : var0;
    }
  }

  public static String num(String var0, int var1) {
    int var2 = var0.length();
    int var3 = var2;

    while (var3 > 0 && var0.charAt(var3 - 1) >= '0' && var0.charAt(var3 - 1) <= '9') {
      var3--;
    }

    return var3 < var2 ? var0.substring(var3) : var0.substring(var1);
  }

  public static void box(Graphics var0, int var1, int var2) {
    if (var1 > 0 && var2 > 0) {
      if (boxImg == null || boxW != var1 || boxH != var2) {
        boxImg = null;
        int[] var3 = new int[var1 * var2];

        for (int var4 = 0; var4 < var2; var4++) {
          for (int var5 = 0; var5 < var1; var5++) {
            boolean var6 = var5 == 0 || var4 == 0 || var5 == var1 - 1 || var4 == var2 - 1;
            boolean var7 = (var5 == 0 || var5 == var1 - 1) && (var4 == 0 || var4 == var2 - 1);
            var3[var4 * var1 + var5] = var7 ? 0 : (var6 ? -3618616 : -1275068416);
          }
        }

        boxImg = Image.createRGBImage(var3, var1, var2, true);
        boxW = var1;
        boxH = var2;
      }

      var0.drawImage(boxImg, 0, 0, 20);
    }
  }

  public static void boxAt(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
    var0.translate(var1, var2);
    box(var0, var3, var4);
    var0.translate(-var1, -var2);
  }

  public static void nop4(Graphics var0, int var1, int var2, int var3, int var4) {
  }

  public static void nop6(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6) {
  }

  public static void drawString(Graphics var0, String var1, int var2, int var3, int var4) {
    if (var1 != null) {
      if ((var4 & 9) != 0) {
        int var5 = width(var1, 0, var1.length());
        if ((var4 & 1) != 0) {
          var2 -= var5 >> 1;
        } else {
          var2 -= var5;
        }
      }

      if ((var4 & 32) != 0) {
        var3 -= H_I;
      } else if ((var4 & 64) != 0) {
        var3 -= H_I - 3;
      } else if ((var4 & 2) != 0) {
        var3 -= H_I >> 1;
      }

      fontcus.drawString(var0, var1, var2, var3);
    }
  }

  public static int stringWidth(Font var0, String var1) {
    return var1 == null ? 0 : width(var1, 0, var1.length());
  }

  public static int charWidth(Font var0, char var1) {
    short var2 = fontcus.charWidth(var1);
    return var2 <= 0 && var1 != 32 ? 12 : var2;
  }

  public static int getHeight(Font var0) {
    return H_I;
  }

  static int cw(char var0) {
    short var1 = fontcus.charWidth(var0);
    return var1 <= 0 && var0 != 32 ? 12 : var1;
  }

  static int width(String var0, int var1, int var2) {
    int var3 = 0;

    for (int var4 = var1; var4 < var2; var4++) {
      var3 += cw(var0.charAt(var4));
    }

    return var3;
  }

  public static String[] wrap(String var0, int var1) {
    if (var1 <= 0) {
      return null;
    } else if (var0 != null && var0.length() != 0) {
      Vector var2 = new Vector();
      wrapInto(var0, var1, var2);
      String[] var3 = new String[var2.size()];
      var2.copyInto(var3);
      return var3;
    } else {
      return new String[]{""};
    }
  }

  static void wrapInto(String var0, int var1, Vector var2) {
    int var3 = var0.length();
    int var4 = 0;

    while (var4 < var3) {
      int var5 = 0;
      int var6 = var4;
      int var7 = -1;
      int var8 = -1;

      while (var6 < var3) {
        char var9 = var0.charAt(var6);
        if (var9 == '\n') {
          break;
        }

        int var10 = tagLen(var0, var6);
        if (var10 > 0) {
          var6 += var10;
        } else {
          int var11 = cw(var9);
          if (var5 + var11 > var1 && var6 > var4) {
            break;
          }

          if (var9 == ' ') {
            var7 = var6;
            var8 = var6;
          } else if (var9 == 8218 && var6 + 1 < var3 && var0.charAt(var6 + 1) != ' ') {
            var7 = var6 + 1;
            var8 = var6 + 1;
          }

          var5 += var11;
          var6++;
        }
      }

      if (var6 < var3 && var0.charAt(var6) != '\n') {
        int var12 = var6;
        int var13 = var6;
        if (var0.charAt(var6) == ' ') {
          var13 = var6 + 1;
        } else if (var7 > var4) {
          var12 = var8;
          var13 = var8 == var7 && var0.charAt(var7) == ' ' ? var7 + 1 : var7;
        }

        var2.addElement(var0.substring(var4, var12));

        while (var13 < var3 && var0.charAt(var13) == ' ') {
          var13++;
        }

        var4 = var13;
      } else {
        var2.addElement(var0.substring(var4, var6));
        var4 = var6 + 1;
        if (var6 >= var3) {
          break;
        }
      }
    }
  }

  static int tagLen(String var0, int var1) {
    if (var0.charAt(var1) != '<') {
      return 0;
    } else {
      int var2 = var0.length();
      if (var1 + 9 < var2 && var0.charAt(var1 + 1) == '0' && var0.charAt(var1 + 2) == 'x' && var0.charAt(var1 + 9) == '>') {
        return 10;
      } else {
        return var1 + 2 < var2 && var0.charAt(var1 + 2) == 62 ? 3 : 0;
      }
    }
  }

  static void fixTags(Vector var0, int var1) {
    String var2 = null;

    for (int var3 = var1; var3 < var0.size(); var3++) {
      String var4 = (String)var0.elementAt(var3);
      if (var2 != null) {
        var4 = var2 + var4;
      }

      String var5 = null;
      int var6 = 0;

      while (var6 < var4.length()) {
        int var7 = tagLen(var4, var6);
        if (var7 == 0) {
          var6++;
        } else {
          String var8 = var4.substring(var6, var6 + var7);
          var5 = var8.equals("</>") ? null : var8;
          var6 += var7;
        }
      }

      if (var5 != null) {
        var4 = var4 + "</>";
      }

      var2 = var5;
      var0.setElementAt(var4, var3);
    }
  }

  public static String[] wrapParts(String var0, String var1, int var2) {
    Vector var3 = new Vector();
    if (var0 != null && var1 != null) {
      int var4 = 0;
      int var5 = var0.length();

      while (var4 < var5) {
        int var6 = var0.indexOf(var1, var4);
        if (var6 < 0) {
          var6 = var5;
        }

        if (var6 > var4) {
          int var7 = var3.size();
          wrapInto(var0.substring(var4, var6), var2, var3);
          fixTags(var3, var7);
        }

        var4 = var6 + var1.length();
      }

      if (var3.size() == 0) {
        return null;
      } else {
        String[] var8 = new String[var3.size()];
        var3.copyInto(var8);
        return var8;
      }
    } else {
      return null;
    }
  }
}
