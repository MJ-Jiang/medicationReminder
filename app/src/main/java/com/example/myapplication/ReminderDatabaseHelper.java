package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ReminderDatabaseHelper is a SQLiteOpenHelper subclass used to manage database creation and version management
 * for the reminder application. It provides methods for inserting, updating, and querying reminders and reminder groups.
 *
 * <p>It relies on {@link ReminderQueryHelper} for most database constants and helper queries.</p>
 */
public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructs a new ReminderDatabaseHelper.
     *
     * @param context the application context
     */
    public ReminderDatabaseHelper(Context context) {
        super(context, ReminderQueryHelper.DATABASE_NAME, null, ReminderQueryHelper.DATABASE_VERSION);
    }
//Passing null means using the default cursor factory.
// This constructor is used to initialize the database helper class, which inherits from SQLiteOpenHelper, and specifies the database name and version.
    /**
     * Called when the database is created for the first time.
     * This method creates two tables: reminders and reminder_groups.
     *
     * @param db the database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + ReminderQueryHelper.TABLE_REMINDERS + "("
                + ReminderQueryHelper.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ReminderQueryHelper.KEY_GROUP_ID + " INTEGER,"
                + ReminderQueryHelper.KEY_NAME + " TEXT,"
                + ReminderQueryHelper.KEY_TIME + " TEXT,"
                + ReminderQueryHelper.KEY_DESCRIPTION + " TEXT,"
                + ReminderQueryHelper.KEY_DOSAGE + " TEXT,"
                + ReminderQueryHelper.KEY_FREQUENCY + " TEXT,"
                + ReminderQueryHelper.KEY_START_DATE + " TEXT,"
                + ReminderQueryHelper.KEY_END_DATE + " TEXT,"
                + ReminderQueryHelper.KEY_DATE+" TEXT,"
                + ReminderQueryHelper.KEY_DISPLAY_START + " TEXT,"
                + ReminderQueryHelper.KEY_DISPLAY_END + " TEXT,"
                + ReminderQueryHelper.KEY_IS_COMPLETED + " INTEGER DEFAULT 0,"
                + ReminderQueryHelper.KEY_IS_NOTIFIED + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
        String CREATE_GROUPS_TABLE = "CREATE TABLE " + ReminderQueryHelper.TABLE_GROUPS + "("
                + ReminderQueryHelper.KEY_GROUP_ID + " INTEGER PRIMARY KEY,"
                + ReminderQueryHelper.KEY_NAME + " TEXT,"
                + ReminderQueryHelper.KEY_TIMES + " TEXT)"; // Stores JSON array
        db.execSQL(CREATE_GROUPS_TABLE);



    }

    /**
     * Called when the database needs to be upgraded.
     * It drops the existing tables and creates new ones.
     *
     * @param db the database
     * @param oldVersion the old database version
     * @param newVersion the new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReminderQueryHelper.TABLE_REMINDERS);//If there is a table called TABLE_REMINDERS, delete it!
        db.execSQL("DROP TABLE IF EXISTS " + ReminderQueryHelper.TABLE_GROUPS);
        onCreate(db);
    }

    /**
     * Adds a new reminder to the database. Also updates the group entry with the new time.
     *
     * @param reminder the reminder object to insert
     * @return the ID of the newly inserted reminder, or -1 if an error occurred
     */
    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        long groupId = reminder.getGroupId();
        long newId = -1;

        try {
            db.beginTransaction();

            ContentValues groupValues = new ContentValues();
            groupValues.put(ReminderQueryHelper.KEY_GROUP_ID, groupId);
            groupValues.put(ReminderQueryHelper.KEY_NAME, reminder.getName());

            ArrayList<String> times = getGroupTimes(groupId);
            if (reminder.getTime() != null && !times.contains(reminder.getTime())) {
                times.add(reminder.getTime());
            }
            groupValues.put(ReminderQueryHelper.KEY_TIMES, new Gson().toJson(times));

            db.insertWithOnConflict(ReminderQueryHelper.TABLE_GROUPS, null, groupValues,
                    SQLiteDatabase.CONFLICT_REPLACE);


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = dateFormat.parse(reminder.getStartDate());
            Date endDate = dateFormat.parse(reminder.getEndDate());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            String timeToProcess = reminder.getTime();

            while (!calendar.getTime().after(endDate)) {
                String dateStr = dateFormat.format(calendar.getTime());

                ContentValues values = new ContentValues();
                values.put(ReminderQueryHelper.KEY_GROUP_ID, groupId);
                values.put(ReminderQueryHelper.KEY_NAME, reminder.getName());
                values.put(ReminderQueryHelper.KEY_DATE, dateStr);
                values.put(ReminderQueryHelper.KEY_TIME, timeToProcess);
                values.put(ReminderQueryHelper.KEY_DESCRIPTION, reminder.getDescription());
                values.put(ReminderQueryHelper.KEY_DOSAGE, reminder.getDosage());
                values.put(ReminderQueryHelper.KEY_FREQUENCY, reminder.getFrequency());
                values.put(ReminderQueryHelper.KEY_START_DATE, reminder.getStartDate());
                values.put(ReminderQueryHelper.KEY_END_DATE, reminder.getEndDate());
                values.put(ReminderQueryHelper.KEY_DISPLAY_START, reminder.getDisplayStartDate());
                values.put(ReminderQueryHelper.KEY_DISPLAY_END, reminder.getDisplayEndDate());
                values.put(ReminderQueryHelper.KEY_IS_COMPLETED, reminder.getIsCompleted() ? 1 : 0);
                values.put(ReminderQueryHelper.KEY_IS_NOTIFIED, reminder.getIsNotified() ? 1 : 0);

                newId = db.insert(ReminderQueryHelper.TABLE_REMINDERS, null, values);

                // Increment date based on frequency
                switch (reminder.getFrequency()) {
                    case "DAILY":
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        break;
                    case "WEEKLY":
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case "MONTHLY":
                        calendar.add(Calendar.MONTH, 1);
                        break;
                    case "YEARLY":
                        calendar.add(Calendar.YEAR, 1);
                        break;
                    default:
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        break;
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }

        return newId;
    }
    /**
     * Retrieves the list of reminder times (as strings) associated with a group ID.
     *
     * @param groupId the group ID to query
     * @return a list of times in string format
     */
    ArrayList<String> getGroupTimes(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> times = new ArrayList<>();

        Cursor cursor = db.query(ReminderQueryHelper.TABLE_GROUPS,
                new String[]{ReminderQueryHelper.KEY_TIMES},
                ReminderQueryHelper.KEY_GROUP_ID + " = ?",//filter rows where group ID equals the given parameter.
                new String[]{String.valueOf(groupId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            String json = cursor.getString(0);//Gets the first column’s value (index 0) from the cursor row as a String.
            if (json != null) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();// Gson needs type info for deserializing generic types correctly.
                times = new Gson().fromJson(json, type);
            }
        }
        cursor.close();
        return times;
    }

    /**
     * Updates the completion status of a reminder by ID.
     *
     * @param id the ID of the reminder
     * @param isCompleted true if the reminder is completed, false otherwise
     * @return the number of rows affected
     */
    public int updateReminderCompletion(long id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderQueryHelper.KEY_IS_COMPLETED, isCompleted ? 1 : 0);

        int rowsAffected = db.update(
                ReminderQueryHelper.TABLE_REMINDERS,
                values,
                ReminderQueryHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return rowsAffected;
    }
    /**
     * Updates the notification status of a reminder by ID.
     *
     * @param id the reminder ID
     * @param isNotified true if notified, false otherwise
     * @return the number of rows affected
     */
    public int updateReminderNotified(long id, boolean isNotified) {
        return ReminderQueryHelper.updateReminderNotified(this, id, isNotified);
    }


    /**
     * Retrieves all reminders if the target date is between their start and end dates, then filtered by frequency.
     * @param targetDate the date to retrieve reminders for
     * @return a list of matching reminders
     */
    public List<Reminder> getRemindersForDate(String targetDate) {
        return ReminderQueryHelper.getRemindersForDate(this, targetDate);
    }

    /**
     * Returns the total number of reminders for a specific date.
     *
     * @param date the date to check
     * @return the count of reminders
     */
    public int getAllRemindersCountForDate(String date) {
        return ReminderQueryHelper.getAllRemindersCountForDate(this, date);
    }

    /**
     * Returns the number of completed reminders for a specific date.
     *
     * @param date the date to check
     * @return the count of completed reminders
     */

    public int getCompletedRemindersCountForDate(String date) {
        return ReminderQueryHelper.getCompletedRemindersCountForDate(this, date);
    }
    /**
     * Retrieves a list of all unique reminder names.
     *
     * @return a list of reminder names
     */
    public List<String> getAllReminderNames() {
        return ReminderQueryHelper.getAllReminderNames(this);
    }
    /**
     * Gets the count of reminders for a specific date and name.
     *
     * @param date the target date
     * @param name the reminder name
     * @return the number of matching reminders
     */
    public int getRemindersCountForDateAndName(Context context,String date, String name) {
        return ReminderQueryHelper.getRemindersCountForDateAndName(this, date, name);
    }
    /**
     * Gets the count of completed reminders for a specific date and name.
     *
     * @param date the target date
     * @param name the reminder name
     * @return the number of matching completed reminders
     */
    public int getCompletedRemindersCountForDateAndName(Context context, String date, String name) {
        return ReminderQueryHelper.getCompletedRemindersCountForDateAndName(this, date, name);
    }

}