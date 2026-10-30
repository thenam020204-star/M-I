/*
 * jl-kvm guest class library (CLDC 1.1). Origin: original code written for jl-kvm (PLAN D12); API surface per the CLDC 1.1 javadoc.
 * License: Apache-2.0.
 */
package java.util;

/** The calendar behind Calendar.getInstance(): proleptic Gregorian, weeks start on Sunday. */
class CalendarImpl extends Calendar {
    private static final long DAY_MS = 86400000L;

    CalendarImpl(TimeZone zone) {
        super();
        setTimeZone(zone);
        setTimeInMillis(System.currentTimeMillis());
    }

    // ---- civil date <-> days since 1970-01-01 (algorithms of H. Hinnant, public domain)

    static long daysFromCivil(long y, int m, int d) {
        // m: 1..12
        y -= m <= 2 ? 1 : 0;
        long era = (y >= 0 ? y : y - 399) / 400;
        long yoe = y - era * 400;
        long doy = (153 * (m + (m > 2 ? -3 : 9)) + 2) / 5 + d - 1;
        long doe = yoe * 365 + yoe / 4 - yoe / 100 + doy;
        return era * 146097 + doe - 719468;
    }

    /** @return {year, month 1..12, day} */
    static long[] civilFromDays(long z) {
        z += 719468;
        long era = (z >= 0 ? z : z - 146096) / 146097;
        long doe = z - era * 146097;
        long yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365;
        long y = yoe + era * 400;
        long doy = doe - (365 * yoe + yoe / 4 - yoe / 100);
        long mp = (5 * doy + 2) / 153;
        long d = doy - (153 * mp + 2) / 5 + 1;
        long m = mp + (mp < 10 ? 3 : -9);
        return new long[] {y + (m <= 2 ? 1 : 0), m, d};
    }

    private static long floorDiv(long a, long b) {
        long q = a / b;
        return (a % b != 0 && ((a < 0) != (b < 0))) ? q - 1 : q;
    }

    private static long floorMod(long a, long b) {
        return a - floorDiv(a, b) * b;
    }

    protected void computeFields() {
        TimeZone tz = getTimeZone();
        long local = time + tz.getRawOffset();
        long days = floorDiv(local, DAY_MS);
        int msInDay = (int) floorMod(local, DAY_MS);
        long[] ymd = civilFromDays(days);
        int dow = (int) floorMod(days + 4, 7) + 1;   // 1970-01-01 was a Thursday (5)
        int off = tz.getOffset(1, (int) ymd[0], (int) ymd[1] - 1, (int) ymd[2], dow, msInDay);
        if (off != tz.getRawOffset()) {
            local = time + off;
            days = floorDiv(local, DAY_MS);
            msInDay = (int) floorMod(local, DAY_MS);
            ymd = civilFromDays(days);
            dow = (int) floorMod(days + 4, 7) + 1;
        }
        fields[YEAR] = (int) ymd[0];
        fields[MONTH] = (int) ymd[1] - 1;
        fields[DAY_OF_MONTH] = (int) ymd[2];
        fields[DAY_OF_WEEK] = dow;
        int hour = msInDay / 3600000;
        fields[HOUR_OF_DAY] = hour;
        fields[HOUR] = hour % 12;
        fields[AM_PM] = hour < 12 ? AM : PM;
        fields[MINUTE] = msInDay / 60000 % 60;
        fields[SECOND] = msInDay / 1000 % 60;
        fields[MILLISECOND] = msInDay % 1000;
    }

    protected void computeTime() {
        long year = isSet[YEAR] ? fields[YEAR] : 1970;
        long month = isSet[MONTH] ? fields[MONTH] : 0;
        long day = isSet[DAY_OF_MONTH] ? fields[DAY_OF_MONTH] : 1;
        // lenient month overflow
        year += floorDiv(month, 12);
        month = floorMod(month, 12);
        long days = daysFromCivil(year, (int) month + 1, 1);
        if (stamp[DAY_OF_WEEK] > stamp[DAY_OF_MONTH] && isSet[DAY_OF_WEEK]) {
            // DAY_OF_WEEK set after the date: move within the (Sunday based) week of the date fields
            long d = days + day - 1;
            long cur = floorMod(d + 4, 7) + 1;
            d += fields[DAY_OF_WEEK] - cur;
            days = d;
        } else {
            days += day - 1;
        }
        long ms;
        boolean ampm = stamp[HOUR] > stamp[HOUR_OF_DAY] || stamp[AM_PM] > stamp[HOUR_OF_DAY];
        long hour;
        if (ampm) {
            hour = (isSet[HOUR] ? fields[HOUR] : 0) + 12L * (isSet[AM_PM] ? fields[AM_PM] : 0);
        } else {
            hour = isSet[HOUR_OF_DAY] ? fields[HOUR_OF_DAY] : 0;
        }
        ms = hour * 3600000L + (isSet[MINUTE] ? fields[MINUTE] : 0) * 60000L + (isSet[SECOND] ? fields[SECOND] : 0) * 1000L
                + (isSet[MILLISECOND] ? fields[MILLISECOND] : 0);
        TimeZone tz = getTimeZone();
        long localMs = days * DAY_MS + ms;
        int off = tz.getRawOffset();
        if (tz.useDaylightTime()) {
            long[] ymd = civilFromDays(floorDiv(localMs, DAY_MS));
            off = tz.getOffset(1, (int) ymd[0], (int) ymd[1] - 1, (int) ymd[2], (int) floorMod(floorDiv(localMs, DAY_MS) + 4, 7) + 1,
                    (int) floorMod(localMs, DAY_MS));
        }
        time = localMs - off;
    }
}
