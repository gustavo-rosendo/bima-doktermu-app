package com.bima.dokterpribadimu.data.remote.service;

import com.bima.dokterpribadimu.model.BaseResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Field;
/**
 * @author Patrick Nugent on 10/7/16.
 */
public interface FileUploadService {

    /*
    Retrofit get annotation with our URL
    And our method that will return us the List of Contacts
    */
    @Multipart
    @POST("/php_upload/upload.php")
    Call<BaseResponse> uploadFile(@Part MultipartBody.Part file);
}
