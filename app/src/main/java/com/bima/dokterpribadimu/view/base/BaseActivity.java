package com.bima.dokterpribadimu.view.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.facebook.appevents.AppEventsLogger;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by apradanas on 2/3/16.
 */
public class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    protected void startLandingActivity() {
        startActivity(new Intent(this, LandingActivity.class));
    }

    protected void startSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
