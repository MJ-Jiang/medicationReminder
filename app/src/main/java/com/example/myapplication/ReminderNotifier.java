package com.example.myapplication;

import android.content.Context;
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
        Log.d("Notifier", "Checking reminders on: " + today);

        for (Reminder reminder : todayReminders) {
            Log.d("Notifier", "Found reminder: " + reminder.getName() + ", time: " + reminder.getTime());

            // 如果已经完成就跳过
            if (reminder.getIsCompleted()) {
                continue;
            }

            // 如果没有通知过，并且到达提醒时间
            if (!reminder.getIsNotified() && isTimeToNotify(reminder.getTime())) {
                // 弹出提醒
                Toast.makeText(context, reminder.getName() + " It's time to take pills", Toast.LENGTH_LONG).show();

                // 更新提醒的 isNotified 状态为 true
                int result = dbHelper.updateReminderNotified(reminder.getId(), true);
              //  Log.d("Notifier", "Update result for " + reminder.getName() + ": " + result);
            }
        }
    }

    // 判断是否是提醒的时间
    private boolean isTimeToNotify(String reminderTime) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Helsinki");
        Calendar now = Calendar.getInstance(timeZone);
       // Log.d("Notifier", "Time Zone: " + timeZone.getID());
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);
       // Log.d("Notifier", "Current time: " + currentHour + ":" + currentMinute);

        try {
            String[] timeParts = reminderTime.split(":");
            int reminderHour = Integer.parseInt(timeParts[0]);
            int reminderMinute = Integer.parseInt(timeParts[1]);
            Log.d("Notifier", "Reminder time: " + reminderHour + ":" + reminderMinute);

            // 如果当前时间和提醒时间一致
            return (currentHour == reminderHour && currentMinute == reminderMinute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
