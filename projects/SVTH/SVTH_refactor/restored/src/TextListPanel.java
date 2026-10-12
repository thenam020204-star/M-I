import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class TextListPanel extends ListPanel {
   private byte style = 0;
   private int[] colors = new int[2];
   private int[][] outlineColors = new int[2][2];

   public TextListPanel() {
      this.items = null;
      this.items = new Vector();
      this.colors[0] = 16711680;
      this.colors[1] = 0;
   }

   public TextListPanel(byte var1, int[][] var2) {
      this.outlineColors = var2;
      this.items = null;
      this.items = new Vector();
      this.style = 1;
   }

   public final void addText(String var1) {
      this.items.addElement(var1);
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void draw(Graphics var1) {
      int var2 = this.y + this.padY + 2;
      int var3 = this.x + this.padX;
      int var4;
      if ((var4 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var4 = this.items.size();
      }

      for (int var5 = this.scrollTop; var5 < var4; var5++) {
         if (this.style == 0) {
            if (var5 == this.selectedIndex) {
               var1.setColor(this.colors[0]);
            } else {
               var1.setColor(this.colors[1]);
            }

            vh.drawString(var1, (String)this.items.elementAt(var5), var3 + this.innerW / 2, var2, 17);
         } else if (this.style == 1) {
            if (var5 == this.selectedIndex) {
               Utils.drawOutlinedText8(
                  var1, (String)this.items.elementAt(var5), var3 + this.innerW / 2, var2, this.outlineColors[0][0], this.outlineColors[0][1], 17
               );
            } else if (this.outlineColors[1][0] == this.outlineColors[1][1]) {
               var1.setColor(this.outlineColors[1][0]);
               vh.drawString(var1, (String)this.items.elementAt(var5), var3 + this.innerW / 2, var2, 17);
            } else {
               Utils.drawOutlinedText8(
                  var1, (String)this.items.elementAt(var5), var3 + this.innerW / 2, var2, this.outlineColors[1][0], this.outlineColors[1][1], 17
               );
            }
         }

         var2 += this.rowHeight;
      }
   }

   public final void clear() {
      this.items.removeAllElements();
      this.items = null;
   }
}
