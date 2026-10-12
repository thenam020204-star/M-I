public final class br {
  private String a_String = "0123456789abcdef";

  private String a(int var1) {
    String var2 = "";

    for (int var3 = 0; var3 <= 3; var3++) {
      var2 = var2 + this.a_String.charAt(var1 >> (var3 << 3) + 4 & 15) + this.a_String.charAt(var1 >> (var3 << 3) & 15);
    }

    return var2;
  }

  private static int a(int var0, int var1) {
    return (var0 & 2147483647) + (var1 & 2147483647) ^ var0 & -2147483648 ^ var1 & -2147483648;
  }

  private int a(int var1, int var2, int var3, int var4, int var5, int var6) {
    int var7;
    return a((var7 = a(a(var2, var1), a(var4, var6))) << var5 | var7 >>> 32 - var5, var3);
  }

  private int a(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
    return this.a(var2 & var3 | ~var2 & var4, var1, var2, var5, var6, var7);
  }

  private int b(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
    return this.a(var2 & var4 | var3 & ~var4, var1, var2, var5, var6, var7);
  }

  private int c(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
    return this.a(var2 ^ var3 ^ var4, var1, var2, var5, var6, var7);
  }

  private int d(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
    return this.a(var3 ^ (var2 | ~var4), var1, var2, var5, var6, var7);
  }

  public final String a(String var1) {
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
      int var14 = this.a(var2, var30, var47, var5, var12[var6], 7, -680876936);
      int var65 = this.a(var5, var14, var30, var47, var12[var6 + 1], 12, -389564586);
      int var49 = this.a(var47, var65, var14, var30, var12[var6 + 2], 17, 606105819);
      int var31 = this.a(var30, var49, var65, var14, var12[var6 + 3], 22, -1044525330);
      int var15 = this.a(var14, var31, var49, var65, var12[var6 + 4], 7, -176418897);
      int var66 = this.a(var65, var15, var31, var49, var12[var6 + 5], 12, 1200080426);
      int var50 = this.a(var49, var66, var15, var31, var12[var6 + 6], 17, -1473231341);
      int var32 = this.a(var31, var50, var66, var15, var12[var6 + 7], 22, -45705983);
      int var16 = this.a(var15, var32, var50, var66, var12[var6 + 8], 7, 1770035416);
      int var67 = this.a(var66, var16, var32, var50, var12[var6 + 9], 12, -1958414417);
      int var51 = this.a(var50, var67, var16, var32, var12[var6 + 10], 17, -42063);
      int var33 = this.a(var32, var51, var67, var16, var12[var6 + 11], 22, -1990404162);
      int var17 = this.a(var16, var33, var51, var67, var12[var6 + 12], 7, 1804603682);
      int var68 = this.a(var67, var17, var33, var51, var12[var6 + 13], 12, -40341101);
      int var52 = this.a(var51, var68, var17, var33, var12[var6 + 14], 17, -1502002290);
      int var34 = this.a(var33, var52, var68, var17, var12[var6 + 15], 22, 1236535329);
      int var18 = this.b(var17, var34, var52, var68, var12[var6 + 1], 5, -165796510);
      int var69 = this.b(var68, var18, var34, var52, var12[var6 + 6], 9, -1069501632);
      int var53 = this.b(var52, var69, var18, var34, var12[var6 + 11], 14, 643717713);
      int var35 = this.b(var34, var53, var69, var18, var12[var6], 20, -373897302);
      int var19 = this.b(var18, var35, var53, var69, var12[var6 + 5], 5, -701558691);
      int var70 = this.b(var69, var19, var35, var53, var12[var6 + 10], 9, 38016083);
      int var54 = this.b(var53, var70, var19, var35, var12[var6 + 15], 14, -660478335);
      int var36 = this.b(var35, var54, var70, var19, var12[var6 + 4], 20, -405537848);
      int var20 = this.b(var19, var36, var54, var70, var12[var6 + 9], 5, 568446438);
      int var71 = this.b(var70, var20, var36, var54, var12[var6 + 14], 9, -1019803690);
      int var55 = this.b(var54, var71, var20, var36, var12[var6 + 3], 14, -187363961);
      int var37 = this.b(var36, var55, var71, var20, var12[var6 + 8], 20, 1163531501);
      int var21 = this.b(var20, var37, var55, var71, var12[var6 + 13], 5, -1444681467);
      int var72 = this.b(var71, var21, var37, var55, var12[var6 + 2], 9, -51403784);
      int var56 = this.b(var55, var72, var21, var37, var12[var6 + 7], 14, 1735328473);
      int var38 = this.b(var37, var56, var72, var21, var12[var6 + 12], 20, -1926607734);
      int var22 = this.c(var21, var38, var56, var72, var12[var6 + 5], 4, -378558);
      int var73 = this.c(var72, var22, var38, var56, var12[var6 + 8], 11, -2022574463);
      int var57 = this.c(var56, var73, var22, var38, var12[var6 + 11], 16, 1839030562);
      int var39 = this.c(var38, var57, var73, var22, var12[var6 + 14], 23, -35309556);
      int var23 = this.c(var22, var39, var57, var73, var12[var6 + 1], 4, -1530992060);
      int var74 = this.c(var73, var23, var39, var57, var12[var6 + 4], 11, 1272893353);
      int var58 = this.c(var57, var74, var23, var39, var12[var6 + 7], 16, -155497632);
      int var40 = this.c(var39, var58, var74, var23, var12[var6 + 10], 23, -1094730640);
      int var24 = this.c(var23, var40, var58, var74, var12[var6 + 13], 4, 681279174);
      int var75 = this.c(var74, var24, var40, var58, var12[var6], 11, -358537222);
      int var59 = this.c(var58, var75, var24, var40, var12[var6 + 3], 16, -722521979);
      int var41 = this.c(var40, var59, var75, var24, var12[var6 + 6], 23, 76029189);
      int var25 = this.c(var24, var41, var59, var75, var12[var6 + 9], 4, -640364487);
      int var76 = this.c(var75, var25, var41, var59, var12[var6 + 12], 11, -421815835);
      int var60 = this.c(var59, var76, var25, var41, var12[var6 + 15], 16, 530742520);
      int var42 = this.c(var41, var60, var76, var25, var12[var6 + 2], 23, -995338651);
      int var26 = this.d(var25, var42, var60, var76, var12[var6], 6, -198630844);
      int var77 = this.d(var76, var26, var42, var60, var12[var6 + 7], 10, 1126891415);
      int var61 = this.d(var60, var77, var26, var42, var12[var6 + 14], 15, -1416354905);
      int var43 = this.d(var42, var61, var77, var26, var12[var6 + 5], 21, -57434055);
      int var27 = this.d(var26, var43, var61, var77, var12[var6 + 12], 6, 1700485571);
      int var78 = this.d(var77, var27, var43, var61, var12[var6 + 3], 10, -1894986606);
      int var62 = this.d(var61, var78, var27, var43, var12[var6 + 10], 15, -1051523);
      int var44 = this.d(var43, var62, var78, var27, var12[var6 + 1], 21, -2054922799);
      int var28 = this.d(var27, var44, var62, var78, var12[var6 + 8], 6, 1873313359);
      int var79 = this.d(var78, var28, var44, var62, var12[var6 + 15], 10, -30611744);
      int var63 = this.d(var62, var79, var28, var44, var12[var6 + 6], 15, -1560198380);
      int var45 = this.d(var44, var63, var79, var28, var12[var6 + 13], 21, 1309151649);
      int var29 = this.d(var28, var45, var63, var79, var12[var6 + 4], 6, -145523070);
      int var80 = this.d(var79, var29, var45, var63, var12[var6 + 11], 10, -1120210379);
      int var64 = this.d(var63, var80, var29, var45, var12[var6 + 2], 15, 718787259);
      int var46 = this.d(var45, var64, var80, var29, var12[var6 + 9], 21, -343485551);
      var2 = a(var29, var2);
      var30 = a(var46, var30);
      var47 = a(var64, var47);
      var5 = a(var80, var5);
    }

    return this.a(var2) + this.a(var30) + this.a(var47) + this.a(var5);
  }
}
