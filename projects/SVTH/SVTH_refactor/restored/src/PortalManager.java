import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;

public final class PortalManager {
   PortalListener listener;
   public int loadState;
   private PortalProtocol protocol;
   private PortalClient client;
   public int progressPercent;
   private int progressTick;
   public boolean isResponseReceived;
   private int waitTicks;
   private Vector structRecords;
   private QQUserProfile userProfile;
   private Vector leaderboard;

   public PortalManager(PortalClient var1) {
      this.client = var1;
      this.structRecords = new Vector();
      this.leaderboard = new Vector();
      this.protocol = new PortalProtocol(var1, this);
   }

   public final void startNetwork() {
      this.protocol.start();
   }

   public final void stopNetwork() {
      this.protocol.stop();
   }

   public final void cancelRequest() {
      this.protocol.cancel();
   }

   public final void renderPage(Hashtable[] var1) {
      if (PortalClient.isPortalShown) {
         if (this.client.getCanvas().isEmbeddedMode) {
            this.client.getBrowser().clearPage();
         }

         PortalCanvasBase.pageImages.removeAllElements();
      }

      for (int var4 = 0; var4 < var1.length; var4++) {
         int var3 = -1;
         String var2 = getString(var1[var4], "type");

         for (int var5 = 0; var5 < PageWidget.TAG_NAMES.length; var5++) {
            if (PageWidget.TAG_NAMES[var5].equals(var2)) {
               var3 = var5;
               break;
            }
         }

         switch (var3) {
            case 0:
               Hashtable var25 = var1[var4];
               this.client.getBrowser().addText(var25);
               break;
            case 1:
               Hashtable var24 = var1[var4];
               this.client.getBrowser().addInput(var24);
               break;
            case 2:
               Hashtable var23 = var1[var4];
               this.client.getBrowser().addButton(var23);
               break;
            case 3:
               Hashtable var22 = var1[var4];
               if (getString(var22, "name").endsWith("_server")) {
                  HttpRequest.portalServerUrl = getString(var22, "value");
               }

               this.client.settings.put(var22.get("name"), var22.get("value"));
               break;
            case 4:
               this.client.getBrowser().newLine();
               break;
            case 5:
               Hashtable var21 = var1[var4];
               String var34;
               if ((var34 = getString(var21, "value")).length() > 0) {
                  var34 = var34;

                  try {
                     byte[] var36;
                     Image var37 = Image.createImage(var36 = Base64Decoder.decode(var34), 0, var36.length);
                     PortalCanvasBase.pageImages.addElement(var37);
                  } catch (Exception var13) {
                     var13.printStackTrace();
                  }
               }

               this.client.getBrowser().addImage(var21);
            case 6:
            default:
               break;
            case 7:
               Hashtable var19 = var1[var4];
               int var38 = getInt(var19, "id", -1);

               PortalResult var41;
               try {
                  String var45 = getString(var19, "code");
                  String var42 = getString(var19, "subCode");
                  String var9 = getString(var19, "msg");
                  Hashtable var28;
                  if (var45.length() == 0 && var42.length() == 0 && var9.length() == 0) {
                     byte[] var20 = getString(var19, "value").getBytes("UTF-8");
                     byte var29 = 0;
                     short var46 = PortalProtocol.getInt16(var20, 0);
                     int var30 = var29 + 2;
                     Hashtable var10000;
                     if (var46 == 0) {
                        var10000 = null;
                     } else {
                        Hashtable var11 = new Hashtable(var46);

                        for (int var12 = 0; var12 < var46; var12++) {
                           byte var47 = var20[var30];
                           String var43 = new String(var20, ++var30, var47, "UTF-8");
                           int var32 = var30 + var47;
                           short var48 = PortalProtocol.getInt16(var20, var32);
                           int var33 = var32 + 2;
                           String var10 = new String(var20, var33, var48, "UTF-8");
                           var30 = var33 + var48;
                           var11.put(var43, var10);
                        }

                        var10000 = var11;
                     }

                     var28 = var10000;
                  } else {
                     (var28 = new Hashtable(3)).put("code", var45);
                     var28.put("subCode", var42);
                     var28.put("msg", var9);
                  }

                  var41 = new PortalResult(getInt(var28, "code", -1), getInt(var28, "subCode", -1), getString(var28, "msg"));
               } catch (Exception var14) {
                  var14.printStackTrace();
                  var41 = new PortalResult(-1, -1, "");
               }

               label107:
               switch (var38) {
                  case 1:
                     switch (var41.getCode()) {
                        case 1:
                           if (this.client.getCanvas().mode != 1) {
                              this.client.getCanvas().setMode(1);
                           }

                           this.client.getCanvas().showMessage(var41.getMessage());
                           break label107;
                        case 2:
                           this.client.a();
                           this.client.getCanvas().showMessage(var41.getMessage());
                        default:
                           break label107;
                     }
                  case 2:
                     switch (var41.getCode()) {
                        case 0:
                           int var49;
                           if ((var49 = Integer.parseInt((String)this.client.settings.get("_vertifyValue")))
                              == Integer.parseInt((String)this.client.settings.get("_vCInterval"))) {
                              var49 = 0;
                           } else {
                              var49++;
                           }

                           this.client.settings.put("_vertifyValue", String.valueOf(var49));
                           this.client.getCanvas().closePortal();
                           break label107;
                        case 1:
                           this.client.getCanvas().showMessage(var41.getMessage());
                           this.client.getCanvas().setMode(5);
                        default:
                           break label107;
                     }
                  default:
                     if (PortalClient.isPortalShown) {
                        this.client.getCanvas().closePortal();
                        if (this.client.gameScreen != null) {
                           this.client.display.setCurrent(this.client.gameScreen);
                        }
                     } else {
                        this.protocol.stop();
                     }
               }

               this.storeResultData(var38, var41);
               this.listener.onPortalResult(var38, var41);
               break;
            case 8:
               Hashtable var18 = var1[var4];
               this.client.getBrowser().addPartLine(var18);
               break;
            case 9:
               Hashtable var17 = var1[var4];
               PortalBrowser var27 = this.client.getBrowser();
               int var40 = getInt(var17, "pixel", 0);
               var27.cursorX += var40;
               if (var27.cursorX > var27.contentWidth) {
                  var27.newLine();
               }
               break;
            case 10:
               Hashtable var16 = var1[var4];
               PortalBrowser var26 = this.client.getBrowser();
               PageRequest var39;
               (var39 = new PageRequest()).isRedirect = true;
               var39.href = getString(var16, "href");
               PageInputWidget[] var44 = var26.findInputs(getString(var16, "addField"));
               var39.setCacheKeys(getString(var16, "cache"));
               var26.currentRequest = var39;
               var26.manager.request(var39, var44);
               this.client.getBrowser().showMessagePage("Đang kết nối... Vui lòng đợi!");
               return;
            case 11:
               Hashtable var15 = var1[var4];
               switch (getInt(var15, "id", -1)) {
                  case 7:
                     QQUserProfile var6 = new QQUserProfile();
                     getString(var15, "uid");
                     getInt(var15, "age", 0);
                     getString(var15, "dataKey");
                     getString(var15, "dataValue");
                     getInt(var15, "honor", 0);
                     var6.level = getInt(var15, "level", 0);
                     getInt(var15, "lost", 0);
                     var6.nickName = getString(var15, "nickName");
                     var6.score = getInt(var15, "score", 0);
                     getInt(var15, "sex", 0);
                     getInt(var15, "status", 0);
                     getInt(var15, "win", 0);
                     this.structRecords.addElement(var6);
                     break;
                  case 8:
                  case 10:
                     RankEntry var7 = new RankEntry();
                     getString(var15, "uid");
                     getInt(var15, "age", 0);
                     getString(var15, "dataKey");
                     getString(var15, "dataValue");
                     getInt(var15, "level", 0);
                     getString(var15, "nickName");
                     getInt(var15, "score", 0);
                     getInt(var15, "sex", 0);
                     this.structRecords.addElement(var7);
                     break;
                  case 9:
                     LeaderboardEntry var8;
                     (var8 = new LeaderboardEntry()).uid = getString(var15, "uid");
                     getInt(var15, "age", 0);
                     getString(var15, "dataKey");
                     getString(var15, "dataValue");
                     var8.nickName = getString(var15, "nickName");
                     var8.score = getInt(var15, "score", 0);
                     getInt(var15, "sex", 0);
                     getInt(var15, "time", 0);
                     getInt(var15, "iType", 0);
                     this.structRecords.addElement(var8);
               }
         }
      }

      if (PortalClient.isPortalShown) {
         if (this.client.getCanvas().isEmbeddedMode) {
            this.client.getBrowser().finishLayout();
         }

         if (this.client.getBrowser().widgets.size() == 0) {
            this.client.getBrowser().showMessagePage("Mạng đang bận‚ vui lòng thử lại sau!");
         }
      }
   }

