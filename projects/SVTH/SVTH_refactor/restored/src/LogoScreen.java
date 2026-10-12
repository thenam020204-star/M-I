import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class LogoScreen {
   public byte mode;
   private Image logoImage;
   private AnimSprite logoAnim;
   private boolean[] logoLoaded;
   private byte logoIndex = 0;
   private String[] logoFiles;
   private int[] logoBackColors;
   private short[] logoDurations;
   private long logoStartTime;
   boolean isFinished = false;
   private Image musicIcon;

   public LogoScreen() {
      this.logoFiles = ChannelConfig.logoFiles;
      this.logoDurations = ChannelConfig.logoTimes;
      this.logoBackColors = ChannelConfig.logoBgColors;
      this.logoDurations = Utils.append(this.logoDurations, 2000);
      if (this.logoFiles != null) {
         this.logoLoaded = new boolean[this.logoFiles.length];
      }

      this.musicIcon = ResourceCache.loadImage("/pic/musicIcon.gf");
      this.logoStartTime = System.currentTimeMillis();
      TouchSoftKeys.setSoftKeys(3);
   }

   public final void draw(Graphics var1) {
      var1.setColor(0);
      var1.setClip(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
      var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
      var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
      if (this.mode == 1) {
         if (this.musicIcon != null) {
            var1.drawImage(this.musicIcon, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 - Utils.lineHeight - 4, 33);
         }

         String var2 = "(Tắt nhạc để trò chơi chạy mượt hơn)";
         String[] var5 = Utils.splitAndWrap("(Tắt nhạc để trò chơi chạy mượt hơn)", "\r\n", DeviceConfig.screenSize[0] - 20);
         var1.setColor(16777113);
         vh.drawString(var1, "Bật âm nhạc?", DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 - Utils.lineHeight, 17);
         var1.setColor(16711680);

         for (int var3 = 0; var5 != null && var3 < var5.length; var3++) {
            vh.drawString(var1, var5[var3], DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2 + vh.stringWidth(Utils.defaultFont, "MM") * var3, 17);
         }

         var1.setColor(16777215);
         if (!DeviceConfig.modelName.equals("E2") && !DeviceConfig.modelName.equals("V8")) {
            vh.drawString(var1, "Có", 2, DeviceConfig.screenSize[1] - 2, 36);
            vh.drawString(var1, "Không", DeviceConfig.screenSize[0] - 2, DeviceConfig.screenSize[1] - 2, 40);
            return;
         }

         vh.drawString(var1, "Không", 2, DeviceConfig.screenSize[1] - 2, 36);
         vh.drawString(var1, "Có", DeviceConfig.screenSize[0] - 2, DeviceConfig.screenSize[1] - 2, 40);
      } else if (this.mode == 2) {
         var1.setColor(16749056);
         var1.setClip(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
         var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
         var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
         if (!this.isFinished) {
            byte var6 = this.logoIndex;
            if (!this.logoLoaded[var6]) {
               if (this.logoFiles[var6].toLowerCase().endsWith(".av")) {
                  this.logoAnim = new AnimSprite();
                  this.logoAnim.load(this.logoFiles[var6]);
                  this.logoAnim.setPosition(DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2);
               } else if (this.logoFiles[var6].toLowerCase().endsWith(".png") || this.logoFiles[var6].toLowerCase().endsWith(".gf")) {
                  this.logoImage = ResourceCache.loadImage(this.logoFiles[var6]);
               }

               this.logoLoaded[var6] = true;
            }

            if (!this.logoFiles[var6].toLowerCase().endsWith(".png") && !this.logoFiles[var6].toLowerCase().endsWith(".gf")) {
               if (this.logoFiles[var6].toLowerCase().endsWith(".av") && this.logoAnim != null) {
                  int var4 = 0;
                  if (this.logoBackColors != null) {
                     var4 = this.logoBackColors[var6];
                  }

                  var1.setColor(var4);
                  var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
                  this.logoAnim.draw(var1);
                  if (this.logoAnim.getFrame() < this.logoAnim.getActionFrameCount(0) - 1) {
                     if (GameCanvas.instance.frameCounter % 3 == 0) {
                        this.logoAnim.nextFrame(false);
                        return;
                     }
                  } else {
                     if (this.logoIndex < this.logoFiles.length - 1) {
                        this.logoIndex++;
                        this.logoStartTime = System.currentTimeMillis();
                        return;
                     }

                     var1.setColor(0);
                     var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
                     this.logoFiles = null;
                     this.isFinished = true;
                     if (DeviceConfig.allowMusic) {
                        this.mode = 1;
                        return;
                     }

                     Main.self.showCover();
                  }
               }
            } else if (this.logoImage != null) {
               var1.drawImage(this.logoImage, DeviceConfig.screenSize[0] / 2, DeviceConfig.screenSize[1] / 2, 3);
               if (System.currentTimeMillis() - this.logoStartTime > (long)this.logoDurations[this.logoIndex]) {
                  if (this.logoIndex < this.logoFiles.length - 1) {
                     this.logoIndex++;
                     this.logoStartTime = System.currentTimeMillis();
                     return;
                  }

                  var1.setColor(0);
                  var1.fillRect(0, 0, GameCanvas.instance.screenWidth, GameCanvas.instance.screenHeight);
                  this.logoFiles = null;
                  this.isFinished = true;
                  if (DeviceConfig.allowMusic) {
                     this.mode = 1;
                     return;
                  }

                  Main.self.showCover();
                  return;
               }
            }
         }
      }
   }
}
