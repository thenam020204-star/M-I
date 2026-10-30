/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/**
 * Calendar fields over a millisecond time value. Lenient like J2SE: out-of-range field values roll into the next unit when the
 * time is recomputed. The implementation class is the package-private {@link CalendarImpl} (proleptic Gregorian, UNVERIFIED for
 * dates before 1582).
 */
public abstract class Calendar {
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DAY_OF_MONTH = 5;
    public static final int DATE = 5;
    public static final int DAY_OF_WEEK = 7;
    public static final int AM_PM = 9;
    public static final int HOUR = 10;
    public static final int HOUR_OF_DAY = 11;
    public static final int MINUTE = 12;
    public static final int SECOND = 13;
    public static final int MILLISECOND = 14;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    public static final int JANUARY = 0;
    public static final int FEBRUARY = 1;
    public static final int MARCH = 2;
    public static final int APRIL = 3;
    public static final int MAY = 4;
    public static final int JUNE = 5;
    public static final int JULY = 6;
    public static final int AUGUST = 7;
    public static final int SEPTEMBER = 8;
    public static final int OCTOBER = 9;
    public static final int NOVEMBER = 10;
    public static final int DECEMBER = 11;
    public static final int AM = 0;
    public static final int PM = 1;

    static final int FIELD_COUNT = 15;

    protected int[] fields;
    protected boolean[] isSet;
    protected long time;

    private boolean isTimeSet;
    private boolean areFieldsSet;
    /** Order in which fields were set (CalendarImpl resolves HOUR_OF_DAY against HOUR/AM_PM and DATE against DAY_OF_WEEK). */
    final int[] stamp = new int[FIELD_COUNT];
    private int nextStamp = 1;
    private TimeZone zone;

    protected Calendar() {
        fields = new int[FIELD_COUNT];
        isSet = new boolean[FIELD_COUNT];
        zone = TimeZone.getDefault();
    }

    public static synchronized Calendar getInstance() {
        return new CalendarImpl(TimeZone.getDefault());
    }

    public static synchronized Calendar getInstance(TimeZone zone) {
        return new CalendarImpl(zone == null ? TimeZone.getDefault() : zone);
    }

    public final Date getTime() {
        return new Date(getTimeInMillis());
    }

    public final void setTime(Date date) {
        setTimeInMillis(date.getTime());
    }

    protected long getTimeInMillis() {
        if (!isTimeSet) {
            computeTime();
            isTimeSet = true;
            areFieldsSet = false;
        }
        return time;
    }

    protected void setTimeInMillis(long millis) {
        time = millis;
        isTimeSet = true;
        areFieldsSet = false;
        for (int i = 0; i < FIELD_COUNT; i++) {
            isSet[i] = false;
            stamp[i] = 0;
        }
        nextStamp = 1;
    }

    public final int get(int field) {
        if (field < 0 || field >= FIELD_COUNT) {
            throw new ArrayIndexOutOfBoundsException(field);
        }
        complete();
        return fields[field];
    }

    public final void set(int field, int value) {
        if (field < 0 || field >= FIELD_COUNT) {
            throw new ArrayIndexOutOfBoundsException(field);
        }
        complete();
        fields[field] = value;
        isSet[field] = true;
        stamp[field] = nextStamp++;
        isTimeSet = false;
        areFieldsSet = true;
        // the fields hold the user's values: computeTime() resolves them, computeFields() then normalises them
    }

    private void complete() {
        if (!isTimeSet) {
            computeTime();
            isTimeSet = true;
            areFieldsSet = false;
        }
        if (!areFieldsSet) {
            computeFields();
            areFieldsSet = true;
            for (int i = 0; i < FIELD_COUNT; i++) {
                isSet[i] = true;
            }
        }
    }

    public TimeZone getTimeZone() {
        return zone;
    }

    public void setTimeZone(TimeZone value) {
        zone = value;
        areFieldsSet = false;
    }

    public boolean before(Object when) {
        return when instanceof Calendar && getTimeInMillis() < ((Calendar) when).getTimeInMillis();
    }

    public boolean after(Object when) {
        return when instanceof Calendar && getTimeInMillis() > ((Calendar) when).getTimeInMillis();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Calendar)) {
            return false;
        }
        Calendar that = (Calendar) obj;
        return getTimeInMillis() == that.getTimeInMillis() && zone.getRawOffset() == that.zone.getRawOffset();
    }

    protected abstract void computeTime();

    protected abstract void computeFields();
}
