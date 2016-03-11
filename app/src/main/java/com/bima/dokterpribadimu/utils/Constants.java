package com.bima.dokterpribadimu.utils;

/**
 * Created by apradanas on 2/3/16.
 */
public class Constants {

    public enum Status {
        SUCCESS,
        FAILED
    }

    public static final String BIMA_WEBSITE = "https://www.bimaindonesia.co.id/";
    public static final String BIMA_FACEBOOK_PAGE = "https://www.facebook.com/Bima-Indonesia-539726116184831/";
    public static final String BIMA_TWITTER = "https://twitter.com/bima_ind";
    public static final String BIMA_EMAIL = "cs@bimaindonesia.co.id";
    public static final String GOOGLE_PLAY_MANAGE_SUBSCRIPTION = "https://play.google.com/store/account";

    public static final String FACEBOOK_APP_ID = "com.facebook.katana";
    public static final String TWITTER_APP_ID = "com.twitter.android";

    public static final String NETWORK_IS_UNREACHABLE = "Network is unreachable";
    public static final String UNABLE_TO_RESOLVE_HOST = "Unable to resolve host";

    public static final int DRAWER_TYPE_DOCTOR_ON_CALL = 0;
    public static final int DRAWER_TYPE_ABOUT = 1;
    public static final int DRAWER_TYPE_PROFILE = 2;
    public static final int DRAWER_TYPE_SIGN_OUT = 3;
    public static final int DRAWER_TYPE_OTHER = 4;

    public static final String LOGIN_TYPE_EMAIL = "email";
    public static final String LOGIN_TYPE_FACEBOOK = "facebook";
    public static final String LOGIN_TYPE_GOOGLE = "google";

    public static final String KEY_USER_PROFILE = "user_profile";
    public static final String KEY_USER_SUBSCIPTION = "user_subscription";
    public static final String KEY_BOOK_CALL_TIME_MILLIS = "book_call_time_millis";
}
