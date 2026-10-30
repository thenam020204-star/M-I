/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public abstract class CustomItem extends Item {
    protected static final int TRAVERSE_HORIZONTAL = 1, TRAVERSE_VERTICAL = 2, KEY_PRESS = 4, KEY_RELEASE = 8, KEY_REPEAT = 16,
            POINTER_PRESS = 32, POINTER_RELEASE = 64, POINTER_DRAG = 128, NONE = 0;

    private Image content;
    private int cw = -1, ch = -1;
    private boolean contentDirty = true;
    private boolean shown;

    protected CustomItem(String label) {
        super(label);
    }

    public int getGameAction(int keyCode) {
        return GameKeys.gameAction(keyCode);
    }

    protected final int getInteractionModes() {
        int m = TRAVERSE_HORIZONTAL | TRAVERSE_VERTICAL | KEY_PRESS | KEY_RELEASE;
        if (jlkvm.internal.Device.info0(jlkvm.internal.Device.REPEAT) != 0) m |= KEY_REPEAT;
        if (jlkvm.internal.Device.info0(jlkvm.internal.Device.POINTER) != 0) m |= POINTER_PRESS | POINTER_RELEASE;
        if (jlkvm.internal.Device.info0(jlkvm.internal.Device.POINTER_MOTION) != 0) m |= POINTER_DRAG;
        return m;
    }

    protected abstract int getMinContentWidth();

    protected abstract int getMinContentHeight();

    protected abstract int getPrefContentWidth(int height);

    protected abstract int getPrefContentHeight(int width);

    protected abstract void paint(Graphics g, int w, int h);

    protected final void invalidate() {
        contentDirty = true;
        changed();
    }

    protected final void repaint() {
        contentDirty = true;
        changed();
    }

    protected final void repaint(int x, int y, int w, int h) {
        if (w <= 0 || h <= 0) return;
        contentDirty = true;
        changed();
    }

    protected void sizeChanged(int w, int h) {
    }

    protected boolean traverse(int dir, int viewportWidth, int viewportHeight, int[] visRect_inout) {
        return false;
    }

    protected void traverseOut() {
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

    protected void showNotify() {
    }

    protected void hideNotify() {
    }

    // ---------------------------------------------------------------- host side (event thread)

    int uiKind() {
        return ScreenCodec.I_CUSTOM;
    }

    /** Size of the content area: the preferred size (or the locked size minus the label) within the form width. */
    private void measure() {
        int w, h;
        try {
            int minW = getMinContentWidth(), minH = getMinContentHeight();
            w = prefW >= 0 ? prefW : getPrefContentWidth(prefH >= 0 ? prefH - labelH() : -1);
            if (w > availW()) w = availW();
            if (w < minW) w = minW;
            h = prefH >= 0 ? prefH - labelH() : getPrefContentHeight(w);
            if (h < minH) h = minH;
        } catch (Throwable t) {
            t.printStackTrace();
            w = 0;
            h = 0;
        }
        if (w < 0) w = 0;
        if (h < 0) h = 0;
        if (w != cw || h != ch) {
            cw = w;
            ch = h;
            contentDirty = true;
            content = null;
            try {
                sizeChanged(w, h);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /** Called by ScreenCodec when the item record is written: updates the off-screen image the host shows. */
    void render() {
        measure();
        if (cw <= 0 || ch <= 0) return;
        if (content == null) {
            content = Image.createImage(cw, ch);
            contentDirty = true;
        }
        if (contentDirty) {
            contentDirty = false;
            try {
                Graphics g = content.getGraphics();
                g.setColor(0xFFFFFF);
                g.fillRect(0, 0, cw, ch);
                g.setColor(0);
                paint(g, cw, ch);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    void encode() {
        render();
        ScreenCodec.w(cw < 0 ? 0 : cw);
        ScreenCodec.w(ch < 0 ? 0 : ch);
        ScreenCodec.w(content == null ? 0 : content.peer);
        ScreenCodec.w(getInteractionModes());
    }

    void onShow() {
        if (shown) return;
        shown = true;
        try {
            showNotify();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void onHide() {
        if (!shown) return;
        shown = false;
        try {
            hideNotify();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    int calcPrefW() {
        measure();
        return Math.max(labelW(), cw);
    }

    int calcPrefH() {
        measure();
        return labelH() + ch;
    }

    int calcMinW() {
        try {
            return Math.max(labelW(), getMinContentWidth());
        } catch (Throwable t) {
            return 0;
        }
    }

    int calcMinH() {
        try {
            return labelH() + getMinContentHeight();
        } catch (Throwable t) {
            return labelH();
        }
    }

    /** Event thread: an input event of the host (type is one of KEY_*, POINTER_*, TRAVERSE_*; p = key code or x << 16 | y or direction). */
    void onUiEvent(int type, int p) {
        try {
            switch (type) {
                case KEY_PRESS: keyPressed(p); break;
                case KEY_RELEASE: keyReleased(p); break;
                case KEY_REPEAT: keyRepeated(p); break;
                case POINTER_PRESS: pointerPressed(p >> 16, (short) p); break;
                case POINTER_RELEASE: pointerReleased(p >> 16, (short) p); break;
                case POINTER_DRAG: pointerDragged(p >> 16, (short) p); break;
                case TRAVERSE_HORIZONTAL:
                case TRAVERSE_VERTICAL: {
                    int[] vis = new int[] {0, 0, cw, ch};
                    boolean inside = traverse(p, availW(), Display.canvasH, vis);
                    jlkvm.internal.Ui.traverse0(iid, p, inside ? 1 : 0, vis[0], vis[1], vis[2], vis[3]);
                    break;
                }
                default: break;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (contentDirty) changed();
    }
}
