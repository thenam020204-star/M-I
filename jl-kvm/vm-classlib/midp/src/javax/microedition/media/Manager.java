/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connection;
import javax.microedition.io.ContentConnection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import jlkvm.internal.media.Audio;
import jlkvm.internal.media.ClipPlayer;
import jlkvm.internal.media.TonePlayer;

/**
 * Player factory (MIDP 2.0 Media subset). The content types and protocols come from the device profile ({@code media{}}); tone
 * sequences and RTTTL/RTX are played by guest code over the host tone generator, everything else by the host audio device. docs/midp.md
 * section 10.
 */
public final class Manager {
    public static final String TONE_DEVICE_LOCATOR = "device://tone";
    private static final String TONE_SEQ = "audio/x-tone-seq";
    private static final int MAX_TEXT = 65536;

    private static String[] types;
    private static String[] protocols;

    private Manager() {
    }

    private static String[] split(String csv) {
        Vector v = new Vector();
        int from = 0;
        while (csv != null && from <= csv.length()) {
            int c = csv.indexOf(',', from);
            if (c < 0) c = csv.length();
            if (c > from) v.addElement(csv.substring(from, c));
            from = c + 1;
        }
        String[] r = new String[v.size()];
        v.copyInto(r);
        return r;
    }

    private static synchronized void load() {
        if (types == null) {
            types = split(Audio.config0(Audio.CFG_TYPES));
            protocols = split(Audio.config0(Audio.CFG_PROTOCOLS));
        }
    }

    private static boolean has(String[] a, String s) {
        for (int i = 0; i < a.length; i++) if (a[i].equalsIgnoreCase(s)) return true;
        return false;
    }

    private static boolean isRtttl(String t) {
        return t.equals("audio/x-rtttl") || t.equals("text/x-rtttl") || t.equals("audio/rtttl");
    }

    private static boolean isMidi(String t) {
        return t.equals("audio/midi") || t.equals("audio/sp-midi");
    }

    /** Lower case, without parameters, common aliases mapped to the names of the profile. */
    private static String canon(String t) {
        int sc = t.indexOf(';');
        if (sc >= 0) t = t.substring(0, sc);
        t = t.trim().toLowerCase();
        if (t.equals("audio/mid") || t.equals("audio/x-midi")) return "audio/midi";
        if (t.equals("audio/wav") || t.equals("audio/wave") || t.equals("audio/x-wave")) return "audio/x-wav";
        if (t.equals("audio/mp3") || t.equals("audio/x-mpeg")) return "audio/mpeg";
        return t;
    }

    private static boolean supportedType(String t) {
        if (has(types, t)) return true;
        return isRtttl(t) && (has(types, "audio/x-rtttl") || has(types, "text/x-rtttl"));
    }

    public static String[] getSupportedContentTypes(String protocol) {
        load();
        if (protocol != null && !has(protocols, protocol)) return new String[0];
        Vector v = new Vector();
        for (int i = 0; i < types.length; i++) {
            if (protocol != null && protocol.equalsIgnoreCase("device") && !types[i].equals(TONE_SEQ)) continue;
            v.addElement(types[i]);
        }
        String[] r = new String[v.size()];
        v.copyInto(r);
        return r;
    }

    public static String[] getSupportedProtocols(String contentType) {
        load();
        String t = contentType == null ? null : canon(contentType);
        if (t != null && !supportedType(t)) return new String[0];
        Vector v = new Vector();
        for (int i = 0; i < protocols.length; i++) {
            if (t != null && protocols[i].equalsIgnoreCase("device") && !t.equals(TONE_SEQ)) continue;
            v.addElement(protocols[i]);
        }
        String[] r = new String[v.size()];
        v.copyInto(r);
        return r;
    }

