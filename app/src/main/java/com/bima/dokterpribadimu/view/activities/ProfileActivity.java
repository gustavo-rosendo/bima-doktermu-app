package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityProfileBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.DrawerFragment;
import com.bima.dokterpribadimu.view.fragments.ProfileFragment;

public class ProfileActivity extends BaseActivity {

    private ActivityProfileBinding binding;
    private DrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

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
        setupProfileFragment();
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
        drawerFragment = DrawerFragment.newInstance(Constants.DRAWER_TYPE_PROFILE);
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

    private void setupProfileFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .replace(R.id.fragment_container, ProfileFragment.newInstance())
                .commit();
    }
}