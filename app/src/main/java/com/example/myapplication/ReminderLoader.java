package com.example.myapplication;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReminderLoader {
    public static List<Reminder> loadRemindersForDate(Context context, ReminderDatabaseHelper dbHelper, String date) {
        List<Reminder> reminders = dbHelper.getRemindersForDate(context, date);

        List<Reminder> expandedReminders = new ArrayList<>();
        for (Reminder reminder : reminders) {
            for (String time : reminder.getTimes()) {
                Reminder singleTimeReminder = new Reminder(reminder);
                singleTimeReminder.setTimes(new ArrayList<>(Collections.singletonList(time)));
                expandedReminders.add(singleTimeReminder);
            }
        }

        Collections.sort(expandedReminders, (r1, r2) -> r1.getTimes().get(0).compareTo(r2.getTimes().get(0)));
        return expandedReminders;
    }
}
