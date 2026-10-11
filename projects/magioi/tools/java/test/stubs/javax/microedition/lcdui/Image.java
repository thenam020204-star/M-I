package javax.microedition.lcdui;
/** Stub toi thieu de test BitmapFont tren desktop. */
public class Image {
    public final int w, h; public final int[] px;
    Image(int w, int h, int[] px) { this.w = w; this.h = h; this.px = px; }
    public static Image createRGBImage(int[] rgb, int w, int h, boolean alpha) {
        return new Image(w, h, (int[]) rgb.clone());
    }
    public int getWidth() { return w; }
    public int getHeight() { return h; }
}
