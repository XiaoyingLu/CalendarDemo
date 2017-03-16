package xy.android.calendardemo;

public interface DatePickerController {


    void onDayOfMonthSelected(Day day);          // 点击日期回调函数，月份记得加1

    void onDateRangeSelected(Day firstDay, Day lastDay, int spanDays);    // 选择范围回调函数，月份记得加1

    void onDateInllegal();  //所选日期超过30天回调
}
