package jlkvm.tmp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import jlkvm.Vm;

/**
 * TEMPORARY benchmark driver (T13b): {@code Bench ROM.jar BENCH.jar workload n reps heapKb}. Runs the guest benchmark reps times (the first
 * run is a warm-up of the host JIT and is not reported as best), prints one line per run with bytecodes, milliseconds and
 * million bytecodes per second, and "best".
 */
public final class Bench {
    public static void main(String[] a) throws Exception {
        String rom = a[0], jar = a[1], workload = a[2];
        int reps = Integer.parseInt(a[4]);
        long heap = Long.parseLong(a[5]) << 10;
        RunJar.extraProps.put("workload", workload);
        RunJar.extraProps.put("n", a[3]);
        double best = 0;
        String checksum = "";
        for (int r = 0; r <= reps; r++) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            final long[] bc = new long[2];
            long t0 = System.nanoTime();
            RunJar.run(rom, jar, false, false, false, false, 600, new PrintStream(bo, true, "UTF-8"), 0, false, heap, new RunJar.Inspector() {
                public void inspect(Vm vm) { bc[0] = vm.sched.bytecodes; bc[1] = vm.gc.collections; } });
            long ns = System.nanoTime() - t0;
            checksum = bo.toString("UTF-8").trim();
            double mbps = bc[0] / (ns / 1e9) / 1e6;
            System.out.println("  " + (r == 0 ? "warm-up " : "run " + r + "    ") + workload + ": " + bc[0] + " bytecodes in " + ns / 1000000 + " ms = " + String.format("%.1f", mbps) + " Mbc/s, gc=" + bc[1]);
            if (r > 0 && mbps > best) best = mbps;
        }
        System.out.println("BEST " + workload + " " + String.format("%.1f", best) + " Mbc/s " + checksum);
    }
}
