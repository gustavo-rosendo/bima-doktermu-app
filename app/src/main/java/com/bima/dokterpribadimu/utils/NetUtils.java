package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by apradanas on 2/3/16.
 */
public class NetUtils {

    /**
     * Get network availability
     * @param context current application context
     * @return true if network is available, false if otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        return context == null
                || ((ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo() != null;
    }
}
