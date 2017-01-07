package com.freeta.daily.fitness.detox_diet_data;

import android.content.Context;

import com.freeta.daily.fitness.R;
import com.freeta.daily.fitness.db.ProgramsDatabaseHelper;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/18/16.
 */
public class DataProcessor {

    private static boolean dbIsInitilized = false;

    public static String DATA_FILENAME = "programs_data_content";

    public static int version = 0;

    public static void updateDataBase(int v) {
        version = v;
        dbIsInitilized = false;
    }

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

    public static List<ProgramInfo> getLikedPrograms(List<ProgramInfo> allPrograms) {
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

    public static List<ProgramInfo> getNewPrograms(Context context) {
        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
        if (!dbIsInitilized) {
            dbIsInitilized = true;
            readJsonAndInitDb(context);
        }

        List<ProgramInfo> allPrograms = databaseHelper.getAllPrograms();
        List<ProgramInfo> newPrograms = new ArrayList<>();
        for (ProgramInfo program : allPrograms) {
            if (program.getIsNew() == 1) {
                newPrograms.add(program);
            }
        }
        return newPrograms;
    }


    public static List<ProgramInfo> getSearchPrograms(Context context, String query) {
        ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
        if (!dbIsInitilized) {
            dbIsInitilized = true;
            readJsonAndInitDb(context);
        }

        List<ProgramInfo> searchedPrograms = databaseHelper.getSearchResults(query);
        return searchedPrograms;
    }


    private static void readJsonAndInitDb(Context context) {
        String res = "";
        try {
            FileInputStream fis = context.openFileInput(DATA_FILENAME);
            res = IOUtils.toString(fis);
            fis.close();
        } catch (Exception exc) {
            System.out.println("Exception while reading retrieved content data");
        }

        try {
            if (res.isEmpty()) {
                res = IOUtils.toString(context.getResources().openRawResource(R.raw.programs_data));
            }

            List<ProgramInfo> receipes = new ArrayList<>();
            JSONObject jsonData = new JSONObject(res);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            for (int i = 0; i < allPrograms.length(); i++) {
                JSONObject program = allPrograms.getJSONObject(i);
                JSONArray schedule = program.getJSONArray("schedule");
                List<DayInfo> days = new ArrayList<>();
                for (int j = 0; j < schedule.length(); j++) {
                    JSONObject day = schedule.getJSONObject(j);
                    days.add(new DayInfo(day.getString("name"), day.getString("description"),
                            day.getString("photo"), day.getInt("photoOnly")));
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
                receipe.setPhotoURL(program.getString("photoURL"));
                receipe.setShortDescription(program.getString("shortDescription"));
                receipe.setFromSourceUrl(program.getString("sourceUrl"));
                receipe.setFromSourceName(program.getString("sourceName"));
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
}
