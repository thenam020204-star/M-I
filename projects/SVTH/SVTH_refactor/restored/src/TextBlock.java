import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class TextBlock {
   private short[] lineIds;
   public String[] lines;
   private int[] lineColors;
   private short x = 15;
   private short y = 30;
   private short w = 0;
   private short h = 0;
   private short visibleLines = 3;
   int[] tagColors = new int[]{16711680, 65280, 255};
   String[] plainLines;
   String[][] colorSpans;

   public TextBlock() {
   }

   public final void addText(String var1, int var2, String var3, int var4) {
      if (var1 == null) {
         this.lineIds = Utils.append(this.lineIds, -1);
      } else {
         String[] var5 = Utils.wrapText(var1, var2);
         var2 = 0;
         if (this.lines != null) {
            var2 = this.lines.length;
         }

         for (int var7 = 0; var7 < var5.length; var7++) {
            this.lineIds = Utils.append(this.lineIds, var2 + var7);
            this.lines = Utils.append(this.lines, var5[var7]);
            this.lineColors = Utils.append(this.lineColors, var4);
         }
      }
   }

   public final void clear() {
      this.lineIds = null;
      this.lines = null;
      this.lineColors = null;
   }

   public final void setBounds(int var1, int var2, int var3, int var4, int var5) {
      this.x = (short)var1;
      this.y = (short)var2;
      this.w = (short)var3;
      this.h = (short)var4;
      this.visibleLines = (short)var5;
   }

   public final void draw(Graphics var1, TextBlock var2, int var3, boolean var4, boolean var5) {
      if (var2 != null && var2.lines != null) {
         if (var4) {
            GameUtil.drawScrollingText(var1, var2.lines, this.x, this.y + 3, this.w, 10, this.visibleLines, 0, var2.lineColors, var5);
            return;
         }

         var1.setColor(0);

         for (int var6 = var3; var6 < var2.lines.length && var6 < var3 + this.visibleLines; var6++) {
            var1.setColor(var2.lineColors[var6]);
            if (!var5) {
               vh.drawString(var1, var2.lines[var6], this.x + 4, this.y + Utils.lineHeight * (var6 - var3), 20);
            } else {
               vh.drawString(var1, var2.lines[var6], this.x + this.w / 2, this.y + Utils.lineHeight * (var6 - var3), 17);
            }
         }

         if (var2.lines.length > this.visibleLines) {
            GameUtil.drawScrollBar(var1, null, null, this.x + this.w - 15, this.y, this.h, this.lines.length, this.visibleLines, var3);
         }
      }
   }

   public final void addTaggedText(String var1, String var2, int var3) {
      String[] var4 = vh.wrapParts(var1, var2, var3);
      if (var4 != null) {
         for (int var5 = 0; var5 < var4.length; var5++) {
            this.parseColorTags(var4[var5], var5);
         }
      }
   }

   private void parseColorTags(String var1, int var2) {
      try {
         int var3 = 0;
         Vector var4 = new Vector();
         StringBuffer var5 = new StringBuffer();
         int var6 = 0;
         String var7 = null;
         int var8 = 0;

         while (var3 < var1.length()) {
            int var9 = var1.indexOf("<", var3);
            int var10 = var1.indexOf(">", var3);
            if (var9 == -1 && var10 == -1) {
               var5.append(var1.substring(var3));
               break;
            }

            var5.append(var1.substring(var3, var9));
            if (!var1.substring(var9 + 1, var10).equals("/")) {
               var6 = var9 - var8;
               var7 = var1.substring(var9 + 1, var10);
               var8 += ("<" + var7 + ">").length();
            } else {
               if (var6 != var9 - var8) {
                  var4.addElement(new String[]{String.valueOf(var2), String.valueOf(var6), String.valueOf(var9 - var8), var7});
               }

               var8 += "</>".length();
            }

            var3 = var10 + 1;
         }

         String[][] var12 = null;
         if (var4.size() > 0) {
            var12 = new String[var4.size()][];
            var4.copyInto(var12);
         }

         for (int var13 = 0; var12 != null && var13 < var12.length; var13++) {
            this.colorSpans = Utils.append(this.colorSpans, var12[var13]);
         }

         this.plainLines = Utils.append(this.plainLines, var5.toString());
      } catch (Exception var11) {
         var11.printStackTrace();
      }
   }

   public final void drawTaggedLine(Graphics var1, String var2, int var3, int var4, int var5, int var6) {
      var1.setColor(var6);
      vh.drawString(var1, var2, var4, var5, 20);

      for (int var9 = 0; this.colorSpans != null && var9 < this.colorSpans.length; var9++) {
         int var7 = Utils.parseInt(this.colorSpans[var9][0]);
         if (var3 == var7) {
            int var8 = Utils.parseInt(this.colorSpans[var9][2]);
            if (var3 == var7 && var8 <= var2.length()) {
               if (!this.colorSpans[var9][3].startsWith("0x") && !this.colorSpans[var9][3].startsWith("0X")) {
                  var1.setColor(this.tagColors[Utils.parseInt(this.colorSpans[var9][3]) - 1]);
               } else {
                  var1.setColor(Utils.parseInt(this.colorSpans[var9][3]));
               }

               var7 = Utils.parseInt(this.colorSpans[var9][1]);
               vh.drawString(var1, var2.substring(var7, var8), var4 + vh.stringWidth(Utils.defaultFont, this.plainLines[var3].substring(0, var7)), var5, 20);
            }
         }
      }
   }
}
