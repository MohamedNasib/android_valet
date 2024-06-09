package com.icapp.icapp.Helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtils {
    public static String getCurrentTimeIn12HourFormat() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Format the time in 12-hour format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return sdf.format(calendar.getTime());
    }

    public static String getCurrentDateTime() {
        long date = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM , yyyy h:mm a");
        return dateFormat.format(date);
    }

    public static String convertDateTime(Long date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM , yyyy h:mm a");
        return dateFormat.format(date);
    }

    public static String convertTime(Long date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }
}
