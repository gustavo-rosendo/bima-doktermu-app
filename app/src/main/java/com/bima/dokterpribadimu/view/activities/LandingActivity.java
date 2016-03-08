package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.sns.LoginClient;
import com.bima.dokterpribadimu.data.sns.LoginListener;
import com.bima.dokterpribadimu.data.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.ActivityLandingBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.facebook.appevents.AppEventsLogger;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class LandingActivity extends BaseActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private ActivityLandingBinding binding;

    private LoginClient loginClient;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        checkLocationTrackerPermission();
        initLoginClient();
        initViews();
    }

    private void initViews() {
        binding.loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClient = FacebookClient.getInstance();
                loginClient.init(LandingActivity.this, loginListener);
                loginClient.signIn();
            }
        });

        binding.loginGplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClient = GplusClient.getInstance();
                loginClient.init(LandingActivity.this, loginListener);
                loginClient.signIn();
            }
        });

        binding.loginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInActivity();
            }
        });
    }

    private void checkLocationTrackerPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            startLocationTracker();
        }
    }

    private void initLoginClient() {
        loginClient = FacebookClient.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginClient.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginClient.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onActivityResult(requestCode, resultCode, data);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                    startLocationTracker();
                }
                break;
        }
    }

    private void startLocationTracker() {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true);

        try {
            LocationTracker tracker = new LocationTracker(this, settings) {

                @Override
                public void onLocationFound(@NonNull Location location) {
                    LandingActivity.this.location = location;
                    stopListening();
                }

                @Override
                public void onTimeout() {

                }
            };
            tracker.startListening();
        } catch (SecurityException se) {
            requestLocationPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onSuccess(UserProfile userProfile) {
            StorageUtils.putString(
                    LandingActivity.this,
                    Constants.KEY_USER_PROFILE,
                    GsonUtils.toJson(userProfile));

            showSuccessDialog(
                    R.drawable.ic_dialog_success,
                    getString(R.string.dialog_success),
                    getString(R.string.dialog_sign_in_success_message),
                    getString(R.string.dialog_get_started),
                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                        @Override
                        public void onClick(DokterPribadimuDialog dialog) {
                            startDoctorCallActivityOnTop();
                        }
                    });
        }

        @Override
        public void onSignOut() {

        }

        @Override
        public void onFail() {
            showErrorDialog(
                    R.drawable.ic_bug,
                    getString(R.string.dialog_failed),
                    getString(R.string.dialog_sign_in_failed_message),
                    getString(R.string.dialog_try_once_more),
                    null);
        }

        @Override
        public void onCancel() {
            Toast.makeText(
                    LandingActivity.this,
                    getString(R.string.dialog_sign_in_canceled_message),
                    Toast.LENGTH_SHORT
            ).show();
        }
    };
}
