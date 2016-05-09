package com.bima.dokterpribadimu.view.activities;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.ActivityLandingBinding;
import com.bima.dokterpribadimu.utils.DeviceInfoUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LandingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = LandingActivity.class.getSimpleName();
    private static final int RC_PHONE_STATE_PERMISSION = 0;

    private ActivityLandingBinding binding;

    private DeviceInfoUtils deviceInfoUtils;

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onDestroy() {
        FacebookClient.release();
        GplusClient.release();

        super.onDestroy();
    }

    private void init() {
        initDeviceInfo();
        initViews();
    }

    @AfterPermissionGranted(RC_PHONE_STATE_PERMISSION)
    public void initDeviceInfo() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
            deviceInfoUtils = DeviceInfoUtils.getInstance(this);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_PHONE_STATE_PERMISSION, Manifest.permission.READ_PHONE_STATE);
        }
    }

    private void initViews() {
        binding.loginSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startSignInActivity(LandingActivity.this, true);
            }
        });

        binding.loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startSignInActivity(LandingActivity.this, false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
