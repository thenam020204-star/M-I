/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

import java.util.Enumeration;

public interface ToDoList extends PIMList {
    ToDo createToDo();

    ToDo importToDo(ToDo a0) throws NullPointerException;

    Enumeration items(int a0, long a1, long a2) throws IllegalArgumentException, SecurityException, PIMException;

    void removeToDo(ToDo a0) throws NullPointerException, SecurityException, PIMException;
}
