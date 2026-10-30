/*
 * jl-kvm guest class library (jsr256 layer, API per tools/api-sigs/db/jsr256-sensor.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.sensor;

public interface SensorInfo {
    int CONN_EMBEDDED = 1;
    int CONN_REMOTE = 2;
    int CONN_SHORT_RANGE_WIRELESS = 4;
    int CONN_WIRED = 8;
    String CONTEXT_TYPE_AMBIENT = "ambient";
    String CONTEXT_TYPE_DEVICE = "device";
    String CONTEXT_TYPE_USER = "user";
    String CONTEXT_TYPE_VEHICLE = "vehicle";
    String PROP_IS_CONTROLLABLE = "controllable";
    String PROP_IS_REPORTING_ERRORS = "errorsReported";
    String PROP_LATITUDE = "latitude";
    String PROP_LOCATION = "location";
    String PROP_LONGITUDE = "longitude";
    String PROP_MAX_RATE = "maxSamplingRate";
    String PROP_VENDOR = "vendor";
    String PROP_VERSION = "version";
    javax.microedition.sensor.ChannelInfo[] getChannelInfos();
    int getConnectionType();
    String getContextType();
    String getDescription();
    int getMaxBufferSize();
    String getModel();
    Object getProperty(String a0);
    String[] getPropertyNames();
    String getQuantity();
    String getUrl();
    boolean isAvailabilityPushSupported();
    boolean isAvailable();
    boolean isConditionPushSupported();
}
