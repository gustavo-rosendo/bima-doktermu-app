package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.ActivityRegisterNameBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterNameActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_LOCATION_PERMISSION = 1;

    private static final String TAG = RegisterNameActivity.class.getSimpleName();
    private static final String USER_PROFILE = "user_profile";
    private static final String PASSWORD = "password";

    @Inject
    UserApi userApi;

    private ActivityRegisterNameBinding binding;

    private UserProfile userProfile;
    private String password;
    private Location location;
    private LocationTracker locationTracker;

    private Tracker mTracker;

    public static Intent create(Context context, UserProfile userProfile, String password) {
        Intent intent = new Intent(context, RegisterNameActivity.class);
        intent.putExtra(USER_PROFILE, GsonUtils.toJson(userProfile));
        intent.putExtra(PASSWORD, password);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_name);

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
    }

    @Override
    protected void onDestroy() {
        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        super.onDestroy();
    }

    private void init() {
        userProfile = GsonUtils.fromJson(getIntent().getStringExtra(USER_PROFILE), UserProfile.class);
        password = getIntent().getStringExtra(PASSWORD);

        initLocation();
        initViews();
    }

    private void initViews() {
        if (!userProfile.getLoginType().equalsIgnoreCase(Constants.LOGIN_TYPE_EMAIL)) {
            binding.registerPasswordContainer.setVisibility(View.VISIBLE);
        }

        binding.registerNameField.setText(userProfile.getName());

        binding.registerValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userProfile.getLoginType().equalsIgnoreCase(Constants.LOGIN_TYPE_EMAIL)) {
                    password = binding.registerPasswordField.getText().toString();
                }

                String referral = binding.registerAgentField.getText().toString();
                String name = binding.registerNameField.getText().toString();
                String[] names = name.split(" ");
                if (validateRegistration(name, password, referral)) {
                    userProfile.setName(name);
                    if (names.length > 0) {
                        userProfile.setFirstName(names[0]);
                        userProfile.setLastName(names[names.length - 1]);
                    }
                    userProfile.setReferral(referral);
                    if (location != null) {
                        userProfile.setRegisterLat(location.getLatitude());
                        userProfile.setRegisterLong(location.getLongitude());
                    }

                    register(password);
                }
            }
        });
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    public void initLocation() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startLocationTracker();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationTracker() {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true);

        try {
            locationTracker = new LocationTracker(this, settings) {

                @Override
                public void onLocationFound(@NonNull Location location) {
                    RegisterNameActivity.this.location = location;
                    stopListening();
                }

                @Override
                public void onTimeout() {
                    startLocationTracker();
                }
            };
            locationTracker.startListening();
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Do user register.
     * @param password user's password
     */
    private void register(String password) {
        userApi.register(userProfile, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse registerResponse) {
                        dismissProgressDialog();

                        if (registerResponse.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putString(
                                    RegisterNameActivity.this,
                                    Constants.KEY_USER_PROFILE,
                                    GsonUtils.toJson(userProfile)
                            );

                            //Doktermu AdMobs Tracking - Registration
                            //Google Android in-app conversion tracking snippet for successful Registration
                            AdWordsConversionReporter.reportWithConversionId(DokterPribadimuApplication.getInstance().getApplicationContext(),
                                    "926691219", "bo6bCMjIu2UQk9_wuQM", "1.00", true);

                            //Google Analytics to track number of registrations (all: from AdMobs + others)
                            mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Growth")
                                    .setAction("Registration")
                                    .setLabel(userProfile.getLoginType())
                                    .setValue(1)
                                    .build());

                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_signed_in),
                                    getString(R.string.dialog_signed_in_message),
                                    getString(R.string.dialog_get_started),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            IntentUtils.startHomeActivityOnTop(RegisterNameActivity.this);
                                        }
                                    });
                        } else {
                            handleError(TAG, registerResponse.getMessage());
                        }
                    }
                });
    }

    /**
     * Validate registration name and password.
     * @param name user's name
     * @param password user's password
     * @param referral user's referral number
     * @return boolean true if name and password are valid, boolean false if otherwise
     */
    private boolean validateRegistration(String name, String password, String referral) {
        if (!ValidationUtils.isValidName(name)) {
            binding.registerNameContainer.setError(getString(R.string.invalid_name_message));
        } else {
            binding.registerNameContainer.setError(null);
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.registerPasswordContainer.setError(
                    String.format(
                            getString(R.string.invalid_password_message),
                            ValidationUtils.MINIMUM_PASSWORD_LENGTH));
        } else {
            binding.registerPasswordContainer.setError(null);
        }

        if (!ValidationUtils.isValidRefferal(referral)) {
            binding.registerAgentContainer.setError(getString(R.string.invalid_referral_message));
        } else {
            binding.registerAgentContainer.setError(null);
        }
        
        return ValidationUtils.isValidName(name)
                && ValidationUtils.isValidPassword(password)
                && ValidationUtils.isValidRefferal(referral);
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
