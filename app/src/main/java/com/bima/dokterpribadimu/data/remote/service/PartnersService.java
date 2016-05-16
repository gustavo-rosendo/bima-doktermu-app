package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.CategoriesResponse;
import com.bima.dokterpribadimu.model.NewsResponse;
import com.bima.dokterpribadimu.model.PartnerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by apradanas
 */
public interface PartnersService {

    @GET("/v2/partners/categories")
    Call<BaseResponse<CategoriesResponse>> getCategories(
            @Query("access_token") String accessToken
    );

    @GET("/v2/partners")
    Call<BaseResponse<PartnerResponse>> getPartners(
            @Query("partner_category") String partnerCategory,
            @Query("search_string") String searchString,
            @Query("access_token") String accessToken
    );
}

