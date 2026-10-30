package ui;

import java.util.Date;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/**
 * T24 sample MIDlet: List (implicit) -> Form (StringItem button, TextField, two ChoiceGroups, Gauge, DateField, Spacer, ImageItem,
 * CustomItem) -> TextBox (numeric) -> Alert -> Canvas with four commands (menu). Driven by tests/midlets/scripts/ui.inp
 * (HeadlessUi verbs). Every callback prints one "> " line and whether it ran on the event thread (the one callSerially uses).
 */
public class UiMIDlet extends MIDlet implements CommandListener, ItemStateListener, ItemCommandListener {
    static void p(String s) { System.out.println("> " + s); }

    Display display;
    Thread eventThread;
    List list;
    Form form;
    TextBox box;
    Alert alert;
    Canvas canvas;
    StringItem button;
    TextField field;
    ChoiceGroup single, multi;
    Gauge gauge;
    DateField date;
    Custom custom;
    Command exit = new Command("Exit", Command.EXIT, 1), back = new Command("Back", Command.BACK, 1), ok = new Command("OK", Command.OK, 1),
            press = new Command("Press", Command.ITEM, 1), c1 = new Command("One", Command.SCREEN, 2), c2 = new Command("Two", Command.SCREEN, 3),
            c3 = new Command("Three", Command.SCREEN, 4);

    String et() { return Thread.currentThread() == eventThread ? " ET" : " NOT-EVENT-THREAD"; }

    class Custom extends CustomItem {
        int paints;
        Custom() { super("Custom"); }
        protected int getMinContentWidth() { return 20; }
        protected int getMinContentHeight() { return 10; }
        protected int getPrefContentWidth(int h) { return 40; }
        protected int getPrefContentHeight(int w) { return 20; }
        protected void paint(Graphics g, int w, int h) { paints++; g.setColor(0xFF0000); g.fillRect(1, 1, w - 2, h - 2); }
        protected void keyPressed(int k) { p("custom keyPressed " + k + " action=" + getGameAction(k) + et()); repaint(); }
        protected void pointerPressed(int x, int y) { p("custom pointerPressed " + x + "," + y + et()); }
        protected boolean traverse(int dir, int w, int h, int[] vis) { p("custom traverse " + dir + et()); return dir == 1; }
    }

    protected void startApp() {
        display = Display.getDisplay(this);
        display.callSerially(new Runnable() { public void run() { eventThread = Thread.currentThread(); } });
        list = new List("Menu", Choice.IMPLICIT, new String[] {"Alpha", "Beta", "Gamma"}, null);
        list.addCommand(exit);
        list.setCommandListener(this);
        p("setCurrent list");
        display.setCurrent(list);

        form = new Form("Form");
        button = new StringItem("Act", "Press me", Item.BUTTON);
        button.addCommand(press);
        button.setDefaultCommand(press);
        button.setItemCommandListener(this);
        field = new TextField("Name", "abc", 10, TextField.ANY);
        single = new ChoiceGroup("One of", Choice.EXCLUSIVE, new String[] {"a", "b", "c"}, null);
        multi = new ChoiceGroup("Some of", Choice.MULTIPLE, new String[] {"x", "y", "z"}, null);
        gauge = new Gauge("Level", true, 10, 3);
        date = new DateField("When", DateField.DATE_TIME, java.util.TimeZone.getTimeZone("GMT"));
        custom = new Custom();
        form.append(button);
        form.append(field);
        form.append(single);
        form.append(multi);
        form.append(gauge);
        form.append(date);
        form.append(new Spacer(5, 7));
        form.append(custom);
        form.append("plain text");
        form.addCommand(back);
        form.addCommand(c1);
        form.setCommandListener(this);
        form.setItemStateListener(this);

        box = new TextBox("Number", "12", 8, TextField.NUMERIC);
        box.addCommand(ok);
        box.setCommandListener(this);

        alert = new Alert("Done", "All fine", null, AlertType.INFO);
        alert.setTimeout(Alert.FOREVER);
        alert.setCommandListener(this);

        canvas = new Canvas() {
            protected void paint(Graphics g) { g.setColor(0x102030); g.fillRect(0, 0, getWidth(), getHeight()); }
            protected void keyPressed(int k) { p("canvas keyPressed " + k + et()); }
        };
        canvas.addCommand(c1);
        canvas.addCommand(c2);
        canvas.addCommand(c3);
        canvas.addCommand(exit);
        canvas.setCommandListener(this);
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }

    public void commandAction(Command c, Displayable d) {
        String where = d == list ? "list" : d == form ? "form" : d == box ? "box" : d == alert ? "alert" : d == canvas ? "canvas" : "?";
        String extra = "";
        if (d == list) extra = " selected=" + list.getSelectedIndex() + " isSelectCommand=" + (c == List.SELECT_COMMAND);
        p("commandAction " + (c == Alert.DISMISS_COMMAND ? "DISMISS" : c == List.SELECT_COMMAND ? "SELECT" : c.getLabel()) + " on " + where + extra + et());
        if (d == list && c == List.SELECT_COMMAND) {
            p("setCurrent form");
            display.setCurrent(form);
        } else if (d == form && c == back) {
            p("form: field=" + field.getString() + " single=" + single.getSelectedIndex() + " multi=" + multi.isSelected(0) + multi.isSelected(1) + multi.isSelected(2)
                    + " gauge=" + gauge.getValue() + " date=" + (date.getDate() == null ? "null" : "" + date.getDate().getTime()));
            display.setCurrent(box);
        } else if (d == box && c == ok) {
            p("box text=" + box.getString());
            display.setCurrent(alert, canvas);
        } else if (d == alert) {
            p("current is canvas: " + (display.getCurrent() == canvas));
        } else if (c == exit) {
            notifyDestroyed();
        }
    }

    public void itemStateChanged(Item item) {
        String what = item == field ? "field=" + field.getString() : item == single ? "single=" + single.getSelectedIndex()
                : item == multi ? "multi=" + multi.isSelected(0) + multi.isSelected(1) + multi.isSelected(2) : item == gauge ? "gauge=" + gauge.getValue()
                : item == date ? "date=" + (date.getDate() == null ? "null" : "" + date.getDate().getTime()) : "other";
        p("itemStateChanged " + what + et());
    }

    public void commandAction(Command c, Item item) {
        p("item commandAction " + c.getLabel() + et());
        // a programmatic change must not call the listener; notifyStateChanged does, on the event thread
        gauge.setValue(9);
        gauge.notifyStateChanged();
    }
}
