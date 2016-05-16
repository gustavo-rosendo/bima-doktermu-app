package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gustavo.santos on 5/16/2016.
 */
public interface ProfileService {

    @GET("/v2/profile")
    Call<BaseResponse<ProfileResponse>> getProfileInfo(
            @Query("access_token") String accessToken
    );
}
