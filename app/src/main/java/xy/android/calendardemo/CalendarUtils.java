package xy.android.calendardemo;

import android.text.format.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {

    private static int dayOfMonth, monthOfYear, curYear, curDate;

    static {
        dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        monthOfYear = Calendar.getInstance().get(Calendar.MONTH) + 1;
        curYear = Calendar.getInstance().get(Calendar.YEAR);
        curDate = Calendar.getInstance().get(Calendar.DATE);
    }


    /**
     * Gets the days in month.
     *
     * @param month the month
     * @param year  the year
     * @return the days in month
     */
    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    /**
     * Gets the flow month days.
     *
     * @param flowMonth the flow month
     * @return the flow month days
     */
    public static int getFlowMonthDays(int flowMonth) {
        int totalDays = 0;
        for (int i = 0; i < flowMonth; i++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, i + 1);
            int days = getDaysInMonth(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
            totalDays += days;
        }
        return totalDays;
    }

    /**
     * Current month remain days.
     *
     * @return the int
     */
    public static int currentMonthRemainDays() {
        Calendar c = Calendar.getInstance();
        return getDaysInMonth(c.get(Calendar.MONTH), c.get(Calendar.YEAR)) - c.get(Calendar.DAY_OF_MONTH) + 1;
    }

    /**
     * Through month.
     *
     * @param calendar the calendar
     * @param passDays the pass days
     * @return the int
     */
    public static int throughMonth(Calendar calendar, int passDays) {
        Calendar c = (Calendar) calendar.clone();
        int curMonth = c.get(Calendar.MONTH);
        int curYear = c.get(Calendar.YEAR);
        c.add(Calendar.DAY_OF_MONTH, passDays - 1);
        int monthCount = (c.get(Calendar.YEAR) - curYear) * 12 + (c.get(Calendar.MONTH) - curMonth);
        return monthCount;
    }

    /**
     * Gets the days of month.
     *
     * @param calendar the calendar
     * @return the days of month
     */
    public static String[] getDaysOfMonth(Calendar calendar) {
        Calendar month = (Calendar) calendar.clone();
        String[] days;
        final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1
        month.set(Calendar.DAY_OF_MONTH, 1);
        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH); //当前月份的天数
        int firstDay = (int) month.get(Calendar.DAY_OF_WEEK); //当前日期是礼拜几, 星期日：1

        if (firstDay == 1) {
            days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
        } else {
            days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
        }

        int j = FIRST_DAY_OF_WEEK;

        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                days[j] = "";
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days[j] = "";
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        int dayNumber = 1;
        for (int i = j - 1; i < days.length; i++) {
            days[i] = "" + dayNumber;
            dayNumber++;
        }
        return days;
    }

    /**
     * Gets the days of month.
     *
     * @param calendar the calendar
     * @return the days of month
     */
    public static List<String> getDayListOfMonth(Calendar calendar) {
        Calendar month = (Calendar) calendar.clone();
        List<String> days = new ArrayList<>();
        final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1
        month.set(Calendar.DAY_OF_MONTH, 1); //把当前的日期设置为这个月的第一天
        int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH); //当前月份的天数
        int firstDay = (int) month.get(Calendar.DAY_OF_WEEK); //当前日期是礼拜几, 星期日：1

