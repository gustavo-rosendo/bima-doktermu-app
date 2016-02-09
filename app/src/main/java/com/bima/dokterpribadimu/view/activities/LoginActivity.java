package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityLoginBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        loginViewModel = new LoginViewModel(this);
        binding.setLoginViewmodel(loginViewModel);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        loginViewModel.release();

        super.onDestroy();
    }
}
