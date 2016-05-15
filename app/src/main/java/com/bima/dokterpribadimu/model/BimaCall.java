package com.bima.dokterpribadimu.model;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public class BimaCall {

    private String bookingStatus;
    private String bookingCreated;
    private String bookingTopic;
    private String bookingSubTopic;

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingCreated() {
        return bookingCreated;
    }

    public String getBookingTopic() {
        return bookingTopic;
    }

    public String getBookingSubTopic() {
        return bookingSubTopic;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setBookingCreated(String bookingCreated) {
        this.bookingCreated = bookingCreated;
    }

    public void setBookingTopic(String bookingTopic) {
        this.bookingTopic = bookingTopic;
    }

    public void setBookingSubTopic(String bookingSubTopic) {
        this.bookingSubTopic = bookingSubTopic;
    }
}
