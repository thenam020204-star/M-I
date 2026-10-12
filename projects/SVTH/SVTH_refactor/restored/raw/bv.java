import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public final class bv {
  private Player a_Player;
  private VolumeControl a_VolumeControl;
  private byte a_B = 100;
  public String a_String;

  public final void a(String var1) {
    try {
      byte[] var2;
      if (at.l_Z) {
        var2 = cc.a_AB(var1, at.j_B);
      } else {
        var2 = cg.a_AB(var1);
      }

      if (var2 != null) {
        ByteArrayInputStream var6 = new ByteArrayInputStream(var2);
        this.a_String = var1;
        if (var1.toLowerCase().endsWith(".mid")) {
          this.a_Player = Manager.createPlayer(var6, "audio/midi");
        } else if (var1.toLowerCase().endsWith(".wav")) {
          this.a_Player = Manager.createPlayer(var6, "audio/x-wav");
        } else if (var1.toLowerCase().endsWith(".amr")) {
          this.a_Player = Manager.createPlayer(var6, "audio/amr");
        }

        this.a_Player.realize();
        this.a_Player.setLoopCount(-1);
        this.a_Player.prefetch();
      }
    } catch (IOException var3) {
      var3.printStackTrace();
    } catch (MediaException var4) {
      var4.printStackTrace();
    } catch (Exception var5) {
      var5.printStackTrace();
    }
  }

  public final void a(int var1) {
    if (this.a_Player != null) {
      this.a_Player.setLoopCount(var1);
    }
  }

  public final void b(int var1) {
    this.a_B = (byte)var1;
    if (this.a_Player != null) {
      this.a_VolumeControl = (VolumeControl)this.a_Player.getControl("VolumeControl");
    }

    if (this.a_VolumeControl != null) {
      this.a_VolumeControl.setLevel(this.a_B);
    }
  }

  public final void a() {
    if (this.a_Player != null) {
      try {
        this.a_Player.start();
        return;
      } catch (MediaException var2) {
        var2.printStackTrace();
      }
    }
  }

  public final void b() {
    if (this.a_Player != null) {
      try {
        this.a_Player.stop();
        return;
      } catch (MediaException var2) {
        var2.printStackTrace();
      }
    }
  }

  public final void c() {
    if (this.a_Player != null) {
      this.a_Player.deallocate();
      if (!at.c_Z) {
        this.a_Player.close();
      }

      this.a_Player = null;
    }

    System.gc();

    try {
      Thread.sleep(20L);
    } catch (InterruptedException var2) {
      var2.printStackTrace();
    }
  }
}
