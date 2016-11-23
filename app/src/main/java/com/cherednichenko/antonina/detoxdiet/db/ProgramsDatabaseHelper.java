package com.cherednichenko.antonina.detoxdiet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DayInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.EventInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/23/16.
 */
public class ProgramsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "DDDatabase1";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_PROGRAMS = "programs";
    private static final String TABLE_DAYS = "days";
    private static final String TABLE_SCHEDULE = "schedule";


    // Programs Table Columns
    private static final String KEY_PROGRAM_ID = "id";
    private static final String KEY_PROGRAM_NAME = "programName";
    private static final String KEY_PROGRAM_DESC = "programDescription";
    private static final String KEY_PROGRAM_SHORT_DESC = "short_description";
    private static final String KEY_PROGRAM_LIKED = "liked";
    private static final String KEY_PROGRAM_RECOMMENDED = "recommended";
    private static final String KEY_PROGRAM_NEW = "new";
    private static final String KEY_PROGRAM_PHOTO_URL = "photo";
    private static final String KEY_PROGRAM_DURATION = "duration";
    private static final String KEY_PROGRAM_CATEGORY = "category";
    private static final String KEY_PROGRAM_FROM_SOURCE_NAME = "source_name";
    private static final String KEY_PROGRAM_FROM_SOURCE_URL = "source_url";

    // Days Table Columns
    private static final String KEY_DAY_ID = "id";
    private static final String KEY_DAY_PROGRAM_ID_FK = "programId";
    private static final String KEY_DAY_NAME = "dayName";
    private static final String KEY_DAY_PHOTO = "dayPhoto";
    private static final String KEY_DAY_PHOTO_ONLY = "dayPhotoOnly";
    private static final String KEY_DAY_DESCRIPTION = "dayDescription";

    //Schedule events columns
    // Days Table Columns
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_PROGRAM_ID_FK = "programId";
    private static final String KEY_EVENT_TIME = "time";


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
//        CREATE VIRTUAL TABLE enrondata1 USING fts3(content TEXT);
        String CREATE_PROGRAMS_TABLE = "CREATE TABLE " + TABLE_PROGRAMS +
                "(" +
                KEY_PROGRAM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_PROGRAM_NAME + " TEXT," +
                KEY_PROGRAM_DESC + " TEXT," +
                KEY_PROGRAM_SHORT_DESC + " TEXT," +
                KEY_PROGRAM_LIKED + " INTEGER," +
                KEY_PROGRAM_NEW + " INThEGER," +
                KEY_PROGRAM_RECOMMENDED + " INTEGER," +
                KEY_PROGRAM_PHOTO_URL + " TEXT," +
                KEY_PROGRAM_DURATION + " INTEGER," +
                KEY_PROGRAM_CATEGORY + " TEXT," +
                KEY_PROGRAM_FROM_SOURCE_NAME + " TEXT," +
                KEY_PROGRAM_FROM_SOURCE_URL + " TEXT" +
                ")";

        String CREATE_DAYS_TABLE = "CREATE TABLE " + TABLE_DAYS +
                "(" +
                KEY_DAY_ID + " INTEGER PRIMARY KEY," +
                KEY_DAY_NAME + " TEXT," +
                KEY_DAY_DESCRIPTION + " TEXT," +
                KEY_DAY_PHOTO + " TEXT," +
                KEY_DAY_PHOTO_ONLY + " INTEGER," +
                KEY_DAY_PROGRAM_ID_FK + " INTEGER REFERENCES " + TABLE_PROGRAMS + // Define a foreign key
                ")";

        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE +
                "(" +
                KEY_EVENT_ID + " INTEGER PRIMARY KEY," +
                KEY_EVENT_TIME + " INTEGER," +
                KEY_EVENT_PROGRAM_ID_FK + " INTEGER REFERENCES " + TABLE_PROGRAMS + // Define a foreign key
                ")";
        try {
            db.execSQL(CREATE_PROGRAMS_TABLE);
            db.execSQL(CREATE_DAYS_TABLE);
            db.execSQL(CREATE_SCHEDULE_TABLE);
        } catch (Exception e) {
            Log.d(TAG, "Error creating database");
        }
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
            values.put(KEY_DAY_PHOTO, day.getPhoto());
            values.put(KEY_DAY_PHOTO_ONLY, day.getOnlyPhoto());


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
            values.put(KEY_PROGRAM_NEW, program.getIsNew());
            values.put(KEY_PROGRAM_NEW, program.getRecommended());
            values.put(KEY_PROGRAM_NAME, program.getName());
            values.put(KEY_PROGRAM_PHOTO_URL, program.getPhotoURL());
            values.put(KEY_PROGRAM_SHORT_DESC, program.getShortDescription());
            values.put(KEY_PROGRAM_CATEGORY, program.getCategory());
            values.put(KEY_PROGRAM_FROM_SOURCE_NAME, program.getFromSourceName());
            values.put(KEY_PROGRAM_FROM_SOURCE_URL, program.getFromSourceUrl());

            int rows = db.update(TABLE_PROGRAMS, values, KEY_PROGRAM_NAME + "= ?", new String[]{program.getName()});
            if (rows == 1) {
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_PROGRAM_ID, TABLE_PROGRAMS, KEY_PROGRAM_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(program.getName())});
                try {
                    if (cursor.moveToFirst()) {
                        programId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }

            } else {
                programId = (int) db.insertOrThrow(TABLE_PROGRAMS, null, values);

                for (DayInfo day : program.getDays()) {
                    addDay(day, programId);
                }
                db.setTransactionSuccessful();
            }

        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return programId;
    }

    public void addEvent(ProgramInfo program, long timeInMillis) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        //Get program id for event
        String PROGRAMS_DAYS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = ?",
                        TABLE_PROGRAMS, KEY_PROGRAM_NAME);

        Cursor programCursor = db.rawQuery(PROGRAMS_DAYS_SELECT_QUERY, new String[]{String.valueOf(program.getName())});
        try {
            if (programCursor.moveToFirst()) {
                do {
                    int programId = programCursor.getInt(programCursor.getColumnIndex(KEY_PROGRAM_ID));
                    db.beginTransaction();
                    ContentValues values = new ContentValues();
                    values.put(KEY_EVENT_PROGRAM_ID_FK, programId);
                    values.put(KEY_EVENT_TIME, timeInMillis);

                    // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
                    db.insertOrThrow(TABLE_SCHEDULE, null, values);
                    db.setTransactionSuccessful();

                } while (programCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add event to database");
        } finally {
            db.endTransaction();
            if (programCursor != null && !programCursor.isClosed()) {
                programCursor.close();
            }
        }
    }

    public List<ProgramInfo> getSearchResults(String query) {
        String PROGRAMS_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s LIKE ",
                        TABLE_PROGRAMS, KEY_PROGRAM_NAME);
        PROGRAMS_SELECT_QUERY = PROGRAMS_SELECT_QUERY + "'%" + query + "%'";

        return getProgramsForQuery(PROGRAMS_SELECT_QUERY);
    }

    // Get all posts in the database
    public List<ProgramInfo> getAllPrograms() {
        String PROGRAMS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_PROGRAMS);

        return getProgramsForQuery(PROGRAMS_SELECT_QUERY);
    }

    private List<ProgramInfo> getProgramsForQuery(String query) {
        List<ProgramInfo> programs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProgramInfo newProgram = new ProgramInfo();
                    newProgram.setName(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_NAME)));
                    newProgram.setDescription(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_DESC)));
                    newProgram.setShortDescription(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_SHORT_DESC)));
                    newProgram.setLiked(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_LIKED)));
                    newProgram.setIsNew(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_NEW)));
                    newProgram.setRecommended(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_RECOMMENDED)));
                    newProgram.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_DURATION)));
                    newProgram.setPhotoURL(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_PHOTO_URL)));
                    newProgram.setCategory(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_CATEGORY)));
                    newProgram.setFromSourceName(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_FROM_SOURCE_NAME)));
                    newProgram.setFromSourceUrl(cursor.getString(cursor.getColumnIndex(KEY_PROGRAM_FROM_SOURCE_URL)));
                    List<DayInfo> days = new ArrayList<>();
                    int programId = cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_ID));

                    String PROGRAMS_DAYS_SELECT_QUERY =
                            String.format("SELECT * FROM %s WHERE %s = ?",
                                    TABLE_DAYS, KEY_DAY_PROGRAM_ID_FK);

                    Cursor dayCursor = db.rawQuery(PROGRAMS_DAYS_SELECT_QUERY, new String[]{String.valueOf(programId)});
                    if (dayCursor.moveToFirst()) {
                        do {
                            DayInfo newDay = new DayInfo();
                            newDay.setDescription(dayCursor.getString(dayCursor.getColumnIndex(KEY_DAY_DESCRIPTION)));
                            newDay.setName(dayCursor.getString(dayCursor.getColumnIndex(KEY_DAY_NAME)));
                            newDay.setPhoto(dayCursor.getString(dayCursor.getColumnIndex(KEY_DAY_PHOTO)));
                            newDay.setOnlyPhoto(dayCursor.getInt(dayCursor.getColumnIndex(KEY_DAY_PHOTO_ONLY)));

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

    public List<EventInfo> getAllEvents() {
        List<EventInfo> events = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String EVENTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_SCHEDULE);

        Cursor cursor = db.rawQuery(EVENTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    EventInfo newEvent = new EventInfo();
                    newEvent.setTime(cursor.getLong(cursor.getColumnIndex(KEY_EVENT_TIME)));
                    int programId = cursor.getInt(cursor.getColumnIndex(KEY_PROGRAM_ID));

                    String PROGRAM_SELECT_QUERY =
                            String.format("SELECT * FROM %s WHERE %s = %s",
                                    TABLE_PROGRAMS, KEY_PROGRAM_ID, programId);

                    List<ProgramInfo> programs = this.getProgramsForQuery(PROGRAM_SELECT_QUERY);
                    newEvent.setProgram(programs.get(0));
                    events.add(newEvent);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return events;
    }

    // Update liked status of program
    public int updateLikedStatusOfProgram(ProgramInfo program, int liked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROGRAM_LIKED, liked);

        // Updating profile picture url for user with that userName
        return db.update(TABLE_PROGRAMS, values, KEY_PROGRAM_NAME + " = ?",
                new String[]{String.valueOf(program.getName())});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);

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