   public final void onPageReceived(Hashtable[] var1) {
      this.isResponseReceived = true;
      this.structRecords.removeAllElements();
      if (PortalClient.isPortalShown && this.client.getCanvas().isEmbeddedMode) {
         this.client.getBrowser().onPageLoaded(var1);
      }
   }

   public static String getString(Hashtable var0, String var1) {
      String var2;
      if ((var2 = (String)var0.get(var1)) == null) {
         var2 = "";
      }

      return var2;
   }

   public static int getInt(Hashtable var0, String var1, int var2) {
      String var3;
      return (var3 = (String)var0.get(var1)) != null && var3.length() != 0 ? Integer.parseInt(var3) : var2;
   }

   public final void tickProgress() {
      if (this.loadState == 1) {
         if (this.isResponseReceived) {
            this.progressPercent += 15;
            if (this.progressPercent > 100) {
               this.progressPercent = 100;
            }

            if (this.progressPercent == 100) {
               this.loadState = 0;
               return;
            }
         } else {
            if (this.progressPercent < 40 && this.progressTick++ % 8 == 0) {
               this.progressPercent++;
            }

            this.waitTicks++;
            if (this.waitTicks > 300) {
               this.protocol.cancel();
               this.onError("Hết thời gian kết nối‚ vui lòng thử lại!");
               this.loadState = 0;
            }
         }
      }
   }

