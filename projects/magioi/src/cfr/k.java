/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class k {
    private static j y = null;
    public static int a;
    public static int b;
    public static int c;
    public static int d;
    public static int e;
    public static int f;
    public static int g;
    public static int h;
    public static int i;
    public static int j;
    public static m k;
    private static boolean z;
    private static int A;
    private static byte[][] B;
    private static byte[][] C;
    private static byte[][] D;
    private static byte[][] E;
    public static int l;
    public static int m;
    public static int n;
    public static int o;
    private static int F;
    private static int G;
    private static int H;
    private static String[] I;
    private static Image[] J;
    private static int[] K;
    private static int[] L;
    private static int M;
    private static byte[] N;
    private static int O;
    private static int P;
    private static int Q;
    private static int R;
    private static int S;
    private static int T;
    private static boolean U;
    private static m V;
    private static int W;
    private static int X;
    public static int p;
    public static int q;
    private static Image Y;
    private static Graphics Z;
    private static int aa;
    private static int ab;
    private static int ac;
    private static int ad;
    private static boolean ae;
    private static int af;
    private static byte[] ag;
    private static byte[][] ah;
    private static byte[][] ai;
    public static boolean r;
    public static boolean s;
    public static boolean t;
    private static short[] aj;
    private static int[] ak;
    private static int[] al;
    private static int am;
    private static int an;
    private static byte ao;
    private static byte ap;
    private static boolean aq;
    private static boolean ar;
    private static byte as;
    private static byte at;
    private static boolean au;
    public static int u;
    private static short[] av;
    private static int[] aw;
    private static byte[] ax;
    private static byte[] ay;
    private static byte[] az;
    private static byte aA;
    private static byte aB;
    public static m[] v;
    public static a[] w;
    private static byte[] aC;
    public static int x;
    private static byte aD;
    private static byte[][] aE;
    private static boolean aF;
    private static int aG;
    private static int aH;
    private static int aI;
    private static m aJ;
    private static boolean aK;
    private static String[] aL;
    private static byte aM;
    private static byte aN;
    private static String[] aO;
    private static byte aP;
    private static boolean aQ;
    private static byte aR;
    private static int aS;
    private static short aT;
    private static byte[] aU;
    private static byte[] aV;

    public static void a(j j2) {
        y = j2;
    }

    public static void a(String[] stringArray, m[] mArray) {
        k.a(mArray);
        e.b();
        k.a(stringArray);
        k.u();
        k.n();
        if (f.ar) {
            Y = Image.createImage((int)(f.as * f.af), (int)(f.at * f.af));
            Z = Y.getGraphics();
            aa = -1;
            ab = -1;
            ac = 0;
            ad = 0;
            ae = true;
        }
    }

    public static void a(int n, int n2, int n3, int n4, int n5, int n6, boolean bl, int n7) {
        y.a(bl);
        a = n;
        b = n2;
        c = n3;
        d = n4;
        g = n5;
        h = n6;
        i = n7;
        j = j.aY;
        System.out.println("change map m_nCurrentScene = " + j);
        byte by = j.k(a);
        if (by != -1) {
            b = by;
        }
        A = -1;
        z = true;
        d.a(d.g);
        k.d(false);
        if (bl) {
            d.a(d.h);
            aQ = true;
            aR = 0;
            y.repaint();
            y.serviceRepaints();
            y.c();
            k.a(a, b);
            y.d();
            d.a(d.i);
            z = false;
            k.d(true);
            if (i >= 0) {
                k.b(i, true);
            }
            k.t();
            k.o();
            k.c(false);
            k.r();
            k.p();
            y.repaint();
            y.serviceRepaints();
        }
    }

    private static void i() {
        if (d.a() == d.h) {
            if (++A == 2) {
                y.c();
                k.a(a, b);
                return;
            }
            if (A == 3) {
                y.d();
                d.a(d.i);
                z = false;
                k.d(true);
                if (i >= 0) {
                    k.b(i, true);
                }
                k.t();
                k.o();
                k.c(false);
                k.r();
                k.p();
                y.repaint();
                y.serviceRepaints();
            }
        }
    }

    private static void b(Graphics graphics) {
        y.c(graphics);
    }

    private static void j() {
        k.c(0);
        k.c((byte)g);
        if (e == 0) {
            k.k.q = c * f.af + (f.af < 16 ? 0 : f.ah) << 8;
            k.k.s = k.v[0].q >> 8;
        } else {
            k.k.s = c * f.af + e;
            k.k.q = k.k.s << 8;
        }
        if (f == 0) {
            k.k.r = d * f.af + (f.af < 16 ? 0 : f.ah) << 8;
            k.k.t = k.v[0].r >> 8;
        } else {
            k.k.t = d * f.af + f;
            k.k.r = k.k.t << 8;
        }
        e = 0;
        f = 0;
        k.k.P = (byte)h;
        k.k.o = true;
        if ((k.k.k & 0x80) != 0) {
            k.k.k = (short)(k.k.k & 0xFFFFFF7F);
        }
        k.f();
    }

    private static void a(int n, int n2) {
        if (k.e()) {
            k.b(false);
        }
        k.a(false);
        a = n;
        b = n2;
        System.out.println("load map " + a + " " + b);
        e.a();
        aG = -1;
        aF = false;
        x = 0;
        k.a("map" + n + ".map", n2);
        k.q();
        k.j();
        U = true;
        k.b(v[0]);
        aA = 0;
        aB = 0;
        k.s();
        k.c(true);
    }

    public static void a() {
        if (d.a() != d.f) {
            d.b();
        }
        if (z) {
            k.i();
        }
        if (aR != 0) {
            k.w();
        }
        p = f.an;
        q = f.ao;
        k.k();
        k.m();
        k.t();
        k.o();
        k.c(false);
        k.r();
        k.p();
    }

    public static void b() {
        E = null;
        B = null;
        C = null;
        D = null;
        for (int i2 = 0; i2 < J.length; ++i2) {
            k.J[i2] = null;
        }
        H = 0;
        J = null;
        K = null;
        L = null;
        N = null;
        ag = null;
        ah = null;
        ai = null;
        aG = -1;
        aF = false;
        x = 0;
        k.q();
        System.gc();
    }

    public static void a(int n) {
        am = n;
    }

    private static void a(String string, int n) {
        try {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            String string2;
            int n8;
            B = null;
            C = null;
            D = null;
            E = null;
            ai = null;
            aL = null;
            aE = null;
            System.gc();
            byte[] byArray = g.a(string, f.aj ? s : f.aj);
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            boolean bl = true;
            byte by = dataInputStream.readByte();
            if (H == 0 || H != by) {
                bl = false;
                H = by;
                I = new String[H];
                for (n8 = 0; n8 < H; ++n8) {
                    k.I[n8] = dataInputStream.readUTF();
                    dataInputStream.readByte();
                }
            } else {
                H = by;
                for (n8 = 0; n8 < H; ++n8) {
                    string2 = dataInputStream.readUTF();
                    if (!I[n8].equals(string2) && bl) {
                        bl = false;
                    }
                    k.I[n8] = string2;
                    dataInputStream.readByte();
                }
                if (an != am) {
                    an = am;
                    bl = false;
                }
            }
            System.out.println("sameAsOldMap = " + bl);
            if (!bl) {
                J = new Image[H];
                K = new int[H];
                L = new int[H];
                M = 0;
                for (n8 = 0; n8 < H; ++n8) {
                    int n9;
                    int n10;
                    string2 = t ? ".png" : ".pnp";
                    String string3 = I[n8] + string2;
                    k.J[n8] = g.a(string3, f.aj ? r : f.aj, 0, am);
                    k.K[n8] = J[n8].getWidth() / f.af;
                    k.L[n8] = M;
                    if (aj != null) {
                        k.K[n8] = ak[n8];
                        k.L[n8] = M;
                        n10 = M;
                        n9 = al[n8];
                    } else {
                        n10 = M;
                        n9 = J[n8].getHeight() / f.af * K[n8];
                    }
                    M = n10 + n9;
                }
                N = new byte[M];
                n8 = 0;
                for (int i2 = 0; i2 < M; ++i2) {
                    if (n8 < H - 1 && i2 == L[n8 + 1]) {
                        ++n8;
                    }
                    k.N[i2] = (byte)n8;
                }
            }
            System.out.println("read map data");
            l = dataInputStream.readByte();
            m = dataInputStream.readByte();
            o = l * f.af;
            k.n = m * f.af;
            F = dataInputStream.readByte();
            B = new byte[F][];
            for (n8 = 0; n8 < B.length; ++n8) {
                k.B[n8] = new byte[l * m];
                for (int i3 = 0; i3 < B[n8].length; ++i3) {
                    k.B[n8][i3] = dataInputStream.readByte();
                }
            }
            byte by2 = dataInputStream.readByte();
            n8 = by2;
            if (by2 == 1) {
                C = new byte[F][];
                for (n7 = 0; n7 < C.length; ++n7) {
                    k.C[n7] = new byte[l * m / 8 + 1];
                    for (int i4 = 0; i4 < C[n7].length; ++i4) {
                        k.C[n7][i4] = dataInputStream.readByte();
                    }
                }
            } else {
                C = new byte[F][];
                for (n7 = 0; n7 < C.length; ++n7) {
                    k.C[n7] = new byte[l * m / 8 + 1];
                    for (int i5 = 0; i5 < C[n7].length; ++i5) {
                        k.C[n7][i5] = 0;
                    }
                }
            }
            D = new byte[F][l * m / 4 + 1];
            for (n7 = 0; n7 < D.length; ++n7) {
                for (int i6 = 0; i6 < D[n7].length; ++i6) {
                    k.D[n7][i6] = dataInputStream.readByte();
                }
            }
            System.out.println("read c");
            byte by3 = dataInputStream.readByte();
            n7 = by3;
            if (by3 == 0) {
                System.out.println("no Collision Layer");
            }
            E = new byte[n7][l * m / 2 + 1];
            for (n6 = 0; n6 < E.length; ++n6) {
                for (n5 = 0; n5 < E[n6].length; ++n5) {
                    k.E[n6][n5] = dataInputStream.readByte();
                }
            }
            ai = new byte[n7][M];
            for (n6 = 0; n6 < ai.length; ++n6) {
                for (n5 = 0; n5 < ai[n6].length; ++n5) {
                    k.ai[n6][n5] = dataInputStream.readByte();
                }
            }
            System.out.println("read o");
            byte by4 = dataInputStream.readByte();
            n6 = by4;
            if (by4 != 0) {
                int n11;
                int n12;
                int n13;
                if (n >= n6) {
                    return;
                }
                n4 = dataInputStream.readShort();
                for (n5 = 0; n > n5; ++n5) {
                    dataInputStream.skipBytes(n4);
                    n4 = dataInputStream.readShort();
                }
                aL = null;
                n3 = dataInputStream.readByte();
                aL = new String[n3];
                for (n2 = 0; n2 < n3; ++n2) {
                    k.aL[n2] = dataInputStream.readUTF();
                }
                aE = null;
                byte by5 = dataInputStream.readByte();
                n2 = by5;
                aE = new byte[by5][];
                for (n13 = 0; n13 < n2; ++n13) {
                    n12 = dataInputStream.readByte() & 0xFF;
                    k.aE[n13] = new byte[n12];
                    dataInputStream.read(aE[n13]);
                }
                n13 = dataInputStream.readByte();
                n12 = 1;
                byte[][] byArrayArray = new byte[n13][];
                for (int i7 = 0; i7 < n13; ++i7) {
                    n11 = dataInputStream.readByte() & 0xFF;
                    ++n12;
                    n12 += n11;
                    byArrayArray[i7] = new byte[n11];
                    dataInputStream.read(byArrayArray[i7]);
                }
                byte[] byArray2 = new byte[n12];
                n12 = 0;
                byArray2[0] = (byte)n13;
                ++n12;
                for (n11 = 0; n11 < n13; ++n11) {
                    byArray2[n12] = (byte)byArrayArray[n11].length;
                    System.arraycopy(byArrayArray[n11], 0, byArray2, ++n12, byArrayArray[n11].length);
                    n12 += byArrayArray[n11].length;
                }
                e.a(byArray2);
                byte by6 = dataInputStream.readByte();
                n11 = by6;
                n11 = by6 * 8;
                aU = new byte[n11];
                dataInputStream.read(aU);
                af = dataInputStream.readByte();
                ag = new byte[af << 1];
                ah = new byte[af][];
                int n14 = 0;
                while (n14 < af << 1) {
                    k.ag[n14++] = dataInputStream.readByte();
                    k.ag[n14++] = dataInputStream.readByte();
                }
                for (n14 = 0; n14 < af; ++n14) {
                    k.ah[n14] = new byte[ag[(n14 << 1) + 1] * 3];
                    int n15 = 0;
                    while (n15 < ag[(n14 << 1) + 1] * 3) {
                        k.ah[n14][n15++] = dataInputStream.readByte();
                        k.ah[n14][n15++] = dataInputStream.readByte();
                        k.ah[n14][n15++] = 0;
                    }
                }
            } else {
                System.out.println("no object layer");
                ag = new byte[0];
                ah = new byte[0][];
            }
            if (f.ar) {
                aa = -1;
                ab = -1;
                ac = 0;
                ad = 0;
                ae = true;
            }
            if (aj != null) {
                System.out.println("init S40 data");
                for (n5 = 0; n5 < B.length; ++n5) {
                    for (n4 = 0; n4 < B[n5].length; ++n4) {
                        int n16;
                        int n17;
                        byte[] byArray3;
                        n3 = B[n5][n4] & 0xFF;
                        if (n3 == 255) continue;
                        n2 = aj[n3 += k.c(n5, n4)];
                        if (n2 < 255) {
                            byArray3 = B[n5];
                            n17 = n4;
                            n16 = n2;
                        } else {
                            byArray3 = B[n5];
                            n17 = n4;
                            n16 = n2 - 255;
                        }
                        byArray3[n17] = (byte)n16;
                    }
                }
            }
            System.out.println("bgCreate finish");
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }

    private static void k() {
        if (V != null) {
            k.b(V);
            return;
        }
        k.b(W, X);
    }

    public static void a(m m2, int n, int n2, boolean bl) {
        if (m2 != null) {
            V = m2;
        } else {
            V = null;
            W = n;
            X = n2;
        }
        U = bl;
    }

    private static void b(m m2) {
        k.b(m2.s, m2.t - 16);
    }

    private static void b(int n, int n2) {
        int n3;
        block20: {
            int n4;
            block19: {
                block18: {
                    block17: {
                        int n5;
                        block16: {
                            block15: {
                                int n6;
                                Q = n - (f.al >> 1);
                                R = n2 - (f.am >> 1);
                                if (Q < 0) {
                                    Q = 0;
                                }
                                if (R < 0) {
                                    R = 0;
                                }
                                if ((n6 = o - f.al - 1) < 0) {
                                    n6 /= 2;
                                }
                                if (Q > n6) {
                                    Q = n6;
                                }
                                if ((n6 = k.n - f.am - 1) < 0) {
                                    n6 /= 2;
                                }
                                if (R > n6) {
                                    R = n6;
                                }
                                if (U) {
                                    O = Q;
                                    P = R;
                                    U = false;
                                    ae = true;
                                    return;
                                }
                                n3 = Q - O >> 1;
                                if (n3 <= f.ak) break block15;
                                n5 = f.ak;
                                break block16;
                            }
                            if (n3 >= -f.ak) break block17;
                            n5 = -f.ak;
                        }
                        n3 = n5;
                    }
                    O += n3;
                    if (n3 == 0) {
                        O = Q;
                    }
                    if ((n3 = R - P >> 1) <= f.ak) break block18;
                    n4 = f.ak;
                    break block19;
                }
                if (n3 >= -f.ak) break block20;
                n4 = -f.ak;
            }
            n3 = n4;
        }
        P += n3;
        if (n3 == 0) {
            P = R;
        }
    }

    public static int c() {
        return O + p;
    }

    public static int d() {
        return P + q;
    }

    private static boolean l() {
        return O != Q || P != R;
    }

    public static void a(int n, boolean bl) {
        ap = (byte)n;
        aq = bl;
    }

    public static void a(boolean bl) {
        ar = bl;
    }

    public static void b(boolean bl) {
        au = bl;
    }

    public static boolean e() {
        return au;
    }

    private static void m() {
        block7: {
            int n;
            block6: {
                byte by;
                byte by2;
                block4: {
                    block5: {
                        by2 = 0;
                        by = 0;
                        if (ap <= 0) break block4;
                        ao = (byte)((((ap = (byte)(ap - 1)) & 1) == 1 ? ap - 1 : -(ap - 1)) >> 1);
                        if (!aq) break block5;
                        n = q - ao;
                        break block6;
                    }
                    p -= ao;
                    return;
                }
                if (!ar) break block7;
                by2 = (d.a(0, 100) & 1) == 0 ? (byte)3 : -3;
                byte by3 = by = (d.a(0, 100) & 1) == 0 ? (byte)3 : -3;
                if (as == by2 && at == by) {
                    by = -by;
                }
                as = by2;
                at = by;
                p += by2;
                n = q + by;
            }
            q = n;
        }
    }

    public static void a(Graphics graphics) {
        block11: {
            Graphics graphics2;
            block10: {
                block9: {
                    if (!z || d.a() != d.h) {
                        k.a(graphics, O, P);
                    }
                    if (x != 1) break block9;
                    if ((aD = (byte)(aD + 1)) > 5) {
                        aD = (byte)5;
                    }
                    graphics2 = graphics;
                    break block10;
                }
                if (aD == 0) break block11;
                if ((aD = (byte)(aD - 1)) < 0) {
                    aD = 0;
                }
                graphics2 = graphics;
            }
            graphics2.setClip(0, 0, f.ad, f.ae);
            graphics.setColor(-16777216);
            int n = f.am / 40 * aD;
            graphics.fillRect(f.an, f.ao, f.al, n);
            graphics.fillRect(f.an, f.am - n, f.al, n);
        }
        if (d.a() != d.f) {
            d.a(graphics);
        }
        if (aR != 0) {
            k.d(graphics);
        }
        if (z) {
            k.b(graphics);
        }
    }

    private static void a(Graphics graphics, int n, int n2) {
        int n3;
        int n4;
        block23: {
            int n5;
            int n6;
            block17: {
                block19: {
                    int n7;
                    int n8;
                    block22: {
                        int n9;
                        block21: {
                            block20: {
                                int n10;
                                block18: {
                                    if (ap <= 0 && !ar) {
                                        graphics.setClip(0, 0, f.ad, f.ae);
                                        graphics.setColor(0);
                                        graphics.fillRect(0, 0, f.ad, f.ae);
                                    }
                                    n8 = -1;
                                    n7 = -1;
                                    S = n / f.af;
                                    T = n2 / f.af;
                                    n6 = -(n % f.af);
                                    n5 = -(n2 % f.af);
                                    n6 -= p;
                                    n5 -= q;
                                    if (!f.ar) break block17;
                                    if (!ae) break block18;
                                    ae = false;
                                    aa = S;
                                    ab = T;
                                    ad = 0;
                                    ac = 0;
                                    Z.setClip(0, 0, Y.getWidth(), Y.getHeight());
                                    Z.setColor(0);
                                    Z.fillRect(0, 0, Y.getWidth(), Y.getHeight());
                                    k.b(Z, 0, 0);
                                    graphics.drawImage(Y, n6, n5, 20);
                                    break block19;
                                }
                                if (S == aa && T == ab) break block19;
                                if (aa < S) {
                                    if ((ac += f.af) >= f.au) {
                                        ac -= f.au;
                                    }
                                    n10 = 1;
                                } else if (aa > S) {
                                    if ((ac -= f.af) < 0) {
                                        ac += f.au;
                                    }
                                    n10 = n8 = 0;
                                }
                                if (ab >= T) break block20;
                                if ((ad += f.af) >= f.av) {
                                    ad -= f.av;
                                }
                                n9 = 1;
                                break block21;
                            }
                            if (ab <= T) break block22;
                            if ((ad -= f.af) < 0) {
                                ad += f.av;
                            }
                            n9 = 0;
                        }
                        n7 = n9;
                    }
                    aa = S;
                    ab = T;
                    if (n7 > -1) {
                        k.b(S, T + (f.at - 1) * n7, ac, ad + (f.av - f.af) * n7);
                    }
                    if (n8 > -1) {
                        k.a(S + (f.as - 1) * n8, T, ac + (f.au - f.af) * n8, ad);
                    }
                }
                n4 = f.au - ac;
                n3 = f.av - ad;
                graphics.setClip(n6, n5, n4, n3);
                graphics.drawImage(Y, n6 - ac, n5 - ad, 20);
                graphics.setClip(n4 + n6, n5, ac, n3);
                graphics.drawImage(Y, n4 + n6, n5 - ad, 20);
                graphics.setClip(n6, n3 + n5, n4, ad);
                graphics.drawImage(Y, n6 - ac, n3 + n5, 20);
                graphics.setClip(n4 + n6, n3 + n5, ac, ad);
                graphics.drawImage(Y, n4 + n6, n3 + n5, 20);
                break block23;
            }
            k.b(graphics, n6, n5);
        }
        if (au) {
            graphics.setClip(0, 0, f.ad, f.ae);
            graphics.setColor(0);
            graphics.fillRect(0, 0, f.ad, f.ae);
        }
        k.c(graphics);
        for (n4 = 0; n4 < 48; ++n4) {
            if (!k.v[n4].n) continue;
            v[n4].b(graphics);
        }
        if (f.Z) {
            graphics.setColor(-65536);
            graphics.setClip(0, 0, f.ad, f.ae);
            for (n4 = 0; n4 < 48; ++n4) {
                if (!k.v[n4].n || !k.v[n4].p || (k.v[n4].k & 4) == 0) continue;
                n3 = k.v[n4].s - k.c();
                int n11 = k.v[n4].t - k.d();
                graphics.drawRect(n3 + k.v[n4].B, n11 + k.v[n4].D, -k.v[n4].B + k.v[n4].C, -k.v[n4].D + k.v[n4].E);
                graphics.drawRect(n3 - 1, n11 - 1, 3, 3);
            }
        }
    }

    private static void b(int n, int n2, int n3) {
        if (n < 0 || n >= B[0].length) {
            return;
        }
        Z.setClip(n2, n3, f.af, f.af);
        for (int i2 = 0; i2 < B.length; ++i2) {
            int n4 = B[i2][n] & 0xFF;
            if (n4 != 255) {
                byte by = N[n4 += k.c(i2, n)];
                int n5 = n4 - L[by];
                int n6 = n5 / K[by] * f.af;
                int n7 = n5 % K[by] * f.af;
                int n8 = k.d(i2, n);
                j.a(Z, J[by], n7, n6, f.af, f.af, f.aw[n8], n2, n3, 20);
                continue;
            }
            if (i2 != 0) continue;
            Z.fillRect(n2, n3, f.af, f.af);
        }
    }

    private static void a(int n, int n2, int n3, int n4) {
        if (n3 >= f.au) {
            n3 -= f.au;
        }
        int n5 = n + n2 * l;
        for (int i2 = 0; i2 < f.at; ++i2) {
            k.b(n5, n3, n4);
            if ((n4 += f.af) >= f.av) {
                n4 -= f.av;
            }
            n5 += l;
        }
    }

    private static void b(int n, int n2, int n3, int n4) {
        if (n4 >= f.av) {
            n4 -= f.av;
        }
        int n5 = n + n2 * l;
        for (int i2 = 0; i2 < f.as; ++i2) {
            k.b(n5, n3, n4);
            if ((n3 += f.af) >= f.au) {
                n3 -= f.au;
            }
            ++n5;
        }
    }

    private static void b(Graphics graphics, int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7 = n;
        graphics.setColor(-16777216);
        int n8 = S + T * l;
        if (graphics == Z) {
            n6 = f.as + 1 + S;
            n5 = f.at + 1 + T;
            n4 = Y.getWidth();
            n3 = Y.getHeight();
        } else {
            n6 = f.as + S;
            n5 = f.at + T;
            n4 = f.al;
            n3 = f.am;
        }
        int n9 = n3;
        int n10 = l;
        int n11 = T;
        while (n11 < n5) {
            block15: {
                int n12 = n8;
                if (n11 < 0) break block15;
                if (n8 >= B[0].length) {
                    return;
                }
                int n13 = S;
                while (n13 < n6) {
                    block16: {
                        block17: {
                            int n14;
                            int n15;
                            int n16;
                            int n17;
                            Graphics graphics2;
                            if (n13 < 0) break block16;
                            if (n13 >= n10 || n8 >= B[0].length) break;
                            if (n11 == 0) {
                                graphics2 = graphics;
                                n17 = n;
                                n16 = 0;
                                n15 = f.af;
                                n14 = n2 + f.af;
                            } else if (n11 >= n5 - 2) {
                                if (n2 >= n9) break;
                                graphics2 = graphics;
                                n17 = n;
                                n16 = n2;
                                n15 = f.af;
                                n14 = n9 - n2;
                            } else {
                                graphics2 = graphics;
                                n17 = n;
                                n16 = n2;
                                n15 = f.af;
                                n14 = f.af;
                            }
                            graphics2.setClip(n17, n16, n15, n14);
                            if (n <= n4 - f.af) break block17;
                            if (n >= n4) break block16;
                            graphics.clipRect(n, n2, n4 - n13, f.af);
                        }
                        for (int i2 = 0; i2 < B.length; ++i2) {
                            int n18 = B[i2][n8] & 0xFF;
                            if (n18 != 255) {
                                byte by = N[n18 += k.c(i2, n8)];
                                int n19 = n18 - L[by];
                                int n20 = n19 / K[by] * f.af;
                                int n21 = n19 % K[by] * f.af;
                                int n22 = k.d(i2, n8);
                                j.a(graphics, J[by], n21, n20, f.af, f.af, f.aw[n22], n, n2, 20);
                                continue;
                            }
                            if (i2 != 0) continue;
                            graphics.fillRect(n, n2, f.af, f.af);
                        }
                    }
                    ++n13;
                    ++n8;
                    n += f.af;
                }
                n = n7;
                n8 = n12;
            }
            ++n11;
            n8 += l;
            n2 += f.af;
        }
    }

    private static int c(int n, int n2) {
        byte by = C[n][n2 >> 3];
        by = (byte)(by >> 7 - (n2 & 7));
        return (by & 1) * 255;
    }

    private static int d(int n, int n2) {
        byte by = D[n][n2 >> 2];
        by = (byte)(by >> 6 - ((n2 & 3) << 1));
        return by & 3;
    }

    public static int a(int n, int n2, int n3) {
        if (n2 < 0 || n2 >= l) {
            return 1;
        }
        if (n3 < 0 || n3 >= m) {
            return 1;
        }
        return k.e(n, n3 * l + n2);
    }

    private static int e(int n, int n2) {
        if (E.length == 0) {
            return -1;
        }
        byte by = E[n][n2 >> 1];
        by = (byte)(by >> 4 - ((n2 & 1) << 2));
        return by & 0xF;
    }

    public static void a(int n, int n2, int n3, int n4, int n5, byte by) {
        int n6 = n3;
        int n7 = 0;
        while (n7 < n4) {
            int n8 = 0;
            while (n8 < n5) {
                k.a(n, n2, n3, by);
                ++n8;
                ++n3;
            }
            n3 = n6;
            ++n7;
            ++n2;
        }
    }

    private static void a(int n, int n2, int n3, byte by) {
        k.a(n, n3 * l + n2, by);
    }

    private static void a(int n, int n2, byte by) {
        byte by2;
        int n3;
        int n4;
        byte[] byArray;
        byte by3 = E[n][n2 >> 1];
        if ((n2 & 1) == 1) {
            byArray = E[n];
            n4 = n2 >> 1;
            n3 = by3 & 0xF0;
            by2 = by;
        } else {
            byArray = E[n];
            n4 = n2 >> 1;
            n3 = by << 4 & 0xF0;
            by2 = by3;
        }
        byArray[n4] = (byte)(n3 | by2 & 0xF);
    }

    private static void n() {
        aA = 0;
        aB = 0;
        av = new short[u];
        ay = new byte[u];
        az = new byte[u];
        aw = new int[u];
        ax = new byte[u];
    }

    private static void o() {
        aA = 0;
        aB = 0;
    }

    public static int a(int n, byte by, short s, int n2, byte by2) {
        int n3;
        short s2;
        int n4;
        if (aA >= u) {
            System.out.println("m_nDrawQueueLength == MAX_DRAW_QUEUE_ITEMS = " + aA);
            return -1;
        }
        if ((by2 & 1) != 0) {
            n4 = n;
            m m2 = v[n4];
            short[] sArray = m2.s();
            s2 = sArray[0];
            short s3 = sArray[1];
            short s4 = sArray[2];
            short s5 = sArray[3];
            if (m2.aa) {
                s2 = -sArray[2];
                s4 = -sArray[0];
            }
            int n5 = m2.s - k.c();
            int n6 = m2.t - k.d();
            if (n5 + s4 < p || n5 + s2 > f.al || n6 + s5 < q || n6 + s3 > f.am) {
                return -1;
            }
        } else if ((by2 & 2) != 0) {
            n4 = aB;
            for (int i2 = 0; i2 < aA; ++i2) {
                if ((ay[n4] & 2) != 0) {
                    int n7 = aw[n4];
                    s2 = ax[n4];
                    if (n7 == n && s2 == by) {
                        return -1;
                    }
                }
                n4 = az[n4];
            }
        }
        k.aw[k.aA] = n;
        k.ax[k.aA] = by;
        k.av[k.aA] = s;
        k.ay[k.aA] = by2;
        n4 = aB;
        int n8 = -1;
        if (n2 == -1) {
            for (n3 = 0; n3 < aA && s >= av[n4]; ++n3) {
                n8 = n4;
                n4 = az[n4];
            }
        } else {
            while (n3 < n2) {
                n8 = n4;
                n4 = az[n4];
                ++n3;
            }
            if (n8 == -1) {
                k.az[k.aA] = aB;
                aB = aA;
            } else {
                k.az[k.aA] = n4;
                k.az[n8] = aA;
            }
            aA = (byte)(aA + 1);
            return n3;
        }
        k.az[k.aA] = n4;
        if (n8 != -1) {
            k.az[n8] = aA;
        } else {
            aB = aA;
        }
        aA = (byte)(aA + 1);
        return n3;
    }

    private static void c(Graphics graphics) {
        byte by = aB;
        for (int i2 = 0; i2 < aA; ++i2) {
            int n;
            if ((ay[by] & 1) != 0) {
                n = aw[by];
                v[n].a(graphics);
            } else if ((ay[by] & 2) != 0) {
                n = aw[by];
                byte by2 = ax[by];
                int n2 = B[by2][n] & 0xFF;
                int n3 = n % l * f.af - k.c();
                int n4 = n / l * f.af - k.d();
                if (n2 != 255) {
                    byte by3 = N[n2 += k.c(by2, n)];
                    int n5 = n2 - L[by3];
                    int n6 = n5 / K[by3] * f.af;
                    int n7 = n5 % K[by3] * f.af;
                    int n8 = k.d(by2, n);
                    j.a(graphics, J[by3], n7, n6, f.af, f.af, f.aw[n8], n3, n4, 20);
                }
            }
            by = az[by];
        }
    }

    private static void p() {
        if (k.e()) {
            return;
        }
        for (int i2 = 0; i2 < aA; ++i2) {
            if ((ay[i2] & 1) == 0 || (ay[i2] & 0x10) != 0) continue;
            k.c(i2);
        }
    }

    private static void c(int n) {
        if (ai.length == 0) {
            return;
        }
        int n2 = aw[n];
        m m2 = v[n2];
        short[] sArray = m2.s();
        short s = sArray[0];
        short s2 = sArray[1];
        short s3 = sArray[2];
        short s4 = sArray[3];
        if (m2.aa) {
            s = -sArray[2];
            s3 = -sArray[0];
        }
        int n3 = (m2.s + s) / f.af;
        int n4 = (m2.t + s2) / f.af;
        if (n3 < 0) {
            n3 = 0;
        }
        if (n4 < 0) {
            n4 = 0;
        }
        int n5 = (m2.s + s3) / f.af - n3 + 1;
        int n6 = (m2.t + s4) / f.af - n4 + 1;
        int n7 = n3 + n4 * l;
        int n8 = 0;
        while (n8 < n6) {
            int n9 = 0;
            while (n9 < n5) {
                if (n3 < l && n4 < m) {
                    byte by = 0;
                    for (int i2 = G; i2 < B.length; ++i2) {
                        byte by2;
                        int n10 = B[i2][n7] & 0xFF;
                        if (n10 == 255 || ai[m2.P][n10 += k.c(i2, n7)] == 0 || (n4 + (by2 = ai[m2.P][n10])) * f.af - k.d() <= av[n]) continue;
                        if (by2 <= by) {
                            by2 = by;
                        } else {
                            by = by2;
                        }
                        k.c(n7, i2, n4, by2);
                    }
                }
                ++n9;
                ++n7;
                ++n3;
            }
            ++n8;
            n7 += l - n5;
            n3 -= n5;
            ++n4;
        }
    }

    private static void c(int n, int n2, int n3, int n4) {
        n3 *= f.af;
        k.a(n, (byte)n2, (short)((n3 -= k.d()) + n4 * f.af), -1, (byte)2);
    }

    private static void q() {
        for (int i2 = 1; i2 < 48; ++i2) {
            v[i2].g();
        }
    }

    private static void a(m[] mArray) {
        v = mArray;
        k = v[0];
        k.q();
    }

    private static void r() {
        int n;
        if (k.v[0].n) {
            v[0].b();
        }
        for (n = 1; n < 48; ++n) {
            if (!k.v[n].n) continue;
            v[n].b();
        }
        if (x == 2 || x == 1) {
            k.v();
        }
        for (n = 0; n < 48; ++n) {
            if (!k.v[n].n) continue;
            v[n].o();
        }
    }

    public static void f() {
        for (int i2 = 0; i2 < 48; ++i2) {
            if (!k.v[i2].n || k.v[i2].S == null) continue;
            v[i2].q();
        }
    }

    private static void d(int n, int n2, int n3, int n4) {
        int n5;
        for (n5 = 0; n5 < ah[n].length && ah[n][n5] < n2; n5 += 3) {
        }
        while (n5 < ah[n].length && ah[n][n5] < n3) {
            if (ah[n][n5 + 2] == 0 && k.a((int)ah[n][n5 + 1], n4, (int)ah[n][n5], n, n5, true) != null) {
                k.ah[n][n5 + 2] = 1;
            }
            n5 += 3;
        }
    }

    private static void e(int n, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < ah[n].length && ah[n][i2] <= n3; i2 += 3) {
            if (ah[n][i2] != n2 && ah[n][i2] != n3 || ah[n][i2 + 2] != 0 || k.a((int)ah[n][i2 + 1], n4, (int)ah[n][i2], n, i2, true) == null) continue;
            k.ah[n][i2 + 2] = 1;
        }
    }

    private static void c(boolean bl) {
        int n = k.c() / f.af;
        int n2 = k.d() / f.af;
        int n3 = n + f.as + 1;
        int n4 = n2 + (f.at + 2);
        if (n != 0) {
            --n;
        }
        if (n2 != 0) {
            --n2;
        }
        if (n4 > m) {
            n4 = m;
        }
        if (!bl) {
            for (int i2 = 0; i2 < ag.length; i2 += 2) {
                int n5 = i2 >> 1;
                if ((short)ag[i2] == n || (short)ag[i2] == n3) {
                    k.d(n5, n2, n4, ag[i2]);
                }
                if ((short)ag[i2] > n && (short)ag[i2] < n3) {
                    k.e(n5, n2, n4, ag[i2]);
                }
                if ((short)ag[i2] <= n3) continue;
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < ag.length; i3 += 2) {
            int n6 = i3 >> 1;
            if ((short)ag[i3] >= n && (short)ag[i3] <= n3) {
                k.d(n6, n2, n4, ag[i3]);
                continue;
            }
            if ((short)ag[i3] <= n3) continue;
            return;
        }
    }

    private static void s() {
        int n;
        int n2 = l;
        int n3 = m;
        if (n3 > m) {
            n3 = m;
        }
        for (n = 0; n < ag.length; n += 2) {
            int n4 = n >> 1;
            if ((short)ag[n] >= 0 && (short)ag[n] <= n2) {
                k.d(n4, 0, n3, ag[n]);
                continue;
            }
            if ((short)ag[n] <= n2) continue;
            return;
        }
        for (n = 1; n < 48; ++n) {
            if (!k.v[n].n) continue;
            k.a(v[n]);
        }
    }

    private static m a(int n, int n2, int n3, int n4, int n5, boolean bl) {
        System.out.println("Activate idx:" + n);
        int n6 = n & 0xFF;
        for (int i2 = 1; i2 < 48; ++i2) {
            byte by;
            m m2;
            int n7;
            block13: {
                block12: {
                    block11: {
                        if (k.v[i2].n) continue;
                        System.out.println("m_objectRooms=" + i2);
                        n = n6 * 8;
                        k.v[i2].O = (short)i2;
                        k.v[i2].n = true;
                        int n8 = aU[n + 2] & 0xFF;
                        if (aC != null && n8 != 255) {
                            for (n7 = 0; n7 < aC.length; n7 += 2) {
                                if (aC[n7] != n8) continue;
                                n8 = aC[n7 + 1];
                                break;
                            }
                        }
                        k.v[i2].T = n8;
                        if (k.v[i2].T == 255) break block11;
                        if (k.v[i2].T >= w.length) {
                            System.out.println("Error:this MAP have not this animFile m_animID = " + n8);
                        }
                        k.v[i2].S = w[k.v[i2].T];
                        break block12;
                    }
                    if (k.v[i2].T >= -1) break block13;
                }
                k.v[i2].o = true;
            }
            k.v[i2].U = aU[n + 3] & 0xFF;
            v[i2].a(k.v[i2].U * (v[i2].j() ? 3 : 1), true);
            k.v[i2].k = aU[n + 5];
            v[i2].c(aU[n + 4]);
            k.v[i2].F = 0;
            k.v[i2].G = aU[n + 7];
            if (bl) {
                k.v[i2].q = n2 * f.af + (aU[n + 6] >> 4) * f.ah << 8;
                k.v[i2].r = n3 * f.af + (aU[n + 6] & 0xF) * f.ah << 8;
                k.v[i2].H = (byte)n4;
                k.v[i2].I = (byte)n5;
                m2 = v[i2];
                by = (byte)n6;
            } else {
                k.v[i2].q = n2 << 8;
                k.v[i2].r = n3 << 8;
                k.v[i2].H = (byte)-1;
                k.v[i2].I = (byte)-1;
                m2 = v[i2];
                by = -1;
            }
            m2.J = by;
            k.v[i2].s = k.v[i2].q >> 8;
            k.v[i2].t = k.v[i2].r >> 8;
            n7 = aU[n + 0] & 0xFF;
            if (n7 != 255) {
                k.v[i2].M.a((byte)n7);
            }
            if ((n7 = aU[n + 1] & 0xFF) != 255) {
                k.v[i2].N.a((byte)n7);
            }
            v[i2].f();
            return v[i2];
        }
        System.out.println("can't insert a object into the list:" + n6);
        return null;
    }

    public static m a(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        for (int i2 = 1; i2 < 48; ++i2) {
            block10: {
                block9: {
                    block8: {
                        if (k.v[i2].n) continue;
                        k.v[i2].O = (short)i2;
                        k.v[i2].n = true;
                        int n10 = n3;
                        if (aC != null && n10 != -1) {
                            for (int i3 = 0; i3 < aC.length; i3 += 2) {
                                if (aC[i3] != n10) continue;
                                n10 = aC[i3 + 1];
                                break;
                            }
                        }
                        k.v[i2].T = n10;
                        if (k.v[i2].T < 0) break block8;
                        k.v[i2].S = w[k.v[i2].T];
                        break block9;
                    }
                    if (k.v[i2].T >= -1) break block10;
                }
                k.v[i2].o = true;
            }
            k.v[i2].U = n4;
            v[i2].a(k.v[i2].U * (v[i2].j() ? 3 : 1), true);
            k.v[i2].k = (short)n6;
            v[i2].c((byte)n5);
            k.v[i2].F = 0;
            k.v[i2].G = (byte)n7;
            k.v[i2].q = n8 << 8;
            k.v[i2].r = n9 << 8;
            k.v[i2].H = (byte)-1;
            k.v[i2].I = (byte)-1;
            k.v[i2].J = (byte)-1;
            k.v[i2].s = k.v[i2].q >> 8;
            k.v[i2].t = k.v[i2].r >> 8;
            if (n != -1) {
                k.v[i2].M.a((byte)n);
            }
            if (n2 != -1) {
                k.v[i2].N.a((byte)n2);
            }
            v[i2].f();
            return v[i2];
        }
        return null;
    }

    public static void a(m m2) {
        if ((m2.k & 2) == 0) {
            if (m2.H != -1) {
                int n;
                int n2;
                byte[] byArray;
                if ((m2.k & 0x40) != 0 && (m2.k & 1) == 0) {
                    k.ah[(short)(m2.H & 0xFF)][(short)(m2.I & 0xFF) + 2] = -1;
                    m2.g();
                    return;
                }
                if ((m2.k & 0x40) != 0) {
                    byArray = ah[(short)(m2.H & 0xFF)];
                    n2 = (short)(m2.I & 0xFF) + 2;
                    n = 60;
                } else {
                    byArray = ah[(short)(m2.H & 0xFF)];
                    n2 = (short)(m2.I & 0xFF) + 2;
                    n = 0;
                }
                byArray[n2] = n;
                k.ah[(short)(m2.H & 0xFF)][(short)(m2.I & 0xFF) + 1] = m2.J;
            }
            m2.g();
        }
    }

    private static void t() {
        if (j.aW != 1) {
            return;
        }
        for (int i2 = 0; i2 < ah.length; ++i2) {
            for (int i3 = 0; i3 < ah[i2].length; i3 += 3) {
                if (ah[i2][i3 + 2] <= 1) continue;
                byte[] byArray = ah[i2];
                int n = i3 + 2;
                byArray[n] = (byte)(byArray[n] - 1);
                if (ah[i2][i3 + 2] != 1) continue;
                k.ah[i2][i3 + 2] = 0;
            }
        }
    }

    private static void a(String[] stringArray) {
        try {
            w = new a[stringArray.length];
            for (int i2 = 0; i2 < stringArray.length; ++i2) {
                byte[] byArray = g.a(stringArray[i2], f.aj);
                if (byArray != null) {
                    DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
                    a a2 = new a();
                    a2.a(dataInputStream);
                    k.w[i2] = a2;
                    continue;
                }
                System.out.println("miss ani file " + stringArray[i2]);
            }
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }

    private static void u() {
        for (int i2 = 0; i2 < w.length; ++i2) {
            if (w[i2] == null) continue;
            w[i2].a();
        }
    }

    public static void b(int n, boolean bl) {
        aG = n;
        aF = true;
        aH = 0;
        aI = 0;
        aJ = null;
        x = bl ? 1 : 2;
        aD = 0;
        k.c(0);
        k.d(false);
        aQ = true;
        aR = 0;
        for (int i2 = 1; i2 < 48; ++i2) {
            if (!k.v[i2].n || !v[i2].d()) continue;
            k.v[i2].k = (short)(k.v[i2].k & 0xFFFFFF7F);
            v[i2].c(0);
        }
        System.out.println("start S " + n);
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void v() {
        if (aE == null || aE.length <= aG) {
            System.out.println("error : have not this SceneScript");
        }
        aF = true;
        block33: do {
            boolean bl;
            block43: {
                int n;
                int n2;
                block44: {
                    if (aH > aE[aG].length) {
                        System.out.println("runSceneScript m_nCurrentScriptOffset is bigger than support ArrayIndexOutOfBoundsException" + aE[aG].length);
                        return;
                    }
                    byte by = aE[aG][aH++];
                    switch (by) {
                        case 0: {
                            byte by2 = aE[aG][aH++];
                            byte by3 = aE[aG][aH++];
                            int n3 = aE[aG][aH++];
                            k.a(by2, by3, (byte)n3);
                            continue block33;
                        }
                        case 1: {
                            if (aQ) continue block33;
                            break;
                        }
                        case 2: {
                            byte by2 = aE[aG][aH++] * f.af + (f.af < 16 ? 0 : f.ah);
                            int n4 = aE[aG][aH++] * f.af + (f.af < 16 ? 0 : f.ah);
                            k.a(null, (int)by2, n4, false);
                            break block43;
                        }
                        case 3: {
                            byte by2 = aE[aG][aH++];
                            m m2 = k.a(by2);
                            if (m2 != null) {
                                k.a(m2, 0, 0, false);
                            }
                            bl = false;
                            continue block33;
                        }
                        case 4: {
                            if (!k.l()) continue block33;
                            break;
                        }
                        case 5: {
                            byte by2 = aE[aG][aH++] * f.af + (f.af < 16 ? 0 : f.ah);
                            int n5 = aE[aG][aH++] * f.af + (f.af < 16 ? 0 : f.ah);
                            U = true;
                            k.a(null, (int)by2, n5, true);
                            continue block33;
                        }
                        case 6: {
                            byte by2 = aE[aG][aH++];
                            m m3 = k.a(by2);
                            if (m3 == null) continue block33;
                            U = true;
                            k.a(m3, 0, 0, true);
                            continue block33;
                        }
                        case 7: {
                            k.a(10, true);
                            continue block33;
                        }
                        case 8: {
                            aI = aE[aG][aH++];
                            continue block33;
                        }
                        case 9: {
                            byte by2 = aE[aG][aH];
                            if (--aI > 0) {
                                aH += by2;
                                continue block33;
                            }
                            ++aH;
                            continue block33;
                        }
                        case 10: {
                            byte by2 = aE[aG][aH++];
                            m m4 = k.a(by2);
                            if (m4 == null) continue block33;
                            aJ = m4;
                            continue block33;
                        }
                        case 11: {
                            byte by2 = aE[aG][aH++];
                            byte by4 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            k.aJ.z = by2 * f.af + (f.af < 16 ? 0 : f.ah);
                            k.aJ.A = by4 * f.af + (f.af < 16 ? 0 : f.ah);
                            k.aJ.k = (short)(k.aJ.k & 0xFFFFFEFF);
                            k.aJ.k = (short)(k.aJ.k | 0x80);
                            continue block33;
                        }
                        case 12: {
                            if (aJ == null) continue block33;
                            if ((k.aJ.k & 0x100) == 0) break;
                            k.aJ.k = (short)(k.aJ.k & 0xFFFFFF7F);
                            continue block33;
                        }
                        case 13: {
                            byte by2 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            int n6 = by2 & 0xFF;
                            aJ.a(n6 * (aJ.j() ? 3 : 1), true);
                            continue block33;
                        }
                        case 14: {
                            if (aJ == null || k.aJ.X) continue block33;
                            break;
                        }
                        case 15: {
                            m m5;
                            byte by2 = aE[aG][aH++];
                            int n7 = by2 * 8;
                            int n3 = aV[n7 + 0];
                            byte by5 = aV[n7 + 1];
                            byte by6 = aV[n7 + 2];
                            byte by7 = aV[n7 + 3];
                            byte by8 = aV[n7 + 4];
                            byte by9 = aV[n7 + 5];
                            byte by10 = aV[n7 + 7];
                            if (aJ == null || (m5 = k.a(n3, by5, by6, by7, by8, by9, by10, k.aJ.s, k.aJ.t)) == null) continue block33;
                            m5.k = (short)(m5.k | 0xFFFF8010);
                            k.aJ.L = m5;
                            continue block33;
                        }
                        case 16: {
                            if (aJ == null) continue block33;
                            if (k.aJ.L != null) {
                                if (k.aJ.L.n) {
                                    if (!k.aJ.L.X) break;
                                    k.a(k.aJ.L);
                                    k.aJ.L = null;
                                    break block43;
                                } else {
                                    k.aJ.L = null;
                                }
                                break block43;
                            } else {
                                bl = false;
                                continue block33;
                            }
                        }
                        case 17: {
                            byte by2 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            aJ.c(by2);
                            continue block33;
                        }
                        case 18: {
                            byte by2 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            k.aJ.o = by2 == 1;
                            continue block33;
                        }
                        case 19: {
                            bl = false;
                            continue block33;
                        }
                        case 20: {
                            aG = -1;
                            aF = false;
                            x = 0;
                            k.d(true);
                            continue block33;
                        }
                        case 21: {
                            byte by2 = aE[aG][aH++];
                            d.a(by2);
                            continue block33;
                        }
                        case 22: {
                            byte by2 = aE[aG][aH++];
                            byte by11 = aE[aG][aH++];
                            k.b((int)by2, by11 == 1);
                            if (x != 1) continue block33;
                            aD = (byte)5;
                            continue block33;
                        }
                        case 23: {
                            byte by2 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            k.aJ.y = by2;
                            continue block33;
                        }
                        case 24: {
                            ++j.aY;
                            continue block33;
                        }
                        case 25: {
                            byte by2 = aE[aG][aH++];
                            int n8 = aE[aG][aH++];
                            int n3 = aE[aG][aH++];
                            n8 *= f.af;
                            n3 *= f.af;
                            k.a((int)by2, n8 += f.af < 16 ? 0 : f.ah, n3 += f.af < 16 ? 0 : f.ah, -1, -1, false);
                            continue block33;
                        }
                        case 26: {
                            byte by2 = aE[aG][aH++];
                            byte by12 = aE[aG][aH++];
                            int n3 = aE[aG][aH++];
                            byte by5 = aE[aG][aH++];
                            byte by6 = aE[aG][aH++];
                            byte by7 = aE[aG][aH++];
                            byte by8 = aE[aG][aH++];
                            aG = -1;
                            aF = false;
                            x = 0;
                            k.a(by2, by12, n3, by5, by6, by7, true, by8);
                            continue block33;
                        }
                        case 27: {
                            byte by2 = aE[aG][aH++];
                            boolean bl2 = y.a(by2);
                            if (bl2) continue block33;
                            n2 = aH;
                            n = 2;
                            break block44;
                        }
                        case 28: {
                            if (aJ == null || aJ == k) continue block33;
                            k.aJ.k = (short)(k.aJ.k | 0x40);
                            k.aJ.k = (short)(k.aJ.k & 0xFFFFFFFD);
                            k.a(aJ);
                            continue block33;
                        }
                        case 29: {
                            byte by2 = aE[aG][aH++];
                            byte by13 = aE[aG][aH++];
                            if (aJ == null) continue block33;
                            aJ.e(by2 * f.af + (f.af < 16 ? 0 : f.ah));
                            aJ.f(by13 * f.af + (f.af < 16 ? 0 : f.ah));
                            continue block33;
                        }
                        case 30: {
                            byte by2 = aE[aG][aH++];
                            if (by2 == 0) {
                                k.b(false);
                                continue block33;
                            }
                            k.b(true);
                            continue block33;
                        }
                        default: {
                            System.out.println("Scene Script: it's gone wrong! ");
                        }
                    }
                    n2 = aH;
                    n = 1;
                }
                aH = n2 - n;
            }
            bl = aF = false;
        } while (aF);
    }

    public static boolean g() {
        return x != 0;
    }

    private static m a(byte by) {
        for (int i2 = 0; i2 < 48; ++i2) {
            if (!k.v[i2].n || k.v[i2].T != by) continue;
            return v[i2];
        }
        return null;
    }

    public static boolean h() {
        return aK;
    }

    private static void d(boolean bl) {
        aK = bl;
    }

    private static void a(byte by, byte by2, byte by3) {
        aM = by;
        aN = by3;
        aO = d.a(aL[by2], null, f.ad - (d.b << 1));
        aP = 0;
        aS = 0;
        aR = 1;
        aQ = false;
    }

    private static void w() {
        ++aS;
        if (aR == 1) {
            if (aS > 3) {
                aR = (byte)2;
                aS = 0;
                return;
            }
        } else {
            if (aR == 2) {
                int n = f.ad;
                if (aP + 1 < aO.length) {
                    n *= 2;
                }
                if (j.m()) {
                    aS = n;
                }
                if (aS * aT >= n) {
                    aR = (byte)3;
                    aS = 0;
                }
                return;
            }
            if (aR == 3) {
                if (j.m()) {
                    aR = (byte)((aP = (byte)(aP + 2)) < aO.length ? 2 : 4);
                    aS = 0;
                    return;
                }
            } else if (aR == 4 && aS > 3) {
                aQ = true;
                aR = 0;
            }
        }
    }

    private static void d(Graphics graphics) {
        graphics.setColor(0);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n = (d.d + d.c) * 2 + 10;
        int n2 = f.ae - n;
        int n3 = d.b;
        int n4 = f.ae - n + 7;
        if (aR == 1) {
            int n5;
            Graphics graphics2;
            j j2;
            int n6 = f.ad / 3 * aS;
            int n7 = n / 3 * aS;
            if (aN == 0) {
                j2 = y;
                graphics2 = graphics;
                n5 = 0;
            } else {
                j2 = y;
                graphics2 = graphics;
                n5 = f.ad - n6;
            }
            j2.a(graphics2, n5, n2, n6, n7);
            return;
        }
        if (aR == 4) {
            int n8;
            Graphics graphics3;
            j j3;
            int n9 = f.ad / 3 * (3 - aS);
            int n10 = n / 3 * (3 - aS);
            if (aN == 0) {
                j3 = y;
                graphics3 = graphics;
                n8 = 0;
            } else {
                j3 = y;
                graphics3 = graphics;
                n8 = f.ad - n9;
            }
            j3.a(graphics3, n8, n2, n9, n10);
            return;
        }
        if (aR == 2 || aR == 3) {
            y.a(graphics, 0, n2, f.ad, n);
            if (aM != -1) {
                y.a(graphics, aN, n2, aM);
            }
            graphics.setColor(0);
            if (aR == 2) {
                graphics.setClip(0, n4, aS * aT, d.d + d.c);
                y.a(graphics, aO[aP], n3, n4, 20);
                if (aS * aT > f.ad && aP + 1 < aO.length) {
                    graphics.setClip(0, n4 + (d.d + d.c), aS * aT - f.ad, d.d + d.c);
                    y.a(graphics, aO[aP + 1], n3, n4 + (d.d + d.c), 20);
                    return;
                }
            } else {
                graphics.setClip(0, 0, f.ad, f.ae);
                y.a(graphics, aO[aP], n3, n4, 20);
                if (aP + 1 < aO.length) {
                    y.a(graphics, aO[aP + 1], n3, n4 + (d.d + d.c), 20);
                }
                y.b(graphics);
            }
        }
    }

    public static Image b(int n) {
        int n2;
        int n3;
        Image image = Image.createImage((int)(l + 1), (int)(m + 1));
        Graphics graphics = image.getGraphics();
        graphics.setColor(-6250336);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(-14671840);
        for (n3 = 0; n3 < m; ++n3) {
            for (n2 = 0; n2 < l; ++n2) {
                if (k.a(n, n2, n3) != 1) continue;
                graphics.fillRect(n2, n3, 2, 2);
            }
        }
        for (n3 = 0; n3 < ag.length; n3 += 2) {
            for (n2 = 0; n2 < ah[n3 >> 1].length; n2 += 3) {
                if (ah[n3 >> 1][n2 + 2] == -1) continue;
                byte by = ag[n3];
                byte by2 = ah[n3 >> 1][n2];
                byte by3 = ah[n3 >> 1][n2 + 1];
                int n4 = by3 * 8;
                int n5 = aU[n4 + 2] & 0xFF;
                int n6 = y.a(n5);
                if (n6 == -1) continue;
                graphics.setColor(n6);
                graphics.fillRect((int)by, (int)by2, 2, 2);
            }
        }
        return image;
    }

    public static void a(byte[] byArray) {
        aV = byArray;
    }

    static {
        c = 0;
        d = 0;
        e = 0;
        f = 0;
        g = 0;
        h = 0;
        i = -1;
        j = 0;
        k = null;
        z = false;
        A = 0;
        G = 1;
        U = false;
        V = null;
        r = false;
        s = false;
        t = true;
        am = 0;
        an = 0;
        ar = false;
        as = 0;
        at = 0;
        au = false;
        u = 64;
        aC = null;
        x = 0;
        aD = 0;
        aF = false;
        aG = -1;
        aH = 0;
        aI = 0;
        aJ = null;
        aK = true;
        aM = 0;
        aN = 0;
        aP = 0;
        aQ = false;
        aR = 0;
        aS = 0;
        aT = (short)(d.b + 2);
        aU = new byte[0];
        aV = null;
    }
}

