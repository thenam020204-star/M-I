package javax.microedition.lcdui;

/** TEMPORARY (T23): mutable images only, pixels live in the host (handle table). */
public class Image {
    int peer;
    private final int width, height;
    private Image(int w, int h) { width = w; height = h; peer = create0(w, h); }
    public static Image createImage(int w, int h) {
        if (w <= 0 || h <= 0) throw new IllegalArgumentException();
        return new Image(w, h);
    }
    public Graphics getGraphics() { return new Graphics(this); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMutable() { return true; }
    public void getRGB(int[] rgb, int off, int scan, int x, int y, int w, int h) { getRGB0(peer, rgb, off, scan, x, y, w, h); }
    private static native int create0(int w, int h);
    private static native void getRGB0(int peer, int[] rgb, int off, int scan, int x, int y, int w, int h);
}
