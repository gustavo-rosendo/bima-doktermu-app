package com.bima.dokterpribadimu.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.sns.LoginClient;
import com.bima.dokterpribadimu.data.remote.sns.LoginListener;
import com.bima.dokterpribadimu.data.remote.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.remote.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.ActivityLandingBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.facebook.appevents.AppEventsLogger;

public class LandingActivity extends BaseActivity {

    private ActivityLandingBinding binding;

    private LoginClient loginClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onDestroy() {
        FacebookClient.release();
        GplusClient.release();

        super.onDestroy();
    }

    private void init() {
        initLoginClient();
        initViews();
    }

    private void initViews() {
        binding.loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClient = FacebookClient.getInstance();
                loginClient.init(LandingActivity.this, loginListener);
                loginClient.signIn();
            }
        });

        binding.loginGplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClient = GplusClient.getInstance();
                loginClient.init(LandingActivity.this, loginListener);
                loginClient.signIn();
            }
        });

        binding.loginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInActivity();
            }
        });
    }

    private void initLoginClient() {
        loginClient = FacebookClient.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginClient.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginClient.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginClient.onActivityResult(requestCode, resultCode, data);
    }

    private LoginListener loginListener = new LoginListener() {
        @Override
        public void onSuccess() {
            startActivity(new Intent(LandingActivity.this, HomeActivity.class));
        }

        @Override
        public void onSignOut() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onCancel() {

        }
    };
}
