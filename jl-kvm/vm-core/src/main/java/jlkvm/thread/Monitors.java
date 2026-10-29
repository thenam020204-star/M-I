package jlkvm.thread;

import jlkvm.Vm;
import jlkvm.link.VmThrow;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Object monitors (T14 skeleton). A record exists only while the object is locked or has waiters/queued threads
 * (flag FLAG_MONITOR in the object header, key = reference: the collector must re-key after compaction, {@link #refs}).
 * Ownership is handed over directly to the first queued thread when the monitor is released (deterministic).
 */
public final class Monitors {
    public static final class Monitor {
        int owner;
        int count;
        final ArrayDeque<VmThread> entry = new ArrayDeque<VmThread>();
        final ArrayList<VmThread> waiters = new ArrayList<VmThread>();
    }

    private final Vm vm;
    private final Map<Integer, Monitor> map = new HashMap<Integer, Monitor>();

    public Monitors(Vm vm) { this.vm = vm; }

    public int size() { return map.size(); }
    /** All references that have a monitor record (GC roots / re-keying). */
    public int[] refs() {
        int[] r = new int[map.size()];
        int i = 0;
        for (Integer k : map.keySet()) r[i++] = k;
        return r;
    }
    /** GC (after marking): forgets the records of objects that did not survive. */
    public void dropDead(jlkvm.heap.Heap h) {
        for (java.util.Iterator<Integer> it = map.keySet().iterator(); it.hasNext(); ) if (!h.isMarked(it.next())) it.remove();
    }
    /** GC (compaction): re-keys every record to the new address of its object. */
    public void relocate(jlkvm.heap.Forwarder f) {
        Map<Integer, Monitor> n = new HashMap<Integer, Monitor>();
        for (Map.Entry<Integer, Monitor> e : map.entrySet()) n.put(f.forward(e.getKey()), e.getValue());
        map.clear();
        map.putAll(n);
    }
    public void rekey(int oldRef, int newRef) {
        Monitor m = map.remove(oldRef);
        if (m != null) map.put(newRef, m);
    }

    /** @return true when acquired; false when the thread was queued (state BLOCKED_ENTER). */
    public boolean enter(VmThread th, int ref) {
        Monitor m = map.get(ref);
        if (m == null) {
            m = new Monitor();
            map.put(ref, m);
            vm.heap.setHasMonitor(ref, true);
        }
        if (m.owner == 0) { m.owner = th.id; m.count = 1; return true; }
        if (m.owner == th.id) { m.count++; return true; }
        th.state = VmThread.BLOCKED_ENTER;
        th.pendingCount = 1;
        th.waitMon = m;
        m.entry.add(th);
        return false;
    }

    private Monitor owned(VmThread th, int ref) {
        Monitor m = map.get(ref);
        if (m == null || m.owner != th.id) throw new VmThrow("java/lang/IllegalMonitorStateException", "current thread not owner");
        return m;
    }

    public void exit(VmThread th, int ref) {
        Monitor m = owned(th, ref);
        if (--m.count == 0) release(m, ref);
    }

    private void release(Monitor m, int ref) {
        m.owner = 0;
        m.count = 0;
        if (!grant(m) && m.waiters.isEmpty()) {
            map.remove(ref);
            vm.heap.setHasMonitor(ref, false);
        }
    }

    private boolean grant(Monitor m) {
        if (m.owner != 0) return false;
        VmThread n = m.entry.poll();
        if (n == null) return false;
        m.owner = n.id;
        m.count = n.pendingCount;
        n.waitMon = null;
        vm.sched.makeRunnable(n);
        return true;
    }

    /** Object.wait(millis): releases the monitor completely and parks the thread. */
    public void await(VmThread th, int ref, long millis) {
        Monitor m = owned(th, ref);
        if (th.interrupted) {
            th.interrupted = false;
            throw new VmThrow("java/lang/InterruptedException", null);
        }
        th.pendingCount = m.count;
        m.waiters.add(th);
        th.state = VmThread.WAITING;
        th.waitMon = m;
        th.waitRef = ref;
        if (millis > 0) vm.sched.addTimed(th, vm.nanoTime() + millis * 1000000L);
        m.owner = 0;
        m.count = 0;
        grant(m);
    }

    public void notify(VmThread th, int ref, boolean all) {
        Monitor m = owned(th, ref);
        while (!m.waiters.isEmpty()) {
            VmThread w = m.waiters.remove(0);
            vm.sched.removeTimed(w);
            toEntry(m, w);
            if (!all) break;
        }
    }

    /** Wake every waiter on {@code ref} regardless of ownership (thread death -> joiners). */
    public void wakeAll(int ref) {
        Monitor m = map.get(ref);
        if (m == null) return;
        while (!m.waiters.isEmpty()) {
            VmThread w = m.waiters.remove(0);
            vm.sched.removeTimed(w);
            toEntry(m, w);
        }
        if (m.owner == 0 && m.entry.isEmpty() && m.waiters.isEmpty()) { map.remove(ref); vm.heap.setHasMonitor(ref, false); }
    }

    private void toEntry(Monitor m, VmThread w) {
        w.state = VmThread.BLOCKED_ENTER;
        w.waitMon = m;
        m.entry.add(w);
        grant(m);
    }

    /** Timer of a timed wait fired. */
    public void timeout(VmThread w) {
        Monitor m = w.waitMon;
        if (m == null) return;
        m.waiters.remove(w);
        toEntry(m, w);
    }

    /** Thread.interrupt(): wakes sleep/wait with InterruptedException, otherwise just sets the flag. */
    public void interrupt(VmThread t) {
        if (t.state == VmThread.SLEEPING) {
            vm.sched.removeTimed(t);
            t.pendingThrow = "java/lang/InterruptedException";
            vm.sched.makeRunnable(t);
        } else if (t.state == VmThread.WAITING && t.joinTarget != null) {
            t.joinTarget.joiners.remove(t);
            t.joinTarget = null;
            t.pendingThrow = "java/lang/InterruptedException";
            vm.sched.makeRunnable(t);
        } else if (t.state == VmThread.WAITING) {
            Monitor m = t.waitMon;
            m.waiters.remove(t);
            vm.sched.removeTimed(t);
            t.pendingThrow = "java/lang/InterruptedException";
            toEntry(m, t);
        } else {
            t.interrupted = true;
        }
    }
}
