package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import java.util.List;

public class ReminderDetailDialog {

    public static void show(Context context, Reminder reminder, List<String> times) {
        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(reminder.getName())
                .setMessage(buildDetailMessage(context, reminder, times, dbHelper))
                .setPositiveButton(context.getString(R.string.ok), null)
                .show();
    }

    private static String buildDetailMessage(Context context, Reminder reminder, List<String> times, ReminderDatabaseHelper dbHelper) {
        String localizedFrequency = ReminderFrequencyHelper.getLocalizedFrequency(context, reminder.getFrequency());
        String timesText = times.toString() // Shows as [2:00, 3:15]
                .replace("[", "")
                .replace("]", "");

        return context.getString(R.string.reminder_description) + ": " + reminder.getDescription() + "\n\n" +
                context.getString(R.string.reminder_dosage) + ": " + reminder.getDosage() + "\n\n" +
                context.getString(R.string.frequency) + ": " + localizedFrequency + "\n\n" +
                context.getString(R.string.description_period) + ": " +
                reminder.getDisplayStartDate() + " - " + reminder.getDisplayEndDate() + "\n\n" +
                context.getString(R.string.description_alltimes) + ": " + timesText;
    }
}
