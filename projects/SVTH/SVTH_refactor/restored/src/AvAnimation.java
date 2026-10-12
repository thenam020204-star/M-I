import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class AvAnimation {
   private String[] imageNames;
   private Image[] images;
   private Image[] tileImages;
   private short[] tileImage;
   private short[] tileX;
   private short[] tileY;
   private short[] tileW;
   private short[] tileH;
   private byte[] tileTransform;
   private short[][] frameTileX;
   private short[][] frameTileY;
   private short[][] frameTileIds;
   private short[][] boxX;
   private short[][] boxY;
   private short[][] boxW;
   private short[][] boxH;
   private byte[][] boxType;
   private short[][] actionFrames;
   private byte formatVersion = 1;
   String imageDir;
   public String path;
   byte cutMode;
   private short[] boundsCache;
   private boolean flipX;
   private boolean flipY;
   private byte[][] imageBytes;
   private Image[] altImages1;
   public Image[] altImages2;
   public byte imageSet;
   private String lastImageName;

   public AvAnimation() {
   }

   public final void recolorImage(String var1, int[] var2, int[] var3) {
      for (int var4 = 0; var4 < this.imageNames.length; var4++) {
         if (this.imageNames[var4].equals(var1)) {
            this.imageBytes[var4] = PngRecolor.replaceColors(this.imageBytes[var4], var2, var3);
            return;
         }
      }
   }

   public final void createImages() {
      this.images = new Image[this.imageNames.length];

      for (int var1 = 0; var1 < this.images.length; var1++) {
         this.images[var1] = Image.createImage(this.imageBytes[var1], 0, this.imageBytes[var1].length);
      }

      this.imageBytes = null;
      this.cutTiles();
   }

   private void cutTiles() {
      if (DeviceConfig.cutAvImg > 0) {
         this.cutMode = DeviceConfig.cutAvImg;
         this.tileImages = new Image[this.tileX.length];

         for (int var1 = 0; var1 < this.tileX.length; var1++) {
            if (this.images[this.tileImage[var1]] != null) {
               int var2 = 0;
               if (this.flipX) {
                  var2 += 2;
               }

               if (this.flipY) {
                  var2++;
               }

               if (DeviceConfig.cutAvImg == 1) {
                  if ((this.tileTransform[var1] ^ var2) > 0) {
                     this.tileImages[var1] = Image.createImage(
                        this.images[this.tileImage[var1]],
                        this.tileX[var1],
                        this.tileY[var1],
                        this.tileW[var1],
                        this.tileH[var1],
                        this.tileTransform[var1] ^ var2
                     );
                  }
               } else if (DeviceConfig.cutAvImg == 2) {
                  this.tileImages[var1] = Image.createImage(
                     this.images[this.tileImage[var1]], this.tileX[var1], this.tileY[var1], this.tileW[var1], this.tileH[var1], this.tileTransform[var1] ^ var2
                  );
               }
            }
         }

         if (DeviceConfig.cutAvImg == 2) {
            this.images = null;
         }
      }
   }

   private void resolveImageDir(String var1) {
      if (var1 == null) {
         if (this.imageDir == null) {
            int var2 = this.path.lastIndexOf(47);
            this.imageDir = this.path.substring(0, var2 + 1);
         }
      } else if (var1.startsWith("/")) {
         this.imageDir = var1;
      } else {
         int var3 = this.path.lastIndexOf(47);
         this.imageDir = this.path.substring(0, var3 + 1) + var1;
      }

      if (!this.imageDir.endsWith("/")) {
         this.imageDir = this.imageDir + "/";
      }
   }

   public final int getActionCount() {
      return this.actionFrames != null ? this.actionFrames.length : 0;
   }

   private boolean parse(byte[] var1) {
      ByteArrayInputStream var8 = new ByteArrayInputStream(var1);
      DataInputStream var9 = new DataInputStream(var8);

      try {
         byte[] var2 = new byte[2];
         var9.read(var2);
         if (var9.readByte() != this.formatVersion) {
            return false;
         } else {
            short var10;
            if ((var10 = var9.readShort()) > 0) {
               this.imageNames = new String[var10];

               for (int var3 = 0; var3 < var10; var3++) {
                  this.imageNames[var3] = var9.readUTF();
               }
            }

            short var14;
            if ((var14 = var9.readShort()) > 0) {
               this.tileImage = new short[var14];
               this.tileX = new short[var14];
               this.tileY = new short[var14];
               this.tileW = new short[var14];
               this.tileH = new short[var14];
               this.tileTransform = new byte[var14];
               byte[] var4 = new byte[4];
               var9.read(var4);

               for (int var5 = 0; var5 < var14; var5++) {
                  if (var10 > 255) {
                     this.tileImage[var5] = var9.readShort();
                  } else {
                     this.tileImage[var5] = (short)(var9.readByte() & 255);
                  }

                  if (var4[0] == 1) {
                     this.tileX[var5] = var9.readShort();
                  } else {
                     this.tileX[var5] = (short)(var9.readByte() & 255);
                  }

                  if (var4[1] == 1) {
                     this.tileY[var5] = var9.readShort();
                  } else {
                     this.tileY[var5] = (short)(var9.readByte() & 255);
                  }

                  if (var4[2] == 1) {
                     this.tileW[var5] = var9.readShort();
                  } else {
                     this.tileW[var5] = (short)(var9.readByte() & 255);
                  }

                  if (var4[3] == 1) {
                     this.tileH[var5] = var9.readShort();
                  } else {
                     this.tileH[var5] = (short)(var9.readByte() & 255);
                  }

                  this.tileTransform[var5] = var9.readByte();
               }
            }

            short var18;
            if ((var18 = var9.readShort()) > 0) {
               byte[] var21 = new byte[3];
               var9.read(var21);
               this.frameTileX = new short[var18][];
               this.frameTileY = new short[var18][];
               this.frameTileIds = new short[var18][];

               for (int var11 = 0; var11 < var18; var11++) {
                  if ((var14 = var9.readShort()) > 0) {
                     this.frameTileX[var11] = new short[var14];
                     this.frameTileY[var11] = new short[var14];
                     this.frameTileIds[var11] = new short[var14];

                     for (int var6 = 0; var6 < var14; var6++) {
                        if (var21[0] == 1) {
                           this.frameTileX[var11][var6] = var9.readShort();
                        } else {
                           this.frameTileX[var11][var6] = var9.readByte();
                        }

                        if (var21[1] == 1) {
                           this.frameTileY[var11][var6] = var9.readShort();
                        } else {
                           this.frameTileY[var11][var6] = var9.readByte();
                        }

                        if (var21[2] == 1) {
                           this.frameTileIds[var11][var6] = var9.readShort();
                        } else {
                           this.frameTileIds[var11][var6] = (short)(var9.readByte() & 255);
                        }
                     }
                  }
               }
            }

            short var22;
            if ((var22 = var9.readShort()) > 0) {
               this.boxX = new short[var22][];
               this.boxY = new short[var22][];
               this.boxW = new short[var22][];
               this.boxH = new short[var22][];
               this.boxType = new byte[var22][];
               var2 = new byte[4];
               var9.read(var2);

               for (int var16 = 0; var16 < this.boxX.length; var16++) {
                  short var24;
                  if ((var24 = var9.readShort()) > 0) {
                     this.boxX[var16] = new short[var24];
                     this.boxY[var16] = new short[var24];
                     this.boxW[var16] = new short[var24];
                     this.boxH[var16] = new short[var24];
                     this.boxType[var16] = new byte[var24];

                     for (int var19 = 0; var19 < var24; var19++) {
                        if (var2[0] == 1) {
                           this.boxX[var16][var19] = var9.readShort();
                        } else {
                           this.boxX[var16][var19] = var9.readByte();
                        }

                        if (var2[1] == 1) {
                           this.boxY[var16][var19] = var9.readShort();
                        } else {
                           this.boxY[var16][var19] = var9.readByte();
                        }

                        if (var2[2] == 1) {
                           this.boxW[var16][var19] = var9.readShort();
                        } else {
                           this.boxW[var16][var19] = (short)(var9.readByte() & 255);
                        }

                        if (var2[3] == 1) {
                           this.boxH[var16][var19] = var9.readShort();
                        } else {
                           this.boxH[var16][var19] = (short)(var9.readByte() & 255);
                        }

                        this.boxType[var16][var19] = var9.readByte();
                     }
                  }
               }
            }

            short var13;
            if ((var13 = var9.readShort()) > 0) {
               this.actionFrames = new short[var13][];
               byte var17 = var9.readByte();

               for (int var25 = 0; var25 < var13; var25++) {
                  if ((var18 = var9.readShort()) > 0) {
                     this.actionFrames[var25] = new short[var18];

                     for (int var23 = 0; var23 < var18; var23++) {
                        if (var17 == 1) {
                           this.actionFrames[var25][var23] = var9.readShort();
                        } else {
                           this.actionFrames[var25][var23] = (short)(var9.readByte() & 255);
                        }
                     }
                  }
               }
            }

            return true;
         }
      } catch (IOException var7) {
         var7.printStackTrace();
         return false;
      }
   }

   public final int getFrameCount(int var1) {
      if (this.actionFrames == null || var1 >= this.actionFrames.length) {
         return 0;
      } else {
         return this.actionFrames[var1] != null ? this.actionFrames[var1].length : 0;
      }
   }

   public final short[] getBounds() {
      if (this.boundsCache == null) {
         short var1 = 0;
         short var2 = 0;
         short var3 = 0;
         short var4 = 0;

         for (int var5 = 0; this.frameTileX != null && var5 < this.frameTileX.length; var5++) {
            for (int var6 = 0; this.frameTileX[var5] != null && var6 < this.frameTileX[var5].length; var6++) {
               if (var1 > this.frameTileX[var5][var6]) {
                  var1 = this.frameTileX[var5][var6];
               }

               if (var3 < this.frameTileX[var5][var6] + this.tileW[this.frameTileIds[var5][var6]]) {
                  var3 = (short)(this.frameTileX[var5][var6] + this.tileW[this.frameTileIds[var5][var6]]);
               }
            }

            for (int var7 = 0; this.frameTileY[var5] != null && var7 < this.frameTileY[var5].length; var7++) {
               if (var2 > this.frameTileY[var5][var7]) {
                  var2 = this.frameTileY[var5][var7];
               }

               if (var4 < this.frameTileY[var5][var7] + this.tileH[this.frameTileIds[var5][var7]]) {
                  var4 = (short)(this.frameTileY[var5][var7] + this.tileH[this.frameTileIds[var5][var7]]);
               }
            }
         }

         this.boundsCache = new short[]{var1, var2, (short)(var3 - var1), (short)(var4 - var2)};
      }

      return this.boundsCache;
   }

   public final short[] getBoxes(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      short[] var7 = null;
      if (this.actionFrames != null
         && var1 < this.actionFrames.length
         && var1 >= 0
         && this.actionFrames[var1] != null
         && var2 < this.actionFrames[var1].length
         && var2 >= 0) {
         short var9 = this.actionFrames[var1][var2];
         if (this.boxX != null && var9 < this.boxX.length && this.boxX[var9] != null) {
            for (int var10 = 0; var10 < this.boxX[var9].length; var10++) {
               short var8 = 0;
               if (var5 == 0) {
                  var8 = 1;
               } else if (var5 == this.boxType[var9][var10]) {
                  var8 = 1;
               }

               if (var8) {
                  if (var7 == null) {
                     var7 = new short[4];
                  } else {
                     short[] var11 = new short[var7.length + 4];
                     System.arraycopy(var7, 0, var11, 0, var7.length);
                     var7 = var11;
                  }

                  if (var6) {
                     var7[var7.length - 4] = (short)(-(this.boxX[var9][var10] + this.boxW[var9][var10]) + var3);
                  } else {
                     var7[var7.length - 4] = (short)(this.boxX[var9][var10] + var3);
                  }

                  var7[var7.length - 3] = (short)(this.boxY[var9][var10] + var4);
                  var7[var7.length - 2] = this.boxW[var9][var10];
                  var8 = this.boxH[var9][var10];
                  var7[var7.length - 1] = var8;
               }
            }
         }
      }

      return var7;
   }

   public final void loadImageBytes(String[] var1, String[] var2, int var3) {
      this.imageBytes = new byte[this.imageNames.length][];

      for (int var4 = 0; var4 < this.imageNames.length; var4++) {
         String var5 = this.imageNames[var4];
         if (var1 != null && var2 != null) {
            for (int var6 = 0; var1 != null && var6 < var1.length; var6++) {
               if (var1[var6].equals(this.imageNames[var4])) {
                  var5 = var2[var6];
                  break;
               }
            }
         }

         this.imageBytes[var4] = ResourceCache.loadBytes(this.imageDir + var5, var3);
         if (this.imageBytes[var4] == null) {
            System.out.println(this.imageDir + var5 + " Không tìm thấy ");
         }
      }
   }

   public final void loadImages(String var1, String[] var2, String[] var3, int[] var4, int[] var5, int var6, boolean var7, boolean var8, int var9) {
      this.tileImages = null;
      this.cutMode = 0;
      if (this.imageNames != null) {
         this.resolveImageDir(var1);
         this.flipX = var7;
         this.flipY = var8;
         this.images = new Image[this.imageNames.length];

         for (int var10 = 0; var10 < this.imageNames.length; var10++) {
            String var11 = this.imageNames[var10];
            if (var2 != null) {
               for (int var12 = 0; var2 != null && var12 < var2.length; var12++) {
                  if (var2[var12].equals(this.imageNames[var10])) {
                     var11 = var3[var12];
                     break;
                  }
               }
            } else if (var3 != null && var10 < var3.length && var3[var10] != null) {
               var11 = var3[var10];
            }

            if (var5 == null && var6 < 0) {
               this.images[var10] = ResourceCache.loadImage(this.imageDir + var11, var9);
            } else {
               byte[] var13;
               if ((var13 = ResourceCache.loadBytes(this.imageDir + var11, var9)) != null) {
                  if (var5 != null) {
                     var13 = PngRecolor.replaceColors(var13, var4, var5);
                  }

                  if (var6 >= 0) {
                     var13 = PngRecolor.scaleBrightness(var13, var6);
                  }

                  this.images[var10] = Utils.createImage(var13);
               }
            }

            if (this.images[var10] == null) {
               System.out.println("Av loadImages không tìm thấy ảnh:" + this.imageDir + var11);
            }
         }

         this.cutTiles();
      }
   }

   public final void draw(Graphics var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.drawFlipped(var1, var2, var3, var4, var5, var6, false);
   }

   public final void drawFlipped(Graphics var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7) {
      if (this.actionFrames != null
         && this.frameTileX != null
         && var2 < this.actionFrames.length
         && this.actionFrames[var2] != null
         && var3 < this.actionFrames[var2].length) {
         short var16 = this.actionFrames[var2][var3];
         short var10 = 0;

         try {
            for (int var11 = 0; this.frameTileX[var16] != null; var11++) {
               if (var11 >= this.frameTileX[var16].length) {
                  return;
               }

               short var8;
               if (var6) {
                  var8 = (short)(-this.frameTileX[var16][var11] + var4);
               } else {
                  var8 = (short)(this.frameTileX[var16][var11] + var4);
               }

               short var9;
               if (var7) {
                  var9 = (short)(-this.frameTileY[var16][var11] + var5);
               } else {
                  var9 = (short)(this.frameTileY[var16][var11] + var5);
               }

               var10 = this.frameTileIds[var16][var11];
               int var14 = 0;
               byte var12;
               if (var6) {
                  var12 = 8;
                  var14 += 2;
               } else {
                  var12 = 4;
               }

               byte var13;
               if (var7) {
                  var13 = 32;
                  var14++;
               } else {
                  var13 = 16;
               }

               this.lastImageName = this.imageNames[this.tileImage[var10]];
               if (DeviceConfig.cutAvImg == 0) {
                  if (this.images != null) {
                     if (this.imageSet == 0) {
                        if (this.images[this.tileImage[var10]] != null) {
                           var1.drawRegion(
                              this.images[this.tileImage[var10]],
                              this.tileX[var10],
                              this.tileY[var10],
                              this.tileW[var10],
                              this.tileH[var10],
                              this.tileTransform[var10] ^ var14,
                              var8,
                              var9,
                              var12 | var13
                           );
                        }
                     } else if (this.imageSet == 1 && this.altImages1 != null) {
                        var1.drawRegion(
                           this.altImages1[this.tileImage[var10]],
                           this.tileX[var10],
                           this.tileY[var10],
                           this.tileW[var10],
                           this.tileH[var10],
                           this.tileTransform[var10] ^ var14,
                           var8,
                           var9,
                           var12 | var13
                        );
                     } else if (this.imageSet == 2 && this.altImages2 != null) {
                        var1.drawRegion(
                           this.altImages2[this.tileImage[var10]],
                           this.tileX[var10],
                           this.tileY[var10],
                           this.tileW[var10],
                           this.tileH[var10],
                           this.tileTransform[var10] ^ var14,
                           var8,
                           var9,
                           var12 | var13
                        );
                     }
                  }
               } else if (DeviceConfig.cutAvImg == 1) {
                  if (this.tileImages != null && this.tileImages[var10] != null) {
                     var1.drawImage(this.tileImages[var10], var8, var9, var12 | var13);
                  } else if (this.images[this.tileImage[var10]] != null) {
                     var1.drawRegion(
                        this.images[this.tileImage[var10]],
                        this.tileX[var10],
                        this.tileY[var10],
                        this.tileW[var10],
                        this.tileH[var10],
                        this.tileTransform[var10] ^ var14,
                        var8,
                        var9,
                        var12 | var13
                     );
                  }
               } else if (DeviceConfig.cutAvImg == 2 && this.tileImages != null && this.tileImages[var10] != null) {
                  var1.drawImage(this.tileImages[var10], var8, var9, var12 | var13);
               }
            }
         } catch (Exception var15) {
            if (DeviceConfig.allowDebug) {
               var15.printStackTrace();
               System.out.println(this.path + " của " + var3 + " lỗi khung hình");
               System.out.println(this.path + " của " + var3 + " Lỗi cắt khung‚tên ảnh=" + this.lastImageName);
               System.out
                  .println(
                     this.path
                        + " của "
                        + var3
                        + " Lỗi khung hình‚phạm vi cắt x="
                        + this.tileX[var10]
                        + ",y="
                        + this.tileY[var10]
                        + ",w="
                        + this.tileW[var10]
                        + ",h="
                        + this.tileH[var10]
                  );
            }
         }
      }
   }

   public final boolean load(String var1) {
      Object var4 = null;
      this.path = var1;
      byte[] var3;
      if (DeviceConfig.cacheAv) {
         var3 = ResourceCache.loadBytes(var1, DeviceConfig.cacheAvGroup);
      } else {
         var3 = Utils.readResource(var1);
      }

      if (var3 == null) {
         System.out.println(var1 + " không tồn tại");
         return false;
      } else {
         this.resolveImageDir(null);
         if (var3 != null) {
            return this.parse(var3);
         } else {
            System.out.println(var1 + "Không tìm thấy tệp");
            return false;
         }
      }
   }
}
