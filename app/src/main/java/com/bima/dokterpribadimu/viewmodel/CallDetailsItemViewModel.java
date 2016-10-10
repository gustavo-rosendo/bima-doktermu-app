package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bima.dokterpribadimu.model.Immunisation;
import com.bima.dokterpribadimu.model.Prescription;

/**
 * Created by gusta_000 on 10/10/2016.
 */

public class CallDetailsItemViewModel extends BaseObservable {
    private Prescription prescription;
    private Immunisation immunisation;

    public CallDetailsItemViewModel(Prescription prescription, Immunisation immunisation) {
        this.prescription = prescription;
        this.immunisation = immunisation;
    }

    @Bindable
    public String getPrescriptionName() {
        return prescription.getName();
    }

    @Bindable
    public String getPrescriptionDuration() {
        return prescription.getDuration();
    }

    @Bindable
    public String getImmunisationName() {
        return immunisation.getName();
    }

    public String getImmunisationRenewal() {
        return immunisation.getRenewal();
    }
}
