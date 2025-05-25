package com.example.myapplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A utility class providing helper methods for date formatting and other common tasks.
 */
public class Utils {

    /**
     * Returns the current date formatted as a string in the pattern "yyyy-MM-dd".
     * <p>
     * This method uses the default locale of the device.
     *
     * @return A string representing today's date in "yyyy-MM-dd" format.
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        return sdf.format(new Date());
    }

}
