/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import javax.microedition.pim.Contact;
import javax.microedition.pim.UnsupportedFieldException;

final class ContactImpl extends ItemBase implements Contact {
    ContactImpl(ListBase list) {
        super(list);
    }

    public int getPreferredIndex(int field) {
        int n = countValues(field);
        for (int i = 0; i < n; i++) {
            if ((getAttributes(field, i) & ATTR_PREFERRED) != 0) {
                return i;
            }
        }
        return -1;
    }
}
