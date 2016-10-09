package com.cherednichenko.antonina.detoxdiet.detox_diet_program_info;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cherednichenko.antonina.detoxdiet.NotificationService;
import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ProgramInfoActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ProgramInfo receipe;

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

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dayList.setLayoutManager(llm);

        DayAdapter adapter = new DayAdapter(this, receipe.getDays());
        dayList.setAdapter(adapter);

        collapsingToolbar.setTitle(receipe.getName());
        ImageView programImage = (ImageView) findViewById(R.id.toolbar_header_image);
        programImage.setImageResource(receipe.getPhotoId());

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


//                Intent intent = new Intent(Intent.ACTION_INSERT);
//                intent.setType("vnd.android.cursor.item/event");
//
//                Calendar cal = Calendar.getInstance();
//                long startTime = cal.getTimeInMillis();
//                long endTime = cal.getTimeInMillis() + receipe.getDuration() * 24 * 60 * 60 * 1000;
//
//                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
//                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
//                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
//
//                intent.putExtra(CalendarContract.Events.TITLE, receipe.getName());
//                intent.putExtra(CalendarContract.Events.DESCRIPTION, receipe.getShortDescription());
//
//
//                startActivity(intent);
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

        alarm.set(
                // This alarm will wake up the device when System.currentTimeMillis()
                // equals the second argument value
                alarm.RTC_WAKEUP,
                System.currentTimeMillis() + (1000 * 2 * 60),
                pendingIntent
        );

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar now = Calendar.getInstance();

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                ProgramInfoActivity.this,
                now.get(Calendar.HOUR),
                0,
                true
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }
}
