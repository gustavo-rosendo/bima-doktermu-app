package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingOpeningBinding;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingPhotoBinding;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingPhotoFragment extends Fragment {

    private static final String ONBOARDING = "onboarding";

    private FragmentOnboardingPhotoBinding binding;

    private Onboarding onboarding;

    public static OnboardingPhotoFragment newInstance(Onboarding onboarding) {
        Bundle bundle = new Bundle();
        bundle.putString(ONBOARDING, GsonUtils.toJson(onboarding));

        OnboardingPhotoFragment fragment = new OnboardingPhotoFragment();
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
        binding = FragmentOnboardingPhotoBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        onboarding = GsonUtils.fromJson(getArguments().getString(ONBOARDING), Onboarding.class);

        Picasso.with(getActivity())
                .load(onboarding.getBackgroundImg())
                .into(binding.onboardingBackground);

        binding.onboardingInfoText.setText(onboarding.getSubtitle());
    }

}
