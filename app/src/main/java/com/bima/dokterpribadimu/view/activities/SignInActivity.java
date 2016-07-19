package com.bima.dokterpribadimu.view.activities;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.data.sns.LoginClient;
import com.bima.dokterpribadimu.data.sns.LoginListener;
import com.bima.dokterpribadimu.data.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.ActivitySignInBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.DeviceInfoUtils;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.fragments.RegisterFragment;
import com.bima.dokterpribadimu.view.fragments.SignInFragment;

import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final String SIGN_IN = "sign_in";
    private static final int RC_PHONE_STATE_PERMISSION = 0;

    @Inject
    UserApi userApi;

    private ActivitySignInBinding binding;

//    private Tracker mTracker;
    private boolean isSignIn;

    private LoginClient loginClient;
    private DeviceInfoUtils deviceInfoUtils;

    public static Intent create(Context context, boolean isSignIn) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(SIGN_IN, isSignIn);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        initDeviceInfo();
        initLoginClient();
        initViews();
    }

    private void initViews() {
        isSignIn = getIntent().getBooleanExtra(SIGN_IN, true);

        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isSignIn) {
            ft.replace(R.id.signin_frame, SignInFragment.newInstance()).commit();
            binding.toolbarTitle.setText(getString(R.string.sign_in));
        } else {
            ft.replace(R.id.signin_frame, RegisterFragment.newInstance()).commit();
            binding.toolbarTitle.setText(getString(R.string.register));
        }
    }

    private void initLoginClient() {
        loginClient = FacebookClient.getInstance();
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

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        FacebookClient.release();
        GplusClient.release();

        super.onDestroy();
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

    public void snsLogin(String loginType) {
        loginClient = loginType.equalsIgnoreCase(Constants.LOGIN_TYPE_FACEBOOK) ?
                FacebookClient.getInstance() : GplusClient.getInstance();
        loginClient.init(SignInActivity.this, loginListener);
        loginClient.signIn();
    }

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onSuccess(UserProfile userProfile) {
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

            login(userProfile, "");
        }

        @Override
        public void onSignOut() {

        }

        @Override
        public void onFail() {
            showErrorDialog(TAG,
                    R.drawable.ic_bug,
                    getString(R.string.dialog_failed),
                    getString(R.string.dialog_sign_in_failed_message),
                    getString(R.string.dialog_try_once_more),
                    null);
        }

        @Override
        public void onCancel() {
            Toast.makeText(
                    SignInActivity.this,
                    getString(R.string.dialog_sign_in_canceled_message),
                    Toast.LENGTH_SHORT
            ).show();
        }
    };

    /**
     * Do user login.
     * @param userProfile UserProfile object from SNS login
     */
    private void login(final UserProfile userProfile, final String password) {
        userApi.login(
                userProfile.getEmail(),
                password,
                userProfile.getLoginType(),
                userProfile.getAccessToken(),
                userProfile)
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
                    public void onNext(BaseResponse signInResponse) {
                        dismissProgressDialog();

                        if (signInResponse.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putString(
                                    SignInActivity.this,
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
                                            IntentUtils.startHomeActivityOnTop(SignInActivity.this);
                                        }
                                    });
                        } else {
                            if (signInResponse.getMessage().contains(Constants.EMAIL_IS_NOT_REGISTERED) ||
                                    signInResponse.getMessage().equalsIgnoreCase(Constants.EMAIL_IS_NOT_REGISTERED)) {
                                startActivity(RegisterNameActivity.create(SignInActivity.this, userProfile, password));
                            } else {
                                handleError(TAG, signInResponse.getMessage());
                            }
                        }
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
