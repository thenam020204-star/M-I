package jlkvm.host.rms;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import jlkvm.DeviceProfile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/** T26 host side: byte accounting and quota, persistence in files (escaped names, atomic rewrite, broken files), JL-Mod import. */
public class RecordStorageTest {
    @Rule public TemporaryFolder tmp = new TemporaryFolder();

    private static DeviceProfile profile(int quota, int storeOv, int recOv) {
        DeviceProfile p = new DeviceProfile();
        p.rmsQuotaBytes = quota; p.rmsStoreOverheadBytes = storeOv; p.rmsRecordOverheadBytes = recOv;
        return p;
    }

    @Test public void accountingAndQuota() throws Exception {
        RecordStorage st = new MemoryRecordStorage(profile(1000, 16, 8));
        RecordStorage.Store s = st.create("S", "a", 0, true, 5);
        assertEquals(16, st.size(s));
        assertEquals(984, st.available("S"));
        byte[] d = new byte[100];
        int n = 0;
        try { for (;;) { st.add("S", s, d, 0, 100, 6); n++; } } catch (RecordStorage.Full e) { }
        assertEquals(9, n);
        assertEquals(16 + 9 * 108, st.size(s));
        assertEquals(12, st.available("S"));
        assertEquals("a failed add changes nothing", 9, s.numRecords());
        assertEquals(10, s.nextId());
        // the quota is per suite: another suite has its own
        assertEquals(1000, st.available("T"));
        // grow within the room, then one byte too much
        assertTrue(st.set("S", s, 1, new byte[112], 0, 112, 7));
        try { st.set("S", s, 1, new byte[113], 0, 113, 8); fail(); } catch (RecordStorage.Full e) { }
        assertEquals(112, s.get(1).length);
        assertFalse(st.set("S", s, 99, d, 0, 1, 9));
        // a second store needs its overhead
        try { st.create("S", "b", 0, true, 9); fail(); } catch (RecordStorage.Full e) { }
        assertTrue(st.remove("S", s, 1, 10));
        assertFalse(st.remove("S", s, 1, 10));
        assertNotNull(st.create("S", "b", 0, true, 11));
        assertEquals(Arrays.asList("a", "b"), st.list("S"));
        assertTrue(st.delete("S", "a"));
        assertFalse(st.delete("S", "a"));
        assertEquals(984, st.available("S"));
        assertTrue("version counts the changes", s.version() >= 10);
    }

    @Test public void filesSurviveANewObject() throws Exception {
        File dir = tmp.newFolder("rms");
        DeviceProfile p = profile(100000, 16, 8);
        FileRecordStorage a = new FileRecordStorage(dir, p);
        String name = "Mix/ed: é中 name";
        RecordStorage.Store s = a.create("Vendor\nSuite", name, 1, false, 1234L);
        a.add("Vendor\nSuite", s, new byte[] {1, 2, 3}, 0, 3, 2000L);
        a.add("Vendor\nSuite", s, new byte[] {9}, 0, 0, 2001L);
        int id3 = a.add("Vendor\nSuite", s, new byte[] {4, 5}, 0, 2, 2002L);
        a.remove("Vendor\nSuite", s, 2, 2003L);
        a.create("Vendor\nSuite", "plain", 0, true, 1L);
        a.create("Other", "plain", 0, true, 1L);

        FileRecordStorage b = new FileRecordStorage(dir, p);
        assertEquals(Arrays.asList("Mix/ed: é中 name", "plain"), b.list("Vendor\nSuite"));
        RecordStorage.Store r = b.find("Vendor\nSuite", name);
        assertNotNull(r);
        assertEquals(1, r.authmode());
        assertFalse(r.writable());
        assertEquals(2, r.numRecords());
        assertArrayEquals(new int[] {1, 3}, r.ids());
        assertArrayEquals(new byte[] {4, 5}, r.get(id3));
        assertEquals("ids are never reused after a restart", 4, r.nextId());
        assertEquals(2003L, r.lastModified());
        assertEquals(a.size(s), b.size(r));
        assertEquals(Arrays.asList("plain"), b.list("Other"));
        assertTrue(b.problems.toString(), b.problems.isEmpty());
        assertTrue(b.delete("Vendor\nSuite", name));
        assertEquals(Arrays.asList("plain"), new FileRecordStorage(dir, p).list("Vendor\nSuite"));
        for (File f : new File(dir, "Vendor%000ASuite").listFiles()) assertFalse("no temporary file left: " + f, f.getName().endsWith(".tmp"));
    }

