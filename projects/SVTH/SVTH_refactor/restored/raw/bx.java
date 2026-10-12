import javax.microedition.lcdui.Graphics;

public final class bx extends cz {
  co a_co;
  private short a_S;
  private short b_S;
  boolean a_Z;

  public final int a() {
    return this.a_S;
  }

  public final int b() {
    return this.a_co != null ? this.a_co.a_I() : 0;
  }

  public final short[] a_AS(int var1) {
    return this.a_co != null ? this.a_co.a(this.a_S, this.b_S, this.d_S, this.e_S, var1, this.a_Z) : null;
  }

  public final short[] a(int var1, int var2, int var3) {
    return this.a_co != null ? this.a_co.a(var1, var2, this.d_S, this.e_S, var3, this.a_Z) : null;
  }

  public final int c() {
    return this.b_S;
  }

  public final int d() {
    return this.a_S >= 0 && this.a_co != null ? this.a_co.a(this.a_S) : 0;
  }

  public final int a_I(int var1) {
    return this.a_S >= 0 && this.a_co != null ? this.a_co.a(var1) : 0;
  }

  public final short[] a_AS() {
    if (!this.a_Z) {
      return this.a_co.a_AS();
    } else {
      short[] var1 = this.a_co.a_AS();
      return new short[]{(short)(-(var1[0] + var1[2])), var1[1], var1[2], var1[3]};
    }
  }

  public final void a(boolean var1) {
    if (this.b_S < this.a_I(this.a_S) - 1) {
      this.b_S++;
    } else {
      if (var1) {
        this.b_S = 0;
      }
    }
  }

  public final void a(Graphics var1) {
    if (this.j_Z && this.a_co != null) {
      this.a_co.a(var1, this.a_S, this.b_S, this.d_S, this.e_S, this.a_Z);
    }
  }

  public final void a(Graphics var1, boolean var2) {
    boolean var4 = false;
    if (this.j_Z && this.a_co != null) {
      this.a_co.a(var1, this.a_S, this.b_S, this.d_S, this.e_S, var2, false);
    }
  }

  public final void a(Graphics var1, int var2, int var3) {
    if (this.j_Z && this.a_co != null) {
      this.a_co.a(var1, this.a_S, this.b_S, (short)var2, (short)var3, this.a_Z);
    }
  }

  public final void b(Graphics var1) {
    short[] var2 = this.a(this.a_S, this.b_S, 1);
    var1.setColor(16711680);

    for (byte var3 = 0; var2 != null && var3 < var2.length; var3 += 4) {
      var1.drawRect(var2[var3], var2[var3 + 1], var2[var3 + 2], var2[var3 + 3]);
    }

    var2 = this.a(this.a_S, this.b_S, 2);
    var1.setColor(255);

    for (byte var6 = 0; var2 != null && var6 < var2.length; var6 += 4) {
      var1.drawRect(var2[var6], var2[var6 + 1], var2[var6 + 2], var2[var6 + 3]);
    }

    var2 = this.a(this.a_S, this.b_S, 3);
    var1.setColor(16711935);

    for (byte var7 = 0; var2 != null && var7 < var2.length; var7 += 4) {
      var1.drawRect(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3]);
    }
  }

  public final boolean a(String var1) {
    boolean var2 = this.a_Z;
    this.a_co = cc.a(var1, null, null, null, null, null, -1, var2, false, 0);
    return this.a_co != null;
  }

  public final boolean a(String var1, int var2) {
    boolean var4 = this.a_Z;
    this.a_co = cc.a(var1, null, null, null, null, null, -1, var4, false, var2);
    return this.a_co != null;
  }

  public final boolean a(String var1, String var2, String[] var3, String[] var4, int[] var5, int[] var6, int var7) {
    if (var4 == null && var6 == null) {
      boolean var11 = this.a_Z;
      this.a_co = cc.a(var1, var2, null, null, null, null, -1, var11, false, var7);
      return this.a_co != null;
    } else {
      this.a_co = new co();
      if (this.a_co.a_Z(var1)) {
        boolean var10 = this.a_Z;
        co var9 = this.a_co;
        this.a_co.a(var2, var3, var4, var5, var6, -1, var10, false, var7);
        return true;
      } else {
        return false;
      }
    }
  }

  public final void a_V(int var1) {
    if (var1 >= 0) {
      if (var1 <= this.b() - 1) {
        this.a_S = (short)var1;
      }

      if (this.b_S > this.a_I(this.a_S) - 1 && this.a_I(this.a_S) - 1 >= 0) {
        this.b_S = (short)(this.a_I(this.a_S) - 1);
      }
    }
  }

  public final void b(int var1) {
    if (var1 <= this.a_I(this.a_S) - 1) {
      this.b_S = (short)var1;
    }
  }
}
