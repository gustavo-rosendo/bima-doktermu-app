package com.bima.dokterpribadimu.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.bima.dokterpribadimu.view.activities.HomeActivity;

/**
 * Created by apradanas on 2/9/16.
 */
public class SignInViewModel extends BaseObservable {

    private Activity activity;

    public SignInViewModel(Activity activity) {
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

    @Bindable
    public View.OnClickListener getSignInClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
            }
        };
    }
}
