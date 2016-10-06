package com.bima.dokterpribadimu.model;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.utils.BookingUtils;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public class BimaCall {

    private String callId;
    private String bookingStatus;
    private String bookingCreated;
    private String bookingTopic;
    private String bookingSubtopic;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingCreated() {
        return bookingCreated;
    }

    public String getBookingTopic() {
        return getTopicName(bookingTopic);
    }

    public String getBookingSubTopic() {
        return getSubtopicName(bookingSubtopic);
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

    public void setBookingSubTopic(String bookingSubtopic) {
        this.bookingSubtopic = bookingSubtopic;
    }

    private String getTopicName(String topicId) {
        if(topicId == null) {
            return "";
        }
        String topicName;

        String[] topicsArray = DokterPribadimuApplication.getInstance().getResources().getStringArray(R.array.call_topics_arrays);
        switch (topicId) {
            case BookingUtils.TOPIC_REPRODUCTION:
                topicName = topicsArray[0];
                break;
            case BookingUtils.TOPIC_HEALTH_ORGAN_DISEASE:
                topicName = topicsArray[1];
                break;
            case BookingUtils.TOPIC_DIET_NUTRITION:
                topicName = topicsArray[2];
                break;
            case BookingUtils.TOPIC_INJURY:
                topicName = topicsArray[3];
                break;
            case BookingUtils.TOPIC_OTHERS:
                topicName = topicsArray[4];
                break;
            default:
                topicName = "";
                break;
        }
        return topicName;
    }

    private String getSubtopicName(String subTopicId) {
        if(subTopicId == null) {
            return "";
        }

        String subTopicName;
        String[] subtopicsReproductionArray = DokterPribadimuApplication.getInstance().getResources().getStringArray(R.array.call_reproduksi_subtopics_arrays);
        String[] subtopicsHealthArray = DokterPribadimuApplication.getInstance().getResources().getStringArray(R.array.call_kesehatan_subtopics_arrays);
        switch (subTopicId) {
            //-> Subtopics for Reproduction:
            case BookingUtils.SUBTOPIC_PREGNANCY:
                subTopicName = subtopicsReproductionArray[0];
                break;
            case BookingUtils.SUBTOPIC_BABY_CHILDREN_HEALTH:
                subTopicName = subtopicsReproductionArray[1];
                break;
            case BookingUtils.SUBTOPIC_HEALTH_SEXUAL_DISEASE:
                subTopicName = subtopicsReproductionArray[2];
                break;
            //-> Subtopics for Health:
            case BookingUtils.SUBTOPIC_INTERNAL_DISEASE:
                subTopicName = subtopicsHealthArray[0];
                break;
            case BookingUtils.SUBTOPIC_BONE_DISEASE:
                subTopicName = subtopicsHealthArray[1];
                break;
            case BookingUtils.SUBTOPIC_NEURAL_DISEASE:
                subTopicName = subtopicsHealthArray[2];
                break;
            case BookingUtils.SUBTOPIC_EYE_DISEASE:
                subTopicName = subtopicsHealthArray[3];
                break;
            case BookingUtils.SUBTOPIC_THT_DISEASE:
                subTopicName = subtopicsHealthArray[4];
                break;
            default:
                subTopicName = "";
                break;
        }
        return subTopicName;
    }

}
