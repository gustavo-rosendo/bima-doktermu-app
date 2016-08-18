package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityCurrentHealthBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;

public class CurrentHealthActivity extends BaseActivity {

    private ActivityCurrentHealthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_health);

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
                updateInfoLayout(View.VISIBLE);
                updateEditLayout(View.GONE);
            }
        });
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
}
