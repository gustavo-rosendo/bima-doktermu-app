package com.bima.dokterpribadimu.view.activities;

import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityDoctorCallCompleteBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DoctorCallCompleteFragment;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;

public class DoctorCallCompleteActivity extends BaseActivity {

   private ActivityDoctorCallCompleteBinding binding;
    private DrawerFragment drawerFragment;

    private static final String TAG = DoctorCallCompleteActivity.class.getSimpleName();

//    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_call_complete);

        DokterPribadimuApplication.getComponent().inject(this);

        // Obtain the shared Tracker instance.
//        mTracker = DokterPribadimuApplication.getInstance().getDefaultTracker();

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.d(TAG, "Setting screen name: " + TAG);
//        mTracker.setScreenName("Image~" + TAG);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        drawerFragment.setOnDrawerItemPressedListener(null);

        super.onDestroy();
    }

    private void init() {
        initViews();
        setupDrawerFragment();
        setupDoctorCallCompleteFragment();
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_MENU_DRAWER);
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void setupDrawerFragment() {
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_DOCTOR_ON_CALL);
        drawerFragment.setOnDrawerItemPressedListener(new DrawerFragment.OnDrawerItemPressed() {
            @Override
            public void onDrawerItemPressed(int selectedDrawerType) {
                binding.drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_drawer, drawerFragment)
                .commit();
    }

    private void setupDoctorCallCompleteFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, DoctorCallCompleteFragment.newInstance())
                .commit();
    }
}
