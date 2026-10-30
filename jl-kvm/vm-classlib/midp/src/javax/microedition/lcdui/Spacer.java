/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class Spacer extends Item {
    private int minW, minH;

    public Spacer(int minWidth, int minHeight) {
        super(null);
        if (minWidth < 0 || minHeight < 0) throw new IllegalArgumentException("size");
        minW = minWidth;
        minH = minHeight;
    }

    public void setMinimumSize(int minWidth, int minHeight) {
        if (minWidth < 0 || minHeight < 0) throw new IllegalArgumentException("size");
        minW = minWidth;
        minH = minHeight;
        changed();
    }

    public void addCommand(Command cmd) {
        throw new IllegalStateException("Spacer cannot have commands");
    }

    public void setDefaultCommand(Command cmd) {
        throw new IllegalStateException("Spacer cannot have commands");
    }

    public void setLabel(String label) {
        throw new IllegalStateException("Spacer cannot have a label");
    }

    int uiKind() {
        return ScreenCodec.I_SPACER;
    }

    void encode() {
    }

    int calcPrefW() {
        return minW;
    }

    int calcPrefH() {
        return minH;
    }

    int calcMinW() {
        return minW;
    }

    int calcMinH() {
        return minH;
    }
}
