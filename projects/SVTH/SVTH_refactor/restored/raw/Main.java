import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet {
  public static Display disp;
  da a_da;
  public static Main self;
  public static bv music;
  public static byte noAttackFrame = 30;
  public boolean playMusic;
  public static Image[] firstload;

  public Main() {
    self = this;
    disp = Display.getDisplay(this);
    cu.a_Z("/00");
    this.a_da = new da(disp);
    at.a();
    b.a();
    String var1;
    if ((var1 = self.getAppProperty("nus")) != null) {
      var1.equals("0");
    }

    if ((var1 = self.getAppProperty("ump")) != null && var1.equals("1")) {
      at.q_Z = true;
    }

    ct.a();
    var1 = "/jzy.bin";
    cq.f_String = cg.a_String(var1 = cg.a_String("/jzy.bin"), "addMenu");
    cq.g_String = cg.a_String(var1, "addMenuLink");
    cq.a_AString = cg.a_AString(var1, "logo");
    cq.a_AI = cg.a_AI(var1, "logoBgColor");
    cq.a_AS = cg.a_AS(var1, "logoTime");
    cq.a_String = cg.a(var1, "aboutus:", "aboutusEnd");
    cg.a_AString(var1, "smsCommand");
    cg.a_String(var1, "smsPhoneNum");
    cq.b_String = cg.a_String(var1, "smsPrice");
    cg.a_B(var1, "smsCommandUseType");
    cq.c_String = cg.a_String(var1, "smsMsg");
    cg.a_B(var1, "mode");
    cg.a_String(var1, "smsPrefix");
    cq.d_String = cg.a_String(var1, "channelId");
    cq.e_String = cg.a_String(var1, "gameId");
    cg.a_B(var1, "needCount");
    cg.a_B(var1, "maxPayTotal");
    cq.a_Z = cg.b_Z(var1, "isKemu");
    cq.b_Z = cg.b_Z(var1, "isFree");
    cg.a_B(var1, "channelPrice");
    cq.c_Z = cg.b_Z(var1, "isBbx");
    cq.h_String = cg.a_String(var1, "cpId");
    cq.d_Z = cg.b_Z(self.getAppProperty("useFreeCode"));
    var1 = "/subChannels.txt";
    cg.a_String("/subChannels.txt", false);
    var1 = this.getAppProperty("cid");
    this.getAppProperty("sid");
    if (var1 != null && var1.length() == 2) {
      cq.d_String = var1;
    }

    o.b();
    o.a_Z(o.r_String);
    if (at.a_AS != null) {
      if (at.a_AS[0] > 0) {
        this.a_da.b_S = at.a_AS[0];
      }

      if (at.a_AS[1] > 0) {
        this.a_da.c_S = at.a_AS[1];
      }
    }

    this.showLogo();
    this.a_da.a();
    disp.setCurrent(this.a_da);
  }

  public void showSoundSet() {
    da.a_B = 1;
  }

  public void showLogo() {
    this.a_da.a_av = new av();
    this.a_da.a_av.a_B = 2;
    da.a_B = 2;
  }

  public void showCover() {
    this.a_da.a_cd = null;
    this.a_da.a_av = null;
    if (at.a_String != null && (at.a_String.equals("L6") || at.a_String.equals("L7"))) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }
    }

    this.a_da.a_bs = new bs();
    this.a_da.a_bs.a();
    da.a_B = 3;
  }

  public void newGame() {
    if (music != null) {
      music.b();
      music.c();
      music = null;
    }

    if (!at.a_String.equals("N73")
      && !at.a_String.equals("N7610")
      && !at.a_String.equals("N5800")
      && !at.a_String.equals("E62")
      && !at.a_String.equals("W958C")) {
      cc.a(1);
      cc.a(0);
      cc.a(-1);
    }

    for (int var1 = 0; ct.a_AString != null && var1 < ct.a_AString.length; var1++) {
      (firstload = new Image[ct.a_AString.length])[var1] = cc.a_Image(ct.a_AString[var1], 1);
    }

    this.a_da.a_cd = new cd();
    this.a_da.a_cd.a_V();
    this.a_da.a_bs = null;
    da.a_B = 4;
  }

  public void continueGame() {
    if (music != null) {
      music.b();
      music.c();
      music = null;
    }

    this.a_da.a_bs = null;
    this.a_da.a_cd = new cd();
    this.a_da.a_cd.a_V();
    da.a_B = 4;
  }

  protected void startApp() {
    if (this.a_da != null) {
      this.a_da.a_Z = false;
      disp.setCurrent(this.a_da);
    }
  }

  protected void pauseApp() {
    if (this.a_da != null) {
      this.a_da.a_Z = true;
      this.stopMusic();
      if (this.a_da.a_cd != null) {
        cd.j();
      }

      da.a_B = 7;
    }
  }

  public void playMusic(String var1, int var2, int var3) {
    if (at.b_Z && var1 != null && !var1.equals("")) {
      if (music == null) {
        music = new bv();
      } else {
        music.b();
        music.c();
      }

      music.a(var1);
      music.a(var2);
      music.b(var3);
      music.a();
    }
  }

  public void stopMusic() {
    if (music != null) {
      music.b();
      music.c();
    }
  }

  protected void destroyApp(boolean var1) {
    if (this.a_da != null) {
      this.a_da.b();

      try {
        Thread.sleep(200L);
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }
    }

    if (ck.a_AImage != null) {
      for (int var3 = 0; var3 < ck.a_AImage.length; var3++) {
        ck.a_AImage[var3] = null;
      }
    }

    ck.a_AImage = null;
    if (o.a_AImage != null) {
      for (int var4 = 0; var4 < o.a_AImage.length; var4++) {
        o.a_AImage[var4] = null;
      }

      o.a_AImage = null;
    }

    if (d.a_AImage != null) {
      for (int var5 = 0; var5 < d.a_AImage.length; var5++) {
        d.a_AImage[var5] = null;
      }

      d.a_AImage = null;
    }

    cc.a();
    this.notifyDestroyed();
  }

  public void stopThread() {
    if (this.a_da != null) {
      this.a_da.b();

      try {
        Thread.sleep(2000L);
        return;
      } catch (InterruptedException var2) {
        var2.printStackTrace();
      }
    }
  }
}
