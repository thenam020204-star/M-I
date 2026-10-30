/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class StringItem extends Item {
    private String text;
    private final int mode;
    private Font font;

    public StringItem(String label, String text) {
        this(label, text, PLAIN);
    }

    public StringItem(String label, String text, int appearanceMode) {
        super(label);
        if (appearanceMode != PLAIN && appearanceMode != HYPERLINK && appearanceMode != BUTTON) throw new IllegalArgumentException("appearanceMode");
        this.text = text;
        this.mode = appearanceMode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        changed();
    }

    public int getAppearanceMode() {
        return mode;
    }

    public void setFont(Font font) {
        this.font = font;
        changed();
    }

    public Font getFont() {
        return font != null ? font : Font.getDefaultFont();
    }

    public void setPreferredSize(int width, int height) {
        super.setPreferredSize(width, height);
    }

    int uiKind() {
        return ScreenCodec.I_STRING;
    }

    void encode() {
        ScreenCodec.ws(text);
        ScreenCodec.w(mode);
        ScreenCodec.w(getFont().key());
    }

    private int pad() {
        return mode == PLAIN ? 0 : 4;
    }

    int calcPrefW() {
        Font f = getFont();
        int tw = text == null ? 0 : Math.min(availW(), f.stringWidth(text) + pad());
        return Math.max(labelW(), tw);
    }

    int calcPrefH() {
        Font f = getFont();
        return labelH() + lines(text, f, availW() - pad()) * f.getHeight() + (text == null || text.length() == 0 ? 0 : pad());
    }

    int calcMinW() {
        return Math.min(calcPrefW(), widestWord(text, getFont()) + pad());
    }

    int calcMinH() {
        return labelH() + (text == null || text.length() == 0 ? 0 : getFont().getHeight() + pad());
    }
}
