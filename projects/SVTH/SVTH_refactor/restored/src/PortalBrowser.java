import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextBox;

public final class PortalBrowser extends PortalCanvasBase {
   public static int focusedLinkIndex;
   private int focusedWidgetIndex;
   private int menuIndex;
   private int scrollBarW;
   private int scrollBarH;
   private int scrollBarX;
   private int scrollBarY;
   private int thumbW;
   private int thumbH;
   private int scrollY;
   private int cursorY;
   private int lineMaxHeight;
   public int contentWidth;
   private int viewHeight;
   private int currentLine;
   private int firstVisibleLine;
   private int lastVisibleLine;
   public int cursorX;
   private int historyIndex;
   private boolean showScrollBar;
   private boolean isEditing;
   private int activeInputIndex;
   public Vector widgets;
   private Vector links;
   private Vector history;
   public PageRequest currentRequest;
   public static IntList lineYs;
   public static Image errorImage;
   private boolean hasLeftButton;
   private boolean hasRightButton;
   private PageRequest leftButtonRequest;
   private PageRequest rightButtonRequest;
   private String[] menuItems = new String[]{"1.Trang chủ", "2.Làm mới", "3.Tiến", "4.Trợ giúp", "5.Đăng xuất", "6.Về trò chơi"};
   private int menuBorderColor = 1991325;
   private int menuBgColor = 731458;
   private int menuTextColor = 8829158;
   private int menuSelTextColor = 16777215;
   private boolean isLoggedIn;

   public PortalBrowser(PortalClient var1) {
      super(var1);
      super.isEmbeddedMode = true;
   }

   public final void drawContent(Graphics var1) {
      var1.setFont(PortalCanvasBase.font);
      this.drawBar(var1, 0, PortalCanvasBase.screenWidth, super.headerHeight);
      if (this.widgets.size() > 0) {
         var1.setClip(0, super.headerHeight, PortalCanvasBase.screenWidth, this.viewHeight);

         for (int var3 = 0; var3 < this.widgets.size(); var3++) {
            ((PageWidget)this.widgets.elementAt(var3)).draw(var1);
         }

         PageInputWidget var10;
         if (this.isInputFocused() && (var10 = (PageInputWidget)this.widgets.elementAt(this.activeInputIndex)).isEditing && var10.inputMode == 0) {
            var10.drawEditor(var1);
         }

         var1.setClip(0, 0, PortalCanvasBase.screenWidth, PortalCanvasBase.screenHeight);
      }

      if (var1.getFont() != PortalCanvasBase.font) {
         var1.setFont(PortalCanvasBase.font);
      }

      if (this.showScrollBar) {
         var1.setClip(this.scrollBarX, this.scrollBarY, this.scrollBarW, this.scrollBarH);
         int var4 = this.scrollY * 10000 / (this.cursorY - this.viewHeight) * (this.scrollBarH - this.thumbH) / 10000;
         var1.setColor(464686);
         var1.fillRect(this.scrollBarX, this.scrollBarY, this.scrollBarW, this.scrollBarH);
         var1.setColor(1657987);
         var1.drawRect(this.scrollBarX, this.scrollBarY, this.scrollBarW, this.scrollBarH);
         var1.setColor(1587307);
         var1.fillRect(this.scrollBarX + 2, this.scrollBarY + 1 + var4, this.thumbW, this.thumbH);
         var1.setClip(0, 0, PortalCanvasBase.screenWidth, PortalCanvasBase.screenHeight);
      }

      Graphics var11 = var1;
      PortalBrowser var2 = this;
      if (super.isMenuOpen) {
         int var8 = PortalCanvasBase.lineHeight * this.menuItems.length + 6;
         int var12 = PortalCanvasBase.screenHeight - super.footerHeight - var8;
         int var5;
         int var6 = (var5 = vh.stringWidth(PortalCanvasBase.font, this.menuItems[0])) + 20;
         var1.setColor(this.menuBgColor);
         var1.fillRect(1, var12, var6, var8);
         var1.setColor(534079);
         var1.drawRect(1, var12, var6 - 1, var8 - 1);
         var1.setColor(this.menuBorderColor);
         var1.drawRect(2, var12 + 1, var6 - 3, var8 - 3);
         var1.setColor(1991325);
         var1.drawRect(2, var12 + 1, var6 - 3, var8 - 3);

         for (int var9 = 0; var9 < var2.menuItems.length; var9++) {
            if (var9 == var2.menuIndex) {
               PortalCanvasBase.fillGradient(
                  var11, 4, var12 + 2 + PortalCanvasBase.lineHeight * var9, var6 - 7, PortalCanvasBase.lineHeight, var2.highlightGradient
               );
               var11.setColor(var2.menuSelTextColor);
            } else {
               var11.setColor(var2.menuTextColor);
            }

            if (var9 == 2 && var2.historyIndex >= var2.history.size() - 1) {
               var11.setColor(9803157);
            }

            vh.drawString(var11, var2.menuItems[var9], 1 + (var6 - var5 >> 1), var12 + 3 + PortalCanvasBase.lineHeight * var9, 20);
         }
      }
   }

