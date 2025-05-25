package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;

import java.util.List;
/**
 * A utility class that displays a detailed dialog showing information about a {@link Reminder}.
 *
 * <p>This dialog presents the reminder's name as the title and shows details such as description,
 * dosage, frequency, reminder period, and all scheduled times in a formatted message.</p>
 */
public class ReminderDetailDialog {
    /**
     * Shows a dialog with detailed information about the specified {@link Reminder}.
     *
     * @param context the Context in which the dialog should be shown
     * @param reminder the Reminder object whose details are to be displayed
     * @param times a list of reminder times as strings (e.g., ["2:00", "3:15"]) to display in the dialog
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
     * Builds the detailed message string shown in the reminder dialog.
     *
     * <p>Includes the reminder's description, dosage, localized frequency, active period,
     * and all scheduled times formatted as a clean string.</p>
     *
     * @param context the Context used to retrieve localized strings
     * @param reminder the Reminder object providing the data
     * @param times the list of reminder times to display
     * @param dbHelper helper for accessing reminder-related data
     * @return a formatted string containing all reminder details for display
     */

    private static String buildDetailMessage(Context context, Reminder reminder, List<String> times, ReminderDatabaseHelper dbHelper) {
        String localizedFrequency = ReminderFrequencyHelper.getLocalizedFrequency(context, reminder.getFrequency());
        String timesText =times.toString().replace("[", "").replace("]", "");
// Shows as [2:00, 3:15]
        return context.getString(R.string.reminder_description) + ": " + reminder.getDescription() + "\n\n" +
                context.getString(R.string.reminder_dosage) + ": " + reminder.getDosage() + "\n\n" +
                context.getString(R.string.frequency) + ": " + localizedFrequency + "\n\n" +
                context.getString(R.string.description_period) + ": " +
                reminder.getDisplayStartDate() + " - " + reminder.getDisplayEndDate() + "\n\n" +
                context.getString(R.string.description_alltimes) + ": " + timesText;
    }
}
