/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

/** Package-private: unique ids of screens, items and commands in the host screen protocol (docs/midp.md section 6). */
final class Ids {
    private static int last;

    private Ids() {
    }

    static synchronized int next() {
        return ++last;
    }
}
