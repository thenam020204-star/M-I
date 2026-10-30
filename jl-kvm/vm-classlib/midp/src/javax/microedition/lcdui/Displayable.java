/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import java.util.Vector;
import jlkvm.internal.EventLoop;

public abstract class Displayable {
    String title;
    Ticker ticker;
    /** Sorted by priority (ascending = more important first), insertion order inside a priority. */
    final Vector commands = new Vector();
    CommandListener listener;
    /** Event thread state: showNotify delivered and hideNotify not yet (D13). */
    boolean visible;
    /** Id in the host screen protocol. */
    final int sid = Ids.next();
    /** The model changed since it was last sent to the host. */
    private boolean uiDirty;
    private final Runnable syncJob = new Runnable() {
        public void run() {
            synchronized (Displayable.this) {
                uiDirty = false;
            }
            if (visible && onScreen()) ScreenCodec.send(Displayable.this);
        }
    };

    Displayable() {
    }

    public void setTitle(String s) {
        title = s;
        touch();
    }

    public String getTitle() {
        return title;
    }

    public void setTicker(Ticker t) {
        ticker = t;
        touch();
    }

    public Ticker getTicker() {
        return ticker;
    }

    /** true between the return of showNotify and the hideNotify (MIDP: showNotify is called "prior to being made visible"). */
    public boolean isShown() {
        Display d = Display.instance;
        return visible && d != null && d.current == this && d.foreground;
    }

    /** current displayable of a foreground display (showNotify may not have run yet): the condition under which repaint requests are kept. */
    boolean onScreen() {
        Display d = Display.instance;
        return d != null && d.current == this && d.foreground;
    }

    public int getWidth() {
        return width0();
    }

    public int getHeight() {
        return height0();
    }

    /** Overridden by Canvas (full-screen mode); keeps getWidth / getHeight declared only in Displayable as in the API. */
    int width0() {
        return Display.canvasW;
    }

    int height0() {
        return Display.canvasH;
    }

    protected void sizeChanged(int w, int h) {
    }

    public void addCommand(Command cmd) {
        if (cmd == null) throw new NullPointerException();
        synchronized (commands) {
            if (commands.contains(cmd)) return;
            int i = 0;
            while (i < commands.size() && ((Command) commands.elementAt(i)).getPriority() <= cmd.getPriority()) i++;
            commands.insertElementAt(cmd, i);
        }
        touch();
    }

    public void removeCommand(Command cmd) {
        if (cmd == null) return;
        synchronized (commands) {
            commands.removeElement(cmd);
        }
        touch();
    }

    public void setCommandListener(CommandListener l) {
        listener = l;
    }

    int commandCount() {
        synchronized (commands) {
            return commands.size();
        }
    }

    /** Event thread: delivers a command to the listener (D13). */
    void fire(Command c) {
        CommandListener l = listener;
        if (l == null) return;
        try {
            l.commandAction(c, this);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /** Soft-key to command mapping of a device without a menu model (T24 refines): left = the most important command, right = a BACK/CANCEL/EXIT/STOP command or the second. */
    Command commandForSoftKey(boolean left) {
        synchronized (commands) {
            int n = commands.size();
            if (n == 0) return null;
            Command rightCmd = null;
            if (n >= 2) {
                for (int i = 0; i < n && rightCmd == null; i++) {
                    Command c = (Command) commands.elementAt(i);
                    int t = c.getCommandType();
                    if (t == Command.BACK || t == Command.CANCEL || t == Command.EXIT || t == Command.STOP) rightCmd = c;
                }
                if (rightCmd == null) rightCmd = (Command) commands.elementAt(1);
            }
            if (!left) return rightCmd;
            for (int i = 0; i < n; i++) {
                Command c = (Command) commands.elementAt(i);
                if (c != rightCmd) return c;
            }
            return null;
        }
    }

    /** The model changed: the host gets the new description on the event thread (coalesced, only while this is the current displayable). */
    void touch() {
        synchronized (this) {
            if (uiDirty) return;
            uiDirty = true;
        }
        if (onScreen()) EventLoop.post(syncJob);
    }

    /** Event thread: sends the whole description now. */
    void sendNow() {
        synchronized (this) {
            uiDirty = false;
        }
        ScreenCodec.send(this);
    }

    /** Kind code of the host protocol (ScreenCodec.K_*). */
    int uiKind() {
        return ScreenCodec.K_CANVAS;
    }

    void encodeBody() {
    }

    Item findItem(int iid) {
        return null;
    }

    Command findCommand(int cid) {
        synchronized (commands) {
            for (int i = 0; i < commands.size(); i++) {
                Command c = (Command) commands.elementAt(i);
                if (c.cid == cid) return c;
            }
        }
        return null;
    }

    /** Event thread: the host activated a screen command. */
    void onUiCommand(int cid) {
        Command c = findCommand(cid);
        if (c != null) fire(c);
    }

    /** Event thread: an interactive item of this screen changed its state on the host. */
    void itemChanged(Item item) {
    }

    /** First BACK / CANCEL / EXIT / STOP command (the one a right soft key takes), or null. */
    Command backCommand() {
        synchronized (commands) {
            for (int i = 0; i < commands.size(); i++) {
                Command c = (Command) commands.elementAt(i);
                int t = c.getCommandType();
                if (t == Command.BACK || t == Command.CANCEL || t == Command.EXIT || t == Command.STOP) return c;
            }
        }
        return null;
    }

    /** Event thread: this displayable became visible (showNotify) / invisible (hideNotify). */
    void onShow() {
        visible = true;
        sendNow();
    }

    void onHide() {
        visible = false;
    }

    /** Event thread: a key event reached a displayable that is not a Canvas (T24 forms handle navigation). */
    void onKey(int type, int code) {
    }
}
