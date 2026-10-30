/*
 * jl-kvm guest class library (jsr234 layer, API per tools/api-sigs/db/jsr234-amms.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.amms;

public interface Module extends javax.microedition.media.Controllable {
    void addMIDIChannel(javax.microedition.media.Player a0, int a1) throws javax.microedition.media.MediaException;
    void addPlayer(javax.microedition.media.Player a0) throws javax.microedition.media.MediaException;
    void removeMIDIChannel(javax.microedition.media.Player a0, int a1);
    void removePlayer(javax.microedition.media.Player a0);
}
