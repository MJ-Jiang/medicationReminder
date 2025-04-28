package com.example.myapplication;

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
    private static final String DATABASE_NAME = "ReminderDB";
    private static final int DATABASE_VERSION = 1;

    // 表结构
    private static final String TABLE_REMINDERS = "reminders";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DOSAGE = "dosage";
    private static final String KEY_FREQUENCY = "frequency";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_DISPLAY_START = "display_start_date";
    private static final String KEY_DISPLAY_END = "display_end_date";
    private static final String KEY_TIMES = "times";

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DOSAGE + " TEXT,"
                + KEY_FREQUENCY + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_END_DATE + " TEXT,"
                + KEY_DISPLAY_START + " TEXT,"
                + KEY_DISPLAY_END + " TEXT,"
                + KEY_TIMES + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // 添加新提醒
    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, reminder.getName());
        values.put(KEY_DESCRIPTION, reminder.getDescription());
        values.put(KEY_DOSAGE, reminder.getDosage());
        values.put(KEY_FREQUENCY, reminder.getFrequency());
        values.put(KEY_START_DATE, reminder.getStartDate());
        values.put(KEY_END_DATE, reminder.getEndDate());
        values.put(KEY_DISPLAY_START, reminder.getDisplayStartDate());
        values.put(KEY_DISPLAY_END, reminder.getDisplayEndDate());

        // 将时间列表转为JSON存储
        Gson gson = new Gson();
        values.put(KEY_TIMES, gson.toJson(reminder.getTimes()));

        long id = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return id;
    }

    // 按日期查询提醒（供主页使用）
    public List<Reminder> getRemindersByDate(String date) {
        List<Reminder> reminders = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{date, date});

        if (cursor.moveToFirst()) {
            do {
                Reminder reminder = new Reminder();
                reminder.setId(cursor.getLong(0));
                reminder.setName(cursor.getString(1));
                reminder.setDescription(cursor.getString(2));
                reminder.setDosage(cursor.getString(3));
                reminder.setFrequency(cursor.getString(4));
                reminder.setStartDate(cursor.getString(5));
                reminder.setEndDate(cursor.getString(6));
                reminder.setDisplayStartDate(cursor.getString(7));
                reminder.setDisplayEndDate(cursor.getString(8));

                // 解析JSON时间列表
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                reminder.setTimes(new Gson().fromJson(cursor.getString(9), type));

                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminders;
    }
}