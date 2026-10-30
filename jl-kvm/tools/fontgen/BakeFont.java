import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * One-off developer tool (NOT part of the build): bakes the bitmap fonts of host-common (resource jlkvm/fonts.bin) from DejaVu Sans
 * and DejaVu Sans Mono (Bitstream Vera licence: free to embed, see host-common/src/main/resources/jlkvm/FONTS-LICENSE.txt).
 * Runtime code never uses AWT, so the pixels are identical on every host (desktop, Android).
 *
 *   java tools/fontgen/BakeFont.java /usr/share/fonts/truetype/dejavu host-common/src/main/resources/jlkvm/fonts.bin
 *
 * File format: "JLF1", u8 nfonts; per font: u8 face (0 proportional, 1 monospace), u8 size (0 small, 1 medium, 2 large), u8 height,
 * u8 baseline, u16 nglyphs; per glyph: u16 codepoint (ascending), u8 advance, height rows of ceil(advance/8) bytes (bit 7 = leftmost).
 */
public class BakeFont {
    static final int[] EM = {10, 13, 17};

    public static void main(String[] a) throws Exception {
        File dir = new File(a[0]);
        List<int[]> ranges = new ArrayList<>();
        int[][] rg = {{0x20, 0x7E}, {0xA0, 0x17F}, {0x1A0, 0x1A1}, {0x1AF, 0x1B0}, {0x1EA0, 0x1EF9}, {0x2013, 0x2014}, {0x2018, 0x201E}, {0x2022, 0x2022}, {0x2026, 0x2026}, {0x20AB, 0x20AC}};
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bo);
        out.writeBytes("JLF1");
        out.writeByte(6);
        String[] files = {"DejaVuSans.ttf", "DejaVuSansMono.ttf"};
        for (int face = 0; face < 2; face++) {
            Font base = Font.createFont(Font.TRUETYPE_FONT, new File(dir, files[face]));
            for (int sz = 0; sz < 3; sz++) {
                Font f = base.deriveFont(Font.PLAIN, (float) EM[sz]);
                BufferedImage probe = new BufferedImage(8, 8, BufferedImage.TYPE_BYTE_BINARY);
                Graphics2D pg = probe.createGraphics();
                pg.setFont(f);
                FontMetrics fm = pg.getFontMetrics();
                int height = fm.getAscent() + fm.getDescent(), baseline = fm.getAscent();
                List<Integer> cps = new ArrayList<>();
                for (int[] r : rg) for (int c = r[0]; c <= r[1]; c++) if (f.canDisplay(c)) cps.add(c);
                out.writeByte(face); out.writeByte(sz); out.writeByte(height); out.writeByte(baseline); out.writeShort(cps.size());
                for (int c : cps) {
                    int adv = Math.max(1, fm.charWidth(c));
                    BufferedImage im = new BufferedImage(adv + 4, height + 4, BufferedImage.TYPE_BYTE_BINARY);
                    Graphics2D g = im.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
                    g.setColor(java.awt.Color.WHITE);
                    g.fillRect(0, 0, adv + 4, height + 4);
                    g.setColor(java.awt.Color.BLACK);
                    g.setFont(f);
                    g.drawString(new String(Character.toChars(c)), 0, baseline);
                    out.writeShort(c); out.writeByte(adv);
                    int rowBytes = (adv + 7) / 8;
                    for (int y = 0; y < height; y++) {
                        for (int bx = 0; bx < rowBytes; bx++) {
                            int v = 0;
                            for (int bit = 0; bit < 8; bit++) {
                                int x = bx * 8 + bit;
                                if (x < adv && (im.getRGB(x, y) & 0xFFFFFF) == 0) v |= 0x80 >> bit;
                            }
                            out.writeByte(v);
                        }
                    }
                }
            }
        }
        Files.write(new File(a[1]).toPath(), bo.toByteArray());
        System.out.println("wrote " + a[1] + " (" + bo.size() + " bytes)");
    }
}
