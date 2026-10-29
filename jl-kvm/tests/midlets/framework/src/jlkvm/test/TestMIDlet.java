package jlkvm.test;

import javax.microedition.midlet.MIDlet;

/**
 * Base class of the jl-kvm API test MIDlets (PLAN T06). A test MIDlet extends this class and implements
 * {@link #runTests()}. Output protocol (stdout, one record per line, compared by the difftest harness):
 *   "> PASS name" / "> FAIL name expected=.. actual=.." for every check, and a final "> DONE pass=N fail=M".
 * Lines starting with "# " are informational and ignored by comparisons.
 * Only CLDC 1.1 / MIDP 2.0 API is used so the class also runs on third-party oracles.
 */
public abstract class TestMIDlet extends MIDlet {
    private int pass, fail;

    /** Implement the checks here; runs once on the thread that called startApp(). */
    protected abstract void runTests() throws Exception;

    protected void startApp() {
        try {
            runTests();
        } catch (Throwable t) {
            fail++;
            System.out.println("> FAIL uncaught " + t.getClass().getName());
        }
        System.out.println("> DONE pass=" + pass + " fail=" + fail);
        notifyDestroyed();
    }

    protected void pauseApp() { }

    protected void destroyApp(boolean unconditional) { }

    protected final void check(String name, boolean cond) {
        if (cond) {
            pass++;
            System.out.println("> PASS " + name);
        } else {
            fail++;
            System.out.println("> FAIL " + name);
        }
    }

    protected final void eq(String name, int expected, int actual) {
        if (expected == actual) {
            pass++;
            System.out.println("> PASS " + name);
        } else {
            fail++;
            System.out.println("> FAIL " + name + " expected=" + expected + " actual=" + actual);
        }
    }

    protected final void eq(String name, Object expected, Object actual) {
        boolean ok = expected == null ? actual == null : expected.equals(actual);
        if (ok) {
            pass++;
            System.out.println("> PASS " + name);
        } else {
            fail++;
            System.out.println("> FAIL " + name + " expected=" + expected + " actual=" + actual);
        }
    }

    /** Expects the runnable to throw an exception whose class name equals {@code exceptionClass}. */
    protected final void throwsExactly(String name, String exceptionClass, Runnable r) {
        String got = "none";
        try {
            r.run();
        } catch (Throwable t) {
            got = t.getClass().getName();
        }
        eq(name, exceptionClass, got);
    }

    protected final void info(String s) {
        System.out.println("# " + s);
    }
}
