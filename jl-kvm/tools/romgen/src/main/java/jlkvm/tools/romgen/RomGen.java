package jlkvm.tools.romgen;

import jlkvm.ClassSource;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFormatException;
import jlkvm.classfile.ClassParser;
import jlkvm.classfile.ConstantPool;
import jlkvm.classfile.Descriptors;
import jlkvm.tools.apisigs.ApiDb;
import jlkvm.tools.apisigs.Differ;
import jlkvm.tools.apisigs.Extractor;
import jlkvm.verify.SourceHierarchy;
import jlkvm.verify.VerifyException;
import jlkvm.verify.Verifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ROM generator (PLAN T17, D11): guest sources -> javac -source 1.3 -target 1.3 with a bootclasspath that is ONLY the library itself
 * -> ProGuard -microedition preverify (StackMap) -> checks -> rom.jar.
 *
 * <pre>
 * RomGen --src DIR [--src DIR]... --out ROM.jar [--base BASE_ROM.jar] [--work DIR] [--jdk8 HOME]
 *        [--api-db DB.json]... [--api-ignore PREFIX]... [--delta] [--no-verify]
 * </pre>
 *
 * Checks (any failure = exit 1, no jar written):
 * <ol>
 *   <li>javac sees nothing but the library (an empty directory as bootclasspath, or --base's classes when building an overlay);</li>
 *   <li>closure: every class named by a constant pool, super/interfaces, field/method descriptor or throws clause exists in the ROM
 *       (a guest class may never reference anything outside itself);</li>
 *   <li>every class passes the project's strict verifier (D6, vm-core), CLDC 1.1;</li>
 *   <li>optional: the public/protected surface equals the signature DB (D9, {@code api-sigs diff}).</li>
 * </ol>
 * Guest sources are never compiled against rt.jar/android.jar (D11).
 */
public final class RomGen {
    private RomGen() {}

    private static final String PROGUARD_ARGS = "-microedition\n-dontshrink\n-dontoptimize\n-dontobfuscate\n-dontnote\n-ignorewarnings\n"
            + "-keepattributes SourceFile,LineNumberTable\n";

    public static void main(String[] args) throws Exception {
        List<Path> srcs = new ArrayList<>();
        Path out = null, base = null, work = null;
        List<Path> apiDb = new ArrayList<>();
        boolean delta = false;
        String jdk8 = System.getenv("JDK8_HOME") != null ? System.getenv("JDK8_HOME") : "/opt/jdk8";
        List<String> apiIgnore = new ArrayList<>();
        boolean verify = true;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--src": srcs.add(Paths.get(args[++i])); break;
                case "--out": out = Paths.get(args[++i]); break;
                case "--base": base = Paths.get(args[++i]); break;
                case "--work": work = Paths.get(args[++i]); break;
                case "--jdk8": jdk8 = args[++i]; break;
                case "--api-db": apiDb.add(Paths.get(args[++i])); break;
                case "--delta": delta = true; break;
                case "--api-ignore": apiIgnore.add(args[++i]); break;
                case "--no-verify": verify = false; break;
                default: usage();
            }
        }
        if (srcs.isEmpty() || out == null) usage();
        try {
            build(srcs, out, base, work, Paths.get(jdk8), apiDb, apiIgnore, verify, delta);
        } catch (RomException e) {
            System.err.println("romgen: FAILED: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void usage() {
        System.err.println("usage: RomGen --src DIR... --out ROM.jar [--base BASE.jar] [--work DIR] [--jdk8 HOME] [--api-db DB.json]... [--api-ignore PREFIX]... [--delta] [--no-verify]\n  --api-db may be repeated (the databases are merged: the ROM surface must equal their union); --delta writes only the classes compiled from --src (a layer jar), the checks still see base + layer");
        System.exit(2);
    }

    /** A check failed. */
    public static final class RomException extends RuntimeException {
        public RomException(String m) { super(m); }
    }

    /** @return the number of classes in the ROM written to {@code out}. */
    public static int build(List<Path> srcs, Path out, Path base, Path work, Path jdk8, Path apiDb, List<String> apiIgnore, boolean verify)
            throws Exception {
        return build(srcs, out, base, work, jdk8, apiDb == null ? new ArrayList<Path>() : java.util.Collections.singletonList(apiDb), apiIgnore, verify, false);
    }

    public static int build(List<Path> srcs, Path out, Path base, Path work, Path jdk8, List<Path> apiDbs, List<String> apiIgnore, boolean verify, boolean delta)
            throws Exception {
        boolean tmp = work == null;
        Path w = tmp ? Files.createTempDirectory("romgen") : work;
        try {
            deleteTree(w);
            Path raw = Files.createDirectories(w.resolve("raw")), pv = Files.createDirectories(w.resolve("pv")), empty = Files.createDirectories(w.resolve("empty"));
            // 1. javac -source 1.3 -target 1.3 (D11)
            List<String> files = new ArrayList<>();
            for (Path s : srcs) {
                try (Stream<Path> st = Files.walk(s)) {
                    st.filter(p -> p.toString().endsWith(".java")).sorted().forEach(p -> files.add(p.toAbsolutePath().toString()));
                }
            }
            if (files.isEmpty()) throw new RomException("no .java sources under " + srcs);
            Path list = w.resolve("srcs.txt");
            Files.write(list, files, StandardCharsets.UTF_8);
            Path javac = jdk8.resolve("bin/javac");
            if (!Files.isExecutable(javac)) throw new RomException("JDK 8 javac not found at " + javac + " (set JDK8_HOME or --jdk8; javac 21 has no -source 1.3)");
            // bootclasspath = the library itself: an empty directory (all classes come from the sources), plus the base ROM for an overlay
            String boot = base == null ? empty.toString() : base.toAbsolutePath().toString();
            run(w, "javac", javac.toString(), "-J-Duser.language=en", "-encoding", "UTF-8", "-nowarn", "-g", "-source", "1.3", "-target", "1.3",
                    "-bootclasspath", boot, "-extdirs", "", "-d", raw.toString(), "@" + list);
            // 2. preverify (StackMap)
            Path conf = w.resolve("pg.conf");
            String libs = base == null ? "" : "-libraryjars " + base.toAbsolutePath() + "\n";
            Files.write(conf, ("-injars " + raw + "\n-outjars " + pv + "\n" + libs + PROGUARD_ARGS).getBytes(StandardCharsets.UTF_8));
            preverify(conf);

            // 3. collect classes
            Map<String, byte[]> rom = new TreeMap<>();
            if (base != null) readJar(base, rom);
            Map<String, byte[]> mine = new TreeMap<>();
            try (Stream<Path> st = Files.walk(pv)) {
                for (Path p : (Iterable<Path>) st.filter(x -> x.toString().endsWith(".class"))::iterator) {
                    String n = pv.relativize(p).toString().replace('\\', '/');
                    mine.put(n.substring(0, n.length() - 6), Files.readAllBytes(p));
                }
            }
            if (mine.isEmpty()) throw new RomException("javac/ProGuard produced no classes");
            rom.putAll(mine);

            // 4. closure: a guest class references only classes of the ROM
            final Map<String, byte[]> all = rom;
            List<String> problems = new ArrayList<>();
            for (Map.Entry<String, byte[]> e : mine.entrySet()) closure(e.getKey(), e.getValue(), all, problems);
            if (!problems.isEmpty()) throw new RomException("guest classes reference classes outside the ROM:\n  " + String.join("\n  ", problems));

            // 5. strict verifier (D6) for every class we built
            if (verify) {
                ClassSource cs = new ClassSource() {
                    public byte[] readClass(String n) { return all.get(n); }
                    public byte[] readResource(String p) { return null; }
                };
                Verifier v = new Verifier(new SourceHierarchy(cs), true, true);
                for (Map.Entry<String, byte[]> e : mine.entrySet()) {
                    try {
                        v.verify(ClassParser.parse(e.getValue()));
                    } catch (VerifyException x) {
                        problems.add(e.getKey() + ": " + x.getMessage());
                    } catch (ClassFormatException x) {
                        problems.add(e.getKey() + ": class format: " + x.getMessage());
                    }
                }
                if (!problems.isEmpty()) throw new RomException("strict verifier rejected the ROM:\n  " + String.join("\n  ", problems));
            }

            // 6. optional API surface check (D9)
            Path tmpJar = w.resolve("check.jar");
            writeJar(rom, tmpJar);
            if (!apiDbs.isEmpty()) {
                ApiDb actual = Extractor.extract("rom", "romgen", null, tmpJar);
                ApiDb expected = ApiDb.read(apiDbs.get(0));
                StringBuilder names = new StringBuilder(apiDbs.get(0).getFileName().toString());
                for (int i = 1; i < apiDbs.size(); i++) {
                    expected.classes.addAll(ApiDb.read(apiDbs.get(i)).classes);
                    names.append(" + ").append(apiDbs.get(i).getFileName());
                }
                List<String> diff = Differ.diff(expected, actual, apiIgnore.toArray(new String[0]));
                if (!diff.isEmpty()) throw new RomException("API surface differs from " + names + " (" + diff.size() + "):\n  " + String.join("\n  ", diff));
                System.out.println("romgen: api surface == " + names);
            }
            if (delta) writeJar(mine, tmpJar);
            Files.createDirectories(out.toAbsolutePath().getParent());
            Files.copy(tmpJar, out, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("romgen: wrote " + out + " (" + rom.size() + " classes, " + mine.size() + " compiled)");
            return rom.size();
        } finally {
            if (tmp) deleteTree(w);
        }
    }

    // ------------------------------------------------------------------ steps

    private static void run(Path dir, String what, String... cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd).directory(dir.toFile()).redirectErrorStream(true);
        pb.environment().remove("JAVA_TOOL_OPTIONS");
        Process p = pb.start();
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (InputStream in = p.getInputStream()) { in.transferTo(bo); }
        int rc = p.waitFor();
        String log = bo.toString("UTF-8").lines().filter(l -> !l.startsWith("Picked up")).collect(Collectors.joining("\n"));
        if (rc != 0) throw new RomException(what + " failed (exit " + rc + "):\n" + log);
        if (!log.isEmpty()) System.out.println(log);
    }

    /** ProGuard -microedition, in-process. */
    private static void preverify(Path conf) throws Exception {
        proguard.Configuration cfg = new proguard.Configuration();
        try (proguard.ConfigurationParser parser = new proguard.ConfigurationParser(new String[] {"@" + conf}, System.getProperties())) {
            parser.parse(cfg);
        }
        try {
            new proguard.ProGuard(cfg).execute();
        } catch (Exception e) {
            throw new RomException("ProGuard preverify failed: " + e);
        }
    }

    /** Every class a class file names must be in the ROM (arrays count by element type). */
    static void closure(String name, byte[] bytes, Map<String, byte[]> rom, List<String> problems) {
        ClassFile cf;
        try {
            cf = ClassParser.parse(bytes);
        } catch (ClassFormatException e) {
            problems.add(name + ": unparsable: " + e.getMessage());
            return;
        }
        TreeSet<String> refs = new TreeSet<>();
        if (cf.superName != null) refs.add(cf.superName);
        Collections.addAll(refs, cf.interfaces);
        ConstantPool cp = cf.pool;
        for (int i = 1; i < cp.size(); i++) {
            int t;
            try { t = cp.tag(i); } catch (ClassFormatException e) { continue; }
            if (t == ConstantPool.CLASS) addClassRef(refs, cp.className(i));
            else if (t == ConstantPool.FIELDREF || t == ConstantPool.METHODREF || t == ConstantPool.IMETHODREF) addDescriptor(refs, cp.refDescriptor(i));
        }
        for (ClassFile.FieldInfo f : cf.fields) addDescriptor(refs, f.descriptor);
        for (ClassFile.MethodInfo m : cf.methods) {
            addDescriptor(refs, m.descriptor);
            if (m.exceptions != null) Collections.addAll(refs, m.exceptions);
        }
        for (String r : refs) if (!rom.containsKey(r)) problems.add(name + " -> " + r);
    }

    private static void addClassRef(TreeSet<String> refs, String n) {
        if (n.startsWith("[")) addDescriptor(refs, n);
        else refs.add(n);
    }

    /** Adds the class types named in a field or method descriptor. */
    private static void addDescriptor(TreeSet<String> refs, String d) {
        for (int i = 0; i < d.length(); i++) {
            if (d.charAt(i) == 'L') {
                int e = d.indexOf(';', i);
                if (e < 0) return;
                refs.add(d.substring(i + 1, e));
                i = e;
            }
        }
    }

    private static void readJar(Path jar, Map<String, byte[]> into) throws IOException {
        try (ZipFile z = new ZipFile(jar.toFile())) {
            for (Enumeration<? extends ZipEntry> en = z.entries(); en.hasMoreElements();) {
                ZipEntry e = en.nextElement();
                if (!e.getName().endsWith(".class")) continue;
                try (InputStream in = z.getInputStream(e)) {
                    into.put(e.getName().substring(0, e.getName().length() - 6), in.readAllBytes());
                }
            }
        }
    }

    /** Deterministic jar (sorted, fixed timestamps): the same sources give the same bytes. */
    static void writeJar(Map<String, byte[]> classes, Path out) throws IOException {
        try (OutputStream os = Files.newOutputStream(out); JarOutputStream jo = new JarOutputStream(os)) {
            for (Map.Entry<String, byte[]> e : classes.entrySet()) {
                JarEntry je = new JarEntry(e.getKey() + ".class");
                je.setTime(0L);
                jo.putNextEntry(je);
                jo.write(e.getValue());
                jo.closeEntry();
            }
        }
    }

    private static void deleteTree(Path p) throws IOException {
        if (!Files.exists(p)) return;
        try (Stream<Path> st = Files.walk(p)) {
            for (Path x : (Iterable<Path>) st.sorted(java.util.Comparator.reverseOrder())::iterator) Files.delete(x);
        }
    }

    /** Test helper: checks a jar the same way (closure + strict verifier). @return the list of problems. */
    public static List<String> check(Path jar) throws IOException {
        Map<String, byte[]> rom = new TreeMap<>();
        readJar(jar, rom);
        List<String> problems = new ArrayList<>();
        for (Map.Entry<String, byte[]> e : rom.entrySet()) closure(e.getKey(), e.getValue(), rom, problems);
        return problems;
    }
}
