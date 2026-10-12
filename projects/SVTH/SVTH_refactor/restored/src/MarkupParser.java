import java.util.Vector;

public final class MarkupParser {
   private volatile boolean isParsing = false;
   private volatile boolean isReentered = false;
   private Vector tags = new Vector(30);

   public MarkupParser() {
   }

   public final int tagCount() {
      return this.tags.size();
   }

   public final MarkupTag tagAt(int var1) {
      return (MarkupTag)this.tags.elementAt(var1);
   }

   public final void parse(String var1) {
      if (var1 != null) {
         char[] var2 = var1.toCharArray();
         StringBuffer var3 = new StringBuffer();
         MarkupTag var4 = new MarkupTag();
         MarkupAttribute var5 = null;
         char var6 = 0;
         boolean var7 = true;
         byte var8 = 0;
         if (this.isParsing) {
            this.isReentered = true;
         }

         this.isParsing = true;

         for (int var10 = 0; var10 < var2.length; var10++) {
            switch (var8) {
               case 0:
                  switch (var2[var10]) {
                     case '<':
                        var8 = 1;
                        if (var3.length() > 0) {
                           var3.delete(0, var3.length());
                        }
                        continue;
                     default:
                        var3.append(var2[var10]);
                        continue;
                  }
               case 1:
                  if ((var2[var10] < 'a' || var2[var10] > 'z')
                     && (var2[var10] < 'A' || var2[var10] > 'Z')
                     && var2[var10] != ':'
                     && var2[var10] != '_'
                     && (var2[var10] < 192 || var2[var10] > 214)
                     && (var2[var10] < 216 || var2[var10] > 246)
                     && (var2[var10] < 248 || var2[var10] > 255)
                     && (var2[var10] < 880 || var2[var10] > 893)
                     && (var2[var10] < 895 || var2[var10] > 8191)
                     && (var2[var10] < 8204 || var2[var10] > 8205)
                     && (var2[var10] < 8304 || var2[var10] > 8591)
                     && (var2[var10] < 11264 || var2[var10] > 12271)
                     && (var2[var10] < 12289 || var2[var10] > '\ud7ff')
                     && (var2[var10] < '豈' || var2[var10] > '﷏')
                     && (var2[var10] < 'ﷰ' || var2[var10] > '�')) {
                     switch (var2[var10]) {
                        case '!':
                           var8 = 9;
                        case '/':
                        default:
                           break;
                        case '>':
                           var8 = 7;
                           var10--;
                           break;
                        case '?':
                           var8 = 8;
                     }
                  } else {
                     var8 = 2;
                     var10--;
                  }
                  break;
               case 2:
                  switch (var2[var10]) {
                     case '\t':
                     case '\n':
                     case '\r':
                     case ' ':
                        var8 = 3;
                        if (var3.length() > 0) {
                           var4.name = var3.toString();
                           var3.delete(0, var3.length());
                        }
                        continue;
                     case '/':
                        if (var2.length > var10 + 1 && var2[var10 + 1] == '>') {
                        }
                        continue;
                     case '>':
                        var8 = 7;
                        var4.name = var3.toString();
                        var3.delete(0, var3.length());
                        continue;
                     default:
                        var3.append(var2[var10]);
                        continue;
                  }
               case 3:
                  if ((var2[var10] < 'a' || var2[var10] > 'z')
                     && (var2[var10] < 'A' || var2[var10] > 'Z')
                     && var2[var10] != ':'
                     && var2[var10] != '_'
                     && (var2[var10] < 192 || var2[var10] > 214)
                     && (var2[var10] < 216 || var2[var10] > 246)
                     && (var2[var10] < 248 || var2[var10] > 255)
                     && (var2[var10] < 880 || var2[var10] > 893)
                     && (var2[var10] < 895 || var2[var10] > 8191)
                     && (var2[var10] < 8204 || var2[var10] > 8205)
                     && (var2[var10] < 8304 || var2[var10] > 8591)
                     && (var2[var10] < 11264 || var2[var10] > 12271)
                     && (var2[var10] < 12289 || var2[var10] > '\ud7ff')
                     && (var2[var10] < '豈' || var2[var10] > '﷏')
                     && (var2[var10] < 'ﷰ' || var2[var10] > '�')) {
                     switch (var2[var10]) {
                        case '/':
                           if (var2.length > var10 + 1 && var2[var10 + 1] == '>') {
                           }
                           break;
                        case '>':
                           var8 = 7;
                           var10--;
                     }
                  } else {
                     var8 = 4;
                     var10--;
                  }
                  break;
               case 4:
                  switch (var2[var10]) {
                     case '=':
                        var8 = 5;
                        if (var3.length() > 0) {
                           var5 = new MarkupAttribute(var3.toString());
                           var3.delete(0, var3.length());
                        }
                        continue;
                     default:
                        var3.append(var2[var10]);
                        continue;
                  }
               case 5:
                  switch (var2[var10]) {
                     case '"':
                     case '\'':
                        var6 = var2[var10];
                        var8 = 6;
                        continue;
                     case '/':
                        if (var2.length > var10 + 1 && var2[var10 + 1] == '>') {
                        }
                        continue;
                     case '>':
                        var8 = 7;
                        if (var3.length() > 0) {
                           var5.setValue(var3.toString());
                           var3.delete(0, var3.length());
                        }

                        var4.addAttribute(var5);
                        var10--;
                        continue;
                     default:
                        var3.append(var2[var10]);
                        continue;
                  }
               case 6:
                  if (var2[var10] == var6) {
                     var8 = 3;
                     if (var3.length() > 0) {
                        var5.setValue(var3.toString());
                        var3.delete(0, var3.length());
                     } else {
                        var5.setValue("");
                     }

                     var4.addAttribute(var5);
                  } else {
                     var3.append(var2[var10]);
                  }
                  break;
               case 7:
                  if (!var4.name.equals("tml")) {
                     this.tags.addElement(var4);
                  }

                  var4 = new MarkupTag();
                  var8 = 0;
                  var10--;
                  break;
               case 8:
                  if (var2[var10] == '>') {
                     var8 = 0;
                  }
                  break;
               case 9:
                  if (var2[var10] == '>') {
                     var8 = 0;
                     var7 = true;
                  } else if (var2[var10] == '-' && var2.length >= var10 + 1 && var2[var10 + 1] == '-' && var7) {
                     var8 = 10;
                     var7 = true;
                     var10++;
                  } else {
                     var7 = false;
                  }
                  break;
               case 10:
                  if (var2[var10] == '-' && var2.length >= var10 + 2 && var2[var10 + 1] == '-' && var2[var10 + 2] == '>') {
                     var8 = 0;
                     var10 += 2;
                  }
            }
         }
      }
   }
}
