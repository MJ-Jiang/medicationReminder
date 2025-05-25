package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Helper class for displaying a date picker dialog and handling date selection logic.
 *
 * <p>This class provides two overloads of the {@code show} method for use in different
 * parts of the application, including {@code MainActivity} and {@code ChartSettingsActivity}.
 * It handles updating the selected date in the UI and optionally refreshing reminders or chart data.</p>
 */
public class DatePickerHelper {

    /**
     * Displays a date picker dialog and updates the UI and reminder list on date selection.
     *
     * <p>This overload is intended for use in {@code MainActivity}, where a list of reminders
     * is updated based on the selected date.</p>
     *
     * @param context       the current context (should be an activity)
     * @param currentDate   the currently selected date in "yyyy-MM-dd" format
     * @param dateTextView  the TextView to display the selected date
     * @param adapter       the adapter used to update the list of reminders
     * @param dbHelper      the database helper used to retrieve reminders
     */
    public static void show(Context context, String currentDate, TextView dateTextView, ReminderAdapter adapter, ReminderDatabaseHelper dbHelper) {
        showDatePicker(context, currentDate, dateTextView, adapter, dbHelper);
    }

    /**
     * Displays a date picker dialog and updates the UI on date selection.
     *
     * <p>This overload is intended for use in {@code ChartSettingsActivity}, where no adapter
     * is used, and chart data may be updated based on the selected date.</p>
     *
     * @param context       the current context (should be an activity)
     * @param currentDate   the currently selected date in "yyyy-MM-dd" format
     * @param dateTextView  the TextView to display the selected date
     * @param dbHelper      the database helper used to access reminder data if needed
     */
    public static void show(Context context, String currentDate, TextView dateTextView, ReminderDatabaseHelper dbHelper) {
        showDatePicker(context, currentDate, dateTextView, null, dbHelper);
    }

    /**
     * Internal method that shows the date picker dialog and handles date changes.
     *
     * @param context       the current context
     * @param currentDate   the date to initialize the date picker with
     * @param dateTextView  the TextView that displays the selected date
     * @param adapter       optional ReminderAdapter to update the reminder list
     * @param dbHelper      the database helper to query reminder data
     */
    private static void showDatePicker(Context context, String currentDate, TextView dateTextView, ReminderAdapter adapter, ReminderDatabaseHelper dbHelper) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());//Creates a date formatter,Turn a Date → String
            //Locale.getDefault(): Use the phone’s current language/region setting
            calendar.setTime(sdf.parse(currentDate));//parse the input currentDate and update the calendar to that date. Turn a String → Date
        } catch (Exception e) {
            e.printStackTrace();
        }

        //context is used to tell the system on which interface this dialog box should be displayed.
        //view The dialog box itself
        new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {////Callback function: what to do after the user selects a date
            Calendar selected =Calendar.getInstance();//representing the current date and time.
            selected.set(year,month,dayOfMonth);//Updates the selected calendar object to match the user-selected date.
            String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime());
            //Converts the Calendar date into a formatted String.
            dateTextView.setText(selectedDate);// Updates the TextView to show the date the user picked.

                    // If adapter is provided, update the reminder list
            if (adapter != null) {
                List<Reminder> reminders = ReminderLoader.loadRemindersForDate( dbHelper, selectedDate);
                adapter.updateData(reminders);
            }
                    // If in ChartSettingsActivity, trigger chart data update
            if (context instanceof ChartSettingsActivity) {
                ((ChartSettingsActivity) context).loadChartData();//call a method to reload or refresh chart data for the selected date.
            }

            },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)//When opening the date picker, the default selected date
        ).show(); //.show() is to actually display this dialog box
    }
    //First, parse a Calendar object from the string currentDate, and use the year, month, and day in
    // the 'calendar' to tell the DatePickerDialog to select this date by default when it is opened. After
    // the user selects a date, the callback function creates a new Calendar object 'selected' with the
    // selected year, month, and day, formats selected into a string (for easy display and database query),
    // and updates the reminder list according to the selected date.
}