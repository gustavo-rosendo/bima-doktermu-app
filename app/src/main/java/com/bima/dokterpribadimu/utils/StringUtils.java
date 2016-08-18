package com.bima.dokterpribadimu.utils;

/**
 * Created by apradanas.
 */
public class StringUtils {

    private static final String DASH = "-";

    public static String getStringOrDashIfNull(String string) {
        return string != null ? string : DASH;
    }
}
