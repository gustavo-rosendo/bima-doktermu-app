package com.bima.dokterpribadimu.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.ActivityRegisterNameBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Token;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

import javax.inject.Inject;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.ProviderError;
import fr.quentinklein.slt.TrackerSettings;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterNameActivity extends BaseActivity {

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

        init();
    }

    private void init() {
        userProfile = GsonUtils.fromJson(getIntent().getStringExtra(USER_PROFILE), UserProfile.class);
        password = getIntent().getStringExtra(PASSWORD);

        initLocation();
        initViews();
    }

    private void initViews() {
        binding.registerValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.registerNameField.getText().toString();
                String[] names = name.split(" ");
                if (ValidationUtils.isValidName(binding.registerNameField.getText().toString())) {
                    userProfile.setName(name);
                    if (names.length > 0) {
                        userProfile.setFirstName(names[0]);
                        userProfile.setLastName(names[names.length - 1]);
                    }
                    userProfile.setReferral(binding.registerAgentField.getText().toString());
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
            LocationTracker tracker = new LocationTracker(this, settings) {

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
            tracker.startListening();
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
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse registerResponse) {
                        if (registerResponse.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putString(
                                    RegisterNameActivity.this,
                                    Constants.KEY_USER_PROFILE,
                                    GsonUtils.toJson(userProfile)
                            );

                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_signed_in),
                                    getString(R.string.dialog_signed_in_message),
                                    getString(R.string.dialog_get_started),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            startDoctorCallActivityOnTop();
                                        }
                                    });
                        } else {
                            handleError(TAG, registerResponse.getMessage());
                        }
                    }
                });
    }
}
