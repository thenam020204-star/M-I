import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

public final class cw {
  private u a_u;

  public cw(MIDlet var1, Display var2, Displayable var3, v var4, int var5, int var6, int var7, int var8, int var9, int var10, String var11) {
    this.a_u = new u(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, 888888, var11);
  }

  public final void a(int var1, int var2) {
    this.a_u.a(1, var2);
  }

  public final void a(int var1) {
    this.a_u.a_V(var1);
  }

  public final void a(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
    this.a_u.a(var1, var2, 0, 0, var5, var6, var7);
  }

  public final cb a() {
    return this.a_u.a_cb();
  }
}
