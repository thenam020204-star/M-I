import javax.microedition.lcdui.Graphics;

public final class TreasureChest extends SpriteBase {
   private AnimSprite anim;
   public short chestId;
   private byte column6;
   public byte openFrame;
   public short gold;
   public short[][] items;
   public short[][] equips;
   public byte chestKind;
   public byte alreadyOpened = 0;
   private short[] drawRect;
   private short[] hitRect;

   public TreasureChest(AnimSprite var1) {
      this.anim = var1;
      this.drawRect = new short[]{-9, -19, 18, 19};
      this.hitRect = new short[]{-12, -12, 24, 12};
   }

   public TreasureChest() {
      this.width = 18;
      this.height = 17;
      this.drawRect = new short[]{(short)(-this.width / 2), (short)(-this.height), this.width, this.height};
      this.hitRect = new short[]{(short)(-this.width / 2), (short)(-(this.height / 2)), this.width, (short)(this.height / 2)};
   }

   public final void setColumn6(int var1) {
      this.column6 = (byte)var1;
   }

   public final void draw(Graphics var1) {
      if (this.isVisible) {
         this.anim.setAction(this.openFrame);
         this.anim.x = this.x;
         this.anim.y = this.y;
         this.anim.draw(var1);
         if (GameCanvas.instance.frameCounter % 3 == 0) {
            this.anim.nextFrame(true);
         }
      }
   }

   public final short[] getCollisionBoxes(int var1) {
      return new short[]{(short)(this.x + this.hitRect[0]), (short)(this.y + this.hitRect[1]), this.hitRect[2], this.hitRect[3]};
   }

   public final short[] getBounds() {
      return this.drawRect;
   }

   public final void drawDebugBoxes(Graphics var1) {
      var1.drawRect(this.x + this.hitRect[0], this.y + this.hitRect[1], this.hitRect[2], this.hitRect[3]);
   }
}
