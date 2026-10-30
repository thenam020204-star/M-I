/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jlkvm.internal.Access;
import jlkvm.internal.KeyTap;

public abstract class GameCanvas extends Canvas {
    public static final int UP_PRESSED = 1 << Canvas.UP, DOWN_PRESSED = 1 << Canvas.DOWN, LEFT_PRESSED = 1 << Canvas.LEFT,
            RIGHT_PRESSED = 1 << Canvas.RIGHT, FIRE_PRESSED = 1 << Canvas.FIRE, GAME_A_PRESSED = 1 << Canvas.GAME_A,
            GAME_B_PRESSED = 1 << Canvas.GAME_B, GAME_C_PRESSED = 1 << Canvas.GAME_C, GAME_D_PRESSED = 1 << Canvas.GAME_D;

    /**
     * Key bookkeeping (event thread writes, game threads read). A key press sets its bit in {@code latched} and {@code down}; a release
     * clears {@code down}. getKeyStates returns {@code latched} and restarts it from {@code down}: a key pressed and released between two
     * calls is seen once, a key that is still down is seen by every call.
     */
    private static final class Tap implements KeyTap {
        final boolean suppress;
        final GameCanvas owner;
        int latched, down;

        Tap(GameCanvas owner, boolean suppress) {
            this.owner = owner;
            this.suppress = suppress;
        }

        public synchronized boolean key(int type, int code) {
            int action;
            try {
                action = owner.getGameAction(code);
            } catch (IllegalArgumentException e) {
                return false;
            }
            if (action == 0) return false;
            int bit = 1 << action;
            if (type == 1) {          // pressed
                latched |= bit;
                down |= bit;
            } else if (type == 2) {   // released
                down &= ~bit;
            }
            return suppress;
        }

        synchronized int poll() {
            int r = latched;
            latched = down;
            return r;
        }
    }

    private final Tap tap;
    private Image buffer;

    protected GameCanvas(boolean suppressKeyEvents) {
        super();
        tap = new Tap(this, suppressKeyEvents);
        Access.canvas.setTap(this, tap);
        ensureBuffer();
    }

    private Image ensureBuffer() {
        Image b = buffer;
        int w = getWidth(), h = getHeight();
        if (b == null || b.getWidth() != w || b.getHeight() != h) buffer = b = Image.createImage(w, h);
        return b;
    }

    protected Graphics getGraphics() {
        return ensureBuffer().getGraphics();
    }

    public int getKeyStates() {
        return tap.poll();
    }

    public void paint(Graphics g) {
        if (g == null) throw new NullPointerException();
        g.drawImage(ensureBuffer(), 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    public void flushGraphics(int x, int y, int width, int height) {
        int w = getWidth(), h = getHeight();
        int x0 = x < 0 ? 0 : x, y0 = y < 0 ? 0 : y, x1 = x + width > w ? w : x + width, y1 = y + height > h ? h : y + height;
        if (x1 <= x0 || y1 <= y0) return;
        if (!isShown()) return;
        ensureBuffer();
        repaint(x0, y0, x1 - x0, y1 - y0);
        serviceRepaints();
    }

    public void flushGraphics() {
        flushGraphics(0, 0, getWidth(), getHeight());
    }
}
