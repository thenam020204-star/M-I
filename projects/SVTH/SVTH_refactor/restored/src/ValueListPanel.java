import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class ValueListPanel extends ListPanel {
   private byte style = 0;
   private int digitFrames;
   private Image digitsImg;
   boolean showTimesPrefix;

   public ValueListPanel() {
      this.items = null;
      this.items = new Vector();
   }

   public ValueListPanel(byte var1, int var2, Image var3) {
      this.digitsImg = var3;
      this.digitFrames = 10;
      this.style = 1;
      this.items = null;
      this.items = new Vector();
   }

   public final void addText(String var1) {
      this.items.addElement(var1);
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void draw(Graphics var1) {
      int var2 = this.y + this.padY;
      int var3 = this.x + this.padX;
      int var4;
      if ((var4 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var4 = this.items.size();
      }

      for (int var5 = this.scrollTop; var5 < var4; var5++) {
         if (this.style == 0) {
            var1.setColor(16777215);
            if (this.showTimesPrefix) {
               vh.drawString(var1, "x  ", var3, var2 + 2, 24);
            }

            vh.drawString(var1, (String)this.items.elementAt(var5), var3, var2 + 2, 20);
         } else if (this.style == 1) {
            int var6 = Integer.parseInt((String)this.items.elementAt(var5));
            Utils.drawNumber(
               var1, var6, var3, var2 - 2 + this.rowHeight / 2, this.digitsImg, this.digitsImg.getWidth() / this.digitFrames, this.digitsImg.getHeight(), 3
            );
         }

         var2 += this.rowHeight;
      }
   }

   public final void clear() {
   }
}
