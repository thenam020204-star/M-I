package jlkvm.verify;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

import java.util.Map;
import java.util.Random;
import jlkvm.Corpus;
import jlkvm.classfile.ClassFile;
import jlkvm.classfile.ClassFormatException;
import jlkvm.classfile.ClassParser;
import org.junit.Test;

/** T08: byte-flipped preverified classes never crash the host: the verifier only answers accept or VerifyException. */
public class VerifierFuzzTest {
    @Test
    public void mutatedClassesNeverCrash() throws Exception {
        Map<String, byte[]> all = Corpus.allClasses();
        assumeTrue(!all.isEmpty());
        CorpusVerifyTest.MapSource stubs = new CorpusVerifyTest.MapSource();
        stubs.m.putAll(Corpus.classes(Corpus.stub("cldcapi11")));
        stubs.m.putAll(Corpus.classes(Corpus.stub("midpapi20")));
        byte[][] seeds = all.values().toArray(new byte[0][]);
        Random rnd = new Random(20261029L);
        int parsed = 0, accepted = 0, rejected = 0;
        for (int i = 0; i < 20000; i++) {
            byte[] b = seeds[rnd.nextInt(seeds.length)].clone();
            int flips = 1 + rnd.nextInt(3);
            for (int k = 0; k < flips; k++) b[rnd.nextInt(b.length)] = (byte) rnd.nextInt(256);
            ClassFile cf;
            try {
                cf = ClassParser.parse(b);
            } catch (ClassFormatException e) {
                continue;
            }
            parsed++;
            for (boolean strict : new boolean[] {true, false}) {
                try {
                    new Verifier(new SourceHierarchy(stubs), strict, true).verify(cf);
                    accepted++;
                } catch (VerifyException e) {
                    rejected++;
                } catch (Throwable t) {
                    throw new AssertionError("verifier crashed (strict=" + strict + ") on mutant " + i + ": " + t, t);
                }
            }
        }
        System.out.println("verifier fuzz: parsed=" + parsed + " verdicts accepted=" + accepted + " rejected=" + rejected);
        assertTrue(parsed > 1000);
    }

    /** Mutates the bytecode (and StackMap offsets) of parsed methods directly, so that every sample really reaches the verifier. */
    @Test
    public void mutatedBytecodeNeverCrashes() throws Exception {
        Map<String, byte[]> all = Corpus.allClasses();
        assumeTrue(!all.isEmpty());
        CorpusVerifyTest.MapSource stubs = new CorpusVerifyTest.MapSource();
        stubs.m.putAll(Corpus.classes(Corpus.stub("cldcapi11")));
        stubs.m.putAll(Corpus.classes(Corpus.stub("midpapi20")));
        java.util.List<ClassFile> cfs = new java.util.ArrayList<ClassFile>();
        for (byte[] b : all.values()) cfs.add(ClassParser.parse(b));
        Random rnd = new Random(77L);
        int n = 0, accepted = 0, rejected = 0;
        for (int i = 0; i < 40000; i++) {
            ClassFile cf = cfs.get(rnd.nextInt(cfs.size()));
            if (cf.methods.length == 0) continue;
            ClassFile.MethodInfo m = cf.methods[rnd.nextInt(cf.methods.length)];
            if (m.code == null) continue;
            byte[] orig = m.code.code.clone();
            ClassFile.StackMapEntry[] sm = m.code.stackMap;
            int[] savedOff = null;
            try {
                int flips = 1 + rnd.nextInt(3);
                for (int k = 0; k < flips; k++) m.code.code[rnd.nextInt(orig.length)] = (byte) (rnd.nextInt(4) == 0 ? rnd.nextInt(256) : orig[rnd.nextInt(orig.length)]);
                if (sm != null && sm.length > 0 && rnd.nextInt(4) == 0) {
                    savedOff = new int[sm.length];
                    for (int k = 0; k < sm.length; k++) savedOff[k] = sm[k].offset;
                    sm[rnd.nextInt(sm.length)].offset = rnd.nextInt(orig.length);
                }
                n++;
                for (boolean strict : new boolean[] {true, false}) {
                    try {
                        new Verifier(new SourceHierarchy(stubs), strict, true).verifyMethod(cf, m, rnd.nextBoolean());
                        accepted++;
                    } catch (VerifyException e) {
                        rejected++;
                    } catch (Throwable t) {
                        throw new AssertionError("verifier crashed (strict=" + strict + ") on " + cf.name + "." + m.name + m.descriptor + ": " + t, t);
                    }
                }
            } finally {
                System.arraycopy(orig, 0, m.code.code, 0, orig.length);
                if (savedOff != null) for (int k = 0; k < sm.length; k++) sm[k].offset = savedOff[k];
            }
        }
        System.out.println("bytecode fuzz: samples=" + n + " accepted=" + accepted + " rejected=" + rejected);
        assertTrue(n > 5000);
    }
}
