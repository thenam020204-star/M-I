package jlkvm.interp;

import jlkvm.Vm;
import jlkvm.heap.Heap;
import jlkvm.heap.TempRoots;
import jlkvm.link.ClassInfo;
import jlkvm.link.RMethod;
import jlkvm.natives.Blocker;
import jlkvm.natives.Handles;
import jlkvm.natives.NativeContext;
import jlkvm.thread.VmThread;

/** The single NativeContext instance (natives run on the vm-thread only). Core natives may use the extra package-level fields. */
public final class NativeCtx implements NativeContext {
    final Vm vm;
    int[] st;
    int base;
    RMethod m;
    VmThread th;
    // results
    int rLo, rHi;
    // requests
    String exClass, exMsg;
    int exRef;
    Blocker blocker;
    boolean yield;
    /** Native asks the interpreter to push a frame of {@code callM} with one reference argument, result = callArg (PUSH_REF). */
    RMethod callM;
    int callArg;
    /** Native asks to initialise a class and re-execute the invoke afterwards. */
    ClassInfo initCls;
    int charge;

    NativeCtx(Vm vm) { this.vm = vm; }

    void begin(VmThread th, int[] st, int base, RMethod m) {
        this.th = th;
        this.st = st;
        this.base = base;
        this.m = m;
        rLo = rHi = 0;
        exClass = null; exMsg = null; exRef = 0;
        blocker = null; yield = false; callM = null; initCls = null; charge = 0;
    }

    public void chargeBytecodes(int n) { charge += n; }

    public int argInt(int i) { return st[base + m.argOffset[i]]; }
    public long argLong(int i) { int k = base + m.argOffset[i]; return ((long) st[k] << 32) | (st[k + 1] & 0xFFFFFFFFL); }
    public float argFloat(int i) { return Float.intBitsToFloat(argInt(i)); }
    public double argDouble(int i) { return Double.longBitsToDouble(argLong(i)); }
    public int argRef(int i) { return argInt(i); }
    public void retInt(int v) { rLo = v; }
    public void retLong(long v) { rHi = (int) (v >>> 32); rLo = (int) v; }
    public void retFloat(float v) { rLo = Float.floatToRawIntBits(v); }
    public void retDouble(double v) { retLong(Double.doubleToRawLongBits(v)); }
    public void retRef(int ref) { rLo = ref; }
    public void retVoid() { }
    public void throwNew(String guestClass, String msg) { exClass = guestClass; exMsg = msg; }
    public void block(Blocker b) { blocker = b; }
    public Heap heap() { return vm.heap; }
    public Handles handles() { return vm.handles; }
    public TempRoots roots() { return vm.roots; }
}
