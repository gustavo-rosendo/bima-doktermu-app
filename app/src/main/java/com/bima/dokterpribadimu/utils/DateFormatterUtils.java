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

    public static String getNewsDisplayDate(String date) {
        try {
            return NEWS_DISPLAY_DATE.format(SERVER_DATE.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
