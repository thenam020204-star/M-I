/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Command
 *  javax.microedition.lcdui.CommandListener
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 *  javax.microedition.lcdui.TextBox
 *  javax.microedition.media.Manager
 *  javax.microedition.media.Player
 *  javax.microedition.media.PlayerListener
 *  javax.microedition.media.control.VolumeControl
 *  javax.microedition.midlet.MIDlet
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextBox;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.midlet.MIDlet;

public final class h
extends j
implements CommandListener,
PlayerListener {
    public static boolean a = false;
    public static byte b = (byte)-1;
    public static byte c = 0;
    public static int d = 0;
    public static boolean e = true;
    public static Image[] f = new Image[31];
    public static Image[] g = new Image[6];
    public static Image h = null;
    public static int i = 0;
    public static h j = null;
    public static byte k = 0;
    public static byte l = (byte)-1;
    public static boolean m = false;
    public static int n = 0;
    public static boolean o = false;
    public static byte p = 0;
    public static int q = 0;
    public static int r = 0;
    public static int s = 0;
    public static boolean t = false;
    public static short u = 0;
    public static short v = 0;
    public static short w = 0;
    public static final int[] x = new int[]{1, 2, 1, 2, 4, 4, 8, 8};
    public static int y = 0;
    public static short z = 1;
    public static int A = 0;
    public static int B = 1;
    public static short C = 0;
    public static short D = 1;
    public static int E = 0;
    public static short F = 0;
    public static short G = (short)10;
    public static short H = (short)8;
    public static short I = (short)3;
    public static short J = 0;
    public static short K = 0;
    public static short L = 0;
    public static short M = 0;
    public static short N = 0;
    public static short O = (short)20;
    public static short P = (short)100;
    public static short Q = 0;
    public static short R = 0;
    public static short S = 0;
    public static short T = 1;
    public static short U = 0;
    public static b V = new b(30, l.b);
    public static int[][] W = new int[30][4];
    public static int[][] X = new int[5][4];
    public static int[] Y = new int[4];
    public static int[] Z = new int[4];
    public static int aa = -1;
    public static int[][] ab = new int[2][2];
    public static byte ac = 0;
    public static i ad = null;
    public static boolean ae = false;
    public static Image af = null;
    public byte ag = (byte)-1;
    public static int ah = -1;
    public static byte ai = 0;
    public static int aj = 0;
    public static int ak = 1;
    public static int al = 0;
    public static byte am = 0;
    public static byte an = 0;
    public static byte ao = 0;
    public static String[] ap = null;
    public static short aq = 0;
    public static boolean ar = false;
    public static boolean as = false;
    public static boolean at = false;
    public static VolumeControl au = null;
    public static int av = 50;
    public static String[] aw;
    public static int ax;
    public static int ay;
    public static Player[] az;
    String aA;
    TextBox aB;
    public static int aC;
    public static int aD;
    public static int aE;
    public static String[] aF;
    public static String[] aG;
    public static long aH;
    public static long aI;
    public static byte aJ;
    public static byte aK;
    public static byte aL;
    public static boolean aM;
    public static boolean aN;
    public static boolean aO;
    public static boolean aP;

    public h(XMessage xMessage) {
        super(xMessage);
        j = this;
        this.setFullScreenMode(true);
        this.q();
        if (d.c()) {
            j.p();
        }
        g.a();
        if (d.d()) {
            j.p();
            return;
        }
    }

    private void q() {
        f.ad = 240;
        f.ae = 320;
        f.ab = -6;
        f.ac = -7;
        j.aR = true;
        k.a(this);
        f.a(16);
        f.ao = 0;
        f.a(f.ad, f.ae - f.ao);
        f.ar = true;
        j.j(12);
        f.ai = ".png";
        f.aj = true;
        k.s = false;
        k.r = true;
        f.Z = false;
        byte[] byArray = new byte[]{-1, -1, 5, 0, 0, 0, 0, 0, -1, -1, 5, 1, 0, 0, 0, 0, -1, -1, 5, 2, 0, 0, 0, 0, -1, -1, 5, 3, 0, 0, 0, 0, -1, -1, 5, 4, 0, 0, 0, 0, -1, -1, 5, 5, 0, 0, 0, 0, -1, -1, 5, 6, 0, 0, 0, 0, -1, -1, 5, 7, 0, 0, 0, 0};
        k.a(byArray);
        l.F = new boolean[l.E.length / 2];
        try {
            Class<?> clazz = Class.forName("com.nokia.mid.ui.DirectGraphics");
            if (clazz != null) {
                f.aa = true;
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void hideNotify() {
        super.hideNotify();
        h.af();
        if (aC != -1) {
            return;
        }
        if (b == 8 && ac == 0) {
            h.c((byte)1);
            ad.a((byte)13);
        }
        this.repaint();
        this.serviceRepaints();
    }

    public final void showNotify() {
        super.showNotify();
        if (b == 2 && at) {
            h.a(0, true);
        }
    }

    public final void a() {
        h.ae();
    }

    public final void b() {
        if (j.aU) {
            return;
        }
        if (aC != -1) {
            h.aj();
            return;
        }
        if (c != b) {
            e = true;
            this.t();
            System.gc();
            b = c;
            this.s();
            j.l();
            e = false;
        }
        ++d;
        switch (b) {
            case 0: {
                h.w();
                return;
            }
            case 1: {
                this.y();
                return;
            }
            case 2: {
                this.B();
                return;
            }
            case 7: {
                this.D();
                return;
            }
            case 8: {
                this.Q();
                return;
            }
            case 9: {
                h.aa();
                return;
            }
            case 10: {
                h.ad();
            }
        }
    }

    public final void a(Graphics graphics) {
        if (e) {
            return;
        }
        if (aC != -1) {
            h.n(graphics);
            return;
        }
        switch (b) {
            case 0: {
                h.e(graphics);
                return;
            }
            case 1: {
                h.f(graphics);
                return;
            }
            case 2: {
                h.g(graphics);
                return;
            }
            case 7: {
                this.h(graphics);
                return;
            }
            case 8: {
                this.i(graphics);
                return;
            }
            case 9: {
                h.l(graphics);
                return;
            }
            case 10: {
                h.m(graphics);
            }
        }
    }

    public final void a(boolean bl) {
        c c2 = (c)k.k;
        ((c)k.k).d = -1;
        ah = -1;
        h.b(bl);
    }

    public final void c() {
        block5: {
            int n;
            block6: {
                block4: {
                    if (k.a != 1 && k.a != 2 && k.a != 3 && k.a != 4 && k.a != 36 && k.a != 37) break block4;
                    if (k != 1) break block5;
                    n = 0;
                    break block6;
                }
                if (k != 1) break block5;
                n = k;
            }
            k.a(n);
        }
        if (k.a == 38 && !aO) {
            h.f(8);
        }
    }

    public final void d() {
        h.R();
        k.k.c(0);
        k.k.a(0, false);
        h.W();
        h.a((byte)7, 0);
    }

    public final int a(int n) {
        if (n == 76) {
            return 65280;
        }
        return -1;
    }

    public final void a(Graphics graphics, int n, int n2, int n3, int n4) {
        h.c(graphics, n, n2, n3, n4);
    }

    public final void a(Graphics graphics, String string, int n, int n2, int n3) {
        int n4 = 6449540;
        int n5 = 0xFFFFFF;
        graphics.setColor(n4);
        j.a(graphics, string, n + 1, n2 + 1, n3, false);
        graphics.setColor(n5);
        j.a(graphics, string, n, n2, n3, true);
    }

    public final void b(Graphics graphics) {
        h.a(graphics, f[10], f.ad - 15, f.ae - 16, j.aX % 3 * 15, 0, 15, 16);
    }

    public final void a(Graphics graphics, byte by, int n, byte by2) {
        graphics.setClip(0, 0, f.ad, f.ae);
        int n2 = 0;
        if (by == 1) {
            n2 = f.ad;
        }
        if (l.I[by2] != -1) {
            if (by2 != l) {
                h.f[25] = g.a("head_" + l.I[by2] + ".png", false, 0, 0);
                l = by2;
            }
            if (f[25] != null) {
                if (by == 0) {
                    graphics.drawImage(f[25], n2, n + 1, 36);
                    h.d(graphics, n2, n + 1 - f[25].getHeight(), f[25].getWidth(), f[25].getHeight());
                    n2 += f[25].getWidth();
                } else {
                    n2 = f.ad - f[25].getWidth();
                    graphics.drawImage(f[25], n2, n + 1, 36);
                    h.d(graphics, n2, n + 1 - f[25].getHeight(), f[25].getWidth(), f[25].getHeight());
                }
            }
        }
        String string = l.H[by2];
        if (by2 == 0) {
            string = j.aS;
        }
        int n3 = d.a.stringWidth(string) + 10;
        if (by == 1) {
            n2 -= n3;
        }
        int n4 = n - 20;
        --n2;
        if (by == 1) {
            n2 += 2;
        }
        graphics.setClip(0, 0, f.ad, f.ae);
        d.a(graphics, n2, n4, n3, 20, 1598381);
        h.d(graphics, n2 - 1, n4 - 1, n3 + 2, 22);
        graphics.setClip(0, 0, f.ad, f.ae);
        n2 += 5;
        if (by == 0) {
            // empty if block
        }
        d.a(graphics, string, n2, n4 + 1 + (20 - d.c >> 1), 20, 5781512, 16762681, 0);
    }

    public final void c(Graphics graphics) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(0xFFFFFF);
        if (d.a() == d.h) {
            graphics.drawString("d.cn...", f.ad >> 1, (f.ae >> 1) - (d.d + d.c), 17);
            graphics.setColor(0xFFFF00);
            graphics.drawString("\u5c0f\u63d0\u793a:\u53f3\u8f6f\u952e", f.ad >> 1, f.ae - d.c * 2 - 4, 17);
            graphics.drawString("\u53ef\u6253\u5f00\u6e38\u620f\u79d8\u7c4d", f.ad >> 1, f.ae - d.c - 2, 17);
        }
    }

    public final boolean a(byte by) {
        switch (by) {
            case 0: {
                ++j.aY;
                ao = (byte)5;
                h.j();
                h.ag();
                h.b((byte)10);
                break;
            }
            case 1: {
                if (!m) {
                    h.b("\u665a\u996d\u8fc7\u540e");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 2: {
                if (!m) {
                    h.b("\u7b2c\u4e8c\u5929");
                    k = 0;
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 3: {
                if (!m) {
                    h.b("\u7b2c\u4e09\u5929");
                    k = 0;
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 4: {
                h.c((byte)1);
                ad.a((byte)16);
                return true;
            }
            case 5: {
                if (!m) {
                    h.b("\u4e00\u573a\u9b54\u754c\u7684\u6545\u4e8b\uff0c\u5373\u5c06\u5f00\u59cb");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 6: {
                if (!m) {
                    h.b("\u7b2c\u4e94\u5929");
                    k = 0;
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 7: {
                if (!m) {
                    h.b("\u7b2c\u56db\u5929");
                    k = 0;
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 8: {
                if (!m) {
                    h.b("\u4e09\u4e2a\u6708\u540e");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 9: {
                if (!m) {
                    h.b("\u6211\u4eec\u5f97\u82f1\u96c4\u7ee7\u7eed\u4ed6\u7ec3\u5251\u7684\u751f\u6d3b\uff0c\u82b1\u5b50\u8fd8\u662f\u8fd9\u6837\u966a\u5728\u4ed6\u8eab\u8fb9\u3002\u82f1\u96c4\u662f\u5426\u5728\u7b49\u5f85\u4e0e\u8428\u6797\u7684\u518d\u76f8\u89c1\u5462\uff1f");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 10: {
                if (!m) {
                    h.b("\u800c\u5854\u5854\u2026\u2026\u4f5c\u4e3a\u8428\u6797\u65b0\u7684\u5bb6\u81e3\uff0c\u5728\u5bab\u6bbf\u91cc\u548c\u4e01\u514b\u8fdb\u884c\u5fd9\u788c\u7684\u6253\u626b\u5de5\u4f5c\uff0c\u9b54\u738b\u5b87\u6625\u4e00\u8e76\u4e0d\u632f\u3002");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 11: {
                if (!m) {
                    h.b("\u96ea\u4e38\u7559\u5728\u4e86\u4e9a\u7559\u7279\u6751\u91cc\uff0c\u5728\u9644\u8fd1\u8fdb\u884c\u523b\u82e6\u7684\u4fee\u70bc\u3002");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            case 12: {
                if (!m) {
                    h.b("\u90a3\u4e48\uff0c\u963f\u5854\u96f7\u5462\u2026\u2026");
                    m = true;
                    break;
                }
                m = h.r();
                return !m;
            }
            default: {
                System.out.println("call my script does not define");
            }
        }
        return false;
    }

    private static void b(String string) {
        o = true;
        n = 0;
        d.a(string, null, f.ad - 10);
    }

    private static boolean r() {
        ++n;
        if (j.g(0x100000) || j.g(2) || j.g(1) || j.aR && j.g(65536) || !j.aR && j.g(131072)) {
            n += 4;
        }
        return o;
    }

    private static void d(Graphics graphics) {
        if (!o) {
            return;
        }
        graphics.setColor(0);
        graphics.fillRect(0, 0, f.ad, f.ae);
        int n = f.ae / 2;
        int n2 = 10 + (f.ae - n >> 1);
        int n3 = d.c + d.d;
        graphics.setClip(0, n2, f.ad, f.ae - n2);
        int n4 = f.ae - d.c - (f.ae - n >> 1);
        int n5 = h.n;
        int n6 = 0;
        for (int i2 = 0; i2 < d.e.length; ++i2) {
            int n7;
            int n8;
            int n9;
            if (n4 - n5 < n2 + n3) {
                n9 = 0;
                n8 = 0xFFFFFF;
                n7 = n5 - (n4 - (n2 + n3));
            } else {
                n9 = 0xFFFFFF;
                n8 = 0;
                n7 = n5;
            }
            n6 = d.a(n9, n8, n7, n3);
            graphics.setColor(n6);
            graphics.drawString(d.e[i2], f.ad >> 1, n4 - n5, 17);
            n5 -= n3;
        }
        if (h.n > d.e.length * n3 + (n4 - n2) - n3) {
            o = false;
        }
        h.a(graphics, 0, -1, l.B);
    }

    public static void a(Graphics graphics, Image image, int n, int n2, int n3, int n4, int n5, int n6) {
        graphics.setClip(n, n2, n5, n6);
        graphics.drawImage(image, n - n3, n2 - n4, 20);
    }

    public static void a(Graphics graphics, int n, int n2, boolean bl) {
        block15: {
            int n3;
            String[] stringArray;
            Font font;
            int n4;
            String string;
            Graphics graphics2;
            block17: {
                block16: {
                    block14: {
                        if (bl) break block14;
                        if (n == 2) {
                            h.a(graphics, f[11], 0, f.ae - 11, 0, 17, 10, 11);
                        }
                        if (n2 == 3) {
                            return;
                        }
                        if (j.aR) {
                            if (n == 0) {
                                h.a(graphics, f[11], 0, f.ae - 10, 0, 0, 10, 8);
                            }
                            if (n2 == 1) {
                                h.a(graphics, f[11], f.ad - 9, f.ae - 10, 0, 8, 9, 9);
                                return;
                            }
                        } else {
                            if (n == 0) {
                                h.a(graphics, f[11], f.ad - 10, f.ae - 10, 0, 0, 10, 8);
                            }
                            if (n2 == 1) {
                                h.a(graphics, f[11], 0, f.ae - 10, 0, 8, 9, 9);
                                return;
                            }
                        }
                        break block15;
                    }
                    graphics.setColor(0xFFFFFF);
                    if (!j.aR) break block16;
                    if (n != -1) {
                        graphics.drawString(l.C[n], 2, f.ae - d.c - 2, 20);
                    }
                    if (n2 == -1) break block15;
                    graphics2 = graphics;
                    string = l.C[n2];
                    n4 = f.ad;
                    font = graphics.getFont();
                    stringArray = l.C;
                    n3 = n2;
                    break block17;
                }
                if (n2 != -1) {
                    graphics.drawString(l.C[n2], 2, f.ae - d.c - 2, 20);
                }
                if (n == -1) break block15;
                graphics2 = graphics;
                string = l.C[n];
                n4 = f.ad;
                font = graphics.getFont();
                stringArray = l.C;
                n3 = n;
            }
            graphics2.drawString(string, n4 - font.stringWidth(stringArray[n3]) - 2, f.ae - d.c - 2, 20);
        }
    }

    public static final void b(byte by) {
        c = by;
    }

    private void s() {
        d = 0;
        switch (b) {
            case 0: {
                h.u();
                return;
            }
            case 1: {
                h.x();
                return;
            }
            case 2: {
                h.z();
                return;
            }
            case 7: {
                h.C();
                return;
            }
            case 8: {
                h.O();
                return;
            }
            case 9: {
                h.Y();
                return;
            }
            case 10: {
                h.ab();
            }
        }
    }

    private void t() {
        switch (b) {
            case 0: {
                h.v();
                return;
            }
            case 1: {
                return;
            }
            case 2: {
                h.A();
                return;
            }
            case 7: {
                return;
            }
            case 8: {
                h.P();
                return;
            }
            case 9: {
                h.Z();
                return;
            }
            case 10: {
                h.ac();
            }
        }
    }

    private static void u() {
        p = 0;
        j.aS = "\u963f\u9c81\u9c81";
        h.I();
        i = j.aV * 2;
        boolean bl = true;
        try {
            h.f[0] = Image.createImage((String)"/cmcc.png");
            i = j.aV * 5;
        }
        catch (Exception exception) {
            bl = false;
        }
        if (!bl) {
            try {
                h.f[0] = Image.createImage((String)"/sp.png");
                i = j.aV * 3;
            }
            catch (Exception exception) {
                try {
                    h.f[0] = Image.createImage((String)"/cp.png");
                    i = j.aV * 1;
                }
                catch (Exception exception2) {
                    p = 1;
                }
            }
        }
        h.ah();
    }

    private static void v() {
        h.f[0] = null;
    }

    private static void w() {
        block13: {
            byte by;
            block15: {
                block14: {
                    block11: {
                        block12: {
                            if (p != 0) break block11;
                            if (i <= 0) break block12;
                            if (--i == j.aV * 3) {
                                try {
                                    h.f[0] = null;
                                    h.f[0] = Image.createImage((String)"/sp.png");
                                    return;
                                }
                                catch (Exception exception) {
                                    return;
                                }
                            }
                            if (i == j.aV * 1) {
                                try {
                                    h.f[0] = null;
                                    h.f[0] = Image.createImage((String)"/cp.png");
                                    return;
                                }
                                catch (Exception exception) {
                                    p = 1;
                                    return;
                                }
                            }
                            break block13;
                        }
                        p = 1;
                        return;
                    }
                    if (p != 1) break block13;
                    if (!j.n()) break block14;
                    if (av == 0) {
                        av = 50;
                    }
                    by = 1;
                    break block15;
                }
                if (!j.o()) break block13;
                av = 0;
                by = 1;
            }
            h.b(by);
        }
    }

    private static void e(Graphics graphics) {
        graphics.setClip(0, 0, f.ad, f.ae);
        if (p == 0) {
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 0, f.ad, f.ae);
            if (f[0] != null) {
                graphics.drawImage(f[0], f.ad - f[0].getWidth() >> 1, f.ae - f[0].getHeight() >> 1, 20);
                return;
            }
        } else if (p == 1) {
            int n;
            String[] stringArray;
            Graphics graphics2;
            graphics.setColor(0);
            graphics.fillRect(0, 0, f.ad, f.ae);
            graphics.setColor(0xFFFFFF);
            graphics.drawString(l.d[0], f.ad >> 1, f.ae - d.c >> 1, 17);
            if (j.aR) {
                graphics.drawString(l.C[4], 2, f.ae - d.c - 2, 20);
                graphics2 = graphics;
                stringArray = l.C;
                n = 5;
            } else {
                graphics.drawString(l.C[5], 2, f.ae - d.c - 2, 20);
                graphics2 = graphics;
                stringArray = l.C;
                n = 4;
            }
            graphics2.drawString(stringArray[n], f.ad - d.b - 2, f.ae - d.c - 2, 20);
            h.a(graphics, 4, 5, true);
        }
    }

    private static void x() {
        i = 8;
        for (int i2 = 0; i2 < g.length; ++i2) {
            h.g[i2] = null;
        }
    }

    private void y() {
        switch (++i) {
            case 10: {
                if (ad == null) {
                    ad = new i(this);
                }
                f.a("l9.pro");
                i += 9;
                return;
            }
            case 20: {
                m[] mArray = new c[48];
                for (int i2 = 0; i2 < 48; ++i2) {
                    mArray[i2] = new c();
                }
                k.a(l.J, mArray);
                h.S();
                h.L();
                h.M();
                k.a(k.k, -1, -1, true);
                i += 9;
                return;
            }
            case 30: {
                i += 9;
                return;
            }
            case 40: {
                h.a(l.K);
                i += 9;
                return;
            }
            case 50: {
                h.b((byte)2);
            }
        }
    }

    private static void f(Graphics graphics) {
        graphics.setColor(0);
        graphics.fillRect(0, 0, f.ad, f.ae);
        int n = f.ae >> 1;
        graphics.setColor(0xFFFFFF);
        graphics.drawString("d.cn...", f.ad >> 1, n - (d.d + d.c), 17);
        int n2 = d.a.stringWidth("d.cn...") + 30;
        int n3 = n2 / 50;
        int n4 = i * n3;
        int n5 = (f.ad - n2 >> 1) + (n2 % 50 >> 1);
        graphics.setColor(0xFFFFFF);
        graphics.drawRect(n5, n, n3 * 50, 3);
        graphics.fillRect(n5, n, n4, 3);
        graphics.drawRect(n5 - 15, n - 30, n3 * 50 + 30, 40);
        h.a(graphics, -1, -1, l.B);
    }

    private static void z() {
        q = 0;
        r = 0;
        s = 0;
        t = false;
        try {
            h.f[11] = g.a("softkey" + f.ai, f.aj, 0, 0);
            h.g[0] = g.a("menu_bg" + f.ai, f.aj, 0, 0);
            h.g[5] = g.a("foreword" + f.ai, f.aj, 0, 0);
            h.g[4] = g.a("menu_string" + f.ai, f.aj, 0, 0);
            h.g[1] = g.a("copyright" + f.ai, f.aj, 0, 0);
            h.g[2] = g.a("title_1" + f.ai, f.aj, 0, 0);
            h.g[3] = g.a("title_2" + f.ai, f.aj, 0, 0);
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        h.a(0, true);
    }

    private static void A() {
        h.u(0);
        for (int i2 = 0; i2 < g.length; ++i2) {
            h.g[i2] = null;
        }
        System.gc();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void B() {
        if (q == 0) {
            int n;
            int n2;
            boolean bl = false;
            if (j.i(4)) {
                if (--r < 0) {
                    n2 = 5;
                }
            } else if (j.i(8) && ++r > 5) {
                n2 = r = 0;
            }
            if (j.n()) {
                if (r == 0) {
                    j.aS = "\u963f\u9c81\u9c81";
                    ad.a(j.aS, l.d[2]);
                    q = 4;
                } else if (r == 1) {
                    boolean bl2 = h.ai();
                    if (bl2) {
                        t = true;
                        h.b((byte)7);
                    } else {
                        ad.a(l.d[1]);
                        q = 5;
                    }
                } else if (r == 2) {
                    q = 2;
                } else if (r == 3) {
                    h.a("[C:0000FF]\u6e38\u620f\u4ecb\u7ecd[/C]:|10\u591a\u5e74\u524d\uff0c\u9b54\u738b\u5b87\u6625\u5c06\u7ef4\u8fbe\u5361\u4f0a\u59c6\u661f\u7403\u4e0a\u7684\u4eba\u7c7b\u7684\u5927\u90e8\u5206\u826f\u5fc3\u593a\u8d70\uff0c\u5e76\u4e14\u51e0\u4e4e\u628a\u4eba\u7c7b\u90fd\u53d8\u6210\u4e86\u6076\u9b54\u548c\u9b54\u7269\u3002\u7ecf\u8fc710\u591a\u5e74\u7684\u5c81\u6708\uff0c\u6211\u4eec\u7684\u7537\u4e3b\u89d2\u662f\u4ec5\u5b58\u4e0d\u591a\u7684\u4eba\u7c7b\uff0c\u4ed6\u7684\u517b\u6bcd\u8eab\u4e3a\u53ec\u5524\u5e08\uff0c\u6536\u96c6\u4e86\u53ec\u5524\u9b54\u738b\u5b87\u6625\u7684\u6750\u6599\uff0c\u5c06\u8981\u8fdb\u884c\u53ec\u5524\u4eea\u5f0f\u3002\u4f46\uff0c\u8c01\u77e5\u9053\uff0c\u9634\u5dee\u9633\u9519\u2026\u2026|[C:0000FF]\u6e38\u620f\u89c4\u5219[/C]:|\u6e38\u620f\u4e2d\u53ef\u8c03\u51fa\u6e38\u620f\u83dc\u5355\uff0c\u9009\u62e9\u72b6\u6001\u9009\u9879\u53ef\u4ee5\u7528\u6765\u63d0\u5347\u4f60\u7684\u5c5e\u6027(\u6bcf\u53471\u7ea7\u80fd\u83b7\u5f973\u70b9\u5c5e\u6027\u70b9\uff0c\u529b\u91cf\u53ef\u63d0\u9ad8\u653b\u51fb\u529b\uff0c\u4f53\u8d28\u53ef\u63d0\u9ad8\u9632\u5fa1\u529b\uff0c\u654f\u6377\u53ef\u63d0\u9ad8\u56de\u907f\u7387\uff0c\u6280\u5de7\u53ef\u63d0\u9ad8\u66b4\u51fb\u51e0\u7387\u4e0e\u66b4\u51fb\u500d\u6570)\uff0c\u9009\u62e9\u6b66\u5668\u9009\u9879\u53ef\u4ee5\u628a\u6b66\u5668\u88c5\u5907\u5230\u8eab\u4e0a\uff0c\u9009\u62e9\u9053\u5177\u9009\u9879\u53ef\u4ee5\u4f7f\u7528\u5df2\u83b7\u5f97\u7684\u9053\u5177\u6216\u8005\u5c06\u9053\u5177\u8bbe\u7f6e\u5230\u5feb\u6377\u952e\u4e0a\uff0c\u9009\u62e9\u6280\u80fd\u9009\u9879\u53ef\u4ee5\u5347\u7ea7\u559c\u6b22\u7684\u6280\u80fd\u6216\u8005\u5c06\u6280\u80fd\u8bbe\u7f6e\u5230\u5feb\u6377\u952e\u4e0a(\u6bcf\u5347\u4e00\u7ea7\u80fd\u83b7\u5f97\u4e00\u70b9\u6280\u80fd\u70b9)\uff0c\u9009\u62e9\u7cfb\u7edf\u9009\u9879\u5219\u53ef\u4ee5\u4fdd\u5b58\u6e38\u620f\u6216\u8bbe\u7f6e\u6e38\u620f\u6216\u67e5\u770b\u5e2e\u52a9\u6216\u9000\u51fa\u6e38\u620f\u3002|[C:0000FF]\u64cd\u4f5c\u65b9\u6cd5[/C]:|\u65b9\u5411\u952e\u63a7\u5236\u4eba\u7269\u79fb\u52a8\u3002\u5728\u6218\u6597\u72b6\u6001\u65f6\uff0c\u4e2d\u95f4\u786e\u8ba4\u952e\u53ef\u4ee5\u8fdb\u884c\u653b\u51fb\uff0c\u5c4f\u5e55\u4e0b\u65b9\u6709\u6280\u80fd\u5feb\u6377\u952e\u53ef\u4ee5\u70b9\u51fb\u4f7f\u7528\uff0c\u5206\u522b\u5bf9\u5e94\u6570\u5b571\u7684\u6846\u6570\u5b573\u7684\u6846\u6570\u5b577\u7684\u6846\u6570\u5b579\u7684\u6846\uff0c\u53ef\u4ee5\u7528\u6765\u8bbe\u7f6e\u6210\u6280\u80fd\u5feb\u6377\u952e\uff0c*\u7684\u6846#\u7684\u6846\u53ef\u4ee5\u7528\u6765\u8bbe\u7f6e\u6210\u9053\u5177\u5feb\u6377\u952e\u3002\u4e2d\u95f4\u786e\u8ba4\u952e\u53ef\u4ee5\u4e0e\u4eba\u7269\u5bf9\u8bdd\u3002", f.ad - 8);
                    q = 1;
                } else if (r == 4) {
                    h.a("\u5ba2\u670d\u7535\u8bdd\uff1a021-64363139|\u5ba2\u670d\u90ae\u7bb1\uff1alevel9info@gmail.com|\u5f00\u53d1\u5546:LEVEL-9", f.ad - 8);
                    q = 3;
                } else if (r == 5) {
                    j.p();
                } else if (r == 6) {
                    try {
                        j.p();
                        j.aQ.platformRequest("http://gm.yicha.cn/game/idx.do?");
                    }
                    catch (Exception exception) {}
                }
            }
            if (j.i(x[y])) {
                if (++y != x.length) return;
                System.out.println("cheat");
                a = true;
                if (a) {
                    j.aS = "\u6dd8\u7c73";
                }
                n = 0;
            } else {
                if (!j.m()) return;
                n = 0;
            }
            y = n;
            return;
        }
        if (q == 1) {
            h.h();
            if (!j.o()) return;
        } else if (q == 2) {
            h.i();
            if (!j.o()) return;
        } else if (q == 3) {
            h.h();
            if (!j.o()) return;
        } else {
            if (q == 4) {
                if (s == 0) {
                    ad.d();
                    if (i.b() != 0) return;
                    int n = i.c();
                    if (n == 0) {
                        t = false;
                        d.a("10\u591a\u5e74\u524d\uff0c\u9b54\u738b\u5b87\u6625\u5c06\u7ef4\u8fbe\u5361\u4f0a\u59c6\u661f\u7403\u4e0a\u7684\u4eba\u7c7b\u7684\u5927\u90e8\u5206\u826f\u5fc3\u593a\u8d70\uff0c\u5e76\u4e14\u51e0\u4e4e\u628a\u4eba\u7c7b\u90fd\u53d8\u6210\u4e86\u6076\u9b54\u548c\u9b54\u7269\u2026\u2026||\u7ecf\u8fc710\u591a\u5e74\u7684\u5c81\u6708\uff0c\u6211\u4eec\u7684\u7537\u4e3b\u89d2\u662f\u4ec5\u5b58\u4e0d\u591a\u7684\u4eba\u7c7b\uff0c\u4ed6\u7684\u517b\u6bcd\u8eab\u4e3a\u53ec\u5524\u5e08\uff0c\u6536\u96c6\u4e86\u53ec\u5524\u9b54\u738b\u5b87\u6625\u7684\u6750\u6599\uff0c\u5c06\u8981\u8fdb\u884c\u53ec\u5524\u4eea\u5f0f\u3002||\u4f46\uff0c\u8c01\u77e5\u9053\uff0c\u9634\u5dee\u9633\u9519\u2026\u2026", null, f.ad - 10);
                        q = 6;
                        return;
                    }
                    if (n == 1) {
                        this.a(l.d[3], j.aS, 3, 0);
                        s = 1;
                        return;
                    }
                    q = 0;
                    return;
                }
                if (s != 1) return;
                if (this.aA == null) return;
                if (this.aA.trim().length() > 0) {
                    j.aS = this.aA;
                }
                ad.a(j.aS, l.d[2]);
                s = 0;
                return;
            }
            if (q == 6) {
                ++s;
                if (j.g(0x100000) || j.g(2) || j.g(1) || j.aR && j.g(65536) || !j.aR && j.g(131072)) {
                    s += 4;
                    return;
                }
                if (!j.o()) return;
                h.b((byte)7);
                return;
            }
            if (q == 5) {
                ad.d();
                if (i.b() != 0) return;
            } else if (q == 7) {
                ad.d();
                if (i.b() != 0) return;
            } else {
                if (q != 10) return;
                h.h();
                if (!j.o()) return;
            }
        }
        q = 0;
    }

    private static void g(Graphics graphics) {
        block31: {
            boolean bl;
            int n;
            int n2;
            Graphics graphics2;
            block24: {
                block30: {
                    block29: {
                        block28: {
                            block27: {
                                block26: {
                                    block25: {
                                        block23: {
                                            block18: {
                                                int n3;
                                                int n4;
                                                block19: {
                                                    int n5;
                                                    short s;
                                                    short s2;
                                                    Image image;
                                                    Graphics graphics3;
                                                    block22: {
                                                        block21: {
                                                            block20: {
                                                                if (q != 0 && q != 5) break block18;
                                                                graphics.setColor(0);
                                                                graphics.fillRect(0, 0, f.ad, f.ae);
                                                                if (g[0] != null) {
                                                                    graphics.drawImage(g[0], f.ad - g[0].getWidth() >> 1, 0, 20);
                                                                }
                                                                if (g[3] != null) {
                                                                    int n6;
                                                                    n4 = 0;
                                                                    n3 = (f.ad + 36) / 36 + 1;
                                                                    int n7 = j.aX * 3 % 37;
                                                                    for (n6 = 0; n6 < n3; ++n6) {
                                                                        graphics.drawImage(g[3], -36 + n6 * 36 + n7, f.ae - 44, 20);
                                                                    }
                                                                    for (n6 = 0; n6 < n3; ++n6) {
                                                                        j.a(graphics, g[3], 0, 0, 36, 44, 1, n6 * 36 - n7, 0, 20);
                                                                    }
                                                                }
                                                                if (g[1] != null) {
                                                                    graphics.setClip(0, 0, f.ad, f.ae);
                                                                    graphics.drawImage(g[1], f.ad - g[1].getWidth() >> 1, f.ae - g[1].getHeight() - 2, 20);
                                                                }
                                                                if (g[2] == null) break block19;
                                                                if (u != 0) break block20;
                                                                v = (short)d.a(111, 241);
                                                                w = (short)d.a(166, 206);
                                                                u = 1;
                                                                break block19;
                                                            }
                                                            if ((u = (short)(u + 1)) > 4) {
                                                                u = 0;
                                                            }
                                                            if (u != 2 && u != 3) break block21;
                                                            graphics3 = graphics;
                                                            image = g[2];
                                                            s2 = v;
                                                            s = w;
                                                            n5 = 0;
                                                            break block22;
                                                        }
                                                        if (u != 4) break block19;
                                                        graphics3 = graphics;
                                                        image = g[2];
                                                        s2 = v;
                                                        s = w;
                                                        n5 = 11;
                                                    }
                                                    h.a(graphics3, image, (int)s2, (int)s, n5, 0, 11, 11);
                                                }
                                                graphics.setClip(0, 0, f.ad, f.ae);
                                                graphics.setColor(0xFFFFFF);
                                                n4 = f.ad >> 1;
                                                n3 = f.ae - 44;
                                                h.a(graphics, g[4], n4 - 32, n3 -= 8, 0, 0 + r * 16, 64, 16);
                                                graphics.setClip(0, 0, f.ad, f.ae);
                                                d.b(graphics, n4 - 63 - (j.aX & 3), n3 + (d.c >> 1) - 1, 6, 0, 0xFFFFFF);
                                                d.b(graphics, n4 + 63 + (j.aX & 3), n3 + (d.c >> 1) - 1, 2, 0, 0xFFFFFF);
                                                h.a(graphics, 0, -1, l.B);
                                                if (q == 5) {
                                                    ad.b(graphics);
                                                }
                                                return;
                                            }
                                            if (q != 1) break block23;
                                            graphics.setColor(168, 136, 64);
                                            graphics.fillRect(0, 0, f.ad, f.ae);
                                            boolean bl2 = false;
                                            int n8 = 0;
                                            int n9 = f.ad - 8;
                                            int n10 = f.ae - 8;
                                            d.a(graphics, 4, 4, n9, n10);
                                            h.b(graphics, l.c[3], f.ad >> 1, 4 + d.d, 17, false);
                                            n8 = 4 + (d.c + (d.d << 1));
                                            h.b(graphics, 4, n8, n9, n10 -= d.c + (d.d << 1) + 10);
                                            graphics2 = graphics;
                                            n2 = -1;
                                            n = 1;
                                            bl = l.B;
                                            break block24;
                                        }
                                        if (q != 2) break block25;
                                        graphics.setColor(168, 136, 64);
                                        graphics.fillRect(0, 0, f.ad, f.ae);
                                        boolean bl3 = false;
                                        boolean bl4 = false;
                                        int n11 = f.ad - 8;
                                        int n12 = f.ae - 8;
                                        d.a(graphics, 4, 4, n11, n12);
                                        h.b(graphics, l.c[2], f.ad >> 1, 4 + d.d, 17, false);
                                        h.a(graphics, f.ad >> 1, f.ae >> 1);
                                        graphics2 = graphics;
                                        n2 = -1;
                                        n = 1;
                                        bl = l.B;
                                        break block24;
                                    }
                                    if (q != 3) break block26;
                                    graphics.setColor(168, 136, 64);
                                    graphics.fillRect(0, 0, f.ad, f.ae);
                                    boolean bl5 = false;
                                    int n13 = 0;
                                    int n14 = f.ad - 8;
                                    int n15 = f.ae - 8;
                                    d.a(graphics, 4, 4, n14, n15);
                                    h.b(graphics, l.c[4], f.ad >> 1, 4 + d.d, 17, false);
                                    n13 = 4 + (d.c + (d.d << 1));
                                    h.b(graphics, 4, n13, n14, n15 -= d.c + (d.d << 1));
                                    graphics2 = graphics;
                                    n2 = -1;
                                    n = 1;
                                    bl = l.B;
                                    break block24;
                                }
                                if (q != 4) break block27;
                                graphics.setColor(0);
                                graphics.fillRect(0, 0, f.ad, f.ae);
                                ad.b(graphics);
                                graphics2 = graphics;
                                n2 = 0;
                                n = 1;
                                bl = l.B;
                                break block24;
                            }
                            if (q != 6) break block28;
                            graphics.setColor(0);
                            graphics.fillRect(0, 0, f.ad, f.ae);
                            int n16 = f.ae / 2;
                            int n17 = 10 + (f.ae - n16 >> 1);
                            int n18 = d.c + d.d;
                            if (g[5] != null) {
                                graphics.drawImage(g[5], f.ad >> 1, n17, 17);
                                n17 += g[5].getHeight() + 2;
                            }
                            graphics.setClip(0, n17, f.ad, f.ae - n17);
                            int n19 = f.ae - d.c - (f.ae - n16 >> 1);
                            int n20 = s;
                            int n21 = 0;
                            for (int i2 = 0; i2 < d.e.length; ++i2) {
                                int n22;
                                int n23;
                                int n24;
                                if (n19 - n20 < n17 + n18) {
                                    n24 = 0;
                                    n23 = 0xFFFFFF;
                                    n22 = n20 - (n19 - (n17 + n18));
                                } else {
                                    n24 = 0xFFFFFF;
                                    n23 = 0;
                                    n22 = n20;
                                }
                                n21 = d.a(n24, n23, n22, n18);
                                graphics.setColor(n21);
                                graphics.drawString(d.e[i2], f.ad >> 1, n19 - n20, 17);
                                n20 -= n18;
                            }
                            if (s > d.e.length * n18 + (n19 - n17) - n18) {
                                h.b((byte)7);
                            }
                            graphics2 = graphics;
                            n2 = 0;
                            n = 1;
                            bl = l.B;
                            break block24;
                        }
                        if (q != 7) break block29;
                        graphics.setColor(0);
                        graphics.fillRect(0, 0, f.ad, f.ae);
                        graphics.setColor(0xFFFFFF);
                        graphics.drawString("\u514d\u8d39\u6295\u7968", f.ad >> 1, 5, 17);
                        ad.b(graphics);
                        graphics2 = graphics;
                        n2 = 0;
                        n = 1;
                        bl = l.B;
                        break block24;
                    }
                    if (q != 8) break block30;
                    graphics.setColor(0);
                    graphics.fillRect(0, 0, f.ad, f.ae);
                    ad.b(graphics);
                    graphics2 = graphics;
                    n2 = 0;
                    n = 1;
                    bl = l.B;
                    break block24;
                }
                if (q != 10) break block31;
                graphics.setColor(168, 136, 64);
                graphics.fillRect(0, 0, f.ad, f.ae);
                boolean bl6 = false;
                int n25 = 0;
                int n26 = f.ad - 8;
                int n27 = f.ae - 8;
                d.a(graphics, 4, 4, n26, n27);
                graphics.setColor(0xFFFFFF);
                n25 = 4 + (d.c + (d.d << 1));
                h.b(graphics, 4, n25, n26, n27 -= d.c + (d.d << 1));
                graphics2 = graphics;
                n2 = -1;
                n = 1;
                bl = false;
            }
            h.a(graphics2, n2, n, bl);
        }
    }

    private static void C() {
        i = 8;
    }

    private void D() {
        switch (++i) {
            case 10: {
                try {
                    h.f[1] = g.a("num" + f.ai, f.aj, 0, 0);
                    h.f[30] = g.a("uinum" + f.ai, f.aj, 0, 0);
                    h.f[2] = g.a("english" + f.ai, f.aj, 0, 0);
                    h.f[3] = g.a("item_0" + f.ai, f.aj, 0, 0);
                    h.f[4] = g.a("item_1" + f.ai, f.aj, 0, 0);
                    h.f[26] = g.a("item_2" + f.ai, f.aj, 0, 0);
                    h.f[6] = g.a("item_num" + f.ai, f.aj, 0, 0);
                    h.f[7] = g.a("shadow_0" + f.ai, f.aj, 0, 0);
                    h.f[8] = g.a("statusbar" + f.ai, f.aj, 0, 0);
                    h.f[27] = g.a("statuslife" + f.ai, f.aj, 0, 0);
                    h.f[29] = g.a("exp_st" + f.ai, f.aj, 0, 0);
                    h.f[28] = g.a("shortkey_num" + f.ai, f.aj, 0, 0);
                    h.f[9] = g.a("status_num" + f.ai, f.aj, 0, 0);
                    h.f[21] = g.a("pointtip" + f.ai, f.aj, 0, 0);
                    h.f[10] = g.a("press" + f.ai, f.aj, 0, 0);
                    h.f[17] = g.a("hitnum" + f.ai, f.aj, 0, 0);
                    h.f[19] = g.a("hitnum_1" + f.ai, f.aj, 0, 0);
                    h.f[18] = g.a("hitnumeffect" + f.ai, f.aj, 0, 0);
                    h.f[20] = g.a("critical" + f.ai, f.aj, 0, 0);
                    h.f[5] = g.a("skills" + f.ai, f.aj, 0, 0);
                    h.f[12] = g.a("equip_0" + f.ai, f.aj, 0, 0);
                    h.f[13] = g.a("equip_1" + f.ai, f.aj, 0, 0);
                    h.f[14] = g.a("equip_2" + f.ai, f.aj, 0, 0);
                    h.f[15] = g.a("equip_3" + f.ai, f.aj, 0, 0);
                    h.f[16] = g.a("equip_4" + f.ai, f.aj, 0, 0);
                    h.f[22] = g.a("status_hit" + f.ai, f.aj, 0, 0);
                    h.f[23] = g.a("status_hitnum" + f.ai, f.aj, 0, 0);
                    h.f[24] = g.a("topic_frame" + f.ai, f.aj, 0, 0);
                }
                catch (Exception exception) {
                    Exception exception2 = exception;
                    exception.printStackTrace();
                }
                i += 9;
                return;
            }
            case 20: {
                for (int i2 = 0; i2 < l.J.length; ++i2) {
                    if (k.w[i2] == null) continue;
                    k.w[i2].b();
                }
                i += 9;
                return;
            }
            case 30: {
                if (t) {
                    h.K();
                } else {
                    h.J();
                }
                i += 9;
                return;
            }
            case 40: {
                i += 9;
                return;
            }
            case 50: {
                h.b((byte)8);
            }
        }
    }

    private void h(Graphics graphics) {
        h.f(graphics);
    }

    public static void e() {
        int n = 0;
        D = 1;
        K = 0;
        L = 0;
        M = 0;
        O = (short)20;
        P = (short)100;
        Q = 0;
        R = 0;
        S = 0;
        n = 300 + 30 * (z - 1);
        D = (short)(200 + 20 * (z - 1));
        short s = G;
        K = (short)(s * 1);
        short s2 = H;
        L = (short)(s2 * 1);
        n += (short)(s2 * 20);
        short s3 = I;
        M = (short)(s3 * 1);
        short s4 = J;
        O = (short)(O + (short)(s4 * 1));
        P = (short)(P + (short)(s4 * 1));
        for (int i2 = 0; i2 < X.length; ++i2) {
            int n2 = X[i2][0];
            if (n2 == -1) continue;
            K = (short)(K + f.aC[n2 * 9 + 1]);
            L = (short)(L + f.aC[n2 * 9 + 2]);
            M = (short)(M + f.aC[n2 * 9 + 3]);
            O = (short)(O + f.aC[n2 * 9 + 4] * 10);
            P = (short)(P + f.aC[n2 * 9 + 5]);
            n += f.aC[n2 * 9 + 6];
            D = (short)(D + f.aC[n2 * 9 + 7]);
            for (int i3 = 1; i3 < 4; ++i3) {
                int n3 = X[i2][i3];
                if (n3 == -1) continue;
                int n4 = f.b(n3);
                if (n4 == 2) {
                    n += f.aA[n3 * 3 + 1];
                    continue;
                }
                if (n4 == 3) {
                    D = (short)(D + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 4) {
                    K = (short)(K + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 5) {
                    L = (short)(L + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 6) {
                    M = (short)(M + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 7) {
                    O = (short)(O + f.aA[n3 * 3 + 1] * 10);
                    continue;
                }
                if (n4 == 8) {
                    P = (short)(P + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 9) {
                    K = (short)(K + f.aA[n3 * 3 + 1]);
                    L = (short)(L + f.aA[n3 * 3 + 1]);
                    M = (short)(M + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 10) {
                    R = (short)(R + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 == 11) {
                    Q = (short)(Q + f.aA[n3 * 3 + 1]);
                    continue;
                }
                if (n4 != 12) continue;
                S = (short)(S + f.aA[n3 * 3 + 1]);
            }
        }
        if (l.T[0] > 0) {
            n += l.R[0 + (l.T[0] - 1)];
        }
        if (l.T[1] > 0) {
            D = (short)(D + l.R[9 + (l.T[1] - 1)]);
        }
        if (l.T[5] > 0) {
            K = (short)(K + l.R[45 + (l.T[5] - 1)]);
        }
        if (l.T[6] > 0) {
            L = (short)(L + l.R[54 + (l.T[6] - 1)]);
        }
        if (l.T[7] > 0) {
            M = (short)(M + (short)(l.R[63 + (l.T[7] - 1)] * 1));
        }
        c c2 = (c)k.k;
        if (c2.f != -1) {
            if (l.T[8] > 0 && c2.e == 8) {
                K = (short)(K + K * l.R[72 + (l.T[8] - 1)] / 100);
            }
            if (l.T[9] > 0 && c2.e == 9) {
                L = (short)(L + L * l.R[81 + (l.T[9] - 1)] / 100);
            }
            if (l.T[10] > 0 && c2.e == 10) {
                M = (short)(M + M * l.R[90 + (l.T[10] - 1)] / 100);
            }
            if (l.T[11] > 0 && c2.e == 11) {
                O = (short)(O + l.R[99 + (l.T[11] - 1)] * 10);
            }
        }
        k.k.R = n;
        if (k.k.Q > n) {
            k.k.Q = n;
        }
        N = (short)h.l(M);
    }

    private static void E() {
        B = 100 + 50 * z * (z - 1);
    }

    private static int l(int n) {
        return n * 10000 * 100 / (n + 100) / 10000;
    }

    private static void m(int n) {
        if ((A += n) >= B) {
            z = (short)(z + 1);
            A = 0;
            h.E();
            h.e();
            k.a(-1, -1, -4, 0, 0, -32752, 0, k.k.s, k.k.t);
            U = (short)(U + 1);
            F = (short)(F + 3);
            k.k.Q = k.k.R;
            C = D;
            int n2 = k.k.s;
            int n3 = k.k.t;
            k.a(-1, -1, 18, 0, 0, -32752, 0, n2, n3);
            k.a(-1, -1, 83, 0, 0, -32752, 0, n2, n3);
            ae = true;
        }
    }

    public static void f() {
        k.k.Q = k.k.R;
        C = D;
    }

    public static void a(Graphics graphics, int n, int n2, int n3) {
        int n4 = V.b(n3);
        int n5 = V.c(n3);
        if (n4 != -1) {
            h.b(graphics, n - 8, n2 - 8, n4);
            if (n5 > 1) {
                h.e(graphics, n + 8, n2 + 8, n5);
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    public static void b(Graphics var0, int var1_1, int var2_2, int var3_3) {
        block6: {
            block8: {
                block16: {
                    block15: {
                        block14: {
                            block13: {
                                block12: {
                                    block11: {
                                        block10: {
                                            block9: {
                                                block7: {
                                                    block5: {
                                                        block4: {
                                                            var0.setClip(var1_1, var2_2, 16, 16);
                                                            var4_4 = f.b(var3_3);
                                                            if (var4_4 != 0 && var4_4 != 1) break block4;
                                                            v0 = var0;
                                                            v1 = h.f[3];
                                                            v2 = var1_1;
                                                            v3 = var3_3;
                                                            ** GOTO lbl95
                                                        }
                                                        if (var4_4 != 13) break block5;
                                                        v0 = var0;
                                                        v1 = h.f[26];
                                                        v4 = var1_1;
                                                        break block6;
                                                    }
                                                    if (var4_4 != 14) break block7;
                                                    v0 = var0;
                                                    v1 = h.f[4];
                                                    v2 = var1_1;
                                                    v5 = (var3_3 - 47) * 16 + 112;
                                                    break block8;
                                                }
                                                if (var4_4 != 15) break block9;
                                                v0 = var0;
                                                v1 = h.f[4];
                                                v2 = var1_1;
                                                v5 = (var3_3 - 48) * 16 + 128;
                                                break block8;
                                            }
                                            if (var4_4 != 16) break block10;
                                            v0 = var0;
                                            v1 = h.f[4];
                                            v2 = var1_1;
                                            v5 = (var3_3 - 49) * 16 + 144;
                                            break block8;
                                        }
                                        if (var4_4 != 17) break block11;
                                        v0 = var0;
                                        v1 = h.f[4];
                                        v2 = var1_1;
                                        v5 = (var3_3 - 50) * 16 + 160;
                                        break block8;
                                    }
                                    if (var4_4 != 18) break block12;
                                    v0 = var0;
                                    v1 = h.f[4];
                                    v2 = var1_1;
                                    v5 = (var3_3 - 51) * 16 + 48;
                                    break block8;
                                }
                                if (var4_4 != 19) break block13;
                                v0 = var0;
                                v1 = h.f[4];
                                v2 = var1_1;
                                v5 = (var3_3 - 52) * 16 + 64;
                                break block8;
                            }
                            if (var4_4 != 20) break block14;
                            v0 = var0;
                            v1 = h.f[4];
                            v2 = var1_1;
                            v5 = (var3_3 - 53) * 16 + 80;
                            break block8;
                        }
                        if (var4_4 != 22) break block15;
                        v0 = var0;
                        v1 = h.f[4];
                        v2 = var1_1;
                        v5 = (var3_3 - 54) * 16 + 96;
                        break block8;
                    }
                    if (var3_3 != 56) break block16;
                    v0 = var0;
                    v1 = h.f[4];
                    v2 = var1_1;
                    v3 = var3_3 - 56;
                    ** GOTO lbl95
                }
                if (var3_3 == 57) {
                    v0 = var0;
                    v1 = h.f[4];
                    v2 = var1_1;
                    v5 = (var3_3 - 57) * 16 + 16;
                } else if (var3_3 == 58) {
                    v0 = var0;
                    v1 = h.f[4];
                    v2 = var1_1;
                    v5 = (var3_3 - 58) * 16 + 32;
                } else {
                    v0 = var0;
                    v1 = h.f[4];
                    v2 = var1_1;
                    v3 = (var3_3 - 8) / 13;
lbl95:
                    // 3 sources

                    v5 = v3 * 16;
                }
            }
            v4 = v2 - v5;
        }
        v0.drawImage(v1, v4, var2_2, 20);
    }

    private static void e(Graphics graphics, int n, int n2, int n3) {
        int n4;
        int n5;
        Image image;
        Graphics graphics2;
        n -= 5;
        n2 -= 7;
        if (n3 < 10) {
            graphics.setClip(n, n2, 5, 7);
            graphics2 = graphics;
            image = f[6];
            n5 = n;
            n4 = n3;
        } else {
            graphics.setClip(n - 5, n2, 5, 7);
            graphics.drawImage(f[6], n - 5 - n3 / 10 * 5, n2, 20);
            graphics.setClip(n, n2, 5, 7);
            graphics2 = graphics;
            image = f[6];
            n5 = n;
            n4 = n3 % 10;
        }
        graphics2.drawImage(image, n5 - n4 * 5, n2, 20);
    }

    public static void b(int n) {
        boolean bl;
        int n2 = V.b(n);
        if (n2 != -1 && (bl = h.n(n2))) {
            V.f(n);
        }
        h.N();
    }

    private static boolean n(int n) {
        if (f.b(n) == 0) {
            short s = f.aA[n * 3 + 1];
            if (s != 0) {
                k.k.b(s);
            }
        } else if (f.b(n) == 1) {
            short s = f.aA[n * 3 + 1];
            if (s != 0 && (C = (short)(C + s)) > D) {
                C = D;
            }
        } else {
            return false;
        }
        return true;
    }

    private static void F() {
        for (int i2 = 0; i2 < W.length; ++i2) {
            h.W[i2][0] = -1;
            h.W[i2][1] = -1;
            h.W[i2][2] = -1;
            h.W[i2][3] = -1;
        }
    }

    public static boolean c(int n) {
        for (int i2 = 0; i2 < l.a; ++i2) {
            if (W[i2][0] != -1) continue;
            h.W[i2][0] = n;
            h.W[i2][1] = -1;
            h.W[i2][2] = -1;
            h.W[i2][3] = -1;
            return true;
        }
        return false;
    }

    public static void g() {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < W.length; ++i2) {
            for (int i3 = 0; i3 < W.length - i2 - 1; ++i3) {
                int n5 = W[i3][0];
                int n6 = W[i3 + 1][0];
                if (n5 == -1) {
                    n5 = Integer.MAX_VALUE;
                }
                if (n6 == -1) {
                    n6 = Integer.MAX_VALUE;
                }
                if (n5 <= n6) continue;
                n = W[i3][0];
                n2 = W[i3][1];
                n3 = W[i3][2];
                n4 = W[i3][3];
                h.W[i3][0] = W[i3 + 1][0];
                h.W[i3][1] = W[i3 + 1][1];
                h.W[i3][2] = W[i3 + 1][2];
                h.W[i3][3] = W[i3 + 1][3];
                h.W[i3 + 1][0] = n;
                h.W[i3 + 1][1] = n2;
                h.W[i3 + 1][2] = n3;
                h.W[i3 + 1][3] = n4;
            }
        }
    }

    public static void a(Graphics graphics, int n, int n2, int[] nArray) {
        if (nArray[0] == -1) {
            return;
        }
        h.c(graphics, n, n2, nArray[0]);
        n -= 8;
        n2 -= 8;
        int n3 = f.aC[nArray[0] * 9 + 8];
        if (n3 > 0) {
            for (int i2 = 0; i2 < n3; ++i2) {
                int n4;
                Graphics graphics2;
                graphics.setColor(0xFFFFFF);
                graphics.fillRect(n + 16 - (n3 - i2) * 5, n2 + 16 - 5, 4, 4);
                if (nArray[1 + i2] >= 0) {
                    graphics2 = graphics;
                    n4 = 0xFF0000;
                } else {
                    graphics2 = graphics;
                    n4 = 0;
                }
                graphics2.setColor(n4);
                graphics.fillRect(n + 16 - (n3 - i2) * 5 + 1, n2 + 16 - 5 + 1, 2, 2);
            }
        }
    }

    public static void c(Graphics graphics, int n, int n2, int n3) {
        Image image;
        int n4;
        block8: {
            int n5;
            Image[] imageArray;
            block4: {
                block7: {
                    block6: {
                        block5: {
                            block3: {
                                n4 = f.c(n3);
                                image = null;
                                if (n4 != 0) break block3;
                                imageArray = f;
                                n5 = 12;
                                break block4;
                            }
                            if (n4 != 1) break block5;
                            imageArray = f;
                            n5 = 13;
                            break block4;
                        }
                        if (n4 != 2) break block6;
                        imageArray = f;
                        n5 = 14;
                        break block4;
                    }
                    if (n4 != 3) break block7;
                    imageArray = f;
                    n5 = 15;
                    break block4;
                }
                if (n4 != 4) break block8;
                imageArray = f;
                n5 = 16;
            }
            image = imageArray[n5];
        }
        graphics.setClip(n -= 8, n2 -= 8, 16, 16);
        int n6 = 0;
        for (int i2 = 0; i2 < f.aC.length; i2 += 9) {
            if (n4 != f.aC[i2]) continue;
            n6 = i2 / 9;
            break;
        }
        graphics.drawImage(image, n - (n3 - n6) / 3 * 16, n2, 20);
    }

    private static void G() {
        for (int i2 = 0; i2 < X.length; ++i2) {
            h.X[i2][0] = -1;
            h.X[i2][1] = -1;
            h.X[i2][2] = -1;
            h.X[i2][3] = -1;
        }
    }

    public static void d(int n) {
        int n2 = W[n][0];
        if (n2 != -1) {
            int n3 = f.c(n2);
            if (X[n3][0] == -1) {
                h.X[n3][0] = n2;
                h.X[n3][1] = W[n][1];
                h.X[n3][2] = W[n][2];
                h.X[n3][3] = W[n][3];
                h.W[n][0] = -1;
                h.W[n][1] = -1;
                h.W[n][2] = -1;
                h.W[n][3] = -1;
            } else {
                int n4 = X[n3][0];
                int n5 = X[n3][1];
                int n6 = X[n3][2];
                int n7 = X[n3][3];
                h.X[n3][0] = n2;
                h.X[n3][1] = W[n][1];
                h.X[n3][2] = W[n][2];
                h.X[n3][3] = W[n][3];
                h.W[n][0] = -1;
                h.W[n][1] = -1;
                h.W[n][2] = -1;
                h.W[n][3] = -1;
                for (int i2 = 0; i2 < W.length; ++i2) {
                    if (W[i2][0] != -1) continue;
                    h.W[i2][0] = n4;
                    h.W[i2][1] = n5;
                    h.W[i2][2] = n6;
                    h.W[i2][3] = n7;
                    break;
                }
            }
        }
        h.e();
        h.H();
    }

    public static boolean e(int n) {
        for (int i2 = 0; i2 < W.length; ++i2) {
            if (W[i2][0] != -1) continue;
            h.W[i2][0] = X[n][0];
            h.W[i2][1] = X[n][1];
            h.W[i2][2] = X[n][2];
            h.W[i2][3] = X[n][3];
            h.X[n][0] = -1;
            h.X[n][1] = -1;
            h.X[n][2] = -1;
            h.X[n][3] = -1;
            h.e();
            return true;
        }
        return false;
    }

    private static void H() {
        Image image;
        if (X[0][0] >= 0 && (image = g.a("s" + X[0][0] / 3 + f.ai, f.aj, 0, 0)) != null) {
            a.a("s0", image);
        }
    }

    private static void I() {
        byte[][] byArrayArray = new byte[][]{{1, 10, 5, 2}, {0, 6, 1, 2, 6, 1, 5, 2}, {0, 6, 1, 2, 5, 2, 6, 2, 8, 1, 15, 1}, {0, 6, 1, 1, 5, 2, 6, 2, 8, 1, 15, 2}, {0, 6, 1, 1, 5, 2, 6, 2, 8, 1, 15, 4}, {0, 2, 1, 2, 5, 2, 6, 2, 8, 1, 15, 3}, {0, 6, 1, 2, 5, 2, 6, 2, 8, 1, 15, 3}, {0, 6, 1, 2, 5, 2, 6, 2, 8, 1, 15, 3, 18, 2}, {0, 6, 1, 2, 5, 2, 6, 2, 8, 1, 15, 3, 18, 3}, {37, 15, 18, 4}, {18, 5, 1, 3, 5, 2, 6, 2, 37, 3}, {0, 6, 1, 2, 5, 2, 6, 2, 15, 3, 18, 3, 37, 15}, {9, 3, 0, 6, 5, 2, 6, 2, 15, 3, 18, 3}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 1, 15, 3, 18, 3, 37, 15}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 15, 3, 18, 3, 12, 0}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 14, 4, 37, 15}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 37, 15, 19, 2}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 37, 15, 14, 4}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 37, 15, 24, 2}, {0, 6, 1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 37, 15, 25, 3}, {1, 6, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 37, 15, 33, 1}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 37, 15, 34, 3}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 4}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 5, 37, 15}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 6}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 7, 37, 15}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 9}, {0, 7, 1, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8}, {0, 7, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 3, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8, 35, 2}, {0, 7, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 6, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8}, {0, 7, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 7, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8}, {0, 7, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 8, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8, 37, 13}, {37, 8}, {0, 7, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 8, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8, 37, 9}, {0, 7, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 8, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 1, 34, 8, 37, 10}, {0, 8, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 8, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 2, 34, 8}, {0, 9, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 1}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 2}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 3}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 4}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 5}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 6}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 7}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 8}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 9}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 10}, {0, 10, 1, 9, 5, 2, 6, 2, 9, 3, 12, 0, 15, 3, 18, 9, 19, 2, 14, 4, 24, 2, 25, 3, 26, 3, 33, 3, 34, 8, 37, 14, 43, 1}};
        j.aZ = byArrayArray;
    }

    private static void J() {
        z = 1;
        E = 0;
        h.E();
        A = 0;
        U = 0;
        F = 0;
        G = (short)10;
        H = (short)8;
        I = (short)3;
        J = 0;
        V.a(l.b);
        V.c();
        V.e(0);
        V.e(0);
        V.e(0);
        V.e(4);
        V.e(4);
        V.e(47);
        h.G();
        h.F();
        h.c(0);
        h.d(0);
        for (int i2 = 0; i2 < l.T.length; ++i2) {
            l.T[i2] = 0;
        }
        l.T[12] = 1;
        l.T[13] = 1;
        l.T[14] = 1;
        l.T[15] = 1;
        h.L();
        h.M();
        h.a(0, 12);
        h.a(1, 13);
        h.a(2, 14);
        h.a(3, 15);
        h.b(0, 0);
        h.b(1, 4);
        h.e();
        h.H();
        k.k.b(k.k.R);
        C = D;
        k.k.a(0, false);
        j.aY = 0;
        k.c = 18;
        k.d = 20;
        k.a = 34;
        k.b = 10;
        k.g = 0;
        k.h = 0;
        k.i = 0;
        k.a(k.a, k.b, k.c, k.d, k.g, k.h, true, k.i);
    }

    private static void K() {
        k.k.a(0, false);
        k.a(k.k, -1, -1, true);
        h.H();
        if (k.i >= 0 && k.j != j.aY) {
            k.i = -1;
        }
        if (ao == 0) {
            k.a(k.a, k.b, k.c, k.d, k.g, k.h, true, k.i);
        } else if (ao != 1 && ao != 3 && ao != 4 && ao != 2 && ao == 5) {
            j.aY = 37;
            k.c = 15;
            k.d = 17;
            k.a = 0;
            k.b = 9;
            k.g = 0;
            k.h = 0;
            k.a(k.a, k.b, k.c, k.d, k.g, k.h, true, k.i);
            ao = 0;
            h.j();
            h.ag();
        }
        d.a(d.i);
    }

    private static void L() {
        for (int i2 = 0; i2 < Y.length; ++i2) {
            h.Y[i2] = -1;
            h.Z[i2] = -1;
        }
    }

    public static void a(int n, int n2) {
        for (int i2 = 0; i2 < Y.length; ++i2) {
            if (Y[i2] != n2) continue;
            h.Y[i2] = -1;
            h.Z[i2] = -1;
        }
        h.Y[n] = n2;
        h.Z[n] = -1;
    }

    private static void o(int n) {
        byte by;
        int n2;
        block33: {
            int n3;
            block32: {
                block31: {
                    aa = n;
                    n2 = Y[n];
                    if (n2 == -1) {
                        return;
                    }
                    by = l.S[n2 * 4 + 2];
                    if (Z[n] <= 0) break block31;
                    n3 = 5;
                    break block32;
                }
                if (C >= by) break block33;
                n3 = 6;
            }
            h.a((byte)n3, 0);
        }
        if (Z[n] == -1 && C >= by) {
            if (n2 == 4) {
                boolean bl = false;
                if (l.T[4] > 0) {
                    k.k.b(l.R[36 + (l.T[4] - 1)]);
                }
                int n4 = k.k.s;
                int n5 = k.k.t + 1;
                k.a(-1, -1, 92, 0, 0, -32752, 0, n4, n5);
            } else if (n2 == 8 || n2 == 9 || n2 == 10 || n2 == 11) {
                if (k.k.U == 0) {
                    k.k.a(48, false);
                }
                c c2 = (c)k.k;
                ((c)k.k).e = n2;
                c2.f = 0;
                h.e();
            } else if (n2 == 12) {
                if (k.k.U == 0) {
                    k.k.a(15, false);
                }
            } else if (n2 == 13) {
                if (k.k.U == 0) {
                    k.k.a(18, false);
                }
            } else if (n2 == 14) {
                if (k.k.U == 0) {
                    k.k.a(27, false);
                }
            } else if (n2 == 15) {
                if (k.k.U == 0) {
                    k.k.a(33, false);
                }
            } else if (n2 == 16) {
                if (k.k.U == 0) {
                    k.k.a(36, false);
                }
            } else if (n2 == 21) {
                if (k.k.U == 0) {
                    k.k.a(24, false);
                }
            } else if (n2 == 20 && k.k.U == 0) {
                System.out.println("ffffff");
                k.k.a(21, false);
                C = (short)(C - by);
            }
            h.Z[n] = 0;
            C = (short)(C - by);
        }
    }

    private static void M() {
        for (int i2 = 0; i2 < ab.length; ++i2) {
            h.ab[i2][0] = -1;
            h.ab[i2][1] = 0;
        }
    }

    public static void b(int n, int n2) {
        for (int i2 = 0; i2 < ab.length; ++i2) {
            if (ab[i2][0] != n2) continue;
            h.ab[i2][0] = -1;
            h.ab[i2][1] = 0;
        }
        h.ab[n][0] = n2;
        h.N();
    }

    private static void N() {
        for (int i2 = 0; i2 < ab.length; ++i2) {
            int n;
            int n2;
            int[] nArray;
            if (ab[i2][0] == -1) continue;
            int n3 = V.d(ab[i2][0]);
            if (n3 == -1) {
                h.ab[i2][0] = -1;
                nArray = ab[i2];
                n2 = 1;
                n = 0;
            } else {
                nArray = ab[i2];
                n2 = 1;
                n = V.c(n3);
            }
            nArray[n2] = n;
        }
    }

    private static void p(int n) {
        int n2 = V.d(ab[n][0]);
        if (n2 != -1) {
            h.b(n2);
        }
    }

    private static void O() {
        ac = 0;
        k.k.a(f[7], -1728053248);
    }

    private static void P() {
        for (int i2 = 0; i2 < k.w.length; ++i2) {
            if (k.w[i2] == null) continue;
            k.w[i2].c();
        }
        k.b();
        h.f[1] = null;
        h.f[2] = null;
        h.f[3] = null;
        h.f[10] = null;
        h.f[12] = null;
    }

    public static void c(byte by) {
        ac = by;
    }

    private void Q() {
        block16: {
            int n;
            i i2;
            block18: {
                block17: {
                    if (ac != 0) {
                        boolean bl = ad.a();
                        if (bl) {
                            h.c((byte)0);
                        }
                        return;
                    }
                    try {
                        k.f();
                        this.V();
                        k.a();
                        h.X();
                    }
                    catch (Exception exception) {
                        Exception exception2 = exception;
                        exception.printStackTrace();
                    }
                    if (!k.h()) break block16;
                    if (j.aX % 50 == 0) {
                        short s;
                        short s2;
                        int n2;
                        m m2;
                        if (l.T[2] > 0) {
                            boolean bl = false;
                            m2 = k.k;
                            n2 = l.R[18 + (l.T[2] - 1)];
                        } else {
                            m2 = k.k;
                            n2 = 1;
                        }
                        m2.b(n2);
                        if (l.T[3] > 0) {
                            s2 = C;
                            s = l.R[27 + (l.T[3] - 1)];
                        } else {
                            s2 = C;
                            s = 1;
                        }
                        C = (short)(s2 + s);
                        if (C > D) {
                            C = D;
                        }
                    }
                    if (k.k.U == 39) break block16;
                    if (!j.i(65536) || d.a() != d.f) break block17;
                    h.c((byte)1);
                    i2 = ad;
                    n = 0;
                    break block18;
                }
                if (j.i(16384)) {
                    h.p(0);
                    return;
                }
                if (j.i(32768)) {
                    h.p(1);
                    return;
                }
                if (j.i(16)) {
                    E += 10000;
                    return;
                }
                if (!j.i(131072) || d.a() != d.f) break block16;
                h.c((byte)1);
                i2 = ad;
                n = 14;
            }
            i2.a((byte)n);
        }
    }

    private void i(Graphics graphics) {
        int n;
        int n2;
        Graphics graphics2;
        if (ac != 0) {
            k.a(graphics);
            ad.a(graphics);
            h.a(graphics, 0, 1, l.B);
            return;
        }
        k.a(graphics);
        this.k(graphics);
        h.d(graphics);
        if (!k.g() && d.a() == d.f) {
            graphics2 = graphics;
            n2 = 2;
            n = 3;
        } else {
            graphics2 = graphics;
            n2 = -1;
            n = -1;
        }
        h.a(graphics2, n2, n, false);
        if (ae) {
            graphics.setClip(0, 0, f.ad, f.ae);
            graphics.setColor(0xFFFFFF);
            graphics.fillRect(0, 0, f.ad, f.ae);
            ae = false;
        }
    }

    private static void R() {
        af = k.b(0);
    }

    private static void b(Graphics graphics, int n, int n2) {
        if (af != null) {
            graphics.setClip(0, 0, f.ad, f.ae);
            int n3 = af.getWidth();
            int n4 = af.getHeight();
            int n5 = n + 2;
            int n6 = n2 + 2;
            graphics.drawImage(af, n5, n6, 20);
            graphics.setColor(0);
            graphics.drawRect(n, n2, n3 + 3, n4 + 3);
            graphics.setColor(0xFFFFFF);
            graphics.drawRect(n + 1, n2 + 1, n3 + 1, n4 + 1);
            graphics.setColor(0xFF0000);
            graphics.fillRect(n5 + k.k.s / f.af, n6 + k.k.t / f.af, 2, 2);
        }
    }

    private static void S() {
        k.k.n = true;
        k.k.T = 0;
        k.k.S = k.w[k.k.T];
        k.k.o = true;
        k.k.c(0);
        k.k.a(0, true);
        k.k.k = (short)14;
    }

    private static byte T() {
        if (j.g(1)) {
            return 0;
        }
        if (j.g(2)) {
            return 4;
        }
        if (j.g(4)) {
            return 6;
        }
        if (j.g(8)) {
            return 2;
        }
        return -1;
    }

    private static byte U() {
        if (j.h(1)) {
            return 0;
        }
        if (j.h(2)) {
            return 4;
        }
        if (j.h(4)) {
            return 6;
        }
        if (j.h(8)) {
            return 2;
        }
        return -1;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void V() {
        block85: {
            block86: {
                int n;
                int n2;
                block88: {
                    int n3;
                    block91: {
                        block90: {
                            block89: {
                                block87: {
                                    block79: {
                                        int n4;
                                        int n5;
                                        block81: {
                                            int n6;
                                            block84: {
                                                block83: {
                                                    block82: {
                                                        block80: {
                                                            block73: {
                                                                c c2;
                                                                int n7;
                                                                int n8;
                                                                block78: {
                                                                    int n9;
                                                                    block75: {
                                                                        block77: {
                                                                            block76: {
                                                                                block74: {
                                                                                    if (!k.h()) {
                                                                                        return;
                                                                                    }
                                                                                    byte by = h.T();
                                                                                    if (k.k.U == 0) {
                                                                                        if (by != -1) {
                                                                                            k.k.c(by);
                                                                                            k.k.a(3, false);
                                                                                            k.k.c(2048);
                                                                                            return;
                                                                                        }
                                                                                        if (j.g(0x100000)) {
                                                                                            k.k.a(6, false);
                                                                                            return;
                                                                                        }
                                                                                        if (j.i(32)) {
                                                                                            h.o(0);
                                                                                            return;
                                                                                        }
                                                                                        if (j.i(128)) {
                                                                                            h.o(1);
                                                                                            return;
                                                                                        }
                                                                                        if (j.i(2048)) {
                                                                                            h.o(2);
                                                                                            return;
                                                                                        }
                                                                                        if (!j.i(8192)) return;
                                                                                        h.o(3);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 3) {
                                                                                        if (h.U() != -1) {
                                                                                            k.k.c(3072);
                                                                                            k.k.a(54, false);
                                                                                        } else if (by == -1) {
                                                                                            k.k.c(0);
                                                                                            k.k.a(0, false);
                                                                                        } else if (this.ag != by) {
                                                                                            k.k.c(by);
                                                                                            k.k.c(2048);
                                                                                            this.ag = by;
                                                                                        } else {
                                                                                            k.k.c(2048);
                                                                                        }
                                                                                        if (k.a(0, k.k.s / f.af, k.k.t / f.af) != 13 || j.aX % 2 != 0) return;
                                                                                        c c3 = (c)k.a(-1, -1, 86, 0, k.k.i(), 16, 0, k.k.s, k.k.t);
                                                                                        if (c3 == null) return;
                                                                                        c3.K = -10;
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 54) {
                                                                                        if (!k.k.X) return;
                                                                                        k.k.c(2048);
                                                                                        k.k.a(3, false);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 6) {
                                                                                        if (k.k.X) {
                                                                                            if (j.g(0x100000)) {
                                                                                                k.k.a(9, false);
                                                                                                return;
                                                                                            }
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V == 2 && k.k.W == 0) {
                                                                                            h.q(k.k.U);
                                                                                        }
                                                                                        k.k.c(0);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 9) {
                                                                                        if (k.k.X) {
                                                                                            if (j.g(0x100000)) {
                                                                                                k.k.a(12, false);
                                                                                                return;
                                                                                            }
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V == 2 && k.k.W == 0) {
                                                                                            h.q(k.k.U);
                                                                                        }
                                                                                        k.k.c(0);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 12) {
                                                                                        if (k.k.X) {
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V == 4 && k.k.W == 0) {
                                                                                            h.q(k.k.U);
                                                                                        }
                                                                                        k.k.c(0);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 15) {
                                                                                        if (k.k.X) {
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V != 2 || k.k.W != 2) return;
                                                                                        c c4 = (c)k.a(-1, -1, -8, -1, k.k.i(), 8, 0, k.k.s, k.k.t);
                                                                                        if (c4 == null) return;
                                                                                        c4.c(4096);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 18) {
                                                                                        if (k.k.X) {
                                                                                            k.k.c(0);
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V != 4) return;
                                                                                        boolean bl = h.q(k.k.U);
                                                                                        if (!bl) return;
                                                                                        k.k.a(51, false);
                                                                                        k.a(-1, -1, 93, 0, k.k.i(), -32752, 0, k.k.s, k.k.t);
                                                                                        k.b(true);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U == 51) {
                                                                                        if (k.k.X) {
                                                                                            k.b(false);
                                                                                            k.k.c(0);
                                                                                            k.k.a(0, false);
                                                                                            return;
                                                                                        }
                                                                                        if (k.k.V != 1 && k.k.V != 3 && k.k.V != 5 && k.k.V != 7 && k.k.V != 9) return;
                                                                                        h.q(k.k.U);
                                                                                        return;
                                                                                    }
                                                                                    if (k.k.U != 21) break block73;
                                                                                    n8 = 0;
                                                                                    if (aa != 0) break block74;
                                                                                    n9 = 32;
                                                                                    break block75;
                                                                                }
                                                                                if (aa != 1) break block76;
                                                                                n9 = 128;
                                                                                break block75;
                                                                            }
                                                                            if (aa != 2) break block77;
                                                                            n9 = 2048;
                                                                            break block75;
                                                                        }
                                                                        if (aa != 3) break block78;
                                                                        n9 = 8192;
                                                                    }
                                                                    n8 = n9;
                                                                }
                                                                byte by = l.S[82];
                                                                if ((k.k.V & 1) != 0) return;
                                                                k.a(true);
                                                                for (n7 = 1; n7 < 48; ++n7) {
                                                                    if (!k.v[n7].n || !(c2 = (c)k.v[n7]).e() || !k.k.b(k.v[n7], 42)) continue;
                                                                    c2.a(k.k);
                                                                    h.a((c)k.v[n7], 21);
                                                                }
                                                                if (!j.g(n8) || C < by) {
                                                                    k.a(-1, -1, 65, 0, k.k.i(), 0, 0, k.k.s, k.k.t);
                                                                    k.k.a(0, false);
                                                                    for (n7 = 1; n7 < 48; ++n7) {
                                                                        if (!k.v[n7].n || !(c2 = (c)k.v[n7]).e() || !k.k.b(k.v[n7], 50)) continue;
                                                                        c2.a(k.k);
                                                                        h.a((c)k.v[n7], 12);
                                                                    }
                                                                    k.a(false);
                                                                    return;
                                                                }
                                                                C = (short)(C - by);
                                                                return;
                                                            }
                                                            if (k.k.U != 27) break block79;
                                                            if (k.k.X) {
                                                                k.k.c(0);
                                                                k.k.a(0, false);
                                                                return;
                                                            }
                                                            if (k.k.V == 1) {
                                                                if (k.k.W != 0) return;
                                                                k.a(-1, -1, 95, 0, k.k.i(), -32752, 0, k.k.s, k.k.t);
                                                                k.a(-1, -1, 93, 0, k.k.i(), -32752, 0, k.k.s, k.k.t);
                                                                k.b(true);
                                                                return;
                                                            }
                                                            if (k.k.V != 2 || k.k.W != 1) return;
                                                            k.b(false);
                                                            n5 = 0;
                                                            n4 = 0;
                                                            if (k.k.i() != 0) break block80;
                                                            n4 = -60;
                                                            n5 = -1;
                                                            break block81;
                                                        }
                                                        if (k.k.i() != 4) break block82;
                                                        n4 = 40;
                                                        n5 = -1;
                                                        break block81;
                                                    }
                                                    if (k.k.i() != 6) break block83;
                                                    n6 = -50;
                                                    break block84;
                                                }
                                                if (k.k.i() != 2) break block81;
                                                n6 = 50;
                                            }
                                            n5 = n6;
                                            n4 = -1;
                                        }
                                        k.a(-1, -1, 37, 0, k.k.i(), 0, 0, k.k.s + n5, k.k.t + n4);
                                        Object var4_18 = null;
                                        k.a(10, true);
                                        return;
                                    }
                                    if (k.k.U == 33) {
                                        if (k.k.X) {
                                            k.a(false);
                                            k.k.c(0);
                                            k.k.a(0, false);
                                            return;
                                        }
                                        if (k.k.V == 2) {
                                            if (k.k.W != 0) return;
                                            k.a(-1, -1, 38, 0, k.k.i(), 0, 0, k.k.s, k.k.t + 4);
                                            Object var2_7 = null;
                                            k.a(true);
                                            return;
                                        }
                                        if (k.k.V != 3 || k.k.W != 0) return;
                                        boolean bl = false;
                                        for (int i2 = 1; i2 < 48; ++i2) {
                                            c c5;
                                            if (!k.v[i2].n || !(c5 = (c)k.v[i2]).d() || !k.k.b(k.v[i2], 60)) continue;
                                            c5.a(k.k);
                                            h.a(c5, 33);
                                            k.a(-1, -1, 38, 0, k.k.i(), 0, 0, k.v[i2].s, k.v[i2].t + 4);
                                        }
                                        return;
                                    }
                                    if (k.k.U != 36) break block85;
                                    if (k.k.X) {
                                        k.k.c(0);
                                        k.k.a(0, false);
                                        return;
                                    }
                                    if (k.k.V != 1 || k.k.W != 0) break block86;
                                    n2 = 0;
                                    n = 0;
                                    if (k.k.i() != 0) break block87;
                                    n = -20;
                                    n2 = -1;
                                    break block88;
                                }
                                if (k.k.i() != 4) break block89;
                                n = 20;
                                n2 = -1;
                                break block88;
                            }
                            if (k.k.i() != 6) break block90;
                            n3 = -20;
                            break block91;
                        }
                        if (k.k.i() != 2) break block88;
                        n3 = 20;
                    }
                    n2 = n3;
                    n = -1;
                }
                k.a(-1, -1, 34, 0, k.k.i(), 0, 0, k.k.s + n2, k.k.t + n);
                Object var4_20 = null;
            }
            h.q(k.k.U);
            return;
        }
        if (k.k.U == 24) {
            int n;
            if (k.k.X) {
                k.a(false);
                k.k.a(0, false);
                return;
            }
            if (k.k.V == 1) {
                if (k.k.W == 0) {
                    k.a(-1, -1, 93, 0, k.k.i(), -32752, 0, k.k.s, k.k.t);
                    k.b(true);
                }
                if (k.k.W != 3) return;
                k.a(-1, -1, 40, 0, k.k.i(), 0, 0, k.k.s, k.k.t);
                return;
            }
            if (k.k.V == 2) {
                k.b(false);
                k.a(-1, -1, 40, 1, k.k.i(), 0, 0, k.k.s, k.k.t);
                k.a(true);
                return;
            }
            if (k.k.V < 3) return;
            if ((k.k.V & 1) == 0) {
                n = d.a(k.c() + 10, k.c() + (f.al - 90));
                int n10 = d.a(k.d() + 30, k.d() + (f.am - 60));
                k.a(-1, -1, 41, 0, k.k.i(), 0, 0, n, n10);
                Object var4_21 = null;
            }
            for (n = 1; n < 48; ++n) {
                c c6;
                if (!k.v[n].n || !(c6 = (c)k.v[n]).d()) continue;
                c6.a(k.k);
                h.a(c6, 24);
            }
            return;
        }
        if (k.k.U == 30) {
            if (k.k.X) {
                k.k.c(0);
                k.k.a(0, false);
                return;
            }
            if (k.k.V != 0) return;
            if (k.e()) {
                k.b(false);
            }
            k.a(false);
            if (k.k.W == 1) {
                k.k.d(768);
                return;
            }
            if (k.k.W == 2) {
                k.k.d(512);
                return;
            }
            if (k.k.W == 3) {
                k.k.d(256);
                return;
            }
            k.k.c(0);
            return;
        }
        if (k.k.U == 39) {
            if (k.k.X) {
                k.k.c(0);
                k.k.r();
                h.b(false);
                if (d.a() != d.g && d.a() != d.h) {
                    d.a(d.g);
                }
                if (d.a() != d.h) return;
                h.b((byte)9);
                return;
            }
            if (k.k.V == 0) {
                return;
            }
            if (k.k.V == 1 && k.k.W == 0) {
                h.f(6);
            }
            k.k.c(0);
            return;
        }
        if (k.k.U != 48) return;
        if (k.k.X) {
            k.k.a(0, false);
            return;
        }
        if (k.k.V != 2 || k.k.W != 0) return;
        ae = true;
    }

    private static boolean q(int n) {
        int n2 = 32;
        int n3 = 40;
        if (n == 36) {
            n2 = 80;
            n3 = 80;
        }
        boolean bl = false;
        for (int i2 = 1; i2 < 48; ++i2) {
            c c2;
            if (!k.v[i2].n || !(c2 = (c)k.v[i2]).e() || !k.k.a(k.v[i2], n2, 20, n3, 20) || !k.k.a(k.v[i2], n2, 20, n3, 20)) continue;
            k.v[i2].c((byte)(k.k.i() + 4 & 7));
            h.a((c)k.v[i2], n);
            bl = true;
        }
        return bl;
    }

    public static void a(c c2, int n) {
        int n2;
        if (!c2.d()) {
            if (c2.U == 0) {
                int n3;
                c2.a(1, false);
                int n4 = d.a(0, 1000);
                if (n4 < 200 && (n3 = d.a(0, 8)) != -1) {
                    k.a(-1, -1, -6, n3, 0, 0, 0, c2.s, c2.t);
                }
            }
            return;
        }
        if (c2.Q <= 0) {
            return;
        }
        k.a(-1, -1, 20, 0, 0, -32752, 0, c2.s, c2.t);
        c2.k = (short)(c2.k & 0xFFFFFF7F);
        c2.c(0);
        if (c2.F != 9) {
            int n5;
            c c3;
            if (n == 6) {
                c2.e((byte)5);
                c3 = c2;
                n5 = 6;
            } else if (n == 9) {
                c2.e((byte)6);
                c3 = c2;
                n5 = 7;
            } else if (n == 21) {
                c2.e((byte)5);
                c3 = c2;
                n5 = 6;
            } else {
                if (n == 27) {
                    System.out.println("bingshui");
                    c2.e((byte)9);
                    c2.h = 0;
                    c2.i = 0;
                    return;
                }
                c2.e((byte)7);
                c3 = c2;
                n5 = 8;
            }
            c3.a(n5);
        }
        int n6 = 0;
        byte by = 0;
        boolean bl = false;
        if (c2.d()) {
            by = c2.G;
            if (d.a(1, 101) <= O / 10) {
                bl = true;
            }
            n6 = h.r(by);
            if (n == 15) {
                n2 = 0;
                if (l.T[12] > 0) {
                    n6 = n6 * l.R[108 + (l.T[12] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 18) {
                n2 = 0;
                if (l.T[13] > 0) {
                    n6 = n6 * l.R[117 + (l.T[13] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 27) {
                n2 = 0;
                if (l.T[14] > 0) {
                    n6 = n6 * l.R[126 + (l.T[14] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 33) {
                n2 = 0;
                if (l.T[15] > 0) {
                    n6 = n6 * l.R[135 + (l.T[15] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 24) {
                n2 = 0;
                if (l.T[21] > 0) {
                    n6 = n6 * l.R[189 + (l.T[21] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 36) {
                n2 = 0;
                if (l.T[16] > 0) {
                    n6 = n6 * l.R[144 + (l.T[16] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 21) {
                n2 = 0;
                if (l.T[20] > 0) {
                    n6 = n6 * l.R[180 + (l.T[20] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            } else if (n == 42) {
                n2 = 0;
                if (l.T[20] > 0) {
                    n6 = n6 * l.R[180 + (l.T[20] - 1)] / 100;
                }
                if (n6 <= 0) {
                    n6 = d.a(0, 2);
                }
            }
            if (bl) {
                n6 += n6 * P / 100;
            }
            if (bl) {
                k.a(-1, -1, 70, 0, 0, -32752, 0, c2.s, c2.t);
                k.a(8, false);
            }
        }
        if (n == 100) {
            n6 = 99999;
        }
        c2.b(-n6);
        n2 = n6 * S / 100;
        k.k.b(n2);
        h.a(1, false);
        an = 0;
        am = (byte)(am + 1);
        c2.d = 0;
        c c4 = (c)k.a(-1, -1, -2, n6, 0, -32752, 0, c2.s, c2.t);
        if (c4 != null) {
            c4.b = bl;
        }
        if (c2.Q <= 0 && n != 12) {
            if (c2.L != null) {
                c2.L.g();
                c2.L = null;
            }
            h.a(c2);
        }
    }

    public static void a(c c2) {
        byte by = c2.G;
        short s = f.ax[by * 10 + 5];
        int n = 20 + 10 * s;
        int n2 = z - s;
        if (n2 > 0) {
            if (n2 > 6) {
                n2 = 6;
            }
            n /= n2;
        }
        n += n * Q / 100;
        h.m(n * T);
        int n3 = d.a(0, 1000);
        if (n3 > 900 - R * 10) {
            short s2 = f.ax[by * 10 + 9];
            if (s2 != -1) {
                int n4;
                int n5 = 0;
                int n6 = d.a(0, 1000);
                if (n6 < 700) {
                    n4 = s2 * 3;
                } else {
                    int n7;
                    int n8;
                    if (n6 < 990) {
                        n8 = s2 * 3;
                        n7 = 1;
                    } else {
                        n8 = s2 * 3;
                        n7 = 2;
                    }
                    n4 = n8 + n7;
                }
                n5 = n4;
                k.a(-1, -1, -7, n5, 0, 0, 0, c2.s, c2.t);
            }
        } else if (n3 > 800 - R * 10) {
            short s3 = f.ax[by * 10 + 8];
            if (s3 != -1) {
                k.a(-1, -1, -6, s3, 0, 0, 0, c2.s, c2.t);
            }
        } else if (n3 > 100 - R * 10) {
            int n9 = f.ax[by * 10 + 7];
            n9 = d.a(n9 >> 1, n9);
            h.a((byte)0, n9);
            E += n9;
        }
        k.a(-1, -1, 21, 0, 0, 0, 0, c2.s, c2.t);
        c2.k = (short)(c2.k | 0x40);
        c2.k = (short)(c2.k & 0xFFFFFFFD);
        k.a(c2);
    }

    private static int r(int n) {
        int n2 = 0;
        short s = 0;
        s = f.ax[n * 10 + 2];
        short s2 = K;
        short s3 = (short)(s * 10000 * 100 / (s + 100) / 10000);
        n2 = (s2 * 100 - s2 * s3) / 100;
        boolean bl = d.a(0, 2) == 0;
        int n3 = n2 >> 3;
        if (n3 <= 1) {
            n3 = 2;
        }
        int n4 = d.a(0, n3);
        n2 = n2 <= 0 ? d.a(0, 2) : (bl ? n2 + n4 : n2 - n4);
        return n2;
    }

    private static int s(int n) {
        short s = 0;
        short s2 = 0;
        int n2 = 0;
        s = f.ax[n * 10 + 1];
        s2 = f.ax[n * 10 + 4];
        short s3 = L;
        short s4 = (short)(s3 * 10000 * 100 / (s3 + 100) / 10000);
        n2 = (s * 100 - s * s4) / 100;
        boolean bl = d.a(0, 2) == 0;
        int n3 = n2 >> 3;
        if (n3 <= 1) {
            n3 = 2;
        }
        int n4 = d.a(0, n3);
        int n5 = n2 <= 0 ? d.a(0, 2) : (n2 = bl ? n2 + n4 : n2 - n4);
        if (s2 != 0) {
            if (s2 == 1) {
                if (l.T[17] > 0) {
                    n2 -= n2 * l.R[153 + (l.T[17] - 1)] / 100;
                }
            } else if (s2 == 2) {
                if (l.T[18] > 0) {
                    n2 -= n2 * l.R[162 + (l.T[18] - 1)] / 100;
                }
            } else if (s2 == 3 && l.T[19] > 0) {
                n2 -= n2 * l.R[171 + (l.T[19] - 1)] / 100;
            }
        }
        return n2;
    }

    public static void b(c c2) {
        if (k.k.U != 39) {
            byte by;
            short s;
            int n = d.a(1, 101);
            if (n < N - (s = f.ax[(by = c2.G) * 10 + 3])) {
                k.a(-1, -1, -5, 0, 0, -32752, 0, k.k.s, k.k.t);
                return;
            }
            k.k.c((byte)(c2.i() + 4 & 7));
            k.k.a(30, false);
            k.a(-1, -1, 20, 0, 0, -32752, 0, k.k.s, k.k.t);
            h.t(c2.G);
        }
    }

    private static void t(int n) {
        int n2 = 0;
        if (a) {
            return;
        }
        n2 = h.s(n);
        k.k.b(-n2);
        c c2 = (c)k.k;
        ((c)k.k).d = 0;
        k.a(-1, -1, -3, n2, 0, -32752, 0, k.k.s, k.k.t);
        if (k.k.Q <= 0) {
            k.k.a(39, true);
            k.k.c(0);
        }
        if (k.e()) {
            k.b(false);
        }
        k.a(false);
    }

    public static void a(byte by, int n) {
        ai = by;
        aj = n;
        ah = 0;
    }

    private static void j(Graphics graphics) {
        block22: {
            int n;
            int n2;
            int n3;
            int n4;
            String string;
            block23: {
                int n5;
                block25: {
                    block24: {
                        if (ah == -1) break block22;
                        if (++ah > 20) {
                            ah = -1;
                        }
                        string = null;
                        if (ai == 0) {
                            string = l.u[0] + aj + l.u[1];
                        } else if (ai == 1) {
                            string = l.u[0] + f.az[aj << 1];
                        } else if (ai == 2) {
                            string = l.u[0] + f.aB[aj];
                        } else if (ai == 3) {
                            string = l.u[2];
                        } else if (ai == 4) {
                            string = l.u[3];
                        } else if (ai == 5) {
                            string = l.u[4];
                        } else if (ai == 6) {
                            string = l.u[5];
                        } else if (ai == 7) {
                            String string2 = "\u672a\u77e5\u533a\u57df";
                            if (k.a >= 0 && k.a < l.D.length) {
                                string2 = l.D[k.a];
                            }
                            string = string2;
                        } else {
                            return;
                        }
                        int n6 = d.a.stringWidth(string) + 6;
                        int n7 = d.c + d.d;
                        n4 = f.ad - n6 >> 1;
                        n3 = 0;
                        n3 = f.ae - 65;
                        graphics.setClip(0, 0, f.ad, f.ae);
                        d.a(graphics, n4, n3, n6, n7);
                        if (f[24] != null) {
                            graphics.setClip(0, 0, f.ad, f.ae);
                            h.d(graphics, n4 - 2, n3 - 2, n6 + 4, n7 + 4);
                        }
                        graphics.setClip(0, 0, f.ad, f.ae);
                        n4 = f.ad >> 1;
                        n3 += d.d >> 1;
                        n2 = 12097640;
                        n = 5781512;
                        if (ai != 2 || aj % 3 == 0) break block23;
                        if (aj % 3 != 1) break block24;
                        n5 = 21670;
                        break block25;
                    }
                    if (aj % 3 != 2) break block23;
                    n5 = 0xFF00FF;
                }
                n = n5;
            }
            d.a(graphics, string, n4, n3, 17, n2, n, 0);
        }
    }

    private static void W() {
        if (ak != 1 && ak != 2) {
            ak = 2;
            al = 0;
        }
    }

    private static void b(boolean bl) {
        if (bl) {
            if (ak != 0) {
                ak = 0;
                return;
            }
        } else if (ak != 0 && ak != 3) {
            ak = 3;
            al = 0;
        }
    }

    private static void X() {
        if (al != -1 && ++al >= 8) {
            al = -1;
            ak = ak == 2 ? 1 : 0;
        }
    }

    private void k(Graphics graphics) {
        block17: {
            block18: {
                block25: {
                    int n;
                    int n2;
                    int n3;
                    Image image;
                    Graphics graphics2;
                    block24: {
                        int n4;
                        int n5;
                        block23: {
                            int n6;
                            block20: {
                                int n7;
                                block22: {
                                    block21: {
                                        block19: {
                                            int n8;
                                            if (k.g()) {
                                                return;
                                            }
                                            if (ak == 0) break block17;
                                            int n9 = 0;
                                            if (ak == 2) {
                                                n9 = -68;
                                            }
                                            if (ak == 2 || ak == 3) {
                                                int n10 = 8 * al;
                                                n9 = ak == 3 ? n9 - n10 : n9 + n10;
                                            } else if (ak == 1) {
                                                n9 = 0;
                                            }
                                            graphics.setClip(0, 0, f.ad, f.ae);
                                            graphics.drawImage(f[8], 0, n9, 20);
                                            int n11 = k.k.Q * 59 / k.k.R;
                                            h.a(graphics, f[27], 7, n9 + 5, 0, 0, n11, 4);
                                            h.b(graphics, 45, n9, String.valueOf(k.k.Q), 1, 0);
                                            int n12 = C * 52 / D;
                                            graphics.setColor(255);
                                            h.a(graphics, f[27], 2, n9 + 18, 0, 4, n12, 4);
                                            h.b(graphics, 37, n9 + 12, String.valueOf(C), 1, 0);
                                            int n13 = A * 171 / B;
                                            graphics.setColor(65280);
                                            graphics.setClip(0, 0, f.ad, f.ae);
                                            h.b(graphics, 19, n9 + 1, String.valueOf(z), 1, 1);
                                            graphics.setClip(0, 0, f.ad, f.ae);
                                            n9 = f.ae - f[28].getHeight() - 2;
                                            if (ak == 2 || ak == 3) {
                                                n8 = 3 * al;
                                                n9 = ak == 3 ? n9 + n8 : (n9 += 24) - n8;
                                            }
                                            graphics.drawImage(f[28], 11, n9, 20);
                                            h.a(graphics, f[29], 35, n9 + 26, 0, 0, n13, 2);
                                            h.f(graphics, 38, n9, 0);
                                            h.f(graphics, 66, n9, 1);
                                            h.f(graphics, 94, n9, 2);
                                            h.f(graphics, 122, n9, 3);
                                            h.g(graphics, 150, n9, 0);
                                            h.g(graphics, 178, n9, 1);
                                            if (af != null) {
                                                n8 = af.getWidth();
                                                h.b(graphics, f.ad - n8 - 4, 0);
                                            }
                                            if (f[22] == null || am <= 0) break block18;
                                            n5 = f.ad - f[22].getWidth() - 10;
                                            n4 = 0;
                                            n6 = 0;
                                            if (an != 0) break block19;
                                            n4 = 5;
                                            n6 = 2;
                                            break block20;
                                        }
                                        if (an != 1) break block21;
                                        n7 = 3;
                                        break block22;
                                    }
                                    if (an != 2) break block20;
                                    n7 = 1;
                                }
                                n4 = n7;
                            }
                            h.a(graphics, f[22], n5 + n6, 66, 0, 0, f[22].getWidth(), f[22].getHeight());
                            n5 += 4;
                            if (am >= 10) break block23;
                            graphics2 = graphics;
                            image = f[23];
                            n3 = n5;
                            n2 = 65 - n4;
                            n = am;
                            break block24;
                        }
                        if (am >= 100) break block25;
                        h.a(graphics, f[23], n5, 65 - n4, am % 10 * 11, 0, 11, 17);
                        graphics2 = graphics;
                        image = f[23];
                        n3 = n5 - 12;
                        n2 = 65 - n4;
                        n = am / 10;
                    }
                    h.a(graphics2, image, n3, n2, n * 11, 0, 11, 17);
                }
                if ((an = (byte)(an + 1)) > 10) {
                    an = 0;
                    am = 0;
                }
            }
            if (f[21] != null) {
                if (F > 0) {
                    h.a(graphics, f[21], f.ad - 40, f.ae - 60, 0, 0 + 24 * (j.aX & 1), 39, 12);
                }
                if (U > 0) {
                    h.a(graphics, f[21], f.ad - 40, f.ae - 45, 0, 12 + 24 * (j.aX & 1), 39, 12);
                }
            }
            h.j(graphics);
        }
    }

    private static void c(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(n, n2, n3, n4);
        d.a(graphics, n, n2, n3, n4, 1598381);
        h.d(graphics, n, n2, n3, n4);
    }

    private static void d(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setColor(255, 198, 57);
        graphics.drawRect(n + 1, n2 + 1, n3 - 3, n4 - 3);
        graphics.setColor(140, 49, 41);
        graphics.drawRect(n, n2, n3 - 1, n4 - 1);
        j.a(graphics, f[24], 0, 12, 8, 5, 0, n - 2, n2 + n4 - 6, 20);
        j.a(graphics, f[24], 0, 12, 8, 5, 2, n + n3 - 6, n2 + n4 - 6, 20);
        j.a(graphics, f[24], 0, 0, 13, 12, 0, n - 3, n2 - 3, 20);
        j.a(graphics, f[24], 0, 0, 13, 12, 2, n + n3 - 10, n2 - 3, 20);
    }

    private static void f(Graphics graphics, int n, int n2, int n3) {
        int n4;
        block14: {
            int n5;
            block11: {
                block13: {
                    block12: {
                        block10: {
                            int n6;
                            int n7;
                            int n8;
                            Graphics graphics2;
                            int n9 = Y[n3];
                            graphics.setClip(0, 0, f.ad, f.ae);
                            if (n9 != -1 && C < l.S[n9 * 4 + 2]) {
                                graphics2 = graphics;
                                n8 = 157;
                                n7 = 8;
                                n6 = 13;
                            } else {
                                graphics2 = graphics;
                                n8 = 53;
                                n7 = 55;
                                n6 = 68;
                            }
                            graphics2.setColor(n8, n7, n6);
                            graphics.fillRect(n + 2, n2 + 2, 20, 20);
                            n += 2;
                            n2 += 2;
                            if (n9 != -1) {
                                h.d(graphics, n + 2, n2 + 2, n9);
                            }
                            graphics.setClip(n, n2, 20, 20);
                            if (n9 != -1 && Z[n3] != -1) {
                                int n10 = n3;
                                Z[n10] = Z[n10] + 1;
                                n4 = l.S[n9 * 4 + 1] * 10;
                                if (Z[n3] > n4) {
                                    h.Z[n3] = -1;
                                } else {
                                    int n11 = 360 - 360 * Z[n3] / n4;
                                    graphics.setColor(0);
                                    if (n11 > 0) {
                                        graphics.fillArc(n - 5, n2 - 5, 30, 30, 90, n11);
                                    }
                                }
                            }
                            n4 = 1;
                            if (n3 != 0) break block10;
                            n5 = 1;
                            break block11;
                        }
                        if (n3 != 1) break block12;
                        n5 = 3;
                        break block11;
                    }
                    if (n3 != 2) break block13;
                    n5 = 7;
                    break block11;
                }
                if (n3 != 3) break block14;
                n5 = 9;
            }
            n4 = n5;
        }
        graphics.setClip(n -= 2, n2 -= 2, 6, 7);
        graphics.drawImage(f[9], n - 6 * n4, n2, 20);
    }

    public static void d(Graphics graphics, int n, int n2, int n3) {
        graphics.setClip(n, n2, 16, 16);
        graphics.drawImage(f[5], n, n2 - n3 * 16, 20);
    }

    private static void g(Graphics graphics, int n, int n2, int n3) {
        block7: {
            int n4;
            int n5;
            Image image;
            Graphics graphics2;
            block6: {
                block5: {
                    int n6 = ab[n3][0];
                    int n7 = ab[n3][1];
                    graphics.setClip(0, 0, f.ad, f.ae);
                    graphics.setClip(n += 2, n2 += 2, 20, 20);
                    if (n6 != -1) {
                        h.b(graphics, n + 2, n2 + 2, n6);
                        if (n7 > 1) {
                            h.e(graphics, n + 2 + 16, n2 + 2 + 16, n7);
                        }
                    }
                    n -= 2;
                    n2 -= 2;
                    if (n3 != 0) break block5;
                    graphics.setClip(n, n2, 7, 7);
                    graphics2 = graphics;
                    image = f[9];
                    n5 = n;
                    n4 = 60;
                    break block6;
                }
                if (n3 != 1) break block7;
                graphics.setClip(n, n2, 7, 7);
                graphics2 = graphics;
                image = f[9];
                n5 = n;
                n4 = 67;
            }
            graphics2.drawImage(image, n5 - n4, n2, 20);
        }
    }

    private static void b(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(8997198);
        graphics.fillRect(n, n2, n3, n4);
        graphics.setColor(14001480);
        n4 -= 2;
        if (n6 <= 0) {
            n6 = 1;
        }
        if ((n8 = n5 * n4 / n6) > n4) {
            n8 = n4;
        }
        int n9 = 0;
        int n10 = 0;
        if (n5 < n6) {
            n9 = (n4 - n8 << 10) / (n6 - n5);
            n10 = n4 - n8 - (n9 * (n6 - n5) >> 10);
        }
        if (n7 + n5 < n6) {
            n10 = 0;
        }
        graphics.fillRect(n + 1, n2 + 1 + (n9 * n7 >> 10) + n10, n3 - 2, n8);
    }

    public static void a(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(41, 56, 41);
        graphics.fillRect(n, n2, n3, n4);
        graphics.setColor(156, 121, 140);
        graphics.drawRect(n, n2, --n3, --n4);
        graphics.drawRect(n, n2, n3, 6);
        graphics.drawRect(n, n2 + n4 - 6, n3, 6);
        graphics.drawLine(n + 1 + (n3 - 3 >> 1), n2 + 3, n + (n3 - 3 >> 1) + 3, n2 + 3);
        graphics.drawLine(n + 1 + (n3 - 3 >> 1) + 1, n2 + 2, n + 1 + (n3 - 3 >> 1) + 1, n2 + 4);
        graphics.drawLine(n + 1 + (n3 - 3 >> 1), n2 + n4 - 6 + 3, n + (n3 - 3 >> 1) + 3, n2 + n4 - 6 + 3);
        graphics.drawLine(n + 1 + (n3 - 3 >> 1) + 1, n2 + n4 - 6 + 2, n + 1 + (n3 - 3 >> 1) + 1, n2 + n4 - 6 + 4);
        n4 -= 15;
        graphics.setColor(198, 178, 148);
        if (n6 <= 0) {
            n6 = 1;
        }
        if ((n8 = n5 * n4 / n6) > n4) {
            n8 = n4;
        }
        int n9 = 0;
        int n10 = 0;
        if (n5 < n6) {
            n9 = (n4 - n8 << 10) / (n6 - n5);
            n10 = n4 - n8 - (n9 * (n6 - n5) >> 10);
        }
        if (n7 + n5 < n6) {
            n10 = 0;
        }
        graphics.fillRect(n + 2, n2 + 8 + (n9 * n7 >> 10) + n10, n3 - 3, n8);
    }

    private static void b(Graphics graphics, String string, int n, int n2, int n3, boolean bl) {
        graphics.setClip(0, 0, f.ad, f.ae);
        int n4 = 5781512;
        if (bl) {
            n4 = 10518600;
        }
        graphics.setColor(n4);
        j.a(graphics, string, n, n2, n3, true);
    }

    public static void a(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        int n6 = 16;
        int n7 = 16;
        if (n5 == 1) {
            n6 = 24;
            n7 = 24;
        }
        int n8 = n6 + 1;
        n2 -= n7;
        int n9 = String.valueOf(n3).length();
        n = (n << 1) - n8 + n8 * (n9 - 1) >> 1;
        for (int i2 = n9 - 1; i2 >= 0; --i2) {
            int n10 = n3 % 10;
            if (n10 >= 0) {
                int n11;
                Image[] imageArray;
                Graphics graphics2;
                graphics.setClip(n, n2, n6, n7);
                if (n5 == 1) {
                    graphics2 = graphics;
                    imageArray = f;
                    n11 = 19;
                } else {
                    graphics2 = graphics;
                    imageArray = f;
                    n11 = 17;
                }
                graphics2.drawImage(imageArray[n11], n - n10 * n6, n2 - n4 * n7, 20);
            }
            n -= n8;
            n3 /= 10;
        }
    }

    private static void b(Graphics graphics, int n, int n2, String string, int n3, int n4) {
        int n5 = string.length();
        n = n3 == -1 ? n - 8 : (n3 == 0 ? (n << 1) - 8 + 8 * (n5 - 1) >> 1 : n + 8 * (n5 - 1));
        for (int i2 = n5 - 1; i2 >= 0; --i2) {
            int n6;
            char c2 = string.charAt(i2);
            if (c2 == ' ') {
                n6 = -1;
            } else {
                n6 = c2;
                n6 -= 48;
            }
            if (n6 >= 0) {
                graphics.setClip(n, n2, 7, 7);
                graphics.drawImage(f[30], n - n6 * 7, n2 - n4 * 7, 20);
            }
            n -= 8;
        }
    }

    public static void a(Graphics graphics, int n, int n2, String string, int n3, int n4) {
        int n5 = string.length();
        n = n3 == -1 ? n - 8 : (n3 == 0 ? (n << 1) - 8 + 8 * (n5 - 1) >> 1 : n + 8 * (n5 - 1));
        for (int i2 = n5 - 1; i2 >= 0; --i2) {
            int n6;
            char c2 = string.charAt(i2);
            if (c2 == '/') {
                n6 = 10;
            } else if (c2 == '*') {
                n6 = 11;
            } else if (c2 == '+') {
                n6 = 12;
            } else if (c2 == '-') {
                n6 = 13;
            } else if (c2 == '%') {
                n6 = 14;
            } else if (c2 == ' ') {
                n6 = -1;
            } else {
                n6 = c2;
                n6 -= 48;
            }
            if (n6 >= 0) {
                graphics.setClip(n, n2, 7, 7);
                graphics.drawImage(f[1], n - n6 * 7, n2 - n4 * 7, 20);
            }
            n -= 8;
        }
    }

    public static void a(Graphics graphics, int n, int n2, String string) {
        int n3 = string.length();
        string = string.toUpperCase();
        for (int i2 = 0; i2 < n3; ++i2) {
            int n4;
            int n5 = n4 = string.charAt(i2);
            graphics.setClip(n, n2, 8, 8);
            graphics.drawImage(f[2], n - (n5 -= 65) * 8, n2, 20);
            n += 8;
        }
    }

    private static void Y() {
        try {
            h = g.a("gameover" + f.ai, f.aj, 0, 0);
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }

    private static void Z() {
        h = null;
    }

    private static void aa() {
        if (j.m()) {
            h.b((byte)2);
        }
    }

    private static void l(Graphics graphics) {
        int n;
        int n2;
        String string;
        Graphics graphics2;
        graphics.setColor(0);
        graphics.fillRect(0, 0, f.ad, f.ae);
        if (h != null) {
            int n3 = f.ae - h.getHeight() >> 1;
            graphics.drawImage(h, f.ad - h.getWidth() >> 1, n3, 20);
            h.getHeight();
            graphics2 = graphics;
            string = "\u6e38\u620f\u7ed3\u675f";
            n2 = f.ad >> 1;
            n = f.ae - d.c - 10;
        } else {
            graphics2 = graphics;
            string = "\u6e38\u620f\u7ed3\u675f";
            n2 = f.ad >> 1;
            n = f.ae - d.c >> 1;
        }
        d.a(graphics2, string, n2, n, 17, 0xFF0000, 0xFFFFFF, 0);
        h.a(graphics, -1, 1, false);
    }

    private static void ab() {
        try {
            h.g[5] = g.a("foreword" + f.ai, f.aj, 0, 0);
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        d.a("\u8bc5\u5492\u5230\u6700\u540e\u8fd8\u662f\u6ca1\u6709\u89e3\u9664\uff0c\u60b2\u5267\u7684\u9b54\u738b\u5b87\u6625\u4e5f\u88ab\u53d8\u5f62\u4e86\u3002\u4ed6\u53e3\u4e2d\u53ef\u6015\u7684\u5927\u9b54\u738b\u5230\u5e95\u662f\u8c01\u5462\uff01\u5982\u60a8\u559c\u6b22\u6211\u4eec\u7684\u6e38\u620f\uff0c\u8bf7\u671f\u5f85\u7eed\u96c6|\u606d\u559c\u60a8\u901a\u5173|\u5f00\u53d1\u4eba\u5458\uff1a|www.level-9.com|\u7ee7\u7eed\u6e38\u620f\u5c06\u8fdb\u5165\u6e38\u620f\u4e8c\u5468\u76ee\uff0c\u88c5\u5907\u4e0e\u5c5e\u6027\u5c06\u7ee7\u627f\u3002|\u540c\u65f6\u5f00\u542f\u4e9a\u7559\u7279\u6751\u79d8\u5bc6\u68ee\u6797\u548c\u7f57\u83b2\u57ce\u6697\u9ed1\u7ade\u6280\u573a\uff0c\u6311\u6218\u8d85\u5f3a\u5206\u8eab\u672f\u96ea\u4e38\uff0c\u67aa\u624b\u8428\u6797\u7b49\u3002\u53ef\u83b7\u53d6\u9b54\u738b\u5957\u88c5\u548c\u5168\u5957\u4eba\u7269\u5361\u7247\u3002", null, f.ad - 10);
        s = 0;
    }

    private static void ac() {
        h.g[5] = null;
    }

    private static void ad() {
        ++s;
        if (j.g(0x100000) || j.g(2) || j.g(1)) {
            s += 4;
        }
    }

    private static void m(Graphics graphics) {
        graphics.setColor(0);
        graphics.fillRect(0, 0, f.ad, f.ae);
        int n = 10;
        int n2 = d.c + d.d;
        if (g[5] != null) {
            graphics.drawImage(g[5], f.ad >> 1, 10, 17);
            n = 10 + (g[5].getHeight() + 2);
        }
        graphics.setClip(0, n, f.ad, f.ae - n);
        int n3 = f.ae - d.c;
        int n4 = s;
        int n5 = 0;
        for (int i2 = 0; i2 < d.e.length; ++i2) {
            int n6;
            int n7;
            int n8;
            if (n3 - n4 < n + n2) {
                n8 = 0;
                n7 = 0xFFFFFF;
                n6 = n4 - (n3 - (n + n2));
            } else {
                n8 = 0xFFFFFF;
                n7 = 0;
                n6 = n4;
            }
            n5 = d.a(n8, n7, n6, n2);
            graphics.setColor(n5);
            graphics.drawString(d.e[i2], f.ad >> 1, n3 - n4, 17);
            n4 -= n2;
        }
        if (s > d.e.length * n2 + (n3 - n) - n2) {
            h.b((byte)2);
        }
    }

    public static void a(String string, int n) {
        ap = d.a(string, null, n - 6 - d.b);
        aq = 0;
        ar = false;
        as = false;
    }

    public static void h() {
        block3: {
            short s;
            block4: {
                block2: {
                    if (!j.i(2)) break block2;
                    if (!ar) break block3;
                    s = (short)(aq + 1);
                    break block4;
                }
                if (!j.i(1) || !as || (aq = (short)(aq - 1)) >= 0) break block3;
                s = 0;
            }
            aq = s;
        }
    }

    public static void b(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        short s = (short)(n4 / (d.c + d.d));
        short s2 = (short)(n4 % (d.c + d.d) >> 1);
        int n5 = n + (d.b >> 1);
        int n6 = n2 + s2;
        ar = false;
        for (int i2 = aq; i2 < ap.length; ++i2) {
            if (i2 - aq >= s) {
                ar = true;
                break;
            }
            h.b(graphics, ap[i2], n5, n6, 20, false);
            n6 += d.c + d.d;
        }
        boolean bl = as = aq > 0;
        if (ar || as) {
            h.b(graphics, n + n3 - 6, n2 + s2, 4, n4 - (s2 << 1), s, ap.length, aq);
        }
    }

    private static void b(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        graphics.setColor(n4);
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

    public static void i() {
        block7: {
            int n;
            block6: {
                block5: {
                    if (!j.i(4)) break block5;
                    if ((av -= 10) < 0) {
                        av = 0;
                    }
                    n = av;
                    break block6;
                }
                if (!j.i(8)) break block7;
                if ((av += 10) > 100) {
                    av = 100;
                }
                n = av;
            }
            h.v(n);
        }
    }

    public static void a(Graphics graphics, int n, int n2) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(61440);
        int n3 = n - 36;
        int n4 = n2;
        int n5 = 3;
        int n6 = 0;
        for (int i2 = 0; i2 < 10; ++i2) {
            if (n6 < av) {
                graphics.setColor(61440);
                graphics.fillRect(n3, n4, 5, n5);
            }
            graphics.setColor(0);
            graphics.drawRect(n3, n4, 5, n5);
            n3 += 7;
            n4 -= 2;
            n5 += 2;
            n6 += 10;
        }
        h.b(graphics, n - 35 - 20, n2 - 5, 6, 0, 11571280);
        h.b(graphics, n + 35 + 20, n2 - 5, 2, 0, 11571280);
    }

    public final void playerUpdate(Player player, String string, Object object) {
        try {
            if (string.equals("endOfMedia") && at) {
                h.a(ax, at);
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static void a(String[] stringArray) {
        aw = stringArray;
        az = new Player[aw.length];
        for (int i2 = 0; i2 < az.length; ++i2) {
            h.a(i2, aw[i2]);
        }
    }

    private static void u(int n) {
        h.w(n);
        if (az[n] != null) {
            try {
                az[n].close();
            }
            catch (Exception exception) {}
        }
        h.az[n] = null;
        if (n == ax) {
            ax = -1;
        }
        if (n == ay) {
            ay = -1;
        }
        au = null;
    }

    private static void ae() {
        h.af();
        for (int i2 = 0; i2 < az.length; ++i2) {
            if (az[i2] != null) {
                try {
                    az[i2].close();
                }
                catch (Exception exception) {}
            }
            h.az[i2] = null;
        }
        ax = -1;
        ay = -1;
        au = null;
        System.gc();
    }

    private static void a(int n, String string) {
        try {
            String string2 = string.substring(string.indexOf(".") + 1);
            System.out.println("sound type = " + string2);
            h.az[n] = Manager.createPlayer((InputStream)new ByteArrayInputStream(g.a(string, f.aj)), (String)(string2.equals("mid") ? "audio/midi" : "audio/x-wav"));
            if (az[n].getState() == 300) {
                az[n].deallocate();
            }
            az[n].addPlayerListener((PlayerListener)j);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private static void a(int n, boolean bl) {
        if (av == 0) {
            if (bl) {
                ay = ax = n;
            }
            return;
        }
        if (n < 0) {
            return;
        }
        at = bl;
        try {
            if (az[n] == null) {
                h.a(n, aw[n]);
            }
            switch (az[n].getState()) {
                case 400: {
                    break;
                }
                case 100: {
                    az[n].realize();
                }
                case 200: {
                    az[n].prefetch();
                }
                case 300: {
                    if (ax != n && av > 0) {
                        au = null;
                        au = (VolumeControl)az[n].getControl("VolumeControl");
                        au.setLevel(av);
                    }
                    az[n].start();
                    ax = n;
                }
                default: {
                    return;
                }
            }
        }
        catch (Exception exception) {}
    }

    private static void v(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n > 100) {
            n = 100;
        }
        av = n;
        if (au != null) {
            au.setLevel(av);
        }
        if (av == 0) {
            if (ay == -1 && ax != -1) {
                ay = az[ax].getState() == 400 ? ax : -1;
            }
            for (int i2 = 0; i2 < az.length; ++i2) {
                if (az[i2] == null) continue;
                try {
                    az[i2].stop();
                    continue;
                }
                catch (Exception exception) {}
            }
            return;
        }
        if (ay != -1) {
            System.out.println("sndPauseIdx" + ay);
            h.a(ay, true);
            ay = -1;
        }
    }

    private static void w(int n) {
        if (av == 0) {
            return;
        }
        if (az[n] != null) {
            try {
                az[n].stop();
                return;
            }
            catch (Exception exception) {}
        }
    }

    private static void af() {
        if (az != null) {
            for (int i2 = 0; i2 < az.length; ++i2) {
                h.w(i2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    private static void ag() {
        byte[] byArray;
        block10: {
            byArray = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            FilterOutputStream filterOutputStream = null;
            byteArrayOutputStream = new ByteArrayOutputStream();
            filterOutputStream = new DataOutputStream(byteArrayOutputStream);
            ((DataOutputStream)filterOutputStream).writeByte(av);
            ((DataOutputStream)filterOutputStream).writeByte(T);
            ((DataOutputStream)filterOutputStream).writeByte(aM ? 1 : 0);
            ((DataOutputStream)filterOutputStream).writeByte(aJ);
            ((DataOutputStream)filterOutputStream).writeByte(ao);
            ((DataOutputStream)filterOutputStream).writeByte(aN ? 1 : 0);
            ((DataOutputStream)filterOutputStream).writeByte(aO ? 1 : 0);
            ((DataOutputStream)filterOutputStream).writeByte(aL);
            ((DataOutputStream)filterOutputStream).writeByte(l.a);
            ((DataOutputStream)filterOutputStream).writeByte(l.b);
            ((DataOutputStream)filterOutputStream).writeByte(aP ? 1 : 0);
            byArray = byteArrayOutputStream.toByteArray();
            filterOutputStream.close();
            byteArrayOutputStream.close();
            try {
                filterOutputStream.close();
                byteArrayOutputStream.close();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            break block10;
            catch (Exception exception) {
                try {
                    Exception exception3 = exception;
                    exception.printStackTrace();
                }
                catch (Throwable throwable) {
                    try {
                        filterOutputStream.close();
                        byteArrayOutputStream.close();
                    }
                    catch (Exception exception4) {
                        Exception exception5 = exception4;
                        exception4.printStackTrace();
                    }
                    throw throwable;
                }
                try {
                    filterOutputStream.close();
                    byteArrayOutputStream.close();
                }
                catch (Exception exception6) {
                    Exception exception7 = exception6;
                    exception6.printStackTrace();
                }
            }
        }
        j.a(byArray, "zenoption");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    private static void ah() {
        byte[] byArray = j.a("zenoption");
        if (byArray != null) {
            ByteArrayInputStream byteArrayInputStream = null;
            FilterInputStream filterInputStream = null;
            byteArrayInputStream = new ByteArrayInputStream(byArray);
            filterInputStream = new DataInputStream(byteArrayInputStream);
            av = ((DataInputStream)filterInputStream).readByte();
            T = ((DataInputStream)filterInputStream).readByte();
            aM = ((DataInputStream)filterInputStream).readByte() == 1;
            aJ = ((DataInputStream)filterInputStream).readByte();
            ao = ((DataInputStream)filterInputStream).readByte();
            aN = ((DataInputStream)filterInputStream).readByte() == 1;
            aO = ((DataInputStream)filterInputStream).readByte() == 1;
            aL = ((DataInputStream)filterInputStream).readByte();
            l.a = ((DataInputStream)filterInputStream).readByte();
            l.b = ((DataInputStream)filterInputStream).readByte();
            aP = ((DataInputStream)filterInputStream).readByte() == 1;
            filterInputStream.close();
            byteArrayInputStream.close();
            try {
                filterInputStream.close();
                byteArrayInputStream.close();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            return;
            catch (Exception exception) {
                try {
                    Exception exception3 = exception;
                    exception.printStackTrace();
                }
                catch (Throwable throwable) {
                    try {
                        filterInputStream.close();
                        byteArrayInputStream.close();
                    }
                    catch (Exception exception4) {
                        Exception exception5 = exception4;
                        exception4.printStackTrace();
                    }
                    throw throwable;
                }
                try {
                    filterInputStream.close();
                    byteArrayInputStream.close();
                }
                catch (Exception exception6) {
                    Exception exception7 = exception6;
                    exception6.printStackTrace();
                }
                return;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public static void j() {
        byte[] byArray;
        block16: {
            int n;
            byArray = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            FilterOutputStream filterOutputStream = null;
            byteArrayOutputStream = new ByteArrayOutputStream();
            filterOutputStream = new DataOutputStream(byteArrayOutputStream);
            ((DataOutputStream)filterOutputStream).writeShort(z);
            ((DataOutputStream)filterOutputStream).writeShort(G);
            ((DataOutputStream)filterOutputStream).writeShort(H);
            ((DataOutputStream)filterOutputStream).writeShort(I);
            ((DataOutputStream)filterOutputStream).writeShort(J);
            ((DataOutputStream)filterOutputStream).writeInt(E);
            ((DataOutputStream)filterOutputStream).writeShort(k.k.Q);
            ((DataOutputStream)filterOutputStream).writeShort(C);
            ((DataOutputStream)filterOutputStream).writeInt(A);
            ((DataOutputStream)filterOutputStream).writeShort(U);
            ((DataOutputStream)filterOutputStream).writeShort(F);
            for (int i2 = 0; i2 < l.T.length; ++i2) {
                ((DataOutputStream)filterOutputStream).writeByte(l.T[i2]);
            }
            ((DataOutputStream)filterOutputStream).writeInt(j.aY);
            ((DataOutputStream)filterOutputStream).writeByte(k.a);
            ((DataOutputStream)filterOutputStream).writeByte(k.b);
            ((DataOutputStream)filterOutputStream).writeByte(k.h);
            ((DataOutputStream)filterOutputStream).writeByte(k.c);
            ((DataOutputStream)filterOutputStream).writeByte(k.d);
            ((DataOutputStream)filterOutputStream).writeByte(k.g);
            ((DataOutputStream)filterOutputStream).writeByte(k.i);
            ((DataOutputStream)filterOutputStream).writeShort(k.j);
            ((DataOutputStream)filterOutputStream).writeUTF(j.aS);
            byte[] byArray2 = V.b();
            ((DataOutputStream)filterOutputStream).writeInt(byArray2.length);
            ((OutputStream)filterOutputStream).write(byArray2);
            for (n = 0; n < W.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeShort(W[n][0]);
                ((DataOutputStream)filterOutputStream).writeByte(W[n][1]);
                ((DataOutputStream)filterOutputStream).writeByte(W[n][2]);
                ((DataOutputStream)filterOutputStream).writeByte(W[n][3]);
            }
            for (n = 0; n < X.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeShort(X[n][0]);
                ((DataOutputStream)filterOutputStream).writeByte(X[n][1]);
                ((DataOutputStream)filterOutputStream).writeByte(X[n][2]);
                ((DataOutputStream)filterOutputStream).writeByte(X[n][3]);
            }
            for (n = 0; n < Y.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeByte(Y[n]);
                ((DataOutputStream)filterOutputStream).writeShort(Z[n]);
            }
            for (n = 0; n < ab.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeByte(ab[n][0]);
            }
            for (n = 0; n < l.F.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeByte(l.F[n] ? 1 : 0);
            }
            byArray = byteArrayOutputStream.toByteArray();
            try {
                filterOutputStream.close();
                byteArrayOutputStream.close();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            break block16;
            catch (Exception exception) {
                try {
                    Exception exception3 = exception;
                    exception.printStackTrace();
                }
                catch (Throwable throwable) {
                    try {
                        filterOutputStream.close();
                        byteArrayOutputStream.close();
                    }
                    catch (Exception exception4) {
                        Exception exception5 = exception4;
                        exception4.printStackTrace();
                    }
                    throw throwable;
                }
                try {
                    filterOutputStream.close();
                    byteArrayOutputStream.close();
                }
                catch (Exception exception6) {
                    Exception exception7 = exception6;
                    exception6.printStackTrace();
                }
            }
        }
        j.a(byArray, "zen");
        System.out.println("RMS save OK");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    private static boolean ai() {
        block16: {
            block17: {
                int n;
                int n2;
                byte[] byArray = j.a("zen");
                if (byArray == null) break block16;
                ByteArrayInputStream byteArrayInputStream = null;
                FilterInputStream filterInputStream = null;
                byteArrayInputStream = new ByteArrayInputStream(byArray);
                filterInputStream = new DataInputStream(byteArrayInputStream);
                z = ((DataInputStream)filterInputStream).readShort();
                G = ((DataInputStream)filterInputStream).readShort();
                H = ((DataInputStream)filterInputStream).readShort();
                I = ((DataInputStream)filterInputStream).readShort();
                J = ((DataInputStream)filterInputStream).readShort();
                E = ((DataInputStream)filterInputStream).readInt();
                k.k.Q = ((DataInputStream)filterInputStream).readShort();
                C = ((DataInputStream)filterInputStream).readShort();
                A = ((DataInputStream)filterInputStream).readInt();
                U = ((DataInputStream)filterInputStream).readShort();
                F = ((DataInputStream)filterInputStream).readShort();
                for (n2 = 0; n2 < l.T.length; ++n2) {
                    l.T[n2] = ((DataInputStream)filterInputStream).readByte();
                }
                j.aY = ((DataInputStream)filterInputStream).readInt();
                k.a = ((DataInputStream)filterInputStream).readByte();
                k.b = ((DataInputStream)filterInputStream).readByte();
                k.h = ((DataInputStream)filterInputStream).readByte();
                k.c = ((DataInputStream)filterInputStream).readByte();
                k.d = ((DataInputStream)filterInputStream).readByte();
                k.g = ((DataInputStream)filterInputStream).readByte();
                k.i = ((DataInputStream)filterInputStream).readByte();
                k.j = ((DataInputStream)filterInputStream).readShort();
                j.aS = ((DataInputStream)filterInputStream).readUTF();
                V.a(l.b);
                n2 = ((DataInputStream)filterInputStream).readInt();
                byte[] byArray2 = new byte[n2];
                ((DataInputStream)filterInputStream).read(byArray2);
                V.a(byArray2);
                for (n = 0; n < W.length; ++n) {
                    h.W[n][0] = ((DataInputStream)filterInputStream).readShort();
                    h.W[n][1] = ((DataInputStream)filterInputStream).readByte();
                    h.W[n][2] = ((DataInputStream)filterInputStream).readByte();
                    h.W[n][3] = ((DataInputStream)filterInputStream).readByte();
                }
                for (n = 0; n < X.length; ++n) {
                    h.X[n][0] = ((DataInputStream)filterInputStream).readShort();
                    h.X[n][1] = ((DataInputStream)filterInputStream).readByte();
                    h.X[n][2] = ((DataInputStream)filterInputStream).readByte();
                    h.X[n][3] = ((DataInputStream)filterInputStream).readByte();
                }
                for (n = 0; n < Y.length; ++n) {
                    h.Y[n] = ((DataInputStream)filterInputStream).readByte();
                    h.Z[n] = ((DataInputStream)filterInputStream).readShort();
                }
                for (n = 0; n < ab.length; ++n) {
                    h.ab[n][0] = ((DataInputStream)filterInputStream).readByte();
                }
                for (n = 0; n < l.F.length; ++n) {
                    l.F[n] = ((DataInputStream)filterInputStream).readByte() == 1;
                }
                h.N();
                h.E();
                h.e();
                System.out.println("RMS load OK");
                try {
                    filterInputStream.close();
                    byteArrayInputStream.close();
                }
                catch (Exception exception) {
                    Exception exception2 = exception;
                    exception.printStackTrace();
                }
                break block17;
                catch (Exception exception) {
                    try {
                        Exception exception3 = exception;
                        exception.printStackTrace();
                        boolean bl = false;
                    }
                    catch (Throwable throwable) {
                        try {
                            filterInputStream.close();
                            byteArrayInputStream.close();
                        }
                        catch (Exception exception4) {
                            Exception exception5 = exception4;
                            exception4.printStackTrace();
                        }
                        throw throwable;
                    }
                    try {
                        filterInputStream.close();
                        byteArrayInputStream.close();
                    }
                    catch (Exception exception6) {
                        Exception exception7 = exception6;
                        exception6.printStackTrace();
                    }
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void a(String string, String string2, int n, int n2) {
        this.aA = null;
        this.aB = new TextBox(string, string2, n, n2);
        this.aB.addCommand(new Command(l.C[0], 4, 1));
        this.aB.addCommand(new Command(l.C[1], 2, 1));
        this.aB.setCommandListener((CommandListener)this);
        Display.getDisplay((MIDlet)j.aQ).setCurrent((Displayable)this.aB);
    }

    public final void commandAction(Command command, Displayable displayable) {
        if (Display.getDisplay((MIDlet)j.aQ).getCurrent() != this) {
            if (command.getLabel().compareTo(l.C[0]) == 0) {
                block10: {
                    if (this.aB.getString() == null || this.aB.getString().length() == 0) {
                        this.aA = "";
                    } else {
                        String string;
                        block11: {
                            h h2;
                            if ((this.aB.getConstraints() & 2) != 0 || (this.aB.getConstraints() & 3) != 0) {
                                try {
                                    String string2 = this.aB.getString();
                                    if (string2.length() > 6) {
                                        Integer.valueOf(string2.substring(5));
                                        Integer.valueOf(string2.substring(0, 5));
                                    } else {
                                        Integer.valueOf(string2);
                                    }
                                    this.aA = this.aB.getString();
                                    break block10;
                                }
                                catch (Exception exception) {
                                    h2 = this;
                                    string = "";
                                    break block11;
                                }
                            }
                            h2 = this;
                            string = this.aB.getString();
                        }
                        h2.aA = string;
                    }
                }
                Display.getDisplay((MIDlet)j.aQ).setCurrent((Displayable)this);
            }
            if (command.getLabel().compareTo(l.C[1]) == 0) {
                this.aA = "";
                Display.getDisplay((MIDlet)j.aQ).setCurrent((Displayable)this);
            }
        }
    }

    public static void f(int n) {
        aE = n;
        String string = l.X[n];
        h.a(string, f.ad - 10);
        aD = 0;
        aC = 0;
    }

    private static void aj() {
        ++aD;
        if (aC == 0) {
            if (j.n()) {
                aC = 1;
                aD = 0;
                aI = 999L;
                aH = 0L;
            } else if (j.o()) {
                aC = -1;
                aD = 0;
                h.ak();
                aE = -1;
            }
            h.h();
            return;
        }
        if (aC == 1) {
            if (aD > 1) {
                do {
                    long l2;
                    aH = l2 = System.currentTimeMillis();
                    boolean bl = d.a(aG[aL], aF[aL]);
                    long l3 = System.currentTimeMillis();
                    if (aG.length > 1 && (aL = (byte)(aL + 1)) >= aG.length) {
                        aL = 0;
                    }
                    aI = l3;
                    if (!bl) {
                        aC = 4;
                        aD = 0;
                        h.a("\u53d1\u9001\u5931\u8d25\uff0c\u662f\u5426\u91cd\u65b0\u53d1\u9001?", f.ad - 10);
                        break;
                    }
                    aC = 2;
                    aD = 0;
                    aJ = (byte)(aJ + 1);
                    aK = (byte)(aK + 1);
                } while (aK < 1);
                if (aK >= 1) {
                    aK = 0;
                    return;
                }
            }
        } else if (aC == 3) {
            if (j.n() || j.o()) {
                aC = -1;
                aD = 0;
                aE = -1;
                return;
            }
        } else if (aC == 4) {
            if (j.n()) {
                aC = 1;
                aD = 0;
                return;
            }
            if (j.o()) {
                aC = -1;
                aD = 0;
                h.ak();
                aE = -1;
                return;
            }
        } else if (aC == 2) {
            if (aI - aH > 30L) {
                // empty if block
            }
            String string = h.x(aE);
            h.a(string, f.ad - 10);
            aC = 3;
            aD = 0;
            aE = -1;
            return;
        }
    }

    private static void n(Graphics graphics) {
        block6: {
            int n;
            int n2;
            Graphics graphics2;
            block5: {
                block4: {
                    graphics.setClip(0, 0, f.ad, f.ae);
                    graphics.setColor(168, 136, 64);
                    graphics.fillRect(0, 0, f.ad, f.ae);
                    int n3 = f.ad - 8;
                    int n4 = f.ae - 8;
                    d.a(graphics, 4, 4, n3, n4);
                    if (aC != 0 && aC != 3 && aC != 4) break block4;
                    graphics.setClip(0, 0, f.ad, f.ae);
                    int n5 = d.c + d.d;
                    int n6 = f.ae - d.c - 6;
                    int n7 = n6 / n5;
                    if (ap.length < n7) {
                        n7 = ap.length;
                    }
                    int n8 = f.ae - d.c - n7 * n5 >> 1;
                    h.b(graphics, 5, n8, f.ad - 10, n7 * n5);
                    graphics2 = graphics;
                    n2 = 0;
                    n = 1;
                    break block5;
                }
                if (aC != 1 && aC != 2) break block6;
                graphics.setClip(0, 0, f.ad, f.ae);
                graphics.setColor(0, 58, 189);
                graphics.drawString("\u53d1\u9001\u4e2d\uff0c\u8bf7\u7a0d\u5019...", f.ad >> 1, f.ae - d.c >> 1, 17);
                graphics2 = graphics;
                n2 = -1;
                n = -1;
            }
            h.a(graphics2, n2, n, false);
        }
    }

    private static String x(int n) {
        String string = l.Y[n];
        if (aE == 0) {
            U = (short)(U + 5);
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 1) {
            l.a = (byte)30;
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 2) {
            l.b = (byte)30;
            V.a(l.b);
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 3) {
            E += 30000;
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 4) {
            F = (short)((z - 1) * 3);
            G = (short)10;
            H = (short)8;
            I = (short)3;
            J = 0;
            h.e();
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 5) {
            aN = true;
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 6) {
            int n2;
            k.k.b(k.k.R);
            C = D;
            k.k.a(0, true);
            int n3 = k.k.s;
            int n4 = k.k.t;
            k.a(-1, -1, 83, 0, 0, -32752, 0, n3, n4);
            ae = true;
            for (n2 = 1; n2 < 48; ++n2) {
                if (!k.v[n2].n) continue;
                c c2 = (c)k.v[n2];
                if (c2.d()) {
                    if (!k.k.a(k.v[n2], 20)) continue;
                    h.a((c)k.v[n2], 100);
                    continue;
                }
                if (c2.T != 88) continue;
                c2.g();
            }
            boolean bl = V.e(55);
            n2 = bl ? 1 : 0;
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            boolean bl2 = V.e(55);
            n2 = bl2 ? 1 : 0;
            if (!bl2) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            boolean bl3 = V.e(55);
            n2 = bl3 ? 1 : 0;
            if (!bl3) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            boolean bl4 = V.e(55);
            n2 = bl4 ? 1 : 0;
            if (!bl4) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            d.a(d.f);
        } else if (aE == 7) {
            aM = true;
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 8) {
            aO = true;
            boolean bl = V.e(55);
            if (!bl) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 9 || aE == 10 || aE == 11 || aE == 12 || aE == 13) {
            short s = l.O[aE - 9];
            boolean bl = h.c(s);
            if (!bl) {
                k.a(-1, -1, -7, s, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 14 || aE == 15 || aE == 16 || aE == 17 || aE == 18 || aE == 19 || aE == 20 || aE == 21) {
            byte by = l.M[aE - 14];
            boolean bl = V.e(by);
            if (!bl) {
                k.a(-1, -1, -6, by, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
            if (!(bl = V.e(55))) {
                k.a(-1, -1, -6, 55, 0, 0, 0, k.k.s, k.k.t);
            }
        } else if (aE == 22) {
            aP = true;
        }
        h.j();
        h.ag();
        return string;
    }

    private static void ak() {
        if (aE == 8 || aE == 20) {
            h.b((byte)2);
        }
    }

    public static boolean k() {
        int n = 0;
        for (int i2 = 1; i2 < 48; ++i2) {
            c c2 = (c)k.v[i2];
            if (!c2.d()) continue;
            n = (byte)(n + 1);
        }
        return n == 0;
    }

    public static void a(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        block5: {
            int n8;
            block3: {
                block4: {
                    block2: {
                        graphics.setClip(0, 0, f.ad, f.ae);
                        graphics.setColor(0xFFFFFF);
                        graphics.drawRect(n - 1, n2, n3 + 1, 5);
                        n7 = 0;
                        if (n6 != 0) break block2;
                        n8 = 0xFF0000;
                        break block3;
                    }
                    if (n6 != 1) break block4;
                    n8 = 255;
                    break block3;
                }
                if (n6 != 2) break block5;
                n8 = 65280;
            }
            n7 = n8;
        }
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(n7);
        int n9 = n4 * n3 / n5;
        graphics.fillRect(n, n2 + 1, n9, 4);
    }

    static {
        ax = -1;
        ay = -1;
        aC = -1;
        aD = 0;
        aE = -1;
        aF = new String[]{"106610985", "106695883"};
        aG = new String[]{"J 24054600501108", "960554600501108"};
        aH = 0L;
        aI = 0L;
        aJ = 0;
        aK = 0;
        aL = 0;
        aM = false;
        aN = false;
        aO = false;
        aP = false;
    }
}

