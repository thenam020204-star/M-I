package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/** T25: Sprite model rules: frames, sequences, position, transforms with the reference pixel, collision rectangle, setImage, exceptions. */
public class Gm01SpriteModel extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }
    static Image sheet(int w, int h) { return Image.createImage(w, h); }
    static String geo(Sprite s) { return "pos=" + s.getX() + "," + s.getY() + " size=" + s.getWidth() + "x" + s.getHeight() + " ref=" + s.getRefPixelX() + "," + s.getRefPixelY(); }

    protected void startApp() {
        final Image img = sheet(32, 6);
        final Sprite s = new Sprite(img, 8, 6);
        t("Sprite(img) single frame", new T() { Object go() { Sprite a = new Sprite(img); return a.getWidth() + "x" + a.getHeight() + " frames=" + a.getRawFrameCount() + " seq=" + a.getFrameSequenceLength(); } });
        t("Sprite(img, 8, 6)", new T() { Object go() { return s.getWidth() + "x" + s.getHeight() + " frames=" + s.getRawFrameCount() + " seq=" + s.getFrameSequenceLength() + " frame=" + s.getFrame(); } });
        t("Sprite(null)", new T() { Object go() { new Sprite((Image) null); return null; } });
        t("Sprite(null, 8, 6)", new T() { Object go() { new Sprite(null, 8, 6); return null; } });
        t("Sprite(null Sprite)", new T() { Object go() { new Sprite((Sprite) null); return null; } });
        t("frame width 0", new T() { Object go() { new Sprite(img, 0, 6); return null; } });
        t("frame height -1", new T() { Object go() { new Sprite(img, 8, -1); return null; } });
        t("frame width not a divisor", new T() { Object go() { new Sprite(img, 5, 6); return null; } });
        t("frame height not a divisor", new T() { Object go() { new Sprite(img, 8, 4); return null; } });
        t("frames 2D (16x6 in 32x12)", new T() { Object go() { return new Integer(new Sprite(sheet(32, 12), 16, 6).getRawFrameCount()); } });

        t("setFrame(3)", new T() { Object go() { s.setFrame(3); return new Integer(s.getFrame()); } });
        t("setFrame(4)", new T() { Object go() { s.setFrame(4); return null; } });
        t("setFrame(-1)", new T() { Object go() { s.setFrame(-1); return null; } });
        t("nextFrame wraps", new T() { Object go() { s.nextFrame(); return new Integer(s.getFrame()); } });
        t("prevFrame wraps", new T() { Object go() { s.prevFrame(); return new Integer(s.getFrame()); } });
        t("setFrameSequence({2,0,2})", new T() { Object go() { s.setFrameSequence(new int[] {2, 0, 2}); return s.getFrameSequenceLength() + " frame=" + s.getFrame(); } });
        t("sequence: setFrame(2), nextFrame", new T() { Object go() { s.setFrame(2); s.nextFrame(); return new Integer(s.getFrame()); } });
        t("sequence: setFrame(3)", new T() { Object go() { s.setFrame(3); return null; } });
        t("raw frame count unchanged", new T() { Object go() { return new Integer(s.getRawFrameCount()); } });
        t("setFrameSequence({4})", new T() { Object go() { s.setFrameSequence(new int[] {4}); return null; } });
        t("setFrameSequence({-1})", new T() { Object go() { s.setFrameSequence(new int[] {-1}); return null; } });
        t("setFrameSequence({})", new T() { Object go() { s.setFrameSequence(new int[0]); return null; } });
        t("setFrameSequence(null) restores", new T() { Object go() { s.setFrameSequence(null); return s.getFrameSequenceLength() + " frame=" + s.getFrame(); } });
        t("sequence is copied", new T() { Object go() { int[] q = {1, 2}; s.setFrameSequence(q); q[0] = 3; s.setFrame(0); return new Integer(s.getFrame()); } });

        t("position", new T() { Object go() { s.setPosition(10, 20); s.move(-3, 5); return geo(s); } });
        t("visible", new T() { Object go() { s.setVisible(false); boolean a = s.isVisible(); s.setVisible(true); return a + " " + s.isVisible(); } });
        t("default ref pixel (0,0)", new T() { Object go() { return geo(s); } });

        for (int tr = 0; tr < 8; tr++) {
            final int tt = tr;
            t("transform " + tr + " ref(2,1) at (10,20)", new T() { Object go() {
                Sprite q = new Sprite(img, 8, 6);
                q.setPosition(10, 20);
                q.defineReferencePixel(2, 1);
                q.setTransform(tt);
                return geo(q); } });
        }
        for (int tr = 0; tr < 8; tr++) {
            final int tt = tr;
            t("transform " + tr + " setRefPixelPosition(50,60)", new T() { Object go() {
                Sprite q = new Sprite(img, 8, 6);
                q.defineReferencePixel(2, 1);
                q.setTransform(tt);
                q.setRefPixelPosition(50, 60);
                return geo(q); } });
        }
        t("setTransform(8)", new T() { Object go() { s.setTransform(8); return null; } });
        t("setTransform(-1)", new T() { Object go() { s.setTransform(-1); return null; } });
        t("ref pixel outside the frame", new T() { Object go() { Sprite q = new Sprite(img, 8, 6); q.defineReferencePixel(-5, 20); q.setRefPixelPosition(0, 0); return geo(q); } });

        // collision rectangle
        t("defineCollisionRectangle negative width", new T() { Object go() { s.defineCollisionRectangle(0, 0, -1, 1); return null; } });
        t("defineCollisionRectangle negative height", new T() { Object go() { s.defineCollisionRectangle(0, 0, 1, -1); return null; } });
        for (int tr = 0; tr < 8; tr++) {
            final int tt = tr;
            t("rect collision, transform " + tr, new T() { Object go() {
                // a: collision rectangle (1,1,4,3) of an 8x6 frame at (0,0); b: 2x2 sprite moved over a's transformed corners
                Sprite a = new Sprite(img, 8, 6);
                a.defineCollisionRectangle(1, 1, 4, 3);
                a.setTransform(tt);
                Sprite b = new Sprite(sheet(2, 2));
                StringBuffer r = new StringBuffer();
                for (int y = -2; y <= 8; y++) {
                    for (int x = -2; x <= 8; x++) {
                        b.setPosition(x, y);
                        r.append(a.collidesWith(b, false) ? '#' : '.');
                    }
                    r.append('/');
                }
                return r; } });
        }
        t("invisible sprites never collide", new T() { Object go() { Sprite a = new Sprite(img, 8, 6), b = new Sprite(img, 8, 6); b.setVisible(false); return new Boolean(a.collidesWith(b, false)); } });
        t("collidesWith(null Sprite)", new T() { Object go() { return new Boolean(s.collidesWith((Sprite) null, false)); } });
        t("collidesWith(null TiledLayer)", new T() { Object go() { return new Boolean(s.collidesWith((TiledLayer) null, false)); } });
        t("collidesWith(null Image)", new T() { Object go() { return new Boolean(s.collidesWith((Image) null, 0, 0, false)); } });

        // setImage
        t("setImage same frame size, more frames keeps sequence and frame", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.setFrameSequence(new int[] {3, 1});
            q.setFrame(1);
            q.setImage(sheet(48, 6), 8, 6);
            return q.getRawFrameCount() + " seq=" + q.getFrameSequenceLength() + " frame=" + q.getFrame(); } });
        t("setImage fewer frames resets", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.setFrameSequence(new int[] {3, 1});
            q.setFrame(1);
            q.setImage(sheet(16, 6), 8, 6);
            return q.getRawFrameCount() + " seq=" + q.getFrameSequenceLength() + " frame=" + q.getFrame(); } });
        t("setImage new frame size keeps the reference pixel", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.setPosition(10, 10);
            q.defineReferencePixel(4, 3);
            q.defineCollisionRectangle(1, 1, 2, 2);
            q.setImage(sheet(20, 10), 10, 10);
            return geo(q); } });
        t("setImage new frame size resets the collision rectangle", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.defineCollisionRectangle(1, 1, 1, 1);
            q.setImage(sheet(20, 10), 10, 10);
            Sprite b = new Sprite(sheet(1, 1));
            b.setPosition(9, 9);
            return new Boolean(q.collidesWith(b, false)); } });
        t("setImage same size keeps the collision rectangle", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.defineCollisionRectangle(1, 1, 1, 1);
            q.setImage(sheet(16, 6), 8, 6);
            Sprite b = new Sprite(sheet(1, 1));
            b.setPosition(5, 5);
            return new Boolean(q.collidesWith(b, false)); } });
        t("setImage bad size", new T() { Object go() { s.setImage(img, 7, 6); return null; } });
        t("setImage null", new T() { Object go() { s.setImage(null, 8, 6); return null; } });

        // copy constructor
        t("Sprite(Sprite) copies the state", new T() { Object go() {
            Sprite q = new Sprite(img, 8, 6);
            q.setPosition(3, 4);
            q.defineReferencePixel(1, 2);
            q.setFrameSequence(new int[] {2, 3});
            q.setFrame(1);
            q.setTransform(Sprite.TRANS_ROT90);
            Sprite c = new Sprite(q);
            q.setFrame(0);
            return geo(c) + " frame=" + c.getFrame() + " seq=" + c.getFrameSequenceLength(); } });
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
