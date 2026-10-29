import java.lang.reflect.Method;

/** Runs bench.BenchMain.run(name, n) directly on the host JVM (reference time for the interpretation overhead). */
public class NativeRef {
    public static void main(String[] a) throws Exception {
        Method m = Class.forName("bench.BenchMain").getMethod("run", String.class, int.class);
        String name = a[0];
        int n = Integer.parseInt(a[1]);
        String res = "";
        double best = 1e18;
        for (int r = 0; r < 5; r++) {
            long t0 = System.nanoTime();
            res = (String) m.invoke(null, name, n);
            best = Math.min(best, (System.nanoTime() - t0) / 1e6);
        }
        System.out.println("NATIVE " + name + " " + String.format("%.0f", best) + " ms " + res);
    }
}
