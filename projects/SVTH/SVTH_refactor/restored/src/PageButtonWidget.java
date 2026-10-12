import javax.microedition.lcdui.Graphics;

public final class PageButtonWidget extends PageWidget {
   private String label;

   public PageButtonWidget(int var1, int var2, int var3) {
      super((byte)2, var1, true, var3);
   }

   public final void setLabel(String var1) {
      this.label = var1;
   }

   public final void draw(Graphics var1) {
      super.drawX = super.x;
      super.drawY = super.y + PageWidget.drawOffsetY;
      if (super.linkIndex == PortalBrowser.focusedLinkIndex) {
         var1.setColor(80737);
         var1.drawRoundRect(super.drawX, super.drawY, super.w, super.h, 3, 3);
         var1.setColor(PageWidget.buttonFocusTextColor);
      } else {
         var1.setColor(80737);
         var1.drawRoundRect(super.drawX, super.drawY, super.w, super.h, 3, 3);
         var1.setColor(PageWidget.buttonTextColor);
      }

      if (this.label != null) {
         vh.drawString(var1, this.label, super.drawX + (super.w >> 1), super.drawY + 1, 17);
      }
   }
}
