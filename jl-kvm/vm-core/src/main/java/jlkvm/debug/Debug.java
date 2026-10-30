package jlkvm.debug;

import jlkvm.Vm;
import jlkvm.heap.Heap;
import jlkvm.link.ClassInfo;
import jlkvm.link.RMethod;
import jlkvm.thread.VmThread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Debug hooks of the VM (PLAN T16, D15): per-method bytecode counters, allocation profiler by pc, trace ring, exception log.
 * One instance per {@link Vm}, created only when {@link DebugFlags#ENABLED} and a feature is switched on in the VmConfig. Every
 * call site is {@code if (DebugFlags.ENABLED && dbg != null)}.
 */
public final class Debug {
    private final Vm vm;
    private final boolean count, profile, logExceptions;
    public final TraceRing ring;
    /** (method id, pc) -> {count, bytes, class id of the last allocation}. */
    private final Map<Long, long[]> sites = new HashMap<Long, long[]>();
    private final List<RMethod> counted = new ArrayList<RMethod>();

    public Debug(Vm vm) {
        this.vm = vm;
        this.count = vm.config.countBytecodes;
        this.profile = vm.config.profileAlloc;
        this.logExceptions = vm.config.logExceptions;
        this.ring = vm.config.traceRing > 0 ? new TraceRing(vm.config.traceRing) : null;
    }

    /** True when at least one per-instruction/allocation feature is on (otherwise the Vm keeps no Debug object). */
    public static boolean wanted(jlkvm.VmConfig c) {
        return c.countBytecodes || c.profileAlloc || c.logExceptions || c.traceRing > 0;
    }

    /** Before every instruction. */
    public void step(VmThread th, RMethod m, int pc, int op) {
        if (ring != null) ring.put(th.id, m.id, pc, op);
        if (count) {
            if (m.bcCount++ == 0) counted.add(m);
        }
    }

    public void allocObject(RMethod m, int pc, ClassInfo c) { site(m, pc, c, (c.instanceWords + Heap.HEADER) * 4L); }

    public void allocArray(RMethod m, int pc, ClassInfo ac, int len) {
        long eb;
        switch (ac.elemKind) {
            case Heap.K_BOOLEAN: case Heap.K_BYTE: eb = 1; break;
            case Heap.K_CHAR: case Heap.K_SHORT: eb = 2; break;
            case Heap.K_LONG: case Heap.K_DOUBLE: eb = 8; break;
            default: eb = 4; break;
        }
        site(m, pc, ac, Heap.ARRAY_HEADER * 4L + eb * Math.max(0, len));
    }

    private void site(RMethod m, int pc, ClassInfo c, long bytes) {
        if (!profile) return;
        long key = ((long) m.id << 32) | (pc & 0xFFFFFFFFL);
        long[] s = sites.get(key);
        if (s == null) { s = new long[3]; sites.put(key, s); }
        s[0]++;
        s[1] += bytes;
        s[2] = c.id;
    }

    /** A guest exception was raised (before a handler is searched). */
    public void exception(VmThread th, int ex) {
        if (!logExceptions) return;
        vm.host.log(1, "exception", "thread " + th.id + " throws " + vm.describe(ex) + "\n" + vm.traceText(ex));
    }

    /** Last {@code n} traced instructions, oldest first, as text. */
    public String ringText(int n) {
        if (ring == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int[] e : ring.last(n)) {
            RMethod m = vm.linker.method(e[1]);
            sb.append("T").append(e[0]).append(' ').append(m).append(" @").append(e[2]).append(" op=").append(e[3]).append('\n');
        }
        return sb.toString();
    }

    public void uncaught(VmThread th) {
        if (ring != null) vm.host.log(3, "trace", "last instructions before the uncaught exception of thread " + th.id + ":\n" + ringText(24));
    }

    /** Top {@code n} methods by executed bytecodes and top {@code n} allocation sites. */
    public String report(int n) {
        StringBuilder sb = new StringBuilder();
        if (count) {
            List<RMethod> l = new ArrayList<RMethod>(counted);
            Collections.sort(l, new Comparator<RMethod>() { public int compare(RMethod a, RMethod b) { return a.bcCount < b.bcCount ? 1 : a.bcCount > b.bcCount ? -1 : 0; } });
            sb.append("bytecodes by method:\n");
            for (int i = 0; i < Math.min(n, l.size()); i++) sb.append("  ").append(l.get(i).bcCount).append("  ").append(l.get(i)).append('\n');
        }
        if (profile) {
            List<Map.Entry<Long, long[]>> l = new ArrayList<Map.Entry<Long, long[]>>(sites.entrySet());
            Collections.sort(l, new Comparator<Map.Entry<Long, long[]>>() { public int compare(Map.Entry<Long, long[]> a, Map.Entry<Long, long[]> b) {
                return a.getValue()[1] < b.getValue()[1] ? 1 : a.getValue()[1] > b.getValue()[1] ? -1 : 0; } });
            sb.append("allocation sites by bytes:\n");
            for (int i = 0; i < Math.min(n, l.size()); i++) {
                long key = l.get(i).getKey();
                long[] s = l.get(i).getValue();
                RMethod m = vm.linker.method((int) (key >>> 32));
                int pc = (int) key;
                sb.append("  ").append(s[1]).append(" B  ").append(s[0]).append("x  ").append(vm.linker.byId((int) s[2]).name).append("  at ")
                  .append(m).append(" pc=").append(pc);
                int line = m.lineAt(pc);
                if (line >= 0) sb.append(" line ").append(line);
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public long allocSites() { return sites.size(); }
}
