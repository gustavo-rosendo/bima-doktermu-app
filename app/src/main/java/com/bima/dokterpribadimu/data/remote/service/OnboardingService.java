package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.OnboardingResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by apradanas.
 */
public interface OnboardingService {

    @GET("/v3/onboarding")
    Call<BaseResponse<OnboardingResponse>> getOnboarding();
}
