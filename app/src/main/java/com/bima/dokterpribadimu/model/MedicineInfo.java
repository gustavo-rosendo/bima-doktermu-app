package com.bima.dokterpribadimu.model;

import java.util.List;

/**
 * Created by apradanas.
 */
public class MedicineInfo {

    private List<Prescription> prescription;
    private List<Immunisation> immunisation;

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public List<Immunisation> getImmunisation() {
        return immunisation;
    }

    public void setImmunisation(List<Immunisation> immunisation) {
        this.immunisation = immunisation;
    }
}
