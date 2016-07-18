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
import com.bima.dokterpribadimu.view.fragments.OnboardingOpeningFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingPhotoFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingSocialFragment;

public class OnboardingActivity extends BaseActivity {

    private static final int ONBOARDING_OPENING_POSITION = 0;
    private static final int ONBOARDING_PHOTO_POSITION = 1;
    private static final int ONBOARDING_LIST_POSITION = 2;
    private static final int ONBOARDING_SOCIAL_POSITION = 3;
    private static final int ONBOARDING_COUNT = 4;

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
                binding.onboardingViewPager.setCurrentItem(ONBOARDING_COUNT - 1, true);
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
                switch (position) {
                    case ONBOARDING_OPENING_POSITION:
                        return OnboardingOpeningFragment.newInstance();
                    case ONBOARDING_PHOTO_POSITION:
                        return OnboardingPhotoFragment.newInstance();
                    case ONBOARDING_SOCIAL_POSITION:
                        return OnboardingSocialFragment.newInstance();
                    default:
                        return OnboardingOpeningFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return ONBOARDING_COUNT;
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
