package jlkvm.host.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * What the guest tells the host about the current displayable (PLAN T24, docs/midp.md section 6): the screen kind, title, ticker,
 * commands and, for a Form, the items. Decoded from the int[] / String[] pair the guest {@code lcdui.ScreenCodec} writes (same
 * numbers). The guest owns the model; a host (Android widgets, the headless test UI) only shows it and reports user actions back with
 * {@code Vm.postEvent} ({@link jlkvm.host.HostEvents}).
 */
public final class ScreenModel {
    public static final int MAGIC = 0x4A4C5549;
    public static final int CANVAS = 1, FORM = 2, LIST = 3, TEXTBOX = 4, ALERT = 5;
    public static final int I_STRING = 1, I_IMAGE = 2, I_TEXT = 3, I_DATE = 4, I_GAUGE = 5, I_CHOICE = 6, I_SPACER = 7, I_CUSTOM = 8;
    public static final int CF_SELECT = 1, CF_DISMISS = 2, CF_DEFAULT = 4;
    public static final String[] KIND_NAMES = {"?", "CANVAS", "FORM", "LIST", "TEXTBOX", "ALERT"};
    public static final String[] ITEM_NAMES = {"?", "STRING", "IMAGE", "TEXT", "DATE", "GAUGE", "CHOICE", "SPACER", "CUSTOM"};
    public static final String[] ALERT_TYPES = {"none", "ALARM", "CONFIRMATION", "ERROR", "INFO", "WARNING"};
    public static final String[] CHOICE_TYPES = {"?", "EXCLUSIVE", "MULTIPLE", "IMPLICIT", "POPUP"};

    public static final class Cmd {
        public int id, type, priority, flags;
        public String label, longLabel;
        public boolean isSelect() { return (flags & CF_SELECT) != 0; }
        public boolean isDismiss() { return (flags & CF_DISMISS) != 0; }
        public boolean isDefault() { return (flags & CF_DEFAULT) != 0; }
    }

    public static final class Elem {
        public String text;
        public int image, font;
        public boolean selected;
    }

    public static final class Choice {
        public int type, fit;
        public final List<Elem> elems = new ArrayList<Elem>();
        public int selectedIndex() {
            for (int i = 0; i < elems.size(); i++) if (elems.get(i).selected) return i;
            return -1;
        }
    }

    public static final class Text {
        public String text, inputMode;
        public int constraints, maxSize, caret;
    }

    public static final class Item {
        public int id, kind, layout, lockedW, lockedH, prefW, prefH, minW, minH;
        public String label;
        public final List<Cmd> commands = new ArrayList<Cmd>();
        // STRING
        public String text;
        public int appearance, font;
        // IMAGE
        public int image;
        public String alt;
        // TEXT
        public Text txt;
        // DATE
        public int dateMode, tzOffset;
        public boolean hasDate;
        public long date;
        // GAUGE
        public boolean interactive;
        public int max, value;
        // CHOICE
        public Choice choice;
        // CUSTOM
        public int contentW, contentH, contentImage, interactionModes;

        public Cmd defaultCommand() {
            for (Cmd c : commands) if (c.isDefault()) return c;
            return null;
        }
    }

    public int id, kind;
    public boolean fullScreen;
    public String title, ticker;
    public final List<Cmd> commands = new ArrayList<Cmd>();
    // FORM
    public int focusItem;
    public final List<Item> items = new ArrayList<Item>();
    // LIST
    public Choice choice;
    // TEXTBOX
    public Text text;
    // ALERT
    public String alertText;
    public int alertImage, alertType, timeout;
    public boolean modal;
    public Item indicator;

    /** Finds the pixels behind an Image handle of the description. */
    public interface Images {
        /** @return the surface of {@code handle} or null. */
        Surface surface(int handle);
    }

    /** Set by the natives while {@link ScreenHost#screen} runs (vm-thread): the host copies the images it needs there ({@link #image}); never use it later. */
    public Images images;

    /** @return a copy of the pixels of the Image handle {@code h} (0 = none), or null. */
    public ImageSnap image(int h) {
        Images i = images;
        return h == 0 || i == null ? null : ImageSnap.of(i.surface(h));
    }

