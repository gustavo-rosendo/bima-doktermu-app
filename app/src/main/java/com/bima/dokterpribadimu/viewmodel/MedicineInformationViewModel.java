package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bima.dokterpribadimu.model.Immunisation;
import com.bima.dokterpribadimu.model.Prescription;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;

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
        String date = "";
        if(prescription != null) {
            date = DateFormatterUtils.getMedicineDisplayDate(prescription.getDate());
        }
        else if(immunisation != null) {
            date = DateFormatterUtils.getMedicineDisplayDate(immunisation.getDate());
        }

        return date;
    }

    @Bindable
    public String getName() {
        return prescription != null ? prescription.getName() : immunisation.getName();
    }

    @Bindable
    public String getDurationOrRenewal() {
        String durationOrRenewal = "";
        if(prescription != null) {
            durationOrRenewal = prescription.getDuration();
        }
        else if(immunisation != null) {
            durationOrRenewal = DateFormatterUtils.getRenewalDisplayDate(immunisation.getRenewal());
        }
        return durationOrRenewal;
    }
}
