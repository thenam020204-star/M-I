package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/** T24: Choice rules (ChoiceGroup and List): selection per type, flags, insert/delete/set, exceptions, fit policy, fonts. */
public class Ui02Choice extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }
    static String sel(Choice c) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < c.size(); i++) b.append(c.isSelected(i) ? '1' : '0');
        return b + " idx=" + c.getSelectedIndex();
    }
    static String[] abc() { return new String[] {"a", "b", "c"}; }

    protected void startApp() {
        final int[] types = {Choice.EXCLUSIVE, Choice.MULTIPLE, Choice.IMPLICIT, Choice.POPUP};
        String[] names = {"EXCLUSIVE", "MULTIPLE", "IMPLICIT", "POPUP"};
        for (int k = 0; k < 4; k++) {
            final int ty = types[k];
            final String nm = names[k];
            t("ChoiceGroup(" + nm + ")", new T() { Object go() { return sel(new ChoiceGroup("g", ty)); } });
            t("List(" + nm + ")", new T() { Object go() { return sel(new List("l", ty)); } });
            t("ChoiceGroup(" + nm + ", abc)", new T() { Object go() { return sel(new ChoiceGroup("g", ty, abc(), null)); } });
            t("List(" + nm + ", abc)", new T() { Object go() { return sel(new List("l", ty, abc(), null)); } });
        }
        t("ChoiceGroup bad type", new T() { Object go() { new ChoiceGroup("g", 9); return null; } });
        t("List bad type", new T() { Object go() { new List("l", 0); return null; } });
        t("ctor null strings", new T() { Object go() { new ChoiceGroup("g", Choice.EXCLUSIVE, null, null); return null; } });
        t("ctor null element", new T() { Object go() { new ChoiceGroup("g", Choice.EXCLUSIVE, new String[] {"a", null}, null); return null; } });
        t("ctor image length mismatch", new T() { Object go() { new ChoiceGroup("g", Choice.EXCLUSIVE, abc(), new Image[2]); return null; } });

        final Choice ex = new ChoiceGroup("g", Choice.EXCLUSIVE, abc(), null);
        final Choice mu = new ChoiceGroup("g", Choice.MULTIPLE, abc(), null);
        final Choice im = new List("l", Choice.IMPLICIT, abc(), null);
        t("exclusive setSelectedIndex(2,true)", new T() { Object go() { ex.setSelectedIndex(2, true); return sel(ex); } });
        t("exclusive setSelectedIndex(2,false) ignored", new T() { Object go() { ex.setSelectedIndex(2, false); return sel(ex); } });
        t("multiple setSelectedIndex(0,true),(2,true)", new T() { Object go() { mu.setSelectedIndex(0, true); mu.setSelectedIndex(2, true); return sel(mu); } });
        t("multiple setSelectedIndex(0,false)", new T() { Object go() { mu.setSelectedIndex(0, false); return sel(mu); } });
        t("implicit setSelectedIndex(1,true)", new T() { Object go() { im.setSelectedIndex(1, true); return sel(im); } });
        t("setSelectedIndex out of range", new T() { Object go() { ex.setSelectedIndex(3, true); return null; } });
        t("isSelected(-1)", new T() { Object go() { return new Boolean(ex.isSelected(-1)); } });
        t("getSelectedFlags", new T() { Object go() { boolean[] f = new boolean[5]; f[4] = true; int n = mu.getSelectedFlags(f); return n + " " + f[0] + f[1] + f[2] + f[3] + f[4]; } });
        t("getSelectedFlags too short", new T() { Object go() { return new Integer(mu.getSelectedFlags(new boolean[2])); } });
        t("getSelectedFlags null", new T() { Object go() { return new Integer(mu.getSelectedFlags(null)); } });
        t("setSelectedFlags multiple", new T() { Object go() { mu.setSelectedFlags(new boolean[] {true, true, false}); return sel(mu); } });
        t("setSelectedFlags exclusive: none true", new T() { Object go() { ex.setSelectedFlags(new boolean[] {false, false, false}); return sel(ex); } });
        t("setSelectedFlags too short", new T() { Object go() { mu.setSelectedFlags(new boolean[1]); return null; } });
        t("setSelectedFlags null", new T() { Object go() { mu.setSelectedFlags(null); return null; } });

        final Choice g = new ChoiceGroup("g", Choice.EXCLUSIVE);
        t("append to empty exclusive selects it", new T() { Object go() { int i = g.append("x", null); return i + " " + sel(g); } });
        t("append second", new T() { Object go() { int i = g.append("y", null); return i + " " + sel(g); } });
        t("insert before the selected keeps it selected", new T() { Object go() { g.insert(0, "w", null); return sel(g) + " " + g.getString(0) + g.getString(1) + g.getString(2); } });
        t("insert(size)", new T() { Object go() { g.insert(g.size(), "z", null); return g.size() + " " + g.getString(3); } });
        t("insert(size+1)", new T() { Object go() { g.insert(g.size() + 1, "q", null); return null; } });
        t("insert null string", new T() { Object go() { g.insert(0, null, null); return null; } });
        t("append null string", new T() { Object go() { g.append(null, null); return null; } });
        t("set(1)", new T() { Object go() { g.set(1, "X", null); return g.getString(1) + " " + sel(g); } });
        t("set out of range", new T() { Object go() { g.set(4, "X", null); return null; } });
        t("set null", new T() { Object go() { g.set(0, null, null); return null; } });
        t("getString out of range", new T() { Object go() { return g.getString(9); } });
        t("getImage", new T() { Object go() { return g.getImage(0); } });
        t("getImage out of range", new T() { Object go() { return g.getImage(-1); } });
        t("delete out of range", new T() { Object go() { g.delete(4); return null; } });
        t("delete an unselected element before the selected", new T() { Object go() { g.delete(0); return sel(g) + " " + g.getString(0); } });
        t("deleteAll", new T() { Object go() { g.deleteAll(); return g.size() + " " + sel(g); } });
        t("mutable image is copied", new T() { Object go() { Image m = Image.createImage(3, 3); g.append("i", m); return new Boolean(g.getImage(0) != m && !g.getImage(0).isMutable()); } });

        t("fit policy default", new T() { Object go() { return new Integer(ex.getFitPolicy()); } });
        t("setFitPolicy(TEXT_WRAP_OFF)", new T() { Object go() { ex.setFitPolicy(Choice.TEXT_WRAP_OFF); return new Integer(ex.getFitPolicy()); } });
        t("setFitPolicy(9)", new T() { Object go() { ex.setFitPolicy(9); return null; } });
        t("getFont default not null", new T() { Object go() { return new Boolean(ex.getFont(0) != null); } });
        t("setFont / getFont", new T() { Object go() { Font f = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE); ex.setFont(1, f); return new Boolean(ex.getFont(1) == f); } });
        t("setFont(null) restores default", new T() { Object go() { ex.setFont(1, null); return new Boolean(ex.getFont(1) != null); } });
        t("setFont out of range", new T() { Object go() { ex.setFont(5, null); return null; } });

        // List command rules
        final List l = new List("l", Choice.IMPLICIT, abc(), null);
        final Command c1 = new Command("c1", Command.SCREEN, 1);
        t("SELECT_COMMAND", new T() { Object go() { return new Integer(List.SELECT_COMMAND.getCommandType()); } });
        t("setSelectCommand(c1) on implicit", new T() { Object go() { l.setSelectCommand(c1); return null; } });
        t("setSelectCommand(null)", new T() { Object go() { l.setSelectCommand(null); return null; } });
        t("setSelectCommand on exclusive list ignored", new T() { Object go() { new List("e", Choice.EXCLUSIVE).setSelectCommand(c1); return null; } });
        t("List title / ticker", new T() { Object go() { l.setTitle("T"); l.setTicker(new Ticker("k")); return l.getTitle() + l.getTicker().getString(); } });
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
