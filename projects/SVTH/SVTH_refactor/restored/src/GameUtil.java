import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class GameUtil {
   private static Image scrollArrowImage;
   private static Image scrollThumbImage;
   private static Image panelImage;
   private static Random random;
   private static byte scrollLine = 0;
   private static int scrollTick = 0;
   private static byte scrollOffset = 0;
   private static boolean scrollMoving = false;
   private static int marqueeX;

   public GameUtil() {
   }

   public static void freeImageCaches() {
      ResourceCache.releaseGroup(1);
      ResourceCache.releaseGroup(0);
      System.gc();
   }

   public static String replaceAll(String var0, String var1, String var2) {
      int var3 = 0;

      int var4;
      while ((var4 = var0.indexOf(var1, var3)) != -1) {
         int var5 = var1.length();
         var0 = var0.substring(var3, var4) + var2 + var0.substring(var4 + var5, var0.length());
         var3 = var4 + var5 + 1;
      }

      return var0;
   }

   public static byte[] sortBytes(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         for (int var2 = 0; var2 < var0.length; var2++) {
            for (int var3 = 0; var3 < var0.length - 1 - var2; var3++) {
               if (var0[var3] > var0[var3 + 1]) {
                  byte var1 = var0[var3];
                  var0[var3] = var0[var3 + 1];
                  var0[var3 + 1] = var1;
               }
            }
         }

         return var0;
      }
   }

   public static boolean isInTeam(RoleSprite var0) {
      boolean var1 = false;

      for (int var2 = 0; var2 < GameData.party.length; var2++) {
         if (GameData.party[var2] == var0) {
            var1 = true;
         }
      }

      return var1;
   }

   public static short[][] parseShortRows(String[] var0, String var1) {
      short[][] var2 = null;
      if (var0 != null) {
         var2 = new short[var0.length][];

         for (int var3 = 0; var0 != null && var3 < var0.length; var3++) {
            if (var1 != null) {
               var2[var3] = Utils.parseShortArray(var0[var3], var1);
            } else {
               var1 = ",";
               var2[var3] = Utils.parseShortArray(var0[var3], var1);
            }
         }
      }

      return var2;
   }

   public static int randomRange(int var0, int var1) {
      if (random == null) {
         random = new Random();
      }

      int var2;
      return var2 = var0 + Math.abs(random.nextInt()) % var1;
   }

   public static int randomBetween(int var0, int var1) {
      if (var1 <= var0) {
         var0 = var0;
      } else {
         var0 = randomRange(var0, var1 - var0 + 1);
      }

      return var0;
   }

   public static void a(short[][] var0, int var1, int var2) {
      int var3;
      for (int var9 = (var3 = var0.length + 1) / 2; var9 > 0; var9 /= 2) {
         for (int var4 = var9; var4 < var3; var4++) {
            int var5 = var4 - var9;

            while (var5 >= var9) {
               short[] var10000 = var0[var5 - 1];
               short[] var10001 = var0[var5 + var9 - 1];
               boolean var8 = false;
               byte var7 = 1;
               short[] var6 = var10001;
               short[] var10 = var10000;
               byte var14;
               if (var10000[var7] < var6[var7]) {
                  byte var11;
                  if (!var8) {
                     var11 = 1;
                  } else {
                     var11 = -1;
                  }

                  var14 = var11;
               } else if (var10[var7] > var6[var7]) {
                  byte var12;
                  if (!var8) {
                     var12 = -1;
                  } else {
                     var12 = 1;
                  }

                  var14 = var12;
               } else {
                  var14 = 0;
               }

               if (var14 > 0) {
                  short[] var13 = var0[var5 - 1];
                  var0[var5 - 1] = var0[var5 + var9 - 1];
                  var0[var5 + var9 - 1] = var13;
                  var5 -= var9;
               } else {
                  var5 = 0;
               }
            }
         }
      }
   }

   public static int[] pickTwoDistinct(int var0, int var1, int var2) {
      int[] var4 = null;
      int[] var6 = null;
      if (var1 + 1 < 2) {
         return null;
      } else {
         for (int var3 = 0; var3 < var1 + 1; var3++) {
            var6 = Utils.append(var6, var3 + 0);
         }

         for (int var7 = 0; var7 < 2; var7++) {
            var1 = randomRange(0, var6.length);
            var4 = Utils.append(var4, var6[var1]);
            var6 = Utils.removeAt(var6, var1);
         }

         return var4;
      }
   }

   public static int pickRandomExcept(int[] var0, int var1) {
      if (Utils.contains(var0, var1)) {
         var0 = Utils.removeValue(var0, var1);
      }

      var1 = randomBetween(0, var0.length - 1);
      int var2;
      return var2 = var0[var1];
   }

   public static void drawScrollingText(Graphics var0, String[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int[] var8, boolean var9) {
      var0.setClip(var2, var3, var4, var6 * Utils.lineHeight);
      if (var6 < var1.length) {
         scrollTick++;
         if (!scrollMoving) {
            if (scrollTick >= 10) {
               scrollMoving = true;
            }
         } else {
            if (scrollTick % 3 == 0) {
               scrollOffset = (byte)(scrollOffset + 6);
            }

            if (scrollOffset >= Utils.lineHeight) {
               scrollMoving = false;
               scrollLine++;
               scrollOffset = 0;
               scrollTick = 0;
               if (scrollLine == var1.length) {
                  scrollLine = 0;
               }
            }
         }
      }

      for (int var10 = scrollLine; var10 < var1.length; var10++) {
         if (var8 != null) {
            var0.setColor(var8[var10]);
         } else {
            var0.setColor(var7);
         }

         if (var9) {
            vh.drawString(var0, var1[var10], var2 + var4 / 2, var3 + Utils.lineHeight * (var10 - scrollLine) - scrollOffset, 17);
         } else {
            vh.drawString(var0, var1[var10], var2, var3 + Utils.lineHeight * (var10 - scrollLine) - scrollOffset, 20);
         }
      }

      var0.setColor(16777215);
      var0.setClip(0, 0, DeviceConfig.screenSize[0], DeviceConfig.screenSize[1]);
   }

   public static void resetScroll() {
      scrollLine = 0;
      scrollTick = 0;
      scrollOffset = 0;
      scrollMoving = false;
   }

   private static int[][] a(short var0) {
      int[][] var1 = null;
      int[] var2 = null;
      int[] var3 = null;
      if (GameData.colorTableText == null) {
         GameData.colorTableText = Utils.loadConfigText("/bin/colorTable.bin");
      }

      int var4;
      if (var0 % 4 == 0) {
         var4 = var0 / 4;
      } else {
         var4 = var0 / 4 + 1;
      }

      String[][] var7 = Utils.getStringTable(GameData.colorTableText, "enemy" + var4 + ":", "enemy" + var4 + "End", null, ",");
      if ((var0 = var0 % 4) == 0) {
         var0 = 4;
      }

      if (var0 != 1 && var7 != null) {
         var2 = new int[var7[0].length];
         var3 = new int[var7[0].length];

         for (int var5 = 0; var5 < var7[0].length; var5++) {
            var2[var5] = Utils.parseInt(var7[0][var5]);
            var3[var5] = Utils.parseInt(var7[var0 - 1][var5]);
         }
      }

      if (var2 != null) {
         (var1 = new int[2][])[0] = var2;
         var1[1] = var3;
      }

      return var1;
   }

   public static AnimSprite loadBattleAnimation(int var0, boolean var1) {
      String var3;
      if (var0 % 4 == 0) {
         int var2 = var0 / 4;
         var3 = "/ft/" + var2 + ".av";
      } else {
         int var5 = var0 / 4 + 1;
         var3 = "/ft/" + var5 + ".av";
      }

      AnimSprite var6;
      (var6 = new AnimSprite()).isMirrored = var1;
      int[][] var4;
      if ((var4 = a((short)var0)) != null) {
         System.out.println("Đổi màu");
         if (!DeviceConfig.modelName.equals("N7610") && !DeviceConfig.modelName.equals("N73")) {
            var6.loadRecolored(var3, null, null, null, var4[0], var4[1], 0);
            if (!DeviceConfig.modelName.equals("N73")
               && !DeviceConfig.modelName.equals("N7610")
               && !DeviceConfig.modelName.equals("N5800")
               && !DeviceConfig.modelName.equals("E62")
               && !DeviceConfig.modelName.equals("W958C")) {
               ResourceCache.releaseGroup(0);
            }
         } else {
            var6.loadRecolored(var3, null, null, null, var4[0], var4[1], 1);
         }
      } else if (!DeviceConfig.modelName.equals("N7610") && !DeviceConfig.modelName.equals("N73")) {
         var6.loadInGroup(var3, 0);
      } else {
         var6.loadInGroup(var3, 1);
      }

      return var6;
   }

   public static String[][] append(String[][] var0, String[] var1) {
      if (var0 == null) {
         (var0 = new String[1][])[0] = var1;
         return var0;
      } else {
         String[][] var2 = new String[var0.length + 1][];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         var2[var2.length - 1] = var1;
         return var2;
      }
   }

   public static int maxValue(int[] var0) {
      int var1 = 0;
      if (var0 == null) {
         var1 = -1;
      } else {
         for (int var2 = 0; var2 < var0.length; var2++) {
            if (var1 <= var0[var2]) {
               var1 = var0[var2];
            }
         }
      }

      return var1;
   }

   public static void drawScrollBar(Graphics var0, Image var1, Image var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      scrollArrowImage = null;
      scrollThumbImage = null;
      if (scrollArrowImage == null) {
         scrollArrowImage = ResourceCache.loadImage("/pic/arrowhead.gf", 2);
      }

      if (scrollThumbImage == null) {
         scrollThumbImage = ResourceCache.loadImage("/pic/arrow1.gf", 2);
      }

      var0.setColor(3219973);
      var0.fillRect(var3, var4 + 4, scrollArrowImage.getWidth(), var5 - 8);
      var0.setColor(13410343);
      var0.fillRect(var3 + 1, var4 + 4, 1, var5 - 8);
      var0.fillRect(var3 + scrollArrowImage.getWidth() - 2, var4 + 4, 1, var5 - 8);
      if (scrollArrowImage != null) {
         var0.drawRegion(scrollArrowImage, 0, 0, scrollArrowImage.getWidth(), scrollArrowImage.getHeight() / 2, 0, var3, var4, 20);
         var0.drawRegion(
            scrollArrowImage, 0, scrollArrowImage.getHeight() / 2, scrollArrowImage.getWidth(), scrollArrowImage.getHeight() / 2, 0, var3, var4 + var5, 36
         );
      }

      if (var6 > var7) {
         int var9 = var5 - 12 - scrollThumbImage.getHeight();
         if (var8 < var6 - var7) {
            var0.drawImage(scrollThumbImage, var3, var4 + 6 + var8 * var9 / (var6 - var7), 20);
            return;
         }

         var0.drawImage(scrollThumbImage, var3, var4 + var5 - 6, 36);
      }
   }

   public static short[] getShortList(String var0, String var1, String var2, String var3, String var4) {
      short[] var10000;
      if ((var0 = Utils.getValue(Utils.substringBetween(var0, var1, var2), var4)) != null && !var0.equals("")) {
         String[] var6 = Utils.split(var0, var3);
         short[] var7 = null;
         if (var6 != null) {
            var7 = new short[var6.length];

            for (int var8 = 0; var8 < var6.length; var8++) {
               var7[var8] = Utils.parseShort(var6[var8]);
            }
         }

         var10000 = var7;
      } else {
         var10000 = null;
      }

      return var10000;
   }

   public static void drawMarquee(Graphics var0, String var1, int var2) {
      var0.setColor(0);
      vh.drawString(var0, var1, marqueeX, 2, 20);
      if ((marqueeX = marqueeX) < -vh.stringWidth(Utils.defaultFont, var1)) {
         marqueeX = DeviceConfig.screenSize[0];
      }
   }

   public static short[][][] append(short[][][] var0, short[][] var1) {
      if (var0 == null) {
         (var0 = new short[1][][])[0] = var1;
         return var0;
      } else {
         short[][][] var2 = new short[var0.length + 1][][];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         var2[var2.length - 1] = var1;
         return var2;
      }
   }

   public static int a_I() {
      int var0 = 0;

      for (int var1 = 0; GameData.myPets != null && var1 < GameData.myPets.length; var1++) {
         if (GameData.myPets[var1].ownerHeroId == GameData.heroId && GameData.myPets[var1].isDeployed) {
            var0++;
         }
      }

      return var0;
   }

   public static void a(int var0) {
      for (int var1 = 0; var1 < GameCanvas.instance.world.sceneLayer.actors.length; var1++) {
         if (GameCanvas.instance.world.sceneLayer.actors[var1] != null
            && GameCanvas.instance.world.sceneLayer.actors[var1].kind == 1
            && GameCanvas.instance.world.sceneLayer.actors[var1].isVisible) {
            System.out.println("==============" + GameCanvas.instance.world.sceneLayer.actors[var1].roleId);
         }
      }

      if (GameData.party[0].roleId != var0) {
         GameData.heroId = (short)var0;
         GameData.storeRoleRecords(GameData.party);
         GameData.party[0].animSprite = null;
         GameCanvas.instance.world.sceneLayer.removeActor(GameData.party[0]);
         GameCanvas.instance.world.sceneLayer.sortAllActors();
         GameCanvas.instance.world.sceneLayer.compactActors();
         short var5 = GameData.party[0].x;
         short var2 = GameData.party[0].y;
         byte var3 = GameData.party[0].direction;
         new RoleSprite();
         RoleSprite var4 = GameData.createRoleSprite(var0);
         GameData.party = null;
         GameData.partyRoleIds = null;
         GameData.addPartyMember(var4);
         if (GameData.heroId < 4) {
            GameData.refreshRoleStats(GameData.party[0]);
         }

         GameData.party[0].setPosition(var5, var2);
         GameData.party[0].setDirection(var3);
         GameCanvas.instance.world.sceneLayer.addActor(GameData.party[0]);
      }
   }

   public static void drawMessageBox(Graphics var0, String var1, int var2, int var3) {
      var0.translate(0, 0);
      if (var1 != null) {
         String[] var4 = Utils.wrapText(var1, var2 - 40);
         int var7 = vh.stringWidth(Utils.defaultFont, var1) + 20;
         int var5 = Utils.lineHeight + 15;
         var3 /= 3;
         int var6 = (var2 - var7) / 2;
         if (var4.length > 1) {
            int var8 = var2 - 30;
            var5 = var4.length * Utils.lineHeight + (var4.length + 1) * 5;
            a(var0, 16, var3, var8, var5);

            for (int var9 = 0; var9 < var4.length; var9++) {
               var0.setColor(16777215);
               vh.drawString(var0, var4[var9], var2 / 2, var3 + 8 + (Utils.lineHeight + 1) * var9, 17);
            }

            return;
         }

         a(var0, var6, var3, var7, var5);
         var0.setColor(16777215);
         vh.drawString(var0, var4[0], var2 / 2, var3 + 8, 17);
      }
   }

   private static void a(Graphics var0, int var1, int var2, int var3, int var4) {
      var0.setColor(1381651);
      var0.fillRoundRect(var1 - 1, var2 - 1, var3 + 3, var4 + 3, 8, 8);
      var0.setColor(16053217);
      var0.drawRoundRect(var1, var2, var3, var4, 8, 8);
      var0.setColor(6768205);
      var0.fillRect(var1 + 2, var2 + 2, var3 - 3, var4 - 3);
   }

   public static void clearPanelImages() {
      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         panelImage = null;
         scrollArrowImage = null;
         scrollThumbImage = null;
      }
   }

   public static void drawPanel(Graphics var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      if (GameSettings.goodOrBad == 1) {
         var0.setColor(15788720);
         var0.fillRect(var1, var2, var3, var4);
         var0.setColor(1317669);
         var0.fillRect(var1 + 1, var2 + 1, var3 - 2, var4 - 2);
      } else {
         if (panelImage == null) {
            panelImage = Image.createRGBImage(a(var3, var4, 1317669, 204), var3, var4, true);
         } else if (var4 != panelImage.getHeight() || var3 != panelImage.getWidth()) {
            clearPanelImages();
            panelImage = Image.createRGBImage(a(var3, var4, 1317669, 204), var3, var4, true);
         }

         var0.drawImage(panelImage, var1, var2, 20);
      }

      if (var8) {
         var0.setColor(3027509);
         var0.drawRoundRect(var1 - 1, var2 - 1, var3 + 1, var4 + 1, 4, 4);
         var0.setColor(12371653);
         var0.drawRoundRect(var1, var2, var3 - 1, var4 - 1, 4, 4);
      }
   }

   public static Image createTranslucentImage(int var0, int var1, int var2, int var3) {
      return Image.createRGBImage(a(var0 - 2, var1 - 2, var2, 216), var0 - 2, var1 - 2, true);
   }

   private static int[] a(int var0, int var1, int var2, int var3) {
      if (var3 >= 0 && var3 <= 255) {
         int[] var6 = new int[var0 * var1];
         var1 = a(var2, "R");
         int var4 = a(var2, "G");
         var2 = a(var2, "B");

         for (int var5 = 0; var5 < var6.length; var5++) {
            var6[var5] = (var3 << 24) + (var1 << 16) + (var4 << 8) + var2;
         }

         return var6;
      } else {
         return null;
      }
   }

   private static int a(int var0, String var1) {
      if (var1.equals("R") || var1.equals("r")) {
         int var3;
         var0 = (var3 = var0 & 0xFF0000) >> 16;
      } else if (var1.equals("G") || var1.equals("g")) {
         int var2;
         var0 = (var2 = var0 & 0xFF00) >> 8;
      } else if (var1.equals("B") || var1.equals("b")) {
         var0 &= 255;
      }

      return var0;
   }

   public static int[] a(String[][] var0, String[][] var1, int var2, int var3, int var4) {
      int var6 = Utils.parseInt(var0[var2][var3]) - 1;
      int[] var5 = Utils.parseIntArray(var1[var6][var3 - 3], "|");
      var6 = Utils.parseInt(var0[var2][var3 + 1]) - 1;
      int[] var7 = Utils.parseIntArray(var1[var6][var3 - 2], "|");
      int[] var8;
      if (var4 == 1) {
         var8 = Utils.append(Utils.append(null, GameData.rollBiasedHalf(var7[0], var7[1], true)), randomBetween(var5[0], var5[1]));
      } else {
         int var9 = randomRange(var7[0], var7[1] - (var7[1] + var7[0]) / 2 + 1);
         var8 = Utils.append(Utils.append(null, var9), randomBetween(var5[0], var5[1]) + var9 * (var4 - 1));
      }

      return var8;
   }

   public static void addDungeonPortal(short var0, short var1, short var2) {
      ScriptEvent var3;
      (var3 = new ScriptEvent()).name = "Dịch chuyển phó bản";
      var3.area = new short[]{(short)(var0 - 12), (short)(var1 - 12), 24, 24};
      var3.isAuto = true;
      var3.isLoop = true;
      var3.scriptLines = new String[][]{{"换场景", String.valueOf((int)var2), "1"}};
      if (GameCanvas.instance.world.script.events == null) {
         GameCanvas.instance.world.script.events = new ScriptEvent[1];
      } else {
         ScriptEvent[] var4 = new ScriptEvent[GameCanvas.instance.world.script.events.length + 1];
         System.arraycopy(GameCanvas.instance.world.script.events, 0, var4, 0, GameCanvas.instance.world.script.events.length);
         GameCanvas.instance.world.script.events = var4;
      }

      GameCanvas.instance.world.script.events[GameCanvas.instance.world.script.events.length - 1] = var3;
      if (GameCanvas.instance.world.portalAnim == null) {
         GameCanvas.instance.world.portalAnim = new AnimSprite();
         GameCanvas.instance.world.portalAnim.load("/map/pics/fazheng.av");
         GameCanvas.instance.world.portalX = var0;
         GameCanvas.instance.world.portalY = var1;
      }
   }

   public static void drawAnimation(Graphics var0, AnimSprite var1, int var2, int var3, boolean var4, int var5) {
      if (var1 != null) {
         var1.setAction(var5);
         var1.x = (short)var2;
         var1.y = (short)var3;
         var1.draw(var0);
         if (GameCanvas.instance.frameCounter % 2 == 0) {
            var1.nextFrame(true);
         }
      }
   }

   public static void a(Graphics var0, int var1, int var2, int var3, Image var4, int var5, int var6, int var7, int var8) {
      if (var4 == null) {
         if (var8 == 1) {
            Utils.drawOutlinedText(var0, "" + var1, var2, var3, 2380419, 9306107, 33);
         } else {
            Utils.drawOutlinedText(var0, "" + var1, var2, var3, 16681646, 8791076, 33);
         }
      } else if (var1 <= 0) {
         Utils.drawNumber(var0, var1, var2, var3, var4, 13, 13, 33);
      } else {
         var5 = Utils.digitCount(var1);
         Utils.drawNumber(var0, var1, var2, var3, var4, 13, 13, 33);
         var0.drawRegion(var4, 143, 0, 13, 13, 0, var2 - var5 * 13 / 2 - 6, var3, 33);
      }
   }

   public static void drawBoldText(Graphics var0, String var1, int var2, int var3, int var4, int var5) {
      var0.setColor(2093589);
      vh.drawString(var0, var1, var2, var3, 17);
      vh.drawString(var0, var1, var2 + 1, var3, 17);
   }

   public static PetInfo[] a_APetInfo() {
      PetInfo[] var0 = null;
      Vector var1 = new Vector();

      for (int var2 = 0; GameData.myPets != null && var2 < GameData.myPets.length; var2++) {
         if (GameData.myPets[var2].ownerHeroId == GameData.heroId) {
            var1.addElement(GameData.myPets[var2]);
         }
      }

      if (var1.size() > 0) {
         var0 = new PetInfo[var1.size()];
         var1.copyInto(var0);
      }

      return var0;
   }
}
