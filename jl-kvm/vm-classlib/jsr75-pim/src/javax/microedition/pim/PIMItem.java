/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

public interface PIMItem {
    int ATTR_NONE = 0;
    int BINARY = 0;
    int BOOLEAN = 1;
    int DATE = 2;
    int EXTENDED_ATTRIBUTE_MIN_VALUE = 16777216;
    int EXTENDED_FIELD_MIN_VALUE = 16777216;
    int INT = 3;
    int STRING = 4;
    int STRING_ARRAY = 5;

    void addBinary(int a0, int a1, byte[] a2, int a3, int a4) throws IllegalArgumentException, NullPointerException, FieldFullException, UnsupportedFieldException;

    void addBoolean(int a0, int a1, boolean a2) throws IllegalArgumentException, FieldFullException, UnsupportedFieldException;

    void addDate(int a0, int a1, long a2) throws IllegalArgumentException, FieldFullException, UnsupportedFieldException;

    void addInt(int a0, int a1, int a2) throws IllegalArgumentException, FieldFullException, UnsupportedFieldException;

    void addString(int a0, int a1, String a2) throws IllegalArgumentException, NullPointerException, FieldFullException, UnsupportedFieldException;

    void addStringArray(int a0, int a1, String[] a2) throws IllegalArgumentException, NullPointerException, FieldFullException, UnsupportedFieldException;

    void addToCategory(String a0) throws NullPointerException, PIMException;

    void commit() throws SecurityException, PIMException;

    int countValues(int a0) throws IllegalArgumentException, UnsupportedFieldException;

    int getAttributes(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    byte[] getBinary(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    boolean getBoolean(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    String[] getCategories();

    long getDate(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    int[] getFields();

    int getInt(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    PIMList getPIMList();

    String getString(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    String[] getStringArray(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    boolean isModified();

    int maxCategories();

    void removeFromCategory(String a0) throws NullPointerException;

    void removeValue(int a0, int a1) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    void setBinary(int a0, int a1, int a2, byte[] a3, int a4, int a5) throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException, UnsupportedFieldException;

    void setBoolean(int a0, int a1, int a2, boolean a3) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    void setDate(int a0, int a1, int a2, long a3) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    void setInt(int a0, int a1, int a2, int a3) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedFieldException;

    void setString(int a0, int a1, int a2, String a3) throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException, UnsupportedFieldException;

    void setStringArray(int a0, int a1, int a2, String[] a3) throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException, UnsupportedFieldException;
}
