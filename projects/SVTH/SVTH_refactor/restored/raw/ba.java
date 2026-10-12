import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public final class ba implements CommandListener {
  TextField a_TextField = null;
  Form a_Form = null;
  cx a_cx;

  public final void commandAction(Command var1, Displayable var2) {
    if (var1.getLabel().equals("OK")) {
      if (this.a_TextField.getString() != null && !this.a_TextField.getString().equals("")) {
        this.a_cx.a(0, this.a_TextField.getString());
        da.a_da.a(da.a_da);
      }
    } else {
      if (var1.getLabel().equals("Trở về")) {
        da.a_da.a(da.a_da);
      }
    }
  }
}
