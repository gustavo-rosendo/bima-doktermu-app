package com.bima.dokterpribadimu.view.fragments;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.analytics.AnalyticsHelper;
import com.bima.dokterpribadimu.analytics.EventConstants;
import com.bima.dokterpribadimu.data.inappbilling.BillingClient;
import com.bima.dokterpribadimu.data.inappbilling.BillingInitializationListener;
import com.bima.dokterpribadimu.data.inappbilling.QueryInventoryListener;
import com.bima.dokterpribadimu.databinding.FragmentHomeBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.utils.UserProfileUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.viewmodel.HomeItemViewModel;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private static final int HOME_ITEM_COUNT = 4;

    private FragmentHomeBinding binding;
    private HomeListViewModel homeListViewModel = new HomeListViewModel();

    @Inject
    BillingClient billingClient;



    private int[] resIds = {
            R.drawable.ic_home_doctor, R.drawable.ic_home_partners, R.drawable.ic_home_news,
            R.drawable.ic_home_settings, R.drawable.ic_home_subscribe
    };

    private int[] stringIds = {
            R.string.drawer_doctor_on_call, R.string.drawer_partners, R.string.drawer_news,
            R.string.drawer_settings, R.string.drawer_subscribe
    };

    private View.OnClickListener[] clickListeners = {
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_DOCTOR_SCREEN_HOME);
                    startDoctorCallActivity();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_PARTNERS_SCREEN_HOME);
                    IntentUtils.startPartnersActivity(getActivity());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_NEWS_SCREEN_HOME);
                    IntentUtils.startNewsActivity(getActivity());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_PROFILE_SCREEN_HOME);
                    startProfileActivity();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AnalyticsHelper.logButtonClickEvent(EventConstants.BTN_SUBSCRIBE_SCREEN_HOME);
                    startSubscriptionActivity();
                }
            }
    };

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        binding.setViewModel(homeListViewModel);

        initHomeItem();
    }

    //Distinguish between initHomeItem() and updateHomeItem() to avoid blinking the screen every time
    private void initHomeItem() {
        homeListViewModel.items.clear();

        //Show home screen with "Settings" button as default
        for (int i = 0; i < HOME_ITEM_COUNT; i++) {
            homeListViewModel.items.add(
                    new HomeItemViewModel(
                            resIds[i],
                            getString(stringIds[i]),
                            clickListeners[i])
            );
        }

        // scroll to top
        binding.homeScrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.homeScrollView.scrollTo(0, 0);
            }
        });
    }

    private void updateHomeItem() {
        boolean subscriptionActive = billingClient.isSubscribedToDokterPribadiKu();

        // show "Subscribe" button if not yet subscribed
        if (!subscriptionActive) {
            // change from "Settings" to "Subscribe"
            int idSettingButton = HOME_ITEM_COUNT - 1;
            homeListViewModel.items.remove(idSettingButton);

            int idSubscribeButton = HOME_ITEM_COUNT;
            homeListViewModel.items.add(
                    new HomeItemViewModel(
                            resIds[idSubscribeButton],
                            getString(stringIds[idSubscribeButton]),
                            clickListeners[idSubscribeButton])
            );

            // scroll to top
            binding.homeScrollView.post(new Runnable() {
                @Override
                public void run() {
                    binding.homeScrollView.scrollTo(0, 0);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsHelper.logViewScreenEvent(EventConstants.SCREEN_HOME);

        initBillingClient();
    }

    @Override
    public void onPause() {
        billingClient.release();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        for (HomeItemViewModel itemViewModel : homeListViewModel.items) {
            itemViewModel.setClickListener(null);
        }
        homeListViewModel.items.clear();

        billingClient.release();

        super.onDestroy();
    }

    private void initBillingClient() {
        billingClient.setBillingInitializationListener(new BillingInitializationListener() {
            @Override
            public void onSuccess() {
                billingClient.queryInventoryAsync();
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.setQueryInventoryListener(new QueryInventoryListener() {
            @Override
            public void onSuccess(boolean isSubscribed) {
                StorageUtils.putBoolean(
                        getActivity(),
                        Constants.KEY_USER_SUBSCIPTION,
                        isSubscribed);

                updateHomeItem();
            }

            @Override
            public void onFailed() {
                // TODO: handle this
            }
        });

        billingClient.init(getActivity());
    }

    public static class HomeListViewModel {
        public final ObservableList<HomeItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.home_item_viewmodel, R.layout.item_home);
    }

}
