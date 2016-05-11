package com.bima.dokterpribadimu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.bima.dokterpribadimu.DokterPribadimuApplication;
import com.bima.dokterpribadimu.R;
import com.bima.dokterpribadimu.databinding.ActivityNewsDetailBinding;
import com.bima.dokterpribadimu.model.News;
import com.bima.dokterpribadimu.utils.DateFormatterUtils;
import com.bima.dokterpribadimu.utils.GsonUtils;
import com.bima.dokterpribadimu.view.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetailActivity extends BaseActivity {

    private static final String NEWS = "news";

    private ActivityNewsDetailBinding binding;

    private News news;

    public static Intent create(Context context, News news) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(NEWS, GsonUtils.toJson(news));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        DokterPribadimuApplication.getComponent().inject(this);

        init();
    }

    private void init() {
        news = GsonUtils.fromJson(getIntent().getExtras().getString(NEWS), News.class);

        initViews();
    }

    private void initViews() {
        binding.toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.newsTitle.setText(news.getNewsTitle());
        binding.newsInfo.setText(
                String.format(
                        "%s - %s",
                        news.getNewsAuthor(),
                        DateFormatterUtils.getNewsDisplayDate(news.getNewsDate())
                )
        );

        Picasso.with(this).load(news.getNewsImage()).into(binding.newsImage);

        binding.newsContent.setText(Html.fromHtml(news.getNewsContent()));
    }
}
