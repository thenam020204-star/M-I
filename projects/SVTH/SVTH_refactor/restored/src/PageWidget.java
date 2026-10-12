import javax.microedition.lcdui.Graphics;

public abstract class PageWidget {
   public static final String[] TAG_NAMES = new String[]{
      "a", "input", "button", "cache", "br", "img", "tab", "return", "partline", "blank", "redirect", "struct"
   };
   public boolean isLink;
   public int linkIndex;
   public int lineIndex;
   public byte type;
   public int w;
   public int h;
   public int x;
   public int y;
   public static int contentTop;
   public static int drawOffsetY;
   public int drawX;
   public int drawY;
   public static int textColor = 12574712;
   public static int linkFocusColor = 16631433;
   public static int linkColor = 8829414;
   public static int inputBorderColor = 865624;
   public static int inputFocusBorderColor = 16213002;
   public static int inputBgColor = 11719149;
   public static int inputFocusBgColor = 7918079;
   public static int inputTextColor = 1397123;
   public static int inputFocusTextColor = 1397123;
   public static int buttonTextColor = 9295103;
   public static int buttonFocusTextColor = 16777215;
   public static int imageFocusColor = 255;

   public PageWidget(byte var1, int var2, boolean var3, int var4) {
      this.type = var1;
      this.lineIndex = var2;
      this.isLink = var3;
      if (this.isLink) {
         this.linkIndex = var4;
      } else {
         this.linkIndex = -1;
      }
   }

   public final void setBounds(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
   }

   public final void setPosition(int var1, int var2) {
      this.x = var1;
      this.y = var2;
   }

   public abstract void draw(Graphics var1);
}
