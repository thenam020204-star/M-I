/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package javax.microedition.shell;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.microedition.lcdui.Display;
import javax.microedition.util.ContextHolder;

import jlkvm.host.GameErrorLog;
import jlkvm.host.android.AndroidAudioDevice;
import jlkvm.host.android.AndroidNokiaDevice;
import jlkvm.host.android.AndroidServices;
import jlkvm.host.ams.MidletSuite;
import jlkvm.host.desktop.VmRunner;
import ru.playsoftware.j2meloader.BuildConfig;
import ru.playsoftware.j2meloader.config.Config;

/**
 * One run of a game on the jl-kvm VM (PLAN T35, D1): the game JAR ({@code res.jar}) is interpreted in place on the {@code vm-thread}
 * of a {@link VmRunner}; this class only wires the Android side to it - the JL-Mod canvas stack ({@link VmCanvas}: surface / GLES /
 * skin / virtual keyboard), audio, vibration, the RMS and file directories of the game and the filtered {@code game-errors.log}
 * (T35b). Created and used from the UI thread, except the callbacks noted in {@link VmRunner}.
 */
final class VmSession {
    private static final String TAG = "VmSession";
    private static volatile VmSession current;

    private final MicroActivity activity;
    private final File appDir;
    private final String appName;
    private final String midletClass;
    private VmRunner runner;
    private VmCanvas canvas;
    private AndroidAudioDevice audio;
    private GameErrorLog errorLog;
    private File errorLogFile;

    VmSession(MicroActivity activity, File appDir, String appName, String midletClass) {
        this.activity = activity;
        this.appDir = appDir;
        this.appName = appName;
        this.midletClass = midletClass;
    }

    static VmSession current() {
        return current;
    }

    VmCanvas canvas() {
        return canvas;
    }

    /** Where this run writes its game-errors.log (overwritten at every start). */
    static File errorLogFile(String appDirName) {
        return new File(new File(Config.getDataDir(), appDirName), Config.GAME_ERRORS_LOG);
    }

