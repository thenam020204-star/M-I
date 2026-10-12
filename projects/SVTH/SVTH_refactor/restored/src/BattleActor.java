import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class BattleActor {
   int slot;
   int targetSlot;
   int skillId;
   byte[] comboPartners;
   public int roleId;
   public short partyUid;
   public int[] stats = new int[21];
   public short spriteW;
   public short spriteH;
   public String name;
   public int moveType = 0;
   public Point attackPos = new Point();
   public int state = 0;
   public boolean showStatusIcon = false;
   byte[] targets;
   byte[] hitFrames;
   public boolean isPresent = true;
   boolean effectDone;
   boolean resultShown;
   boolean castIntroDone = false;
   public boolean isDead = false;
   boolean blowFlag = false;
   boolean hasArrived;
   boolean inCombo = false;
   public boolean hasCounter = false;
   public boolean alwaysCrit = false;
   public boolean isCountering = false;
   boolean isCrit = false;
   boolean halfDamage = false;
   boolean dodged;
   public boolean isDefending;
   private BattleMotion motion = new BattleMotion();
   int moveDir;
   public int[] moveVelocity = new int[]{0, 0};
   public AnimSprite sprite;
   public AnimSprite castPortrait;
   AnimSprite[] effectSprites = null;
   AnimSprite hitEffect;
   AnimSprite defendEffect;
   AnimSprite poisonEffect;
   AnimSprite confuseEffect;
   AnimSprite stunEffect;
   public short[] skillIds;
   public Point pos = new Point();
   public Point homePos = new Point();
   public short[] skillLevels;
   public Skill[] skills;
   public short[] activeSkillIds;
   int expFactor;
   int i_I;
   public byte[] elements;
   int attack;
   int defense;
   int speed;
   short critRate;
   public byte[] baseElements;
   public byte[] poison;
   public byte stunTurns;
   public byte confuseTurns;
   public byte silenceTurns;
   public byte hitsLeft;
   public byte[] attackBuff;
   public byte[] rallyBuff;
   public byte[] defenseDebuff;
   public int hp;
   public int mp;
   public Image missImage;
   int hiddenTicks;
   boolean actionAnimDone;

   public BattleActor() {
      new Point();
      this.i_I = 0;
      this.elements = new byte[4];
      this.critRate = 5;
      this.poison = new byte[]{0, 0};
      this.stunTurns = 0;
      this.confuseTurns = 0;
      this.silenceTurns = 0;
      this.hitsLeft = 0;
      this.attackBuff = new byte[]{0, 0};
      this.rallyBuff = new byte[]{0, 0};
      this.defenseDebuff = new byte[]{0, 0};
      this.hiddenTicks = 0;
      this.actionAnimDone = false;
      this.missImage = ResourceCache.loadImage("/pic/miss.gf");
   }

   public final void setPos(Point var1) {
      this.pos.x = var1.x;
      this.pos.y = var1.y;
   }

   public final int getMoveType() {
      return this.moveType;
   }

   public final void draw(Graphics var1) {
      if (this.isPresent) {
         if (this.state == 0 || this.state == 14 || this.state == 2 || this.state == 13 || this.state == 1 || this.state == 8 || this.state == 11) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
               return;
            }
         } else if (this.state == 9 || this.state == 17) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
               return;
            }
         } else if (this.state == 3) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
            }

            if (this.defendEffect == null) {
               this.state = 0;
               return;
            }

            this.defendEffect.setPosition(this.pos.x, this.pos.y);
            this.defendEffect.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.defendEffect.nextFrame(true);
            }

            if (this.defendEffect.getFrame() >= this.defendEffect.getActionFrameCount(0) - 1) {
               this.defendEffect.setFrame(0);
               this.state = 0;
               return;
            }
         } else if (this.state == 15) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            if (this.slot < 10) {
               if (this.slot == 0) {
                  if (DeviceConfig.modelName.equals("N7610")) {
                     this.sprite.draw(var1);
                  } else {
                     this.sprite.drawFlipped(var1, true);
                  }
               } else if (DeviceConfig.modelName.equals("N7610")) {
                  this.sprite.draw(var1);
               } else {
                  this.sprite.drawFlipped(var1, false);
               }
            } else if (DeviceConfig.modelName.equals("N7610")) {
               this.sprite.draw(var1);
            } else {
               this.sprite.drawFlipped(var1, true);
            }

            if (GameCanvas.instance.frameCounter % 3 == 0) {
               if (DeviceConfig.modelName.equals("N7610")) {
                  this.sprite.nextFrame(true);
                  return;
               }

               this.sprite.nextFrame(true);
               return;
            }
         } else if (this.state == 6 || this.state == 16) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
               return;
            }
         } else if (this.state == 5) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
               return;
            }
         } else if (this.state == 10) {
            this.sprite.setPosition(this.pos.x, this.pos.y);
            this.sprite.draw(var1);
            if (GameCanvas.instance.frameCounter % 3 == 0) {
               this.sprite.nextFrame(true);
            }
         }
      }
   }

   public final void startReturn() {
      this.moveDir = BattleMotion.directionTo(this.pos.x, this.homePos.x);
      this.moveVelocity = BattleMotion.velocity(this.pos.x, this.pos.y, this.homePos.x, this.homePos.y, this.sprite.getActionFrameCount(2));
      this.hasArrived = false;
   }

   public final void setAction(int var1, int var2) {
      this.sprite.setAction(var1);
      this.sprite.setFrame(0);
   }

   public static void setSpriteAction(AnimSprite var0, int var1, int var2) {
      var0.setAction(var1);
      var0.setFrame(0);
   }
}
