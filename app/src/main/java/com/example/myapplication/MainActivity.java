package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewSelectedDate;
    private Button buttonCreateReminder;
    private ImageButton buttonSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        buttonCreateReminder = findViewById(R.id.buttonCreateReminder);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);

        // 设置默认日期为今天
        textViewSelectedDate.setText(Utils.getTodayDate());

        // "Create Reminder" 按钮跳转到 CreateReminderActivity
        buttonCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateReminderActivity.class);
                startActivity(intent);
            }
        });

        // 日历按钮（目前先放着，后面加日历选择）
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 之后加日期选择器，现在可以先弹一个Toast测试
                Utils.showToast(MainActivity.this, "Date Picker Coming Soon!");
            }
        });
    }
}
