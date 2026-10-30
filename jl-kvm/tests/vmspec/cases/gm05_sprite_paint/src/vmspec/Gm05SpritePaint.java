package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/** T25: Sprite painting: frames, sequences, the 8 transforms, position / reference pixel, visibility, clip; checksum of the target image. */
public class Gm05SpritePaint extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    /** 3 frames of 5x4, each with an asymmetric pattern in its own colour. */
    static Image sheet() {
        Image im = Image.createImage(15, 4);
        Graphics g = im.getGraphics();
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, 15, 4);
        g.setColor(0xFF0000); g.fillRect(0, 0, 3, 1); g.fillRect(0, 1, 1, 3);
        g.setColor(0x00A000); g.fillRect(5, 0, 5, 1); g.fillRect(9, 1, 1, 2); g.fillRect(6, 3, 2, 1);
        g.setColor(0x0000FF); g.fillRect(10, 0, 1, 4); g.fillRect(11, 3, 4, 1); g.fillRect(13, 1, 2, 1);
        return Image.createImage(im);
    }

    static String sum(Image im) {
        int w = im.getWidth(), h = im.getHeight();
        int[] px = new int[w * h];
        im.getRGB(px, 0, w, 0, 0, w, h);
        int a = 1, b = 0;
        for (int i = 0; i < px.length; i++) {
            a = (a + (px[i] & 0xFFFFFF) + i) % 65521;
            b = (b + a) % 65521;
        }
        return Integer.toHexString((b << 16) | a);
    }

    static Image target() {
        Image t = Image.createImage(16, 16);
        t.getGraphics().setColor(0xFFFFFF);
        t.getGraphics().fillRect(0, 0, 16, 16);
        return t;
    }

    protected void startApp() {
        Sprite s = new Sprite(sheet(), 5, 4);
        for (int f = 0; f < 3; f++) {
            Image t = target();
            s.setFrame(f);
            s.setPosition(3, 5);
            s.paint(t.getGraphics());
            p("frame " + f + ": " + sum(t));
        }
        for (int tr = 0; tr < 8; tr++) {
            Image t = target();
            s.setFrame(1);
            s.setTransform(0);
            s.setPosition(3, 5);
            s.defineReferencePixel(2, 1);
            s.setTransform(tr);
            s.paint(t.getGraphics());
            p("transform " + tr + " at ref (2,1) on (3,5)+...: " + sum(t) + " pos=" + s.getX() + "," + s.getY());
        }
        s.setTransform(0);
        s.setFrameSequence(new int[] {2, 0});
        Image t1 = target();
        s.setPosition(0, 0);
        s.paint(t1.getGraphics());
        s.nextFrame();
        s.setPosition(8, 8);
        s.paint(t1.getGraphics());
        p("sequence {2,0}: " + sum(t1));
        Image t2 = target();
        Graphics g2 = t2.getGraphics();
        g2.setClip(2, 2, 6, 6);
        g2.translate(1, 1);
        s.setPosition(0, 0);
        s.paint(g2);
        p("clip + translate: " + sum(t2));
        Image t3 = target();
        s.setVisible(false);
        s.paint(t3.getGraphics());
        p("invisible: " + sum(t3) + " " + sum(target()));
        Image t4 = target();
        s.setVisible(true);
        s.setPosition(-3, -2);
        s.paint(t4.getGraphics());
        s.setPosition(14, 14);
        s.paint(t4.getGraphics());
        p("partly outside: " + sum(t4));
        try { s.paint(null); p("paint(null) ok"); } catch (NullPointerException e) { p("paint(null) NullPointerException"); }
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
