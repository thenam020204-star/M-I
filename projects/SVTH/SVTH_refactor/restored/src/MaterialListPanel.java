import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class MaterialListPanel extends ListPanel {
   Image cursorImg;
   private Image digitsImg;
   private int cursorBob;

   public MaterialListPanel() {
      new Vector();
      this.items = null;
      this.items = new Vector();
      if (this.digitsImg == null) {
         this.digitsImg = ResourceCache.loadImage("/pic/number01.gf", 2);
      }
   }

   public final void addMaterial(Image var1, String var2, int var3, int var4) {
      this.items.addElement(new MaterialEntry(var1, var2, var3, var4));
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      int var3 = this.y + this.padY;
      int var4;
      if ((var4 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var4 = this.items.size();
      }

      if (this.items.size() > this.visibleRows) {
         GameUtil.drawScrollBar(
            var1, null, null, this.x + this.innerW - 5, this.y, this.h - 10 - (this.padY << 2), this.items.size(), this.visibleRows, this.scrollTop
         );
      }

      for (int var5 = this.scrollTop; var5 < var4; var5++) {
         int var6 = this.x + this.padX;
         MaterialEntry var2 = (MaterialEntry)this.items.elementAt(var5);
         if (this.digitsImg != null) {
            int var11 = var6 + 15 + this.w * 3 / 4;
            int var10 = var2.requiredCount;
            int var9 = var2.ownedCount;
            var1.drawRegion(this.digitsImg, 60, 0, 6, 7, 0, var11, var3, 17);
            Utils.drawNumber(var1, var9, var11 - 3, var3, this.digitsImg, 6, 7, 24);
            Utils.drawNumber(var1, var10, var11 + 3, var3, this.digitsImg, 6, 7, 20);
         }

         Image var7 = var2.icon;
         if (var2.icon != null) {
            var1.drawImage(var7, var6 + 5 + (var7.getWidth() >> 1), var3, 3);
            if (var5 == this.selectedIndex) {
               var1.setColor(12842492);
               var1.drawRect(var6 + 5 - 1, var3 - (var7.getHeight() + 2) / 2, var7.getWidth() + 2, var7.getHeight() + 2);
               var1.setColor(541060);
               var1.drawRect(var6 + 5 - 2, var3 - 1 - (var7.getHeight() + 2) / 2, var7.getWidth() + 4, var7.getHeight() + 4);
            } else {
               var1.setColor(15589360);
               var1.drawRect(var6 + 5 - 1, var3 - (var7.getHeight() + 2) / 2, var7.getWidth() + 2, var7.getHeight() + 2);
               var1.setColor(6898011);
               var1.drawRect(var6 + 5 - 2, var3 - 1 - (var7.getHeight() + 2) / 2, var7.getWidth() + 4, var7.getHeight() + 4);
            }
         }

         if (var5 == this.selectedIndex) {
            if (this.cursorImg != null) {
               var1.drawRegion(this.cursorImg, 0, 0, 15, 15, 0, this.x + this.cursorBob, var3 + 4, 3);
            }

            Utils.drawOutlinedText(var1, var2.name, var6 + (this.w >> 1), var3 - 8, 16711680, 16777215, 17);
         } else {
            Utils.drawOutlinedText(var1, var2.name, var6 + (this.w >> 1), var3 - 8, 15987162, 5971280, 17);
         }

         var3 += this.rowHeight;
      }
   }

   public final void clear() {
   }
}
