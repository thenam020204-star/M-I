import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class DexListPanel extends ListPanel {
   private int page = 1;
   private int pageCount;
   private int cursorBob = 0;
   private Image cursorImg;
   private Image digitsImg;

   public DexListPanel() {
      this.items = new Vector();
   }

   public final void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
      this.visibleRows = var4 / this.rowHeight;
      this.innerW = 0;
      this.innerW = this.innerW + (var3 - 2 * this.padX);
      this.selectedIndex = 0;
   }

   public final void updatePageCount() {
      if (this.items.size() > 0) {
         if (this.items.size() % this.visibleRows == 0) {
            this.pageCount = this.items.size() / this.visibleRows;
            return;
         }

         this.pageCount = this.items.size() / this.visibleRows + 1;
      }
   }

   private int absoluteIndex() {
      return this.selectedIndex + (this.page - 1) * this.visibleRows;
   }

   public final int getSelectedPetId() {
      return ((DexEntry)this.items.elementAt(this.absoluteIndex())).petId;
   }

   public final String getSelectedName() {
      return ((DexEntry)this.items.elementAt(this.absoluteIndex())).name;
   }

   public final void nextPage() {
      if (this.page < this.pageCount) {
         this.page++;
         this.selectedIndex = 0;
      }
   }

   public final void prevPage() {
      if (this.page > 1) {
         this.page--;
         this.selectedIndex = 0;
      }
   }

   public final void selectNext() {
      if (this.selectedIndex < this.visibleRows - 1 && this.selectedIndex + (this.page - 1) * this.visibleRows < this.items.size() - 1) {
         this.selectedIndex++;
      }
   }

   public final void selectPrev() {
      if (this.selectedIndex > 0) {
         this.selectedIndex--;
      }
   }

   public final void addEntry(int var1, String var2) {
      this.items.addElement(new DexEntry(var1, var2));
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void setImages(Image var1, Image var2) {
      this.cursorImg = var1;
      this.digitsImg = var2;
   }

   public final void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      int var3 = this.y + this.padY;
      int var4 = this.x;
      int var2;
      if ((var2 = this.page * this.visibleRows) > this.items.size()) {
         this.items.size();
      }

      if (this.cursorImg != null) {
         var1.drawRegion(this.cursorImg, 0, 0, 15, 15, 0, var4 + 5 + this.cursorBob, var3 + this.selectedIndex * this.rowHeight, 17);
      }

      for (int var5 = 0; var5 < this.visibleRows; var5++) {
         if ((this.page - 1) * this.visibleRows + var5 < this.items.size()) {
            DexEntry var8 = (DexEntry)this.items.elementAt((this.page - 1) * this.visibleRows + var5);
            String var6 = "#  ";
            String var7;
            if ((var7 = String.valueOf(var8.petId)).length() == 1) {
               var7 = "00" + var7;
            } else if (var7.length() == 2) {
               var7 = "0" + var7;
            }

            if (var5 == this.selectedIndex) {
               Utils.drawOutlinedText(var1, var6 + var7, var4 + Utils.fontWidthMM, var3, 16711680, 16777215, 20);
               Utils.drawOutlinedText(var1, var8.name, var4 + this.w * 3 / 4, var3, 16711680, 16777215, 17);
            } else {
               Utils.drawOutlinedText(var1, var6 + var7, var4 + Utils.fontWidthMM, var3, 15987162, 5971280, 20);
               Utils.drawOutlinedText(var1, var8.name, var4 + this.w * 3 / 4, var3, 15987162, 5971280, 17);
            }

            var3 += this.rowHeight;
         }
      }

      var1.drawRegion(this.digitsImg, 60, 0, 6, 7, 0, var4 + this.w / 2, this.y + this.visibleRows * this.rowHeight, 33);
      Utils.drawNumber(var1, this.page, var4 + this.w / 2 - 6, this.y + this.visibleRows * this.rowHeight, this.digitsImg, 6, 7, 40);
      Utils.drawNumber(var1, this.pageCount, var4 + this.w / 2 + 6, this.y + this.visibleRows * this.rowHeight, this.digitsImg, 6, 7, 36);
   }

   public final void clear() {
      this.items.removeAllElements();
   }
}
