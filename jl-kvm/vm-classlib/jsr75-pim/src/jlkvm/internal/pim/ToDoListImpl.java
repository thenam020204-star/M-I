/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.pim;

import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.ToDo;
import javax.microedition.pim.ToDoList;

final class ToDoListImpl extends ListBase implements ToDoList {
    ToDoListImpl(String name, int mode, Vector store) {
        super(PIM.TODO_LIST, name, mode, store);
    }

    ItemBase create() {
        return new ToDoImpl(this);
    }

    public ToDo createToDo() {
        return new ToDoImpl(this);
    }

    public ToDo importToDo(ToDo item) {
        return (ToDo) importItem((ItemBase) item);
    }

    public void removeToDo(ToDo item) throws PIMException {
        remove((ItemBase) item);
    }

    /** completeInd: ToDo.COMPLETED field present with true (1), false (0) or ignored (-1); due date in [startDue, endDue] when given (Long.MIN_VALUE / MAX_VALUE). */
    public Enumeration items(int completionStatus, long startRange, long endRange) throws PIMException {
        checkOpen();
        needRead();
        if (completionStatus < -1 || completionStatus > 1 || startRange > endRange) {
            throw new IllegalArgumentException("Bad range or completion status");
        }
        Vector all = snapshot(), out = new Vector();
        for (int i = 0; i < all.size(); i++) {
            ItemBase it = (ItemBase) all.elementAt(i);
            boolean done = it.countValues(ToDo.COMPLETED) > 0 && it.getBoolean(ToDo.COMPLETED, 0);
            if (completionStatus == 0 && done || completionStatus == 1 && !done) {
                continue;
            }
            long due = it.number(ToDo.DUE);
            if (due != Long.MIN_VALUE && (due < startRange || due > endRange)) {
                continue;
            }
            out.addElement(it);
        }
        return out.elements();
    }
}
