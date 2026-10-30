/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/**
 * Green thread (PLAN D5). The VM keeps the scheduler state; this class holds vmId (scheduler thread id, 0 = never started),
 * priority (read by the VM at start) and the natives.
 */
public class Thread implements Runnable {
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;

    private static int threadInitNumber;

    private Runnable target;
    private int vmId;
    private int priority = NORM_PRIORITY;
    private String name;

    public Thread() {
        this(null, null);
    }

    public Thread(Runnable target) {
        this(target, null);
    }

    public Thread(String name) {
        this(null, name);
    }

    public Thread(Runnable target, String name) {
        if (name == null) {
            name = nextName();
        }
        this.target = target;
        this.name = name;
        Thread cur = currentThread();
        if (cur != null) {
            int p = cur.priority;
            this.priority = p < MIN_PRIORITY || p > MAX_PRIORITY ? NORM_PRIORITY : p;
        }
    }

    private static synchronized String nextName() {
        return "Thread-" + threadInitNumber++;
    }

    public static native Thread currentThread();

    public static native void yield();

    public static native void sleep(long millis) throws InterruptedException;

    public static native int activeCount();

    public void run() {
        if (target != null) {
            target.run();
        }
    }

    public synchronized void start() {
        start0();
    }

    private native void start0();

    public native void interrupt();

    public final native boolean isAlive();

    private native void join0() throws InterruptedException;

    public final void join() throws InterruptedException {
        join0();
    }

    public final int getPriority() {
        return priority;
    }

    public final void setPriority(int newPriority) {
        if (newPriority < MIN_PRIORITY || newPriority > MAX_PRIORITY) {
            throw new IllegalArgumentException();
        }
        priority = newPriority;
        setPriority0(newPriority);
    }

    private native void setPriority0(int newPriority);

    public final String getName() {
        return name;
    }

    public String toString() {
        return "Thread[" + name + "," + priority + "]";
    }
}
