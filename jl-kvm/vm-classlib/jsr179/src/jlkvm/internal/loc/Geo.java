/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour: the project's own reading of the API, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.loc;

/** ROM-private: the arithmetic of Coordinates (CLDC 1.1 has sin/cos/sqrt but no atan / asin, so atan is written here). */
public final class Geo {
    /** Earth radius in meters (WGS84 equatorial; the distance is spherical, UNVERIFIED). */
    private static final double R = 6378137.0;

    private Geo() {
    }

    /** atan(z) for any z: argument halving twice into |z| < 0.42, then the Taylor series. */
    static double atan(double z) {
        boolean neg = z < 0.0;
        double x = neg ? -z : z;
        boolean inv = x > 1.0;
        if (inv) {
            x = 1.0 / x;
        }
        for (int i = 0; i < 2; i++) {
            x = x / (1.0 + Math.sqrt(1.0 + x * x));   // atan(x) = 2 atan(x / (1 + sqrt(1 + x^2)))
        }
        double x2 = x * x, term = x, sum = 0.0;
        for (int k = 0; k < 14; k++) {
            sum += term / (2 * k + 1);
            term = -term * x2;
        }
        sum *= 4.0;
        if (inv) {
            sum = Math.PI / 2 - sum;
        }
        return neg ? -sum : sum;
    }

    static double atan2(double y, double x) {
        if (x > 0.0) {
            return atan(y / x);
        }
        if (x < 0.0) {
            return y >= 0.0 ? atan(y / x) + Math.PI : atan(y / x) - Math.PI;
        }
        return y > 0.0 ? Math.PI / 2 : y < 0.0 ? -Math.PI / 2 : 0.0;
    }

    /** Haversine distance in meters. */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double p1 = Math.toRadians(lat1), p2 = Math.toRadians(lat2);
        double dp = p2 - p1, dl = Math.toRadians(lon2 - lon1);
        double s1 = Math.sin(dp / 2.0), s2 = Math.sin(dl / 2.0);
        double a = s1 * s1 + Math.cos(p1) * Math.cos(p2) * s2 * s2;
        if (a > 1.0) {
            a = 1.0;
        }
        return 2.0 * R * atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
    }

    /** Initial bearing in degrees [0, 360). */
    public static double azimuth(double lat1, double lon1, double lat2, double lon2) {
        double p1 = Math.toRadians(lat1), p2 = Math.toRadians(lat2);
        double dl = Math.toRadians(lon2 - lon1);
        double y = Math.sin(dl) * Math.cos(p2);
        double x = Math.cos(p1) * Math.sin(p2) - Math.sin(p1) * Math.cos(p2) * Math.cos(dl);
        double deg = Math.toDegrees(atan2(y, x));
        if (deg < 0.0) {
            deg += 360.0;
        }
        return deg >= 360.0 ? 0.0 : deg;
    }

    private static String two(long v) {
        return v < 10 ? "0" + v : Long.toString(v);
    }

    private static String milli(long v) {
        return v < 10 ? "00" + v : v < 100 ? "0" + v : Long.toString(v);
    }

    /** [-]D:MM:SS.sss (type 1) or [-]D:MM.mmm (type 2); IllegalArgumentException for a coordinate outside [-180, 180] or another type. */
    public static String format(double coordinate, int type) {
        if (!(coordinate >= -180.0 && coordinate <= 180.0) || (type != 1 && type != 2)) {
            throw new IllegalArgumentException("coordinate or type");
        }
        boolean neg = coordinate < 0.0;
        double a = neg ? -coordinate : coordinate;
        StringBuffer sb = new StringBuffer();
        if (type == 1) {
            long t = (long) (a * 3600000.0 + 0.5);   // thousandths of a second
            long sec = t % 60000;
            sb.append(t / 3600000).append(':').append(two((t / 60000) % 60)).append(':').append(two(sec / 1000)).append('.').append(milli(sec % 1000));
        } else {
            long t = (long) (a * 60000.0 + 0.5);   // thousandths of a minute
            long min = t % 60000;
            sb.append(t / 60000).append(':').append(two(min / 1000)).append('.').append(milli(min % 1000));
        }
        String r = sb.toString();
        return neg && !isZero(r) ? "-" + r : r;
    }

    private static boolean isZero(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '0' && c != ':' && c != '.') {
                return false;
            }
        }
        return true;
    }

    private static double num(String s, boolean fraction) {
        if (s.length() == 0) {
            throw new IllegalArgumentException("empty field");
        }
        int dots = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                dots++;
            } else if (c < '0' || c > '9') {
                throw new IllegalArgumentException("bad character in " + s);
            }
        }
        if (dots > 1 || (dots == 1 && !fraction) || s.charAt(0) == '.' || s.charAt(s.length() - 1) == '.') {
            throw new IllegalArgumentException("bad number " + s);
        }
        return Double.parseDouble(s);
    }

    /** [-]D, [-]D.ddd, [-]D:MM, [-]D:MM.mmm, [-]D:MM:SS, [-]D:MM:SS.sss; minutes and seconds below 60. IllegalArgumentException for anything else. */
    public static double parse(String s) {
        if (s == null) {
            throw new NullPointerException("coordinate");
        }
        boolean neg = s.startsWith("-");
        String b = neg ? s.substring(1) : s;
        int p1 = b.indexOf(':');
        double v;
        if (p1 < 0) {
            v = num(b, true);
        } else {
            int p2 = b.indexOf(':', p1 + 1);
            double d = num(b.substring(0, p1), false);
            if (p2 < 0) {
                double m = num(b.substring(p1 + 1), true);
                if (m >= 60.0) {
                    throw new IllegalArgumentException("minutes");
                }
                v = d + m / 60.0;
            } else {
                double m = num(b.substring(p1 + 1, p2), false);
                double sec = num(b.substring(p2 + 1), true);
                if (m >= 60.0 || sec >= 60.0) {
                    throw new IllegalArgumentException("minutes or seconds");
                }
                v = d + m / 60.0 + sec / 3600.0;
            }
        }
        if (v > 180.0) {
            throw new IllegalArgumentException("out of range");
        }
        return neg ? -v : v;
    }
}
