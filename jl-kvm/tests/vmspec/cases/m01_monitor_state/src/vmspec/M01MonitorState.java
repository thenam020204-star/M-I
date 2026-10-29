package vmspec;

import javax.microedition.midlet.MIDlet;

public class M01MonitorState extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }



    protected void startApp() {
        Object o = new Object();
        try { o.wait(); p("wait ok"); } catch (IllegalMonitorStateException e) { p("wait IllegalMonitorStateException"); } catch (InterruptedException e) { p("wait Interrupted"); }
        try { o.wait(10); p("wait(10) ok"); } catch (IllegalMonitorStateException e) { p("wait(10) IllegalMonitorStateException"); } catch (InterruptedException e) { p("wait(10) Interrupted"); }
        try { o.notify(); p("notify ok"); } catch (IllegalMonitorStateException e) { p("notify IllegalMonitorStateException"); }
        try { o.notifyAll(); p("notifyAll ok"); } catch (IllegalMonitorStateException e) { p("notifyAll IllegalMonitorStateException"); }
        synchronized (o) {
            o.notify();
            o.notifyAll();
            p("notify inside lock ok");
            synchronized (o) { synchronized (o) { p("reentrant x3 ok"); } }
            o.notify();
            p("notify after nested exit ok");
        }
        try { o.notify(); p("notify after exit ok"); } catch (IllegalMonitorStateException e) { p("notify after exit IllegalMonitorStateException"); }
        Object lockA = new Object(), lockB = new Object();
        synchronized (lockA) {
            try { lockB.notify(); p("notify other lock ok"); } catch (IllegalMonitorStateException e) { p("notify other lock IllegalMonitorStateException"); }
        }
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
