/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** sin/cos/tan/sqrt/ceil/floor are VM natives (fdlibm semantics, the same on every host). */
public final class Math {
    public static final double E = 2.7182818284590452354;
    public static final double PI = 3.14159265358979323846;

    private Math() {
    }

    public static native double sin(double a);

    public static native double cos(double a);

    public static native double tan(double a);

    public static native double sqrt(double a);

    public static native double ceil(double a);

    public static native double floor(double a);

    public static int abs(int a) {
        return a < 0 ? -a : a;
    }

    public static long abs(long a) {
        return a < 0 ? -a : a;
    }

    public static float abs(float a) {
        return a <= 0.0F ? 0.0F - a : a;
    }

    public static double abs(double a) {
        return a <= 0.0D ? 0.0D - a : a;
    }

    public static int max(int a, int b) {
        return a >= b ? a : b;
    }

    public static long max(long a, long b) {
        return a >= b ? a : b;
    }

    public static float max(float a, float b) {
        if (a != a) {
            return a;
        }
        if (a == 0.0f && b == 0.0f && Float.floatToIntBits(a) == 0x80000000) {
            return b;
        }
        return a >= b ? a : b;
    }

    public static double max(double a, double b) {
        if (a != a) {
            return a;
        }
        if (a == 0.0d && b == 0.0d && Double.doubleToLongBits(a) == 0x8000000000000000L) {
            return b;
        }
        return a >= b ? a : b;
    }

    public static int min(int a, int b) {
        return a <= b ? a : b;
    }

    public static long min(long a, long b) {
        return a <= b ? a : b;
    }

    public static float min(float a, float b) {
        if (a != a) {
            return a;
        }
        if (a == 0.0f && b == 0.0f && Float.floatToIntBits(b) == 0x80000000) {
            return b;
        }
        return a <= b ? a : b;
    }

    public static double min(double a, double b) {
        if (a != a) {
            return a;
        }
        if (a == 0.0d && b == 0.0d && Double.doubleToLongBits(b) == 0x8000000000000000L) {
            return b;
        }
        return a <= b ? a : b;
    }

    public static double toRadians(double angdeg) {
        return angdeg / 180.0 * PI;
    }

    public static double toDegrees(double angrad) {
        return angrad * 180.0 / PI;
    }
}
