package vmspec;

import java.lang.ref.WeakReference;
import javax.microedition.midlet.MIDlet;

/** T13: CLDC 1.1 java.lang.ref.WeakReference: referents reachable only weakly are cleared by a collection, the others survive (and move) intact. */
public class G03Main extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static class Holder { Object inner; int[] data = new int[] { 11, 22, 33 }; }
    static class MyWeak extends WeakReference {
        int tag;
        Object extra;
        MyWeak(Object r, int tag, Object extra) { super(r); this.tag = tag; this.extra = extra; }
    }

    static void gc() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
    }

    static void garbage(int n) { for (int i = 0; i < n; i++) { int[] g = new int[16]; g[0] = i; } }

    static WeakReference weakOnly() { return new WeakReference(new Object()); }

    static WeakReference[] chain() {
        Holder a = new Holder();
        Holder b = new Holder();
        a.inner = b;                                   // b is strongly reachable only through a
        return new WeakReference[] { new WeakReference(a), new WeakReference(b) };
    }

    protected void startApp() {
        Object strong = new Holder();
        ((Holder) strong).inner = "kept";
        WeakReference wStrong = new WeakReference(strong);
        WeakReference wGone = weakOnly();
        int[] arr = new int[] { 1, 2, 3 };
        WeakReference wArr = new WeakReference(arr);
        WeakReference wCleared = new WeakReference(new Holder());
        wCleared.clear();
        WeakReference[] ch = chain();
        MyWeak my = new MyWeak(new Object(), 7, "extra-kept");
        MyWeak myLive = new MyWeak(strong, 8, new int[] { 5 });
        WeakReference[] many = new WeakReference[500];
        for (int i = 0; i < many.length; i++) many[i] = new WeakReference(new int[4]);
        WeakReference[] manyLive = new WeakReference[500];
        Object[] keep = new Object[500];
        for (int i = 0; i < keep.length; i++) { keep[i] = new int[] { i }; manyLive[i] = new WeakReference(keep[i]); }

        garbage(5000);
        gc();
        garbage(100);
        gc();

        p("strong referent kept: " + (wStrong.get() == strong));
        p("kept referent intact: " + (((Holder) wStrong.get()).data[2] + " " + ((Holder) wStrong.get()).inner));
        p("weak-only referent cleared: " + (wGone.get() == null));
        p("array referent kept: " + (wArr.get() == arr) + " " + ((int[]) wArr.get())[1]);
        p("cleared by clear(): " + (wCleared.get() == null));
        p("weak chain a cleared: " + (ch[0].get() == null));
        p("weak chain b cleared: " + (ch[1].get() == null));
        p("subclass referent cleared: " + (my.get() == null));
        p("subclass fields kept: " + my.tag + " " + my.extra);
        p("subclass live referent: " + (myLive.get() == strong) + " " + myLive.tag + " " + ((int[]) myLive.extra)[0]);
        int alive = 0, aliveLive = 0, wrong = 0;
        for (int i = 0; i < many.length; i++) if (many[i].get() != null) alive++;
        for (int i = 0; i < manyLive.length; i++) {
            Object o = manyLive[i].get();
            if (o != null) { aliveLive++; if (o != keep[i] || ((int[]) o)[0] != i) wrong++; }
        }
        p("garbage referents still alive: " + alive);
        p("live referents alive: " + aliveLive + " wrong: " + wrong);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
