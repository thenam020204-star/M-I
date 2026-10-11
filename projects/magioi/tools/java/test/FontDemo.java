import java.io.*;
import javax.microedition.lcdui.Graphics;

/** Render thu moi font (18/15/13 co chan, 15s/13s khong chan, 15c/13c chan chon loc) ra PNG: java FontDemo <thu_muc_font> <out.png> */
public class FontDemo {
    public static void main(String[] a) throws Exception {
        String dir = a[0];
        int W = 300, H = 560, S = 3;
        Graphics g = new Graphics(W, H);
        g.setColor(0x20304A); g.fillRect(0, 0, W, H);
        String text = "Vương Quốc Sủng Vật - Cầu Vồng. Ngày xưa, ở một vương quốc xa xôi, "
                + "những chú thú cưng sống vui vẻ bên nhau.";
        int y = 4;
        String[] sizes = {"18", "15", "13", "15s", "13s", "15c", "13c"};
        for (int k = 0; k < sizes.length; k++) {
            BitmapFont f = new BitmapFont(new DataInputStream(new FileInputStream(dir + "/f" + sizes[k] + ".bin")));
            g.setColor(0xFFD700);
            f.drawString(g, sizes[k] + "px  ẤẦẨẪẬ ỨỪỬỮỰ Đđ ơư", 4, y, 0);
            y += f.getHeight() + 2;
            g.setColor(0xFFFFFF);
            y += f.drawWrapped(g, text, 4, y, W - 8, 1, Graphics.LEFT) + 4;
            f.drawOutlined(g, "Viền chữ: Hộp quà", W / 2, y, Graphics.TOP | Graphics.HCENTER, 0xFFFFFF, 0x000000);
            y += f.getHeight() + 8;
        }
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(W * S, H * S, java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int j = 0; j < H * S; j++) for (int i = 0; i < W * S; i++) img.setRGB(i, j, g.buf[(j / S) * W + i / S]);
        javax.imageio.ImageIO.write(img, "png", new File(a[1]));
        System.out.println("ok");
    }
}
