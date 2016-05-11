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
import com.bima.dokterpribadimu.databinding.FragmentHomeBinding;
import com.bima.dokterpribadimu.utils.Constants;
import com.bima.dokterpribadimu.utils.IntentUtils;
import com.bima.dokterpribadimu.utils.StorageUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.viewmodel.HomeItemViewModel;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {

    private static final int HOME_ITEM_COUNT = 4;

    private FragmentHomeBinding binding;
    private HomeListViewModel homeListViewModel = new HomeListViewModel();

    private int[] resIds = {
            R.drawable.ic_home_doctor, R.drawable.ic_home_partners, R.drawable.ic_home_news,
            R.drawable.ic_home_subscribe, R.drawable.ic_home_settings
    };

    private int[] stringIds = {
            R.string.drawer_doctor_on_call, R.string.drawer_partners, R.string.drawer_news,
            R.string.drawer_subscribe, R.string.drawer_settings
    };

    private View.OnClickListener[] clickListeners = {
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startDoctorCallActivity();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.startPartnersActivity(getActivity());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentUtils.startNewsActivity(getActivity());
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startSubscriptionActivity();
                }
            },
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startProfileActivity();
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

        for (int i = 0; i < HOME_ITEM_COUNT; i++) {
            if (i == HOME_ITEM_COUNT - 1) {
                // show settings if already subscribed
                boolean subscriptionActive =
                        StorageUtils.getBoolean(getActivity(), Constants.KEY_USER_SUBSCIPTION, false);
                if (subscriptionActive) {
                    i++;
                }
            }

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

    @Override
    public void onDestroy() {
        for (HomeItemViewModel itemViewModel : homeListViewModel.items) {
            itemViewModel.setClickListener(null);
        }
        homeListViewModel.items.clear();

        super.onDestroy();
    }

    public static class HomeListViewModel {
        public final ObservableList<HomeItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.home_item_viewmodel, R.layout.item_home);
    }
}