   public final void beginLoading() {
      this.loadState = 1;
      this.progressPercent = 0;
      this.progressTick = 0;
      this.isResponseReceived = false;
      this.waitTicks = 0;
   }

   public final void onError(String var1) {
      if (PortalClient.isPortalShown) {
         this.client.getCanvas().showMessage(var1);
      } else {
         this.protocol.stop();
      }

      this.isResponseReceived = true;
   }

   private static void setCommandUrl(PageRequest var0, int var1) {
      switch (var1) {
         case 1:
            var0.href = "invoke/pay.m?_type=1";
            return;
         case 2:
            var0.href = "invoke/pay.m?_type=2";
            return;
         case 10:
            var0.href = "index.m";
            return;
         case 12:
            var0.href = "scoreTop.m";
            return;
         case 16:
            var0.href = "profile.m";
            return;
         case 17:
            var0.href = "loginInput.m";
            return;
         case 18:
            var0.href = "verify.m";
            return;
         case 20:
            var0.href = "onlineList.m";
            return;
         case 100:
            var0.href = "help.m";
            return;
         case 101:
            var0.href = "login.m";
            return;
         case 102:
            var0.href = "logout.m";
         default:
            return;
         case 1001:
            var0.href = "invoke/getUserInfo.m";
            return;
         case 1002:
            var0.href = "invoke/getOppUserInfo.m";
            return;
         case 1003:
            var0.href = "invoke/getServTime.m";
            return;
         case 1504:
            var0.href = "invoke/getFeedInfo.m?_type=0";
            return;
         case 1505:
            var0.href = "invoke/getFeedInfo.m?_type=1";
            return;
         case 1506:
            var0.href = "invoke/getFeedInfo.m?_type=2";
            return;
         case 1507:
            var0.href = "invoke/getFeedInfo.m?_type=3";
            return;
         case 1508:
            var0.href = "invoke/getFeedInfo.m?_type=4";
            return;
         case 1509:
            var0.href = "invoke/joinChallenge.m";
            return;
         case 1510:
            var0.href = "invoke/giveUpChallenge.m";
            return;
         case 2002:
            var0.href = "invoke/getRankData.m?_type=2";
            return;
         case 2003:
            var0.href = "invoke/getRankData.m?_type=1";
            return;
         case 2005:
            var0.href = "invoke/getRankData.m?_type=3";
            return;
         case 2100:
            var0.href = "invoke/uploadData.m";
      }
   }

