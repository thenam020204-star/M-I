/*
 * Decompiled with CFR 0.152.
 */
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class f {
    public static boolean Z = false;
    public static boolean aa = false;
    public static int ab = -6;
    public static int ac = -7;
    public static int ad = 0;
    public static int ae = 0;
    public static int af = 8;
    public static int ag = af << 8;
    public static int ah = af >> 1;
    public static String ai = ".png";
    public static boolean aj = false;
    public static int ak = af - 2;
    public static int al = 0;
    public static int am = 0;
    public static int an = 0;
    public static int ao = 0;
    public static int ap = 0;
    public static int aq = 0;
    public static boolean ar = false;
    public static int as = (al + ap) / af + 1;
    public static int at = (am + aq) / af + 1;
    public static int au = as * af;
    public static int av = at * af;
    public static final int[] aw = new int[]{0, 2, 1, 3};
    public static short[] ax;
    public static String[] ay;
    public static String[] az;
    public static short[] aA;
    public static String[] aB;
    public static short[] aC;
    public static int[] aD;

    public static void a(int n) {
        af = n;
        ag = af << 8;
        ah = af >> 1;
        ak = af - 2;
    }

    public static void a(int n, int n2) {
        al = n;
        am = n2;
        ap = al % af == 0 ? 0 : af - al % af;
        aq = am % af == 0 ? 0 : af - am % af;
        as = (al + ap) / af + 1;
        at = (am + aq) / af + 1;
        au = as * af;
        av = at * af;
    }

    public static int b(int n) {
        return aA[n * 3 + 0];
    }

    public static int c(int n) {
        return aC[n * 9 + 0];
    }

    public static void a(String string) {
        byte[] byArray = g.a(string, aj);
        try {
            int n;
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(byArray));
            short s = dataInputStream.readShort();
            az = new String[s << 1];
            for (n = 0; n < az.length; ++n) {
                f.az[n] = dataInputStream.readUTF();
            }
            aA = new short[s * 3];
            for (n = 0; n < aA.length; ++n) {
                f.aA[n] = dataInputStream.readShort();
            }
            s = dataInputStream.readShort();
            aB = new String[s];
            for (n = 0; n < aB.length; ++n) {
                f.aB[n] = dataInputStream.readUTF();
            }
            aC = new short[s * 9];
            for (n = 0; n < aC.length; ++n) {
                f.aC[n] = dataInputStream.readShort();
            }
            aD = new int[s];
            for (n = 0; n < aD.length; ++n) {
                f.aD[n] = dataInputStream.readInt();
            }
            s = dataInputStream.readShort();
            ay = new String[s];
            for (n = 0; n < ay.length; ++n) {
                f.ay[n] = dataInputStream.readUTF();
            }
            ax = new short[s * 10];
            for (n = 0; n < ax.length; ++n) {
                f.ax[n] = dataInputStream.readShort();
            }
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    static {
        int[] nArray = new int[]{0, 8192, 16384, 180};
        ax = null;
        ay = null;
        az = null;
        aA = null;
        aB = null;
        aC = null;
        aD = null;
    }
}

