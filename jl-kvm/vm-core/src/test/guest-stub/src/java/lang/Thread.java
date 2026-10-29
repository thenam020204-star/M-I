package java.lang;

public class Thread implements Runnable {
    public static final int MIN_PRIORITY = 1, NORM_PRIORITY = 5, MAX_PRIORITY = 10;
    private Runnable target;
    private int vmId;
    private int priority = NORM_PRIORITY;
    public Thread() { }
    public Thread(Runnable target) { this.target = target; }
    public void run() { if (target != null) target.run(); }
    public synchronized void start() { start0(); }
    private native void start0();
    public static native Thread currentThread();
    public static native void sleep(long ms) throws InterruptedException;
    public static native void yield();
    public native void interrupt();
    public final native boolean isAlive();
    public static native int activeCount();
    private native void join0() throws InterruptedException;
    public final void join() throws InterruptedException { join0(); }
    public final int getPriority() { return priority; }
    public final void setPriority(int p) { if (p < MIN_PRIORITY || p > MAX_PRIORITY) throw new IllegalArgumentException(); priority = p; }
    public String toString() { return "Thread"; }
}
