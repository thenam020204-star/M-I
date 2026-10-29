package selftest;

import jlkvm.test.TestMIDlet;

/** Smoke test of the test framework itself: must print PASS x3 and DONE pass=3 fail=0 on every oracle. */
public class SelfTestMIDlet extends TestMIDlet {
    protected void runTests() throws Exception {
        check("true-is-true", true);
        eq("int-eq", 4, 2 + 2);
        throwsExactly("npe", "java.lang.NullPointerException", new Runnable() {
            public void run() {
                Object o = null;
                o.hashCode();
            }
        });
    }
}
