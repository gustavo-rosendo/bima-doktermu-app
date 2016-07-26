package com.bima.dokterpribadimu.view.activities;

import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityLandingBinding;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.facebook.appevents.AppEventsLogger;

public class LandingActivity extends BaseActivity {

    private static final String TAG = LandingActivity.class.getSimpleName();
    private static final int RC_PHONE_STATE_PERMISSION = 0;

    private ActivityLandingBinding binding;

//    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_LANDING);

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    private void init() {
        initViews();
    }

    private void initViews() {
        binding.loginSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_LOGIN_SCREEN_LANDING);
                IntentUtils.startSignInActivity(LandingActivity.this, true);
            }
        });

        binding.loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_REGISTER_SCREEN_LANDING);
                IntentUtils.startSignInActivity(LandingActivity.this, false);
            }
        });
    }
}
