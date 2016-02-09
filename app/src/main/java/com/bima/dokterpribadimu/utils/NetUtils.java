package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by apradanas on 2/3/16.
 */
public class NetUtils {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }
}
