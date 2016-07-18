package com.bima.dokterpribadimu.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingPhotoBinding;
import com.bima.dokterpribadimu.databinding.FragmentOnboardingSocialBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingSocialFragment extends Fragment {

    private FragmentOnboardingSocialBinding binding;

    public static OnboardingSocialFragment newInstance() {
        OnboardingSocialFragment fragment = new OnboardingSocialFragment();
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
        // TODO: initialize content
    }

}
