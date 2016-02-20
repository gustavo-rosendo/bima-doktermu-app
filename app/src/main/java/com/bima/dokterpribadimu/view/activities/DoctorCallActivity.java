package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityHomeBinding;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.HomeFragment;

public class DoctorCallActivity extends BaseActivity {

    private ActivityHomeBinding binding;
    private DrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    @Override
    protected void onDestroy() {
        drawerFragment.setOnDrawerItemPressedListener(null);

        super.onDestroy();
    }

    private void init() {
        initViews();
        setupDrawerFragment();
        setupHomeFragment();

        UserProfile userProfile = GsonUtils.fromJson(
                StorageUtils.getString(this, Constants.KEY_USER_PROFILE, ""),
                UserProfile.class);
        Toast.makeText(DoctorCallActivity.this, "Hello, " + userProfile.getName(), Toast.LENGTH_SHORT).show();
    }

    private void initViews() {
        binding.toolbarHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void setupDrawerFragment() {
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_HOME);
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

    private void setupHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, HomeFragment.newInstance())
                .commit();
    }
}