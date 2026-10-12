import java.io.InputStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;

public final class SmsFeeConfig {
   private static Hashtable cpidTable = new Hashtable();
   private static String[][] smsCodes = new String[2][];
   private static String[][] smsDests = new String[2][];
   private static String[][] doCodes = new String[1][];
   private static String[][] doDests = new String[1][];
   private static String channelFirst = "Fr";
   private static String channelSecond = "Sec";
   private static String terminal = "N7610";
   private static String freeCode = "299";
   private static String freeDest = "10661700299";
   private static String interChannel = "Idx";
   private static String qq = "0";
   private static int[] logoLinkReco = new int[3];
   private static int feeMode = 0;
   private static int[][] smsCodeUseCounts = new int[2][];

   public SmsFeeConfig(MIDlet var1) {
      this.loadJadProperties(var1);
   }

   private static final String[] split(String var0, String var1) {
      if (var0 == null) {
         System.out.println(" split. aStr == null");
         return null;
      } else if (var0.length() > 0 && var1 != null) {
         if (var1.length() <= 0) {
            return new String[]{var0};
         } else {
            String[] var2 = null;
            String var3 = var0;
            var1 = var1;
            int[] var4 = new int[var0.length()];
            int[] var5 = new int[var0.length()];
            int var6 = 0;
            boolean var7 = false;
            int var8 = 0;

            do {
               boolean var10 = false;
               int var9;
               if ((var9 = var8 + var1.length()) <= var3.length() && var3.substring(var8, var9).equals(var1)) {
                  var10 = true;
               }

               if (var10) {
                  if (var7) {
                     var7 = false;
                     var5[var6++] = var8;
                  }

                  var8 = var9;
               } else {
                  if (!var7) {
                     var7 = true;
                     var4[var6] = var8;
                  }

                  var8++;
               }
            } while (var8 < var3.length());

            if (var7) {
               var5[var6++] = var3.length();
            }

            if (var6 > 0) {
               var2 = new String[var6];

               for (int var12 = 0; var12 < var6; var12++) {
                  var2[var12] = var0.substring(var4[var12], var5[var12]);
               }
            }

            return var2;
         }
      } else {
         return new String[]{var0};
      }
   }

   private static final String decryptJadValue(String var0) {
      if (var0 != null && var0.length() >= 32) {
         try {
            IdeaCipher var1;
            (var1 = new IdeaCipher()).setKey("a4s3d5f1");
            String var5 = var1.decryptHex(var0.substring(0, var0.length() - 32));
            int var2 = 0;

            for (int var3 = 0; var3 < var5.length(); var3++) {
               if ((var5.charAt(var3) < 'a' || var5.charAt(var3) > 'z')
                  && (var5.charAt(var3) < 'A' || var5.charAt(var3) > 'Z')
                  && (var5.charAt(var3) < '0' || var5.charAt(var3) > '9')
                  && var5.charAt(var3) != ';'
                  && var5.charAt(var3) != ',') {
                  var2 = var3;
                  break;
               }
            }

            String var6 = var5.substring(0, var2);
            return new Md5Text().md5Hex(var6).equals(var0.substring(var0.length() - 32)) ? var6 : null;
         } catch (Exception var4) {
            var4.printStackTrace();
            return null;
         }
      } else {
         System.out.println("des. aStr ==null or aStr length < 32");
         return null;
      }
   }

