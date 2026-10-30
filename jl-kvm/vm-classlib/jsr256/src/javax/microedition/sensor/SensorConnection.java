/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface SensorConnection extends javax.microedition.io.Connection {
    int STATE_CLOSED = 4;
    int STATE_LISTENING = 2;
    int STATE_OPENED = 1;
    javax.microedition.sensor.Channel getChannel(javax.microedition.sensor.ChannelInfo a0);
    javax.microedition.sensor.Data[] getData(int a0);
    javax.microedition.sensor.Data[] getData(int a0, long a1, boolean a2, boolean a3, boolean a4);
    javax.microedition.sensor.SensorInfo getSensorInfo();
    int getState();
    void removeDataListener();
    void setDataListener(javax.microedition.sensor.DataListener a0, int a1);
    void setDataListener(javax.microedition.sensor.DataListener a0, int a1, long a2, boolean a3, boolean a4, boolean a5);
}
