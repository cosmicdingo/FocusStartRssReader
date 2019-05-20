package com.example.focusstartrssreader;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private final static String TAG = "date converter";
    private final static String pattern = "EEE, DD MMM yyyy HH:mm:ss";

    public static long dateToTime(String source) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss", Locale.US);
        try {
            Date date = dateFormat.parse(source);
            Log.d(TAG, "dateToTime: string date = " + dateFormat.format(date));
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String timeToDate(long millis) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }
}
