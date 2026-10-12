import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class MultiTapInput {
   private String text;
   private int inputType;
   private int tapIndex;
   private int currentKey;
   private int commitTimer;
   private int caseMode;
   private boolean isSymbolPickerOpen;
   private int symbolIndex;
   private int symbolCols;
   private int symbolRows;
   private char[][] keyChars;
   private char[] symbols;
   private String selectLabel;
   private String cancelLabel;
   private int maxLength;
   private PortalClient client;

   public MultiTapInput(int var1, int var2, PortalClient var3) {
      this.text = "";
      this.isSymbolPickerOpen = false;
      this.symbolIndex = 0;
      this.keyChars = new char[][]{
         {'0', '0', '+', '-', '=', '_', '*', '/', '\\', '|', ',', '.', '?', ':', ';', '\'', '"', '<', '>', '[', ']', '{', '}', '`'},
         {'1', '1', ' ', '#', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'},
         {'2', '2', 'a', 'b', 'c'},
         {'3', '3', 'd', 'e', 'f'},
         {'4', '4', 'g', 'h', 'i'},
         {'5', '5', 'j', 'k', 'l'},
         {'6', '6', 'm', 'n', 'o'},
         {'7', '7', 'p', 'q', 'r', 's'},
         {'8', '8', 't', 'u', 'v'},
         {'9', '9', 'w', 'x', 'y', 'z'}
      };
      this.symbols = new char[]{
         ' ',
         '#',
         '~',
         '!',
         '@',
         '#',
         '$',
         '%',
         '^',
         '&',
         '*',
         '(',
         ')',
         '+',
         '-',
         '=',
         '_',
         '*',
         '/',
         '\\',
         '|',
         ',',
         '.',
         '?',
         ':',
         ';',
         '\'',
         '"',
         '<',
         '>',
         '[',
         ']',
         '{',
         '}',
         '`'
      };
      this.selectLabel = "Chọn";
      this.cancelLabel = "Hủy";
      this.maxLength = 12;
      this.client = var3;
      this.inputType = var1;
      this.maxLength = var2;
      if (var1 == 65536) {
         this.inputType = 0;
      }

      if (this.inputType == 0) {
         this.caseMode = 1;
      }
   }

   public final String getText() {
      return this.text;
   }

   public final boolean isSymbolPickerOpen() {
      return this.isSymbolPickerOpen;
   }

   public final boolean handleKey(int var1, int var2) {
      char var3 = this.inputType == 2;
      boolean var4 = this.inputType == 0;
      if (var3) {
         if (this.handleTapKey(var1, var2)) {
            return true;
         }
      } else if (var4) {
         if (var1 == 42) {
            if (!this.isSymbolPickerOpen) {
               this.commitPending();
               this.commitTimer = 0;
               if (this.text.length() >= this.maxLength) {
                  return true;
               }
            }

            this.isSymbolPickerOpen = !this.isSymbolPickerOpen;
            return true;
         }

         if (this.isSymbolPickerOpen) {
            switch (var2) {
               case 1:
                  this.symbolIndex = this.symbolIndex - this.symbolCols;
                  if (this.symbolIndex < 0) {
                     var1 = (this.symbolIndex + this.symbolCols) % this.symbolCols;
                     var2 = this.symbols.length % this.symbolCols;
                     if (var1 < var2) {
                        this.symbolIndex = this.symbols.length / this.symbolCols * this.symbolCols + var1;
                     } else {
                        this.symbolIndex = (this.symbols.length / this.symbolCols - 1) * this.symbolCols + var1;
                     }
                  }

                  return true;
               case 2:
                  this.symbolIndex--;
                  if (this.symbolIndex < 0) {
                     this.symbolIndex = this.symbols.length - 1;
                  }

                  return true;
               case 3:
               case 4:
               case 7:
               default:
                  return false;
               case 5:
                  this.symbolIndex++;
                  if (this.symbolIndex >= this.symbols.length) {
                     this.symbolIndex = 0;
                  }

                  return true;
               case 6:
                  this.symbolIndex = this.symbolIndex + this.symbolCols;
                  if (this.symbolIndex >= this.symbols.length) {
                     this.symbolIndex = this.symbolIndex % this.symbolCols;
                  }

                  return true;
               case 8:
                  String var6 = this.selectLabel;
                  if (this.isSymbolPickerOpen) {
                     if (var6 == this.selectLabel) {
                        this.isSymbolPickerOpen = false;
                        String var7 = this.text;
                        var3 = this.symbols[this.symbolIndex];
                        String var8 = var7 + var3;
                        this.text = var8;
                     } else if (var6 == this.cancelLabel) {
                        this.isSymbolPickerOpen = false;
                     }
                  }

                  return true;
            }
         }

         if (this.handleTapKey(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   private boolean handleTapKey(int var1, int var2) {
      if (var1 == 35 && this.inputType == 0) {
         this.caseMode++;
         if (this.caseMode > 2) {
            this.caseMode = 0;
         }

         return true;
      } else if ((var2 != 2 || var1 == 52) && var1 != -8 && var1 != -12 && var1 != -12345) {
         var2 = var1;
         MultiTapInput var6 = this;
         int var3 = 0;

         int var10000;
         while (true) {
            if (var3 >= var6.keyChars.length) {
               var10000 = -1;
               break;
            }

            if (var6.keyChars[var3][0] == var2) {
               var10000 = var3;
               break;
            }

            var3++;
         }

         var1 = var10000;
         if (var10000 < 0) {
            this.commitPending();
            this.commitTimer = 0;
            return false;
         } else if (this.text.length() >= this.maxLength) {
            return true;
         } else {
            switch (this.caseMode) {
               case 0:
                  char var8;
                  if ((var8 = this.keyCharAt(var1, 0)) >= '0' && var8 <= '9') {
                     String var10 = this.text;
                     String var11 = var10 + var8;
                     this.text = var11;
                  }
                  break;
               case 1:
               case 2:
                  if (this.commitTimer > 0) {
                     if (this.currentKey == var1) {
                        this.tapIndex = (this.tapIndex + 1) % (this.keyChars[this.currentKey].length - 1);
                        this.commitTimer = 14;
                        return true;
                     }

                     this.commitPending();
                     this.commitTimer = 0;
                     if (this.text.length() >= this.maxLength) {
                        return true;
                     }
                  }

                  this.commitTimer = 16;
                  this.currentKey = var1;
                  this.tapIndex = 1;
            }

            return true;
         }
      } else {
         String var4 = this.text;
         if (this.text.length() > 0) {
            String var5 = var4.substring(0, var4.length() - 1);
            this.text = var5;
         }

         return true;
      }
   }

   public final void tick() {
      if (this.commitTimer > 0) {
         this.commitTimer--;
         if (this.commitTimer == 1) {
            this.commitPending();
         }
      }
   }

   private void commitPending() {
      if (this.commitTimer > 0) {
         String var1 = this.text;
         char var2 = this.pendingChar();
         var1 = var1 + var2;
         this.text = var1;
      }
   }

   private char pendingChar() {
      char var1 = this.keyCharAt(this.currentKey, this.tapIndex);
      if (this.caseMode == 2 && var1 >= 'a' && var1 <= 'z') {
         var1 = (char)(var1 - 'a' + 65);
      }

      return var1;
   }

   private char keyCharAt(int var1, int var2) {
      return this.keyChars[var1][var2 + 1];
   }

   public final void drawCursor(Graphics var1, int var2, int var3, int var4) {
      if (System.currentTimeMillis() % 1500L < 1000L) {
         var1.setColor(1397123);
         var1.drawLine(var2, var3, var2, var3 + vh.getHeight(var1.getFont()) - 3);
      }

      if (this.commitTimer > 0) {
         char var5 = this.pendingChar();
         Font var6;
         int var7 = vh.charWidth(var6 = var1.getFont(), var5);
         int var8 = vh.getHeight(var6) + 1;
         var1.setColor(255);
         var1.fillRect(var2, var3, var7, var8);
         var1.setColor(16777215);
         var1.drawChar(var5, var2, var3 + var8 - 2, 36);
      }

      if (this.inputType == 0) {
         String var12 = "abc";
         if (this.caseMode == 0) {
            var12 = "123";
         } else if (this.caseMode == 1) {
            var12 = "abc";
         } else if (this.caseMode == 2) {
            var12 = "ABC";
         }

         var1.setColor(16777215);
         int var13 = vh.stringWidth(var1.getFont(), var12);
         int var14 = var4 - var13 - 2;
         var2 = var3 + vh.getHeight(var1.getFont()) - 1;
         var3 = var13 + 4;
         var4 = vh.getHeight(var1.getFont()) + 2;
         var1.fillRoundRect(var14, var2, var3, var4, 4, 4);
         var1.setColor(0);
         var1.drawRoundRect(var14, var2, var3, var4, 4, 4);
         vh.drawString(var1, var12, var14 + 2, var2 + 1, 20);
      }
   }

   public final void drawSymbolPicker(Graphics var1) {
      if (this.isSymbolPickerOpen) {
         int var2 = vh.getHeight(var1.getFont()) + 2;
         int var3 = vh.charWidth(var1.getFont(), '国') + 2;
         var2 = Math.max(var2, var3);
         var3 = this.client.screenWidth;
         this.symbolCols = var3 / var2 - 1;
         this.symbolRows = (this.symbols.length + this.symbolCols - 1) / this.symbolCols;
         int var4 = (this.symbolRows + 1) * var2;
         var3 = (var3 - this.symbolCols * var2) / 2;
         int var5 = this.client.screenHeight - var4 - (var2 << 1);
         var1.setColor(4473924);
         var1.fillRect(var3, var5, this.symbolCols * var2, var4);
         var1.setColor(8947848);
         var1.drawRect(var3, var5, this.symbolCols * var2, var4);
         var1.setColor(11184810);
         var1.fillRect(var3 + this.symbolIndex % this.symbolCols * var2, var5 + var2 + this.symbolIndex / this.symbolCols * var2, var2, var2);
         if (var5 < 0) {
            var5 = 0;
         }

         var1.setColor(16777215);
         vh.drawString(var1, "Chọn ký hiệu:", var3 + 5, var5 + 1, 20);
         var5 += var2;
         var1.setColor(8947848);

         for (int var11 = 0; var11 <= this.symbolRows; var11++) {
            var1.drawLine(var3, var5 + var11 * var2, var3 + var2 * this.symbolCols, var5 + var11 * var2);
         }

         for (int var12 = 0; var12 <= this.symbolCols; var12++) {
            var1.drawLine(var3 + var12 * var2, var5, var3 + var12 * var2, var5 + var2 * this.symbolRows);
         }

         var4 = (var2 - vh.getHeight(var1.getFont()) + 1) / 2;
         var1.setColor(16777215);

         for (int var6 = 0; var6 < this.symbolRows; var6++) {
            for (int var7 = 0; var7 < this.symbolCols; var7++) {
               if (var6 * this.symbolCols + var7 < this.symbols.length) {
                  var1.drawChar(this.symbols[var6 * this.symbolCols + var7], var3 + var7 * var2 + var2 / 2, var5 + var6 * var2 + var4, 17);
               }
            }
         }
      }
   }

   public final int getCommitTimer() {
      return this.commitTimer;
   }

   public MultiTapInput() {
   }

   public static BattleActor newBattleActor() {
      return new BattleActor();
   }
}
