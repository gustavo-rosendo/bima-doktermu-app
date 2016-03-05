package com.bima.dokterpribadimu.model;

import android.content.Context;

import com.bima.dokterpribadimu.utils.StorageUtils;

/**
 * Created by apradanas on 3/5/16.
 */
public class Token {

    private static final String PREF_ACCESS_TOKEN = "token.access_token";

    private String token;

    public String getToken() {
        return token;
    }

    public static void saveAccessToken(Context context, String token) {
        StorageUtils.putString(context, PREF_ACCESS_TOKEN, token);
    }

    public static void removeAccessToken(Context context) {
        StorageUtils.remove(context, PREF_ACCESS_TOKEN);
    }

    public static String getAccessToken(Context context) {
        return StorageUtils.getString(context, PREF_ACCESS_TOKEN, null);
    }
}
