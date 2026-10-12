public final class SideQuestEntry {
   int questIndex;
   int type;
   String name = "";
   boolean isDone;

   public SideQuestEntry() {
      this.isDone = false;
   }

   public SideQuestEntry(int var1, int var2, String var3, boolean var4) {
      this.questIndex = var1;
      this.type = var2;
      this.name = var3;
      this.isDone = var4;
   }
}
