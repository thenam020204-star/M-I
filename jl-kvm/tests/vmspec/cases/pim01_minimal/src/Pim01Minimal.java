package vmspec;

import java.util.Enumeration;
import javax.microedition.midlet.MIDlet;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.FieldFullException;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.RepeatRule;
import javax.microedition.pim.ToDo;
import javax.microedition.pim.ToDoList;
import javax.microedition.pim.UnsupportedFieldException;

/** T32a: the PIM that games call (PIM.getInstance, listPIMLists, openPIMList, items, close, Contact.getString) plus enough of the rest to be self-consistent: in-memory lists, empty at start. */
public class Pim01Minimal extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static int count(Enumeration e) { int n = 0; while (e.hasMoreElements()) { e.nextElement(); n++; } return n; }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        PIM pim = PIM.getInstance();
        p("same instance " + (pim == PIM.getInstance()) + " constants " + PIM.CONTACT_LIST + PIM.EVENT_LIST + PIM.TODO_LIST + " " + PIM.READ_ONLY + PIM.WRITE_ONLY + PIM.READ_WRITE);
        String[] names = pim.listPIMLists(PIM.CONTACT_LIST);
        p("contact lists " + names.length + " " + names[0] + " event lists " + pim.listPIMLists(PIM.EVENT_LIST)[0] + " todo lists " + pim.listPIMLists(PIM.TODO_LIST)[0]);
        try { pim.listPIMLists(0); p("bad type: ok"); } catch (Throwable t) { p("bad type: " + cn(t)); }
        try { pim.openPIMList(PIM.CONTACT_LIST, 9); p("bad mode: ok"); } catch (Throwable t) { p("bad mode: " + cn(t)); }
        try { pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, null); p("null name: ok"); } catch (Throwable t) { p("null name: " + cn(t)); }
        try { pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, "nope"); p("unknown name: ok"); } catch (PIMException t) { p("unknown name: PIMException reason " + t.getReason()); }
        // the game's sequence
        ContactList cl = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, names[0]);
        p("list name " + cl.getName() + " items " + count(cl.items()) + " search " + count(cl.items("x")));
        cl.close();
        try { cl.items(); p("items after close: ok"); } catch (PIMException t) { p("items after close: PIMException reason " + t.getReason()); }
        try { cl.close(); p("close twice: ok"); } catch (PIMException t) { p("close twice: PIMException reason " + t.getReason()); }
        // fields
        cl = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
        p("fields " + cl.getSupportedFields().length + " TEL supported " + cl.isSupportedField(Contact.TEL) + " type " + cl.getFieldDataType(Contact.TEL) + " NAME type " + cl.getFieldDataType(Contact.NAME)
            + " array size " + cl.stringArraySize(Contact.NAME) + " max TEL " + cl.maxValues(Contact.TEL) + " max NAME " + cl.maxValues(Contact.NAME) + " max categories " + cl.maxCategories());
        try { cl.getFieldDataType(Event.START + 1000); p("unsupported field: ok"); } catch (UnsupportedFieldException t) { p("unsupported field: UnsupportedFieldException " + t.getField()); }
        Contact c = cl.createContact();
        p("new contact: modified " + c.isModified() + " fields " + c.getFields().length + " list ok " + (c.getPIMList() == cl));
        c.addString(Contact.FORMATTED_NAME, PIMItem.ATTR_NONE, "Alice");
        c.addString(Contact.TEL, Contact.ATTR_MOBILE, "0123");
        c.addString(Contact.TEL, Contact.ATTR_HOME | Contact.ATTR_PREFERRED, "0456");
        String[] nm = new String[cl.stringArraySize(Contact.NAME)];
        nm[Contact.NAME_FAMILY] = "Liddell";
        nm[Contact.NAME_GIVEN] = "Alice";
        c.addStringArray(Contact.NAME, PIMItem.ATTR_NONE, nm);
        try { c.addStringArray(Contact.NAME, PIMItem.ATTR_NONE, nm); p("second NAME: ok"); } catch (FieldFullException t) { p("second NAME: FieldFullException " + t.getField()); }
        try { c.addStringArray(Contact.ADDR, PIMItem.ATTR_NONE, nm); p("array of the wrong size: ok"); } catch (Throwable t) { p("array of the wrong size: " + cn(t)); }
        try { c.getString(Contact.TEL, 5); p("index 5: ok"); } catch (Throwable t) { p("index 5: " + cn(t)); }
        try { c.getInt(Contact.TEL, 0); p("wrong type: ok"); } catch (Throwable t) { p("wrong type: " + cn(t)); }
        try { c.addString(Contact.TEL, 0, null); p("null value: ok"); } catch (Throwable t) { p("null value: " + cn(t)); }
        p("modified " + c.isModified() + " fields " + c.getFields().length + " TEL count " + c.countValues(Contact.TEL) + " preferred " + c.getPreferredIndex(Contact.TEL) + " attrs " + c.getAttributes(Contact.TEL, 0)
            + " name[0] " + c.getStringArray(Contact.NAME, 0)[Contact.NAME_FAMILY] + " categories " + c.maxCategories());
        c.commit();
        p("after commit: modified " + c.isModified() + " list has " + count(cl.items()));
        c.setString(Contact.TEL, 0, Contact.ATTR_MOBILE, "9999");
        p("edited but not committed: stored tel " + ((Contact) cl.items().nextElement()).getString(Contact.TEL, 0) + " working copy " + c.getString(Contact.TEL, 0));
        c.commit();
        Contact back = (Contact) cl.items().nextElement();
        p("committed again: stored tel " + back.getString(Contact.TEL, 0) + " still " + count(cl.items()) + " item; search 'ali' " + count(cl.items("ali")) + " 'zzz' " + count(cl.items("zzz")));
        Contact tmpl = cl.createContact();
        tmpl.addString(Contact.TEL, 0, "0456");
        p("match template " + count(cl.items(tmpl)));
        c.removeValue(Contact.TEL, 0);
        p("after removeValue: count " + c.countValues(Contact.TEL));
        try { c.addToCategory("x"); p("addToCategory: ok"); } catch (PIMException t) { p("addToCategory: PIMException reason " + t.getReason()); }
        cl.removeContact(back);
        p("removed: " + count(cl.items()));
        try { cl.removeContact(back); p("remove twice: ok"); } catch (PIMException t) { p("remove twice: PIMException"); }
        cl.close();
        // read only / write only
        ContactList ro = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY);
        Contact x = ro.createContact();
        x.addString(Contact.UID, 0, "u");
        try { x.commit(); p("commit on read only: ok"); } catch (Throwable t) { p("commit on read only: " + cn(t)); }
        ro.close();
        ContactList wo = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.WRITE_ONLY);
        try { wo.items(); p("items on write only: ok"); } catch (Throwable t) { p("items on write only: " + cn(t)); }
        wo.close();
        // events and to-dos
        EventList el = (EventList) pim.openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE);
        Event e = el.createEvent();
        e.addDate(Event.START, 0, 1000L);
        e.addDate(Event.END, 0, 2000L);
        e.addString(Event.SUMMARY, 0, "meeting");
        RepeatRule rr = new RepeatRule();
        rr.setInt(RepeatRule.FREQUENCY, RepeatRule.DAILY);
        rr.setInt(RepeatRule.COUNT, 3);
        rr.setDate(RepeatRule.END, 5000L);
        e.setRepeat(rr);
        e.commit();
        p("event: " + count(el.items(EventList.STARTING, 0, 1500, false)) + count(el.items(EventList.STARTING, 1500, 3000, false)) + count(el.items(EventList.ENDING, 1500, 3000, false))
            + count(el.items(EventList.OCCURRING, 1200, 1300, false)) + count(el.items(EventList.OCCURRING, 3000, 4000, false)) + " repeat " + (e.getRepeat() == rr) + " fields "
            + rr.getFields().length + " freq " + rr.getInt(RepeatRule.FREQUENCY) + " end " + rr.getDate(RepeatRule.END) + " " + new RepeatRule().equals(rr) + " supported " + el.getSupportedRepeatRuleFields(RepeatRule.DAILY).length);
        try { el.items(EventList.STARTING, 5, 1, false); p("bad range: ok"); } catch (Throwable t) { p("bad range: " + cn(t)); }
        el.close();
        ToDoList tl = (ToDoList) pim.openPIMList(PIM.TODO_LIST, PIM.READ_WRITE);
        ToDo td = tl.createToDo();
        td.addString(ToDo.SUMMARY, 0, "write tests");
        td.addBoolean(ToDo.COMPLETED, 0, false);
        td.addInt(ToDo.PRIORITY, 0, 2);
        td.commit();
        ToDo td2 = tl.createToDo();
        td2.addBoolean(ToDo.COMPLETED, 0, true);
        td2.commit();
        p("todo: all " + count(tl.items()) + " open " + count(tl.items(0, Long.MIN_VALUE, Long.MAX_VALUE)) + " done " + count(tl.items(1, Long.MIN_VALUE, Long.MAX_VALUE)));
        tl.close();
        // serial formats are not supported
        p("serial formats " + pim.supportedSerialFormats(PIM.CONTACT_LIST).length);
        try { pim.fromSerialFormat(new java.io.ByteArrayInputStream(new byte[0]), "UTF-8"); p("fromSerialFormat: ok"); } catch (PIMException t) { p("fromSerialFormat: PIMException reason " + t.getReason()); }
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
