package com.bima.dokterpribadimu.view.fragments;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bima.dokterpribadimu.BR;
import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.FragmentMedicineInformationBinding;
import com.bima.dokterpribadimu.model.Immunisation;
import com.bima.dokterpribadimu.model.Prescription;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.view.base.BaseFragment;
import com.bima.dokterpribadimu.viewmodel.MedicineInformationViewModel;

import java.util.Arrays;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * A simple {@link BaseFragment} subclass.
 */
public class MedicineInformationFragment extends BaseFragment {

    private static final String PRESCRIPTION = "prescription";
    private static final String IMMUNISATION = "immunisation";
    private static final String IS_IMMUNISATION = "is_immunisation";

    private FragmentMedicineInformationBinding binding;

    private List<Prescription> prescriptions;
    private List<Immunisation> immunisations;
    private boolean isImmunisation;

    private MedicineInformationListViewModel medicineInformationListViewModel = new MedicineInformationListViewModel();

    public static MedicineInformationFragment newInstance(
            List<Prescription> prescription, List<Immunisation> immunisation, boolean isImmunisation) {
        Bundle bundle = new Bundle();
        bundle.putString(PRESCRIPTION, GsonUtils.toJson(prescription));
        bundle.putString(IMMUNISATION, GsonUtils.toJson(immunisation));
        bundle.putBoolean(IS_IMMUNISATION, isImmunisation);

        MedicineInformationFragment fragment = new MedicineInformationFragment();
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
        binding = FragmentMedicineInformationBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        try {
            prescriptions = Arrays.asList(GsonUtils.fromJson(getArguments().getString(PRESCRIPTION), Prescription[].class));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        try {
            immunisations = Arrays.asList(GsonUtils.fromJson(getArguments().getString(IMMUNISATION), Immunisation[].class));
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

        isImmunisation = getArguments().getBoolean(IS_IMMUNISATION);

        if (isImmunisation) {
            binding.medicineInfoDurationRenewal.setText(getString(R.string.medicine_renewal));
        }

        binding.setViewModel(medicineInformationListViewModel);

        if (prescriptions != null) {
            medicineInformationListViewModel.items.clear();
            for (Prescription prescription : prescriptions) {
                medicineInformationListViewModel.items.add(new MedicineInformationViewModel(prescription));
            }
        } else if (immunisations != null) {
            medicineInformationListViewModel.items.clear();
            for (Immunisation immunisation : immunisations) {
                medicineInformationListViewModel.items.add(new MedicineInformationViewModel(immunisation));
            }
        }
    }

    public static class MedicineInformationListViewModel {
        public final ObservableList<MedicineInformationViewModel> items = new ObservableArrayList<>();
        public final ItemView itemView = ItemView.of(BR.medicine_info_item_viewmodel, R.layout.item_medicine_information);
    }
}
