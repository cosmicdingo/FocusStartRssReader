package com.example.focusstartrssreader.tools.converter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    private final static String TAG = "date converter";
    private final static String pattern = "E, dd MMM yyyy HH:mm:ss";

    public static long dateToTime(String source) {

        SimpleDateFormat dateFormat;

        if(source.charAt(0) >= '0' && source.charAt(0) <= '9')
            dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
        else dateFormat = new SimpleDateFormat(pattern, Locale.US);
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }
}
