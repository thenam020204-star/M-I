/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Image;

/**
 * Package-private: one bit per pixel for pixel-level collision detection, set when the pixel is fully opaque (alpha 255; MicroEmulator and
 * FreeJ2ME agree, the Sun reference code names a FULLY_OPAQUE_ALPHA; UNVERIFIED on devices).
 */
final class PixelMask {
    private PixelMask() {
    }

    static int[] build(Image img) {
        int w = img.getWidth(), h = img.getHeight();
        int[] bits = new int[(w * h + 31) >> 5];
        int[] row = new int[w];
        for (int y = 0; y < h; y++) {
            img.getRGB(row, 0, w, 0, y, w, 1);
            for (int x = 0; x < w; x++) {
                if ((row[x] >>> 24) == 0xFF) {
                    int i = y * w + x;
                    bits[i >> 5] |= 1 << (i & 31);
                }
            }
        }
        return bits;
    }

    static boolean get(int[] bits, int w, int x, int y) {
        int i = y * w + x;
        return (bits[i >> 5] & (1 << (i & 31))) != 0;
    }
}
