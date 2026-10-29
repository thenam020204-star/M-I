package bench;

import javax.microedition.midlet.MIDlet;

/**
 * T13b guest benchmark (PLAN D2 gate). The workload is chosen by the MIDlet property "workload" and scaled by "n";
 * the VM host (tests/bench/run_bench.sh -> jlkvm.tmp.Bench) counts executed bytecodes. Every workload returns a checksum that
 * must be identical on the project VM and on a JVM (it is printed as "> workload checksum").
 */
public class BenchMain extends MIDlet {
    // ---------------------------------------------------------------- arithmetic (int and long, branches)
    static int arith(int n) {
        int a = 1, b = 2, c = 3;
        for (int i = 0; i < n; i++) {
            a = a * 31 + i;
            b ^= a >>> 3;
            c += (b & 0xFF) - (a >> 5);
            if ((c & 1) == 0) a++; else b--;
        }
        return a ^ b ^ c;
    }

    static long arithLong(int n) {
        long a = 1, b = 2;
        for (int i = 0; i < n; i++) {
            a = a * 6364136223846793005L + i;
            b ^= a >>> 7;
            if ((b & 4) != 0) a += b; else a -= i;
        }
        return a ^ b;
    }

    // ---------------------------------------------------------------- virtual / interface / static calls, fields
    static abstract class Shape {
        int k;
        abstract int area(int x);
        int twice(int x) { return area(x) + area(x + 1); }
    }
    static final class Sq extends Shape { int area(int x) { return x * x + k; } }
    static final class Circ extends Shape { int area(int x) { return 3 * x + k; } }
    static final class Tri extends Shape { int area(int x) { return (x >> 1) + 7 + k; } }
    interface Op { int apply(int x); }
    static final class Inc implements Op { public int apply(int x) { return x + 1; } }
    static final class Dbl implements Op { public int apply(int x) { return x << 1; } }
    static int sq(int x) { return x * x; }

    static int calls(int n) {
        Shape[] s = new Shape[] { new Sq(), new Circ(), new Tri() };
        for (int i = 0; i < s.length; i++) s[i].k = i;
        Op[] o = new Op[] { new Inc(), new Dbl() };
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += s[i % 3].twice(i & 255);
            sum += o[i & 1].apply(sum & 1023);
            sum += sq(i & 15);
        }
        return sum;
    }

    // ---------------------------------------------------------------- arrays
    static int arrays(int n) {
        int[] a = new int[1024];
        byte[] b = new byte[4096];
        char[] c = new char[512];
        int[][] m = new int[16][16];
        int sum = 0;
        for (int r = 0; r < n; r++) {
            for (int i = 0; i < a.length; i++) a[i] = i * r;
            for (int i = 0; i < a.length; i++) sum += a[i];
            for (int i = 0; i < b.length; i++) b[i] = (byte) (i + r);
            for (int i = 0; i < b.length; i += 7) sum ^= b[i];
            for (int i = 0; i < c.length; i++) c[i] = (char) (i ^ r);
            for (int i = 0; i < c.length; i += 5) sum += c[i];
            for (int i = 0; i < 16; i++) for (int j = 0; j < 16; j++) { m[i][j] = i + j + r; sum += m[j][i]; }
        }
        return sum;
    }

    // ---------------------------------------------------------------- allocation (short-lived lists, small arrays, collection)
    static final class Node { Node next; int v; }
    static int alloc(int n) {
        Node head = null;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            Node x = new Node();
            x.v = i;
            if ((i & 63) != 0) x.next = head;
            head = x;
            if ((i & 15) == 0) { int[] t = new int[8 + (i & 7)]; t[0] = i; sum += t[0] + t.length; }
            if ((i & 255) == 255) { int k = 0; for (Node p = head; p != null; p = p.next) k += p.v & 3; sum += k; }
        }
        return sum;
    }

    // ---------------------------------------------------------------- strings / library code written in Java (stub ROM)
    static int strings(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("item").append(i).append(':').append(i * 3);
            String s = sb.toString();
            sum += s.length() + s.charAt(s.length() - 1) + s.indexOf(':');
        }
        return sum;
    }

    // ---------------------------------------------------------------- floating point (CLDC 1.1)
    static float fp(int n) {
        float x = 0.5f, y = 1.25f;
        for (int i = 0; i < n; i++) {
            x = x * 1.0001f + y * 0.5f;
            y = y - x * 0.00001f;
            if (x > 1000f) x = x * 0.001f;
        }
        return x + y;
    }

    /** @return the checksum of the named workload run n times its unit size */
    public static String run(String name, int n) {
        if (name.equals("arith")) return "" + arith(n * 100000);
        if (name.equals("long")) return "" + arithLong(n * 100000);
        if (name.equals("calls")) return "" + calls(n * 30000);
        if (name.equals("arrays")) return "" + arrays(n * 40);
        if (name.equals("alloc")) return "" + alloc(n * 60000);
        if (name.equals("strings")) return "" + strings(n * 8000);
        if (name.equals("fp")) return "" + fp(n * 100000);
        return "unknown workload " + name;
    }

    protected void startApp() {
        String w = getAppProperty("workload");
        String n = getAppProperty("n");
        int scale = n == null ? 1 : Integer.parseInt(n);
        System.out.println("> " + w + " " + run(w == null ? "arith" : w, scale));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
