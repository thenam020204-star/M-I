package jlkvm;

import jlkvm.gc.Gc;
import jlkvm.heap.Heap;
import jlkvm.heap.TempRoots;
import jlkvm.interp.CoreNatives;
import jlkvm.interp.Frames;
import jlkvm.interp.Interpreter;
import jlkvm.link.ClassInfo;
import jlkvm.link.Errors;
import jlkvm.link.Linker;
import jlkvm.link.RMethod;
import jlkvm.link.VmThrow;
import jlkvm.natives.GuestServices;
import jlkvm.natives.Handles;
import jlkvm.natives.NativeRegistry;
import jlkvm.thread.Monitors;
import jlkvm.thread.Scheduler;
import jlkvm.thread.VmThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/** The VM hub (PLAN "Interface giữa module"). All state is touched by the vm-thread only, except {@link #postEvent} / {@link #requestExit}. */
public final class Vm implements ExecutionEngine, GuestServices {
    public final VmConfig config;
    public final DeviceProfile profile;
    public final HostPlatform host;
    public final ClassSource app;
    public final Heap heap;
    public final TempRoots roots = new TempRoots();
    public final Handles handles = new Handles();
    public final NativeRegistry registry = new NativeRegistry();
    public final Linker linker;
    public final Scheduler sched;
    public final Monitors monitors;
    public final Interpreter interp;
    public final Gc gc;

    public volatile boolean exitRequested;
    /** Bytecodes charged to the running slice by natives (see {@link #charge}). */
    public int charged;
    /** Class whose <clinit> the interpreter must run next (result 1 of {@link #initStep}). */
    public ClassInfo initRun;

    private final Map<String, Integer> internTable = new HashMap<String, Integer>();
    private final ConcurrentLinkedQueue<int[]> events = new ConcurrentLinkedQueue<int[]>();
    private final long baseNanos, baseMillis;
    /** Preallocated OutOfMemoryError (GC root: cell 0). */
    private final int[] oomCell = new int[1];
    private ClassInfo stringClass, throwableClass;
    private int strValue, strOffset, strCount, thMessage, thTrace, threadVmId;

    private Vm(VmConfig cfg, HostPlatform host, ClassSource app, DeviceProfile p) {
        this.config = cfg;
        this.profile = p;
        this.host = host;
        this.app = app;
        this.heap = new Heap(p.heapBytes, cfg.hashSeed);
        this.baseNanos = host.hostNanoTime();
        this.baseMillis = System.currentTimeMillis();
        registry.setServices(this);
        CoreNatives.register(this);
        host.registerNatives(registry);
        this.linker = new Linker(this, host.rom(), app);
        this.monitors = new Monitors(this);
        this.sched = new Scheduler(this);
        this.interp = new Interpreter(this);
        this.gc = new Gc(this);
        heap.setCollector(gc);
        heap.setStressInterval(cfg.gcStressInterval);
    }

    public static Vm create(VmConfig cfg, HostPlatform host, ClassSource app, DeviceProfile p) {
        Vm vm = new Vm(cfg, host, app, p);
        vm.boot();
        return vm;
    }

    private void boot() {
        linker.find("java/lang/Object");
        stringClass = linker.find("java/lang/String");
        linker.find("java/lang/Class");
        throwableClass = linker.find("java/lang/Throwable");
        ClassInfo th = linker.find("java/lang/Thread");
        for (ClassInfo c : new ClassInfo[] {linker.objectClass, stringClass, linker.classClass, throwableClass, th}) markInitialisedIfTrivial(c);
        strValue = stringClass.findDeclaredField("value", null).slot;
        strOffset = stringClass.findDeclaredField("offset", null).slot;
        strCount = stringClass.findDeclaredField("count", null).slot;
        thMessage = throwableClass.findDeclaredField("message", null).slot;
        thTrace = throwableClass.findDeclaredField("trace", null).slot;
        threadVmId = th.findDeclaredField("vmId", null).slot;
        ClassInfo oom = linker.find("java/lang/OutOfMemoryError");
        markInitialisedIfTrivial(oom);
        oomCell[0] = heap.allocObject(oom.id);
        if (oomCell[0] == 0) throw new IllegalStateException("heap too small");
    }

