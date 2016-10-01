package com.cherednichenko.antonina.detoxdiet.detox_diet_data;

import android.content.Context;

import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;

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

    private static boolean dbIsInitilized = false;

    public static List<ProgramInfo> getAllPrograms(Context context) {
        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
        if (!dbIsInitilized) {
            dbIsInitilized = true;
            readJsonAndInitDb(context);
        }
        return databaseHelper.getAllPrograms();
    }

    public static List<ProgramInfo> getLikedPrograms(Context context) {
        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
        if (!dbIsInitilized) {
            dbIsInitilized = true;
            readJsonAndInitDb(context);
        }

        List<ProgramInfo> allPrograms = databaseHelper.getAllPrograms();
        List<ProgramInfo> likedPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getLiked() == 1) {
                likedPrograms.add(program);
            }
        }
        return likedPrograms;
    }

    public static List<ProgramInfo> getDietPrograms(List<ProgramInfo> allPrograms) {
        List<ProgramInfo> dietPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getCategory().equals("diet")) {
                dietPrograms.add(program);
            }
        }
        return dietPrograms;

    }

    public static List<ProgramInfo> getDetoxPrograms(List<ProgramInfo> allPrograms) {
        List<ProgramInfo> detoxPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getCategory().equals("detox")) {
                detoxPrograms.add(program);
            }
        }
        return detoxPrograms;

    }

    public static List<ProgramInfo> getNewPrograms(List<ProgramInfo> allPrograms) {
        List<ProgramInfo> newPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getIsNew() == 1) {
                newPrograms.add(program);
            }
        }
        return newPrograms;
    }

    public static List<ProgramInfo> getRecommendedPrograms(List<ProgramInfo> allPrograms) {
        List<ProgramInfo> recommendedPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getIsNew() == 1) {
                recommendedPrograms.add(program);
            }
        }
        return recommendedPrograms;
    }

    public static List<ProgramInfo> getSearchPrograms(Context context, String query) {
        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
        if (!dbIsInitilized) {
            dbIsInitilized = true;
            readJsonAndInitDb(context);
        }

        List<ProgramInfo> allPrograms = databaseHelper.getAllPrograms().subList(0, 1);
        return allPrograms;
    }


    private static void readJsonAndInitDb(Context context) {
        InputStream stream = context.getResources().openRawResource(R.raw.programs_data);
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
                receipe.setCategory(program.getString("category"));
                receipe.setLiked(0);
                receipe.setRecommended(program.getInt("recommended"));
                receipe.setIsNew(program.getInt("new"));
                receipe.setName(program.getString("name"));
                receipe.setPhotoId(images[i]);
                receipe.setShortDescription(program.getString("shortDescription"));
                receipes.add(receipe);

            }

            ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
            for (ProgramInfo program : receipes) {
                databaseHelper.addProgram(program);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


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
