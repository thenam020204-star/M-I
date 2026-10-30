/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import javax.microedition.pim.ToDo;

final class ToDoImpl extends ItemBase implements ToDo {
    ToDoImpl(ListBase list) {
        super(list);
    }
}
