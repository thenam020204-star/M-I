/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import java.util.Vector;
import jlkvm.internal.Ui;

/**
 * Package-private: writes the description of a Displayable for the host (native Ui.sync0) - the format is documented in docs/midp.md
 * section 6 and read by jlkvm.host.ui.ScreenModel.decode (same numbers). Event thread only (static buffers).
 */
final class ScreenCodec {
    static final int MAGIC = 0x4A4C5549;
    static final int K_CANVAS = 1, K_FORM = 2, K_LIST = 3, K_TEXTBOX = 4, K_ALERT = 5;
    static final int I_STRING = 1, I_IMAGE = 2, I_TEXT = 3, I_DATE = 4, I_GAUGE = 5, I_CHOICE = 6, I_SPACER = 7, I_CUSTOM = 8;
    /** Command flags. */
    static final int CF_SELECT = 1, CF_DISMISS = 2, CF_DEFAULT = 4;

    private static int[] d = new int[512];
    private static int n;
    private static String[] s = new String[64];
    private static int ns;

    private ScreenCodec() {
    }

    static void w(int v) {
        if (n == d.length) {
            int[] g = new int[n * 2];
            System.arraycopy(d, 0, g, 0, n);
            d = g;
        }
        d[n++] = v;
    }

    /** Writes a string reference: index into the string table, -1 for null. */
    static void ws(String str) {
        if (str == null) {
            w(-1);
            return;
        }
        if (ns == s.length) {
            String[] g = new String[ns * 2];
            System.arraycopy(s, 0, g, 0, ns);
            s = g;
        }
        s[ns] = str;
        w(ns++);
    }

    static void send(Displayable disp) {
        n = 0;
        ns = 0;
        try {
            w(MAGIC);
            w(disp.sid);
            w(disp.uiKind());
            w(disp instanceof Canvas && ((Canvas) disp).fullScreen ? 1 : 0);
            ws(disp.title);
            ws(disp.ticker == null ? null : disp.ticker.getString());
            commands(disp);
            disp.encodeBody();
            Ui.sync0(d, n, s, ns);
        } finally {
            for (int i = 0; i < ns; i++) s[i] = null;
        }
    }

    private static void cmd(Command c, int flags) {
        w(c.cid);
        w(c.getCommandType());
        w(c.getPriority());
        w(flags);
        ws(c.getLabel());
        ws(c.getLongLabel());
    }

    private static void commands(Displayable disp) {
        Command extra = null;
        int flagsFor = 0;
        if (disp instanceof List) {
            List l = (List) disp;
            if (l.m.type == Choice.IMPLICIT && l.selectCommand != null && !disp.commands.contains(l.selectCommand)) extra = l.selectCommand;
        } else if (disp instanceof Alert && ((Alert) disp).implicitDismiss()) {
            extra = Alert.DISMISS_COMMAND;
            flagsFor = CF_DISMISS;
        }
        Vector cs = disp.commands;
        synchronized (cs) {
            w(cs.size() + (extra == null ? 0 : 1));
            for (int i = 0; i < cs.size(); i++) {
                Command c = (Command) cs.elementAt(i);
                int f = 0;
                if (disp instanceof List && ((List) disp).m.type == Choice.IMPLICIT && c == ((List) disp).selectCommand) f = CF_SELECT;
                cmd(c, f);
            }
            if (extra != null) cmd(extra, flagsFor == 0 ? CF_SELECT : flagsFor);
        }
    }

    static void choice(ChoiceModel m) {
        synchronized (m) {
            w(m.type);
            w(m.fit);
            w(m.n);
            for (int i = 0; i < m.n; i++) {
                ws(m.str[i]);
                w(m.img[i] == null ? 0 : m.img[i].peer);
                w((m.font[i] != null ? m.font[i] : Font.getDefaultFont()).key());
                w(m.sel[i] ? 1 : 0);
            }
        }
    }

    static void text(TextModel m) {
        synchronized (m) {
            ws(m.getString());
            w(m.constraints);
            w(m.maxSize);
            w(m.caret);
            ws(m.inputMode);
        }
    }

    static void listBody(List l) {
        choice(l.m);
    }

    static void alertBody(Alert a) {
        ws(a.text0());
        Image im = a.image0();
        w(im == null ? 0 : im.peer);
        w(a.type0());
        w(a.getTimeout());
        w(a.modal() ? 1 : 0);
        Gauge g = a.indicator0();
        if (g == null) {
            w(0);
        } else {
            w(1);
            item(g, false);
        }
    }

    static void formBody(Form f) {
        Vector items = f.items0();
        synchronized (items) {
            w(f.focus == null ? 0 : f.focus.iid);
            w(items.size());
            for (int i = 0; i < items.size(); i++) item((Item) items.elementAt(i), true);
        }
    }

    private static void item(Item it, boolean inForm) {
        w(it.iid);
        w(it.uiKind());
        ws(it.label);
        w(it.layout);
        w(it.prefW);
        w(it.prefH);
        w(it.getPreferredWidth());
        w(it.getPreferredHeight());
        w(it.getMinimumWidth());
        w(it.getMinimumHeight());
        Vector cs = it.commands;
        synchronized (cs) {
            w(cs.size());
            for (int i = 0; i < cs.size(); i++) {
                Command c = (Command) cs.elementAt(i);
                cmd(c, c == it.defaultCommand ? CF_DEFAULT : 0);
            }
        }
        it.encode();
    }
}
