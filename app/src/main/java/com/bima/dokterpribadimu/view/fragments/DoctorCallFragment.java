package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.BuildConfig;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.data.servertime.ServerTimeClient;
import com.bima.dokterpribadimu.data.servertime.SntpClient;
import com.bima.dokterpribadimu.databinding.FragmentDoctorCallBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DoctorCallFragment extends BaseFragment {

    private static final String TAG = DoctorCallFragment.class.getSimpleName();

    private static final String HOUR_PATTERN = "HH";
    private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat(HOUR_PATTERN);
    private static final String TIME_ZONE = "GMT+7";

    private static final int NIGHT_HOUR_LIMIT = 10;
    private static final int NIGHT_HOUR_LIMIT_24_FORMAT = 22;
    private static final int MORNING_HOUR_LIMIT = 6;

    @Inject
    BillingClient billingClient;

    @Inject
    ServerTimeClient serverTimeClient;

    private FragmentDoctorCallBinding binding;

    private SntpClient sntpClient;


    public static DoctorCallFragment newInstance() {
        DoctorCallFragment fragment = new DoctorCallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);

        HOUR_FORMAT.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoctorCallBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        initBillingClient();

        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_DOCTOR_CALL);

    }

    @Override
    public void onPause() {
        billingClient.release();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        billingClient.release();

        super.onDestroy();
    }

    private void initBillingClient() {
        billingClient.setBillingInitializationListener(new BillingInitializationListener() {
            @Override
            public void onSuccess() {
                billingClient.queryInventoryAsync();
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.setQueryInventoryListener(new QueryInventoryListener() {
            @Override
            public void onSuccess(boolean isSubscribed) {
                StorageUtils.putBoolean(
                        getActivity(),
                        Constants.KEY_USER_SUBSCIPTION,
                        isSubscribed);
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.init(getActivity());
    }

    private void initViews() {
        if (billingClient.isSubscribedToDokterPribadiKu() || BuildConfig.DEBUG) {
            binding.doctorCallInfoText.setText(getResources().getString(R.string.doctor_on_call_info));
            binding.bookCallButton.setText(getResources().getString(R.string.doctor_on_call_book_a_call));
        }
        else {
            binding.doctorCallInfoText.setText(getResources().getString(R.string.doctor_on_call_subscription_info));
            binding.bookCallButton.setText(getResources().getString(R.string.doctor_on_call_subscribe));
        }

        binding.bookCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billingClient.isSubscribedToDokterPribadiKu() || BuildConfig.DEBUG) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_BOOK_SCREEN_DOCTOR_CALL);
                    checkServerTime();
                }
                else {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_SUBSCRIBE_SCREEN_DOCTOR_CALL);
                    startSubscriptionActivity();
                }
            }
        });

    }

    private void checkServerTime() {
        serverTimeClient.getSntpClient()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SntpClient>bindToLifecycle())
                .subscribe(new Subscriber<SntpClient>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();

                        processBookCall();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(SntpClient sntpClient) {
                        DoctorCallFragment.this.sntpClient = sntpClient;
                    }
                });
    }

    private void processBookCall() {
        if (!isValidBookCallTime()) {
            showLateDialog(getString(R.string.dialog_take_me_home), null);
        } else if(!validatePhoneNumber()) {
            // The toast will show so we do nothing else
        } else {
            startBookCallActivity();
        }
    }

    /**
     * Validate book a call phone number. A phone number is valid if it has at least 11 digits, like in: 02742112462
     * @return boolean true if phone number is valid, boolean false if otherwise
     */
    private boolean validatePhoneNumber() {
        boolean isValid = false;

        if(UserProfileUtils.getUserProfile(getActivity()).getMsisdn().length() < Constants.PHONE_NUMBER_MINIMAL_DIGITS) {
            Toast.makeText(
                    getActivity(),
                    getString(R.string.invalid_call_phone_number_message),
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            isValid = true;
        }

        return isValid;
    }
    private boolean isValidBookCallTime() {
        if(BuildConfig.DEBUG) return true;

        boolean isValidTime;

        if (sntpClient != null) {
            long time = sntpClient.getNtpTime();
            Date date = new Date(time);
            int hour = Integer.parseInt(HOUR_FORMAT.format(date));

            Log.d("Server Time", date.toString());
            Log.d("GMT+7 Hour", HOUR_FORMAT.format(date));

            isValidTime = !(hour >= NIGHT_HOUR_LIMIT_24_FORMAT
                    || hour < MORNING_HOUR_LIMIT);
        } else {
            int hour = TimeUtils.getCurrentTimeHour();
            int ampm = TimeUtils.getCurrentTimeAmPm();

            isValidTime = !((ampm == Calendar.PM && hour >= NIGHT_HOUR_LIMIT)
                    || (ampm == Calendar.AM && hour < MORNING_HOUR_LIMIT));
        }

        return isValidTime;
    }



}