package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.databinding.FragmentDoctorCallBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;

import java.util.Calendar;

import javax.inject.Inject;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DoctorCallFragment extends BaseFragment {

    private static final int NIGHT_HOUR_LIMIT = 10;
    private static final int MORNING_HOUR_LIMIT = 6;

    @Inject
    BillingClient billingClient;

    private FragmentDoctorCallBinding binding;

    public static DoctorCallFragment newInstance() {
        DoctorCallFragment fragment = new DoctorCallFragment();
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
        binding = FragmentDoctorCallBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        initBillingClient();
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
        if (billingClient.isSubscribedToDokterPribadiKu()) {
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
                int hour = TimeUtils.getCurrentTimeHour();
                int ampm = TimeUtils.getCurrentTimeAmPm();
                if (billingClient.isSubscribedToDokterPribadiKu()) {
                    if ((ampm == Calendar.PM && hour >= NIGHT_HOUR_LIMIT) ||
                            (ampm == Calendar.AM && hour < MORNING_HOUR_LIMIT)) {
                        showLateDialog(getString(R.string.dialog_take_me_home), null);
                    } else {
                        startBookCallActivity();
                    }
                }
                else {
                    startSubscriptionActivity();
                }
            }
        });
    }

}
