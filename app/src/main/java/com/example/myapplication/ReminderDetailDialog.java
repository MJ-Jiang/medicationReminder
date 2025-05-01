package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;

public class ReminderDetailDialog {

    public static void show(Context context, Reminder reminder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(reminder.getName())
                .setMessage(buildDetailMessage(context, reminder))
                .setPositiveButton(context.getString(R.string.ok), null)
                .show();
    }

    private static String buildDetailMessage(Context context, Reminder reminder) {
        ReminderDatabaseHelper dbHelper = new ReminderDatabaseHelper(context);
        String localizedFrequency = dbHelper.getLocalizedFrequency(context, reminder.getFrequency());
        String timesText = TextUtils.join("\n", reminder.getTimes());

        return context.getString(R.string.reminder_description) + ": " + reminder.getDescription() + "\n\n" +
                context.getString(R.string.reminder_dosage) + ": " + reminder.getDosage() + "\n\n" +
                context.getString(R.string.frequency) + ": " + localizedFrequency + "\n\n" +
                context.getString(R.string.description_period) + ": " + reminder.getDisplayStartDate() + " - " + reminder.getDisplayEndDate() + "\n\n" +
                context.getString(R.string.description_alltimes) + ":\n" + timesText;
    }
}
