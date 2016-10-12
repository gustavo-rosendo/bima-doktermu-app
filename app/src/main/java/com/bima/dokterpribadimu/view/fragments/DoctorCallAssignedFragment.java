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
import com.bima.dokterpribadimu.data.remote.api.BookingApi;
import com.bima.dokterpribadimu.databinding.FragmentDoctorCallAssignedBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CallAssigned;
import com.bima.dokterpribadimu.model.CallAssignedResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.view.components.CancelCallModalDialog;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DoctorCallAssignedFragment extends BaseFragment {

    private static final String TAG = DoctorCallAssignedFragment.class.getSimpleName();

    private FragmentDoctorCallAssignedBinding binding;

    private CancelCallModalDialog cancelCallModalDialog;

    @Inject
    BookingApi bookingApi;

    public static DoctorCallAssignedFragment newInstance() {
        DoctorCallAssignedFragment fragment = new DoctorCallAssignedFragment();
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
        binding = FragmentDoctorCallAssignedBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

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

        binding.bookCallAssignedCancelCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_BOOK_CALL_ASSIGNED_CANCEL_CALL);

                if(cancelCallModalDialog == null) {
                    cancelCallModalDialog = new CancelCallModalDialog(getContext());
                }

                cancelCallModalDialog.setListener(new CancelCallModalDialog.OnCancelCallModalDialogClickListener() {
                    @Override
                    public void onClick(CancelCallModalDialog dialog) {
                        int lastCallId = getCallId();
                        if(lastCallId > 0) {
                            //Call backend to call the booking
                            cancelCall(String.valueOf(lastCallId),
                                    UserProfileUtils.getUserProfile(getContext()).getAccessToken());
                            dismissCancelCallDialog();
                        }
                        else {
                            dismissCancelCallDialog();
                            //Invalid call id, show default error message
                            handleError(TAG, null);
                        }
                    }
                });

                cancelCallModalDialog.showDialog();
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
     * Cancel the booked call
     * @param callId Id of the last call
     * @param accessToken Access token of the user
     */
    private void cancelCall(final String callId, final String accessToken) {
        bookingApi.cancelCall(callId, accessToken)
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
                    public void onNext(BaseResponse cancelCallResponse) {
                        dismissProgressDialog();

                        if (cancelCallResponse.getStatus() == Constants.Status.SUCCESS) {
                            showSuccessDialog(
                                    R.drawable.ic_smiley,
                                    getString(R.string.book_call_cancelled),
                                    getString(R.string.book_call_cancelled_message),
                                    getString(R.string.ok),
                                    null);

                            //If the call is succesfully canceled, just go back to Doctor On Call screen
                            IntentUtils.startDoctorCallActivityOnTop(
                                    DokterPribadimuApplication.getInstance().getApplicationContext());
                        } else {
                            handleError(TAG, cancelCallResponse.getMessage());
                        }
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
        if(callAssigned == null) return;

        if(callAssigned.getDoctorName() != null && !callAssigned.getDoctorName().isEmpty()) {
            binding.bookCallAssignedDoctor.setText(callAssigned.getDoctorName());
        }

        if(callAssigned.getTimeEstimation() != null && !callAssigned.getTimeEstimation().isEmpty()) {
            binding.bookCallAssignedTime.setText(callAssigned.getTimeEstimation());
        }

        if(callAssigned.getDoctorPicture() != null && !callAssigned.getDoctorPicture().isEmpty()) {
            try{
                Picasso.with(getContext()).load(callAssigned.getDoctorPicture())
                        .centerCrop()
                        .fit()
                        .error(R.drawable.ic_profile_picture)
                        .placeholder(R.drawable.ic_profile_picture)
                        .into(binding.doctorCallIcon);
            } catch(RuntimeException re) {
                re.printStackTrace();
            }
        }

        //Set action for when user clicks on the doctor's picture
        binding.doctorCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to doctor profile, if possible
                if(callAssigned.getDoctorId() != null &&
                        !callAssigned.getDoctorId().isEmpty()) {
                    IntentUtils.startDoctorProfileActivity(getContext(), callAssigned.getDoctorId());
                }
            }
        });

        //Set action for when user clicks on the doctor's name
        binding.bookCallAssignedDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to doctor profile, if possible
                if(callAssigned.getDoctorId() != null &&
                        !callAssigned.getDoctorId().isEmpty()) {
                    IntentUtils.startDoctorProfileActivity(getContext(), callAssigned.getDoctorId());
                }
            }
        });
    }

    private void dismissCancelCallDialog() {
        cancelCallModalDialog.dismiss();
        cancelCallModalDialog.clearReference();
        cancelCallModalDialog = null;
    }

}
