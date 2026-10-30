/*
 * jl-kvm guest class library (JSR-75 PIM layer, API per tools/api-sigs/db/jsr75-pim.json). Origin: original code written for jl-kvm (PLAN D12, T32a);
 * behaviour from the JSR-75 PIM API as remembered, open points UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */
package javax.microedition.pim;

import java.util.Enumeration;
import java.util.Vector;

public class RepeatRule {
    public static final int FREQUENCY = 0;
    public static final int DAY_IN_MONTH = 1;
    public static final int FIRST = 1;
    public static final int DAY_IN_WEEK = 2;
    public static final int SECOND = 2;
    public static final int DAY_IN_YEAR = 4;
    public static final int THIRD = 4;
    public static final int FOURTH = 8;
    public static final int MONTH_IN_YEAR = 8;
    public static final int DAILY = 16;
    public static final int FIFTH = 16;
    public static final int WEEK_IN_MONTH = 16;
    public static final int WEEKLY = 17;
    public static final int MONTHLY = 18;
    public static final int YEARLY = 19;
    public static final int COUNT = 32;
    public static final int LAST = 32;
    public static final int END = 64;
    public static final int SECONDLAST = 64;
    public static final int INTERVAL = 128;
    public static final int THIRDLAST = 128;
    public static final int FOURTHLAST = 256;
    public static final int FIFTHLAST = 512;
    public static final int SATURDAY = 1024;
    public static final int FRIDAY = 2048;
    public static final int THURSDAY = 4096;
    public static final int WEDNESDAY = 8192;
    public static final int TUESDAY = 16384;
    public static final int MONDAY = 32768;
    public static final int SUNDAY = 65536;
    public static final int JANUARY = 131072;
    public static final int FEBRUARY = 262144;
    public static final int MARCH = 524288;
    public static final int APRIL = 1048576;
    public static final int MAY = 2097152;
    public static final int JUNE = 4194304;
    public static final int JULY = 8388608;
    public static final int AUGUST = 16777216;
    public static final int SEPTEMBER = 33554432;
    public static final int OCTOBER = 67108864;
    public static final int NOVEMBER = 134217728;
    public static final int DECEMBER = 268435456;

    private final int[] ids = new int[16];
    private final int[] vals = new int[16];
    private int n;
    private final Vector except = new Vector();

    public RepeatRule() {
    }

    private int find(int field) {
        for (int i = 0; i < n; i++) {
            if (ids[i] == field) {
                return i;
            }
        }
        return -1;
    }

    private static void checkField(int field) {
        if (field != FREQUENCY && field != COUNT && field != END && field != INTERVAL && field != DAY_IN_WEEK && field != DAY_IN_MONTH
                && field != DAY_IN_YEAR && field != MONTH_IN_YEAR && field != WEEK_IN_MONTH) {
            throw new IllegalArgumentException("Invalid repeat rule field " + field);
        }
    }

    public void addExceptDate(long date) {
        Long d = new Long(date);
        if (!except.contains(d)) {
            except.addElement(d);
        }
    }

    public void removeExceptDate(long date) {
        except.removeElement(new Long(date));
    }

    public Enumeration getExceptDates() {
        return except.elements();
    }

    public int getInt(int field) {
        checkField(field);
        if (field == END) {
            throw new IllegalArgumentException("END is a date field");
        }
        int i = find(field);
        if (i < 0) {
            throw new IllegalArgumentException("Field not set: " + field);
        }
        return vals[i];
    }

    public void setInt(int field, int value) {
        checkField(field);
        if (field == END) {
            throw new IllegalArgumentException("END is a date field");
        }
        put(field, value);
    }

    private void put(int field, int v) {
        int i = find(field);
        if (i < 0) {
            i = n++;
            ids[i] = field;
        }
        vals[i] = v;
    }

    public long getDate(int field) {
        if (field != END) {
            throw new IllegalArgumentException("Not a date field: " + field);
        }
        int i = find(END);
        if (i < 0) {
            throw new IllegalArgumentException("END not set");
        }
        return endDate;
    }

    private long endDate;

    public void setDate(int field, long date) {
        if (field != END) {
            throw new IllegalArgumentException("Not a date field: " + field);
        }
        put(END, 0);
        endDate = date;
    }

    public int[] getFields() {
        int[] r = new int[n];
        System.arraycopy(ids, 0, r, 0, n);
        return r;
    }

    /** The occurrences are not computed in this runtime (no event can hold a rule that matters): always empty. */
    public Enumeration dates(long startDate, long subsetBeginning, long subsetEnding) {
        return new Vector().elements();
    }

    public boolean equals(Object o) {
        if (!(o instanceof RepeatRule)) {
            return false;
        }
        RepeatRule r = (RepeatRule) o;
        if (r.n != n || r.except.size() != except.size()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            int j = r.find(ids[i]);
            if (j < 0 || r.vals[j] != vals[i] || (ids[i] == END && r.endDate != endDate)) {
                return false;
            }
        }
        for (int i = 0; i < except.size(); i++) {
            if (!r.except.contains(except.elementAt(i))) {
                return false;
            }
        }
        return true;
    }
}
