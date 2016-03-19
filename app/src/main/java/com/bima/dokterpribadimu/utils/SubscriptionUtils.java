package com.bima.dokterpribadimu.utils;

/**
 * Created by gusta_000 on 15/3/2016.
 */
public class SubscriptionUtils {

    private static final String JANUARI     = "Jan";
    private static final String FEBRUARI    = "Feb";
    private static final String MARET       = "Mar";
    private static final String APRIL       = "Apr";
    private static final String MEI         = "Mei";
    private static final String JUNI        = "Jun";
    private static final String JULI        = "Jul";
    private static final String AGUSTUS     = "Ag";
    private static final String SEPTEMBER   = "Sep";
    private static final String OKTOBER     = "Ok";
    private static final String NOPEMBER    = "No";
    private static final String DESEMBER    = "De";

    public static String formatDateOfBirth(String dateOfBirth) {
        String finalDateOfBirth;

        String[] day_Month_Year = dateOfBirth.split(" ");
        if(day_Month_Year.length == 3) {
            finalDateOfBirth = day_Month_Year[2];
            if(day_Month_Year[1].contains(JANUARI)) {
                finalDateOfBirth += "-01-";
            }
            else if(day_Month_Year[1].contains(FEBRUARI)) {
                finalDateOfBirth += "-02-";
            }
            else if(day_Month_Year[1].contains(MARET)) {
                finalDateOfBirth += "-03-";
            }
            else if(day_Month_Year[1].contains(APRIL)) {
                finalDateOfBirth += "-04-";
            }
            else if(day_Month_Year[1].contains(MEI)) {
                finalDateOfBirth += "-05-";
            }
            else if(day_Month_Year[1].contains(JUNI)) {
                finalDateOfBirth += "-06-";
            }
            else if(day_Month_Year[1].contains(JULI)) {
                finalDateOfBirth += "-07-";
            }
            else if(day_Month_Year[1].contains(AGUSTUS)) {
                finalDateOfBirth += "-08-";
            }
            else if(day_Month_Year[1].contains(SEPTEMBER)) {
                finalDateOfBirth += "-09-";
            }
            else if(day_Month_Year[1].contains(OKTOBER)) {
                finalDateOfBirth += "-10-";
            }
            else if(day_Month_Year[1].contains(NOPEMBER)) {
                finalDateOfBirth += "-11-";
            }
            else if(day_Month_Year[1].contains(DESEMBER)) {
                finalDateOfBirth += "-12-";
            }
            finalDateOfBirth += day_Month_Year[0]; //finally date of birth has the format "yyyy-MM-dd"
        } else {
            // An error occurred, return an empty String
            finalDateOfBirth = "";
        }

        return finalDateOfBirth;
    }

}
