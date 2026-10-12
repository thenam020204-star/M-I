import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class DialogBox {
   private TextBlock layout;
   public boolean isVisible;
   public String[] lines;
   public byte lineIndex;
   private String speakerName;
   public String[] choices;
   public byte selectedChoice;
   short choiceScroll;
   public byte boxMode;
   public static byte modeTalk = 0;
   public static byte modeChoice = 1;
   private short boxWidth;
   private byte linesPerPage;
   private short boxMargin;
   private byte lastLine;
   private byte visibleLines;
   private byte firstVisibleLine;
   private byte typingLine;
   private byte typingChar;
   public RoleSprite speaker;
   private byte lineHeight = 16;
   private short speakerId;
   private static byte portraitFrame;
   static short choiceRows = 3;
   private Image tailImage;
   private Image arrowImage;
   private Image portraitImage;
   private Image cornerImage;
   private Image cornerImage2;
   private int speakerHeight;
   private boolean dockBottom;
   private AnimSprite portraitAnim;
   private int boxX;
   private int boxY;
   private int tailSide;
   private int tailOffset;
   private boolean tailOnTop;
   private int f_I = 20;
   private boolean isTyping;

   public DialogBox() {
      byte var1 = 2;
      this.lineHeight = (byte)vh.getHeight(Utils.defaultFont);
      if (this.lineHeight < 16) {
         this.lineHeight = 16;
      }

      this.linesPerPage = 3;
      this.boxMargin = 4;
      this.arrowImage = ResourceCache.loadImage("/pic/arrow.gf", 2);
      if (this.tailImage == null) {
         ResourceCache.loadImage("/pic/btpc.gf");
      }

      if (this.cornerImage == null) {
         ResourceCache.loadImage("/pic/jiao1.gf", 2);
      }

      if (this.cornerImage2 == null) {
         ResourceCache.loadImage("/pic/jiao2.gf", 2);
      }
   }

   public final void closeDialog() {
      GameUtil.clearPanelImages();
      this.portraitAnim = null;
      this.tailImage = null;
      this.isVisible = false;
      this.lineIndex = 0;
      this.selectedChoice = 0;
      this.boxMode = 0;
      portraitFrame = 0;
      this.arrowImage = null;
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         GameUtil.freeImageCaches();
      }

      GameCanvas.instance.world.script.advance(3);
   }

   public final void scrollUp() {
      this.firstVisibleLine = (byte)(this.firstVisibleLine - this.linesPerPage);
      if (this.firstVisibleLine < 0) {
         this.firstVisibleLine = 0;
      }
   }

   public final void scrollDown() {
      if ((this.typingLine >= this.firstVisibleLine + this.visibleLines - 1 || this.typingLine >= this.layout.plainLines.length - 1)
         && this.typingChar >= this.layout.plainLines[this.typingLine].length() - 1) {
         if (this.firstVisibleLine + this.visibleLines - 1 < this.lastLine) {
            this.firstVisibleLine = (byte)(this.firstVisibleLine + this.linesPerPage);
         }

         if (this.firstVisibleLine > this.lastLine) {
            this.firstVisibleLine = this.lastLine;
         }
      } else {
         this.typingLine = (byte)(this.firstVisibleLine + this.visibleLines - 1);
         if (this.typingLine > this.layout.plainLines.length - 1) {
            this.typingLine = (byte)(this.layout.plainLines.length - 1);
         }

         this.typingChar = (byte)(this.layout.plainLines[this.typingLine].length() - 1);
      }
   }

   public final boolean isTextComplete() {
      return this.firstVisibleLine + this.visibleLines - 1 >= this.lastLine && this.typingChar >= this.layout.plainLines[this.typingLine].length() - 1;
   }

   public final void computeBoxWidth() {
      this.boxWidth = (short)(DeviceConfig.screenSize[0] - this.boxMargin);
   }

   public final void layoutLine() {
      if (this.layout != null) {
         this.layout = null;
      }

      this.layout = new TextBlock();
      String var1 = null;
      if (this.lineIndex >= 0 && this.lines != null && this.lineIndex < this.lines.length) {
         var1 = this.lines[this.lineIndex];
      }

      this.speakerName = null;
      this.portraitImage = null;
      this.portraitAnim = null;
      int var2 = var1.indexOf(".");
      int var3 = var1.indexOf("$");
      if (var3 < 0) {
         var2 = -1;
      }

      if (var1.indexOf("@") != -1) {
         var1 = GameUtil.replaceAll(var1, "@", String.valueOf(ScriptEngine.randomValue));
      }

      if (var2 != -1) {
         this.speakerId = Utils.parseShort(var1.substring(var3 + 1, var2));
         if (var3 == 0) {
            portraitFrame = 0;
         } else {
            portraitFrame = Utils.parseByte(var1.substring(0, var3));
         }

         if (portraitFrame > 6) {
            portraitFrame = 0;
         }

         GameCanvas.instance.world.dialogSpeaker = GameCanvas.instance.world.findRoleById(this.speakerId);
         var1 = var1.substring(var2 + 1);
         String var7 = GameData.rolePortraits[this.speakerId - 1];
         this.speakerName = GameData.roleNames[this.speakerId - 1];
         if (var7 != null && !var7.equals("")) {
            if (var7.endsWith(".av")) {
               this.portraitAnim = new AnimSprite();
               this.portraitAnim.load(var7);
               this.portraitAnim.setAction(portraitFrame);
            } else {
               this.portraitImage = ResourceCache.loadImage(var7);
            }
         }

         var1 = "<0x7ff5e8>" + this.speakerName + "</>%" + var1;
      }

      this.computeBoxWidth();
      if (var1 != null) {
         if (this.portraitImage == null && this.portraitAnim == null) {
            this.layout.addTaggedText(var1, "%", this.boxWidth - 16);
         } else {
            this.layout.addTaggedText(var1, "%", this.boxWidth - 16);
         }

         this.lastLine = (byte)(this.layout.plainLines.length - 1);
      }

      this.visibleLines = this.linesPerPage;
      if (GameCanvas.instance.world.dialogSpeaker != null) {
         if (GameCanvas.instance.world.battle == null || !GameCanvas.instance.world.battle.isStoryBattle) {
            this.speakerHeight = GameCanvas.instance.world.dialogSpeaker.headHeight;
         } else if (this.speakerId != 1 && this.speakerId != 2 && this.speakerId != 3) {
            this.dockBottom = true;
         } else {
            this.speakerHeight = GameCanvas.instance.world.battle.actors[0].spriteH;
         }
      }

      this.dockBottom = true;
      boolean var5 = false;
      var2 = this.f_I - (this.boxMargin + (DeviceConfig.screenSize[0] - this.boxMargin) / 2 - DeviceConfig.screenSize[0] / 2);
      int var4;
      if (this.dockBottom) {
         var3 = this.boxMargin / 2;
         var4 = DeviceConfig.screenSize[1] - (this.visibleLines * (Utils.lineHeight + 1) + 10) - 5;
      } else if (0 <= DeviceConfig.screenSize[0] / 2) {
         var3 = 0 - this.boxWidth / 2 + this.boxMargin / 2;
         var4 = 0 - this.speakerHeight - this.visibleLines * (Utils.lineHeight + 1) - 13;
         if (var3 < this.boxMargin / 4) {
            var2 += var3 - this.boxMargin / 4;
            var3 = this.boxMargin / 4;
         }

         if (var4 < 10) {
            var5 = true;
            var4 = 11;
         }

         this.tailSide = 2;
      } else {
         var2 = -var2;
         var3 = 0 - this.boxWidth / 2 - this.boxMargin / 2;
         var4 = 0 - this.speakerHeight - this.visibleLines * (Utils.lineHeight + 1) - 10;
         if (var3 + this.boxWidth > DeviceConfig.screenSize[0] - this.boxMargin / 4) {
            var2 += var3 + this.boxWidth - (DeviceConfig.screenSize[0] - this.boxMargin / 4);
            var3 = DeviceConfig.screenSize[0] - this.boxMargin / 4 - this.boxWidth;
         }

         if (var4 < 10) {
            var5 = true;
            var4 = 11;
         }

         this.tailSide = 1;
      }

      this.tailOffset = var2;
      this.tailOnTop = var5;
      this.boxX = var3;
      this.boxY = var4;
      this.boxMode = 0;
      this.firstVisibleLine = 0;
      this.typingLine = this.firstVisibleLine;
      this.typingChar = 0;
      this.isTyping = true;
   }

   public final void draw(Graphics var1) {
      if (this.isVisible) {
         if (GameCanvas.instance.frameCounter % 3 == 0 && this.isTyping) {
            if (this.typingChar < this.layout.plainLines[this.typingLine].length() - 1) {
               this.typingChar++;
            } else if (this.typingLine < this.firstVisibleLine + this.visibleLines - 1 && this.typingLine < this.layout.plainLines.length - 1) {
               this.typingLine++;
               this.typingChar = 0;
            }

            if (this.typingLine >= this.layout.plainLines.length - 1
               && this.typingChar >= this.layout.plainLines[this.layout.plainLines.length - 1].length() - 1) {
               this.isTyping = false;
            }
         }

         if (this.boxMode == 0) {
            if (this.portraitImage != null) {
               var1.drawImage(this.portraitImage, DeviceConfig.screenSize[0], this.boxY + this.visibleLines * (Utils.lineHeight + 1) + 10 - 2, 40);
            } else if (this.portraitAnim != null) {
               this.portraitAnim.drawAt(var1, DeviceConfig.screenSize[0] - 30, DeviceConfig.screenSize[1]);
            }

            int var10003 = this.boxY + 5;
            int var10005 = this.visibleLines * (Utils.lineHeight + 1) + 10;
            int var11 = this.tailOnTop;
            int var10 = this.tailOffset;
            int var9 = this.tailSide;
            int var8 = var10005;
            int var7 = this.boxWidth;
            int var6 = var10003;
            int var5 = this.boxX;
            if (!this.dockBottom) {
               if (!var11) {
                  var1.setClip(0, 0, DeviceConfig.screenSize[0], var6 + var8 - 1);
               } else {
                  var1.setClip(0, var6 + 1, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
               }
            }

            vh.boxAt(var1, var5, var6, var7, var8, 1317669, 15788720, 80, true);
            var1.setColor(2170141);
            vh.nop6(var1, var5 - 1, var6 - 1, var7 + 1, var8 + 1, 10, 10);
            var1.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
            if (this.cornerImage != null) {
               var1.drawRegion(this.cornerImage, 0, 0, this.cornerImage.getWidth() >> 1, this.cornerImage.getHeight() >> 1, 0, var5 - 1, var6 - 1, 20);
               var1.drawRegion(
                  this.cornerImage,
                  this.cornerImage.getWidth() >> 1,
                  0,
                  this.cornerImage.getWidth() >> 1,
                  this.cornerImage.getHeight() >> 1,
                  0,
                  var5 + 1 + var7,
                  var6 - 1,
                  24
               );
            }

            if (this.cornerImage2 != null) {
               var1.drawRegion(this.cornerImage2, 0, 0, this.cornerImage2.getWidth() >> 1, this.cornerImage2.getHeight(), 0, var5 - 1, var6 + 1 + var8, 36);
               var1.drawRegion(
                  this.cornerImage2,
                  this.cornerImage2.getWidth() >> 1,
                  0,
                  this.cornerImage2.getWidth() >> 1,
                  this.cornerImage2.getHeight(),
                  0,
                  var5 + var7 + 1,
                  var6 + 1 + var8,
                  40
               );
            } else if (this.cornerImage != null) {
               var1.drawRegion(
                  this.cornerImage,
                  0,
                  this.cornerImage.getHeight() >> 1,
                  this.cornerImage.getWidth() >> 1,
                  this.cornerImage.getHeight() >> 1,
                  0,
                  var5 - 1,
                  var6 + 1 + var8,
                  36
               );
               var1.drawRegion(
                  this.cornerImage,
                  this.cornerImage.getWidth() >> 1,
                  this.cornerImage.getHeight() >> 1,
                  this.cornerImage.getWidth() >> 1,
                  this.cornerImage.getHeight() >> 1,
                  0,
                  var5 + var7 + 1,
                  var6 + 1 + var8,
                  40
               );
            }

            if (!this.dockBottom) {
               if (!var11) {
                  var1.setColor(15788720);
                  vh.nop4(var1, var5 + 2, var6 + var8 - 1, (var7 - 20) / 2 - 2 + var10, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 + var8 - 1, (var7 - 20) / 2 - 1 - var10, 1);
                  var1.setColor(2170141);
                  vh.nop4(var1, var5 + 2, var6 + var8, (var7 - 20) / 2 - 2 + var10, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 + var8, (var7 - 20) / 2 - 1 - var10, 1);
                  vh.nop4(var1, var5 + 1, var6 + var8 - 1, 1, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + (var7 - 20) / 2 - 1, var6 + var8 - 1, 1, 1);
               } else {
                  var1.setColor(15788720);
                  vh.nop4(var1, var5 + 2, var6, (var7 - 20) / 2 - 2 + var10, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6, (var7 - 20) / 2 - 1 - var10, 1);
                  var1.setColor(2170141);
                  vh.nop4(var1, var5 + 2, var6 - 1, (var7 - 20) / 2 - 2 + var10, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + var10, var6 - 1, (var7 - 20) / 2 - 1 - var10, 1);
                  vh.nop4(var1, var5 + 1, var6, 1, 1);
                  vh.nop4(var1, var5 + var7 / 2 + 9 + (var7 - 20) / 2 - 1, var6, 1, 1);
               }

               if (this.tailImage != null) {
                  if (!var11) {
                     if (var9 == 1) {
                        var1.drawRegion(this.tailImage, 0, 0, 28, 11, 0, var5 + var7 / 2 - 10 + var10, var6 + var8 - 1, 20);
                     }

                     if (var9 == 2) {
                        var1.drawRegion(this.tailImage, 28, 0, 28, 11, 0, var5 + var7 / 2 - 10 - 9 + var10, var6 + var8 - 1, 20);
                     }
                  } else {
                     if (var9 == 1) {
                        var1.drawRegion(this.tailImage, 0, 11, 28, 11, 0, var5 + var7 / 2 - 10 + var10, var6 + 1, 36);
                     }

                     if (var9 == 2) {
                        var1.drawRegion(this.tailImage, 28, 11, 28, 11, 0, var5 + var7 / 2 - 10 - 9 + var10, var6 + 1, 36);
                     }
                  }
               }
            }

            var1.setColor(8401696);

            for (int var15 = this.firstVisibleLine; var15 < this.firstVisibleLine + this.visibleLines; var15++) {
               if (this.layout.plainLines != null && var15 < this.layout.plainLines.length) {
                  if (var15 < this.typingLine) {
                     if (this.layout.plainLines != null) {
                        this.layout
                           .drawTaggedLine(
                              var1,
                              this.layout.plainLines[var15],
                              var15,
                              this.boxX + 3 + 8,
                              this.boxY + 10 + this.lineHeight * (var15 - this.firstVisibleLine),
                              16777215
                           );
                     }
                  } else if (var15 == this.typingLine && this.layout.plainLines != null) {
                     TextBlock var10000 = this.layout;
                     String var10002 = this.layout.plainLines[var15].substring(0, this.typingChar + 1);
                     int var10004 = this.typingChar + 1;
                     var10005 = this.boxX + 3 + 8;
                     int var10006 = this.boxY + 10 + this.lineHeight * (var15 - this.firstVisibleLine);
                     var10 = 16777215;
                     var9 = var10006;
                     var8 = var10005;
                     var7 = var10004;
                     var6 = var15;
                     String var18 = var10002;
                     Graphics var17 = var1;
                     TextBlock var14 = var10000;
                     var11 = (boolean)0;
                     String[][] var25 = null;

                     for (int var12 = 0; var14.colorSpans != null && var12 < var14.colorSpans.length; var12++) {
                        int var13 = Utils.parseInt(var14.colorSpans[var12][0]);
                        if (var6 == var13) {
                           var11 = (boolean)1;
                           var25 = GameUtil.append(var25, var14.colorSpans[var12]);
                        }
                     }

                     if (!var11) {
                        var1.setColor(16777215);
                        vh.drawString(var1, var18, var8, var9, 20);
                     } else if (var7 >= var14.plainLines[var6].length()) {
                        var14.drawTaggedLine(var1, var14.plainLines[var6].substring(0, var7), var6, var8, var9, 16777215);
                     } else {
                        for (int var28 = 0; var25 != null && var28 < var25.length; var28++) {
                           if (Utils.parseInt(var25[var28][0]) == var6) {
                              var5 = Utils.parseInt(var25[var28][1]);
                              var11 = Utils.parseInt(var25[var28][2]);
                              if (var7 >= var5 && var7 <= var11) {
                                 var14.drawTaggedLine(var17, var14.plainLines[var6].substring(0, var5), var6, var8, var9, 16777215);
                                 if (!var25[var28][3].startsWith("0x") && !var25[var28][3].startsWith("0X")) {
                                    var17.setColor(var14.tagColors[Utils.parseInt(var25[var28][3]) - 1]);
                                 } else {
                                    var17.setColor(Utils.parseInt(var25[var28][3]));
                                 }

                                 vh.drawString(
                                    var17,
                                    var14.plainLines[var6].substring(var5, var7),
                                    var8 + vh.stringWidth(Utils.defaultFont, var14.plainLines[var6].substring(0, var5)),
                                    var9,
                                    20
                                 );
                                 break;
                              }

                              var14.drawTaggedLine(var17, var14.plainLines[var6].substring(0, var7), var6, var8, var9, 16777215);
                           }
                        }
                     }
                  }
               }
            }
         } else {
            if (this.boxMode == modeChoice) {
               byte var2;
               if (GameCanvas.instance.frameCounter % 12 == 0) {
                  var2 = 3;
               } else {
                  var2 = 0;
               }

               choiceRows = 3;
               int var3 = DeviceConfig.screenSize[1] / 2 - (choiceRows * (Utils.lineHeight + 1) + 10) / 2;
               int var4 = choiceRows * (Utils.lineHeight + 1) + 10;
               if (this.choices.length < choiceRows) {
                  var4 = this.choices.length * (Utils.lineHeight + 1) + 10;
                  var3 = DeviceConfig.screenSize[1] / 2 - (this.choices.length * (Utils.lineHeight + 1) + 10) / 2;
               }

               vh.boxAt(var1, 20, var3, DeviceConfig.screenSize[0] - 40, var4, 1317669, 15788720, 80, true);
               var1.setColor(2170141);
               vh.nop6(var1, 19, var3 - 1, DeviceConfig.screenSize[0] - 40 + 1, var4 + 1, 10, 10);
               if (this.cornerImage != null) {
                  var1.drawRegion(this.cornerImage, 0, 0, this.cornerImage.getWidth() >> 1, this.cornerImage.getHeight() >> 1, 0, 19, var3 - 1, 20);
                  var1.drawRegion(
                     this.cornerImage,
                     this.cornerImage.getWidth() >> 1,
                     0,
                     this.cornerImage.getWidth() >> 1,
                     this.cornerImage.getHeight() >> 1,
                     0,
                     21 + DeviceConfig.screenSize[0] - 40,
                     var3 - 1,
                     24
                  );
                  var1.drawRegion(
                     this.cornerImage,
                     0,
                     this.cornerImage.getWidth() >> 1,
                     this.cornerImage.getHeight() >> 1,
                     this.cornerImage.getHeight() >> 1,
                     0,
                     19,
                     var3 + 1 + var4,
                     36
                  );
                  var1.drawRegion(
                     this.cornerImage,
                     this.cornerImage.getWidth() >> 1,
                     this.cornerImage.getHeight() >> 1,
                     this.cornerImage.getWidth() >> 1,
                     this.cornerImage.getHeight() >> 1,
                     0,
                     21 + DeviceConfig.screenSize[0] - 40,
                     var3 + 1 + var4,
                     40
                  );
               }

               if (this.choices.length > choiceRows) {
                  GameUtil.drawScrollBar(
                     var1,
                     null,
                     null,
                     DeviceConfig.screenSize[0] - 35,
                     var3 + 5,
                     choiceRows * (Utils.lineHeight + 1),
                     this.choices.length,
                     choiceRows,
                     this.choiceScroll
                  );
               }

               for (int var16 = 0; var16 < choiceRows; var16++) {
                  if (var16 + this.choiceScroll < this.choices.length) {
                     if (var16 + this.choiceScroll == this.selectedChoice) {
                        GameUtil.drawBoldText(
                           var1,
                           this.choices[var16 + this.choiceScroll],
                           DeviceConfig.screenSize[0] / 2,
                           var3 + 5 + var16 * (Utils.lineHeight + 1),
                           2093589,
                           17
                        );
                        if (this.choices.length > 1 && this.arrowImage != null) {
                           var1.drawRegion(
                              this.arrowImage,
                              0,
                              0,
                              15,
                              15,
                              0,
                              DeviceConfig.screenSize[0] / 2 - vh.stringWidth(Utils.defaultFont, this.choices[var16 + this.choiceScroll]) / 2 - 10 + var2,
                              var3 + 5 + var16 * (Utils.lineHeight + 1) + Utils.lineHeight / 2,
                              3
                           );
                        }
                     } else {
                        var1.setColor(16777215);
                        vh.drawString(
                           var1, this.choices[var16 + this.choiceScroll], DeviceConfig.screenSize[0] / 2, var3 + 5 + var16 * (Utils.lineHeight + 1), 17
                        );
                     }
                  }
               }
            }
         }
      }
   }

   public final void nextLine() {
      if ((this.typingLine >= this.firstVisibleLine + this.visibleLines - 1 || this.typingLine >= this.layout.plainLines.length - 1)
         && this.typingChar >= this.layout.plainLines[this.typingLine].length() - 1) {
         if (this.lineIndex < this.lines.length - 1) {
            this.lineIndex++;
            if (this.lineIndex > this.lines.length - 1) {
               this.lineIndex = (byte)(this.lines.length - 1);
            }

            this.firstVisibleLine = 0;
            this.layoutLine();
         }
      } else {
         this.typingLine = (byte)(this.firstVisibleLine + this.visibleLines - 1);
         if (this.typingLine > this.layout.plainLines.length - 1) {
            this.typingLine = (byte)(this.layout.plainLines.length - 1);
         }

         this.typingChar = (byte)(this.layout.plainLines[this.typingLine].length() - 1);
         this.isTyping = false;
      }
   }
}
