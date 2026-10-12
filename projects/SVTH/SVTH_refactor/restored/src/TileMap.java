import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class TileMap {
   private short viewX;
   private short viewY;
   private short viewW;
   private short viewH;
   private short mapWidth;
   private short mapHeight;
   private byte tileW;
   private byte tileH;
   private String[] tilesetNames;
   private Image[] tilesetImages;
   private byte[] tileImageIndex;
   private short[] tileSrcX;
   private short[] tileSrcY;
   private short[] tileSrcW;
   private short[] tileSrcH;
   private byte[] tileTransform;
   private Image[] tileImages;
   private byte[][] layerCols;
   private byte[][] layerRows;
   private short[][] layerTiles;
   private byte[] layerEncoding;
   private byte backgroundLayerCount;
   private short[] blockX;
   private short[] blockY;
   private short[] blockW;
   private short[] blockH;
   private byte[] blockType;
   private String imageDir;
   private String mapPath;
   private byte[][] tilesetBytes;
   private int backgroundColor;
   private Image bufferImage;
   private Graphics bufferGraphics;
   private boolean useScrollBuffer = true;
   private boolean bufferReady;
   private short bufferOriginX;
   private short bufferOriginY;
   private short bufferW;
   private short bufferH;
   private short bufferCols;
   private short bufferRows;
   private short bufferMargin;
   private boolean useGridAccel = true;
   private short[][][] backgroundGrid;
   private short[][][] foregroundGrid;
   private byte mapFormat;

   public TileMap() {
   }

   public final void recolorTileset(String var1, int[] var2, int[] var3) {
      for (int var4 = 0; this.tilesetNames != null && var4 < this.tilesetNames.length; var4++) {
         if (this.tilesetNames[var4].equals(var1)) {
            this.tilesetBytes[var4] = PngRecolor.replaceColors(this.tilesetBytes[var4], var2, var3);
            return;
         }
      }
   }

   public final boolean hitsBlock(short[] var1, int var2) {
      for (byte var4 = 0; var1 != null && var4 < var1.length; var4 += 4) {
         for (int var3 = 0; this.blockX != null && var3 < this.blockX.length; var3++) {
            if (1 == this.blockType[var3]
               && Utils.rectsOverlap(
                  var1[var4], var1[var4 + 1], var1[var4 + 2], var1[var4 + 3], this.blockX[var3], this.blockY[var3], this.blockW[var3], this.blockH[var3]
               )) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean isLayerTileAligned(int var1) {
      for (int var2 = 0; this.layerTiles != null && this.layerTiles[var1] != null && var2 < this.layerTiles[var1].length; var2++) {
         if (this.tileSrcW[this.layerTiles[var1][var2]] != this.tileW) {
            return false;
         }

         if (this.tileSrcH[this.layerTiles[var1][var2]] != this.tileH) {
            return false;
         }
      }

      return true;
   }

   public final void clear() {
      this.mapWidth = 0;
      this.mapHeight = 0;
      this.tileW = 0;
      this.tileH = 0;
      this.tilesetImages = null;
      this.tileImageIndex = null;
      this.tileSrcX = null;
      this.tileSrcY = null;
      this.tileSrcW = null;
      this.tileSrcH = null;
      this.tileTransform = null;
      this.tileImages = null;
      this.layerCols = null;
      this.layerRows = null;
      this.layerTiles = null;
      this.backgroundLayerCount = -1;
      this.blockX = null;
      this.blockY = null;
      this.blockW = null;
      this.blockH = null;
      this.blockType = null;
      this.tilesetBytes = null;
      this.bufferImage = null;
      this.bufferGraphics = null;
      this.bufferReady = false;
   }

   public final void releaseImages() {
      this.tilesetImages = null;
      this.tileImages = null;
      this.tilesetBytes = null;
      this.bufferImage = null;
      this.bufferGraphics = null;
      this.bufferReady = false;
   }

   private void redrawBufferColumns(int var1, int var2, int var3, int var4) {
      for (int var5 = var1; var5 <= var2; var5++) {
         if (var3 > 0) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               var5 * this.tileW % this.bufferW,
               var4 * this.tileH % this.bufferH,
               new short[]{(short)(var5 * this.tileW), (short)(var4 * this.tileH), this.tileW, (short)(var3 * this.tileH)}
            );
         }

         if (var3 < this.bufferRows) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               var5 * this.tileW % this.bufferW,
               0,
               new short[]{(short)(var5 * this.tileW), (short)((var4 + var3) * this.tileH), this.tileW, (short)((this.bufferRows - var3) * this.tileH)}
            );
         }
      }
   }

   private void redrawBufferRows(int var1, int var2, int var3, int var4) {
      for (int var5 = var1; var5 <= var2; var5++) {
         if (var3 > 0) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               var4 * this.tileW % this.bufferW,
               var5 * this.tileH % this.bufferH,
               new short[]{(short)(var4 * this.tileW), (short)(var5 * this.tileH), (short)(var3 * this.tileW), this.tileH}
            );
         }

         if (var3 < this.bufferCols) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               (var4 + var3) * this.tileW % this.bufferW,
               var5 * this.tileH % this.bufferH,
               new short[]{(short)((var4 + var3) * this.tileW), (short)(var5 * this.tileH), (short)((this.bufferCols - var3) * this.tileW), this.tileH}
            );
         }
      }
   }

   public final void createImages() {
      if (this.tilesetNames != null) {
         this.tilesetImages = new Image[this.tilesetNames.length];

         for (int var1 = 0; var1 < this.tilesetImages.length; var1++) {
            this.tilesetImages[var1] = Image.createImage(this.tilesetBytes[var1], 0, this.tilesetBytes[var1].length);
         }

         this.tilesetBytes = null;
         TileMap var3 = this;
         if (DeviceConfig.cutMapImg == 1) {
            this.tileImages = new Image[this.tileSrcX.length];

            for (int var4 = 0; var4 < var3.tileSrcX.length; var4++) {
               if (var3.tilesetImages[var3.tileImageIndex[var4]] != null && var3.tileTransform[var4] > 0) {
                  var3.tileImages[var4] = Image.createImage(
                     var3.tilesetImages[var3.tileImageIndex[var4]],
                     var3.tileSrcX[var4],
                     var3.tileSrcY[var4],
                     var3.tileSrcW[var4],
                     var3.tileSrcH[var4],
                     var3.tileTransform[var4]
                  );
               }
            }
         } else {
            if (DeviceConfig.cutMapImg == 2) {
               this.tileImages = new Image[this.tileSrcX.length];

               for (int var2 = 0; var2 < var3.tileSrcX.length; var2++) {
                  if (var3.tilesetImages[var3.tileImageIndex[var2]] != null) {
                     var3.tileImages[var2] = Image.createImage(
                        var3.tilesetImages[var3.tileImageIndex[var2]],
                        var3.tileSrcX[var2],
                        var3.tileSrcY[var2],
                        var3.tileSrcW[var2],
                        var3.tileSrcH[var2],
                        var3.tileTransform[var2]
                     );
                  }
               }

               var3.tilesetImages = null;
            }
         }
      }
   }

   private void setImageDir(String var1) {
      if (var1 == null) {
         if (this.imageDir == null) {
            int var2 = this.mapPath.lastIndexOf(47);
            this.imageDir = this.mapPath.substring(0, var2 + 1);
         }
      } else if (var1.startsWith("/")) {
         this.imageDir = var1;
      } else {
         int var3 = this.mapPath.lastIndexOf(47);
         this.imageDir = this.mapPath.substring(0, var3 + 1) + var1;
      }

      if (!this.imageDir.endsWith("/")) {
         this.imageDir = this.imageDir + "/";
      }
   }

   private boolean parseMap(byte[] var1) {
      if (var1 == null) {
         return false;
      } else {
         ByteArrayInputStream var13 = new ByteArrayInputStream(var1);
         DataInputStream var14 = new DataInputStream(var13);

         try {
            byte[] var2 = new byte[3];
            var14.read(var2);
            this.mapFormat = var14.readByte();
            this.mapWidth = var14.readShort();
            this.mapHeight = var14.readShort();
            this.tileW = var14.readByte();
            this.tileH = var14.readByte();
            short var15;
            if ((var15 = (short)(var14.readByte() & 255)) > 0) {
               this.tilesetNames = new String[var15];

               for (int var3 = 0; var3 < var15; var3++) {
                  this.tilesetNames[var3] = var14.readUTF();
               }
            }

            short var20;
            if ((var20 = var14.readShort()) > 0) {
               this.tileImageIndex = new byte[var20];
               this.tileSrcX = new short[var20];
               this.tileSrcY = new short[var20];
               this.tileSrcW = new short[var20];
               this.tileSrcH = new short[var20];
               this.tileTransform = new byte[var20];
               var2 = new byte[4];
               var14.read(var2);

               for (int var4 = 0; var4 < var20; var4++) {
                  this.tileImageIndex[var4] = var14.readByte();
                  if (var2[0] == 1) {
                     this.tileSrcX[var4] = var14.readShort();
                  } else {
                     this.tileSrcX[var4] = (short)(var14.readByte() & 255);
                  }

                  if (var2[1] == 1) {
                     this.tileSrcY[var4] = var14.readShort();
                  } else {
                     this.tileSrcY[var4] = (short)(var14.readByte() & 255);
                  }

                  if (var2[2] == 1) {
                     this.tileSrcW[var4] = var14.readShort();
                  } else {
                     this.tileSrcW[var4] = (short)(var14.readByte() & 255);
                  }

                  if (var2[3] == 1) {
                     this.tileSrcH[var4] = var14.readShort();
                  } else {
                     this.tileSrcH[var4] = (short)(var14.readByte() & 255);
                  }

                  this.tileTransform[var4] = var14.readByte();
               }
            }

            if (this.mapFormat == 1) {
               byte var17;
               if ((var17 = var14.readByte()) > 0) {
                  this.layerCols = new byte[var17][];
                  this.layerRows = new byte[var17][];
                  this.layerTiles = new short[var17][];

                  for (int var21 = 0; var21 < var17; var21++) {
                     short var5;
                     if ((var5 = var14.readShort()) > 0) {
                        this.layerCols[var21] = new byte[var5];
                        this.layerRows[var21] = new byte[var5];
                        this.layerTiles[var21] = new short[var5];

                        for (int var6 = 0; var6 < var5; var6++) {
                           this.layerCols[var21][var6] = var14.readByte();
                           this.layerRows[var21][var6] = var14.readByte();
                           if (var20 > 255) {
                              this.layerTiles[var21][var6] = var14.readShort();
                           } else {
                              this.layerTiles[var21][var6] = (short)(var14.readByte() & 255);
                           }
                        }
                     }
                  }
               }
            } else if (this.mapFormat == 2) {
               byte var18;
               if ((var18 = var14.readByte()) > 0 && var18 > 0) {
                  this.layerCols = new byte[var18][];
                  this.layerRows = new byte[var18][];
                  this.layerTiles = new short[var18][];
                  this.layerEncoding = new byte[var18];

                  for (int var22 = 0; var22 < var18; var22++) {
                     int var25 = var14.readByte();
                     this.layerEncoding[var22] = (byte)var25;
                     if (var25 == 0) {
                        short var33;
                        if ((var33 = var14.readShort()) > 0) {
                           this.layerCols[var22] = new byte[var33];
                           this.layerRows[var22] = new byte[var33];
                           this.layerTiles[var22] = new short[var33];

                           for (int var40 = 0; var40 < var33; var40++) {
                              this.layerCols[var22][var40] = var14.readByte();
                              this.layerRows[var22][var40] = var14.readByte();
                              if (var20 > 255) {
                                 this.layerTiles[var22][var40] = var14.readShort();
                              } else {
                                 this.layerTiles[var22][var40] = (short)(var14.readByte() & 255);
                              }
                           }
                        }
                     } else if (var25 == 1) {
                        int var32 = this.mapWidth / this.tileW + (this.mapWidth % this.tileW != 0 ? 1 : 0);
                        int var39 = this.mapHeight / this.tileH + (this.mapHeight % this.tileH != 0 ? 1 : 0);
                        if (this.tileImageIndex.length > 255) {
                           for (int var44 = 0; var44 < var39; var44++) {
                              for (int var48 = 0; var48 < var32; var48++) {
                                 if ((var25 = var14.readShort()) > 0) {
                                    this.layerCols[var22] = Utils.append(this.layerCols[var22], var48);
                                    this.layerRows[var22] = Utils.append(this.layerRows[var22], var44);
                                    this.layerTiles[var22] = Utils.append(this.layerTiles[var22], var25 - 1);
                                 }
                              }
                           }
                        } else {
                           for (int var43 = 0; var43 < var39; var43++) {
                              for (int var47 = 0; var47 < var32; var47++) {
                                 if ((var25 = (short)(var14.readByte() & 255)) > 0) {
                                    this.layerCols[var22] = Utils.append(this.layerCols[var22], var47);
                                    this.layerRows[var22] = Utils.append(this.layerRows[var22], var43);
                                    this.layerTiles[var22] = Utils.append(this.layerTiles[var22], var25 - 1);
                                 }
                              }
                           }
                        }
                     } else if (var25 == 2) {
                        int var31 = this.mapWidth / this.tileW + (this.mapWidth % this.tileW != 0 ? 1 : 0);
                        int var7 = this.mapHeight / this.tileH + (this.mapHeight % this.tileH != 0 ? 1 : 0);
                        short var8;
                        if (this.tileImageIndex.length > 255) {
                           var8 = var14.readShort();
                        } else {
                           var8 = var14.readByte();
                        }

                        int var9 = this.tileSrcW[var8] / this.tileW;
                        var25 = this.tileSrcH[var8] / this.tileH;

                        for (int var10 = 0; var10 < var7; var10 += var25) {
                           for (int var11 = 0; var11 < var31; var11 += var9) {
                              this.layerCols[var22] = Utils.append(this.layerCols[var22], var11);
                              this.layerRows[var22] = Utils.append(this.layerRows[var22], var10);
                              this.layerTiles[var22] = Utils.append(this.layerTiles[var22], var8);
                           }
                        }
                     }
                  }
               }

               this.backgroundColor = var14.readInt();
            }

            this.backgroundLayerCount = var14.readByte();
            short var19;
            if ((var19 = var14.readShort()) > 0) {
               byte[] var23 = new byte[4];
               var14.read(var23);
               this.blockX = new short[var19];
               this.blockY = new short[var19];
               this.blockW = new short[var19];
               this.blockH = new short[var19];
               this.blockType = new byte[var19];

               for (int var29 = 0; var29 < var19; var29++) {
                  if (var23[0] == 1) {
                     this.blockX[var29] = var14.readShort();
                  } else {
                     this.blockX[var29] = var14.readByte();
                  }

                  if (var23[1] == 1) {
                     this.blockY[var29] = var14.readShort();
                  } else {
                     this.blockY[var29] = var14.readByte();
                  }

                  if (var23[2] == 1) {
                     this.blockW[var29] = var14.readShort();
                  } else {
                     this.blockW[var29] = (short)(var14.readByte() & 255);
                  }

                  if (var23[3] == 1) {
                     this.blockH[var29] = var14.readShort();
                  } else {
                     this.blockH[var29] = (short)(var14.readByte() & 255);
                  }

                  this.blockType[var29] = var14.readByte();
               }
            }

            if (this.useGridAccel && this.layerCols != null) {
               boolean var24 = true;
               boolean var30 = true;
               if (this.backgroundLayerCount > 0) {
                  for (int var35 = 0; var35 < this.backgroundLayerCount; var35++) {
                     if (!this.isLayerTileAligned(var35)) {
                        var24 = false;
                        System.out.println("Bản đồ " + var35 + " Ô lớp không chuẩn, hậu cảnh không dùng được tăng tốc ma trận");
                        break;
                     }
                  }

                  for (int var36 = this.backgroundLayerCount; var36 < this.layerCols.length; var36++) {
                     if (!this.isLayerTileAligned(var36)) {
                        var30 = false;
                        System.out.println("Bản đồ " + var36 + " Ô lớp không chuẩn, tiền cảnh không dùng được tăng tốc ma trận");
                        break;
                     }
                  }
               } else if (this.backgroundLayerCount < 0) {
                  for (int var34 = 0; var34 < this.layerCols.length; var34++) {
                     if (!this.isLayerTileAligned(var34)) {
                        var24 = false;
                        System.out.println("Bản đồ " + var34 + " Ô lớp không chuẩn, hậu cảnh không dùng được tăng tốc ma trận");
                        break;
                     }
                  }

                  var30 = false;
               }

               if (this.backgroundLayerCount > 0) {
                  if (var24) {
                     this.backgroundGrid = new short[this.backgroundLayerCount][][];
                  }

                  if (var30) {
                     this.foregroundGrid = new short[this.layerCols.length - this.backgroundLayerCount][][];
                  }
               } else if (this.backgroundLayerCount == 0) {
                  if (var30) {
                     this.foregroundGrid = new short[this.layerCols.length - this.backgroundLayerCount][][];
                  }
               } else if (var24) {
                  this.backgroundGrid = new short[this.layerCols.length][][];
               }

               for (int var37 = 0; this.backgroundGrid != null && var37 < this.backgroundGrid.length; var37++) {
                  int var41 = this.mapHeight / this.tileH;
                  int var45 = this.mapWidth / this.tileW;
                  this.backgroundGrid[var37] = new short[var41][var45];

                  for (int var49 = 0; this.layerCols[var37] != null && var49 < this.layerCols[var37].length; var49++) {
                     this.backgroundGrid[var37][this.layerRows[var37][var49]][this.layerCols[var37][var49]] = (short)(this.layerTiles[var37][var49] + 1);
                  }

                  this.layerCols[var37] = null;
                  this.layerRows[var37] = null;
                  this.layerTiles[var37] = null;
               }

               for (int var38 = 0; this.foregroundGrid != null && var38 < this.foregroundGrid.length; var38++) {
                  int var42 = this.mapHeight / this.tileH;
                  int var46 = this.mapWidth / this.tileW;
                  this.foregroundGrid[var38] = new short[var42][var46];

                  for (int var50 = 0;
                     this.layerCols[var38 + this.backgroundLayerCount] != null && var50 < this.layerCols[var38 + this.backgroundLayerCount].length;
                     var50++
                  ) {
                     this.foregroundGrid[var38][this.layerRows[var38 + this.backgroundLayerCount][var50]][this.layerCols[var38 + this.backgroundLayerCount][var50]] = (short)(
                        this.layerTiles[var38 + this.backgroundLayerCount][var50] + 1
                     );
                  }

                  this.layerCols[var38 + this.backgroundLayerCount] = null;
                  this.layerRows[var38 + this.backgroundLayerCount] = null;
                  this.layerTiles[var38 + this.backgroundLayerCount] = null;
               }

               if (var24 && this.backgroundGrid != null) {
                  System.out.println("Hậu cảnh dùng tăng tốc ma trận");
               }

               if (var30 && this.foregroundGrid != null) {
                  System.out.println("Tiền cảnh dùng tăng tốc ma trận");
               }
            }

            return true;
         } catch (IOException var12) {
            var12.printStackTrace();
            return false;
         }
      }
   }

   public final short getMapHeight() {
      return this.mapHeight;
   }

   public final short getMapWidth() {
      return this.mapWidth;
   }

   public final void loadTilesetBytes(String[] var1, String[] var2, String var3, int var4) {
      if (this.tilesetNames != null) {
         this.setImageDir(var3);
         this.tilesetBytes = new byte[this.tilesetNames.length][];

         for (int var5 = 0; var5 < this.tilesetNames.length; var5++) {
            String var6 = this.tilesetNames[var5];
            this.tilesetBytes[var5] = ResourceCache.loadBytes(this.imageDir + var6, var4);
         }
      }
   }

   public final void drawBackground(Graphics var1, int var2, int var3) {
      if (!this.useScrollBuffer || !DeviceConfig.useCarmarkCache) {
         this.drawBackgroundArea(var1, var2, var3, new short[]{this.viewX, this.viewY, this.viewW, this.viewH});
      } else if (this.bufferReady) {
         int var8 = this.viewX % this.bufferW;
         int var9 = this.viewY % this.bufferH;
         var1.drawRegion(this.bufferImage, var8, var9, this.bufferW - var8, this.bufferH - var9, 0, var2, var3, 20);
         var1.drawRegion(this.bufferImage, 0, var9, var8, this.bufferH - var9, 0, var2 + this.bufferW - var8, var3, 20);
         var1.drawRegion(this.bufferImage, 0, 0, var8, var9, 0, var2 + this.bufferW - var8, var3 + this.bufferH - var9, 20);
         var1.drawRegion(this.bufferImage, var8, 0, this.bufferW - var8, var9, 0, var2, var3 + this.bufferH - var9, 20);
      } else {
         this.bufferMargin = (short)(this.tileW << 1);
         this.bufferW = (short)(this.viewW / this.tileW * this.tileW + (this.viewW % this.tileW != 0 ? this.tileW : 0) + this.bufferMargin);
         this.bufferH = (short)(this.viewH / this.tileH * this.tileH + (this.viewH % this.tileH != 0 ? this.tileH : 0) + this.bufferMargin);
         this.bufferCols = (short)(this.bufferW / this.tileW);
         this.bufferRows = (short)(this.bufferH / this.tileH);
         this.bufferOriginX = (short)(this.viewX / this.tileW * this.tileW);
         this.bufferOriginY = (short)(this.viewY / this.tileH * this.tileH);
         this.bufferImage = Image.createImage(this.bufferW, this.bufferH);
         this.bufferGraphics = this.bufferImage.getGraphics();
         this.bufferGraphics.setColor(this.backgroundColor);
         this.bufferGraphics.fillRect(0, 0, this.bufferW, this.bufferH);
         int var4 = this.viewX / this.tileW;
         int var5 = this.viewY / this.tileH;
         int var6 = this.bufferCols - var4 % this.bufferCols;
         var2 = this.bufferRows - var5 % this.bufferRows;
         this.drawBackgroundArea(
            this.bufferGraphics,
            var4 % this.bufferCols * this.tileW,
            var5 % this.bufferRows * this.tileH,
            new short[]{(short)(var4 * this.tileW), (short)(var5 * this.tileH), (short)(var6 * this.tileW), (short)(var2 * this.tileH)}
         );
         if (this.bufferCols > var6) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               0,
               var5 % this.bufferRows * this.tileH,
               new short[]{
                  (short)((var4 + var6) * this.tileW), (short)(var5 * this.tileH), (short)((this.bufferCols - var6) * this.tileW), (short)(var2 * this.tileH)
               }
            );
         }

         if (this.bufferRows > var2) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               var4 % this.bufferCols * this.tileW,
               0,
               new short[]{
                  (short)(var4 * this.tileW), (short)((var5 + var2) * this.tileH), (short)(var6 * this.tileW), (short)((this.bufferRows - var2) * this.tileH)
               }
            );
         }

         if (this.bufferCols > var6 && this.bufferRows > var2) {
            this.drawBackgroundArea(
               this.bufferGraphics,
               0,
               0,
               new short[]{
                  (short)((var4 + var6) * this.tileW),
                  (short)((var5 + var2) * this.tileH),
                  (short)((this.bufferCols - var6) * this.tileW),
                  (short)((this.bufferRows - var2) * this.tileH)
               }
            );
         }

         this.bufferReady = true;
      }
   }

   private void drawBackgroundArea(Graphics var1, int var2, int var3, short[] var4) {
      if (this.backgroundGrid != null) {
         short var21;
         short var23 = (short)((var21 = var4[0]) + var4[2]);
         short var7;
         short var18 = (short)((var7 = var4[1]) + var4[3]);
         short var8 = (short)(var21 / this.tileW);
         short var9 = (short)(var23 / this.tileW + (var23 % this.tileW != 0 ? 1 : 0));
         short var10 = (short)(var7 / this.tileH);
         short var11 = (short)(var18 / this.tileH + (var18 % this.tileH != 0 ? 1 : 0));
         if (var8 < 0) {
            var8 = 0;
         }

         if (var10 < 0) {
            var10 = 0;
         }

         var1.translate(var2 - var21, var3 - var7);
         if (this.useScrollBuffer && DeviceConfig.useCarmarkCache || DeviceConfig.fillMapBackColor) {
            var1.setColor(this.backgroundColor);
            var1.fillRect(var21, var7, var23 - var21, var18 - var7);
         }

         short var19;
         var23 = var19 = (short)(var8 * this.tileW);
         short var12;
         short var13 = var12 = (short)(var10 * this.tileH);

         for (int var14 = 0; var14 < this.backgroundGrid.length; var14++) {
            for (int var15 = var10; var15 < var11 && var15 < this.backgroundGrid[var14].length; var15++) {
               for (int var16 = var8; var16 < var9 && var16 < this.backgroundGrid[var14][var15].length; var16++) {
                  short var17;
                  if ((var17 = (short)(this.backgroundGrid[var14][var15][var16] - 1)) >= 0) {
                     if (this.tileImages != null && this.tileImages[var17] != null) {
                        var1.drawImage(this.tileImages[var17], var19, var12, 20);
                     } else {
                        var1.drawRegion(
                           this.tilesetImages[this.tileImageIndex[var17]],
                           this.tileSrcX[var17],
                           this.tileSrcY[var17],
                           this.tileSrcW[var17],
                           this.tileSrcH[var17],
                           this.tileTransform[var17],
                           var19,
                           var12,
                           20
                        );
                     }
                  }

                  var19 = (short)(var19 + this.tileW);
               }

               var12 = (short)(var12 + this.tileH);
               var19 = var23;
            }

            var12 = var13;
         }

         var1.translate(-var2 + var21, -var3 + var7);
      } else if (this.backgroundLayerCount > 0) {
         short[] var20 = new short[this.backgroundLayerCount];

         for (int var22 = 0; var22 < this.backgroundLayerCount; var22++) {
            var20[var22] = (short)var22;
         }

         if (this.useScrollBuffer && DeviceConfig.useCarmarkCache || DeviceConfig.fillMapBackColor) {
            var1.setColor(this.backgroundColor);
            var1.translate(var2 - var4[0], var3 - var4[1]);
            var1.fillRect(var4[0], var4[1], var4[2], var4[3]);
            var1.translate(-var2 + var4[0], -var3 + var4[1]);
         }

         this.drawLayers(var1, var20, var2, var3, var4);
      } else {
         if (this.backgroundLayerCount < 0) {
            short[] var5 = new short[this.layerCols.length];

            for (int var6 = 0; var6 < this.layerCols.length; var6++) {
               var5[var6] = (short)var6;
            }

            if (this.useScrollBuffer && DeviceConfig.useCarmarkCache || DeviceConfig.fillMapBackColor) {
               var1.setColor(this.backgroundColor);
               var1.translate(var2 - var4[0], var3 - var4[1]);
               var1.fillRect(var4[0], var4[1], var4[2], var4[3]);
               var1.translate(-var2 + var4[0], -var3 + var4[1]);
            }

            this.drawLayers(var1, var5, var2, var3, var4);
         }
      }
   }

   public final void drawDebugBlocks(Graphics var1) {
      for (int var2 = 0; this.blockX != null && var2 < this.blockX.length; var2++) {
         if (this.blockType[var2] == 1) {
            var1.setColor(16711680);
         } else if (this.blockType[var2] == 2) {
            var1.setColor(255);
         } else {
            var1.setColor(16711935);
         }

         var1.drawRect(this.blockX[var2], this.blockY[var2], this.blockW[var2], this.blockH[var2]);
      }
   }

   public final void drawForeground(Graphics var1, int var2, int var3) {
      if (this.foregroundGrid != null) {
         short var18 = this.viewX;
         short var19 = (short)(this.viewX + this.viewW);
         short var6 = this.viewY;
         short var7 = (short)(this.viewY + this.viewH);
         short var8 = (short)(var18 / this.tileW);
         var19 = (short)(var19 / this.tileW);
         short var9 = (short)(var6 / this.tileH);
         var7 = (short)(var7 / this.tileH);
         if (var8 < 0) {
            var8 = 0;
         }

         if (var9 < 0) {
            var9 = 0;
         }

         if (var19 > this.mapWidth / this.tileW - 1) {
            var19 = (short)(this.mapWidth / this.tileW - 1);
         }

         if (var7 > this.mapHeight / this.tileH - 1) {
            var7 = (short)(this.mapHeight / this.tileH - 1);
         }

         var1.translate(var2 - var18, var3 - var6);
         short var10;
         short var11 = var10 = (short)(var8 * this.tileW);
         short var12;
         short var13 = var12 = (short)(var9 * this.tileH);

         for (int var14 = 0; var14 < this.foregroundGrid.length; var14++) {
            for (int var15 = var9; var15 <= var7; var15++) {
               for (int var16 = var8; var16 <= var19; var16++) {
                  short var17;
                  if ((var17 = (short)((short)(this.foregroundGrid[var14][var15][var16] & 255) - 1)) >= 0) {
                     if (this.tileImages != null && this.tileImages[var17] != null) {
                        var1.drawImage(this.tileImages[var17], var10, var12, 20);
                     } else {
                        var1.drawRegion(
                           this.tilesetImages[this.tileImageIndex[var17]],
                           this.tileSrcX[var17],
                           this.tileSrcY[var17],
                           this.tileSrcW[var17],
                           this.tileSrcH[var17],
                           this.tileTransform[var17],
                           var10,
                           var12,
                           20
                        );
                     }
                  }

                  var10 = (short)(var10 + this.tileW);
               }

               var12 = (short)(var12 + this.tileH);
               var10 = var11;
            }

            var12 = var13;
         }

         var1.translate(-var2 + var18, -var3 + var6);
      } else {
         if (this.backgroundLayerCount >= 0) {
            short[] var4 = new short[this.layerCols.length - this.backgroundLayerCount];

            for (int var5 = 0; var5 < var4.length; var5++) {
               var4[var5] = (short)(this.backgroundLayerCount + var5);
            }

            this.drawLayers(var1, var4, var2, var3, new short[]{this.viewX, this.viewY, this.viewW, this.viewH});
         }
      }
   }

   private void drawLayers(Graphics var1, short[] var2, int var3, int var4, short[] var5) {
      if (var2 != null) {
         short var6;
         int var7 = (var6 = var5[0]) + var5[2];
         short var8;
         int var9 = (var8 = var5[1]) + var5[3];
         int var10 = var1.getClipX();
         int var11 = var1.getClipY();
         int var12 = var1.getClipWidth();
         int var13 = var1.getClipHeight();
         var1.setClip(var3, var4, var5[2], var5[3]);
         var1.translate(var3 - var5[0], var4 - var5[1]);
         var6 = (short)(var6 / this.tileW);
         var8 = (short)(var8 / this.tileH);
         short var19 = (short)(var7 / this.tileW + (var7 % this.tileW != 0 ? 1 : 0));
         short var21 = (short)(var9 / this.tileH + (var9 % this.tileH != 0 ? 1 : 0));
         if (var6 < 0) {
            var6 = 0;
         }

         if (var8 < 0) {
            var8 = 0;
         }

         for (int var14 = 0; var14 < var2.length; var14++) {
            for (int var15 = 0; this.layerCols[var2[var14]] != null && var15 < this.layerCols[var2[var14]].length; var15++) {
               int var16 = this.layerCols[var2[var14]][var15] * this.tileW;
               int var17 = this.layerRows[var2[var14]][var15] * this.tileH;
               if (this.tileSrcW[this.layerTiles[var2[var14]][var15]] > this.tileW || this.tileSrcH[this.layerTiles[var2[var14]][var15]] > this.tileH) {
                  if (Utils.rectsOverlap(
                     var5[0],
                     var5[1],
                     var5[2],
                     var5[3],
                     var16,
                     var17,
                     this.tileSrcW[this.layerTiles[var2[var14]][var15]],
                     this.tileSrcH[this.layerTiles[var2[var14]][var15]]
                  )) {
                     if (this.tileImages != null && this.tileImages[this.layerTiles[var2[var14]][var15]] != null) {
                        var1.drawImage(this.tileImages[this.layerTiles[var2[var14]][var15]], var16, var17, 20);
                     } else if (this.tilesetImages != null && this.tilesetImages[this.tileImageIndex[this.layerTiles[var2[var14]][var15]]] != null) {
                        var1.drawRegion(
                           this.tilesetImages[this.tileImageIndex[this.layerTiles[var2[var14]][var15]]],
                           this.tileSrcX[this.layerTiles[var2[var14]][var15]],
                           this.tileSrcY[this.layerTiles[var2[var14]][var15]],
                           this.tileSrcW[this.layerTiles[var2[var14]][var15]],
                           this.tileSrcH[this.layerTiles[var2[var14]][var15]],
                           this.tileTransform[this.layerTiles[var2[var14]][var15]],
                           var16,
                           var17,
                           20
                        );
                     }
                  }
               } else if (this.layerCols[var2[var14]][var15] >= var6
                  && this.layerCols[var2[var14]][var15] <= var19
                  && this.layerRows[var2[var14]][var15] >= var8
                  && this.layerRows[var2[var14]][var15] <= var21) {
                  if (this.tileImages != null && this.tileImages[this.layerTiles[var2[var14]][var15]] != null) {
                     var1.drawImage(this.tileImages[this.layerTiles[var2[var14]][var15]], var16, var17, 20);
                  } else if (this.tilesetImages != null && this.tilesetImages[this.tileImageIndex[this.layerTiles[var2[var14]][var15]]] != null) {
                     var1.drawRegion(
                        this.tilesetImages[this.tileImageIndex[this.layerTiles[var2[var14]][var15]]],
                        this.tileSrcX[this.layerTiles[var2[var14]][var15]],
                        this.tileSrcY[this.layerTiles[var2[var14]][var15]],
                        this.tileSrcW[this.layerTiles[var2[var14]][var15]],
                        this.tileSrcH[this.layerTiles[var2[var14]][var15]],
                        this.tileTransform[this.layerTiles[var2[var14]][var15]],
                        var16,
                        var17,
                        20
                     );
                  }
               }
            }
         }

         var1.translate(-(var3 - var5[0]), -(var4 - var5[1]));
         var1.setClip(var10, var11, var12, var13);
      }
   }

   public final boolean loadMap(String var1) {
      this.mapPath = var1;
      byte[] var2;
      if (DeviceConfig.cacheMap) {
         var2 = ResourceCache.loadBytes(var1, DeviceConfig.cacheMapGroup);
      } else {
         var2 = Utils.readResource(var1);
      }

      this.setImageDir(null);
      if (var2 != null) {
         return this.parseMap(var2);
      } else {
         System.out.println(var1 + " Không tìm thấy tệp");
         return false;
      }
   }

   public final void setViewport(int var1, int var2, int var3, int var4) {
      this.viewX = (short)var1;
      this.viewY = (short)var2;
      this.viewW = (short)var3;
      this.viewH = (short)var4;
      if (this.viewX > this.mapWidth - var3) {
         this.viewX = (short)(this.mapWidth - var3);
      }

      if (this.viewX < 0) {
         this.viewX = 0;
      }

      if (this.viewY > this.mapHeight - var4) {
         this.viewY = (short)(this.mapHeight - var4);
      }

      if (this.viewY < 0) {
         this.viewY = 0;
      }

      if (this.useScrollBuffer && this.bufferImage != null) {
         if (this.viewX - this.bufferOriginX >= this.bufferMargin) {
            var3 = (var2 = (this.bufferOriginX + this.bufferW) / this.tileW) + (this.viewX - this.bufferOriginX) / this.tileW - 1;
            var4 = this.bufferOriginY / this.tileH;
            int var5 = this.bufferRows - var4 % this.bufferRows;
            this.redrawBufferColumns(var2, var3, var5, var4);
            this.bufferOriginX = (short)(this.viewX / this.tileW * this.tileW);
         }

         if (this.viewX < this.bufferOriginX) {
            var2 = this.bufferOriginX / this.tileW - 1;
            var3 = this.viewX / this.tileW;
            var4 = this.bufferOriginY / this.tileH;
            int var18 = this.bufferRows - var4 % this.bufferRows;
            this.redrawBufferColumns(var3, var2, var18, var4);
            this.bufferOriginX = (short)(this.viewX / this.tileW * this.tileW);
         }

         if (this.viewY - this.bufferOriginY >= this.bufferMargin) {
            var3 = (var2 = (this.bufferOriginY + this.bufferH) / this.tileH) + (this.viewY - this.bufferOriginY) / this.tileH - 1;
            var4 = this.bufferOriginX / this.tileW;
            int var19 = this.bufferCols - var4 % this.bufferCols;
            this.redrawBufferRows(var2, var3, var19, var4);
            this.bufferOriginY = (short)(this.viewY / this.tileH * this.tileH);
         }

         if (this.viewY < this.bufferOriginY) {
            var2 = this.bufferOriginY / this.tileH - 1;
            var3 = this.viewY / this.tileH;
            var4 = this.bufferOriginX / this.tileW;
            int var20 = this.bufferCols - var4 % this.bufferCols;
            this.redrawBufferRows(var3, var2, var20, var4);
            this.bufferOriginY = (short)(this.viewY / this.tileH * this.tileH);
         }
      }
   }
}
