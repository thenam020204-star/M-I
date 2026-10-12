final class MarkupAttribute {
   private String name;
   private String value;

   public MarkupAttribute(String var1) {
      this.name = var1;
   }

   protected final void setValue(String var1) {
      this.value = var1;
   }

   public final String getName() {
      return this.name;
   }

   public final String getValue() {
      return this.value;
   }
}
