import javax.microedition.lcdui.Graphics;

public abstract class SpriteBase {
   public short x;
   public short y;
   public short width;
   public short height;
   public boolean isVisible = true;
   public byte kind;
   public short roleId;
   public short headHeight;
   public boolean isSolid = true;

   public SpriteBase() {
   }

   public final void setPosition(int var1, int var2) {
      this.x = (short)var1;
      this.y = (short)var2;
   }

   public abstract void draw(Graphics var1);

   public abstract short[] getBounds();

   public abstract short[] getCollisionBoxes(int var1);

   public abstract void drawDebugBoxes(Graphics var1);
}
