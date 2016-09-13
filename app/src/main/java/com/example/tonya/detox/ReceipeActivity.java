package com.example.tonya.detox;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;

public class ReceipeActivity extends AppCompatActivity {

    private ReceipeInfo receipe;
    private String programName;
    private String programDescription;
    private String shortDescription;
    private int programDuration;

    private int[] images = {R.drawable.green_detox_program, R.drawable.citrus_detox_program, R.drawable.apple_detox_program,
            R.drawable.juice_detox_program, R.drawable.rice_detox_program};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);

        Intent intent = getIntent();
        receipe = (ReceipeInfo) intent.getSerializableExtra("receipe_info");

        RecyclerView dayList = (RecyclerView) findViewById(R.id.dayCardList);
        dayList.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dayList.setLayoutManager(llm);

        DayAdapter adapter = new DayAdapter(this, receipe.days);
        dayList.setAdapter(adapter);

        collapsingToolbar.setTitle(this.programName);
        ImageView programImage = (ImageView) findViewById(R.id.toolbar_header_image);
        programImage.setImageResource(receipe.photoId);

        FloatingActionButton schedule = (FloatingActionButton) findViewById(R.id.schedule_floating_button);
        schedule.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");

                Calendar cal = Calendar.getInstance();
                long startTime = cal.getTimeInMillis();
                long endTime = cal.getTimeInMillis() + programDuration * 24 * 60 * 60 * 1000;

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

                intent.putExtra(CalendarContract.Events.TITLE, programName);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, shortDescription);


                startActivity(intent);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
