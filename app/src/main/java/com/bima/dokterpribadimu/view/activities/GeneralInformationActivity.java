package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.ProfileApi;
import com.bima.dokterpribadimu.databinding.ActivityGeneralInformationBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Information;
import com.bima.dokterpribadimu.model.InformationResponse;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.StringUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GeneralInformationActivity extends BaseActivity {

    private static final String TAG = GeneralInformationActivity.class.getSimpleName();
    private ActivityGeneralInformationBinding binding;

    @Inject
    ProfileApi profileApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_general_information);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.generalEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfoLayout(View.GONE);
                updateEditLayout(View.VISIBLE);
            }
        });

        binding.generalSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfoLayout(View.VISIBLE);
                updateEditLayout(View.GONE);
            }
        });

        getHealthInformation(UserProfileUtils.getUserProfile(this).getAccessToken());
    }

    private void updateInfoLayout(int visibility) {
        binding.generalHeightText.setVisibility(visibility);
        binding.generalWeightText.setVisibility(visibility);
        binding.generalReligionText.setVisibility(visibility);
        binding.generalBloodTypeText.setVisibility(visibility);
        binding.generalSmokerText.setVisibility(visibility);
        binding.generalPhysicalExerciseText.setVisibility(visibility);
        binding.generalHealthInsuranceText.setVisibility(visibility);
        binding.generalEditButton.setVisibility(visibility);
    }

    private void updateEditLayout(int visibility) {
        binding.generalHeightEditLayout.setVisibility(visibility);
        binding.generalWeightEditLayout.setVisibility(visibility);
        binding.generalReligionEditLayout.setVisibility(visibility);
        binding.generalBloodTypeEditLayout.setVisibility(visibility);
        binding.generalSmokerEditLayout.setVisibility(visibility);
        binding.generalPhysicalExerciseEditLayout.setVisibility(visibility);
        binding.generalHealthInsuranceEditLayout.setVisibility(visibility);
        binding.generalSaveButton.setVisibility(visibility);
    }

    /**
     * Get user's health info
     * @param accessToken user's access token
     */
    private void getHealthInformation(final String accessToken) {
        profileApi.getHealthInformation(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<InformationResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<InformationResponse>>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        handleError(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResponse<InformationResponse> informationResponse) {
                        if (informationResponse.getStatus() == Constants.Status.SUCCESS) {
                            Information information = informationResponse.getData().getInformation();
                            if (information != null) {
                                updateViews(information);
                            }
                        } else {
                            handleError(TAG, informationResponse.getMessage());
                        }
                    }
                });
    }

    private void updateViews(Information information) {
        binding.generalHeightText.setText(StringUtils.getStringOrDashIfNull(information.getHeight()));
        binding.generalWeightText.setText(StringUtils.getStringOrDashIfNull(information.getWeight()));
        binding.generalReligionText.setText(StringUtils.getStringOrDashIfNull(information.getReligion()));
        binding.generalBloodTypeText.setText(StringUtils.getStringOrDashIfNull(information.getBloodType()));
        binding.generalSmokerText.setText(StringUtils.getStringOrDashIfNull(information.getSmoker()));
        binding.generalPhysicalExerciseText.setText(StringUtils.getStringOrDashIfNull(information.getPhysicalExercise()));
        binding.generalHealthInsuranceText.setText(StringUtils.getStringOrDashIfNull(information.getHealthInsurance()));
    }
}
