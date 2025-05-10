package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ReminderNotifier notifier;
    private Handler handler = new Handler(Looper.getMainLooper());

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
        ImageView buttonSelectDate = findViewById(R.id.buttonSelectDate);
        FloatingActionButton fabSettings = findViewById(R.id.fabSettings);


        textViewSelectedDate.setText(Utils.getTodayDate());
        updateReminderList(Utils.getTodayDate());

        buttonCreateReminder.setOnClickListener(v -> startActivity(new Intent(this, CreateReminderActivity.class)));

        buttonSelectDate.setOnClickListener(v ->
                DatePickerHelper.show(this, textViewSelectedDate.getText().toString(), textViewSelectedDate, adapter, dbHelper));
        fabSettings.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        dbHelper = new ReminderDatabaseHelper(this);
        notifier = new ReminderNotifier(this, dbHelper);
        Runnable reminderCheckRunnable = new Runnable() {
            @Override
            public void run() {
                notifier.checkAndNotifyReminders();
                handler.postDelayed(this, 60000); // 每分钟执行一次
            }
        };
        handler.post(reminderCheckRunnable);

    }
    private void updateReminderList(String date){
        List<Reminder> reminders=ReminderLoader.loadRemindersForDate(this,dbHelper,date);
        adapter.updateData(reminders);

    }


    @Override
    public void onReminderClick(Reminder reminder) {
        // This now only loads the times, not full reminders
        List<String> times = dbHelper.getGroupTimes(reminder.getGroupId());
        ReminderDetailDialog.show(this, reminder, times);
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
