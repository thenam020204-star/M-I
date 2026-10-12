import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class PetListPanel extends ListPanel {
   private Image deployedBadge;
   private int cursorBob = 0;
   Image cursorImg;

   public PetListPanel() {
      this.items = new Vector();
      ResourceCache.loadImage("/pic/number01.gf", 2);
      String var10001 = "/pic/D19.gf";
      this.deployedBadge = null;
   }

   public final void addEntry(int var1, int var2, boolean var3, String var4, int var5) {
      this.items.addElement(new PetListEntry(var1, var2, var3, var4, var5));
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final int getSelectedSlot() {
      int var2 = this.selectedIndex;
      if (var2 < this.items.size()) {
         return ((PetListEntry)this.items.elementAt(var2)).getSlotIndex();
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public final void draw(Graphics var1) {
      int var3 = this.y + this.padY + 2;
      int var4 = this.x + this.padX;
      int var5;
      if ((var5 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var5 = this.items.size();
      }

      if (this.items.size() > this.visibleRows) {
         GameUtil.drawScrollBar(var1, null, null, this.x + this.innerW - 5, this.y + 5, this.h - 10, this.items.size(), this.visibleRows, this.scrollTop);
      }

      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      for (int var6 = this.scrollTop; var6 < var5; var6++) {
         PetListEntry var2 = (PetListEntry)this.items.elementAt(var6);
         if (var6 == this.selectedIndex) {
            Utils.drawOutlinedText8(var1, var2.name, var4 + 5, var3, 9502459, 540331, 20);
            if (this.cursorImg != null) {
               var1.drawRegion(this.cursorImg, 0, 0, 15, 15, 0, var4 + this.cursorBob, var3 + this.rowHeight / 2, 3);
            }
         } else {
            var1.setColor(15793919);
            vh.drawString(var1, var2.name, var4 + 5, var3, 20);
         }

         Utils.drawOutlinedText8(var1, "" + var2.getLevel(), var4 + (this.innerW - 40), var3, 15921628, 5971029, 20);
         int var7 = vh.stringWidth(Utils.defaultFont, "" + var2.getLevel());
         Utils.drawOutlinedText8(var1, " cấp", var4 + var7 + (this.innerW - 40), var3, 16706636, 9193495, 20);
         if (var2.isDeployed()) {
            if (this.deployedBadge != null) {
               var1.drawImage(this.deployedBadge, var4 + this.innerW - 10, var3 - 2 + this.rowHeight / 2, 10);
            } else {
               Utils.drawOutlinedText(var1, "Xuất chiến", var4 + this.innerW - 10, var3 - 2 + this.rowHeight / 2 - Utils.lineHeight / 2, 16308570, 203551, 24);
            }
         }

         var3 += this.rowHeight;
      }
   }

   public final void clear() {
   }
}
