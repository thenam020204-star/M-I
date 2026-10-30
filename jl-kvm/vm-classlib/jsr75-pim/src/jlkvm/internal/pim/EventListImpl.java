/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.RepeatRule;

final class EventListImpl extends ListBase implements EventList {
    EventListImpl(String name, int mode, Vector store) {
        super(PIM.EVENT_LIST, name, mode, store);
    }

    ItemBase create() {
        return new EventImpl(this);
    }

    public Event createEvent() {
        return new EventImpl(this);
    }

    public Event importEvent(Event item) {
        return (Event) importItem((ItemBase) item);
    }

    public void removeEvent(Event item) throws PIMException {
        remove((ItemBase) item);
    }

    /** Repeat rules are not expanded: an event is matched by its own START / END only (UNVERIFIED, docs/jsr.md). */
    public Enumeration items(int searchType, long startDate, long endDate, boolean initialEventOnly) throws PIMException {
        checkOpen();
        needRead();
        if (searchType != STARTING && searchType != ENDING && searchType != OCCURRING || startDate > endDate) {
            throw new IllegalArgumentException("Bad search type or range");
        }
        Vector all = snapshot(), out = new Vector();
        for (int i = 0; i < all.size(); i++) {
            ItemBase it = (ItemBase) all.elementAt(i);
            long s = it.number(Event.START), e = it.number(Event.END);
            if (s == Long.MIN_VALUE) {
                continue;
            }
            if (e == Long.MIN_VALUE) {
                e = s;
            }
            boolean hit = searchType == STARTING ? s >= startDate && s <= endDate
                    : searchType == ENDING ? e >= startDate && e <= endDate : s <= endDate && e >= startDate;
            if (hit) {
                out.addElement(it);
            }
        }
        return out.elements();
    }

    public int[] getSupportedRepeatRuleFields(int frequency) {
        if (frequency != RepeatRule.DAILY && frequency != RepeatRule.WEEKLY && frequency != RepeatRule.MONTHLY && frequency != RepeatRule.YEARLY) {
            throw new IllegalArgumentException("Bad frequency: " + frequency);
        }
        return new int[] {RepeatRule.COUNT, RepeatRule.END, RepeatRule.INTERVAL};
    }
}
