/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.lang;

/** Root of the class hierarchy. hashCode/getClass/notify/notifyAll/wait are natives of the VM (PLAN D8). */
public class Object {
    public Object() {
    }

    public final native Class getClass();

    public native int hashCode();

    public boolean equals(Object obj) {
        return this == obj;
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    public final native void notify();

    public final native void notifyAll();

    /** Waits for a notify; a timeout of 0 waits forever. IllegalMonitorStateException when the caller does not own the monitor. */
    public final native void wait(long timeout) throws InterruptedException;

    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }
        if (nanos >= 500000 || (nanos != 0 && timeout == 0)) {
            timeout++;
        }
        wait(timeout);
    }

    public final void wait() throws InterruptedException {
        wait(0L);
    }
}
