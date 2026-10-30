/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package javax.microedition.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Datagram extends DataInput, DataOutput {
    String getAddress();

    byte[] getData();

    int getLength();

    int getOffset();

    void setAddress(String addr) throws IOException;

    void setAddress(Datagram reference);

    void setLength(int len);

    void setData(byte[] buffer, int offset, int len);

    void reset();
}