    /**
     * Boot-time and host-side creation of instances (no guest {@code new} runs): a class whose chain from the top has no
     * <clinit> is marked initialised (statics prepared); at the first class with a <clinit> the walk stops, so that the
     * interpreter runs it at the first real use (JVMS 5.5) instead of never.
     */
    private void markInitialisedIfTrivial(ClassInfo c) {
        java.util.ArrayList<ClassInfo> chain = new java.util.ArrayList<ClassInfo>();
        for (ClassInfo k = c; k != null; k = k.superClass) chain.add(0, k);
        for (ClassInfo k : chain) {
            if (k.state != ClassInfo.LINKED) continue;
            if (k.clinit != null) {
                host.log(2, "vm", "host-created instance of " + c.name + ": <clinit> of " + k.name + " runs at its first guest use");
                return;
            }
            linker.prepareStatics(k);
            k.state = ClassInfo.INITIALIZED;
        }
    }

    // ------------------------------------------------------------------ ExecutionEngine

    public int runSlice(int maxBytecodes) { return sched.runSlice(maxBytecodes); }
    public long idleUntilNanos() { return sched.idleUntilNanos(); }

    /** Thread-safe: queued for the guest event loop (D13). */
    public void postEvent(int type, int a, int b, int c) { events.add(new int[] {type, a, b, c}); }
    public int[] pollEvent() { return events.poll(); }
    public void requestExit() { exitRequested = true; }

    public long nanoTime() { return host.hostNanoTime(); }
    public long currentTimeMillis() { return baseMillis + (nanoTime() - baseNanos) / 1000000L; }

    // ------------------------------------------------------------------ launching threads

    /** Starts the main guest thread running {@code cls.method(Ljava/lang/String;)V}-like static method with one String argument. */
    public VmThread launch(String cls, String method, String desc, String arg) {
        ClassInfo c = linker.find(cls);
        RMethod m = c.findDeclared(method, desc);
        if (m == null || !m.isStatic) throw new IllegalArgumentException("no static method " + cls + "." + method + desc);
        VmThread t = sched.create();
        int tobj = heap.allocObject(linker.find("java/lang/Thread").id);
        heap.putInt(tobj, threadVmId, t.id);
        t.threadObj = tobj;
        t.initFirst = c;   // JVMS 5.5: the class of a static method is initialised before the method runs
        int s = arg == null ? 0 : newString(arg);
        enterFrame(t, m, s);
        sched.makeRunnable(t);
        return t;
    }

    /** Thread.start(): runs {@code run()V} of the Thread object. */
    public void startThread(int threadObj) {
        if (heap.getInt(threadObj, threadVmId) != 0) throw new VmThrow("java/lang/IllegalThreadStateException", null);
        ClassInfo c = linker.byId(heap.classId(threadObj));
        RMethod run = c.selectVirtual(linker.find("java/lang/Thread").findDeclared("run", "()V"));
        VmThread t = sched.create();
        t.threadObj = threadObj;
        heap.putInt(threadObj, threadVmId, t.id);
        enterFrame(t, run, threadObj);
        sched.makeRunnable(t);
    }

    /** Runtime.gc(): a full collection now (called from a native: the thread state is published). */
    public void collectGarbage() { gc.collect(heap, 0); }

    /** Host-side references the collector must treat as roots and rewrite (cell 0: the preallocated OutOfMemoryError). */
    public int[] hostRootCells() { return oomCell; }

    public int threadVmIdSlot() { return threadVmId; }

    private void enterFrame(VmThread t, RMethod m, int arg) {
        int[] st = t.stack;
        int n = 0;
        if (m.argWords > 0) st[n++] = arg;
        int hdr = m.maxLocals;
        st[hdr + Frames.PREV_FP] = -1;
        st[hdr + Frames.PREV_MID] = 0;
        st[hdr + Frames.PREV_PC] = 0;
        st[hdr + Frames.SYNC] = 0;
        st[hdr + Frames.FLAGS] = 0;
        st[hdr + Frames.EXTRA] = 0;
        st[hdr + Frames.LOCKS] = 0;
        t.fp = 0;
        t.method = m;
        t.pc = 0;
        t.sp = hdr + Frames.HDR;
    }

    // ------------------------------------------------------------------ class initialisation (T12 skeleton)

