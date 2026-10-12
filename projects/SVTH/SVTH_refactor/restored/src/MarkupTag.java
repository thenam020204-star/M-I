import java.util.Vector;

final class MarkupTag {
   String name;
   Vector attributes = new Vector(10);

   public MarkupTag() {
   }

   public final void addAttribute(MarkupAttribute var1) {
      this.attributes.addElement(var1);
   }
}
