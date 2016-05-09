package com.bima.dokterpribadimu.utils;

import android.content.Context;
import android.content.Intent;

import com.bima.dokterpribadimu.view.activities.HomeActivity;

/**
 * Created by apradanas on 5/9/16.
 */
public class IntentUtils {

    /**
     *
     * @param context caller's activity / fragment context
     */
    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    /**
     * Start HomeActivity and clear any stack behind it
     * @param context caller's activity / fragment context
     */
    public static void startHomeActivityOnTop(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
