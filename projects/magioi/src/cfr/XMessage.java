/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.midlet.MIDlet
 */
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public class XMessage
extends MIDlet {
    public static h a;
    public static Thread b;

    public XMessage() {
        a = new h(this);
        b = new Thread(a);
        b.start();
        Display.getDisplay((MIDlet)this).setCurrent((Displayable)a);
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean bl) {
        j.p();
    }
}

