import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

public final class SmsPaymentFlow implements ResultListener {
   private byte payMode;
   private String productDesc = null;
   private static short screenWidth = 240;
   private static short screenHeight = 320;
   private static String[] smsPrefixes;
   private static String[] smsNumbers;
   private static String[] smsMessages;
   private byte sentCount;
   private String gameIdProp;
   private String channelIdProp;
   private String productIdProp;
   private static byte state;
   private boolean lotteryWon;
   private String lotteryMsg;
   private String msgLotteryPending = "Lấy ưu đãi chưa xong, nhấn phím mềm phải để quay lại bốc thăm, nhấn phím mềm trái để bỏ bốc thăm và tiếp tục mua.";
   private String msgLotteryFailed = "Lấy ưu đãi qua mạng thất bại, nhấn phím mềm phải để thử lại, nhấn phím mềm trái để bỏ ưu đãi và tiếp tục mua.";
   private String msgRetryFailed = "Thử lại thất bại, có thể quyền mạng tạm bị chặn, lần sau vào game sẽ khôi phục, nhấn phím mềm trái để bỏ ưu đãi và tiếp tục mua.";
   private String msgPaymentIncomplete = "Bạn chưa hoàn tất thanh toán‚xin đừng thoát. Bấm phím mềm phải để trở về tiếp tục mua‚bấm phím mềm trái để thoát mua hàng.";
   private String msgSuccess = "Chúc mừng bạn mua thành công, cảm ơn bạn đã ủng hộ game Tỉnh Trung Nguyệt! Nhấn phím mềm phải để trở về tận hưởng thêm nhiều nội dung hấp dẫn.";
   private ResultListener resultListener = null;
   private String[] smsContents;
   private byte[] scrollOffsets;
   private short visibleLines;
   private String[] wrappedLines;
   private int retryCount;
   private LotteryRequest lotteryRequest;
   private int textColor = 16777215;
   private int c_I = 0;
   private int disabledColor = 7368816;
   private boolean showScrollHint;
   private String scrollHint = "Hãy cuộn xuống đọc hết rồi mới bấm OK";
   private String[] scrollHintLines;
   private String productId;
   private String[] sentProductIds;
   private byte[] sentCounts;
   private String GATE_STORE = "onegateinfo";
   private String SENDTIMES_STORE = "sendtimes";
   private Image topbarLogo;
   private Image topbarBack;
   private Image topbarUrl;
   private Image bottomBack;
   private byte topBarHeight;
   private static String gateInfo;
   private static String[][] smscAreaTable = new String[][]{
      {"Unicom", "86130"},
      {"Bắc Kinh", "1380010"},
      {"Thiên Tân", "1380022"},
      {"Hà Bắc", "1380031", "1380033"},
      {"Sơn Tây", "1380035", "1380034"},
      {"Nội Mông", "1380047", "1380048"},
      {"Liêu Ninh", "1380024", "1380041", "1380042"},
      {"Cát Lâm", "1380043"},
      {"Hắc Long Giang", "1380045"},
      {"Thượng Hải", "1380021", "1380024"},
      {"Giang Tô", "1380051", "1380025", "1380052"},
      {"Chiết Giang", "1380057", "1380058"},
      {"An Huy", "1380055", "1380056"},
      {"Phúc Kiến", "1380059"},
      {"Giang Tây", "1380079", "1380070"},
      {"Sơn Đông", "1380053", "1380063", "1380054"},
      {"Hà Nam", "1380037", "1380039"},
      {"Hồ Bắc", "1380027", "1380071", "1380072"},
      {"Hồ Nam", "1380073", "1380074"},
      {"Quảng Đông", "1380020", "1380075", "1380076", "1380066"},
      {"Quảng Tây", "1380077"},
      {"Hải Nam", "1380089"},
      {"Trùng Khánh", "1380023"},
      {"Tứ Xuyên", "1380028", "1380081", "1380082", "1380083"},
      {"Quý Châu", "1380085"},
      {"Vân Nam", "1380087", "1380088"},
      {"Thiểm Tây", "1380029", "1380091"},
      {"Cam Túc", "1380093", "1380094"},
      {"Thanh Hải", "1380097"},
      {"Ninh Hạ", "1380095"},
      {"Tân Cương", "1380099", "1380090"}
   };
   private String smscNumber;
   private String areaName;
   private int needBuyAmount;
   private int handselAmount;
   private int boughtCount;
   private int handselCount;

