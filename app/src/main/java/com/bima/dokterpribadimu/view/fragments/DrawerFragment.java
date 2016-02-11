package com.bima.dokterpribadimu.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.data.remote.sns.LoginClient;
import com.bima.dokterpribadimu.data.remote.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.remote.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.FragmentDrawerBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.view.activities.HomeActivity;
import com.bima.dokterpribadimu.view.activities.LandingActivity;
import com.bima.dokterpribadimu.view.base.BaseFragment;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DrawerFragment extends BaseFragment {

    private static final String ACTIVE_DRAWER_TAG = "active_drawer";

    private OnDrawerItemPressed onDrawerItemPressedListener;

    private FragmentDrawerBinding binding;
    private int activeDrawer;

    public static DrawerFragment newInstance(int activeDrawer) {
        Bundle bundle = new Bundle();
        bundle.putInt(ACTIVE_DRAWER_TAG, activeDrawer);

        DrawerFragment fragment = new DrawerFragment();
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
        binding = FragmentDrawerBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        FacebookClient.release();

        super.onDestroy();
    }

    private void init() {
        initBundle();
        initViews();
    }

    private void initBundle() {
        activeDrawer = getArguments().getInt(ACTIVE_DRAWER_TAG);
    }

    private void initViews() {
        switch (activeDrawer) {
            case Constants.DRAWER_TYPE_HOME:
                binding.drawerHomeButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_DOCTOR_ON_CALL:
                binding.drawerDoctorOnCallButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_ABOUT:
                binding.drawerAboutBimaButton.setSelected(true);
                break;
        }

        binding.drawerHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_HOME);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_HOME) {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                }
            }
        });

        binding.drawerDoctorOnCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_DOCTOR_ON_CALL);
                }
            }
        });

        binding.drawerAboutBimaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_ABOUT);
                }
            }
        });

        binding.drawerSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_SIGN_OUT);
                }

                signOutFacebook();
                signOutGPlus();
                startActivity(new Intent(getActivity(), LandingActivity.class));
            }
        });
    }

    private void signOutFacebook() {
        LoginClient loginClient = FacebookClient.getInstance();
        loginClient.init(getActivity(), null);
        loginClient.signOut();
    }

    private void signOutGPlus() {
        LoginClient loginClient = GplusClient.getInstance();
        loginClient.init(getActivity(), null);
        loginClient.signOut();
    }

    public void setOnDrawerItemPressedListener(OnDrawerItemPressed onDrawerItemPressedListener) {
        this.onDrawerItemPressedListener = onDrawerItemPressedListener;
    }

    public interface OnDrawerItemPressed {
        void onDrawerItemPressed(int selectedDrawerType);
    }
}
