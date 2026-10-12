import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class PageTextWidget extends PageWidget {
   private int fontStyle;
   private int fontSize;
   private int color;
   private String text;

   public PageTextWidget(String var1, int var2, int var3, boolean var4, int var5) {
      super((byte)0, var2, var4, var5);
      this.text = var1;
   }

   public final void setStyle(int var1, int var2, int var3) {
      this.color = var1;
      this.fontStyle = var2;
      this.fontSize = var3;
      Font var4 = Font.getFont(0, this.fontStyle, this.fontSize);
      super.w = vh.stringWidth(var4, this.text);
      super.h = vh.getHeight(var4);
   }

   private void applyStyle(Graphics var1) {
      if (this.color != -16777216) {
         var1.setColor(this.color);
      }

      if (this.fontSize != 8 || this.fontStyle != 0) {
         var1.setFont(Font.getFont(0, this.fontStyle, this.fontSize));
      }
   }

   public final void draw(Graphics var1) {
      if (this.text != null && this.text.length() != 0) {
         super.drawX = super.x;
         super.drawY = super.y + PageWidget.drawOffsetY;
         if (!super.isLink) {
            var1.setColor(PageWidget.textColor);
            this.applyStyle(var1);
            vh.drawString(var1, this.text, super.drawX, super.drawY, 20);
         } else if (super.linkIndex == PortalBrowser.focusedLinkIndex) {
            var1.setColor(PageWidget.linkFocusColor);
            var1.drawLine(super.drawX, super.drawY + super.h, super.drawX + super.w, super.drawY + super.h);
            var1.setColor(6177536);
            vh.drawString(var1, this.text, super.drawX - 1, super.drawY - 1, 20);
            vh.drawString(var1, this.text, super.drawX + 1, super.drawY + 1, 20);
            var1.setColor(PageWidget.linkFocusColor);
            this.applyStyle(var1);
            vh.drawString(var1, this.text, super.drawX, super.drawY, 20);
         } else {
            var1.setColor(PageWidget.linkColor);
            var1.drawLine(super.drawX, super.drawY + super.h, super.drawX + super.w, super.drawY + super.h);
            var1.setColor(PageWidget.linkColor);
            this.applyStyle(var1);
            vh.drawString(var1, this.text, super.drawX, super.drawY, 20);
         }
      }
   }
}
