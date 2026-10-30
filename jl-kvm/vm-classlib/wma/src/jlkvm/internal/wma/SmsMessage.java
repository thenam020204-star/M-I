/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.wma;

import java.util.Date;
import javax.wireless.messaging.Message;

/** Base of the message containers (javadoc: "just containers for the data that is passed in"; nothing is checked by the setters). */
public abstract class SmsMessage implements Message {
    private String address;
    private final Date timestamp;

    SmsMessage(String address, Date timestamp) {
        this.address = address;
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String addr) {
        address = addr;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
