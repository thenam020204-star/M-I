/*
 * jl-kvm guest class library (JSR-135 layer, profile api "jsr-135"). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * API signatures: tools/api-sigs/db/jsr-135-extra.json (extracted from the JL-Mod classes, Apache-2.0; docs/midp.md section 10).
 */
package javax.microedition.media.control;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

public interface MIDIControl extends Control {
    int NOTE_ON = 144;
    int CONTROL_CHANGE = 176;

    boolean isBankQuerySupported();

    int[] getProgram(int channel) throws MediaException;

    int getChannelVolume(int channel);

    void setProgram(int channel, int bank, int program);

    void setChannelVolume(int channel, int volume);

    int[] getBankList(boolean custom) throws MediaException;

    int[] getProgramList(int bank) throws MediaException;

    String getProgramName(int bank, int prog) throws MediaException;

    String getKeyName(int bank, int prog, int key) throws MediaException;

    void shortMidiEvent(int type, int data1, int data2);

    int longMidiEvent(byte[] data, int offset, int length);
}
