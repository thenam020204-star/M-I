/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Sprite extends Layer {
    public static final int TRANS_NONE = 0, TRANS_ROT90 = 5, TRANS_ROT180 = 3, TRANS_ROT270 = 6, TRANS_MIRROR = 2, TRANS_MIRROR_ROT90 = 7,
            TRANS_MIRROR_ROT180 = 1, TRANS_MIRROR_ROT270 = 4;

    private Image image;
    private int imgW, imgH;
    /** Frame size of the (untransformed) source. */
    private int srcW, srcH, imgCols, rawFrames;
    private int[] seq;
    private int frameIdx;
    /** Reference pixel in untransformed frame coordinates. */
    private int refX, refY;
    private int t;
    /** Collision rectangle in untransformed frame coordinates, and its image under the transform (relative to the sprite's top-left). */
    private int colX, colY, colW, colH;
    private int tcX, tcY, tcW, tcH;
    /** Lazily built alpha mask of {@link #image}. */
    private int[] mask;

    public Sprite(Image image) {
        super(image.getWidth(), image.getHeight());
        init(image, image.getWidth(), image.getHeight());
    }

    public Sprite(Image image, int frameWidth, int frameHeight) {
        super(check(image, frameWidth, frameHeight), frameHeight);
        init(image, frameWidth, frameHeight);
    }

    public Sprite(Sprite s) {
        super(s.width, s.height);
        image = s.image;
        imgW = s.imgW;
        imgH = s.imgH;
        srcW = s.srcW;
        srcH = s.srcH;
        imgCols = s.imgCols;
        rawFrames = s.rawFrames;
        seq = new int[s.seq.length];
        System.arraycopy(s.seq, 0, seq, 0, seq.length);
        frameIdx = s.frameIdx;
        refX = s.refX;
        refY = s.refY;
        t = s.t;
        colX = s.colX;
        colY = s.colY;
        colW = s.colW;
        colH = s.colH;
        tcX = s.tcX;
        tcY = s.tcY;
        tcW = s.tcW;
        tcH = s.tcH;
        mask = s.mask;
        x = s.x;
        y = s.y;
        visible = s.visible;
    }

    private static int check(Image img, int fw, int fh) {
        if (img == null) throw new NullPointerException();
        if (fw < 1 || fh < 1 || img.getWidth() % fw != 0 || img.getHeight() % fh != 0) throw new IllegalArgumentException("frame size");
        return fw;
    }

    private void init(Image img, int fw, int fh) {
        check(img, fw, fh);
        image = img;
        imgW = img.getWidth();
        imgH = img.getHeight();
        srcW = fw;
        srcH = fh;
        imgCols = imgW / fw;
        rawFrames = imgCols * (imgH / fh);
        seq = defaultSequence(rawFrames);
        frameIdx = 0;
        mask = null;
        t = TRANS_NONE;
        refX = refY = 0;
        colX = colY = 0;
        colW = fw;
        colH = fh;
        computeBounds();
    }

    private static int[] defaultSequence(int n) {
        int[] s = new int[n];
        for (int i = 0; i < n; i++) s[i] = i;
        return s;
    }

    // ---------------------------------------------------------------- geometry

    /** x of the point (px, py) of a w x h frame after transform tr. */
    private static int tx(int px, int py, int tr, int w, int h) {
        switch (tr) {
            case TRANS_ROT90: case TRANS_MIRROR_ROT90: return h - py - 1;
            case TRANS_ROT180: case TRANS_MIRROR: return w - px - 1;
            case TRANS_ROT270: case TRANS_MIRROR_ROT270: return py;
            default: return px;
        }
    }

    private static int ty(int px, int py, int tr, int w, int h) {
        switch (tr) {
            case TRANS_ROT90: case TRANS_MIRROR_ROT270: return px;
            case TRANS_ROT180: case TRANS_MIRROR_ROT180: return h - py - 1;
            case TRANS_ROT270: case TRANS_MIRROR_ROT90: return w - px - 1;
            default: return py;
        }
    }

    /** Inverse transform: untransformed x / y of the pixel (lx, ly) of the transformed frame. */
    private static int ux(int lx, int ly, int tr, int w, int h) {
        switch (tr) {
            case TRANS_ROT90: case TRANS_MIRROR_ROT270: return ly;
            case TRANS_ROT180: case TRANS_MIRROR: return w - 1 - lx;
            case TRANS_ROT270: case TRANS_MIRROR_ROT90: return w - 1 - ly;
            default: return lx;
        }
    }

    private static int uy(int lx, int ly, int tr, int w, int h) {
        switch (tr) {
            case TRANS_ROT90: case TRANS_MIRROR_ROT90: return h - 1 - lx;
            case TRANS_ROT180: case TRANS_MIRROR_ROT180: return h - 1 - ly;
            case TRANS_ROT270: case TRANS_MIRROR_ROT270: return lx;
            default: return ly;
        }
    }

    private void computeBounds() {
        boolean swap = t == TRANS_ROT90 || t == TRANS_ROT270 || t == TRANS_MIRROR_ROT90 || t == TRANS_MIRROR_ROT270;
        width = swap ? srcH : srcW;
        height = swap ? srcW : srcH;
        int x1 = tx(colX, colY, t, srcW, srcH), x2 = tx(colX + colW - 1, colY + colH - 1, t, srcW, srcH);
        int y1 = ty(colX, colY, t, srcW, srcH), y2 = ty(colX + colW - 1, colY + colH - 1, t, srcW, srcH);
        tcX = x1 < x2 ? x1 : x2;
        tcY = y1 < y2 ? y1 : y2;
        tcW = swap ? colH : colW;
        tcH = swap ? colW : colH;
    }

    public void defineReferencePixel(int inp_x, int inp_y) {
        refX = inp_x;
        refY = inp_y;
    }

    public void setRefPixelPosition(int inp_x, int inp_y) {
        x = inp_x - tx(refX, refY, t, srcW, srcH);
        y = inp_y - ty(refX, refY, t, srcW, srcH);
    }

    public int getRefPixelX() {
        return x + tx(refX, refY, t, srcW, srcH);
    }

    public int getRefPixelY() {
        return y + ty(refX, refY, t, srcW, srcH);
    }

    public void setTransform(int transform) {
        if (transform < 0 || transform > 7) throw new IllegalArgumentException("transform");
        // the reference pixel stays where it is
        x += tx(refX, refY, t, srcW, srcH) - tx(refX, refY, transform, srcW, srcH);
        y += ty(refX, refY, t, srcW, srcH) - ty(refX, refY, transform, srcW, srcH);
        t = transform;
        computeBounds();
    }

    public void defineCollisionRectangle(int inp_x, int inp_y, int width, int height) {
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        colX = inp_x;
        colY = inp_y;
        colW = width;
        colH = height;
        computeBounds();
    }

    // ---------------------------------------------------------------- frames

    public void setFrame(int sequenceIndex) {
        if (sequenceIndex < 0 || sequenceIndex >= seq.length) throw new IndexOutOfBoundsException("frame " + sequenceIndex);
        frameIdx = sequenceIndex;
    }

    public final int getFrame() {
        return frameIdx;
    }

    public int getRawFrameCount() {
        return rawFrames;
    }

    public int getFrameSequenceLength() {
        return seq.length;
    }

    public void nextFrame() {
        frameIdx = frameIdx + 1 == seq.length ? 0 : frameIdx + 1;
    }

    public void prevFrame() {
        frameIdx = frameIdx == 0 ? seq.length - 1 : frameIdx - 1;
    }

    public void setFrameSequence(int[] sequence) {
        if (sequence == null) {
            seq = defaultSequence(rawFrames);
            frameIdx = 0;
            return;
        }
        if (sequence.length < 1) throw new IllegalArgumentException("empty sequence");
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < 0 || sequence[i] >= rawFrames) throw new ArrayIndexOutOfBoundsException("frame " + sequence[i]);
        }
        seq = new int[sequence.length];
        System.arraycopy(sequence, 0, seq, 0, sequence.length);
        frameIdx = 0;
    }

    public void setImage(Image img, int frameWidth, int frameHeight) {
        check(img, frameWidth, frameHeight);
        int frames = (img.getWidth() / frameWidth) * (img.getHeight() / frameHeight);
        boolean keep = frames >= rawFrames;
        int oldW = srcW, oldH = srcH;
        int refPx = x + tx(refX, refY, t, oldW, oldH), refPy = y + ty(refX, refY, t, oldW, oldH);
        int[] oldSeq = seq;
        int oldIdx = frameIdx;
        image = img;
        imgW = img.getWidth();
        imgH = img.getHeight();
        srcW = frameWidth;
        srcH = frameHeight;
        imgCols = imgW / frameWidth;
        rawFrames = frames;
        mask = null;
        if (keep) {
            seq = oldSeq;
            frameIdx = oldIdx;
        } else {
            seq = defaultSequence(frames);
            frameIdx = 0;
        }
        if (oldW != frameWidth || oldH != frameHeight) {
            // new frame size: default collision rectangle, the reference pixel keeps its place on the painter
            colX = colY = 0;
            colW = frameWidth;
            colH = frameHeight;
            computeBounds();
            x = refPx - tx(refX, refY, t, srcW, srcH);
            y = refPy - ty(refX, refY, t, srcW, srcH);
        }
    }

    // ---------------------------------------------------------------- painting

    public final void paint(Graphics g) {
        if (g == null) throw new NullPointerException();
        if (!visible) return;
        int f = seq[frameIdx];
        g.drawRegion(image, (f % imgCols) * srcW, (f / imgCols) * srcH, srcW, srcH, t, x, y, Graphics.TOP | Graphics.LEFT);
    }

    // ---------------------------------------------------------------- collision

    /** true when the pixel (px, py) in painter coordinates is an opaque pixel inside this sprite's collision rectangle. */
    private boolean opaqueAt(int px, int py) {
        int lx = px - x, ly = py - y;
        int u = ux(lx, ly, t, srcW, srcH), v = uy(lx, ly, t, srcW, srcH);
        if (u < colX || v < colY || u >= colX + colW || v >= colY + colH) return false;
        if (u < 0 || v < 0 || u >= srcW || v >= srcH) return false;
        int f = seq[frameIdx];
        return PixelMask.get(mask(), imgW, (f % imgCols) * srcW + u, (f / imgCols) * srcH + v);
    }

    private int[] mask() {
        int[] m = mask;
        if (m == null) mask = m = PixelMask.build(image);
        return m;
    }

    public final boolean collidesWith(Sprite s, boolean pixelLevel) {
        if (s == null) throw new NullPointerException();
        if (!visible || !s.visible) return false;
        int ax0 = x + tcX, ay0 = y + tcY, ax1 = ax0 + tcW, ay1 = ay0 + tcH;
        int bx0 = s.x + s.tcX, by0 = s.y + s.tcY, bx1 = bx0 + s.tcW, by1 = by0 + s.tcH;
        int x0 = ax0 > bx0 ? ax0 : bx0, y0 = ay0 > by0 ? ay0 : by0, x1 = ax1 < bx1 ? ax1 : bx1, y1 = ay1 < by1 ? ay1 : by1;
        if (x0 >= x1 || y0 >= y1) return false;
        if (!pixelLevel) return true;
        for (int py = y0; py < y1; py++) {
            for (int px = x0; px < x1; px++) {
                if (opaqueAt(px, py) && s.opaqueAt(px, py)) return true;
            }
        }
        return false;
    }

    public final boolean collidesWith(TiledLayer layer, boolean pixelLevel) {
        if (layer == null) throw new NullPointerException();
        if (!visible || !layer.visible) return false;
        int cw = layer.getCellWidth(), ch = layer.getCellHeight();
        int ax0 = x + tcX, ay0 = y + tcY, ax1 = ax0 + tcW, ay1 = ay0 + tcH;
        // the cells that overlap the collision rectangle
        int lx = layer.getX(), ly = layer.getY();
        if (ax0 >= lx + layer.getWidth() || ay0 >= ly + layer.getHeight() || ax1 <= lx || ay1 <= ly || ax0 >= ax1 || ay0 >= ay1) return false;
        int c0 = ax0 <= lx ? 0 : (ax0 - lx) / cw, c1 = (ax1 - 1 - lx) / cw;
        int r0 = ay0 <= ly ? 0 : (ay0 - ly) / ch, r1 = (ay1 - 1 - ly) / ch;
        if (c1 >= layer.getColumns()) c1 = layer.getColumns() - 1;
        if (r1 >= layer.getRows()) r1 = layer.getRows() - 1;
        for (int r = r0; r <= r1; r++) {
            for (int c = c0; c <= c1; c++) {
                int tile = layer.staticTileAt(c, r);
                if (tile == 0) continue;
                if (!pixelLevel) return true;
                int cx0 = lx + c * cw, cy0 = ly + r * ch;
                int x0 = ax0 > cx0 ? ax0 : cx0, y0 = ay0 > cy0 ? ay0 : cy0, x1 = ax1 < cx0 + cw ? ax1 : cx0 + cw, y1 = ay1 < cy0 + ch ? ay1 : cy0 + ch;
                if (layer.pixelHit(tile, c, r, this, x0, y0, x1, y1)) return true;
            }
        }
        return false;
    }

    public final boolean collidesWith(Image img, int ix, int iy, boolean pixelLevel) {
        if (img == null) throw new NullPointerException();
        if (!visible) return false;
        int ax0 = x + tcX, ay0 = y + tcY, ax1 = ax0 + tcW, ay1 = ay0 + tcH;
        int bx1 = ix + img.getWidth(), by1 = iy + img.getHeight();
        int x0 = ax0 > ix ? ax0 : ix, y0 = ay0 > iy ? ay0 : iy, x1 = ax1 < bx1 ? ax1 : bx1, y1 = ay1 < by1 ? ay1 : by1;
        if (x0 >= x1 || y0 >= y1) return false;
        if (!pixelLevel) return true;
        int[] m = PixelMask.build(img);
        int w = img.getWidth();
        for (int py = y0; py < y1; py++) {
            for (int px = x0; px < x1; px++) {
                if (opaqueAt(px, py) && PixelMask.get(m, w, px - ix, py - iy)) return true;
            }
        }
        return false;
    }

    /** Used by TiledLayer: pixel test of this sprite against a tile cell (rectangle [x0,x1) x [y0,y1) in painter coordinates). */
    final boolean opaque(int px, int py) {
        return opaqueAt(px, py);
    }
}
