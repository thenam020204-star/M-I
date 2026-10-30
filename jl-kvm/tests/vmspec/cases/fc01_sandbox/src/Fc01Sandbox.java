package vmspec;

import java.io.IOException;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.midlet.MIDlet;

/** T32a: the virtual roots and the sandbox: no URL and no navigation leads out of a root. */
public class Fc01Sandbox extends MIDlet {
    static void p(String s) { System.out.println("> " + s); }

    static String cn(Throwable t) { String n = t.getClass().getName(); return n.substring(n.lastIndexOf('.') + 1); }

    static void open(String url) {
        try { FileConnection f = (FileConnection) Connector.open(url); p(url + ": opened " + f.getURL()); f.close(); } catch (Throwable t) { p(url + ": " + cn(t)); }
    }

    protected void startApp() {
        try {
            run();
        } catch (Throwable t) {
            p("UNEXPECTED " + t);
        }
        notifyDestroyed();
    }

    void run() throws Exception {
        StringBuffer sb = new StringBuffer();
        for (Enumeration e = FileSystemRegistry.listRoots(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
        p("roots " + sb);
        FileConnection all = (FileConnection) Connector.open("file:///");
        p("root list: exists " + all.exists() + " dir " + all.isDirectory() + " name '" + all.getName() + "' path " + all.getPath() + " url " + all.getURL());
        sb = new StringBuffer();
        for (Enumeration e = all.list(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
        p("root list entries " + sb);
        all.close();
        open("file:///root1/");
        open("file://localhost/root1/");
        open("file:///ROOT1/");
        open("file:///c:/dir/file.txt");
        open("file:///root1/a%20b/c%C3%A9");
        // everything below must be refused
        open("file:///root1/../x");
        open("file:///root1/a/../../x");
        open("file:///../x");
        open("file:///root1/./x");
        open("file:///root1/a%2e%2e/x");
        open("file:///root1/%2E%2E/x");
        open("file:///c:/%2e%2e/%2e%2e/etc/passwd");
        open("file:///root1/a\\b");
        open("file:///root1/a%5cb");
        open("file:///root1//x");
        open("file:///root1/a:b");
        open("file:///root1/x%00y");
        open("file:///root1/x%zz");
        open("file:///etc/passwd");
        open("file:////etc/passwd");
        open("file:///tmp/x");
        open("file://evilhost/root1/");
        open("file:root1/");
        open("file://");
        open("file:///root1/x;y=z");
        // navigation
        FileConnection d = (FileConnection) Connector.open("file:///root1/", Connector.READ_WRITE);
        FileConnection a = (FileConnection) Connector.open("file:///root1/a/", Connector.READ_WRITE);
        a.mkdir();
        d.setFileConnection("a");
        p("after setFileConnection(a): " + d.getURL() + " path " + d.getPath() + " name " + d.getName());
        try { d.setFileConnection("../x"); p("../x: ok"); } catch (Throwable t) { p("../x: " + cn(t)); }
        try { d.setFileConnection("b/c"); p("b/c: ok"); } catch (Throwable t) { p("b/c: " + cn(t)); }
        try { d.setFileConnection("missing"); p("missing: ok"); } catch (Throwable t) { p("missing: " + cn(t)); }
        try { d.setFileConnection(null); p("null: ok"); } catch (Throwable t) { p("null: " + cn(t)); }
        d.setFileConnection("..");
        p("after ..: " + d.getURL());
        try { d.setFileConnection(".."); p("above the root: ok"); } catch (Throwable t) { p("above the root: " + cn(t)); }
        p("still at " + d.getURL());
        a.delete();
        a.close();
        d.close();
        // the roots do not see each other
        FileConnection x = (FileConnection) Connector.open("file:///c:/only-in-c.txt");
        x.create();
        FileConnection y = (FileConnection) Connector.open("file:///e:/only-in-c.txt");
        p("c exists " + x.exists() + ", e exists " + y.exists());
        x.delete();
        x.close();
        y.close();
    }

    protected void pauseApp() {}
    protected void destroyApp(boolean u) {}
}
