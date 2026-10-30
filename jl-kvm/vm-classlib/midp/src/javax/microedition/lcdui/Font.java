/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Font metrics and glyphs come from the host (natives metrics0 / charWidth0 / charsWidth0); the key packs face | style | size. */
public final class Font {
    public static final int STYLE_PLAIN = 0, STYLE_BOLD = 1, STYLE_ITALIC = 2, STYLE_UNDERLINED = 4;
    public static final int SIZE_MEDIUM = 0, SIZE_SMALL = 8, SIZE_LARGE = 16;
    public static final int FACE_SYSTEM = 0, FACE_MONOSPACE = 32, FACE_PROPORTIONAL = 64;
    public static final int FONT_STATIC_TEXT = 0, FONT_INPUT_TEXT = 1;
    private static final Font[] cache = new Font[96];
    private final int face, style, size, key, height, baseline;

    private Font(int face, int style, int size) {
        this.face = face;
        this.style = style;
        this.size = size;
        this.key = face | style | size;
        int m = metrics0(key);
        height = m >>> 16;
        baseline = m & 0xFFFF;
    }

    public static Font getDefaultFont() {
        return getFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
    }

    public static Font getFont(int fontSpecifier) {
        if (fontSpecifier != FONT_STATIC_TEXT && fontSpecifier != FONT_INPUT_TEXT) throw new IllegalArgumentException("fontSpecifier");
        return getDefaultFont();
    }

    public static synchronized Font getFont(int face, int style, int size) {
        if ((face != FACE_SYSTEM && face != FACE_MONOSPACE && face != FACE_PROPORTIONAL) || (style & ~7) != 0
                || (size != SIZE_SMALL && size != SIZE_MEDIUM && size != SIZE_LARGE)) throw new IllegalArgumentException("font");
        int k = face | style | size;
        Font f = cache[k];
        if (f == null) {
            f = new Font(face, style, size);
            cache[k] = f;
        }
        return f;
    }

    public int getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public int getFace() {
        return face;
    }

    public boolean isPlain() {
        return style == STYLE_PLAIN;
    }

    public boolean isBold() {
        return (style & STYLE_BOLD) != 0;
    }

    public boolean isItalic() {
        return (style & STYLE_ITALIC) != 0;
    }

    public boolean isUnderlined() {
        return (style & STYLE_UNDERLINED) != 0;
    }

    public int getHeight() {
        return height;
    }

    public int getBaselinePosition() {
        return baseline;
    }

    public int charWidth(char ch) {
        return charWidth0(key, ch);
    }

    public int charsWidth(char[] ch, int offset, int length) {
        if (ch == null) throw new NullPointerException();
        if (offset < 0 || length < 0 || offset > ch.length - length) throw new ArrayIndexOutOfBoundsException();
        return charsWidth0(key, ch, offset, length);
    }

    public int stringWidth(String str) {
        char[] c = str.toCharArray();
        return charsWidth0(key, c, 0, c.length);
    }

    public int substringWidth(String str, int offset, int len) {
        if (offset < 0 || len < 0 || offset > str.length() - len) throw new StringIndexOutOfBoundsException();
        char[] c = str.toCharArray();
        return charsWidth0(key, c, offset, len);
    }

    int key() {
        return key;
    }

    int advanceOf(char[] c, int off, int len) {
        return charsWidth0(key, c, off, len);
    }

    private static native int metrics0(int key);

    private static native int charWidth0(int key, char c);

    private static native int charsWidth0(int key, char[] data, int off, int len);
}
