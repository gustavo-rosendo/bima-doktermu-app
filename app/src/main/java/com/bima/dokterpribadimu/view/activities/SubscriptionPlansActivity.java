package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivitySubscriptionPlansBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;

/**
 * Created by gustavo.santos on 8/31/2016.
 */
public class SubscriptionPlansActivity extends BaseActivity {

    private static final String TAG = SubscriptionPlansActivity.class.getSimpleName();

    private ActivitySubscriptionPlansBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription_plans);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
