package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by apradanas on 5/9/16.
 */
public class HomeItemViewModel extends BaseObservable {

    private int icon;
    private String title;
    private View.OnClickListener clickListener;

    public HomeItemViewModel(int icon, String title, View.OnClickListener clickListener) {
        this.icon = icon;
        this.title = title;
        this.clickListener = clickListener;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @Bindable
    public int getIcon() {
        return icon;
    }

    @Bindable
    public View.OnClickListener getClickListener() {
        return clickListener;
    }
}
