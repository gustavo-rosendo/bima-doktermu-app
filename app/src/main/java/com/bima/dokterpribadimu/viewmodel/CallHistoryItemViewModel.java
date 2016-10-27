package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.bima.dokterpribadimu.model.BimaCall;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public class CallHistoryItemViewModel extends BaseObservable {
    private BimaCall call;
    private View.OnClickListener clickListener;

    public CallHistoryItemViewModel(BimaCall call, View.OnClickListener clickListener) {
        this.call = call;
        this.clickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Bindable
    public String getStatus() {
        return call.getBookingStatus();
    }

    @Bindable
    public String getTopicSubtopic() {
        String title;

        String subTopic = call.getBookingSubTopic();
        if(subTopic == null || subTopic == "") {
            title = call.getBookingTopic();
        }
        else {
            title = call.getBookingTopic() + " - " + subTopic;
        }

        return title;
    }

    @Bindable
    public String getDate() {
        return DateFormatterUtils.getCallHistoryDisplayDate(call.getBookingCreated());
    }

    @Bindable
    public View.OnClickListener getClickListener() {
        return clickListener;
    }
}
