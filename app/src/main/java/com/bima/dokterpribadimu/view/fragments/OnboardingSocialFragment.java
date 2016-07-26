package com.bima.dokterpribadimu.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingSocialBinding;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.view.activities.OnboardingActivity;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingSocialFragment extends Fragment {

    private static final String ONBOARDING = "onboarding";

    private FragmentOnboardingSocialBinding binding;

    private Onboarding onboarding;

    public static OnboardingSocialFragment newInstance(Onboarding onboarding) {
        Bundle bundle = new Bundle();
        bundle.putString(ONBOARDING, GsonUtils.toJson(onboarding));

        OnboardingSocialFragment fragment = new OnboardingSocialFragment();
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
        binding = FragmentOnboardingSocialBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        onboarding = GsonUtils.fromJson(getArguments().getString(ONBOARDING), Onboarding.class);

        Picasso.with(getActivity())
                .load(onboarding.getBackgroundImg())
                .into(binding.onboardingBackground);

        binding.onboardingInfoText.setText(onboarding.getSubtitle());
        binding.onboardingBottomShareText.setText(onboarding.getShareBtnText());

        binding.onboardingBottomShareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnboardingActivity) getActivity()).openShareBottomSheet(onboarding);
            }
        });
    }

}
