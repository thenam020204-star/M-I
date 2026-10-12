import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

/** Keys <script> : Robot chạy kịch bản phím/ảnh chụp trên màn hình X (Xvfb).
 *  Lệnh: wait <ms> | key <TÊN> [giữ ms] | keys <TÊN> <n> <khoảng ms> | shot <file.png> | move <x> <y> | click | say <text> */
public class Keys {
    static Robot r;
    static int code(String n) {
        switch (n) {
            case "UP": return KeyEvent.VK_UP; case "DOWN": return KeyEvent.VK_DOWN; case "LEFT": return KeyEvent.VK_LEFT; case "RIGHT": return KeyEvent.VK_RIGHT;
            case "OK": return KeyEvent.VK_ENTER; case "SL": return KeyEvent.VK_F1; case "SR": return KeyEvent.VK_F2;
            case "STAR": return KeyEvent.VK_MULTIPLY; case "POUND": return KeyEvent.VK_DIVIDE;
        }
        if (n.length() == 1 && Character.isDigit(n.charAt(0))) return KeyEvent.VK_NUMPAD0 + (n.charAt(0) - '0');
        throw new RuntimeException("key? " + n);
    }
    public static void main(String[] a) throws Exception {
        r = new Robot();
        r.setAutoDelay(10);
        BufferedReader br = new BufferedReader(new FileReader(a[0]));
        String ln;
        while ((ln = br.readLine()) != null) {
            ln = ln.trim();
            if (ln.isEmpty() || ln.startsWith("#")) continue;
            String[] t = ln.split("\\s+");
            switch (t[0]) {
                case "wait": Thread.sleep(Long.parseLong(t[1])); break;
                case "key": { int k = code(t[1]); r.keyPress(k); Thread.sleep(t.length > 2 ? Long.parseLong(t[2]) : 80); r.keyRelease(k); Thread.sleep(120); break; }
                case "keys": { int k = code(t[1]); int n = Integer.parseInt(t[2]); long gap = Long.parseLong(t[3]); for (int i = 0; i < n; i++) { r.keyPress(k); Thread.sleep(60); r.keyRelease(k); Thread.sleep(gap); } break; }
                case "move": r.mouseMove(Integer.parseInt(t[1]), Integer.parseInt(t[2])); break;
                case "click": r.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK); r.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK); break;
                case "shot": {
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    BufferedImage im = r.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
                    ImageIO.write(im, "png", new File(t[1])); break; }
                case "say": System.out.println("KEYS " + ln.substring(4)); break;
                default: throw new RuntimeException("cmd? " + t[0]);
            }
        }
    }
}
