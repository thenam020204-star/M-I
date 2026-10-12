import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * MeRun <jar> <script> <outdir>  — harness MicroEmulator trong cùng JVM (Xvfb): chạy game, tiêm phím trực tiếp vào canvas
 * (keyPressed/keyReleased bằng reflection, tất định) và đọc trạng thái game.
 * Lệnh kịch bản:
 *   wait <ms> | key <mã game: -5 OK, -1 lên, -2 xuống, -3 trái, -4 phải, -6 SL, -7 SR, 48..57 số, 42 *, 35 #> [giữ ms]
 *   keys <mã> <n> <khoảng ms> | show (showNotify) | shot <tên.png> | say <text>
 *   until <Lớp> <trườngTĩnh> <giá trị int> <timeout ms>     (chờ field tĩnh == giá trị)
 *   probe <Lớp> <trườngTĩnh> [<trường>...]                   (in giá trị; theo tên + kiểu đoán)
 *   spam <phím> <ms> <timeout ms> <Lớp> <trườngTĩnh> [<trường>...] -- <trường[:Kiểu]> <giá trị>   (bấm phím đến khi field == giá trị)
 *   setf <Lớp> <trườngTĩnh> [<trường>...] -- <trường[:Kiểu]> i:n|z:b   (đặt field instance; ví dụ ép cd.a=2 để mở menu thử)
 *   quit
 */
public class MeRun {
    static MIDlet midlet;
    static ClassLoader cl;
    static Robot robot;
    static String out;

    static Displayable cur() { return Display.getDisplay(midlet).getCurrent(); }

    /** name hoặc name:Kiểu (Kiểu = tên đơn giản của kiểu field, ví dụ a:cd, a:short[]) để phân biệt field trùng tên. */
    static Object field(Class<?> c, Object o, String name, boolean wantStatic) throws Exception {
        String type = null;
        int col = name.indexOf(':');
        if (col > 0) { type = name.substring(col + 1); name = name.substring(0, col); }
        for (Class<?> k = c; k != null; k = k.getSuperclass()) {
            for (Field f : k.getDeclaredFields()) {
                if (f.getName().equals(name) && Modifier.isStatic(f.getModifiers()) == wantStatic && (type == null || f.getType().getSimpleName().equals(type))) { f.setAccessible(true); return f.get(o); }
            }
        }
        throw new NoSuchFieldException(name);
    }

