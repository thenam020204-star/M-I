import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public abstract class ListPanel {
   protected int x;
   protected int y;
   protected int w;
   protected int h;
   protected int innerW = 0;
   protected int padX = 4;
   protected int selectedIndex;
   protected int scrollTop = 0;
   protected int visibleRows = 0;
   protected int rowHeight = Utils.lineHeight + 4;
   protected int padY = 2;
   Vector items;

   public ListPanel() {
   }

   public abstract void draw(Graphics var1);

   public final int itemCount() {
      return this.items != null ? this.items.size() : 0;
   }

   public abstract void clear();

   public void setSelectedIndex(int var1) {
      if (var1 >= this.items.size()) {
         throw new IndexOutOfBoundsException();
      } else {
         this.selectedIndex = var1;
      }
   }

   public void selectNext() {
      if (this.selectedIndex < this.items.size() - 1) {
         this.selectedIndex++;
         if (this.selectedIndex >= this.scrollTop + this.visibleRows) {
            this.scrollTop++;
         }
      }
   }

   public void selectPrev() {
      if (this.selectedIndex > 0) {
         this.selectedIndex--;
         if (this.selectedIndex < this.scrollTop) {
            this.scrollTop = this.selectedIndex;
         }
      }
   }

   public final void resetScroll(int var1) {
      this.scrollTop = 0;
   }

   public final int[] getBounds() {
      int[] var1;
      (var1 = new int[4])[0] = this.x;
      var1[1] = this.y;
      var1[2] = this.w;
      var1[3] = this.h;
      return var1;
   }

   public final int getSelectedIndex() {
      return this.selectedIndex;
   }

   public final void setRowHeight(int var1) {
      this.rowHeight = var1;
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
      this.visibleRows = var4 / this.rowHeight;
      this.innerW = 0;
      this.innerW = this.innerW + (var3 - 2 * this.padX);
   }
}
