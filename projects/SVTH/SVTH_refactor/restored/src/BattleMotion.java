public final class BattleMotion {
   public BattleMotion() {
   }

   public static int directionTo(int var0, int var1) {
      byte var2;
      if (var0 - var1 > 0) {
         var2 = 2;
      } else {
         var2 = 3;
      }

      return var2;
   }

   public static Point step(int var0, int var1, int var2, int var3, int var4) {
      Point var5 = new Point();
      if (var4 == 2) {
         var0 -= var2 / 100000;
      } else if (var4 == 3) {
         var0 += var2 / 100000;
      }

      var1 += var3 / 100000;
      var5.x = var0;
      var5.y = var1;
      return var5;
   }

   public static int[] velocity(int var0, int var1, int var2, int var3, int var4) {
      int[] var5 = new int[]{0, 0};
      var0 = Math.abs(var2 - var0);
      var5[0] = var0 * 100000 / var4;
      var5[0] = var5[0] > 0 ? var5[0] : -var5[0];
      var5[1] = (var3 - var1) * 100000 / var4;
      return var5;
   }

   static {
      new BattleMotion();
   }
}
