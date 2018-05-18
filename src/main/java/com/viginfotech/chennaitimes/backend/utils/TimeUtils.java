package com.viginfotech.chennaitimes.backend.utils;


import com.viginfotech.chennaitimes.backend.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static final long ONE_SECOND_IN_MILLIS = 1000;
    public static final long ONE_MINUTE_IN_MILLIS = 60 * ONE_SECOND_IN_MILLIS;
    public static final long ONE_HOUR_IN_MILLIS = 60 * ONE_MINUTE_IN_MILLIS;
    public static final long ONE_DAY_IN_MILLIS = 24 * ONE_HOUR_IN_MILLIS;
    private static SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = new SimpleDateFormat[]{
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00", Locale.US),
            new SimpleDateFormat("MM/dd/yy", Locale.US)


    };

    public static Date parseTimeStamp(String timestamp, int source) {
        int i = 0;
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {

            try {

                Date data = format.parse(timestamp);
                if (i == 0 && source == Constants.SOURCE_NAKKHEERAN) {
                    return new Date(data.getTime() - ((5 * ONE_HOUR_IN_MILLIS) + (30 * ONE_MINUTE_IN_MILLIS)));
                }
                i++;
                return data;


            } catch (ParseException ex) {
                continue;
            }
        }


        return null;


    }


    public static long getLast6HoursInMills() {
        long now = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.HOUR_OF_DAY, -6);

        System.out.println("time " + calendar.getTime());
        //return new Date(now -(6*ONE_HOUR_IN_MILLIS)).getTime();
        return calendar.getTimeInMillis();
    }

    public static long getLast24HoursInMills() {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("IST"));
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        System.out.println("time " + calendar.getTime());
        //return new Date(now -(6*ONE_HOUR_IN_MILLIS)).getTime();
        return calendar.getTimeInMillis();
    }

    public static long getLastTwoHourInMills() {
        long now = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -2);

        System.out.println("2 hours back " + calendar.getTimeInMillis());

        return calendar.getTimeInMillis();
    }
}
