package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

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
                String height = binding.generalHeightEditText.getText().toString().trim();
                String weight = binding.generalWeightEditText.getText().toString().trim();
                String religion = binding.generalReligionSpinner.getSelectedItem().toString();
                String bloodType = binding.generalBloodTypeSpinner.getSelectedItem().toString();
                String smoker = binding.generalSmokerSpinner.getSelectedItem().toString();
                String physicalExercise = binding.generalPhysicalExerciseSpinner.getSelectedItem().toString();
                String healthInsurance = binding.generalHealthInsuranceSpinner.getSelectedItem().toString();

                updateHealthInformation(
                        (height != "")? height : null,
                        (weight != "")? weight : null,
                        (religion != "" && religion != "-")? religion : null,
                        (bloodType != "" && bloodType != "-")? bloodType : null,
                        (smoker != "" && smoker != "-")? smoker : null,
                        (physicalExercise != "" && physicalExercise != "-")? physicalExercise : null,
                        (healthInsurance != "" && healthInsurance != "-")? healthInsurance : null,
                        UserProfileUtils.getUserProfile(GeneralInformationActivity.this).getAccessToken()
                );

                updateInfoLayout(View.VISIBLE);
                updateEditLayout(View.GONE);
            }
        });

        ArrayAdapter<String> spinnerReligionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.religion_arrays)){
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerReligionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.generalReligionSpinner.setAdapter(spinnerReligionAdapter);
        binding.generalReligionSpinner.setSelection(spinnerReligionAdapter.getCount());

        ArrayAdapter<String> spinnerBloodTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.blood_type_arrays)){
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerBloodTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.generalBloodTypeSpinner.setAdapter(spinnerBloodTypeAdapter);
        binding.generalBloodTypeSpinner.setSelection(spinnerBloodTypeAdapter.getCount());

        ArrayAdapter<String> spinnerSmokerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.question_arrays)){
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerSmokerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.generalSmokerSpinner.setAdapter(spinnerSmokerAdapter);
        binding.generalSmokerSpinner.setSelection(spinnerSmokerAdapter.getCount());

        ArrayAdapter<String> spinnerPhysicalExerciseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.physical_exercise_arrays)){
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerPhysicalExerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.generalPhysicalExerciseSpinner.setAdapter(spinnerPhysicalExerciseAdapter);
        binding.generalPhysicalExerciseSpinner.setSelection(spinnerPhysicalExerciseAdapter.getCount());

        ArrayAdapter<String> spinnerHealthInsuranceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.question_arrays)){
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerHealthInsuranceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.generalHealthInsuranceSpinner.setAdapter(spinnerHealthInsuranceAdapter);
        binding.generalHealthInsuranceSpinner.setSelection(spinnerHealthInsuranceAdapter.getCount());

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

        if(visibility == View.VISIBLE) {
            updateEditViews();
        }
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

    /**
     * Get user's health info
     * @param accessToken user's access token
     */
    private void updateHealthInformation(final String height, final String weight, final String religion,
                                         final String bloodType, final String smoker, final String physicalExercise,
                                         final String healthInsurance, final String accessToken) {
        profileApi.updateHealthInformation(height, weight, religion, bloodType,
                                        smoker, physicalExercise, healthInsurance, accessToken)
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
                            Information information = new Information();
                            information.setHeight(height);
                            information.setWeight(weight);
                            information.setReligion(religion);
                            information.setBloodType(bloodType);
                            information.setSmoker(smoker);
                            information.setPhysicalExercise(physicalExercise);
                            information.setHealthInsurance(healthInsurance);
                            //Update the "view-mode" of the screen with the new information
                            updateViews(information);

                            showSuccessDialog(
                                    R.drawable.ic_thumb_up,
                                    getString(R.string.dialog_success),
                                    getString(R.string.dialog_profile_saved_success_message),
                                    getString(R.string.ok),
                                    null);
                        } else {
                            handleError(TAG, response.getMessage());
                        }
                    }
                });
    }

    private void updateViews(Information information) {
        binding.generalHeightText.setText(StringUtils.getStringOrDashIfNull(information.getHeight()) + " " + getResources().getString(R.string.cm));
        binding.generalWeightText.setText(StringUtils.getStringOrDashIfNull(information.getWeight()) + " " + getResources().getString(R.string.kg));
        binding.generalReligionText.setText(StringUtils.getStringOrDashIfNull(information.getReligion()));
        binding.generalBloodTypeText.setText(StringUtils.getStringOrDashIfNull(information.getBloodType()));
        binding.generalSmokerText.setText(StringUtils.getStringOrDashIfNull(information.getSmoker()));
        binding.generalPhysicalExerciseText.setText(StringUtils.getStringOrDashIfNull(information.getPhysicalExercise()));
        binding.generalHealthInsuranceText.setText(StringUtils.getStringOrDashIfNull(information.getHealthInsurance()));
    }

    private void updateEditViews() {
        String height = binding.generalHeightText.getText().toString();
        String weight = binding.generalWeightText.getText().toString();
        String religion = binding.generalReligionText.getText().toString();
        String bloodType = binding.generalBloodTypeText.getText().toString();
        String smoker = binding.generalSmokerText.getText().toString();
        String physicalExercise = binding.generalPhysicalExerciseText.getText().toString();
        String healthInsurance = binding.generalHealthInsuranceText.getText().toString();

        binding.generalHeightEditText.setText(height);
        binding.generalWeightEditText.setText(weight);

        String[] religionArr = getResources().getStringArray(R.array.religion_arrays);
        for(int i = 0; i < religionArr.length; i++) {
            if(religionArr[i].equalsIgnoreCase(religion) && i <= binding.generalReligionSpinner.getCount()) {
                binding.generalReligionSpinner.setSelection(i);
                break;
            }
        }

        String[] bloodTypeArr = getResources().getStringArray(R.array.blood_type_arrays);
        for(int i = 0; i < bloodTypeArr.length; i++) {
            if(bloodTypeArr[i].equalsIgnoreCase(bloodType) && i <= binding.generalBloodTypeSpinner.getCount()) {
                binding.generalBloodTypeSpinner.setSelection(i);
                break;
            }
        }

        String[] smokerArr = getResources().getStringArray(R.array.question_arrays);
        for(int i = 0; i < smokerArr.length; i++) {
            if(smokerArr[i].equalsIgnoreCase(smoker) && i <= binding.generalSmokerSpinner.getCount()) {
                binding.generalSmokerSpinner.setSelection(i);
                break;
            }
        }

        String[] physicalExerciseArr = getResources().getStringArray(R.array.physical_exercise_arrays);
        for(int i = 0; i < smokerArr.length; i++) {
            if(physicalExerciseArr[i].equalsIgnoreCase(physicalExercise) && i <= binding.generalPhysicalExerciseSpinner.getCount()) {
                binding.generalPhysicalExerciseSpinner.setSelection(i);
                break;
            }
        }

        String[] healthInsuranceArr = getResources().getStringArray(R.array.question_arrays);
        for(int i = 0; i < healthInsuranceArr.length; i++) {
            if(healthInsuranceArr[i].equalsIgnoreCase(healthInsurance) && i <= binding.generalHealthInsuranceSpinner.getCount()) {
                binding.generalHealthInsuranceSpinner.setSelection(i);
                break;
            }
        }
    }
}
