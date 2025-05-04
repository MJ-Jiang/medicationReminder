package com.example.myapplication;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

public class ReminderFrequencyHelper {
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

    public static boolean isDateMatchFrequency(Context context, Date queryDate, Date startDate, String frequency) {
        long diffDays = (queryDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);

        // Fixed frequency codes, do not translate here
        String daily = "DAILY";
        String weekly = "WEEKLY";
        String monthly = "MONTHLY";
        String yearly = "YEARLY";

        if (frequency.equalsIgnoreCase(daily)) {
            return true;
        } else if (frequency.equalsIgnoreCase(weekly)) {
            return diffDays % 7 == 0;
        } else if (frequency.equalsIgnoreCase(monthly)) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(startDate);
            cal2.setTime(queryDate);
            return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
        } else if (frequency.equalsIgnoreCase(yearly)) {
            Calendar cal3 = Calendar.getInstance();
            Calendar cal4 = Calendar.getInstance();
            cal3.setTime(startDate);
            cal4.setTime(queryDate);
            return cal3.get(Calendar.DAY_OF_YEAR) == cal4.get(Calendar.DAY_OF_YEAR);
        } else {
            return false;
        }
    }
}
