import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public final class aj {
  private byte[] a_AB;
  private byte[] b_AB;
  private byte[] c_AB;
  private int a_I;
  private int b_I;
  private int c_I;
  private int d_I;
  private byte[] d_AB;
  private boolean a_Z = true;
  private Random a_Random = new Random();

  private static long a(byte[] var0, int var1) {
    long var3 = 0L;

    for (int var2 = var1 + 4; var1 < var2; var1++) {
      var3 = var3 << 8 | (long)(var0[var1] & 255);
    }

    return var3 & 4294967295L | var3 >>> 32;
  }

  public final byte[] a(byte[] var1, byte[] var2) {
    int var3 = var1.length;
    byte[] var6 = var1;
    aj var5 = this;
    this.a_AB = new byte[8];
    this.b_AB = new byte[8];
    this.c_I = 1;
    this.d_I = 0;
    this.a_I = this.b_I = 0;
    this.d_AB = var2;
    this.a_Z = true;
    this.c_I = (var3 + 10) % 8;
    if (this.c_I != 0) {
      this.c_I = 8 - this.c_I;
    }

    this.c_AB = new byte[var3 + this.c_I + 10];
    this.a_AB[0] = (byte)(this.a_Random.nextInt() & 248 | this.c_I);

    for (int var7 = 1; var7 <= var5.c_I; var7++) {
      var5.a_AB[var7] = (byte)var5.a_Random.nextInt();
    }

    var5.c_I++;

    for (int var8 = 0; var8 < 8; var8++) {
      var5.b_AB[var8] = 0;
    }

    var5.d_I = 1;

    while (var5.d_I <= 2) {
      if (var5.c_I < 8) {
        var5.a_AB[var5.c_I++] = (byte)var5.a_Random.nextInt();
        var5.d_I++;
      }

      if (var5.c_I == 8) {
        var5.a();
      }
    }

    int var9 = 0;

    while (var3 > 0) {
      if (var5.c_I < 8) {
        var5.a_AB[var5.c_I++] = var6[var9++];
        var3--;
      }

      if (var5.c_I == 8) {
        var5.a();
      }
    }

    var5.d_I = 1;

    while (var5.d_I <= 7) {
      if (var5.c_I < 8) {
        var5.a_AB[var5.c_I++] = 0;
        var5.d_I++;
      }

      if (var5.c_I == 8) {
        var5.a();
      }
    }

    return var5.c_AB;
  }

  private byte[] a(byte[] var1) {
    try {
      int var2 = 16;
      long var3 = a(var1, 0);
      long var5 = a(var1, 4);
      long var7 = a(this.d_AB, 0);
      long var9 = a(this.d_AB, 4);
      long var11 = a(this.d_AB, 8);
      long var13 = a(this.d_AB, 12);

      for (long var15 = 0L; var2-- > 0; var5 = var5 + ((var3 << 4) + var11 ^ var3 + var15 ^ (var3 >>> 5) + var13) & 4294967295L) {
        var15 = var15 + 2654435769L & 4294967295L;
        var3 = var3 + ((var5 << 4) + var7 ^ var5 + var15 ^ (var5 >>> 5) + var9) & 4294967295L;
      }

      ByteArrayOutputStream var18 = new ByteArrayOutputStream(8);
      DataOutputStream var19;
      (var19 = new DataOutputStream(var18)).writeInt((int)var3);
      var19.writeInt((int)var5);
      var19.close();
      return var18.toByteArray();
    } catch (IOException var17) {
      return null;
    }
  }

  private void a() {
    for (this.c_I = 0; this.c_I < 8; this.c_I++) {
      if (this.a_Z) {
        this.a_AB[this.c_I] = (byte)(this.a_AB[this.c_I] ^ this.b_AB[this.c_I]);
      } else {
        this.a_AB[this.c_I] = (byte)(this.a_AB[this.c_I] ^ this.c_AB[this.b_I + this.c_I]);
      }
    }

    System.arraycopy(this.a(this.a_AB), 0, this.c_AB, this.a_I, 8);

    for (this.c_I = 0; this.c_I < 8; this.c_I++) {
      this.c_AB[this.a_I + this.c_I] = (byte)(this.c_AB[this.a_I + this.c_I] ^ this.b_AB[this.c_I]);
    }

    System.arraycopy(this.a_AB, 0, this.b_AB, 0, 8);
    this.b_I = this.a_I;
    this.a_I += 8;
    this.c_I = 0;
    this.a_Z = false;
  }
}
