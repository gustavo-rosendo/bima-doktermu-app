package com.bima.dokterpribadimu.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by apradanas on 3/11/16.
 */
public class TimeUtils {

    public static final double ONE_HOUR_MS = 60 * 60 * 1000; // 1 hour in milliseconds

    private static final String SUBSCRIPTION_ORDER_DATE_FORMAT = "dd MMMM yyyy";

    public static int getCurrentTimeHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }

    public static int getCurrentTimeAmPm() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.AM_PM);
    }

    public static String getSubscriptionOrderDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(SUBSCRIPTION_ORDER_DATE_FORMAT, new Locale("id", "ID"));
        return sdf.format(new Date());
    }

    public static double getElapsedTimeMillis() {
        long elapsedTime = System.nanoTime();
        double elapsedTimeMillis = (double) elapsedTime / 1000000.0;
        return elapsedTimeMillis;
    }

    public static boolean hasOneHourPassed(double initialTimeMillis) {
        boolean oneHourPassed = false;

        double deltaTimeMillis = getElapsedTimeMillis() - initialTimeMillis;

        if(deltaTimeMillis >= ONE_HOUR_MS || deltaTimeMillis < 0.0) {
            oneHourPassed = true;
        }

        return oneHourPassed;
    }
}
