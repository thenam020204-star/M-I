public final class ct {
  public static String[] a_AString;
  public static byte a_B;
  public static String a_String;
  public static byte b_B;
  public static short a_S = 36;
  public static byte c_B = 5;

  public static void a() {
    String var0;
    a_AString = cg.a_AString(var0 = cg.a_String("/bin/myConfig.txt", false), "Firstload");
    a_B = cg.a_B(var0, "skipNeedle");
    a_String = cg.a_String(var0, "TitlesMusic");
    b_B = cg.a_B(var0, "goodorbad");
    cg.b_Z(var0, "useImgColorTable");
    a_S = cg.a_S(var0, "goodsMaxNum");
    c_B = cg.a_B(var0, "maxPetNumber");
  }
}
