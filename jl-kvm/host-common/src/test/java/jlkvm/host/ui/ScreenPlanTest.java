package jlkvm.host.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/** ScreenModel -> view-neutral plan (PLAN T35c): the decisions the Android widgets only execute. */
public class ScreenPlanTest {
    private static ScreenModel.Cmd cmd(int id, int type, int prio, String label) {
        ScreenModel.Cmd c = new ScreenModel.Cmd();
        c.id = id; c.type = type; c.priority = prio; c.label = label;
        return c;
    }

    private static ScreenModel form(ScreenModel.Cmd... cs) {
        ScreenModel m = new ScreenModel();
        m.id = 7; m.kind = ScreenModel.FORM; m.title = "T";
        for (ScreenModel.Cmd c : cs) m.commands.add(c);
        return m;
    }

    private static ScreenModel.Item item(int id, int kind) {
        ScreenModel.Item it = new ScreenModel.Item();
        it.id = id; it.kind = kind;
        return it;
    }

    private static String labels(List<ScreenPlan.Cmd> l) {
        StringBuilder b = new StringBuilder();
        for (ScreenPlan.Cmd c : l) b.append(b.length() > 0 ? "," : "").append(c.label);
        return b.toString();
    }

    @Test public void oneCommandIsTheLeftKey() {
        ScreenPlan p = ScreenPlan.of(form(cmd(1, ScreenPlan.CT_OK, 1, "Go")));
        assertEquals("Go", p.bar.left.label);
        assertNull(p.bar.right);
        assertNull(p.bar.back);
        assertTrue(p.bar.menu.isEmpty());
    }

    @Test public void backCommandIsTheRightKeyAndTheBackKey() {
        ScreenPlan p = ScreenPlan.of(form(cmd(1, ScreenPlan.CT_SCREEN, 2, "Opt"), cmd(2, ScreenPlan.CT_EXIT, 1, "Quit"), cmd(3, ScreenPlan.CT_BACK, 3, "Back")));
        assertEquals("Quit", p.bar.back.label);       // lowest priority number wins among the back-like types
        assertEquals("Quit", p.bar.right.label);
        assertEquals("Opt,Back", labels(p.bar.menu));  // the others are in the menu, ordered
        assertNull(p.bar.left);
    }

    @Test public void twoPlainCommandsFillBothKeys() {
        ScreenPlan p = ScreenPlan.of(form(cmd(1, ScreenPlan.CT_SCREEN, 5, "B"), cmd(2, ScreenPlan.CT_OK, 5, "A")));
        assertEquals("A", p.bar.left.label);           // same priority: OK first
        assertEquals("B", p.bar.right.label);
        assertTrue(p.bar.menu.isEmpty());
        assertEquals("A,B", labels(p.bar.all()));
    }

    @Test public void manyCommandsGoToTheMenuKeyInPriorityOrder() {
        ScreenPlan p = ScreenPlan.of(form(cmd(1, ScreenPlan.CT_SCREEN, 9, "C"), cmd(2, ScreenPlan.CT_SCREEN, 1, "A"), cmd(3, ScreenPlan.CT_HELP, 4, "B"),
                cmd(4, ScreenPlan.CT_CANCEL, 1, "X")));
        assertEquals("A,B,C", labels(p.bar.menu));
        assertNull(p.bar.left);
        assertEquals("X", p.bar.right.label);
        assertEquals(4, p.bar.all().size());
    }

    @Test public void noCommands() {
        assertTrue(ScreenPlan.of(form()).bar.isEmpty());
    }

