package com.bima.dokterpribadimu.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;
import com.squareup.picasso.Picasso;
import com.bima.dokterpribadimu.R;

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
        try{
            Picasso.with(imageView.getContext())
                    .load(url)
                    .error(R.drawable.ic_img_placeholder)
                    .placeholder(R.drawable.ic_img_placeholder)
                    .into(imageView);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
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
