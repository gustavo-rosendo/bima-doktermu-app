package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.analytics.FirebaseAnalyticsHelper;
import com.bima.dokterpribadimu.data.sns.LoginClient;
import com.bima.dokterpribadimu.data.sns.facebook.FacebookClient;
import com.bima.dokterpribadimu.data.sns.gplus.GplusClient;
import com.bima.dokterpribadimu.databinding.FragmentDrawerBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class DrawerFragment extends BaseFragment {

    private static final String ACTIVE_DRAWER_TAG = "active_drawer";

    private OnDrawerItemPressed onDrawerItemPressedListener;

    private LoginClient loginClient;

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
        GplusClient.release();
        loginClient = null;

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_MENU_DRAWER);
    }

    @Override
    public void onStart() {
        super.onStart();
        loginClient.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        loginClient.onStop();
    }

    private void init() {
        initBundle();
        initViews();
        initLoginClient();
    }

    private void initBundle() {
        activeDrawer = getArguments().getInt(ACTIVE_DRAWER_TAG);
    }

    private void initViews() {
        switch (activeDrawer) {
            case Constants.DRAWER_TYPE_DOCTOR_ON_CALL:
                binding.drawerDoctorCallButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_PARTNERS:
                binding.drawerPartnersButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_NEWS:
                binding.drawerNewsButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_ABOUT:
                binding.drawerAboutBimaButton.setSelected(true);
                break;
            case Constants.DRAWER_TYPE_PROFILE:
                binding.drawerProfileButton.setSelected(true);
                break;
        }

        binding.drawerBimaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_HOME);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_HOME) {
                    IntentUtils.startHomeActivityOnTop(getActivity());
                }
            }
        });

        binding.drawerDoctorCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_DOCTOR_ON_CALL);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_DOCTOR_ON_CALL) {
                    startDoctorCallActivity();
                }
            }
        });

        binding.drawerPartnersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_PARTNERS);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_PARTNERS) {
                    IntentUtils.startPartnersActivity(getActivity());
                }
            }
        });

        binding.drawerNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_NEWS);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_NEWS) {
                    IntentUtils.startNewsActivity(getActivity());
                }
            }
        });

        binding.drawerAboutBimaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_ABOUT);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_ABOUT) {
                    startAboutActivity();
                }
            }
        });

        binding.drawerSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean subscriptionActive =
                        StorageUtils.getBoolean(getActivity(), Constants.KEY_USER_SUBSCIPTION, false);

                if (!subscriptionActive) {
                    if (onDrawerItemPressedListener != null) {
                        onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_SUBSCRIBE);
                    }

                    if (activeDrawer != Constants.DRAWER_TYPE_SUBSCRIBE) {
                        startSubscriptionActivity();
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            getString(R.string.dialog_already_subscribed_message),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.drawerProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDrawerItemPressedListener != null) {
                    onDrawerItemPressedListener.onDrawerItemPressed(Constants.DRAWER_TYPE_PROFILE);
                }

                if (activeDrawer != Constants.DRAWER_TYPE_PROFILE) {
                    startProfileActivity();
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

                //GUS - Fix issue #60: Phone number is not saved after log out
                //StorageUtils.remove(getActivity(), Constants.KEY_USER_PROFILE);

                startLandingActivityOnTop();
            }
        });
    }

    private void initLoginClient() {
        GplusClient.getInstance().init(getActivity(), null);
        loginClient = GplusClient.getInstance();
    }

    private void signOutFacebook() {
        loginClient = FacebookClient.getInstance();
        loginClient.init(getActivity(), null);
        loginClient.signOut();
    }

    private void signOutGPlus() {
        loginClient = GplusClient.getInstance();
        loginClient.signOut();
    }

    public void setOnDrawerItemPressedListener(OnDrawerItemPressed onDrawerItemPressedListener) {
        this.onDrawerItemPressedListener = onDrawerItemPressedListener;
    }

    public interface OnDrawerItemPressed {
        void onDrawerItemPressed(int selectedDrawerType);
    }
}
