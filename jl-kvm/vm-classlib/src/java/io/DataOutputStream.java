/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.io;

public class DataOutputStream extends OutputStream implements DataOutput {
    protected OutputStream out;

    public DataOutputStream(OutputStream out) {
        this.out = out;
    }

    public synchronized void write(int b) throws IOException {
        out.write(b);
    }

    public synchronized void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    public final void writeBoolean(boolean v) throws IOException {
        out.write(v ? 1 : 0);
    }

    public final void writeByte(int v) throws IOException {
        out.write(v);
    }

    public final void writeShort(int v) throws IOException {
        out.write((v >>> 8) & 0xFF);
        out.write(v & 0xFF);
    }

    public final void writeChar(int v) throws IOException {
        out.write((v >>> 8) & 0xFF);
        out.write(v & 0xFF);
    }

    public final void writeInt(int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write(v & 0xFF);
    }

    public final void writeLong(long v) throws IOException {
        writeInt((int) (v >>> 32));
        writeInt((int) v);
    }

    public final void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    public final void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    public final void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            writeChar(s.charAt(i));
        }
    }

    /** Modified UTF-8 with a two byte length prefix. */
    public final void writeUTF(String str) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        for (int i = 0; i < strlen; i++) {
            int c = str.charAt(i);
            if (c >= 0x0001 && c <= 0x007F) {
                utflen++;
            } else if (c > 0x07FF) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }
        if (utflen > 65535) {
            throw new UTFDataFormatException();
        }
        byte[] bytearr = new byte[utflen + 2];
        int count = 0;
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) (utflen & 0xFF);
        for (int i = 0; i < strlen; i++) {
            int c = str.charAt(i);
            if (c >= 0x0001 && c <= 0x007F) {
                bytearr[count++] = (byte) c;
            } else if (c > 0x07FF) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | (c & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | (c & 0x3F));
            }
        }
        out.write(bytearr, 0, utflen + 2);
    }
}
