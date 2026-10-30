/*
 * jl-kvm guest class library (WMA 1.1 layer, API per tools/api-sigs/db/s40dp2-wma-1.1.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour per the WMA javadoc of the S40 DP2.0 SDK 1.1, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.wireless.messaging;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.microedition.io.Connection;

public interface MessageConnection extends Connection {
    String TEXT_MESSAGE = "text";
    String BINARY_MESSAGE = "binary";

    Message newMessage(String type);

    Message newMessage(String type, String address);

    void send(Message msg) throws IOException, InterruptedIOException;

    Message receive() throws IOException, InterruptedIOException;

    void setMessageListener(MessageListener l) throws IOException;

    int numberOfSegments(Message msg);
}
