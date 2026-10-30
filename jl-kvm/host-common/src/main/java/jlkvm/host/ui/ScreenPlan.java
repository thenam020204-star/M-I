package jlkvm.host.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The view-neutral description of what a host with real widgets shows for one {@link ScreenModel} (PLAN T35c): the rows of a Form or
 * List, the text box, the Alert buttons, the soft-key bar derived from the commands, input kinds, gauge modes and font looks. No
 * android.* here: host-android's {@code AndroidScreenHost} only turns a plan into views, so everything that decides WHAT is shown is
 * tested on the JVM ({@code ScreenPlanTest}). Built on the vm-thread (images are copied there), used on the UI thread.
 */
public final class ScreenPlan {
    // ---- Command types (lcdui.Command)
    public static final int CT_SCREEN = 1, CT_BACK = 2, CT_CANCEL = 3, CT_OK = 4, CT_HELP = 5, CT_STOP = 6, CT_EXIT = 7, CT_ITEM = 8;

    // ---- row kinds
    public static final int R_STRING = 1, R_LINK = 2, R_BUTTON = 3, R_IMAGE = 4, R_EDIT = 5, R_DATE = 6, R_GAUGE = 7, R_CHOICE = 8, R_SPACER = 9, R_CUSTOM = 10,
            R_LIST_ELEMENT = 11;
    // ---- choice widgets
    public static final int C_RADIO = 1, C_CHECK = 2, C_SPINNER = 3, C_IMPLICIT = 4;
    // ---- text input kinds
    public static final int IN_TEXT = 0, IN_EMAIL = 1, IN_NUMBER = 2, IN_PHONE = 3, IN_URL = 4, IN_DECIMAL = 5;
    // ---- horizontal placement
    public static final int G_DEFAULT = 0, G_LEFT = 1, G_RIGHT = 2, G_CENTER = 3;
    // ---- date field modes (lcdui.DateField)
    public static final int D_DATE = 1, D_TIME = 2, D_DATE_TIME = 3;

    /** Look of a lcdui.Font key (face | style | size). */
    public static final class FontLook {
        public final boolean bold, italic, underline, mono;
        /** -1 small, 0 medium, 1 large. */
        public final int size;
        FontLook(int key) {
            bold = (key & 1) != 0;
            italic = (key & 2) != 0;
            underline = (key & 4) != 0;
            mono = (key & 32) != 0;
            size = (key & 8) != 0 ? -1 : (key & 16) != 0 ? 1 : 0;
        }
        @Override public String toString() { return (bold ? "b" : "") + (italic ? "i" : "") + (underline ? "u" : "") + (mono ? "m" : "") + size; }
    }

    public static final class Cmd {
        public final int id, type, priority;
        public final String label, longLabel;
        Cmd(ScreenModel.Cmd c) {
            id = c.id; type = c.type; priority = c.priority; label = c.label == null ? "" : c.label; longLabel = c.longLabel;
        }
        /** The text a button shows: the long label when there is room. */
        public String text(boolean roomy) { return roomy && longLabel != null && longLabel.length() > 0 ? longLabel : label; }
        @Override public String toString() { return label + "#" + id; }
    }

    /** The soft-key style bar of a screen: two buttons, the right one is the "back" command. */
    public static final class CmdBar {
        /** Left button command, or null. */
        public Cmd left;
        /** Right button command, or null. */
        public Cmd right;
        /** When not empty the left button is a "menu" button opening these commands (priority order). */
        public final List<Cmd> menu = new ArrayList<Cmd>();
        /** The command the back key runs (BACK / CANCEL / EXIT / STOP of the lowest priority number), or null. */
        public Cmd back;
        public boolean isEmpty() { return left == null && right == null && menu.isEmpty(); }
        /** Every command of the bar, ordered. */
        public List<Cmd> all() {
            List<Cmd> r = new ArrayList<Cmd>();
            if (!menu.isEmpty()) r.addAll(menu); else if (left != null) r.add(left);
            if (right != null) r.add(right);
            return r;
        }
    }

    public static final class Option {
        public String text;
        public ImageSnap image;
        public FontLook font;
        public boolean selected;
    }

    public static final class Input {
        public int kind;
        public boolean password, editable = true, sensitive, noSuggestions, capsWords, capsSentences;
        public int maxSize;
        /** Emails, URLs, numbers... must not be corrected or capitalised. */
        public boolean rawText() { return kind != IN_TEXT; }
    }

