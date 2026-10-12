import java.util.Hashtable;

public final class PageRequest {
   public boolean isRedirect;
   public String[] cacheKeys;
   public String inputFields;
   public short protocolVersion = 1;
   public Hashtable params = null;
   public String href = "";

   public PageRequest() {
   }

   public final void setCacheKeys(String var1) {
      this.cacheKeys = PortalTextUtil.splitAmpersand(var1);
   }
}
