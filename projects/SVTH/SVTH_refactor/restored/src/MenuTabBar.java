import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class MenuTabBar extends ListPanel {
   private Image barBg;
   private Image tabIcons;
   private Image tabTitles;
   private Image tabUnderline;
   private Image arrowsImg;
   boolean isSelectMode = false;
   boolean hideArrows = false;
   private Image selectTitleImg;
   private int cursorBob = 0;
   private String[] tabNames = new String[]{"Trạng thái", "Sủng vật", "Kỹ năng", "Trang bị", "Túi đồ", "Nhiệm vụ", "Đồ giám", "Hệ thống"};

   public MenuTabBar() {
      this.arrowsImg = ResourceCache.loadImage("/pic/D04.gf");
      this.items = new Vector();
      this.barBg = ResourceCache.loadImage("/pic/D03.gf");
      String var10001 = "/pic/D46.gf";
      this.selectTitleImg = null;
      this.tabIcons = ResourceCache.loadImage("/pic/xttb.gf");
      this.tabTitles = ResourceCache.loadImage("/pic/D12.gf");
      this.tabUnderline = ResourceCache.loadImage("/pic/tbbj.gf");
   }

   public final void setSelectMode(boolean var1) {
      this.isSelectMode = true;
   }

   public final void setLayout(int var1, int var2, int var3, int var4, int var5) {
      this.x = var1;
      this.y = var2;
      this.w = 193;
      this.h = 28;
      this.rowHeight = 28;
      this.innerW = 27;
      this.visibleRows = 5;
   }

   public final void addTab(int var1, int var2) {
      this.items.addElement(new MenuTab(var1, var2));
      if (this.selectedIndex < 0) {
         this.selectedIndex = 0;
      }
   }

   public final void setSelectedIndex(int var1) {
      if (var1 >= this.items.size()) {
         throw new IndexOutOfBoundsException();
      } else {
         this.selectedIndex = var1;
      }
   }

   public final void selectNext() {
      if (this.selectedIndex < this.items.size() - 1) {
         this.selectedIndex++;
         if (this.selectedIndex >= this.scrollTop + this.visibleRows) {
            this.scrollTop++;
            return;
         }
      } else {
         this.scrollTop = 0;
         this.selectedIndex = 0;
      }
   }

   public final void selectPrev() {
      if (this.selectedIndex > 0) {
         this.selectedIndex--;
         if (this.selectedIndex < this.scrollTop) {
            this.scrollTop = this.selectedIndex;
            return;
         }
      } else {
         this.selectedIndex = this.items.size() - 1;
         this.scrollTop = this.items.size() - this.visibleRows;
      }
   }

   public final void draw(Graphics var1) {
      if (GameCanvas.instance.frameCounter % 12 == 0) {
         this.cursorBob = 3;
      } else {
         this.cursorBob = 0;
      }

      int var4;
      if ((var4 = this.scrollTop + this.visibleRows) > this.items.size()) {
         var4 = this.items.size();
      }

      int var3 = this.x;
      if (this.barBg != null) {
         var1.drawImage(this.barBg, var3, this.y, 20);
      } else {
         var1.setColor(5924969);
         var1.fillRect(this.x + 1, this.y + 1, this.w - 2, 15);
         var1.setColor(1911597);
         var1.fillRect(this.x + 1, this.y + 15, this.w - 2, 15);
         var1.setColor(3027509);
         var1.drawRoundRect(this.x, this.y + 1, this.w, 29, 4, 4);
         var1.setColor(12371653);
         var1.drawRoundRect(this.x + 1, this.y + 2, this.w - 2, 28, 4, 4);
      }

      if (!this.isSelectMode) {
         if (!this.hideArrows) {
            Utils.drawFrame(
               var1, this.arrowsImg, this.arrowsImg.getWidth() / 2, this.arrowsImg.getHeight(), 0, var3 + 5 - this.cursorBob, this.y + (this.h >> 1), 3, 0
            );
            Utils.drawFrame(
               var1,
               this.arrowsImg,
               this.arrowsImg.getWidth() / 2,
               this.arrowsImg.getHeight(),
               1,
               var3 - 5 + this.cursorBob + this.w,
               this.y + (this.h >> 1),
               3,
               0
            );
         }

         var3 += 54;

         for (int var5 = this.scrollTop; var5 < var4; var5++) {
            MenuTab var2 = (MenuTab)this.items.elementAt(var5);
            if (var5 == this.selectedIndex) {
               if (this.tabTitles != null) {
                  Utils.drawFrame(
                     var1,
                     this.tabTitles,
                     this.tabTitles.getWidth(),
                     this.tabTitles.getHeight() / 8,
                     var2.tabId,
                     this.x + 5 + (this.padX << 1),
                     this.y + 5,
                     20,
                     0
                  );
               } else {
                  Utils.drawOutlinedText(var1, this.tabNames[var2.tabId], this.x + 15 + (this.padX << 1), this.y + 5, 16308570, 203551, 20);
               }

               Utils.drawFrame(var1, this.tabIcons, this.tabIcons.getWidth() / 8, this.tabIcons.getHeight(), var2.iconFrame, var3, this.y - 4, 20, 0);
               var1.drawImage(this.tabUnderline, var3, this.y + 29, 36);
            } else {
               Utils.drawFrame(var1, this.tabIcons, this.tabIcons.getWidth() / 8, this.tabIcons.getHeight(), var2.iconFrame, var3, this.y, 20, 0);
            }

            var3 += this.innerW;
         }
      } else if (this.selectTitleImg != null) {
         var1.drawImage(this.selectTitleImg, this.x + this.w / 2, this.y + 15, 3);
      } else {
         Utils.drawOutlinedText(var1, "Chọn", this.x + this.w / 2, this.y + 15 - Utils.lineHeight / 2, 16308570, 203551, 17);
      }
   }

   public final void clear() {
   }
}
