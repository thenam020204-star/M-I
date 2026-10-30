package jlkvm.host.ui;

import java.io.IOException;

/** Creates surfaces and answers font questions; one per host (software on desktop, Bitmap based on Android). */
public interface SurfaceFactory {
    /** Mutable, opaque white. */
    Surface create(int w, int h);
    /** Mutable surface with a per-pixel alpha channel, every pixel {@code argb} (Nokia DirectUtils.createImage(w, h, argb), T28); {@link Surface#hasAlpha} is true. */
    Surface createArgb(int w, int h, int argb);
    /** Immutable surface from encoded image bytes (PNG required by MIDP 2.0; hosts may add JPEG/GIF). @throws IOException when the data cannot be decoded. */
    Surface decode(byte[] data, int off, int len) throws IOException;
    /** Immutable surface from 0xAARRGGBB pixels; without {@code alpha} the alpha byte is ignored (opaque). */
    Surface fromRgb(int[] argb, int w, int h, boolean alpha);
    /** (height << 16) | baseline of the font {@code key}. */
    int fontMetrics(int key);
    int charWidth(int key, char c);
    int charsWidth(int key, char[] c, int off, int len);
}
