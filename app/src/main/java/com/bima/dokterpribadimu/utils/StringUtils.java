package com.bima.dokterpribadimu.utils;

/**
 * Created by apradanas.
 */
public class StringUtils {

    private static final String DASH = "-";

    public static String getStringOrDashIfNull(String string) {
        return (string != null && string != "") ? string : DASH;
    }

    public static String getFileNameOnly(String string) {
        int index = string.lastIndexOf("/");
        return string.substring(index + 1);
    }
}