    public static final class Row {
        public int id, kind, gravity = G_DEFAULT;
        public String label;
        public boolean newlineBefore, newlineAfter, expandH, shrinkH;
        // STRING / LINK / BUTTON
        public String text;
        public FontLook font;
        // IMAGE / CUSTOM
        public ImageSnap image;
        public String alt;
        // EDIT
        public Input input;
        // DATE
        public int dateMode;
        public boolean hasDate;
        public long date;
        public int tzOffset;
        // GAUGE
        public boolean indefinite, interactive;
        public int max, value;
        // CHOICE / LIST_ELEMENT
        public int widget;
        public final List<Option> options = new ArrayList<Option>();
        // SPACER / CUSTOM sizes
        public int w, h;
        public int interactionModes;
        // item commands: the default one runs on tap, the others on long press
        public final List<Cmd> commands = new ArrayList<Cmd>();
        public Cmd defaultCommand;
    }

    public static final class AlertPlan {
        public String text;
        public int type;
        public ImageSnap image;
        public int timeoutMs;
        public boolean modal, forever;
        public Row indicator;
        /** Buttons, in priority order; the dismiss command alone gives one "OK"-like button. */
        public final List<Cmd> buttons = new ArrayList<Cmd>();
        public Cmd dismiss;
    }

    public final int screenId, kind;
    public final boolean fullScreen;
    public final String title, ticker;
    public final CmdBar bar = new CmdBar();
    public final List<Row> rows = new ArrayList<Row>();
    /** TEXTBOX: the one edit row. */
    public Row textBox;
    public AlertPlan alert;
    /** Form: id of the item to bring on screen, 0 none. */
    public int focusItem;
    /** LIST: widget of the elements. */
    public int listWidget;

    private ScreenPlan(ScreenModel m) {
        screenId = m.id;
        kind = m.kind;
        fullScreen = m.fullScreen;
        title = m.title;
        ticker = m.ticker;
    }

    /** Builds the plan; call on the vm-thread (the images are copied from {@code m.images}). */
    public static ScreenPlan of(ScreenModel m) {
        ScreenPlan p = new ScreenPlan(m);
        fillBar(p.bar, m.commands);
        switch (m.kind) {
            case ScreenModel.FORM:
                p.focusItem = m.focusItem;
                for (ScreenModel.Item it : m.items) p.rows.add(row(m, it));
                break;
            case ScreenModel.LIST:
                p.listWidget = widget(m.choice.type);
                for (ScreenModel.Elem e : m.choice.elems) p.rows.add(listRow(m, p.listWidget, e, p.rows.size()));
                break;
            case ScreenModel.TEXTBOX: {
                Row r = new Row();
                r.id = m.id;
                r.kind = R_EDIT;
                r.text = m.text.text;
                r.input = input(m.text);
                p.textBox = r;
                break;
            }
            case ScreenModel.ALERT: {
                AlertPlan a = new AlertPlan();
                a.text = m.alertText;
                a.type = m.alertType;
                a.image = m.image(m.alertImage);
                a.timeoutMs = m.timeout;
                a.forever = m.timeout == -2;
                a.modal = m.modal;
                if (m.indicator != null) a.indicator = row(m, m.indicator);
                List<Cmd> cs = new ArrayList<Cmd>();
                for (ScreenModel.Cmd c : m.commands) {
                    cs.add(new Cmd(c));
                    if (c.isDismiss()) a.dismiss = cs.get(cs.size() - 1);
                }
                sort(cs);
                a.buttons.addAll(cs);
                p.alert = a;
                break;
            }
            default: break;
        }
        return p;
    }

    private static int widget(int choiceType) {
        switch (choiceType) {
            case 2: return C_CHECK;
            case 3: return C_IMPLICIT;
            case 4: return C_SPINNER;
            default: return C_RADIO;
        }
    }

    private static Row listRow(ScreenModel m, int widget, ScreenModel.Elem e, int index) {
        Row r = new Row();
        r.id = index;
        r.kind = R_LIST_ELEMENT;
        r.widget = widget;
        r.text = e.text;
        r.font = new FontLook(e.font);
        r.image = m.image(e.image);
        Option o = new Option();
        o.text = e.text;
        o.image = r.image;
        o.font = r.font;
        o.selected = e.selected;
        r.options.add(o);
        return r;
    }

