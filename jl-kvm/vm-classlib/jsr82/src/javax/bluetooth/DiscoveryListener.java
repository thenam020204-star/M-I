/*
 * jl-kvm guest class library (jsr82 layer, API per tools/api-sigs/db/s40dp2-jsr82.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.bluetooth;

public interface DiscoveryListener {
    int INQUIRY_COMPLETED = 0;
    int INQUIRY_ERROR = 7;
    int INQUIRY_TERMINATED = 5;
    int SERVICE_SEARCH_COMPLETED = 1;
    int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
    int SERVICE_SEARCH_ERROR = 3;
    int SERVICE_SEARCH_NO_RECORDS = 4;
    int SERVICE_SEARCH_TERMINATED = 2;
    void deviceDiscovered(javax.bluetooth.RemoteDevice a0, javax.bluetooth.DeviceClass a1);
    void inquiryCompleted(int a0);
    void serviceSearchCompleted(int a0, int a1);
    void servicesDiscovered(int a0, javax.bluetooth.ServiceRecord[] a1);
}
