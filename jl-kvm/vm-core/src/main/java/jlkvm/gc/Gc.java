package jlkvm.gc;

import jlkvm.Vm;
import jlkvm.debug.DebugFlags;
import jlkvm.classfile.ClassFile;
import jlkvm.heap.Forwarder;
import jlkvm.heap.Heap;
import jlkvm.interp.Frames;
import jlkvm.link.ClassInfo;
import jlkvm.link.RMethod;
import jlkvm.natives.Disposable;
import jlkvm.natives.Handles;
import jlkvm.thread.VmThread;
import jlkvm.verify.TypeMaps;
import jlkvm.verify.TypeMaps.RefMap;
import java.util.BitSet;
import java.util.Map;

/**
 * Precise stop-the-world collector (PLAN D4, T13): mark, then sliding compaction (docs/gc.md has the whole protocol).
 * <ol>
 * <li>Mark from the roots with an explicit stack ({@code FLAG_MARK} in the object header). WeakReference referents are not traced.</li>
 * <li>Clear the referents of weak references whose target was not marked, release the host handles whose owner died,
 *     drop the monitor records of dead objects.</li>
 * <li>One linear walk of the (parsable) heap builds a table of live runs: old start, new start, length.</li>
 * <li>Rewrite every reference (roots and fields of live objects) through the run table, slide the runs down with
 *     {@code System.arraycopy}, set the allocation pointer. Header words move with the object, so identity hashes survive.</li>
 * </ol>
 * Roots: thread stacks (reference maps from {@link TypeMaps}), statics, class mirrors, string-literal cache, intern table,
 * {@code TempRoots}, thread objects, monitor locks, the preallocated OutOfMemoryError. The vm-thread is the only thread
 * (D5), so every other green thread is parked with its registers published; the running thread publishes them before any
 * allocation that may collect (see Interpreter and docs/gc.md for the safepoint rules).
 */
public final class Gc implements Heap.Collector, Forwarder {
    private final Vm vm;
    private final Heap heap;
    private final int[] mem;
    private final TypeMaps maps;

    private int[] markStack = new int[1024];
    private int markTop;
    private int[] weaks = new int[16];
    private int nWeak;
    private int[] runOld = new int[256], runNew = new int[256], runLen = new int[256];
    private int nRuns;
    private boolean updating, active;
    private BitSet starts;          // gcVerify: object starts of the heap before the collection
    private String where = "";

    // statistics (T36 uses them)
    public long collections, lastLiveWords, lastFreedWords, lastNanos, totalNanos, maxNanos, lastMarkedObjects;

    public Gc(Vm vm) {
        this.vm = vm;
        this.heap = vm.heap;
        this.mem = vm.heap.mem;
        this.maps = vm.linker.typeMaps;
    }

    // ------------------------------------------------------------------ entry point

    public void collect(Heap h, int neededWords) {
        if (active) return;   // never re-entered: the collector does not allocate guest objects
        active = true;
        long t0 = System.nanoTime();
        try {
            int topBefore = heap.top();
            boolean verify = vm.config.gcVerify;
            if (verify) { starts = new BitSet(topBefore); for (int o = heap.firstObject(); o != 0; o = heap.nextObject(o)) starts.set(o); }
            // 1. mark
            updating = false;
            markTop = 0;
            nWeak = 0;
            lastMarkedObjects = 0;
            rootPass();
            drain();
            // 2. weak references, peers, monitors
            clearWeak();
            sweepHandles();
            vm.monitors.dropDead(heap);
            // 3. live runs
            buildRuns(topBefore);
            int newTop = nRuns == 0 ? Heap.BASE : runNew[nRuns - 1] + runLen[nRuns - 1];
            boolean moves = newTop != topBefore;
            // 4. update references and slide
            updating = moves;
            if (moves) rootPass();
            walkLive(moves);
            if (moves) {
                for (int i = 0; i < nRuns; i++) if (runOld[i] != runNew[i]) System.arraycopy(mem, runOld[i], mem, runNew[i], runLen[i]);
                vm.monitors.relocate(this);
                heap.setTop(newTop);
            }
            updating = false;
            collections++;
            lastLiveWords = newTop - Heap.BASE;
            lastFreedWords = topBefore - newTop;
            if (verify) verifyHeap();
            if (DebugFlags.ENABLED && vm.config.debug) vm.host.log(1, "gc", "#" + collections + " live=" + (lastLiveWords * 4) + "B freed=" + (lastFreedWords * 4) + "B objects=" + lastMarkedObjects);
        } finally {
            updating = false;
            active = false;
            starts = null;
            long dt = System.nanoTime() - t0;
            lastNanos = dt;
            totalNanos += dt;
            if (dt > maxNanos) maxNanos = dt;
        }
    }

