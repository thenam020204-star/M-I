/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

public class LayerManager {
    private Layer[] layers = new Layer[8];
    private int n;
    private int viewX, viewY, viewW = Integer.MAX_VALUE, viewH = Integer.MAX_VALUE;

    public LayerManager() {
    }

    public void append(Layer l) {
        if (l == null) throw new NullPointerException();
        removeImpl(l);
        addAt(l, n);
    }

    public void insert(Layer l, int index) {
        if (l == null) throw new NullPointerException();
        removeImpl(l);
        if (index < 0 || index > n) throw new IndexOutOfBoundsException("index " + index);
        addAt(l, index);
    }

    public Layer getLayerAt(int index) {
        if (index < 0 || index >= n) throw new IndexOutOfBoundsException("index " + index);
        return layers[index];
    }

    public int getSize() {
        return n;
    }

    public void remove(Layer l) {
        if (l == null) throw new NullPointerException();
        removeImpl(l);
    }

    public void setViewWindow(int x, int y, int width, int height) {
        if (width < 0 || height < 0) throw new IllegalArgumentException("size");
        viewX = x;
        viewY = y;
        viewW = width;
        viewH = height;
    }

    /** Paints the visible layers, last index first, so that index 0 is on top; the view window appears at (x, y) of g. g's state is restored. */
    public void paint(Graphics g, int x, int y) {
        if (g == null) throw new NullPointerException();
        int cx = g.getClipX(), cy = g.getClipY(), cw = g.getClipWidth(), ch = g.getClipHeight();
        int dx = x - viewX, dy = y - viewY;
        g.translate(dx, dy);
        g.clipRect(viewX, viewY, viewW > (1 << 24) ? (1 << 24) : viewW, viewH > (1 << 24) ? (1 << 24) : viewH);
        for (int i = n - 1; i >= 0; i--) {
            Layer l = layers[i];
            if (l.visible) l.paint(g);
        }
        g.translate(-dx, -dy);
        g.setClip(cx, cy, cw, ch);
    }

    private void addAt(Layer l, int index) {
        if (n == layers.length) {
            Layer[] g = new Layer[n * 2];
            System.arraycopy(layers, 0, g, 0, n);
            layers = g;
        }
        System.arraycopy(layers, index, layers, index + 1, n - index);
        layers[index] = l;
        n++;
    }

    private void removeImpl(Layer l) {
        for (int i = 0; i < n; i++) {
            if (layers[i] == l) {
                System.arraycopy(layers, i + 1, layers, i, n - i - 1);
                layers[--n] = null;
                return;
            }
        }
    }
}
