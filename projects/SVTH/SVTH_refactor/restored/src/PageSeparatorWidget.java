import javax.microedition.lcdui.Graphics;

public final class PageSeparatorWidget extends PageWidget {
   private String patternText;
   private int rowHeight;
   private int lineThickness;
   private int color;

   public PageSeparatorWidget(int var1, int var2, boolean var3, int var4) {
      super((byte)8, var1, var3, var4);
   }

   public final void setup(String var1, int var2, int var3, int var4) {
      if (var1 != null) {
         this.patternText = "";
         this.lineThickness = PortalCanvasBase.lineHeight;
         int var5 = vh.stringWidth(PortalCanvasBase.font, var1);
         var5 = (PortalCanvasBase.screenWidth - (var2 << 1)) / var5;

         for (int var6 = 0; var6 < var5; var6++) {
            this.patternText = this.patternText + var1;
         }
      } else {
         this.lineThickness = 2;
      }

      super.x = var2;
      this.rowHeight = var3;
      this.color = var4;
   }

   public final void draw(Graphics var1) {
      super.drawX = super.x;
      super.drawY = super.y + PageWidget.drawOffsetY;
      super.drawY = super.drawY + (this.rowHeight - this.lineThickness >> 1);
      if (this.patternText != null) {
         if (this.color != -16777216) {
            var1.setColor(this.color);
         } else {
            var1.setColor(PageWidget.textColor);
         }

         vh.drawString(var1, this.patternText, super.drawX, super.drawY, 20);
      } else {
         var1.setColor(0);
         var1.drawLine(super.drawX, super.drawY, PortalCanvasBase.screenWidth - super.drawX, super.drawY);
         var1.setColor(3568808);
         var1.drawLine(super.drawX, super.drawY + 1, PortalCanvasBase.screenWidth - super.drawX, super.drawY + 1);
      }
   }
}
