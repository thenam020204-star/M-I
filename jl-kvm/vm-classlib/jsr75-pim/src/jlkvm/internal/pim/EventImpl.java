/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import javax.microedition.pim.Event;
import javax.microedition.pim.RepeatRule;

final class EventImpl extends ItemBase implements Event {
    private RepeatRule repeat;

    EventImpl(ListBase list) {
        super(list);
    }

    public RepeatRule getRepeat() {
        return repeat;
    }

    public void setRepeat(RepeatRule value) {
        repeat = value;
    }
}