    private static Input input(ScreenModel.Text t) {
        Input in = new Input();
        switch (t.constraints & 0xFFFF) {
            case 1: in.kind = IN_EMAIL; break;
            case 2: in.kind = IN_NUMBER; break;
            case 3: in.kind = IN_PHONE; break;
            case 4: in.kind = IN_URL; break;
            case 5: in.kind = IN_DECIMAL; break;
            default: in.kind = IN_TEXT; break;
        }
        in.password = (t.constraints & 0x10000) != 0;
        in.editable = (t.constraints & 0x20000) == 0;
        in.sensitive = (t.constraints & 0x40000) != 0;
        in.noSuggestions = (t.constraints & 0x80000) != 0;
        in.capsWords = (t.constraints & 0x100000) != 0;
        in.capsSentences = (t.constraints & 0x200000) != 0;
        in.maxSize = t.maxSize;
        return in;
    }

    private static Row row(ScreenModel m, ScreenModel.Item it) {
        Row r = new Row();
        r.id = it.id;
        r.label = it.label;
        int h = it.layout & 3;
        r.gravity = h == 1 ? G_LEFT : h == 2 ? G_RIGHT : h == 3 ? G_CENTER : G_DEFAULT;
        r.newlineBefore = (it.layout & 256) != 0;
        r.newlineAfter = (it.layout & 512) != 0;
        r.expandH = (it.layout & 2048) != 0;
        r.shrinkH = (it.layout & 1024) != 0;
        for (ScreenModel.Cmd c : it.commands) r.commands.add(new Cmd(c));
        sort(r.commands);
        ScreenModel.Cmd d = it.defaultCommand();
        if (d != null) for (Cmd c : r.commands) if (c.id == d.id) r.defaultCommand = c;
        switch (it.kind) {
            case ScreenModel.I_STRING:
                r.kind = it.appearance == 1 ? R_LINK : it.appearance == 2 ? R_BUTTON : R_STRING;
                r.text = it.text;
                r.font = new FontLook(it.font);
                break;
            case ScreenModel.I_IMAGE:
                r.kind = it.appearance == 2 ? R_BUTTON : R_IMAGE;
                r.image = m.image(it.image);
                r.alt = it.alt;
                if (r.kind == R_BUTTON) r.text = it.alt != null ? it.alt : it.label;
                break;
            case ScreenModel.I_TEXT:
                r.kind = R_EDIT;
                r.text = it.txt.text;
                r.input = input(it.txt);
                break;
            case ScreenModel.I_DATE:
                r.kind = R_DATE;
                r.dateMode = it.dateMode;
                r.hasDate = it.hasDate;
                r.date = it.date;
                r.tzOffset = it.tzOffset;
                break;
            case ScreenModel.I_GAUGE:
                r.kind = R_GAUGE;
                r.interactive = it.interactive;
                r.indefinite = it.max == -1;
                r.max = it.max;
                r.value = it.value;
                break;
            case ScreenModel.I_CHOICE:
                r.kind = R_CHOICE;
                r.widget = widget(it.choice.type);
                for (ScreenModel.Elem e : it.choice.elems) {
                    Option o = new Option();
                    o.text = e.text;
                    o.image = m.image(e.image);
                    o.font = new FontLook(e.font);
                    o.selected = e.selected;
                    r.options.add(o);
                }
                break;
            case ScreenModel.I_SPACER:
                r.kind = R_SPACER;
                r.w = it.minW;
                r.h = it.minH;
                break;
            case ScreenModel.I_CUSTOM:
                r.kind = R_CUSTOM;
                r.w = it.contentW;
                r.h = it.contentH;
                r.image = m.image(it.contentImage);
                r.interactionModes = it.interactionModes;
                break;
            default: throw new IllegalArgumentException("item kind " + it.kind);
        }
        return r;
    }

    // ------------------------------------------------------------------ commands

    private static int rank(int type) {
        switch (type) {
            case CT_OK: return 0;
            case CT_SCREEN: return 1;
            case CT_ITEM: return 2;
            case CT_HELP: return 3;
            default: return 4;   // BACK, CANCEL, STOP, EXIT
        }
    }

    static boolean isBackType(int type) { return type == CT_BACK || type == CT_CANCEL || type == CT_EXIT || type == CT_STOP; }

