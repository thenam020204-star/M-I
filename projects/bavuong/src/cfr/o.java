/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 *  javax.microedition.midlet.MIDlet
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public final class o
extends Canvas
implements Runnable,
s {
    public ForeverHero a;
    public Thread b;
    public r c;
    public q d;
    public Graphics e;
    public Random f;
    public static int g;
    public int h;
    public int i;
    public Font j;
    public int k;
    public int l;
    public int m;
    public int n;
    public int o;
    public int p;
    public boolean q;
    public boolean r;
    public int s;
    public int t;
    public Image[] u;
    public Image v;
    public Image w;
    public String x;
    public Image y;
    public int z;
    public String A;
    public int B;
    public boolean C;
    public int D;
    public int E;
    public int F;
    public boolean G;
    public int H;
    public Image I;
    public Image J;
    public Image K;
    public Image L;
    public Image[] M;
    public Image[] N;
    public Image[] O;
    public Image P;
    public Image Q;
    public Image[] R;
    public Image S;
    public Image[] T;
    public Image[] U;
    public Image[] V;
    public Image[] W;
    public Image[] X;
    public Image[] Y;
    public Image[] Z;
    public Image[] aa;
    public Image ab;
    public Image ac;
    public Image[] ad;
    public Image[] ae;
    public Image[] af;
    public Image[] ag;
    public Image[] ah;
    public Image[] ai;
    public Image[] aj;
    public Image[] ak;
    public Image[] al;
    public Image[] am;
    public Image an;
    public int ao;
    public int ap;
    public int[] aq;
    public int[] ar;
    public int as;
    public int at;
    public int au;
    public int av;
    public int aw;
    public int ax;
    public int[] ay;
    public int[] az;
    public int aA;
    public int aB;
    public int aC;
    public int[] aD;
    public int aE;
    public int[] aF;
    public int[] aG;
    public int[][] aH;
    public int[] aI;
    public int[] aJ;
    public int[] aK;
    public String aL;
    public int aM;
    public int aN;
    public int aO;
    public int aP;
    public int[] aQ;
    public int aR;
    public int[] aS;
    public int aT;
    public k[] aU;
    public int aV;
    public int[] aW;
    public d[] aX;
    public int aY;
    public int aZ;
    public int[] ba;
    public int[] bb;
    public int bc;
    public int bd;
    public int be;
    public int[] bf;
    public int[] bg;
    public int[] bh;
    public int[] bi;
    public int[] bj;
    public int[] bk;
    public n[] bl;
    public m[] bm;
    public j[] bn;
    public long bo;
    public int bp;
    public int bq;
    public int br;
    public int bs;
    public int bt;
    public int bu;
    public int bv;
    public int bw;
    public int[] bx;
    public int[] by;
    public int bz;
    public int bA;
    public int[] bB;
    public int[] bC;
    public int bD;
    public int bE;
    public int bF;
    public int bG;
    public int bH;
    public int bI;
    public int bJ;
    public int bK;
    public int bL;
    public int bM;
    public int bN;
    public int bO;
    public String[] bP;
    public int bQ;
    public int bR;
    public int bS;
    public int bT;
    public int bU;
    public int bV;
    public int bW;
    public int bX;
    public int bY;
    public int bZ;
    public int ca;
    public int cb;
    public int cc;
    public int cd;
    public int[] ce;
    public int cf;
    public int cg;
    public int ch;
    public int ci;
    public int[][] cj;
    public int[][] ck;
    public int[][] cl;
    public int[] cm;
    public int cn;
    public int co;
    public int[][] cp;
    public int[][] cq;
    public int cr;
    public int cs;
    public int ct;
    public int[] cu;
    public int[][] cv;
    public int cw;
    public int cx;
    public int cy;
    public int[] cz;
    public int[][][] cA;
    public int cB;
    public int cC;
    public int cD;
    public int cE;
    public int cF;
    public int cG;
    public int cH;
    public int cI;
    public int cJ;
    public int cK;
    public int cL;
    public int cM;
    public int cN;
    public int cO;
    public int cP;
    public int cQ;
    public int cR;
    public int cS;
    public int cT;
    public int cU;
    public int cV;
    public int cW;
    public int cX;
    public int cY;
    public int cZ;
    public int da;
    public int[][] db;
    public int dc;
    public int dd;
    public int de;
    public int df;
    public int[][] dg;
    public int dh;
    public int di;
    public int dj;
    public int dk;
    public int dl;
    public int dm;
    public int dn;
    public int do;
    public int dp;
    public int dq;
    public int dr;
    public int ds;
    public int dt;
    public int[][] du;
    public int dv;
    public int dw;
    public int dx;
    public int dy;
    public int dz;
    public int dA;
    public int dB;
    public int dC;
    public int dD;
    public int dE;
    public int[][] dF;
    public int[] dG;
    public int dH;
    public int dI;
    public int dJ;
    public int[][][] dK;
    public int dL;
    public int dM;
    public int[][] dN;
    public int dO;
    public int dP;
    public int dQ;
    public int[] dR;
    public int dS;
    public int dT;
    public int dU;
    public int dV;
    public int dW;
    public int dX;
    public int dY;
    public int dZ;
    public int ea;
    public int eb;
    public int ec;
    public int ed;
    public int ee;
    public int ef;
    public int eg;
    public int eh;
    public int ei;
    public int ej;
    public int ek;
    public int[][] el;
    public int em;
    public int en;
    public int[][] eo;
    public int ep;
    public int eq;
    public int er;
    public int es;
    public int et;
    public int eu;
    public int ev;
    public int ew;
    public int ex;
    public int ey;
    public int ez;
    public int eA;
    public int eB;
    public int eC;
    public int eD;
    public int eE;
    public int[] eF;
    public int eG;
    public int[] eH;
    public int[] eI;
    public int[][] eJ;
    public String[] eK;
    public int[][] eL;
    public int[] eM;
    public int[] eN;
    public p[] eO;
    public t[] eP;
    public int eQ;
    public int eR;
    public int eS;
    public int eT;
    public int eU;
    public int eV;
    public int eW;
    public int eX;
    public int eY;
    public int eZ;
    public int fa;
    public int fb;
    public int fc;
    public int fd;
    public int fe;
    public int ff;
    public int fg;
    public int[] fh;
    public int fi;
    public int fj;
    public int fk;
    public int fl;
    public int fm;
    public int[] fn;
    public String fo;
    public int fp;
    public int fq;
    public int fr;
    public int[] fs;
    public int ft;
    public int[] fu;
    public int fv;
    public int fw;
    public int[] fx;
    public int fy;
    public int fz;
    public int[] fA;
    public int fB;
    public int fC;
    public int fD;
    public int fE;
    public int fF;
    public int fG;
    public int fH;
    public int fI;
    public int[][] fJ;
    public String[] fK;
    public int fL;
    public int fM;
    public int[] fN;
    public int fO;
    public int fP;
    public int fQ;
    public int fR;
    public int[][] fS;
    public int fT;
    public int fU;
    public int fV;
    public int fW;
    public int fX;
    public int fY;
    public int fZ;
    public int ga;
    public int gb;
    public int gc;
    public int gd;
    public int ge;
    public int gf;
    public int gg;
    public int gh;
    public int gi;
    public int gj;
    public int gk;
    public int gl;
    public int gm;
    public int gn;
    public int go;
    public int gp;
    public int gq;
    public int gr;
    public int gs;
    public String[] gt;
    public int gu;
    public int gv;
    public int gw;
    public int gx;
    public byte[] gy;
    public byte[] gz;
    public Image[] gA;
    public byte[] gB;
    public int gC;
    public InputStream gD;
    public DataInputStream gE;
    public int gF;
    public int gG;
    public int gH;
    public int gI;
    public int gJ;
    private static String[] _BB;
    private static byte[] data;
    private int _CC = 0;
    private int _a;
    private long _b;
    private static long _c;
    private static MIDlet _d;
    private Image _e;
    private byte _f = (byte)2;
    private final byte[] _g = new byte[256];
    private int _h;
    private int _i;
    private int _j;
    private int _k;
    private int _l;
    private int _m;
    private int _n;
    private long _o;
    private int _p;
    private byte[] _q;
    private int[] _r;
    private static int _s;
    private static int _DD0;
    private static long _DD1;
    private static int _DD2;
    private static long _DD3;
    private static int _DD4;
    private static long _DD5;
    private static int _DD6;
    private static long _DD7;
    private static int _DD8;
    private static long _DD9;
    private static int _DD10;
    private static long _DD11;
    private static int _DD12;
    private static long _DD13;
    private static int _DD14;
    private static long _DD15;
    private static int _DD16;
    private static long _DD17;
    private static int _DD18;
    private static long _DD19;

    public o(ForeverHero foreverHero) {
        this.h = 0;
        this.i = 1;
        int[] nArray = new int[]{1, 9, 14, 15, 47, 10, 12, 19, 55, 20, 28, 25, 24, 80, 42, 81, 33};
        this.q = false;
        this.s = 0;
        this.t = 0;
        this.x = null;
        this.F = 0;
        this.ad = new Image[10];
        this.aq = new int[15];
        this.ar = new int[15];
        this.aG = new int[160];
        this.aI = new int[2];
        this.aJ = new int[2];
        this.aK = new int[3];
        this.aW = new int[10];
        this.bg = new int[4];
        this.bx = new int[2];
        this.by = new int[2];
        this.bB = new int[2];
        this.bC = new int[2];
        this.bP = new String[15];
        this.ce = new int[17];
        this.cj = new int[5][3];
        this.ck = new int[5][2];
        this.cl = new int[3][2];
        this.cm = new int[6];
        this.cp = new int[6][3];
        this.cq = new int[50][12];
        this.cu = new int[50];
        this.cv = new int[50][2];
        this.cz = new int[4];
        this.cA = new int[2][30][2];
        this.db = new int[5][4];
        this.dg = new int[4][2];
        this.du = new int[20][9];
        this.dF = new int[10][5];
        this.dG = new int[10];
        this.dK = new int[6][4][4];
        this.dN = new int[4][6];
        this.dR = new int[2];
        this.el = new int[2][2];
        this.eo = new int[5][2];
        this.eF = new int[13];
        this.eH = new int[3];
        this.eI = new int[50];
        this.eJ = new int[4][26];
        this.eK = new String[4];
        this.eL = new int[15][2];
        this.eM = new int[22];
        this.eN = new int[4];
        this.eO = new p[150];
        this.eP = new t[100];
        this.fh = new int[60];
        this.fl = 5;
        this.fn = new int[12];
        this.fs = new int[16];
        this.fu = new int[4];
        this.fx = new int[4];
        this.fJ = new int[2][4];
        this.fK = new String[2];
        this.fN = new int[5];
        this.fS = new int[30][6];
        this.gt = new String[5];
        this.a = foreverHero;
        this.setFullScreenMode(true);
        this.j = Font.getFont((int)0, (int)0, (int)8);
        this.b = new Thread(this);
        this.ap = 0;
        this.bx[0] = 240;
        this.bx[1] = 320;
        this.bz = 0;
        this.bA = 0;
        this.by[0] = this.bx[0] / 2;
        this.by[1] = this.bx[1] / 2;
        this.bB[0] = this.bx[0] / 2;
        this.bB[1] = (this.bx[1] - 40) / 2 + 20;
        this.bD = 1;
        this.bE = 0;
        this.bF = 0;
        this.bG = 0;
        this.bI = 0;
        this.q = false;
        this.k = this.bx[0];
        this.l = this.bx[1];
        this.m = this.by[0];
        this.n = this.by[1];
        this.o = this.j.stringWidth("\u5bbd");
        this.p = this.j.getHeight();
        this.c = new r();
        int n2 = 0;
        while (n2 < 150) {
            this.eO[n2] = new p(this);
            ++n2;
        }
        n2 = 0;
        while (n2 < 100) {
            this.eP[n2] = new t(this);
            ++n2;
        }
        this.bL = 1;
        System.out.println("=======" + this.bL);
        if (this.bL == 1) {
            this.bu = 1;
            this.bv = 1;
            this.a(1);
            this.C = false;
        }
        this.d = new q(this);
        this.z = 0;
        this.gv = 0;
        this.gJ = 0;
        this.b.start();
    }

    public final void a(int n2) {
        this.ap = 1;
        if (this.bu != 1) {
            this.ap = n2;
        }
    }

    public final void hideNotify() {
        if (_s == 1) {
            ++_s;
            return;
        }
        this.q = true;
        this.r = true;
        this.aW();
        this.d.b();
    }

    public final void showNotify() {
        this.q = false;
        this.bq = 0;
        this.bT = 0;
        this.a();
    }

    public final synchronized void a() {
        this.notify();
    }

    public final void run() {
        if (_s < 1) {
            while (_c < this._b) {
                if (!this.isShown()) continue;
                long l2 = System.currentTimeMillis();
                this._h();
                this.repaint();
                this.serviceRepaints();
                int n2 = (int)(System.currentTimeMillis() - l2);
                if (n2 < this._a) {
                    try {
                        Thread.sleep(this._a - n2);
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                    _c += (long)this._a;
                    continue;
                }
                _c += (long)this._a;
            }
            this._f();
            try {
                this._i();
            }
            catch (Exception exception) {
                // empty catch block
            }
            return;
        }
        try {
            System.out.println("thread:" + (this.b == null));
            while (this.b != null) {
                Thread.sleep(this.bK + 1);
                if (this.q) {
                    if (this.dA == 1 && this.dp == 0) {
                        this.dA = 0;
                        this.bD = 1;
                    }
                    if (this.eb != 1) continue;
                    this.eb = 0;
                    this.dA = 0;
                    continue;
                }
                if (this.fP == 0 && this.ap != 0) {
                    this.n();
                }
                if (this.t == 0 && this.gJ == 0) {
                    this.repaint(0, 0, this.bx[0], this.bx[1]);
                    this.serviceRepaints();
                }
                Thread.yield();
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void b() {
        this.ay();
        this.au = this.B;
        this.at = 0;
        this.av = 5;
        this.ei = 0;
        this.fW = 0;
        this.dU = 0;
    }

    public final void b(int n2) {
        int n3 = 0;
        this.at = 0;
        if (n2 == 0) {
            this.eQ = 0;
            this.eR = 0;
            this.g(1, 3);
        }
        n3 = this.bx[1] < 150 ? 20 : (this.bx[1] < 300 ? 30 : 50);
        this.gf = this.bx[1] - n3 + 10;
        this.gg = n3 - 20;
        this.gh = this.gf;
        this.gi = n3;
    }

    public final void c() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        n2 = 0;
        while (n2 < 5) {
            n3 = 0;
            while (n3 < 2) {
                this.ck[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        this.dE = 0;
        n2 = 0;
        while (n2 < 10) {
            this.dG[n2] = 0;
            ++n2;
        }
        n2 = 0;
        while (n2 < 10) {
            n3 = 0;
            while (n3 < 5) {
                this.dF[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 6) {
            n3 = 0;
            while (n3 < 3) {
                this.cp[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 6) {
            this.cm[n2] = 0;
            ++n2;
        }
        n2 = 0;
        while (n2 < 5) {
            n3 = 0;
            while (n3 < 3) {
                this.cj[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 4) {
            this.cz[n2] = 0;
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            n3 = 0;
            while (n3 < 2) {
                this.cl[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 50) {
            n3 = 0;
            while (n3 < 12) {
                this.cq[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 60) {
            this.fh[n2] = 0;
            ++n2;
        }
        n2 = 0;
        while (n2 < 4) {
            n3 = 0;
            while (n3 < 26) {
                this.eJ[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 2) {
            n3 = 0;
            while (n3 < 30) {
                n4 = 0;
                while (n4 < 2) {
                    this.cA[n2][n3][n4] = 0;
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            this.eH[n2] = 0;
            ++n2;
        }
        n2 = 0;
        while (n2 < 50) {
            this.eI[n2] = 0;
            ++n2;
        }
    }

    public final void d() {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        this.eC = 0;
        this.eD = 0;
        this.eE = 0;
        this.eG = 0;
        this.fm = 1;
        this.fk = 1;
        this.fl = 0;
        this.bD = 0;
        this.bV = 57;
        this.cc = 32;
        this.cd = 10;
        this.cC = 2;
        this.cD = 2;
        this.bY = 0;
        this.bZ = 0;
        this.c();
        this.fP = 0;
        this.fQ = 0;
    }

    public final void e() {
        int n2 = 0;
        this.ce[0] = 100;
        this.ce[1] = 0;
        this.ce[2] = 1000;
        this.ce[3] = 1000;
        this.ce[4] = 100;
        this.ce[5] = 100;
        this.ce[6] = 100;
        this.ce[7] = 100;
        this.ce[8] = 100;
        this.v(this.bZ);
        this.w(this.bZ);
        this.ce[11] = 0;
        this.ce[12] = 0;
        this.ce[13] = 0;
        this.ce[14] = 0;
        this.ce[15] = 0;
        this.cf = 1000;
        this.cg = 1000;
        this.ch = 8;
        this.ci = 0;
        this.cn = 0;
        this.cs = 0;
        this.ct = 3000;
        this.cr = 50;
        n2 = 0;
        while (n2 < 6) {
            this.cm[n2] = 6;
            ++n2;
        }
        n2 = 0;
        while (n2 < 6) {
            this.cp[n2][0] = 6;
            this.cp[n2][1] = 28700;
            this.cp[n2][2] = 28700;
            ++n2;
        }
        this.cz[0] = 3;
        this.cz[1] = 3;
        this.cz[2] = 3;
        this.cz[3] = 1;
        this.co = 1;
        this.cG = 3;
        this.eb = 0;
        this.ec = 10;
        this.ed = 0;
        this.ee = 0;
        n2 = 0;
        while (n2 < 3) {
            this.c(2, n2 + 11, 0);
            this.d(this.fn);
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            this.c(4, n2 + 11, 0);
            this.d(this.fn);
            ++n2;
        }
        this.c(5, 6, 0);
        this.d(this.fn);
        this.cl[0][0] = 2;
        this.cl[0][1] = 0;
        this.cl[1][0] = 4;
        this.cl[1][1] = 3;
        this.cl[2][0] = 5;
        this.cl[2][1] = 6;
    }

    public final void f() {
        int n2 = 0;
        int n3 = 0;
        int[] nArray = new int[13];
        nArray[0] = 78;
        nArray[1] = 6;
        nArray[2] = 9;
        nArray[3] = 6;
        nArray[4] = 9;
        nArray[5] = 78;
        nArray[10] = 32;
        int[] nArray2 = nArray;
        int[] nArray3 = new int[22];
        nArray3[1] = 1;
        nArray3[2] = 1;
        nArray3[3] = 3;
        nArray3[9] = 14;
        nArray3[10] = 10;
        nArray3[11] = 1;
        int[] nArray4 = nArray3;
        this.ce[0] = 1;
        this.ce[1] = 0;
        this.ce[4] = 12;
        this.ce[5] = 50;
        this.ce[6] = 50;
        this.ce[7] = 50;
        this.ce[8] = 0;
        this.v(this.bZ);
        this.w(this.bZ);
        this.ce[12] = 0;
        this.ce[13] = 0;
        this.ce[14] = 0;
        this.ce[15] = 0;
        this.aj();
        this.ce[2] = this.cf;
        this.ce[3] = this.cg;
        this.ch = 8;
        this.ci = 0;
        this.cn = 0;
        this.cs = 0;
        this.ct = 200 + this.ce[4] * 20;
        this.cr = 30;
        this.bX = 1;
        this.cG = 2;
        this.c();
        this.co = 0;
        this.eG = 0;
        n2 = 0;
        while (n2 < 13) {
            this.eF[n2] = nArray2[n2];
            ++n2;
        }
        n2 = 0;
        while (n2 < 22) {
            this.eJ[0][n2] = nArray4[n2];
            ++n2;
        }
        this.eC = 1;
        this.eD = 0;
        this.eE = 0;
        this.fm = 0;
        this.fk = 0;
        System.out.println("######################");
        this.eb = 0;
        this.ec = 4;
        this.ed = 2;
        this.ee = 0;
        this.bY = 1;
        this.ce[11] = 200;
        n2 = 0;
        while (n2 < 15) {
            n3 = 0;
            while (n3 < 2) {
                this.eL[n2][n3] = 0;
                ++n3;
            }
            ++n2;
        }
        this.eL[0][0] = 17;
        this.eL[1][0] = 18;
        this.eL[2][0] = 23;
        this.eL[3][0] = 14;
        this.eL[4][0] = 20;
        this.eL[5][0] = 21;
        this.eL[6][0] = 22;
        this.eL[7][0] = 24;
    }

    public final void g() {
        try {
            this.gE.close();
            this.gD.close();
            this.gE = null;
            this.gD = null;
            Runtime.getRuntime().gc();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void h() {
        System.out.println("InitGame=================");
        int n2 = 0;
        byte[] byArray = new byte[8];
        int[] nArray = new int[2];
        String string = null;
        this.gj = 0;
        this.gJ = 1;
        try {
            int n3;
            int[] nArray2;
            String string2 = string = "/data/MD" + this.bV + ".dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var15_8 = null;
            inputStream = o._m(clazz, string2);
            this.gD = inputStream;
            this.gE = new DataInputStream(this.gD);
            this.gE.read(byArray, 0, 4);
            nArray[0] = this.a(byArray, 0);
            byte[] byArray2 = new byte[nArray[0]];
            int[] nArray3 = new int[nArray[0]];
            this.gE.read(byArray2, 0, nArray[0]);
            this.g();
            int n4 = 0;
            while (n4 < byArray2.length) {
                nArray3[n4] = byArray2[n4] + 127;
                ++n4;
            }
            this.aP = 0;
            this.aR = 0;
            this.aT = 0;
            this.aV = 0;
            n4 = 0;
            while (n4 < 10) {
                this.aW[n4] = 0;
                ++n4;
            }
            int n5 = 0;
            this.bU = nArray3[0];
            this.aP = nArray3[1];
            if (this.aP > 0) {
                this.aQ = new int[this.aP * 9];
                System.arraycopy(nArray3, 15, this.aQ, 0, this.aP * 9);
                n5 = this.aP * 9;
            }
            this.aR = nArray3[2];
            if (this.aR > 0) {
                this.aS = new int[this.aR * 10];
                System.arraycopy(nArray3, 15 + n5, this.aS, 0, this.aR * 10);
                n5 += this.aR * 10;
            }
            this.aT = nArray3[5];
            if (this.aT > 0) {
                this.aY = nArray3[3];
                int[] nArray4 = new int[this.aY * 29];
                System.arraycopy(nArray3, 15 + n5, nArray4, 0, this.aY * 29);
                this.aZ = nArray3[4];
                this.ba = new int[this.aZ * 5];
                System.arraycopy(nArray3, 15 + (n5 += this.aY * 29), this.ba, 0, this.aZ * 5);
                n5 += this.aZ * 5;
                if (this.eD == 11 && this.bV == 60) {
                    this.ba[2] = 4;
                }
                nArray2 = new int[this.aT * 7];
                System.arraycopy(nArray3, 15 + n5, nArray2, 0, this.aT * 7);
                this.aU = new k[this.aT];
                this.a(nArray2, nArray4);
                n5 += this.aT * 7;
                this.bl = new n[20];
                n4 = 0;
                while (n4 < 20) {
                    this.bl[n4] = new n();
                    ++n4;
                }
                this.bn = new j[10];
                n4 = 0;
                while (n4 < 10) {
                    this.bn[n4] = new j();
                    ++n4;
                }
            }
            this.aV = nArray3[6];
            if (this.aV > 0) {
                nArray2 = new int[this.aV * 30];
                System.arraycopy(nArray3, 15 + n5, nArray2, 0, this.aV * 30);
                System.arraycopy(nArray3, 15 + (n5 += this.aV * 30), this.aW, 0, 10);
                n5 += 10;
                this.aX = new d[this.aV];
                this.b(nArray2);
            }
            this.bc = nArray3[8];
            if (this.bc > 0) {
                this.bb = new int[this.bc * 7];
                System.arraycopy(nArray3, 15 + n5, this.bb, 0, this.bc * 7);
                n5 += this.bc * 7;
            }
            this.bd = nArray3[9];
            this.bm = new m[10];
            n4 = 0;
            while (n4 < 10) {
                this.bm[n4] = new m();
                ++n4;
            }
            if (this.bd > 0) {
                nArray2 = new int[this.bd * 6];
                System.arraycopy(nArray3, 15 + n5, nArray2, 0, this.bd * 6);
                this.c(nArray2);
                n5 += this.bd * 6;
            }
            this.be = nArray3[10];
            if (this.be > 0) {
                this.bf = new int[this.be * 4];
                System.arraycopy(nArray3, 15 + n5, this.bf, 0, this.be * 4);
                n5 += this.be * 4;
            }
            n4 = 0;
            while (n4 < 4) {
                this.bg[n4] = nArray3[11 + n4];
                if (this.bg[n4] > 0) {
                    if (n4 == 0) {
                        this.bh = new int[this.bg[n4] * 5];
                        System.arraycopy(nArray3, 15 + n5, this.bh, 0, this.bg[n4] * 5);
                    } else if (n4 == 1) {
                        this.bi = new int[this.bg[n4] * 5];
                        System.arraycopy(nArray3, 15 + n5, this.bi, 0, this.bg[n4] * 5);
                    } else if (n4 == 2) {
                        this.bj = new int[this.bg[n4] * 5];
                        System.arraycopy(nArray3, 15 + n5, this.bj, 0, this.bg[n4] * 5);
                    } else if (n4 == 3) {
                        this.bk = new int[this.bg[n4] * 5];
                        System.arraycopy(nArray3, 15 + n5, this.bk, 0, this.bg[n4] * 5);
                    }
                    n5 += this.bg[n4] * 5;
                }
                ++n4;
            }
            string2 = string = "/data/Map" + this.bU + ".dat";
            clazz = this.getClass();
            inputStream = null;
            var15_8 = null;
            inputStream = o._m(clazz, string2);
            this.gD = inputStream;
            this.gE = new DataInputStream(this.gD);
            this.gE.read(byArray, 0, 4);
            nArray[0] = this.a(byArray, 0);
            byArray2 = new byte[nArray[0]];
            nArray3 = new int[nArray[0]];
            this.gE.read(byArray2, 0, nArray[0]);
            this.g();
            n4 = 0;
            while (n4 < byArray2.length) {
                nArray3[n4] = byArray2[n4] + 127;
                ++n4;
            }
            this.bC = new int[2];
            this.bC[0] = nArray3[0];
            this.bC[1] = nArray3[1];
            n2 = this.bC[0] * this.bC[1];
            this.aH = new int[4][n2];
            n4 = 0;
            while (n4 < 4) {
                System.arraycopy(nArray3, 2 + n2 * n4, this.aH[n4], 0, n2);
                ++n4;
            }
            this.aA = nArray3[2 + n2 * 4];
            this.aB = nArray3[2 + n2 * 4 + 1];
            this.aC = nArray3[2 + n2 * 4 + 2];
            this.aE = nArray3[2 + n2 * 4 + 3 + this.aC];
            this.aD = new int[this.aC];
            this.aF = new int[this.aE];
            System.arraycopy(nArray3, 5 + n2 * 4, this.aD, 0, this.aC);
            System.arraycopy(nArray3, 6 + n2 * 4 + this.aC, this.aF, 0, this.aE);
            this.am();
            this.bt = this.aH[3][0] % 20 + 20;
            if (this.eC != 1 && this.fk != 1) {
                this.bq = 0;
            }
            this.bH = this.bt == 36 || this.bt == 37 ? 1 : 0;
            if (this.bZ == 1 && this.dE == 1 && this.bH == 1) {
                this.dG[0] = this.dG[0] <= this.ei ? 9999 - (this.ei - this.dG[0]) : 9999 - (this.dG[0] - this.ei);
            } else {
                n4 = 0;
                while (n4 < 10) {
                    this.dG[n4] = 0;
                    n3 = 0;
                    while (n3 < 5) {
                        this.dF[n4][n3] = 0;
                        ++n3;
                    }
                    ++n4;
                }
                this.dE = 0;
            }
            n4 = 0;
            while (n4 < 5) {
                if (this.cj[n4][0] == 7 && this.cj[n4][2] == 0) {
                    this.ck[n4][0] = this.ck[n4][0] <= this.ei ? 9999 - (this.ei - this.ck[n4][0]) : 9999 - (this.ck[n4][0] - this.ei);
                }
                ++n4;
            }
            if (this.dH == 1 && this.bH == 1) {
                this.dJ = this.dJ <= this.ei ? 9999 - (this.ei - this.dJ) : 9999 - (this.dJ - this.ei);
            } else {
                this.dH = 0;
                this.cE = 10;
            }
            if (this.aT > 0) {
                n4 = 0;
                while (n4 < 10) {
                    this.bn[n4].e = 0;
                    ++n4;
                }
            }
            this.bT = 0;
            this.ei = 0;
            this.ej = 0;
            this.bE = 0;
            this.bF = 0;
            this.ek = 0;
            this.em = 0;
            this.dL = 0;
            this.dM = 0;
            this.ca = this.cc * 20 + 10;
            this.cb = (this.cd + 1) * 20;
            this.cB = 0;
            this.cE = 10;
            this.df = 0;
            this.d(this.ca, this.cb);
            if (this.eC == 0 && this.fk == 0) {
                this.bD = 1;
            }
            this.cF = 0;
            this.cH = 0;
            this.cI = 0;
            this.cJ = 0;
            this.dn = 0;
            this.dA = 0;
            this.dB = 0;
            this.dc = 0;
            this.dd = 0;
            this.cU = 0;
            this.cx = 0;
            this.dT = 0;
            this.dU = 0;
            this.fd = -1;
            this.fe = -1;
            this.fi = 0;
            this.eb = 0;
            this.eh = 0;
            this.fg = 0;
            this.fj = 0;
            this.go = 0;
            this.gq = 0;
            this.eq = 0;
            this.gs = 0;
            this.dp = 0;
            this.dq = 0;
            this.ds = 0;
            this.di = 0;
            n4 = 0;
            while (n4 < 3) {
                this.aK[n4] = 0;
                ++n4;
            }
            n4 = 0;
            while (n4 < 5) {
                n3 = 0;
                while (n3 < 2) {
                    this.eo[n4][n3] = 1;
                    ++n3;
                }
                ++n4;
            }
            n4 = 0;
            while (n4 < 4) {
                n3 = 0;
                while (n3 < 2) {
                    this.dg[n4][n3] = 0;
                    ++n3;
                }
                ++n4;
            }
            n4 = 0;
            while (n4 < 5) {
                n3 = 0;
                while (n3 < 4) {
                    this.db[n4][n3] = 0;
                    ++n3;
                }
                ++n4;
            }
            n4 = 0;
            while (n4 < 4) {
                n3 = 0;
                while (n3 < 6) {
                    this.dN[n4][n3] = 0;
                    ++n3;
                }
                ++n4;
            }
            switch (this.bV) {
                case 1: {
                    this.en = 2;
                    break;
                }
                case 3: {
                    this.en = 1;
                    break;
                }
                default: {
                    this.en = 0;
                }
            }
            if (this.en > 0) {
                this.eo[0][0] = 30;
                this.eo[0][1] = 120;
                this.eo[1][0] = 110;
                this.eo[1][1] = 60;
                this.eo[2][0] = 190;
                this.eo[2][1] = 100;
                this.eo[3][0] = 270;
                this.eo[3][1] = 40;
                this.eo[4][0] = 350;
                this.eo[4][1] = 80;
                n4 = 0;
                while (n4 < 5) {
                    int[] nArray5 = this.eo[n4];
                    nArray5[1] = nArray5[1] - (this.en - 1) * 40;
                    ++n4;
                }
                this.ep = 0;
            }
            this.g(this.bV, 1);
            if (this.eC != 1 && this.fk != 1) {
                this.aM = 1;
                this.aN = this.ei;
            }
            this.aO = 0;
            this.at();
            this.au();
            this.as = this.bV == 57 ? 1 : 0;
            this.fa = this.gd = this.ca;
            this.fb = this.ge = this.cb;
            if (this.fm == 1) {
                switch (this.bV) {
                    case 57: 
                    case 58: 
                    case 59: 
                    case 79: {
                        this.bW = 0;
                        break;
                    }
                    default: {
                        this.bW = 1;
                        break;
                    }
                }
            } else {
                this.bW = 1;
            }
            if (this.eD > 51 && this.bV == 52) {
                this.aH[1][11 * this.bC[0] + 6] = 155;
                this.aH[2][11 * this.bC[0] + 6] = 10;
                this.aH[1][11 * this.bC[0] + 7] = 156;
                this.aH[2][11 * this.bC[0] + 7] = 10;
            }
            if (this.eD > 68) {
                this.aH[1][4 * this.bC[0] + 3] = 0;
                int[] nArray6 = this.aH[2];
                int n6 = 4 * this.bC[0] + 3;
                nArray6[n6] = nArray6[n6] - 10;
            }
            Runtime.getRuntime().gc();
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void i() {
        int n2;
        this.a(this.eD, 0);
        this.cF = 0;
        this.cT = 0;
        this.di = 0;
        this.cH = 0;
        this.cI = 0;
        this.dp = 0;
        this.cJ = 0;
        this.bD = 0;
        this.eE = 0;
        this.aM = 0;
        this.eb = 0;
        this.dA = 0;
        this.cQ = 0;
        this.gq = 0;
        this.eh = 0;
        int n3 = 0;
        while (n3 < 4) {
            n2 = 0;
            while (n2 < 6) {
                this.dN[n3][n2] = 0;
                ++n2;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.aT) {
            int n4;
            n2 = 0;
            while (n2 < 4) {
                n4 = 0;
                while (n4 < 2) {
                    this.aU[n3].o[n2][n4] = 0;
                    ++n4;
                }
                ++n2;
            }
            n2 = 0;
            while (n2 < 5) {
                n4 = 0;
                while (n4 < 4) {
                    this.aU[n3].x[n2][n4] = 0;
                    ++n4;
                }
                ++n2;
            }
            ++n3;
        }
        if (this.aT > 0) {
            n3 = 0;
            while (n3 < 20) {
                this.bl[n3].m = 0;
                ++n3;
            }
            n3 = 0;
            while (n3 < 10) {
                this.bn[n3].e = 0;
                ++n3;
            }
        }
        n3 = 0;
        while (n3 < this.aV) {
            if (this.aX[n3].k == 0) {
                this.aX[n3].m = 0;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 5) {
            n2 = 0;
            while (n2 < 4) {
                this.db[n3][n2] = 0;
                ++n2;
            }
            ++n3;
        }
        if (this.eF[10] > 0) {
            this.bq = 0;
            this.bs = this.eF[10];
        }
    }

    public final void j() {
        int n2;
        this.a(this.fl, 1);
        this.cF = 0;
        this.di = 0;
        this.cH = 0;
        this.cI = 0;
        this.dp = 0;
        this.cJ = 0;
        this.bD = 0;
        this.eE = 0;
        this.aM = 0;
        this.bX = 0;
        int n3 = 0;
        while (n3 < 4) {
            n2 = 0;
            while (n2 < 6) {
                this.dN[n3][n2] = 0;
                ++n2;
            }
            ++n3;
        }
        this.eZ = 1;
        this.fa = this.ca;
        this.fb = this.cb;
        n3 = 0;
        while (n3 < this.aT) {
            int n4;
            n2 = 0;
            while (n2 < 4) {
                n4 = 0;
                while (n4 < 2) {
                    this.aU[n3].o[n2][n4] = 0;
                    ++n4;
                }
                ++n2;
            }
            n2 = 0;
            while (n2 < 5) {
                n4 = 0;
                while (n4 < 4) {
                    this.aU[n3].x[n2][n4] = 0;
                    ++n4;
                }
                ++n2;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 5) {
            n2 = 0;
            while (n2 < 4) {
                this.db[n3][n2] = 0;
                ++n2;
            }
            ++n3;
        }
    }

    public final void a(int n2, int n3) {
        System.out.println("EventDialog--------" + n3 + "  " + n2);
        boolean bl = false;
        boolean bl2 = false;
        String[][] stringArray = null;
        if (n3 == 0) {
            stringArray = n2 <= 34 ? u.a[n2] : (n2 <= 81 ? i.a[n2 - 35] : h.a[n2 - 82]);
        } else if (n3 == 1) {
            stringArray = h.c[n2];
        }
        this.gJ = 1;
        int n4 = 0;
        while (n4 < stringArray.length) {
            this.a(stringArray[n4], n4, 230);
            ++n4;
        }
        this.eT = stringArray.length;
        System.gc();
        this.eS = 0;
        this.eQ = 0;
        this.eX = 0;
        this.eY = 0;
        this.ej = 0;
        this.gJ = 0;
    }

    public final void b(int n2, int n3) {
        System.out.println("NpcDialog----------------Kind = " + n3);
        boolean bl = false;
        String[][] stringArray = null;
        try {
            this.gJ = 1;
            if (n3 == 0) {
                stringArray = h.b[n2];
            } else if (n3 == 1) {
                stringArray = n2 <= 60 ? g.a[n2] : f.a[n2 - 61];
            } else if (n3 == 2) {
                stringArray = n2 <= 37 ? e.a[n2] : e.b[n2 - 38];
            } else if (n3 == 3) {
                stringArray = c.a[n2];
            } else if (n3 == 4) {
                stringArray = b.a[n2];
            } else if (n3 == 5) {
                stringArray = b.b[n2];
            }
            int n4 = 0;
            while (n4 < stringArray.length) {
                this.a(stringArray[n4], n4, 230);
                ++n4;
            }
            this.eT = stringArray.length;
            this.gJ = 0;
            System.gc();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void c(int n2) {
        int n3 = 0;
        int n4 = 0;
        boolean bl = false;
        System.out.println("InitBuy-----------Kind = " + n2);
        if (this.bg[n2 - 1] > 0) {
            n4 = 0;
            while (n4 < this.bg[n2 - 1]) {
                if (n2 == 1) {
                    if (this.bh[n4 * 5 + 2] <= this.eH[this.fD] && this.bh[n4 * 5 + 4] <= this.eD || this.fy == 7) {
                        ++n3;
                    }
                } else if (n2 == 2) {
                    if (this.bi[n4 * 5 + 2] <= this.eH[this.fD] && this.bi[n4 * 5 + 4] <= this.eD) {
                        ++n3;
                    }
                } else if (n2 == 3) {
                    if (this.bj[n4 * 5 + 2] <= this.eH[this.fD] && this.bj[n4 * 5 + 4] <= this.eD) {
                        ++n3;
                    }
                } else if (n2 == 4 && this.bk[n4 * 5 + 2] <= this.eH[this.fD] && this.bk[n4 * 5 + 4] <= this.eD) {
                    ++n3;
                }
                ++n4;
            }
            if (n3 < 16) {
                n3 = 15;
            } else if (n3 % 5 != 0) {
                n3 = (n3 / 5 + 1) * 5;
            }
            this.fB = n3;
            this.fA = new int[n3 * 5];
            n4 = 0;
            while (n4 < n3) {
                this.fA[n4 * 5 + 0] = 0;
                ++n4;
            }
            n3 = 0;
            n4 = 0;
            while (n4 < this.bg[n2 - 1]) {
                if (n2 == 1) {
                    if (this.bh[n4 * 5 + 2] <= this.eH[this.fD] && this.bh[n4 * 5 + 4] <= this.eD || this.fy == 7) {
                        this.fA[n3 * 5 + 0] = this.bh[n4 * 5 + 0];
                        this.fA[n3 * 5 + 1] = this.bh[n4 * 5 + 1];
                        this.fA[n3 * 5 + 2] = this.bh[n4 * 5 + 2];
                        this.fA[n3 * 5 + 3] = this.bh[n4 * 5 + 3];
                        this.fA[n3 * 5 + 4] = this.bh[n4 * 5 + 4];
                        ++n3;
                    }
                } else if (n2 == 2) {
                    if (this.bi[n4 * 5 + 2] <= this.eH[this.fD] && this.bi[n4 * 5 + 4] <= this.eD) {
                        this.fA[n3 * 5 + 0] = this.bi[n4 * 5 + 0];
                        this.fA[n3 * 5 + 1] = this.bi[n4 * 5 + 1];
                        this.fA[n3 * 5 + 2] = this.bi[n4 * 5 + 2];
                        this.fA[n3 * 5 + 3] = this.bi[n4 * 5 + 3];
                        this.fA[n3 * 5 + 4] = this.bi[n4 * 5 + 4];
                        ++n3;
                    }
                } else if (n2 == 3) {
                    if (this.bj[n4 * 5 + 2] <= this.eH[this.fD] && this.bj[n4 * 5 + 4] <= this.eD) {
                        this.fA[n3 * 5 + 0] = this.bj[n4 * 5 + 0];
                        this.fA[n3 * 5 + 1] = this.bj[n4 * 5 + 1];
                        this.fA[n3 * 5 + 2] = this.bj[n4 * 5 + 2];
                        this.fA[n3 * 5 + 3] = this.bj[n4 * 5 + 3];
                        this.fA[n3 * 5 + 4] = this.bj[n4 * 5 + 4];
                        ++n3;
                    }
                } else if (n2 == 4 && this.bk[n4 * 5 + 2] <= this.eH[this.fD] && this.bk[n4 * 5 + 4] <= this.eD) {
                    this.fA[n3 * 5 + 0] = this.bk[n4 * 5 + 0];
                    this.fA[n3 * 5 + 1] = this.bk[n4 * 5 + 1];
                    this.fA[n3 * 5 + 2] = this.bk[n4 * 5 + 2];
                    this.fA[n3 * 5 + 3] = this.bk[n4 * 5 + 3];
                    this.fA[n3 * 5 + 4] = this.bk[n4 * 5 + 4];
                    ++n3;
                }
                ++n4;
            }
        }
        this.eu = 0;
        this.ev = 0;
        this.ew = 0;
    }

    public final void k() {
        this.aH = null;
        this.aQ = null;
        this.aS = null;
        this.ba = null;
        this.aU = null;
        this.aX = null;
        this.bb = null;
        if (this.aT > 0) {
            this.bl = null;
            this.bn = null;
        }
        this.bm = null;
        this.bf = null;
        this.bh = null;
        this.bi = null;
        this.bj = null;
        this.bk = null;
        this.aD = null;
        this.aF = null;
    }

    public final void l() {
        int[] nArray = new int[]{120, 60, 100, 40, 80};
        this.eo[this.ep][0] = 350;
        this.eo[this.ep][1] = nArray[this.ep] - (this.en - 1) * 40;
        if (this.ep < 4) {
            ++this.ep;
            return;
        }
        this.ep = 0;
    }

    public final void m() {
        int[][] nArrayArray = new int[6][];
        nArrayArray[0] = new int[]{-10, -30, -7, 6};
        int[] nArray = new int[4];
        nArray[1] = -20;
        nArray[3] = 8;
        nArrayArray[1] = nArray;
        nArrayArray[2] = new int[]{10, -35, 9, 5};
        nArrayArray[3] = new int[]{8, -45, 8, -6};
        nArrayArray[4] = new int[]{2, -60, -2, -9};
        nArrayArray[5] = new int[]{-8, -52, -8, -7};
        int[][] nArrayArray2 = nArrayArray;
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int[] nArray2 = new int[]{3, 4, 5, 6, 6, 6};
        int[][] nArrayArray3 = new int[6][];
        int[] nArray3 = new int[3];
        nArray3[1] = 2;
        nArray3[2] = 4;
        nArrayArray3[0] = nArray3;
        int[] nArray4 = new int[4];
        nArray4[1] = 2;
        nArray4[2] = 3;
        nArray4[3] = 5;
        nArrayArray3[1] = nArray4;
        int[] nArray5 = new int[5];
        nArray5[1] = 1;
        nArray5[2] = 2;
        nArray5[3] = 3;
        nArray5[4] = 4;
        nArrayArray3[2] = nArray5;
        int[] nArray6 = new int[6];
        nArray6[1] = 1;
        nArray6[2] = 2;
        nArray6[3] = 3;
        nArray6[4] = 4;
        nArray6[5] = 5;
        nArrayArray3[3] = nArray6;
        int[] nArray7 = new int[6];
        nArray7[1] = 1;
        nArray7[2] = 2;
        nArray7[3] = 3;
        nArray7[4] = 4;
        nArray7[5] = 5;
        nArrayArray3[4] = nArray7;
        int[] nArray8 = new int[6];
        nArray8[1] = 1;
        nArray8[2] = 2;
        nArray8[3] = 3;
        nArray8[4] = 4;
        nArray8[5] = 5;
        nArrayArray3[5] = nArray8;
        int[][] nArrayArray4 = nArrayArray3;
        int n4 = this.ca - this.aI[0];
        int n5 = this.cb - this.aI[1];
        this.dv = nArray2[this.cm[0] - 1];
        int n6 = 0;
        while (n6 < this.dv) {
            this.du[n6][0] = n4 + nArrayArray2[nArrayArray4[this.cm[0] - 1][n6]][0];
            this.du[n6][1] = n5 + nArrayArray2[nArrayArray4[this.cm[0] - 1][n6]][1];
            this.du[n6][2] = 0;
            this.du[n6][3] = 0;
            this.du[n6][4] = 0;
            this.du[n6][5] = nArrayArray2[nArrayArray4[this.cm[0] - 1][n6]][2];
            this.du[n6][6] = nArrayArray2[nArrayArray4[this.cm[0] - 1][n6]][3];
            this.du[n6][7] = 0;
            this.du[n6][8] = 0;
            ++n6;
        }
        n2 = 40;
        n3 = 0;
        while (n2 < 161 && !bl) {
            n6 = 0;
            while (n6 < this.aT && !bl) {
                if (this.aU[n6].ay == 1 && this.aU[n6].ab < 5 || this.aU[n6].ab > 4 && this.aU[n6].aH == 1) {
                    int n7 = this.aU[n6].f - this.aI[0];
                    int n8 = this.aU[n6].g - this.aI[1];
                    if (n7 > 0 && n7 < this.bx[0] && n8 > 0 && n8 < this.bx[1] - 40) {
                        int n9 = this.t(n7, n4);
                        int n10 = this.t(n8, n5);
                        if (n9 >= 0 && n9 <= n2 && n10 >= 0 && n10 <= n2) {
                            this.du[n3][2] = n7;
                            this.du[n3][3] = n8 - 15;
                            this.du[n3][8] = n6;
                            if (++n3 == this.dv) {
                                bl = true;
                            }
                        }
                    }
                }
                ++n6;
            }
            n2 += 40;
        }
        if (n3 > 0) {
            while (n3 < this.dv) {
                this.du[n3][2] = this.du[n3 - 1][2];
                this.du[n3][3] = this.du[n3 - 1][3];
                this.du[n3][8] = this.du[n3 - 1][8];
                ++n3;
            }
        }
    }

    public final void n() {
        boolean bl = false;
        System.out.println("m_LoadState------------------" + this.ap);
        if (this.ap == 2) {
            this.L();
            this.I();
            Runtime.getRuntime().gc();
            this.ao = 0;
            this.bu = 23;
            this.bI = 0;
            this.eq = 0;
            this.bY = this.cl[0][0] == 2 ? 0 : 1;
            if (this.bW == 1 && this.bH == 1) {
                this.a("ch", 320);
                this.b("ch", 15993);
                this.v();
                this.p();
                this.a("sskill", 432);
                this.b("sskill", 24082);
                this.w();
                this.p();
            }
            this.e(0);
            this.bq = 0;
            Runtime.getRuntime().gc();
            this.ap = 0;
        } else if (this.ap == 3) {
            this.A();
            this.bu = 23;
            this.bv = 23;
            this.ao = 0;
            this.h();
            this.aj();
            this.bX = 1;
            this.bW = 1;
            this.a(1);
        } else if (this.ap == 4) {
            this.A();
            this.bu = 23;
            this.bv = 23;
            this.ao = 0;
            this.d();
            this.e();
            if (this.eu == 1) {
                this.fl = 5;
                this.bV = 27;
                this.cc = 6;
                this.cd = 6;
                this.cC = 0;
                this.cD = 0;
                this.bX = 1;
                this.bW = 1;
            }
            this.h();
            this.j();
            if (this.eu == 1) {
                this.bX = 1;
                this.bW = 1;
            }
            this.a(1);
        } else if (this.ap == 5) {
            this.ce[11] = this.ce[11] - 100;
            this.ce[2] = this.cf;
            this.ce[3] = this.cg;
            this.cc = 10;
            this.cd = 6;
            this.ca = this.cc * 20;
            this.cb = (this.cd + 1) * 20;
            this.cC = 1;
            this.fC = 1;
            this.d(this.ca, this.cb);
            this.fP = 2;
            this.fQ = 255;
            this.bD = 1;
            this.bI = 1;
            this.bT = 0;
            this.fd = -1;
            this.fj = 0;
            this.ap = 0;
        } else if (this.ap == 6) {
            this.aW();
            this.A();
            this.bu = 4;
            this.bv = 4;
            this.b();
            this.bI = 0;
            this.a(1);
        } else if (this.ap == 7) {
            this.F();
            if (this.bW == 1 && this.bH == 1) {
                this.G();
            }
            Runtime.getRuntime().gc();
            int n2 = 0;
            while (n2 < 2) {
                if (this.cq[this.cl[n2][1]][6] == 0) {
                    this.cs -= this.cq[this.cl[n2][1]][5];
                    this.j(this.cl[n2][0], this.cl[n2][1]);
                    int n3 = 0;
                    while (n3 < 12) {
                        this.cq[this.cl[n2][1]][n3] = 0;
                        ++n3;
                    }
                    this.cl[n2][0] = 0;
                    this.cl[n2][1] = 0;
                    if (n2 == 0 && this.cl[n2][0] == 0) {
                        this.bY = 1;
                    }
                }
                ++n2;
            }
            this.v(this.bZ);
            this.w(this.bZ);
            this.aj();
            this.a("ch", 320);
            this.b("ch", 15993);
            this.u();
            if (this.bW == 1 && this.bH == 1) {
                this.v();
            }
            this.p();
            Runtime.getRuntime().gc();
            this.go = 5;
            this.gp = this.ei;
            this.br = 1;
            this.bp = 14;
            this.ap = 0;
        } else if (this.ap == 8 || this.ap == 9) {
            this.A();
            if (this.ap == 8) {
                this.bu = 5;
                this.at = 0;
                this.aw = 1;
                this.av = 2;
                this.dU = 0;
            } else {
                this.bu = 4;
                this.b();
            }
            this.bv = 4;
            this.fW = 1;
            this.fV = 0;
            this.bq = 0;
            this.a(1);
        } else if (this.ap == 10) {
            this.bV = this.gG;
            this.cc = this.gH;
            this.cd = this.gI;
            this.cC = 0;
            this.cD = 0;
            this.aW();
            this.A();
            this.bu = 23;
            this.bv = 23;
            this.h();
            this.ao = 0;
            this.gF = 0;
            this.eu = 0;
            this.ev = 0;
            this.ew = 0;
            this.ex = 0;
            this.eA = 0;
            this.a(1);
        }
        if (this.ap == 1) {
            this.t = 1;
            this.o();
            this.t = 0;
            if (this.bv == 23) {
                this.fP = 2;
                this.fQ = 255;
                this.bD = 1;
                this.bI = 0;
            }
            this.ap = 0;
        }
    }

    public final void paint(Graphics graphics) {
        if (_s < 1) {
            if (this._CC < 20) {
                o._BB[this._CC] = this._o(_BB[this._CC]);
                ++this._CC;
            }
            graphics.setColor(0);
            graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            graphics.setColor(0xFFFFFF);
            if (this._e != null) {
                graphics.drawImage(this._e, this.getWidth() / 2, this.getHeight() / 2, 3);
            }
            return;
        }
        this.e = graphics;
        this.e.setFont(this.j);
        try {
            if (this.r) {
                this.c(0, 0, this.k, this.l, 0);
                this.a("\u6309\u786e\u5b9a\u952e\u7ee7\u7eed", this.m, this.n - this.p / 2, 0xFFFFFF, 17);
                return;
            }
            if (this.ap == 0 && this.t == 0) {
                boolean bl = false;
                if (this.bu < 4) {
                    switch (this.bu) {
                        case 1: {
                            this.aX();
                            break;
                        }
                        case 3: {
                            this.aA();
                            bl = true;
                        }
                    }
                } else if (this.bu < 13) {
                    this.aB();
                    bl = true;
                } else if (this.bu < 44) {
                    this.aD();
                }
                if (bl && g != 0 && this.bq == 0) {
                    this.bq = 1;
                }
                if (this.bN == 1) {
                    this.I(0);
                }
            }
            if (this.fP == 1) {
                this.bD = 0;
                this.bI = 1;
                if (this.fQ + 80 < 255) {
                    this.fQ += 80;
                } else {
                    this.fQ = 255;
                    this.fP = 0;
                }
            } else if (this.fP == 2) {
                this.bT = 0;
                if (this.fQ - 80 > 0) {
                    this.fQ -= 80;
                } else {
                    this.fQ = 0;
                    this.fP = 0;
                }
            }
            if (this.fP > 0) {
                this.f(0, 0, this.bx[0], this.bx[1], 0);
            }
            if (this.t == 1 && this.bu != 1) {
                this.f(0, 0, this.bx[0], this.bx[1], 0);
                this.b("\u52a0\u8f7d\u4e2d...", this.by[0], this.by[1] - 20, 1, 16766592);
                this.g(this.by[0] - 56, this.by[1], 112, 7, 14860183);
                this.f(this.by[0] - 55, this.by[1] + 1, this.s, 5, 16404992);
            }
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        this.h = (this.h + 1) % 100;
    }

    public final void d(int n2) {
        this.s = n2;
        this.repaint();
        this.serviceRepaints();
    }

    public final void o() {
        boolean bl = false;
        System.out.println("ImageLoad is Running...:" + this.bv);
        try {
            switch (this.bv) {
                case 1: {
                    this.q();
                    break;
                }
                case 3: {
                    this.r();
                    break;
                }
                case 4: {
                    this.s();
                    break;
                }
                case 23: {
                    this.Z = new Image[10];
                    this.aj = new Image[4];
                    this.ai = new Image[21];
                    this.O = new Image[16];
                    this.V = new Image[9];
                    this.U = new Image[3];
                    this.T = new Image[4];
                    this.ak = new Image[4];
                    this.al = new Image[13];
                    System.out.println("ImageLoad-GM_GAMEING-m_MyImageCheck:" + this.bW);
                    this.d(0);
                    this.t();
                    this.d(10);
                    if (this.aT > 0) {
                        this.y();
                    }
                    if (this.aV > 0) {
                        this.z();
                    }
                    this.d(30);
                    this.a("game", 480);
                    this.b("game", 14990);
                    int n2 = 0;
                    while (n2 < 3) {
                        this.V[6 + n2] = this.c(n2, 0);
                        ++n2;
                    }
                    n2 = 0;
                    while (n2 < 3) {
                        this.U[n2] = this.c(3 + n2, 0);
                        ++n2;
                    }
                    n2 = 0;
                    while (n2 < 2) {
                        this.T[n2] = this.c(6 + n2, 0);
                        ++n2;
                    }
                    if (this.bH == 1) {
                        n2 = 2;
                        while (n2 < 4) {
                            this.T[n2] = this.c(6 + n2, 0);
                            ++n2;
                        }
                        if (this.bZ == 0) {
                            this.ak[0] = this.c(49, 0);
                            this.ak[2] = this.c(52, 0);
                            this.ak[1] = this.c(50, 0);
                        } else {
                            this.ak[0] = this.c(51, 0);
                            this.ak[2] = this.c(52, 0);
                        }
                    }
                    n2 = 0;
                    while (n2 < 10) {
                        this.Z[n2] = this.c(19 + n2, 0);
                        ++n2;
                    }
                    if (this.en > 0) {
                        this.S = this.c(9 + this.en, 0);
                    }
                    n2 = 0;
                    while (n2 < 7) {
                        this.al[n2] = this.c(12 + n2, 0);
                        ++n2;
                    }
                    this.al[7] = this.c(53, 0);
                    n2 = 0;
                    while (n2 < 5) {
                        this.al[8 + n2] = this.c(54 + n2, 0);
                        ++n2;
                    }
                    n2 = 0;
                    while (n2 < 20) {
                        this.ai[n2] = this.c(29 + n2, 0);
                        ++n2;
                    }
                    this.ai[20] = this.c(59, 0);
                    this.p();
                    this.d(50);
                    this.v = this.c("/image/money");
                    this.w = this.c("/image/star");
                    if (this.bW == 1) {
                        this.a("ch", 320);
                        this.b("ch", 15993);
                        if (this.bY == 0) {
                            this.Q = this.c(12, 0);
                        }
                        this.an = this.c(39, 0);
                        this.u();
                        if (this.ao == 0 && this.bH == 1) {
                            this.v();
                        }
                        this.p();
                        this.a("sskill", 432);
                        this.b("sskill", 24082);
                        if (this.ao == 0 && this.bH == 1) {
                            this.w();
                        }
                        this.p();
                    }
                    this.d(82);
                    this.a("number", 32);
                    this.b("number", 743);
                    n2 = 0;
                    while (n2 < 4) {
                        this.aj[n2] = this.c(n2, 0);
                        ++n2;
                    }
                    this.p();
                    this.d(100);
                    if (this.ao == 1) {
                        this.e(1);
                        this.x();
                    } else {
                        this.e(0);
                    }
                    try {
                        this.I = Image.createImage((String)"/image/game/game_menu.png");
                        this.J = Image.createImage((String)"/image/game/game_status.png");
                        this.K = Image.createImage((String)"/image/game/game_map.png");
                        this.L = Image.createImage((String)"/image/game/doorflag.png");
                    }
                    catch (Exception exception) {
                        Exception exception2 = exception;
                        exception.printStackTrace();
                    }
                    this.d(110);
                }
            }
        }
        catch (Exception exception) {}
        Runtime.getRuntime().gc();
    }

    public final int a(String string) {
        if (string.compareTo("bg") == 0) {
            return 1;
        }
        if (string.compareTo("ch") == 0) {
            return 1;
        }
        if (string.compareTo("em") == 0) {
            return 2;
        }
        if (string.compareTo("enemy") == 0) {
            return 1;
        }
        if (string.compareTo("game") == 0) {
            return 2;
        }
        if (string.compareTo("intro") == 0) {
            return 2;
        }
        if (string.compareTo("item") == 0) {
            return 1;
        }
        if (string.compareTo("logo") == 0) {
            return 2;
        }
        if (string.compareTo("menu") == 0) {
            return 2;
        }
        if (string.compareTo("number") == 0) {
            return 1;
        }
        if (string.compareTo("obj") == 0) {
            return 1;
        }
        if (string.compareTo("sskill") == 0) {
            return 1;
        }
        return 0;
    }

    public final void a(String string, int n2) {
        this.x = string;
        this.gJ = 1;
        if (this.a(string) == 1) {
            String string2;
            String string3 = string2 = "/image/" + string + ".idx";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var8_7 = null;
            this.gD = inputStream = o._m(clazz, string3);
            try {
                this.gC = n2;
                this.gy = new byte[this.gC];
                this.gD.read(this.gy, 0, this.gC);
                this.gD.close();
                this.gD = null;
                Runtime.getRuntime().gc();
                Runtime.getRuntime().gc();
            }
            catch (Exception exception) {
                this.gD = null;
            }
        }
        this.gJ = 0;
    }

    public final int b(String string) {
        System.out.println("\u521d\u59cb\u5316\u56fe\u7247\u7684\u6570\u91cf===========:" + string);
        if (string.compareTo("bg") == 0) {
            return 585;
        }
        if (string.compareTo("ch") == 0) {
            return 40;
        }
        if (string.compareTo("em") == 0) {
            return 6;
        }
        if (string.compareTo("enemy") == 0) {
            return 432;
        }
        if (string.compareTo("game") == 0) {
            return 60;
        }
        if (string.compareTo("intro") == 0) {
            return 2;
        }
        if (string.compareTo("item") == 0) {
            return 62;
        }
        if (string.compareTo("logo") == 0) {
            return 24;
        }
        if (string.compareTo("menu") == 0) {
            return 31;
        }
        if (string.compareTo("number") == 0) {
            return 4;
        }
        if (string.compareTo("obj") == 0) {
            return 595;
        }
        if (string.compareTo("sskill") == 0) {
            return 54;
        }
        return 0;
    }

    public final void b(String string, int n2) {
        this.gJ = 1;
        if (this.a(string) == 1) {
            String string2;
            String string3 = string2 = "/image/" + string + ".dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var10_8 = null;
            this.gD = inputStream = o._m(clazz, string3);
            try {
                this.gC = n2;
                this.gB = new byte[this.gC];
                this.gD.read(this.gB, 0, this.gC);
                this.gD.close();
                this.gD = null;
                Runtime.getRuntime().gc();
            }
            catch (Exception exception) {
                this.gD = null;
            }
        } else {
            String string4 = "/image/" + string;
            String string5 = null;
            int n3 = this.b(string);
            this.gA = new Image[n3];
            try {
                int n4 = 0;
                while (n4 < n3) {
                    string5 = n4 < 10 ? String.valueOf(string4) + "/" + string + "_00" + n4 + ".png" : (n4 < 100 ? String.valueOf(string4) + "/" + string + "_0" + n4 + ".png" : String.valueOf(string4) + "/" + string + "_" + n4 + ".png");
                    this.gA[n4] = Image.createImage((String)string5);
                    ++n4;
                }
                Runtime.getRuntime().gc();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
        }
        this.gJ = 0;
    }

    public final void p() {
        if (this.gA != null) {
            int n2 = 0;
            while (n2 < this.gA.length) {
                this.gA[n2] = null;
                ++n2;
            }
        }
        this.gA = null;
        this.gB = null;
        this.gy = null;
        Runtime.getRuntime().gc();
    }

    public final Image c(int n2, int n3) {
        if (this.a(this.x) == 1) {
            try {
                this.y = null;
                int n4 = this.a(this.gy, n2 * 8);
                int n5 = this.a(this.gy, n2 * 8 + 4);
                this.gz = new byte[n4];
                System.arraycopy(this.gB, n5, this.gz, 0, n4);
                if (n3 > 0) {
                    System.out.println("\u8c03\u8272\u677f");
                    int n6 = l.a(this.gz);
                    int n7 = l.a(this.gz, n6);
                    if (n3 == 1 || n3 == 3) {
                        l.a(this.gz, n6, n7, n3);
                    } else if (n3 == 2) {
                        l.a(this.gz, n6, n7, this.aq, this.ar);
                    }
                    l.b(this.gz, n6);
                }
                this.y = Image.createImage((byte[])this.gz, (int)0, (int)n4);
                this.gz = null;
                Runtime.getRuntime().gc();
                return this.y;
            }
            catch (Exception exception) {
                System.out.println("Image load Error Index == " + n2 + "  Kind=== " + n3);
                return null;
            }
        }
        return this.gA[n2];
    }

    public final void q() {
        this.ae = new Image[24];
        this.d(0);
        this.u = new Image[3];
        int n2 = 0;
        while (n2 < 3) {
            this.u[n2] = this.c("image/logo/logo" + n2);
            ++n2;
        }
        System.out.println("\u8f7d\u5165\uff2c\uff2f\uff27\uff2f\u804c\u9a8a");
        n2 = 0;
        while (n2 < 24) {
            this.ae[n2] = this.c(n2, 0);
            if (n2 == 12) {
                this.d((n2 + 1) * 4);
            }
            ++n2;
        }
        this.p();
        this.d(110);
    }

    public final Image c(String string) {
        try {
            return Image.createImage((String)("/" + string + ".png"));
        }
        catch (IOException iOException) {
            IOException iOException2 = iOException;
            iOException.printStackTrace();
            return null;
        }
    }

    public final void r() {
        this.ae = new Image[2];
        this.d(0);
        this.a("intro", 40);
        this.b("intro", 41300);
        int n2 = 0;
        while (n2 < 2) {
            this.ae[n2] = this.c(n2, 0);
            ++n2;
        }
        this.p();
        this.d(110);
    }

    public final void s() {
        this.aj = new Image[4];
        this.aa = new Image[31];
        try {
            this.d(0);
            this.a("menu", 248);
            this.b("menu", 15716);
            int n2 = 0;
            while (n2 < 31) {
                this.aa[n2] = this.c(n2, 0);
                if (n2 == 15) {
                    this.d(n2 * 2);
                }
                ++n2;
            }
            this.p();
            this.d(70);
            this.a("number", 32);
            this.b("number", 743);
            n2 = 0;
            while (n2 < 4) {
                this.aj[n2] = this.c(n2, 0);
                ++n2;
            }
            this.p();
            this.d(110);
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void t() {
        System.out.println("LoadTile==========:\u8f7d\u5165\u5730\u56fe\u56fe\u5757\u5143\u7d20");
        this.M = new Image[100];
        this.N = new Image[150];
        int n2 = 0;
        this.ay = new int[255];
        this.az = new int[255];
        try {
            this.a("bg", 4680);
            this.b("bg", 82720);
            int n3 = 0;
            while (n3 < this.aC) {
                int n4 = this.aD[n3] - 1 + 1;
                if (n4 >= 160) {
                    n4 -= 160;
                    n4 = this.aA == 4 ? (n4 += 500) : (n4 += 100 * this.aA + 200);
                }
                if (this.as == 0) {
                    n2 = 0;
                } else if (this.as == 1) {
                    n2 = 1;
                }
                this.M[n3] = this.c(n4, n2);
                this.ay[this.aD[n3]] = n3;
                ++n3;
            }
            this.p();
            this.a("obj", 4760);
            this.b("obj", 141133);
            n3 = 0;
            while (n3 < this.aE) {
                int n5 = this.aF[n3] - 1 + 1;
                if (n5 >= 150) {
                    n5 -= 150;
                    n5 += 100 * this.aB + 200;
                }
                if (this.as == 0) {
                    n2 = 0;
                } else if (this.as == 1) {
                    n2 = 1;
                }
                this.N[n3] = this.c(n5, n2);
                this.az[this.aF[n3]] = n3;
                ++n3;
            }
            this.p();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void u() {
        int n2 = 0;
        int n3 = 0;
        while (n3 < 4) {
            if (this.as == 0) {
                n2 = 0;
                if (this.cl[1][0] > 0 && this.cq[this.cl[1][1]][7] - 26 > 0) {
                    this.a(this.cq[this.cl[1][1]][7] - 26, 2, 1);
                    n2 = 2;
                }
            } else if (this.as == 1) {
                n2 = 1;
            }
            this.O[n3] = this.c(n3, n2);
            ++n3;
        }
    }

    public final void a(int n2, int n3, int n4) {
        byte[] byArray = new byte[15];
        byte[] byArray2 = new byte[15];
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        if (n4 == 0) {
            n6 = 15;
            n8 = 8515 + (n3 - 1) * 60;
        } else {
            n6 = 12;
            n8 = 11389 + n3 * 84;
        }
        this.gJ = 1;
        try {
            String string = "/data/WineData.dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var14_13 = null;
            this.gD = inputStream = o._m(clazz, string);
            this.gE = new DataInputStream(this.gD);
            this.gE.skip(n8);
            this.gE.read(byArray, 0, n6);
            n7 = (n2 - 1) * n6;
            this.gE.skip(n7);
            this.gE.read(byArray2, 0, n6);
            this.g();
            n5 = 0;
            while (n5 < n6) {
                this.aq[n5] = byArray[n5] + 128;
                this.ar[n5] = byArray2[n5] + 128;
                ++n5;
            }
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void v() {
        this.R = new Image[16];
        boolean bl = false;
        int n2 = 0;
        boolean bl2 = false;
        int n3 = 4;
        if (this.bY == 1) {
            n3 = 8;
        }
        try {
            if (this.as == 0) {
                n2 = 0;
                if (this.cl[1][0] > 0 && this.cq[this.cl[1][1]][7] - 26 > 0) {
                    this.a(this.cq[this.cl[1][1]][7] - 26, 2, 1);
                    n2 = 2;
                }
            } else if (this.as == 1) {
                n2 = 1;
            }
            int n4 = 0;
            while (n4 < 4) {
                this.O[4 + n4] = this.c(n3 + n4, n2);
                ++n4;
            }
            if (this.bY == 0) {
                if (this.as == 0) {
                    n2 = 0;
                    if (this.cl[0][0] > 0 && this.cq[this.cl[0][1]][7] - 12 > 0) {
                        this.a(this.cq[this.cl[0][1]][7] - 12, 0, 1);
                        n2 = 2;
                    }
                } else if (this.as == 1) {
                    n2 = 1;
                }
                n4 = 0;
                while (n4 < 10) {
                    this.R[n4] = this.c(13 + n4, n2);
                    ++n4;
                }
            } else {
                if (this.as == 0) {
                    n2 = 0;
                    if (this.cl[0][0] > 0 && this.cq[this.cl[0][1]][7] - 19 > 0) {
                        this.a(this.cq[this.cl[0][1]][7] - 19, 1, 1);
                        n2 = 2;
                    }
                } else if (this.as == 1) {
                    n2 = 1;
                }
                n4 = 0;
                while (n4 < 16) {
                    this.R[n4] = this.c(23 + n4, n2);
                    ++n4;
                }
            }
            boolean bl3 = false;
            bl3 = false;
            Runtime.getRuntime().gc();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void w() {
        block21: {
            try {
                if (this.co <= 0) break block21;
                if (this.bZ == 0) {
                    if ((this.co - 1) % 3 == 0) {
                        int n2 = 0;
                        while (n2 < 3) {
                            this.O[8 + n2] = this.c(7 + n2, 0);
                            ++n2;
                        }
                    } else if ((this.co - 1) % 3 == 1) {
                        int n3 = 0;
                        while (n3 < 3) {
                            this.O[8 + n3] = this.c(29 + n3, 0);
                            ++n3;
                        }
                    } else if ((this.co - 1) % 3 == 2) {
                        int n4 = 0;
                        while (n4 < 3) {
                            this.O[8 + n4] = this.c(32 + n4, 0);
                            ++n4;
                        }
                    }
                } else if (this.bZ == 1) {
                    if ((this.co - 1) % 3 == 0) {
                        int n5 = 0;
                        while (n5 < 4) {
                            this.O[8 + n5] = this.c(35 + n5, 0);
                            ++n5;
                        }
                    } else if ((this.co - 1) % 3 == 1) {
                        int n6 = 0;
                        while (n6 < 4) {
                            this.O[8 + n6] = this.c(39 + n6, 0);
                            ++n6;
                        }
                    } else if ((this.co - 1) % 3 == 2) {
                        int n7 = 0;
                        while (n7 < 8) {
                            this.O[8 + n7] = this.c(43 + n7, 0);
                            ++n7;
                        }
                    }
                }
                this.P = this.c(51 + (this.co - 1) % 3, 0);
                return;
            }
            catch (Exception exception) {}
        }
    }

    public final void x() {
        this.aa = new Image[17];
        try {
            this.a("menu", 248);
            this.b("menu", 15716);
            int n2 = 14;
            while (n2 < 16) {
                this.aa[n2] = this.c(n2, 0);
                ++n2;
            }
            this.p();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void y() {
        int n2;
        int n3;
        this.W = new Image[48];
        this.X = new Image[4];
        this.am = new Image[3];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        byte[] byArray = new byte[40];
        try {
            this.a("enemy", 3456);
            this.b("enemy", 190612);
            n3 = 0;
            while (n3 < this.aZ) {
                n2 = this.ba[n3 * 5 + 0];
                int n8 = this.ba[n3 * 5 + 1];
                int n9 = this.ba[n3 * 5 + 2];
                n5 = this.ba[n3 * 5 + 3];
                switch (n8) {
                    case 0: {
                        n4 = 4;
                        break;
                    }
                    case 1: {
                        n4 = 8;
                        break;
                    }
                    case 2: {
                        n4 = 12;
                        break;
                    }
                    case 3: {
                        n4 = 2;
                    }
                }
                if (n2 > 0) {
                    if (n9 > 0) {
                        if (n9 == 4) {
                            n6 = 3;
                        } else {
                            n6 = 2;
                            this.a(n9, n2, 0);
                        }
                    } else {
                        n6 = 0;
                    }
                    int n10 = 0;
                    while (n10 < n4) {
                        this.W[n3 * 12 + n10] = this.c((n2 - 1) * 12 + n10, n6);
                        ++n10;
                    }
                }
                if (n5 > 0) {
                    this.X[n3] = this.c(324 + (n5 - 1), 0);
                }
                ++n3;
            }
            this.p();
            this.a("sskill", 432);
            this.b("sskill", 24082);
            if (this.gk > 0) {
                switch (this.gk / 10) {
                    case 1: {
                        n4 = 10;
                        break;
                    }
                    case 2: {
                        n4 = 7;
                        break;
                    }
                    case 3: {
                        n4 = 3;
                        break;
                    }
                    case 4: {
                        n4 = 0;
                    }
                }
                n3 = 0;
                while (n3 < 3) {
                    this.am[n3] = this.gk / 10 < 3 ? this.c(n4 + n3, 0) : this.c(29 + (n4 + n3), 0);
                    ++n3;
                }
            }
            this.p();
        }
        catch (Exception exception) {}
        this.gJ = 1;
        try {
            String string = "/data/WineData.dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var17_14 = null;
            this.gD = inputStream = o._m(clazz, string);
            this.gE = new DataInputStream(this.gD);
            this.gE.skip(1302);
            int n11 = 0;
            n3 = 0;
            while (n3 < this.aZ) {
                n2 = this.ba[n3 * 5 + 0];
                n7 = (n2 - n11 - 1) * 40;
                this.gE.skip(n7);
                this.gE.read(byArray, 0, 40);
                int n12 = 0;
                while (n12 < 40) {
                    this.aG[n3 * 40 + n12] = byArray[n12] + 128 - 100;
                    ++n12;
                }
                n11 = n2;
                ++n3;
            }
            this.g();
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void z() {
        this.Y = new Image[20];
        int n2 = 0;
        boolean bl = false;
        try {
            this.a("enemy", 3456);
            this.b("enemy", 190612);
            int n3 = 0;
            while (n3 < 10) {
                if (this.aW[n3] > 0) {
                    int n4;
                    if (this.aW[n3] < 10) {
                        n4 = 0;
                        while (n4 < 4) {
                            this.Y[n2 + n4] = this.c(340 + ((this.aW[n3] - 2) * 4 + n4), 0);
                            ++n4;
                        }
                        n2 += 4;
                    } else if (this.aW[n3] < 100) {
                        this.Y[n2] = this.c(340 + this.aW[n3], 0);
                        ++n2;
                    } else {
                        n4 = 0;
                        while (n4 < 4) {
                            this.Y[n2 + n4] = this.c((this.aW[n3] - 100 - 1) * 12 + n4, 0);
                            ++n4;
                        }
                        n2 += 4;
                    }
                }
                ++n3;
            }
            this.p();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void e(int n2) {
        this.af = new Image[50];
        this.ag = new Image[6];
        this.ah = new Image[4];
        boolean bl = false;
        int[] nArray = new int[6];
        int n3 = 1;
        int n4 = 1;
        int n5 = 0;
        while (n5 < 50) {
            this.cu[n5] = 0;
            ++n5;
        }
        try {
            this.a("item", 496);
            this.b("item", 15010);
            n5 = 0;
            while (n5 < 5) {
                if (this.cj[n5][0] != 0) {
                    if (this.cj[n5][0] == 1) {
                        int n6 = this.cq[this.cj[n5][1]][7];
                        this.cu[n6] = 1;
                    } else {
                        nArray[this.cj[n5][1]] = 1;
                    }
                }
                ++n5;
            }
            if (n2 == 1) {
                n5 = 1;
                while (n5 < 45) {
                    this.cu[n5] = 1;
                    ++n5;
                }
                n5 = 0;
                while (n5 < 6) {
                    nArray[n5] = 1;
                    ++n5;
                }
                n5 = 0;
                while (n5 < 4) {
                    if (n5 != 3) {
                        if (this.bZ == 0) {
                            n3 = n5;
                        } else if (this.bZ == 1) {
                            n3 = 3 + n5;
                        }
                    } else {
                        n3 = 6;
                    }
                    this.ah[n5] = this.c(45 + n3, 0);
                    ++n5;
                }
            }
            n5 = 0;
            while (n5 < 6) {
                if (nArray[n5] != 0) {
                    if (n5 < 4) {
                        if (this.bZ == 0) {
                            n4 = n5;
                        } else if (this.bZ == 1) {
                            n4 = 4 + n5;
                        }
                    } else {
                        n4 = 8 + n5 - 4;
                    }
                    this.ag[n5] = this.c(52 + n4, 0);
                }
                ++n5;
            }
            n5 = 1;
            while (n5 < 50) {
                if (this.cu[n5] != 0) {
                    this.af[n5] = this.c(n5, 0);
                }
                ++n5;
            }
            this.p();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void A() {
        switch (this.bv) {
            case 1: 
            case 3: {
                this.C();
                this.B();
                break;
            }
            case 4: {
                this.D();
                break;
            }
            case 23: {
                this.E();
                this.V = null;
                this.U = null;
                if (this.bW == 1) {
                    if (this.bY == 0) {
                        this.Q = null;
                    }
                    this.an = null;
                    this.F();
                    if (this.ao == 0 && this.bH == 1) {
                        this.G();
                        this.H();
                    }
                }
                this.T = null;
                if (this.en > 0) {
                    this.S = null;
                }
                if (this.aT > 0) {
                    this.J();
                }
                if (this.aV > 0) {
                    this.K();
                }
                this.Z = null;
                this.aj = null;
                this.ai = null;
                this.al = null;
                this.ak = null;
                this.v = null;
                this.w = null;
                this.L();
                if (this.eq == 1) {
                    this.I();
                }
                this.k();
            }
        }
        Runtime.getRuntime().gc();
    }

    public final void B() {
        this.ae = null;
        this.u = null;
    }

    public final void C() {
        this.ae = null;
    }

    public final void D() {
        this.aa = null;
        this.aj = null;
    }

    public final void E() {
        this.M = null;
        this.N = null;
    }

    public final void F() {
        int n2 = 0;
        while (n2 < 4) {
            this.O[n2] = null;
            ++n2;
        }
    }

    public final void G() {
        int n2 = 4;
        while (n2 < 8) {
            this.O[n2] = null;
            ++n2;
        }
        this.R = null;
    }

    public final void H() {
        if (this.co > 0) {
            int n2 = 8;
            while (n2 < 16) {
                this.O[n2] = null;
                ++n2;
            }
            this.P = null;
        }
    }

    public final void I() {
        this.aa = null;
    }

    public final void J() {
        this.W = null;
        this.X = null;
        this.am = null;
    }

    public final void K() {
        this.Y = null;
    }

    public final void L() {
        this.af = null;
        this.ah = null;
        this.ag = null;
    }

    public final int a(String string, int n2, int n3) {
        int n4 = 0;
        try {
            int n5 = n3;
            int n6 = 0;
            block2: do {
                int n7;
                String string2;
                if ((n6 = string.indexOf("|", 0)) == -1) {
                    string2 = string;
                } else {
                    string2 = string.substring(0, n6);
                    n7 = string.length();
                    string = string.substring(n6 + 1, n7);
                }
                block3: while (true) {
                    int n8;
                    if ((n8 = this.j.stringWidth(string2)) <= n5) {
                        this.eO[n4 + n2].a = string2;
                        ++n4;
                        continue block2;
                    }
                    n7 = string2.length();
                    int n9 = 1;
                    while (true) {
                        if (n9 > n7) continue block3;
                        n8 = this.j.substringWidth(string2, 0, n9);
                        if (n8 >= n5) {
                            String string3;
                            this.eO[n4 + n2].a = string3 = string2.substring(0, n9 - 1);
                            ++n4;
                            string2 = string2.substring(n9 - 1, n7);
                            continue block3;
                        }
                        ++n9;
                    }
                    break;
                }
            } while (n6 > 0);
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        return n4;
    }

    public final int a(String[] stringArray, int n2, int n3) {
        int n4 = 0;
        try {
            int n5 = n3;
            boolean bl = false;
            String string = stringArray[3];
            block2: while (true) {
                int n6;
                if ((n6 = this.j.stringWidth(string)) <= n5) {
                    this.eP[n2].a = (byte)Integer.parseInt(stringArray[0]);
                    this.eP[n2].b = (byte)Integer.parseInt(stringArray[1]);
                    this.eP[n2].c = (byte)Integer.parseInt(stringArray[2]);
                    this.eP[n2].d[n4] = string;
                    this.eP[n2].e = ++n4;
                    break;
                }
                int n7 = string.length();
                int n8 = 1;
                while (true) {
                    if (n8 > n7) continue block2;
                    n6 = this.j.substringWidth(string, 0, n8);
                    if (n6 >= n5) {
                        this.eP[n2].a = (byte)Integer.parseInt(stringArray[0]);
                        this.eP[n2].b = (byte)Integer.parseInt(stringArray[1]);
                        this.eP[n2].c = (byte)Integer.parseInt(stringArray[2]);
                        this.eP[n2].d[n4] = string.substring(0, n8 - 1);
                        this.eP[n2].e = ++n4;
                        string = string.substring(n8 - 1, n7);
                        continue block2;
                    }
                    ++n8;
                }
                break;
            }
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        return n4;
    }

    public final int c(String string, int n2) {
        int n3 = 0;
        try {
            int n4 = n2;
            int n5 = 0;
            block2: do {
                int n6;
                String string2;
                if ((n5 = string.indexOf("|", 0)) == -1) {
                    string2 = string;
                } else {
                    string2 = string.substring(0, n5);
                    n6 = string.length();
                    string = string.substring(n5 + 1, n6);
                }
                block3: while (true) {
                    int n7;
                    if ((n7 = this.j.stringWidth(string2)) <= n4) {
                        this.bP[n3] = string2;
                        ++n3;
                        continue block2;
                    }
                    n6 = string2.length();
                    int n8 = 1;
                    while (true) {
                        if (n8 > n6) continue block3;
                        n7 = this.j.substringWidth(string2, 0, n8);
                        if (n7 >= n4) {
                            String string3;
                            this.bP[n3] = string3 = string2.substring(0, n8 - 1);
                            ++n3;
                            string2 = string2.substring(n8 - 1, n6);
                            continue block3;
                        }
                        ++n8;
                    }
                    break;
                }
            } while (n5 > 0);
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        System.gc();
        return n3;
    }

    public final void f(int n2) {
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = false;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int[] nArray = new int[4];
        int n11 = 0;
        Object var14_14 = null;
        long l2 = 0L;
        Object var17_16 = null;
        l2 = o._n();
        if (l2 % 1800000L < 2000L && this.z == 0) {
            n4 = this.aU[n2].l < 10 ? 14 : (this.aU[n2].l >= 10 && this.aU[n2].l < 20 ? 15 : (this.aU[n2].l >= 20 && this.aU[n2].l < 30 ? 16 : this.s(0, 10) + 17));
            if (this.bU % 2 == 0) {
                var14_14 = null;
                l2 = 0L;
                var17_16 = null;
                l2 = o._n();
                n8 = l2 % 2L == 0L ? 4 : 5;
            } else {
                var14_14 = null;
                l2 = 0L;
                var17_16 = null;
                l2 = o._n();
                n8 = l2 % 2L == 0L ? 2 : 3;
            }
            n9 = n4;
            n10 = 1;
            bl = true;
            this.z = 1;
        } else {
            n5 = this.s(0, 2);
            if (n5 == 0) {
                if (this.aU[n2].as > 0) {
                    n6 = this.s(0, 100);
                    if (n6 <= this.aU[n2].au) {
                        n8 = this.aU[n2].as;
                        n9 = this.aU[n2].at;
                        n10 = 1;
                        bl = true;
                    } else {
                        bl = false;
                    }
                } else {
                    bl = false;
                }
            } else if (this.be > 0) {
                while (n7 < 4) {
                    n3 = 0;
                    if (this.eJ[n7][1] != 0) {
                        while (n3 < this.be && !bl) {
                            if (this.eJ[n7][2] == 3 && this.bf[n3 * 4 + 0] == this.eJ[n7][3] && this.bf[n3 * 4 + 1] == this.eJ[n7][4]) {
                                bl = true;
                                nArray[n11] = n3;
                                ++n11;
                                continue;
                            }
                            ++n3;
                        }
                    }
                    ++n7;
                }
                n3 = n11 == 0 ? this.s(0, this.be) : nArray[this.s(0, n11)];
                n6 = this.s(0, 100);
                if (n6 <= this.bf[n3 * 4 + 3]) {
                    n8 = this.bf[n3 * 4 + 0];
                    n9 = this.bf[n3 * 4 + 1];
                    n10 = this.bf[n3 * 4 + 2];
                    if (n10 == 0) {
                        n10 = 1;
                    }
                    bl = true;
                } else {
                    bl = false;
                }
            } else {
                bl = false;
            }
        }
        bl = bl && n8 > 0 && n8 < 13;
        if (bl) {
            n3 = 0;
            while (n3 < 10 && !bl2) {
                if (this.bm[n3].f == 0) {
                    bl2 = true;
                    continue;
                }
                ++n3;
            }
            if (bl2) {
                this.bm[n3].a = this.aU[n2].h;
                this.bm[n3].b = this.aU[n2].i;
                this.bm[n3].c = n8;
                this.bm[n3].d = n9;
                this.bm[n3].e = n10;
                this.bm[n3].g = 0;
                this.bm[n3].h = this.ei;
                this.bm[n3].i = 0;
                this.bm[n3].f = 1;
                this.bm[n3].j = n8 == 1 ? 8 : (n8 == 2 || n8 == 3 ? 9 : (n8 == 4 || n8 == 5 ? 10 : (n8 == 6 ? 11 : (n8 == 11 || n8 == 12 ? 12 : 3))));
                if (n8 < 13) {
                    this.c(n8, n9, 0);
                    this.bm[n3].k = this.fo;
                } else {
                    this.bm[n3].k = n9 * n10 + "\u4e21";
                }
            }
        }
        var14_14 = null;
        l2 = 0L;
        var17_16 = null;
        l2 = o._n();
        if (l2 % 1800000L >= 2000L) {
            this.z = 0;
        }
    }

    public final void d(int n2, int n3) {
        this.aI[0] = n2 <= this.bB[0] ? 0 : (n2 >= this.bC[0] * 20 - this.bB[0] ? (this.bC[0] * 20 > this.bx[0] ? this.bC[0] * 20 - this.bx[0] : 0) : n2 - (this.bx[0] >> 1));
        this.aI[1] = n3 <= this.bB[1] ? 0 : (n3 >= this.bC[1] * 20 - (this.bx[1] - this.bB[1] - 40) ? (this.bC[1] * 20 > this.bx[1] - 40 ? this.bC[1] * 20 - (this.bx[1] - 40) : 0) : n3 - this.bB[1]);
        this.aI[0] = this.aI[0] + this.aJ[0];
        this.aI[1] = this.aI[1] + this.aJ[1];
    }

    public final void M() {
        this.cc = this.ca / 20;
        if (this.cb % 20 == 0) {
            this.cd = this.cb / 20 - 1;
            return;
        }
        this.cd = this.cb / 20;
    }

    public final void N() {
        boolean bl = false;
        boolean bl2 = false;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (this.cF == 0) {
            if (this.cE < 20) {
                if (this.ca % this.cE != 0) {
                    this.ca = this.cc * 20 + 10;
                }
            } else if (this.ca % this.cE != 10) {
                this.ca = this.cc * 20 + 10;
            }
            if (this.cb % this.cE != 0) {
                this.cb = (this.cd + 1) * 20;
            }
        }
        if (this.bE == 1 && this.cF == 0) {
            if (this.cC == this.cD) {
                n2 = this.a(this.cc, this.cd, this.ca, this.cb, this.cD, this.cE, 0, 0);
                if (n2 == 0) {
                    if (this.cD < 2) {
                        n3 = this.a(this.cc, this.cd, this.ca, this.cb, 2, 20, 0, 0);
                        n4 = this.a(this.cc, this.cd, this.ca, this.cb, 3, 20, 0, 0);
                        if (n3 == 1 && n4 == 0) {
                            this.cC = 2;
                            bl = true;
                        } else if (n3 == 0 && n4 == 1) {
                            this.cC = 3;
                            bl = true;
                        } else if (n3 == 1 && n4 == 1) {
                            this.cC = this.ca % 20 < 10 ? 2 : 3;
                            bl = true;
                        } else if (n3 == 0 && n4 == 0) {
                            bl = false;
                        }
                    } else {
                        n3 = this.a(this.cc, this.cd, this.ca, this.cb, 0, 20, 0, 0);
                        n4 = this.a(this.cc, this.cd, this.ca, this.cb, 1, 20, 0, 0);
                        if (n3 == 1 && n4 == 0) {
                            this.cC = 0;
                            bl = true;
                        } else if (n3 == 0 && n4 == 1) {
                            this.cC = 1;
                            bl = true;
                        } else if (n3 == 1 && n4 == 1) {
                            this.cC = this.cb % 20 < 10 ? 0 : 1;
                            bl = true;
                        } else if (n3 == 0 && n4 == 0) {
                            bl = false;
                        }
                    }
                } else {
                    bl = n2 == 1;
                }
            } else {
                if (this.cC < 2) {
                    if (this.cb % 20 == 0) {
                        n3 = 1;
                    }
                } else if (this.ca % 20 == 10) {
                    n3 = 1;
                }
                if (n3 == 1 && this.a(this.cc, this.cd, this.ca, this.cb, this.cD, this.cE, 0, 0) == 1) {
                    this.cC = this.cD;
                }
                bl = (n2 = this.a(this.cc, this.cd, this.ca, this.cb, this.cC, this.cE, 0, 0)) == 1;
            }
            if (bl) {
                if (this.cC == 0) {
                    if (this.cb > 0) {
                        this.cb = this.cb - this.cE > 0 ? (this.cb -= this.cE) : 0;
                    }
                    bl2 = true;
                } else if (this.cC == 1) {
                    if (this.cb < this.bC[1] * 20) {
                        this.cb = this.cb + this.cE < this.bC[1] * 20 ? (this.cb += this.cE) : this.bC[1] * 20;
                    }
                    bl2 = true;
                } else if (this.cC == 2) {
                    if (this.ca > 0) {
                        this.ca = this.ca - this.cE > 0 ? (this.ca -= this.cE) : 0;
                    }
                    bl2 = true;
                } else if (this.cC == 3) {
                    if (this.ca < this.bC[0] * 20) {
                        this.ca = this.ca + this.cE < this.bC[0] * 20 ? (this.ca += this.cE) : this.bC[0] * 20;
                    }
                    bl2 = true;
                }
            } else {
                this.cD = this.cC;
                this.bE = 0;
                if (n2 == 5) {
                    this.fi = 1;
                }
            }
        }
        if (this.cd > 1) {
            int n5 = this.aH[3][(this.cd - 1) * this.bC[0] + this.cc] % 20;
            if (this.ek == 0) {
                if (n5 > 0 && n5 < 3) {
                    if (n5 == 1) {
                        this.em = this.aH[3][(this.cd - 1) * this.bC[0] + this.cc - 1] % 20 == 1 ? (this.cd - 2) * this.bC[0] + this.cc - 1 : (this.cd - 2) * this.bC[0] + this.cc;
                        this.el[0][0] = this.aH[1][this.em];
                        this.el[0][1] = this.aH[1][this.em + 1];
                        this.el[1][0] = this.aH[1][this.em + this.bC[0]];
                        this.el[1][1] = this.aH[1][this.em + 1 + this.bC[0]];
                        this.aH[1][this.em] = 0;
                        this.aH[1][this.em + 1] = 0;
                        this.aH[1][this.em + this.bC[0]] = 0;
                        this.aH[1][this.em + 1 + this.bC[0]] = 0;
                    }
                    this.ek = 1;
                    if (this.br == 0) {
                        this.br = 1;
                        this.bp = 8;
                    }
                }
            } else if (this.ek == 1 && n5 == 0) {
                this.ek = 0;
                this.aH[1][this.em] = this.el[0][0];
                this.aH[1][this.em + 1] = this.el[0][1];
                this.aH[1][this.em + this.bC[0]] = this.el[1][0];
                this.aH[1][this.em + 1 + this.bC[0]] = this.el[1][1];
            }
        }
        if (this.eZ == 0) {
            this.fa = this.ca;
            this.fb = this.cb;
        }
        this.d(this.fa, this.fb);
        n2 = 0;
        while (n2 < 10) {
            if (this.bm[n2].f == 1 && this.bm[n2].c < 13 && this.bm[n2].a == this.cc && this.bm[n2].b == this.cd) {
                this.fe = n2;
                break;
            }
            ++n2;
        }
        if (n2 == 10 && this.a(this.cc, this.cd, this.ca, this.cb, this.cC, this.cE, 0, 0) != 4) {
            this.fe = -1;
        }
        this.cB = bl2 ? (this.cB < 3 ? ++this.cB : 0) : 0;
        if (this.bG == 1 && this.bF == 0 && this.eC == 0) {
            this.bE = 0;
        }
    }

    public final int a(int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        int n10 = 0;
        int n11 = 0;
        boolean bl = false;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        if (n6 == 0) {
            n16 = n4 - 9;
            n17 = n4 + 9;
            n5 = n5 - n7 - 9;
        } else if (n6 == 1) {
            n16 = n4 - 9;
            n17 = n4 + 9;
            n5 += n7;
        } else if (n6 == 2) {
            n16 = n17 = n4 - 9 - n7;
        } else if (n6 == 3) {
            n16 = n17 = n4 + 9 + n7;
        }
        if (n16 < 0 || n5 < 0 || n17 >= this.bC[0] * 20 || n5 > this.bC[1] * 20) {
            n10 = 0;
        } else {
            if (n7 != 0) {
                n12 = n16 / 20;
                n13 = n17 / 20;
                n14 = n5 % 20 == 0 ? (n15 = n5 / 20 - 1) : (n15 = n5 / 20);
            } else {
                n12 = n13 = n2;
                n14 = n15 = n3;
            }
            n10 = this.a(n12, n13, n14, n15, n2, n3, n6);
            if (n8 == 0) {
                if (this.aT > 0) {
                    n11 = 0;
                    while (n11 < this.aT) {
                        if (this.aU[n11].ay == 1 && (this.aU[n11].h == n12 && this.aU[n11].i == n14 || this.aU[n11].h == n13 && this.aU[n11].i == n15)) {
                            if (this.aU[n11].ab == 2) {
                                n10 = 2;
                                break;
                            }
                            if (n10 == 1) {
                                n10 = 2;
                                break;
                            }
                        }
                        ++n11;
                    }
                }
                if (this.aV > 0) {
                    n11 = 0;
                    while (n11 < this.aV) {
                        if (this.aX[n11].l == 1 && (this.aX[n11].a == n12 && this.aX[n11].b == n14 || this.aX[n11].a == n13 && this.aX[n11].b == n15)) {
                            this.fd = n11;
                            n10 = 3;
                            break;
                        }
                        ++n11;
                    }
                    if (n10 != 3) {
                        this.fd = -1;
                    }
                }
            } else if (n8 == 1 && n10 == 1) {
                if (n9 < 99) {
                    n11 = 0;
                    while (n11 < this.aT) {
                        if (this.aU[n11].ay == 1 && n11 != n9 && (this.aU[n11].h == n12 && this.aU[n11].i == n14 || this.aU[n11].h == n13 && this.aU[n11].i == n15)) {
                            n10 = 0;
                            break;
                        }
                        ++n11;
                    }
                }
                if (n6 == 0 || n6 == 1) {
                    if (this.t(this.cb, n5) <= n7 && this.cc == n2) {
                        n10 = 2;
                    }
                } else if ((n6 == 2 || n6 == 3) && this.t(this.ca, n4) <= n7 && this.cd == n3) {
                    n10 = 2;
                }
            }
            if (n10 == 1) {
                n11 = 0;
                while (n11 < 10 && !bl) {
                    if (this.bm[n11].f == 1 && (this.bm[n11].a == n12 && this.bm[n11].b == n14 || this.bm[n11].a == n13 && this.bm[n11].b == n15)) {
                        if (n8 == 0) {
                            if (this.bm[n11].c < 13) {
                                n10 = 1;
                            } else {
                                n10 = 4;
                                this.fe = n11;
                            }
                        } else {
                            n10 = this.bm[n11].c < 13 ? 1 : 0;
                        }
                        bl = true;
                    }
                    ++n11;
                }
            }
        }
        return n10;
    }

    public final int a(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = 1;
        boolean bl = true;
        boolean bl2 = true;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        n10 = this.aH[2][n4 * this.bC[0] + n2] % 100 / 10;
        n11 = this.aH[2][n5 * this.bC[0] + n3] % 100 / 10;
        n12 = this.aH[2][n7 * this.bC[0] + n6] % 100 / 10;
        block0 : switch (n8) {
            case 0: {
                if (n4 != n7) {
                    if (n10 == 1 || n10 == 5) {
                        bl = false;
                    } else {
                        switch (n12) {
                            case 2: 
                            case 5: 
                            case 7: 
                            case 8: {
                                bl = false;
                            }
                        }
                    }
                }
                if (bl && n5 != n7) {
                    if (n11 == 1 || n11 == 5) {
                        bl2 = false;
                    } else {
                        switch (n12) {
                            case 2: 
                            case 5: 
                            case 7: 
                            case 8: {
                                bl2 = false;
                            }
                        }
                    }
                }
                if (bl && bl2) {
                    if (n2 == n3) break;
                    switch (n10) {
                        case 4: 
                        case 6: 
                        case 8: {
                            n9 = 0;
                        }
                    }
                    switch (n11) {
                        case 3: 
                        case 6: 
                        case 7: {
                            n9 = 0;
                        }
                    }
                    break;
                }
                n9 = 0;
                break;
            }
            case 1: {
                if (n4 != n7) {
                    switch (n10) {
                        case 1: 
                        case 2: 
                        case 5: 
                        case 7: 
                        case 8: {
                            bl = false;
                            break;
                        }
                        default: {
                            if (n12 != 5) break;
                            bl = false;
                        }
                    }
                }
                if (bl && n5 != n7) {
                    switch (n11) {
                        case 1: 
                        case 2: 
                        case 5: 
                        case 7: 
                        case 8: {
                            bl = false;
                            break;
                        }
                        default: {
                            if (n12 != 5) break;
                            bl = false;
                        }
                    }
                }
                if (bl) {
                    if (n2 == n3) break;
                    switch (n10) {
                        case 4: 
                        case 6: {
                            n9 = 0;
                        }
                    }
                    switch (n11) {
                        case 3: 
                        case 6: {
                            n9 = 0;
                        }
                    }
                    break;
                }
                n9 = 0;
                break;
            }
            case 2: {
                if (n2 == n6) break;
                switch (n10) {
                    case 1: 
                    case 4: 
                    case 6: 
                    case 8: {
                        n9 = 0;
                        break block0;
                    }
                }
                switch (n12) {
                    case 3: 
                    case 6: 
                    case 7: {
                        n9 = 0;
                    }
                }
                break;
            }
            case 3: {
                if (n2 == n6) break;
                switch (n10) {
                    case 1: 
                    case 3: 
                    case 6: 
                    case 7: {
                        n9 = 0;
                        break block0;
                    }
                }
                switch (n12) {
                    case 4: 
                    case 6: 
                    case 8: {
                        n9 = 0;
                    }
                }
            }
        }
        if (this.aH[3][n5 * this.bC[0] + n3] % 20 == 9 || this.aH[3][n4 * this.bC[0] + n2] % 20 == 9 || this.aH[3][n5 * this.bC[0] + n3] % 20 == 8 || this.aH[3][n4 * this.bC[0] + n2] % 20 == 8) {
            n9 = 5;
        }
        return n9;
    }

    public final int a(int n2, int[] nArray, int[] nArray2, int n3, int n4) {
        int n5 = -1;
        switch (n3) {
            case 0: {
                if (nArray[1] - nArray2[1] < 0 || nArray[1] - nArray2[1] > n2 || nArray2[2] != nArray[2]) break;
                if (n4 == 0) {
                    n5 = 1;
                    break;
                }
                n5 = nArray[1] - nArray2[1];
                break;
            }
            case 1: {
                if (nArray2[1] - nArray[1] < 0 || nArray2[1] - nArray[1] > n2 || nArray2[2] != nArray[2]) break;
                if (n4 == 0) {
                    n5 = 1;
                    break;
                }
                n5 = nArray2[1] - nArray[1];
                break;
            }
            case 2: {
                if (nArray[0] - nArray2[0] < 0 || nArray[0] - nArray2[0] > n2 || nArray2[3] != nArray[3]) break;
                if (n4 == 0) {
                    n5 = 1;
                    break;
                }
                n5 = nArray[0] - nArray2[0];
                break;
            }
            case 3: {
                if (nArray2[0] - nArray[0] < 0 || nArray2[0] - nArray[0] > n2 || nArray2[3] != nArray[3]) break;
                n5 = n4 == 0 ? 1 : nArray2[0] - nArray[0];
            }
        }
        return n5;
    }

    public final int a(int n2, int[] nArray, int n3, int n4, int n5) {
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 1;
        int n13 = 0;
        boolean bl = false;
        n7 = nArray[0];
        n8 = nArray[1];
        n9 = nArray[2];
        n10 = nArray[3];
        n13 = n4;
        if (n4 >= 90) {
            n13 = n4 - 90;
        }
        while (n11 < n2 && n12 == 1 && !bl) {
            n12 = this.a(n9, n10, n7, n8, n3, 10, n13, n5);
            if (n12 != 1) continue;
            switch (n3) {
                case 0: {
                    n8 -= 10;
                    break;
                }
                case 1: {
                    n8 += 10;
                    break;
                }
                case 2: {
                    n7 -= 10;
                    break;
                }
                case 3: {
                    n7 += 10;
                }
            }
            n9 = n7 / 20;
            n10 = n8 % 20 == 0 ? n8 / 20 - 1 : n8 / 20;
            if (n10 < 0 || n10 > this.bC[1] - 1 || n9 < 0 || n9 > this.bC[0] - 1) {
                bl = true;
                continue;
            }
            n11 += 10;
        }
        n6 = n4 >= 90 ? n12 : n11;
        return n6;
    }

    public final int O() {
        int n2 = 0;
        int n3 = 0;
        if (this.cc == 0 || this.cd == 0 || this.cc == this.bC[0] - 1 || this.cd == this.bC[1] - 1) {
            while (n3 == 0 && n2 < this.aP) {
                if (this.aQ[n2 * 9 + 8] == 0 || this.aQ[n2 * 9 + 7] < this.eD && this.aQ[n2 * 9 + 8] > this.eD) {
                    if (this.cC == this.aQ[n2 * 9 + 0] && this.bE == 1) {
                        if (this.cC < 2) {
                            if (this.cc >= this.aQ[n2 * 9 + 1] && this.cc <= this.aQ[n2 * 9 + 2] && this.cd == this.cC * (this.bC[1] - 1)) {
                                n3 = 1;
                                continue;
                            }
                            ++n2;
                            continue;
                        }
                        if (this.cd >= this.aQ[n2 * 9 + 1] && this.cd <= this.aQ[n2 * 9 + 2] && this.cc == (this.cC - 2) * (this.bC[0] - 1)) {
                            n3 = 1;
                            continue;
                        }
                        ++n2;
                        continue;
                    }
                    ++n2;
                    continue;
                }
                ++n2;
            }
        }
        if (n3 == 1) {
            this.bV = this.aQ[n2 * 9 + 3];
            this.cc = this.aQ[n2 * 9 + 4];
            this.cd = this.aQ[n2 * 9 + 5];
            this.cD = this.cC = this.aQ[n2 * 9 + 6];
            this.ca = this.cc * 20 + 10;
            this.cb = (this.cd + 1) * 20;
        }
        return n3;
    }

    public final int P() {
        int n2 = 0;
        int n3 = 0;
        while (n2 == 0 && n3 < this.aR) {
            if (this.aS[n3 * 10 + 9] == 0 || this.aS[n3 * 10 + 8] < this.eD && this.aS[n3 * 10 + 9] > this.eD) {
                if (this.aS[n3 * 10 + 0] <= this.cc && this.cc <= this.aS[n3 * 10 + 1] && this.aS[n3 * 10 + 2] <= this.cd && this.cd <= this.aS[n3 * 10 + 3]) {
                    n2 = 1;
                    continue;
                }
                ++n3;
                continue;
            }
            ++n3;
        }
        if (n2 == 1) {
            this.bV = this.aS[n3 * 10 + 4];
            this.cc = this.aS[n3 * 10 + 5];
            this.cd = this.aS[n3 * 10 + 6];
            this.cD = this.cC = this.aS[n3 * 10 + 7];
            this.ca = this.cc * 20 + 10;
            this.cb = (this.cd + 1) * 20;
        }
        return n2;
    }

    public final int g(int n2) {
        int n3 = 0;
        switch (n2) {
            case 0: {
                n3 = 1;
                break;
            }
            case 1: {
                n3 = 0;
                break;
            }
            case 2: {
                n3 = 3;
                break;
            }
            case 3: {
                n3 = 2;
            }
        }
        return n3;
    }

    public final void Q() {
        boolean bl = false;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        boolean bl2 = false;
        int n9 = 0;
        boolean bl3 = false;
        int[] nArray = new int[4];
        boolean bl4 = false;
        boolean bl5 = false;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0;
        if (this.cQ == 1) {
            this.cF = 2;
            this.dc += this.cR;
            this.cW = this.cS;
            this.cQ = 0;
            this.dd = 1;
            n17 = 1;
        }
        this.cR = 0;
        this.de = 0;
        if (this.dV == 1 && this.eC == 0) {
            n4 = this.ei - this.dY;
            if (n4 < 0) {
                n4 += 9999;
            }
            if (n4 >= this.dW) {
                this.cF = 2;
                this.dc += this.dX;
                this.cY = 0;
                this.dd = 1;
                this.dY = this.ei;
                this.de = 1;
                n17 = 1;
            }
        }
        n5 = 0;
        while (n5 < this.aT) {
            n8 = 0;
            n7 = 0;
            bl3 = false;
            if (this.aU[n5].ay == 1) {
                if (this.aU[n5].r > 0) {
                    if (this.cJ == 0) {
                        n10 = 70 + (this.ce[5] + this.ce[15] % 10000 - this.aU[n5].ag);
                        n11 = this.s(0, 100);
                        if (n11 <= n10) {
                            n8 = n11 <= n10 * (20 + this.ce[14]) / 100 ? 2 : 1;
                            if (this.cl[0][0] > 0) {
                                n12 = this.ce[12] - this.ce[13] % 10000;
                                this.cP = this.ce[13] % 10000 + this.s(0, n12);
                                if (this.cl[0][0] > 0) {
                                    if (this.cq[this.cl[0][1]][6] > 0) {
                                        int[] nArray2 = this.cq[this.cl[0][1]];
                                        nArray2[6] = nArray2[6] - 1;
                                    }
                                    if (this.cq[this.cl[0][1]][6] < 20) {
                                        this.go = 5;
                                        this.gp = this.ei;
                                        if (this.br == 0) {
                                            this.br = 1;
                                            this.bp = 14;
                                        }
                                    }
                                }
                            } else {
                                this.cP = 0;
                            }
                            this.cO = this.ce[9];
                        } else {
                            n8 = 0;
                        }
                    } else {
                        n8 = 1;
                    }
                    if (this.cl[0][0] > 0) {
                        n12 = this.ce[13] / 10000;
                        n12 = n12 == 0 ? 3 : --n12;
                        n16 = this.a(0, n12, this.aU[n5].aa, this.cP);
                    }
                    n12 = this.co == 0 ? 3 : (this.co - 1) % 3;
                    n15 = this.a(0, n12, this.aU[n5].aa, this.cO);
                    n15 += n16;
                }
                if (this.aU[n5].D > 0) {
                    n10 = 70 + (this.aU[n5].ag - (this.ce[5] + this.ce[15] % 10000));
                    n11 = this.s(0, 100);
                    n7 = n11 <= n10 ? (n11 <= n10 * 20 / 100 ? 2 : 1) : 0;
                    if (n7 > 0) {
                        n17 = n7;
                        n9 = this.aU[n5].aF;
                        this.aU[n5].aF = 0;
                        if (this.aU[n5].V == 1) {
                            n9 /= 2;
                        }
                        if (this.cl[1][0] > 0) {
                            n12 = this.ce[16] / 10000;
                            n12 = n12 == 0 ? 3 : --n12;
                            n14 = this.a(0, n12, this.aU[n5].aa, this.ce[16] % 10000);
                            if (this.cl[1][0] > 0) {
                                if (this.cq[this.cl[1][1]][6] > 0) {
                                    int[] nArray3 = this.cq[this.cl[1][1]];
                                    nArray3[6] = nArray3[6] - 1;
                                }
                                if (this.cq[this.cl[1][1]][6] < 20) {
                                    this.go = 5;
                                    this.gp = this.ei;
                                    if (this.br == 0) {
                                        this.br = 1;
                                        this.bp = 14;
                                    }
                                }
                            }
                        } else {
                            n14 = 0;
                        }
                        n12 = this.co == 0 ? 3 : (this.co - 1) % 3;
                        n13 = this.a(0, n12, this.aU[n5].aa, this.ce[10]);
                        n9 -= n14 + n13;
                        if (n7 == 2) {
                            n9 *= 2;
                        }
                        if (n9 <= 0) {
                            n9 = 1;
                        }
                        if (this.aU[n5].r > 0 && n8 > 0 && this.dc == 0) {
                            this.cQ = 1;
                            this.cR += n9;
                            this.cS = this.cW;
                            this.cY = 2;
                        } else if (this.cF < 3) {
                            this.cQ = 0;
                            this.dd = 1;
                            this.cD = this.cC = this.g(this.cW);
                            this.dc += n9 + this.cR;
                            this.cY = this.aU[n5].D == 1 ? (n7 == 1 ? 2 : 3) : 5;
                        }
                        if (this.aU[n5].aI > -1) {
                            if (this.dV == 0) {
                                this.dY = this.ei;
                                this.dW = this.aU[n5].al;
                                this.dX = this.aU[n5].ak;
                                this.dV = 1;
                            }
                            this.aU[n5].aG = 1;
                            this.dZ = this.ei;
                            this.ea = this.aU[n5].ai;
                            this.cE = 5;
                            this.aU[n5].aI = -1;
                        }
                    } else {
                        this.dc += 0;
                        this.dd = 1;
                    }
                }
                if (this.aU[n5].r > 0) {
                    if (n8 > 0) {
                        this.eg = this.ei;
                        this.eh = 1;
                        this.ef = n5;
                        if (n8 == 2) {
                            n15 = this.h(n15);
                        }
                        n9 = n15 - this.aU[n5].af;
                        if ((this.bY != this.bZ || this.cl[0][0] == 0) && n9 > 20) {
                            n9 = 20;
                        }
                        if (n9 <= 0) {
                            n9 = 1;
                        }
                        if (this.aU[n5].D > 0 && n7 > 0) {
                            this.aU[n5].k = 3;
                            this.aU[n5].p = n9;
                            this.aU[n5].n = this.dk;
                        } else {
                            this.aU[n5].j = 3;
                            this.aU[n5].k = 5;
                            this.aU[n5].z = n9;
                            this.aU[n5].n = this.dk;
                            if (this.aU[n5].ab < 2) {
                                this.aU[n5].b = this.aU[n5].a = this.g(this.cC);
                            }
                        }
                        this.aU[n5].s = this.cI;
                        if (this.co > 0 && this.bY == this.bZ && this.cl[0][0] > 0 && this.cH == this.cG - 1) {
                            if (this.cp[this.co - 1][0] > 2) {
                                this.aU[n5].n = 0;
                                this.aU[n5].H = 4;
                                this.dn = 10;
                            }
                        } else {
                            this.aU[n5].H = this.cJ == 0 && n8 == 2 ? 3 : this.dl;
                        }
                        this.aU[n5].F = this.cC;
                        this.aU[n5].w = this.cN;
                        if (this.aU[n5].ab > 1) {
                            this.aU[n5].H = 0;
                            this.aU[n5].n = 0;
                        }
                    } else {
                        if (this.aU[n5].D <= 0 || n7 != 1) {
                            if (this.ei % 2 == 0 && this.aU[n5].H > 0) {
                                this.aU[n5].k = 10;
                                this.aU[n5].j = 10;
                                this.i(n5);
                                this.j(n5);
                            } else {
                                this.aU[n5].q = 1;
                            }
                            this.aU[n5].z = 0;
                            bl3 = true;
                        }
                        if (this.br == 0) {
                            this.br = 1;
                            this.bp = 2 + this.bY * 2;
                        }
                    }
                }
                if (this.aU[n5].j == 3 && this.aU[n5].z > 0) {
                    bl3 = true;
                    if (this.aU[n5].s == 2) {
                        this.aU[n5].Q[0] = this.aU[n5].Q[0] == 0 ? 2 : 0;
                        this.aU[n5].o[this.aU[n5].Q[0]][1] = 1;
                        this.aU[n5].o[this.aU[n5].Q[0]][0] = 0;
                    } else if (this.aU[n5].s == 3) {
                        this.aU[n5].Q[1] = this.aU[n5].Q[1] == 1 ? 3 : 1;
                        this.aU[n5].o[this.aU[n5].Q[1]][1] = 1;
                        this.aU[n5].o[this.aU[n5].Q[1]][0] = 0;
                    }
                    if (this.cJ == 0 || this.bY == 0) {
                        this.br = 1;
                        if (this.bp != 15) {
                            this.bp = n8 == 2 ? 15 : 3;
                        }
                    }
                    if (this.aU[n5].n > 0) {
                        this.aU[n5].t = (this.dh + 1) % 3;
                        this.aU[n5].u = 1;
                        if (this.aU[n5].n == 1) {
                            n3 = 10;
                        } else if (this.aU[n5].n == 2) {
                            n3 = 20;
                        }
                        if (this.a(this.aU[n5].h, this.aU[n5].i, this.aU[n5].f, this.aU[n5].g, this.aU[n5].F, n3, 1, n5) == 1) {
                            switch (this.aU[n5].F) {
                                case 0: {
                                    this.aU[n5].g -= n3;
                                    break;
                                }
                                case 1: {
                                    this.aU[n5].g += n3;
                                    break;
                                }
                                case 2: {
                                    this.aU[n5].f -= n3;
                                    break;
                                }
                                case 3: {
                                    this.aU[n5].f += n3;
                                }
                                default: {
                                    break;
                                }
                            }
                        }
                    } else {
                        ++this.aU[n5].J;
                    }
                }
                if (bl3) {
                    if (this.aU[n5].z > 0 && n8 == 0) {
                        n8 = 1;
                    }
                    this.aU[n5].x[4][0] = n8 + 1;
                    this.aU[n5].x[4][1] = this.aU[n5].z;
                    n4 = 0;
                    if (n8 == 2) {
                        n6 = 0;
                        while (n6 < 5) {
                            if (this.aU[n5].x[n6][0] == 3) {
                                ++n4;
                            }
                            ++n6;
                        }
                        if (n4 > 1) {
                            this.aU[n5].x[4][3] = this.aU[n5].g - this.W[this.aU[n5].ar * 12].getHeight() - (n4 - 1) * 20;
                        }
                        this.aU[n5].x[4][2] = this.aU[n5].f;
                    } else {
                        this.aU[n5].x[4][2] = this.aU[n5].y == 0 ? this.aU[n5].f - 10 : this.aU[n5].f + 10;
                    }
                    if (n4 < 2) {
                        this.aU[n5].x[4][3] = this.aU[n5].g - this.W[this.aU[n5].ar * 12].getHeight();
                    }
                    this.aU[n5].y = this.aU[n5].y == 0 ? 1 : 0;
                    this.aU[n5].ah -= this.aU[n5].z;
                    if (this.aU[n5].ah < 0) {
                        this.aU[n5].ah = 0;
                        this.aU[n5].az = this.co;
                    }
                    this.aU[n5].z = 0;
                }
            }
            this.m(n5);
            ++n5;
        }
        bl3 = false;
        if (this.dd > 0) {
            bl3 = true;
        } else {
            if (this.cF == 1 && (this.cI == 2 || this.cI == 3)) {
                if ((this.dk == 1 || this.dk == 2) && this.a(this.cc, this.cd, this.ca, this.cb, this.cC, this.cE, 0, 0) == 1) {
                    bl = true;
                    n2 = this.cC;
                    n3 = 0;
                    if (this.dk == 1) {
                        n3 = 10;
                    } else if (this.dk == 2) {
                        n3 = 20;
                    }
                }
            } else if (this.cF == 2) {
                this.cJ = 0;
                bl3 = true;
            } else if (this.cF == 3) {
                this.cF = 4;
                nArray[0] = this.ca;
                nArray[1] = this.cb;
                nArray[2] = this.cc;
                nArray[3] = this.cd;
                if (this.cY != 0) {
                    this.cD = this.cC = this.g(this.cW);
                }
                if (this.cY == 2) {
                    this.cX = this.a(20, nArray, this.cW, 0, 0);
                } else if (this.cY == 3) {
                    this.cX = this.a(60, nArray, this.cW, 0, 0);
                    this.cV = this.cX / 10;
                    this.cZ = 0;
                    this.da = 20;
                } else if (this.cY == 5) {
                    this.cZ = 0;
                    this.da = 20;
                }
            }
            if (this.cF == 4) {
                if (this.cY == 2) {
                    bl = true;
                    n2 = this.cW;
                    n3 = this.cX;
                    this.cT = 1;
                    this.di = 1;
                    this.cF = 0;
                } else if (this.cY == 3 || this.cY == 5) {
                    if (this.cZ < 6) {
                        if (this.cY == 3) {
                            bl = true;
                            n2 = this.cW;
                            n3 = this.cV * s.gK[this.cZ];
                        } else {
                            bl = false;
                            n2 = this.cW;
                            n3 = 0;
                        }
                        this.cT = 2;
                        this.di = 1;
                        this.da = s.gL[this.cZ];
                        ++this.cZ;
                    } else if (this.cZ == 6) {
                        this.cF = 0;
                        this.da = 0;
                    }
                } else {
                    this.cF = 0;
                }
            }
        }
        if (bl3) {
            if (this.dc > 0) {
                if (this.fm == 0) {
                    this.ce[2] = this.ce[2] - this.dc;
                    if (this.ce[2] < 0) {
                        this.ce[2] = 0;
                        this.bI = 1;
                        this.eE = 0;
                        this.fV = 255;
                        this.bu = 38;
                    }
                }
                if (this.bu != 38) {
                    this.br = 1;
                    this.bp = 3;
                }
                this.dA = 0;
                this.cJ = 0;
                this.cF = 2;
                this.bD = 0;
                this.eb = 0;
                this.dh = this.s(0, 3);
                this.di = 1;
                if (n17 == 1) {
                    this.r(50, 100);
                } else if (n17 == 2) {
                    this.r(80, 300);
                }
                n2 = this.cW;
                bl = true;
                this.df = this.df == 0 ? 1 : 0;
                this.dg[this.df][1] = 1;
                this.dg[this.df][0] = 0;
                this.cT = this.df + 1;
            }
            this.db[4][0] = n17 + 1;
            this.db[4][1] = this.dc;
            this.db[4][2] = this.ca;
            n4 = 0;
            if (n17 == 2) {
                n6 = 0;
                while (n6 < 5) {
                    if (this.db[n6][0] == 3) {
                        ++n4;
                    }
                    ++n6;
                }
                if (n4 > 1) {
                    this.db[4][3] = this.cb - 33 - (n4 - 1) * 20;
                }
            }
            if (n4 < 2) {
                this.db[4][3] = this.cb - 33;
            }
        }
        if (bl) {
            if (this.a(this.cc, this.cd, this.ca, this.cb, n2, n3, 0, 0) == 1) {
                this.di = 1;
                switch (n2) {
                    case 0: {
                        this.cb -= n3;
                        break;
                    }
                    case 1: {
                        this.cb += n3;
                        break;
                    }
                    case 2: {
                        this.ca -= n3;
                        break;
                    }
                    case 3: {
                        this.ca += n3;
                    }
                }
                this.M();
                this.d(this.ca, this.cb);
                return;
            }
        } else {
            this.di = 0;
        }
    }

    public final int h(int n2) {
        int n3 = 0;
        n3 = this.ce[5] > 200 ? n2 * 4 : (this.ce[5] > 100 ? n2 * 3 : n2 * 2);
        return n3;
    }

    public final void R() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        int n8 = 0;
        n4 = 0;
        while (n4 < this.aT) {
            if (this.aU[n4].ay == 1) {
                bl = false;
                n7 = 0;
                bl2 = false;
                this.aU[n4].r = 0;
                if (this.bZ == 0) {
                    if (this.dr == 0 && this.aU[n4].R > 0) {
                        this.aU[n4].j = 3;
                        this.aU[n4].k = 10;
                        this.aU[n4].H = this.dl;
                        this.aU[n4].n = this.dk;
                        this.aU[n4].w = this.cN;
                        --this.aU[n4].R;
                        n8 = this.ce[4] + this.ce[7] / 3;
                        if (this.cm[0] == 5) {
                            n8 = n8 * 15 / 10;
                        } else if (this.cm[0] == 6) {
                            n8 *= 2;
                        }
                        this.aU[n4].z = n8 / 2;
                        bl = true;
                        bl2 = true;
                    } else if (this.dr == 1 && this.dq == 2) {
                        n5 = 0;
                        while (n5 < 6) {
                            if (this.du[n5][3] == 1 && this.du[n5][2] == 4) {
                                n7 = 1;
                            } else if (this.du[n5][3] == 1 && this.du[n5][2] > 4) {
                                n7 = 2;
                            }
                            if (n7 > 0 && (this.aU[n4].ab < 5 || this.aU[n4].aH == 1) && this.aU[n4].h == this.du[n5][0] && this.aU[n4].i == this.du[n5][1]) {
                                this.aU[n4].j = 3;
                                this.aU[n4].k = 10;
                                this.aU[n4].H = 5;
                                this.aU[n4].w = 1 + this.ei % 2;
                                this.aU[n4].n = 0;
                                bl = true;
                                if (n7 == 1) {
                                    n8 = (this.ce[9] + this.ce[12]) * 15 / 10;
                                    if (this.cm[1] > 5) {
                                        n8 *= 2;
                                    }
                                    this.aU[n4].z = n8;
                                    bl2 = true;
                                }
                            }
                            ++n5;
                        }
                    } else if (this.dr == 2 && this.dq > 1) {
                        n8 = this.cm[2] < 6 ? this.ce[4] * 4 : this.ce[4] * 5;
                        n5 = 0;
                        while (n5 < 6) {
                            n6 = 0;
                            while (n6 < 4) {
                                if (this.dK[n5][n6][3] == 1 && (this.aU[n4].ab < 5 || this.aU[n4].aH == 1) && this.dK[n5][n6][0] == this.aU[n4].h && this.dK[n5][n6][1] == this.aU[n4].i) {
                                    if (this.dK[n5][n6][2] == 1) {
                                        this.aU[n4].j = 3;
                                        this.aU[n4].k = 10;
                                        this.aU[n4].H = 5;
                                        if (this.dq == 2) {
                                            this.aU[n4].w = 1;
                                            this.aU[n4].J = 20;
                                        } else {
                                            this.aU[n4].w = 2;
                                            this.aU[n4].J = 30;
                                        }
                                        this.aU[n4].z = n8;
                                        bl = true;
                                        bl2 = true;
                                    } else if (this.dK[n5][n6][2] > 1) {
                                        bl = true;
                                        this.aU[n4].J -= 6;
                                    }
                                }
                                ++n6;
                            }
                            ++n5;
                        }
                    } else if (this.dr == 3 && this.dq == 6) {
                        switch (this.cm[3]) {
                            case 4: 
                            case 5: {
                                n8 = this.ce[4] * 7;
                                break;
                            }
                            case 6: {
                                n8 = this.ce[4] * 8;
                                break;
                            }
                            default: {
                                n8 = this.ce[4] * 6;
                            }
                        }
                        switch (this.cC) {
                            case 0: {
                                if (this.aU[n4].h != this.cc || this.aU[n4].i != this.cd - 1) break;
                                n7 = 1;
                                break;
                            }
                            case 1: {
                                if (this.aU[n4].h != this.cc || this.aU[n4].i != this.cd + 1) break;
                                n7 = 1;
                                break;
                            }
                            case 2: {
                                if (this.aU[n4].h != this.cc - 1 || this.aU[n4].i != this.cd) break;
                                n7 = 1;
                                break;
                            }
                            case 3: {
                                if (this.aU[n4].h != this.cc + 1 || this.aU[n4].i != this.cd) break;
                                n7 = 1;
                            }
                        }
                        if (n7 == 1 && (this.aU[n4].ab < 5 || this.aU[n4].aH == 1)) {
                            this.aU[n4].j = 3;
                            this.aU[n4].k = 10;
                            this.aU[n4].H = 3;
                            this.aU[n4].b = this.aU[n4].a = this.g(this.cC);
                            this.aU[n4].F = this.cC;
                            this.aU[n4].w = 2;
                            this.aU[n4].z = n8;
                            this.aU[n4].P = 1;
                            bl = true;
                            bl2 = true;
                        }
                    }
                } else if (this.bZ == 1) {
                    if (this.dr == 0 && this.dq > 1 && (this.aU[n4].ab < 5 || this.aU[n4].aH == 1)) {
                        n2 = this.aU[n4].f - this.aI[0];
                        n3 = this.aU[n4].g - this.aI[1];
                        if (n2 > 0 && n3 > 0 && n2 < this.bx[0] && n3 < this.bx[1] - 20) {
                            switch (this.cC) {
                                case 0: {
                                    if (this.aU[n4].h != this.cc || this.cb <= this.aU[n4].g) break;
                                    n7 = 1;
                                    break;
                                }
                                case 1: {
                                    if (this.aU[n4].h != this.cc || this.cb >= this.aU[n4].g) break;
                                    n7 = 1;
                                    break;
                                }
                                case 2: {
                                    if (this.aU[n4].i != this.cd || this.ca <= this.aU[n4].f) break;
                                    n7 = 1;
                                    break;
                                }
                                case 3: {
                                    if (this.aU[n4].i != this.cd || this.ca >= this.aU[n4].f) break;
                                    n7 = 1;
                                }
                            }
                            if (n7 == 1) {
                                this.aU[n4].j = 3;
                                this.aU[n4].k = 10;
                                this.aU[n4].b = this.aU[n4].a = this.g(this.cC);
                                this.aU[n4].F = this.cC;
                                this.aU[n4].H = 3;
                                this.aU[n4].w = 1 + this.ei % 2;
                                switch (this.cm[0]) {
                                    case 1: {
                                        n8 = 30;
                                        break;
                                    }
                                    case 2: {
                                        n8 = 45;
                                        break;
                                    }
                                    case 3: {
                                        n8 = 68;
                                        break;
                                    }
                                    case 4: {
                                        n8 = 101;
                                        break;
                                    }
                                    case 5: {
                                        n8 = 151;
                                        break;
                                    }
                                    case 6: {
                                        n8 = 226;
                                    }
                                }
                                this.aU[n4].z = n8 += this.ce[7];
                                bl = true;
                                if (this.dm == 1) {
                                    bl2 = true;
                                }
                            }
                        }
                    } else if (this.dr == 1) {
                        if (this.dq == 7 && this.aU[n4].ab < 2) {
                            n2 = this.aU[n4].f - this.aI[0];
                            n3 = this.aU[n4].g - this.aI[1];
                            if (n2 > 0 && n3 > 0 && n2 < this.bx[0] && n3 < this.bx[1] - 20) {
                                this.aU[n4].z = 20 + (this.cm[1] - 1) * 20;
                                bl2 = true;
                                this.ce[2] = this.ce[2] + this.cm[1] * 20;
                                if (this.ce[2] > this.cf) {
                                    this.ce[2] = this.cf;
                                }
                                this.aU[n4].T = 0;
                                this.aU[n4].k = 10;
                                this.aU[n4].H = 0;
                            }
                        }
                    } else if (this.dr == 2) {
                        n2 = this.aU[n4].f - this.aI[0];
                        n3 = this.aU[n4].g - this.aI[1];
                        if (this.aU[n4].ab < 2 && n2 > 0 && n3 > 0 && n2 < this.bx[0] && n3 < this.bx[1] - 20) {
                            this.aU[n4].a = 1;
                            if (this.dq > 6) {
                                this.aU[n4].d = 0;
                                this.aU[n4].U = 1;
                                this.aU[n4].w = this.dq % 2 == 0 ? 0 : (this.dq % 4 == 1 ? 6 : 7);
                                this.aU[n4].j = 3;
                                this.aU[n4].k = 10;
                                this.aU[n4].H = 4;
                            }
                            if (this.dq == 17 && this.cm[2] > 4) {
                                this.aU[n4].V = 1;
                                this.aU[n4].W = this.ei;
                                this.aU[n4].X = (this.cm[2] - 4) * 50;
                            }
                        }
                    } else if (this.dr == 3 && this.dq == 3 && (this.aU[n4].ab < 2 || this.aU[n4].aH == 1)) {
                        if (this.aU[n4].h == this.cc && this.aU[n4].i == this.cd - 1) {
                            n7 = 1;
                            this.aU[n4].F = 0;
                        } else if (this.aU[n4].h == this.cc && this.aU[n4].i == this.cd + 1) {
                            n7 = 1;
                            this.aU[n4].F = 1;
                        } else if (this.aU[n4].i == this.cd && this.aU[n4].h == this.cc - 1) {
                            n7 = 1;
                            this.aU[n4].F = 2;
                        } else if (this.aU[n4].i == this.cd && this.aU[n4].h == this.cc + 1) {
                            n7 = 1;
                            this.aU[n4].F = 3;
                        }
                        if (n7 == 1) {
                            this.aU[n4].P = 2;
                            this.aU[n4].j = 3;
                            this.aU[n4].k = 10;
                            this.aU[n4].H = 2;
                            this.aU[n4].w = 1;
                            this.aU[n4].b = this.aU[n4].a = this.g(this.aU[n4].F);
                            bl = true;
                            if (this.cm[3] > 4) {
                                n8 = this.ce[9] + this.ce[12];
                                n8 = this.cm[3] == 5 ? n8 * 15 / 10 : (n8 *= 2);
                                this.aU[n4].z = n8;
                                bl2 = true;
                            }
                        }
                    }
                }
            }
            if (bl) {
                if (this.aU[n4].w == 1) {
                    this.aU[n4].Q[0] = this.aU[n4].Q[0] == 0 ? 2 : 0;
                    this.aU[n4].o[this.aU[n4].Q[0]][1] = 1;
                    this.aU[n4].o[this.aU[n4].Q[0]][0] = 0;
                } else if (this.aU[n4].w == 2) {
                    this.aU[n4].Q[1] = this.aU[n4].Q[1] == 1 ? 3 : 1;
                    this.aU[n4].o[this.aU[n4].Q[1]][1] = 1;
                    this.aU[n4].o[this.aU[n4].Q[1]][0] = 0;
                }
                if (this.bY != 0 || this.dr != 2) {
                    this.br = 1;
                    this.bp = 3;
                }
            }
            if (bl2) {
                this.aU[n4].x[4][0] = 2;
                this.aU[n4].x[4][1] = this.aU[n4].z;
                this.aU[n4].x[4][3] = this.aU[n4].g - this.W[this.aU[n4].ar * 12].getHeight();
                if (this.aU[n4].y == 0) {
                    this.aU[n4].y = 1;
                    this.aU[n4].x[4][2] = this.aU[n4].f - 10;
                } else {
                    this.aU[n4].y = 0;
                    this.aU[n4].x[4][2] = this.aU[n4].f + 10;
                }
                this.aU[n4].ah -= this.aU[n4].z;
                if (this.aU[n4].ah < 0) {
                    this.aU[n4].ah = 0;
                }
                this.aU[n4].az = 0;
                this.aU[n4].z = 0;
            }
            ++n4;
        }
    }

    public final int a(int n2, int n3, int n4, int n5) {
        if (n3 >= 3 || n4 != 3) {
            if (n3 == 3 && n4 < 3) {
                n5 /= 10;
            } else if (n3 != 3 || n4 != 3) {
                if (n3 == n4) {
                    n5 /= 2;
                } else if (!(n3 == 0 && n4 == 1 || n3 == 1 && n4 == 2 || n3 == 2 && n4 == 0)) {
                    n5 /= 10;
                }
            }
        }
        return n5;
    }

    public final void S() {
        int[] nArray = new int[4];
        nArray[1] = 1;
        nArray[3] = 2;
        Object var2_1 = null;
        int n2 = 0;
        while (n2 < this.aV) {
            if (this.aX[n2].l == 1 && this.aX[n2].h == 1) {
                switch (this.aX[n2].c) {
                    case 0: {
                        this.aX[n2].e -= 10;
                        break;
                    }
                    case 1: {
                        this.aX[n2].e += 10;
                        break;
                    }
                    case 2: {
                        this.aX[n2].d -= 10;
                        break;
                    }
                    case 3: {
                        this.aX[n2].d += 10;
                    }
                }
                if (this.aX[n2].g == 1) {
                    this.aX[n2].f = this.aX[n2].f < 3 ? ++this.aX[n2].f : 0;
                }
                this.aX[n2].a = this.aX[n2].d / 20;
                this.aX[n2].b = this.aX[n2].e % 20 == 0 ? this.aX[n2].e / 20 - 1 : this.aX[n2].e / 20;
            }
            ++n2;
        }
    }

    public final void i(int n2) {
        int[] nArray = new int[4];
        this.aU[n2].I = 0;
        nArray[0] = this.aU[n2].f;
        nArray[1] = this.aU[n2].g;
        nArray[2] = this.aU[n2].h;
        nArray[3] = this.aU[n2].i;
        if (this.aU[n2].ab > 1) {
            this.aU[n2].H = 0;
            this.aU[n2].n = 0;
        }
        switch (this.aU[n2].H) {
            case 0: {
                return;
            }
            case 1: {
                return;
            }
            case 2: {
                this.aU[n2].G = this.a(20, nArray, this.aU[n2].F, 10, 99);
                return;
            }
            case 3: {
                this.aU[n2].G = this.a(60, nArray, this.aU[n2].F, 10, 99);
                this.aU[n2].E = this.aU[n2].G / 10;
                this.aU[n2].I = 0;
                this.aU[n2].J = 20;
                return;
            }
            case 4: {
                this.aU[n2].k = 4;
                this.aU[n2].j = 4;
                this.aU[n2].L = this.ei;
                this.aU[n2].N = this.dn;
                this.aU[n2].O = 0;
                this.aU[n2].P = 0;
                this.aU[n2].e = 4;
                this.aU[n2].w = 5;
                this.aU[n2].f = this.aU[n2].h * 20 + 10;
                this.aU[n2].g = (this.aU[n2].i + 1) * 20;
                return;
            }
            case 5: {
                this.aU[n2].I = 0;
                this.aU[n2].J = 20;
            }
        }
    }

    public final void j(int n2) {
        int n3 = 0;
        boolean bl = false;
        switch (this.aU[n2].H) {
            case 2: {
                this.b(n2, this.aU[n2].G, this.aU[n2].F);
                this.aU[n2].w = 1;
                this.aU[n2].u = 1;
                this.aU[n2].k = 1;
                n3 = 2;
                if (this.aU[n2].P == 2) {
                    bl = true;
                    break;
                }
                if (this.aU[n2].ah > 0) break;
                this.aU[n2].ah = 0;
                n3 = 1;
                break;
            }
            case 3: 
            case 5: {
                if (this.aU[n2].I < 6) {
                    if (this.aU[n2].H == 3) {
                        this.b(n2, this.aU[n2].E * s.gK[this.aU[n2].I], this.aU[n2].F);
                    }
                    this.aU[n2].w = 2;
                    this.aU[n2].u = 1;
                    this.aU[n2].J = s.gL[this.aU[n2].I];
                    ++this.aU[n2].I;
                    break;
                }
                if (this.aU[n2].I != 6) break;
                if (this.aU[n2].P == 1 && this.s(0, 10) > 2) {
                    this.aU[n2].P = 2;
                }
                if (this.aU[n2].P == 2) {
                    bl = true;
                    break;
                }
                if (this.aU[n2].ah <= 0) {
                    this.aU[n2].ah = 0;
                    n3 = 1;
                    break;
                }
                n3 = 2;
                this.aU[n2].j = 1;
                this.aU[n2].k = 1;
                this.aU[n2].J = 0;
                this.aU[n2].f = this.aU[n2].h * 20 + 10;
                this.aU[n2].g = (this.aU[n2].i + 1) * 20;
                break;
            }
            case 4: {
                if (this.aU[n2].ah <= 0) {
                    this.aU[n2].ah = 0;
                    n3 = 1;
                    break;
                }
                n3 = 2;
                bl = true;
                break;
            }
            default: {
                if (this.aU[n2].ah <= 0) {
                    this.aU[n2].ah = 0;
                    n3 = 1;
                    break;
                }
                n3 = 2;
                if (this.aU[n2].ab < 2) {
                    this.aU[n2].j = 1;
                    this.aU[n2].k = 1;
                    break;
                }
                this.aU[n2].j = 0;
                this.aU[n2].k = 0;
            }
        }
        if (bl) {
            this.aU[n2].k = 4;
            this.aU[n2].L = this.ei;
            this.aU[n2].N = this.dn;
            this.aU[n2].O = 0;
            this.aU[n2].P = 0;
            this.aU[n2].e = 4;
            this.aU[n2].f = this.aU[n2].h * 20 + 10;
            this.aU[n2].g = (this.aU[n2].i + 1) * 20;
        }
        if (n3 == 1) {
            this.aU[n2].B = 0;
            this.aU[n2].C = 0;
            this.aU[n2].D = 0;
            this.aU[n2].k = 9;
            this.aU[n2].M = this.ei;
            return;
        }
        if (n3 == 2) {
            this.aU[n2].H = 0;
            this.aU[n2].J = 0;
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void T() {
        var8_1 = 0;
        var9_2 = false;
        var10_3 = false;
        var11_4 = new int[4];
        var12_5 = false;
        var13_6 = 0;
        var14_7 = 0;
        var15_8 = new int[7];
        var16_9 = false;
        var17_10 = 0;
        var18_11 = 0;
        var19_12 = 0;
        var20_13 = 0;
        var21_14 = 0;
        var22_15 = 0;
        var1_16 = 0;
        while (var1_16 < this.aT) {
            block149: {
                block151: {
                    block150: {
                        var14_7 = 0;
                        var12_5 = false;
                        var9_2 = false;
                        var10_3 = this.fk != 1 && this.eC != 1 || this.aU[var1_16].j == 11;
                        if (this.aU[var1_16].ax != 1 || !var10_3) break block149;
                        if (this.dp != 0) break block150;
                        if ((this.aU[var1_16].j == 3 || this.aU[var1_16].j == 5) && this.aU[var1_16].r == 0) {
                            this.aU[var1_16].k = 10;
                        }
                        if (this.aU[var1_16].j != this.aU[var1_16].k) {
                            this.aU[var1_16].j = this.aU[var1_16].k;
                            if (this.aU[var1_16].k == 10) {
                                this.i(var1_16);
                            }
                        }
                        this.aU[var1_16].D = 0;
                        this.aU[var1_16].w = 0;
                        this.aU[var1_16].u = 0;
                        if (this.fk != 1 && this.eC != 1 && this.aU[var1_16].j < 9) {
                            if (this.aU[var1_16].j != 2) {
                                this.aU[var1_16].B = 0;
                                this.aU[var1_16].C = 0;
                            }
                            if (this.gj > 0 && this.aU[var1_16].am > 0) {
                                var8_1 = this.ei - this.gl;
                                if (var8_1 < 0) {
                                    var8_1 += 9999;
                                }
                                if (var8_1 > this.aU[var1_16].an) {
                                    this.l(var1_16);
                                    this.gl = this.ei;
                                }
                            }
                        }
                        if (this.aU[var1_16].q == 1) {
                            this.aU[var1_16].j = 2;
                            this.aU[var1_16].B = 1;
                            this.aU[var1_16].C = 0;
                            this.aU[var1_16].q = 0;
                        }
                        if (this.aU[var1_16].V == 1) {
                            var8_1 = this.ei - this.aU[var1_16].W;
                            if (var8_1 < 0) {
                                var8_1 += 9999;
                            }
                            if (var8_1 > this.aU[var1_16].X) {
                                this.aU[var1_16].V = 0;
                            }
                        }
                        block0 : switch (this.aU[var1_16].j) {
                            case 0: {
                                if (this.aU[var1_16].ab < 2) {
                                    var4_19 = this.ca - this.aU[var1_16].f;
                                    var5_20 = this.cb - this.aU[var1_16].g;
                                    var6_21 = var4_19 > 0 ? var4_19 : -var4_19;
                                    v0 = var7_22 = var5_20 > 0 ? var5_20 : -var5_20;
                                    if (var6_21 < 100 && var7_22 < 100) {
                                        this.aU[var1_16].b = var6_21 > var7_22 ? (var4_19 > 0 ? 3 : 2) : (var5_20 > 0 ? 1 : 0);
                                        this.aU[var1_16].a = this.aU[var1_16].b;
                                        this.aU[var1_16].j = 1;
                                        this.aU[var1_16].k = 1;
                                        this.aU[var1_16].K = this.ei;
                                    }
                                    this.aU[var1_16].e = this.aU[var1_16].b;
                                    break;
                                }
                                if (this.aU[var1_16].ab == 2) {
                                    var8_1 = this.ei - this.aU[var1_16].K;
                                    if (var8_1 < 0) {
                                        var8_1 += 9999;
                                    }
                                    if (this.aU[var1_16].aA == 0) {
                                        var14_7 = this.aU[var1_16].ac;
                                    } else if (this.aU[var1_16].aA == 1) {
                                        var14_7 = this.aU[var1_16].ad;
                                    }
                                    if (var8_1 > var14_7) {
                                        var15_8[0] = var1_16;
                                        var15_8[1] = this.aU[var1_16].h;
                                        var15_8[2] = this.aU[var1_16].i;
                                        var15_8[3] = 1;
                                        var15_8[4] = 0;
                                        var15_8[5] = this.aU[var1_16].ar;
                                        var15_8[6] = this.aU[var1_16].aw;
                                        this.a(var15_8);
                                        this.aU[var1_16].S = 0;
                                        this.aU[var1_16].K = this.ei;
                                        switch (this.aU[var1_16].a) {
                                            case 0: {
                                                this.aU[var1_16].a = 2;
                                                break;
                                            }
                                            case 1: {
                                                this.aU[var1_16].a = 3;
                                                break;
                                            }
                                            case 2: {
                                                this.aU[var1_16].a = 1;
                                                break;
                                            }
                                            case 3: {
                                                this.aU[var1_16].a = 0;
                                            }
                                        }
                                    }
                                    if (var8_1 == var14_7 - 2) {
                                        this.aU[var1_16].S = 1;
                                        break;
                                    }
                                } else if (this.aU[var1_16].ab >= 5) {
                                    this.k(var1_16);
                                    break;
                                }
                                break block151;
                            }
                            case 1: {
                                var4_19 = this.ca - this.aU[var1_16].f;
                                var5_20 = this.cb - this.aU[var1_16].g;
                                var6_21 = var4_19 > 0 ? var4_19 : -var4_19;
                                v1 = var7_22 = var5_20 > 0 ? var5_20 : -var5_20;
                                if (this.aU[var1_16].f % 20 == 10 && this.aU[var1_16].g % 20 == 0) {
                                    this.aU[var1_16].b = var6_21 <= this.aU[var1_16].aq && var7_22 <= this.aU[var1_16].aq ? (this.aU[var1_16].h == this.cc || this.aU[var1_16].i == this.cd ? (var6_21 >= var7_22 ? (var4_19 > 0 ? 3 : 2) : (var5_20 > 0 ? 1 : 0)) : (var6_21 < var7_22 ? (var4_19 > 0 ? 3 : 2) : (var5_20 > 0 ? 1 : 0))) : (var6_21 >= var7_22 ? (var4_19 > 0 ? 3 : 2) : (var5_20 > 0 ? 1 : 0));
                                    var8_1 = this.a(this.aU[var1_16].h, this.aU[var1_16].i, this.aU[var1_16].f, this.aU[var1_16].g, this.aU[var1_16].b, 20, 1, var1_16);
                                    if (var8_1 == 0) {
                                        if (this.aU[var1_16].b == 0 || this.aU[var1_16].b == 1) {
                                            var20_13 = 2;
                                            var21_14 = 3;
                                        } else if (this.aU[var1_16].b == 2 || this.aU[var1_16].b == 3) {
                                            var20_13 = 0;
                                            var21_14 = 1;
                                        }
                                        var18_11 = this.a(this.aU[var1_16].h, this.aU[var1_16].i, this.aU[var1_16].f, this.aU[var1_16].g, var20_13, 20, 1, var1_16);
                                        var19_12 = this.a(this.aU[var1_16].h, this.aU[var1_16].i, this.aU[var1_16].f, this.aU[var1_16].g, var21_14, 20, 1, var1_16);
                                        if (var18_11 == 1 && var19_12 == 1) {
                                            var23_23 = null;
                                            var24_24 = 0L;
                                            var26_25 = null;
                                            var24_24 = o._n();
                                            this.aU[var1_16].a = var24_24 % 2L == 0L ? var20_13 : var21_14;
                                        } else {
                                            this.aU[var1_16].a = var18_11 == 1 ? var20_13 : (var19_12 == 1 ? var21_14 : this.g(this.aU[var1_16].b));
                                        }
                                        this.aU[var1_16].e = this.a(this.aU[var1_16].h, this.aU[var1_16].i, this.aU[var1_16].f, this.aU[var1_16].g, this.aU[var1_16].a, 20, 1, var1_16) == 1 ? this.aU[var1_16].a : 4;
                                    } else if (var8_1 == 1) {
                                        this.aU[var1_16].a = this.aU[var1_16].b;
                                        this.aU[var1_16].e = this.aU[var1_16].b;
                                    } else if (var8_1 == 2) {
                                        this.aU[var1_16].a = this.aU[var1_16].b;
                                        this.aU[var1_16].e = this.aU[var1_16].b;
                                        var9_2 = true;
                                    }
                                } else {
                                    if (this.aU[var1_16].a == 0 || this.aU[var1_16].a == 1) {
                                        if (this.aU[var1_16].f % 20 != 10) {
                                            this.aU[var1_16].f = this.aU[var1_16].h * 20 + 10;
                                        }
                                    } else if ((this.aU[var1_16].a == 2 || this.aU[var1_16].a == 3) && this.aU[var1_16].g % 20 != 0) {
                                        this.aU[var1_16].g = (this.aU[var1_16].i + 1) * 20;
                                    }
                                    if ((var8_1 = this.a(this.aU[var1_16].h, this.aU[var1_16].i, this.aU[var1_16].f, this.aU[var1_16].g, this.aU[var1_16].a, this.aU[var1_16].c, 1, var1_16)) == 0) {
                                        this.aU[var1_16].a = this.g(this.aU[var1_16].a);
                                    }
                                    this.aU[var1_16].e = this.aU[var1_16].a;
                                }
                                if (this.aU[var1_16].a < 2 && this.aU[var1_16].h == this.cc || this.aU[var1_16].a > 1 && this.aU[var1_16].i == this.cd) {
                                    var11_4[0] = this.aU[var1_16].f;
                                    var11_4[1] = this.aU[var1_16].g;
                                    var11_4[2] = this.aU[var1_16].h;
                                    var11_4[3] = this.aU[var1_16].i;
                                    var13_6 = this.a(this.aU[var1_16].aq, var11_4, this.aU[var1_16].a, 91, 99);
                                    if (var13_6 == 2) {
                                        var9_2 = true;
                                    }
                                }
                                if (this.aU[var1_16].Z == 2 && this.aU[var1_16].aG == 0) {
                                    var9_2 = true;
                                }
                                if (var9_2) {
                                    this.aU[var1_16].j = 2;
                                    this.aU[var1_16].k = 2;
                                    this.aU[var1_16].A = 1;
                                    this.aU[var1_16].B = 0;
                                    this.aU[var1_16].C = 0;
                                    this.aU[var1_16].S = 0;
                                    this.aU[var1_16].K = this.ei;
                                    break;
                                }
                                if (this.aU[var1_16].e == 0) {
                                    this.aU[var1_16].g -= this.aU[var1_16].c;
                                    break;
                                }
                                if (this.aU[var1_16].e == 1) {
                                    this.aU[var1_16].g += this.aU[var1_16].c;
                                    break;
                                }
                                if (this.aU[var1_16].e == 2) {
                                    this.aU[var1_16].f -= this.aU[var1_16].c;
                                    break;
                                }
                                if (this.aU[var1_16].e == 3) {
                                    this.aU[var1_16].f += this.aU[var1_16].c;
                                    break;
                                }
                                break block151;
                            }
                            case 2: {
                                if (this.aU[var1_16].B != 0) ** GOTO lbl246
                                var8_1 = this.ei - this.aU[var1_16].K;
                                if (var8_1 < 0) {
                                    var8_1 += 9999;
                                }
                                if (this.aU[var1_16].A == 1) {
                                    if (var8_1 > this.aU[var1_16].ad) {
                                        this.aU[var1_16].B = 1;
                                        this.aU[var1_16].C = 0;
                                        this.aU[var1_16].A = 0;
                                        this.aU[var1_16].K = this.ei;
                                        break;
                                    }
                                } else {
                                    if (this.aU[var1_16].a == 0 || this.aU[var1_16].a == 1) {
                                        var12_5 = this.t(this.cb, this.aU[var1_16].g) <= this.aU[var1_16].aq && this.cc == this.aU[var1_16].h;
                                    } else if (this.aU[var1_16].a == 2 || this.aU[var1_16].a == 3) {
                                        var12_5 = this.t(this.ca, this.aU[var1_16].f) <= this.aU[var1_16].aq && this.cd == this.aU[var1_16].i;
                                    }
                                    if (var12_5) {
                                        var11_4[0] = this.aU[var1_16].f;
                                        var11_4[1] = this.aU[var1_16].g;
                                        var11_4[2] = this.aU[var1_16].h;
                                        var11_4[3] = this.aU[var1_16].i;
                                        var13_6 = this.a(this.aU[var1_16].aq, var11_4, this.aU[var1_16].a, 91, 99);
                                        if (var13_6 == 2) {
                                            var8_1 = this.ei - this.aU[var1_16].K;
                                            if (var8_1 < 0) {
                                                var8_1 += 9999;
                                            }
                                            if (var8_1 > this.aU[var1_16].ad) {
                                                this.aU[var1_16].B = 1;
                                                this.aU[var1_16].C = 0;
                                                this.aU[var1_16].A = 0;
                                                this.aU[var1_16].K = this.ei;
                                                break;
                                            }
                                        } else if (var13_6 == 1) {
                                            switch (this.aU[var1_16].a) {
                                                case 0: {
                                                    this.aU[var1_16].g -= this.aU[var1_16].c;
                                                    break;
                                                }
                                                case 1: {
                                                    this.aU[var1_16].g += this.aU[var1_16].c;
                                                    break;
                                                }
                                                case 2: {
                                                    this.aU[var1_16].f -= this.aU[var1_16].c;
                                                    break;
                                                }
                                                case 3: {
                                                    this.aU[var1_16].f += this.aU[var1_16].c;
                                                }
                                            }
                                            this.aU[var1_16].e = this.aU[var1_16].a;
                                            break;
                                        }
                                    } else {
                                        this.aU[var1_16].S = 0;
                                        this.aU[var1_16].B = 0;
                                        this.aU[var1_16].j = 1;
                                        this.aU[var1_16].k = 1;
                                        this.aU[var1_16].e = this.aU[var1_16].a;
                                        break;
lbl246:
                                        // 1 sources

                                        if (this.aU[var1_16].C < 1) {
                                            ++this.aU[var1_16].C;
                                            if (this.aU[var1_16].Z == 0 || this.aU[var1_16].Z == 2 && this.aU[var1_16].aG == 1) {
                                                var11_4[0] = this.aU[var1_16].f;
                                                var11_4[1] = this.aU[var1_16].g;
                                                var11_4[2] = this.aU[var1_16].h;
                                                var11_4[3] = this.aU[var1_16].i;
                                                var13_6 = this.a(this.aU[var1_16].aq, var11_4, this.aU[var1_16].a, 91, 99);
                                                if (var13_6 == 2 && this.dE == 0 && this.cF < 3) {
                                                    this.aU[var1_16].D = 1;
                                                    this.aU[var1_16].aF = this.aU[var1_16].ae;
                                                    this.cW = this.aU[var1_16].a;
                                                    break;
                                                }
                                            } else if (this.aU[var1_16].Z == 1 || this.aU[var1_16].Z == 2) {
                                                var15_8[0] = var1_16;
                                                var15_8[1] = this.aU[var1_16].h;
                                                var15_8[2] = this.aU[var1_16].i;
                                                var15_8[3] = 2 - this.aU[var1_16].Z;
                                                var15_8[4] = this.aU[var1_16].aj > 0 ? 1 : 0;
                                                var15_8[5] = this.aU[var1_16].ar;
                                                var15_8[6] = this.aU[var1_16].aw;
                                                this.a(var15_8);
                                                break;
                                            }
                                        } else {
                                            this.aU[var1_16].B = 0;
                                            this.aU[var1_16].C = 0;
                                            this.aU[var1_16].D = 0;
                                            this.aU[var1_16].K = this.ei;
                                            if (this.aU[var1_16].Z == 2) {
                                                this.aU[var1_16].k = 1;
                                                this.aU[var1_16].j = 1;
                                                break;
                                            }
                                        }
                                    }
                                }
                                break block151;
                            }
                            case 3: {
                                this.aU[var1_16].B = 0;
                                this.aU[var1_16].C = 0;
                                this.aU[var1_16].k = 10;
                                this.aU[var1_16].z = this.aU[var1_16].p;
                                break;
                            }
                            case 4: {
                                this.aU[var1_16].U = 0;
                                this.aU[var1_16].w = 5;
                                var8_1 = this.ei - this.aU[var1_16].L;
                                if (var8_1 < 0) {
                                    var8_1 += 9999;
                                }
                                if (var8_1 > this.aU[var1_16].N) {
                                    if (this.aU[var1_16].ah <= 0) {
                                        this.aU[var1_16].ah = 0;
                                        this.aU[var1_16].B = 0;
                                        this.aU[var1_16].C = 0;
                                        this.aU[var1_16].D = 0;
                                        this.aU[var1_16].k = 9;
                                        this.aU[var1_16].M = this.ei;
                                        break;
                                    }
                                    this.aU[var1_16].j = 1;
                                    this.aU[var1_16].k = 1;
                                    this.aU[var1_16].B = 0;
                                    this.aU[var1_16].C = 0;
                                    break;
                                }
                                if (this.aU[var1_16].O < 2) {
                                    ++this.aU[var1_16].O;
                                    break;
                                }
                                this.aU[var1_16].O = 0;
                                break;
                            }
                            case 5: {
                                break;
                            }
                            case 6: 
                            case 7: 
                            case 8: {
                                break;
                            }
                            case 9: {
                                this.aU[var1_16].e = 4;
                                var8_1 = this.ei - this.aU[var1_16].M;
                                if (var8_1 < 0) {
                                    var8_1 += 9999;
                                }
                                if (var8_1 > 4) {
                                    this.aU[var1_16].j = 0;
                                    this.aU[var1_16].k = 0;
                                    this.aU[var1_16].ay = 0;
                                    this.aU[var1_16].R = 0;
                                    if (this.aU[var1_16].am == 0 && this.aU[var1_16].an > 0 && this.aU[var1_16].ax == 1) {
                                        if (this.aU[var1_16].aE * this.aU[var1_16].an < 10000) {
                                            ++this.aU[var1_16].aE;
                                        }
                                        this.aU[var1_16].j = 12;
                                        this.aU[var1_16].k = 12;
                                        this.aU[var1_16].M = this.ei;
                                    } else {
                                        this.aU[var1_16].ax = 0;
                                    }
                                    if (this.fm == 0) {
                                        this.ce[1] = this.ce[0] == 70 && this.eD < 78 && this.ce[1] + this.aU[var1_16].ao >= this.ch ? this.ch - 1 : this.ce[1] + this.aU[var1_16].ao;
                                        this.ce[11] = this.ce[11] + this.aU[var1_16].ap;
                                        var4_19 = this.aU[var1_16].az;
                                        if (var4_19 > 0 && var4_19 < 4 && this.cp[var4_19 - 1][0] < 6) {
                                            v2 = this.cp[var4_19 - 1];
                                            v2[1] = v2[1] + this.aU[var1_16].ao;
                                            if (this.cp[var4_19 - 1][1] > this.cp[var4_19 - 1][2]) {
                                                v3 = this.cp[var4_19 - 1];
                                                v3[0] = v3[0] + 1;
                                                v4 = this.cp[var4_19 - 1];
                                                v4[1] = v4[1] - this.cp[var4_19 - 1][2];
                                                switch (this.cp[var4_19 - 1][0]) {
                                                    case 2: {
                                                        this.cp[var4_19 - 1][2] = 2800;
                                                        break;
                                                    }
                                                    case 3: {
                                                        this.cp[var4_19 - 1][2] = 8680;
                                                        break;
                                                    }
                                                    case 4: {
                                                        this.cp[var4_19 - 1][2] = 16450;
                                                        break;
                                                    }
                                                    case 5: {
                                                        this.cp[var4_19 - 1][2] = 28700;
                                                        break;
                                                    }
                                                    case 6: {
                                                        this.cp[var4_19 - 1][1] = 28700;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    var2_17 = 0;
                                    while (var2_17 < 4) {
                                        if (this.eJ[var2_17][1] > 0 && this.eJ[var2_17][2] == 2) {
                                            if (this.eJ[var2_17][3] > 0) {
                                                var22_15 = this.bV == 112 ? 29 : this.bV;
                                                if (this.eJ[var2_17][3] == var22_15 && this.eJ[var2_17][4] == this.aU[var1_16].Y) {
                                                    v5 = this.eJ[var2_17];
                                                    v5[6] = v5[6] + 1;
                                                    this.gq = var2_17 + 1;
                                                    this.gr = this.ei;
                                                }
                                            } else if (this.eJ[var2_17][4] == this.aU[var1_16].Y) {
                                                v6 = this.eJ[var2_17];
                                                v6[6] = v6[6] + 1;
                                                this.gq = var2_17 + 1;
                                                this.gr = this.ei;
                                            }
                                            if (this.eJ[var2_17][5] <= this.eJ[var2_17][6]) {
                                                this.eJ[var2_17][22] = 1;
                                            }
                                        }
                                        ++var2_17;
                                    }
                                    if (this.ei % 30 < 15 && !var16_9) {
                                        var17_10 = var1_16;
                                        var16_9 = true;
                                        break;
                                    }
                                }
                                break block151;
                            }
                            case 10: {
                                this.j(var1_16);
                                break;
                            }
                            case 11: {
                                switch (this.aU[var1_16].e) {
                                    case 0: {
                                        this.aU[var1_16].g -= this.aU[var1_16].c;
                                        break block0;
                                    }
                                    case 1: {
                                        this.aU[var1_16].g += this.aU[var1_16].c;
                                        break block0;
                                    }
                                    case 2: {
                                        this.aU[var1_16].f -= this.aU[var1_16].c;
                                        break block0;
                                    }
                                    case 3: {
                                        this.aU[var1_16].f += this.aU[var1_16].c;
                                    }
                                }
                                break;
                            }
                            case 12: {
                                var8_1 = this.ei - this.aU[var1_16].M;
                                if (var8_1 < 0) {
                                    var8_1 += 9999;
                                }
                                if (var8_1 <= this.aU[var1_16].aE * this.aU[var1_16].an) break block151;
                                this.aU[var1_16].h = this.aU[var1_16].aJ[0];
                                this.aU[var1_16].i = this.aU[var1_16].aJ[1];
                                this.aU[var1_16].a = this.aU[var1_16].aJ[2];
                                this.aU[var1_16].ah = this.aU[var1_16].aJ[3];
                                this.aU[var1_16].d = 0;
                                this.aU[var1_16].e = 4;
                                this.aU[var1_16].j = 0;
                                this.aU[var1_16].k = 0;
                                this.aU[var1_16].ay = 1;
                                this.aU[var1_16].ax = 1;
                                this.aU[var1_16].B = 0;
                                this.aU[var1_16].u = 0;
                                this.aU[var1_16].w = 0;
                                this.aU[var1_16].q = 0;
                                this.aU[var1_16].r = 0;
                                this.aU[var1_16].R = 0;
                                this.aU[var1_16].T = 0;
                                this.aU[var1_16].n = 0;
                                this.aU[var1_16].s = 0;
                                this.aU[var1_16].P = 0;
                                this.aU[var1_16].V = 0;
                                this.aU[var1_16].aA = 0;
                                this.aU[var1_16].aB = 0;
                                this.aU[var1_16].aC = 0;
                                this.aU[var1_16].aD = 0;
                                this.aU[var1_16].aG = 0;
                                this.aU[var1_16].K = this.ei;
                                var2_17 = 0;
                                while (var2_17 < 5) {
                                    var3_18 = 0;
                                    while (var3_18 < 4) {
                                        this.aU[var1_16].x[var2_17][var3_18] = 0;
                                        ++var3_18;
                                    }
                                    ++var2_17;
                                }
                                var2_17 = 0;
                                while (var2_17 < 4) {
                                    var3_18 = 0;
                                    while (var3_18 < 2) {
                                        this.aU[var1_16].o[var2_17][var3_18] = 0;
                                        ++var3_18;
                                    }
                                    ++var2_17;
                                }
                                this.aU[var1_16].Q[0] = 0;
                                this.aU[var1_16].Q[1] = 1;
                                this.aU[var1_16].f = this.aU[var1_16].h * 20 + 10;
                                this.aU[var1_16].g = (this.aU[var1_16].i + 1) * 20;
                            }
                            default: {
                                break;
                            }
                            {
                            }
                        }
                        break block151;
                    }
                    if (this.dp == 1 && this.dq == 0) {
                        if (this.aU[var1_16].j != 0) {
                            this.aU[var1_16].j = 0;
                            this.aU[var1_16].k = 0;
                        }
                        this.aU[var1_16].B = 0;
                        this.aU[var1_16].C = 0;
                        this.aU[var1_16].d = 0;
                        this.aU[var1_16].r = 0;
                    }
                }
                if (this.aU[var1_16].ay == 1) {
                    var2_17 = 0;
                    while (var2_17 < 4) {
                        if (this.aU[var1_16].o[var2_17][1] == 1) {
                            if (this.aU[var1_16].o[var2_17][0] < 1) {
                                v7 = this.aU[var1_16].o[var2_17];
                                v7[0] = v7[0] + 1;
                            } else {
                                this.aU[var1_16].o[var2_17][1] = 0;
                            }
                        }
                        ++var2_17;
                    }
                    var2_17 = 0;
                    while (var2_17 < 4) {
                        this.aU[var1_16].x[var2_17][0] = this.aU[var1_16].x[var2_17 + 1][0];
                        this.aU[var1_16].x[var2_17][1] = this.aU[var1_16].x[var2_17 + 1][1];
                        this.aU[var1_16].x[var2_17][2] = this.aU[var1_16].x[var2_17 + 1][2];
                        this.aU[var1_16].x[var2_17][3] = this.aU[var1_16].x[var2_17 + 1][3];
                        if (var2_17 == 3) {
                            this.aU[var1_16].x[var2_17 + 1][0] = 0;
                            this.aU[var1_16].x[var2_17 + 1][1] = 0;
                            this.aU[var1_16].x[var2_17 + 1][2] = 0;
                            this.aU[var1_16].x[var2_17 + 1][3] = 0;
                        }
                        ++var2_17;
                    }
                    this.m(var1_16);
                    if (this.aU[var1_16].e < 4) {
                        this.aU[var1_16].d = this.aU[var1_16].d < 3 ? ++this.aU[var1_16].d : 0;
                    }
                }
            }
            ++var1_16;
        }
        if (var16_9) {
            this.f(var17_10);
        }
        if (this.fk != 1 && this.eC != 1) {
            this.V();
        }
    }

    public final void a(int[] nArray) {
        int n2 = 0;
        boolean bl = false;
        while (n2 < 20 && !bl) {
            if (this.bl[n2].m == 0) {
                bl = true;
                continue;
            }
            ++n2;
        }
        if (n2 < 20) {
            this.bl[n2].a = nArray[1];
            this.bl[n2].b = nArray[2];
            this.bl[n2].c = this.aU[nArray[0]].a;
            this.bl[n2].f = nArray[0];
            this.bl[n2].h = nArray[3];
            this.bl[n2].g = nArray[6];
            this.bl[n2].k = nArray[5];
            if (nArray[4] == 1) {
                this.bl[n2].i = this.ei;
                this.bl[n2].j = this.aU[nArray[0]].aj;
                this.aU[this.bl[n2].f].aG = 1;
                this.bl[n2].l = 1;
            } else {
                this.bl[n2].l = 0;
            }
            this.bl[n2].d = this.bl[n2].a * 20 + 10;
            this.bl[n2].e = (this.bl[n2].b + 1) * 20;
            this.bl[n2].m = 1;
        }
    }

    public final void U() {
        int n2 = 0;
        int n3 = 0;
        boolean bl = false;
        int[] nArray = new int[4];
        int n4 = 0;
        while (n4 < 20) {
            bl = false;
            if (this.bl[n4].m == 1) {
                if (this.bl[n4].h == 1) {
                    nArray[0] = this.bl[n4].d;
                    nArray[1] = this.bl[n4].e;
                    nArray[2] = this.bl[n4].a--;
                    nArray[3] = this.bl[n4].b--;
                    n2 = this.a(20, nArray, this.bl[n4].c, 91, 99);
                    if (n2 == 0 || n2 > 4) {
                        this.bl[n4].m = 0;
                    } else if (n2 == 2) {
                        if (this.dE == 0 && this.cF < 3) {
                            this.aU[this.bl[n4].f].D = 1;
                            this.aU[this.bl[n4].f].aF = this.aU[this.bl[n4].f].ae;
                            this.cW = this.bl[n4].c;
                            if (this.bl[n4].l == 1) {
                                this.aU[this.bl[n4].f].aI = n4;
                            }
                        }
                        this.bl[n4].m = 0;
                    } else {
                        switch (this.bl[n4].c) {
                            case 0: {
                                this.bl[n4].e -= 20;
                                break;
                            }
                            case 1: {
                                this.bl[n4].e += 20;
                                ++this.bl[n4].b;
                                break;
                            }
                            case 2: {
                                this.bl[n4].d -= 20;
                                break;
                            }
                            case 3: {
                                this.bl[n4].d += 20;
                                ++this.bl[n4].a;
                            }
                        }
                    }
                } else if (this.bl[n4].h == 0) {
                    n3 = this.ei - this.bl[n4].i;
                    if (n3 < 0) {
                        n3 += 9999;
                    }
                    if (n3 >= this.bl[n4].j) {
                        this.bl[n4].m = 0;
                        this.aU[this.bl[n4].f].aG = 0;
                    } else if (this.t(this.bl[n4].d, this.ca) <= 30 && this.t(this.bl[n4].e, this.cb) <= 30 && this.dE == 0 && this.cF < 3) {
                        if (n3 == 0) {
                            this.aU[this.bl[n4].f].D = 1;
                            this.aU[this.bl[n4].f].aF = this.aU[this.bl[n4].f].ae;
                            this.cW = this.bl[n4].c;
                        }
                        if (this.bl[n4].l == 1) {
                            bl = true;
                        }
                    }
                }
                if (bl) {
                    if (this.dV == 0) {
                        this.dY = this.ei;
                        this.dW = this.aU[this.bl[n4].f].al;
                        this.dX = this.aU[this.bl[n4].f].ak;
                        this.dV = 1;
                    }
                    this.aU[this.bl[n4].f].aG = 1;
                    this.dZ = this.ei;
                    this.ea = this.aU[this.bl[n4].f].ai;
                    this.cE = 5;
                }
            }
            ++n4;
        }
    }

    public final void k(int n2) {
        int n3 = 0;
        int n4 = 0;
        int[] nArray = new int[7];
        int[][] nArrayArray = new int[6][];
        nArrayArray[0] = new int[]{-1, 2};
        int[] nArray2 = new int[2];
        nArray2[1] = 2;
        nArrayArray[1] = nArray2;
        nArrayArray[2] = new int[]{1, 2};
        nArrayArray[3] = new int[]{-1, 3};
        int[] nArray3 = new int[2];
        nArray3[1] = 3;
        nArrayArray[4] = nArray3;
        nArrayArray[5] = new int[]{1, 3};
        int[][] nArrayArray2 = nArrayArray;
        switch (this.aU[n2].aA) {
            case 0: {
                if (this.aU[n2].aB == 0) {
                    if (this.aU[n2].f > 10) {
                        this.aU[n2].f -= 10;
                    } else {
                        this.aU[n2].aB = 1;
                        ++this.aU[n2].aC;
                    }
                } else if (this.aU[n2].aB == 1) {
                    if (this.aU[n2].f < this.bC[0] * 20 - 10) {
                        this.aU[n2].f += 10;
                    } else {
                        this.aU[n2].aB = 0;
                        ++this.aU[n2].aC;
                    }
                }
                this.m(n2);
                if (this.aU[n2].aC == 4 && this.aU[n2].f == this.bC[0] * 10 + 10) {
                    this.aU[n2].aA = 1;
                    this.aU[n2].aB = 0;
                    this.aU[n2].aC = 0;
                    return;
                }
                if (this.t(this.cc, this.aU[n2].h) <= 1 && this.aU[n2].f % 20 == 10) {
                    nArray[0] = n2;
                    nArray[1] = this.aU[n2].h;
                    nArray[2] = this.aU[n2].i;
                    nArray[3] = 1;
                    nArray[4] = 0;
                    nArray[5] = this.aU[n2].ar;
                    nArray[6] = this.aU[n2].aw;
                    this.a(nArray);
                    this.aU[n2].S = 1;
                    return;
                }
                this.aU[n2].S = 0;
                return;
            }
            case 1: {
                if (this.aU[n2].aB < 2) {
                    ++this.aU[n2].aB;
                    if (this.aU[n2].aD == 0) {
                        this.aU[n2].g -= 10;
                        return;
                    }
                    if (this.aU[n2].aD != 1) break;
                    this.aU[n2].g += 10;
                    return;
                }
                this.aU[n2].K = this.ei;
                this.aU[n2].aA = 2;
                this.aU[n2].aB = 0;
                if (this.aU[n2].aD == 0) {
                    n4 = 0;
                    while (n4 < 6) {
                        nArray[0] = n2;
                        nArray[1] = this.aU[n2].h + nArrayArray2[n4][0];
                        nArray[2] = this.aU[n2].i + nArrayArray2[n4][1];
                        nArray[3] = 0;
                        nArray[4] = 1;
                        nArray[5] = this.aU[n2].ar + 1;
                        nArray[6] = 1;
                        this.a(nArray);
                        ++n4;
                    }
                    return;
                }
                this.aU[n2].aH = 1;
                return;
            }
            case 2: {
                n3 = this.ei - this.aU[n2].K;
                if (n3 < 0) {
                    n3 += 9999;
                }
                if (this.aU[n2].aD == 0) {
                    this.aU[n2].aC = this.aU[n2].ad;
                } else if (this.aU[n2].aD == 1) {
                    this.aU[n2].aC = this.aU[n2].ac;
                }
                if (n3 <= this.aU[n2].aC) break;
                this.aU[n2].aH = 0;
                if (this.aU[n2].aB < 2) {
                    ++this.aU[n2].aB;
                    if (this.aU[n2].aD == 0) {
                        this.aU[n2].g += 10;
                        return;
                    }
                    if (this.aU[n2].aD != 1) break;
                    this.aU[n2].g -= 10;
                    return;
                }
                this.aU[n2].aA = 0;
                this.aU[n2].aB = 0;
                this.aU[n2].aC = 0;
                this.aU[n2].aD = this.aU[n2].aD == 0 ? 1 : 0;
                this.aU[n2].f = this.aU[n2].h * 20 + 10;
            }
        }
    }

    public final void l(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        int n7 = 0;
        int n8 = 0;
        int[][] nArrayArray = new int[4][];
        int[] nArray = new int[2];
        nArray[1] = -1;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[2];
        nArray2[1] = 1;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[0] = -1;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[0] = 1;
        nArrayArray[3] = nArray4;
        int[][] nArrayArray2 = nArrayArray;
        n8 = this.aU[n2].am % 10;
        switch (n8) {
            case 0: {
                n7 = 1;
                break;
            }
            case 1: {
                n7 = 5;
                break;
            }
            case 2: {
                n7 = 4;
            }
        }
        n5 = 0;
        while (n5 < n7) {
            n6 = 0;
            bl = false;
            while (n6 < 10 && !bl) {
                if (this.bn[n6].e == 0) {
                    bl = true;
                    continue;
                }
                ++n6;
            }
            if (bl) {
                switch (n8) {
                    case 0: {
                        n3 = this.cc;
                        n4 = this.cd;
                        break;
                    }
                    case 1: {
                        if (n5 == 0) {
                            n3 = this.cc;
                            n4 = this.cd;
                            break;
                        }
                        n3 = this.cc + nArrayArray2[n5 - 1][0];
                        n4 = this.cd + nArrayArray2[n5 - 1][1];
                        break;
                    }
                    case 2: {
                        n3 = this.aU[n2].h + nArrayArray2[n5][0];
                        n4 = this.aU[n2].i + nArrayArray2[n5][1];
                    }
                }
                if (n3 >= 0 && n3 < this.bC[0] && n4 >= 0 && n4 < this.bC[1]) {
                    this.bn[n6].a = n3;
                    this.bn[n6].b = n4;
                    this.bn[n6].c = 0;
                    this.bn[n6].f = n2;
                    this.bn[n6].d = 0;
                    this.bn[n6].e = 1;
                }
            }
            ++n5;
        }
    }

    public final void V() {
        int n2 = 0;
        while (n2 < 10) {
            if (this.bn[n2].e == 1) {
                if (this.bn[n2].d == 0) {
                    if (this.bn[n2].c < 15) {
                        ++this.bn[n2].c;
                    } else {
                        this.bn[n2].c = 0;
                        this.bn[n2].d = 1;
                    }
                } else if (this.bn[n2].d == 1) {
                    if (this.bn[n2].c < 2) {
                        ++this.bn[n2].c;
                        if (this.bn[n2].a == this.cc && this.bn[n2].b == this.cd && this.dE == 0) {
                            this.aU[this.bn[n2].f].D = 2;
                            this.aU[this.bn[n2].f].aF += this.aU[this.bn[n2].f].ae * 2;
                        }
                    } else {
                        this.bn[n2].e = 0;
                    }
                }
            }
            ++n2;
        }
    }

    public final void m(int n2) {
        this.aU[n2].h = this.aU[n2].f / 20;
        if (this.aU[n2].g % 20 == 0) {
            this.aU[n2].i = this.aU[n2].g / 20 - 1;
            return;
        }
        this.aU[n2].i = this.aU[n2].g / 20;
    }

    public final void b(int n2, int n3, int n4) {
        switch (n4) {
            case 0: {
                this.aU[n2].g -= n3;
                return;
            }
            case 1: {
                this.aU[n2].g += n3;
                return;
            }
            case 2: {
                this.aU[n2].f -= n3;
                return;
            }
            case 3: {
                this.aU[n2].f += n3;
            }
        }
    }

    public final void W() {
        boolean bl = false;
        switch (this.cF) {
            case 0: {
                return;
            }
            case 1: {
                if (this.cH < this.cG) {
                    ++this.cH;
                    if (this.cH <= 0) break;
                    if (this.dh < 2) {
                        ++this.dh;
                        return;
                    }
                    this.dh = 0;
                    return;
                }
                if (this.cJ != 0) break;
                this.cI = 0;
                this.cF = 0;
                this.bD = 1;
            }
        }
    }

    public final void X() {
        boolean bl = false;
        boolean bl2 = false;
        int[][] nArrayArray = new int[][]{{3, 4, 5, 6}, {8, 10, 12, 14}, {3, 4, 5, 6}};
        this.dk = 0;
        this.dl = 0;
        this.dm = 0;
        this.dc = 0;
        this.dd = 0;
        this.cU = 0;
        int n2 = 0;
        while (n2 < this.aT) {
            this.aU[n2].r = 0;
            ++n2;
        }
        if (this.cQ == 0) {
            switch (this.cF) {
                case 0: {
                    if (this.eC != 0 || this.fk != 0 || this.dA != 0) break;
                    this.bD = 1;
                    this.cJ = 0;
                    this.cT = 0;
                    this.cU = 0;
                    this.cI = 0;
                    this.cN = 0;
                    break;
                }
                case 1: {
                    if (this.cJ == 0) {
                        if (this.cH == 0) {
                            this.cI = 1;
                        } else if (this.cH > 0 && this.cH < this.cG) {
                            this.dm = 1;
                            this.dk = 1;
                            this.dl = 2;
                            this.cI = this.cH == 1 ? 2 : (this.cI == 2 ? 3 : 2);
                            this.cN = this.cI - 1;
                        } else if (this.cH == this.cG) {
                            if (this.co > 0 && this.bY == this.bZ && this.cl[0][0] > 0) {
                                if (this.cp[this.co - 1][0] > 2) {
                                    this.cJ = 1;
                                    this.cK = 0;
                                    this.cL = 0;
                                    this.cM = 2;
                                    this.dk = 0;
                                    this.dl = 4;
                                    this.dn = 10;
                                } else if (this.cp[this.co - 1][0] < 3) {
                                    bl = true;
                                    this.dl = 2;
                                }
                            } else {
                                bl = true;
                            }
                            if (bl) {
                                this.cI = this.cI == 2 ? 4 : 5;
                                this.bD = 1;
                                this.cF = 0;
                                this.cJ = 0;
                                this.cN = 0;
                            }
                        }
                    }
                    if (this.cJ == 1) {
                        if (this.cK == 0) {
                            this.cI = 1;
                            if (this.cL < this.cM) {
                                ++this.cL;
                            } else {
                                this.cK = 1;
                                this.cL = 0;
                                this.cI = 3;
                                if (this.bZ == 0) {
                                    this.cM = 3;
                                    this.dm = 0;
                                    this.cN = 0;
                                    this.n((this.co - 1) % 3);
                                } else if (this.bZ == 1) {
                                    this.cU = (this.co - 1) % 3 + 1;
                                    this.cM = nArrayArray[(this.co - 1) % 3][this.cp[this.co - 1][0] - 3];
                                    this.dm = 1;
                                    this.cN = 2;
                                    this.n((this.co - 1) % 3 + 3);
                                }
                                this.dk = 0;
                                this.dl = 3;
                            }
                        } else if (this.cK == 1) {
                            if (this.bS == 1) {
                                this.dT = 1;
                                this.dU = 0;
                            }
                            if (this.cL < this.cM - 1) {
                                ++this.cL;
                                this.dk = 0;
                                this.dl = 5;
                                if (this.bZ == 0) {
                                    if (this.cL == 1) {
                                        this.dm = 1;
                                        this.cN = 3;
                                    } else {
                                        this.dm = 0;
                                    }
                                    this.cI = 3;
                                } else if (this.bZ == 1) {
                                    this.cU = (this.co - 1) % 3 + 1;
                                    this.dl = (this.co - 1) % 3 == 0 ? 5 : 3;
                                    this.dm = 1;
                                    this.cI = this.cI == 2 ? 3 : 2;
                                    this.cN = this.cI + 1;
                                }
                            } else {
                                this.cN = 0;
                                this.dm = 0;
                                this.bD = 1;
                                this.cF = 0;
                                this.cI = 0;
                                this.cJ = 0;
                                this.aW();
                            }
                        }
                    }
                    if (this.dm <= 0) break;
                    this.Y();
                    break;
                }
                case 2: {
                    this.cF = 3;
                }
            }
        }
        if (this.cJ == 0) {
            n2 = 0;
            while (n2 < 4) {
                int n3 = 0;
                while (n3 < 6) {
                    this.dN[n2][n3] = 0;
                    ++n3;
                }
                ++n2;
            }
        }
        n2 = 0;
        while (n2 < 2) {
            if (this.dg[n2][1] == 1) {
                if (this.dg[n2][0] < 1) {
                    int[] nArray = this.dg[n2];
                    nArray[0] = nArray[0] + 1;
                } else {
                    this.dg[n2][1] = 0;
                }
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 4) {
            this.db[n2][0] = this.db[n2 + 1][0];
            this.db[n2][1] = this.db[n2 + 1][1];
            this.db[n2][2] = this.db[n2 + 1][2];
            this.db[n2][3] = this.db[n2 + 1][3];
            if (n2 == 3) {
                this.db[n2 + 1][0] = 0;
                this.db[n2 + 1][1] = 0;
                this.db[n2 + 1][2] = 0;
                this.db[n2 + 1][3] = 0;
            }
            ++n2;
        }
    }

    public final void Y() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        int n7 = 0;
        boolean bl2 = false;
        int[][] nArray = new int[2][4];
        int[] nArray2 = new int[4];
        int[][] nArrayArray = new int[4][];
        int[] nArray3 = new int[4];
        nArray3[1] = 1;
        nArray3[2] = 2;
        nArray3[3] = 3;
        nArrayArray[0] = nArray3;
        int[] nArray4 = new int[4];
        nArray4[0] = 1;
        nArray4[2] = 3;
        nArray4[3] = 2;
        nArrayArray[1] = nArray4;
        int[] nArray5 = new int[4];
        nArray5[0] = 2;
        nArray5[1] = 3;
        nArray5[2] = 1;
        nArrayArray[2] = nArray5;
        int[] nArray6 = new int[4];
        nArray6[0] = 3;
        nArray6[1] = 2;
        nArray6[3] = 1;
        nArrayArray[3] = nArray6;
        int[][] nArrayArray2 = nArrayArray;
        int[][] nArrayArray3 = new int[4][];
        nArrayArray3[0] = new int[4];
        int[] nArray7 = new int[4];
        nArray7[1] = 1;
        nArrayArray3[1] = nArray7;
        int[] nArray8 = new int[4];
        nArray8[1] = 2;
        nArray8[2] = 3;
        nArrayArray3[2] = nArray8;
        int[] nArray9 = new int[4];
        nArray9[1] = 1;
        nArray9[2] = 2;
        nArray9[3] = 3;
        nArrayArray3[3] = nArray9;
        int[][] nArrayArray4 = nArrayArray3;
        nArray[0][0] = this.ca;
        nArray[0][1] = this.cb;
        nArray[0][2] = this.cc;
        nArray[0][3] = this.cd;
        if (this.cJ == 0) {
            nArray2[this.cC] = 1;
            this.cP = 0;
        } else if (this.cJ == 1 && this.cK == 1) {
            if (this.bZ == 0) {
                if ((this.co - 1) % 3 == 0) {
                    nArray2[this.cC] = 1;
                    this.cO = this.ce[9] * 2;
                    this.cP = this.ce[12] * 2;
                } else if ((this.co - 1) % 3 == 1) {
                    n7 = this.co < 4 && this.cp[this.co - 1][0] < 6 ? this.cp[this.co - 1][0] - 2 : 4;
                    n2 = 0;
                    while (n2 < n7) {
                        nArray2[nArrayArray2[this.cC][nArrayArray4[n7 - 1][n2]]] = 1;
                        ++n2;
                    }
                    this.cO = this.ce[9] * 13 / 10;
                    this.cP = this.ce[12] * 13 / 10;
                } else if ((this.co - 1) % 3 == 2) {
                    nArray2[this.cC] = 1;
                    this.cO = this.ce[9] * 3;
                    this.cP = this.ce[12] * 3;
                }
            } else if (this.bZ == 1) {
                if ((this.co - 1) % 3 == 0) {
                    nArray2[0] = 1;
                    nArray2[1] = 1;
                    nArray2[2] = 1;
                    nArray2[3] = 1;
                } else {
                    nArray2[this.cC] = 1;
                }
                if ((this.co - 1) % 3 == 2) {
                    this.cO = this.ce[9] / 2 * 3;
                    this.cP = this.ce[12] / 2 * 3;
                } else {
                    this.cO = this.ce[9];
                    this.cP = this.ce[12];
                }
            }
        }
        n2 = 0;
        while (n2 < 4) {
            if (this.cJ == 1 && this.bZ == 0 && (this.co - 1) % 3 == 0) {
                n3 = 0;
                if (this.dN[n2][5] == 1) {
                    while (n3 < this.aT) {
                        if (this.aU[n3].ay == 1 && this.dN[n2][2] == this.aU[n3].h && this.dN[n2][3] == this.aU[n3].i && this.aU[n3].j < 9) {
                            this.aU[n3].r = 1;
                        }
                        ++n3;
                    }
                }
            } else if (nArray2[n2] > 0) {
                n3 = 0;
                n4 = this.a(30, nArray[0], n2, 90, 0);
                while (n3 < this.aT) {
                    if (this.aU[n3].ay == 1) {
                        nArray[1][0] = this.aU[n3].f;
                        nArray[1][1] = this.aU[n3].g;
                        nArray[1][2] = this.aU[n3].h;
                        nArray[1][3] = this.aU[n3].i;
                        n5 = this.a(30, nArray[0], nArray[1], n2, 0);
                        if (n4 == 2 && n5 == 1 && this.aU[n3].j < 9) {
                            this.aU[n3].r = 1;
                            ++n6;
                        }
                    }
                    ++n3;
                }
            }
            ++n2;
        }
        if (this.cJ == 0 && n6 == 0 && this.br == 0) {
            this.br = 1;
            this.bp = 2 + this.bY * 2;
            return;
        }
        if (this.cJ == 1 && this.bZ == 1) {
            if (this.gw == 0) {
                this.gw = 1;
                this.bq = 3;
                if (n6 == 0) {
                    this.gx = 1;
                    return;
                }
                this.gx = 2;
                return;
            }
            if (n6 == 0 && this.gx == 2) {
                this.gx = 1;
                return;
            }
            if (n6 > 0 && this.gx == 1) {
                this.gx = 2;
                return;
            }
            if (this.cL == this.cM - 1) {
                if (n6 == 0) {
                    this.br = 1;
                    this.bp = 2 + this.bY * 2;
                    return;
                }
                this.br = 1;
                this.bp = 3;
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    public final int Z() {
        block11: {
            var1_1 = 0;
            var2_2 = -1;
            var3_3 = false;
            var4_4 = 0;
            var5_5 = new int[4];
            while (var4_4 < this.aT) {
                if (this.aU[var4_4].ay == 1 && this.aU[var4_4].ah > 0 && this.t(this.aU[var4_4].f, this.ca) <= 30 && this.t(this.aU[var4_4].g, this.cb) <= 30) {
                    if (this.aU[var4_4].h == this.cc) {
                        if (this.aU[var4_4].i < this.cd) {
                            var5_5[0] = 1;
                        } else {
                            var5_5[1] = 1;
                        }
                    } else if (this.aU[var4_4].i == this.cd) {
                        if (this.aU[var4_4].h < this.cc) {
                            var5_5[2] = 1;
                        } else {
                            var5_5[3] = 1;
                        }
                    }
                }
                ++var4_4;
            }
            if (var5_5[this.cC] != 1) ** GOTO lbl27
            var2_2 = this.cC;
            break block11;
lbl-1000:
            // 1 sources

            {
                if (var5_5[var1_1] > 0) {
                    var2_2 = var1_1;
                }
                ++var1_1;
lbl27:
                // 2 sources

                ** while (var1_1 < 4)
            }
        }
        return var2_2;
    }

    public final void n(int n2) {
        int n3 = this.ca;
        int n4 = this.cb;
        int n5 = this.cc;
        int n6 = this.cd;
        int n7 = 0;
        int n8 = 0;
        boolean bl = false;
        boolean bl2 = false;
        int[][] nArrayArray = new int[4][];
        int[] nArray = new int[2];
        nArray[1] = -1;
        nArrayArray[0] = nArray;
        int[] nArray2 = new int[2];
        nArray2[1] = 1;
        nArrayArray[1] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[0] = -1;
        nArrayArray[2] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[0] = 1;
        nArrayArray[3] = nArray4;
        int[][] nArrayArray2 = nArrayArray;
        int[][] nArrayArray3 = new int[4][];
        nArrayArray3[0] = new int[2];
        int[] nArray5 = new int[2];
        nArray5[1] = -10;
        nArrayArray3[1] = nArray5;
        nArrayArray3[2] = new int[]{-5, -5};
        nArrayArray3[3] = new int[]{5, -5};
        int[][] nArrayArray4 = nArrayArray3;
        int[][] nArrayArray5 = new int[4][];
        int[] nArray6 = new int[4];
        nArray6[1] = 1;
        nArray6[2] = 2;
        nArray6[3] = 3;
        nArrayArray5[0] = nArray6;
        int[] nArray7 = new int[4];
        nArray7[0] = 1;
        nArray7[2] = 3;
        nArray7[3] = 2;
        nArrayArray5[1] = nArray7;
        int[] nArray8 = new int[4];
        nArray8[0] = 2;
        nArray8[1] = 3;
        nArray8[2] = 1;
        nArrayArray5[2] = nArray8;
        int[] nArray9 = new int[4];
        nArray9[0] = 3;
        nArray9[1] = 2;
        nArray9[3] = 1;
        nArrayArray5[3] = nArray9;
        int[][] nArrayArray6 = nArrayArray5;
        int[][] nArrayArray7 = new int[4][];
        nArrayArray7[0] = new int[4];
        int[] nArray10 = new int[4];
        nArray10[1] = 1;
        nArrayArray7[1] = nArray10;
        int[] nArray11 = new int[4];
        nArray11[1] = 2;
        nArray11[2] = 3;
        nArrayArray7[2] = nArray11;
        int[] nArray12 = new int[4];
        nArray12[1] = 1;
        nArray12[2] = 2;
        nArray12[3] = 3;
        nArrayArray7[3] = nArray12;
        int[][] nArrayArray8 = nArrayArray7;
        switch (n2) {
            case 0: 
            case 1: 
            case 2: {
                n7 = this.co < 4 && this.cp[(this.co - 1) % 3][0] < 6 ? this.cp[(this.co - 1) % 3][0] - 2 : 4;
                int n9 = 0;
                while (n9 < n7) {
                    if (n2 == 0) {
                        n8 = this.cC;
                        if (this.a(n5, n6, n3, n4, n8, 20, 90, 0) == 1) {
                            this.dN[n9][0] = n3 += nArrayArray2[n8][0] * 20;
                            this.dN[n9][1] = n4 += nArrayArray2[n8][1] * 20;
                            this.dN[n9][2] = n5 += nArrayArray2[n8][0];
                            this.dN[n9][3] = n6 += nArrayArray2[n8][1];
                        }
                    } else {
                        if (n2 == 1) {
                            n8 = nArrayArray6[this.cC][nArrayArray8[n7 - 1][n9]];
                            this.dN[n9][0] = this.ca + nArrayArray2[n8][0] * 20;
                            this.dN[n9][1] = this.cb + nArrayArray2[n8][1] * 20;
                        } else {
                            n8 = this.cC;
                            this.dN[n9][0] = this.ca + nArrayArray2[n8][0] * 20 + nArrayArray4[n9][0];
                            this.dN[n9][1] = this.cb + nArrayArray2[n8][1] * 20 + nArrayArray4[n9][1];
                        }
                        this.dN[n9][2] = this.cc + nArrayArray2[n8][0];
                        this.dN[n9][3] = this.cd + nArrayArray2[n8][1];
                    }
                    this.dN[n9][4] = 0;
                    this.dN[n9][5] = 1;
                    ++n9;
                }
                return;
            }
            case 3: {
                int n10 = 0;
                while (n10 < 4) {
                    this.dN[n10][0] = this.ca + nArrayArray2[n10][0] * 20;
                    this.dN[n10][1] = this.cb + nArrayArray2[n10][1] * 20;
                    this.dN[n10][2] = this.cc + nArrayArray2[n10][0];
                    this.dN[n10][3] = this.cd + nArrayArray2[n10][1];
                    this.dN[n10][4] = 0;
                    this.dN[n10][5] = 1;
                    ++n10;
                }
                break;
            }
        }
    }

    public final void aa() {
        int n2 = 0;
        boolean bl = false;
        boolean bl2 = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        boolean bl3 = false;
        int[] nArray = new int[2];
        if (this.dq == 0) {
            n3 = 0;
            while (n3 < 5) {
                n4 = 0;
                while (n4 < 4) {
                    this.db[n3][n4] = 0;
                    ++n4;
                }
                ++n3;
            }
            this.a("sskill", 432);
            this.b("sskill", 24082);
        }
        if (this.bZ == 0) {
            block0 : switch (this.dr) {
                case 0: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(6, 0);
                            this.p();
                            this.m();
                            this.cI = 2;
                            this.dt = 0;
                            this.dk = 0;
                            this.dm = 0;
                            ++this.dq;
                            break;
                        }
                        case 1: {
                            if (this.cm[0] < 3) {
                                if (this.dt < 3) {
                                    ++this.dt;
                                } else {
                                    ++this.dq;
                                }
                            } else if (this.dt < 7) {
                                ++this.dt;
                            } else {
                                ++this.dq;
                            }
                            switch (this.cC) {
                                case 0: {
                                    this.cC = 2;
                                    break;
                                }
                                case 1: {
                                    this.cC = 3;
                                    break;
                                }
                                case 2: {
                                    this.cC = 1;
                                    break;
                                }
                                case 3: {
                                    this.cC = 0;
                                }
                            }
                            this.cI = this.cI == 2 ? 3 : 2;
                            if (this.dt - 1 >= this.dv) break;
                            this.du[this.dt - 1][7] = 1;
                            if (this.dt % 2 != 1) break;
                            this.br = 1;
                            this.bp = 4;
                            break;
                        }
                        case 2: {
                            n3 = 0;
                            while (n3 < this.dv && n5 == 0) {
                                if (this.du[n3][7] == 1) {
                                    n5 = 1;
                                    continue;
                                }
                                ++n3;
                            }
                            if (n5 == 0) {
                                n3 = 0;
                                while (n3 < this.aT && n5 == 0) {
                                    if (this.aU[n3].R > 0) {
                                        n5 = 1;
                                        continue;
                                    }
                                    ++n3;
                                }
                            }
                            if (n5 != 0) break;
                            this.dk = 0;
                            this.dm = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            this.ad[0] = null;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(13, 0);
                            this.ad[1] = this.c(14, 0);
                            this.p();
                            this.dQ = 0;
                            this.dP = this.cm[1] + 30;
                            this.dO = 0;
                            this.dR[0] = this.cc;
                            this.dR[1] = this.cd;
                            ++this.dq;
                            break;
                        }
                        case 1: {
                            if (this.dQ < this.dP) {
                                ++this.dQ;
                                break;
                            }
                            ++this.dq;
                            this.br = 1;
                            this.bp = 11;
                            break;
                        }
                        case 2: {
                            this.cI = 2;
                            if (this.dO > 0) {
                                if (this.du[0][2] < 6) {
                                    this.dT = 1;
                                    this.dU = 0;
                                    n3 = 0;
                                    while (n3 < 6) {
                                        int[] nArray2 = this.du[n3];
                                        nArray2[2] = nArray2[2] + 1;
                                        ++n3;
                                    }
                                    break block0;
                                }
                                ++this.dq;
                                break;
                            }
                            ++this.dq;
                            break;
                        }
                        case 3: {
                            n3 = 0;
                            while (n3 < 20) {
                                n4 = 0;
                                while (n4 < 9) {
                                    this.du[n3][n4] = 0;
                                    ++n4;
                                }
                                ++n3;
                            }
                            this.dk = 0;
                            this.dm = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            this.ad[0] = null;
                            this.ad[1] = null;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (this.dq) {
                        case 0: {
                            n3 = 0;
                            while (n3 < 6) {
                                this.ad[n3] = this.c(7 + n3, 0);
                                ++n3;
                            }
                            this.ad[6] = this.c(15, 0);
                            this.p();
                            this.ds = 0;
                            this.cI = 2;
                            ++this.dq;
                            break;
                        }
                        case 1: {
                            this.ag();
                            this.ds = 1;
                            this.cI = 0;
                            ++this.dq;
                            this.br = 1;
                            this.bp = 10;
                            break;
                        }
                        case 2: 
                        case 3: 
                        case 4: {
                            this.dT = 1;
                            this.dU = 0;
                            n3 = 0;
                            while (n3 < this.cm[2]) {
                                this.dK[n3][this.dq - 1][3] = 1;
                                ++n3;
                            }
                            ++this.dq;
                            if (this.dq != 5) break;
                            this.br = 1;
                            this.bp = 10;
                            break;
                        }
                        case 5: 
                        case 6: 
                        case 7: 
                        case 8: {
                            this.dT = 1;
                            this.dU = 0;
                            n3 = 0;
                            while (n3 < this.cm[2]) {
                                this.dK[n3][this.dq - 5][2] = 0;
                                ++n3;
                            }
                            ++this.dq;
                            break;
                        }
                        case 9: {
                            if (this.dK[0][3][2] != 4) break;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            n3 = 0;
                            while (n3 < 7) {
                                this.ad[n3] = null;
                                ++n3;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(this.cC * 2 + 21, 0);
                            this.ad[1] = this.c(this.cC * 2 + 22, 0);
                            this.p();
                            this.dk = 0;
                            this.dl = 0;
                            this.dm = 0;
                            ++this.dq;
                            break block0;
                        }
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: {
                            this.cI = 4;
                            ++this.dq;
                            break block0;
                        }
                        case 6: {
                            this.dT = 1;
                            this.dU = 0;
                            this.cI = 5;
                            ++this.dq;
                            break block0;
                        }
                        case 7: {
                            this.dn = 20 + this.cm[3] * 5;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            this.ad[0] = null;
                            this.ad[1] = null;
                        }
                    }
                }
            }
            if (this.dr == 0) {
                n3 = 0;
                while (n3 < this.dv) {
                    if (this.du[n3][7] == 1) {
                        if (this.du[n3][4] == 0) {
                            if (this.du[n3][0] < 0 || this.du[n3][0] > this.bx[0] || this.du[n3][1] < 0 || this.du[n3][1] > this.bx[1]) {
                                this.du[n3][4] = 1;
                                if (this.du[n3][2] == 0 && this.du[n3][3] == 0) {
                                    this.du[n3][7] = 0;
                                } else {
                                    this.du[n3][5] = (this.du[n3][2] - this.du[n3][0]) / 4;
                                    this.du[n3][6] = (this.du[n3][3] - this.du[n3][1]) / 4;
                                }
                            } else {
                                int[] nArray3 = this.du[n3];
                                nArray3[0] = nArray3[0] + this.du[n3][5];
                                int[] nArray4 = this.du[n3];
                                nArray4[1] = nArray4[1] + this.du[n3][6];
                            }
                        } else {
                            nArray[0] = this.t(this.du[n3][0], this.du[n3][2]);
                            nArray[1] = this.t(this.du[n3][1], this.du[n3][3]);
                            if (nArray[0] < 20 && nArray[1] < 20) {
                                if (this.aU[this.du[n3][8]].j != 9) {
                                    ++this.aU[this.du[n3][8]].R;
                                }
                                this.du[n3][7] = 0;
                                this.dk = 0;
                                this.dl = 5;
                                this.cN = this.cN == 1 ? 2 : 1;
                            } else {
                                int[] nArray5 = this.du[n3];
                                nArray5[0] = nArray5[0] + this.du[n3][5];
                                int[] nArray6 = this.du[n3];
                                nArray6[1] = nArray6[1] + this.du[n3][6];
                            }
                            if (this.du[n3][0] < 0 || this.du[n3][0] > this.bx[0] || this.du[n3][1] < 0 || this.du[n3][1] > this.bx[1]) {
                                this.du[n3][7] = 0;
                            }
                        }
                    }
                    ++n3;
                }
            } else if (this.dr == 2) {
                n3 = 0;
                while (n3 < 6) {
                    n4 = 0;
                    while (n4 < 4) {
                        if (this.dK[n3][n4][3] == 1 && this.dK[n3][n4][2] < 4) {
                            int[] nArray7 = this.dK[n3][n4];
                            nArray7[2] = nArray7[2] + 1;
                        }
                        ++n4;
                    }
                    ++n3;
                }
            }
        } else if (this.bZ == 1) {
            block36 : switch (this.dr) {
                case 0: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(this.cC / 2, 0);
                            this.ad[1] = this.c(this.cC + 2, 0);
                            this.p();
                            this.dt = 2;
                            this.cI = 2;
                            this.dk = 0;
                            this.dl = 0;
                            this.dm = 0;
                            ++this.dq;
                            break;
                        }
                        case 1: {
                            this.dt = this.cm[0] * 4;
                            this.dw = this.ei;
                            ++this.dq;
                            break;
                        }
                        case 2: {
                            n2 = this.ei - this.dw;
                            if (n2 < 0) {
                                n2 += 9999;
                            }
                            this.dl = 0;
                            if (this.ei % 2 == 0) {
                                this.dt += 2;
                                this.dm = 1;
                            } else {
                                this.dt -= 2;
                                this.dm = 2;
                            }
                            if (n2 != 20) break;
                            this.dl = 3;
                            this.dm = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            this.ad[0] = null;
                            this.ad[1] = null;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(15, 0);
                            this.ad[1] = this.c(19, 0);
                            this.ad[2] = this.c(20, 0);
                            this.p();
                            this.ah();
                            this.dA = 1;
                            this.dM = 1;
                            this.dL = 1;
                            ++this.dq;
                            break;
                        }
                        case 1: 
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: {
                            ++this.dq;
                            if (this.dq != 5) break;
                            this.br = 1;
                            this.bp = 16;
                            break;
                        }
                        case 6: {
                            n5 = 0;
                            n3 = 0;
                            while (n3 < this.dv) {
                                nArray[0] = this.t(this.du[n3][0], this.du[n3][2]);
                                nArray[1] = this.t(this.du[n3][1], this.du[n3][3]);
                                if (nArray[0] < 20 && nArray[1] < 20) {
                                    this.du[n3][7] = 0;
                                } else {
                                    int[] nArray8 = this.du[n3];
                                    nArray8[0] = nArray8[0] + this.du[n3][5];
                                    int[] nArray9 = this.du[n3];
                                    nArray9[1] = nArray9[1] + this.du[n3][6];
                                }
                                if (this.du[n3][7] == 1) {
                                    ++n5;
                                }
                                ++n3;
                            }
                            if (n5 != 0) break;
                            ++this.dq;
                            break;
                        }
                        case 7: {
                            this.dA = 0;
                            this.dM = 0;
                            this.dL = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            n3 = 0;
                            while (n3 < 3) {
                                this.ad[n3] = null;
                                ++n3;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (this.dq) {
                        case 0: {
                            n3 = 0;
                            while (n3 < 4) {
                                this.ad[n3] = this.c(15 + n3, 0);
                                ++n3;
                            }
                            this.p();
                            this.ds = 1;
                            ++this.dq;
                            break;
                        }
                        case 1: {
                            this.br = 1;
                            this.bp = 10;
                            this.dA = 1;
                            this.dM = 1;
                            ++this.dq;
                            break;
                        }
                        case 2: 
                        case 3: 
                        case 4: 
                        case 5: 
                        case 6: {
                            ++this.dq;
                            break;
                        }
                        case 7: 
                        case 8: 
                        case 9: 
                        case 10: 
                        case 11: 
                        case 12: 
                        case 13: 
                        case 14: 
                        case 15: 
                        case 16: {
                            this.ds = this.dq % 2 + 1;
                            ++this.dq;
                            break;
                        }
                        case 17: {
                            this.dn = 30 + this.cm[2] * 10;
                            this.dA = 0;
                            this.dM = 0;
                            this.dL = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            n3 = 0;
                            while (n3 < 4) {
                                this.ad[n3] = null;
                                ++n3;
                            }
                            break;
                        }
                    }
                    break;
                }
                case 3: {
                    switch (this.dq) {
                        case 0: {
                            this.ad[0] = this.c(15, 0);
                            this.ad[1] = this.c(37, 0);
                            this.ad[2] = this.c(38, 0);
                            this.p();
                            this.ds = 1;
                            this.dT = 1;
                            this.dU = 0;
                            ++this.dq;
                            break block36;
                        }
                        case 1: 
                        case 2: 
                        case 3: {
                            if (this.dq == 1) {
                                this.br = 1;
                                this.bp = 4;
                            }
                            this.dM = 1;
                            ++this.dq;
                            break block36;
                        }
                        case 4: {
                            this.dn = 30 + this.cm[3] * 10;
                            this.dA = 0;
                            this.dM = 0;
                            this.dL = 0;
                            this.dp = 0;
                            this.ds = 0;
                            this.cI = 0;
                            this.bD = 1;
                            bl3 = true;
                            n3 = 0;
                            while (n3 < 3) {
                                this.ad[n3] = null;
                                ++n3;
                            }
                            break block36;
                        }
                    }
                }
            }
            if (this.dr == 2 && this.dq > 0) {
                this.dS = this.dS < 3 ? ++this.dS : 0;
            }
        }
        if (this.dr == 4 || this.dr == 5) {
            switch (this.dq) {
                case 0: {
                    this.cI = 2;
                    this.dm = 1;
                    this.dk = 0;
                    ++this.dq;
                    break;
                }
                case 1: {
                    this.cI = 4;
                    this.dm = 0;
                    this.dk = 2;
                    ++this.dq;
                    break;
                }
                case 2: {
                    this.dk = 0;
                    this.dp = 0;
                    this.cI = 0;
                    this.bD = 1;
                    bl3 = true;
                    if (this.dr == 4) {
                        this.dH = 1;
                        this.dJ = this.ei;
                        this.dI = (this.cm[4] - 1) * 10 + 30;
                        this.cE = this.dV == 1 ? 10 : 20;
                        this.ca = this.cc * 20 + 10;
                        this.cb = (this.cd + 1) * 20;
                        break;
                    }
                    this.af();
                }
            }
        }
        if (bl3) {
            this.dr = 0;
            this.cj[this.dD][2] = 0;
            this.ck[this.dD][0] = this.ei;
        }
    }

    public final void a(int[] nArray, int[] nArray2) {
        int n2 = 0;
        int n3 = 0;
        boolean bl = false;
        int n4 = 0;
        this.fO = 0;
        int n5 = 0;
        while (n5 < this.aT) {
            this.aU[n5] = new k();
            this.aU[n5].h = nArray[n5 * 7 + 0];
            this.aU[n5].i = nArray[n5 * 7 + 1];
            this.aU[n5].a = nArray[n5 * 7 + 2];
            this.aU[n5].c = nArray[n5 * 7 + 3];
            this.aU[n5].l = nArray[n5 * 7 + 4];
            n2 = this.aU[n5].m = nArray[n5 * 7 + 5];
            this.aU[n5].Y = nArray2[n2 * 29 + 0];
            this.aU[n5].Z = nArray2[n2 * 29 + 1];
            this.aU[n5].aa = nArray2[n2 * 29 + 2];
            this.aU[n5].ab = nArray2[n2 * 29 + 3];
            this.aU[n5].ac = nArray2[n2 * 29 + 4];
            this.aU[n5].ad = nArray2[n2 * 29 + 5];
            this.aU[n5].ae = nArray2[n2 * 29 + 6] * nArray2[n2 * 29 + 7];
            this.aU[n5].af = nArray2[n2 * 29 + 8] * nArray2[n2 * 29 + 9];
            this.aU[n5].ag = nArray2[n2 * 29 + 10] * nArray2[n2 * 29 + 11];
            this.aU[n5].ah = nArray2[n2 * 29 + 12] * nArray2[n2 * 29 + 13];
            this.aU[n5].ai = nArray2[n2 * 29 + 14];
            this.aU[n5].aj = nArray2[n2 * 29 + 15];
            this.aU[n5].ak = nArray2[n2 * 29 + 16];
            this.aU[n5].al = nArray2[n2 * 29 + 17];
            this.aU[n5].am = nArray2[n2 * 29 + 18];
            this.aU[n5].an = nArray2[n2 * 29 + 19];
            this.aU[n5].ao = nArray2[n2 * 29 + 20] * nArray2[n2 * 29 + 21];
            this.aU[n5].ap = nArray2[n2 * 29 + 22] * nArray2[n2 * 29 + 23];
            this.aU[n5].aq = nArray2[n2 * 29 + 24];
            this.aU[n5].ar = nArray2[n2 * 29 + 25];
            this.aU[n5].as = nArray2[n2 * 29 + 26];
            this.aU[n5].at = nArray2[n2 * 29 + 27];
            this.aU[n5].au = nArray2[n2 * 29 + 28];
            this.aU[n5].av = this.ba[this.aU[n5].ar * 5 + 1];
            this.aU[n5].aw = this.ba[this.aU[n5].ar * 5 + 4];
            this.aU[n5].K = this.ei;
            this.aU[n5].f = this.aU[n5].h * 20 + 10;
            this.aU[n5].g = (this.aU[n5].i + 1) * 20;
            this.aU[n5].aJ[0] = this.aU[n5].h;
            this.aU[n5].aJ[1] = this.aU[n5].i;
            this.aU[n5].aJ[2] = this.aU[n5].a;
            this.aU[n5].aJ[3] = this.aU[n5].ah;
            if (nArray[n5 * 7 + 6] != 0 && nArray[n5 * 7 + 6] < this.eD || nArray[n5 * 7 + 6] == 200) {
                this.aU[n5].ax = 0;
                this.aU[n5].ay = 0;
            }
            if (this.aU[n5].am > 0) {
                this.gj = 1;
                this.gk = this.aU[n5].am;
                this.gl = this.ei;
            }
            if ((this.eD < 22 || this.eD == 23 && this.eJ[0][22] == 1 || this.eD > 23) && this.bV == 11 && n5 == 11) {
                this.aU[n5].ax = 0;
                this.aU[n5].ay = 0;
            }
            if ((this.eD < 66 || this.eD == 66 && this.eJ[0][22] == 1) && this.bV == 39 && n5 == 0) {
                this.aU[n5].ax = 0;
                this.aU[n5].ay = 0;
            }
            n3 = 0;
            while (n3 < 8 && !bl) {
                if (this.aU[n5].Y == this.eL[n3][0]) {
                    if (n3 < 3) {
                        switch (this.eL[n3][1]) {
                            case 0: 
                            case 3: {
                                this.aU[n5].ah += this.aU[n5].ah / 2;
                                break;
                            }
                            case 1: {
                                break;
                            }
                            case 2: {
                                this.aU[n5].ah += this.aU[n5].ah / 5;
                            }
                        }
                    } else {
                        switch (this.eL[n3][1]) {
                            case 0: {
                                break;
                            }
                            case 1: {
                                this.aU[n5].ah += this.aU[n5].ah / 10;
                                break;
                            }
                            case 2: {
                                this.aU[n5].ah += this.aU[n5].ah * 3 / 10;
                                break;
                            }
                            case 3: {
                                this.aU[n5].ah += this.aU[n5].ah / 2;
                            }
                        }
                    }
                    this.aU[n5].aJ[3] = this.aU[n5].ah;
                    bl = true;
                }
                ++n3;
            }
            ++n5;
        }
        n4 = 0;
        n5 = 0;
        while (n5 < this.aY) {
            if (n4 != nArray2[n5 * 29 + 0]) {
                n3 = 0;
                bl = false;
                while (n3 < this.aT && !bl) {
                    if (n5 == this.aU[n3].m && this.aU[n3].ax == 1) {
                        this.fN[this.fO] = n3 + 1;
                        ++this.fO;
                        n4 = nArray2[n5 * 29 + 0];
                        bl = true;
                        continue;
                    }
                    ++n3;
                }
            }
            ++n5;
        }
    }

    public final void b(int[] nArray) {
        int n2 = 0;
        boolean bl = false;
        boolean bl2 = false;
        n2 = 0;
        while (n2 < this.aV) {
            this.aX[n2] = new d();
            this.aX[n2].a = nArray[n2 * 30 + 0];
            this.aX[n2].b = nArray[n2 * 30 + 1];
            this.aX[n2].c = nArray[n2 * 30 + 2];
            this.aX[n2].j = nArray[n2 * 30 + 3];
            this.aX[n2].g = nArray[n2 * 30 + 4];
            this.aX[n2].l = nArray[n2 * 30 + 5];
            this.aX[n2].i = nArray[n2 * 30 + 6];
            this.aX[n2].o = nArray[n2 * 30 + 7];
            this.aX[n2].p = nArray[n2 * 30 + 8];
            this.aX[n2].q = nArray[n2 * 30 + 9];
            this.aX[n2].r = nArray[n2 * 30 + 19] / 100;
            this.aX[n2].s = nArray[n2 * 30 + 19] % 100;
            this.aX[n2].t = nArray[n2 * 30 + 27];
            this.aX[n2].u = nArray[n2 * 30 + 28];
            this.aX[n2].n[0][0] = nArray[n2 * 30 + 10];
            this.aX[n2].n[0][1] = nArray[n2 * 30 + 11];
            this.aX[n2].n[0][2] = nArray[n2 * 30 + 12];
            this.aX[n2].n[1][0] = nArray[n2 * 30 + 13];
            this.aX[n2].n[1][1] = nArray[n2 * 30 + 14];
            this.aX[n2].n[1][2] = nArray[n2 * 30 + 15];
            this.aX[n2].n[2][0] = nArray[n2 * 30 + 16];
            this.aX[n2].n[2][1] = nArray[n2 * 30 + 17];
            this.aX[n2].n[2][2] = nArray[n2 * 30 + 18];
            this.aX[n2].v[0][0] = nArray[n2 * 30 + 21];
            this.aX[n2].v[0][1] = nArray[n2 * 30 + 22];
            this.aX[n2].v[1][0] = nArray[n2 * 30 + 23];
            this.aX[n2].v[1][1] = nArray[n2 * 30 + 24];
            this.aX[n2].v[2][0] = nArray[n2 * 30 + 25];
            this.aX[n2].v[2][1] = nArray[n2 * 30 + 26];
            this.aX[n2].d = this.aX[n2].a * 20 + 10;
            this.aX[n2].e = (this.aX[n2].b + 1) * 20;
            if (nArray[n2 * 30 + 20] != 0 && nArray[n2 * 30 + 20] <= this.eD) {
                this.aX[n2].l = 1;
            }
            if (nArray[n2 * 30 + 29] != 0 && nArray[n2 * 30 + 29] <= this.eD) {
                this.aX[n2].l = 0;
            }
            if (this.eD == 48 && this.bV == 80 && n2 == 1) {
                this.aX[n2].l = 1;
            }
            ++n2;
        }
    }

    public final void c(int[] nArray) {
        int n2 = 0;
        int n3 = 0;
        while (n2 < this.bd) {
            if (this.eD >= nArray[n2 * 6 + 4] && this.fh[nArray[n2 * 6 + 5] - 1] == 0) {
                this.bm[n3].a = nArray[n2 * 6 + 0];
                this.bm[n3].b = nArray[n2 * 6 + 1];
                if (nArray[n2 * 6 + 2] == 100) {
                    this.bm[n3].c = 14;
                    this.bm[n3].d = nArray[n2 * 6 + 3];
                    this.bm[n3].e = 100;
                } else {
                    this.bm[n3].c = nArray[n2 * 6 + 2] == 15 ? nArray[n2 * 6 + 2] : nArray[n2 * 6 + 2] % 10;
                    this.bm[n3].d = nArray[n2 * 6 + 3];
                    this.bm[n3].e = nArray[n2 * 6 + 2] / 10;
                }
                this.bm[n3].g = nArray[n2 * 6 + 5];
                this.bm[n3].f = 1;
                switch (nArray[n2 * 6 + 5]) {
                    case 8: {
                        this.bm[n3].e = 5;
                    }
                }
                if (this.bm[n3].c < 13) {
                    this.c(this.bm[n3].c, this.bm[n3].d, 0);
                    this.bm[n3].k = this.fo;
                } else if (this.bm[n3].c == 14) {
                    this.bm[n3].k = this.bm[n3].d * this.bm[n3].e + "\u4e24";
                }
                this.bm[n3].j = this.bm[n3].c == 1 ? 8 : (this.bm[n3].c == 2 || this.bm[n3].c == 3 ? 9 : (this.bm[n3].c == 4 || this.bm[n3].c == 5 ? 10 : (this.bm[n3].c == 6 ? 11 : (this.bm[n3].c == 11 || this.bm[n3].c == 12 ? 12 : 3))));
                ++n3;
            }
            ++n2;
        }
    }

    public final void o(int n2) {
        int n3 = 0;
        if (n2 == 1) {
            n3 = 2382;
        }
        this.gJ = 1;
        try {
            String string = "/data/WineData.dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var8_6 = null;
            this.gD = inputStream = o._m(clazz, string);
            this.gE = new DataInputStream(this.gD);
            byte[] byArray = new byte[13];
            if (n2 == 0) {
                n3 += this.eD * 13;
            } else if (n2 == 1) {
                n3 += (this.fl - 4) * 13;
            }
            this.gE.skip(n3);
            this.gE.read(byArray, 0, 13);
            this.g();
            int n4 = 0;
            while (n4 < 13) {
                this.eF[n4] = byArray[n4] + 128;
                ++n4;
            }
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final int ab() {
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        if (this.fm == 1) {
            if (this.fl == 4 || this.fl == 5) {
                n3 = 0;
                while (n3 < this.aT) {
                    if (this.aU[n3].ax == 1) {
                        ++n4;
                    }
                    ++n3;
                }
                if (n4 == 0) {
                    bl = true;
                    if (this.fl == 4) {
                        this.aH[1][23 * this.bC[0] + 8] = 0;
                        int[] nArray = this.aH[3];
                        int n5 = 23 * this.bC[0] + 8;
                        nArray[n5] = nArray[n5] - 9;
                        this.aH[1][23 * this.bC[0] + 9] = 0;
                        int[] nArray2 = this.aH[3];
                        int n6 = 23 * this.bC[0] + 9;
                        nArray2[n6] = nArray2[n6] - 9;
                    } else if (this.fl == 5) {
                        this.aH[1][22 * this.bC[0] + 12] = 0;
                        int[] nArray = this.aH[3];
                        int n7 = 22 * this.bC[0] + 12;
                        nArray[n7] = nArray[n7] - 9;
                        this.aH[1][22 * this.bC[0] + 13] = 0;
                        int[] nArray3 = this.aH[3];
                        int n8 = 22 * this.bC[0] + 13;
                        nArray3[n8] = nArray3[n8] - 9;
                    }
                }
            } else {
                bl = true;
            }
        } else if (this.eJ[0][0] == 0) {
            if (this.eF[11] == 1 || this.eF[11] == 3) {
                if (this.eJ[0][22] == 1) {
                    bl = true;
                }
            } else {
                bl = true;
            }
        }
        if (bl) {
            switch (this.eF[12]) {
                case 0: {
                    if (this.eF[0] == 0) break;
                    n2 = 2;
                    break;
                }
                case 1: {
                    if (this.bV != this.eF[0] || this.eF[1] > this.cc || this.cc > this.eF[3] || this.eF[2] > this.cd || this.cd > this.eF[4]) break;
                    n2 = 2;
                    break;
                }
                case 2: {
                    if (this.bV != this.eF[0] || this.bH != 1 || this.aU[this.eF[1]].ax != 0) break;
                    n2 = 2;
                    break;
                }
                case 3: {
                    n4 = this.h(this.eF[1], this.eF[2]);
                    if (n4 <= -1 || this.cq[n4][4] < this.eF[3]) break;
                    n2 = 2;
                    break;
                }
                case 4: {
                    if (this.bV != this.eF[0] || this.ff != this.eF[1]) break;
                    n2 = 2;
                    break;
                }
                case 5: {
                    if (this.bV != this.eF[0] || this.fg != 1 || this.bm[this.fe].a != this.eF[1] || this.bm[this.fe].b != this.eF[2] || this.bm[this.fe].g <= 0) break;
                    n2 = 2;
                }
            }
            if (n2 == 2) {
                this.ff = 0;
            }
        }
        return n2;
    }

    /*
     * Unable to fully structure code
     */
    public final void ac() {
        block565: {
            var1_1 = false;
            var2_2 = false;
            var3_3 = 0;
            var4_4 = 0;
            this.bu = 22;
            block0 : switch (this.eD) {
                case 0: {
                    switch (this.eE) {
                        case 0: {
                            this.ca = this.cc * 20;
                            this.eT = 1;
                            this.bE = 0;
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            if (this.cc == 7 && this.ca % 20 == 10) {
                                this.cC = 1;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            this.cC = 3;
                            break;
                        }
                        case 3: {
                            if (this.cd != 12 || this.cb % 20 != 0) break;
                            this.cC = 2;
                            ++this.eE;
                            break;
                        }
                        case 4: {
                            if (this.cc != 0 || this.ca % 20 != 10) break;
                            this.b(60, 15, 13, 2, 0);
                            this.a(1, 0, 4, 7, 1);
                            this.aX[0].l = 1;
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            this.eT = 1;
                            this.bE = 0;
                            var1_1 = true;
                            break;
                        }
                        case 7: {
                            if (this.cc == 12 && this.ca % 20 == 10) {
                                this.bE = 0;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 8: 
                        case 9: 
                        case 10: 
                        case 11: {
                            this.cC = this.eE % 2 + 2;
                            ++this.eE;
                            break;
                        }
                        case 12: {
                            this.cC = 0;
                            this.eZ = 1;
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            ++this.eE;
                            break;
                        }
                        case 13: {
                            if (this.aX[0].b == 13 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 3;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 1;
                            this.aX[0].h = 1;
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            break;
                        }
                        case 14: {
                            if (this.aX[0].a == 10 && this.aX[0].d % 20 == 10) {
                                this.aX[0].h = 0;
                                this.eZ = 0;
                                this.cC = 2;
                                this.eT = 8;
                                var1_1 = true;
                                break;
                            }
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            break;
                        }
                        case 16: {
                            if (this.aU[0].i == 12 && this.aU[0].g % 20 == 0) {
                                this.aU[0].a = 2;
                                this.aU[0].e = 2;
                                ++this.eE;
                                break;
                            }
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.aU[0].B = 0;
                            this.aU[0].C = 0;
                            this.aU[0].a = 0;
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.aU[0].e = 0;
                            break;
                        }
                        case 17: {
                            if (this.aU[0].h != 12 || this.aU[0].f % 20 != 10) break;
                            this.aU[0].e = 4;
                            this.eT = 14;
                            var1_1 = true;
                            break;
                        }
                        case 19: {
                            if (this.aU[0].h == 13 && this.aU[0].f % 20 == 10) {
                                this.aU[0].a = 1;
                                this.aU[0].e = 1;
                                ++this.eE;
                                break;
                            }
                            this.aU[0].e = 3;
                            this.aU[0].a = 3;
                            break;
                        }
                        case 20: {
                            if (this.aU[0].i != 15 || this.aU[0].g % 20 != 0) break;
                            this.aU[0].e = 4;
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            this.eT = 3;
                            var1_1 = true;
                            break;
                        }
                        case 22: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 9 && this.cb % 20 == 0) {
                                this.bE = 0;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 3: 
                        case 4: 
                        case 5: 
                        case 6: 
                        case 7: 
                        case 8: {
                            if (this.eE == 3) {
                                this.dT = 1;
                                this.dU = 0;
                                this.r(70, 1000);
                            }
                            ++this.eE;
                            break;
                        }
                        case 9: 
                        case 10: 
                        case 11: 
                        case 12: {
                            this.eZ = 0;
                            this.cC = this.cC == 0 ? 3 : (this.cC == 3 ? 1 : (this.cC == 1 ? 2 : 0));
                            ++this.eE;
                            break;
                        }
                        case 13: {
                            this.cC = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 15: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 2: 
                case 6: 
                case 8: 
                case 9: 
                case 10: 
                case 13: 
                case 14: 
                case 15: 
                case 16: 
                case 19: 
                case 25: 
                case 26: 
                case 27: 
                case 30: 
                case 37: 
                case 39: 
                case 42: 
                case 45: 
                case 47: 
                case 49: 
                case 50: 
                case 53: 
                case 54: 
                case 56: 
                case 57: 
                case 58: 
                case 59: 
                case 60: 
                case 61: 
                case 62: 
                case 64: 
                case 65: 
                case 66: 
                case 70: 
                case 72: 
                case 73: 
                case 74: 
                case 75: 
                case 78: 
                case 79: 
                case 80: 
                case 81: 
                case 84: {
                    switch (this.eE) {
                        case 0: {
                            if (this.eD == 13) {
                                this.dT = 1;
                                this.dU = 0;
                                this.r(70, 1000);
                            } else if (this.eD == 42) {
                                this.aU[0].ay = 1;
                                this.aU[0].ax = 1;
                            }
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            if (this.eD == 8) {
                                this.c(1, 20, 0);
                                this.fn[4] = 5;
                                this.d(this.fn);
                            } else if (this.eD == 10) {
                                this.ce[7] = this.ce[7] + 3;
                                this.v(this.bZ);
                                this.w(this.bZ);
                                this.aj();
                            } else if (this.eD == 14) {
                                this.cm[2] = 1;
                                this.cm[3] = 1;
                                this.cm[5] = 1;
                            } else if (this.eD == 42) {
                                this.aU[0].ay = 0;
                                this.aU[0].ax = 0;
                            }
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 3: 
                case 7: {
                    switch (this.eE) {
                        case 0: {
                            this.aX[0].h = 0;
                            if (this.cd == 12 && this.cb % 20 == 0) {
                                this.cC = 2;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            if (this.cc != 10 || this.ca % 20 != 10) break;
                            this.cC = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.cd != 10 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            if (this.eD == 3) {
                                var3_3 = 0;
                                while (var3_3 < 3) {
                                    this.cp[var3_3][0] = 1;
                                    this.cp[var3_3][1] = 0;
                                    this.cp[var3_3][2] = 1010;
                                    ++var3_3;
                                }
                                this.co = 1;
                            } else {
                                var4_4 = this.bZ == 0 ? 2 : 3;
                                this.c(var4_4, 0, 0);
                                this.d(this.fn);
                            }
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 4: {
                    switch (this.eE) {
                        case 0: {
                            this.aX[0].h = 0;
                            if (this.cd == 12 && this.cb % 20 == 0) {
                                this.cC = 2;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            if (this.cc != 10 || this.ca % 20 != 10) break;
                            this.cC = 0;
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.aU[0].B = 0;
                            this.aU[0].C = 0;
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.aU[0].a = 0;
                            this.aU[0].e = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.cd == 10 && this.cb % 20 == 0) {
                                this.bE = 0;
                            }
                            if (this.aU[0].i != 10 || this.aU[0].g % 20 != 0) break;
                            this.aU[0].a = 2;
                            this.aU[0].e = 2;
                            ++this.eE;
                            break;
                        }
                        case 3: {
                            if (this.aU[0].h != 11 || this.aU[0].f % 20 != 10) break;
                            this.aU[0].a = 0;
                            this.aU[0].e = 4;
                            this.eT = 2;
                            var1_1 = true;
                            break;
                        }
                        case 5: {
                            if (this.aU[0].i == 8 && this.aU[0].g % 20 == 10) {
                                this.aU[0].a = 3;
                                this.aU[0].e = 3;
                                ++this.eE;
                                break;
                            }
                            this.aU[0].e = 0;
                            this.cC = 3;
                            this.bE = 1;
                            break;
                        }
                        case 6: {
                            if (this.aU[0].h == 12 && this.aU[0].f % 20 == 10) {
                                this.aU[0].a = 1;
                                this.aU[0].e = 4;
                            }
                            if (this.cc != 14 || this.ca % 20 != 10) break;
                            this.cC = 0;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            if (this.cd != 8 || this.cb % 20 != 10) break;
                            this.cC = 2;
                            this.aX[0].h = 1;
                            ++this.eE;
                            break;
                        }
                        case 8: {
                            if (this.cc == 13 && this.ca % 20 == 10) {
                                this.bE = 0;
                                this.cC = 1;
                            }
                            if (this.aX[0].b != 9 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].c = 3;
                            ++this.eE;
                            break;
                        }
                        case 9: {
                            if (this.aX[0].a != 13 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].h = 0;
                            this.aX[0].c = 0;
                            this.eT = 6;
                            var1_1 = true;
                            break;
                        }
                        case 11: {
                            ++this.eD;
                            this.a(this.eD, 0);
                            this.eE = 0;
                            this.bq = 0;
                            this.bs = 31;
                            this.aX[1].l = 1;
                        }
                    }
                    break;
                }
                case 5: {
                    switch (this.eE) {
                        case 0: {
                            if (this.aX[1].b == 11 && this.aX[1].e % 20 == 0) {
                                this.aX[1].c = 3;
                                this.aX[1].h = 0;
                                this.aX[0].h = 1;
                                this.aX[0].c = 3;
                                ++this.eE;
                            } else {
                                this.aX[1].h = 1;
                            }
                            this.eZ = 1;
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 1: {
                            if (this.aX[0].a == 14 && this.aX[0].d % 20 == 10) {
                                this.aX[0].c = 1;
                                this.aX[0].h = 1;
                                ++this.eE;
                            }
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            break;
                        }
                        case 2: {
                            if (this.aX[0].b == 11 && this.aX[0].e % 20 == 0) {
                                this.aX[0].h = 0;
                                this.aX[0].c = 2;
                                this.eT = 3;
                                var1_1 = true;
                            }
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            break;
                        }
                        case 4: {
                            if (this.aU[0].h == 11 && this.aU[0].f % 20 == 10) {
                                this.aU[0].a = 1;
                                this.aU[0].e = 1;
                                ++this.eE;
                            } else {
                                this.aU[0].e = 2;
                                this.aU[0].a = 2;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 5: {
                            if (this.aU[0].i == 10 && this.aU[0].g % 20 == 0) {
                                this.aU[0].e = 4;
                                this.aU[0].a = 3;
                                this.eT = 6;
                                var1_1 = true;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 7: {
                            if (this.aX[1].b == 13 && this.aX[1].e % 20 == 0) {
                                this.aX[1].h = 0;
                                this.aX[1].c = 2;
                            } else {
                                this.aX[1].c = 1;
                                this.aX[1].h = 1;
                            }
                            if (this.aU[0].i == 13 && this.aU[0].g % 20 == 0) {
                                this.aU[0].e = 4;
                                this.aU[0].a = 3;
                                this.eT = 1;
                                var1_1 = true;
                            } else {
                                this.aU[0].a = 1;
                                this.aU[0].e = 1;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 9: {
                            if (this.aU[0].i == 11 && this.aU[0].g % 20 == 0) {
                                this.aU[0].a = 3;
                                this.aU[0].e = 3;
                                ++this.eE;
                            } else {
                                this.aU[0].a = 0;
                                this.aU[0].e = 0;
                                this.aX[1].c = 1;
                                this.aX[1].h = 1;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 10: {
                            if (this.aU[0].h == 12 && this.aU[0].f % 20 == 10) {
                                this.aX[1].h = 0;
                                this.aX[1].l = 0;
                                this.aU[0].e = 4;
                                this.eT = 7;
                                var1_1 = true;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 12: {
                            if (this.aU[0].h == 13 && this.aU[0].f % 20 == 10) {
                                this.aU[0].e = 4;
                                this.eT = 4;
                                var1_1 = true;
                                break;
                            }
                            this.aU[0].a = 3;
                            this.aU[0].e = 3;
                            break;
                        }
                        case 14: {
                            this.eZ = 0;
                            if (this.cc == 14 && this.ca % 20 == 10) {
                                this.cC = 1;
                                ++this.eE;
                                break;
                            }
                            this.cC = 3;
                            this.bE = 1;
                            break;
                        }
                        case 15: {
                            if (this.cd != 9 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            this.aX[0].c = 0;
                            this.eT = 6;
                            var1_1 = true;
                            break;
                        }
                        case 17: {
                            this.aU[0].a = 3;
                            this.aX[0].c = 2;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 19: {
                            if (this.aU[0].i == 15 && this.aU[0].g % 20 == 0) {
                                this.aU[0].e = 4;
                                this.aU[0].ay = 0;
                                this.aU[0].ax = 0;
                                this.eT = 1;
                                var1_1 = true;
                                break;
                            }
                            this.aU[0].e = 1;
                            this.aU[0].a = 1;
                            break;
                        }
                        case 21: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 11: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fV = 0;
                            this.bX = 0;
                            this.bW = 0;
                            this.b(60, 13, 15, 0, 0);
                            this.aX[0].a = 3;
                            this.aX[0].b = 8;
                            this.aX[0].d = 60;
                            this.aX[0].e = 180;
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.aU[0].B = 0;
                            this.aU[0].C = 0;
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.eZ = 1;
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fV = 255;
                            this.fW = 0;
                            if (this.aU[0].i == 13 && this.aU[0].g % 20 == 0) {
                                this.aU[0].a = 2;
                                this.aU[0].e = 2;
                                ++this.eE;
                            } else {
                                this.aU[0].a = 0;
                                this.aU[0].e = 0;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 3: {
                            if (this.aU[0].h == 4 && this.aU[0].f % 20 == 10) {
                                this.aU[0].a = 0;
                                this.aU[0].e = 0;
                                ++this.eE;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 4: {
                            if (this.aU[0].i == 8 && this.aU[0].g % 20 == 0) {
                                this.aU[0].e = 4;
                                this.aU[0].a = 2;
                                var1_1 = true;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 6: {
                            this.fW = 1;
                            this.fX = 7;
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            this.aU[0].C = 0;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 8: {
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 9: {
                            this.aU[0].C = 0;
                            this.aU[0].B = 0;
                            this.fW = 0;
                            this.fV = 255;
                            this.aU[0].a = 1;
                            this.aU[0].e = 1;
                            ++this.eE;
                            break;
                        }
                        case 10: {
                            if (this.aU[0].i == 13 && this.aU[0].g % 20 == 0) {
                                this.aU[0].a = 3;
                                this.aU[0].e = 3;
                                this.fW = 1;
                                this.fX = 6;
                                this.fV = 255;
                                ++this.eE;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 11: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fV = 0;
                            this.bX = 1;
                            this.bW = 1;
                            this.b(1, 19, 14, 2, 0);
                            ++this.eE;
                            break;
                        }
                        case 12: {
                            this.eZ = 0;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.fV = 255;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 12: {
                    switch (this.eE) {
                        case 0: {
                            this.ca = this.cc * 20 + 10;
                            this.cb = (this.cd + 1) * 20;
                            this.cC = 0;
                            this.bE = 0;
                            this.aX[1].l = 1;
                            this.aX[1].c = 3;
                            this.aX[1].h = 1;
                            this.eZ = 1;
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.aX[1].a == 1 && this.aX[1].d % 20 == 10) {
                                this.aX[1].c = 1;
                                ++this.eE;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 2: {
                            if (this.aX[1].b == 17 && this.aX[1].e % 20 == 0) {
                                this.aX[1].c = 3;
                                ++this.eE;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 3: {
                            if (this.aX[1].a == 6 && this.aX[1].d % 20 == 10) {
                                this.aX[1].h = 0;
                                this.cC = 2;
                                this.eZ = 0;
                                this.eT = 4;
                                var1_1 = true;
                                break;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 5: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fV = 0;
                            this.b(60, 12, 15, 0, 0);
                            this.aX[0].a = 3;
                            this.aX[0].b = 8;
                            this.aX[0].d = 60;
                            this.aX[0].e = 180;
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            ++this.eE;
                            break;
                        }
                        case 6: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 8: {
                            if (this.cd == 13 && this.cb % 20 == 0) {
                                this.bE = 0;
                                this.eT = 1;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 10: 
                        case 11: 
                        case 12: 
                        case 13: 
                        case 14: {
                            this.cC = this.eE % 2 + 2;
                            ++this.eE;
                            break;
                        }
                        case 15: {
                            if (this.cc == 4 && this.ca % 20 == 10) {
                                this.bq = 0;
                                this.bs = 34;
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.cC = 2;
                            this.bE = 1;
                            break;
                        }
                        case 16: {
                            if (this.cd != 8 || this.cb % 20 != 0) break;
                            this.cC = 2;
                            this.bE = 0;
                            this.eT = 8;
                            var1_1 = true;
                            break;
                        }
                        case 18: {
                            this.eZ = 1;
                            this.fY = (this.aU[0].f - this.ca) / 5;
                            this.fZ = (this.aU[0].g - this.cb) / 5;
                            this.gc = 0;
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.aU[0].B = 0;
                            this.aU[0].C = 0;
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.aU[0].e = 4;
                            ++this.eE;
                            break;
                        }
                        case 19: {
                            if (this.gc > 4) {
                                this.gd = this.aU[0].f;
                                this.ge = this.aU[0].g;
                                this.aU[0].e = 0;
                                ++this.eE;
                            } else {
                                this.gd += this.fY;
                                this.ge += this.fZ;
                                ++this.gc;
                            }
                            this.fa = this.gd;
                            this.fb = this.ge;
                            break;
                        }
                        case 20: {
                            if (this.aU[0].i == 13 && this.aU[0].g % 20 == 0) {
                                this.aU[0].a = 2;
                                this.aU[0].e = 2;
                                ++this.eE;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 21: {
                            if (this.aU[0].h == 4 && this.aU[0].f % 20 == 10) {
                                this.aU[0].a = 0;
                                this.aU[0].e = 0;
                                ++this.eE;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 22: {
                            if (this.aU[0].i == 9 && this.aU[0].g % 20 == 0) {
                                this.eZ = 0;
                                this.aU[0].e = 4;
                                this.aU[0].a = 2;
                                this.eT = 5;
                                var1_1 = true;
                                break;
                            }
                            this.fa = this.aU[0].f;
                            this.fb = this.aU[0].g;
                            break;
                        }
                        case 24: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fW = 0;
                            this.eY = 1;
                            this.eX = 2;
                            this.b(52, 11, 9, 2, 0);
                            this.aX[0].l = 1;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 26: {
                            this.fW = 1;
                            this.fX = 6;
                            this.eY = 0;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 7;
                            var1_1 = true;
                            break;
                        }
                        case 28: {
                            if (this.aX[0].b == 12 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 3;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 1;
                            this.aX[0].h = 1;
                            break;
                        }
                        case 29: {
                            if (this.aX[0].a != 15 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].h = 0;
                            this.aX[0].l = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 31: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 17: 
                case 20: 
                case 21: 
                case 24: 
                case 28: 
                case 31: 
                case 35: 
                case 41: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cc == 9 && this.ca % 20 == 10) {
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            if (this.cc > 9) {
                                this.cC = 2;
                                break;
                            }
                            this.cC = 3;
                            break;
                        }
                        case 1: {
                            if (this.cd != 10 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            var1_1 = true;
                            break;
                        }
                        case 3: {
                            if (this.eD == 21) {
                                this.cm[4] = 1;
                            }
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 18: {
                    switch (this.eE) {
                        case 0: {
                            this.b(47, 12, 15, 0, 0);
                            this.aX[1].l = 1;
                            this.eZ = 1;
                            this.fY = (this.ca - this.aX[1].d) / 5;
                            this.fZ = (this.cb - this.aX[1].e) / 5;
                            this.gc = 0;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.gc > 4) {
                                this.gd = this.aX[1].d;
                                this.ge = this.aX[1].e;
                                this.eT = 12;
                                var1_1 = true;
                            } else {
                                this.gd -= this.fY;
                                this.ge -= this.fZ;
                                ++this.gc;
                            }
                            this.fa = this.gd;
                            this.fb = this.ge;
                            break;
                        }
                        case 3: {
                            if (this.aX[1].b == 13 && this.aX[1].e % 20 == 0) {
                                this.aX[1].c = 3;
                                ++this.eE;
                            } else {
                                this.aX[1].c = 1;
                                this.aX[1].h = 1;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 4: {
                            if (this.aX[1].a == 13 && this.aX[1].d % 20 == 10) {
                                this.aX[1].c = 1;
                                ++this.eE;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 5: {
                            if (this.aX[1].b == 15 && this.aX[1].e % 20 == 0) {
                                this.aX[1].h = 0;
                                ++this.eE;
                            }
                            this.fa = this.aX[1].d;
                            this.fb = this.aX[1].e;
                            break;
                        }
                        case 6: {
                            this.aX[1].c = 2;
                            this.cC = 3;
                            this.fV = 100;
                            this.fW = 1;
                            this.fX = 5;
                            ++this.eE;
                            break;
                        }
                        case 7: 
                        case 8: 
                        case 9: {
                            if (this.eE == 9) {
                                this.fW = 0;
                            } else {
                                this.fV = 100 + this.eE % 2 * 155;
                            }
                            ++this.eE;
                            break;
                        }
                        case 10: 
                        case 11: 
                        case 12: {
                            ++this.eE;
                            break;
                        }
                        case 13: {
                            this.aX[1].c = 1;
                            this.cC = 0;
                            this.fa = this.aX[0].d;
                            this.fb = this.aX[0].e;
                            ++this.eE;
                            break;
                        }
                        case 14: {
                            this.aX[1].l = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 16: {
                            this.eZ = 0;
                            if (this.cd == 12 && this.cb % 20 == 0) {
                                this.cC = 2;
                                ++this.eE;
                                break;
                            }
                            this.cC = 0;
                            this.bE = 1;
                            break;
                        }
                        case 17: {
                            if (this.cc != 8 || this.ca % 20 != 10) break;
                            this.cC = 0;
                            ++this.eE;
                            break;
                        }
                        case 18: {
                            if (this.cd != 8 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            this.eT = 9;
                            var1_1 = true;
                            break;
                        }
                        case 20: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 22: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fV = 0;
                            this.a(0, 0, 16, 5, 0);
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 23: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fV = 0;
                            this.a(0, 0, 16, 5, 0);
                            this.cF = 0;
                            this.cT = 0;
                            this.cU = 0;
                            var3_3 = 0;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ax = 0;
                                this.aU[var3_3].ay = 0;
                                ++var3_3;
                            }
                            this.aU[11].ax = 1;
                            this.aU[11].ay = 1;
                            this.aU[11].j = 11;
                            this.aU[11].k = 11;
                            this.a(2, 11, this.aU[11].aJ[0], this.aU[11].aJ[1], 1);
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            var2_2 = true;
                            this.aU[11].ax = 0;
                            this.aU[11].ay = 0;
                        }
                    }
                    break;
                }
                case 29: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 1 && this.cb % 20 == 0) {
                                this.bE = 0;
                                this.cC = 3;
                            } else {
                                this.bE = 1;
                            }
                            if (this.aX[3].b == 1 && this.aX[3].e % 20 == 0) {
                                this.aX[3].h = 0;
                                this.aX[3].c = 2;
                                var1_1 = true;
                                break;
                            }
                            this.aX[3].l = 1;
                            this.aX[3].c = 0;
                            this.aX[3].h = 1;
                            break;
                        }
                        case 2: {
                            if (this.aX[3].a == 12 && this.aX[3].d % 20 == 10) {
                                this.aX[3].c = 0;
                                ++this.eE;
                                break;
                            }
                            this.aX[3].h = 1;
                            break;
                        }
                        case 3: {
                            if (this.aX[3].b != 0 || this.aX[3].e % 20 != 10) break;
                            this.aX[3].l = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 32: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cc == 11 && this.ca % 20 == 10) {
                                this.cC = 1;
                                ++this.eE;
                                break;
                            }
                            if (this.ca < 230) {
                                this.cC = 3;
                            } else if (this.ca > 230) {
                                this.cC = 2;
                            }
                            this.bE = 1;
                            this.aX[0].l = 1;
                            break;
                        }
                        case 1: {
                            if (this.cd == 5 && this.cb % 20 == 0) {
                                this.cC = 3;
                                this.bE = 0;
                            }
                            if (this.aX[0].b == 5 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 2;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 0;
                            this.aX[0].h = 1;
                            break;
                        }
                        case 2: {
                            if (this.aX[0].a != 13 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].h = 0;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            if (this.aX[0].b == 3 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 3;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 0;
                            this.aX[0].h = 1;
                            break;
                        }
                        case 5: {
                            if (this.aX[0].a != 19 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].h = 0;
                            this.aX[0].l = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 33: {
                    switch (this.eE) {
                        case 0: {
                            if (this.aX[0].b == 11 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 2;
                            } else {
                                this.aX[0].l = 1;
                                this.aX[0].c = 1;
                                this.aX[0].h = 1;
                            }
                            if (this.cd == 11 && this.cb % 20 == 0) {
                                this.cC = 3;
                                ++this.eE;
                                break;
                            }
                            this.cC = 0;
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            if (this.aX[0].a == 22 && this.aX[0].d % 20 == 10) {
                                this.aX[0].h = 0;
                            }
                            if (this.cc != 21 || this.ca % 20 != 10) break;
                            this.fV = 255;
                            this.bE = 0;
                            this.eT = 6;
                            var1_1 = true;
                            break;
                        }
                        case 3: {
                            if (this.cd == 9 && this.cb % 20 == 0) {
                                this.bE = 0;
                                this.fW = 1;
                                this.fX = 6;
                                if (this.fV > 0) {
                                    this.fV -= 20;
                                    break;
                                }
                                this.b(30, 10, 19, 0, 0);
                                this.aU[0].j = 11;
                                this.aU[0].k = 11;
                                ++this.eE;
                                break;
                            }
                            this.cC = 0;
                            this.bE = 1;
                            break;
                        }
                        case 4: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            if (this.cd == 12 && this.cb % 20 == 0) {
                                this.eT = 9;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 6: {
                            this.aU[0].i = 13;
                            this.aU[0].g = 280;
                            this.aU[0].a = 0;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            this.fW = 1;
                            this.fX = 7;
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 8: {
                            this.aU[0].C = 0;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 9: {
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 10: {
                            this.aU[0].C = 0;
                            this.aU[0].B = 0;
                            this.fW = 0;
                            this.fV = 255;
                            this.eT = 3;
                            var1_1 = true;
                            break;
                        }
                        case 12: {
                            this.aU[0].j = 1;
                            this.aU[0].k = 1;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 34: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            var3_3 = 0;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ax = 0;
                                this.aU[var3_3].ay = 0;
                                ++var3_3;
                            }
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 1);
                            this.aU[0].ax = 1;
                            this.aU[0].ay = 1;
                            this.a(0, 0, 10, 12, 0);
                            this.bE = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 9;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            this.dT = 1;
                            this.dU = 0;
                            this.eT = 2;
                            var1_1 = true;
                            break;
                        }
                        case 6: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fW = 0;
                            this.eY = 1;
                            this.eX = 2;
                            this.b(29, 25, 11, 1, 0);
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 8: {
                            this.eY = 0;
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 36: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 8 && this.cb % 20 == 0) {
                                this.bE = 0;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 2: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 38: 
                case 40: 
                case 43: 
                case 69: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cc == 8 && this.ca % 20 == 10) {
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            this.cC = 3;
                            break;
                        }
                        case 1: {
                            if (this.cd != 6 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            var1_1 = true;
                            break;
                        }
                        case 3: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 44: {
                    switch (this.eE) {
                        case 0: {
                            this.eT = 2;
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            if (this.cd == 17 && this.cb % 20 == 0) {
                                this.cC = 2;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            this.cC = 1;
                            break;
                        }
                        case 3: {
                            if ((this.cc != 10 || this.ca % 20 != 10) && this.cc >= 10 && (this.cc != 10 || this.ca % 20 != 0)) break;
                            this.cc = 10;
                            this.cd = 17;
                            this.ca = 210;
                            this.cb = 360;
                            this.cC = 0;
                            this.bE = 0;
                            this.eT = 25;
                            var1_1 = true;
                            break;
                        }
                        case 5: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 46: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.a(0, 0, 17, 15, 0);
                            this.bE = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.aH[1][14 * this.bC[0] + 16] = 0;
                            v0 = this.aH[3];
                            v1 = 14 * this.bC[0] + 16;
                            v0[v1] = v0[v1] - 9;
                            this.aH[1][14 * this.bC[0] + 17] = 0;
                            v2 = this.aH[3];
                            v3 = 14 * this.bC[0] + 17;
                            v2[v3] = v2[v3] - 9;
                            this.bE = 1;
                            ++this.eE;
                            break;
                        }
                        case 3: {
                            if (this.cd != 11 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            this.cC = 2;
                            this.aX[0].c = 3;
                            var1_1 = true;
                            break;
                        }
                        case 5: {
                            if (this.aX[0].b == 18 && this.aX[0].e % 20 == 0) {
                                this.aX[0].c = 2;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 1;
                            this.aX[0].h = 1;
                            this.cC = 1;
                            break;
                        }
                        case 6: {
                            if (this.aX[0].a != 10 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].c = 1;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            if (this.aX[0].b != 20 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].l = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 48: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 17 && this.cb % 20 == 0) {
                                this.bE = 0;
                                this.cC = 3;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 2: {
                            if (this.aX[1].b == 19 && this.aX[1].e % 20 == 0) {
                                this.aX[1].h = 0;
                                this.aX[1].l = 0;
                                var2_2 = true;
                                break;
                            }
                            this.aX[1].c = 1;
                            this.aX[1].h = 1;
                        }
                    }
                    break;
                }
                case 51: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 12 && this.cb % 20 == 0) {
                                this.bE = 0;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 2: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 52: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            var3_3 = 0;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ax = 0;
                                this.aU[var3_3].ay = 0;
                                ++var3_3;
                            }
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 1);
                            this.aU[0].ax = 1;
                            this.aU[0].ay = 1;
                            this.a(0, 0, 11, 9, 2);
                            this.bE = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 6;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.b(52, 11, 9, 2, 0);
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 2;
                            var1_1 = true;
                            break;
                        }
                        case 7: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 55: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.bX = 0;
                            this.bW = 0;
                            this.eZ = 1;
                            this.b(99, 12, 19, 0, 0);
                            this.aX[5].l = 1;
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.aU[0].j = 11;
                            this.aU[0].k = 11;
                            this.fW = 0;
                            this.eY = 1;
                            this.eX = 2;
                            this.eS = 2;
                            this.gc = 255;
                            ++this.eE;
                            this.fa = this.aX[5].d;
                            this.fb = this.aX[5].e;
                            break;
                        }
                        case 2: {
                            if (this.gc > 0) {
                                this.gc -= 20;
                            }
                            if (this.gc >= 0) break;
                            this.gc = 0;
                            ++this.eE;
                            break;
                        }
                        case 3: 
                        case 4: 
                        case 5: 
                        case 6: {
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            if (this.gc < 255) {
                                this.gc += 20;
                            }
                            if (this.gc <= 255) break;
                            this.eS = 0;
                            this.gc = 255;
                            this.eY = 1;
                            this.eX = 2;
                            ++this.eR;
                            ++this.eQ;
                            ++this.eE;
                            break;
                        }
                        case 8: {
                            this.eY = 0;
                            this.fW = 1;
                            this.fX = 6;
                            if (this.fV < 255) {
                                this.fV += 20;
                            } else {
                                this.fW = 0;
                                this.aX[5].h = 1;
                                this.aX[5].c = 0;
                                ++this.eE;
                            }
                            this.fa = this.aX[5].d;
                            this.fb = this.aX[5].e;
                            break;
                        }
                        case 9: {
                            if (this.aX[5].b == 14 && this.aX[5].e % 20 == 0) {
                                this.aX[5].c = 1;
                                this.aX[5].h = 0;
                                ++this.eE;
                            }
                            this.fa = this.aX[5].d;
                            this.fb = this.aX[5].e;
                            break;
                        }
                        case 10: 
                        case 11: 
                        case 12: 
                        case 13: {
                            ++this.eE;
                            break;
                        }
                        case 14: {
                            this.fV = 100;
                            this.fW = 1;
                            this.fX = 5;
                            ++this.eE;
                            break;
                        }
                        case 15: 
                        case 16: 
                        case 17: 
                        case 18: {
                            if (this.eE == 18) {
                                this.fW = 0;
                                this.aX[5].l = 0;
                                this.aX[4].l = 1;
                                this.eT = 5;
                                var1_1 = true;
                                break;
                            }
                            this.fV = 100 + this.eE % 2 * 155;
                            ++this.eE;
                            break;
                        }
                        case 20: {
                            this.fW = 1;
                            this.fX = 7;
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 21: {
                            this.aU[0].C = 0;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 22: {
                            this.fV = 100;
                            this.aU[0].B = 1;
                            this.aU[0].C = 1;
                            ++this.eE;
                            break;
                        }
                        case 23: {
                            this.br = 1;
                            this.bp = 13;
                            this.aX[4].l = 0;
                            this.aU[0].C = 0;
                            this.aU[0].B = 0;
                            this.fW = 0;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 24: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 25: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.bX = 1;
                            this.bW = 1;
                            this.eZ = 0;
                            this.b(107, 3, 9, 1, 0);
                            ++this.eE;
                            break;
                        }
                        case 26: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 63: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 11 && this.cb % 20 == 0) {
                                this.bE = 0;
                                this.eT = 9;
                                var1_1 = true;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 2: {
                            if (this.cc == 8 && this.ca % 20 == 10) {
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.cC = 3;
                            this.bE = 1;
                            break;
                        }
                        case 3: {
                            if (this.cd != 7 || this.cb % 20 != 10) break;
                            this.cC = 3;
                            ++this.eE;
                            break;
                        }
                        case 4: {
                            if (this.cc != 9 || this.ca % 20 != 10) break;
                            this.bE = 0;
                            this.cC = 1;
                            this.eZ = 1;
                            this.gc = 0;
                            this.fZ = (this.aX[2].e - this.cb) / 5;
                            this.aX[1].l = 1;
                            this.aX[2].l = 1;
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            if (this.gc > 4) {
                                this.fa = this.aX[2].d;
                                this.fb = this.aX[2].e;
                                this.aX[1].h = 1;
                                this.aX[2].h = 1;
                                ++this.eE;
                                break;
                            }
                            this.fa -= 0;
                            this.fb += this.fZ;
                            ++this.gc;
                            break;
                        }
                        case 6: {
                            if (this.aX[1].b == 10 && this.aX[1].e % 20 == 0) {
                                this.aX[1].h = 0;
                            }
                            if (this.aX[2].b == 9 && this.aX[2].e % 20 == 0) {
                                this.aX[2].h = 0;
                                this.aX[2].c = 2;
                                this.eT = 5;
                                var1_1 = true;
                            }
                            this.fa = this.aX[2].d;
                            this.fb = this.aX[2].e;
                            break;
                        }
                        case 8: {
                            this.eZ = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 10: {
                            if (this.cc == 8 && this.ca % 20 == 10) {
                                this.cC = 1;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            this.cC = 2;
                            break;
                        }
                        case 11: {
                            if (this.cd != 8 || this.cb % 20 != 0) break;
                            this.aX[2].c = 0;
                            this.bE = 0;
                            this.eT = 6;
                            var1_1 = true;
                            break;
                        }
                        case 13: {
                            this.fV = 100;
                            this.fW = 1;
                            this.fX = 5;
                            ++this.eE;
                            break;
                        }
                        case 14: 
                        case 15: 
                        case 16: {
                            if (this.eE == 16) {
                                this.fW = 0;
                                this.aX[0].l = 0;
                                this.aX[1].l = 0;
                                this.aX[2].l = 0;
                                this.eT = 1;
                                var1_1 = true;
                                break;
                            }
                            this.fV = 100 + this.eE % 2 * 155;
                            ++this.eE;
                            break;
                        }
                        case 18: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 67: 
                case 82: 
                case 85: 
                case 87: 
                case 89: {
                    switch (this.eE) {
                        case 0: {
                            if (this.eD == 85) {
                                this.aX[0].l = 1;
                                this.aX[0].c = 2;
                                this.cC = 3;
                            }
                            this.a(2, 0, this.cc, this.cd - 2, 1);
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 3: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 1);
                            if (this.eD == 85) {
                                this.aX[0].l = 0;
                            }
                            ++this.eE;
                            break;
                        }
                        case 4: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 68: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            var3_3 = 1;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ay = 0;
                                this.aU[var3_3].ax = 0;
                                ++var3_3;
                            }
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 2);
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.a(0, 0, this.aU[0].h - 1, this.aU[0].i, 3);
                            this.aH[1][4 * this.bC[0] + 3] = 0;
                            v4 = this.aH[2];
                            v5 = 4 * this.bC[0] + 3;
                            v4[v5] = v4[v5] - 10;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            this.aX[0].l = 0;
                            this.bE = 1;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 71: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cd == 17 && this.cb % 20 == 0) {
                                this.cC = 3;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            if (this.cc != 11 || this.ca % 20 != 10) break;
                            this.cC = 2;
                            this.bE = 0;
                            this.eT = 3;
                            var1_1 = true;
                            break;
                        }
                        case 3: {
                            if (this.aX[10].b == 17 && this.aX[10].e % 20 == 0) {
                                this.aX[10].h = 0;
                                this.eT = 3;
                                var1_1 = true;
                                break;
                            }
                            this.aX[10].l = 1;
                            this.aX[10].h = 1;
                            break;
                        }
                        case 5: {
                            this.fW = 1;
                            this.fX = 8;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 6: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fX = 6;
                            this.b(91, 7, 9, 1, 0);
                            this.ca = this.cc * 20;
                            this.a(1, 0, 8, 9, 2);
                            this.aX[0].l = 1;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 17;
                            var1_1 = true;
                            break;
                        }
                        case 9: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 76: {
                    switch (this.eE) {
                        case 0: {
                            this.eT = 11;
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            if (this.cc == 5 && this.ca % 20 == 10) {
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.cC = 3;
                            this.bE = 1;
                            break;
                        }
                        case 3: {
                            if (this.cd != 9 || this.cb % 20 != 0) break;
                            this.cC = 3;
                            this.aX[0].c = 3;
                            this.aX[0].h = 1;
                            ++this.eE;
                            break;
                        }
                        case 4: {
                            if (this.cc != 7 || this.ca % 20 != 0) break;
                            this.bE = 0;
                            this.cC = 1;
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            if (this.aX[0].a != 5 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].c = 1;
                            ++this.eE;
                            break;
                        }
                        case 6: {
                            if (this.aX[0].b != 11 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].c = 3;
                            ++this.eE;
                            break;
                        }
                        case 7: {
                            if (this.aX[0].a != 8 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].c = 0;
                            ++this.eE;
                            break;
                        }
                        case 8: {
                            if (this.aX[0].b != 9 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].h = 0;
                            this.aX[0].c = 2;
                            this.eT = 3;
                            var1_1 = true;
                            break;
                        }
                        case 10: {
                            if (this.aX[0].a == 7 && this.aX[0].d % 20 == 10) {
                                this.aX[0].h = 0;
                                this.eT = 3;
                                var1_1 = true;
                                break;
                            }
                            this.aX[0].h = 1;
                            this.cc = 6;
                            this.ca = 130;
                            break;
                        }
                        case 12: {
                            this.fW = 1;
                            this.fX = 9;
                            this.fV = 150;
                            ++this.eE;
                            break;
                        }
                        case 13: 
                        case 14: 
                        case 15: {
                            ++this.eE;
                            break;
                        }
                        case 16: {
                            this.eT = 9;
                            var1_1 = true;
                            break;
                        }
                        case 18: {
                            if (this.aX[0].a == 8 && this.aX[0].d % 20 == 10) {
                                this.aX[0].c = 1;
                                ++this.eE;
                                break;
                            }
                            this.aX[0].c = 3;
                            this.aX[0].h = 1;
                            break;
                        }
                        case 19: {
                            if (this.aX[0].b != 11 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].c = 2;
                            ++this.eE;
                            break;
                        }
                        case 20: {
                            if (this.aX[0].a != 4 || this.aX[0].d % 20 != 10) break;
                            this.aX[0].c = 1;
                            ++this.eE;
                            break;
                        }
                        case 21: {
                            if (this.aX[0].b != 15 || this.aX[0].e % 20 != 0) break;
                            this.aX[0].h = 0;
                            this.aX[0].l = 0;
                            ++this.eE;
                            break;
                        }
                        case 22: {
                            if (this.fV < 255) {
                                this.fV += 10;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 24: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 77: {
                    switch (this.eE) {
                        case 0: {
                            this.eT = 10;
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            this.bm[0].a = 7;
                            this.bm[0].b = 4;
                            this.bm[0].c = 1;
                            this.bm[0].d = 20;
                            this.bm[0].e = 5;
                            this.bm[0].g = 8;
                            this.bm[0].f = 1;
                            ++this.eE;
                            break;
                        }
                        case 3: {
                            this.eT = 5;
                            var1_1 = true;
                            break;
                        }
                        case 5: {
                            this.fV = 100;
                            this.fW = 1;
                            this.fX = 5;
                            ++this.eE;
                            break;
                        }
                        case 6: 
                        case 7: 
                        case 8: 
                        case 9: {
                            if (this.eE == 9) {
                                this.fW = 0;
                                this.eT = 4;
                                var1_1 = true;
                                break;
                            }
                            this.fV = 100 + this.eE % 2 * 155;
                            ++this.eE;
                            break;
                        }
                        case 11: {
                            if (this.ce[0] == 70) {
                                this.ce[1] = this.ch;
                            }
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 83: 
                case 86: 
                case 88: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            var3_3 = 1;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ay = 0;
                                this.aU[var3_3].ax = 0;
                                ++var3_3;
                            }
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 1);
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.a(0, 0, this.aU[0].h, this.aU[0].i + 2, 0);
                            this.cF = 0;
                            this.cT = 0;
                            this.cU = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            if (this.eD == 83) {
                                this.eT = 4;
                            }
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            if (this.eD == 83) {
                                this.bE = 1;
                                ++this.eE;
                                break;
                            }
                            var2_2 = true;
                            break;
                        }
                        case 5: {
                            if (this.cd != 13 || this.cb % 20 != 0) break;
                            this.bE = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 7: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 90: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            var3_3 = 1;
                            while (var3_3 < this.aT) {
                                this.aU[var3_3].ay = 0;
                                this.aU[var3_3].ax = 0;
                                ++var3_3;
                            }
                            this.a(2, 0, this.aU[0].aJ[0], this.aU[0].aJ[1], 1);
                            this.aU[0].ay = 1;
                            this.aU[0].ax = 1;
                            this.a(0, 0, this.aU[0].h, this.aU[0].i + 2, 0);
                            this.cF = 0;
                            this.cT = 0;
                            this.cU = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 3;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.aX[0].l = 1;
                            if (this.aX[0].a == 9 && this.aX[0].d % 20 == 10) {
                                this.aX[0].h = 0;
                                this.eT = 8;
                                var1_1 = true;
                                break;
                            }
                            this.aX[0].h = 1;
                            break;
                        }
                        case 6: 
                        case 7: 
                        case 8: 
                        case 9: {
                            this.fW = 1;
                            this.fX = 7;
                            this.fV = this.eE % 2 == 0 ? 150 : 255;
                            if (this.eE == 9) {
                                this.aX[0].l = 0;
                                this.eT = 3;
                                this.fW = 0;
                                var1_1 = true;
                                break;
                            }
                            ++this.eE;
                            break;
                        }
                        case 11: {
                            this.aU[0].ay = 0;
                            this.aU[0].ax = 0;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 13: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 91: {
                    switch (this.eE) {
                        case 0: {
                            if (this.cc == 4 && this.ca % 20 == 10) {
                                this.cC = 0;
                                ++this.eE;
                                break;
                            }
                            this.bE = 1;
                            break;
                        }
                        case 1: {
                            if (this.cd != 9 || this.cb % 20 != 0) break;
                            this.cC = 3;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.cc != 6 || this.ca % 20 != 0) break;
                            this.bE = 0;
                            this.cC = 1;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.b(52, 10, 12, 0, 0);
                            this.aX[1].l = 1;
                            ++this.eE;
                            break;
                        }
                        case 6: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 8;
                            var1_1 = true;
                            break;
                        }
                        case 8: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 92: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 1: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.a(1, 0, 10, 10, 1);
                            this.aX[0].l = 1;
                            this.aX[1].l = 0;
                            ++this.eE;
                            break;
                        }
                        case 2: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 7;
                            var1_1 = true;
                            break;
                        }
                        case 4: {
                            this.fW = 1;
                            this.fX = 6;
                            this.fV = 255;
                            ++this.eE;
                            break;
                        }
                        case 5: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.a(1, 2, 10, 10, 1);
                            this.aX[2].l = 1;
                            this.aX[0].l = 0;
                            ++this.eE;
                            break;
                        }
                        case 6: {
                            if (this.fV < 255) {
                                this.fV += 20;
                                break;
                            }
                            this.fW = 0;
                            this.eT = 5;
                            var1_1 = true;
                            break;
                        }
                        case 8: {
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 93: {
                    switch (this.eE) {
                        case 0: {
                            this.eT = 8;
                            var1_1 = true;
                            break;
                        }
                        case 2: {
                            this.aX[0].l = 0;
                            ++this.eE;
                            break;
                        }
                        case 3: {
                            this.fT = 260;
                            this.fU = 20;
                            this.fW = 1;
                            this.fX = 0;
                            this.fV = 3;
                            this.fY = 40;
                            this.fZ = 24;
                            this.gc = 0;
                            this.eZ = 1;
                            this.fa = this.fT;
                            this.fb = this.fU;
                            ++this.eE;
                            break;
                        }
                        case 4: {
                            if (this.gc > 5) {
                                this.fW = 1;
                                this.fX = 6;
                                this.fV = 255;
                                this.eZ = 0;
                                ++this.eE;
                                break;
                            }
                            this.ga = 5 + this.gc * 8;
                            ++this.gc;
                            this.fX = 1;
                            this.fT -= this.fY;
                            this.fU += this.fZ;
                            this.fa = this.fT;
                            this.fb = this.fU;
                            break;
                        }
                        case 5: {
                            if (this.fV > 0) {
                                this.fV -= 20;
                                break;
                            }
                            this.fW = 0;
                            this.eY = 1;
                            this.eX = 2;
                            this.eT = 1;
                            var1_1 = true;
                            break;
                        }
                        case 7: {
                            this.fW = 1;
                            this.eY = 0;
                            var2_2 = true;
                        }
                    }
                    break;
                }
                case 94: {
                    switch (this.eE) {
                        case 0: {
                            this.fW = 0;
                            this.eY = 1;
                            this.eX = 2;
                            this.eS = 2;
                            this.gc = 255;
                            this.eu = 2;
                            this.ev = 0;
                            ++this.eE;
                            break block0;
                        }
                        case 1: {
                            this.p(0);
                            break block0;
                        }
                        case 2: {
                            this.fW = 1;
                            this.eS = 0;
                            this.eY = 0;
                            this.b(93, 4, 15, 0, 0);
                            this.aX[0].l = 0;
                            this.aX[1].l = 1;
                            ++this.eE;
                            break block0;
                        }
                        case 3: {
                            if (this.fV < 255) {
                                this.fV += 20;
                            } else {
                                this.bE = 1;
                                ++this.eE;
                            }
                            this.fW = 0;
                            break block0;
                        }
                        case 4: {
                            if (this.cd == 11 && this.cb % 20 == 0) {
                                this.cC = 3;
                                ++this.eE;
                                break block0;
                            }
                            break block565;
                        }
                        case 5: {
                            if (this.cc == 6 && this.ca % 20 == 10) {
                                this.bE = 0;
                                if (this.fV > 0) {
                                    this.fV -= 20;
                                    break block0;
                                }
                                this.fV = 0;
                                this.aW();
                                this.ap = 9;
                                var2_2 = true;
                            } else {
                                ** GOTO lbl2277
                            }
                        }
                    }
                }
            }
        }
        if (var1_1) {
            this.ae();
        }
        this.cD = this.cC;
        if (this.fX != 2 && this.fX != 4) {
            if (this.fV < 0) {
                this.fV = 0;
            }
            if (this.fV > 255) {
                this.fV = 255;
            }
        }
        if (var2_2) {
            if (this.eF[11] == 2 || this.eF[11] == 3) {
                if (this.eJ[0][2] == 3 && this.eJ[0][3] < 11) {
                    this.b(this.eJ[0][3], this.eJ[0][4], this.eJ[0][5], 1);
                }
                if (this.eJ[0][9] == 13) {
                    this.ce[1] = this.ce[0] == 70 && this.eD < 77 && this.ce[1] + this.eJ[0][10] * this.eJ[0][11] >= this.ch ? this.ch - 1 : this.ce[1] + this.eJ[0][10] * this.eJ[0][11];
                } else if (this.eJ[0][9] == 14) {
                    this.ce[11] = this.ce[11] + this.eJ[0][10] * this.eJ[0][11];
                }
                ++this.eG;
                this.d(0, 0, this.eG);
            }
            this.bu = 23;
            this.bD = 1;
            this.eC = 0;
            this.eX = 0;
            this.eY = 0;
            ++this.eD;
            this.o(0);
            this.am();
            this.au();
            this.aW();
            this.bq = 0;
        }
    }

    public final void p(int n2) {
        if (this.eu > 0) {
            if (this.ev == 0) {
                if (this.gc > 0) {
                    this.gc -= 20;
                }
                if (this.gc < 0) {
                    this.gc = 0;
                    ++this.ev;
                }
            } else if (this.ev < 4) {
                ++this.ev;
            } else if (this.ev == 4) {
                if (this.gc < 255) {
                    this.gc += 20;
                }
                if (this.gc > 255) {
                    ++this.eR;
                    ++this.eQ;
                    this.gc = 255;
                    this.ev = 0;
                    --this.eu;
                    if (this.eu == 0) {
                        if (n2 == 1) {
                            ++this.eE;
                        } else {
                            this.eR = 2;
                            this.eQ = 2;
                            this.eT = this.eT * 2 + 1;
                            this.b(1);
                        }
                    }
                }
            }
        }
        if (this.eu == 0) {
            this.eS = 3;
        }
    }

    public final void ad() {
        boolean bl = false;
        boolean bl2 = false;
        int n2 = 0;
        int n3 = 0;
        this.bu = 24;
        block0 : switch (this.fl) {
            case 0: {
                switch (this.eE) {
                    case 0: {
                        if (this.aX[0].a == 20 && this.aX[0].d % 20 == 10) {
                            this.fT = this.aX[0].d;
                            this.fU = this.aI[1] + 15;
                            this.fV = 100;
                            this.fW = 1;
                            this.fX = 5;
                            this.aW();
                            ++this.eE;
                        } else {
                            this.aX[0].h = 1;
                            this.aX[1].h = 1;
                            this.aX[2].h = 1;
                            this.gd = this.aX[0].d;
                            this.ge = this.aX[0].e - 80;
                            this.aJ[0] = 0;
                            this.aJ[1] = this.ei % 2 * 5;
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 1: 
                    case 2: 
                    case 3: {
                        if (this.eE == 3) {
                            this.bq = 0;
                            this.bs = 33;
                            n2 = 0;
                            while (n2 < 3) {
                                this.aX[n2].h = 0;
                                this.aX[n2].l = 0;
                                this.aX[n2 + 3].a = this.aX[n2].a;
                                this.aX[n2 + 3].b = this.aX[n2].b;
                                this.aX[n2 + 3].d = this.aX[n2].d;
                                this.aX[n2 + 3].e = this.aX[n2].e;
                                this.aX[n2 + 3].l = 1;
                                ++n2;
                            }
                            this.aJ[0] = 0;
                            this.aJ[1] = 0;
                            this.fX = 0;
                            this.fV = 3;
                            this.fY = (this.bx[0] - 20) / 5;
                            this.fZ = (200 - (this.aI[1] + 15)) / 5;
                            this.gb = this.gd - this.by[0];
                            this.gc = 0;
                            this.eT = 3;
                            bl = true;
                        } else {
                            this.fV = 100 + this.eE % 2 * 155;
                            ++this.eE;
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 5: {
                        if (this.gc > 5) {
                            this.dT = 1;
                            this.dU = 0;
                            this.r(70, 1000);
                            this.fX = 2;
                            ++this.eE;
                        } else {
                            this.ga = 5 + this.gc * 8;
                            ++this.gc;
                            this.fX = 1;
                            this.gd = this.fT -= this.fY;
                            this.fU += this.fZ;
                            if (this.gd < this.gb - 20) {
                                this.gd = this.gb - 20;
                            }
                            n2 = 0;
                            while (n2 < 3) {
                                this.aX[n2].h = 1;
                                this.aX[n2].l = 1;
                                this.aX[n2 + 3].l = 0;
                                ++n2;
                            }
                        }
                        if (this.gc == 1) {
                            this.aW();
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 6: {
                        n3 = this.bx[0] > this.bx[1] ? this.bx[0] : this.bx[1];
                        if (this.fV < n3 + 100) {
                            this.fV = this.fV * 3 < 300 ? (this.fV *= 3) : 450;
                        } else {
                            this.fV = 0;
                            this.fX = 4;
                            ++this.eE;
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 7: {
                        this.b(79, 5, 9, 2, 1);
                        this.bq = 0;
                        this.bs = 32;
                        this.fV = 0;
                        ++this.eE;
                        break;
                    }
                    case 8: {
                        this.fX = 5;
                        if (this.fV < 255) {
                            this.fV += 20;
                            break;
                        }
                        this.fW = 0;
                        ++this.eE;
                        break;
                    }
                    case 9: {
                        this.eT = 3;
                        bl = true;
                        break;
                    }
                    case 11: {
                        this.aW();
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.fV = 0;
                        this.b(58, 19, 14, 2, 1);
                        n2 = 0;
                        while (n2 < 3) {
                            this.aX[6 + n2].d += 100;
                            this.aX[6 + n2].a += 5;
                            this.aX[6 + n2].h = 1;
                            ++n2;
                        }
                        this.fa = this.gd = this.aX[6].d;
                        this.fb = this.ge = this.aX[6].e;
                        ++this.eE;
                        break;
                    }
                    case 12: {
                        if (this.fV < 255) {
                            this.fV += 20;
                        } else {
                            this.fW = 0;
                            ++this.eE;
                        }
                        this.fa = this.gd = this.aX[6].d;
                        this.fb = this.ge = this.aX[6].e;
                        break;
                    }
                    case 13: {
                        if (this.aX[6].a == 11 && this.aX[6].d % 20 == 10) {
                            this.bq = 0;
                            this.bs = 31;
                            n2 = 0;
                            while (n2 < 3) {
                                this.aX[3 + n2].l = 1;
                                this.aX[6 + n2].h = 0;
                                this.aX[6 + n2].l = 0;
                                ++n2;
                            }
                            this.eT = 12;
                            bl = true;
                        }
                        this.fa = this.gd = this.aX[6].d;
                        this.fb = this.ge = this.aX[6].e;
                        break;
                    }
                    case 15: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.fV = 0;
                        ++this.fl;
                        this.b(79, 4, 20, 2, 2);
                        n2 = 0;
                        while (n2 < 2) {
                            this.aU[n2].j = 11;
                            this.aU[n2].k = 11;
                            this.aU[n2].e = 4;
                            ++n2;
                        }
                        break;
                    }
                }
                break;
            }
            case 1: {
                switch (this.eE) {
                    case 0: {
                        if (this.fV < 255) {
                            this.fV += 20;
                            break;
                        }
                        this.fW = 0;
                        this.dT = 1;
                        this.dU = 0;
                        this.r(70, 1000);
                        this.eT = 2;
                        bl = true;
                        this.fY = (this.aU[0].f - this.aX[0].d) / 5;
                        this.fZ = (this.aU[0].g - this.aX[0].e) / 5;
                        this.gc = 0;
                        break;
                    }
                    case 2: 
                    case 6: 
                    case 10: {
                        if (this.gc > 4) {
                            this.gd = this.aX[0].d;
                            this.ge = this.aX[0].e;
                            this.eT = this.eE == 2 ? 2 : 4;
                            bl = true;
                            this.fY = (this.aU[0].f - this.aX[0].d) / 5;
                            this.fZ = (this.aU[0].g - this.aX[0].e) / 5;
                            this.gc = 0;
                            break;
                        }
                        this.gd -= this.fY;
                        this.ge -= this.fZ;
                        ++this.gc;
                        break;
                    }
                    case 4: 
                    case 8: 
                    case 12: {
                        if (this.gc > 4) {
                            this.gd = this.aU[0].f;
                            this.ge = this.aU[0].g;
                            if (this.eE == 8) {
                                this.eT = 1;
                            } else if (this.eE == 4) {
                                this.eT = 1;
                            } else {
                                this.eT = 1;
                                this.dT = 1;
                                this.dU = 0;
                                this.r(70, 1000);
                            }
                            bl = true;
                            this.fY = (this.aU[0].f - this.aX[0].d) / 5;
                            this.fZ = (this.aU[0].g - this.aX[0].e) / 5;
                            this.gc = 0;
                            break;
                        }
                        this.gd += this.fY;
                        this.ge += this.fZ;
                        ++this.gc;
                        break;
                    }
                    case 14: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.aX[0].l = 0;
                        this.aX[3].l = 1;
                        this.aX[2].a = 14;
                        this.aX[2].b = 7;
                        this.aX[2].d = 290;
                        this.aX[2].e = 160;
                        this.gd = this.aX[3].d;
                        this.ge = this.aX[3].e;
                        ++this.eE;
                        break;
                    }
                    case 15: 
                    case 18: {
                        if (this.fV < 255) {
                            this.fV += 20;
                            break;
                        }
                        this.fW = 0;
                        if (this.eE == 15) {
                            this.eT = 2;
                        } else if (this.eE == 18) {
                            this.eT = 3;
                        }
                        bl = true;
                        this.fY = (this.aX[3].d - this.aU[1].f) / 5;
                        this.fZ = (this.aU[1].g - this.aX[3].e) / 5;
                        this.gc = 0;
                        break;
                    }
                    case 17: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.bq = 0;
                        this.bs = 33;
                        this.aX[2].l = 0;
                        this.aX[4].a = 11;
                        this.aX[4].b = 11;
                        this.aX[5].a = 10;
                        this.aX[5].b = 10;
                        this.aX[6].a = 10;
                        this.aX[6].b = 12;
                        n2 = 0;
                        while (n2 < 3) {
                            this.aX[4 + n2].d = this.aX[4 + n2].a * 20 + 10;
                            this.aX[4 + n2].e = (this.aX[4 + n2].b + 1) * 20;
                            ++n2;
                        }
                        this.aX[8].a = 5;
                        this.aX[8].b = 14;
                        this.aX[8].d = 110;
                        this.aX[8].e = 300;
                        this.aX[8].c = 3;
                        this.aU[0].h = 4;
                        this.aU[0].i = 14;
                        this.aU[1].h = 3;
                        this.aU[1].i = 14;
                        n2 = 0;
                        while (n2 < 2) {
                            this.aU[n2].f = this.aU[n2].h * 20 + 10;
                            this.aU[n2].g = (this.aU[n2].i + 1) * 20;
                            this.aU[n2].a = 3;
                            ++n2;
                        }
                        ++this.eE;
                        break;
                    }
                    case 20: {
                        if (this.gc > 4) {
                            this.gd = this.aU[1].f;
                            this.ge = this.aU[1].g;
                            n2 = 0;
                            while (n2 < 2) {
                                this.aU[n2].j = 11;
                                this.aU[n2].k = 11;
                                this.aU[n2].e = 3;
                                ++n2;
                            }
                            this.aX[8].h = 1;
                            ++this.eE;
                            break;
                        }
                        this.gd -= this.fY;
                        this.ge += this.fZ;
                        ++this.gc;
                        break;
                    }
                    case 21: {
                        if (this.aU[0].h == 14 && this.aU[0].f % 20 == 0) {
                            n2 = 0;
                            while (n2 < 2) {
                                this.aU[n2].a = 0;
                                this.aU[n2].e = 0;
                                ++n2;
                            }
                            this.aX[8].c = 0;
                            ++this.eE;
                        }
                        this.gd = this.aU[0].f;
                        this.ge = this.aU[0].g;
                        break;
                    }
                    case 22: {
                        this.gd = this.aU[0].f;
                        this.ge = this.aU[0].g;
                        if (this.aU[0].i == 11 && this.aU[0].g % 20 == 0) {
                            this.aU[0].e = 4;
                            this.gd = this.aU[0].f;
                        }
                        if (this.aU[1].i != 9 || this.aU[1].g % 20 != 0) break;
                        this.aU[1].e = 4;
                        this.aU[1].a = 3;
                        this.aX[8].h = 0;
                        this.aX[8].c = 2;
                        this.ge = this.aU[1].g;
                        this.eT = 9;
                        bl = true;
                        break;
                    }
                    case 24: {
                        this.fW = 1;
                        this.fX = 7;
                        this.fV = 100;
                        this.aU[0].B = 1;
                        this.aU[0].C = 1;
                        this.aW();
                        ++this.eE;
                        break;
                    }
                    case 25: {
                        this.aU[0].C = 0;
                        this.fV = 255;
                        ++this.eE;
                        break;
                    }
                    case 26: {
                        this.fV = 100;
                        this.aU[0].B = 1;
                        this.aU[0].C = 1;
                        ++this.eE;
                        break;
                    }
                    case 27: {
                        this.aU[0].C = 0;
                        this.aU[0].B = 0;
                        this.fW = 0;
                        this.fV = 255;
                        this.aX[3].l = 0;
                        this.aX[7].l = 1;
                        this.aX[7].a = this.aX[3].a;
                        this.aX[7].d = this.aX[3].d;
                        ++this.eE;
                        break;
                    }
                    case 28: {
                        this.eT = 2;
                        bl = true;
                        break;
                    }
                    case 30: {
                        if (this.aU[1].i == 7) {
                            this.aU[1].a = 3;
                            this.aU[1].e = 3;
                            ++this.eE;
                            break;
                        }
                        this.aU[1].a = 0;
                        this.aU[1].e = 0;
                        break;
                    }
                    case 31: {
                        if (this.aU[1].h != 13 || this.aU[1].f % 20 != 10) break;
                        this.aU[1].e = 4;
                        this.aU[1].a = 0;
                        ++this.eE;
                        break;
                    }
                    case 32: {
                        this.aU[1].a = 1;
                        this.eT = 1;
                        bl = true;
                        break;
                    }
                    case 34: {
                        this.eT = 2;
                        bl = true;
                        break;
                    }
                    case 36: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.fV = 0;
                        ++this.fl;
                        this.a(this.fl, 1);
                        this.eE = 0;
                        this.fR = 1;
                        this.b(59, 11, 8, 1, 1);
                        this.aX[2].l = 0;
                    }
                }
                this.fa = this.gd;
                this.fb = this.ge;
                break;
            }
            case 2: {
                switch (this.eE) {
                    case 0: {
                        if (this.fV == 0) {
                            this.bq = 0;
                        }
                        this.bs = 34;
                        if (this.fV < 255) {
                            this.fV += 20;
                        } else {
                            this.fW = 0;
                            this.eT = 3;
                            bl = true;
                            this.fY = (this.aX[1].d - this.aX[2].d) / 5;
                            this.fZ = (this.aX[2].e - this.aX[1].e) / 5;
                            this.gc = 0;
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 2: {
                        if (this.gc > 4) {
                            this.gd = this.aX[2].d;
                            this.ge = this.aX[2].e;
                            this.aX[2].l = 1;
                            this.eT = 1;
                            bl = true;
                        } else {
                            this.gd -= this.fY;
                            this.ge += this.fZ;
                            ++this.gc;
                        }
                        this.fa = this.gd;
                        this.fb = this.ge;
                        break;
                    }
                    case 4: {
                        if (this.aX[2].b == 7 && this.aX[2].e % 20 == 0) {
                            this.aX[2].c = 2;
                            ++this.eE;
                        } else {
                            this.aX[2].h = 1;
                        }
                        this.fa = this.aX[2].d;
                        this.fb = this.aX[2].e;
                        break;
                    }
                    case 5: {
                        if (this.aX[2].a == 11 && this.aX[2].d % 20 == 10) {
                            this.aX[2].c = 1;
                            this.aX[2].h = 0;
                            this.eT = 7;
                            bl = true;
                        }
                        this.fa = this.gd = this.aX[2].d;
                        this.fb = this.ge = this.aX[2].e;
                        break;
                    }
                    case 7: {
                        if (this.aX[2].a == 10 && this.aX[2].d % 20 == 10) {
                            this.aX[2].c = 1;
                            ++this.eE;
                        } else {
                            this.aX[2].c = 2;
                            this.aX[2].h = 1;
                        }
                        this.fa = this.gd = this.aX[2].d;
                        this.fb = this.ge = this.aX[2].e;
                        break;
                    }
                    case 8: {
                        if (this.aX[2].b == 10 && this.aX[2].e % 20 == 0) {
                            this.aX[2].c = 3;
                            ++this.eE;
                        }
                        this.fa = this.gd = this.aX[2].d;
                        this.fb = this.ge = this.aX[2].e;
                        break;
                    }
                    case 9: 
                    case 10: 
                    case 11: {
                        if (this.aX[2].a != 11 || this.aX[2].d % 20 != 10) break;
                        this.aX[2].c = 0;
                        this.aX[2].h = 0;
                        this.aX[0].l = 0;
                        ++this.eE;
                        break;
                    }
                    case 12: {
                        if (this.aX[2].a == 14 && this.aX[2].d % 20 == 10) {
                            this.aX[2].c = 0;
                            ++this.eE;
                            break;
                        }
                        this.aX[2].c = 3;
                        this.aX[2].h = 1;
                        break;
                    }
                    case 13: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.fW = 0;
                        this.eY = 1;
                        this.eX = 2;
                        this.eT = 5;
                        bl = true;
                        this.aX[1].l = 0;
                        this.a(1, 2, 14, 3, 1);
                        this.aX[2].h = 0;
                        break;
                    }
                    case 15: {
                        this.fW = 1;
                        this.fX = 6;
                        this.eY = 0;
                        if (this.fV < 255) {
                            this.fV += 20;
                        } else {
                            this.aX[2].h = 1;
                            this.fW = 0;
                            ++this.eE;
                        }
                        this.fa = this.gd = this.aX[2].d;
                        this.fb = this.ge = this.aX[2].e;
                        break;
                    }
                    case 16: {
                        if (this.aX[2].b == 8 && this.aX[2].e % 20 == 0) {
                            this.aX[2].h = 0;
                            this.aX[2].c = 3;
                            ++this.eE;
                        }
                        this.fa = this.gd = this.aX[2].d;
                        this.fb = this.ge = this.aX[2].e;
                        break;
                    }
                    case 17: {
                        this.aX[2].c = 0;
                        ++this.eE;
                        break;
                    }
                    case 18: {
                        this.aX[2].c = 2;
                        this.eT = 1;
                        bl = true;
                        break;
                    }
                    case 20: {
                        if (this.aX[2].a == 12 && this.aX[2].d % 20 == 10) {
                            this.aX[2].h = 0;
                            this.aH[1][8 * this.bC[0] + 11] = 0;
                            this.eT = 1;
                            bl = true;
                            break;
                        }
                        this.aX[2].h = 1;
                        break;
                    }
                    case 22: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.fR = 0;
                        this.fW = 0;
                        this.eY = 1;
                        this.eX = 2;
                        this.eS = 2;
                        this.gc = 255;
                        ++this.eE;
                        break;
                    }
                    case 23: {
                        if (this.gc > 0) {
                            this.gc -= 20;
                        }
                        if (this.gc >= 0) break;
                        this.gc = 0;
                        ++this.eE;
                        break;
                    }
                    case 24: 
                    case 25: 
                    case 26: 
                    case 27: {
                        ++this.eE;
                        break;
                    }
                    case 28: {
                        if (this.gc < 255) {
                            this.gc += 20;
                        }
                        if (this.gc <= 255) break;
                        this.gc = 255;
                        ++this.fl;
                        this.a(this.fl, 1);
                        this.eY = 1;
                        this.eX = 2;
                        this.eE = 0;
                    }
                }
                break;
            }
            case 3: {
                switch (this.eE) {
                    case 0: {
                        this.bX = 1;
                        this.bW = 1;
                        this.aW();
                        this.b(78, 6, 9, 1, 1);
                        this.ca = this.cc * 20;
                        this.bq = 0;
                        this.bs = 35;
                        this.eT = 3;
                        bl = true;
                        break;
                    }
                    case 2: {
                        this.eY = 0;
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV < 255) {
                            this.fV += 20;
                            break;
                        }
                        this.fW = 0;
                        this.eT = 3;
                        bl = true;
                        break;
                    }
                    case 4: {
                        this.fW = 1;
                        this.fX = 6;
                        if (this.fV > 0) {
                            this.fV -= 20;
                            break;
                        }
                        this.aW();
                        n2 = 0;
                        while (n2 < 5) {
                            this.cj[n2][0] = 7;
                            this.cj[n2][1] = n2;
                            this.cj[n2][2] = 0;
                            this.ck[n2][0] = this.ei;
                            this.ck[n2][1] = this.s(n2);
                            ++n2;
                        }
                        this.b(27, 6, 6, 0, 1);
                        this.bq = 0;
                        this.bs = 37;
                        ++this.eE;
                        break;
                    }
                    case 5: {
                        if (this.fV < 255) {
                            this.fV += 20;
                            break;
                        }
                        this.fW = 0;
                        this.eT = 7;
                        bl = true;
                        break;
                    }
                    case 7: {
                        this.aX[0].l = 0;
                        bl2 = true;
                    }
                }
                break;
            }
            case 4: {
                switch (this.eE) {
                    case 0: {
                        this.bq = 0;
                        this.bs = 37;
                        bl = true;
                        break;
                    }
                    case 2: {
                        this.aW();
                        this.aX[0].l = 0;
                        bl2 = true;
                    }
                }
                break;
            }
            case 5: {
                switch (this.eE) {
                    case 0: {
                        this.bq = 0;
                        this.bs = 37;
                        this.eT = 1;
                        bl = true;
                        break block0;
                    }
                    case 2: {
                        this.bZ = 0;
                        this.gs = 1;
                        this.eS = 0;
                        ++this.eE;
                        break block0;
                    }
                    case 3: {
                        break block0;
                    }
                    case 4: {
                        this.eT = 5;
                        bl = true;
                        break block0;
                    }
                    case 6: {
                        this.aW();
                        this.aX[0].l = 0;
                        bl2 = true;
                    }
                }
            }
        }
        if (bl) {
            this.ae();
        }
        this.cD = this.cC;
        if (this.fX != 2 && this.fX != 4) {
            if (this.fV < 0) {
                this.fV = 0;
            }
            if (this.fV > 255) {
                this.fV = 255;
            }
        }
        if (bl2) {
            System.out.println("\u6559\u7a0b\u8fdb\u5165\u5230gameINg\u72b6\u6001");
            this.bu = 23;
            this.bD = 1;
            this.fk = 0;
            this.eX = 0;
            this.eY = 0;
            this.eZ = 0;
            ++this.fl;
            this.o(1);
            if (this.fl < 6) {
                this.bM = 2;
                this.bI = 1;
                if (this.fl == 4) {
                    this.g(4, 3);
                } else if (this.fl == 5) {
                    n2 = 0;
                    while (n2 < 3) {
                        this.b(n2, 0, 0, 0);
                        this.c(3, n2 + 11, 0);
                        this.d(this.fn);
                        ++n2;
                    }
                    n2 = 0;
                    while (n2 < 5) {
                        this.ck[n2][0] = this.ei;
                        this.ck[n2][1] = this.s(n2);
                        ++n2;
                    }
                    this.g(9, 3);
                    this.cl[0][0] = 3;
                    this.cl[0][1] = 0;
                    this.cl[1][0] = 4;
                    this.cl[1][1] = 3;
                    this.cl[2][0] = 5;
                    this.cl[2][1] = 6;
                    this.t(10);
                }
            }
            this.aW();
            this.bq = 0;
        }
    }

    public final void a(int n2, int n3, int n4, int n5, int n6) {
        if (n2 == 0) {
            this.cc = n4;
            this.cd = n5;
            this.cD = this.cC = n6;
            this.ca = n4 * 20 + 10;
            this.cb = (n5 + 1) * 20;
            this.bE = 0;
            return;
        }
        if (n2 == 1) {
            this.aX[n3].a = n4;
            this.aX[n3].b = n5;
            this.aX[n3].c = n6;
            this.aX[n3].d = n4 * 20 + 10;
            this.aX[n3].e = (n5 + 1) * 20;
            return;
        }
        if (n2 == 2) {
            this.aU[n3].h = n4;
            this.aU[n3].i = n5;
            this.aU[n3].b = this.aU[n3].a = n6;
            this.aU[n3].f = n4 * 20 + 10;
            this.aU[n3].g = (n5 + 1) * 20;
        }
    }

    public final void ae() {
        this.bE = 0;
        ++this.eE;
        this.eR = 0;
        this.eS = 1;
        this.bD = 1;
        byte[] byArray = new byte[2];
        byte[] byArray2 = byArray;
        byArray[0] = this.eP[this.eQ].a;
        byArray2[1] = this.eP[this.eQ].b;
        this.a("em", 48);
        this.b("em", 7468);
        this.ab = this.c(0, 0);
        this.fc = this.r(byArray2[1]);
        if (byArray2[0] > 0) {
            this.ac = this.c(byArray2[0], 0);
        }
        this.p();
    }

    public final void b(int n2, int n3, int n4, int n5, int n6) {
        this.A();
        this.bu = 23;
        this.bv = 23;
        this.bV = n2;
        this.cc = n3;
        this.cd = n4;
        this.cD = this.cC = n5;
        this.h();
        if (n6 == 2) {
            this.j();
        }
        this.t = 1;
        this.o();
        this.t = 0;
    }

    public final int a(int[] nArray, int n2) {
        int n3 = 0;
        int n4 = 10;
        int n5 = 1000000000;
        boolean bl = false;
        while (!bl && n4 > 1) {
            if (n2 / n5 > 0) {
                bl = true;
                continue;
            }
            n5 /= 10;
            --n4;
        }
        int n6 = n4;
        while (n6 > 0) {
            nArray[n3] = n2 - n2 / 10 * 10;
            n2 /= 10;
            --n6;
            ++n3;
        }
        return n4;
    }

    /*
     * Unable to fully structure code
     */
    public final int q(int var1_1) {
        block32: {
            var2_2 = 0;
            var3_3 = 0;
            var4_4 = 0;
            var5_5 = false;
            var6_6 = 0;
            var7_7 = 0;
            var8_8 = 0;
            var2_2 = 0;
            while (var2_2 < 50) {
                var3_3 = 0;
                while (var3_3 < 2) {
                    this.cv[var2_2][var3_3] = 0;
                    ++var3_3;
                }
                ++var2_2;
            }
            var2_2 = 0;
            while (var2_2 < 3) {
                this.eN[var2_2] = 0;
                ++var2_2;
            }
            var2_2 = 0;
            if (var1_1 >= 11) ** GOTO lbl116
            while (var2_2 < this.cr) {
                switch (var1_1) {
                    case 0: {
                        if (this.cq[var2_2][1] != 2 && this.cq[var2_2][1] != 3 || this.cl[0][0] > 0 && this.cl[0][1] == var2_2) break;
                        if (this.cl[0][1] == var2_2) {
                            this.eN[0] = var6_6;
                        }
                        this.cv[var6_6][0] = this.cq[var2_2][1];
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                        break;
                    }
                    case 1: {
                        if (this.cq[var2_2][1] != 4 || this.cl[1][0] > 0 && this.cl[1][1] == var2_2) break;
                        if (this.cl[1][1] == var2_2) {
                            this.eN[1] = var6_6;
                        }
                        this.cv[var6_6][0] = this.cq[var2_2][1];
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                        break;
                    }
                    case 2: {
                        if (this.cq[var2_2][1] != 5 || this.cl[2][0] > 0 && this.cl[2][1] == var2_2) break;
                        if (this.cl[2][1] == var2_2) {
                            this.eN[2] = var6_6;
                        }
                        this.cv[var6_6][0] = this.cq[var2_2][1];
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                        break;
                    }
                    case 3: {
                        if (this.cq[var2_2][1] != 1) break;
                        var4_4 = 0;
                        var5_5 = false;
                        while (var4_4 < 5 && !var5_5) {
                            if (this.cj[var4_4][1] == var2_2 && this.cj[var4_4][0] == 1) {
                                var5_5 = true;
                                continue;
                            }
                            ++var4_4;
                        }
                        if (var5_5) break;
                        this.cv[var6_6][0] = this.cq[var2_2][1];
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                        break;
                    }
                    case 5: 
                    case 6: {
                        if (var1_1 == 5) {
                            var7_7 = 0;
                            var8_8 = 7;
                        } else {
                            var7_7 = 1;
                            var8_8 = 6;
                        }
                        if (this.cq[var2_2][1] <= var7_7 || this.cq[var2_2][1] >= var8_8) break;
                        var3_3 = 0;
                        while (var3_3 < 3) {
                            if (this.cl[var3_3][1] == var2_2) {
                                if (this.cl[var3_3][0] == 2 || this.cl[var3_3][0] == 3) {
                                    this.eN[0] = var6_6;
                                    break;
                                }
                                if (this.cl[var3_3][0] == 4) {
                                    this.eN[1] = var6_6;
                                    break;
                                }
                                if (this.cl[var3_3][0] != 5) break;
                                this.eN[2] = var6_6;
                                break;
                            }
                            ++var3_3;
                        }
                        this.cv[var6_6][0] = this.cq[var2_2][1];
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                    }
                }
                ++var2_2;
            }
            if (var1_1 != 3) break block32;
            var2_2 = 0;
            while (var2_2 < 6) {
                var4_4 = 0;
                var5_5 = false;
                if (this.cm[var2_2] > 0) {
                    while (var4_4 < 5 && !var5_5) {
                        if (this.cj[var4_4][0] == 7 && this.cj[var4_4][1] == var2_2) {
                            var5_5 = true;
                            continue;
                        }
                        ++var4_4;
                    }
                    if (!var5_5) {
                        this.cv[var6_6][0] = 7;
                        this.cv[var6_6][1] = var2_2;
                        ++var6_6;
                    }
                }
                ++var2_2;
            }
            break block32;
lbl-1000:
            // 1 sources

            {
                if (this.cA[var1_1 - 11][var2_2][0] > 0) {
                    this.cv[var6_6][0] = var1_1;
                    this.cv[var6_6][1] = var2_2;
                    ++var6_6;
                }
                ++var2_2;
lbl116:
                // 2 sources

                ** while (var2_2 < this.cr / 2)
            }
        }
        var6_6 = var1_1 == 5 ? this.cr : (var1_1 > 10 ? this.cr / 2 : (var6_6 < 16 ? 15 : var6_6 / 5 * 5));
        return var6_6;
    }

    public final int b(int n2, int n3, int n4, int n5) {
        block14: {
            int n6 = 0;
            int n7 = 0;
            boolean bl = false;
            boolean bl2 = false;
            int n8 = 0;
            if (n5 == 0) {
                bl = true;
                n8 = n2;
            } else if (n5 == 1) {
                while (n6 < this.cr && !bl) {
                    if (n3 == this.cq[n6][0] && n2 == this.cq[n6][1]) {
                        bl = true;
                        continue;
                    }
                    ++n6;
                }
                if (bl) {
                    n8 = n6;
                }
            }
            if (!bl) break block14;
            if (this.cq[n8][1] < 2 || this.cq[n8][1] > 5) {
                this.cs -= this.cq[n8][5] * n4;
                if (this.cq[n8][4] > n4) {
                    int[] nArray = this.cq[n8];
                    nArray[4] = nArray[4] - n4;
                } else {
                    n7 = 0;
                    while (n7 < 5) {
                        if (n8 == this.cj[n7][1] && this.cj[n7][0] == 1) {
                            this.cj[n7][0] = 0;
                            this.cj[n7][1] = 0;
                            this.cj[n7][2] = 0;
                        }
                        ++n7;
                    }
                    n7 = 0;
                    while (n7 < 12) {
                        this.cq[n8][n7] = 0;
                        ++n7;
                    }
                }
            } else {
                this.cs -= this.cq[n8][5];
                n7 = 0;
                while (n7 < 12) {
                    this.cq[n8][n7] = 0;
                    ++n7;
                }
            }
        }
        return 0;
    }

    public final int d(int[] nArray) {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        n4 = this.c(nArray[1], nArray[0], nArray[4], nArray[5]);
        if (n4 > -1) {
            n2 = 0;
            while (n2 < 12) {
                if (n2 == 4 && (nArray[1] < 2 || nArray[1] > 5)) {
                    int[] nArray2 = this.cq[n4];
                    nArray2[4] = nArray2[4] + nArray[4];
                } else {
                    this.cq[n4][n2] = nArray[n2];
                }
                ++n2;
            }
            n3 = nArray[1] > 1 && nArray[1] < 6 ? 1 : nArray[4];
            this.cs += n3 * nArray[5];
        }
        return n4;
    }

    /*
     * Unable to fully structure code
     */
    public final int c(int var1_1, int var2_2, int var3_3, int var4_4) {
        block10: {
            var5_5 = 0;
            var6_6 = false;
            var7_7 = 0;
            if (var1_1 <= 1 || var1_1 >= 6) ** GOTO lbl17
            while (var5_5 < this.cr && !var6_6) {
                if (this.cq[var5_5][1] == 0) {
                    var6_6 = true;
                    continue;
                }
                ++var5_5;
            }
            var7_7 = var6_6 ? (this.cs + var4_4 <= this.ct ? var5_5 : -1) : -2;
            break block10;
lbl-1000:
            // 1 sources

            {
                if (this.cq[var5_5][0] == var2_2 && this.cq[var5_5][1] == var1_1) {
                    var6_6 = true;
                    continue;
                }
                ++var5_5;
lbl17:
                // 3 sources

                ** while (var5_5 < this.cr && !var6_6)
            }
lbl18:
            // 1 sources

            if (this.cs + var4_4 * var3_3 <= this.ct) {
                if (var6_6) {
                    var7_7 = this.cq[var5_5][4] + var3_3 > 99 ? -3 : var5_5;
                } else {
                    var5_5 = 0;
                    while (var5_5 < this.cr && !var6_6) {
                        if (this.cq[var5_5][1] == 0) {
                            var6_6 = true;
                            continue;
                        }
                        ++var5_5;
                    }
                    var7_7 = !var6_6 ? -2 : var5_5;
                }
            } else {
                var7_7 = -1;
            }
        }
        return var7_7;
    }

    public final int r(int n2) {
        int n3 = 0;
        boolean bl = false;
        int n4 = 0;
        while (n3 < this.aV && !bl) {
            if (this.aX[n3].j == n2) {
                bl = true;
                continue;
            }
            ++n3;
        }
        if (!bl && n2 > 100) {
            n3 = 0;
            while (n3 < this.aT && !bl) {
                if (this.aU[n3].Y == n2 - 100) {
                    bl = true;
                    n3 += 100;
                    continue;
                }
                ++n3;
            }
        }
        n4 = bl ? n3 : -1;
        return n4;
    }

    public final void c(int n2, int n3, int n4) {
        boolean bl = false;
        System.out.println("\u83b7\u53d6\u9053\u5177\u4fe1\u606f");
        this.gJ = 1;
        try {
            if (n2 < 7) {
                String[] stringArray = null;
                if (n2 == 1) {
                    stringArray = b.e[n3];
                } else if (n2 == 2) {
                    stringArray = b.f[n3];
                } else if (n2 == 3) {
                    stringArray = b.g[n3];
                } else if (n2 == 4) {
                    stringArray = b.h[n3];
                } else if (n2 == 5) {
                    stringArray = b.i[n3];
                } else if (n2 == 6) {
                    stringArray = b.j[n3];
                }
                byte[] byArray = new byte[13];
                this.a(byArray, 0, Integer.parseInt(stringArray[0]));
                this.a(byArray, 4, Integer.parseInt(stringArray[1]));
                this.a(byArray, 8, Integer.parseInt(stringArray[2]));
                byArray[12] = (byte)Integer.parseInt(stringArray[3]);
                if (n4 == 0) {
                    int n5 = 0;
                    while (n5 < 12) {
                        this.fn[n5] = 0;
                        ++n5;
                    }
                    this.fn[2] = (short)this.b(byArray, 0);
                    this.fn[3] = (short)this.b(byArray, 2);
                    this.fp = this.a(byArray, 4);
                    this.fn[5] = (byte)(byArray[8] + 127);
                    this.fq = (byte)(byArray[9] + 127);
                    short s2 = (short)this.b(byArray, 10);
                    this.fn[6] = s2;
                    this.fr = s2;
                    this.fn[7] = (byte)(byArray[12] + 127);
                    this.eT = this.c(stringArray[4], 112);
                } else if (n4 == 1) {
                    this.fp = this.a(byArray, 4);
                    this.fq = (byte)(byArray[9] + 127);
                    this.fr = (short)this.b(byArray, 10);
                    this.fn[7] = (byte)(byArray[12] + 127);
                    this.eT = this.c(stringArray[4], 112);
                }
            } else if (n2 < 11) {
                String string = null;
                if (n2 == 7) {
                    string = b.k[n3];
                } else if (n2 == 8) {
                    string = b.l[n3];
                } else if (n2 == 9) {
                    string = b.m[n3];
                }
                if (this.bZ == 1) {
                    if (n2 == 7 || n2 == 8) {
                        n3 -= 6;
                    } else if (n2 == 9) {
                        n3 -= 4;
                    }
                }
                this.fn[7] = n3;
                this.eT = this.c(string, 112);
            } else if (n2 == 11) {
                String[] stringArray = a.h[n3];
                this.fw = Integer.parseInt(stringArray[0]) + 127;
                int n6 = 0;
                while (n6 < 4) {
                    this.fu[n6] = Integer.parseInt(stringArray[1 + n6]) + 127;
                    ++n6;
                }
                this.ft = Integer.parseInt(stringArray[5]) + 127;
                this.fv = Integer.parseInt(stringArray[6]);
                this.fp = Integer.parseInt(stringArray[7]);
                n6 = 0;
                while (n6 < 12) {
                    this.fs[n6] = Integer.parseInt(stringArray[7 + n6]) + 127;
                    ++n6;
                }
                this.eT = this.c(stringArray[21], 112);
            } else if (n2 == 12) {
                String[] stringArray = a.g[n3];
                this.fu[0] = Integer.parseInt(stringArray[0]) + 127;
                this.fu[1] = Integer.parseInt(stringArray[1]) + 127;
                this.fu[2] = Integer.parseInt(stringArray[2]) + 127;
                this.fv = Integer.parseInt(stringArray[3]);
                this.ft = Integer.parseInt(stringArray[4]) + 127;
                int n7 = 0;
                while (n7 < 16) {
                    this.fs[n7] = Integer.parseInt(stringArray[4 + n7]) + 127;
                    ++n7;
                }
                this.fp = Integer.parseInt(stringArray[21]);
                this.eT = this.c(stringArray[23], 112);
            }
            this.fn[0] = n3;
            this.fn[1] = n2;
            if (n2 > 10) {
                this.ao();
            }
            this.fo = this.bP[0];
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final int e(int n2, int n3) {
        int n4 = 0;
        int[][] nArrayArray = new int[][]{{30, 26, 22, 18, 14, 10}, {8, 7, 6, 5, 4, 3}, new int[6], {3, 3, 3, 3, 3, 3}, {10, 9, 8, 7, 6, 5}, {2, 2, 2, 2, 2, 1}, {20, 17, 14, 11, 8, 5}, {20, 17, 14, 11, 8, 5}, {17, 14, 11, 8, 5, 2}, new int[6], {10, 9, 8, 7, 6, 5}, {20, 17, 14, 11, 8, 5}};
        int[][] nArrayArray2 = new int[][]{{30, 50, 70, 90, 120, 150}, {30, 50, 70, 90, 120, 150}, {10, 20, 40, 80, 120, 160}, {10, 20, 30, 50, 70, 100}, {10, 20, 30, 40, 50, 60}, {10, 20, 30, 40, 50, 60}, {80, 130, 180, 230, 280, 330}, {30, 60, 90, 120, 170, 220}, {30, 60, 90, 120, 170, 220}, {10, 20, 30, 40, 50, 60}, {10, 20, 30, 40, 50, 60}, {20, 70, 120, 170, 220, 300}};
        if (this.ce[3] >= nArrayArray2[n2][n3]) {
            this.dy = nArrayArray2[n2][n3] == -1 ? this.cg / 10 : nArrayArray2[n2][n3];
            this.dz = nArrayArray[n2][n3] / 2;
            n4 = 1;
        }
        return n4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void f(int n2, int n3) {
        int n4 = 0;
        boolean bl = false;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        if (n2 == 1) {
            if (this.cq[n3][2] == 1) {
                if (this.dV == 1) {
                    this.dV = 0;
                    this.de = 0;
                    this.cE = this.dH == 1 ? 20 : 10;
                    this.ca = this.cc * 20 + 10;
                    this.cb = (this.cd + 1) * 20;
                }
            } else if (this.cq[n3][2] == 2) {
                this.ce[7] = this.ce[7] + this.cq[n3][3];
                this.v(this.bZ);
                this.w(this.bZ);
                this.aj();
                this.ce[3] = this.cg;
            } else if (this.cq[n3][0] < 31) {
                this.ce[2] = this.cq[n3][2] == 100 ? this.cf : this.ce[2] + this.cq[n3][2];
                this.ce[3] = this.cq[n3][3] == 100 ? this.cg : this.ce[3] + this.cq[n3][3];
            } else {
                this.gF = 1;
                if (this.cq[n3][0] == 31) {
                    this.gG = 10;
                } else if (this.cq[n3][0] == 32) {
                    this.gG = 20;
                } else if (this.cq[n3][0] == 33) {
                    this.gG = 36;
                }
                this.gH = this.cq[n3][2];
                this.gI = this.cq[n3][3];
            }
        } else if (n2 == 7) {
            n5 = n3;
        }
        if (n2 == 1) {
            if (this.ce[2] > this.cf) {
                this.ce[2] = this.cf;
            }
            if (this.ce[3] > this.cg) {
                this.ce[3] = this.cg;
            }
            if (this.cq[n3][4] > 0) {
                int[] nArray = this.cq[n3];
                nArray[4] = nArray[4] - 1;
            }
            this.cs -= this.cq[n3][5];
            if (this.cq[n3][4] == 0) {
                n7 = 0;
                while (n7 < 5) {
                    if (n3 == this.cj[n7][1] && this.cj[n7][0] == 1) {
                        this.cj[n7][0] = 0;
                        this.cj[n7][1] = 0;
                        this.cj[n7][2] = 0;
                    }
                    ++n7;
                }
                n7 = 0;
                while (n7 < 12) {
                    this.cq[n3][n7] = 0;
                    ++n7;
                }
            }
            this.dC = 0;
            this.dB = 0;
            return;
        }
        if (this.bH == 1) {
            if (this.cm[n5] > 0) {
                n6 = n5;
                if (this.bZ == 1) {
                    n6 += 6;
                }
                n4 = this.e(n6, this.cm[n5] - 1);
            }
            if (n4 == 1) {
                this.eb = 0;
                if (this.bZ == 0 && n5 == 2 || this.bZ == 1 && n5 == 3) {
                    this.ds = 1;
                    this.dr = n5;
                    this.dp = 1;
                    this.dq = 0;
                    this.bE = 0;
                    this.bD = 0;
                    this.dA = 0;
                    if (this.fm != 0) return;
                    this.ce[3] = this.ce[3] - this.dy;
                    if (this.ce[3] >= 0) return;
                    this.ce[3] = 0;
                    return;
                }
                this.dx = this.ei;
                this.dr = n5;
                this.dA = 1;
                this.bD = 0;
                this.bE = 0;
                this.bq = 3;
                return;
            }
            this.go = 1;
            this.gp = this.ei;
            this.br = 1;
            this.bp = 14;
            return;
        }
        this.dC = 0;
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void af() {
        block10: {
            var1_1 = 0;
            var2_2 = false;
            if (this.bZ != 0) break block10;
            if (true) ** GOTO lbl34
            do {
                if (this.dF[var1_1][3] == 0) {
                    switch (this.cC) {
                        case 0: {
                            this.dF[var1_1][0] = this.cc;
                            this.dF[var1_1][1] = this.cd - 1;
                            break;
                        }
                        case 1: {
                            this.dF[var1_1][0] = this.cc;
                            this.dF[var1_1][1] = this.cd + 1;
                            break;
                        }
                        case 2: {
                            this.dF[var1_1][0] = this.cc - 1;
                            this.dF[var1_1][1] = this.cd;
                            break;
                        }
                        case 3: {
                            this.dF[var1_1][0] = this.cc + 1;
                            this.dF[var1_1][1] = this.cd;
                        }
                    }
                    if (this.a(this.cc, this.cd, this.ca, this.cb, this.cC, 20, 0, 0) == 1) {
                        v0 = this.aH[2];
                        v1 = this.dF[var1_1][1] * this.bC[0] + this.dF[var1_1][0];
                        v0[v1] = v0[v1] + 10;
                        this.dF[var1_1][2] = (this.cm[5] - 1) * 20 + 30;
                        this.dF[var1_1][3] = 1;
                        this.dF[var1_1][4] = 0;
                        this.dG[var1_1] = this.ei;
                    }
                    var2_2 = true;
                } else {
                    ++var1_1;
                }
lbl34:
                // 3 sources

                if (var1_1 >= 10) return;
            } while (!var2_2);
            return;
        }
        this.dG[0] = this.ei;
        this.dE = 1;
        this.dF[0][2] = (this.cm[5] - 1) * 20 + 30;
        this.dF[0][3] = 1;
    }

    public final int s(int n2) {
        boolean bl = false;
        int n3 = 0;
        int[][] nArrayArray = new int[][]{{200, 180, 160, 140, 120, 100}, {350, 300, 250, 200, 150, 100}, {400, 350, 300, 250, 200, 150}, {100, 90, 80, 70, 60, 30}, {100, 90, 80, 70, 60, 50}, {10, 9, 8, 7, 6, 6}, {350, 320, 290, 260, 230, 200}, {220, 200, 180, 160, 140, 120}, {400, 350, 300, 250, 200, 150}, {100, 90, 80, 70, 60, 30}, {100, 90, 80, 70, 60, 50}, {90, 100, 120, 130, 130, 130}};
        n3 = n2;
        if (this.bZ == 1) {
            n3 += 6;
        }
        return nArrayArray[n3][this.cm[n2] - 1];
    }

    public final void t(int n2) {
        int n3 = 0;
        n3 = 0;
        while (n3 < 5) {
            if (this.cj[n3][0] == 7 && (n2 == 10 || n2 == this.cj[n3][1])) {
                this.cj[n3][2] = 0;
                this.ck[n3][0] = this.ei;
                this.ck[n3][1] = this.s(this.cj[n3][1]);
            }
            ++n3;
        }
    }

    public final void ag() {
        int[][][] nArrayArray = new int[6][][];
        int[][] nArrayArray2 = new int[4][];
        int[] nArray = new int[2];
        nArray[1] = -1;
        nArrayArray2[0] = nArray;
        int[] nArray2 = new int[2];
        nArray2[0] = -1;
        nArrayArray2[1] = nArray2;
        int[] nArray3 = new int[2];
        nArray3[1] = 1;
        nArrayArray2[2] = nArray3;
        int[] nArray4 = new int[2];
        nArray4[0] = 1;
        nArrayArray2[3] = nArray4;
        nArrayArray[0] = nArrayArray2;
        nArrayArray[1] = new int[][]{{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        int[][] nArrayArray3 = new int[4][];
        int[] nArray5 = new int[2];
        nArray5[1] = 2;
        nArrayArray3[0] = nArray5;
        int[] nArray6 = new int[2];
        nArray6[0] = 2;
        nArrayArray3[1] = nArray6;
        int[] nArray7 = new int[2];
        nArray7[1] = -2;
        nArrayArray3[2] = nArray7;
        int[] nArray8 = new int[2];
        nArray8[0] = -2;
        nArrayArray3[3] = nArray8;
        nArrayArray[2] = nArrayArray3;
        nArrayArray[3] = new int[][]{{-1, -2}, {2, -1}, {-1, 2}, {-2, -1}};
        nArrayArray[4] = new int[][]{{-2, 1}, {-1, -2}, {2, -1}, {1, 2}};
        nArrayArray[5] = new int[][]{{-2, -2}, {-2, 2}, {2, 2}, {2, -2}};
        int[][][] nArrayArray4 = nArrayArray;
        int n2 = 0;
        int n3 = 0;
        n2 = 0;
        while (n2 < this.cm[2]) {
            n3 = 0;
            while (n3 < 4) {
                if (this.cc + nArrayArray4[n2][n3][0] > 0 && this.cc + nArrayArray4[n2][n3][0] < this.bC[0] && this.cd + nArrayArray4[n2][n3][1] > 0 && this.cd + nArrayArray4[n2][n3][1] < this.bC[1]) {
                    this.dK[n2][n3][0] = this.cc + nArrayArray4[n2][n3][0];
                    this.dK[n2][n3][1] = this.cd + nArrayArray4[n2][n3][1];
                    this.dK[n2][n3][2] = 0;
                    this.dK[n2][n3][3] = n3 == 0 ? 1 : 0;
                }
                ++n3;
            }
            ++n2;
        }
    }

    public final void ah() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        n5 = this.ca - this.aI[0];
        n6 = this.cb - this.aI[1];
        this.dv = 0;
        int n7 = 0;
        while (n7 < 20) {
            n4 = 0;
            while (n4 < 9) {
                this.du[n7][n4] = 0;
                ++n4;
            }
            ++n7;
        }
        n4 = 0;
        n7 = 0;
        while (n7 < this.aT) {
            if (this.aU[n7].ay == 1 && this.aU[n7].ab < 2) {
                n2 = this.aU[n7].f - this.aI[0];
                n3 = this.aU[n7].g - this.aI[1];
                if (n2 > 0 && n3 > 0 && n2 < this.bx[0] && n3 < this.bx[1] - 20) {
                    this.du[n4][0] = n2;
                    this.du[n4][1] = n3 - 38;
                    this.du[n4][2] = n5;
                    this.du[n4][3] = n6 - 15;
                    this.du[n4][4] = 0;
                    this.du[n4][5] = (this.du[n4][2] - this.du[n4][0]) / 4;
                    this.du[n4][6] = (this.du[n4][3] - this.du[n4][1]) / 4;
                    this.du[n4][7] = 1;
                    this.du[n4][8] = n7;
                    ++n4;
                    ++this.dv;
                    this.aU[n7].T = 1;
                }
            }
            ++n7;
        }
    }

    public final void u(int n2) {
        int n3 = this.dR[0];
        int n4 = this.dR[1];
        int n5 = 0;
        int n6 = 0;
        switch (n2) {
            case 0: {
                --n4;
                break;
            }
            case 1: {
                ++n4;
                break;
            }
            case 2: {
                --n3;
                break;
            }
            case 3: {
                ++n3;
            }
        }
        n5 = n3 * 20 - this.aI[0];
        n6 = (n4 + 1) * 20 - this.aI[1];
        if (n5 >= 0 && n5 <= this.bx[0] && n6 >= 0 && n6 <= this.bx[1]) {
            this.dR[0] = n3;
            this.dR[1] = n4;
            this.du[this.dO][0] = this.dR[0];
            this.du[this.dO][1] = this.dR[1];
            this.du[this.dO][2] = 0;
            this.du[this.dO][3] = 1;
            ++this.dO;
            if (this.dO == this.cm[1]) {
                ++this.dq;
            }
        }
    }

    public final int ai() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (this.dH == 1) {
            n2 = this.ei - this.dJ;
            if (n2 < 0) {
                n2 = 9999 + this.dJ;
            }
            if (this.dI <= n2) {
                this.dH = 0;
                this.cE = 10;
                this.cj[this.dD][2] = 0;
                this.ck[this.dD][0] = this.ei;
            }
        }
        if (this.cx == 1) {
            n2 = this.ei - this.cy;
            if (n2 < 0) {
                n2 = 9999 + this.cy;
            }
            if (n2 >= 10) {
                this.cx = 0;
            } else if (n2 == 5) {
                this.eb = 0;
            }
        }
        if (this.eb == 1) {
            n2 = this.ei - this.dx;
            if (n2 < 0) {
                n2 = 9999 + this.dx;
            }
            if (n2 >= this.dz) {
                this.ce[2] = this.ec > 4 ? this.ce[2] + 6 : this.ce[2] + 4;
                if (this.ce[2] > this.cf) {
                    this.ce[2] = this.cf;
                }
                this.ce[3] = this.ce[3] + this.ec;
                if (this.ce[3] > this.cg) {
                    this.ce[3] = this.cg;
                }
                if (this.eJ[0][2] == 1 && this.eJ[0][3] == this.bV) {
                    this.eJ[0][22] = 1;
                }
                this.dx = this.ei;
            }
        }
        if (this.eh == 1) {
            n2 = this.ei - this.eg;
            if (n2 < 0) {
                n2 = 9999 + this.eg;
            }
            if (n2 > 10) {
                this.eh = 0;
            }
        }
        if (this.go > 0) {
            n2 = this.ei - this.gp;
            if (n2 < 0) {
                n2 = 9999 + this.gp;
            }
            if (n2 > 10) {
                this.go = 0;
            }
        }
        if (this.aM > 0) {
            n2 = this.ei - this.aN;
            if (n2 < 0) {
                n2 = 9999 + this.aN;
            }
            if (n2 > 5) {
                if (this.aO < 5) {
                    ++this.aO;
                } else {
                    this.aM = 0;
                }
            }
        }
        if (this.gq > 0) {
            n2 = this.ei - this.gr;
            if (n2 < 0) {
                n2 = 9999 + this.gr;
            }
            if (n2 > 10) {
                this.gq = 0;
            }
        }
        n3 = 0;
        while (n3 < 10) {
            if (this.bm[n3].f == 1 && this.bm[n3].g == 0) {
                n2 = this.ei - this.bm[n3].h;
                if (n2 < 0) {
                    n2 = 9999 + this.bm[n3].h;
                }
                if (n2 == 500) {
                    this.bm[n3].i = 1;
                }
                if (n2 > 510) {
                    this.bm[n3].f = 0;
                }
            }
            ++n3;
        }
        if (this.bZ == 0) {
            n3 = 0;
            while (n3 < 10) {
                if (this.dF[n3][3] == 1) {
                    if (this.dF[n3][4] < 3) {
                        int[] nArray = this.dF[n3];
                        nArray[4] = nArray[4] + 1;
                    } else {
                        n2 = this.ei - this.dG[n3];
                        if (n2 < 0) {
                            n2 += 9999;
                        }
                        if (this.dF[n3][2] <= n2) {
                            this.dF[n3][3] = 0;
                            int[] nArray = this.aH[2];
                            int n5 = this.dF[n3][1] * this.bC[0] + this.dF[n3][0];
                            nArray[n5] = nArray[n5] - 10;
                        }
                    }
                }
                ++n3;
            }
        } else if (this.dE == 1) {
            n2 = this.ei - this.dG[0];
            if (n2 < 0) {
                n2 += 9999;
            }
            if (n2 >= this.dF[0][2]) {
                this.dE = 0;
            }
        }
        if (this.dV == 1) {
            n2 = this.ei - this.dZ;
            if (n2 < 0) {
                n2 = 9999 + this.dZ;
            }
            if (n2 > this.ea) {
                this.dV = 0;
                this.de = 0;
                this.cE = this.dH == 1 ? 20 : 10;
                this.ca = this.cc * 20 + 10;
                this.cb = (this.cd + 1) * 20;
            }
        }
        if (this.cl[0][0] > 0 && this.cq[this.cl[0][1]][6] == 0 && this.cF == 0) {
            n4 = 1;
        }
        if (this.cl[1][0] > 0 && this.cq[this.cl[1][1]][6] == 0) {
            n4 = 1;
        }
        return n4;
    }

    public final int v(int n2) {
        boolean bl = false;
        if (n2 == 0) {
            this.ce[9] = this.ce[0] + this.ce[4] * 3 / 2 + this.ce[5] / 5 + this.ce[7] / 10;
        } else if (n2 == 1) {
            this.ce[9] = this.ce[0] + this.ce[4] + this.ce[5] / 3 + this.ce[7] / 2;
        }
        return 0;
    }

    public final int w(int n2) {
        boolean bl = false;
        if (n2 == 0) {
            this.ce[10] = this.ce[0] + this.ce[4] * 4 / 10 + this.ce[5] / 5 + this.ce[7] * 2 / 10;
        } else if (n2 == 1) {
            this.ce[10] = this.ce[0] + this.ce[4] * 3 / 10 + this.ce[5] / 3 + this.ce[7] * 8 / 10;
        }
        return 0;
    }

    public final int aj() {
        boolean bl = false;
        this.cf = this.ce[0] * 4 + this.ce[6] * 8;
        this.cg = this.ce[0] * 4 + this.ce[7] * 8;
        if (this.ce[2] > this.cf) {
            this.ce[2] = this.cf;
        }
        if (this.ce[3] > this.cg) {
            this.ce[3] = this.cg;
        }
        return 0;
    }

    public final int ak() {
        boolean bl = false;
        int n2 = 0;
        switch (this.ce[0]) {
            case 1: {
                n2 = 8;
                break;
            }
            case 2: {
                n2 = 16;
                break;
            }
            case 3: {
                n2 = 32;
                break;
            }
            case 4: {
                n2 = 42;
                break;
            }
            case 5: {
                n2 = 50;
                break;
            }
            default: {
                n2 = (this.ce[0] - 3) * 10 + this.ch;
            }
        }
        this.ce[1] = this.ce[1] - this.ch;
        this.ch = n2;
        return 0;
    }

    public final void d(int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = 0;
        if (n2 == 0) {
            n5 = 2421;
        }
        n6 = 0;
        while (n6 < 26) {
            this.eJ[n3][n6] = 0;
            ++n6;
        }
        this.gJ = 1;
        try {
            String string = "/data/WineData.dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var10_9 = null;
            this.gD = inputStream = o._m(clazz, string);
            this.gE = new DataInputStream(this.gD);
            byte[] byArray = new byte[22];
            this.gE.skip(n5 += n4 * 22);
            this.gE.read(byArray, 0, 22);
            this.g();
            n6 = 0;
            while (n6 < 22) {
                this.eJ[n3][n6] = byArray[n6] + 128;
                ++n6;
            }
            this.eJ[n3][22] = 0;
            this.eJ[n3][23] = 0;
            this.eJ[n3][24] = 0;
            this.eJ[n3][25] = 0;
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void al() {
        System.out.println("GetQuestName--------------------");
        int n2 = 0;
        int n3 = 0;
        boolean bl = false;
        Object var7_4 = null;
        String string = null;
        this.gJ = 1;
        try {
            int n4 = 0;
            while (n4 < 4) {
                if (this.eJ[n4][1] > 0) {
                    if (this.eJ[n4][0] == 0) {
                        n3 = 9;
                    } else if (this.eJ[n4][0] > 0) {
                        n3 = 24 + this.eJ[n4][0] % 10;
                    }
                    n2 = this.eJ[n4][1] - 1;
                    if (n3 == 9) {
                        string = b.c[n2];
                    } else if (n3 == 25) {
                        string = a.f[n2];
                    } else if (n3 == 26) {
                        string = a.e[n2];
                    } else if (n3 == 27) {
                        string = a.d[n2];
                    }
                    int n5 = string.indexOf("|", 0);
                    this.eK[n4] = string.substring(0, n5);
                    string = null;
                }
                ++n4;
            }
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void e(int n2, int n3, int n4) {
        System.out.println("-----GetQuestDe---------------");
        int n5 = 0;
        int n6 = 0;
        String string = null;
        String string2 = null;
        this.gJ = 1;
        try {
            if (n2 == 0) {
                n6 = 9;
            } else if (n2 > 0) {
                n6 = 24 + n2 % 10;
            }
            n5 = this.eJ[n3][1] - 1;
            if (n6 == 9) {
                string2 = b.c[n5];
            } else if (n6 == 25) {
                string2 = a.f[n5];
            } else if (n6 == 26) {
                string2 = a.e[n5];
            } else if (n6 == 27) {
                string2 = a.d[n5];
            }
            int n7 = string2.indexOf("|", 0);
            string = string2.substring(n7 + 1, string2.length());
            this.eT = this.a(string, 0, 112);
            this.eR = 0;
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void am() {
        int n2 = 0;
        n2 = this.fm == 1 ? this.fl : this.eD;
        int n3 = 0;
        while (n3 < this.bc) {
            int n4 = this.bb[n3 * 7 + 0];
            int n5 = this.bb[n3 * 7 + 2];
            int n6 = this.bb[n3 * 7 + 1];
            int n7 = this.bb[n3 * 7 + 3];
            int n8 = 0;
            while (n8 < n5 - n4 + 1) {
                int n9 = 0;
                while (n9 < n7 - n6 + 1) {
                    if (this.bb[n3 * 7 + 4] <= n2 || this.bb[n3 * 7 + 6] >= n2) {
                        if (this.aH[3][(n9 + n6) * this.bC[0] + (n4 + n8)] % 20 == 9) {
                            this.aH[1][(n9 + n6) * this.bC[0] + (n4 + n8)] = 0;
                            int[] nArray = this.aH[3];
                            int n10 = (n9 + n6) * this.bC[0] + (n4 + n8);
                            nArray[n10] = nArray[n10] - 9;
                        } else if (this.aH[3][(n9 + n6) * this.bC[0] + (n4 + n8)] % 20 == 8) {
                            int[] nArray = this.aH[3];
                            int n11 = (n9 + n6) * this.bC[0] + (n4 + n8);
                            nArray[n11] = nArray[n11] - 8;
                        }
                    } else if (this.aH[3][(n9 + n6) * this.bC[0] + (n4 + n8)] % 20 != 9 && this.aH[3][(n9 + n6) * this.bC[0] + (n4 + n8)] % 20 != 8) {
                        int[] nArray = this.aH[3];
                        int n12 = (n9 + n6) * this.bC[0] + (n4 + n8);
                        nArray[n12] = nArray[n12] + 9;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n3;
        }
    }

    public final void g(int n2, int n3) {
        System.out.println("GetText---------------" + n2 + "," + n3);
        int n4 = 0;
        String string = null;
        this.gJ = 1;
        try {
            if (n3 == 0) {
                string = a.a[n2];
            } else if (n3 == 1) {
                string = a.b[n2 - 1];
            } else if (n3 == 2) {
                string = b.d[n2 - 1];
            } else if (n3 == 3) {
                string = a.c[n2 - 1];
            }
            System.out.println("SText:" + string);
            if (n3 == 0) {
                int n5 = string.indexOf("|", 0);
                if (n5 == -1) {
                    this.bP[0] = string;
                    this.bP[1] = "  ";
                } else {
                    this.bP[0] = string.substring(0, n5);
                    this.bP[1] = string.substring(n5 + 1, string.length());
                }
                this.A = string;
                n4 = 2;
            } else if (n3 == 1) {
                this.aL = string;
            } else if (n3 == 2) {
                this.eT = this.a(string, 0, 112);
            } else if (n3 == 3) {
                this.eT = this.a(string, 0, 170);
            }
            if (n3 == 0) {
                this.bQ = 0;
                this.bR = n4 / 2;
            } else if (n3 == 2 || n3 == 3) {
                this.eR = 0;
            }
        }
        catch (Exception exception) {}
        this.gJ = 0;
    }

    public final void an() {
        int n2 = 0;
        boolean bl = false;
        int n3 = this.cc;
        int n4 = this.cd;
        switch (this.cC) {
            case 0: {
                --n4;
                break;
            }
            case 1: {
                ++n4;
                break;
            }
            case 2: {
                --n3;
                break;
            }
            case 3: {
                ++n3;
            }
        }
        while (n2 < this.bc && !bl) {
            if (this.bb[n2 * 7 + 0] <= n3 && n3 <= this.bb[n2 * 7 + 2] && this.bb[n2 * 7 + 1] <= n4 && n4 <= this.bb[n2 * 7 + 3]) {
                switch (this.cC) {
                    case 0: {
                        if (this.cb % 20 < 10) break;
                        bl = true;
                        break;
                    }
                    case 1: {
                        if (this.cb % 20 > 10) break;
                        bl = true;
                        break;
                    }
                    case 2: {
                        if (this.ca % 20 > 10) break;
                        bl = true;
                        break;
                    }
                    case 3: {
                        if (this.ca % 20 < 10) break;
                        bl = true;
                    }
                }
                if (bl) continue;
                ++n2;
                continue;
            }
            ++n2;
        }
        if (bl) {
            this.g(this.bb[n2 * 7 + 5], 0);
            this.bM = 1;
            this.bI = 1;
            this.br = 1;
            this.bp = 14;
        }
    }

    public final int h(int n2, int n3) {
        int n4 = -1;
        int n5 = 0;
        boolean bl = false;
        while (n5 < this.cr && !bl) {
            if (this.cq[n5][0] == n3 && this.cq[n5][1] == n2) {
                bl = true;
                n4 = n5;
                continue;
            }
            ++n5;
        }
        return n4;
    }

    public final int f(int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (n6 < this.cr / 2 && !bl) {
            if (this.cA[n2 - 11][n6][0] == n3) {
                bl = true;
                continue;
            }
            ++n6;
        }
        if (bl) {
            n5 = this.cA[n2 - 11][n6][1] + n4 <= 99 ? n6 : -3;
        } else {
            n6 = 0;
            while (n6 < this.cr / 2 && !bl) {
                if (this.cA[n2 - 11][n6][0] == 0) {
                    bl = true;
                    continue;
                }
                ++n6;
            }
            n5 = bl ? n6 : -2;
        }
        return n5;
    }

    public final int g(int n2, int n3, int n4) {
        int n5 = 0;
        boolean bl = false;
        n5 = this.f(n2, n3, n4);
        if (n5 > -1) {
            this.cA[n2 - 11][n5][0] = n3;
            int[] nArray = this.cA[n2 - 11][n5];
            nArray[1] = nArray[1] + n4;
        }
        return n5;
    }

    public final int h(int n2, int n3, int n4) {
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (n6 < this.cr / 2 && !bl) {
            if (this.cA[n2 - 11][n6][0] == n3) {
                bl = true;
                continue;
            }
            ++n6;
        }
        if (bl) {
            if (this.cA[n2 - 11][n6][1] - n4 > 0) {
                int[] nArray = this.cA[n2 - 11][n6];
                nArray[1] = nArray[1] - n4;
            } else {
                this.cA[n2 - 11][n6][1] = 0;
                this.cA[n2 - 11][n6][0] = 0;
            }
        } else {
            n5 = -1;
        }
        return n5;
    }

    public final int i(int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        boolean bl = false;
        while (n5 < this.cr / 2 && !bl) {
            if (this.cA[n2 - 11][n5][0] == n3) {
                bl = true;
                continue;
            }
            ++n5;
        }
        n4 = bl ? n5 : -1;
        return n4;
    }

    public final void ao() {
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n6 < 4) {
            n2 = 0;
            bl = false;
            if (this.fn[1] == 11) {
                n3 = 6;
                n4 = this.fs[n6 * 3 + 0];
                n5 = this.fs[n6 * 3 + 1];
            } else if (this.fn[1] == 12) {
                n3 = this.fs[n6 * 4 + 0];
                n4 = this.fs[n6 * 4 + 1];
                n5 = this.fs[n6 * 4 + 2];
            }
            if (n5 > 0) {
                while (n2 < this.cr && !bl) {
                    if (n3 == this.cq[n2][1] && n4 == this.cq[n2][0]) {
                        bl = true;
                        continue;
                    }
                    ++n2;
                }
                this.fx[n6] = bl ? (n3 > 1 && n3 < 6 ? 1 : (this.cq[n2][4] < n5 ? this.cq[n2][4] : n5)) : 0;
            } else {
                this.fx[n6] = 0;
            }
            ++n6;
        }
    }

    public final int ap() {
        boolean bl = false;
        int n2 = 0;
        boolean bl2 = false;
        boolean bl3 = false;
        int n3 = 0;
        if (this.fz == 0) {
            if (this.fx[0] >= this.fE) {
                n2 = 1;
            }
        } else if (this.fz == 1) {
            while (n3 < 4 && !bl3) {
                if (this.fx[n3] >= this.fs[n3 * 3 + 1]) {
                    ++n3;
                    continue;
                }
                bl3 = true;
            }
            if (!bl3) {
                n2 = 1;
            }
        } else if (this.fz == 2) {
            while (n3 < 4 && !bl3) {
                if (this.fx[n3] >= this.fs[n3 * 4 + 2]) {
                    ++n3;
                    continue;
                }
                bl3 = true;
            }
            if (!bl3) {
                n2 = 1;
            }
        }
        return n2;
    }

    public final int aq() {
        block3: {
            block4: {
                block2: {
                    boolean bl = false;
                    if (this.fz != 0) break block2;
                    this.b(this.fJ[1][0], this.fJ[1][1], this.fJ[1][2], 1);
                    break block3;
                }
                if (this.fz != 1) break block4;
                this.h(11, this.cA[0][this.fJ[1][2]][0], 1);
                int n2 = 0;
                while (n2 < 4) {
                    this.b(6, this.fs[n2 * 3 + 0], this.fs[n2 * 3 + 1], 1);
                    ++n2;
                }
                break block3;
            }
            if (this.fz != 2) break block3;
            this.h(12, this.cA[1][this.fJ[1][2]][0], 1);
            int n3 = 0;
            while (n3 < 4) {
                this.b(this.fs[n3 * 4 + 0], this.fs[n3 * 4 + 1], this.fs[n3 * 4 + 2], 1);
                ++n3;
            }
        }
        return 0;
    }

    public final int ar() {
        int[] nArray = new int[12];
        boolean bl = false;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        if (this.fz == 0) {
            this.cq[this.fJ[0][2]][4] = this.fE;
            if (this.cq[this.fJ[0][2]][4] == 10) {
                if (this.fJ[0][0] == 2 || this.fJ[0][0] == 3) {
                    int[] nArray2 = this.cq[this.fJ[0][2]];
                    nArray2[2] = nArray2[2] + this.cq[this.fJ[0][2]][2];
                    int[] nArray3 = this.cq[this.fJ[0][2]];
                    nArray3[3] = nArray3[3] + this.cq[this.fJ[0][2]][3] % 10000;
                } else {
                    int[] nArray4 = this.cq[this.fJ[0][2]];
                    nArray4[2] = nArray4[2] + this.cq[this.fJ[0][2]][2] % 10000;
                }
            } else if (this.fJ[0][0] == 2 || this.fJ[0][0] == 3) {
                n4 = this.cq[this.fJ[0][2]][2] / 10;
                if (n4 == 0) {
                    n4 = 1;
                }
                int[] nArray5 = this.cq[this.fJ[0][2]];
                nArray5[2] = nArray5[2] + n4;
                n4 = this.cq[this.fJ[0][2]][3] % 10000 / 10;
                if (n4 == 0) {
                    n4 = 1;
                }
                int[] nArray6 = this.cq[this.fJ[0][2]];
                nArray6[3] = nArray6[3] + n4;
            } else {
                n4 = this.cq[this.fJ[0][2]][2] % 10000 / 10;
                if (n4 == 0) {
                    n4 = 1;
                }
                int[] nArray7 = this.cq[this.fJ[0][2]];
                nArray7[2] = nArray7[2] + n4;
            }
            n2 = 0;
            while (n2 < 3) {
                if (this.cl[n2][0] > 0 && this.cl[n2][1] == this.fJ[0][2]) {
                    if (this.fJ[0][0] == 2 || this.fJ[0][0] == 3) {
                        this.ce[12] = this.cq[this.fJ[0][2]][2];
                        this.ce[13] = this.cq[this.fJ[0][2]][3];
                    } else if (this.fJ[0][0] == 4) {
                        this.ce[16] = this.cq[this.fJ[0][2]][2];
                    } else if (this.fJ[0][0] == 5) {
                        this.ce[15] = this.cq[this.fJ[0][2]][2];
                    }
                }
                ++n2;
            }
        } else if (this.fz == 1) {
            if (this.fw == 0) {
                n2 = 0;
                while (n2 < 4) {
                    if (this.fu[n2] > 0) {
                        this.fu[n2] = this.s(0, this.fu[n2]) + 1;
                    }
                    int[] nArray8 = this.cq[this.fJ[0][2]];
                    int n5 = 8 + n2;
                    nArray8[n5] = nArray8[n5] + this.fu[n2];
                    n3 = 0;
                    while (n3 < 3) {
                        if (this.cl[n3][0] > 0 && this.cl[n3][1] == this.fJ[0][2]) {
                            int n6 = 4 + n2;
                            this.ce[n6] = this.ce[n6] + this.fu[n2];
                        }
                        ++n3;
                    }
                    ++n2;
                }
            } else if (this.fw == 1) {
                if (this.cq[this.fJ[0][2]][1] == 2 || this.cq[this.fJ[0][2]][1] == 3) {
                    this.cq[this.fJ[0][2]][3] = (this.fu[0] + 1) * 10000 + this.cq[this.fJ[0][2]][3] % 10000;
                } else {
                    this.cq[this.fJ[0][2]][2] = (this.fu[0] + 1) * 10000 + this.cq[this.fJ[0][2]][2] % 10000;
                }
                n2 = 0;
                while (n2 < 3) {
                    if (this.cl[n2][0] > 0 && this.cl[n2][1] == this.fJ[0][2]) {
                        if (n2 == 0) {
                            this.ce[13] = this.cq[this.fJ[0][2]][3];
                        } else if (n2 == 1) {
                            this.ce[16] = this.cq[this.fJ[0][2]][2];
                        } else if (n2 == 2) {
                            this.ce[15] = this.cq[this.fJ[0][2]][2];
                        }
                    }
                    ++n2;
                }
            } else if (this.fw == 2) {
                this.c(this.cq[this.fJ[0][2]][1], this.cq[this.fJ[0][2]][0], 0);
                this.cq[this.fJ[0][2]][6] = this.fn[6];
            }
        } else if (this.fz == 2) {
            this.c(this.fu[0], this.fu[1], 0);
            n2 = 0;
            while (n2 < 12) {
                nArray[n2] = this.fn[n2];
                ++n2;
            }
            nArray[4] = this.fu[2];
            this.d(nArray);
        }
        return 0;
    }

    public final void j(int n2, int n3) {
        this.ce[4] = this.ce[4] - this.cq[n3][8];
        this.ce[5] = this.ce[5] - this.cq[n3][9];
        this.ce[6] = this.ce[6] - this.cq[n3][10];
        this.ce[7] = this.ce[7] - this.cq[n3][11];
        if (n2 == 2 || n2 == 3) {
            this.ce[12] = 0;
            this.ce[13] = 0;
            return;
        }
        if (n2 == 4) {
            this.ce[16] = 0;
            return;
        }
        if (n2 == 5) {
            this.ce[15] = 0;
        }
    }

    public final void k(int n2, int n3) {
        this.ce[4] = this.ce[4] + this.cq[n3][8];
        this.ce[5] = this.ce[5] + this.cq[n3][9];
        this.ce[6] = this.ce[6] + this.cq[n3][10];
        this.ce[7] = this.ce[7] + this.cq[n3][11];
        if (n2 == 2 || n2 == 3) {
            this.ce[12] = this.cq[n3][2];
            this.ce[13] = this.cq[n3][3];
            return;
        }
        if (n2 == 4) {
            this.ce[16] = this.cq[n3][2];
            return;
        }
        if (n2 == 5) {
            this.ce[15] = this.cq[n3][2];
        }
    }

    public final int x(int n2) {
        int[] nArray = new int[12];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        int n7 = -1;
        if (n2 > -1) {
            n3 = this.bm[n2].c;
            n4 = this.bm[n2].d;
            n5 = this.bm[n2].e;
            if (n3 < 13) {
                this.c(n3, n4, 0);
                if (n3 < 11) {
                    n6 = 0;
                    while (n6 < 12) {
                        nArray[n6] = this.fn[n6];
                        ++n6;
                    }
                    if (n3 < 2 || n3 > 5) {
                        nArray[4] = n5;
                    }
                    if ((n7 = this.d(nArray)) > -1) {
                        this.aL = "\u83b7\u5f97" + this.fo;
                    }
                } else {
                    n7 = this.g(n3, n4 + 1, n5);
                    this.aL = this.fo;
                }
                if (n7 < 0) {
                    this.go = n7 * -1 + 1;
                    this.gp = this.ei;
                    this.br = 1;
                    this.bp = 14;
                }
            } else if (n3 == 13) {
                if (this.ce[0] == 70 && this.eD < 78) {
                    n4 = 0;
                    n5 = 0;
                }
                this.aL = "\u7ecf\u9a8c\u503c +" + n4 * n5 + "\u4e0a\u5347";
                this.ce[1] = this.ce[1] + n4 * n5;
                n7 = 1;
            } else if (n3 == 14) {
                this.aL = "\u83b7\u5f97" + n4 * n5 + "\u4e24.";
                this.ce[11] = this.ce[11] + n4 * n5;
                n7 = 1;
            } else if (n3 == 15) {
                n7 = 100;
            }
            if (this.bm[n2].g > 0) {
                this.fh[this.bm[n2].g - 1] = 1;
            }
        } else {
            n7 = -1;
        }
        return n7;
    }

    public final void as() {
        int n2 = 0;
        int n3 = 0;
        n2 = 0;
        while (n2 < 4) {
            if (this.eJ[n2][1] > 0) {
                switch (this.eJ[n2][2]) {
                    case 2: {
                        break;
                    }
                    case 3: {
                        n3 = this.h(this.eJ[n2][3], this.eJ[n2][4]);
                        if (n3 > -1) {
                            this.eJ[n2][6] = this.eJ[n2][3] > 1 && this.eJ[n2][3] < 6 ? 1 : this.cq[n3][4];
                            if (this.eJ[n2][6] >= this.eJ[n2][5]) {
                                this.eJ[n2][22] = 1;
                                break;
                            }
                            this.eJ[n2][22] = 0;
                            break;
                        }
                        this.eJ[n2][6] = 0;
                        this.eJ[n2][22] = 0;
                        break;
                    }
                    case 4: {
                        if (this.eJ[n2][3] != this.bV || this.fd <= -1 || this.ff != this.eJ[n2][4]) break;
                        this.eJ[n2][22] = 1;
                        break;
                    }
                    case 5: {
                        break;
                    }
                    case 6: {
                        if (this.bV != this.eJ[n2][3] || this.eJ[n2][4] > this.cc || this.cc > this.eJ[n2][6] || this.eJ[n2][5] > this.cd || this.cd > this.eJ[n2][7]) break;
                        this.eJ[n2][22] = 1;
                    }
                }
            }
            ++n2;
        }
    }

    public final void at() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        n2 = 0;
        while (n2 < this.aV) {
            if (this.eJ[1][1] == 0 || this.eJ[2][1] == 0 || this.eJ[3][1] == 0) {
                n4 = this.aX[n2].q;
                n3 = this.eI[this.aX[n2].t];
                if (n4 <= n3 && n3 < n4 + 3 && this.aX[n2].v[n3 - n4][0] > 0) {
                    if (this.aX[n2].v[n3 - n4][1] <= this.eD) {
                        this.aX[n2].k = 1;
                        this.aX[n2].m = 1;
                    }
                } else {
                    this.aX[n2].k = 0;
                    this.aX[n2].m = 0;
                }
            } else {
                this.aX[n2].k = 0;
                this.aX[n2].m = 0;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            if (this.eJ[n2 + 1][1] > 0) {
                if (this.bV == this.eJ[n2 + 1][23]) {
                    this.aX[this.eJ[n2 + 1][24]].k = 2;
                    this.aX[this.eJ[n2 + 1][24]].m = 2;
                }
                if (this.eJ[n2 + 1][2] == 4 && this.bV == this.eJ[n2 + 1][3] && (n3 = this.r(this.eJ[n2 + 1][4])) > -1) {
                    this.aX[n3].k = 3;
                    this.aX[n3].m = 2;
                }
            }
            ++n2;
        }
    }

    public final void au() {
        int n2 = 0;
        while (n2 < this.aV) {
            if (this.aX[n2].k == 0) {
                this.aX[n2].m = 0;
            }
            if (this.eJ[0][2] == 4 && this.bV == this.eJ[0][3] && this.eJ[0][4] == this.aX[n2].j) {
                this.aX[n2].m = 2;
            }
            if (this.eF[12] == 4 && this.eF[0] == this.bV && this.eF[1] == this.aX[n2].j) {
                this.aX[n2].m = 2;
            }
            ++n2;
        }
    }

    public final void av() {
        boolean bl = false;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        boolean bl2 = false;
        boolean bl3 = false;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        if (this.aX[this.fd].k == 0) {
            n10 = this.aX[this.fd].n[0][0];
            n9 = this.aX[this.fd].n[0][1];
            n3 = 0;
            n2 = this.aX[this.fd].n[0][2] - 1;
            if (n9 != 0 || n10 != 0) {
                if (n10 > 0) {
                    if (this.eD > n10) {
                        n3 = 4;
                        n2 = this.aX[this.fd].n[1][2] - 1;
                        if (this.eD > this.aX[this.fd].n[1][0]) {
                            n3 = 5;
                            n2 = this.aX[this.fd].n[2][2] - 1;
                        }
                    }
                } else if (n9 > 0 && this.eH[this.aX[this.fd].r] > n9) {
                    n3 = 4;
                    n2 = this.aX[this.fd].n[1][2] - 1;
                    if (this.eH[this.aX[this.fd].r] > this.aX[this.fd].n[1][0]) {
                        n3 = 5;
                        n2 = this.aX[this.fd].n[2][2] - 1;
                    }
                }
            }
            this.eV = 0;
            this.eU = 0;
        } else {
            if (this.aX[this.fd].k == 1) {
                n3 = this.aX[this.fd].r + 1;
                n4 = this.aX[this.fd].t;
                n8 = this.aX[this.fd].q;
                n2 = this.aX[this.fd].v[this.eI[n4] - n8][0] - 1;
                n7 = this.fd;
                this.y(n2);
                this.eV = 0;
                n5 = 0;
                while (n5 < 4) {
                    this.eN[n5] = this.eM[18 + n5];
                    ++n5;
                }
            } else {
                n10 = 0;
                n9 = 0;
                this.eV = 0;
                while (n5 < 3 && !bl2) {
                    if (this.eJ[n5 + 1][1] > 0) {
                        if (this.eJ[n5 + 1][2] == 4) {
                            if (this.aX[this.fd].j == this.eJ[n5 + 1][4] && this.bV == this.eJ[n5 + 1][3]) {
                                n10 = 3;
                                n9 = n5 + 1;
                                bl2 = true;
                            } else if (this.fd == this.eJ[n5 + 1][24] && this.bV == this.eJ[n5 + 1][23]) {
                                n10 = 2;
                                n9 = n5 + 1;
                            }
                        } else if (this.fd == this.eJ[n5 + 1][24] && this.bV == this.eJ[n5 + 1][23]) {
                            if (this.eJ[n5 + 1][22] == 0) {
                                n10 = 2;
                                n9 = n5 + 1;
                            } else {
                                n10 = 3;
                                n9 = n5 + 1;
                                bl2 = true;
                            }
                        }
                    }
                    ++n5;
                }
                if (n10 > 0) {
                    this.eV = n10;
                    this.eW = n9;
                    n3 = this.eJ[n9][0] % 10;
                    n4 = this.eJ[n9][25];
                    n2 = this.eJ[n9][1] - 1;
                    n7 = this.eJ[n9][24];
                }
                n5 = 0;
                while (n5 < 4) {
                    this.eN[n5] = this.eJ[this.eW][18 + n5];
                    ++n5;
                }
            }
            this.eU = 1;
        }
        this.eQ = 0;
        this.eR = 0;
        this.fj = 1;
        this.bu = 22;
        this.eS = 1;
        this.bI = 1;
        this.b(n2, n3);
        n11 = this.eT;
        if (this.eU == 1) {
            switch (this.eV) {
                case 0: {
                    n11 = this.eN[0] / 2;
                    n12 = 0;
                    break;
                }
                case 2: {
                    n11 = this.eN[3] / 2;
                    n12 = this.eN[2] / 2;
                    break;
                }
                case 3: {
                    n12 = this.eN[3] / 2;
                    n11 = this.eT;
                    n6 = this.z(this.eW);
                    if (n6 == 1) {
                        if (this.eJ[this.eW][2] == 3) {
                            if (this.eJ[this.eW][3] < 11) {
                                this.b(this.eJ[this.eW][3], this.eJ[this.eW][4], this.eJ[this.eW][5], 1);
                            } else {
                                this.h(this.eJ[this.eW][3], this.eJ[this.eW][4], this.eJ[this.eW][5]);
                            }
                        }
                        int n13 = n3 - 1;
                        this.eH[n13] = this.eH[n13] + this.eJ[this.eW][8];
                        n5 = 0;
                        while (n5 < 26) {
                            this.eJ[this.eW][n5] = 0;
                            ++n5;
                        }
                        this.aw();
                        int n14 = n4;
                        this.eI[n14] = this.eI[n14] + 1;
                        if (this.bV == this.eJ[this.eW][3]) {
                            this.aX[n7].k = 0;
                            this.aX[n7].m = 0;
                        }
                        this.aX[this.fd].k = 0;
                        this.aX[this.fd].m = 0;
                        if (this.fh[50 + this.eW] > 0) {
                            int[] nArray = this.eL[this.fh[50 + this.eW]];
                            nArray[1] = nArray[1] + 1;
                        }
                        this.fh[50 + this.eW] = 0;
                        break;
                    }
                    this.eU = 0;
                    this.fj = 0;
                    this.bu = 23;
                    this.eS = 0;
                    this.fd = -1;
                    this.ff = 0;
                    if (n6 == -1) {
                        this.bO = 1;
                    } else if (n6 == -2) {
                        this.bO = 36;
                    } else if (n6 == -3) {
                        this.bO = 10;
                    }
                    this.g(this.bO, 0);
                    this.bM = 1;
                    this.bI = 1;
                    this.br = 1;
                    this.bp = 14;
                }
            }
        }
        if (this.bM == 0) {
            this.eQ = this.eR = n12;
            this.eT = n11;
            byte[] byArray = new byte[2];
            byte[] byArray2 = byArray;
            byArray[0] = this.eP[this.eQ].a;
            byArray2[1] = this.eP[this.eQ].b;
            this.a("em", 48);
            this.b("em", 7468);
            this.ab = this.c(0, 0);
            this.fc = this.r(byArray2[1]);
            if (byArray2[0] > 0) {
                this.ac = this.c(byArray2[0], 0);
            }
            this.p();
        }
    }

    public final void y(int n2) {
        int n3 = 0;
        int n4 = 0;
        byte[] byArray = new byte[22];
        if (this.aX[this.fd].r == 0) {
            n3 = 3961;
        } else if (this.aX[this.fd].r == 1) {
            n3 = 6601;
        } else if (this.aX[this.fd].r == 2) {
            n3 = 10135;
        }
        this.gJ = 1;
        n3 += n2 * 22;
        try {
            String string = "/data/WineData.dat";
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var9_8 = null;
            this.gD = inputStream = o._m(clazz, string);
            this.gE = new DataInputStream(this.gD);
            this.gE.skip(n3);
            this.gE.read(byArray, 0, 22);
            this.g();
        }
        catch (Exception exception) {}
        n4 = 0;
        while (n4 < 22) {
            this.eM[n4] = n4 >= 18 ? 4 + (n4 - 18) * 2 : byArray[n4] + 128;
            ++n4;
        }
        this.gJ = 0;
    }

    public final int z(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        boolean bl = false;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int[] nArray = new int[12];
        int[] nArray2 = new int[]{-1, -1, -1};
        n3 = 0;
        while (n3 < 5) {
            this.gt[n3] = "";
            ++n3;
        }
        n3 = 0;
        while (n3 < 3 && !bl) {
            n5 = 0;
            if (this.eJ[n2][9 + n3 * 3] > 0) {
                if (this.eJ[n2][9 + n3 * 3] < 13) {
                    n8 = this.eJ[n2][10 + n3 * 3];
                    if (this.bZ == 1) {
                        if (this.eJ[n2][9 + n3 * 3] == 7 || this.eJ[n2][9 + n3 * 3] == 8) {
                            n8 += 6;
                        } else if (this.eJ[n2][9 + n3 * 3] == 9) {
                            n8 += 4;
                        }
                    }
                    this.c(this.eJ[n2][9 + n3 * 3], n8, 0);
                    if (this.eJ[n2][9 + n3 * 3] < 7) {
                        n4 = 0;
                        while (n4 < 12) {
                            nArray[n4] = this.fn[n4];
                            ++n4;
                        }
                        if (this.eJ[n2][9 + n3 * 3] < 2 || this.eJ[n2][9 + n3 * 3] > 5) {
                            nArray[4] = this.eJ[n2][11 + n3 * 3];
                        }
                        n5 = this.d(nArray);
                        this.gt[n7] = this.fo;
                        this.gt[n7 + 1] = "\u83b7\u5f97" + this.eJ[n2][11 + n3 * 3] + "\u4e2a";
                        n7 += 2;
                    } else if (this.eJ[n2][9 + n3 * 3] == 11 || this.eJ[n2][9 + n3 * 3] == 12) {
                        n5 = this.g(this.eJ[n2][9 + n3 * 3], this.eJ[n2][10 + n3 * 3] + 1, this.eJ[n2][11 + n3 * 3]);
                        this.gt[n7] = this.fo;
                        this.gt[n7 + 1] = "\u83b7\u5f97" + this.eJ[n2][11 + n3 * 3] + "\u4e2a";
                        n7 += 2;
                    } else {
                        if (this.eJ[n2][9 + n3 * 3] == 7) {
                            this.cm[this.eJ[n2][10 + n3 * 3]] = 1;
                        } else if (this.eJ[n2][9 + n3 * 3] == 8) {
                            this.cp[this.eJ[n2][10 + n3 * 3]][0] = 6;
                            this.cp[this.eJ[n2][10 + n3 * 3]][1] = 28700;
                            this.cp[this.eJ[n2][10 + n3 * 3]][2] = 28700;
                        } else if (this.eJ[n2][9 + n3 * 3] == 9) {
                            this.cz[this.eJ[n2][10 + n3 * 3]] = 1;
                            if (this.bZ == 0) {
                                switch (this.eJ[n2][10 + n3 * 3]) {
                                    case 0: {
                                        this.ce[4] = this.ce[4] + 5;
                                        break;
                                    }
                                    case 1: {
                                        this.ec += 2;
                                        this.ed = 1;
                                        break;
                                    }
                                    case 2: {
                                        this.ce[6] = this.ce[6] + 5;
                                        break;
                                    }
                                    case 3: {
                                        this.ec += 2;
                                        this.ce[4] = this.ce[4] + 5;
                                        this.ce[6] = this.ce[6] + 5;
                                    }
                                }
                            } else {
                                switch (this.eJ[n2][10 + n3 * 3]) {
                                    case 0: {
                                        this.ce[7] = this.ce[7] + 5;
                                        break;
                                    }
                                    case 1: {
                                        this.ec += 2;
                                        this.ed = 1;
                                        break;
                                    }
                                    case 2: {
                                        this.ce[5] = this.ce[5] + 5;
                                        break;
                                    }
                                    case 3: {
                                        this.ec += 2;
                                        this.ce[5] = this.ce[5] + 5;
                                        this.ce[7] = this.ce[7] + 5;
                                    }
                                }
                            }
                            this.v(this.bZ);
                            this.w(this.bZ);
                            this.aj();
                        }
                        this.gt[n7] = this.fo;
                        ++n7;
                    }
                    if (n5 > -1) {
                        nArray2[n3] = n5;
                    } else {
                        bl = true;
                    }
                } else if (this.eJ[n2][9 + n3 * 3] == 13) {
                    if (this.ce[0] == 70 && this.eD < 78 && this.ce[1] + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3] >= this.ch) {
                        this.eJ[n2][10 + n3 * 3] = 0;
                        this.eJ[n2][11 + n3 * 3] = 0;
                    }
                    this.ce[1] = this.ce[1] + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3];
                    this.gt[n7] = "\u7ecf\u9a8c\u503c:" + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3];
                    ++n7;
                    n5 = 1;
                } else if (this.eJ[n2][9 + n3 * 3] == 14) {
                    this.ce[11] = this.ce[11] + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3];
                    this.gt[n7] = "\u91d1\u989d:" + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3];
                    ++n7;
                    n5 = 1;
                }
            } else {
                n5 = 1;
            }
            if (n5 <= -1) continue;
            ++n3;
        }
        this.gt[n7] = "\u606d\u559c!!!";
        if (bl) {
            n3 = 0;
            while (n3 < 3) {
                if (this.eJ[n2][9 + n3 * 3] > 0 && this.eJ[n2][9 + n3 * 3] < 13) {
                    if (this.eJ[n2][9 + n3 * 3] < 7) {
                        if (nArray2[n3] > -1) {
                            this.b(nArray2[n3], 0, this.eJ[n2][11 + n3 * 3], 0);
                        }
                    } else if ((this.eJ[n2][9 + n3 * 3] == 11 || this.eJ[n2][9 + n3 * 3] == 12) && nArray2[n3] > -1) {
                        this.h(this.eJ[n2][9 + n3 * 3], this.eJ[n2][10 + n3 * 3] + 1, this.eJ[n2][11 + n3 * 3]);
                    }
                }
                ++n3;
            }
            n6 = n5;
        } else {
            n6 = 1;
        }
        return n6;
    }

    public final void A(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        n3 = 0;
        while (n3 < 5) {
            this.gt[n3] = "";
            ++n3;
        }
        n3 = 0;
        while (n3 < 3) {
            if (this.eJ[n2][9 + n3 * 3] > 0) {
                if (this.eJ[n2][9 + n3 * 3] < 13) {
                    n5 = this.eJ[n2][10 + n3 * 3];
                    if (this.bZ == 1) {
                        if (this.eJ[n2][9 + n3 * 3] == 7 || this.eJ[n2][9 + n3 * 3] == 8) {
                            n5 += 6;
                        } else if (this.eJ[n2][9 + n3 * 3] == 9) {
                            n5 += 4;
                        }
                    }
                    this.c(this.eJ[n2][9 + n3 * 3], n5, 0);
                    if (this.eJ[n2][9 + n3 * 3] > 6 && this.eJ[n2][9 + n3 * 3] < 10) {
                        this.gt[n4] = this.fo;
                        ++n4;
                    } else {
                        this.gt[n4] = this.fo;
                        this.gt[n4 + 1] = this.eJ[n2][11 + n3 * 3] + "\u4e2a";
                        n4 += 2;
                    }
                } else if (this.eJ[n2][9 + n3 * 3] == 13) {
                    this.gt[n4] = "\u7ecf\u9a8c\u503c:" + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3];
                    ++n4;
                } else if (this.eJ[n2][9 + n3 * 3] == 14) {
                    this.gt[n4] = "\u91d1\u989d:" + this.eJ[n2][10 + n3 * 3] * this.eJ[n2][11 + n3 * 3] + "\u4e24";
                    ++n4;
                }
            }
            ++n3;
        }
    }

    public final void aw() {
        int n2 = 0;
        while (n2 < 2) {
            if (this.eJ[n2 + 1][1] == 0) {
                this.eK[n2 + 1] = this.eK[n2 + 2];
                this.eK[n2 + 2] = "";
                int n3 = 0;
                while (n3 < 26) {
                    this.eJ[n2 + 1][n3] = this.eJ[n2 + 2][n3];
                    this.eJ[n2 + 2][n3] = 0;
                    ++n3;
                }
                this.fh[50 + n2 + 1] = this.fh[50 + n2 + 2];
                this.fh[50 + n2 + 2] = 0;
            }
            ++n2;
        }
    }

    public final void ax() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 21;
        int[] nArray = new int[1080];
        this.gn = 0;
        nArray[0] = this.bY;
        nArray[1] = this.bZ;
        nArray[2] = this.ca;
        nArray[3] = this.cb;
        nArray[4] = this.cc;
        nArray[5] = this.cd;
        nArray[6] = this.cC;
        nArray[7] = this.bV;
        nArray[8] = this.ci;
        nArray[9] = this.cn;
        nArray[10] = this.co;
        nArray[11] = this.cr;
        nArray[12] = this.cs;
        nArray[13] = this.ct;
        nArray[14] = this.eD;
        nArray[15] = this.eG;
        nArray[16] = this.ch;
        nArray[17] = this.ec;
        nArray[18] = this.ed;
        nArray[19] = this.B;
        System.arraycopy(this.ce, 0, nArray, 21, 17);
        n4 += 17;
        n2 = 0;
        while (n2 < 5) {
            System.arraycopy(this.cj[n2], 0, nArray, n4, 3);
            n4 += 3;
            ++n2;
        }
        n2 = 0;
        while (n2 < 5) {
            System.arraycopy(this.ck[n2], 0, nArray, n4, 2);
            n4 += 2;
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            System.arraycopy(this.cl[n2], 0, nArray, n4, 2);
            n4 += 2;
            ++n2;
        }
        System.arraycopy(this.cm, 0, nArray, n4, 6);
        n4 += 6;
        n2 = 0;
        while (n2 < 6) {
            System.arraycopy(this.cp[n2], 0, nArray, n4, 3);
            n4 += 3;
            ++n2;
        }
        n2 = 0;
        while (n2 < 50) {
            System.arraycopy(this.cq[n2], 0, nArray, n4, 12);
            n4 += 12;
            ++n2;
        }
        System.arraycopy(this.cz, 0, nArray, n4, 4);
        n4 += 4;
        n2 = 0;
        while (n2 < 2) {
            n3 = 0;
            while (n3 < 30) {
                System.arraycopy(this.cA[n2][n3], 0, nArray, n4, 2);
                n4 += 2;
                ++n3;
            }
            ++n2;
        }
        System.arraycopy(this.eF, 0, nArray, n4, 13);
        n4 += 13;
        n2 = 0;
        while (n2 < 4) {
            System.arraycopy(this.eJ[n2], 0, nArray, n4, 26);
            n4 += 26;
            ++n2;
        }
        System.arraycopy(this.eH, 0, nArray, n4, 3);
        System.arraycopy(this.eI, 0, nArray, n4 += 3, 50);
        n4 += 50;
        n2 = 0;
        while (n2 < 15) {
            System.arraycopy(this.eL[n2], 0, nArray, n4, 2);
            n4 += 2;
            ++n2;
        }
        System.arraycopy(this.fh, 0, nArray, n4, 60);
        byte[] byArray = new byte[4320];
        n2 = 0;
        while (n2 < 1080) {
            this.a(byArray, n2 * 4, nArray[n2]);
            ++n2;
        }
        this.c.b(byArray);
        this.gn = 1;
        if (this.gm == 0) {
            this.gm = 1;
            this.aV();
        }
    }

    public final int ay() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 21;
        int[] nArray = new int[1080];
        byte[] byArray = new byte[4320];
        if ((byArray = this.c.a(byArray)) != null) {
            n3 = 0;
            while (n3 < 1080) {
                nArray[n3] = this.a(byArray, n3 * 4);
                ++n3;
            }
            this.bY = nArray[0];
            this.bZ = nArray[1];
            this.ca = nArray[2];
            this.cb = nArray[3];
            this.cc = nArray[4];
            this.cd = nArray[5];
            this.cC = nArray[6];
            this.bV = nArray[7];
            this.ci = nArray[8];
            this.cn = nArray[9];
            this.co = nArray[10];
            this.cr = nArray[11];
            this.cs = nArray[12];
            this.ct = nArray[13];
            this.eD = nArray[14];
            this.eG = nArray[15];
            this.ch = nArray[16];
            this.ec = nArray[17];
            this.ed = nArray[18];
            this.B = nArray[19];
            System.arraycopy(nArray, 21, this.ce, 0, 17);
            n5 += 17;
            n3 = 0;
            while (n3 < 5) {
                System.arraycopy(nArray, n5, this.cj[n3], 0, 3);
                n5 += 3;
                ++n3;
            }
            n3 = 0;
            while (n3 < 5) {
                System.arraycopy(nArray, n5, this.ck[n3], 0, 2);
                n5 += 2;
                ++n3;
            }
            n3 = 0;
            while (n3 < 3) {
                System.arraycopy(nArray, n5, this.cl[n3], 0, 2);
                n5 += 2;
                ++n3;
            }
            System.arraycopy(nArray, n5, this.cm, 0, 6);
            n5 += 6;
            n3 = 0;
            while (n3 < 6) {
                System.arraycopy(nArray, n5, this.cp[n3], 0, 3);
                n5 += 3;
                ++n3;
            }
            n3 = 0;
            while (n3 < 50) {
                System.arraycopy(nArray, n5, this.cq[n3], 0, 12);
                n5 += 12;
                ++n3;
            }
            System.arraycopy(nArray, n5, this.cz, 0, 4);
            n5 += 4;
            n3 = 0;
            while (n3 < 2) {
                n4 = 0;
                while (n4 < 30) {
                    System.arraycopy(nArray, n5, this.cA[n3][n4], 0, 2);
                    n5 += 2;
                    ++n4;
                }
                ++n3;
            }
            System.arraycopy(nArray, n5, this.eF, 0, 13);
            n5 += 13;
            n3 = 0;
            while (n3 < 4) {
                System.arraycopy(nArray, n5, this.eJ[n3], 0, 26);
                n5 += 26;
                ++n3;
            }
            System.arraycopy(nArray, n5, this.eH, 0, 3);
            System.arraycopy(nArray, n5 += 3, this.eI, 0, 50);
            n5 += 50;
            n3 = 0;
            while (n3 < 15) {
                System.arraycopy(nArray, n5, this.eL[n3], 0, 2);
                n5 += 2;
                ++n3;
            }
            System.arraycopy(nArray, n5, this.fh, 0, 60);
            n2 = 1;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void az() {
        Object var4_3;
        long l2;
        Object var1_1;
        if (this.gw == 1 && this.cJ == 0) {
            this.gw = 0;
            this.gx = 0;
        }
        if (this.bq == 1 && this.eC == 0 && this.fk == 0) {
            var1_1 = null;
            l2 = 0L;
            var4_3 = null;
            l2 = o._n();
            if (l2 - this.bo > 3000L) {
                this.bq = 0;
            }
        }
        if (this.br == 1) {
            this.bq = 1;
            var1_1 = null;
            l2 = 0L;
            var4_3 = null;
            this.bo = l2 = o._n();
        }
        if (this.bq != 0) return;
        if (this.eC == 1 || this.fk == 1) {
            if (this.fm == 0) {
                if (this.bs <= 29) return;
                this.bq = 3;
                return;
            }
            this.bq = 3;
            return;
        }
        if (this.dp != 1 && this.bt > 0) {
            this.bq = 3;
            return;
        }
        this.aW();
    }

    public final void l(int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        System.out.println("ItemKind:" + n2);
        if (n2 > 0) {
            if (n2 > 10) {
                this.ez = n2 == 11 || n2 == 12 ? 1 : 0;
                n4 = this.cA[n2 - 11][n3][0] - 1;
            } else if (n2 == 7) {
                n4 = n3;
                if (this.bZ == 1) {
                    n4 += 6;
                }
                this.ez = 1;
            } else {
                n4 = this.cq[n3][0];
                if (n2 > 1 && n2 < 6) {
                    this.ez = 0;
                    n5 = 0;
                    while (n5 < 12) {
                        this.fn[n5] = this.cq[n3][n5];
                        ++n5;
                    }
                } else {
                    this.ez = 1;
                }
            }
            this.c(n2, n4, 1);
            return;
        }
        this.fn[1] = 0;
    }

    private void aX() {
        ++this.ei;
        ++this.D;
        this.c(0, 0, this.k, this.l, 0xFFFFFF);
        switch (this.D / 30) {
            case 0: {
                this.a(this.u[0], this.m, this.n, 3);
                return;
            }
            case 1: {
                this.a(this.u[1], this.m, this.n, 3);
                return;
            }
            case 2: {
                this.c(0, 0, this.k, this.l, 0);
                this.a(this.u[2], this.m, this.n, 3);
                return;
            }
        }
        this.C = true;
        this.c(0, 0, this.k, this.l, 0);
        this.B(13813153);
        this.a("\u662f\u5426\u5f00\u59cb\u58f0\u97f3?", this.m, this.n - this.p / 2, 13813153, 17);
        this.a("\u662f", 0, this.l, 13813153, 36);
        this.a("\u5426", this.k, this.l, 13813153, 40);
    }

    public final void B(int n2) {
        this.e.setColor(n2);
    }

    public final void a(String string, int n2, int n3, int n4, int n5) {
        this.B(n4);
        this.e.drawString(string, n2, n3, n5);
    }

    public final void c(int n2, int n3, int n4, int n5, int n6) {
        this.B(n6);
        this.e.fillRect(n2, n3, n4, n5);
    }

    public final void aA() {
        this.f(0, 0, this.bx[0], this.bx[1], 0);
        this.a(this.ae[0], this.by[0] - this.ae[0].getWidth() / 2, 0, this.ae[0].getWidth(), this.ae[0].getHeight(), 0, 0);
        if (this.h % 10 < 5) {
            this.a(this.ae[1], this.by[0] - this.ae[1].getWidth() / 2, this.bx[1] - 45, this.ae[1].getWidth(), this.ae[1].getHeight(), 0, 0);
        }
    }

    public final void aB() {
        int n2 = 0;
        int n3 = 0;
        boolean bl = false;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int[] nArray = new int[10];
        int n11 = 0;
        int n12 = 0;
        String string = "";
        if (this.bx[1] < 170) {
            n4 = this.by[1] - 70;
            n5 = 28;
        } else {
            n4 = this.by[1] - 80;
            n5 = 32;
        }
        this.f(0, 0, this.bx[0], this.bx[1], 16645094);
        if (this.bx[1] > 160) {
            this.a(this.aa[0], 0, 0, 133, 86, 0, 0);
            this.a(this.aa[1], this.bx[0] - 118, this.bx[1] - 107, 118, 107, 0, 0);
        }
        switch (this.bu) {
            case 4: 
            case 5: 
            case 6: {
                n8 = this.bu == 4 ? this.au : this.aw;
                n9 = this.bu - 2;
                n2 = 0;
                while (n2 < this.av + 1) {
                    if (n8 == n2) {
                        if (this.at < 3) {
                            this.a(this.aa[7], this.by[0] - 50, n4 + n2 * n5, 96, 25, 0, 0);
                        } else if (this.at == 3) {
                            this.a(this.aa[7], this.by[0] - 50, n4 + n2 * n5, 25, 25, 0, 0);
                            this.a(this.aa[7], this.by[0] - 25, n4 + n2 * n5, 13, 25, 83, 0);
                            this.a(this.aa[7], this.by[0] + 12, n4 + n2 * n5, 25, 25, 0, 0);
                            this.a(this.aa[7], this.by[0] + 37, n4 + n2 * n5, 13, 25, 83, 0);
                        } else if (this.at == 4) {
                            this.a(this.aa[7], this.by[0] - 50, n4 + n2 * n5, 13, 25, 0, 0);
                            this.a(this.aa[7], this.by[0] - 35, n4 + n2 * n5, 13, 25, 83, 0);
                            this.a(this.aa[7], this.by[0] + 24, n4 + n2 * n5, 13, 25, 0, 0);
                            this.a(this.aa[7], this.by[0] + 37, n4 + n2 * n5, 13, 25, 83, 0);
                        }
                        if (this.at > 4) {
                            if (this.at < 10) {
                                n6 = (this.at - 4) * 5;
                                n7 = (this.at - 4) * 15;
                            } else {
                                n6 = 49;
                                n7 = 72;
                            }
                            this.a(this.aa[5], this.by[0] - 35 - n6, n4 - 6 + n2 * n5, n6, 30, 49 - n6, 0);
                            this.a(this.aa[6], this.by[0] + 27, n4 + 4 + n2 * n5, n7, 24, 0, 0);
                            if (this.at > 9) {
                                this.a(this.aa[10], this.by[0] - 64, n4 - 7 + n2 * n5, 8, 5, 0, 0);
                                this.a(this.aa[11], this.by[0] + 62, n4 + 20 + n2 * n5, 8, 5, 0, 0);
                            }
                            if (this.at > 10) {
                                this.a(this.aa[9], this.by[0] - 67, n4 + 13 + n2 * n5, 8, 5, 0, 0);
                                this.a(this.aa[12], this.by[0] + 80, n4 + 9 + n2 * n5, 9, 6, 0, 0);
                            }
                            if (this.at > 11) {
                                this.a(this.aa[8], this.by[0] - 50, n4 + 19 + n2 * n5, 8, 5, 0, 0);
                                this.a(this.aa[13], this.by[0] + 52, n4 + 3 + n2 * n5, 12, 8, 0, 0);
                            }
                        }
                    }
                    this.a(this.aa[n9], this.by[0] - 35, n4 + 4 + n2 * n5, 100, 20, 0, n2 * 20);
                    ++n2;
                }
                if (this.bu != 4) {
                    this.a(this.aa[30], this.bx[0] - 54, this.bx[1] - 15, 52, 13, 0, 0);
                }
                if (this.E == 1) {
                    boolean bl2 = false;
                    boolean bl3 = false;
                    int n13 = this.m - 50;
                    n3 = this.n - 50;
                    this.c(n13, n3, 100, 100, 14269571);
                    this.e(n13, n3, 100, 100, 0x442200);
                    this.a("\u6ca1\u6709\u8bb0\u5f55\u5b58\u5728!", 5, n13 + 50, n3 + 50 - this.p, 5318150, 17);
                    int n14 = this.o << 1;
                    int n15 = n13 + (100 - n14) / 2;
                    n3 = n3 + 100 - this.p - 3;
                    this.c(n15, n3, n14, this.p, 3165648);
                    this.e(n15, n3, n14, this.p, 7208896);
                    this.a("\u786e\u5b9a", n15, n3, 16771328, 0);
                    return;
                }
                if (this.dU != 1) break;
                this.f(this.by[0] - 70, this.by[1] - 55, 140, 150, 14269571);
                this.g(this.by[0] - 69, this.by[1] - 54, 138, 148, 0x442200);
                this.a("\u91cd\u65b0\u5f00\u59cb\u65f6\u5df2\u4fdd\u5b58\u7684\u6e38\u620f\u5c06\u4f1a\u4e22\u5931.\u8981\u7ee7\u7eed\u5417?", 5, this.by[0], this.by[1] - 40, 5318150, 17);
                n3 = this.by[1] + 20;
                n2 = 0;
                while (n2 < 2) {
                    if (n2 == this.dS) {
                        n10 = 16771328;
                        this.f(this.by[0] - 39 + n2 * 52, n3 + 45, 30, 27, 7208896);
                        this.f(this.by[0] - 38 + n2 * 52, n3 + 46, 28, 25, 3165648);
                    } else {
                        n10 = 8849673;
                    }
                    string = "\u662f";
                    if (n2 == 1) {
                        string = "\u5426";
                    }
                    this.b(string, this.by[0] - 25 + n2 * 52, n3 + 50, 1, n10);
                    ++n2;
                }
                break;
            }
            case 10: {
                this.m(170, 140);
                if (this.aw == 0) {
                    string = "\u64cd\u4f5c\u8bf4\u660e";
                } else if (this.aw == 1) {
                    string = "\u6545\u4e8b\u60c5\u8282";
                } else if (this.aw == 2) {
                    string = "\u5e2e\u52a9\u8bf4\u660e";
                }
                this.b(string, this.by[0], this.by[1] - 65, 1, 0xFFFF00);
                this.a(this.aa[16], this.by[0], this.by[1] - 35, 3, 5, 0, 0);
                this.a(this.aa[16], this.by[0] + 50, this.by[1] - 35, 3, 5, 3, 0);
                n11 = this.a(nArray, this.eR + 1);
                n12 = 0;
                while (n12 < n11) {
                    this.a(this.aj[2], this.by[0] + 15 - n12 * 4, this.by[1] - 36, 5, 7, nArray[n12] * 5, 0);
                    ++n12;
                }
                this.a(this.aa[28], this.by[0] + 25, this.by[1] - 36, 7, 7, 0, 0);
                n11 = this.a(nArray, this.eT);
                n12 = 0;
                while (n12 < n11) {
                    this.a(this.aj[2], this.by[0] + 40 - n12 * 4, this.by[1] - 36, 5, 7, nArray[n12] * 5, 0);
                    ++n12;
                }
                n2 = 0;
                while (n2 < 4) {
                    this.b(this.eO[this.eR * 4 + n2].a, this.by[0] - 80, this.by[1] - 20 + n2 * 21, 0, 0);
                    ++n2;
                }
                this.a(this.aa[30], this.bx[0] - 54, this.bx[1] - 15, 52, 13, 0, 0);
                break;
            }
            case 9: {
                this.aC();
                this.b("\u6e38\u620f\u8bbe\u7f6e", this.by[0], this.by[1] - 60, 1, 0xFFFF00);
                n2 = 0;
                while (n2 < 2) {
                    if (this.ax == n2) {
                        this.f(this.by[0] - 58, this.by[1] - 27 + n2 * 20, 116, 20, 16770235);
                        this.f(this.by[0] - 58, this.by[1] - 26 + n2 * 20, 116, 18, 14447118);
                        this.f(this.by[0] - 58, this.by[1] - 25 + n2 * 20, 116, 16, 16759159);
                    }
                    this.a(this.aa[14], this.by[0] - 40, this.by[1] - 23 + n2 * 20, 28, 14, 0, n2 * 14);
                    switch (n2) {
                        case 0: {
                            this.a(this.aa[15], this.by[0] + 10, this.by[1] - 23 + n2 * 20, 25, 14, 0, g * 14);
                            break;
                        }
                        case 1: {
                            this.b(Integer.toString(this.bJ + 1), this.by[0] + 20, this.by[1] - 23 + n2 * 20 - 3, 1, 0xFFFFFF);
                        }
                    }
                    this.a(this.aa[16], this.by[0], this.by[1] - 19 + n2 * 20, 3, 5, 0, 0);
                    this.a(this.aa[16], this.by[0] + 40, this.by[1] - 19 + n2 * 20, 3, 5, 3, 0);
                    ++n2;
                }
                this.a(this.aa[30], this.bx[0] - 54, this.bx[1] - 15, 52, 13, 0, 0);
                break;
            }
            case 11: {
                this.C(0);
                break;
            }
            case 8: {
                this.m(170, 140);
                string = "\u6e38\u620f\u5173\u4e8e";
                this.b(string, this.by[0], this.by[1] - 65, 1, 0xFFFF00);
                this.a(this.aa[16], this.by[0], this.by[1] - 35, 3, 5, 0, 0);
                this.a(this.aa[16], this.by[0] + 50, this.by[1] - 35, 3, 5, 3, 0);
                n11 = this.a(nArray, this.i);
                n12 = 0;
                while (n12 < n11) {
                    this.a(this.aj[2], this.by[0] + 15 - n12 * 4, this.by[1] - 36, 5, 7, nArray[n12] * 5, 0);
                    ++n12;
                }
                this.a(this.aa[28], this.by[0] + 25, this.by[1] - 36, 7, 7, 0, 0);
                n11 = this.a(nArray, 5);
                n12 = 0;
                while (n12 < n11) {
                    this.a(this.aj[2], this.by[0] + 40 - n12 * 4, this.by[1] - 36, 5, 7, nArray[n12] * 5, 0);
                    ++n12;
                }
                if (this.i == 1) {
                    this.b("\u53d1\u884c\u5546", this.by[0] - 80, this.by[1] - 20 + 0, 0, 0);
                    this.b("\u5146\u8363\u8054\u5408(\u5317\u4eac)", this.by[0] - 80, this.by[1] - 20 + 21, 0, 0);
                    this.b("\u79d1\u6280\u53d1\u5c55\u6709\u9650\u516c\u53f8", this.by[0] - 80, this.by[1] - 20 + 42, 0, 0);
                    this.b("\u5ba2\u670d\u90ae\u7bb1:", this.by[0] - 80, this.by[1] - 20 + 63, 0, 0);
                } else if (this.i == 2) {
                    this.b("service@zrong.cn", this.by[0] - 80, this.by[1] - 20 + 0, 0, 0);
                    this.b("\u5ba2\u670d\u7535\u8bdd\uff1a", this.by[0] - 80, this.by[1] - 20 + 21, 0, 0);
                    this.b("010-82212999", this.by[0] - 80, this.by[1] - 20 + 42, 0, 0);
                    this.b("\u5f00\u53d1\u5546:", this.by[0] - 80, this.by[1] - 20 + 63, 0, 0);
                } else if (this.i == 3) {
                    this.b("\u5927\u8fde\u963f\u91cc\u5965\u65af\u79d1", this.by[0] - 80, this.by[1] - 20 + 0, 0, 0);
                    this.b("\u6280\u54a8\u8be2\u6709\u9650\u516c\u53f8", this.by[0] - 80, this.by[1] - 20 + 21, 0, 0);
                    this.b("[\u5927\u8fde\u7389\u8861\u8f6f\u4ef6", this.by[0] - 80, this.by[1] - 20 + 42, 0, 0);
                    this.b("\u6709\u9650\u516c\u53f8]", this.by[0] - 80, this.by[1] - 20 + 63, 0, 0);
                } else if (this.i == 4) {
                    this.b("\u7f51\u5740\uff1a", this.by[0] - 80, this.by[1] - 20 + 0, 0, 0);
                    this.b("www.alioth.biz", this.by[0] - 80, this.by[1] - 20 + 21, 0, 0);
                    this.b("\u5ba2\u670d\u90ae\u7bb1\uff1a", this.by[0] - 80, this.by[1] - 20 + 42, 0, 0);
                    this.b("game@alioth.biz", this.by[0] - 80, this.by[1] - 20 + 63, 0, 0);
                } else if (this.i == 5) {
                    this.b("\u5ba2\u670d\u7535\u8bdd\uff1a", this.by[0] - 80, this.by[1] - 20 + 0, 0, 0);
                    this.b("0411-39750505-802", this.by[0] - 80, this.by[1] - 20 + 21, 0, 0);
                }
                this.a(this.aa[30], this.bx[0] - 54, this.bx[1] - 15, 52, 13, 0, 0);
            }
        }
        if (this.fW == 1) {
            if (this.fV < 255) {
                this.fV += 20;
            }
            if (this.fV > 255) {
                this.fV = 255;
                this.fW = 0;
                this.bI = 0;
            }
            this.f(0, 0, this.bx[0], this.bx[1], 0);
        } else if (this.at < 12) {
            ++this.at;
        }
        if (this.ei < 9999) {
            ++this.ei;
            return;
        }
        this.ei = 0;
    }

    public final void aC() {
        this.f(this.by[0] - 60, this.by[1] - 65, 120, 130, 14269571);
        this.g(this.by[0] - 59, this.by[1] - 64, 118, 128, 0x442200);
        this.f(this.by[0] - 59, this.by[1] - 64, 118, 25, 0x442200);
        this.f(this.by[0] - 58, this.by[1] - 63, 116, 23, 7028256);
    }

    public final void m(int n2, int n3) {
        this.f(this.by[0] - n2 / 2, this.by[1] - n3 / 2, n2, n3, 14269571);
        this.g(this.by[0] - n2 / 2 + 1, this.by[1] - n3 / 2 + 1, n2 - 2, n3 - 2, 0x442200);
        this.f(this.by[0] - n2 / 2 + 1, this.by[1] - n3 / 2 + 1, n2 - 2, 25, 0x442200);
        this.f(this.by[0] - n2 / 2 + 2, this.by[1] - n3 / 2 + 2, n2 - 4, 23, 7028256);
    }

    public final void C(int n2) {
        int n3 = 0;
        int n4 = 0;
        boolean bl = false;
        int n5 = 0;
        System.out.println("DrawPrologue");
        if (this.gh <= this.gg) {
            this.gh += 20;
            ++this.eR;
        } else {
            this.gh -= 2;
        }
        n3 = this.gh;
        n4 = this.eR;
        if (n2 == 1) {
            this.eT = 15;
        }
        while (!bl) {
            if (n3 < this.gf + 20 && n4 < this.eT - 1) {
                n5 = n3 < this.gg + 40 || n3 > this.gf - 40 ? (n2 == 0 ? 11447187 : 0x969696) : (n2 == 0 ? 6375738 : 0xFFFFFF);
                if (n2 == 0) {
                    this.b(this.eO[n4].a, this.by[0], n3, 1, n5);
                } else {
                    this.b(h.a[12][n4][3], this.by[0], n3, 1, n5);
                }
                n3 += 20;
                ++n4;
                continue;
            }
            bl = true;
        }
        this.f(0, 0, this.bx[0], this.gi, 0);
        this.f(0, this.bx[1] - this.gi, this.bx[0], this.gi, 0);
        if (n2 == 0) {
            this.a(this.aa[26], this.bx[0] - 35, this.bx[1] - 14, 34, 13, 0, 0);
            this.a(this.aa[18], 1, this.bx[1] - 14, 25, 13, 0, 0);
        }
        if (this.eR == this.eT) {
            if (n2 == 0) {
                this.bD = 0;
                this.ap = 4;
                this.fP = 1;
                this.fQ = 0;
                return;
            }
            ++this.eE;
        }
    }

    public final void aD() {
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        String string = "";
        int n7 = 0;
        n4 = this.bx[1] / 2 - 57;
        if (this.bI == 0 && this.eC == 0 && this.fk == 0) {
            this.as();
            n2 = this.ab();
            if (n2 == 0) {
                n2 = this.O();
            }
            if (n2 == 0) {
                n2 = this.P();
            }
        }
        this.br = 0;
        this.bp = 0;
        if (n2 == 0) {
            int n8;
            int n9;
            if (this.bI == 0) {
                if (this.eC == 1) {
                    this.ac();
                } else if (this.fk == 1) {
                    this.ad();
                } else {
                    n9 = 0;
                    while (n9 < 5) {
                        if (this.cj[n9][0] == 7 && this.cj[n9][2] == 0) {
                            n3 = this.ei - this.ck[n9][0];
                            if (n3 < 0) {
                                n3 += 9999;
                            }
                            if (this.ck[n9][1] < n3) {
                                this.cj[n9][2] = 1;
                            }
                        }
                        ++n9;
                    }
                    if (this.bZ == 0) {
                        n9 = 0;
                        while (n9 < 4) {
                            if (this.dN[n9][5] == 1) {
                                if (this.dN[n9][4] < 2) {
                                    int[] nArray = this.dN[n9];
                                    nArray[4] = nArray[4] + 1;
                                } else {
                                    this.dN[n9][5] = 0;
                                }
                            }
                            ++n9;
                        }
                    }
                    n6 = this.ai();
                    if (this.ce[1] >= this.ch && this.cx == 0) {
                        this.ce[0] = this.ce[0] + 1;
                        this.ci += 4;
                        if (this.ce[0] % 3 == 0 && this.ce[0] > 10) {
                            ++this.cn;
                        }
                        this.cy = this.ei;
                        this.cx = 1;
                        this.eb = 1;
                        this.ak();
                        this.v(this.bZ);
                        this.w(this.bZ);
                        this.aj();
                        this.ce[2] = this.cf;
                        this.ce[3] = this.cg;
                        this.br = 1;
                        this.bp = 7;
                    }
                }
                this.N();
                this.M();
                if (this.dp == 0) {
                    this.X();
                } else {
                    this.aa();
                }
                if (this.aT > 0) {
                    this.T();
                    this.U();
                    if (this.dp == 0) {
                        this.Q();
                    } else {
                        this.R();
                    }
                }
                if (this.aV > 0) {
                    this.S();
                }
            }
            if (this.en == 1) {
                n9 = 0;
                while (n9 < 5) {
                    int[] nArray = this.eo[n9];
                    nArray[0] = nArray[0] - 3;
                    ++n9;
                }
                if (this.eo[this.ep][0] < -50) {
                    this.l();
                }
            }
            if (this.dT > 0) {
                if (this.dU < 3) {
                    ++this.dU;
                    if (this.aJ[0] != 0) {
                        this.aJ[0] = 0;
                        this.aJ[1] = 0;
                    } else {
                        this.aJ[0] = 5 + (this.dT - 1) * 2;
                        this.aJ[1] = -5 + (this.dT - 1) * 2;
                    }
                } else {
                    this.dT = 0;
                    this.aJ[0] = 0;
                    this.aJ[1] = 0;
                }
            }
            if (this.bT == 0) {
                this.f(0, 0, this.bx[0], this.bx[1], 0);
                if (this.ds == 2) {
                    this.f(0, 0, this.bx[0], this.bx[1], 0xFFFFFF);
                }
                this.aH();
                if (this.eC != 1 && this.fk != 1 && this.fj != 1 && (this.bM <= 0 || this.bT != 0) && this.bM != 1) {
                    this.aG();
                }
                if (this.de == 1) {
                    this.f(0, 0, this.bx[0], this.bx[1], 0xFF00FF);
                }
                if (this.dp == 1) {
                    this.aN();
                }
                if (this.fR == 1) {
                    this.aJ();
                }
                this.aK();
                if (this.go > 0 && this.ei % 4 < 3) {
                    if (this.go == 1) {
                        string = "\u5185\u529b\u4e0d\u8db3!!";
                    } else if (this.go == 2) {
                        string = "\u5305\u88f9\u8d85\u91cd";
                    } else if (this.go == 3) {
                        string = "\u5305\u88f9\u7a7a\u95f4\u4e0d\u8db3";
                    } else if (this.go == 4) {
                        string = "\u8d85\u8fc7\u6700\u5927\u503c";
                    } else if (this.go == 5) {
                        string = "\u8b66\u544a\u88c5\u5907\u8010\u4e45\u5ea6!";
                    } else if (this.go == 6) {
                        string = "\u7834\u574f\u88c5\u5907!!";
                    }
                    n8 = this.j.stringWidth(string);
                    this.f(this.by[0] - n8 / 2, this.bx[1] - 20, n8, 22, 65535);
                    this.f(this.by[0] - n8 / 2 + 1, this.bx[1] - 19, n8 - 2, 20, 203);
                    this.b(string, this.by[0], this.bx[1] - 17, 1, 0xFFFF00);
                }
                if (this.eq == 0 && this.eC == 0) {
                    if (this.ci > 0 && this.ei % 4 < 2) {
                        this.a(this.al[6], this.bx[0] - 9 - 2, this.bx[1] - 72, 9, 9, 0, 0);
                    }
                    if (this.cn > 0 && this.ei % 4 < 2) {
                        this.a(this.al[6], this.bx[0] - 9 - 12, this.bx[1] - 72, 9, 9, 9, 0);
                    }
                }
            }
            if (this.aM > 0) {
                this.S(this.aM);
            }
            if (this.eh == 1) {
                this.aL();
            }
            if (this.gq > 0) {
                this.aM();
            }
            if (this.eC == 1 || this.fk == 1 || this.fj == 1) {
                this.aI();
            }
            if (this.bI == 0) {
                this.bT = 0;
                if (this.cF == 1) {
                    this.W();
                }
                this.ei = this.ei < 9999 ? ++this.ei : 0;
            } else {
                if (this.eq == 1) {
                    this.aP();
                }
                if (this.bM > 0 && this.bT == 0) {
                    this.I(this.bM);
                }
                if (this.bu == 25 || this.bu == 31) {
                    this.aR();
                }
                switch (this.bu) {
                    case 26: {
                        this.aS();
                        break;
                    }
                    case 27: 
                    case 30: 
                    case 32: 
                    case 35: 
                    case 37: 
                    case 40: {
                        n5 = 2;
                        n4 = this.by[1] - 65;
                        break;
                    }
                    case 28: {
                        n5 = 3;
                        n4 = this.by[1] - 65;
                        break;
                    }
                    case 29: {
                        this.K(this.by[1] - 65);
                        break;
                    }
                    case 33: 
                    case 34: 
                    case 36: {
                        this.aT();
                        break;
                    }
                    case 20: {
                        n5 = 1;
                        break;
                    }
                    case 21: {
                        this.K(n4);
                        break;
                    }
                    case 41: {
                        this.aE();
                    }
                }
                if (n5 > 0) {
                    this.q(n5 - 1, n4);
                }
                this.bT = 1;
                this.ej = this.ej < 9999 ? ++this.ej : 0;
            }
            if (this.fW == 1) {
                if (this.fX > 3 && this.fX < 9) {
                    n7 = this.fX == 6 ? 0 : (this.fX == 7 ? 0xFF0000 : (this.fX == 8 ? 16712158 : 0xFFFFFF));
                    this.f(0, 0, this.bx[0], this.bx[1], n7);
                } else if (this.fX < 4 && (this.fX != 0 || this.ei % 4 <= 1)) {
                    n8 = this.fT - this.aI[0];
                    int n10 = this.fU - this.aI[1];
                    n9 = 0;
                    while (n9 < 3) {
                        if (this.fm == 0) {
                            this.e.setColor(255, 0, 0);
                        } else {
                            this.e.setColor(255, 255, 255);
                        }
                        this.e.drawImage(this.w, n8, n10, 0);
                        ++n9;
                    }
                    if (this.fX == 1) {
                        int n11 = this.ga / 4;
                        n4 = n11 * (200 - (this.aI[1] + 15)) / (this.bx[0] - 20);
                        n9 = 0;
                        while (n9 < 4) {
                            if (this.fm == 0) {
                                this.e.setColor(255 - n9 * 30, 0, 0);
                            } else {
                                this.e.setColor(255 - n9 * 30, 255 - n9 * 30, 255 - n9 * 30);
                            }
                            this.e.drawLine(n8 + n9 * n11 + 3, n10 - n9 * n4 + 16, n8 + (n9 + 1) * n11, n10 - (n9 + 1) * n4);
                            if (this.fm == 0) {
                                this.e.setColor(255, 0, 0);
                            } else {
                                this.e.setColor(255, 255, 255);
                            }
                            ++n9;
                        }
                    }
                }
            }
            if (this.fe > -1) {
                this.aO();
            }
            if (this.ff != 0 && this.fj == 0) {
                if (this.fd > -1) {
                    this.av();
                }
            } else if (this.fg == 1) {
                if (this.x(this.fe) > -1) {
                    n9 = 0;
                    while (n9 < 4) {
                        if (this.eJ[n9][1] > 0 && this.eJ[n9][2] == 5 && this.bV == this.eJ[n9][3] && this.bm[this.fe].a == this.eJ[n9][4] && this.bm[this.fe].b == this.eJ[n9][5]) {
                            this.eJ[n9][22] = 1;
                        }
                        if (this.eJ[n9][2] == 3 && this.eJ[n9][3] == this.bm[this.fe].c && this.eJ[n9][4] == this.bm[this.fe].d) {
                            this.gq = n9 + 1;
                            this.gr = this.ei;
                        }
                        ++n9;
                    }
                    this.bm[this.fe].f = 0;
                    this.fg = 0;
                    this.fe = -1;
                    this.aM = 2;
                    this.aN = this.ei;
                    this.aO = 0;
                    this.br = 1;
                    this.bp = 9;
                } else {
                    this.fg = 0;
                    this.fe = -1;
                }
            } else if (this.fi == 1) {
                this.an();
                this.fi = 0;
            }
            if (this.bu == 38) {
                this.aF();
            } else {
                if (this.bu == 23 || this.bu == 22 || this.bu == 24) {
                    this.az();
                } else if (this.bq == 3) {
                    this.bq = 1;
                }
                if (this.gv == 1) {
                    n3 = this.ei - this.gu;
                    if (n3 < 0) {
                        n3 += 9999;
                    }
                    if (n3 > 800) {
                        this.gv = 0;
                        this.r(0, 0);
                    }
                }
            }
        } else if (n2 > 0) {
            if (n2 < 2) {
                this.aW();
                this.A();
                this.bu = 23;
                this.bv = 23;
                this.h();
                this.fP = 1;
                this.fQ = 0;
                this.a(1);
            } else {
                this.aW();
                if (this.fl == 6 && this.fm == 1) {
                    this.fm = 0;
                }
                if (this.fm == 1) {
                    this.fk = 1;
                } else {
                    this.eC = 1;
                }
                this.eE = 0;
                System.out.println("m_TuCount:" + this.fl);
                if (this.eF[0] != this.eF[5]) {
                    this.A();
                    this.bu = 23;
                    this.bv = 23;
                    this.bV = this.eF[5];
                    this.cc = this.eF[6];
                    this.cd = this.eF[7];
                    this.cD = this.cC = this.eF[8];
                    this.h();
                    if (this.fm == 1) {
                        this.a(this.fl, 1);
                        if (this.fl == 4) {
                            this.bY = 1;
                            this.bZ = 1;
                        }
                    } else {
                        if (this.fl == 6) {
                            this.f();
                            this.fl = 0;
                        }
                        this.i();
                    }
                    this.fP = 1;
                    this.fQ = 0;
                    this.a(1);
                } else {
                    this.bu = 23;
                    this.bv = 23;
                    this.i();
                }
            }
        }
        if (n6 == 1) {
            this.ap = 7;
        }
    }

    public final void aE() {
        int n2 = 0;
        int n3 = 0;
        this.f(this.by[0] - 60, this.by[1] - 50, 120, 100, 14269571);
        this.g(this.by[0] - 59, this.by[1] - 49, 118, 98, 0x442200);
        if (this.fh[57] != 14) {
            this.b(this.fo, this.by[0] - 50, this.by[1] - 40, 0, 5318150);
            this.b(String.valueOf(Integer.toString(this.fh[59])) + "\u4e2a", this.by[0] - 50, this.by[1] - 23, 0, 5318150);
        } else {
            this.b("\uac8c\uc784\uba38\ub2c8", this.by[0] - 50, this.by[1] - 40, 0, 5318150);
            this.b(String.valueOf(Integer.toString(this.fh[58] * this.fh[59] * 100)) + "\u4e24", this.by[0] - 50, this.by[1] - 23, 0, 5318150);
        }
        this.b("\ubc1b\uc73c\uc2dc\uaca0\uc2b5\ub2c8\uae4c?", this.by[0] - 50, this.by[1] - 6, 0, 5318150);
        n2 = 0;
        while (n2 < 2) {
            if (n2 == this.ey) {
                n3 = 16771328;
                this.f(this.by[0] - 50 + n2 * 52, this.by[1] + 14, 48, 23, 7208896);
                this.f(this.by[0] - 49 + n2 * 52, this.by[1] + 15, 46, 21, 3165648);
            } else {
                n3 = 8849673;
            }
            String string = "\uc608";
            if (n2 == 1) {
                string = "\uc544\ub2c8\uc694";
            }
            this.b(string, this.by[0] - 25 + n2 * 52, this.by[1] + 18, 1, n3);
            ++n2;
        }
    }

    public final void aF() {
        int n2 = 0;
        this.bT = 0;
        this.f(0, 0, this.bx[0], this.bx[1], 0);
        switch (this.eE) {
            case 0: {
                if (this.fV > 0) {
                    this.fV -= 20;
                }
                if (this.fV >= 0) break;
                this.fV = 0;
                ++this.eE;
                this.ea = 255;
                break;
            }
            case 1: {
                if (this.ea > 0) {
                    this.ea -= 20;
                }
                if (this.ea >= 0) break;
                this.ea = 0;
                ++this.eE;
                this.dY = this.ej;
                break;
            }
            case 2: {
                n2 = this.ej - this.dY;
                if (n2 < 0) {
                    n2 = 9999 + this.dY;
                }
                if (20 > n2) break;
                ++this.eE;
                break;
            }
            case 3: {
                if (this.ea < 255) {
                    this.ea += 20;
                }
                if (this.ea <= 255) break;
                this.aW();
                this.ap = 8;
                this.ea = 255;
                ++this.eE;
            }
        }
        if (this.eE > 0 && this.eE < 4) {
            this.b("\u5931\u53bb\u610f\u8bc6.", this.by[0], this.by[1] - 7, 1, 0xFFFFFF);
            this.b("\u54ce\u2026\u5230\u8fd9\u513f\u2026", this.by[0], this.by[1] + 13, 1, 0xFFFFFF);
        }
    }

    public final void aG() {
        this.e.drawImage(this.I, 0, this.bx[1] - 40, 36);
        this.e.drawImage(this.J, this.bx[0], this.bx[1] - 40, 40);
        this.e.drawImage(this.K, this.bx[0], 0, 24);
    }

    public final void aH() {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        boolean bl = false;
        int n8 = this.aI[0] % 20;
        int n9 = this.aI[1] % 20;
        this.bz = this.aI[0] / 20;
        this.bA = this.aI[1] / 20;
        int n10 = -n9;
        while (n10 < this.bx[1] + 100) {
            n2 = -n8;
            while (n2 < this.bx[0] + 20) {
                int n11;
                if (this.ds == 0) {
                    if (n10 < this.bx[1] - 20 && this.bA + n4 < this.bC[1] && n2 < this.bx[0] && this.bz + n3 < this.bC[0] && (n5 = this.aH[0][(this.bA + n4) * this.bC[0] + this.bz + n3]) > 0) {
                        this.a(this.M[this.ay[n5]], n2, n10, 20, 20, 0, 0);
                    }
                    if (this.en == 1 && n10 > -n9 && n10 < this.bx[1] && this.bA + n4 - 1 < this.bC[1] && n2 < this.bx[0] && this.bz + n3 < this.bC[0]) {
                        n11 = 0;
                        while (n11 < 5) {
                            int n12 = this.eo[n11][0] / 20;
                            int n13 = this.eo[n11][1] / 20;
                            if (n12 == this.bz + n3 && n13 == this.bA + n4 - 1) {
                                this.a(this.S, this.eo[n11][0] - this.aI[0] - 26, n10 - 20, 52, 15, 0, 0);
                            }
                            ++n11;
                        }
                    }
                }
                if (n10 > -n9 + 20 && this.bA + n4 - 2 < this.bC[1] && n10 < this.bx[1] + 20) {
                    if (n2 < this.bx[0] && this.bz + n3 < this.bC[0]) {
                        if (this.ds == 0) {
                            n5 = this.aH[1][(this.bA + n4 - 2) * this.bC[0] + this.bz + n3];
                            n6 = this.aH[2][(this.bA + n4 - 2) * this.bC[0] + this.bz + n3];
                            n7 = this.aH[3][(this.bA + n4 - 2) * this.bC[0] + this.bz + n3];
                            if (n5 > 0 && n6 / 100 == 0) {
                                this.d(n5, n6, n7, n2, n10 - 20);
                            }
                            n11 = 0;
                            while (n11 < 10) {
                                if (this.bm[n11].f == 1 && this.bm[n11].a == this.bz + n3 && this.bm[n11].b == this.bA + n4 - 2 && (this.bm[n11].i == 0 || this.bm[n11].i == 1 && this.ei % 2 == 0)) {
                                    n5 = this.bm[n11].j;
                                    this.a(this.al[n5], n2 + 10 - this.al[n5].getWidth() / 2, n10 - 20 - this.al[n5].getHeight(), this.al[n5].getWidth(), this.al[n5].getHeight(), 0, 0);
                                }
                                ++n11;
                            }
                        }
                        n11 = 0;
                        while (n11 < this.aT) {
                            if (this.aU[n11].ay == 1 && this.aU[n11].h == this.bz + n3 && this.aU[n11].i == this.bA + n4 - 2) {
                                this.G(n11);
                            }
                            ++n11;
                        }
                        n11 = 0;
                        while (n11 < this.aV) {
                            if (this.aX[n11].l == 1 && this.aX[n11].a == this.bz + n3 && this.aX[n11].b == this.bA + n4 - 2) {
                                this.H(n11);
                            }
                            ++n11;
                        }
                        if (this.bZ == 0) {
                            n11 = 0;
                            while (n11 < 10) {
                                if (this.dF[n11][3] == 1 && this.dF[n11][0] == this.bz + n3 && this.dF[n11][1] == this.bA + n4 - 2) {
                                    this.D(n11);
                                }
                                ++n11;
                            }
                            if (this.dp == 1) {
                                if (this.dr == 1 && this.dq == 1) {
                                    n11 = 0;
                                    while (n11 < 6) {
                                        if (this.du[n11][3] == 1 && this.du[n11][0] == this.bz + n3 && this.du[n11][1] == this.bA + n4 - 2) {
                                            this.a(this.al[5], n2 + 2, n10 - 20 - 10, 16, 8, 0, 0);
                                        }
                                        ++n11;
                                    }
                                } else if (this.dr == 2 && this.dq > 0) {
                                    n11 = 0;
                                    while (n11 < this.cm[2]) {
                                        int n14 = 0;
                                        while (n14 < 4) {
                                            if (this.dK[n11][n14][3] == 1 && this.dK[n11][n14][2] < 4 && this.dK[n11][n14][0] == this.bz + n3 && this.dK[n11][n14][1] == this.bA + n4 - 2) {
                                                this.d(n11, n14, n2, n10 - 20);
                                            }
                                            ++n14;
                                        }
                                        ++n11;
                                    }
                                }
                            }
                        }
                        n11 = 0;
                        while (n11 < 4) {
                            if (this.dN[n11][5] == 1 && this.dN[n11][2] == this.bz + n3 && this.dN[n11][3] == this.bA + n4 - 2) {
                                this.E(n11);
                            }
                            ++n11;
                        }
                    }
                    if (this.bX == 1 && this.cc + 1 == this.bz + n3 && this.cd == this.bA + n4 - 2) {
                        this.n(this.ca, this.cb);
                    }
                    if (this.gk > 0 && this.aT > 0) {
                        n11 = 0;
                        while (n11 < 10) {
                            if (this.bn[n11].e == 1 && this.bn[n11].b == this.bA + n4 - 2) {
                                if (this.bn[n11].d == 0) {
                                    if (this.bn[n11].a == this.bz + n3 && this.bz + n3 < this.bC[0]) {
                                        this.a(this.al[5], n2 + 2, n10 - 20 - 8, 16, 8, this.bn[n11].c / 10 * 16, 0);
                                    }
                                } else if (this.bn[n11].d == 1 && this.bn[n11].a + 1 == this.bz + n3) {
                                    n6 = this.bn[n11].c;
                                    this.a(this.am[n6], n2 - this.am[n6].getWidth() / 2 - 12, n10 - 20 - this.am[n6].getHeight(), this.am[n6].getWidth(), this.am[n6].getHeight(), 0, 0);
                                }
                            }
                            ++n11;
                        }
                    }
                    if (this.bH == 1 && n2 < this.bx[0]) {
                        n11 = 0;
                        while (n11 < 20) {
                            if (this.bl[n11].m == 1 && this.bl[n11].a + 1 == this.bz + n3 && this.bl[n11].b == this.bA + n4 - 2) {
                                this.F(n11);
                            }
                            ++n11;
                        }
                    }
                }
                if (this.ds == 0 && n10 > -n9 + 80 && this.bA + n4 - 5 < this.bC[1] && n2 < this.bx[0] && this.bz + n3 < this.bC[0]) {
                    n5 = this.aH[1][(this.bA + n4 - 5) * this.bC[0] + this.bz + n3];
                    n6 = this.aH[2][(this.bA + n4 - 5) * this.bC[0] + this.bz + n3];
                    n7 = this.aH[3][(this.bA + n4 - 5) * this.bC[0] + this.bz + n3];
                    if (n5 > 0 && n6 / 100 == 1) {
                        this.d(n5, n6, n7, n2, n10 - 80);
                    }
                }
                n2 += 20;
                ++n3;
            }
            n3 = 0;
            n10 += 20;
            ++n4;
        }
        if (this.eC != 1 && this.fk != 1) {
            int n15 = 0;
            int n16 = 0;
            int n17 = 0;
            n2 = 0;
            while (n2 < this.aP) {
                if (this.bU != 61 || n2 != 0) {
                    n15 = this.aQ[n2 * 9 + 0];
                    switch (n15) {
                        case 0: {
                            n17 = (this.aQ[n2 * 9 + 1] + this.aQ[n2 * 9 + 2]) / 2 * 20 - this.aI[0] + 10 + 3;
                            n16 = 0 - this.aI[1];
                            break;
                        }
                        case 1: {
                            n17 = (this.aQ[n2 * 9 + 1] + this.aQ[n2 * 9 + 2]) / 2 * 20 - this.aI[0] + 10 + 3;
                            n16 = this.bC[1] * 20 - 22 - this.aI[1];
                            break;
                        }
                        case 2: {
                            n17 = 6 - this.aI[0];
                            n16 = (this.aQ[n2 * 9 + 1] + this.aQ[n2 * 9 + 2]) / 2 * 20 - this.aI[1];
                            break;
                        }
                        case 3: {
                            n17 = this.bC[0] * 20 - 15 - this.aI[0];
                            n16 = (this.aQ[n2 * 9 + 1] + this.aQ[n2 * 9 + 2]) / 2 * 20 - this.aI[1];
                        }
                    }
                    if (this.h % 4 < 2) {
                        this.e.drawImage(this.L, n17, n16, 20);
                    } else {
                        this.e.drawImage(this.L, n17, n16 + 3, 20);
                    }
                }
                ++n2;
            }
        }
        n2 = 0;
        while (n2 < 3) {
            if (this.aK[n2] < n2) {
                int n18 = n2;
                this.aK[n18] = this.aK[n18] + 1;
            } else {
                this.aK[n2] = 0;
            }
            ++n2;
        }
    }

    public final void d(int n2, int n3, int n4, int n5, int n6) {
        int n7 = 0;
        int n8 = 0;
        boolean bl = false;
        boolean bl2 = false;
        n7 = n4 % 80 / 20;
        int n9 = this.N[this.az[n2 + this.aK[n7]]].getWidth();
        int n10 = this.N[this.az[n2 + this.aK[n7]]].getHeight();
        if (n3 % 10 == 1) {
            n8 = 10;
        }
        n6 -= n8;
        if (n4 / 80 == 0) {
            n5 -= (n9 - 20) / 2;
        } else if (n4 / 80 != 1) {
            n5 -= n9 - 20;
        }
        this.a(this.N[this.az[n2 + this.aK[n7]]], n5, n6 - n10, n9, n10, 0, 0);
    }

    public final void D(int n2) {
        int n3 = 0;
        int n4 = 0;
        boolean bl = false;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        n3 = this.dF[n2][0] * 20 + 10;
        n4 = (this.dF[n2][1] + 1) * 20;
        n5 = n3 - this.aI[0];
        n6 = n4 - this.aI[1];
        n7 = this.dF[n2][4] == 1 ? 20 : (this.dF[n2][4] == 2 ? 30 : 41);
        if (this.dF[n2][4] > 0) {
            this.a(this.ak[1], n5 - 9, n6 - n7, 19, n7 - 10, 0, 0);
        }
        this.a(this.ak[0], n5 - 10, n6 - 13, 20, 13, 0, 0);
    }

    public final void d(int n2, int n3, int n4, int n5) {
        int n6 = 0;
        int n7 = 0;
        if (this.dK[n2][n3][2] > 0) {
            switch (this.dK[n2][n3][2]) {
                case 1: {
                    n6 = 20;
                    n7 = 8;
                    break;
                }
                case 2: {
                    n6 = 32;
                    n7 = 44;
                    break;
                }
                case 3: {
                    n6 = 28;
                    n7 = 33;
                }
            }
            this.a(this.ad[this.dK[n2][n3][2] - 1], n4 - n6 / 2, n5 - n7, n6, n7, 0, 0);
        }
    }

    public final void E(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int[][] nArrayArray = new int[][]{{20, 8, 32, 44, 28, 33}, {23, 36, 32, 42, 31, 41}, {13, 75, 19, 89, 8, 69}};
        n5 = this.dN[n2][0] - this.aI[0];
        n6 = this.dN[n2][1] - this.aI[1];
        if (this.bZ == 0) {
            n3 = nArrayArray[(this.co - 1) % 3][this.dN[n2][4] * 2];
            n4 = nArrayArray[(this.co - 1) % 3][this.dN[n2][4] * 2 + 1];
            this.a(this.O[8 + this.dN[n2][4]], n5 - n3 / 2, n6 - n4, n3, n4, 0, 0);
            return;
        }
        if (this.bZ == 1) {
            this.a(this.O[9], n5 - 10, n6 - 20, 20, 20, this.ei % 2 * 20, 0);
        }
    }

    public final void F(int n2) {
        boolean bl = false;
        int n3 = this.bl[n2].g;
        int n4 = this.bl[n2].k;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        n8 = this.bl[n2].d - this.aI[0];
        n9 = this.bl[n2].e - this.aI[1];
        switch (n3) {
            case 0: {
                n6 = this.X[n4].getWidth() / 4;
                n5 = n6 * this.bl[n2].c;
                break;
            }
            case 1: {
                n6 = this.X[n4].getWidth();
                n5 = 0;
                break;
            }
            case 2: {
                n6 = this.X[n4].getWidth() / 2;
                n5 = n6 * (this.ei % 2);
            }
        }
        n7 = this.X[n4].getHeight();
        this.a(this.X[n4], n8 - (n6 >> 1), n9 - 10 - (n7 >> 1), n6, n7, n5, 0);
    }

    public final void n(int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 1;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int n13 = 0;
        int[] nArray = new int[10];
        boolean bl = false;
        int n14 = n2 - this.aI[0];
        int n15 = n3 - this.aI[1];
        if (this.cT == 1) {
            n5 = 2;
        } else if (this.cT == 2) {
            n5 = 6;
        }
        switch (this.cC) {
            case 2: {
                if (this.cT == 1) {
                    n4 = 3;
                    break;
                }
                if (this.cT != 2) break;
                n4 = 7;
                break;
            }
            case 3: {
                if (this.cT == 1) {
                    n4 = -3;
                    break;
                }
                if (this.cT != 2) break;
                n4 = -7;
            }
        }
        if (this.dH == 1) {
            this.a(this.ak[2], n14 - 13, n15 - 11, 26, 17, this.ei % 2 * 26, 0);
        }
        if (this.cF == 1 && (this.cI == 2 || this.cI == 3)) {
            n6 = 10;
        }
        if (this.cC == 1 && this.di > 0) {
            this.b(this.dh, this.cC, n14, n15 - 10, n6, this.di, this.dj);
        }
        if (this.cT == 0) {
            if (this.bY == 0) {
                this.o(n14, n15);
            } else {
                this.p(n14, n15);
            }
        } else {
            this.a(this.O[this.cC], n14 - 8, n15 - 10 - this.da, 17, 10, 0, 22);
            this.a(this.O[this.cC], n14 - 8 + n4, n15 - 32 + n5 - this.da, 17, 20, 0, 0);
        }
        if (this.cC != 1 && this.di > 0) {
            this.b(this.dh, this.cC, n14, n15 + 2, n6, this.di, this.dj);
        }
        if (this.dA == 1 || this.eb == 1) {
            this.a(this.U[this.ei % 2], n14 - 19, n15 - 14, 38, 14, 0, 0);
            if (this.dA == 1) {
                n6 = this.ei - this.dx;
                if (n6 < 0) {
                    n6 += 9999;
                }
                this.f(n14 - 16, n15 - 38, 32, 4, 0);
                this.f(n14 - 15, n15 - 37, 30, 2, 0x404040);
                if (n6 >= this.dz) {
                    n8 = this.ei % 2;
                    n7 = 30;
                } else {
                    n7 = n6 * 30 / this.dz;
                }
                if (n8 == 1) {
                    this.f(n14 - 15, n15 - 37, n7, 1, 3790591);
                    this.f(n14 - 15, n15 - 36, n7, 1, 32204);
                }
            }
        } else if (this.dA == 0 && this.cJ == 1 && this.cK == 0) {
            this.a(this.U[this.cL % 2], n14 - 19, n15 - 14, 38, 14, 0, 0);
        }
        int n16 = 0;
        while (n16 < 2) {
            if (this.dg[n16][1] == 1) {
                if (this.dg[n16][0] == 0) {
                    n10 = 12;
                    n11 = 12;
                } else if (this.dg[n16][0] == 1) {
                    n10 = 22;
                    n11 = 22;
                }
                n9 = n16 == 0 ? n15 - 20 : n15 - 5;
                this.a(this.T[this.dg[n16][0]], n14 - n10, n9 - n11, 30, 30, 0, 0);
            }
            ++n16;
        }
        n16 = 0;
        while (n16 < 5) {
            if (this.db[n16][0] > 0) {
                n13 = this.db[n16][3] - this.aI[1];
                if (this.db[n16][0] == 3) {
                    n12 = n16 == 2 || n16 == 3 ? this.db[n16][2] - this.aI[0] : this.db[n16][2] - this.aI[0] + (2 - n16) * 20;
                    this.a(this.T[3], n12 - this.T[3].getWidth() / 2, n13 - (this.T[3].getHeight() + 2), this.T[3].getWidth(), this.T[3].getHeight(), 0, 0);
                } else if (this.db[n16][0] == 1) {
                    n12 = this.db[n16][2] - this.aI[0];
                    this.a(this.T[2], n12 - this.T[2].getWidth() / 2, n13 - (this.T[2].getHeight() + 2) - (4 - n16) * 7, this.T[2].getWidth(), this.T[2].getHeight(), 0, 0);
                } else {
                    int n17 = this.a(nArray, this.db[n16][1]);
                    int n18 = 0;
                    while (n18 < n17) {
                        n12 = this.db[n16][2] - this.aI[0];
                        this.a(this.aj[3], n12 - n18 * 7, n13 - (this.aj[3].getHeight() + 2) - (4 - n16) * 7, 8, 8, nArray[n18] * 8, 0);
                        ++n18;
                    }
                }
            }
            ++n16;
        }
        if (this.cx == 1) {
            this.a(this.al[0], n14 - 27, n15 - 43 - this.ei % 2 * 2, 55, 13, 0, 0);
        }
    }

    public final void o(int n2, int n3) {
        boolean bl;
        boolean bl2;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int n17;
        block89: {
            block88: {
                n17 = 0;
                n16 = 0;
                n15 = 0;
                n14 = 0;
                n13 = 0;
                n12 = 0;
                n11 = 0;
                int[] nArray = new int[4];
                nArray[1] = 1;
                nArray[3] = 2;
                int[] nArray2 = nArray;
                n10 = 0;
                n9 = 0;
                n8 = 0;
                n7 = 0;
                n6 = 0;
                n5 = 40;
                n4 = 40;
                bl2 = false;
                bl = false;
                if (this.bH == 1) {
                    bl = true;
                }
                if (this.dA == 1) {
                    switch (this.dB) {
                        case 0: 
                        case 2: {
                            this.cC = 1;
                        }
                    }
                    this.cI = 1;
                }
                if (this.cI != 0) break block88;
                n16 = this.cC;
                n15 = 17;
                n14 = 33;
                n12 = n13 = nArray2[this.cB];
                n17 = n2 - 8;
                block3 : switch (this.cC) {
                    case 0: {
                        n8 = 4;
                        n10 = -17;
                        n9 = 12;
                        if (nArray2[this.cB] == 2) {
                            n10 = -16;
                            n9 = 13;
                            break;
                        }
                        break block89;
                    }
                    case 1: {
                        n8 = 5;
                        n10 = 4;
                        n9 = 11;
                        switch (nArray2[this.cB]) {
                            case 1: {
                                n9 = 14;
                                break block3;
                            }
                            case 2: {
                                n9 = 13;
                            }
                        }
                        break;
                    }
                    case 2: {
                        n8 = 2;
                        n10 = -9;
                        n9 = 12;
                        switch (nArray2[this.cB]) {
                            case 0: {
                                n9 = 11;
                                break block3;
                            }
                            case 1: {
                                n10 = -11;
                                break block3;
                            }
                            case 2: {
                                n10 = -7;
                            }
                        }
                        break;
                    }
                    case 3: {
                        n8 = 3;
                        n10 = -2;
                        n9 = 17;
                        switch (nArray2[this.cB]) {
                            case 0: {
                                n10 = -3;
                                break block3;
                            }
                            case 1: {
                                n9 = 18;
                                break block3;
                            }
                            case 2: {
                                n9 = 16;
                            }
                        }
                    }
                }
                break block89;
            }
            if (this.cC > 1) {
                n15 = 25;
                n14 = 28;
            } else {
                n15 = 18;
                n14 = 33;
            }
            switch (this.cC) {
                case 0: {
                    n10 = 4;
                    n9 = 27;
                    n8 = 1;
                    n17 = n2 - n15 / 2;
                    break;
                }
                case 1: {
                    n10 = -17;
                    n9 = 30;
                    n17 = n2 - n15 / 2;
                    break;
                }
                case 2: {
                    n10 = -1;
                    n9 = 21;
                    n8 = 1;
                    n17 = n2 - 17;
                    break;
                }
                case 3: {
                    n10 = -12;
                    n9 = 21;
                    n17 = n2 - 8;
                }
            }
            if (this.cI >= 4) {
                n12 = 0;
                n13 = 2 + (this.cI - 4) * 4 + this.cC;
                bl2 = true;
                n11 = 0;
                if (this.cI == 4) {
                    if (this.dr == 3 && this.dp == 1) {
                        bl = false;
                        switch (this.cC) {
                            case 0: {
                                n7 = n2 - 26;
                                n6 = n3 - 46;
                                n5 = 52;
                                n4 = 46;
                                break;
                            }
                            case 1: {
                                n7 = n2 - 35;
                                n6 = n3 - 45;
                                n5 = 53;
                                n4 = 45;
                                break;
                            }
                            case 2: {
                                n7 = n2 - 33;
                                n6 = n3 - 41;
                                n5 = 54;
                                n4 = 41;
                                break;
                            }
                            case 3: {
                                n7 = n2 - 20;
                                n6 = n3 - 41;
                                n5 = 54;
                                n4 = 41;
                            }
                        }
                    } else {
                        switch (this.cC) {
                            case 0: {
                                n7 = 9;
                                n6 = 30;
                                n5 = 20;
                                n4 = 22;
                                break;
                            }
                            case 1: {
                                n7 = 6;
                                n6 = 21;
                                n5 = 20;
                                n4 = 21;
                                break;
                            }
                            case 2: {
                                n7 = 18;
                                n6 = 21;
                                n5 = 20;
                                n4 = 21;
                                break;
                            }
                            case 3: {
                                n7 = 2;
                                n6 = 21;
                                n5 = 20;
                                n4 = 21;
                            }
                        }
                    }
                } else if (this.dr == 3 && this.dp == 1) {
                    bl = false;
                    switch (this.cC) {
                        case 0: {
                            n7 = n2 - 33;
                            n6 = n3 - 47;
                            n5 = 56;
                            n4 = 53;
                            break;
                        }
                        case 1: {
                            n7 = n2 - 28;
                            n6 = n3 - 38;
                            n5 = 57;
                            n4 = 55;
                            break;
                        }
                        case 2: {
                            n7 = n2 - 27;
                            n6 = n3 - 39;
                            n5 = 54;
                            n4 = 53;
                            break;
                        }
                        case 3: {
                            n7 = n2 - 27;
                            n6 = n3 - 39;
                            n5 = 54;
                            n4 = 53;
                        }
                    }
                } else {
                    switch (this.cC) {
                        case 0: {
                            n7 = -5;
                            n6 = 27;
                            n5 = 10;
                            n4 = 20;
                            break;
                        }
                        case 1: {
                            n7 = 17;
                            n6 = 29;
                            n5 = 12;
                            n4 = 23;
                            break;
                        }
                        case 2: {
                            n7 = 4;
                            n6 = 21;
                            n5 = 16;
                            n4 = 24;
                            break;
                        }
                        case 3: {
                            n7 = 12;
                            n6 = 21;
                            n5 = 16;
                            n4 = 24;
                        }
                    }
                }
            } else if (this.cI > 1) {
                n13 = this.cI - 2;
                n12 = n13 + 1;
                bl2 = true;
                if (this.cI == 2) {
                    n5 = 40;
                    n4 = 34;
                    switch (this.cC) {
                        case 0: {
                            n10 = -18;
                            n9 = 17;
                            n8 = 2;
                            n7 = 19;
                            n6 = 39;
                            break;
                        }
                        case 1: {
                            n10 = 6;
                            n9 = 18;
                            n8 = 3;
                            n7 = 20;
                            n6 = 37;
                            break;
                        }
                        case 2: {
                            n10 = -22;
                            n9 = 17;
                            n8 = 2;
                            n7 = 23;
                            n6 = 37;
                            break;
                        }
                        case 3: {
                            n10 = 9;
                            n9 = 17;
                            n8 = 3;
                            n7 = 17;
                            n6 = 37;
                        }
                    }
                } else {
                    n5 = 34;
                    n4 = 34;
                    switch (this.cC) {
                        case 0: {
                            n10 = 4;
                            n9 = 13;
                            n8 = 3;
                            n7 = 17;
                            n6 = 35;
                            break;
                        }
                        case 1: {
                            n10 = -14;
                            n9 = 12;
                            n8 = 2;
                            n7 = 14;
                            n6 = 33;
                            break;
                        }
                        case 2: {
                            n10 = -1;
                            n9 = 7;
                            n8 = 3;
                            n7 = 23;
                            n6 = 26;
                            break;
                        }
                        case 3: {
                            n10 = -12;
                            n9 = 7;
                            n8 = 2;
                            n7 = 11;
                            n6 = 26;
                        }
                    }
                }
                n11 = this.cC * n5;
            }
            n16 = 4 + this.cC;
        }
        if (this.dp == 1 && (this.dr == 0 || this.dr == 3)) {
            bl2 = false;
        }
        if (this.cC == 0 && bl2) {
            this.a(this.R[n13], n2 - n7, n3 - n6, n5, n4, n11, 0);
        }
        if ((this.cC == 0 || this.cC == 3) && bl) {
            this.a(this.Q, n2 + n10, n3 - n9, 13, 13, n8 * 13, 0);
        }
        if (this.dr == 3 && this.dp == 1 && this.cI > 3) {
            this.a(this.ad[this.cI - 4], n7, n6, n5, n4, 0, 0);
        } else {
            this.a(this.O[n16], n17, n3 - n14, n15, n14, n15 * n12, 0);
        }
        if (this.cC != 0 && bl2) {
            this.a(this.R[n13], n2 - n7, n3 - n6, n5, n4, n11, 0);
        }
        if ((this.cC == 1 || this.cC == 2) && bl) {
            this.a(this.Q, n2 + n10, n3 - n9, 13, 13, n8 * 13, 0);
        }
    }

    public final void p(int n2, int n3) {
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int[] nArray = new int[4];
        nArray[1] = 1;
        nArray[3] = 2;
        int[] nArray2 = nArray;
        int n10 = 0;
        int n11 = 0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (this.dA == 1) {
            switch (this.dB) {
                case 1: 
                case 2: {
                    this.cC = 1;
                }
            }
            this.cI = 1;
        }
        if (this.cI == 0) {
            n5 = this.cC;
            n6 = 17;
            n7 = 32;
            n9 = n8 = nArray2[this.cB];
            n4 = n2 - 8;
        } else {
            n5 = 4 + this.cC;
            if (this.cC > 1) {
                n6 = 22;
                n7 = 30;
            } else {
                n6 = 18;
                n7 = 32;
            }
            switch (this.cC) {
                case 0: {
                    n4 = n2 - n6 / 2;
                    break;
                }
                case 1: {
                    n4 = n2 - n6 / 2;
                    break;
                }
                case 2: {
                    n4 = n2 - 14;
                    break;
                }
                case 3: {
                    n4 = n2 - 8;
                }
            }
            if (this.cI >= 4) {
                n9 = 0;
                n8 = 8 + (this.cI - 4) * 4 + this.cC;
                bl3 = true;
                if (this.cI == 4) {
                    switch (this.cC) {
                        case 0: {
                            n10 = 12;
                            n11 = 31;
                            break;
                        }
                        case 1: {
                            n10 = 8;
                            n11 = 19;
                            break;
                        }
                        case 2: {
                            n10 = 17;
                            n11 = 12;
                            break;
                        }
                        case 3: {
                            n10 = 15;
                            n11 = 12;
                        }
                    }
                } else {
                    switch (this.cC) {
                        case 0: {
                            n10 = 14;
                            n11 = 29;
                            break;
                        }
                        case 1: {
                            n10 = 10;
                            n11 = 19;
                            break;
                        }
                        case 2: {
                            n10 = 14;
                            n11 = 20;
                            break;
                        }
                        case 3: {
                            n10 = 7;
                            n11 = 20;
                        }
                    }
                }
            } else if (this.cI > 1) {
                if (this.cU == 0) {
                    n8 = (this.cI - 2) * 4 + this.cC;
                    n9 = n8 / 4 + 1;
                    bl3 = true;
                    if (this.cI == 2) {
                        switch (this.cC) {
                            case 0: {
                                n10 = 10;
                                n11 = 39;
                                break;
                            }
                            case 1: {
                                n10 = 1;
                                n11 = 29;
                                break;
                            }
                            case 2: {
                                n10 = 18;
                                n11 = 13;
                                break;
                            }
                            case 3: {
                                n10 = 7;
                                n11 = 13;
                            }
                        }
                    } else {
                        switch (this.cC) {
                            case 0: {
                                n10 = 11;
                                n11 = 21;
                                break;
                            }
                            case 1: {
                                n10 = 15;
                                n11 = 15;
                                break;
                            }
                            case 2: {
                                n10 = 21;
                                n11 = 24;
                                break;
                            }
                            case 3: {
                                n10 = 2;
                                n11 = 24;
                            }
                        }
                    }
                } else if (this.cU == 1) {
                    n5 = 8;
                    bl3 = false;
                    n9 = this.cI - 2;
                    n6 = 34;
                    n7 = 32;
                    n4 = n2 - 17;
                    n10 = n9 == 0 ? 28 : 31;
                } else if (this.cU == 2) {
                    n5 = 8 + this.cC;
                    bl3 = false;
                    n9 = this.cI - 2;
                    switch (this.cC) {
                        case 0: {
                            n6 = 34;
                            n7 = 33;
                            n4 = n2 - 17;
                            break;
                        }
                        case 1: {
                            n6 = 27;
                            n7 = 33;
                            n4 = n2 - 13;
                            break;
                        }
                        case 2: {
                            n6 = 39;
                            n7 = 29;
                            n4 = n2 - 31;
                            break;
                        }
                        case 3: {
                            n6 = 39;
                            n7 = 29;
                            n4 = n2 - 9;
                        }
                    }
                } else if (this.cU == 3) {
                    n8 = (this.cI - 2) * 4 + this.cC;
                    n9 = n8 / 4 + 1;
                    bl3 = false;
                    switch (this.cC) {
                        case 0: {
                            n10 = 22;
                            n11 = 65;
                            break;
                        }
                        case 1: {
                            n10 = 9;
                            n11 = 41;
                            break;
                        }
                        case 2: {
                            n10 = 56;
                            n11 = 27;
                            break;
                        }
                        case 3: {
                            n10 = 22;
                            n11 = 27;
                        }
                    }
                }
            }
        }
        if (this.dp == 1 && this.dr == 0) {
            bl3 = false;
        }
        if (this.dL == 1) {
            this.a(this.ad[1], n2 - 14, n3 - 36, 28, 31, this.ei % 2 * 28, 0);
        }
        if (this.cC == 0 && bl3) {
            this.a(this.R[n8], n2 - n10, n3 - n11, 50, 50, 0, 0);
        }
        if (this.dp == 1 && this.dr == 3 && this.dq > 0) {
            if (this.ei % 2 == 0) {
                this.a(this.ad[1], n2 - 28, n3 - 18, 56, 20, 0, 0);
            } else {
                this.a(this.ad[2], n2 - 31, n3 - 18, 63, 22, 0, 0);
            }
        } else if (this.cU == 1) {
            this.a(this.O[10 + this.ei % 2], n2 - n10, n3 - 18, 100, 100, 0, 0);
        }
        if (this.dM == 0) {
            this.a(this.O[n5], n4, n3 - n7, n6, n7, n6 * n9, 0);
        } else if (this.dM == 1) {
            this.a(this.ad[0], n2 - 9, n3 - 33 - this.ei % 2 * 3, 19, 31, 0, 0);
        }
        if (this.cC != 0 && bl3) {
            this.a(this.R[n8], n2 - n10, n3 - n11, 50, 50, 0, 0);
        }
        if (this.cU == 3) {
            this.a(this.O[8 + n8], n2 - n10, n3 - n11, 100, 100, 0, 0);
        }
        if (this.dE == 1) {
            this.a(this.ak[0], n2 - 12, n3 - 8, 25, 10, 0, 20);
            n10 = this.ei % 3 < 2 ? 10 : 0;
            n11 = (this.ei % 3 + 1) * 8;
            this.a(this.ak[0], n2 - 12, n3 - n11, 25, 10, 0, n10);
        }
        if (this.dp == 1 && this.dr == 2 && this.dq > 0) {
            if (this.dS == 0) {
                this.a(this.ad[1], n2 - 14, n3 - 17, 4, 13, 0, 0);
                this.a(this.ad[1], n2 + 10, n3 - 17, 4, 13, 4, 0);
                return;
            }
            if (this.dS == 1) {
                this.a(this.ad[1], n2 - 14, n3 - 17, 4, 13, 0, 0);
                this.a(this.ad[1], n2 + 10, n3 - 17, 4, 13, 4, 0);
                this.a(this.ad[2], n2 - 19, n3 - 18, 4, 15, 0, 0);
                this.a(this.ad[2], n2 + 15, n3 - 18, 4, 15, 4, 0);
                return;
            }
            if (this.dS == 2) {
                this.a(this.ad[2], n2 - 19, n3 - 18, 4, 15, 0, 0);
                this.a(this.ad[2], n2 + 15, n3 - 18, 4, 15, 4, 0);
                this.a(this.ad[2], n2 - 25, n3 - 19, 5, 17, 0, 0);
                this.a(this.ad[2], n2 + 19, n3 - 19, 5, 17, 5, 0);
                return;
            }
            if (this.dS == 3) {
                this.a(this.ad[2], n2 - 25, n3 - 19, 5, 17, 0, 0);
                this.a(this.ad[2], n2 + 19, n3 - 19, 5, 17, 5, 0);
            }
        }
    }

    public final void G(int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11;
        int n12;
        int n13;
        int n14;
        int n15;
        int n16;
        int[] nArray;
        block66: {
            int[][] nArrayArray;
            int n17;
            int n18;
            int n19;
            int n20;
            int n21;
            int n22;
            block65: {
                nArray = new int[10];
                n16 = 0;
                n15 = 0;
                boolean bl = false;
                boolean bl2 = false;
                n14 = 0;
                n13 = 0;
                n12 = 0;
                n11 = 0;
                n10 = 0;
                n9 = 0;
                n8 = 0;
                n7 = 0;
                n6 = 0;
                n22 = 0;
                n21 = 0;
                n5 = 0;
                n4 = 0;
                int n23 = 0;
                int[] nArray2 = new int[4];
                nArray2[1] = 1;
                nArray2[3] = 2;
                int[] nArray3 = nArray2;
                n20 = 0;
                n3 = 0;
                n19 = 0;
                n18 = 0;
                int n24 = 0;
                int n25 = 0;
                int n26 = 0;
                n17 = 0;
                int[][] nArrayArray2 = new int[2][];
                int[] nArray4 = new int[12];
                nArray4[1] = 4;
                nArray4[2] = -4;
                nArray4[4] = 4;
                nArray4[7] = 4;
                nArray4[9] = 4;
                nArray4[10] = -4;
                nArrayArray2[0] = nArray4;
                int[] nArray5 = new int[12];
                nArray5[1] = -4;
                nArray5[2] = 4;
                nArray5[4] = -4;
                nArray5[7] = 4;
                nArray5[9] = 4;
                nArray5[10] = 4;
                nArrayArray2[1] = nArray5;
                nArrayArray = nArrayArray2;
                if (this.aU[n2].w == 5) {
                    n23 = this.ei % 2 * 3;
                }
                n13 = this.aU[n2].f - this.aI[0];
                n12 = this.aU[n2].g - this.aI[1];
                n11 = this.aU[n2].a;
                n24 = this.aU[n2].ar;
                n25 = this.aU[n2].av;
                if (this.aU[n2].w == 1) {
                    n21 = 2;
                } else if (this.aU[n2].w == 2) {
                    n21 = 6;
                }
                switch (n11) {
                    case 2: {
                        if (this.aU[n2].w == 1) {
                            n22 = 3;
                            break;
                        }
                        if (this.aU[n2].w != 2) break;
                        n22 = 7;
                        break;
                    }
                    case 3: {
                        if (this.aU[n2].w == 1) {
                            n22 = -3;
                            break;
                        }
                        if (this.aU[n2].w != 2) break;
                        n22 = -7;
                    }
                }
                if (this.aU[n2].T == 1) {
                    this.a(this.ad[1], n13 - 14, n12 - 36, 28, 31, this.ei % 2 * 28, 0);
                }
                if (this.aU[n2].D == 1) {
                    n6 = 10;
                }
                if (n11 == 1 && this.aU[n2].u > 0) {
                    this.b(this.aU[n2].t, n11, n13, n12 - 10, n6, this.aU[n2].u, this.aU[n2].v);
                }
                if (n25 == 0 || n25 == 1) {
                    if (this.aU[n2].B == 0) {
                        if (nArray3[this.aU[n2].d] == 0) {
                            n19 = this.aG[n24 * 40 + n11 * 2];
                            n18 = this.aG[n24 * 40 + n11 * 2 + 1];
                        } else {
                            n19 = this.aG[n24 * 40 + 8 + n11 * 2];
                            n18 = this.aG[n24 * 40 + 8 + n11 * 2 + 1];
                        }
                    } else if (this.aU[n2].C == 0) {
                        n19 = this.aG[n24 * 40 + 24 + n11 * 2];
                        n18 = this.aG[n24 * 40 + 24 + n11 * 2 + 1];
                    } else {
                        n19 = this.aG[n24 * 40 + 32 + n11 * 2];
                        n18 = this.aG[n24 * 40 + 32 + n11 * 2 + 1];
                    }
                    if (n25 == 0) {
                        n17 = n24 * 12 + n11;
                        n20 = this.W[n17].getWidth() / 2;
                        n3 = this.W[n17].getHeight();
                        n26 = nArray3[this.aU[n2].d] == 0 ? 0 : n20;
                    } else if (n25 == 1) {
                        n26 = 0;
                        n17 = nArray3[this.aU[n2].d] == 0 ? n24 * 12 + n11 : n24 * 12 + 4 + n11;
                        n20 = this.W[n17].getWidth();
                        n3 = this.W[n17].getHeight();
                    }
                } else if (n25 == 2) {
                    if (this.aU[n2].B == 0) {
                        n17 = n24 * 12 + n11;
                        n20 = this.W[n17].getWidth() / 3;
                        n3 = this.W[n17].getHeight();
                        n26 = nArray3[this.aU[n2].d] * n20;
                        n19 = this.aG[n24 * 40 + nArray3[this.aU[n2].d] * 8 + n11 * 2];
                        n18 = this.aG[n24 * 40 + nArray3[this.aU[n2].d] * 8 + n11 * 2 + 1];
                    } else {
                        n17 = n24 * 12 + (this.aU[n2].C + 1) * 4 + n11;
                        n20 = this.W[n17].getWidth();
                        n3 = this.W[n17].getHeight();
                        n26 = 0;
                        n19 = this.aG[n24 * 40 + 24 + this.aU[n2].C * 8 + n11 * 2];
                        n18 = this.aG[n24 * 40 + 24 + this.aU[n2].C * 8 + n11 * 2 + 1];
                    }
                } else if (n25 == 3) {
                    n11 = 0;
                    n17 = n24 * 12;
                    n20 = this.W[n17].getWidth();
                    n3 = this.W[n17].getHeight();
                    if (this.aU[n2].S == 0) {
                        n19 = this.aG[n24 * 40];
                        n18 = this.aG[n24 * 40 + 1];
                    } else if (this.aU[n2].S == 1) {
                        n19 = this.aG[n24 * 40 + 4];
                        n18 = this.aG[n24 * 40 + 5];
                    }
                    n26 = 0;
                }
                if (this.aU[n2].w != 0 && this.aU[n2].w != 5) break block65;
                this.a(this.W[n17], n13 - n19 + n23, n12 - n3 + n18, n20, n3, n26, 0);
                if (this.aU[n2].S == 1) {
                    this.a(this.W[n17 + 1], n13 - this.aG[n24 * 40 + 2] + n23, n12 - this.W[n17 + 1].getHeight() + this.aG[n24 * 40 + 3], this.W[n17 + 1].getWidth(), this.W[n17 + 1].getHeight(), 0, 0);
                }
                if (this.aU[n2].j != 9) break block66;
                n14 = this.ei - this.aU[n2].M;
                if (n14 < 0) {
                    n14 += 9999;
                }
                if (n14 >= 5) break block66;
                n8 = this.an.getWidth();
                n7 = this.an.getHeight();
                this.a(this.an, n13 - n8 / 10, n12 - n3 / 2 - n7 / 2, n8 / 5, n7, n14 * (n8 / 5), 0);
                if (n14 <= 0) break block66;
                this.a(this.an, n13 - n20 / 2 + 5 - n8 / 10, n12 - n3 + 8 - n7 / 2, n8 / 5, n7, n14 * (n8 / 5), 0);
                this.a(this.an, n13 - n20 / 2 + 5 - n8 / 10, n12 - 8 - n7 / 2, n8 / 5, n7, n14 * (n8 / 5), 0);
                this.a(this.an, n13 + n20 / 2 - 5 - n8 / 10, n12 - n3 + 10 - n7 / 2, n8 / 5, n7, (n14 - 1) * (n8 / 5), 0);
                this.a(this.an, n13 + n20 / 2 - 5 - n8 / 10, n12 - 10 - n7 / 2, n8 / 5, n7, (n14 - 1) * (n8 / 5), 0);
                break block66;
            }
            if (this.aU[n2].w < 3) {
                this.a(this.W[n17], n13 - n19, n12 - 10 - this.aU[n2].J + n18, n20, 10, 0, n3 - 10);
                this.a(this.W[n17], n13 - n19 + n22, n12 - n3 + n21 - this.aU[n2].J + n18, n20, n3 - 10, 0, 0);
            } else if (this.aU[n2].w < 5) {
                n22 = (this.co - 1) % 3 == 2 ? 9 : 12;
                this.a(this.P, n13 - n22, n12 - 40 - (this.aU[n2].w - 3) * 5, 100, 100, 0, 0);
            } else if (this.aU[n2].w < 8) {
                n8 = n20 / 2;
                n7 = n3 / 3;
                n10 = this.aU[n2].w - 6;
                n14 = 0;
                while (n14 < 3) {
                    this.a(this.W[n17], n13 - n19 + nArrayArray[n10][n14 * 4], n12 - n3 + n14 * n7 + nArrayArray[n10][n14 * 4 + 1], n8, n7, 0, n14 * n7);
                    this.a(this.W[n17], n13 - n19 + nArrayArray[n10][n14 * 4 + 2] + n8, n12 - n3 + n14 * n7 + nArrayArray[n10][n14 * 4 + 3], n20 - n8, n7, n8, n14 * n7);
                    ++n14;
                }
            }
        }
        if (n11 != 1 && this.aU[n2].u > 0) {
            this.b(this.aU[n2].t, n11, n13, n12 + 2, n6, this.aU[n2].u, this.aU[n2].v);
        }
        n14 = 0;
        while (n14 < 4) {
            if (this.aU[n2].o[n14][1] == 1) {
                if (this.aU[n2].o[n14][0] == 0) {
                    n8 = 12;
                    n7 = 12;
                } else if (this.aU[n2].o[n14][0] == 1) {
                    n8 = 22;
                    n7 = 22;
                }
                switch (n14) {
                    case 0: {
                        n10 = n13;
                        n9 = n12 - 20;
                        break;
                    }
                    case 1: {
                        n10 = n13;
                        n9 = n12 - 5;
                        break;
                    }
                    case 2: {
                        n10 = n13 - 7;
                        n9 = n12 - 13;
                        break;
                    }
                    case 3: {
                        n10 = n13 + 7;
                        n9 = n12 - 10;
                    }
                }
                this.a(this.T[this.aU[n2].o[n14][0]], n10 - n8, n9 - n7 - this.aU[n2].J, 30, 30, 0, 0);
            }
            ++n14;
        }
        n14 = 0;
        while (n14 < 5) {
            if (this.aU[n2].x[n14][0] > 0) {
                n4 = this.aU[n2].x[n14][3] - this.aI[1];
                if (this.aU[n2].x[n14][0] == 3) {
                    n5 = n14 == 2 || n14 == 3 ? this.aU[n2].x[n14][2] - this.aI[0] : this.aU[n2].x[n14][2] - this.aI[0] + (2 - n14) * 20;
                    this.a(this.T[3], n5 - this.T[3].getWidth() / 2, n4 - (this.T[3].getHeight() - 2), this.T[3].getWidth(), this.T[3].getHeight(), 0, 0);
                } else if (this.aU[n2].x[n14][0] == 2) {
                    n16 = this.a(nArray, this.aU[n2].x[n14][1]);
                    n15 = 0;
                    while (n15 < n16) {
                        n5 = this.aU[n2].x[n14][2] - this.aI[0];
                        this.a(this.aj[3], n5 - n15 * 7, n4 - (this.aj[3].getHeight() - 5) - (4 - n14) * 7, 8, 8, nArray[n15] * 8, 0);
                        ++n15;
                    }
                } else {
                    n5 = this.aU[n2].x[n14][2] - this.aI[0];
                    this.a(this.T[2], n5 - this.T[2].getWidth() / 2, n4 - (this.T[2].getHeight() - 5) - (4 - n14) * 7, this.T[2].getWidth(), this.T[2].getHeight(), 0, 0);
                }
            }
            ++n14;
        }
        if (this.aU[n2].w == 5) {
            this.a(this.U[2], n13 - 15, n12 - n3 - 18, 29, 16, this.aU[n2].O * 29, 0);
        }
        if (this.aU[n2].U == 1) {
            this.a(this.U[2], n13 - 15, n12 - n3 - 18, 29, 16, this.ei % 3 * 29, 0);
            if (this.dS == 3) {
                this.a(this.ad[1], n13 - 14, n12 - 17, 4, 13, 0, 0);
                this.a(this.ad[1], n13 + 10, n12 - 17, 4, 13, 4, 0);
                return;
            }
            if (this.dS == 2) {
                this.a(this.ad[1], n13 - 14, n12 - 17, 4, 13, 0, 0);
                this.a(this.ad[1], n13 + 10, n12 - 17, 4, 13, 4, 0);
                this.a(this.ad[2], n13 - 19, n12 - 18, 4, 15, 0, 0);
                this.a(this.ad[2], n13 + 15, n12 - 18, 4, 15, 4, 0);
                return;
            }
            if (this.dS == 1) {
                this.a(this.ad[2], n13 - 19, n12 - 18, 4, 15, 0, 0);
                this.a(this.ad[2], n13 + 15, n12 - 18, 4, 15, 4, 0);
                this.a(this.ad[2], n13 - 25, n12 - 19, 5, 17, 0, 0);
                this.a(this.ad[2], n13 + 19, n12 - 19, 5, 17, 5, 0);
                return;
            }
            if (this.dS == 0) {
                this.a(this.ad[2], n13 - 25, n12 - 19, 5, 17, 0, 0);
                this.a(this.ad[2], n13 + 19, n12 - 19, 5, 17, 5, 0);
            }
        }
    }

    public final void H(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int[] nArray = new int[4];
        nArray[1] = 1;
        nArray[3] = 2;
        int[] nArray2 = nArray;
        int n8 = 0;
        int n9 = 0;
        boolean bl = false;
        n3 = this.aX[n2].d - this.aI[0];
        n4 = this.aX[n2].e - this.aI[1];
        if (this.aX[n2].g == 1) {
            n5 = this.aX[n2].c;
            n6 = this.aX[n2].i - 1;
            n8 = this.Y[n6 + n5].getWidth() / 3;
            n9 = this.Y[n6 + n5].getHeight();
            n7 = nArray2[this.aX[n2].f] * n8;
        } else {
            n6 = this.aX[n2].i - 1;
            n4 -= this.aX[n2].s;
            if (this.aX[n2].g == 0) {
                n8 = this.Y[n6].getWidth();
                n9 = this.Y[n6].getHeight();
                switch (this.aX[n2].j) {
                    case 17: {
                        n3 -= 10;
                        n4 -= 6;
                        if (this.fl != 1 || this.eE <= 16) break;
                        n4 += 27;
                        break;
                    }
                    case 20: 
                    case 24: {
                        n3 -= 10;
                    }
                }
                if (this.fk == 1 && this.fl > 2 && this.aX[n2].j == 16) {
                    // empty if block
                }
            } else if (this.aX[n2].g == 2) {
                n8 = this.Y[n6].getWidth() / 2;
                n9 = this.Y[n6].getHeight();
                this.aX[n2].f = this.aX[n2].f < 3 ? ++this.aX[n2].f : 0;
                switch (this.aX[n2].j) {
                    case 10: 
                    case 11: 
                    case 12: {
                        n7 = n8 * (this.aX[n2].f % 2);
                        break;
                    }
                    default: {
                        if (this.aX[n2].j == 55) {
                            n3 -= 6;
                            n4 -= 3;
                        }
                        n7 = n8 * (this.aX[n2].f / 2);
                    }
                }
            }
            switch (this.aX[n2].j) {
                case 75: 
                case 76: {
                    n3 -= 10;
                }
            }
        }
        this.a(this.Y[n6 + n5], n3 - n8 / 2, n4 - n9, n8, n9, n7, 0);
        if ((this.fd != n2 || this.fj != 1 && this.eC != 1) && this.aX[n2].m > 0) {
            this.a(this.al[1], n3 - 7, n4 - n9 - 20, 14, 18, (this.aX[n2].m - 1) * 14, 0);
        }
    }

    public final void aI() {
        int n2;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0xFFFFFF;
        String string = "";
        if (this.eY > 0) {
            n4 = this.bx[1] / 5 + 1;
            n3 = 0;
            while (n3 < n4) {
                this.f(0, 2 - this.eX + n3 * 5, this.bx[0], this.eX * 2 + 1, 0);
                ++n3;
            }
        }
        if (this.fW == 1 && this.fX == 9) {
            this.f(0, 0, this.bx[0], this.bx[1], 0);
        }
        if (this.eS == 1) {
            this.f(0, this.bx[1] - 42, this.bx[0], 49, 5712384);
            n4 = this.bx[0] / 14;
            n3 = 0;
            while (n3 < n4) {
                this.a(this.Z[8], 7 + n3 * 14, this.bx[1] - 49, 14, 14, 14, 0);
                this.a(this.Z[8], 7 + n3 * 14, this.bx[1] - 14, 14, 14, 14, 28);
                ++n3;
            }
            this.a(this.Z[8], 0, this.bx[1] - 49, 14, 30, 0, 0);
            this.a(this.Z[8], 0, this.bx[1] - 19, 14, 19, 0, 23);
            this.a(this.Z[8], this.bx[0] - 14, this.bx[1] - 49, 14, 30, 28, 0);
            this.a(this.Z[8], this.bx[0] - 14, this.bx[1] - 19, 14, 19, 28, 23);
            byte[] byArray = new byte[2];
            byte[] byArray2 = byArray;
            byArray[0] = this.eP[this.eQ].a;
            byArray2[1] = this.eP[this.eQ].b;
            byte[] byArray3 = new byte[2];
            byte[] byArray4 = byArray3;
            byArray3[0] = this.eP[this.eQ].c;
            if (byArray4[0] > 0 && byArray4[0] < 90 && byArray2[1] != 49) {
                int n7;
                int n8;
                if (byArray2[1] > 1) {
                    if (this.fc < 100) {
                        n8 = this.aX[this.fc].d - this.aI[0];
                        n7 = this.aX[this.fc].e - this.aI[1];
                        n5 = this.Y[this.aX[this.fc].i - 1].getHeight() + 20 + this.aX[this.fc].s;
                    } else {
                        n8 = this.aU[this.fc - 100].f - this.aI[0];
                        n7 = this.aU[this.fc - 100].g - this.aI[1];
                        n5 = this.W[this.aU[this.fc - 100].ar * 12].getHeight() + 20;
                    }
                } else {
                    n8 = this.ca - this.aI[0];
                    n7 = this.cb - this.aI[1];
                    n5 = 53;
                }
                this.a(this.ab, n8, n7 - n5 + this.ei % 2 * 2, 23, 19, (byArray4[0] - 1) % 5 * 23, (byArray4[0] - 1) / 5 * 19);
            } else if (byArray4[0] > 90) {
                switch (byArray4[0]) {
                    case 91: {
                        n6 = 0xFFFF00;
                        break;
                    }
                    case 92: {
                        n6 = 65280;
                        break;
                    }
                    case 93: {
                        n6 = 65535;
                        break;
                    }
                    case 94: {
                        n6 = 0xFF96FF;
                        break;
                    }
                    case 95: {
                        n6 = 16750080;
                    }
                }
            }
            if (this.ac != null) {
                if (byArray2[0] > 0 && byArray2[0] < 3) {
                    this.a(this.ac, this.bx[0] - this.ac.getWidth(), this.bx[1] - 49 - this.ac.getHeight(), this.ac.getWidth(), this.ac.getHeight(), 0, 0);
                } else if (byArray2[0] > 0) {
                    this.a(this.ac, 0, this.bx[1] - 49 - this.ac.getHeight(), this.ac.getWidth(), this.ac.getHeight(), 0, 0);
                }
            }
            if (this.eP[this.eQ].e <= 2) {
                n2 = 0;
                while (n2 < this.eP[this.eQ].e) {
                    this.b(this.eP[this.eQ].d[n2], 5, this.bx[1] - 41 + n2 * 19, 0, n6);
                    ++n2;
                }
                this.F = 0;
            } else {
                System.out.println("m_DialogChecks111==" + this.eS);
                n2 = this.F * 2;
                while (n2 < Math.min(this.F * 2 + 2, this.eP[this.eQ].e)) {
                    this.b(this.eP[this.eQ].d[n2], 5, this.bx[1] - 41 + n2 % 2 * 19, 0, n6);
                    ++n2;
                }
            }
            if (this.fj == 0) {
                this.a(this.al[2], this.by[0] - 12, this.bx[1] - 49 - 13, 33, 13, 0, 0);
            }
        } else if (this.eS == 2) {
            System.out.println("m_DialogCheck==2");
            n2 = 0;
            while (n2 < this.eP[this.eQ].e) {
                this.b(this.eP[this.eQ].d[n2], this.by[0], this.by[1] - 10 + n2 * 19, 1, 0xFFFFFF);
                ++n2;
            }
        } else if (this.eS == 3) {
            System.out.println("m_DialogCheck==3");
            this.C(1);
        }
        if (this.gs == 1) {
            System.out.println("\u9009\u62e9\u5251\u4fa0&\u6b66\u5723");
            this.f(this.by[0] - 60, this.by[1] - 15, 120, 30, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 14, 118, 28, 3868928);
            n3 = 0;
            while (n3 < 2) {
                if (n3 == this.bZ) {
                    n6 = 16771328;
                    this.f(this.by[0] - 50 + n3 * 52, this.by[1] - 12, 48, 23, 7208896);
                    this.f(this.by[0] - 49 + n3 * 52, this.by[1] - 11, 46, 21, 3165648);
                } else {
                    n6 = 0xFFFFFF;
                }
                if (n3 == 0) {
                    string = "\u5251\u4fa0";
                } else if (n3 == 1) {
                    string = "\u6b66\u5723";
                }
                this.b(string, this.by[0] - 26 + n3 * 52, this.by[1] - 8, 1, n6);
                ++n3;
            }
        }
    }

    public final void b(int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9 = 0;
        if (n7 == 1) {
            switch (n3) {
                case 0: 
                case 1: {
                    n9 = n4 - 15;
                    break;
                }
                case 2: {
                    n9 = n4 - 11 + n6;
                    break;
                }
                case 3: {
                    n9 = n4 - 21 - n6;
                }
            }
            if (n2 == 0) {
                this.a(this.V[6], n9 + 9, n5 - 7, 15, 9, 0, 0);
                this.a(this.V[8], n9 + 3, n5 - 11, 11, 6, 0, 0);
                this.a(this.V[8], n9 + 23, n5 - 8, 11, 6, 0, 0);
                return;
            }
            if (n2 == 1) {
                this.a(this.V[7], n9, n5 - 6, 17, 8, 0, 0);
                this.a(this.V[8], n9 + 11, n5 - 6, 11, 6, 0, 0);
                this.a(this.V[8], n9 + 19, n5 - 9, 11, 6, 0, 0);
                return;
            }
            if (n2 == 2) {
                this.a(this.V[7], n9 + 5, n5 - 6, 17, 8, 0, 0);
                this.a(this.V[8], n9 + 16, n5 - 6, 11, 6, 0, 0);
            }
        }
    }

    public final void aJ() {
        boolean bl = false;
        int n2 = 0;
        boolean bl2 = false;
        int n3 = 0;
        boolean bl3 = false;
        int n4 = 0;
        if (this.ej % 2 == 0) {
            while (n2 < 30 && !bl2) {
                if (this.fS[n2][2] == 0) {
                    this.fS[n2][0] = this.s(0, this.bx[0]);
                    this.fS[n2][1] = 0;
                    this.fS[n2][2] = 1;
                    this.fS[n2][3] = this.s(0, 3) + 1;
                    this.fS[n2][4] = this.fS[n2][3] * (this.s(0, 2) + 1);
                    this.fS[n2][5] = 0;
                    bl2 = true;
                }
                ++n2;
            }
        }
        n3 = 0;
        while (n3 < 30) {
            if (this.fS[n3][2] == 1) {
                int[] nArray = this.fS[n3];
                nArray[1] = nArray[1] + this.fS[n3][4];
                if (this.fS[n3][0] >= this.bx[0] || this.fS[n3][0] < 0 || this.fS[n3][1] >= this.bx[1] - 40) {
                    this.fS[n3][2] = 0;
                }
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 30) {
            if (this.fS[n3][2] == 1) {
                if (this.fS[n3][3] == 1) {
                    n4 = 4;
                } else if (this.fS[n3][3] == 2) {
                    n4 = 8;
                } else if (this.fS[n3][3] == 3) {
                    n4 = 12;
                }
                this.e.setColor(200, 200, 200);
                this.e.drawLine(this.fS[n3][0] - n4 / 2, this.fS[n3][1], this.fS[n3][0] + n4 / 2, this.fS[n3][1]);
                this.e.drawLine(this.fS[n3][0], this.fS[n3][1] - n4 / 2, this.fS[n3][0], this.fS[n3][1] + n4 / 2);
                this.e.drawLine(this.fS[n3][0] - n4 / 4, this.fS[n3][1] - n4 / 4, this.fS[n3][0] + n4 / 4, this.fS[n3][1] + n4 / 4);
                this.e.drawLine(this.fS[n3][0] - n4 / 4, this.fS[n3][1] + n4 / 4, this.fS[n3][0] + n4 / 4, this.fS[n3][1] - n4 / 4);
            }
            ++n3;
        }
    }

    public final void aK() {
        int[] nArray = new int[10];
        int n2 = 0;
        int n3 = 0;
        int[] nArray2 = new int[5];
        nArray2[0] = 1;
        nArray2[1] = 3;
        nArray2[2] = 7;
        nArray2[3] = 9;
        int[] nArray3 = nArray2;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        boolean bl = false;
        int n10 = 0;
        int n11 = 0;
        Image image = null;
        if (this.bx[0] < 250) {
            n5 = 0;
            while (n5 < 2) {
                this.a(this.Z[7], this.by[0] - 90 - n5 * 30, this.bx[1] - 40, 30, 40, 0, 0);
                this.a(this.Z[7], this.by[0] + 60 + n5 * 30, this.bx[1] - 40, 30, 40, 0, 0);
                ++n5;
            }
        } else {
            n5 = 0;
            while (n5 < 4) {
                this.a(this.Z[7], this.by[0] - 90 - n5 * 30, this.bx[1] - 40, 30, 40, 0, 0);
                this.a(this.Z[7], this.by[0] + 60 + n5 * 30, this.bx[1] - 40, 30, 40, 0, 0);
                ++n5;
            }
        }
        this.f(this.by[0] - 64, this.bx[1] - 40, 128, 40, 0);
        this.g(this.by[0] - 64, this.bx[1] - 40, this.bx[0], 40, 5712384);
        n6 = this.ce[2] * 52 / this.cf;
        n7 = this.ce[3] * 52 / this.cg;
        n8 = this.ch != 0 ? this.ce[1] * 49 / this.ch : 0;
        n9 = this.cs * 49 / this.ct;
        if (this.dV != 1 || this.ei % 2 != 0) {
            this.a(this.Z[1], this.by[0] - 12 - n6, this.bx[1] - 39, n6, 7, 52 - n6, 0);
        }
        this.a(this.Z[2], this.by[0] + 64 - 52, this.bx[1] - 39, n7, 7, 0, 0);
        this.f(this.by[0] - 64, this.bx[1] - 32, 128, 1, 14920215);
        this.a(this.Z[3], this.by[0] - 15 - n8, this.bx[1] - 30, n8, 2, 49 - n8, 0);
        this.a(this.Z[4], this.by[0] + 64 - 49, this.bx[1] - 30, n9, 2, 0, 0);
        this.f(this.by[0] - 64, this.bx[1] - 32, 128, 1, 14920215);
        n5 = 0;
        while (n5 < 5) {
            this.a(this.ai[13], this.by[0] - 59 + n5 * 24, this.bx[1] - 24, 20, 22, 0, 0);
            if (this.cj[n5][0] != 0) {
                if (this.cj[n5][0] == 1) {
                    n4 = this.cj[n5][1];
                    image = this.af[this.cq[n4][7]];
                } else if (this.cj[n5][0] == 7) {
                    image = this.ag[this.cj[n5][1]];
                }
                this.a(image, this.by[0] - 58 + n5 * 24, this.bx[1] - 22, 20, 22, 0, 0);
                if (this.cj[n5][0] == 1) {
                    n2 = this.a(nArray, this.cq[this.cj[n5][1]][4]);
                    n3 = 0;
                    while (n3 < n2) {
                        this.a(this.aj[1], this.by[0] - 45 + n5 * 24 - n3 * 4, this.bx[1] - 10, 5, 7, nArray[n3] * 5, 0);
                        ++n3;
                    }
                } else if (this.cj[n5][0] == 7 && this.cj[n5][2] == 0) {
                    n11 = this.ei - this.ck[n5][0];
                    if (n11 < 0) {
                        n11 += 9999;
                    }
                    n10 = n11 * 18 / this.ck[n5][1];
                    this.a(this.Z[9], this.by[0] - 57 + n5 * 24, this.bx[1] - 22 + n10, 16, 18 - n10, 0, n10);
                }
            }
            this.a(this.aj[0], this.by[0] - 58 + n5 * 24, this.bx[1] - 23, 5, 7, nArray3[n5] * 5, 0);
            ++n5;
        }
        n2 = this.a(nArray, this.ce[0]);
        this.a(this.Z[0], this.by[0] - 15, this.bx[1] - 45, 30, 19, 0, 0);
        n3 = 0;
        while (n3 < 4 - n2) {
            this.a(this.Z[5], this.by[0] + 3 - n3 * 4, this.bx[1] - 31, 3, 5, 0, 0);
            ++n3;
        }
        n3 = 0;
        while (n3 < n2) {
            this.a(this.aj[0], this.by[0] + 3 - n3 * 4, this.bx[1] - 32, 5, 7, nArray[n3] * 5, 0);
            ++n3;
        }
    }

    public final void aL() {
        int[] nArray = new int[10];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        this.f(2, 2, 50, 22, 0);
        this.g(2, 2, 49, 21, 14208416);
        this.f(2, 17, 49, 1, 14208416);
        this.f(4, 19, 45, 2, 0x404040);
        n4 = this.aU[this.ef].ah * 35 / this.aU[this.ef].aJ[3];
        this.f(4, 19, n4, 1, 16733846);
        this.f(4, 20, n4, 1, 12782411);
        this.a(this.ai[5], 4, 3, 26, 13, 0, 0);
        n5 = this.aU[this.ef].l;
        if (n5 > 20) {
            n5 += n5 / 4;
        }
        n2 = this.a(nArray, n5);
        n3 = 0;
        while (n3 < 3 - n2) {
            this.a(this.Z[5], 34 - n3 * 4 + 5, 8, 3, 5, 0, 0);
            ++n3;
        }
        n3 = 0;
        while (n3 < n2) {
            this.a(this.aj[0], 34 - n3 * 4 + 5, 7, 5, 7, nArray[n3] * 5, 0);
            ++n3;
        }
    }

    public final void aM() {
        int[] nArray = new int[10];
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        this.f(this.bx[0] - 40, 0, 40, 13, 14920215);
        this.f(this.bx[0] - 38, 1, 38, 11, 0);
        n5 = this.gq - 1;
        n4 = this.eJ[n5][6] > this.eJ[n5][5] ? this.eJ[n5][5] : this.eJ[n5][6];
        n2 = this.a(nArray, n4);
        n3 = 0;
        while (n3 < n2) {
            this.a(this.aj[0], this.bx[0] - 28 - n3 * 4, 3, 5, 7, nArray[n3] * 5, 0);
            ++n3;
        }
        this.a(this.ai[8], this.bx[0] - 20, 3, 7, 7, 0, 0);
        n2 = this.a(nArray, this.eJ[n5][5]);
        n3 = 0;
        while (n3 < n2) {
            this.a(this.aj[0], this.bx[0] - 12 + n3 * 4, 3, 5, 7, nArray[n2 - n3 - 1] * 5, 0);
            ++n3;
        }
    }

    public final void aN() {
        block37: {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            int n10;
            int n11;
            int n12;
            int n13;
            int n14;
            int n15;
            block36: {
                n15 = 0;
                n14 = 0;
                n13 = 0;
                n12 = 0;
                n11 = this.ca - this.aI[0];
                n10 = this.cb - this.aI[1];
                n9 = 0;
                n8 = 0;
                n7 = 0;
                n6 = 0;
                n5 = 0;
                n4 = 0;
                n3 = 0;
                n2 = 1;
                if (this.bZ != 0) break block36;
                switch (this.dr) {
                    case 0: {
                        n5 = 0;
                        while (n5 < this.dv) {
                            if (this.du[n5][7] == 1) {
                                this.a(this.ad[0], this.du[n5][0], this.du[n5][1], 10, 10, this.ei % 2 * 10, 0);
                            }
                            ++n5;
                        }
                        return;
                    }
                    case 1: {
                        if (this.dq == 2) {
                            n5 = 0;
                            while (n5 < 6) {
                                if (this.du[n5][3] == 1) {
                                    n9 = this.du[n5][0] * 20 - this.aI[0];
                                    n8 = (this.du[n5][1] + 1) * 20 - this.aI[1];
                                    if (this.du[n5][2] < 3) {
                                        n8 -= (this.bx[1] - 40) / 4 * (3 - this.du[n5][2]);
                                    } else if (this.du[n5][2] > 3) {
                                        n6 = (this.du[n5][2] - 3) * 5;
                                    }
                                    this.a(this.ad[0], n9 - 1, n8 - 56 + n6, 21, 56 - n6, 0, 0);
                                    if (this.du[n5][2] > 3) {
                                        this.a(this.ad[1], n9 - 1, n8 - 9, 22, 12, 0, 0);
                                    }
                                }
                                ++n5;
                            }
                        }
                        break block37;
                    }
                    default: {
                        return;
                    }
                }
            }
            if (this.bZ == 1) {
                switch (this.dr) {
                    case 0: {
                        switch (this.cC) {
                            case 0: {
                                n15 = n11 - this.dt / 2;
                                n14 = 0;
                                n13 = this.dt;
                                n12 = n10 - 35;
                                n7 = 40;
                                n6 = 29;
                                n9 = -19;
                                n8 = -47;
                                n3 = n10 - 40;
                                break;
                            }
                            case 1: {
                                n15 = n11 - this.dt / 2;
                                n14 = n10 + 10;
                                n13 = this.dt;
                                n12 = this.bx[1] - n14;
                                n7 = 40;
                                n6 = 29;
                                n9 = -20;
                                n8 = -7;
                                n3 = this.bx[1] - n14;
                                break;
                            }
                            case 2: {
                                n15 = 0;
                                n14 = n10 - 12 - this.dt / 2;
                                n13 = n11 - 28;
                                n12 = this.dt;
                                n7 = 29;
                                n6 = 40;
                                n9 = -39;
                                n8 = -31;
                                n3 = n11 - 28;
                                break;
                            }
                            case 3: {
                                n15 = n11 + 20;
                                n14 = n10 - 12 - this.dt / 2;
                                n13 = this.bx[0] - n11 + 20;
                                n12 = this.dt;
                                n7 = 29;
                                n6 = 40;
                                n9 = 10;
                                n8 = -31;
                                n3 = this.bx[0] - n11 + 20;
                            }
                        }
                        this.f(n15, n14, n13, n12, 0xF0F0F0);
                        if (this.cm[0] > 4 && this.ei % 2 == 0) {
                            n2 = 2;
                        }
                        n4 = 0;
                        while (n4 < n2) {
                            n5 = 0;
                            while (n5 < n3 / 10 + 1) {
                                switch (this.cC) {
                                    case 0: {
                                        this.a(this.ad[0], n11 - this.dt / 2 + n4 * 3, n5 * 10, 3, 10, 0, 0);
                                        this.a(this.ad[0], n11 + this.dt / 2 - 3 - n4 * 3, n5 * 10, 3, 10, 3, 0);
                                        break;
                                    }
                                    case 1: {
                                        this.a(this.ad[0], n11 - this.dt / 2 + n4 * 3, n10 + 5 + n5 * 10, 3, 10, 0, 0);
                                        this.a(this.ad[0], n11 + this.dt / 2 - 3 - n4 * 3, n10 + 5 + n5 * 10, 3, 10, 3, 0);
                                        break;
                                    }
                                    case 2: {
                                        this.a(this.ad[0], n5 * 10, n10 - 12 - this.dt / 2 + n4 * 3, 10, 3, 0, 0);
                                        this.a(this.ad[0], n5 * 10, n10 - 12 + this.dt / 2 - 3 - n4 * 3, 10, 3, 0, 3);
                                        break;
                                    }
                                    case 3: {
                                        this.a(this.ad[0], n11 + 20 + n5 * 10, n10 - 12 - this.dt / 2 + n4 * 3, 10, 3, 0, 0);
                                        this.a(this.ad[0], n11 + 20 + n5 * 10, n10 - 12 + this.dt / 2 - 3 - n4 * 3, 10, 3, 0, 3);
                                    }
                                }
                                ++n5;
                            }
                            ++n4;
                        }
                        this.a(this.ad[1], n11 + n9, n10 + n8, n7, n6, n7 * (this.ei % 2), 0);
                        return;
                    }
                    case 1: {
                        if (this.dq <= 0) break;
                        n3 = this.dq;
                        if (n3 > 5) {
                            n3 = 5;
                        }
                        n5 = 0;
                        while (n5 < this.dv) {
                            if (this.du[n5][7] == 1) {
                                this.a(this.ad[2], this.du[n5][0] - 4, this.du[n5][1] - 14, 9, 14, (n3 - 1) * 9, 0);
                            }
                            ++n5;
                        }
                        break;
                    }
                }
            }
        }
    }

    public final void e(int n2, int n3, int n4, int n5, int n6) {
        this.B(n6);
        this.e.drawRect(n2, n3, n4, n5);
    }

    public final void a(String string, int n2, int n3, int n4, int n5, int n6) {
        if (string == null) {
            return;
        }
        n2 = n2 > string.length() ? string.length() : n2;
        int n7 = string.length();
        this.B(n5);
        int n8 = n7 % n2 == 0 ? n7 / n2 : n7 / n2 + 1;
        int n9 = 0;
        while (n9 < n8) {
            this.e.drawString(string.substring(n9 * n2, (n9 + 1) * n2 > n7 ? n7 : (n9 + 1) * n2), n3, n4 + n9 * this.p, n6);
            ++n9;
        }
    }

    public final void I(int n2) {
        boolean bl = false;
        int n3 = 0;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = false;
        System.out.println("drawWarnning:" + n2);
        if (n2 < 2) {
            this.d(this.A);
            return;
        }
        if (n2 == 2 || n2 == 3) {
            System.out.println("come here warning");
            this.g(this.by[0] - 70, this.by[1] - 65, 140, 130, 0xFFCC55);
            this.g(this.by[0] - 69, this.by[1] - 64, 138, 128, 0x442200);
            this.f(this.by[0] - 68, this.by[1] - 63, 136, 126, 3413504);
            if (n2 == 2) {
                n3 = 0;
                while (n3 < 5) {
                    this.b(this.eO[this.eR + n3].a, this.m, this.by[1] - 50 + n3 * 20, 1, 0xFFFF00);
                    ++n3;
                }
                if (this.eR > 0) {
                    this.a(this.ai[15], this.by[0] + 63, this.by[1] - 60, 5, 3, 0, 0);
                }
                if (this.eR < this.eT - 5) {
                    this.a(this.ai[15], this.by[0] + 63, this.by[1] + 57, 5, 3, 0, 2);
                    return;
                }
            } else if (n2 == 3) {
                this.b("\u4efb\u52a1\u5b8c\u6210", this.by[0], this.by[1] - 55, 1, 65535);
                n3 = 0;
                while (n3 < 5) {
                    this.b(this.gt[n3], this.by[0] - 55, this.by[1] - 35 + n3 * 18, 0, 0xFFFF00);
                    ++n3;
                }
            }
        }
    }

    private void d(String string) {
        int n2 = this.o * 7;
        int n3 = this.p * 6;
        int n4 = this.m - n2 / 2;
        int n5 = this.n - n3 / 2;
        this.f(n4, n5, n2 + 2, n3 + 2, 8407056);
        this.e(n4 - 1, n5 - 1, n2 + 2, n3 + 2, 5046253);
        boolean bl = false;
        int n6 = string.length();
        int n7 = n6 % 6 == 0 ? n6 / 6 : n6 / 6 + 1;
        int n8 = n4 + n2 / 2;
        int n9 = n5 + (n3 - n7 * this.p) / 2;
        this.a(string, 6, n8, n9, 0xFFFFFF, 17);
    }

    public final void aO() {
        if (this.bm[this.fe].c < 15) {
            this.f(0, 20, this.bx[0], 20, 8657);
            this.b(this.bm[this.fe].k, this.by[0], 23, 1, 0xFFFF00);
        }
    }

    public final void J(int n2) {
        int n3 = 6;
        this.f(this.by[0] - 55, n2 + 6, 73, 18, 13149034);
        this.f(this.by[0] - 54, n2 + 7, 71, 16, 3868928);
        if (this.eB == 0) {
            n3 = 0;
        }
        this.a(this.ai[19], this.by[0] - 42 + n3, n2 + 9, 53, 11, 0, this.eB * 12);
        this.a(this.ai[15], this.by[0] - 48, n2 + 10 + 3, 3, 5, 0, 0);
        this.a(this.ai[15], this.by[0] + 7, n2 + 10 + 3, 3, 5, 2, 0);
        this.a(this.aj[1], this.by[0] - 53, n2 + 9 + 3, 5, 7, 5, 0);
        this.a(this.aj[1], this.by[0] + 11, n2 + 9 + 3, 5, 7, 15, 0);
    }

    public final void q(int n2, int n3) {
        int[] nArray = new int[10];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        Image image = null;
        int n8 = 0;
        String string = null;
        this.f(this.by[0] - 60, n3 + 1, 120, 133, 14664086);
        this.f(this.by[0] - 59, n3 + 2, 118, 131, 5843463);
        if (this.bu != 40) {
            if (n2 == 0) {
                this.J(n3);
                this.a(this.ai[5], this.by[0] + 27, n3 + 5, 26, 13, 0, 109);
                n4 = this.a(nArray, this.cs);
                n5 = 0;
                while (n5 < n4) {
                    this.a(this.aj[2], this.by[0] + 22 - n5 * 4, n3 + 14 + 5, 5, 7, nArray[n5] * 5, 0);
                    ++n5;
                }
                this.a(this.ai[8], this.by[0] + 28, n3 + 14 + 5, 7, 7, 0, 0);
                n4 = this.a(nArray, this.ct);
                n5 = 0;
                while (n5 < n4) {
                    this.a(this.aj[2], this.by[0] + 52 - n5 * 4, n3 + 14 + 5, 5, 7, nArray[n5] * 5, 0);
                    ++n5;
                }
            } else if (n2 == 1) {
                this.f(this.by[0] - 50, n3 + 6, 100, 14, 13149034);
                this.f(this.by[0] - 49, n3 + 7, 98, 12, 3868928);
                if (this.bu != 28 && this.v != null) {
                    this.e.drawImage(this.v, this.by[0] - 48, n3 + 8, 0);
                }
                n4 = this.a(nArray, this.ce[11]);
                n5 = 0;
                while (n5 < n4) {
                    this.a(this.aj[2], this.by[0] + 40 - n5 * 4, n3 + 9, 5, 7, nArray[n5] * 5, 0);
                    ++n5;
                }
            } else if (n2 == 2) {
                this.J(n3);
                this.e.drawImage(this.v, this.by[0] + 18, n3 + 10, 0);
                n4 = this.a(nArray, this.ce[11]);
                n5 = 0;
                while (n5 < n4) {
                    this.a(this.aj[2], this.by[0] + 52 - n5 * 4, n3 + 14, 5, 7, nArray[n5] * 5, 0);
                    ++n5;
                }
            }
        }
        if (this.bu == 30 || this.bu == 37) {
            this.f(this.by[0] - 53, n3 + 24, 106, 90, 14664086);
            this.f(this.by[0] - 52, n3 + 25, 104, 88, 2494464);
            this.a(this.ai[13], this.by[0] - 43, n3 + 34, 47, 22, 0, 0);
            if (this.fn[1] < 11) {
                this.a(this.af[this.fn[7]], this.by[0] - 42, n3 + 36, 18, 18, 0, 0);
            } else {
                this.a(this.af[11], this.by[0] - 42, n3 + 36, 18, 18, 0, 0);
            }
            this.f(this.by[0] - 3, n3 + 37, 27, 16, 13149034);
            this.f(this.by[0] - 2, n3 + 38, 25, 14, 0);
            this.a(this.ai[15], this.by[0] + 8, n3 + 32, 5, 3, 0, 0);
            this.a(this.ai[15], this.by[0] + 8, n3 + 55, 5, 3, 0, 2);
            this.a(this.ai[15], this.by[0] - 8, n3 + 42, 3, 5, 0, 0);
            this.a(this.ai[15], this.by[0] + 26, n3 + 42, 3, 5, 2, 0);
            n4 = this.a(nArray, this.fF);
            n5 = 0;
            while (n5 < n4) {
                this.a(this.aj[1], this.by[0] + 12 - n5 * 5, n3 + 41, 5, 7, nArray[n5] * 5, 0);
                ++n5;
            }
            this.b("\u4e2a", this.by[0] + 33, n3 + 38, 0, 16771328);
            if (this.bu == 30) {
                if (this.fC != 2) {
                    this.b(this.fI + "\u4e24", this.by[0], n3 + 65, 1, 12647769);
                } else {
                    this.b("\u73b0\u91d1:" + Integer.toString(this.fI) + "\u5143", this.by[0], n3 + 65, 1, 12647769);
                }
            } else {
                this.b("\u8981\u4e22\u5f03\u5417\uff1f", this.by[0], n3 + 65, 1, 0xFFFFFF);
            }
            int n9 = 0;
            while (n9 < 2) {
                if (n9 == this.fH && this.fG == 1) {
                    n8 = 16771328;
                    this.f(this.by[0] - 50 + n9 * 52, n3 + 89, 48, 23, 7208896);
                    this.f(this.by[0] - 49 + n9 * 52, n3 + 90, 46, 21, 3165648);
                } else {
                    n8 = 0xFFFFFF;
                }
                string = n9 == 0 ? (this.bu == 30 ? (this.fC == 0 || this.fC == 2 ? "\u8d2d\u4e70" : "\u5356\u51fa") : "\u4e22\u5f03") : "\u53d6\u6d88";
                this.b(string, this.by[0] - 26 + n9 * 52, n3 + 93, 1, n8);
                ++n9;
            }
            return;
        }
        if (this.bu == 32) {
            if (this.fC == 0) {
                this.b("\u8981\u4f4f\u5bbf\u5417\uff1f", this.by[0], n3 + 40, 1, 0xFFFFFF);
                this.b("100\u4e24", this.by[0], n3 + 60, 1, 0xFFFFFF);
                int n10 = 0;
                while (n10 < 2) {
                    if (n10 == this.eu) {
                        n8 = 16771328;
                        this.f(this.by[0] - 50 + n10 * 52, n3 + 89, 48, 23, 7208896);
                        this.f(this.by[0] - 49 + n10 * 52, n3 + 90, 46, 21, 3165648);
                    } else {
                        n8 = 0xFFFFFF;
                    }
                    string = n10 == 0 ? "\u4f4f\u5bbf" : "\u53d6\u6d88";
                    this.b(string, this.by[0] - 39 + n10 * 52, n3 + 93, 0, n8);
                    ++n10;
                }
                return;
            }
            if (this.fC == 1) {
                this.b("\u6062\u590d\u4e86\u4f53\u529b\u548c", this.by[0], n3 + 40, 1, 0xFFFFFF);
                this.b("\u5185\u529b.", this.by[0], n3 + 60, 1, 0xFFFFFF);
                this.b("\u786e\u8ba4/\u53d6\u6d88", this.by[0], n3 + 85, 1, 0xFFFFFF);
                return;
            }
        } else {
            if (this.bu == 40) {
                int n11 = 0;
                while (n11 < 3) {
                    this.b(this.eO[n11].a, this.by[0], n3 + 40 + n11 * 20, 1, 0xFFFFFF);
                    ++n11;
                }
                return;
            }
            this.e.translate(0, 3);
            n7 = this.bu == 27 ? 10 : 15;
            int n12 = 0;
            while (n12 < n7) {
                int n13 = n12 % 5;
                int n14 = n3 + 24 + n12 / 5 * 23;
                this.a(this.ai[13], this.by[0] - 52 + n13 * 21, n14, 20, 22, 0, 0);
                if (this.bu == 27) {
                    if (this.fA[this.eA * 25 + n12 * 5 + 0] != 0) {
                        n6 = this.fA[this.eA * 25 + n12 * 5 + 3];
                        this.a(this.af[n6], this.by[0] - 51 + n13 * 21, n14 + 2, 18, 18, 0, 0);
                    }
                } else if (this.cv[this.eA * 5 + n12][0] != 0) {
                    if ((this.bu == 28 || this.eq == 1 && this.es == 2) && this.eB == 0) {
                        int n15 = 0;
                        while (n15 < 3) {
                            if (this.cl[n15][0] > 0 && this.eN[n15] == this.eA * 5 + n12) {
                                this.f(this.by[0] - 51 + n13 * 21, n14 + 1, 18, 20, 0x990000);
                            }
                            ++n15;
                        }
                    }
                    if (this.cv[this.eA * 5 + n12][0] == 7) {
                        image = this.ag[this.cv[this.eA * 5 + n12][1]];
                    } else if (this.cv[this.eA * 5 + n12][0] == 11 || this.cv[this.eA * 5 + n12][0] == 12) {
                        image = this.af[11];
                    } else {
                        n6 = this.cq[this.cv[this.eA * 5 + n12][1]][7];
                        image = this.af[n6];
                    }
                    this.a(image, this.by[0] - 51 + n13 * 21, n14 + 2, 18, 18, 0, 0);
                    if (this.cv[this.eA * 5 + n12][0] == 1 || this.cv[this.eA * 5 + n12][0] == 6) {
                        n4 = this.a(nArray, this.cq[this.cv[this.eA * 5 + n12][1]][4]);
                        n5 = 0;
                        while (n5 < n4) {
                            this.a(this.aj[1], this.by[0] - 38 + n13 * 21 - n5 * 4, n14 + 14, 5, 7, nArray[n5] * 5, 0);
                            ++n5;
                        }
                    } else if (this.cv[this.eA * 5 + n12][0] > 10) {
                        n4 = this.a(nArray, this.cA[this.cv[this.eA * 5 + n12][0] - 11][this.cv[this.eA * 5 + n12][1]][1]);
                        n5 = 0;
                        while (n5 < n4) {
                            this.a(this.aj[1], this.by[0] - 38 + n13 * 21 - n5 * 4, n14 + 14, 5, 7, nArray[n5] * 5, 0);
                            ++n5;
                        }
                    }
                }
                if (n12 == this.ew) {
                    this.a(this.ai[14], this.by[0] - 52 + n13 * 21, n14, 20, 22, 0, 0);
                }
                ++n12;
            }
            n8 = this.fn[1] > 1 && this.fn[1] < 6 && this.fn[0] > 14 ? 65280 : 0xFFFFFF;
            if (this.bu == 27) {
                this.f(this.by[0] - 50, n3 + 75, 100, 42, 13149034);
                this.f(this.by[0] - 49, n3 + 76, 98, 40, 2494464);
                if (this.fn[1] > 0) {
                    this.b(this.fo, this.by[0], n3 + 78, 1, n8);
                    string = Integer.toString(this.fp);
                    string = this.bu == 27 ? String.valueOf(string) + "\u4e24" : String.valueOf(string) + "\u5143";
                    this.b(string, this.by[0], n3 + 98, 1, 0xFFFFFF);
                }
            } else {
                this.f(this.by[0] - 50, n3 + 95, 100, 22, 13149034);
                this.f(this.by[0] - 49, n3 + 96, 98, 20, 2494464);
                if (this.fn[1] > 0) {
                    this.b(this.fo, this.by[0], n3 + 98, 1, n8);
                }
            }
            this.a(this.ai[10], this.by[0] - 50, n3 + 118, 53, 11, 0, 24);
            if (this.et == 2) {
                this.a(this.ai[10], this.by[0] + 14, n3 + 118, 53, 11, 0, 36);
            }
            this.e.translate(0, -3);
        }
    }

    public final void K(int n2) {
        int[] nArray = new int[10];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        Image image = null;
        boolean bl = false;
        boolean bl2 = false;
        int n9 = 0;
        boolean bl3 = false;
        this.f(this.by[0] - 60, n2 - 6, 120, 142, 14664086);
        this.f(this.by[0] - 59, n2 + 1 - 6, 118, 140, 5843463);
        if (this.fn[1] < 9) {
            if (this.fn[1] < 7) {
                image = this.af[this.fn[7]];
            } else if (this.fn[1] == 7) {
                image = this.ag[this.fn[7]];
            } else if (this.fn[1] == 8) {
                image = this.ah[this.fn[7] % 3];
            }
            this.a(this.ai[13], this.by[0] - 56, n2 + 7 - 6, 20, 22, 0, 0);
            this.a(image, this.by[0] - 55, n2 + 9 - 6, 18, 18, 0, 0);
            if (this.fn[1] == 8 && this.fn[7] > 2) {
                this.a(this.ah[3], this.by[0] - 54, n2 + 9 - 6, 16, 18, 0, 0);
            }
        } else if (this.fn[1] > 10) {
            this.a(this.ai[13], this.by[0] - 56, n2 + 7 - 6, 20, 22, 0, 0);
            this.a(this.af[11], this.by[0] - 55, n2 + 9 - 6, 18, 18, 0, 0);
        }
        int n10 = this.fn[1] > 1 && this.fn[1] < 6 && this.fn[0] > 14 ? 65280 : 0xFFFF00;
        this.b(this.fo, this.by[0] - 35, n2 + 10 - 6, 0, n10);
        this.f(this.by[0] - 58, n2 + 34 - 5, 116, 90, 15258029);
        this.f(this.by[0] - 57, n2 + 35 - 5, 114, 88, 3868928);
        System.out.println("m_ST_SubDataMode:" + this.ez);
        if (this.ez == 0) {
            if (this.fn[1] < 11) {
                int n11 = 0;
                while (n11 < 5) {
                    switch (n11) {
                        case 0: {
                            n7 = 0;
                            n5 = this.by[0] - 50;
                            n6 = n2 + 38;
                            n8 = this.fq;
                            bl3 = false;
                            break;
                        }
                        case 1: {
                            n5 = this.by[0] - 50;
                            n6 = n2 + 51;
                            if (this.fn[1] == 2 || this.fn[1] == 3) {
                                n8 = this.fn[3] % 10000;
                                n7 = 28;
                                bl3 = true;
                                break;
                            }
                            if (this.fn[1] == 4) {
                                n8 = this.fn[2] % 10000;
                                n7 = 42;
                                bl3 = false;
                                break;
                            }
                            if (this.fn[1] != 5) break;
                            n8 = this.fn[2] % 10000;
                            n7 = 56;
                            bl3 = false;
                            break;
                        }
                        case 2: {
                            n7 = 110;
                            n5 = this.by[0] - 50;
                            n6 = n2 + 66;
                            n8 = this.fn[5];
                            bl3 = false;
                            break;
                        }
                        case 3: {
                            n7 = 122;
                            n5 = this.by[0] - 50;
                            n6 = n2 + 81;
                            n8 = this.fn[6];
                            bl3 = true;
                            break;
                        }
                        case 4: {
                            n7 = 98;
                            n5 = this.by[0] + 7;
                            n6 = n2 + 38;
                            n8 = this.fn[4];
                        }
                    }
                    if (n11 != 4) {
                        n3 = this.a(nArray, n8);
                        n4 = 0;
                        while (n4 < n3) {
                            this.a(this.aj[0], n5 + 29 + n4 * 4, n6 - 2, 5, 7, nArray[n3 - 1 - n4] * 5, 0);
                            ++n4;
                        }
                        if (bl3) {
                            n8 = n11 == 1 ? this.fn[2] : this.fr;
                            this.a(this.ai[8], n5 + 51, n6 - 2, 7, 7, 0, 0);
                            n3 = this.a(nArray, n8);
                            n4 = 0;
                            while (n4 < n3) {
                                this.a(this.aj[0], n5 + 62 + n4 * 4, n6 - 2, 5, 7, nArray[n3 - 1 - n4] * 5, 0);
                                ++n4;
                            }
                        }
                    } else {
                        n3 = this.a(nArray, this.fn[4]);
                        n4 = 0;
                        while (n4 < n3) {
                            this.a(this.aj[1], n5 + 29 + n4 * 4, n6 - 3, 5, 7, nArray[n3 - 1 - n4] * 5, 0);
                            ++n4;
                        }
                    }
                    this.a(this.ai[5], n5 - 3, n6 - 5, 26, 12, 0, n7);
                    this.a(this.ai[8], n5 + 26 - 4, n6 - 3, 7, 7, 14, 0);
                    ++n11;
                }
                if (this.fn[1] > 1 && this.fn[1] < 6) {
                    this.a(this.ai[5], this.by[0] + 7, n2 + 66 - 5, 26, 12, 0, 133);
                    this.a(this.ai[8], this.by[0] + 26 + 7, n2 + 66 - 2, 7, 7, 14, 0);
                    n8 = this.fn[1] < 4 ? this.fn[3] / 10000 : this.fn[2] / 10000;
                    this.a(this.ai[20], this.by[0] + 35 + 7, n2 + 66 - 4, 12, 11, n8 * 12, 0);
                }
                this.e.translate(0, 5);
                this.f(this.by[0] - 58, n2 + 86 + 1, 116, 32, 15258029);
                this.f(this.by[0] - 57, n2 + 87 + 1, 114, 30, 13149034);
                this.f(this.by[0] - 57, n2 + 99 + 3, 108, 1, 8407056);
                n11 = 0;
                while (n11 < 4) {
                    int n12;
                    int n13;
                    if (n11 < 2) {
                        n13 = n11;
                        n12 = 0;
                    } else {
                        n13 = n11 - 2;
                        n12 = 1;
                    }
                    this.a(this.ai[6], this.by[0] - 50 + n13 * 54, n2 + 90 + n12 * 15, 23, 12, 0, n11 * 12);
                    n3 = this.a(nArray, this.fn[8 + n11]);
                    n4 = 0;
                    while (n4 < n3) {
                        this.a(this.aj[2], this.by[0] - 9 + n13 * 54 - n4 * 4, n2 + 90 + n12 * 15, 5, 7, nArray[n4] * 5, 0);
                        ++n4;
                    }
                    if (this.fn[8 + n11] > 0) {
                        this.a(this.ai[8], this.by[0] - 26 + n13 * 54, n2 + 90 + n12 * 15, 7, 7, 21, 0);
                    }
                    ++n11;
                }
                this.e.translate(0, -5);
            } else {
                int n14 = 0;
                while (n14 < 4) {
                    int n15 = n14 / 2;
                    int n16 = n14 < 2 ? n14 : n14 - 2;
                    if (this.fn[1] == 11) {
                        n9 = this.fs[n14 * 3 + 1];
                        image = this.af[this.fs[n14 * 3 + 2]];
                    } else if (this.fn[1] == 12) {
                        n9 = this.fs[n14 * 4 + 2];
                        image = this.af[this.fs[n14 * 4 + 3]];
                    }
                    if (n9 > 0) {
                        this.a(this.ai[13], this.by[0] - 48 + n16 * 52, n2 + 44 + n15 * 40, 20, 22, 0, 0);
                        this.a(image, this.by[0] - 47 + n16 * 52, n2 + 46 + n15 * 40, 18, 18, 0, 0);
                        n3 = this.a(nArray, this.fx[n14]);
                        n4 = 0;
                        while (n4 < n3) {
                            this.a(this.aj[1], this.by[0] - 21 + n16 * 52 - n4 * 4, n2 + 51 + n15 * 40, 5, 7, nArray[n4] * 5, 0);
                            ++n4;
                        }
                        this.a(this.ai[8], this.by[0] - 15 + n16 * 52, n2 + 51 + n15 * 40, 7, 7, 0, 0);
                        n3 = this.a(nArray, n9);
                        n4 = 0;
                        while (n4 < n3) {
                            this.a(this.aj[2], this.by[0] - 5 + n16 * 52 - n4 * 4, n2 + 51 + n15 * 40, 5, 7, nArray[n4] * 5, 0);
                            ++n4;
                        }
                    }
                    ++n14;
                }
            }
        } else if (this.ez == 1) {
            int n17 = this.eT > 4 ? 4 : this.eT;
            n10 = this.fn[1] > 1 && this.fn[1] < 6 && this.fn[0] > 14 ? 0xFFFF00 : 0xFFFFFF;
            int n18 = 1;
            while (n18 < n17) {
                this.b(this.bP[this.eR + n18], this.by[0] - 55, n2 + 37 + n18 * 20 - 5, 0, n10);
                ++n18;
            }
            this.a(this.ai[15], this.by[0] + 50, n2 + 37 + 6, 5, 3, 0, 0);
            this.a(this.ai[15], this.by[0] + 50, n2 + 108 + 6, 5, 3, 0, 2);
        }
        if (this.fn[1] > 1 && this.fn[1] < 6) {
            this.a(this.ai[15], this.by[0] - 55, n2 + 122 + 6, 3, 5, 0, 0);
            this.a(this.ai[15], this.by[0] + 52, n2 + 122 + 6, 3, 5, 2, 0);
        }
    }

    public final void aP() {
        int n2 = 0;
        int n3 = 0;
        int n4 = this.by[0] - 60;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        n2 = this.bx[1] / 2 - 57;
        boolean bl4 = false;
        boolean bl5 = false;
        this.e.setColor(13149034);
        this.e.fillRect(n4 - 3, n2 - 6, 126, 140);
        if (this.bT == 0) {
            this.f(this.by[0] - 60 - 10, n2 - 16, 140, 162, 13149034);
            n3 = 0;
            while (n3 < 11) {
                if (n3 < 9) {
                    this.a(this.ai[17], this.by[0] - 56 - 10 + n3 * 14, n2 - 16, 14, 14, 14, 0);
                    this.a(this.ai[17], this.by[0] - 56 - 10 + n3 * 14, n2 + 116 + 16, 14, 14, 14, 28);
                }
                this.a(this.ai[17], this.by[0] - 60 - 10, n2 + 9 + n3 * 14 - 16, 14, 14, 0, 14);
                this.a(this.ai[17], this.by[0] + 46 + 10, n2 + 9 + n3 * 14 - 16, 14, 14, 28, 14);
                ++n3;
            }
            this.a(this.ai[17], this.by[0] - 60 - 10, n2 - 16, 14, 14, 0, 0);
            this.a(this.ai[17], this.by[0] + 46 + 10, n2 - 16, 14, 14, 28, 0);
            this.a(this.ai[17], this.by[0] - 60 - 10, n2 + 116 + 16, 14, 14, 0, 28);
            this.a(this.ai[17], this.by[0] + 46 + 10, n2 + 116 + 16, 14, 14, 28, 28);
        }
        if (this.bx[1] >= 140) {
            this.a(this.ai[0], this.by[0] - 60 - 10, n2 - 16 - 16, 5, 16, 0, 0);
            n3 = 0;
            while (n3 < 26) {
                this.a(this.ai[0], this.by[0] - 55 - 10 + n3 * 5, n2 - 16 - 16, 5, 16, 5, 0);
                ++n3;
            }
            this.a(this.ai[0], this.by[0] + 55 + 10, n2 - 16 - 16, 5, 16, 10, 0);
            n3 = 0;
            while (n3 < 6) {
                this.a(this.ai[1], this.by[0] - 57 - 10 + n3 * 22 + 2, n2 - 16 - 16, 17, 16, (n3 + 1) * 17, 0);
                if (n3 == this.es && this.er == 0) {
                    this.a(this.ai[1], this.by[0] - 57 - 10 + n3 * 22 + 2, n2 - 16 - 16, 17, 16, 0, 0);
                }
                ++n3;
            }
        }
        if (this.bu != 21 && this.bu != 20) {
            n3 = 0;
            while (n3 < 11) {
                this.a(this.ai[2], this.by[0] - 45 - 10 + n3 * 10, n2 + 7 - 16, 10, 6, 0, 0);
                this.a(this.ai[2], this.by[0] - 45 - 10 + n3 * 10, n2 + 5 + 8 - 16, 10, 9, 0, 2);
                ++n3;
            }
            int n5 = 0;
            if (this.es == 1 || this.es == 2) {
                n5 = 1;
            } else if (this.es > 2) {
                n5 = this.es - 1;
            }
            this.a(this.ai[3], this.by[0] - 25, n2 + 8 - 16, 48, 12, 0, n5 * 12);
            switch (this.es) {
                case 0: {
                    this.L(n2);
                    break;
                }
                case 1: {
                    this.M(n2);
                    break;
                }
                case 2: {
                    this.N(n2 + 7);
                    break;
                }
                case 3: {
                    this.O(n2);
                    break;
                }
                case 4: {
                    this.P(n2);
                    break;
                }
                case 5: {
                    this.Q(n2);
                }
            }
        }
        this.a(this.al[7], this.by[0] + 8 + 10, n2 + 132 + 16, 52, 13, 0, 0);
    }

    public final void L(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int[] nArray = new int[10];
        int n7 = 0;
        int n8 = 0;
        boolean bl = false;
        boolean bl2 = false;
        this.f(this.by[0] - 53 - 10 + 3, n2 + 19, 21, 24, 14664086);
        this.f(this.by[0] - 52 - 10 + 3, n2 + 20, 19, 22, 8407056);
        this.f(this.by[0] - 51 - 10 + 3, n2 + 21, 17, 20, 16046774);
        this.a(this.O[1], this.by[0] - 51 - 10 + 3, n2 + 21, 17, 20, 0, 0);
        this.a(this.ai[9], this.by[0] - 51 - 10, n2 + 45, 25, 13, this.bZ * 26, 0);
        this.e.translate(0, 3);
        this.f(this.by[0] - 31 - 10 + 5, n2 + 20 - 20 + 3, 99, 90, 14664086);
        this.f(this.by[0] - 30 - 10 + 5, n2 + 21 - 20 + 3, 97, 88, 8407056);
        n3 = 0;
        while (n3 < 6) {
            this.a(this.ai[5], this.by[0] - 31, n2 + 22 + n3 * 14 - 20 + 5, 26, 14, 0, n3 * 14);
            this.a(this.ai[8], this.by[0] - 5, n2 + 22 + n3 * 14 - 20 + 8, 7, 7, 14, 0);
            switch (n3) {
                case 0: {
                    n4 = this.ce[0];
                    break;
                }
                case 1: {
                    n4 = this.ce[1];
                    break;
                }
                case 2: {
                    n4 = this.ce[9] + this.ce[13] % 10000;
                    break;
                }
                case 3: {
                    n4 = this.ce[10] + this.ce[16];
                    break;
                }
                case 4: {
                    n4 = this.ce[2];
                    break;
                }
                case 5: {
                    n4 = this.ce[3];
                }
            }
            n7 = this.a(nArray, n4);
            n8 = 0;
            while (n8 < n7) {
                this.a(this.aj[2], this.by[0] + 11 - n8 * 4 + 9, n2 + 22 + n3 * 14 - 20 + 8, 5, 7, nArray[n8] * 5, 0);
                ++n8;
            }
            switch (n3) {
                case 1: {
                    n4 = this.ch;
                    break;
                }
                case 2: {
                    n4 = this.ce[9] + this.ce[12];
                    break;
                }
                case 4: {
                    n4 = this.cf;
                    break;
                }
                case 5: {
                    n4 = this.cg;
                }
            }
            if (n3 != 0 && n3 != 3) {
                this.a(this.ai[8], this.by[0] + 17 + 9, n2 + 22 + n3 * 14 - 20 + 8, 7, 7, 0, 0);
                n7 = this.a(nArray, n4);
                n8 = 0;
                while (n8 < n7) {
                    this.a(this.aj[2], this.by[0] + 45 - n8 * 4 + 6, n2 + 22 + n3 * 14 - 20 + 8, 5, 7, nArray[n8] * 5, 0);
                    ++n8;
                }
            }
            ++n3;
        }
        this.e.translate(0, -3);
        this.e.translate(0, -2);
        n3 = 0;
        while (n3 < 2) {
            this.f(this.by[0] - 51, n2 + 96 + n3 * 16 + 20 - 4, 102, 1, 8407056);
            ++n3;
        }
        n3 = 0;
        while (n3 < 4) {
            n6 = n3 / 2;
            n5 = n3 < 2 ? n3 : n3 - 2;
            this.a(this.ai[6], this.by[0] - 49 + n5 * 51, n2 + 87 + n6 * 16 + 20 - 7, 23, 11, 0, n3 * 12);
            if (n3 == this.eu && this.er == 1) {
                this.a(this.ai[7], this.by[0] - 10 + n5 * 51, n2 + 86 + n6 * 16 + 20 - 4, 9, 9, 9 * this.ex, 0);
            } else {
                this.a(this.ai[7], this.by[0] - 10 + n5 * 51, n2 + 86 + n6 * 16 + 20 - 4, 9, 9, 18, 0);
            }
            n4 = this.ce[n3 + 4];
            if (n3 == 1) {
                n4 = this.ce[5] + this.ce[15];
            }
            n7 = this.a(nArray, n4);
            n8 = 0;
            while (n8 < n7) {
                this.a(this.aj[2], this.by[0] - 17 + n5 * 51 - n8 * 4, n2 + 86 + n6 * 16 + 20 - 3, 5, 7, nArray[n8] * 5, 0);
                ++n8;
            }
            ++n3;
        }
        this.a(this.ai[6], this.by[0] + 2, n2 + 87 + 24 + 20, 23, 11, 0, 48);
        n7 = this.a(nArray, this.ci);
        n8 = 0;
        while (n8 < n7) {
            this.a(this.aj[2], this.by[0] + 35 - n8 * 4, n2 + 87 + 24 + 20 + 4, 5, 7, nArray[n8] * 5, 0);
            ++n8;
        }
        this.e.translate(0, 2);
    }

    public final void M(int n2) {
        int[] nArray = new int[10];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int[] nArray2 = new int[5];
        nArray2[0] = 1;
        nArray2[1] = 3;
        nArray2[2] = 7;
        nArray2[3] = 9;
        int[] nArray3 = nArray2;
        Object var11_10 = null;
        boolean bl = false;
        this.e.translate(0, 2);
        this.f(this.by[0] - 55 - 5, n2 + 23 - 7, 120, 47, 14664086);
        this.f(this.by[0] - 54 - 5, n2 + 24 + 0 - 7, 118, 45, 5843463);
        this.a(this.ai[11], this.by[0] - 55 - 5, n2 + 19 - 12, 54, 10, 0, 0);
        this.a(this.ai[12], this.by[0] - 53 - 5, n2 + 22 - 9, 47, 11, 0, 0);
        this.a(this.O[1], this.by[0] - 19, n2 + 30 - 4, 17, 33, 0, 0);
        n5 = 0;
        while (n5 < 3) {
            n7 = n2 + 37;
            if (n5 == 0) {
                n6 = this.by[0] - 48;
            } else if (n5 == 1) {
                n6 = this.by[0] + 6;
            } else if (n5 == 2) {
                n6 = this.by[0] + 30;
            }
            this.a(this.ai[13], n6, n7 - 4, 22, 22, 0, 0);
            if (this.er == 1 && this.et == 0) {
                if (this.eu == n5) {
                    this.a(this.ai[14], n6, n7 - 4, 22, 22, 0, 0);
                }
                switch (this.eu) {
                    case 0: {
                        this.f(this.by[0] - 26, n2 + 52 - 4, 9, 1, 65280);
                        break;
                    }
                    case 1: {
                        this.f(this.by[0] - 2, n2 + 47 - 4, 8, 1, 65280);
                        break;
                    }
                    case 2: {
                        this.f(this.by[0] - 5, n2 + 60 - 4, 47, 1, 65280);
                        this.f(this.by[0] + 41, n2 + 59 - 4, 1, 2, 65280);
                    }
                }
            }
            if (this.cl[n5][0] != 0) {
                n8 = this.cq[this.cl[n5][1]][7];
                this.a(this.af[n8], n6 + 1, n7 + 2 - 4, 18, 18, 0, 0);
            }
            ++n5;
        }
        this.e.translate(0, -2);
        this.e.translate(0, -3);
        this.f(this.by[0] - 55 - 5, n2 + 68 + 12, 120, 51, 14664086);
        this.f(this.by[0] - 54 - 5, n2 + 69 + 12, 118, 49, 5843463);
        this.a(this.ai[11], this.by[0] - 55 - 5, n2 + 64 + 7, 54, 10, 0, 0);
        this.a(this.ai[12], this.by[0] - 53, n2 + 67 + 7, 47, 11, 0, 12);
        n5 = 0;
        while (n5 < 5) {
            this.a(this.ai[13], this.by[0] - 52 + n5 * 21, n2 + 77 + 12, 20, 22, 0, 0);
            if (n5 == this.ev && this.et == 1 && this.er == 1) {
                this.a(this.ai[14], this.by[0] - 52 + n5 * 21, n2 + 77 + 12, 20, 22, 0, 0);
            }
            if (this.cj[n5][0] != 0) {
                Image image;
                if (this.cj[n5][0] == 7) {
                    image = this.ag[this.cj[n5][1]];
                } else {
                    n8 = this.cq[this.cj[n5][1]][7];
                    image = this.af[n8];
                }
                this.a(image, this.by[0] - 51 + n5 * 21, n2 + 79 + 12, 18, 18, 0, 0);
            }
            this.a(this.aj[0], this.by[0] - 51 + n5 * 21, n2 + 78 + 12, 5, 7, nArray3[n5] * 5, 0);
            ++n5;
        }
        this.f(this.by[0] - 55, n2 + 101 + 12, 110, 1, 14664086);
        this.a(this.ai[12], this.by[0] - 50, n2 + 104 + 12, 47, 11, 0, 48);
        n3 = this.a(nArray, this.ce[11]);
        n4 = 0;
        while (n4 < n3) {
            this.a(this.aj[2], this.by[0] + 40 - n4 * 4, n2 + 104 + 14, 5, 7, nArray[n4] * 5, 0);
            ++n4;
        }
        this.e.translate(0, 3);
        this.a(this.ai[10], this.by[0] - 40, n2 + 115 + 14, 53, 11, 0, 12);
    }

    public final void N(int n2) {
        this.q(0, n2);
    }

    public final void O(int n2) {
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int[] nArray = new int[10];
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        String string = "";
        if (this.ey == 0) {
            this.f(this.by[0] - 55, n2 + 24 - 14, 110, 106, 14664086);
            this.f(this.by[0] - 54, n2 + 25 - 14, 108, 104, 8407056);
            n3 = 0;
            while (n3 < 3) {
                if (n3 == this.eu && this.er == 1) {
                    n11 = 0xFFFF00;
                    this.f(this.by[0] - 50, n2 + 34 + n3 * 24 - 6, 100, 23, 14928809);
                    this.f(this.by[0] - 49, n2 + 35 + n3 * 24 - 6, 98, 21, 2494464);
                } else {
                    n11 = 0xFFFFFF;
                }
                if (n3 == 0) {
                    string = "\u57fa\u672c\u62db\u5f0f";
                } else if (n3 == 1) {
                    string = "\u4e0a\u4e58\u62db\u5f0f";
                } else if (n3 == 2) {
                    string = "\u4e0a\u4e58\u5fc3\u6cd5";
                }
                this.b(string, this.by[0], n2 + 37 + n3 * 24 - 6, 1, n11);
                ++n3;
            }
        } else if (this.ey == 1) {
            this.f(this.by[0] - 53, n2 + 18 - 2, 106, 105, 14664086);
            this.f(this.by[0] - 52, n2 + 19 - 2, 104, 103, 5843463);
            System.out.println("\u57fa\u672c\u62db\u5f0f");
            n3 = 0;
            while (n3 < 6) {
                n5 = n3 % 3;
                n6 = n2 + 25 + n3 / 3 * 27;
                if (n3 + 1 == this.co) {
                    this.f(this.by[0] - 43 + n5 * 30, n6 - 3, 28, 28, 0xFF0000);
                }
                this.a(this.ai[13], this.by[0] - 40 + n5 * 30, n6, 22, 22, 0, 0);
                if (n3 == this.ev && this.er == 1) {
                    this.a(this.ai[14], this.by[0] - 40 + n5 * 30, n6, 22, 22, 0, 0);
                }
                if (this.cp[n3][0] > 0) {
                    this.a(this.ah[n3 % 3], this.by[0] - 39 + n5 * 30, n6 + 2, 18, 18, 0, 0);
                    if (n3 > 2) {
                        this.a(this.ah[3], this.by[0] - 38 + n5 * 30, n6 + 2, 16, 18, 0, 0);
                    }
                }
                ++n3;
            }
            this.f(this.by[0] - 51, n2 + 82, 102, 33, 14664086);
            this.f(this.by[0] - 50, n2 + 83, 100, 31, 8538126);
            if (this.cp[this.ev][0] > 0) {
                this.b(this.fo, this.by[0], n2 + 85 + 1, 1, 0xFFFF00);
            }
            this.e.translate(0, 4);
            this.a(this.aj[1], this.by[0] - 47, n2 + 100, 5, 7, 5 * this.cp[this.ev][0], 0);
            this.a(this.ai[8], this.by[0] - 41, n2 + 100, 7, 7, 0, 0);
            this.a(this.aj[2], this.by[0] - 33, n2 + 100, 5, 7, 30, 0);
            this.f(this.by[0] - 25, n2 + 100, 73, 7, 0);
            this.f(this.by[0] - 24, n2 + 101, 71, 5, 0x404040);
            if (this.cp[this.ev][2] > 0) {
                n8 = this.cp[this.ev][1] * 71 / this.cp[this.ev][2];
                this.f(this.by[0] - 24, n2 + 101, n8, 2, 12254541);
                this.f(this.by[0] - 24, n2 + 103, n8, 3, 7657472);
            }
            this.e.translate(0, -4);
            this.a(this.ai[10], this.by[0] - 40, n2 + 116 + 9, 53, 11, 0, 12);
        } else if (this.ey == 2) {
            this.f(this.by[0] - 55, n2 + 17, 110, 86, 14664086);
            this.f(this.by[0] - 54, n2 + 18, 108, 84, 8407056);
            n3 = 0;
            while (n3 < 6) {
                n5 = n3 % 2;
                n6 = n2 + 20 + n3 / 2 * 27;
                this.f(this.by[0] - 53 + n5 * 53, n6, 52, 26, 14664086);
                this.f(this.by[0] - 52 + n5 * 53, n6 + 1, 50, 24, 5843463);
                this.a(this.ai[13], this.by[0] - 50 + n5 * 53, n6 + 2, 22, 22, 0, 0);
                this.a(this.ag[n3], this.by[0] - 49 + n5 * 53, n6 + 4, 18, 18, 0, 0);
                if (this.ev == n3 && this.er == 1) {
                    this.a(this.ai[14], this.by[0] - 50 + n5 * 53, n6 + 2, 22, 22, 0, 0);
                }
                n4 = 0;
                while (n4 < 6) {
                    if (this.cm[n3] > n4) {
                        this.a(this.ai[16], this.by[0] - 26 + n5 * 53 + n4 * 4, n6 + 11, 2, 8, 0, 0);
                    } else {
                        this.a(this.ai[16], this.by[0] - 26 + n5 * 53 + n4 * 4, n6 + 11, 2, 8, 2, 0);
                    }
                    ++n4;
                }
                if (this.cm[n3] == 0) {
                    this.a(this.Z[9], this.by[0] - 47 + n5 * 53, n6 + 4, 16, 18, 0, 0);
                }
                ++n3;
            }
        } else if (this.ey == 3) {
            this.f(this.by[0] - 55 - 5, n2 + 17 - 6, 120, 95, 14664086);
            this.f(this.by[0] - 54 - 5, n2 + 18 - 6, 118, 93, 8407056);
            n3 = 0;
            while (n3 < 4) {
                if (n3 == this.ev) {
                    this.f(this.by[0] - 55 - 5, n2 + 21 + n3 * 22 - 6, 120, 21, 7208896);
                    this.f(this.by[0] - 54 - 5, n2 + 22 + n3 * 22 - 6, 118, 19, 3165648);
                } else {
                    this.f(this.by[0] - 55 - 5, n2 + 21 + n3 * 22 - 6, 120, 22, 14664086);
                    this.f(this.by[0] - 54 - 5, n2 + 22 + n3 * 22 - 6, 118, 19, 8538126);
                }
                n7 = n3 == 3 ? 1 : 3;
                n4 = 0;
                while (n4 < n7) {
                    if (this.cz[n3] > n4) {
                        this.a(this.ai[16], this.by[0] + 44 - n4 * 4, n2 + 26 + n3 * 22 - 6, 2, 8, 0, 0);
                    } else {
                        this.a(this.ai[16], this.by[0] + 44 - n4 * 4, n2 + 26 + n3 * 22 - 6, 2, 8, 2, 0);
                    }
                    ++n4;
                }
                n11 = this.cz[n3] == 0 ? 0x737373 : 0xFFFFFF;
                if (this.bZ == 0) {
                    if (n3 == 0) {
                        string = "\u9053\u5bb6\u5fc3\u6cd5";
                    } else if (n3 == 1) {
                        string = "\u6b63\u95e8\u5fc3\u6cd5";
                    } else if (n3 == 2) {
                        string = "\u90aa\u95e8\u5fc3\u6cd5";
                    } else if (n3 == 3) {
                        string = "\u795e\u58eb\u5fc3\u6cd5";
                    }
                } else if (this.bZ == 1) {
                    if (n3 == 0) {
                        string = "\u9053\u5bb6\u5fc3\u6cd5";
                    } else if (n3 == 1) {
                        string = "\u6b63\u95e8\u5fc3\u6cd5";
                    } else if (n3 == 2) {
                        string = "\u90aa\u95e8\u5fc3\u6cd5";
                    } else if (n3 == 3) {
                        string = "\u795e\u58eb\u5fc3\u6cd5";
                    }
                }
                this.b(string, this.by[0] - 50, n2 + 23 + n3 * 22 - 6, 0, n11);
                ++n3;
            }
        }
        if (this.ey != 1) {
            this.a(this.ai[6], this.by[0], n2 + 108 + 12, 47, 11, 0, 48);
            n9 = this.a(nArray, this.cn);
            n10 = 0;
            while (n10 < n9) {
                this.a(this.aj[2], this.by[0] + 45 - n10 * 4, n2 + 108 + 15, 5, 7, nArray[n10] * 5, 0);
                ++n10;
            }
        }
        if (this.ey > 1) {
            this.a(this.ai[10], this.by[0] - 50, n2 + 108, 53, 11, 0, 12);
        }
    }

    public final void P(int n2) {
        int[] nArray = new int[10];
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        boolean bl2 = false;
        String string = null;
        this.f(this.by[0] - 55 - 10 + 6, n2 + 22 - 16, 118, 134, 14664086);
        this.f(this.by[0] - 54 - 10 + 6, n2 + 23 - 16, 116, 132, 8407056);
        if (this.et == 0 || this.et == 2) {
            this.f(this.by[0] - 55 - 10 + 6, n2 + 22 - 16, 118, 134, 14664086);
            this.f(this.by[0] - 54 - 10 + 6, n2 + 23 - 16, 116, 132, 8407056);
            if (this.et == 0) {
                this.a(this.ai[12], this.by[0] - 50, n2 + 23 - 11, 47, 11, 0, 24);
                this.a(this.ai[12], this.by[0] - 50, n2 + 50, 47, 11, 0, 36);
                int n7 = 0;
                while (n7 < 4) {
                    n6 = n7 == 0 ? n2 + 32 - 6 : n2 + 65 + (n7 - 1) * 18;
                    if (n7 == this.eu && this.er == 1) {
                        this.f(this.by[0] - 51, n6 - 2, 102, 22, 14928809);
                        this.f(this.by[0] - 50, n6 - 1, 100, 20, 2494464);
                    }
                    if (this.fm == 1 && n7 == 0) {
                        this.b("\u7ec3\u4e60\u6a21\u5f0f", this.by[0], n2 + 34, 1, 0xFFFF00);
                    } else if (this.eJ[n7][1] > 0) {
                        n5 = 0xFFFFFF;
                        if (this.eJ[n7][22] == 1) {
                            n5 = 0xFFFF00;
                        }
                        this.b(this.eK[n7], this.by[0], n6 + 1, 1, n5);
                    }
                    ++n7;
                }
                this.a(this.ai[10], this.by[0] + 20, n2 + 113 + 13, 47, 11, 0, 36);
                return;
            }
            this.b(this.eK[this.eu], this.by[0], n2 + 30, 1, 0xFFFF00);
            this.b("\u8981\u53d6\u6d88\u4efb\u52a1", this.by[0], n2 + 50, 1, 0xFFFFFF);
            this.b("\u5417\uff1f", this.by[0], n2 + 65, 1, 0xFFFFFF);
            int n8 = 0;
            while (n8 < 2) {
                if (n8 == this.ew) {
                    n5 = 16771328;
                    this.f(this.by[0] - 50 + n8 * 52, n2 + 85, 48, 23, 7208896);
                    this.f(this.by[0] - 49 + n8 * 52, n2 + 86, 46, 21, 3165648);
                } else {
                    n5 = 8849673;
                }
                string = "\u662f";
                if (n8 == 1) {
                    string = "\u5426";
                }
                this.b(string, this.by[0] - 25 + n8 * 52, n2 + 89, 1, n5);
                ++n8;
            }
            return;
        }
        this.f(this.by[0] - 60 - 3, n2 + 5, 126, 125, 14664086);
        this.f(this.by[0] - 59 - 3, n2 + 6, 124, 123, 39397895);
        this.f(this.by[0] - 55 - 3, n2 + 24 + 2, 116, 92, 13149034);
        this.f(this.by[0] - 54 - 3, n2 + 25 + 2, 114, 90, 2494464);
        this.b(this.eK[this.eu], this.by[0], n2 + 8, 1, 0xFFFF00);
        if (this.ev == 0) {
            n6 = this.eT > 4 ? 4 : this.eT;
            int n9 = 0;
            while (n9 < n6) {
                this.b(this.eO[this.eR + n9].a, this.by[0] - 50 - 3, n2 + 32 + n9 * 20 + 2, 0, 0xFFFFFF);
                ++n9;
            }
            this.a(this.ai[15], this.by[0] + 47, n2 + 27, 5, 3, 0, 0);
            this.a(this.ai[15], this.by[0] + 47, n2 + 110, 5, 3, 0, 2);
            if (this.eJ[this.eu][2] == 2 || this.eJ[this.eu][2] == 3) {
                n6 = this.eJ[this.eu][6] > this.eJ[this.eu][5] ? this.eJ[this.eu][5] : this.eJ[this.eu][6];
                n3 = this.a(nArray, n6);
                n4 = 0;
                while (n4 < n3) {
                    this.a(this.aj[2], this.by[0] - 8 - n4 * 4, n2 + 120, 5, 7, nArray[n4] * 5, 0);
                    ++n4;
                }
                this.a(this.ai[8], this.by[0], n2 + 120, 7, 7, 0, 0);
                n3 = this.a(nArray, this.eJ[this.eu][5]);
                n4 = 0;
                while (n4 < n3) {
                    this.a(this.aj[2], this.by[0] + 8 + n4 * 4, n2 + 120, 5, 7, nArray[n3 - n4 - 1] * 5, 0);
                    ++n4;
                }
            }
        } else {
            this.b("[\u4efb\u52a1\u5956\u52b1]", this.by[0] - 50, n2 + 32, 0, 0xFFFF00);
            int n10 = 0;
            while (n10 < 4) {
                this.b(this.gt[n10], this.by[0] - 50, n2 + 49 + n10 * 17, 0, 0xFFFFFF);
                ++n10;
            }
        }
        this.a(this.ai[15], this.by[0] - 55, n2 + 120 + 2, 3, 5, 0, 0);
        this.a(this.ai[15], this.by[0] + 50, n2 + 120 + 2, 3, 5, 2, 0);
    }

    public final void Q(int n2) {
        boolean bl = false;
        boolean bl2 = false;
        this.f(this.by[0] - 55 - 10 + 5, n2 + 20 - 20 + 8, 120, 130, 14664086);
        this.f(this.by[0] - 54 - 10 + 5, n2 + 21 - 20 + 8, 118, 128, 8407056);
        if (this.ey == 0 || this.ey > 0 && this.eu == 1) {
            int n3 = 0;
            while (n3 < 6) {
                if (n3 == this.eu && this.er == 1) {
                    this.f(this.by[0] - 50, n2 + 23 - 20 + n3 * 18 + 17, 100, 17, 14928809);
                    this.f(this.by[0] - 49, n2 + 24 - 20 + n3 * 18 + 17, 98, 15, 2494464);
                }
                this.a(this.ai[18], this.by[0] - 24, n2 + 25 - 20 + n3 * 18 + 17, 61, 14, 0, n3 * 14);
                ++n3;
            }
        } else if (this.ey == 1) {
            if (this.eu == 2) {
                this.m(170, 120);
                this.b("\u64cd\u4f5c\u8bf4\u660e", this.by[0], this.by[1] - 55, 1, 0xFFFF00);
                int n4 = 0;
                while (n4 < 4) {
                    this.b(this.eO[this.eR + n4].a, this.by[0] - 73, n2 + 30 + n4 * 20, 0, 0);
                    ++n4;
                }
                this.a(this.ai[4], this.by[0] + 72, n2 + 26, 5, 3, 0, 0);
                this.a(this.ai[4], this.by[0] + 72, n2 + 109, 5, 3, 0, 2);
            } else if (this.eu == 3) {
                int n5 = 0;
                while (n5 < 2) {
                    if (this.ev == n5) {
                        this.f(this.by[0] - 50, n2 + 26 + n5 * 19, 100, 17, 16508095);
                        this.f(this.by[0] - 49, n2 + 27 + n5 * 19, 98, 15, 2494464);
                    }
                    this.a(this.aa[14], this.by[0] - 39, n2 + 28 + n5 * 19, 25, 14, 0, n5 * 14);
                    switch (n5) {
                        case 0: {
                            this.a(this.aa[15], this.by[0] + 10, n2 + 28 + n5 * 19, 25, 14, 0, g * 14);
                            break;
                        }
                        case 1: {
                            this.b(Integer.toString(this.bJ + 1), this.by[0] + 20, n2 + 28 + n5 * 19 - 2, 1, 0xFFFFFF);
                        }
                    }
                    this.a(this.ai[15], this.by[0], n2 + 32 + n5 * 19, 3, 5, 0, 0);
                    this.a(this.ai[15], this.by[0] + 40, n2 + 32 + n5 * 19, 3, 5, 2, 0);
                    ++n5;
                }
            }
        }
        if (this.ey == 1 && this.eu == 1 || this.ey == 2 && this.eu == 1) {
            if (this.gn == 1) {
                this.ey = 2;
            }
            this.d("\u4fdd\u5b58\u5b8c\u6bd5");
        }
        this.aQ();
    }

    public final void aQ() {
        if (this.G) {
            int n2 = 5 * this.o;
            int n3 = 4 * this.p;
            int n4 = this.m - n2 / 2;
            int n5 = this.n - n3 / 2;
            this.c(n4, n5, n2, n3, 0xFFFADD);
            this.e(n4, n5, n2, n3, 13215083);
            this.a("\u662f\u5426\u8fd4\u56de\u4e3b\u83dc\u5355?", 4, n4 + n2 / 2, n5, 0x633600, 17);
            this.c(this.H == 0 ? n4 : n4 + n2 - this.o, n5 + n3 - this.p, this.o, this.p, 255);
            this.a("\u662f", n4, n5 + n3, 0xFF0000, 36);
            this.a("\u5426", n4 + n2, n5 + n3, 0xFF0000, 40);
        }
    }

    public final void aR() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        int[] nArray = new int[10];
        int n13 = 0;
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17 = 0xFFFFFF;
        boolean bl = false;
        boolean bl2 = false;
        this.f(this.by[0] - 60 - 10, this.by[1] - 60 - 5, 140, 140, 14664086);
        this.f(this.by[0] - 59 - 10, this.by[1] - 59 - 5, 138, 138, 5843463);
        if (this.bu == 25) {
            this.b(this.aL, this.by[0], this.by[1] - 57 - 2, 1, 0xFFFF00);
            this.e.translate(0, 3);
            this.f(this.by[0] - 43, this.by[1] - 40, 86, 86, 15258029);
            this.f(this.by[0] - 42, this.by[1] - 39, 84, 84, 3868928);
            n2 = this.by[0] - this.bC[0];
            n3 = this.by[1] - this.bC[1] + 3;
            n6 = 0;
            while (n6 < this.bC[1]) {
                n7 = 0;
                while (n7 < this.bC[0]) {
                    n8 = this.aH[2][n6 * this.bC[0] + n7] % 100 / 10;
                    if (n8 == 0) {
                        this.f(n2 + n7 * 2, n3 + n6 * 2, 2, 2, 0xEEAA55);
                    }
                    ++n7;
                }
                ++n6;
            }
            n6 = 0;
            while (n6 < this.aP) {
                n4 = this.aQ[n6 * 9 + 0];
                switch (n4) {
                    case 0: {
                        n10 = n3;
                        n12 = 2;
                        n9 = n2 + this.aQ[n6 * 9 + 1] * 2;
                        n11 = (this.aQ[n6 * 9 + 2] - this.aQ[n6 * 9 + 1] + 1) * 2;
                        break;
                    }
                    case 1: {
                        n10 = n3 + (this.bC[1] - 1) * 2;
                        n12 = 2;
                        n9 = n2 + this.aQ[n6 * 9 + 1] * 2;
                        n11 = (this.aQ[n6 * 9 + 2] - this.aQ[n6 * 9 + 1] + 1) * 2;
                        break;
                    }
                    case 2: {
                        n9 = n2;
                        n11 = 2;
                        n10 = n3 + this.aQ[n6 * 9 + 1] * 2;
                        n12 = (this.aQ[n6 * 9 + 2] - this.aQ[n6 * 9 + 1] + 1) * 2;
                        break;
                    }
                    case 3: {
                        n9 = n2 + (this.bC[0] - 1) * 2;
                        n11 = 2;
                        n10 = n3 + this.aQ[n6 * 9 + 1] * 2;
                        n12 = (this.aQ[n6 * 9 + 2] - this.aQ[n6 * 9 + 1] + 1) * 2;
                    }
                }
                this.f(n9, n10, n11, n12, 46847);
                ++n6;
            }
            n6 = 0;
            while (n6 < this.aR) {
                n9 = n2 + this.aS[n6 * 10 + 0] * 2;
                n10 = n3 + this.aS[n6 * 10 + 2] * 2;
                n11 = (this.aS[n6 * 10 + 1] - this.aS[n6 * 10 + 0] + 1) * 2;
                n12 = (this.aS[n6 * 10 + 3] - this.aS[n6 * 10 + 2] + 1) * 2;
                this.f(n9, n10, n11, n12, 46847);
                ++n6;
            }
            n6 = this.ej % 2;
            this.f(n2 + this.cc * 2 - n6, n3 + this.cd * 2 - n6, 2 + n6 * 2, 2 + n6 * 2, 0xFF0000);
            this.e.translate(0, -3);
            if (this.bt < 38) {
                this.a(this.al[4], this.by[0] + 5 - 10, this.by[1] + 55, 60, 15, 0, 0);
                return;
            }
        } else if (this.bu == 31) {
            n5 = this.fN[this.eu] - 1;
            this.e.translate(-4, 0);
            this.R(n5);
            this.e.translate(4, 0);
            n6 = 0;
            while (n6 < 5) {
                switch (n6) {
                    case 0: {
                        n15 = this.aU[n5].l;
                        if (n15 > 20) {
                            n15 += n15 / 4;
                        }
                        n16 = 0;
                        break;
                    }
                    case 1: {
                        n15 = this.aU[n5].ae;
                        n16 = 28;
                        break;
                    }
                    case 2: {
                        n15 = this.aU[n5].af;
                        n16 = 42;
                        break;
                    }
                    case 3: {
                        n15 = this.aU[n5].ag;
                        n16 = 84;
                        break;
                    }
                    case 4: {
                        n15 = this.aU[n5].ao;
                        n16 = 13;
                    }
                }
                this.a(this.ai[5], this.by[0] - 2, this.by[1] - 50 + n6 * 13 - 9, 26, 13, 0, n16);
                this.a(this.ai[8], this.by[0] + 25 - 3, this.by[1] - 50 + n6 * 13 - 6, 7, 7, 14, 0);
                n13 = this.a(nArray, n15);
                n14 = 0;
                while (n14 < n13) {
                    this.a(this.aj[2], this.by[0] + 33 + n14 * 4 + 10, this.by[1] - 50 + n6 * 13 - 6, 5, 7, nArray[n13 - 1 - n14] * 5, 0);
                    ++n14;
                }
                ++n6;
            }
            this.f(this.by[0] - 60 - 10, this.by[1] + 8, 140, 62, 14664086);
            this.f(this.by[0] - 59 - 10, this.by[1] + 9, 138, 60, 3868928);
            switch (this.aU[n5].aa) {
                case 0: {
                    n17 = 16750080;
                    break;
                }
                case 1: {
                    n17 = 65535;
                    break;
                }
                case 2: {
                    n17 = 0xFFFF00;
                    break;
                }
                case 3: {
                    n17 = 0xFFFFFF;
                }
            }
            n6 = 0;
            while (n6 < 3) {
                this.b(this.eO[this.eR + n6].a, this.by[0] - 55 - 10, this.by[1] + 14 + n6 * 18 - 2, 0, n17);
                ++n6;
            }
            this.a(this.ai[15], this.by[0] + 52 + 10, this.by[1] + 10, 5, 3, 0, 0);
            this.a(this.ai[15], this.by[0] + 52 + 10, this.by[1] + 65, 5, 3, 0, 2);
            this.a(this.ai[15], this.by[0] - 55 - 10 + 2, this.by[1] - 55, 3, 5, 0, 0);
            this.a(this.ai[15], this.by[0] + 52 + 10 - 2, this.by[1] - 55, 3, 5, 2, 0);
        }
    }

    public final void R(int n2) {
        boolean bl = false;
        int n3 = 0;
        int[] nArray = new int[4];
        nArray[1] = 1;
        nArray[3] = 2;
        int[] nArray2 = nArray;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = this.by[0] - 26;
        int n11 = this.by[1] - 22;
        boolean bl2 = false;
        n6 = this.aU[n2].ar;
        n7 = this.aU[n2].av;
        n3 = this.ej % 4;
        if (n7 == 0 || n7 == 1) {
            if (n7 == 0) {
                n9 = n6 * 12 + 1;
                n4 = this.W[n9].getWidth() / 2;
                n5 = this.W[n9].getHeight();
                n8 = n3 < 2 ? 0 : n4;
            } else if (n7 == 1) {
                n8 = 0;
                n9 = n3 < 2 ? n6 * 12 + 1 : n6 * 12 + 4 + 1;
                n4 = this.W[n9].getWidth();
                n5 = this.W[n9].getHeight();
            }
        } else if (n7 == 2) {
            n9 = n6 * 12 + 1;
            n4 = this.W[n9].getWidth() / 3;
            n5 = this.W[n9].getHeight();
            n8 = nArray2[n3] * n4;
        } else if (n7 == 3) {
            n9 = n6 * 12;
            n4 = this.W[n9].getWidth();
            n5 = this.W[n9].getHeight();
            n8 = 0;
        }
        this.a(this.W[n9], n10 - n4 / 2, n11 - n5 / 2, n4, n5, n8, 0);
    }

    public final void S(int n2) {
        if (n2 == 1) {
            this.f(0, this.by[1] - 16 + this.aO * 3, this.bx[0], 33 - this.aO * 6, 14664086);
            this.f(0, this.by[1] - 15 + this.aO * 3, this.bx[0], 31 - this.aO * 6, 3868928);
        } else {
            this.f(0, this.by[1] - 15 + this.aO * 3, this.bx[0], 31 - this.aO * 6, 8657);
        }
        if (this.aO < 2) {
            this.b(this.aL, this.by[0], this.by[1] - 7, 1, 0xFFFF00);
        }
    }

    public final void aS() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        String string = "";
        if (this.fy < 5 || this.fy == 7) {
            n5 = this.by[1] - 44;
            n6 = 88;
        } else if (this.fy == 6) {
            n5 = this.by[1] - 54;
            n6 = 108;
        }
        this.f(this.by[0] - 58, n5, 116, n6, 5712384);
        n2 = 0;
        while (n2 < 7) {
            this.a(this.Z[8], this.by[0] - 58 + 7 + n2 * 14, n5, 14, 14, 14, 0);
            this.a(this.Z[8], this.by[0] - 58 + 7 + n2 * 14, n5 + n6 - 14, 14, 14, 14, 28);
            ++n2;
        }
        n3 = n6 / 14 - 1;
        n2 = 0;
        while (n2 < n3) {
            this.a(this.Z[8], this.by[0] - 58, n5 + 14 + n2 * 14, 14, 14, 0, 14);
            this.a(this.Z[8], this.by[0] + 44, n5 + 14 + n2 * 14, 14, 14, 28, 14);
            ++n2;
        }
        this.a(this.Z[8], this.by[0] - 58, n5, 14, 14, 0, 0);
        this.a(this.Z[8], this.by[0] + 44, n5, 14, 14, 28, 0);
        this.a(this.Z[8], this.by[0] - 58, n5 + n6 - 14, 14, 14, 0, 28);
        this.a(this.Z[8], this.by[0] + 44, n5 + n6 - 14, 14, 14, 28, 28);
        this.f(this.by[0] - 41, n5 + 11, 82, n6 - 21, 14664086);
        this.f(this.by[0] - 40, n5 + 12, 80, n6 - 23, 5843463);
        n3 = this.fy < 5 || this.fy == 7 ? 3 : 4;
        n2 = 0;
        while (n2 < n3) {
            if (n2 == this.fz) {
                n4 = 0xFFFF00;
                this.f(this.by[0] - 37, n5 + 15 + n2 * 20, 74, 19, 7208896);
                this.f(this.by[0] - 36, n5 + 16 + n2 * 20, 72, 17, 3165648);
            } else {
                n4 = 0xFFFFFF;
                this.f(this.by[0] - 37, n5 + 15 + n2 * 20, 74, 19, 14928809);
                this.f(this.by[0] - 36, n5 + 16 + n2 * 20, 72, 17, 8538126);
            }
            if (this.fy < 5 || this.fy == 7) {
                switch (n2) {
                    case 0: {
                        if (this.fy == 7) {
                            string = "\u7f51\u4e0a\u5546\u5e97";
                            break;
                        }
                        string = "\u8d2d  \u4e70";
                        break;
                    }
                    case 1: {
                        if (this.fy == 7) {
                            string = "\u63a5\u6536\u7269\u54c1";
                            break;
                        }
                        string = "\u8d29  \u5356";
                        break;
                    }
                    case 2: {
                        string = "\u8fd4\u56de";
                    }
                }
            } else if (this.fy == 6) {
                switch (n2) {
                    case 0: {
                        string = "\u5f3a  \u5316";
                        break;
                    }
                    case 1: {
                        string = "\u5236  \u70bc";
                        break;
                    }
                    case 2: {
                        string = "\u5236  \u9020";
                        break;
                    }
                    case 3: {
                        string = "\u8fd4  \u56de";
                    }
                }
            }
            this.b(string, this.by[0], n5 + 18 + n2 * 20, 1, n4);
            ++n2;
        }
    }

    public final void aT() {
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        String string = null;
        int[] nArray = new int[10];
        int n5 = 0;
        int n6 = 0;
        if (this.bu == 33) {
            this.f(this.by[0] - 60, this.by[1] - 60, 120, 22, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 59, 118, 20, 8538126);
            this.f(this.by[0] - 60, this.by[1] - 39, 120, 99, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 38, 118, 97, 3868928);
            if (this.fz == 0) {
                string = "\u5f3a\u5316";
            } else if (this.fz == 1) {
                string = "\u5236\u70bc";
            } else if (this.fz == 2) {
                string = "\u5236\u9020";
            }
            this.b(string, this.by[0], this.by[1] - 56, 1, 0xFFFF00);
            if (this.fz != 2) {
                this.a(this.ai[13], this.by[0] - 52, this.by[1] - 37, 20, 22, 0, 0);
                this.a(this.af[this.fJ[0][3]], this.by[0] - 51, this.by[1] - 35, 18, 18, 0, 0);
                if (this.fC == 0) {
                    this.a(this.ai[14], this.by[0] - 52, this.by[1] - 37, 20, 22, 0, 0);
                }
                if (this.fJ[0][0] > 0) {
                    if (this.fz == 0) {
                        this.a(this.ai[5], this.by[0] - 17, this.by[1] - 30, 17, 7, 0, 49);
                        this.a(this.ai[8], this.by[0] + 2, this.by[1] - 30, 7, 7, 14, 0);
                        n5 = this.a(nArray, this.cq[this.fJ[0][2]][4]);
                        n6 = 0;
                        while (n6 < n5) {
                            this.a(this.aj[2], this.by[0] + 13 - n6 * 4, this.by[1] - 30, 5, 7, nArray[n6] * 5, 0);
                            ++n6;
                        }
                        this.a(this.ai[15], this.by[0] + 22, this.by[1] - 29, 3, 5, 2, 0);
                        n5 = this.a(nArray, this.cq[this.fJ[0][2]][4] + 1);
                        n6 = 0;
                        while (n6 < n5) {
                            this.a(this.aj[1], this.by[0] + 32 - n6 * 4, this.by[1] - 30, 5, 7, nArray[n6] * 5, 0);
                            ++n6;
                        }
                    } else {
                        this.b(this.fK[0], this.by[0] - 27, this.by[1] - 33, 0, 0xFFFFFF);
                    }
                } else {
                    this.b("\u9009\u62e9\u88c5\u5907", this.by[0] - 27, this.by[1] - 33, 0, 0xFFFFFF);
                }
            }
            this.a(this.ai[13], this.by[0] - 52, this.by[1] - 12, 20, 22, 0, 0);
            this.a(this.af[this.fJ[1][3]], this.by[0] - 51, this.by[1] - 10, 18, 18, 0, 0);
            if (this.fC == 1) {
                this.a(this.ai[14], this.by[0] - 52, this.by[1] - 12, 20, 22, 0, 0);
            }
            if (this.fJ[1][0] > 0) {
                this.b(this.fK[1], this.by[0] - 27, this.by[1] - 8, 0, 12647769);
                this.b(String.valueOf(Integer.toString(this.fv)) + "\u4e24", this.by[0], this.by[1] + 14, 1, 0xFFFF00);
            } else if (this.fz == 0) {
                this.b("\u6750\u6599\u7269\u54c1", this.by[0] - 27, this.by[1] - 8, 0, 12647769);
            } else if (this.fz == 1) {
                this.b("\u9009\u62e9\u5236\u70bc\u6240", this.by[0] - 27, this.by[1] - 8, 0, 12647769);
            } else if (this.fz == 2) {
                this.b("\u9009\u62e9\u5236\u9020\u6240", this.by[0] - 27, this.by[1] - 8, 0, 12647769);
            }
            n2 = 0;
            while (n2 < 2) {
                if (n2 == this.eu && this.fC == 2) {
                    n4 = 16771328;
                    this.f(this.by[0] - 50 + n2 * 52, this.by[1] + 34, 48, 23, 7208896);
                    this.f(this.by[0] - 49 + n2 * 52, this.by[1] + 35, 46, 21, 3165648);
                } else {
                    n4 = 0xFFFFFF;
                }
                if (n2 == 1) {
                    string = "\u53d6\u6d88";
                }
                this.b(string, this.by[0] - 39 + n2 * 52, this.by[1] + 38, 0, n4);
                ++n2;
            }
            return;
        }
        if (this.bu == 34) {
            if (this.eX < 10) {
                ++this.eX;
            } else {
                this.eY = 1;
            }
            this.f(this.by[0] - 60, this.by[1] - 34, 120, 22, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 33, 118, 20, 8538126);
            this.f(this.by[0] - 60, this.by[1] - 13, 120, 47, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 12, 118, 45, 3868928);
            if (this.fz == 0) {
                string = "\u5f3a\u5316\u4e2d..";
            } else if (this.fz == 1) {
                string = "\u5236\u70bc\u4e2d..";
            } else if (this.fz == 2) {
                string = "\u5236\u9020\u4e2d..";
            }
            this.b(string, this.by[0], this.by[1] - 30, 1, 0xFFFF00);
            this.g(this.by[0] - 37, this.by[1] + 15, 72, 10, 0xFFFF00);
            this.f(this.by[0] + 34 - this.eX * 7, this.by[1] + 16, this.eX * 7, 8, 0xFF0000);
            this.a(this.Y[0], this.by[0] + 19, this.by[1] - 5, 39, 29, this.eX % 2 * 39, 0);
            if (this.eY == 1) {
                Object var9_9 = null;
                long l2 = 0L;
                Object var12_11 = null;
                l2 = o._n();
                n3 = (int)l2 % 100;
                this.fM = n3 <= this.ft ? 1 : 0;
                if (this.fM == 1 && this.fz == 2) {
                    this.c(this.fu[0], this.fu[1], 0);
                    if (this.c(this.fn[1], this.fn[0], this.fu[2], this.fn[5]) < 0) {
                        this.fM = 0;
                    }
                }
                if (this.fM == 0) {
                    if (this.fz != 2) {
                        this.b(this.fJ[0][2], 0, 1, 0);
                    }
                    n2 = 0;
                    while (n2 < 3) {
                        if (this.cl[n2][0] > 0 && this.cl[n2][1] == this.fJ[0][2]) {
                            this.j(this.fJ[0][0], this.fJ[0][2]);
                            this.cl[n2][0] = 0;
                            this.cl[n2][1] = 0;
                        }
                        ++n2;
                    }
                } else {
                    this.ar();
                }
                this.v(this.bZ);
                this.w(this.bZ);
                this.aj();
                this.ce[11] = this.ce[11] - this.fv;
                this.B = 1;
                this.ax();
                this.bu = 36;
                this.bT = 0;
                this.eY = 0;
                return;
            }
        } else if (this.bu == 36) {
            this.f(this.by[0] - 60, this.by[1] - 60, 120, 22, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 59, 118, 20, 8538126);
            this.f(this.by[0] - 60, this.by[1] - 39, 120, 99, 14664086);
            this.f(this.by[0] - 59, this.by[1] - 38, 118, 97, 3868928);
            if (this.fM == 0) {
                if (this.fz == 0) {
                    string = "\u5f3a\u5316\u5931\u8d25!!";
                } else if (this.fz == 1) {
                    string = "\u5236\u70bc\u5931\u8d25!!";
                } else if (this.fz == 2) {
                    string = "\u5236\u9020\u5931\u8d25!!";
                }
            } else if (this.fM == 1) {
                if (this.fz == 0) {
                    string = "\u5f3a\u5316\u6210\u529f!!";
                } else if (this.fz == 1) {
                    string = "\u5236\u70bc\u6210\u529f!!";
                } else if (this.fz == 2) {
                    string = "\u5236\u4f5c\u6210\u529f!!";
                }
            }
            this.b(string, this.by[0], this.by[1] - 56, 1, 0xFFFF00);
            if (this.fM == 0) {
                this.b("\u8fd9\u79cd\u6750\u6599", this.by[0], this.by[1] - 20, 1, 0xFFFFFF);
                this.b("\u592a\u70c2\u4e86\u3002", this.by[0], this.by[1], 1, 0xFFFFFF);
            } else if (this.fM == 1) {
                this.b("\u592a\u597d\u4e86.", this.by[0], this.by[1] - 20, 1, 0xFFFFFF);
                this.b("\u4f60\u8fd0\u6c14\u771f\u597d!", this.by[0], this.by[1], 1, 0xFFFFFF);
            }
            this.f(this.by[0] - 24, this.by[1] + 34, 48, 23, 7208896);
            this.f(this.by[0] - 23, this.by[1] + 35, 46, 21, 3165648);
            this.b("\u786e\u8ba4", this.by[0], this.by[1] + 38, 1, 0xFFFF00);
        }
    }

    public final void keyPressed(int n2) {
        if (this.r) {
            if (n2 == -5) {
                this.showNotify();
                this.d.a();
                this.r = false;
            }
            return;
        }
        boolean bl = false;
        boolean bl2 = false;
        if (this.ap != 0) {
            return;
        }
        if (n2 == -13 || n2 == -14) {
            this.aV();
            return;
        }
        if (this.bN == 0) {
            this.T(n2);
            if (this.bN == 1) {
                this.g(this.bO, 0);
                return;
            }
        } else {
            switch (n2) {
                case -5: 
                case 53: {
                    if (this.bQ < this.bR - 1) {
                        ++this.bQ;
                        return;
                    }
                    this.bN = 0;
                }
            }
        }
    }

    public final void keyReleased(int n2) {
        boolean bl = false;
        boolean bl2 = false;
        int n3 = 0;
        if (this.ap != 0) {
            return;
        }
        this.ex = 0;
        block0 : switch (this.bu) {
            case 23: {
                if (this.bG == 1) {
                    this.bF = 0;
                }
                this.bE = 0;
                if (this.eb == 1) {
                    this.eb = 0;
                    this.dA = 0;
                    this.bq = 0;
                    this.aW();
                }
                if (this.bD == 1) {
                    switch (n2) {
                        case -4: 
                        case -3: 
                        case -2: 
                        case -1: 
                        case 50: 
                        case 52: 
                        case 54: 
                        case 56: {
                            if (this.bG != 1) break block0;
                            this.bE = 0;
                        }
                    }
                    return;
                }
                if (this.bH != 1 || this.dp != 0 || this.dA == 0) break;
                switch (n2) {
                    case 48: 
                    case 49: 
                    case 51: 
                    case 55: 
                    case 57: {
                        if (this.bZ == 0 && this.dB == 2 || this.bZ == 1 && this.dB == 3 || this.dC != 7) break block0;
                        n3 = this.ei - this.dx;
                        if (n3 < 0) {
                            n3 += 9999;
                        }
                        if (n3 > this.dz && this.do == n2) {
                            this.ds = this.dB < 4 ? 1 : 0;
                            this.dr = this.dB;
                            this.dp = 1;
                            this.dq = 0;
                            this.bE = 0;
                            this.bD = 0;
                            this.dA = 0;
                            if (this.fm != 0) break block0;
                            this.ce[3] = this.ce[3] - this.dy;
                            if (this.ce[3] >= 0) break block0;
                            this.ce[3] = 0;
                            return;
                        }
                        this.cI = 0;
                        this.bD = 1;
                        this.dA = 0;
                        this.aW();
                        return;
                    }
                }
                this.cI = 0;
                this.bD = 1;
                this.dA = 0;
                this.aW();
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void T(int var1_1) {
        block104: {
            var2_2 = 0;
            var3_3 = 0;
            block0 : switch (this.bu) {
                case 1: {
                    if (!this.C) break;
                    switch (var1_1) {
                        case -6: {
                            o.g = 1;
                            this.ei = 0;
                            this.ax = 0;
                            this.fV = 255;
                            var7_4 = null;
                            var8_6 = 0L;
                            var10_8 = null;
                            this.bo = var8_6 = o._n();
                            this.bu = 3;
                            this.bv = 3;
                            this.a(1);
                            if (!this.aU()) {
                                this.bJ = 7;
                                this.c.a[0] = 7;
                                this.c.a[2] = 1;
                                this.bG = 1;
                                this.c.a[3] = 1;
                                this.gm = 0;
                                this.c.a[4] = 0;
                                this.bS = 1;
                                this.c.a[5] = 1;
                                this.bL = 0;
                                this.c.a[6] = 0;
                                this.c.b();
                            }
                            this.d.a();
                            this.bK = (9 - this.bJ) * 30;
                            return;
                        }
                        case -7: {
                            o.g = 0;
                            this.ei = 0;
                            this.ax = 0;
                            this.fV = 255;
                            var7_5 = null;
                            var8_7 = 0L;
                            var10_9 = null;
                            this.bo = var8_7 = o._n();
                            this.bu = 3;
                            this.bv = 3;
                            this.a(1);
                            if (!this.aU()) {
                                this.bJ = 7;
                                this.c.a[0] = 7;
                                this.c.a[2] = 1;
                                this.bG = 1;
                                this.c.a[3] = 1;
                                this.gm = 0;
                                this.c.a[4] = 0;
                                this.bS = 1;
                                this.c.a[5] = 1;
                                this.bL = 0;
                                this.c.a[6] = 0;
                                this.c.b();
                            }
                            this.bK = (9 - this.bJ) * 30;
                        }
                    }
                    return;
                }
                case 3: {
                    if (var1_1 != -5) break;
                    this.A();
                    this.bu = 4;
                    this.bv = 4;
                    this.b();
                    this.a(1);
                    return;
                }
                case 4: {
                    this.U(var1_1);
                    return;
                }
                case 5: 
                case 6: {
                    if (this.bI != 0) break;
                    this.V(var1_1);
                    return;
                }
                case 8: {
                    switch (var1_1) {
                        case -4: 
                        case 54: {
                            if (this.i >= 5) break block0;
                            ++this.i;
                            return;
                        }
                        case -3: 
                        case 52: {
                            if (this.i <= 1) break block0;
                            --this.i;
                            return;
                        }
                        case -7: {
                            this.bu = 4;
                            this.at = 0;
                            this.av = 5;
                            this.i = 1;
                        }
                    }
                    return;
                }
                case 10: {
                    this.W(var1_1);
                    return;
                }
                case 9: {
                    this.X(var1_1);
                    return;
                }
                case 23: {
                    System.out.println("GM_GAMEING");
                    if (this.bM > 0) {
                        switch (var1_1) {
                            case -7: 
                            case -5: 
                            case 53: {
                                System.out.println("\u7ec3\u4e60\u6a21\u5f0f\u7b49\u4e2d\u5728GM_GAMING\u72b6\u6001\u4e0b\u7684\u63d0\u793a\u4fe1\u606f\u5904\u7406");
                                if (this.bM == 1) {
                                    System.out.println("GM_GAMING--1");
                                    if (this.bQ < this.bR - 1) {
                                        ++this.bQ;
                                        this.bT = 0;
                                        return;
                                    }
                                    this.bM = 0;
                                    this.bI = 0;
                                    this.bT = 0;
                                    this.cC = this.g(this.cC);
                                    return;
                                }
                                if (this.bM == 2) {
                                    System.out.println("GM_GAMING--2");
                                    this.bM = 0;
                                    this.bI = 0;
                                    this.bT = 0;
                                    return;
                                }
                                if (this.bM != 3) break block0;
                                System.out.println("GM_GAMING--3");
                                this.bM = 0;
                                this.bI = 0;
                                this.bT = 0;
                                return;
                            }
                            case -1: 
                            case 50: {
                                if (this.bM != 2) break block0;
                                if (this.eR > 0) {
                                    --this.eR;
                                }
                                this.bT = 0;
                                return;
                            }
                            case -2: 
                            case 56: {
                                if (this.bM != 2) break block0;
                                if (this.eR < this.eT - 5) {
                                    ++this.eR;
                                }
                                this.bT = 0;
                            }
                        }
                        return;
                    }
                    System.out.println("\u5965\u5df4\u9a6celse");
                    if (this.bD == 1) {
                        this.Y(var1_1);
                        System.out.println("\u5965\u5df4\u9a6caaaa");
                        switch (var1_1) {
                            case 49: {
                                var3_3 = 1;
                                break;
                            }
                            case 51: {
                                var3_3 = 2;
                                break;
                            }
                            case 55: {
                                var3_3 = 3;
                                break;
                            }
                            case 57: {
                                var3_3 = 4;
                                break;
                            }
                            case 48: {
                                var3_3 = 5;
                            }
                        }
                        if (var3_3 <= 0) break;
                        System.out.println("\u5965\u5df4\u9a6cddddd");
                        this.dC = this.cj[var3_3 - 1][0];
                        if (this.dC <= 0) break;
                        this.dB = this.cj[var3_3 - 1][1];
                        this.dD = var3_3 - 1;
                        if (this.dC == 7 && (this.cj[var3_3 - 1][2] == 0 || this.bY != this.bZ || this.cl[0][0] == 0 || this.bH != 1)) {
                            this.dC = 0;
                        }
                        if (this.dC <= 0) break;
                        this.do = var1_1;
                        this.f(this.dC, this.dB);
                        if (this.gF != 1) break;
                        this.fP = 1;
                        this.fQ = 0;
                        this.ap = 10;
                        return;
                    }
                    System.out.println("\u5965\u5df4\u9a6cbbbbb");
                    if (this.bZ == 0) {
                        if (this.dp != 1 || this.dr != 1 || this.dq != 1) break;
                        switch (var1_1) {
                            case -1: 
                            case 50: {
                                var2_2 = 0;
                                break;
                            }
                            case -2: 
                            case 56: {
                                var2_2 = 1;
                                break;
                            }
                            case -3: 
                            case 52: {
                                var2_2 = 2;
                                break;
                            }
                            case -4: 
                            case 54: {
                                var2_2 = 3;
                            }
                        }
                        this.u(var2_2);
                        return;
                    }
                    if (this.bZ != 1 || this.cK != 1 || this.cJ != 1 || (this.co - 1) % 3 != 1) break;
                    switch (var1_1) {
                        case -1: 
                        case 50: {
                            this.cC = 0;
                            break;
                        }
                        case -2: 
                        case 56: {
                            this.cC = 1;
                            break;
                        }
                        case -3: 
                        case 52: {
                            this.cC = 2;
                            break;
                        }
                        case -4: 
                        case 54: {
                            this.cC = 3;
                        }
                    }
                    this.cD = this.cC;
                    return;
                }
                case 13: {
                    this.Z(var1_1);
                    return;
                }
                case 14: {
                    this.aa(var1_1);
                    return;
                }
                case 15: {
                    this.ab(var1_1);
                    return;
                }
                case 17: {
                    this.ac(var1_1);
                    return;
                }
                case 18: {
                    this.ad(var1_1);
                    return;
                }
                case 19: {
                    this.ae(var1_1);
                    return;
                }
                case 20: 
                case 35: {
                    this.af(var1_1);
                    return;
                }
                case 22: 
                case 24: {
                    this.ag(var1_1);
                    return;
                }
                case 21: 
                case 29: {
                    this.ah(var1_1);
                    return;
                }
                case 11: {
                    switch (var1_1) {
                        case -6: 
                        case 35: {
                            this.aW();
                            this.bD = 0;
                            this.ap = 4;
                            if (var1_1 == 35) {
                                this.eu = 1;
                                this.F = 0;
                                return;
                            }
                            if (var1_1 != -6) break block0;
                            this.eu = 0;
                            this.fP = 1;
                            this.fQ = 0;
                        }
                    }
                    return;
                }
                case 25: 
                case 31: {
                    this.al(var1_1);
                    return;
                }
                case 26: {
                    this.ai(var1_1);
                    return;
                }
                case 27: 
                case 28: {
                    this.aj(var1_1);
                    return;
                }
                case 30: {
                    this.ak(var1_1);
                    return;
                }
                case 32: {
                    this.am(var1_1);
                    return;
                }
                case 33: {
                    this.an(var1_1);
                    return;
                }
                case 36: {
                    this.ao(var1_1);
                    return;
                }
                case 37: {
                    this.ap(var1_1);
                    return;
                }
                case 40: {
                    if (var1_1 != -5 && var1_1 != -7 && var1_1 != 53) break;
                    this.ew = 0;
                    this.eA = 0;
                    this.bT = 0;
                    var4_10 = 0;
                    if (this.fC == 0 || this.fC == 2) {
                        if (this.fC == 0) {
                            this.bu = 27;
                        }
                        this.c(this.fA[0], this.fA[1], 0);
                        return;
                    }
                    if (this.fC == 1) {
                        this.bu = 28;
                        if (this.eB == 0) {
                            this.cw = this.q(5);
                        } else if (this.eB == 1 || this.eB == 2) {
                            this.cw = this.q(this.eB + 10);
                        }
                        var4_10 = this.eA * 5 + this.ew;
                        this.l(this.cv[var4_10][0], this.cv[var4_10][1]);
                        return;
                    }
                    if (this.fC != 3) break;
                    this.bu = 26;
                    this.fz = 1;
                    return;
                }
                case 41: {
                    var5_12 = new int[12];
                    var6_13 = 0;
                    switch (var1_1) {
                        case -5: 
                        case 53: {
                            if (this.ey != 0) ** GOTO lbl340
                            if (this.fh[57] > 10) {
                                if (this.fh[57] < 13) {
                                    var6_13 = this.f(this.fh[57], this.fh[58] + 1, this.fh[59]);
                                }
                            } else if (this.fh[57] != 6 || this.fh[58] <= 71) {
                                var4_11 = 0;
                                while (var4_11 < 12) {
                                    var5_12[var4_11] = this.fn[var4_11];
                                    ++var4_11;
                                }
                                var5_12[4] = this.fn[1] > 1 && this.fn[1] < 6 ? 0 : this.fh[59];
                                var6_13 = this.c(var5_12[1], var5_12[0], var5_12[4], var5_12[5]);
                            } else if (this.fh[57] == 6 && this.fh[58] == 72) {
                                if (this.cr > 40) {
                                    var6_13 = -4;
                                }
                            } else if (this.fh[57] == 6 && this.fh[58] == 73 && this.cr == 50) {
                                var6_13 = -4;
                            }
                            if (var6_13 < 0) {
                                this.bN = 1;
                            }
                            if (var6_13 == -1) {
                                this.bO = 1;
                                return;
                            }
                            if (var6_13 == -2) {
                                this.bO = 2;
                                return;
                            }
                            if (var6_13 == -3) {
                                this.bO = 10;
                                return;
                            }
                            if (var6_13 == -4) {
                                this.bO = 37;
                                return;
                            }
                            break block104;
lbl340:
                            // 1 sources

                            this.bu = 26;
                            this.fz = 1;
                            return;
                        }
                        case -4: 
                        case -3: 
                        case 52: 
                        case 54: {
                            if (this.ey == 0) {
                                this.ey = 1;
                                return;
                            }
                            this.ey = 0;
                        }
                    }
                }
            }
        }
    }

    public final void U(int n2) {
        switch (n2) {
            case -5: 
            case 53: {
                this.at = 0;
                this.aw = 0;
                switch (this.au) {
                    case 0: {
                        this.ay();
                        if (this.dU == 0) {
                            if (this.B == 1) {
                                this.dU = 1;
                                this.dS = 0;
                                return;
                            }
                            this.b(0);
                            this.bu = 11;
                            return;
                        }
                        if (this.dS == 0) {
                            this.B = 0;
                            this.ax();
                            this.b(0);
                            this.bu = 11;
                            return;
                        }
                        this.dU = 0;
                        this.dS = 0;
                        this.au = 0;
                        return;
                    }
                    case 1: {
                        if (this.E == 1) {
                            this.E = 0;
                            return;
                        }
                        this.ay();
                        if (this.B == 1) {
                            this.ay();
                            this.aW();
                            this.ap = 3;
                            this.fP = 1;
                            this.fQ = 0;
                            this.bD = 0;
                            return;
                        }
                        this.E = 1;
                        return;
                    }
                    case 2: {
                        this.bu = 6;
                        this.av = 3;
                        this.dU = 0;
                        this.dS = 0;
                        return;
                    }
                    case 3: {
                        this.bu = 9;
                        this.ax = 0;
                        return;
                    }
                    case 4: {
                        this.i = 1;
                        this.bu = 8;
                        this.ax = 0;
                        return;
                    }
                    case 5: {
                        this.b = null;
                        this.a.notifyDestroyed();
                    }
                }
                return;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.dS == 0) {
                    this.dS = 1;
                    return;
                }
                this.dS = 0;
                return;
            }
            case -1: 
            case 50: {
                if (this.E == 1) {
                    return;
                }
                this.at = 0;
                if (this.au > 0) {
                    --this.au;
                    return;
                }
                this.au = this.av;
                return;
            }
            case -2: 
            case 56: {
                if (this.E == 1) {
                    return;
                }
                this.at = 0;
                if (this.au < this.av) {
                    ++this.au;
                    return;
                }
                this.au = 0;
            }
        }
    }

    public final void V(int n2) {
        int n3 = 0;
        block0 : switch (n2) {
            case -5: 
            case 53: {
                switch (this.bu) {
                    case 5: {
                        if (this.dU == 0) {
                            if (this.aw == 0) {
                                if (this.gm == 1) {
                                    this.dU = 1;
                                    this.dS = 0;
                                    return;
                                }
                                this.b(0);
                                this.bu = 11;
                                return;
                            }
                            if (this.aw != 1) break block0;
                            if (this.gm == 1) {
                                this.ay();
                                this.aW();
                                this.ap = 3;
                                this.fP = 1;
                                this.fQ = 0;
                                this.bD = 0;
                                return;
                            }
                            this.b(0);
                            this.bu = 11;
                            return;
                        }
                        if (this.dS == 0) {
                            this.b(0);
                            this.bu = 11;
                            return;
                        }
                        this.dU = 0;
                        return;
                    }
                    case 6: {
                        this.at = 0;
                        this.ax = 0;
                        switch (this.aw) {
                            case 0: {
                                this.bu = 10;
                                n3 = 5;
                                break;
                            }
                            case 1: {
                                this.bu = 10;
                                n3 = 1;
                                break;
                            }
                            case 2: {
                                this.bu = 10;
                                n3 = 6;
                                break;
                            }
                            case 3: {
                                this.bu = 10;
                                n3 = 7;
                            }
                        }
                        if (this.aw >= 4) break block0;
                        this.g(n3, 3);
                        this.eT /= 4;
                        if (this.aw != 3) break block0;
                        ++this.eT;
                    }
                }
                return;
            }
            case -1: 
            case 50: {
                if (this.dU != 0) break;
                this.at = 0;
                if (this.aw > 0) {
                    --this.aw;
                    return;
                }
                this.aw = this.av - 1;
                return;
            }
            case -2: 
            case 56: {
                if (this.dU != 0) break;
                this.at = 0;
                if (this.aw < this.av - 1) {
                    ++this.aw;
                    return;
                }
                this.aw = 0;
                return;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.dS == 0) {
                    this.dS = 1;
                    return;
                }
                this.dS = 0;
                return;
            }
            case -7: {
                if (this.dU == 0) {
                    this.bu = 4;
                    this.at = 0;
                    this.av = 5;
                    return;
                }
                this.dU = 0;
            }
        }
    }

    public final void W(int n2) {
        switch (n2) {
            case -7: {
                this.bu = 6;
                this.at = 0;
                this.av = 3;
                return;
            }
            case -4: 
            case 54: {
                if (this.eR >= this.eT - 1) break;
                ++this.eR;
                return;
            }
            case -3: 
            case 52: {
                if (this.eR <= 0) break;
                --this.eR;
            }
        }
    }

    public final void X(int n2) {
        boolean bl = false;
        switch (n2) {
            case -7: {
                this.at = 0;
                this.av = 5;
                this.bu = 4;
                this.aV();
                break;
            }
            case -1: 
            case 50: {
                if (this.ax > 0) {
                    --this.ax;
                    break;
                }
                this.ax = 1;
                break;
            }
            case -2: 
            case 56: {
                if (this.ax < 1) {
                    ++this.ax;
                    break;
                }
                this.ax = 0;
                break;
            }
            case -3: 
            case 52: {
                if (this.ax == 0) {
                    if ((g ^= 1) == 1) {
                        this.d.a();
                        break;
                    }
                    this.d.b();
                    break;
                }
                if (this.ax == 1) {
                    if (this.bJ > 0) {
                        --this.bJ;
                        break;
                    }
                    this.bJ = 9;
                    break;
                }
                if (this.ax == 2) {
                    if (this.bG == 0) {
                        this.bG = 1;
                        break;
                    }
                    this.bG = 0;
                    break;
                }
                if (this.ax != 3) break;
                if (this.bS == 0) {
                    this.bS = 1;
                    break;
                }
                this.bS = 0;
                break;
            }
            case -4: 
            case 54: {
                if (this.ax == 0) {
                    if ((g ^= 1) == 1) {
                        this.d.a();
                        break;
                    }
                    this.d.b();
                    break;
                }
                if (this.ax == 2) {
                    if (this.bG == 0) {
                        this.bG = 1;
                        break;
                    }
                    this.bG = 0;
                    break;
                }
                if (this.ax == 1) {
                    if (this.bJ < 9) {
                        ++this.bJ;
                        break;
                    }
                    this.bJ = 0;
                    break;
                }
                if (this.ax == 2) {
                    if (this.bG == 0) {
                        this.bG = 1;
                        break;
                    }
                    this.bG = 0;
                    break;
                }
                if (this.ax != 3) break;
                this.bS = this.bS == 0 ? 1 : 0;
            }
        }
        this.bK = (9 - this.bJ) * 30;
    }

    public final void Y(int n2) {
        int n3 = 0;
        int[][] nArrayArray = new int[][]{{3, 4, 3, 4, 2}, {4, 5, 5, 6, 4}};
        switch (n2) {
            case -5: 
            case 53: {
                this.bE = 0;
                System.out.println("\u653b\u51fb----------------");
                if (this.cF != 0) break;
                System.out.println("11111111");
                if (this.fd != -1) {
                    System.out.println("22222:" + this.fd);
                    this.ff = this.aX[this.fd].j;
                    return;
                }
                if (this.fe != -1) {
                    System.out.println("3333");
                    this.fg = 1;
                    return;
                }
                if (this.bH != 1 || this.cF != 0) break;
                System.out.println("444444");
                n3 = this.Z();
                if (n3 > -1) {
                    this.cC = n3;
                }
                this.cF = 1;
                this.cH = 0;
                this.cJ = 0;
                this.cD = this.cC;
                if (this.co == 0 || this.bY != this.bZ || this.cl[0][0] == 0) {
                    System.out.println("555555");
                    this.cG = 2;
                } else if (this.co < 4) {
                    System.out.println("666666");
                    this.cG = this.cp[this.co - 1][0] < 3 ? nArrayArray[this.bZ][this.cp[this.co - 1][0] - 1] : nArrayArray[this.bZ][(this.co - 1) % 3 + 2];
                } else {
                    System.out.println("777777");
                    this.cJ = 1;
                    this.cK = 0;
                    this.cL = 0;
                    this.cM = 2;
                    this.dk = 0;
                    this.dl = 4;
                }
                this.dj = 0;
                this.bD = 0;
                return;
            }
            case -1: 
            case 50: {
                if (this.bE == 0 || this.bG == 1) {
                    this.bE = 1;
                    this.bF = 1;
                } else if (this.cC == 0) {
                    this.bE = 0;
                }
                this.cC = 0;
                this.cD = 0;
                return;
            }
            case -2: 
            case 56: {
                if (this.bE == 0 || this.bG == 1) {
                    this.bE = 1;
                    this.bF = 1;
                } else if (this.cC == 1) {
                    this.bE = 0;
                }
                this.cC = 1;
                this.cD = 1;
                return;
            }
            case 55: {
                return;
            }
            case 49: {
                return;
            }
            case 48: {
                return;
            }
            case 51: {
                return;
            }
            case 57: {
                return;
            }
            case -3: 
            case 52: {
                if (this.bE == 0 || this.bG == 1) {
                    this.bE = 1;
                    this.bF = 1;
                } else if (this.cC == 2) {
                    this.bE = 0;
                }
                this.cC = 2;
                this.cD = 2;
                return;
            }
            case -4: 
            case 54: {
                if (this.bE == 0 || this.bG == 1) {
                    this.bE = 1;
                    this.bF = 1;
                } else if (this.cC == 3) {
                    this.bE = 0;
                }
                this.cC = 3;
                this.cD = 3;
                return;
            }
            case 35: {
                if (this.bH != 1) break;
                this.bE = 0;
                this.eb = 1;
                this.dA = 1;
                this.dx = this.ei;
                this.dz = this.ed * 5 + 10 - this.ee;
                this.cI = 1;
                this.cC = 1;
                this.cD = 1;
                this.bq = 3;
                return;
            }
            case 42: {
                this.bI = 1;
                this.bu = 25;
                this.g(this.bV, 1);
                this.aM = 0;
                return;
            }
            case -7: 
            case -6: {
                this.aq(n2);
            }
        }
    }

    private void aq(int n2) {
        if (this.cJ == 0 && this.cF != 1 && this.cI == 0) {
            if (this.bW == 1 && this.bH == 1) {
                this.G();
                this.H();
            }
            this.L();
            this.ao = 1;
            this.bI = 1;
            this.bu = 13;
            this.eq = 1;
            this.er = 0;
            this.es = 0;
            this.ey = 0;
            this.aM = 0;
            this.bT = 0;
            this.ej = 0;
            this.fe = -1;
            if (n2 == -6) {
                this.er = 1;
                this.es = 5;
                this.et = 0;
                this.bu = 13 + this.es + 1;
                this.eu = 0;
                this.ev = 0;
                this.ex = 0;
                this.eA = 1;
            }
            this.e(1);
            this.x();
        }
    }

    public final void Z(int n2) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        switch (n2) {
            case -5: 
            case 53: {
                if (this.fm == 1 && this.es == 4) break;
                this.er = 1;
                if (this.es == 2) {
                    this.et = 2;
                    this.bu = 20;
                    this.ew = 0;
                } else {
                    this.et = 0;
                    this.bu = 13 + this.es + 1;
                    if (this.es == 5) {
                        this.eA = 0;
                    }
                }
                this.bT = 0;
                break;
            }
            case -3: 
            case 52: {
                this.es = this.es > 0 ? --this.es : 5;
                this.bT = 0;
                break;
            }
            case -4: 
            case 54: {
                this.es = this.es < 5 ? ++this.es : 0;
                this.bT = 0;
                break;
            }
            case -7: {
                this.ap = 2;
                this.gF = 0;
            }
        }
        if (this.es == 4) {
            this.et = 0;
            if (this.fm == 0) {
                this.al();
            }
        } else if (this.es == 2) {
            this.eB = 0;
            this.cw = this.q(5);
            this.l(this.cv[0][0], this.cv[0][1]);
        }
        this.eu = 0;
        this.ev = 0;
        this.ew = 0;
        this.ex = 0;
        this.eA = 0;
    }

    public final void aa(int n2) {
        switch (n2) {
            case -5: 
            case 53: {
                this.ex = 1;
                if (this.ci <= 0) break;
                int n3 = this.eu + 4;
                this.ce[n3] = this.ce[n3] + 1;
                this.v(this.bZ);
                this.w(this.bZ);
                this.aj();
                if (this.eu == 0) {
                    this.ct = 200 + this.ce[4] * 20;
                }
                --this.ci;
                this.bT = 0;
                return;
            }
            case -1: 
            case 50: {
                this.eu = this.eu > 0 ? (this.eu -= 2) : (this.eu += 2);
                return;
            }
            case -2: 
            case 56: {
                this.eu = this.eu < 2 ? (this.eu += 2) : (this.eu -= 2);
                return;
            }
            case -3: 
            case 52: {
                this.eu = this.eu > 0 ? --this.eu : 3;
                return;
            }
            case -4: 
            case 54: {
                this.eu = this.eu < 3 ? ++this.eu : 0;
                return;
            }
            case -7: {
                this.bu = 13;
                this.er = 0;
            }
        }
    }

    public final void ab(int n2) {
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        switch (n2) {
            case -5: 
            case 53: {
                this.bu = 20;
                this.ew = 0;
                this.bT = 0;
                this.cw = this.et == 0 ? this.q(this.eu) : this.q(3);
                this.l(this.cv[0][0], this.cv[0][1]);
                break;
            }
            case -2: 
            case -1: 
            case 50: 
            case 56: {
                this.et = this.et == 0 ? 1 : 0;
                bl = true;
                break;
            }
            case -3: 
            case 52: {
                if (this.et == 0) {
                    if (this.eu > 0) {
                        --this.eu;
                        break;
                    }
                    this.eu = 2;
                    break;
                }
                if (this.et != 1) break;
                if (this.ev > 0) {
                    --this.ev;
                    break;
                }
                this.ev = 4;
                break;
            }
            case -4: 
            case 54: {
                if (this.et == 0) {
                    if (this.eu < 2) {
                        ++this.eu;
                        break;
                    }
                    this.eu = 0;
                    break;
                }
                if (this.et != 1) break;
                if (this.ev < 4) {
                    ++this.ev;
                    break;
                }
                this.ev = 0;
                break;
            }
            case 42: {
                if (this.et == 0) {
                    if (this.cl[this.eu][0] > 0) {
                        n3 = this.cq[this.cl[this.eu][1]][0];
                        n4 = this.cq[this.cl[this.eu][1]][1];
                        n5 = 0;
                        while (n5 < 12) {
                            this.fn[n5] = this.cq[this.cl[this.eu][1]][n5];
                            ++n5;
                        }
                    } else {
                        n4 = 0;
                    }
                } else {
                    n4 = this.cj[this.ev][0];
                    if (n4 == 1) {
                        n3 = this.cq[this.cj[this.ev][1]][0];
                    } else if (n4 == 7) {
                        n3 = this.cj[this.ev][1];
                        if (this.bZ == 1) {
                            n3 += 6;
                        }
                    }
                }
                if (n4 <= 0) break;
                this.bw = this.bu;
                this.bu = 21;
                this.bT = 0;
                this.eR = 0;
                if (n4 > 1 && n4 < 6) {
                    this.ez = 0;
                    n6 = 1;
                } else {
                    this.ez = 1;
                }
                this.c(n4, n3, n6);
                break;
            }
            case -7: {
                this.bu = 13;
                this.er = 0;
                this.v(this.bZ);
                this.w(this.bZ);
                this.aj();
            }
        }
        if (bl) {
            if (this.et == 0) {
                this.eu = 0;
                return;
            }
            if (this.et == 1) {
                this.ev = 0;
            }
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     */
    public final void ac(int var1_1) {
        var2_2 = 0;
        var3_3 = 0;
        var4_4 = 0;
        var5_5 = false;
        switch (var1_1) {
            case -5: 
            case 53: {
                if (this.ey != 0) ** GOTO lbl14
                this.ey = this.eu + 1;
                this.ev = 0;
                this.bT = 0;
                if (this.eu == 0 && this.cp[0][0] > 0) {
                    var3_3 = 1;
                }
                ** GOTO lbl63
lbl14:
                // 1 sources

                if (this.ey != 1) ** GOTO lbl29
                if (this.ev <= 2) ** GOTO lbl25
                System.out.println("00000");
                if (this.cp[this.ev - 3][0] == 6) {
                    var5_5 = true;
                    System.out.println("11111");
                } else {
                    System.out.println("2222");
                    this.bN = 1;
                    this.bO = 38;
                }
                ** GOTO lbl26
lbl25:
                // 1 sources

                var5_5 = true;
lbl26:
                // 3 sources

                if (var5_5 && this.cp[this.ev][0] > 0) {
                    this.co = this.ev + 1;
                }
                ** GOTO lbl63
lbl29:
                // 1 sources

                if (this.ey == 2) {
                    if (this.cn > 0 && this.cm[this.ev] > 0 && this.cm[this.ev] < 6) {
                        this.bT = 0;
                        v0 = this.ev;
                        this.cm[v0] = this.cm[v0] + 1;
                        --this.cn;
                        this.t(this.ev);
                    }
                } else if (this.ey == 3 && this.ev != 3 && this.cn > 0 && this.cz[this.ev] > 0 && this.cz[this.ev] < 3) {
                    v1 = this.ev;
                    this.cz[v1] = this.cz[v1] + 1;
                    switch (this.ev) {
                        case 0: {
                            if (this.bZ == 0) {
                                this.ce[4] = this.ce[4] + this.cz[this.ev] * 5;
                                break;
                            }
                            this.ce[7] = this.ce[7] + this.cz[this.ev] * 5;
                            break;
                        }
                        case 1: {
                            this.ec += 2;
                            this.ed = 0;
                            break;
                        }
                        case 2: {
                            if (this.bZ == 0) {
                                this.ce[6] = this.ce[6] + this.cz[this.ev] * 5;
                                break;
                            }
                            this.ce[5] = this.ce[5] + this.cz[this.ev] * 5;
                            break;
                        }
                    }
                    this.v(this.bZ);
                    this.w(this.bZ);
                    this.aj();
                }
lbl63:
                // 6 sources

                if (this.bN == 1) break;
                break;
            }
            case -1: 
            case 50: {
                if (this.ey == 0) {
                    if (this.eu > 0) {
                        --this.eu;
                        break;
                    }
                    this.eu = 2;
                    break;
                }
                if (this.ey == 1) {
                    this.ev = this.ev < 3 ? (this.ev += 3) : (this.ev -= 3);
                    var3_3 = 1;
                    break;
                }
                if (this.ey == 2) {
                    if (this.ev < 2) {
                        this.ev += 4;
                        break;
                    }
                    this.ev -= 2;
                    break;
                }
                if (this.ey != 3) break;
                if (this.ev > 0) {
                    --this.ev;
                    break;
                }
                this.ev = 3;
                break;
            }
            case -2: 
            case 56: {
                if (this.ey == 0) {
                    if (this.eu < 2) {
                        ++this.eu;
                        break;
                    }
                    this.eu = 0;
                    break;
                }
                if (this.ey == 1) {
                    this.ev = this.ev < 3 ? (this.ev += 3) : (this.ev -= 3);
                    var3_3 = 1;
                    break;
                }
                if (this.ey == 2) {
                    if (this.ev > 3) {
                        this.ev -= 4;
                        break;
                    }
                    this.ev += 2;
                    break;
                }
                if (this.ey != 3) break;
                if (this.ev < 3) {
                    ++this.ev;
                    break;
                }
                this.ev = 0;
                break;
            }
            case -3: 
            case 52: {
                if (this.ey == 1) {
                    this.ev = this.ev > 0 ? --this.ev : 5;
                    var3_3 = 1;
                    break;
                }
                if (this.ey != 2) break;
                if (this.ev > 0) {
                    --this.ev;
                    break;
                }
                this.ev = 5;
                break;
            }
            case -4: 
            case 54: {
                if (this.ey == 1) {
                    this.ev = this.ev < 5 ? ++this.ev : 0;
                    var3_3 = 1;
                    break;
                }
                if (this.ey != 2) break;
                if (this.ev < 5) {
                    ++this.ev;
                    break;
                }
                this.ev = 0;
                break;
            }
            case -7: {
                if (this.ey == 0) {
                    this.bu = 13;
                    this.er = 0;
                } else {
                    this.ey = 0;
                }
                this.bT = 0;
                break;
            }
            case 42: {
                if (this.ey == 1) {
                    if (this.cp[this.ev][0] <= 0) break;
                    var3_3 = 3;
                    break;
                }
                if (this.ey == 2) {
                    var2_2 = this.ev;
                    if (this.bZ == 1) {
                        var2_2 += 6;
                    }
                    var4_4 = 7;
                    var3_3 = 2;
                    break;
                }
                if (this.ey != 3) break;
                var2_2 = this.ev;
                if (this.bZ == 1) {
                    var2_2 += 4;
                }
                var4_4 = 9;
                var3_3 = 2;
                break;
            }
        }
        if (var3_3 > 0) {
            if (var3_3 > 1) {
                this.bw = this.bu;
                this.bu = 21;
                this.bT = 0;
                this.eR = 0;
                this.ez = 1;
            }
            if (var3_3 == 1 || var3_3 == 3) {
                var2_2 = this.ev;
                if (this.bZ == 1) {
                    var2_2 += 6;
                }
                var4_4 = 8;
            }
            this.c(var4_4, var2_2, 0);
        }
    }

    public final void ad(int n2) {
        switch (n2) {
            case -5: 
            case 53: {
                if (this.et == 0) {
                    if (this.eJ[this.eu][1] <= 0) break;
                    this.et = 1;
                    this.ev = 0;
                    this.bT = 0;
                    this.A(this.eu);
                    this.e(this.eJ[this.eu][0], this.eu, this.eJ[this.eu][0]);
                    return;
                }
                if (this.et == 1) {
                    this.et = 0;
                    this.bT = 0;
                    return;
                }
                if (this.et != 2) break;
                if (this.ew == 0) {
                    int n3 = this.eJ[this.eu][25];
                    this.eI[n3] = this.eI[n3] + 1;
                    this.fh[50 + this.eu] = 0;
                    int n4 = 0;
                    while (n4 < 26) {
                        this.eJ[this.eu][n4] = 0;
                        ++n4;
                    }
                    this.aw();
                    this.at();
                }
                this.et = 0;
                this.bT = 0;
                return;
            }
            case -1: 
            case 50: {
                if (this.et == 0) {
                    if (this.eu > 0) {
                        --this.eu;
                    }
                    return;
                }
                if (this.et != 1 || this.eR <= 0) break;
                --this.eR;
                return;
            }
            case -2: 
            case 56: {
                if (this.et == 0) {
                    if (this.eu < 3) {
                        ++this.eu;
                    }
                    return;
                }
                if (this.et != 1 || this.eR >= this.eT - 4) break;
                ++this.eR;
                return;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.et == 2) {
                    if (this.ew == 0) {
                        this.ew = 1;
                        return;
                    }
                    this.ew = 0;
                    return;
                }
                if (this.et != 1) break;
                if (this.ev == 0) {
                    this.ev = 1;
                    return;
                }
                this.ev = 0;
                return;
            }
            case -7: {
                if (this.et == 0) {
                    this.bu = 13;
                    this.er = 0;
                    return;
                }
                if (this.et != 1) break;
                this.et = 0;
                this.bT = 0;
                return;
            }
            case 35: {
                if (this.et != 0) break;
                if (this.eJ[this.eu][1] == 0) {
                    this.bN = 1;
                    this.bO = 20;
                    return;
                }
                if (this.eJ[this.eu][0] == 0) {
                    this.bN = 1;
                    this.bO = 21;
                    return;
                }
                if (this.eJ[this.eu][0] > 10) {
                    this.bN = 1;
                    this.bO = 32;
                    return;
                }
                this.ew = 0;
                this.et = 2;
                this.bT = 0;
            }
        }
    }

    public final void ae(int n2) {
        boolean bl = false;
        System.out.println("m_ST_SubMode:" + this.ey);
        switch (n2) {
            case -5: 
            case 53: {
                if (this.G) {
                    if (this.H == 0) {
                        this.ap = 6;
                    }
                    this.H = 0;
                    this.G = false;
                    return;
                }
                if (this.ey == 0) {
                    if (this.eu == 2 || this.eu == 3) {
                        this.ey = 1;
                        this.ev = 0;
                        if (this.eu == 2) {
                            this.g(5, 3);
                        }
                    } else if (this.eu == 0) {
                        this.ap = 2;
                    } else if (this.eu == 1) {
                        if (this.fm == 0) {
                            this.ey = 1;
                            this.B = 1;
                            this.ax();
                        } else {
                            this.bN = 1;
                            this.bO = 12;
                        }
                    } else if (this.eu == 4) {
                        this.G = true;
                        this.H = 0;
                    } else if (this.eu == 5) {
                        this.a.notifyDestroyed();
                    }
                    this.bT = 0;
                    break;
                }
                if (this.ey == 1 && (n2 == -5 || n2 == 53)) {
                    if (this.eu == 2) {
                        this.ey = 0;
                    }
                    this.bT = 0;
                    break;
                }
                if (this.ey != 2 || n2 != -5 && n2 != 53) break;
                this.ey = 0;
                this.bT = 0;
                break;
            }
            case -1: 
            case 50: {
                if (this.G) {
                    return;
                }
                if (this.ey == 0) {
                    if (this.eu > 0) {
                        --this.eu;
                        break;
                    }
                    this.eu = 4;
                    break;
                }
                if (this.ey != 1) break;
                if (this.eu == 3) {
                    if (this.ev > 0) {
                        --this.ev;
                        break;
                    }
                    this.ev = 1;
                    break;
                }
                if (this.eu != 2 || this.eR <= 0) break;
                --this.eR;
                break;
            }
            case -2: 
            case 56: {
                if (this.G) {
                    return;
                }
                if (this.ey == 0) {
                    if (this.eu < 4) {
                        ++this.eu;
                        break;
                    }
                    this.eu = 0;
                    break;
                }
                if (this.ey != 1) break;
                if (this.eu == 3) {
                    if (this.ev < 1) {
                        ++this.ev;
                        break;
                    }
                    this.ev = 0;
                    break;
                }
                if (this.eu != 2 || this.eR + 3 >= this.eT - 1) break;
                ++this.eR;
                break;
            }
            case -3: 
            case 52: {
                if (this.G) {
                    this.H ^= 1;
                    return;
                }
                if (this.ey != 1 || this.eu != 3) break;
                if (this.ev == 0) {
                    if ((g ^= 1) == 0) {
                        this.d.b();
                        break;
                    }
                    this.d.a();
                    break;
                }
                if (this.ev == 1) {
                    if (this.bJ > 0) {
                        --this.bJ;
                        break;
                    }
                    this.bJ = 9;
                    break;
                }
                bl = true;
                break;
            }
            case -4: 
            case 54: {
                if (this.G) {
                    this.H ^= 1;
                    return;
                }
                if (this.ey != 1 || this.eu != 3) break;
                if (this.ev == 0) {
                    if ((g ^= 1) == 0) {
                        this.d.b();
                        break;
                    }
                    this.d.a();
                    break;
                }
                if (this.ev == 1) {
                    if (this.bJ < 9) {
                        ++this.bJ;
                        break;
                    }
                    this.bJ = 0;
                    break;
                }
                bl = true;
                break;
            }
            case -7: {
                if (this.G) {
                    this.H = 0;
                    this.G = false;
                    return;
                }
                if (this.ey == 0) {
                    if (this.eA == 0) {
                        this.bu = 13;
                        this.er = 0;
                    } else {
                        this.ap = 2;
                    }
                } else if (this.ey == 1) {
                    this.ey = 0;
                    if (this.eu == 3) {
                        this.aV();
                    }
                }
                this.bT = 0;
            }
        }
        if (bl) {
            switch (this.ev) {
                case 2: {
                    if (this.bG == 0) {
                        this.bG = 1;
                        break;
                    }
                    this.bG = 0;
                    break;
                }
                case 3: {
                    this.bS = this.bS == 0 ? 1 : 0;
                }
            }
        }
        this.bK = (9 - this.bJ) * 30;
    }

    public final void af(int n2) {
        boolean bl = false;
        int n3 = 0;
        boolean bl2 = false;
        int n4 = 0;
        boolean bl3 = false;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        n7 = this.eA * 5 + this.ew;
        switch (n2) {
            case -5: 
            case 53: {
                if (this.bu == 20) {
                    if (this.et == 0) {
                        if (this.cv[n7][0] > 0 && this.fq > this.ce[0]) {
                            this.bN = 1;
                            this.bO = 0;
                        } else {
                            if (this.cv[n7][0] > 0 && this.fq > this.ce[0]) {
                                this.bN = 1;
                                this.bO = 0;
                            }
                            if (this.cl[this.eu][0] > 0) {
                                this.j(this.cl[this.eu][0], this.cl[this.eu][1]);
                            }
                            if (this.cv[n7][0] > 0) {
                                this.cl[this.eu][0] = this.cv[n7][0];
                                this.cl[this.eu][1] = this.cv[n7][1];
                            } else {
                                this.cl[this.eu][0] = 0;
                                this.cl[this.eu][1] = 0;
                            }
                            if (this.cl[this.eu][0] > 0) {
                                this.k(this.cl[this.eu][0], this.cl[this.eu][1]);
                            }
                            if (this.eu == 1) {
                                n4 = 0;
                                while (n4 < 4) {
                                    this.O[n4] = null;
                                    ++n4;
                                }
                                this.a("ch", 312);
                                this.b("ch", 14675);
                                this.u();
                                this.p();
                            }
                        }
                    } else if (this.et == 1) {
                        this.cj[this.ev][0] = this.cv[n7][0];
                        if (this.cv[n7][0] == 1) {
                            this.cj[this.ev][1] = this.cv[n7][1];
                        } else if (this.cv[n7][0] == 7) {
                            this.cj[this.ev][2] = 0;
                            this.ck[this.ev][0] = this.ei;
                            this.cj[this.ev][1] = n6 = this.cv[n7][1];
                            this.ck[this.ev][1] = this.s(n6);
                        }
                    } else if (this.et == 2) {
                        if (this.cv[n7][0] == 1) {
                            this.f(1, this.cv[n7][1]);
                            this.cw = this.q(5);
                            this.bT = 0;
                        }
                        if (this.gF == 0) {
                            bl = true;
                        } else {
                            this.fP = 1;
                            this.fQ = 0;
                            this.ap = 10;
                        }
                    }
                    if (this.bN == 0 && !bl) {
                        this.bu = 14 + this.es;
                        this.bT = 0;
                    }
                } else if (this.bu == 35) {
                    if (this.cv[n7][0] > 0) {
                        if (this.fC == 0) {
                            if (this.fz == 0 && this.cq[this.cv[n7][1]][4] == 10) {
                                this.bN = 1;
                                this.bO = 13;
                            } else {
                                this.fJ[0][0] = this.cv[n7][0];
                                this.fJ[0][1] = this.cq[this.cv[n7][1]][0];
                                this.fJ[0][2] = this.cv[n7][1];
                                this.fJ[0][3] = this.cq[this.cv[n7][1]][7];
                                this.fK[0] = this.fo;
                                if (this.fz == 0) {
                                    this.fv = 200;
                                    this.fE = this.cq[this.cv[n7][1]][4] + 1;
                                    this.ft = 110 - this.fE * 10;
                                    if (this.fE < 9 && this.ft < 50) {
                                        this.ft = 50;
                                    } else if (this.fE > 8) {
                                        this.ft = 30;
                                    }
                                    this.fJ[1][0] = 6;
                                    this.fJ[1][2] = this.fE;
                                    this.fJ[1][1] = 70;
                                    this.fJ[1][3] = 4;
                                    this.fK[1] = "\u6301\u6709" + this.fE + "\u4e2a";
                                    n3 = this.h(6, this.fJ[1][1]);
                                    this.fx[0] = n3 > -1 ? this.cq[n3][4] : 0;
                                    this.fC = 2;
                                    this.eu = 0;
                                } else {
                                    this.fC = 1;
                                }
                            }
                        } else if (this.fC == 1) {
                            this.fJ[1][0] = this.cv[n7][0];
                            this.fJ[1][2] = this.cv[n7][1];
                            this.fJ[1][3] = 11;
                            this.fK[1] = this.fo;
                            n6 = this.cA[this.fJ[1][0] - 11][this.cv[n7][1]][0] - 1;
                            this.c(this.fJ[1][0], n6, 1);
                            if (this.fz == 2) {
                                this.fJ[0][0] = 1;
                            }
                            this.fC = 2;
                            this.eu = 0;
                        }
                        this.fL = this.fJ[0][0] > 0 && this.fJ[1][0] > 0 ? 1 : 0;
                        if (this.bN == 0) {
                            this.bT = 0;
                            this.bu = 33;
                        }
                    } else {
                        this.bN = 1;
                        this.bO = 11;
                    }
                }
                if (this.bN != 1) break;
            }
            case -1: 
            case 50: {
                if (this.ew - 5 >= 0) {
                    this.ew -= 5;
                } else if (this.eA > 0) {
                    --this.eA;
                    this.bT = 0;
                }
                bl = true;
                break;
            }
            case -2: 
            case 56: {
                if (this.ew + 5 < 15) {
                    this.ew += 5;
                } else if (n7 + 5 <= this.cw - 1) {
                    ++this.eA;
                    this.bT = 0;
                }
                bl = true;
                break;
            }
            case -3: 
            case 52: {
                if (this.ew > 0) {
                    --this.ew;
                } else if (this.eA > 0) {
                    this.ew += 4;
                    --this.eA;
                    this.bT = 0;
                }
                bl = true;
                break;
            }
            case -4: 
            case 54: {
                if (this.ew < 14) {
                    ++this.ew;
                } else if (n7 < this.cw - 1) {
                    ++this.eA;
                    this.ew -= 4;
                    this.bT = 0;
                }
                bl = true;
                break;
            }
            case 49: {
                if (this.et != 2) break;
                this.eB = this.eB > 0 ? --this.eB : 2;
                this.ew = 0;
                this.eA = 0;
                this.bT = 0;
                if (this.eB == 0) {
                    this.cw = this.q(5);
                } else if (this.eB == 1 || this.eB == 2) {
                    this.cw = this.q(this.eB + 10);
                }
                bl = true;
                break;
            }
            case 51: {
                if (this.et != 2) break;
                this.eB = this.eB < 2 ? ++this.eB : 0;
                this.ew = 0;
                this.eA = 0;
                this.bT = 0;
                if (this.eB == 0) {
                    this.cw = this.q(5);
                } else if (this.eB == 1 || this.eB == 2) {
                    this.cw = this.q(this.eB + 10);
                }
                bl = true;
                break;
            }
            case -7: {
                if (this.bu == 20) {
                    if (this.et == 2) {
                        this.bu = 13;
                        this.er = 0;
                        this.et = 0;
                        bl = true;
                    } else {
                        this.bu = 14 + this.es;
                    }
                } else if (this.bu == 35) {
                    this.bu = 33;
                    this.fL = this.fJ[0][0] > 0 && this.fJ[1][0] > 0 ? 1 : 0;
                }
                this.bT = 0;
                break;
            }
            case 42: {
                if (this.cv[this.eA * 5 + this.ew][0] <= 0) break;
                this.bw = this.bu;
                this.bu = this.bu == 20 ? 21 : 29;
                this.bT = 0;
                this.eR = 0;
                switch (this.cv[this.eA * 5 + this.ew][0]) {
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: {
                        this.ez = 0;
                        break;
                    }
                    default: {
                        this.ez = 1;
                    }
                }
                bl = true;
                System.out.println("m_ST_SubDataMode:" + this.ez);
                break;
            }
            case 35: {
                n5 = this.cv[n7][0];
                if (this.bu == 20) {
                    switch (n5) {
                        case 7: 
                        case 8: 
                        case 9: 
                        case 10: {
                            this.bN = 1;
                            this.bO = 15;
                            break;
                        }
                        default: {
                            if (n5 <= 0 || this.et != 2) break;
                            this.bw = this.bu;
                            this.bu = 37;
                            this.bT = 0;
                            this.fE = 1;
                            if (n5 > 10) {
                                this.fE = this.cA[n5 - 11][this.cv[n7][1]][1];
                            } else if (n5 < 2 || n5 > 5) {
                                this.fE = this.cq[this.cv[n7][1]][4];
                            }
                            this.fF = 1;
                            this.fH = 0;
                            this.fG = 0;
                        }
                    }
                }
                bl = true;
            }
        }
        n7 = this.eA * 5 + this.ew;
        System.out.println("ANum:" + n7);
        if (bl) {
            this.l(this.cv[n7][0], this.cv[n7][1]);
        }
    }

    public final void ag(int n2) {
        boolean bl = false;
        int n3 = 0;
        boolean bl2 = false;
        int n4 = 0;
        int n5 = 0;
        if (this.eS == 1) {
            switch (n2) {
                case -5: 
                case 53: {
                    if (this.eR < this.eT - 1) {
                        if (this.eP[this.eQ].e > 2) {
                            ++this.F;
                            if (this.F * 2 < this.eP[this.eQ].e) break;
                            this.F = 0;
                        }
                        ++this.eR;
                        ++this.eQ;
                        byte[] byArray = new byte[2];
                        byte[] byArray2 = byArray;
                        byArray[0] = this.eP[this.eQ].a;
                        byArray2[1] = this.eP[this.eQ].b;
                        this.fc = this.r(byArray2[1]);
                        if (this.fj != 0) break;
                        this.ac = null;
                        this.a("em", 48);
                        this.b("em", 7468);
                        if (this.ab == null) {
                            this.ab = this.c(0, 0);
                        }
                        if (byArray2[0] > 0) {
                            this.ac = this.c(byArray2[0], 0);
                        }
                        this.p();
                        break;
                    }
                    if (this.eP[this.eQ].e > 2) {
                        ++this.F;
                        if (this.F * 2 < this.eP[this.eQ].e) break;
                        this.F = 0;
                    }
                    if (this.eU == 1 && this.eV == 0 && this.eR == this.eT - 1) break;
                    bl = true;
                    break;
                }
                case 35: {
                    if (this.fj != 0) break;
                    this.eQ += this.eT - 1 - this.eR;
                    this.eR = this.eT - 1;
                    bl = true;
                    break;
                }
                case 49: {
                    if (this.eU != 1 || this.eV != 0 || this.eR != this.eT - 1) break;
                    this.eV = 1;
                    this.eT += (this.eN[1] - this.eN[0]) / 2;
                    ++this.eR;
                    ++this.eQ;
                    while (n3 < 3 && !bl2) {
                        if (this.eJ[n3 + 1][1] == 0) {
                            int n6 = 0;
                            while (n6 < 22) {
                                this.eJ[n3 + 1][n6] = this.eM[n6];
                                ++n6;
                            }
                            this.eJ[n3 + 1][22] = 0;
                            this.eJ[n3 + 1][23] = this.bV;
                            this.eJ[n3 + 1][24] = this.fd;
                            this.eJ[n3 + 1][25] = this.aX[this.fd].t;
                            this.fh[50 + n3 + 1] = this.aX[this.fd].u > 0 ? this.aX[this.fd].u : 0;
                            if (this.aX[this.fd].p == 1) {
                                int[] nArray = this.eJ[n3 + 1];
                                nArray[0] = nArray[0] + 10;
                            }
                            bl2 = true;
                            continue;
                        }
                        ++n3;
                    }
                    this.aX[this.fd].k = 2;
                    this.aX[this.fd].m = 2;
                    if (this.eJ[n3 + 1][2] == 4 && this.eJ[n3 + 1][3] == this.bV) {
                        n4 = this.r(this.eJ[n3 + 1][4]);
                        this.aX[n4].k = 3;
                        this.aX[n4].m = 2;
                    }
                    this.at();
                    this.au();
                    break;
                }
                case 50: {
                    if (this.eU != 1 || this.eV != 0 || this.eR != this.eT - 1) break;
                    this.eV = 1;
                    this.eT += (this.eN[2] - this.eN[0]) / 2;
                    this.eQ = this.eR = this.eN[1] / 2;
                    if (this.aX[this.fd].p != 0) break;
                    int n7 = this.aX[this.fd].t;
                    this.eI[n7] = this.eI[n7] + 1;
                    this.aX[this.fd].k = 0;
                    this.aX[this.fd].m = 0;
                }
            }
            if (bl) {
                if (this.eU == 0) {
                    if (this.fj == 0) {
                        ++this.eQ;
                        ++this.eE;
                        this.bD = 0;
                    } else {
                        System.out.println("\u5bf9\u8bdd\u6846\u63a7\u5236:" + this.fd + "," + this.aX[this.fd].o);
                        if (this.aX[this.fd].o == 7) {
                            this.aX[this.fd].o = 0;
                        }
                        if (this.aX[this.fd].o == 0) {
                            this.bu = 23;
                            this.bI = 0;
                        } else {
                            n5 = this.aX[this.fd].o == 5 ? 5 : this.aX[this.fd].o;
                        }
                    }
                } else {
                    this.bu = 23;
                    this.bI = 0;
                    System.out.println("\u5bf9\u8bdd\u6846\u7684\u63a7\u52362");
                    this.at();
                    this.au();
                    if (this.eV == 3) {
                        this.bM = 3;
                        this.bI = 1;
                        this.bT = 0;
                    } else if (this.eV == 2) {
                        if (this.aX[this.fd].o == 5) {
                            n5 = 5;
                        } else if (this.aX[this.fd].o > 0) {
                            n5 = this.aX[this.fd].o;
                        }
                    }
                }
                if (n5 > 0) {
                    if (n5 == 5) {
                        this.bu = 32;
                        this.fy = this.aX[this.fd].o;
                        this.bI = 1;
                        this.bT = 0;
                        this.fD = this.aX[this.fd].r;
                    } else {
                        this.L();
                        System.out.println("\u8fdb\u5165\u5546\u5e97");
                        this.bu = 26;
                        this.fy = this.aX[this.fd].o;
                        this.bI = 1;
                        this.bT = 0;
                        this.fD = this.aX[this.fd].r;
                        this.e(1);
                    }
                }
                this.ab = null;
                this.ac = null;
                this.eU = 0;
                this.eV = 0;
                this.eW = 0;
                this.eu = 0;
                this.fC = 0;
                this.fz = 0;
                this.fj = 0;
                this.eR = 0;
                this.eS = 0;
                this.ff = 0;
                return;
            }
        } else if (this.gs == 1) {
            switch (n2) {
                case -5: 
                case 53: {
                    this.gs = 0;
                    ++this.eE;
                    return;
                }
                case -4: 
                case -3: 
                case 52: 
                case 54: {
                    if (this.bZ == 0) {
                        this.bZ = 1;
                        return;
                    }
                    this.bZ = 0;
                }
            }
        }
    }

    public final void ah(int n2) {
        switch (n2) {
            case -7: {
                this.bu = this.bw;
                this.bT = 0;
                return;
            }
            case -1: 
            case 50: {
                if (this.ez != 1 || this.eR <= 0) break;
                --this.eR;
                return;
            }
            case -2: 
            case 56: {
                if (this.ez != 1 || this.eR + 4 >= this.eT) break;
                ++this.eR;
                return;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                System.out.println("m_It_Info[1]:" + this.fn[1]);
                if (this.fn[1] <= 1 || this.fn[1] >= 6) break;
                this.ez ^= 1;
            }
        }
    }

    public final void ai(int n2) {
        boolean bl = false;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        boolean bl2 = false;
        switch (n2) {
            case -5: 
            case 53: {
                this.bT = 0;
                if (this.fy < 5) {
                    switch (this.fz) {
                        case 0: {
                            this.c(this.fy);
                            this.ew = 0;
                            this.eA = 0;
                            this.fC = 0;
                            this.bu = 27;
                            break;
                        }
                        case 1: {
                            this.cw = this.q(5);
                            this.ew = 0;
                            this.eA = 0;
                            this.fC = 1;
                            this.eB = 0;
                            this.bu = 28;
                            break;
                        }
                        case 2: {
                            bl = true;
                        }
                    }
                    if (this.bu == 27) {
                        n5 = this.fA[this.eA * 5 + this.ew * 5 + 0];
                        n6 = this.fA[this.eA * 5 + this.ew * 5 + 1];
                        this.c(n5, n6, 0);
                        break;
                    }
                    if (this.bu != 28) break;
                    n5 = this.cv[this.eA * 5 + this.ew][0];
                    n6 = this.cq[this.cv[this.eA * 5 + this.ew][1]][0];
                    if (n5 > 0) {
                        this.c(n5, n6, 1);
                        break;
                    }
                    this.fn[1] = 0;
                    break;
                }
                if (this.fy != 6) break;
                if (this.fz == 3) {
                    bl = true;
                    break;
                }
                this.bu = 33;
                this.fC = this.fz == 2 ? 1 : 0;
                this.eu = 0;
                this.fL = 0;
                n3 = 0;
                while (n3 < 2) {
                    n4 = 0;
                    while (n4 < 4) {
                        this.fJ[n3][n4] = 0;
                        ++n4;
                    }
                    ++n3;
                }
                this.fK[0] = "";
                this.fK[1] = "";
                break;
            }
            case -1: 
            case 50: {
                if (this.fy < 5) {
                    if (this.fz > 0) {
                        --this.fz;
                        break;
                    }
                    this.fz = 2;
                    break;
                }
                if (this.fy != 6) break;
                if (this.fz > 0) {
                    --this.fz;
                    break;
                }
                this.fz = 3;
                break;
            }
            case -2: 
            case 56: {
                if (this.fy < 5) {
                    if (this.fz < 2) {
                        ++this.fz;
                        break;
                    }
                    this.fz = 0;
                    break;
                }
                if (this.fy != 6) break;
                if (this.fz < 3) {
                    ++this.fz;
                    break;
                }
                this.fz = 0;
                break;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.fy != 5) break;
                if (this.fz == 0) {
                    this.fz = 1;
                    break;
                }
                this.fz = 0;
                break;
            }
            case -7: {
                bl = true;
            }
        }
        if (bl) {
            this.L();
            this.bu = 23;
            this.fj = 0;
            this.bT = 0;
            this.bI = 0;
            this.e(0);
        }
    }

    /*
     * Unable to fully structure code
     */
    public final void aj(int var1_1) {
        var2_2 = false;
        var3_3 = 0;
        var6_4 = 0;
        var7_5 = 0;
        var8_6 = 0;
        var9_7 = 0;
        switch (var1_1) {
            case -5: 
            case 53: {
                if (this.fn[1] == 0) ** GOTO lbl63
                if (this.fC != 0) ** GOTO lbl26
                if (this.fn[1] > 1 && this.fn[1] < 6 || this.fn[1] == 6 && this.fn[0] > 71) {
                    this.fE = 1;
                    this.fG = 1;
                } else {
                    this.fE = this.fn[1] > 10 ? ((var9_7 = this.i(this.fn[1], this.fn[0] + 1)) != -1 ? 99 - this.cA[this.fn[1] - 11][var9_7][1] : 99) : ((var9_7 = this.h(this.fn[1], this.fn[0])) != -1 ? 99 - this.cq[var9_7][4] : 99);
                    this.fG = 0;
                }
                if (this.ce[11] >= this.fp) {
                    this.bu = 30;
                    this.fF = 1;
                    this.fI = this.fp;
                    this.fH = 0;
                } else {
                    this.bN = 1;
                    this.bO = 9;
                }
                ** GOTO lbl66
lbl26:
                // 1 sources

                if (this.fC != 1) ** GOTO lbl56
                var5_8 = 0;
                if (this.eB != 0) ** GOTO lbl35
                while (var5_8 < 3) {
                    if (this.cl[var5_8][0] <= 0 || this.eN[var5_8] != this.eA * 5 + this.ew) {
                        ++var5_8;
                        continue;
                    }
                    ** GOTO lbl36
                }
                ** GOTO lbl36
lbl35:
                // 1 sources

                var5_8 = 3;
lbl36:
                // 3 sources

                if (var5_8 == 3) {
                    if (this.fn[1] > 10) {
                        this.fE = this.cA[this.fn[1] - 11][this.cv[this.eA * 5 + this.ew][1]][1];
                        this.fG = 0;
                    } else if (this.fn[1] > 1 && this.fn[1] < 6) {
                        this.fE = 1;
                        this.fG = 1;
                    } else {
                        var9_7 = this.cv[this.eA * 5 + this.ew][1];
                        this.fE = this.cq[var9_7][4];
                        this.fG = 0;
                    }
                    this.bu = 30;
                    this.fF = 1;
                    this.fI = this.fp / 2;
                    this.fH = 0;
                } else {
                    this.bN = 1;
                    this.bO = 34;
                }
                ** GOTO lbl66
lbl56:
                // 1 sources

                if (this.fC == 2) {
                    this.fG = 1;
                    this.fF = this.fE = this.fn[4];
                    this.bu = 30;
                    this.fH = 0;
                    this.fI = this.fp;
                }
                ** GOTO lbl66
lbl63:
                // 1 sources

                this.bN = 1;
                this.bO = 11;
                this.fE = 0;
lbl66:
                // 6 sources

                if (this.bN != 1) break;
            }
            case -1: 
            case 50: {
                if (this.ew - 5 >= 0) {
                    this.ew -= 5;
                } else if (this.eA > 0) {
                    --this.eA;
                    this.bT = 0;
                }
                var2_2 = true;
                break;
            }
            case -2: 
            case 56: {
                if (this.bu == 27) {
                    var6_4 = 10;
                    var3_3 = this.fB;
                } else {
                    var6_4 = 15;
                    var3_3 = this.cr;
                }
                if (this.ew + 5 < var6_4) {
                    this.ew += 5;
                } else if (this.eA * 5 + this.ew + 5 < var3_3) {
                    ++this.eA;
                    this.bT = 0;
                }
                var2_2 = true;
                break;
            }
            case -3: 
            case 52: {
                if (this.ew > 0) {
                    --this.ew;
                } else if (this.eA > 0) {
                    this.ew += 4;
                    --this.eA;
                    this.bT = 0;
                }
                var2_2 = true;
                break;
            }
            case -4: 
            case 54: {
                if (this.bu == 27) {
                    var6_4 = 9;
                    var3_3 = this.fB - 1;
                } else {
                    var6_4 = 14;
                    var3_3 = this.cr - 1;
                }
                if (this.ew < var6_4) {
                    ++this.ew;
                } else if (this.eA * 5 + this.ew < var3_3) {
                    ++this.eA;
                    this.ew -= 4;
                    this.bT = 0;
                }
                var2_2 = true;
                break;
            }
            case 49: {
                if (this.fC != 1) break;
                this.eB = this.eB > 0 ? --this.eB : 2;
                this.ew = 0;
                this.eA = 0;
                this.bT = 0;
                if (this.eB == 0) {
                    this.cw = this.q(5);
                } else if (this.eB == 1 || this.eB == 2) {
                    this.cw = this.q(this.eB + 10);
                }
                var2_2 = true;
                break;
            }
            case 51: {
                if (this.fC != 1) break;
                this.eB = this.eB < 2 ? ++this.eB : 0;
                this.ew = 0;
                this.eA = 0;
                this.bT = 0;
                if (this.eB == 0) {
                    this.cw = this.q(5);
                } else if (this.eB == 1 || this.eB == 2) {
                    this.cw = this.q(this.eB + 10);
                }
                var2_2 = true;
                break;
            }
            case -7: {
                if (this.fC == 0) {
                    this.fA = null;
                }
                this.bu = 26;
                this.bT = 0;
                break;
            }
            case 42: {
                if (this.fn[1] <= 0) break;
                this.bw = this.bu;
                this.bu = 29;
                this.bT = 0;
                this.eR = 0;
                this.ez = this.fn[1] > 1 && this.fn[1] < 6 ? 0 : 1;
            }
        }
        if (var2_2) {
            if (this.bu == 27) {
                var7_5 = this.fA[this.eA * 25 + this.ew * 5 + 0];
                var8_6 = this.fA[this.eA * 25 + this.ew * 5 + 1];
                System.out.println("ItemKind:" + var7_5 + ",ItemNum" + var8_6);
                if (var7_5 > 0) {
                    this.c(var7_5, var8_6, 0);
                    return;
                }
                this.fn[1] = 0;
                return;
            }
            var10_9 = this.eA * 5 + this.ew;
            this.l(this.cv[var10_9][0], this.cv[var10_9][1]);
        }
    }

    public final void ak(int n2) {
        int n3;
        int[] nArray = new int[12];
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        boolean bl = false;
        switch (n2) {
            case -5: 
            case 53: {
                if (this.fG == 0) {
                    this.fG = 1;
                    this.fH = 0;
                    break;
                }
                if (this.fG != 1) break;
                if (this.fH == 0) {
                    if (this.fC == 0 || this.fC == 2) {
                        if (this.fn[1] > 10) {
                            n4 = this.f(this.fn[1], this.fn[0] + 1, this.fF);
                        } else if (this.fn[1] != 6 || this.fn[0] <= 71) {
                            int n8 = 0;
                            while (n8 < 12) {
                                nArray[n8] = this.fn[n8];
                                ++n8;
                            }
                            nArray[4] = this.fn[1] > 1 && this.fn[1] < 6 ? 0 : this.fF;
                            n4 = this.c(nArray[1], nArray[0], nArray[4], nArray[5]);
                        } else if (this.fn[1] == 6 && this.fn[0] == 72) {
                            if (this.cr > 40) {
                                n4 = -4;
                            }
                        } else if (this.fn[1] == 6 && this.fn[0] == 73 && this.cr == 50) {
                            n4 = -4;
                        }
                        if (n4 < 0) {
                            this.bN = 1;
                        }
                        if (n4 == -1) {
                            this.bO = 1;
                        } else if (n4 == -2) {
                            this.bO = 2;
                        } else if (n4 == -3) {
                            this.bO = 10;
                        } else if (n4 == -4) {
                            this.bO = 37;
                        } else if (n4 > -1 && this.fC == 0) {
                            if (this.fn[1] > 10) {
                                this.g(this.fn[1], this.fn[0] + 1, this.fF);
                            } else if (this.fn[1] == 6 && this.fn[0] == 72) {
                                this.cr = 40;
                            } else if (this.fn[1] == 6 && this.fn[0] == 73) {
                                this.cr = 50;
                            } else {
                                this.d(nArray);
                            }
                            this.ce[11] = this.ce[11] - this.fI;
                            n5 = 1;
                        }
                    } else if (this.fC == 1) {
                        if (this.cv[this.eA * 5 + this.ew][0] > 0) {
                            if (this.cv[this.eA * 5 + this.ew][0] < 11) {
                                this.b(this.cv[this.eA * 5 + this.ew][1], 0, this.fF, 0);
                                this.ce[11] = this.ce[11] + this.fI;
                                int n9 = 0;
                                while (n9 < 5) {
                                    if (this.cj[n9][0] < 7 && this.cj[n9][1] == this.cv[this.eA * 5 + this.ew][1] && ((this.cj[n9][0] == 1 || this.cj[n9][0] == 6) && this.cq[this.cj[n9][1]][4] == 0 || this.cj[n9][0] > 1 && this.cj[n9][0] < 6)) {
                                        this.cj[n9][0] = 0;
                                        this.cj[n9][1] = 0;
                                        this.cj[n9][2] = 0;
                                    }
                                    ++n9;
                                }
                            } else {
                                n6 = this.cv[this.eA * 5 + this.ew][0];
                                n7 = this.cA[n6 - 11][this.cv[this.eA * 5 + this.ew][1]][0];
                                this.h(n6, n7, this.fF);
                                this.ce[11] = this.ce[11] + this.fI;
                            }
                        }
                        n5 = 2;
                    }
                    if (n5 <= 0) break;
                    this.bu = 40;
                    if (this.fn[1] == 6 && this.fn[0] > 71) {
                        n5 = 5;
                    }
                    this.g(n5 + 14, 3);
                    n5 = 0;
                    break;
                }
                if (this.fH != 1) break;
                n5 = 1;
                break;
            }
            case -1: 
            case 50: {
                if (this.fG != 0) break;
                if (this.fF + 10 <= this.fE) {
                    if ((this.fF + 10) * this.fp <= this.ce[11] || this.fC == 1) {
                        this.fF += 10;
                        if (this.fC == 0) {
                            this.fI = this.fF * this.fp;
                            break;
                        }
                        if (this.fC != 1) break;
                        this.fI = this.fF * (this.fp / 2);
                        break;
                    }
                    this.bN = 1;
                    this.bO = 9;
                    break;
                }
                if (this.fF < this.fE) {
                    if (this.fE * this.fp <= this.ce[11] || this.fC == 1) {
                        if (this.fC == 0) {
                            this.fI = this.fE * this.fp;
                        } else if (this.fC == 1) {
                            this.fI = this.fE * (this.fp / 2);
                        }
                        this.fF = this.fE;
                        break;
                    }
                    this.bN = 1;
                    this.bO = 9;
                    break;
                }
                this.bN = 1;
                if (this.fC == 0) {
                    this.bO = 10;
                    break;
                }
                this.bO = 22;
                break;
            }
            case -2: 
            case 56: {
                if (this.fG != 0) break;
                if (this.fF - 10 > 1) {
                    this.fF -= 10;
                } else if (this.fF > 1) {
                    this.fF = 1;
                } else {
                    this.bN = 1;
                    this.bO = 19;
                }
                if (this.fC == 0) {
                    this.fI = this.fF * this.fp;
                    break;
                }
                if (this.fC != 1) break;
                this.fI = this.fF * (this.fp / 2);
                break;
            }
            case -3: 
            case 52: {
                if (this.fG == 0) {
                    if (this.fF > 1) {
                        --this.fF;
                        if (this.fC == 0) {
                            this.fI = this.fF * this.fp;
                            break;
                        }
                        if (this.fC != 1) break;
                        this.fI = this.fF * (this.fp / 2);
                        break;
                    }
                    this.bN = 1;
                    this.bO = 19;
                    break;
                }
                if (this.fG != 1) break;
                if (this.fH == 0) {
                    this.fH = 1;
                    break;
                }
                this.fH = 0;
                break;
            }
            case -4: 
            case 54: {
                n3 = this.ce[11] / this.fp;
                System.out.println("m_My_Val[11]:\u91d1\u94b1" + this.ce[11] + "," + n3);
                if (this.fG == 0) {
                    if (this.fF < this.fE) {
                        if (this.fF < n3 || this.fC == 1) {
                            ++this.fF;
                            if (this.fC == 0) {
                                this.fI = this.fF * this.fp;
                                break;
                            }
                            if (this.fC != 1) break;
                            this.fI = this.fF * (this.fp / 2);
                            break;
                        }
                        this.bN = 1;
                        this.bO = 9;
                        break;
                    }
                    this.bN = 1;
                    if (this.fC == 0) {
                        this.bO = 10;
                        break;
                    }
                    this.bO = 22;
                    break;
                }
                if (this.fG != 1) break;
                if (this.fH == 0) {
                    this.fH = 1;
                    break;
                }
                this.fH = 0;
                break;
            }
            case -7: {
                if (this.fG == 0 || this.fG == 1 && this.fn[1] > 1 && this.fn[1] < 6 || this.fC == 2) {
                    n5 = 1;
                    break;
                }
                this.fG = 0;
            }
        }
        if (n5 == 1) {
            this.ew = 0;
            this.eA = 0;
            n3 = 0;
            if (this.fC != 1) {
                if (this.fC == 0) {
                    this.bu = 27;
                }
                this.c(this.fA[0], this.fA[1], 0);
                return;
            }
            this.bu = 28;
            if (this.eB == 0) {
                this.cw = this.q(5);
            } else if (this.eB == 1 || this.eB == 2) {
                this.cw = this.q(this.eB + 10);
            }
            n3 = this.eA * 5 + this.ew;
            this.l(this.cv[n3][0], this.cv[n3][1]);
        }
    }

    public final void al(int n2) {
        boolean bl = false;
        int n3 = 0;
        switch (n2) {
            case -5: 
            case 53: {
                break;
            }
            case -1: 
            case 50: {
                if (this.bu != 31 || this.eR <= 0) break;
                --this.eR;
                break;
            }
            case -2: 
            case 56: {
                if (this.bu != 31 || this.eR >= this.eT - 3) break;
                ++this.eR;
                break;
            }
            case -3: 
            case 52: {
                if (this.bu != 31) break;
                this.eu = this.eu > 0 ? --this.eu : this.fO - 1;
                bl = true;
                break;
            }
            case -4: 
            case 54: {
                if (this.bu != 31) break;
                this.eu = this.eu < this.fO - 1 ? ++this.eu : 0;
                bl = true;
                break;
            }
            case -7: {
                if (this.bu == 25) {
                    this.bu = 23;
                    this.bI = 0;
                    break;
                }
                this.bu = 25;
                break;
            }
            case 35: {
                if (this.bu != 25 || this.bt >= 38) break;
                this.bu = 31;
                this.eu = 0;
                bl = true;
            }
        }
        if (bl) {
            n3 = this.fN[this.eu] - 1;
            System.out.println("\u67e5\u770b\u654c\u4eba\u4fe1\u606f:" + n3);
            this.g(this.aU[n3].Y, 2);
        }
    }

    public final void am(int n2) {
        boolean bl = false;
        switch (n2) {
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.fC != 0) break;
                if (this.eu == 0) {
                    this.eu = 1;
                    break;
                }
                this.eu = 0;
                break;
            }
            case -5: 
            case 53: {
                if (this.fC == 0) {
                    if (this.eu == 0) {
                        if (this.ce[11] >= 100) {
                            this.ap = 5;
                            this.fP = 1;
                            this.fQ = 0;
                            break;
                        }
                        this.bN = 1;
                        this.bO = 9;
                        break;
                    }
                    if (this.eu != 1) break;
                    bl = true;
                    break;
                }
                if (this.fC != 1) break;
                bl = true;
                break;
            }
            case -7: {
                bl = true;
            }
        }
        if (bl) {
            this.bu = 23;
            this.bT = 0;
            this.fj = 0;
            this.bT = 0;
            this.bI = 0;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     */
    public final void an(int var1_1) {
        var2_2 = 0;
        var3_3 = false;
        var4_4 = false;
        var5_5 = false;
        var6_6 = false;
        switch (var1_1) {
            case -5: 
            case 53: {
                if (this.fC != 0) ** GOTO lbl12
                this.cw = this.q(6);
                var3_3 = true;
                ** GOTO lbl41
lbl12:
                // 1 sources

                if (this.fC != 1) ** GOTO lbl17
                if (this.fz != 0) {
                    this.cw = this.q(this.fz + 10);
                    var3_3 = true;
                }
                ** GOTO lbl41
lbl17:
                // 1 sources

                if (this.fC != 2) ** GOTO lbl41
                if (this.eu != 0) ** GOTO lbl38
                if (this.fL != 1) ** GOTO lbl35
                if (this.ap() != 1) ** GOTO lbl32
                if (this.ce[11] >= this.fv) {
                    this.bu = 34;
                    this.bT = 0;
                    this.eY = 0;
                    this.eX = 0;
                    this.aq();
                } else {
                    this.bN = 1;
                    this.bO = 9;
                }
                ** GOTO lbl41
lbl32:
                // 1 sources

                this.bN = 1;
                this.bO = 16;
                ** GOTO lbl41
lbl35:
                // 1 sources

                this.bN = 1;
                this.bO = 17;
                ** GOTO lbl41
lbl38:
                // 1 sources

                if (this.eu == 1) {
                    this.bu = 26;
                    this.bT = 0;
                }
lbl41:
                // 10 sources

                if (this.bN == 1) break;
                break;
            }
            case -7: {
                this.bu = 26;
                this.bT = 0;
                break;
            }
            case -1: 
            case 50: {
                if (this.fz == 2) {
                    var2_2 = 1;
                }
                if (this.fC <= var2_2) break;
                --this.fC;
                break;
            }
            case -2: 
            case 56: {
                if (this.fC >= 2) break;
                ++this.fC;
                break;
            }
            case -4: 
            case -3: 
            case 52: 
            case 54: {
                if (this.fC != 2) break;
                if (this.eu == 0) {
                    this.eu = 1;
                    break;
                }
                this.eu = 0;
                break;
            }
        }
        if (var3_3) {
            this.bu = 35;
            this.ew = 0;
            this.eA = 0;
            this.l(this.cv[0][0], this.cv[0][1]);
        }
    }

    public final void ao(int n2) {
        switch (n2) {
            case -5: 
            case 53: {
                this.bu = 26;
                this.bT = 0;
            }
        }
    }

    public final void ap(int n2) {
        int n3 = 0;
        boolean bl = false;
        int n4 = 0;
        int n5 = 0;
        boolean bl2 = false;
        switch (n2) {
            case -5: 
            case 53: {
                if (this.fG == 0) {
                    this.fG = 1;
                    break;
                }
                if (this.fH == 0) {
                    n5 = this.cv[this.eA * 5 + this.ew][0];
                    if (n5 < 11) {
                        n4 = this.cv[this.eA * 5 + this.ew][1];
                        this.b(n4, 0, this.fF, 0);
                        n3 = 0;
                        while (n3 < 3) {
                            if (this.cl[n3][0] > 0 && this.eN[n3] == this.eA * 5 + this.ew) {
                                this.j(this.cl[n3][0], this.cl[n3][1]);
                                this.cl[n3][0] = 0;
                                this.cl[n3][1] = 0;
                            }
                            ++n3;
                        }
                        this.v(this.bZ);
                        this.w(this.bZ);
                        this.aj();
                        this.cw = this.q(5);
                    } else {
                        n4 = this.cA[n5 - 11][this.cv[this.eA * 5 + this.ew][1]][0];
                        this.h(n5, n4, this.fF);
                        this.cw = this.q(n5);
                    }
                    this.eA = 0;
                    this.ew = 0;
                    this.l(this.cv[0][0], this.cv[0][1]);
                }
                bl = true;
                break;
            }
            case -1: 
            case 50: {
                if (this.fG == 0) {
                    if (this.dC > 1 && this.dC < 6) {
                        this.bN = 1;
                        this.bO = 18;
                        break;
                    }
                    if (this.fF + 10 <= this.fE) {
                        this.fF += 10;
                        break;
                    }
                    if (this.fF < this.fE) {
                        this.fF = this.fE;
                        break;
                    }
                    this.bN = 1;
                    this.bO = 22;
                    break;
                }
                this.fG = 0;
                break;
            }
            case -2: 
            case 56: {
                if (this.fG != 0) break;
                if (this.dC > 1 && this.dC < 6) {
                    this.bN = 1;
                    this.bO = 18;
                    break;
                }
                if (this.fF - 10 >= 1) {
                    this.fF -= 10;
                    break;
                }
                this.bN = 1;
                this.bO = 19;
                break;
            }
            case -3: 
            case 52: {
                if (this.fG == 0) {
                    if (this.dC > 1 && this.dC < 6) {
                        this.bN = 1;
                        this.bO = 18;
                        break;
                    }
                    if (this.fF > 1) {
                        --this.fF;
                        break;
                    }
                    this.bN = 1;
                    this.bO = 19;
                    break;
                }
                if (this.fH == 0) {
                    this.fH = 1;
                    break;
                }
                this.fH = 0;
                break;
            }
            case -4: 
            case 54: {
                if (this.fG == 0) {
                    if (this.dC > 1 && this.dC < 6) {
                        this.bN = 1;
                        this.bO = 18;
                        break;
                    }
                    if (this.fF < this.fE) {
                        ++this.fF;
                        break;
                    }
                    this.bN = 1;
                    this.bO = 22;
                    break;
                }
                if (this.fH == 0) {
                    this.fH = 1;
                    break;
                }
                this.fH = 0;
                break;
            }
            case -7: {
                bl = true;
            }
        }
        if (bl) {
            this.bu = this.bw;
            this.bT = 0;
        }
    }

    public final boolean aU() {
        if (this.c.a()) {
            this.bJ = this.c.a[0];
            this.bG = this.c.a[3];
            this.gm = this.c.a[4];
            this.bS = this.c.a[5];
            this.bL = this.c.a[6];
            return true;
        }
        return false;
    }

    public final void aV() {
        this.c.a[0] = this.bJ;
        this.c.a[2] = 1;
        this.c.a[3] = this.bG;
        this.c.a[4] = this.gm;
        this.c.a[5] = this.bS;
        this.c.a[6] = this.bL;
        this.c.b();
    }

    public final int a(byte[] byArray, int n2) {
        return (byArray[n2 + 3] & 0xFF) << 24 | (byArray[n2 + 2] & 0xFF) << 16 | (byArray[n2 + 1] & 0xFF) << 8 | byArray[n2] & 0xFF;
    }

    public final int b(byte[] byArray, int n2) {
        return (byArray[n2 + 1] & 0xFF) << 8 | byArray[n2] & 0xFF;
    }

    public final void a(byte[] byArray, int n2, int n3) {
        byArray[n2 + 3] = (byte)(n3 >> 24);
        byArray[n2 + 2] = (byte)(n3 >> 16 & 0xFF);
        byArray[n2 + 1] = (byte)(n3 >> 8 & 0xFF);
        byArray[n2 + 0] = (byte)(n3 & 0xFF);
    }

    public final void aW() {
        System.out.println("\u5173\u95ed\u58f0\u97f3");
    }

    public final void r(int n2, int n3) {
        if (this.eC == 0 && this.fk == 0) {
            this.gv = 1;
            this.gu = this.ei;
        }
    }

    public final int s(int n2, int n3) {
        int n4 = n2;
        if (n2 != n3) {
            if (this.f == null) {
                Object var4_4 = null;
                long l2 = 0L;
                Object var7_6 = null;
                l2 = o._n();
                this.f = new Random(l2);
            }
            n4 = (this.f.nextInt() >>> 1) % (n3 - n2) + n2;
        }
        return n4;
    }

    public final void a(Image image, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n2 + n4 >= 0 && n3 <= this.bx[1] && n3 + n5 >= 0 && n2 <= this.bx[0]) {
            int n8 = n2;
            int n9 = n3;
            if (n2 < 0) {
                n4 += n2;
                n8 = 0;
            }
            if (n3 < 0) {
                n5 += n3;
                n9 = 0;
            }
            if (n2 + n4 > this.bx[0]) {
                n4 = this.bx[0] - n2;
            }
            if (n2 + n5 > this.bx[1]) {
                n5 = this.bx[1] - n3;
            }
            this.e.setClip(n8, n9, n4, n5);
            if (image != null) {
                this.e.drawImage(image, n2 - n6, n3 - n7, 20);
            }
            this.e.setClip(0, 0, this.bx[0], this.bx[1]);
            return;
        }
    }

    public final void a(Image image, int n2, int n3, int n4) {
        if (image != null) {
            this.e.drawImage(image, n2, n3, 3);
        }
    }

    public final void f(int n2, int n3, int n4, int n5, int n6) {
        this.e.setColor(n6);
        this.e.fillRect(n2, n3, n4, n5);
    }

    public final void g(int n2, int n3, int n4, int n5, int n6) {
        this.e.setColor(n6);
        this.e.drawRect(n2, n3, n4 - 1, n5 - 1);
    }

    public final void b(String string, int n2, int n3, int n4, int n5) {
        this.e.setColor(n5);
        if (n4 == 0) {
            this.e.drawString(string, n2, n3, 20);
            return;
        }
        this.e.drawString(string, n2, n3, 17);
    }

    public final int t(int n2, int n3) {
        int n4 = 0;
        n4 = n2 - n3 > 0 ? n2 - n3 : n3 - n2;
        return n4;
    }

    private void _z(byte[] byArray) {
        for (int i2 = 0; i2 < 256; ++i2) {
            this._g[i2] = (byte)i2;
        }
        int n2 = 0;
        for (int i3 = 0; i3 < 256; ++i3) {
            n2 = n2 + this._g[i3] + byArray[i3 % byArray.length] & 0xFF;
            byte by = this._g[i3];
            this._g[i3] = this._g[n2];
            this._g[n2] = by;
        }
        this._h = 0;
        this._i = 0;
    }

    private static final int _l(String string) {
        byte[] byArray = string.getBytes();
        int n2 = 147;
        try {
            int n3 = byArray.length;
            while (n3 > 0) {
                n2 = n2 * 147 ^ byArray[--n3];
            }
            return n2;
        }
        catch (Exception exception) {
            return n2;
        }
    }

    public static long _n() {
        if (_s >= 1) {
            return System.currentTimeMillis();
        }
        return 0L;
    }

    public static InputStream _m(Class clazz, String string) {
        if (_s >= 1) {
            if (clazz != null) {
                return clazz.getResourceAsStream(string);
            }
            if (!string.startsWith("/")) {
                string = "/" + string;
            }
            return "".getClass().getResourceAsStream(string);
        }
        return null;
    }

    private String _o(String string) {
        char[] cArray = new char[string.length()];
        string.getChars(0, string.length(), cArray, 0);
        int n2 = 0;
        for (int i2 = 0; i2 < cArray.length; ++i2) {
            cArray[i2] = (char)(cArray[i2] - '\u0001' ^ n2++);
        }
        return new String(cArray);
    }

    private void _y(byte[] byArray, int n2, byte[] byArray2, int n3, int n4) {
        int n5 = this._h;
        int n6 = this._i;
        for (int i2 = 0; i2 < n4; ++i2) {
            n5 = n5 + 1 & 0xFF;
            n6 = n6 + this._g[n5] & 0xFF;
            byte by = this._g[n5];
            this._g[n5] = this._g[n6];
            this._g[n6] = by;
            byArray2[n3 + i2] = (byte)(byArray[n2 + i2] ^ this._g[this._g[n5] + this._g[n6] & 0xFF]);
        }
        this._h = n5;
        this._i = n6;
    }

    private final void _x() {
        this._j = 1732584193;
        this._k = -271733879;
        this._l = -1732584194;
        this._m = 271733878;
        this._n = 0;
        this._o = 0L;
        this._p = 0;
        this._n = -1009589776;
        this._q = new byte[64];
        this._r = new int[80];
    }

    private static void _s(byte[] byArray, int n2, int n3) {
        byArray[n2] = (byte)(n3 >>> 24);
        byArray[n2 + 1] = (byte)(n3 >>> 16);
        byArray[n2 + 2] = (byte)(n3 >>> 8);
        byArray[n2 + 3] = (byte)n3;
    }

    private void _t(byte[] byArray, int n2) {
        int n3;
        int[] nArray = this._r;
        int n4 = 0;
        while (n4 < 16) {
            nArray[n4] = byArray[n2] << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
            ++n4;
            n2 += 4;
        }
        for (int i2 = 16; i2 < 80; ++i2) {
            int n5 = nArray[i2 - 3] ^ nArray[i2 - 8] ^ nArray[i2 - 14] ^ nArray[i2 - 16];
            nArray[i2] = n5 << 1 | n5 >>> 31;
        }
        int n6 = this._j;
        int n7 = this._k;
        int n8 = this._l;
        int n9 = this._m;
        int n10 = this._n;
        for (n3 = 0; n3 < 20; ++n3) {
            int n11 = (n6 << 5 | n6 >>> 27) + (n7 & n8 | ~n7 & n9) + n10 + nArray[n3] + 1518500249;
            n10 = n9;
            n9 = n8;
            n8 = n7 << 30 | n7 >>> 2;
            n7 = n6;
            n6 = n11;
        }
        while (n3 < 40) {
            int n12 = (n6 << 5 | n6 >>> 27) + (n7 ^ n8 ^ n9) + n10 + nArray[n3] + 1859775393;
            n10 = n9;
            n9 = n8;
            n8 = n7 << 30 | n7 >>> 2;
            n7 = n6;
            n6 = n12;
            ++n3;
        }
        while (n3 < 60) {
            int n13 = (n6 << 5 | n6 >>> 27) + (n7 & n8 | n7 & n9 | n8 & n9) + n10 + nArray[n3] + -1894007588;
            n10 = n9;
            n9 = n8;
            n8 = n7 << 30 | n7 >>> 2;
            n7 = n6;
            n6 = n13;
            ++n3;
        }
        while (n3 < 80) {
            int n14 = (n6 << 5 | n6 >>> 27) + (n7 ^ n8 ^ n9) + n10 + nArray[n3] + -899497514;
            n10 = n9;
            n9 = n8;
            n8 = n7 << 30 | n7 >>> 2;
            n7 = n6;
            n6 = n14;
            ++n3;
        }
        this._j += n6;
        this._k += n7;
        this._l += n8;
        this._m += n9;
        this._n += n10;
        this._o += 512L;
    }

    private final void _u(byte[] byArray, int n2) {
        byte[] byArray2 = this._q;
        if (byArray2 != null) {
            int n3 = this._p;
            long l2 = this._o + (long)(n3 << 3);
            byArray2[n3++] = (byte)-128;
            if (n3 > 56) {
                while (n3 < 64) {
                    byArray2[n3++] = (byte)0;
                }
                this._t(byArray2, 0);
                n3 = 0;
            }
            while (n3 < 56) {
                byArray2[n3++] = (byte)0;
            }
            o._s(byArray2, 56, (int)(l2 >> 32));
            o._s(byArray2, 60, (int)l2);
            this._t(byArray2, 0);
            this._q = null;
            this._r = null;
        }
        o._s(byArray, n2, this._j);
        o._s(byArray, n2 + 4, this._k);
        o._s(byArray, n2 + 8, this._l);
        o._s(byArray, n2 + 12, this._m);
        o._s(byArray, n2 + 16, this._n);
    }

    private final byte[] _v() {
        byte[] byArray = new byte[20];
        this._u(byArray, 0);
        return byArray;
    }

    private final void _w(byte[] byArray, int n2, int n3) {
        int n4;
        if (this._p > 0 && n3 >= (n4 = 64 - this._p)) {
            System.arraycopy(byArray, n2, this._q, this._p, n4);
            this._t(this._q, 0);
            this._p = 0;
            n2 += n4;
            n3 -= n4;
        }
        while (n3 >= 64) {
            this._t(byArray, n2);
            n2 += 64;
            n3 -= 64;
        }
        System.arraycopy(byArray, n2, this._q, this._p, n3);
        this._p += n3;
    }

    public static Displayable _b() {
        Displayable displayable = Display.getDisplay((MIDlet)_d).getCurrent() == null ? null : (_s < 1 ? null : Display.getDisplay((MIDlet)_d).getCurrent());
        return displayable;
    }

    private void _f() {
        ++_s;
    }

    private static int _p(byte[] byArray, int n2) {
        return (byArray[n2] & 0xFF) << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
    }

    private byte[] _q(byte[] byArray, byte[] byArray2) {
        int n2 = byArray2.length;
        if (n2 < 36 || byArray2[0] != this._f) {
            throw new IllegalArgumentException();
        }
        this._x();
        this._w(byArray2, 1, 7);
        this._w(byArray, 0, byArray.length);
        this._z(this._v());
        byte[] byArray3 = new byte[n2 - 8];
        this._y(byArray2, 8, byArray3, 0, n2 - 8);
        this._x();
        this._w(byArray2, 0, 8);
        this._w(byArray3, 0, n2 - 28);
        byte[] byArray4 = this._v();
        for (int i2 = 0; i2 < 20; ++i2) {
            if (byArray4[i2] == byArray3[n2 - 28 + i2]) continue;
            throw new IllegalArgumentException();
        }
        int n3 = o._p(byArray3, 0);
        int n4 = o._p(byArray3, 4);
        byte[] byArray5 = new byte[n3];
        if (n4 != -1) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(byArray3, 8, byArray5, 0, n2 - 36);
        return byArray5;
    }

    private byte[] _r() {
        try {
            byte[] byArray = new byte[16];
            System.arraycopy(this._o("8f416aef").getBytes(), 0, byArray, 0, 8);
            System.arraycopy(this._o("624c8855").getBytes(), 0, byArray, 8, 8);
            byte[] byArray2 = this._q(byArray, data);
            return byArray2;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private void _g() {
        try {
            data = this._r();
            int n2 = 0;
            for (int i2 = 0; i2 < _BB.length; ++i2) {
                o._BB[i2] = "" + ((data[n2] & 0xFF) << 24 | (data[n2 + 1] & 0xFF) << 16 | (data[n2 + 2] & 0xFF) << 8 | data[n2 + 3] & 0xFF);
                n2 += 4;
            }
            this._e = Image.createImage((byte[])data, (int)80, (int)(data.length - 80));
            data = null;
            if (this._e == null) {
                _d.notifyDestroyed();
            }
        }
        catch (Exception exception) {
            data = null;
            _d.notifyDestroyed();
        }
    }

    private static final void DrawEffect(Graphics graphics, String string, int n2, int n3) {
        graphics.setColor(0);
        graphics.drawString(string, n2, n3 - 1, 20);
        graphics.drawString(string, n2 - 1, n3, 20);
        graphics.drawString(string, n2 + 1, n3, 20);
        graphics.drawString(string, n2, n3 + 1, 20);
        graphics.setColor(0xFFFFFF);
        graphics.drawString(string, n2, n3, 20);
    }

    public void _i() throws Exception {
        ((ForeverHero)_d)._a_a();
        ((ForeverHero)_d)._b_b();
    }

    private void _h() {
    }

    public o(MIDlet mIDlet, int n2) {
        this._a = 64;
        this._b = 2048L;
        _c = 0L;
        _d = mIDlet;
        this._g();
        this.setFullScreenMode(true);
        Display.getDisplay((MIDlet)_d).setCurrent((Displayable)this);
    }

    public void _e() {
        new Thread(this).start();
    }

    static {
        _DD0 = -779149751;
        _DD1 = 1567455253;
        _DD2 = 1497768413;
        _DD3 = -1919310907;
        _DD4 = -1922864816;
        _DD5 = -1882539868;
        _DD6 = 1567455253;
        _DD7 = 1284855725;
        _DD8 = 3172252;
        _DD9 = 19526;
        _DD10 = 250031035;
        _DD11 = -1345944411;
        _DD12 = 250031035;
        _DD13 = -116529636;
        _DD14 = -126577053;
        _DD15 = 19526;
        _DD16 = 258805394;
        _DD17 = 934281994;
        _DD18 = -126577053;
        _DD19 = -1429256661;
        _BB = new String[20];
        data = new byte[1682];
        _s = 0;
        Object var1 = null;
        byte[] cfr_ignored_0 = new byte[1682];
        try {
            InputStream inputStream = "".getClass().getResourceAsStream("/_bb");
            inputStream.read(data);
            inputStream.close();
        }
        catch (Exception exception) {}
    }

    public static boolean _a0() {
        return (long)_DD18 != (long)o._l(_BB[18]) - _c;
    }

    public static boolean _a1() {
        return _DD13 != (long)o._l(_BB[13]) - _c;
    }

    public static boolean _a2() {
        return _DD19 != (long)o._l(_BB[19]) - _c;
    }
}

