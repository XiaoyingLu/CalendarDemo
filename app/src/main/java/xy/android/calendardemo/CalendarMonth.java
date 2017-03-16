package xy.android.calendardemo;

import java.util.List;

public class CalendarMonth {

    private int month; //月份
    private int year; //年份
    private List<Day> days; //每个月的天数

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
