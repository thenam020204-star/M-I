/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class ImageItem extends Item {
    public static final int LAYOUT_DEFAULT = 0, LAYOUT_LEFT = 1, LAYOUT_RIGHT = 2, LAYOUT_CENTER = 3, LAYOUT_NEWLINE_BEFORE = 256, LAYOUT_NEWLINE_AFTER = 512;
    private Image image;
    private String alt;
    private final int mode;

    public ImageItem(String label, Image img, int layout, String altText) {
        this(label, img, layout, altText, PLAIN);
    }

    public ImageItem(String label, Image img, int layout, String altText, int appearanceMode) {
        super(label);
        if (appearanceMode != PLAIN && appearanceMode != HYPERLINK && appearanceMode != BUTTON) throw new IllegalArgumentException("appearanceMode");
        checkLayout(layout);
        this.layout = layout;
        this.image = snapshot(img);
        this.alt = altText;
        this.mode = appearanceMode;
    }

    private static Image snapshot(Image i) {
        return i == null || !i.isMutable() ? i : Image.createImage(i);
    }

    private static void checkLayout(int l) {
        if ((l & ~(LAYOUT_2 | LAYOUT_VEXPAND | LAYOUT_VSHRINK | LAYOUT_EXPAND | LAYOUT_SHRINK | LAYOUT_NEWLINE_AFTER | LAYOUT_NEWLINE_BEFORE
                | LAYOUT_VCENTER | LAYOUT_CENTER)) != 0) throw new IllegalArgumentException("layout");
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image img) {
        image = snapshot(img);
        changed();
    }

    public String getAltText() {
        return alt;
    }

    public void setAltText(String text) {
        alt = text;
        changed();
    }

    public int getLayout() {
        return super.getLayout();
    }

    public void setLayout(int layout) {
        checkLayout(layout);
        super.setLayout(layout);
    }

    public int getAppearanceMode() {
        return mode;
    }

    int uiKind() {
        return ScreenCodec.I_IMAGE;
    }

    void encode() {
        ScreenCodec.w(image == null ? 0 : image.peer);
        ScreenCodec.ws(alt);
        ScreenCodec.w(mode);
    }

    int calcPrefW() {
        return Math.max(labelW(), image == null ? 0 : Math.min(availW(), image.getWidth()));
    }

    int calcPrefH() {
        return labelH() + (image == null ? 0 : image.getHeight());
    }

    int calcMinW() {
        return calcPrefW();
    }

    int calcMinH() {
        return calcPrefH();
    }
}
