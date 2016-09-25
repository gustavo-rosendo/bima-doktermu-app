package com.bima.dokterpribadimu.view.activities;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
                if(validateFields()) {
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
                final String cancer = binding.currentHealthCancerSpinner.getSelectedItem().toString();
                if(position != adapterView.getCount()) {
                    addCancerTextView(cancer);

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
                final String allergy = binding.currentHealthAllergiesSpinner.getSelectedItem().toString();
                if(position != adapterView.getCount()) {
                    addAllergyTextView(allergy);
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
        List<String> auxArr = null;
        String aux = null;

        aux = healthCondition.getDiabetes();
        if(aux == null || aux.isEmpty()) {
            binding.currentHealthDiabetesText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthDiabetesText.setText(healthCondition.getDiabetes());
        }

        auxArr = healthCondition.getCancer();
        if(auxArr == null || auxArr.isEmpty()) {
            binding.currentHealthCancerText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthCancerText.setText("");

            for (int i = 0; i < auxArr.size(); i++) {
                String item = auxArr.get(i);
                if(item != null) {
                    binding.currentHealthCancerText.setText(binding.currentHealthCancerText.getText() + item);
                    //add a new line up to the last item
                    if(i != auxArr.size() - 1) {
                        binding.currentHealthCancerText.append("\n\n");
                    }
                }
            }
        }

        aux = healthCondition.getBloodPressure();
        if(aux == null || aux.isEmpty()) {
            binding.currentHealthBloodPressureText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthBloodPressureText.setText(healthCondition.getBloodPressure());
        }

        auxArr = healthCondition.getAllergies();
        if(auxArr == null || auxArr.isEmpty()) {
            binding.currentHealthAllergiesText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthAllergiesText.setText("");

            for (int i = 0; i < auxArr.size(); i++) {
                String item = auxArr.get(i);
                if(item != null) {
                    binding.currentHealthAllergiesText.setText(binding.currentHealthAllergiesText.getText() + item);
                    //add a new line up to the last item
                    if(i != auxArr.size() - 1) {
                        binding.currentHealthAllergiesText.append("\n\n");
                    }
                }
            }
        }

        aux = healthCondition.getAsthma();
        if(aux == null || aux.isEmpty()) {
            binding.currentHealthAsthmaText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthAsthmaText.setText(healthCondition.getAsthma());
        }

        aux = healthCondition.getPregnant();
        if(aux == null || aux.isEmpty()) {
            binding.currentHealthPregnantText.setText(getResources().getString(R.string.minus));
        } else {
            binding.currentHealthPregnantText.setText(healthCondition.getPregnant());
        }
    }

    private void updateEditViews(HealthCondition healthCondition) {
        List<String> auxArr = null;
        String aux = null;

        aux = healthCondition.getDiabetes();
        populateStringSpinner(binding.currentHealthDiabetesSpinner, aux);

        auxArr = healthCondition.getCancer();
        binding.currentHealthCancerSpinner.setSelection(binding.currentHealthCancerSpinner.getCount());
        if(auxArr != null && !auxArr.isEmpty()) {
            for (int i = 0; i < auxArr.size(); i++) {
                String item = auxArr.get(i);
                if(item != null) {
                    addCancerTextView(item);
                }
            }
        }

        aux = healthCondition.getBloodPressure();
        populateStringSpinner(binding.currentHealthBloodPressureSpinner, aux);

        auxArr = healthCondition.getAllergies();
        binding.currentHealthAllergiesSpinner.setSelection(binding.currentHealthAllergiesSpinner.getCount());
        if(auxArr != null && !auxArr.isEmpty()) {
            for (int i = 0; i < auxArr.size(); i++) {
                String item = auxArr.get(i);
                if(item != null) {
                    addAllergyTextView(item);
                }
            }
        }

        aux = healthCondition.getAsthma();
        populateStringSpinner(binding.currentHealthAsthmaSpinner, aux);

        aux = healthCondition.getPregnant();
        populateStringSpinner(binding.currentHealthPregnantSpinner, aux);
    }

    private void populateStringSpinner(Spinner spinner, String item) {
        spinner.setSelection(spinner.getCount());
        if(item != null && !item.isEmpty()) {
            for (int i = 0; i < spinner.getCount(); i++) {
                if (item.equals(spinner.getItemAtPosition(i).toString())) {
                    spinner.setSelection(i);
                }
            }
        }
    }

    private void addCancerTextView(final String cancer) {
        if(cancerArrayList.contains(cancer)) {
            Toast.makeText(this,
                    R.string.current_health_already_selected,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.current_health_cancer_edit_layout);

            final RelativeLayout relativeLayout = new RelativeLayout(CurrentHealthActivity.this);
            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            p1.addRule(RelativeLayout.ABOVE, R.id.current_health_cancer_spinner);
            relativeLayout.setLayoutParams(p1);

            final TextView cancerTextView = new TextView(CurrentHealthActivity.this);
            cancerTextView.setText(cancer);
            RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            cancerTextView.setLayoutParams(p2);
            cancerTextView.setTextColor(Color.BLACK);
            cancerTextView.setTextSize(getResources().getDimension(R.dimen.generic_super_small_text_size));
            int padding = (int) getResources().getDimension(R.dimen.generic_large_padding);
            cancerTextView.setPadding(padding, padding, padding, padding);
            relativeLayout.addView(cancerTextView, 0);

            final ImageButton cancerBtn = new ImageButton(CurrentHealthActivity.this);
            cancerBtn.setImageResource(R.drawable.ic_close_bima_blue);
            cancerBtn.setBackgroundColor(0);
            RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            p3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cancerBtn.setLayoutParams(p3);

            cancerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayout.removeView(cancerTextView);
                    relativeLayout.removeView(cancerBtn);
                    linearLayout.removeView(relativeLayout);
                    cancerArrayList.remove(cancer);
                }
            });

            relativeLayout.addView(cancerBtn);
            linearLayout.addView(relativeLayout, 0);

            //Add selected value to the list
            cancerArrayList.add(cancer);
        }
    }

    private void addAllergyTextView(final String allergy) {
        if(allergiesArrayList.contains(allergy)) {
            Toast.makeText(this,
                    R.string.current_health_already_selected,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.current_health_allergies_edit_layout);

            final RelativeLayout relativeLayout = new RelativeLayout(CurrentHealthActivity.this);
            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(p1);

            final TextView allergyTextView = new TextView(CurrentHealthActivity.this);
            allergyTextView.setText(allergy);
            RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            allergyTextView.setLayoutParams(p2);
            allergyTextView.setTextColor(Color.BLACK);
            allergyTextView.setTextSize(getResources().getDimension(R.dimen.generic_super_small_text_size));
            int padding = (int) getResources().getDimension(R.dimen.generic_large_padding);
            allergyTextView.setPadding(padding, padding, padding, padding);
            relativeLayout.addView(allergyTextView, 0);

            final ImageButton allergyBtn = new ImageButton(CurrentHealthActivity.this);
            allergyBtn.setImageResource(R.drawable.ic_close_bima_blue);
            allergyBtn.setBackgroundColor(0);
            RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            p3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            allergyBtn.setLayoutParams(p3);

            allergyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    relativeLayout.removeView(allergyTextView);
                    relativeLayout.removeView(allergyBtn);
                    linearLayout.removeView(relativeLayout);
                    allergiesArrayList.remove(allergy);
                }
            });

            relativeLayout.addView(allergyBtn);

            linearLayout.addView(relativeLayout, 0);

            //Add selected value to the list
            allergiesArrayList.add(allergy);
        }
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
                                updateEditViews(healthCondition);
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

    boolean validateFields() {
        boolean valid = true;
        String validationMsg = "";
        String diabetes = binding.currentHealthDiabetesSpinner.getSelectedItem().toString();
        String bloodPressure = binding.currentHealthBloodPressureSpinner.getSelectedItem().toString();
        String asthma = binding.currentHealthAsthmaSpinner.getSelectedItem().toString();
        String pregnant = binding.currentHealthPregnantSpinner.getSelectedItem().toString();

        String[] yesNoArr = getResources().getStringArray(R.array.current_health_yes_no_arrays);
        String[] bloodPressureArr = getResources().getStringArray(R.array.blood_pressure_arrays);

        //check if the text showing is still 'ADD INFO'
        if(diabetes.equals(yesNoArr[yesNoArr.length-1])) {
            Toast.makeText(this,
                    R.string.current_health_invalid_diabetes,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(bloodPressure.equals(bloodPressureArr[bloodPressureArr.length-1])) {
            Toast.makeText(this,
                    R.string.current_health_invalid_blood_pressure,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(asthma.equals(yesNoArr[yesNoArr.length-1])) {
            Toast.makeText(this,
                    R.string.current_health_invalid_asthma,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else if(pregnant.equals(yesNoArr[yesNoArr.length-1])) {
            Toast.makeText(this,
                    R.string.current_health_invalid_pregnant,
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}
