/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/** The J2SE linear congruential generator (48 bit seed): same sequence for the same seed on every platform. */
public class Random {
    private long seed;

    public Random() {
        this(System.currentTimeMillis());
    }

    public Random(long seed) {
        setSeed(seed);
    }

    public synchronized void setSeed(long seed) {
        this.seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
    }

    protected synchronized int next(int bits) {
        seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        return (int) (seed >>> (48 - bits));
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        if ((n & -n) == n) {
            return (int) ((n * (long) next(31)) >> 31);
        }
        int bits, val;
        do {
            bits = next(31);
            val = bits % n;
        } while (bits - val + (n - 1) < 0);
        return val;
    }

    public long nextLong() {
        return ((long) next(32) << 32) + next(32);
    }

    public float nextFloat() {
        return next(24) / ((float) (1 << 24));
    }

    public double nextDouble() {
        return (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
    }
}
