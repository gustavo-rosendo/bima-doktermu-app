package com.bima.dokterpribadimu.utils;

import java.util.Calendar;

/**
 * Created by apradanas on 3/11/16.
 */
public class TimeUtils {

    public static final double ONE_HOUR_MS = 60 * 60 * 1000; // 1 hour in milliseconds

    public static int getCurrentTimeHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }

    public static int getCurrentTimeAmPm() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.AM_PM);
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