    /** @return 0 initialised (or being initialised by this thread), 1 run {@link #initRun}'s <clinit> first, 2 another thread is initialising: yield and retry. */
    public int initStep(VmThread th, ClassInfo c) {
        switch (c.state) {
            case ClassInfo.INITIALIZED: return 0;
            case ClassInfo.ERROR: throw Errors.noClassDef(c.name);
            case ClassInfo.INITIALIZING: return c.initThread == th.id ? 0 : 2;
            default: break;
        }
        if (!c.isInterface && c.superClass != null && c.superClass.state != ClassInfo.INITIALIZED) {
            int r;
            try {
                r = initStep(th, c.superClass);
            } catch (VmThrow x) {
                c.state = ClassInfo.ERROR;
                throw x;
            }
            if (r != 0) return r;
        }
        c.state = ClassInfo.INITIALIZING;
        c.initThread = th.id;
        linker.prepareStatics(c);
        if (c.clinit == null) {
            c.state = ClassInfo.INITIALIZED;
            c.initThread = 0;
            return 0;
        }
        initRun = c;
        return 1;
    }

    // ------------------------------------------------------------------ strings

    public int newString(String s) {
        if (s == null) return 0;
        int n = s.length();
        ClassInfo ca = linker.primitiveArray(Heap.K_CHAR);
        int arr = heap.allocArray(ca.id, Heap.K_CHAR, n);
        if (arr == 0) return 0;
        for (int i = 0; i < n; i++) heap.putChar(arr, i, s.charAt(i));
        int mark = roots.mark();
        int ri = roots.push(arr);
        int obj = heap.allocObject(stringClass.id);
        arr = roots.get(ri);
        roots.release(mark);
        if (obj == 0) return 0;
        heap.putRef(obj, strValue, arr);
        heap.putInt(obj, strOffset, 0);
        heap.putInt(obj, strCount, n);
        return obj;
    }

    public String string(int ref) {
        if (ref == 0) return null;
        int arr = heap.getRef(ref, strValue), off = heap.getInt(ref, strOffset), n = heap.getInt(ref, strCount);
        char[] cs = new char[n];
        for (int i = 0; i < n; i++) cs[i] = heap.getChar(arr, off + i);
        return new String(cs);
    }

    /** Canonical guest String for the given content (string literals, String.intern). */
    public int intern(String s) {
        Integer r = internTable.get(s);
        if (r != null) return r;
        int ref = newString(s);
        if (ref != 0) internTable.put(s, ref);
        return ref;
    }

    /** String.intern() for an existing guest string. */
    public int internExisting(int ref) {
        String s = string(ref);
        Integer r = internTable.get(s);
        if (r != null) return r;
        internTable.put(s, ref);
        return ref;
    }

    public Map<String, Integer> internTable() { return internTable; }

    public int newByteArray(byte[] b, int off, int len) {
        int a = heap.allocArray(linker.primitiveArray(Heap.K_BYTE).id, Heap.K_BYTE, len);
        if (a == 0) return 0;
        for (int i = 0; i < len; i++) heap.putByte(a, i, b[off + i]);
        return a;
    }

    public String className(int objRef) { return linker.byId(heap.classId(objRef)).name.replace('/', '.'); }

    public void charge(int bytecodes) { interp.nativeCtx().chargeBytecodes(bytecodes); }

    // ------------------------------------------------------------------ exceptions

    public VmThrow oomThrow(String what) { return new VmThrow(oomCell[0]); }

    public int materialize(VmThrow t, VmThread th) {
        if (t.ref != 0) return t.ref;
        return newThrowable(t.guestClass, t.msg, th);
    }

    /** Allocates a guest Throwable without running its constructor (message and stack trace filled in directly). */
    public int newThrowable(String cls, String msg, VmThread th) {
        ClassInfo c;
        try {
            c = linker.find(cls);
        } catch (VmThrow x) {
            c = linker.find("java/lang/Error");
            msg = cls + (msg == null ? "" : ": " + msg);
        }
        markInitialisedIfTrivial(c);
        int ex = heap.allocObject(c.id);
        if (ex == 0) return oomCell[0];
        int mark = roots.mark();
        int ri = roots.push(ex);
        if (msg != null) {
            int s = newString(msg);
            if (s != 0) heap.putRef(roots.get(ri), thMessage, s);
        }
        if (th != null) fillTrace(roots.get(ri), th, false);
        ex = roots.get(ri);
        roots.release(mark);
        return ex;
    }

