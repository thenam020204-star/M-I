package game;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import javax.microedition.midlet.MIDlet;

/**
 * T25 sample: a GameCanvas game loop on its own thread (key states with MIDP latching, suppressKeyEvents true then false, sprite /
 * tiled layer / layer manager painted into the off-screen buffer, flushGraphics) and an allocation check of the library's drawing path.
 * Driven by tests/midlets/scripts/game.inp.
 */
public class GameMIDlet extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    Display display;
    Thread eventThread;

    class Game extends GameCanvas {
        final String name;
        Game(String name, boolean suppress) { super(suppress); this.name = name; }
        protected void keyPressed(int k) { p(name + " keyPressed " + k + (Thread.currentThread() == eventThread ? " ET" : " NOT-EVENT-THREAD")); }
        protected void keyReleased(int k) { p(name + " keyReleased " + k); }
        protected void showNotify() { p(name + " showNotify"); }
        Graphics graphics() { return getGraphics(); }
    }

    Game a, b;

    protected void startApp() {
        display = Display.getDisplay(this);
        display.callSerially(new Runnable() { public void run() { eventThread = Thread.currentThread(); } });
        a = new Game("A", true);
        b = new Game("B", false);
        display.setCurrent(a);
        new Thread() { public void run() { loop(); } }.start();
    }

    static String hex(int v) { return Integer.toHexString(v); }

    void poll(Game g, int ms, int step) {
        int last = 0;
        for (int t = 0; t < ms; t += step) {
            int s = g.getKeyStates();
            if (s != last) { p(g.name + " keys " + hex(s)); last = s; }
            try { Thread.sleep(step); } catch (InterruptedException e) { }
        }
    }

    void loop() {
        // phase 1: canvas A (events suppressed for game keys), polling every 50 ms for 1000 ms
        Graphics ga = a.graphics();
        p("A initial buffer pixel: " + Integer.toHexString(pixel(a)));
        ga.setColor(0x336699);
        ga.fillRect(0, 0, 20, 20);
        a.flushGraphics();
        p("flush shown: " + a.isShown());
        poll(a, 1000, 50);
        // phase 2: canvas B (events delivered)
        display.setCurrent(b);
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        p("B shown: " + b.isShown());
        poll(b, 700, 50);
        // phase 3: game objects on B, then an allocation check of the drawing path
        Graphics g = b.graphics();
        Image sheet = Image.createImage(16, 8);
        Graphics sg = sheet.getGraphics();
        sg.setColor(0xFF0000); sg.fillRect(0, 0, 8, 8);
        sg.setColor(0x00FF00); sg.fillRect(8, 0, 8, 8);
        final Sprite sprite = new Sprite(Image.createImage(sheet), 8, 8);
        sprite.setFrameSequence(new int[] {0, 1});
        Image ts = Image.createImage(16, 8);
        Graphics tg = ts.getGraphics();
        tg.setColor(0x0000FF); tg.fillRect(0, 0, 8, 8);
        tg.setColor(0xFFFF00); tg.fillRect(8, 0, 8, 8);
        TiledLayer tl = new TiledLayer(8, 6, Image.createImage(ts), 8, 8);
        tl.fillCells(0, 0, 8, 6, 1);
        int an = tl.createAnimatedTile(2);
        tl.setCell(3, 3, an);
        LayerManager lm = new LayerManager();
        lm.append(sprite);
        lm.append(tl);
        Sprite other = new Sprite(Image.createImage(sheet), 8, 8);
        other.setPosition(20, 10);
        for (int i = 0; i < 5; i++) frame(g, lm, sprite, other, tl, an, i);   // warm-up (masks are built on the first pixel-level query)
        // the alpha masks of pixel-level collision are built on the first query of each image: do that before measuring
        sprite.setPosition(20, 10);
        sprite.collidesWith(other, true);
        sprite.collidesWith(tl, true);
        System.gc();
        long before = Runtime.getRuntime().freeMemory();
        int hits = 0;
        for (int i = 0; i < 200; i++) hits += frame(g, lm, sprite, other, tl, an, i) ? 1 : 0;
        long after = Runtime.getRuntime().freeMemory();
        p("hits in 200 frames: " + hits);
        p("heap bytes allocated by 200 frames of flush / paint / collision: " + (before - after));
        p("final pixel: " + Integer.toHexString(pixel(b)));
        notifyDestroyed();
    }

    boolean frame(Graphics g, LayerManager lm, Sprite sprite, Sprite other, TiledLayer tl, int an, int i) {
        sprite.setPosition(i % 40, (i / 2) % 20);
        sprite.nextFrame();
        tl.setAnimatedTile(an, 1 + (i & 1));
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, 64, 48);
        lm.paint(g, 0, 0);
        boolean hit = sprite.collidesWith(other, true) | sprite.collidesWith(tl, true) | sprite.collidesWith(other, false);
        b.flushGraphics();
        return hit;
    }

    /** The first pixel of the canvas buffer: GameCanvas.paint draws the buffer at 0,0, so a 1x1 image receives it. */
    int pixel(Game c) {
        int[] px = new int[1];
        Image im = Image.createImage(1, 1);
        c.paint(im.getGraphics());
        im.getRGB(px, 0, 1, 0, 0, 1, 1);
        return px[0];
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
