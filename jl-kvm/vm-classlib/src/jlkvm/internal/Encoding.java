/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package jlkvm.internal;

import java.io.IOException;
import java.io.InputStream;

/**
 * Character encodings of the guest library (PLAN T19): ISO-8859-1, UTF-8, US-ASCII and UTF-16 (big endian with byte order mark
 * detection on input, big endian without BOM on output; UTF-16BE / UTF-16LE explicit). Shared by String, InputStreamReader,
 * OutputStreamWriter and PrintStream. The default is the profile property {@code microedition.encoding}.
 * Not part of any device API: ROM-private (excluded from the API surface check).
 */
public final class Encoding {
    public static final int LATIN1 = 1, UTF8 = 2, ASCII = 3, UTF16 = 4, UTF16BE = 5, UTF16LE = 6;

    private static int defaultId;

    private Encoding() {
    }

    /** Encoding id for a name (case-insensitive, with the usual aliases), 0 when not supported. */
    public static int lookup(String name) {
        if (name == null) {
            return 0;
        }
        String n = name.toUpperCase();
        if (n.equals("ISO-8859-1") || n.equals("ISO8859_1") || n.equals("ISO8859-1") || n.equals("ISO_8859-1") || n.equals("ISO_8859_1")
                || n.equals("LATIN1") || n.equals("L1") || n.equals("8859_1") || n.equals("CP819") || n.equals("IBM819")) {
            return LATIN1;
        }
        if (n.equals("UTF-8") || n.equals("UTF8")) {
            return UTF8;
        }
        if (n.equals("US-ASCII") || n.equals("ASCII") || n.equals("ISO646-US")) {
            return ASCII;
        }
        if (n.equals("UTF-16") || n.equals("UTF16") || n.equals("UNICODE")) {
            return UTF16;
        }
        if (n.equals("UTF-16BE") || n.equals("UTF16BE") || n.equals("UNICODEBIGUNMARKED")) {
            return UTF16BE;
        }
        if (n.equals("UTF-16LE") || n.equals("UTF16LE") || n.equals("UNICODELITTLEUNMARKED")) {
            return UTF16LE;
        }
        return 0;
    }

    /** Encoding id of {@code microedition.encoding}; ISO-8859-1 when the profile value is not supported. */
    public static int defaultId() {
        if (defaultId == 0) {
            int id = lookup(System.getProperty("microedition.encoding"));
            defaultId = id == 0 ? LATIN1 : id;
        }
        return defaultId;
    }

    /** Id for an explicit encoding name; throws UnsupportedEncodingException for an unknown one, NullPointerException for null. */
    public static int require(String name) throws java.io.UnsupportedEncodingException {
        if (name == null) {
            throw new NullPointerException();
        }
        int id = lookup(name);
        if (id == 0) {
            throw new java.io.UnsupportedEncodingException(name);
        }
        return id;
    }

    // ------------------------------------------------------------------ chars -> bytes

    /** Encodes c[off..off+len). Characters the encoding cannot represent become '?'. */
    public static byte[] encode(int id, char[] c, int off, int len) {
        switch (id) {
            case UTF8:
                return encodeUtf8(c, off, len);
            case UTF16:
            case UTF16BE:
            case UTF16LE:
                return encodeUtf16(id, c, off, len);
            default: {
                int max = id == ASCII ? 0x7F : 0xFF;
                byte[] r = new byte[len];
                for (int i = 0; i < len; i++) {
                    char ch = c[off + i];
                    r[i] = (byte) (ch <= max ? ch : '?');
                }
                return r;
            }
        }
    }

