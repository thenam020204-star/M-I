package jlkvm.host.desktop;

/** Time source of the desktop host. */
public interface Clock {
    long nanos();

    /** Wall clock. */
    final class Real implements Clock {
        public long nanos() { return System.nanoTime(); }
    }

    /** Test clock: only moves when told to (the deterministic VM keeps its own virtual time on top of it). */
    final class Virtual implements Clock {
        private long now = 1000000000L;
        public long nanos() { return now; }
        public void advance(long ns) { now += ns; }
    }
}
