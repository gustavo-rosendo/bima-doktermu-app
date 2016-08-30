package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.ProfileApi;
import com.bima.dokterpribadimu.databinding.ActivityCurrentHealthBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.HealthCondition;
import com.bima.dokterpribadimu.model.HealthConditionResponse;
import com.bima.dokterpribadimu.model.Information;
import com.bima.dokterpribadimu.model.InformationResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CurrentHealthActivity extends BaseActivity {

    private static final String TAG = CurrentHealthActivity.class.getSimpleName();
    private ActivityCurrentHealthBinding binding;

    @Inject
    ProfileApi profileApi;

    private List<String> cancerArrayList;
    private List<String> allergiesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_health);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        cancerArrayList = new ArrayList<>();
        allergiesArrayList = new ArrayList<>();

        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.currentHealthEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInfoLayout(View.GONE);
                updateEditLayout(View.VISIBLE);
            }
        });

        binding.currentHealthSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diabetes = binding.currentHealthDiabetesSpinner.getSelectedItem().toString();
                String bloodPressure = binding.currentHealthBloodPressureSpinner.getSelectedItem().toString();
                String asthma = binding.currentHealthAsthmaSpinner.getSelectedItem().toString();
                String pregnant = binding.currentHealthPregnantSpinner.getSelectedItem().toString();

                updateHealthCondition((diabetes != "" && diabetes != "-")? diabetes : null,
                        (cancerArrayList.isEmpty())? null : cancerArrayList,
                        (bloodPressure != "" && bloodPressure != "-")? bloodPressure : null,
                        (allergiesArrayList.isEmpty())? null : allergiesArrayList,
                        (asthma != "" && asthma != "-")? asthma : null,
                        (pregnant != "" && pregnant != "-")? pregnant : null,
                        UserProfileUtils.getUserProfile(CurrentHealthActivity.this).getAccessToken());

                updateInfoLayout(View.VISIBLE);
                updateEditLayout(View.GONE);
            }
        });

        ArrayAdapter<String> spinnerDiabetesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.current_health_yes_no_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerDiabetesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthDiabetesSpinner.setAdapter(spinnerDiabetesAdapter);
        binding.currentHealthDiabetesSpinner.setSelection(spinnerDiabetesAdapter.getCount());

        ArrayAdapter<String> spinnerCancerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.cancer_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerCancerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthCancerSpinner.setAdapter(spinnerCancerAdapter);
        binding.currentHealthCancerSpinner.setSelection(spinnerCancerAdapter.getCount());
        binding.currentHealthCancerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String cancer = binding.currentHealthCancerSpinner.getSelectedItem().toString();
                if(position != adapterView.getCount()) {
                    TextView cancerTextView = new TextView(CurrentHealthActivity.this);
                    cancerTextView.setText(cancer);
                    cancerTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    int padding = (int) getResources().getDimension(R.dimen.generic_large_padding);
                    cancerTextView.setPadding(padding, padding, padding, padding);

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.current_health_cancer_edit_layout);
                    linearLayout.addView(cancerTextView, 0);

                    //Add selected value to the list
                    cancerArrayList.add(cancer);
                    //Reset the selection
                    binding.currentHealthCancerSpinner.setSelection(adapterView.getCount());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> spinnerBloodPressureAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.blood_pressure_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerBloodPressureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthBloodPressureSpinner.setAdapter(spinnerBloodPressureAdapter);
        binding.currentHealthBloodPressureSpinner.setSelection(spinnerBloodPressureAdapter.getCount());

        ArrayAdapter<String> spinnerAllergiesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.allergies_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerAllergiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthAllergiesSpinner.setAdapter(spinnerAllergiesAdapter);
        binding.currentHealthAllergiesSpinner.setSelection(spinnerAllergiesAdapter.getCount());

        binding.currentHealthAllergiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String allergy = binding.currentHealthAllergiesSpinner.getSelectedItem().toString();
                if(position != adapterView.getCount()) {
                    TextView allergyTextView = new TextView(CurrentHealthActivity.this);
                    allergyTextView.setText(allergy);
                    allergyTextView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    int padding = (int) getResources().getDimension(R.dimen.generic_large_padding);
                    allergyTextView.setPadding(padding, padding, padding, padding);

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.current_health_allergies_edit_layout);
                    linearLayout.addView(allergyTextView, 0);

                    //Add selected value to the list
                    allergiesArrayList.add(allergy);
                    //Reset the selection
                    binding.currentHealthAllergiesSpinner.setSelection(adapterView.getCount());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> spinnerAsthmaAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.current_health_yes_no_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerAsthmaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthAsthmaSpinner.setAdapter(spinnerAsthmaAdapter);
        binding.currentHealthAsthmaSpinner.setSelection(spinnerAsthmaAdapter.getCount());

        ArrayAdapter<String> spinnerPregnantAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.current_health_yes_no_arrays)) {
            @Override
            public int getCount() {
                return super.getCount() - 1; // don't display last item. It is used as hint.
            }
        };
        spinnerPregnantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currentHealthPregnantSpinner.setAdapter(spinnerPregnantAdapter);
        binding.currentHealthPregnantSpinner.setSelection(spinnerPregnantAdapter.getCount());

        getHealthCondition(UserProfileUtils.getUserProfile(this).getAccessToken());
    }

    private void updateInfoLayout(int visibility) {
        binding.currentHealthDiabetesText.setVisibility(visibility);
        binding.currentHealthCancerText.setVisibility(visibility);
        binding.currentHealthBloodPressureText.setVisibility(visibility);
        binding.currentHealthAllergiesText.setVisibility(visibility);
        binding.currentHealthAsthmaText.setVisibility(visibility);
        binding.currentHealthPregnantText.setVisibility(visibility);
        binding.currentHealthEditButton.setVisibility(visibility);
    }

    private void updateEditLayout(int visibility) {
        binding.currentHealthDiabetesEditLayout.setVisibility(visibility);
        binding.currentHealthCancerEditLayout.setVisibility(visibility);
        binding.currentHealthBloodPressureEditLayout.setVisibility(visibility);
        binding.currentHealthAllergiesEditLayout.setVisibility(visibility);
        binding.currentHealthAsthmaEditLayout.setVisibility(visibility);
        binding.currentHealthPregnantEditLayout.setVisibility(visibility);
        binding.currentHealthSaveButton.setVisibility(visibility);
    }

    private void updateInfoViews(HealthCondition healthCondition) {
        binding.currentHealthDiabetesText.setText(healthCondition.getDiabetes());
        binding.currentHealthCancerText.setText(healthCondition.getCancer().toString());
        binding.currentHealthBloodPressureText.setText(healthCondition.getBloodPressure());
        binding.currentHealthAllergiesText.setText(healthCondition.getAllergies().toString());
        binding.currentHealthAsthmaText.setText(healthCondition.getAsthma());
        binding.currentHealthPregnantText.setText(healthCondition.getPregnant());
    }

    /**
     * Get user's health condition
     * @param accessToken user's access token
     */
    private void getHealthCondition(final String accessToken) {
        profileApi.getHealthCondition(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<HealthConditionResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<HealthConditionResponse>>() {

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
                    public void onNext(BaseResponse<HealthConditionResponse> healthConditionResponse) {
                        if (healthConditionResponse.getStatus() == Constants.Status.SUCCESS) {
                            HealthCondition healthCondition = healthConditionResponse.getData().getHealthCondition();
                            if (healthCondition != null) {
                                updateInfoViews(healthCondition);
                            }
                        } else {
                            handleError(TAG, healthConditionResponse.getMessage());
                        }
                    }
                });
    }


    /**
     * Update user's health condition
     */
    private void updateHealthCondition(final String diabetes, final List<String> cancer,
                                       final String bloodPressure, final List<String> allergies,
                                       final String asthma, final String pregnant, final String accessToken) {
        profileApi.updateHealthCondition(diabetes, cancer, bloodPressure, allergies,
                                        asthma, pregnant, accessToken)
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
                            HealthCondition healthCondition = new HealthCondition();
                            healthCondition.setDiabetes(diabetes);
                            healthCondition.setCancer(cancer);
                            healthCondition.setBloodPressure(bloodPressure);
                            healthCondition.setAllergies(allergies);
                            healthCondition.setAsthma(asthma);
                            healthCondition.setPregnant(pregnant);
                            //Update the "view-mode" of the screen with the new healthCondition
                            updateInfoViews(healthCondition);

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

}
