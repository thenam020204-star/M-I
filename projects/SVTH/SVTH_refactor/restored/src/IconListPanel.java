import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class IconListPanel extends ListPanel {
   private boolean showSelection = false;

   public IconListPanel(boolean var1) {
      this.showSelection = true;
      this.items = null;
      this.items = new Vector();
   }

   public final void addIcon(Image var1) {
      this.items.addElement(var1);
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void draw(Graphics var1) {
      var1.setClip(this.x, this.y, this.innerW, this.h);
      int var2 = this.y + this.rowHeight / 2;
      int var3 = this.x + this.padX;
      int var4;
      if ((var4 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var4 = this.items.size();
      }

      for (int var5 = this.scrollTop; var5 < var4; var5++) {
         Image var6 = (Image)this.items.elementAt(var5);
         var1.drawImage(var6, var3 + (var6.getWidth() >> 1), var2, 3);
         if (var5 == this.selectedIndex && this.showSelection) {
            var1.setColor(12842492);
            var1.drawRect(var3 - 1, var2 - (var6.getHeight() + 2) / 2, var6.getWidth() + 2, var6.getHeight() + 2);
            var1.setColor(541060);
            var1.drawRect(var3 - 2, var2 - 1 - (var6.getHeight() + 2) / 2, var6.getWidth() + 4, var6.getHeight() + 4);
         } else {
            var1.setColor(15589360);
            var1.drawRect(var3 - 1, var2 - (var6.getHeight() + 2) / 2, var6.getWidth() + 2, var6.getHeight() + 2);
            var1.setColor(6898011);
            var1.drawRect(var3 - 2, var2 - 1 - (var6.getHeight() + 2) / 2, var6.getWidth() + 4, var6.getHeight() + 4);
         }

         var2 += this.rowHeight;
      }

      var1.setClip(0, 0, GameCanvas.instance.getWidth(), GameCanvas.instance.getHeight());
   }

   public final void clear() {
      if (this.items != null) {
         this.items.removeAllElements();
      }
   }
}