   private static int parseInt(String var0) {
      int var1 = 0;

      try {
         var1 = Integer.parseInt(var0);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return var1;
   }

   private static String utf8String(byte[] var0) {
      String var1;
      try {
         var1 = new String(var0, "utf-8");
      } catch (Exception var3) {
         try {
            var1 = new String(var0, "UTF-8");
         } catch (Exception var2) {
            var1 = new String(var0);
         }
      }

      return var1;
   }

   private static String[] getCpidEntry(int var0, String var1) {
      try {
         if (cpidTable.isEmpty()) {
            System.out.println("is empty");
         }

         if (cpidTable.containsKey(var1)) {
            Vector var3 = (Vector)cpidTable.get(var1);
            return var0 >= 0 && var0 < var3.size() ? (String[])var3.elementAt(var0) : null;
         } else {
            return null;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   private void loadCpidList() {
      InputStream var1;
      if ((var1 = new Object().getClass().getResourceAsStream("/cpid.txt")) != null) {
         try {
            byte[] var2 = new byte[var1.available()];
            var1.read(var2);
            String var16 = utf8String(var2);
            System.out.println("cpidListStr=" + var16);
            String[] var17 = split(var16, "\n");
            System.out.println("splitArray.length=" + (var17 != null ? var17.length : 0));

            for (int var7 = 0; var7 < (var17 != null ? var17.length : 0); var7++) {
               String[] var3 = split(var17[var7], ",");
               System.out.println("lineArray=" + var3.length);
               System.out.println("lineArray[0]=" + var3[0]);
               System.out.println("lineArray[1]=" + var3[1]);
               System.out.println("lineArray[2]=" + var3[2].length() + "," + var3[2].trim().length());
               String var4 = var3[0].trim();
               String var5 = decryptJadValue(var3[1].trim());
               String var6 = decryptJadValue(var3[2].trim());
               var3 = new String[]{var5, var6};
               System.out.println("cpid=" + var4 + ",code=" + var5 + ",dest=" + var6);
               if (!cpidTable.containsKey(var4)) {
                  Vector var19;
                  (var19 = new Vector()).addElement(var3);
                  cpidTable.put(var4, var19);
                  System.out.println("put.cpid=" + var4 + ",CodeDestV.size()=" + var19.size());
               } else {
                  Vector var20 = (Vector)cpidTable.get(var4);
                  System.out.println("old.size()=" + var20.size());
                  var20.addElement(var3);
                  System.out.println("old.size.2()=" + var20.size());
                  cpidTable.remove(var4);
                  cpidTable.put(var4, var20);
                  System.out.println("put.cpid=" + var4 + ",old.size()=" + var20.size());
                  System.out.println("cpid=" + var4 + ",cpidHashtable=" + ((Vector)cpidTable.get(var4)).size());
               }
            }

            return;
         } catch (Exception var14) {
            var14.printStackTrace();
         } finally {
            try {
               if (var1 != null) {
                  var1.close();
               }
            } catch (Exception var13) {
            }
         }
      }
   }

   private void loadJadProperties(MIDlet var1) {
      try {
         String var14;
         if ((var14 = var1.getAppProperty("ChannelFirst")) != null) {
            channelFirst = var14;
         }

         if ((var14 = var1.getAppProperty("ChannelSecond")) != null) {
            channelSecond = var14;
         }

         if ((var14 = var1.getAppProperty("InterChannel")) != null) {
            interChannel = var14;
         }

         if ((var14 = var1.getAppProperty("Term")) != null) {
            terminal = var14;
         }

         if ((var14 = var1.getAppProperty("QQ")) != null) {
            qq = var14;
         }

         var1.getAppProperty("GameCenterName");
         var1.getAppProperty("GameCenterUrl");
         if ((var14 = decryptJadValue(var1.getAppProperty("FreeCode"))) != null) {
            freeCode = var14;
         }

         if ((var14 = decryptJadValue(var1.getAppProperty("FreeDest"))) != null) {
            freeDest = var14;
         }

         var14 = decryptJadValue(var1.getAppProperty("SmsCode1"));
         String var24 = decryptJadValue(var1.getAppProperty("SmsCode2"));
         String var4 = decryptJadValue(var1.getAppProperty("SmsDest1"));
         String var5 = decryptJadValue(var1.getAppProperty("SmsDest2"));
         smsCodes[0] = split(var14, ";");
         smsCodes[1] = split(var24, ";");
         smsDests[0] = split(var4, ";");
         smsDests[1] = split(var5, ";");
         String[] var22 = split(var1.getAppProperty("LogoLinkReco"), ";");

         for (int var25 = 0; var25 < (var22 != null ? (var22.length > 3 ? 3 : var22.length) : 0); var25++) {
            logoLinkReco[var25] = parseInt(var22[var25]);
         }

         if ((var24 = var1.getAppProperty("FeeMode")) != null) {
            feeMode = parseInt(var24);
         }

         var14 = decryptJadValue(var1.getAppProperty("DoCode"));
         String var10 = decryptJadValue(var1.getAppProperty("DoDest"));
         doCodes[0] = split(var14, ";");
         doDests[0] = split(var10, ";");
         return;
      } catch (NullPointerException var8) {
         var8.printStackTrace();
      } finally {
         int var2 = 1;
         if (smsCodes[0] != null && smsDests[0] != null) {
            if (smsCodes[0].length != smsDests[0].length) {
               var2 = 0;
            }
         } else {
            var2 = 0;
         }

         if (!var2) {
            smsCodes[0] = null;
            smsDests[0] = null;
            smsCodes[0] = new String[2];
            smsDests[0] = new String[2];
            smsCodes[0][0] = "62";
            smsCodes[0][1] = "63";
            smsDests[0][0] = "106662267";
            smsDests[0][1] = "106662269";
         }

         var2 = (boolean)1;
         if (smsCodes[1] != null && smsDests[1] != null) {
            if (smsCodes[1].length != smsDests[1].length) {
               var2 = (boolean)0;
            }
         } else {
            var2 = (boolean)0;
         }

         if (!var2) {
            smsCodes[1] = null;
            smsDests[1] = null;
            smsCodes[1] = new String[1];
            smsDests[1] = new String[1];
            smsCodes[1][0] = "JG";
            smsDests[1][0] = "1066170073";
         }

         var2 = (boolean)1;
         if (doCodes[0] != null && doDests[0] != null) {
            if (doCodes[0].length != doDests[0].length) {
               var2 = (boolean)0;
            }
         } else {
            var2 = (boolean)0;
         }

         if (!var2) {
            doCodes[0] = null;
            doDests[0] = null;
            this.loadCpidList();
         }

         var2 = 0;

         while (true) {
            if (var2 >= 2) {
               ;
            } else {
               smsCodeUseCounts[var2] = new int[smsCodes[var2].length];

               for (int var3 = 0; var3 < smsCodes[var2].length; var3++) {
                  smsCodeUseCounts[var2][var3] = 0;
               }

               var2++;
            }
         }
      }
   }

   public static String[] pickSmsCode(int var0) {
      if (var0 != 1 && var0 != 2) {
         var0 = 2;
      }

      int var2 = 0;
      int var1 = 100000;
      boolean var3 = true;

      for (int var4 = 0; var4 < smsCodeUseCounts[var0 - 1].length; var4++) {
         if (smsCodeUseCounts[var0 - 1][var4] <= var1) {
            var2 = var4;
            var1 = smsCodeUseCounts[var0 - 1][var4];
         }

         if (smsCodeUseCounts[var0 - 1][var4] != 0) {
            var3 = false;
         }
      }

      if (var3) {
         var2 = Math.abs(new Random().nextInt()) % 100 % smsCodes[var0 - 1].length;
         System.out.println("Random minId :" + var2);
      }

      if (var2 < 0 || var2 >= smsCodes[var0 - 1].length) {
         var2 = 0;
      }

      String[] var5;
      (var5 = new String[2])[0] = smsCodes[var0 - 1][var2];
      var5[1] = smsDests[var0 - 1][var2];
      smsCodeUseCounts[var0 - 1][var2]++;
      return var5;
   }

   public static String getChannelFirst() {
      return channelFirst;
   }

   public static String getChannelSecond() {
      return channelSecond;
   }

   public static String getTerminal() {
      return terminal;
   }

   public static String[] getFreeCodeDest() {
      String[] var0;
      (var0 = new String[2])[0] = freeCode;
      var0[1] = freeDest;
      return var0;
   }

   public static String getInterChannel() {
      return interChannel;
   }

   public static String getQQ() {
      return qq;
   }

   public static int getFeeMode() {
      return feeMode;
   }

   public final String[] getDoCodeDest(int var1, int var2) {
      if (var1 == 0) {
         return null;
      } else {
         try {
            var1--;
            if (doCodes[0] != null && var1 >= 0 && var1 < doCodes[0].length) {
               if (doCodes[0][var1] != null && doDests[0][var1] != null && doCodes[0][var1].length() > 0 && doDests[0][var1].length() > 0) {
                  String[] var5;
                  (var5 = new String[2])[0] = doCodes[0][var1];
                  var5[1] = doDests[0][var1];
                  return var5;
               } else {
                  return this.getCpidCodeDest(var1, var2);
               }
            } else {
               return this.getCpidCodeDest(var1, var2);
            }
         } catch (Exception var3) {
            return null;
         }
      }
   }

   private String[] getCpidCodeDest(int var1, int var2) {
      String var3 = Integer.toString(var2);
      return getCpidEntry(var1, var3);
   }
}
