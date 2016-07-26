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
import com.bima.dokterpribadimu.databinding.FragmentOnboardingListTestimonialBinding;
import com.bima.dokterpribadimu.model.Onboarding;
import com.bima.dokterpribadimu.model.OnboardingList;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.viewmodel.OnboardingTestimonialItemViewModel;

import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingListTestimonialFragment extends Fragment {

    private static final String ONBOARDING = "onboarding";

    private FragmentOnboardingListTestimonialBinding binding;

    private Onboarding onboarding;

    private TestimonialListViewModel listViewModel = new TestimonialListViewModel();

    public static OnboardingListTestimonialFragment newInstance(Onboarding onboarding) {
        Bundle bundle = new Bundle();
        bundle.putString(ONBOARDING, GsonUtils.toJson(onboarding));

        OnboardingListTestimonialFragment fragment = new OnboardingListTestimonialFragment();
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
        binding = FragmentOnboardingListTestimonialBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        binding.setViewModel(listViewModel);

        onboarding = GsonUtils.fromJson(getArguments().getString(ONBOARDING), Onboarding.class);

        List<OnboardingList> onboardingLists = onboarding.getListTemplate();
        if (onboardingLists != null) {
            for (OnboardingList onboardingList : onboardingLists) {
                listViewModel.items.add(new OnboardingTestimonialItemViewModel(onboardingList));
            }
        }
    }

    public static class TestimonialListViewModel {
        public final ObservableList<OnboardingTestimonialItemViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.testimonial_item_viewmodel, R.layout.item_onboarding_testimonial);
    }

}
