package com.bima.dokterpribadimu.utils;

import android.text.TextUtils;

/**
 * Created by apradanas on 2/20/16.
 */
public class ValidationUtils {

    public static final int MINIMUM_PASSWORD_LENGTH = 4;

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
}
