public final class Md5Text {
   private String HEX_CHARS = "0123456789abcdef";

   public Md5Text() {
   }

   private String wordToHex(int var1) {
      String var2 = "";

      for (int var3 = 0; var3 <= 3; var3++) {
         var2 = var2 + this.HEX_CHARS.charAt(var1 >> (var3 << 3) + 4 & 15) + this.HEX_CHARS.charAt(var1 >> (var3 << 3) & 15);
      }

      return var2;
   }

   private static int safeAdd(int var0, int var1) {
      return (var0 & 2147483647) + (var1 & 2147483647) ^ var0 & -2147483648 ^ var1 & -2147483648;
   }

   private int cmn(int var1, int var2, int var3, int var4, int var5, int var6) {
      int var7;
      return safeAdd((var7 = safeAdd(safeAdd(var2, var1), safeAdd(var4, var6))) << var5 | var7 >>> 32 - var5, var3);
   }

   private int ff(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return this.cmn(var2 & var3 | ~var2 & var4, var1, var2, var5, var6, var7);
   }

   private int gg(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return this.cmn(var2 & var4 | var3 & ~var4, var1, var2, var5, var6, var7);
   }

   private int hh(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return this.cmn(var2 ^ var3 ^ var4, var1, var2, var5, var6, var7);
   }

   private int ii(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return this.cmn(var3 ^ (var2 | ~var4), var1, var2, var5, var6, var7);
   }

