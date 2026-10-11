/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class g {
    private static int a;
    private static int b;
    private static int[] c;
    private static byte[] d;
    private static int[] e;
    private static String[] f;
    private static InputStream g;
    private static int h;
    private static int i;
    private static int j;
    private static byte[] k;
    private static int l;
    private static long[] m;

    public static void a() {
        try {
            String string = "level-9";
            DataInputStream dataInputStream = new DataInputStream(j.aQ.getClass().getResourceAsStream(string));
            a = dataInputStream.readInt();
            b = dataInputStream.readInt();
            f = new String[b];
            c = new int[b];
            d = new byte[b];
            e = new int[b];
            for (int i2 = 0; i2 < b; ++i2) {
                g.f[i2] = dataInputStream.readUTF();
                g.c[i2] = dataInputStream.readInt();
                g.d[i2] = dataInputStream.readByte();
                g.e[i2] = dataInputStream.readInt();
            }
            dataInputStream.close();
            System.gc();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static int a(String string) {
        for (int i2 = 0; i2 < b; ++i2) {
            if (f[i2] == null || !string.equals(f[i2])) continue;
            return i2;
        }
        return -1;
    }

    public static byte[] a(String string, boolean bl) {
        byte[] byArray = null;
        if (bl) {
            try {
                byArray = new byte[g.b(string)];
                int n = byArray.length;
                int n2 = n <= a - h ? n : a - h;
                int n3 = 0;
                while (true) {
                    int n4 = g.read(byArray, n3, n2);
                    n3 += n4;
                    n -= n4;
                    if ((n2 -= n4) > 0) continue;
                    g.b();
                    if (n <= 0) break;
                    g = j.aQ.getClass().getResourceAsStream("/res" + ++j);
                    h = 0;
                    n2 = n < a ? n : a;
                }
            }
            catch (Exception exception) {}
        } else {
            try {
                InputStream inputStream = j.aQ.getClass().getResourceAsStream("/" + string);
                int n = inputStream.available();
                if (n <= 0) {
                    int n5;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while ((n5 = inputStream.read()) != -1) {
                        byteArrayOutputStream.write(n5);
                    }
                    byArray = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                } else {
                    byArray = new byte[n];
                    inputStream.read(byArray);
                }
                inputStream.close();
            }
            catch (Exception exception) {}
        }
        return byArray;
    }

    private static int b(String string) throws Exception {
        i = g.a(string);
        g = j.aQ.getClass().getResourceAsStream("/res" + d[i]);
        j = d[i];
        if (g != null) {
            g.skip(e[i]);
            h = e[i];
        }
        return c[i];
    }

    private static void b() throws Exception {
        if (g != null) {
            g.close();
            g = null;
        }
        System.gc();
    }

    public static Image a(String string, boolean bl, int n, int n2) {
        Image image = null;
        int n3 = string.indexOf(".");
        if (string.substring(n3 + 1).toLowerCase().equals("png")) {
            byte[] byArray = g.a(string, bl);
            if (byArray != null) {
                if (n2 != 0) {
                    g.a(byArray, n2);
                }
                image = Image.createImage((byte[])byArray, (int)0, (int)byArray.length);
            } else {
                System.out.println("worning: file " + string + " is null");
            }
        } else {
            byte[] byArray = g.a(string, bl);
            if (byArray != null) {
                image = g.a(byArray, n, n2);
            } else {
                System.out.println("worning: file " + string + " is null");
            }
        }
        return image;
    }

    private static Image a(byte[] byArray, int n, int n2) {
        int n3;
        int n4;
        int n5;
        int[] nArray;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        block14: {
            int n14;
            block13: {
                block12: {
                    if (byArray == null) {
                        return null;
                    }
                    if (byArray.length == 1) {
                        return null;
                    }
                    l = 0;
                    k = byArray;
                    n13 = g.a(8) + 1;
                    n12 = g.a(8) + 1;
                    n11 = g.a(16) + 1;
                    n10 = g.a(16) + 1;
                    n9 = g.a(8) + 1;
                    n8 = g.a(4);
                    n7 = g.a(4);
                    n6 = g.a(8);
                    nArray = new int[n9];
                    n5 = 24;
                    if (n8 != 0) break block12;
                    n14 = 16;
                    break block13;
                }
                if (n8 != 1) break block14;
                n14 = 8;
            }
            n5 = n14;
        }
        for (n4 = 0; n4 < n12; ++n4) {
            if (n != n4) {
                g.b(n9 * n5);
                continue;
            }
            for (int i2 = 0; i2 < n9; ++i2) {
                int n15 = g.a(n5);
                nArray[i2] = g.a(n15, n8);
            }
        }
        g.a(nArray, n2);
        n4 = g.a(16);
        byte[] byArray2 = g.d(n4);
        byte[] byArray3 = new byte[45 + n9 * 3 + (n7 == 1 ? 12 + (n6 + 1) : 0) + 12 + n4 + 12];
        int n16 = 0;
        g.a(byArray3, 0, 32, -1991225785);
        g.a(byArray3, 32, 32, 218765834);
        g.a(byArray3, 64, 32, 13);
        g.a(byArray3, 96, 32, 1229472850);
        g.a(byArray3, 128, 32, n11);
        g.a(byArray3, 160, 32, n10);
        g.a(byArray3, 192, 8, n13);
        g.a(byArray3, 200, 32, 0x3000000);
        int n17 = g.c(byArray3, 12, 29);
        g.a(byArray3, 232, 32, n17);
        g.a(byArray3, 264, 32, n9 * 3);
        g.a(byArray3, 296, 32, 1347179589);
        for (n3 = 0; n3 < n9; ++n3) {
            g.a(byArray3, 328 + n3 * 24, 24, nArray[n3]);
        }
        n17 = g.c(byArray3, 37, 41 + n9 * 3);
        g.a(byArray3, 264 + (8 + n9 * 3 << 3), 32, n17);
        n16 = 264 + (8 + n9 * 3 + 4 << 3);
        if (n7 == 1) {
            g.a(byArray3, n16, 32, n6 + 1);
            g.a(byArray3, n16 + 32, 32, 1951551059);
            for (n3 = 0; n3 < n6; ++n3) {
                g.a(byArray3, n16 + 64 + (n3 << 3), 8, 255);
            }
            g.a(byArray3, n16 + 64 + (n3 << 3), 8, 0);
            n17 = g.c(byArray3, (n16 >> 3) + 4, (n16 >> 3) + 4 + 4 + (n6 + 1));
            g.a(byArray3, n16 + (8 + (n6 + 1) << 3), 32, n17);
            n16 += 8 + (n6 + 1) + 4 << 3;
        }
        g.a(byArray3, n16, 32, n4);
        g.a(byArray3, n16 + 32, 32, 1229209940);
        for (n3 = 0; n3 < n4; ++n3) {
            g.a(byArray3, n16 + 64 + (n3 << 3), 8, (int)byArray2[n3]);
        }
        n17 = g.c(byArray3, (n16 >> 3) + 4, (n16 >> 3) + 4 + 4 + n4);
        g.a(byArray3, n16 + (8 + n4 << 3), 32, n17);
        g.a(byArray3, n16 += 8 + n4 + 4 << 3, 32, 0);
        g.a(byArray3, n16 + 32, 32, 1229278788);
        n17 = g.c(byArray3, (n16 >> 3) + 4, (n16 >> 3) + 4 + 4 + 0);
        g.a(byArray3, n16 + 64, 32, n17);
        k = null;
        return Image.createImage((byte[])byArray3, (int)0, (int)byArray3.length);
    }

    private static void a(int[] nArray, int n) {
        int n2 = nArray.length;
        switch (n) {
            case 1: {
                for (int i2 = 1; i2 < n2; ++i2) {
                    int n3 = nArray[i2];
                    int n4 = (n3 & 0xFF00) >> 8;
                    int n5 = n3 & 0xFF;
                    n3 = (n3 & 0xFF0000) >> 16;
                    int n6 = (n5 + n4 * 6 + n3 * 3) / 16;
                    nArray[i2] = n6 << 16 | n6 << 8 | n6;
                }
                break;
            }
            case 2: {
                for (int i3 = 1; i3 < n2; ++i3) {
                    int n7 = nArray[i3];
                    int n8 = (n7 & 0xFF00) >> 8;
                    int n9 = n7 & 0xFF;
                    n7 = (n7 & 0xFF0000) >> 16;
                    int n10 = (n9 + n8 * 6 + n7 * 3) / 10;
                    nArray[i3] = n10 << 16;
                }
                break;
            }
            case 3: {
                for (int i4 = 1; i4 < n2; ++i4) {
                    nArray[i4] = 0xFFFFFF;
                }
                break;
            }
        }
    }

    private static void a(byte[] byArray, int n) {
        boolean bl = false;
        for (int i2 = 0; i2 < byArray.length; ++i2) {
            if (byArray[i2] != 80 || byArray[i2 + 1] != 76 || byArray[i2 + 2] != 84 || byArray[i2 + 3] != 69) continue;
            bl = true;
            int n2 = (byArray[i2 - 4] & 0xFF) << 24 | (byArray[i2 - 3] & 0xFF) << 16 | (byArray[i2 - 2] & 0xFF) << 8 | byArray[i2 - 1] & 0xFF;
            n2 /= 3;
            try {
                int n3;
                int n4;
                int[] nArray = new int[n2];
                for (n4 = 0; n4 < n2; ++n4) {
                    n3 = byArray[i2 + 4 + n4 * 3];
                    byte by = byArray[i2 + 5 + n4 * 3];
                    byte by2 = byArray[i2 + 6 + n4 * 3];
                    nArray[n4] = (n3 & 0xFF) << 16 | (by & 0xFF) << 8 | by2 & 0xFF;
                }
                g.a(nArray, n);
                n4 = 8 * (i2 - 4);
                g.a(byArray, n4, 32, n2 * 3);
                g.a(byArray, n4 + 32, 32, 1347179589);
                for (n3 = 0; n3 < n2; ++n3) {
                    g.a(byArray, n4 + 64 + n3 * 24, 24, nArray[n3]);
                }
                n3 = g.c(byArray, (n4 >> 3) + 4, (n4 >> 3) + 4 + 4 + n2 * 3);
                g.a(byArray, n4 + (8 + n2 * 3 << 3), 32, n3);
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            break;
        }
        if (!bl) {
            System.out.println("image data Color wrong");
        }
    }

    private static int b(byte[] byArray, int n, int n2) {
        int n3 = 0;
        if ((n & 7) == 0 && (n2 & 7) == 0) {
            int n4 = n2 >> 3;
            for (int i2 = 0; i2 < n4; ++i2) {
                n3 <<= 8;
                n3 += byArray[n >> 3] & 0xFF;
                n += 8;
            }
        } else {
            for (int i3 = 1; i3 <= n2; ++i3) {
                int n5 = n >> 3;
                n3 += (byArray[n5] >> 7 - (n - (n5 << 3)) & 1) << n2 - i3;
                ++n;
            }
        }
        return n3;
    }

    private static void a(byte[] byArray, int n, int n2, int n3) {
        int n4 = 0;
        byte by = 0;
        int n5 = n;
        int n6 = 1 << n2 - 1;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n7;
            byte[] byArray2;
            int n8;
            n4 = n5 >> 3;
            by = (byte)(1 << 7 - (n5 - (n4 << 3)));
            if ((n3 & n6) == n6) {
                int n9 = n4;
                n8 = n9;
                byArray2 = byArray;
                n7 = byArray[n9] | by;
            } else {
                int n10 = n4;
                n8 = n10;
                byArray2 = byArray;
                n7 = byArray[n10] & (byte)(0xFF ^ by);
            }
            byArray2[n8] = (byte)n7;
            n3 <<= 1;
            ++n5;
        }
    }

    private static int a(int n) {
        int n2 = g.b(k, l, n);
        g.b(n);
        return n2;
    }

    private static void b(int n) {
        l += n;
    }

    private static void c(int n) {
        l += n << 3;
    }

    private static byte c() {
        return (byte)g.a(8);
    }

    private static byte[] d(int n) {
        byte[] byArray = new byte[n];
        if ((l & 7) == 0) {
            System.arraycopy(k, l >> 3, byArray, 0, n);
            g.c(n);
        } else {
            for (int i2 = 0; i2 < n; ++i2) {
                byArray[i2] = g.c();
            }
        }
        return byArray;
    }

    private static int a(int n, int n2) {
        int n3;
        int n4;
        int n5;
        switch (n2) {
            case 1: {
                n5 = (n >> 5) * 255 / 7;
                n4 = (n >> 2 & 7) * 255 / 7;
                n3 = (n & 3) * 255 / 3;
                break;
            }
            case 0: {
                n5 = (n >> 11) * 255 / 31;
                n4 = (n >> 5 & 0x3F) * 255 / 63;
                n3 = (n & 0x1F) * 255 / 31;
                break;
            }
            default: {
                return n;
            }
        }
        return n5 << 16 | n4 << 8 | n3;
    }

    private static void d() {
        m = new long[256];
        for (int i2 = 0; i2 < 256; ++i2) {
            long l2 = i2;
            for (int i3 = 0; i3 < 8; ++i3) {
                l2 = (l2 & 1L) == 1L ? 0xEDB88320L ^ l2 >> 1 : l2 >> 1;
            }
            g.m[i2] = l2;
        }
    }

    private static int c(byte[] byArray, int n, int n2) {
        long l2 = 0xFFFFFFFFL;
        for (int i2 = n; i2 < n2; ++i2) {
            l2 = m[(int)((l2 ^ (long)byArray[i2]) & 0xFFL)] ^ l2 >> 8;
        }
        return (int)(l2 ^ 0xFFFFFFFFL);
    }

    public static Image a(Image image, int n) {
        int n2 = 0xFF00FF;
        int n3 = image.getWidth();
        int n4 = image.getHeight();
        Image image2 = Image.createImage((int)n3, (int)n4);
        Graphics graphics = image2.getGraphics();
        graphics.setColor(n2);
        graphics.fillRect(0, 0, n3, n4);
        graphics.drawImage(image, 0, 0, 20);
        int[] nArray = new int[n3 * n4];
        image2.getRGB(nArray, 0, n3, 0, 0, n3, n4);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            int n5;
            int n6;
            int[] nArray2;
            int n7 = nArray[i2];
            int n8 = (n7 & 0xFF00) >> 8;
            int n9 = n7 & 0xFF;
            n7 = (n7 & 0xFF0000) >> 16;
            if (n8 <= 8 && n7 >= 248 && n9 >= 248) {
                nArray2 = nArray;
                n6 = i2;
                n5 = 0xFF00FF;
            } else {
                int n10 = i2;
                nArray[n10] = nArray[n10] & 0xFFFFFF;
                int n11 = i2;
                n6 = n11;
                nArray2 = nArray;
                n5 = nArray[n11] | n << 24;
            }
            nArray2[n6] = n5;
        }
        System.gc();
        if (f.aa) {
            return image;
        }
        Image image3 = Image.createRGBImage((int[])nArray, (int)n3, (int)n4, (boolean)true);
        return image3;
    }

    static {
        g.d();
    }
}

