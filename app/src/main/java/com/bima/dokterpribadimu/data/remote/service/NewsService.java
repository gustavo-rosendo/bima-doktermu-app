package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apradanas on 5/11/16.
 */
public interface NewsService {

    @GET("/v3/news")
    Call<BaseResponse<NewsResponse>> getNews(
            @Query("news_category") String newsCategory,
            @Query("access_token") String accessToken
    );
}
