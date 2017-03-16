package xy.android.calendardemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {

    private CalendarAdapter mCalendarAdapter;
    private RecyclerView recyclerView;
    private TextView tvSelectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_calendar);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        tvSelectedDate = (TextView)findViewById(R.id.tv_selected_date);

        initResAndListener();
    }

    private void initResAndListener() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mCalendarAdapter = new CalendarAdapter(this, new DatePickerController() {

            @Override
            public void onDayOfMonthSelected(Day day) {
                tvSelectedDate.setText(String.format(getString(R.string.selected_single_date), day.getYear(),
                        day.getMonth(), day.getDay()));
            }

            @Override
            public void onDateRangeSelected(Day firstDay, Day lastDay, int spanDays) {
                tvSelectedDate.setText(String.format(getString(R.string.invite_selected_dates), firstDay.getYear(),
                        firstDay.getMonth(), firstDay.getDay(), lastDay.getYear(),
                        lastDay.getMonth(), lastDay.getDay()));
            }

            @Override
            public void onDateInllegal() {

            }
        });
        recyclerView.setAdapter(mCalendarAdapter);
    }
}
