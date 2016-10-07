package com.bima.dokterpribadimu.data.remote.api;

import com.bima.dokterpribadimu.data.remote.base.BaseApi;
import com.bima.dokterpribadimu.data.remote.service.FileUploadService;
import com.bima.dokterpribadimu.model.BaseResponse;
import com.bima.dokterpribadimu.model.FileUploadResponse;
import com.bima.dokterpribadimu.utils.GsonUtils;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import okhttp3.MultipartBody;

/**
 * Created by Patrick Nugent on 07/10/2016.
 */
public class FileUploadApi extends BaseApi<FileUploadService> {

    private FileUploadService fileUploadService;

    public FileUploadApi() {
        super(BaseApi.API_TYPE_SERVER);
        this.fileUploadService = this.createService();
    }

    @Override
    protected Class getServiceClass() {
        return FileUploadService.class;
    }


    /**
     * FileUploadApi implementation to get call history
     * @return Observable<BaseResponse>
     */
    public Observable<BaseResponse<FileUploadResponse>> uploadFile(final MultipartBody.Part body, final String accessToken) {
        return Observable.create(new Observable.OnSubscribe<BaseResponse<FileUploadResponse>>() {
            @Override
            public void call(final Subscriber<? super BaseResponse<FileUploadResponse>> subscriber) {
                try {
                    Response response = fileUploadService.uploadFile(body, accessToken).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext((BaseResponse<FileUploadResponse>) response.body());
                        subscriber.onCompleted();
                    } else {
                        BaseResponse error = GsonUtils.fromJson(
                                response.errorBody().string(),
                                BaseResponse.class);
                        subscriber.onError(new Exception(error.getMessage()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
