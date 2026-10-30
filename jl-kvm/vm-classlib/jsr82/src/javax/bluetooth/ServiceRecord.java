/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.bluetooth;

public interface ServiceRecord {
    int AUTHENTICATE_ENCRYPT = 2;
    int AUTHENTICATE_NOENCRYPT = 1;
    int L2CAP_UUID = 256;
    int NOAUTHENTICATE_NOENCRYPT = 0;
    int PROTOCOL_DESCRIPTOR_LIST_ID = 4;
    int RFCOMM_UUID = 3;
    int SERIAL_PORT_UUID = 4353;
    int SERVICE_CLASS_IDLIST_ID = 1;
    int SERVICE_ID = 3;
    int SERVICE_NAME_ID = 256;
    int SERVICE_RECORD_HANDLE_ID = 0;
    int SERVICE_RECORD_STATE_ID = 2;
    int[] getAttributeIDs();
    javax.bluetooth.DataElement getAttributeValue(int a0);
    String getConnectionURL(int a0, boolean a1);
    javax.bluetooth.RemoteDevice getHostDevice();
    boolean populateRecord(int[] a0) throws java.io.IOException;
    boolean setAttributeValue(int a0, javax.bluetooth.DataElement a1);
    void setDeviceServiceClasses(int a0);
}
