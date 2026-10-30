package vmspec;

import javax.microedition.midlet.MIDlet;

/** T14: bounded buffer (capacity 3) with wait/notifyAll between producers and consumers; results independent of scheduling. */
public class P01Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static final class Buffer {
        private final int[] data = new int[3];
        private int head, count;
        private boolean closed;
        synchronized void put(int v) throws InterruptedException {
            while (count == data.length) wait();
            data[(head + count) % data.length] = v;
            count++;
            notifyAll();
        }
        /** -1 when closed and empty. */
        synchronized int take() throws InterruptedException {
            while (count == 0) {
                if (closed) return -1;
                wait();
            }
            int v = data[head];
            head = (head + 1) % data.length;
            count--;
            notifyAll();
            return v;
        }
        synchronized void close() { closed = true; notifyAll(); }
    }

    static int sum, taken;

    static synchronized void add(int v) { sum += v; taken++; }

    protected void startApp() {
        // one producer, one consumer: order is preserved
        final Buffer b = new Buffer();
        final StringBuffer seq = new StringBuffer();
        Thread prod = new Thread(new Runnable() { public void run() {
            try { for (int i = 1; i <= 12; i++) b.put(i); } catch (InterruptedException e) { }
            b.close();
        } });
        Thread cons = new Thread(new Runnable() { public void run() {
            try { for (int v; (v = b.take()) >= 0; ) seq.append(v).append(' '); } catch (InterruptedException e) { }
        } });
        cons.start(); prod.start();
        try { prod.join(); cons.join(); } catch (InterruptedException e) { }
        p("sequence " + seq.toString().trim());

        // three producers, two consumers: only the totals are scheduling independent
        final Buffer b2 = new Buffer();
        Thread[] ps = new Thread[3], cs = new Thread[2];
        for (int k = 0; k < ps.length; k++) {
            final int base = k * 100;
            ps[k] = new Thread(new Runnable() { public void run() {
                try { for (int i = 1; i <= 20; i++) b2.put(base + i); } catch (InterruptedException e) { }
            } });
        }
        for (int k = 0; k < cs.length; k++) {
            cs[k] = new Thread(new Runnable() { public void run() {
                try { for (int v; (v = b2.take()) >= 0; ) add(v); } catch (InterruptedException e) { }
            } });
        }
        for (int k = 0; k < cs.length; k++) cs[k].start();
        for (int k = 0; k < ps.length; k++) ps[k].start();
        try { for (int k = 0; k < ps.length; k++) ps[k].join(); } catch (InterruptedException e) { }
        b2.close();
        try { for (int k = 0; k < cs.length; k++) cs[k].join(); } catch (InterruptedException e) { }
        p("taken " + taken + " sum " + sum);

        // interrupt a consumer blocked in wait()
        final Buffer b3 = new Buffer();
        final String[] res = new String[1];
        Thread w = new Thread(new Runnable() { public void run() {
            try { b3.take(); res[0] = "took"; } catch (InterruptedException e) { res[0] = "interrupted"; }
        } });
        w.start();
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        w.interrupt();
        try { w.join(); } catch (InterruptedException e) { }
        p("blocked consumer " + res[0]);
        notifyDestroyed();
    }
    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
