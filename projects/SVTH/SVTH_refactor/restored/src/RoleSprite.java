import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class RoleSprite extends SpriteBase {
   public byte renderMode;
   private Image image;
   public AnimSprite animSprite;
   public static byte lastMoveDirection;
   public byte moveSpeed = 4;
   public String name;
   public String facePath;
   public byte direction;
   public static byte statCount = 21;
   public int[] stats;
   public byte jobLevel;
   public byte[] elementValues;
   public byte[][] attrGrowth = new byte[4][5];
   public byte actorType;
   protected short mapWidth;
   protected short mapHeight;
   public short[][] followTrail;
   protected boolean canWander;
   public boolean isFlying;
   public boolean isRiding;
   public boolean isCombatant;
   public Skill[] skills;
   public short[] skillIds;
   public short[] equipIds;
   public Equip[] equips;
   int encounterSteps = -1;
   protected boolean isWandering;
   public byte savedDirection;
   public String battleSpritePath;
   public boolean noClip;
   private short[] footBox;
   private short[] bounds;
   public short petUid;
   public boolean isAnimating = true;
   private short targetX;
   private short targetY;
   private byte targetDirection;
   boolean isMovingToTarget;
   boolean isPlayingAction;
   public int actionRepeat;
   public int actionId;
   int returnAction;
   private short[] stepBoxes;
   private long wanderLastTime;
   private long wanderInterval = 2000L;
   private boolean isWanderStepping = false;

   public RoleSprite(Image var1) {
      this.renderMode = 0;
      this.image = var1;
      this.headHeight = (short)this.image.getHeight();
      this.bounds = new short[]{
         (short)(-this.image.getWidth() / 2), (short)(-this.image.getHeight()), (short)this.image.getWidth(), (short)this.image.getHeight()
      };
      this.width = this.bounds[2];
      this.height = this.bounds[3];
      this.footBox = new short[]{(short)(-this.image.getWidth() / 2), -8, (short)this.image.getWidth(), 8};
   }

   public RoleSprite(AnimSprite var1) {
      this.renderMode = 1;
      this.animSprite = var1;
      if (this.animSprite != null) {
         this.bounds = this.animSprite.getBounds();
      }

      short[] var2;
      if ((var2 = this.animSprite.getBoxesAt(0, 0, 2)) != null) {
         this.headHeight = var2[3];
      } else {
         this.headHeight = 40;
      }

      this.width = this.bounds[2];
      this.height = this.bounds[3];
   }

   public final void draw(Graphics var1) {
      if (this.isVisible) {
         switch (this.renderMode) {
            case 0:
               if (this.image != null) {
                  var1.drawImage(this.image, this.x, this.y, 33);
                  return;
               }
               break;
            case 1:
               if (this.animSprite != null) {
                  this.animSprite.x = (short)this.x;
                  this.animSprite.y = (short)this.y;
                  this.animSprite.draw(var1);
                  if (this.isAnimating && GameCanvas.instance.frameCounter % 2 == 0) {
                     this.animSprite.nextFrame(true);
                  }
               }
         }
      }
   }

   public RoleSprite() {
      this.direction = 2;
   }

   public final short[] getCollisionBoxes(int var1) {
      switch (this.renderMode) {
         case 0:
            if (this.image != null) {
               short[] var2;
               (var2 = new short[4])[0] = (short)(this.x - this.image.getWidth() / 2);
               var2[1] = (short)(this.y - this.image.getHeight() / 2);
               var2[2] = (short)this.image.getWidth();
               var2[3] = (short)(this.image.getHeight() / 2);
               this.height = (short)this.image.getHeight();
               return var2;
            }

            return null;
         case 1:
            if (this.animSprite != null) {
               this.animSprite.x = this.x;
               this.animSprite.y = this.y;
               return this.animSprite.getBoxesAt(0, 0, var1);
            }

            return null;
         default:
            return null;
      }
   }

   public final void setDirection(int var1) {
      if (this.renderMode == 1) {
         this.direction = (byte)var1;
         if (this.animSprite != null) {
            this.animSprite.setAction(var1);
            return;
         }
      } else if (this.renderMode == 0) {
         this.direction = (byte)var1;
      }
   }

   public final void updateMoveTo() {
      if (this.isMovingToTarget) {
         if (this.targetX - this.x < -this.moveSpeed) {
            this.setDirection(2);
            this.stepForward();
            return;
         }

         if (this.targetX - this.x > this.moveSpeed) {
            this.setDirection(3);
            this.stepForward();
            return;
         }

         if (this.targetY - this.y < -this.moveSpeed) {
            this.setDirection(1);
            this.stepForward();
            return;
         }

         if (this.targetY - this.y > this.moveSpeed) {
            this.setDirection(0);
            this.stepForward();
            return;
         }

         this.animSprite.setFrame(0);
         this.isMovingToTarget = false;
         this.moveSpeed = 4;
         if (this.targetDirection >= 0 && this.targetDirection <= 3) {
            this.setDirection(this.targetDirection);
            this.setIdleAnim();
            if (DeviceConfig.allowDebug) {
               System.out.println(this.roleId + " quay hướng " + this.targetDirection);
            }
         }

         GameCanvas.instance.world.script.advance(5);
      }
   }

   public final void stepForward() {
      if (this.renderMode == 1) {
         if (this.animSprite != null) {
            this.animSprite.x = this.x;
            this.animSprite.y = this.y;
            this.stepBoxes = this.getCollisionBoxes(1);
            if (this.stepBoxes != null) {
               for (byte var1 = 0; var1 < this.stepBoxes.length; var1 += 4) {
                  switch (this.direction) {
                     case 0:
                        this.stepBoxes[var1 + 1] = (short)(this.stepBoxes[var1 + 1] + this.moveSpeed);
                        break;
                     case 1:
                        this.stepBoxes[var1 + 1] = (short)(this.stepBoxes[var1 + 1] - this.moveSpeed);
                        break;
                     case 2:
                        this.stepBoxes[var1] = (short)(this.stepBoxes[var1] - this.moveSpeed);
                        break;
                     case 3:
                        this.stepBoxes[var1] = (short)(this.stepBoxes[var1] + this.moveSpeed);
                  }
               }
            }

            boolean var4;
            if (!this.isMovingToTarget) {
               label120:
               if (this.isBlocked(this.stepBoxes)) {
                  if (this.noClip) {
                     short[] var3 = this.stepBoxes;
                     byte var2 = 0;

                     boolean var10000;
                     while (true) {
                        if (var2 >= var3.length) {
                           var10000 = false;
                           break;
                        }

                        if (var3[var2] < 0
                           || var3[var2] + var3[var2 + 2] > World.mapWidth
                           || var3[var2 + 1] < 0
                           || var3[var2 + 1] + var3[var2 + 3] > World.mapHeight) {
                           var10000 = true;
                           break;
                        }

                        var2 += 4;
                     }

                     if (!var10000) {
                        var4 = 1;
                        break label120;
                     }
                  }

                  var4 = 0;
               } else {
                  var4 = 1;
               }
            } else {
               var4 = 1;
            }

            if (!var4) {
               if (this.roleId != 1 && this.roleId != 2 && this.roleId != 3) {
                  this.setIdleAnim();
               }

               return;
            }

            switch (this.direction) {
               case 0:
                  this.y = (short)(this.y + this.moveSpeed);
                  break;
               case 1:
                  this.y = (short)(this.y - this.moveSpeed);
                  break;
               case 2:
                  this.x = (short)(this.x - this.moveSpeed);
                  break;
               case 3:
                  this.x = (short)(this.x + this.moveSpeed);
            }

            lastMoveDirection = this.direction;
            if (this != GameData.party[0]
               || GameData.sceneEnemyCountRange == null
               || GameCanvas.instance.world.script.isRunning
               || this.isFlying
               || this.isRiding) {
               return;
            }

            if (this.encounterSteps == -1) {
               Random var5;
               (var5 = new Random()).setSeed(System.currentTimeMillis());
               var4 = Math.abs(var5.nextInt()) / 42949672;
               this.encounterSteps = var4 + 100;
            } else if (this.encounterSteps > 0) {
               this.encounterSteps--;
            } else {
               this.encounterSteps = -1;
               if (GameCanvas.instance.world.worldState != 2 || GameCanvas.instance.world.script.isRunning || this.isMovingToTarget) {
                  return;
               }

               GameCanvas.instance.world.triggerRandomEncounter(false);
            }
         }
      } else if (this.image != null) {
         switch (this.direction) {
            case 0:
               this.y = (short)(this.y + this.moveSpeed);
               return;
            case 1:
               this.y = (short)(this.y - this.moveSpeed);
               return;
            case 2:
               this.x = (short)(this.x - this.moveSpeed);
               return;
            case 3:
               this.x = (short)(this.x + this.moveSpeed);
         }
      }
   }

   public final void setIdleAnim() {
      if (this.renderMode == 1 && this.animSprite.getActionCount() == 8) {
         if (this.animSprite.getAction() == 0) {
            this.animSprite.setAction(4);
            return;
         }

         if (this.animSprite.getAction() == 1) {
            this.animSprite.setAction(5);
            return;
         }

         if (this.animSprite.getAction() == 2) {
            this.animSprite.setAction(6);
            return;
         }

         if (this.animSprite.getAction() == 3) {
            this.animSprite.setAction(7);
         }
      }
   }

   public final void moveTo(int var1, int var2, int var3) {
      this.targetX = (short)var1;
      this.targetY = (short)var2;
      this.targetDirection = (byte)var3;
      this.isMovingToTarget = true;
   }

   public final void initFollowTrail() {
      this.followTrail = null;
      GameData.updateFollowSpacing(this.moveSpeed);
      this.followTrail = new short[(GameData.party.length - 1) * GameData.followStepGap + 1][3];
      this.followTrail[0][0] = this.x;
      this.followTrail[0][1] = this.y;
      this.followTrail[0][2] = (short)this.direction;

      for (int var1 = 1; var1 < this.followTrail.length; var1++) {
         this.followTrail[var1][0] = this.followTrail[var1 - 1][0];
         this.followTrail[var1][1] = this.followTrail[var1 - 1][1];
         this.followTrail[var1][2] = this.followTrail[var1 - 1][2];
         switch (this.direction) {
            case 0:
               this.followTrail[var1][1] = (short)(this.followTrail[var1 - 1][1] - this.moveSpeed);
               break;
            case 1:
               this.followTrail[var1][1] = (short)(this.followTrail[var1 - 1][1] + this.moveSpeed);
               break;
            case 2:
               this.followTrail[var1][0] = (short)(this.followTrail[var1 - 1][0] + this.moveSpeed);
               break;
            case 3:
               this.followTrail[var1][0] = (short)(this.followTrail[var1 - 1][0] - this.moveSpeed);
         }
      }
   }

   public final void resetFollowTrail() {
      GameData.updateFollowSpacing(this.moveSpeed);
      short[][] var1;
      (var1 = new short[(GameData.party.length - 1) * GameData.followStepGap + 1][3])[0][0] = this.followTrail[0][0];
      var1[0][1] = this.followTrail[0][1];
      var1[0][2] = this.followTrail[0][2];

      for (int var2 = 1; var2 < var1.length; var2++) {
         var1[var2][0] = this.x;
         var1[var2][1] = this.y;
         var1[var2][2] = (short)this.direction;
         switch (this.direction) {
            case 0:
               var1[var2][1] = (short)(var1[var2 - 1][1] - this.moveSpeed);
               break;
            case 1:
               var1[var2][1] = (short)(var1[var2 - 1][1] + this.moveSpeed);
               break;
            case 2:
               var1[var2][0] = (short)(var1[var2 - 1][0] + this.moveSpeed);
               break;
            case 3:
               var1[var2][0] = (short)(var1[var2 - 1][0] - this.moveSpeed);
         }
      }

      this.followTrail = null;
      this.followTrail = var1;
   }

   public final int getSpeed() {
      int var1 = 0 + this.stats[9];
      int var2 = 0;

      for (int var3 = 0; var3 < 6; var3++) {
         if (this.equips[var3] != null) {
            var2 += this.equips[var3].getTotalSpeed();
         }
      }

      int var4;
      return var4 = var1 + var2;
   }

   public final int getAttack() {
      int var1 = 0 + this.stats[7];
      int var2 = 0;

      for (int var3 = 0; var3 < 6; var3++) {
         if (this.equips[var3] != null) {
            var2 += this.equips[var3].getTotalAttack();
         }
      }

      int var4;
      return var4 = var1 + var2;
   }

   public final int getDefense() {
      int var1 = 0 + this.stats[8];
      int var2 = 0;

      for (int var3 = 0; var3 < 6; var3++) {
         if (this.equips[var3] != null) {
            var2 += this.equips[var3].getTotalDefense();
         }
      }

      int var4;
      return var4 = var1 + var2;
   }

   public final int getMaxHp() {
      int var1 = 0 + this.stats[4];
      int var2 = 0;

      for (int var3 = 0; var3 < 6; var3++) {
         if (this.equips[var3] != null) {
            var2 += this.equips[var3].getTotalHp();
         }
      }

      int var4;
      return var4 = var1 + var2;
   }

   public final int getFreePoints() {
      return this.stats[16];
   }

   public final int getAttrPoints1() {
      int var1;
      return var1 = 0 + this.stats[17];
   }

   public final int getAttrPoints2() {
      int var1;
      return var1 = 0 + this.stats[18];
   }

   public final int getAttrPoints3() {
      int var1;
      return var1 = 0 + this.stats[19];
   }

   public final int getAttrPoints4() {
      int var1;
      return var1 = 0 + this.stats[20];
   }

   public final int getMaxMp() {
      int var1 = 0 + this.stats[6];
      int var2 = 0;

      for (int var3 = 0; var3 < 6; var3++) {
         if (this.equips[var3] != null) {
            var2 += this.equips[var3].getTotalMp();
         }
      }

      int var4;
      return var4 = var1 + var2;
   }

   private boolean isBlocked(short[] var1) {
      short[] var3 = this.stepBoxes;
      TileMap var2 = GameCanvas.instance.world.tileMap;
      if (GameCanvas.instance.world.tileMap.hitsBlock(var3, 1)) {
         return true;
      } else {
         for (byte var4 = 0; var4 < var1.length; var4 += 4) {
            if (var1[var4] < 0 || var1[var4] + var1[var4 + 2] > World.mapWidth || var1[var4 + 1] < 0 || var1[var4 + 1] + var1[var4 + 3] > World.mapHeight) {
               return true;
            }
         }

         if (GameCanvas.instance.world.sceneLayer != null) {
            if (this.isWandering) {
               if (!GameData.party[0].isFlying) {
                  if (GameCanvas.instance.world.sceneLayer.collides(var1, this, 1)) {
                     return true;
                  }
               } else if (GameCanvas.instance.world.sceneLayer.collidesExcept(var1, new SpriteBase[]{this}, 1)) {
                  return true;
               }
            } else if (!GameData.party[0].isFlying && GameCanvas.instance.world.sceneLayer.collidesExcept(var1, GameData.party, 1)) {
               return true;
            }
         }

         return false;
      }
   }

   public final void stopWander() {
      this.isWandering = false;
   }

   public final void resumeWander() {
      if (this.canWander && !this.isWandering) {
         this.isWandering = true;
      }
   }

   public final void updateWander() {
      if (this.canWander && this.isWandering) {
         if (this.wanderLastTime == 0L) {
            this.wanderLastTime = System.currentTimeMillis();
         }

         if (System.currentTimeMillis() - this.wanderLastTime > this.wanderInterval) {
            this.wanderLastTime = System.currentTimeMillis();
            this.wanderInterval = (long)GameUtil.randomBetween(1000, 4000);
            if (this.isWanderStepping) {
               this.isWanderStepping = false;
               this.setIdleAnim();
            } else {
               int var1;
               if ((var1 = GameUtil.pickRandomExcept(new int[]{0, 1, 2, 3, 4}, this.direction)) < 4) {
                  this.isWanderStepping = true;
                  this.setDirection(var1);
               } else {
                  this.isWanderStepping = false;
               }
            }
         }

         if (this.isWanderStepping) {
            this.stepForward();
         }
      }
   }

   public final void startFlying() {
      if (!GameCanvas.instance.world.battlePending && GameCanvas.instance.world.loadFinished) {
         if (GameData.party[0].roleId == 1) {
            GameData.flyAnimPath = "/av/long1.av";
         } else {
            GameData.flyAnimPath = "/av/long2.av";
         }

         this.noClip = true;
         GameData.party[0].moveSpeed = 4;
         if (!DeviceConfig.modelName.equals("N73") && !DeviceConfig.modelName.equals("N7610")) {
            GameData.party[0].animSprite = null;
            GameData.party[0].animSprite = new AnimSprite();
            GameData.party[0].animSprite.loadInGroup(GameData.flyAnimPath, -1);
            GameData.party[0].animSprite.setAction(this.direction);
         } else {
            GameData.party[0].animSprite = null;
            GameData.party[0].animSprite = new AnimSprite();
            GameData.party[0].animSprite.loadInGroup(GameData.flyAnimPath, 1);
            GameData.party[0].animSprite.setAction(this.direction);
         }

         if (GameCanvas.instance.world.sceneLayer != null) {
            RoleSprite var2 = GameData.party[0];
            SceneLayer var1 = GameCanvas.instance.world.sceneLayer;
            GameCanvas.instance.world.sceneLayer.removeActor(var2);
            if (var1.skyActors == null) {
               var1.skyActors = new SpriteBase[1];
            } else {
               SpriteBase[] var3 = new SpriteBase[var1.skyActors.length + 1];
               System.arraycopy(var1.skyActors, 0, var3, 0, var1.skyActors.length);
               var1.skyActors = var3;
            }

            var1.skyActors[var1.skyActors.length - 1] = var2;
         }

         this.isFlying = true;
         GameCanvas.instance.world.nearChest = false;
      }
   }

   public final void land() {
      if (!GameCanvas.instance.world.battlePending && GameCanvas.instance.world.loadFinished) {
         if ((this.isBlocked(this.getCollisionBoxes(1)) || GameCanvas.instance.world.sceneLayer.collides(this.getCollisionBoxes(1), this, 1))
            && !GameCanvas.instance.world.script.isRunning) {
            GameCanvas.instance.showToast("Không thể hạ cánh tại đây", 8);
         } else {
            this.noClip = false;
            System.out.println("Kết thúc bay");
            this.isFlying = false;
            GameData.party[0].moveSpeed = 4;
            if (!DeviceConfig.modelName.equals("N73") && !DeviceConfig.modelName.equals("N7610")) {
               GameData.party[0].animSprite.loadInGroup(GameData.roleAnimPaths[this.roleId - 1], -1);
            } else {
               GameData.party[0].animSprite.loadInGroup(GameData.roleAnimPaths[this.roleId - 1], 1);
            }

            RoleSprite var2 = GameData.party[0];
            SceneLayer var1 = GameCanvas.instance.world.sceneLayer;
            GameCanvas.instance.world.sceneLayer.removeSkyActor(var2);
            var1.addActor(var2);
            GameData.party[0].setIdleAnim();
         }
      }
   }

   public final short[] getBounds() {
      return this.bounds;
   }

   public final void drawDebugBoxes(Graphics var1) {
      var1.setColor(16711680);
      if (this.renderMode == 1) {
         if (this.animSprite != null) {
            this.animSprite.setPosition(this.x, this.y);
            this.animSprite.drawDebugBoxes(var1);
            return;
         }
      } else if (this.renderMode == 0) {
         for (byte var2 = 0; this.footBox != null && var2 < this.footBox.length; var2 += 4) {
            var1.drawRect(this.x + this.footBox[var2], this.y + this.footBox[var2 + 1], this.footBox[var2 + 2], this.footBox[var2 + 3]);
         }
      }
   }

   public final void updateScriptAction() {
      if (this.animSprite.getFrame() < this.animSprite.getActionFrameCount(this.actionId) - 1) {
         this.animSprite.nextFrame(true);
      } else if (this.actionRepeat <= 1) {
         this.isPlayingAction = false;
         this.animSprite.setAction(this.returnAction);
         GameCanvas.instance.world.script.advance(2);
      } else {
         this.actionRepeat--;
         this.animSprite.setFrame(0);
      }
   }

   public static RoleSprite findTalkableNear(RoleSprite var0) {
      if (GameCanvas.instance.world.sceneLayer != null) {
         for (int var1 = 0; GameCanvas.instance.world.sceneLayer.actors != null && var1 < GameCanvas.instance.world.sceneLayer.actors.length; var1++) {
            if (GameCanvas.instance.world.sceneLayer.actors[var1] != null
               && GameCanvas.instance.world.sceneLayer.actors[var1].kind == 1
               && !GameUtil.isInTeam((RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var1])) {
               short[] var2 = var0.getCollisionBoxes(1);
               short[] var3;
               if ((var3 = GameCanvas.instance.world.sceneLayer.actors[var1].getCollisionBoxes(1)) == null) {
                  System.out.println("Lỗi!!!" + GameCanvas.instance.world.sceneLayer.actors[var1].roleId + " nhân vật chưa có khung va chạm");
               }

               if (Utils.rectsOverlap(var2[0], var2[1], var2[2], var2[3], var3[0] - 5, var3[1] - 5, var3[2] + 10, var3[3] + 10)) {
                  return (RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var1];
               }
            }
         }
      }

      return null;
   }

   public static TreasureChest findChestAhead(RoleSprite var0) {
      if (GameCanvas.instance.world.sceneLayer != null) {
         for (int var1 = 0; GameCanvas.instance.world.sceneLayer.actors != null && var1 < GameCanvas.instance.world.sceneLayer.actors.length; var1++) {
            if (GameCanvas.instance.world.sceneLayer.actors[var1] != null
               && var0 != GameCanvas.instance.world.sceneLayer.actors[var1]
               && GameCanvas.instance.world.sceneLayer.actors[var1].kind == 12) {
               short[] var3 = null;
               short[] var4 = var0.getCollisionBoxes(1);
               switch (var0.direction) {
                  case 0:
                     var3 = new short[]{var4[0], (short)(var4[1] + var4[3]), var4[2], (short)var4[3]};
                     break;
                  case 1:
                     var3 = new short[]{var4[0], (short)(var4[1] - var4[3]), var4[2], (short)var4[3]};
                     break;
                  case 2:
                     var3 = new short[]{(short)(var4[0] - var4[2]), var4[1], (short)var4[2], var4[3]};
                     break;
                  case 3:
                     var3 = new short[]{(short)(var4[0] + var4[2]), var4[1], (short)var4[2], var4[3]};
               }

               if (Utils.rectsOverlap(var3, GameCanvas.instance.world.sceneLayer.actors[var1].getCollisionBoxes(1))) {
                  return (TreasureChest)GameCanvas.instance.world.sceneLayer.actors[var1];
               }
            }
         }
      }

      return null;
   }

   public static RoleSprite findRoleOnMap(int var0) {
      for (int var1 = 0; GameCanvas.instance.world.sceneLayer.actors != null && var1 < GameCanvas.instance.world.sceneLayer.actors.length; var1++) {
         if (GameCanvas.instance.world.sceneLayer.actors[var1] != null
            && GameCanvas.instance.world.sceneLayer.actors[var1].roleId == var0
            && GameCanvas.instance.world.sceneLayer.actors[var1].kind == 1) {
            return (RoleSprite)GameCanvas.instance.world.sceneLayer.actors[var1];
         }
      }

      return null;
   }
}