    public static Player createPlayer(String locator) throws IOException, MediaException {
        if (locator == null) throw new IllegalArgumentException("null locator");
        load();
        int c = locator.indexOf(':');
        if (c <= 0) throw new MediaException("Malformed locator: " + locator);
        String scheme = locator.substring(0, c).toLowerCase();
        if (!has(protocols, scheme)) throw new MediaException("Unsupported protocol: " + scheme);
        if (scheme.equals("device")) {
            if (!locator.equalsIgnoreCase(TONE_DEVICE_LOCATOR) || !"1".equals(Audio.config0(Audio.CFG_TONE))) {
                throw new MediaException("Unsupported device locator: " + locator);
            }
            return new TonePlayer(TonePlayer.DEVICE, TONE_SEQ, null);
        }
        Connection conn = Connector.open(locator, Connector.READ);
        if (!(conn instanceof StreamConnection)) {
            conn.close();
            throw new MediaException("Not a stream: " + locator);
        }
        StreamConnection sc = (StreamConnection) conn;
        InputStream in = null;
        try {
            String type = null;
            if (sc instanceof ContentConnection) type = ((ContentConnection) sc).getType();
            if (type == null || canon(type).equals("application/octet-stream") || canon(type).length() == 0) type = byExtension(locator);
            in = sc.openInputStream();
            return fromStream(in, type);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            try {
                sc.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    private static String byExtension(String locator) {
        String l = locator.toLowerCase();
        int q = l.indexOf('?');
        if (q >= 0) l = l.substring(0, q);
        if (l.endsWith(".mid") || l.endsWith(".midi")) return "audio/midi";
        if (l.endsWith(".wav")) return "audio/x-wav";
        if (l.endsWith(".mp3")) return "audio/mpeg";
        if (l.endsWith(".amr")) return "audio/amr";
        if (l.endsWith(".rtx") || l.endsWith(".rtttl")) return "audio/x-rtttl";
        return null;
    }

    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        if (stream == null) throw new IllegalArgumentException("null stream");
        load();
        return fromStream(stream, type);
    }

    private static int fill(InputStream in, byte[] b) throws IOException {
        int n = 0;
        while (n < b.length) {
            int k = in.read(b, n, b.length - n);
            if (k < 0) break;
            n += k;
        }
        return n;
    }

    private static String sniff(byte[] b, int n) {
        if (n >= 4 && b[0] == 'M' && b[1] == 'T' && b[2] == 'h' && b[3] == 'd') return "audio/midi";
        if (n >= 12 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F' && b[8] == 'W' && b[9] == 'A' && b[10] == 'V' && b[11] == 'E') return "audio/x-wav";
        if (n >= 3 && b[0] == 'I' && b[1] == 'D' && b[2] == '3') return "audio/mpeg";
        if (n >= 2 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xE0) == 0xE0) return "audio/mpeg";
        if (n >= 5 && b[0] == '#' && b[1] == '!' && b[2] == 'A' && b[3] == 'M' && b[4] == 'R') return "audio/amr";
        if (n >= 2 && (b[0] & 0xFF) == 0xFE && b[1] == 1) return TONE_SEQ;
        int colons = 0;
        for (int i = 0; i < n && i < 256; i++) {
            int c = b[i] & 0xFF;
            if (c == ':') colons++;
            else if (c < 9 || (c > 13 && c < 32) || c > 126) return null;
        }
        return colons >= 2 ? "audio/x-rtttl" : null;
    }

    private static Player fromStream(InputStream in, String type) throws IOException, MediaException {
        byte[] buf = new byte[1024];
        int n = fill(in, buf);
        String t = type == null ? sniff(buf, n) : canon(type);
        if (t == null || t.length() == 0) throw new MediaException("Cannot determine the content type");
        if (!supportedType(t)) throw new MediaException("Unsupported content type: " + t);
        if (t.equals(TONE_SEQ) || isRtttl(t)) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(buf, 0, n);
            int k;
            while ((k = in.read(buf, 0, buf.length)) > 0) {
                if (bo.size() + k > MAX_TEXT) throw new MediaException("tone data too long");
                bo.write(buf, 0, k);
            }
            return new TonePlayer(t.equals(TONE_SEQ) ? TonePlayer.TONE_SEQ : TonePlayer.RTTTL, t, bo.toByteArray());
        }
        Audio.stage0(null, 0, -1);
        Audio.stage0(buf, 0, n);
        int k;
        while ((k = in.read(buf, 0, buf.length)) > 0) Audio.stage0(buf, 0, k);
        int src = Audio.source0(t);
        if (src <= 0) throw new MediaException("The audio device cannot take " + t);
        return new ClipPlayer(t, src, isMidi(t));
    }

    public static void playTone(int note, int duration, int volume) throws MediaException {
        if (note < 0 || note > 127) throw new IllegalArgumentException("note " + note + " out of 0..127");
        if (duration <= 0) throw new IllegalArgumentException("duration " + duration);
        if (volume < 0) volume = 0;
        if (volume > 100) volume = 100;
        if (!"1".equals(Audio.config0(Audio.CFG_TONE))) throw new MediaException("tone generator not available");
        Audio.tone0(note, duration, volume);
    }
}
