import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class cg {
  private static Object a_Object = new Object();
  public static final Font a_Font;
  public static final short a_S = (short)vh.stringWidth(a_Font = Font.getFont(0, 0, 8), "MM");
  public static short b_S = (short)vh.getHeight(a_Font);
  public static final byte[] a_AB = new byte[]{7};
  private static Hashtable a_Hashtable = new Hashtable();
  private static final byte[] b_AB = new byte[]{7, 3, 8, 7, 5, 9, 4, 2, 5, 4, 9, 5, 4, 9, 3, 1, 2, 7, 5, 2};
  private static final byte[] c_AB = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82};
  private static Vector a_Vector;
  private static Vector b_Vector;

  public static final boolean[] a(boolean[] var0, boolean var1) {
    if (var0 == null) {
      (var0 = new boolean[1])[0] = var1;
      return var0;
    } else {
      boolean[] var2 = new boolean[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final byte[] a_AB(byte[] var0, int var1) {
    if (var0 == null) {
      (var0 = new byte[1])[0] = (byte)var1;
      return var0;
    } else {
      byte[] var2 = new byte[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = (byte)var1;
      return var2;
    }
  }

  public static final byte[][] a(byte[][] var0, byte[] var1) {
    if (var0 == null) {
      (var0 = new byte[1][])[0] = var1;
      return var0;
    } else {
      byte[][] var2 = new byte[var0.length + 1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final int[] a_AI(int[] var0, int var1) {
    if (var0 == null) {
      (var0 = new int[1])[0] = var1;
      return var0;
    } else {
      int[] var2 = new int[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final int[][] a(int[][] var0, int[] var1) {
    if (var0 == null) {
      (var0 = new int[1][])[0] = var1;
      return var0;
    } else {
      int[][] var2 = new int[var0.length + 1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final short[] a_AS(short[] var0, int var1) {
    if (var0 == null) {
      (var0 = new short[1])[0] = (short)var1;
      return var0;
    } else {
      short[] var2 = new short[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = (short)var1;
      return var2;
    }
  }

  public static short[] a_AS(short[] var0, short[] var1) {
    if (var1 != null) {
      if (var0 == null) {
        var0 = new short[var1.length];
        System.arraycopy(var1, 0, var0, 0, var1.length);
        return var0;
      } else {
        short[] var2 = new short[var0.length + var1.length];
        System.arraycopy(var0, 0, var2, 0, var0.length);
        System.arraycopy(var1, 0, var2, var0.length, var1.length);
        return var2;
      }
    } else {
      return var0;
    }
  }

  public static final short[][] a(short[][] var0, short[] var1) {
    if (var0 == null) {
      (var0 = new short[1][])[0] = var1;
      return var0;
    } else {
      short[][] var2 = new short[var0.length + 1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final String[] a(String[] var0, String var1) {
    if (var0 == null) {
      (var0 = new String[1])[0] = var1;
      return var0;
    } else {
      String[] var2 = new String[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final String[][] a(String[][] var0, String[] var1) {
    if (var0 == null) {
      (var0 = new String[1][])[0] = var1;
      return var0;
    } else {
      String[][] var2 = new String[var0.length + 1][];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var2.length - 1] = var1;
      return var2;
    }
  }

  public static final boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
    return var4 < var0 + var2 && var5 < var1 + var3 && var4 + var6 > var0 && var5 + var7 > var1;
  }

  public static final boolean a_Z(short[] var0, short[] var1) {
    return var0 != null && var1 != null
      ? var1[0] < var0[0] + var0[2] && var1[1] < var0[1] + var0[3] && var1[0] + var1[2] > var0[0] && var1[1] + var1[3] > var0[1]
      : false;
  }

  public static final void a() {
    a_Hashtable.clear();
  }

  public static boolean a_Z(String var0, String var1) {
    if (var0 != null && var1 != null) {
      if (var0.endsWith(".png") || var0.endsWith(".gf")) {
        var0 = var0.substring(0, var0.lastIndexOf(46)) + ".gf";
      }

      if (var1.endsWith(".png") || var1.endsWith(".gf")) {
        var1 = var1.substring(0, var1.lastIndexOf(46)) + ".gf";
      }

      return var0.equals(var1);
    } else {
      return false;
    }
  }

  public static final void a(Graphics var0, Image var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
    try {
      if (at.c_B == 0) {
        var8 = var1.getWidth();
        int var9 = var1.getHeight();
        int var10 = var8 / var2;
        int var11 = var9 / var3;
        int var12 = var4 / var10;
        int var13 = var4 % var10;
        if (var12 < var11 && var13 < var10) {
          var0.drawRegion(var1, var13 * var2, var12 * var3, var2, var3, 0, var5, var6, var7);
          return;
        }
      } else {
        Image[] var23;
        if (a_Hashtable.containsKey(var1)) {
          var23 = (Image[])a_Hashtable.get(var1);
        } else {
          var23 = new Image[var1.getWidth() / var2 * var1.getHeight() / var3];
          int var24 = var1.getWidth();

          for (int var25 = 0; var25 < var23.length; var25++) {
            int var26 = var24 / var2;
            int var27 = var25 / var26;
            int var28 = var25 % var26;
            var23[var25] = Image.createImage(var1, var28 * var2, var27 * var3, var2, var3, 0);
          }

          a_Hashtable.put(var1, var23);
        }

        int var21 = var7;
        int var20 = var6;
        var3 = var5;
        var2 = var4;
        Image[] var17 = var23;
        var0 = var0;

        try {
          var0.drawImage(var17[var2], var3, var20, var21);
        } catch (Exception var14) {
          return;
        }
      }
    } catch (Exception var15) {
    }
  }

  public static final void a(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6) {
    var0.setColor(var5);
    vh.drawString(var0, var1, var2, var3 - 1, var6);
    vh.drawString(var0, var1, var2, var3 + 1, var6);
    vh.drawString(var0, var1, var2 - 1, var3, var6);
    vh.drawString(var0, var1, var2 + 1, var3, var6);
    var0.setColor(var4);
    vh.drawString(var0, var1, var2, var3, var6);
  }

  public static final void b(Graphics var0, String var1, int var2, int var3, int var4, int var5, int var6) {
    var0.setColor(var5);
    vh.drawString(var0, var1, var2, var3 - 1, var6);
    vh.drawString(var0, var1, var2, var3 + 1, var6);
    vh.drawString(var0, var1, var2 - 1, var3, var6);
    vh.drawString(var0, var1, var2 + 1, var3, var6);
    vh.drawString(var0, var1, var2 - 1, var3 - 1, var6);
    vh.drawString(var0, var1, var2 + 1, var3 - 1, var6);
    vh.drawString(var0, var1, var2 - 1, var3 + 1, var6);
    vh.drawString(var0, var1, var2 + 1, var3 + 1, var6);
    var0.setColor(var4);
    vh.drawString(var0, var1, var2, var3, var6);
  }

  public static final void a(Graphics var0, int var1, int var2, int var3, Image var4, int var5, int var6, int var7) {
    var7 = var7;
    boolean var11 = false;
    boolean var12 = false;
    var6 = var6;
    var5 = var5;
    var4 = var4;
    var3 = var3;
    var2 = var2;
    var0 = var0;
    String var8 = String.valueOf(var1);
    if (at.c_B == 0) {
      if ((var7 & 1) == 1) {
        var2 -= var5 * a(var1) / 2;
      }

      for (int var33 = 0; var33 < var8.length(); var33++) {
        byte var18;
        if ((var18 = (byte)(var8.charAt(var33) - '0')) >= 0 && var18 <= 9) {
          if ((var7 & 4) == 4) {
            var0.drawRegion(var4, var18 * var5, 0, var5, var6, 0, var2 + var33 * var5, var3, var7);
          } else if ((var7 & 8) == 8) {
            var0.drawRegion(var4, var18 * var5, 0, var5, var6, 0, var2 - (var8.length() - var33 - 1) * var5, var3, var7);
          } else if ((var7 & 1) == 1) {
            int var35 = var7 & 50;
            var0.drawRegion(var4, var18 * var5, 0, var5, var6, 0, var2 + var33 * var5, var3, 4 | var35);
          }
        } else if (var18 == -3) {
          if ((var7 & 4) == 4) {
            var0.drawRegion(var4, var5 * 10, 0, var5, var6, 0, var2 + var33 * var5, var3, var7);
          } else if ((var7 & 8) == 8) {
            var0.drawRegion(var4, var5 * 10, 0, var5, var6, 0, var2 - (var8.length() - var33 - 1) * var5, var3, var7);
          } else if ((var7 & 1) == 1) {
            int var34 = var7 & 50;
            var0.drawRegion(var4, var5 * 10, 0, var5, var6, 0, var2 + var33 * var5, var3, 4 | var34);
          }
        }
      }
    } else {
      Image[] var9;
      if (!a_Hashtable.containsKey(var4)) {
        var9 = new Image[var4.getWidth() / var5];

        for (int var10 = 0; var10 < var9.length; var10++) {
          var9[var10] = Image.createImage(var4, var10 * var5, 0, var5, var6, 0);
        }

        a_Hashtable.put(var4, var9);
      } else {
        var9 = (Image[])a_Hashtable.get(var4);
      }

      var6 = var7;
      boolean var14 = false;
      boolean var15 = false;
      var5 = var5;
      Image[] var24 = var9;
      var3 = var3;
      var2 = var2;
      var0 = var0;
      String var17 = String.valueOf(var1);
      var7 &= 50;

      for (int var31 = 0; var31 < var17.length(); var31++) {
        byte var32;
        if ((var32 = (byte)(var17.charAt(var31) - '0')) >= 0 && var32 <= 9) {
          if ((var6 & 4) == 4) {
            var0.drawImage(var24[var32], var2 + var31 * var5, var3, 4 | var7);
          } else if ((var6 & 8) == 8) {
            var0.drawImage(var24[var32], var2 - (var17.length() - var31) * var5, var3, 4 | var7);
          } else if ((var6 & 1) == 1) {
            var0.drawImage(var24[var32], var2 + var31 * var5 - var17.length() * var5 / 2, var3, 4 | var7);
          }
        } else if (var32 == -3) {
          if ((var6 & 4) == 4) {
            var0.drawImage(var24[10], var2 + var31 * var5, var3, 4 | var7);
          } else if ((var6 & 8) == 8) {
            var0.drawImage(var24[10], var2 - (var17.length() - var31) * var5, var3, 4 | var7);
          } else if ((var6 & 1) == 1) {
            var0.drawImage(var24[10], var2 + var31 * var5 - var17.length() * var5 / 2, var3, 4 | var7);
          }
        }
      }
    }
  }

  private static boolean c_Z(String var0) {
    var0 = vh.rn(var0);
    if (var0 == null) {
      return false;
    } else if (cu.b(var0)) {
      return true;
    } else {
      if (a_Vector == null) {
        a_Vector = new Vector();
      }

      if (b_Vector == null) {
        b_Vector = new Vector();
      }

      if (a_Vector.contains(var0)) {
        return true;
      } else if (b_Vector.contains(var0)) {
        return false;
      } else {
        InputStream var1 = null;

        try {
          if ((var1 = a_Object.getClass().getResourceAsStream(var0)) == null) {
            b_Vector.addElement(var0);
            return false;
          }

          a_Vector.addElement(var0);
        } catch (Exception var9) {
          return false;
        } finally {
          try {
            if (var1 != null) {
              var1.close();
            }
          } catch (IOException var8) {
            var8.printStackTrace();
          }
        }

        return true;
      }
    }
  }

  public static final boolean a_Z(String var0) {
    if (at.d_Z) {
      String var1 = null;
      if (var0.endsWith(".png")) {
        var1 = var0.substring(0, var0.length() - 4) + ".gf";
      }

      if (var0.endsWith(".gf")) {
        var1 = var0.substring(0, var0.length() - 3) + ".png";
      }

      if (c_Z(var0) || c_Z(var1)) {
        return true;
      }

      if (cu.b(var0) || cu.b(var1)) {
        return true;
      }
    } else {
      if (var0.endsWith(".png")) {
        var0 = var0.substring(0, var0.length() - 4) + ".gf";
      }

      if (c_Z(var0)) {
        return true;
      }

      if (cu.b(var0)) {
        return true;
      }
    }

    return false;
  }

  public static final boolean b_Z(String var0, String var1) {
    return b_Z(a_String(var0, var1));
  }

  public static final byte[] a_AB(String var0, String var1) {
    return b_AB(a_String(var0, var1), ",");
  }

  public static final byte[][] a_AAB(String var0, String var1, String var2, String var3) {
    try {
      int var4 = 0;
      byte var5 = 0;
      if (var3 != null) {
        var5 = (byte)var3.charAt(0);
      }

      byte[] var21;
      int var15 = (var21 = var0.getBytes("UTF-8")).length;
      byte[] var17;
      int var6 = (var17 = var1.getBytes("UTF-8")).length;
      byte[] var19;
      int var7 = (var19 = var2.getBytes("UTF-8")).length;
      int var8 = -1;
      int var9 = -1;
      int var10 = 0;
      int var11 = 0;
      int var12 = -1;

      for (int var13 = 0; var13 < var15; var13++) {
        if (var8 == -1) {
          if (var21[var13] == var17[var10]) {
            if (++var10 == var6) {
              var8 = var13 + 1;
            }
          } else {
            var10 = 0;
          }
        } else if (var8 != -1 && var9 == -1) {
          if (var21[var13] == var19[var11]) {
            if (++var11 == var7) {
              var9 = var13;
            }
          } else {
            var11 = 0;
          }

          if (var21[var13] == 13) {
            var4++;
          }
        }
      }

      if (var4 > 0) {
        var4--;
      }

      if (var8 != -1 && var9 != -1) {
        byte[][] var18 = new byte[var4][];
        int var20 = 0;
        var4 = -1;

        for (int var28 = var8; var28 < var9; var28++) {
          if (var5 == 0 && var4 == -1 && var21[var28] == 10) {
            var4 = 0;
            var12 = var28 + 1;
          } else if (var5 > 0 && var21[var28] == var5) {
            var4 = 0;
            var12 = var28 + 1;
          } else if (var21[var28] == 44) {
            var4++;
          } else if (var21[var28] == 13 && var4 != -1) {
            byte[] var16 = new byte[var4 + 1];
            var4 = 0;
            var6 = 0;
            boolean var25 = false;

            for (int var27 = var12; var27 <= var28; var27++) {
              if (var21[var27] == 45) {
                var25 = true;
              } else if (var21[var27] != 44 && var21[var27] != 13) {
                var8 = var21[var27] - 48 & 0xFF;
                var6 = var6 * 10 + var8;
              } else {
                if (var25) {
                  var16[var4] = (byte)(-var6);
                } else {
                  var16[var4] = (byte)var6;
                }

                var6 = 0;
                var4++;
                var25 = false;
              }
            }

            var18[var20] = var16;
            var20++;
            var4 = -1;
          }
        }

        return var18;
      } else {
        return null;
      }
    } catch (Exception var14) {
      var14.printStackTrace();
      return null;
    }
  }

  public static final byte a_B(String var0, String var1) {
    return a_B(a_String(var0, var1));
  }

  public static final byte[] a_AB(String var0, boolean var1) {
    var0 = vh.rn(var0);
    if (var0 != null && !var0.equals("")) {
      String var2 = null;
      String var3 = null;
      int var4;
      if ((var4 = var0.lastIndexOf(47)) != -1) {
        var2 = var0.substring(var4 + 1);
      }

      if (!var0.startsWith("/pub/")) {
        var3 = "/pub/" + var2;
      }

      byte[] var20 = null;
      InputStream var22 = null;

      try {
        if ((var22 = a_Object.getClass().getResourceAsStream(var0)) == null) {
          if (var3 != null) {
            var22 = a_Object.getClass().getResourceAsStream(var3);
          }

          if (var22 == null) {
            if (var1) {
              if ((var20 = cu.a_AB(var0)) == null && var3 != null) {
                var20 = cu.a_AB(var3);
              }

              return var20;
            }

            return null;
          }

          System.out.println("Đọc trong pub " + var3);
        }

        int var16;
        if ((var16 = var22.available()) <= 0) {
          ByteArrayOutputStream var17 = new ByteArrayOutputStream();
          byte[] var19 = new byte[4096];

          while ((var21 = var22.read(var19)) != -1) {
            var17.write(var19, 0, var21);
          }

          var20 = var17.toByteArray();
        } else {
          var20 = new byte[var16];
          var22.read(var20);
        }
      } catch (IOException var13) {
        var13.printStackTrace();
        return var20;
      } finally {
        try {
          if (var22 != null) {
            var22.close();
          }
        } catch (IOException var12) {
          var12.printStackTrace();
        }
      }

      return var20;
    } else {
      return null;
    }
  }

  public static final byte[] a_AB(String var0) {
    return a_AB(var0, true);
  }

  private static final byte[] c_AB(String var0) {
    byte[] var1;
    if ((var1 = a_AB(var0, true)) == null) {
      return null;
    } else {
      if (var0.toLowerCase().endsWith(".gf")) {
        byte var3 = 0;

        for (byte var2 = 0; var2 < var1.length; var2 += 10) {
          var1[var2] += b_AB[var3];
          if (var3 < b_AB.length - 1) {
            var3++;
          } else {
            var3 = 0;
          }
        }
      }

      byte[] var4 = new byte[var1.length + 16];
      System.arraycopy(c_AB, 0, var4, 0, c_AB.length);
      System.arraycopy(var1, 0, var4, 16, var1.length);
      return var4;
    }
  }

  public static final Image a(byte[] var0) {
    return var0 == null ? null : Image.createImage(var0, 0, var0.length);
  }

  public static final Image a_Image(String var0) {
    if (var0 != null && !var0.equals("")) {
      if (!var0.startsWith("/")) {
        var0 = "/" + var0;
      }

      byte[] var1;
      return (var1 = b_AB(var0)) == null ? null : Image.createImage(var1, 0, var1.length);
    } else {
      return null;
    }
  }

  public static final byte[] b_AB(String var0) {
    var0 = vh.rn(var0);
    if (var0 != null && !var0.equals("")) {
      if (!var0.startsWith("/")) {
        var0 = "/" + var0;
      } else {
        var0 = var0;
      }

      String var1 = null;
      String var2 = var0.substring(var0.lastIndexOf(47) + 1);
      if (!var0.startsWith("/pub/")) {
        var1 = "/pub/" + var2;
      }

      if (true) {
        if (var0.endsWith(".png") && !c_Z(var0) && !c_Z(var1)) {
          var0 = var0.substring(0, var0.length() - 4) + ".gf";
        } else if (var0.endsWith(".gf") && !c_Z(var0) && !c_Z(var1)) {
          var0 = var0.substring(0, var0.length() - 3) + ".png";
        }
      } else if (var0.endsWith(".png")) {
        var0 = var0.substring(0, var0.length() - 4) + ".gf";
      }

      try {
        byte[] var6;
        if (!var0.toLowerCase().endsWith(".png") && !var0.toLowerCase().endsWith(".jpg")) {
          if (!var0.toLowerCase().endsWith(".gf")) {
            return null;
          }

          var6 = c_AB(var0);
        } else {
          var6 = a_AB(var0, true);
        }

        return var6;
      } catch (Exception var3) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static final int[] a_AI(String var0, String var1) {
    return b_AI(a_String(var0, var1), ",");
  }

  public static final int a_I(String var0, String var1) {
    return a_I(a_String(var0, var1));
  }

  public static final int a(int var0) {
    return String.valueOf(var0).length();
  }

  public static final short[] a_AS(String var0, String var1) {
    return b_AS(a_String(var0, var1), ",");
  }

  public static final short[][] a_AAS(String var0, String var1, String var2, String var3) {
    short[][] var4 = null;
    if ((var0 = a(var0, var1, var2)) == null) {
      return null;
    } else {
      String[] var6;
      if ((var6 = b_AString(var0, "\r\n")) != null) {
        var4 = new short[var6.length][];

        for (int var7 = 0; var6 != null && var7 < var6.length; var7++) {
          if (var3 != null) {
            int var8;
            if ((var8 = var6[var7].indexOf(var3)) == -1) {
              var4[var7] = b_AS(var6[var7], ",");
              continue;
            }

            var6[var7] = var6[var7].substring(var8 + var3.length());
          }

          var4[var7] = b_AS(var6[var7], ",");
        }
      }

      return var4;
    }
  }

  public static final short a_S(String var0, String var1) {
    return a_S(a_String(var0, var1));
  }

  public static final String[] a_AString(String var0, String var1) {
    return b_AString(a_String(var0, var1), ",");
  }

  public static final String[] a_AString(String var0, String var1, String var2, String var3) {
    return a_AString(var0, var1, var2, var3, null);
  }

  public static final String[] a_AString(String var0, String var1, String var2, String var3, String var4) {
    if ((var0 = a(var0, var1, var2)) == null) {
      return null;
    } else {
      String[] var7 = b_AString(var0, "\r\n");

      for (int var8 = 0; var7 != null && var8 < var7.length; var8++) {
        int var9;
        if (var3 != null) {
          var9 = var7[var8].indexOf(var3);
        } else {
          var9 = 0;
        }

        if (var9 != -1) {
          if (var4 != null) {
            int var5;
            if ((var5 = var7[var8].indexOf(var4, var9)) != -1) {
              var7[var8] = var7[var8].substring(var9 + (var3 != null ? var3.length() : 0), var5);
            } else {
              var7[var8] = var7[var8].substring(var9 + (var3 != null ? var3.length() : 0));
            }
          } else {
            var7[var8] = var7[var8].substring(var9 + (var3 != null ? var3.length() : 0));
          }
        }
      }

      return var7;
    }
  }

  public static final String[][] a_AAString(String var0, String var1, String var2, String var3) {
    return a_AAString(var0, var1, var2, null, ",");
  }

  public static final String[][] a_AAString(String var0, String var1, String var2, String var3, String var4) {
    String[][] var5 = null;
    if ((var0 = a(var0, var1, var2)) == null) {
      return null;
    } else {
      String[] var7;
      if ((var7 = b_AString(var0, "\r\n")) != null) {
        var5 = new String[var7.length][];

        for (int var8 = 0; var7 != null && var8 < var7.length; var8++) {
          if (var3 != null) {
            int var9;
            if ((var9 = var7[var8].indexOf(var3)) == -1) {
              var5[var8] = b_AString(var7[var8], var4);
              continue;
            }

            var7[var8] = var7[var8].substring(var9 + var3.length());
          }

          var5[var8] = b_AString(var7[var8], var4);
        }
      }

      return var5;
    }
  }

  public static final String a_String(String var0, String var1) {
    if (var0 != null && var1 != null && !var0.equals("") && !var1.equals("")) {
      String[] var3 = b_AString(var0, "\r\n");

      for (int var2 = 0; var3 != null && var2 < var3.length; var2++) {
        if (var3[var2] != null && !var3[var2].equals("") && var3[var2].startsWith(var1 + "=")) {
          return var3[var2].substring((var1 + "=").length());
        }
      }

      return null;
    } else {
      return null;
    }
  }

  public static final String a(String var0, String var1, String var2) {
    if (var0 != null && !var0.equals("")) {
      int var3;
      if (var1 != null) {
        var3 = var0.indexOf(var1);
      } else {
        var3 = 0;
      }

      if (var3 >= 0) {
        if (var2 == null) {
          if (var1 != null) {
            var0 = var0.substring(var3 + var1.length());
          } else {
            var0 = var0;
          }

          return var0;
        }

        int var6;
        if ((var6 = var0.indexOf(var2, var3)) >= 0) {
          if (var1 != null) {
            var0 = var0.substring(var3 + var1.length(), var6);
          } else {
            var0 = var0.substring(0, var6);
          }

          return var0;
        }
      }

      return null;
    } else {
      return null;
    }
  }

  public static final boolean a_Z(byte[] var0, int var1) {
    for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
      if (var0[var2] == var1) {
        return true;
      }
    }

    return false;
  }

  public static final boolean a_Z(int[] var0, int var1) {
    for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
      if (var0[var2] == var1) {
        return true;
      }
    }

    return false;
  }

  public static final boolean a_Z(short[] var0, int var1) {
    for (int var2 = 0; var0 != null && var2 < var0.length; var2++) {
      if (var0[var2] == var1) {
        return true;
      }
    }

    return false;
  }

  public static final String a_String(String var0) {
    return a_String(var0, !at.d_Z);
  }

  public static final String a_String(String var0, boolean var1) {
    var0 = vh.rn(var0);
    var1 = false;
    if (var0 != null && !var0.equals("")) {
      String var2 = null;
      if (at.i_Z) {
        var2 = cc.a(var0, var1, at.g_B);
      } else {
        if (var1 && var0.endsWith(".txt")) {
          var0 = var0.substring(0, var0.length() - 4) + ".bin";
        }

        byte[] var9;
        if ((var9 = a_AB(var0, true)) == null) {
          if (!var0.toLowerCase().endsWith(".bin")) {
            int var3;
            if ((var3 = var0.lastIndexOf(46)) > 0) {
              var0 = var0.substring(0, var3) + ".bin";
            }

            var9 = a_AB(var0, true);
          } else {
            if (!var0.toLowerCase().endsWith(".bin")) {
              return null;
            }

            int var10;
            if ((var10 = var0.lastIndexOf(46)) > 0) {
              var0 = var0.substring(0, var10) + ".txt";
            }

            var9 = a_AB(var0, true);
          }
        }

        if (var9 == null) {
          return null;
        }

        if (!var0.toLowerCase().endsWith(".txt")) {
          if (a_AB != null) {
            short var12 = 0;

            for (int var7 = 0; var7 < var9.length; var7++) {
              var9[var7] -= a_AB[var12];
              if (var12 < a_AB.length - 1) {
                var12++;
              } else {
                var12 = 0;
              }
            }
          } else {
            for (int var11 = 0; var11 < var9.length; var11++) {
              var9[var11] = (byte)(var9[var11] + 10);
            }
          }
        }

        try {
          var2 = new String(var9, "UTF-8");
        } catch (UnsupportedEncodingException var4) {
          var4.printStackTrace();
        } catch (Exception var5) {
          var5.printStackTrace();
        }
      }

      return var2;
    } else {
      return null;
    }
  }

  public static final byte[] b(byte[] var0, int var1) {
    if (var0 == null) {
      return null;
    } else {
      for (int var2 = 0; var2 < var0.length; var2++) {
        if (var0[var2] == var1) {
          return c(var0, var2);
        }
      }

      return var0;
    }
  }

  public static final int[] b(int[] var0, int var1) {
    int[] var2 = var0;

    for (int var3 = 0; var0 != null && var3 < var0.length; var3++) {
      if (var0[var3] == var1) {
        var2 = c(var0, var3);
        break;
      }
    }

    return var2;
  }

  public static final short[] b(short[] var0, int var1) {
    short[] var2 = var0;

    for (int var3 = 0; var0 != null && var3 < var0.length; var3++) {
      if (var0[var3] == var1) {
        var2 = c(var0, var3);
        break;
      }
    }

    return var2;
  }

  public static final byte[] c(byte[] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      byte[] var3 = new byte[var0.length - 1];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  public static final byte[][] a(byte[][] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      byte[][] var3 = new byte[var0.length - 1][];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  public static final int[] c(int[] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      int[] var3 = new int[var0.length - 1];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  public static final short[] c(short[] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      short[] var3 = new short[var0.length - 1];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  public static final short[][] a(short[][] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      for (int var2 = var1; var2 < var0.length - 1; var2++) {
        var0[var2] = var0[var2 + 1];
      }

      short[][] var3 = new short[var0.length - 1][];
      System.arraycopy(var0, 0, var3, 0, var0.length - 1);
      return var3;
    } else {
      return null;
    }
  }

  public static final String[] a(String[] var0, int var1) {
    if (var0 != null && var0.length != 1) {
      String[] var2 = new String[var0.length - 1];
      if (var1 == 0) {
        System.arraycopy(var0, 1, var2, 0, var0.length - 1);
      } else if (var1 == var0.length - 1) {
        System.arraycopy(var0, 0, var2, 0, var0.length - 1);
      } else {
        System.arraycopy(var0, 0, var2, 0, var1);
        System.arraycopy(var0, var1 + 1, var2, var1, var0.length - var1 - 1);
      }

      return var2;
    } else {
      return null;
    }
  }

  public static String[] b_AString(String var0, String var1) {
    if (var0 != null && var1 != null) {
      int var2 = 0;
      String[] var3 = null;
      Vector var4 = new Vector();

      while (var2 < var0.length()) {
        int var5;
        if ((var5 = var0.indexOf(var1, var2)) == -1) {
          if (!var0.substring(var2).equals("")) {
            var4.addElement(var0.substring(var2));
          }
          break;
        }

        if (!var0.substring(var2, var5).equals("")) {
          var4.addElement(var0.substring(var2, var5));
        }

        var2 = var5 + var1.length();
      }

      if (var4.size() > 0) {
        var3 = new String[var4.size()];
        var4.copyInto(var3);
      }

      return var3;
    } else {
      return null;
    }
  }

  public static final String[] a(String var0, String var1, int var2) {
    String[] var3 = null;
    String[] var7 = b_AString(var0, var1);
    Vector var8 = new Vector();

    for (int var4 = 0; var7 != null && var4 < var7.length; var4++) {
      String[] var5 = a(var7[var4], var2);

      for (int var6 = 0; var5 != null && var6 < var5.length; var6++) {
        var8.addElement(var5[var6]);
      }
    }

    if (var8.size() > 0) {
      var3 = new String[var8.size()];
      var8.copyInto(var3);
    }

    return var3;
  }

  public static final String[] a(String var0, int var1) {
    return vh.wrap(var0, var1);
  }

  public static final byte[] b_AB(String var0, String var1) {
    byte[] var2 = null;
    int[] var3;
    if ((var3 = b_AI(var0, var1)) != null) {
      var2 = new byte[var3.length];

      for (int var4 = 0; var4 < var3.length; var4++) {
        var2[var4] = (byte)var3[var4];
      }
    }

    return var2;
  }

  public static final int[] b_AI(String var0, String var1) {
    if (var0 != null && !var0.equals("")) {
      String[] var3 = b_AString(var0, var1);
      int[] var4 = null;
      if (var3 != null) {
        var4 = new int[var3.length];

        for (int var2 = 0; var2 < var3.length; var2++) {
          var4[var2] = a_I(var3[var2]);
        }
      }

      return var4;
    } else {
      return null;
    }
  }

  public static final short[] b_AS(String var0, String var1) {
    short[] var2 = null;
    int[] var3;
    if ((var3 = b_AI(var0, var1)) != null) {
      var2 = new short[var3.length];

      for (int var4 = 0; var4 < var3.length; var4++) {
        var2[var4] = (short)var3[var4];
      }
    }

    return var2;
  }

  public static final boolean b_Z(String var0) {
    return var0 != null && !var0.equals("") ? a_I(var0) == 1 : false;
  }

  public static final byte a_B(String var0) {
    if (var0 != null && !var0.equals("")) {
      byte var1 = 0;

      try {
        if (!var0.startsWith("0x") && !var0.startsWith("0X")) {
          var1 = Byte.parseByte(var0);
        } else {
          var1 = Byte.parseByte(var0.substring(2), 16);
        }
      } catch (Exception var2) {
      }

      return var1;
    } else {
      return 0;
    }
  }

  public static final int a_I(String var0) {
    if (var0 != null && !var0.equals("")) {
      int var1 = 0;

      try {
        if (!var0.startsWith("0x") && !var0.startsWith("0X")) {
          var1 = Integer.parseInt(var0);
        } else {
          var1 = Integer.parseInt(var0.substring(2), 16);
        }
      } catch (Exception var2) {
        var2.printStackTrace();
      }

      return var1;
    } else {
      return 0;
    }
  }

  public static final short a_S(String var0) {
    if (var0 != null && !var0.equals("")) {
      short var1 = 0;

      try {
        if (!var0.startsWith("0x") && !var0.startsWith("0X")) {
          var1 = Short.parseShort(var0);
        } else {
          var1 = Short.parseShort(var0.substring(2), 16);
        }
      } catch (Exception var2) {
      }

      return var1;
    } else {
      return 0;
    }
  }
}
