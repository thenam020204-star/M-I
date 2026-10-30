package nk;

import com.nokia.mid.sound.Sound;
import com.nokia.mid.sound.SoundListener;
import com.nokia.mid.ui.DeviceControl;
import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;
import com.nokia.mid.ui.FullCanvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

/** FullCanvas key codes and sizes, DeviceControl, Sound events on guest threads; driven by tests/midlets/scripts/nokia.inp. */
final class NokiaTest {
    static void p(String s) { System.out.println("> " + s); }

    static Thread eventThread;

    static final class Listener implements SoundListener {
        public void soundStateChanged(Sound s, int event) {
            Thread t = Thread.currentThread();
            p("sound event " + event + " state " + s.getState() + " onEventThread " + (t == eventThread));
        }
    }

    static final class Canvas extends FullCanvas {
        int paints;
        Sound tone;

        protected void paint(Graphics g) {
            if (eventThread == null) eventThread = Thread.currentThread();
            paints++;
            DirectGraphics dg = DirectUtils.getDirectGraphics(g);
            dg.fillTriangle(0, 0, 20, 0, 0, 20, 0xFF00FF00);
            p("paint " + getWidth() + "x" + getHeight());
        }

        protected void sizeChanged(int w, int h) { p("sizeChanged " + w + "x" + h); }

        protected void keyPressed(int k) {
            if (eventThread == null) eventThread = Thread.currentThread();
            String n;
            try { n = getKeyName(k); } catch (IllegalArgumentException e) { n = "?"; }
            int a;
            try { a = getGameAction(k); } catch (IllegalArgumentException e) { a = -99; }
            p("keyPressed " + k + " name " + n + " action " + a + (k == KEY_SOFTKEY1 ? " SOFTKEY1" : k == KEY_SOFTKEY2 ? " SOFTKEY2" : k == KEY_SOFTKEY3 ? " SOFTKEY3"
                : k == KEY_SEND ? " SEND" : k == KEY_END ? " END" : k == KEY_UP_ARROW ? " UP" : k == KEY_DOWN_ARROW ? " DOWN" : k == KEY_LEFT_ARROW ? " LEFT" : k == KEY_RIGHT_ARROW ? " RIGHT" : ""));
            if (k == 49) {   // '1': play a short tone, with a listener
                tone = new Sound(440, 100);
                tone.setSoundListener(new Listener());
                tone.play(1);
                p("playing state " + tone.getState());
            }
            if (k == 50) {   // '2': stop it again
                tone.stop();
                p("stopped state " + tone.getState());
            }
            if (k == 51) {   // '3': two plays in a row on one concurrent slot, the first is stopped
                final Sound a1 = new Sound(440, 5000), a2 = new Sound(880, 5000);
                SoundListener l = new SoundListener() { public void soundStateChanged(Sound s, int e) { p("slot " + (s == a1 ? "first" : "second") + " event " + e); } };
                a1.setSoundListener(l);
                a2.setSoundListener(l);
                a1.play(1);
                a2.play(1);
                p("states " + a1.getState() + " " + a2.getState());
                a1.release();
                a2.release();
            }
            if (k == 52) {   // '4': DeviceControl
                DeviceControl.startVibra(50, 300);
                DeviceControl.stopVibra();
                DeviceControl.setLights(0, 100);
                DeviceControl.flashLights(200);
                DeviceControl.startVibra(0, 0);
                try { DeviceControl.startVibra(101, 10); } catch (IllegalArgumentException e) { p("vibra 101 IllegalArgumentException"); }
                try { DeviceControl.setLights(1, 10); } catch (IllegalArgumentException e) { p("lights 1 IllegalArgumentException"); }
                try { DeviceControl.setLights(0, 101); } catch (IllegalArgumentException e) { p("level 101 IllegalArgumentException"); }
                try { DeviceControl.flashLights(-1); } catch (IllegalArgumentException e) { p("flash -1 IllegalArgumentException"); }
                p("device control done");
            }
        }
    }

    static void run(MIDlet m) {
        Canvas c = new Canvas();
        p("size before show " + c.getWidth() + "x" + c.getHeight());
        try { c.addCommand(new Command("x", Command.OK, 1)); p("addCommand no exception"); } catch (IllegalStateException e) { p("addCommand IllegalStateException"); }
        try { c.setCommandListener(null); p("setCommandListener no exception"); } catch (IllegalStateException e) { p("setCommandListener IllegalStateException"); }
        try { c.setCommandListener(new CommandListener() { public void commandAction(Command x, Displayable d) {} }); p("setCommandListener no exception"); } catch (IllegalStateException e) { p("setCommandListener IllegalStateException"); }
        p("constants " + FullCanvas.KEY_SOFTKEY1 + " " + FullCanvas.KEY_SOFTKEY2 + " " + FullCanvas.KEY_SOFTKEY3 + " " + FullCanvas.KEY_SEND + " " + FullCanvas.KEY_END + " "
            + FullCanvas.KEY_UP_ARROW + " " + FullCanvas.KEY_DOWN_ARROW + " " + FullCanvas.KEY_LEFT_ARROW + " " + FullCanvas.KEY_RIGHT_ARROW);
        Display.getDisplay(m).setCurrent(c);
        Image im = DirectUtils.createImage(2, 2, 0x00000000);
        p("transparent image mutable " + im.isMutable() + " pixel " + Integer.toHexString(pixel(im)));
    }

    static int pixel(Image im) {
        int[] a = new int[1];
        im.getRGB(a, 0, 1, 0, 0, 1, 1);
        return a[0];
    }
}
