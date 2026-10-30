/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import jlkvm.internal.Device;
import jlkvm.internal.Access;
import jlkvm.internal.CanvasAccess;
import jlkvm.internal.EventLoop;
import jlkvm.internal.KeyTap;

public abstract class Canvas extends Displayable {
    public static final int KEY_NUM0 = 48, KEY_NUM1 = 49, KEY_NUM2 = 50, KEY_NUM3 = 51, KEY_NUM4 = 52, KEY_NUM5 = 53, KEY_NUM6 = 54, KEY_NUM7 = 55,
            KEY_NUM8 = 56, KEY_NUM9 = 57, KEY_STAR = 42, KEY_POUND = 35;
    public static final int UP = 1, DOWN = 6, LEFT = 2, RIGHT = 5, FIRE = 8, GAME_A = 9, GAME_B = 10, GAME_C = 11, GAME_D = 12;

    static {
        // friend access for javax.microedition.lcdui.game.GameCanvas (key states), see jlkvm.internal.Access
        Access.canvas = new CanvasAccess() {
            public void setTap(Object canvas, KeyTap tap) {
                ((Canvas) canvas).tap = tap;
            }

            public void initFull(Object canvas) {
                ((Canvas) canvas).initFull();
            }
        };
    }

    /** Nokia FullCanvas (through LcduiAccess): full screen from the constructor on, so the size the application knows is the full one. */
    void initFull() {
        fullScreen = true;
        knownW = getWidth();
        knownH = getHeight();
    }

    boolean fullScreen;
    /** Sees every key event first (GameCanvas); set through {@link Access}. */
    KeyTap tap;
    private Graphics gfx;
    /** Menu of the commands (more than two): open, highlighted entry. Drawn over the canvas by the library. */
    boolean menuOpen;
    private int menuSel;
    private Image buffer;
    private boolean repaintPending, painting;
    private int dirtyX0, dirtyY0, dirtyX1, dirtyY1;
    /** Size the application was last told about (constructor / sizeChanged). */
    private int knownW, knownH;
    private final Runnable paintJob = new Runnable() {
        public void run() {
            paintNow();
        }
    };

    protected Canvas() {
        knownW = getWidth();
        knownH = getHeight();
    }

    public boolean isDoubleBuffered() {
        return Device.info0(Device.DOUBLE_BUFFERED) != 0;
    }

    public boolean hasPointerEvents() {
        return Device.info0(Device.POINTER) != 0;
    }

    public boolean hasPointerMotionEvents() {
        return Device.info0(Device.POINTER_MOTION) != 0;
    }

    public boolean hasRepeatEvents() {
        return Device.info0(Device.REPEAT) != 0;
    }

    int width0() {
        return fullScreen ? Display.screenW : Display.canvasW;
    }

    int height0() {
        return fullScreen ? Display.screenH : Display.canvasH;
    }

    public int getKeyCode(int gameAction) {
        switch (gameAction) {
            case UP: return Device.key0(Device.KEY_UP);
            case DOWN: return Device.key0(Device.KEY_DOWN);
            case LEFT: return Device.key0(Device.KEY_LEFT);
            case RIGHT: return Device.key0(Device.KEY_RIGHT);
            case FIRE: return Device.key0(Device.KEY_SELECT);
            case GAME_A: return KEY_NUM1;
            case GAME_B: return KEY_NUM3;
            case GAME_C: return KEY_NUM7;
            case GAME_D: return KEY_NUM9;
            default: throw new IllegalArgumentException("gameAction");
        }
    }

    public int getGameAction(int keyCode) {
        if (keyCode == Device.key0(Device.KEY_UP) || keyCode == KEY_NUM2) return UP;
        if (keyCode == Device.key0(Device.KEY_DOWN) || keyCode == KEY_NUM8) return DOWN;
        if (keyCode == Device.key0(Device.KEY_LEFT) || keyCode == KEY_NUM4) return LEFT;
        if (keyCode == Device.key0(Device.KEY_RIGHT) || keyCode == KEY_NUM6) return RIGHT;
        if (keyCode == Device.key0(Device.KEY_SELECT) || keyCode == KEY_NUM5) return FIRE;
        if (keyCode == KEY_NUM1) return GAME_A;
        if (keyCode == KEY_NUM3) return GAME_B;
        if (keyCode == KEY_NUM7) return GAME_C;
        if (keyCode == KEY_NUM9) return GAME_D;
        if (keyCode == KEY_NUM0 || keyCode == KEY_STAR || keyCode == KEY_POUND || (keyCode >= -11 && keyCode <= -1)) return 0;
        throw new IllegalArgumentException("keyCode " + keyCode);
    }

