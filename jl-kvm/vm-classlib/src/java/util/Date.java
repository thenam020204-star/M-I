/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

public class Date {
    private long fastTime;

    public Date() {
        this(System.currentTimeMillis());
    }

    public Date(long date) {
        fastTime = date;
    }

    public long getTime() {
        return fastTime;
    }

    public void setTime(long time) {
        fastTime = time;
    }

    public boolean equals(Object obj) {
        return obj instanceof Date && getTime() == ((Date) obj).getTime();
    }

    public int hashCode() {
        long ht = getTime();
        return (int) ht ^ (int) (ht >> 32);
    }

    private static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private static void two(StringBuffer b, int v) {
        if (v < 10) {
            b.append('0');
        }
        b.append(v);
    }

    /** "dow mon dd hh:mm:ss zzz yyyy" in the default time zone. */
    public String toString() {
        Calendar c = Calendar.getInstance();
        c.setTime(this);
        StringBuffer b = new StringBuffer();
        b.append(DAYS[c.get(Calendar.DAY_OF_WEEK) - 1]).append(' ');
        b.append(MONTHS[c.get(Calendar.MONTH)]).append(' ');
        two(b, c.get(Calendar.DAY_OF_MONTH));
        b.append(' ');
        two(b, c.get(Calendar.HOUR_OF_DAY));
        b.append(':');
        two(b, c.get(Calendar.MINUTE));
        b.append(':');
        two(b, c.get(Calendar.SECOND));
        b.append(' ').append(c.getTimeZone().getID()).append(' ');
        b.append(c.get(Calendar.YEAR));
        return b.toString();
    }
}
