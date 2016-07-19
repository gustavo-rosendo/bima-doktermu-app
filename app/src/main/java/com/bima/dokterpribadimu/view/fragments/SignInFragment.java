package com.bima.dokterpribadimu.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentSignInBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.Constants.Status;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TokenUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.activities.SignInActivity;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;
import com.bima.dokterpribadimu.view.components.ForgotPasswordDialog;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class SignInFragment extends BaseFragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    @Inject
    UserApi userApi;

    private FragmentSignInBinding binding;

    private ForgotPasswordDialog forgotPasswordDialog;

    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseAnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_LOGIN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.signInFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignInActivity) getActivity()).snsLogin(Constants.LOGIN_TYPE_FACEBOOK);
            }
        });

        binding.signInGplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignInActivity) getActivity()).snsLogin(Constants.LOGIN_TYPE_GOOGLE);
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.signInEmailField.getText().toString();
                String password = binding.signInPasswordField.getText().toString();
                if (validateSignIn(email, password)) {
                    login(email, password, TokenUtils.generateToken(email + System.currentTimeMillis()));
                }
            }
        });

        binding.signInWhatsMyPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (forgotPasswordDialog == null) {
                    forgotPasswordDialog = new ForgotPasswordDialog(getActivity());
                }

                forgotPasswordDialog.setListener(new ForgotPasswordDialog.OnForgotPasswordDialogClickListener() {
                    @Override
                    public void onClick(ForgotPasswordDialog dialog, String email) {
                        dismissForgotPasswordDialog();

                        resetPassword(email, TokenUtils.generateToken(email + System.currentTimeMillis()));
                    }
                });

                forgotPasswordDialog.showDialog();
            }
        });
    }

    private void dismissForgotPasswordDialog() {
        forgotPasswordDialog.dismiss();
        forgotPasswordDialog.clearReference();
        forgotPasswordDialog = null;
    }

    /**
     * Validate login email and password.
     * @param email user's email
     * @param password user's password
     * @return boolean true if email and password are valid, boolean false if otherwise
     */
    private boolean validateSignIn(String email, String password) {
        if (!ValidationUtils.isValidEmail(email)) {
            binding.signInEmailContainer.setError(getString(R.string.invalid_email_message));
        } else {
            binding.signInEmailContainer.setError(null);
        }

        if (!ValidationUtils.isValidPassword(password)) {
            binding.signInPasswordContainer.setError(
                    String.format(
                            getString(R.string.invalid_password_message),
                            ValidationUtils.MINIMUM_PASSWORD_LENGTH));
        } else {
            binding.signInPasswordContainer.setError(null);
        }

        return ValidationUtils.isValidEmail(email) && ValidationUtils.isValidPassword(password);
    }

    /**
     * Do user login.
     * @param email user's email
     * @param password user's password
     */
    private void login(final String email, String password, final String accessToken) {
        userApi.login(email, password, Constants.LOGIN_TYPE_EMAIL, accessToken, null)
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

                        if (signInResponse.getStatus() == Status.SUCCESS) {

                            UserProfile userProfile = UserProfileUtils.getUserProfile(
                                    DokterPribadimuApplication.getInstance().getApplicationContext());

                            if(userProfile == null) {
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
                                        accessToken
                                );
                            }
                            else {
                                userProfile.setEmail(email);
                                userProfile.setLoginType(Constants.LOGIN_TYPE_EMAIL);
                                userProfile.setAccessToken(accessToken);
                            }

                            StorageUtils.putString(
                                    getActivity(),
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
                            FirebaseAnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_LOGIN_SUCCESS);
                        } else {
                            handleError(TAG, signInResponse.getMessage());
                        }
                    }
                });
    }

    /**
     * Do reset login
     * @param email user's email
     */
    private void resetPassword(final String email, final String accessToken) {
        userApi.resetPassword(email, accessToken)
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

                        if (signInResponse.getStatus() == Status.SUCCESS) {
                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_forgot_password_success),
                                    getString(R.string.dialog_forgot_password_success_message),
                                    getString(R.string.dialog_take_me_home),
                                    null);
                            FirebaseAnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_LOGIN_FORGOT_PASSWORD_SUCCESS);
                        } else {
                            String errorMessage = signInResponse.getMessage();
                            if (errorMessage.contains(Constants.EMAIL_NOT_FOUND)) {
                                showErrorDialog(TAG,
                                        R.drawable.ic_bug,
                                        getString(R.string.dialog_reset_password_email_not_registered_title),
                                        getString(R.string.dialog_reset_password_email_not_registered_message),
                                        getString(R.string.dialog_try_once_more),
                                        null);
                            } else {
                                handleError(TAG, signInResponse.getMessage());
                            }
                        }
                    }
                });
    }
}
