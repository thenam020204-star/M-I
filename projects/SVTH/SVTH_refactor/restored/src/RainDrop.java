import java.util.Random;

public final class RainDrop {
   public short x;
   public short y;
   public byte dx;
   public byte dy;
   public byte lineDx = -3;
   public byte lineDy = 6;
   private static Random random;

   public RainDrop() {
      if (random == null) {
         random = new Random();
      }

      this.x = (short)GameUtil.randomRange(0, DeviceConfig.screenSize[0] + 150);
      this.y = (short)(-Math.abs(random.nextInt() % DeviceConfig.screenSize[1]));
      this.dx = -5;
      this.dy = 10;
   }
}
