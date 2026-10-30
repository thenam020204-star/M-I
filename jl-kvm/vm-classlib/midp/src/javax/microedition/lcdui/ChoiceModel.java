/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Package-private: the element list and selection rules shared by ChoiceGroup and List (MIDP 2.0 Choice javadoc). */
final class ChoiceModel {
    final int type;
    int fit;
    int n;
    String[] str = new String[4];
    Image[] img = new Image[4];
    Font[] font = new Font[4];
    boolean[] sel = new boolean[4];

    ChoiceModel(int type) {
        this.type = type;
    }

    boolean single() {
        return type != Choice.MULTIPLE;
    }

    private void grow() {
        if (n < str.length) return;
        int c = n * 2;
        String[] s2 = new String[c];
        Image[] i2 = new Image[c];
        Font[] f2 = new Font[c];
        boolean[] b2 = new boolean[c];
        System.arraycopy(str, 0, s2, 0, n);
        System.arraycopy(img, 0, i2, 0, n);
        System.arraycopy(font, 0, f2, 0, n);
        System.arraycopy(sel, 0, b2, 0, n);
        str = s2;
        img = i2;
        font = f2;
        sel = b2;
    }

    private static Image snapshot(Image i) {
        return i == null || !i.isMutable() ? i : Image.createImage(i);
    }

    private void range(int i) {
        if (i < 0 || i >= n) throw new IndexOutOfBoundsException("element " + i);
    }

    synchronized int append(String s, Image im) {
        insert(n, s, im);
        return n - 1;
    }

    synchronized void insert(int at, String s, Image im) {
        if (at < 0 || at > n) throw new IndexOutOfBoundsException("element " + at);
        if (s == null) throw new NullPointerException();
        im = snapshot(im);
        grow();
        System.arraycopy(str, at, str, at + 1, n - at);
        System.arraycopy(img, at, img, at + 1, n - at);
        System.arraycopy(font, at, font, at + 1, n - at);
        System.arraycopy(sel, at, sel, at + 1, n - at);
        str[at] = s;
        img[at] = im;
        font[at] = null;
        sel[at] = false;
        n++;
        if (single() && n == 1) sel[0] = true;
    }

    synchronized void delete(int at) {
        range(at);
        boolean was = sel[at];
        System.arraycopy(str, at + 1, str, at, n - at - 1);
        System.arraycopy(img, at + 1, img, at, n - at - 1);
        System.arraycopy(font, at + 1, font, at, n - at - 1);
        System.arraycopy(sel, at + 1, sel, at, n - at - 1);
        n--;
        str[n] = null;
        img[n] = null;
        font[n] = null;
        sel[n] = false;
        if (single() && was && n > 0) sel[at < n ? at : n - 1] = true;
    }

    synchronized void deleteAll() {
        for (int i = 0; i < n; i++) {
            str[i] = null;
            img[i] = null;
            font[i] = null;
            sel[i] = false;
        }
        n = 0;
    }

    synchronized void set(int at, String s, Image im) {
        range(at);
        if (s == null) throw new NullPointerException();
        str[at] = s;
        img[at] = snapshot(im);
    }

    synchronized String getString(int i) {
        range(i);
        return str[i];
    }

    synchronized Image getImage(int i) {
        range(i);
        return img[i];
    }

    synchronized boolean isSelected(int i) {
        range(i);
        return sel[i];
    }

    synchronized int getSelectedIndex() {
        if (!single()) return -1;
        for (int i = 0; i < n; i++) if (sel[i]) return i;
        return -1;
    }

    synchronized int getSelectedFlags(boolean[] out) {
        if (out == null) throw new NullPointerException();
        if (out.length < n) throw new IllegalArgumentException("array too short");
        int c = 0;
        for (int i = 0; i < out.length; i++) {
            boolean b = i < n && sel[i];
            out[i] = b;
            if (b) c++;
        }
        return c;
    }

    synchronized void setSelectedIndex(int i, boolean s) {
        range(i);
        if (single()) {
            if (!s) return;
            for (int k = 0; k < n; k++) sel[k] = k == i;
        } else {
            sel[i] = s;
        }
    }

    synchronized void setSelectedFlags(boolean[] in) {
        if (in == null) throw new NullPointerException();
        if (in.length < n) throw new IllegalArgumentException("array too short");
        if (single()) {
            int first = -1;
            for (int i = 0; i < n && first < 0; i++) if (in[i]) first = i;
            if (first < 0 && n > 0) first = 0;
            for (int i = 0; i < n; i++) sel[i] = i == first;
        } else {
            for (int i = 0; i < n; i++) sel[i] = in[i];
        }
    }

    void setFitPolicy(int p) {
        if (p != Choice.TEXT_WRAP_DEFAULT && p != Choice.TEXT_WRAP_ON && p != Choice.TEXT_WRAP_OFF) throw new IllegalArgumentException("fitPolicy");
        fit = p;
    }

    synchronized void setFont(int i, Font f) {
        range(i);
        font[i] = f;
    }

    synchronized Font getFont(int i) {
        range(i);
        return font[i] != null ? font[i] : Font.getDefaultFont();
    }

    /** Host action: the user picked element i (state of a multiple choice toggles to {@code s}). */
    synchronized void hostSelect(int i, boolean s) {
        if (i < 0 || i >= n) return;
        if (single()) setSelectedIndex(i, true);
        else sel[i] = s;
    }

    static void checkType(int t, boolean allowImplicit, boolean allowPopup) {
        if (t == Choice.EXCLUSIVE || t == Choice.MULTIPLE || (t == Choice.IMPLICIT && allowImplicit) || (t == Choice.POPUP && allowPopup)) return;
        throw new IllegalArgumentException("choiceType");
    }

    void fill(String[] strings, Image[] images) {
        if (strings == null) throw new NullPointerException();
        for (int i = 0; i < strings.length; i++) if (strings[i] == null) throw new NullPointerException();
        if (images != null && images.length != strings.length) throw new IllegalArgumentException("images");
        for (int i = 0; i < strings.length; i++) append(strings[i], images == null ? null : images[i]);
    }

    int prefHeight(int availW) {
        Font f = Font.getDefaultFont();
        int rows = n < 1 ? 1 : n;
        return rows * (f.getHeight() + 2);
    }
}
