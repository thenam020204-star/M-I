package javax.microedition.lcdui;

/** TEMPORARY (T23 writes the real Graphics): colour + fillRect on an Image. */
public class Graphics {
    private final Image image;
    private int color;
    Graphics(Image image) { this.image = image; }
    public void setColor(int rgb) { color = rgb & 0xFFFFFF; }
    public int getColor() { return color; }
    public void fillRect(int x, int y, int w, int h) { fillRect0(image.peer, color, x, y, w, h); }
    private static native void fillRect0(int peer, int rgb, int x, int y, int w, int h);
}
