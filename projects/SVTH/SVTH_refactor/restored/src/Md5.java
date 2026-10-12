public final class Md5 {
   private static int[][] shifts = new int[][]{{7, 12, 17, 22}, {5, 9, 14, 20}, {4, 11, 16, 23}, {6, 10, 15, 21}};
   private static byte[] PADDING = new byte[]{
      -128,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0,
      0
   };
   private long[] state = new long[4];
   private long[] bitCount = new long[2];
   private byte[] buffer = new byte[64];
   private byte[] digest = new byte[16];

   public Md5() {
      this.reset();
   }

   private void reset() {
      this.bitCount[0] = 0L;
      this.bitCount[1] = 0L;
      this.state[0] = 1732584193L;
      this.state[1] = 4023233417L;
      this.state[2] = 2562383102L;
      this.state[3] = 271733878L;
   }

   private static long ff(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var14;
      return (long)((int)(var14 = var0 + (var2 & var4 | ~var2 & var6) + var8 + var12) << (int)var10 | (int)var14 >>> (int)(32L - var10)) + var2;
   }

   private static long gg(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var14;
      return (long)((int)(var14 = var0 + (var2 & var6 | var4 & ~var6) + var8 + var12) << (int)var10 | (int)var14 >>> (int)(32L - var10)) + var2;
   }

   private static long hh(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var14;
      return (long)((int)(var14 = var0 + (var2 ^ var4 ^ var6) + var8 + var12) << (int)var10 | (int)var14 >>> (int)(32L - var10)) + var2;
   }

   private static long ii(long var0, long var2, long var4, long var6, long var8, long var10, long var12) {
      long var14;
      return (long)((int)(var14 = var0 + (var4 ^ (var2 | ~var6)) + var8 + var12) << (int)var10 | (int)var14 >>> (int)(32L - var10)) + var2;
   }

   private void update(byte[] var1, int var2) {
      byte[] var5 = new byte[64];
      int var4 = (int)(this.bitCount[0] >>> 3) & 63;
      if ((this.bitCount[0] = this.bitCount[0] + (long)(var2 << 3)) < (long)(var2 << 3)) {
         this.bitCount[1]++;
      }

      this.bitCount[1] = this.bitCount[1] + (long)(var2 >>> 29);
      int var3 = 64 - var4;
      if (var2 >= var3) {
         copyBytes(this.buffer, var1, var4, 0, var3);
         this.transform(this.buffer);

         while (var3 + 63 < var2) {
            copyBytes(var5, var1, 0, var3, 64);
            this.transform(var5);
            var3 += 64;
         }

         var4 = 0;
      } else {
         var3 = 0;
      }

      copyBytes(this.buffer, var1, var4, var3, var2 - var3);
   }

   private static void copyBytes(byte[] var0, byte[] var1, int var2, int var3, int var4) {
      for (int var5 = 0; var5 < var4; var5++) {
         var0[var2 + var5] = var1[var3 + var5];
      }
   }

   private void transform(byte[] var1) {
      long var2 = this.state[0];
      long var4 = this.state[1];
      long var6 = this.state[2];
      long var8 = this.state[3];
      long[] var10;
      long[] var10000 = var10 = new long[16];
      byte[] var11 = var1;
      long[] var14 = var10000;
      int var12 = 0;

      for (byte var13 = 0; var13 < 64; var13 += 4) {
         var14[var12] = toUnsigned(var11[var13]) | toUnsigned(var11[var13 + 1]) << 8 | toUnsigned(var11[var13 + 2]) << 16 | toUnsigned(var11[var13 + 3]) << 24;
         var12++;
      }

      var2 = ff(var2, var4, var6, var8, var10[0], (long)shifts[0][0], 3614090360L);
      var8 = ff(var8, var2, var4, var6, var10[1], (long)shifts[0][1], 3905402710L);
      var6 = ff(var6, var8, var2, var4, var10[2], (long)shifts[0][2], 606105819L);
      var4 = ff(var4, var6, var8, var2, var10[3], (long)shifts[0][3], 3250441966L);
      var2 = ff(var2, var4, var6, var8, var10[4], (long)shifts[0][0], 4118548399L);
      var8 = ff(var8, var2, var4, var6, var10[5], (long)shifts[0][1], 1200080426L);
      var6 = ff(var6, var8, var2, var4, var10[6], (long)shifts[0][2], 2821735955L);
      var4 = ff(var4, var6, var8, var2, var10[7], (long)shifts[0][3], 4249261313L);
      var2 = ff(var2, var4, var6, var8, var10[8], (long)shifts[0][0], 1770035416L);
      var8 = ff(var8, var2, var4, var6, var10[9], (long)shifts[0][1], 2336552879L);
      var6 = ff(var6, var8, var2, var4, var10[10], (long)shifts[0][2], 4294925233L);
      var4 = ff(var4, var6, var8, var2, var10[11], (long)shifts[0][3], 2304563134L);
      var2 = ff(var2, var4, var6, var8, var10[12], (long)shifts[0][0], 1804603682L);
      var8 = ff(var8, var2, var4, var6, var10[13], (long)shifts[0][1], 4254626195L);
      var6 = ff(var6, var8, var2, var4, var10[14], (long)shifts[0][2], 2792965006L);
      var4 = ff(var4, var6, var8, var2, var10[15], (long)shifts[0][3], 1236535329L);
      var2 = gg(var2, var4, var6, var8, var10[1], (long)shifts[1][0], 4129170786L);
      var8 = gg(var8, var2, var4, var6, var10[6], (long)shifts[1][1], 3225465664L);
      var6 = gg(var6, var8, var2, var4, var10[11], (long)shifts[1][2], 643717713L);
      var4 = gg(var4, var6, var8, var2, var10[0], (long)shifts[1][3], 3921069994L);
      var2 = gg(var2, var4, var6, var8, var10[5], (long)shifts[1][0], 3593408605L);
      var8 = gg(var8, var2, var4, var6, var10[10], (long)shifts[1][1], 38016083L);
      var6 = gg(var6, var8, var2, var4, var10[15], (long)shifts[1][2], 3634488961L);
      var4 = gg(var4, var6, var8, var2, var10[4], (long)shifts[1][3], 3889429448L);
      var2 = gg(var2, var4, var6, var8, var10[9], (long)shifts[1][0], 568446438L);
      var8 = gg(var8, var2, var4, var6, var10[14], (long)shifts[1][1], 3275163606L);
      var6 = gg(var6, var8, var2, var4, var10[3], (long)shifts[1][2], 4107603335L);
      var4 = gg(var4, var6, var8, var2, var10[8], (long)shifts[1][3], 1163531501L);
      var2 = gg(var2, var4, var6, var8, var10[13], (long)shifts[1][0], 2850285829L);
      var8 = gg(var8, var2, var4, var6, var10[2], (long)shifts[1][1], 4243563512L);
      var6 = gg(var6, var8, var2, var4, var10[7], (long)shifts[1][2], 1735328473L);
      var4 = gg(var4, var6, var8, var2, var10[12], (long)shifts[1][3], 2368359562L);
      var2 = hh(var2, var4, var6, var8, var10[5], (long)shifts[2][0], 4294588738L);
      var8 = hh(var8, var2, var4, var6, var10[8], (long)shifts[2][1], 2272392833L);
      var6 = hh(var6, var8, var2, var4, var10[11], (long)shifts[2][2], 1839030562L);
      var4 = hh(var4, var6, var8, var2, var10[14], (long)shifts[2][3], 4259657740L);
      var2 = hh(var2, var4, var6, var8, var10[1], (long)shifts[2][0], 2763975236L);
      var8 = hh(var8, var2, var4, var6, var10[4], (long)shifts[2][1], 1272893353L);
      var6 = hh(var6, var8, var2, var4, var10[7], (long)shifts[2][2], 4139469664L);
      var4 = hh(var4, var6, var8, var2, var10[10], (long)shifts[2][3], 3200236656L);
      var2 = hh(var2, var4, var6, var8, var10[13], (long)shifts[2][0], 681279174L);
      var8 = hh(var8, var2, var4, var6, var10[0], (long)shifts[2][1], 3936430074L);
      var6 = hh(var6, var8, var2, var4, var10[3], (long)shifts[2][2], 3572445317L);
      var4 = hh(var4, var6, var8, var2, var10[6], (long)shifts[2][3], 76029189L);
      var2 = hh(var2, var4, var6, var8, var10[9], (long)shifts[2][0], 3654602809L);
      var8 = hh(var8, var2, var4, var6, var10[12], (long)shifts[2][1], 3873151461L);
      var6 = hh(var6, var8, var2, var4, var10[15], (long)shifts[2][2], 530742520L);
      var4 = hh(var4, var6, var8, var2, var10[2], (long)shifts[2][3], 3299628645L);
      var2 = ii(var2, var4, var6, var8, var10[0], (long)shifts[3][0], 4096336452L);
      var8 = ii(var8, var2, var4, var6, var10[7], (long)shifts[3][1], 1126891415L);
      var6 = ii(var6, var8, var2, var4, var10[14], (long)shifts[3][2], 2878612391L);
      var4 = ii(var4, var6, var8, var2, var10[5], (long)shifts[3][3], 4237533241L);
      var2 = ii(var2, var4, var6, var8, var10[12], (long)shifts[3][0], 1700485571L);
      var8 = ii(var8, var2, var4, var6, var10[3], (long)shifts[3][1], 2399980690L);
      var6 = ii(var6, var8, var2, var4, var10[10], (long)shifts[3][2], 4293915773L);
      var4 = ii(var4, var6, var8, var2, var10[1], (long)shifts[3][3], 2240044497L);
      var2 = ii(var2, var4, var6, var8, var10[8], (long)shifts[3][0], 1873313359L);
      var8 = ii(var8, var2, var4, var6, var10[15], (long)shifts[3][1], 4264355552L);
      var6 = ii(var6, var8, var2, var4, var10[6], (long)shifts[3][2], 2734768916L);
      var4 = ii(var4, var6, var8, var2, var10[13], (long)shifts[3][3], 1309151649L);
      var2 = ii(var2, var4, var6, var8, var10[4], (long)shifts[3][0], 4149444226L);
      var8 = ii(var8, var2, var4, var6, var10[11], (long)shifts[3][1], 3174756917L);
      var6 = ii(var6, var8, var2, var4, var10[2], (long)shifts[3][2], 718787259L);
      var4 = ii(var4, var6, var8, var2, var10[9], (long)shifts[3][3], 3951481745L);
      this.state[0] = this.state[0] + var2;
      this.state[1] = this.state[1] + var4;
      this.state[2] = this.state[2] + var6;
      this.state[3] = this.state[3] + var8;
   }

   private static void encode(byte[] var0, long[] var1, int var2) {
      int var3 = 0;

      for (byte var4 = 0; var4 < var2; var4 += 4) {
         var0[var4] = (byte)((int)(var1[var3] & 255L));
         var0[var4 + 1] = (byte)((int)(var1[var3] >>> 8 & 255L));
         var0[var4 + 2] = (byte)((int)(var1[var3] >>> 16 & 255L));
         var0[var4 + 3] = (byte)((int)(var1[var3] >>> 24 & 255L));
         var3++;
      }
   }

   private static long toUnsigned(byte var0) {
      return (long)(var0 < 0 ? var0 & 255 : var0);
   }

   public static String hexDigest(byte[] var0) {
      Md5 var5;
      (var5 = new Md5()).reset();
      var5.update(var0, var0.length);
      byte[] var1;
      encode(var1 = new byte[8], var5.bitCount, 8);
      int var2;
      var2 = (var2 = (int)(var5.bitCount[0] >>> 3) & 63) < 56 ? 56 - var2 : 120 - var2;
      var5.update(PADDING, var2);
      var5.update(var1, 8);
      encode(var5.digest, var5.state, 16);
      var0 = var5.digest;
      StringBuffer var7 = new StringBuffer(16);

      for (int var9 = 0; var9 < 16; var9++) {
         byte var3 = var0[var9];
         char[] var4 = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
         char[] var10;
         (var10 = new char[2])[0] = var4[var3 >>> 4 & 15];
         var10[1] = var4[var3 & 15];
         var7.append(new String(var10));
      }

      return var7.toString();
   }
}
