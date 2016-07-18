package com.bima.dokterpribadimu.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityOnboardingBinding;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.AboutFragment;
import com.bima.dokterpribadimu.view.fragments.NewsFragment;

public class OnboardingActivity extends BaseActivity {

    private ActivityOnboardingBinding binding;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        binding.onboardingSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: remove hardcode
                binding.onboardingViewPager.setCurrentItem(3, true);
            }
        });

        binding.onboardingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onboardingViewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return AboutFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        binding.onboardingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.onboardingViewPager.setAdapter(adapter);
        binding.onboardingIndicator.setViewPager(binding.onboardingViewPager);
    }
}
