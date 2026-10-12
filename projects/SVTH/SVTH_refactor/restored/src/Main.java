import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet {
   public static Display disp;
   GameCanvas a_GameCanvas;
   public static Main self;
   public static MusicPlayer music;
   public static byte noAttackFrame = 30;
   public boolean playMusic;
   public static Image[] firstload;

   public Main() {
      self = this;
      disp = Display.getDisplay(this);
      PackFile.loadIndex("/00");
      this.a_GameCanvas = new GameCanvas(disp);
      DeviceConfig.load();
      KeyMap.loadKeyConfig();
      String var1;
      if ((var1 = self.getAppProperty("nus")) != null) {
         var1.equals("0");
      }

      if ((var1 = self.getAppProperty("ump")) != null && var1.equals("1")) {
         DeviceConfig.useMoonicNetPay = true;
      }

      GameSettings.load();
      var1 = "/jzy.bin";
      ChannelConfig.addMenuText = Utils.getValue(var1 = Utils.loadConfigText("/jzy.bin"), "addMenu");
      ChannelConfig.addMenuLink = Utils.getValue(var1, "addMenuLink");
      ChannelConfig.logoFiles = Utils.getStringArray(var1, "logo");
      ChannelConfig.logoBgColors = Utils.getIntArray(var1, "logoBgColor");
      ChannelConfig.logoTimes = Utils.getShortArray(var1, "logoTime");
      ChannelConfig.aboutText = Utils.substringBetween(var1, "aboutus:", "aboutusEnd");
      Utils.getStringArray(var1, "smsCommand");
      Utils.getValue(var1, "smsPhoneNum");
      ChannelConfig.smsPrice = Utils.getValue(var1, "smsPrice");
      Utils.getByte(var1, "smsCommandUseType");
      ChannelConfig.smsMessage = Utils.getValue(var1, "smsMsg");
      Utils.getByte(var1, "mode");
      Utils.getValue(var1, "smsPrefix");
      ChannelConfig.channelId = Utils.getValue(var1, "channelId");
      ChannelConfig.gameId = Utils.getValue(var1, "gameId");
      Utils.getByte(var1, "needCount");
      Utils.getByte(var1, "maxPayTotal");
      ChannelConfig.isKemu = Utils.getBool(var1, "isKemu");
      ChannelConfig.isFree = Utils.getBool(var1, "isFree");
      Utils.getByte(var1, "channelPrice");
      ChannelConfig.isBbx = Utils.getBool(var1, "isBbx");
      ChannelConfig.cpId = Utils.getValue(var1, "cpId");
      ChannelConfig.useFreeCode = Utils.parseBool(self.getAppProperty("useFreeCode"));
      var1 = "/subChannels.txt";
      Utils.loadText("/subChannels.txt", false);
      var1 = this.getAppProperty("cid");
      this.getAppProperty("sid");
      if (var1 != null && var1.length() == 2) {
         ChannelConfig.channelId = var1;
      }

      GameData.loadStaticTables();
      GameData.loadPurchaseState(GameData.purchaseStore);
      if (DeviceConfig.screenSize != null) {
         if (DeviceConfig.screenSize[0] > 0) {
            this.a_GameCanvas.screenWidth = DeviceConfig.screenSize[0];
         }

         if (DeviceConfig.screenSize[1] > 0) {
            this.a_GameCanvas.screenHeight = DeviceConfig.screenSize[1];
         }
      }

      this.showLogo();
      this.a_GameCanvas.startLoop();
      disp.setCurrent(this.a_GameCanvas);
   }

   public void showSoundSet() {
      GameCanvas.screenState = 1;
   }

   public void showLogo() {
      this.a_GameCanvas.logoScreen = new LogoScreen();
      this.a_GameCanvas.logoScreen.mode = 2;
      GameCanvas.screenState = 2;
   }

   public void showCover() {
      this.a_GameCanvas.world = null;
      this.a_GameCanvas.logoScreen = null;
      if (DeviceConfig.modelName != null && (DeviceConfig.modelName.equals("L6") || DeviceConfig.modelName.equals("L7"))) {
         try {
            Thread.sleep(200L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      this.a_GameCanvas.titleScreen = new TitleScreen();
      this.a_GameCanvas.titleScreen.startLoading();
      GameCanvas.screenState = 3;
   }

   public void newGame() {
      if (music != null) {
         music.stop();
         music.release();
         music = null;
      }

      if (!DeviceConfig.modelName.equals("N73")
         && !DeviceConfig.modelName.equals("N7610")
         && !DeviceConfig.modelName.equals("N5800")
         && !DeviceConfig.modelName.equals("E62")
         && !DeviceConfig.modelName.equals("W958C")) {
         ResourceCache.releaseGroup(1);
         ResourceCache.releaseGroup(0);
         ResourceCache.releaseGroup(-1);
      }

      for (int var1 = 0; GameSettings.firstLoadFiles != null && var1 < GameSettings.firstLoadFiles.length; var1++) {
         (firstload = new Image[GameSettings.firstLoadFiles.length])[var1] = ResourceCache.loadImage(GameSettings.firstLoadFiles[var1], 1);
      }

      this.a_GameCanvas.world = new World();
      this.a_GameCanvas.world.startGame();
      this.a_GameCanvas.titleScreen = null;
      GameCanvas.screenState = 4;
   }

   public void continueGame() {
      if (music != null) {
         music.stop();
         music.release();
         music = null;
      }

      this.a_GameCanvas.titleScreen = null;
      this.a_GameCanvas.world = new World();
      this.a_GameCanvas.world.startGame();
      GameCanvas.screenState = 4;
   }

   protected void startApp() {
      if (this.a_GameCanvas != null) {
         this.a_GameCanvas.paused = false;
         disp.setCurrent(this.a_GameCanvas);
      }
   }

   protected void pauseApp() {
      if (this.a_GameCanvas != null) {
         this.a_GameCanvas.paused = true;
         this.stopMusic();
         if (this.a_GameCanvas.world != null) {
            World.stopMusic();
         }

         GameCanvas.screenState = 7;
      }
   }

   public void playMusic(String var1, int var2, int var3) {
      if (DeviceConfig.playMusic && var1 != null && !var1.equals("")) {
         if (music == null) {
            music = new MusicPlayer();
         } else {
            music.stop();
            music.release();
         }

         music.load(var1);
         music.setLoopCount(var2);
         music.setVolume(var3);
         music.start();
      }
   }

   public void stopMusic() {
      if (music != null) {
         music.stop();
         music.release();
      }
   }

   protected void destroyApp(boolean var1) {
      if (this.a_GameCanvas != null) {
         this.a_GameCanvas.stopLoop();

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }

      if (Equip.icons != null) {
         for (int var3 = 0; var3 < Equip.icons.length; var3++) {
            Equip.icons[var3] = null;
         }
      }

      Equip.icons = null;
      if (GameData.itemIcons != null) {
         for (int var4 = 0; var4 < GameData.itemIcons.length; var4++) {
            GameData.itemIcons[var4] = null;
         }

         GameData.itemIcons = null;
      }

      if (Skill.icons != null) {
         for (int var5 = 0; var5 < Skill.icons.length; var5++) {
            Skill.icons[var5] = null;
         }

         Skill.icons = null;
      }

      ResourceCache.clearAll();
      this.notifyDestroyed();
   }

   public void stopThread() {
      if (this.a_GameCanvas != null) {
         this.a_GameCanvas.stopLoop();

         try {
            Thread.sleep(2000L);
            return;
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }
      }
   }
}