//		if (firstDay == 1) {
//			days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6) ];
//		} else {
//			days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
//		}

        int j = FIRST_DAY_OF_WEEK;

        if (firstDay > 1) {
            for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK - 1; j++) {
                days.add("");
            }
        } else {
            for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days.add("");
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        for (int dayNumber = 1; dayNumber <= lastDay; dayNumber++) {
            days.add(dayNumber + "");
        }

        int remains = days.size() % 7;
        if (remains > 0) {
            for (int k = 0; k < 7 - remains; k++) {
                days.add("");
            }
        }
        return days;
    }

    public static boolean sameDay(int monthDay, Time time) {
        return (curYear == time.year) && (monthOfYear == time.month) && (monthDay == time.monthDay);
    }

    public static boolean isToday(Day monthDay) {
        return (curYear == monthDay.getYear()) && (monthOfYear == monthDay.getMonth()) && (curDate == monthDay.getDay());
    }

    /**
     * 判断是否是已经过去的日期
     *
     * @param monthDay
     * @param time
     * @return
     */
    public static boolean prevDay(int monthDay, Time time) {
        return ((curYear < time.year)) || (curYear == time.year && monthOfYear < time.month) ||
                (curYear == time.year && monthOfYear == time.month && monthDay < time.monthDay);
    }

    public static boolean prevDay(Day monthDay) {
        return ((curYear > monthDay.getYear())) || (curYear == monthDay.getYear() && monthOfYear > monthDay.getMonth()) ||
                (curYear == monthDay.getYear() && monthOfYear == monthDay.getMonth() && curDate > monthDay.getDay());
    }

    /**
     * 判断 monthDay 是否在 monthDay2 之前
     *
     * @param monthDay
     * @param monthDay2
     * @return
     */
    public static boolean prevDay(Day monthDay, Day monthDay2) {
        return ((monthDay2.getYear() > monthDay.getYear())) || (monthDay2.getYear() == monthDay.getYear() && monthDay2.getMonth() > monthDay.getMonth()) ||
                (monthDay2.getYear() == monthDay.getYear() && monthDay2.getMonth() == monthDay.getMonth() && monthDay2.getDay() > monthDay.getDay());

    }

    /**
     * 获取两个日期的时间间隔
     *
     * @param first
     * @param last
     * @return
     */
    public static int getIntervalDays(Day first, Day last) {
        Day firstDay;
        Day lastDay;
        if (first.getDay() == last.getDay() && first.getMonth() == last.getMonth() && first.getYear() == last.getYear()) {
            return 0;
        }
        if (prevDay(first, last)) {
            firstDay = first;
            lastDay = last;
        } else {
            firstDay = last;
            lastDay = first;
        }
        //同年同月
        if (firstDay.getYear() == lastDay.getYear() && firstDay.getMonth() == lastDay.getMonth()) {
            return Math.abs(firstDay.getDay() - lastDay.getDay()) + 1;
        }
        int firstMonth = firstDay.getMonth();
        int lastMonth = lastDay.getMonth();
        int days = 0;
        int firstDays = getDaysInMonth(firstMonth - 1, firstDay.getYear());
        //同年
        if (firstDay.getYear() == lastDay.getYear()) {
            for (int i = firstMonth + 1; i < lastMonth; i++) {
                days += getDaysInMonth(firstMonth + i - 1, firstDay.getYear());
            }
            return firstDays - firstDay.getDay() + 1 + days + lastDay.getDay();

        }

        //不同年
        for (int i = firstMonth + 1; i <= 12 - firstMonth; i++) {
            days += getDaysInMonth(firstMonth + i - 1, firstDay.getYear());
        }
        for (int j = 1; j < lastMonth; j++) {
            days += getDaysInMonth(j - 1, lastDay.getYear());
        }
        return firstDays - firstDay.getDay() + 1 + days + lastDay.getDay();
    }

    /**
     * 获取一个月y中星期x 对应的日期
     * @param calendar y
     * @param weekDay 星期x
     * @param offDay 起始时间偏量
     */
    public static List<Date> getWeekDayInMonth(Calendar calendar, int[] weekDay, int offDay) {
        Calendar month = (Calendar) calendar.clone();
        month.add(Calendar.DATE, offDay);
        List<Date> dates = new ArrayList<>();
        month.set(curYear, monthOfYear-1, curDate);
        for (int i =0; i < 365; i++) {
            month.add(Calendar.DATE, 1);
            for (int j = 0; j < weekDay.length; j++) {
                if ((month.get(Calendar.DAY_OF_WEEK) - 1) == weekDay[j]) {
                    Date date = month.getTime();
                    dates.add(date);
                }
            }
        }
        return dates;
    }
}