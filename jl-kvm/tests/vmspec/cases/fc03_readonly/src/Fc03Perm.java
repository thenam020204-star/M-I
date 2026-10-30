package vmspec;

import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemListener;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.midlet.MIDlet;

/** T32a: the permission of the profile (file.permission): "read" lets a game look but not change anything, "deny" refuses even the roots. The profile comes from profile.json. */
public class Fc03Perm extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            for (Enumeration e = FileSystemRegistry.listRoots(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
            p("roots " + sb);
        } catch (Throwable t) { p("roots: " + cn(t)); }
        try { p("addFileSystemListener " + FileSystemRegistry.addFileSystemListener(new FileSystemListener() { public void rootChanged(int s, String r) {} })); } catch (Throwable t) { p("addFileSystemListener: " + cn(t)); }
        try { FileSystemRegistry.addFileSystemListener(null); } catch (Throwable t) { p("addFileSystemListener(null): " + cn(t)); }
        String[] modes = {"READ", "WRITE", "READ_WRITE"};
        int[] m = {Connector.READ, Connector.WRITE, Connector.READ_WRITE};
        for (int i = 0; i < 3; i++) {
            try { FileConnection f = (FileConnection) Connector.open("file:///root1/", m[i]); p(modes[i] + ": opened, exists " + f.exists()); f.close(); } catch (Throwable t) { p(modes[i] + ": " + cn(t)); }
        }
        try {
            FileConnection f = (FileConnection) Connector.open("file:///root1/x.txt");
            p("open default mode ok");
            try { f.create(); p("create: ok"); } catch (Throwable t) { p("create: " + cn(t)); }
            try { f.mkdir(); p("mkdir: ok"); } catch (Throwable t) { p("mkdir: " + cn(t)); }
            p("exists " + f.exists() + " canWrite " + f.canWrite());
            f.close();
        } catch (Throwable t) { p("open default mode: " + cn(t)); }
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
