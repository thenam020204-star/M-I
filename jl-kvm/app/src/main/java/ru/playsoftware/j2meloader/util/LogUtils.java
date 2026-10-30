package ru.playsoftware.j2meloader.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import ru.playsoftware.j2meloader.config.Config;

/**
 * "Save log" (PLAN T35b): {@code log.txt} = the {@code game-errors.log} of the last run (the VM's own filtered log: uncaught guest
 * exceptions with stack trace, link errors, OutOfMemoryError + heap, missing resources, undecodable media) + the logcat lines of THIS
 * process and of the app's own tags only. The old {@code logcat -t 500} dumped the whole system log: MIUIInput / InsetsController /
 * ViewRootImpl / Audio* lines on every touch, and no game error at all.
 */
public class LogUtils {
    /** logcat tags of the app (debug builds write the VM trace and the guest console to them); anything else is system noise. */
    public static final String[] APP_TAGS = {"jlkvm-vm", "jlkvm-guest", "VmSession", "AndroidRuntime"};

    private static volatile File gameErrors;

    /** The VM calls this when a run starts. */
    public static void setGameErrorsFile(File f) {
        gameErrors = f;
    }

    public static void writeLog() throws IOException {
        File logFile = new File(Config.getEmulatorDir(), "log.txt");
        if (logFile.exists() && !logFile.delete()) {
            throw new IOException("Can't replace " + logFile);
        }
        try (Writer w = new OutputStreamWriter(new FileOutputStream(logFile), StandardCharsets.UTF_8)) {
            File ge = gameErrors;
            w.write("== " + Config.GAME_ERRORS_LOG + " ==\n");
            if (ge != null && ge.isFile()) {
                try (InputStream in = new FileInputStream(ge)) {
                    byte[] b = new byte[8192];
                    for (int n; (n = in.read(b)) > 0; ) w.write(new String(b, 0, n, StandardCharsets.UTF_8));
                }
            } else {
                w.write("(no game has run)\n");
            }
            w.write("\n== logcat (pid " + android.os.Process.myPid() + ", app tags) ==\n");
            for (String line : filteredLogcat(500)) {
                w.write(line);
                w.write('\n');
            }
        }
    }

    /** Last {@code max} lines of this process, restricted to {@link #APP_TAGS}; empty when logcat is not readable. */
    static java.util.List<String> filteredLogcat(int max) {
        java.util.ArrayDeque<String> keep = new java.util.ArrayDeque<>();
        try {
            String[] cmd = new String[APP_TAGS.length + 5];
            cmd[0] = "logcat";
            cmd[1] = "-d";
            cmd[2] = "--pid=" + android.os.Process.myPid();
            cmd[3] = "-s";
            int i = 4;
            for (String t : APP_TAGS) cmd[i++] = t + ":V";
            Process p = new ProcessBuilder(java.util.Arrays.copyOf(cmd, i)).redirectErrorStream(true).start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
                for (String l; (l = r.readLine()) != null; ) {
                    if (l.startsWith("---")) continue;   // "--------- beginning of main"
                    keep.addLast(l);
                    if (keep.size() > max) keep.removeFirst();
                }
            }
        } catch (IOException | RuntimeException ignored) {
            // no logcat access: the game log alone is still the useful part
        }
        return new java.util.ArrayList<>(keep);
    }
}
