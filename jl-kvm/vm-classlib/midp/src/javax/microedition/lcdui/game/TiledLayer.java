/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class TiledLayer extends Layer {
    private final int cols, rows;
    private int[] cells;
    private Image image;
    private int tileW, tileH, numTiles, imgCols;
    /** Animated tile k (index -k) shows the static tile anim[k]; anim[0] unused. */
    private int[] anim = new int[4];
    private int animCount;

    public TiledLayer(int columns, int rows, Image image, int tileWidth, int tileHeight) {
        super(columns < 1 || tileWidth < 1 ? throwIae("columns / tileWidth") : columns * tileWidth,
                rows < 1 || tileHeight < 1 ? throwIae("rows / tileHeight") : rows * tileHeight);
        this.cols = columns;
        this.rows = rows;
        cells = new int[columns * rows];
        setStaticTileSet0(image, tileWidth, tileHeight);
    }

    private static int throwIae(String m) {
        throw new IllegalArgumentException(m);
    }

    public void setStaticTileSet(Image image, int tileWidth, int tileHeight) {
        int old = numTiles;
        int w = image == null ? 0 : image.getWidth(), h = image == null ? 0 : image.getHeight();
        if (image == null) throw new NullPointerException();
        if (tileWidth < 1 || tileHeight < 1 || w % tileWidth != 0 || h % tileHeight != 0) throw new IllegalArgumentException("tile size");
        setStaticTileSet0(image, tileWidth, tileHeight);
        setWidthImpl(cols * tileWidth);
        setHeightImpl(rows * tileHeight);
        if (numTiles < old) {
            // fewer tiles than before: every cell is cleared and the animated tiles are deleted
            for (int i = 0; i < cells.length; i++) cells[i] = 0;
            animCount = 0;
        }
    }

    private void setStaticTileSet0(Image img, int tw, int th) {
        if (img == null) throw new NullPointerException();
        int w = img.getWidth(), h = img.getHeight();
        if (tw < 1 || th < 1 || w % tw != 0 || h % th != 0) throw new IllegalArgumentException("tile size");
        image = img;
        mask = null;
        tileW = tw;
        tileH = th;
        imgCols = w / tw;
        numTiles = imgCols * (h / th);
    }

    public int createAnimatedTile(int staticTileIndex) {
        if (staticTileIndex < 0 || staticTileIndex > numTiles) throw new IndexOutOfBoundsException("tile " + staticTileIndex);
        if (animCount + 1 >= anim.length) {
            int[] g = new int[anim.length * 2];
            System.arraycopy(anim, 0, g, 0, anim.length);
            anim = g;
        }
        anim[++animCount] = staticTileIndex;
        return -animCount;
    }

    public int getAnimatedTile(int animatedTileIndex) {
        int k = -animatedTileIndex;
        if (k < 1 || k > animCount) throw new IndexOutOfBoundsException("animated tile " + animatedTileIndex);
        return anim[k];
    }

    public void setAnimatedTile(int animatedTileIndex, int staticTileIndex) {
        int k = -animatedTileIndex;
        if (k < 1 || k > animCount) throw new IndexOutOfBoundsException("animated tile " + animatedTileIndex);
        if (staticTileIndex < 0 || staticTileIndex > numTiles) throw new IndexOutOfBoundsException("tile " + staticTileIndex);
        anim[k] = staticTileIndex;
    }

    private void checkTile(int t) {
        if (t > numTiles || -t > animCount) throw new IndexOutOfBoundsException("tile " + t);
    }

    public int getCell(int col, int row) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) throw new IndexOutOfBoundsException("cell " + col + "," + row);
        return cells[row * cols + col];
    }

    public void setCell(int col, int row, int tileIndex) {
        if (col < 0 || col >= cols || row < 0 || row >= rows) throw new IndexOutOfBoundsException("cell " + col + "," + row);
        checkTile(tileIndex);
        cells[row * cols + col] = tileIndex;
    }

    public void fillCells(int col, int row, int numCols, int numRows, int tileIndex) {
        if (numCols < 0 || numRows < 0) throw new IllegalArgumentException("size");
        if (col < 0 || col > cols || row < 0 || row > rows || col + numCols > cols || row + numRows > rows) throw new IndexOutOfBoundsException("region");
        checkTile(tileIndex);
        for (int r = row; r < row + numRows; r++) for (int c = col; c < col + numCols; c++) cells[r * cols + c] = tileIndex;
    }

    public final int getCellWidth() {
        return tileW;
    }

    public final int getCellHeight() {
        return tileH;
    }

    public final int getColumns() {
        return cols;
    }

    public final int getRows() {
        return rows;
    }

    /** Only the cells that touch the clip are drawn; no object is allocated. */
    public final void paint(Graphics g) {
        if (g == null) throw new NullPointerException();
        if (!visible) return;
        int cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
        if (cw <= 0 || ch <= 0) return;
        if (cx + cw - 1 - x < 0 || cy + ch - 1 - y < 0) return;
        int c0 = cx - x < 0 ? 0 : (cx - x) / tileW, r0 = cy - y < 0 ? 0 : (cy - y) / tileH;
        int c1 = (cx + cw - 1 - x) / tileW, r1 = (cy + ch - 1 - y) / tileH;
        if (c1 >= cols) c1 = cols - 1;
        if (r1 >= rows) r1 = rows - 1;
        for (int r = r0; r <= r1; r++) {
            int base = r * cols;
            for (int c = c0; c <= c1; c++) {
                int t = cells[base + c];
                if (t < 0) t = anim[-t];
                if (t == 0) continue;
                t--;
                g.drawRegion(image, (t % imgCols) * tileW, (t / imgCols) * tileH, tileW, tileH, Sprite.TRANS_NONE, x + c * tileW, y + r * tileH,
                        Graphics.TOP | Graphics.LEFT);
            }
        }
    }

    // ---------------------------------------------------------------- for Sprite collision detection

    /** Lazily built alpha mask of the tile image (replaced with the tile set). */
    private int[] mask;

    /** Pixel test of a sprite against the tile {@code tile} (1-based) shown in cell (c, r): region [x0,x1) x [y0,y1) in painter coordinates. */
    final boolean pixelHit(int tile, int c, int r, Sprite s, int x0, int y0, int x1, int y1) {
        int[] m = mask;
        if (m == null) mask = m = PixelMask.build(image);
        int w = imgCols * tileW;
        int bx = ((tile - 1) % imgCols) * tileW, by = ((tile - 1) / imgCols) * tileH;
        int cellX = x + c * tileW, cellY = y + r * tileH;
        for (int py = y0; py < y1; py++) {
            for (int px = x0; px < x1; px++) {
                if (s.opaque(px, py) && PixelMask.get(m, w, bx + px - cellX, by + py - cellY)) return true;
            }
        }
        return false;
    }

    /** Static tile (1-based, 0 = empty) shown in the cell. */
    final int staticTileAt(int col, int row) {
        int t = cells[row * cols + col];
        return t < 0 ? anim[-t] : t;
    }

    final Image tileImage() {
        return image;
    }

    final int imageColumns() {
        return imgCols;
    }
}