    /** Priority number first (1 = most important), then OK before the others, then the order the guest gave (stable). */
    private static void sort(List<Cmd> cs) {
        Collections.sort(cs, new Comparator<Cmd>() {
            public int compare(Cmd a, Cmd b) {
                if (a.priority != b.priority) return a.priority < b.priority ? -1 : 1;
                return rank(a.type) - rank(b.type);
            }
        });
    }

    static void fillBar(CmdBar bar, List<ScreenModel.Cmd> src) {
        List<Cmd> all = new ArrayList<Cmd>();
        for (ScreenModel.Cmd c : src) all.add(new Cmd(c));
        sort(all);
        Cmd back = null;
        for (Cmd c : all) if (isBackType(c.type)) { back = c; break; }
        bar.back = back;
        List<Cmd> rest = new ArrayList<Cmd>(all);
        if (back != null) rest.remove(back);
        if (back == null && rest.size() == 2) {
            bar.left = rest.get(0);
            bar.right = rest.get(1);
            return;
        }
        bar.right = back;
        if (rest.size() == 1) bar.left = rest.get(0);
        else if (rest.size() > 1) bar.menu.addAll(rest);
    }

    // ------------------------------------------------------------------ keys of the update logic

    /**
     * Describes everything that makes the widgets different, WITHOUT the state the user edits (typed text, selection, gauge value,
     * date): two plans of the same screen with equal keys can be shown by updating the existing widgets in place (the caret and the
     * keyboard stay), see {@link #sameStructure}.
     */
    public String structureKey() {
        StringBuilder b = new StringBuilder();
        b.append(screenId).append('/').append(kind).append('/').append(fullScreen).append('/').append(title).append('/').append(ticker).append('/');
        for (Cmd c : bar.all()) b.append(c.id).append(':').append(c.label).append(',');
        b.append('|').append(bar.back == null ? 0 : bar.back.id).append('|').append(focusItem).append('|').append(listWidget).append('|');
        for (Row r : rows) rowKey(b, r);
        if (textBox != null) rowKey(b, textBox);
        if (alert != null) {
            b.append("A").append(alert.text).append(alert.type).append(alert.timeoutMs).append(alert.modal).append(imgKey(alert.image));
            for (Cmd c : alert.buttons) b.append(c.id).append(c.label).append(',');
            if (alert.indicator != null) rowKey(b, alert.indicator);
        }
        return b.toString();
    }

    private static String imgKey(ImageSnap s) {
        if (s == null) return "-";
        int h = s.w * 31 + s.h;
        for (int i = 0; i < s.argb.length; i++) h = h * 31 + s.argb[i];
        return s.w + "x" + s.h + "#" + h;
    }

    private static void rowKey(StringBuilder b, Row r) {
        b.append('[').append(r.id).append(',').append(r.kind).append(',').append(r.gravity).append(r.newlineBefore).append(r.newlineAfter).append(r.expandH)
                .append(r.shrinkH).append(',').append(r.label).append(',').append(r.font).append(',').append(r.alt).append(',').append(imgKey(r.image));
        if (r.kind == R_STRING || r.kind == R_LINK || r.kind == R_BUTTON) b.append(',').append(r.text);
        if (r.input != null) {
            Input i = r.input;
            b.append(",in").append(i.kind).append(i.password).append(i.editable).append(i.sensitive).append(i.noSuggestions).append(i.capsWords)
                    .append(i.capsSentences).append(i.maxSize);
        }
        if (r.kind == R_DATE) b.append(",d").append(r.dateMode).append(r.tzOffset);
        if (r.kind == R_GAUGE) b.append(",g").append(r.interactive).append(r.indefinite).append(r.max);
        b.append(",w").append(r.widget).append(",s").append(r.w).append('x').append(r.h).append(',').append(r.interactionModes);
        for (Option o : r.options) b.append(",o").append(o.text).append(o.font).append(imgKey(o.image));
        for (Cmd c : r.commands) b.append(",c").append(c.id).append(c.label);
        b.append(r.defaultCommand == null ? "" : ",dc" + r.defaultCommand.id);
        b.append(']');
    }

    /** true when {@code o} differs from this plan only in user-editable state (text, selection, gauge value, date). */
    public boolean sameStructure(ScreenPlan o) { return o != null && structureKey().equals(o.structureKey()); }
}
