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
import java.util.ArrayList;
import java.util.List;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {


    public ReminderDatabaseHelper(Context context) {
        super(context, ReminderQueryHelper.DATABASE_NAME, null, ReminderQueryHelper.DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReminderQueryHelper.TABLE_REMINDERS);//如果存在叫 TABLE_REMINDERS 的表，就把它删掉！
        db.execSQL("DROP TABLE IF EXISTS " + ReminderQueryHelper.TABLE_GROUPS);
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
            groupValues.put(ReminderQueryHelper.KEY_GROUP_ID, groupId);
            groupValues.put(ReminderQueryHelper.KEY_NAME, reminder.getName());
            ArrayList<String> times = getGroupTimes(groupId);
            if (reminder.getTime() != null) {
                times.add(reminder.getTime());
            }
            groupValues.put(ReminderQueryHelper.KEY_TIMES, new Gson().toJson(times));

            db.insertWithOnConflict(ReminderQueryHelper.TABLE_GROUPS, null, groupValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
            ContentValues values = new ContentValues();
            values.put(ReminderQueryHelper.KEY_GROUP_ID, groupId);
            values.put(ReminderQueryHelper.KEY_NAME, reminder.getName());
            values.put(ReminderQueryHelper.KEY_TIME, reminder.getTime());
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

        Cursor cursor = db.query(ReminderQueryHelper.TABLE_GROUPS,
                new String[]{ReminderQueryHelper.KEY_TIMES},
                ReminderQueryHelper.KEY_GROUP_ID + " = ?",
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
    public int updateReminderCompletion(long id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReminderQueryHelper.KEY_IS_COMPLETED, isCompleted ? 1 : 0);

        int rowsAffected = db.update(
                ReminderQueryHelper.TABLE_REMINDERS,
                values,
                ReminderQueryHelper.KEY_ID + " = ?",
                new String[]{String.valueOf(id)}//把 id 转换为字符串作为参数填入 ? 中，防止 SQL 注入。
        );

        db.close();
        return rowsAffected;
    }

    public int updateReminderNotified(long id, boolean isNotified) {
        return ReminderQueryHelper.updateReminderNotified(this, id, isNotified);
    }


    @SuppressLint("Range")
    public List<Reminder> getAllRemindersByDateRange(String date) {
        return ReminderQueryHelper.getAllRemindersByDateRange(this, date);
    }

    public List<Reminder> getRemindersForDate(Context context, String targetDate) {
        return ReminderQueryHelper.getRemindersForDate(this, context, targetDate);
    }




    public int getAllRemindersCountForDate(Context context,String date) {
        return ReminderQueryHelper.getAllRemindersCountForDate(this, context, date);
    }

//    public int getAllRemindersCountForDate(Context context,String date) {
//        return ReminderQueryHelper.getAllRemindersCountForDate(this, context, date);
//    }



    public int getCompletedRemindersCountForDate(String date) {
        return ReminderQueryHelper.getCompletedRemindersCountForDate(this, date);
    }

    public List<String> getAllReminderNames() {
        return ReminderQueryHelper.getAllReminderNames(this);
    }

    public int getRemindersCountForDateAndName(String date, String name) {
        return ReminderQueryHelper.getRemindersCountForDateAndName(this, date, name);
    }

    public int getCompletedRemindersCountForDateAndName(String date, String name) {
        return ReminderQueryHelper.getCompletedRemindersCountForDateAndName(this, date, name);
    }

}