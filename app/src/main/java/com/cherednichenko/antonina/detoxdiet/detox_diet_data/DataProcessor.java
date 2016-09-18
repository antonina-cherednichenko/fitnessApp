package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

import com.cherednichenko.antonina.detoxdiet.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/18/16.
 */
public class DataProcessor {
    private static int[] images = {R.drawable.green_detox_program, R.drawable.citrus_detox_program, R.drawable.apple_detox_program,
            R.drawable.juice_detox_program, R.drawable.rice_detox_program};


    public static List<ProgramInfo> createReceipeList(InputStream stream) {
        List<ProgramInfo> receipes = new ArrayList<>();
        try {
            String chatFileData = loadProgramsData(stream);
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            for (int i = 0; i < allPrograms.length(); i++) {
                JSONObject program = allPrograms.getJSONObject(i);
                JSONArray schedule = program.getJSONArray("schedule");
                List<DayInfo> days = new ArrayList<>();
                for (int j = 0; j < schedule.length(); j++) {
                    JSONObject day = schedule.getJSONObject(j);
                    days.add(new DayInfo(day.getString("name"), day.getString("description")));
                }

                ProgramInfo receipe = new ProgramInfo();
                receipe.setDays(days);
                receipe.setDescription(program.getString("description"));
                receipe.setDuration(program.getInt("duration"));
                receipe.setLiked(false);
                receipe.setName(program.getString("name"));
                receipe.setPhotoId(images[i]);
                receipe.setShortDescription(program.getString("shortDescription"));
                receipes.add(receipe);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receipes;
    }

    private static String loadProgramsData(InputStream stream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append(receiveString);
            stringBuilder.append("\n");
        }

        bufferedReader.close();
        inputStreamReader.close();
        stream.close();

        return stringBuilder.toString();
    }
}