   public final void showMessagePage(String var1) {
      if (var1 == null) {
         var1 = "Không có dữ liệu";
      }

      int var3 = (PortalCanvasBase.screenHeight - super.footerHeight) / PortalCanvasBase.lineHeight >> 1;
      int var2 = vh.stringWidth(PortalCanvasBase.font, var1);
      int var4 = 8;
      if (var2 > PortalCanvasBase.screenWidth) {
         int var5 = var2 / PortalCanvasBase.screenWidth + 1;
         if ((var3 -= var5 / 2) < 0) {
            var3 = 0;
         }
      } else {
         var4 = PortalCanvasBase.screenWidth - var2 - 10 >> 1;
      }

      if (var4 < 0) {
         var4 = 0;
      }

      Hashtable[] var6 = new Hashtable[var3 + 1];

      for (int var7 = 0; var7 < var3; var7++) {
         var6[var7] = new Hashtable();
         var6[var7].put("type", PageWidget.TAG_NAMES[4]);
      }

      var6[var6.length - 1] = new Hashtable();
      var6[var6.length - 1].put("type", PageWidget.TAG_NAMES[0]);
      var6[var6.length - 1].put("value", var1);
      var6[var6.length - 1].put("space", String.valueOf(var4));
      super.manager.renderPage(var6);
   }

   public final PageInputWidget[] findInputs(String var1) {
      String[] var7;
      PageInputWidget[] var2 = new PageInputWidget[(var7 = PortalTextUtil.splitAmpersand(var1)).length];
      int var3 = 0;

      for (int var5 = 0; var5 < this.widgets.size(); var5++) {
         PageWidget var4;
         if ((var4 = (PageWidget)this.widgets.elementAt(var5)).type == 1) {
            PageInputWidget var8 = (PageInputWidget)var4;

            for (int var6 = 0; var6 < var7.length; var6++) {
               if (var8.getFieldName().equals(var7[var6])) {
                  var2[var6] = var8;
                  var3++;
                  break;
               }
            }

            if (var3 == var2.length) {
               break;
            }
         }
      }

      return var2;
   }

   private void addLink(String var1, String var2, String var3) {
      PageRequest var4;
      (var4 = new PageRequest()).href = var1;
      if (var2.length() > 0) {
         var4.setCacheKeys(var2);
      }

      var4.inputFields = var3;
      this.links.addElement(var4);
      if (focusedLinkIndex == -1) {
         focusedLinkIndex = 0;
      }
   }

   private void setSoftKeyLink(String var1, String var2, String var3, int var4) {
      PageRequest var5;
      (var5 = new PageRequest()).href = var1;
      if (var2.length() > 0) {
         var5.setCacheKeys(var2);
      }

      var5.inputFields = var3;
      switch (var4) {
         case 1:
            this.leftButtonRequest = var5;
            return;
         case 2:
            this.rightButtonRequest = var5;
      }
   }

   private void addWidget(PageWidget var1) {
      if (var1.y + PageWidget.drawOffsetY < PortalCanvasBase.screenHeight - super.footerHeight) {
         this.lastVisibleLine = var1.lineIndex;
      }

      this.widgets.addElement(var1);
   }