    private static final class Reader {
        final int[] d;
        final String[] s;
        int p;
        Reader(int[] d, String[] s) { this.d = d; this.s = s; }
        int i() { return d[p++]; }
        boolean b() { return d[p++] != 0; }
        String str() { int k = d[p++]; return k < 0 ? null : s[k]; }
    }

    /** @param data the ints (length = number written), @param strings the string table. @throws IllegalArgumentException on a malformed description. */
    public static ScreenModel decode(int[] data, String[] strings) {
        try {
            Reader r = new Reader(data, strings);
            if (r.i() != MAGIC) throw new IllegalArgumentException("bad magic");
            ScreenModel m = new ScreenModel();
            m.id = r.i();
            m.kind = r.i();
            m.fullScreen = (r.i() & 1) != 0;
            m.title = r.str();
            m.ticker = r.str();
            for (int n = r.i(); n > 0; n--) m.commands.add(cmd(r));
            switch (m.kind) {
                case CANVAS: break;
                case FORM:
                    m.focusItem = r.i();
                    for (int n = r.i(); n > 0; n--) m.items.add(item(r));
                    break;
                case LIST: m.choice = choice(r); break;
                case TEXTBOX: m.text = text(r); break;
                case ALERT:
                    m.alertText = r.str();
                    m.alertImage = r.i();
                    m.alertType = r.i();
                    m.timeout = r.i();
                    m.modal = r.b();
                    if (r.b()) m.indicator = item(r);
                    break;
                default: throw new IllegalArgumentException("kind " + m.kind);
            }
            if (r.p != data.length) throw new IllegalArgumentException("trailing ints: " + (data.length - r.p));
            return m;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("truncated description");
        }
    }

    private static Cmd cmd(Reader r) {
        Cmd c = new Cmd();
        c.id = r.i();
        c.type = r.i();
        c.priority = r.i();
        c.flags = r.i();
        c.label = r.str();
        c.longLabel = r.str();
        return c;
    }

    private static Choice choice(Reader r) {
        Choice c = new Choice();
        c.type = r.i();
        c.fit = r.i();
        for (int n = r.i(); n > 0; n--) {
            Elem e = new Elem();
            e.text = r.str();
            e.image = r.i();
            e.font = r.i();
            e.selected = r.b();
            c.elems.add(e);
        }
        return c;
    }

    private static Text text(Reader r) {
        Text t = new Text();
        t.text = r.str();
        t.constraints = r.i();
        t.maxSize = r.i();
        t.caret = r.i();
        t.inputMode = r.str();
        return t;
    }

    private static Item item(Reader r) {
        Item it = new Item();
        it.id = r.i();
        it.kind = r.i();
        it.label = r.str();
        it.layout = r.i();
        it.lockedW = r.i();
        it.lockedH = r.i();
        it.prefW = r.i();
        it.prefH = r.i();
        it.minW = r.i();
        it.minH = r.i();
        for (int n = r.i(); n > 0; n--) it.commands.add(cmd(r));
        switch (it.kind) {
            case I_STRING: it.text = r.str(); it.appearance = r.i(); it.font = r.i(); break;
            case I_IMAGE: it.image = r.i(); it.alt = r.str(); it.appearance = r.i(); break;
            case I_TEXT: it.txt = text(r); break;
            case I_DATE:
                it.dateMode = r.i();
                it.hasDate = r.b();
                long hi = r.i(), lo = r.i();
                it.date = (hi << 32) | (lo & 0xFFFFFFFFL);
                it.tzOffset = r.i();
                break;
            case I_GAUGE: it.interactive = r.b(); it.max = r.i(); it.value = r.i(); break;
            case I_CHOICE: it.choice = choice(r); break;
            case I_SPACER: break;
            case I_CUSTOM: it.contentW = r.i(); it.contentH = r.i(); it.contentImage = r.i(); it.interactionModes = r.i(); break;
            default: throw new IllegalArgumentException("item kind " + it.kind);
        }
        return it;
    }

