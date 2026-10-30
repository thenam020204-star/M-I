/*
 * jl-kvm guest class library (JSR-135 layer, profile api "jsr-135"). Origin: original code written for jl-kvm (PLAN D12). License: Apache-2.0.
 * API signatures: tools/api-sigs/db/jsr-135-extra.json (extracted from the JL-Mod classes, Apache-2.0; docs/midp.md section 10).
 */
package jlkvm.internal.media.ext;

import javax.microedition.media.MediaException;
import javax.microedition.media.control.MIDIControl;
import jlkvm.internal.media.Audio;

/**
 * MIDIControl on the host synthesiser (the device, not the clip: one synthesiser, UNVERIFIED which devices share it between players).
 * No bank / program queries (isBankQuerySupported false: the query methods throw MediaException). The control remembers the last
 * program and volume it sent per channel.
 */
final class Midi implements MIDIControl {
    private final int[] volume = new int[16];
    private final int[] bank = new int[16];
    private final int[] program = new int[16];

    Midi() {
        for (int i = 0; i < 16; i++) {
            volume[i] = -1;
            program[i] = -1;
            bank[i] = 0;
        }
    }

    private static void channel(int c) {
        if (c < 0 || c > 15) throw new IllegalArgumentException("channel " + c + " out of 0..15");
    }

    public boolean isBankQuerySupported() {
        return false;
    }

    public int[] getProgram(int channel) throws MediaException {
        channel(channel);
        if (program[channel] < 0) throw new MediaException("program of channel " + channel + " is not known");
        return new int[] {bank[channel], program[channel]};
    }

    public int getChannelVolume(int channel) {
        channel(channel);
        return volume[channel];
    }

    public void setProgram(int channel, int b, int prog) {
        channel(channel);
        if (b < 0 || b > 16383) throw new IllegalArgumentException("bank " + b + " out of 0..16383");
        if (prog < 0 || prog > 127) throw new IllegalArgumentException("program " + prog + " out of 0..127");
        Audio.midiShort0(CONTROL_CHANGE | channel, 0, b >> 7);
        Audio.midiShort0(CONTROL_CHANGE | channel, 32, b & 127);
        Audio.midiShort0(0xC0 | channel, prog, 0);
        bank[channel] = b;
        program[channel] = prog;
    }

    public void setChannelVolume(int channel, int v) {
        channel(channel);
        if (v < 0 || v > 127) throw new IllegalArgumentException("volume " + v + " out of 0..127");
        Audio.midiShort0(CONTROL_CHANGE | channel, 7, v);
        volume[channel] = v;
    }

    public int[] getBankList(boolean custom) throws MediaException {
        throw new MediaException("bank queries are not supported");
    }

    public int[] getProgramList(int b) throws MediaException {
        throw new MediaException("bank queries are not supported");
    }

    public String getProgramName(int b, int prog) throws MediaException {
        throw new MediaException("bank queries are not supported");
    }

    public String getKeyName(int b, int prog, int key) throws MediaException {
        throw new MediaException("bank queries are not supported");
    }

    public void shortMidiEvent(int type, int d1, int d2) {
        if (type < 0x80 || type > 0xFF || type == 0xF0 || type == 0xF7) throw new IllegalArgumentException("status byte " + type);
        if (d1 < 0 || d1 > 127 || d2 < 0 || d2 > 127) throw new IllegalArgumentException("data byte out of 0..127");
        Audio.midiShort0(type, d1, d2);
        int ch = type & 15;
        int kind = type & 0xF0;
        if (kind == 0xB0 && d1 == 7) volume[ch] = d2;
        else if (kind == 0xC0) program[ch] = d1;
    }

    public int longMidiEvent(byte[] data, int offset, int length) {
        if (data == null) throw new IllegalArgumentException("null data");
        if (offset < 0 || length < 0 || offset + length > data.length) throw new IllegalArgumentException("offset / length");
        if (length == 0) return 0;
        Audio.midiLong0(data, offset, length);
        return length;
    }
}
