package com.bima.dokterpribadimu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivitySignInBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.RegisterFragment;
import com.bima.dokterpribadimu.view.fragments.SignInFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SignInActivity extends BaseActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final String SIGN_IN = "sign_in";

    private ActivitySignInBinding binding;

    private Tracker mTracker;
    private boolean isSignIn;

    public static Intent create(Context context, boolean isSignIn) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(SIGN_IN, isSignIn);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initViews() {
        isSignIn = getIntent().getBooleanExtra(SIGN_IN, true);

        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isSignIn) {
            ft.replace(R.id.signin_frame, SignInFragment.newInstance()).commit();
            binding.toolbarTitle.setText(getString(R.string.sign_in));
        } else {
            ft.replace(R.id.signin_frame, RegisterFragment.newInstance()).commit();
            binding.toolbarTitle.setText(getString(R.string.register));
        }
    }
}
