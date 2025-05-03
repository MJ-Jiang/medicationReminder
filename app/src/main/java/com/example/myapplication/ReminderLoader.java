package com.example.myapplication;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReminderLoader {
    public static List<Reminder> loadRemindersForDate(Context context, ReminderDatabaseHelper dbHelper, String date) {
        // Get reminders from database (already expanded by time)
        List<Reminder> reminders = dbHelper.getRemindersForDate(context, date);

        // Sort by time
        Collections.sort(reminders, (r1, r2) -> {
            // Handle potential null times
            String time1 = r1.getTime() != null ? r1.getTime() : "";
            String time2 = r2.getTime() != null ? r2.getTime() : "";
            return time1.compareTo(time2);
        });

        return reminders;
    }
}
