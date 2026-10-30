/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12);
 * behaviour per the MIDP 2.0 / J2SE javadoc of java.util.TimerTask. License: Apache-2.0.
 */
package java.util;

public abstract class TimerTask implements Runnable {
    static final int VIRGIN = 0, SCHEDULED = 1, EXECUTED = 2, CANCELLED = 3;

    /** Guards state / nextExecutionTime / period (task code never takes it). */
    final Object lock = new Object();
    int state = VIRGIN;
    long nextExecutionTime;
    /** 0 = one shot, &gt; 0 = fixed rate, &lt; 0 = fixed delay (|period| is the interval). */
    long period;

    protected TimerTask() {
    }

    public abstract void run();

    public boolean cancel() {
        synchronized (lock) {
            boolean result = state == SCHEDULED;
            state = CANCELLED;
            return result;
        }
    }

    public long scheduledExecutionTime() {
        synchronized (lock) {
            return period < 0 ? nextExecutionTime + period : nextExecutionTime - period;
        }
    }
}