   public final void addButton(Hashtable var1) {
      String var2 = PortalManager.getString(var1, "href");
      PortalManager.getString(var1, "name");
      String var3 = PortalManager.getString(var1, "value");
      int var4;
      switch (var4 = PortalManager.getInt(var1, "lv", 0)) {
         case 0:
            PageButtonWidget var6 = new PageButtonWidget(this.currentLine, this.widgets.size(), this.links.size());
            int var5 = vh.stringWidth(PortalCanvasBase.font, var3);
            var6.setLabel(var3);
            if (this.cursorX + var5 > this.contentWidth) {
               this.newLine();
            }

            var6.setBounds(this.cursorX, this.cursorY, var5, PortalCanvasBase.lineHeight);
            this.cursorX += var5;
            if (var6.h > this.lineMaxHeight) {
               this.lineMaxHeight = var6.h;
            }

            this.addWidget(var6);
            this.addLink(var2, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"));
         default:
            return;
         case 1:
            PortalCanvasBase.softKeyLabels[10] = var3;
            this.hasLeftButton = true;
            this.setSoftKeyLink(var2, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"), var4);
            return;
         case 2:
            this.hasRightButton = true;
            PortalCanvasBase.softKeyLabels[11] = var3;
            this.setSoftKeyLink(var2, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"), var4);
      }
   }

   public final void addInput(Hashtable var1) {
      String var2 = PortalManager.getString(var1, "name");
      String var3 = PortalManager.getString(var1, "prompt");
      int var4 = PortalManager.getInt(var1, "len", 20);
      int var5 = PortalManager.getInt(var1, "size", 0);
      int var6 = PortalManager.getInt(var1, "imp", 0);
      int var7 = 0;
      switch (var6) {
         case 1:
            var7 = 2;
            break;
         case 2:
            var7 = 65536;
            break;
         case 3:
            var7 = 5;
            break;
         case 4:
            var7 = 3;
            break;
         case 5:
            var7 = 1;
            break;
         case 6:
            var7 = 4;
            break;
         case 7:
            var7 = 131072;
      }

      int var8 = PortalManager.getInt(var1, "lv", 1);
      PageInputWidget var9 = new PageInputWidget(this.currentLine, this.widgets.size(), this.links.size(), var7, var4, var8, super.client);
      if (this.cursorX + var5 > this.contentWidth) {
         this.newLine();
      }

      var9.setBounds(this.cursorX, this.cursorY, var5, PortalCanvasBase.lineHeight);
      var9.setPromptAndName(var3, var2);
      this.cursorX += var5;
      if (var9.h > this.lineMaxHeight) {
         this.lineMaxHeight = var9.h;
      }

      this.addWidget(var9);
      this.addLink("", "", "");
      if (this.links.size() == 1) {
         this.focusedWidgetIndex = this.widgetIndexOfLink(focusedLinkIndex);
         this.activeInputIndex = this.focusedWidgetIndex;
      }
   }

   public final void addImage(Hashtable var1) {
      String var2 = PortalManager.getString(var1, "href");
      boolean var3 = false;
      if (var2.length() > 0) {
         var3 = true;
      }

      int var6 = PortalManager.getInt(var1, "id", -1);
      Image var4;
      if (PortalCanvasBase.pageImages.size() > 0 && var6 > 0 && var6 <= PortalCanvasBase.pageImages.size()) {
         var4 = (Image)PortalCanvasBase.pageImages.elementAt(var6 - 1);
      } else {
         var4 = errorImage;
         var6 = -1;
      }

      int var5 = var4.getWidth();
      int var8 = var4.getHeight();
      PageImageWidget var7 = new PageImageWidget(this.currentLine, this.widgets.size(), var3, this.links.size());
      if (this.cursorX + var5 > this.contentWidth) {
         this.newLine();
      }

      if (var8 > this.lineMaxHeight) {
         this.lineMaxHeight = var8;
      }

      var7.setBounds(this.cursorX, this.cursorY, var5, var8);
      var7.setImage(var6, PortalManager.getInt(var1, "align", 1), PortalManager.getInt(var1, "valign", 2));
      this.cursorX += var5;
      this.addWidget(var7);
      if (var3) {
         this.addLink(var2, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"));
      }
   }

   public final void addText(Hashtable var1) {
      String var2 = PortalManager.getString(var1, "value");
      String var3 = PortalManager.getString(var1, "href");
      boolean var4 = false;
      if (var3.length() > 0) {
         var4 = true;
      }

      int var5 = this.contentWidth - this.cursorX;
      int var6 = PortalManager.getInt(var1, "size", -1);
      int var7 = PortalManager.getInt(var1, "style", -1);
      Font var14;
      if (var6 < 0 && var7 < 0) {
         var14 = PortalCanvasBase.font;
      } else {
         byte var13;
         switch (var7) {
            case 1:
               var13 = 1;
               break;
            case 2:
               var13 = 2;
               break;
            case 3:
               var13 = 3;
               break;
            case 4:
               var13 = 4;
               break;
            case 5:
               var13 = 5;
               break;
            case 6:
               var13 = 6;
               break;
            case 7:
               var13 = 7;
               break;
            default:
               var13 = 0;
         }

         byte var16;
         switch (var6) {
            case 1:
               var16 = 16;
               break;
            case 2:
               var16 = 0;
               break;
            default:
               var16 = 8;
         }

         var14 = Font.getFont(0, var13, var16);
      }

      var7 = PortalManager.getInt(var1, "color", -16777216);
      int var8 = vh.stringWidth(var14, var2);
      int var9 = PortalManager.getInt(var1, "space", 0);
      if (var8 <= var5) {
         this.addTextWidget(var2, var4, var7, var14.getStyle(), var14.getSize(), var9);
      } else {
         if (this.cursorX != 5) {
            String var11 = PortalTextUtil.clipToWidth(var2, var5, var14);
            this.addTextWidget(var11, var4, var7, var14.getStyle(), var14.getSize(), var9);
            var2 = var2.substring(var11.length(), var2.length());
         }

         String[] var12 = PortalTextUtil.wrapText(var2, var14, this.contentWidth - 5);

         for (int var10 = 0; var10 < var12.length; var10++) {
            this.newLine();
            this.addTextWidget(var12[var10], var4, var7, var14.getStyle(), var14.getSize(), var9);
         }
      }

      if (var4) {
         this.addLink(var3, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"));
      }
   }

   private void addTextWidget(String var1, boolean var2, int var3, int var4, int var5, int var6) {
      PageTextWidget var7;
      (var7 = new PageTextWidget(var1, this.currentLine, this.widgets.size(), var2, this.links.size())).setPosition(this.cursorX + var6, this.cursorY);
      var7.setStyle(var3, var4, var5);
      this.cursorX = this.cursorX + var7.w;
      if (this.lineMaxHeight < var7.h) {
         this.lineMaxHeight = var7.h;
      }

      this.addWidget(var7);
   }

   public final void newLine() {
      this.cursorX = 5;
      lineYs.add(this.cursorY);
      this.cursorY = this.cursorY + this.lineMaxHeight + 1;
      this.lineMaxHeight = PortalCanvasBase.lineHeight - 2;
      this.currentLine++;
   }

   public final void finishLayout() {
      this.cursorY = this.cursorY + this.lineMaxHeight + 1;
      if (this.cursorY > this.viewHeight) {
         this.showScrollBar = true;
         this.thumbH = this.viewHeight * 10000 / this.cursorY * (this.scrollBarH - 2) / 10000;
         if (this.thumbH < PortalCanvasBase.lineHeight) {
            this.thumbH = PortalCanvasBase.lineHeight;
         }
      }
   }

   public final void addPartLine(Hashtable var1) {
      String var2 = PortalManager.getString(var1, "value");
      String var3 = PortalManager.getString(var1, "href");
      int var4 = PortalManager.getInt(var1, "space", 0);
      int var5 = PortalManager.getInt(var1, "height", 0);
      int var6 = PortalManager.getInt(var1, "color", -16777216);
      boolean var7 = false;
      if (var3.length() > 0) {
         var7 = true;
      }

      if (var2.length() <= 0) {
         var2 = null;
      }

      PageSeparatorWidget var8;
      (var8 = new PageSeparatorWidget(this.currentLine, this.widgets.size(), var7, var7 ? this.links.size() : -1)).setup(var2, var4, var5, var6);
      var8.y = this.cursorY;
      this.cursorX = this.contentWidth - var4;
      if (var5 > this.lineMaxHeight) {
         this.lineMaxHeight = var5;
      }

      this.addWidget(var8);
      if (var7) {
         this.addLink(var3, PortalManager.getString(var1, "cache"), PortalManager.getString(var1, "addField"));
      }
   }

   public final void onPageLoaded(Hashtable[] var1) {
      if (super.mode == 1) {
         if (var1 != null && var1.length > 0 && !this.currentRequest.isRedirect) {
            PageSnapshot var3 = new PageSnapshot(var1);
            int var2 = this.history.size();
            if (this.historyIndex != var2 - 1) {
               var2--;

               while (var2 > this.historyIndex) {
                  this.history.removeElementAt(var2);
                  var2--;
               }
            }

            if (this.history.size() < 5) {
               this.historyIndex++;
               this.history.addElement(var3);
            } else {
               this.history.removeElementAt(0);
               this.history.addElement(var3);
            }
         }

         this.setSoftKeys((byte)(this.hasLeftButton ? 10 : 5), (byte)(this.hasRightButton ? 11 : (this.canGoBack() ? 9 : 4)));
      }
   }

   private boolean canGoBack() {
      return this.historyIndex > 0;
   }

   public final void clearPage() {
      this.cursorX = 5;
      this.cursorY = 0;
      this.scrollY = 0;
      PageWidget.drawOffsetY = PageWidget.contentTop - this.scrollY;
      this.currentLine = 0;
      this.activeInputIndex = -1;
      this.focusedWidgetIndex = -1;
      focusedLinkIndex = -1;
      this.firstVisibleLine = 0;
      this.lastVisibleLine = 0;
      this.showScrollBar = false;
      this.hasLeftButton = false;
      this.hasRightButton = false;
      this.lineMaxHeight = 0;
      this.widgets.removeAllElements();
      this.links.removeAllElements();
      lineYs.clear();
   }

   public final void setMode(int var1) {
      super.mode = var1;
      super.isEmbeddedMode = true;
      switch (var1) {
         case 0:
            this.showMessagePage("Game thuê bao tháng khi chạy phải đăng nhập‚ hãy đăng nhập trước!");
            this.setSoftKeys((byte)6, (byte)4);
            return;
         case 1:
            this.setSoftKeys((byte)5, (byte)4);
            this.historyIndex = -1;
            this.isEditing = false;
            this.showScrollBar = false;
            this.activeInputIndex = -1;
            this.focusedWidgetIndex = -1;
      }
   }

   public final void requestCommand(int var1) {
      this.showMessagePage("Đang kết nối... Vui lòng đợi!");
      super.manager.sendCommand(var1);
   }

   private int findNextFocusLine(int var1) {
      int var3 = 0;
      if (var1 % 2 == 1) {
         for (int var4 = 0; var4 < this.widgets.size(); var4++) {
            PageWidget var2;
            if ((var2 = (PageWidget)this.widgets.elementAt(var4)).linkIndex == focusedLinkIndex) {
               var3 = var2.lineIndex;
               if (var1 == 5) {
                  return var3;
               }
            }

            if (var2.linkIndex > focusedLinkIndex && var2.isLink) {
               if (var1 == 1 && var2.lineIndex > var3) {
                  return var2.lineIndex;
               }

               if (var1 == 3) {
                  return var2.lineIndex;
               }
            }
         }
      } else {
         for (int var6 = this.widgets.size() - 1; var6 >= 0; var6--) {
            PageWidget var5;
            if ((var5 = (PageWidget)this.widgets.elementAt(var6)).linkIndex == focusedLinkIndex) {
               var3 = var5.lineIndex;
            }

            if (this.linkIndexAtLine(var3 + 1) == focusedLinkIndex) {
               var3++;
            }

            if (var5.linkIndex < focusedLinkIndex && var5.isLink) {
               if (var1 == 0 && var5.lineIndex < var3) {
                  return var5.lineIndex;
               }

               if (var1 == 2) {
                  return var5.lineIndex;
               }
            }
         }
      }

      return -1;
   }

   private int widgetIndexOfLink(int var1) {
      for (int var2 = 0; var2 < this.widgets.size(); var2++) {
         if (((PageWidget)this.widgets.elementAt(var2)).linkIndex == var1) {
            return var2;
         }
      }

      return -1;
   }

   private int lineY(int var1, int var2) {
      while (var1 >= 0 && var1 < lineYs.size()) {
         if (var2 == 0) {
            return lineYs.get(var1);
         }

         int var10000 = var1 + 1;
         var2 = 0;
         var1 = var10000;
      }

      return 0;
   }

   private int linkIndexAtLine(int var1) {
      int var2 = -1;

      for (int var3 = 0; var3 < this.widgets.size(); var3++) {
         PageWidget var4;
         if ((var4 = (PageWidget)this.widgets.elementAt(var3)).isLink && var4.lineIndex == var1) {
            var2 = var4.linkIndex;
            break;
         }
      }

      return var2;
   }

   private void moveFocusUp(int var1) {
      if (this.isInputFocused()) {
         ((PageInputWidget)this.widgets.elementAt(this.activeInputIndex)).isEditing = false;
      }

      int var2 = this.findNextFocusLine(var1);
      int var3 = this.findNextFocusLine(5);
      if (var2 != -1) {
         if (var1 == 0) {
            focusedLinkIndex = this.linkIndexAtLine(var2);
            this.focusedWidgetIndex = this.widgetIndexOfLink(focusedLinkIndex);
         } else if (var1 == 2 && var2 >= this.firstVisibleLine && focusedLinkIndex > 0) {
            focusedLinkIndex--;
            this.focusedWidgetIndex = this.widgetIndexOfLink(focusedLinkIndex);
         }
      }

      int var4 = this.firstVisibleLine + (this.lastVisibleLine - this.firstVisibleLine) / 2 + 1;
      if (this.firstVisibleLine > 0) {
         boolean var5 = false;
         if (var2 == -1) {
            var5 = true;
         } else if (var1 == 0 && var3 <= var4) {
            var5 = true;
         } else if (var3 > var2 & var3 <= var4) {
            var5 = true;
         } else if (var1 == 3 && this.lineY(var2, 1) < this.scrollY) {
            var5 = true;
         }

         if (var5) {
            this.firstVisibleLine--;
            this.lastVisibleLine--;
            this.applyScroll();
         }
      }

      this.updateFocus();
   }

   private void moveFocusDown(int var1) {
      if (this.isInputFocused()) {
         ((PageInputWidget)this.widgets.elementAt(this.activeInputIndex)).isEditing = false;
      }

      int var2 = this.findNextFocusLine(var1);
      int var3 = this.findNextFocusLine(5);
      if (var2 != -1) {
         if (var1 == 1 && var2 <= this.lastVisibleLine) {
            focusedLinkIndex = this.linkIndexAtLine(var2);
            this.focusedWidgetIndex = this.widgetIndexOfLink(focusedLinkIndex);
         } else if (var1 == 3 && var2 <= this.lastVisibleLine && focusedLinkIndex < this.links.size()) {
            focusedLinkIndex++;
            this.focusedWidgetIndex = this.widgetIndexOfLink(focusedLinkIndex);
         }
      }

      int var4 = this.firstVisibleLine + (this.lastVisibleLine - this.firstVisibleLine) / 2 + 1;
      if (this.cursorY - this.scrollY >= this.viewHeight) {
         boolean var5 = false;
         if (var2 == -1) {
            var5 = true;
         } else if (var1 == 1 && var3 >= var4) {
            var5 = true;
         } else if (var1 == 3 && var3 < var2 && var3 >= var4) {
            var5 = true;
         } else if (this.lineY(var2, 0) > this.scrollY + this.viewHeight) {
            var5 = true;
         }

         if (var5) {
            this.firstVisibleLine++;
            if (this.lastVisibleLine < this.currentLine) {
               this.lastVisibleLine++;
            }

            this.applyScroll();
         }
      }

      this.updateFocus();
   }

   private void applyScroll() {
      this.scrollY = this.lineY(this.firstVisibleLine, 0);
      PageWidget.drawOffsetY = PageWidget.contentTop - this.scrollY;
   }

   private void updateFocus() {
      if (this.focusedWidgetIndex != -1) {
         PageWidget var1;
         if ((var1 = (PageWidget)this.widgets.elementAt(this.focusedWidgetIndex)).type == 1) {
            this.activeInputIndex = this.focusedWidgetIndex;
            if (this.isEditing) {
               ((PageInputWidget)var1).isEditing = true;
               return;
            }
         } else {
            this.isEditing = false;
         }
      }
   }

   private boolean isInputFocused() {
      return this.activeInputIndex >= 0 && this.activeInputIndex == this.focusedWidgetIndex;
   }

   protected final void handleKey() {
      switch (super.mode) {
         case 0:
            switch (super.keyCode) {
               case -507:
                  if (super.rightSoftKey == 4) {
                     this.showConfirm("Bạn chắc chắn muốn thoát?", 1);
                  }
                  break;
               case -506:
                  if (super.leftSoftKey == 6) {
                     this.requestCommand(super.client.pendingCommand);
                     this.setMode(1);
                     return;
                  }
            }

            return;
         case 1:
            if (super.isMenuOpen) {
               switch (super.keyCode) {
                  case -507:
                     super.isMenuOpen = false;
                     this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                     return;
                  case -506:
                  case -505:
                     super.isMenuOpen = false;
                     switch (this.menuIndex) {
                        case 0:
                           this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                           super.manager.sendCommand(10);
                           return;
                        case 1:
                           this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                           super.manager.request(this.currentRequest, null);
                           return;
                        case 2:
                           if (this.historyIndex < this.history.size() - 1) {
                              this.historyIndex++;
                              PageSnapshot var7 = (PageSnapshot)this.history.elementAt(this.historyIndex);
                              super.manager.renderPage(var7.elements);
                              this.setSoftKeys((byte)(this.hasLeftButton ? 10 : 5), (byte)(this.hasRightButton ? 11 : (this.canGoBack() ? 9 : 4)));
                              return;
                           }
                           break;
                        case 3:
                           this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                           super.manager.sendCommand(100);
                           return;
                        case 4:
                           this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                           if (this.isLoggedIn) {
                              this.showConfirm("Bạn chắc chắn muốn đăng xuất số hiện tại?", 1);
                              super.isLogoutPending = true;
                              return;
                           }

                           super.manager.sendCommand(101);
                           return;
                        case 5:
                           this.closePortal();
                           super.client.display.setCurrent(super.client.gameScreen);
                        default:
                           return;
                     }
                  case -504:
                  case -503:
                  default:
                     break;
                  case -502:
                     this.menuIndex = (this.menuIndex + 1) % this.menuItems.length;
                     break;
                  case -501:
                     this.menuIndex = (this.menuIndex + this.menuItems.length - 1) % this.menuItems.length;
                     return;
               }

               return;
            } else {
               switch (super.keyCode) {
                  case -507:
                     if (super.manager.loadState == 1) {
                        if (!super.manager.isResponseReceived) {
                           super.manager.cancelRequest();
                           super.manager.loadState = 0;
                           return;
                        }
                     } else {
                        switch (super.rightSoftKey) {
                           case 4:
                              this.showConfirm("Bạn chắc chắn muốn thoát?", 1);
                              return;
                           case 9:
                              if (this.historyIndex > 0 && this.historyIndex < this.history.size()) {
                                 this.historyIndex--;
                                 PageSnapshot var6 = (PageSnapshot)this.history.elementAt(this.historyIndex);
                                 super.manager.renderPage(var6.elements);
                                 this.setSoftKeys((byte)(this.hasLeftButton ? 10 : 5), (byte)(this.hasRightButton ? 11 : (this.canGoBack() ? 9 : 4)));
                                 return;
                              }
                              break;
                           case 11:
                              super.manager.request(this.rightButtonRequest, this.findInputs(this.rightButtonRequest.inputFields));
                              this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                        }
                     }

                     return;
                  case -506:
                     if (super.manager.loadState != 1) {
                        switch (super.leftSoftKey) {
                           case 5:
                              super.isMenuOpen = true;
                              if (super.client.settings != null && super.client.isLoggedIn()) {
                                 this.isLoggedIn = true;
                                 this.menuItems[4] = "5.Đăng xuất";
                              } else {
                                 this.isLoggedIn = false;
                                 this.menuItems[4] = "5.Đăng nhập QQ";
                              }

                              this.setSoftKeys((byte)6, (byte)9);
                              return;
                           case 10:
                              super.manager.request(this.leftButtonRequest, this.findInputs(this.leftButtonRequest.inputFields));
                              this.setSoftKeys(super.prevLeftSoftKey, super.prevRightSoftKey);
                        }
                     }

                     return;
                  default:
                     if (this.isInputFocused()) {
                        PageInputWidget var2;
                        (var2 = (PageInputWidget)this.widgets.elementAt(this.activeInputIndex)).handleKey(super.keyCode, super.gameAction);
                        switch (super.keyCode) {
                           case -505:
                              if (var2.inputMode == 0) {
                                 var2.isEditing = !var2.isEditing;
                                 this.isEditing = var2.isEditing;
                                 return;
                              } else {
                                 if (super.textBox == null) {
                                    super.textBox = new TextBox("Hãy nhập", "", 16, 0);
                                    super.okCommand = new Command("OK", 4, 1);
                                    super.backCommand = new Command("Trở về", 2, 1);
                                 }

                                 var2 = (PageInputWidget)this.widgets.elementAt(this.activeInputIndex);
                                 super.textBox.setMaxSize(var2.maxLength);
                                 super.textBox.setConstraints(var2.constraints);
                                 super.textBox.setTitle(var2.getPrompt());
                                 super.textBox.setString("");
                                 super.textBox.addCommand(super.okCommand);
                                 super.textBox.addCommand(super.backCommand);
                                 super.textBox.setCommandListener(this);
                                 super.client.display.setCurrent(super.textBox);
                              }
                           default:
                              return;
                           case -504:
                              if (!var2.isSymbolPickerOpen() && !this.isEditing) {
                                 this.moveFocusDown(3);
                                 return;
                              }
                              break;
                           case -503:
                              if (!var2.isSymbolPickerOpen() && !this.isEditing) {
                                 this.moveFocusUp(2);
                                 return;
                              }
                              break;
                           case -502:
                              if (!var2.isSymbolPickerOpen()) {
                                 this.moveFocusDown(1);
                                 return;
                              }
                              break;
                           case -501:
                              if (!var2.isSymbolPickerOpen()) {
                                 this.moveFocusUp(0);
                                 return;
                              }
                        }
                     } else {
                        switch (super.keyCode) {
                           case -505:
                              if (this.links.size() > 0) {
                                 PageRequest var5 = (PageRequest)this.links.elementAt(focusedLinkIndex);
                                 PageInputWidget[] var3 = this.findInputs(var5.inputFields);
                                 super.manager.request(var5, var3);
                              }
                              break;
                           case -504:
                              this.moveFocusDown(3);
                              return;
                           case -503:
                              this.moveFocusUp(2);
                              return;
                           case -502:
                              this.moveFocusDown(1);
                              return;
                           case -501:
                              this.moveFocusUp(0);
                              return;
                        }
                     }
               }
            }
      }
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1 == super.okCommand) {
         ((PageInputWidget)this.widgets.elementAt(this.activeInputIndex)).setText(super.textBox.getString());
      }

      super.client.display.setCurrent(this);
   }

   public final void closePortal() {
      super.releaseResources();
      PortalClient.saveSettings(PortalProtocol.serializeMap(super.client.settings));
      errorImage = null;
      this.widgets.removeAllElements();
      this.links.removeAllElements();
      lineYs.clear();
      this.widgets = null;
      this.links = null;
      lineYs = null;
      this.history.removeAllElements();
      this.history = null;
      this.currentRequest = null;
      this.leftButtonRequest = null;
      this.rightButtonRequest = null;
      super.manager.stopNetwork();
   }

   public final void initialize() {
      if (!super.isInitialized) {
         super.initMetrics(true);
         this.activeInputIndex = -1;
         this.focusedWidgetIndex = -1;
         this.widgets = new Vector();
         this.links = new Vector();
         this.history = new Vector();
         PortalCanvasBase.pageImages = new Vector();
         lineYs = new IntList();
         this.scrollBarW = 5;
         this.scrollBarX = PortalCanvasBase.screenWidth - this.scrollBarW - 1;
         this.scrollBarY = super.headerHeight + 1;
         this.scrollBarH = PortalCanvasBase.screenHeight - super.headerHeight - super.footerHeight - 2;
         this.thumbW = this.scrollBarW - 2;
         this.thumbH = 0;
         this.contentWidth = PortalCanvasBase.screenWidth - this.scrollBarW - 2;
         this.viewHeight = PortalCanvasBase.screenHeight - super.headerHeight - super.footerHeight;
         PageWidget.contentTop = super.headerHeight + 2;

         try {
            errorImage = Image.createImage("/com/tencent/mbox/error.png");
         } catch (Exception var1) {
            var1.printStackTrace();
         }

         super.isInitialized = true;
         this.isLoggedIn = false;
      }
   }

   public final void showMessage(String var1) {
      this.showMessagePage(var1);
   }
}
