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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);//如果存在叫 TABLE_REMINDERS 的表，就把它删掉！
        onCreate(db);
    }//删掉旧表，创建新表

    // 添加新提醒
    public long addReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();//ContentValues ➔ 相当于一个小型字典（key-value表），用来装要存进数据库的数据。

        values.put(KEY_NAME, reminder.getName());
        values.put(KEY_DESCRIPTION, reminder.getDescription());
        values.put(KEY_DOSAGE, reminder.getDosage());
        values.put(KEY_FREQUENCY, reminder.getFrequency());
        values.put(KEY_START_DATE, reminder.getStartDate());
        values.put(KEY_END_DATE, reminder.getEndDate());
        values.put(KEY_DISPLAY_START, reminder.getDisplayStartDate());
        values.put(KEY_DISPLAY_END, reminder.getDisplayEndDate());

        // 将时间列表转为JSON存储
        Gson gson = new Gson();//Gson 是 Google 提供的一个超好用的库，用来把对象变成 JSON 字符串，或者反过来解析。
        values.put(KEY_TIMES, gson.toJson(reminder.getTimes()));//把提醒里的时间列表 times（是一个 ArrayList）变成 JSON 格式存起来

        long id = db.insert(TABLE_REMINDERS, null, values);//执行表的插入操作，插入所有value数据
        db.close();//用完数据库要关闭
        return id;//把新插入的id返回
    }

    // 按日期查询提醒（供主页使用）
    public List<Reminder> getRemindersByDate(String date) {
        List<Reminder> reminders = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_REMINDERS + " WHERE "
                + KEY_START_DATE + " <= ? AND " + KEY_END_DATE + " >= ?";

        SQLiteDatabase db = this.getReadableDatabase();//打开一个只能读的数据库连接。
        Cursor cursor = db.rawQuery(query, new String[]{date, date});//执行一个 SQL 查询，得到查询结果，并用 Cursor（光标）对象来接收。
        //Cursor是一个类，指向数据库查询返回的结果，可以一行一行地移动。可以通过 Cursor 来读出每一行的具体内容，比如提醒的名字、时间等。
        //rawQuery(query, new String[]{date, date})	执行原生SQL，把两个 ? 替换成给定的 date

        if (cursor.moveToFirst()) {//移动到第一条数据，如果有的话返回 true
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