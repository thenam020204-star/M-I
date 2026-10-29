package jlkvm.thread;

import jlkvm.ExecutionEngine;
import jlkvm.Vm;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Green-thread scheduler (D5, T14 skeleton): round robin, time slice counted in bytecodes. */
public final class Scheduler {
    private final Vm vm;
    private final ArrayDeque<VmThread> runq = new ArrayDeque<VmThread>();
    private final ArrayList<VmThread> timed = new ArrayList<VmThread>();
    private final ArrayList<VmThread> blockedNative = new ArrayList<VmThread>();
    private final Map<Integer, VmThread> byId = new HashMap<Integer, VmThread>();
    private int nextId = 1;
    private long idleUntil;
    public VmThread current;
    /** Bytecodes executed by guest threads since the VM started (benchmarks, T13b; natives' charges included). */
    public long bytecodes;

    public Scheduler(Vm vm) { this.vm = vm; }

    public VmThread create() {
        VmThread t = new VmThread(nextId++, vm.profile.threadStackWords);
        byId.put(t.id, t);
        return t;
    }
    public VmThread thread(int id) { return byId.get(id); }
    public int liveCount() { return byId.size(); }
    public Iterable<VmThread> threads() { return byId.values(); }
    public long idleUntilNanos() { return idleUntil; }

    public void makeRunnable(VmThread t) {
        t.state = VmThread.RUNNABLE;
        t.wakeAt = 0;
        runq.add(t);
    }
    public void addTimed(VmThread t, long wakeAt) { t.wakeAt = wakeAt; if (!timed.contains(t)) timed.add(t); }
    public void removeTimed(VmThread t) { timed.remove(t); t.wakeAt = 0; }
    public void addBlockedNative(VmThread t) { blockedNative.add(t); }

    private void pollTimers(long now) {
        for (int i = 0; i < timed.size(); ) {
            VmThread t = timed.get(i);
            if (t.wakeAt <= now) {
                timed.remove(i);
                t.wakeAt = 0;
                if (t.state == VmThread.SLEEPING) makeRunnable(t);
                else if (t.state == VmThread.WAITING) vm.monitors.timeout(t);
            } else i++;
        }
    }

    private void pollBlocked() {
        for (int i = 0; i < blockedNative.size(); ) {
            VmThread t = blockedNative.get(i);
            if (vm.interp.completeBlocked(t)) { blockedNative.remove(i); makeRunnable(t); }
            else i++;
        }
    }

    public int runSlice(int max) {
        int budget = max;
        while (budget > 0) {
            if (vm.exitRequested) return ExecutionEngine.EXITED;
            long now = vm.nanoTime();
            if (!timed.isEmpty()) pollTimers(now);
            if (!blockedNative.isEmpty()) pollBlocked();
            VmThread t = runq.poll();
            if (t == null) {
                if (byId.isEmpty()) return ExecutionEngine.EXITED;
                long u = Long.MAX_VALUE;
                for (VmThread x : timed) u = Math.min(u, x.wakeAt);
                if (!blockedNative.isEmpty()) u = Math.min(u, now + 1000000L);
                idleUntil = u;
                return ExecutionEngine.IDLE_UNTIL;
            }
            current = t;
            t.state = VmThread.RUNNING;
            vm.charged = 0;
            int slice = Math.min(vm.profile.timesliceBytecodes, budget);
            int used = vm.interp.run(t, slice);
            bytecodes += Math.max(0, used);
            budget -= Math.max(1, used) + vm.charged;
            current = null;
            if (t.state == VmThread.RUNNING) { t.state = VmThread.RUNNABLE; runq.add(t); }
            else if (t.state == VmThread.DEAD) ended(t);
        }
        return vm.exitRequested ? ExecutionEngine.EXITED : ExecutionEngine.RUNNING;
    }

    private void ended(VmThread t) {
        byId.remove(t.id);
        timed.remove(t);
        for (VmThread j : t.joiners) { j.joinTarget = null; makeRunnable(j); }
        t.joiners.clear();
    }
}
