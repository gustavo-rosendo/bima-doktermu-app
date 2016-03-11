package com.bima.dokterpribadimu.utils;

import java.util.Calendar;

/**
 * Created by apradanas on 3/11/16.
 */
public class TimeUtils {

    public static int getCurrentTimeHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }

    public static int getCurrentTimeAmPm() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.AM_PM);
    }
}
