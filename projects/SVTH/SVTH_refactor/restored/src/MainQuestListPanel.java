import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class MainQuestListPanel extends ListPanel {
   Image cursorImg;
   private int cursorBob;

   public MainQuestListPanel() {
      new Vector();
      this.items = null;
      this.items = new Vector();
   }

   public final String getSelectedName() {
      return ((MainQuestEntry)this.items.elementAt(this.selectedIndex)).getName();
   }

   public final int getSelectedStatus() {
      return ((MainQuestEntry)this.items.elementAt(this.selectedIndex)).getStatus();
   }

   public final void addQuest(int var1, int var2, String var3) {
      this.items.addElement(new MainQuestEntry(var1, var2, var3));
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
            var1,
            null,
            null,
            this.x + this.innerW - 10,
            this.y + (this.padY << 1),
            this.h - (this.padY << 2),
            this.items.size(),
            this.visibleRows,
            this.scrollTop
         );
      }

      for (int var5 = this.scrollTop; var5 < var4; var5++) {
         MainQuestEntry var2 = (MainQuestEntry)this.items.elementAt(var5);
         if (var5 == this.selectedIndex) {
            if (this.cursorImg != null) {
               var1.drawRegion(this.cursorImg, 0, 0, 15, 15, 0, this.x + 15 + this.cursorBob, var3 + (this.padY << 2) + 4, 3);
            }

            var1.setColor(9240568);
         } else {
            var1.setColor(16777215);
         }

         if (var2.getStatus() == 1) {
            var1.setColor(11184810);
         }

         vh.drawString(var1, var2.getName(), this.x + this.w / 2, var3, 17);
         var3 += this.rowHeight;
      }
   }

   public final void clear() {
      if (this.items != null) {
         this.items.removeAllElements();
         this.items = null;
      }
   }
}