   public final String md5Hex(String var1) {
      int var2;
      String var11;
      int[] var3 = new int[(var2 = ((var11 = var1).length() + 8 >> 6) + 1) << 4];

      for (int var4 = 0; var4 < var2 << 4; var4++) {
         var3[var4] = 0;
      }

      int var47;
      for (var47 = 0; var47 < var11.length(); var47++) {
         var3[var47 >> 2] = var3[var47 >> 2] | var11.charAt(var47) << (var47 % 4 << 3);
      }

      var3[var47 >> 2] = var3[var47 >> 2] | 128 << (var47 % 4 << 3);
      var3[(var2 << 4) - 2] = var11.length() << 3;
      int[] var12 = var3;
      var2 = 1732584193;
      int var30 = -271733879;
      var47 = -1732584194;
      int var5 = 271733878;

      for (byte var6 = 0; var6 < var12.length; var6 += 16) {
         int var14 = this.ff(var2, var30, var47, var5, var12[var6], 7, -680876936);
         int var65 = this.ff(var5, var14, var30, var47, var12[var6 + 1], 12, -389564586);
         int var49 = this.ff(var47, var65, var14, var30, var12[var6 + 2], 17, 606105819);
         int var31 = this.ff(var30, var49, var65, var14, var12[var6 + 3], 22, -1044525330);
         int var15 = this.ff(var14, var31, var49, var65, var12[var6 + 4], 7, -176418897);
         int var66 = this.ff(var65, var15, var31, var49, var12[var6 + 5], 12, 1200080426);
         int var50 = this.ff(var49, var66, var15, var31, var12[var6 + 6], 17, -1473231341);
         int var32 = this.ff(var31, var50, var66, var15, var12[var6 + 7], 22, -45705983);
         int var16 = this.ff(var15, var32, var50, var66, var12[var6 + 8], 7, 1770035416);
         int var67 = this.ff(var66, var16, var32, var50, var12[var6 + 9], 12, -1958414417);
         int var51 = this.ff(var50, var67, var16, var32, var12[var6 + 10], 17, -42063);
         int var33 = this.ff(var32, var51, var67, var16, var12[var6 + 11], 22, -1990404162);
         int var17 = this.ff(var16, var33, var51, var67, var12[var6 + 12], 7, 1804603682);
         int var68 = this.ff(var67, var17, var33, var51, var12[var6 + 13], 12, -40341101);
         int var52 = this.ff(var51, var68, var17, var33, var12[var6 + 14], 17, -1502002290);
         int var34 = this.ff(var33, var52, var68, var17, var12[var6 + 15], 22, 1236535329);
         int var18 = this.gg(var17, var34, var52, var68, var12[var6 + 1], 5, -165796510);
         int var69 = this.gg(var68, var18, var34, var52, var12[var6 + 6], 9, -1069501632);
         int var53 = this.gg(var52, var69, var18, var34, var12[var6 + 11], 14, 643717713);
         int var35 = this.gg(var34, var53, var69, var18, var12[var6], 20, -373897302);
         int var19 = this.gg(var18, var35, var53, var69, var12[var6 + 5], 5, -701558691);
         int var70 = this.gg(var69, var19, var35, var53, var12[var6 + 10], 9, 38016083);
         int var54 = this.gg(var53, var70, var19, var35, var12[var6 + 15], 14, -660478335);
         int var36 = this.gg(var35, var54, var70, var19, var12[var6 + 4], 20, -405537848);
         int var20 = this.gg(var19, var36, var54, var70, var12[var6 + 9], 5, 568446438);
         int var71 = this.gg(var70, var20, var36, var54, var12[var6 + 14], 9, -1019803690);
         int var55 = this.gg(var54, var71, var20, var36, var12[var6 + 3], 14, -187363961);
         int var37 = this.gg(var36, var55, var71, var20, var12[var6 + 8], 20, 1163531501);
         int var21 = this.gg(var20, var37, var55, var71, var12[var6 + 13], 5, -1444681467);
         int var72 = this.gg(var71, var21, var37, var55, var12[var6 + 2], 9, -51403784);
         int var56 = this.gg(var55, var72, var21, var37, var12[var6 + 7], 14, 1735328473);
         int var38 = this.gg(var37, var56, var72, var21, var12[var6 + 12], 20, -1926607734);
         int var22 = this.hh(var21, var38, var56, var72, var12[var6 + 5], 4, -378558);
         int var73 = this.hh(var72, var22, var38, var56, var12[var6 + 8], 11, -2022574463);
         int var57 = this.hh(var56, var73, var22, var38, var12[var6 + 11], 16, 1839030562);
         int var39 = this.hh(var38, var57, var73, var22, var12[var6 + 14], 23, -35309556);
         int var23 = this.hh(var22, var39, var57, var73, var12[var6 + 1], 4, -1530992060);
         int var74 = this.hh(var73, var23, var39, var57, var12[var6 + 4], 11, 1272893353);
         int var58 = this.hh(var57, var74, var23, var39, var12[var6 + 7], 16, -155497632);
         int var40 = this.hh(var39, var58, var74, var23, var12[var6 + 10], 23, -1094730640);
         int var24 = this.hh(var23, var40, var58, var74, var12[var6 + 13], 4, 681279174);
         int var75 = this.hh(var74, var24, var40, var58, var12[var6], 11, -358537222);
         int var59 = this.hh(var58, var75, var24, var40, var12[var6 + 3], 16, -722521979);
         int var41 = this.hh(var40, var59, var75, var24, var12[var6 + 6], 23, 76029189);
         int var25 = this.hh(var24, var41, var59, var75, var12[var6 + 9], 4, -640364487);
         int var76 = this.hh(var75, var25, var41, var59, var12[var6 + 12], 11, -421815835);
         int var60 = this.hh(var59, var76, var25, var41, var12[var6 + 15], 16, 530742520);
         int var42 = this.hh(var41, var60, var76, var25, var12[var6 + 2], 23, -995338651);
         int var26 = this.ii(var25, var42, var60, var76, var12[var6], 6, -198630844);
         int var77 = this.ii(var76, var26, var42, var60, var12[var6 + 7], 10, 1126891415);
         int var61 = this.ii(var60, var77, var26, var42, var12[var6 + 14], 15, -1416354905);
         int var43 = this.ii(var42, var61, var77, var26, var12[var6 + 5], 21, -57434055);
         int var27 = this.ii(var26, var43, var61, var77, var12[var6 + 12], 6, 1700485571);
         int var78 = this.ii(var77, var27, var43, var61, var12[var6 + 3], 10, -1894986606);
         int var62 = this.ii(var61, var78, var27, var43, var12[var6 + 10], 15, -1051523);
         int var44 = this.ii(var43, var62, var78, var27, var12[var6 + 1], 21, -2054922799);
         int var28 = this.ii(var27, var44, var62, var78, var12[var6 + 8], 6, 1873313359);
         int var79 = this.ii(var78, var28, var44, var62, var12[var6 + 15], 10, -30611744);
         int var63 = this.ii(var62, var79, var28, var44, var12[var6 + 6], 15, -1560198380);
         int var45 = this.ii(var44, var63, var79, var28, var12[var6 + 13], 21, 1309151649);
         int var29 = this.ii(var28, var45, var63, var79, var12[var6 + 4], 6, -145523070);
         int var80 = this.ii(var79, var29, var45, var63, var12[var6 + 11], 10, -1120210379);
         int var64 = this.ii(var63, var80, var29, var45, var12[var6 + 2], 15, 718787259);
         int var46 = this.ii(var45, var64, var80, var29, var12[var6 + 9], 21, -343485551);
         var2 = safeAdd(var29, var2);
         var30 = safeAdd(var46, var30);
         var47 = safeAdd(var64, var47);
         var5 = safeAdd(var80, var5);
      }

      return this.wordToHex(var2) + this.wordToHex(var30) + this.wordToHex(var47) + this.wordToHex(var5);
   }
}
