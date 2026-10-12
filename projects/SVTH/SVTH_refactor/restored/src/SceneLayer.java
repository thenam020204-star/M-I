import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class SceneLayer {
   static SceneLayer instance;
   private short viewX;
   private short viewY;
   private short viewW;
   private short viewH;
   private Building[] buildings;
   public SpriteBase[] actors;
   private short[][] midPlacements;
   private short[][] groundPlacements;
   private short[][] topPlacements;
   public SpriteBase[] skyActors;
   private SpriteBase[] groundActors;
   private String imageDir;
   private String animDir;
   private String[] resourceNames;
   private short[][] resourceRects;
   private int phase;
   private int[] occurredEvents;
   private int[][] midEventConds;
   private int[][] groundEventConds;
   private int[][] topEventConds;
   private short[][] midPhases;
   private short[][] groundPhases;
   private short[][] topPhases;
   private boolean[] midVisible;
   private boolean[] groundVisible;
   private boolean[] topVisible;

   public SceneLayer() {
      instance = this;
   }

   public final void addActor(SpriteBase var1) {
      if (this.actors == null) {
         this.actors = new SpriteBase[1];
      } else {
         SpriteBase[] var2 = new SpriteBase[this.actors.length + 1];
         System.arraycopy(this.actors, 0, var2, 0, this.actors.length);
         this.actors = var2;
      }

      this.actors[this.actors.length - 1] = var1;
   }

   public final boolean collides(short[] var1, SpriteBase var2, int var3) {
      return this.collidesExcept(var1, new SpriteBase[]{var2}, 1);
   }

   public final boolean collidesExcept(short[] var1, SpriteBase[] var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         for (int var4 = 0; this.buildings != null && this.midPlacements != null && var4 < this.midPlacements.length; var4++) {
            short[] var6;
            if (this.midPlacements[var4] != null
               && this.isMidVisible(var4)
               && this.buildings[this.midPlacements[var4][0]].getBounds() != null
               && (var6 = this.buildings[this.midPlacements[var4][0]].getBoxesAt(this.midPlacements[var4][1], this.midPlacements[var4][2], var3)) != null) {
               for (byte var5 = 0; var5 < var1.length; var5 += 4) {
                  for (byte var7 = 0; var7 < var6.length; var7 += 4) {
                     if (Utils.rectsOverlap(
                        var1[var5], var1[var5 + 1], var1[var5 + 2], var1[var5 + 3], var6[var7], var6[var7 + 1], var6[var7 + 2], var6[var7 + 3]
                     )) {
                        return true;
                     }
                  }
               }
            }
         }

         for (int var8 = 0; this.actors != null && var8 < this.actors.length; var8++) {
            if (this.actors[var8] != null && this.actors[var8].isVisible && this.actors[var8].isSolid) {
               boolean var9 = true;

               for (int var11 = 0; var2 != null && var11 < var2.length; var11++) {
                  if (var2[var11] == this.actors[var8]) {
                     var9 = false;
                     break;
                  }
               }

               short[] var10;
               if (var9 && this.actors[var8].getBounds() != null && (var10 = this.actors[var8].getCollisionBoxes(var3)) != null) {
                  for (byte var13 = 0; var13 < var1.length - 3; var13 += 4) {
                     for (byte var12 = 0; var12 < var10.length - 3; var12 += 4) {
                        if (Utils.rectsOverlap(
                           var1[var13], var1[var13 + 1], var1[var13 + 2], var1[var13 + 3], var10[var12], var10[var12 + 1], var10[var12 + 2], var10[var12 + 3]
                        )) {
                           return true;
                        }
                     }
                  }
               }
            }
         }

         return false;
      }
   }

   public final void clearBuildings() {
      this.midPlacements = null;
      this.groundPlacements = null;
      this.topPlacements = null;
      this.buildings = null;
   }

   public final void compactActors() {
      if (this.groundActors != null) {
         this.sortByY(this.groundActors);

         for (int var1 = this.groundActors.length - 1; var1 >= 0; var1--) {
            if (this.groundActors[var1] != null) {
               if (var1 < this.groundActors.length - 1) {
                  SpriteBase[] var2 = new SpriteBase[var1 + 1];
                  System.arraycopy(this.groundActors, 0, var2, 0, var2.length);
                  this.groundActors = var2;
                  return;
               }

               return;
            }
         }
      }

      if (this.actors != null) {
         this.sortByY(this.actors);

         for (int var3 = this.actors.length - 1; var3 >= 0; var3--) {
            if (this.actors[var3] != null) {
               if (var3 < this.actors.length - 1) {
                  SpriteBase[] var4 = new SpriteBase[var3 + 1];
                  System.arraycopy(this.actors, 0, var4, 0, var4.length);
                  this.actors = var4;
                  return;
               }

               return;
            }
         }
      }

      if (this.skyActors != null) {
         this.sortByY(this.skyActors);

         for (int var5 = this.skyActors.length - 1; var5 >= 0; var5--) {
            if (this.skyActors[var5] != null) {
               if (var5 < this.skyActors.length - 1) {
                  SpriteBase[] var6 = new SpriteBase[var5 + 1];
                  System.arraycopy(this.skyActors, 0, var6, 0, var6.length);
                  this.skyActors = var6;
                  return;
               }

               return;
            }
         }
      }
   }

   public final void loadBuildings(String var1, String var2, String var3) {
      boolean var12 = false;
      byte var13 = -1;
      SceneLayer var14 = null;
      var14 = null;
      var14 = null;
      var14 = null;
      if (this.readBuildingFile(var1, var2, var3)) {
         byte var7 = 0;
         byte var6 = -1;
         Object var5 = null;
         Object var23 = null;
         Object var22 = null;
         Object var20 = null;
         var14 = this;
         if (this.resourceNames != null && this.resourceNames.length > 0) {
            this.buildings = new Building[this.resourceNames.length];

            for (int var8 = 0; var14.resourceNames != null && var8 < var14.resourceNames.length; var8++) {
               if (var14.resourceNames[var8] != null && !var14.resourceNames[var8].equals("")) {
                  Object var9 = var14.resourceNames[var8];
                  if (var14.resourceNames[var8].toLowerCase().endsWith(".gf") || var14.resourceNames[var8].toLowerCase().endsWith(".png")) {
                     Image var24 = null;
                     if (var20 != null) {
                        for (int var11 = 0; var20 != null && var11 < ((Object[])var20).length; var11++) {
                           if (((Object[])var20)[var11].equals(var14.resourceNames[var8])) {
                              var9 = ((Object[])var22)[var11];
                              break;
                           }
                        }
                     } else if (var22 != null && var8 < ((Object[])var22).length && ((Object[])var22)[var8] != null) {
                        var9 = ((Object[])var22)[var8];
                     }

                     if (var5 == null && var6 < 0) {
                        var24 = ResourceCache.loadImage(var14.imageDir + var14.resourceNames[var8], var7);
                     } else {
                        byte[] var25;
                        if ((var25 = ResourceCache.loadBytes(var14.imageDir + var9, var7)) != null) {
                           if (var5 != null) {
                              var25 = PngRecolor.replaceColors(var25, (int[])var23, (int[])var5);
                           }

                           if (var6 >= 0) {
                              var25 = PngRecolor.scaleBrightness(var25, var6);
                           }

                           var24 = Utils.createImage(var25);
                        }
                     }

                     var14.buildings[var8] = new Building(var24, var14.resourceRects[var8]);
                  } else if (var14.resourceNames[var8].toLowerCase().endsWith(".av")) {
                     AnimSprite var10;
                     (var10 = new AnimSprite())
                        .loadRecolored(
                           var14.imageDir + var14.resourceNames[var8], var14.animDir, (String[])var20, (String[])var22, (int[])var23, (int[])var5, var7
                        );
                     var14.buildings[var8] = new Building(var10, var14.resourceRects[var8]);
                  }
               }
            }
         }

         var14.resourceNames = null;
         var14.resourceRects = null;
      }
   }

   public final void loadBuildingsRecolored(
      String var1, String var2, String var3, String[] var4, String[] var5, String[] var6, int[][] var7, int[][] var8, int var9, int var10
   ) {
      if (this.readBuildingFile(var1, var2, var3)) {
         byte var24 = -1;
         byte var23 = -1;
         int[][] var22 = var8;
         int[][] var21 = var7;
         var4 = var6;
         Object var19 = null;
         Object var18 = null;
         SceneLayer var17 = this;
         if (this.resourceNames != null && this.resourceNames.length > 0) {
            this.buildings = new Building[this.resourceNames.length];

            for (int var25 = 0; var17.resourceNames != null && var25 < var17.resourceNames.length; var25++) {
               if (var17.resourceNames[var25] != null && !var17.resourceNames[var25].equals("")) {
                  Object var26 = var17.resourceNames[var25];
                  if (!var17.resourceNames[var25].toLowerCase().endsWith(".gf") && !var17.resourceNames[var25].toLowerCase().endsWith(".png")) {
                     if (var17.resourceNames[var25].toLowerCase().endsWith(".av")) {
                        AnimSprite var27 = new AnimSprite();
                        if (var4 != null && (var22 != null || var23 >= 0)) {
                           AvAnimation var30;
                           (var30 = new AvAnimation()).load(var17.imageDir + var17.resourceNames[var25]);
                           var30.imageDir = var17.animDir;
                           var30.loadImageBytes((String[])var18, (String[])var19, var24);

                           for (int var32 = 0; var32 < var4.length; var32++) {
                              if (var32 < var21.length && var32 < var22.length) {
                                 var30.recolorImage(var4[var32], var21[var32], var22[var32]);
                              }
                           }

                           var30.createImages();
                           var27.anim = var30;
                        } else {
                           String var10001 = var17.imageDir + var17.resourceNames[var25];
                           String var31 = var17.animDir;
                           String var29 = var10001;
                           var27.loadRecolored(var29, var31, (String[])var18, (String[])var19, null, null, var24);
                        }

                        var17.buildings[var25] = new Building(var27, var17.resourceRects[var25]);
                     }
                  } else {
                     Image var11 = null;
                     if (var18 != null) {
                        for (int var12 = 0; var18 != null && var12 < ((Object[])var18).length; var12++) {
                           if (((Object[])var18)[var12].equals(var17.resourceNames[var25])) {
                              var26 = ((Object[])var19)[var12];
                              break;
                           }
                        }
                     } else if (var19 != null && var25 < ((Object[])var19).length && ((Object[])var19)[var25] != null) {
                        var26 = ((Object[])var19)[var25];
                     }

                     if (var4 != null && (var22 != null || var23 >= 0)) {
                        boolean var28 = false;

                        for (int var13 = 0; var4 != null && var13 < var4.length; var13++) {
                           if (var26.equals(var4[var13]) && var21 != null && var22 != null && var13 < var21.length && var13 < var22.length) {
                              var28 = true;
                              byte[] var14;
                              if ((var14 = ResourceCache.loadBytes(var17.imageDir + var26, var24)) != null) {
                                 if (var22 != null) {
                                    var14 = PngRecolor.replaceColors(var14, var21[var13], var22[var13]);
                                 }

                                 if (var23 >= 0) {
                                    var14 = PngRecolor.scaleBrightness(var14, var23);
                                 }

                                 var11 = Utils.createImage(var14);
                              }
                           }
                        }

                        if (!var28) {
                           var11 = ResourceCache.loadImage(var17.imageDir + var26, var24);
                        }
                     } else {
                        var11 = ResourceCache.loadImage(var17.imageDir + var26, var24);
                     }

                     var17.buildings[var25] = new Building(var11, var17.resourceRects[var25]);
                  }
               }
            }
         }

         var17.resourceNames = null;
         var17.resourceRects = null;
      }
   }

   private boolean readBuildingFile(String var1, String var2, String var3) {
      this.imageDir = var2;
      if (var3 == null) {
         if (this.animDir == null) {
            int var4 = this.imageDir.lastIndexOf(47);
            this.animDir = this.imageDir.substring(0, var4 + 1);
         }
      } else if (var3.startsWith("/")) {
         this.animDir = var3;
      } else {
         int var30 = this.imageDir.lastIndexOf(47);
         this.animDir = this.imageDir.substring(0, var30 + 1) + var3;
      }

      if (!this.animDir.endsWith("/")) {
         this.animDir = this.animDir + "/";
      }

      this.resourceNames = null;
      this.resourceRects = null;
      this.midPlacements = null;
      this.groundPlacements = null;
      this.topPlacements = null;
      this.midVisible = null;
      this.groundVisible = null;
      this.topVisible = null;
      byte[] var7;
      if (DeviceConfig.cacheMap) {
         var7 = ResourceCache.loadBytes(var1, DeviceConfig.cacheMapGroup);
      } else {
         var7 = Utils.readResource(var1);
      }

      if (var7 == null) {
         return false;
      } else {
         ByteArrayInputStream var8 = new ByteArrayInputStream(var7);
         DataInputStream var9 = new DataInputStream(var8);

         try {
            short var10 = var9.readShort();
            this.resourceNames = new String[var10];

            for (int var21 = 0; var21 < var10; var21++) {
               this.resourceNames[var21] = var9.readUTF();
            }

            short var22 = var9.readShort();
            this.resourceRects = new short[var22][];

            for (int var11 = 0; var11 < var22; var11++) {
               byte var31;
               if ((var31 = var9.readByte()) > 0) {
                  this.resourceRects[var11] = new short[var31];

                  for (int var5 = 0; var5 < var31; var5++) {
                     this.resourceRects[var11][var5] = var9.readShort();
                  }
               }
            }

            short var12;
            if ((var12 = var9.readShort()) > 0) {
               this.midPlacements = new short[var12][];

               for (int var32 = 0; var32 < var12; var32++) {
                  this.midPlacements[var32] = new short[3];

                  for (int var40 = 0; var40 < 3; var40++) {
                     this.midPlacements[var32][var40] = var9.readShort();
                  }
               }
            }

            short var33;
            if ((var33 = var9.readShort()) > 0) {
               this.groundPlacements = new short[var33][];

               for (int var41 = 0; var41 < var33; var41++) {
                  this.groundPlacements[var41] = new short[3];

                  for (int var13 = 0; var13 < 3; var13++) {
                     this.groundPlacements[var41][var13] = var9.readShort();
                  }
               }
            }

            short var42;
            if ((var42 = var9.readShort()) > 0) {
               this.topPlacements = new short[var42][];

               for (int var14 = 0; var14 < var42; var14++) {
                  this.topPlacements[var14] = new short[3];

                  for (int var23 = 0; var23 < 3; var23++) {
                     this.topPlacements[var14][var23] = var9.readShort();
                  }
               }
            }

            short var15;
            if ((var15 = var9.readShort()) > 0) {
               this.midEventConds = new int[var15][];

               for (int var24 = 0; var24 < var15; var24++) {
                  var33 = var9.readShort();

                  for (int var43 = 0; var43 < var33; var43++) {
                     this.midEventConds[var24] = Utils.append(this.midEventConds[var24], var9.readInt());
                  }
               }
            }

            short var25;
            if ((var25 = var9.readShort()) > 0) {
               this.groundEventConds = new int[var25][];

               for (int var35 = 0; var35 < var25; var35++) {
                  var42 = var9.readShort();

                  for (int var16 = 0; var16 < var42; var16++) {
                     this.groundEventConds[var35] = Utils.append(this.groundEventConds[var35], var9.readInt());
                  }
               }
            }

            if ((var33 = var9.readShort()) > 0) {
               this.topEventConds = new int[var33][];

               for (int var45 = 0; var45 < var33; var45++) {
                  short var17 = var9.readShort();

                  for (int var26 = 0; var26 < var17; var26++) {
                     this.topEventConds[var45] = Utils.append(this.topEventConds[var45], var9.readInt());
                  }
               }
            }

            if ((var42 = var9.readShort()) > 0) {
               this.midPhases = new short[var42][];

               for (int var18 = 0; var18 < var42; var18++) {
                  short var27 = var9.readShort();

                  for (int var37 = 0; var37 < var27; var37++) {
                     this.midPhases[var18] = Utils.append(this.midPhases[var18], var9.readShort());
                  }
               }
            }

            short var19;
            if ((var19 = var9.readShort()) > 0) {
               this.groundPhases = new short[var19][];

               for (int var28 = 0; var28 < var19; var28++) {
                  var33 = var9.readShort();

                  for (int var47 = 0; var47 < var33; var47++) {
                     this.groundPhases[var28] = Utils.append(this.groundPhases[var28], var9.readShort());
                  }
               }
            }

            short var29;
            if ((var29 = var9.readShort()) > 0) {
               this.topPhases = new short[var29][];

               for (int var39 = 0; var39 < var29; var39++) {
                  var42 = var9.readShort();

                  for (int var20 = 0; var20 < var42; var20++) {
                     this.topPhases[var39] = Utils.append(this.topPhases[var39], var9.readShort());
                  }
               }
            }

            return true;
         } catch (Exception var6) {
            var6.printStackTrace();
            return false;
         }
      }
   }

   private boolean isMidVisible(int var1) {
      this.computeVisibility();
      return this.midVisible != null ? this.midVisible[var1] : true;
   }

   public final void drawMiddle(Graphics var1, int var2, int var3) {
      Graphics var10 = var1;
      SceneLayer var9 = this;
      this.computeVisibility();
      this.sortByY(this.actors);
      this.sortByY(this.groundActors);
      var1.translate(-this.viewX + var2, -this.viewY + var3);

      for (int var5 = 0; var9.groundPlacements != null && var5 < var9.groundPlacements.length; var5++) {
         boolean var6;
         short[] var7;
         if ((var6 = var9.groundVisible[var5])
            && var9.buildings[var9.groundPlacements[var5][0]] != null
            && (var7 = var9.buildings[var9.groundPlacements[var5][0]].getBounds()) != null
            && Utils.rectsOverlap(
               var9.viewX,
               var9.viewY,
               var9.viewW,
               var9.viewH,
               var9.groundPlacements[var5][1] + var7[0],
               var9.groundPlacements[var5][2] + var7[1],
               var7[2],
               var7[3]
            )) {
            var9.buildings[var9.groundPlacements[var5][0]].draw(var10, var9.groundPlacements[var5][1], var9.groundPlacements[var5][2]);
         }
      }

      for (int var12 = 0; var9.groundActors != null && var12 < var9.groundActors.length; var12++) {
         short[] var14;
         if (var9.groundActors[var12] != null
            && var9.groundActors[var12].isVisible
            && (var14 = var9.groundActors[var12].getBounds()) != null
            && Utils.rectsOverlap(
               var9.viewX, var9.viewY, var9.viewW, var9.viewH, var9.groundActors[var12].x + var14[0], var9.groundActors[var12].y + var14[1], var14[2], var14[3]
            )) {
            var9.groundActors[var12].draw(var10);
         }
      }

      int var13 = 0;
      if (var9.midPlacements == null) {
         for (int var15 = 0; var9.actors != null && var15 < var9.actors.length && var9.actors[var15] != null; var15++) {
            short[] var19;
            if (var9.actors[var15].isVisible
               && (var19 = var9.actors[var15].getBounds()) != null
               && Utils.rectsOverlap(
                  var9.viewX, var9.viewY, var9.viewW, var9.viewH, var9.actors[var15].x + var19[0], var9.actors[var15].y + var19[1], var19[2], var19[3]
               )) {
               var9.actors[var15].draw(var10);
               var13 = var9.actors.length;
            }
         }
      } else {
         short var16 = var9.midPlacements[0][2];

         for (int var20 = 0; var9.actors != null && var20 < var9.actors.length && var9.actors[var20] != null; var20++) {
            if (var9.actors[var20].isVisible) {
               short[] var8;
               if ((var8 = var9.actors[var20].getBounds()) != null) {
                  if (var9.actors[var20].y > var16) {
                     break;
                  }

                  if (Utils.rectsOverlap(
                     var9.viewX, var9.viewY, var9.viewW, var9.viewH, var9.actors[var20].x + var8[0], var9.actors[var20].y + var8[1], var8[2], var8[3]
                  )) {
                     var9.actors[var20].draw(var10);
                  }

                  var13 = var20 + 1;
               }
            } else {
               var13 = var20 + 1;
            }
         }
      }

      for (int var17 = 0; var9.midPlacements != null && var17 < var9.midPlacements.length; var17++) {
         if (var17 > 0 && var17 < var9.midPlacements.length) {
            for (int var21 = var13; var9.actors != null && var21 < var9.actors.length && var9.actors[var21] != null; var21++) {
               if (var9.actors[var21].isVisible) {
                  short[] var24;
                  if ((var24 = var9.actors[var21].getBounds()) != null && var9.actors[var21].y > var9.midPlacements[var17 - 1][2]) {
                     if (var9.actors[var21].y > var9.midPlacements[var17][2]) {
                        break;
                     }

                     if (Utils.rectsOverlap(
                        var9.viewX, var9.viewY, var9.viewW, var9.viewH, var9.actors[var21].x + var24[0], var9.actors[var21].y + var24[1], var24[2], var24[3]
                     )) {
                        var9.actors[var21].draw(var10);
                     }

                     var13 = var21 + 1;
                  }
               } else {
                  var13 = var21 + 1;
               }
            }
         }

         short[] var22;
         if (var9.isMidVisible(var17)
            && var9.buildings[var9.midPlacements[var17][0]] != null
            && (var22 = var9.buildings[var9.midPlacements[var17][0]].getBounds()) != null
            && Utils.rectsOverlap(
               var9.viewX,
               var9.viewY,
               var9.viewW,
               var9.viewH,
               var9.midPlacements[var17][1] + var22[0],
               var9.midPlacements[var17][2] + var22[1],
               var22[2],
               var22[3]
            )) {
            var9.buildings[var9.midPlacements[var17][0]].draw(var10, var9.midPlacements[var17][1], var9.midPlacements[var17][2]);
         }
      }

      if (var9.midPlacements != null) {
         short var18 = var9.midPlacements[var9.midPlacements.length - 1][2];

         for (int var23 = var13; var9.actors != null && var23 < var9.actors.length && var9.actors[var23] != null; var23++) {
            short[] var25;
            if (var9.actors[var23].isVisible
               && (var25 = var9.actors[var23].getBounds()) != null
               && var9.actors[var23].y > var18
               && Utils.rectsOverlap(
                  var9.viewX, var9.viewY, var9.viewW, var9.viewH, var9.actors[var23].x + var25[0], var9.actors[var23].y + var25[1], var25[2], var25[3]
               )) {
               var9.actors[var23].draw(var10);
            }
         }
      }

      var10.translate(var9.viewX - var2, var9.viewY - var3);
   }

   public final void drawDebugRects(Graphics var1) {
      for (int var2 = 0; this.midPlacements != null && var2 < this.midPlacements.length; var2++) {
         short[] var3;
         if (this.midPlacements[var2] != null
            && this.isMidVisible(var2)
            && this.buildings[this.midPlacements[var2][0]] != null
            && (var3 = this.buildings[this.midPlacements[var2][0]].getBounds()) != null
            && Utils.rectsOverlap(
               this.viewX, this.viewY, this.viewW, this.viewH, this.midPlacements[var2][1] + var3[0], this.midPlacements[var2][2] + var3[1], var3[2], var3[3]
            )) {
            this.buildings[this.midPlacements[var2][0]].drawDebugRects(var1, this.midPlacements[var2][1], this.midPlacements[var2][2]);
         }
      }

      for (int var4 = 0; this.actors != null && var4 < this.actors.length; var4++) {
         if (this.actors[var4] != null && this.actors[var4].isVisible) {
            this.actors[var4].drawDebugBoxes(var1);
         }
      }

      for (int var5 = 0; this.skyActors != null && var5 < this.skyActors.length; var5++) {
         if (this.skyActors[var5] != null && this.skyActors[var5].isVisible) {
            this.skyActors[var5].drawDebugBoxes(var1);
         }
      }
   }

   public final void drawSkyActors(Graphics var1, int var2, int var3) {
      this.sortByY(this.skyActors);
      var1.translate(-this.viewX + var2, -this.viewY + var3);

      for (int var4 = 0; this.skyActors != null && var4 < this.skyActors.length && this.skyActors[var4] != null; var4++) {
         short[] var5;
         if (this.skyActors[var4].isVisible
            && (var5 = this.skyActors[var4].getBounds()) != null
            && Utils.rectsOverlap(
               this.viewX, this.viewY, this.viewW, this.viewH, this.skyActors[var4].x + var5[0], this.skyActors[var4].y + var5[1], var5[2], var5[3]
            )) {
            this.skyActors[var4].draw(var1);
         }
      }

      var1.translate(this.viewX - var2, this.viewY - var3);
   }

   public final void drawTop(Graphics var1, int var2, int var3) {
      var1.translate(-this.viewX + var2, -this.viewY + var3);

      for (int var4 = 0; this.topPlacements != null && var4 < this.topPlacements.length; var4++) {
         boolean var5;
         short[] var6;
         if ((var5 = this.topVisible[var4])
            && this.buildings[this.topPlacements[var4][0]] != null
            && (var6 = this.buildings[this.topPlacements[var4][0]].getBounds()) != null
            && Utils.rectsOverlap(
               this.viewX, this.viewY, this.viewW, this.viewH, this.topPlacements[var4][1] + var6[0], this.topPlacements[var4][2] + var6[1], var6[2], var6[3]
            )) {
            this.buildings[this.topPlacements[var4][0]].draw(var1, this.topPlacements[var4][1], this.topPlacements[var4][2]);
         }
      }

      var1.translate(this.viewX - var2, this.viewY - var3);
   }

   public final void animateBuildings() {
      for (int var1 = 0; this.buildings != null && var1 < this.buildings.length; var1++) {
         if (this.buildings[var1] != null) {
            this.buildings[var1].nextFrame();
         }
      }
   }

   public final void removeSkyActor(SpriteBase var1) {
      int var2 = -1;

      for (int var3 = 0; this.skyActors != null && var3 < this.skyActors.length; var3++) {
         if (this.skyActors[var3] == var1) {
            var2 = var3;
            break;
         }
      }

      if (var2 >= 0) {
         this.skyActors[var2] = null;
      }

      this.compactActors();
   }

   public final void removeActor(SpriteBase var1) {
      int var2 = -1;

      for (int var3 = 0; this.actors != null && var3 < this.actors.length; var3++) {
         if (this.actors[var3] == var1) {
            var2 = var3;
            break;
         }
      }

      if (var2 >= 0) {
         this.actors[var2] = null;
      }

      this.compactActors();
   }

   public final void setPhase(int var1) {
      this.phase = var1;
   }

   public final void setOccurredEvents(int[] var1) {
      this.occurredEvents = var1;
   }

   public final void setViewport(int var1, int var2, int var3, int var4) {
      this.viewX = (short)var1;
      this.viewY = (short)var2;
      this.viewW = (short)var3;
      this.viewH = (short)var4;
   }

   public final void sortAllActors() {
      this.sortByY(this.actors);
      this.sortByY(this.skyActors);
      this.sortByY(this.groundActors);
   }

   private void sortByY(SpriteBase[] var1) {
      boolean var2 = false;
      var1 = var1;
      if (var1 != null) {
         int var3;
         for (int var5 = (var3 = var1.length) / 2; var5 != 0; var5 /= 2) {
            for (int var6 = var5; var6 < var3; var6++) {
               SpriteBase var9 = null;
               short var7;
               if (var1[var6] == null) {
                  var7 = 32767;
               } else {
                  var7 = (var9 = var1[var6]).y;
               }

               int var4 = var6 - var5;
               short var8;
               if (var1[var4] == null) {
                  var8 = 32767;
               } else {
                  var8 = var1[var4].y;
               }

               if (!var2) {
                  while (var7 < var8 && var4 >= 0 && var4 < var3) {
                     var1[var4 + var5] = var1[var4];
                     if ((var4 -= var5) < 0 || var4 >= var3) {
                        break;
                     }

                     if (var1[var4] == null) {
                        var8 = 32767;
                     } else {
                        var8 = var1[var4].y;
                     }
                  }
               } else {
                  while (var7 > var8 && var4 >= 0 && var4 < var3) {
                     var1[var4 + var5] = var1[var4];
                     if ((var4 -= var5) < 0 || var4 >= var3) {
                        break;
                     }

                     if (var1[var4] == null) {
                        var8 = 32767;
                     } else {
                        var8 = var1[var4].y;
                     }
                  }
               }

               var1[var4 + var5] = var9;
            }
         }
      }
   }

   private void computeVisibility() {
      if (this.midVisible == null && this.midPlacements != null) {
         this.midVisible = new boolean[this.midPlacements.length];

         for (int var1 = 0; var1 < this.midVisible.length; var1++) {
            this.midVisible[var1] = true;
         }

         for (int var4 = 0; this.midEventConds != null && var4 < this.midEventConds.length; var4++) {
            for (int var2 = 1; var2 < this.midEventConds[var4].length; var2++) {
               if (this.midEventConds[var4][var2] > 0 && !Utils.contains(this.occurredEvents, this.midEventConds[var4][var2])
                  || this.midEventConds[var4][var2] < 0 && Utils.contains(this.occurredEvents, this.midEventConds[var4][var2])) {
                  this.midVisible[this.midEventConds[var4][0]] = false;
                  break;
               }
            }
         }

         for (int var5 = 0; this.midPhases != null && var5 < this.midPhases.length; var5++) {
            boolean var12 = false;

            for (int var3 = 1; var3 < this.midPhases[var5].length; var3++) {
               if (this.midPhases[var5][var3] == this.phase) {
                  var12 = true;
                  break;
               }
            }

            if (!var12) {
               this.midVisible[this.midPhases[var5][0]] = false;
            }
         }

         this.midEventConds = null;
         this.midPhases = null;
      }

      if (this.groundVisible == null && this.groundPlacements != null) {
         this.groundVisible = new boolean[this.groundPlacements.length];

         for (int var6 = 0; var6 < this.groundVisible.length; var6++) {
            this.groundVisible[var6] = true;
         }

         for (int var7 = 0; this.groundEventConds != null && var7 < this.groundEventConds.length; var7++) {
            for (int var13 = 1; var13 < this.groundEventConds[var7].length; var13++) {
               if (this.groundEventConds[var7][var13] > 0 && !Utils.contains(this.occurredEvents, this.groundEventConds[var7][var13])
                  || this.groundEventConds[var7][var13] < 0 && Utils.contains(this.occurredEvents, this.groundEventConds[var7][var13])) {
                  this.groundVisible[this.groundEventConds[var7][0]] = false;
                  break;
               }
            }
         }

         for (int var8 = 0; this.groundPhases != null && var8 < this.groundPhases.length; var8++) {
            boolean var14 = false;

            for (int var17 = 1; var17 < this.groundPhases[var8].length; var17++) {
               if (this.groundPhases[var8][var17] == this.phase) {
                  var14 = true;
                  break;
               }
            }

            if (!var14) {
               this.groundVisible[this.groundPhases[var8][0]] = false;
            }
         }

         this.groundEventConds = null;
         this.groundPhases = null;
      }

      if (this.topVisible == null && this.topPlacements != null) {
         this.topVisible = new boolean[this.topPlacements.length];

         for (int var9 = 0; var9 < this.topVisible.length; var9++) {
            this.topVisible[var9] = true;
         }

         for (int var10 = 0; this.topEventConds != null && var10 < this.topEventConds.length; var10++) {
            for (int var15 = 1; var15 < this.topEventConds[var10].length; var15++) {
               if (this.topEventConds[var10][var15] > 0 && !Utils.contains(this.occurredEvents, this.topEventConds[var10][var15])
                  || this.topEventConds[var10][var15] < 0 && Utils.contains(this.occurredEvents, this.topEventConds[var10][var15])) {
                  this.topVisible[this.topEventConds[var10][0]] = false;
                  break;
               }
            }
         }

         for (int var11 = 0; this.topPhases != null && var11 < this.topPhases.length; var11++) {
            boolean var16 = false;

            for (int var18 = 1; var18 < this.topPhases[var11].length; var18++) {
               if (this.topPhases[var11][var18] == this.phase) {
                  var16 = true;
                  break;
               }
            }

            if (!var16) {
               this.topVisible[this.topPhases[var11][0]] = false;
            }
         }

         this.topEventConds = null;
         this.topPhases = null;
      }
   }
}
