package ev;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

/**
 * Input events and soft keys (T22), driven by a script on host-desktop. Every callback records which thread ran it: they must all be
 * the same single thread (D13). Keys: 0 = toggle full-screen mode, 5 = callSerially + repaint + serviceRepaints.
 */
public class EventsMIDlet extends MIDlet implements CommandListener {
    static void p(String s) { System.out.println("> " + s); }

    Thread eventThread;
    boolean consistent = true;
    Display d;
    Command back = new Command("Back", Command.BACK, 1), ok = new Command("OK", Command.OK, 2);

    void thread(String where) {
        Thread t = Thread.currentThread();
        if (eventThread == null) eventThread = t;
        else if (eventThread != t) { consistent = false; p("THREAD MISMATCH in " + where); }
    }

    class C extends Canvas {
        protected void paint(Graphics g) { thread("paint"); p("paint " + getWidth() + "x" + getHeight()); g.setColor(0x102030); g.fillRect(0, 0, getWidth(), getHeight()); }
        protected void showNotify() { thread("showNotify"); p("showNotify"); }
        protected void hideNotify() { thread("hideNotify"); p("hideNotify"); }
        protected void sizeChanged(int w, int h) { thread("sizeChanged"); p("sizeChanged " + w + "x" + h); }
        protected void keyPressed(int k) {
            thread("keyPressed");
            p("keyPressed " + k + " action=" + safeAction(k) + " name=" + safeName(k));
            if (k == 48) { fullScreen = !fullScreen; setFullScreenMode(fullScreen); }
            if (k == 53) {
                d.callSerially(new Runnable() { public void run() { thread("callSerially"); p("callSerially ran"); } });
                repaint();
                serviceRepaints();
                p("serviceRepaints returned");
            }
        }
        boolean fullScreen;
        protected void keyReleased(int k) { thread("keyReleased"); p("keyReleased " + k); }
        protected void keyRepeated(int k) { thread("keyRepeated"); p("keyRepeated " + k); }
        protected void pointerPressed(int x, int y) { thread("pointerPressed"); p("pointerPressed " + x + "," + y); }
        int safeAction(int k) { try { return getGameAction(k); } catch (IllegalArgumentException e) { return -99; } }
        String safeName(int k) { try { return getKeyName(k); } catch (IllegalArgumentException e) { return "?"; } }
    }

    protected void startApp() {
        d = Display.getDisplay(this);
        C c = new C();
        c.addCommand(ok);
        c.addCommand(back);
        c.setCommandListener(this);
        p("keycodes UP=" + c.getKeyCode(Canvas.UP) + " DOWN=" + c.getKeyCode(Canvas.DOWN) + " LEFT=" + c.getKeyCode(Canvas.LEFT) + " RIGHT=" + c.getKeyCode(Canvas.RIGHT)
            + " FIRE=" + c.getKeyCode(Canvas.FIRE) + " A=" + c.getKeyCode(Canvas.GAME_A) + " B=" + c.getKeyCode(Canvas.GAME_B) + " C=" + c.getKeyCode(Canvas.GAME_C) + " D=" + c.getKeyCode(Canvas.GAME_D));
        p("flags repeat=" + c.hasRepeatEvents() + " pointer=" + c.hasPointerEvents() + " motion=" + c.hasPointerMotionEvents() + " doubleBuffered=" + c.isDoubleBuffered());
        d.setCurrent(c);
    }

    public void commandAction(Command c, Displayable s) {
        thread("commandAction");
        p("commandAction " + c.getLabel());
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { p("consistent=" + consistent); }
}