    // ------------------------------------------------------------------ roots (shared by the mark and the update pass)

    /** Visits one root location: marks the referent, or rewrites the location with the object's new address. */
    private void slot(int[] a, int i) {
        int v = a[i];
        if (v != 0) a[i] = updating ? forward(v) : markRoot(v);
    }

    private int fix(int v) {
        if (v == 0) return 0;
        return updating ? forward(v) : markRoot(v);
    }

    private int markRoot(int ref) {
        if (starts != null && !starts.get(ref)) throw new IllegalStateException("GC verify: root " + ref + " is not an object start (" + where + ")");
        mark(ref);
        return ref;
    }

    private void rootPass() {
        // TempRoots
        for (int i = 0, n = vm.roots.size(); i < n; i++) { int v = vm.roots.get(i); if (v != 0) { if (starts != null) where = "temp root " + i; vm.roots.set(i, fix(v)); } }
        // host cells (preallocated OutOfMemoryError)
        where = "host cell";
        int[] cells = vm.hostRootCells();
        for (int i = 0; i < cells.length; i++) slot(cells, i);
        // classes: statics, mirror, string literal cache
        for (ClassInfo c : vm.linker.allClasses()) {
            if (starts != null) where = "statics of " + c.name;
            int[] s = c.statics;
            for (int i = 0; i < s.length; i++) if (c.staticIsRef[i]) slot(s, i);
            if (c.mirror != 0) c.mirror = fix(c.mirror);
            int[] sr = c.strRefs;
            if (sr != null) for (int i = 0; i < sr.length; i++) if (sr[i] != 0) slot(sr, i);
        }
        // intern table
        where = "intern table";
        for (Map.Entry<String, Integer> e : vm.internTable().entrySet()) e.setValue(fix(e.getValue()));
        // handle owners are weak: handled by sweepHandles / walkLive
        if (updating) {
            Handles h = vm.handles;
            for (int id = 1; id < h.capacity(); id++) if (h.rawAt(id) != null && h.owner(id) != 0) h.setOwner(id, forward(h.owner(id)));
        }
        // threads
        for (VmThread t : vm.sched.threads()) threadRoots(t);
    }

    private void threadRoots(VmThread th) {
        if (starts != null) where = "thread " + th.id;
        if (th.threadObj != 0) th.threadObj = fix(th.threadObj);
        if (th.waitRef != 0) th.waitRef = fix(th.waitRef);
        for (int i = 0; i < th.lockTop; i++) slot(th.locks, i);
        if (th.method == null || th.state == VmThread.DEAD || th.fp < 0) return;
        final int[] st = th.stack;
        int fp = th.fp, pc = th.pc;
        RMethod m = th.method;
        int regionEnd = th.sp;
        boolean top = true, useAt = th.inNative == null;
        RMethod nat = th.inNative;
        for (;;) {
            if (starts != null) where = "thread " + th.id + " frame " + m + " pc " + pc;
            if (m.code == null) throw new IllegalStateException("GC: frame of a method without code: " + m);
            ClassFile cf = m.owner.cf;
            RefMap map;
            try {
                map = useAt ? maps.at(cf, m.mi, pc) : maps.atCall(cf, m.mi, pc);
            } catch (jlkvm.verify.VerifyException e) {
                throw new IllegalStateException("GC needs a verified method (no type map for " + m + "): " + e.getMessage());
            }
            if (map == null) throw new IllegalStateException("GC: no type map at pc " + pc + " of " + m + (useAt ? " (before)" : " (call)"));
            int hdr = fp + m.maxLocals, base = hdr + Frames.HDR;
            for (int i = 0; i < m.maxLocals; i++) if (map.localIsRef(i)) slot(st, fp + i);
            int end = regionEnd;
            if (top && nat != null) {
                end = regionEnd - nat.argWords;
                for (int j = 0; j < nat.argWords; j++) if (nat.argWordIsRef[j]) slot(st, end + j);
            }
            int depth = Math.min(end - base, map.stackDepth());
            for (int j = 0; j < depth; j++) if (map.stackIsRef(j)) slot(st, base + j);
            slot(st, hdr + Frames.SYNC);
            int flags = st[hdr + Frames.FLAGS];
            if ((flags & Frames.F_EXTRA_REF) != 0) slot(st, hdr + Frames.EXTRA);
            int pfp = st[hdr + Frames.PREV_FP];
            if (pfp < 0) break;
            // the caller of a class-initialisation frame is stopped BEFORE its instruction (it re-executes); other callers are inside an invoke
            useAt = (flags & Frames.F_INIT) != 0;
            regionEnd = fp;
            pc = st[hdr + Frames.PREV_PC];
            m = vm.linker.method(st[hdr + Frames.PREV_MID]);
            fp = pfp;
            top = false;
        }
    }

