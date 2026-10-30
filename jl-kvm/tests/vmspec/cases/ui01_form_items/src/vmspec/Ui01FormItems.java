package vmspec;

import java.util.Date;
import java.util.TimeZone;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/** T24: model rules of Form, Item, StringItem, ImageItem, Spacer, Gauge, DateField (exceptions and return values; no display needed). */
public class Ui01FormItems extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }

    protected void startApp() {
        final Form f = new Form("F");
        final StringItem s1 = new StringItem("L", "text");
        final StringItem s2 = new StringItem(null, "b", Item.BUTTON);
        t("append item", new T() { Object go() { return new Integer(f.append(s1)); } });
        t("append item again", new T() { Object go() { f.append(s1); return null; } });
        t("append null item", new T() { Object go() { f.append((Item) null); return null; } });
        t("append null string", new T() { Object go() { f.append((String) null); return null; } });
        t("append null image", new T() { Object go() { f.append((Image) null); return null; } });
        t("append string", new T() { Object go() { return new Integer(f.append("plain")); } });
        t("append image", new T() { Object go() { return new Integer(f.append(Image.createImage(4, 4))); } });
        t("size", new T() { Object go() { return new Integer(f.size()); } });
        t("get(1) is a StringItem with null label and text plain", new T() { Object go() { StringItem x = (StringItem) f.get(1); return x.getLabel() + "/" + x.getText(); } });
        t("get(2) is an ImageItem", new T() { Object go() { return new Boolean(f.get(2) instanceof ImageItem); } });
        t("get(3)", new T() { Object go() { return f.get(3); } });
        t("get(-1)", new T() { Object go() { return f.get(-1); } });
        t("insert(0, s2)", new T() { Object go() { f.insert(0, s2); return new Boolean(f.get(0) == s2); } });
        t("insert(size+1)", new T() { Object go() { f.insert(f.size() + 1, new StringItem("z", "z")); return null; } });
        t("insert(size)", new T() { Object go() { f.insert(f.size(), new StringItem("z", "z")); return new Integer(f.size()); } });
        t("insert owned item", new T() { Object go() { f.insert(0, s1); return null; } });
        t("set(1, new)", new T() { Object go() { f.set(1, new StringItem("n", "n")); return new Boolean(((StringItem) f.get(1)).getLabel().equals("n")); } });
        t("set(0, s1): s1 was replaced earlier, free again", new T() { Object go() { f.set(0, s1); return null; } });
        t("set out of range", new T() { Object go() { f.set(f.size(), new StringItem("q", "q")); return null; } });
        t("s1 is owned by f now: a second form refuses it", new T() { Object go() { Form g = new Form("G"); g.append(s1); return new Integer(g.size()); } });
        t("delete(size)", new T() { Object go() { f.delete(f.size()); return null; } });
        t("delete(0)", new T() { Object go() { f.delete(0); return new Integer(f.size()); } });
        t("deleted item reusable", new T() { Object go() { new Form("H").append(s2); return null; } });
        t("deleteAll", new T() { Object go() { f.deleteAll(); return new Integer(f.size()); } });
        t("Form(title, items) with null element", new T() { Object go() { new Form("x", new Item[] {new StringItem("a", "a"), null}); return null; } });
        t("Form(title, items)", new T() { Object go() { return new Integer(new Form("x", new Item[] {new StringItem("a", "a"), new Spacer(1, 1)}).size()); } });
        t("Form(title, null items)", new T() { Object go() { return new Integer(new Form("x", null).size()); } });
        t("Form title", new T() { Object go() { return new Form("T").getTitle(); } });
        t("Form no title", new T() { Object go() { return new Form(null).getTitle(); } });

        // Item
        final StringItem it = new StringItem("lab", "t");
        t("getLabel", new T() { Object go() { return it.getLabel(); } });
        t("setLabel(null)", new T() { Object go() { it.setLabel(null); return it.getLabel(); } });
        t("default layout", new T() { Object go() { return new Integer(it.getLayout()); } });
        t("setLayout(CENTER|NEWLINE_AFTER)", new T() { Object go() { it.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER); return new Integer(it.getLayout()); } });
        t("setLayout(invalid 1<<20)", new T() { Object go() { it.setLayout(1 << 20); return null; } });
        t("setPreferredSize(-2, 5)", new T() { Object go() { it.setPreferredSize(-2, 5); return null; } });
        t("setPreferredSize(30, 20) locks", new T() { Object go() { it.setPreferredSize(30, 20); return it.getPreferredWidth() + "x" + it.getPreferredHeight(); } });
        t("minimum <= preferred", new T() { Object go() { return new Boolean(it.getMinimumWidth() <= it.getPreferredWidth() && it.getMinimumHeight() <= it.getPreferredHeight()); } });
        t("addCommand(null)", new T() { Object go() { it.addCommand(null); return null; } });
        t("StringItem ctor bad mode", new T() { Object go() { new StringItem("a", "b", 7); return null; } });
        t("StringItem appearance", new T() { Object go() { return new Integer(new StringItem("a", "b", Item.HYPERLINK).getAppearanceMode()); } });
        t("StringItem font default non-null", new T() { Object go() { return new Boolean(it.getFont() != null); } });
        t("StringItem setText(null)", new T() { Object go() { it.setText(null); return it.getText(); } });
        t("notifyStateChanged without form", new T() { Object go() { it.notifyStateChanged(); return null; } });
        t("notifyStateChanged in a form", new T() { Object go() { new Form("n").append(it); it.notifyStateChanged(); return null; } });
        final Command cmd = new Command("c", Command.ITEM, 1);
        t("setDefaultCommand adds it", new T() { Object go() { it.setDefaultCommand(cmd); it.removeCommand(cmd); it.setDefaultCommand(null); return null; } });

        // ImageItem
        t("ImageItem null image", new T() { Object go() { ImageItem ii = new ImageItem("l", null, ImageItem.LAYOUT_CENTER, "alt"); return ii.getImage() + "/" + ii.getAltText() + "/" + ii.getLayout(); } });
        t("ImageItem invalid layout", new T() { Object go() { new ImageItem("l", null, 1 << 20, "alt"); return null; } });
        t("ImageItem mutable image is copied", new T() { Object go() { Image m = Image.createImage(5, 5); ImageItem ii = new ImageItem("l", m, 0, null); return new Boolean(ii.getImage() != m && !ii.getImage().isMutable()); } });
        t("ImageItem immutable kept", new T() { Object go() { Image m = Image.createImage(Image.createImage(5, 5)); ImageItem ii = new ImageItem("l", m, 0, null); return new Boolean(ii.getImage() == m); } });
        t("ImageItem bad mode", new T() { Object go() { new ImageItem("l", null, 0, null, 9); return null; } });

        // Spacer
        final Spacer sp = new Spacer(10, 20);
        t("Spacer sizes", new T() { Object go() { return sp.getMinimumWidth() + "x" + sp.getMinimumHeight(); } });
        t("Spacer negative", new T() { Object go() { new Spacer(-1, 1); return null; } });
        t("Spacer.setMinimumSize negative", new T() { Object go() { sp.setMinimumSize(1, -1); return null; } });
        t("Spacer.addCommand", new T() { Object go() { sp.addCommand(cmd); return null; } });
        t("Spacer.setLabel", new T() { Object go() { sp.setLabel("x"); return null; } });
        t("Spacer.setDefaultCommand", new T() { Object go() { sp.setDefaultCommand(cmd); return null; } });

        // Gauge
        t("Gauge interactive max 0", new T() { Object go() { new Gauge("g", true, 0, 0); return null; } });
        t("Gauge indefinite interactive", new T() { Object go() { new Gauge("g", true, Gauge.INDEFINITE, 0); return null; } });
        t("Gauge non-interactive indefinite", new T() { Object go() { Gauge g = new Gauge("g", false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING); return g.getMaxValue() + "/" + g.getValue(); } });
        t("Gauge value clamp", new T() { Object go() { Gauge g = new Gauge("g", false, 10, 50); g.setValue(-4); int a = g.getValue(); g.setValue(11); return a + "/" + g.getValue(); } });
        t("Gauge setMaxValue lowers value", new T() { Object go() { Gauge g = new Gauge("g", true, 10, 8); g.setMaxValue(5); return g.getValue() + "/" + g.getMaxValue(); } });
        t("Gauge setMaxValue(0) interactive", new T() { Object go() { new Gauge("g", true, 5, 1).setMaxValue(0); return null; } });
        t("Gauge isInteractive", new T() { Object go() { return new Boolean(new Gauge("g", true, 5, 1).isInteractive()); } });

        // DateField
        final DateField df = new DateField("d", DateField.DATE_TIME, TimeZone.getTimeZone("GMT"));
        t("DateField bad mode", new T() { Object go() { new DateField("d", 0); return null; } });
        t("DateField uninitialised", new T() { Object go() { return df.getDate(); } });
        t("DateField date-time round trip", new T() { Object go() { df.setDate(new Date(1000000000000L)); return new Long(df.getDate().getTime()); } });
        t("DateField DATE mode: time of day zero", new T() { Object go() { df.setInputMode(DateField.DATE); return new Long(df.getDate().getTime() % 86400000L); } });
        t("DateField TIME mode: date 1 Jan 1970", new T() { Object go() { df.setInputMode(DateField.TIME); return new Boolean(df.getDate().getTime() < 86400000L); } });
        t("DateField setInputMode bad", new T() { Object go() { df.setInputMode(5); return null; } });
        t("DateField setDate(null)", new T() { Object go() { df.setDate(null); return df.getDate(); } });
        t("DateField getInputMode", new T() { Object go() { return new Integer(df.getInputMode()); } });

        // Command
        t("Command bad type", new T() { Object go() { new Command("a", 0, 1); return null; } });
        t("Command null label", new T() { Object go() { new Command(null, Command.OK, 1); return null; } });
        t("Command accessors", new T() { Object go() { Command c = new Command("s", "long", Command.HELP, 3); return c.getLabel() + c.getLongLabel() + c.getCommandType() + c.getPriority(); } });
        t("Ticker null", new T() { Object go() { new Ticker(null); return null; } });
        t("Ticker", new T() { Object go() { Ticker k = new Ticker("a"); k.setString("b"); return k.getString(); } });
        // Alert
        final Alert al = new Alert("a", "text", null, AlertType.WARNING);
        t("Alert defaults", new T() { Object go() { return al.getString() + " " + (al.getType() == AlertType.WARNING) + " " + (al.getTimeout() == al.getDefaultTimeout()) + " " + (al.getDefaultTimeout() > 0); } });
        t("Alert.setTimeout(0)", new T() { Object go() { al.setTimeout(0); return null; } });
        t("Alert.setTimeout(FOREVER)", new T() { Object go() { al.setTimeout(Alert.FOREVER); return new Integer(al.getTimeout()); } });
        t("Alert indicator interactive", new T() { Object go() { al.setIndicator(new Gauge("g", true, 5, 0)); return null; } });
        t("Alert indicator ok", new T() { Object go() { Gauge g = new Gauge(null, false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING); al.setIndicator(g); return new Boolean(al.getIndicator() == g); } });
        t("Alert indicator owned by a form", new T() { Object go() { Gauge g = new Gauge(null, false, 5, 0); new Form("x").append(g); al.setIndicator(g); return null; } });
        t("Alert indicator null", new T() { Object go() { al.setIndicator(null); return al.getIndicator(); } });
        t("Alert image mutable copied", new T() { Object go() { Image m = Image.createImage(4, 4); al.setImage(m); return new Boolean(al.getImage() != m); } });
        t("Alert.DISMISS_COMMAND not addable", new T() { Object go() { al.addCommand(Alert.DISMISS_COMMAND); return null; } });
        t("Alert type / string setters", new T() { Object go() { al.setType(null); al.setString("n"); return al.getType() + " " + al.getString(); } });
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
