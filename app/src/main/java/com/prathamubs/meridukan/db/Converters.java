package com.prathamubs.meridukan.db;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {
    private static final String TAG = Converters.class.getName();
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

    @TypeConverter
    public static Date dateFromString(String dateString) {
        try {
            return dateString == null ? null : DATE_TIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse from " + dateString);
            return null;
        }
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return date == null ? null : DATE_TIME_FORMAT.format(date);
    }
}
