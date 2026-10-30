/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;

public abstract class Layer {
    int x, y, width, height;
    boolean visible = true;

    Layer(int width, int height) {
        setWidthImpl(width);
        setHeightImpl(height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final boolean isVisible() {
        return visible;
    }

    public abstract void paint(Graphics g);

    void setWidthImpl(int w) {
        if (w < 0) throw new IllegalArgumentException("width");
        width = w;
    }

    void setHeightImpl(int h) {
        if (h < 0) throw new IllegalArgumentException("height");
        height = h;
    }
}
