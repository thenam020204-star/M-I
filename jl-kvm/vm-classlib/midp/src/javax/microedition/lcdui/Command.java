/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class Command {
    public static final int SCREEN = 1, BACK = 2, CANCEL = 3, OK = 4, HELP = 5, STOP = 6, EXIT = 7, ITEM = 8;
    /** Id in the host screen protocol. */
    final int cid = Ids.next();
    private final String label, longLabel;
    private final int type, priority;

    public Command(String label, int commandType, int priority) {
        this(label, null, commandType, priority);
    }

    public Command(String shortLabel, String longLabel, int commandType, int priority) {
        if (shortLabel == null) throw new NullPointerException();
        if (commandType < SCREEN || commandType > ITEM) throw new IllegalArgumentException("commandType");
        this.label = shortLabel;
        this.longLabel = longLabel;
        this.type = commandType;
        this.priority = priority;
    }

    public String getLabel() {
        return label;
    }

    public String getLongLabel() {
        return longLabel;
    }

    public int getCommandType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }
}
