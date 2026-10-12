import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.ConnectionNotFoundException;

public class PortalProtocol implements HttpRequestListener {
   private HttpRequestQueue queue;
   private PortalManager manager;
   private PortalClient client;

   public PortalProtocol(PortalClient var1, PortalManager var2) {
      this.manager = var2;
      this.client = var1;
      this.queue = new HttpRequestQueue(var1);
   }

   public final void start() {
      this.queue.start();
   }

   public final void onRequestDone(HttpRequest var1) {
      byte[] var2;
      if ((var2 = var1.getData()) != null) {
         Hashtable[] var3 = parsePage(var2);
         this.manager.renderPage(var3);
         this.manager.onPageReceived(var3);
      }
   }

   public final void onRequestFailed(HttpRequest var1) {
      this.manager.onError(var1.getErrorMessage());
   }

   private static Hashtable[] parsePage(byte[] var0) {
      Hashtable[] var1 = null;

      try {
         String var8 = new String(var0, "UTF-8");
         MarkupParser var2;
         (var2 = new MarkupParser()).parse(var8);
         var1 = new Hashtable[var2.tagCount()];

         for (int var4 = 0; var4 < var1.length; var4++) {
            var1[var4] = new Hashtable(10);
            MarkupTag var9 = var2.tagAt(var4);
            var1[var4].put("type", var9.name);

            for (int var5 = 0; var5 < var9.attributes.size(); var5++) {
               MarkupAttribute var3 = var9.attributes.size() == 0 ? null : (MarkupAttribute)var9.attributes.elementAt(var5);
               var1[var4].put(var3.getName(), var3.getValue());
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      return var1;
   }

   public final void sendRequest(PageRequest var1, PageInputWidget[] var2) {
      this.client.getBrowser().currentRequest = var1;
      if (this.manager.loadState == 1) {
         this.queue.cancelAll();
      }

      this.manager.beginLoading();
      if (!var1.href.equals("") && var1.href.substring(0, 4).equals("http")) {
         try {
            this.client.midlet.platformRequest(var1.href);
         } catch (ConnectionNotFoundException var6) {
            var6.printStackTrace();
         }
      } else {
         PageInputWidget[] var5 = var2;
         PortalClient var4 = this.client;
         PageRequest var3 = var1;
         if (var1.params == null) {
            var1.params = new Hashtable();
         }

         if (var1.cacheKeys != null) {
            for (int var8 = 0; var8 < var3.cacheKeys.length; var8++) {
               var3.params.put(var3.cacheKeys[var8], PortalManager.getString(var4.settings, var3.cacheKeys[var8]));
            }
         }

         if (var2 != null) {
            for (int var9 = 0; var9 < var5.length; var9++) {
               var3.params.put(var5[var9].getFieldName(), var5[var9].getValue());
            }
         }

         byte[] var10 = null;

         try {
            var10 = this.buildPacket(var1);
         } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
         }

         HttpRequest var10000 = new HttpRequest;
         String var12 = var1.href;
         if (var1.href == null || var12.trim().length() == 0) {
            var12 = "main";
         }

         var10000./* $VF: Unable to resugar constructor */<init>(
            this.client.platformId == 888888 ? HttpRequest.sngameServerUrl + var12 : HttpRequest.portalServerUrl + var12, var10, this
         );
         HttpRequest var11 = var10000;
         if (var10 == null) {
            var11.setMethod("GET");
         } else {
            var11.setMethod("POST");
         }

         this.queue.enqueue(var11);
      }
   }

   public final void cancel() {
      this.queue.cancelAll();
   }

   public final void stop() {
      this.queue.stop();
   }

   private byte[] buildPacket(PageRequest var1) {
      if (var1 == null) {
         return null;
      } else {
         byte[] var2;
         if ((var2 = serializeMap(var1.params)) == null) {
            return null;
         } else {
            String var3 = this.client.a_String;
            byte[] var10000;
            if (this.client.a_String == null) {
               var10000 = null;
            } else {
               int var4;
               byte[] var5 = new byte[(var4 = var3.length()) << 2];
               int var6 = 0;

               for (int var7 = 0; var7 < var4; var7++) {
                  char var8;
                  if ((var8 = var3.charAt(var7)) < 128) {
                     var5[var6++] = (byte)var8;
                  } else if (var8 < 2048) {
                     var5[var6++] = (byte)(var8 >> 6 & 31 | 192);
                     var5[var6++] = (byte)(var8 & '?' | 128);
                  } else if (var8 < 65536) {
                     var5[var6++] = (byte)(var8 >> '\f' & 15 | 224);
                     var5[var6++] = (byte)(var8 >> 6 & 63 | 128);
                     var5[var6++] = (byte)(var8 & '?' | 128);
                  } else if (var8 < 2097152) {
                     var5[var6++] = (byte)(var8 >> 18 & 7 | 240);
                     var5[var6++] = (byte)(var8 >> '\f' & 63 | 128);
                     var5[var6++] = (byte)(var8 >> 6 & 63 | 128);
                     var5[var6++] = (byte)(var8 & '?' | 128);
                  }
               }

               byte[] var32 = new byte[var6];

               for (int var40 = 0; var40 < var6; var40++) {
                  var32[var40] = var5[var40];
               }

               var10000 = var32;
            }

            byte[] var12 = var10000;
            int var20;
            var20 = (var20 = var10000.length) > 32 ? 32 : var20;
            String var23 = "";
            if (this.client.settings.containsKey("_uuid")) {
               var23 = (String)this.client.settings.get("_uuid");
            }

            byte[] var31 = new byte[0];

            try {
               var31 = var23.getBytes("UTF-8");
            } catch (Exception var10) {
               try {
                  var31 = var23.getBytes("UTF-8");
               } catch (Exception var9) {
                  var9.printStackTrace();
               }

               var10.printStackTrace();
            }

            int var33 = var20 + 25;
            var33 += 2;
            var33 += var31.length;
            var33 += 4;
            var33 += 2;
            var33 += var2.length;
            var33++;
            short var11 = var1.protocolVersion;
            byte[] var24;
            (var24 = new byte[var33])[0] = 2;
            putInt32(var24, 1, (long)var33);
            putInt16(var24, 5, (short)100);
            putInt32(var24, 7, 4L);
            putInt32(var24, 11, (long)this.client.platformId);
            putInt32(var24, 15, (long)this.client.f_I);
            putInt32(var24, 19, (long)this.client.gameId);
            putInt16(var24, 23, (short)var20);
            System.arraycopy(var12, 0, var24, 25, var20);
            int var13 = var20 + 25;
            putInt16(var24, var13, (short)var31.length);
            int var14 = var13 + 2;
            System.arraycopy(var31, 0, var24, var14, var31.length);
            int var15 = var14 + var31.length;
            putInt32(var24, var15, 0L);
            int var16 = var15 + 4;
            putInt16(var24, var16, var11);
            int var17 = var16 + 2;
            System.arraycopy(var2, 0, var24, var17, var2.length);
            int var18 = var17 + var2.length;
            var24[var18] = 3;
            if (var33 != ++var18) {
               return null;
            } else {
               String var22 = "TT@Game911&007**";
               return new QQTeaCipher().encrypt(var24, var22.getBytes());
            }
         }
      }
   }

   private static void putInt32(byte[] var0, int var1, long var2) {
      var0[var1] = (byte)((int)(var2 >> 24 & 255L));
      var0[var1 + 1] = (byte)((int)(var2 >> 16 & 255L));
      var0[var1 + 2] = (byte)((int)(var2 >> 8 & 255L));
      var0[var1 + 3] = (byte)((int)(var2 & 255L));
   }

   private static void putInt16(byte[] var0, int var1, short var2) {
      var0[var1] = (byte)(var2 >> 8);
      var0[var1 + 1] = (byte)var2;
   }

   public static short getInt16(byte[] var0, int var1) {
      return (short)(var0[var1] << 8 | var0[var1 + 1]);
   }

   public static byte[] serializeMap(Hashtable var0) {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         int var2 = var0.size();

         try {
            Enumeration var3 = var0.keys();
            byte[] var4;
            putInt16(var4 = new byte[2], 0, (short)var2);
            var1.write(var4);

            while (var3.hasMoreElements()) {
               String var10;
               byte[] var5 = (var10 = (String)var3.nextElement()).getBytes("UTF-8");
               var2 = 1 + var5.length;
               var4 = ((String)var0.get(var10)).getBytes("UTF-8");
               var2 += 2;
               byte[] var9;
               (var9 = new byte[var2 + var4.length])[0] = (byte)var5.length;
               System.arraycopy(var5, 0, var9, 1, var5.length);
               int var12 = 1 + var5.length;
               putInt16(var9, var12, (short)var4.length);
               int var13 = var12 + 2;
               System.arraycopy(var4, 0, var9, var13, var4.length);
               var1.write(var9);
            }

            return var1.toByteArray();
         } catch (Exception var6) {
            var6.printStackTrace();
            return null;
         }
      }
   }

   public PortalProtocol() {
   }

   public static void dumpArray(int[] var0, String var1) {
      if (var0 == null) {
         System.out.println(var1 + " is null!");
      } else {
         System.out.println(var1 + "={");

         for (int var2 = 0; var2 < var0.length; var2++) {
            if (var2 < var0.length - 1) {
               System.out.println("" + var0[var2] + ",");
            } else {
               System.out.println("" + var0[var2]);
            }
         }

         System.out.println(" }");
         System.out.println("length=" + var0.length);
      }
   }

   public static void dumpArray(short[][] var0, String var1) {
      if (var0 == null) {
         System.out.println(var1 + " is null!");
      } else {
         System.out.println(var1 + "={");

         for (int var4 = 0; var4 < var0.length; var4++) {
            String var2 = "  {";

            for (int var3 = 0; var0[var4] != null && var3 < var0[var4].length; var3++) {
               if (var3 < var0[var4].length - 1) {
                  var2 = var2 + "" + var0[var4][var3] + ",";
               } else {
                  var2 = var2 + "" + var0[var4][var3];
               }
            }

            var2 = var2 + "}";
            if (var4 < var0.length - 1) {
               System.out.println(var2 + ",");
            } else {
               System.out.println(var2);
            }
         }

         System.out.println("}");
         System.out.println("length=" + var0.length);
      }
   }

   public static void dumpArray(String[] var0, String var1) {
      if (var0 == null) {
         System.out.println(var1 + " is null!");
      } else {
         System.out.println(var1 + "={");

         for (int var2 = 0; var2 < var0.length; var2++) {
            if (var2 < var0.length - 1) {
               System.out.println("" + var0[var2] + ",");
            } else {
               System.out.println("" + var0[var2]);
            }
         }

         System.out.println(" }");
         System.out.println("length=" + var0.length);
      }
   }
}
