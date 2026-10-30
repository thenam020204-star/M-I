/*
 * jl-kvm guest class library (MIDP 2.0 layer). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 */
package jlkvm.internal;

/** ROM-private: package-private hooks of lcdui.Canvas for the game package (javax.microedition.lcdui.game), set by Canvas's static initializer. */
public interface CanvasAccess {
    void setTap(Object canvas, KeyTap tap);

    /** Nokia FullCanvas: full screen from the constructor on (the size the application knows is the full one, no sizeChanged at the first show). */
    void initFull(Object canvas);
}
