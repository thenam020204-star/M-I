/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Model part of Item (label, layout, commands, sizes). Rendering and layout are T24. */
public abstract class Item {
    public static final int LAYOUT_DEFAULT = 0, LAYOUT_LEFT = 1, LAYOUT_RIGHT = 2, LAYOUT_CENTER = 3, LAYOUT_TOP = 16, LAYOUT_BOTTOM = 32,
            LAYOUT_VCENTER = 48, LAYOUT_NEWLINE_BEFORE = 256, LAYOUT_NEWLINE_AFTER = 512, LAYOUT_SHRINK = 1024, LAYOUT_VSHRINK = 4096,
            LAYOUT_EXPAND = 2048, LAYOUT_VEXPAND = 8192, LAYOUT_2 = 16384;
    public static final int PLAIN = 0, HYPERLINK = 1, BUTTON = 2;

    String label;
    int layout;
    int prefW = -1, prefH = -1;
    Command defaultCommand;
    ItemCommandListener itemListener;
    java.util.Vector commands = new java.util.Vector();
    /** The Form / Alert that contains this item (set by T24 classes). */
    Screen owner;

    Item(String label) {
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        if ((layout & ~(LAYOUT_2 | LAYOUT_VEXPAND | LAYOUT_VSHRINK | LAYOUT_EXPAND | LAYOUT_SHRINK | LAYOUT_NEWLINE_AFTER
                | LAYOUT_NEWLINE_BEFORE | LAYOUT_VCENTER | LAYOUT_CENTER)) != 0) throw new IllegalArgumentException("layout");
        this.layout = layout;
    }

    public void addCommand(Command cmd) {
        if (cmd == null) throw new NullPointerException();
        if (!commands.contains(cmd)) commands.addElement(cmd);
    }

    public void removeCommand(Command cmd) {
        if (cmd == null) return;
        commands.removeElement(cmd);
        if (cmd == defaultCommand) defaultCommand = null;
    }

    public void setItemCommandListener(ItemCommandListener l) {
        itemListener = l;
    }

    public int getPreferredWidth() {
        return prefW >= 0 ? prefW : 0;
    }

    public int getPreferredHeight() {
        return prefH >= 0 ? prefH : 0;
    }

    public int getMinimumWidth() {
        return 0;
    }

    public int getMinimumHeight() {
        return 0;
    }

    public void setPreferredSize(int width, int height) {
        if (width < -1 || height < -1) throw new IllegalArgumentException("size");
        prefW = width;
        prefH = height;
    }

    public void setDefaultCommand(Command cmd) {
        if (cmd == null) {
            defaultCommand = null;
            return;
        }
        if (!commands.contains(cmd)) commands.addElement(cmd);
        defaultCommand = cmd;
    }

    public void notifyStateChanged() {
        if (owner == null) throw new IllegalStateException("not owned by a Form");
    }
}
