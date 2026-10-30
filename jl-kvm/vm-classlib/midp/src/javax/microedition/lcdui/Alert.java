/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;


public class Alert extends Screen {
    public static final int FOREVER = -2;
    public static final Command DISMISS_COMMAND = new Command("Done", Command.OK, 0);
    private String text;
    private Image image;
    private AlertType type;
    private Gauge indicator;
    private int timeout = 3000;

    public Alert(String title) {
        this(title, null, null, null);
    }

    public Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        setTitle(title);
        text = alertText;
        image = alertImage == null || !alertImage.isMutable() ? alertImage : Image.createImage(alertImage);
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
        touch();
    }

    public AlertType getType() {
        return type;
    }

    public void setType(AlertType type) {
        this.type = type;
        touch();
    }

    public String getString() {
        return text;
    }

    public void setString(String str) {
        text = str;
        touch();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = img == null || !img.isMutable() ? img : Image.createImage(img);
        touch();
    }

    public void setIndicator(Gauge indicator) {
        if (indicator != null) {
            if (indicator.isInteractive() || indicator.commands.size() > 0 || indicator.defaultCommand != null || indicator.itemListener != null)
                throw new IllegalArgumentException("indicator");
            if (indicator.owner != null && indicator.owner != this) throw new IllegalStateException("indicator is owned by a container");
        }
        if (this.indicator != null) this.indicator.owner = null;
        if (indicator != null) indicator.owner = this;
        this.indicator = indicator;
        touch();
    }

    public Gauge getIndicator() {
        return indicator;
    }

    public void addCommand(Command cmd) {
        if (cmd == DISMISS_COMMAND) return;
        super.addCommand(cmd);
    }

    public void removeCommand(Command cmd) {
        super.removeCommand(cmd);
    }

    public void setCommandListener(CommandListener l) {
        super.setCommandListener(l);
    }

    /** No application command: the implicit DISMISS_COMMAND is offered (and delivered to the listener). */
    boolean implicitDismiss() {
        return commandCount() == 0;
    }

    /** Modal: stays until dismissed (FOREVER, or two or more application commands). */
    boolean modal() {
        return timeout == FOREVER || commandCount() >= 2;
    }

    int uiKind() {
        return ScreenCodec.K_ALERT;
    }

    void encodeBody() {
        ScreenCodec.alertBody(this);
    }

    int type0() {
        return type == null ? 0 : type.kind;
    }

    Image image0() {
        return image;
    }

    String text0() {
        return text;
    }

    Gauge indicator0() {
        return indicator;
    }

    Item findItem(int iid) {
        return indicator != null && indicator.iid == iid ? indicator : null;
    }

    Command findCommand(int cid) {
        if (cid == DISMISS_COMMAND.cid) return DISMISS_COMMAND;
        return super.findCommand(cid);
    }

    /** Event thread: the host activated a command; the implicit dismiss command also leaves the Alert. */
    void onUiCommand(int cid) {
        Command c = findCommand(cid);
        if (c == null) return;
        if (c == DISMISS_COMMAND && implicitDismiss()) {
            fire(c);
            Display d = Display.instance;
            if (d != null) d.dismissAlert(this);
        } else {
            fire(c);
        }
    }
}
