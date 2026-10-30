package vmspec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UTFDataFormatException;
import java.io.Writer;
import javax.microedition.midlet.MIDlet;

public class Cl05Io extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xFF;
            sb.append("0123456789abcdef".charAt(v >> 4)).append("0123456789abcdef".charAt(v & 15));
        }
        return sb.toString();
    }

    interface IoOp { void run() throws Exception; }

    static String ex(IoOp r) {
        try { r.run(); return "no-exception"; } catch (Throwable t) { return t.getClass().getName(); }
    }

    protected void startApp() {
        try { run(); } catch (Exception e) { p("unexpected " + e); }
        notifyDestroyed();
    }

    void run() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5, (byte) 200});
        p("bais " + in.available() + " " + in.read() + " " + in.markSupported());
        in.mark(0);
        byte[] b = new byte[10];
        p("bais2 " + in.read(b, 0, 3) + " " + hex(b) + " " + in.skip(1) + " " + in.available() + " " + in.read() + " " + in.read() + " " + in.read() + " " + in.skip(5) + " " + in.read(b, 0, 2) + " " + in.read(b, 0, 0));
        in.reset();
        p("bais reset " + in.available() + " " + in.read());
        ByteArrayInputStream part = new ByteArrayInputStream(new byte[] {9, 8, 7, 6}, 1, 2);
        p("bais part " + part.available() + " " + part.read() + " " + part.read() + " " + part.read());
        final ByteArrayInputStream bi = new ByteArrayInputStream(new byte[4]);
        p("bais ex " + ex(new IoOp() { public void run() throws Exception { bi.read(new byte[2], 1, 2); } }) + " " + ex(new IoOp() { public void run() throws Exception { bi.read(null, 0, 1); } }) + " " + ex(new IoOp() { public void run() throws Exception { bi.read(new byte[2], -1, 1); } }));
        p("skip neg " + new ByteArrayInputStream(new byte[5]).skip(-3) + " " + new ByteArrayInputStream(new byte[5]).skip(0));
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bo.write(65); bo.write(new byte[] {66, 67, 68}, 1, 2); bo.write(0x141);
        p("baos " + bo.size() + " " + hex(bo.toByteArray()) + " " + bo.toString());
        bo.reset();
        p("baos reset " + bo.size() + " " + hex(bo.toByteArray()));
        p("baos ex " + ex(new IoOp() { public void run() throws Exception { new ByteArrayOutputStream(-1); } }) + " " + ex(new IoOp() { public void run() throws Exception { new ByteArrayOutputStream().write(new byte[1], 1, 1); } }));
        ByteArrayOutputStream ob = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ob);
        dos.writeBoolean(true); dos.writeByte(-2); dos.writeByte(0x1FF); dos.writeShort(-3); dos.writeShort(0x12345); dos.writeChar('A'); dos.writeChar(0x10042); dos.writeInt(0x01020304); dos.writeInt(-1); dos.writeLong(0x0102030405060708L); dos.writeLong(-2L);
        dos.writeFloat(1.5f); dos.writeDouble(-2.25); dos.writeUTF("hi"); dos.writeUTF(""); dos.writeUTF("é€\u0000x"); dos.writeChars("ab"); dos.write(7); dos.write(new byte[] {8, 9}, 0, 2);
        dos.flush();
        byte[] all = ob.toByteArray();
        p("dos " + all.length + " " + hex(all));
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(all));
        p("dis " + dis.readBoolean() + " " + dis.readByte() + " " + dis.readByte() + " " + dis.readShort() + " " + dis.readShort() + " " + dis.readChar() + " " + (int) dis.readChar() + " " + dis.readInt() + " " + dis.readInt() + " " + dis.readLong() + " " + dis.readLong() + " " + dis.readFloat() + " " + dis.readDouble());
        String u1 = dis.readUTF(), u2 = dis.readUTF(), u3 = dis.readUTF();
        p("dis utf " + u1 + "|" + u2.length() + "|" + u3.length() + " " + (int) u3.charAt(0) + " " + (int) u3.charAt(1) + " " + (int) u3.charAt(2) + " " + dis.readChar() + dis.readChar() + " " + dis.readUnsignedByte() + " " + dis.available());
        byte[] fb = new byte[2];
        dis.readFully(fb);
        p("dis fully " + hex(fb) + " " + dis.available() + " " + dis.read());
        p("dis ex " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[3])).readInt(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[0])).readByte(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[1])).readUnsignedShort(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[] {0, 5, 'a'})).readUTF(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[] {0, 1, (byte) 0xC0})).readUTF(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(new ByteArrayInputStream(new byte[] {0, 1, (byte) 0xFF})).readUTF(); } }) + " " + ex(new IoOp() { public void run() throws Exception { new DataInputStream(null).read(); } }));
        p("dis skip " + new DataInputStream(new ByteArrayInputStream(new byte[5])).skipBytes(3) + " " + new DataInputStream(new ByteArrayInputStream(new byte[2])).skipBytes(9));
        p("dos ex " + ex(new IoOp() { public void run() throws Exception { new DataOutputStream(new ByteArrayOutputStream()).writeUTF(new String(new char[70000])); } }));
        p("utf len " + hex(ob2(new IoOp2() { public void run(DataOutputStream d) throws Exception { d.writeUTF("\u0000\u007f\u0080߿ࠀ￿"); } })));
        ByteArrayOutputStream cs = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(cs);
        ps.print("s"); ps.print(1); ps.print(2L); ps.print(true); ps.print('c'); ps.print(1.5f); ps.print(2.5); ps.print(new char[] {'x', 'y'}); ps.print((Object) null); ps.print((String) null); ps.println(); ps.println("L"); ps.println(7); ps.println('q'); ps.println(false); ps.println(0.5); ps.println(new char[] {'z'}); ps.println(new StringBuffer("sb"));
        ps.write(65); ps.write(new byte[] {66, 67, 68}, 1, 2); ps.flush();
        p("printstream " + cs.size() + " " + cs.toString().replace('\n', '/').replace('\r', '~'));
        p("printstream err " + ps.checkError());
        ps.close();
        ByteArrayOutputStream cs2 = new ByteArrayOutputStream();
        OutputStreamWriter w = new OutputStreamWriter(cs2);
        w.write("hello"); w.write('!'); w.write(new char[] {'a', 'b', 'c'}, 1, 2); w.write("xyz", 1, 1); w.flush();
        p("writer " + cs2.toString() + " " + cs2.size());
        w.close();
        p("writer closed ex " + ex(new IoOp() { public void run() throws Exception { new OutputStreamWriter(new ByteArrayOutputStream()).close(); } }));
        InputStreamReader rd = new InputStreamReader(new ByteArrayInputStream("line1\nline2".getBytes()));
        char[] cb = new char[20];
        p("reader " + rd.ready() + " " + (char) rd.read() + " " + rd.read(cb, 0, 4) + " " + new String(cb, 0, 4).replace('\n', '/') + " " + rd.skip(1) + " " + rd.read(cb, 0, 20) + " " + new String(cb, 0, 5) + " " + rd.read() + " " + rd.read(cb, 0, 3) + " " + rd.read(cb, 0, 0));
        p("reader mark " + rd.markSupported());
        p("reader ex " + ex(new IoOp() { public void run() throws Exception { new InputStreamReader(null); } }) + " " + ex(new IoOp() { public void run() throws Exception { new InputStreamReader(new ByteArrayInputStream(new byte[0]), null); } }) + " " + ex(new IoOp() { public void run() throws Exception { new InputStreamReader(new ByteArrayInputStream(new byte[0]), "nope"); } }) + " " + ex(new IoOp() { public void run() throws Exception { new OutputStreamWriter(new ByteArrayOutputStream(), "nope"); } }));
        InputStream plain = new InputStream() {
            int n = 0;
            public int read() { return n < 5 ? 10 * n++ : -1; }
        };
        byte[] pb = new byte[8];
        p("InputStream default " + plain.read(pb) + " " + hex(pb) + " " + plain.available() + " " + plain.markSupported() + " " + plain.skip(3) + " " + plain.read(pb, 0, 0));
        final int[] wrote = new int[1];
        OutputStream cnt = new OutputStream() {
            public void write(int x) { wrote[0] += x; }
        };
        cnt.write(new byte[] {1, 2, 3}); cnt.write(new byte[] {4, 5, 6}, 1, 2); cnt.flush(); cnt.close();
        p("OutputStream default " + wrote[0]);
        Reader rr = new Reader() {
            int i = 0;
            public int read(char[] c, int o, int l) { if (i >= 3) return -1; c[o] = (char) ('a' + i++); return 1; }
            public void close() { }
        };
        char[] rc = new char[4];
        p("Reader default " + rr.read() + " " + rr.read(rc) + " " + rc[0] + " " + rr.skip(5) + " " + rr.ready() + " " + rr.markSupported());
        final StringBuffer sink = new StringBuffer();
        Writer ww = new Writer() {
            public void write(char[] c, int o, int l) { sink.append(c, o, l); }
            public void flush() { }
            public void close() { }
        };
        ww.write(65); ww.write("BCD"); ww.write(new char[] {'E', 'F'}); ww.write("GHIJ", 1, 2);
        p("Writer default " + sink);
        p("exceptions " + new IOException("m").getMessage() + " " + new EOFException().getMessage() + " " + new UTFDataFormatException("u").getMessage());
    }

    interface IoOp2 { void run(DataOutputStream d) throws Exception; }

    static byte[] ob2(IoOp2 o) throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        o.run(new DataOutputStream(b));
        return b.toByteArray();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean u) { }
}
