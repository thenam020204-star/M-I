/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package jlkvm.internal.io.http;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/** ROM-private: parses the HTTP date of RFC 1123 ("Sun, 06 Nov 1994 08:49:37 GMT"). */
final class HttpDate {
    private HttpDate() {
    }

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /** @return milliseconds since 1970, or -1 when the text is not a date */
    static long parse(String s) {
        if (s == null) {
            return -1;
        }
        try {
            s = s.trim();
            int comma = s.indexOf(',');
            if (comma >= 0) {
                s = s.substring(comma + 1).trim();
            }
            // "06 Nov 1994 08:49:37 GMT"
            int sp1 = s.indexOf(' ');
            int sp2 = s.indexOf(' ', sp1 + 1);
            int sp3 = s.indexOf(' ', sp2 + 1);
            int day = Integer.parseInt(s.substring(0, sp1));
            String mon = s.substring(sp1 + 1, sp2);
            int month = -1;
            for (int i = 0; i < 12; i++) {
                if (MONTHS[i].equals(mon)) {
                    month = i;
                }
            }
            int year = Integer.parseInt(s.substring(sp2 + 1, sp3));
            String t = sp3 + 1 < s.length() ? s.substring(sp3 + 1).trim() : "";
            int c1 = t.indexOf(':');
            int c2 = t.indexOf(':', c1 + 1);
            int hh = Integer.parseInt(t.substring(0, c1));
            int mm = Integer.parseInt(t.substring(c1 + 1, c2));
            int ss = Integer.parseInt(t.substring(c2 + 1, c2 + 3));
            if (month < 0 || day < 1 || day > 31 || hh > 23 || mm > 59 || ss > 60) {
                return -1;
            }
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.HOUR_OF_DAY, hh);
            c.set(Calendar.MINUTE, mm);
            c.set(Calendar.SECOND, ss);
            c.set(Calendar.MILLISECOND, 0);
            Date d = c.getTime();
            return d.getTime();
        } catch (RuntimeException e) {
            return -1;
        }
    }
}
