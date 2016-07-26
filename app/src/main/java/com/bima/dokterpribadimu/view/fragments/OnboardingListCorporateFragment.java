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
import com.bima.dokterpribadimu.databinding.FragmentOnboardingListCorporateBinding;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingListTestimonialBinding;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.model.OnboardingList;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.viewmodel.OnboardingCorporateItemViewModel;
import com.bima.dokterpribadimu.viewmodel.OnboardingTestimonialItemViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingListCorporateFragment extends Fragment {

    private static final String ONBOARDING = "onboarding";

    private FragmentOnboardingListCorporateBinding binding;

    private Onboarding onboarding;

    private CorporateListViewModel listViewModel = new CorporateListViewModel();

    public static OnboardingListCorporateFragment newInstance(Onboarding onboarding) {
        Bundle bundle = new Bundle();
        bundle.putString(ONBOARDING, GsonUtils.toJson(onboarding));

        OnboardingListCorporateFragment fragment = new OnboardingListCorporateFragment();
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
        binding = FragmentOnboardingListCorporateBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        binding.setViewModel(listViewModel);

        onboarding = GsonUtils.fromJson(getArguments().getString(ONBOARDING), Onboarding.class);

        List<OnboardingList> onboardingLists = onboarding.getListTemplate();
        if (onboardingLists != null) {
            for (OnboardingList onboardingList : onboardingLists) {
                listViewModel.items.add(new OnboardingCorporateItemViewModel(onboardingList));
            }
        }
    }

    public static class CorporateListViewModel {
        public final ObservableList<OnboardingCorporateItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.corporate_item_viewmodel, R.layout.item_onboarding_corporate);
    }

}
