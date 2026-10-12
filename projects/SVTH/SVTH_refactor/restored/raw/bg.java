import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public abstract class bg {
  protected int a_I;
  protected int b_I;
  protected int c_I;
  protected int d_I;
  protected int e_I = 0;
  protected int f_I = 4;
  protected int g_I;
  protected int h_I = 0;
  protected int i_I = 0;
  protected int j_I = cg.b_S + 4;
  protected int k_I = 2;
  Vector a_Vector;

  public abstract void a(Graphics var1);

  public final int b() {
    return this.a_Vector != null ? this.a_Vector.size() : 0;
  }

  public abstract void a_V();

  public void a(int var1) {
    if (var1 >= this.a_Vector.size()) {
      throw new IndexOutOfBoundsException();
    } else {
      this.g_I = var1;
    }
  }

  public void e() {
    if (this.g_I < this.a_Vector.size() - 1) {
      this.g_I++;
      if (this.g_I >= this.h_I + this.i_I) {
        this.h_I++;
      }
    }
  }

  public void f() {
    if (this.g_I > 0) {
      this.g_I--;
      if (this.g_I < this.h_I) {
        this.h_I = this.g_I;
      }
    }
  }

  public final void b(int var1) {
    this.h_I = 0;
  }

  public final int[] a() {
    int[] var1;
    (var1 = new int[4])[0] = this.a_I;
    var1[1] = this.b_I;
    var1[2] = this.c_I;
    var1[3] = this.d_I;
    return var1;
  }

  public final int c() {
    return this.g_I;
  }

  public final void c(int var1) {
    this.j_I = var1;
  }

  public void a(int var1, int var2, int var3, int var4) {
    this.a_I = var1;
    this.b_I = var2;
    this.c_I = var3;
    this.d_I = var4;
    this.i_I = var4 / this.j_I;
    this.e_I = 0;
    this.e_I = this.e_I + (var3 - 2 * this.f_I);
  }
}
