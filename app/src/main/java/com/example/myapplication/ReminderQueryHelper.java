package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper class to perform various database queries related to reminders.
 * <p>
 * This class provides methods to retrieve reminders filtered by date,
 * count reminders, get reminder names, and update reminder notification status.
 * It abstracts raw SQL queries and database operations for easier use.
 * </p>
 */
public class ReminderQueryHelper {
    public static final String DATABASE_NAME= "ReminderDB";
    public static final int DATABASE_VERSION = 1;
    // Table and column names constants
    public static final String TABLE_REMINDERS = "reminders";
    public static final String TABLE_GROUPS = "reminder_groups";
    public static final String KEY_ID = "id";
    public static final String KEY_GROUP_ID ="group_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TIME = "time";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DOSAGE ="dosage";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_DATE="date";
    public static final String KEY_DISPLAY_START = "display_start_date";
    public static final String KEY_DISPLAY_END = "display_end_date";
    public static final String KEY_TIMES= "times";
    public static final String KEY_IS_COMPLETED ="is_completed";
    public static final String KEY_IS_NOTIFIED = "is_notified";


    /**
     * Retrieves all reminder records from the database that match the given date.
     *
     *
     * @param reminderDatabaseHelper The database helper used to access the reminders database.
     * @param targetDate The target date in the format "yyyy-MM-dd" to retrieve reminders for.
     * @return A list of Reminder objects scheduled for the specified date.
     */

    @SuppressLint("Range")
    public static List<Reminder> getRemindersForDate(ReminderDatabaseHelper reminderDatabaseHelper, String targetDate) {
        List<Reminder> reminders = new ArrayList<>();

        String query = "SELECT * FROM " + ReminderQueryHelper.TABLE_REMINDERS + " WHERE " +
                ReminderQueryHelper.KEY_DATE + " = ?";

        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{targetDate});

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getLong(cursor.getColumnIndex(ReminderQueryHelper.KEY_ID)));
                reminder.setGroupId(cursor.getLong(cursor.getColumnIndex(ReminderQueryHelper.KEY_GROUP_ID)));
                reminder.setName(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_NAME)));
                reminder.setDate(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_DATE)));
                reminder.setTime(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_TIME)));
                reminder.setDescription(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_DESCRIPTION)));
                reminder.setDosage(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_DOSAGE)));
                reminder.setFrequency(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_FREQUENCY)));
                reminder.setStartDate(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_START_DATE)));
                reminder.setEndDate(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_END_DATE)));
                reminder.setDisplayStartDate(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_DISPLAY_START)));
                reminder.setDisplayEndDate(cursor.getString(cursor.getColumnIndex(ReminderQueryHelper.KEY_DISPLAY_END)));
                reminder.setIsCompleted(cursor.getInt(cursor.getColumnIndex(ReminderQueryHelper.KEY_IS_COMPLETED)) == 1);
                reminder.setIsNotified(cursor.getInt(cursor.getColumnIndex(ReminderQueryHelper.KEY_IS_NOTIFIED)) == 1);

                reminders.add(reminder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return reminders;
    }




    /**
     * Returns the count of reminders scheduled for the specified date.
     *
     * @param reminderDatabaseHelper The database helper to query reminders.
     * @param targetDate The target date in "yyyy-MM-dd" format to check reminders for.
     * @return The number of reminders scheduled on the target date.
     */
    public static int getAllRemindersCountForDate(ReminderDatabaseHelper reminderDatabaseHelper, String targetDate) {
        List<Reminder> reminders = getRemindersForDate(reminderDatabaseHelper, targetDate);
        return reminders.size();
    }


    /**
     * Counts the number of reminders marked as completed on the specified date.

     * @param reminderDatabaseHelper the helper instance to access the reminders database
     * @param targetDate the date (in "yyyy-MM-dd" format) for which to count completed reminders
     * @return the count of completed reminders on the specified date
     */
    public static int getCompletedRemindersCountForDate(ReminderDatabaseHelper reminderDatabaseHelper, String targetDate) {
        List<Reminder> reminders = getRemindersForDate(reminderDatabaseHelper, targetDate);
        int count = 0;
        for (Reminder reminder : reminders) {
            if (reminder.getIsCompleted()) {
                count++;
            }
        }
        return count;
    }




    /**
     * Retrieves a list of all unique reminder names stored in the database.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @return list of distinct reminder names
     */
    public static List<String> getAllReminderNames(ReminderDatabaseHelper reminderDatabaseHelper) {
        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        List<String> names = new ArrayList<>();

        Cursor cursor = db.query(true, TABLE_REMINDERS,
                new String[]{KEY_NAME},
                null, null,
                KEY_NAME, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return names;
    }

    /**
     * Retrieves a list of reminders that occur on the specified date and match the given name.
     *
     * @param dbHelper    the ReminderDatabaseHelper instance for database access
     * @param targetDate  the date to filter reminders on, formatted as "yyyy-MM-dd"
     * @param targetName  the exact name of the reminder to filter by
     * @return a list of reminders active on the target date and matching the specified name
     */

    public static List<Reminder> getRemindersForDateAndName(ReminderDatabaseHelper dbHelper, String targetDate, String targetName) {
        List<Reminder> remindersForDate = getRemindersForDate(dbHelper,  targetDate);
        List<Reminder> filteredReminders = new ArrayList<>();

        for (Reminder r : remindersForDate) {
            if (r.getName().equals(targetName)) {
                filteredReminders.add(r);
            }
        }
        return filteredReminders;

    }

    /**
     * Returns the count of reminders active on the given date and matching the specified name.
     * @param dbHelper the ReminderDatabaseHelper instance for database access

     * @param date the date string ("yyyy-MM-dd") to filter reminders by
     * @param name the exact reminder name to filter by
     * @return the number of reminders matching the given date and name criteria
     */
    public static int getRemindersCountForDateAndName(ReminderDatabaseHelper dbHelper,  String date, String name) {
        List<Reminder> reminders = getRemindersForDateAndName(dbHelper, date, name);
        return reminders.size();
    }
    /**
     * Returns the count of completed reminders active on the given date and matching
     * the specified name. Completion status is determined by the isCompleted flag.
     *
     * @param dbHelper the ReminderDatabaseHelper instance for database access
     * @param date the date string ("yyyy-MM-dd") to filter reminders by
     * @param name the exact reminder name to filter by
     * @return the number of completed reminders matching the given date and name criteria
     */
    public static int getCompletedRemindersCountForDateAndName(ReminderDatabaseHelper dbHelper,  String date, String name) {
        List<Reminder> reminders = getRemindersForDateAndName(dbHelper,  date, name);
        int count = 0;
        for (Reminder r : reminders) {
            if (r.getIsCompleted()) {
                count++;
            }
        }
        return count;
    }



    /**
     * Updates the notification status of a reminder identified by its ID.
     *
     * @param dbHelper the database helper instance
     * @param id the unique identifier of the reminder to update
     * @param isNotified true to mark the reminder as notified, false otherwise
     * @return the number of rows affected by the update (should be 1 if successful)
     */
    public static int updateReminderNotified(ReminderDatabaseHelper dbHelper, long id, boolean isNotified) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_NOTIFIED, isNotified ? 1 : 0);

        int rowsAffected = db.update(TABLE_REMINDERS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
       //Updates the records in the TABLE_REMINDERS table where KEY_ID is equal to id. The updated content is the fields specified in values. Returns the number of rows updated.

        db.close();
        return rowsAffected;
    }

}
