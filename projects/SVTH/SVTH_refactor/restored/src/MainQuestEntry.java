public final class MainQuestEntry {
   private String name;
   private int status;

   public MainQuestEntry() {
   }

   public MainQuestEntry(int var1, int var2, String var3) {
      this.status = var2;
      this.name = var3;
   }

   public final String getName() {
      return this.name;
   }

   public final int getStatus() {
      return this.status;
   }
}
