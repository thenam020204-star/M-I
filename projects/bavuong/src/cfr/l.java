/*
 * Decompiled with CFR 0.152.
 */
public final class l {
    private static final int[] a = new int[256];

    static {
        int n2 = 0;
        while (n2 < 256) {
            int n3 = n2;
            int n4 = 0;
            while (n4 < 8) {
                n3 = (n3 & 1) != 0 ? n3 >>> 1 ^ 0xEDB88320 : (n3 >>>= 1);
                ++n4;
            }
            l.a[n2] = n3;
            ++n2;
        }
    }

    private static int a(byte[] byArray, int n2, int n3) {
        byArray[n2 + 0] = (byte)(n3 >>> 24);
        byArray[n2 + 1] = (byte)(n3 >>> 16 & 0xFF);
        byArray[n2 + 2] = (byte)(n3 >>> 8 & 0xFF);
        byArray[n2 + 3] = (byte)n3;
        return n2 + 4;
    }

    private static int c(byte[] byArray, int n2) {
        return (byArray[n2 + 0] & 0xFF) << 24 | (byArray[n2 + 1] & 0xFF) << 16 | (byArray[n2 + 2] & 0xFF) << 8 | byArray[n2 + 3] & 0xFF;
    }

    private static int b(byte[] byArray, int n2, int n3) {
        int n4 = -1;
        int n5 = n2 + n3;
        int[] nArray = a;
        while (n2 != n5) {
            n4 = nArray[(n4 ^ byArray[n2]) & 0xFF] ^ n4 >>> 8;
            ++n2;
        }
        return ~n4;
    }

    public static final int a(byte[] byArray) {
        int n2 = 8;
        while (byArray[n2 + 4] != 80 || byArray[n2 + 5] != 76 || byArray[n2 + 6] != 84 || byArray[n2 + 7] != 69) {
            int n3 = l.c(byArray, n2);
            n2 += 8 + n3 + 4;
        }
        return n2;
    }

    public static final int a(byte[] byArray, int n2) {
        return l.c(byArray, n2) / 3;
    }

    public static final int b(byte[] byArray, int n2) {
        int n3 = l.c(byArray, n2);
        int n4 = l.b(byArray, n2 + 4, 4 + n3);
        return l.a(byArray, n2 + 4 + 4 + n3, n4);
    }

    public static final void a(byte[] byArray, int n2, int n3, int[] nArray, int[] nArray2) {
        byte[] byArray2 = new byte[3];
        int n4 = 0;
        while (n4 < n3) {
            System.arraycopy(byArray, n2 + 4 + 4 + 3 * n4, byArray2, 0, 3);
            int n5 = 0;
            while (n5 < 5) {
                if (nArray[n5 * 3 + 0] == (byArray2[0] & 0xFF) && nArray[n5 * 3 + 1] == (byArray2[1] & 0xFF) && nArray[n5 * 3 + 2] == (byArray2[2] & 0xFF)) {
                    byArray2[0] = (byte)nArray2[n5 * 3 + 0];
                    byArray2[1] = (byte)nArray2[n5 * 3 + 1];
                    byArray2[2] = (byte)nArray2[n5 * 3 + 2];
                    System.arraycopy(byArray2, 0, byArray, n2 + 4 + 4 + 3 * n4, 3);
                }
                ++n5;
            }
            ++n4;
        }
    }

    public static final void a(byte[] byArray, int n2, int n3, int n4) {
        byte[] byArray2 = new byte[3];
        int n5 = 0;
        int n6 = 0;
        boolean bl = false;
        while (n5 < n3) {
            System.arraycopy(byArray, n2 + 4 + 4 + 3 * n5, byArray2, 0, 3);
            n6 = n4 == 1 ? ((byArray2[0] & 0xFF) + (byArray2[1] & 0xFF) + (byArray2[2] & 0xFF)) / 3 : 0;
            byArray2[1] = byArray2[2] = (byte)n6;
            byArray2[0] = byArray2[2];
            System.arraycopy(byArray2, 0, byArray, n2 + 4 + 4 + 3 * n5, 3);
            ++n5;
        }
    }
}

