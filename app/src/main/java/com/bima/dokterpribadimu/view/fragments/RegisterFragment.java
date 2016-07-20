package com.bima.dokterpribadimu.view.fragments;


import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentRegisterBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.DeviceInfoUtils;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TokenUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.activities.RegisterNameActivity;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.base.BaseFragment;
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

/**
 * A simple {@link BaseFragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    private static final int RC_PHONE_STATE_PERMISSION = 0;
    private static final int RC_LOCATION_PERMISSION = 1;

    private static final String TAG = RegisterFragment.class.getSimpleName();

    @Inject
    UserApi userApi;

    private UserProfile userProfile;
    private Location location;
    private LocationTracker locationTracker;
    private FragmentRegisterBinding binding;
    private DeviceInfoUtils deviceInfoUtils;

//    private Tracker mTracker;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        initLocation();
        initDeviceInfo();
        initViews();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        FirebaseAnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_REGISTER);
    }

    @AfterPermissionGranted(RC_PHONE_STATE_PERMISSION)
    public void initDeviceInfo() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            deviceInfoUtils = DeviceInfoUtils.getInstance(getActivity());
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location),
                    RC_PHONE_STATE_PERMISSION, Manifest.permission.READ_PHONE_STATE);
        }
    }

    private void initViews() {
        binding.registerFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_FB_SCREEN_REGISTER);
                ((SignInActivity) getActivity()).snsLogin(Constants.LOGIN_TYPE_FACEBOOK);
            }
        });

        binding.registerGplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_GPLUS_SCREEN_REGISTER);
                ((SignInActivity) getActivity()).snsLogin(Constants.LOGIN_TYPE_GOOGLE);
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_REGISTER_SCREEN_REGISTER);

                final String email = binding.registerEmailField.getText().toString();
                String password = binding.registerPasswordField.getText().toString();
                String referral = binding.registerAgentField.getText().toString();
                String name = binding.registerNameField.getText().toString();
                String[] names = name.split(" ");

                if (validateRegistration(name, email, password, referral)) {
                    userProfile = new UserProfile(
                            "",
                            "",
                            "",
                            "",
                            email,
                            "",
                            "",
                            "",
                            Constants.LOGIN_TYPE_EMAIL,
                            0.0,
                            0.0,
                            TokenUtils.generateToken(email + System.currentTimeMillis())
                    );

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

                    if (deviceInfoUtils != null) {
                        userProfile.setDeviceType(
                                String.format(
                                        Constants.DEVICE_TYPE_FORMAT,
                                        deviceInfoUtils.getBrand(),
                                        deviceInfoUtils.getProduct()));
                        userProfile.setDeviceImei(deviceInfoUtils.getDeviceId());
                        if(userProfile.getMsisdn().isEmpty()) {
                            userProfile.setMsisdn(deviceInfoUtils.getMsisdnPhoneNumber());
                        }
                        userProfile.setDeviceOperator(deviceInfoUtils.getSimOperatorName());
                        userProfile.setDeviceSoftware(String.format(Constants.DEVICE_SOFTWARE_FORMAT, deviceInfoUtils.getRelease()));
                    }

                    register(password);
                }

            }
        });
    }

    @AfterPermissionGranted(RC_LOCATION_PERMISSION)
    public void initLocation() {
        if (EasyPermissions.hasPermissions(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
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
            locationTracker = new LocationTracker(
                    DokterPribadimuApplication.getInstance().getApplicationContext(), settings) {

                @Override
                public void onLocationFound(@NonNull Location location) {
                    RegisterFragment.this.location = location;
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
     * Validate registration name, email, password and referral.
     * @param name user's name
     * @param email user's email
     * @param password user's password
     * @param referral user's referral number
     * @return boolean true if name, email, password and referral are valid, boolean false if otherwise
     */
    private boolean validateRegistration(String name, String email, String password, String referral) {
        if (!ValidationUtils.isValidName(name)) {
            binding.registerNameContainer.setError(getString(R.string.invalid_name_message));
        } else {
            binding.registerNameContainer.setError(null);
        }

        if (!ValidationUtils.isValidEmail(email)) {
            binding.registerEmailContainer.setError(getString(R.string.invalid_email_message));
        } else {
            binding.registerEmailContainer.setError(null);
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
                &&ValidationUtils.isValidEmail(email)
                && ValidationUtils.isValidPassword(password)
                && ValidationUtils.isValidRefferal(referral);
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
                                    DokterPribadimuApplication.getInstance().getApplicationContext(),
                                    Constants.KEY_USER_PROFILE,
                                    GsonUtils.toJson(userProfile)
                            );

                            //Doktermu AdMobs Tracking - Registration
                            //Google Android in-app conversion tracking snippet for successful Registration
//                            AdWordsConversionReporter.reportWithConversionId(DokterPribadimuApplication.getInstance().getApplicationContext(),
//                                    "926691219", "bo6bCMjIu2UQk9_wuQM", "1.00", true);

                            //Google Analytics to track number of registrations (all: from AdMobs + others)
//                            mTracker.send(new HitBuilders.EventBuilder()
//                                    .setCategory("Growth")
//                                    .setAction("Registration")
//                                    .setLabel(userProfile.getLoginType())
//                                    .setValue(1)
//                                    .build());

                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_signed_in),
                                    getString(R.string.dialog_signed_in_message),
                                    getString(R.string.dialog_get_started),
                                    new DokterPribadimuDialog.OnDokterPribadimuDialogClickListener() {
                                        @Override
                                        public void onClick(DokterPribadimuDialog dialog) {
                                            IntentUtils.startHomeActivityOnTop(DokterPribadimuApplication.getInstance().getApplicationContext());
                                        }
                                    });
                            FirebaseAnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_REGISTER_SUCCESS);
                        } else {
                            handleError(TAG, registerResponse.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    @Override
    public void onDestroy() {
        if (locationTracker != null) {
            locationTracker.stopListening();
        }

        super.onDestroy();
    }
}