    public static void main(String[] a) throws Exception {
        String jar = a[0], script = a[1]; out = a[2];
        new File(out).mkdirs();
        robot = new Robot();
        final String[] margs = {"--device", "dev240/device.xml", "--appclasspath", jar, "Main"};
        Thread t = new Thread() { public void run() { try { org.microemu.app.Main.main(margs); } catch (Throwable e) { e.printStackTrace(); } } };
        t.start();
        long t0 = System.currentTimeMillis();
        while ((midlet = org.microemu.MIDletBridge.getCurrentMIDlet()) == null || cur() == null) {
            Thread.sleep(200);
            if (System.currentTimeMillis() - t0 > 30000) throw new RuntimeException("midlet không khởi động");
        }
        cl = midlet.getClass().getClassLoader();
        System.out.println("MERUN started " + cur().getClass().getName());
        BufferedReader br = new BufferedReader(new FileReader(script));
        String ln;
        while ((ln = br.readLine()) != null) {
            ln = ln.trim();
            if (ln.isEmpty() || ln.startsWith("#")) continue;
            String[] w = ln.split("\\s+");
            try {
                switch (w[0]) {
                    case "wait": Thread.sleep(Long.parseLong(w[1])); break;
                    case "say": System.out.println("MERUN " + ln.substring(4)); break;
                    case "show": call(cur(), "showNotify"); break;
                    case "key": case "keys": {
                        int code = Integer.parseInt(w[1]);
                        int n = w[0].equals("key") ? 1 : Integer.parseInt(w[2]);
                        long gap = w[0].equals("key") ? (w.length > 2 ? Long.parseLong(w[2]) : 80) : Long.parseLong(w[3]);
                        for (int i = 0; i < n; i++) { callInt(cur(), "keyPressed", code); Thread.sleep(60); callInt(cur(), "keyReleased", code); Thread.sleep(gap); }
                        break; }
                    case "spam": {            // spam <phím> <khoảng ms> <timeout ms> <Lớp> <trườngTĩnh> [<trường>...] -- <trường[:Kiểu]> <giá trị>: bấm phím đến khi field instance cuối chuỗi == giá trị
                        int code = Integer.parseInt(w[1]); long gap = Long.parseLong(w[2]), lim = System.currentTimeMillis() + Long.parseLong(w[3]);
                        int sep = Arrays.asList(w).indexOf("--");
                        boolean ok = false;
                        while (System.currentTimeMillis() < lim) {
                            Object o = field(cl.loadClass(w[4]), null, w[5], true);
                            for (int i = 6; i < sep; i++) o = field(o.getClass(), o, w[i], false);
                            Object v = field(o.getClass(), o, w[sep + 1], false);
                            if (String.valueOf(v).equals(w[sep + 2])) { ok = true; break; }
                            callInt(cur(), "keyPressed", code); Thread.sleep(60); callInt(cur(), "keyReleased", code); Thread.sleep(gap);
                        }
                        System.out.println("MERUN spam " + ln.substring(5) + " -> " + ok); break; }
                    case "shot": {
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        BufferedImage im = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
                        ImageIO.write(im, "png", new File(out, w[1])); break; }
                    case "until": {
                        Class<?> c = cl.loadClass(w[1]);
                        long lim = System.currentTimeMillis() + Long.parseLong(w[4]);
                        boolean ok = false;
                        while (System.currentTimeMillis() < lim) { Object v = field(c, null, w[2], true); if (v != null && String.valueOf(v).equals(w[3])) { ok = true; break; } Thread.sleep(100); }
                        System.out.println("MERUN until " + w[1] + "." + w[2] + "==" + w[3] + " -> " + ok);
                        break; }
                    case "probe": {
                        Class<?> c = cl.loadClass(w[1]);
                        Object o = field(c, null, w[2], true);
                        for (int i = 3; i < w.length && o != null; i++) o = field(o.getClass(), o, w[i], false);
                        System.out.println("MERUN probe " + ln.substring(6) + " = " + o); break; }
                    case "callm": {           // callm <Lớp> <trườngTĩnh> [<trường>...] -- <hàm> [args typed: i:1 z:true s:text S:a;b;c]
                        int sep = Arrays.asList(w).indexOf("--");
                        Class<?> c = cl.loadClass(w[1]);
                        Object o = field(c, null, w[2], true);
                        for (int i = 3; i < sep; i++) o = field(o.getClass(), o, w[i], false);
                        Object r = invoke(o.getClass(), o, w[sep + 1], Arrays.copyOfRange(w, sep + 2, w.length));
                        System.out.println("MERUN callm " + ln.substring(6) + " -> " + r); break; }
                    case "setf": {            // setf <Lớp> <trườngTĩnh> [<trường>...] -- <trường[:Kiểu]> <i:n|z:b>  (đặt field instance cuối chuỗi)
                        int sep = Arrays.asList(w).indexOf("--");
                        Class<?> c = cl.loadClass(w[1]);
                        Object o = field(c, null, w[2], true);
                        for (int i = 3; i < sep; i++) o = field(o.getClass(), o, w[i], false);
                        String fn = w[sep + 1], val = w[sep + 2]; String tp = null; int col = fn.indexOf(':');
                        if (col > 0) { tp = fn.substring(col + 1); fn = fn.substring(0, col); }
                        boolean done = false;
                        for (Class<?> k = o.getClass(); k != null && !done; k = k.getSuperclass()) for (Field f : k.getDeclaredFields()) {
                            if (!f.getName().equals(fn) || Modifier.isStatic(f.getModifiers()) || (tp != null && !f.getType().getSimpleName().equals(tp))) continue;
                            f.setAccessible(true);
                            if (val.startsWith("z:")) f.setBoolean(o, Boolean.parseBoolean(val.substring(2)));
                            else { int n = Integer.parseInt(val.substring(2)); Class<?> ft = f.getType(); if (ft == byte.class) f.setByte(o, (byte) n); else if (ft == short.class) f.setShort(o, (short) n); else f.setInt(o, n); }
                            done = true; break;
                        }
                        System.out.println("MERUN setf " + ln.substring(5) + " -> " + done); break; }
                    case "calls": {           // calls <Lớp> <hàm tĩnh> [args]
                        Class<?> c = cl.loadClass(w[1]);
                        Object r = invoke(c, null, w[2], Arrays.copyOfRange(w, 3, w.length));
                        System.out.println("MERUN calls " + ln.substring(6) + " -> " + r); break; }
                    case "quit": System.out.println("MERUN quit"); System.out.flush(); Runtime.getRuntime().halt(0);
                    default: System.out.println("MERUN ??? " + ln);
                }
            } catch (Throwable e) { System.out.println("MERUN ERROR in '" + ln + "': " + e); e.printStackTrace(System.out); }
        }
        System.out.println("MERUN end of script");
        System.out.flush();
        Runtime.getRuntime().halt(0);
    }

