/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 */
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class d {
    public static final Font a = Font.getFont((int)0, (int)0, (int)8);
    public static int b = a.stringWidth("\u6d4b");
    public static int c = a.getHeight();
    public static int d = 2;
    private static final Random l = new Random();
    public static String[] e = null;
    private static byte m = 0;
    public static byte f = 0;
    public static byte g = 1;
    public static byte h = (byte)2;
    public static byte i = (byte)3;
    public static byte j = (byte)4;
    public static byte k = (byte)5;
    private static int n = 0;
    private static byte o = 0;

    public static final int a(int n, int n2) {
        if (n2 - n <= 0) {
            return n;
        }
        int n3 = l.nextInt() % (n2 - n);
        if (n3 < 0) {
            n3 = -n3;
        }
        return n + n3;
    }

    public static String[] a(String string, Font font, int n) {
        int n2;
        Object object;
        if (font == null) {
            font = a;
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        Vector<Object> vector = new Vector<Object>();
        boolean bl = false;
        String string2 = null;
        int n6 = -1;
        while (true) {
            String string3;
            if (n3 >= string.length()) {
                object = string.substring(n4, n3);
                if (n6 != -1) {
                    if (bl) {
                        object = (String)object + "[/C]";
                        n2 = vector.size();
                        if (n2 > n6) {
                            object = string2 + (String)object;
                        }
                    } else {
                        int n7 = vector.size();
                        if (n7 > n6) {
                            object = string2 + (String)object;
                        }
                    }
                }
                break;
            }
            char c2 = string.charAt(n3);
            if (c2 == '|') {
                string3 = string.substring(n4, n3);
                if (n6 != -1) {
                    if (bl) {
                        string3 = string3 + "[/C]";
                        int n8 = vector.size();
                        if (n8 > n6) {
                            string3 = string2 + string3;
                        }
                    } else {
                        int n9 = vector.size();
                        if (n9 > n6) {
                            string3 = string2 + string3;
                        }
                        n6 = -1;
                    }
                }
                vector.addElement(string3);
                n5 = 0;
                n4 = ++n3;
                continue;
            }
            if (c2 == '[') {
                if ((c2 = string.charAt(++n3)) == 'C' && string.charAt(n3 + 1) == ':') {
                    string2 = string.substring(n3 - 1, n3 + 9);
                    n6 = vector.size();
                    bl = true;
                    n3 += 9;
                    continue;
                }
                if (c2 == '/' && string.charAt(n3 + 1) == 'C') {
                    n3 += 3;
                    bl = false;
                    continue;
                }
                if (c2 != 'N' || string.charAt(n3 + 1) != '/') continue;
                string3 = j.aS;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(string.substring(0, n3 - 1));
                stringBuffer.append(string3);
                stringBuffer.append(string.substring(n3 + 3));
                string = stringBuffer.toString();
                --n3;
                continue;
            }
            if ((n5 += font.charWidth(c2)) > n) {
                string3 = string.substring(n4, n3);
                if (n6 != -1) {
                    if (bl) {
                        string3 = string3 + "[/C]";
                        int n10 = vector.size();
                        if (n10 > n6) {
                            string3 = string2 + string3;
                        }
                    } else {
                        int n11 = vector.size();
                        if (n11 > n6) {
                            string3 = string2 + string3;
                        }
                        n6 = -1;
                    }
                }
                vector.addElement(string3);
                n5 = font.charWidth(c2);
                n4 = n3++;
                continue;
            }
            ++n3;
        }
        vector.addElement(object);
        object = new String[vector.size()];
        for (n2 = 0; n2 < vector.size(); ++n2) {
            object[n2] = (String)vector.elementAt(n2);
        }
        vector.removeAllElements();
        System.gc();
        e = object;
        return object;
    }

    public static final void a(Graphics graphics, String string, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        String string2;
        Graphics graphics2;
        graphics.setColor(n4);
        if (n6 == 0) {
            graphics2 = graphics;
            string2 = string;
            n7 = n + 1;
        } else {
            graphics.drawString(string, n - 1, n2, n3);
            graphics.drawString(string, n + 1, n2, n3);
            graphics.drawString(string, n, n2 - 1, n3);
            graphics2 = graphics;
            string2 = string;
            n7 = n;
        }
        graphics2.drawString(string2, n7, n2 + 1, n3);
        graphics.setColor(n5);
        graphics.drawString(string, n, n2, n3);
    }

    public static void a(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        int n6 = n5 >> 16;
        int n7 = n5 >> 8 & 0xFF;
        int n8 = n5 & 0xFF;
        for (int i2 = 0; i2 < n4; ++i2) {
            int n9 = n4 - 2 * i2;
            n9 *= n9;
            if (i2 > n4 / 2) {
                n9 = -n9;
            }
            n9 = n9 * 512 / (n4 + 1) / (n4 + 1) / 3;
            graphics.setColor(Math.max(16, Math.min(240, n6 + n9)), Math.max(16, Math.min(240, n7 + n9)), Math.max(16, Math.min(240, n8 + n9)));
            graphics.fillRect(n, n2 + i2, n3, 1);
        }
        graphics.setColor(986895);
        graphics.fillRect(n, n2 - 1, n3, 1);
        graphics.fillRect(n, n2 + n4, n3, 1);
        graphics.fillRect(n - 1, n2, 1, n4);
        graphics.fillRect(n + n3, n2, 1, n4);
    }

    public static final void b(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        graphics.setColor(n4);
        graphics.setClip(0, 0, f.ad, f.ae + 80);
        switch (n3) {
            case 0: {
                int n6;
                for (n6 = 0; n6 < 6; ++n6) {
                    graphics.drawLine(n - n6, n2 + n6, n + n6, n2 + n6);
                }
                if (n4 == n5) break;
                graphics.setColor(n5);
                for (n6 = 0; n6 < 4; ++n6) {
                    graphics.drawLine(n - n6, n2 + 1 + n6, n + n6, n2 + 1 + n6);
                }
                return;
            }
            case 4: {
                int n7;
                --n2;
                for (n7 = 0; n7 < 6; ++n7) {
                    graphics.drawLine(n - n7, n2 - n7, n + n7, n2 - n7);
                }
                if (n4 == n5) break;
                graphics.setColor(n5);
                for (n7 = 0; n7 < 4; ++n7) {
                    graphics.drawLine(n - n7, n2 - 1 - n7, n + n7, n2 - 1 - n7);
                }
                return;
            }
            case 6: {
                int n8;
                for (n8 = 0; n8 < 6; ++n8) {
                    graphics.drawLine(n + n8, n2 - n8, n + n8, n2 + n8);
                }
                if (n4 == n5) break;
                graphics.setColor(n5);
                for (n8 = 0; n8 < 4; ++n8) {
                    graphics.drawLine(n + 1 + n8, n2 - n8, n + 1 + n8, n2 + n8);
                }
                return;
            }
            case 2: {
                int n9;
                --n;
                for (n9 = 0; n9 < 6; ++n9) {
                    graphics.drawLine(n - n9, n2 - n9, n - n9, n2 + n9);
                }
                if (n4 == n5) break;
                graphics.setColor(n5);
                for (n9 = 0; n9 < 4; ++n9) {
                    graphics.drawLine(n - 1 - n9, n2 - n9, n - 1 - n9, n2 + n9);
                }
                break;
            }
        }
    }

    public static final void a(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(128, 96, 32);
        graphics.fillRoundRect(n + 2, n2 + 2, n3, n4, 3, 3);
        graphics.setColor(216, 200, 160);
        graphics.fillRoundRect(n, n2, n3, n4, 3, 3);
    }

    public static void a(byte by) {
        m = by;
        n = 0;
        if (m == g) {
            o = (byte)d.a(0, 5);
        }
    }

    public static byte a() {
        return m;
    }

    public static void b() {
        block8: {
            int n;
            block11: {
                block10: {
                    block9: {
                        block7: {
                            if (m != g) break block7;
                            if (++d.n > 10) {
                                d.n = 0;
                                m = h;
                                return;
                            }
                            break block8;
                        }
                        if (m != i) break block9;
                        if (++d.n > 10) {
                            d.n = 0;
                            m = f;
                            return;
                        }
                        break block8;
                    }
                    if (m != j) break block10;
                    if (d.n == 1) {
                        m = f;
                    }
                    n = d.n;
                    break block11;
                }
                if (m != k) break block8;
                n = d.n;
            }
            d.n = n + 1;
        }
    }

    public static void a(Graphics graphics) {
        block27: {
            int n;
            int n2;
            int n3;
            Graphics graphics2;
            block26: {
                int n4;
                Graphics graphics3;
                block31: {
                    block30: {
                        block28: {
                            block29: {
                                block24: {
                                    block25: {
                                        graphics.setClip(0, 0, f.ad, f.ae);
                                        if (m != g) break block24;
                                        graphics.setColor(0);
                                        if (o != 0) break block25;
                                        int n5 = f.ad / 10 * d.n;
                                        graphics.fillRect(0, 0, n5, f.ae);
                                        graphics.fillRect(n5 + 3, 0, 9, f.ae);
                                        graphics2 = graphics;
                                        n3 = n5 + 19;
                                        n2 = 0;
                                        n = 4;
                                        break block26;
                                    }
                                    if (o == 1) {
                                        int n6;
                                        boolean bl = false;
                                        int n7 = 20 * 10000 * d.n / 10;
                                        int n8 = d.b(0, 0, f.ad >> 1, f.ae >> 1) + 4 << 1;
                                        int n9 = n6 = 0;
                                        while (n9 < 360) {
                                            graphics.fillArc(-(n8 - f.ad >> 1), -(n8 - f.ae >> 1), n8, n8, n6, n7 / 10000);
                                            n9 = n6 + 20;
                                        }
                                        return;
                                    }
                                    if (o == 2) {
                                        int n10;
                                        int n11 = f.ad / 16 + 1;
                                        int n12 = n11 * 10000 * d.n / 10 / 10000;
                                        int n13 = n10 = 0;
                                        while (n13 < f.ae) {
                                            int n14;
                                            int n15 = n14 = 0;
                                            while (n15 < f.ad) {
                                                graphics.fillRect(n14, n10, n12, n12);
                                                n15 = n14 + n11;
                                            }
                                            n13 = n10 + n11;
                                        }
                                        return;
                                    }
                                    if (o == 3) {
                                        int n16 = d.b(0, 0, f.ad >> 1, f.ae >> 1) + 4 << 1;
                                        int n17 = n16 * 10000 * d.n / 10 / 10000;
                                        graphics.fillArc(-(n17 - f.ad >> 1), -(n17 - f.ae >> 1), n17, n17, 0, 360);
                                        return;
                                    }
                                    if (o == 4) {
                                        int n18;
                                        int n19 = f.ad / 10 + 1;
                                        boolean bl = false;
                                        int n20 = n18 = 0;
                                        while (n20 < f.ae) {
                                            int n21;
                                            Graphics graphics4;
                                            if ((n18 / 4 & 1) == 0) {
                                                graphics4 = graphics;
                                                n21 = 0;
                                            } else {
                                                graphics4 = graphics;
                                                n21 = f.ad - n19 * d.n;
                                            }
                                            graphics4.fillRect(n21, n18, n19 * d.n, 4);
                                            n20 = n18 + 4;
                                        }
                                        return;
                                    }
                                    break block27;
                                }
                                if (m != i) break block28;
                                graphics.setColor(0);
                                if (o != 0) break block29;
                                int n22 = f.ad / 10 * d.n;
                                graphics.fillRect(n22, 0, 4, f.ae);
                                graphics.fillRect(n22 + 11, 0, 9, f.ae);
                                graphics2 = graphics;
                                n3 = n22 + 23;
                                n2 = 0;
                                n = f.ad - n22;
                                break block26;
                            }
                            if (o == 1) {
                                int n23;
                                boolean bl = false;
                                int n24 = 20 * 10000 * d.n / 10;
                                int n25 = d.b(0, 0, f.ad >> 1, f.ae >> 1) + 4 << 1;
                                int n26 = n23 = 0;
                                while (n26 < 360) {
                                    graphics.fillArc(-(n25 - f.ad >> 1), -(n25 - f.ae >> 1), n25, n25, n23, 20 - n24 / 10000);
                                    n26 = n23 + 20;
                                }
                                return;
                            }
                            if (o == 2) {
                                int n27;
                                int n28 = f.ad / 16 + 1;
                                int n29 = n28 * 10000 * d.n / 10 / 10000;
                                int n30 = n27 = 0;
                                while (n30 < f.ae) {
                                    int n31;
                                    int n32 = n31 = 0;
                                    while (n32 < f.ad) {
                                        graphics.fillRect(n31, n27, n28 - n29, n28 - n29);
                                        n32 = n31 + n28;
                                    }
                                    n30 = n27 + n28;
                                }
                                return;
                            }
                            if (o == 3) {
                                int n33 = d.b(0, 0, f.ad >> 1, f.ae >> 1) + 4 << 1;
                                int n34 = n33 - n33 * 10000 * d.n / 10 / 10000;
                                graphics.fillArc(-(n34 - f.ad >> 1), -(n34 - f.ae >> 1), n34, n34, 0, 360);
                                return;
                            }
                            if (o == 4) {
                                int n35;
                                int n36 = f.ad / 10 + 1;
                                boolean bl = false;
                                int n37 = n35 = 0;
                                while (n37 < f.ae) {
                                    int n38;
                                    Graphics graphics5;
                                    if ((n35 / 4 & 1) == 0) {
                                        graphics5 = graphics;
                                        n38 = 0;
                                    } else {
                                        graphics5 = graphics;
                                        n38 = f.ad - n36 * (10 - d.n);
                                    }
                                    graphics5.fillRect(n38, n35, n36 * (10 - d.n), 4);
                                    n37 = n35 + 4;
                                }
                                return;
                            }
                            break block27;
                        }
                        if (m != h) break block30;
                        graphics3 = graphics;
                        n4 = 0;
                        break block31;
                    }
                    if (m != j && (m != k || (d.n & 1) != 1)) break block27;
                    graphics3 = graphics;
                    n4 = 0xFFFFFF;
                }
                graphics3.setColor(n4);
                graphics2 = graphics;
                n3 = 0;
                n2 = 0;
                n = f.ad;
            }
            graphics2.fillRect(n3, n2, n, f.ae);
        }
    }

    private static int b(int n, int n2, int n3, int n4) {
        int n5;
        int n6 = Math.abs(n - n3);
        int n7 = n6 < (n5 = Math.abs(n2 - n4)) ? n6 : n5;
        return n6 + n5 - (n7 >> 1) - (n7 >> 2) + (n7 >> 4);
    }

    public static boolean a(String string, String string2) {
        return true;
    }

    public static boolean c() {
        try {
            Class<?> clazz = Class.forName("XConnection");
            if (clazz != null) {
                return true;
            }
        }
        catch (Exception exception) {}
        return false;
    }

    public static boolean d() {
        return false;
    }

    public static int a(int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        block13: {
            int n8;
            block12: {
                block11: {
                    int n9;
                    int n10;
                    int n11 = n >> 16 & 0xFF;
                    int n12 = n >> 8 & 0xFF;
                    int n13 = n & 0xFF;
                    n7 = n2 >> 16 & 0xFF;
                    n6 = n2 >> 8 & 0xFF;
                    n5 = n2 & 0xFF;
                    if (n3 < 0) {
                        n3 = 0;
                    }
                    if (n3 >= n4) {
                        return n;
                    }
                    n7 = ((n11 - n7 << 16) / n4 * n3 >> 16) + n7;
                    n6 = ((n12 - n6 << 16) / n4 * n3 >> 16) + n6;
                    n5 = ((n13 - n5 << 16) / n4 * n3 >> 16) + n5;
                    if (n7 < 0) {
                        n10 = 0;
                    } else if (n7 > 255) {
                        n10 = n7 = 255;
                    }
                    if (n6 < 0) {
                        n9 = 0;
                    } else if (n6 > 255) {
                        n9 = n6 = 255;
                    }
                    if (n5 >= 0) break block11;
                    n8 = 0;
                    break block12;
                }
                if (n5 <= 255) break block13;
                n8 = 255;
            }
            n5 = n8;
        }
        return n7 << 16 | n6 << 8 | n5;
    }
}