    public String getKeyName(int keyCode) throws IllegalArgumentException {
        if (keyCode >= KEY_NUM0 && keyCode <= KEY_NUM9) return String.valueOf((char) keyCode);
        if (keyCode == KEY_STAR) return "*";
        if (keyCode == KEY_POUND) return "#";
        if (keyCode == Device.key0(Device.KEY_SOFT_LEFT)) return "SOFT1";
        if (keyCode == Device.key0(Device.KEY_SOFT_RIGHT)) return "SOFT2";
        if (keyCode == Device.key0(Device.KEY_UP)) return "UP";
        if (keyCode == Device.key0(Device.KEY_DOWN)) return "DOWN";
        if (keyCode == Device.key0(Device.KEY_LEFT)) return "LEFT";
        if (keyCode == Device.key0(Device.KEY_RIGHT)) return "RIGHT";
        if (keyCode == Device.key0(Device.KEY_SELECT)) return "SELECT";
        if (keyCode == Device.key0(Device.KEY_CLEAR)) return "CLEAR";
        if (keyCode == Device.key0(Device.KEY_SEND)) return "SEND";
        if (keyCode == Device.key0(Device.KEY_END)) return "END";
        throw new IllegalArgumentException("keyCode " + keyCode);
    }

    public void setFullScreenMode(boolean mode) {
        if (mode == fullScreen) return;
        fullScreen = mode;
        if (onScreen()) {
            EventLoop.post(new Runnable() {
                public void run() {
                    resized();
                }
            });
        }
    }

    protected abstract void paint(Graphics g);

    protected void showNotify() {
    }

    protected void hideNotify() {
    }

    protected void keyPressed(int keyCode) {
    }

    protected void keyReleased(int keyCode) {
    }

    protected void keyRepeated(int keyCode) {
    }

    protected void pointerPressed(int x, int y) {
    }

    protected void pointerReleased(int x, int y) {
    }

    protected void pointerDragged(int x, int y) {
    }

    protected void sizeChanged(int w, int h) {
    }

    public final void repaint() {
        repaint(0, 0, getWidth(), getHeight());
    }

