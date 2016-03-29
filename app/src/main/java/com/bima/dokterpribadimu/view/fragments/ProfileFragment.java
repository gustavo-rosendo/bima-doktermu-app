package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentProfileBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.ChangePasswordDialog;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private static final String EMAIL_FILTER_REGEX = "(?<=.{2}).(?=[^@]*?.@)";

    @Inject
    UserApi userApi;

    private FragmentProfileBinding binding;

    private ChangePasswordDialog dialog;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        // set default profile picture
        Picasso.with(getActivity())
                .load(R.drawable.ic_profile_picture)
                .into(binding.profilePictureImage);

        boolean subscriptionActive =
                StorageUtils.getBoolean(getActivity(), Constants.KEY_USER_SUBSCIPTION, false);
        if (subscriptionActive) {
            binding.profileMonthlySubscriptionText.setText(getString(R.string.subscription_enabled));
            binding.profileMonthlySubscriptionText.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
        } else {
            binding.profileMonthlySubscriptionText.setText(getString(R.string.subscription_disabled));
            binding.profileMonthlySubscriptionText.setTextColor(ContextCompat.getColor(getActivity(), R.color.orange));
        }

        UserProfile userProfile = UserProfileUtils.getUserProfile(getActivity());

        try {
            Picasso.with(getActivity())
                    .load(userProfile.getPicture())
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.ic_profile_picture)
                    .error(R.drawable.ic_profile_picture)
                    .into(binding.profilePictureImage);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }

        binding.profileNameText.setText(userProfile.getName());

        binding.profileAppInfo.setText(
                String.format(getString(R.string.profile_app_info), BuildConfig.VERSION_NAME));

        if (userProfile.getLoginType().equals(Constants.LOGIN_TYPE_EMAIL)) {
            binding.profileEmailAddressText.setText(
                    userProfile.getEmail().replaceAll(EMAIL_FILTER_REGEX, "*"));

            binding.profileEditPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog == null) {
                        dialog = new ChangePasswordDialog(getActivity());
                    }

                    dialog.setListener(new ChangePasswordDialog.OnChangePasswordDialogClickListener() {
                        @Override
                        public void onClick(ChangePasswordDialog dialog, String oldPassword, String newPassword) {
                            changePassword(
                                    oldPassword,
                                    newPassword,
                                    UserProfileUtils.getUserProfile(getActivity()).getAccessToken());
                        }
                    }).showDialog();
                }
            });
        } else {
            binding.profilePersonalInfoLayout.setVisibility(View.GONE);
        }

        binding.profileManageSubscriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startViewIntent(Constants.GOOGLE_PLAY_MANAGE_SUBSCRIPTION);
            }
        });
    }

    /**
     * Do change password.
     * @param oldPassword user's email
     * @param newPassword user's password
     * @param accessToken user's access token
     */
    private void changePassword(final String oldPassword, String newPassword, final String accessToken) {
        userApi.changePassword(oldPassword, newPassword, accessToken)
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
                    public void onNext(BaseResponse response) {
                        dismissProgressDialog();

                        if (response.getStatus() == Constants.Status.SUCCESS) {
                            dialog.dismiss();
                            dialog = null;

                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_signed_in),
                                    getString(R.string.dialog_password_changed_message),
                                    getString(R.string.dialog_get_started),
                                    null);
                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }

}
