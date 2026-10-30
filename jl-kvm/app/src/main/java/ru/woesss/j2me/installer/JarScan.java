package ru.woesss.j2me.installer;

import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import jlkvm.tools.apisigs.ApiDb;
import jlkvm.tools.jarscan.Finding;
import jlkvm.tools.jarscan.Report;
import jlkvm.tools.jarscan.ScanProfile;
import jlkvm.tools.jarscan.Scanner;
import ru.playsoftware.j2meloader.EmulatorApplication;
import ru.playsoftware.j2meloader.config.Config;

/**
 * Runs {@code jarscan} (PLAN T04, T34) on a game JAR at install time: the same checks as the CLI (CLDC / MIDP / Nokia / JSR API surface,
 * class file version, StackMap, floating point, large arrays...) against the signature databases bundled as assets
 * ({@code jarscan/profile.json}, {@code jarscan/db/NAME.json}, copied from {@code tools/api-sigs/db} by the build).
 */
public final class JarScan {
    private static final String TAG = "JarScan";
    public static final String ASSET_DIR = "jarscan/";

    /** Where the profile and the databases come from (assets on a device, files in a unit test). */
    public interface Source {
        InputStream open(String path) throws IOException;
    }

    public static final class Result {
        public final boolean safe;
        public final int errors, warnings, infos;
        /** The full jarscan report, Markdown. */
        public final String markdown;
        /** A few lines for the installer dialog. */
        public final String summary;

        Result(boolean safe, int errors, int warnings, int infos, String markdown, String summary) {
            this.safe = safe;
            this.errors = errors;
            this.warnings = warnings;
            this.infos = infos;
            this.markdown = markdown;
            this.summary = summary;
        }
    }

    /** Unit tests (no assets in a plain JVM): where the databases come from instead of the APK. */
    static volatile Source testSource;

    private JarScan() {
    }

    public static Source assets(final AssetManager assets) {
        return new Source() {
            @Override
            public InputStream open(String path) throws IOException {
                return assets.open(path);
            }
        };
    }

    public static Result scan(File jar, Source source) throws IOException {
        ScanProfile profile;
        try (Reader r = new InputStreamReader(source.open(ASSET_DIR + "profile.json"), StandardCharsets.UTF_8)) {
            profile = ScanProfile.read(r);
        }
        List<ApiDb> dbs = new ArrayList<>();
        for (String api : profile.apis) {
            try (Reader r = new InputStreamReader(source.open(ASSET_DIR + "db/" + api + ".json"), StandardCharsets.UTF_8)) {
                dbs.add(ApiDb.read(r));
            }
        }
        Scanner scanner = new Scanner(profile, dbs);
        scanner.scan(jar);
        int e = 0, w = 0, i = 0;
        for (Finding f : scanner.findings()) {
            if (f.level == Finding.Level.ERROR) e++;
            else if (f.level == Finding.Level.WARN) w++;
            else i++;
        }
        boolean safe = !scanner.hasErrors();
        StringBuilder sum = new StringBuilder();
        sum.append(safe ? "jarscan: OK" : "jarscan: NOT J2ME-SAFE").append(" - ")
                .append(e).append(" error, ").append(w).append(" warning, ").append(i).append(" info\n");
        int shown = 0;
        outer:
        for (Finding.Level lv : new Finding.Level[]{Finding.Level.ERROR, Finding.Level.WARN}) {
            for (Finding f : scanner.findings()) {
                if (f.level != lv) continue;
                if (shown >= 25) break outer;
                shown++;
                sum.append(lv == Finding.Level.ERROR ? "ERROR " : "WARN ").append(f.code).append(' ')
                        .append(f.where).append(": ").append(f.message).append('\n');
            }
        }
        if (e + w > shown) {
            sum.append("... (").append(e + w - shown).append(" more in jarscan-report.md)\n");
        }
        return new Result(safe, e, w, i, Report.markdown(jar.getName(), profile, scanner), sum.toString());
    }

    /**
     * Scans {@code jar} with the bundled databases and writes the Markdown report to {@code out}.
     *
     * @return the dialog summary, or {@code null} when the scan itself failed (the install does not fail because of it)
     */
    @Nullable
    public static String scanToFile(@NonNull File jar, @NonNull File out) {
        try {
            Source src = testSource != null ? testSource : assets(EmulatorApplication.getInstance().getAssets());
            Result r = scan(jar, src);
            try (Writer w = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8)) {
                w.write(r.markdown);
            }
            return r.summary;
        } catch (Throwable t) {
            Log.w(TAG, "jarscan failed for " + jar, t);
            return null;
        }
    }

    /** The saved report of an installed game, null when absent. */
    @Nullable
    public static String readReport(@NonNull File appDir) {
        File f = new File(appDir, Config.MIDLET_SCAN_REPORT_FILE);
        if (!f.isFile()) return null;
        try (InputStream in = new FileInputStream(f)) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) > 0; ) bo.write(b, 0, n);
            return bo.toString("UTF-8");
        } catch (IOException e) {
            return null;
        }
    }
}
