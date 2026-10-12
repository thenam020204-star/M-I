import javax.microedition.lcdui.Graphics;

public abstract class cz {
  public short d_S;
  public short e_S;
  public short f_S;
  public short g_S;
  public boolean j_Z = true;
  public byte i_B;
  public short h_S;
  public short i_S;
  public boolean k_Z = true;

  public final void a(int var1, int var2) {
    this.d_S = (short)var1;
    this.e_S = (short)var2;
  }

  public abstract void a(Graphics var1);

  public abstract short[] a_AS();

  public abstract short[] a_AS(int var1);

  public abstract void b(Graphics var1);
}
