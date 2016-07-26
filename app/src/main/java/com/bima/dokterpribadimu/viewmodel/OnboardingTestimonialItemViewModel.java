package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bima.dokterpribadimu.model.OnboardingList;
import com.squareup.picasso.Picasso;

/**
 * Created by apradanas.
 */
public class OnboardingTestimonialItemViewModel extends BaseObservable {

    private OnboardingList onboardingList;

    public OnboardingTestimonialItemViewModel(OnboardingList onboardingList) {
        this.onboardingList = onboardingList;
    }

    @Bindable
    public String getTitle() {
        return onboardingList.getTitle();
    }

    @Bindable
    public String getDesciption() {
        return onboardingList.getDescription();
    }

    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView imageView, String url){
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

    @Bindable
    public String getImage() {
        return onboardingList.getImage();
    }
}
