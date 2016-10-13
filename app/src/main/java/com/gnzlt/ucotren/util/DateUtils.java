package com.gnzlt.ucotren.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtils {

    public static final int ONE_MINUTE_IN_MILLIS = 60000;
    public static final int DEFAULT_MARGIN_IN_MINS = 10;

    public static final SimpleDateFormat HHmmFormat = new SimpleDateFormat("HH:mm", Locale.US);
    private static final SimpleDateFormat ddMMyyFormat = new SimpleDateFormat("ddMMyy", Locale.US);
    private static final SimpleDateFormat ddMMyyHHmmFormat = new SimpleDateFormat("ddMMyy HH:mm", Locale.US);
    private static final SimpleDateFormat NewDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

    public static Date getCurrentDayInParseFormat() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return calendar.getTime();
    }

    public static Date getDateFromDepartureHour(String departureHour) throws ParseException {
        return ddMMyyHHmmFormat.parse(ddMMyyFormat.format(new Date()) + " " + departureHour);
    }

    public static String getFormattedNewDate(Date date) {
        return NewDateFormat.format(date);
    }

    public static Date getDateWithMargin(Date date, int minutes) {
        return new Date(date.getTime() + (minutes * ONE_MINUTE_IN_MILLIS));
    }

    public static boolean isValidBusDay(String workingDays) {
        int currentDayOfTheWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        boolean isSaturday = currentDayOfTheWeek == Calendar.SATURDAY;
        boolean isSunday = currentDayOfTheWeek == Calendar.SUNDAY;
        boolean isWeekend = isSaturday || isSunday;

        switch (workingDays) {
            case "0":
                return !isWeekend;
            case "1":
                return isSaturday;
            case "2":
                return isSunday;
            case "3":
                return isWeekend;
            case "4":
                return !isSunday;
            default:
                return true;
        }
    }
}
