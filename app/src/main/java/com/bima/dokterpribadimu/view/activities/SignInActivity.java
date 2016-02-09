package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivitySignInBinding;
import com.bima.dokterpribadimu.viewmodel.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        signInViewModel = new SignInViewModel(this);
        binding.setSignInViewmodel(signInViewModel);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        signInViewModel.release();

        super.onDestroy();
    }
}
