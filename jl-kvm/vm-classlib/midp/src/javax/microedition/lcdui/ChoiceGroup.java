/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

public class ChoiceGroup extends Item implements Choice {
    final ChoiceModel m;

    public ChoiceGroup(String label, int choiceType) {
        super(label);
        ChoiceModel.checkType(choiceType, false, true);
        m = new ChoiceModel(choiceType);
    }

    public ChoiceGroup(String label, int choiceType, String[] stringElements, Image[] imageElements) {
        super(label);
        ChoiceModel.checkType(choiceType, false, true);
        m = new ChoiceModel(choiceType);
        m.fill(stringElements, imageElements);
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
        changed();
        return r;
    }

    public void insert(int elementNum, String stringPart, Image imagePart) {
        m.insert(elementNum, stringPart, imagePart);
        changed();
    }

    public void delete(int elementNum) {
        m.delete(elementNum);
        changed();
    }

    public void deleteAll() {
        m.deleteAll();
        changed();
    }

    public void set(int elementNum, String stringPart, Image imagePart) {
        m.set(elementNum, stringPart, imagePart);
        changed();
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
        changed();
    }

    public void setSelectedFlags(boolean[] selectedArray) {
        m.setSelectedFlags(selectedArray);
        changed();
    }

    public void setFitPolicy(int fitPolicy) {
        m.setFitPolicy(fitPolicy);
        changed();
    }

    public int getFitPolicy() {
        return m.fit;
    }

    public void setFont(int elementNum, Font font) {
        m.setFont(elementNum, font);
        changed();
    }

    public Font getFont(int elementNum) {
        return m.getFont(elementNum);
    }

    int uiKind() {
        return ScreenCodec.I_CHOICE;
    }

    void encode() {
        ScreenCodec.choice(m);
    }

    int calcPrefW() {
        return availW();
    }

    int calcPrefH() {
        return labelH() + (m.type == POPUP ? Font.getDefaultFont().getHeight() + 2 : m.prefHeight(availW()));
    }

    int calcMinW() {
        return Math.min(availW(), 4 * Font.getDefaultFont().charWidth('W'));
    }

    int calcMinH() {
        return labelH() + Font.getDefaultFont().getHeight() + 2;
    }

    /** Event thread: the user changed the selection. */
    void hostSelect(int i, boolean s) {
        m.hostSelect(i, s);
    }
}