    static Object invoke(Class<?> c, Object o, String name, String[] a) throws Exception {
        Object[] args = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            String v = a[i];
            if (v.startsWith("i:")) args[i] = Integer.valueOf(v.substring(2));
            else if (v.startsWith("z:")) args[i] = Boolean.valueOf(v.substring(2));
            else if (v.startsWith("s:")) args[i] = v.substring(2);
            else if (v.startsWith("S:")) args[i] = v.substring(2).split(";");
            else throw new RuntimeException("arg? " + v);
        }
        for (Class<?> k = c; k != null; k = k.getSuperclass()) {
            for (Method m : k.getDeclaredMethods()) {
                if (!m.getName().equals(name) || m.getParameterTypes().length != args.length) continue;
                Class<?>[] pt = m.getParameterTypes(); boolean ok = true;
                for (int i = 0; i < pt.length && ok; i++) {
                    Class<?> t = pt[i];
                    if (args[i] instanceof Integer) ok = t == int.class || t == short.class || t == byte.class || t == long.class;
                    else if (args[i] instanceof Boolean) ok = t == boolean.class;
                    else if (args[i] instanceof String) ok = t == String.class;
                    else ok = t == String[].class;
                }
                if (!ok) continue;
                m.setAccessible(true);
                Object[] cv = new Object[args.length];
                for (int i = 0; i < cv.length; i++) {
                    Class<?> t = pt[i]; Object v = args[i];
                    if (v instanceof Integer) { int n = (Integer) v; cv[i] = t == short.class ? (Object) Short.valueOf((short) n) : t == byte.class ? (Object) Byte.valueOf((byte) n) : t == long.class ? (Object) Long.valueOf(n) : v; }
                    else cv[i] = v;
                }
                try { return m.invoke(o, cv); } catch (InvocationTargetException e) { throw new RuntimeException("target threw " + e.getCause(), e.getCause()); }
            }
        }
        throw new NoSuchMethodException(name + "/" + args.length);
    }
    static void call(Object o, String m) throws Exception { Method me = find(o.getClass(), m, null); me.invoke(o); }
    static void callInt(Object o, String m, int v) throws Exception { Method me = find(o.getClass(), m, int.class); me.invoke(o, v); }
    static Method find(Class<?> c, String name, Class<?> p) throws Exception {
        for (Class<?> k = c; k != null; k = k.getSuperclass()) {
            try { Method m = p == null ? k.getDeclaredMethod(name) : k.getDeclaredMethod(name, p); m.setAccessible(true); return m; } catch (NoSuchMethodException e) { }
        }
        throw new NoSuchMethodException(name);
    }
}
