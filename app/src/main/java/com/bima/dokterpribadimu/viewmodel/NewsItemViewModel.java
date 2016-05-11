package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apradanas on 5/11/16.
 */
public class NewsItemViewModel extends BaseObservable {

    private News news;
    private View.OnClickListener clickListener;

    public NewsItemViewModel(News news, View.OnClickListener clickListener) {
        this.news = news;
        this.clickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Bindable
    public String getTitle() {
        return news.getNewsTitle();
    }

    @Bindable
    public String getInfo() {
        return news.getNewsAuthor() + " - " + DateFormatterUtils.getNewsDisplayDate(news.getNewsDate());
    }

    @BindingAdapter({"android:src"})
    public static void setImageResource(ImageView imageView, String url){
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

    @Bindable
    public String getImage() {
        return news.getNewsImage();
    }

    @Bindable
    public View.OnClickListener getClickListener() {
        return clickListener;
    }
}
