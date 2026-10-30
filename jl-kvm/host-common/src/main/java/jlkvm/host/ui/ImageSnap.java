package jlkvm.host.ui;

/** A copy of the pixels of a guest Image taken on the vm-thread, so that the UI thread never touches a handle the guest may free. */
public final class ImageSnap {
    public final int w, h;
    /** 0xAARRGGBB, w * h entries. */
    public final int[] argb;

    public ImageSnap(int w, int h, int[] argb) {
        this.w = w;
        this.h = h;
        this.argb = argb;
    }

    /** @return the pixels of {@code s}, or null when {@code s} is null or empty. */
    public static ImageSnap of(Surface s) {
        if (s == null) return null;
        int w = s.width(), h = s.height();
        if (w <= 0 || h <= 0 || (long) w * h > 4000000L) return null;
        int[] px = new int[w * h];
        s.getRgb(px, 0, w, 0, 0, w, h);
        return new ImageSnap(w, h, px);
    }
}
