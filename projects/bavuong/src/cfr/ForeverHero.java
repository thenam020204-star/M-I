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

public class ForeverHero
extends MIDlet {
    public Display a;
    public o b;
    public static int _D_D = 0;

    public final void startApp() {
        if (_D_D == 0) {
            o o2 = new o(this, 0);
            o2._e();
            ++_D_D;
        } else {
            this._d_d();
        }
    }

    public final void destroyApp(boolean bl) {
    }

    public final void pauseApp() {
        this.b.q = true;
        this.b.hideNotify();
    }

    public void _c_c() {
        this.b = new o(this);
        this.a = Display.getDisplay((MIDlet)this);
    }

    public void _d_d() {
        this.a.setCurrent((Displayable)this.b);
    }

    public void _a_a() {
        this._c_c();
    }

    public void _b_b() {
        this._d_d();
    }
}

