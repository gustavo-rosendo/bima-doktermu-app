package com.bima.dokterpribadimu.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.bima.dokterpribadimu.data.remote.api.FileUploadApi;
import com.bima.dokterpribadimu.data.remote.service.FileUploadService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gusta_000 on 15/10/2016.
 */

public class FileUploadBackgroundService extends IntentService {
    private static final String TAG = FileUploadBackgroundService.class.getSimpleName();

    public static final String FILE_URL = "FILE_URL";
    public static final String CALL_ID = "CALL_ID";
    public static final String UNIQUE_FILE_NAME = "UNIQUE_FILE_NAME";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    FileUploadApi fileUploadApi;

    //Default constructor (obligatory)
    public FileUploadBackgroundService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String fileURL = intent.getExtras().getString(FILE_URL);
        final String callId = intent.getExtras().getString(CALL_ID);
        final String newFileName = intent.getExtras().getString(UNIQUE_FILE_NAME);
        final String accessToken = intent.getExtras().getString(ACCESS_TOKEN);

        Log.d(TAG, "Starting file upload...");
        Log.d(TAG, "fileURL = " + fileURL);
        Log.d(TAG, "callId = " + callId);
        Log.d(TAG, "newFileName = " + newFileName);
        Log.d(TAG, "accessToken = " + accessToken);

        //File creating from selected URL
        final File file = new File(fileURL);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part file_data        = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        MultipartBody.Part call_id          = MultipartBody.Part.createFormData("call_id", callId);
        MultipartBody.Part uniqueFileName   = MultipartBody.Part.createFormData("filename", newFileName);
        MultipartBody.Part access_token     = MultipartBody.Part.createFormData("access_token", accessToken);

        fileUploadApi = new FileUploadApi();
        FileUploadService fileUploadService = fileUploadApi.getFileUploadService();

        if(fileUploadService != null) {
            //Execute the request to upload the file
            Call<BaseResponse> call = fileUploadService.uploadFile(file_data, call_id, uniqueFileName, access_token);
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call,
                                       Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        Log.v(TAG, "Upload success: " + newFileName);
                    }
                    else {
                        try {
                            BaseResponse error = GsonUtils.fromJson(
                                    response.errorBody().string(),
                                    BaseResponse.class);
                            Log.e(TAG, "Upload error: " + newFileName + " - " + error.getMessage());
                            Log.e(TAG, "response.toString() = " + response.toString());
                            Log.e(TAG, "error.getData().toString() = " + error.getData().toString());
                            Exception ex = new Exception(error.getData().toString());
                            ex.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e(TAG, "Upload error: " + newFileName + " - " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
        else {
            Log.e(TAG, "Upload error: " + newFileName + " - fileUploadService == null");
        }
    }
}
