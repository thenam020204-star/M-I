/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public abstract class PIM {
    public static final int CONTACT_LIST = 1;
    public static final int EVENT_LIST = 2;
    public static final int TODO_LIST = 3;
    public static final int READ_ONLY = 1;
    public static final int WRITE_ONLY = 2;
    public static final int READ_WRITE = 3;

    protected PIM() {
    }

    public static PIM getInstance() {
        return jlkvm.internal.pim.PimImpl.instance();
    }

    public abstract PIMList openPIMList(int pimListType, int mode) throws SecurityException, IllegalArgumentException, PIMException;

    public abstract PIMList openPIMList(int pimListType, int mode, String name) throws SecurityException, IllegalArgumentException, NullPointerException, PIMException;

    public abstract String[] listPIMLists(int pimListType) throws SecurityException, IllegalArgumentException;

    public abstract PIMItem[] fromSerialFormat(InputStream is, String enc) throws NullPointerException, PIMException, UnsupportedEncodingException;

    public abstract void toSerialFormat(PIMItem item, OutputStream os, String enc, String dataFormat) throws NullPointerException, IllegalArgumentException, PIMException, UnsupportedEncodingException;

    public abstract String[] supportedSerialFormats(int pimListType) throws IllegalArgumentException;
}
