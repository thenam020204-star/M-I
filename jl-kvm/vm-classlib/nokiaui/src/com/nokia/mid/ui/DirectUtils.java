/*
 * jl-kvm guest class library (Nokia UI layer, API per tools/api-sigs/db/s40dp2-nokia-ui.json). Origin: original code written for jl-kvm
 * (PLAN D12, T28); behaviour per the Nokia UI API 1.1 javadoc (S40 DP2.0 SDK 1.1), open points UNVERIFIED (docs/nokia.md). License: Apache-2.0.
 */
package com.nokia.mid.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import jlkvm.internal.LcduiAccess;
import jlkvm.internal.nokia.Lcdui;

public class DirectUtils {
    private DirectUtils() {
    }

    /** The same graphics context; the wrapper is made once per Graphics (no allocation per paint). */
    public static DirectGraphics getDirectGraphics(Graphics g) {
        if (g == null) throw new NullPointerException();
        LcduiAccess a = Lcdui.get();
        Object e = a.ext(g);
        if (e == null) {
            e = new DirectGraphicsImp(g, a);
            a.setExt(g, e);
        }
        return (DirectGraphics) e;
    }

    /** Mutable image, every pixel {@code ARGBcolor} (alpha 0 = transparent). */
    public static Image createImage(int width, int height, int ARGBcolor) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("size");
        return (Image) Lcdui.get().newArgbImage(width, height, ARGBcolor);
    }

    /** Like Image.createImage(byte[], int, int) but mutable (keeps the alpha of the encoded image). */
    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        Image src = Image.createImage(imageData, imageOffset, imageLength);
        Image im = (Image) Lcdui.get().newArgbImage(src.getWidth(), src.getHeight(), 0);
        im.getGraphics().drawImage(src, 0, 0, Graphics.TOP | Graphics.LEFT);
        return im;
    }
}
