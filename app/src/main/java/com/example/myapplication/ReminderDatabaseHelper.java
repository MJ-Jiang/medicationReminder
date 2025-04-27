package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 2;  // 升级版本号，确保新的表创建

    // Reminder 表字段
    public static final String TABLE_NAME = "reminders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DOSAGE = "dosage";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_FREQUENCY = "frequency";

    // Times 表字段
    public static final String TABLE_TIMES = "times";
    public static final String COLUMN_REMINDER_ID = "reminder_id";  // 外键，关联到 reminders 表
    public static final String COLUMN_TIME = "time";

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 reminders 表
        String createRemindersTable = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DOSAGE + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT, " +
                COLUMN_FREQUENCY + " TEXT)";
        db.execSQL(createRemindersTable);

        // 创建 times 表
        String createTimesTable = "CREATE TABLE " + TABLE_TIMES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REMINDER_ID + " INTEGER, " +  // 外键，关联到 reminders 表
                COLUMN_TIME + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_REMINDER_ID + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + "))";
        db.execSQL(createTimesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果数据库版本升级，删除旧表并重新创建新表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a new reminder and its times
    public void addReminder(String name, String description, String dosage, String startDate, String endDate, String frequency, ArrayList<String> times) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Start transaction for atomicity
        db.beginTransaction();
        try {
            // 插入提醒的基本数据（不包含时间）
            ContentValues reminderValues = new ContentValues();
            reminderValues.put(COLUMN_NAME, name);
            reminderValues.put(COLUMN_DESCRIPTION, description);
            reminderValues.put(COLUMN_DOSAGE, dosage);
            reminderValues.put(COLUMN_START_DATE, startDate);
            reminderValues.put(COLUMN_END_DATE, endDate);
            reminderValues.put(COLUMN_FREQUENCY, frequency);

            // 插入新的提醒记录
            long reminderId = db.insert(TABLE_NAME, null, reminderValues);

            // 插入每个时间到 times 表
            if (reminderId != -1) {  // 如果提醒插入成功
                for (String time : times) {
                    ContentValues timeValues = new ContentValues();
                    timeValues.put(COLUMN_REMINDER_ID, reminderId);  // 外键，关联到提醒
                    timeValues.put(COLUMN_TIME, time);
                    db.insert(TABLE_TIMES, null, timeValues);  // 插入每个时间
                }

                // 提交事务
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
        }

        db.close();
    }
}
