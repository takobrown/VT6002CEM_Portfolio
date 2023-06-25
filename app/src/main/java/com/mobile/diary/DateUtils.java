package com.mobile.diary;

import com.github.gzuliyujiang.calendarpicker.core.Interval;
import com.github.gzuliyujiang.calendarpicker.core.NumInterval;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateUtils {

    public static Calendar calendar(long timeInMillis) {
        return calendar(new Date(timeInMillis));
    }

    public static Calendar calendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        return calendar;
    }


    public static int maxDaysOfMonth(Date date) {
        return calendar(date).getActualMaximum(Calendar.DATE);
    }


    public static int firstDayOfMonthIndex(Date date) {
        Calendar calendar = calendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }


    public static int isTodayOfMonth(Date date) {
        Calendar current = calendar(new Date());
        Calendar calendar = calendar(date);
        if (diverse(current, calendar, Calendar.YEAR)) {
            return -1;
        }
        if (diverse(current, calendar, Calendar.MONTH)) {
            return -1;
        }
        return current.get(Calendar.DAY_OF_MONTH) - 1;
    }


    public static boolean diverse(Calendar calendarA, Calendar calendarB, int field) {
        boolean same;
        try {
            same = calendarA.get(field) == calendarB.get(field);
        } catch (Exception e) {
            same = false;
        }
        return !same;
    }


    public static int months(Date sDate, Date eDate) {
        Calendar before = calendar(min(sDate, eDate));
        Calendar after = calendar(max(sDate, eDate));
        int diffYear = after.get(Calendar.YEAR) - before.get(Calendar.YEAR);
        int diffMonth = after.get(Calendar.MONTH) - before.get(Calendar.MONTH);
        return diffYear * 12 + diffMonth;
    }

    public static Date max(Date sDate, Date eDate) {
        return sDate.getTime() > eDate.getTime() ? sDate : eDate;
    }

    public static Date min(Date sDate, Date eDate) {
        return sDate.getTime() > eDate.getTime() ? eDate : sDate;
    }


    public static List<Date> fillDates(Date sDate, Date eDate) {
        List<Date> dates = new ArrayList<>();
        if (null == sDate || null == eDate) {
            dates.add(new Date());
        } else {
            Calendar calendar = calendar(min(sDate, eDate));
            int months = months(sDate, eDate);
            for (int i = 0; i <= months; i++) {
                dates.add(calendar.getTime());
                calendar.add(Calendar.MONTH, 1);
            }
        }
        return dates;
    }


    public static NumInterval daysInterval(Date month, Interval<Date> dateInterval) {
        final NumInterval range = new NumInterval();
        if (null == month || null == dateInterval) {
            return range;
        }
        final int maxDaysOfMonth = maxDaysOfMonth(month);
        Date sDay;
        Date eDay;

        if (null == dateInterval.left()) {
            Calendar safeCalendar = calendar(month);
            safeCalendar.set(Calendar.DAY_OF_MONTH, 1);
            sDay = safeCalendar.getTime();
        } else {
            sDay = new Date(dateInterval.left().getTime());
        }
        if (null == dateInterval.right()) {
            Date date = max(sDay, month);
            Calendar safeCalendar = calendar(date);
            safeCalendar.set(Calendar.DAY_OF_MONTH, maxDaysOfMonth);
            eDay = safeCalendar.getTime();
        } else {
            eDay = new Date(dateInterval.right().getTime());
        }

        sDay = min(sDay, eDay);
        eDay = max(sDay, eDay);

        Calendar[] calendars = new Calendar[]{calendar(month), calendar(sDay), calendar(eDay)};
        Calendar miniYearCalendar = calendars[0];
        for (int i = 1; i < calendars.length; i++) {
            if (miniYearCalendar.get(Calendar.YEAR) > calendars[i].get(Calendar.YEAR)) {
                miniYearCalendar = calendars[i];
            }
        }
        final long miniDate = miniYearCalendar.getTime().getTime();
        long[] diffDays = new long[calendars.length];
        for (int i = 0; i < calendars.length; i++) {
            Calendar cal = calendar(new Date(miniDate));
            int diffYear = calendars[i].get(Calendar.YEAR) - cal.get(Calendar.YEAR);
            for (int j = 0; j < diffYear; j++) {
                diffDays[i] += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                cal.add(Calendar.YEAR, 1);
            }
        }
        calendars[0].set(Calendar.DAY_OF_MONTH, 1);
        final long dayIndex = diffDays[0] + calendars[0].get(Calendar.DAY_OF_YEAR);
        final long limitA = diffDays[1] + calendars[1].get(Calendar.DAY_OF_YEAR);
        final long limitB = diffDays[2] + calendars[2].get(Calendar.DAY_OF_YEAR);

        long temp;
        for (int i = 0; i < maxDaysOfMonth; i++) {
            temp = dayIndex + i;
            boolean contain = (temp >= limitA) && (temp <= limitB);
            if (!contain) {
                continue;
            }
            if (range.left() < 0) {
                range.left(i);
            }
            range.right(i);
            if (limitA == temp) {
                range.lBound(i);
            }
            if (limitB == temp) {
                range.rBound(i);
            }
        }
        return range;
    }


    public static Date specialDayInMonth(Date month, int index) {
        Calendar calendar = calendar(month);
        calendar.set(Calendar.DAY_OF_MONTH, index + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    public static Date getLastDayFromMonth(Date date) {
        Calendar calendar = calendar(date);
        calendar.set(Calendar.DAY_OF_MONTH, maxDaysOfMonth(date));
        return calendar.getTime();
    }


    public static Date getDayYearAgo(Date date) {
        Calendar calendar = calendar(date);
        calendar.add(Calendar.MONTH, -11);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return calendar.getTime();
    }


    public static String timeToString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(calendar.getTime());
    }
}
