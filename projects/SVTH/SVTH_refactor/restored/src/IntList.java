public final class IntList {
   private int[] values = new int[30];
   private int count = 0;

   public IntList() {
   }

   protected final void add(int var1) {
      if (this.count > this.values.length - 1) {
         int[] var2 = new int[this.values.length];
         System.arraycopy(this.values, 0, var2, 0, var2.length);
         this.values = null;
         this.values = new int[var2.length + 30];
         System.arraycopy(var2, 0, this.values, 0, var2.length);
      }

      this.values[this.count] = var1;
      this.count++;
   }

   public final int get(int var1) {
      return this.values[var1];
   }

   public final int size() {
      return this.count;
   }

   protected final void clear() {
      this.count = 0;
      this.values = null;
      this.values = new int[30];
   }
}
