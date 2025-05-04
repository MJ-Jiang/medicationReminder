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

public class ReminderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ReminderDB";
    private static final int DATABASE_VERSION = 1;

    // 表结构
    private static final String TABLE_REMINDERS = "reminders";
    private static final String TABLE_GROUPS = "reminder_groups";


    private static final String KEY_ID = "id";
    private static final String KEY_GROUP_ID = "group_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TIME = "time";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DOSAGE = "dosage";
    private static final String KEY_FREQUENCY = "frequency";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_DISPLAY_START = "display_start_date";
    private static final String KEY_DISPLAY_END = "display_end_date";
    private static final String KEY_TIMES = "times";
    private static final String KEY_IS_COMPLETED = "is_completed";
    private static final String KEY_IS_NOTIFIED = "is_notified";

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_GROUP_ID + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DOSAGE + " TEXT,"
                + KEY_FREQUENCY + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_END_DATE + " TEXT,"
                + KEY_DISPLAY_START + " TEXT,"
                + KEY_DISPLAY_END + " TEXT,"
                + KEY_IS_COMPLETED + " INTEGER DEFAULT 0,"
                + KEY_IS_NOTIFIED + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
        String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_GROUPS + "("
                + KEY_GROUP_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_TIMES + " TEXT)"; // Stores JSON array
        db.execSQL(CREATE_GROUPS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);//如果存在叫 TABLE_REMINDERS 的表，就把它删掉！
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        onCreate(db);
    }//删掉旧表，创建新表

    // 添加新提醒
    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        long groupId = reminder.getGroupId();
        long newId = -1;
        try{
            db.beginTransaction();
            ContentValues groupValues = new ContentValues();
            groupValues.put(KEY_GROUP_ID, groupId);
            groupValues.put(KEY_NAME, reminder.getName());
            ArrayList<String> times = getGroupTimes(groupId);
            if (reminder.getTime() != null) {
                times.add(reminder.getTime());
            }
            groupValues.put(KEY_TIMES, new Gson().toJson(times));

            db.insertWithOnConflict(TABLE_GROUPS, null, groupValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
            ContentValues values = new ContentValues();
            values.put(KEY_GROUP_ID, groupId);
            values.put(KEY_NAME, reminder.getName());
            values.put(KEY_TIME, reminder.getTime());
            values.put(KEY_DESCRIPTION, reminder.getDescription());
            values.put(KEY_DOSAGE, reminder.getDosage());
            values.put(KEY_FREQUENCY, reminder.getFrequency());
            values.put(KEY_START_DATE, reminder.getStartDate());
            values.put(KEY_END_DATE, reminder.getEndDate());
            values.put(KEY_DISPLAY_START, reminder.getDisplayStartDate());
            values.put(KEY_DISPLAY_END, reminder.getDisplayEndDate());
            values.put(KEY_IS_COMPLETED, reminder.getIsCompleted() ? 1 : 0);
            values.put(KEY_IS_NOTIFIED, reminder.getIsNotified() ? 1 : 0);

            newId = db.insert(TABLE_REMINDERS, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return newId;
    }
    ArrayList<String> getGroupTimes(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> times = new ArrayList<>();

        Cursor cursor = db.query(TABLE_GROUPS,
                new String[]{KEY_TIMES},
                KEY_GROUP_ID + " = ?",
                new String[]{String.valueOf(groupId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            String json = cursor.getString(0);
            if (json != null) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                times = new Gson().fromJson(json, type);
            }
        }
        cursor.close();
        return times;
    }

@SuppressLint("Range")
private List<Reminder> getAllRemindersByDateRange(String date) {
    List<Reminder> reminders = new ArrayList<>();
    String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE "
            + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";

    SQLiteDatabase db = this.getReadableDatabase();
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

    public List<Reminder> getRemindersForDate(Context context, String targetDate) {
        List<Reminder> filteredReminders = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date queryDate = sdf.parse(targetDate);
            List<Reminder> remindersInRange = getAllRemindersByDateRange(targetDate);

            for (Reminder reminder : remindersInRange) {
                Date startDate = sdf.parse(reminder.getStartDate());

                // Compare using fixed codes (DAILY, WEEKLY, MONTHLY, YEARLY)
                if (isDateMatchFrequency(context, queryDate, startDate, reminder.getFrequency())) {
                    filteredReminders.add(reminder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filteredReminders;
    }

    private boolean isDateMatchFrequency(Context context, Date queryDate, Date startDate, String frequency) {
        long diffDays = (queryDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

        // Fixed frequency codes, do not translate here
        String daily = "DAILY";
        String weekly = "WEEKLY";
        String monthly = "MONTHLY";
        String yearly = "YEARLY";

        if (frequency.equalsIgnoreCase(daily)) {
            return true;
        } else if (frequency.equalsIgnoreCase(weekly)) {
            return diffDays % 7 == 0;
        } else if (frequency.equalsIgnoreCase(monthly)) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(startDate);
            cal2.setTime(queryDate);
            return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        } else if (frequency.equalsIgnoreCase(yearly)) {
            Calendar cal3 = Calendar.getInstance();
            Calendar cal4 = Calendar.getInstance();
            cal3.setTime(startDate);
            cal4.setTime(queryDate);
            return cal3.get(Calendar.DAY_OF_YEAR) == cal4.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }

    public String getLocalizedFrequency(Context context, String frequencyKey) {
        // Translate for UI display only
        switch (frequencyKey) {
            case "DAILY":
                return context.getString(R.string.daily);
            case "WEEKLY":
                return context.getString(R.string.weekly);
            case "MONTHLY":
                return context.getString(R.string.monthly);
            case "YEARLY":
                return context.getString(R.string.yearly);
            default:
                return "";
        }
    }


    public int updateReminderCompletion(long id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_COMPLETED, isCompleted ? 1 : 0);

        int rowsAffected = db.update(
                TABLE_REMINDERS,
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)}//把 id 转换为字符串作为参数填入 ? 中，防止 SQL 注入。
        );

        db.close();
        return rowsAffected;
    }

    public int getAllRemindersCountForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
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
    public int getCompletedRemindersCountForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
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

}