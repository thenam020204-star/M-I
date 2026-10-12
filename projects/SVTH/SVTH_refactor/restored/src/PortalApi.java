import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public final class PortalApi {
   private PortalClient client;

   public PortalApi(MIDlet var1, Display var2, Displayable var3, PortalListener var4, int var5, int var6, int var7, int var8, int var9, int var10, String var11) {
      this.client = new PortalClient(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 888888, var11);
   }

   public final void openPage(int var1, int var2) {
      this.client.openPortal(1, var2);
   }

   public final void sendCommand(int var1) {
      this.client.sendCommand(var1);
   }

   public final void uploadScore(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
      this.client.uploadScore(var1, var2, 0, 0, var5, var6, var7);
   }

   public final QQUserProfile getUserProfile() {
      return this.client.getUserProfile();
   }
}
