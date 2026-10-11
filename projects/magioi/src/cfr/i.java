/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class i {
    h a = null;
    public static final int b = 20 - d.c < 0 ? 2 : (20 - d.c >> 1) + 2;
    public static Image[] c = new Image[4];
    public static byte d = 0;
    public static byte e = 0;
    public static boolean f = true;
    public static byte g = 0;
    public static byte h = 0;
    public static byte i = 0;
    public static byte j = 0;
    public static byte k = 0;
    public boolean l = false;
    public short[] m = new short[4];
    public String[] n = null;
    public static int o = 0;
    public static int p = 0;
    public static byte q = 0;
    public static byte r = 0;
    public static int s = 0;
    public static String[] t = null;
    public static String[] u = null;
    public static short v = (short)150;
    public static byte w = 0;
    public static int x = -1;
    public static int y = 0;

    public i(h h2) {
        this.a = h2;
        this.a((byte)0);
        try {
            if (c[0] == null) {
                i.c[0] = g.a("menu_0" + f.ai, f.aj, 0, 0);
            }
            if (c[1] == null) {
                i.c[1] = g.a("hand" + f.ai, f.aj, 0, 0);
            }
            if (c[2] == null) {
                i.c[2] = g.a("menu_biankuang" + f.ai, f.aj, 0, 0);
            }
            if (c[3] == null) {
                i.c[3] = g.a("menu_ingamestring" + f.ai, f.aj, 0, 0);
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    private void a(int n) {
        switch (d) {
            case 0: {
                this.g();
                break;
            }
            case 1: {
                this.j();
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                break;
            }
            case 4: {
                break;
            }
            case 5: {
                break;
            }
            case 6: {
                break;
            }
            case 9: {
                this.x();
                break;
            }
            case 10: {
                this.A();
                break;
            }
            case 12: {
                break;
            }
            case 17: {
                this.F();
                break;
            }
            case 18: {
                this.M();
                break;
            }
            case 11: {
                this.M();
                break;
            }
            case 19: {
                this.P();
            }
        }
        d = (byte)n;
        f = true;
        e = 0;
        h = 0;
        i = 0;
        j = 0;
        k = 0;
        switch (d) {
            case 0: {
                this.f();
                return;
            }
            case 1: {
                this.i();
                return;
            }
            case 2: {
                i.l();
                return;
            }
            case 3: {
                return;
            }
            case 4: {
                return;
            }
            case 5: {
                this.p();
                return;
            }
            case 6: {
                return;
            }
            case 9: {
                this.w();
                return;
            }
            case 10: {
                this.z();
                return;
            }
            case 12: {
                this.C();
                return;
            }
            case 17: {
                this.E();
                return;
            }
            case 18: {
                this.L();
                return;
            }
            case 19: {
                this.O();
                return;
            }
            case 20: {
                this.R();
            }
        }
    }

    private static void b(int n) {
        e = (byte)n;
    }

    public final void a(byte by) {
        d = by;
        f = true;
        g = 0;
        this.l = false;
        this.a((int)d);
    }

    private void e() {
        this.l = true;
    }

    public final boolean a() {
        if (d == 8) {
            this.u();
        } else if (d == 11) {
            this.v();
        } else if (d == 9) {
            this.y();
        } else if (d == 10) {
            this.B();
        } else if (d == 12) {
            this.D();
        } else if (d == 13) {
            this.J();
        } else if (d == 14) {
            this.K();
        } else if (d == 15) {
            this.H();
        } else if (d == 16) {
            this.I();
        } else if (d == 17) {
            this.G();
        } else if (d == 18) {
            this.N();
        } else if (d == 19) {
            this.Q();
        } else if (d == 20) {
            this.S();
        } else if (f) {
            if (j.i(8)) {
                if ((g = (byte)(g + 1)) >= 7) {
                    g = 0;
                }
                this.a((int)g);
            } else if (j.i(4)) {
                if ((g = (byte)(g - 1)) < 0) {
                    g = (byte)6;
                }
                this.a((int)g);
            } else if (j.n() || j.i(2)) {
                f = false;
            } else if (j.o()) {
                this.e();
            }
        } else {
            switch (d) {
                case 0: {
                    this.h();
                    break;
                }
                case 1: {
                    this.k();
                    break;
                }
                case 2: {
                    this.m();
                    break;
                }
                case 3: {
                    this.n();
                    break;
                }
                case 4: {
                    i.o();
                    break;
                }
                case 5: {
                    this.q();
                    break;
                }
                case 6: {
                    this.r();
                }
            }
        }
        return this.l;
    }

    public final void a(Graphics graphics) {
        if (d == 8) {
            this.c(graphics);
            return;
        }
        if (d == 11) {
            this.d(graphics);
            return;
        }
        if (d == 9) {
            this.e(graphics);
            return;
        }
        if (d == 10) {
            this.f(graphics);
            return;
        }
        if (d == 12) {
            this.g(graphics);
            return;
        }
        if (d == 13) {
            this.k(graphics);
            return;
        }
        if (d == 14) {
            this.l(graphics);
            return;
        }
        if (d == 15) {
            this.i(graphics);
            return;
        }
        if (d == 16) {
            this.j(graphics);
            return;
        }
        if (d == 17) {
            this.h(graphics);
            return;
        }
        if (d == 18) {
            this.m(graphics);
            return;
        }
        if (d == 19) {
            this.n(graphics);
            return;
        }
        if (d == 20) {
            this.o(graphics);
            return;
        }
        int n = f.ad - 176 >> 1;
        int n2 = f.ae - 208 >> 1;
        this.b(graphics, n, n2, 176, 208, g);
        n2 += 20;
        switch (d) {
            case 0: {
                this.a(graphics, n, n2, 176, 188);
                break;
            }
            case 1: {
                this.b(graphics, n, n2, 176, 188);
                break;
            }
            case 2: {
                this.c(graphics, n, n2, 176, 188);
                break;
            }
            case 3: {
                this.d(graphics, n, n2, 176, 188);
                break;
            }
            case 4: {
                this.g(graphics, n, n2, 176, 188);
                break;
            }
            case 5: {
                this.h(graphics, n, n2, 176, 188);
                break;
            }
            case 6: {
                this.i(graphics, n, n2, 176, 188);
            }
        }
        if (f) {
            d.b(graphics, n - 8 + (j.aX & 1), n2 - 20 + 10, 6, 0xFFFFFF, 0);
            d.b(graphics, n + 176 + 8 - (j.aX & 1), n2 - 20 + 10, 2, 0xFFFFFF, 0);
        }
        if (c[2] != null) {
            graphics.setClip(0, 0, f.ad, f.ae);
            j.a(graphics, c[2], 0, 0, 12, 12, 0, n - 2, n2 - 20, 20);
            j.a(graphics, c[2], 0, 0, 12, 12, 2, n + 176 - 10, n2 - 20, 20);
            j.a(graphics, c[2], 0, 0, 12, 12, 1, n - 2, n2 + 188 - 10, 20);
            j.a(graphics, c[2], 0, 0, 12, 12, 3, n + 176 - 10, n2 + 188 - 10, 20);
        }
    }

    private void f() {
        for (int i2 = 0; i2 < this.m.length; ++i2) {
            this.m[i2] = 0;
        }
    }

    private void g() {
        for (int i2 = 0; i2 < this.m.length; ++i2) {
            this.m[i2] = 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void h() {
        if (e == 0) {
            if (j.i(2)) {
                if (h >= 3) return;
                h = (byte)(h + 1);
                return;
            }
            if (j.i(1) && h > 0) {
                h = (byte)(h - 1);
                return;
            }
            if (j.i(4)) {
                if (this.m[h] <= 0) return;
                byte by = h;
                this.m[by] = (short)(this.m[by] - 1);
                h.F = (short)(h.F + 1);
                return;
            }
            if (j.i(8)) {
                if (h.F <= 0) return;
                h.F = (short)(h.F - 1);
                byte by = h;
                this.m[by] = (short)(this.m[by] + 1);
                return;
            }
            if (!j.n() && !j.o()) {
                if (h != 0) return;
                if (!j.i(1)) return;
            }
            boolean bl = false;
            for (int i2 = 0; i2 < this.m.length; ++i2) {
                if (this.m[i2] <= 0) continue;
                bl = true;
            }
            if (bl) {
                this.a(null, l.g[1]);
                i.b(1);
                return;
            }
            this.a((int)g);
            return;
        }
        if (e != 1) return;
        this.d();
        if (i.b() != 0) return;
        if (i.c() != -1 && i.c() == 0) {
            h.G = (short)(h.G + this.m[0]);
            this.m[0] = 0;
            h.H = (short)(h.H + this.m[1]);
            this.m[1] = 0;
            h.I = (short)(h.I + this.m[2]);
            this.m[2] = 0;
            h.J = (short)(h.J + this.m[3]);
            this.m[3] = 0;
            h.e();
        }
        i.b(0);
    }

    private void a(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        String string;
        int n7;
        int n8 = n + n3 - 110 - 7;
        int n9 = n2 + 7;
        i.a(graphics, n8, n9, 110, 64, 3);
        int n10 = k.k.R + this.m[1] * 20;
        h.a(graphics, (n8 += 27) + 40, n9 += 2, k.k.Q + "/" + n10, 0, this.m[1] > 0 ? 1 : 0);
        h.a(graphics, n8, n9 + 8, 80, k.k.Q, n10, 0);
        h.a(graphics, n8 - 25, n9 + 4, "HP");
        h.a(graphics, n8 + 40, n9 += 22, h.C + "/" + h.D, 0, 0);
        h.a(graphics, n8, n9 + 8, 80, h.C, h.D, 1);
        h.a(graphics, n8 - 25, n9 + 4, "EN");
        h.a(graphics, n8 + 40, n9 += 22, String.valueOf(h.A), 0, 0);
        h.a(graphics, n8, n9 + 8, 80, h.A, h.B, 2);
        h.a(graphics, n8 - 25, n9 + 4, "EXP");
        n8 = n + 32;
        n9 = n2 + 50;
        k.k.S.a(graphics, 0, n8, n9, false, false);
        h.a(graphics, n8 -= 20, n9 += 5, "LV");
        h.a(graphics, n8 + 30, n9 + 1, String.valueOf(h.z), 0, 0);
        n8 = n + 7;
        n9 = n2 + n4 - 100 - 7;
        i.a(graphics, n8, n9, 82, 100, 1);
        graphics.setColor(16312520);
        graphics.drawLine(n8, n9 + 80, n8 + 82, n9 + 80);
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(0xFFFFFF);
        ++n8;
        n9 += b;
        for (n7 = 0; n7 < 5; ++n7) {
            block16: {
                int n11;
                block13: {
                    block15: {
                        block14: {
                            block12: {
                                graphics.setColor(0);
                                graphics.setClip(0, 0, f.ad, f.ae);
                                h.a(graphics, c[3], n8, n9 + 2 - 1, 0 + n7 * 30, 0, 30, 16);
                                graphics.setClip(0, 0, f.ad, f.ae);
                                string = String.valueOf(h.G + this.m[0]);
                                if (n7 != 1) break block12;
                                n11 = h.H + this.m[1];
                                break block13;
                            }
                            if (n7 != 2) break block14;
                            n11 = h.I + this.m[2];
                            break block13;
                        }
                        if (n7 != 3) break block15;
                        n11 = h.J + this.m[3];
                        break block13;
                    }
                    if (n7 != 4) break block16;
                    n11 = h.F;
                }
                string = String.valueOf(n11);
            }
            n6 = 0;
            if (n7 < 4 && this.m[n7] > 0) {
                n6 = 1;
            }
            n5 = n8 + 82 - 24;
            h.a(graphics, n5, n9 + 6, string, 0, n6);
            if (h == n7 && !f) {
                d.b(graphics, n5 - 21 + (j.aX & 1), n9 + 9, 6, 0xFFFFFF, 0);
                d.b(graphics, n5 + 21 - (j.aX & 1), n9 + 9, 2, 0xFFFFFF, 0);
                graphics.setClip(0, 0, f.ad, f.ae);
                graphics.setColor(0xFFFF00);
                graphics.drawRect(n5 - 21 - 1, n9 + 3 - 1, 43, 15);
            }
            n9 += 20;
        }
        n8 = n + n3 - 72 - 7;
        n9 = n2 + n4 - 100 - 7;
        i.a(graphics, n8, n9, 72, 100, 2);
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(0xFFFFFF);
        ++n8;
        n9 += b;
        for (n7 = 0; n7 < 5; ++n7) {
            block22: {
                String string2;
                block18: {
                    int n12;
                    StringBuffer stringBuffer;
                    block21: {
                        block20: {
                            block19: {
                                block17: {
                                    graphics.setColor(0);
                                    graphics.setClip(0, 0, f.ad, f.ae);
                                    h.a(graphics, c[3], n8, n9 + 2 - 1, 150 + n7 * 30, 0, 30, 16);
                                    graphics.setClip(0, 0, f.ad, f.ae);
                                    string = String.valueOf(h.K + this.m[0] * 1);
                                    if (n7 != 1) break block17;
                                    string2 = String.valueOf(h.L + this.m[1] * 1);
                                    break block18;
                                }
                                if (n7 != 2) break block19;
                                string2 = String.valueOf(h.M + this.m[2] * 1);
                                break block18;
                            }
                            if (n7 != 3) break block20;
                            stringBuffer = new StringBuffer();
                            n12 = (h.O + this.m[3] * 1) / 10;
                            break block21;
                        }
                        if (n7 != 4) break block22;
                        stringBuffer = new StringBuffer();
                        n12 = h.P + this.m[3] * 1;
                    }
                    string2 = stringBuffer.append(String.valueOf(n12)).append("%").toString();
                }
                string = string2;
            }
            n6 = 0;
            if (n7 < 4 && this.m[n7] > 0 || n7 == 4 && this.m[3] > 0) {
                n6 = 1;
            }
            n5 = n8 - 1 + 72 - 1;
            h.a(graphics, n5, n9 + 6, string, -1, n6);
            n9 += 20;
        }
        if (e == 1) {
            this.b(graphics);
        }
    }

    private void i() {
        this.n = null;
        int n = h.V.b(0);
        this.c(n);
    }

    private void j() {
        this.n = null;
    }

    private void c(int n) {
        String[] stringArray;
        i i2;
        if (n != -1) {
            String string = f.az[(n << 1) + 1];
            i2 = this;
            stringArray = d.a(string, null, 156);
        } else {
            System.out.println("null");
            i2 = this;
            stringArray = null;
        }
        i2.n = stringArray;
    }

    private void k() {
        block57: {
            int n;
            block67: {
                block90: {
                    block93: {
                        block92: {
                            block91: {
                                block76: {
                                    block89: {
                                        block79: {
                                            int n2;
                                            block80: {
                                                block78: {
                                                    block88: {
                                                        block86: {
                                                            block87: {
                                                                block83: {
                                                                    block85: {
                                                                        block84: {
                                                                            block81: {
                                                                                block82: {
                                                                                    block77: {
                                                                                        block75: {
                                                                                            block74: {
                                                                                                block73: {
                                                                                                    block68: {
                                                                                                        block72: {
                                                                                                            int n3;
                                                                                                            block71: {
                                                                                                                block69: {
                                                                                                                    int n4;
                                                                                                                    block70: {
                                                                                                                        block58: {
                                                                                                                            block59: {
                                                                                                                                block66: {
                                                                                                                                    int n5;
                                                                                                                                    block64: {
                                                                                                                                        int n6;
                                                                                                                                        block65: {
                                                                                                                                            block60: {
                                                                                                                                                block63: {
                                                                                                                                                    int n7;
                                                                                                                                                    String[] stringArray;
                                                                                                                                                    StringBuffer stringBuffer;
                                                                                                                                                    String string;
                                                                                                                                                    i i2;
                                                                                                                                                    block62: {
                                                                                                                                                        block61: {
                                                                                                                                                            block52: {
                                                                                                                                                                block53: {
                                                                                                                                                                    block54: {
                                                                                                                                                                        int n8;
                                                                                                                                                                        block56: {
                                                                                                                                                                            int n9;
                                                                                                                                                                            block55: {
                                                                                                                                                                                if (e != 0) break block52;
                                                                                                                                                                                if (j.i(8)) {
                                                                                                                                                                                    if (h + 1 < 18) {
                                                                                                                                                                                        h = (byte)(h + 1);
                                                                                                                                                                                    } else if (h / 6 + i + 1 < 5) {
                                                                                                                                                                                        h = (byte)(h - 5);
                                                                                                                                                                                        i = (byte)(i + 1);
                                                                                                                                                                                    }
                                                                                                                                                                                    int n10 = h.V.b(i * 6 + h);
                                                                                                                                                                                    this.c(n10);
                                                                                                                                                                                    return;
                                                                                                                                                                                }
                                                                                                                                                                                if (j.i(4)) {
                                                                                                                                                                                    if (h > 0) {
                                                                                                                                                                                        h = (byte)(h - 1);
                                                                                                                                                                                    } else if (i > 0) {
                                                                                                                                                                                        h = (byte)(h + 5);
                                                                                                                                                                                        i = (byte)(i - 1);
                                                                                                                                                                                    }
                                                                                                                                                                                    int n11 = h.V.b(i * 6 + h);
                                                                                                                                                                                    this.c(n11);
                                                                                                                                                                                    return;
                                                                                                                                                                                }
                                                                                                                                                                                if (j.i(2)) {
                                                                                                                                                                                    if (h + 6 < 18) {
                                                                                                                                                                                        h = (byte)(h + 6);
                                                                                                                                                                                    } else if (h / 6 + i + 1 < 5) {
                                                                                                                                                                                        i = (byte)(i + 1);
                                                                                                                                                                                    }
                                                                                                                                                                                    int n12 = h.V.b(i * 6 + h);
                                                                                                                                                                                    this.c(n12);
                                                                                                                                                                                    return;
                                                                                                                                                                                }
                                                                                                                                                                                if (j.i(1)) {
                                                                                                                                                                                    if (h - 6 >= 0) {
                                                                                                                                                                                        h = (byte)(h - 6);
                                                                                                                                                                                    } else if (i > 0) {
                                                                                                                                                                                        i = (byte)(i - 1);
                                                                                                                                                                                    } else {
                                                                                                                                                                                        this.a((int)g);
                                                                                                                                                                                        return;
                                                                                                                                                                                    }
                                                                                                                                                                                    int n13 = h.V.b(i * 6 + h);
                                                                                                                                                                                    this.c(n13);
                                                                                                                                                                                    return;
                                                                                                                                                                                }
                                                                                                                                                                                if (!j.n()) break block53;
                                                                                                                                                                                int n14 = h.V.b(i * 6 + h);
                                                                                                                                                                                if (n14 == -1) break block54;
                                                                                                                                                                                n9 = f.b(n14);
                                                                                                                                                                                if (n9 != 0 && n9 != 1) break block55;
                                                                                                                                                                                this.a(l.h, 64);
                                                                                                                                                                                n8 = 1;
                                                                                                                                                                                break block56;
                                                                                                                                                                            }
                                                                                                                                                                            if (n9 >= 13 && n9 <= 22) break block57;
                                                                                                                                                                            this.a(l.i, 64);
                                                                                                                                                                            n8 = 2;
                                                                                                                                                                        }
                                                                                                                                                                        i.b(n8);
                                                                                                                                                                    }
                                                                                                                                                                    return;
                                                                                                                                                                }
                                                                                                                                                                if (j.o()) {
                                                                                                                                                                    this.a((int)g);
                                                                                                                                                                    return;
                                                                                                                                                                }
                                                                                                                                                                break block57;
                                                                                                                                                            }
                                                                                                                                                            if (e != 1) break block58;
                                                                                                                                                            this.d();
                                                                                                                                                            if (i.b() != 0) break block57;
                                                                                                                                                            if (i.c() == -1) break block59;
                                                                                                                                                            n6 = h.V.b(i * 6 + h);
                                                                                                                                                            if (i.c() != 0) break block60;
                                                                                                                                                            if (f.b(n6) != 0) break block61;
                                                                                                                                                            i2 = this;
                                                                                                                                                            string = l.k[0] + l.k[1] + k.k.Q + "/" + k.k.R;
                                                                                                                                                            stringBuffer = new StringBuffer().append(l.k[2]).append(f.aA[3 * n6 + 1]);
                                                                                                                                                            stringArray = l.k;
                                                                                                                                                            n7 = 1;
                                                                                                                                                            break block62;
                                                                                                                                                        }
                                                                                                                                                        if (f.b(n6) != 1) break block63;
                                                                                                                                                        i2 = this;
                                                                                                                                                        string = l.k[0] + l.k[3] + h.C + "/" + h.D;
                                                                                                                                                        stringBuffer = new StringBuffer().append(l.k[2]).append(f.aA[3 * n6 + 1]);
                                                                                                                                                        stringArray = l.k;
                                                                                                                                                        n7 = 3;
                                                                                                                                                    }
                                                                                                                                                    i2.a(string, stringBuffer.append(stringArray[n7]).append(l.k[4]).toString());
                                                                                                                                                }
                                                                                                                                                n5 = 3;
                                                                                                                                                break block64;
                                                                                                                                            }
                                                                                                                                            if (i.c() != 1) break block65;
                                                                                                                                            this.a(l.j, 64);
                                                                                                                                            n5 = 4;
                                                                                                                                            break block64;
                                                                                                                                        }
                                                                                                                                        if (i.c() != 2) break block66;
                                                                                                                                        this.a(f.az[n6 << 1], l.q[1]);
                                                                                                                                        n5 = 5;
                                                                                                                                    }
                                                                                                                                    i.b(n5);
                                                                                                                                }
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            n = 0;
                                                                                                                            break block67;
                                                                                                                        }
                                                                                                                        if (e != 2) break block68;
                                                                                                                        this.d();
                                                                                                                        if (i.b() != 0) break block57;
                                                                                                                        n4 = h.V.b(i * 6 + h);
                                                                                                                        if (i.c() == -1) break block69;
                                                                                                                        if (i.c() != 0) break block70;
                                                                                                                        j = 1;
                                                                                                                        n3 = 6;
                                                                                                                        break block71;
                                                                                                                    }
                                                                                                                    if (i.c() != 1) break block72;
                                                                                                                    this.a(f.az[n4 << 1], l.q[1]);
                                                                                                                    n3 = 5;
                                                                                                                    break block71;
                                                                                                                }
                                                                                                                n3 = 0;
                                                                                                            }
                                                                                                            i.b(n3);
                                                                                                        }
                                                                                                        return;
                                                                                                    }
                                                                                                    if (e != 3) break block73;
                                                                                                    this.d();
                                                                                                    if (i.b() != 0) break block57;
                                                                                                    if (i.c() == 0) {
                                                                                                        int n15 = i * 6 + h;
                                                                                                        h.b(n15);
                                                                                                        int n16 = h.V.b(i * 6 + h);
                                                                                                        this.c(n16);
                                                                                                    }
                                                                                                    n = 0;
                                                                                                    break block67;
                                                                                                }
                                                                                                if (e != 4) break block74;
                                                                                                this.d();
                                                                                                if (i.b() != 0) break block57;
                                                                                                if (i.c() != -1) {
                                                                                                    int n17 = i * 6 + h;
                                                                                                    int n18 = h.V.b(n17);
                                                                                                    h.b(i.c(), n18);
                                                                                                }
                                                                                                n = 0;
                                                                                                break block67;
                                                                                            }
                                                                                            if (e != 5) break block75;
                                                                                            this.d();
                                                                                            if (i.b() != 0) break block57;
                                                                                            if (i.c() == 0) {
                                                                                                int n19 = i * 6 + h;
                                                                                                h.V.f(n19);
                                                                                                this.c(n19);
                                                                                            }
                                                                                            n = 0;
                                                                                            break block67;
                                                                                        }
                                                                                        if (e != 6) break block76;
                                                                                        if (j != 1) break block77;
                                                                                        if (j.i(8)) break block78;
                                                                                        if (!j.i(4) && !j.i(2)) break block79;
                                                                                        n2 = 0;
                                                                                        break block80;
                                                                                    }
                                                                                    if (j != 0) break block81;
                                                                                    if (j.i(8)) break block78;
                                                                                    if (!j.i(1)) break block82;
                                                                                    n2 = 1;
                                                                                    break block80;
                                                                                }
                                                                                if (!j.i(2)) break block79;
                                                                                n2 = 3;
                                                                                break block80;
                                                                            }
                                                                            if (j != 2) break block83;
                                                                            if (!j.i(4)) break block84;
                                                                            n2 = 0;
                                                                            break block80;
                                                                        }
                                                                        if (!j.i(1)) break block85;
                                                                        n2 = 1;
                                                                        break block80;
                                                                    }
                                                                    if (!j.i(2)) break block79;
                                                                    n2 = 4;
                                                                    break block80;
                                                                }
                                                                if (j != 3) break block86;
                                                                if (!j.i(8)) break block87;
                                                                n2 = 4;
                                                                break block80;
                                                            }
                                                            if (!j.i(1)) break block79;
                                                            n2 = 0;
                                                            break block80;
                                                        }
                                                        if (j != 4) break block79;
                                                        if (!j.i(4)) break block88;
                                                        n2 = 3;
                                                        break block80;
                                                    }
                                                    if (!j.i(1)) break block79;
                                                }
                                                n2 = 2;
                                            }
                                            j = (byte)n2;
                                        }
                                        if (!j.n()) break block89;
                                        if (h.X[j][0] != -1) {
                                            int n20;
                                            int n21 = h.X[j][0];
                                            int n22 = f.aC[n21 * 9 + 8];
                                            boolean bl = false;
                                            for (int i3 = 0; i3 < n22; ++i3) {
                                                if (h.X[j][1 + i3] != -1) continue;
                                                bl = true;
                                                break;
                                            }
                                            if (!bl) {
                                                this.a(l.l[2]);
                                                n20 = 7;
                                            } else {
                                                this.a(f.aB[h.X[j][0]], l.k[5]);
                                                n20 = 8;
                                            }
                                            i.b(n20);
                                            return;
                                        }
                                        break block57;
                                    }
                                    if (!j.o()) break block57;
                                    break block90;
                                }
                                if (e != 7) break block91;
                                this.d();
                                if (i.b() != 0) break block57;
                                n = 6;
                                break block67;
                            }
                            if (e != 8) break block92;
                            this.d();
                            if (i.b() != 0) break block57;
                            if (i.c() == 0) {
                                if (h.E < 1000) {
                                    this.a(l.l[4]);
                                    i.b(7);
                                    return;
                                }
                                n = 9;
                            } else {
                                n = 6;
                            }
                            break block67;
                        }
                        if (e != 9) break block93;
                        if ((k = (byte)(k + 1)) < 15) break block57;
                        int n23 = i * 6 + h;
                        int n24 = h.V.b(n23);
                        h.V.f(n23);
                        for (int i4 = 1; i4 < 4; ++i4) {
                            if (h.X[j][i4] != -1) continue;
                            h.X[i.j][i4] = n24;
                            break;
                        }
                        h.E -= 1000;
                        h.e();
                        this.a(l.l[3]);
                        n = 10;
                        break block67;
                    }
                    if (e != 10) break block57;
                    this.d();
                    if (i.b() != 0) break block57;
                    k = 0;
                    int n25 = h.V.b(i * 6 + h);
                    this.c(n25);
                }
                n = 0;
            }
            i.b(n);
        }
    }

    private void b(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        graphics.setClip(0, 0, f.ad, f.ae);
        if (e == 6 || e == 7 || e == 8 || e == 9 || e == 10) {
            int n7 = n + 7;
            int n8 = n2 + 7;
            int n9 = n3 - 14;
            boolean bl = false;
            graphics.setColor(0);
            graphics.drawString(l.l[0], n7 + (n9 >> 1), n8, 17);
            i.a(graphics, n7, n8 += d.c + d.d, n9, 100, 3);
            k.k.S.a(graphics, 0, n7 + 45, n8 + 80, false, false);
            boolean bl2 = false;
            if (j == 1) {
                bl2 = true;
            }
            i.a(graphics, n7 + 33, n8 + 10, 22, 22, bl2, true);
            if (h.X[1][0] != -1) {
                h.a(graphics, n7 + 33 + 11, n8 + 10 + 11, h.X[1]);
            }
            bl2 = false;
            if (j == 0) {
                bl2 = true;
            }
            i.a(graphics, n7 + 7, n8 + 40, 22, 22, bl2, true);
            if (h.X[0][0] != -1) {
                h.a(graphics, n7 + 7 + 11, n8 + 40 + 11, h.X[0]);
            }
            bl2 = false;
            if (j == 2) {
                bl2 = true;
            }
            i.a(graphics, n7 + 59, n8 + 40, 22, 22, bl2, true);
            if (h.X[2][0] != -1) {
                h.a(graphics, n7 + 59 + 11, n8 + 40 + 11, h.X[2]);
            }
            bl2 = false;
            if (j == 3) {
                bl2 = true;
            }
            i.a(graphics, n7 + 7, n8 + 70, 22, 22, bl2, true);
            if (h.X[3][0] != -1) {
                h.a(graphics, n7 + 7 + 11, n8 + 70 + 11, h.X[3]);
            }
            bl2 = false;
            if (j == 4) {
                bl2 = true;
            }
            i.a(graphics, n7 + 59, n8 + 70, 22, 22, bl2, true);
            if (h.X[4][0] != -1) {
                h.a(graphics, n7 + 59 + 11, n8 + 70 + 11, h.X[4]);
            }
            int n10 = 0;
            n10 = k <= 10 ? k * 2 : 20;
            n8 += 20;
            i.a(graphics, n7 += 120, n8 += n10, 22, 22, false, true);
            int n11 = i * 6 + h;
            h.a(graphics, n7 + 11, n8 + 11, n11);
            n8 += 20;
            if (k == 0) {
                graphics.setClip(0, 0, f.ad, f.ae);
                graphics.setColor(0);
                graphics.drawLine(n7 + 11 - 5, n8 + 11, n7 + 11 + 5, n8 + 11);
                graphics.drawLine(n7 + 11, n8 + 11 - 5, n7 + 11, n8 + 11 + 5);
            }
            i.a(graphics, n7, n8 += 20 - n10 * 2, 22, 22, false, true);
            h.a(graphics, n7 + 11, n8 + 11, h.X[j]);
            n8 = n2 + 7 + 100 + d.c + d.d + 7;
            n7 = n + 7;
            i.a(graphics, n7, n8, n9, 20, 4);
            graphics.setClip(0, 0, f.ad, f.ae);
            if (k == 11 || k == 13) {
                graphics.setColor(0xFFFFFF);
                graphics.fillRect(0, 0, f.ad, f.ae);
            }
            if (e != 10) {
                graphics.setColor(0xFFFFFF);
                int n12 = h.V.b(n11);
                graphics.drawString(f.az[(n12 << 1) + 1], n7 + (n9 >> 1), n8 + b, 17);
            }
            if (e == 7 || e == 8 || e == 10) {
                this.b(graphics);
            }
            return;
        }
        int n13 = n3 - 14;
        boolean bl = false;
        int n14 = n + 7;
        int n15 = n2 + 7;
        i.a(graphics, n14, n15, n13, 79, 4);
        n15 += 5;
        n14 += 5;
        boolean bl3 = false;
        for (n6 = 0; n6 < 3; ++n6) {
            for (n5 = 0; n5 < 6; ++n5) {
                boolean bl4 = false;
                if (!f && h == n6 * 6 + n5) {
                    bl4 = true;
                }
                boolean bl5 = false;
                if ((i + n6) * 6 + n5 < l.b) {
                    bl5 = true;
                }
                i.a(graphics, n14 + n5 * 23, n15 + n6 * 23, 22, 22, bl4, bl5);
                h.a(graphics, n14 + n5 * 23 + 11, n15 + n6 * 23 + 11, (i + n6) * 6 + n5);
            }
        }
        h.a(graphics, n + n3 - 20, n15, 7, 69, 3, 5, (int)i);
        n15 = n2 + n4 - 64 - 7;
        n14 = n + 7;
        i.a(graphics, n14, n15 + 20 + 4, n13, 40, 2);
        i.a(graphics, n14, n15, n13, 20, 4);
        graphics.setClip(0, 0, f.ad, f.ae);
        n6 = h.V.b(i * 6 + h);
        if (n6 != -1) {
            graphics.setColor(0xFFFFFF);
            graphics.drawString(f.az[n6 << 1], n14 + 5, n15 + b, 20);
            n15 += 24;
            for (n5 = 0; n5 < this.n.length; ++n5) {
                graphics.setColor(0);
                graphics.drawString(this.n[n5], n14 + 5, n15 + b, 20);
                n15 += 20;
            }
        }
        n15 = n2 + 7 + 80 + 7 + 2;
        n14 = n + n3 - 7 - 97;
        i.a(graphics, n14, n15, 97, 12, 3);
        h.a(graphics, n14 + 2, n15 += 2, "GOLD");
        h.a(graphics, n14 + 97 - 2, n15 + 1, String.valueOf(h.E), -1, 0);
        if (e == 1 || e == 2 || e == 3 || e == 4 || e == 5) {
            this.b(graphics);
        }
    }

    private static void l() {
        h = 1;
    }

    /*
     * Unable to fully structure code
     */
    private void m() {
        block63: {
            block64: {
                block78: {
                    block77: {
                        block75: {
                            block76: {
                                block70: {
                                    block71: {
                                        block74: {
                                            block73: {
                                                block72: {
                                                    block69: {
                                                        block66: {
                                                            block67: {
                                                                block68: {
                                                                    block65: {
                                                                        block51: {
                                                                            block62: {
                                                                                block53: {
                                                                                    block59: {
                                                                                        block61: {
                                                                                            block60: {
                                                                                                block56: {
                                                                                                    block58: {
                                                                                                        block57: {
                                                                                                            block55: {
                                                                                                                block54: {
                                                                                                                    block52: {
                                                                                                                        if (i.e != 0) break block51;
                                                                                                                        if (i.h != 1) break block52;
                                                                                                                        if (j.i(8)) {
                                                                                                                            i.h = (byte)2;
                                                                                                                        } else if (j.i(4) || j.i(2)) {
                                                                                                                            i.h = 0;
                                                                                                                        } else if (j.i(1)) {
                                                                                                                            this.a((int)i.g);
                                                                                                                        }
                                                                                                                        break block53;
                                                                                                                    }
                                                                                                                    if (i.h != 0) break block54;
                                                                                                                    if (j.i(8)) {
                                                                                                                        i.h = (byte)2;
                                                                                                                    } else if (j.i(1)) {
                                                                                                                        i.h = 1;
                                                                                                                    } else if (j.i(2)) {
                                                                                                                        i.h = (byte)3;
                                                                                                                    }
                                                                                                                    break block53;
                                                                                                                }
                                                                                                                if (i.h != 2) break block55;
                                                                                                                if (j.i(4)) {
                                                                                                                    i.h = 0;
                                                                                                                } else if (j.i(1)) {
                                                                                                                    i.h = 1;
                                                                                                                } else if (j.i(2)) {
                                                                                                                    i.h = (byte)4;
                                                                                                                }
                                                                                                                break block53;
                                                                                                            }
                                                                                                            if (i.h != 3) break block56;
                                                                                                            if (!j.i(8)) break block57;
                                                                                                            i.h = (byte)4;
                                                                                                            break block53;
                                                                                                        }
                                                                                                        if (!j.i(1)) break block58;
                                                                                                        i.h = 0;
                                                                                                        break block53;
                                                                                                    }
                                                                                                    if (!j.i(2)) break block53;
                                                                                                    break block59;
                                                                                                }
                                                                                                if (i.h != 4) break block53;
                                                                                                if (!j.i(4)) break block60;
                                                                                                i.h = (byte)3;
                                                                                                break block53;
                                                                                            }
                                                                                            if (!j.i(1)) break block61;
                                                                                            i.h = (byte)2;
                                                                                            break block53;
                                                                                        }
                                                                                        if (!j.i(2)) break block53;
                                                                                    }
                                                                                    i.h = 0;
                                                                                    i.b(1);
                                                                                }
                                                                                if (!j.n()) break block62;
                                                                                if (h.X[i.h][0] == -1) break block63;
                                                                                if (i.h == 0) {
                                                                                    v0 = this;
                                                                                    v1 = l.o;
                                                                                } else {
                                                                                    v0 = this;
                                                                                    v1 = l.p;
                                                                                }
                                                                                v0.a(v1, 64);
                                                                                v2 = 2;
                                                                                break block64;
                                                                            }
                                                                            if (j.o()) {
                                                                                this.a((int)i.g);
                                                                                return;
                                                                            }
                                                                            break block63;
                                                                        }
                                                                        if (i.e != 1) break block65;
                                                                        if (j.i(8)) {
                                                                            if (i.h + 1 < 18) {
                                                                                i.h = (byte)(i.h + 1);
                                                                                return;
                                                                            }
                                                                            if (i.h / 6 + i.i + 1 < 5) {
                                                                                i.h = (byte)(i.h - 5);
                                                                                i.i = (byte)(i.i + 1);
                                                                                return;
                                                                            }
                                                                        } else if (j.i(4)) {
                                                                            if (i.h > 0) {
                                                                                i.h = (byte)(i.h - 1);
                                                                                return;
                                                                            }
                                                                            if (i.i > 0) {
                                                                                i.h = (byte)(i.h + 5);
                                                                                i.i = (byte)(i.i - 1);
                                                                                return;
                                                                            }
                                                                        } else if (j.i(2)) {
                                                                            if (i.h + 6 < 18) {
                                                                                i.h = (byte)(i.h + 6);
                                                                                return;
                                                                            }
                                                                            if (i.h / 6 + i.i + 1 < 5) {
                                                                                i.i = (byte)(i.i + 1);
                                                                                return;
                                                                            }
                                                                        } else {
                                                                            if (j.i(1)) {
                                                                                if (i.h - 6 >= 0) {
                                                                                    i.h = (byte)(i.h - 6);
                                                                                    return;
                                                                                }
                                                                                if (i.i > 0) {
                                                                                    i.i = (byte)(i.i - 1);
                                                                                    return;
                                                                                }
                                                                                i.b(0);
                                                                                i.h = (byte)3;
                                                                                return;
                                                                            }
                                                                            if (j.n()) {
                                                                                var1_1 = i.i * 6 + i.h;
                                                                                var2_5 = h.W[var1_1][0];
                                                                                if (var2_5 != -1) {
                                                                                    this.a(l.m, 64);
                                                                                    v3 = 4;
                                                                                } else {
                                                                                    this.a(l.n, 64);
                                                                                    v3 = 5;
                                                                                }
                                                                                i.b(v3);
                                                                                return;
                                                                            }
                                                                            if (j.o()) {
                                                                                this.a((int)i.g);
                                                                                return;
                                                                            }
                                                                        }
                                                                        break block63;
                                                                    }
                                                                    if (i.e != 2) break block66;
                                                                    this.d();
                                                                    if (i.b() != 0) break block63;
                                                                    if (i.c() == -1) break block67;
                                                                    var1_2 = false;
                                                                    if (i.c() != 0) break block68;
                                                                    i.b(3);
                                                                    var2_6 = i.a(h.X[i.h]);
                                                                    var3_8 = false;
                                                                    h.a(var2_6, 156);
                                                                    break block63;
                                                                }
                                                                if (i.c() == 1) {
                                                                    if (h.e(i.h)) {
                                                                        v4 = 0;
                                                                    } else {
                                                                        this.a(l.q[0]);
                                                                        v4 = 7;
                                                                    }
                                                                    i.b(v4);
                                                                }
                                                                return;
                                                            }
                                                            v2 = 0;
                                                            break block64;
                                                        }
                                                        if (i.e != 3) break block69;
                                                        h.h();
                                                        if (!j.n() && !j.o()) break block63;
                                                        v2 = 0;
                                                        break block64;
                                                    }
                                                    if (i.e != 4) break block70;
                                                    this.d();
                                                    if (i.b() != 0) break block63;
                                                    if (i.c() == -1) break block71;
                                                    var1_3 = i.i * 6 + i.h;
                                                    var2_7 = h.W[var1_3][0];
                                                    if (i.c() != 0) break block72;
                                                    h.d(var1_3);
                                                    i.b(0);
                                                    i.h = (byte)f.c(var2_7);
                                                    break block63;
                                                }
                                                if (i.c() != 1) break block73;
                                                i.b(6);
                                                var3_9 = i.a(h.W[var1_3]);
                                                h.a(var3_9, 156);
                                                break block63;
                                            }
                                            if (i.c() != 2) break block74;
                                            h.g();
                                            i.b(1);
                                            break block63;
                                        }
                                        if (i.c() == 3) {
                                            i.b(8);
                                            this.a(f.aB[var2_7], l.q[1]);
                                        }
                                        return;
                                    }
                                    v2 = 1;
                                    break block64;
                                }
                                if (i.e != 5) break block75;
                                this.d();
                                if (i.b() != 0) break block63;
                                if (i.c() == -1) break block76;
                                if (i.c() != 0) break block63;
                                h.g();
                                ** GOTO lbl-1000
                            }
                            v2 = 1;
                            break block64;
                        }
                        if (i.e != 6) break block77;
                        h.h();
                        if (!j.n() && !j.o()) break block63;
                        v2 = 1;
                        break block64;
                    }
                    if (i.e != 7) break block78;
                    this.d();
                    if (!j.n() && !j.o()) break block63;
                    v2 = 0;
                    break block64;
                }
                if (i.e == 8) {
                    this.d();
                    if (i.b() == 0) {
                        ** if (i.c() == -1) goto lbl-1000
lbl-1000:
                        // 1 sources

                        {
                            if (i.c() == 0) {
                                var1_4 = i.i * 6 + i.h;
                                h.W[var1_4][0] = -1;
                                h.W[var1_4][1] = -1;
                                h.W[var1_4][2] = -1;
                                h.W[var1_4][3] = -1;
                            } else {
                                ** GOTO lbl225
                            }
lbl225:
                            // 3 sources

                            v2 = 1;
                            ** GOTO lbl229
                        }
                    }
                }
                break block63;
lbl-1000:
                // 2 sources

                {
                    v2 = 1;
                }
            }
            i.b(v2);
        }
    }

    private void c(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        block48: {
            String string;
            int n10;
            int n11;
            int n12;
            int n13;
            block52: {
                int n14;
                Graphics graphics2;
                block50: {
                    int n15;
                    block51: {
                        block49: {
                            int n16;
                            int[][] nArray;
                            graphics.setClip(0, 0, f.ad, f.ae);
                            if (e != 3 && e != 6) break block48;
                            n13 = n + 7;
                            n12 = n2 + 7;
                            n11 = n3 - 14;
                            n10 = 0;
                            i.a(graphics, n13, n12, n11, 20, 4);
                            if (e == 3) {
                                nArray = h.X;
                                n16 = h;
                            } else {
                                n15 = i * 6 + h;
                                nArray = h.W;
                                n16 = n15;
                            }
                            int n17 = nArray[n16][0];
                            string = f.aB[n17];
                            n15 = n17 % 3;
                            if (n15 != 0) break block49;
                            graphics2 = graphics;
                            n14 = 0xFFFFFF;
                            break block50;
                        }
                        if (n15 != 1) break block51;
                        graphics2 = graphics;
                        n14 = 21670;
                        break block50;
                    }
                    if (n15 != 2) break block52;
                    graphics2 = graphics;
                    n14 = 0xFF00FF;
                }
                graphics2.setColor(n14);
            }
            graphics.drawString(string, n13 + (n11 >> 1), n12 + b, 17);
            n10 = n4 - 7 - 20 - 4 - 7;
            i.a(graphics, n13, n12 += 24, n11, n10, 2);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.b(graphics, n13, n12, n11, n10);
            return;
        }
        int n18 = n + 7;
        int n19 = n2 + 7;
        int n20 = n3 - 14;
        boolean bl = false;
        i.a(graphics, n18, n19, n20, 100, 3);
        k.k.S.a(graphics, 0, n18 + 45, n19 + 80, false, false);
        boolean bl2 = false;
        boolean bl3 = false;
        if (!f && e == 0 && h == 1) {
            bl3 = true;
        }
        i.a(graphics, n18 + 33, n19 + 10, 22, 22, bl3, true);
        if (h.X[1][0] != -1) {
            h.a(graphics, n18 + 33 + 11, n19 + 10 + 11, h.X[1]);
        }
        bl3 = false;
        if (!f && e == 0 && h == 0) {
            bl3 = true;
        }
        i.a(graphics, n18 + 7, n19 + 40, 22, 22, bl3, true);
        if (h.X[0][0] != -1) {
            h.a(graphics, n18 + 7 + 11, n19 + 40 + 11, h.X[0]);
        }
        bl3 = false;
        if (!f && e == 0 && h == 2) {
            bl3 = true;
        }
        i.a(graphics, n18 + 59, n19 + 40, 22, 22, bl3, true);
        if (h.X[2][0] != -1) {
            h.a(graphics, n18 + 59 + 11, n19 + 40 + 11, h.X[2]);
        }
        bl3 = false;
        if (!f && e == 0 && h == 3) {
            bl3 = true;
        }
        i.a(graphics, n18 + 7, n19 + 70, 22, 22, bl3, true);
        if (h.X[3][0] != -1) {
            h.a(graphics, n18 + 7 + 11, n19 + 70 + 11, h.X[3]);
        }
        bl3 = false;
        if (!f && e == 0 && h == 4) {
            bl3 = true;
        }
        i.a(graphics, n18 + 59, n19 + 70, 22, 22, bl3, true);
        if (h.X[4][0] != -1) {
            h.a(graphics, n18 + 59 + 11, n19 + 70 + 11, h.X[4]);
        }
        n18 += 90;
        n19 += b;
        int n21 = h.K;
        int n22 = h.L;
        int n23 = h.M;
        int n24 = h.O;
        int n25 = h.P;
        if (e == 1 && (n9 = h.W[n8 = i * 6 + h][0]) != -1) {
            int n26;
            int n27;
            n7 = f.c(n9);
            n6 = h.X[n7][0];
            if (n6 != -1) {
                n21 -= f.aC[n6 * 9 + 1];
                n22 -= f.aC[n6 * 9 + 2];
                n23 -= f.aC[n6 * 9 + 3];
                n24 -= f.aC[n6 * 9 + 4] * 10;
                n25 -= f.aC[n6 * 9 + 5];
                for (n5 = 1; n5 < 4; ++n5) {
                    n27 = h.X[n7][n5];
                    if (n27 == -1) continue;
                    n26 = f.b(n27);
                    if (n26 == 4) {
                        n21 -= f.aA[n27 * 3 + 1];
                        continue;
                    }
                    if (n26 == 5) {
                        n22 -= f.aA[n27 * 3 + 1];
                        continue;
                    }
                    if (n26 == 6) {
                        n23 -= f.aA[n27 * 3 + 1];
                        continue;
                    }
                    if (n26 == 7) {
                        n24 -= f.aA[n27 * 3 + 1] * 10;
                        continue;
                    }
                    if (n26 != 8) continue;
                    n25 -= f.aA[n27 * 3 + 1];
                }
            }
            n21 += f.aC[n9 * 9 + 1];
            n22 += f.aC[n9 * 9 + 2];
            n23 += f.aC[n9 * 9 + 3];
            n24 += f.aC[n9 * 9 + 4] * 10;
            n25 += f.aC[n9 * 9 + 5];
            for (n5 = 1; n5 < 4; ++n5) {
                n27 = h.W[n8][n5];
                if (n27 == -1) continue;
                n26 = f.b(n27);
                if (n26 == 4) {
                    n21 += f.aA[n27 * 3 + 1];
                    continue;
                }
                if (n26 == 5) {
                    n22 += f.aA[n27 * 3 + 1];
                    continue;
                }
                if (n26 == 6) {
                    n23 += f.aA[n27 * 3 + 1];
                    continue;
                }
                if (n26 == 7) {
                    n24 += f.aA[n27 * 3 + 1] * 10;
                    continue;
                }
                if (n26 != 8) continue;
                n25 += f.aA[n27 * 3 + 1];
            }
        }
        for (n8 = 0; n8 < 5; ++n8) {
            String string;
            block71: {
                String string2;
                block57: {
                    int n28;
                    StringBuffer stringBuffer;
                    block70: {
                        block74: {
                            int n29;
                            block73: {
                                block72: {
                                    block66: {
                                        block69: {
                                            int n30;
                                            block68: {
                                                block67: {
                                                    block62: {
                                                        block65: {
                                                            int n31;
                                                            block64: {
                                                                block63: {
                                                                    block58: {
                                                                        block61: {
                                                                            int n32;
                                                                            block60: {
                                                                                block59: {
                                                                                    block53: {
                                                                                        block56: {
                                                                                            int n33;
                                                                                            block55: {
                                                                                                block54: {
                                                                                                    graphics.setColor(0xFFFFFF);
                                                                                                    h.a(graphics, c[3], n18, n19 + 2 - 1, 150 + n8 * 30, 0, 30, 16);
                                                                                                    graphics.setClip(0, 0, f.ad, f.ae);
                                                                                                    string = "";
                                                                                                    n7 = 0;
                                                                                                    if (n8 != 0) break block53;
                                                                                                    if (n21 <= h.K) break block54;
                                                                                                    n33 = 1;
                                                                                                    break block55;
                                                                                                }
                                                                                                if (n21 >= h.K) break block56;
                                                                                                n33 = 2;
                                                                                            }
                                                                                            n7 = n33;
                                                                                        }
                                                                                        string2 = String.valueOf(n21);
                                                                                        break block57;
                                                                                    }
                                                                                    if (n8 != 1) break block58;
                                                                                    if (n22 <= h.L) break block59;
                                                                                    n32 = 1;
                                                                                    break block60;
                                                                                }
                                                                                if (n22 >= h.L) break block61;
                                                                                n32 = 2;
                                                                            }
                                                                            n7 = n32;
                                                                        }
                                                                        string2 = String.valueOf(n22);
                                                                        break block57;
                                                                    }
                                                                    if (n8 != 2) break block62;
                                                                    if (n23 <= h.M) break block63;
                                                                    n31 = 1;
                                                                    break block64;
                                                                }
                                                                if (n23 >= h.M) break block65;
                                                                n31 = 2;
                                                            }
                                                            n7 = n31;
                                                        }
                                                        string2 = String.valueOf(n23);
                                                        break block57;
                                                    }
                                                    if (n8 != 3) break block66;
                                                    if (n24 <= h.O) break block67;
                                                    n30 = 1;
                                                    break block68;
                                                }
                                                if (n24 >= h.O) break block69;
                                                n30 = 2;
                                            }
                                            n7 = n30;
                                        }
                                        stringBuffer = new StringBuffer();
                                        n28 = n24 / 10;
                                        break block70;
                                    }
                                    if (n8 != 4) break block71;
                                    if (n25 <= h.P) break block72;
                                    n29 = 1;
                                    break block73;
                                }
                                if (n25 >= h.P) break block74;
                                n29 = 2;
                            }
                            n7 = n29;
                        }
                        stringBuffer = new StringBuffer();
                        n28 = n25;
                    }
                    string2 = stringBuffer.append(String.valueOf(n28)).append("%").toString();
                }
                string = string2;
            }
            n6 = n18 + 70;
            h.a(graphics, n6, n19 + 6, string, -1, n7);
            n19 += 20;
        }
        n18 = n + 7;
        n19 = n2 + n4 - 70 - 7;
        i.a(graphics, n18, n19, n20, 70, 4);
        ++n19;
        n18 += 5;
        for (n9 = 0; n9 < 3; ++n9) {
            for (n7 = 0; n7 < 6; ++n7) {
                n6 = 0;
                if (!f && e == 1 && h == n9 * 6 + n7) {
                    n6 = 1;
                }
                n5 = 0;
                if ((i + n9) * 6 + n7 < l.a) {
                    n5 = 1;
                }
                i.a(graphics, n18 + n7 * 23, n19 + n9 * 23, 22, 22, n6 != 0, n5 != 0);
                int[] nArray = h.W[(i + n9) * 6 + n7];
                if (nArray[0] == -1) continue;
                h.a(graphics, n18 + n7 * 23 + 11, n19 + n9 * 23 + 11, nArray);
            }
        }
        h.a(graphics, n + n3 - 20, n19, 7, 69, 3, 5, (int)i);
        if (e == 2 || e == 4 || e == 5 || e == 7 || e == 8) {
            this.b(graphics);
        }
    }

    private void n() {
        block44: {
            int n;
            block51: {
                block54: {
                    block53: {
                        block52: {
                            block45: {
                                block46: {
                                    block50: {
                                        int n2;
                                        block48: {
                                            boolean bl;
                                            byte by;
                                            block49: {
                                                block47: {
                                                    block37: {
                                                        int n3;
                                                        block39: {
                                                            int n4;
                                                            block42: {
                                                                block43: {
                                                                    block40: {
                                                                        block41: {
                                                                            block38: {
                                                                                if (e != 0) break block37;
                                                                                if (!j.i(1)) break block38;
                                                                                if ((h = (byte)(h - 1)) < 0) {
                                                                                    h = 0;
                                                                                    f = true;
                                                                                }
                                                                                break block39;
                                                                            }
                                                                            if (!j.i(2)) break block40;
                                                                            if ((h = (byte)(h + 1)) >= 4) {
                                                                                h = (byte)3;
                                                                            }
                                                                            if (l.U[h * 9 + i] != -1) break block39;
                                                                            if (h != 2) break block41;
                                                                            n4 = i <= 1 ? 1 : (i <= 4 ? 3 : 6);
                                                                            break block42;
                                                                        }
                                                                        if (h != 3) break block39;
                                                                        n4 = 3;
                                                                        break block42;
                                                                    }
                                                                    if (!j.i(4)) break block43;
                                                                    n3 = 1;
                                                                    for (int i2 = i - 1; i2 >= 0; --i2) {
                                                                        int n5 = h * 9 + i2;
                                                                        if (l.U[n5] == -1) {
                                                                            ++n3;
                                                                            continue;
                                                                        }
                                                                        i = (byte)(i - n3);
                                                                        break;
                                                                    }
                                                                    if (i < 0) {
                                                                        i = 0;
                                                                    }
                                                                    break block39;
                                                                }
                                                                if (!j.i(8)) break block39;
                                                                n3 = 1;
                                                                for (int i3 = i + 1; i3 < 9; ++i3) {
                                                                    int n6 = h * 9 + i3;
                                                                    if (l.U[n6] == -1) {
                                                                        ++n3;
                                                                        continue;
                                                                    }
                                                                    i = (byte)(i + n3);
                                                                    break;
                                                                }
                                                                if (i < 9) break block39;
                                                                n4 = 8;
                                                            }
                                                            i = (byte)n4;
                                                        }
                                                        if (j.n()) {
                                                            String[] stringArray;
                                                            i i4;
                                                            n3 = l.U[h * 9 + i];
                                                            if (l.S[n3 * 4 + 0] == 1) {
                                                                i4 = this;
                                                                stringArray = l.r;
                                                            } else {
                                                                i4 = this;
                                                                String[] stringArray2 = new String[2];
                                                                stringArray2[0] = l.r[0];
                                                                stringArray = stringArray2;
                                                                stringArray2[1] = l.r[1];
                                                            }
                                                            i4.a(stringArray, 84);
                                                            i.b(1);
                                                        }
                                                        if (j.o()) {
                                                            this.a((int)g);
                                                            return;
                                                        }
                                                        break block44;
                                                    }
                                                    if (e != 1) break block45;
                                                    this.d();
                                                    if (i.b() != 0) break block44;
                                                    if (i.c() == -1) break block46;
                                                    int n7 = h * 9 + i;
                                                    by = l.U[n7];
                                                    bl = true;
                                                    for (int i5 = 0; i5 < l.V[n7].length; ++i5) {
                                                        byte by2 = l.V[n7][i5];
                                                        byte by3 = l.U[by2];
                                                        if (l.T[by3] != 0) continue;
                                                        bl = false;
                                                        break;
                                                    }
                                                    if (i.c() != 0) break block47;
                                                    if (!bl) {
                                                        this.a(l.t[0]);
                                                        n2 = 5;
                                                    } else if (h.U <= 0) {
                                                        this.a(l.t[1]);
                                                        n2 = 5;
                                                    } else if (l.T[by] >= 9) {
                                                        this.a(l.t[2]);
                                                        n2 = 5;
                                                    } else {
                                                        this.a(l.Q[by << 1], l.t[3]);
                                                        n2 = 4;
                                                    }
                                                    break block48;
                                                }
                                                if (i.c() != 1) break block49;
                                                i.b(2);
                                                h.a(l.Q[(by << 1) + 1], 156);
                                                break block44;
                                            }
                                            if (i.c() != 2) break block50;
                                            if (l.T[by] < 1) {
                                                this.a(l.t[4]);
                                                n2 = 5;
                                            } else if (!bl) {
                                                this.a(l.t[0]);
                                                n2 = 5;
                                            } else {
                                                this.a(l.s, 64);
                                                n2 = 3;
                                            }
                                        }
                                        i.b(n2);
                                    }
                                    return;
                                }
                                n = 0;
                                break block51;
                            }
                            if (e != 2) break block52;
                            h.h();
                            if (!j.n() && !j.o()) break block44;
                            n = 0;
                            break block51;
                        }
                        if (e != 3) break block53;
                        this.d();
                        if (i.b() != 0) break block44;
                        if (i.c() != -1) {
                            byte by = l.U[h * 9 + i];
                            h.a(i.c(), (int)by);
                        }
                        n = 0;
                        break block51;
                    }
                    if (e != 4) break block54;
                    this.d();
                    if (i.b() != 0) break block44;
                    if (i.c() != -1) {
                        byte by = l.U[h * 9 + i];
                        if (i.c() == 0 && h.U > 0 && l.T[by] < 9) {
                            byte by4 = by;
                            l.T[by4] = (byte)(l.T[by4] + 1);
                            h.U = (short)(h.U - 1);
                            h.e();
                        }
                    }
                    n = 0;
                    break block51;
                }
                if (e != 5) break block44;
                this.d();
                if (i.b() != 0) break block44;
                n = 0;
            }
            i.b(n);
        }
    }

    private void d(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        byte by = l.U[h * 9 + i];
        if (e == 2) {
            int n5 = n + 7;
            int n6 = n2 + 7;
            int n7 = n3 - 14;
            int n8 = 0;
            i.a(graphics, n5, n6, n7, 20, 4);
            graphics.setColor(0xFFFFFF);
            graphics.drawString(l.Q[by << 1], n5 + (n7 >> 1), n6 + b, 17);
            n8 = n4 - 7 - 20 - 4 - 7;
            i.a(graphics, n5, n6 += 24, n7, n8, 2);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.b(graphics, n5, n6, n7, n8);
            return;
        }
        int n9 = 0;
        boolean bl = false;
        int n10 = n + n3 - 116 - 7;
        int n11 = n2 + 7;
        i.a(graphics, n10, n11, 116, 20, 3);
        h.a(graphics, n10 + 2, n11 += 6, "SKILL POINT");
        h.a(graphics, n10 + 116 - 1, n11 + 1, String.valueOf(h.U), -1, 0);
        n10 = n + 7;
        n11 = n2 + 7 + 20 + 7 + 3;
        n9 = n3 - 14;
        i.a(graphics, n10, n11, n9, 114, 3);
        this.e(graphics, n10, n11, n9, 114);
        n11 = n2 + n4 - 7 - 20;
        n10 = n + (n3 - 140 >> 1);
        i.a(graphics, n10, n11, 140, 20, 4);
        graphics.setColor(0xFFFFFF);
        graphics.drawString(l.Q[by << 1], n10 + 70, n11 + b, 17);
        if (e == 1 || e == 3 || e == 4 || e == 5) {
            this.b(graphics);
        }
    }

    private void e(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = n2 + (n4 - 24 >> 1) - h * 64;
        p = p + n5 >> 1;
        int n6 = n + (n3 - 24 >> 1) - i * 40;
        int n7 = o = o + n6 >> 1;
        int n8 = p;
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int i3 = 0; i3 < 9; ++i3) {
                graphics.setClip(n, n2, n3, n4);
                int n9 = i2 * 9 + i3;
                byte by = l.U[n9];
                if (by != -1) {
                    int n10;
                    int n11;
                    int n12;
                    Graphics graphics2;
                    int n13;
                    int n14;
                    int n15;
                    if (by != l.Q.length / 2 - 1) {
                        int n16;
                        int n17;
                        int n18;
                        Graphics graphics3;
                        if (l.T[by] == 0) {
                            graphics3 = graphics;
                            n18 = 88;
                            n17 = 92;
                            n16 = 92;
                        } else {
                            graphics3 = graphics;
                            n18 = 226;
                            n17 = 233;
                            n16 = 29;
                        }
                        graphics3.setColor(n18, n17, n16);
                        i.f(graphics, n7 + 12, n8 + 24, n7 + 12, n8 + 24 + 20);
                    }
                    boolean bl = true;
                    for (n15 = 0; n15 < l.V[n9].length; ++n15) {
                        n14 = l.V[n9][n15];
                        n13 = l.U[n14];
                        if (l.T[n13] != 0) continue;
                        bl = false;
                        break;
                    }
                    if (!bl) {
                        graphics2 = graphics;
                        n12 = 88;
                        n11 = 92;
                        n10 = 92;
                    } else {
                        graphics2 = graphics;
                        n12 = 226;
                        n11 = 233;
                        n10 = 29;
                    }
                    graphics2.setColor(n12, n11, n10);
                    if (l.V[n9].length > 0) {
                        n15 = Integer.MAX_VALUE;
                        n14 = -2147483647;
                        n13 = 0;
                        for (int i4 = 0; i4 < l.V[n9].length; ++i4) {
                            byte by2 = l.V[n9][i4];
                            n13 = by2 / 9 * 64 + p;
                            int n19 = by2 % 9 * 40 + o;
                            if (n19 > n14) {
                                n14 = n19;
                            }
                            if (n19 >= n15) continue;
                            n15 = n19;
                        }
                        i.f(graphics, n15 + 12, n13 + 24 + 20, n14 + 12 + 1, n13 + 24 + 20);
                        i.f(graphics, n7 + 12, n8 - 1, n7 + 12, n8 - 20);
                    }
                    this.a(graphics, (int)by, n7, n8, bl);
                }
                n7 += 40;
            }
            n8 += 64;
            n7 = o;
        }
        if (!f) {
            this.a(graphics, o + i * 40 - 12, p + h * 64 + 12, true);
        }
    }

    private static void f(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8;
        Graphics graphics2;
        graphics.drawLine(n, n2, n3, n4);
        if (n == n3) {
            graphics2 = graphics;
            n8 = n + 1;
            n7 = n2;
            n6 = n3 + 1;
            n5 = n4;
        } else if (n2 == n4) {
            graphics2 = graphics;
            n8 = n;
            n7 = n2 + 1;
            n6 = n3;
            n5 = n4 + 1;
        } else {
            graphics2 = graphics;
            n8 = n + 1;
            n7 = n2 - 1;
            n6 = n3 + 1;
            n5 = n4 - 1;
        }
        graphics2.drawLine(n8, n7, n6, n5);
    }

    private void a(Graphics graphics, int n, int n2, int n3, boolean bl) {
        int n4;
        int n5;
        int n6;
        Graphics graphics2;
        graphics.setColor(0);
        graphics.drawRect(n2, n3, 24, 24);
        graphics.setColor(0xFFFFFF);
        graphics.drawRect(n2 + 1, n3 + 1, 22, 22);
        n2 += 2;
        n3 += 2;
        if (bl) {
            graphics2 = graphics;
            n6 = 216;
            n5 = 200;
            n4 = 160;
        } else {
            graphics2 = graphics;
            n6 = 157;
            n5 = 8;
            n4 = 13;
        }
        graphics2.setColor(n6, n5, n4);
        graphics.fillRect(n2, n3, 20, 20);
        graphics.clipRect(n2 += 2, n3 += 2, 16, 16);
        graphics.drawImage(h.f[5], n2, n3 - n * 16, 20);
        if (l.T[n] >= 0) {
            graphics.clipRect(n2 += 11, n3 += 9, 5, 7);
            graphics.drawImage(h.f[6], n2 - l.T[n] * 5, n3, 20);
        }
    }

    private static void o() {
        f = true;
    }

    private void g(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        int n5 = n + 7;
        int n6 = n2 + 7;
        int n7 = n3 - 14;
        int n8 = 0;
        i.a(graphics, n5, n6, n7, 20, 4);
        graphics.setColor(0xFFFFFF);
        String string = "\u672a\u77e5\u533a\u57df";
        if (k.a >= 0 && k.a < l.D.length) {
            string = l.D[k.a];
        }
        graphics.drawString(string, n5 + (n7 >> 1), n6 + b, 17);
        n8 = n4 - 7 - 20 - 4 - 7;
        i.a(graphics, n5, n6 += 24, n7, n8, 2);
        if (h.af != null) {
            int n9 = h.af.getWidth();
            int n10 = h.af.getHeight();
            int n11 = n5 + (n7 - n9 * 2 >> 1);
            int n12 = n6 + (n8 - n10 * 2 >> 1);
            for (int i2 = 0; i2 < n10; ++i2) {
                for (int i3 = 0; i3 < n9; ++i3) {
                    int n13 = n11 + (i3 << 1);
                    int n14 = n12 + (i2 << 1);
                    graphics.setClip(n13, n14, 1, 1);
                    graphics.drawImage(h.af, n13 - i3, n14 - i2, 20);
                    graphics.setClip(n13 + 1, n14, 1, 1);
                    graphics.drawImage(h.af, n13 - i3 + 1, n14 - i2, 20);
                    graphics.setClip(n13, n14 + 1, 1, 1);
                    graphics.drawImage(h.af, n13 - i3, n14 - i2 + 1, 20);
                    graphics.setClip(n13 + 1, n14 + 1, 1, 1);
                    graphics.drawImage(h.af, n13 - i3 + 1, n14 - i2 + 1, 20);
                }
            }
            if ((j.aX & 2) != 0) {
                graphics.setClip(0, 0, f.ad, f.ae);
                graphics.setColor(0xFF0000);
                graphics.fillRect(n11 + (k.k.s / f.af << 1), n12 + (k.k.t / f.af << 1), 3, 3);
            }
            return;
        }
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(0);
        graphics.drawString("\u65e0", n5 + (n7 >> 1), n6 + (n8 >> 1), 17);
    }

    private void p() {
        h.a(l.G[(j.aY << 1) + 1], 156);
    }

    private void q() {
        block5: {
            boolean bl;
            block4: {
                block2: {
                    block3: {
                        h.h();
                        if (!h.as && !h.ar || j.i(1) && !h.as) break block2;
                        if (!j.i(4) && !j.i(8)) break block3;
                        bl = true;
                        break block4;
                    }
                    if (!j.n() && !j.o()) break block5;
                }
                bl = true;
            }
            f = bl;
        }
    }

    private void h(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        int n5 = n + 7;
        int n6 = n2 + 7;
        int n7 = n3 - 14;
        int n8 = 0;
        i.a(graphics, n5, n6, n7, 20, 4);
        graphics.setColor(0xFFFFFF);
        String string = l.G[j.aY << 1];
        graphics.drawString(string, n5 + (n7 >> 1), n6 + b, 17);
        n8 = n4 - 7 - 20 - 4 - 7;
        i.a(graphics, n5, n6 += 24, n7, n8, 2);
        graphics.setClip(0, 0, f.ad, f.ae);
        h.b(graphics, n5, n6, n7, n8);
    }

    private void r() {
        block17: {
            int n;
            block27: {
                block31: {
                    block30: {
                        block29: {
                            block28: {
                                block26: {
                                    block14: {
                                        block16: {
                                            int n2;
                                            i i2;
                                            block20: {
                                                block25: {
                                                    block24: {
                                                        block23: {
                                                            block22: {
                                                                block21: {
                                                                    block18: {
                                                                        block19: {
                                                                            block15: {
                                                                                if (e != 0) break block14;
                                                                                if (!j.i(2)) break block15;
                                                                                if (h >= 4) break block16;
                                                                                h = (byte)(h + 1);
                                                                                break block17;
                                                                            }
                                                                            if (!j.i(1)) break block18;
                                                                            if (h != 0) break block19;
                                                                            i2 = this;
                                                                            n2 = g;
                                                                            break block20;
                                                                        }
                                                                        if (h <= 0) break block16;
                                                                        h = (byte)(h - 1);
                                                                        break block17;
                                                                    }
                                                                    if (!j.o()) break block21;
                                                                    i2 = this;
                                                                    n2 = g;
                                                                    break block20;
                                                                }
                                                                if (!j.n()) break block16;
                                                                if (h != 1) break block22;
                                                                if (!h.aM) {
                                                                    h.f(7);
                                                                    return;
                                                                }
                                                                i = 0;
                                                                this.a(l.g[2]);
                                                                i.b(3);
                                                                break block17;
                                                            }
                                                            if (h != 2) break block23;
                                                            i.b(5);
                                                            break block17;
                                                        }
                                                        if (h != 3) break block24;
                                                        h.a("[C:0000FF]\u6e38\u620f\u4ecb\u7ecd[/C]:|10\u591a\u5e74\u524d\uff0c\u9b54\u738b\u5b87\u6625\u5c06\u7ef4\u8fbe\u5361\u4f0a\u59c6\u661f\u7403\u4e0a\u7684\u4eba\u7c7b\u7684\u5927\u90e8\u5206\u826f\u5fc3\u593a\u8d70\uff0c\u5e76\u4e14\u51e0\u4e4e\u628a\u4eba\u7c7b\u90fd\u53d8\u6210\u4e86\u6076\u9b54\u548c\u9b54\u7269\u3002\u7ecf\u8fc710\u591a\u5e74\u7684\u5c81\u6708\uff0c\u6211\u4eec\u7684\u7537\u4e3b\u89d2\u662f\u4ec5\u5b58\u4e0d\u591a\u7684\u4eba\u7c7b\uff0c\u4ed6\u7684\u517b\u6bcd\u8eab\u4e3a\u53ec\u5524\u5e08\uff0c\u6536\u96c6\u4e86\u53ec\u5524\u9b54\u738b\u5b87\u6625\u7684\u6750\u6599\uff0c\u5c06\u8981\u8fdb\u884c\u53ec\u5524\u4eea\u5f0f\u3002\u4f46\uff0c\u8c01\u77e5\u9053\uff0c\u9634\u5dee\u9633\u9519\u2026\u2026|[C:0000FF]\u6e38\u620f\u89c4\u5219[/C]:|\u6e38\u620f\u4e2d\u53ef\u8c03\u51fa\u6e38\u620f\u83dc\u5355\uff0c\u9009\u62e9\u72b6\u6001\u9009\u9879\u53ef\u4ee5\u7528\u6765\u63d0\u5347\u4f60\u7684\u5c5e\u6027(\u6bcf\u53471\u7ea7\u80fd\u83b7\u5f973\u70b9\u5c5e\u6027\u70b9\uff0c\u529b\u91cf\u53ef\u63d0\u9ad8\u653b\u51fb\u529b\uff0c\u4f53\u8d28\u53ef\u63d0\u9ad8\u9632\u5fa1\u529b\uff0c\u654f\u6377\u53ef\u63d0\u9ad8\u56de\u907f\u7387\uff0c\u6280\u5de7\u53ef\u63d0\u9ad8\u66b4\u51fb\u51e0\u7387\u4e0e\u66b4\u51fb\u500d\u6570)\uff0c\u9009\u62e9\u6b66\u5668\u9009\u9879\u53ef\u4ee5\u628a\u6b66\u5668\u88c5\u5907\u5230\u8eab\u4e0a\uff0c\u9009\u62e9\u9053\u5177\u9009\u9879\u53ef\u4ee5\u4f7f\u7528\u5df2\u83b7\u5f97\u7684\u9053\u5177\u6216\u8005\u5c06\u9053\u5177\u8bbe\u7f6e\u5230\u5feb\u6377\u952e\u4e0a\uff0c\u9009\u62e9\u6280\u80fd\u9009\u9879\u53ef\u4ee5\u5347\u7ea7\u559c\u6b22\u7684\u6280\u80fd\u6216\u8005\u5c06\u6280\u80fd\u8bbe\u7f6e\u5230\u5feb\u6377\u952e\u4e0a(\u6bcf\u5347\u4e00\u7ea7\u80fd\u83b7\u5f97\u4e00\u70b9\u6280\u80fd\u70b9)\uff0c\u9009\u62e9\u7cfb\u7edf\u9009\u9879\u5219\u53ef\u4ee5\u4fdd\u5b58\u6e38\u620f\u6216\u8bbe\u7f6e\u6e38\u620f\u6216\u67e5\u770b\u5e2e\u52a9\u6216\u9000\u51fa\u6e38\u620f\u3002|[C:0000FF]\u64cd\u4f5c\u65b9\u6cd5[/C]:|\u65b9\u5411\u952e\u63a7\u5236\u4eba\u7269\u79fb\u52a8\u3002\u5728\u6218\u6597\u72b6\u6001\u65f6\uff0c\u4e2d\u95f4\u786e\u8ba4\u952e\u53ef\u4ee5\u8fdb\u884c\u653b\u51fb\uff0c\u5c4f\u5e55\u4e0b\u65b9\u6709\u6280\u80fd\u5feb\u6377\u952e\u53ef\u4ee5\u70b9\u51fb\u4f7f\u7528\uff0c\u5206\u522b\u5bf9\u5e94\u6570\u5b571\u7684\u6846\u6570\u5b573\u7684\u6846\u6570\u5b577\u7684\u6846\u6570\u5b579\u7684\u6846\uff0c\u53ef\u4ee5\u7528\u6765\u8bbe\u7f6e\u6210\u6280\u80fd\u5feb\u6377\u952e\uff0c*\u7684\u6846#\u7684\u6846\u53ef\u4ee5\u7528\u6765\u8bbe\u7f6e\u6210\u9053\u5177\u5feb\u6377\u952e\u3002\u4e2d\u95f4\u786e\u8ba4\u952e\u53ef\u4ee5\u4e0e\u4eba\u7269\u5bf9\u8bdd\u3002", 157);
                                                        i.b(2);
                                                        break block17;
                                                    }
                                                    if (h != 4) break block25;
                                                    this.a(null, l.g[0]);
                                                    i.b(1);
                                                    break block17;
                                                }
                                                if (h != 0) break block16;
                                                i2 = this;
                                                n2 = 14;
                                            }
                                            i2.a(n2);
                                        }
                                        return;
                                    }
                                    if (e != 1) break block26;
                                    this.d();
                                    if (i.b() != 0) break block17;
                                    if (i.c() != -1) {
                                        if (i.c() == 0) {
                                            System.out.println("yes");
                                            h.b((byte)2);
                                        } else if (i.c() == 1) {
                                            System.out.println("no");
                                        }
                                    }
                                    n = 0;
                                    break block27;
                                }
                                if (e != 2) break block28;
                                h.h();
                                if (!j.o() && !j.n()) break block17;
                                n = 0;
                                break block27;
                            }
                            if (e != 3) break block29;
                            this.d();
                            i = (byte)(i + 1);
                            if (i > 4 || j.n()) {
                                h.j();
                                i.b(4);
                                this.a(l.g[3]);
                                return;
                            }
                            break block17;
                        }
                        if (e != 4) break block30;
                        this.d();
                        if (i.b() != 0) break block17;
                        break block31;
                    }
                    if (e != 5) break block17;
                    h.i();
                    if (!j.o() && !j.n()) break block17;
                }
                n = 0;
            }
            i.b(n);
        }
    }

    private void i(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        if (e == 2) {
            int n5 = n3 - 14;
            int n6 = n4 - 14;
            int n7 = n + 7;
            int n8 = n2 + 7;
            i.a(graphics, n7, n8, n5, n6, 2);
            h.b(graphics, n7, n8, n5, n6);
        } else if (e == 5) {
            int n9 = n + 7;
            int n10 = n2 + 7;
            int n11 = n3 - 14;
            int n12 = 0;
            i.a(graphics, n9, n10, n11, 20, 4);
            graphics.setColor(0);
            String string = l.f[2];
            graphics.drawString(string, n9 + (n11 >> 1), n10 + b, 17);
            n12 = n4 - 7 - 20 - 4 - 7;
            i.a(graphics, n9, n10 += 24, n11, n12, 2);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.a(graphics, f.ad >> 1, f.ae >> 1);
        } else {
            boolean bl = false;
            boolean bl2 = false;
            int n13 = n3 - 20;
            boolean bl3 = false;
            int n14 = n + 10;
            int n15 = n2 + (n4 - 160 >> 1);
            i.a(graphics, n14, n15, n13, 160, 3);
            n15 += 10;
            n14 += 10;
            n13 -= 20;
            for (int i2 = 0; i2 < 5; ++i2) {
                i.a(graphics, n14, n15, n13, 20, 4);
                if (!f && h == i2) {
                    i.a(graphics, n14, n15, n13, 20, 5);
                }
                graphics.setColor(0xFFFFFF);
                graphics.drawString(l.f[0 + i2], n + (n3 >> 1), n15 + b, 17);
                n15 += 30;
            }
        }
        if (e == 1 || e == 3 || e == 4) {
            this.b(graphics);
        }
    }

    /*
     * Unable to fully structure code
     */
    private static void a(Graphics var0, int var1_1, int var2_2, int var3_3, int var4_4, int var5_5) {
        var6_6 = 0xD8D8A8;
        var7_7 = 2703715;
        var8_8 = 14072452;
        switch (var5_5) {
            case 1: {
                var6_6 = 16767884;
                var7_7 = 10975288;
                v0 = 16767884;
                ** GOTO lbl34
            }
            case 2: {
                var6_6 = 16767884;
                var7_7 = 10975288;
                v0 = 16767884;
                ** GOTO lbl34
            }
            case 3: {
                var6_6 = 0xDDDDAA;
                var7_7 = 7891032;
                v0 = 6838344;
                ** GOTO lbl34
            }
            case 4: {
                var6_6 = 0xD8D8A8;
                var7_7 = 2703715;
                v0 = 6838344;
                ** GOTO lbl34
            }
            case 5: {
                var6_6 = 10975288;
                var7_7 = 0xFFFFFF;
                v0 = 10975288;
                ** GOTO lbl34
            }
            case 6: {
                var6_6 = 10975288;
                var7_7 = 5061169;
                v0 = 5061169;
lbl34:
                // 6 sources

                var8_8 = v0;
            }
        }
        var0.setClip(0, 0, f.ad, f.ae);
        var0.setColor(var7_7);
        var0.fillRoundRect(var1_1 - 3, var2_2 - 3, var3_3 + 6, var4_4 + 6, 3, 3);
        var0.setColor(var6_6);
        var0.fillRoundRect(var1_1 - 1, var2_2 - 1, var3_3 + 2, var4_4 + 2, 3, 3);
        var0.setColor(var8_8);
        var0.fillRoundRect(var1_1, var2_2, var3_3, var4_4, 3, 3);
    }

    private void b(Graphics graphics, int n, int n2, int n3, int n4, int n5) {
        i.a(graphics, n, n2 + 20, n3, n4 - 20, 6);
        int n6 = 5061169;
        int n7 = n3 / 8;
        int n8 = n;
        graphics.setClip(n, n2, n3, 20);
        for (int i2 = 0; i2 < 7; ++i2) {
            int n9;
            int n10;
            if (i2 == n5) {
                int n11;
                Graphics graphics2;
                i.j(graphics, n8, n2, n7 << 1, 20);
                graphics.setColor(n6);
                graphics.fillRect(n8, n2 + 20 - 2, n7 << 1, 2);
                if (f) {
                    graphics2 = graphics;
                    n11 = 0xFFFFFF;
                } else {
                    graphics2 = graphics;
                    n11 = 0;
                }
                graphics2.setColor(n11);
                h.a(graphics, c[3], n8 + n7 - 15, n2 + b, 300 + i2 * 30, 0, 30, 16);
                graphics.setClip(0, 0, f.ad, f.ae);
                n10 = n8;
                n9 = n7 << 1;
            } else {
                i.j(graphics, n8, n2, n7, 20);
                j.a(graphics, c[0], i2 * 16, 0, 16, 15, 0, n8 + (n7 - 16 >> 1), n2 + 2, 20);
                n10 = n8;
                n9 = n7;
            }
            n8 = n10 + n9;
        }
    }

    private static void j(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = 10975288;
        int n6 = 5061169;
        graphics.setClip(n, n2, n3, n4);
        graphics.setColor(n5);
        graphics.fillRoundRect(n, n2, n3, n4, 3, 3);
        graphics.fillRect(n, n2 + n4 - 1, n3, 1);
        graphics.setColor(n6);
        graphics.fillRoundRect(n + 2, n2 + 2, n3 - 4, n4 - 4, 3, 3);
    }

    private static void a(Graphics graphics, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        graphics.setClip(0, 0, f.ad, f.ae);
        if (bl2) {
            graphics.setColor(8);
            graphics.fillRect(n + 3, n2 + 3, n3 - 6, n4 - 6);
        } else {
            graphics.setColor(0xFF0000);
            graphics.fillRect(n + 3, n2 + 3, n3 - 6, n4 - 6);
            graphics.setColor(0);
            graphics.drawLine(n + 3, n2 + 3, n + n3 - 3, n2 + n4 - 3);
            graphics.drawLine(n + 3, n2 + n4 - 3, n + n3 - 3, n2 + 3);
        }
        graphics.setColor(7891032);
        graphics.drawRect(n + 2, n2, --n3 - 4, --n4);
        graphics.drawRect(n, n2 + 2, n3, n4 - 4);
        graphics.setColor(0xDDDDAA);
        graphics.drawRect(n + 1, n2 + 1, n3 - 2, n4 - 2);
        if (bl) {
            graphics.setColor(0xFFFF00);
            graphics.drawRect(n, n2, n3, n4);
            graphics.drawRect(n + 1, n2 + 1, n3 - 2, n4 - 2);
        }
    }

    public final void a(String string, String string2) {
        q = 0;
        if (string2 != null) {
            q = 1;
        }
        v = (short)200;
        t = null;
        u = null;
        if (string != null) {
            t = d.a(string, null, 190 - d.b);
        }
        if (string2 != null) {
            u = d.a(string2, null, 190 - d.b);
        }
        i.s();
    }

    public final void a(String string) {
        q = 0;
        v = (short)200;
        t = null;
        u = null;
        if (string != null) {
            t = d.a(string, null, 190 - d.b);
        }
        i.s();
    }

    private void a(String[] stringArray, int n) {
        q = (byte)3;
        v = (short)n;
        t = stringArray;
        u = null;
        i.s();
    }

    private void a(int n, int n2) {
        block5: {
            String string;
            block3: {
                block4: {
                    block2: {
                        u = null;
                        t = null;
                        if (n2 != 0) break block2;
                        q = (byte)4;
                        string = "\u8d2d\u4e70\u6570\u91cf";
                        break block3;
                    }
                    if (n2 != 1) break block4;
                    q = (byte)5;
                    string = "\u5356\u51fa\u6570\u91cf";
                    break block3;
                }
                if (n2 != 2) break block5;
                q = (byte)6;
                string = "\u4e22\u5f03\u6570\u91cf";
            }
            t = d.a(string, null, 190 - d.b);
        }
        v = (short)200;
        y = n;
        i.s();
        w = 1;
    }

    private static void s() {
        w = 0;
        r = 1;
        s = 0;
        x = -1;
    }

    private static void t() {
        t = null;
        u = null;
    }

    public static int b() {
        return r;
    }

    public static int c() {
        return x;
    }

    public final void d() {
        block23: {
            block24: {
                block26: {
                    int n;
                    byte by;
                    block29: {
                        block30: {
                            block27: {
                                block28: {
                                    block25: {
                                        block22: {
                                            ++s;
                                            if (r != 1) break block22;
                                            if (s > 2) {
                                                r = (byte)2;
                                                s = 0;
                                                return;
                                            }
                                            break block23;
                                        }
                                        if (r != 2) break block24;
                                        if (q != 0 && q != 1 && q != 2) break block25;
                                        if (j.i(2)) {
                                            System.out.println("yes");
                                            by = 1;
                                        } else if (j.i(1)) {
                                            System.out.println("no");
                                            by = 0;
                                        }
                                        break block26;
                                    }
                                    if (q != 3) break block27;
                                    if (!j.i(2)) break block28;
                                    if ((w = (byte)(w + 1)) < t.length) break block26;
                                    n = t.length - 1;
                                    break block29;
                                }
                                if (j.i(1) && (w = (byte)(w - 1)) < 0) {
                                    by = 0;
                                }
                                break block26;
                            }
                            if (q != 4 && q != 5 && q != 6) break block26;
                            if (!j.i(2)) break block30;
                            if ((w = (byte)(w - 1)) < 1) {
                                by = 1;
                            }
                            break block26;
                        }
                        if (!j.i(1)) break block26;
                        int n2 = 1;
                        if (q == 4) {
                            n2 = h.E / f.aA[y * 3 + 2];
                            int n3 = h.V.d(y);
                            int n4 = 0;
                            if (n3 >= 0) {
                                n4 = h.V.c(n3);
                            }
                            if (n2 > 99 - n4) {
                                n2 = 99 - n4;
                            }
                        } else if (q == 5) {
                            int n5 = h.V.d(y);
                            n2 = h.V.c(n5);
                        }
                        w = (byte)(w + 1);
                        if (w < n2) break block26;
                        n = n2;
                    }
                    by = w = (byte)n;
                }
                if (j.n()) {
                    if (q == 0) {
                        r = (byte)4;
                        s = 0;
                        return;
                    }
                    r = (byte)3;
                    s = 0;
                    x = w;
                    return;
                }
                if (j.o()) {
                    r = (byte)4;
                    s = 0;
                    x = -1;
                    return;
                }
                break block23;
            }
            if (r == 3) {
                if (s > 4) {
                    r = (byte)4;
                    s = 0;
                    return;
                }
            } else if (r == 4 && s > 2) {
                r = 0;
                s = 0;
                i.t();
            }
        }
    }

    public final void b(Graphics graphics) {
        block36: {
            int n;
            int n2;
            int n3;
            short s;
            block44: {
                int n4;
                block48: {
                    boolean bl;
                    int n5;
                    int n6;
                    Graphics graphics2;
                    i i2;
                    block47: {
                        int n7;
                        block46: {
                            block45: {
                                block43: {
                                    int n8;
                                    block38: {
                                        int n9;
                                        int n10;
                                        block39: {
                                            int n11;
                                            int n12;
                                            block41: {
                                                block42: {
                                                    block40: {
                                                        block37: {
                                                            if (r == 1 || r == 4) {
                                                                int n13 = v / 2;
                                                                boolean bl2 = false;
                                                                int n14 = i.s * n13;
                                                                if (r == 4) {
                                                                    n14 = (2 - i.s) * n13;
                                                                }
                                                                int n15 = f.ad - n14 >> 1;
                                                                int n16 = f.ae - 20 >> 1;
                                                                i.a(graphics, n15, n16, n14, 20, 0);
                                                                return;
                                                            }
                                                            if (r != 2 && r != 3) break block36;
                                                            n8 = 10;
                                                            if (q != 0 && q != 1) break block37;
                                                            if (t != null) {
                                                                n8 = 10 + (d.d + t.length * (d.d + d.c));
                                                            }
                                                            if (u != null) {
                                                                n8 += d.d + (2 + u.length) * (d.d + d.c);
                                                            }
                                                            if (t == null || u == null) break block38;
                                                            n10 = n8;
                                                            n9 = d.d;
                                                            break block39;
                                                        }
                                                        if (q != 2) break block40;
                                                        n8 = 10 + (d.d + d.c);
                                                        n8 += d.d;
                                                        n8 += 30;
                                                        n8 += d.d;
                                                        if (u == null) break block38;
                                                        n10 = n8;
                                                        n12 = d.d;
                                                        n11 = (2 + u.length) * (d.d + d.c);
                                                        break block41;
                                                    }
                                                    if (q != 3) break block42;
                                                    if (t == null) break block38;
                                                    n10 = 10;
                                                    n12 = d.d;
                                                    n11 = t.length * (d.d + d.c);
                                                    break block41;
                                                }
                                                if (q != 4 && q != 5 && q != 6) break block38;
                                                n8 = 10 + (d.d + d.c);
                                                n8 += d.d;
                                                n8 += d.d + d.c;
                                                n8 += d.d;
                                                n10 = n8 = (n8 += d.d + d.c) + d.d;
                                                n12 = d.d;
                                                n11 = d.c;
                                            }
                                            n9 = n12 + n11;
                                        }
                                        n8 = n10 + n9;
                                    }
                                    s = v;
                                    n3 = f.ad - s >> 1;
                                    n2 = f.ae - n8 >> 1;
                                    i.a(graphics, n3, n2, (int)s, n8, 0);
                                    if (i.s <= 0 && r != 3) break block36;
                                    if (q != 0 && q != 1) break block43;
                                    n2 += 5 + d.d;
                                    if (t != null) {
                                        for (n = 0; n < t.length; ++n) {
                                            i.a(graphics, t[n], n3 + (s >> 1), n2, 17, false);
                                            n2 += d.d + d.c;
                                        }
                                    }
                                    if (t != null && u != null) {
                                        i.a(graphics, n3 + 10, n2 - 1, s - 20);
                                        n2 += d.d;
                                    }
                                    break block44;
                                }
                                if (q == 2) break block44;
                                if (q != 3) break block45;
                                n2 += 5 + d.d;
                                if (t != null) {
                                    for (n = 0; n < t.length; ++n) {
                                        graphics.setClip(0, 0, f.ad, f.ae);
                                        if (r == 3 && w == n) {
                                            if ((i.s & 1) == 1) {
                                                i.a(graphics, t[n], n3 + (s >> 1), n2, 17, false);
                                            }
                                        } else {
                                            i.a(graphics, t[n], n3 + (s >> 1), n2, 17, false);
                                            if (w == n) {
                                                this.a(graphics, n3 + 2, n2 + 9, true);
                                            }
                                        }
                                        n2 += d.d + d.c;
                                    }
                                }
                                break block44;
                            }
                            if (q != 4 && q != 5 && q != 6) break block44;
                            n2 += 5 + d.d;
                            n = y;
                            n4 = f.aA[n * 3 + 2];
                            if (q == 5) {
                                n4 >>= 1;
                            }
                            h.b(graphics, n3 + 5 + d.d, n2 + (d.c - 14 >> 1), n);
                            i.a(graphics, f.az[n << 1], n3 + 5 + d.d + 14 + 2, n2, 20, false);
                            i.a(graphics, n3 + 10, (n2 += d.d + d.c) - 1, s - 20);
                            graphics.setClip(0, 0, f.ad, f.ae);
                            i.a(graphics, l.x[2], n3 + 5 + d.d, n2 += d.d, 20, false);
                            h.a(graphics, n3 + 5 + d.d + d.b * l.x[2].length(), n2 + (d.c >> 1) - 3, String.valueOf(h.E), 1, 0);
                            i.a(graphics, n3 + 10, (n2 += d.d + d.c) - 1, s - 20);
                            graphics.setClip(0, 0, f.ad, f.ae);
                            i.a(graphics, t[0], n3 + 5 + d.d + 14 + 2, n2 += d.d, 20, false);
                            int n17 = 1;
                            if (q == 4) {
                                n17 = h.E / f.aA[y * 3 + 2];
                                n7 = h.V.d(y);
                                int n18 = 0;
                                if (n7 >= 0) {
                                    n18 = h.V.c(n7);
                                }
                                if (n17 > 99 - n18) {
                                    n17 = 99 - n18;
                                }
                            } else if (q == 5) {
                                n7 = h.V.d(y);
                                n17 = h.V.c(n7);
                            }
                            h.a(graphics, n3 + s - 10, n2 + (d.c >> 1) - 3, w + "/" + n17, -1, 0);
                            graphics.setClip(0, 0, f.ad, f.ae);
                            n7 = n3 + s - 10 - 7 * String.valueOf(n17).length() - 21;
                            if (r != 3) {
                                if (w < n17) {
                                    d.b(graphics, n7 + 4, n2 + (d.c >> 1) - 3 - 7, 0, 0xFFFFFF, 0);
                                }
                                if (w > 1) {
                                    d.b(graphics, n7 + 4, n2 + (d.c >> 1) - 3 + 14, 4, 0xFFFFFF, 0);
                                }
                            }
                            if (r == 3) break block46;
                            i2 = this;
                            graphics2 = graphics;
                            n6 = n7 - 22;
                            n5 = n2 + (d.c >> 1) - 1;
                            bl = true;
                            break block47;
                        }
                        if ((i.s & 1) != 1) break block48;
                        i2 = this;
                        graphics2 = graphics;
                        n6 = n7 - 22;
                        n5 = n2 + (d.c >> 1) - 1;
                        bl = false;
                    }
                    i2.a(graphics2, n6, n5, bl);
                }
                i.a(graphics, n3 + 10, (n2 += d.d + d.c) - 1, s - 20);
                graphics.setClip(0, 0, f.ad, f.ae);
                i.a(graphics, l.x[3], n3 + 5 + d.d + 14 + 2, n2 += d.d, 20, false);
                h.a(graphics, n3 + s - 5 - 5, n2 + (d.c >> 1) - 3, String.valueOf(w * n4), -1, 0);
            }
            if (u != null) {
                for (n = 0; n < u.length; ++n) {
                    i.a(graphics, u[n], n3 + (s >> 1), n2, 17, false);
                    n2 += d.d + d.c;
                }
                if (r != 3 || w != 0 || (i.s & 1) == 1) {
                    i.a(graphics, "\u662f", n3 + (s >> 1), n2, 17, false);
                }
                n2 += d.d + d.c;
                if (r != 3 || w != 1 || (i.s & 1) == 1) {
                    i.a(graphics, "\u5426", n3 + (s >> 1), n2, 17, false);
                }
                n2 += d.c >> 1;
                if (w == 0) {
                    n2 -= d.d + d.c;
                }
                if (r != 3) {
                    this.a(graphics, n3 + (s >> 1) - d.b - 16, n2, true);
                }
            }
        }
    }

    private static void a(Graphics graphics, int n, int n2, int n3) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(144, 112, 72);
        graphics.drawLine(n, n2, n + n3, n2);
        graphics.setColor(184, 152, 104);
        graphics.drawLine(n, n2 + 1, n + n3, n2 + 1);
    }

    private static void a(Graphics graphics, String string, int n, int n2, int n3, boolean bl) {
        graphics.setClip(0, 0, f.ad, f.ae);
        int n4 = 12097640;
        int n5 = 5781512;
        if (bl) {
            n4 = 12626040;
            n5 = 10518600;
        }
        d.a(graphics, string, n, n2, n3, n4, n5, 0);
    }

    private void a(Graphics graphics, int n, int n2, boolean bl) {
        n2 -= 4;
        if (bl) {
            n -= j.aX & 3;
        }
        graphics.setClip(n, n2, 16, 11);
        graphics.drawImage(c[1], n, n2, 20);
    }

    private static String a(int[] nArray) {
        int n;
        StringBuffer stringBuffer = new StringBuffer();
        int n2 = nArray[0];
        short s = f.aC[n2 * 9 + 8];
        int n3 = f.c(n2);
        short s2 = f.aC[n2 * 9 + 1];
        short s3 = f.aC[n2 * 9 + 2];
        short s4 = f.aC[n2 * 9 + 3];
        short s5 = f.aC[n2 * 9 + 4];
        short s6 = f.aC[n2 * 9 + 5];
        short s7 = f.aC[n2 * 9 + 6];
        short s8 = f.aC[n2 * 9 + 7];
        int n4 = 0;
        if (n2 % 3 == 1) {
            n = 1;
        } else if (n2 % 3 == 2) {
            n = n4 = 2;
        }
        if (n4 != 0) {
            if (n4 == 1) {
                stringBuffer.append("[C:0054A6]");
            } else {
                stringBuffer.append("[C:FF00FF]");
            }
        }
        if (s > 0) {
            stringBuffer.append("(" + s + "\u5b54)");
        }
        if (n3 == 0) {
            stringBuffer.append("\u6b66\u5668|\u653b\u51fb\uff1a" + s2);
        } else if (n3 == 1) {
            stringBuffer.append("\u5934\u76d4|\u56de\u907f\uff1a" + s4);
        } else if (n3 == 2) {
            stringBuffer.append("\u9632\u5177|\u9632\u5fa1\uff1a" + s3);
        } else if (n3 == 3) {
            stringBuffer.append("\u978b\u5b50");
        } else if (n3 == 4) {
            stringBuffer.append("\u9970\u54c1");
        }
        stringBuffer.append("|\u9644\u52a0\u5c5e\u6027\uff1a");
        boolean bl = false;
        if (s2 != 0 && n3 != 0) {
            stringBuffer.append("|\u653b\u51fb\uff1a+" + s2);
            bl = true;
        }
        if (s3 != 0 && n3 != 2) {
            stringBuffer.append("|\u9632\u5fa1\uff1a+" + s3);
            bl = true;
        }
        if (s4 != 0 && n3 != 1) {
            stringBuffer.append("|\u56de\u907f\uff1a+" + s4);
            bl = true;
        }
        if (s5 != 0) {
            stringBuffer.append("|\u66b4\u51fb\u51e0\u7387\uff1a+" + s5 + "%");
            bl = true;
        }
        if (s6 != 0) {
            stringBuffer.append("|\u66b4\u51fb\u500d\u6570\uff1a+" + s6 + "%");
            bl = true;
        }
        if (s7 != 0) {
            stringBuffer.append("|\u8840\u91cf\uff1a+" + s7);
            bl = true;
        }
        if (s8 != 0) {
            stringBuffer.append("|\u9b54\u91cf\uff1a+" + s8);
            bl = true;
        }
        if (!bl) {
            stringBuffer.append("\u65e0");
        }
        if (s > 0) {
            stringBuffer.append("|\u5361\u7247\u9644\u52a0\u5c5e\u6027\uff1a");
            bl = false;
            for (int i2 = 1; i2 < 4; ++i2) {
                if (nArray[i2] == -1) continue;
                int n5 = f.b(nArray[i2]);
                if (n5 == 2) {
                    stringBuffer.append("|\u8840\u91cf\uff1a+");
                } else if (n5 == 3) {
                    stringBuffer.append("|\u9b54\u91cf\uff1a+");
                } else if (n5 == 4) {
                    stringBuffer.append("|\u653b\u51fb\uff1a+");
                } else if (n5 == 5) {
                    stringBuffer.append("|\u9632\u5fa1\uff1a+");
                } else if (n5 == 6) {
                    stringBuffer.append("|\u56de\u907f\uff1a+");
                } else if (n5 == 7) {
                    stringBuffer.append("|\u66b4\u51fb\u7387\uff1a+");
                } else if (n5 == 8) {
                    stringBuffer.append("|\u66b4\u51fb\u5a01\u529b\uff1a+");
                } else if (n5 == 9) {
                    stringBuffer.append("|\u5168\u5c5e\u6027\uff1a+");
                } else if (n5 == 10) {
                    stringBuffer.append("|\u6389\u5b9d\u7387\uff1a+");
                } else if (n5 == 11) {
                    stringBuffer.append("|\u7ecf\u9a8c\u503c\uff1a+");
                } else if (n5 == 12) {
                    stringBuffer.append("|\u5438\u8840\uff1a+");
                }
                stringBuffer.append(f.aA[nArray[i2] * 3 + 1]);
                if (n5 == 7 || n5 == 8 || n5 == 10 || n5 == 11 || n5 == 12) {
                    stringBuffer.append("%");
                }
                bl = true;
            }
            if (!bl) {
                stringBuffer.append("\u65e0");
            }
        }
        if (n4 != 0) {
            stringBuffer.append("[/C]");
        }
        return stringBuffer.toString();
    }

    private void u() {
        if (e == 0) {
            this.a(l.v, 200);
            i.b(1);
            return;
        }
        if (e == 1) {
            this.d();
            if (r == 0) {
                if (x != -1) {
                    if (x == 0) {
                        this.a(9);
                        return;
                    }
                    if (x == 1) {
                        this.a(10);
                        return;
                    }
                    if (x == 2) {
                        this.a(12);
                        return;
                    }
                    if (x == 3) {
                        this.a(17);
                        return;
                    }
                    if (x == 4) {
                        this.a(20);
                        return;
                    }
                } else {
                    this.e();
                }
            }
        }
    }

    private void v() {
        if (e == 0) {
            this.a(l.w, 200);
            i.b(1);
            return;
        }
        if (e == 1) {
            this.d();
            if (r == 0) {
                if (x != -1) {
                    if (x == 0) {
                        this.a(18);
                        return;
                    }
                    if (x == 1) {
                        this.a(12);
                        return;
                    }
                } else {
                    this.e();
                }
            }
        }
    }

    private void c(Graphics graphics) {
        this.b(graphics);
    }

    private void d(Graphics graphics) {
        this.b(graphics);
    }

    private void w() {
        this.n = null;
        int n = i * 6 + h;
        if (n < l.L.length) {
            byte by = l.L[n];
            this.c(by);
        }
    }

    private void x() {
        this.n = null;
    }

    private void y() {
        block39: {
            int n;
            block41: {
                block40: {
                    block38: {
                        if (e != 0) break block38;
                        if (j.i(8)) {
                            if (h + 1 < 18) {
                                h = (byte)(h + 1);
                            } else if (h / 6 + i + 1 < 5) {
                                h = (byte)(h - 5);
                                i = (byte)(i + 1);
                            }
                            int n2 = i * 6 + h;
                            if (n2 < l.L.length) {
                                byte by = l.L[n2];
                                this.c(by);
                            }
                            return;
                        }
                        if (j.i(4)) {
                            if (h > 0) {
                                h = (byte)(h - 1);
                            } else if (i > 0) {
                                h = (byte)(h + 5);
                                i = (byte)(i - 1);
                            }
                            int n3 = i * 6 + h;
                            if (n3 < l.L.length) {
                                byte by = l.L[n3];
                                this.c(by);
                            }
                            return;
                        }
                        if (j.i(2)) {
                            if (h + 6 < 18) {
                                h = (byte)(h + 6);
                            } else if (h / 6 + i + 1 < 5) {
                                i = (byte)(i + 1);
                            }
                            int n4 = i * 6 + h;
                            if (n4 < l.L.length) {
                                byte by = l.L[n4];
                                this.c(by);
                            }
                            return;
                        }
                        if (j.i(1)) {
                            if (h - 6 >= 0) {
                                h = (byte)(h - 6);
                            } else if (i > 0) {
                                i = (byte)(i - 1);
                            } else {
                                return;
                            }
                            int n5 = i * 6 + h;
                            if (n5 < l.L.length) {
                                byte by = l.L[n5];
                                this.c(by);
                            }
                            return;
                        }
                        if (j.n()) {
                            int n6 = i * 6 + h;
                            if (n6 < l.L.length) {
                                int n7;
                                byte by = l.L[n6];
                                short s = f.aA[by * 3 + 2];
                                int n8 = h.V.d(by);
                                int n9 = 0;
                                if (n8 >= 0) {
                                    n9 = h.V.c(n8);
                                }
                                if (h.E < s) {
                                    this.a(l.x[0]);
                                    n7 = 2;
                                } else if (n9 >= 99) {
                                    this.a(l.x[1]);
                                    n7 = 2;
                                } else {
                                    this.a(by, 0);
                                    n7 = 1;
                                }
                                i.b(n7);
                            }
                            return;
                        }
                        if (j.o()) {
                            this.a(8);
                            return;
                        }
                        break block39;
                    }
                    if (e != 1) break block40;
                    this.d();
                    if (i.b() != 0) break block39;
                    if (i.c() != -1) {
                        int n10;
                        int n11 = i * 6 + h;
                        byte by = l.L[n11];
                        short s = f.aA[by * 3 + 2];
                        if (h.V.b(by, 0)) {
                            if ((h.E -= s * x) < 0) {
                                h.E = 0;
                            }
                            for (int i2 = 0; i2 < x; ++i2) {
                                h.V.e(by);
                            }
                            n10 = 0;
                        } else {
                            this.a(l.x[9]);
                            n10 = 2;
                        }
                        i.b(n10);
                        return;
                    }
                    n = 0;
                    break block41;
                }
                if (e != 2) break block39;
                this.d();
                if (i.b() != 0) break block39;
                n = 0;
            }
            i.b(n);
        }
    }

    private void e(Graphics graphics) {
        int n;
        int n2;
        int n3;
        int n4 = f.ad - 176 >> 1;
        int n5 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        i.a(graphics, n4, n5, 176, 208, 0);
        graphics.setColor(0);
        graphics.drawString(l.v[0], n4 + 88, n5 + b, 17);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n6 = n4 + 7;
        int n7 = (n5 += 20) + 7;
        i.a(graphics, n6, n7, 162, 79, 4);
        n7 += 5;
        n6 += 5;
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 6; ++n2) {
                n = 0;
                if (h == n3 * 6 + n2) {
                    n = 1;
                }
                i.a(graphics, n6 + n2 * 23, n7 + n3 * 23, 22, 22, n != 0, true);
                int n8 = (i + n3) * 6 + n2;
                if (n8 >= l.L.length) continue;
                h.b(graphics, n6 + n2 * 23 + 11 - 8, n7 + n3 * 23 + 11 - 8, l.L[n8]);
            }
        }
        h.a(graphics, n4 + 176 - 20, n7, 7, 69, 3, 5, (int)i);
        n7 = n5 + 188 - 64 - 7;
        n6 = n4 + 7;
        i.a(graphics, n6, n7 + 20 + 4, 162, 40, 2);
        i.a(graphics, n6, n7, 162, 20, 4);
        graphics.setClip(0, 0, f.ad, f.ae);
        n3 = i * 6 + h;
        if (n3 < l.L.length) {
            n2 = l.L[n3];
            graphics.setColor(0xFFFFFF);
            graphics.drawString(f.az[n2 << 1], n6 + 5, n7 + b, 20);
            n7 += 24;
            for (n = 0; n < this.n.length; ++n) {
                graphics.setColor(0);
                graphics.drawString(this.n[n], n6 + 5, n7 + b, 20);
                n7 += 20;
            }
        }
        n7 = n5 + 7 + 80 + 7 + 2;
        n6 = n4 + 176 - 7 - 97;
        i.a(graphics, n6, n7, 97, 12, 3);
        h.a(graphics, n6 + 2, n7 += 2, "GOLD");
        h.a(graphics, n6 + 97 - 2, n7 + 1, String.valueOf(h.E), -1, 0);
        if (e == 1 || e == 2) {
            this.b(graphics);
        }
    }

    private void z() {
        this.n = null;
        int n = h.V.b(0);
        this.c(n);
    }

    private void A() {
        this.n = null;
    }

    private void B() {
        if (e == 0) {
            if (j.i(8)) {
                if (h + 1 < 18) {
                    h = (byte)(h + 1);
                } else if (h / 6 + i + 1 < 5) {
                    h = (byte)(h - 5);
                    i = (byte)(i + 1);
                }
                int n = h.V.b(i * 6 + h);
                this.c(n);
                return;
            }
            if (j.i(4)) {
                if (h > 0) {
                    h = (byte)(h - 1);
                } else if (i > 0) {
                    h = (byte)(h + 5);
                    i = (byte)(i - 1);
                }
                int n = h.V.b(i * 6 + h);
                this.c(n);
                return;
            }
            if (j.i(2)) {
                if (h + 6 < 18) {
                    h = (byte)(h + 6);
                } else if (h / 6 + i + 1 < 5) {
                    i = (byte)(i + 1);
                }
                int n = h.V.b(i * 6 + h);
                this.c(n);
                return;
            }
            if (j.i(1)) {
                if (h - 6 >= 0) {
                    h = (byte)(h - 6);
                } else if (i > 0) {
                    i = (byte)(i - 1);
                }
                int n = h.V.b(i * 6 + h);
                this.c(n);
                return;
            }
            if (j.n()) {
                int n = h.V.b(i * 6 + h);
                if (n != -1) {
                    boolean bl = false;
                    this.a(n, 1);
                    i.b(1);
                }
                return;
            }
            if (j.o()) {
                this.a(8);
                return;
            }
        } else if (e == 1) {
            this.d();
            if (r == 0) {
                if (x != -1) {
                    int n = i * 6 + h;
                    int n2 = h.V.b(n);
                    short s = f.aA[n2 * 3 + 2];
                    s = (short)(s >> 1);
                    h.E += s * x;
                    for (int i2 = 0; i2 < x; ++i2) {
                        h.V.f(n);
                    }
                }
                i.b(0);
            }
        }
    }

    private void f(Graphics graphics) {
        int n;
        int n2;
        int n3 = f.ad - 176 >> 1;
        int n4 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        i.a(graphics, n3, n4, 176, 208, 0);
        graphics.setColor(0);
        graphics.drawString(l.v[1], n3 + 88, n4 + b, 17);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n5 = n3 + 7;
        int n6 = (n4 += 20) + 7;
        i.a(graphics, n5, n6, 162, 79, 4);
        n6 += 5;
        n5 += 5;
        for (n2 = 0; n2 < 3; ++n2) {
            for (n = 0; n < 6; ++n) {
                boolean bl = false;
                if (h == n2 * 6 + n) {
                    bl = true;
                }
                i.a(graphics, n5 + n * 23, n6 + n2 * 23, 22, 22, bl, true);
                h.a(graphics, n5 + n * 23 + 11, n6 + n2 * 23 + 11, (i + n2) * 6 + n);
            }
        }
        h.a(graphics, n3 + 176 - 20, n6, 7, 69, 3, 5, (int)i);
        n6 = n4 + 188 - 64 - 7;
        n5 = n3 + 7;
        i.a(graphics, n5, n6 + 20 + 4, 162, 40, 2);
        i.a(graphics, n5, n6, 162, 20, 4);
        graphics.setClip(0, 0, f.ad, f.ae);
        n2 = h.V.b(i * 6 + h);
        if (n2 != -1) {
            graphics.setColor(0xFFFFFF);
            graphics.drawString(f.az[n2 << 1], n5 + 5, n6 + b, 20);
            n6 += 24;
            for (n = 0; n < this.n.length; ++n) {
                graphics.setColor(0);
                graphics.drawString(this.n[n], n5 + 5, n6 + b, 20);
                n6 += 20;
            }
        }
        n6 = n4 + 7 + 80 + 7 + 2;
        n5 = n3 + 176 - 7 - 97;
        i.a(graphics, n5, n6, 97, 12, 3);
        h.a(graphics, n5 + 2, n6 += 2, "GOLD");
        h.a(graphics, n5 + 97 - 2, n6 + 1, String.valueOf(h.E), -1, 0);
        if (e == 1 || e == 2) {
            this.b(graphics);
        }
    }

    private void C() {
        i.b(1);
    }

    private void D() {
        block33: {
            int n;
            block37: {
                block38: {
                    block34: {
                        block35: {
                            int n2;
                            block36: {
                                block32: {
                                    if (e == 0) {
                                        return;
                                    }
                                    if (e != 1) break block32;
                                    if (j.i(8)) {
                                        if (h + 1 < 18) {
                                            h = (byte)(h + 1);
                                            return;
                                        }
                                        if (h / 6 + i + 1 < 5) {
                                            h = (byte)(h - 5);
                                            i = (byte)(i + 1);
                                            return;
                                        }
                                    } else if (j.i(4)) {
                                        if (h > 0) {
                                            h = (byte)(h - 1);
                                            return;
                                        }
                                        if (i > 0) {
                                            h = (byte)(h + 5);
                                            i = (byte)(i - 1);
                                            return;
                                        }
                                    } else if (j.i(2)) {
                                        if (h + 6 < 18) {
                                            h = (byte)(h + 6);
                                            return;
                                        }
                                        if (h / 6 + i + 1 < 5) {
                                            i = (byte)(i + 1);
                                            return;
                                        }
                                    } else if (j.i(1)) {
                                        if (h - 6 >= 0) {
                                            h = (byte)(h - 6);
                                            return;
                                        }
                                        if (i > 0) {
                                            i = (byte)(i - 1);
                                            return;
                                        }
                                    } else {
                                        if (j.n()) {
                                            int n3 = i * 6 + h;
                                            int n4 = h.W[n3][0];
                                            if (n4 != -1) {
                                                this.a(l.y, 64);
                                                i.b(4);
                                            }
                                            return;
                                        }
                                        if (j.o()) {
                                            this.a(9);
                                            return;
                                        }
                                    }
                                    break block33;
                                }
                                if (e == 2) {
                                    return;
                                }
                                if (e == 3) {
                                    return;
                                }
                                if (e != 4) break block34;
                                this.d();
                                if (i.b() != 0) break block33;
                                if (i.c() == -1) break block35;
                                n2 = i * 6 + h;
                                int n5 = h.W[n2][0];
                                if (i.c() != 0) break block36;
                                i.b(8);
                                this.a(f.aB[n5], l.x[4] + f.aD[n5] / 2 + l.x[5]);
                                break block33;
                            }
                            if (i.c() == 1) {
                                i.b(6);
                                String string = i.a(h.W[n2]);
                                h.a(string, 156);
                            }
                            return;
                        }
                        n = 1;
                        break block37;
                    }
                    if (e == 5) {
                        return;
                    }
                    if (e != 6) break block38;
                    h.h();
                    if (!j.n() && !j.o()) break block33;
                    n = 1;
                    break block37;
                }
                if (e == 7) {
                    return;
                }
                if (e != 8) break block33;
                this.d();
                if (i.b() != 0) break block33;
                if (i.c() != -1) {
                    if (i.c() == 0) {
                        int n6 = i * 6 + h;
                        int n7 = h.W[n6][0];
                        h.E += f.aD[n7] / 2;
                        h.W[n6][0] = -1;
                        h.W[n6][1] = -1;
                        h.W[n6][2] = -1;
                        h.W[n6][3] = -1;
                    }
                    n = 1;
                } else {
                    n = 1;
                }
            }
            i.b(n);
        }
    }

    private void g(Graphics graphics) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        block41: {
            String string;
            int n7;
            int n8;
            block45: {
                int n9;
                Graphics graphics2;
                block43: {
                    int n10;
                    block44: {
                        block42: {
                            int n11;
                            int[][] nArray;
                            n6 = f.ad - 176 >> 1;
                            n5 = f.ae - 208 >> 1;
                            graphics.setClip(0, 0, f.ad, f.ae);
                            i.a(graphics, n6, n5, 176, 208, 0);
                            graphics.setColor(0);
                            graphics.drawString(l.v[2], n6 + 88, n5 + b, 17);
                            n5 += 20;
                            graphics.setClip(0, 0, f.ad, f.ae);
                            if (e != 3 && e != 6) break block41;
                            n8 = n6 + 7;
                            n7 = n5 + 7;
                            i.a(graphics, n8, n7, 162, 20, 4);
                            if (e == 3) {
                                nArray = h.X;
                                n11 = h;
                            } else {
                                n10 = i * 6 + h;
                                nArray = h.W;
                                n11 = n10;
                            }
                            int n12 = nArray[n11][0];
                            string = f.aB[n12];
                            n10 = n12 % 3;
                            if (n10 != 0) break block42;
                            graphics2 = graphics;
                            n9 = 0xFFFFFF;
                            break block43;
                        }
                        if (n10 != 1) break block44;
                        graphics2 = graphics;
                        n9 = 21670;
                        break block43;
                    }
                    if (n10 != 2) break block45;
                    graphics2 = graphics;
                    n9 = 0xFF00FF;
                }
                graphics2.setColor(n9);
            }
            graphics.drawString(string, n8 + 81, n7 + b, 17);
            i.a(graphics, n8, n7 += 24, 162, 150, 2);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.b(graphics, n8, n7, 162, 150);
            return;
        }
        int n13 = n6 + 7;
        int n14 = n5 + 7;
        i.a(graphics, n13, n14, 162, 100, 3);
        k.k.S.a(graphics, 0, n13 + 45, n14 + 80, false, false);
        boolean bl = false;
        boolean bl2 = false;
        if (!f && e == 0 && h == 1) {
            bl2 = true;
        }
        i.a(graphics, n13 + 33, n14 + 10, 22, 22, bl2, true);
        if (h.X[1][0] != -1) {
            h.a(graphics, n13 + 33 + 11, n14 + 10 + 11, h.X[1]);
        }
        bl2 = false;
        if (!f && e == 0 && h == 0) {
            bl2 = true;
        }
        i.a(graphics, n13 + 7, n14 + 40, 22, 22, bl2, true);
        if (h.X[0][0] != -1) {
            h.a(graphics, n13 + 7 + 11, n14 + 40 + 11, h.X[0]);
        }
        bl2 = false;
        if (!f && e == 0 && h == 2) {
            bl2 = true;
        }
        i.a(graphics, n13 + 59, n14 + 40, 22, 22, bl2, true);
        if (h.X[2][0] != -1) {
            h.a(graphics, n13 + 59 + 11, n14 + 40 + 11, h.X[2]);
        }
        bl2 = false;
        if (!f && e == 0 && h == 3) {
            bl2 = true;
        }
        i.a(graphics, n13 + 7, n14 + 70, 22, 22, bl2, true);
        if (h.X[3][0] != -1) {
            h.a(graphics, n13 + 7 + 11, n14 + 70 + 11, h.X[3]);
        }
        bl2 = false;
        if (!f && e == 0 && h == 4) {
            bl2 = true;
        }
        i.a(graphics, n13 + 59, n14 + 70, 22, 22, bl2, true);
        if (h.X[4][0] != -1) {
            h.a(graphics, n13 + 59 + 11, n14 + 70 + 11, h.X[4]);
        }
        n13 += 90;
        n14 += b;
        int n15 = h.K;
        int n16 = h.L;
        int n17 = h.M;
        int n18 = h.O;
        int n19 = h.P;
        if (e == 1 && (n4 = h.W[n3 = i * 6 + h][0]) != -1) {
            int n20;
            int n21;
            int n22;
            n2 = f.c(n4);
            n = h.X[n2][0];
            if (n != -1) {
                n15 -= f.aC[n * 9 + 1];
                n16 -= f.aC[n * 9 + 2];
                n17 -= f.aC[n * 9 + 3];
                n18 -= f.aC[n * 9 + 4] * 10;
                n19 -= f.aC[n * 9 + 5];
                for (n22 = 1; n22 < 4; ++n22) {
                    n21 = h.X[n2][n22];
                    if (n21 == -1) continue;
                    n20 = f.b(n21);
                    if (n20 == 6) {
                        n17 -= f.aA[n21 * 3 + 1];
                        continue;
                    }
                    if (n20 != 7) continue;
                    n18 -= f.aA[n21 * 3 + 1] * 10;
                }
            }
            n15 += f.aC[n4 * 9 + 1];
            n16 += f.aC[n4 * 9 + 2];
            n17 += f.aC[n4 * 9 + 3];
            n18 += f.aC[n4 * 9 + 4] * 10;
            n19 += f.aC[n4 * 9 + 5];
            for (n22 = 1; n22 < 4; ++n22) {
                n21 = h.W[n3][n22];
                if (n21 == -1) continue;
                n20 = f.b(n21);
                if (n20 == 6) {
                    n17 += f.aA[n21 * 3 + 1];
                    continue;
                }
                if (n20 != 7) continue;
                n18 += f.aA[n21 * 3 + 1] * 10;
            }
        }
        for (n3 = 0; n3 < 5; ++n3) {
            String string;
            block64: {
                String string2;
                block50: {
                    int n23;
                    StringBuffer stringBuffer;
                    block63: {
                        block67: {
                            int n24;
                            block66: {
                                block65: {
                                    block59: {
                                        block62: {
                                            int n25;
                                            block61: {
                                                block60: {
                                                    block55: {
                                                        block58: {
                                                            int n26;
                                                            block57: {
                                                                block56: {
                                                                    block51: {
                                                                        block54: {
                                                                            int n27;
                                                                            block53: {
                                                                                block52: {
                                                                                    block46: {
                                                                                        block49: {
                                                                                            int n28;
                                                                                            block48: {
                                                                                                block47: {
                                                                                                    graphics.setColor(0);
                                                                                                    graphics.setClip(0, 0, f.ad, f.ae);
                                                                                                    graphics.drawString(l.e[n3], n13, n14 + (20 - d.c >> 1), 0);
                                                                                                    string = "";
                                                                                                    n2 = 0;
                                                                                                    if (n3 != 0) break block46;
                                                                                                    if (n15 <= h.K) break block47;
                                                                                                    n28 = 1;
                                                                                                    break block48;
                                                                                                }
                                                                                                if (n15 >= h.K) break block49;
                                                                                                n28 = 2;
                                                                                            }
                                                                                            n2 = n28;
                                                                                        }
                                                                                        string2 = String.valueOf(n15);
                                                                                        break block50;
                                                                                    }
                                                                                    if (n3 != 1) break block51;
                                                                                    if (n16 <= h.L) break block52;
                                                                                    n27 = 1;
                                                                                    break block53;
                                                                                }
                                                                                if (n16 >= h.L) break block54;
                                                                                n27 = 2;
                                                                            }
                                                                            n2 = n27;
                                                                        }
                                                                        string2 = String.valueOf(n16);
                                                                        break block50;
                                                                    }
                                                                    if (n3 != 2) break block55;
                                                                    if (n17 <= h.M) break block56;
                                                                    n26 = 1;
                                                                    break block57;
                                                                }
                                                                if (n17 >= h.M) break block58;
                                                                n26 = 2;
                                                            }
                                                            n2 = n26;
                                                        }
                                                        string2 = String.valueOf(n17);
                                                        break block50;
                                                    }
                                                    if (n3 != 3) break block59;
                                                    if (n18 <= h.O) break block60;
                                                    n25 = 1;
                                                    break block61;
                                                }
                                                if (n18 >= h.O) break block62;
                                                n25 = 2;
                                            }
                                            n2 = n25;
                                        }
                                        stringBuffer = new StringBuffer();
                                        n23 = n18 / 10;
                                        break block63;
                                    }
                                    if (n3 != 4) break block64;
                                    if (n19 <= h.P) break block65;
                                    n24 = 1;
                                    break block66;
                                }
                                if (n19 >= h.P) break block67;
                                n24 = 2;
                            }
                            n2 = n24;
                        }
                        stringBuffer = new StringBuffer();
                        n23 = n19;
                    }
                    string2 = stringBuffer.append(String.valueOf(n23)).append("%").toString();
                }
                string = string2;
            }
            n = n13 + 70;
            h.a(graphics, n, n14 + 6, string, -1, n2);
            n14 += 20;
        }
        n13 = n6 + 7;
        n14 = n5 + 188 - 70 - 7;
        i.a(graphics, n13, n14, 162, 70, 4);
        ++n14;
        n13 += 5;
        for (n4 = 0; n4 < 3; ++n4) {
            for (n2 = 0; n2 < 6; ++n2) {
                n = 0;
                if (e == 1 && h == n4 * 6 + n2) {
                    n = 1;
                }
                i.a(graphics, n13 + n2 * 23, n14 + n4 * 23, 22, 22, n != 0, true);
                int[] nArray = h.W[(i + n4) * 6 + n2];
                if (nArray[0] == -1) continue;
                h.a(graphics, n13 + n2 * 23 + 11, n14 + n4 * 23 + 11, nArray);
            }
        }
        h.a(graphics, n6 + 176 - 20, n14, 7, 69, 3, 5, (int)i);
        if (e == 2 || e == 4 || e == 5 || e == 7 || e == 8) {
            this.b(graphics);
        }
    }

    private void E() {
        this.n = null;
        int n = i * 6 + h;
        if (n < l.N.length) {
            byte by = l.N[n];
            this.c(by);
        }
    }

    private void F() {
        this.n = null;
    }

    private void G() {
        block36: {
            int n;
            block40: {
                block39: {
                    block37: {
                        block38: {
                            block35: {
                                if (e != 0) break block35;
                                if (j.i(8)) {
                                    if (h + 1 < 18) {
                                        h = (byte)(h + 1);
                                    } else if (h / 6 + i + 1 < 5) {
                                        h = (byte)(h - 5);
                                        i = (byte)(i + 1);
                                    }
                                    int n2 = i * 6 + h;
                                    if (n2 < l.N.length) {
                                        byte by = l.N[n2];
                                        this.c(by);
                                    }
                                    return;
                                }
                                if (j.i(4)) {
                                    if (h > 0) {
                                        h = (byte)(h - 1);
                                    } else if (i > 0) {
                                        h = (byte)(h + 5);
                                        i = (byte)(i - 1);
                                    }
                                    int n3 = i * 6 + h;
                                    if (n3 < l.N.length) {
                                        byte by = l.N[n3];
                                        this.c(by);
                                    }
                                    return;
                                }
                                if (j.i(2)) {
                                    if (h + 6 < 18) {
                                        h = (byte)(h + 6);
                                    } else if (h / 6 + i + 1 < 5) {
                                        i = (byte)(i + 1);
                                    }
                                    int n4 = i * 6 + h;
                                    if (n4 < l.N.length) {
                                        byte by = l.N[n4];
                                        this.c(by);
                                    }
                                    return;
                                }
                                if (j.i(1)) {
                                    if (h - 6 >= 0) {
                                        h = (byte)(h - 6);
                                    } else if (i > 0) {
                                        i = (byte)(i - 1);
                                    } else {
                                        return;
                                    }
                                    int n5 = i * 6 + h;
                                    if (n5 < l.N.length) {
                                        byte by = l.N[n5];
                                        this.c(by);
                                    }
                                    return;
                                }
                                if (j.n()) {
                                    System.out.println("\u6362\u4e1c\u897f");
                                    int n6 = i * 6 + h;
                                    if (n6 < l.N.length) {
                                        int n7;
                                        int n8;
                                        byte by = l.N[n6];
                                        short s = f.aA[by * 3 + 2];
                                        int n9 = h.V.d(by);
                                        int n10 = 0;
                                        if (n9 >= 0) {
                                            n10 = h.V.c(n9);
                                        }
                                        if ((n8 = h.V.d(55)) != -1) {
                                            h.V.c(n8);
                                        }
                                        if (n10 >= 99) {
                                            this.a(l.x[1]);
                                            n7 = 2;
                                        } else {
                                            this.a(f.az[by << 1], l.x[7] + s + l.x[8]);
                                            n7 = 1;
                                        }
                                        i.b(n7);
                                    }
                                    return;
                                }
                                if (j.o()) {
                                    this.a(8);
                                    return;
                                }
                                break block36;
                            }
                            if (e != 1) break block37;
                            this.d();
                            if (i.b() != 0) break block36;
                            if (i.c() != 0) break block38;
                            int n11 = i * 6 + h;
                            byte by = l.N[n11];
                            int n12 = f.aA[by * 3 + 2];
                            int n13 = h.V.d(55);
                            int n14 = 0;
                            if (n13 != -1) {
                                n14 = h.V.c(n13);
                            }
                            if (n14 < n12) {
                                this.a(l.x[6]);
                                i.b(2);
                                return;
                            }
                            for (int i2 = 0; i2 < n12; ++i2) {
                                h.V.f(n13);
                            }
                            h.V.e(by);
                            break block39;
                        }
                        n = 0;
                        break block40;
                    }
                    if (e != 2) break block36;
                    this.d();
                    if (i.b() != 0) break block36;
                }
                n = 0;
            }
            i.b(n);
        }
    }

    private void h(Graphics graphics) {
        int n;
        int n2;
        int n3;
        int n4 = f.ad - 176 >> 1;
        int n5 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        i.a(graphics, n4, n5, 176, 208, 0);
        graphics.setColor(0);
        graphics.drawString(l.v[3], n4 + 88, n5 + b, 17);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n6 = n4 + 7;
        int n7 = (n5 += 20) + 7;
        i.a(graphics, n6, n7, 162, 79, 4);
        n7 += 5;
        n6 += 5;
        for (n3 = 0; n3 < 3; ++n3) {
            for (n2 = 0; n2 < 6; ++n2) {
                n = 0;
                if (h == n3 * 6 + n2) {
                    n = 1;
                }
                i.a(graphics, n6 + n2 * 23, n7 + n3 * 23, 22, 22, n != 0, true);
                int n8 = (i + n3) * 6 + n2;
                if (n8 >= l.N.length) continue;
                h.b(graphics, n6 + n2 * 23 + 11 - 8, n7 + n3 * 23 + 11 - 8, l.N[n8]);
            }
        }
        h.a(graphics, n4 + 176 - 20, n7, 7, 69, 3, 5, (int)i);
        n7 = n5 + 188 - 64 - 7;
        n6 = n4 + 7;
        i.a(graphics, n6, n7 + 20 + 4, 162, 40, 2);
        i.a(graphics, n6, n7, 162, 20, 4);
        graphics.setClip(0, 0, f.ad, f.ae);
        n3 = i * 6 + h;
        if (n3 < l.N.length) {
            n2 = l.N[n3];
            graphics.setColor(0xFFFFFF);
            graphics.drawString(f.az[n2 << 1], n6 + 5, n7 + b, 20);
            n7 += 24;
            for (n = 0; n < this.n.length; ++n) {
                graphics.setColor(0);
                graphics.drawString(this.n[n], n6 + 5, n7 + b, 20);
                n7 += 20;
            }
        }
        n7 = n5 + 7 + 80 + 7 + 2;
        n6 = n4 + 176 - 7 - 97;
        i.a(graphics, n6, n7, 97, 12, 3);
        h.a(graphics, n6 + 2, n7 += 2, "TICKET");
        n2 = h.V.a(55, 0);
        n = 0;
        if (n2 != -1) {
            n = h.V.c(n2);
        }
        h.a(graphics, n6 + 97 - 2, n7 + 1, String.valueOf(n), -1, 0);
        if (e == 1 || e == 2) {
            this.b(graphics);
        }
    }

    private void H() {
        block9: {
            int n;
            block7: {
                block11: {
                    block8: {
                        block10: {
                            block6: {
                                if (e != 0) break block6;
                                this.a(l.z, 200);
                                n = 1;
                                break block7;
                            }
                            if (e != 1) break block8;
                            this.d();
                            if (r != 0) break block9;
                            if (x == -1) break block10;
                            if (x == 0) {
                                h.f();
                                this.e();
                                return;
                            }
                            if (x != 1) break block9;
                            i = 0;
                            this.a(l.g[2]);
                            n = 3;
                            break block7;
                        }
                        this.e();
                        return;
                    }
                    if (e != 3) break block11;
                    this.d();
                    i = (byte)(i + 1);
                    if (i > 4 || j.n()) {
                        h.j();
                        i.b(4);
                        this.a(l.g[3]);
                        return;
                    }
                    break block9;
                }
                if (e != 4) break block9;
                this.d();
                if (i.b() != 0) break block9;
                n = 0;
            }
            i.b(n);
        }
    }

    private void i(Graphics graphics) {
        this.b(graphics);
    }

    private void I() {
        if (e == 0) {
            i.b(1);
            return;
        }
        if (e == 1) {
            this.d();
            if (r == 0) {
                if (x != -1 && x != 0) {
                    if (x == 1) {
                        j.aY += 2;
                        k.a(24, 2, 17, 18, 0, 0, false, 0);
                    }
                } else {
                    ++j.aY;
                }
                this.e();
            }
        }
    }

    private void j(Graphics graphics) {
        this.b(graphics);
    }

    private void J() {
        if (j.m()) {
            this.e();
        }
    }

    private void k(Graphics graphics) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(168, 136, 64);
        graphics.fillRect(0, 0, f.ad, f.ae);
        int n = f.ad - 8;
        int n2 = f.ae - 8;
        d.a(graphics, 4, 4, n, n2);
        i.a(graphics, "d.cn", f.ad >> 1, f.ae - d.c >> 1, 17, false);
        h.a(graphics, 0, 1, l.B);
    }

    private void K() {
        block8: {
            int n;
            i i2;
            block11: {
                block9: {
                    block13: {
                        block12: {
                            block10: {
                                byte by;
                                byte by2 = (byte)(l.W.length - 1);
                                if (e != 0) break block8;
                                if (j.i(2)) {
                                    if ((h = (byte)(h + 1)) > by2) {
                                        by = by2;
                                    }
                                } else if (j.i(1) && (h = (byte)(h - 1)) < 0) {
                                    by = h = 0;
                                }
                                if (!j.n()) break block9;
                                if (h != 0) break block10;
                                i2 = this;
                                n = 18;
                                break block11;
                            }
                            if (h != 1) break block12;
                            i2 = this;
                            n = 19;
                            break block11;
                        }
                        if (h - 2 != 5 || !h.aN) break block13;
                        i2 = this;
                        n = 8;
                        break block11;
                    }
                    h.f(h - 2);
                    return;
                }
                if (!j.o()) break block8;
                i2 = this;
                n = 6;
            }
            i2.a(n);
        }
    }

    private void l(Graphics graphics) {
        int n = f.ad - 176 >> 1;
        int n2 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        int n3 = l.W.length;
        if (e == 0) {
            int n4 = 30 * n3 + 10;
            int n5 = n + 10;
            int n6 = n2 + (208 - n4 >> 1);
            i.a(graphics, n5, n6, 156, n4, 3);
            n6 += 10;
            n5 += 10;
            for (int i2 = 0; i2 < n3; ++i2) {
                i.a(graphics, n5, n6, 136, 20, 4);
                if (h == i2) {
                    i.a(graphics, n5, n6, 136, 20, 5);
                }
                graphics.setColor(0xFFFFFF);
                graphics.drawString(l.W[0 + i2], n + 88, n6 + b, 17);
                n6 += 30;
            }
            h.a(graphics, 0, 1, l.B);
        }
    }

    private void L() {
        this.n = null;
        int n = i * 6 + h;
        if (n < l.O.length) {
            int n2 = l.O[n];
            String string = i.a(new int[]{n2, -1, -1, -1});
            h.a(string, 156);
        }
    }

    private void M() {
        this.n = null;
    }

    private void N() {
        block14: {
            block16: {
                block15: {
                    int n;
                    if (e != 0) break block14;
                    int n2 = n = l.O.length < 6 ? 6 : l.O.length;
                    if (j.i(8)) {
                        if (h + 1 < 6) {
                            h = (byte)(h + 1);
                        } else if (h + i + 1 < n) {
                            i = (byte)(i + 1);
                        }
                        int n3 = h + i;
                        if (n3 < l.O.length) {
                            int n4 = l.O[n3];
                            String string = i.a(new int[]{n4, -1, -1, -1});
                            h.a(string, 156);
                        }
                        return;
                    }
                    if (j.i(4)) {
                        if (h > 0) {
                            h = (byte)(h - 1);
                        } else if (i > 0) {
                            i = (byte)(i - 1);
                        }
                        int n5 = h + i;
                        if (n5 < l.O.length) {
                            int n6 = l.O[n5];
                            String string = i.a(new int[]{n6, -1, -1, -1});
                            h.a(string, 156);
                        }
                        return;
                    }
                    if (j.i(2) || j.i(1)) {
                        h.h();
                        return;
                    }
                    if (!j.n()) break block15;
                    if (h + i >= l.O.length) break block14;
                    h.f(9 + h + i);
                    break block16;
                }
                if (!j.o()) break block14;
            }
            this.a(14);
        }
    }

    private void m(Graphics graphics) {
        block8: {
            int n;
            int n2;
            int n3;
            block12: {
                int n4;
                Graphics graphics2;
                block10: {
                    int n5;
                    block11: {
                        block9: {
                            int n6;
                            int n7 = f.ad - 176 >> 1;
                            int n8 = f.ae - 208 >> 1;
                            graphics.setClip(0, 0, f.ad, f.ae);
                            i.a(graphics, n7, n8, 176, 208, 0);
                            graphics.setColor(0);
                            graphics.drawString(l.W[0], n7 + 88, n8 + b, 17);
                            graphics.setClip(0, 0, f.ad, f.ae);
                            n3 = n7 + 7;
                            n2 = (n8 += 20) + 7;
                            i.a(graphics, n3, n2, 162, 33, 4);
                            n2 += 5;
                            n3 += 12;
                            for (int i2 = 0; i2 < 6; ++i2) {
                                n6 = 0;
                                if (h == i2) {
                                    n6 = 1;
                                }
                                i.a(graphics, n3 + i2 * 23, n2, 22, 22, n6 != 0, true);
                                n = i + i2;
                                if (n >= l.O.length) continue;
                                h.a(graphics, n3 + i2 * 23 + 11, n2 + 11, new int[]{l.O[n], -1, -1, -1});
                            }
                            if (i > 0) {
                                d.b(graphics, n7 + 8 + (j.aX & 1), n2 + 10, 6, 0xFFFFFF, 0);
                            }
                            if (i + 6 < l.O.length) {
                                d.b(graphics, n7 + 176 - 8 - (j.aX & 1), n2 + 10, 2, 0xFFFFFF, 0);
                            }
                            n2 = n8 + 188 - 124 - 7;
                            n3 = n7 + 7;
                            i.a(graphics, n3, n2 + 20 + 4, 162, 100, 2);
                            i.a(graphics, n3, n2, 162, 20, 4);
                            graphics.setClip(0, 0, f.ad, f.ae);
                            n6 = i + h;
                            if (n6 >= l.O.length) break block8;
                            n = l.O[n6];
                            n5 = n % 3;
                            if (n5 != 0) break block9;
                            graphics2 = graphics;
                            n4 = 0xFFFFFF;
                            break block10;
                        }
                        if (n5 != 1) break block11;
                        graphics2 = graphics;
                        n4 = 21670;
                        break block10;
                    }
                    if (n5 != 2) break block12;
                    graphics2 = graphics;
                    n4 = 0xFF00FF;
                }
                graphics2.setColor(n4);
            }
            graphics.drawString(f.aB[n], n3 + 5, n2 + b, 20);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.b(graphics, n3, n2 += 24, 162, 93);
        }
        if (e == 1 || e == 2) {
            this.b(graphics);
        }
    }

    private void O() {
        this.n = null;
        int n = i * 6 + h;
        if (n < l.M.length) {
            byte by = l.M[n];
            String string = f.az[(by << 1) + 1];
            h.a(string, 156);
        }
    }

    private void P() {
        this.n = null;
    }

    private void Q() {
        block14: {
            block16: {
                block15: {
                    int n;
                    if (e != 0) break block14;
                    int n2 = n = l.M.length < 6 ? 6 : l.M.length;
                    if (j.i(8)) {
                        if (h + 1 < 6) {
                            h = (byte)(h + 1);
                        } else if (h + i + 1 < n) {
                            i = (byte)(i + 1);
                        }
                        int n3 = h + i;
                        if (n3 < l.M.length) {
                            byte by = l.M[n3];
                            String string = f.az[(by << 1) + 1];
                            h.a(string, 156);
                        }
                        return;
                    }
                    if (j.i(4)) {
                        if (h > 0) {
                            h = (byte)(h - 1);
                        } else if (i > 0) {
                            i = (byte)(i - 1);
                        }
                        int n4 = h + i;
                        if (n4 < l.M.length) {
                            byte by = l.M[n4];
                            String string = f.az[(by << 1) + 1];
                            h.a(string, 156);
                        }
                        return;
                    }
                    if (j.i(2) || j.i(1)) {
                        h.h();
                        return;
                    }
                    if (!j.n()) break block15;
                    System.out.println("14 + m_nTempSelect + m_nTempSelect2==14" + h + i);
                    if (h + i >= l.M.length) break block14;
                    h.f(14 + h + i);
                    break block16;
                }
                if (!j.o()) break block14;
            }
            this.a(14);
        }
    }

    private void n(Graphics graphics) {
        int n;
        int n2;
        int n3 = f.ad - 176 >> 1;
        int n4 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        i.a(graphics, n3, n4, 176, 208, 0);
        graphics.setColor(0);
        graphics.drawString(l.W[1], n3 + 88, n4 + b, 17);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n5 = n3 + 7;
        int n6 = (n4 += 20) + 7;
        i.a(graphics, n5, n6, 162, 33, 4);
        n6 += 5;
        n5 += 12;
        for (int i2 = 0; i2 < 6; ++i2) {
            n2 = 0;
            if (h == i2) {
                n2 = 1;
            }
            i.a(graphics, n5 + i2 * 23, n6, 22, 22, n2 != 0, true);
            n = i + i2;
            if (n >= l.M.length) continue;
            h.b(graphics, n5 + i2 * 23 + 11 - 8, n6 + 11 - 8, l.M[n]);
        }
        if (i > 0) {
            d.b(graphics, n3 + 8 + (j.aX & 1), n6 + 10, 6, 0xFFFFFF, 0);
        }
        if (i + 6 < l.M.length) {
            d.b(graphics, n3 + 176 - 8 - (j.aX & 1), n6 + 10, 2, 0xFFFFFF, 0);
        }
        n6 = n4 + 188 - 124 - 7;
        n5 = n3 + 7;
        i.a(graphics, n5, n6 + 20 + 4, 162, 100, 2);
        i.a(graphics, n5, n6, 162, 20, 4);
        graphics.setClip(0, 0, f.ad, f.ae);
        n2 = i + h;
        if (n2 < l.M.length) {
            n = l.M[n2];
            graphics.setColor(0xFFFFFF);
            graphics.drawString(f.az[n << 1], n5 + 5, n6 + b, 20);
            graphics.setClip(0, 0, f.ad, f.ae);
            h.b(graphics, n5, n6 += 24, 162, 93);
        }
        if (e == 1 || e == 2) {
            this.b(graphics);
        }
    }

    private void R() {
        h.a(l.A[0], 156);
    }

    private void S() {
        block12: {
            int n;
            block11: {
                block15: {
                    block14: {
                        block13: {
                            block8: {
                                block9: {
                                    block10: {
                                        if (e != 0) break block8;
                                        h.h();
                                        if (!j.n()) break block9;
                                        if (h.E < 30000) break block10;
                                        i = 0;
                                        this.a(l.A[1]);
                                        n = 1;
                                        break block11;
                                    }
                                    h.f(3);
                                    this.a(8);
                                    return;
                                }
                                if (j.o()) {
                                    this.a(8);
                                    return;
                                }
                                break block12;
                            }
                            if (e != 1) break block13;
                            this.d();
                            i = (byte)(i + 1);
                            if (i > 10) {
                                h.E -= 30000;
                                short s = l.P[d.a(0, l.P.length)];
                                boolean bl = h.c(s);
                                if (!bl) {
                                    k.a(-1, -1, -7, s, 0, 0, 0, k.k.s, k.k.t);
                                }
                                h.j();
                                i.b(2);
                                String string = l.A[2] + f.aB[s];
                                if (!bl) {
                                    string = string + "|" + l.A[5];
                                }
                                this.a(string);
                                return;
                            }
                            break block12;
                        }
                        if (e != 2) break block14;
                        this.d();
                        if (i.b() != 0) break block12;
                        break block15;
                    }
                    if (e != 3) break block12;
                    this.d();
                    if (i.b() != 0) break block12;
                }
                n = 0;
            }
            i.b(n);
        }
    }

    private void o(Graphics graphics) {
        int n = f.ad - 176 >> 1;
        int n2 = f.ae - 208 >> 1;
        graphics.setClip(0, 0, f.ad, f.ae);
        i.a(graphics, n, n2, 176, 208, 0);
        graphics.setColor(0);
        graphics.drawString(l.v[4], n + 88, n2 + b, 17);
        graphics.setClip(0, 0, f.ad, f.ae);
        int n3 = n + 7;
        int n4 = (n2 += 20) + 7;
        i.a(graphics, n3, n4, 162, 20, 4);
        graphics.setColor(0xFFFFFF);
        String string = l.A[3];
        graphics.drawString(string, n3 + 81, n4 + b, 17);
        i.a(graphics, n3, n4 += 24, 162, 150, 2);
        graphics.setClip(0, 0, f.ad, f.ae);
        h.b(graphics, n3, n4, 162, 150);
        if (e != 0) {
            this.b(graphics);
        }
    }
}

