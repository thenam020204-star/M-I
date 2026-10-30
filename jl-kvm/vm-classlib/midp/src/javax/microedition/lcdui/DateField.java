/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateField extends Item {
    public static final int DATE = 1, TIME = 2, DATE_TIME = 3;
    private int mode;
    private final TimeZone tz;
    private boolean has;
    private long time;

    public DateField(String label, int mode) {
        this(label, mode, null);
    }

    public DateField(String label, int mode, TimeZone timeZone) {
        super(label);
        checkMode(mode);
        this.mode = mode;
        this.tz = timeZone;
    }

    private static void checkMode(int m) {
        if (m != DATE && m != TIME && m != DATE_TIME) throw new IllegalArgumentException("mode");
    }

    /** null when the field is not initialised; in DATE mode the time of day is 00:00, in TIME mode the day is 1 January 1970 (field time zone). */
    public synchronized Date getDate() {
        if (!has) return null;
        Calendar c = Calendar.getInstance(tz != null ? tz : TimeZone.getDefault());
        c.setTime(new Date(time));
        if (mode == DATE) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } else if (mode == TIME) {
            c.set(Calendar.YEAR, 1970);
            c.set(Calendar.MONTH, Calendar.JANUARY);
            c.set(Calendar.DAY_OF_MONTH, 1);
        }
        return c.getTime();
    }

    public synchronized void setDate(Date date) {
        if (date == null) {
            has = false;
        } else {
            has = true;
            time = date.getTime();
        }
        changed();
    }

    public int getInputMode() {
        return mode;
    }

    public void setInputMode(int mode) {
        checkMode(mode);
        this.mode = mode;
        changed();
    }

    int uiKind() {
        return ScreenCodec.I_DATE;
    }

    void encode() {
        Date d = getDate();
        ScreenCodec.w(mode);
        ScreenCodec.w(d == null ? 0 : 1);
        long t = d == null ? 0 : d.getTime();
        ScreenCodec.w((int) (t >>> 32));
        ScreenCodec.w((int) t);
        ScreenCodec.w((tz != null ? tz : TimeZone.getDefault()).getRawOffset());
    }

    int calcPrefW() {
        return availW();
    }

    int calcPrefH() {
        return labelH() + Font.getDefaultFont().getHeight() + 4;
    }

    int calcMinW() {
        return Math.min(availW(), 8 * Font.getDefaultFont().charWidth('0'));
    }

    int calcMinH() {
        return calcPrefH();
    }

    /** Event thread: the user picked a date / time (null: cleared). */
    synchronized void hostSet(boolean has, long millis) {
        this.has = has;
        if (has) time = millis;
    }
}
