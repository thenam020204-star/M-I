package jlkvm.host.ams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jlkvm.DeviceProfile;

/**
 * Nokia JAD attributes (PLAN T30) turned into decisions the host acts on. Pure data: a desktop or Android host reads the {@link Plan}
 * (what size the game sees, where the frame goes on the physical screen, which virtual keys to show, orientation lock).
 * <p>Sources (docs/nokia.md section "System properties and JAD attributes"): {@code Nokia-MIDlet-Category} is in the S40 DP2.0 SDK 1.1
 * (User Guide "About MIDP MIDlets"; the emulator's JAD parser lists it); the display-size, keypad and orientation attributes are NOT in that
 * SDK: their names and the semantics below are UNVERIFIED and acted on only when the profile lists them in {@code jad.honored}.
 * An attribute that is present but not honored, or has a bad value, is ignored and reported in {@link Plan#ignored} (never an error:
 * a device installs a MIDlet whose unknown attributes it does not understand).
 */
public final class NokiaJad {
    private NokiaJad() {}

    public static final String CATEGORY = "Nokia-MIDlet-Category", ORIGINAL_SIZE = "Nokia-MIDlet-Original-Display-Size",
            TARGET_SIZE = "Nokia-MIDlet-Target-Display-Size", KEYPAD = "Nokia-MIDlet-On-Screen-Keypad", ORIENTATION = "Nokia-MIDlet-App-Orientation";

    public static final class Plan {
        /** Raw category or null; {@link #isGame} is true for "Games" (User Guide: only that string selects the Games folder). */
        public String category;
        public boolean isGame;
        /** Full-screen size and normal-Canvas size the guest sees (profile screen/canvas, or derived from Original-Display-Size). */
        public int screenW, screenH, canvasW, canvasH;
        /** Rectangle on the physical screen (size of the frame scaled to fit the target box, centered in it); scale 1 = unscaled. */
        public int presentX, presentY, presentW, presentH;
        public double scale = 1.0;
        /** "no" | "gameactions" | "navigationkeys" or null (host default). */
        public String keypad;
        /** Names of profile keys the host should draw as touch buttons for {@link #keypad} (empty for "no"/null). */
        public final List<String> virtualKeys = new ArrayList<String>();
        /** "portrait" | "landscape" or null (free). */
        public String orientation;
        public final List<String> ignored = new ArrayList<String>();
    }

    private static boolean honored(DeviceProfile p, String name, Map<String, String> a, Plan out) {
        if (a.get(name) == null) return false;
        if (p.jadHonored.contains(name)) return true;
        out.ignored.add(name + ": not honored by profile " + p.name);
        return false;
    }

    /** "w,h" with 1 <= w,h <= 4096, else null. */
    static int[] size(String v) {
        if (v == null) return null;
        String[] t = v.split(",", -1);
        if (t.length != 2) return null;
        try {
            int w = Integer.parseInt(t[0].trim()), h = Integer.parseInt(t[1].trim());
            return w >= 1 && w <= 4096 && h >= 1 && h <= 4096 ? new int[] {w, h} : null;
        } catch (NumberFormatException e) { return null; }
    }

