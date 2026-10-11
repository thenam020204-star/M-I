import javax.microedition.lcdui.Graphics;
import rpg.GameScreen;
import rpg.Resources;

/**
 * Khung hoi thoai kieu Heroes Lore 2: hop 240x64 sat mep duoi, nen toi ban trong suot, vien vang,
 * 3 dong chu f13c rong 220px, chan dung dung tren canh tren hop.
 * Thay cho anh offscreen 240x37 / 2 dong cua ban goc (MapScript.dialogNewPage/dialogTypeStep/paint).
 * Trang thai trang (dlgStart, dlgPageLen, dlgTyped, dlgPage, dlgShownPage) van nam trong MapScript
 * de phan xu ly phim cua ban goc giu nguyen.
 */
public final class DialogBox {
    public static final int BOX_H = 64;
    public static final int LINES = 3;
    private static final int PAD_X = 10;
    private static final int BG = 0xC8101420;      // ARGB: nen xanh den, ~78% dac
    private static final int BORDER = 0xC9A45C;
    private static final int BORDER_DARK = 0x4A3418;

    /** Doan dang hien tren hop (tach khoi dlgStart vi dlgStart nhay sang trang sau khi go xong). */
    private static int shownStart;
    private static int shownLen;
    private static int[] row;

    public static int textWidth() {
        return GameScreen.width - 2 * PAD_X;
    }

    private static int boxTop() {
        return MapScript.dlgNarration == 0 ? GameScreen.height - BOX_H
                : (GameScreen.height - 33 - BOX_H) >> 1;
    }

    /** Thay MapScript.dialogNewPage(): sang trang moi -> xoa chu dang hien. */
    public static void newPage() {
        shownLen = 0;
    }

    /** Thay MapScript.dialogTypeStep(): go them chu; false khi trang da go xong. */
    public static boolean typeStep() {
        byte[] text = MapScript.dlgText;
        if (text == null) return false;
        if (MapScript.dlgTyped == MapScript.dlgPageLen) {
            MapScript.dlgStart = (short) (MapScript.dlgStart + MapScript.dlgTyped);
            MapScript.dlgPageLen = 0;
            MapScript.dlgPage = (short) (MapScript.dlgPage + 1);
        }
        if (MapScript.dlgPage >= MapScript.dlgShownPage + 1) return false;
        if (MapScript.dlgPageLen == 0) {
            MapScript.dlgPageLen = (short) TextRenderer.bytesForLines(text, MapScript.dlgStart, textWidth(), LINES);
            MapScript.dlgTyped = 0;
        }
        int typed = TextRenderer.typeNextChar(text, MapScript.dlgStart, MapScript.dlgTyped);
        if (typed > MapScript.dlgPageLen) typed = MapScript.dlgPageLen;
        MapScript.dlgTyped = (short) typed;
        shownStart = MapScript.dlgStart;
        shownLen = typed;
        return true;
    }

    private static void fillTranslucent(Graphics g, int x, int y, int w, int h, int argb) {
        if (row == null || row.length < w) row = new int[w];
        for (int i = 0; i < w; i++) row[i] = argb;
        for (int j = 0; j < h; j++) g.drawRGB(row, 0, w, x, y + j, w, 1, true);
    }

    /** Thay doan ve hop thoai trong MapScript.paint(). */
    public static void paint(GameGraphics gg) {
        byte[] text = MapScript.dlgText;
        if (text == null) return;
        int w = GameScreen.width;
        int top = boxTop();
        Graphics g = gg.g;
        gg.setClip(0, 0, w, GameScreen.height);
        if (MapScript.dlgNarration > 0) {
            // loi dan (man den): chu can giua
            gg.setColor(0);
            gg.fillRect(0, GameScreen.height - BOX_H, w, BOX_H);
        } else {
            int ox = GameScreen.centerX - (w >> 1);
            if (MapScript.dlgPortrait >= 0) {
                GameImage face = Resources.portraits[MapScript.dlgPortrait];
                if (face != null) {
                    if (MapScript.dlgPortraitRight != 0) gg.drawImage(face, ox + w - 2, top, Graphics.BOTTOM | Graphics.RIGHT);
                    else gg.drawImage(face, ox + 2, top, Graphics.BOTTOM | Graphics.LEFT);
                }
            }
            fillTranslucent(g, 0, top, w, BOX_H, BG);
            gg.setColor(BORDER_DARK);
            gg.drawRect(0, top, w - 1, BOX_H - 1);
            gg.setColor(BORDER);
            gg.drawRect(1, top + 1, w - 3, BOX_H - 3);
        }
        int lh = TextRenderer.lineHeight + 2;
        int ty = top + ((BOX_H - LINES * lh) >> 1) + 1;
        gg.setColor(0xFFFFFF);
        if (MapScript.dlgNarration == 0) {
            TextRenderer.drawTextCore(gg, PAD_X, ty, textWidth(), text, shownStart, shownLen, 4);
        } else {
            TextRenderer.drawTextCore(gg, w >> 1, ty, textWidth(), text, shownStart, shownLen, 1);
        }
    }
}
