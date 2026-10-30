/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class AlertType {
    public static final AlertType INFO = new AlertType(4);
    public static final AlertType WARNING = new AlertType(5);
    public static final AlertType ERROR = new AlertType(3);
    public static final AlertType ALARM = new AlertType(1);
    public static final AlertType CONFIRMATION = new AlertType(2);

    /** Code in the host screen protocol (0 for application subclasses). */
    final int kind;

    protected AlertType() {
        kind = 0;
    }

    AlertType(int kind) {
        this.kind = kind;
    }

    /** No audio on this device layer yet (T27 adds the sound path): reports that no sound was played. */
    public boolean playSound(Display display) {
        if (display == null) throw new NullPointerException();
        return false;
    }
}
