package vmspec;

import javax.microedition.midlet.MIDlet;

/** Character classification over ranges: Basic Latin + Latin-1 are required by CLDC (compared); the wider ranges are informational ('# '). */
public class Cl10Character extends MIDlet {
    static String sum(int from, int to) {
        int d = 0, lo = 0, up = 0, tl = 0, tu = 0, dg = 0, nlo = 0, nup = 0;
        for (int c = from; c <= to; c++) {
            char ch = (char) c;
            if (Character.isDigit(ch)) { d = d * 31 + c; }
            if (Character.isLowerCase(ch)) { lo = lo * 31 + c; nlo++; }
            if (Character.isUpperCase(ch)) { up = up * 31 + c; nup++; }
            tl = tl * 31 + Character.toLowerCase(ch);
            tu = tu * 31 + Character.toUpperCase(ch);
            dg = dg * 31 + Character.digit(ch, 16) * 7 + Character.digit(ch, 36) + Character.digit(ch, 10) * 3 + Character.digit(ch, 2);
        }
        return d + " " + lo + " " + up + " " + tl + " " + tu + " " + dg + " counts " + nlo + "/" + nup;
    }

    protected void startApp() {
        System.out.println("> ascii " + sum(0, 0x7f));
        System.out.println("> latin1 " + sum(0x80, 0xff));
        System.out.println("# latinExt " + sum(0x100, 0x24f));
        System.out.println("# greek " + sum(0x370, 0x3ff));
        System.out.println("# cyrillic " + sum(0x400, 0x4ff));
        System.out.println("# latinAdditional " + sum(0x1e00, 0x1eff));
        System.out.println("# bmp " + sum(0, 0xffff));
        StringBuffer sb = new StringBuffer();
        for (char c = 0xc0; c < 0x100; c++) { sb.append(Character.isLowerCase(c) ? 'l' : Character.isUpperCase(c) ? 'U' : '.'); }
        System.out.println("> latin1 map " + sb);
        sb = new StringBuffer();
        for (char c = 0xc0; c < 0x100; c++) { sb.append(Integer.toHexString(Character.toUpperCase(c))).append(',').append(Integer.toHexString(Character.toLowerCase(c))).append(' '); }
        System.out.println("> latin1 case " + sb);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
