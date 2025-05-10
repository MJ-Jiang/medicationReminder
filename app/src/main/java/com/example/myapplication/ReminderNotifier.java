package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import java.util.List;

import java.util.TimeZone;


public class ReminderNotifier {

    private Context context;
    private ReminderDatabaseHelper dbHelper;

    public ReminderNotifier(Context context, ReminderDatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }



    // 检查并提醒
    public void checkAndNotifyReminders() {
        String today = Utils.getTodayDate(); // 获取今天的日期
        List<Reminder> todayReminders = ReminderQueryHelper.getAllRemindersByDateRange(dbHelper, today);  // 获取今天的提醒
        for (Reminder reminder : todayReminders) {

            if (reminder.getIsCompleted()) {
                continue;
            }

            // 如果没有通知过，并且到达提醒时间
            if (!reminder.getIsNotified() && isTimeToNotify(reminder.getTime())) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    new AlertDialog.Builder(context)
                            .setTitle("Reminder")
                            .setMessage(reminder.getName() + " It's time to take pills")
                            .setPositiveButton("OK", null)
                            .show();
                });


                dbHelper.updateReminderNotified(reminder.getId(), true);
            }

        }
    }

    // 判断是否是提醒的时间
    private boolean isTimeToNotify(String reminderTime) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Helsinki");
        Calendar now = Calendar.getInstance(timeZone);

        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);


        try {
            String[] timeParts = reminderTime.split(":");
            int reminderHour = Integer.parseInt(timeParts[0]);
            int reminderMinute = Integer.parseInt(timeParts[1]);


            // 如果当前时间和提醒时间一致
            return (currentHour == reminderHour && currentMinute == reminderMinute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
