/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class List extends Screen implements Choice {
    public static final Command SELECT_COMMAND = new Command("", Command.SCREEN, 0);
    final ChoiceModel m;
    Command selectCommand = SELECT_COMMAND;

    public List(String title, int listType) {
        ChoiceModel.checkType(listType, true, false);
        m = new ChoiceModel(listType);
        setTitle(title);
    }

    public List(String title, int listType, String[] stringElements, Image[] imageElements) {
        ChoiceModel.checkType(listType, true, false);
        m = new ChoiceModel(listType);
        m.fill(stringElements, imageElements);
        setTitle(title);
    }

    public int size() {
        return m.n;
    }

    public String getString(int elementNum) {
        return m.getString(elementNum);
    }

    public Image getImage(int elementNum) {
        return m.getImage(elementNum);
    }

    public int append(String stringPart, Image imagePart) {
        int r = m.append(stringPart, imagePart);
        touch();
        return r;
    }

    public void insert(int elementNum, String stringPart, Image imagePart) {
        m.insert(elementNum, stringPart, imagePart);
        touch();
    }

    public void delete(int elementNum) {
        m.delete(elementNum);
        touch();
    }

    public void deleteAll() {
        m.deleteAll();
        touch();
    }

    public void set(int elementNum, String stringPart, Image imagePart) {
        m.set(elementNum, stringPart, imagePart);
        touch();
    }

    public boolean isSelected(int elementNum) {
        return m.isSelected(elementNum);
    }

    public int getSelectedIndex() {
        return m.getSelectedIndex();
    }

    public int getSelectedFlags(boolean[] selectedArray_return) {
        return m.getSelectedFlags(selectedArray_return);
    }

    public void setSelectedIndex(int elementNum, boolean selected) {
        m.setSelectedIndex(elementNum, selected);
        touch();
    }

    public void setSelectedFlags(boolean[] selectedArray) {
        m.setSelectedFlags(selectedArray);
        touch();
    }

    public void setFitPolicy(int fitPolicy) {
        m.setFitPolicy(fitPolicy);
        touch();
    }

    public int getFitPolicy() {
        return m.fit;
    }

    public void setFont(int elementNum, Font font) {
        m.setFont(elementNum, font);
        touch();
    }

    public Font getFont(int elementNum) {
        return m.getFont(elementNum);
    }

    /** Only for IMPLICIT lists; the command is added to the list's commands (as by addCommand); null: no command is issued on selection. */
    public void setSelectCommand(Command command) {
        if (m.type != IMPLICIT) return;
        Command old = selectCommand;
        if (command != null && command != SELECT_COMMAND) addCommand(command);
        if (old != null && old != SELECT_COMMAND && old != command) removeCommand(old);
        selectCommand = command;
        touch();
    }

    public void removeCommand(Command cmd) {
        if (cmd == selectCommand && cmd != SELECT_COMMAND) selectCommand = null;
        super.removeCommand(cmd);
    }

    public void setTitle(String s) {
        super.setTitle(s);
    }

    public void setTicker(Ticker t) {
        super.setTicker(t);
    }

    int uiKind() {
        return ScreenCodec.K_LIST;
    }

    void encodeBody() {
        ScreenCodec.listBody(this);
    }

    Command findCommand(int cid) {
        Command c = super.findCommand(cid);
        if (c == null && selectCommand != null && selectCommand.cid == cid) c = selectCommand;
        return c;
    }

    /** Event thread: the user picked an element. */
    void onUiChoice(int i, boolean s) {
        m.hostSelect(i, s);
        if (m.type == IMPLICIT) {
            Command c = selectCommand;
            if (c != null) fire(c);
        }
    }
}
