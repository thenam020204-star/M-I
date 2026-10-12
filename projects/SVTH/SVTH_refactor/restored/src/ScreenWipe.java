import javax.microedition.lcdui.Graphics;

public final class ScreenWipe {
   private byte wipeType;
   private static byte typeStripes = 0;
   private static byte typeGrid = 1;
   private static byte e_B = 2;
   private static byte f_B = 3;
   public static byte typeCount = 2;
   private boolean isRevealing;
   private boolean isCovering;
   private int progress;
   private int cellSize = 30;

   public ScreenWipe() {
   }

   public final void setType(int var1) {
      this.wipeType = (byte)var1;
   }

   public final void begin() {
      this.isRevealing = true;
      this.isCovering = false;
      if (this.wipeType == 0) {
         this.progress = 0;
      } else {
         if (this.wipeType == typeGrid) {
            this.progress = this.cellSize;
         }
      }
   }

   public final boolean isActive() {
      return this.isRevealing || this.isCovering;
   }

   public final void draw(Graphics var1) {
      if (this.wipeType == 0) {
         if (this.isRevealing) {
            GameCanvas.instance.world.playerControlEnabled = false;
            var1.setColor(0);
            var1.fillRect(this.progress, 0, 4, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 20, 0, 8, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 40, 0, 12, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 60, 0, 16, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 80, 0, GameCanvas.instance.getWidth(), GameCanvas.instance.getHeight());
            if (!DeviceConfig.modelName.equals("D608")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("E398")
               && !DeviceConfig.modelName.equals("L6")
               && !DeviceConfig.modelName.equals("L7")
               && !DeviceConfig.modelName.equals("K1")
               && !DeviceConfig.modelName.equals("N6230i")) {
               this.progress += 20;
            } else {
               this.progress += 30;
            }

            if (this.progress > GameCanvas.instance.getWidth()) {
               this.isRevealing = false;
               GameCanvas.instance.world.playerControlEnabled = true;
            }

            return;
         }

         if (this.isCovering) {
            if (this.progress >= 0) {
               this.isCovering = false;
               GameCanvas.instance.world.startGame();
            }

            var1.setColor(0);
            var1.fillRect(this.progress, 0, 4, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 20, 0, 8, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 40, 0, 12, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + 60, 0, 16, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress, 0, GameCanvas.instance.getWidth(), GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + GameCanvas.instance.getWidth() + 20 - 16, 0, 16, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + GameCanvas.instance.getWidth() + 40 - 12, 0, 12, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + GameCanvas.instance.getWidth() + 60 - 8, 0, 8, GameCanvas.instance.getHeight());
            var1.fillRect(this.progress + GameCanvas.instance.getWidth() + 80 - 4, 0, 4, GameCanvas.instance.getHeight());
            if (!DeviceConfig.modelName.equals("D608")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("E398")
               && !DeviceConfig.modelName.equals("L6")
               && !DeviceConfig.modelName.equals("L7")
               && !DeviceConfig.modelName.equals("K1")
               && !DeviceConfig.modelName.equals("N6230i")) {
               if (this.progress + 20 > 0) {
                  this.progress = 0;
                  return;
               }

               this.progress += 20;
            } else if (this.progress + 30 <= 0) {
               this.progress += 30;
            } else {
               this.progress = 0;
            }
         }
      } else if (this.wipeType == typeGrid) {
         byte var2 = (byte)(GameCanvas.instance.screenWidth / this.cellSize + 1);
         byte var3 = (byte)(GameCanvas.instance.screenHeight / this.cellSize + 1);
         var1.setColor(0);
         if (this.isRevealing) {
            GameCanvas.instance.world.playerControlEnabled = false;

            for (int var4 = 0; var4 < var2; var4++) {
               for (int var5 = 0; var5 < var3; var5++) {
                  var1.fillRect(var4 * this.cellSize, var5 * this.cellSize, this.progress, this.progress);
               }
            }

            if (!DeviceConfig.modelName.equals("D608")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("E398")
               && !DeviceConfig.modelName.equals("L6")
               && !DeviceConfig.modelName.equals("L7")
               && !DeviceConfig.modelName.equals("K1")
               && !DeviceConfig.modelName.equals("N6230i")) {
               this.progress -= 3;
            } else {
               this.progress -= 5;
            }

            if (this.progress < 0) {
               this.isRevealing = false;
               GameCanvas.instance.world.playerControlEnabled = true;
               return;
            }
         } else if (this.isCovering) {
            for (int var6 = 0; var6 < var2; var6++) {
               for (int var7 = 0; var7 < var3; var7++) {
                  var1.fillRect(var6 * this.cellSize, var7 * this.cellSize, this.progress, this.progress);
               }
            }

            if (!DeviceConfig.modelName.equals("D608")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("E398")
               && !DeviceConfig.modelName.equals("L6")
               && !DeviceConfig.modelName.equals("L7")
               && !DeviceConfig.modelName.equals("K1")
               && !DeviceConfig.modelName.equals("N6230i")) {
               this.progress += 3;
            } else {
               this.progress += 5;
            }

            if (this.progress > this.cellSize) {
               this.isCovering = false;
               this.progress = 0;
               GameCanvas.instance.world.startGame();
            }
         }
      }
   }
}
