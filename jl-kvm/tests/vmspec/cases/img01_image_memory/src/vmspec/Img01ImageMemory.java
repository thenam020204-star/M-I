package vmspec;

import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/**
 * Whether Image pixel memory counts against the Java heap is device dependent (profile parameter
 * imagesCountInHeap, PLAN "Ràng buộc"). The oracles are JVM-hosted, so only the delta is printed ('# ' info).
 */
public class Img01ImageMemory extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    protected void startApp() {
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long f0 = rt.freeMemory();
        Image[] keep = new Image[10];
        for (int i = 0; i < keep.length; i++) keep[i] = Image.createImage(200, 200);
        long f1 = rt.freeMemory();
        System.out.println("# freeMemory delta after 10 images 200x200: " + (f0 - f1) + " bytes (nominal pixel data " + (10 * 200 * 200 * 4) + ")");
        p("images created " + keep.length);
        p("mutable " + keep[0].isMutable() + " size " + keep[0].getWidth() + "x" + keep[0].getHeight());
        keep = null;
        rt.gc();
        long total = rt.totalMemory();
        p("total>=free " + (total >= rt.freeMemory()));
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