    // ------------------------------------------------------------------ mark

    private void mark(int ref) {
        int h = mem[ref];
        if (h < 0) return;
        mem[ref] = h | Heap.FLAG_MARK;
        lastMarkedObjects++;
        if (markTop == markStack.length) markStack = java.util.Arrays.copyOf(markStack, markTop * 2);
        markStack[markTop++] = ref;
    }

    private void check(int child, int parent) {
        if (!starts.get(child)) throw new IllegalStateException("GC verify: object " + parent + " (" + vm.linker.byId(mem[parent] & Heap.CLASS_MASK).name + ") holds " + child + ", not an object start");
    }

    private void drain() {
        final boolean verify = starts != null;
        while (markTop > 0) {
            int o = markStack[--markTop];
            int h = mem[o];
            int kind = (h & Heap.KIND_MASK) >>> Heap.KIND_SHIFT;
            if (kind == Heap.K_OBJECT) {
                ClassInfo c = vm.linker.byId(h & Heap.CLASS_MASK);
                int[] slots = c.refSlots;
                int skip = -1;
                if (c.isWeakRef) {
                    skip = c.weakSlot;
                    if (nWeak == weaks.length) weaks = java.util.Arrays.copyOf(weaks, nWeak * 2);
                    weaks[nWeak++] = o;
                }
                for (int i = 0; i < slots.length; i++) {
                    int s = slots[i];
                    if (s == skip) continue;
                    int child = mem[o + Heap.HEADER + s];
                    if (child != 0) {
                        if (verify) check(child, o);
                        if (mem[child] >= 0) mark(child);
                    }
                }
            } else if (kind == Heap.K_REF) {
                int n = mem[o + 2], b = o + Heap.ARRAY_HEADER;
                for (int i = 0; i < n; i++) {
                    int child = mem[b + i];
                    if (child != 0) {
                        if (verify) check(child, o);
                        if (mem[child] >= 0) mark(child);
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------ after marking

    /** CLDC 1.1 WeakReference: an unmarked referent is cleared (there are no reference queues and no finalizers in CLDC). */
    private void clearWeak() {
        for (int i = 0; i < nWeak; i++) {
            int w = weaks[i];
            ClassInfo c = vm.linker.byId(mem[w] & Heap.CLASS_MASK);
            if (c.weakSlot < 0) continue;
            int a = w + Heap.HEADER + c.weakSlot;
            int r = mem[a];
            if (r != 0 && mem[r] >= 0) mem[a] = 0;
        }
    }

    /** Peer cleanup (D8): host objects whose guest owner died are released (and disposed). */
    private void sweepHandles() {
        Handles h = vm.handles;
        for (int id = 1; id < h.capacity(); id++) {
            Object o = h.rawAt(id);
            if (o == null) continue;
            int owner = h.owner(id);
            if (owner != 0 && mem[owner] >= 0) {
                h.release(id);
                if (o instanceof Disposable) ((Disposable) o).dispose();
            }
        }
    }

    // ------------------------------------------------------------------ compaction

    private void buildRuns(int top) {
        nRuns = 0;
        int newTop = Heap.BASE;
        boolean inRun = false;
        int p = Heap.BASE;
        while (p < top) {
            int size = heap.sizeWords(p);
            if (mem[p] < 0) {
                if (!inRun) {
                    if (nRuns == runOld.length) {
                        runOld = java.util.Arrays.copyOf(runOld, nRuns * 2);
                        runNew = java.util.Arrays.copyOf(runNew, nRuns * 2);
                        runLen = java.util.Arrays.copyOf(runLen, nRuns * 2);
                    }
                    runOld[nRuns] = p;
                    runNew[nRuns] = newTop;
                    runLen[nRuns] = 0;
                    nRuns++;
                    inRun = true;
                }
                runLen[nRuns - 1] += size;
                newTop += size;
            } else inRun = false;
            p += size;
        }
    }

    /** New address of a marked object (binary search in the run table). Valid between the run walk and the slide. */
    public int forward(int ref) {
        int lo = 0, hi = nRuns - 1;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (runOld[mid] <= ref) lo = mid; else hi = mid - 1;
        }
        return runNew[lo] + (ref - runOld[lo]);
    }

    /** Walks every live object at its old address: clears the mark and (when objects move) rewrites its reference fields. */
    private void walkLive(boolean rewrite) {
        for (int r = 0; r < nRuns; r++) {
            int p = runOld[r], end = p + runLen[r];
            while (p < end) {
                int h = mem[p];
                int size = heap.sizeWords(p);
                int kind = (h & Heap.KIND_MASK) >>> Heap.KIND_SHIFT;
                if (rewrite) {
                    if (kind == Heap.K_OBJECT) {
                        int[] slots = vm.linker.byId(h & Heap.CLASS_MASK).refSlots;
                        for (int i = 0; i < slots.length; i++) {
                            int a = p + Heap.HEADER + slots[i];
                            int v = mem[a];
                            if (v != 0) mem[a] = forward(v);
                        }
                    } else if (kind == Heap.K_REF) {
                        int n = mem[p + 2], b = p + Heap.ARRAY_HEADER;
                        for (int i = 0; i < n; i++) {
                            int v = mem[b + i];
                            if (v != 0) mem[b + i] = forward(v);
                        }
                    }
                }
                mem[p] = h & ~Heap.FLAG_MARK;
                p += size;
            }
        }
    }

    // ------------------------------------------------------------------ verification (VmConfig.gcVerify)

    private void verifyHeap() {
        BitSet ok = new BitSet(heap.top());
        for (int o = heap.firstObject(); o != 0; o = heap.nextObject(o)) {
            if (mem[o] < 0) throw new IllegalStateException("GC verify: mark left on " + o);
            ok.set(o);
        }
        Handles hs = vm.handles;
        for (int id = 1; id < hs.capacity(); id++)
            if (hs.rawAt(id) != null && hs.owner(id) != 0 && !ok.get(hs.owner(id))) throw new IllegalStateException("GC verify: handle " + id + " owner " + hs.owner(id) + " is not an object start");
        for (int o = heap.firstObject(); o != 0; o = heap.nextObject(o)) {
            int h = mem[o];
            int kind = (h & Heap.KIND_MASK) >>> Heap.KIND_SHIFT;
            if (kind == Heap.K_OBJECT) {
                for (int s : vm.linker.byId(h & Heap.CLASS_MASK).refSlots) {
                    int v = mem[o + Heap.HEADER + s];
                    if (v != 0 && !ok.get(v)) throw new IllegalStateException("GC verify: dangling field in object " + o + " -> " + v);
                }
            } else if (kind == Heap.K_REF) {
                for (int i = 0; i < mem[o + 2]; i++) {
                    int v = mem[o + Heap.ARRAY_HEADER + i];
                    if (v != 0 && !ok.get(v)) throw new IllegalStateException("GC verify: dangling element in array " + o + " -> " + v);
                }
            }
        }
    }
}
