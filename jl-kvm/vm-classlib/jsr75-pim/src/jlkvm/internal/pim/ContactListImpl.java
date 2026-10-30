/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.util.Vector;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

final class ContactListImpl extends ListBase implements ContactList {
    ContactListImpl(String name, int mode, Vector store) {
        super(PIM.CONTACT_LIST, name, mode, store);
    }

    ItemBase create() {
        return new ContactImpl(this);
    }

    public Contact createContact() {
        return new ContactImpl(this);
    }

    public Contact importContact(Contact item) {
        return (Contact) importItem((ItemBase) item);
    }

    public void removeContact(Contact item) throws PIMException {
        remove((ItemBase) item);
    }
}
