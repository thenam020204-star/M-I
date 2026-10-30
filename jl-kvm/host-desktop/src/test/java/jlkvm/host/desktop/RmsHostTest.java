package jlkvm.host.desktop;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import jlkvm.host.rms.FileRecordStorage;
import jlkvm.host.rms.RecordStorage;
import jlkvm.profile.ProfileLoader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * T26 on the desktop host: the record stores of a suite outlive a run (FileRecordStorage), another suite reaches the shared ones by
 * vendor + suite name (authmode / writable), the quota of the profile decides "full", and a JL-Mod save directory is imported.
 */
public class RmsHostTest {
    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    private static HeadlessRunner.Result run(String midlet, RecordStorage st, File profile) throws Exception {
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet(midlet);
        o.records = st;
        o.profile = profile;
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertEquals(r.stdout, HeadlessRunner.Result.EXITED, r.exitKind);
        assertEquals(r.host.guestErr.toString(), 0, r.host.guestErr.length());
        return r;
    }

    private File profile(String rms) throws Exception {
        String base = new String(Files.readAllBytes(new File(DesktopTestSupport.root(), "profiles/jlkvm-min.json").toPath()), "UTF-8");
        File f = tmp.newFile();
        // replace the "rms" object of the default profile
        String json = base.replaceAll("(?s)\"rms\"\\s*:\\s*\\{.*?\\}", "\"rms\": " + rms);
        assertNotEquals(base, json);
        Files.write(f.toPath(), json.getBytes("UTF-8"));
        return f;
    }

    @Test public void storesSurviveRunsAndAreSharedBetweenSuites() throws Exception {
        File dir = tmp.newFolder("rms");
        FileRecordStorage st = new FileRecordStorage(dir, ProfileLoader.fromJson("{}"));
        String r1 = DesktopTestSupport.onlyGuestLines(run("rms", st, null).stdout);
        assertEquals("> run 1: creating\n> game version=1 records=1\n", r1);
        // a new storage object over the same directory = a new process
        st = new FileRecordStorage(dir, ProfileLoader.fromJson("{}"));
        String r2 = DesktopTestSupport.onlyGuestLines(run("rms", st, null).stdout);
        assertEquals("> later run: stores=4 game records=1 nextId=2 version=1\n>   record 1 = score=100\n> rw holds: w1\n", r2);

        String b = DesktopTestSupport.onlyGuestLines(run("rmsb", st, null).stdout);
        assertEquals("> own stores: null\n"
            + "> pub: records=2 first=p1 name=pub\n"
            + "> pub add: SecurityException\n"
            + "> pub set: SecurityException\n"
            + "> pub delete: SecurityException\n"
            + "> pub setMode: SecurityException\n"
            + "> pub still 2 records, same object again: true\n"
            + "> pub closed: RecordStoreNotOpenException\n"
            + "> rw: records=2\n"
            + "> priv: SecurityException\n"
            + "> missing: RecordStoreNotFoundException\n"
            + "> no suite: RecordStoreNotFoundException\n"
            + "> other vendor: RecordStoreNotFoundException\n"
            + "> own suite by name: same object=true\n"
            + "> own list: 1\n", b);

        st = new FileRecordStorage(dir, ProfileLoader.fromJson("{}"));
        String r3 = DesktopTestSupport.onlyGuestLines(run("rms", st, null).stdout);
        assertTrue(r3, r3.contains("> rw holds: w1 fromB\n"));
        assertTrue(r3, r3.contains("> later run: stores=4 game records=2"));
        assertTrue(st.problems.toString(), st.problems.isEmpty());
    }

    @Test public void quotaFromTheProfile() throws Exception {
        File p = profile("{\"quotaBytes\": 1000, \"storeOverheadBytes\": 16, \"recordOverheadBytes\": 8}");
        String out = DesktopTestSupport.onlyGuestLines(run("rmsq", new jlkvm.host.rms.MemoryRecordStorage(ProfileLoader.fromJson("{\"rms\":{\"quotaBytes\":1000,\"storeOverheadBytes\":16,\"recordOverheadBytes\":8}}")), p).stdout);
        assertEquals("> empty: size=16 available=984\n"
            + "> full after 9 records\n"
            + "> size=988 available=12 records=9 nextId=10\n"
            + "> same size set ok\n"
            + "> after delete: available=120\n"
            + "> grown by 120 (exactly the room): available=0\n"
            + "> grow by 1: full, record 1 still 220 bytes\n"
            + "> empty record: full (needs 8 bytes overhead)\n"
            + "> second store: full\n"
            + "> after deleting the big record: available=228\n"
            + "> second store created, size=16 available=212 (quota is shared by the stores of the suite)\n"
            + "> after delete: null\n", out);
    }

    @Test public void importsAJlModSaveDirectory() throws Exception {
        File jl = tmp.newFolder("jlmod");
        try (java.io.DataOutputStream o = new java.io.DataOutputStream(new java.io.FileOutputStream(new File(jl, "game.rsh")))) {
            o.write("MIDRMS".getBytes("US-ASCII")); o.write(3); o.write(0); o.write(0);
            o.writeUTF("game"); o.writeLong(1L); o.writeInt(4); o.writeInt(0); o.writeByte(0); o.writeInt(1); o.writeInt(1);
        }
        try (java.io.DataOutputStream o = new java.io.DataOutputStream(new java.io.FileOutputStream(new File(jl, "game.1.rsr")))) {
            byte[] d = "score=77".getBytes("US-ASCII");
            o.writeInt(1); o.writeInt(0); o.writeInt(d.length); o.write(d);
        }
        HeadlessRunner.Options o = new HeadlessRunner.Options();
        o.jar = DesktopTestSupport.midlet("rms");
        o.importJlMod = jl;
        // the first run of RmsA finds a store, so it takes the "later run" branch and reads the imported record
        HeadlessRunner.Result r = HeadlessRunner.run(o);
        assertNotNull(r.rmsImport);
        assertEquals(1, r.rmsImport.stores);
        assertEquals(r.rmsImport.problems.toString(), 0, r.rmsImport.problems.size());
        String out = DesktopTestSupport.onlyGuestLines(r.stdout);
        assertTrue(out, out.startsWith("> later run: stores=1 game records=1 nextId=2 version=4\n>   record 1 = score=77\n"));
    }
}
