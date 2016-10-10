package com.bima.dokterpribadimu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityCallHistoryDetailsBinding;
import com.bima.dokterpribadimu.model.CallHistoryDetails;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StringUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.viewmodel.CallDetailsItemViewModel;
import com.bima.dokterpribadimu.viewmodel.NewsItemViewModel;
import com.squareup.picasso.Picasso;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by gusta_000 on 8/10/2016.
 */
public class CallDetailsActivity extends BaseActivity {

    private static final String TAG = CallDetailsActivity.class.getSimpleName();

    private static final String CALL_HISTORY_DETAILS = "call_history_details";

    private ActivityCallHistoryDetailsBinding binding;

    private CallHistoryDetails callHistoryDetails;

    public static Intent create(Context context, CallHistoryDetails callHistoryDetails) {
        Intent intent = new Intent(context, CallDetailsActivity.class);
        intent.putExtra(CALL_HISTORY_DETAILS, GsonUtils.toJson(callHistoryDetails));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_history_details);
        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        callHistoryDetails = GsonUtils.fromJson(getIntent().getExtras().getString(CALL_HISTORY_DETAILS),
                                                    CallHistoryDetails.class);

        initViews();
    }

    private void initViews() {
        //Set action for back button
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Set call topic and subtopic texts
        if(callHistoryDetails.getBookingTopic() != null && !callHistoryDetails.getBookingTopic().isEmpty()) {
            binding.toolbarTitle.setText(callHistoryDetails.getBookingTopic());
        }
        if(callHistoryDetails.getBookingSubTopic() != null && !callHistoryDetails.getBookingSubTopic().isEmpty()) {
            binding.callSubtopic.setText(callHistoryDetails.getBookingSubTopic());
        }

        //Set call booking date
        if(callHistoryDetails.getBookingCreated() != null && !callHistoryDetails.getBookingCreated().isEmpty()) {
            binding.callDate.setText(callHistoryDetails.getBookingCreated());
        }
        else {
            binding.callDate.setVisibility(View.GONE);
        }

        //Load Doctor Picture in the CircleImageView
        if(callHistoryDetails.getDoctorPicture() != null && !callHistoryDetails.getDoctorPicture().isEmpty()) {
            try{
                Picasso.with(this).load(callHistoryDetails.getDoctorPicture())
                        .centerCrop()
                        .fit()
                        .error(R.drawable.ic_profile_picture)
                        .placeholder(R.drawable.ic_profile_picture)
                        .into(binding.callDoctorPictureImage);
            } catch(RuntimeException re) {
                re.printStackTrace();
            }
        }
        else {
            Picasso.with(this).load(R.drawable.ic_profile_picture)
                    .centerCrop()
                    .fit()
                    .into(binding.callDoctorPictureImage);
        }

        //Set doctor name
        binding.callDoctorName.setText(callHistoryDetails.getDoctorName());

        //Set action for when user clicks on the doctor's picture/name
        binding.callDetailsDoctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Redirect to doctor profile, if possible
                if(callHistoryDetails.getDoctorId() != null &&
                        !callHistoryDetails.getDoctorId().isEmpty()) {
                    IntentUtils.startDoctorProfileActivity(CallDetailsActivity.this, callHistoryDetails.getDoctorId());
                }
            }
        });

        //Set call summary content (doctor's notes)
        binding.callSummaryContent.setText(StringUtils.getStringOrDashIfNull(callHistoryDetails.getCallSummary()));

        //TODO: set prescription list

        //TODO: set immunisation list

        //Set user notes content
        binding.callMyNotesContent.setText(StringUtils.getStringOrDashIfNull(callHistoryDetails.getNotes()));

        //Set user's uploaded pictures, if any
        if(callHistoryDetails.getPictures() != null && !callHistoryDetails.getPictures().isEmpty()) {
            try {
                Picasso.with(this).load(callHistoryDetails.getPictures().get(0).getUrl())
                        .error(R.drawable.ic_img_placeholder)
                        .placeholder(R.drawable.ic_img_placeholder)
                        .into(binding.callMyPictures1);
                binding.callMyPictures1.setVisibility(View.VISIBLE);
            } catch(RuntimeException re) {
                re.printStackTrace();
            }

            //If there is more than one picture, load the second one
            if(callHistoryDetails.getPictures().size() > 1) {
                try {
                    Picasso.with(this).load(callHistoryDetails.getPictures().get(1).getUrl())
                            .error(R.drawable.ic_img_placeholder)
                            .placeholder(R.drawable.ic_img_placeholder)
                            .into(binding.callMyPictures2);
                    binding.callMyPictures2.setVisibility(View.VISIBLE);
                } catch(RuntimeException re) {
                    re.printStackTrace();
                }
            }
        }
        else {
            //If no pictures were uploaded, just show a simple "-"
            binding.callMyPicturesText.setText(getString(R.string.minus));
            binding.callMyPicturesText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class CallDetailsListViewModel {
        public final ObservableList<CallDetailsItemViewModel> items = new ObservableArrayList<>();
        public final ItemView prescriptionItemView = ItemView.of(BR.call_details_item_viewmodel, R.layout.item_call_details_prescription);
        public final ItemView immunisationItemView = ItemView.of(BR.call_details_item_viewmodel, R.layout.item_call_details_immunisation);
    }
}
