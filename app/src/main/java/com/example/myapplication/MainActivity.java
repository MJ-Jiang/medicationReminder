package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int DATE_PICKER_REQUEST=1;

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
        buttonCreateReminder.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateReminderActivity.class));
        });
        buttonSelectDate.setOnClickListener(v -> showDatePicker());
    }
    private void showDatePicker(){
        String currentDate=textViewSelectedDate.getText().toString();
        Calendar calendar=Calendar.getInstance();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            calendar.setTime(sdf.parse(currentDate));
        }catch (Exception e){
            e.printStackTrace();
        }
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    textViewSelectedDate.setText(selectedDate);

                    //
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }
}


