package com.example.tonya.detox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);

        Intent intent = getIntent();
        position = intent.getIntExtra("receipe_position", 0);

        RecyclerView dayList = (RecyclerView) findViewById(R.id.dayCardList);
        dayList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dayList.setLayoutManager(llm);

        DayAdapter adapter = new DayAdapter(this, createDayList(position));
        dayList.setAdapter(adapter);
    }

    private List<DayInfo> createDayList(int position) {
        List<DayInfo> days = new ArrayList<>();
        try {
            String chatFileData = loadProgramsData();
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            JSONObject program = allPrograms.getJSONObject(position);
            JSONArray schedule = program.getJSONArray("schedule");
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

}
