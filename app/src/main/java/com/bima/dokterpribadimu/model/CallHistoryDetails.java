package com.bima.dokterpribadimu.model;

import com.bima.dokterpribadimu.utils.DateFormatterUtils;

import java.util.List;

/**
 * Created by gustavo.santos on 10/6/2016.
 */
public class CallHistoryDetails {
    String bookingTopic;
    String bookingSubtopic;
    String bookingCreated;
    String doctorId;
    String doctorName;
    String doctorPicture;
    String callSummary;
    List<Prescription> prescription;
    List<Immunisation> immunisation;
    String notes;
    List<Picture> pictures;

    public String getBookingTopic() {
        return bookingTopic;
    }

    public void setBookingTopic(String bookingTopic) {
        this.bookingTopic = bookingTopic;
    }

    public String getBookingSubTopic() {
        return bookingSubtopic;
    }

    public void setBookingSubTopic(String bookingSubTopic) {
        this.bookingSubtopic = bookingSubTopic;
    }

    public String getBookingCreated() {
        return DateFormatterUtils.getCallHistoryDisplayDate(bookingCreated);
    }

    public void setBookingCreated(String bookingCreated) {
        this.bookingCreated = bookingCreated;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPicture() {
        return doctorPicture;
    }

    public void setDoctorPicture(String doctorPicture) {
        this.doctorPicture = doctorPicture;
    }

    public String getCallSummary() {
        return callSummary;
    }

    public void setCallSummary(String callSummary) {
        this.callSummary = callSummary;
    }

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