   public SmsPaymentFlow(MIDlet var1) {
      this.channelIdProp = var1.getAppProperty("cid");
      var1.getAppProperty("sid");
      this.gameIdProp = var1.getAppProperty("gid");
      this.productIdProp = var1.getAppProperty("pid");
      if (this.channelIdProp == null || this.channelIdProp.equals("")) {
         this.channelIdProp = "18";
      }

      if (this.gameIdProp == null || this.gameIdProp.equals("")) {
         this.gameIdProp = "100";
      }

      if (this.productIdProp == null || this.productIdProp.equals("")) {
         this.productIdProp = "01";
      }

      this.scrollOffsets = new byte[14];
      this.visibleLines = (short)((screenHeight - (Utils.lineHeight + 4 << 1)) / Utils.lineHeight);
      this.scrollHintLines = Utils.wrapText(this.scrollHint, screenWidth - 8);
      this.loadSendTimes();
      this.topbarLogo = Utils.loadImage("/ui/topbar_logo.gf");
      this.topbarBack = Utils.loadImage("/ui/topbar_back.gf");
      this.topbarUrl = Utils.loadImage("/ui/topbar_url.gf");
      this.bottomBack = Utils.loadImage("/ui/bottom_back.gf");
      if (this.topbarBack != null) {
         this.topBarHeight = (byte)(this.topbarBack.getHeight() + 2);
      } else {
         this.topBarHeight = 31;
      }

      if (this.topbarUrl == null) {
         this.msgSuccess = "Chúc mừng bạn mua thành công, cảm ơn bạn đã ủng hộ game của chúng tôi! Nhấn phím mềm phải để trở về tận hưởng thêm nhiều nội dung hấp dẫn.";
      }
   }

   public final void onResult(int var1, String var2) {
      if (var1 == 0) {
         this.sentCount++;
         if (this.sentCount < smsMessages.length) {
            state = 6;
            this.scrollOffsets[6] = 0;
         } else {
            state = 9;
         }
      } else if (var1 == 2) {
         state = 10;
      } else {
         state = 11;
      }
   }

   private boolean detectArea() {
      try {
         this.smscNumber = System.getProperty("wireless.messaging.sms.smsc");
         System.out.println("Lấy số trung tâm tin nhắn " + this.smscNumber);
         if (this.smscNumber != null) {
            for (int var1 = 0; var1 < smscAreaTable.length; var1++) {
               for (int var2 = 1; var2 < smscAreaTable[var1].length; var2++) {
                  if (this.smscNumber.indexOf(smscAreaTable[var1][var2]) != -1) {
                     this.areaName = smscAreaTable[var1][0];
                     break;
                  }
               }

               if (this.areaName != null) {
                  break;
               }
            }
         } else {
            this.areaName = "Telecom";
         }

         if (this.areaName == null) {
            this.areaName = "Không rõ";
         }

         System.out.println("Xác định vùng của máy:" + this.areaName);
         return true;
      } catch (Exception var3) {
         System.out.println("Chưa lắp thẻ SIM hoặc điện thoại đang ngoại tuyến");
         return false;
      }
   }

