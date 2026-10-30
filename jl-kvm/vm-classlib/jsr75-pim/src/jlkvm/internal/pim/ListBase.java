/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.UnsupportedFieldException;

/** An open view on the in-memory items of one list type (the items live in PimImpl for the whole run; nothing is persisted). */
abstract class ListBase implements PIMList {
    final int type;
    private final String name;
    final int mode;
    private final Vector store;
    private boolean closed;

    ListBase(int type, String name, int mode, Vector store) {
        this.type = type;
        this.name = name;
        this.mode = mode;
        this.store = store;
    }

    abstract ItemBase create();

    final void checkOpen() throws PIMException {
        if (closed) {
            throw new PIMException("The list is closed", PIMException.LIST_CLOSED);
        }
    }

    final void needRead() {
        if (mode == PIM.WRITE_ONLY) {
            throw new SecurityException("The list was opened write only");
        }
    }

    final void needWrite() {
        if (mode == PIM.READ_ONLY) {
            throw new SecurityException("The list was opened read only");
        }
    }

    /**
     * Item.commit(): stores a copy of the item (the item object is the application's working copy). The store holds one Object[] {ItemBase} per
     * stored item; a working copy remembers its entry ({@code entry}), so committing again replaces the stored copy and remove() finds it.
     */
    final void commit(ItemBase item) throws PIMException {
        checkOpen();
        needWrite();
        synchronized (store) {
            ItemBase stored = create();
            item.copyTo(stored);
            if (item.entry != null && store.contains(item.entry)) {
                item.entry[0] = stored;
                return;
            }
            Object[] e = new Object[] {stored};
            store.addElement(e);
            item.entry = e;
        }
    }

    final ItemBase importItem(ItemBase src) {
        if (src == null) {
            throw new NullPointerException("item");
        }
        ItemBase n = create();
        src.copyTo(n);
        return n;
    }

    final void remove(ItemBase item) throws PIMException {
        if (item == null) {
            throw new NullPointerException("item");
        }
        checkOpen();
        needWrite();
        synchronized (store) {
            if (item.entry != null && store.removeElement(item.entry)) {
                item.entry = null;
                return;
            }
        }
        throw new PIMException("The item is not in the list", PIMException.GENERAL_ERROR);
    }

    /** Snapshot of the stored items, each as a fresh working copy bound to this list. */
    final Vector snapshot() {
        Vector out = new Vector();
        synchronized (store) {
            for (int i = 0; i < store.size(); i++) {
                ItemBase c = create();
                ((ItemBase) ((Object[]) store.elementAt(i))[0]).copyTo(c);
                c.entry = (Object[]) store.elementAt(i);
                c.clean();
                out.addElement(c);
            }
        }
        return out;
    }

    public String getName() {
        return name;
    }

    public void close() throws PIMException {
        checkOpen();
        closed = true;
    }

    public Enumeration items() throws PIMException {
        checkOpen();
        needRead();
        return snapshot().elements();
    }

    public Enumeration items(PIMItem matchingItem) throws PIMException {
        checkOpen();
        if (matchingItem == null) {
            throw new NullPointerException("matchingItem");
        }
        needRead();
        if (!(matchingItem instanceof ItemBase) || ((ItemBase) matchingItem).list.type != type) {
            throw new IllegalArgumentException("The item is not of this list type");
        }
        Vector all = snapshot(), out = new Vector();
        for (int i = 0; i < all.size(); i++) {
            if (((ItemBase) all.elementAt(i)).matches((ItemBase) matchingItem)) {
                out.addElement(all.elementAt(i));
            }
        }
        return out.elements();
    }

    public Enumeration items(String matchingValue) throws PIMException {
        checkOpen();
        if (matchingValue == null) {
            throw new NullPointerException("matchingValue");
        }
        needRead();
        Vector all = snapshot(), out = new Vector();
        for (int i = 0; i < all.size(); i++) {
            if (((ItemBase) all.elementAt(i)).anyStringContains(matchingValue)) {
                out.addElement(all.elementAt(i));
            }
        }
        return out.elements();
    }

    public Enumeration itemsByCategory(String category) throws PIMException {
        checkOpen();
        needRead();
        return category == null ? snapshot().elements() : new Vector().elements();
    }

    public String[] getCategories() throws PIMException {
        checkOpen();
        return new String[0];
    }

    public boolean isCategory(String category) throws PIMException {
        checkOpen();
        if (category == null) {
            throw new NullPointerException("category");
        }
        return false;
    }

    public void addCategory(String category) throws PIMException {
        checkOpen();
        needWrite();
        if (category == null) {
            throw new NullPointerException("category");
        }
        throw new PIMException("Categories are not supported", PIMException.MAX_CATEGORIES_EXCEEDED);
    }

    public void deleteCategory(String category, boolean deleteUnassignedItems) throws PIMException {
        checkOpen();
        needWrite();
        if (category == null) {
            throw new NullPointerException("category");
        }
    }

    public void renameCategory(String currentCategory, String newCategory) throws PIMException {
        checkOpen();
        needWrite();
        if (currentCategory == null || newCategory == null) {
            throw new NullPointerException("category");
        }
        throw new PIMException("No such category", PIMException.GENERAL_ERROR);
    }

    public int maxCategories() {
        return 0;
    }

    public boolean isSupportedField(int field) {
        return Fields.row(type, field) >= 0;
    }

    public int[] getSupportedFields() {
        return Fields.ids(type);
    }

    private int row(int field) {
        int r = Fields.row(type, field);
        if (r < 0) {
            throw new UnsupportedFieldException("Field not supported: " + field, field);
        }
        return r;
    }

    public boolean isSupportedAttribute(int field, int attribute) {
        int[] a = Fields.attributes(type, field);
        for (int i = 0; i < a.length; i++) {
            if (a[i] == attribute) {
                return true;
            }
        }
        return false;
    }

    public int[] getSupportedAttributes(int field) {
        row(field);
        return Fields.attributes(type, field);
    }

    public boolean isSupportedArrayElement(int field, int arrayElement) {
        int[] a = Fields.arrayElements(type, field);
        for (int i = 0; i < a.length; i++) {
            if (a[i] == arrayElement) {
                return true;
            }
        }
        return false;
    }

    public int[] getSupportedArrayElements(int field) {
        if (Fields.dataType(type, row(field)) != PIMItem.STRING_ARRAY) {
            throw new IllegalArgumentException("Not a string array field: " + field);
        }
        return Fields.arrayElements(type, field);
    }

    public int getFieldDataType(int field) {
        return Fields.dataType(type, row(field));
    }

    public String getFieldLabel(int field) {
        return Fields.label(type, row(field));
    }

    public String getAttributeLabel(int attribute) {
        String l = Fields.attributeLabel(attribute);
        if (l == null || type != PIM.CONTACT_LIST) {
            throw new UnsupportedFieldException("Attribute not supported: " + attribute, -1);
        }
        return l;
    }

    public String getArrayElementLabel(int stringArrayField, int arrayElement) {
        if (!isSupportedArrayElement(stringArrayField, arrayElement)) {
            throw new IllegalArgumentException("Not an element of field " + stringArrayField);
        }
        return "Element " + arrayElement;
    }

    public int maxValues(int field) {
        int r = Fields.row(type, field);
        return r < 0 ? 0 : Fields.maxValues(type, r);
    }

    public int stringArraySize(int field) {
        int r = Fields.row(type, field);
        if (r < 0 || Fields.dataType(type, r) != PIMItem.STRING_ARRAY) {
            throw new IllegalArgumentException("Not a string array field: " + field);
        }
        return Fields.arraySize(type, r);
    }
}
