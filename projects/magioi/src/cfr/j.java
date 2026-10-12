/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.nokia.mid.ui.DirectGraphics
 *  com.nokia.mid.ui.DirectUtils
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 *  javax.microedition.midlet.MIDlet
 *  javax.microedition.rms.RecordStore
 */
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

public abstract class j
extends Canvas
implements Runnable {
    public static MIDlet aQ = null;
    public static boolean aR = true;
    public static String aS = "";
    public static DirectGraphics aT;
    public static boolean aU;
    private static int[] a;
    private static int b;
    private static int c;
    private static int d;
    private static int e;
    private static boolean f;
    private static int g;
    private static int h;
    private static int i;
    private static int j;
    private static int k;
    private static int l;
    private static int m;
    public static int aV;
    private static long n;
    private static boolean o;
    private static long p;
    private static long q;
    public static int aW;
    public static int aX;
    private static int r;
    private static int s;
    public static int aY;
    public static byte[][] aZ;

    public j(MIDlet mIDlet) {
        aQ = mIDlet;
    }

    public void showNotify() {
        aU = false;
        j.l();
    }

    public void hideNotify() {
        aU = true;
        j.l();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int b(int n) {
        int n2 = 0;
        boolean bl = true;
        switch (n) {
            case 48: {
                n2 = 16;
                break;
            }
            case 49: {
                n2 = 32;
                break;
            }
            case 50: {
                n2 = 65;
                break;
            }
            case 51: {
                n2 = 128;
                break;
            }
            case 52: {
                n2 = 260;
                break;
            }
            case 53: {
                n2 = 0 | 0x100200;
                break;
            }
            case 54: {
                n2 = 1032;
                break;
            }
            case 55: {
                n2 = 2048;
                break;
            }
            case 56: {
                n2 = 4098;
                break;
            }
            case 57: {
                n2 = 8192;
                break;
            }
            case 35: {
                n2 = 0 | 0x8000;
                break;
            }
            case 42: {
                n2 = 16384;
                break;
            }
            case -11: {
                n2 = 0 | 0x200000;
                break;
            }
            default: {
                if (n == f.ab || (f.ab == 21 || f.ab == -21) && n == -f.ab) {
                    n2 = 0 | 0x10000;
                    break;
                }
                if (n == f.ac || (f.ac == 22 || f.ac == -22) && n == -f.ac) {
                    n2 = 0 | 0x20000;
                    break;
                }
                bl = false;
            }
        }
        if (bl) return n2;
        switch (this.getGameAction(n)) {
            case 1: {
                int n3 = n2;
                int n4 = 1;
                return n3 | n4;
            }
            case 6: {
                int n3 = n2;
                int n4 = 2;
                return n3 | n4;
            }
            case 2: {
                int n3 = n2;
                int n4 = 4;
                return n3 | n4;
            }
            case 5: {
                int n3 = n2;
                int n4 = 8;
                return n3 | n4;
            }
            case 8: {
                if (n == -10) return n2;
                int n3 = n2;
                int n4 = 0x100000;
                return n3 | n4;
            }
        }
        return n2;
    }

    protected void keyPressed(int n) {
        b = n;
        int n2 = this.b(n);
        c = n2;
        d |= (e |= n2);
    }

    protected void keyReleased(int n) {
        int n2 = this.b(n);
        e &= ~n2;
    }

    protected void keyRepeated(int n) {
    }

    private static void e() {
        b = 0;
        h = c;
        c = 0;
        f = false;
        ++k;
        if (h != 0) {
            if (j != 0 && k < 6 && j == h) {
                i = h;
                j = 0;
            } else {
                k = 0;
                i = 0;
                j = h;
            }
        } else if (i != 0) {
            i = 0;
        }
        j.h();
    }

    private static void f() {
        j.g();
    }

    public static final void l() {
        h = 0;
        d = 0;
        b = 0;
        c = 0;
        e = 0;
        f = false;
        g = 0;
        j.a[0] = 0;
        j.a[1] = 0;
    }

    private static final int g() {
        if (!f) {
            g = d;
            d = e;
            f = true;
        }
        return g;
    }

    public static boolean m() {
        return h != 0;
    }

    public static boolean g(int n) {
        return (j.g() & n) != 0;
    }

    public static boolean h(int n) {
        return (i & n) != 0;
    }

    public static boolean i(int n) {
        return (h & n) != 0;
    }

    public static boolean n() {
        return aR && j.i(65536) || !aR && j.i(131072) || j.i(0x100000);
    }

    public static boolean o() {
        return aR && j.i(131072) || !aR && j.i(65536) || j.i(0x200000);
    }

    protected void pointerPressed(int n, int n2) {
        System.out.println("Pressedx = " + n + ", y = " + n2);
        l = n;
        m = n2;
    }

    protected void pointerReleased(int n, int n2) {
        System.out.println("Releasedx = " + n + ", y = " + n2);
    }

    protected void pointerDragged(int n, int n2) {
        System.out.println("Draggedx = " + n + ", y = " + n2);
    }

    private static final void h() {
        l = -1;
        m = -1;
    }

    public static void p() {
        o = false;
    }

    public static void j(int n) {
        aV = n;
        j.n = 1000 / aV;
    }

    public void run() {
        while (o) {
            p = System.currentTimeMillis();
            ++aX;
            j.e();
            this.b();
            j.f();
            this.repaint();
            this.serviceRepaints();
            if (q < p / 1000L) {
                q = p / 1000L;
                aW = 1;
            } else {
                ++aW;
            }
            do {
                try {
                    Thread.currentThread();
                    Thread.sleep(1L);
                }
                catch (Exception exception) {}
            } while (System.currentTimeMillis() - p < n);
        }
        this.a();
        aQ.notifyDestroyed();
    }

    public abstract void a();

    public abstract void b();

    public abstract void a(Graphics var1);

    public boolean a(byte by) {
        return true;
    }

    public void paint(Graphics graphics) {
        graphics.setFont(d.a);
        this.a(graphics);
    }

    public static void a(Graphics graphics, Image image, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (!f.aa) {
            graphics.drawRegion(image, n, n2, n3, n4, n5, n6, n7, n8);
            return;
        }
        aT = DirectUtils.getDirectGraphics((Graphics)graphics);
        int n9 = 0;
        int n10 = image.getWidth();
        int n11 = image.getHeight();
        int n12 = 0;
        switch (n5) {
            case 2: {
                n9 = 8192;
                n = n10 - (n + n3);
                break;
            }
            case 1: {
                n9 = 16384;
                n2 = n11 - (n2 + n4);
                break;
            }
            case 3: {
                n9 = 180;
                n = n10 - (n + n3);
                n2 = n11 - (n2 + n4);
                break;
            }
            case 5: {
                n9 = 270;
                n12 = n2;
                n2 = n;
                n = n11 - (n12 + n4);
                n12 = n3;
                n3 = n4;
                n4 = n12;
                break;
            }
            case 7: {
                n9 = 8282;
                n12 = n;
                n = n11 - (n2 + n4);
                n2 = n10 - (n12 + n3);
                n12 = n3;
                n3 = n4;
                n4 = n12;
                break;
            }
            case 4: {
                n9 = 16474;
                n12 = n;
                n = n2;
                n2 = n12;
                n12 = n3;
                n3 = n4;
                n4 = n12;
                break;
            }
            case 6: {
                n9 = 90;
                n12 = n;
                n = n2;
                n2 = n10 - (n12 + n3);
                n12 = n3;
                n3 = n4;
                n4 = n12;
                break;
            }
            default: {
                n9 = 0;
            }
        }
        graphics.setClip(n6, n7, n3, n4);
        aT.drawImage(image, n6 - n, n7 - n2, n8, n9);
    }

    public void a(Graphics graphics, int n, int n2, int n3, int n4) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(144, 112, 72);
        graphics.fillRect(n, n2, n3, n4);
        graphics.setColor(216, 200, 160);
        graphics.fillRect(n + 3, n2 + 5, n3 - 6, n4 - 8);
    }

    public void a(Graphics graphics, String string, int n, int n2, int n3) {
        int n4 = 12097640;
        int n5 = 5781512;
        graphics.setColor(n4);
        j.a(graphics, string, n + 1, n2 + 1, n3, false);
        graphics.setColor(n5);
        j.a(graphics, string, n, n2, n3, true);
    }

    public void b(Graphics graphics) {
        d.b(graphics, f.ad - 6, f.ae - 3 + (aX & 1), 4, 0, 0xFFFFFF);
    }

    public void a(Graphics graphics, byte by, int n, byte by2) {
    }

    public static void a(Graphics graphics, String string, int n, int n2, int n3, boolean bl) {
        int n4 = 0;
        String string2 = null;
        r = graphics.getColor();
        for (int i2 = 0; i2 < string.length(); ++i2) {
            int n5;
            char c2 = string.charAt(i2);
            if (c2 != '[') continue;
            string2 = string.substring(n4, i2);
            graphics.drawString(string2, n, n2, n3);
            n4 = i2 + 1;
            n += graphics.getFont().stringWidth(string2) + 1;
            c2 = string.charAt(++i2);
            if (c2 == 'C' && string.charAt(i2 + 1) == ':') {
                i2 += 2;
                int n6 = 0;
                try {
                    n6 = Integer.valueOf(string.substring(i2, i2 + 6), 16);
                    i2 += 6;
                }
                catch (Exception exception) {}
                if (bl) {
                    s = n6;
                    graphics.setColor(s);
                }
                n5 = i2;
            } else {
                if (c2 != '/' || string.charAt(i2 + 1) != 'C') continue;
                i2 += 2;
                if (bl) {
                    graphics.setColor(r);
                }
                n5 = i2;
            }
            n4 = n5 + 1;
        }
        if (n4 < string.length()) {
            graphics.drawString(string.substring(n4), n, n2, n3);
        }
    }

    public static byte k(int n) {
        byte[] byArray = aZ[aY];
        for (int i2 = 0; i2 < byArray.length; i2 += 2) {
            if (n != byArray[i2]) continue;
            return byArray[i2 + 1];
        }
        return -1;
    }

    public abstract void c();

    public abstract void d();

    public abstract void a(boolean var1);

    public int a(int n) {
        return -1;
    }

    public void c(Graphics graphics) {
        graphics.setClip(0, 0, f.ad, f.ae);
        graphics.setColor(0);
        graphics.fillRect(0, 0, f.ad, f.ae);
        if (d.a() == d.h) {
            graphics.setColor(0xFFFFFF);
            graphics.drawString("d.cn...", f.ad >> 1, (f.ae >> 1) - (d.d + d.c), 17);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public static boolean a(byte[] byArray, String string) {
        block13: {
            RecordStore recordStore;
            block12: {
                if (byArray == null) {
                    return false;
                }
                recordStore = null;
                recordStore = RecordStore.openRecordStore((String)string, (boolean)true);
                if (recordStore.getNumRecords() == 0) {
                    recordStore.addRecord(byArray, 0, byArray.length);
                    break block12;
                }
                recordStore.setRecord(1, byArray, 0, byArray.length);
            }
            try {
                recordStore.closeRecordStore();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            break block13;
            catch (Exception exception) {
                try {
                    Exception exception3 = exception;
                    exception.printStackTrace();
                }
                catch (Throwable throwable) {
                    try {
                        recordStore.closeRecordStore();
                    }
                    catch (Exception exception4) {
                        Exception exception5 = exception4;
                        exception4.printStackTrace();
                    }
                    throw throwable;
                }
                try {
                    recordStore.closeRecordStore();
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     */
    public static byte[] a(String string) {
        byte[] byArray;
        block10: {
            byArray = null;
            RecordStore recordStore = null;
            recordStore = RecordStore.openRecordStore((String)string, (boolean)false);
            byArray = recordStore.getRecord(1);
            try {
                recordStore.closeRecordStore();
            }
            catch (Exception exception) {
                Exception exception2 = exception;
                exception.printStackTrace();
            }
            break block10;
            catch (Exception exception) {
                Exception exception3;
                try {
                    exception3 = exception;
                    exception.printStackTrace();
                }
                catch (Throwable throwable) {
                    try {
                        recordStore.closeRecordStore();
                    }
                    catch (Exception exception4) {
                        Exception exception5 = exception4;
                        exception4.printStackTrace();
                    }
                    throw throwable;
                }
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception exception6) {
                    exception3 = exception6;
                    exception6.printStackTrace();
                }
            }
        }
        return byArray;
    }

    static {
        aU = false;
        a = new int[2];
        l = -1;
        m = -1;
        aV = 10;
        n = 1000 / aV;
        o = true;
        p = 0L;
        q = 0L;
        aW = 1;
        aX = 0;
        r = 0;
        s = 0;
        aY = 0;
        aZ = null;
    }
}

