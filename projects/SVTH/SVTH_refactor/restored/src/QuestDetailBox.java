import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class QuestDetailBox {
   private int x;
   private int y;
   private int w;
   private int h;
   private String title;
   private String[] descLines;
   private String rewardTitle;
   private String[] rewards;
   int[] colors;
   private int lineHeight = Utils.lineHeight + 4;
   int visibleLines;
   int totalLines;
   int scrollLine = 0;
   private Image expIcon;
   private Image goldIcon;

   public QuestDetailBox(int var1, int var2, int var3, int var4) {
      this.x = var1;
      this.y = var2;
      this.w = var3;
      this.h = var4;
      this.visibleLines = this.h / this.lineHeight;
   }

   public final void setContent(String var1, String var2, String var3, String var4) {
      this.title = var1;
      this.descLines = Utils.wrapText(var2, this.w - 20);
      this.totalLines = this.totalLines + this.descLines.length + 2;
      this.rewardTitle = var3;
      if (var4 != null && !var4.equals("-1")) {
         this.rewards = Utils.split(var4, "|");
         this.totalLines = this.totalLines + this.rewards.length;
      } else {
         this.rewards = null;
      }
   }

   public final void draw(Graphics var1) {
      var1.setClip(this.x, this.y, this.w, this.h);
      int var2 = this.x;
      int var3 = this.y + 2 - this.scrollLine * this.lineHeight;
      if (this.totalLines > this.visibleLines) {
         GameUtil.drawScrollBar(var1, null, null, this.x + this.w - 15, this.y + 5, this.h - 10, this.totalLines, this.visibleLines, this.scrollLine);
      }

      if (this.colors[0] == this.colors[1]) {
         var1.setColor(this.colors[0]);
         vh.drawString(var1, this.title, var2 + 4, var3 + 2, 20);
      } else {
         Utils.drawOutlinedText8(var1, this.title, var2 + 4, var3, this.colors[0], this.colors[1], 20);
      }

      var3 += this.lineHeight;
      var1.setColor(16777215);

      for (int var4 = 0; var4 < this.descLines.length; var4++) {
         vh.drawString(var1, this.descLines[var4], var2 + 4, var3, 20);
         var3 += this.lineHeight;
      }

      if (this.rewards != null) {
         if (this.colors[2] == this.colors[3]) {
            var1.setColor(this.colors[2]);
            vh.drawString(var1, this.rewardTitle, var2 + 4, var3, 20);
         } else {
            Utils.drawOutlinedText8(var1, this.rewardTitle, var2 + 4, var3, this.colors[2], this.colors[3], 20);
         }
      }

      var3 += this.lineHeight;
      var1.setColor(16777215);
      if (this.rewards != null) {
         System.out.println("sDetailStr" + this.rewards);

         for (int var14 = 0; var14 < this.rewards.length; var14++) {
            short[] var5;
            if ((var5 = Utils.parseShortArray(this.rewards[var14], ","))[0] == 1) {
               var2 = this.x + 4;
               GameData.loadItemTable();
               if (GameData.itemIcons == null) {
                  GameData.loadItemIcons();
               }

               if (GameData.itemIcons[GameData.findItemIndex(var5[1])] != null) {
                  var1.drawImage(
                     GameData.itemIcons[GameData.findItemIndex(var5[1])],
                     var2 + GameData.itemIcons[GameData.findItemIndex(var5[1])].getWidth() / 2,
                     var3 - 2 + this.lineHeight / 2,
                     3
                  );
                  var2 += GameData.itemIcons[GameData.findItemIndex(var5[1])].getWidth();
               }

               vh.drawString(var1, GameData.itemNames[GameData.findItemIndex(var5[1])], var2, var3, 20);
               var2 = var2 + vh.stringWidth(Utils.defaultFont, GameData.itemNames[GameData.findItemIndex(var5[1])]) + 5;
               vh.drawString(var1, "X" + var5[2], var2, var3, 20);
            } else if (var5[0] == 2) {
               var2 = this.x + 4;
               Equip.loadTable();
               if (Equip.icons == null) {
                  Equip.loadIcons();
               }

               if (Equip.icons[GameData.findEquipIndex(var5[1])] != null) {
                  var1.drawImage(
                     Equip.icons[GameData.findEquipIndex(var5[1])],
                     var2 + GameData.itemIcons[GameData.findItemIndex(var5[1])].getWidth() / 2,
                     var3 - 2 + this.lineHeight / 2,
                     3
                  );
                  var2 += Equip.icons[GameData.findEquipIndex(var5[1])].getWidth();
               }

               vh.drawString(var1, Equip.names[GameData.findEquipIndex(var5[1])], var2, var3, 20);
               var2 = var2 + vh.stringWidth(Utils.defaultFont, Equip.names[GameData.findItemIndex(var5[1])]) + 5;
               vh.drawString(var1, "X 1", var2, var3, 20);
            } else if (var5[0] == 3) {
               var2 = this.x + 4;
               if (this.goldIcon == null) {
                  this.goldIcon = ResourceCache.loadImage("/pic/D10.gf");
               }

               var1.drawImage(this.goldIcon, var2 + this.goldIcon.getWidth() / 2, var3 - 2 + this.lineHeight / 2, 3);
               vh.drawString(var1, "" + var5[1], var2 + this.goldIcon.getWidth() + 5, var3, 20);
            } else if (var5[0] == 5) {
               var2 = this.x + 4;
               if (this.expIcon == null) {
                  this.expIcon = ResourceCache.loadImage("/icon/13.gf");
               }

               var1.drawImage(this.expIcon, var2 + this.expIcon.getWidth() / 2, var3 - 2 + this.lineHeight / 2, 3);
               vh.drawString(var1, "EXP", var2 + this.expIcon.getWidth() + 5, var3, 20);
               vh.drawString(var1, "" + var5[1], var2 + 10 + this.expIcon.getWidth() + vh.stringWidth(Utils.defaultFont, "Thuộc tính"), var3, 20);
            }

            var3 += this.lineHeight;
         }
      }

      var1.setClip(0, 0, GameCanvas.instance.getWidth(), GameCanvas.instance.getHeight());
   }
}
