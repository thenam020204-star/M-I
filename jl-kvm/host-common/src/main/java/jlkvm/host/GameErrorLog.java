package jlkvm.host;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code game-errors.log} (PLAN T35b): the short log of one run, filtered from {@code HostPlatform.log}. It keeps ONLY what helps to
 * find why a game fails, and never system noise or input:
 * <ul>
 *   <li>{@code vm} level 3 - an uncaught guest exception: class, message and the guest stack trace (class.method, pc, line when known);</li>
 *   <li>{@code link} - NoClassDefFoundError / NoSuchMethodError / NoSuchFieldError / VerifyError raised by the VM;</li>
 *   <li>{@code oom} - OutOfMemoryError with the heap numbers;</li>
 *   <li>{@code resource} - a resource not found after every valid path was tried (one line per resource);</li>
 *   <li>{@code media} - an image / sound that cannot be decoded (size, MIME).</li>
 * </ul>
 * Everything else (debug tags, key / touch / frame events - those go to the D14 record file) is dropped. The same message is written once.
 * At most {@link #MAX_LINES} lines are kept so a runaway game cannot fill the storage. Thread safe.
 */
public final class GameErrorLog {
    public static final int MAX_LINES = 2000;
    private static final int MAX_MESSAGE = 6000;

    private final Writer out;
    private final Set<String> seen = new HashSet<String>();
    private int lines;
    private boolean full;
    private final long start = System.nanoTime();

    /** @param out where the lines go (closed by {@link #close}); null = keep nothing */
    public GameErrorLog(Writer out) { this.out = out; }

    /** Whether {@code HostPlatform.log(level, tag, ...)} belongs in the file. */
    public static boolean accepts(int level, String tag) {
        if (tag == null) return false;
        if (tag.equals("vm")) return level >= 3;
        // level 1 of the linker is the debug trace "loaded Foo" (D15): not an error
        return level >= 2 && (tag.equals("link") || tag.equals("oom") || tag.equals("resource") || tag.equals("media") || tag.equals("profile"));
    }

    /** First lines: the device profile and the game. Not filtered. */
    public synchronized void header(String text) { write("[profile] " + text, false); }

    public synchronized void log(int level, String tag, String msg) {
        if (!accepts(level, tag)) return;
        if (msg == null) msg = "";
        if (msg.length() > MAX_MESSAGE) msg = msg.substring(0, MAX_MESSAGE) + "...";
        write("[" + (tag.equals("vm") ? "exception" : tag) + "] " + msg, true);
    }

    private void write(String text, boolean once) {
        if (out == null) return;
        if (once && !seen.add(text)) return;
        if (lines >= MAX_LINES) {
            if (!full) { full = true; put("[log] limit of " + MAX_LINES + " lines reached, the rest is dropped"); }
            return;
        }
        put(text);
    }

    private void put(String text) {
        try {
            long ms = (System.nanoTime() - start) / 1000000L;
            out.write("+" + ms + "ms " + text.replace("\r", "") + "\n");
            out.flush();
            lines += 1 + countNewlines(text);
        } catch (IOException ignored) {
            // the log must never break the game
        }
    }

    private static int countNewlines(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '\n') n++;
        return n;
    }

    public synchronized int lineCount() { return lines; }

    public synchronized void close() {
        if (out == null) return;
        try { out.close(); } catch (IOException ignored) { }
    }
}
