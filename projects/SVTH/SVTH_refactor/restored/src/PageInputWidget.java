import javax.microedition.lcdui.Graphics;

public final class PageInputWidget extends PageWidget {
   private MultiTapInput keypad;
   public int maxLength;
   public int constraints = 0;
   public int inputMode;
   private int textWidth;
   private String prompt;
   private String fieldName;
   public boolean isEditing;
   private String displayText;
   private String value;
   private static int charWidth;

   public PageInputWidget(int var1, int var2, int var3, int var4, int var5, int var6, PortalClient var7) {
      super((byte)1, var1, true, var3);
      this.inputMode = var6;
      this.maxLength = var5;
      this.isEditing = false;
      this.displayText = "";
      this.value = "";
      charWidth = vh.charWidth(PortalCanvasBase.font, 'a');
      this.textWidth = 0;
      this.constraints = var4;
      if (var6 == 0) {
         this.keypad = new MultiTapInput(var4, var5, var7);
      }
   }

   public final void setPromptAndName(String var1, String var2) {
      this.prompt = var1;
      this.fieldName = var2;
   }

   public final void draw(Graphics var1) {
      super.drawX = super.x;
      super.drawY = super.y + PageWidget.drawOffsetY;
      if (super.linkIndex == PortalBrowser.focusedLinkIndex) {
         if (this.isEditing) {
            var1.setColor(16777215);
            var1.fillRect(super.drawX, super.drawY, super.w, super.h);
            var1.setColor(PageWidget.inputFocusBorderColor);
            var1.drawRect(super.drawX, super.drawY, super.w - 1, super.h - 1);
            var1.setColor(167930);
            var1.drawRect(super.drawX + 1, super.drawY + 1, super.w - 3, super.h - 3);
            var1.setColor(PageWidget.inputFocusTextColor);
         } else {
            var1.setColor(PageWidget.inputFocusBgColor);
            var1.fillRect(super.drawX, super.drawY, super.w, super.h);
            var1.setColor(PageWidget.inputFocusBorderColor);
            var1.drawRect(super.drawX, super.drawY, super.w - 1, super.h - 1);
            var1.setColor(167930);
            var1.drawRect(super.drawX + 1, super.drawY + 1, super.w - 3, super.h - 3);
            var1.setColor(PageWidget.inputFocusTextColor);
         }
      } else {
         var1.setColor(PageWidget.inputBgColor);
         var1.fillRect(super.drawX, super.drawY, super.w, super.h);
         var1.setColor(PageWidget.inputBorderColor);
         var1.drawRect(super.drawX, super.drawY, super.w - 1, super.h - 1);
         var1.setColor(PageWidget.inputTextColor);
      }

      if (this.displayText.length() > 0) {
         if (this.constraints != 65536 && vh.stringWidth(PortalCanvasBase.font, this.displayText) >= super.w - 6 - charWidth) {
            var1.setClip(super.drawX + 2, super.drawY, super.w - 4, super.h);
            vh.drawString(var1, this.displayText, super.drawX + super.w - 6 - charWidth, super.drawY + 3, 24);
            var1.setClip(0, 0, PortalCanvasBase.screenWidth, PortalCanvasBase.screenHeight);
            return;
         }

         vh.drawString(var1, this.displayText, super.drawX + 3, super.drawY + 3, 20);
      }
   }

   public final void drawEditor(Graphics var1) {
      this.keypad.tick();
      if (vh.stringWidth(PortalCanvasBase.font, this.displayText) < super.w - 6 - charWidth) {
         this.keypad.drawCursor(var1, super.drawX + this.textWidth + 3, super.drawY + 3, super.w);
      } else {
         this.keypad.drawCursor(var1, super.drawX + super.w - 6 - charWidth, super.drawY + 3, super.w);
      }

      this.keypad.drawSymbolPicker(var1);
      if (this.keypad.getCommitTimer() == 1) {
         this.setText(this.keypad.getText());
      }
   }

   public final void handleKey(int var1, int var2) {
      if (this.inputMode == 0 && this.isEditing) {
         this.keypad.handleKey(var1, var2);
         this.setText(this.keypad.getText());
      }
   }

   public final void setText(String var1) {
      this.value = var1;
      if (this.constraints != 65536) {
         this.displayText = var1;
         this.textWidth = vh.stringWidth(PortalCanvasBase.font, this.displayText);
      } else {
         if (this.textWidth < super.w - 6) {
            this.displayText = PortalTextUtil.maskString(var1.length());
            this.textWidth = vh.stringWidth(PortalCanvasBase.font, this.displayText);
         }
      }
   }

   public final String getValue() {
      return this.constraints == 65536 ? Md5.hexDigest(this.value.getBytes()) : this.value;
   }

   public final boolean isSymbolPickerOpen() {
      return this.inputMode == 0 ? this.keypad.isSymbolPickerOpen() : false;
   }

   public final String getPrompt() {
      return this.prompt;
   }

   public final String getFieldName() {
      return this.fieldName;
   }
}
