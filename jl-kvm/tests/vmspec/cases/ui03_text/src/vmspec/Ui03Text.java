package vmspec;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/** T24: TextField / TextBox rules: constraints, maxSize, insert / delete / getChars / setChars, exceptions. Both classes share the rules; each check runs on both. */
public class Ui03Text extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }
    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }
    abstract static class T { abstract Object go() throws Exception; }
    static void t(String what, T c) {
        String r;
        try { Object o = c.go(); r = o == null ? "ok" : "ok " + o; } catch (Throwable e) { r = cn(e); }
        p(what + " -> " + r);
    }

    interface Make { Object make(String text, int max, int constraints); }
    static String str(Object o) { return o instanceof TextField ? ((TextField) o).getString() : ((TextBox) o).getString(); }

    protected void startApp() {
        for (int k = 0; k < 2; k++) {
            final boolean box = k == 1;
            final String n = box ? "TextBox" : "TextField";
            final Make mk = box ? (Make) new Make() { public Object make(String t, int m, int c) { return new TextBox("t", t, m, c); } }
                    : (Make) new Make() { public Object make(String t, int m, int c) { return new TextField("l", t, m, c); } };
            t(n + " ctor ok", new T() { Object go() { return str(mk.make("hi", 10, TextField.ANY)); } });
            t(n + " ctor null text", new T() { Object go() { return "[" + str(mk.make(null, 10, TextField.ANY)) + "]"; } });
            t(n + " ctor maxSize 0", new T() { Object go() { mk.make("", 0, TextField.ANY); return null; } });
            t(n + " ctor text longer than maxSize", new T() { Object go() { mk.make("abcdef", 3, TextField.ANY); return null; } });
            t(n + " ctor bad constraints 6", new T() { Object go() { mk.make("", 5, 6); return null; } });
            t(n + " ctor bad constraint bit", new T() { Object go() { mk.make("", 5, 1 << 30); return null; } });
            t(n + " ctor NUMERIC with letters", new T() { Object go() { mk.make("12a", 5, TextField.NUMERIC); return null; } });
            t(n + " ctor NUMERIC -12", new T() { Object go() { return str(mk.make("-12", 5, TextField.NUMERIC)); } });
            t(n + " ctor NUMERIC 1-2", new T() { Object go() { mk.make("1-2", 5, TextField.NUMERIC); return null; } });
            t(n + " ctor DECIMAL -1.5", new T() { Object go() { return str(mk.make("-1.5", 8, TextField.DECIMAL)); } });
            t(n + " ctor DECIMAL 1.5.2", new T() { Object go() { mk.make("1.5.2", 8, TextField.DECIMAL); return null; } });
            t(n + " ctor NUMERIC with '.'", new T() { Object go() { mk.make("1.5", 8, TextField.NUMERIC); return null; } });
            t(n + " ctor PHONENUMBER +49 #1*", new T() { Object go() { return str(mk.make("+49 #1*".replace(' ', '0'), 12, TextField.PHONENUMBER)); } });
            t(n + " ctor PHONENUMBER letters", new T() { Object go() { mk.make("12a", 8, TextField.PHONENUMBER); return null; } });
            t(n + " ctor EMAILADDR / URL / flags", new T() { Object go() { mk.make("a@b", 8, TextField.EMAILADDR | TextField.PASSWORD | TextField.SENSITIVE); mk.make("x:/y", 8, TextField.URL | TextField.UNEDITABLE | TextField.NON_PREDICTIVE | TextField.INITIAL_CAPS_WORD); return "ok"; } });
        }
        final TextField f = new TextField("l", "hello", 8, TextField.ANY);
        final TextBox b = new TextBox("t", "hello", 8, TextField.ANY);
        t("size / maxSize / constraints", new T() { Object go() { return f.size() + " " + f.getMaxSize() + " " + f.getConstraints() + " " + b.size() + " " + b.getMaxSize(); } });
        t("setString", new T() { Object go() { f.setString("abc"); return f.getString(); } });
        t("setString null empties", new T() { Object go() { f.setString(null); return "[" + f.getString() + "] " + f.size(); } });
        t("setString too long", new T() { Object go() { f.setString("123456789"); return null; } });
        t("insert in the middle", new T() { Object go() { f.setString("ad"); f.insert("bc", 1); return f.getString(); } });
        t("insert position -3 -> 0", new T() { Object go() { f.insert("X", -3); return f.getString(); } });
        t("insert position 99 -> end", new T() { Object go() { f.insert("Y", 99); return f.getString(); } });
        t("insert exceeding maxSize", new T() { Object go() { f.insert("123456", 0); return null; } });
        t("insert null", new T() { Object go() { f.insert((String) null, 0); return null; } });
        t("insert chars", new T() { Object go() { f.setString(""); f.insert(new char[] {'a', 'b', 'c', 'd'}, 1, 2, 0); return f.getString(); } });
        t("insert chars bad range", new T() { Object go() { f.insert(new char[2], 1, 5, 0); return null; } });
        t("insert chars null", new T() { Object go() { f.insert((char[]) null, 0, 0, 0); return null; } });
        t("delete", new T() { Object go() { f.setString("abcdef"); f.delete(1, 3); return f.getString(); } });
        t("delete bad range", new T() { Object go() { f.delete(2, 9); return null; } });
        t("delete negative", new T() { Object go() { f.delete(-1, 1); return null; } });
        t("getChars", new T() { Object go() { char[] c = new char[6]; int n = f.getChars(c); return n + " " + c[0] + c[1] + c[2]; } });
        t("getChars too short", new T() { Object go() { return new Integer(f.getChars(new char[1])); } });
        t("getChars null", new T() { Object go() { return new Integer(f.getChars(null)); } });
        t("setChars", new T() { Object go() { f.setChars(new char[] {'x', 'y', 'z'}, 1, 2); return f.getString(); } });
        t("setChars null empties", new T() { Object go() { f.setChars(null, 0, 0); return "[" + f.getString() + "]"; } });
        t("setChars bad range", new T() { Object go() { f.setChars(new char[2], 1, 4); return null; } });
        t("setMaxSize(3) truncates", new T() { Object go() { f.setString("abcdef"); int r = f.setMaxSize(3); return r + " " + f.getString() + " " + f.getMaxSize(); } });
        t("setMaxSize(0)", new T() { Object go() { f.setMaxSize(0); return null; } });
        t("setMaxSize(100)", new T() { Object go() { return new Boolean(f.setMaxSize(100) >= 100 || f.getMaxSize() > 3); } });
        t("setConstraints keeps matching text", new T() { Object go() { f.setString("123"); f.setConstraints(TextField.NUMERIC); return f.getString(); } });
        t("setConstraints clears text that no longer matches", new T() { Object go() { f.setConstraints(TextField.ANY); f.setString("12a"); f.setConstraints(TextField.NUMERIC); return "[" + f.getString() + "]"; } });
        t("setConstraints bad", new T() { Object go() { f.setConstraints(77); return null; } });
        t("setString violating constraints", new T() { Object go() { f.setString("abc"); return null; } });
        t("setInitialInputMode", new T() { Object go() { f.setInitialInputMode("MIDP_UPPERCASE_LATIN"); f.setInitialInputMode(null); return "ok"; } });
        t("TextBox title / ticker", new T() { Object go() { b.setTitle("new"); b.setTicker(new Ticker("tk")); return b.getTitle() + b.getTicker().getString(); } });
        t("TextBox insert / delete", new T() { Object go() { b.setString("12345"); b.delete(0, 2); b.insert("ab", 1); return b.getString(); } });
        p("done");
        notifyDestroyed();
    }

    protected void pauseApp() { }
    protected void destroyApp(boolean u) { }
}
