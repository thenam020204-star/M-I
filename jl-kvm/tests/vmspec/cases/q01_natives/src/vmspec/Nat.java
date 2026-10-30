package vmspec;

/** Natives implemented by the test host (key "vmspec/Nat.name(descriptor)"). */
class Nat {
    static native int add(int a, int b);
    static native double mix(int a, long b, float c, double d);
    static native long mixl(int a, long b);
    static native int fill(byte[] a, int v);
    static native void fail(String s);
    static native String newString(int i);
    static native int blockFill(int ms, byte[] a);
    static native void blockThrow(int polls);
    static native long blockLong(long v);
    static native void nothing();   // no host registers this one
    }
