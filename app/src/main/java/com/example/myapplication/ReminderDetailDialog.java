package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import java.util.List;
/**
 * Utility class for displaying detailed information about a {@link Reminder} in a dialog.
 *
 * <p>This dialog presents the reminder's name as the title and displays key details such as
 * description, dosage, frequency, active period, and scheduled times in a well-formatted message.</p>
 */
public class ReminderDetailDialog {
    /**
     * Displays an AlertDialog with detailed information about the specified {@link Reminder}.
     *
     * @param context  the context used to build and display the dialog
     * @param reminder the Reminder object whose details will be shown
     * @param times    a list of scheduled reminder times (e.g., ["2:00", "3:15"])
     */
    public static void show(Context context, Reminder reminder, List<String> times) {
        ReminderDatabaseHelper dbHelper =new ReminderDatabaseHelper(context);

        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle(reminder.getName())
                .setMessage(buildDetailMessage(context, reminder,times,dbHelper))
                .setPositiveButton(context.getString(R.string.ok), null)
                .show();
    }
    /**
     * Constructs the formatted message used in the reminder detail dialog.
     *
     * <p>The message includes the reminder's description, dosage, localized frequency,
     * display period (start and end dates), and all scheduled times.</p>
     *
     * @param context  the context used to fetch localized string resources
     * @param reminder the Reminder object containing the detail information
     * @param times    a list of scheduled times to be included in the message
     * @param dbHelper an instance of {@link ReminderDatabaseHelper} (currently unused)
     * @return a formatted string that presents all relevant details about the reminder
     */

    private static String buildDetailMessage(Context context, Reminder reminder, List<String> times, ReminderDatabaseHelper dbHelper) {
        String frequencyKey = reminder.getFrequency();
        String localizedFrequency;

        switch (frequencyKey) {
            case "DAILY":
                localizedFrequency = context.getString(R.string.daily);
                break;
            case "WEEKLY":
                localizedFrequency = context.getString(R.string.weekly);
                break;
            case "MONTHLY":
                localizedFrequency = context.getString(R.string.monthly);
                break;
            case "YEARLY":
                localizedFrequency = context.getString(R.string.yearly);
                break;
            default:
                localizedFrequency = "";
                break;
        }

        String timesText = times.toString().replace("[", "").replace("]", "");
// Shows as [2:00, 3:15]
        return context.getString(R.string.reminder_description) + ": " + reminder.getDescription() + "\n\n" +
                context.getString(R.string.reminder_dosage) + ": " + reminder.getDosage() + "\n\n" +
                context.getString(R.string.frequency) + ": " + localizedFrequency + "\n\n" +
                context.getString(R.string.description_period) + ": " +
                reminder.getDisplayStartDate() + " - " + reminder.getDisplayEndDate() + "\n\n" +
                context.getString(R.string.description_alltimes) + ": " + timesText;
    }

}
