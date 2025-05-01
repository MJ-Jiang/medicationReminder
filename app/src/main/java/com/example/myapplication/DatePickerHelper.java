package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatePickerHelper {
    public static void show(Context context, String currentDate, TextView dateTextView, ReminderAdapter adapter, ReminderDatabaseHelper dbHelper) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            calendar.setTime(sdf.parse(currentDate));
        } catch (Exception e) {
            e.printStackTrace();
        }

        new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth);
                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime());

                    List<Reminder> reminders = ReminderLoader.loadRemindersForDate(context, dbHelper, selectedDate);
                    adapter.updateData(reminders);
                    dateTextView.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
