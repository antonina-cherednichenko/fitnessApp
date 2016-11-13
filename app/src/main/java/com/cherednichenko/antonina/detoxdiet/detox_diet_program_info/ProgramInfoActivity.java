package com.cherednichenko.antonina.detoxdiet.detox_diet_program_info;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherednichenko.antonina.detoxdiet.NotificationService;
import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.util.Calendar;

public class ProgramInfoActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ProgramInfo receipe;

    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);

        Intent intent = getIntent();
        receipe = (ProgramInfo) intent.getSerializableExtra("receipe_info");

        RecyclerView dayList = (RecyclerView) findViewById(R.id.dayCardList);
        dayList.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dayList.setLayoutManager(llm);

        DayAdapter adapter = new DayAdapter(this, receipe.getDays());
        dayList.setAdapter(adapter);

        collapsingToolbar.setTitle(" ");
        ImageView programImage = (ImageView) findViewById(R.id.toolbar_header_image);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(receipe.getName());
                    isShow = true;
                } else if (isShow) {
                    //carefull there should a space between double quote otherwise it wont work
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        Picasso
                .with(this)
                .load(receipe.getPhotoURL())
                .fit()
                .into(programImage);

        FloatingActionButton schedule = (FloatingActionButton) findViewById(R.id.schedule_floating_button);
        schedule.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();


                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ProgramInfoActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("receipe_info", receipe);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                serviceIntent, PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.monthOfYear, this.dayOfMonth, hourOfDay, minute, second);

        long scheduleTime = calendar.getTimeInMillis();
        alarm.set(
                // This alarm will wake up the device when System.currentTimeMillis()
                // equals the second argument value
                alarm.RTC_WAKEUP,
                scheduleTime,
                pendingIntent
        );

        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(this);
        databaseHelper.addEvent(receipe, scheduleTime);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                ProgramInfoActivity.this,
                now.get(Calendar.HOUR),
                0,
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }
}
