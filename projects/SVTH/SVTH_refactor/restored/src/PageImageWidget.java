import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class PageImageWidget extends PageWidget {
   private int imageId;
   private int align;
   private int valign;

   public PageImageWidget(int var1, int var2, boolean var3, int var4) {
      super((byte)5, var1, var3, var4);
   }

   public final void setImage(int var1, int var2, int var3) {
      this.imageId = var1;
      this.align = var2;
      this.valign = var3;
   }

   public final void draw(Graphics var1) {
      Image var2;
      if (this.imageId > 0 && this.imageId <= PortalCanvasBase.pageImages.size()) {
         var2 = (Image)PortalCanvasBase.pageImages.elementAt(this.imageId - 1);
      } else {
         var2 = PortalBrowser.errorImage;
      }

      super.drawX = super.x;
      super.drawY = super.y;
      switch (this.align) {
         case 2:
            super.drawX = PortalCanvasBase.screenWidth - super.w >> 1;
            break;
         case 3:
            super.drawX = PortalCanvasBase.screenWidth - super.w - 5;
      }

      int var3 = super.h;
      if (super.lineIndex < PortalBrowser.lineYs.size() - 1) {
         var3 = PortalBrowser.lineYs.get(super.lineIndex + 1) - PortalBrowser.lineYs.get(super.lineIndex);
      }

      switch (this.valign) {
         case 2:
            super.drawY = super.drawY + (var3 - super.h >> 1);
            break;
         case 3:
            super.drawY = super.drawY + (var3 - super.h);
      }

      super.drawX = super.drawX;
      super.drawY = super.drawY + PageWidget.drawOffsetY;
      if (super.isLink) {
         var1.setColor(PageWidget.imageFocusColor);
         if (super.linkIndex == PortalBrowser.focusedLinkIndex) {
            var1.fillRect(super.drawX - 1, super.drawY - 1, super.w + 2, super.h + 2);
         } else {
            var1.drawLine(super.drawX, super.drawY + super.h + 1, super.drawX + super.w, super.drawY + super.h + 1);
         }
      }

      if (var2 != null) {
         var1.drawImage(var2, super.drawX, super.drawY, 20);
      } else {
         var1.drawRect(super.drawX, super.drawY, super.w, super.h);
      }
   }
}