    /** Deterministic text form of the screen (ids left out): what scripts and tests compare. */
    public String dump() {
        StringBuilder b = new StringBuilder();
        b.append("screen ").append(KIND_NAMES[kind]).append(" title=").append(q(title));
        if (ticker != null) b.append(" ticker=").append(q(ticker));
        if (kind == CANVAS && fullScreen) b.append(" fullscreen");
        b.append('\n');
        cmds(b, commands, "  ");
        switch (kind) {
            case FORM:
                for (int i = 0; i < items.size(); i++) item(b, i, items.get(i), "  ");
                break;
            case LIST: choice(b, choice, "  "); break;
            case TEXTBOX: text(b, text, "  "); break;
            case ALERT:
                b.append("  alert type=").append(ALERT_TYPES[alertType]).append(" text=").append(q(alertText)).append(" timeout=").append(timeout)
                        .append(modal ? " modal" : "").append(alertImage != 0 ? " image" : "").append('\n');
                if (indicator != null) item(b, 0, indicator, "  indicator ");
                break;
            default: break;
        }
        return b.toString();
    }

    private static String q(String s) { return s == null ? "null" : '"' + s.replace("\n", "\\n") + '"'; }

    private static void cmds(StringBuilder b, List<Cmd> cs, String ind) {
        for (Cmd c : cs) {
            b.append(ind).append("command ").append(q(c.label)).append(" type=").append(c.type).append(" prio=").append(c.priority);
            if (c.longLabel != null) b.append(" long=").append(q(c.longLabel));
            if (c.isSelect()) b.append(" select");
            if (c.isDismiss()) b.append(" dismiss");
            if (c.isDefault()) b.append(" default");
            b.append('\n');
        }
    }

    private static void choice(StringBuilder b, Choice c, String ind) {
        b.append(ind).append("choice ").append(CHOICE_TYPES[c.type]).append(" fit=").append(c.fit).append('\n');
        for (int i = 0; i < c.elems.size(); i++) {
            Elem e = c.elems.get(i);
            b.append(ind).append("  [").append(i).append("] ").append(e.selected ? '*' : ' ').append(q(e.text)).append(e.image != 0 ? " image" : "").append('\n');
        }
    }

    private static void text(StringBuilder b, Text t, String ind) {
        b.append(ind).append("text ").append(q(t.text)).append(" constraints=0x").append(Integer.toHexString(t.constraints)).append(" max=").append(t.maxSize)
                .append(" caret=").append(t.caret);
        if (t.inputMode != null) b.append(" mode=").append(q(t.inputMode));
        b.append('\n');
    }

    private static void item(StringBuilder b, int idx, Item it, String ind) {
        b.append(ind).append("item ").append(idx).append(' ').append(ITEM_NAMES[it.kind]).append(" label=").append(q(it.label));
        if (it.layout != 0) b.append(" layout=").append(it.layout);
        b.append('\n');
        String in2 = ind + "  ";
        switch (it.kind) {
            case I_STRING: b.append(in2).append("text=").append(q(it.text)).append(" mode=").append(it.appearance).append('\n'); break;
            case I_IMAGE: b.append(in2).append("image=").append(it.image != 0).append(" alt=").append(q(it.alt)).append(" mode=").append(it.appearance).append('\n'); break;
            case I_TEXT: text(b, it.txt, in2); break;
            case I_DATE:
                b.append(in2).append("date mode=").append(it.dateMode).append(" value=").append(it.hasDate ? Long.toString(it.date) : "null").append('\n');
                break;
            case I_GAUGE: b.append(in2).append("gauge ").append(it.interactive ? "interactive" : "static").append(" value=").append(it.value).append('/').append(it.max).append('\n'); break;
            case I_CHOICE: choice(b, it.choice, in2); break;
            case I_SPACER: b.append(in2).append("spacer ").append(it.minW).append('x').append(it.minH).append('\n'); break;
            case I_CUSTOM: b.append(in2).append("custom content=").append(it.contentW).append('x').append(it.contentH).append(" modes=").append(it.interactionModes).append('\n'); break;
            default: break;
        }
        cmds(b, it.commands, in2);
    }
}