    public static Plan plan(DeviceProfile p, Map<String, String> a) {
        Plan r = new Plan();
        r.screenW = p.screenWidth; r.screenH = p.screenHeight; r.canvasW = p.canvasWidth; r.canvasH = p.canvasHeight;
        if (honored(p, CATEGORY, a, r)) {
            r.category = a.get(CATEGORY).trim();
            r.isGame = r.category.equals("Games");
        }
        if (honored(p, ORIENTATION, a, r)) {
            String v = a.get(ORIENTATION).trim().toLowerCase();
            if (v.equals("portrait") || v.equals("landscape")) r.orientation = v; else r.ignored.add(ORIENTATION + ": bad value '" + a.get(ORIENTATION) + "'");
        }
        if (honored(p, KEYPAD, a, r)) {
            String v = a.get(KEYPAD).trim().toLowerCase();
            if (v.equals("no")) r.keypad = v;
            else if (v.equals("gameactions")) { r.keypad = v; add(r, "up", "down", "left", "right", "select"); }
            else if (v.equals("navigationkeys")) { r.keypad = v; add(r, "up", "down", "left", "right", "select", "softLeft", "softRight"); }
            else r.ignored.add(KEYPAD + ": bad value '" + a.get(KEYPAD) + "'");
        }
        // the physical box: profile screen, swapped when the orientation lock asks the other way round
        int boxW = p.screenWidth, boxH = p.screenHeight;
        if (r.orientation != null && (r.orientation.equals("landscape") != (boxW > boxH)) && boxW != boxH) {
            boxW = p.screenHeight; boxH = p.screenWidth;
            r.screenW = p.screenHeight; r.screenH = p.screenWidth; r.canvasW = p.canvasHeight; r.canvasH = p.canvasWidth;   // the device turns its frame
        }
        int[] orig = honored(p, ORIGINAL_SIZE, a, r) ? size(a.get(ORIGINAL_SIZE)) : null;
        if (a.get(ORIGINAL_SIZE) != null && orig == null && p.jadHonored.contains(ORIGINAL_SIZE)) r.ignored.add(ORIGINAL_SIZE + ": bad value '" + a.get(ORIGINAL_SIZE) + "'");
        int[] tgt = honored(p, TARGET_SIZE, a, r) ? size(a.get(TARGET_SIZE)) : null;
        if (a.get(TARGET_SIZE) != null && tgt == null && p.jadHonored.contains(TARGET_SIZE)) r.ignored.add(TARGET_SIZE + ": bad value '" + a.get(TARGET_SIZE) + "'");
        if (tgt != null && orig == null) { r.ignored.add(TARGET_SIZE + ": needs a valid " + ORIGINAL_SIZE); tgt = null; }
        int frameW = r.screenW, frameH = r.screenH;
        if (orig != null) {
            int chromeW = p.screenWidth - p.canvasWidth, chromeH = p.screenHeight - p.canvasHeight;
            r.screenW = frameW = orig[0]; r.screenH = frameH = orig[1];
            r.canvasW = Math.max(1, orig[0] - chromeW); r.canvasH = Math.max(1, orig[1] - chromeH);
            if (tgt != null) { boxW = tgt[0]; boxH = tgt[1]; }
        }
        double sx = (double) boxW / frameW, sy = (double) boxH / frameH;
        r.scale = orig == null && tgt == null && boxW == frameW && boxH == frameH ? 1.0 : Math.min(sx, sy);
        r.presentW = (int) Math.round(frameW * r.scale); r.presentH = (int) Math.round(frameH * r.scale);
        r.presentX = (boxW - r.presentW) / 2; r.presentY = (boxH - r.presentH) / 2;
        return r;
    }

    /** Applies the sizes of a plan to the profile (what Canvas.getWidth/Height and Display report to the guest). */
    public static void apply(DeviceProfile p, Plan r) {
        p.screenWidth = r.screenW; p.screenHeight = r.screenH; p.canvasWidth = r.canvasW; p.canvasHeight = r.canvasH;
    }

    /** Install-time check against the device limits: the error text (suite is refused) or null. */
    public static String checkInstall(DeviceProfile p, Map<String, String> a) {
        String s = a.get(Descriptor.MIDLET_JAR_SIZE);
        if (s == null || p.jarMaxBytes <= 0) return null;
        try {
            long n = Long.parseLong(s.trim());
            if (n > p.jarMaxBytes) return "MIDlet-Jar-Size " + n + " exceeds the device limit " + p.jarMaxBytes + " (profile " + p.name + ")";
        } catch (NumberFormatException e) { return "MIDlet-Jar-Size is not a number: " + s; }
        return null;
    }

    private static void add(Plan r, String... k) { for (int i = 0; i < k.length; i++) r.virtualKeys.add(k[i]); }
}
