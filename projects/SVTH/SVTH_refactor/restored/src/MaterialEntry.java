import javax.microedition.lcdui.Image;

public final class MaterialEntry {
   Image icon;
   String name;
   int ownedCount;
   int requiredCount;

   public MaterialEntry() {
   }

   public MaterialEntry(Image var1, String var2, int var3, int var4) {
      this.icon = var1;
      this.name = var2;
      this.ownedCount = var3;
      this.requiredCount = var4;
   }
}