   private static boolean isLotteryToday() {
      RecordStore var0 = null;
      byte[] var1 = null;

      try {
         RecordEnumeration var2;
         if ((var0 = RecordStore.openRecordStore("lotteryDate", false)) != null && (var2 = var0.enumerateRecords(null, null, false)).hasNextElement()) {
            int var12 = var2.nextRecordId();
            var1 = var0.getRecord(var12);
         }

         if (var0 != null) {
            var0.closeRecordStore();
         }
      } catch (RecordStoreFullException var5) {
         var5.printStackTrace();
      } catch (RecordStoreNotFoundException var6) {
      } catch (RecordStoreException var7) {
      }

      if (var1 != null) {
         ByteArrayInputStream var13 = new ByteArrayInputStream(var1);
         DataInputStream var14 = new DataInputStream(var13);

         try {
            short var9 = var14.readShort();
            byte var11 = var14.readByte();
            byte var15 = var14.readByte();
            Calendar var3;
            (var3 = Calendar.getInstance()).set(1, var9);
            var3.set(2, var11 - 1);
            var3.set(5, var15);
            var3.set(10, 0);
            var3.set(12, 0);
            var3.set(13, 0);
            var3.set(14, 0);
            Calendar var10;
            (var10 = Calendar.getInstance()).set(10, 0);
            var10.set(12, 0);
            var10.set(13, 0);
            var10.set(14, 0);
            return var10.equals(var3);
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean loadGate(int var1) {
      smsPrefixes = null;
      smsNumbers = null;
      smsMessages = null;
      if (gateInfo == null) {
         SmsPaymentFlow var2 = this;
         RecordStore var3 = null;
         byte[] var4 = null;

         try {
            RecordEnumeration var12;
            if ((var3 = RecordStore.openRecordStore(var2.GATE_STORE, false)) != null && (var12 = var3.enumerateRecords(null, null, false)).hasNextElement()) {
               int var13 = var12.nextRecordId();
               var4 = var3.getRecord(var13);
            }

            if (var3 != null) {
               var3.closeRecordStore();
            }
         } catch (RecordStoreFullException var7) {
            var7.printStackTrace();
         } catch (RecordStoreNotFoundException var8) {
         } catch (RecordStoreException var9) {
         }

         if (var4 != null) {
            ByteArrayInputStream var14 = new ByteArrayInputStream(var4);
            DataInputStream var15 = new DataInputStream(var14);

            try {
               gateInfo = var15.readUTF();
               System.out.println("Lấy kênh từ rms");
            } catch (IOException var6) {
               var6.printStackTrace();
            }
         }

         if (gateInfo == null) {
            String var19;
            if ((var19 = Utils.loadConfigText("/ui/gate.bin")) != null) {
               int var21 = Utils.parseInt(Utils.getValue(var19, this.areaName));
               int var16 = Utils.getInt(var19, "default");
               if (var21 > 0) {
                  System.out.println("Lấy kênh từ file cục bộ " + var21 + " Thông tin");
                  gateInfo = Utils.substringBetween(var19, "gate" + var21 + ":", "gate" + var21 + "End");
               } else {
                  System.out.println("Lấy kênh mặc định từ file cục bộ " + var16 + " Thông tin");
                  gateInfo = Utils.substringBetween(var19, "gate" + var16 + ":", "gate" + var16 + "End");
               }

               boolean var10000 = true;
            } else {
               boolean var23 = false;
            }
         }
      }

      if (gateInfo == null) {
         System.out.println("Lấy thất bại");
         return false;
      } else {
         String var17 = Utils.getValue(gateInfo, "precmd");
         String var20 = Utils.getValue(gateInfo, "number");
         String var22 = Utils.getValue(gateInfo, "msg");
         int var5;
         if ((var5 = Utils.parseInt(Utils.getValue(gateInfo, "price"))) < 100) {
            var5 *= 100;
         }

         if (var1 >= 100) {
            var1 /= var5;
         } else {
            var1 = var1 * 100 / var5;
         }

         if (var1 > 0) {
            smsPrefixes = new String[var1];
            smsNumbers = new String[var1];
            smsMessages = new String[var1];

            for (int var11 = 0; var11 < smsPrefixes.length; var11++) {
               smsPrefixes[var11] = var17;
               smsNumbers[var11] = var20;
               smsMessages[var11] = var22;
            }
         }

         return true;
      }
   }

   private int getSentCount(String var1) {
      for (int var2 = 0; this.sentProductIds != null && var2 < this.sentProductIds.length; var2++) {
         if (this.sentProductIds[var2].equals(var1)) {
            return this.sentCounts[var2];
         }
      }

      return 0;
   }

   public final void handleKey(int var1) {
      this.showScrollHint = false;
      if (state == 13) {
         if (var1 == KeyMap.softLeftKey) {
            state = 9;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 12) {
         if (var1 == KeyMap.softRightKey) {
            this.resultListener.onResult(1, "Không có SIM");
            state = 0;
            return;
         }
      } else if (state == 1) {
         if (var1 == KeyMap.softLeftKey) {
            state = 3;
            this.lotteryRequest = new LotteryRequest(this);
            this.lotteryRequest.amount = 0;
            this.lotteryRequest.sendRequest(this.gameIdProp, this.channelIdProp, 0);
         } else if (var1 == KeyMap.softRightKey) {
            this.loadGate(0);
            this.sentCount = (byte)this.getSentCount(this.productId);
            this.loadHandselRecord();
            if (this.needBuyAmount > 0 && this.handselAmount > 0 && this.boughtCount == this.needBuyAmount && this.handselCount < this.handselAmount) {
               state = 13;
            } else {
               state = 6;
            }
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 2) {
         if (var1 == KeyMap.softLeftKey) {
            this.loadGate(0);
            this.sentCount = (byte)this.getSentCount(this.productId);
            state = 6;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 3) {
         if (var1 == KeyMap.softRightKey) {
            state = 4;
            return;
         }
      } else if (state == 4) {
         if (var1 == KeyMap.softRightKey) {
            this.scrollOffsets[state] = 0;
            state = 3;
            return;
         }

         if (var1 == KeyMap.softLeftKey) {
            this.loadGate(0);
            this.sentCount = (byte)this.getSentCount(this.productId);
            if (smsPrefixes != null) {
               this.loadHandselRecord();
               if (this.needBuyAmount > 0 && this.handselAmount > 0 && this.boughtCount == this.needBuyAmount && this.handselCount < this.handselAmount) {
                  state = 13;
                  return;
               }

               state = 6;
               return;
            }

            state = 11;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 5) {
         if (var1 == KeyMap.softLeftKey) {
            this.loadGate(0);
            this.sentCount = (byte)this.getSentCount(this.productId);
            if (smsPrefixes != null) {
               this.loadHandselRecord();
               if (this.needBuyAmount > 0 && this.handselAmount > 0 && this.boughtCount == this.needBuyAmount && this.handselCount < this.handselAmount) {
                  state = 13;
                  return;
               }

               state = 6;
               return;
            }

            state = 11;
            return;
         }

         if (var1 == KeyMap.softRightKey) {
            this.scrollOffsets[state] = 0;
            state = 3;
            this.retryCount++;
            this.lotteryRequest.sendRequest(this.gameIdProp, this.channelIdProp, 0);
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 6) {
         if (var1 == KeyMap.softLeftKey) {
            if (this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
               this.showScrollHint = true;
               return;
            }

            state = 7;
            if (this.payMode == 1) {
               SmsSendTask.send(smsPrefixes[this.sentCount] + this.smsContents[0], smsNumbers[this.sentCount], this);
               return;
            }

            SmsSendTask.send(this.smsContents[this.sentCount], smsNumbers[this.sentCount], this);
            return;
         }

         if (var1 == KeyMap.softRightKey) {
            state = 8;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 8) {
         if (var1 == KeyMap.softLeftKey) {
            if (this.sentCount > 0) {
               this.saveSentCount(this.productId, this.sentCount);
            }

            this.resultListener.onResult(1, "Người dùng hủy thanh toán");
            state = 0;
            return;
         }

         if (var1 == KeyMap.softRightKey) {
            state = 6;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 9) {
         if (var1 == KeyMap.softRightKey) {
            this.saveSentCount(this.productId, 0);
            if (this.needBuyAmount > 0 && this.handselAmount > 0) {
               if (this.boughtCount < this.needBuyAmount) {
                  this.boughtCount++;
               } else if (this.boughtCount >= this.needBuyAmount) {
                  if (this.handselCount < this.handselAmount) {
                     this.handselCount++;
                  }

                  if (this.handselCount >= this.handselAmount) {
                     this.boughtCount = 0;
                     this.handselCount = 0;
                  }
               }

               this.saveHandselRecord();
            }

            this.resultListener.onResult(0, "");
            state = 0;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
            return;
         }
      } else if (state == 10 || state == 11) {
         if (var1 == KeyMap.softRightKey) {
            state = 6;
            return;
         }

         if (var1 == 1) {
            if (this.scrollOffsets[state] > 0) {
               this.scrollOffsets[state]--;
               return;
            }
         } else if (var1 == 6 && this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            this.scrollOffsets[state]++;
         }
      }
   }

   public final void draw(Graphics var1) {
      var1.setFont(Utils.defaultFont);
      var1.setClip(0, 0, screenWidth, screenHeight);
      var1.setColor(4077616);
      var1.fillRect(0, 0, screenWidth, screenHeight);
      Graphics var3 = var1;
      SmsPaymentFlow var2 = this;
      if (this.topbarBack != null) {
         for (int var4 = 0; var4 < screenWidth; var4 += var2.topbarBack.getWidth()) {
            var3.drawImage(var2.topbarBack, var4, 0, 20);
         }
      } else {
         var1.setColor(15723495);
         var1.fillRect(0, 0, screenWidth, 29);
      }

      if (var2.topbarLogo != null) {
         var3.drawImage(var2.topbarLogo, 4, 0, 20);
      } else {
         Utils.drawOutlinedText(var3, "Thông tin thanh toán", screenWidth / 2, (29 - Utils.lineHeight) / 2, 14698514, 16777215, 17);
      }

      if (screenWidth > 128 && var2.topbarUrl != null) {
         var3.drawImage(var2.topbarUrl, screenWidth - 4, 0, 24);
      }

      if (var2.bottomBack != null) {
         for (int var7 = 0; var7 < screenWidth; var7 += var2.bottomBack.getWidth()) {
            var3.drawImage(var2.bottomBack, var7, screenHeight, 36);
         }
      } else {
         var3.setColor(15723495);
         var3.fillRect(0, screenHeight - 29, screenWidth, 29);
      }

      if (state == 13) {
         this.drawScrollText(
            var1,
            "Bạn đã tham gia hôm nay mua "
               + this.needBuyAmount
               + " Tặng "
               + this.handselAmount
               + " ưu đãi, trước đó đã mua "
               + this.boughtCount
               + " điểm tính phí, vì vậy điểm này miễn phí, nhấn Tiếp tục để nhận miễn phí.",
            true
         );
         var1.setColor(0);
         vh.drawString(var1, "Tiếp tục", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
      } else if (state == 12) {
         this.drawScrollText(var1, "Máy chưa lắp SIM hoặc không có sóng, không thể tiếp tục mua.", false);
         var1.setColor(0);
         vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
      } else if (state == 1) {
         this.drawScrollText(
            var1,
            "Thưa người chơi! Mỗi ngày bạn có một lần lấy ưu đãi điểm tính phí qua mạng (dùng chưa tới 1K dữ liệu), nhận thưởng ưu đãi khi mua điểm tính phí trong ngày; ngày lễ hoặc khi game mới ra mắt còn ưu đãi lớn hơn, đừng bỏ lỡ!",
            true
         );
         var1.setColor(0);
         vh.drawString(var1, "Tham gia", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
         vh.drawString(var1, "Bỏ quyền", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
      } else if (state == 2) {
         this.drawScrollText(var1, this.lotteryMsg, true);
         var1.setColor(0);
         vh.drawString(var1, "Tiếp tục", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
      } else if (state == 3) {
         this.drawScrollText(var1, "Đang lấy ưu đãi, xin chờ...", false);
         var1.setColor(0);
         vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
      } else if (state == 4) {
         this.drawScrollText(var1, this.msgLotteryPending, true);
         var1.setColor(0);
         vh.drawString(var1, "Tiếp", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
         vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
      } else if (state == 5) {
         if (this.retryCount > 0) {
            this.drawScrollText(var1, this.msgRetryFailed, true);
         } else {
            this.drawScrollText(var1, this.msgLotteryFailed, true);
         }

         var1.setColor(0);
         vh.drawString(var1, "Tiếp", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
         vh.drawString(var1, "Thử lại", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
      } else {
         if (state == 6) {
            if (smsMessages != null) {
               StringBuffer var10000;
               String var10001;
               if (this.sentCount == 0) {
                  var10000 = new StringBuffer(String.valueOf(this.productDesc))
                     .append("\n \n")
                     .append("Cần gửi ")
                     .append(smsMessages.length)
                     .append(" tin nhắn. Hãy bấm xác nhận tin thứ 1.\n")
                     .append(
                        this.needBuyAmount > 0 && this.handselAmount > 0
                           ? "Bạn đã tham gia hôm nay mua "
                              + this.needBuyAmount
                              + " Tặng "
                              + this.handselAmount
                              + " ưu đãi, mua thêm "
                              + (this.needBuyAmount - this.boughtCount)
                              + " điểm tính phí là được nhận miễn phí "
                              + this.handselAmount
                              + " điểm tính phí.\n"
                           : ""
                     );
                  var10001 = smsMessages[0];
               } else if (smsMessages.length - this.sentCount <= 0) {
                  state = 9;
                  var10000 = new StringBuffer(String.valueOf(this.productDesc)).append("\n \n").append("Đã gửi ").append(this.sentCount);
                  var10001 = "tin, thanh toán thành công";
               } else {
                  var10000 = new StringBuffer(String.valueOf(this.productDesc))
                     .append("\n \n")
                     .append("Đã gửi ")
                     .append(this.sentCount)
                     .append(" tin,còn cần gửi ")
                     .append(smsMessages.length - this.sentCount)
                     .append(" tin,hãy bấm xác nhận tin thứ ")
                     .append(this.sentCount + 1)
                     .append(" tin.\n");
                  var10001 = this.sentCount < smsMessages.length ? smsMessages[this.sentCount] : smsMessages[smsMessages.length - 1];
               }

               String var5 = var10000.append(var10001).toString();
               this.drawScrollText(var1, var5, true);
            }

            var1.setColor(0);
            if (this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
               var1.setColor(this.disabledColor);
            }

            vh.drawString(var1, "OK", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
            var1.setColor(0);
            vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
            if (this.showScrollHint && this.scrollHintLines != null) {
               var1.setColor(15393756);
               var1.fillRect(
                  2,
                  (screenHeight - this.scrollHintLines.length * Utils.lineHeight) / 2 - 2,
                  screenWidth - 4,
                  this.scrollHintLines.length * Utils.lineHeight + 4
               );
               var1.setColor(0);
               var1.drawRect(
                  2,
                  (screenHeight - this.scrollHintLines.length * Utils.lineHeight) / 2 - 2,
                  screenWidth - 4,
                  this.scrollHintLines.length * Utils.lineHeight + 4
               );
               var1.setColor(0);

               for (int var6 = 0; var6 < this.scrollHintLines.length; var6++) {
                  vh.drawString(
                     var1, this.scrollHintLines[var6], 4, (screenHeight - this.scrollHintLines.length * Utils.lineHeight) / 2 - 1 + var6 * Utils.lineHeight, 0
                  );
               }

               return;
            }
         } else {
            if (state == 8) {
               if (this.sentCount > 0) {
                  this.drawScrollText(var1, "Đã gửi " + this.sentCount + " tin, nếu thoát giữa chừng, lần sau có thể gửi tiếp các tin nhắn chưa gửi.", true);
               } else {
                  this.drawScrollText(var1, this.msgPaymentIncomplete, true);
               }

               var1.setColor(0);
               vh.drawString(var1, "Thoát", 4, screenHeight - (29 - Utils.lineHeight) / 2, 36);
               vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
               return;
            }

            if (state == 7) {
               this.drawScrollText(
                  var1, "Đang gửi tin nhắn‚xin đừng thoát để tránh bị trừ phí lặp lại!\nĐang gửi lần thứ " + (this.sentCount + 1) + " tin nhắn...", false
               );
               return;
            }

            if (state == 9) {
               this.drawScrollText(var1, this.msgSuccess, true);
               var1.setColor(0);
               vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
               return;
            }

            if (state == 10) {
               this.drawScrollText(var1, "Tin nhắn chưa gửi được‚hãy kiểm tra xem quyền gửi tin nhắn của trò chơi có bị chặn không.", true);
               var1.setColor(0);
               vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
               return;
            }

            if (state == 11) {
               this.drawScrollText(var1, "Gửi tin nhắn thất bại‚hãy kiểm tra chức năng tin nhắn của điện thoại đã cài đặt đúng chưa‚rồi trở về thử lại.", true);
               var1.setColor(0);
               vh.drawString(var1, "Trở về", screenWidth - 4, screenHeight - (29 - Utils.lineHeight) / 2, 40);
            }
         }
      }
   }

   private void drawScrollText(Graphics var1, String var2, boolean var3) {
      this.wrappedLines = Utils.splitAndWrap(var2, "\n", screenWidth - 4);
      var1.setColor(this.textColor);

      for (int var6 = this.scrollOffsets[state];
         this.wrappedLines != null && var6 < this.wrappedLines.length && var6 < this.scrollOffsets[state] + this.visibleLines;
         var6++
      ) {
         vh.drawString(var1, this.wrappedLines[var6], 2, this.topBarHeight + (var6 - this.scrollOffsets[state]) * Utils.lineHeight, 0);
      }

      if (var3) {
         int var10002 = screenWidth / 2;
         int var5 = screenHeight - 19;
         int var4 = var10002;
         if (this.scrollOffsets[state] == 0) {
            var1.setColor(this.disabledColor);
         } else {
            var1.setColor(0);
         }

         var1.fillTriangle(var4, var5, var4 - 5, var5 + 5, var4 + 5, var5 + 5);
         var10002 = screenWidth / 2;
         var5 = screenHeight - 9;
         var4 = var10002;
         if (this.wrappedLines != null && this.wrappedLines.length - this.scrollOffsets[state] > this.visibleLines) {
            var1.setColor(0);
         } else {
            var1.setColor(this.disabledColor);
         }

         var1.fillTriangle(var4, var5, var4 + 4, var5 - 4, var4 - 4, var5 - 4);
      }
   }

   private void loadHandselRecord() {
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         RecordEnumeration var3;
         if ((var1 = RecordStore.openRecordStore("handselRecord", false)) != null && (var3 = var1.enumerateRecords(null, null, false)).hasNextElement()) {
            int var9 = var3.nextRecordId();
            var2 = var1.getRecord(var9);
         }

         if (var1 != null) {
            var1.closeRecordStore();
         }
      } catch (RecordStoreFullException var4) {
         var4.printStackTrace();
      } catch (RecordStoreNotFoundException var5) {
      } catch (RecordStoreException var6) {
      }

      label40: {
         if (var2 != null) {
            ByteArrayInputStream var10 = new ByteArrayInputStream(var2);
            DataInputStream var11 = new DataInputStream(var10);

            try {
               if (isLotteryToday()) {
                  this.needBuyAmount = var11.readByte();
                  this.handselAmount = var11.readByte();
                  this.boughtCount = var11.readByte();
                  this.handselCount = var11.readByte();
               } else {
                  this.needBuyAmount = 0;
                  this.handselAmount = 0;
                  this.boughtCount = 0;
                  this.handselCount = 0;
               }
               break label40;
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }

         this.needBuyAmount = 0;
         this.handselAmount = 0;
         this.boughtCount = 0;
         this.handselCount = 0;
      }

      this.saveHandselRecord();
   }

   private final void loadSendTimes() {
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         RecordEnumeration var3;
         if ((var1 = RecordStore.openRecordStore(this.SENDTIMES_STORE, false)) != null && (var3 = var1.enumerateRecords(null, null, false)).hasNextElement()) {
            int var11 = var3.nextRecordId();
            var2 = var1.getRecord(var11);
         }

         if (var1 != null) {
            var1.closeRecordStore();
         }
      } catch (RecordStoreFullException var4) {
         var4.printStackTrace();
      } catch (RecordStoreNotFoundException var5) {
      } catch (RecordStoreException var6) {
      }

      if (var2 != null) {
         ByteArrayInputStream var12 = new ByteArrayInputStream(var2);
         DataInputStream var13 = new DataInputStream(var12);

         try {
            byte var9;
            if ((var9 = var13.readByte()) <= 0) {
               this.sentProductIds = null;
               this.sentCounts = null;
               return;
            }

            for (int var10 = 0; var10 < var9; var10++) {
               this.sentProductIds = Utils.append(this.sentProductIds, var13.readUTF());
               this.sentCounts = Utils.append(this.sentCounts, var13.readByte());
            }

            return;
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }
   }

   private void saveHandselRecord() {
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         DataOutputStream var4;
         (var4 = new DataOutputStream(var3)).writeByte(this.needBuyAmount);
         var4.writeByte(this.handselAmount);
         var4.writeByte(this.boughtCount);
         var4.writeByte(this.handselCount);
         var2 = var3.toByteArray();
         (var1 = RecordStore.openRecordStore("handselRecord", true)).closeRecordStore();
         RecordStore.deleteRecordStore("handselRecord");
         var1 = RecordStore.openRecordStore("handselRecord", true);
      } catch (RecordStoreFullException var6) {
         var6.printStackTrace();
      } catch (RecordStoreNotFoundException var7) {
      } catch (RecordStoreException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      try {
         var1.addRecord(var2, 0, var2.length);
         var1.closeRecordStore();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   private void saveSentCount(String var1, int var2) {
      boolean var3 = false;

      for (int var4 = 0; this.sentProductIds != null && var4 < this.sentProductIds.length; var4++) {
         if (this.sentProductIds[var4].equals(var1)) {
            this.sentCounts[var4] = (byte)var2;
            var3 = true;
         }
      }

      if (!var3) {
         this.sentProductIds = Utils.append(this.sentProductIds, var1);
         this.sentCounts = Utils.append(this.sentCounts, (byte)var2);
      }

      RecordStore var14 = null;
      byte[] var11 = null;

      try {
         ByteArrayOutputStream var12 = new ByteArrayOutputStream();
         DataOutputStream var13;
         (var13 = new DataOutputStream(var12)).writeByte(this.sentProductIds.length);

         for (int var5 = 0; var5 < this.sentProductIds.length; var5++) {
            var13.writeUTF(this.sentProductIds[var5]);
            var13.writeByte(this.sentCounts[var5]);
         }

         var11 = var12.toByteArray();
         (var14 = RecordStore.openRecordStore(this.SENDTIMES_STORE, true)).closeRecordStore();
         RecordStore.deleteRecordStore(this.SENDTIMES_STORE);
         var14 = RecordStore.openRecordStore(this.SENDTIMES_STORE, true);
      } catch (RecordStoreFullException var7) {
         var7.printStackTrace();
      } catch (RecordStoreNotFoundException var8) {
      } catch (RecordStoreException var9) {
         var9.printStackTrace();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      try {
         var14.addRecord(var11, 0, var11.length);
         var14.closeRecordStore();
      } catch (Exception var6) {
         var6.printStackTrace();
      }
   }

   public final void setScreenSize(int var1, int var2) {
      screenWidth = (short)var1;
      screenHeight = (short)var2;
      this.visibleLines = (short)((screenHeight - 62) / Utils.lineHeight);
      this.scrollHintLines = Utils.wrapText(this.scrollHint, screenWidth - 8);
   }

   public final void startPayment(String var1, String var2, String var3, String var4, String var5, ResultListener var6) {
      this.startPayments(new String[]{var1}, new String[]{var2}, new String[]{var3}, var4, var5, var6);
   }

   public final void startPayments(String[] var1, String[] var2, String[] var3, String var4, String var5, ResultListener var6) {
      this.resultListener = var6;
      if (this.detectArea()) {
         this.payMode = 2;
         this.productDesc = var4;
         this.smsContents = var1;
         smsNumbers = var2;
         smsMessages = var3;
         state = 6;
         this.productId = var5;
         this.sentCount = (byte)this.getSentCount(var5);
      } else {
         state = 12;
      }
   }

   static void setGateInfo(String var0) {
      gateInfo = var0;
   }

   static boolean loadGateOf(SmsPaymentFlow var0, int var1) {
      return var0.loadGate(var1);
   }

   static String productIdOf(SmsPaymentFlow var0) {
      return var0.productId;
   }

   static int sentCountOf(SmsPaymentFlow var0, String var1) {
      return var0.getSentCount(var1);
   }

   static void setSentCountOf(SmsPaymentFlow var0, byte var1) {
      var0.sentCount = var1;
   }

   static void saveGateInfo(SmsPaymentFlow var0) {
      var0 = var0;
      RecordStore var1 = null;
      byte[] var2 = null;

      try {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         new DataOutputStream(var3).writeUTF(gateInfo);
         var2 = var3.toByteArray();
         (var1 = RecordStore.openRecordStore(var0.GATE_STORE, true)).closeRecordStore();
         RecordStore.deleteRecordStore(var0.GATE_STORE);
         var1 = RecordStore.openRecordStore(var0.GATE_STORE, true);
      } catch (RecordStoreFullException var6) {
         var6.printStackTrace();
      } catch (RecordStoreNotFoundException var7) {
      } catch (RecordStoreException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      try {
         var1.addRecord(var2, 0, var2.length);
         var1.closeRecordStore();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   static String getGateInfo() {
      return gateInfo;
   }

   static void setLotteryWon(SmsPaymentFlow var0, boolean var1) {
      var0.lotteryWon = var1;
   }

   static void setLotteryMsg(SmsPaymentFlow var0, String var1) {
      var0.lotteryMsg = var1;
   }

   static boolean isLotteryWon(SmsPaymentFlow var0) {
      return var0.lotteryWon;
   }

   static void setNeedBuyAmount(SmsPaymentFlow var0, int var1) {
      var0.needBuyAmount = var1;
   }

   static void setHandselAmount(SmsPaymentFlow var0, int var1) {
      var0.handselAmount = var1;
   }

   static void resetBoughtCount(SmsPaymentFlow var0, int var1) {
      var0.boughtCount = 0;
   }

   static void resetHandselCount(SmsPaymentFlow var0, int var1) {
      var0.handselCount = 0;
   }

   static void saveLotteryDate(SmsPaymentFlow var0) {
      RecordStore var10 = null;
      byte[] var1 = null;

      try {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         DataOutputStream var3 = new DataOutputStream(var2);
         Calendar var4 = Calendar.getInstance();
         var3.writeShort(var4.get(1));
         var3.writeByte(var4.get(2) + 1);
         var3.writeByte(var4.get(5));
         var1 = var2.toByteArray();
         (var10 = RecordStore.openRecordStore("lotteryDate", true)).closeRecordStore();
         RecordStore.deleteRecordStore("lotteryDate");
         var10 = RecordStore.openRecordStore("lotteryDate", true);
      } catch (RecordStoreFullException var6) {
         var6.printStackTrace();
      } catch (RecordStoreNotFoundException var7) {
      } catch (RecordStoreException var8) {
         var8.printStackTrace();
      } catch (IOException var9) {
         var9.printStackTrace();
      }

      try {
         var10.addRecord(var1, 0, var1.length);
         var10.closeRecordStore();
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   static void saveHandselRecordOf(SmsPaymentFlow var0) {
      var0.saveHandselRecord();
   }

   static void setState(byte var0) {
      state = var0;
   }
}
