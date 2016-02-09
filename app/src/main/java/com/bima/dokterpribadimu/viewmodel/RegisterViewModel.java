package com.bima.dokterpribadimu.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

/**
 * Created by apradanas on 2/10/16.
 */
public class RegisterViewModel extends BaseObservable {

    private Activity activity;

    public RegisterViewModel(Activity activity) {
        this.activity = activity;
    }

    public void release() {
        activity = null;
    }

    @Bindable
    public View.OnClickListener getBackClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        };
    }
}