    /**
     * ExceptionInInitializerError for a <clinit> that threw a non-Error. CLDC has no such class, so the guest class is the
     * nearest CLDC superclass (java.lang.Error, CLDC 4.2/5.1.4); message "ExceptionInInitializerError: <class>: <message>" and
     * the stack trace of the original exception are kept.
     */
    public int wrapInitializerError(int orig) {
        int mark = roots.mark();
        int ri = roots.push(orig);
        String text = describe(orig);
        int ex = newThrowable(Errors.guest("java/lang/ExceptionInInitializerError"), "ExceptionInInitializerError: " + text, null);
        orig = roots.get(ri);
        roots.release(mark);
        if (ex != oomCell[0]) heap.putRef(ex, thTrace, heap.getRef(orig, thTrace));
        return ex;
    }

    /** Records (method id, pc) pairs of the frames of {@code th} into the Throwable's trace field. */
    public void fillTrace(int ex, VmThread th, boolean skipInits) {
        int[] st = th.stack;
        int fp = th.fp, pc = th.pc;
        RMethod m = th.method;
        int max = profile.maxTraceFrames;
        int[] buf = new int[max * 2];
        int n = 0;
        boolean skipping = skipInits;
        while (m != null && n < max) {
            if (skipping && m.name.equals("<init>") && m.owner.isSubclassOf(throwableClass) && st[fp] == ex) {
                // constructor chain of the exception itself
            } else {
                skipping = false;
                buf[2 * n] = m.id;
                buf[2 * n + 1] = pc;
                n++;
            }
            int hdr = fp + m.maxLocals;
            int pfp = st[hdr + Frames.PREV_FP];
            if (pfp < 0) break;
            pc = st[hdr + Frames.PREV_PC];
            m = linker.method(st[hdr + Frames.PREV_MID]);
            fp = pfp;
        }
        int mark = roots.mark();
        int ri = roots.push(ex);
        int arr = heap.allocArray(linker.primitiveArray(Heap.K_INT).id, Heap.K_INT, 2 * n);
        ex = roots.get(ri);
        roots.release(mark);
        if (arr == 0) return;
        for (int i = 0; i < 2 * n; i++) heap.putIntElem(arr, i, buf[i]);
        heap.putRef(ex, thTrace, arr);
    }

    /** "\tat pkg.Class.method(File.java:line)" lines of a Throwable's recorded trace. */
    public String traceText(int ex) {
        int arr = heap.getRef(ex, thTrace);
        StringBuilder sb = new StringBuilder();
        if (arr == 0) return "";
        int n = heap.arrayLength(arr) / 2;
        for (int i = 0; i < n; i++) {
            RMethod m = linker.method(heap.getIntElem(arr, 2 * i));
            int pc = heap.getIntElem(arr, 2 * i + 1);
            int line = m.lineAt(pc);
            String file = m.owner.cf == null ? null : m.owner.cf.sourceFile;
            sb.append("\tat ").append(m.owner.name.replace('/', '.')).append('.').append(m.name).append('(');
            if (m.isNative) sb.append("Native Method");
            else if (file != null) sb.append(file).append(line >= 0 ? ":" + line : "");
            else sb.append("Unknown Source");
            sb.append(")\n");
        }
        return sb.toString();
    }

    /** Class name and message of a Throwable (no guest code is run). */
    public String describe(int ex) {
        int msg = heap.getRef(ex, thMessage);
        String n = className(ex);
        return msg == 0 ? n : n + ": " + string(msg);
    }

    public void reportUncaught(VmThread th, int ex) {
        host.log(3, "vm", "Uncaught exception in guest thread " + th.id + ": " + describe(ex) + "\n" + traceText(ex));
    }

    public int throwableMessageSlot() { return thMessage; }
    public int throwableTraceSlot() { return thTrace; }
    public List<VmThread> threadList() {
        List<VmThread> l = new ArrayList<VmThread>();
        for (VmThread t : sched.threads()) l.add(t);
        return l;
    }
}
