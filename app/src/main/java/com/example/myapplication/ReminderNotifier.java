package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import java.util.Locale;
import java.util.TimeZone;

/**
 * ReminderNotifier handles checking reminders for the current day and notifying the user
 * at the appropriate times using Android AlertDialogs.
 * <p>
 * It queries the database for today's reminders, checks their completion and notification status,
 * and shows a notification dialog if it's time to remind the user.
 * </p>
 */
public class ReminderNotifier {

    private Context context;
        private ReminderDatabaseHelper dbHelper;
    /**
     * Constructs a ReminderNotifier with the given context and database helper.
     *
     * @param context  the application context used to display dialogs
     * @param dbHelper the database helper to query reminders and update notification status
     */

    public ReminderNotifier(Context context,ReminderDatabaseHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }


    /**
     * Checks today's reminders and shows a notification dialog for each reminder
     * that has not been completed or notified and whose reminder time matches the current time.
     */

    public void checkAndNotifyReminders() {

        String today=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        List<Reminder> todayReminders =ReminderQueryHelper.getRemindersForDate(dbHelper, today);

        for (Reminder reminder : todayReminders) {

            if (reminder.getIsCompleted()) {
                continue;
            }


            if (!reminder.getIsNotified() && isTimeToNotify(reminder.getTime())) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    //Create a Handler that will send the task (Runnable) to the main thread's message queue
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
    /**
     * Determines if the current time matches the specified reminder time.
     *
     * @param reminderTime the reminder time as a string in "HH:mm" format
     * @return true if current time matches reminder time, false otherwise
     */
    private boolean isTimeToNotify(String reminderTime) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Helsinki");
        Calendar now = Calendar.getInstance(timeZone);

        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);


        try {
            String[] timeParts = reminderTime.split(":");
            int reminderHour = Integer.parseInt(timeParts[0]);
            int reminderMinute = Integer.parseInt(timeParts[1]);



            return (currentHour == reminderHour && currentMinute == reminderMinute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
