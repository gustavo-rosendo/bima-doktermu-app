package com.bima.dokterpribadimu.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.data.sns.LoginClient;
import com.bima.dokterpribadimu.data.sns.LoginListener;
import com.bima.dokterpribadimu.data.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.ActivityLandingBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TokenUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.facebook.appevents.AppEventsLogger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LandingActivity extends BaseActivity {

    private static final String TAG = LandingActivity.class.getSimpleName();

    @Inject
    UserApi userApi;

    private ActivityLandingBinding binding;

    private LoginClient loginClient;

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

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onSuccess(UserProfile userProfile) {
            login(userProfile, TokenUtils.generateToken(userProfile.getId() + userProfile.getLoginType()));
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

    /**
     * Do user login.
     * @param userProfile UserProfile object from SNS login
     */
    private void login(final UserProfile userProfile, final String password) {
        userApi.login(
                    userProfile.getEmail(),
                    password,
                    userProfile.getLoginType(),
                    userProfile.getAccessToken())
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
                    public void onNext(BaseResponse signInResponse) {
                        if (signInResponse.getStatus() == Constants.Status.SUCCESS) {
                            StorageUtils.putString(
                                    LandingActivity.this,
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
                            if (signInResponse.getMessage().contains(Constants.EMAIL_IS_NOT_REGISTERED)) {
                                startActivity(RegisterNameActivity.create(LandingActivity.this, userProfile, password));
                            } else {
                                handleError(TAG, signInResponse.getMessage());
                            }
                        }
                    }
                });
    }
}