    void start() throws IOException {
        Context ctx = activity.getApplicationContext();
        File jar = new File(appDir, Config.MIDLET_RES_FILE);
        if (!jar.isFile()) throw new IOException("Game JAR not found: " + jar);
        VmRunner.Options o = new VmRunner.Options();
        o.jar = jar;
        o.romDir = extractRom(ctx);
        o.profileJson = readAsset(ctx.getAssets(), "jlkvm/jlkvm-nokia.json");
        o.debug = BuildConfig.DEBUG;
        // guest console and VM trace only in the debug build (D15); release keeps nothing but game-errors.log
        if (BuildConfig.DEBUG) {
            o.out = new PrintStream(new LogcatStream("jlkvm-guest", Log.INFO), true);
            o.log = new PrintStream(new LogcatStream("jlkvm-vm", Log.DEBUG), true);
        }
        boolean vibration = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx)
                .getBoolean(ru.playsoftware.j2meloader.util.Constants.PREF_VIBRATION, true);
        o.manifest = readFile(Config.manifestFile(appDir));
        canvas = new VmCanvas(this);
        AndroidServices services = new AndroidServices(ctx, canvas);
        services.setVibration(vibration);
        o.services = services;
        audio = new AndroidAudioDevice(ctx);
        o.audio = audio;
        AndroidNokiaDevice nokia = new AndroidNokiaDevice(ctx);
        nokia.setVibration(vibration);
        o.nokia = nokia;
        File dataDir = new File(Config.getDataDir(), appDir.getName());
        //noinspection ResultOfMethodCallIgnored
        dataDir.mkdirs();
        jlkvm.DeviceProfile profile = jlkvm.profile.ProfileLoader.fromJson(o.profileJson);
        o.records = new jlkvm.host.rms.FileRecordStorage(new File(dataDir, "jlkvm-rms"), profile);
        o.fs = new jlkvm.host.fs.DirFs(new File(dataDir, "jlkvm-fs"), profile.fileRoots, profile.fileQuotaBytes);
        errorLogFile = errorLogFile(appDir.getName());
        errorLog = new GameErrorLog(new FileWriter(errorLogFile, false));
        ru.playsoftware.j2meloader.util.LogUtils.setGameErrorsFile(errorLogFile);
        o.errorLog = errorLog;
        o.midlet = midletNumber();
        runner = new VmRunner(o);
        runner.setListener(error -> activity.runOnUiThread(() -> {
            if (error != null) Log.e(TAG, "VM stopped by a host error", error);
            MidletThread.notifyDestroyed();
        }));
        current = this;
        canvas.attach(runner);
        runner.start();
        // the guest canvas has the size of the JL-Mod canvas (virtual screen, scaling and padding are the host's)
        runner.displaySize(canvas.getWidth(), canvas.getHeight());
        activity.setCurrent(canvas);
        canvas.doShowNotify();
    }

    private int midletNumber() throws IOException {
        File mf = Config.manifestFile(appDir);
        MidletSuite s = MidletSuite.load(readFile(mf), null);
        for (MidletSuite.Entry e : s.midlets) if (e.className.equals(midletClass)) return e.index;
        return 1;
    }

    void pause() {
        VmRunner r = runner;
        if (r == null || r.isFinished()) return;
        r.pause();
        if (audio != null) audio.pauseAll();
    }

    void resume() {
        VmRunner r = runner;
        if (r == null || r.isFinished()) return;
        r.resume();
        r.repaint();
    }

    /** The back key / "exit" menu / force close: MIDlet.destroyApp, then the process ends (same as JL-Mod). */
    void destroy(boolean unconditional) {
        VmRunner r = runner;
        if (r == null) return;
        r.destroy(unconditional);
    }

    /** Activity.onDestroy: stop the vm-thread and free the host objects. */
    void release() {
        VmRunner r = runner;
        runner = null;
        if (r != null) r.stop(true, 1500);
        if (audio != null) audio.releaseAll();
        if (errorLog != null) errorLog.close();
        if (current == this) current = null;
    }

    private File extractRom(Context ctx) throws IOException {
        File dir = new File(ctx.getFilesDir(), "jlkvm-rom");
        File stamp = new File(dir, ".stamp");
        String want = BuildConfig.VERSION_CODE + ":" + BuildConfig.BUILD_TYPE;
        AssetManager am = ctx.getAssets();
        String[] names = am.list("jlkvm/rom");
        if (names == null || names.length == 0) throw new IOException("ROM layers missing from the APK (assets/jlkvm/rom)");
        if (stamp.isFile() && want.equals(new String(readFile(stamp), "UTF-8"))) return dir;
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        for (String n : names) {
            try (InputStream in = am.open("jlkvm/rom/" + n); FileOutputStream out = new FileOutputStream(new File(dir, n))) {
                byte[] b = new byte[16384];
                for (int k; (k = in.read(b)) > 0; ) out.write(b, 0, k);
            }
        }
        try (FileOutputStream out = new FileOutputStream(stamp)) {
            out.write(want.getBytes("UTF-8"));
        }
        return dir;
    }

    private static String readAsset(AssetManager am, String path) throws IOException {
        try (InputStream in = am.open(path)) {
            return new String(readAll(in), "UTF-8");
        }
    }

    private static byte[] readFile(File f) throws IOException {
        try (InputStream in = new java.io.FileInputStream(f)) {
            return readAll(in);
        }
    }

    private static byte[] readAll(InputStream in) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] b = new byte[8192];
        for (int n; (n = in.read(b)) > 0; ) bo.write(b, 0, n);
        return bo.toByteArray();
    }

    /** Line-buffered stream into logcat; only created in the debug build. */
    private static final class LogcatStream extends java.io.OutputStream {
        private final String tag;
        private final int prio;
        private final StringBuilder line = new StringBuilder();

        LogcatStream(@NonNull String tag, int prio) {
            this.tag = tag;
            this.prio = prio;
        }

        @Override
        public synchronized void write(int b) {
            if (b == '\n') {
                Log.println(prio, tag, line.toString());
                line.setLength(0);
            } else if (b != '\r') {
                line.append((char) (b & 0xFF));
            }
        }
    }

    static void startDisplayQueue() {
        Display.initDisplay();
    }
}
