package vmspec;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.midlet.MIDlet;

/**
 * T22a: java.util.Timer / TimerTask (MIDP 2.0): one-shot, fixed-delay vs fixed-rate (catch-up), cancel inside run(), cancel before run,
 * task that throws ends the timer, Timer.cancel, argument checks, Date arguments, ordering by time. Lines with "> " depend only on
 * order and on inequalities with a wide margin (the oracles run in real time); "# " lines carry the measured times (informational,
 * compared between deterministic runs by vm-core TimerTest).
 */
public class Tm01Timer extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static void i(String s) { System.out.println("# " + s); }

    static final Object lock = new Object();
    static void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException e) { } }

    static String catchName(Throwable t) {
        String n = t.getClass().getName();
        return n.substring(n.lastIndexOf('.') + 1);
    }

    /** schedules {@code t} through {@code s}, prints the exception class (or "ok"). */
    abstract static class Call { abstract void go() throws Exception; }
    static void expect(String what, Call c) {
        String r = "ok";
        try { c.go(); } catch (Throwable t) { r = catchName(t); }
        p(what + " -> " + r);
    }

    static class Rec extends TimerTask {
        final String name; final long sleepInRun; final int max; final Timer timer;
        int runs; long[] start = new long[16], sched = new long[16]; boolean cancelResult;
        Rec(String name, long sleepInRun, int max, Timer timer) { this.name = name; this.sleepInRun = sleepInRun; this.max = max; this.timer = timer; }
        public void run() {
            start[runs] = System.currentTimeMillis();
            sched[runs] = scheduledExecutionTime();
            runs++;
            if (sleepInRun > 0 && (runs == 1 || sleepInRun < 0)) sleep(sleepInRun);
            if (runs >= max) { cancelResult = cancel(); synchronized (lock) { lock.notifyAll(); } }
        }
        void await(long maxMs) {
            long end = System.currentTimeMillis() + maxMs;
            synchronized (lock) {
                while (runs < max && System.currentTimeMillis() < end) { try { lock.wait(50); } catch (InterruptedException e) { } }
            }
        }
    }

    protected void startApp() {
        long t0 = System.currentTimeMillis();

        // 1. one shot
        Timer tm = new Timer();
        Rec one = new Rec("one", 0, 1, tm);
        tm.schedule(one, 200);
        p("one-shot before: runs=" + one.runs);
        one.await(3000);
        sleep(50);
        p("one-shot after: runs=" + one.runs + " delayed>=150=" + (one.start[0] - t0 >= 150) + " scheduledTime==start(approx)=" + (Math.abs(one.sched[0] - one.start[0]) < 100));
        p("one-shot cancel after run returns " + one.cancel());
        i("one-shot start-t0=" + (one.start[0] - t0));
        tm.cancel();

        // 2. fixed-rate: periods stay on the grid, the first run (250 ms) makes the next ones catch up
        tm = new Timer();
        Rec fr = new Rec("rate", 250, 5, tm);
        long b = System.currentTimeMillis();
        tm.scheduleAtFixedRate(fr, 100, 100);
        fr.await(5000);
        long grid1 = fr.sched[1] - fr.sched[0], grid2 = fr.sched[2] - fr.sched[1], grid3 = fr.sched[3] - fr.sched[2];
        p("rate: scheduled times " + grid1 + " " + grid2 + " " + grid3 + " first=" + (fr.sched[0] - b >= 100 && fr.sched[0] - b < 160));
        p("rate: run 2 and 3 caught up (started within 90 ms of the previous): " + (fr.start[1] - (fr.start[0] + 250) < 90) + " " + (fr.start[2] - fr.start[1] < 90));
        p("rate: cancel in run returned " + fr.cancelResult);
        i("rate starts " + (fr.start[0] - b) + " " + (fr.start[1] - b) + " " + (fr.start[2] - b) + " " + (fr.start[3] - b) + " " + (fr.start[4] - b));
        tm.cancel();

        // 3. fixed-delay: a slow first run pushes everything behind it
        tm = new Timer();
        Rec fd = new Rec("delay", 250, 4, tm);
        b = System.currentTimeMillis();
        tm.schedule(fd, 100, 100);
        fd.await(5000);
        p("delay: next run is due period after the START of the previous one (already past after a 250 ms run, so it follows at once): gap " + (fd.start[1] - fd.start[0] >= 240 && fd.start[1] - fd.start[0] < 330));
        p("delay: gap start3-start2 >= 100: " + (fd.start[2] - fd.start[1] >= 100) + " " + (fd.start[3] - fd.start[2] >= 100));
        p("delay: scheduledExecutionTime == start (approx): " + (Math.abs(fd.sched[1] - fd.start[1]) < 100));
        i("delay starts " + (fd.start[0] - b) + " " + (fd.start[1] - b) + " " + (fd.start[2] - b) + " " + (fd.start[3] - b));
        tm.cancel();

        // 4. cancel before the run
        tm = new Timer();
        Rec c1 = new Rec("c1", 0, 1, tm), c2 = new Rec("c2", 0, 1, tm);
        tm.schedule(c1, 150);
        tm.schedule(c2, 250);
        p("cancel scheduled: " + c1.cancel() + " second cancel: " + c1.cancel());
        c2.await(3000);
        sleep(50);
        p("cancelled task ran=" + c1.runs + " other ran=" + c2.runs);
        final Timer tmc = tm;
        final Rec cc = c1;
        expect("schedule cancelled task", new Call() { void go() { tmc.schedule(cc, 10); } });
        final Rec tw = new Rec("tw", 0, 1, tm);
        expect("schedule task twice", new Call() { void go() { tmc.schedule(tw, 500); tmc.schedule(tw, 500); } });
        tm.cancel();

        // 5. Timer.cancel: pending tasks never run, later schedule fails
        tm = new Timer();
        Rec pend = new Rec("pend", 0, 1, tm);
        tm.schedule(pend, 200);
        tm.cancel();
        sleep(400);
        p("after Timer.cancel runs=" + pend.runs + " task cancel returns " + pend.cancel());
        final Timer dead = tm;
        expect("schedule on cancelled timer", new Call() { void go() { dead.schedule(new Rec("x", 0, 1, dead), 10); } });
        expect("scheduleAtFixedRate on cancelled timer", new Call() { void go() { dead.scheduleAtFixedRate(new Rec("x", 0, 1, dead), 10, 10); } });

        // 6. a task that throws ends the timer; the task behind it never runs
        tm = new Timer();
        final Rec behind = new Rec("behind", 0, 1, tm);
        tm.schedule(new TimerTask() { public void run() { throw new RuntimeException("task failure"); } }, 100);
        tm.schedule(behind, 300);
        sleep(600);
        p("task behind a throwing task ran=" + behind.runs);
        final Timer broken = tm;
        expect("schedule after a task threw", new Call() { void go() { broken.schedule(new Rec("y", 0, 1, broken), 10); } });

        // 7. argument checks
        final Timer ok = new Timer();
        expect("negative delay", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), -1); } });
        expect("negative delay (periodic)", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), -1, 10); } });
        expect("zero period", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), 0, 0); } });
        expect("negative period", new Call() { void go() { ok.scheduleAtFixedRate(new Rec("a", 0, 1, ok), 0, -5); } });
        expect("zero period (Date)", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), new Date(), 0); } });
        expect("negative time", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), new Date(-1)); } });
        expect("null task", new Call() { void go() { ok.schedule(null, 10); } });
        expect("null date", new Call() { void go() { ok.schedule(new Rec("a", 0, 1, ok), (Date) null); } });

        // 8. Date arguments: a time in the past runs at once; a time in the future waits
        Rec past = new Rec("past", 0, 1, ok), fut = new Rec("fut", 0, 1, ok);
        b = System.currentTimeMillis();
        ok.schedule(past, new Date(b - 5000));
        past.await(2000);
        p("past date ran=" + past.runs + " within 150ms=" + (past.start[0] - b < 150));
        ok.schedule(fut, new Date(System.currentTimeMillis() + 300));
        sleep(100);
        p("future date before=" + fut.runs);
        fut.await(3000);
        p("future date after=" + fut.runs);
        Rec dr = new Rec("dr", 0, 3, ok);
        ok.scheduleAtFixedRate(dr, new Date(System.currentTimeMillis() + 50), 80);
        dr.await(3000);
        p("Date fixed rate grid: " + (dr.sched[1] - dr.sched[0]) + " " + (dr.sched[2] - dr.sched[1]));

        // 9. tasks of different delays run in order of time, not of scheduling
        final StringBuffer order = new StringBuffer();
        ok.schedule(new TimerTask() { public void run() { order.append('c'); synchronized (lock) { lock.notifyAll(); } } }, 400);
        ok.schedule(new TimerTask() { public void run() { order.append('a'); } }, 100);
        ok.schedule(new TimerTask() { public void run() { order.append('b'); } }, 250);
        sleep(700);
        p("order of delays 400,100,250: " + order);

        // 10. two timers are independent: a long task in one does not delay the other
        Timer slow = new Timer(), quick = new Timer();
        final Rec q = new Rec("q", 0, 1, quick);
        slow.schedule(new TimerTask() { public void run() { sleep(600); } }, 10);
        quick.schedule(q, 100);
        q.await(400);
        p("independent timers: quick ran=" + q.runs);
        slow.cancel();
        quick.cancel();
        ok.cancel();
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
