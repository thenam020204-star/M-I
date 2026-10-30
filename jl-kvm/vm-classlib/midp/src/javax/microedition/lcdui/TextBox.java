/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class TextBox extends Screen {

    final TextModel m;

    public TextBox(String title, String text, int maxSize, int constraints) {
        
        m = new TextModel(text, maxSize, constraints);
        setTitle(title);
    }

    public String getString() {
        return m.getString();
    }

    public void setString(String text) {
        m.setString(text);
        touch();
    }

    public int getChars(char[] data) {
        return m.getChars(data);
    }

    public void setChars(char[] data, int offset, int length) {
        m.setChars(data, offset, length);
        touch();
    }

    public void insert(String src, int position) {
        m.insert(src, position);
        touch();
    }

    public void insert(char[] data, int offset, int length, int position) {
        m.insertChars(data, offset, length, position);
        touch();
    }

    public void delete(int offset, int length) {
        m.delete(offset, length);
        touch();
    }

    public int getMaxSize() {
        return m.maxSize;
    }

    public int setMaxSize(int maxSize) {
        int r = m.setMaxSize(maxSize);
        touch();
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
        touch();
    }

    public int getConstraints() {
        return m.constraints;
    }

    public void setInitialInputMode(String characterSubset) {
        m.inputMode = characterSubset;
        touch();
    }

    public void setTitle(String s) {
        super.setTitle(s);
    }

    public void setTicker(Ticker t) {
        super.setTicker(t);
    }

    int uiKind() {
        return ScreenCodec.K_TEXTBOX;
    }

    void encodeBody() {
        ScreenCodec.text(m);
    }

    /** Event thread: the user edited the text. */
    void onUiText(String s) {
        m.hostSet(s);
    }

}
