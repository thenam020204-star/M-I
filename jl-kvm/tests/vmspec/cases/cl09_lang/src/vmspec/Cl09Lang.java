package vmspec;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import javax.microedition.midlet.MIDlet;

public class Cl09Lang extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String ex(Runnable r) {
        try { r.run(); return "no-exception"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    public static class Plain { public Plain() { } }
    public static class NoDefault { public NoDefault(int x) { } }
    static class PrivCtor { private PrivCtor() { } }
    public static abstract class Abs { }
    public interface Iface { }
    public static class Impl implements Iface { }

    static final Object lock = new Object();
    static int counter;

    protected void startApp() {
        // System.arraycopy
        int[] a = {1, 2, 3, 4, 5};
        System.arraycopy(a, 0, a, 1, 4);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) { sb.append(a[i]); }
        System.arraycopy(a, 1, a, 0, 4);
        for (int i = 0; i < a.length; i++) { sb.append(a[i]); }
        p("arraycopy overlap " + sb);
        p("arraycopy ex " + ex(new Runnable() { public void run() { System.arraycopy(new int[2], 0, new int[2], 1, 2); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy(new int[2], -1, new int[2], 0, 1); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy(new int[2], 0, new int[2], 0, -1); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy(null, 0, new int[2], 0, 1); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy(new int[2], 0, new long[2], 0, 1); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy("s", 0, new int[2], 0, 1); } }) + " " + ex(new Runnable() { public void run() { System.arraycopy(new Object[1], 0, new String[1], 0, 0); } }));
        Object[] src = {"a", new Integer(1)};
        try { System.arraycopy(src, 0, new String[2], 0, 2); p("arraycopy store none"); } catch (ArrayStoreException e) { p("arraycopy store ArrayStoreException"); }
        // Class
        try {
            Class c = Class.forName("vmspec.Cl09Lang$Plain");
            Object o = c.newInstance();
            p("class " + c.getName() + " " + (o instanceof Plain) + " " + c.isInstance(o) + " " + c.isInstance("x") + " " + c.isInterface() + " " + c.isArray() + " " + (o.getClass() == c) + " " + (Class.forName("vmspec.Cl09Lang$Plain") == c));
            p("class2 " + Class.forName("vmspec.Cl09Lang$Iface").isInterface() + " " + Iface.class.isAssignableFrom(Impl.class) + " " + Impl.class.isAssignableFrom(Iface.class) + " " + Object.class.isAssignableFrom(Plain.class) + " " + Plain.class.isAssignableFrom(Plain.class) + " " + Class.forName("[I").isArray() + " " + Class.forName("[I").getName() + " " + Class.forName("[Ljava.lang.String;").getName() + " " + new int[0][0].getClass().getName() + " " + "x".getClass().getName() + " " + String.class.getName() + " " + int[].class.getName());
        } catch (Exception e) { p("unexpected " + e); }
        try { Class.forName("vmspec.Nope"); p("forName none"); } catch (ClassNotFoundException e) { p("forName ClassNotFoundException"); }
        try { Class.forName(null); p("forName null none"); } catch (Exception e) { p("forName null " + e.getClass().getName()); }
        try { Class.forName("vmspec.Cl09Lang$NoDefault").newInstance(); p("newInstance none"); } catch (Exception e) { p("newInstance nodefault " + e.getClass().getName()); }
        try { Class.forName("vmspec.Cl09Lang$Abs").newInstance(); p("newInstance none"); } catch (Exception e) { p("newInstance abstract " + e.getClass().getName()); }
        try { Class.forName("vmspec.Cl09Lang$Iface").newInstance(); p("newInstance none"); } catch (Exception e) { p("newInstance interface " + e.getClass().getName()); }
        try { Class.forName("vmspec.Cl09Lang$PrivCtor").newInstance(); p("newInstance none"); } catch (Exception e) { p("newInstance private " + e.getClass().getName()); }
        // resources
        try {
            InputStream r = Cl09Lang.class.getResourceAsStream("data.txt");
            p("resource relative " + (r != null ? "" + r.read() : "null"));
            r = Cl09Lang.class.getResourceAsStream("/rootdata.txt");
            p("resource absolute " + (r != null ? "" + r.read() : "null"));
            p("resource missing " + (Cl09Lang.class.getResourceAsStream("/nothing.txt") == null) + " " + (Cl09Lang.class.getResourceAsStream("rootdata.txt") == null) + " " + (Cl09Lang.class.getResourceAsStream("/vmspec/data.txt") != null));
        } catch (Exception e) { p("unexpected " + e); }
        // Throwable
        Throwable t = new Throwable("msg");
        p("throwable " + t.getMessage() + " " + t + " " + new Throwable().getMessage() + " " + new Exception().toString() + " " + new Exception("m") + " " + new RuntimeException("r").getMessage() + " " + new Error("e"));
        p("exceptions " + new IllegalArgumentException() + " " + new ArrayIndexOutOfBoundsException(3).getMessage() + " " + new StringIndexOutOfBoundsException(4).getMessage() + " " + new NumberFormatException("n") + " " + new IndexOutOfBoundsException("i"));
        p("exception types " + (new ArrayIndexOutOfBoundsException() instanceof IndexOutOfBoundsException) + (new StringIndexOutOfBoundsException() instanceof IndexOutOfBoundsException) + (new NumberFormatException() instanceof IllegalArgumentException) + (new IllegalThreadStateException() instanceof IllegalArgumentException) + (new ClassNotFoundException() instanceof Exception) + (new OutOfMemoryError() instanceof Error) + (new InterruptedException() instanceof Exception) + (new IllegalMonitorStateException() instanceof RuntimeException) + (new NegativeArraySizeException() instanceof RuntimeException) + (new ArithmeticException() instanceof RuntimeException) + (new SecurityException() instanceof RuntimeException) + (new ArrayStoreException() instanceof RuntimeException) + (new ClassCastException() instanceof RuntimeException) + (new NullPointerException() instanceof RuntimeException) + (new InstantiationException() instanceof Exception) + (new IllegalAccessException() instanceof Exception));
        // WeakReference
        Object strong = new Object();
        WeakReference wr = new WeakReference(strong);
        p("weak get " + (wr.get() == strong));
        wr.clear();
        p("weak clear " + (wr.get() == null));
        WeakReference w2 = new WeakReference(new Object());
        for (int i = 0; i < 3; i++) { System.gc(); }
        p("weak collected " + (w2.get() == null));
        // Thread
        Thread cur = Thread.currentThread();
        p("main thread " + cur.isAlive() + " " + cur.getPriority() + " " + (cur.getName() != null));
        Thread th = new Thread(new Runnable() { public void run() { synchronized (lock) { counter++; } } }, "worker");
        p("thread new " + th.isAlive() + " " + th.getName() + " " + th.getPriority());
        th.setPriority(Thread.MAX_PRIORITY);
        p("thread prio " + th.getPriority() + " " + ex(new Runnable() { public void run() { new Thread().setPriority(0); } }) + " " + ex(new Runnable() { public void run() { new Thread().setPriority(11); } }));
        th.start();
        try { th.join(); } catch (InterruptedException e) { }
        p("thread joined " + th.isAlive() + " " + counter);
        try { th.start(); p("restart none"); } catch (IllegalThreadStateException e) { p("restart IllegalThreadStateException"); } catch (Throwable e) { p("restart " + e.getClass().getName()); }
        final Thread[] holder = new Thread[1];
        Thread sl = new Thread() { public void run() { try { Thread.sleep(100000); p("sleep no interrupt"); } catch (InterruptedException e) { p("sleeper interrupted"); } } };
        sl.start();
        try { Thread.sleep(50); } catch (InterruptedException e) { }
        sl.interrupt();
        try { sl.join(); } catch (InterruptedException e) { }
        p("sleeper dead " + !sl.isAlive());
        Thread sub = new Thread("sub") { public void run() { p("subclass run " + getName() + " " + (Thread.currentThread() == this)); } };
        sub.start();
        try { sub.join(); } catch (InterruptedException e) { }
        p("Thread consts " + Thread.MIN_PRIORITY + Thread.NORM_PRIORITY + Thread.MAX_PRIORITY);
        p("sleep ex " + ex(new Runnable() { public void run() { try { Thread.sleep(-1); } catch (InterruptedException e) { } } }));
        // wait/notify
        final int[] state = new int[1];
        Thread waiter = new Thread() { public void run() { synchronized (lock) { while (state[0] == 0) { try { lock.wait(); } catch (InterruptedException e) { } } p("waiter woke " + state[0]); } } };
        waiter.start();
        Thread.yield();
        synchronized (lock) { state[0] = 7; lock.notifyAll(); }
        try { waiter.join(); } catch (InterruptedException e) { }
        p("wait ex " + ex(new Runnable() { public void run() { try { lock.wait(); } catch (InterruptedException e) { } } }) + " " + ex(new Runnable() { public void run() { lock.notify(); } }) + " " + ex(new Runnable() { public void run() { synchronized (lock) { try { lock.wait(-1); } catch (InterruptedException e) { } } } }));
        long t0 = System.currentTimeMillis();
        synchronized (lock) { try { lock.wait(60); } catch (InterruptedException e) { } }
        p("timed wait " + (System.currentTimeMillis() - t0 >= 50));
        // Object / Runtime
        Object o1 = new Object(), o2 = new Object();
        p("object " + o1.equals(o1) + o1.equals(o2) + (o1.hashCode() == o1.hashCode()) + (System.identityHashCode(o1) == o1.hashCode()) + " " + o1.toString().startsWith("java.lang.Object@") + " " + (o1.getClass() == Object.class) + " " + new int[0].getClass().getName());
        p("runtime " + (Runtime.getRuntime().totalMemory() > 0) + (Runtime.getRuntime().freeMemory() > 0) + (Runtime.getRuntime().freeMemory() <= Runtime.getRuntime().totalMemory()) + " " + (System.currentTimeMillis() > 1000000000000L));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
