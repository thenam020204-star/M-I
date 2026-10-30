/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class TextField extends Item {
    public static final int ANY = 0, EMAILADDR = 1, NUMERIC = 2, PHONENUMBER = 3, URL = 4, DECIMAL = 5;
    public static final int PASSWORD = 0x10000, UNEDITABLE = 0x20000, SENSITIVE = 0x40000, NON_PREDICTIVE = 0x80000,
            INITIAL_CAPS_WORD = 0x100000, INITIAL_CAPS_SENTENCE = 0x200000;
    public static final int CONSTRAINT_MASK = 0xFFFF;
    final TextModel m;

    public TextField(String label, String text, int maxSize, int constraints) {
        super(label);
        m = new TextModel(text, maxSize, constraints);

    }

    public String getString() {
        return m.getString();
    }

    public void setString(String text) {
        m.setString(text);
        changed();
    }

    public int getChars(char[] data) {
        return m.getChars(data);
    }

    public void setChars(char[] data, int offset, int length) {
        m.setChars(data, offset, length);
        changed();
    }

    public void insert(String src, int position) {
        m.insert(src, position);
        changed();
    }

    public void insert(char[] data, int offset, int length, int position) {
        m.insertChars(data, offset, length, position);
        changed();
    }

    public void delete(int offset, int length) {
        m.delete(offset, length);
        changed();
    }

    public int getMaxSize() {
        return m.maxSize;
    }

    public int setMaxSize(int maxSize) {
        int r = m.setMaxSize(maxSize);
        changed();
        return r;
    }

    public int size() {
        return m.len;
    }

    public int getCaretPosition() {
        return m.caret;
    }

    public void setConstraints(int constraints) {
        m.setConstraints(constraints);
        changed();
    }

    public int getConstraints() {
        return m.constraints;
    }

    public void setInitialInputMode(String characterSubset) {
        m.inputMode = characterSubset;
        changed();
    }


    int uiKind() {
        return ScreenCodec.I_TEXT;
    }

    void encode() {
        ScreenCodec.text(m);
    }

    int calcPrefW() {
        return availW();
    }

    int calcPrefH() {
        return labelH() + Font.getDefaultFont().getHeight() + 4;
    }

    int calcMinW() {
        return Math.min(availW(), 6 * Font.getDefaultFont().charWidth('W'));
    }

    int calcMinH() {
        return calcPrefH();
    }
}
