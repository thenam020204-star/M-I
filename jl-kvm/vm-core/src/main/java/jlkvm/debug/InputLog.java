package jlkvm.debug;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;

/**
 * D14 record / replay of the input events (deterministic mode only: the stamps are VIRTUAL nanoseconds). Recording writes one
 * line "nanos type a b c" per event at the moment the guest takes it from the queue; replaying hands the same events to the
 * guest at the same virtual time (the host's live events are dropped), so the run is reproduced exactly.
 */
public final class InputLog {
    private PrintWriter out;
    private final ArrayDeque<long[]> replay = new ArrayDeque<long[]>();
    private final boolean replaying;

    public InputLog(String recordPath, String replayPath) {
        try {
            if (replayPath != null) {
                BufferedReader r = new BufferedReader(new FileReader(replayPath));
                for (String l; (l = r.readLine()) != null; ) {
                    l = l.trim();
                    if (l.length() == 0 || l.charAt(0) == '#') continue;
                    String[] f = l.split("\\s+");
                    replay.add(new long[] {Long.parseLong(f[0]), Long.parseLong(f[1]), Long.parseLong(f[2]), Long.parseLong(f[3]), Long.parseLong(f[4])});
                }
                r.close();
            }
            if (recordPath != null) out = new PrintWriter(new FileWriter(recordPath));
        } catch (IOException e) {
            throw new IllegalArgumentException("input log: " + e);
        }
        replaying = replayPath != null;
    }

    public boolean replaying() { return replaying; }

    /** Virtual time of the next event to replay, Long.MAX_VALUE when none (the scheduler jumps the virtual clock there when idle). */
    public long nextReplayNanos() { return replay.isEmpty() ? Long.MAX_VALUE : replay.peek()[0]; }

    /** The event the guest takes now ({@code live} = head of the host queue, consumed only when recording). */
    public int[] take(long nowNanos, int[] live) {
        if (replaying) {
            long[] e = replay.peek();
            if (e == null || e[0] > nowNanos) return null;
            replay.poll();
            return new int[] {(int) e[1], (int) e[2], (int) e[3], (int) e[4]};
        }
        if (live != null && out != null) {
            out.println(nowNanos + " " + live[0] + " " + live[1] + " " + live[2] + " " + live[3]);
            out.flush();
        }
        return live;
    }

    public void close() { if (out != null) out.close(); }
}
