import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;

public final class MusicPlayer {
   private Player player;
   private VolumeControl volumeControl;
   private byte volume = 100;
   public String fileName;

   public MusicPlayer() {
   }

   public final void load(String var1) {
      try {
         byte[] var2;
         if (DeviceConfig.cacheMusic) {
            var2 = ResourceCache.loadBytes(var1, DeviceConfig.cacheMusicGroup);
         } else {
            var2 = Utils.readResource(var1);
         }

         if (var2 != null) {
            ByteArrayInputStream var6 = new ByteArrayInputStream(var2);
            this.fileName = var1;
            if (var1.toLowerCase().endsWith(".mid")) {
               this.player = Manager.createPlayer(var6, "audio/midi");
            } else if (var1.toLowerCase().endsWith(".wav")) {
               this.player = Manager.createPlayer(var6, "audio/x-wav");
            } else if (var1.toLowerCase().endsWith(".amr")) {
               this.player = Manager.createPlayer(var6, "audio/amr");
            }

            this.player.realize();
            this.player.setLoopCount(-1);
            this.player.prefetch();
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      } catch (MediaException var4) {
         var4.printStackTrace();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public final void setLoopCount(int var1) {
      if (this.player != null) {
         this.player.setLoopCount(var1);
      }
   }

   public final void setVolume(int var1) {
      this.volume = (byte)var1;
      if (this.player != null) {
         this.volumeControl = (VolumeControl)this.player.getControl("VolumeControl");
      }

      if (this.volumeControl != null) {
         this.volumeControl.setLevel(this.volume);
      }
   }

   public final void start() {
      if (this.player != null) {
         try {
            this.player.start();
            return;
         } catch (MediaException var2) {
            var2.printStackTrace();
         }
      }
   }

   public final void stop() {
      if (this.player != null) {
         try {
            this.player.stop();
            return;
         } catch (MediaException var2) {
            var2.printStackTrace();
         }
      }
   }

   public final void release() {
      if (this.player != null) {
         this.player.deallocate();
         if (!DeviceConfig.notUseMusicClose) {
            this.player.close();
         }

         this.player = null;
      }

      System.gc();

      try {
         Thread.sleep(20L);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }
   }
}