    @Test public void brokenFileIsSkipped() throws Exception {
        File dir = tmp.newFolder("rms2");
        DeviceProfile p = profile(100000, 16, 8);
        FileRecordStorage a = new FileRecordStorage(dir, p);
        a.create("S", "good", 0, true, 1L);
        a.create("S", "bad", 0, true, 1L);
        File bad = new File(new File(dir, "S"), "bad.rms");
        Files.write(bad.toPath(), new byte[] {1, 2, 3});
        FileRecordStorage b = new FileRecordStorage(dir, p);
        assertEquals(Arrays.asList("good"), b.list("S"));
        assertEquals(1, b.problems.size());
    }

    @Test public void importsJlModStores() throws Exception {
        File dir = tmp.newFolder("jlmod");
        // layout of AndroidRecordStoreManager: <name>.rsh, <name>.<id>.rsr (written here exactly as RecordStoreImpl.writeHeader / writeRecord do)
        try (DataOutputStream o = new DataOutputStream(new FileOutputStream(new File(dir, "Save.rsh")))) {
            o.write("MIDRMS".getBytes("US-ASCII")); o.write(3); o.write(0); o.write(0);
            o.writeUTF("Save"); o.writeLong(777L); o.writeInt(5); o.writeInt(1); o.writeByte(1); o.writeInt(2); o.writeInt(7);
        }
        record(dir, "Save.1.rsr", 1, new byte[] {10, 20, 30});
        record(dir, "Save.7.rsr", 7, new byte[0]);
        record(dir, "Save.x.rsr", 0, new byte[0]);           // not a record file of "Save"
        try (DataOutputStream o = new DataOutputStream(new FileOutputStream(new File(dir, "Save.2.rsr")))) { o.writeInt(2); }   // truncated
        try (DataOutputStream o = new DataOutputStream(new FileOutputStream(new File(dir, "Other.rsh")))) {
            o.write("MIDRMS".getBytes("US-ASCII")); o.write(3); o.write(0); o.write(0);
            o.writeUTF("Other"); o.writeLong(1L); o.writeInt(0); o.writeInt(0); o.writeByte(0); o.writeInt(0);   // old header without lastRecordId
        }
        Files.write(new File(dir, "Junk.rsh").toPath(), new byte[] {1, 2});
        MemoryRecordStorage st = new MemoryRecordStorage(new DeviceProfile());
        JlModRmsImport.Result r = JlModRmsImport.importDir(dir, st, "S");
        assertEquals(2, r.stores);
        assertEquals(2, r.records);
        assertEquals(r.problems.toString(), 2, r.problems.size());   // truncated Save.2.rsr, Junk.rsh
        assertEquals(Arrays.asList("Other", "Save"), st.list("S"));
        RecordStorage.Store s = st.find("S", "Save");
        assertEquals(1, s.authmode());
        assertTrue(s.writable());
        assertEquals(5, s.version());
        assertEquals(777L, s.lastModified());
        assertArrayEquals(new int[] {1, 7}, s.ids());
        assertArrayEquals(new byte[] {10, 20, 30}, s.get(1));
        assertEquals(8, s.nextId());
        assertEquals(1, st.find("S", "Other").nextId());
    }

    private static void record(File dir, String file, int id, byte[] data) throws Exception {
        try (DataOutputStream o = new DataOutputStream(new FileOutputStream(new File(dir, file)))) {
            o.writeInt(id); o.writeInt(0); o.writeInt(data.length); o.write(data);
        }
    }
}
