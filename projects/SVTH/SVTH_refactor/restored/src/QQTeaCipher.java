import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public final class QQTeaCipher {
   private byte[] plainBlock;
   private byte[] prevPlainBlock;
   private byte[] output;
   private int outPos;
   private int prevOutPos;
   private int blockPos;
   private int padCount;
   private byte[] key;
   private boolean isFirstBlock = true;
   private Random random = new Random();

   public QQTeaCipher() {
   }

   private static long readUInt32(byte[] var0, int var1) {
      long var3 = 0L;

      for (int var2 = var1 + 4; var1 < var2; var1++) {
         var3 = var3 << 8 | (long)(var0[var1] & 255);
      }

      return var3 & 4294967295L | var3 >>> 32;
   }

   public final byte[] encrypt(byte[] var1, byte[] var2) {
      int var3 = var1.length;
      byte[] var6 = var1;
      QQTeaCipher var5 = this;
      this.plainBlock = new byte[8];
      this.prevPlainBlock = new byte[8];
      this.blockPos = 1;
      this.padCount = 0;
      this.outPos = this.prevOutPos = 0;
      this.key = var2;
      this.isFirstBlock = true;
      this.blockPos = (var3 + 10) % 8;
      if (this.blockPos != 0) {
         this.blockPos = 8 - this.blockPos;
      }

      this.output = new byte[var3 + this.blockPos + 10];
      this.plainBlock[0] = (byte)(this.random.nextInt() & 248 | this.blockPos);

      for (int var7 = 1; var7 <= var5.blockPos; var7++) {
         var5.plainBlock[var7] = (byte)var5.random.nextInt();
      }

      var5.blockPos++;

      for (int var8 = 0; var8 < 8; var8++) {
         var5.prevPlainBlock[var8] = 0;
      }

      var5.padCount = 1;

      while (var5.padCount <= 2) {
         if (var5.blockPos < 8) {
            var5.plainBlock[var5.blockPos++] = (byte)var5.random.nextInt();
            var5.padCount++;
         }

         if (var5.blockPos == 8) {
            var5.encryptNextBlock();
         }
      }

      int var9 = 0;

      while (var3 > 0) {
         if (var5.blockPos < 8) {
            var5.plainBlock[var5.blockPos++] = var6[var9++];
            var3--;
         }

         if (var5.blockPos == 8) {
            var5.encryptNextBlock();
         }
      }

      var5.padCount = 1;

      while (var5.padCount <= 7) {
         if (var5.blockPos < 8) {
            var5.plainBlock[var5.blockPos++] = 0;
            var5.padCount++;
         }

         if (var5.blockPos == 8) {
            var5.encryptNextBlock();
         }
      }

      return var5.output;
   }

   private byte[] encipherBlock(byte[] var1) {
      try {
         int var2 = 16;
         long var3 = readUInt32(var1, 0);
         long var5 = readUInt32(var1, 4);
         long var7 = readUInt32(this.key, 0);
         long var9 = readUInt32(this.key, 4);
         long var11 = readUInt32(this.key, 8);
         long var13 = readUInt32(this.key, 12);

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

   private void encryptNextBlock() {
      for (this.blockPos = 0; this.blockPos < 8; this.blockPos++) {
         if (this.isFirstBlock) {
            this.plainBlock[this.blockPos] = (byte)(this.plainBlock[this.blockPos] ^ this.prevPlainBlock[this.blockPos]);
         } else {
            this.plainBlock[this.blockPos] = (byte)(this.plainBlock[this.blockPos] ^ this.output[this.prevOutPos + this.blockPos]);
         }
      }

      System.arraycopy(this.encipherBlock(this.plainBlock), 0, this.output, this.outPos, 8);

      for (this.blockPos = 0; this.blockPos < 8; this.blockPos++) {
         this.output[this.outPos + this.blockPos] = (byte)(this.output[this.outPos + this.blockPos] ^ this.prevPlainBlock[this.blockPos]);
      }

      System.arraycopy(this.plainBlock, 0, this.prevPlainBlock, 0, 8);
      this.prevOutPos = this.outPos;
      this.outPos += 8;
      this.blockPos = 0;
      this.isFirstBlock = false;
   }
}
