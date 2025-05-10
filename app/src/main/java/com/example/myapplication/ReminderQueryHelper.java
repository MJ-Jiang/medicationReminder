package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReminderQueryHelper {
    public static final String DATABASE_NAME = "ReminderDB";
    public static final int DATABASE_VERSION = 1;
    // 表结构
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

    public static List<Reminder> getRemindersForDate(ReminderDatabaseHelper reminderDatabaseHelper, Context context, String targetDate) {
        List<Reminder> filteredReminders = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

    @SuppressLint("Range")
    static List<Reminder> getAllRemindersByDateRange(ReminderDatabaseHelper reminderDatabaseHelper, String date) {
        List<Reminder> reminders = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";

        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{date, date});

        if (cursor.moveToFirst()) {
            do {
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

    public static int getAllRemindersCountForDate(ReminderDatabaseHelper reminderDatabaseHelper, String date) {
        SQLiteDatabase db = reminderDatabaseHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";

        Cursor cursor = db.rawQuery(query, new String[]{date, date});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

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
    // ReminderQueryHelper.java
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
