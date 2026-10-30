/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;

/**
 * The PIM of the runtime: one default list per type ("Contacts", "Calendar", "ToDo"), empty at start, kept in memory for the run only.
 * Serial formats (vCard / vCalendar) are not supported. The profile permission pim.permission "deny" is a SecurityException.
 */
public final class PimImpl extends PIM {
    private static PimImpl instance;
    private static final String[] NAMES = {"Contacts", "Calendar", "ToDo"};
    private final Vector[] stores = {new Vector(), new Vector(), new Vector()};

    private PimImpl() {
    }

    public static synchronized PIM instance() {
        if (instance == null) {
            instance = new PimImpl();
        }
        return instance;
    }

    private static void checkType(int type) {
        if (type != CONTACT_LIST && type != EVENT_LIST && type != TODO_LIST) {
            throw new IllegalArgumentException("Bad list type: " + type);
        }
    }

    private static void perm() {
        if (Pim.perm0() == 0) {
            throw new SecurityException("PIM access denied by the device profile");
        }
    }

    public PIMList openPIMList(int pimListType, int mode) throws PIMException {
        checkType(pimListType);
        return openPIMList(pimListType, mode, NAMES[pimListType - 1]);
    }

    public PIMList openPIMList(int pimListType, int mode, String name) throws PIMException {
        checkType(pimListType);
        if (mode != READ_ONLY && mode != WRITE_ONLY && mode != READ_WRITE) {
            throw new IllegalArgumentException("Bad mode: " + mode);
        }
        if (name == null) {
            throw new NullPointerException("name");
        }
        perm();
        if (!name.equals(NAMES[pimListType - 1])) {
            throw new PIMException("No such list: " + name, PIMException.LIST_NOT_ACCESSIBLE);
        }
        Vector s = stores[pimListType - 1];
        if (pimListType == CONTACT_LIST) {
            return new ContactListImpl(name, mode, s);
        }
        return pimListType == EVENT_LIST ? (PIMList) new EventListImpl(name, mode, s) : new ToDoListImpl(name, mode, s);
    }

    public String[] listPIMLists(int pimListType) {
        checkType(pimListType);
        perm();
        return new String[] {NAMES[pimListType - 1]};
    }

    public PIMItem[] fromSerialFormat(InputStream is, String enc) throws PIMException, UnsupportedEncodingException {
        if (is == null) {
            throw new NullPointerException("is");
        }
        throw new PIMException("Serial formats are not supported", PIMException.FEATURE_NOT_SUPPORTED);
    }

    public void toSerialFormat(PIMItem item, OutputStream os, String enc, String dataFormat) throws PIMException, UnsupportedEncodingException {
        if (item == null || os == null || enc == null || dataFormat == null) {
            throw new NullPointerException();
        }
        throw new PIMException("Serial formats are not supported", PIMException.FEATURE_NOT_SUPPORTED);
    }

    public String[] supportedSerialFormats(int pimListType) {
        checkType(pimListType);
        return new String[0];
    }
}
