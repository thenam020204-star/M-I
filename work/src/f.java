/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Graphics
 */
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import rpg.d;

public final class f
extends Canvas {
    public static az var_az_a;
    private static int var_int_a;
    private static int var_int_b;
    private static int var_int_c;
    public static boolean var_boolean_a;
    private static byte var_byte_a;
    private static ea var_ea_a;
    private static boolean var_boolean_c;
    public static boolean var_boolean_b;

    public f() {
        this.setFullScreenMode(true);
    }

    protected final void paint(Graphics object) {
        if (var_boolean_b) {
            int n2 = this.getWidth();
            int n3 = this.getHeight();
            object.setClip(0, 0, n3, n2);
            object.setColor(0);
            object.fillRect(0, 0, n3, n2);
            object.setColor(0xFFFFFF);
            object = new cm((Graphics)object);
            ac.a((cm)object, n3 >> 1, n2 >> 1, d.D, 0, 1);
            return;
        }
        if (!ds.var_boolean_a) {
            int n4 = this.getWidth();
            int n5 = this.getHeight();
            object.setClip(0, 0, n4, n5);
            object.setColor(0);
            object.fillRect(0, 0, n4, n5);
            object.setColor(0xFFFFFF);
            object = new cm((Graphics)object);
            ac.a((cm)object, n4 >> 1, n5 >> 1, d.E, 0, 1);
            return;
        }
        if (var_boolean_a) {
            int n6 = this.getWidth();
            int n7 = this.getHeight();
            object.setClip(0, 0, n6, n7);
            object.setColor(0);
            object.fillRect(0, 0, n6, n7);
            object.setColor(0xFFFFFF);
            if (var_byte_a < 20) {
                object = new cm((Graphics)object);
                ac.a((cm)object, n6 >> 1, n7 >> 1, d.C, 0, 1);
            }
            byte by2 = var_byte_a;
            var_byte_a = (byte)(by2 + 1);
            if (by2 > 30) {
                var_byte_a = 0;
            }
            return;
        }
        if (cm.var_javax_microedition_lcdui_Image_a != null) {
            object.drawImage(cm.var_javax_microedition_lcdui_Image_a, 0, 0, 20);
        }
    }

    protected final void keyPressed(int n2) {
        if (n2 == -12) {
            bq.i();
        }
        if (var_boolean_b) {
            return;
        }
        if (!ds.var_boolean_a) {
            return;
        }
        if (var_boolean_a) {
            if (this.isShown()) {
                var_boolean_a = false;
                bq.j();
                if (var_az_a != null) {
                    var_az_a.b();
                }
            }
            return;
        }
        var_int_a = n2;
    }

    protected final void keyReleased(int n2) {
        var_int_b = n2;
    }

    public static void a() {
        if (var_int_a != 0) {
            if (var_int_c != 0) {
                if (var_az_a != null) {
                    var_az_a.boolean_a(1, var_int_c);
                }
                var_int_c = 0;
            }
            var_int_c = var_int_a;
            if (var_az_a != null) {
                var_az_a.boolean_a(0, var_int_a);
            }
            var_int_a = 0;
        }
        if (var_int_b != 0) {
            if (var_int_c != 0) {
                if (var_az_a != null) {
                    var_az_a.boolean_a(1, var_int_c);
                }
                var_int_c = 0;
            }
            var_int_b = 0;
        }
    }

    protected final void showNotify() {
    }

    protected final void hideNotify() {
        if (!ds.var_boolean_a) {
            return;
        }
        var_boolean_a = true;
        bq.i();
        if (var_az_a != null) {
            var_az_a.boolean_a(1, var_int_c);
            var_int_c = 0;
            var_az_a.void_a();
        }
    }

    public static boolean a(az az2) {
        boolean bl2 = var_az_a == null;
        var_az_a = az2;
        return bl2;
    }

    public final void sizeChanged(int n2, int n3) {
        if (n2 > n3) {
            var_boolean_b = true;
            return;
        }
        var_boolean_b = false;
    }

    public final int getWidth() {
        return 240;
    }

    public final int getHeight() {
        return 320;
    }

    static {
        var_az_a = null;
        var_int_a = 0;
        var_int_b = 0;
        var_int_c = 0;
        var_boolean_a = false;
        var_byte_a = 0;
        var_ea_a = null;
        var_boolean_c = false;
        var_boolean_b = false;
    }
}

