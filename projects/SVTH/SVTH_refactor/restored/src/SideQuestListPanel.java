import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class SideQuestListPanel extends ListPanel {
   Image cursorImg;
   private Image digitsImg;
   private Image doneMarkImg;
   private int cursorBob;

   public SideQuestListPanel() {
      new Vector();
      this.items = null;
      this.items = new Vector();
      if (this.digitsImg == null) {
         this.digitsImg = ResourceCache.loadImage("/pic/number01.gf", 2);
      }

      if (this.doneMarkImg == null) {
         this.doneMarkImg = ResourceCache.loadImage("/pic/D15.gf");
      }
   }

   public final String getSelectedName() {
      return ((SideQuestEntry)this.items.elementAt(this.selectedIndex)).name;
   }

   public final int getSelectedQuestIndex() {
      return ((SideQuestEntry)this.items.elementAt(this.selectedIndex)).questIndex;
   }

   public final void addQuest(int var1, int var2, String var3, boolean var4) {
      this.items.addElement(new SideQuestEntry(var1, var2, var3, var4));
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
         int var6 = this.x + this.padX;
         SideQuestEntry var2 = (SideQuestEntry)this.items.elementAt(var5);
         if (this.digitsImg != null) {
            var6 += drawQuestTypeTag(var1, var2.type, var6, var3);
         }

         if (var5 == this.selectedIndex) {
            if (this.cursorImg != null) {
               var1.drawRegion(this.cursorImg, 0, 0, 15, 15, 0, this.x + this.cursorBob, var3 + (this.padY << 2) + 4, 3);
            }

            var1.setColor(9240568);
         } else {
            var1.setColor(16777215);
         }

         vh.drawString(var1, var2.name, var6, var3, 20);
         if (var2.isDone) {
            Utils.drawFrame(var1, this.doneMarkImg, this.doneMarkImg.getWidth() / 2, this.doneMarkImg.getHeight(), 0, this.x + this.w - 18, var3, 17, 0);
         } else {
            Utils.drawFrame(var1, this.doneMarkImg, this.doneMarkImg.getWidth() / 2, this.doneMarkImg.getHeight(), 1, this.x + this.w - 18, var3, 17, 0);
         }

         var3 += this.rowHeight;
      }
   }

   public static int drawQuestTypeTag(Graphics var0, int var1, int var2, int var3) {
      Utils.drawOutlinedText8(var0, "[", var2, var3, 15987164, 7546142, 17);
      Utils.drawOutlinedText8(var0, "]", var2 + (Utils.fontWidthMM + 4 << 1), var3, 15987164, 7546142, 17);
      if (var1 == 3) {
         Utils.drawOutlinedText8(var0, "Công hội", var2 + 4 + Utils.fontWidthMM, var3, 9633535, 409514, 17);
      } else {
         Utils.drawOutlinedText8(var0, "Thường", var2 + 4 + Utils.fontWidthMM, var3, 5439068, 608022, 17);
      }

      return 20 + 2 * Utils.fontWidthMM;
   }

   public final void clear() {
      this.items.removeAllElements();
      this.items = null;
   }
}
