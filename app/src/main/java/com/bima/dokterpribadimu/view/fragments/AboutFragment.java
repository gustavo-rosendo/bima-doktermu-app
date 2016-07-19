package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.bima.dokterpribadimu.databinding.FragmentAboutBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.base.BaseFragment;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class AboutFragment extends BaseFragment {

    private FragmentAboutBinding binding;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DokterPribadimuApplication.getComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_ABOUT_BIMA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.aboutWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_WEB_SCREEN_ABOUT_BIMA);
                startViewIntent(Constants.BIMA_WEBSITE);
            }
        });

        binding.aboutCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_CSPHONE_SCREEN_ABOUT_BIMA);
                startDialIntent(getString(R.string.about_bima_phone_number));
            }
        });

        binding.aboutFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_FACEBOOK_SCREEN_ABOUT_BIMA);
                startFacebookIntent();
            }
        });

        binding.aboutTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_TWITTER_SCREEN_ABOUT_BIMA);
                startTwitterIntent();
            }
        });

        binding.aboutEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAnalyticsHelper.logButtonClickEvent(EventConstants.BTN_EMAIL_SCREEN_ABOUT_BIMA);
                startMailIntent();
            }
        });
    }

}
