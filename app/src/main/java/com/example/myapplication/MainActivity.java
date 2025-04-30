package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ReminderAdapter.OnReminderClickListener {
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private ReminderDatabaseHelper dbHelper;
    private static final int DATE_PICKER_REQUEST=1;

    private TextView textViewSelectedDate;
    private Button buttonCreateReminder;
    private ImageButton buttonSelectDate;
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

        // 设置日期选择监听
        findViewById(R.id.buttonSelectDate).setOnClickListener(v -> showDatePicker());

        // 加载今日提醒
        loadRemindersForDate(Utils.getTodayDate());
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);

        // 设置默认日期为今天
        textViewSelectedDate.setText(Utils.getTodayDate());

        // "Create Reminder" 按钮跳转到 CreateReminderActivity
        buttonCreateReminder.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateReminderActivity.class));
        });
        buttonSelectDate.setOnClickListener(v -> showDatePicker());
    }
    private void showDatePicker(){
        String currentDate=textViewSelectedDate.getText().toString();
        Calendar calendar=Calendar.getInstance();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            calendar.setTime(sdf.parse(currentDate));
        }catch (Exception e){
            e.printStackTrace();
        }
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDate = sdf.format(selectedCalendar.getTime());

                    textViewSelectedDate.setText(selectedDate);
                    loadRemindersForDate(selectedDate);
                    Log.d("DEBUG", "select date: " + selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }
    private void loadRemindersForDate(String date) {
        List<Reminder> reminders = dbHelper.getRemindersForDate(date);

        // 展开多个时间的提醒（一个时间对应一个条目）
        List<Reminder> expandedReminders = new ArrayList<>();
        for (Reminder reminder : reminders) {
            for (String time : reminder.getTimes()) {
                Reminder singleTimeReminder = new Reminder(reminder); // 需要实现拷贝构造方法
                singleTimeReminder.setTimes(new ArrayList<>(Collections.singletonList(time)));
                expandedReminders.add(singleTimeReminder);
            }
        }

        adapter.updateData(expandedReminders);
        ((TextView)findViewById(R.id.textViewSelectedDate)).setText(date);
    }

    @Override
    public void onReminderClick(Reminder reminder) {
        showReminderDetailDialog(reminder);
    }

    @Override
    public void onCheckedChanged(Reminder reminder, boolean isChecked) {
        // 更新数据库中的完成状态
        dbHelper.updateReminderCompletion(reminder.getId(), isChecked);
    }

    private void showReminderDetailDialog(Reminder reminder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(reminder.getName())
                .setMessage(buildDetailMessage(reminder))
                .setPositiveButton("OK", null)
                .show();
    }

    private String buildDetailMessage(Reminder reminder) {
        return "Description: " + reminder.getDescription() + "\n\n" +
                "Dosage: " + reminder.getDosage() + "\n\n" +
                "Frequency: " + reminder.getFrequency() + "\n\n" +
                "Period: " + reminder.getDisplayStartDate() + " to " + reminder.getDisplayEndDate() + "\n\n" +
                "All Times:\n" + TextUtils.join("\n", reminder.getTimes());
    }
}
