/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal;

/**
 * ROM-private: package-private hooks of lcdui.Graphics / lcdui.Image for the Nokia UI layer (com.nokia.mid.ui.DirectGraphics), set by
 * the static initialiser of Graphics. Parameters are Objects so that this interface depends on no lcdui class.
 */
public interface LcduiAccess {
    /** Current colour of a Graphics as 0xAARRGGBB (alpha 255 unless Nokia setARGBColor set another). */
    int argb(Object graphics);

    /** Sets the colour of a Graphics from 0xAARRGGBB: the RGB part as Graphics.setColor does, plus the alpha used by every later primitive until the next setColor. */
    void setArgb(Object graphics, int argb);

    /** Per-Graphics slot of the Nokia layer (the DirectGraphics wrapper, so that getDirectGraphics allocates once per Graphics). */
    Object ext(Object graphics);

    void setExt(Object graphics, Object ext);

    /** Pixel width / height of the surface a Graphics draws on. */
    int targetWidth(Object graphics);

    int targetHeight(Object graphics);

    /** Copies the 0xAARRGGBB pixels of the surface at (x, y) (Graphics coordinates, the translation is added) into out; the region must lie inside the surface. */
    void getRgb(Object graphics, int[] out, int off, int scan, int x, int y, int w, int h);

    /** New MUTABLE Image with an alpha channel, every pixel {@code argb}. */
    Object newArgbImage(int w, int h, int argb);
}
