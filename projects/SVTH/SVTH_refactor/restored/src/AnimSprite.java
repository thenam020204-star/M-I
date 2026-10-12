import javax.microedition.lcdui.Graphics;

public final class AnimSprite extends SpriteBase {
   AvAnimation anim;
   private short action;
   private short frame;
   boolean isMirrored;

   public AnimSprite() {
   }

   public final int getAction() {
      return this.action;
   }

   public final int getActionCount() {
      return this.anim != null ? this.anim.getActionCount() : 0;
   }

   public final short[] getCollisionBoxes(int var1) {
      return this.anim != null ? this.anim.getBoxes(this.action, this.frame, this.x, this.y, var1, this.isMirrored) : null;
   }

   public final short[] getBoxesAt(int var1, int var2, int var3) {
      return this.anim != null ? this.anim.getBoxes(var1, var2, this.x, this.y, var3, this.isMirrored) : null;
   }

   public final int getFrame() {
      return this.frame;
   }

   public final int getFrameCount() {
      return this.action >= 0 && this.anim != null ? this.anim.getFrameCount(this.action) : 0;
   }

   public final int getActionFrameCount(int var1) {
      return this.action >= 0 && this.anim != null ? this.anim.getFrameCount(var1) : 0;
   }

   public final short[] getBounds() {
      if (!this.isMirrored) {
         return this.anim.getBounds();
      } else {
         short[] var1 = this.anim.getBounds();
         return new short[]{(short)(-(var1[0] + var1[2])), var1[1], var1[2], var1[3]};
      }
   }

   public final void nextFrame(boolean var1) {
      if (this.frame < this.getActionFrameCount(this.action) - 1) {
         this.frame++;
      } else {
         if (var1) {
            this.frame = 0;
         }
      }
   }

   public final void draw(Graphics var1) {
      if (this.isVisible && this.anim != null) {
         this.anim.draw(var1, this.action, this.frame, this.x, this.y, this.isMirrored);
      }
   }

   public final void drawFlipped(Graphics var1, boolean var2) {
      boolean var4 = false;
      if (this.isVisible && this.anim != null) {
         this.anim.drawFlipped(var1, this.action, this.frame, this.x, this.y, var2, false);
      }
   }

   public final void drawAt(Graphics var1, int var2, int var3) {
      if (this.isVisible && this.anim != null) {
         this.anim.draw(var1, this.action, this.frame, (short)var2, (short)var3, this.isMirrored);
      }
   }

   public final void drawDebugBoxes(Graphics var1) {
      short[] var2 = this.getBoxesAt(this.action, this.frame, 1);
      var1.setColor(16711680);

      for (byte var3 = 0; var2 != null && var3 < var2.length; var3 += 4) {
         var1.drawRect(var2[var3], var2[var3 + 1], var2[var3 + 2], var2[var3 + 3]);
      }

      var2 = this.getBoxesAt(this.action, this.frame, 2);
      var1.setColor(255);

      for (byte var6 = 0; var2 != null && var6 < var2.length; var6 += 4) {
         var1.drawRect(var2[var6], var2[var6 + 1], var2[var6 + 2], var2[var6 + 3]);
      }

      var2 = this.getBoxesAt(this.action, this.frame, 3);
      var1.setColor(16711935);

      for (byte var7 = 0; var2 != null && var7 < var2.length; var7 += 4) {
         var1.drawRect(var2[var7], var2[var7 + 1], var2[var7 + 2], var2[var7 + 3]);
      }
   }

   public final boolean load(String var1) {
      boolean var2 = this.isMirrored;
      this.anim = ResourceCache.getAnimation(var1, null, null, null, null, null, -1, var2, false, 0);
      return this.anim != null;
   }

   public final boolean loadInGroup(String var1, int var2) {
      boolean var4 = this.isMirrored;
      this.anim = ResourceCache.getAnimation(var1, null, null, null, null, null, -1, var4, false, var2);
      return this.anim != null;
   }

   public final boolean loadRecolored(String var1, String var2, String[] var3, String[] var4, int[] var5, int[] var6, int var7) {
      if (var4 == null && var6 == null) {
         boolean var11 = this.isMirrored;
         this.anim = ResourceCache.getAnimation(var1, var2, null, null, null, null, -1, var11, false, var7);
         return this.anim != null;
      } else {
         this.anim = new AvAnimation();
         if (this.anim.load(var1)) {
            boolean var10 = this.isMirrored;
            AvAnimation var9 = this.anim;
            this.anim.loadImages(var2, var3, var4, var5, var6, -1, var10, false, var7);
            return true;
         } else {
            return false;
         }
      }
   }

   public final void setAction(int var1) {
      if (var1 >= 0) {
         if (var1 <= this.getActionCount() - 1) {
            this.action = (short)var1;
         }

         if (this.frame > this.getActionFrameCount(this.action) - 1 && this.getActionFrameCount(this.action) - 1 >= 0) {
            this.frame = (short)(this.getActionFrameCount(this.action) - 1);
         }
      }
   }

   public final void setFrame(int var1) {
      if (var1 <= this.getActionFrameCount(this.action) - 1) {
         this.frame = (short)var1;
      }
   }
}