   public final void sendCommand(int var1) {
      PageRequest var2;
      setCommandUrl(var2 = new PageRequest(), var1);
      var2.params = new Hashtable();
      String var3 = (String)this.client.settings.get("_uid");
      String var4;
      if (var1 == 18 && var3 != null && (var4 = (String)this.client.settings.get(var3)) != null) {
         var2.params.put(var3, var4);
      }

      this.addSessionParams(var2);
      this.protocol.sendRequest(var2, null);
   }

   public final void request(PageRequest var1, PageInputWidget[] var2) {
      if (var1.href.length() != 0) {
         this.protocol.sendRequest(var1, var2);
      }
   }

   private void addSessionParams(PageRequest var1) {
      String var2 = (String)this.client.settings.get("_uid");
      String var3 = (String)this.client.settings.get("_sid");
      String var4 = (String)this.client.settings.get("_a8");
      if (var2 == null) {
         var1.params.put("_uid", "");
      } else {
         var1.params.put("_uid", var2);
      }

      if (var3 == null) {
         var1.params.put("_sid", "");
      } else {
         var1.params.put("_sid", var3);
      }

      if (var4 == null) {
         var1.params.put("_a8", "");
      } else {
         var1.params.put("_a8", var4);
      }
   }

   protected final void uploadScore(int var1, String var2, int var3, int var4, String var5, String var6, String var7) {
      PageRequest var8;
      setCommandUrl(var8 = new PageRequest(), 2100);
      var8.params = new Hashtable();
      this.addSessionParams(var8);
      var8.params.put("_score", String.valueOf(var1));
      var8.params.put("_oppUin", var2);
      var8.params.put("_oppScore", String.valueOf(var3));
      var8.params.put("_winner", String.valueOf(var4));
      var8.params.put("_dataKey", var5);
      var8.params.put("_dataValue", var6);
      var8.params.put("_extInfo", var7);
      this.protocol.sendRequest(var8, null);
   }

   private void storeResultData(int var1, PortalResult var2) {
      switch (var1) {
         case 6:
            return;
         case 7:
            this.userProfile = null;
            Object var6;
            if (this.structRecords.size() > 0 && (var6 = this.structRecords.elementAt(this.structRecords.size() - 1)) instanceof QQUserProfile) {
               this.userProfile = (QQUserProfile)var6;
               return;
            }
            break;
         case 8:
         case 10:
            if (this.structRecords.size() > 0 && this.structRecords.elementAt(this.structRecords.size() - 1) instanceof RankEntry) {
               return;
            }
            break;
         case 9:
            this.leaderboard.removeAllElements();

            for (int var5 = 0; this.structRecords != null && var5 < this.structRecords.size(); var5++) {
               if (this.structRecords.elementAt(var5) instanceof LeaderboardEntry) {
                  LeaderboardEntry var7 = (LeaderboardEntry)this.structRecords.elementAt(var5);

                  int var3;
                  for (var3 = 0; var3 < this.leaderboard.size(); var3++) {
                     LeaderboardEntry var4 = (LeaderboardEntry)this.leaderboard.elementAt(var3);
                     if (var7.uid.equals(var4.uid)) {
                        break;
                     }
                  }

                  if (var3 == this.leaderboard.size()) {
                     if (this.leaderboard.size() == 50) {
                        this.leaderboard.removeElementAt(0);
                     }

                     this.leaderboard.addElement(var7);
                  }
               }
            }
      }
   }

   public final QQUserProfile getUserProfile() {
      return this.userProfile;
   }

   public final void clearUserData() {
      this.userProfile = null;
      this.leaderboard.removeAllElements();
   }
}
