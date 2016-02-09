package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityRegisterBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.viewmodel.RegisterViewModel;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        registerViewModel = new RegisterViewModel(this);
        binding.setRegisterViewmodel(registerViewModel);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        registerViewModel.release();

        super.onDestroy();
    }
}
