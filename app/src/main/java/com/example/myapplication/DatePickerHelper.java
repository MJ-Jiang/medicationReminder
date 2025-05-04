package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DatePickerHelper {

    // 原始方法（供 MainActivity 使用）
    public static void show(Context context, String currentDate, TextView dateTextView,
                            ReminderAdapter adapter, ReminderDatabaseHelper dbHelper) {
        showDatePicker(context, currentDate, dateTextView, adapter, dbHelper);
    }

    // 新增方法（供 ChartSettingsActivity 使用，不需要 adapter）
    public static void show(Context context, String currentDate, TextView dateTextView,
                            ReminderDatabaseHelper dbHelper) {
        showDatePicker(context, currentDate, dateTextView, null, dbHelper);
    }

    // 私有方法，统一处理日期选择逻辑
    private static void showDatePicker(Context context, String currentDate, TextView dateTextView,
                                       ReminderAdapter adapter, ReminderDatabaseHelper dbHelper) {
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
                    String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(selected.getTime());

                    dateTextView.setText(selectedDate);

                    // 如果有 adapter，更新列表数据（MainActivity 使用）
                    if (adapter != null) {
                        List<Reminder> reminders = ReminderLoader.loadRemindersForDate(context, dbHelper, selectedDate);
                        adapter.updateData(reminders);
                    }
                    // 如果是 ChartSettingsActivity，可以在这里触发图表更新
                    if (context instanceof ChartSettingsActivity) {
                        ((ChartSettingsActivity) context).loadChartData();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}