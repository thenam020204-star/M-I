package javax.microedition.lcdui;

/** TEMPORARY (T22 writes the real EventLoop/Canvas): paints happen on the Display's event thread. */
public abstract class Canvas extends Displayable {
    private boolean pending;
    private Graphics g;
    protected Canvas() { }
    protected abstract void paint(Graphics g);
    protected void showNotify() { }
    protected void hideNotify() { }
    public int getWidth() { return 240; }
    public int getHeight() { return 320; }
    public final void repaint() {
        synchronized (this) {
            if (pending) return;
            pending = true;
        }
        Display.post(this);
    }
    public final void serviceRepaints() {
        synchronized (this) {
            while (pending) {
                try { wait(); } catch (InterruptedException e) { return; }
            }
        }
    }
    void doPaint() {
        try {
            if (g == null) g = Image.createImage(getWidth(), getHeight()).getGraphics();
            paint(g);
        } finally {
            synchronized (this) {
                pending = false;
                notifyAll();
            }
        }
    }
    void shown() { showNotify(); }
}
