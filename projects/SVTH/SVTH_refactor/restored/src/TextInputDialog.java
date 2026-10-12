import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public final class TextInputDialog implements CommandListener {
   TextField textField = null;
   Form form = null;
   ResultListener listener;

   public TextInputDialog() {
   }

   public final void commandAction(Command var1, Displayable var2) {
      if (var1.getLabel().equals("OK")) {
         if (this.textField.getString() != null && !this.textField.getString().equals("")) {
            this.listener.onResult(0, this.textField.getString());
            GameCanvas.instance.setDisplayable(GameCanvas.instance);
         }
      } else {
         if (var1.getLabel().equals("Trở về")) {
            GameCanvas.instance.setDisplayable(GameCanvas.instance);
         }
      }
   }
}
