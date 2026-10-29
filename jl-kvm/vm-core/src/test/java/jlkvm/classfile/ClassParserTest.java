package jlkvm.classfile;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.util.Map;
import java.util.Random;
import jlkvm.Corpus;
import org.junit.Test;

public class ClassParserTest {
    @Test
    public void parsesWholeCorpus() throws Exception {
        Map<String, byte[]> all = Corpus.allClasses();
        assumeTrue("run tests/vmspec/run_vmspec.sh and tools/env/setup_toolchain.sh first", !all.isEmpty());
        int ok = 0;
        for (Map.Entry<String, byte[]> e : all.entrySet()) {
            ClassFile c;
            try {
                c = ClassParser.parse(e.getValue());
            } catch (ClassFormatException ex) {
                // raw negative-test classes are still well formed; nothing in the corpus may be rejected
                throw new AssertionError(e.getKey() + ": " + ex.getMessage());
            }
            assertNotNull(c.name);
            ok++;
        }
        System.out.println("parsed " + ok + " classes (100%)");
        assertTrue(ok > 100);
    }

    @Test
    public void readsStackMapAndCode() throws Exception {
        byte[] b = Corpus.classes(Corpus.vmspecJars().values().iterator().next()).values().iterator().next();
        ClassFile c = ClassParser.parse(b);
        boolean sawCode = false;
        for (ClassFile.MethodInfo m : c.methods) if (m.code != null) { sawCode = true; assertTrue(m.code.code.length > 0); }
        assertTrue(sawCode);
    }

    @Test
    public void rejectsGarbage() {
        byte[][] bad = {new byte[0], new byte[] {1, 2, 3}, new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE},
                new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, 0, 0, 0, 50, 0, 1}};
        for (byte[] b : bad) {
            try {
                ClassParser.parse(b);
                fail();
            } catch (ClassFormatException expected) {
                assertEquals("java/lang/ClassFormatError", ClassFormatException.GUEST_CLASS);
            }
        }
    }

    /** T07: 10k truncated / byte-flipped samples never crash the host; only ClassFormatException is allowed. */
    @Test
    public void fuzz10k() throws Exception {
        Map<String, byte[]> all = Corpus.allClasses();
        assumeTrue(!all.isEmpty());
        byte[][] seeds = all.values().toArray(new byte[0][]);
        Random r = new Random(20261029L);
        int rejected = 0, accepted = 0;
        for (int i = 0; i < 10000; i++) {
            byte[] s = seeds[r.nextInt(seeds.length)].clone();
            switch (r.nextInt(5)) {
                case 0: // truncate
                    s = java.util.Arrays.copyOf(s, r.nextInt(s.length));
                    break;
                case 1: // flip a few bytes
                    for (int k = 1 + r.nextInt(4); k > 0; k--) s[r.nextInt(s.length)] = (byte) r.nextInt(256);
                    break;
                case 2: // swap two bytes
                    int a = r.nextInt(s.length), b = r.nextInt(s.length);
                    byte t = s[a]; s[a] = s[b]; s[b] = t;
                    break;
                case 3: // damage the header area (constant pool counts, lengths)
                    for (int k = 0; k < 3; k++) s[8 + r.nextInt(Math.min(64, s.length - 8))] = (byte) r.nextInt(256);
                    break;
                default: // extend
                    s = java.util.Arrays.copyOf(s, s.length + 1 + r.nextInt(8));
            }
            try {
                ClassParser.parse(s);
                accepted++;
            } catch (ClassFormatException e) {
                rejected++;
            } catch (Throwable t) {
                throw new AssertionError("sample " + i + " crashed the host: " + t, t);
            }
        }
        System.out.println("fuzz: rejected=" + rejected + " accepted=" + accepted);
        assertTrue(rejected > 1000);
    }
}
