package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.remote.api.RateYourCallApi;
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.databinding.FragmentDoctorCallCompleteBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallAssigned;
import com.bima.dokterpribadimu.model.CallAssignedResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.TimeUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.RateYourCallDialog;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DoctorCallCompleteFragment extends BaseFragment {

    private static final String TAG = DoctorCallCompleteFragment.class.getSimpleName();

    private FragmentDoctorCallCompleteBinding binding;

    private RateYourCallDialog rateYourCallDialog;

    private int lastCallId = 0;

    @Inject
    RateYourCallApi rateYourCallApi;

    @Inject
    BookingApi bookingApi;

    public static DoctorCallCompleteFragment newInstance() {
        DoctorCallCompleteFragment fragment = new DoctorCallCompleteFragment();
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
        binding = FragmentDoctorCallCompleteBinding.inflate(inflater, container, false);

        initViews();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(canShowRateYourCall()) {
            if(rateYourCallDialog != null) {
                rateYourCallDialog.showDialog();
            }
        }

//        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_DOCTOR_CALL);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {

        if(rateYourCallDialog == null) {
            rateYourCallDialog = new RateYourCallDialog(getActivity());
        }
        rateYourCallDialog.setListener(new RateYourCallDialog.OnRateYourCallDialogClickListener() {
            @Override
            public void onClick(RateYourCallDialog dialog, float rating) {
                dismissRateYourCallDialog();

                Integer ratingInt = Integer.valueOf(Math.round(rating));
                rateCall(String.valueOf(lastCallId), ratingInt, UserProfileUtils.getUserProfile(getActivity()).getAccessToken());
            }
        });
    }

    /**
     * Check if the minimum time between calls has already passed and should show Rate Your Call dialog
     * @return boolean true if rate your call dialog can already be shown, boolean false if otherwise
     */
    private int getCallId() {
        String callId = StorageUtils.getString(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Constants.KEY_BOOK_CALL_ID_LAST_CALL,
                "0");

        //Just check if it is a valid integer
        int lastCallId;
        try {
            lastCallId = Integer.parseInt(callId);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Couldn't get the last call id (Invalid callId): " + callId
                    + " - error message: "
                    + e.getMessage());
            e.printStackTrace();
            lastCallId = 0;
        }

        return lastCallId;
    }

    /**
     * Check if the minimum time between calls has already passed and should show Rate Your Call dialog
     * @return boolean true if rate your call dialog can already be shown, boolean false if otherwise
     */
    private boolean canShowRateYourCall() {
        double lastBookedCallTimeMillis = StorageUtils.getDouble(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Constants.KEY_BOOK_CALL_TIME_MILLIS,
                -1);
        //if it's not found, it shouldn't show the rate call dialog
        if(lastBookedCallTimeMillis == -1) {
            return false;
        }

        String callId = StorageUtils.getString(
                DokterPribadimuApplication.getInstance().getApplicationContext(),
                Constants.KEY_BOOK_CALL_ID_LAST_CALL,
                "0");

        this.lastCallId = 0;
        try {
            this.lastCallId = Integer.parseInt(callId);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Couldn't get the last call id: " + callId
                    + " - error message: "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }

        boolean canShowRateYourCall = false;
        if(this.lastCallId != 0 && TimeUtils.hasOneHourPassed(lastBookedCallTimeMillis)) {
            canShowRateYourCall = true;
        }

        return canShowRateYourCall;
    }

    private void dismissRateYourCallDialog() {
        rateYourCallDialog.dismiss();
        rateYourCallDialog.clearReference();
        rateYourCallDialog = null;
    }


    /**
     * Rate the call received in the last hour
     * @param callId Id of the last call
     * @param rating Rating given by the user
     */
    private void rateCall(final String callId, final Integer rating, final String accessToken) {
        rateYourCallApi.rateCall(callId, rating, accessToken)
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
                    public void onNext(BaseResponse rateCallResponse) {
                        dismissProgressDialog();

                        if (rateCallResponse.getStatus() == Constants.Status.SUCCESS) {
                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.dialog_rate_your_call_success),
                                    getString(R.string.dialog_rate_your_call_success_message),
                                    getString(R.string.ok),
                                    null);
                            AnalyticsHelper.logViewDialogRatingEvent(EventConstants.DIALOG_DOCTOR_CALL_RATING_SUCCESS, rating);
                        } else {
                            handleError(TAG, rateCallResponse.getMessage());
                        }

                        IntentUtils.startHomeActivityOnTop(getActivity());
                    }
                });
    }



    /**
     * Get call assigned details
     * @param callId the id of the assigned call
     * @param accessToken user's access token
     */
    private void getCallAssignment(final String callId, final String accessToken) {
        bookingApi.getCallAssignment(callId, accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<CallAssignedResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<CallAssignedResponse>>() {

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
                    public void onNext(BaseResponse<CallAssignedResponse> callAssignedResponse) {
                        if (callAssignedResponse.getStatus() == Constants.Status.SUCCESS) {
                            if(callAssignedResponse.getData() != null &&
                                    callAssignedResponse.getData().getCallAssignment() != null) {
                                CallAssigned callAssigned = callAssignedResponse.getData().getCallAssignment();

                                //Update the layout with the information
                                updateViewsInfo(callAssigned);
                            }
                        } else {
                            handleError(TAG, callAssignedResponse.getMessage());
                        }
                    }
                });
    }

    private void updateViewsInfo(final CallAssigned callAssigned) {


    }

}
