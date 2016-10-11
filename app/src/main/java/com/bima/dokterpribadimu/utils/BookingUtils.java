package com.bima.dokterpribadimu.utils;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;

/**
 * Created by gustavo.santos on 5/16/2016.
 */
public class BookingUtils {

    public static final String TOPIC_REPRODUCTION = "reproduksi";
    public static final String TOPIC_HEALTH_ORGAN_DISEASE = "kesehatan_penyakit_organ";
    public static final String TOPIC_DIET_NUTRITION = "diet_nutrisi";
    public static final String TOPIC_INJURY = "cedera";
    public static final String TOPIC_OTHERS = "lain_lain";

    //-> Subtopics for Reproduction
    public static final String SUBTOPIC_PREGNANCY = "kehamilan_kesuburan";
    public static final String SUBTOPIC_BABY_CHILDREN_HEALTH = "kesehatan_bayi_anak";
    public static final String SUBTOPIC_HEALTH_SEXUAL_DISEASE = "kesehatan_penyakit_kelamin";

    //-> Subtopics for Health
    public static final String SUBTOPIC_INTERNAL_DISEASE = "penyakit_dalam";
    public static final String SUBTOPIC_BONE_DISEASE = "penyakit_tulang";
    public static final String SUBTOPIC_NEURAL_DISEASE = "penyakit_syaraf";
    public static final String SUBTOPIC_EYE_DISEASE = "penyakit_mata";
    public static final String SUBTOPIC_THT_DISEASE = "penyakit_tht";

    //Booking status
    public static final String STATUS_PENDING   = "pending";
    public static final String STATUS_CANCELLED = "canceled";
    public static final String STATUS_EXPIRED   = "expired";
    public static final String STATUS_FINISHED  = "completed";

    public static String getTopicSimpleName(String topic) {
        String topicSimpleName = "";

        String[] topicsArray = DokterPribadimuApplication.getInstance()
                .getResources().getStringArray(R.array.call_topics_arrays);
        if(topic.contentEquals(TOPIC_REPRODUCTION)) {
            topicSimpleName = topicsArray[0];
        }
        else if(topic.contentEquals(TOPIC_HEALTH_ORGAN_DISEASE)) {
            topicSimpleName = topicsArray[1];
        }
        else if(topic.contentEquals(TOPIC_DIET_NUTRITION)) {
            topicSimpleName = topicsArray[2];
        }
        else if(topic.contentEquals(TOPIC_INJURY)) {
            topicSimpleName = topicsArray[3];
        }
        else if(topic.contentEquals(TOPIC_OTHERS)) {
            topicSimpleName = topicsArray[4];
        }
        return topicSimpleName;
    }

    public static String getSubTopicSimpleName(String subTopic) {
        String subTopicSimpleName = "";

        String[] kesehatanSubtopicsArray = DokterPribadimuApplication.getInstance()
                .getResources().getStringArray(R.array.call_kesehatan_subtopics_arrays);
        String[] reproduksiSubtopicsArray = DokterPribadimuApplication.getInstance()
                .getResources().getStringArray(R.array.call_reproduksi_subtopics_arrays);

        if(subTopic.contentEquals(SUBTOPIC_PREGNANCY)) {
            subTopicSimpleName = reproduksiSubtopicsArray[0];
        }
        else if(subTopic.contentEquals(SUBTOPIC_BABY_CHILDREN_HEALTH)) {
            subTopicSimpleName = reproduksiSubtopicsArray[1];
        }
        else if(subTopic.contentEquals(SUBTOPIC_HEALTH_SEXUAL_DISEASE)) {
            subTopicSimpleName = reproduksiSubtopicsArray[2];
        }
        else if(subTopic.contentEquals(SUBTOPIC_INTERNAL_DISEASE)) {
            subTopicSimpleName = kesehatanSubtopicsArray[0];
        }
        else if(subTopic.contentEquals(SUBTOPIC_BONE_DISEASE)) {
            subTopicSimpleName = kesehatanSubtopicsArray[1];
        }
        else if(subTopic.contentEquals(SUBTOPIC_NEURAL_DISEASE)) {
            subTopicSimpleName = kesehatanSubtopicsArray[2];
        }
        else if(subTopic.contentEquals(SUBTOPIC_EYE_DISEASE)) {
            subTopicSimpleName = kesehatanSubtopicsArray[3];
        }
        else if(subTopic.contentEquals(SUBTOPIC_THT_DISEASE)) {
            subTopicSimpleName = kesehatanSubtopicsArray[4];
        }

        return subTopicSimpleName;
    }
}