    private static byte[] encodeUtf8(char[] c, int off, int len) {
        int n = 0;
        for (int i = 0; i < len; i++) {
            char ch = c[off + i];
            if (ch < 0x80) {
                n += 1;
            } else if (ch < 0x800) {
                n += 2;
            } else if (ch >= 0xD800 && ch <= 0xDBFF && i + 1 < len && c[off + i + 1] >= 0xDC00 && c[off + i + 1] <= 0xDFFF) {
                n += 4;
                i++;
            } else if (ch >= 0xD800 && ch <= 0xDFFF) {
                n += 1;
            } else {
                n += 3;
            }
        }
        byte[] r = new byte[n];
        int p = 0;
        for (int i = 0; i < len; i++) {
            char ch = c[off + i];
            if (ch < 0x80) {
                r[p++] = (byte) ch;
            } else if (ch < 0x800) {
                r[p++] = (byte) (0xC0 | (ch >> 6));
                r[p++] = (byte) (0x80 | (ch & 0x3F));
            } else if (ch >= 0xD800 && ch <= 0xDBFF && i + 1 < len && c[off + i + 1] >= 0xDC00 && c[off + i + 1] <= 0xDFFF) {
                int cp = 0x10000 + ((ch - 0xD800) << 10) + (c[off + i + 1] - 0xDC00);
                i++;
                r[p++] = (byte) (0xF0 | (cp >> 18));
                r[p++] = (byte) (0x80 | ((cp >> 12) & 0x3F));
                r[p++] = (byte) (0x80 | ((cp >> 6) & 0x3F));
                r[p++] = (byte) (0x80 | (cp & 0x3F));
            } else if (ch >= 0xD800 && ch <= 0xDFFF) {
                r[p++] = (byte) '?';
            } else {
                r[p++] = (byte) (0xE0 | (ch >> 12));
                r[p++] = (byte) (0x80 | ((ch >> 6) & 0x3F));
                r[p++] = (byte) (0x80 | (ch & 0x3F));
            }
        }
        return r;
    }

    private static byte[] encodeUtf16(int id, char[] c, int off, int len) {
        boolean bom = id == UTF16;
        byte[] r = new byte[len * 2 + (bom ? 2 : 0)];
        int p = 0;
        if (bom) {
            r[p++] = (byte) 0xFE;
            r[p++] = (byte) 0xFF;
        }
        for (int i = 0; i < len; i++) {
            char ch = c[off + i];
            if (id == UTF16LE) {
                r[p++] = (byte) ch;
                r[p++] = (byte) (ch >> 8);
            } else {
                r[p++] = (byte) (ch >> 8);
                r[p++] = (byte) ch;
            }
        }
        return r;
    }

    // ------------------------------------------------------------------ bytes -> chars

