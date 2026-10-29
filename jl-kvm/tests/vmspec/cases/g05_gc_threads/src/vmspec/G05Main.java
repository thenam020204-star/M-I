package vmspec;

import javax.microedition.midlet.MIDlet;

/**
 * T13: collections while several green threads are parked in different states (sleep, wait, blocked on a monitor, preempted)
 * and hold references in their frames, locals, operand stacks and monitors. Producers and consumers allocate heavily; the
 * totals are printed after join, so the output does not depend on scheduling.
 */
public class G05Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final Object lock = new Object();
    static Object[] queue = new Object[8];
    static int head, tail, count;
    static volatile boolean done;

    static void put(Object o) throws InterruptedException {
        synchronized (lock) {
            while (count == queue.length) lock.wait();
            queue[tail] = o;
            tail = (tail + 1) % queue.length;
            count++;
            lock.notifyAll();
        }
    }

    static Object take() throws InterruptedException {
        synchronized (lock) {
            while (count == 0) lock.wait();
            Object o = queue[head];
            queue[head] = null;
            head = (head + 1) % queue.length;
            count--;
            lock.notifyAll();
            return o;
        }
    }

    static final class Cell { Cell next; int[] data; int v; }

    static long sumP, sumC;
    static int sleeperOk, waiterOk, blockedOk;

    static class Producer extends Thread {
        final int id;
        Producer(int id) { this.id = id; }
        public void run() {
            try {
                for (int i = 0; i < 3000; i++) {
                    Cell c = new Cell();
                    c.v = id * 100000 + i;
                    c.data = new int[1 + (i & 7)];
                    c.data[0] = c.v;
                    c.next = new Cell();               // garbage-ish companion
                    synchronized (lock) { sumP += c.v; }
                    put(c);
                }
            } catch (InterruptedException e) { }
        }
    }

    static class Consumer extends Thread {
        final int n;
        Consumer(int n) { this.n = n; }
        public void run() {
            try {
                for (int i = 0; i < n; i++) {
                    Cell c = (Cell) take();
                    if (c.data[0] != c.v) p("CORRUPT");
                    synchronized (lock) { sumC += c.v; }
                }
            } catch (InterruptedException e) { }
        }
    }

    static class Sleeper extends Thread {
        public void run() {
            Cell mine = new Cell();                    // referenced only from this frame while the thread sleeps
            mine.data = new int[] { 4, 5, 6 };
            mine.next = new Cell();
            mine.next.v = 77;
            String s = "sl" + "eeper";
            try { Thread.sleep(400); } catch (InterruptedException e) { }
            if (mine.data[2] == 6 && mine.next.v == 77 && s.equals("sleeper")) sleeperOk = 1;
        }
    }

    static class Waiter extends Thread {
        public void run() {
            Cell mine = new Cell();
            mine.data = new int[] { 9 };
            synchronized (this) {
                try { wait(); } catch (InterruptedException e) { }
                if (mine.data[0] == 9) waiterOk = 1;
            }
        }
    }

    static class Blocked extends Thread {
        final Object m;
        Blocked(Object m) { this.m = m; }
        public void run() {
            Cell mine = new Cell();
            mine.v = 5;
            synchronized (m) {                         // main holds m for a while: this thread is blocked entering
                if (mine.v == 5) blockedOk = 1;
            }
        }
    }

    protected void startApp() {
        Sleeper sl = new Sleeper();
        Waiter wa = new Waiter();
        Object gate = new int[] { 1, 2 };
        Blocked bl = new Blocked(gate);
        Producer[] ps = new Producer[3];
        Consumer[] cs = new Consumer[2];
        synchronized (gate) {
            sl.start(); wa.start(); bl.start();
            for (int i = 0; i < ps.length; i++) { ps[i] = new Producer(i + 1); ps[i].start(); }
            cs[0] = new Consumer(4500); cs[1] = new Consumer(4500);
            cs[0].start(); cs[1].start();
            for (int i = 0; i < 20; i++) {
                for (int k = 0; k < 2000; k++) { int[] g = new int[16]; g[0] = k; }
                Runtime.getRuntime().gc();
                try { Thread.sleep(5); } catch (InterruptedException e) { }
            }
        }
        try {
            for (int i = 0; i < ps.length; i++) ps[i].join();
            for (int i = 0; i < cs.length; i++) cs[i].join();
            sl.join();
            Runtime.getRuntime().gc();
            synchronized (wa) { wa.notify(); }
            wa.join();
            bl.join();
        } catch (InterruptedException e) { }
        p("produced == consumed: " + (sumP == sumC) + " sum=" + sumP);
        p("sleeper kept its locals: " + sleeperOk);
        p("waiter kept its locals: " + waiterOk);
        p("blocked thread kept its locals: " + blockedOk);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
