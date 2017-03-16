package xy.android.calendardemo;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final DatePickerController mController;
    private SelectedDays<Day> rangeDays;                        // 选择日期范围
    private Calendar calendar;
    private final LayoutInflater mLayoutInflater;
    private List<CalendarMonth> mCalendarList;
    private final int screenWidth;
    private int clickCount = 0;
    private int mSpanDays;

    public CalendarAdapter(Context context, DatePickerController datePickerController) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mController = datePickerController;
        screenWidth = Utils.getScreenWidth(mContext);
        initData();
    }

    private void initData() {
        rangeDays = new SelectedDays<>();
        mCalendarList = new ArrayList<>();
        calendar = Calendar.getInstance();

        int startMonth = calendar.get(Calendar.MONTH) + 1; //2
        int startYear = calendar.get(Calendar.YEAR);
        List<String> days;
        List<Day> dayList;

        for (int i = 0; i <= 12; i++) {
            CalendarMonth calendarMonth = new CalendarMonth();
            dayList = new ArrayList<>();
            if (i > 0) {
                calendar.add(Calendar.MONTH, 1); //每次月份+1
            }
            days = CalendarUtils.getDayListOfMonth(calendar);
            for (String day : days) {
                Day dayItem = new Day();
                dayItem.setEnable(false);
                dayItem.setOrder(false);
                if (null != day && !day.equals("")) {
                    dayItem.setDay(Integer.valueOf(day));
                }
                dayItem.setYear(startMonth + i > 12 ? startYear + 1 : startYear);
                dayItem.setMonth((startMonth + i) % 12 == 0 ? 12 : (startMonth + i) % 12);
                dayList.add(dayItem);
            }
            calendarMonth.setMonth((startMonth + i) % 12 == 0 ? 12 : (startMonth + i) % 12);
            calendarMonth.setYear(startMonth + i > 12 ? startYear + 1 : startYear);
            calendarMonth.setDays(dayList);
            mCalendarList.add(calendarMonth);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.layout_month_item, parent, false);
        return new MonthHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MonthHolder monthHolder = (MonthHolder) holder;

        final CalendarMonth calendarMonth = mCalendarList.get(position);
        String[] months = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        int index = calendarMonth.getMonth() % 12 == 0 ? 11 : calendarMonth.getMonth() % 12 - 1;
        monthHolder.tvMonth.setText(months[index]);

        monthHolder.rvDays.setLayoutManager(new GridLayoutManager(mContext, 7));
//        monthHolder.rvDays.setHasFixedSize(true);

        //设置子recyclerView 高度
//        ViewGroup.LayoutParams layoutParams = monthHolder.rvDays.getLayoutParams();
//        layoutParams.height = (int)(calendarMonth.getDays().size()/7 * Math.ceil(screenWidth/7.0));
//        monthHolder.rvDays.setLayoutParams(layoutParams);

        MonthDayAdapter monthDayAdapter = new MonthDayAdapter(mContext, calendarMonth.getDays(),
                calendarMonth.getYear(), calendarMonth.getMonth());
        monthHolder.rvDays.setAdapter(monthDayAdapter);

        monthDayAdapter.setOnDayClickListener(new MonthDayAdapter.OnDayClickListener() {
            @Override
            public void onDayClick(int dayPos, Day day) {
                if (mController != null) {
                    mController.onDayOfMonthSelected(day);
                }
                if (clickCount >= 2) {
                    clickCount--;
                } else if (rangeDays.first != null && day.equals(rangeDays.first)) {
                    clickCount = 1;
                } else {
                    clickCount++;
                }
                switch (clickCount) {
                    case 1:
                        rangeDays.first = day;
                        rangeDays.firstPos = dayPos;
                        clearListLabel();
                        day.setSingleChosen(true);
//                        dayHolder.tvDay.setTextColor(mContext.getResources().getColor(R.color.app_blue));
//                        dayHolder.tvDay.setBackgroundResource(R.drawable.circle_day);
                        calendarMonth.getDays().set(dayPos, day);
                        notifyDataSetChanged();
                        if (null != mController) {
                            mController.onDayOfMonthSelected(day);
                        }
                        break;
                    case 2:
                        rangeDays.last = day;
                        rangeDays.lastPos = dayPos;
                        clearListLabel();
                        setRangeSelectedDay(rangeDays, position, dayPos);
//                        mController.onDateRangeSelected(rangeDays);
                        break;
                }
            }
        });
    }

    /**
     * 清除所有选择标记
     */
    public void clearListLabel() {
        for (CalendarMonth calendar : mCalendarList) {
            for (Day day : calendar.getDays()) {
                day.setFirst(false);
                day.setLast(false);
                day.setSingleChosen(false);
                day.setOrder(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 范围选时对点击的日期的处理
     *
     * @param day
     * @param position
     * @param dayPos
     */
    public void setRangeSelectedDay(SelectedDays<Day> day, int position, int dayPos) {
        Day first = day.first;
        Day last = day.last;
        int mSpanDays = CalendarUtils.getIntervalDays(first, last);
        if (mSpanDays > 30) {
            mController.onDateInllegal();
            return;
        }
//        if (CalendarUtils.prevDay(day, rangeDays.first)) {
//            Day middle = rangeDays.first;
//            rangeDays.first = day;
//
//        }

        CalendarMonth calendarMonth = mCalendarList.get(position);
        List<Day> dayList = calendarMonth.getDays();
        if (first.getYear() == last.getYear() && first.getMonth() == last.getMonth()) {
            //同月内的两个日期
            int diff = Math.abs(last.getDay() - first.getDay());
            if (first.getDay() > last.getDay()) {
                dayPos = dayPos + diff;
            }
            Day dayLast = dayList.get(dayPos);
            dayLast.setLast(true);
            Day dayFirst = dayList.get(dayPos - diff);
            dayFirst.setFirst(true);
            dayList.set(dayPos, dayLast);
            dayList.set(dayPos - diff, dayFirst);
            for (int i = dayPos - diff + 1; i < dayPos; i++) {
                Day dayOrder = dayList.get(i);
                dayOrder.setOrder(true);
                dayList.set(i, dayOrder);
            }
            calendarMonth.setDays(dayList);
            mCalendarList.set(position, calendarMonth);
            mController.onDateRangeSelected(dayFirst, dayLast, dayLast.getDay() - dayFirst.getDay() + 1);
        } else if (first.getYear() == last.getYear()) { //同年不同月
            int monthDiff = Math.abs(last.getMonth() - first.getMonth());
            int pos = position; //标记最后一个月在集合中的角标
            if (first.getMonth() > last.getMonth()) {
                dayPos = day.firstPos;
                day.firstPos = day.lastPos;
                pos = position + monthDiff;
            }

            setSelectedDays(dayPos, pos, monthDiff, day);
        } else { //不同年
            int monthDiff = last.getMonth() + 12 - first.getMonth();
            int pos = position; //标记最后一个月在集合中的角标
            if (first.getYear() > last.getYear()) {
                monthDiff = first.getMonth() + 12 - last.getMonth();
                dayPos = day.firstPos;
                day.firstPos = day.lastPos;
                pos = position + monthDiff;
            }
            setSelectedDays(dayPos, pos, monthDiff, day);
        }

        notifyDataSetChanged();
    }

    /**
     * @param dayPos    最后一个日期在List<Day>中的位置
     * @param pos       最后一个月在集合中的位置
     * @param monthDiff 所选日历区间差几个月
     * @param day
     */
    private void setSelectedDays(int dayPos, int pos, int monthDiff, SelectedDays<Day> day) {
        int spanDays = 2;

        CalendarMonth lastCalendar = mCalendarList.get(pos);
        List<Day> lastDays = lastCalendar.getDays();
        Day dayLast = lastDays.get(dayPos);
        dayLast.setLast(true);
        lastDays.set(dayPos, dayLast);
        for (int i = 0; i < dayPos; i++) {
            Day dayOrder = lastDays.get(i);
            dayOrder.setOrder(true);
            lastDays.set(i, dayOrder);
            if (dayOrder.getDay() != 0) {
                spanDays++;
            }
        }
        lastCalendar.setDays(lastDays);
        mCalendarList.set(pos, lastCalendar);

        CalendarMonth firstCalendar = mCalendarList.get(pos - monthDiff);
        List<Day> firstDays = firstCalendar.getDays();
        Day dayFirst = firstDays.get(day.firstPos);
        dayFirst.setFirst(true);
        firstDays.set(day.firstPos, dayFirst);
        for (int j = day.firstPos + 1; j < firstDays.size(); j++) {
            Day dayOrder = firstDays.get(j);
            dayOrder.setOrder(true);
            firstDays.set(j, dayOrder);
            if (dayOrder.getDay() != 0) {
                spanDays++;
            }
        }
        firstCalendar.setDays(firstDays);
        mCalendarList.set(pos - monthDiff, firstCalendar);

        for (int k = pos - monthDiff + 1; k < pos; k++) {
            CalendarMonth month = mCalendarList.get(k);
            List<Day> days = month.getDays();
            for (Day dayItem : days) {
                dayItem.setOrder(true);
                if (dayItem.getDay() != 0) {
                    spanDays++;
                }
            }
        }
        mSpanDays = spanDays;
        mController.onDateRangeSelected(dayFirst, dayLast, spanDays);
    }

    class MonthHolder extends RecyclerView.ViewHolder {
        TextView tvMonth;
        RecyclerView rvDays;

        public MonthHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month_name);
            rvDays = (RecyclerView) itemView.findViewById(R.id.rv_days);
        }
    }

    @Override
    public int getItemCount() {
        return mCalendarList.size();
    }

    public static class SelectedDays<K> implements Serializable {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;
        private int firstPos;
        private int lastPos;

        public SelectedDays() {
        }

        public SelectedDays(K first, K last) {
            this.first = first;
            this.last = last;
        }

        public K getFirst() {
            return first;
        }

        public void setFirst(K first) {
            this.first = first;
        }

        public K getLast() {
            return last;
        }

        public void setLast(K last) {
            this.last = last;
        }

        public int getFirstPos() {
            return firstPos;
        }

        public void setFirstPos(int firstPos) {
            this.firstPos = firstPos;
        }

        public int getLastPos() {
            return lastPos;
        }

        public void setLastPos(int lastPos) {
            this.lastPos = lastPos;
        }
    }
}
