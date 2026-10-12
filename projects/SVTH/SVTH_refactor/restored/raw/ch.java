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

public final class ch implements cx {
  private byte a_B;
  private String a_String = null;
  private static short a_S = 240;
  private static short b_S = 320;
  private static String[] a_AString;
  private static String[] b_AString;
  private static String[] c_AString;
  private byte b_B;
  private String b_String;
  private String c_String;
  private String d_String;
  private static byte c_B;
  private boolean a_Z;
  private String e_String;
  private String f_String = "Lấy ưu đãi chưa xong, nhấn phím mềm phải để quay lại bốc thăm, nhấn phím mềm trái để bỏ bốc thăm và tiếp tục mua.";
  private String g_String = "Lấy ưu đãi qua mạng thất bại, nhấn phím mềm phải để thử lại, nhấn phím mềm trái để bỏ ưu đãi và tiếp tục mua.";
  private String h_String = "Thử lại thất bại, có thể quyền mạng tạm bị chặn, lần sau vào game sẽ khôi phục, nhấn phím mềm trái để bỏ ưu đãi và tiếp tục mua.";
  private String i_String = "Bạn chưa hoàn tất thanh toán‚xin đừng thoát. Bấm phím mềm phải để trở về tiếp tục mua‚bấm phím mềm trái để thoát mua hàng.";
  private String j_String = "Chúc mừng bạn mua thành công, cảm ơn bạn đã ủng hộ game Tỉnh Trung Nguyệt! Nhấn phím mềm phải để trở về tận hưởng thêm nhiều nội dung hấp dẫn.";
  private cx a_cx = null;
  private String[] d_AString;
  private byte[] a_AB;
  private short c_S;
  private String[] e_AString;
  private int a_I;
  private f a_f;
  private int b_I = 16777215;
  private int c_I = 0;
  private int d_I = 7368816;
  private boolean b_Z;
  private String k_String = "Hãy cuộn xuống đọc hết rồi mới bấm OK";
  private String[] f_AString;
  private String l_String;
  private String[] g_AString;
  private byte[] b_AB;
  private String m_String = "onegateinfo";
  private String n_String = "sendtimes";
  private Image a_Image;
  private Image b_Image;
  private Image c_Image;
  private Image d_Image;
  private byte d_B;
  private static String o_String;
  private static String[][] a_AAString = new String[][]{
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
  private String p_String;
  private String q_String;
  private int e_I;
  private int f_I;
  private int g_I;
  private int h_I;

  public ch(MIDlet var1) {
    this.c_String = var1.getAppProperty("cid");
    var1.getAppProperty("sid");
    this.b_String = var1.getAppProperty("gid");
    this.d_String = var1.getAppProperty("pid");
    if (this.c_String == null || this.c_String.equals("")) {
      this.c_String = "18";
    }

    if (this.b_String == null || this.b_String.equals("")) {
      this.b_String = "100";
    }

    if (this.d_String == null || this.d_String.equals("")) {
      this.d_String = "01";
    }

    this.a_AB = new byte[14];
    this.c_S = (short)((b_S - (cg.b_S + 4 << 1)) / cg.b_S);
    this.f_AString = cg.a(this.k_String, a_S - 8);
    this.b_V();
    this.a_Image = cg.a_Image("/ui/topbar_logo.gf");
    this.b_Image = cg.a_Image("/ui/topbar_back.gf");
    this.c_Image = cg.a_Image("/ui/topbar_url.gf");
    this.d_Image = cg.a_Image("/ui/bottom_back.gf");
    if (this.b_Image != null) {
      this.d_B = (byte)(this.b_Image.getHeight() + 2);
    } else {
      this.d_B = 31;
    }

    if (this.c_Image == null) {
      this.j_String = "Chúc mừng bạn mua thành công, cảm ơn bạn đã ủng hộ game của chúng tôi! Nhấn phím mềm phải để trở về tận hưởng thêm nhiều nội dung hấp dẫn.";
    }
  }

  public final void a(int var1, String var2) {
    if (var1 == 0) {
      this.b_B++;
      if (this.b_B < c_AString.length) {
        c_B = 6;
        this.a_AB[6] = 0;
      } else {
        c_B = 9;
      }
    } else if (var1 == 2) {
      c_B = 10;
    } else {
      c_B = 11;
    }
  }

  private boolean a_Z() {
    try {
      this.p_String = System.getProperty("wireless.messaging.sms.smsc");
      System.out.println("Lấy số trung tâm tin nhắn " + this.p_String);
      if (this.p_String != null) {
        for (int var1 = 0; var1 < a_AAString.length; var1++) {
          for (int var2 = 1; var2 < a_AAString[var1].length; var2++) {
            if (this.p_String.indexOf(a_AAString[var1][var2]) != -1) {
              this.q_String = a_AAString[var1][0];
              break;
            }
          }

          if (this.q_String != null) {
            break;
          }
        }
      } else {
        this.q_String = "Telecom";
      }

      if (this.q_String == null) {
        this.q_String = "Không rõ";
      }

      System.out.println("Xác định vùng của máy:" + this.q_String);
      return true;
    } catch (Exception var3) {
      System.out.println("Chưa lắp thẻ SIM hoặc điện thoại đang ngoại tuyến");
      return false;
    }
  }

  private static boolean b_Z() {
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

  private boolean a_Z(int var1) {
    a_AString = null;
    b_AString = null;
    c_AString = null;
    if (o_String == null) {
      ch var2 = this;
      RecordStore var3 = null;
      byte[] var4 = null;

      try {
        RecordEnumeration var12;
        if ((var3 = RecordStore.openRecordStore(var2.m_String, false)) != null && (var12 = var3.enumerateRecords(null, null, false)).hasNextElement()) {
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
          o_String = var15.readUTF();
          System.out.println("Lấy kênh từ rms");
        } catch (IOException var6) {
          var6.printStackTrace();
        }
      }

      if (o_String == null) {
        String var19;
        if ((var19 = cg.a_String("/ui/gate.bin")) != null) {
          int var21 = cg.a_I(cg.a_String(var19, this.q_String));
          int var16 = cg.a_I(var19, "default");
          if (var21 > 0) {
            System.out.println("Lấy kênh từ file cục bộ " + var21 + " Thông tin");
            o_String = cg.a(var19, "gate" + var21 + ":", "gate" + var21 + "End");
          } else {
            System.out.println("Lấy kênh mặc định từ file cục bộ " + var16 + " Thông tin");
            o_String = cg.a(var19, "gate" + var16 + ":", "gate" + var16 + "End");
          }

          boolean var10000 = true;
        } else {
          boolean var23 = false;
        }
      }
    }

    if (o_String == null) {
      System.out.println("Lấy thất bại");
      return false;
    } else {
      String var17 = cg.a_String(o_String, "precmd");
      String var20 = cg.a_String(o_String, "number");
      String var22 = cg.a_String(o_String, "msg");
      int var5;
      if ((var5 = cg.a_I(cg.a_String(o_String, "price"))) < 100) {
        var5 *= 100;
      }

      if (var1 >= 100) {
        var1 /= var5;
      } else {
        var1 = var1 * 100 / var5;
      }

      if (var1 > 0) {
        a_AString = new String[var1];
        b_AString = new String[var1];
        c_AString = new String[var1];

        for (int var11 = 0; var11 < a_AString.length; var11++) {
          a_AString[var11] = var17;
          b_AString[var11] = var20;
          c_AString[var11] = var22;
        }
      }

      return true;
    }
  }

  private int a_I(String var1) {
    for (int var2 = 0; this.g_AString != null && var2 < this.g_AString.length; var2++) {
      if (this.g_AString[var2].equals(var1)) {
        return this.b_AB[var2];
      }
    }

    return 0;
  }

  public final void a_V(int var1) {
    this.b_Z = false;
    if (c_B == 13) {
      if (var1 == b.a_S) {
        c_B = 9;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 12) {
      if (var1 == b.b_S) {
        this.a_cx.a(1, "Không có SIM");
        c_B = 0;
        return;
      }
    } else if (c_B == 1) {
      if (var1 == b.a_S) {
        c_B = 3;
        this.a_f = new f(this);
        this.a_f.a_I = 0;
        this.a_f.a(this.b_String, this.c_String, 0);
      } else if (var1 == b.b_S) {
        this.a_Z(0);
        this.b_B = (byte)this.a_I(this.l_String);
        this.a_V();
        if (this.e_I > 0 && this.f_I > 0 && this.g_I == this.e_I && this.h_I < this.f_I) {
          c_B = 13;
        } else {
          c_B = 6;
        }
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 2) {
      if (var1 == b.a_S) {
        this.a_Z(0);
        this.b_B = (byte)this.a_I(this.l_String);
        c_B = 6;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 3) {
      if (var1 == b.b_S) {
        c_B = 4;
        return;
      }
    } else if (c_B == 4) {
      if (var1 == b.b_S) {
        this.a_AB[c_B] = 0;
        c_B = 3;
        return;
      }

      if (var1 == b.a_S) {
        this.a_Z(0);
        this.b_B = (byte)this.a_I(this.l_String);
        if (a_AString != null) {
          this.a_V();
          if (this.e_I > 0 && this.f_I > 0 && this.g_I == this.e_I && this.h_I < this.f_I) {
            c_B = 13;
            return;
          }

          c_B = 6;
          return;
        }

        c_B = 11;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 5) {
      if (var1 == b.a_S) {
        this.a_Z(0);
        this.b_B = (byte)this.a_I(this.l_String);
        if (a_AString != null) {
          this.a_V();
          if (this.e_I > 0 && this.f_I > 0 && this.g_I == this.e_I && this.h_I < this.f_I) {
            c_B = 13;
            return;
          }

          c_B = 6;
          return;
        }

        c_B = 11;
        return;
      }

      if (var1 == b.b_S) {
        this.a_AB[c_B] = 0;
        c_B = 3;
        this.a_I++;
        this.a_f.a(this.b_String, this.c_String, 0);
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 6) {
      if (var1 == b.a_S) {
        if (this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
          this.b_Z = true;
          return;
        }

        c_B = 7;
        if (this.a_B == 1) {
          bh.a(a_AString[this.b_B] + this.d_AString[0], b_AString[this.b_B], this);
          return;
        }

        bh.a(this.d_AString[this.b_B], b_AString[this.b_B], this);
        return;
      }

      if (var1 == b.b_S) {
        c_B = 8;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 8) {
      if (var1 == b.a_S) {
        if (this.b_B > 0) {
          this.a(this.l_String, this.b_B);
        }

        this.a_cx.a(1, "Người dùng hủy thanh toán");
        c_B = 0;
        return;
      }

      if (var1 == b.b_S) {
        c_B = 6;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 9) {
      if (var1 == b.b_S) {
        this.a(this.l_String, 0);
        if (this.e_I > 0 && this.f_I > 0) {
          if (this.g_I < this.e_I) {
            this.g_I++;
          } else if (this.g_I >= this.e_I) {
            if (this.h_I < this.f_I) {
              this.h_I++;
            }

            if (this.h_I >= this.f_I) {
              this.g_I = 0;
              this.h_I = 0;
            }
          }

          this.c();
        }

        this.a_cx.a(0, "");
        c_B = 0;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
        return;
      }
    } else if (c_B == 10 || c_B == 11) {
      if (var1 == b.b_S) {
        c_B = 6;
        return;
      }

      if (var1 == 1) {
        if (this.a_AB[c_B] > 0) {
          this.a_AB[c_B]--;
          return;
        }
      } else if (var1 == 6 && this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        this.a_AB[c_B]++;
      }
    }
  }

  public final void a(Graphics var1) {
    var1.setFont(cg.a_Font);
    var1.setClip(0, 0, a_S, b_S);
    var1.setColor(4077616);
    var1.fillRect(0, 0, a_S, b_S);
    Graphics var3 = var1;
    ch var2 = this;
    if (this.b_Image != null) {
      for (int var4 = 0; var4 < a_S; var4 += var2.b_Image.getWidth()) {
        var3.drawImage(var2.b_Image, var4, 0, 20);
      }
    } else {
      var1.setColor(15723495);
      var1.fillRect(0, 0, a_S, 29);
    }

    if (var2.a_Image != null) {
      var3.drawImage(var2.a_Image, 4, 0, 20);
    } else {
      cg.a(var3, "Thông tin thanh toán", a_S / 2, (29 - cg.b_S) / 2, 14698514, 16777215, 17);
    }

    if (a_S > 128 && var2.c_Image != null) {
      var3.drawImage(var2.c_Image, a_S - 4, 0, 24);
    }

    if (var2.d_Image != null) {
      for (int var7 = 0; var7 < a_S; var7 += var2.d_Image.getWidth()) {
        var3.drawImage(var2.d_Image, var7, b_S, 36);
      }
    } else {
      var3.setColor(15723495);
      var3.fillRect(0, b_S - 29, a_S, 29);
    }

    if (c_B == 13) {
      this.a(
        var1,
        "Bạn đã tham gia hôm nay mua "
          + this.e_I
          + " Tặng "
          + this.f_I
          + " ưu đãi, trước đó đã mua "
          + this.g_I
          + " điểm tính phí, vì vậy điểm này miễn phí, nhấn Tiếp tục để nhận miễn phí.",
        true
      );
      var1.setColor(0);
      vh.drawString(var1, "Tiếp tục", 4, b_S - (29 - cg.b_S) / 2, 36);
    } else if (c_B == 12) {
      this.a(var1, "Máy chưa lắp SIM hoặc không có sóng, không thể tiếp tục mua.", false);
      var1.setColor(0);
      vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
    } else if (c_B == 1) {
      this.a(
        var1,
        "Thưa người chơi! Mỗi ngày bạn có một lần lấy ưu đãi điểm tính phí qua mạng (dùng chưa tới 1K dữ liệu), nhận thưởng ưu đãi khi mua điểm tính phí trong ngày; ngày lễ hoặc khi game mới ra mắt còn ưu đãi lớn hơn, đừng bỏ lỡ!",
        true
      );
      var1.setColor(0);
      vh.drawString(var1, "Tham gia", 4, b_S - (29 - cg.b_S) / 2, 36);
      vh.drawString(var1, "Bỏ quyền", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
    } else if (c_B == 2) {
      this.a(var1, this.e_String, true);
      var1.setColor(0);
      vh.drawString(var1, "Tiếp tục", 4, b_S - (29 - cg.b_S) / 2, 36);
    } else if (c_B == 3) {
      this.a(var1, "Đang lấy ưu đãi, xin chờ...", false);
      var1.setColor(0);
      vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
    } else if (c_B == 4) {
      this.a(var1, this.f_String, true);
      var1.setColor(0);
      vh.drawString(var1, "Tiếp", 4, b_S - (29 - cg.b_S) / 2, 36);
      vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
    } else if (c_B == 5) {
      if (this.a_I > 0) {
        this.a(var1, this.h_String, true);
      } else {
        this.a(var1, this.g_String, true);
      }

      var1.setColor(0);
      vh.drawString(var1, "Tiếp", 4, b_S - (29 - cg.b_S) / 2, 36);
      vh.drawString(var1, "Thử lại", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
    } else {
      if (c_B == 6) {
        if (c_AString != null) {
          StringBuffer var10000;
          String var10001;
          if (this.b_B == 0) {
            var10000 = new StringBuffer(String.valueOf(this.a_String))
              .append("\n \n")
              .append("Cần gửi ")
              .append(c_AString.length)
              .append(" tin nhắn. Hãy bấm xác nhận tin thứ 1.\n")
              .append(
                this.e_I > 0 && this.f_I > 0
                  ? "Bạn đã tham gia hôm nay mua "
                    + this.e_I
                    + " Tặng "
                    + this.f_I
                    + " ưu đãi, mua thêm "
                    + (this.e_I - this.g_I)
                    + " điểm tính phí là được nhận miễn phí "
                    + this.f_I
                    + " điểm tính phí.\n"
                  : ""
              );
            var10001 = c_AString[0];
          } else if (c_AString.length - this.b_B <= 0) {
            c_B = 9;
            var10000 = new StringBuffer(String.valueOf(this.a_String)).append("\n \n").append("Đã gửi ").append(this.b_B);
            var10001 = "tin, thanh toán thành công";
          } else {
            var10000 = new StringBuffer(String.valueOf(this.a_String))
              .append("\n \n")
              .append("Đã gửi ")
              .append(this.b_B)
              .append(" tin,còn cần gửi ")
              .append(c_AString.length - this.b_B)
              .append(" tin,hãy bấm xác nhận tin thứ ")
              .append(this.b_B + 1)
              .append(" tin.\n");
            var10001 = this.b_B < c_AString.length ? c_AString[this.b_B] : c_AString[c_AString.length - 1];
          }

          String var5 = var10000.append(var10001).toString();
          this.a(var1, var5, true);
        }

        var1.setColor(0);
        if (this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
          var1.setColor(this.d_I);
        }

        vh.drawString(var1, "OK", 4, b_S - (29 - cg.b_S) / 2, 36);
        var1.setColor(0);
        vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
        if (this.b_Z && this.f_AString != null) {
          var1.setColor(15393756);
          var1.fillRect(2, (b_S - this.f_AString.length * cg.b_S) / 2 - 2, a_S - 4, this.f_AString.length * cg.b_S + 4);
          var1.setColor(0);
          var1.drawRect(2, (b_S - this.f_AString.length * cg.b_S) / 2 - 2, a_S - 4, this.f_AString.length * cg.b_S + 4);
          var1.setColor(0);

          for (int var6 = 0; var6 < this.f_AString.length; var6++) {
            vh.drawString(var1, this.f_AString[var6], 4, (b_S - this.f_AString.length * cg.b_S) / 2 - 1 + var6 * cg.b_S, 0);
          }

          return;
        }
      } else {
        if (c_B == 8) {
          if (this.b_B > 0) {
            this.a(var1, "Đã gửi " + this.b_B + " tin, nếu thoát giữa chừng, lần sau có thể gửi tiếp các tin nhắn chưa gửi.", true);
          } else {
            this.a(var1, this.i_String, true);
          }

          var1.setColor(0);
          vh.drawString(var1, "Thoát", 4, b_S - (29 - cg.b_S) / 2, 36);
          vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
          return;
        }

        if (c_B == 7) {
          this.a(var1, "Đang gửi tin nhắn‚xin đừng thoát để tránh bị trừ phí lặp lại!\nĐang gửi lần thứ " + (this.b_B + 1) + " tin nhắn...", false);
          return;
        }

        if (c_B == 9) {
          this.a(var1, this.j_String, true);
          var1.setColor(0);
          vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
          return;
        }

        if (c_B == 10) {
          this.a(var1, "Tin nhắn chưa gửi được‚hãy kiểm tra xem quyền gửi tin nhắn của trò chơi có bị chặn không.", true);
          var1.setColor(0);
          vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
          return;
        }

        if (c_B == 11) {
          this.a(var1, "Gửi tin nhắn thất bại‚hãy kiểm tra chức năng tin nhắn của điện thoại đã cài đặt đúng chưa‚rồi trở về thử lại.", true);
          var1.setColor(0);
          vh.drawString(var1, "Trở về", a_S - 4, b_S - (29 - cg.b_S) / 2, 40);
        }
      }
    }
  }

  private void a(Graphics var1, String var2, boolean var3) {
    this.e_AString = cg.a(var2, "\n", a_S - 4);
    var1.setColor(this.b_I);

    for (int var6 = this.a_AB[c_B]; this.e_AString != null && var6 < this.e_AString.length && var6 < this.a_AB[c_B] + this.c_S; var6++) {
      vh.drawString(var1, this.e_AString[var6], 2, this.d_B + (var6 - this.a_AB[c_B]) * cg.b_S, 0);
    }

    if (var3) {
      int var10002 = a_S / 2;
      int var5 = b_S - 19;
      int var4 = var10002;
      if (this.a_AB[c_B] == 0) {
        var1.setColor(this.d_I);
      } else {
        var1.setColor(0);
      }

      var1.fillTriangle(var4, var5, var4 - 5, var5 + 5, var4 + 5, var5 + 5);
      var10002 = a_S / 2;
      var5 = b_S - 9;
      var4 = var10002;
      if (this.e_AString != null && this.e_AString.length - this.a_AB[c_B] > this.c_S) {
        var1.setColor(0);
      } else {
        var1.setColor(this.d_I);
      }

      var1.fillTriangle(var4, var5, var4 + 4, var5 - 4, var4 - 4, var5 - 4);
    }
  }

  private void a_V() {
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
          if (b_Z()) {
            this.e_I = var11.readByte();
            this.f_I = var11.readByte();
            this.g_I = var11.readByte();
            this.h_I = var11.readByte();
          } else {
            this.e_I = 0;
            this.f_I = 0;
            this.g_I = 0;
            this.h_I = 0;
          }
          break label40;
        } catch (Exception var7) {
          var7.printStackTrace();
        }
      }

      this.e_I = 0;
      this.f_I = 0;
      this.g_I = 0;
      this.h_I = 0;
    }

    this.c();
  }

  private final void b_V() {
    RecordStore var1 = null;
    byte[] var2 = null;

    try {
      RecordEnumeration var3;
      if ((var1 = RecordStore.openRecordStore(this.n_String, false)) != null && (var3 = var1.enumerateRecords(null, null, false)).hasNextElement()) {
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
          this.g_AString = null;
          this.b_AB = null;
          return;
        }

        for (int var10 = 0; var10 < var9; var10++) {
          this.g_AString = cg.a(this.g_AString, var13.readUTF());
          this.b_AB = cg.a_AB(this.b_AB, var13.readByte());
        }

        return;
      } catch (IOException var7) {
        var7.printStackTrace();
      }
    }
  }

  private void c() {
    RecordStore var1 = null;
    byte[] var2 = null;

    try {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      DataOutputStream var4;
      (var4 = new DataOutputStream(var3)).writeByte(this.e_I);
      var4.writeByte(this.f_I);
      var4.writeByte(this.g_I);
      var4.writeByte(this.h_I);
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

  private void a(String var1, int var2) {
    boolean var3 = false;

    for (int var4 = 0; this.g_AString != null && var4 < this.g_AString.length; var4++) {
      if (this.g_AString[var4].equals(var1)) {
        this.b_AB[var4] = (byte)var2;
        var3 = true;
      }
    }

    if (!var3) {
      this.g_AString = cg.a(this.g_AString, var1);
      this.b_AB = cg.a_AB(this.b_AB, (byte)var2);
    }

    RecordStore var14 = null;
    byte[] var11 = null;

    try {
      ByteArrayOutputStream var12 = new ByteArrayOutputStream();
      DataOutputStream var13;
      (var13 = new DataOutputStream(var12)).writeByte(this.g_AString.length);

      for (int var5 = 0; var5 < this.g_AString.length; var5++) {
        var13.writeUTF(this.g_AString[var5]);
        var13.writeByte(this.b_AB[var5]);
      }

      var11 = var12.toByteArray();
      (var14 = RecordStore.openRecordStore(this.n_String, true)).closeRecordStore();
      RecordStore.deleteRecordStore(this.n_String);
      var14 = RecordStore.openRecordStore(this.n_String, true);
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

  public final void a(int var1, int var2) {
    a_S = (short)var1;
    b_S = (short)var2;
    this.c_S = (short)((b_S - 62) / cg.b_S);
    this.f_AString = cg.a(this.k_String, a_S - 8);
  }

  public final void a(String var1, String var2, String var3, String var4, String var5, cx var6) {
    this.a(new String[]{var1}, new String[]{var2}, new String[]{var3}, var4, var5, var6);
  }

  public final void a(String[] var1, String[] var2, String[] var3, String var4, String var5, cx var6) {
    this.a_cx = var6;
    if (this.a_Z()) {
      this.a_B = 2;
      this.a_String = var4;
      this.d_AString = var1;
      b_AString = var2;
      c_AString = var3;
      c_B = 6;
      this.l_String = var5;
      this.b_B = (byte)this.a_I(var5);
    } else {
      c_B = 12;
    }
  }

  static void a_V(String var0) {
    o_String = var0;
  }

  static boolean a_Z(ch var0, int var1) {
    return var0.a_Z(var1);
  }

  static String a_String(ch var0) {
    return var0.l_String;
  }

  static int a_I(ch var0, String var1) {
    return var0.a_I(var1);
  }

  static void a(ch var0, byte var1) {
    var0.b_B = var1;
  }

  static void a_V(ch var0) {
    var0 = var0;
    RecordStore var1 = null;
    byte[] var2 = null;

    try {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      new DataOutputStream(var3).writeUTF(o_String);
      var2 = var3.toByteArray();
      (var1 = RecordStore.openRecordStore(var0.m_String, true)).closeRecordStore();
      RecordStore.deleteRecordStore(var0.m_String);
      var1 = RecordStore.openRecordStore(var0.m_String, true);
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

  static String a_String() {
    return o_String;
  }

  static void a(ch var0, boolean var1) {
    var0.a_Z = var1;
  }

  static void a_V(ch var0, String var1) {
    var0.e_String = var1;
  }

  static boolean a_Z(ch var0) {
    return var0.a_Z;
  }

  static void a_V(ch var0, int var1) {
    var0.e_I = var1;
  }

  static void b(ch var0, int var1) {
    var0.f_I = var1;
  }

  static void c(ch var0, int var1) {
    var0.g_I = 0;
  }

  static void d(ch var0, int var1) {
    var0.h_I = 0;
  }

  static void b(ch var0) {
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

  static void c(ch var0) {
    var0.c();
  }

  static void a(byte var0) {
    c_B = var0;
  }
}
