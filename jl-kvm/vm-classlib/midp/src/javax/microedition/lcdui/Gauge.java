/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;


public class Gauge extends Item {
    public static final int INDEFINITE = -1;
    public static final int CONTINUOUS_IDLE = 0, INCREMENTAL_IDLE = 1, CONTINUOUS_RUNNING = 2, INCREMENTAL_UPDATING = 3;
    private final boolean interactive;
    private int maxValue, value;

    public Gauge(String label, boolean interactive, int maxValue, int initialValue) {
        super(label);
        if (interactive) {
            if (maxValue <= 0) throw new IllegalArgumentException("maxValue");
        } else if (maxValue <= 0 && maxValue != INDEFINITE) throw new IllegalArgumentException("maxValue");
        this.interactive = interactive;
        this.maxValue = maxValue;
        if (maxValue == INDEFINITE) {
            if (initialValue < CONTINUOUS_IDLE || initialValue > INCREMENTAL_UPDATING) throw new IllegalArgumentException("initialValue");
            this.value = initialValue;
        } else {
            this.value = initialValue < 0 ? 0 : initialValue > maxValue ? maxValue : initialValue;
        }
    }

    public void setValue(int value) {
        if (maxValue == INDEFINITE) {
            if (interactive || value < CONTINUOUS_IDLE || value > INCREMENTAL_UPDATING) return;
            this.value = value;
        } else {
            this.value = value < 0 ? 0 : value > maxValue ? maxValue : value;
        }
        changed();
    }

    public int getValue() {
        return value;
    }

    public void setMaxValue(int maxValue) {
        if (interactive) {
            if (maxValue <= 0) throw new IllegalArgumentException("maxValue");
        } else if (maxValue <= 0 && maxValue != INDEFINITE) throw new IllegalArgumentException("maxValue");
        boolean wasIndefinite = this.maxValue == INDEFINITE;
        this.maxValue = maxValue;
        if (maxValue == INDEFINITE) {
            if (!wasIndefinite) value = CONTINUOUS_IDLE;
        } else {
            if (wasIndefinite) value = 0;
            if (value > maxValue) value = maxValue;
        }
        changed();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public boolean isInteractive() {
        return interactive;
    }

    int uiKind() {
        return ScreenCodec.I_GAUGE;
    }

    void encode() {
        ScreenCodec.w(interactive ? 1 : 0);
        ScreenCodec.w(maxValue);
        ScreenCodec.w(value);
    }

    int calcPrefW() {
        return availW();
    }

    int calcPrefH() {
        return labelH() + Font.getDefaultFont().getHeight();
    }

    int calcMinW() {
        return Math.min(availW(), 40);
    }

    int calcMinH() {
        return calcPrefH();
    }

    /** Event thread: the user moved an interactive gauge. */
    void hostValue(int v) {
        if (interactive && maxValue != INDEFINITE) value = v < 0 ? 0 : v > maxValue ? maxValue : v;
    }

    public void setLabel(String label) {
        super.setLabel(label);
    }

    public void setLayout(int layout) {
        super.setLayout(layout);
    }

    public void addCommand(Command cmd) {
        super.addCommand(cmd);
    }

    public void setItemCommandListener(ItemCommandListener l) {
        super.setItemCommandListener(l);
    }

    public void setPreferredSize(int width, int height) {
        super.setPreferredSize(width, height);
    }

    public void setDefaultCommand(Command cmd) {
        super.setDefaultCommand(cmd);
    }
}