    @Test public void formRows() {
        ScreenModel m = form();
        m.focusItem = 12;
        ScreenModel.Item s = item(10, ScreenModel.I_STRING);
        s.label = "L"; s.text = "hello"; s.appearance = 2; s.font = 1 | 16 | 32; s.layout = 3 | 256;
        s.commands.add(cmd(5, ScreenPlan.CT_ITEM, 1, "Do"));
        s.commands.get(0).flags = ScreenModel.CF_DEFAULT;
        s.commands.add(cmd(6, ScreenPlan.CT_ITEM, 0, "Zero"));
        m.items.add(s);
        ScreenModel.Item t = item(11, ScreenModel.I_TEXT);
        t.txt = new ScreenModel.Text(); t.txt.text = "ab"; t.txt.constraints = 5 | 0x10000 | 0x200000; t.txt.maxSize = 9;
        m.items.add(t);
        ScreenModel.Item g = item(12, ScreenModel.I_GAUGE);
        g.max = -1; g.value = 3;
        m.items.add(g);
        ScreenPlan p = ScreenPlan.of(m);
        assertEquals(3, p.rows.size());
        assertEquals(12, p.focusItem);
        ScreenPlan.Row r = p.rows.get(0);
        assertEquals(ScreenPlan.R_BUTTON, r.kind);
        assertEquals(ScreenPlan.G_CENTER, r.gravity);
        assertTrue(r.newlineBefore && !r.newlineAfter);
        assertTrue(r.font.bold && r.font.mono && r.font.size == 1 && !r.font.italic);
        assertEquals("Zero,Do", labels(r.commands));
        assertEquals(5, r.defaultCommand.id);
        ScreenPlan.Row e = p.rows.get(1);
        assertEquals(ScreenPlan.R_EDIT, e.kind);
        assertEquals(ScreenPlan.IN_DECIMAL, e.input.kind);
        assertTrue(e.input.password && e.input.editable && e.input.capsSentences && !e.input.capsWords && e.input.rawText());
        assertEquals(9, e.input.maxSize);
        ScreenPlan.Row gr = p.rows.get(2);
        assertTrue(gr.indefinite && !gr.interactive);
        assertEquals(3, gr.value);
    }

    @Test public void choiceWidgets() {
        ScreenModel m = form();
        int[] types = {1, 2, 3, 4};
        int[] want = {ScreenPlan.C_RADIO, ScreenPlan.C_CHECK, ScreenPlan.C_IMPLICIT, ScreenPlan.C_SPINNER};
        for (int i = 0; i < 4; i++) {
            ScreenModel.Item it = item(20 + i, ScreenModel.I_CHOICE);
            it.choice = new ScreenModel.Choice();
            it.choice.type = types[i];
            ScreenModel.Elem e = new ScreenModel.Elem();
            e.text = "e" + i; e.selected = i == 1;
            it.choice.elems.add(e);
            m.items.add(it);
        }
        ScreenPlan p = ScreenPlan.of(m);
        for (int i = 0; i < 4; i++) assertEquals(want[i], p.rows.get(i).widget);
        assertTrue(p.rows.get(1).options.get(0).selected);
    }

    @Test public void listRowsAndImages() {
        ScreenModel m = new ScreenModel();
        m.id = 3; m.kind = ScreenModel.LIST;
        m.choice = new ScreenModel.Choice();
        m.choice.type = 3;
        for (int i = 0; i < 3; i++) {
            ScreenModel.Elem e = new ScreenModel.Elem();
            e.text = "x" + i; e.image = i == 1 ? 77 : 0;
            m.choice.elems.add(e);
        }
        m.images = new ScreenModel.Images() {
            public Surface surface(int h) { return h == 77 ? new SoftRaster().createArgb(2, 3, 0x80112233) : null; }
        };
        ScreenPlan p = ScreenPlan.of(m);
        assertEquals(ScreenPlan.C_IMPLICIT, p.listWidget);
        assertEquals(3, p.rows.size());
        assertEquals(1, p.rows.get(1).id);
        assertNull(p.rows.get(0).image);
        ScreenPlan.Row r = p.rows.get(1);
        assertNotNull(r.image);
        assertEquals(2, r.image.w);
        assertEquals(3, r.image.h);
        assertEquals(0x80112233, r.image.argb[5]);
        m.images = null;
        assertNull(m.image(77));
    }

