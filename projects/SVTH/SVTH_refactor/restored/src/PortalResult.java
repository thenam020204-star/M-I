public final class PortalResult {
   private int code;
   private String message;

   public PortalResult(int var1, int var2, String var3) {
      this.code = var1;
      this.message = var3;
   }

   public final int getCode() {
      return this.code;
   }

   public final String getMessage() {
      return this.message;
   }
}
