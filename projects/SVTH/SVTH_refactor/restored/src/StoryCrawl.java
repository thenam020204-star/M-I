import javax.microedition.lcdui.Graphics;

public final class StoryCrawl {
   private String rawText;
   String[] lines;
   private short clipH;
   private short endY;
   private short x;
   private short y;
   private short scrollY;
   short style;
   private byte lineIndex;
   private byte charIndex;
   private boolean showPreviousLines = false;
   private short typeScrollY = 0;
   private short visibleLines = 4;

   public StoryCrawl() {
   }

   public final void draw(Graphics var1) {
      int var2 = vh.getHeight(Utils.defaultFont);
      if (this.style == 1) {
         var1.setColor(16711680);
         if (!DeviceConfig.modelName.equals("E2")
            && !DeviceConfig.modelName.equals("E398")
            && !DeviceConfig.modelName.equals("L6")
            && !DeviceConfig.modelName.equals("L7")
            && !DeviceConfig.modelName.equals("K1")
            && !DeviceConfig.modelName.equals("V8")) {
            vh.drawString(var1, "Bỏ qua", DeviceConfig.screenSize[0] - 2, DeviceConfig.screenSize[1] - 2, 40);
         } else {
            vh.drawString(var1, "Bỏ qua", 2, DeviceConfig.screenSize[1] - 2, 36);
         }

         var1.setClip(this.x, this.y, DeviceConfig.screenSize[0], this.clipH);
         if (GameCanvas.instance.frameCounter % 2 == 0) {
            this.scrollY = (short)(this.scrollY - GameCanvas.instance.world.crawlSpeed);
         }

         if (this.scrollY <= this.endY) {
            GameCanvas.instance.world.storyCrawlActive = false;
            GameCanvas.instance.world.script.advance(5);
            this.lines = null;
         } else {
            for (int var4 = 0; this.lines != null && var4 < this.lines.length; var4++) {
               var1.setColor(16777215);
               vh.drawString(var1, this.lines[var4], DeviceConfig.screenSize[0] / 2, this.scrollY + var2 * var4, 17);
            }
         }
      } else {
         var1.setColor(0);
         var1.fillRect(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         var1.setColor(16711680);
         if (!DeviceConfig.modelName.equals("E2")
            && !DeviceConfig.modelName.equals("E398")
            && !DeviceConfig.modelName.equals("L6")
            && !DeviceConfig.modelName.equals("L7")
            && !DeviceConfig.modelName.equals("K1")
            && !DeviceConfig.modelName.equals("V8")) {
            vh.drawString(var1, "Bỏ qua", DeviceConfig.screenSize[0] - 2, DeviceConfig.screenSize[1] - 2, 40);
         } else {
            vh.drawString(var1, "Bỏ qua", 2, DeviceConfig.screenSize[1] - 2, 36);
         }

         var1.setClip(this.x, DeviceConfig.screenSize[1] / 2 - this.visibleLines * var2 / 2, DeviceConfig.screenSize[0], var2 * this.visibleLines);
         if (this.lineIndex <= this.lines.length - 1) {
            if (this.lineIndex <= this.visibleLines - 1) {
               var1.setColor(16777215);
               vh.drawString(
                  var1,
                  this.lines[this.lineIndex].substring(0, this.charIndex + 1),
                  DeviceConfig.screenSize[0] / 2 - vh.stringWidth(Utils.defaultFont, this.lines[this.lineIndex]) / 2,
                  DeviceConfig.screenSize[1] / 2 - this.visibleLines * var2 / 2 + var2 * this.lineIndex,
                  20
               );
            } else {
               var1.setColor(16777215);
               vh.drawString(
                  var1,
                  this.lines[this.lineIndex].substring(0, this.charIndex + 1),
                  DeviceConfig.screenSize[0] / 2 - vh.stringWidth(Utils.defaultFont, this.lines[this.lineIndex]) / 2,
                  DeviceConfig.screenSize[1] / 2 - this.visibleLines * var2 / 2 + var2 * (this.visibleLines - 1),
                  20
               );
            }
         }

         if (this.showPreviousLines) {
            for (int var3 = 0; this.lines != null && var3 < this.lineIndex; var3++) {
               var1.setColor(16777215);
               vh.drawString(
                  var1,
                  this.lines[var3],
                  DeviceConfig.screenSize[0] / 2,
                  this.typeScrollY + DeviceConfig.screenSize[1] / 2 - this.visibleLines * var2 / 2 + var2 * var3,
                  17
               );
            }
         }

         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         if (this.charIndex < this.lines[this.lineIndex].length() - 1) {
            if (GameCanvas.instance.frameCounter % GameCanvas.instance.world.crawlSpeed == 0) {
               this.charIndex++;
            }
         } else if (GameCanvas.instance.frameCounter % GameCanvas.instance.world.crawlSpeed == 0) {
            this.showPreviousLines = true;
            this.lineIndex++;
            if (this.lineIndex >= this.visibleLines) {
               this.typeScrollY = (short)(this.typeScrollY - var2);
            }

            this.charIndex = 0;
         }

         if (this.lineIndex > this.lines.length - 1) {
            GameCanvas.instance.world.storyCrawlActive = false;
            GameCanvas.instance.world.script.advance(5);
            this.lines = null;
            this.showPreviousLines = false;
            this.typeScrollY = 0;
         }
      }
   }

   public final void begin(int var1, int var2, int var3, int var4, int var5) {
      this.x = (short)var2;
      this.y = (short)var3;
      this.scrollY = (short)(var4 + var3);
      this.style = (short)var5;
      String var6 = Utils.loadConfigText("/bin/story.bin");
      this.rawText = Utils.substringBetween(var6, "Story" + var1 + ":", "Story" + var1 + "End");
      if (this.rawText.indexOf("&") != -1) {
         this.rawText = GameUtil.replaceAll(this.rawText, "&", GameData.roleNames[0]);
      }

      this.lines = Utils.splitAndWrap(this.rawText, "\r\n", DeviceConfig.screenSize[0] - 10);
      this.rawText = null;
      this.clipH = (short)var4;
      this.endY = (short)(-(this.lines.length * vh.getHeight(Utils.defaultFont)) + var3);
      this.lineIndex = 0;
      this.charIndex = 0;
      if (this.style == 1) {
         GameCanvas.instance.world.crawlSpeed = 2;
      } else {
         GameCanvas.instance.world.crawlSpeed = 8;
      }
   }
}