    public final void repaint(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) return;
        boolean post = false;
        synchronized (this) {
            if (!onScreen()) return;
            if (repaintPending) {
                if (x < dirtyX0) dirtyX0 = x;
                if (y < dirtyY0) dirtyY0 = y;
                if (x + width > dirtyX1) dirtyX1 = x + width;
                if (y + height > dirtyY1) dirtyY1 = y + height;
            } else {
                dirtyX0 = x;
                dirtyY0 = y;
                dirtyX1 = x + width;
                dirtyY1 = y + height;
                repaintPending = true;
                post = true;
            }
        }
        if (post) EventLoop.post(paintJob);
    }

    public final void serviceRepaints() {
        if (EventLoop.isEventThread()) {
            paintNow();
            return;
        }
        synchronized (this) {
            while ((repaintPending || painting) && onScreen()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    /** Event thread: runs the pending repaint (if any) synchronously. */
    void paintNow() {
        int x0, y0, x1, y1;
        synchronized (this) {
            if (!repaintPending) return;
            repaintPending = false;
            if (!isShown()) {
                notifyAll();
                return;
            }
            x0 = dirtyX0;
            y0 = dirtyY0;
            x1 = dirtyX1;
            y1 = dirtyY1;
            painting = true;
        }
        try {
            int w = getWidth(), h = getHeight();
            if (buffer == null || buffer.getWidth() != w || buffer.getHeight() != h) {
                buffer = Image.createImage(w, h);
                gfx = new Graphics(buffer, true);
            }
            Graphics g = gfx;
            g.reset();
            g.setClip(x0, y0, x1 - x0, y1 - y0);
            paint(g);
            if (menuOpen) paintMenu(g, w, h);
            Display.present(buffer);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            synchronized (this) {
                painting = false;
                notifyAll();
            }
        }
    }

    /** Event thread: the drawable area changed (setFullScreenMode, display size). */
    void resized() {
        int w = getWidth(), h = getHeight();
        if (!isShown()) return;
        if (w != knownW || h != knownH) {
            knownW = w;
            knownH = h;
            try {
                sizeChanged(w, h);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        repaint();
    }

    void onShow() {
        int w = getWidth(), h = getHeight();
        if (w != knownW || h != knownH) {
            knownW = w;
            knownH = h;
            try {
                sizeChanged(w, h);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        try {
            showNotify();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        visible = true;
        sendNow();
        repaint();
    }

    void onHide() {
        visible = false;
        menuOpen = false;
        try {
            hideNotify();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        synchronized (this) {
            notifyAll();
        }
    }

    /** Commands the menu lists: all except the one the right soft key takes. */
    private Command menuCommand(int i) {
        Command back = backCommand();
        synchronized (commands) {
            for (int k = 0; k < commands.size(); k++) {
                Command c = (Command) commands.elementAt(k);
                if (c == back) continue;
                if (i-- == 0) return c;
            }
        }
        return null;
    }

    private int menuSize() {
        int n = commandCount();
        return backCommand() != null ? n - 1 : n;
    }

    void openMenu() {
        menuOpen = true;
        menuSel = 0;
        repaint();
    }

    /** Event thread: a key while the menu is open. @return the command to fire, or null (the menu stays open or was closed). */
    Command menuKey(int code) {
        int n = menuSize();
        if (code == Device.key0(Device.KEY_UP)) {
            menuSel = menuSel == 0 ? n - 1 : menuSel - 1;
        } else if (code == Device.key0(Device.KEY_DOWN)) {
            menuSel = menuSel + 1 >= n ? 0 : menuSel + 1;
        } else if (code == Device.key0(Device.KEY_SELECT) || code == Device.key0(Device.KEY_SOFT_LEFT)) {
            menuOpen = false;
            repaint();
            return menuCommand(menuSel);
        } else if (code == Device.key0(Device.KEY_SOFT_RIGHT) || code == Device.key0(Device.KEY_CLEAR)) {
            menuOpen = false;
            repaint();
            return null;
        }
        repaint();
        return null;
    }

    private void paintMenu(Graphics g, int w, int h) {
        Font f = Font.getDefaultFont();
        int n = menuSize(), rowH = f.getHeight() + 2, boxW = w * 3 / 4, boxH = n * rowH + 4;
        int x = 2, y = h - boxH - 2;
        g.setClip(0, 0, w, h);
        g.setFont(f);
        g.setColor(0xFFFFFF);
        g.fillRect(x, y, boxW, boxH);
        g.setColor(0);
        g.drawRect(x, y, boxW - 1, boxH - 1);
        for (int i = 0; i < n; i++) {
            Command c = menuCommand(i);
            if (i == menuSel) {
                g.setColor(0x000080);
                g.fillRect(x + 1, y + 2 + i * rowH, boxW - 2, rowH);
                g.setColor(0xFFFFFF);
            } else {
                g.setColor(0);
            }
            if (c != null) g.drawString(c.getLabel(), x + 3, y + 3 + i * rowH, Graphics.TOP | Graphics.LEFT);
        }
    }

    void onKey(int type, int code) {
        KeyTap k = tap;
        if (k != null && k.key(type, code)) return;
        try {
            if (type == EventLoop.KEY_PRESSED) keyPressed(code);
            else if (type == EventLoop.KEY_RELEASED) keyReleased(code);
            else if (hasRepeatEvents()) keyRepeated(code);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void onPointer(int type, int x, int y) {
        try {
            if (type == EventLoop.POINTER_PRESSED) pointerPressed(x, y);
            else if (type == EventLoop.POINTER_RELEASED) pointerReleased(x, y);
            else if (hasPointerMotionEvents()) pointerDragged(x, y);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
