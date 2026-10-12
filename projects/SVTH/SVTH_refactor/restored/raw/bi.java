import java.util.Random;

public final class bi {
  public short a_S;
  public short b_S;
  public byte a_B;
  public byte b_B;
  public byte c_B = -3;
  public byte d_B = 6;
  private static Random a_Random;

  public bi() {
    if (a_Random == null) {
      a_Random = new Random();
    }

    this.a_S = (short)k.a(0, at.a_AS[0] + 150);
    this.b_S = (short)(-Math.abs(a_Random.nextInt() % at.a_AS[1]));
    this.a_B = -5;
    this.b_B = 10;
  }
}
