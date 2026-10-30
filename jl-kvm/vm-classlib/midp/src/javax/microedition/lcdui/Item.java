/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public abstract class Item {
    public static final int LAYOUT_DEFAULT = 0, LAYOUT_LEFT = 1, LAYOUT_RIGHT = 2, LAYOUT_CENTER = 3, LAYOUT_TOP = 16, LAYOUT_BOTTOM = 32,
            LAYOUT_VCENTER = 48, LAYOUT_NEWLINE_BEFORE = 256, LAYOUT_NEWLINE_AFTER = 512, LAYOUT_SHRINK = 1024, LAYOUT_VSHRINK = 4096,
            LAYOUT_EXPAND = 2048, LAYOUT_VEXPAND = 8192, LAYOUT_2 = 16384;
    public static final int PLAIN = 0, HYPERLINK = 1, BUTTON = 2;

    /** Id in the host screen protocol. */
    final int iid = Ids.next();
    String label;
    int layout;
    int prefW = -1, prefH = -1;
    Command defaultCommand;
    ItemCommandListener itemListener;
    final java.util.Vector commands = new java.util.Vector();
    /** The Form / Alert that contains this item. */
    Screen owner;

    Item(String label) {
        this.label = label;
    }

    public void setLabel(String label) {
        this.label = label;
        changed();
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
        changed();
    }

    public void addCommand(Command cmd) {
        if (cmd == null) throw new NullPointerException();
        synchronized (commands) {
            if (!commands.contains(cmd)) commands.addElement(cmd);
        }
        changed();
    }

    public void removeCommand(Command cmd) {
        if (cmd == null) return;
        synchronized (commands) {
            commands.removeElement(cmd);
        }
        if (cmd == defaultCommand) defaultCommand = null;
        changed();
    }

    public void setItemCommandListener(ItemCommandListener l) {
        itemListener = l;
    }

    public int getPreferredWidth() {
        return prefW >= 0 ? prefW : calcPrefW();
    }

    public int getPreferredHeight() {
        return prefH >= 0 ? prefH : calcPrefH();
    }

    public int getMinimumWidth() {
        return calcMinW();
    }

    public int getMinimumHeight() {
        return calcMinH();
    }

    public void setPreferredSize(int width, int height) {
        if (width < -1 || height < -1) throw new IllegalArgumentException("size");
        prefW = width;
        prefH = height;
        changed();
    }

    public void setDefaultCommand(Command cmd) {
        if (cmd == null) {
            defaultCommand = null;
            changed();
            return;
        }
        synchronized (commands) {
            if (!commands.contains(cmd)) commands.addElement(cmd);
        }
        defaultCommand = cmd;
        changed();
    }

    /** MIDP: tells the ItemStateListener of the containing Form (delivered on the event thread, D13). */
    public void notifyStateChanged() {
        final Screen o = owner;
        if (!(o instanceof Form)) throw new IllegalStateException("not owned by a Form");
        jlkvm.internal.EventLoop.post(new Runnable() {
            public void run() {
                if (owner == o) o.itemChanged(Item.this);
            }
        });
    }

    // ---------------------------------------------------------------- package-private: model <-> host screen protocol

    /** The item's model changed: the screen that contains it is re-sent to the host. */
    void changed() {
        Screen o = owner;
        if (o != null) o.touch();
    }

    /** Kind code of the host protocol (ScreenCodec.I_*). */
    abstract int uiKind();

    /** Writes the kind specific part of the item record (ScreenCodec). */
    abstract void encode();

    Command findCommand(int cid) {
        synchronized (commands) {
            for (int i = 0; i < commands.size(); i++) {
                Command c = (Command) commands.elementAt(i);
                if (c.cid == cid) return c;
            }
        }
        return null;
    }

    /** Event thread: the host activated a command of this item. */
    void fire(Command c) {
        ItemCommandListener l = itemListener;
        if (l == null) return;
        try {
            l.commandAction(c, this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // Layout estimates (the host has the real layout; MIDP leaves the sizes to the implementation): lines wrap at the form width.
    static int availW() {
        return Display.canvasW;
    }

    int labelH() {
        return label == null || label.length() == 0 ? 0 : lines(label, Font.getDefaultFont(), availW()) * Font.getDefaultFont().getHeight();
    }

    int labelW() {
        return label == null || label.length() == 0 ? 0 : Math.min(availW(), Font.getDefaultFont().stringWidth(label));
    }

    int calcPrefW() {
        return labelW();
    }

    int calcPrefH() {
        return labelH();
    }

    int calcMinW() {
        return 0;
    }

    int calcMinH() {
        return labelH();
    }

    /** Number of display lines of {@code s} when wrapped at {@code w} pixels (explicit newlines count). */
    static int lines(String s, Font f, int w) {
        if (s == null || s.length() == 0) return 0;
        if (w <= 0) w = 1;
        int total = 0, start = 0;
        for (;;) {
            int nl = s.indexOf('\n', start);
            String seg = nl < 0 ? s.substring(start) : s.substring(start, nl);
            int sw = f.stringWidth(seg);
            total += sw <= w ? 1 : (sw + w - 1) / w;
            if (nl < 0) break;
            start = nl + 1;
        }
        return total;
    }

    static int widestWord(String s, Font f) {
        int best = 0, start = 0;
        if (s == null) return 0;
        for (int i = 0; i <= s.length(); i++) {
            if (i == s.length() || s.charAt(i) == ' ' || s.charAt(i) == '\n') {
                int ww = f.stringWidth(s.substring(start, i));
                if (ww > best) best = ww;
                start = i + 1;
            }
        }
        return best;
    }
}
