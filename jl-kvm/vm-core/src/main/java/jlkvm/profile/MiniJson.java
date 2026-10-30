package jlkvm.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Minimal JSON reader (objects, arrays, strings, numbers, true/false/null) so vm-core needs no library. Numbers become Long or Double. */
public final class MiniJson {
    private final String s;
    private int p;

    private MiniJson(String s) { this.s = s; }

    /** @return Map (LinkedHashMap), List, String, Long, Double, Boolean or null. @throws IllegalArgumentException on malformed text. */
    public static Object parse(String text) {
        MiniJson j = new MiniJson(text);
        j.ws();
        Object v = j.value();
        j.ws();
        if (j.p != j.s.length()) throw j.err("trailing text");
        return v;
    }

    private IllegalArgumentException err(String m) { return new IllegalArgumentException("JSON: " + m + " at offset " + p); }

    private void ws() { while (p < s.length() && " \t\r\n".indexOf(s.charAt(p)) >= 0) p++; }

    private Object value() {
        if (p >= s.length()) throw err("unexpected end");
        char c = s.charAt(p);
        if (c == '{') return object();
        if (c == '[') return array();
        if (c == '"') return string();
        if (s.startsWith("true", p)) { p += 4; return Boolean.TRUE; }
        if (s.startsWith("false", p)) { p += 5; return Boolean.FALSE; }
        if (s.startsWith("null", p)) { p += 4; return null; }
        return number();
    }

    private Map<String, Object> object() {
        Map<String, Object> m = new LinkedHashMap<String, Object>();
        p++; ws();
        if (s.charAt(p) == '}') { p++; return m; }
        for (;;) {
            ws();
            if (p >= s.length() || s.charAt(p) != '"') throw err("key expected");
            String k = string();
            ws();
            if (p >= s.length() || s.charAt(p) != ':') throw err("':' expected");
            p++; ws();
            m.put(k, value());
            ws();
            if (p >= s.length()) throw err("unexpected end");
            char c = s.charAt(p++);
            if (c == '}') return m;
            if (c != ',') throw err("',' or '}' expected");
        }
    }

    private List<Object> array() {
        List<Object> l = new ArrayList<Object>();
        p++; ws();
        if (s.charAt(p) == ']') { p++; return l; }
        for (;;) {
            ws();
            l.add(value());
            ws();
            if (p >= s.length()) throw err("unexpected end");
            char c = s.charAt(p++);
            if (c == ']') return l;
            if (c != ',') throw err("',' or ']' expected");
        }
    }

    private String string() {
        StringBuilder b = new StringBuilder();
        p++;
        for (;;) {
            if (p >= s.length()) throw err("unterminated string");
            char c = s.charAt(p++);
            if (c == '"') return b.toString();
            if (c != '\\') { b.append(c); continue; }
            if (p >= s.length()) throw err("bad escape");
            c = s.charAt(p++);
            switch (c) {
                case 'n': b.append('\n'); break;
                case 't': b.append('\t'); break;
                case 'r': b.append('\r'); break;
                case 'b': b.append('\b'); break;
                case 'f': b.append('\f'); break;
                case 'u':
                    if (p + 4 > s.length()) throw err("bad \\u escape");
                    b.append((char) Integer.parseInt(s.substring(p, p + 4), 16)); p += 4; break;
                default: b.append(c);
            }
        }
    }

    private Object number() {
        int st = p;
        while (p < s.length() && "+-0123456789.eE".indexOf(s.charAt(p)) >= 0) p++;
        if (st == p) throw err("value expected");
        String t = s.substring(st, p);
        try {
            if (t.indexOf('.') < 0 && t.indexOf('e') < 0 && t.indexOf('E') < 0) return Long.valueOf(t);
            return Double.valueOf(t);
        } catch (NumberFormatException e) {
            throw err("bad number " + t);
        }
    }
}
