package com.bima.dokterpribadimu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterNameActivity extends BaseActivity {

    private static final String TAG = RegisterNameActivity.class.getSimpleName();
    private static final String USER_PROFILE = "user_profile";
    private static final String PASSWORD = "password";

    @Inject
    UserApi userApi;

    private ActivityRegisterNameBinding binding;

    private UserProfile userProfile;
    private String password;

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

                    register(password);
                }
            }
        });
    }

    /**
     * Do user register.
     * @param password user's password
     */
    private void register(String password) {
        userApi.register(userProfile, password)
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
                    public void onNext(BaseResponse<Token> registerResponse) {
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
