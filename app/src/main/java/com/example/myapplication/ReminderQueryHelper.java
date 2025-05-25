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
    public static final String DATABASE_NAME = "ReminderDB";
    public static final int DATABASE_VERSION = 1;
    // Table and column names constants
    public static final String TABLE_REMINDERS = "reminders";
    public static final String TABLE_GROUPS = "reminder_groups";
    public static final String KEY_ID = "id";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TIME = "time";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DOSAGE = "dosage";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_DISPLAY_START = "display_start_date";
    public static final String KEY_DISPLAY_END = "display_end_date";
    public static final String KEY_TIMES = "times";
    public static final String KEY_IS_COMPLETED = "is_completed";
    public static final String KEY_IS_NOTIFIED = "is_notified";

    /**
     * Retrieves reminders active on a specific date, filtered by their frequency settings.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @param context the context used for frequency matching (localization)
     * @param targetDate the date (formatted as "yyyy-MM-dd") for which reminders should be fetched
     * @return a list of reminders that are scheduled for the given date
     */
    public static List<Reminder> getRemindersForDate(ReminderDatabaseHelper reminderDatabaseHelper, Context context, String targetDate) {
        List<Reminder> filteredReminders = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

//Find all reminders with startDate <= targetDate <= endDate, and further determine whether the targetDate
// should really be reminded based on the repetition frequency of each reminder (such as DAILY, WEEKLY, etc.).
        try {
            Date queryDate = sdf.parse(targetDate);
            List<Reminder> remindersInRange = reminderDatabaseHelper.getAllRemindersByDateRange(targetDate);

            for (Reminder reminder : remindersInRange) {
                Date startDate = sdf.parse(reminder.getStartDate());

                // Compare using fixed codes (DAILY, WEEKLY, MONTHLY, YEARLY)
                if (ReminderFrequencyHelper.isDateMatchFrequency(context, queryDate, startDate, reminder.getFrequency())) {
                    filteredReminders.add(reminder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filteredReminders;
    }

    /**
     * Retrieves all reminders that are active within the date range including the specified date.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @param date the date to check (formatted as "yyyy-MM-dd")
     * @return list of reminders active on the given date range
     */
    @SuppressLint("Range")
    static List<Reminder> getAllRemindersByDateRange(ReminderDatabaseHelper reminderDatabaseHelper, String date) {
        List<Reminder> reminders = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";//startDate <= targetDate <= endDate

        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();//Get a "readable" database instance
        Cursor cursor = db.rawQuery(query, new String[]{date, date});//Execute a raw SQL query
        //Cursor is an object similar to a "result set" that represents all the data we have found in the database.
        // Use it to traverse all rows and get the data of each row.

        if (cursor.moveToFirst()) {
            do {
                //getLong: Get a long value from the cursor
                // cursor.getColumnIndex: gets the index (position) of the column named...
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                reminder.setGroupId(cursor.getLong(cursor.getColumnIndex(KEY_GROUP_ID)));
                reminder.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                reminder.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
                reminder.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                reminder.setDosage(cursor.getString(cursor.getColumnIndex(KEY_DOSAGE)));
                reminder.setFrequency(cursor.getString(cursor.getColumnIndex(KEY_FREQUENCY)));
                reminder.setStartDate(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)));
                reminder.setEndDate(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)));
                reminder.setDisplayStartDate(cursor.getString(cursor.getColumnIndex(KEY_DISPLAY_START)));
                reminder.setDisplayEndDate(cursor.getString(cursor.getColumnIndex(KEY_DISPLAY_END)));
                reminder.setIsCompleted(cursor.getInt(cursor.getColumnIndex(KEY_IS_COMPLETED)) == 1);
                reminder.setIsNotified(cursor.getInt(cursor.getColumnIndex(KEY_IS_NOTIFIED)) == 1);

                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminders;
    }

    /**
     * Counts the number of reminders that are effectively active on the specified date.
     * <p>
     * This method first retrieves all reminders whose date ranges include the target date
     * (i.e., startDate <= targetDate <= endDate). It then filters these reminders by checking
     * if the target date matches their recurrence frequency (e.g., daily, weekly, monthly).
     * </p>
     *
     * @param reminderDatabaseHelper Helper instance to access the reminders database
     * @param context               Android context used for frequency matching logic
     * @param targetDate            Target date as a string in the format "yyyy-MM-dd"
     * @return                      The count of reminders that should trigger on the target date
     */

    public static int getAllRemindersCountForDate(ReminderDatabaseHelper reminderDatabaseHelper, Context context, String targetDate) {
        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date queryDate = sdf.parse(targetDate);

            List<Reminder> remindersInRange = reminderDatabaseHelper.getAllRemindersByDateRange(targetDate);

            for (Reminder reminder : remindersInRange) {
                Date startDate = sdf.parse(reminder.getStartDate());

                if (ReminderFrequencyHelper.isDateMatchFrequency(context, queryDate, startDate, reminder.getFrequency())) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }



    /**
     * Counts all reminders marked as completed on the specified date.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @param date the date to check (formatted as "yyyy-MM-dd")
     * @return number of completed reminders on that date
     */
    public static int getCompletedRemindersCountForDate(ReminderDatabaseHelper reminderDatabaseHelper, String date) {
        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ? AND "
                + KEY_IS_COMPLETED + " = 1";  // 只计算已完成的

        Cursor cursor = db.rawQuery(query, new String[]{date, date});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
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
     * Counts reminders active on a specified date with a specific name.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @param date the date to check (formatted as "yyyy-MM-dd")
     * @param name the name of the reminder to filter by
     * @return number of reminders matching the date and name
     */
    public static int getRemindersCountForDateAndName(ReminderDatabaseHelper reminderDatabaseHelper, String date, String name) {
        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ? AND "
                + KEY_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{date, date, name});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /**
     * Counts completed reminders active on a specified date with a specific name.
     *
     * @param reminderDatabaseHelper helper instance to access the database
     * @param date the date to check (formatted as "yyyy-MM-dd")
     * @param name the name of the reminder to filter by
     * @return number of completed reminders matching the date and name
     */
    public static int getCompletedRemindersCountForDateAndName(ReminderDatabaseHelper reminderDatabaseHelper, String date, String name) {
        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ? AND "
                + KEY_NAME + " = ? AND "
                + KEY_IS_COMPLETED + " = 1";

        Cursor cursor = db.rawQuery(query, new String[]{date, date, name});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
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

        int rowsAffected = db.update(
                TABLE_REMINDERS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
        return rowsAffected;
    }

}
