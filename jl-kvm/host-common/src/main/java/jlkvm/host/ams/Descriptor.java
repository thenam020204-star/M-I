package jlkvm.host.ams;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JAD / manifest attributes (PLAN T21). Pure-Java port of {@code ru.woesss.j2me.jar.Descriptor} (JL-Mod, Apache-2.0, Yury Kharchenko):
 * the original cannot be linked from the host modules because it uses the Android Context / SpannableStringBuilder classes (UI text) and
 * the app's FileUtils; the attribute names, the required-attribute checks and the version comparison are kept. Differences (deliberate):
 * (1) continuation lines are recognised by their leading space as in the manifest / JAD syntax (the original treated every line without a
 * colon as a continuation, which breaks a continuation line containing ':'), (2) a UTF-8 BOM and CR / LF / CRLF line ends are accepted,
 * (3) no android UI methods.
 */
public final class Descriptor {
    public static final String MIDLET_NAME = "MIDlet-Name", MIDLET_VERSION = "MIDlet-Version", MIDLET_VENDOR = "MIDlet-Vendor";
    public static final String MIDLET_JAR_URL = "MIDlet-Jar-URL", MIDLET_JAR_SIZE = "MIDlet-Jar-Size";
    public static final String MIDLET_N = "MIDlet-", MICROEDITION_PROFILE = "MicroEdition-Profile", MICROEDITION_CONFIGURATION = "MicroEdition-Configuration";
    public static final String MIDLET_ICON = "MIDlet-Icon", MIDLET_DESCRIPTION = "MIDlet-Description", MIDLET_DATA_SIZE = "MIDlet-Data-Size";

    private final boolean isJad;
    private final Map<String, String> attributes = new LinkedHashMap<String, String>();

    /** @param isJad true for a JAD (MIDlet-Jar-URL / MIDlet-Jar-Size required), false for a manifest. */
    public Descriptor(String source, boolean isJad) throws IOException {
        this.isJad = isJad;
        try {
            parse(source);
        } catch (RuntimeException e) {
            throw new DescriptorException("Bad descriptor: " + e, e);
        }
        if (isJad) verifyJadAttrs();
        verify();
    }

    public Descriptor(byte[] utf8, boolean isJad) throws IOException {
        this(new String(utf8, "UTF-8"), isJad);
    }

    private void parse(String source) {
        if (!source.isEmpty() && source.charAt(0) == '﻿') source = source.substring(1);
        String[] raw = source.split("\r\n|\r|\n", -1);
        String last = null;
        java.util.ArrayList<String> logical = new java.util.ArrayList<String>();
        for (String line : raw) {
            if (!line.isEmpty() && line.charAt(0) == ' ' && last != null) {
                last += line.substring(1);
                logical.set(logical.size() - 1, last);
            } else if (!line.isEmpty()) {
                last = line;
                logical.add(line);
            }
        }
        for (String line : logical) {
            int c = line.indexOf(':');
            if (c < 0) continue;   // not an attribute line: ignored (JL-Mod ignored these as well when they could not be joined)
            attributes.put(strip(line.substring(0, c)), strip(line.substring(c + 1)));
        }
    }

    private static String strip(String s) {
        int b = 0, e = s.length();
        while (b < e && (s.charAt(b) == ' ' || s.charAt(b) == '\t')) b++;
        while (e > b && (s.charAt(e - 1) == ' ' || s.charAt(e - 1) == '\t')) e--;
        return s.substring(b, e);
    }

    private void verifyJadAttrs() throws DescriptorException {
        String size = attributes.get(MIDLET_JAR_SIZE);
        if (size == null) throw new DescriptorException(fail(MIDLET_JAR_SIZE, "not found"));
        try {
            Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new DescriptorException(fail(MIDLET_JAR_SIZE, size), e);
        }
        String url = attributes.get(MIDLET_JAR_URL);
        if (url == null || url.isEmpty()) throw new DescriptorException(fail(MIDLET_JAR_URL, url == null ? "not found" : "empty value"));
    }

    private void verify() throws DescriptorException {
        if (getName() == null) throw new DescriptorException(fail(MIDLET_NAME, "not found"));
        if (getVendor() == null) throw new DescriptorException(fail(MIDLET_VENDOR, "not found"));
        if (getVersion() == null) throw new DescriptorException(fail(MIDLET_VERSION, "not found"));
    }

    private static String fail(String k, String v) { return "Fail attribute '" + k + ": " + v + "'"; }

    public String getName() { return attributes.get(MIDLET_NAME); }
    public String getVendor() { return attributes.get(MIDLET_VENDOR); }
    public String getVersion() { return attributes.get(MIDLET_VERSION); }
    public String get(String name) { return attributes.get(name); }
    public Map<String, String> getAttrs() { return attributes; }
    public boolean isJad() { return isJad; }

    /** Compares this descriptor's version ("1.2.3") with another version string: 1, 0, -1 (as the JL-Mod original). */
    public int compareVersion(String version) {
        if (version == null) return 1;
        String[] mv = getVersion().split("\\."), ov = version.split("\\.");
        int len = Math.max(mv.length, ov.length);
        for (int i = 0; i < len; i++) {
            int m = i < mv.length ? num(mv[i]) : 0, o = i < ov.length ? num(ov[i]) : 0;
            if (m != o) return m < o ? -1 : 1;
        }
        return 0;
    }

    private static int num(String s) {
        try { return Integer.parseInt(s.trim()); } catch (NumberFormatException e) { return 0; }
    }

    /** JAD attributes override the manifest ones for the same name (MIDP 2.0 section 4.2 / 4.3: the JAD value is what getAppProperty returns). */
    public void merge(Descriptor newer) { attributes.putAll(newer.attributes); }
}
