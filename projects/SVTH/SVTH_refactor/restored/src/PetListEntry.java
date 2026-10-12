public final class PetListEntry {
   private int slotIndex;
   private boolean deployed;
   String name;
   private int level;

   public PetListEntry() {
      this.deployed = false;
   }

   public PetListEntry(int var1, int var2, boolean var3, String var4, int var5) {
      this.slotIndex = var2;
      this.deployed = var3;
      this.name = var4;
      this.level = var5;
   }

   public final int getSlotIndex() {
      return this.slotIndex;
   }

   public final int getLevel() {
      return this.level;
   }

   public final boolean isDeployed() {
      return this.deployed;
   }
}
