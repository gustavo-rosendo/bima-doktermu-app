package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by apradanas on 3/5/16.
 */
public interface UserService {

    @FormUrlEncoded
    @POST("/developer/v1/user/login")
    Call<BaseResponse<Token>> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("login_type") String loginType
    );

    @FormUrlEncoded
    @POST("/developer/v1/user/register")
    Call<BaseResponse<Token>> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("login_type") String loginType
    );
}
