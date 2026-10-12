import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

public final class QQCommunityOverlay implements PortalListener {
   private byte viewType;
   private static byte VIEW_NONE = 0;
   private static byte VIEW_PROFILE = 1;
   private static byte VIEW_RANKING = 2;
   private static byte VIEW_UPLOAD = 3;
   private byte status;
   private static byte STATUS_IDLE = 0;
   private static byte STATUS_LOADING = 1;
   private static byte STATUS_NOT_LOGGED_IN = 2;
   private static byte STATUS_DONE = 3;
   private static byte STATUS_TIMEOUT = 4;
   private static byte STATUS_UPLOAD_FAILED = 5;
   private short screenWidth;
   private short screenHeight;
   private boolean swapSoftKeys;
   private int uploadedScore;
   private short textWidth;
   private int rankColW;
   private int nameColW;
   private int scoreColW;
   private int tableWidthWanted;
   private int tableWidth;
   private int tableX;
   private int rowCount;
   private int tableY;
   private QQUserProfile userProfile;
   private LeaderboardEntry[] leaderboard;
   private long requestStartTime;
   private String message;
   private static QQCommunityOverlay instance;
   private static PortalApi api;
   private byte[] nameScrollX;
   private byte[] nameScrollPause;
   private StringBuffer loadingDots = new StringBuffer(".");

   private QQCommunityOverlay(short var1, short var2, boolean var3) {
      this.screenWidth = var1;
      this.screenHeight = var2;
      this.swapSoftKeys = var3;
      this.textWidth = (short)((var1 << 2) / 5);
   }

