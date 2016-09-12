package com.example.tonya.detox;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReceipeActivity extends AppCompatActivity {

    private int position;
    private String programName;
    private String programDescription;

    private int[] images = {R.drawable.green_detox_program, R.drawable.citrus_detox_program, R.drawable.apple_detox_program,
            R.drawable.juice_detox_program, R.drawable.rice_detox_program};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);

        Intent intent = getIntent();
        position = intent.getIntExtra("receipe_position", 0);

        RecyclerView dayList = (RecyclerView) findViewById(R.id.dayCardList);
        dayList.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dayList.setLayoutManager(llm);

        DayAdapter adapter = new DayAdapter(this, createDayList(position));
        dayList.setAdapter(adapter);

        collapsingToolbar.setTitle(this.programName);
        ImageView programImage = (ImageView) findViewById(R.id.toolbar_header_image);
        programImage.setImageResource(images[position]);

        FloatingActionButton schedule = (FloatingActionButton) findViewById(R.id.schedule_floating_button);
        schedule.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceipeActivity.this, ScheduleProgram.class);
                startActivity(intent);
            }
        });


    }

    private List<DayInfo> createDayList(int position) {
        List<DayInfo> days = new ArrayList<>();
        try {
            String chatFileData = loadProgramsData();
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            JSONObject program = allPrograms.getJSONObject(position);
            JSONArray schedule = program.getJSONArray("schedule");
            this.programName = program.getString("name");
            this.programDescription = program.getString("description");
            for (int i = 0; i < schedule.length(); i++) {
                JSONObject day = schedule.getJSONObject(i);
                days.add(new DayInfo(day.getString("name"), day.getString("description")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }

    private String loadProgramsData() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.programs_data);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append(receiveString);
            stringBuilder.append("\n");
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();


        return stringBuilder.toString();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
