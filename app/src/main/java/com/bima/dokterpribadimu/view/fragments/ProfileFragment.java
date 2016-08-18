package com.bima.dokterpribadimu.view.fragments;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.remote.api.CallHistoryApi;
import com.bima.dokterpribadimu.data.remote.api.ProfileApi;
import com.bima.dokterpribadimu.data.remote.api.UserApi;
import com.bima.dokterpribadimu.databinding.FragmentProfileBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.BimaCall;
import com.bima.dokterpribadimu.model.CallHistoryResponse;
import com.bima.dokterpribadimu.model.ProfileResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.ChangePasswordDialog;
import com.bima.dokterpribadimu.view.components.ChangePhoneNumberDialog;
import com.bima.dokterpribadimu.viewmodel.CallHistoryItemViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private static final String EMAIL_FILTER_REGEX = "(?<=.{2}).(?=[^@]*?.@)";

    private static final int CALL_HISTORY_LIMIT = 200;

    private static final int MAX_NUM_CALLS_TO_SHOW = 3;

    @Inject
    UserApi userApi;

    @Inject
    ProfileApi profileApi;

    @Inject
    CallHistoryApi callHistoryApi;

    private FragmentProfileBinding binding;

    private ChangePasswordDialog dialog;

    private ChangePhoneNumberDialog phoneNumberDialog;

    private CallHistoryListViewModel callHistoryListViewModel = new CallHistoryListViewModel();
    private List<BimaCall> callHistoryItems = new ArrayList<>();

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
    public void onResume() {
        super.onResume();
        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_PROFILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        initViews();

        initProfileInfo();

        initCallHistory();

        return binding.getRoot();
    }

    private void initProfileInfo() {
        getProfileInfo(UserProfileUtils.getUserProfile(getActivity()).getAccessToken());
    }

    private void initCallHistory() {
        binding.setViewModel(callHistoryListViewModel);

        getCallHistory(CALL_HISTORY_LIMIT, UserProfileUtils.getUserProfile(getActivity()).getAccessToken());

        binding.profileCallHistoryMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexNextItem = callHistoryListViewModel.items.size();
                if(indexNextItem < callHistoryItems.size()) {
                    int i = 0;
                    for(; i < MAX_NUM_CALLS_TO_SHOW && (indexNextItem + i < callHistoryItems.size()) ; i++) {
                        callHistoryListViewModel.items.add(
                                new CallHistoryItemViewModel(
                                        callHistoryItems.get(indexNextItem + i),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // add action when user clicks on a call
                                            }
                                        })
                        );
                    }

                    //Hide the "Show more" button if
                    //all calls in the history were already shown
                    if(indexNextItem + i >= callHistoryItems.size()) {
                        binding.profileCallHistoryMore.setVisibility(View.GONE);
                    }
                }
            }
        });
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
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_PASSWORD_EDIT_SCREEN_PROFILE);

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
            binding.profileEmailAddressLinearLayout.setVisibility(View.GONE);
            binding.profileEditPasswordLinearLayout.setVisibility(View.GONE);
        }

        if(userProfile.getMsisdn() == null || userProfile.getMsisdn().isEmpty()) {
            binding.profileMyPhoneNumberImageView.setImageResource(R.drawable.ic_add);
        }
        else {
            binding.profileMyPhoneNumberText.setText(userProfile.getMsisdn());
            binding.profileMyPhoneNumberImageView.setImageResource(R.drawable.ic_edit);
        }
        binding.profileMyPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_PHONE_EDIT_SCREEN_PROFILE);

                if (phoneNumberDialog == null) {
                    phoneNumberDialog = new ChangePhoneNumberDialog(getActivity());
                }

                phoneNumberDialog.setListener(new ChangePhoneNumberDialog.OnChangePhoneNumberDialogClickListener() {
                    @Override
                    public void onClick(ChangePhoneNumberDialog phoneNumberDialog, String newPhoneNumber) {
                        changePhoneNumber(
                                newPhoneNumber,
                                UserProfileUtils.getUserProfile(getActivity()).getAccessToken());
                    }
                }).showDialog();
            }
        });

        binding.profileManageSubscriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_SETTING_SCREEN_PROFILE);
                startViewIntent(Constants.GOOGLE_PLAY_MANAGE_SUBSCRIPTION);
            }
        });

        binding.profileGeneralInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startGeneralInformationActivity(getActivity());
            }
        });

        binding.profileCurrentHealthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startCurrentHealthActivity(getActivity());
            }
        });

        binding.profileMedicineInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startMedicineInformationActivity(getActivity());
            }
        });
    }

    @Override
    public void onDestroy() {
        for (CallHistoryItemViewModel itemViewModel : callHistoryListViewModel.items) {
            itemViewModel.setClickListener(null);
        }
        callHistoryListViewModel.items.clear();

        super.onDestroy();
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
                            AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_PROFILE_CHANGE_PASSWORD_SUCCESS);
                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }

    /**
     * Do change phone number.
     * @param newPhoneNumber user's phoneNumber
     * @param accessToken user's access token
     */
    private void changePhoneNumber(final String newPhoneNumber, final String accessToken) {
        profileApi.changePhoneNumber(newPhoneNumber, accessToken)
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
                            phoneNumberDialog.dismiss();
                            phoneNumberDialog= null;

                            UserProfile userProfile = UserProfileUtils.getUserProfile(getActivity());
                            userProfile.setMsisdn(newPhoneNumber);
                            StorageUtils.putString(
                                    getActivity(),
                                    Constants.KEY_USER_PROFILE,
                                    GsonUtils.toJson(userProfile)
                            );
                            binding.profileMyPhoneNumberText.setText(newPhoneNumber);
                            binding.profileMyPhoneNumberImageView.setImageResource(R.drawable.ic_edit);

                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_signed_in),
                                    getString(R.string.dialog_phone_number_changed_message),
                                    getString(R.string.dialog_take_me_home),
                                    null);
                            AnalyticsHelper.logViewDialogEvent(EventConstants.DIALOG_PROFILE_CHANGE_PHONE_SUCCESS);
                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }

    /**
     * Get user's profile info
     * @param accessToken user's access token
     */
    private void getProfileInfo(final String accessToken) {
        profileApi.getProfileInfo(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<ProfileResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<ProfileResponse>>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                        binding.dialogProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.dialogProgressBar.setVisibility(View.GONE);

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<ProfileResponse> profileResponse) {
                        if (profileResponse.getStatus() == Constants.Status.SUCCESS) {
                            String subscriptionEnd = profileResponse.getData().getProfile().getSubscriptionEnd();
                            if(subscriptionEnd == null) {
                                subscriptionEnd = "";
                            }
                            binding.profileNextBillingDateText.setText(subscriptionEnd);

                            String memberNumber = profileResponse.getData().getProfile().getMemberNumber();
                            if(memberNumber == null) {
                                memberNumber = "";
                            }
                            binding.profileMemberNumberText.setText(memberNumber);
                        } else {
                            handleError(TAG, profileResponse.getMessage());
                        }
                    }
                });
    }

    /**
     * Get call history list
     * @param limit restrict the number of returned calls from the history
     * @param accessToken user's access token
     */
    private void getCallHistory(final int limit, final String accessToken) {
        callHistoryApi.getCallHistory(limit, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<CallHistoryResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<CallHistoryResponse>>() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompleted() {
                        binding.dialogProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.dialogProgressBar.setVisibility(View.GONE);

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<CallHistoryResponse> callHistoryResponse) {
                        if (callHistoryResponse.getStatus() == Constants.Status.SUCCESS) {
                            callHistoryListViewModel.items.clear();

                            int count = 0;
                            for (final BimaCall booking : callHistoryResponse.getData().getCalls()) {
                                callHistoryItems.add(booking);

                                //show only the first three items in the beginning
                                if(count < MAX_NUM_CALLS_TO_SHOW) {
                                    callHistoryListViewModel.items.add(
                                            new CallHistoryItemViewModel(
                                                    booking,
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            // add action when user clicks on a call
                                                        }
                                                    })
                                    );
                                }
                                count++;
                            }

                            //Hide the "Show more" button if there are
                            //only 3 calls in the history
                            if(callHistoryItems.size() <= MAX_NUM_CALLS_TO_SHOW) {
                                binding.profileCallHistoryMore.setVisibility(View.GONE);
                            }
                        } else {
                            handleError(TAG, callHistoryResponse.getMessage());
                        }
                    }
                });
    }

    public static class CallHistoryListViewModel {
        public final ObservableList<CallHistoryItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.call_history_item_viewmodel, R.layout.item_call_history);
    }

}
