package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentSignInBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Token;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.Constants.Status;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.ValidationUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.DokterPribadimuDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.signInPasswordHint.setText(
                String.format(
                        getString(R.string.invalid_password_message),
                        ValidationUtils.MINIMUM_PASSWORD_LENGTH));

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = binding.signInEmailField.getText().toString();
                String password = binding.signInPasswordField.getText().toString();
                if (validateSignIn(email, password)) {
                    login(email, password);
                }
            }
        });
    }

    /**
     * Validate login email and password.
     * @param email user's email
     * @param password user's password
     * @return boolean true if email and password are valid, boolean false if otherwise
     */
    private boolean validateSignIn(String email, String password) {
        if (!ValidationUtils.isValidEmail(email)) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.invalid_email_message),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (!ValidationUtils.isValidPassword(password)) {
            Toast.makeText(
                    getActivity(),
                    String.format(getString(R.string.invalid_password_message), ValidationUtils.MINIMUM_PASSWORD_LENGTH),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            return true;
        }
        return false;
    }

    /**
     * Do user login.
     * @param email user's email
     * @param password user's password
     */
    private void login(final String email, String password) {
        userApi.login(email, password, Constants.LOGIN_TYPE_EMAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<Token>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<Token>>() {

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
                    public void onNext(BaseResponse<Token> signInResponse) {
                        if (signInResponse.getStatus() == Status.SUCCESS) {
                            if (signInResponse.getData() != null
                                    && signInResponse.getData().getToken() != null) {
                                Token.saveAccessToken(getActivity(), signInResponse.getData().getToken());
                            }

                            UserProfile userProfile = new UserProfile(
                                                                "",
                                                                "",
                                                                "",
                                                                "",
                                                                email,
                                                                "",
                                                                Constants.LOGIN_TYPE_EMAIL
                                                            );
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
                        } else {
                            handleError(TAG, signInResponse.getMessage());
                        }
                    }
                });
    }
}
