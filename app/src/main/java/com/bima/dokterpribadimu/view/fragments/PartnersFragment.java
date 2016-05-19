package com.bima.dokterpribadimu.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentPartnersBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.activities.PartnersLandingActivity;
import com.bima.dokterpribadimu.view.base.BaseFragment;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class PartnersFragment extends BaseFragment {

    private FragmentPartnersBinding binding;

    public static PartnersFragment newInstance() {
        PartnersFragment fragment = new PartnersFragment();
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
        binding = FragmentPartnersBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        final boolean subscriptionActive =
                StorageUtils.getBoolean(getActivity(), Constants.KEY_USER_SUBSCIPTION, false);

        if (subscriptionActive) {
            binding.partnersInfoText.setText(getString(R.string.partners_active_info));
            binding.partnersButton.setText(getString(R.string.partners_find));
        }

        binding.partnersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscriptionActive) {
                    IntentUtils.startPartnersLandingActivity(getActivity());
                } else {
                    startSubscriptionActivity();
                }
            }
        });
    }
}
