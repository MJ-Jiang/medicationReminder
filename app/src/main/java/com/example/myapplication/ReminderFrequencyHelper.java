package com.example.myapplication;

import android.content.Context;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper class to manage reminder frequency operations such as
 * localization of frequency labels and checking if a given date matches a reminder's frequency.
 */
public class ReminderFrequencyHelper {

    /**
     * Returns a localized, user-friendly string for the given frequency key.
     *
     * <p>This method maps fixed frequency codes ("DAILY", "WEEKLY", "MONTHLY", "YEARLY")
     * to localized strings used in the UI.</p>
     *
     * @param context the Context used to access string resources
     * @param frequencyKey the frequency code string (e.g., "DAILY", "WEEKLY")
     * @return localized frequency label, or an empty string if the key is unknown
     */
    public static String getLocalizedFrequency(Context context, String frequencyKey) {
        // Translate for UI display only
        switch (frequencyKey) {
            case "DAILY":
                return context.getString(R.string.daily);
            case "WEEKLY":
                return context.getString(R.string.weekly);
            case "MONTHLY":
                return context.getString(R.string.monthly);
            case "YEARLY":
                return context.getString(R.string.yearly);
            default:
                return "";
        }
    }

    /**
     * Checks whether a given query date matches the specified reminder frequency
     * starting from the given start date.
     *
     * <p>Supports frequencies: daily, weekly, monthly, and yearly.</p>
     *
     * <ul>
     *   <li>Daily: always true</li>
     *   <li>Weekly: true if queryDate is exactly N weeks after startDate</li>
     *   <li>Monthly: true if day of month matches</li>
     *   <li>Yearly: true if day of year matches</li>
     * </ul>
     *
     * @param context the Context
     * @param queryDate the date to check
     * @param startDate the reminder's start date
     * @param frequency the frequency code ("DAILY", "WEEKLY", "MONTHLY", "YEARLY")
     * @return true if queryDate matches the frequency pattern starting from startDate, false otherwise
     */

    public static boolean isDateMatchFrequency(Context context, Date queryDate, Date startDate, String frequency) {
        long diffDays = (queryDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

        // Fixed frequency codes
        String daily = "DAILY";
        String weekly = "WEEKLY";
        String monthly = "MONTHLY";
        String yearly = "YEARLY";

        if (frequency.equalsIgnoreCase(daily)) {
            return true;
        } else if (frequency.equalsIgnoreCase(weekly)) {
            return diffDays % 7 == 0;
        } else if (frequency.equalsIgnoreCase(monthly)) {
            Calendar cal1 =Calendar.getInstance();
            Calendar cal2 =Calendar.getInstance();
            cal1.setTime(startDate);
            cal2.setTime(queryDate);
            return cal1.get(Calendar.DAY_OF_MONTH) ==cal2.get(Calendar.DAY_OF_MONTH);
        } else if (frequency.equalsIgnoreCase(yearly)) {
            Calendar cal3 =Calendar.getInstance();
            Calendar cal4 =Calendar.getInstance();
            cal3.setTime(startDate);
            cal4.setTime(queryDate);
            return cal3.get(Calendar.DAY_OF_YEAR) ==cal4.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }
}
