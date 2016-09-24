package com.cherednichenko.antonina.detoxdiet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DayInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/23/16.
 */
public class ProgramsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PROGRAMS = "programs";
    private static final String TABLE_DAYS = "days";

    // Programs Table Columns
    private static final String KEY_PROGRAM_ID = "id";
    private static final String KEY_PROGRAM_NAME = "programName";
    private static final String KEY_PROGRAM_DESC = "programDescription";
    private static final String KEY_PROGRAM_SHORT_DESC = "short_description";
    private static final String KEY_PROGRAM_LIKED = "liked";
    private static final String KEY_PROGRAM_PHOTO_URL = "photo";
    private static final String KEY_PROGRAM_DURATION = "duration";

    // Days Table Columns
    private static final String KEY_DAY_ID = "id";
    private static final String KEY_DAY_PROGRAM_ID_FK = "programId";
    private static final String KEY_DAY_NAME = "dayName";
    private static final String KEY_DAY_DESCRIPTION = "dayDescription";

    private static ProgramsDatabaseHelper sInstance;

    private static final String TAG = "ProgramsDatabaseHelper";

    public static synchronized ProgramsDatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new ProgramsDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ProgramsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROGRAMS_TABLE = "CREATE TABLE " + TABLE_PROGRAMS +
                "(" +
                KEY_PROGRAM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_PROGRAM_NAME + " TEXT," +
                KEY_PROGRAM_DESC + " TEXT," +
                KEY_PROGRAM_SHORT_DESC + " TEXT," +
                KEY_PROGRAM_LIKED + " INTEGER," +
                KEY_PROGRAM_PHOTO_URL + " INTEGER," +
                KEY_PROGRAM_DURATION + " INTEGER" +
                ")";

        String CREATE_DAYS_TABLE = "CREATE TABLE " + TABLE_DAYS +
                "(" +
                KEY_DAY_ID + " INTEGER PRIMARY KEY," +
                KEY_DAY_NAME + " TEXT," +
                KEY_DAY_DESCRIPTION + " TEXT," +
                KEY_DAY_PROGRAM_ID_FK + " INTEGER REFERENCES " + TABLE_PROGRAMS + // Define a foreign key
                ")";

        db.execSQL(CREATE_PROGRAMS_TABLE);
        db.execSQL(CREATE_DAYS_TABLE);
    }


    // Insert a post into the database
    public void addDay(DayInfo day, long programId) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DAY_PROGRAM_ID_FK, programId);
            values.put(KEY_DAY_NAME, day.getName());
            values.put(KEY_DAY_DESCRIPTION, day.getDescription());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_DAYS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public long addProgram(ProgramInfo program) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        int programId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROGRAM_DESC, program.getDescription());
            values.put(KEY_PROGRAM_DURATION, program.getDuration());
            values.put(KEY_PROGRAM_LIKED, program.getLiked());
            values.put(KEY_PROGRAM_NAME, program.getName());
            values.put(KEY_PROGRAM_PHOTO_URL, program.getPhotoId());
            values.put(KEY_PROGRAM_SHORT_DESC, program.getShortDescription());

            programId = (int) db.insertOrThrow(TABLE_PROGRAMS, null, values);

            for (DayInfo day : program.getDays()) {
                addDay(day, programId);
            }
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return programId;
    }

    // Get all posts in the database
    public List<ProgramInfo> getAllPrograms() {
        List<ProgramInfo> programs = new ArrayList<>();

        String PROGRAMS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_PROGRAMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PROGRAMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProgramInfo newProgram = new ProgramInfo();
                    newProgram.setName(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_NAME)));
                    newProgram.setDescription(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_DESC)));
                    newProgram.setShortDescription(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_SHORT_DESC)));
                    newProgram.setLiked(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_LIKED)));
                    newProgram.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_DURATION)));
                    newProgram.setPhotoId(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_PHOTO_URL)));
                    List<DayInfo> days = new ArrayList<>();
                    int programId = cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_ID));

                    String PROGRAMS_DAYS_SELECT_QUERY =
                            String.format("SELECT * FROM %s WHERE %s = ?",
                                    TABLE_DAYS, KEY_PROGRAM_ID);

                    Cursor dayCursor = db.rawQuery(PROGRAMS_DAYS_SELECT_QUERY, new String[]{String.valueOf(programId)});
                    if (dayCursor.moveToFirst()) {
                        do {
                            DayInfo newDay = new DayInfo();
                            newDay.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DAY_DESCRIPTION)));
                            newDay.setName(cursor.getString(cursor.getColumnIndex(KEY_DAY_NAME)));
                            days.add(newDay);

                        } while (dayCursor.moveToNext());
                    }
                    newProgram.setDays(days);
                    programs.add(newProgram);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return programs;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
            onCreate(db);
        }

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

}
