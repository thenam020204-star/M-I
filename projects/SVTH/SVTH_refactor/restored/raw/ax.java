import javax.microedition.lcdui.Graphics;

public final class ax extends cz {
  private bx a_bx;
  public short a_S;
  private byte d_B;
  public byte a_B;
  public short b_S;
  public short[][] a_AAS;
  public short[][] b_AAS;
  public byte b_B;
  public byte c_B = 0;
  private short[] a_AS;
  private short[] b_AS;

  public ax(bx var1) {
    this.a_bx = var1;
    this.a_AS = new short[]{-9, -19, 18, 19};
    this.b_AS = new short[]{-12, -12, 24, 12};
  }

  public ax() {
    this.f_S = 18;
    this.g_S = 17;
    this.a_AS = new short[]{(short)(-this.f_S / 2), (short)(-this.g_S), this.f_S, this.g_S};
    this.b_AS = new short[]{(short)(-this.f_S / 2), (short)(-(this.g_S / 2)), this.f_S, (short)(this.g_S / 2)};
  }

  public final void a_V(int var1) {
    this.d_B = (byte)var1;
  }

  public final void a(Graphics var1) {
    if (this.j_Z) {
      this.a_bx.a_V(this.a_B);
      this.a_bx.d_S = this.d_S;
      this.a_bx.e_S = this.e_S;
      this.a_bx.a(var1);
      if (da.a_da.a_S % 3 == 0) {
        this.a_bx.a(true);
      }
    }
  }

  public final short[] a_AS(int var1) {
    return new short[]{(short)(this.d_S + this.b_AS[0]), (short)(this.e_S + this.b_AS[1]), this.b_AS[2], this.b_AS[3]};
  }

  public final short[] a_AS() {
    return this.a_AS;
  }

  public final void b(Graphics var1) {
    var1.drawRect(this.d_S + this.b_AS[0], this.e_S + this.b_AS[1], this.b_AS[2], this.b_AS[3]);
  }
}
