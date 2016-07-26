package com.bima.dokterpribadimu.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.data.remote.api.OnboardingApi;
import com.bima.dokterpribadimu.databinding.ActivityOnboardingBinding;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.model.OnboardingList;
import com.bima.dokterpribadimu.model.OnboardingResponse;
import com.bima.dokterpribadimu.model.UserProfile;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.bima.dokterpribadimu.view.fragments.OnboardingListCorporateFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingListTestimonialFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingOpeningFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingPhotoFragment;
import com.bima.dokterpribadimu.view.fragments.OnboardingSocialFragment;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OnboardingActivity extends BaseActivity {

    private static final int ONBOARDING_OPENING_TYPE = 1;
    private static final int ONBOARDING_PHOTO_TYPE = 2;
    private static final int ONBOARDING_LIST_TYPE = 3;
    private static final int ONBOARDING_SOCIAL_TYPE = 4;

    private static final int ONBOARDING_LIST_TESTIMONIAL_TYPE = 1;
    private static final int ONBOARDING_LIST_CORPORATE_TYPE = 2;

    private ActivityOnboardingBinding binding;

    @Inject
    OnboardingApi onboardingApi;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);

        DokterPribadimuApplication.getComponent().inject(this);

        getOnboarding();
    }

    /**
     * Get onboarding list
     */
    private void getOnboarding() {
        onboardingApi.getOnboarding()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<BaseResponse<OnboardingResponse>>bindToLifecycle())
                .subscribe(new Subscriber<BaseResponse<OnboardingResponse>>() {

                    @Override
                    public void onStart() {
                        showProgressDialog();
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();

                        UserProfile userProfile = GsonUtils.fromJson(
                                StorageUtils.getString(OnboardingActivity.this, Constants.KEY_USER_PROFILE, ""),
                                UserProfile.class
                        );

                        if (userProfile == null) {
                            IntentUtils.startLandingActivity(OnboardingActivity.this);
                        } else {
                            IntentUtils.startHomeActivityOnTop(OnboardingActivity.this);
                        }

                        finish();
                    }

                    @Override
                    public void onNext(BaseResponse<OnboardingResponse> onboardingResponse) {
                        setupViews(onboardingResponse.getData().getOnboarding());
                    }
                });
    }

    private void setupViews(final List<Onboarding> onboardings) {
        binding.onboardingSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onboardingViewPager.setCurrentItem(onboardings.size() - 1, true);
            }
        });

        binding.onboardingPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onboardingViewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        binding.onboardingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.onboardingViewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        binding.onboardingJumpInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfile userProfile = GsonUtils.fromJson(
                        StorageUtils.getString(OnboardingActivity.this, Constants.KEY_USER_PROFILE, ""),
                        UserProfile.class
                );

                if (userProfile == null) {
                    IntentUtils.startLandingActivity(OnboardingActivity.this);
                } else {
                    IntentUtils.startHomeActivityOnTop(OnboardingActivity.this);
                }

                finish();
            }
        });

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                Onboarding onboarding = onboardings.get(position);
                switch (Integer.parseInt(onboarding.getTemplateType())) {
                    case ONBOARDING_OPENING_TYPE:
                        return OnboardingOpeningFragment.newInstance(onboarding);
                    case ONBOARDING_PHOTO_TYPE:
                        return OnboardingPhotoFragment.newInstance(onboarding);
                    case ONBOARDING_LIST_TYPE:
                        List<OnboardingList> onboardingLists = onboarding.getListTemplate();
                        if (onboardingLists != null) {
                            if (Integer.parseInt(onboardingLists.get(0).getType())
                                    == ONBOARDING_LIST_TESTIMONIAL_TYPE) {
                                return OnboardingListTestimonialFragment.newInstance(onboarding);
                            } else {
                                return OnboardingListCorporateFragment.newInstance(onboarding);
                            }
                        } else {
                            return OnboardingListTestimonialFragment.newInstance(onboarding);
                        }
                    case ONBOARDING_SOCIAL_TYPE:
                        return OnboardingSocialFragment.newInstance(onboarding);
                    default:
                        return OnboardingOpeningFragment.newInstance(onboarding);
                }
            }

            @Override
            public int getCount() {
                return onboardings.size();
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

                binding.onboardingSkipButton.setVisibility(
                        position == 0 ? View.VISIBLE : View.GONE);
                binding.onboardingPreviousButton.setVisibility(
                        position == 0 ? View.GONE : View.VISIBLE);
                binding.onboardingNextButton.setVisibility(
                        position == onboardings.size() - 1 ? View.GONE : View.VISIBLE);
                binding.onboardingJumpInButton.setVisibility(
                        position == onboardings.size() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.onboardingViewPager.setAdapter(adapter);
        binding.onboardingIndicator.setViewPager(binding.onboardingViewPager);

        binding.onboardingLayout.setVisibility(View.VISIBLE);
    }

    public void openShareBottomSheet(Onboarding onboarding) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, onboarding.getShareText());
        shareIntent.setType("text/plain");
        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(
                this,
                shareIntent,
                onboarding.getShareBtnText(),
                new IntentPickerSheetView.OnIntentPickedListener() {
                    @Override
                    public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                        binding.onboardingBottomSheet.dismissSheet();
                        startActivity(activityInfo.getConcreteIntent(shareIntent));
                    }
        });
        // Filter out built in sharing options such as bluetooth and beam.
        intentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
            @Override
            public boolean include(IntentPickerSheetView.ActivityInfo info) {
                return !info.componentName.getPackageName().startsWith("com.android");
            }
        });

        binding.onboardingBottomSheet.showWithSheetView(intentPickerSheet);
    }
}
