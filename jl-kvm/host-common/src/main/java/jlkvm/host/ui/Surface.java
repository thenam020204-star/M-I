package jlkvm.host.ui;

/**
 * A drawable pixel surface (an lcdui Image or the Canvas back buffer). This is the common native interface of the lcdui layer
 * (PLAN T23): host-desktop and the portable {@link SoftRaster} implement it in software; host-android may implement it on
 * the Android Bitmap / Canvas classes (JL-Mod backend) - NOT done yet, see docs/lcdui-natives.md.
 * <p>
 * Every drawing method gets absolute coordinates (the guest already applied translate and anchor) and the absolute clip box
 * [cx0, cx1) x [cy0, cy1); the implementation further clips to the surface. Colours are 0xRRGGBB (alpha ignored) except where
 * stated. Pixel format of {@link #getRgb} is 0xAARRGGBB, non-premultiplied; surfaces of mutable images are always opaque.
 */
public interface Surface {
    int width();
    int height();
    /** true when the surface carries per-pixel alpha (PNG with alpha, createRGBImage(processAlpha)). */
    boolean hasAlpha();

    void drawLine(int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int stroke);
    void fillRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h);
    void drawRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int stroke);
    void drawRoundRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int aw, int ah, int stroke);
    void fillRoundRect(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int aw, int ah);
    void drawArc(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int start, int arc, int stroke);
    void fillArc(int rgb, int cx0, int cy0, int cx1, int cy1, int x, int y, int w, int h, int start, int arc);
    void fillTriangle(int rgb, int cx0, int cy0, int cx1, int cy1, int x1, int y1, int x2, int y2, int x3, int y3);
    /** (x, y) = top-left of the text box. {@code fontKey} = face | style | size of lcdui.Font. */
    void drawChars(int rgb, int cx0, int cy0, int cx1, int cy1, int fontKey, char[] c, int off, int len, int x, int y);
    /** Draws the region (sx, sy, sw, sh) of {@code src} transformed by {@code transform} (0..7, Sprite.TRANS_*), top-left at (dx, dy), alpha blended. */
    void drawSurface(int cx0, int cy0, int cx1, int cy1, Surface src, int sx, int sy, int sw, int sh, int transform, int dx, int dy);
    void drawRgb(int cx0, int cy0, int cx1, int cy1, int[] rgb, int off, int scan, int x, int y, int w, int h, boolean alpha);
    /** Copies a region of this surface onto itself (overlap-safe). */
    void copyArea(int cx0, int cy0, int cx1, int cy1, int sx, int sy, int w, int h, int dx, int dy);
    void getRgb(int[] out, int off, int scan, int x, int y, int w, int h);
    /** New immutable surface holding the transformed region. */
    Surface copyRegion(int x, int y, int w, int h, int transform);
}
