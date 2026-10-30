package vmspec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.ConnectionClosedException;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.IllegalModeException;
import javax.microedition.midlet.MIDlet;

/** T32a: FileConnection operations on the virtual file system (default profile: three roots in memory, 8 MiB each). */
public class Fc02Ops extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static String ls(FileConnection f) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (Enumeration e = f.list(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
        return sb.toString();
    }

    static String ls(FileConnection f, String filter, boolean hidden) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (Enumeration e = f.list(filter, hidden); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
        return sb.toString();
    }

    static FileConnection open(String url) throws IOException { return (FileConnection) Connector.open(url); }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
            t.printStackTrace();
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        FileConnection root = open("file:///root1/");
        p("root: total " + root.totalSize() + " used " + root.usedSize() + " available " + root.availableSize() + " list '" + ls(root) + "' size " + root.directorySize(true));
        FileConnection dir = open("file:///root1/data/");
        p("dir before: exists " + dir.exists() + " isDirectory " + dir.isDirectory() + " name " + dir.getName() + " path " + dir.getPath());
        dir.mkdir();
        p("dir after mkdir: exists " + dir.exists() + " isDirectory " + dir.isDirectory() + " name " + dir.getName() + " url " + dir.getURL());
        try { dir.mkdir(); p("mkdir twice: ok"); } catch (Throwable t) { p("mkdir twice: " + cn(t)); }
        FileConnection f = open("file:///root1/data/save.dat");
        p("file before: exists " + f.exists() + " size " + f.fileSize() + " lastModified " + f.lastModified() + " canRead " + f.canRead() + " canWrite " + f.canWrite());
        try { f.openInputStream(); p("read missing: ok"); } catch (Throwable t) { p("read missing: " + cn(t)); }
        try { f.openOutputStream(); p("write missing: ok"); } catch (Throwable t) { p("write missing: " + cn(t)); }
        f.create();
        try { f.create(); p("create twice: ok"); } catch (Throwable t) { p("create twice: " + cn(t)); }
        p("file after create: exists " + f.exists() + " isDirectory " + f.isDirectory() + " size " + f.fileSize() + " name " + f.getName() + " path " + f.getPath() + " canRead " + f.canRead() + " canWrite " + f.canWrite());
        OutputStream o = f.openOutputStream();
        o.write(new byte[] {1, 2, 3, 4, 5});
        o.write(6);
        o.flush();
        p("size after flush " + f.fileSize());
        o.close();
        InputStream in = f.openInputStream();
        byte[] b = new byte[10];
        int n = in.read(b, 0, 10);
        p("read " + n + ": " + b[0] + b[1] + b[2] + b[3] + b[4] + b[5] + " next " + in.read() + " available " + in.available());
        in.close();
        // openOutputStream() overwrites from the start and does not shorten the file
        o = f.openOutputStream();
        o.write(9);
        o.write(9);
        o.close();
        p("size after overwrite " + f.fileSize());
        // append at the end
        o = f.openOutputStream(f.fileSize());
        o.write(7);
        o.close();
        try { f.openOutputStream(100); p("offset beyond the end: ok"); } catch (Throwable t) { p("offset beyond the end: " + cn(t)); }
        try { f.openOutputStream(-1); p("negative offset: ok"); } catch (Throwable t) { p("negative offset: " + cn(t)); }
        DataInputStream di = f.openDataInputStream();
        p("data: " + di.readByte() + di.readByte() + di.readByte() + di.readByte() + di.readByte() + di.readByte() + di.readByte() + " size " + f.fileSize());
        di.close();
        DataOutputStream dout = f.openDataOutputStream();
        dout.writeInt(0x01020304);
        dout.writeUTF("hi");
        dout.close();
        di = f.openDataInputStream();
        p("data again: " + di.readInt() + " " + di.readUTF() + " size " + f.fileSize());
        di.close();
        f.truncate(3);
        p("size after truncate(3) " + f.fileSize());
        f.truncate(50);
        p("size after truncate(50) " + f.fileSize());
        try { f.truncate(-1); p("truncate(-1): ok"); } catch (Throwable t) { p("truncate(-1): " + cn(t)); }
        p("used " + root.usedSize() + " directorySize " + root.directorySize(true) + "/" + root.directorySize(false));
        // list, filter, hidden
        FileConnection g = open("file:///root1/data/Game.sav");
        g.create();
        FileConnection h = open("file:///root1/data/hidden.tmp");
        h.create();
        h.setHidden(true);
        p("hidden " + h.isHidden() + " " + g.isHidden());
        FileConnection sub = open("file:///root1/data/sub/");
        sub.mkdir();
        p("list: " + ls(dir));
        p("list(*.sav, false): " + ls(dir, "*.sav", false) + "| list(*.sa*, true): " + ls(dir, "*.sa*", true) + "| list(*, true): " + ls(dir, "*", true) + "| list(sub, true): " + ls(dir, "sub", true) + "| list(save.dat, false): " + ls(dir, "save.dat", false));
        try { f.list(); p("list of a file: ok"); } catch (Throwable t) { p("list of a file: " + cn(t)); }
        try { dir.delete(); p("delete non-empty: ok"); } catch (Throwable t) { p("delete non-empty: " + cn(t)); }
        // attributes
        f.setWritable(false);
        p("canWrite after setWritable(false): " + f.canWrite() + " canRead " + f.canRead());
        try { f.openOutputStream(); p("write read-only file: ok"); } catch (Throwable t) { p("write read-only file: " + cn(t)); }
        f.setWritable(true);
        f.setReadable(false);
        try { f.openInputStream(); p("read unreadable file: ok"); } catch (Throwable t) { p("read unreadable file: " + cn(t)); }
        f.setReadable(true);
        // rename
        f.rename("save2.dat");
        p("renamed: " + f.getURL() + " exists " + f.exists() + " old exists " + open("file:///root1/data/save.dat").exists());
        try { f.rename("Game.sav"); p("rename onto an existing file: ok"); } catch (Throwable t) { p("rename onto an existing file: " + cn(t)); }
        try { f.rename("a/b"); p("rename with a path: ok"); } catch (Throwable t) { p("rename with a path: " + cn(t)); }
        try { f.rename(".."); p("rename ..: ok"); } catch (Throwable t) { p("rename ..: " + cn(t)); }
        // modes
        FileConnection ro = open("file:///root1/data/save2.dat");
        FileConnection r = (FileConnection) Connector.open("file:///root1/data/save2.dat", Connector.READ);
        FileConnection w = (FileConnection) Connector.open("file:///root1/data/save2.dat", Connector.WRITE);
        try { r.openOutputStream(); p("READ mode openOutputStream: ok"); } catch (Throwable t) { p("READ mode openOutputStream: " + cn(t)); }
        try { r.delete(); p("READ mode delete: ok"); } catch (Throwable t) { p("READ mode delete: " + cn(t)); }
        try { w.openInputStream(); p("WRITE mode openInputStream: ok"); } catch (Throwable t) { p("WRITE mode openInputStream: " + cn(t)); }
        try { w.fileSize(); p("WRITE mode fileSize: ok"); } catch (Throwable t) { p("WRITE mode fileSize: " + cn(t)); }
        p("READ mode fileSize " + r.fileSize());
        // streams outlive the connection
        InputStream s2 = r.openInputStream();
        r.close();
        p("stream after close: " + s2.read());
        s2.close();
        try { s2.read(); p("read closed stream: ok"); } catch (Throwable t) { p("read closed stream: " + cn(t)); }
        p("Connector.openInputStream: " + Connector.openInputStream("file:///root1/data/save2.dat").read());
        // closed connection
        p("isOpen " + r.isOpen() + " name still " + r.getName());
        try { r.exists(); p("exists on closed: ok"); } catch (Throwable t) { p("exists on closed: " + cn(t)); }
        try { r.fileSize(); p("fileSize on closed: ok"); } catch (Throwable t) { p("fileSize on closed: " + cn(t)); }
        try { r.list(); p("list on closed: ok"); } catch (Throwable t) { p("list on closed: " + cn(t)); }
        // delete
        sub.delete();
        g.delete();
        h.delete();
        ro.delete();
        try { ro.delete(); p("delete twice: ok"); } catch (Throwable t) { p("delete twice: " + cn(t)); }
        dir.delete();
        p("end: list '" + ls(root) + "' used " + root.usedSize());
        // limits: a file cannot be created in a missing directory; a directory is not a file
        FileConnection orphan = open("file:///root1/nodir/x.txt");
        try { orphan.create(); p("create without parent: ok"); } catch (Throwable t) { p("create without parent: " + cn(t)); }
        try { root.openInputStream(); p("read a directory: ok"); } catch (Throwable t) { p("read a directory: " + cn(t)); }
        try { root.delete(); p("delete a root: ok"); } catch (Throwable t) { p("delete a root: " + cn(t)); }
        // FileConnection is a StreamConnection (Connection types of the generic framework)
        p("types " + (root instanceof javax.microedition.io.StreamConnection) + " " + (root instanceof javax.microedition.io.InputConnection));
        p("exceptions " + (new ConnectionClosedException() instanceof RuntimeException) + " " + (new IllegalModeException("x") instanceof RuntimeException));
        orphan.close();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
