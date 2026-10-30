/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12);
 * behaviour per the MIDP 2.0 / J2SE javadoc of java.util.Timer (D9: no purge(), no daemon / name constructors in MIDP).
 */
package java.util;

/**
 * One guest (green) thread executes the tasks one after another, in order of their execution time, against System.currentTimeMillis()
 * (the virtual clock in deterministic mode, D14). A task that throws ends the timer thread (the exception is the thread's uncaught
 * exception) and every later schedule call throws IllegalStateException, as for a cancelled timer.
 */
public class Timer {
    private final Vector queue = new Vector();   // TimerTask, ascending nextExecutionTime, FIFO among equals
    private boolean newTasksMayBeScheduled = true;

    public Timer() {
        new Thread() {
            public void run() {
                try {
                    mainLoop();
                } finally {
                    synchronized (queue) {
                        newTasksMayBeScheduled = false;
                        queue.removeAllElements();
                    }
                }
            }
        }.start();
    }

    public void schedule(TimerTask task, long delay) {
        if (delay < 0) throw new IllegalArgumentException("Negative delay.");
        sched(task, System.currentTimeMillis() + delay, 0);
    }

    public void schedule(TimerTask task, Date time) {
        sched(task, time.getTime(), 0);
    }

    public void schedule(TimerTask task, long delay, long period) {
        if (delay < 0) throw new IllegalArgumentException("Negative delay.");
        if (period <= 0) throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, -period);
    }

    public void schedule(TimerTask task, Date firstTime, long period) {
        if (period <= 0) throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), -period);
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (delay < 0) throw new IllegalArgumentException("Negative delay.");
        if (period <= 0) throw new IllegalArgumentException("Non-positive period.");
        sched(task, System.currentTimeMillis() + delay, period);
    }

    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        if (period <= 0) throw new IllegalArgumentException("Non-positive period.");
        sched(task, firstTime.getTime(), period);
    }

    public void cancel() {
        synchronized (queue) {
            newTasksMayBeScheduled = false;
            queue.removeAllElements();
            queue.notify();
        }
    }

    private void sched(TimerTask task, long time, long period) {
        if (task == null) throw new NullPointerException();
        if (time < 0) throw new IllegalArgumentException("Illegal execution time.");
        synchronized (queue) {
            if (!newTasksMayBeScheduled) throw new IllegalStateException("Timer already cancelled.");
            synchronized (task.lock) {
                if (task.state != TimerTask.VIRGIN) throw new IllegalStateException("Task already scheduled or cancelled");
                task.nextExecutionTime = time;
                task.period = period;
                task.state = TimerTask.SCHEDULED;
            }
            insert(task);
            if (queue.elementAt(0) == task) queue.notify();
        }
    }

    /** queue lock held: sorted insert behind the tasks with the same time. */
    private void insert(TimerTask task) {
        int i = queue.size();
        while (i > 0 && ((TimerTask) queue.elementAt(i - 1)).nextExecutionTime > task.nextExecutionTime) i--;
        queue.insertElementAt(task, i);
    }

    private void mainLoop() {
        while (true) {
            TimerTask task;
            boolean fired;
            synchronized (queue) {
                while (queue.isEmpty() && newTasksMayBeScheduled) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (queue.isEmpty()) break;   // cancelled and nothing left
                long currentTime, executionTime;
                task = (TimerTask) queue.elementAt(0);
                synchronized (task.lock) {
                    if (task.state == TimerTask.CANCELLED) {
                        queue.removeElementAt(0);
                        continue;
                    }
                    currentTime = System.currentTimeMillis();
                    executionTime = task.nextExecutionTime;
                    fired = executionTime <= currentTime;
                    if (fired) {
                        queue.removeElementAt(0);
                        if (task.period == 0) {
                            task.state = TimerTask.EXECUTED;
                        } else {
                            task.nextExecutionTime = task.period < 0 ? currentTime - task.period : executionTime + task.period;
                            insert(task);
                        }
                    }
                }
                if (!fired) {
                    try {
                        queue.wait(executionTime - currentTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (fired) task.run();
        }
    }
}
