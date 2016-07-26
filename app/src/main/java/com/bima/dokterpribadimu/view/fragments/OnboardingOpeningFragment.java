package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingOpeningBinding;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingOpeningFragment extends BaseFragment {

    private static final String ONBOARDING = "onboarding";

    private FragmentOnboardingOpeningBinding binding;

    private Onboarding onboarding;

    public static OnboardingOpeningFragment newInstance(Onboarding onboarding) {
        Bundle bundle = new Bundle();
        bundle.putString(ONBOARDING, GsonUtils.toJson(onboarding));

        OnboardingOpeningFragment fragment = new OnboardingOpeningFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingOpeningBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        onboarding = GsonUtils.fromJson(getArguments().getString(ONBOARDING), Onboarding.class);

        Picasso.with(getActivity())
                .load(onboarding.getBackgroundImg())
                .into(binding.onboardingBackground);

        binding.onboardingTitleText.setText(onboarding.getTitle());
        binding.onboardingInfoText.setText(onboarding.getSubtitle());
    }

}