   private boolean onKey(int var1) {
      boolean var2 = true;
      if (this.status == STATUS_DONE) {
         if (var1 == KeyMap.softLeftKey) {
            this.reset();
            openPage(12);
         } else if (var1 == KeyMap.softRightKey) {
            this.reset();
         }
      } else if (this.status == STATUS_NOT_LOGGED_IN) {
         if (var1 == KeyMap.softLeftKey) {
            this.reset();
            openPage(16);
         } else if (var1 == KeyMap.softRightKey) {
            this.reset();
         }
      } else if (this.status == STATUS_TIMEOUT) {
         this.reset();
      } else if (this.status == STATUS_LOADING) {
         this.reset();
      } else if (this.status == STATUS_UPLOAD_FAILED) {
         if (var1 == KeyMap.softLeftKey) {
            uploadScore(this.uploadedScore);
         } else if (var1 == KeyMap.softRightKey) {
            this.reset();
         }
      } else if (this.status == 0) {
         if (this.viewType == VIEW_UPLOAD) {
            this.reset();
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public final void onPortalResult(int var1, PortalResult var2) {
      int var3 = var2.getCode();
      String var4 = var2.getMessage();
      System.out.println("Loại dữ liệu:" + var1);
      System.out.println("Kết quả dữ liệu:" + var3);
      System.out.println("Thông tin dữ liệu:" + var4);
      if (var3 == 0) {
         if (var1 == 7) {
            this.userProfile = api.getUserProfile();
         } else if (var1 == 12) {
            this.parseLeaderboard(var4);
         } else if (var1 == 3) {
            this.message = var4;
         }

         this.status = STATUS_DONE;
      } else {
         if (var3 == -1) {
            this.status = STATUS_NOT_LOGGED_IN;
         }
      }
   }

   private static void openPage(int var0) {
      api.openPage(1, var0);
   }

   private void reset() {
      this.viewType = 0;
      this.status = 0;
      this.userProfile = null;
      this.leaderboard = null;
      this.requestStartTime = 0L;
   }

   private void drawWrappedText(Graphics var1, String var2, int var3, int var4) {
      String[] var6 = Utils.wrapText(var2, this.textWidth);
      var1.setColor(16777215);

      for (byte var5 = 0; var5 < var6.length; var5++) {
         vh.drawString(var1, var6[var5], var3, var4 - (Utils.lineHeight + 2) * (var6.length - 1 - var5), 33);
      }
   }

   private void parseLeaderboard(String var1) {
      String[] var4;
      if ((var4 = Utils.split(var1, "|#|")) != null) {
         this.leaderboard = new LeaderboardEntry[var4.length];

         for (byte var2 = 0; var4 != null && var2 < var4.length; var2++) {
            String[] var3 = Utils.split(var4[var2], "|");
            this.leaderboard[var2] = new LeaderboardEntry();
            this.leaderboard[var2].score = Integer.parseInt(var3[0]);
            this.leaderboard[var2].nickName = var3[1];
         }
      }
   }

   private void drawLeaderboard(Graphics var1) {
      this.rankColW = Utils.fontWidthMM * 3;
      this.nameColW = Utils.fontWidthMM * 7;
      this.scoreColW = Utils.fontWidthMM << 1;
      this.tableWidthWanted = this.rankColW + this.nameColW + this.scoreColW;
      this.tableWidth = this.tableWidthWanted < this.textWidth ? this.tableWidthWanted : this.textWidth;
      this.nameColW = this.tableWidth - this.rankColW - this.scoreColW;
      this.tableX = (this.screenWidth - this.tableWidth) / 2;
      this.rowCount = (byte)((this.screenHeight / Utils.lineHeight << 2) / 5);
      this.tableY = (this.screenHeight - this.rowCount * Utils.lineHeight) / 2;
      this.rowCount = this.rowCount < this.leaderboard.length ? this.rowCount : this.leaderboard.length;
      var1.setColor(16777215);
      vh.drawString(var1, "Bảng xếp hạng", this.screenWidth / 2, (this.tableY - Utils.lineHeight) / 2, 17);
      vh.drawString(var1, "Hạng", this.tableX, this.tableY, 20);
      vh.drawString(var1, "Tên", this.tableX + this.rankColW, this.tableY, 20);
      vh.drawString(var1, "Điểm", this.tableX + this.rankColW + this.nameColW, this.tableY, 20);

      for (int var2 = 0; this.leaderboard != null && var2 < this.rowCount; var2++) {
         vh.drawString(var1, "" + (var2 + 1), this.tableX + Utils.fontWidthMM, this.tableY + Utils.lineHeight * (var2 + 1), 17);
         vh.drawString(var1, "" + this.leaderboard[var2].score, this.tableX + this.rankColW + this.nameColW, this.tableY + Utils.lineHeight * (var2 + 1), 20);
      }

      for (int var7 = 0; this.leaderboard != null && var7 < this.rowCount; var7++) {
         if (this.nameScrollX == null) {
            this.nameScrollX = new byte[this.rowCount];
            this.nameScrollPause = new byte[this.rowCount];
         }

         int var6 = this.nameColW - Utils.fontWidthMM / 2;
         var1.setClip(this.tableX + this.rankColW, this.tableY + Utils.lineHeight, var6, this.screenHeight);
         vh.drawString(
            var1, this.leaderboard[var7].nickName, this.tableX + this.rankColW + this.nameScrollX[var7], this.tableY + Utils.lineHeight * (var7 + 1), 20
         );
         if (vh.stringWidth(Utils.defaultFont, this.leaderboard[var7].nickName) > var6) {
            if (Math.abs(this.nameScrollX[var7]) < vh.stringWidth(Utils.defaultFont, this.leaderboard[var7].nickName) - var6) {
               if (this.nameScrollPause[var7] > 0) {
                  this.nameScrollPause[var7]--;
               } else {
                  this.nameScrollX[var7]--;
               }
            } else if (this.nameScrollPause[var7] < 30) {
               this.nameScrollPause[var7]++;
            } else {
               this.nameScrollX[var7] = 0;
            }
         }

         var1.setClip(0, 0, this.screenWidth, this.screenHeight);
      }
   }

   private void drawYesNo(Graphics var1) {
      this.drawSoftKeyLabels(var1, "Có", "Không");
   }

   private void drawSoftKeyLabels(Graphics var1, String var2, String var3) {
      var1.setColor(16777215);
      if (this.swapSoftKeys) {
         vh.drawString(var1, var3, 5, this.screenHeight - 5, 36);
         if (var2 != null) {
            vh.drawString(var1, var2, this.screenWidth - 5, this.screenHeight - 5, 40);
            return;
         }
      } else {
         if (var2 != null) {
            vh.drawString(var1, var2, 5, this.screenHeight - 5, 36);
         }

         vh.drawString(var1, var3, this.screenWidth - 5, this.screenHeight - 5, 40);
      }
   }

   public static void init(MIDlet var0, Display var1, Displayable var2, short var3, short var4, boolean var5, int var6, int var7) {
      instance = new QQCommunityOverlay(var3, var4, false);
      api = new PortalApi(var0, var1, var2, instance, var3, var4, KeyMap.softLeftCodes[0], KeyMap.softRightCodes[0], var6, 120, "");
   }

   public static void drawOverlay(Graphics var0) {
      if (instance != null) {
         QQCommunityOverlay var10 = instance;
         if (instance.status != 0 || var10.viewType == VIEW_UPLOAD) {
            short var5 = var10.screenHeight;
            short var4 = var10.screenWidth;
            byte var2 = 0;
            var2 = (boolean)0;
            Graphics var3 = var0;
            if ((var2 = (byte)(var5 / 44)) * 22 << 1 < var5) {
               var2++;
            }

            byte var14;
            if ((var14 = (byte)(var4 / 44)) * 22 << 1 < var4) {
               var14++;
            }

            for (int var13 = 0; var13 < var14; var13++) {
               for (int var6 = 0; var6 < var2; var6++) {
                  int var10001 = 0 + (var13 * 22 << 1);
                  int var9 = 0 + (var6 * 22 << 1);
                  int var8 = var10001;
                  var3.setColor(1259628);
                  var3.fillRect(var8, var9, 22, 22);
                  var3.fillRect(var8 + 22, var9 + 22, 22, 22);
                  var3.setColor(996456);
                  var3.fillRect(var8 + 22, var9, 22, 22);
                  var3.fillRect(var8, var9 + 22, 22, 22);
               }
            }

            if (var10.status == 0) {
               if (var10.viewType == VIEW_UPLOAD) {
                  var10.drawWrappedText(var0, "Nhấn phím bất kỳ để về trò chơi", var10.screenWidth / 2, var10.screenHeight / 2);
                  return;
               }
            } else {
               if (var10.status == STATUS_LOADING) {
                  var0.setColor(16777215);
                  var10.loadingDots.append(".");
                  if (var10.loadingDots.toString().length() > 5) {
                     var10.loadingDots = new StringBuffer(".");
                  }

                  vh.drawString(
                     var0,
                     "Đang đọc dữ liệu " + var10.loadingDots.toString(),
                     (var10.screenWidth - vh.stringWidth(Utils.defaultFont, "Đang đọc dữ liệu.....")) / 2,
                     var10.screenHeight / 2,
                     36
                  );
                  vh.drawString(var0, "(Nhấn phím bất kỳ để thoát)", var10.screenWidth / 2, var10.screenHeight / 2 + Utils.lineHeight, 17);
                  if (System.currentTimeMillis() - var10.requestStartTime > 10000L) {
                     System.out.println("Bắt đầu:" + var10.requestStartTime);
                     System.out.println("Kết thúc:" + System.currentTimeMillis());
                     var10.status = STATUS_TIMEOUT;
                  }

                  return;
               }

               if (var10.status == STATUS_DONE) {
                  if (var10.viewType == VIEW_PROFILE) {
                     var0.setColor(16777215);
                     vh.drawString(var0, "Tên QQ:" + var10.userProfile.nickName, 5, var10.screenHeight / 2 - Utils.lineHeight, 36);
                     vh.drawString(var0, "Cấp:" + var10.userProfile.level, 5, var10.screenHeight / 2, 36);
                     vh.drawString(var0, "Điểm cao nhất:" + var10.userProfile.score, 5, var10.screenHeight / 2 + Utils.lineHeight, 36);
                     vh.drawString(var0, "Điểm hiện tại:" + 0, 5, var10.screenHeight / 2 + (Utils.lineHeight << 1), 36);
                  } else if (var10.viewType == VIEW_RANKING) {
                     if (var10.leaderboard != null) {
                        var10.drawLeaderboard(var0);
                     } else {
                        var10.drawWrappedText(
                           var0, "Chưa ai lên bảng, mau giành hạng nhất nào!", var10.screenWidth / 2, (var10.screenWidth - Utils.lineHeight) / 2
                        );
                     }
                  } else if (var10.viewType == VIEW_UPLOAD) {
                     var10.drawWrappedText(var0, var10.message, var10.screenWidth / 2, var10.screenHeight / 2);
                  }

                  var10.drawSoftKeyLabels(var0, "Cộng đồng QQ", "Trở về");
                  return;
               }

               if (var10.status == STATUS_NOT_LOGGED_IN) {
                  var10.drawWrappedText(var0, "Chưa đăng nhập, đăng nhập ngay?", var10.screenWidth / 2, var10.screenHeight / 2);
                  var10.drawYesNo(var0);
                  return;
               }

               if (var10.status == STATUS_TIMEOUT) {
                  var10.drawWrappedText(
                     var0, "Hết thời gian lấy dữ liệu, hãy kiểm tra cài đặt mạng!", var10.screenWidth / 2, var10.screenHeight / 2 - Utils.lineHeight
                  );
                  vh.drawString(var0, "Nhấn phím bất kỳ để tiếp tục", var10.screenWidth / 2, var10.screenHeight / 2, 33);
                  return;
               }

               if (var10.status == STATUS_UPLOAD_FAILED) {
                  var10.drawWrappedText(var0, "Tải lên thất bại, thử lại?", var10.screenWidth / 2, var10.screenHeight / 2);
                  var10.drawYesNo(var0);
               }
            }
         }
      }
   }

   public static boolean handleKey(int var0) {
      boolean var1 = false;
      if (instance != null) {
         var1 = instance.onKey(var0);
      }

      return var1;
   }

   public static boolean handlePointer(int var0, int var1) {
      if (instance != null) {
         QQCommunityOverlay var4 = instance;
         boolean var3 = false;
         if (Utils.rectsOverlap(0, var4.screenHeight - 22, 22, 22, var0, var1, 1, 1)) {
            if (var4.swapSoftKeys) {
               var4.onKey(KeyMap.softRightKey);
            } else {
               var4.onKey(KeyMap.softLeftKey);
            }

            var3 = true;
         } else if (Utils.rectsOverlap(var4.screenWidth - 22, var4.screenHeight - 22, 22, 22, var0, var1, 1, 1)) {
            if (var4.swapSoftKeys) {
               var4.onKey(KeyMap.softLeftKey);
            } else {
               var4.onKey(KeyMap.softRightKey);
            }

            var3 = true;
         }
      }

      return false;
   }

   public static void openHomePage() {
      openPage(10);
   }

   public static void showRanking() {
      short var0 = 2005;
      QQCommunityOverlay var1 = instance;
      instance.requestStartTime = System.currentTimeMillis();
      api.sendCommand(2005);
      instance.viewType = VIEW_RANKING;
      instance.status = STATUS_LOADING;
   }

   public static void uploadScore(int var0) {
      String var1 = "";
      var0 = var0;
      instance.viewType = VIEW_UPLOAD;
      if (var0 <= 0) {
         instance.message = "Điểm hiện tại của bạn là 0, tải lên không hợp lệ.";
         instance.status = STATUS_DONE;
      } else {
         instance.requestStartTime = System.currentTimeMillis();

         try {
            instance.uploadedScore = var0;
            if (var1 != null && !var1.equals("")) {
               var1 = "1:" + var1;
            }

            api.uploadScore(var0, "", 0, 0, "", "", var1);
            instance.status = 0;
         } catch (Exception var2) {
            var2.printStackTrace();
            instance.status = STATUS_UPLOAD_FAILED;
         }
      }
   }
}
