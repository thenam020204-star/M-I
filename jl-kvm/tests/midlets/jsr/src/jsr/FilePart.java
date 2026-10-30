package jsr;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/** The FileConnection part of JsrMidlet (loaded only when the profile declares jsr75-fc). */
class FilePart {
    static void p(String s) { JsrMidlet.p(s); }

    static String cn(Throwable t) { return JsrMidlet.cn(t); }

    void run() {
        try {
            FileConnection d = (FileConnection) Connector.open("file:///root1/saves/");
            if (!d.exists()) d.mkdir();
            FileConnection f = (FileConnection) Connector.open("file:///root1/saves/slot1.dat");
            if (!f.exists()) f.create();
            OutputStream o = f.openOutputStream();
            o.write("saved".getBytes());
            o.close();
            InputStream in = f.openInputStream();
            byte[] buf = new byte[16];
            int n = in.read(buf);
            in.close();
            p("file: " + new String(buf, 0, n) + " size " + f.fileSize());
            f.close();
            d.close();
        } catch (Throwable t) {
            p("file: " + cn(t));
        }
        String[] urls = {"file:///root1/../escape.txt", "file:///root1/%2e%2e/escape.txt", "file:///root1/link/secret.txt", "file:///root1/link/new.txt"};
        for (int i = 0; i < urls.length; i++) {
            try {
                FileConnection f = (FileConnection) Connector.open(urls[i]);
                p(urls[i] + ": opened, exists " + f.exists());
                if (!f.exists()) { f.create(); p(urls[i] + ": created"); }
                else { InputStream in = f.openInputStream(); p(urls[i] + ": read " + in.read()); in.close(); }
                f.close();
            } catch (Throwable t) {
                p(urls[i] + ": " + cn(t));
            }
        }
        try {
            FileConnection l = (FileConnection) Connector.open("file:///root1/");
            StringBuffer sb = new StringBuffer();
            for (Enumeration e = l.list(); e.hasMoreElements();) sb.append(e.nextElement()).append(' ');
            p("root1 list: " + sb);
            l.close();
        } catch (Throwable t) {
            p("list: " + cn(t));
        }
    }

}
