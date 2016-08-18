package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bima.dokterpribadimu.model.Immunisation;
import com.bima.dokterpribadimu.model.Prescription;

/**
 * Created by apradanas.
 */
public class MedicineInformationViewModel extends BaseObservable {

    private Prescription prescription;
    private Immunisation immunisation;

    public MedicineInformationViewModel(Prescription prescription) {
        this.prescription = prescription;
    }

    public MedicineInformationViewModel(Immunisation immunisation) {
        this.immunisation = immunisation;
    }

    @Bindable
    public String getDate() {
        return prescription != null ? prescription.getDate() : immunisation.getDate();
    }

    @Bindable
    public String getName() {
        return prescription != null ? prescription.getName() : immunisation.getName();
    }

    @Bindable
    public String getDurationOrRenewal() {
        return prescription != null ? prescription.getDuration() : immunisation.getRenewal();
    }
}
