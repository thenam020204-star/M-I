public final class PetInfo {
   public int petId;
   public String name;
   public int[] stats;
   public boolean isDeployed;
   public byte[] elementValues;
   short[] skillIds;
   public Skill[] skills;
   public int uid;
   String dropSpec;
   byte[] refineItemIds;
   public boolean isFainted;
   byte ownerHeroId;
   byte[][] implantSlots = new byte[2][2];

   public PetInfo() {
      for (int var1 = 0; var1 < 2; var1++) {
         this.implantSlots[var1][0] = 0;
         this.implantSlots[var1][1] = 0;
      }
   }
}
