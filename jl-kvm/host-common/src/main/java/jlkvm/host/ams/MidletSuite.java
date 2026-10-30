package jlkvm.host.ams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The MIDlet suite as the AMS sees it: manifest (+ optional JAD) merged into the attribute set returned by MIDlet.getAppProperty,
 * and the list of MIDlets ({@code MIDlet-1}, {@code MIDlet-2}, ... = name, icon, class). Lookups of names are case-insensitive
 * (Descriptor keeps the original case).
 */
public final class MidletSuite {
    public static final class Entry {
        public final int index;
        public final String name, icon, className;
        Entry(int index, String name, String icon, String className) { this.index = index; this.name = name; this.icon = icon; this.className = className; }
        @Override public String toString() { return "MIDlet-" + index + ": " + name + "," + icon + "," + className; }
    }

    public final Map<String, String> attributes = new LinkedHashMap<String, String>();
    public final List<Entry> midlets = new ArrayList<Entry>();

    /** @param manifest bytes of META-INF/MANIFEST.MF (or null), @param jad bytes of the .jad (or null). */
    public static MidletSuite load(byte[] manifest, byte[] jad) throws IOException {
        MidletSuite s = new MidletSuite();
        Descriptor m = manifest == null ? null : new Descriptor(manifest, false);
        Descriptor j = jad == null ? null : new Descriptor(jad, true);
        if (m == null && j == null) throw new DescriptorException("no manifest and no JAD");
        if (m != null && j != null) {
            // MIDP 2.0 section 4.3: Name / Vendor / Version of JAD and manifest must agree, otherwise the suite is rejected
            for (String k : new String[] {Descriptor.MIDLET_NAME, Descriptor.MIDLET_VENDOR, Descriptor.MIDLET_VERSION}) {
                if (!j.get(k).equals(m.get(k))) throw new DescriptorException("JAD and manifest disagree on " + k + ": '" + j.get(k) + "' vs '" + m.get(k) + "'");
            }
        }
        if (m != null) s.attributes.putAll(m.getAttrs());
        if (j != null) s.attributes.putAll(j.getAttrs());   // JAD wins
        for (int i = 1; ; i++) {
            String v = s.attributes.get("MIDlet-" + i);
            if (v == null) break;
            String[] p = v.split(",", -1);
            if (p.length < 3) throw new DescriptorException("MIDlet-" + i + " needs 'name, icon, class': " + v);
            s.midlets.add(new Entry(i, p[0].trim(), p[1].trim(), p[2].trim()));
        }
        if (s.midlets.isEmpty()) throw new DescriptorException("no MIDlet-1 attribute");
        return s;
    }

    /** @param n 1-based MIDlet number (MIDlet-n). */
    public Entry midlet(int n) {
        if (n < 1 || n > midlets.size()) throw new IllegalArgumentException("no MIDlet-" + n);
        return midlets.get(n - 1);
    }
}
