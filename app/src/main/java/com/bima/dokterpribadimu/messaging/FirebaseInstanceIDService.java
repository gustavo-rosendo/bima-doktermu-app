package com.bima.dokterpribadimu.messaging;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;

import android.util.Log;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "DPM_FirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String firebase_token = FirebaseInstanceId.getInstance().getToken();

        registerToken(firebase_token);

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + firebase_token);
    }

    public static String getToken() {

        String firebase_token = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Get token: " + firebase_token);

        return firebase_token;
    }

    /**
     * Do register token.
     * @param firebaseToken firebase client token received from the firebase messaging service
     */
    private void registerToken(final String firebaseToken) {


        StorageUtils.putString(FirebaseInstanceIDService.this,
                               Constants.KEY_FIREBASE_ACCESS_TOKEN,
                               firebaseToken);

        StorageUtils.putBoolean(FirebaseInstanceIDService.this,
                Constants.KEY_FIREBASE_ACCESS_TOKEN_SAVED,
                false);

    }
}