    /** Decodes b[off..off+len) into a new, exactly sized array. Malformed input becomes U+FFFD (one per bad byte). */
    public static char[] decode(int id, byte[] b, int off, int len) {
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new StringIndexOutOfBoundsException("offset " + off + ", length " + len + ", byte[].length " + b.length);
        }
        char[] out = new char[len];
        int n = 0;
        switch (id) {
            case UTF8: {
                int i = off, end = off + len;
                while (i < end) {
                    int c = b[i] & 0xFF;
                    if (c < 0x80) {
                        out[n++] = (char) c;
                        i++;
                        continue;
                    }
                    int need = c >= 0xC2 && c < 0xE0 ? 1 : c >= 0xE0 && c < 0xF0 ? 2 : c >= 0xF0 && c < 0xF5 ? 3 : 0;
                    if (need == 0) {
                        out[n++] = '\uFFFD';
                        i++;
                        continue;
                    }
                    int cp = need == 1 ? c & 0x1F : need == 2 ? c & 0x0F : c & 0x07;
                    int k = 1;
                    for (; k <= need; k++) {
                        if (i + k >= end || (b[i + k] & 0xC0) != 0x80) {
                            break;
                        }
                        cp = (cp << 6) | (b[i + k] & 0x3F);
                    }
                    if (k <= need || (need == 2 && cp < 0x800) || (need == 3 && (cp < 0x10000 || cp > 0x10FFFF))
                            || (cp >= 0xD800 && cp <= 0xDFFF)) {
                        out[n++] = '�';
                        i++;
                        continue;
                    }
                    i += need + 1;
                    if (cp >= 0x10000) {
                        cp -= 0x10000;
                        out[n++] = (char) (0xD800 + (cp >> 10));
                        out[n++] = (char) (0xDC00 + (cp & 0x3FF));
                    } else {
                        out[n++] = (char) cp;
                    }
                }
                break;
            }
            case UTF16:
            case UTF16BE:
            case UTF16LE: {
                int i = off, end = off + len;
                boolean le = id == UTF16LE;
                if (id == UTF16 && len >= 2) {
                    int b0 = b[i] & 0xFF, b1 = b[i + 1] & 0xFF;
                    if (b0 == 0xFE && b1 == 0xFF) {
                        i += 2;
                    } else if (b0 == 0xFF && b1 == 0xFE) {
                        le = true;
                        i += 2;
                    }
                }
                for (; i + 1 < end; i += 2) {
                    out[n++] = le ? (char) (((b[i + 1] & 0xFF) << 8) | (b[i] & 0xFF)) : (char) (((b[i] & 0xFF) << 8) | (b[i + 1] & 0xFF));
                }
                if (i < end) {
                    out[n++] = '�';
                }
                break;
            }
            default: {
                int max = id == ASCII ? 0x7F : 0xFF;
                for (int i = 0; i < len; i++) {
                    int c = b[off + i] & 0xFF;
                    out[n++] = c <= max ? (char) c : '�';
                }
                break;
            }
        }
        if (n == out.length) {
            return out;
        }
        char[] r = new char[n];
        System.arraycopy(out, 0, r, 0, n);
        return r;
    }

    /** Incremental decoder used by InputStreamReader: reads only the bytes needed for the next character. */
    public static final class Decoder {
        private final int id;
        private boolean le, started;
        private int pending = -1;
        /** A byte that was read but not consumed (the byte that ended a malformed UTF-8 sequence): the next read starts with it. */
        private int held = -1;
        private boolean sLe, sStarted;
        private int sPending = -1, sHeld = -1;

        /** Remembers the decoder state together with InputStreamReader.mark (the byte position is marked on the stream itself). */
        public void mark() {
            sLe = le;
            sStarted = started;
            sPending = pending;
            sHeld = held;
        }

        public void reset() {
            le = sLe;
            started = sStarted;
            pending = sPending;
            held = sHeld;
        }

        /** True when a char or a byte is waiting inside the decoder (the stream's available() does not count it). */
        public boolean hasPending() {
            return pending >= 0 || held >= 0;
        }

        private int nextByte(InputStream in) throws IOException {
            if (held >= 0) {
                int h = held;
                held = -1;
                return h;
            }
            return in.read();
        }

        public Decoder(int id) {
            this.id = id;
            this.le = id == UTF16LE;
        }

        /** @return the next char, or -1 at the end of the stream. */
        public int read(InputStream in) throws IOException {
            if (pending >= 0) {
                int p = pending;
                pending = -1;
                return p;
            }
            int c = nextByte(in);
            if (c < 0) {
                return -1;
            }
            switch (id) {
                case UTF8: {
                    if (c < 0x80) {
                        return c;
                    }
                    int need = c >= 0xC2 && c < 0xE0 ? 1 : c >= 0xE0 && c < 0xF0 ? 2 : c >= 0xF0 && c < 0xF5 ? 3 : 0;
                    if (need == 0) {
                        return 0xFFFD;
                    }
                    int cp = need == 1 ? c & 0x1F : need == 2 ? c & 0x0F : c & 0x07;
                    for (int k = 0; k < need; k++) {
                        if (!in.markSupported()) {
                            int d = nextByte(in);
                            if (d < 0 || (d & 0xC0) != 0x80) {
                                return 0xFFFD;   // truncated sequence: the byte that broke it is dropped (non-seekable stream)
                            }
                            cp = (cp << 6) | (d & 0x3F);
                        } else {
                            int d = nextByte(in);
                            if (d < 0) {
                                return 0xFFFD;
                            }
                            if ((d & 0xC0) != 0x80) {
                                held = d;   // not consumed: it starts the next char
                                return 0xFFFD;
                            }
                            cp = (cp << 6) | (d & 0x3F);
                        }
                    }
                    if ((need == 2 && cp < 0x800) || (need == 3 && (cp < 0x10000 || cp > 0x10FFFF)) || (cp >= 0xD800 && cp <= 0xDFFF)) {
                        return 0xFFFD;
                    }
                    if (cp >= 0x10000) {
                        cp -= 0x10000;
                        pending = 0xDC00 + (cp & 0x3FF);
                        return 0xD800 + (cp >> 10);
                    }
                    return cp;
                }
                case UTF16:
                case UTF16BE:
                case UTF16LE: {
                    int d = nextByte(in);
                    if (d < 0) {
                        return 0xFFFD;
                    }
                    if (!started) {
                        started = true;
                        if (id == UTF16) {
                            if (c == 0xFE && d == 0xFF) {
                                return read(in);
                            }
                            if (c == 0xFF && d == 0xFE) {
                                le = true;
                                return read(in);
                            }
                        }
                    }
                    return le ? (d << 8) | c : (c << 8) | d;
                }
                default:
                    if (id == ASCII && c > 0x7F) {
                        return 0xFFFD;
                    }
                    return c;
            }
        }
    }
}
