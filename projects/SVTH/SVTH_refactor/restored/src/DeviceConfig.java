public final class DeviceConfig {
   public static byte cutMapImg;
   public static byte cutAvImg;
   public static byte cutImg;
   public static boolean allowMusic;
   public static boolean playMusic;
   public static byte defaultVolume = 80;
   public static boolean notUseMusicClose;
   public static boolean allowDebug;
   public static boolean debugMode;
   public static String modelName;
   public static short[] screenSize;
   public static byte rmsCount;
   public static boolean unloadBeforeBattle;
   public static boolean moreSleep;
   public static boolean h_Z = false;
   public static byte f_B = 8;
   public static String[] crackClassNames;
   public static boolean cacheTxt;
   public static byte cacheTxtGroup;
   public static boolean cacheMap;
   public static byte cacheMapGroup;
   public static boolean cacheAv;
   public static byte cacheAvGroup;
   public static boolean cacheMusic;
   public static byte cacheMusicGroup;
   public static boolean needCopyRight;
   public static boolean useImgColorTable;
   public static boolean useGlassMirror = true;
   public static boolean useAlphaMirror = true;
   public static boolean useMoonicNetPay = false;
   public static boolean useCarmarkCache = true;
   public static boolean fillMapBackColor;

   public DeviceConfig() {
   }

   public static void load() {
      String var0;
      cutMapImg = Utils.getByte(var0 = Utils.loadText("/bin/config.txt", false), "cutMapImg");
      cutAvImg = Utils.getByte(var0, "cutAvImg");
      cutImg = Utils.getByte(var0, "cutImg");
      allowMusic = Utils.getBool(var0, "allowMusic");
      playMusic = Utils.getBool(var0, "playMusic");
      notUseMusicClose = Utils.getBool(var0, "notUseMusicClose");
      allowDebug = Utils.getBool(var0, "alowDebug");
      debugMode = Utils.getBool(var0, "debug");
      modelName = Utils.getValue(var0, "model");
      Utils.getBool(var0, "allowUseFrameSkip");
      useGlassMirror = Utils.getBool(var0, "useGlassMirror");
      Utils.getBool(var0, "useWaterMirror");
      useAlphaMirror = Utils.getBool(var0, "useAlphaMirror");
      useMoonicNetPay = Utils.getBool(var0, "useMoonicNetPay");
      String var1;
      if ((var1 = Utils.getValue(var0, "useCarmarkCache")) != null && !var1.equals("")) {
         useCarmarkCache = Utils.parseBool(var1);
      }

      if ((var1 = Utils.getValue(var0, "fillMapBackColor")) != null && !var1.equals("")) {
         fillMapBackColor = Utils.parseBool(var1);
      }

      if (modelName == null) {
         modelName = " ";
      }

      Utils.getValue(var0, "modelNo");
      screenSize = Utils.getShortArray(var0, "screenSize");
      rmsCount = Utils.getByte(var0, "rmsCount");
      Utils.getStringArray(var0, "rmsName");
      if (rmsCount == 0) {
         rmsCount = 3;
      }

      crackClassNames = Utils.getStringArray(var0, "crack");
      if (allowMusic) {
         playMusic = true;
      } else {
         playMusic = false;
      }

      cacheTxt = Utils.getBool(var0, "cacheTxt");
      cacheTxtGroup = Utils.getByte(var0, "cacheTxtGroup");
      cacheMap = Utils.getBool(var0, "cacheMap");
      cacheMapGroup = Utils.getByte(var0, "cacheMapGroup");
      cacheAv = Utils.getBool(var0, "cacheAv");
      cacheAvGroup = Utils.getByte(var0, "cacheAvGroup");
      cacheMusic = Utils.getBool(var0, "cacheMusic");
      cacheMusicGroup = Utils.getByte(var0, "cacheMusicGroup");
      needCopyRight = Utils.getBool(var0, "needCopyRight");
      useImgColorTable = Utils.getBool(var0, "useImgColorTable");
      Utils.getBool(var0, "useStart");
      Utils.getBool(var0, "useEnemyFlash");
      unloadBeforeBattle = Utils.getBool(var0, "unloadBeforeBattle");
      moreSleep = Utils.getBool(var0, "moreSleep");
      Utils.getByte(var0, "pansShowType");
      Utils.getStringArray(var0, "logo");
      Utils.getShortArray(var0, "logoTime");
      Utils.getIntArray(var0, "logoBgColor");
   }
}
