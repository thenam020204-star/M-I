package jlkvm.host.desktop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jlkvm.host.HostEvents;

/**
 * Scripted keyboard / pointer / life cycle input (the *.inp format of tests/games/README.md). Times are virtual milliseconds from the
 * start of the VM. {@code key tap C [hold]} expands to press + release; {@code key hold C ms} to press, repeats (every
 * {@code repeatInterval} after {@code repeatDelay}, the profile's key repeat timing) and release. Extra commands for tests:
 * {@code ui ...} (high-level UI actions, see {@link HeadlessUi}), {@code audio end N} (the headless audio device finishes clip N, T27), {@code destroy [0|1]} (AMS destroyApp request), {@code size W H}, {@code repaint}. Entries with the same time keep file order.
 */
public final class ScriptedInput {
    public static final int SHOT = 100, QUIT = 101, UI = 102, AUDIO = 103;

    public static final class Entry {
        public final long ms;
        public final int type, a, b;
        public final String name;
        Entry(long ms, int type, int a, int b, String name) { this.ms = ms; this.type = type; this.a = a; this.b = b; this.name = name; }
        @Override public String toString() { return ms + " type=" + type + " " + a + " " + b + (name == null ? "" : " " + name); }
    }

    public final List<Entry> entries = new ArrayList<Entry>();
    private int next;

    public static ScriptedInput parse(String text, int repeatDelayMs, int repeatIntervalMs) throws IOException {
        ScriptedInput s = new ScriptedInput();
        int ln = 0;
        for (String line : text.split("\r\n|\r|\n")) {
            ln++;
            int h = line.indexOf('#');
            if (h >= 0) line = line.substring(0, h);
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] t = line.split("\\s+");
            try {
                long ms = Long.parseLong(t[0]);
                String cmd = t[1];
                if (cmd.equals("key")) {
                    int code = Integer.parseInt(t[3]);
                    String k = t[2];
                    if (k.equals("press")) s.add(ms, HostEvents.KEY_PRESSED, code, 0, null);
                    else if (k.equals("release")) s.add(ms, HostEvents.KEY_RELEASED, code, 0, null);
                    else if (k.equals("repeat")) s.add(ms, HostEvents.KEY_REPEATED, code, 0, null);
                    else if (k.equals("tap")) {
                        long hold = t.length > 4 ? Long.parseLong(t[4]) : 80;
                        s.add(ms, HostEvents.KEY_PRESSED, code, 0, null);
                        s.add(ms + hold, HostEvents.KEY_RELEASED, code, 0, null);
                    } else if (k.equals("hold")) {
                        long hold = Long.parseLong(t[4]);
                        s.add(ms, HostEvents.KEY_PRESSED, code, 0, null);
                        for (long r = repeatDelayMs; r < hold; r += repeatIntervalMs) s.add(ms + r, HostEvents.KEY_REPEATED, code, 0, null);
                        s.add(ms + hold, HostEvents.KEY_RELEASED, code, 0, null);
                    } else throw new IllegalArgumentException("key " + k);
                } else if (cmd.equals("ptr")) {
                    int type = t[2].equals("press") ? HostEvents.POINTER_PRESSED : t[2].equals("drag") ? HostEvents.POINTER_DRAGGED : HostEvents.POINTER_RELEASED;
                    s.add(ms, type, Integer.parseInt(t[3]), Integer.parseInt(t[4]), null);
                } else if (cmd.equals("pause")) s.add(ms, HostEvents.PAUSE, 0, 0, null);
                else if (cmd.equals("resume")) s.add(ms, HostEvents.RESUME, 0, 0, null);
                else if (cmd.equals("destroy")) s.add(ms, HostEvents.DESTROY, t.length > 2 ? Integer.parseInt(t[2]) : 1, 0, null);
                else if (cmd.equals("size")) s.add(ms, HostEvents.DISPLAY_SIZE, Integer.parseInt(t[2]), Integer.parseInt(t[3]), null);
                else if (cmd.equals("repaint")) s.add(ms, HostEvents.REPAINT_ALL, 0, 0, null);
                else if (cmd.equals("shot")) s.add(ms, SHOT, 0, 0, t[2]);
                else if (cmd.equals("ui")) s.add(ms, UI, 0, 0, line.trim().substring(line.trim().indexOf("ui") + 2).trim());
                else if (cmd.equals("audio")) s.add(ms, AUDIO, 0, 0, line.trim().substring(line.trim().indexOf("audio") + 5).trim());
                else if (cmd.equals("quit")) s.add(ms, QUIT, 0, 0, null);
                else throw new IllegalArgumentException("command " + cmd);
            } catch (RuntimeException e) {
                throw new IOException("script line " + ln + ": " + e.getMessage() + " in '" + line + "'");
            }
        }
        // stable sort by time (file order kept for equal times)
        Collections.sort(s.entries, new Comparator<Entry>() { public int compare(Entry x, Entry y) { return x.ms < y.ms ? -1 : x.ms > y.ms ? 1 : 0; } });
        return s;
    }

    private void add(long ms, int type, int a, int b, String name) { entries.add(new Entry(ms, type, a, b, name)); }

    /** Next undelivered entry, or null. */
    public Entry peek() { return next < entries.size() ? entries.get(next) : null; }

    public Entry take() { return entries.get(next++); }
}
