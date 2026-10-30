/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;
import jlkvm.internal.Device;

/**
 * Pixels live in the host (peer = host raster handle owned by this object, released by the collector). When the profile says
 * imagesCountInHeap the pixel memory is also charged to the guest heap by holding a byte[] of the same size.
 */
public class Image {
    int peer;
    private final int width, height;
    private final boolean mutable;
    private byte[] heapCharge;

    private Image(int width, int height, boolean mutable) {
        this.width = width;
        this.height = height;
        this.mutable = mutable;
        charge();
    }

    private void charge() {
        if (Device.info0(Device.IMAGES_IN_HEAP) != 0) heapCharge = new byte[width * height * 4];
    }

    public static Image createImage(int width, int height) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("size");
        Image im = new Image(width, height, true);
        im.peer = im.create0(width, height);
        if (im.peer == 0) throw new OutOfMemoryError();
        return im;
    }

    public static Image createImage(Image source) {
        if (source == null) throw new NullPointerException();
        if (!source.mutable) return source;
        Image im = new Image(source.width, source.height, false);
        im.peer = im.copy0(source.peer, 0, 0, source.width, source.height, 0);
        return im;
    }

    public static Image createImage(String name) throws IOException {
        if (name == null) throw new NullPointerException();
        InputStream in = Image.class.getResourceAsStream(name.startsWith("/") ? name : "/" + name);
        if (in == null) throw new IOException("resource not found: " + name);
        try {
            return createImage(in);
        } finally {
            in.close();
        }
    }

    public static Image createImage(InputStream stream) throws IOException {
        if (stream == null) throw new NullPointerException();
        byte[] buf = new byte[4096];
        int n = 0;
        for (;;) {
            if (n == buf.length) {
                byte[] bigger = new byte[buf.length * 2];
                System.arraycopy(buf, 0, bigger, 0, n);
                buf = bigger;
            }
            int r = stream.read(buf, n, buf.length - n);
            if (r < 0) break;
            n += r;
        }
        Image im = decode(buf, 0, n);
        if (im == null) throw new IOException("cannot decode image");
        return im;
    }

    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        if (imageData == null) throw new NullPointerException();
        if (imageOffset < 0 || imageLength < 0 || imageOffset > imageData.length - imageLength) throw new ArrayIndexOutOfBoundsException();
        Image im = decode(imageData, imageOffset, imageLength);
        if (im == null) throw new IllegalArgumentException("cannot decode image");
        return im;
    }

    private static Image decode(byte[] data, int off, int len) {
        int p = decode0(data, off, len);
        if (p == 0) return null;
        int wh = size0(p);
        Image im = new Image(wh >>> 16, wh & 0xFFFF, false);
        im.peer = p;
        own0(p, im);   // the host raster is released when this object dies
        return im;
    }

    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
        if (image == null) throw new NullPointerException();
        if (transform < 0 || transform > 7) throw new IllegalArgumentException("transform");
        if (x < 0 || y < 0 || width <= 0 || height <= 0 || x + width > image.width || y + height > image.height || x + width < 0 || y + height < 0)
            throw new IllegalArgumentException("region");
        boolean swap = transform >= 4;
        Image im = new Image(swap ? height : width, swap ? width : height, false);
        im.peer = im.copy0(image.peer, x, y, width, height, transform);
        return im;
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        if (rgb == null) throw new NullPointerException();
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("size");
        if (width * height > rgb.length) throw new ArrayIndexOutOfBoundsException();
        Image im = new Image(width, height, false);
        im.peer = im.fromRgb0(rgb, width, height, processAlpha);
        return im;
    }

    public Graphics getGraphics() {
        if (!mutable) throw new IllegalStateException("immutable image");
        return new Graphics(this, false);
    }

    public boolean isMutable() {
        return mutable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        if (rgbData == null) throw new NullPointerException();
        if (width < 0 || height < 0 || x < 0 || y < 0 || x + width > this.width || y + height > this.height) throw new IllegalArgumentException("region");
        if (width == 0 || height == 0) return;
        int abs = scanlength < 0 ? -scanlength : scanlength;
        if (abs < width) throw new IllegalArgumentException("scanlength");
        long lo = offset + (scanlength < 0 ? (long) (height - 1) * scanlength : 0L);
        long hi = offset + (scanlength > 0 ? (long) (height - 1) * scanlength : 0L) + width - 1;
        if (lo < 0 || hi >= rgbData.length) throw new ArrayIndexOutOfBoundsException();
        getRGB0(peer, rgbData, offset, scanlength, x, y, width, height);
    }

    private native int create0(int w, int h);

    private static native int decode0(byte[] data, int off, int len);

    private native int fromRgb0(int[] rgb, int w, int h, boolean alpha);

    private native int copy0(int srcPeer, int x, int y, int w, int h, int transform);

    private static native int size0(int peer);

    private static native void own0(int peer, Image owner);

    private static native void getRGB0(int peer, int[] rgb, int off, int scan, int x, int y, int w, int h);
}
