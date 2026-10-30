/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Model part of Alert (text, image, type, timeout, indicator, commands). Showing it (timer, dismissal) is T24. */
public class Alert extends Screen {
    public static final int FOREVER = -2;
    public static final Command DISMISS_COMMAND = new Command("Done", Command.OK, 0);
    private String text;
    private Image image;
    private AlertType type;
    private Gauge indicator;
    private int timeout = 3000;
    private boolean userCommands;

    public Alert(String title) {
        this(title, null, null, null);
    }

    public Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        setTitle(title);
        text = alertText;
        image = alertImage;
        type = alertType;
    }

    public int getDefaultTimeout() {
        return 3000;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int time) {
        if (time <= 0 && time != FOREVER) throw new IllegalArgumentException("time");
        timeout = time;
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
    }

    public String getString() {
        return text;
    }

    public void setString(String str) {
        text = str;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img;
    }

    public void setIndicator(Gauge indicator) {
        if (indicator != null && (indicator.isInteractive() || indicator.owner != null || indicator.commands.size() > 0 || indicator.defaultCommand != null))
            throw new IllegalArgumentException("indicator");
        if (indicator != null) indicator.owner = this;
        if (this.indicator != null) this.indicator.owner = null;
        this.indicator = indicator;
    }

    public Gauge getIndicator() {
        return indicator;
    }

    public void addCommand(Command cmd) {
        if (cmd == DISMISS_COMMAND) return;
        super.addCommand(cmd);
        userCommands = true;
    }

    public void removeCommand(Command cmd) {
        super.removeCommand(cmd);
        userCommands = commandCount() > 0;
    }

    public void setCommandListener(CommandListener l) {
        super.setCommandListener(l);
    }

    boolean implicitDismiss() {
        return !userCommands;
    }
}
