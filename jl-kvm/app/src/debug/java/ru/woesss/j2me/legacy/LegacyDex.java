package ru.woesss.j2me.legacy;

import java.io.File;
import java.io.IOException;

import com.android.dx.command.dexer.Main;

/**
 * Debug build only (PLAN D17): converts a game JAR to DEX for the old JL-Mod engine so that a game can run on both engines and be
 * compared (T35 A/B). Called by reflection from MicroLoader on the first launch with the dex engine; the release build has neither
 * this class nor dexlib.
 */
public final class LegacyDex {
    private LegacyDex() {
    }

    public static void convert(File jar, File outZip) throws IOException {
        try {
            Main.main(new String[]{"--no-optimize", "--output=" + outZip.getAbsolutePath(), jar.getAbsolutePath()});
        } catch (Throwable e) {
            throw new IOException("Dexing error", e);
        }
    }
}
