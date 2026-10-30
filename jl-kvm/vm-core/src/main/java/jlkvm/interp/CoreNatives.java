package jlkvm.interp;

import jlkvm.Vm;
import jlkvm.heap.Heap;
import jlkvm.link.ClassInfo;
import jlkvm.link.Errors;
import jlkvm.link.RMethod;
import jlkvm.link.VmThrow;
import jlkvm.natives.NativeContext;
import jlkvm.natives.NativeMethod;
import jlkvm.natives.NativeRegistry;
import jlkvm.thread.VmThread;

/** Natives of the VM itself (Object, Class, Thread, System/Runtime basics, Throwable, bit conversions, Math). Not host dependent. */
public final class CoreNatives {
    private CoreNatives() {}

    private static NativeCtx x(NativeContext c) { return (NativeCtx) c; }

    public static void register(final Vm vm) {
        NativeRegistry r = vm.registry;
        final Heap heap = vm.heap;

        r.register("java/lang/Object.hashCode()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(heap.identityHash(c.argRef(0))); } });
        r.register("java/lang/Object.getClass()Ljava/lang/Class;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(vm.linker.mirror(vm.linker.byId(heap.classId(c.argRef(0))))); } });
        r.register("java/lang/Object.notify()V", new NativeMethod() { public void invoke(NativeContext c) {
            vm.monitors.notify(x(c).th, c.argRef(0), false); } });
        r.register("java/lang/Object.notifyAll()V", new NativeMethod() { public void invoke(NativeContext c) {
            vm.monitors.notify(x(c).th, c.argRef(0), true); } });
        r.register("java/lang/Object.wait(J)V", new NativeMethod() { public void invoke(NativeContext c) {
            long ms = c.argLong(1);
            if (ms < 0) { c.throwNew("java/lang/IllegalArgumentException", "timeout value is negative"); return; }
            vm.monitors.await(x(c).th, c.argRef(0), ms); } });

        // ---- Class
        r.register("java/lang/Class.getName()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            ClassInfo k = vm.linker.fromMirror(c.argRef(0));
            c.retRef(vm.newString(k.name.replace('/', '.'))); } });
        r.register("java/lang/Class.forName(Ljava/lang/String;)Ljava/lang/Class;", new NativeMethod() { public void invoke(NativeContext c) {
            String n = vm.string(c.argRef(0));
            if (n == null) { c.throwNew("java/lang/NullPointerException", null); return; }
            ClassInfo k;
            try {
                k = vm.linker.find(n.replace('.', '/'));
            } catch (VmThrow t) {
                // only the requested class itself missing is a ClassNotFoundException; a missing superclass stays a NoClassDefFoundError
                if (t.guestClass != null && t.guestClass.equals("java/lang/NoClassDefFoundError") && n.replace('.', '/').equals(t.msg)) c.throwNew("java/lang/ClassNotFoundException", n);
                else throw t;
                return;
            }
            VmThread th = x(c).th;
            if (k.state != ClassInfo.INITIALIZED && !(k.state == ClassInfo.INITIALIZING && k.initThread == th.id)) { x(c).initCls = k; return; }
            c.retRef(vm.linker.mirror(k)); } });
        r.register("java/lang/Class.newInstance()Ljava/lang/Object;", new NativeMethod() { public void invoke(NativeContext c) {
            ClassInfo k = vm.linker.fromMirror(c.argRef(0));
            if (k.isInterface || k.isArray || (k.access & 0x400) != 0) { c.throwNew("java/lang/InstantiationException", k.name); return; }
            VmThread th = x(c).th;
            if (k.state != ClassInfo.INITIALIZED && !(k.state == ClassInfo.INITIALIZING && k.initThread == th.id)) { x(c).initCls = k; return; }
            RMethod init = k.findDeclared("<init>", "()V");
            if (init == null) { c.throwNew("java/lang/InstantiationException", k.name); return; }
            if ((init.access & 0x2) != 0) { c.throwNew("java/lang/IllegalAccessException", k.name); return; }
            int o = heap.allocObject(k.id);
            if (o == 0) throw vm.oomThrow(k.name);
            x(c).callM = init;
            x(c).callArg = o; } });
        r.register("java/lang/Class.isInstance(Ljava/lang/Object;)Z", new NativeMethod() { public void invoke(NativeContext c) {
            int o = c.argRef(1);
            c.retInt(o != 0 && vm.linker.isAssignable(vm.linker.byId(heap.classId(o)), vm.linker.fromMirror(c.argRef(0))) ? 1 : 0); } });
        r.register("java/lang/Class.isAssignableFrom(Ljava/lang/Class;)Z", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(vm.linker.isAssignable(vm.linker.fromMirror(c.argRef(1)), vm.linker.fromMirror(c.argRef(0))) ? 1 : 0); } });
        r.register("java/lang/Class.isInterface()Z", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(vm.linker.fromMirror(c.argRef(0)).isInterface ? 1 : 0); } });
        r.register("java/lang/Class.isArray()Z", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(vm.linker.fromMirror(c.argRef(0)).isArray ? 1 : 0); } });
        r.register("java/lang/Class.resourceBytes(Ljava/lang/String;)[B", new NativeMethod() { public void invoke(NativeContext c) {
            String p = vm.string(c.argRef(0));
            byte[] b = vm.app == null ? null : vm.app.readResource(p);
            if (b == null) b = vm.host.rom().readResource(p);
            c.retRef(b == null ? 0 : vm.newByteArray(b, 0, b.length)); } });

        // ---- Thread
        r.register("java/lang/Thread.start0()V", new NativeMethod() { public void invoke(NativeContext c) {
            vm.startThread(c.argRef(0)); } });
        r.register("java/lang/Thread.sleep(J)V", new NativeMethod() { public void invoke(NativeContext c) {
            long ms = c.argLong(0);
            VmThread th = x(c).th;
            if (ms < 0) { c.throwNew("java/lang/IllegalArgumentException", "timeout value is negative"); return; }
            if (th.interrupted) { th.interrupted = false; c.throwNew("java/lang/InterruptedException", null); return; }
            if (ms == 0) { x(c).yield = true; return; }
            th.state = VmThread.SLEEPING;
            vm.sched.addTimed(th, vm.nanoTime() + ms * 1000000L); } });
        r.register("java/lang/Thread.setPriority0(I)V", new NativeMethod() { public void invoke(NativeContext c) {
            VmThread t = vm.sched.thread(heap.getInt(c.argRef(0), vm.threadVmIdSlot()));
            if (t != null) vm.sched.setPriority(t, c.argInt(1)); } });
        r.register("java/lang/Thread.yield()V", new NativeMethod() { public void invoke(NativeContext c) { x(c).yield = true; } });
        r.register("java/lang/Thread.currentThread()Ljava/lang/Thread;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(x(c).th.threadObj); } });
        r.register("java/lang/Thread.interrupt()V", new NativeMethod() { public void invoke(NativeContext c) {
            VmThread t = vm.sched.thread(heap.getInt(c.argRef(0), vm.threadVmIdSlot()));
            if (t != null) vm.monitors.interrupt(t); } });
        r.register("java/lang/Thread.isAlive()Z", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(vm.sched.thread(heap.getInt(c.argRef(0), vm.threadVmIdSlot())) != null ? 1 : 0); } });
        r.register("java/lang/Thread.join0()V", new NativeMethod() { public void invoke(NativeContext c) {
            VmThread th = x(c).th;
            VmThread target = vm.sched.thread(heap.getInt(c.argRef(0), vm.threadVmIdSlot()));
            if (th.interrupted) { th.interrupted = false; c.throwNew("java/lang/InterruptedException", null); return; }
            if (target == null || target == th) return;
            target.joiners.add(th);
            th.joinTarget = target;
            th.state = VmThread.WAITING; } });
        r.register("java/lang/Thread.activeCount()I", new NativeMethod() { public void invoke(NativeContext c) {
            c.retInt(vm.sched.liveCount()); } });

        // ---- System / Runtime / String
        r.register("java/lang/System.arraycopy(Ljava/lang/Object;ILjava/lang/Object;II)V", new NativeMethod() { public void invoke(NativeContext c) {
            arraycopy(vm, c.argRef(0), c.argInt(1), c.argRef(2), c.argInt(3), c.argInt(4)); } });
        r.register("java/lang/System.identityHashCode(Ljava/lang/Object;)I", new NativeMethod() { public void invoke(NativeContext c) {
            int o = c.argRef(0);
            c.retInt(o == 0 ? 0 : heap.identityHash(o)); } });
        r.register("java/lang/System.currentTimeMillis()J", new NativeMethod() { public void invoke(NativeContext c) {
            c.retLong(vm.currentTimeMillis()); } });
        r.register("java/lang/System.getProperty0(Ljava/lang/String;)Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            String v = vm.profile.properties.get(vm.string(c.argRef(0)));
            c.retRef(v == null ? 0 : vm.newString(v)); } });
        r.register("java/lang/Runtime.freeMemory()J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(heap.freeMemory()); } });
        r.register("java/lang/Runtime.totalMemory()J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(heap.totalMemory()); } });
        r.register("java/lang/Runtime.gc()V", new NativeMethod() { public void invoke(NativeContext c) { vm.collectGarbage(); } });
        r.register("java/lang/Runtime.exit(I)V", new NativeMethod() { public void invoke(NativeContext c) { vm.requestExit(); } });
        r.register("java/lang/String.intern()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(vm.internExisting(c.argRef(0))); } });

        // ---- Throwable
        r.register("java/lang/Throwable.fillInStackTrace()V", new NativeMethod() { public void invoke(NativeContext c) {
            vm.fillTrace(c.argRef(0), x(c).th, true); } });
        r.register("java/lang/Throwable.traceText()Ljava/lang/String;", new NativeMethod() { public void invoke(NativeContext c) {
            c.retRef(vm.newString(vm.traceText(c.argRef(0)))); } });

        // ---- bit conversions, Math
        r.register("java/lang/Float.floatToIntBits(F)I", new NativeMethod() { public void invoke(NativeContext c) { c.retInt(Float.floatToIntBits(c.argFloat(0))); } });
        r.register("java/lang/Float.intBitsToFloat(I)F", new NativeMethod() { public void invoke(NativeContext c) { c.retFloat(Float.intBitsToFloat(c.argInt(0))); } });
        r.register("java/lang/Double.doubleToLongBits(D)J", new NativeMethod() { public void invoke(NativeContext c) { c.retLong(Double.doubleToLongBits(c.argDouble(0))); } });
        r.register("java/lang/Double.longBitsToDouble(J)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(Double.longBitsToDouble(c.argLong(0))); } });
        r.register("java/lang/Math.sin(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.sin(c.argDouble(0))); } });
        r.register("java/lang/Math.cos(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.cos(c.argDouble(0))); } });
        r.register("java/lang/Math.tan(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.tan(c.argDouble(0))); } });
        r.register("java/lang/Math.sqrt(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.sqrt(c.argDouble(0))); } });
        r.register("java/lang/Math.ceil(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.ceil(c.argDouble(0))); } });
        r.register("java/lang/Math.floor(D)D", new NativeMethod() { public void invoke(NativeContext c) { c.retDouble(StrictMath.floor(c.argDouble(0))); } });
    }

    /** System.arraycopy semantics: NPE, ArrayStoreException, ArrayIndexOutOfBoundsException; overlap-safe. */
    static void arraycopy(Vm vm, int src, int sp, int dst, int dp, int len) {
        Heap h = vm.heap;
        if (src == 0 || dst == 0) throw new VmThrow("java/lang/NullPointerException", null);
        if (!h.isArray(src) || !h.isArray(dst)) throw new VmThrow("java/lang/ArrayStoreException", "not an array");
        int kind = h.kind(src);
        if (kind != h.kind(dst)) throw new VmThrow("java/lang/ArrayStoreException", "type mismatch");
        if (sp < 0 || dp < 0 || len < 0 || (long) sp + len > h.arrayLength(src) || (long) dp + len > h.arrayLength(dst))
            throw new VmThrow("java/lang/ArrayIndexOutOfBoundsException", "arraycopy: out of bounds");
        if (len == 0) return;
        int[] mem = h.mem;
        switch (kind) {
            case Heap.K_REF: {
                ClassInfo sc = vm.linker.byId(h.classId(src)), dc = vm.linker.byId(h.classId(dst));
                boolean check = !vm.linker.isAssignable(sc, dc);
                if (check || src == dst) {
                    int[] tmp = new int[len];
                    for (int i = 0; i < len; i++) tmp[i] = h.getRefElem(src, sp + i);
                    for (int i = 0; i < len; i++) {
                        int v = tmp[i];
                        if (check && v != 0 && !vm.linker.isAssignable(vm.linker.byId(h.classId(v)), dc.component))
                            throw new VmThrow("java/lang/ArrayStoreException", vm.linker.byId(h.classId(v)).name);
                        h.putRefElem(dst, dp + i, v);
                    }
                } else System.arraycopy(mem, src + Heap.ARRAY_HEADER + sp, mem, dst + Heap.ARRAY_HEADER + dp, len);
                break;
            }
            case Heap.K_INT: case Heap.K_FLOAT:
                System.arraycopy(mem, src + Heap.ARRAY_HEADER + sp, mem, dst + Heap.ARRAY_HEADER + dp, len);
                break;
            case Heap.K_LONG: case Heap.K_DOUBLE:
                System.arraycopy(mem, src + Heap.ARRAY_HEADER + 2 * sp, mem, dst + Heap.ARRAY_HEADER + 2 * dp, 2 * len);
                break;
            case Heap.K_CHAR: case Heap.K_SHORT:
                if (src == dst && sp < dp) for (int i = len - 1; i >= 0; i--) h.putChar(dst, dp + i, h.getChar(src, sp + i));
                else for (int i = 0; i < len; i++) h.putChar(dst, dp + i, h.getChar(src, sp + i));
                break;
            default:
                if (src == dst && sp < dp) for (int i = len - 1; i >= 0; i--) h.putByte(dst, dp + i, h.getByte(src, sp + i));
                else for (int i = 0; i < len; i++) h.putByte(dst, dp + i, h.getByte(src, sp + i));
                break;
        }
    }
}