    @Test public void alertButtons() {
        ScreenModel m = new ScreenModel();
        m.id = 9; m.kind = ScreenModel.ALERT; m.alertText = "Sure?"; m.alertType = 2; m.timeout = -2; m.modal = true;
        ScreenModel.Cmd d = cmd(1, ScreenPlan.CT_OK, 0, "OK");
        d.flags = ScreenModel.CF_DISMISS;
        m.commands.add(d);
        ScreenPlan p = ScreenPlan.of(m);
        assertEquals("Sure?", p.alert.text);
        assertTrue(p.alert.forever && p.alert.modal);
        assertEquals(1, p.alert.buttons.size());
        assertEquals(1, p.alert.dismiss.id);
        ScreenModel.Item ind = item(40, ScreenModel.I_GAUGE);
        ind.max = 10; ind.value = 2;
        m.indicator = ind;
        assertEquals(2, ScreenPlan.of(m).alert.indicator.value);
    }

    @Test public void textBox() {
        ScreenModel m = new ScreenModel();
        m.id = 4; m.kind = ScreenModel.TEXTBOX;
        m.text = new ScreenModel.Text();
        m.text.text = "hi"; m.text.constraints = 0x20000 | 3; m.text.maxSize = 20;
        ScreenPlan p = ScreenPlan.of(m);
        assertEquals(4, p.textBox.id);
        assertEquals(ScreenPlan.IN_PHONE, p.textBox.input.kind);
        assertFalse(p.textBox.input.editable);
    }

    @Test public void structureIgnoresUserState() {
        ScreenModel a = form(), b = form();
        for (ScreenModel m : new ScreenModel[] {a, b}) {
            ScreenModel.Item t = item(11, ScreenModel.I_TEXT);
            t.txt = new ScreenModel.Text(); t.txt.text = m == a ? "x" : "xy"; t.txt.maxSize = 9;
            m.items.add(t);
            ScreenModel.Item c = item(12, ScreenModel.I_CHOICE);
            c.choice = new ScreenModel.Choice(); c.choice.type = 1;
            for (int i = 0; i < 2; i++) { ScreenModel.Elem e = new ScreenModel.Elem(); e.text = "e"; e.selected = (i == 0) == (m == a); c.choice.elems.add(e); }
            m.items.add(c);
            ScreenModel.Item g = item(13, ScreenModel.I_GAUGE);
            g.max = 10; g.interactive = true; g.value = m == a ? 1 : 9;
            m.items.add(g);
        }
        assertTrue(ScreenPlan.of(a).sameStructure(ScreenPlan.of(b)));
        b.items.get(0).txt.maxSize = 10;                                 // a real change
        assertFalse(ScreenPlan.of(a).sameStructure(ScreenPlan.of(b)));
        b.items.get(0).txt.maxSize = 9;
        b.items.get(1).choice.elems.get(0).text = "other";
        assertFalse(ScreenPlan.of(a).sameStructure(ScreenPlan.of(b)));
        assertFalse(ScreenPlan.of(a).sameStructure(null));
        List<ScreenPlan> none = new ArrayList<ScreenPlan>();
        assertTrue(none.isEmpty());
    }

    @Test public void decodeThenPlan() {
        // the real wire format -> plan (a Form with one string and a Back command)
        int[] d = {ScreenModel.MAGIC, 5, ScreenModel.FORM, 0, 0, -1,
                1, 70, 2, 1, 0, 0, -1,                   // one command: id 70, BACK, priority 1, label "s"
                0, 1,                                    // focus item, one item
                31, ScreenModel.I_STRING, -1, 0, 0, 0, 0, 0, 0, 0, 0,   // item 31: no label, layout, sizes, no commands
                0, 0, 0};                                // text "s", appearance, font
        ScreenModel m = ScreenModel.decode(d, new String[] {"s"});
        ScreenPlan p = ScreenPlan.of(m);
        assertEquals(5, p.screenId);
        assertEquals("s", p.title);
        assertEquals(70, p.bar.back.id);
        assertEquals(1, p.rows.size());
        assertEquals(31, p.rows.get(0).id);
    }
}
