/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.media.Manager
 *  javax.microedition.media.MediaException
 *  javax.microedition.media.Player
 *  javax.microedition.media.control.VolumeControl
 */
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public final class q {
    private String[][] b = new String[][]{{"/snd/30.mid", "audio/midi"}};
    public Player a;

    public q(o o2) {
        this.a(0);
    }

    private void a(int n2) {
        try {
            String string = this.b[0][0];
            Class<?> clazz = this.getClass();
            InputStream inputStream = null;
            Object var6_5 = null;
            inputStream = o._m(clazz, string);
            this.a = Manager.createPlayer((InputStream)inputStream, (String)this.b[0][1]);
            return;
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
            return;
        }
    }

    public final void a() {
        try {
            if (o.g != 1) {
                return;
            }
            this.a.stop();
            this.a.prefetch();
            VolumeControl volumeControl = (VolumeControl)this.a.getControl("VolumeControl");
            volumeControl.setLevel(100);
            this.a.setLoopCount(-1);
            this.a.start();
            return;
        }
        catch (Exception exception) {
            return;
        }
    }

    public final void b() {
        try {
            this.a.stop();
            return;
        }
        catch (MediaException mediaException) {
            MediaException mediaException2 = mediaException;
            mediaException.printStackTrace();
            return;
        }
    }
}

