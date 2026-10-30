/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

import java.util.Enumeration;

public interface PIMList {
    String UNCATEGORIZED = null;

    void addCategory(String a0) throws NullPointerException, SecurityException, PIMException;

    void close() throws PIMException;

    void deleteCategory(String a0, boolean a1) throws NullPointerException, SecurityException, PIMException;

    String getArrayElementLabel(int a0, int a1) throws IllegalArgumentException, UnsupportedFieldException;

    String getAttributeLabel(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    String[] getCategories() throws PIMException;

    int getFieldDataType(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    String getFieldLabel(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    String getName();

    int[] getSupportedArrayElements(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    int[] getSupportedAttributes(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    int[] getSupportedFields();

    boolean isCategory(String a0) throws NullPointerException, PIMException;

    boolean isSupportedArrayElement(int a0, int a1);

    boolean isSupportedAttribute(int a0, int a1);

    boolean isSupportedField(int a0);

    Enumeration items() throws SecurityException, PIMException;

    Enumeration items(String a0) throws NullPointerException, SecurityException, PIMException;

    Enumeration items(PIMItem a0) throws IllegalArgumentException, NullPointerException, SecurityException, PIMException;

    Enumeration itemsByCategory(String a0) throws SecurityException, PIMException;

    int maxCategories();

    int maxValues(int a0) throws IllegalArgumentException;

    void renameCategory(String a0, String a1) throws NullPointerException, SecurityException, PIMException;

    int stringArraySize(int a0) throws IllegalArgumentException;
}
