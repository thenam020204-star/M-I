import java.nio.file.*;
import java.util.*;

/**
 * Informational difftest for T08: loads every class of build/verify/cases.txt into HotSpot with -Xverify:all and prints
 * "id  strict-verdict-of-jl-kvm  hotspot-verdict(ok|VerifyError|other)". HotSpot is a JVM (old inference verifier for
 * class files < 50), not a CLDC VM, so differences are expected and are listed in docs/verifier-report.md.
 * Usage: java -Xverify:all HotspotVerify.java DIR
 */
public class HotspotVerify {
    public static void main(String[] a) throws Exception {
        Path dir = Paths.get(a[0]);
        for (String l : Files.readAllLines(dir.resolve("cases.txt"))) {
            String[] f = l.split("\t");
            byte[] b = Files.readAllBytes(dir.resolve(f[0] + ".class"));
            ClassLoader cl = new ClassLoader(null) {
                @Override protected Class<?> findClass(String n) throws ClassNotFoundException {
                    if (n.equals("vf." + f[0])) return defineClass(n, b, 0, b.length);
                    throw new ClassNotFoundException(n);
                }
            };
            String v;
            try {
                Class.forName("vf." + f[0], true, cl);
                v = "ok";
            } catch (VerifyError e) {
                v = "VerifyError";
            } catch (Throwable t) {
                v = t.getClass().getSimpleName();
            }
            System.out.println(f[0] + "\t" + f[1] + "\t" + v);
        }
    }
}
