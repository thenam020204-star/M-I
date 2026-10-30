package jlkvm.host.desktop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jlkvm.Vm;
import jlkvm.host.HostEvents;
import jlkvm.host.ui.ScreenHost;
import jlkvm.host.ui.ScreenModel;

/**
 * The headless {@link ScreenHost}: remembers the screen description the guest sent last and turns script lines ({@code ui ...}) into the
 * user actions a real UI would report (PLAN T24). Verbs ({@link #perform}):
 * <pre>
 * ui dump [label]                       record the current screen description (text form of ScreenModel.dump)
 * ui cmd LABEL                          activate a screen command by label      ui dismiss      the implicit Alert dismiss command
 * ui select IDX [0|1]                   List: pick element IDX (multiple choice: set/clear, default toggle)
 * ui text STRING                        TextBox: the user typed STRING (replaces the contents)
 * ui item N cmd LABEL                   activate a command of item N (0-based index in the Form)
 * ui item N choice IDX [0|1]            ChoiceGroup element        ui item N text STRING     TextField     ui item N gauge V
 * ui item N date MILLIS|clear           DateField                  ui item N custom CODE P   CustomItem event (HostEvents.UI_CUSTOM)
 * </pre>
 */
public final class HeadlessUi implements ScreenHost {
    public volatile ScreenModel current;
    /** Text form of every description received, in order. */
    public final List<String> history = Collections.synchronizedList(new ArrayList<String>());
    /** Results of {@code ui dump} lines: "label\n" + description. */
    public final List<String> dumps = new ArrayList<String>();
    public final List<String> errors = new ArrayList<String>();
    public final List<String> traversals = Collections.synchronizedList(new ArrayList<String>());
    public volatile int screens;
    private final Map<Integer, String> texts = new ConcurrentHashMap<Integer, String>();

    public void screen(ScreenModel s) {
        current = s;
        screens++;
        history.add(s.dump());
    }

    public String text(int id) { return texts.get(id); }

    public void traverseResult(int itemId, int dir, boolean inside, int x, int y, int w, int h) {
        traversals.add("item " + itemId + " dir " + dir + " inside=" + inside + " rect " + x + "," + y + "," + w + "," + h);
    }

    private void err(String m) { errors.add(m); }

    private static ScreenModel.Cmd byLabel(List<ScreenModel.Cmd> cs, String label) {
        for (ScreenModel.Cmd c : cs) if (label.equals(c.label)) return c;
        return null;
    }

    /** Executes one {@code ui} script line (the text after "ui"). @return true when an event was posted. */
    public boolean perform(String line, Vm vm) {
        ScreenModel m = current;
        String[] t = line.trim().split("\\s+", 2);
        String verb = t[0], rest = t.length > 1 ? t[1] : "";
        if (verb.equals("dump")) {
            dumps.add((rest.isEmpty() ? "dump" : rest) + "\n" + (m == null ? "no screen\n" : m.dump()));
            return false;
        }
        if (m == null) { err("no screen for: ui " + line); return false; }
        try {
            if (verb.equals("cmd")) {
                ScreenModel.Cmd c = byLabel(m.commands, rest);
                if (c == null) { err("no command '" + rest + "' on the screen"); return false; }
                vm.postEvent(HostEvents.UI_COMMAND, m.id, c.id, 0);
                return true;
            }
            if (verb.equals("dismiss")) {
                for (ScreenModel.Cmd c : m.commands) if (c.isDismiss()) { vm.postEvent(HostEvents.UI_COMMAND, m.id, c.id, 0); return true; }
                err("no dismiss command");
                return false;
            }
            if (verb.equals("select")) {
                if (m.choice == null) { err("not a list"); return false; }
                String[] a = rest.split("\\s+");
                int idx = Integer.parseInt(a[0]);
                int f = flag(m.choice, idx, a);
                applyChoice(m.choice, idx, f != 0);
                vm.postEvent(HostEvents.UI_CHOICE, m.id, idx, f);
                return true;
            }
            if (verb.equals("text")) {
                if (m.text == null) { err("not a text box"); return false; }
                texts.put(m.id, rest);
                if (rest.length() <= m.text.maxSize) m.text.text = rest;   // the widget shows what the user typed (the guest may reject it)
                vm.postEvent(HostEvents.UI_TEXT, m.id, 0, 0);
                return true;
            }
            if (verb.equals("item")) {
                String[] a = rest.split("\\s+", 3);
                int n = Integer.parseInt(a[0]);
                ScreenModel.Item it = m.kind == ScreenModel.ALERT && n == 0 && m.indicator != null ? m.indicator : n < m.items.size() ? m.items.get(n) : null;
                if (it == null) { err("no item " + n); return false; }
                String what = a[1], arg = a.length > 2 ? a[2] : "";
                if (what.equals("cmd")) {
                    ScreenModel.Cmd c = byLabel(it.commands, arg);
                    if (c == null) { err("no command '" + arg + "' on item " + n); return false; }
                    vm.postEvent(HostEvents.UI_ITEM_COMMAND, it.id, c.id, 0);
                } else if (what.equals("choice")) {
                    if (it.choice == null) { err("item " + n + " is not a choice"); return false; }
                    String[] b = arg.split("\\s+");
                    int idx = Integer.parseInt(b[0]);
                    int f = flag(it.choice, idx, b);
                    applyChoice(it.choice, idx, f != 0);
                    vm.postEvent(HostEvents.UI_CHOICE, it.id, idx, f);
                } else if (what.equals("text")) {
                    if (it.txt == null) { err("item " + n + " is not a text field"); return false; }
                    texts.put(it.id, arg);
                    if (arg.length() <= it.txt.maxSize) it.txt.text = arg;
                    vm.postEvent(HostEvents.UI_TEXT, it.id, 0, 0);
                } else if (what.equals("gauge")) {
                    int v = Integer.parseInt(arg.trim());
                    it.value = Math.max(0, Math.min(it.max, v));
                    vm.postEvent(HostEvents.UI_GAUGE, it.id, v, 0);
                } else if (what.equals("date")) {
                    if (arg.trim().equals("clear")) { it.hasDate = false; vm.postEvent(HostEvents.UI_DATE_CLEAR, it.id, 0, 0); }
                    else { long ms = Long.parseLong(arg.trim()); it.hasDate = true; it.date = ms; vm.postEvent(HostEvents.UI_DATE, it.id, (int) (ms >>> 32), (int) ms); }
                } else if (what.equals("custom")) {
                    String[] b = arg.split("\\s+");
                    vm.postEvent(HostEvents.UI_CUSTOM, it.id, Integer.parseInt(b[0]), b.length > 1 ? Integer.parseInt(b[1]) : 0);
                } else { err("unknown item action " + what); return false; }
                return true;
            }
            err("unknown ui verb: " + verb);
        } catch (RuntimeException e) {
            err("ui " + line + ": " + e);
        }
        return false;
    }

    /** The widget state after the user's action (the guest does not re-send host-originated changes). */
    private static void applyChoice(ScreenModel.Choice c, int idx, boolean on) {
        if (idx < 0 || idx >= c.elems.size()) return;
        if (c.type == 2) c.elems.get(idx).selected = on;
        else for (int i = 0; i < c.elems.size(); i++) c.elems.get(i).selected = i == idx;
    }

    private static int flag(ScreenModel.Choice c, int idx, String[] a) {
        if (a.length > 1) return Integer.parseInt(a[1]);
        if (c.type == 2 && idx >= 0 && idx < c.elems.size()) return c.elems.get(idx).selected ? 0 : 1;   // MULTIPLE toggles
        return 1;
    }
}
