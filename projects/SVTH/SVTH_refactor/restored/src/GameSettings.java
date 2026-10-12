public final class GameSettings {
   public static String[] firstLoadFiles;
   public static byte skipNeedle;
   public static String titleMusic;
   public static byte goodOrBad;
   public static short maxItemTypes = 36;
   public static byte maxPetCount = 5;

   public GameSettings() {
   }

   public static void load() {
      String var0;
      firstLoadFiles = Utils.getStringArray(var0 = Utils.loadText("/bin/myConfig.txt", false), "Firstload");
      skipNeedle = Utils.getByte(var0, "skipNeedle");
      titleMusic = Utils.getValue(var0, "TitlesMusic");
      goodOrBad = Utils.getByte(var0, "goodorbad");
      Utils.getBool(var0, "useImgColorTable");
      maxItemTypes = Utils.getShort(var0, "goodsMaxNum");
      maxPetCount = Utils.getByte(var0, "maxPetNumber");
   }
}
