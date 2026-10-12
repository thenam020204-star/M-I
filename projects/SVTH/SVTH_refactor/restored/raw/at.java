public final class at {
  public static byte a_B;
  public static byte b_B;
  public static byte c_B;
  public static boolean a_Z;
  public static boolean b_Z;
  public static byte d_B = 80;
  public static boolean c_Z;
  public static boolean d_Z;
  public static boolean e_Z;
  public static String a_String;
  public static short[] a_AS;
  public static byte e_B;
  public static boolean f_Z;
  public static boolean g_Z;
  public static boolean h_Z = false;
  public static byte f_B = 8;
  public static String[] a_AString;
  public static boolean i_Z;
  public static byte g_B;
  public static boolean j_Z;
  public static byte h_B;
  public static boolean k_Z;
  public static byte i_B;
  public static boolean l_Z;
  public static byte j_B;
  public static boolean m_Z;
  public static boolean n_Z;
  public static boolean o_Z = true;
  public static boolean p_Z = true;
  public static boolean q_Z = false;
  public static boolean r_Z = true;
  public static boolean s_Z;

  public static void a() {
    String var0;
    a_B = cg.a_B(var0 = cg.a_String("/bin/config.txt", false), "cutMapImg");
    b_B = cg.a_B(var0, "cutAvImg");
    c_B = cg.a_B(var0, "cutImg");
    a_Z = cg.b_Z(var0, "allowMusic");
    b_Z = cg.b_Z(var0, "playMusic");
    c_Z = cg.b_Z(var0, "notUseMusicClose");
    d_Z = cg.b_Z(var0, "alowDebug");
    e_Z = cg.b_Z(var0, "debug");
    a_String = cg.a_String(var0, "model");
    cg.b_Z(var0, "allowUseFrameSkip");
    o_Z = cg.b_Z(var0, "useGlassMirror");
    cg.b_Z(var0, "useWaterMirror");
    p_Z = cg.b_Z(var0, "useAlphaMirror");
    q_Z = cg.b_Z(var0, "useMoonicNetPay");
    String var1;
    if ((var1 = cg.a_String(var0, "useCarmarkCache")) != null && !var1.equals("")) {
      r_Z = cg.b_Z(var1);
    }

    if ((var1 = cg.a_String(var0, "fillMapBackColor")) != null && !var1.equals("")) {
      s_Z = cg.b_Z(var1);
    }

    if (a_String == null) {
      a_String = " ";
    }

    cg.a_String(var0, "modelNo");
    a_AS = cg.a_AS(var0, "screenSize");
    e_B = cg.a_B(var0, "rmsCount");
    cg.a_AString(var0, "rmsName");
    if (e_B == 0) {
      e_B = 3;
    }

    a_AString = cg.a_AString(var0, "crack");
    if (a_Z) {
      b_Z = true;
    } else {
      b_Z = false;
    }

    i_Z = cg.b_Z(var0, "cacheTxt");
    g_B = cg.a_B(var0, "cacheTxtGroup");
    j_Z = cg.b_Z(var0, "cacheMap");
    h_B = cg.a_B(var0, "cacheMapGroup");
    k_Z = cg.b_Z(var0, "cacheAv");
    i_B = cg.a_B(var0, "cacheAvGroup");
    l_Z = cg.b_Z(var0, "cacheMusic");
    j_B = cg.a_B(var0, "cacheMusicGroup");
    m_Z = cg.b_Z(var0, "needCopyRight");
    n_Z = cg.b_Z(var0, "useImgColorTable");
    cg.b_Z(var0, "useStart");
    cg.b_Z(var0, "useEnemyFlash");
    f_Z = cg.b_Z(var0, "unloadBeforeBattle");
    g_Z = cg.b_Z(var0, "moreSleep");
    cg.a_B(var0, "pansShowType");
    cg.a_AString(var0, "logo");
    cg.a_AS(var0, "logoTime");
    cg.a_AI(var0, "logoBgColor");
  }
}
