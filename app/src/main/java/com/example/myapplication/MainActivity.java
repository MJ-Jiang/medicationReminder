package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements ReminderAdapter.OnReminderClickListener {
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ReminderDatabaseHelper dbHelper;
    private TextView textViewSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        dbHelper = new ReminderDatabaseHelper(this);

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.recyclerViewReminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        Button buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        ImageButton buttonSelectDate = findViewById(R.id.buttonSelectDate);
        FloatingActionButton fabSettings = findViewById(R.id.fabSettings);


        textViewSelectedDate.setText(Utils.getTodayDate());
        updateReminderList(Utils.getTodayDate());
        buttonCreateReminder.setOnClickListener(v -> startActivity(new Intent(this, CreateReminderActivity.class)));
        buttonSelectDate.setOnClickListener(v ->
                DatePickerHelper.show(this, textViewSelectedDate.getText().toString(), textViewSelectedDate, adapter, dbHelper));
        fabSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }
    private void updateReminderList(String date){
        List<Reminder> reminders=ReminderLoader.loadRemindersForDate(this,dbHelper,date);
        adapter.updateData(reminders);

    }


    @Override
    public void onReminderClick(Reminder reminder) {
        ReminderDetailDialog.show(this, reminder);
    }

    @Override
    public void onCheckedChanged(Reminder reminder, boolean isChecked) {
        // 更新数据库中的完成状态
        dbHelper.updateReminderCompletion(reminder.getId(), isChecked);
    }



    @Override
    protected void onResume() {
        super.onResume();
        updateReminderList(textViewSelectedDate.getText().toString());
    }

    @Override
    protected void attachBaseContext(Context base) {
        SharedPreferences prefs = base.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("language", "en");
        Locale newLocale = new Locale(lang);
        Locale.setDefault(newLocale);

        Configuration config = base.getResources().getConfiguration();
        config.setLocale(newLocale);

        super.attachBaseContext(base.createConfigurationContext(config));
    }

}
