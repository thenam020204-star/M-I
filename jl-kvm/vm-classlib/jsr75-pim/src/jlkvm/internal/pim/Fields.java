/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import javax.microedition.pim.Contact;
import javax.microedition.pim.Event;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.ToDo;

/** Field tables of the three list types: id, data type, string array size (0 = not an array), maximum values (-1 = unlimited), label. */
final class Fields {
    private Fields() {
    }

    private static final int[][] CONTACT = {
        {Contact.ADDR, PIMItem.STRING_ARRAY, 7, -1}, {Contact.BIRTHDAY, PIMItem.DATE, 0, 1}, {Contact.CLASS, PIMItem.INT, 0, 1},
        {Contact.EMAIL, PIMItem.STRING, 0, -1}, {Contact.FORMATTED_ADDR, PIMItem.STRING, 0, 1}, {Contact.FORMATTED_NAME, PIMItem.STRING, 0, 1},
        {Contact.NAME, PIMItem.STRING_ARRAY, 5, 1}, {Contact.NICKNAME, PIMItem.STRING, 0, 1}, {Contact.NOTE, PIMItem.STRING, 0, 1},
        {Contact.ORG, PIMItem.STRING, 0, 1}, {Contact.PHOTO, PIMItem.BINARY, 0, 1}, {Contact.PHOTO_URL, PIMItem.STRING, 0, 1},
        {Contact.PUBLIC_KEY, PIMItem.BINARY, 0, 1}, {Contact.PUBLIC_KEY_STRING, PIMItem.STRING, 0, 1}, {Contact.REVISION, PIMItem.DATE, 0, 1},
        {Contact.TEL, PIMItem.STRING, 0, -1}, {Contact.TITLE, PIMItem.STRING, 0, 1}, {Contact.UID, PIMItem.STRING, 0, 1}, {Contact.URL, PIMItem.STRING, 0, 1}};
    private static final String[] CONTACT_LABELS = {"Address", "Birthday", "Class", "Email", "Formatted address", "Formatted name", "Name", "Nickname",
        "Note", "Organization", "Photo", "Photo URL", "Public key", "Public key string", "Revision", "Telephone", "Title", "UID", "URL"};
    private static final int[][] EVENT = {
        {Event.ALARM, PIMItem.INT, 0, 1}, {Event.CLASS, PIMItem.INT, 0, 1}, {Event.END, PIMItem.DATE, 0, 1}, {Event.LOCATION, PIMItem.STRING, 0, 1},
        {Event.NOTE, PIMItem.STRING, 0, 1}, {Event.REVISION, PIMItem.DATE, 0, 1}, {Event.START, PIMItem.DATE, 0, 1}, {Event.SUMMARY, PIMItem.STRING, 0, 1},
        {Event.UID, PIMItem.STRING, 0, 1}};
    private static final String[] EVENT_LABELS = {"Alarm", "Class", "End", "Location", "Note", "Revision", "Start", "Summary", "UID"};
    private static final int[][] TODO = {
        {ToDo.CLASS, PIMItem.INT, 0, 1}, {ToDo.COMPLETED, PIMItem.BOOLEAN, 0, 1}, {ToDo.COMPLETION_DATE, PIMItem.DATE, 0, 1}, {ToDo.DUE, PIMItem.DATE, 0, 1},
        {ToDo.NOTE, PIMItem.STRING, 0, 1}, {ToDo.PRIORITY, PIMItem.INT, 0, 1}, {ToDo.REVISION, PIMItem.DATE, 0, 1}, {ToDo.SUMMARY, PIMItem.STRING, 0, 1},
        {ToDo.UID, PIMItem.STRING, 0, 1}};
    private static final String[] TODO_LABELS = {"Class", "Completed", "Completion date", "Due", "Note", "Priority", "Revision", "Summary", "UID"};

    private static int[][] table(int type) {
        return type == PIM.CONTACT_LIST ? CONTACT : type == PIM.EVENT_LIST ? EVENT : TODO;
    }

    private static String[] labels(int type) {
        return type == PIM.CONTACT_LIST ? CONTACT_LABELS : type == PIM.EVENT_LIST ? EVENT_LABELS : TODO_LABELS;
    }

    /** Index of the field in the table of the type, -1 when the list type does not support it. */
    static int row(int type, int field) {
        int[][] t = table(type);
        for (int i = 0; i < t.length; i++) {
            if (t[i][0] == field) {
                return i;
            }
        }
        return -1;
    }

    static int[] ids(int type) {
        int[][] t = table(type);
        int[] r = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            r[i] = t[i][0];
        }
        return r;
    }

    static int dataType(int type, int row) {
        return table(type)[row][1];
    }

    static int arraySize(int type, int row) {
        return table(type)[row][2];
    }

    static int maxValues(int type, int row) {
        return table(type)[row][3];
    }

    static String label(int type, int row) {
        return labels(type)[row];
    }

    /** Attributes of a field (contact: telephone, email, address). */
    static int[] attributes(int type, int field) {
        if (type != PIM.CONTACT_LIST) {
            return new int[0];
        }
        if (field == Contact.TEL) {
            return new int[] {Contact.ATTR_ASST, Contact.ATTR_AUTO, Contact.ATTR_FAX, Contact.ATTR_HOME, Contact.ATTR_MOBILE, Contact.ATTR_OTHER,
                Contact.ATTR_PAGER, Contact.ATTR_PREFERRED, Contact.ATTR_SMS, Contact.ATTR_WORK};
        }
        if (field == Contact.EMAIL || field == Contact.ADDR) {
            return new int[] {Contact.ATTR_HOME, Contact.ATTR_OTHER, Contact.ATTR_PREFERRED, Contact.ATTR_WORK};
        }
        return new int[0];
    }

    static String attributeLabel(int attr) {
        switch (attr) {
            case Contact.ATTR_ASST: return "Assistant";
            case Contact.ATTR_AUTO: return "Auto";
            case Contact.ATTR_FAX: return "Fax";
            case Contact.ATTR_HOME: return "Home";
            case Contact.ATTR_MOBILE: return "Mobile";
            case Contact.ATTR_OTHER: return "Other";
            case Contact.ATTR_PAGER: return "Pager";
            case Contact.ATTR_PREFERRED: return "Preferred";
            case Contact.ATTR_SMS: return "SMS";
            case Contact.ATTR_WORK: return "Work";
            default: return null;
        }
    }

    static int[] arrayElements(int type, int field) {
        if (type == PIM.CONTACT_LIST && field == Contact.NAME) {
            return new int[] {Contact.NAME_FAMILY, Contact.NAME_GIVEN, Contact.NAME_OTHER, Contact.NAME_PREFIX, Contact.NAME_SUFFIX};
        }
        if (type == PIM.CONTACT_LIST && field == Contact.ADDR) {
            return new int[] {Contact.ADDR_COUNTRY, Contact.ADDR_EXTRA, Contact.ADDR_LOCALITY, Contact.ADDR_POBOX, Contact.ADDR_POSTALCODE,
                Contact.ADDR_REGION, Contact.ADDR_STREET};
        }
        return new int[0];
    }
}
