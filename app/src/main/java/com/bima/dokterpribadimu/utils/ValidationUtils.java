package com.bima.dokterpribadimu.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apradanas on 2/20/16.
 */
public class ValidationUtils {

    public static final int MINIMUM_PASSWORD_LENGTH = 4;
    public static final int MINIMUM_NAME_LENGTH = 3;
    public static final int REFFERAL_LENGTH = 4;

    public static final String PHONE_NUMBER_REGEX = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

    /**
     * Validate email using Patterns
     * @param email user's email
     * @return true if email is not empty & valid, false if otherwise
     */
    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate password
     * @param password user's password
     * @return true if password is not empty & more than MINIMUM_PASSWORD_LENGTH, false if otherwise
     */
    public static boolean isValidPassword(CharSequence password) {
        return !TextUtils.isEmpty(password)
                && password.length() >= MINIMUM_PASSWORD_LENGTH;
    }

    /**
     * Validate password
     * @param refferal user's password
     * @return true if password is not empty & more than MINIMUM_PASSWORD_LENGTH, false if otherwise
     */
    public static boolean isValidRefferal(CharSequence refferal) {
        return TextUtils.isEmpty(refferal)
                || (!TextUtils.isEmpty(refferal) && refferal.length() == REFFERAL_LENGTH);
    }

    /**
     * Validate phone number
     * @param phoneNumber user's phone number
     * @return true if phoneNumber is not empty & valid, false if otherwise
     */
    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        Pattern p = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher m = p.matcher(phoneNumber);

        return !TextUtils.isEmpty(phoneNumber) && m.matches();
    }

    /**
     * Validate name
     * @param name user's name
     * @return true if name is not empty & valid, false if otherwise
     */
    public static boolean isValidName(CharSequence name) {
        Pattern p = Pattern.compile(getMinimumLengthRegex(MINIMUM_NAME_LENGTH));
        Matcher m = p.matcher(name);

        return !TextUtils.isEmpty(name) && m.matches();
    }

    /**
     * Get minimum length regex
     * @param minLength string length
     * @return regex String for minimum length
     */
    public static String getMinimumLengthRegex(int minLength) {
        return String.format("^(?=.{%d,}$).*", minLength);
    }
}
