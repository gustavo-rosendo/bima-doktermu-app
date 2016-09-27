package com.bima.dokterpribadimu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by apradanas on 5/12/16.
 */
public class DateFormatterUtils {

    private static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";
    private static final String NEWS_DISPLAY_DATE_FORMAT = "dd/MM/yyyy";

    private static final SimpleDateFormat SERVER_DATE = new SimpleDateFormat(SERVER_DATE_FORMAT, new Locale("id", "ID"));
    private static final SimpleDateFormat NEWS_DISPLAY_DATE = new SimpleDateFormat(NEWS_DISPLAY_DATE_FORMAT, new Locale("id", "ID"));

    private static final String SERVER_MEDICINE_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String MEDICINE_DISPLAY_DATE_FORMAT = "dd.MM.yy";

    private static final SimpleDateFormat SERVER_MEDICINE_DATE = new SimpleDateFormat(SERVER_MEDICINE_DATE_FORMAT, new Locale("id", "ID"));
    private static final SimpleDateFormat MEDICINE_DISPLAY_DATE = new SimpleDateFormat(MEDICINE_DISPLAY_DATE_FORMAT, new Locale("id", "ID"));


    public static String getNewsDisplayDate(String date) {
        try {
            return NEWS_DISPLAY_DATE.format(SERVER_DATE.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMedicineDisplayDate(String date) {
        try {
            return MEDICINE_DISPLAY_DATE.format(SERVER_MEDICINE_DATE.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
