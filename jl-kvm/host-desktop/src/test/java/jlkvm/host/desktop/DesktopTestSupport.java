package jlkvm.host.desktop;

import java.io.File;

/** Locates the repo root and builds the test MIDlet jars on demand (tests/midlets/build.sh needs the J2ME toolchain of tools/env). */
final class DesktopTestSupport {
    private DesktopTestSupport() {}

    static File root() { return new File(System.getProperty("jlkvm.root", "..")).getAbsoluteFile(); }

    static File midlet(String name) throws Exception {
        File jar = new File(root(), "tests/midlets/build/" + name + ".jar");
        File src = new File(root(), "tests/midlets/" + name + "/src");
        if (!jar.isFile() || newer(src, jar.lastModified())) {
            ProcessBuilder pb = new ProcessBuilder(new File(root(), "tests/midlets/build.sh").getPath(), new File(root(), "tests/midlets/" + name).getPath(), jar.getPath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            byte[] out = drain(p.getInputStream());
            if (p.waitFor() != 0) throw new IllegalStateException("midlet build failed: " + new String(out));
        }
        return jar;
    }

    static File vmspec(String id) throws Exception {
        File jar = new File(root(), "tests/vmspec/build/vmspec/" + id + "/" + id + ".jar");
        if (!jar.isFile()) {
            ProcessBuilder pb = new ProcessBuilder(new File(root(), "tests/vmspec/run_vmspec.sh").getPath(), "--engine", "jlkvm", id);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            drain(p.getInputStream());
            p.waitFor();
        }
        if (!jar.isFile()) throw new IllegalStateException("vmspec case not built: " + id);
        return jar;
    }

    static byte[] drain(java.io.InputStream in) throws java.io.IOException {
        java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) > 0;) bo.write(b, 0, n);
        return bo.toByteArray();
    }

    private static boolean newer(File dir, long t) {
        File[] fs = dir.listFiles();
        if (fs == null) return false;
        for (File f : fs) if (f.isDirectory() ? newer(f, t) : f.lastModified() > t) return true;
        return false;
    }

    static String[] lines(String s) { return s.isEmpty() ? new String[0] : s.split("\r?\n"); }

    static String onlyGuestLines(String s) {
        StringBuilder sb = new StringBuilder();
        for (String l : lines(s)) if (l.startsWith("> ")) sb.append(l).append('\n');
        return sb.toString();
    }
}
