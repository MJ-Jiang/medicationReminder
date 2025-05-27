package com.example.myapplication;


import java.util.Collections;
import java.util.List;

/**
 * Utility class for loading and sorting reminders for a specific date.
 */
public class ReminderLoader {
    /**
     * Loads reminders for the given date from the database and sorts them by time.
     *
     * <p>This method retrieves all reminders for a specific date using the provided
     * {@link ReminderDatabaseHelper}, then sorts them chronologically by their time
     * value (as a string). If any reminder has a null time, it is treated as an empty string
     * during sorting to avoid errors.</p>
     *
     * @param dbHelper the database helper used to query reminders
     * @param date     the date for which to load reminders (formatted as yyyy-MM-dd)
     * @return a list of reminders for the specified date, sorted by time
     */
    public static List<Reminder> loadRemindersForDate( ReminderDatabaseHelper dbHelper, String date) {
        // Get reminders from database (already expanded by time)
        List<Reminder> reminders = dbHelper.getRemindersForDate( date);

        // Sort by time
        Collections.sort(reminders, (r1, r2) -> {
            // Handle potential null times
            String time1 =r1.getTime() != null ?r1.getTime() : "";
            String time2 =r2.getTime() !=null ?r2.getTime() : "";
            return time1.compareTo(time2);
        });
        return reminders;
    }
}
