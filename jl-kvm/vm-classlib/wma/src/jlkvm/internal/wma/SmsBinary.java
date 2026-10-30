/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package jlkvm.internal.wma;

import java.util.Date;
import javax.wireless.messaging.BinaryMessage;

final class SmsBinary extends SmsMessage implements BinaryMessage {
    private byte[] payload;

    SmsBinary(String address, Date timestamp) {
        super(address, timestamp);
    }

    public byte[] getPayloadData() {
        return payload;
    }

    public void setPayloadData(byte[] data) {
        payload = data;
    }
}
